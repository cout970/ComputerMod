package com.cout970.computer.emulator;

import com.cout970.computer.api.IModuleCPU;
import com.cout970.computer.api.IModuleMMU;
import com.cout970.computer.api.IModuleRAM;
import com.cout970.computer.util.ComputerUtilsKt;
import jline.internal.Log;
import net.minecraft.nbt.NBTTagCompound;

public class ModuleCPU_MIPS implements IModuleCPU {

    //debug mode to see where the emulator fails
    protected boolean debug = true;
    protected IModuleRAM memory;
    protected IModuleMMU mmu;
    protected int[] registers = new int[32];
    protected int regHI = 0;
    protected int regLO = 0;
    protected int regPC = 0;
    //exceptions
    protected int regStatus = 0;
    protected int regCause = 0;
    protected int regEPC = 0;

    protected int enableMMU = 0;

    protected int cpuCycles = -1;

    public ModuleCPU_MIPS() {
    }

    @Override
    public boolean isRunning() {
        return cpuCycles >= 0;
    }

    @Override
    public void start() {
        cpuCycles = 0;
    }

    @Override
    public void stop() {
        cpuCycles = -1;
    }

    @Override
    public void reset() {
        cpuCycles = 0;
        memory.clear();
        regPC = 0x00400000;
        regStatus = 0x0000FFFF;
        regCause = 0;
        regEPC = 0;
        mmu.activeTranslation(true);
        for (int i = 0; i < getRegisterCount(); i++) {
            setRegister(i, 0);
        }
        debug = false;
    }

    @Override
    public void iterate() {
        if (cpuCycles >= 0) {
            if (debug) {
                cpuCycles += 1;
            } else {
                cpuCycles += 2000;//CPU clock speed / 20 ticks
            }
            if (cpuCycles > 10000) {//more cycles if there is any interruption like disk access
                cpuCycles = 10000;
            }

            while (cpuCycles > 0) {
                --cpuCycles;
                executeInstruction();
            }
        }
    }

    public void advancePC() {
        regPC = (regPC + 4);
    }

    public int getRegister(int t) {
        return registers[t];
    }

    public void setRegister(int s, int val) {
        if (s == 0) return;
        registers[s] = val;
    }

    @Override
    public int getRegisterCount() {
        return registers.length;
    }

    @Override
    public String getName() {
        return "MIPS_CPU";
    }

    private void executeInstruction() {
        int instruct = mmu.readInstruction(regPC);
        advancePC();
        switch (CONTROL(instruct)) {
            case R:
                TypeR(instruct);
                break;
            case J:
                TypeJ(instruct);
                break;
            case I:
                TypeI(instruct);
                break;
            case Exception:
                Exception(instruct);
            default:
                throwException(1);
        }
    }

    public enum InstructionType {R, I, J, Exception, NOP}

    public InstructionType CONTROL(int instruct) {
        if (instruct == 0)
            return InstructionType.NOP;                //no action
        int opcode = ComputerUtilsKt.getBitsFromInt(instruct, 26, 31, false);
        if (instruct == 0x0000000c || opcode == 0x10)
            return InstructionType.Exception;        //exception
        if (opcode == 0)
            return InstructionType.R;                //type R
        if (opcode == 0x2 || opcode == 0x3)
            return InstructionType.J;                //type J
        return InstructionType.I;                    //type I
    }

    public void TypeR(int instruct) {
        int rs, rt, rd, shamt, func;
        long m1, m2, mt;

        func = ComputerUtilsKt.getBitsFromInt(instruct, 0, 5, false);
        shamt = ComputerUtilsKt.getBitsFromInt(instruct, 6, 10, false);
        rd = ComputerUtilsKt.getBitsFromInt(instruct, 11, 15, false);
        rt = ComputerUtilsKt.getBitsFromInt(instruct, 16, 20, false);
        rs = ComputerUtilsKt.getBitsFromInt(instruct, 21, 25, false);
        if (debug) {
            String[] names = {
                    "sll", "unknow", "srl", "sra", "sllv", "unknow", "srlv", "srav", "jr", "jalr", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow",
                    "mfhi", "mthi", "mflo", "mtlo", "unknow", "unknow", "unknow", "unknow", "mult", "multu", "div", "divu", "unknow", "unknow", "unknow", "unknow",
                    "add", "addu", "sub", "subu", "and", "or", "xor", "nor", "slt", "sltu", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow",
                    "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow"};
            Log.debug(String.format("PC: %x, Type R: inst: %s, op: %d, shamt: %d, rd: %d, rt: %d, rs: %d, name: %s", regPC, Integer.toHexString(instruct), func, shamt, rd, rt, rs, names[func]));
        }
        switch (func) {

            case 0x0://sll
                setRegister(rd, getRegister(rt) << shamt);
                break;
            case 0x2://srl
                setRegister(rd, getRegister(rt) >>> shamt);
                break;
            case 0x3://sra
                setRegister(rd, getRegister(rt) >> shamt);
                break;
            case 0x4://sllv
                setRegister(rd, getRegister(rt) << rs);
                break;
            case 0x6://srlv
                setRegister(rd, getRegister(rt) >> rs);
                break;
            case 0x7://srav
                setRegister(rd, getRegister(rs) >>> rt);
                break;
            case 0x8://jr
                if (getRegister(rs) == 0 || getRegister(rs) == -1) {
                    throwException(6);
                    return;
                }
                regPC = getRegister(rs);
                break;
            case 0x9://jalr
                if (getRegister(rs) == -1 || getRegister(rs) == 0) {
                    throwException(6);
                }
                setRegister(rt, regPC);
                regPC = getRegister(rs);
                break;

            case 0x10://mfhi
                setRegister(rd, regHI);
                break;
            case 0x11://mthi
                regHI = getRegister(rd);
                break;
            case 0x12://mflo
                setRegister(rd, regLO);
                break;
            case 0x13://mtlo
                regLO = getRegister(rd);
                break;

            case 0x18://mult
                m1 = getRegister(rs);
                m2 = getRegister(rt);
                mt = m1 * m2;
                regLO = (int) mt;
                regHI = (int) (mt >> 32);
                break;
            case 0x19://multu
                m1 = getRegister(rs);
                m2 = getRegister(rt);
                m1 = (m1 << 32) >>> 32;
                m2 = (m2 << 32) >>> 32;
                mt = m1 * m2;
                regLO = (int) mt;
                regHI = (int) (mt >> 32);
                break;
            case 0x1a://div
                if (getRegister(rt) != 0) {
                    regLO = getRegister(rs) / getRegister(rt);
                    regHI = getRegister(rs) % getRegister(rt);
                } else {
                    throwException(2);
                }
                break;
            case 0x1b://divu
                m1 = getRegister(rs);
                m2 = getRegister(rt);
                m1 = (m1 << 32) >>> 32;
                m2 = (m2 << 32) >>> 32;
                if (m2 == 0) {
                    throwException(2);
                } else {
                    regLO = (int) (m1 / m2);
                    regHI = (int) (m1 % m2);
                }
                break;
            case 0x20://add
                setRegister(rd, getRegister(rt) + getRegister(rs));
                break;
            case 0x21://addu
                m1 = getRegister(rs);
                m2 = getRegister(rt);
                m1 = (m1 << 32) >>> 32;
                m2 = (m2 << 32) >>> 32;
                mt = m1 + m2;
                setRegister(rd, (int) mt);
                break;
            case 0x22://sub
                setRegister(rd, getRegister(rs) - getRegister(rt));
                break;
            case 0x23://subu
                m1 = getRegister(rs);
                m2 = getRegister(rt);
                m1 &= 0xFFFFFFFF;
                m2 &= 0xFFFFFFFF;
                mt = m1 - m2;
                mt &= 0xFFFFFFFF;
                setRegister(rd, (int) mt);
                break;
            case 0x24://and
                setRegister(rd, getRegister(rt) & getRegister(rs));
                break;
            case 0x25://or
                setRegister(rd, getRegister(rt) | getRegister(rs));
                break;
            case 0x26://xor
                setRegister(rd, getRegister(rt) ^ getRegister(rs));
                break;
            case 0x27://nor
                setRegister(rd, ~(getRegister(rt) | getRegister(rs)));
                break;
            case 0x2a://slt
                if (getRegister(rs) < getRegister(rt))
                    setRegister(rd, 1);
                else
                    setRegister(rd, 0);
                break;
            case 0x2b://sltu
                m1 = getRegister(rs);
                m2 = getRegister(rt);
                m1 = (m1 << 32) >>> 32;
                m2 = (m2 << 32) >>> 32;
                if (m1 < m2)
                    setRegister(rd, 1);
                else
                    setRegister(rd, 0);
                break;
            default:
                throwException(1);
                break;
        }
    }

    public void TypeJ(int instruct) {
        int dir = ComputerUtilsKt.getBitsFromInt(instruct, 0, 25, false);
        int code = ComputerUtilsKt.getBitsFromInt(instruct, 26, 31, false);
        if (debug) {
            Log.debug(String.format("PC: %x, Type J: inst: %s, op: %d, dir: %d, new dir: %d", regPC, Integer.toHexString(instruct), code, dir,
                    (regPC & 0xF0000000) | dir << 2));
        }
        switch (code) {
            case 0x2://j
                regPC &= 0xF0000000;
                regPC |= dir << 2;
                break;
            case 0x3://jal
                setRegister(31, regPC);
                regPC &= 0xF0000000;
                regPC |= dir << 2;
                break;
            default:
                throwException(1);
                break;
        }
    }

    private void TypeI(int instruct) {
        int opcode, rs, rt, inmed, inmedU;
        long m1, m2;

        opcode = ComputerUtilsKt.getBitsFromInt(instruct, 26, 31, false);
        rs = ComputerUtilsKt.getBitsFromInt(instruct, 21, 25, false);
        rt = ComputerUtilsKt.getBitsFromInt(instruct, 16, 20, false);
        inmed = ComputerUtilsKt.getBitsFromInt(instruct, 0, 15, true);
        inmedU = ComputerUtilsKt.getBitsFromInt(instruct, 0, 15, false);
        if (debug) {
            String[] names = {
                    "unknow", "bgez", "unknow", "unknow", "beq", "bne", "blez", "bgtz", "addi", "addiu", "slti", "sltiu", "andi", "ori", "xori", "lui",
                    "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "llo", "lhi", "trap", "unknow", "unknow", "unknow", "unknow", "unknow",
                    "lb", "lh", "unknow", "lw", "lbu", "lhu", "unknow", "unknow", "sb", "sh", "unknow", "sw", "unknow", "unknow", "unknow", "unknow", "unknow",
                    "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow", "unknow"};

            Log.debug(String.format("PC: %x, Type I: inst: %x, op: %d, rt: %d, rs: %d, inmed: %d, inmedU: %d, name: %s", regPC, instruct, opcode, rt, rs, inmed, inmedU, names[opcode]));
        }
        switch (opcode) {
            case 0x1:
                if (rt == 1) {//bgez
                    if (getRegister(rs) >= 0) {
                        regPC += (inmed << 2);
                    }
                } else if (rt == 0) {//bltz
                    if (getRegister(rs) < 0) {
                        regPC += (inmed << 2);
                    }
                }
                break;
            case 0x4://beq
                if (getRegister(rt) == getRegister(rs)) {
                    regPC += (inmed << 2);
                }
                break;
            case 0x5://bne
                if (getRegister(rt) != getRegister(rs)) {
                    regPC += (inmed << 2);
                }
                break;
            case 0x6://blez
                if (getRegister(rs) <= 0) {
                    regPC += (inmed << 2);
                }
                break;
            case 0x7://bgtz
                if (getRegister(rs) > 0) {
                    regPC += (inmed << 2);
                }
                break;
            case 0x8://addi
                setRegister(rt, getRegister(rs) + inmed);
                break;
            case 0x9://addiu
                setRegister(rt, getRegister(rs) + inmedU);
                break;
            case 0xa://slti
                if (getRegister(rs) < inmed)
                    setRegister(rt, 1);
                else
                    setRegister(rt, 0);
                break;
            case 0xb://sltiu
                m1 = getRegister(rs);
                m2 = inmedU;
                m1 = (m1 << 32) >>> 32;
                m2 = (m2 << 32) >>> 32;
                if (m1 < m2)
                    setRegister(rt, 1);
                else
                    setRegister(rt, 0);
                break;
            case 0xc://andi
                setRegister(rt, getRegister(rs) & inmedU);
                break;
            case 0xd://ori
                setRegister(rt, getRegister(rs) | inmedU);
                break;
            case 0xe://xori
                setRegister(rt, getRegister(rs) ^ inmedU);
                break;
            case 0xf://lui
                setRegister(rt, inmedU << 16);
                break;
            case 0x18://llo
                setRegister(rt, (getRegister(rt) & 0xFFFF0000) | inmedU);
                break;
            case 0x19://lhi
                setRegister(rt, (getRegister(rt) & 0x0000FFFF) | (inmedU << 16));
                break;
            case 0x1a://trap
                break;
            case 0x20://lb
                setRegister(rt, mmu.readByte(getRegister(rs) + inmed));
                break;
            case 0x21://lh
                setRegister(rt, (short) (mmu.readWord(getRegister(rs) + inmed)));
                break;
            case 0x23://lw
                if (((getRegister(rs) + inmed) & 0x3) != 0) {
                    throwException(5);
                    break;
                }
                setRegister(rt, mmu.readWord(getRegister(rs) + inmed));
                break;
            case 0x24://lbu
                setRegister(rt, mmu.readByte(getRegister(rs) + inmed) & 0xFF);
                break;
            case 0x25://lhu
                setRegister(rt, mmu.readWord(getRegister(rs) + inmed) & 0xFFFF);
                break;
            case 0x28://sb
                mmu.writeByte(getRegister(rs) + inmed, (byte) (getRegister(rt) & 0xFF));
                break;
            case 0x29://sh
                mmu.writeByte(getRegister(rs) + inmed, (byte) (getRegister(rt) & 0xFF));
                mmu.writeByte(getRegister(rs) + inmed + 1, (byte) (getRegister(rt) & 0xFF00));
                break;
            case 0x2b://sw
                if (((getRegister(rs) + inmed) & 0x3) != 0) {
                    throwException(5);
                    break;
                }
                mmu.writeWord(getRegister(rs) + inmed, getRegister(rt));
                break;
            default:
                throwException(1);
                break;
        }
    }


    public void Exception(int instruct) {
        if (instruct == 0x42000010) {//rfe return from exception
            regPC = regEPC;
            regStatus >>= 4;
            return;
        } else if (instruct == 0x0000000C) {
            throwException(3);
            return;
        }
        int code = ComputerUtilsKt.getBitsFromInt(instruct, 21, 25, false);
        int rt = ComputerUtilsKt.getBitsFromInt(instruct, 16, 20, false);
        int rd = ComputerUtilsKt.getBitsFromInt(instruct, 11, 15, false);
        if (code == 0x0) {//mfc0 rd, rt
            int val = 0;
            if (rt == 12) val = regStatus;
            if (rt == 13) val = regCause;
            if (rt == 14) val = regEPC;
            setRegister(rd, val);
            return;
        } else if (code == 0x4) {//mtc0 rd, rt
            int val = getRegister(rt);
            if (rt == 12) regStatus = val;
            if (rt == 13) regCause = val;
            if (rt == 14) regEPC = val;
            return;
        }
        throwException(1);
    }

    public void throwException(int flag) {
        if (debug) {
            regPC -= 4;
            Log.debug("Exception: " + flag + ", PC: " + Integer.toHexString(regPC) + ", Instruction: " + Integer.toHexString(mmu.readInstruction(regPC)));

            for (int i = -10; i < 10; i++) {
                StringBuilder builder = new StringBuilder();
                builder.append("PC: 0x").append(Integer.toHexString(regPC + i));
                if (i == 0) {
                    builder.append(" || ");
                } else {
                    builder.append(" :: ");
                }
                builder.append("0x").append(Integer.toHexString(mmu.readWord(regPC + i))).append(" ");
                Log.debug(builder.toString());
            }

            Log.debug("Registers: ");
            for (int i = 0; i < 32; i++) {
                Log.debug("$" + i + " " + Integer.toHexString(getRegister(i)));
            }
            stop();
        }
//        else {
//            if ((regStatus & (flag + 1)) == 0) {
//                return;
//            }
//            regCause = flag;
//            regEPC = regPC;
//            regStatus <<= 4;
//            regPC = 0x000;
//        }
    }

    @Override
    public void load(NBTTagCompound nbt) {
        registers = nbt.getIntArray("Regs");
        if (registers.length != 32) registers = new int[32];
        cpuCycles = nbt.getInteger("Cycles");
        regPC = nbt.getInteger("PC");
        regHI = nbt.getInteger("regHI");
        regLO = nbt.getInteger("regLO");
        regStatus = nbt.getInteger("Status");
        regCause = nbt.getInteger("Cause");
        regEPC = nbt.getInteger("EPC");
    }

    @Override
    public void save(NBTTagCompound nbt) {
        nbt.setIntArray("Regs", registers);
        nbt.setInteger("Cycles", cpuCycles);
        nbt.setInteger("PC", regPC);
        nbt.setInteger("regHI", regHI);
        nbt.setInteger("regLO", regLO);
        nbt.setInteger("Status", regStatus);
        nbt.setInteger("Cause", regCause);
        nbt.setInteger("EPC", regEPC);
    }

    @Override
    public int getRegPC() {
        return regPC;
    }

    @Override
    public void setRegPC(int value) {
        regPC = value;
    }

    @Override
    public IModuleRAM getMemory() {
        return memory;
    }

    @Override
    public void setMemory(IModuleRAM ram) {
        memory = ram;
    }

    @Override
    public IModuleMMU getMMU() {
        return mmu;
    }

    @Override
    public void setMMU(IModuleMMU mmu) {
        this.mmu = mmu;
        mmu.setCPU(this);
        mmu.setRAM(memory);
    }
}

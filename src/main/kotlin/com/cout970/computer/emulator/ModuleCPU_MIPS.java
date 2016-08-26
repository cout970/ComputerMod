package com.cout970.computer.emulator;

import com.cout970.computer.api.IExceptionCPU;
import com.cout970.computer.api.IModuleCPU;
import com.cout970.computer.api.IModuleMMU;
import com.cout970.computer.api.IModuleRAM;
import com.cout970.computer.emulator.exception.ArithmeticException;
import com.cout970.computer.emulator.exception.*;
import com.cout970.computer.emulator.exception.NullPointerException;
import com.cout970.computer.util.ComputerUtilsKt;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;

public class ModuleCPU_MIPS implements IModuleCPU {

    public static final String[] registerNames = {"z0", "at", "v0", "v1", "a0", "a1", "a2", "a3", "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "t8", "t9", "k0", "k1", "gp", "sp", "fp", "ra"};

    public static final int CPU_CLOCK = 2000;

    // /debug mode to see where the emulator fails
    protected boolean debug = false;

    protected IModuleRAM memory;
    protected IModuleMMU mmu;
    //cpu registers
    protected int[] registers = new int[32];
    protected int regHI = 0;
    protected int regLO = 0;
    protected int regPC = 0;
    //exceptions registers
    protected int regStatus = 0;
    protected int regCause = 0;
    protected int regEPC = 0;
    //PC used to read the current instruction
    protected int pfPC = 0;
    //used to implement delayed branch, if the value is -1 no jump should be performed,
    //otherwise the value should be the effective jump address
    protected int jump = -1;
    //counts the number of instructions to run this tick
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

        //every time the PC is reset, the mmu mode changes to kernel
        mmu.activeTranslation(false);

        //reset registers
        Arrays.fill(registers, 0);
        regHI = 0;
        regLO = 0;
        regPC = 0x00400;//1024
        regStatus = 0x0000FFFF;
        regCause = 0;
        regEPC = 0;

        //clears the memory
        memory.clear();
        debug = false;
    }

    @Override
    public void iterate() {
        if (cpuCycles >= 0) {
            if (debug) {
                cpuCycles += 1;
            } else {
                //CPU clock speed, indicates the amount of instructions to run per tick
                cpuCycles += CPU_CLOCK;
            }
            //more cycles if the CPU halts using sleep();
            if (cpuCycles > CPU_CLOCK * 10) {
                cpuCycles = CPU_CLOCK * 10;
            }

            //DEBUG to measure the performance of the cpu
//            long nanos = System.nanoTime();
//            int cycles = cpuCycles;

            while (cpuCycles > 0) {
                cpuCycles--;
                executeInstruction();
            }

//            nanos = System.nanoTime() - nanos;
//            log("Cycles: %d, Time: %10.1f(ns), %5.1f(micro seg) %.1f(ms), %.1f(s)", cycles, (float)nanos, (float)nanos/1000, (float)nanos/(1000*1000), (float)nanos/(1000*1000*1000));
        }
    }


    public int getRegister(int t) {
        return registers[t];
    }

    public void setRegister(int s, int val) {
        //reg Zero is hardwire to ground, so it always has the value 0
        if (s == 0) {
            return;
        }
//        if (debug) {
//            log("Reg: %s, change from 0x%08x (%d) to 0x%08x (%d)", registerNames[s], registers[s], registers[s], val, val);
//        }
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

    public enum InstructionType {R, I, J, EXCEPTION, COPROCESSOR, NOP}

    private void executeInstruction() {
        //FETCH INSTRUCTION
        pfPC = regPC;
        int instruct = mmu.readInstruction(regPC);
        //increment PC or perform a jump
        if (jump != -1) {
            regPC = jump;
            jump = -1;
        } else {
            regPC = (regPC + 4);
        }
        //DEBUG prints the instruction info in the log
        if (debug) {
            debugInst(instruct);
        }

        //DECODE INSTRUCTION
        int opcode = ComputerUtilsKt.getBitsFromInt(instruct, 26, 31, false);
        InstructionType type;

        if (instruct == 0) {
            type = InstructionType.NOP;              //no action
        } else if (instruct == 0x0000000c || opcode == 0x10) {
            type = InstructionType.EXCEPTION;        //exception/syscall/trap
        } else if ((opcode & 0x10) > 0) {
            type = InstructionType.COPROCESSOR;
        } else if (opcode == 0) {
            type = InstructionType.R;                //type R
        } else if (opcode == 0x2 || opcode == 0x3) {
            type = InstructionType.J;                //type J
        } else {
            type = InstructionType.I;                //type I
        }

        //EXECUTE INSTRUCTION and WRITEBACK
        if (type == InstructionType.R) {
            //aux vars used to calculate unsigned operations without having overflow
            long m1, m2, mt;

            int func = ComputerUtilsKt.getBitsFromInt(instruct, 0, 5, false);
            int shamt = ComputerUtilsKt.getBitsFromInt(instruct, 6, 10, false);
            int rd = ComputerUtilsKt.getBitsFromInt(instruct, 11, 15, false);
            int rt = ComputerUtilsKt.getBitsFromInt(instruct, 16, 20, false);
            int rs = ComputerUtilsKt.getBitsFromInt(instruct, 21, 25, false);

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
                        throwException(new NullPointerException());
                        return;
                    }
                    jump = getRegister(rs);
                    break;
                case 0x9://jalr
                    if (getRegister(rs) == -1 || getRegister(rs) == 0) {
                        throwException(new NullPointerException());
                    }
                    setRegister(rt, regPC);
                    jump = getRegister(rs);
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
                        throwException(new ArithmeticException());
                    }
                    break;
                case 0x1b://divu
                    m1 = getRegister(rs);
                    m2 = getRegister(rt);
                    m1 = (m1 << 32) >>> 32;
                    m2 = (m2 << 32) >>> 32;
                    if (m2 == 0) {
                        throwException(new ArithmeticException());
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
                    if (getRegister(rs) < getRegister(rt)) {
                        setRegister(rd, 1);
                    } else {
                        setRegister(rd, 0);
                    }
                    break;
                case 0x2b://sltu
                    m1 = getRegister(rs);
                    m2 = getRegister(rt);
                    m1 = (m1 << 32) >>> 32;
                    m2 = (m2 << 32) >>> 32;
                    if (m1 < m2) {
                        setRegister(rd, 1);
                    } else {
                        setRegister(rd, 0);
                    }
                    break;
                default:
                    throwException(new InvalidInstruction());
                    break;
            }
        } else if (type == InstructionType.J) {
            int dir = ComputerUtilsKt.getBitsFromInt(instruct, 0, 25, false);

            switch (opcode) {
                case 0x2://j
                    jump = regPC;
                    jump &= 0xF0000000;
                    jump |= dir << 2;
                    break;
                case 0x3://jal
                    setRegister(31, regPC);
                    jump = regPC;
                    jump &= 0xF0000000;
                    jump |= dir << 2;
                    break;
            }
        } else if (type == InstructionType.I) {
            //aux vars for unsigned operations
            long m1, m2;

            int rs = ComputerUtilsKt.getBitsFromInt(instruct, 21, 25, false);
            int rt = ComputerUtilsKt.getBitsFromInt(instruct, 16, 20, false);
            int inmed = ComputerUtilsKt.getBitsFromInt(instruct, 0, 15, true);
            int inmedU = ComputerUtilsKt.getBitsFromInt(instruct, 0, 15, false);

            switch (opcode) {
                case 0x1:
                    if (rt == 1) {//bgez
                        if (getRegister(rs) >= 0) {
                            jump = regPC + (inmed << 2);
                        }
                    } else if (rt == 0) {//bltz
                        if (getRegister(rs) < 0) {
                            jump = regPC + (inmed << 2);
                        }
                    }
                    break;
                case 0x4://beq
                    if (getRegister(rt) == getRegister(rs)) {
                        jump = regPC + (inmed << 2);
                    }
                    break;
                case 0x5://bne
                    if (getRegister(rt) != getRegister(rs)) {
                        jump = regPC + (inmed << 2);
                    }
                    break;
                case 0x6://blez
                    if (getRegister(rs) <= 0) {
                        jump = regPC + (inmed << 2);
                    }
                    break;
                case 0x7://bgtz
                    if (getRegister(rs) > 0) {
                        jump = regPC + (inmed << 2);
                    }
                    break;
                case 0x8://addi
                    setRegister(rt, getRegister(rs) + inmed);
                    break;
                case 0x9://addiu
                    setRegister(rt, getRegister(rs) + inmed);
                    break;
                case 0xa://slti
                    if (getRegister(rs) < inmed) {
                        setRegister(rt, 1);
                    } else {
                        setRegister(rt, 0);
                    }
                    break;
                case 0xb://sltiu
                    m1 = getRegister(rs);
                    m2 = inmedU;
                    m1 = (m1 << 32) >>> 32;
                    m2 = (m2 << 32) >>> 32;
                    if (m1 < m2) {
                        setRegister(rt, 1);
                    } else {
                        setRegister(rt, 0);
                    }
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
                        throwException(new WordBoundaryException());
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
                        throwException(new WordBoundaryException());
                        break;
                    }
                    mmu.writeWord(getRegister(rs) + inmed, getRegister(rt));
                    break;
                default:
                    throwException(new InvalidInstruction());
                    break;
            }
        } else if (type == InstructionType.EXCEPTION) {
            throwException(new Syscall());//syscall/trap
        } else if (type == InstructionType.COPROCESSOR) {//coprocessor instructions

            int code = ComputerUtilsKt.getBitsFromInt(instruct, 21, 25, false);
            int rt = ComputerUtilsKt.getBitsFromInt(instruct, 16, 20, false);
            int rd = ComputerUtilsKt.getBitsFromInt(instruct, 11, 15, false);

            if (code == 0x0) {//mfc0 rd, rt | move from coprocessor 0
                int val = 0;
                if (rt == 12) {
                    val = regStatus;
                }
                if (rt == 13) {
                    val = regCause;
                }
                if (rt == 14) {
                    val = regEPC;
                }
                setRegister(rd, val);
            } else if (code == 0x4) {//mtc0 rd, rt | move to coprocessor 0
                int val = getRegister(rt);
                if (rt == 12) {
                    regStatus = val;
                }
                if (rt == 13) {
                    regCause = val;
                }
                if (rt == 14) {
                    regEPC = val;
                }
            } else if (code == 0x10 && (instruct & 0b111111) == 0b010000) {//rfe | return from exception
                regPC = regEPC;
                regStatus >>= 4;
            } else {
                throwException(new InvalidInstruction());
            }
        }
    }


    private void debugInst(int instruct) {
        if (instruct == 0) {
            log("PC: 0x%08x \t NOP", pfPC);
            return;
        }

        int opcode = ComputerUtilsKt.getBitsFromInt(instruct, 26, 31, false);

        if (instruct == 0x0000000c || opcode == 0x10) {
            log("Exception: inst: 0x%08x, inst: %d", instruct, instruct);

        } else if (opcode == 0) {//type R

            String[] names = {"SLL  ", "UNKNOW", "SRL  ", "SRA  ", "SLLV ", "UNKNOW", "SRLV ", "SRAV ", "JR   ",
                    "JALR ", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "MFHI ", "MTHI ", "MFLO ",
                    "MTLO ", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "MULT ", "MULTU", "DIV  ", "DIVU ", "UNKNOW",
                    "UNKNOW", "UNKNOW", "UNKNOW", "ADD  ", "ADDU ", "SUB  ", "SUBU ", "AND  ", "OR   ", "XOR  ",
                    "NOR  ", "UNKNOW", "UNKNOW", "SLT  ", "SLTU ", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW",
                    "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW",
                    "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW"};

            int rs, rt, rd, shamt, func;
            func = ComputerUtilsKt.getBitsFromInt(instruct, 0, 5, false);
            shamt = ComputerUtilsKt.getBitsFromInt(instruct, 6, 10, false);
            rd = ComputerUtilsKt.getBitsFromInt(instruct, 11, 15, false);
            rt = ComputerUtilsKt.getBitsFromInt(instruct, 16, 20, false);
            rs = ComputerUtilsKt.getBitsFromInt(instruct, 21, 25, false);
            log("PC: 0x%08x \t %s $%s, $%s, $%s (%05d) \t Type R: inst: 0x%08x", pfPC, names[func], registerNames[rd], registerNames[rs], registerNames[rt], shamt, instruct);

        } else if (opcode == 0x2 || opcode == 0x3) {//type J

            String[] names = {"UNKNOW", "UNKNOW", "J    ", "JAL  "};
            int dir = ComputerUtilsKt.getBitsFromInt(instruct, 0, 25, false);
            log("PC: 0x%08x \t %s 0x%08x (0x%08x) \t Type J: inst: 0x%08x", pfPC, names[opcode], dir, (regPC & 0xF0000000) | dir << 2, instruct);

        } else {//type I

            String[] names = {"UNKNOW", "BGEZ ", "UNKNOW", "UNKNOW", "BEQ  ", "BNE  ", "BLEZ ", "BGTZ ", "ADDI ",
                    "ADDIU", "SLTI ", "SLTIU", "ANDI ", "ORI  ", "XORI ", "LUI  ", "UNKNOW", "UNKNOW", "UNKNOW",
                    "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "LLO  ", "LHI  ", "TRAP ", "UNKNOW", "UNKNOW",
                    "UNKNOW", "UNKNOW", "UNKNOW", "LB   ", "LH   ", "UNKNOW", "LW   ", "LBU  ", "LHU  ", "UNKNOW",
                    "UNKNOW", "SB   ", "SH   ", "UNKNOW", "SW   ", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW",
                    "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW",
                    "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW", "UNKNOW"};

            int rs, rt, inmed, inmedU;
            rs = ComputerUtilsKt.getBitsFromInt(instruct, 21, 25, false);
            rt = ComputerUtilsKt.getBitsFromInt(instruct, 16, 20, false);
            inmed = ComputerUtilsKt.getBitsFromInt(instruct, 0, 15, true);
            inmedU = ComputerUtilsKt.getBitsFromInt(instruct, 0, 15, false);
            log("PC: 0x%08x \t %s $%s, $%s, %05d (%05d) \t Type I: inst: 0x%08x", pfPC, names[opcode], registerNames[rs], registerNames[rt], inmed, inmedU, instruct);
        }
    }

    private void log(String s, Object... objs) {
        debug(String.format(s, objs));
    }


    private void debug(Object t) {
        System.out.println(String.valueOf(t));
    }

    @Override
    public void throwException(IExceptionCPU exception) {

//        if (debug) {
        log("Exception: %d (%s), regPC: 0x%08x, pfPC: 0x%08x, Description: %s", exception.getCode(), exception.getName(), regPC, pfPC, exception.getDescription());
        int inst = mmu.readInstruction(pfPC);
        debugInst(inst);

        debug("Registers: ");
        for (int i = 0; i < 32; i++) {
            log("\t %d: \t %s : 0x%08x (%08d)", i, registerNames[i], getRegister(i), getRegister(i));
        }
        stop();
//        } else {
//            int flag = exception.getCode();
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
    public void load(NBTTagCompound tag) {
        if (tag.hasKey("CPU")) {
            NBTTagCompound nbt = tag.getCompoundTag("CPU");
            registers = nbt.getIntArray("Regs");
            if (registers.length != 32) {
                registers = new int[32];
            }
            cpuCycles = nbt.getInteger("Cycles");
            regPC = nbt.getInteger("PC");
            regHI = nbt.getInteger("regHI");
            regLO = nbt.getInteger("regLO");
            regStatus = nbt.getInteger("Status");
            regCause = nbt.getInteger("Cause");
            regEPC = nbt.getInteger("EPC");
            jump = nbt.getInteger("Jump");
        }
    }

    @Override
    public void save(NBTTagCompound tag) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setIntArray("Regs", registers);
        nbt.setInteger("Cycles", cpuCycles);
        nbt.setInteger("PC", regPC);
        nbt.setInteger("regHI", regHI);
        nbt.setInteger("regLO", regLO);
        nbt.setInteger("Status", regStatus);
        nbt.setInteger("Cause", regCause);
        nbt.setInteger("EPC", regEPC);
        nbt.setInteger("Jump", jump);
        tag.setTag("CPU", nbt);
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

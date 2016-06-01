package com.cout970.computer.tileentity

import com.cout970.computer.api.*
import com.cout970.computer.emulator.Motherboard
import com.cout970.computer.emulator.PeripheralMonitor
import com.cout970.computer.network.*
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fml.relauncher.Side

/**
 * Created by cout970 on 20/05/2016.
 */
class TileOldComputer : TileBase(), IComputer, IServerButtonListener, IPeripheralProvider, IMessageStorage, ITickable {

    val motherboard = Motherboard(this)
    val monitor = object : PeripheralMonitor(this){
        override fun onKeyPressed(key: Int){
            super.onKeyPressed(key)
            sendMonitorDataToServer()
        }
    }

    override fun update() {
        if (!world.isRemote) {
            if (motherboard.isAssembled) {
                motherboard.iterate()
                markDirty()
            } else if (worldObj.totalWorldTime.toInt() % 40 == 0) {
                motherboard.assembly()
            }
        }
    }

    override fun getCPU(): IModuleCPU? {
        return motherboard.cpu
    }

    override fun getMemory(): IModuleRAM? {
        return motherboard.ram
    }

    override fun getModules(): Array<IModule?> {
        return arrayOf(motherboard.diskDrive)
    }

    override fun getPeripherals(): MutableList<IPeripheral?> = mutableListOf(monitor)


    override fun onPress(mouseButton: Int) {
        if (worldObj.isRemote) {
            val msg = MessageButtonClick(mouseButton, world.provider.dimension, position!!)
            ChannelManager.channel.sendToServer(msg)
        } else {
            if (motherboard.isAssembled) {
                if (mouseButton == 0) {
                    cpu!!.start()
                } else if (mouseButton == 1) {
                    cpu!!.reset()
                    motherboard.rom!!.loadBios(motherboard.ram)
                } else if (mouseButton == 2) {
                    cpu!!.stop()
                }
            }
        }
    }

    override fun getPosition(): BlockPos? = getPos()

    fun sendMonitorDataToServer() {
        if (world.isRemote) {
            val msg = MessageGenericUpdate(this, Side.SERVER, world.provider.dimension, position!!)
            ChannelManager.channel.sendToServer(msg)
        }
    }

    fun sendMonitorDataToClient(player: EntityPlayer) {
        if (!world.isRemote) {
            val msg = MessageGenericUpdate(this, Side.CLIENT, world.provider.dimension, position!!)
            ChannelManager.channel.sendTo(msg, player as EntityPlayerMP)
        }
    }

    override fun saveToMessage(buff: ByteBuf, processSide: Side) {
        if (processSide === Side.SERVER) {
            // saving client info
            monitor.saveToMessage(buff, processSide)
        } else {
            // saving server info
            buff.writeInt(monitor.cursorPos)
            buff.writeBytes(monitor.buffer)
        }
    }

    override fun loadFromMessage(buff: ByteBuf, processSide: Side) {
        if (processSide === Side.SERVER) {
            // reading client info
            monitor.loadFromMessage(buff, processSide)
        } else {
            // reading server info
            monitor.cursorPos = buff.readInt()
            buff.readBytes(monitor.buffer)
        }
    }

    override fun readFromNBT(data: NBTTagCompound) {
        super.readFromNBT(data)
        motherboard.deserializeNBT(data.getCompoundTag("inv"))
        monitor.load(data)
    }

    override fun writeToNBT(data: NBTTagCompound): NBTTagCompound {
        data.setTag("inv", motherboard.serializeNBT())
        monitor.save(data)
        return super.writeToNBT(data)
    }

    override fun hasCapability(capability: Capability<*>?, facing: EnumFacing?): Boolean {
        if(capability == IComputer.IDENTIFIER || capability == IPeripheralProvider.IDENTIFIER)
            return true
        return super.hasCapability(capability, facing)
    }

    override fun <T> getCapability(capability: Capability<T>?, facing: EnumFacing?): T? {
        if(capability == IComputer.IDENTIFIER || capability == IPeripheralProvider.IDENTIFIER)
            return this as T
        return super.getCapability(capability, facing)
    }
}
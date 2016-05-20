package com.cout970.computer.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * @author cout970
 */
public interface IComputer {

	@CapabilityInject(IComputer.class)
	Capability<IComputer> IDENTIFIER = null;

	/**
	 * The CPU of the computer
	 */
	IModuleCPU getCPU();

	/**
	 * The main memory of the computer
	 */
	IModuleRAM getMemory();

	/**
	 * The peripherals inside this computer
	 */
	IModule[] getModules();

	/**
	 * @return Gives the position of the block where the computer is.
	 */
	BlockPos getPosition();

	/**
	 * @return Gives the world where the computer is.
	 */
	World getWorld();

}

package com.cout970.computer.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import java.util.List;

/**
 * @author cout970
 */
public interface IPeripheralProvider {

	@CapabilityInject(IPeripheralProvider.class)
	Capability<IPeripheralProvider> IDENTIFIER = null;

	/**
	 * Returns the peripherals on this block, if there is no peripherals on this block,
	 * this should return an empty array
	 */
	List<IPeripheral> getPeripherals();
}

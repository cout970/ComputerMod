package com.cout970.computer.api;

/**
 * @author cout970
 */
public interface IModuleROM extends IModule {

	/**
	 * Loads the Bios into the RAM
	 */
	void loadBios(IModuleRAM ram);
}

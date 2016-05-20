package com.cout970.computer.api;

/**
 * @author cout970
 */
public interface IModuleGPU extends IModule {

	/**
	 * Called every GAME tick
	 */
	void iterate();

	/**
	 * Called every render tick in the GUI
	 */
	void renderTick();
}

package com.cout970.computer.api;

/**
 * @author cout970
 */
public interface IModuleCPU extends IModule {

	/**
	 * Called every GAME tick
	 */
	void iterate();

	/**
	 * @return If the CPU clock is running
	 */
	boolean isRunning();

	/**
	 * Starts the CPU clock
	 */
	void start();

	/**
	 * Stops the CPU clock
	 */
	void stop();

	/**
	 * Clears the memory and the registers
	 */
	void reset();

	/**
	 * Returns the value in a specific register, the register PC is not included
	 */
	int getRegister(int reg);

	void setRegister(int reg, int value);

	/**
	 * Returns the amount of registers in the CPU, the register PC is not included
	 */
	int getRegisterCount();

	/**
	 * returns the value of the register PC
	 */
	int getRegPC();

	void setRegPC(int value);

	/**
	 * Returns the ram module that the CPU uses to load and store values
	 */
	IModuleRAM getMemory();

	void setMemory(IModuleRAM ram);

	/**
	 * Returns the module that translate virtual memory addreses to phisical addresses
	 */
	IModuleMMU getMMU();

	void setMMU(IModuleMMU mmu);


	void throwException(IExceptionCPU exception);
}

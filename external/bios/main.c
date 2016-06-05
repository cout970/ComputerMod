//
// Created by cout970 on 21/05/2016.
//
#include "../drivers/floppydrive.h"
#include "../drivers/harddrive.h"
#include "../drivers/monitor.h"
#include "../lib/stdio.h"
#include "../lib/system.h"

#define BLOCK_SIZE 0x1000

void search(FloppyDrive **disk, HardDrive **harddrive);
void loadFloppyDisk(FloppyDrive *disk, byte *mem);
void loadHardDrive(HardDrive *disk, byte *mem);
void printContent(Monitor *mon, byte *mem);
void boot();

//sets the stack pointer and jumps to main
asm (
		"   la $sp, 0x6000\n"
		"   j main\n"
		"	nop\n"
);

void main() {

	//variables
	Monitor *mon; 					//primary monitor
	FloppyDrive *disk = NULL; 		//floppy disk drive
	HardDrive *harddrive = NULL;	//hard drive
	//aux variable
	int c, searchD = 1;
	//pointer to the address where to dump the disk content
	byte *mem = (byte *) 0x2000;

	//code start

	//gets the default monitor
	mon = getMonitor(DEFAULT_MONITOR_ADDR);

	//clears the screen
	memset(mon->buffer, 32, mon->size);

	//reset the cursor
	setCursorMark(mon, 0);
	setCursor(mon, 0);

	//prints data for the user, to know if the bios works or not
	printf(" -> Booting...\n");

	printf("Searching devices...\n");

	search(&disk, &harddrive);

	//load from hard drive
	if (harddrive != NULL) {
		loadHardDrive(harddrive, mem);
		boot();
	}

	while (1) {

		//clears the screen
		memset(mon->buffer, 32, mon->size);
		//reset the cursor
		setCursorMark(mon, 0);
		setCursor(mon, 0);

		if (searchD) {
			searchD = 0;
			printf("Searching devices...\n");
			search(&disk, &harddrive);
		}

		//printing avalible options
		printf("\nOptions:\n");
		if (disk != NULL) {
			printf(" A: Boot using Floppy disk\n");
			printf(" B: Dump Floppy disk content\n");
		}

		if (harddrive != NULL) {
			printf(" C: Boot using Hard drive\n");
			printf(" D: Dump Hard drive content\n");
		}

		printf(" E: Refresh disks\n");
		printf(" F: Shutdown\n");
		printf("\n");

		printf("Option: ");
		//reseting the key buffer
		mon->keyBufferSize = 0;
		//waits until the user press a key
		c = getKey(mon);
		//jumps to other line te avoid conflicts with others printf
		printf("\n");

		switch (c) {
		case 'a':
		case 'A':
			if (disk == NULL) {
				printf("Invalid option\n");
			} else {
				loadFloppyDisk(disk, mem);
				boot();
			}
			break;
		case 'b':
		case 'B':
			if (disk == NULL) {
				printf("Invalid option\n");
				continue;
			} else {
				loadFloppyDisk(disk, mem);
				printContent(mon, mem);
				//reseting the key buffer
				mon->keyBufferSize = 0;
				//waits until the user press a key
				getKey(mon);
			}
			break;
		case 'c':
		case 'C':
			if (harddrive == NULL) {
				printf("Invalid option\n");
				continue;
			} else {
				loadHardDrive(harddrive, mem);
				boot();
			}
			break;
		case 'd':
		case 'D':
			if (harddrive == NULL) {
				printf("Invalid option\n");
				continue;
			} else {
				loadHardDrive(harddrive, mem);
				printContent(mon, mem);
				//reseting the key buffer
				mon->keyBufferSize = 0;
				//waits until the user press a key
				getKey(mon);
			}
			break;
		case 'e':
		case 'E':
			searchD = 1;
			break;
		case 'f':
		case 'F':
			printf("Shutdown...\n");
			exit(0);

			break;
		default:
			printf("Invalid option\n");
		}
	}
}

void search(FloppyDrive **disk, HardDrive **harddrive) {

	//searching for a hard drive
	*harddrive = getHardDrive(DEFAULT_HARD_DRIVE_ADDR);

	if (*harddrive != NULL) {
		printf("Found: Hard drive\n");
	} else {
		printf("Hard drive not found\n");
		*harddrive = NULL;
	}

	//searching for a floppy disk
	*disk = getFloppyDrive(DEFAULT_FLOPPY_DRIVE_ADDR);

	if (*disk != NULL) {
		printf("Found: Floppy disk drive\n");
		if (FloppyDrive_hasDisk(*disk)) {
			printf("Founded Floppy disk in the drive\n");
		} else {
			printf("The drive is empty\n");
			*disk = NULL;
		}
	} else {
		printf("Floppy disk drive not found\n");
		*disk = NULL;
	}
}

void loadFloppyDisk(FloppyDrive *disk, byte *mem) {
	printf("Loading disk...\n");

	FloppyDrive_seekBlock(disk, 0);
	FloppyDrive_readBlock(disk);
	FloppyDrive_copyFromBuffer(disk, mem, BLOCK_SIZE, 0, 0);

	printf("Disk loaded\n");
}

void loadHardDrive(HardDrive *disk, byte *mem) {
	printf("Loading hard drive...\n");

	HardDrive_seekBlock(disk, 0);
	HardDrive_readBlock(disk);
	HardDrive_copyFromBuffer(disk, mem, BLOCK_SIZE, 0, 0);

	printf("Hard drive loaded\n");
}

void printContent(Monitor *mon, byte *mem) {
	//aux values
	int i, j, next = 0, current;
	//hexadecimal characters
	char hex[] = "0123456789ABCDEF";
	//buffer to print in the screen
	char buffer[80];

	memset(buffer, 32, 80);

	for (j = 0; j < 32; j++) {
		for (i = 0; i < 32; i += 3) {
			current = mem[next++];
			buffer[4 + i] = *(hex + (current & 0xF));
			buffer[4 + i + 1] = *(hex + ((current & 0xF0) >> 4));
			buffer[4 + i + 2] = ' ';
			//if current is a control character
			if (current <= ' ') {
				current = '.';
			}
			buffer[40 + i] = current;
		}
		for (i = 0; i < 80; i++) {
			mon->buffer[i + (j + 10) * 80] = buffer[i];
		}
	}
}

void boot() {
	printf("Jumping to boot...\n");

	//sets the stack pointer and jumps to the boot code
	asm volatile(
			"   la $sp, 0x6000\n"
			"   j 0x2000\n"
			"   nop\n"
	);
}

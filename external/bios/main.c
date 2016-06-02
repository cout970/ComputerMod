//
// Created by cout970 on 21/05/2016.
//
#include "../drivers/monitor.h"
#include "../drivers/diskdrive.h"
#include "../lib/stdio.h"

#define BLOCK_SIZE 0x1000

void search(Diskdrive **disk, Diskdrive **harddrive);
void load(Diskdrive *disk, byte *mem);
void printContent(byte *mem);
void boot();

//sets the stack pointer and jumps to main
asm (
		"   la $sp, 0x6000\n"
		"   j main\n"
);

void main() {

	//variables
	Monitor *mon; 					//primary monitor
	Diskdrive *disk = NULL; 		//floppy disk drive
	//TODO make a custom driver for the hard drive
	Diskdrive *harddrive = NULL;	//hard drive
	//aux variable
	int c;
	//pointer to the address where to dump the disk content
	byte *mem = (byte *) 0x2000;

	//code start

	//gets the default monitor
	mon = getMonitor(DEFAULT_MONITOR_ADDR);

	//clears the screen
	memset(mon->buffer, 32, mon->size);

	//reset the cursor
	setCursorMark(mon, 0);

	//there is some bug in printf and the first char in the screen is not printed
	putchar(32);
	printf("\n");
	//prints data for the user, to know if the bios works or not
	printf("Booting...\n");

	printf("Searching devices...\n");

	search(&disk, &harddrive);

	//printing avalible options
	printf("\nOptions:\n");
	if (disk != NULL) {
		printf(" A: Boot Disk\n");
		printf(" B: Dump disk content\n");
	}

	if (harddrive != NULL) {
		printf(" C: Boot Hard drive\n");
		printf(" D: Dump disk content\n");
	}

	printf(" E: Refresh disks\n");
	printf(" F: Clear\n");
	printf(" G: Shutdown\n");
	printf("\n");

	while (1) {
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
				load(disk, mem);
				boot();
			}
			break;
		case 'b':
		case 'B':
			if (disk == NULL) {
				printf("Invalid option\n");
				continue;
			} else {
				load(disk, mem);
				printContent(mem);
			}
			break;
		case 'c':
		case 'C':
			if (harddrive == NULL) {
				printf("Invalid option\n");
				continue;
			} else {
				load(harddrive, mem);
				boot();
			}
			break;
		case 'd':
		case 'D':
			if (harddrive == NULL) {
				printf("Invalid option\n");
				continue;
			} else {
				load(harddrive, mem);
				printContent(mem);
			}
			break;
		case 'e':
		case 'E':
			printf("Searching devices...\n");
			search(&disk, &harddrive);
			break;
		case 'f':
		case 'F':
			//clears the screen
			memset(mon->buffer, 32, mon->size);

			//reset the cursor
			setCursorMark(mon, 0);
			break;
		case 'g':
		case 'G':
			printf("Shutdown...");
			asm volatile (
					"	la $v0, 10\n"
					"	syscall\n"
			);
			break;
		default:
			printf("Invalid option\n");
		}
	}
}

void search(Diskdrive **disk, Diskdrive **harddrive) {
	//searching for a floppy disk
	*disk = getDiskDrive(DEFAULT_FLOPPY_DRIVE_ADDR);

	if ((*disk)->active && (*disk)->id == FLOPPY_DRIVE_ID) {
		printf("Found: Floppy disk drive\n");
		if (hasDisk(*disk)) {
			printf("Founded Floppy disk in the drive\n");
		} else {
			printf("The drive is empty\n");
			*disk = NULL;
		}
	} else {
		printf("Floppy disk drive not found\n");
		*disk = NULL;
	}

	//searching for a hard drive
	*harddrive = getDiskDrive(DEFAULT_HARD_DRIVE_ADDR);

	if ((*harddrive)->active && (*harddrive)->id == HARD_DRIVE_ID) {
		printf("Found: Hard drive\n");
	} else {
		printf("Hard drive not found\n");
		*harddrive = NULL;
	}
}

void load(Diskdrive *disk, byte *mem) {
	printf("Loading disk...\n");

	seekBlock(disk, 0);
	readBlock(disk);
	copyFromBuffer(disk, mem, BLOCK_SIZE, 0, 0);

	printf("Disk loaded\n");
}

void printContent(byte *mem) {
	//aux values
	int i, j, next = 0, current;
	//hexadecimal characters
	char hex[] = "0123456789ABCDEF";
	//buffer to print in the screen
	char buffer[80];

	memset(buffer, 32, 80);

	for (j = 0; j < 20; j++) {
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
		printf(buffer);
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

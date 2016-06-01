//
// Created by cout970 on 21/05/2016.
//
#include "../drivers/monitor.h"
#include "../drivers/diskdrive.h"

#define BLOCK_SIZE 0x1000


void print(Monitor *mon, char *str, int offset);

asm (
"   la $sp, 0x6000\n"
"   j main\n"
);


void main() {

    Monitor *mon;
    Diskdrive *disk;
    int i, j, c = 0, a, b, b1, b2, e;
    uint8_t *mem = (uint8_t *) 0x2000;
    int size;
    int offset;
    char alphanumeric[] = "0123456789ABCDEF";

    mon = getMonitor(DEFAULT_MONITOR_ADDR);

    int g = mon->id + 1;

    size = mon->lines * mon->columns;

    //se limpia la pantalla
    for (i = 0; i < size; i++) {
        mon->buffer[i] = 32; //space
        *(&mon->cursor) = i;
    }

    print(mon, "Booting...", mon->columns * c++);

    print(mon, "Searching for disk...", mon->columns * c++);
    disk = getDiskDrive(DEFAULT_DISKDRIVE_ADDR);

    if (disk == NULL) {
        print(mon, "Disk not found.", mon->columns * c++);
        return;
    }
    if (!hasDisk(disk)) {
        while (!hasDisk(disk)) {
            print(mon, "Please insert a disk ", mon->columns * c);
			print(mon, "Please insert a disk.", mon->columns * c);
        }
        c++;
    }

    print(mon, "Loading disk...", mon->columns * c++);
    seekBlock(disk, 0);
    readBlock(disk);
    copyFromBuffer(disk, mem, BLOCK_SIZE, 0, 0);
    print(mon, "Disk loading done.", mon->columns * c++);
	
    print(mon, "Printing memory dump at 0x2000:", mon->columns * c++);
    c++;
    //DEBUG
    a = 0;
    e = 0;
    for (j = 0; j < BLOCK_SIZE / 32 && j < 40; j++) {
        offset = mon->columns * c;
        for (i = 0; i < 32; i += 3) {
            b = mem[a++];
            b1 = b & 0xF;
            b2 = (b & 0xF0) >> 4;
            mon->buffer[4 + i + offset] = *(alphanumeric + b1);
            mon->buffer[4 + i + offset + 1] = *(alphanumeric + b2);
            mon->buffer[4 + i + offset + 2] = ' ';
            if (b <= ' ') {
                b = '.';
            }
            mon->buffer[40 + i + offset] = b;
        }
        c++;
    }
    
	if(*(int*)mem == 0){
		print(mon, "Invalid boot disk", mon->columns * c++);
		return;
	}
	
	print(mon, "End", mon->columns * c++);
	
    asm volatile(
    "   la $sp, 0x6000\n"
    "   j 0x2000\n"
    "   nop\n"
    );
}

void print(Monitor *mon, char *str, int offset) {
    int i;
    for (i = 0; str[i]; ++i) {
        mon->buffer[i + offset] = str[i];
    }
}

//char *toString(char *buff, int i) {
//    int val = i;
//    int j;
//    for (j = 0; j < 8; j++) {
//        buff[7-j] = (val % 10) + '0';
//        val = val / 10;
//        buff[j] = '0';
//    }
//}
//
//void boot() {
//    asm volatile(
//    "   la $sp, 512\n"
//            "   j 0x1000\n"
//            "   nop\n"
//    );
//}


//
//    print(mon, "Init:", mon->columns * c);
//    c++;
//
//    toString(string, (int)init);
//    print(mon, string, mon->columns * c);
//    c++;
//
//    print(mon, "Mem:", mon->columns * c);
//    c++;
//
//    toString(string, (int)mem);
//    print(mon, string, mon->columns * c);
//    c++;
//
//    print(mon, "Monitor:", mon->columns * c);
//    c++;
//
//    toString(string, (int)mon);
//    print(mon, string, mon->columns * c);
//    c++;
//
//    print(mon, "Local var &i:", mon->columns * c);
//    c++;
//
//    toString(string, (int)&i);
//    print(mon, string, mon->columns * c);
//    c++;

//
// Created by cout970 on 21/05/2016.
//

#include "harddrive.h"

#include "peripheral.h"
#include "../lib/system.h"


// gets the HardDrive using the bus address, if the bus address doesn't contains a
// HardDrive the function will return NULL
HardDrive* getHardDrive(int addr){
    HardDrive* ptr = (HardDrive *) getPeripheral(addr);
    if(!ptr->active || ptr->id != HARD_DRIVE_ID) return NULL;
    return ptr;
}

// returns the total size of the disk
int HardDrive_getSize(HardDrive* disk){
	return disk->totalSize;
}

// returns the size of a block
int HardDrive_getBlockSize(HardDrive* disk){
    return disk->blockSize;
}

// returns the amount of blocks in the disk
int HardDrive_getBlockAmount(HardDrive* disk){
    return disk->numBlocks;
}

// gets the label of the disk and stores it in buff, the size of the buff should be greater or
// equal than the size of a block, returns 0 if success or an error code otherwise
int HardDrive_getLabel(HardDrive* disk, char* buff){
    disk->action = HARD_DRIVE_ACTION_READ_LABEL;
    while(disk->finished != HARD_DRIVE_FINISHED){
        sleep(1);
    }
    memcpy(buff, disk->buffer, disk->blockSize);
    return disk->errorCode;
}

// sets the disk label using the string in buff, the size of buff must be equal or less than
// the size of a block, returns 0 if success or an error code otherwise
int HardDrive_setLabel(HardDrive* disk, char* buff){
    memcpy(disk->buffer, buff, disk->blockSize);
    disk->action = HARD_DRIVE_ACTION_WRITE_LABEL;
    while(disk->finished != HARD_DRIVE_FINISHED){
        sleep(1);
    }
    return disk->errorCode;
}

// gets the serial number of the disk and stores it in buff, the size of the buff should be greater or
// equal than the size of a block, returns 0 if success or an error code otherwise
int HardDrive_getSerialNumber(HardDrive* disk, char* buff){
    disk->action = HARD_DRIVE_ACTION_READ_SERIAL;
    while(disk->finished != HARD_DRIVE_FINISHED){
        sleep(1);
    }
    memcpy(buff, disk->buffer, disk->blockSize);
    return disk->errorCode;
}

// reads a disk block, returns 0 if success or an error code otherwise
int HardDrive_readBlock(HardDrive* disk){
    disk->action = HARD_DRIVE_ACTION_READ_BLOCK;
    while(disk->finished != HARD_DRIVE_FINISHED){
        sleep(1);
    }
    return disk->errorCode;
}

// writes a disk block, returns 0 if success or an error code otherwise
int HardDrive_writeBlock(HardDrive* disk){
    disk->action = HARD_DRIVE_ACTION_WRITE_BLOCK;
    while(disk->finished != HARD_DRIVE_FINISHED){
        sleep(1);
    }
    return disk->errorCode;
}

// sets the current block to b
void HardDrive_seekBlock(HardDrive* disk, int b){
    disk->block = b;
}

// gets the address of the internal buffer of the drive, the content may change
// so make sure to make a copy before using the data
byte* HardDrive_getHardDriveBuffer(HardDrive* disk){
    return disk->buffer;
}

// copies the content of the internal buffer to buff, starting in buffOffset up to amount, and starting with offset in the internal buffer
void HardDrive_copyFromBuffer(HardDrive* disk, byte* buff, int amount, int buffOffset, int offset){
    memcpy(buff + buffOffset, disk->buffer+offset, amount);
}

// copies the content of buff to the internal buffer, starting at buffOffset up to buffSize, and starting with offset in the internal buffer
void HardDrive_copyToBuffer(HardDrive* disk, byte* buff, int amount, int buffOffset, int offset){
    memcpy(disk->buffer+offset, buff + buffOffset,  amount);
}

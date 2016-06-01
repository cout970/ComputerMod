//
// Created by cout970 on 21/05/2016.
//

#include "diskdrive.h"
#include "peripheral.h"
#include "../lib/system.h"


// gets the diskdrive using the bus address, if the bus address doesn't contains a
// DiskDrive the function will return NULL
Diskdrive* getDiskDrive(int addr){
    Diskdrive* ptr = (Diskdrive *) getPeripheral(addr);
    if(!ptr->active || ptr->id != DISKDRIVE_ID) return NULL;
    return ptr;
}

//  returns 1 if the drive contains a valid disk
int hasDisk(Diskdrive* disk){
    return disk->disk;
}

// returns the number of game ticks needed to perform an action
int getDiskSpeed(Diskdrive* disk){
    return disk->speed;
}

// returns the total size of the disk
int getDiskSize(Diskdrive* disk){
    return disk->diskSize;
}

// returns the size of a block
int getBlockSize(Diskdrive* disk){
    return disk->blockSize;
}

// returns the amount of blocks in the disk
int getBlockAmount(Diskdrive* disk){
    return disk->numBlocks;
}

// gets the label of the disk and stores it in buff, the size of the buff should be greater or
// equal than the size of a block, returns 0 if success or an error code otherwise
int getDiskLabel(Diskdrive* disk, char* buff){
    disk->action = DISKDRIVE_ACTION_READ_LABEL;
    while(!disk->finished){
        sleep(1);
    }
    memcpy(buff, disk->buffer, disk->blockSize);
    return disk->errorCode;
}

// sets the disk label using the string in buff, the size of buff must be equal or less than
// the size of a block, returns 0 if success or an error code otherwise
int setDiskLabel(Diskdrive* disk, char* buff){
    memcpy(disk->buffer, buff, disk->blockSize);
    disk->action = DISKDRIVE_ACTION_WRITE_LABEL;
    while(!disk->finished){
        sleep(1);
    }
    return disk->errorCode;
}

// gets the serial number of the disk and stores it in buff, the size of the buff should be greater or
// equal than the size of a block, returns 0 if success or an error code otherwise
int getDiskSerialNumber(Diskdrive* disk, char* buff){
    disk->action = DISKDRIVE_ACTION_READ_SERIAL;
    while(!disk->finished){
        sleep(1);
    }
    memcpy(buff, disk->buffer, disk->blockSize);
    return disk->errorCode;
}

// reads a disk block, returns 0 if success or an error code otherwise
int readBlock(Diskdrive* disk){
    disk->action = DISKDRIVE_ACTION_READ_BLOCK;
    while(!disk->finished){
        sleep(1);
    }
    return disk->errorCode;
}

// writes a disk block, returns 0 if success or an error code otherwise
int writeBlock(Diskdrive* disk){
    disk->action = DISKDRIVE_ACTION_WRITE_BLOCK;
    while(!disk->finished){
        sleep(1);
    }
    return disk->errorCode;
}

// sets the current block to b
void seekBlock(Diskdrive* disk, int b){
    disk->block = b;
}

// gets the address of the internal buffer of the drive, the content may change
// so make sure to make a copy before using the data
byte* getDiskDriveBuffer(Diskdrive* disk){
    return disk->buffer;
}

// copies the content of the internal buffer to buff, starting in buffOffset up to amount, and starting with offset in the internal buffer
void copyFromBuffer(Diskdrive* disk, byte* buff, int amount, int buffOffset, int offset){
    memcpy(buff + buffOffset, disk->buffer+offset, amount);
}

// copies the content of buff to the internal buffer, starting at buffOffset up to buffSize, and starting with offset in the internal buffer
void copyToBuffer(Diskdrive* disk, byte* buff, int amount, int buffOffset, int offset){
    memcpy(disk->buffer+offset, buff + buffOffset,  amount);
}
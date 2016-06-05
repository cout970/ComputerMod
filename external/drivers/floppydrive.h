//
// Created by cout970 on 21/05/2016.
//

#ifndef FLOPPY_DRIVE_H
#define FLOPPY_DRIVE_H

#include "../lib/util.h"

//default bus address
#define DEFAULT_FLOPPY_DRIVE_ADDR 2

//valid acctions
#define FLOPPY_DRIVE_ACTION_NONE -1
#define FLOPPY_DRIVE_ACTION_WAITING 0
#define FLOPPY_DRIVE_ACTION_READ_LABEL 1
#define FLOPPY_DRIVE_ACTION_WRITE_LABEL 2
#define FLOPPY_DRIVE_ACTION_READ_SERIAL 3
#define FLOPPY_DRIVE_ACTION_READ_BLOCK 4
#define FLOPPY_DRIVE_ACTION_WRITE_BLOCK 5

//valid ID
#define FLOPPY_DRIVE_ID 2

//value of "byte finished" to know if the current action has finished
#define FLOPPY_DRIVE_FINISHED 0

//values of "byte disk"
#define FLOPPY_DRIVE_NO_DISK 0
#define FLOPPY_DRIVE_VALID_DISK 1

//error codes
#define FLOPPY_DRIVE_ERROR_NO_DISK 1
#define FLOPPY_DRIVE_ERROR_CORRUPT_DISK 2
#define FLOPPY_DRIVE_ERROR_OUT_OF_DISK_SIZE 3
#define FLOPPY_DRIVE_ERROR_INVALID_BLOCK 4

typedef struct {
/*    */        //device data
/* 00 */        byte active;            // active flag, shows if the device is connected;
/* 01 */        byte id;                // id, shows the type of the device, id: 0 Motherboard, id: 1 Monitor id: 2 Diskdrive id: 3-255 unknown
/*    */
/*    */        //controls
/* 02 */        byte action;            // the action that the drive should perform
/* 03 */        byte finished;          // stores a 0 if the last action has finished or the number of ticks remaining to finish the action
/* 04 */        uint16_t errorCode;     // stores the error code of the last action, 0 means no error
/*    */
/*    */        //disk data
/* 06 */        byte disk;              // stores a 1 if the drive has a disk inside and 0 if not
/* 07 */        byte speed;             // the amount of tick to complete an action, if disk == 0 then this will be 0
/* 08 */        int diskSize;           // the space in the disk, if disk == 0 then this will be 0
/* 12 */        int numBlocks;          // the number of block in the disk, if disk == 0 then this will be 0
/*    */
/*    */        //access data
/* 16 */        int block;              // the current block loaded/to load
/* 20 */        int blockSize;          // the amount of bytes in a block
/* 24 */        byte buffer[0];         // the internal buffer of the drive, the size of the buffer is stored in blockSize
/*    */
} FloppyDrive;

// gets the diskdrive using the bus address, if the bus address doesn't contains a
// DiskDrive the function will return NULL
FloppyDrive* getFloppyDrive(int addr);

//  returns 1 if the drive contains a valid disk
int FloppyDrive_hasDisk(FloppyDrive* disk);

// returns the number of game ticks needed to perform an action
int FloppyDrive_getSpeed(FloppyDrive* disk);

// returns the total size of the disk
int FloppyDrive_getSize(FloppyDrive* disk);

// returns the size of a block
int FloppyDrive_getBlockSize(FloppyDrive* disk);

// returns the amount of blocks in the disk
int FloppyDrive_getBlockAmount(FloppyDrive* disk);

// gets the label of the disk and stores it in buff, the size of the buff should be greater or
// equal than the size of a block, returns 0 if success or an error code otherwise
int FloppyDrive_getLabel(FloppyDrive* disk, char* buff);

// sets the disk label using the string in buff, the size of buff must be equal or less than
// the size of a block, returns 0 if success or an error code otherwise
int FloppyDrive_setLabel(FloppyDrive* disk, char* buff);

// gets the serial number of the disk and stores it in buff, the size of the buff should be greater or
// equal than the size of a block, returns 0 if success or an error code otherwise
int FloppyDrive_getSerialNumber(FloppyDrive* disk, char* buff);

// reads a disk block, returns 0 if success or an error code otherwise
int FloppyDrive_readBlock(FloppyDrive* disk);

// writes a disk block, returns 0 if success or an error code otherwise
int FloppyDrive_writeBlock(FloppyDrive* disk);

// sets the current block to b
void FloppyDrive_seekBlock(FloppyDrive* disk, int b);

// gets the address of the internal buffer of the drive, the content may change
// so make sure to make a copy before using the data
byte* FloppyDrive_getBuffer(FloppyDrive* disk);

// copies the content of the internal buffer to buff, starting in buffOffset up to amount, and starting with offset in the internal buffer
void FloppyDrive_copyFromBuffer(FloppyDrive* disk, byte* buff, int amount, int buffOffset, int offset);

// copies the content of buff to the internal buffer, starting at buffOffset up to buffSize, and starting with offset in the internal buffer
void FloppyDrive_copyToBuffer(FloppyDrive* disk, byte* buff, int amount, int buffOffset, int offset);

#endif //FLOPPY_DRIVE_H

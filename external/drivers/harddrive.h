//
// Created by cout970 on 05/06/2016.
//

#ifndef HARD_DRIVE_H
#define HARD_DRIVE_H

#include "../lib/util.h"

//default bus address
#define DEFAULT_HARD_DRIVE_ADDR 3

//valid acctions
#define HARD_DRIVE_ACTION_NONE -1
#define HARD_DRIVE_ACTION_WAITING 0
#define HARD_DRIVE_ACTION_READ_LABEL 1
#define HARD_DRIVE_ACTION_WRITE_LABEL 2
#define HARD_DRIVE_ACTION_READ_SERIAL 3
#define HARD_DRIVE_ACTION_READ_BLOCK 4
#define HARD_DRIVE_ACTION_WRITE_BLOCK 5

//valid ID
#define HARD_DRIVE_ID 3

//value of "byte finished" to know if the current action has finished
#define HARD_DRIVE_FINISHED 0

//error codes
#define HARD_DRIVE_ERROR_NO_HARD_DRIVE 1
#define HARD_DRIVE_ERROR_CORRUPT_HARD_DRIVE 2
#define HARD_DRIVE_ERROR_OUT_OF_HARD_DRIVE_SIZE 3
#define HARD_DRIVE_ERROR_INVALID_BLOCK 4

typedef struct {
/*    */        //device data
/* 00 */        byte active;            // active flag, shows if the device is connected and if there is a storage device in the hard drive slot
/* 01 */        byte id;                // id, shows the type of the device, id: 0 Motherboard, id: 1 Monitor id: 2 FloppyDrive id: 3 HardDrive
/*    */
/*    */        //controls
/* 02 */        byte action;            // the action that the drive should perform
/* 03 */        byte finished;          // stores a 0 if the last action has finished or the number of ticks remaining to finish the action
/* 04 */        int errorCode;     		// stores the error code of the last action, 0 means no error
/*    */
/*    */		//hard drive data
/* 08 */        int totalSize;          //  the space in the hard drive
/* 12 */        int numBlocks;          // the number of block in the hard drive
/*    */
/*    */        //access data
/* 16 */        int block;              // the current block loaded/to load
/* 20 */        int blockSize;          // the amount of bytes in a block
/* 24 */        byte buffer[0];         // the internal buffer of the drive, the size of the buffer is stored in blockSize
/*    */
} HardDrive;

// gets the diskdrive using the bus address, if the bus address doesn't contains a
// DiskDrive the function will return NULL
HardDrive* getHardDrive(int addr);

// returns the total size of the disk
int HardDrive_getSize(HardDrive* disk);

// returns the size of a block
int HardDrive_getBlockSize(HardDrive* disk);

// returns the amount of blocks in the disk
int HardDrive_getBlockAmount(HardDrive* disk);

// gets the label of the disk and stores it in buff, the size of the buff should be greater or
// equal than the size of a block, returns 0 if success or an error code otherwise
int HardDrive_getLabel(HardDrive* disk, char* buff);

// sets the disk label using the string in buff, the size of buff must be equal or less than
// the size of a block, returns 0 if success or an error code otherwise
int HardDrive_setLabel(HardDrive* disk, char* buff);

// gets the serial number of the disk and stores it in buff, the size of the buff should be greater or
// equal than the size of a block, returns 0 if success or an error code otherwise
int HardDrive_getSerialNumber(HardDrive* disk, char* buff);

// reads a disk block, returns 0 if success or an error code otherwise
int HardDrive_readBlock(HardDrive* disk);

// writes a disk block, returns 0 if success or an error code otherwise
int HardDrive_writeBlock(HardDrive* disk);

// sets the current block to b
void HardDrive_seekBlock(HardDrive* disk, int b);

// gets the address of the internal buffer of the drive, the content may change
// so make sure to make a copy before using the data
byte* HardDrive_getBuffer(HardDrive* disk);

// copies the content of the internal buffer to buff, starting in buffOffset up to amount, and starting with offset in the internal buffer
void HardDrive_copyFromBuffer(HardDrive* disk, byte* buff, int amount, int buffOffset, int offset);

// copies the content of buff to the internal buffer, starting at buffOffset up to buffSize, and starting with offset in the internal buffer
void HardDrive_copyToBuffer(HardDrive* disk, byte* buff, int amount, int buffOffset, int offset);

#endif //HARD_DRIVE_H

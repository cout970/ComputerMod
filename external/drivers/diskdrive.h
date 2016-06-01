//
// Created by cout970 on 21/05/2016.
//

#ifndef DISKDRIVE_H
#define DISKDRIVE_H

#include "../lib/util.h"

//default bus address
#define DEFAULT_DISKDRIVE_ADDR 2

//valid acctions
#define DISKDRIVE_ACTION_NONE -1
#define DISKDRIVE_ACTION_WAITING 0
#define DISKDRIVE_ACTION_READ_LABEL 1
#define DISKDRIVE_ACTION_WRITE_LABEL 2
#define DISKDRIVE_ACTION_READ_SERIAL 3
#define DISKDRIVE_ACTION_READ_BLOCK 4
#define DISKDRIVE_ACTION_WRITE_BLOCK 5

#define DISKDRIVE_ID 2

#define DISKDRIVE_FINISHED 0

#define DISKDRIVE_NO_DISK 0
#define DISKDRIVE_VALID_DISK 1

//error codes
#define DISKDRIVE_ERROR_NO_DISK 1
#define DISKDRIVE_ERROR_CORRUPT_DISK 2
#define DISKDRIVE_ERROR_OUT_OF_DISK_SIZE 3
#define DISKDRIVE_ERROR_INVALID_BLOCK 4

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
} Diskdrive;

// gets the diskdrive using the bus address, if the bus address doesn't contains a
// DiskDrive the function will return NULL
Diskdrive* getDiskDrive(int addr);

//  returns 1 if the drive contains a valid disk
int hasDisk(Diskdrive* disk);

// returns the number of game ticks needed to perform an action
int getDiskSpeed(Diskdrive* disk);

// returns the total size of the disk
int getDiskSize(Diskdrive* disk);

// returns the size of a block
int getBlockSize(Diskdrive* disk);

// returns the amount of blocks in the disk
int getBlockAmount(Diskdrive* disk);

// gets the label of the disk and stores it in buff, the size of the buff should be greater or
// equal than the size of a block, returns 0 if success or an error code otherwise
int getDiskLabel(Diskdrive* disk, char* buff);

// sets the disk label using the string in buff, the size of buff must be equal or less than
// the size of a block, returns 0 if success or an error code otherwise
int setDiskLabel(Diskdrive* disk, char* buff);

// gets the serial number of the disk and stores it in buff, the size of the buff should be greater or
// equal than the size of a block, returns 0 if success or an error code otherwise
int getDiskSerialNumber(Diskdrive* disk, char* buff);

// reads a disk block, returns 0 if success or an error code otherwise
int readBlock(Diskdrive* disk);

// writes a disk block, returns 0 if success or an error code otherwise
int writeBlock(Diskdrive* disk);

// sets the current block to b
void seekBlock(Diskdrive* disk, int b);

// gets the address of the internal buffer of the drive, the content may change
// so make sure to make a copy before using the data
byte* getDiskDriveBuffer(Diskdrive* disk);

// copies the content of the internal buffer to buff, starting in buffOffset up to amount, and starting with offset in the internal buffer
void copyFromBuffer(Diskdrive* disk, byte* buff, int amount, int buffOffset, int offset);

// copies the content of buff to the internal buffer, starting at buffOffset up to buffSize, and starting with offset in the internal buffer
void copyToBuffer(Diskdrive* disk, byte* buff, int amount, int buffOffset, int offset);

#endif //DISKDRIVE_H

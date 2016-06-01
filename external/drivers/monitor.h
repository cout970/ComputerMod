//
// Created by cout970 on 21/05/2016.
//

#ifndef MONITOR_H
#define MONITOR_H

#include "../lib/util.h"

#define DEFAULT_MONITOR_ADDR 1

#define MONITOR_ID 1

#define MONITOR_MOUSE_BUTTON_NONE 0
#define MONITOR_MOUSE_BUTTON_LEFT 1
#define MONITOR_MOUSE_BUTTON_RIGHT 2
#define MONITOR_MOUSE_BUTTON_MIDDLE 3

typedef struct{
/*    */        //device data
/* 00 */        byte active;            // active flag, shows if the device is connected;
/* 01 */        byte id;                // id, shows the type of the device, id: 0 Motherboard, id: 1 Monitor id: 2 Diskdrive id: 3-255 unknown
/*    */
/*    */        //keyboard data
/* 02 */        byte keyBufferPtr;      // saves the position of the keyBuffer the where the las key has been stored
/* 03 */        byte keyBufferSize;     // stores the amount of keys stored in the buffer, the max value is 8
/* 04 */        char keyBuffer[8];      // stores the keys pressed by the keyboard in a circular array, where keyBufferPtr indicates
/*    */                                // the first value and (keyBufferPtr+keyBufferSize)%8 indicates the last value
/*    */
/*    */        //mouse data
/* 12 */        int mouseButton;        // the button used by the mouse in the last click on the screen
/* 16 */        int mouseX;             // the xCoord of the last mouse click on the screen
/* 20 */        int mouseY;             // the yCoord of the last mouse click on the screen
/*    */
/*    */        //screen data
/* 24 */        int size;               // size of the screen (lines*columns)
/* 28 */        int lines;              // number of lines in the monitor
/* 32 */        int columns;            // size of the lines in the monitor
/*    */
/*    */        //text buffer
/* 36 */        int cursor;             // the position of the screen where the cursor should be displayed
/* 40 */        int cursorMark;         // the cursor that marks when the next char should be placed
/* 44 */        byte buffer[0];         // buffer stores the chars in the monitor, the size of the buffer may change
/*    */                                // between monitors, to get the size use Monitor.size
} Monitor;

// gets the Monitor using the bus address, if the bus address doesn't contains a
// Monitor the function will return NULL
Monitor* getMonitor(int addr);

// gets a key from the keyBuffer, if some key is stored in the keyBuffer the function will return it,
// but if the keyBuffer is empty the function will wait until some key is pressed
char getKey(Monitor* mon);

// prints a char into the screen, the function use the cursorMark to know where to place the char,
// and the increments tha value of cursorMark
void putKey(Monitor* mon, int c);

// gets the last button pressed by the mouse and resets the value of mouseButton to 0, the mouse should
// click inside of the screen to receive the event
int getMouseButton(Monitor* mon);

// gets the X coord of the last place where the mouse clicked
int getMouseX(Monitor* mon);

// gets the Y coord of the last place where the mouse clicked
int getMouseY(Monitor* mon);

// gets the size of the screen (lines * columns), this is also the size of the screen buffer
int getScreenSize(Monitor* mon);

// gets the number of lines in the screen
int getScreenLines(Monitor* mon);

//gets the number of columns in the screen, a.k.a. the number of chars per line
int getScreenColumns(Monitor* mon);

// returns the position of the cursor that is displayed on the screen,
// to get the x,y coords, use (cursor % getScreenColumns(), cursor / getScreenLines())
int getCursor(Monitor* mon);

// returns the position of the cursor that is used to place chars in the screen,
// to get the x,y coords, use (cursor % getScreenColumns(), cursor / getScreenLines())
int getCursorMark(Monitor* mon);

// sets the cursor position
void setCursor(Monitor* mon, int pos);

// sets the cursorMark position
void setCursorMark(Monitor* mon, int pos);

// sets the cursor x,y coordinates
void setCursorXY(Monitor* mon, int x, int y);

// sets the cursorMark x,y coordinates
void setCursorMarkXY(Monitor* mon, int x, int y);

// gets the screen buffer to directly write chars to it
char *getMonitorBuffer(Monitor* mon);

// clears the screen
void clearScreen(Monitor* mon);

//Use stdio.h to print and read from the monitor

#endif //MONITOR_H

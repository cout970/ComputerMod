//
// Created by cout970 on 21/05/2016.
//

#include "monitor.h"
#include "peripheral.h"
#include "../lib/system.h"

// gets the Monitor using the bus address, if the bus address doesn't contains a
// Monitor the function will return NULL
Monitor *getMonitor(int addr) {
    Monitor *mon = (Monitor *) getPeripheral(addr);
    if (!mon->active || mon->id != MONITOR_ID) return NULL;
    return mon;
}

// gets a key from the keyBuffer, if some key is stored in the keyBuffer the function will return it,
// but if the keyBuffer is empty the function will wait until some key is pressed
char getKey(Monitor *mon) {
    while (mon->keyBufferSize <= 0) {
        sleep(1);
    }
    char key = mon->keyBuffer[mon->keyBufferPtr % 8];
    mon->keyBufferPtr++;
    mon->keyBufferPtr = mon->keyBufferPtr % 8;
    mon->keyBufferSize--;
    return key;
}

// prints a char into the screen, the function use the cursorMark to know where to place the char,
// and the increments tha value of cursorMark
void putKey(Monitor *mon, int c) {
    int pos = mon->cursorMark % mon->size;
    mon->buffer[pos] = c;
    mon->cursorMark = (mon->cursorMark + 1) % mon->size;
	mon->cursor = mon->cursorMark;
}

// gets the last button pressed by the mouse and resets the value of mouseButton to 0, the mouse should
// click inside of the screen to receive the event
int getMouseButton(Monitor *mon) {
    int i = mon->mouseButton;
    mon->mouseButton = 0;
    return i;
}

// gets the X coord of the last place where the mouse clicked
int getMouseX(Monitor *mon) {
    return mon->mouseX;
}

// gets the Y coord of the last place where the mouse clicked
int getMouseY(Monitor *mon) {
    return mon->mouseY;
}

// gets the size of the screen (lines * columns), this is also the size of the screen buffer
int getScreenSize(Monitor *mon) {
    return mon->size;
}

// gets the number of lines in the screen
int getScreenLines(Monitor *mon) {
    return mon->lines;
}

//gets the number of columns in the screen, a.k.a. the number of chars per line
int getScreenColumns(Monitor *mon) {
    return mon->columns;
}

// returns the position of the cursor that is displayed on the screen,
// to get the x,y coords, use (cursor % getScreenColumns(), cursor / getScreenLines())
int getCursor(Monitor *mon) {
    return mon->cursor % mon->size;
}

// returns the position of the cursor that is used to place chars in the screen,
// to get the x,y coords, use (cursor % getScreenColumns(), cursor / getScreenLines())
int getCursorMark(Monitor *mon) {
    return mon->cursorMark % mon->size;
}

// sets the cursor position
void setCursor(Monitor *mon, int pos) {
    mon->cursor = pos;
}

// sets the cursorMark position
void setCursorMark(Monitor *mon, int pos) {
    mon->cursorMark = pos;
}

// sets the cursor x,y coordinates
void setCursorXY(Monitor *mon, int x, int y) {
    mon->cursor = y * mon->columns + x;
}

// sets the cursorMark x,y coordinates
void setCursorMarkXY(Monitor *mon, int x, int y) {
    mon->cursorMark = y * mon->columns + x;
}

// gets the screen buffer to directly write chars to it
char *getMonitorBuffer(Monitor *mon) {
    return (char *) mon->buffer;
}

void clearScreen(Monitor* mon){
    memset(mon->buffer, 32, mon->size);
}
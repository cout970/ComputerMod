//
// Created by cout970 on 21/05/2016.
//

#include "util.h"

int min(int a, int b) {
    return a > b ? b : a;
}

int max(int a, int b) {
    return a > b ? a : b;
}

// Copies the C string pointed by source into the array pointed by destination,
// including the terminating null character (and stopping at that point).
// destination is returned.
char *strcpy(char *destination, const char *source) {
    int i;
    for (i = 0; ; i++) {
        ((byte*)destination)[i] = ((byte*)source)[i];
        if (!((byte*)source)[i])break;
    }
    return destination;
}

// Copies the values of num bytes from the location pointed to by source directly
// to the memory block pointed to by destination.
// destination is returned.
void *memcpy(void *destination, const void *source, int num) {
    int i;
    for (i = 0; i < num; i++) {
        ((byte*)destination)[i] = ((byte*)source)[i];
    }
    return destination;
}

// Sets the first num bytes of the block of memory pointed by ptr to the specified value
void *memset(void *ptr, byte value, int num){
    int i;
    for (i = 0; i < num; ++i) {
        ((byte*)ptr)[i] = value;
    }
}
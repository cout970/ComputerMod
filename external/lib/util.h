//
// Created by cout970 on 21/05/2016.
//

#ifndef STD_UTIL_H
#define STD_UTIL_H

//nul value
#define NULL (void*)0
//unsigned types
#ifndef uint8_t
typedef unsigned char uint8_t;
#endif
#ifndef uint16_t
typedef unsigned short int uint16_t;
#endif
//#ifndef uint32_t
//typedef unsigned int uint32_t;
//#endif
//signed types
#ifndef int8_t
typedef signed char int8_t;
#endif
#ifndef int16_t
typedef signed short int int16_t;
#endif
//#ifndef int32_t
//typedef signed int int32_t;
//#endif

typedef uint8_t byte;

#define abs(t) t > 0 ? t : -t

#define min(a, b) a > b ? b : a

#define max(a, b) a < b ? b : a

// Copies the C string pointed by source into the array pointed by destination,
// including the terminating null character (and stopping at that point).
// destination is returned.
char *strcpy(char *destination, const char *source);

// Copies the values of num bytes from the location pointed to by source directly
// to the memory block pointed to by destination.
// destination is returned.
void *memcpy(void *destination, const void *source, int num);

// Sets the first num bytes of the block of memory pointed by ptr to the specified value
void *memset(void *ptr, byte value, int num);

#endif //STD_UTIL_H

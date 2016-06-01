//
// Created by cout970 on 27/05/2016.
//

#ifndef STDIO_H
#define STDIO_H
//
///*
//###################
//#### CONSTANTS ####
//###################
//*/
//#define EOF -1
//
////#define SEEK_SET    0    /* Seek from beginning of file.  */
////#define SEEK_END    2    /* Seek from end of file.  */
////#define SEEK_CUR    1    /* Seek from current position.  */
//
//typedef struct{
//    int fd;
//} FILE;
//
///*
//###################
//#### VARIABLES ####
//###################
//*/
//
//static Monitor *stdin;
//static Monitor *stdout;
//static Monitor *stderr;
//
////TODO add more functions
//
////int fscanf(FILE *stream, const char *format, ...);
//
//int scanf(const char *format, ...);
//
////int sscanf(const char *s, const char *format, ...);
//
int printf(char *format, ...);
//
////int sprintf(char *s, const char *format, ...);
//
////int fprintf(FILE *f, const char *format, ...);
//
////int fgetc(FILE *stream);
//
////int fputc(int c, FILE *stream);
//
//char getchar(void);
//
int putchar(int c);
//
////size_t fread(void *__restrict __ptr, size_t __size, size_t __n, FILE *__restrict __stream);
//
////size_t fwrite(const void *__restrict __ptr, size_t __size, size_t __n, FILE *__restrict __s);
//
////int fseek(FILE *__stream, long int __off, int __whence);
//
////void rewind(FILE *__stream);

#endif //STDIO_H

//
// Created by cout970 on 27/05/2016.
//

#include "stdio.h"
#include "../drivers/monitor.h"

Monitor *stdout = NULL;

void checkMonitor(){
    if(stdout == NULL){
        stdout = getMonitor(DEFAULT_MONITOR_ADDR);
    }
}

int putchar(int c){
    checkMonitor();
	int i;
	if(c == '\n'){
		i = stdout->cursorMark + getScreenColumns(stdout) -(stdout->cursorMark % getScreenColumns(stdout));
		setCursor(stdout, i);
		setCursorMark(stdout, i);
	}else if(c == 8){
		i = stdout->cursorMark;
		stdout->buffer[--i] = 32;
		setCursor(stdout, i);
		setCursorMark(stdout, i);
	}else{
		putKey(stdout, c);
	}
	return 1;
}

int printf(char *format, ...) {
    //byte* args = (byte*)(&format)+1;
	int count = 0;
    //char *p;
    int i;
    //char *s;
	//int *a;

    for (i = 0; format[i] != '\0'; i++) {
	
        if (format[i] != '%') {
			putchar(format[i]);
			count++;
        } else {
           i++;
            switch (format[i]) {
                case 'c':
                    //putchar((char)*(args++));
                    break;

//                case 'd':
//                    i = va_arg(argp, int);
//                    s = itoa(i, fmtbuf, 10);
//                    fputs(s, stdout);
//                    break;
//
//                case 's':
//                    s = va_arg(argp, char *);
//                    fputs(s, stdout);
//                    break;
//
//                case 'x':
//                    i = va_arg(argp, int);
//                    s = itoa(i, fmtbuf, 16);
//                    fputs(s, stdout);
//                    break;
//
//                case '%':
//                    putchar('%');
//                    break;
            }
        }
    }
	return count;
}
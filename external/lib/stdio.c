//
// Created by cout970 on 27/05/2016.
//

#include "stdio.h"
#include "../drivers/monitor.h"

//test
#include "test.h"
//test end

Monitor *stdout = NULL;

void checkMonitor() {
	if (stdout == NULL) {
		stdout = getMonitor(DEFAULT_MONITOR_ADDR);
	}
}

int putchar(int c) {
	checkMonitor();
	int i;
	if (c == '\n') {
		i = stdout->cursorMark + getScreenColumns(stdout)
				- (stdout->cursorMark % getScreenColumns(stdout));
		setCursor(stdout, i);
		setCursorMark(stdout, i);
	} else if (c == 8) {
		i = stdout->cursorMark;
		stdout->buffer[--i] = 32;
		setCursor(stdout, i);
		setCursorMark(stdout, i);
	} else {
		putKey(stdout, c);
	}
	return 1;
}

/**
 * Converte the integer i into an ascii string using 'base'
 * the result is stored in buff, so buff size should be 12 or more
 */
char* itoa(int i, char *buff, int base) {
	char code[] = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	int j;
	int val = i;
	int neg = 1;
	if (i > 0) {
		neg = 0;
		i = -i;
	} else if (i == 0) {
		buff[11] = '\0';
		buff[10] = '0';
		return &buff[10];
	}
	if (base > sizeof(code))
		base = sizeof(code);
	//end of the string
	buff[11] = '\0';
	//place digist from the end to the start
	for (j = 10; j >= 1; j--) {
		buff[j] = code[abs(val % base)];
		val /= base;
		if (val == 0)
			break;
	}
	if (neg) {
		buff[--j] = '-';
	}

	return &buff[j];
}

int printf(char *format, ...) {
	va_list list;
	int count = 0;
	int i, j; //aux
	byte *str;
	byte buff[12];

	va_start(list, format);

	for (i = 0; format[i] != '\0'; i++) {

		if (format[i] != '%') {
			putchar(format[i]);
			count++;
		} else {
			i++;
			switch (format[i]) {
			case 'c':
				putchar(va_arg(list, char));
				count++;
				break;
			case 'd':
				j = va_arg(list, int);
				str = itoa(j, buff, 10);
				count += printf(str);
				break;
			case 's':
				str = va_arg(list, char*);
				for (j = 0; str[j] != '\0'; j++) {
					putchar(str[j]);
					count++;
				}
				break;
			case 'x':
				j = va_arg(list, int);
				str = itoa(j, buff, 16);
				count += printf(str);
				break;
			case '%':
				putchar('%');
				break;
			}
		}
	}

	va_end(list);
	return count;
}

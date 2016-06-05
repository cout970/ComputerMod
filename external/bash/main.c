//
// Created by cout970 on 25/05/2016.
//

#include "../drivers/monitor.h"
#include "../drivers/harddrive.h"
#include "../lib/stdio.h"
#include "../lib/system.h"


void main() {

	Monitor *mon = getMonitor(DEFAULT_MONITOR_ADDR);
	HardDrive *harddrive = NULL;
	char c;
	int i;

	clearScreen(mon);
	setCursor(mon, 0);
	setCursorMark(mon, 0);

	printf(" Loading...\n");

//	printf(" Test string...\n");
//	printf("Char a: '%c', b: '%c' and c: '%c'\n", 'a', 'b', 'c');
//	printf(
//			"String 'test': '%s', 'other test': '%s' and 'the last test': '%s'\n",
//			"test", "other test", "the last test");
//
//	printf("\nDecimal:\n");
//	printf("Int 0:     '%d'\n", 0);
//	printf("Int 1:     '%d'\n", 1);
//	printf("Int 12:    '%d'\n", 12);
//	printf("Int 123:   '%d'\n", 123);
//	printf("Int 1234:  '%d'\n", 1234);
//	printf("Int -1:    '%d'\n", -1);
//	printf("Int -12:   '%d'\n", -12);
//	printf("Int -123:  '%d'\n", -123);
//	printf("Int -1234: '%d'\n", -1234);
//	printf("Int -12345:'%d'\n", -12345);
//	printf("Int 4294967295:'%d'\n", 0xFFFFFFFF);
//	printf("Int 2147483647:'%d'\n", 2147483647);
//
//	printf("\nHexadecimal:\n");
//	printf("Int 0:     '%x'\n", 0);
//	printf("Int 1:     '%x'\n", 1);
//	printf("Int 12:    '%x'\n", 12);
//	printf("Int 123:   '%x'\n", 123);
//	printf("Int 1234:  '%x'\n", 1234);
//	printf("Int -1:    '%x'\n", -1);
//	printf("Int -12:   '%x'\n", -12);
//	printf("Int -123:  '%x'\n", -123);
//	printf("Int -1234: '%x'\n", -1234);
//	printf("Int -12345:'%x'\n", -12345);
//	printf("Int 4294967295:'%x'\n", 0xFFFFFFFF);
//	printf("Int 2147483647:'%x'\n", 2147483647);

	//write test
	while (1) {
		c = getKey(mon);
		if (c == 13)
			c = '\n';
		if ((c & 0xFF) == 200) { //up
			i = mon->cursorMark - mon->columns;
			setCursor(mon, i);
			setCursorMark(mon, i);
		} else if ((c & 0xFF) == 208) { //down
			i = mon->cursorMark + mon->columns;
			setCursor(mon, i);
			setCursorMark(mon, i);
		} else {
			putchar(c);
		}
	}

	exit(0);
}

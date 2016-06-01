//
// Created by cout970 on 25/05/2016.
//

#include "../drivers/monitor.h"
#include "../lib/stdio.h"
#include "../lib/system.h"

void main(void) {
    Monitor *mon = getMonitor(DEFAULT_MONITOR_ADDR);
	char c;
	int i;
	
    clearScreen(mon);
	
	setCursor(mon, 0);
	setCursorMark(mon, 0);
	
	
	putchar('T');
	printf("Test string con T de Test...\n");
	
	while(1){
		c = getKey(mon);
		if(c == 13) c = '\n';
		if((c & 0xFF) == 200){//up
			i = mon->cursorMark - mon->columns;
			setCursor(mon, i);
			setCursorMark(mon, i);
		}else if((c & 0xFF) == 208){//down
			i = mon->cursorMark + mon->columns;
			setCursor(mon, i);
			setCursorMark(mon, i);
		}else{
			putchar(c);
		}
	}
	
	exit(0);
}
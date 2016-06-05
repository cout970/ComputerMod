//
// Created by cout970 on 21/05/2016.
//

#include "system.h"


void sleep(int ticks){

}

void exit(int code){
	asm volatile (
	"	la $v0, 10\n"
	"	syscall\n"
	);
}

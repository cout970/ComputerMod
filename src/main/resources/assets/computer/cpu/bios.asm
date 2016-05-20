
.text 0x000
main:
# print "Starting..."
#0x53 0x74 0x61 0x72 0x74 0x69 0x6e 0x67 0x2e 0x2e 0x2e
	li $sp, 0x2FFC
	jal clear
	#file write
	li $a0, 0
	jal seek
	li $a0, 0
	jal fseek
	jal fload
	jal fbuffer
	move $a0, $v0
	jal printf
	li $a0, 0x2e
	jal putchar
	li $a0, 0x2e
	jal putchar
	li $a0, 0x2e
	jal putchar
	#exit
	addi $v0, $zero, 10
	syscall

.include "stdio.asm"
.include "stdfio.asm"


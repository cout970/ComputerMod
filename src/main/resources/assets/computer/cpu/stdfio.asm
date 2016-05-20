
#defines
.eqv base_file 0xff020000
.eqv base_file_offset 0xc
.eqv block_size 0x1000

#macros
.macro return
	jr $ra
.end_macro

.macro push ($reg)
	subu $sp, $sp, 4
	sw $reg, ($sp)
.end_macro

.macro pop ($reg)
	lw $reg, ($sp)
	addiu $sp, $sp, 4
.end_macro

.macro next
	pop $ra
	return
.end_macro
#vars
fpointer:	
	nop

# clears the screen
# void () $t0, $t1, $t2
fclear:
	la $t0, base_file
	addiu $t0, $t0, base_file_offset
	li $t1, block_size
	li $t2, 32 # space char
fclear_loop_1:
	sb $t2, ($t0)
	addiu $t0, $t0, 1
	subi $t1, $t1, 1
	beqz $t1, fclear_exit
	j fclear_loop_1
fclear_exit:
	return
	
# sets the cursor position
# void ($a0) 
fseek:
	sw $a0, fpointer
	return

# writes the char to std output
# void ($a0) $t0, $t1
fputchar:
	la $t0, base_file
	addiu $t0, $t0, base_file_offset
	lw $t1, fpointer
	addu $t0, $t0, $t1
	sb $a0, ($t0)
	addi $t1, $t1, 1
	sw $t1, fpointer
	return
	
# reads a single char from the std input
# $v0 () $v0
fgetchar:
	la $t0, base_file
	addiu $t0, $t0, base_file_offset
	lw $t1, fpointer
	addu $t0, $t0, $t1
	lb $v0, ($t0)
	addi $t1, $t1, 1
	sw $t1, fpointer
	return
	
# prints a string in the std output
# void ($a0) $t0, $t1, $s0
fprintf:
	push $ra
	move $t1, $a0
fprintf_loop_1:
	lb $t0, ($t1)
	addi $t1, $t1, 1
	beqz $t0, fprintf_exit
	move $a0, $t0
	move $s0, $t1
	jal fputchar
	move $t1, $s0
	j fprintf_loop_1
fprintf_exit:
	next
	
# saves the buffer in the disk
# void () $t0, $t1
fflush:
	li $t0, 5
	la $t1, base_file
	sb $t0, 1($t1)
	return
	
# returns a pointer to the disk buffer
# $v0 () $t0, $t1
fbuffer:
	la $t0, base_file
	addiu $t0, $t0, base_file_offset
	lw $t1, fpointer
	addu $t0, $t0, $t1
	move $v0, $t0
	return
	
# loads a sector from the disk, this will clear the buffer
# void () $t0, $t1
fload:
	li $t0, 4
	la $t1, base_file
	sb $t0, 1($t1)
fload_loop_1:
	lb $t0, 2($t1)
	beqz $t0, fload_loop_1
	return
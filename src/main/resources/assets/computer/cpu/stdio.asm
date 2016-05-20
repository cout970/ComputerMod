
#defines
.eqv base_display 0xFF010000
.eqv base_display_cursor 0xFF01000C
.eqv base_display_offset 24

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

# clears the screen
# void () $t0, $t1, $t2
clear:
	la $t0, base_display
	addi $t0, $t0, base_display_offset
	li $t1, 4000
	li $t2, 32 # space char
clear_loop_1:
	sb $t2, ($t0)
	addiu $t0, $t0, 1
	subi $t1, $t1, 1
	beqz $t1, clear_exit
	j clear_loop_1
clear_exit:
	return
	
# sets the cursor position
# void ($a0)
seek:
	sw $a0, base_display_cursor
	return

# writes the char to std output
# void ($a0) $t0, $t1
putchar:
	la $t1, base_display
	lw $t0, base_display_cursor
	addu $t1, $t0, $t1
	addiu $t1, $t1, base_display_offset
	sb $a0, ($t1)
	addiu $t0, $t0, 1
	sw $t0, base_display_cursor
	return
	
# reads a single char from the std input
# $v0 () $v0
getchar:
	li $v0, 12
	syscall # unimplemented
	return
	
# prints a string in the std output
# void ($a0) $t0, $t1, $s0
printf:
	push $ra
	move $s0, $a0
printf_loop_1:
	lb $a0, ($s0)
	addi $s0, $s0, 1
	beqz $a0, printf_exit
	jal putchar
	j printf_loop_1
printf_exit:
	next

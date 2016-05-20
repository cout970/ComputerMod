
.macro pop ($reg)
	lw $reg, ($sp)
	addiu $sp, $sp, 4
.end_macro

.macro push ($reg)
	subu $sp, $sp, 4
	sw $reg, ($sp)
.end_macro

.macro pop_fp ($reg)
	lw $reg, ($fp)
	addiu $fp, $fp, 4
.end_macro

.macro push_fp ($reg)
	subu $fp, $fp, 4
	sw $reg, ($fp)
.end_macro

.macro exit
	addi $v0, $zero, 10
	syscall
.end_macro

.macro return
	jr $ra
.end_macro

.macro next
	pop $ra
	return
.end_macro

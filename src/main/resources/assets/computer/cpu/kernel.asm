# linux kernel attemp 1

# macros
.include "macros.asm"

.kdata
start_message:	.asciiz "Starting..."


# Init
.ktext 
main:
	la $a0, start_message
	jal printf
	
	exit
	
save_registers:
	push $v0
	push $v1
	push $a0
	push $a1
	push $a2
	push $a3
	push $t0
	push $t1
	push $t2
	push $t3
	push $t4
	push $t5
	push $t6
	push $t7
	push $t8
	push $t9
	push $s0
	push $s1
	push $s2
	push $s3
	push $s4
	push $s5
	push $s6
	push $s7
	push $k0
	push $k1
	push $gp
	push $fp
	push $ra
	
	return

load_registers:
	pop $ra
	pop $fp
	pop $gp
	pop $k1
	pop $k0
	pop $s7
	pop $s6
	pop $s5
	pop $s4
	pop $s3
	pop $s2
	pop $s1
	pop $s0
	pop $t9
	pop $t8
	pop $t7
	pop $t6
	pop $t5
	pop $t4
	pop $t3
	pop $t2
	pop $t1
	pop $t0                                                                                                
	pop $a3
	pop $a2
	pop $a1
	pop $a0
	pop $v1
	pop $v0
# process table

# Interruption handler
	
#includes 
.include "exceptions.asm"
.include "stdio.asm"

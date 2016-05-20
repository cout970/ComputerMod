

.data
new_line:
	.asciiz "____\n"

	.align 2
.text
___global___uId_6___name_printf:
	sw $ra, 128($sp)
	sw $a2 8($sp)
	sw $t1 20($sp)
	sw $t2 24($sp)
	sw $v1 0($sp)
	sw $a3 12($sp)
	sw $a1 4($sp)
	sw $t0 16($sp)
___function___uId_6___vertex_1:

#	Def #7
	addu $a1, $sp, 380

#	Def #1

#	Def #4

#	Def #5

#	Def M

#	ReadArray $134 = #7[0](len = 4) -> M = -1
	lw $a2, 380($sp)

#	$135 = #7 + 4
	addu $v1, $a1, 4

#	Move $84 = $135
	move $t1, $v1

#	Move $92 = $134
	move $a3, $a2
___function___uId_6___vertex_2:

#	ReadArray $104 = #7[0](len = 4) -> M = -1
	lw $a2, 380($sp)

#	ReadArray $90 = $104[0](len = 1) -> M = -1
	lb $v1, 0($a2)

#	NopForBranch $90

#	branchIfFalse NopForBranch $90
	beq $v1, $0, ___function___uId_6___vertex_28
___function___uId_6___vertex_3:

#	$91 = $90 == 37
	seq $v1, $v1, 37

#	NopForBranch $91

#	branchIfFalse NopForBranch $91
	beq $v1, $0, ___function___uId_6___vertex_27
___function___uId_6___vertex_4:

#	WriteArray $104[0](len = 1) = 0 -> M = -1
	sb $0, 0($a2)

#	Push $92

#	Push $92
	move $a0, $a3
	li $v0, 4
	syscall

#	Fetch Return: $105

#	ReadArray $106 = #7[0](len = 4) -> M = -1
	lw $v1, 380($sp)

#	WriteArray $106[0](len = 1) = 37 -> M = -1
	li $v0, 37
	sb $v0, 0($v1)

#	ReadArray $107 = #7[0](len = 4) -> M = -1
	lw $v1, 380($sp)

#	$108 = $107 + 1
	addu $v1, $v1, 1

#	WriteArray #7[0](len = 4) = $108 -> M = -1
	sw $v1, 380($sp)

#	ReadArray $94 = #7[0](len = 4) -> M = -1
	lw $a3, 380($sp)

#	ReadArray $109 = $94[0](len = 1) -> M = -1
	lb $a2, 0($a3)

#	$110 = $109 == 100
	seq $v1, $a2, 100

#	NopForBranch $110

#	branchIfFalse NopForBranch $110
	beq $v1, $0, ___function___uId_6___vertex_8
___function___uId_6___vertex_5:

#	$85 = $84 + 4
	addu $a2, $t1, 4

#	$86 = $85 - 4
	subu $v1, $a2, 4

#	ReadArray $87 = $86[0](len = 4) -> M = -1
	lw $v1, 0($v1)

#	Push $87

#	Push $87
	move $a0, $v1
	li $v0, 1
	syscall

#	Fetch Return: $88

#	Move $89 = $85
	move $a2, $a2
___function___uId_6___vertex_6:

#	ReadArray $128 = #7[0](len = 4) -> M = -1
	lw $v1, 380($sp)

#	$129 = $128 + 1
	addu $v1, $v1, 1

#	Move $120 = $89
	move $t0, $a2

#	Move $121 = $129
	move $a2, $v1
___function___uId_6___vertex_7:

#	ReadArray $118 = #7[0](len = 4) -> M = -1
	lw $v1, 380($sp)

#	$119 = $118 + 1
	addu $v1, $v1, 1

#	WriteArray #7[0](len = 4) = $119 -> M = -1
	sw $v1, 380($sp)

#	Move $84 = $120
	move $t1, $t0

#	Move $92 = $121
	move $a3, $a2

#	unconditionalNext
	j ___function___uId_6___vertex_2
___function___uId_6___vertex_8:

#	$113 = $109 == 99
	seq $v1, $a2, 99

#	NopForBranch $113

#	branchIfFalse NopForBranch $113
	beq $v1, $0, ___function___uId_6___vertex_11
___function___uId_6___vertex_9:

#	$114 = $84 + 4
	addu $a2, $t1, 4

#	$115 = $114 - 4
	subu $v1, $a2, 4

#	ReadArray $116 = $115[0](len = 1) -> M = -1
	lb $v1, 0($v1)

#	Push $116
	move $a0, $v1
	li $v0, 11
	syscall

#	Fetch Return: $117

#	Move $103 = $114
	move $v1, $a2
___function___uId_6___vertex_10:

#	Move $89 = $103
	move $a2, $v1

#	unconditionalNext
	j ___function___uId_6___vertex_6
___function___uId_6___vertex_11:

#	$123 = $109 == 115
	seq $v1, $a2, 115

#	NopForBranch $123

#	branchIfFalse NopForBranch $123
	beq $v1, $0, ___function___uId_6___vertex_14
___function___uId_6___vertex_12:

#	$130 = $84 + 4
	addu $a2, $t1, 4

#	$131 = $130 - 4
	subu $v1, $a2, 4

#	ReadArray $132 = $131[0](len = 4) -> M = -1
	lw $v1, 0($v1)

#	Push $132

#	Push $132
	move $a0, $v1
	li $v0, 4
	syscall

#	Fetch Return: $133

#	Move $124 = $130
	move $v1, $a2
___function___uId_6___vertex_13:

#	Move $103 = $124
	move $v1, $v1

#	unconditionalNext
	j ___function___uId_6___vertex_10
___function___uId_6___vertex_14:

#	$95 = $94 + 1
	addu $v1, $a3, 1

#	WriteArray #7[0](len = 4) = $95 -> M = -1
	sw $v1, 380($sp)

#	ReadArray $96 = $95[0](len = 1) -> M = -1
	lb $v1, 0($v1)

#	$81 = $96 - 48
	subu $t0, $v1, 48

#	ReadArray $97 = #7[0](len = 4) -> M = -1
	lw $v1, 380($sp)

#	$98 = $97 + 1
	addu $v1, $v1, 1

#	WriteArray #7[0](len = 4) = $98 -> M = -1
	sw $v1, 380($sp)

#	$99 = $84 + 4
	addu $t2, $t1, 4

#	$100 = $99 - 4
	subu $v1, $t2, 4

#	ReadArray $101 = $100[0](len = 4) -> M = -1
	lw $a2, 0($v1)

#	$102 = $101 < 0
	slt $v1, $a2, $zero

#	NopForBranch $102

#	branchIfFalse NopForBranch $102
	beq $v1, $0, ___function___uId_6___vertex_26
___function___uId_6___vertex_15:

#	$80 = -#10
	neg $a3, $t1

#	#12 = $81 - 1
	subu $a2, $t0, 1

#	Push 45
	li $a0, 45
	li $v0, 11
	syscall

#	Fetch Return: $82

#	Move #10 = $80
	move $t1, $a3

#	Move $83 = #12
	move $v1, $a2
___function___uId_6___vertex_16:

#	Move #11 = #10
	move $a3, $t1

#	Move $78 = $83
	move $v1, $v1
___function___uId_6___vertex_17:

#	NopForBranch #11

#	branchIfFalse NopForBranch #11
	beq $a3, $0, ___function___uId_6___vertex_19
___function___uId_6___vertex_18:

#	$125 = $78 - 1
	subu $a2, $v1, 1

#	$126 = #11 / 10
	div $v1, $a3, 10

#	Move #11 = $126
	move $a3, $v1

#	Move $78 = $125
	move $v1, $a2

#	unconditionalNext
	j ___function___uId_6___vertex_17
___function___uId_6___vertex_19:

#	Move $79 = $78
	move $a2, $v1
___function___uId_6___vertex_20:

#	$122 = $79 > 0
	sgt $v1, $a2, 0

#	NopForBranch $122

#	branchIfFalse NopForBranch $122
	beq $v1, $0, ___function___uId_6___vertex_22
___function___uId_6___vertex_21:

#	Push 48
	li $a0, 48
	li $v0, 11
	syscall

#	Fetch Return: $111

#	$112 = $79 - 1
	subu $v1, $a2, 1

#	Move $79 = $112
	move $a2, $v1

#	unconditionalNext
	j ___function___uId_6___vertex_20
___function___uId_6___vertex_22:

#	NopForBranch #10

#	branchIfFalse NopForBranch #10
	beq $t1, $0, ___function___uId_6___vertex_25
___function___uId_6___vertex_23:

#	Push #10

#	Push #10
	move $a0, $t1
	li $v0, 1
	syscall

#	Fetch Return: $127
___function___uId_6___vertex_24:

#	Move $124 = $99
	move $v1, $t2

#	unconditionalNext
	j ___function___uId_6___vertex_13
___function___uId_6___vertex_25:

#	unconditionalNext
	j ___function___uId_6___vertex_24
___function___uId_6___vertex_26:

#	Move #10 = $101
	move $t1, $a2

#	Move $83 = $81
	move $v1, $t0

#	unconditionalNext
	j ___function___uId_6___vertex_16
___function___uId_6___vertex_27:

#	Move $120 = $84
	move $t0, $t1

#	Move $121 = $92
	move $a2, $a3

#	unconditionalNext
	j ___function___uId_6___vertex_7
___function___uId_6___vertex_28:

#	Push $92

#	Push $92
	move $a0, $a3
	li $v0, 4
	syscall

#	Fetch Return: $93
___function___uId_6___vertex_29:
	lw $a2, 8($sp)
	lw $t1, 20($sp)
	lw $t2, 24($sp)
	lw $v1, 0($sp)
	lw $a3, 12($sp)
	lw $a1, 4($sp)
	lw $t0, 16($sp)
	lw $ra, 128($sp)
	jr $ra
___global___uId_13___name_clear:
	sw $v1 0($sp)
	sw $a1 4($sp)
___function___uId_13___vertex_1:

#	Def M

#	Move $140 = 0
	li $a1, 0
___function___uId_13___vertex_2:

#	$143 = $140 < 256
	li $at, 256
	slt $v1, $a1, $at

#	NopForBranch $143

#	branchIfFalse NopForBranch $143
	beq $v1, $0, ___function___uId_13___vertex_4
___function___uId_13___vertex_3:

#	$141 = $140 + 32512
	addu $v1, $a1, 32512

#	WriteArray $141[0](len = 1) = 32 -> M = -1
	li $v0, 32
	sb $v0, 0($v1)

#	$142 = $140 + 1
	addu $v1, $a1, 1

#	Move $140 = $142
	move $a1, $v1

#	unconditionalNext
	j ___function___uId_13___vertex_2
___function___uId_13___vertex_4:
___function___uId_13___vertex_5:
	lw $v1, 0($sp)
	lw $a1, 4($sp)
	jr $ra
main:
	addu $sp, $sp, -148
___global___uId_14___name_main:
	sw $ra, 128($sp)
___function___uId_14___vertex_1:

#	Def #13

#	Def M

#	Call #13
	addu $sp, $sp, -152
	jal ___global___uId_13___name_clear
	addu $sp, $sp, 152

#	Fetch Return: $145

#	SetReturn 0
	li $v0, 0
___function___uId_14___vertex_2:
	lw $ra, 128($sp)
	addu $sp, $sp, 148
	jr $ra

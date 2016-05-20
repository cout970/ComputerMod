
# stdio

# writes the char to std output
# void () $v0
putchar:
	li $v0, 11
	syscall
	return
	
# reads a single char from the std input
# $v0 () $v0
getchar:
	li $v0, 12
	syscall
	return
	
# prints a string in the std output
# void ($a0) $v0
printf:
	li $v0, 4
	syscall
	return

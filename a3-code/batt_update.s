	.file	"batt_update.c"
	.text
	.globl	set_batt_from_ports
	.type	set_batt_from_ports, @function
set_batt_from_ports:
.LFB52:
	.cfi_startproc
	movzwl	BATT_VOLTAGE_PORT(%rip), %ecx
	testw	%cx, %cx
	js	.L7
	movw	%cx, (%rdi)
	movswl	BATT_VOLTAGE_PORT(%rip), %edx
	subl	$3000, %edx
	leal	7(%rdx), %eax
	testl	%edx, %edx
	cmovns	%edx, %eax
	sarl	$3, %eax
	movb	%al, 2(%rdi)
	cmpb	$100, %al
	jle	.L3
	movb	$100, 2(%rdi)
.L3:
	cmpw	$3800, %cx
	jle	.L4
	movb	$100, 2(%rdi)
.L4:
	cmpw	$2999, %cx
	jg	.L5
	movb	$0, 2(%rdi)
.L5:
	testb	$1, BATT_STATUS_PORT(%rip)
	jne	.L8
	movb	$0, 3(%rdi)
	movl	$0, %eax
	ret
.L8:
	movb	$1, 3(%rdi)
	movl	$0, %eax
	ret
.L7:
	movl	$1, %eax
	ret
	.cfi_endproc
.LFE52:
	.size	set_batt_from_ports, .-set_batt_from_ports
	.globl	set_display_from_batt
	.type	set_display_from_batt, @function
set_display_from_batt:
.LFB53:
	.cfi_startproc
	pushq	%rbx
	.cfi_def_cfa_offset 16
	.cfi_offset 3, -16
	subq	$48, %rsp
	.cfi_def_cfa_offset 64
	movq	%fs:40, %rax
	movq	%rax, 40(%rsp)
	xorl	%eax, %eax
	movl	$63, (%rsp)
	movl	$3, 4(%rsp)
	movl	$109, 8(%rsp)
	movl	$103, 12(%rsp)
	movl	$83, 16(%rsp)
	movl	$118, 20(%rsp)
	movl	$126, 24(%rsp)
	movl	$35, 28(%rsp)
	movl	$127, 32(%rsp)
	movl	$119, 36(%rsp)
	movl	%edi, %eax
	sarl	$24, %eax
	testb	%al, %al
	jne	.L10
	movswl	%di, %ecx
	cmpl	$1000, %ecx
	jle	.L11
	movl	$1717986919, %edx
	movl	%ecx, %eax
	imull	%edx
	sarl	$2, %edx
	movl	%ecx, %eax
	sarl	$31, %eax
	subl	%eax, %edx
	movl	%edx, %r8d
	leal	(%rdx,%rdx,4), %edx
	leal	(%rdx,%rdx), %eax
	subl	%eax, %ecx
	movl	%ecx, %edx
	movl	%r8d, %ecx
	cmpl	$4, %edx
	jle	.L11
	leal	1(%r8), %ecx
.L11:
	movl	$1717986919, %r8d
	movl	%ecx, %eax
	imull	%r8d
	sarl	$2, %edx
	movl	%ecx, %ebx
	sarl	$31, %ebx
	subl	%ebx, %edx
	movl	%edx, %r11d
	leal	(%rdx,%rdx,4), %edx
	leal	(%rdx,%rdx), %eax
	movl	%ecx, %r9d
	subl	%eax, %r9d
	movl	%r11d, %eax
	imull	%r8d
	sarl	$2, %edx
	movl	%r11d, %eax
	sarl	$31, %eax
	subl	%eax, %edx
	leal	(%rdx,%rdx,4), %edx
	leal	(%rdx,%rdx), %eax
	subl	%eax, %r11d
	movl	$1374389535, %edx
	movl	%ecx, %eax
	imull	%edx
	movl	%edx, %ecx
	sarl	$5, %ecx
	subl	%ebx, %ecx
	movl	%ecx, %eax
	imull	%r8d
	movl	%edx, %eax
	sarl	$2, %eax
	movl	%ecx, %edx
	sarl	$31, %edx
	subl	%edx, %eax
	leal	(%rax,%rax,4), %edx
	leal	(%rdx,%rdx), %eax
	subl	%eax, %ecx
	movl	%ecx, %eax
	cltq
	movl	(%rsp,%rax,4), %eax
	sall	$14, %eax
	movslq	%r11d, %r10
	movl	(%rsp,%r10,4), %edx
	sall	$7, %edx
	orl	%edx, %eax
	movslq	%r9d, %r9
	orl	(%rsp,%r9,4), %eax
	movl	%eax, %edx
	orl	$6291456, %edx
	sall	$8, %edi
	sarl	$24, %edi
	testb	%dil, %dil
	je	.L12
	leal	-5(%rdi), %ecx
	cmpb	$24, %cl
	ja	.L13
	orl	$274726912, %eax
	movl	%eax, %edx
.L12:
	movl	%edx, (%rsi)
.L17:
	movl	$0, %eax
	movq	40(%rsp), %rsi
	xorq	%fs:40, %rsi
	jne	.L28
	addq	$48, %rsp
	.cfi_remember_state
	.cfi_def_cfa_offset 16
	popq	%rbx
	.cfi_def_cfa_offset 8
	ret
.L13:
	.cfi_restore_state
	cmpb	$49, %dil
	jg	.L14
	orl	$408944640, %eax
	movl	%eax, %edx
	jmp	.L12
.L14:
	cmpb	$69, %dil
	jg	.L15
	orl	$476053504, %eax
	movl	%eax, %edx
	jmp	.L12
.L15:
	cmpb	$89, %dil
	jg	.L16
	orl	$509607936, %eax
	movl	%eax, %edx
	jmp	.L12
.L16:
	cmpb	$100, %dil
	jg	.L12
	orl	$526385152, %eax
	movl	%eax, %edx
	jmp	.L12
.L10:
	sall	$8, %edi
	sarl	$24, %edi
	movsbl	%dil, %r8d
	movl	$1717986919, %ecx
	movl	%r8d, %eax
	imull	%ecx
	sarl	$2, %edx
	movl	%r8d, %ebx
	sarl	$31, %ebx
	subl	%ebx, %edx
	movl	%edx, %r11d
	leal	(%rdx,%rdx,4), %edx
	leal	(%rdx,%rdx), %eax
	movl	%r8d, %r9d
	subl	%eax, %r9d
	movl	%r11d, %eax
	imull	%ecx
	sarl	$2, %edx
	movl	%r11d, %eax
	sarl	$31, %eax
	subl	%eax, %edx
	leal	(%rdx,%rdx,4), %edx
	leal	(%rdx,%rdx), %eax
	subl	%eax, %r11d
	movl	%r11d, %r10d
	movl	$1374389535, %edx
	movl	%r8d, %eax
	imull	%edx
	sarl	$5, %edx
	movl	%edx, %r8d
	subl	%ebx, %r8d
	movl	%r8d, %eax
	imull	%ecx
	movl	%edx, %eax
	sarl	$2, %eax
	movl	%r8d, %edx
	sarl	$31, %edx
	subl	%edx, %eax
	leal	(%rax,%rax,4), %edx
	leal	(%rdx,%rdx), %eax
	subl	%eax, %r8d
	movl	%r8d, %eax
	je	.L26
	movslq	%r8d, %rdx
	movl	(%rsp,%rdx,4), %edx
	sall	$14, %edx
.L18:
	orl	%r10d, %eax
	je	.L19
	movslq	%r10d, %r10
	movl	(%rsp,%r10,4), %eax
	sall	$7, %eax
	orl	%eax, %edx
.L19:
	movslq	%r9d, %r9
	orl	(%rsp,%r9,4), %edx
	movl	%edx, %eax
	orl	$8388608, %eax
	cmpb	$4, %dil
	jle	.L20
	leal	-5(%rdi), %ecx
	cmpb	$24, %cl
	ja	.L21
	movl	%edx, %eax
	orl	$276824064, %eax
.L20:
	movl	%eax, (%rsi)
	jmp	.L17
.L26:
	movl	%r8d, %edx
	jmp	.L18
.L21:
	cmpb	$49, %dil
	jg	.L22
	movl	%edx, %eax
	orl	$411041792, %eax
	jmp	.L20
.L22:
	cmpb	$69, %dil
	jg	.L23
	movl	%edx, %eax
	orl	$478150656, %eax
	jmp	.L20
.L23:
	cmpb	$89, %dil
	jg	.L24
	movl	%edx, %eax
	orl	$511705088, %eax
	jmp	.L20
.L24:
	cmpb	$100, %dil
	jg	.L20
	movl	%edx, %eax
	orl	$528482304, %eax
	jmp	.L20
.L28:
	call	__stack_chk_fail@PLT
	.cfi_endproc
.LFE53:
	.size	set_display_from_batt, .-set_display_from_batt
	.globl	batt_update
	.type	batt_update, @function
batt_update:
.LFB54:
	.cfi_startproc
	subq	$24, %rsp
	.cfi_def_cfa_offset 32
	movq	%fs:40, %rax
	movq	%rax, 8(%rsp)
	xorl	%eax, %eax
	movw	$-100, 4(%rsp)
	movb	$-1, 6(%rsp)
	movb	$-1, 7(%rsp)
	leaq	4(%rsp), %rdi
	call	set_batt_from_ports
	cmpl	$1, %eax
	je	.L29
	movl	$0, (%rsp)
	movq	%rsp, %rsi
	movl	4(%rsp), %edi
	call	set_display_from_batt
	movl	(%rsp), %eax
	movl	%eax, BATT_DISPLAY_PORT(%rip)
	movl	$0, %eax
.L29:
	movq	8(%rsp), %rdx
	xorq	%fs:40, %rdx
	jne	.L33
	addq	$24, %rsp
	.cfi_remember_state
	.cfi_def_cfa_offset 8
	ret
.L33:
	.cfi_restore_state
	call	__stack_chk_fail@PLT
	.cfi_endproc
.LFE54:
	.size	batt_update, .-batt_update
	.ident	"GCC: (Ubuntu 7.4.0-1ubuntu1~18.04.1) 7.4.0"
	.section	.note.GNU-stack,"",@progbits

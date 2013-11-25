	.text
	.align 2
	.global divide
	.type divide, %function
	@r0 - dzielna
	@r1 - dzielnik
	@r0 - iloraz
divide:
	stmfd sp!, {fp,ip,lr}
	push {r0-r1}
	pop {r2-r3}
	mov r0, #0

	cmp r2, #0
	beq divide_end

	cmp r2, r3
	beq divide_one

	cmp r3, #0
	beq divide_end

	cmp r2, r3
	ble divide_end

	mov r1, r2
divide_loop:
	sub r1, r1, r3
	add r0, r0, #1

	cmp r1, #0
	beq divide_end

	cmp r1, r3
	blt divide_end

	b divide_loop

divide_one:
	mov r0, #1

divide_end:

	ldmfd sp!, {fp,ip,lr}
	bx lr
	.size divide, .-divide

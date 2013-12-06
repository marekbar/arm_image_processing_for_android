@ This file is jni/ThreshHold.s
@ Author: Marek Bar 33808
@ mail: marekbar1985@gmail.com
	.text
	.align	2
	.global	ThreshHold
	.type	ThreshHold, %function
ThreshHold:
										@thresholding
	stmfd	sp!, {fp,ip,lr}
										@r0 - image pointer
										@r1 - pixels' count
										@r2 - pixel size
										@r3 - limit - thresh
	push {r0-r11}

thresh_loop:
	ldr r5, [r0]						@load pixel from memory
	and r4, r5, #255
	cmp r4, r3
	bl white_it

black_it:
	mov r5, #0
	b end_thresh

white_it:
	mov r5, #255

end_thresh:
@*******************************************************************

	and r4, r5, #255
	mov r5, r4, lsl #8
	mov r6, r4, lsl #16
	add r4, r4, r5
	add r4, r4, r6
	str r4, [r0]			@store pixel in memory
	add r0, r0, r2			@move pointer at next pixel
	subs r1, r1, #1			@decrease and compare if equal zero
	bne thresh_loop 		@iterate until all elements passed


	pop {r0-r11}
	ldmfd	sp!, {fp,ip,lr}
	bx	lr
	.size	ThreshHold, .-ThreshHold

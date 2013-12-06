@ This file is jni/Grayscale.s
@ Author: Marek Bar 33808
@ mail: marekbar1985@gmail.com
@ Notes:
@ Pushing registers r4-r11 on stack avoids Fatal signal 11 with code=1
@ This is simple version without conversion to natural grayscale
	.text
	.align	2
	.global	Grayscale
	.type	Grayscale, %function
Grayscale:
										@Convert to grayscale
	stmfd	sp!, {fp,ip,lr}
										@r0 - image pointer
										@r1 - pixels' count
										@r2 - pixel size

	push {r4-r11}						@critical
grayscale_loop:
	ldr r3, [r0]						@load pixel from memory
@-------------------------------------------------------------------
								@summing pixels
								@Pixel structure BGRA_8888
	mov r4, r3, lsr #16			@extract blue value
	and r4, r4, #255

	mov r5, r3, lsr #8			@extract green value
	and r5, r5, #255

	and r3, r3, #255			@extract red value

	add r3, r3, r4				@sum pixel channel values
	add r3, r3, r5

@*******************************************************************
	div:				@dividing setup section
		push {r0-r2}	@remember pixel address and number of pixels left to the end of image at stack
		mov r2, #3		@divider = 3
		mov r0, #0		@dividing result = 0

		cmp r2, r3		@if number and divider are equal r2 == r3 ?
		beq div_one		@then result will be r0 = 1

		cmp r3, #0		@if number is 0
		beq div_end		@then stop

		cmp r3, r2		@if number lower then divider, like 2 < 3 ?
		ble div_end		@so result will be 0

		div_loop:				@dividing by difference
			sub r3, r3, r2		@nuber = number - divide
			add r0, r0, #1		@count it - this is result calculation

			cmp r3, #0			@is there sth to divide?
			beq div_end			@if not then end

			cmp r3, r2			@number == divider ? div_end : carry on
			blt div_end			@end dividing

			b div_loop			@carry on dividing

	div_one:
		mov r0, #1		@set dividing result as 1

	div_end:			@dividing finished
		mov r3, r0		@place result in r3
		pop {r0-r2}		@restore r3
@*******************************************************************
	mov r4, r3, lsl #8		@set green channel
	mov r5, r3, lsl #16		@set blue channel
	add r3, r3, r4			@create pixel in grayscale
	add r3, r3, r5			@from newly set channels
@-------------------------------------------------------------------
	str r3, [r0]			@store pixel in memory
	add r0, r0, r2			@move pointer at next pixel
	add r11, r11, r2
	subs r1, r1, #1			@decrease and compare if equal zero
	bne grayscale_loop 		@iterate until all elements passed


	pop {r4-r11}
	ldmfd	sp!, {fp,ip,lr}
	bx	lr
	.size	Grayscale, .-Grayscale

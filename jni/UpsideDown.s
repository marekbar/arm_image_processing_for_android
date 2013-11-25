@ This file is jni/UpsideDown.s
	.text
	.align	2
	.global	UpsideDown
	.type	UpsideDown, %function
UpsideDown:
										@Make it upside down
	stmfd	sp!, {fp,ip,lr}
										@r0 - pointer
										@r1 - pixel size
										@r2 - image width
										@r3 - image height
	mul r9, r2, r1						@stride - image row length in bytes

	mov r8, r0							@rememeber image start address
iterate_rows:
		cmp r3, #0
		beq iterate_rows_end

		mov r4, r0							@pointer to row beginning

		mov r5, r4
		add r5, r5, r9						@point on next row
		sub r5, r5, r1						@point on last pixel of prevoius row

	mov r10, r2							@move row width to temp variable used for local lop
	iterate_row:
			cmp r10, #1							@one pixel in row or one pixel left to swap
			beq iterate_row_end					@no need to swap

			ldr r6, [r4]						@load pixel from left side in row
			ldr r7, [r5]						@load pixel from right side in row
			str r6, [r5]						@swap them and store
			str r7, [r4]

			add r4, r4, r1						@pixel on the left from middle address
			sub r5, r5, r1						@pixel on the right from middle address

			sub r10, r10, #2					@count remaining pixels to swap
			cmp r10, #0							@any pixels left?
			bne iterate_row						@if so then carry on swaping pixels in row
	iterate_row_end:

		add r8, r8, r9						@next row adress
		mov r0, r8							@point to next row

		sub r3, r3, #1						@calculate rows remaining
		b iterate_rows

iterate_rows_end:

	ldmfd	sp!, {fp,ip,lr}
	bx	lr
	.size UpsideDown, .-UpsideDown

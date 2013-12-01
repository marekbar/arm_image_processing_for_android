@ This file is jni/ApplyKernel3x3.s
@ Applying kernel at each pixel where kernel value is stored at 4-byte
@ Kernel size is - width: 3 and heigh: 3
@ Image pixel structure ABGR_8888
@ Author: Marek Bar 33808
@ mail: marekbar1985@gmail.com

	.text
	.align	2
	.global	ApplyKernel3x3
	.type	ApplyKernel3x3, %function

ApplyKernel3x3:
									
	stmfd	sp!, {fp,ip,lr}
		
	push {r0-r11}
										@r0 - image pointer
										@r1 - image details array - each detail passed on 4 bytes
										@width, height, pixel size
										@r2 - kernal pointer
										@r3 - divider after summing
	mov r11, r3
	ldr r3, [r1]						@extract image width
	push {r3}

	add r1, r1, #4						@move pointer at image height
	ldr r4, [r1]						@extract image height
	push {r4}

	add r1, r1, #4						@move pointer at pixel size
	ldr r1, [r1]						@extract pixel size, forgets where were image details

	mul r4, r3, r4						@number of pixels
	mul r5, r3, r1						@calculate image stride
	mov r3, r5							@because Rd and Rm must be different

	add r0, r0, r3						@move pointer at start position
	add r0, r0, r1						@to avoid going out of image index during applying kernel

	mul r5, r4, r1						@calculate stop position
	sub r5, r5, r3
	sub r5, r5, r1
	sub r5, r5, r1						@pointer to last allowed address
	mov r4, r5

	pop {r6-r7}							@image width & height
	sub r6, r6, #2
	sub r7, r7, #2
	mul r5, r6, r7						@total pixels' number to iterate
@-----------------------------------------------------------------
@ r0 - image pointer at start position
@ r1 - pixel size
@ r2 - kernel pointer
@ r3 - image stride
@ r4 - pointer to last allowed address
@ r6 - r11 - registers left to use
@-----------------------------------------------------------------

kernel_pixels_loop:

@----------------------------1------------------------------------
	sub r6, r0, r3						@p1 address
	sub r6, r6, r1						@p1 address get
	ldr r6, [r6]						@p1 value
	and r6, r6, #255
	ldr r7, [r2]						@k1 value
	mul r8, r6, r7						@w1 = p1 * k1
	mov r9, r8							@sum += w1
@----------------------------2------------------------------------
	sub r6, r0, r3						@p2 addres get
	ldr r6, [r6]						@p2 value
	and r6, r6, #255
	add r7, r2, #1						@w2 address
	ldr r7, [r7]						@w2 value
	mul r8, r6, r7						@w2 = p2 * k2
	add r9, r9, r8						@sum += w2
@----------------------------3------------------------------------
	sub r6, r0, r3						@p3 address get
	add r6, r6, r1						@p3 address get
	ldr r6, [r6]						@p3 value get
	and r6, r6, #255
	add r7, r2, #2						@k3 address get
	ldr r7, [r7]						@k3 value get
	mul r8, r6, r7						@w3 = p3 * k3
	add r9, r9, r8						@sum += w3
@----------------------------4------------------------------------
	sub r6, r0, r1						@p4 address get
	ldr r6, [r6]						@p4 value get
	and r6, r6, #255
	add r7, r2, #3						@k4 address get
	ldr r7, [r7]						@k4 value get
	mul r8, r6, r7						@w4 = p4 * k4
	add r9, r9, r8						@sum += w4
@----------------------------5------------------------------------
	ldr r6, [r0]						@p5 value get
	and r6, r6, #255
	add r7, r2, #4						@k5 address get
	ldr r7, [r7]						@k5 value get
	mul r8, r6, r7						@w5 = p5 * k5
	add r9, r9, r8						@sum += w5
@----------------------------6------------------------------------
	add r6, r0, r1						@p6 address get
	ldr r6, [r6]						@p6 value get
	and r6, r6, #255
	add r7, r2, #5						@k6 address get
	ldr r7, [r7]						@k6 value get
	mul r8, r6, r7						@w6 = p6 * k6
	add r9, r9, r8						@sum += w6
@----------------------------7------------------------------------
	add r6, r0, r3						@p7 address get
	sub r6, r6, r1						@p7 address get
	ldr r6, [r6]						@p7 value get
	and r6, r6, #255
	add r7, r2, #6						@k7 address get
	ldr r7, [r7]						@k7 value get
	mul r8, r6, r7						@w7 = p7 * k7
	add r9, r9, r8						@sum += w7
@----------------------------8------------------------------------
	add r6, r0, r3						@p8 address get
	ldr r6, [r6]						@p8 value get
	and r6, r6, #255
	add r7, r2, #7						@k8 address get
	ldr r7, [r7]						@k8 value get
	mul r8, r6, r7						@w8 = p8 * k8
	add r9, r9, r8						@sum += w8
@----------------------------9------------------------------------
	add r6, r0, r3						@p9 address get
	add r6, r6, r1						@p9 address get
	ldr r6, [r6]						@p9 value get
	and r6, r6, #255
	add r7, r2, #8						@k9 address get
	ldr r7, [r7]						@k9 value get
	mul r8, r6, r7						@w9 = p9 * k9
	add r9, r9, r8						@sum += w9

	cmp r9, #255
	ble saving
	mov r9, #255
@now the sum of multiplying applied mask by pixels must be divided
@by length of mask - in this case it will always be 9
@ r9 - number to divide
@ r10 - divider
@ r11 - dividing result
@ Dividing
@-----------------------------------------------------------------


saving:
@-----------------------------------------------------------------
	and r11, r9, #255
	mov r9, r11, lsl #16
	mov r10, r11, lsl #8
	add r11, r11, r9
	add r11, r11, r10
	str r11, [r0]						@store result of edging in memory

@-----------------------------------------------------------------
finish:
	add r0, r0, r1						@move pointer at next pixel
	subs r5, r5, #1						@and one pixel less
	bne kernel_pixels_loop 				@carry on with applying mask


	pop {r0-r11}
	ldmfd	sp!, {fp,ip,lr}
	bx	lr
	.size	ApplyKernel3x3, .-ApplyKernel3x3

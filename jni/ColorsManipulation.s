@ This file is jni/ColorsManipulation.s
@ Allows to manipulate ABGR PNG image
@ Author: Marek Bar
@ 2013-11-12
@ mail: marekbar1985@gmail.com
	.text
	.align	2
	.global	ColorManipulate
	.type	ColorManipulate, %function
ColorManipulate:

	stmfd	sp!, {fp,ip,lr}
@r0 - pointer
@r1 - array length- number of pixels
@r2 - array element size - in bytes
@r3 - option, which is:
	push {r0} @remember image array start address

	@model ABGR - alpha, blue, green, red
	mov r8, #4278190080					@alpha mask
	mov r9, #16711680					@blue mask
	mov r10, #65280						@green mask
	mov r11, #255						@red mask

@switch option*****************************************************************
	cmp r3, #0 							@keep it original
	beq color_finish 					@don't process - finish

	cmp r3, #1 							@only red channel
	beq only_red						@if r3 == 1 then go to only_red

	cmp r3, #2 							@only green channel
	beq only_green						@if r3 == 2 then go to only_green

	cmp r3, #3							@only blue channel
	beq only_blue						@if r3 == 3 then go to only_blue

	cmp r3, #4							@remove red channel
	beq no_red							@if r3 == 3 then go to no_red

	cmp r3, #5							@remove green channel
	beq no_green						@if r3 == 5 then go to no_green

	cmp r3, #6							@remove blue channel
	beq no_blue							@if r3 == 6 then go to no_blue
@end of switch******************************************************************

@setting masks-------------------------------------------------------------------
only_red:
	mov r6, r11 						@set mask
	b color_array_loop 					@go to pixel processing loop
only_green:
	mov r6, r10							@set mask
	b color_array_loop					@go to pixel processing loop
only_blue:
	mov r6, r9							@set mask
	b color_array_loop					@go to pixel processing loop
no_red:
	add r6, r10, r9						@set mask
	b color_array_loop					@go to pixel processing loop
no_green:
	add r6, r9, r11						@set mask
	b color_array_loop 					@go to pixel processing loop
no_blue:
	add r6, r10, r11					@set mask
	b color_array_loop					@go to pixel processing loop
@end of setting masks------------------------------------------------------------

@********************************************************************************
color_array_loop:						@pixel processing loop
	ldr r3, [r0] 						@load pixel to r3 register

	and r3, r3, r6						@apply set mask at the beginning in switch part

	str r3, [r0] 						@replace pixel value in memory with this from register r3
	add r0, r0, r2						@move pointer at next pixel
	sub r1, r1, #1						@calc number of remaining pixels to the end of image

	cmp r1, #0 							@check if any pixels left
	bne color_array_loop				@if any pixels left than carry on with pixel processing
@********************************************************************************

color_finish:							@end of pixel processing
	pop {r0} 							@restore image start address
	ldmfd	sp!, {fp,ip,lr}
	bx	lr
	.size	ColorManipulate, .-ColorManipulate

@ This file is jni/Frame.s
	.text
	.align	2
	.global	AddFrame
	.type	AddFrame, %function
AddFrame:
	stmfd	sp!, {fp,ip,lr}
@r0 - pointer
@r1 - params array: bytes, bytesPerPixel, stride, borderWidth
	push {r0-r1}




	pop {r0-r1}
	ldmfd	sp!, {fp,ip,lr}
	bx	lr
	.size	AddFrame, .-AddFrame

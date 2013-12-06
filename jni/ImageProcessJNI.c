#include <jni.h>
#include "include/ImageProcessJNI.h"

JNIEXPORT jbyteArray JNICALL Java_pl_marekbar_Main_BitmapToGrayscale(JNIEnv * env, jobject thisObj, jbyteArray bitmap, jint bytes, jint bytesPerPixel)
{
	Grayscale((*env)->GetByteArrayElements(env, bitmap, 0), bytes, bytesPerPixel);
	return bitmap;
}

JNIEXPORT jbyteArray JNICALL Java_pl_marekbar_Main_BitmapColorManipulate(JNIEnv * env, jobject obj , jbyteArray bitmap, jint bytes, jint bytesPerPixel, jint option)
{
	jbyte *pointer = (*env)->GetByteArrayElements(env, bitmap, 0);
	ColorManipulate(pointer, bytes, bytesPerPixel, option);
	return bitmap;
}

JNIEXPORT jint JNICALL Java_pl_marekbar_Main_Divide(JNIEnv *env, jobject obj, jint a, jint b)
{
	return divide(a,b);
}

JNIEXPORT jbyteArray JNICALL Java_pl_marekbar_Main_BitmapDetectEdges(JNIEnv *env, jobject obj, jbyteArray bitmap, jint imageWidth, jint imageHeight, jbyteArray gray, jint option)
{
	jint bytesPerPixel = 4;
	jbyte *bitmapPointer = (*env)->GetByteArrayElements(env, bitmap, 0);
	jbyte *grayPointer = (*env)->GetByteArrayElements(env, gray, 0);
	jint imageDetails[3] = {imageWidth, imageHeight, bytesPerPixel};
	Grayscale(bitmapPointer, imageWidth * imageHeight, bytesPerPixel);

	switch(option)
	{
		case 0:
		{
			jint PrevitP1[9] = {
						-1, 0, 1,
						-1, 0, 1,
						-1, 0, 1
				};
			jint PrevitP2[9] = {
						 0, 1, 1,
						-1, 0, 1,
						-1, 0, 0
				};
			jint PrevitP3[9] = {
						1, 1, 1,
						0, 0, 0,
					    -1, -1, -1
				};
			jint PrevitP4[9] = {
						1, 1, 0,
						1, 0, -1,
						0, -1, -1
				};


			ApplyKernel3x3(bitmapPointer, imageDetails, PrevitP1, grayPointer);
			ApplyKernel3x3(grayPointer, imageDetails, PrevitP2, bitmapPointer);
			ApplyKernel3x3(bitmapPointer, imageDetails, PrevitP3, grayPointer);
			ApplyKernel3x3(grayPointer, imageDetails, PrevitP4, bitmapPointer);
			return gray;
		}break;
		case 1:
		{
			jint SobelP1[9] = {
						-1, 0, 1,
						-2, 0, 2,
						-1, 0, 1
				};
			jint SobelP2[9] = {
						 0, 1, 2,
						-1, 0, 1,
						-2, -1, 0
				};
			jint SobelP3[9] = {
						1, 2, 1,
						0, 0, 0,
					    -1, -2, -1
				};
			jint SobelP4[9] = {
						2, 1, 0,
						1, 0, -1,
						0, -1, -2
				};


			ApplyKernel3x3(bitmapPointer, imageDetails, SobelP1, grayPointer);
			ApplyKernel3x3(grayPointer, imageDetails, SobelP2, bitmapPointer);
			ApplyKernel3x3(bitmapPointer, imageDetails, SobelP3, grayPointer);
			ApplyKernel3x3(grayPointer, imageDetails, SobelP4, bitmapPointer);
			return gray;
		}break;
	}
}

JNIEXPORT jbyteArray JNICALL Java_pl_marekbar_Main_BitmapMirrorX(JNIEnv *env, jobject obj, jbyteArray bitmap, jint bytesPerPixel, jint imageWidth, jint imageHeight)
{
	MirrorX((*env)->GetByteArrayElements(env, bitmap, 0), bytesPerPixel, imageWidth, imageHeight);
	return bitmap;
}

JNIEXPORT jbyteArray JNICALL Java_pl_marekbar_Main_BitmapMirrorY(JNIEnv *env, jobject obj, jbyteArray bitmap, jint bytesPerPixel, jint imageWidth, jint imageHeight)
{
	MirrorY((*env)->GetByteArrayElements(env, bitmap, 0), bytesPerPixel, imageWidth, imageHeight);
	return bitmap;
}


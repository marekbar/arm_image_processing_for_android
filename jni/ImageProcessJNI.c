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

JNIEXPORT jbyteArray JNICALL Java_pl_marekbar_Main_BitmapDetectEdges(JNIEnv *env, jobject obj, jbyteArray bitmap, jint imageWidth, jint imageHeight, jint bytesPerPixel)
{
	jbyte *bitmapPointer = (*env)->GetByteArrayElements(env, bitmap, 0);
	Grayscale(bitmapPointer, imageWidth * imageHeight, bytesPerPixel);

	jint oX[9] = {
			-1, 0, 1,
			-2, 0, 2,
			-1, 0, 1
	};
	jint oY[9] = {
			1, 2, 1,
			0, 0, 0,
			-1, -2, -1
	};

	jint imageDetails[3] = {imageWidth, imageHeight, bytesPerPixel};

	ApplyKernel3x3(bitmapPointer, imageDetails, oX, 1);
//	ThreshHold(bitmapPointer, imageWidth*imageHeight, bytesPerPixel, 127);
	return bitmap;
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


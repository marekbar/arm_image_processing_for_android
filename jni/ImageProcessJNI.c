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

JNIEXPORT jbyteArray JNICALL Java_pl_marekbar_Main_BitmapAddBorder(JNIEnv * env, jobject obj, jbyteArray bitmap, jintArray args)
{
	jbyte *pointer = (*env)->GetByteArrayElements(env, bitmap, 0);
	AddFrame(pointer, args);
	return bitmap;
}

JNIEXPORT jint JNICALL Java_pl_marekbar_Main_Divide(JNIEnv *env, jobject obj, jint a, jint b)
{
	return divide(a,b);
}

JNIEXPORT jbyteArray JNICALL Java_pl_marekbar_Main_BitmapUpsideDown(JNIEnv *env, jobject obj, jbyteArray bitmap, jint pixelSize, jint width, jint height)
{
	jbyte *pointer = (*env)->GetByteArrayElements(env, bitmap, 0);
	UpsideDown(pointer, pixelSize, width, height);
	return bitmap;
}

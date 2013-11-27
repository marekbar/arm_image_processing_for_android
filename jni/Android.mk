LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := armimageprocess
LOCAL_SRC_FILES := ImageProcessJNI.c Divide.s Grayscale.s ColorsManipulation.s Frame.s UpsideDown.s ApplyKernel3x3.s ThreshHold.s
LOCAL_CFLAGS := -g

include $(BUILD_SHARED_LIBRARY)

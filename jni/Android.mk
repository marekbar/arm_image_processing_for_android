LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := armimageprocess
LOCAL_SRC_FILES := ImageProcessJNI.c Divide.s Grayscale.s ColorsManipulation.s Frame.s ApplyKernel3x3.s ThreshHold.s MirrorX.s MirrorY.s
LOCAL_CFLAGS := -g

include $(BUILD_SHARED_LIBRARY)

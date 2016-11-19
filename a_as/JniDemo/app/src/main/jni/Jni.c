//
// Created by junxu.wang on 16/11/19.
//

#include "wu_a_jni_JniBase.h"

/*
 * Class:     wu_a_jni_JniBase
 * Method:    getName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_wu_a_jni_JniBase_getName
        (JNIEnv *env, jclass obj){
    return (*env)->NewStringUTF(env,"测试_getName");
}

/*
 * Class:     wu_a_jni_JniBase
 * Method:    getAge
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_wu_a_jni_JniBase_getAge
        (JNIEnv *env, jclass obj, jstring str){
    jint jint1=12;
    return jint1;
}
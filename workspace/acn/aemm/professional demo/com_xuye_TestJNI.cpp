#define LOG_TAG "Test-JNI"
#include <utils/Log.h>
#include <utils/misc.h>

#include <android_runtime/AndroidRuntime.h>
#include <errno.h>
#include <unistd.h>
#include <string.h>
#include <android_runtime/ActivityManager.h>
#include <assert.h>
#include <utils/String16.h>

#include "service/ITestService.h"
#include "service/TestService.h"

using namespace android;

static void com_xuye_helloJNI(JNIEnv *env, jobject thiz,
                              jint value)
{
    LOGV("JNI");
    String16 url16("content://com.xuye.TestContentProvider/hellojni");
    int fd = android::openContentProviderFile(url16);
    if(fd >= 0) {
        unsigned char benable = 0;
        //LOGE("Couldn't open fd for %s", url);
        //return UNKNOWN_ERROR;
        lseek(fd, 0, SEEK_SET); //SET
        //read(fd, (char *)&benable, sizeof(benable));
        write(fd, "hello copy and post\n", strlen("hello copy and post\n"));
        close(fd);
     }
}

static void com_xuye_helloJNI2(JNIEnv *env, jobject thiz,
                              jint value)
{
    LOGV("JNI2");
    String16 url16("content://com.xuye.TestContentProvider/hellojni2");
    int fd = android::openContentProviderFile(url16);
    if(fd >= 0) {
        unsigned char benable = 0;
        //LOGE("Couldn't open fd for %s", url);
        //return UNKNOWN_ERROR;
        lseek(fd, 0, SEEK_SET); //SET
        //read(fd, (char *)&benable, sizeof(benable));
        write(fd, "hello copy and post\n", strlen("hello copy and post\n"));
        close(fd);
     }
}

static bool com_xuye_isEnableDial(JNIEnv *env, jobject thiz)
{
    String16 url16("content://com.xuye.TestContentProvider/aemmdial");
    int fd = android::openContentProviderFile(url16);
    unsigned char benable = 1;

    if(fd >= 0) {
        //LOGE("Couldn't open fd for %s", url);
        //return UNKNOWN_ERROR;
        lseek(fd, 0, SEEK_SET); //SET
        read(fd, (char *)&benable, sizeof(benable));
        //write(fd, "hello copy and post\n", strlen("hello copy and post\n"));
        close(fd);
     }
    return benable == 0;
}

static int com_xuye_openFile(JNIEnv *env, jobject thiz, jstring fileName)
{
//    LOGI("com_xuye_openFile");
//    sp<ITestService> service = TestService::getTestService();
//    if(true) return (int)service->TestCommand_1();

    const char16_t* name16 = env->GetStringChars(fileName, NULL);
    jsize nameLen = env->GetStringLength(fileName);
    String16 url16("content://com.xuye.TestContentProvider/");
    url16.append(name16, (size_t)nameLen);
    env->ReleaseStringChars(fileName, name16);
    int fd = android::openContentProviderFile(url16);
    lseek(fd, 0, SEEK_SET);
    return fd;
}

static int com_xuye_readFile(JNIEnv *env, jobject thiz, int fd, jbyteArray buffer)
{
    LOGI("com_xuye_readFile");
    jsize bl = env->GetArrayLength(buffer);
    jbyte* ba = env->GetByteArrayElements(buffer, JNI_FALSE);
    LOGI("fd = %d, bl = %d", fd, bl);
    bl = read(fd, (char *)ba, (size_t)bl);
    env->ReleaseByteArrayElements(buffer, ba, 0);
    LOGI("bl = %d, errno = %d", bl, errno);
    return bl;
}

static void com_xuye_closeFile(JNIEnv *env, jobject thiz, int fd)
{
    close(fd);
}

static jobject com_xuye_openFile2(JNIEnv *env, jobject thiz, jstring filename)
{
    jfieldID field_fd;
    jmethodID const_fdesc;
    jclass class_fdesc, class_ioex;
    jobject ret;
    int fd;
    char *fname;

    class_ioex = env->FindClass("java/io/IOException");
    if (class_ioex == NULL) return NULL;
    class_fdesc = env->FindClass("java/io/FileDescriptor");
    if (class_fdesc == NULL) return NULL;

    fd = com_xuye_openFile(env, thiz, filename);
    if (fd < 0) {
        // open returned an error. Throw an IOException with the error string
        char buf[1024];
        sprintf(buf, "open: %s", strerror(errno));
        env->ThrowNew(class_ioex, buf);
        return NULL;
    }

    // construct a new FileDescriptor
    const_fdesc = env->GetMethodID(class_fdesc, "<init>", "()V");
    if (const_fdesc == NULL) return NULL;
    ret = env->NewObject(class_fdesc, const_fdesc);

    // poke the "fd" field with the file descriptor
    field_fd = env->GetFieldID(class_fdesc, "fd", "I");
    if (field_fd == NULL) return NULL;
    env->SetIntField(ret, field_fd, fd);

    // and return it
    return ret;
}

static int com_xuye_TestServiceCommand_1(JNIEnv *env, jobject thiz)
{
    sp<ITestService> service = TestService::getTestService();
    return (int)service->TestCommand_1();
}

static JNINativeMethod methods[] = {
    { "helloJNI",
      "(I)V",
      (void *)com_xuye_helloJNI },
    { "helloJNI2",
      "(I)V",
      (void *)com_xuye_helloJNI2 },
    { "isEnableDial",
      "()Z",
      (void *)com_xuye_isEnableDial },
    { "openFile",
      "(Ljava/lang/String;)I",
      (void *)com_xuye_openFile },
    { "openFile2",
      "(Ljava/lang/String;)Ljava/lang/Object;",
      (void *)com_xuye_openFile2 },
    { "readFile",
      "(I[B)I",
      (void *)com_xuye_readFile },
    { "closeFile",
      "(I)V",
      (void *)com_xuye_closeFile },
};

int register_com_xuye_testJNI(JNIEnv *_env)
{
    int err;
    err = android::AndroidRuntime::registerNativeMethods(_env, "com/xuye/TestJNI", methods, NELEM(methods));
    return err;
}

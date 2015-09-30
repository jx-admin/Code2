
package net.ds.effect.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.util.Log;

/**
 * Utility class for reflection.
 * 
 * @author shixiaolei
 */
public class ReflectionUtils {

    private static final String TAG = Constants.TAG;

    public static Class<?> getClass(Object object, String className) {
        Class<?> cls = null;
        if (object != null) {
            cls = object.getClass();
            return cls;
        }
        if (className != null && className.length() > 0) {
            try {
                cls = Class.forName(className);
            } catch (ClassNotFoundException e) {
                Log.w(TAG, "exception", e);
            } catch (Throwable e) {
                Log.w(TAG, "exception", e);
            }
        }
        return cls;
    }

    public static Method getMethod(Class<?> cls, String methodName) {
        Method m = null;
        if (methodName != null && methodName.length() > 0) {
            try {
                m = cls.getMethod(methodName);
            } catch (NoSuchMethodException e) {
                Log.w(TAG, "exception", e);
            } catch (Throwable e) {
                Log.w(TAG, "exception", e);
            }
        }
        return m;
    }

    public static Method getMethod(Class<?> cls, String methodName, Class<?> paramTypes[]) {
        Method m = null;
        if (methodName != null && methodName.length() > 0) {
            try {
                m = cls.getMethod(methodName, paramTypes);
            } catch (NoSuchMethodException e) {
                Log.w(TAG, "exception", e);
            } catch (Throwable e) {
                Log.w(TAG, "exception", e);
            }
        }
        return m;
    }

    public static Method getDeclaredMethod(Class<?> cls, String methodName, Class<?> paramTypes[]) {
        Method m = null;
        if (methodName != null && methodName.length() > 0) {
            try {
                m = cls.getDeclaredMethod(methodName, paramTypes);
            } catch (NoSuchMethodException e) {
                Log.w(TAG, "exception", e);
            } catch (Throwable e) {
                Log.w(TAG, "exception", e);
            }
        }
        return m;
    }

    public static Field getField(Class<?> cls, String fieldName) {
        java.lang.reflect.Field f = null;
        try {
            f = cls.getField(fieldName);
        } catch (NoSuchFieldException e) {
            Log.w(TAG, "exception", e);
        } catch (Throwable e) {
            Log.w(TAG, "exception", e);
        }
        return f;
    }

    public static Field getDeclaredField(Class<?> cls, String fieldName) {
        java.lang.reflect.Field f = null;
        try {
            f = cls.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Log.w(TAG, "exception", e);
        } catch (Throwable e) {
            Log.w(TAG, "exception", e);
        }
        return f;
    }

    public static Object invokeMethod(Object object, Method m) {
        Object rc = null;
        try {
            boolean acc = m.isAccessible();
            if (!acc) {
                m.setAccessible(true);
            }
            rc = m.invoke(object);
            if (!acc) {
                m.setAccessible(false);
            }
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "exception", e);
        } catch (IllegalAccessException e) {
            Log.w(TAG, "exception", e);
        } catch (InvocationTargetException e) {
            Log.w(TAG, "exception", e);
        } catch (Throwable e) {
            Log.w(TAG, "exception", e);
        }
        return rc;
    }

    public static Object invokeMethod(Object object, Method m, Object... args) {
        Object rc = null;
        try {
            boolean acc = m.isAccessible();
            if (!acc) {
                m.setAccessible(true);
            }
            rc = m.invoke(object, args);
            if (!acc) {
                m.setAccessible(false);
            }
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "exception", e);
        } catch (IllegalAccessException e) {
            Log.w(TAG, "exception", e);
        } catch (InvocationTargetException e) {
            Log.w(TAG, "exception", e);
        } catch (Throwable e) {
            Log.w(TAG, "exception", e);
        }
        return rc;
    }

    public static Object invokeField(Object object, Field field) {
        Object rc = null;
        try {
            boolean acc = field.isAccessible();
            if (!acc) {
                field.setAccessible(true);
            }
            rc = field.get(object);
            if (!acc) {
                field.setAccessible(false);
            }
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "exception", e);
        } catch (IllegalAccessException e) {
            Log.w(TAG, "exception", e);
        } catch (Throwable e) {
            Log.w(TAG, "exception", e);
        }
        return rc;
    }

    public static Object invokeField(Object object, Field field, Object value) {
        Object rc = null;
        try {
            boolean acc = field.isAccessible();
            if (!acc) {
                field.setAccessible(true);
            }
            field.set(object, value);
            if (!acc) {
                field.setAccessible(false);
            }
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "exception", e);
        } catch (IllegalAccessException e) {
            Log.w(TAG, "exception", e);
        } catch (Throwable e) {
            Log.w(TAG, "exception", e);
        }
        return rc;
    }

    public static Object invokeMethod(Class<?> cls, Object object, String className, Method m,
            String methodName) {
        if (cls == null) {
            cls = getClass(object, className);
        }
        if (cls == null) {
            return null;
        }
        if (m == null) {
            m = getMethod(cls, methodName);
        }
        if (m == null) {
            return null;
        }
        return invokeMethod(object, m);
    }

}

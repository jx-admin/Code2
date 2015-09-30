package net.ds.effect.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.os.Build;
import android.view.MotionEvent;

public class CommonUtils {
    public static boolean isEnable() {
        return Build.VERSION.SDK_INT >= 14;
    }

    public static Object invokeMethod(Class<?> cls, Object object, String methodName, Class<?>[] paramTypes, Object... paramValues) throws Exception {
        Method method = cls.getDeclaredMethod(methodName, paramTypes);
        boolean accessible = method.isAccessible();
        try {
            method.setAccessible(true);
            return method.invoke(object, paramValues);
        } finally {
            method.setAccessible(accessible);
        }
    }

    public static Object invokeField(Class<?> cls, Object object, String fieldName) throws Exception {
        Field field = cls.getDeclaredField(fieldName);

        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            return field.get(object);
        } finally {
            field.setAccessible(accessible);
        }
    }
    
    public static <T> T forceInvoke(Object object, String methodName, Class<?>[] argsClasses, Object[] args) throws InvocationTargetException {
        Method method = null;
        Class<?> clazz = object.getClass();
        Exception innerException = null;
        while (clazz != null) {
            try {
                method = clazz.getDeclaredMethod(methodName, argsClasses);
            } catch (Exception e) {
                innerException = e;
            }
            if (method != null) {
                break;
            } else {
                clazz = clazz.getSuperclass();
            }
        }

        if (method == null) {
            throw new InvocationTargetException(innerException);
        }

        boolean asscessiableChanged = false;
        try {
            if (!method.isAccessible()) {
                method.setAccessible(true);
                asscessiableChanged = true;
            }
            return (T) method.invoke(object, args);
        } catch (IllegalArgumentException e) {
            throw new InvocationTargetException(e);
        } catch (IllegalAccessException e) {
            throw new InvocationTargetException(e);
        } finally {
            if (asscessiableChanged && method.isAccessible()) {
                method.setAccessible(false);
            }
        }
    }
    
    public static String getEventAction(MotionEvent e) {
        int action = e.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            return "down";
        } else if (action == MotionEvent.ACTION_MOVE) {
            return "move";
        } else if (action == MotionEvent.ACTION_UP) {
            return "up";
        } else if (action == MotionEvent.ACTION_CANCEL) {
            return "cancel";
        } else {
            return String.valueOf(action);
        }
    }
}

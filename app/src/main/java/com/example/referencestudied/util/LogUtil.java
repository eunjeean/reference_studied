package com.example.referencestudied.util;

import android.util.Log;

public class LogUtil {
    public static void v(String message) {
        String tag = "";
        // LogUtil의 성능 저하를 막기 위해 getStackTrace 호출을 한번만 하도록 변수 추가
        StackTraceElement traceElement = new Throwable().getStackTrace()[1];
        String temp = traceElement.getClassName();
        if (temp != null) {
            int lastDotPos = temp.lastIndexOf(".");
            if (((lastDotPos + 1) >= 0) && ((lastDotPos + 1) <= temp.length())) {
                tag = temp.substring(lastDotPos + 1);
            }
        } else {
            temp = "";
        }
        String methodName = traceElement.getMethodName();
        int lineNumber = traceElement.getLineNumber();

        Log.v(temp, "[" + tag + "] " + methodName + "()" + "[" + lineNumber + "]" + " >> " + message);
    }

    public static void i(String message) {
        String tag = "";
        // LogUtil의 성능 저하를 막기 위해 getStackTrace 호출을 한번만 하도록 변수 추가
        StackTraceElement traceElement = new Throwable().getStackTrace()[1];
        String temp = traceElement.getClassName();
        if (temp != null) {
            int lastDotPos = temp.lastIndexOf(".");
            if (((lastDotPos + 1) >= 0) && ((lastDotPos + 1) <= temp.length())) {
                tag = temp.substring(lastDotPos + 1);
            }
        } else {
            temp = "";
        }
        String methodName = traceElement.getMethodName();
        int lineNumber = traceElement.getLineNumber();

        Log.i(temp, "[" + tag + "] " + methodName + "()" + "[" + lineNumber + "]" + " >> " + message);
    }

    public static void d(String message) {
        String tag = "";
        // LogUtil의 성능 저하를 막기 위해 getStackTrace 호출을 한번만 하도록 변수 추가
        StackTraceElement traceElement = new Throwable().getStackTrace()[1];
        String temp = traceElement.getClassName();
        if (temp != null) {
            int lastDotPos = temp.lastIndexOf(".");
            if (((lastDotPos + 1) >= 0) && ((lastDotPos + 1) <= temp.length())) {
                tag = temp.substring(lastDotPos + 1);
            }
        } else {
            temp = "";
        }
        String methodName = traceElement.getMethodName();
        int lineNumber = traceElement.getLineNumber();

        Log.d(temp, "[" + tag + "] " + methodName + "()" + "[" + lineNumber + "]" + " >> " + message);
    }

    public static void w(String message) {
        String tag = "";
        // LogUtil의 성능 저하를 막기 위해 getStackTrace 호출을 한번만 하도록 변수 추가
        StackTraceElement traceElement = new Throwable().getStackTrace()[1];
        String temp = traceElement.getClassName();
        if (temp != null) {
            int lastDotPos = temp.lastIndexOf(".");
            if (((lastDotPos + 1) >= 0) && ((lastDotPos + 1) <= temp.length())) {
                tag = temp.substring(lastDotPos + 1);
            }
        } else {
            temp = "";
        }
        String methodName = traceElement.getMethodName();
        int lineNumber = traceElement.getLineNumber();

        Log.w(temp, "[" + tag + "] " + methodName + "()" + "[" + lineNumber + "]" + " >> " + message);
    }

    public static void e(String message) {
        String tag = "";
        // LogUtil의 성능 저하를 막기 위해 getStackTrace 호출을 한번만 하도록 변수 추가
        StackTraceElement traceElement = new Throwable().getStackTrace()[1];
        String temp = traceElement.getClassName();
        if (temp != null) {
            int lastDotPos = temp.lastIndexOf(".");
            if (((lastDotPos + 1) >= 0) && ((lastDotPos + 1) <= temp.length())) {
                tag = temp.substring(lastDotPos + 1);
            }
        } else {
            temp = "";
        }
        String methodName = traceElement.getMethodName();
        int lineNumber = traceElement.getLineNumber();

        Log.e(temp, "[" + tag + "] " + methodName + "()" + "[" + lineNumber + "]" + " >> " + message);
    }
}

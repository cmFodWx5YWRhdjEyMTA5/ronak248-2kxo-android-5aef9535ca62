package com.example.apimodule.ApiBase;

import java.lang.reflect.Field;

public class Utils {
    private static final String TAG = "Utils";

    public static String toString(Object object) {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(object.getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = object.getClass().getDeclaredFields();

        //print field names paired with their values
        for (Field field : fields) {
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                //requires access to private field:
                result.append(field.get(object));
            } catch (IllegalAccessException ex) {
                // ignored
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }
}
package kr.co.kimga.batch.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.isStatic;

public class ReflectionUtils {

    public static List<String> getFieldNames(Class<?> clazz) {
        ArrayList<String> fieldNames = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (isStatic(field.getModifiers()))
                continue;
            fieldNames.add(field.getName());
        }
        return fieldNames;
    }
}

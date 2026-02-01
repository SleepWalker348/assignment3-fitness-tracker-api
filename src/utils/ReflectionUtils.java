package utils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {
    public static void printClassInfo(Class<?> c) {
        System.out.println("Class Name: " + c.getSimpleName());
        System.out.println("Fields:");
        for (Field field : c.getDeclaredFields()) {
            System.out.println(" - " + field.getName() + " (" + field.getType().getSimpleName() + ")");
        }
        System.out.println("Methods:");
        for (Method method : c.getDeclaredMethods()) {
            System.out.println(" - " + method.getName());
        }
    }
}
package me.trojan.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionHelper {

    private ReflectionHelper() {}

    /* When using this method, ensure to use 'setAccessible(false)' after you are finished. */
    public static Field accessPrivateField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static Method accessPrivateMethod(Class<?> clazz, String methodName, Class<?> className) throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod(methodName, className);
        method.setAccessible(true);
        return method;
    }

    public static void removeFinalModifier(Field fieldToRemoveFinalModifierOn) throws NoSuchFieldException, IllegalAccessException {
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(fieldToRemoveFinalModifierOn, fieldToRemoveFinalModifierOn.getModifiers() & ~Modifier.FINAL);
        modifiersField.setAccessible(false);
    }
}

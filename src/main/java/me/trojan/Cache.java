package me.trojan;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* This class contains values that have to be cached, mostly for performance related reasons.
 * Getting a Field via Reflection once and then storing it is much less performance-intensive than retrieving the Field each time. */
public class Cache {
    public static Field mapBossInfo;
    public static Field title;
    public static Field subTitle;

    public static Field anvilText;

    public static Field tablistHeader;
    public static Field tablistFooter;

    public static boolean playerIsDead = false;

    public static Field iterators;

    public static Method func_148248_b;
    public static Field field_148241_e;
}

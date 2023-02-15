package me.trojan.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    private RegexUtils() {}

    public static String findMatch(String haystack, String regex, int groupToReturn) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(haystack);
        return matcher.find() ? matcher.group(groupToReturn) : "No match found.";
    }
}

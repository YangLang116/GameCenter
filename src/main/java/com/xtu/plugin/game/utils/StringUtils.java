package com.xtu.plugin.game.utils;

import org.jetbrains.annotations.Nullable;

public class StringUtils {

    public static boolean isEmpty(@Nullable String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmpty(@Nullable String str) {
        return !isEmpty(str);
    }
}

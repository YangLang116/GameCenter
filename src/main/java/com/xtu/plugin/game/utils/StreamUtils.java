package com.xtu.plugin.game.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StreamUtils {

    @Nullable
    public static String readTextFromResource(@NotNull String name) {
        InputStream stream = StreamUtils.class.getResourceAsStream(name);
        if (stream == null) return null;
        byte[] data = readFromStream(stream);
        if (data == null) return null;
        return new String(data, StandardCharsets.UTF_8);
    }

    private static byte[] readFromStream(@NotNull InputStream inputStream) {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            return null;
        } finally {
            CloseUtils.close(inputStream);
            CloseUtils.close(outputStream);
        }
    }
}

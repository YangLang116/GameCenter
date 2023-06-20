package com.xtu.plugin.game.utils;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class StreamUtils {

    public static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readTextFromResource(@NotNull String name) {
        InputStream resourceAsStream = StreamUtils.class.getClassLoader().getResourceAsStream(name);
        if (resourceAsStream == null) return null;
        return readFromStream(resourceAsStream);
    }

    public static String readFromStream(@NotNull InputStream inputStream) {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        } finally {
            closeStream(inputStream);
            closeStream(outputStream);
        }
    }

    public static void writeToStream(@NotNull OutputStream outputStream, @NotNull String dataStr) throws IOException {
        try {
            outputStream.write(dataStr.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } finally {
            closeStream(outputStream);
        }
    }
}

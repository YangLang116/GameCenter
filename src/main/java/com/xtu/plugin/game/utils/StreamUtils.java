package com.xtu.plugin.game.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class StreamUtils {

    @Nullable
    public static String readTextFromUrl(@NotNull String url, @NotNull String contentType) {
        try {
            URL netUrl = new URL(url);
            URLConnection urlConnection = netUrl.openConnection();
            urlConnection.setReadTimeout(5 * 1000);
            urlConnection.setRequestProperty("Content-Type", contentType);
            InputStream inputStream = urlConnection.getInputStream();
            return readFromStream(inputStream);
        } catch (IOException e) {
            LogUtils.error("StreamUtils", e);
            return null;
        }
    }

    @Nullable
    public static String readTextFromResource(@NotNull String name) {
        InputStream stream = StreamUtils.class.getClassLoader().getResourceAsStream(name);
        if (stream == null) return null;
        return readFromStream(stream);
    }

    @Nullable
    public static String readFromStream(@NotNull InputStream inputStream) {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        } finally {
            CloseUtils.close(inputStream);
            CloseUtils.close(outputStream);
        }
    }

    public static void writeToStream(@NotNull OutputStream outputStream, @NotNull String dataStr) throws IOException {
        try {
            outputStream.write(dataStr.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } finally {
            CloseUtils.close(outputStream);
        }
    }
}

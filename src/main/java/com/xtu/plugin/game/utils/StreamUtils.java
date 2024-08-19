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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class StreamUtils {

    @Nullable
    public static Map<String, String> loadConfig(@NotNull String path) {
        InputStream configStream = null;
        try {
            configStream = StreamUtils.class.getResourceAsStream(path);
            Properties properties = new Properties();
            properties.load(configStream);
            Set<Map.Entry<Object, Object>> entries = properties.entrySet();
            Map<String, String> pairList = new HashMap<>();
            for (Map.Entry<Object, Object> entry : entries) {
                String key = entry.getKey().toString().trim();
                String value = entry.getValue().toString().trim();
                pairList.put(key, value);
            }
            return pairList;
        } catch (IOException e) {
            LogUtils.error("StreamUtils", e);
            return null;
        } finally {
            CloseUtils.close(configStream);
        }
    }

    public static byte[] readDataFromUrl(@NotNull String url) {
        try {
            URL netUrl = new URL(url);
            URLConnection urlConnection = netUrl.openConnection();
            urlConnection.setReadTimeout(5 * 1000);
            urlConnection.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
            InputStream inputStream = urlConnection.getInputStream();
            return readFromStream(inputStream);
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    public static String readTextFromResource(@NotNull String name) {
        InputStream stream = StreamUtils.class.getResourceAsStream(name);
        if (stream == null) return null;
        byte[] data = readFromStream(stream);
        if (data == null) return null;
        return new String(data, StandardCharsets.UTF_8);
    }


    public static byte[] readFromStream(@NotNull InputStream inputStream) {
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

    public static void writeToStream(@NotNull OutputStream outputStream, @NotNull String dataStr) throws IOException {
        try {
            outputStream.write(dataStr.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } finally {
            CloseUtils.close(outputStream);
        }
    }
}

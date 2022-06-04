package com.xtu.plugin.game.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
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

    public static String readText(InputStream inputStream) {
        ByteArrayOutputStream outputStream = null;
        int len = 0;
        byte[] buff = new byte[1024];
        try {
            outputStream = new ByteArrayOutputStream();
            while ((len = inputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
            }
            return outputStream.toString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeStream(outputStream);
        }
    }
}

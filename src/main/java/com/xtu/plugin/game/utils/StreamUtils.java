package com.xtu.plugin.game.utils;

import java.io.Closeable;
import java.io.IOException;

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
}

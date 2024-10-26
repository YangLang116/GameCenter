package com.xtu.plugin.game.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    @Nullable
    public static String readAsString(@NotNull String path) {
        Path targetPath = Paths.get(path);
        try {
            return Files.readString(targetPath);
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] readAsBytes(@NotNull String path) {
        Path targetPath = Paths.get(path);
        try {
            return Files.readAllBytes(targetPath);
        } catch (IOException e) {
            return null;
        }
    }
}

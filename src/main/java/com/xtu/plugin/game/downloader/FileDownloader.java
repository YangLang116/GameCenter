package com.xtu.plugin.game.downloader;

import com.intellij.openapi.application.PathManager;
import com.xtu.plugin.game.utils.CloseUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileDownloader {

    private final OkHttpClient client;

    private final ExecutorService executor;

    public FileDownloader() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();
        this.executor = Executors.newFixedThreadPool(10);
    }

    public CompletableFuture<String> downloadFileAsync(@NotNull String url, boolean cacheFirst) {
        return CompletableFuture.supplyAsync(() -> {
            if (cacheFirst) {
                return downloadFileWithCacheFirst(url);
            } else {
                return downloadFileWithNetworkFirst(url);
            }
        }, executor);
    }

    @Nullable
    public String downloadFileWithNetworkFirst(@NotNull String url) {
        Path targetPath = getTargetPath(url);
        String downloadPath = loadFromNetwork(url, targetPath);
        if (downloadPath != null) {
            return downloadPath;
        }
        return loadFromDisk(targetPath);
    }

    @Nullable
    public String downloadFileWithCacheFirst(@NotNull String url) {
        Path targetPath = getTargetPath(url);
        String cachePath = loadFromDisk(targetPath);
        if (cachePath != null) {
            return cachePath;
        }
        return loadFromNetwork(url, targetPath);
    }

    @NotNull
    private Path getTargetPath(@NotNull String url) {
        int index = url.lastIndexOf("/");
        String fileName = url.substring(index + 1);
        String savePath = PathManager.getPluginTempPath() + "/GameCenter/" + fileName;
        return Paths.get(savePath);
    }

    @Nullable
    private String loadFromDisk(@NotNull Path targetPath) {
        if (Files.exists(targetPath)) {
            return targetPath.toString();
        } else {
            return null;
        }
    }

    @Nullable
    private String loadFromNetwork(@NotNull String url, @NotNull Path savePath) {
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) return null;
            ResponseBody body = response.body();
            if (body == null) return null;
            InputStream inputStream = body.byteStream();
            return writeFile(inputStream, savePath);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    private String writeFile(@NotNull InputStream inputStream, @NotNull Path targetPath) {
        Path tempPath = Paths.get(targetPath + ".temp");
        OutputStream outputStream = null;
        try {
            Files.createDirectories(targetPath.getParent());
            outputStream = Files.newOutputStream(tempPath);
            int bytesRead;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return targetPath.toString();
        } catch (Exception e) {
            CloseUtils.close(inputStream);
            CloseUtils.close(outputStream);
            return null;
        }
    }
}

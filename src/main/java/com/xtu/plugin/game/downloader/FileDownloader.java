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

public class FileDownloader {

    private final OkHttpClient client = new OkHttpClient();

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public CompletableFuture<String> downloadFileAsync(@NotNull String url) {
        return CompletableFuture.supplyAsync(() -> downloadFile(url), executor);
    }

    private String getFileName(@NotNull String url) {
        int index = url.lastIndexOf("/");
        return url.substring(index + 1);
    }

    @Nullable
    public String downloadFile(@NotNull String url) {
        String fileName = getFileName(url);
        String savePath = PathManager.getPluginTempPath() + "/" + fileName;
        Path targetPath = Paths.get(savePath);
        if (Files.exists(targetPath)) {
            return targetPath.toString();
        }
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) return null;
            ResponseBody body = response.body();
            if (body == null) return null;
            InputStream inputStream = body.byteStream();
            return writeFile(inputStream, targetPath);
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

    public void dispose() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }
}

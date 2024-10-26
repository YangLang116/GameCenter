package com.xtu.plugin.game.utils;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.util.SystemInfo;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.IOException;

public class AdviceUtils {

    private static final String APP_KEY = "GameCenter";

    @SuppressWarnings("HttpUrlsUsage")
    private static final String sURL = "http://iflutter.toolu.cn/api/advice";

    public static void submitData(@Nullable Project project, @NotNull String title, @NotNull String content) {
        JSONObject jsonData = new JSONObject();
        jsonData.put("title", title);
        jsonData.put("content", content);
        jsonData.put("app_key", APP_KEY);
        jsonData.put("os", SystemInfo.getOsNameAndVersion());
        jsonData.put("version", VersionUtils.getPluginVersion());

        Application application = ApplicationManager.getApplication();
        application.executeOnPooledThread(() -> {
            boolean ignore = sendData(jsonData.toString());
            if (project != null) {
                ToastUtil.make(project, MessageType.INFO, "thank you for submitting ~");
            }
        });
    }

    private static boolean sendData(@NotNull String data) {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(data, JSON);
        Request request = new Request.Builder()
                .url(sURL)
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            return false;
        }
    }
}

package com.xtu.plugin.game.utils;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

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
            try {
                sendData(jsonData.toString());
                if (project != null) ToastUtil.make(project, MessageType.INFO, "thank you for submitting ~");
            } catch (Exception e) {
                if (project != null) ToastUtil.make(project, MessageType.ERROR, e.getMessage());
            }
        });
    }

    private static void sendData(@NotNull String data) throws Exception {
        URL url = new URL(sURL);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setReadTimeout(5 * 1000);
        urlConnection.setDoOutput(true);
        StreamUtils.writeToStream(urlConnection.getOutputStream(), data);
        urlConnection.getResponseCode();
    }
}

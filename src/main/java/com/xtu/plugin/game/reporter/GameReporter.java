package com.xtu.plugin.game.reporter;

import com.google.gson.Gson;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.util.SystemInfo;
import com.xtu.plugin.game.res.GameResManager;
import com.xtu.plugin.game.utils.ToastUtil;
import com.xtu.plugin.game.utils.VersionUtils;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GameReporter {

    private final Gson gson;
    private final OkHttpClient client;

    private GameReporter() {
        this.gson = new Gson();
        this.client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build();
    }

    private static final GameReporter sInstance = new GameReporter();

    public static GameReporter getInstance() {
        return sInstance;
    }

    public void submitAdvice(@Nullable Project project, @NotNull String title, @NotNull String content) {
        ReportData data = new ReportData(title, content);
        String url = GameResManager.getInstance().getAdviceUrl();
        submitJsonData(url, data, new CallbackAdapter() {
            @Override
            void onFinish() {
                if (project != null) {
                    ToastUtil.make(project, MessageType.INFO, "thank you for submitting ~");
                }
            }
        });
    }

    @SuppressWarnings("SameParameterValue")
    private void submitJsonData(@NotNull String url, @NotNull ReportData data, @NotNull Callback callback) {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        String jsonStr = gson.toJson(data);
        RequestBody requestBody = RequestBody.create(jsonStr, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    static abstract class CallbackAdapter implements Callback {

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            onFinish();
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) {
            onFinish();
        }

        abstract void onFinish();
    }

    static class ReportData {

        private final String title;
        private final String content;
        private final String os = SystemInfo.getOsNameAndVersion();
        private final String version = VersionUtils.getPluginVersion();

        private ReportData(@NotNull String title, @NotNull String content) {
            this.title = title;
            this.content = content;
        }

        @Override
        public String toString() {
            String appKey = "GameCenter";
            return "ReportData{" +
                    "title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", app_key='" + appKey + '\'' +
                    ", os='" + os + '\'' +
                    ", version='" + version + '\'' +
                    '}';
        }
    }
}

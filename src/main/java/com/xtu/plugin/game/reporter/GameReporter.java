package com.xtu.plugin.game.reporter;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.util.SystemInfo;
import com.xtu.plugin.game.res.GameResManager;
import com.xtu.plugin.game.utils.ToastUtil;
import com.xtu.plugin.game.utils.VersionUtils;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GameReporter {

    private final OkHttpClient client;

    private GameReporter() {
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
        JSONObject jsonData = new JSONObject();
        jsonData.put("title", title);
        jsonData.put("content", content);
        jsonData.put("app_key", "GameCenter");
        jsonData.put("os", SystemInfo.getOsNameAndVersion());
        jsonData.put("version", VersionUtils.getPluginVersion());

        String url = GameResManager.getInstance().getAdviceUrl();
        submitJsonData(url, jsonData, new CallbackAdapter() {
            @Override
            void onFinish() {
                if (project != null) {
                    ToastUtil.make(project, MessageType.INFO, "thank you for submitting ~");
                }
            }
        });
    }

    @SuppressWarnings("SameParameterValue")
    private void submitJsonData(@NotNull String url, @NotNull JSONObject jsonData, @NotNull Callback callback) {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonData.toString(), JSON);
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
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            onFinish();
        }

        abstract void onFinish();
    }
}

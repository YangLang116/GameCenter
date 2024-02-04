package com.xtu.plugin.game.utils;

import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.util.SystemInfo;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class AdviceUtils {

    private static final String APP_KEY = "GameCenter";

    @SuppressWarnings("HttpUrlsUsage")
    private static final String sURL = "http://iflutter.toolu.cn/api/advice";

    public static void submitData(String title, String content) {
        JSONObject jsonData = new JSONObject();
        jsonData.put("title", title);
        jsonData.put("content", content);
        jsonData.put("app_key", APP_KEY);
        jsonData.put("os", SystemInfo.getOsNameAndVersion());
        jsonData.put("version", VersionUtils.getPluginVersion());
        String dataStr = jsonData.toString();
        try {
            URL url = new URL(sURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(5 * 1000);
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setDoOutput(true);
            StreamUtils.writeToStream(urlConnection.getOutputStream(), dataStr);
            urlConnection.getResponseCode();
            ToastUtil.make(MessageType.INFO, "thank you for submitting ~");
        } catch (IOException e) {
            ToastUtil.make(MessageType.ERROR, e.getMessage());
        }
    }
}

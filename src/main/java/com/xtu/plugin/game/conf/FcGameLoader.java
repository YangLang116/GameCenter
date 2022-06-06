package com.xtu.plugin.game.conf;

import com.xtu.plugin.game.utils.StreamUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FcGameLoader {

    private FcGameLoader() {
    }

    private static final FcGameLoader sInstance = new FcGameLoader();

    public static FcGameLoader getInstance() {
        return sInstance;
    }

    private final List<Game> gameList = new ArrayList<>();

    public void load() {
        try {
            URL url = new URL("https://game.toolu.cn/conf.json");
            URLConnection urlConnection = url.openConnection();
            urlConnection.setReadTimeout(5 * 1000);
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            String content = StreamUtils.readFromStream(inputStream);
            if (content == null) return;
            JSONArray gameJsonArray = new JSONArray(content);
            int length = gameJsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject gameObj = (JSONObject) gameJsonArray.get(i);
                String gameName = gameObj.optString("name");
                String gamePath = gameObj.optString("path");
                this.gameList.add(new Game(gameName, gamePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public static class Game {

        public String name;
        public String path;

        public Game(String name, String path) {
            this.name = name;
            this.path = path;
        }

        @Override
        public String toString() {
            return "Game{" +
                    "name='" + name + '\'' +
                    ", path='" + path + '\'' +
                    '}';
        }
    }
}

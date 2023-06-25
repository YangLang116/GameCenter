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

@SuppressWarnings("SpellCheckingInspection")
public class FcGameLoader {

    private FcGameLoader() {
    }

    private static final FcGameLoader sInstance = new FcGameLoader();

    public static FcGameLoader getInstance() {
        return sInstance;
    }

    public final List<Game> actionGameList = new ArrayList<>(); //动作类
    public final List<Game> adventureGameList = new ArrayList<>(); //冒险类
    public final List<Game> chessGameList = new ArrayList<>(); //棋牌类
    public final List<Game> collectionGameList = new ArrayList<>(); //合辑类
    public final List<Game> desktopGameList = new ArrayList<>(); //桌面类
    public final List<Game> eduGameList = new ArrayList<>(); //益智类
    public final List<Game> racingGameList = new ArrayList<>(); //竞速类
    public final List<Game> rolePlayGameList = new ArrayList<>(); //角色扮演
    public final List<Game> shooterGameList = new ArrayList<>(); //射击类
    public final List<Game> sportGameList = new ArrayList<>(); //运动类
    public final List<Game> strategyGameList = new ArrayList<>(); //策略类
    public final List<Game> strategySimGameList = new ArrayList<>(); //战略模拟

    public void load() {
        loadGame("nes_action.json", actionGameList);
        loadGame("nes_adventure.json", adventureGameList);
        loadGame("nes_chess.json", chessGameList);
        loadGame("nes_collection.json", collectionGameList);
        loadGame("nes_desktop.json", desktopGameList);
        loadGame("nes_edu.json", eduGameList);
        loadGame("nes_racing.json", racingGameList);
        loadGame("nes_roleplay.json", rolePlayGameList);
        loadGame("nes_shooter.json", shooterGameList);
        loadGame("nes_sports.json", sportGameList);
        loadGame("nes_strategy.json", strategyGameList);
        loadGame("nes_strategy_simulation.json", strategySimGameList);
    }

    private void loadGame(String config, List<Game> gameList) {
        try {
            URL url = new URL("https://game.toolu.cn/category/" + config);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setReadTimeout(5 * 1000);
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            InputStream inputStream = urlConnection.getInputStream();
            String content = StreamUtils.readFromStream(inputStream);
            if (content == null) return;
            JSONArray gameJsonArray = new JSONArray(content);
            int length = gameJsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject gameObj = (JSONObject) gameJsonArray.get(i);
                String game_name = gameObj.optString("name");
                String game_desc = gameObj.optString("desc");
                String game_icon = gameObj.optString("icon");
                String game_url = gameObj.optString("url");
                gameList.add(new Game(game_name, game_desc, game_icon, game_url));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Game {

        public String name;
        public String desc;
        public String icon;
        public String url;

        public Game(String name, String desc, String icon, String url) {
            this.name = name;
            this.desc = desc;
            this.icon = icon;
            this.url = url;
        }
    }
}

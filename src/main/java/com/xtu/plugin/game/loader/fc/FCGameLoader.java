package com.xtu.plugin.game.loader.fc;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.xtu.plugin.game.utils.StreamUtils;
import org.apache.xerces.impl.dv.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class FCGameLoader {

    private FCGameLoader() {
    }

    private static final FCGameLoader sInstance = new FCGameLoader();

    public static FCGameLoader getInstance() {
        return sInstance;
    }

    public final List<FCGame> actionGameList = new ArrayList<>();
    public final List<FCGame> adventureGameList = new ArrayList<>();
    public final List<FCGame> chessGameList = new ArrayList<>();
    public final List<FCGame> comGameList = new ArrayList<>();
    public final List<FCGame> tableGameList = new ArrayList<>();
    public final List<FCGame> puzzleGameList = new ArrayList<>();
    public final List<FCGame> racingGameList = new ArrayList<>();
    public final List<FCGame> rolePlayGameList = new ArrayList<>();
    public final List<FCGame> shooterGameList = new ArrayList<>();
    public final List<FCGame> sportGameList = new ArrayList<>();
    public final List<FCGame> strategyGameList = new ArrayList<>();
    public final List<FCGame> simGameList = new ArrayList<>();

    public void load() {
        Application application = ApplicationManager.getApplication();
        application.executeOnPooledThread(() -> {
            loadGame("nes_action.txt", actionGameList);
            loadGame("nes_adventure.txt", adventureGameList);
            loadGame("nes_chess.txt", chessGameList);
            loadGame("nes_collection.txt", comGameList);
            loadGame("nes_desktop.txt", tableGameList);
            loadGame("nes_edu.txt", puzzleGameList);
            loadGame("nes_racing.txt", racingGameList);
            loadGame("nes_roleplay.txt", rolePlayGameList);
            loadGame("nes_shooter.txt", shooterGameList);
            loadGame("nes_sports.txt", sportGameList);
            loadGame("nes_strategy.txt", strategyGameList);
            loadGame("nes_strategy_simulation.txt", simGameList);
        });
    }

    private void loadGame(String config, List<FCGame> gameList) {
        try {
            URL url = new URL("https://gitee.com/YangLang116/nes-game-list/raw/config/category/" + config);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setReadTimeout(5 * 1000);
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            InputStream inputStream = urlConnection.getInputStream();
            String content = StreamUtils.readFromStream(inputStream);
            if (content == null) return;
            String jsonStr = new String(Base64.decode(content), StandardCharsets.UTF_8);
            JSONArray gameJsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < gameJsonArray.length(); i++) {
                JSONObject gameObj = (JSONObject) gameJsonArray.get(i);
                String game_name = gameObj.optString("name");
                String game_desc = gameObj.optString("desc");
                String game_icon = gameObj.optString("icon");
                String game_url = gameObj.optString("url");
                gameList.add(new FCGame(game_name, game_desc, game_icon, game_url));
            }
        } catch (IOException e) {
            //ignore
        }
    }
}

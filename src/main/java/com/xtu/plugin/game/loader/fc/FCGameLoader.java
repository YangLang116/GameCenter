package com.xtu.plugin.game.loader.fc;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.xtu.plugin.game.constant.GameConst;
import com.xtu.plugin.game.utils.StreamUtils;
import org.apache.xerces.impl.dv.util.Base64;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

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
        initGame("nes_action.txt", actionGameList);
        initGame("nes_adventure.txt", adventureGameList);
        initGame("nes_chess.txt", chessGameList);
        initGame("nes_collection.txt", comGameList);
        initGame("nes_desktop.txt", tableGameList);
        initGame("nes_edu.txt", puzzleGameList);
        initGame("nes_racing.txt", racingGameList);
        initGame("nes_roleplay.txt", rolePlayGameList);
        initGame("nes_shooter.txt", shooterGameList);
        initGame("nes_sports.txt", sportGameList);
        initGame("nes_strategy.txt", strategyGameList);
        initGame("nes_strategy_simulation.txt", simGameList);
    }

    private void initGame(@NotNull String config, @NotNull List<FCGame> gameList) {
        Application application = ApplicationManager.getApplication();
        application.executeOnPooledThread(() -> {
            List<FCGame> fcGames = loadGame(config);
            if (fcGames == null) return;
            application.invokeLater(() -> gameList.addAll(fcGames));
        });
    }

    @Nullable
    private List<FCGame> loadGame(@NotNull String configFile) {
        String url = GameConst.PREFIX_CONFIG + configFile;
        String content = StreamUtils.readTextFromUrl(url, "application/json;charset=UTF-8");
        if (content == null) return null;
        return parseGameList(content);
    }

    @NotNull
    private static List<FCGame> parseGameList(@NotNull String content) {
        List<FCGame> gameList = new ArrayList<>();
        String jsonStr = new String(Base64.decode(content));
        JSONArray gameJsonArray = new JSONArray(jsonStr);
        for (int i = 0; i < gameJsonArray.length(); i++) {
            JSONObject gameObj = (JSONObject) gameJsonArray.get(i);
            String game_name = gameObj.optString("name");
            String game_desc = gameObj.optString("desc");
            String game_icon = gameObj.optString("icon");
            String game_url = gameObj.optString("url");
            gameList.add(new FCGame(game_name, game_desc, game_icon, game_url));
        }
        return gameList;
    }
}

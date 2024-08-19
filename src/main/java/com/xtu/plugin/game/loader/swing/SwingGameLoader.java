package com.xtu.plugin.game.loader.swing;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.xtu.plugin.game.utils.StreamUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SwingGameLoader {

    private static final SwingGameLoader sInstance = new SwingGameLoader();

    private SwingGameLoader() {
    }

    public static SwingGameLoader getInstance() {
        return sInstance;
    }

    private final List<SwingGame> gameList = new ArrayList<>();

    public void load() {
        Application application = ApplicationManager.getApplication();
        application.executeOnPooledThread(() -> {
            List<SwingGame> swingGames = loadGame();
            application.invokeLater(() -> gameList.addAll(swingGames));
        });
    }

    @NotNull
    private List<SwingGame> loadGame() {
        Map<String, String> configList = StreamUtils.loadConfig("/game/swing/conf.properties");
        assert configList != null;
        List<SwingGame> gameList = new ArrayList<>();
        for (Map.Entry<String, String> entry : configList.entrySet()) {
            SwingGame game = new SwingGame(entry.getKey(), entry.getValue());
            gameList.add(game);
        }
        return gameList;
    }

    @NotNull
    public List<SwingGame> getGameList() {
        return this.gameList;
    }
}

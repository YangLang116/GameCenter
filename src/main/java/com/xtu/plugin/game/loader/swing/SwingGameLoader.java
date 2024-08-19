package com.xtu.plugin.game.loader.swing;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.xtu.plugin.game.utils.CloseUtils;
import com.xtu.plugin.game.utils.LogUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
            if (swingGames == null) return;
            application.invokeLater(() -> gameList.addAll(swingGames));
        });
    }

    @Nullable
    private List<SwingGame> loadGame() {
        InputStream configStream = null;
        try {
            configStream = SwingGameLoader.class.getResourceAsStream("/game/swing/conf.properties");
            Properties properties = new Properties();
            properties.load(configStream);
            Set<Map.Entry<Object, Object>> entries = properties.entrySet();
            List<SwingGame> gameList = new ArrayList<>();
            for (Map.Entry<Object, Object> gameEntry : entries) {
                String gameName = gameEntry.getKey().toString().trim();
                String entryClass = gameEntry.getValue().toString().trim();
                SwingGame game = new SwingGame(gameName, entryClass);
                gameList.add(game);
            }
            return gameList;
        } catch (IOException e) {
            LogUtils.error("SwingGameLoader", e);
            return null;
        } finally {
            CloseUtils.close(configStream);
        }
    }

    public List<SwingGame> getGameList() {
        return this.gameList;
    }
}

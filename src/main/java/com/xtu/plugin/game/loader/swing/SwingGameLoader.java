package com.xtu.plugin.game.loader.swing;

import com.xtu.plugin.game.utils.CloseUtils;

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
        InputStream configStream = null;
        try {
            configStream = SwingGameLoader.class.getResourceAsStream("/game/swing/conf.properties");
            Properties properties = new Properties();
            properties.load(configStream);
            Set<Map.Entry<Object, Object>> entries = properties.entrySet();
            for (Map.Entry<Object, Object> gameEntry : entries) {
                String gameName = gameEntry.getKey().toString().trim();
                String entryClass = gameEntry.getValue().toString().trim();
                SwingGame game = new SwingGame(gameName, entryClass);
                this.gameList.add(game);
            }
        } catch (IOException e) {
            //ignore
        } finally {
            CloseUtils.close(configStream);
        }
    }

    public List<SwingGame> getGameList() {
        return this.gameList;
    }
}

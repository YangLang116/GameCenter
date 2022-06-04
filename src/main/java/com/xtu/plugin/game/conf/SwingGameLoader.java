package com.xtu.plugin.game.conf;

import com.xtu.plugin.game.utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class SwingGameLoader {

    private static final SwingGameLoader sInstance = new SwingGameLoader();

    private SwingGameLoader() {
    }

    public static SwingGameLoader getInstance() {
        return sInstance;
    }

    private final Map<String, String> gameList = new HashMap<>();

    public void load() {
        InputStream configStream = null;
        try {
            configStream = SwingGameLoader.class.getResourceAsStream("/game/swing/conf.properties");
            Properties properties = new Properties();
            properties.load(configStream);
            Set<Object> keySet = properties.keySet();
            for (Object key : keySet) {
                String gameName = key.toString().trim();
                String mainClass = ((String) properties.get(key)).trim();
                this.gameList.put(gameName, mainClass);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            StreamUtils.closeStream(configStream);
        }
    }

    public Map<String, String> getGameList() {
        return this.gameList;
    }
}

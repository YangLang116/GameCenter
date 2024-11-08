package com.xtu.plugin.game.loader;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.xtu.plugin.game.utils.StreamUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public abstract class GameLoader {

    private final String configFile;

    public GameLoader(@NotNull String configFile) {
        this.configFile = configFile;
    }

    public void load() {
        Application application = ApplicationManager.getApplication();
        application.executeOnPooledThread(() -> {
            Map<String, String> config = readConfig(configFile);
            if (config == null) return;
            parseConfig(config);
        });
    }

    public abstract void parseConfig(@NotNull Map<String, String> config);

    @Nullable
    private static Map<String, String> readConfig(@NotNull String path) {
        try (InputStream stream = StreamUtils.class.getResourceAsStream(path)) {
            Properties properties = new Properties();
            properties.load(stream);
            Set<Map.Entry<Object, Object>> entries = properties.entrySet();
            Map<String, String> pairList = new HashMap<>();
            for (Map.Entry<Object, Object> entry : entries) {
                String key = entry.getKey().toString().trim();
                String value = entry.getValue().toString().trim();
                pairList.put(key, value);
            }
            return pairList;
        } catch (IOException e) {
            return null;
        }
    }
}

package com.xtu.plugin.game.loader.swing;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.xtu.plugin.game.loader.GameLoader;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SwingGameLoader extends GameLoader {

    private SwingGameLoader() {
        super("/game/swing/conf.properties");
    }

    private static final SwingGameLoader sInstance = new SwingGameLoader();

    public static SwingGameLoader getInstance() {
        return sInstance;
    }

    private final List<SwingGame> gameList = new ArrayList<>();

    @Override
    public void parseConfig(@NotNull Map<String, String> config) {
        List<SwingGame> dataList = new ArrayList<>();
        for (Map.Entry<String, String> entry : config.entrySet()) {
            SwingGame game = new SwingGame(entry.getKey(), entry.getValue());
            dataList.add(game);
        }
        Application application = ApplicationManager.getApplication();
        application.invokeLater(() -> gameList.addAll(dataList));
    }

    @NotNull
    public List<SwingGame> getGameList() {
        return this.gameList;
    }
}

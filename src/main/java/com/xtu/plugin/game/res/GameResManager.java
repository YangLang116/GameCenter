package com.xtu.plugin.game.res;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("HttpUrlsUsage")
public class GameResManager {

    private GameResManager() {
    }

    private static final GameResManager sInstance = new GameResManager();

    public static GameResManager getInstance() {
        return sInstance;
    }

    @NotNull
    public String getRepo() {
        return "https://github.com/YangLang116/nes-game-list";
    }

    @NotNull
    public String getGameNote() {
        return "Because there are too many games, we cannot ensure that every game can run normally. ";
    }

    @NotNull
    public String getConfigUrl(@NotNull String name) {
        return "http://iflutter.toolu.cn/nes-game-list/category/" + name;
    }

    @NotNull
    public String getResUrl(@NotNull String name) {
        return "http://iflutter.toolu.cn/nes-game-list/nes_list/" + name;
    }

    @NotNull
    public String getAdviceUrl() {
        return "http://iflutter.toolu.cn/api/advice";
    }
}

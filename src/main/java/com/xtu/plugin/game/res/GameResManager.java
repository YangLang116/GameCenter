package com.xtu.plugin.game.res;

import org.jetbrains.annotations.NotNull;

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
        return "https://gitee.com/YangLang116/nes-game-list/raw/config/category/" + name;
    }

    @NotNull
    public String getResUrl(@NotNull String name) {
        return "https://gitee.com/YangLang116/nes-game-list/raw/config/nes_list/" + name;
    }
}

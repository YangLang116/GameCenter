package com.xtu.plugin.game.loader.fc.entity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FCGameCategory implements Comparable<FCGameCategory> {

    public final String name;
    public final String path;
    public final List<FCGame> games = new ArrayList<>();

    public FCGameCategory(@NotNull String name, @NotNull String path) {
        this.name = name;
        this.path = path;
    }

    public FCGameCategory(@NotNull String name, @NotNull List<FCGame> games) {
        this.name = name;
        this.path = null;
        this.games.addAll(games);
    }

    public void updateGames(@NotNull List<FCGame> data) {
        this.games.clear();
        this.games.addAll(data);
    }

    public boolean noGames() {
        return games.isEmpty();
    }

    @Override
    public int compareTo(@NotNull FCGameCategory other) {
        return name.compareTo(other.name);
    }
}

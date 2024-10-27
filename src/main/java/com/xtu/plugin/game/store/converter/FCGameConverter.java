package com.xtu.plugin.game.store.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.util.xmlb.Converter;
import com.xtu.plugin.game.loader.fc.entity.FCGame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FCGameConverter extends Converter<List<FCGame>> {

    private final Gson gson = new Gson();

    @Nullable
    @Override
    public List<FCGame> fromString(@NotNull String s) {
        return gson.fromJson(s, new TypeToken<>() {
        });
    }

    @Nullable
    @Override
    public String toString(List<FCGame> fcGames) {
        return gson.toJson(fcGames);
    }
}

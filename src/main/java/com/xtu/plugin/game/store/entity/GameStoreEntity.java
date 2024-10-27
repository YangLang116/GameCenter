package com.xtu.plugin.game.store.entity;

import com.intellij.util.xmlb.annotations.OptionTag;
import com.xtu.plugin.game.loader.fc.entity.FCGame;
import com.xtu.plugin.game.store.converter.FCGameConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameStoreEntity {

    @OptionTag(converter = FCGameConverter.class)
    public List<FCGame> favoriteGame = new ArrayList<>();

    public GameStoreEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameStoreEntity that = (GameStoreEntity) o;
        return Objects.equals(favoriteGame, that.favoriteGame);
    }

    @Override
    public String toString() {
        return "GameStoreEntity{" +
                "favoriteGame=" + favoriteGame +
                '}';
    }
}

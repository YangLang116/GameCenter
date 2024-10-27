package com.xtu.plugin.game.store;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.xtu.plugin.game.loader.fc.entity.FCGame;
import com.xtu.plugin.game.loader.fc.entity.FCGameCategory;
import com.xtu.plugin.game.store.entity.GameStoreEntity;
import org.jetbrains.annotations.NotNull;

@State(name = "GameCenter", storages = {@Storage("GameCenter.xml")})
public class GameStorageService implements PersistentStateComponent<GameStoreEntity> {

    public static GameStorageService getService() {
        return ApplicationManager.getApplication().getService(GameStorageService.class);
    }

    private GameStoreEntity storageEntity = new GameStoreEntity();

    @Override
    @NotNull
    public GameStoreEntity getState() {
        return storageEntity;
    }

    @Override
    public void loadState(@NotNull GameStoreEntity state) {
        this.storageEntity = state;
    }

    public FCGameCategory getFavoriteCategory() {
        return new FCGameCategory("Favorite", storageEntity.favoriteGame);
    }

    public boolean isFavorite(@NotNull FCGame game) {
        return storageEntity.favoriteGame.contains(game);
    }

    public void addFavoriteGame(@NotNull FCGame game) {
        storageEntity.favoriteGame.add(game);
    }

    public void removeFavoriteGame(@NotNull FCGame game) {
        storageEntity.favoriteGame.remove(game);
    }
}

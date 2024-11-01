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

    private static GameStoreEntity getStore() {
        return ApplicationManager.getApplication().getService(GameStorageService.class).storageEntity;
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

    @NotNull
    public static FCGameCategory getFavoriteCategory() {
        return new FCGameCategory("Favorite", getStore().favoriteGame);
    }

    public static boolean isFavorite(@NotNull FCGame game) {
        return getStore().favoriteGame.contains(game);
    }

    public static void addFavoriteGame(@NotNull FCGame game) {
        getStore().favoriteGame.add(game);
    }

    public static void removeFavoriteGame(@NotNull FCGame game) {
        getStore().favoriteGame.remove(game);
    }

    @NotNull
    public static String getGameRepo() {
        return getStore().gameRepo;
    }

    public static void setGameRepo(@NotNull String repo) {
        getStore().gameRepo = repo;
    }
}

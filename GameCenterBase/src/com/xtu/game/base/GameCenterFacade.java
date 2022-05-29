package com.xtu.game.base;

import com.xtu.game.base.service.AudioService;
import com.xtu.game.base.service.StorageService;

import java.util.HashMap;
import java.util.Map;

public final class GameCenterFacade {

    private static final GameCenterFacade sInstance = new GameCenterFacade();

    private GameCenterFacade() {
    }

    public static GameCenterFacade getInstance() {
        return sInstance;
    }

    private final Map<String, GameCenterServiceProvider<? extends GameCenterService>> servicePool = new HashMap<>();

    public <T extends GameCenterService> void injectService(Class<T> serviceInf,
                                                            GameCenterServiceProvider<T> provider) {
        this.servicePool.put(serviceInf.getSimpleName(), provider);
    }

    @SuppressWarnings("unchecked")
    private <T extends GameCenterService> T getService(Class<T> service) {
        GameCenterServiceProvider<T> serviceProvider = (GameCenterServiceProvider<T>) this.servicePool.get(service.getSimpleName());
        if (serviceProvider == null) return null;
        return serviceProvider.get();
    }

    public AudioService getAudioService() {
        return getService(AudioService.class);
    }

    public StorageService getStorageService() {
        return getService(StorageService.class);
    }
}

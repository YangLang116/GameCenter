package com.xtu.plugin.game.lifecycle.service;

import com.intellij.ide.util.PropertiesComponent;
import com.xtu.game.base.service.StorageService;

public class StorageServiceImpl implements StorageService {

    private PropertiesComponent getStorage() {
        return PropertiesComponent.getInstance();
    }

    @Override
    public String read(String key) {
        return getStorage().getValue(key);
    }

    @Override
    public void save(String key, String value) {
        PropertiesComponent storage = getStorage();
        storage.setValue(key, value);
    }
}

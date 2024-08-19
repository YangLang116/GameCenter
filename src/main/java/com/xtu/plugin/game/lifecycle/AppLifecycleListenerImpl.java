package com.xtu.plugin.game.lifecycle;

import com.intellij.ide.AppLifecycleListener;
import com.xtu.game.base.GameCenterFacade;
import com.xtu.game.base.GameCenterServiceProvider;
import com.xtu.game.base.service.AudioService;
import com.xtu.game.base.service.StorageService;
import com.xtu.plugin.game.lifecycle.service.AudioServiceImpl;
import com.xtu.plugin.game.lifecycle.service.StorageServiceImpl;
import com.xtu.plugin.game.loader.fc.FCGameLoader;
import com.xtu.plugin.game.loader.swing.SwingGameLoader;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AppLifecycleListenerImpl implements AppLifecycleListener {

    @Override
    public void appFrameCreated(@NotNull List<String> commandLineArgs) {
        GameCenterFacade gameCenterFacade = GameCenterFacade.getInstance();
        gameCenterFacade.injectService(AudioService.class, new GameCenterServiceProvider<>() {
            @Override
            public AudioService create() {
                return new AudioServiceImpl();
            }
        });
        gameCenterFacade.injectService(StorageService.class, new GameCenterServiceProvider<>() {
            @Override
            public StorageService create() {
                return new StorageServiceImpl();
            }
        });
        SwingGameLoader.getInstance().load();
        FCGameLoader.getInstance().load();
    }
}

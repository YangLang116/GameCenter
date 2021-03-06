package com.xtu.plugin.game.lifecycle;

import com.intellij.ide.AppLifecycleListener;
import com.xtu.game.base.GameCenterFacade;
import com.xtu.game.base.GameCenterServiceProvider;
import com.xtu.game.base.service.AudioService;
import com.xtu.game.base.service.StorageService;
import com.xtu.plugin.game.conf.FcGameLoader;
import com.xtu.plugin.game.conf.SwingGameLoader;
import com.xtu.plugin.game.lifecycle.service.AudioServiceImpl;
import com.xtu.plugin.game.lifecycle.service.StorageServiceImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AppLifecycleListenerImpl implements AppLifecycleListener {

    @Override
    public void appFrameCreated(@NotNull List<String> commandLineArgs) {
        //注册服务
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
        FcGameLoader.getInstance().load();
    }
}

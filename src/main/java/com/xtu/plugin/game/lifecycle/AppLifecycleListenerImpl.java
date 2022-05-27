package com.xtu.plugin.game.lifecycle;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.ide.util.RunOnceUtil;
import com.xtu.game.base.GameCenterFacade;
import com.xtu.game.base.GameCenterServiceProvider;
import com.xtu.game.base.service.AudioService;
import com.xtu.game.base.service.StorageService;
import com.xtu.plugin.game.conf.ConfLoader;
import com.xtu.plugin.game.lifecycle.service.AudioServiceImpl;
import com.xtu.plugin.game.lifecycle.service.StorageServiceImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AppLifecycleListenerImpl implements AppLifecycleListener {

    @Override
    public void appFrameCreated(@NotNull List<String> commandLineArgs) {
        RunOnceUtil.runOnceForApp("register GameCenter Service", () -> {
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
            //加载游戏列表
            ConfLoader.getInstance().load();
        });
    }
}

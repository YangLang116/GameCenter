package com.xtu.plugin.game.action.fc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.ui.jcef.JBCefApp;
import com.xtu.plugin.game.manager.GameManager;
import com.xtu.plugin.game.ui.FCGamePlayDialog;
import com.xtu.plugin.game.utils.ToastUtil;
import org.jetbrains.annotations.NotNull;

public final class FCGameOfflineAction extends AnAction {

    public FCGameOfflineAction() {
        super("Offline");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        GameManager.loadOfflineGame(project, new GameManager.OnGameHtmlListener() {
            @Override
            public void onReady(@NotNull String html) {
                if (JBCefApp.isSupported()) {
                    FCGamePlayDialog.play(project, "FC Game", html);
                } else {
                    GameManager.openGameWithBrowser(project, "offline.html", html);
                }
            }

            @Override
            public void onFail(@NotNull String error) {
                ToastUtil.make(project, MessageType.ERROR, error);
            }
        });

    }
}

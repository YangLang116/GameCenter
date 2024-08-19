package com.xtu.plugin.game.action.fc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.ui.jcef.JBCefApp;
import com.xtu.plugin.game.manager.GameManager;
import com.xtu.plugin.game.ui.FCGamePlayDialog;
import org.jetbrains.annotations.NotNull;

public final class FCGameOfflineAction extends AnAction {

    public FCGameOfflineAction() {
        super("Offline");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) return;
        GameManager.getGameOfflineHtml(html -> {
            if (JBCefApp.isSupported()) {
                FCGamePlayDialog.play(project, "FC Game", html);
            } else {
                GameManager.openGameWithBrowser(project, "offline.html", html);
            }
        });

    }
}

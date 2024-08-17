package com.xtu.plugin.game.action.fc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.ui.jcef.JBCefApp;
import com.xtu.plugin.game.ui.FCGamePlayDialog;
import com.xtu.plugin.game.utils.GameUtils;
import org.jetbrains.annotations.NotNull;

public final class FCGameOfflineAction extends AnAction {

    public FCGameOfflineAction() {
        super("Offline");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) return;
        String gameContent = GameUtils.getGameOfflineHtml();
        if (JBCefApp.isSupported()) {
            FCGamePlayDialog.play(project, "FC Game", gameContent);
        } else {
            GameUtils.openGameWithBrowser(project, gameContent, "offline.html");
        }
    }
}

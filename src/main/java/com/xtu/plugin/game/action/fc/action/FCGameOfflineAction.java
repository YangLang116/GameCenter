package com.xtu.plugin.game.action.fc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.xtu.plugin.game.starter.GameStarter;
import org.jetbrains.annotations.NotNull;

public final class FCGameOfflineAction extends AnAction {

    public FCGameOfflineAction() {
        super("Play Offline");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        GameStarter.getInstance().playOfflineFCGame(project);
    }
}

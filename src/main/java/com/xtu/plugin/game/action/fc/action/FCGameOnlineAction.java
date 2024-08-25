package com.xtu.plugin.game.action.fc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.xtu.plugin.game.ui.FCGameListDialog;
import org.jetbrains.annotations.NotNull;

public final class FCGameOnlineAction extends AnAction {

    public FCGameOnlineAction() {
        super("Online");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        FCGameListDialog.show(project);
    }
}

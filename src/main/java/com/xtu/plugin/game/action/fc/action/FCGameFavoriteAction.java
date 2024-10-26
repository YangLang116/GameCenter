package com.xtu.plugin.game.action.fc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.xtu.plugin.game.ui.FCGameFavoriteDialog;
import org.jetbrains.annotations.NotNull;

public class FCGameFavoriteAction extends AnAction {

    public FCGameFavoriteAction() {
        super("Play Favorite");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        FCGameFavoriteDialog.show(project);
    }
}

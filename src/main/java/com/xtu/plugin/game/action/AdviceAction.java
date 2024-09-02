package com.xtu.plugin.game.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.xtu.plugin.game.ui.AdviceDialog;
import org.jetbrains.annotations.NotNull;

public final class AdviceAction extends AnAction {

    public AdviceAction() {
        super("Suggestion");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        AdviceDialog.show(project);
    }
}

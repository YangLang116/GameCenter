package com.xtu.plugin.game.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.xtu.plugin.game.ui.AdviceDialog;
import com.xtu.plugin.game.utils.WindowUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

final class AdviceAction extends AnAction {

    AdviceAction() {
        super("Suggestion");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        JComponent rootPanel = WindowUtils.getVisibleRootPanel();
        AdviceDialog.show(rootPanel);
    }
}

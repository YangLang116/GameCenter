package com.xtu.plugin.game.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.xtu.plugin.game.ui.FCGameListDialog;
import org.jetbrains.annotations.NotNull;

final class FCGameNetAction extends AnAction {

    FCGameNetAction() {
        super("Play FC-Game Online");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        FCGameListDialog.showGameList();
    }
}

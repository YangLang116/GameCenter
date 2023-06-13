package com.xtu.plugin.game.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.xtu.plugin.game.ui.FCGameListDialog;
import org.jetbrains.annotations.NotNull;

public class FCGameAction extends AnAction {

    public FCGameAction() {
        super("FC-Game List");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        FCGameListDialog.showGameList();
    }
}

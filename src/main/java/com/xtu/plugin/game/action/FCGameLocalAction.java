package com.xtu.plugin.game.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.jcef.JBCefApp;
import com.xtu.plugin.game.ui.FCGamePlayDialog;
import com.xtu.plugin.game.utils.GameUtils;
import org.jetbrains.annotations.NotNull;

final class FCGameLocalAction extends AnAction {

    FCGameLocalAction() {
        super("Load FC-Game From Path");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        boolean supportJCEF = JBCefApp.isSupported();
        String gameContent = GameUtils.getGameOfflineHtml();
        if (supportJCEF) {
            FCGamePlayDialog.showDialog("", gameContent);
        } else {
            GameUtils.openGameWithBrowser(gameContent, "offline.html");
        }
    }
}

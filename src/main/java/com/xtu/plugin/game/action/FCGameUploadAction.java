package com.xtu.plugin.game.action;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

final class FCGameUploadAction extends AnAction {

    private static final String REPO = "https://github.com/YangLang116/nes-game-list";

    FCGameUploadAction() {
        super("Upload FC-Game");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        BrowserUtil.open(REPO);
    }
}

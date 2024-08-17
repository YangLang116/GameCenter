package com.xtu.plugin.game.action.fc.action;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public final class FCGameUploadAction extends AnAction {

    private static final String REPO = "https://github.com/YangLang116/nes-game-list";

    public FCGameUploadAction() {
        super("Upload");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        BrowserUtil.open(REPO);
    }
}

package com.xtu.plugin.game.action.fc.action;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.xtu.plugin.game.constant.GameConst;
import org.jetbrains.annotations.NotNull;

public final class FCGameUploadAction extends AnAction {


    public FCGameUploadAction() {
        super("Upload");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        BrowserUtil.open(GameConst.REPO);
    }
}

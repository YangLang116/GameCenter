package com.xtu.plugin.game.action.fc;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.xtu.plugin.game.action.fc.action.FCGameOfflineAction;
import com.xtu.plugin.game.action.fc.action.FCGameOnlineAction;
import com.xtu.plugin.game.action.fc.action.FCGameUploadAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FCGameGroup extends ActionGroup {

    @Override
    public AnAction @NotNull [] getChildren(@Nullable AnActionEvent e) {
        return new AnAction[]{
                new FCGameOnlineAction(),
                new FCGameOfflineAction(),
                new FCGameUploadAction()
        };
    }
}

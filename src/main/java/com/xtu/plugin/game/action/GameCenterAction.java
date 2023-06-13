package com.xtu.plugin.game.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

final class GameCenterAction extends AnAction {

    private final Runnable action;

    GameCenterAction(@NotNull String text, @NotNull Runnable action) {
        super(text);
        this.action = action;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        this.action.run();
    }
}

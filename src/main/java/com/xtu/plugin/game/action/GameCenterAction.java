package com.xtu.plugin.game.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public final class GameCenterAction extends AnAction {

    private final Runnable action;

    public GameCenterAction(@NotNull String text, @NotNull Runnable action) {
        super(text);
        this.action = action;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        this.action.run();
    }
}

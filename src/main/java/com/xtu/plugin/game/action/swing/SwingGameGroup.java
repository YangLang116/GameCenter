package com.xtu.plugin.game.action.swing;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.xtu.plugin.game.action.swing.action.SwingGameAction;
import com.xtu.plugin.game.loader.swing.SwingGame;
import com.xtu.plugin.game.loader.swing.SwingGameLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SwingGameGroup extends ActionGroup {
    @Override
    public AnAction @NotNull [] getChildren(@Nullable AnActionEvent event) {
        final List<AnAction> actionList = new ArrayList<>();
        List<SwingGame> gameList = SwingGameLoader.getInstance().getGameList();
        for (SwingGame swingGame : gameList) {
            SwingGameAction action = new SwingGameAction(swingGame);
            actionList.add(action);
        }
        return actionList.toArray(AnAction[]::new);
    }
}



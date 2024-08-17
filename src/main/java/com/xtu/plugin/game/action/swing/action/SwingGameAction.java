package com.xtu.plugin.game.action.swing.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.xtu.plugin.game.loader.swing.SwingGame;
import com.xtu.plugin.game.utils.ToastUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class SwingGameAction extends AnAction {

    private final SwingGame game;

    public SwingGameAction(@NotNull SwingGame game) {
        super(game.name);
        this.game = game;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        loadGame(project);
    }

    private void loadGame(@NotNull Project project) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Class<?> aClass = classLoader.loadClass(game.entryClass);
            Method runGameMethod = aClass.getDeclaredMethod("runGame");
            runGameMethod.invoke(null);
        } catch (Exception e) {
            ToastUtil.make(project, MessageType.ERROR, e.getMessage());
        }
    }
}

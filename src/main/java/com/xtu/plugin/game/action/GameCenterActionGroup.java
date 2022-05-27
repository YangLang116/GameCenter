package com.xtu.plugin.game.action;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.xtu.plugin.game.conf.ConfLoader;
import com.xtu.plugin.game.utils.ToastUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Map;

public final class GameCenterActionGroup extends ActionGroup {

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            e.getPresentation().setEnabledAndVisible(false);
            return;
        }
        Map<String, String> gameList = ConfLoader.getInstance().getGameList();
        if (gameList.size() <= 0) {
            e.getPresentation().setEnabledAndVisible(false);
            return;
        }
        e.getPresentation().setEnabledAndVisible(true);
    }

    @Override
    public AnAction @NotNull [] getChildren(@Nullable AnActionEvent e) {
        Map<String, String> gameList = ConfLoader.getInstance().getGameList();
        if (gameList.size() <= 0 || e == null) return new AnAction[0];
        return gameList.entrySet().stream().map(entry -> {
            String gameName = entry.getKey();
            String mainClass = entry.getValue();
            return new GameCenterAction(gameName, () -> runGame(e.getProject(), mainClass));
        }).toArray(AnAction[]::new);
    }

    private void runGame(Project project, String mainClass) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Class<?> aClass = classLoader.loadClass(mainClass);
            Method runGameMethod = aClass.getDeclaredMethod("runGame");
            runGameMethod.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.make(project, MessageType.ERROR, e.getMessage());
        }
    }
}

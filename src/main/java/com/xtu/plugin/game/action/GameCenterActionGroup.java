package com.xtu.plugin.game.action;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.ui.MessageType;
import com.xtu.plugin.game.conf.SwingGameLoader;
import com.xtu.plugin.game.utils.ToastUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class GameCenterActionGroup extends ActionGroup {

    @Override
    public AnAction @NotNull [] getChildren(@Nullable AnActionEvent e) {
        if (e == null || e.getProject() == null) return new AnAction[0];
        final List<AnAction> actionList = new ArrayList<>();
        actionList.add(new FCGameAction());
        actionList.add(new Separator());
        Map<String, String> gameList = SwingGameLoader.getInstance().getGameList();
        if (gameList.size() > 0) {
            for (Map.Entry<String, String> swingGame : gameList.entrySet()) {
                String gameName = swingGame.getKey();
                String mainClass = swingGame.getValue();
                actionList.add(new GameCenterAction(gameName, () -> runGame(mainClass)));
            }
        }
        return actionList.toArray(AnAction[]::new);
    }

    private void runGame(String mainClass) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Class<?> aClass = classLoader.loadClass(mainClass);
            Method runGameMethod = aClass.getDeclaredMethod("runGame");
            runGameMethod.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.make(MessageType.ERROR, e.getMessage());
        }
    }
}

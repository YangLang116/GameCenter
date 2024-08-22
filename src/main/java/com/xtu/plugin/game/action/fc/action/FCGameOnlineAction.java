package com.xtu.plugin.game.action.fc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.xtu.plugin.game.loader.fc.FCGameLoader;
import com.xtu.plugin.game.loader.fc.entity.FCGameCategory;
import com.xtu.plugin.game.ui.FCGameListDialog;
import com.xtu.plugin.game.utils.ToastUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class FCGameOnlineAction extends AnAction {

    public FCGameOnlineAction() {
        super("Online");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        List<FCGameCategory> categoryList = FCGameLoader.getInstance().getCategoryList();
        if (categoryList.isEmpty()) {
            ToastUtil.make(project, MessageType.INFO, "Restart idea to load games");
            return;
        }
        FCGameListDialog.show(project, categoryList);
    }
}

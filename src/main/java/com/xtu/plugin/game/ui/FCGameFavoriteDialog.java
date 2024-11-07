package com.xtu.plugin.game.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.xtu.plugin.game.loader.fc.entity.FCGameCategory;
import com.xtu.plugin.game.store.GameStorageService;
import com.xtu.plugin.game.ui.component.FCGameEmptyComponent;
import com.xtu.plugin.game.ui.component.FCGameListComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FCGameFavoriteDialog extends DialogWrapper {

    public static void show(@NotNull Project project) {
        FCGameFavoriteDialog dialog = new FCGameFavoriteDialog(project);
        dialog.show();
    }

    private final Project project;

    private FCGameFavoriteDialog(@NotNull Project project) {
        super(project, null, true, IdeModalityType.IDE, false);
        this.project = project;
        setTitle("");
        setSize(480, 320);
        setResizable(false);
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        FCGameCategory category = GameStorageService.getFavoriteCategory();
        if (category.noGames()) {
            return new FCGameEmptyComponent("No favorites");
        } else {
            return new FCGameListComponent(project, category.games);
        }
    }
}

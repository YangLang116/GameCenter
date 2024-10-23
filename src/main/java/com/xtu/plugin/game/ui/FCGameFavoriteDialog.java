package com.xtu.plugin.game.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.xtu.plugin.game.loader.fc.entity.FCGameCategory;
import com.xtu.plugin.game.store.GameStorageService;
import com.xtu.plugin.game.ui.component.FCGameListComponent;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FCGameFavoriteDialog extends DialogWrapper {

    private final Project project;

    public static void show(@NotNull Project project) {
        FCGameFavoriteDialog dialog = new FCGameFavoriteDialog(project);
        dialog.show();
    }

    private FCGameFavoriteDialog(@NotNull Project project) {
        super(project, null, false, IdeModalityType.IDE, false);
        this.project = project;
        setTitle("Favorite Game");
        setSize(360, 150);
        init();
    }

    @Override
    protected @Nullable @NonNls String getDimensionServiceKey() {
        return FCGameFavoriteDialog.class.getSimpleName();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        FCGameCategory category = GameStorageService.getService().getFavoriteCategory();
        return new FCGameListComponent(project, this, "No favorites", category);
    }
}

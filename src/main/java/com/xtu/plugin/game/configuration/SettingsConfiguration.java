package com.xtu.plugin.game.configuration;

import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.NlsContexts;
import com.xtu.plugin.game.store.GameStorageService;
import com.xtu.plugin.game.utils.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

public class SettingsConfiguration implements SearchableConfigurable {

    private JComponent rootComponent;
    private JTextField repoComponent;

    @Override
    public @NotNull @NonNls String getId() {
        return getClass().getName();
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "GameCenter";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return rootComponent;
    }

    @Override
    public boolean isModified() {
        return !Objects.equals(GameStorageService.getGameRepo(), repoComponent.getText().trim());
    }


    @Override
    public void reset() {
        String repo = GameStorageService.getGameRepo();
        repoComponent.setText(repo);
    }

    @Override
    public void apply() {
        String repo = repoComponent.getText().trim();
        if (StringUtils.isNotEmpty(repo)) {
            GameStorageService.setGameRepo(repo);
        }
    }

}

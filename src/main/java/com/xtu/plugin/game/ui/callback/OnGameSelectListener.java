package com.xtu.plugin.game.ui.callback;

import com.xtu.plugin.game.loader.fc.entity.FCGame;
import org.jetbrains.annotations.NotNull;

public interface OnGameSelectListener {
    void onSelect(@NotNull FCGame game);
}
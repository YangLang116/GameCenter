package com.xtu.plugin.game.ui.render;

import com.xtu.plugin.game.loader.fc.entity.FCGame;
import com.xtu.plugin.game.ui.component.FCGameCellComponent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class FCGameListRender extends FCGameCellComponent implements ListCellRenderer<FCGameListRender.ItemEntity> {


    @Override
    public Component getListCellRendererComponent(JList<? extends ItemEntity> list,
                                                  ItemEntity data,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        setData(data.game.name, data.game.desc, data.cover);
        return this;
    }

    public static class ItemEntity {

        private final FCGame game;
        private final Image cover;

        public ItemEntity(@NotNull FCGame game) {
            this.game = game;
            this.cover = null;
        }

        public ItemEntity(@NotNull FCGame game, @NotNull Image cover) {
            this.game = game;
            this.cover = cover;
        }

        public String getKeyword() {
            return game.name;
        }

        public FCGame getGame() {
            return game;
        }
    }
}

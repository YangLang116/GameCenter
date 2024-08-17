package com.xtu.plugin.game.ui.render;

import com.xtu.plugin.game.loader.fc.FCGame;
import net.coobird.thumbnailator.Thumbnails;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

public class FCGameCellRender extends JPanel implements ListCellRenderer<FCGame> {

    private final JLabel nameComponent;
    private final JLabel iconComponent;

    public FCGameCellRender() {
        setLayout(new BorderLayout());
        this.nameComponent = new JLabel();
        add(nameComponent, BorderLayout.NORTH);
        this.iconComponent = new JLabel();
        add(this.iconComponent, BorderLayout.CENTER);
    }

    private void setGameIcon(@NotNull String url) {
        try {
            URL urls = new URL(url);//获取资源url
            BufferedInputStream in = new BufferedInputStream(urls.openStream());
            BufferedImage thumbnailBI = Thumbnails.of(in).scale(0.9f).outputQuality(0.5).asBufferedImage();
            ImageIcon icon = new ImageIcon(thumbnailBI);
            iconComponent.setIcon(icon);
        } catch (IOException e) {
            //
        }
    }

    private void setGameName(@NotNull String name) {
        this.nameComponent.setText(name);
    }


    @Override
    public Component getListCellRendererComponent(JList<? extends FCGame> list, FCGame game,
                                                  int index, boolean isSelected,
                                                  boolean cellHasFocus) {
        //setGameIcon("https://gitee.com/YangLang116/nes-game-list/raw/config/nes_list/" + game.icon);
        setGameName(game.name);
        return this;
    }
}

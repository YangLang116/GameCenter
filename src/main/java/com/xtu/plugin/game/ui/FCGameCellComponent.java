package com.xtu.plugin.game.ui;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.xtu.plugin.game.loader.fc.entity.FCGame;
import com.xtu.plugin.game.store.GameStorageService;
import com.xtu.plugin.game.ui.callback.OnGameSelectListener;
import icons.PluginIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FCGameCellComponent extends Box {

    private boolean isFavorite;

    public FCGameCellComponent(@NotNull FCGame game,
                               @NotNull OnGameSelectListener clickListener) {
        super(BoxLayout.Y_AXIS);
        MouseAdapter mouseAdapter = getMouseAdapter(game, clickListener);
        addMouseListener(mouseAdapter);
        addHeadComponent(game);
        addDescComponent(game.desc, mouseAdapter);
        add(new JSeparator(JSeparator.HORIZONTAL));
    }

    @NotNull
    private MouseAdapter getMouseAdapter(@NotNull FCGame game, @NotNull OnGameSelectListener clickListener) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickListener.onSelect(game);
            }
        };
    }

    private void addHeadComponent(@NotNull FCGame game) {
        Box container = Box.createHorizontalBox();
        container.setBorder(JBUI.Borders.empty(10, 0));
        container.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        container.add(createFavoriteIcon(game));
        container.add(Box.createHorizontalStrut(5));
        container.add(createNameLabel(game.name));
        add(container);
    }

    private JComponent createFavoriteIcon(@NotNull FCGame game) {
        this.isFavorite = GameStorageService.getService().isFavorite(game);
        JLabel favoriteIcon = new JLabel();
        favoriteIcon.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        favoriteIcon.setSize(new Dimension(16, 16));
        favoriteIcon.setIcon(isFavorite ? PluginIcons.star : PluginIcons.starEmpty);
        favoriteIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isFavorite = !isFavorite;
                favoriteIcon.setIcon(isFavorite ? PluginIcons.star : PluginIcons.starEmpty);
                if (isFavorite) {
                    GameStorageService.getService().addFavoriteGame(game);
                } else {
                    GameStorageService.getService().removeFavoriteGame(game);
                }
            }
        });
        return favoriteIcon;
    }

    private JLabel createNameLabel(@NotNull String name) {
        JLabel nameLabel = new JLabel(String.format("<html><u>%s</u></html>", name));
        nameLabel.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        nameLabel.setFont(new Font(null, Font.BOLD, JBUI.scaleFontSize(18f)));
        nameLabel.setForeground(JBColor.foreground());
        return nameLabel;
    }

    private void addDescComponent(@NotNull String desc, @NotNull MouseAdapter mouseAdapter) {
        JTextArea descLabel = new JTextArea(desc);
        descLabel.setEditable(false);
        descLabel.setLineWrap(true);
        descLabel.setWrapStyleWord(true);
        descLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        descLabel.setBorder(JBUI.Borders.emptyBottom(10));
        descLabel.setForeground(JBColor.foreground().darker());
        descLabel.setBackground(new Color(0, 0, 0, 0));
        descLabel.setFont(new Font(null, Font.PLAIN, JBUI.scaleFontSize(13f)));
        descLabel.addMouseListener(mouseAdapter);
        add(descLabel);
    }
}

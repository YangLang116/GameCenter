package com.xtu.plugin.game.ui;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.xtu.plugin.game.downloader.FileDownloader;
import com.xtu.plugin.game.loader.fc.entity.FCGame;
import com.xtu.plugin.game.ui.callback.OnGameSelectListener;
import com.xtu.plugin.game.ui.component.FCGameCoverComponent;
import icons.PluginIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FCGameCellComponent extends JPanel {

    private boolean isFavorite;

    public FCGameCellComponent(@NotNull FileDownloader coverDownloader,
                               @NotNull FCGame game,
                               @NotNull OnGameSelectListener clickListener) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(-1, 160));
        add(createGameBox(coverDownloader, game), BorderLayout.CENTER);
        add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.SOUTH);
    }

    private JComponent createGameBox(@NotNull FileDownloader coverDownloader,
                                     @NotNull FCGame game) {
        JPanel rootContainer = new JPanel(new BorderLayout());
        rootContainer.setBorder(JBUI.Borders.empty(10, 5));

        JComponent coverComponent = new FCGameCoverComponent(160, 160, game.icon, coverDownloader);
//        coverComponent.setAlignmentX(JComponent.LEFT_ALIGNMENT);
//        coverComponent.setBorder(JBUI.Borders.emptyBottom(20));
        rootContainer.add(coverComponent, BorderLayout.WEST);

        JPanel infoContainer = new JPanel(new BorderLayout());
        infoContainer.setBorder(JBUI.Borders.emptyLeft(10));
//        infoContainer.setAlignmentX(JComponent.LEFT_ALIGNMENT);
//        infoContainer.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        rootContainer.add(infoContainer, BorderLayout.CENTER);

        JLabel nameLabel = new JLabel(String.format("<html><u>%s</u></html>", game.name));
//        nameLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        nameLabel.setFont(new Font(null, Font.BOLD, JBUI.scaleFontSize(18f)));
        nameLabel.setForeground(JBColor.foreground());
        infoContainer.add(nameLabel, BorderLayout.NORTH);

        JTextArea descLabel = new JTextArea(game.desc);
        descLabel.setEditable(false);
        descLabel.setLineWrap(true);
        descLabel.setWrapStyleWord(true);
        descLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        descLabel.setBorder(JBUI.Borders.empty(10, 0));
        descLabel.setForeground(JBColor.foreground().darker());
        descLabel.setBackground(new Color(0, 0, 0, 0));
        descLabel.setFont(new Font(null, Font.PLAIN, JBUI.scaleFontSize(13f)));
        infoContainer.add(descLabel, BorderLayout.CENTER);

        Box buttonContainer = Box.createHorizontalBox();
        buttonContainer.setBorder(JBUI.Borders.emptyTop(10));
        infoContainer.add(buttonContainer, BorderLayout.SOUTH);

        JButton starBtn = new JButton("Star");
        starBtn.setIcon(PluginIcons.STAR);
        buttonContainer.add(starBtn);

        buttonContainer.add(Box.createHorizontalStrut(10));

        JButton playBtn = new JButton("Play");
        playBtn.setIcon(PluginIcons.PLAY);
        buttonContainer.add(playBtn);

        return rootContainer;
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

//    private void addHeadComponent(@NotNull FCGame game) {
//        Box container = Box.createHorizontalBox();
//        container.setBorder(JBUI.Borders.empty(10, 0));
//        container.setAlignmentX(JComponent.LEFT_ALIGNMENT);
//        container.add(createFavoriteIcon(game));
//        container.add(Box.createHorizontalStrut(5));
//        container.add(createNameLabel(game.name));
//        add(container);
//    }

//    private JComponent createFavoriteIcon(@NotNull FCGame game) {
//        this.isFavorite = GameStorageService.getService().isFavorite(game);
//        JLabel favoriteIcon = new JLabel();
//        favoriteIcon.setAlignmentY(JComponent.CENTER_ALIGNMENT);
//        favoriteIcon.setSize(new Dimension(16, 16));
//        favoriteIcon.setIcon(isFavorite ? PluginIcons.star : PluginIcons.starEmpty);
//        favoriteIcon.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                isFavorite = !isFavorite;
//                favoriteIcon.setIcon(isFavorite ? PluginIcons.star : PluginIcons.starEmpty);
//                if (isFavorite) {
//                    GameStorageService.getService().addFavoriteGame(game);
//                } else {
//                    GameStorageService.getService().removeFavoriteGame(game);
//                }
//            }
//        });
//        return favoriteIcon;
//    }
}

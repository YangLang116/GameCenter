package com.xtu.plugin.game.ui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.xtu.plugin.game.downloader.FileDownloader;
import com.xtu.plugin.game.loader.fc.entity.FCGame;
import com.xtu.plugin.game.reporter.GameReporter;
import com.xtu.plugin.game.store.GameStorageService;
import com.xtu.plugin.game.ui.callback.OnGameSelectListener;
import com.xtu.plugin.game.ui.component.FCGameCoverComponent;
import icons.PluginIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class FCGameCellComponent extends JPanel {

    private boolean isFavorite;

    public FCGameCellComponent(@NotNull Project project,
                               @NotNull FileDownloader coverDownloader,
                               @NotNull FCGame game,
                               @NotNull OnGameSelectListener clickListener) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(-1, 160));
        add(createGameBox(project, game, coverDownloader, clickListener), BorderLayout.CENTER);
        add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.SOUTH);
    }

    private JComponent createGameBox(@NotNull Project project,
                                     @NotNull FCGame game,
                                     @NotNull FileDownloader coverDownloader,
                                     @NotNull OnGameSelectListener clickListener) {
        JPanel rootContainer = new JPanel(new BorderLayout());
        rootContainer.setBorder(JBUI.Borders.empty(10, 0));

        JComponent coverComponent = new FCGameCoverComponent(140, 140, game.icon, coverDownloader);
        rootContainer.add(coverComponent, BorderLayout.WEST);

        JPanel infoContainer = new JPanel(new BorderLayout());
        infoContainer.setBorder(JBUI.Borders.empty(5, 10, 5, 0));
        rootContainer.add(infoContainer, BorderLayout.CENTER);

        JLabel nameLabel = new JLabel(String.format("<html><u>%s</u></html>", game.name));
        nameLabel.setToolTipText(game.name);
        nameLabel.setFont(new Font(null, Font.BOLD, 18));
        nameLabel.setForeground(JBColor.foreground());
        infoContainer.add(nameLabel, BorderLayout.NORTH);

        JTextArea descLabel = new JTextArea(game.desc);
        descLabel.setEditable(false);
        descLabel.setLineWrap(true);
        descLabel.setWrapStyleWord(true);
        descLabel.setToolTipText(game.desc);
        descLabel.setFont(new Font(null, Font.PLAIN, 13));
        descLabel.setForeground(JBColor.foreground().darker());
        descLabel.setBackground(new Color(0, 0, 0, 0));
        descLabel.setBorder(JBUI.Borders.empty(10, 0));
        infoContainer.add(descLabel, BorderLayout.CENTER);

        Box buttonContainer = Box.createHorizontalBox();
        buttonContainer.setBorder(JBUI.Borders.emptyTop(10));
        infoContainer.add(buttonContainer, BorderLayout.SOUTH);

        JButton starBtn = new JButton("Star");
        this.isFavorite = GameStorageService.isFavorite(game);
        starBtn.setIcon(this.isFavorite ? PluginIcons.STAR : PluginIcons.STAR_EMPTY);
        starBtn.addActionListener(e -> {
            if (this.isFavorite) {
                GameStorageService.removeFavoriteGame(game);
                starBtn.setIcon(PluginIcons.STAR_EMPTY);
                this.isFavorite = false;
            } else {
                GameStorageService.addFavoriteGame(game);
                starBtn.setIcon(PluginIcons.STAR);
                this.isFavorite = true;
            }
        });
        buttonContainer.add(starBtn);
        buttonContainer.add(Box.createHorizontalStrut(10));
        JButton playBtn = new JButton("Play", PluginIcons.PLAY);
        playBtn.addActionListener(e -> clickListener.onSelect(game));
        buttonContainer.add(playBtn);
        buttonContainer.add(Box.createHorizontalStrut(10));
        JButton runErrorBtn = new JButton("Run Error", PluginIcons.RUN_ERROR);
        runErrorBtn.addActionListener(e -> GameReporter.getInstance().submitAdvice(project, "Game Run Error", game.name));
        buttonContainer.add(runErrorBtn);

        return rootContainer;
    }
}

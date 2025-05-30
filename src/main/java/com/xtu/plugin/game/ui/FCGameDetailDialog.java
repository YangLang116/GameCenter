package com.xtu.plugin.game.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.xtu.plugin.game.downloader.FileDownloader;
import com.xtu.plugin.game.loader.fc.entity.FCGame;
import com.xtu.plugin.game.reporter.GameReporter;
import com.xtu.plugin.game.res.GameResManager;
import com.xtu.plugin.game.starter.GameStarter;
import com.xtu.plugin.game.store.GameStorageService;
import icons.PluginIcons;
import net.coobird.thumbnailator.Thumbnails;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class FCGameDetailDialog extends DialogWrapper {

    public static void show(@NotNull Project project,
                            @NotNull FCGame game,
                            @NotNull FileDownloader downloader) {
        FCGameDetailDialog dialog = new FCGameDetailDialog(project, game, downloader);
        dialog.show();
    }

    private final Project project;
    private final FCGame game;
    private final FileDownloader downloader;

    private static final int SIZE_COVER = 150;

    private FCGameDetailDialog(@NotNull Project project,
                               @NotNull FCGame game,
                               @NotNull FileDownloader downloader) {
        super(project, null, false, IdeModalityType.IDE);
        this.project = project;
        this.game = game;
        this.downloader = downloader;
        setTitle("");
        setSize(480, 280);
        setResizable(false);
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBorder(JBUI.Borders.empty(10));
        rootPanel.add(createCover(), BorderLayout.WEST);
        rootPanel.add(createInfo(), BorderLayout.CENTER);
        return rootPanel;
    }

    private JComponent createCover() {
        Box box = Box.createVerticalBox();
        JLabel coverView = new JLabel();
        coverView.setPreferredSize(new Dimension(SIZE_COVER, SIZE_COVER));
        loadCover(coverView);
        box.add(coverView);
        box.add(Box.createVerticalGlue());
        return box;
    }

    private void loadCover(@NotNull JLabel coverView) {
        String url = GameResManager.getInstance().getResUrl(game.icon);
        downloader.downloadFileAsync(url, true)
                .thenApplyAsync(path -> path == null ? null : loadThumbnail(path))
                .thenAccept(image -> coverView.setIcon(image != null ? new ImageIcon(image) : PluginIcons.GAME));
    }

    @Nullable
    private Image loadThumbnail(@NotNull String path) {
        try {
            File cacheFile = new File(path);
            return Thumbnails.of(cacheFile)
                    .forceSize(SIZE_COVER, SIZE_COVER)
                    .imageType(BufferedImage.TYPE_INT_ARGB)
                    .asBufferedImage();
        } catch (Exception e) {
            return null;
        }
    }

    private JComponent createInfo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(JBUI.Borders.emptyLeft(10));

        JLabel titleLabel = new JLabel(String.format("<html><u>%s</u></html>", game.name));
        titleLabel.setFont(new Font(null, Font.BOLD, 18));
        titleLabel.setForeground(JBColor.foreground());
        panel.add(titleLabel, BorderLayout.NORTH);

        JTextArea descLabel = new JTextArea(game.desc);
        descLabel.setEditable(false);
        descLabel.setFocusable(false);
        descLabel.setLineWrap(true);
        descLabel.setWrapStyleWord(true);
        descLabel.setFont(new Font(null, Font.PLAIN, 13));
        descLabel.setForeground(JBColor.foreground().darker());
        descLabel.setBackground(JBColor.background());

        JScrollPane scrollPane = new JScrollPane(descLabel);
        scrollPane.setBorder(JBUI.Borders.empty(5, 0));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    @Override
    protected Action @NotNull [] createActions() {
        return new Action[]{new FavoriteAction(), new RunAction(), new ReportAction()};
    }

    class FavoriteAction extends DialogWrapper.DialogWrapperAction {

        private boolean isFavorite;

        protected FavoriteAction() {
            super("");
            isFavorite = GameStorageService.isFavorite(game);
            refreshUI();
        }

        @Override
        protected void doAction(ActionEvent actionEvent) {
            close(DialogWrapper.OK_EXIT_CODE);
            if (isFavorite) {
                GameStorageService.removeFavoriteGame(game);
                isFavorite = false;
            } else {
                GameStorageService.addFavoriteGame(game);
                isFavorite = true;
            }
            refreshUI();
        }

        private void refreshUI() {
            putValue(Action.NAME, isFavorite ? "Starred" : "Unstar");
            putValue(Action.SMALL_ICON, isFavorite ? PluginIcons.STAR : PluginIcons.STAR_EMPTY);
        }
    }

    class RunAction extends DialogWrapper.DialogWrapperAction {


        protected RunAction() {
            super("Play");
            putValue(Action.SMALL_ICON, PluginIcons.PLAY);
        }

        @Override
        protected void doAction(ActionEvent actionEvent) {
            close(DialogWrapper.OK_EXIT_CODE);
            GameStarter.getInstance().playOnlineFCGame(project, game);
        }
    }

    class ReportAction extends DialogWrapper.DialogWrapperAction {

        protected ReportAction() {
            super("Report Error");
            putValue(Action.SMALL_ICON, PluginIcons.RUN_ERROR);
        }

        @Override
        protected void doAction(ActionEvent actionEvent) {
            close(DialogWrapper.OK_EXIT_CODE);
            GameReporter.getInstance().submitAdvice(project, "Game Run Error", game.name);
        }
    }
}

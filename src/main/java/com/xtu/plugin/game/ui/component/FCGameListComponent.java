package com.xtu.plugin.game.ui.component;

import com.intellij.openapi.project.Project;
import com.intellij.ui.ListSpeedSearch;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.xtu.plugin.game.downloader.FileDownloader;
import com.xtu.plugin.game.loader.fc.entity.FCGame;
import com.xtu.plugin.game.res.GameResManager;
import com.xtu.plugin.game.ui.FCGameDetailDialog;
import com.xtu.plugin.game.ui.render.FCGameListRender;
import net.coobird.thumbnailator.Thumbnails;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FCGameListComponent extends JPanel {

    private final FileDownloader coverDownloader = new FileDownloader();

    public FCGameListComponent(@NotNull Project project, @NotNull List<FCGame> games) {
        setLayout(new BorderLayout());
        JBList<FCGameListRender.ItemEntity> listView = new JBList<>();
        listView.setCellRenderer(new FCGameListRender());
        JBScrollPane scrollView = new JBScrollPane(listView);
        add(scrollView, BorderLayout.CENTER);

        supportSearch(listView);
        setData(listView, games);
        bindListener(project, listView);
    }

    private void supportSearch(@NotNull JBList<FCGameListRender.ItemEntity> listView) {
        ListSpeedSearch.installOn(listView, FCGameListRender.ItemEntity::getKeyword);
    }

    private void setData(@NotNull JBList<FCGameListRender.ItemEntity> listView, @NotNull List<FCGame> games) {
        DefaultListModel<FCGameListRender.ItemEntity> listModel = new DefaultListModel<>();
        for (int i = 0; i < games.size(); i++) {
            FCGame game = games.get(i);
            listModel.addElement(new FCGameListRender.ItemEntity(game));
            int finalI = i;
            downloadCover(coverDownloader, game.icon).thenAccept(image -> {
                if (image != null) {
                    listModel.set(finalI, new FCGameListRender.ItemEntity(game, image));
                }
            });
        }
        listView.setModel(listModel);
    }

    private CompletableFuture<Image> downloadCover(@NotNull FileDownloader downloader, @NotNull String resPath) {
        String url = GameResManager.getInstance().getResUrl(resPath);
        return downloader.downloadFileAsync(url, true)
                .thenApplyAsync(path -> path == null ? null : loadThumbnail(path));
    }

    @Nullable
    private Image loadThumbnail(@NotNull String path) {
        try {
            File cacheFile = new File(path);
            return Thumbnails.of(cacheFile)
                    .forceSize(100, 100)
                    .imageType(BufferedImage.TYPE_INT_ARGB)
                    .asBufferedImage();
        } catch (Exception e) {
            return null;
        }
    }

    private void bindListener(@NotNull Project project,
                              @NotNull JBList<FCGameListRender.ItemEntity> listView) {
        listView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = listView.locationToIndex(e.getPoint());
                FCGameListRender.ItemEntity itemEntity = listView.getModel().getElementAt(index);
                FCGame game = itemEntity.getGame();
                FCGameDetailDialog.show(project, game, coverDownloader);
            }
        });
    }
}

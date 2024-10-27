package com.xtu.plugin.game.ui.component;

import com.xtu.plugin.game.downloader.FileDownloader;
import com.xtu.plugin.game.res.GameResManager;
import icons.PluginIcons;
import net.coobird.thumbnailator.Thumbnails;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.CompletableFuture;

public class FCGameCoverComponent extends JLabel {

    public FCGameCoverComponent(int width, int height,
                                @NotNull String name,
                                @NotNull FileDownloader downloader) {
        setSize(new Dimension(width, height));
        setIcon(PluginIcons.GAME);
        downloadThumbnail(downloader, name).thenAccept(path -> {
            if (path == null) return;
            loadThumbnail(path, width, height);
        });
    }

    private CompletableFuture<String> downloadThumbnail(@NotNull FileDownloader downloader, @NotNull String name) {
        String url = GameResManager.getInstance().getResUrl(name);
        return downloader.downloadFileAsync(url, true);
    }

    private void loadThumbnail(@NotNull String path, int width, int height) {
        try {
            File cacheFile = new File(path);
            BufferedImage image = Thumbnails.of(cacheFile)
                    .size(width, height)
                    .imageType(BufferedImage.TYPE_INT_ARGB)
                    .asBufferedImage();
            refreshThumbnail(image);
        } catch (Exception e) {
            //ignore
        }
    }

    private void refreshThumbnail(@Nullable BufferedImage image) {
        if (image == null) return;
        setIcon(new ImageIcon(image));
    }
}

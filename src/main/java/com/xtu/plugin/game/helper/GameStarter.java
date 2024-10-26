package com.xtu.plugin.game.helper;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.ui.jcef.JBCefApp;
import com.xtu.plugin.game.downloader.FileDownloader;
import com.xtu.plugin.game.loader.fc.entity.FCGame;
import com.xtu.plugin.game.res.GameResManager;
import com.xtu.plugin.game.ui.FCGamePlayDialog;
import com.xtu.plugin.game.utils.FileUtils;
import com.xtu.plugin.game.utils.LogUtils;
import com.xtu.plugin.game.utils.StreamUtils;
import com.xtu.plugin.game.utils.ToastUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;

public class GameStarter {

    private GameStarter() {
    }

    private static final GameStarter sInstance = new GameStarter();

    public static GameStarter getInstance() {
        return sInstance;
    }

    private final FileDownloader gameDownloader = new FileDownloader();

    public void playOnlineFCGame(@NotNull Project project, @NotNull FCGame game) {
        loadOnlineGame(project, game, new GameStarter.OnGameHtmlListener() {
            @Override
            public void onReady(@NotNull String html) {
                if (JBCefApp.isSupported()) {
                    FCGamePlayDialog.play(project, game.name, html);
                } else {
                    openGameWithBrowser(project, "online.html", html);
                }
            }

            @Override
            public void onFail(@NotNull String error) {
                ToastUtil.make(project, MessageType.ERROR, error);
            }
        });
    }

    public void playOfflineFCGame(@NotNull Project project) {
        loadOfflineGame(project, new GameStarter.OnGameHtmlListener() {
            @Override
            public void onReady(@NotNull String html) {
                if (JBCefApp.isSupported()) {
                    FCGamePlayDialog.play(project, "FC Game", html);
                } else {
                    openGameWithBrowser(project, "offline.html", html);
                }
            }

            @Override
            public void onFail(@NotNull String error) {
                ToastUtil.make(project, MessageType.ERROR, error);
            }
        });
    }

    private void openGameWithBrowser(@NotNull Project project,
                                     @NotNull String fileName,
                                     @NotNull String gameContent) {
        try {
            String tempPath = PathManager.getTempPath() + "/" + fileName;
            Files.writeString(Paths.get(tempPath), gameContent);
            Application application = ApplicationManager.getApplication();
            application.invokeLater(() -> BrowserUtil.browse(new File(tempPath)));
        } catch (Exception e) {
            LogUtils.error("GameManager", e);
            ToastUtil.make(project, MessageType.ERROR, e.getMessage());
        }
    }

    private void loadOfflineGame(@NotNull Project project,
                                 @NotNull GameStarter.OnGameHtmlListener onGameHtmlReady) {

        loadGame(project, () -> {
            String template = StreamUtils.readTextFromResource("/game/fc/html/offline.html");
            assert template != null;
            String gameHtml = template
                    .replace("{htmlCss}", getCssStyle())
                    .replace("{libScript}", getScriptContent());
            onGameHtmlReady.onReady(gameHtml);
        });
    }

    private void loadOnlineGame(@NotNull Project project,
                                @NotNull FCGame game,
                                @NotNull GameStarter.OnGameHtmlListener onGameHtmlReady) {
        loadGame(project, () -> {
            String nesUrl = GameResManager.getInstance().getResUrl(game.url);
            String downloadPath = gameDownloader.downloadFile(nesUrl);
            if (downloadPath == null) {
                onGameHtmlReady.onFail("download game fail");
                return;
            }
            byte[] gameBytes = FileUtils.readAsBytes(downloadPath);
            if (gameBytes == null) {
                onGameHtmlReady.onFail("load game fail");
            } else {
                String template = StreamUtils.readTextFromResource("/game/fc/html/online.html");
                assert template != null;
                String gameHtml = template
                        .replace("{title}", game.name)
                        .replace("{htmlCss}", getCssStyle())
                        .replace("{libScript}", getScriptContent())
                        .replace("{gameContent}", Base64.getEncoder().encodeToString(gameBytes));
                onGameHtmlReady.onReady(gameHtml);
            }
        });
    }

    private void loadGame(@NotNull Project project, @NotNull Runnable task) {
        new Task.Backgroundable(project, "Loading...") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(true);
                task.run();
            }
        }.queue();
    }

    @NotNull
    private String getCssStyle() {
        return Objects.requireNonNull(StreamUtils.readTextFromResource("/game/fc/html/js_nes.css"));
    }

    @NotNull
    private String getScriptContent() {
        return StreamUtils.readTextFromResource("/game/fc/html/libs/jquery-1.4.2.min.js") + "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/nes.js") + "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/utils.js") + "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/cpu.js") + "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/keyboard.js") + "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/mappers.js") + "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/papu.js") + "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/ppu.js") + "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/rom.js") + "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/ui.js");
    }

    public interface OnGameHtmlListener {

        void onReady(@NotNull String html);

        void onFail(@NotNull String error);
    }
}

package com.xtu.plugin.game.manager;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.util.io.FileUtil;
import com.xtu.plugin.game.constant.GameConst;
import com.xtu.plugin.game.loader.fc.entity.FCGame;
import com.xtu.plugin.game.utils.LogUtils;
import com.xtu.plugin.game.utils.StreamUtils;
import com.xtu.plugin.game.utils.ToastUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Base64;
import java.util.Objects;

public class GameManager {

    public static void openGameWithBrowser(@NotNull Project project,
                                           @NotNull String fileName,
                                           @NotNull String gameContent) {
        Application application = ApplicationManager.getApplication();
        application.executeOnPooledThread(() -> {
            try {
                final File gameFile = new File(PathManager.getTempPath(), fileName);
                FileUtil.writeToFile(gameFile, gameContent);
                application.invokeLater(() -> BrowserUtil.browse(gameFile));
            } catch (Exception e) {
                LogUtils.error("GameManager", e);
                ToastUtil.make(project, MessageType.ERROR, e.getMessage());
            }
        });
    }

    public static void loadOfflineGame(@NotNull Project project,
                                       @NotNull GameManager.OnGameHtmlListener onGameHtmlReady) {

        loadGame(project, () -> {
            Application application = ApplicationManager.getApplication();
            String template = StreamUtils.readTextFromResource("/game/fc/html/offline.html");
            assert template != null;
            String gameHtml = template
                    .replace("{htmlCss}", getCssStyle())
                    .replace("{libScript}", getScriptContent());
            application.invokeLater(() -> onGameHtmlReady.onReady(gameHtml));
        });
    }

    public static void loadOnlineGame(@NotNull Project project,
                                      @NotNull FCGame game,
                                      @NotNull GameManager.OnGameHtmlListener onGameHtmlReady) {
        loadGame(project, () -> {
            Application application = ApplicationManager.getApplication();
            byte[] gameBytes = StreamUtils.readDataFromUrl(GameConst.PREFIX_RES + game.url);
            if (gameBytes == null) {
                application.invokeLater(() -> onGameHtmlReady.onFail("load game fail"));
            } else {
                String template = StreamUtils.readTextFromResource("/game/fc/html/online.html");
                assert template != null;
                String gameHtml = template
                        .replace("{title}", game.name)
                        .replace("{htmlCss}", getCssStyle())
                        .replace("{libScript}", getScriptContent())
                        .replace("{gameContent}", Base64.getEncoder().encodeToString(gameBytes));
                application.invokeLater(() -> onGameHtmlReady.onReady(gameHtml));
            }
        });
    }

    private static void loadGame(@NotNull Project project, @NotNull Runnable task) {
        new Task.Backgroundable(project, "Loading...") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(true);
                task.run();
                indicator.setIndeterminate(false);
                indicator.setFraction(1);
            }
        }.queue();
    }

    @NotNull
    private static String getCssStyle() {
        return Objects.requireNonNull(StreamUtils.readTextFromResource("/game/fc/html/js_nes.css"));
    }

    @NotNull
    private static String getScriptContent() {
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

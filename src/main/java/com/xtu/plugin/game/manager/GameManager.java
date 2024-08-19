package com.xtu.plugin.game.manager;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.util.io.FileUtil;
import com.xtu.plugin.game.constant.GameConst;
import com.xtu.plugin.game.loader.fc.FCGame;
import com.xtu.plugin.game.utils.LogUtils;
import com.xtu.plugin.game.utils.StreamUtils;
import com.xtu.plugin.game.utils.ToastUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public class GameManager {

    public static void openGameWithBrowser(@NotNull Project project,
                                           @NotNull String fileName,
                                           @NotNull String gameContent) {
        Application application = ApplicationManager.getApplication();
        application.executeOnPooledThread(() -> {
            final String gameTempDir = PathManager.getTempPath();
            final File gameFile = new File(gameTempDir, fileName);
            try {
                FileUtil.writeToFile(gameFile, gameContent);
                application.invokeLater(() -> BrowserUtil.browse(gameFile));
            } catch (Exception e) {
                LogUtils.error("GameManager", e);
                ToastUtil.make(project, MessageType.ERROR, e.getMessage());
            }
        });
    }

    public static void getGameOfflineHtml(@NotNull GameManager.OnGameHtmlListener onGameHtmlReady) {
        Application application = ApplicationManager.getApplication();
        application.executeOnPooledThread(() -> {
            String template = StreamUtils.readTextFromResource("game/fc/html/offline.html");
            assert template != null;
            String gameHtml = template
                    .replace("{htmlCss}", getCssStyle())
                    .replace("{libScript}", getScriptContent());
            application.invokeLater(() -> onGameHtmlReady.onReady(gameHtml));
        });
    }

    public static void getGameOnlineHtml(@NotNull FCGame game,
                                         @NotNull GameManager.OnGameHtmlListener onGameHtmlReady) {
        Application application = ApplicationManager.getApplication();
        application.executeOnPooledThread(() -> {
            String template = StreamUtils.readTextFromResource("game/fc/html/online.html");
            assert template != null;
            String gameUrl = GameConst.PREFIX_RES + game.url;
            String gameContent = template
                    .replace("{title}", game.name)
                    .replace("{htmlCss}", getCssStyle())
                    .replace("{libScript}", getScriptContent())
                    .replace("{gameUrl}", gameUrl);
            application.invokeLater(() -> onGameHtmlReady.onReady(gameContent));
        });
    }

    @NotNull
    private static String getCssStyle() {
        return Objects.requireNonNull(StreamUtils.readTextFromResource("game/fc/html/js_nes.css"));
    }

    @NotNull
    private static String getScriptContent() {
        return StreamUtils.readTextFromResource("game/fc/html/libs/jquery-1.4.2.min.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/libs/nes.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/libs/utils.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/libs/cpu.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/libs/keyboard.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/libs/mappers.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/libs/papu.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/libs/ppu.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/libs/rom.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/ui.js");
    }

    public interface OnGameHtmlListener {
        void onReady(@NotNull String html);
    }
}

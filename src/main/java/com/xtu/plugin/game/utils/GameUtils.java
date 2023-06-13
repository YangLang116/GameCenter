package com.xtu.plugin.game.utils;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public class GameUtils {

    public static String getGameOnlineHtml(String title, String gamePath) {
        String htmlContent = StreamUtils.readTextFromResource("game/fc/html/online.html");
        assert htmlContent != null;
        htmlContent = htmlContent.replace("{title}", title);
        htmlContent = htmlContent.replace("{htmlCss}", getCssStyle());
        htmlContent = htmlContent.replace("{libScript}", getScriptContent());
        htmlContent = htmlContent.replace("{GameFile}", gamePath);
        return htmlContent;
    }

    public static String getGameOfflineHtml() {
        String htmlContent = StreamUtils.readTextFromResource("game/fc/html/offline.html");
        assert htmlContent != null;
        htmlContent = htmlContent.replace("{htmlCss}", getCssStyle());
        htmlContent = htmlContent.replace("{libScript}", getScriptContent());
        return htmlContent;
    }

    public static void openGameWithBrowser(String gameContent, String fileName) {
        final String gameTempDir = PathManager.getTempPath();
        final File gameFile = new File(gameTempDir, fileName);
        try {
            FileUtil.writeToFile(gameFile, gameContent);
            BrowserUtil.browse(gameFile);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.make(MessageType.ERROR, e.getMessage());
        }
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
}

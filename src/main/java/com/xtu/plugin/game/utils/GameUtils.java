package com.xtu.plugin.game.utils;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.util.io.FileUtil;

import java.io.File;

public class GameUtils {

    public static String generateGame(String title, String gamePath) {
        String htmlContent = StreamUtils.readTextFromResource("game/fc/html/index.html");
        assert htmlContent != null;
        htmlContent = htmlContent.replace("{title}", title);
        //css
        String cssStyle = StreamUtils.readTextFromResource("game/fc/html/js_nes.css");
        assert cssStyle != null;
        htmlContent = htmlContent.replace("{htmlCss}", cssStyle);
        //script
        String scriptContent = StreamUtils.readTextFromResource("game/fc/html/libs/jquery-1.4.2.min.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/libs/nes.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/libs/utils.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/libs/cpu.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/libs/keyboard.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/libs/mappers.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/libs/papu.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/libs/ppu.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/libs/rom.js") + "\n" +
                StreamUtils.readTextFromResource("game/fc/html/ui.js");
        htmlContent = htmlContent.replace("{libScript}", scriptContent);
        //game Data
        htmlContent = htmlContent.replace("{GameFile}", gamePath);
        return htmlContent;
    }

    public static void openGameWithBrowser(String gameContent) {
        final String gameTempDir = PathManager.getTempPath();
        final File gameFile = new File(gameTempDir, "game.html");
        Application application = ApplicationManager.getApplication();
        application.executeOnPooledThread(() -> {
            try {
                FileUtil.writeToFile(gameFile, gameContent);
                BrowserUtil.browse(gameFile);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.make(MessageType.ERROR, e.getMessage());
            }
        });
    }
}

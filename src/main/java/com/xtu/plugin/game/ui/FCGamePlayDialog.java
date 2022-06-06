package com.xtu.plugin.game.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.JBColor;
import com.intellij.ui.javafx.JavaFxHtmlPanel;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.jcef.JBCefBrowser;
import com.xtu.plugin.game.conf.FcGameLoader;
import com.xtu.plugin.game.utils.StreamUtils;
import javafx.scene.web.WebEngine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FCGamePlayDialog extends DialogWrapper {

    public static void showDialog(@NotNull Project project, FcGameLoader.Game game) {
        new FCGamePlayDialog(project, game).show();
    }

    private final FcGameLoader.Game game;

    private FCGamePlayDialog(@NotNull Project project, FcGameLoader.Game game) {
        super(project, null, false, IdeModalityType.IDE, false);
        this.game = game;
        setTitle(game.name);
        setSize(512, 480);
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        boolean supportJCEF = JBCefApp.isSupported();
        if (supportJCEF) {
            JBCefBrowser jcefBrowser = new JBCefBrowser();
            jcefBrowser.getComponent().setBackground(JBColor.BLACK);
            jcefBrowser.loadHTML(getGameContent());
            return jcefBrowser.getComponent();
        } else {
            MyFxHtmlPanel htmlPanel = new MyFxHtmlPanel();
            htmlPanel.setHtml(getGameContent());
            htmlPanel.setBackground(JBColor.BLACK);
            return htmlPanel.getComponent();
        }
    }

    private String getGameContent() {
        String htmlContent = StreamUtils.readTextFromResource("/game/fc/html/index.html");
        //css
        String cssStyle = StreamUtils.readTextFromResource("/game/fc/html/js_nes.css");
        htmlContent = htmlContent.replace("{htmlCss}", cssStyle);
        //script
        String scriptContent = StreamUtils.readTextFromResource("/game/fc/html/libs/jquery-1.4.2.min.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/nes.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/utils.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/cpu.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/keyboard.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/mappers.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/papu.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/ppu.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/rom.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/ui.js");
        htmlContent = htmlContent.replace("{libScript}", scriptContent);
        //game Data
        htmlContent = htmlContent.replace("{GameFile}", game.path);
        return htmlContent;
    }

    private static class MyFxHtmlPanel extends JavaFxHtmlPanel {

        @Override
        protected void registerListeners(@NotNull WebEngine engine) {
            if (myWebView != null) {
                myWebView.setZoom(0.9);
            }
        }
    }
}


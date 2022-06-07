package com.xtu.plugin.game.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.JBColor;
import com.intellij.ui.jcef.JBCefBrowser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FCGamePlayDialog extends DialogWrapper {

    public static void showDialog(@NotNull Project project, String title, String htmlContent) {
        new FCGamePlayDialog(project, title, htmlContent).show();
    }

    private final String htmlContent;

    private FCGamePlayDialog(@NotNull Project project, String title, String htmlContent) {
        super(project, null, false, IdeModalityType.IDE, false);
        this.htmlContent = htmlContent;
        setTitle(title);
        setSize(512, 480);
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JBCefBrowser jcefBrowser = new JBCefBrowser();
        Disposer.register(getDisposable(), jcefBrowser);
        jcefBrowser.getComponent().setBackground(JBColor.BLACK);
        jcefBrowser.loadHTML(htmlContent);
        return jcefBrowser.getComponent();
    }
}


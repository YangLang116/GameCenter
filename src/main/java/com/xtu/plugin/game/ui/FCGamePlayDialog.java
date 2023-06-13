package com.xtu.plugin.game.ui;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.JBColor;
import com.intellij.ui.jcef.JBCefBrowser;
import com.xtu.plugin.game.utils.WindowUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FCGamePlayDialog extends DialogWrapper {

    public static void showDialog(String title, String htmlContent) {
        JComponent parentComponent = WindowUtils.getVisibleRootPanel();
        new FCGamePlayDialog(parentComponent, title, htmlContent).show();
    }

    private final String htmlContent;

    private FCGamePlayDialog(JComponent parentComponent, String title, String htmlContent) {
        super(null, parentComponent, false, IdeModalityType.IDE, false);
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


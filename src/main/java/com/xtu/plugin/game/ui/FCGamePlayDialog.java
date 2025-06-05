package com.xtu.plugin.game.ui;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.JBColor;
import com.intellij.ui.jcef.JBCefBrowser;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FCGamePlayDialog extends DialogWrapper {

    private final String htmlContent;

    public static void play(@NotNull Project project, @NotNull String title, @NotNull String htmlContent) {
        Runnable showRunnable = () -> {
            FCGamePlayDialog dialog = new FCGamePlayDialog(project, title, htmlContent);
            dialog.show();
        };
        Application application = ApplicationManager.getApplication();
        if (application.isDispatchThread()) {
            showRunnable.run();
        } else {
            application.invokeLater(showRunnable, ModalityState.any());
        }
    }

    private FCGamePlayDialog(@NotNull Project project, @NotNull String gameName, @NotNull String gameContent) {
        super(project, null, false, IdeModalityType.IDE, false);
        this.htmlContent = gameContent;
        setTitle(gameName);
        setSize(550, 543);
        init();
    }

    @Override
    protected @NonNls @Nullable String getDimensionServiceKey() {
        return FCGamePlayDialog.class.getName();
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


package com.xtu.plugin.game.utils;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by YangLang on 2017/11/25.
 */
public class ToastUtil {

    private static void make(@NotNull JComponent jComponent, @NotNull MessageType type, @NotNull String text) {
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(text, type, null)
                .setFadeoutTime(7500)
                .createBalloon()
                .show(RelativePoint.getCenterOf(jComponent), Balloon.Position.above);
    }


    public static void make(@NotNull Project project, @NotNull MessageType type, @NotNull String text) {
        Runnable showRunnable = () -> {
            StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
            if (statusBar == null) return;
            JComponent component = statusBar.getComponent();
            if (component == null) return;
            make(component, type, text);
        };
        Application application = ApplicationManager.getApplication();
        if (application.isDispatchThread()) {
            showRunnable.run();
        } else {
            application.invokeLater(showRunnable, ModalityState.any());
        }
    }
}
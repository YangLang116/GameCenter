package com.xtu.plugin.game.utils;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import org.jdesktop.swingx.util.WindowUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by YangLang on 2017/11/25.
 */
public class ToastUtil {

    public static void make(MessageType type, String text) {
        Runnable showRunnable = () -> {
            WindowManager windowManager = WindowManager.getInstance();
            JFrame currentFrame = windowManager.findVisibleFrame();
            if (currentFrame == null) return;
            Point windowCenterPoint = WindowUtils.getPointForCentering(currentFrame);
            RelativePoint relativePoint = RelativePoint.fromScreen(windowCenterPoint);
            JBPopupFactory.getInstance()
                    .createHtmlTextBalloonBuilder(text, type, null)
                    .setFadeoutTime(7500)
                    .createBalloon()
                    .show(relativePoint, Balloon.Position.below);
        };
        Application application = ApplicationManager.getApplication();
        if (application.isDispatchThread()) {
            showRunnable.run();
        } else {
            application.invokeLater(showRunnable, ModalityState.any());
        }
    }
}
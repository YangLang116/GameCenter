package com.xtu.plugin.game.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogUtils {

    private static final Logger LOG = Logger.getInstance("iFlutter -> ");

    public static void info(@NotNull String message) {
        LOG.info(message);
    }

    public static void error(@NotNull Project project,
                             @NotNull String entryPoint,
                             @NotNull Exception exception) {
        LOG.error(entryPoint + " : " + exception.getMessage());
        String content = "message: " + exception.getMessage() + "\n" +
                "stackTrace: \n" + getStackTrace(exception);
        AdviceUtils.submitData(project, "error catch", content);
    }

    private static String getStackTrace(@NotNull Exception e) {
        StringWriter strWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(strWriter, true));
        return strWriter.toString();
    }

}

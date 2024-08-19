package com.xtu.plugin.game.utils;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogUtils {

    private static final Logger LOG = Logger.getInstance("LogUtils -> ");

    public static void error(@NotNull String entryPoint,
                             @NotNull Exception exception) {
        LOG.error(entryPoint + " : " + exception.getMessage());
        String content = "message: \n" + exception.getMessage() + "\n" +
                "stackTrace: \n" + getStackTrace(exception);
        AdviceUtils.submitData(null, "error catch", content);
    }

    private static String getStackTrace(@NotNull Exception e) {
        StringWriter strWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(strWriter, true));
        return strWriter.toString();
    }

}

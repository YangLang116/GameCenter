package com.xtu.plugin.game.utils;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;

public class VersionUtils {

    public static String getPluginVersion() {
        PluginId pluginId = PluginId.getId("com.xtu.plugins.game.center");
        IdeaPluginDescriptor pluginDescriptor = PluginManagerCore.getPlugin(pluginId);
        if (pluginDescriptor != null) return pluginDescriptor.getVersion();
        return "0.0.0";
    }
}

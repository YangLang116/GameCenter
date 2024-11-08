package com.xtu.plugin.game.loader.fc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.xtu.plugin.game.downloader.FileDownloader;
import com.xtu.plugin.game.loader.GameLoader;
import com.xtu.plugin.game.loader.fc.entity.FCGame;
import com.xtu.plugin.game.loader.fc.entity.FCGameCategory;
import com.xtu.plugin.game.res.GameResManager;
import com.xtu.plugin.game.utils.FileUtils;
import org.apache.xerces.impl.dv.util.Base64;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FCGameLoader extends GameLoader {

    private FCGameLoader() {
        super("/game/fc/conf.properties");
    }

    private static final FCGameLoader sInstance = new FCGameLoader();

    public static FCGameLoader getInstance() {
        return sInstance;
    }

    private final List<FCGameCategory> categoryList = new ArrayList<>();

    private final Gson gson = new Gson();
    private final FileDownloader configDownloader = new FileDownloader();

    @Override
    public void parseConfig(@NotNull Map<String, String> config) {
        List<FCGameCategory> categories = new ArrayList<>();
        for (Map.Entry<String, String> entry : config.entrySet()) {
            FCGameCategory category = new FCGameCategory(entry.getKey(), entry.getValue());
            categories.add(category);
        }
        Collections.sort(categories);
        Application application = ApplicationManager.getApplication();
        application.invokeLater(() -> {
            categoryList.addAll(categories);
            categories.forEach(this::loadGame);
        });
    }

    public void loadGame(@NotNull FCGameCategory category) {
        if (category.path == null) return;
        String loadTitle = String.format("Loading %s Games", category.name);
        new Task.Backgroundable(null, loadTitle, false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(true);
                List<FCGame> fcGames = requestGame(category.path);
                if (fcGames == null) return;
                Application application = ApplicationManager.getApplication();
                application.invokeLater(() -> category.updateGames(fcGames));
            }
        }.queue();
    }

    @Nullable
    private List<FCGame> requestGame(@NotNull String name) {
        String configUrl = GameResManager.getInstance().getConfigUrl(name);
        String cachePath = configDownloader.downloadFileWithNetworkFirst(configUrl);
        if (cachePath == null) return null;
        String data = FileUtils.readAsString(cachePath);
        if (data == null) return null;
        return parseGameList(data);
    }

    @Nullable
    private List<FCGame> parseGameList(@NotNull String base64Str) {
        try {
            String jsonStr = new String(Base64.decode(base64Str));
            return gson.fromJson(jsonStr, new TypeToken<>() {
            });
        } catch (Exception e) {
            return null;
        }
    }

    @NotNull
    public List<FCGameCategory> getCategoryList() {
        return categoryList;
    }
}

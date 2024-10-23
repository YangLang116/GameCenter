package com.xtu.plugin.game.loader.fc;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.xtu.plugin.game.constant.GameConst;
import com.xtu.plugin.game.loader.fc.entity.FCGame;
import com.xtu.plugin.game.loader.fc.entity.FCGameCategory;
import com.xtu.plugin.game.utils.StreamUtils;
import org.apache.xerces.impl.dv.util.Base64;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FCGameLoader {

    private FCGameLoader() {
    }

    private static final FCGameLoader sInstance = new FCGameLoader();

    public static FCGameLoader getInstance() {
        return sInstance;
    }

    private final List<FCGameCategory> categoryList = new ArrayList<>();

    public void load() {
        Application application = ApplicationManager.getApplication();
        application.executeOnPooledThread(() -> {
            Map<String, String> configList = StreamUtils.loadConfig("/game/fc/conf.properties");
            assert configList != null;
            List<FCGameCategory> categories = new ArrayList<>();
            for (Map.Entry<String, String> entry : configList.entrySet()) {
                FCGameCategory category = new FCGameCategory(entry.getKey(), entry.getValue());
                categories.add(category);
            }
            application.invokeLater(() -> {
                categoryList.addAll(categories);
                for (FCGameCategory category : categories) {
                    loadGame(category);
                }
            });
        });
    }

    public void loadGame(@NotNull FCGameCategory category) {
        if (category.path == null) return;
        Application application = ApplicationManager.getApplication();
        String loadTitle = String.format("Loading %s Games", category.name);
        new Task.Backgroundable(null, loadTitle, false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(true);
                List<FCGame> fcGames = requestGame(category.path);
                if (fcGames == null) return;
                application.invokeLater(() -> category.updateGames(fcGames));
            }
        }.queue();
    }

    @Nullable
    private List<FCGame> requestGame(@NotNull String path) {
        byte[] data = StreamUtils.readDataFromUrl(GameConst.PREFIX_CONFIG + path);
        if (data == null) return null;
        String base64Str = new String(data, StandardCharsets.UTF_8);
        return parseGameList(base64Str);
    }

    @Nullable
    private static List<FCGame> parseGameList(@NotNull String base64Str) {
        try {
            String jsonStr = new String(Base64.decode(base64Str));
            JSONArray gameJsonArray = new JSONArray(jsonStr);
            List<FCGame> gameList = new ArrayList<>();
            for (int i = 0; i < gameJsonArray.length(); i++) {
                JSONObject gameObj = (JSONObject) gameJsonArray.get(i);
                String name = gameObj.optString("name");
                String desc = gameObj.optString("desc");
                String icon = gameObj.optString("icon");
                String url = gameObj.optString("url");
                gameList.add(new FCGame(name, desc, icon, url));
            }
            return gameList;
        } catch (Exception e) {
            return null;
        }
    }

    @NotNull
    public List<FCGameCategory> getCategoryList() {
        return categoryList;
    }
}

package com.xtu.plugin.game.ui;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.jcef.JBCefApp;
import com.xtu.plugin.game.conf.FcGameLoader;
import com.xtu.plugin.game.utils.StreamUtils;
import com.xtu.plugin.game.utils.ToastUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FCGameListDialog extends DialogWrapper {

    public static void showGameList(@NotNull Project project) {
        new FCGameListDialog(project).show();
    }

    private final Project project;

    private FCGameListDialog(@NotNull Project project) {
        super(project, null, false, IdeModalityType.IDE, false);
        this.project = project;
        setHorizontalStretch(1.2f);
        setVerticalStretch(1.5f);
        init();
    }

    @Override
    protected @Nullable @NonNls String getDimensionServiceKey() {
        return FCGameListDialog.class.getSimpleName();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(getGameListView(), BorderLayout.CENTER);
        return rootPanel;
    }

    private JComponent getGameListView() {
        final JBList<FcGameLoader.Game> listView = new JBList<>();
        DefaultListModel<FcGameLoader.Game> listModel = new DefaultListModel<>();
        listModel.addAll(FcGameLoader.getInstance().getGameList());
        listView.setModel(listModel);
        listView.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel textLabel = new JLabel(value.name);
            textLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            return textLabel;
        });
        listView.addListSelectionListener(e -> {
            FcGameLoader.Game selectedGame = listView.getSelectedValue();
            close(DialogWrapper.OK_EXIT_CODE);
            String gameContent = getGameContent(selectedGame.name, selectedGame.path);
            boolean supportJCEF = JBCefApp.isSupported();
            if (supportJCEF) {
                FCGamePlayDialog.showDialog(project, selectedGame.name, gameContent);
            } else {
                loadHtmlFromFs(gameContent);
            }
        });
        return new JBScrollPane(listView);
    }

    private void loadHtmlFromFs(String gameContent) {
        try {
            VirtualFile projectDir = ProjectUtil.guessProjectDir(project);
            if (projectDir == null) return;
            VirtualFile ideaDirectory = projectDir.findChild(".idea");
            if (ideaDirectory == null) ideaDirectory = projectDir.createChildDirectory(project, ".idea");
            VirtualFile gameHtml = ideaDirectory.findOrCreateChildData(project, "game.html");
            VfsUtil.saveText(gameHtml, gameContent);
            File htmlFile = new File(gameHtml.getPath());
            BrowserUtil.browse(htmlFile);
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.make(project, MessageType.ERROR, e.getMessage());
        }
    }


    private String getGameContent(String title, String gamePath) {
        String htmlContent = StreamUtils.readTextFromResource("/game/fc/html/index.html");
        htmlContent = htmlContent.replace("{title}", title);
        //css
        String cssStyle = StreamUtils.readTextFromResource("/game/fc/html/js_nes.css");
        htmlContent = htmlContent.replace("{htmlCss}", cssStyle);
        //script
        String scriptContent = StreamUtils.readTextFromResource("/game/fc/html/libs/jquery-1.4.2.min.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/nes.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/utils.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/cpu.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/keyboard.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/mappers.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/papu.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/ppu.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/libs/rom.js") +
                "\n" +
                StreamUtils.readTextFromResource("/game/fc/html/ui.js");
        htmlContent = htmlContent.replace("{libScript}", scriptContent);
        //game Data
        htmlContent = htmlContent.replace("{GameFile}", gamePath);
        return htmlContent;
    }
}

package com.xtu.plugin.game.ui;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.jcef.JBCefApp;
import com.xtu.plugin.game.conf.FcGameLoader;
import com.xtu.plugin.game.utils.GameUtils;
import com.xtu.plugin.game.utils.WindowUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class FCGameListDialog extends DialogWrapper {

    public static void showGameList() {
        JComponent parentComponent = WindowUtils.getVisibleRootPanel();
        new FCGameListDialog(parentComponent).show();
    }

    private FCGameListDialog(JComponent parentComponent) {
        super(null, parentComponent, false, IdeModalityType.IDE, false);
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
            playGame(selectedGame);
        });
        return new JBScrollPane(listView);
    }

    private static void playGame(FcGameLoader.Game selectedGame) {
        boolean supportJCEF = JBCefApp.isSupported();
        String gameContent = GameUtils.getGameOnlineHtml(selectedGame.name, selectedGame.path);
        if (supportJCEF) {
            FCGamePlayDialog.showDialog(selectedGame.name, gameContent);
        } else {
            GameUtils.openGameWithBrowser(gameContent, "online.html");
        }
    }
}

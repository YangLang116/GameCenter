package com.xtu.plugin.game.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import com.intellij.util.ui.JBUI;
import com.xtu.plugin.game.loader.fc.FCGame;
import com.xtu.plugin.game.loader.fc.FCGameLoader;
import com.xtu.plugin.game.ui.render.FCGameCellRender;
import com.xtu.plugin.game.utils.GameUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FCGameListDialog extends DialogWrapper {

    private final Project project;

    public static void show(@NotNull Project project) {
        FCGameListDialog dialog = new FCGameListDialog(project);
        dialog.show();
    }

    private FCGameListDialog(@NotNull Project project) {
        super(project, null, false, IdeModalityType.PROJECT, false);
        this.project = project;
        setTitle("Game List");
        setSize(360, 520);
        init();
    }

    @Override
    protected @Nullable @NonNls String getDimensionServiceKey() {
        return FCGameListDialog.class.getSimpleName();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(createTipLabel(), BorderLayout.NORTH);
        contentPanel.add(getGameTabView(), BorderLayout.CENTER);
        return contentPanel;
    }

    @NotNull
    private JLabel createTipLabel() {
        JLabel tip = new JLabel("Because there are too many games, we cannot ensure that every game can run normally. ");
        tip.setBorder(JBUI.Borders.empty(5, 0));
        return tip;
    }

    @NotNull
    private JComponent getGameTabView() {
        JBTabs tabs = new JBTabsImpl(project);
        FCGameLoader gameLoader = FCGameLoader.getInstance();
        tabs.addTab(createGameTabInfo("Action", gameLoader.actionGameList));
        tabs.addTab(createGameTabInfo("Role-Playing", gameLoader.rolePlayGameList));
        tabs.addTab(createGameTabInfo("Shooter", gameLoader.shooterGameList));
        tabs.addTab(createGameTabInfo("Sports", gameLoader.sportGameList));
        tabs.addTab(createGameTabInfo("Puzzle", gameLoader.puzzleGameList));
        tabs.addTab(createGameTabInfo("Strategy", gameLoader.strategyGameList));
        tabs.addTab(createGameTabInfo("Adventure", gameLoader.adventureGameList));
        tabs.addTab(createGameTabInfo("Racing", gameLoader.racingGameList));
        tabs.addTab(createGameTabInfo("Chess", gameLoader.chessGameList));
        tabs.addTab(createGameTabInfo("Table-Game", gameLoader.tableGameList));
        tabs.addTab(createGameTabInfo("Simulation", gameLoader.simGameList));
        tabs.addTab(createGameTabInfo("Compilation", gameLoader.comGameList));
        return tabs.getComponent();
    }

    @NotNull
    private TabInfo createGameTabInfo(String type, @NotNull List<FCGame> gameList) {
        final JBList<FCGame> listView = new JBList<>();
        DefaultListModel<FCGame> listModel = new DefaultListModel<>();
        listModel.addAll(gameList);
        listView.setModel(listModel);
        listView.setCellRenderer(new FCGameCellRender());
        listView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = listView.locationToIndex(e.getPoint());
                onGameItemClick(index, gameList);
            }
        });
        int MaskCode = SystemInfo.isMac ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;
        listView.registerKeyboardAction(e -> doSearch(listView, gameList),
                KeyStroke.getKeyStroke(KeyEvent.VK_F, MaskCode),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        JBScrollPane scrollPane = new JBScrollPane(listView);
        TabInfo tabInfo = new TabInfo(scrollPane);
        tabInfo.setText(type);
        return tabInfo;
    }

    private void doSearch(@NotNull JBList<FCGame> listView, @NotNull List<FCGame> gameList) {
        String searchKey = Messages.showInputDialog("", "Enter Game Name", Messages.getInformationIcon());
        if (StringUtils.isEmpty(searchKey)) return;
        int selectIndex = -1;
        for (int i = 0; i < gameList.size(); i++) {
            String gameName = gameList.get(i).name;
            if (gameName.contains(searchKey)) {
                selectIndex = i;
                break;
            }
        }
        if (selectIndex == -1) return;
        listView.setSelectedIndex(selectIndex);
        listView.ensureIndexIsVisible(selectIndex);
    }

    private void onGameItemClick(int index, @NotNull List<FCGame> gameList) {
        if (index < 0 || index >= gameList.size()) return;
        close(DialogWrapper.OK_EXIT_CODE);
        FCGame selectedGame = gameList.get(index);
        String gameContent = GameUtils.getGameOnlineHtml(selectedGame.name, selectedGame.url);
        if (JBCefApp.isSupported()) {
            FCGamePlayDialog.play(project, selectedGame.name, gameContent);
        } else {
            GameUtils.openGameWithBrowser(project, gameContent, "online.html");
        }
    }
}

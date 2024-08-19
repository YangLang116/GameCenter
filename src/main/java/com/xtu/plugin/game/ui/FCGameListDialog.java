package com.xtu.plugin.game.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import com.intellij.util.ui.JBUI;
import com.xtu.plugin.game.loader.fc.FCGame;
import com.xtu.plugin.game.loader.fc.FCGameLoader;
import com.xtu.plugin.game.manager.GameManager;
import com.xtu.plugin.game.ui.render.FCGameCellComponent;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class FCGameListDialog extends DialogWrapper implements FCGameCellComponent.OnItemClickListener {

    private final Project project;

    public static void show(@NotNull Project project) {
        FCGameListDialog dialog = new FCGameListDialog(project);
        dialog.show();
    }

    private FCGameListDialog(@NotNull Project project) {
        super(project, null, false, IdeModalityType.PROJECT, false);
        this.project = project;
        setTitle("Game List");
        setSize(180, 520);
        init();
    }

    @Override
    protected @Nullable @NonNls String getDimensionServiceKey() {
        return FCGameListDialog.class.getSimpleName();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(getGameTabView(), BorderLayout.CENTER);
        contentPanel.add(createTipLabel(), BorderLayout.SOUTH);
        return contentPanel;
    }

    @NotNull
    private JLabel createTipLabel() {
        JLabel tip = new JLabel("Because there are too many games, we cannot ensure that every game can run normally. ");
        tip.setFont(new Font(null, Font.BOLD, JBUI.scaleFontSize(12f)));
        tip.setBorder(JBUI.Borders.empty(10, 5));
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
        final Box listView = Box.createVerticalBox();
        for (FCGame fcGame : gameList) {
            listView.add(new FCGameCellComponent(fcGame, this));
        }
        int MaskCode = SystemInfo.isMac ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;
        listView.registerKeyboardAction(e -> doSearch(listView, gameList),
                KeyStroke.getKeyStroke(KeyEvent.VK_F, MaskCode),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        JBScrollPane scrollPane = new JBScrollPane(listView);
        TabInfo tabInfo = new TabInfo(scrollPane);
        tabInfo.setText(type);
        return tabInfo;
    }

    private void doSearch(@NotNull Box listView, @NotNull List<FCGame> gameList) {
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
        Component component = listView.getComponent(selectIndex);
        Rectangle bounds = component.getBounds();
        listView.scrollRectToVisible(bounds);
    }

    @Override
    public void onClick(@NotNull FCGame game) {
        close(DialogWrapper.CLOSE_EXIT_CODE);
        GameManager.getGameOnlineHtml(game, html -> {
            if (JBCefApp.isSupported()) {
                FCGamePlayDialog.play(project, game.name, html);
            } else {
                GameManager.openGameWithBrowser(project, "online.html", html);
            }
        });
    }
}

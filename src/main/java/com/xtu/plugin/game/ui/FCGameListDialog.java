package com.xtu.plugin.game.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import com.xtu.plugin.game.conf.FcGameLoader;
import com.xtu.plugin.game.utils.GameUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FCGameListDialog extends DialogWrapper {

    public static void showGameList() {
        ProjectManager projectManager = ProjectManager.getInstance();
        Project project = projectManager.getDefaultProject();
        new FCGameListDialog(project).show();
    }

    private final Project project;

    private FCGameListDialog(Project project) {
        super(project, null, false, IdeModalityType.IDE, false);
        this.project = project;
        setTitle("因游戏太多，无法确保每个游戏可正常运行，对于无法运行的游戏可通过Suggestion联系删除");
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
        rootPanel.add(getGameTabView(), BorderLayout.CENTER);
        return rootPanel;
    }

    private JComponent getGameTabView() {
        JBTabs tabs = new JBTabsImpl(project);
        tabs.addTab(createGameTabInfo("动作类", FcGameLoader.getInstance().actionGameList));
        tabs.addTab(createGameTabInfo("角色扮演类", FcGameLoader.getInstance().rolePlayGameList));
        tabs.addTab(createGameTabInfo("射击类", FcGameLoader.getInstance().shooterGameList));
        tabs.addTab(createGameTabInfo("运动类", FcGameLoader.getInstance().sportGameList));
        tabs.addTab(createGameTabInfo("益智类", FcGameLoader.getInstance().eduGameList));
        tabs.addTab(createGameTabInfo("策略类", FcGameLoader.getInstance().strategyGameList));
        tabs.addTab(createGameTabInfo("冒险类", FcGameLoader.getInstance().adventureGameList));
        tabs.addTab(createGameTabInfo("竞速类", FcGameLoader.getInstance().racingGameList));
        tabs.addTab(createGameTabInfo("棋牌类", FcGameLoader.getInstance().chessGameList));
        tabs.addTab(createGameTabInfo("桌面类", FcGameLoader.getInstance().desktopGameList));
        tabs.addTab(createGameTabInfo("战略模拟类", FcGameLoader.getInstance().strategySimGameList));
        tabs.addTab(createGameTabInfo("合集类", FcGameLoader.getInstance().collectionGameList));
        return tabs.getComponent();
    }

    private TabInfo createGameTabInfo(String type, List<FcGameLoader.Game> gameList) {
        final JBList<FcGameLoader.Game> listView = new JBList<>();
        DefaultListModel<FcGameLoader.Game> listModel = new DefaultListModel<>();
        listModel.addAll(gameList);
        listView.setModel(listModel);
        listView.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel textLabel = new JLabel(value.name);
            textLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            return textLabel;
        });
        listView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = listView.locationToIndex(e.getPoint());
                if (index < 0 || index >= gameList.size()) return;
                FcGameLoader.Game selectedGame = gameList.get(index);
                close(DialogWrapper.OK_EXIT_CODE);
                playGame(selectedGame);
            }
        });
        int MaskCode = SystemInfo.isMac ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;
        listView.registerKeyboardAction(e -> {
            String searchKey = Messages.showInputDialog("", "输入游戏名称", Messages.getInformationIcon());
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
        }, KeyStroke.getKeyStroke(KeyEvent.VK_F, MaskCode), JComponent.WHEN_IN_FOCUSED_WINDOW);
        JBScrollPane scrollPane = new JBScrollPane(listView);
        TabInfo tabInfo = new TabInfo(scrollPane);
        tabInfo.setText(type);
        return tabInfo;
    }

    private static void playGame(FcGameLoader.Game selectedGame) {
        boolean supportJCEF = JBCefApp.isSupported();
        String gameContent = GameUtils.getGameOnlineHtml(selectedGame.name, selectedGame.url);
        if (supportJCEF) {
            FCGamePlayDialog.showDialog(selectedGame.name, gameContent);
        } else {
            GameUtils.openGameWithBrowser(gameContent, "online.html");
        }
    }
}

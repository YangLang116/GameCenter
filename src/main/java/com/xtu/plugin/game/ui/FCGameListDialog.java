package com.xtu.plugin.game.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import com.intellij.util.ui.JBUI;
import com.xtu.plugin.game.constant.GameConst;
import com.xtu.plugin.game.loader.fc.FCGameLoader;
import com.xtu.plugin.game.loader.fc.entity.FCGame;
import com.xtu.plugin.game.loader.fc.entity.FCGameCategory;
import com.xtu.plugin.game.manager.GameManager;
import com.xtu.plugin.game.utils.StringUtils;
import com.xtu.plugin.game.utils.ToastUtil;
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
        JLabel tip = new JLabel(GameConst.TIP);
        tip.setForeground(JBColor.foreground().darker());
        tip.setFont(new Font(null, Font.PLAIN, JBUI.scaleFontSize(12f)));
        tip.setBorder(JBUI.Borders.empty(10, 5));
        return tip;
    }

    @NotNull
    private JComponent getGameTabView() {
        JBTabs tabs = new JBTabsImpl(project);
        List<FCGameCategory> categoryList = FCGameLoader.getInstance().getCategoryList();
        for (FCGameCategory category : categoryList) {
            TabInfo tabInfo = new TabInfo(buildContentPanel(category));
            tabInfo.setText(category.name);
            tabs.addTab(tabInfo);
        }
        return tabs.getComponent();
    }

    private @NotNull JComponent buildContentPanel(@NotNull FCGameCategory category) {
        if (category.noGames()) {
            JLabel emptyTip = new JLabel("Click To Reload");
            emptyTip.setIcon(Messages.getErrorIcon());
            emptyTip.setForeground(JBColor.foreground());
            emptyTip.setFont(new Font(null, Font.BOLD, JBUI.scaleFontSize(18f)));
            emptyTip.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    close(DialogWrapper.CLOSE_EXIT_CODE);
                    FCGameLoader.getInstance().loadGame(category);
                }
            });
            GridBagConstraints tipConstraint = new GridBagConstraints();
            tipConstraint.anchor = GridBagConstraints.CENTER;
            tipConstraint.fill = GridBagConstraints.NONE;
            tipConstraint.weightx = 0.5;
            tipConstraint.weighty = 0.5;
            JPanel emptyPanel = new JPanel(new GridBagLayout());
            emptyPanel.add(emptyTip, tipConstraint);
            return emptyPanel;
        } else {
            final Box listView = Box.createVerticalBox();
            for (FCGame fcGame : category.games) {
                listView.add(new FCGameCellComponent(fcGame, this));
            }
            int MaskCode = SystemInfo.isMac ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;
            listView.registerKeyboardAction(e -> doSearch(listView, category.games),
                    KeyStroke.getKeyStroke(KeyEvent.VK_F, MaskCode),
                    JComponent.WHEN_IN_FOCUSED_WINDOW);
            return new JBScrollPane(listView);
        }
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
        GameManager.loadOnlineGame(project, game, new GameManager.OnGameHtmlListener() {
            @Override
            public void onReady(@NotNull String html) {
                if (JBCefApp.isSupported()) {
                    FCGamePlayDialog.play(project, game.name, html);
                } else {
                    GameManager.openGameWithBrowser(project, "online.html", html);
                }
            }

            @Override
            public void onFail(@NotNull String error) {
                ToastUtil.make(project, MessageType.ERROR, error);
            }
        });
    }
}

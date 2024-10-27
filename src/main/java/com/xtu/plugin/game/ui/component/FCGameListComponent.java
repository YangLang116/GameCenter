package com.xtu.plugin.game.ui.component;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import com.xtu.plugin.game.downloader.FileDownloader;
import com.xtu.plugin.game.helper.GameStarter;
import com.xtu.plugin.game.loader.fc.FCGameLoader;
import com.xtu.plugin.game.loader.fc.entity.FCGame;
import com.xtu.plugin.game.loader.fc.entity.FCGameCategory;
import com.xtu.plugin.game.ui.FCGameCellComponent;
import com.xtu.plugin.game.ui.callback.OnGameSelectListener;
import com.xtu.plugin.game.ui.callback.OnRefreshListener;
import com.xtu.plugin.game.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FCGameListComponent extends JPanel {

    private final FileDownloader coverDownloader = new FileDownloader();

    public FCGameListComponent(@NotNull Project project,
                               @NotNull DialogWrapper owner,
                               @NotNull String emptyTip,
                               @NotNull FCGameCategory category) {
        setLayout(new BorderLayout());
        if (category.noGames()) {
            OnRefreshListener onRefreshListener = () -> {
                owner.close(DialogWrapper.CLOSE_EXIT_CODE);
                FCGameLoader.getInstance().loadGame(category);
            };
            addEmptyComponent(emptyTip, onRefreshListener);
        } else {
            OnGameSelectListener itemClickListener = game -> {
                owner.close(DialogWrapper.CLOSE_EXIT_CODE);
                GameStarter.getInstance().playOnlineFCGame(project, game);
            };
            addListComponent(project, category.games, itemClickListener);
        }
    }

    private void addEmptyComponent(@NotNull String tip, @NotNull OnRefreshListener onRefreshListener) {
        JLabel emptyTip = new JLabel(tip);
        emptyTip.setForeground(JBColor.foreground());
        emptyTip.setFont(new Font(null, Font.BOLD, 18));
        emptyTip.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onRefreshListener.onRefresh();
            }
        });
        GridBagConstraints tipConstraint = new GridBagConstraints();
        tipConstraint.anchor = GridBagConstraints.CENTER;
        tipConstraint.fill = GridBagConstraints.NONE;
        tipConstraint.weightx = 0.5;
        tipConstraint.weighty = 0.5;
        JPanel emptyPanel = new JPanel(new GridBagLayout());
        emptyPanel.add(emptyTip, tipConstraint);
        add(emptyPanel, BorderLayout.CENTER);
    }

    private void addListComponent(@NotNull Project project,
                                  @NotNull List<FCGame> games,
                                  @NotNull OnGameSelectListener clickListener) {
        final Box listView = Box.createVerticalBox();
        listView.setBorder(JBUI.Borders.empty(0, 5));
        for (int index = games.size() - 1; index >= 0; index--) {
            FCGame fcGame = games.get(index);
            listView.add(new FCGameCellComponent(project, coverDownloader, fcGame, clickListener), 0);
        }
        int MaskCode = SystemInfo.isMac ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;
        listView.registerKeyboardAction(e -> doSearch(listView, games), KeyStroke.getKeyStroke(KeyEvent.VK_F, MaskCode), JComponent.WHEN_IN_FOCUSED_WINDOW);
        JBScrollPane scrollPane = new JBScrollPane(listView, JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JBScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
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
}

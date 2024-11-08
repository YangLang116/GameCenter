package com.xtu.plugin.game.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.JBUI;
import com.xtu.plugin.game.loader.fc.FCGameLoader;
import com.xtu.plugin.game.loader.fc.entity.FCGameCategory;
import com.xtu.plugin.game.res.GameResManager;
import com.xtu.plugin.game.ui.component.FCGameEmptyComponent;
import com.xtu.plugin.game.ui.component.FCGameListComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FCGameCategoryDialog extends DialogWrapper {

    public static void show(@NotNull Project project) {
        FCGameCategoryDialog dialog = new FCGameCategoryDialog(project);
        dialog.show();
    }

    private JPanel rootPanel;
    private JTabbedPane tabbedPane;

    private FCGameCategoryDialog(@NotNull Project project) {
        super(project, null, true, IdeModalityType.IDE, false);
        initWindow();
        initContentView(project);
        init();
    }

    private void initWindow() {
        setTitle("");
        setSize(480, 520);
        setResizable(false);
    }

    private void initContentView(@NotNull Project project) {
        this.rootPanel = new JPanel(new BorderLayout());
        this.rootPanel.add(this.tabbedPane = getGameTabView(project), BorderLayout.CENTER);
        this.rootPanel.add(createTipLabel(), BorderLayout.SOUTH);
    }

    @NotNull
    private JTabbedPane getGameTabView(@NotNull Project project) {
        List<FCGameCategory> categoryList = FCGameLoader.getInstance().getCategoryList();
        JTabbedPane tabbedPane = new JTabbedPane(JBTabbedPane.LEFT, JBTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setFont(new Font(null, Font.BOLD, 15));
        for (FCGameCategory category : categoryList) {
            tabbedPane.addTab(category.name, createTabContentView(project, category));
        }
        return tabbedPane;
    }

    @NotNull
    private JComponent createTabContentView(@NotNull Project project, @NotNull FCGameCategory category) {
        if (category.noGames()) {
            return new FCGameEmptyComponent("Click To Reload", () -> {
                close(DialogWrapper.CLOSE_EXIT_CODE);
                FCGameLoader.getInstance().loadGame(category);
            });
        } else {
            return new FCGameListComponent(project, category.games);
        }
    }

    @NotNull
    private JComponent createTipLabel() {
        String gameNode = GameResManager.getInstance().getGameNote();
        JLabel tip = new JLabel(gameNode);
        tip.setForeground(JBColor.foreground().darker());
        tip.setFont(new Font(null, Font.PLAIN, 12));
        tip.setBorder(JBUI.Borders.empty(10, 5));
        return tip;
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return this.rootPanel;
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return (JComponent) this.tabbedPane.getComponentAt(0);
    }
}

package com.xtu.plugin.game.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.JBUI;
import com.xtu.plugin.game.loader.fc.FCGameLoader;
import com.xtu.plugin.game.loader.fc.entity.FCGameCategory;
import com.xtu.plugin.game.res.GameResManager;
import com.xtu.plugin.game.ui.component.FCGameListComponent;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FCGameCategoryDialog extends DialogWrapper {

    private final Project project;

    public static void show(@NotNull Project project) {
        FCGameCategoryDialog dialog = new FCGameCategoryDialog(project);
        dialog.show();
    }

    private FCGameCategoryDialog(@NotNull Project project) {
        super(project, null, false, IdeModalityType.IDE, false);
        this.project = project;
        setTitle("Game Category");
        setSize(640, 520);
        init();
    }

    @Override
    protected @Nullable @NonNls String getDimensionServiceKey() {
        return FCGameCategoryDialog.class.getSimpleName();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(getGameTabView(), BorderLayout.CENTER);
        contentPanel.add(createTipLabel(), BorderLayout.SOUTH);
        return contentPanel;
    }

    @NotNull
    private JComponent getGameTabView() {
        List<FCGameCategory> categoryList = FCGameLoader.getInstance().getCategoryList();
        JTabbedPane tabbedPane = new JTabbedPane(JBTabbedPane.LEFT, JBTabbedPane.SCROLL_TAB_LAYOUT);
        for (FCGameCategory category : categoryList) {
            JComponent content = new FCGameListComponent(project, this, "Click To Reload", category);
            tabbedPane.addTab(category.name, content);
        }
        return tabbedPane;
    }

    @NotNull
    private JLabel createTipLabel() {
        String gameNode = GameResManager.getInstance().getGameNote();
        JLabel tip = new JLabel(gameNode);
        tip.setForeground(JBColor.foreground().darker());
        tip.setFont(new Font(null, Font.PLAIN, 12));
        tip.setBorder(JBUI.Borders.empty(10, 5));
        return tip;
    }
}

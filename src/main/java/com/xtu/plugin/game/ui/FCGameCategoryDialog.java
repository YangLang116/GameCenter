package com.xtu.plugin.game.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.JBUI;
import com.xtu.plugin.game.constant.GameConst;
import com.xtu.plugin.game.loader.fc.FCGameLoader;
import com.xtu.plugin.game.loader.fc.entity.FCGameCategory;
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
        setSize(360, 520);
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
        JTabbedPane tabbedPane = new JTabbedPane(JBTabbedPane.TOP, JBTabbedPane.SCROLL_TAB_LAYOUT);
        List<FCGameCategory> categoryList = FCGameLoader.getInstance().getCategoryList();
        for (FCGameCategory category : categoryList) {
            tabbedPane.addTab(category.name, new FCGameListComponent(project, this, "Click To Reload", category));
        }
        return tabbedPane;
    }

    @NotNull
    private JLabel createTipLabel() {
        JLabel tip = new JLabel(GameConst.TIP);
        tip.setForeground(JBColor.foreground().darker());
        tip.setFont(new Font(null, Font.PLAIN, JBUI.scaleFontSize(12f)));
        tip.setBorder(JBUI.Borders.empty(10, 5));
        return tip;
    }
}

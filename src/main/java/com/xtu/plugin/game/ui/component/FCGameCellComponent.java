package com.xtu.plugin.game.ui.component;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import icons.PluginIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class FCGameCellComponent extends JPanel {

    protected JLabel coverView;
    protected JLabel titleView;
    protected JTextArea descView;

    public FCGameCellComponent() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(-1, 120));
        add(createGameBox(), BorderLayout.CENTER);
        add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.SOUTH);
    }

    private JComponent createGameBox() {
        JPanel rootContainer = new JPanel(new BorderLayout());
        rootContainer.setBorder(JBUI.Borders.empty(10, 10));
        rootContainer.add(createCoverView(), BorderLayout.WEST);
        rootContainer.add(createInfoContainer(), BorderLayout.CENTER);
        return rootContainer;
    }

    private JComponent createCoverView() {
        Box panel = Box.createVerticalBox();
        this.coverView = new JLabel();
        this.coverView.setPreferredSize(new Dimension(100, 100));
        panel.add(this.coverView);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JComponent createInfoContainer() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(JBUI.Borders.emptyLeft(5));
        panel.add(this.titleView = createTitleView(), BorderLayout.NORTH);
        panel.add(this.descView = createDescView(), BorderLayout.CENTER);
        return panel;
    }

    private JLabel createTitleView() {
        JLabel titleLabel = new JLabel();
        titleLabel.setFont(new Font(null, Font.BOLD, 18));
        titleLabel.setForeground(JBColor.foreground());
        return titleLabel;
    }

    private JTextArea createDescView() {
        JTextArea descLabel = new JTextArea();
        descLabel.setEditable(false);
        descLabel.setLineWrap(true);
        descLabel.setWrapStyleWord(true);
        descLabel.setFont(new Font(null, Font.PLAIN, 13));
        descLabel.setForeground(JBColor.foreground().darker());
        descLabel.setBackground(new Color(0, 0, 0, 0));
        descLabel.setBorder(JBUI.Borders.empty(5, 0));
        return descLabel;
    }

    public void setData(@NotNull String name,
                        @NotNull String desc,
                        @Nullable Image cover) {
        this.titleView.setText(String.format("<html><u>%s</u></html>", name));
        this.titleView.setToolTipText(name);

        String descSummary = desc.length() > 50 ? desc.substring(0, 50) + "..." : desc;
        this.descView.setText(descSummary);
        this.descView.setToolTipText(desc);

        this.coverView.setIcon(cover == null ? PluginIcons.GAME : new ImageIcon(cover));
    }
}

package com.xtu.plugin.game.ui;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.xtu.plugin.game.loader.fc.entity.FCGame;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FCGameCellComponent extends JPanel {

    public FCGameCellComponent(@NotNull FCGame game,
                               @NotNull OnItemClickListener clickListener) {
        setLayout(new BorderLayout());
        MouseAdapter mouseAdapter = getMouseAdapter(game, clickListener);
        add(createName(game.name), BorderLayout.NORTH);
        add(createDesc(game.desc, mouseAdapter), BorderLayout.CENTER);
        add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.SOUTH);
        addMouseListener(mouseAdapter);
    }

    @NotNull
    private MouseAdapter getMouseAdapter(@NotNull FCGame game, @NotNull OnItemClickListener clickListener) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickListener.onClick(game);
            }
        };
    }

    @NotNull
    private static JTextArea createDesc(@NotNull String desc, @NotNull MouseAdapter mouseAdapter) {
        JTextArea descLabel = new JTextArea(desc);
        descLabel.setEditable(false);
        descLabel.setLineWrap(true);
        descLabel.setWrapStyleWord(true);
        descLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        descLabel.setBorder(JBUI.Borders.empty(0, 5, 10, 5));
        descLabel.setForeground(JBColor.foreground().darker());
        descLabel.setBackground(new Color(0, 0, 0, 0));
        descLabel.setFont(new Font(null, Font.PLAIN, JBUI.scaleFontSize(13f)));
        descLabel.addMouseListener(mouseAdapter);
        return descLabel;
    }

    @NotNull
    private static JLabel createName(@NotNull String name) {
        String gameName = String.format("<html><u>%s</u></html>", name);
        JLabel nameLabel = new JLabel(gameName);
        nameLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        nameLabel.setBorder(JBUI.Borders.empty(10, 5));
        nameLabel.setForeground(JBColor.foreground());
        nameLabel.setFont(new Font(null, Font.BOLD, JBUI.scaleFontSize(18f)));
        return nameLabel;
    }

    public interface OnItemClickListener {
        void onClick(@NotNull FCGame game);
    }
}

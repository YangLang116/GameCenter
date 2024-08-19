package com.xtu.plugin.game.ui.render;

import com.intellij.util.ui.JBUI;
import com.xtu.plugin.game.loader.fc.FCGame;
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
        descLabel.setBackground(new Color(0, 0, 0, 0));
        descLabel.setFont(new Font(null, Font.PLAIN, JBUI.scaleFontSize(12f)));
        descLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        descLabel.setBorder(JBUI.Borders.empty(0, 5, 10, 5));
        descLabel.addMouseListener(mouseAdapter);
        return descLabel;
    }

    @NotNull
    private static JLabel createName(@NotNull String gameName) {
        JLabel nameLabel = new JLabel(gameName);
        nameLabel.setFont(new Font(null, Font.BOLD, JBUI.scaleFontSize(16f)));
        nameLabel.setBorder(JBUI.Borders.empty(10, 5));
        nameLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        return nameLabel;
    }

    public interface OnItemClickListener {
        void onClick(@NotNull FCGame game);
    }
}

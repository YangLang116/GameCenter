package com.xtu.plugin.game.ui.component;

import com.intellij.ui.JBColor;
import com.xtu.plugin.game.ui.callback.OnRefreshListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FCGameEmptyComponent extends JPanel {

    public FCGameEmptyComponent(@NotNull String tip) {
        this(tip, null);
    }

    public FCGameEmptyComponent(@NotNull String tip, @Nullable OnRefreshListener listener) {
        setLayout(new GridBagLayout());
        addLabel(tip, listener);
    }

    private void addLabel(@NotNull String tip, @Nullable OnRefreshListener listener) {
        JLabel emptyTip = new JLabel(tip);
        emptyTip.setForeground(JBColor.foreground());
        emptyTip.setFont(new Font(null, Font.BOLD, 18));

        GridBagConstraints tipConstraint = new GridBagConstraints();
        tipConstraint.anchor = GridBagConstraints.CENTER;
        tipConstraint.fill = GridBagConstraints.NONE;
        tipConstraint.weightx = 0.5;
        tipConstraint.weighty = 0.5;

        add(emptyTip, tipConstraint);

        bindListener(emptyTip, listener);
    }

    private void bindListener(@NotNull JComponent component, @Nullable OnRefreshListener listener) {
        if (listener == null) return;
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onRefresh();
            }
        });
    }

}

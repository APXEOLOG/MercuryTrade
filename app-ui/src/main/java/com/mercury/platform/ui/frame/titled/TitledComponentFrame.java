package com.mercury.platform.ui.frame.titled;

import com.mercury.platform.ui.frame.ComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 18.01.2017.
 */
public abstract class TitledComponentFrame extends ComponentFrame {
    protected JPanel miscPanel;
    protected JButton hideButton;
    private JLabel frameTitleLabel;
    protected TitledComponentFrame(String title) {
        super(title);
        miscPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    }

    @Override
    protected void initialize() {
        super.initialize();
        initHeaderPanel();
    }

    private void initHeaderPanel(){
        if(layout instanceof BorderLayout) {
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(AppThemeColor.HEADER);
            headerPanel.setPreferredSize(new Dimension(100,26));
            headerPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.MSG_HEADER_BORDER));

            JLabel appIcon = componentsFactory.getIconLabel("app/app-icon.png", 16);
            frameTitleLabel = componentsFactory.getTextLabel(getFrameTitle());
            frameTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
            frameTitleLabel.setVerticalAlignment(SwingConstants.CENTER);
            frameTitleLabel.addMouseListener(new DraggedFrameMouseListener());
            frameTitleLabel.addMouseMotionListener(new DraggedFrameMotionListener());

            appIcon.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
            headerPanel.add(appIcon,BorderLayout.LINE_START);
            headerPanel.add(frameTitleLabel, BorderLayout.CENTER);

            miscPanel.setBackground(AppThemeColor.TRANSPARENT);
            hideButton = componentsFactory.getIconButton("app/close.png", 14, AppThemeColor.FRAME_ALPHA, "");
            hideButton.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
            hideButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(SwingUtilities.isLeftMouseButton(e)) {
                        TitledComponentFrame.this.setVisible(false);
                    }
                }
            });
            miscPanel.add(hideButton);
            headerPanel.add(miscPanel, BorderLayout.LINE_END);
            this.add(headerPanel, BorderLayout.PAGE_START);
        }
    }
    protected abstract String getFrameTitle();

    public void setFrameTitle(String title) {
        frameTitleLabel.setText(title);
    }
    protected void removeHideButton() {
        this.miscPanel.remove(hideButton);
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}

package com.mercury.platform.ui.frame.other;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.shared.pojo.ScaleData;
import com.mercury.platform.ui.misc.event.SaveScaleEvent;
import com.mercury.platform.ui.misc.event.ScaleChangeEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class SetUpScaleFrame extends OverlaidFrame {
    private Map<String,Float> scaleData;
    public SetUpScaleFrame() {
        super("MercuryTrade");
    }

    @Override
    protected void initialize() {
        this.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,2),
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1)));

        this.scaleData = ConfigManager.INSTANCE.getScaleData();

        JPanel rootPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(6,6,0,6));

        JPanel header = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        header.add(componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT, TextAlignment.LEFTOP,18f,"Scale settings"));

        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
        root.setBackground(AppThemeColor.SLIDE_BG);
        root.add(getScaleSettingsPanel(),BorderLayout.CENTER);

        JPanel miscPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        JButton cancel = componentsFactory.getBorderedButton("Cancel");
        cancel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER),
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT, 3)
        ));
        cancel.setBackground(AppThemeColor.FRAME);

        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                FramesManager.INSTANCE.disableScale();
            }
        });
        cancel.setPreferredSize(new Dimension(100, 26));

        JButton save = componentsFactory.getBorderedButton("Save");
        save.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                FramesManager.INSTANCE.disableScale();
                ConfigManager.INSTANCE.saveScaleData(scaleData);
                EventRouter.UI.fireEvent(new SaveScaleEvent(scaleData));
            }
        });
        save.setPreferredSize(new Dimension(100, 26));

        miscPanel.add(cancel);
        miscPanel.add(save);
        rootPanel.add(root,BorderLayout.CENTER);
        this.add(header,BorderLayout.PAGE_START);
        this.add(rootPanel,BorderLayout.CENTER);
        this.add(miscPanel,BorderLayout.PAGE_END);
        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/4-this.getSize().height/2);
    }

    private JPanel getScaleSettingsPanel(){
        JPanel root = componentsFactory.getTransparentPanel(new GridLayout(3,1));
        JLabel notificationLabel = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                "Notification panel: ");
        JSlider notificationSlider = componentsFactory.getSlider(9,15, (int) (scaleData.get("notification")*10));
        JLabel notificationValue = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                String.valueOf(notificationSlider.getValue() * 10) + "%");
        notificationValue.setBorder(null);
        notificationSlider.addChangeListener((event)-> repaint());
        notificationSlider.addMouseListener(new MouseAdapter() {
            private int prevValue = 10;
            @Override
            public void mouseReleased(MouseEvent e) {
                if(notificationSlider.getValue() != prevValue){
                    prevValue = notificationSlider.getValue();
                    notificationValue.setText(String.valueOf(notificationSlider.getValue() * 10)+ "%");
                    scaleData.put("notification",notificationSlider.getValue()/10f);
                    EventRouter.UI.fireEvent(new ScaleChangeEvent.NotificationScaleChangeEvent(notificationSlider.getValue()/10f));
                    repaint();
                }
            }
        });

        JLabel taskBarLabel = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                "Task panel: ");

        JSlider taskBarSlider = componentsFactory.getSlider(9,15, (int) (scaleData.get("taskbar")*10));
        JLabel taskBarValue = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                String.valueOf(taskBarSlider.getValue() * 10)+ "%");
        taskBarValue.setBorder(null);
        taskBarSlider.addChangeListener((event)-> repaint());
        taskBarSlider.addMouseListener(new MouseAdapter() {
            private int prevValue = 10;
            @Override
            public void mouseReleased(MouseEvent e) {
                if(taskBarSlider.getValue() != prevValue){
                    prevValue = taskBarSlider.getValue();
                    taskBarValue.setText(String.valueOf(taskBarSlider.getValue() * 10)+ "%");
                    scaleData.put("taskbar",taskBarSlider.getValue()/10f);
                    EventRouter.UI.fireEvent(new ScaleChangeEvent.TaskBarScaleChangeEvent(taskBarSlider.getValue()/10f));
                    repaint();
                }
            }
        });

        JLabel itemInfoLabel = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                "Item cell panel: ");
        JSlider itemInfoSlider = componentsFactory.getSlider(9,15, (int) (scaleData.get("itemcell") * 10));
        JLabel itemInfoValue = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                String.valueOf(itemInfoSlider.getValue() * 10)+ "%");
        itemInfoValue.setBorder(null);
        itemInfoSlider.addChangeListener((event)-> repaint());
        itemInfoSlider.addMouseListener(new MouseAdapter() {
            private int prevValue = 10;
            @Override
            public void mouseReleased(MouseEvent e) {
                if(itemInfoSlider.getValue() != prevValue){
                    prevValue = itemInfoSlider.getValue();
                    itemInfoValue.setText(String.valueOf(itemInfoSlider.getValue() * 10)+ "%");
                    scaleData.put("itemcell",itemInfoSlider.getValue()/10f);
                    EventRouter.UI.fireEvent(new ScaleChangeEvent.ItemPanelScaleChangeEvent(itemInfoSlider.getValue()/10f));
                    repaint();
                }
            }
        });

        root.add(componentsFactory.getSliderSettingsPanel(notificationLabel,notificationValue,notificationSlider));
        root.add(componentsFactory.getSliderSettingsPanel(taskBarLabel,taskBarValue,taskBarSlider));
        root.add(componentsFactory.getSliderSettingsPanel(itemInfoLabel,itemInfoValue,itemInfoSlider));
        return root;
    }

    @Override
    public void initHandlers() {

    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}

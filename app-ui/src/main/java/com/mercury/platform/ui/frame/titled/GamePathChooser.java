package com.mercury.platform.ui.frame.titled;

import com.mercury.platform.core.utils.FileMonitor;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class GamePathChooser extends TitledComponentFrame {
    private final Logger logger = LogManager.getLogger(GamePathChooser.class.getSimpleName());
    private JLabel statusLabel;
    private String gamePath;
    private boolean readyToStart = false;

    public GamePathChooser() {
        super("MercuryTrade");
        processingHideEvent = false;
        processEResize = false;
        processSEResize = false;
        setAlwaysOnTop(true);
    }

    @Override
    protected void initialize() {
        super.initialize();
        removeHideButton();
        this.add(getChooserPanel(),BorderLayout.CENTER);
        this.add(getMiscPanel(),BorderLayout.PAGE_END);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.pack();
        this.setVisible(true);
    }
    private JPanel getChooserPanel(){
        JPanel panel = componentsFactory.getTransparentPanel(new BorderLayout());

        statusLabel = componentsFactory.getTextLabel("");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(AppThemeColor.TEXT_IMPORTANT);
        panel.add(statusLabel,BorderLayout.CENTER);

        JPanel chooserPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField textField = componentsFactory.getTextField("Example: \"C:/Path of Exile\"");
        textField.setPreferredSize(new Dimension(450,26));
        textField.setMinimumSize(new Dimension(450,26));
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                repaint();
            }
        });

        chooserPanel.add(textField);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        JButton selectButton = componentsFactory.getBorderedButton("Select");
        selectButton.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(GamePathChooser.this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                gamePath = fileChooser.getSelectedFile().getPath();
                textField.setText(gamePath);
                repaint();
                pack();
            }
        });
        chooserPanel.add(selectButton);
        panel.add(chooserPanel,BorderLayout.PAGE_START);
        return panel;
    }

    private JPanel getMiscPanel(){
        JPanel miscPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = componentsFactory.getBorderedButton("Close");
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!readyToStart) {
                    System.exit(0);
                }
            }
        });
        JButton saveButton = componentsFactory.getBorderedButton("Save");
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!readyToStart) {
                    if (ConfigManager.INSTANCE.isValidGamePath(gamePath)) {
                        readyToStart = true;
                        statusLabel.setText("Success!");
                        saveButton.setEnabled(false);
                        closeButton.setEnabled(false);
                        statusLabel.setForeground(AppThemeColor.TEXT_SUCCESS);
                        pack();
                        repaint();
                        Timer timer = new Timer(1000, null);
                        timer.addActionListener(actionEvent -> {
                            timer.stop();
                            ConfigManager.INSTANCE.setGamePath(gamePath + File.separator);
                            new FileMonitor().start();
                            FramesManager.INSTANCE.start();
                            setVisible(false);
                        });
                        timer.start();
                        logger.info("UI module was started.");
                    } else {
                        statusLabel.setText("Wrong game path!");
                        pack();
                    }
                }
            }
        });
        miscPanel.add(saveButton);
        miscPanel.add(closeButton);
        return miscPanel;
    }


    @Override
    public void initHandlers() {

    }

    @Override
    protected String getFrameTitle() {
        return "Select game path (check task manager if not sure)";
    }
}

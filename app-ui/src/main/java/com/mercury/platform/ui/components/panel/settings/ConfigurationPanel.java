package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.ui.components.panel.TransparentPanel;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;

/**
 * Created by Константин on 05.01.2017.
 */
public abstract class ConfigurationPanel extends TransparentPanel implements HasUI{
    public abstract boolean processAndSave();

    public abstract void restore();
}

package com.mercury.platform;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.core.utils.FileMonitor;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.HideLoadingFrame;
import com.mercury.platform.ui.frame.other.MercuryLoadingFrame;
import com.mercury.platform.ui.frame.titled.GamePathChooser;
import com.mercury.platform.ui.manager.FramesManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppMain {
    private static final Logger logger = LogManager.getLogger(AppMain.class.getSimpleName());
    public static void main(String[] args) {
        MercuryLoadingFrame mercuryLoadingFrame = new MercuryLoadingFrame();
        mercuryLoadingFrame.init();
        mercuryLoadingFrame.showComponent();
        new AppStarter().startApplication();
        String gamePath = ConfigManager.INSTANCE.getGamePath();
        if(gamePath.equals("") || !ConfigManager.INSTANCE.isValidGamePath(gamePath)){
            EventRouter.CORE.fireEvent(new HideLoadingFrame());
            GamePathChooser gamePathChooser = new GamePathChooser();
            gamePathChooser.init();
        }else{
            new FileMonitor().start();
            FramesManager.INSTANCE.start();
            EventRouter.CORE.fireEvent(new HideLoadingFrame());
        }
    }
}
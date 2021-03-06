package com.mercury.platform.shared;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Created by Константин on 21.02.2017.
 */
public class UpdateManager {
    private final static Logger logger = LogManager.getLogger(UpdateManager.class.getSimpleName());
    private final static String LOCAL_UPDATER_PATH = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade\\local-updater.jar";
    private static final String JAR_FILE_PATH = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade\\temp\\MercuryTrade.jar";
    private static class UpdateManagerHolder {
        static final UpdateManager HOLDER_INSTANCE = new UpdateManager();
    }
    public static UpdateManager INSTANCE = UpdateManagerHolder.HOLDER_INSTANCE;

    public void doUpdate(){
        try {
            String path = StringUtils.substringAfter(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath(), "/");
            logger.debug("Execute local updater, source path: {}",path);
            if(new File(JAR_FILE_PATH).exists()) {
                Runtime.getRuntime().exec("java -jar " + LOCAL_UPDATER_PATH + " " + "\"" + path + "\"");
            }
        } catch (Exception e1) {
            logger.error("Error while execute local-updater: ", e1);
        }
    }
}

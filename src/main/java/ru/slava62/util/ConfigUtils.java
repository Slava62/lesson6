package ru.slava62.util;


import lombok.experimental.UtilityClass;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@UtilityClass
public class ConfigUtils {
    Properties prop = new Properties();
    private static InputStream configFile;

    static {
        try {
            configFile = new FileInputStream("src/test/resources/application.properties");
            prop.load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getBaseUrl() {return prop.getProperty("url");}
    public String getStand() {return prop.getProperty("stand");}

}

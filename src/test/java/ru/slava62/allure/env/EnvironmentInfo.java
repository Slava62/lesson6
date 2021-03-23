package ru.slava62.allure.env;

import lombok.experimental.UtilityClass;
import ru.slava62.util.ConfigUtils;
import ru.slava62.util.RetrofitUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@UtilityClass
public class EnvironmentInfo {

  public static void setAllureEnvironment() {

        Properties allureProperties=new Properties();
        allureProperties.put("URL", ConfigUtils.getBaseUrl());
        allureProperties.put("Stand", ConfigUtils.getStand());
      try {

          allureProperties.store(new FileOutputStream
                  ("target/allure-results/environment.properties")
                  ,"Current environment");
      } catch (IOException e) {
          e.printStackTrace();
      }

  }
}

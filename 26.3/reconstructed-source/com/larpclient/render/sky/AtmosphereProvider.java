package com.larpclient.render.sky;

import com.larpclient.LarpClient;

public interface AtmosphereProvider {
   default SkyboxTheme atmosphere() {
      return LarpClient.getInstance().getConfigManager().getPreferences().background;
   }

   default boolean dimAtmosphere() {
      return false;
   }
}

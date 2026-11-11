package com.larpclient.hud;

import com.larpclient.render.sky.SkyRenderContext;
import java.io.IOException;

public interface HudRenderer extends AutoCloseable {
   void render(SkyRenderContext var1) throws IOException;

   void reload();

   @Override
   void close();
}

package com.larpclient;

import java.io.IOException;

public interface d0 extends AutoCloseable {
   void render(pa var1) throws IOException;

   void reload();

   @Override
   void close();
}

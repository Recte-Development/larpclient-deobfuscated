package com.larpclient;

import java.io.IOException;

public interface ec extends AutoCloseable {
   void render(e_ var1) throws IOException;

   void reload();

   @Override
   void close();
}

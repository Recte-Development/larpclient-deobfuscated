package com.larpclient;

import java.io.IOException;

public interface gt extends AutoCloseable {
   void render(gx var1) throws IOException;

   void reload();

   @Override
   void close();
}

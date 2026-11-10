package com.larpclient;

import java.io.IOException;

public interface tp extends AutoCloseable {
   void render(wj var1) throws IOException;

   void reload();

   @Override
   void close();
}

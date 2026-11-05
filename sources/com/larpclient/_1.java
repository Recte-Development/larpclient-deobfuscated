package com.larpclient;

import java.io.IOException;

public interface _1 extends AutoCloseable {
   void render(lb var1) throws IOException;

   void reload();

   @Override
   void close();
}

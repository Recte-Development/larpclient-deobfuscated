package com.larpclient;

public final class ch {
   public static final String i = "Larp Client";
   public static final int l = 512;
   public static final int C = 96;
   public static final int x = 256;
   public static final int a = ("Larp Client".length() + 1) * 96;
   public static final int Q = a + 256;

   private ch() {
   }

   public static ch.x t(int var0, int var1) {
      int var2 = Math.max(12, Math.min(Math.min(104, var1 / 5), var0 / 3));
      int var3 = Math.max(1, Math.min(256, var0 - 32));
      int var4 = Math.max(1, var3 * 96 / 512);
      int var5 = Math.max(8, var2 / 6);
      int var6 = (var1 - var2 - var5 - var4) / 2;
      int var7 = Math.min(Math.min(var0, var1), var2 * 2);
      p1.e var8 = new p1.e((var0 - var2) / 2, var6, var2, var2);
      return new ch.x(var8, new p1.e((var0 - var3) / 2, var6 + var2 + var5, var3, var4), new p1.e((var0 - var7) / 2, var6 + (var2 - var7) / 2, var7, var7));
   }

   public record x(p1.e logo, p1.e title, p1.e glow) {
   }
}

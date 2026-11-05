package com.larpclient;

public final class c0 {
   public static final String E = "Larp Client";
   public static final int P = 512;
   public static final int e = 96;
   public static final int v = 256;
   public static final int F = ("Larp Client".length() + 1) * 96;
   public static final int h = F + 256;

   private c0() {
   }

   public static c0.t L(int var0, int var1) {
      int var2 = Math.max(12, Math.min(Math.min(104, var1 / 5), var0 / 3));
      int var3 = Math.max(1, Math.min(256, var0 - 32));
      int var4 = Math.max(1, var3 * 96 / 512);
      int var5 = Math.max(8, var2 / 6);
      int var6 = (var1 - var2 - var5 - var4) / 2;
      int var7 = Math.min(Math.min(var0, var1), var2 * 2);
      p5.w var8 = new p5.w((var0 - var2) / 2, var6, var2, var2);
      return new c0.t(var8, new p5.w((var0 - var3) / 2, var6 + var2 + var5, var3, var4), new p5.w((var0 - var7) / 2, var6 + (var2 - var7) / 2, var7, var7));
   }

   public record t(p5.w logo, p5.w title, p5.w glow) {
   }
}

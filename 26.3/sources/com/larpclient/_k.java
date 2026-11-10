package com.larpclient;

import com.larpclient.module.HUDModule;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class _k {
   public static final int v = 21;
   public static final int q = 4;
   public static final int P = 13;
   public static final int a = 4;
   public static final int A = 10;
   public static final int r = 7;
   public static final double Z = 34.0;
   public static final double c = 12.0;
   public static final long p = 90000000L;
   private static final Map<String, Float> w = new HashMap<>();
   private static final _k.d T = new _k.d();
   private static final _k.d V = new _k.d();
   private static Object C;
   private static long g;
   private static long d;
   private static long Q;

   private _k() {
   }

   public static boolean o() {
      HUDModule var0 = HUDModule.w();
      return var0 != null && var0.z("keystrokes");
   }

   public static void U(int var0) {
      if (o() && Minecraft.getInstance().gui.screen() == null && var0 <= 1 && var0 >= 0) {
         (var0 == 0 ? T : V).U(System.nanoTime());
      }
   }

   public static void y() {
      if (o()) {
         long var0 = System.nanoTime();
         T.U(var0);
         d = var0;
      }
   }

   public static void A() {
      if (o()) {
         Q = System.nanoTime();
      }
   }

   public static boolean T(long var0, long var2) {
      return var2 != 0L && var0 - var2 >= 0L && var0 - var2 < 90000000L;
   }

   public static int J(boolean var0) {
      return (var0 ? T : V).v(System.nanoTime());
   }

   public static int h(HUDModule var0) {
      return Z(var0) * 2 + 63 + 8;
   }

   public static int b(HUDModule var0) {
      int var1 = Z(var0) * 2 + 42 + 4;
      if (var0.k.getValue()) {
         var1 += 25;
      }

      if (var0.b.getValue()) {
         var1 += 17;
      }

      return var1;
   }

   private static int Z(HUDModule var0) {
      return var0.L.getValue().intValue();
   }

   public static float B(float var0, boolean var1, double var2) {
      double var4 = 1.0 - Math.exp(-(var1 ? 34.0 : 12.0) * Math.clamp(var2, 0.0, 0.1));
      return (float)(var0 + ((var1 ? 1 : 0) - var0) * var4);
   }

   public static void j(GuiGraphicsExtractor var0, HUDModule var1, int var2, int var3, boolean var4) {
      Minecraft var5 = Minecraft.getInstance();
      long var6 = System.nanoTime();
      double var8 = g == 0L ? 0.016666666666666666 : (var6 - g) / 1.0E9;
      g = var6;
      if (C != var5.level) {
         w.clear();
         T.y();
         V.y();
         Q = 0L;
         d = 0L;
         C = var5.level;
      }

      int var10 = h(var1);
      int var11 = b(var1);
      h5.q(var0, var2, var3, var10, var11, 8.0F, vv.N(-16119284, var1.M.getValue() / 255.0));
      nc.X(var0, var2, var3, var10, var11, 8.0F);
      nc.e(var0, var2, var3, var10, var11, 8);
      Options var12 = var5.options;
      boolean var13 = var1.w.getValue();
      int var14 = var2 + Z(var1);
      int var15 = var3 + Z(var1);
      byte var16 = 25;
      int var17 = X(var12.keyUp, var4) && !T(var6, Q) ? 1 : 0;
      u(var0, var13 ? "↑" : "W", null, (boolean)var17, var14 + var16, var15, 21, 21, var8);
      var15 += var16;
      u(var0, var13 ? "←" : "A", null, X(var12.keyLeft, var4), var14, var15, 21, 21, var8);
      u(var0, var13 ? "↓" : "S", null, X(var12.keyDown, var4), var14 + var16, var15, 21, 21, var8);
      u(var0, var13 ? "→" : "D", null, X(var12.keyRight, var4), var14 + var16 * 2, var15, 21, 21, var8);
      var15 += var16;
      byte var18 = 71;
      if (var1.k.getValue()) {
         int var19 = (var18 - 4) / 2;
         boolean var20 = var1.o.getValue();
         int var21 = !X(var12.keyAttack, var4) && (var4 || !T(var6, d)) ? 0 : 1;
         u(var0, "LMB", var20 ? J(true) + " CPS" : null, (boolean)var21, var14, var15, var19, 21, var8);
         u(var0, "RMB", var20 ? J(false) + " CPS" : null, X(var12.keyUse, var4), var14 + var18 - var19, var15, var19, 21, var8);
         var15 += var16;
      }

      if (var1.b.getValue()) {
         float var24 = w("Space", X(var12.keyJump, var4), var8);
         G(var0, var14, var15, var18, 13, var24);
         int var25 = Math.round(var18 * 0.3F);
         h5.q(var0, var14 + (var18 - var25) / 2, var15 + 6 - 1, var25, 2.0F, 1.0F, vv.Q(-7697772, -1, var24));
      }
   }

   private static boolean X(KeyMapping var0, boolean var1) {
      return !var1 && var0.isDown();
   }

   private static float w(String var0, boolean var1, double var2) {
      float var4 = B(w.getOrDefault(var0, 0.0F), var1, var2);
      w.put(var0, var4);
      return var4;
   }

   private static void G(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, float var5) {
      h5.q(var0, var1, var2, var3, var4, 4.0F, vv.N(-1, 0.055 * (1.0F - var5)));
      if (!(var5 <= 0.01F)) {
         w1.h(var0, var1, var2, var3, var4, 4.0F, 6.0F * var5, vv.O(com.larpclient.c.y, 0.45 * var5), vv.O(com.larpclient.c.y, 0.45 * var5));
         h5.q(var0, var1, var2, var3, var4, 4.0F, vv.O(com.larpclient.c.y, 0.9 * var5));
      }
   }

   private static void u(GuiGraphicsExtractor var0, String var1, String var2, boolean var3, int var4, int var5, int var6, int var7, double var8) {
      float var10 = w(var1, var3, var8);
      G(var0, var4, var5, var6, var7, var10);
      int var11 = vv.Q(com.larpclient.c.W, -1, var10);
      double var12 = var4 + var6 / 2.0;
      if (var2 == null) {
         i5.i(var0, var1, var12 - i5.l(var1, 10.0) / 2.0, n8.b(var5, var7, 10.0), 10.0, var11);
      } else {
         double var14 = var5 + var7 / 2.0 - 6.7640915F;
         i5.i(var0, var1, var12 - i5.l(var1, 10.0) / 2.0, var14, 10.0, var11);
         i5.i(var0, var2, var12 - i5.l(var2, 7.0) / 2.0, var14 + 10.0, 7.0, vv.Q(-7697772, -1, var10));
      }
   }

   public static final class d {
      public static final long x = 1000000000L;
      private final ArrayDeque<Long> P = new ArrayDeque<>();

      public void U(long var1) {
         this.P.addLast(var1);
         this.E(var1);
      }

      public int v(long var1) {
         this.E(var1);
         return this.P.size();
      }

      public void y() {
         this.P.clear();
      }

      private void E(long var1) {
         while (!this.P.isEmpty() && var1 - this.P.peekFirst() > 1000000000L) {
            this.P.removeFirst();
         }
      }
   }
}

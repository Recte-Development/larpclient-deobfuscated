package com.larpclient;

import com.larpclient.module.HUDModule;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class xc {
   public static final int T = 21;
   public static final int R = 4;
   public static final int K = 13;
   public static final int d = 4;
   public static final int B = 10;
   public static final int W = 7;
   public static final double n = 34.0;
   public static final double P = 12.0;
   private static final Map<String, Float> m = new HashMap<>();
   private static final xc.k X = new xc.k();
   private static final xc.k r = new xc.k();
   private static Object F;
   private static long z;

   private xc() {
   }

   public static boolean G() {
      HUDModule var0 = HUDModule.f();
      return var0 != null && var0.D("keystrokes");
   }

   public static void f(int var0) {
      if (G() && Minecraft.getInstance().gui.screen() == null && var0 <= 1 && var0 >= 0) {
         (var0 == 0 ? X : r).U(System.nanoTime());
      }
   }

   public static int z(boolean var0) {
      return (var0 ? X : r).X(System.nanoTime());
   }

   public static int E(HUDModule var0) {
      return d(var0) * 2 + 63 + 8;
   }

   public static int y(HUDModule var0) {
      int var1 = d(var0) * 2 + 42 + 4;
      if (var0.O.getValue()) {
         var1 += 25;
      }

      if (var0.W.getValue()) {
         var1 += 17;
      }

      return var1;
   }

   private static int d(HUDModule var0) {
      return var0.Z.getValue().intValue();
   }

   public static float r(float var0, boolean var1, double var2) {
      double var4 = 1.0 - Math.exp(-(var1 ? 34.0 : 12.0) * Math.clamp(var2, 0.0, 0.1));
      return (float)(var0 + ((var1 ? 1 : 0) - var0) * var4);
   }

   public static void n(GuiGraphicsExtractor var0, HUDModule var1, int var2, int var3, boolean var4) {
      Minecraft var5 = Minecraft.getInstance();
      long var6 = System.nanoTime();
      double var8 = z == 0L ? 0.016666666666666666 : (var6 - z) / 1.0E9;
      z = var6;
      if (F != var5.level) {
         m.clear();
         X.M();
         r.M();
         F = var5.level;
      }

      int var10 = E(var1);
      int var11 = y(var1);
      fr.S(var0, var2, var3, var10, var11, 8.0F, oh.i(-16119284, var1.P.getValue() / 255.0));
      m0.V(var0, var2, var3, var10, var11, 8.0F);
      m0.g(var0, var2, var3, var10, var11, 8);
      Options var12 = var5.options;
      boolean var13 = var1.g.getValue();
      int var14 = var2 + d(var1);
      int var15 = var3 + d(var1);
      byte var16 = 25;
      e(var0, var13 ? "↑" : "W", null, m(var12.keyUp, var4), var14 + var16, var15, 21, 21, var8);
      var15 += var16;
      e(var0, var13 ? "←" : "A", null, m(var12.keyLeft, var4), var14, var15, 21, 21, var8);
      e(var0, var13 ? "↓" : "S", null, m(var12.keyDown, var4), var14 + var16, var15, 21, 21, var8);
      e(var0, var13 ? "→" : "D", null, m(var12.keyRight, var4), var14 + var16 * 2, var15, 21, 21, var8);
      var15 += var16;
      byte var17 = 71;
      if (var1.O.getValue()) {
         int var18 = (var17 - 4) / 2;
         boolean var19 = var1.m.getValue();
         e(var0, "LMB", var19 ? z(true) + " CPS" : null, m(var12.keyAttack, var4), var14, var15, var18, 21, var8);
         e(var0, "RMB", var19 ? z(false) + " CPS" : null, m(var12.keyUse, var4), var14 + var17 - var18, var15, var18, 21, var8);
         var15 += var16;
      }

      if (var1.W.getValue()) {
         float var22 = U("Space", m(var12.keyJump, var4), var8);
         C(var0, var14, var15, var17, 13, var22);
         int var23 = Math.round(var17 * 0.3F);
         fr.S(var0, var14 + (var17 - var23) / 2, var15 + 6 - 1, var23, 2.0F, 1.0F, oh.j(-7697772, -1, var22));
      }
   }

   private static boolean m(KeyMapping var0, boolean var1) {
      return !var1 && var0.isDown();
   }

   private static float U(String var0, boolean var1, double var2) {
      float var4 = r(m.getOrDefault(var0, 0.0F), var1, var2);
      m.put(var0, var4);
      return var4;
   }

   private static void C(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, float var5) {
      fr.S(var0, var1, var2, var3, var4, 4.0F, oh.i(-1, 0.055 * (1.0F - var5)));
      if (!(var5 <= 0.01F)) {
         ow.A(var0, var1, var2, var3, var4, 4.0F, 6.0F * var5, oh.S(rv.e, 0.45 * var5), oh.S(rv.e, 0.45 * var5));
         fr.S(var0, var1, var2, var3, var4, 4.0F, oh.S(rv.e, 0.9 * var5));
      }
   }

   private static void e(GuiGraphicsExtractor var0, String var1, String var2, boolean var3, int var4, int var5, int var6, int var7, double var8) {
      float var10 = U(var1, var3, var8);
      C(var0, var4, var5, var6, var7, var10);
      int var11 = oh.j(rv.G0, -1, var10);
      double var12 = var4 + var6 / 2.0;
      if (var2 == null) {
         rd.v(var0, var1, var12 - rd.H(var1, 10.0) / 2.0, rz.W(var5, var7, 10.0), 10.0, var11);
      } else {
         double var14 = var5 + var7 / 2.0 - 6.7640915F;
         rd.v(var0, var1, var12 - rd.H(var1, 10.0) / 2.0, var14, 10.0, var11);
         rd.v(var0, var2, var12 - rd.H(var2, 7.0) / 2.0, var14 + 10.0, 7.0, oh.j(-7697772, -1, var10));
      }
   }

   public static final class k {
      public static final long g = 1000000000L;
      private final ArrayDeque<Long> S = new ArrayDeque<>();

      public void U(long var1) {
         this.S.addLast(var1);
         this.D(var1);
      }

      public int X(long var1) {
         this.D(var1);
         return this.S.size();
      }

      public void M() {
         this.S.clear();
      }

      private void D(long var1) {
         while (!this.S.isEmpty() && var1 - this.S.peekFirst() > 1000000000L) {
            this.S.removeFirst();
         }
      }
   }
}

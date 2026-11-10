package com.larpclient;

import com.larpclient.module.HUDModule;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class to {
   public static final int z = 21;
   public static final int Q = 4;
   public static final int D = 13;
   public static final int b = 4;
   public static final int B = 10;
   public static final int E = 7;
   public static final double t = 34.0;
   public static final double g = 12.0;
   public static final long m = 90000000L;
   private static final Map<String, Float> a = new HashMap<>();
   private static final to.i l = new to.i();
   private static final to.i K = new to.i();
   private static Object f;
   private static long R;
   private static long J;
   private static long S;

   private to() {
   }

   public static boolean N() {
      HUDModule var0 = HUDModule.K();
      return var0 != null && var0.g("keystrokes");
   }

   public static void m(int var0) {
      if (N() && Minecraft.getInstance().gui.screen() == null && var0 <= 1 && var0 >= 0) {
         (var0 == 0 ? l : K).v(System.nanoTime());
      }
   }

   public static void B() {
      if (N()) {
         long var0 = System.nanoTime();
         l.v(var0);
         J = var0;
      }
   }

   public static void j() {
      if (N()) {
         S = System.nanoTime();
      }
   }

   public static boolean z(long var0, long var2) {
      return var2 != 0L && var0 - var2 >= 0L && var0 - var2 < 90000000L;
   }

   public static int g(boolean var0) {
      return (var0 ? l : K).c(System.nanoTime());
   }

   public static int q(HUDModule var0) {
      return i(var0) * 2 + 63 + 8;
   }

   public static int v(HUDModule var0) {
      int var1 = i(var0) * 2 + 42 + 4;
      if (var0.p.getValue()) {
         var1 += 25;
      }

      if (var0.L.getValue()) {
         var1 += 17;
      }

      return var1;
   }

   private static int i(HUDModule var0) {
      return var0.K.getValue().intValue();
   }

   public static float s(float var0, boolean var1, double var2) {
      double var4 = 1.0 - Math.exp(-(var1 ? 34.0 : 12.0) * Math.clamp(var2, 0.0, 0.1));
      return (float)(var0 + ((var1 ? 1 : 0) - var0) * var4);
   }

   public static void Y(GuiGraphicsExtractor var0, HUDModule var1, int var2, int var3, boolean var4) {
      Minecraft var5 = Minecraft.getInstance();
      long var6 = System.nanoTime();
      double var8 = R == 0L ? 0.016666666666666666 : (var6 - R) / 1.0E9;
      R = var6;
      if (f != var5.level) {
         a.clear();
         l.T();
         K.T();
         S = 0L;
         J = 0L;
         f = var5.level;
      }

      int var10 = q(var1);
      int var11 = v(var1);
      kv.f(var0, var2, var3, var10, var11, 8.0F, uh.H(-16119284, var1.T.getValue() / 255.0));
      u9.n(var0, var2, var3, var10, var11, 8.0F);
      u9.M(var0, var2, var3, var10, var11, 8);
      Options var12 = var5.options;
      boolean var13 = var1.x.getValue();
      int var14 = var2 + i(var1);
      int var15 = var3 + i(var1);
      byte var16 = 25;
      int var17 = X(var12.keyUp, var4) && !z(var6, S) ? 1 : 0;
      p(var0, var13 ? "↑" : "W", null, (boolean)var17, var14 + var16, var15, 21, 21, var8);
      var15 += var16;
      p(var0, var13 ? "←" : "A", null, X(var12.keyLeft, var4), var14, var15, 21, 21, var8);
      p(var0, var13 ? "↓" : "S", null, X(var12.keyDown, var4), var14 + var16, var15, 21, 21, var8);
      p(var0, var13 ? "→" : "D", null, X(var12.keyRight, var4), var14 + var16 * 2, var15, 21, 21, var8);
      var15 += var16;
      byte var18 = 71;
      if (var1.p.getValue()) {
         int var19 = (var18 - 4) / 2;
         boolean var20 = var1.b.getValue();
         int var21 = !X(var12.keyAttack, var4) && (var4 || !z(var6, J)) ? 0 : 1;
         p(var0, "LMB", var20 ? g(true) + " CPS" : null, (boolean)var21, var14, var15, var19, 21, var8);
         p(var0, "RMB", var20 ? g(false) + " CPS" : null, X(var12.keyUse, var4), var14 + var18 - var19, var15, var19, 21, var8);
         var15 += var16;
      }

      if (var1.L.getValue()) {
         float var24 = V("Space", X(var12.keyJump, var4), var8);
         y(var0, var14, var15, var18, 13, var24);
         int var25 = Math.round(var18 * 0.3F);
         kv.f(var0, var14 + (var18 - var25) / 2, var15 + 6 - 1, var25, 2.0F, 1.0F, uh.q(-7697772, -1, var24));
      }
   }

   private static boolean X(KeyMapping var0, boolean var1) {
      return !var1 && var0.isDown();
   }

   private static float V(String var0, boolean var1, double var2) {
      float var4 = s(a.getOrDefault(var0, 0.0F), var1, var2);
      a.put(var0, var4);
      return var4;
   }

   private static void y(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, float var5) {
      kv.f(var0, var1, var2, var3, var4, 4.0F, uh.H(-1, 0.055 * (1.0F - var5)));
      if (!(var5 <= 0.01F)) {
         tb.Q(var0, var1, var2, var3, var4, 4.0F, 6.0F * var5, uh.F(ho.J, 0.45 * var5), uh.F(ho.J, 0.45 * var5));
         kv.f(var0, var1, var2, var3, var4, 4.0F, uh.F(ho.J, 0.9 * var5));
      }
   }

   private static void p(GuiGraphicsExtractor var0, String var1, String var2, boolean var3, int var4, int var5, int var6, int var7, double var8) {
      float var10 = V(var1, var3, var8);
      y(var0, var4, var5, var6, var7, var10);
      int var11 = uh.q(ho.Z, -1, var10);
      double var12 = var4 + var6 / 2.0;
      if (var2 == null) {
         gw.S(var0, var1, var12 - gw.m(var1, 10.0) / 2.0, c2.X(var5, var7, 10.0), 10.0, var11);
      } else {
         double var14 = var5 + var7 / 2.0 - 6.7640915F;
         gw.S(var0, var1, var12 - gw.m(var1, 10.0) / 2.0, var14, 10.0, var11);
         gw.S(var0, var2, var12 - gw.m(var2, 7.0) / 2.0, var14 + 10.0, 7.0, uh.q(-7697772, -1, var10));
      }
   }

   public static final class i {
      public static final long f = 1000000000L;
      private final ArrayDeque<Long> k = new ArrayDeque<>();

      public void v(long var1) {
         this.k.addLast(var1);
         this.H(var1);
      }

      public int c(long var1) {
         this.H(var1);
         return this.k.size();
      }

      public void T() {
         this.k.clear();
      }

      private void H(long var1) {
         while (!this.k.isEmpty() && var1 - this.k.peekFirst() > 1000000000L) {
            this.k.removeFirst();
         }
      }
   }
}

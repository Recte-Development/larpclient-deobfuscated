package com.larpclient;

import com.larpclient.module.HUDModule;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class kp {
   public static final int c = 21;
   public static final int W = 4;
   public static final int q = 13;
   public static final int E = 4;
   public static final int Z = 10;
   public static final int s = 7;
   public static final double P = 34.0;
   public static final double Y = 12.0;
   private static final Map<String, Float> S = new HashMap<>();
   private static final kp.q C = new kp.q();
   private static final kp.q t = new kp.q();
   private static Object g;
   private static long Q;

   private kp() {
   }

   public static boolean n() {
      HUDModule var0 = HUDModule.U();
      return var0 != null && var0.e("keystrokes");
   }

   public static void j(int var0) {
      if (n() && Minecraft.getInstance().gui.screen() == null && var0 <= 1 && var0 >= 0) {
         (var0 == 0 ? C : t).e(System.nanoTime());
      }
   }

   public static int i(boolean var0) {
      return (var0 ? C : t).k(System.nanoTime());
   }

   public static int Y(HUDModule var0) {
      return T(var0) * 2 + 63 + 8;
   }

   public static int h(HUDModule var0) {
      int var1 = T(var0) * 2 + 42 + 4;
      if (var0.e.P()) {
         var1 += 25;
      }

      if (var0.z.P()) {
         var1 += 17;
      }

      return var1;
   }

   private static int T(HUDModule var0) {
      return var0.o.P().intValue();
   }

   public static float t(float var0, boolean var1, double var2) {
      double var4 = 1.0 - Math.exp(-(var1 ? 34.0 : 12.0) * Math.clamp(var2, 0.0, 0.1));
      return (float)(var0 + ((var1 ? 1 : 0) - var0) * var4);
   }

   public static void J(GuiGraphicsExtractor var0, HUDModule var1, int var2, int var3, boolean var4) {
      Minecraft var5 = Minecraft.getInstance();
      long var6 = System.nanoTime();
      double var8 = Q == 0L ? 0.016666666666666666 : (var6 - Q) / 1.0E9;
      Q = var6;
      if (g != var5.level) {
         S.clear();
         C.T();
         t.T();
         g = var5.level;
      }

      int var10 = Y(var1);
      int var11 = h(var1);
      c7.n(var0, var2, var3, var10, var11, 8.0F, _3.A(-16119284, var1.i.P() / 255.0));
      mn.b(var0, var2, var3, var10, var11, 8.0F);
      mn.X(var0, var2, var3, var10, var11, 8);
      Options var12 = var5.options;
      boolean var13 = var1.c.P();
      int var14 = var2 + T(var1);
      int var15 = var3 + T(var1);
      byte var16 = 25;
      v(var0, var13 ? "↑" : "W", null, h(var12.keyUp, var4), var14 + var16, var15, 21, 21, var8);
      var15 += var16;
      v(var0, var13 ? "←" : "A", null, h(var12.keyLeft, var4), var14, var15, 21, 21, var8);
      v(var0, var13 ? "↓" : "S", null, h(var12.keyDown, var4), var14 + var16, var15, 21, 21, var8);
      v(var0, var13 ? "→" : "D", null, h(var12.keyRight, var4), var14 + var16 * 2, var15, 21, 21, var8);
      var15 += var16;
      byte var17 = 71;
      if (var1.e.P()) {
         int var18 = (var17 - 4) / 2;
         boolean var19 = var1.Y.P();
         v(var0, "LMB", var19 ? i(true) + " CPS" : null, h(var12.keyAttack, var4), var14, var15, var18, 21, var8);
         v(var0, "RMB", var19 ? i(false) + " CPS" : null, h(var12.keyUse, var4), var14 + var17 - var18, var15, var18, 21, var8);
         var15 += var16;
      }

      if (var1.z.P()) {
         float var22 = A("Space", h(var12.keyJump, var4), var8);
         x(var0, var14, var15, var17, 13, var22);
         int var23 = Math.round(var17 * 0.3F);
         c7.n(var0, var14 + (var17 - var23) / 2, var15 + 6 - 1, var23, 2.0F, 1.0F, _3.D(-7697772, -1, var22));
      }
   }

   private static boolean h(KeyMapping var0, boolean var1) {
      return !var1 && var0.isDown();
   }

   private static float A(String var0, boolean var1, double var2) {
      float var4 = t(S.getOrDefault(var0, 0.0F), var1, var2);
      S.put(var0, var4);
      return var4;
   }

   private static void x(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, float var5) {
      c7.n(var0, var1, var2, var3, var4, 4.0F, _3.A(-1, 0.055 * (1.0F - var5)));
      if (!(var5 <= 0.01F)) {
         tl.j(var0, var1, var2, var3, var4, 4.0F, 6.0F * var5, _3.U(f.C, 0.45 * var5), _3.U(f.C, 0.45 * var5));
         c7.n(var0, var1, var2, var3, var4, 4.0F, _3.U(f.C, 0.9 * var5));
      }
   }

   private static void v(GuiGraphicsExtractor var0, String var1, String var2, boolean var3, int var4, int var5, int var6, int var7, double var8) {
      float var10 = A(var1, var3, var8);
      x(var0, var4, var5, var6, var7, var10);
      int var11 = _3.D(f.s, -1, var10);
      double var12 = var4 + var6 / 2.0;
      if (var2 == null) {
         we.F(var0, var1, var12 - we.V(var1, 10.0) / 2.0, ew.N(var5, var7, 10.0), 10.0, var11);
      } else {
         double var14 = var5 + var7 / 2.0 - 6.7640915F;
         we.F(var0, var1, var12 - we.V(var1, 10.0) / 2.0, var14, 10.0, var11);
         we.F(var0, var2, var12 - we.V(var2, 7.0) / 2.0, var14 + 10.0, 7.0, _3.D(-7697772, -1, var10));
      }
   }

   public static final class q {
      public static final long h = 1000000000L;
      private final ArrayDeque<Long> I = new ArrayDeque<>();

      public void e(long var1) {
         this.I.addLast(var1);
         this.n(var1);
      }

      public int k(long var1) {
         this.n(var1);
         return this.I.size();
      }

      public void T() {
         this.I.clear();
      }

      private void n(long var1) {
         while (!this.I.isEmpty() && var1 - this.I.peekFirst() > 1000000000L) {
            this.I.removeFirst();
         }
      }
   }
}

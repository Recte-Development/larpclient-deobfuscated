package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.b5;
import com.larpclient.cv;
import com.larpclient.ez;
import com.larpclient.f6;
import com.larpclient.i_;
import com.larpclient.k;
import com.larpclient.q8;
import com.larpclient.tj;
import com.larpclient.wi;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_408;

public final class CustomCrosshairModule extends Module {
   public static final String a = "Tung Tung";
   private static final Map<String, String> V = Map.of("6-7", "67", "Custom Image", "Tung Tung");
   private static CustomCrosshairModule Y;
   private final li X = this.addSetting(
      new li("Style", "Crosshair geometry", "Star of David", Stream.concat(f6.Y.stream(), Stream.of("Tung Tung")).toArray(String[]::new)) {
         @Override
         public boolean acceptsValue(String var1) {
            return CustomCrosshairModule.V.containsKey(var1) || super.acceptsValue(var1);
         }

         @Override
         public void setValue(String var1) {
            super.setValue(CustomCrosshairModule.V.getOrDefault(var1, var1));
         }
      }
   );
   private final aj S = this.addSetting(new aj("Drawing", "Cells of the drawn crosshair", i_.y.q()));
   private final lo f = this.addSetting(new lo("Size", "Shape radius; half the image width", 7.0, 2.0, 24.0, 1.0));
   private final lo x = this.addSetting(new lo("Thickness", "Line width", 1.0, 1.0, 5.0, 1.0));
   private final l3 m = this.addSetting(new l3("Theme Color", "Follow the interface accent color", true));
   private final l0 b = this.addSetting(new l0("Color", "Crosshair color and opacity", -16737281));
   private final l3 C = this.addSetting(new l3("Outline", "Thin dark contour for bright and dark scenery", false));
   private final l3 I = this.addSetting(new l3("Spin", "Continuously rotate the crosshair", true));
   private final lo E = this.addSetting(new lo("Spin Speed", "Degrees per second", 180.0, 30.0, 720.0, 15.0));
   private final l3 d = this.addSetting(new l3("Click Spin", "Turn a quarter on each left click", false));
   private final lo p = this.addSetting(new lo("Click Spin Time", "Milliseconds per quarter turn", 350.0, 100.0, 1000.0, 25.0));
   private double G;
   private long P;
   private double i;
   private double t;
   private double F;
   private long M;
   private com.larpclient.g.l s;
   private String K;
   private i_ k = i_.y;

   public CustomCrosshairModule() {
      super("CustomCrosshair", "Customize your crosshair", Category.RENDER, -1);
      Y = this;
      this.E.O(this.I::getValue);
      this.b.O(() -> !this.m.getValue());
      this.p.O(this.d::getValue);
      this.x.O(() -> f6.b(this.X.getValue()));
      this.C.O(() -> !this.X.G("Tung Tung"));
      this.S.O(() -> false);
      this.X.a(var0 -> q8.G("Crosshair", "Style selected: {}", var0));
      this.I.a(var0 -> q8.G("Crosshair", "Spin {}", var0 ? "enabled" : "disabled"));
      this.d.a(var1 -> {
         this.T();
         q8.G("Crosshair", "Click Spin {}", var1 ? "enabled" : "disabled");
      });
      HudElementRegistry.addLast(
         class_2960.method_60655("larp-client", "customcrosshair"),
         (var1, var2) -> {
            class_310 var3 = getMc();
            if (this.isOperational()
               && this.isInGame()
               && !var3.field_1690.field_1842
               && var3.field_1690.method_31044().method_31034()
               && (var3.field_1755 == null || var3.field_1755.getClass() == class_408.class || var3.field_1755 instanceof tj var4 && var4.N())) {
               var1 = wi.p(var1, false, false);
               if (var1 != null) {
                  com.larpclient.k.U(var1);
                  k.l var10 = com.larpclient.k.m("crosshair");
                  if (!var10.q()) {
                     var10.O(var3.method_22683().method_4486(), var3.method_22683().method_4502());
                     var10.e(var1, var10.l(var3.method_22683().method_4486()), var10.M(var3.method_22683().method_4502()));

                     try {
                        this.c(var1, var10.l(var3.method_22683().method_4486()) + var10.L / 2, var10.M(var3.method_22683().method_4502()) + var10.F / 2);
                     } finally {
                        var10.o(var1);
                     }
                  }
               }
            }
         }
      );
   }

   public static void J(class_332 var0, int var1, int var2) {
      if (Y != null) {
         Y.n(var0, var1, var2, true);
      }
   }

   public static int X() {
      return Y == null ? 10 : Y.I(Y.X.G("Tung Tung") ? Y.s : null);
   }

   private int I(com.larpclient.g.l var1) {
      int var2 = this.f.getValue().intValue();
      return var1 != null
         ? b5.F(var1, var2, this.I.getValue() || this.d.getValue())
         : f6.e(this.X.getValue(), var2, this.x.getValue().intValue(), this.C.getValue());
   }

   public List<lb<?>> W() {
      return List.of(this.X, this.S, this.f, this.x);
   }

   public List<lb<?>> H() {
      return List.of(this.m, this.b, this.C, this.I, this.E, this.d, this.p);
   }

   public boolean Z() {
      return this.X.G("Tung Tung");
   }

   public boolean s() {
      return this.X.G("Draw Your Own");
   }

   public i_ o() {
      String var1 = this.S.getValue();
      if (!var1.equals(this.K)) {
         this.K = var1;

         try {
            this.k = i_.E(var1);
         } catch (IllegalArgumentException var3) {
            this.k = i_.y;
            q8.L("Crosshair", "Saved drawing is unreadable ({}); showing the default until it is edited", var3.getMessage());
         }
      }

      return this.k;
   }

   public void k(int var1, int var2, boolean var3) {
      i_ var4 = this.o().u(var1, var2, var3);
      if (var4 != this.k) {
         this.S.Y(var4.q());
      }
   }

   public void M() {
      this.S.Y(i_.p.q());
      q8.G("Crosshair", "Drawing cleared");
   }

   public static void H(JsonObject var0) {
      if (!var0.has("Theme Color") && var0.has("Color")) {
         try {
            int var1 = (int)((long)var0.get("Color").getAsDouble());
            var0.addProperty("Theme Color", var1 == -1381654 || var1 == ez.a);
         } catch (RuntimeException var2) {
         }
      }
   }

   private int s() {
      return this.m.getValue() ? ez.a | 0xFF000000 : this.b.getValue();
   }

   public int A() {
      return this.s() | 0xFF000000;
   }

   @Override
   public void onEnable() {
      this.G = 0.0;
      this.T();
   }

   @Override
   public void onDisable() {
      this.G = 0.0;
      this.T();
      this.s = null;
      com.larpclient.g.E();
   }

   public static void X(int var0) {
      if (Y != null && Y.isEnabled()) {
         CustomCrosshairModule var1 = Y;
         if (var0 == 0 && var1 != null && var1.isOperational() && var1.d.getValue() && getMc().field_1724 != null && getMc().field_1755 == null) {
            var1.J(System.nanoTime());
         }
      }
   }

   private void T() {
      this.i = this.t = this.F = 0.0;
      this.M = 0L;
   }

   private void J(long var1) {
      double var3 = this.p.getValue() / 1000.0;
      double var5 = this.M == 0L ? 1.0 : Math.clamp((var1 - this.M) / 1.0E9 / var3, 0.0, 1.0);
      double var7 = this.q(var5);
      double var9 = this.I(var5);
      double var11 = Math.floor(var7 / (Math.PI * 2)) * 2.0 * Math.PI;
      this.i = var7 - var11;
      this.t = this.t - var11 + (Math.PI / 2);
      this.F = var9;
      this.M = var1;
   }

   private double q(double var1) {
      double var3 = this.p.getValue() / 1000.0;
      double var5 = var1 * var1;
      double var7 = var5 * var1;
      return (2.0 * var7 - 3.0 * var5 + 1.0) * this.i + (var7 - 2.0 * var5 + var1) * this.F * var3 + (3.0 * var5 - 2.0 * var7) * this.t;
   }

   private double I(double var1) {
      double var3 = this.p.getValue() / 1000.0;
      double var5 = var1 * var1;
      return ((6.0 * var5 - 6.0 * var1) * this.i + (3.0 * var5 - 4.0 * var1 + 1.0) * this.F * var3 + (6.0 * var1 - 6.0 * var5) * this.t) / var3;
   }

   private double J() {
      if (this.d.getValue() && this.M != 0L) {
         double var1 = Math.clamp((System.nanoTime() - this.M) / 1.0E9 / (this.p.getValue() / 1000.0), 0.0, 1.0);
         return this.q(var1);
      } else {
         return this.t;
      }
   }

   private double s() {
      long var1 = System.nanoTime();
      if (!this.I.getValue()) {
         this.G = 0.0;
         this.P = var1;
         return 0.0;
      } else {
         double var3 = Math.clamp((var1 - this.P) / 1.0E9, 0.0, 0.1);
         this.P = var1;
         this.G = (this.G + var3 * Math.toRadians(this.E.getValue())) % (Math.PI * 2);
         return this.G;
      }
   }

   private void c(class_332 var1, int var2, int var3) {
      this.n(var1, var2, var3, false);
   }

   private void n(class_332 var1, int var2, int var3, boolean var4) {
      class_310 var5 = getMc();
      int var6 = this.f.getValue().intValue();
      int var7 = this.x.getValue().intValue();
      int var8 = this.s();
      com.larpclient.g.l var9 = null;
      if (this.X.G("Tung Tung")) {
         double var10 = Math.hypot(var1.method_51448().m00(), var1.method_51448().m01()) * var5.method_22683().method_4495();
         var9 = com.larpclient.g.X((int)Math.ceil(2 * var6 * var10), var4);
      }

      if (!var4) {
         this.s = var9;
      }

      if (!var4 && var8 >>> 24 != 0) {
         k.l var12 = com.larpclient.k.m("crosshair");
         int var11 = this.I(var9);
         com.larpclient.k.d(
            var12,
            new cv.z(
               var12.l(var5.method_22683().method_4486()) + (int)Math.floor((var12.L / 2.0 - var11) * var12.o()),
               var12.M(var5.method_22683().method_4502()) + (int)Math.floor((var12.F / 2.0 - var11) * var12.o()),
               (int)Math.ceil(2 * var11 * var12.o()),
               (int)Math.ceil(2 * var11 * var12.o())
            )
         );
      }

      double var13 = this.s() + this.J();
      if (var9 != null) {
         b5.S(var1, var2, var3, var9, var6, var8, var13);
      } else {
         b5.k(var1, var2, var3, this.X.G("Tung Tung") ? "Star of David" : this.X.getValue(), var6, var7, this.o(), var8, this.C.getValue(), var13, var4);
      }
   }

   public static boolean y() {
      return Y != null && Y.isEnabled() ? Y != null && Y.isOperational() : false;
   }
}

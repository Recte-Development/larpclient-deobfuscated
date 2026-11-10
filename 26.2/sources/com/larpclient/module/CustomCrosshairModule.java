package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.h7;
import com.larpclient.ho;
import com.larpclient.hw;
import com.larpclient.kc;
import com.larpclient.m4;
import com.larpclient.p2;
import com.larpclient.sb;
import com.larpclient.zd;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.Identifier;

public final class CustomCrosshairModule extends Module {
   public static final String K = "Tung Tung";
   private static final Map<String, String> h = Map.of("6-7", "67", "Custom Image", "Tung Tung");
   private static CustomCrosshairModule S;
   private final l v = this.addSetting(
      new l("Style", "Crosshair geometry", "Star of David", Stream.concat(sb.s.stream(), Stream.of("Tung Tung")).toArray(String[]::new)) {
         @Override
         public boolean acceptsValue(String var1) {
            return CustomCrosshairModule.h.containsKey(var1) || super.acceptsValue(var1);
         }

         @Override
         public void setValue(String var1) {
            super.setValue(CustomCrosshairModule.h.getOrDefault(var1, var1));
         }
      }
   );
   private final g j = this.addSetting(new g("Drawing", "Cells of the drawn crosshair", kc.I.a()));
   private final e l = this.addSetting(new e("Size", "Shape radius; half the image width", 7.0, 2.0, 24.0, 1.0));
   private final e f = this.addSetting(new e("Thickness", "Line width", 1.0, 1.0, 5.0, 1.0));
   private final s w = this.addSetting(new s("Theme Color", "Follow the interface accent color", true));
   private final i R = this.addSetting(new i("Color", "Crosshair color and opacity", -16737281));
   private final s W = this.addSetting(new s("Outline", "Thin dark contour for bright and dark scenery", false));
   private final s X = this.addSetting(new s("Spin", "Continuously rotate the crosshair", true));
   private final e t = this.addSetting(new e("Spin Speed", "Degrees per second", 180.0, 30.0, 720.0, 15.0));
   private final s N = this.addSetting(new s("Click Spin", "Turn a quarter on each left click", false));
   private final e z = this.addSetting(new e("Click Spin Time", "Milliseconds per quarter turn", 350.0, 100.0, 1000.0, 25.0));
   private double x;
   private long g;
   private double J;
   private double b;
   private double o;
   private long F;
   private zd.e Z;
   private String q;
   private kc L = kc.I;

   public CustomCrosshairModule() {
      super("CustomCrosshair", "Customize your crosshair", Category.RENDER, -1);
      S = this;
      this.t.P(this.X::getValue);
      this.R.P(() -> !this.w.getValue());
      this.z.P(this.N::getValue);
      this.f.P(() -> sb.b(this.v.getValue()));
      this.W.P(() -> !this.v.O("Tung Tung"));
      this.j.P(() -> false);
      this.v.o(var0 -> com.larpclient.h.A("Crosshair", "Style selected: {}", var0));
      this.X.o(var0 -> com.larpclient.h.A("Crosshair", "Spin {}", var0 ? "enabled" : "disabled"));
      this.N.o(var1 -> {
         this.c();
         com.larpclient.h.A("Crosshair", "Click Spin {}", var1 ? "enabled" : "disabled");
      });
      HudElementRegistry.addLast(
         Identifier.fromNamespaceAndPath("larp-client", "customcrosshair"),
         (var1, var2) -> {
            Minecraft var3 = getMc();
            if (this.isOperational()
               && this.isInGame()
               && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
               && var3.options.getCameraType().isFirstPerson()
               && (var3.gui.screen() == null || var3.gui.screen().getClass() == ChatScreen.class || var3.gui.screen() instanceof h7 var4 && var4.G())) {
               var1 = p2.x(var1, false, false);
               if (var1 != null) {
                  m4.Z(var1);
                  m4.p var10 = m4.S("crosshair");
                  if (!var10.k()) {
                     var10.b(var3.getWindow().getGuiScaledWidth(), var3.getWindow().getGuiScaledHeight());
                     var10.N(var1, var10.T(var3.getWindow().getGuiScaledWidth()), var10.P(var3.getWindow().getGuiScaledHeight()));

                     try {
                        this.F(var1, var10.T(var3.getWindow().getGuiScaledWidth()) + var10.d / 2, var10.P(var3.getWindow().getGuiScaledHeight()) + var10.T / 2);
                     } finally {
                        var10.w(var1);
                     }
                  }
               }
            }
         }
      );
   }

   public static void b(GuiGraphicsExtractor var0, int var1, int var2) {
      if (S != null) {
         S.z(var0, var1, var2, true);
      }
   }

   public static int q() {
      return S == null ? 10 : S.R(S.v.O("Tung Tung") ? S.Z : null);
   }

   private int R(zd.e var1) {
      int var2 = this.l.getValue().intValue();
      return var1 != null
         ? com.larpclient.pm.t(var1, var2, this.X.getValue() || this.N.getValue())
         : sb.l(this.v.getValue(), var2, this.f.getValue().intValue(), this.W.getValue());
   }

   public List<q<?>> H() {
      return List.of(this.v, this.j, this.l, this.f);
   }

   public List<q<?>> Z() {
      return List.of(this.w, this.R, this.W, this.X, this.t, this.N, this.z);
   }

   public boolean V() {
      return this.v.O("Tung Tung");
   }

   public boolean I() {
      return this.v.O("Draw Your Own");
   }

   public kc l() {
      String var1 = this.j.getValue();
      if (!var1.equals(this.q)) {
         this.q = var1;

         try {
            this.L = kc.i(var1);
         } catch (IllegalArgumentException var3) {
            this.L = kc.I;
            com.larpclient.h.g("Crosshair", "Saved drawing is unreadable ({}); showing the default until it is edited", var3.getMessage());
         }
      }

      return this.L;
   }

   public void H(int var1, int var2, boolean var3) {
      kc var4 = this.l().I(var1, var2, var3);
      if (var4 != this.L) {
         this.j.k(var4.a());
      }
   }

   public void W() {
      this.j.k(kc.H.a());
      com.larpclient.h.A("Crosshair", "Drawing cleared");
   }

   public static void a(JsonObject var0) {
      if (!var0.has("Theme Color") && var0.has("Color")) {
         try {
            int var1 = (int)((long)var0.get("Color").getAsDouble());
            var0.addProperty("Theme Color", var1 == -1381654 || var1 == ho.J);
         } catch (RuntimeException var2) {
         }
      }
   }

   private int F() {
      return this.w.getValue() ? ho.J | 0xFF000000 : this.R.getValue();
   }

   public int m() {
      return this.F() | 0xFF000000;
   }

   @Override
   public void onEnable() {
      this.x = 0.0;
      this.c();
   }

   @Override
   public void onDisable() {
      this.x = 0.0;
      this.c();
      this.Z = null;
      zd.B();
   }

   public static void q(int var0) {
      if (S != null && S.isEnabled()) {
         CustomCrosshairModule var1 = S;
         if (var0 == 0 && var1 != null && var1.isOperational() && var1.N.getValue() && getMc().player != null && getMc().gui.screen() == null) {
            var1.g(System.nanoTime());
         }
      }
   }

   private void c() {
      this.J = this.b = this.o = 0.0;
      this.F = 0L;
   }

   private void g(long var1) {
      double var3 = this.z.getValue() / 1000.0;
      double var5 = this.F == 0L ? 1.0 : Math.clamp((var1 - this.F) / 1.0E9 / var3, 0.0, 1.0);
      double var7 = this.k(var5);
      double var9 = this.L(var5);
      double var11 = Math.floor(var7 / (Math.PI * 2)) * 2.0 * Math.PI;
      this.J = var7 - var11;
      this.b = this.b - var11 + (Math.PI / 2);
      this.o = var9;
      this.F = var1;
   }

   private double k(double var1) {
      double var3 = this.z.getValue() / 1000.0;
      double var5 = var1 * var1;
      double var7 = var5 * var1;
      return (2.0 * var7 - 3.0 * var5 + 1.0) * this.J + (var7 - 2.0 * var5 + var1) * this.o * var3 + (3.0 * var5 - 2.0 * var7) * this.b;
   }

   private double L(double var1) {
      double var3 = this.z.getValue() / 1000.0;
      double var5 = var1 * var1;
      return ((6.0 * var5 - 6.0 * var1) * this.J + (3.0 * var5 - 4.0 * var1 + 1.0) * this.o * var3 + (6.0 * var1 - 6.0 * var5) * this.b) / var3;
   }

   private double T() {
      if (this.N.getValue() && this.F != 0L) {
         double var1 = Math.clamp((System.nanoTime() - this.F) / 1.0E9 / (this.z.getValue() / 1000.0), 0.0, 1.0);
         return this.k(var1);
      } else {
         return this.b;
      }
   }

   private double h() {
      long var1 = System.nanoTime();
      if (!this.X.getValue()) {
         this.x = 0.0;
         this.g = var1;
         return 0.0;
      } else {
         double var3 = Math.clamp((var1 - this.g) / 1.0E9, 0.0, 0.1);
         this.g = var1;
         this.x = (this.x + var3 * Math.toRadians(this.t.getValue())) % (Math.PI * 2);
         return this.x;
      }
   }

   private void F(GuiGraphicsExtractor var1, int var2, int var3) {
      this.z(var1, var2, var3, false);
   }

   private void z(GuiGraphicsExtractor var1, int var2, int var3, boolean var4) {
      Minecraft var5 = getMc();
      int var6 = this.l.getValue().intValue();
      int var7 = this.f.getValue().intValue();
      int var8 = this.F();
      zd.e var9 = null;
      if (this.v.O("Tung Tung")) {
         double var10 = Math.hypot(var1.pose().m00(), var1.pose().m01()) * var5.getWindow().getGuiScale();
         var9 = zd.L((int)Math.ceil(2 * var6 * var10), var4);
      }

      if (!var4) {
         this.Z = var9;
      }

      if (!var4 && var8 >>> 24 != 0) {
         m4.p var12 = m4.S("crosshair");
         int var11 = this.R(var9);
         m4.k(
            var12,
            new hw.l(
               var12.T(var5.getWindow().getGuiScaledWidth()) + (int)Math.floor((var12.d / 2.0 - var11) * var12.s()),
               var12.P(var5.getWindow().getGuiScaledHeight()) + (int)Math.floor((var12.T / 2.0 - var11) * var12.s()),
               (int)Math.ceil(2 * var11 * var12.s()),
               (int)Math.ceil(2 * var11 * var12.s())
            )
         );
      }

      double var13 = this.h() + this.T();
      if (var9 != null) {
         com.larpclient.pm.U(var1, var2, var3, var9, var6, var8, var13);
      } else {
         com.larpclient.pm.m(
            var1, var2, var3, this.v.O("Tung Tung") ? "Star of David" : this.v.getValue(), var6, var7, this.l(), var8, this.W.getValue(), var13, var4
         );
      }
   }

   public static boolean p() {
      return S != null && S.isEnabled() ? S != null && S.isOperational() : false;
   }
}

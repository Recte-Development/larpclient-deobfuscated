package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient._q;
import com.larpclient.dk;
import com.larpclient.fv;
import com.larpclient.oc;
import com.larpclient.qc;
import com.larpclient.qj;
import com.larpclient.qy;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.Identifier;

public final class CustomCrosshairModule extends Module {
   public static final String o = "Tung Tung";
   private static final Map<String, String> W = Map.of("6-7", "67", "Custom Image", "Tung Tung");
   private static CustomCrosshairModule G;
   private final o j = this.addSetting(
      new o("Style", "Crosshair geometry", "Star of David", Stream.concat(com.larpclient.z.G.stream(), Stream.of("Tung Tung")).toArray(String[]::new)) {
         @Override
         public boolean acceptsValue(String var1) {
            return CustomCrosshairModule.W.containsKey(var1) || super.acceptsValue(var1);
         }

         @Override
         public void setValue(String var1) {
            super.setValue(CustomCrosshairModule.W.getOrDefault(var1, var1));
         }
      }
   );
   private final e s = this.addSetting(new e("Drawing", "Cells of the drawn crosshair", qj.K.G()));
   private final b K = this.addSetting(new b("Size", "Shape radius; half the image width", 7.0, 2.0, 24.0, 1.0));
   private final b b = this.addSetting(new b("Thickness", "Line width", 1.0, 1.0, 5.0, 1.0));
   private final m V = this.addSetting(new m("Theme Color", "Follow the interface accent color", true));
   private final h H = this.addSetting(new h("Color", "Crosshair color and opacity", -16737281));
   private final m d = this.addSetting(new m("Outline", "Thin dark contour for bright and dark scenery", false));
   private final m q = this.addSetting(new m("Spin", "Continuously rotate the crosshair", true));
   private final b c = this.addSetting(new b("Spin Speed", "Degrees per second", 180.0, 30.0, 720.0, 15.0));
   private final m n = this.addSetting(new m("Click Spin", "Turn a quarter on each left click", false));
   private final b i = this.addSetting(new b("Click Spin Time", "Milliseconds per quarter turn", 350.0, 100.0, 1000.0, 25.0));
   private double k;
   private long C;
   private double f;
   private double m;
   private double E;
   private long p;
   private oc.j v;
   private String U;
   private qj a = qj.K;

   public CustomCrosshairModule() {
      super("CustomCrosshair", "Customize your crosshair", Category.RENDER, -1);
      G = this;
      this.c.a(this.q::getValue);
      this.H.a(() -> !this.V.getValue());
      this.i.a(this.n::getValue);
      this.b.a(() -> com.larpclient.z.a(this.j.getValue()));
      this.d.a(() -> !this.j.w("Tung Tung"));
      this.s.a(() -> false);
      this.j.w(var0 -> _q.z("Crosshair", "Style selected: {}", var0));
      this.q.w(var0 -> _q.z("Crosshair", "Spin {}", var0 ? "enabled" : "disabled"));
      this.n.w(var1 -> {
         this.M();
         _q.z("Crosshair", "Click Spin {}", var1 ? "enabled" : "disabled");
      });
      HudElementRegistry.addLast(
         Identifier.fromNamespaceAndPath("larp-client", "customcrosshair"),
         (var1, var2) -> {
            Minecraft var3 = getMc();
            if (this.isOperational()
               && this.isInGame()
               && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
               && var3.options.getCameraType().isFirstPerson()
               && (var3.gui.screen() == null || var3.gui.screen().getClass() == ChatScreen.class || var3.gui.screen() instanceof fv var4 && var4.C())) {
               var1 = qc.h(var1, false, false);
               if (var1 != null) {
                  dk.n(var1);
                  dk.q var10 = dk.T("crosshair");
                  if (!var10.X()) {
                     var10.s(var3.getWindow().getGuiScaledWidth(), var3.getWindow().getGuiScaledHeight());
                     var10.p(var1, var10.f(var3.getWindow().getGuiScaledWidth()), var10.p(var3.getWindow().getGuiScaledHeight()));

                     try {
                        this.n(var1, var10.f(var3.getWindow().getGuiScaledWidth()) + var10.w / 2, var10.p(var3.getWindow().getGuiScaledHeight()) + var10.Z / 2);
                     } finally {
                        var10.I(var1);
                     }
                  }
               }
            }
         }
      );
   }

   public static void U(GuiGraphicsExtractor var0, int var1, int var2) {
      if (G != null) {
         G.z(var0, var1, var2, true);
      }
   }

   public static int X() {
      return G == null ? 10 : G.C(G.j.w("Tung Tung") ? G.v : null);
   }

   private int C(oc.j var1) {
      int var2 = this.K.getValue().intValue();
      return var1 != null
         ? com.larpclient.d.D(var1, var2, this.q.getValue() || this.n.getValue())
         : com.larpclient.z.U(this.j.getValue(), var2, this.b.getValue().intValue(), this.d.getValue());
   }

   public List<r<?>> f() {
      return List.of(this.j, this.s, this.K, this.b);
   }

   public List<r<?>> x() {
      return List.of(this.V, this.H, this.d, this.q, this.c, this.n, this.i);
   }

   public boolean o() {
      return this.j.w("Tung Tung");
   }

   public boolean P() {
      return this.j.w("Draw Your Own");
   }

   public qj T() {
      String var1 = this.s.getValue();
      if (!var1.equals(this.U)) {
         this.U = var1;

         try {
            this.a = qj.p(var1);
         } catch (IllegalArgumentException var3) {
            this.a = qj.K;
            _q.R("Crosshair", "Saved drawing is unreadable ({}); showing the default until it is edited", var3.getMessage());
         }
      }

      return this.a;
   }

   public void R(int var1, int var2, boolean var3) {
      qj var4 = this.T().E(var1, var2, var3);
      if (var4 != this.a) {
         this.s.T(var4.G());
      }
   }

   public void F() {
      this.s.T(qj.U.G());
      _q.z("Crosshair", "Drawing cleared");
   }

   public static void y(JsonObject var0) {
      if (!var0.has("Theme Color") && var0.has("Color")) {
         try {
            int var1 = (int)((long)var0.get("Color").getAsDouble());
            var0.addProperty("Theme Color", var1 == -1381654 || var1 == com.larpclient.c.y);
         } catch (RuntimeException var2) {
         }
      }
   }

   private int G() {
      return this.V.getValue() ? com.larpclient.c.y | 0xFF000000 : this.H.getValue();
   }

   public int e() {
      return this.G() | 0xFF000000;
   }

   @Override
   public void onEnable() {
      this.k = 0.0;
      this.M();
   }

   @Override
   public void onDisable() {
      this.k = 0.0;
      this.M();
      this.v = null;
      oc.s();
   }

   public static void k(int var0) {
      if (G != null && G.isEnabled()) {
         CustomCrosshairModule var1 = G;
         if (var0 == 0 && var1 != null && var1.isOperational() && var1.n.getValue() && getMc().player != null && getMc().gui.screen() == null) {
            var1.s(System.nanoTime());
         }
      }
   }

   private void M() {
      this.f = this.m = this.E = 0.0;
      this.p = 0L;
   }

   private void s(long var1) {
      double var3 = this.i.getValue() / 1000.0;
      double var5 = this.p == 0L ? 1.0 : Math.clamp((var1 - this.p) / 1.0E9 / var3, 0.0, 1.0);
      double var7 = this.r(var5);
      double var9 = this.w(var5);
      double var11 = Math.floor(var7 / (Math.PI * 2)) * 2.0 * Math.PI;
      this.f = var7 - var11;
      this.m = this.m - var11 + (Math.PI / 2);
      this.E = var9;
      this.p = var1;
   }

   private double r(double var1) {
      double var3 = this.i.getValue() / 1000.0;
      double var5 = var1 * var1;
      double var7 = var5 * var1;
      return (2.0 * var7 - 3.0 * var5 + 1.0) * this.f + (var7 - 2.0 * var5 + var1) * this.E * var3 + (3.0 * var5 - 2.0 * var7) * this.m;
   }

   private double w(double var1) {
      double var3 = this.i.getValue() / 1000.0;
      double var5 = var1 * var1;
      return ((6.0 * var5 - 6.0 * var1) * this.f + (3.0 * var5 - 4.0 * var1 + 1.0) * this.E * var3 + (6.0 * var1 - 6.0 * var5) * this.m) / var3;
   }

   private double F() {
      if (this.n.getValue() && this.p != 0L) {
         double var1 = Math.clamp((System.nanoTime() - this.p) / 1.0E9 / (this.i.getValue() / 1000.0), 0.0, 1.0);
         return this.r(var1);
      } else {
         return this.m;
      }
   }

   private double g() {
      long var1 = System.nanoTime();
      if (!this.q.getValue()) {
         this.k = 0.0;
         this.C = var1;
         return 0.0;
      } else {
         double var3 = Math.clamp((var1 - this.C) / 1.0E9, 0.0, 0.1);
         this.C = var1;
         this.k = (this.k + var3 * Math.toRadians(this.c.getValue())) % (Math.PI * 2);
         return this.k;
      }
   }

   private void n(GuiGraphicsExtractor var1, int var2, int var3) {
      this.z(var1, var2, var3, false);
   }

   private void z(GuiGraphicsExtractor var1, int var2, int var3, boolean var4) {
      Minecraft var5 = getMc();
      int var6 = this.K.getValue().intValue();
      int var7 = this.b.getValue().intValue();
      int var8 = this.G();
      oc.j var9 = null;
      if (this.j.w("Tung Tung")) {
         double var10 = Math.hypot(var1.pose().m00(), var1.pose().m01()) * var5.getWindow().getGuiScale();
         var9 = oc.o((int)Math.ceil(2 * var6 * var10), var4);
      }

      if (!var4) {
         this.v = var9;
      }

      if (!var4 && var8 >>> 24 != 0) {
         dk.q var12 = dk.T("crosshair");
         int var11 = this.C(var9);
         dk.X(
            var12,
            new qy.i(
               var12.f(var5.getWindow().getGuiScaledWidth()) + (int)Math.floor((var12.w / 2.0 - var11) * var12.F()),
               var12.p(var5.getWindow().getGuiScaledHeight()) + (int)Math.floor((var12.Z / 2.0 - var11) * var12.F()),
               (int)Math.ceil(2 * var11 * var12.F()),
               (int)Math.ceil(2 * var11 * var12.F())
            )
         );
      }

      double var13 = this.g() + this.F();
      if (var9 != null) {
         com.larpclient.d.s(var1, var2, var3, var9, var6, var8, var13);
      } else {
         com.larpclient.d.C(
            var1, var2, var3, this.j.w("Tung Tung") ? "Star of David" : this.j.getValue(), var6, var7, this.T(), var8, this.d.getValue(), var13, var4
         );
      }
   }

   public static boolean K() {
      return G != null && G.isEnabled() ? G != null && G.isOperational() : false;
   }
}

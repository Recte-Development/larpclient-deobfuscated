package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.ez;
import com.larpclient.hu;
import com.larpclient.m3;
import com.larpclient.mq;
import com.larpclient.mt;
import com.larpclient.x1;
import com.larpclient.x5;
import com.larpclient.zo;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.Identifier;

public final class CustomCrosshairModule extends Module {
   public static final String p = "Tung Tung";
   private static final Map<String, String> b = Map.of("6-7", "67", "Custom Image", "Tung Tung");
   private static CustomCrosshairModule k;
   private final d6 U = this.addSetting(
      new d6("Style", "Crosshair geometry", "Star of David", Stream.concat(x5.F.stream(), Stream.of("Tung Tung")).toArray(String[]::new)) {
         @Override
         public boolean acceptsValue(String var1) {
            return CustomCrosshairModule.b.containsKey(var1) || super.acceptsValue(var1);
         }

         @Override
         public void setValue(String var1) {
            super.setValue(CustomCrosshairModule.b.getOrDefault(var1, var1));
         }
      }
   );
   private final ds O = this.addSetting(new ds("Drawing", "Cells of the drawn crosshair", com.larpclient.dp.h.z()));
   private final d9 n = this.addSetting(new d9("Size", "Shape radius; half the image width", 7.0, 2.0, 24.0, 1.0));
   private final d9 c = this.addSetting(new d9("Thickness", "Line width", 1.0, 1.0, 5.0, 1.0));
   private final dh Y = this.addSetting(new dh("Theme Color", "Follow the interface accent color", true));
   private final dk V = this.addSetting(new dk("Color", "Crosshair color and opacity", -16737281));
   private final dh u = this.addSetting(new dh("Outline", "Thin dark contour for bright and dark scenery", false));
   private final dh B = this.addSetting(new dh("Spin", "Continuously rotate the crosshair", true));
   private final d9 W = this.addSetting(new d9("Spin Speed", "Degrees per second", 180.0, 30.0, 720.0, 15.0));
   private final dh X = this.addSetting(new dh("Click Spin", "Turn a quarter on each left click", false));
   private final d9 r = this.addSetting(new d9("Click Spin Time", "Milliseconds per quarter turn", 350.0, 100.0, 1000.0, 25.0));
   private double z;
   private long L;
   private double s;
   private double e;
   private double m;
   private long K;
   private x1.b Q;
   private String f;
   private com.larpclient.dp w = com.larpclient.dp.h;

   public CustomCrosshairModule() {
      super("CustomCrosshair", "Customize your crosshair", Category.RENDER, -1);
      k = this;
      this.W.H(this.B::P);
      this.V.H(() -> !this.Y.P());
      this.r.H(this.X::P);
      this.c.H(() -> x5.Z(this.U.P()));
      this.u.H(() -> !this.U.S("Tung Tung"));
      this.O.H(() -> false);
      this.U.h(var0 -> mt.K("Crosshair", "Style selected: {}", var0));
      this.B.h(var0 -> mt.K("Crosshair", "Spin {}", var0 ? "enabled" : "disabled"));
      this.X.h(var1 -> {
         this.E();
         mt.K("Crosshair", "Click Spin {}", var1 ? "enabled" : "disabled");
      });
      HudElementRegistry.addLast(
         Identifier.fromNamespaceAndPath("larp-client", "customcrosshair"),
         (var1, var2) -> {
            Minecraft var3 = getMc();
            if (this.isOperational()
               && this.isInGame()
               && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
               && var3.options.getCameraType().isFirstPerson()
               && (var3.gui.screen() == null || var3.gui.screen().getClass() == ChatScreen.class || var3.gui.screen() instanceof hu var4 && var4.Z())) {
               var1 = zo.R(var1, false, false);
               if (var1 != null) {
                  mq.x(var1);
                  mq.s var10 = mq.B("crosshair");
                  if (!var10.N()) {
                     var10.P(var3.getWindow().getGuiScaledWidth(), var3.getWindow().getGuiScaledHeight());
                     var10.v(var1, var10.W(var3.getWindow().getGuiScaledWidth()), var10.i(var3.getWindow().getGuiScaledHeight()));

                     try {
                        this.p(var1, var10.W(var3.getWindow().getGuiScaledWidth()) + var10.W / 2, var10.i(var3.getWindow().getGuiScaledHeight()) + var10.n / 2);
                     } finally {
                        var10.i(var1);
                     }
                  }
               }
            }
         }
      );
   }

   public static void l(GuiGraphicsExtractor var0, int var1, int var2) {
      if (k != null) {
         k.Q(var0, var1, var2, true);
      }
   }

   public static int C() {
      return k == null ? 10 : k.T(k.U.S("Tung Tung") ? k.Q : null);
   }

   private int T(x1.b var1) {
      int var2 = this.n.P().intValue();
      return var1 != null ? m3.G(var1, var2, this.B.P() || this.X.P()) : x5.V(this.U.P(), var2, this.c.P().intValue(), this.u.P());
   }

   public List<df<?>> S() {
      return List.of(this.U, this.O, this.n, this.c);
   }

   public List<df<?>> V() {
      return List.of(this.Y, this.V, this.u, this.B, this.W, this.X, this.r);
   }

   public boolean Y() {
      return this.U.S("Tung Tung");
   }

   public boolean n() {
      return this.U.S("Draw Your Own");
   }

   public com.larpclient.dp p() {
      String var1 = this.O.P();
      if (!var1.equals(this.f)) {
         this.f = var1;

         try {
            this.w = com.larpclient.dp.T(var1);
         } catch (IllegalArgumentException var3) {
            this.w = com.larpclient.dp.h;
            mt.T("Crosshair", "Saved drawing is unreadable ({}); showing the default until it is edited", var3.getMessage());
         }
      }

      return this.w;
   }

   public void E(int var1, int var2, boolean var3) {
      com.larpclient.dp var4 = this.p().x(var1, var2, var3);
      if (var4 != this.w) {
         this.O.v(var4.z());
      }
   }

   public void r() {
      this.O.v(com.larpclient.dp.m.z());
      mt.K("Crosshair", "Drawing cleared");
   }

   public static void D(JsonObject var0) {
      if (!var0.has("Theme Color") && var0.has("Color")) {
         try {
            int var1 = (int)((long)var0.get("Color").getAsDouble());
            var0.addProperty("Theme Color", var1 == -1381654 || var1 == com.larpclient.f.C);
         } catch (RuntimeException var2) {
         }
      }
   }

   private int H() {
      return this.Y.P() ? com.larpclient.f.C | 0xFF000000 : this.V.P();
   }

   public int b() {
      return this.H() | 0xFF000000;
   }

   @Override
   public void onEnable() {
      this.z = 0.0;
      this.E();
   }

   @Override
   public void onDisable() {
      this.z = 0.0;
      this.E();
      this.Q = null;
      x1.z();
   }

   public static void E(int var0) {
      if (k != null && k.isEnabled()) {
         CustomCrosshairModule var1 = k;
         if (var0 == 0 && var1 != null && var1.isOperational() && var1.X.P() && getMc().player != null && getMc().gui.screen() == null) {
            var1.S(System.nanoTime());
         }
      }
   }

   private void E() {
      this.s = this.e = this.m = 0.0;
      this.K = 0L;
   }

   private void S(long var1) {
      double var3 = this.r.P() / 1000.0;
      double var5 = this.K == 0L ? 1.0 : Math.clamp((var1 - this.K) / 1.0E9 / var3, 0.0, 1.0);
      double var7 = this.j(var5);
      double var9 = this.N(var5);
      double var11 = Math.floor(var7 / (Math.PI * 2)) * 2.0 * Math.PI;
      this.s = var7 - var11;
      this.e = this.e - var11 + (Math.PI / 2);
      this.m = var9;
      this.K = var1;
   }

   private double j(double var1) {
      double var3 = this.r.P() / 1000.0;
      double var5 = var1 * var1;
      double var7 = var5 * var1;
      return (2.0 * var7 - 3.0 * var5 + 1.0) * this.s + (var7 - 2.0 * var5 + var1) * this.m * var3 + (3.0 * var5 - 2.0 * var7) * this.e;
   }

   private double N(double var1) {
      double var3 = this.r.P() / 1000.0;
      double var5 = var1 * var1;
      return ((6.0 * var5 - 6.0 * var1) * this.s + (3.0 * var5 - 4.0 * var1 + 1.0) * this.m * var3 + (6.0 * var1 - 6.0 * var5) * this.e) / var3;
   }

   private double m() {
      if (this.X.P() && this.K != 0L) {
         double var1 = Math.clamp((System.nanoTime() - this.K) / 1.0E9 / (this.r.P() / 1000.0), 0.0, 1.0);
         return this.j(var1);
      } else {
         return this.e;
      }
   }

   private double y() {
      long var1 = System.nanoTime();
      if (!this.B.P()) {
         this.z = 0.0;
         this.L = var1;
         return 0.0;
      } else {
         double var3 = Math.clamp((var1 - this.L) / 1.0E9, 0.0, 0.1);
         this.L = var1;
         this.z = (this.z + var3 * Math.toRadians(this.W.P())) % (Math.PI * 2);
         return this.z;
      }
   }

   private void p(GuiGraphicsExtractor var1, int var2, int var3) {
      this.Q(var1, var2, var3, false);
   }

   private void Q(GuiGraphicsExtractor var1, int var2, int var3, boolean var4) {
      Minecraft var5 = getMc();
      int var6 = this.n.P().intValue();
      int var7 = this.c.P().intValue();
      int var8 = this.H();
      x1.b var9 = null;
      if (this.U.S("Tung Tung")) {
         double var10 = Math.hypot(var1.pose().m00(), var1.pose().m01()) * var5.getWindow().getGuiScale();
         var9 = x1.o((int)Math.ceil(2 * var6 * var10), var4);
      }

      if (!var4) {
         this.Q = var9;
      }

      if (!var4 && var8 >>> 24 != 0) {
         mq.s var12 = mq.B("crosshair");
         int var11 = this.T(var9);
         mq.H(
            var12,
            new ez.h(
               var12.W(var5.getWindow().getGuiScaledWidth()) + (int)Math.floor((var12.W / 2.0 - var11) * var12.b()),
               var12.i(var5.getWindow().getGuiScaledHeight()) + (int)Math.floor((var12.n / 2.0 - var11) * var12.b()),
               (int)Math.ceil(2 * var11 * var12.b()),
               (int)Math.ceil(2 * var11 * var12.b())
            )
         );
      }

      double var13 = this.y() + this.m();
      if (var9 != null) {
         m3.t(var1, var2, var3, var9, var6, var8, var13);
      } else {
         m3.x(var1, var2, var3, this.U.S("Tung Tung") ? "Star of David" : this.U.P(), var6, var7, this.p(), var8, this.u.P(), var13, var4);
      }
   }

   public static boolean I() {
      return k != null && k.isEnabled() ? k != null && k.isOperational() : false;
   }
}

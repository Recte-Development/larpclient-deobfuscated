package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient._h;
import com.larpclient._n;
import com.larpclient.fz;
import com.larpclient.jt;
import com.larpclient.o0;
import com.larpclient.o8;
import com.larpclient.ps;
import com.larpclient.r4;
import com.larpclient.rv;
import com.larpclient.y8;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.Identifier;

public final class CustomCrosshairModule extends Module {
   public static final String C = "Tung Tung";
   private static final Map<String, String> P = Map.of("6-7", "67", "Custom Image", "Tung Tung");
   private static CustomCrosshairModule i;
   private final lp q = this.addSetting(
      new lp("Style", "Crosshair geometry", "Star of David", Stream.concat(o0.f.stream(), Stream.of("Tung Tung")).toArray(String[]::new)) {
         @Override
         public boolean acceptsValue(String var1) {
            return CustomCrosshairModule.P.containsKey(var1) || super.acceptsValue(var1);
         }

         @Override
         public void setValue(String var1) {
            super.setValue(CustomCrosshairModule.P.getOrDefault(var1, var1));
         }
      }
   );
   private final li d = this.addSetting(new li("Drawing", "Cells of the drawn crosshair", _n.x.t()));
   private final lc g = this.addSetting(new lc("Size", "Shape radius; half the image width", 7.0, 2.0, 24.0, 1.0));
   private final lc f = this.addSetting(new lc("Thickness", "Line width", 1.0, 1.0, 5.0, 1.0));
   private final ln z = this.addSetting(new ln("Theme Color", "Follow the interface accent color", true));
   private final lv m = this.addSetting(new lv("Color", "Crosshair color and opacity", -16737281));
   private final ln E = this.addSetting(new ln("Outline", "Thin dark contour for bright and dark scenery", false));
   private final ln r = this.addSetting(new ln("Spin", "Continuously rotate the crosshair", true));
   private final lc W = this.addSetting(new lc("Spin Speed", "Degrees per second", 180.0, 30.0, 720.0, 15.0));
   private final ln L = this.addSetting(new ln("Click Spin", "Turn a quarter on each left click", false));
   private final lc F = this.addSetting(new lc("Click Spin Time", "Milliseconds per quarter turn", 350.0, 100.0, 1000.0, 25.0));
   private double x;
   private long T;
   private double H;
   private double N;
   private double t;
   private long u;
   private fz.j v;
   private String A;
   private _n K = _n.x;

   public CustomCrosshairModule() {
      super("CustomCrosshair", "Customize your crosshair", Category.RENDER, -1);
      i = this;
      this.W.x(this.r::getValue);
      this.m.x(() -> !this.z.getValue());
      this.F.x(this.L::getValue);
      this.f.x(() -> o0.Y(this.q.getValue()));
      this.E.x(() -> !this.q.i("Tung Tung"));
      this.d.x(() -> false);
      this.q.W(var0 -> r4.V("Crosshair", "Style selected: {}", var0));
      this.r.W(var0 -> r4.V("Crosshair", "Spin {}", var0 ? "enabled" : "disabled"));
      this.L.W(var1 -> {
         this.j();
         r4.V("Crosshair", "Click Spin {}", var1 ? "enabled" : "disabled");
      });
      HudElementRegistry.addLast(
         Identifier.fromNamespaceAndPath("larp-client", "customcrosshair"),
         (var1, var2) -> {
            Minecraft var3 = getMc();
            if (this.isOperational()
               && this.isInGame()
               && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
               && var3.options.getCameraType().isFirstPerson()
               && (var3.gui.screen() == null || var3.gui.screen().getClass() == ChatScreen.class || var3.gui.screen() instanceof ps var4 && var4.w())) {
               var1 = y8.U(var1, false, false);
               if (var1 != null) {
                  o8.e(var1);
                  o8.a var10 = o8.q("crosshair");
                  if (!var10.z()) {
                     var10.q(var3.getWindow().getGuiScaledWidth(), var3.getWindow().getGuiScaledHeight());
                     var10.J(var1, var10.b(var3.getWindow().getGuiScaledWidth()), var10.M(var3.getWindow().getGuiScaledHeight()));

                     try {
                        this.z(var1, var10.b(var3.getWindow().getGuiScaledWidth()) + var10.N / 2, var10.M(var3.getWindow().getGuiScaledHeight()) + var10.F / 2);
                     } finally {
                        var10.z(var1);
                     }
                  }
               }
            }
         }
      );
   }

   public static void N(GuiGraphicsExtractor var0, int var1, int var2) {
      if (i != null) {
         i.C(var0, var1, var2, true);
      }
   }

   public static int d() {
      return i == null ? 10 : i.I(i.q.i("Tung Tung") ? i.v : null);
   }

   private int I(fz.j var1) {
      int var2 = this.g.getValue().intValue();
      return var1 != null
         ? jt.D(var1, var2, this.r.getValue() || this.L.getValue())
         : o0.d(this.q.getValue(), var2, this.f.getValue().intValue(), this.E.getValue());
   }

   public List<l6<?>> B() {
      return List.of(this.q, this.d, this.g, this.f);
   }

   public List<l6<?>> t() {
      return List.of(this.z, this.m, this.E, this.r, this.W, this.L, this.F);
   }

   public boolean H() {
      return this.q.i("Tung Tung");
   }

   public boolean y() {
      return this.q.i("Draw Your Own");
   }

   public _n e() {
      String var1 = this.d.getValue();
      if (!var1.equals(this.A)) {
         this.A = var1;

         try {
            this.K = _n.e(var1);
         } catch (IllegalArgumentException var3) {
            this.K = _n.x;
            r4.m("Crosshair", "Saved drawing is unreadable ({}); showing the default until it is edited", var3.getMessage());
         }
      }

      return this.K;
   }

   public void Z(int var1, int var2, boolean var3) {
      _n var4 = this.e().i(var1, var2, var3);
      if (var4 != this.K) {
         this.d.i(var4.t());
      }
   }

   public void D() {
      this.d.i(_n.T.t());
      r4.V("Crosshair", "Drawing cleared");
   }

   public static void N(JsonObject var0) {
      if (!var0.has("Theme Color") && var0.has("Color")) {
         try {
            int var1 = (int)((long)var0.get("Color").getAsDouble());
            var0.addProperty("Theme Color", var1 == -1381654 || var1 == rv.e);
         } catch (RuntimeException var2) {
         }
      }
   }

   private int x() {
      return this.z.getValue() ? rv.e | 0xFF000000 : this.m.getValue();
   }

   public int Q() {
      return this.x() | 0xFF000000;
   }

   @Override
   public void onEnable() {
      this.x = 0.0;
      this.j();
   }

   @Override
   public void onDisable() {
      this.x = 0.0;
      this.j();
      this.v = null;
      fz.I();
   }

   public static void j(int var0) {
      if (i != null && i.isEnabled()) {
         CustomCrosshairModule var1 = i;
         if (var0 == 0 && var1 != null && var1.isOperational() && var1.L.getValue() && getMc().player != null && getMc().gui.screen() == null) {
            var1.H(System.nanoTime());
         }
      }
   }

   private void j() {
      this.H = this.N = this.t = 0.0;
      this.u = 0L;
   }

   private void H(long var1) {
      double var3 = this.F.getValue() / 1000.0;
      double var5 = this.u == 0L ? 1.0 : Math.clamp((var1 - this.u) / 1.0E9 / var3, 0.0, 1.0);
      double var7 = this.F(var5);
      double var9 = this.l(var5);
      double var11 = Math.floor(var7 / (Math.PI * 2)) * 2.0 * Math.PI;
      this.H = var7 - var11;
      this.N = this.N - var11 + (Math.PI / 2);
      this.t = var9;
      this.u = var1;
   }

   private double F(double var1) {
      double var3 = this.F.getValue() / 1000.0;
      double var5 = var1 * var1;
      double var7 = var5 * var1;
      return (2.0 * var7 - 3.0 * var5 + 1.0) * this.H + (var7 - 2.0 * var5 + var1) * this.t * var3 + (3.0 * var5 - 2.0 * var7) * this.N;
   }

   private double l(double var1) {
      double var3 = this.F.getValue() / 1000.0;
      double var5 = var1 * var1;
      return ((6.0 * var5 - 6.0 * var1) * this.H + (3.0 * var5 - 4.0 * var1 + 1.0) * this.t * var3 + (6.0 * var1 - 6.0 * var5) * this.N) / var3;
   }

   private double F() {
      if (this.L.getValue() && this.u != 0L) {
         double var1 = Math.clamp((System.nanoTime() - this.u) / 1.0E9 / (this.F.getValue() / 1000.0), 0.0, 1.0);
         return this.F(var1);
      } else {
         return this.N;
      }
   }

   private double q() {
      long var1 = System.nanoTime();
      if (!this.r.getValue()) {
         this.x = 0.0;
         this.T = var1;
         return 0.0;
      } else {
         double var3 = Math.clamp((var1 - this.T) / 1.0E9, 0.0, 0.1);
         this.T = var1;
         this.x = (this.x + var3 * Math.toRadians(this.W.getValue())) % (Math.PI * 2);
         return this.x;
      }
   }

   private void z(GuiGraphicsExtractor var1, int var2, int var3) {
      this.C(var1, var2, var3, false);
   }

   private void C(GuiGraphicsExtractor var1, int var2, int var3, boolean var4) {
      Minecraft var5 = getMc();
      int var6 = this.g.getValue().intValue();
      int var7 = this.f.getValue().intValue();
      int var8 = this.x();
      fz.j var9 = null;
      if (this.q.i("Tung Tung")) {
         double var10 = Math.hypot(var1.pose().m00(), var1.pose().m01()) * var5.getWindow().getGuiScale();
         var9 = fz.m((int)Math.ceil(2 * var6 * var10), var4);
      }

      if (!var4) {
         this.v = var9;
      }

      if (!var4 && var8 >>> 24 != 0) {
         o8.a var12 = o8.q("crosshair");
         int var11 = this.I(var9);
         o8.D(
            var12,
            new _h.p(
               var12.b(var5.getWindow().getGuiScaledWidth()) + (int)Math.floor((var12.N / 2.0 - var11) * var12.p()),
               var12.M(var5.getWindow().getGuiScaledHeight()) + (int)Math.floor((var12.F / 2.0 - var11) * var12.p()),
               (int)Math.ceil(2 * var11 * var12.p()),
               (int)Math.ceil(2 * var11 * var12.p())
            )
         );
      }

      double var13 = this.q() + this.F();
      if (var9 != null) {
         jt.v(var1, var2, var3, var9, var6, var8, var13);
      } else {
         jt.f(var1, var2, var3, this.q.i("Tung Tung") ? "Star of David" : this.q.getValue(), var6, var7, this.e(), var8, this.E.getValue(), var13, var4);
      }
   }

   public static boolean S() {
      return i != null && i.isEnabled() ? i != null && i.isOperational() : false;
   }
}

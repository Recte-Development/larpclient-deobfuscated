package com.larpclient;

import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldExtractionContext;
import net.minecraft.class_11658;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_259;
import net.minecraft.class_310;

public final class uu {
   private static final ip G = ip.build(class_259.method_1077());
   private final Module R;
   private final com.larpclient.module.l3 F = new com.larpclient.module.l3("Highlight", "Show the next placement with a soft block outline", true);
   private final com.larpclient.module.li g = new com.larpclient.module.li(
      "Highlight Color Mode", "Follow your theme or choose a custom color", "Theme", "Theme", "Custom"
   );
   private final com.larpclient.module.l0 W = new com.larpclient.module.l0("Highlight Color", "Custom placement highlight color", ez.a);
   private final com.larpclient.module.lo N = i("Highlight Opacity", "Outline visibility", 0.85, 0.1, 1.0, 0.05);
   private final com.larpclient.module.lo w = new com.larpclient.module.lo("Highlight Thickness", "Outline width in blocks", 0.015, 0.005, 0.04, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   };
   private final com.larpclient.module.l3 s = new com.larpclient.module.l3("Highlight Fill", "Subtle tint inside the placement box", true);
   private final com.larpclient.module.lo c = i("Highlight Fill Opacity", "Keep the scene visible through the fill", 0.08, 0.01, 0.3, 0.01);
   private final com.larpclient.module.lo J = i("Highlight Glow", "Soft edge halo and shared quality-controlled bloom", 0.65, 0.0, 1.5, 0.05);
   private final com.larpclient.module.lo Q;
   private final com.larpclient.module.lo x;
   private final List<com.larpclient.module.lb<?>> y;
   private final e2 I = new e2();
   private final RenderStateDataKey<uu.n> D;
   private class_2338 O;
   private Object q;
   private Object B;
   private Object T;
   private Object u;
   private int k;
   private int C = -1;
   private class_243 i;

   public uu(Module var1, Consumer<com.larpclient.module.lb<?>> var2) {
      this(var1, var2, -1.0, -1.0);
   }

   public uu(Module var1, Consumer<com.larpclient.module.lb<?>> var2, double var3, double var5) {
      this.R = var1;
      this.Q = var3 < 0.0 ? null : r("Highlight Hold", "Seconds to retain the previous placement box", var3, 0.0, 3.0);
      this.x = var5 < 0.0 ? null : r("Highlight Fade", "Seconds for the previous box to fade out", var5, 0.1, 2.0);
      ArrayList var7 = new ArrayList<>(List.of(this.F, this.g, this.W, this.N, this.w, this.s, this.c, this.J));
      if (this.Q != null) {
         var7.add(this.Q);
         var7.add(this.x);
      }

      this.y = List.copyOf(var7);
      this.D = RenderStateDataKey.create(() -> var1.getName() + " placement highlight");
      this.y.forEach(var2);

      for (com.larpclient.module.lb var9 : this.y) {
         if (var9 != this.F) {
            var9.O(this.F::getValue);
         }
      }

      this.W.O(() -> this.F.getValue() && this.g.G("Custom"));
      this.c.O(() -> this.F.getValue() && this.s.getValue());
      this.F.a(var1x -> this.p());
   }

   public boolean S() {
      return this.F.getValue();
   }

   public String V() {
      return this.R.getName() + " highlight";
   }

   public List<com.larpclient.module.lb<?>> l() {
      return this.y;
   }

   public void x(class_2338 var1) {
      class_310 var2 = class_310.method_1551();
      this.O = this.F.getValue() && var1 != null ? var1.method_10062() : null;
      this.q = var2.field_1687;
      this.B = var2.field_1724;
      this.k = var2.field_1724 == null ? 0 : var2.field_1724.field_6012;
   }

   public void p() {
      this.O = null;
      this.q = this.B = this.T = this.u = null;
      this.i = null;
      this.I.j();
   }

   public void d() {
      this.p();
      q8.G(
         this.R.getName(),
         "Placement highlight {}; theme/custom color, world-fixed fades, shared depth-tested glow",
         this.F.getValue() ? "enabled" : "disabled"
      );
   }

   public void U(WorldExtractionContext var1) {
      class_11658 var2 = var1.worldState();
      var2.setData(this.D, null);
      class_310 var3 = class_310.method_1551();
      if (this.R.isRuntimeActive()
         && this.F.getValue()
         && var3.field_1724 != null
         && var3.field_1724.method_5805()
         && var3.field_1755 == null
         && !var3.field_1690.field_1842
         && var3.method_1569()) {
         class_243 var4 = var1.camera().method_71156();
         int var5 = this.i != null && this.i.method_1022(var4) > 8.0 ? 1 : 0;
         if (this.T != var1.world() || this.u != var3.field_1724 || this.C != t_.g || var5 != 0) {
            this.I.j();
            if (var5 != 0) {
               this.O = null;
            }

            this.T = var1.world();
            this.u = var3.field_1724;
            this.C = t_.g;
         }

         this.i = var4;
         class_2338 var6 = fk.f(this.q, var3.field_1687, this.B, var3.field_1724, this.k, var3.field_1724.field_6012) ? this.O : null;
         List var7 = this.I.B(var6, qo.x(), this.Q == null ? 0.06 : this.Q.getValue(), this.x == null ? 0.18 : this.x.getValue());
         if (!var7.isEmpty()) {
            var2.setData(
               this.D,
               new uu.n(
                  var7,
                  this.g.G("Theme") ? ez.a : this.W.getValue(),
                  this.N.getValue(),
                  this.w.getValue(),
                  this.s.getValue() ? this.c.getValue() : 0.0,
                  this.J.getValue()
               )
            );
         }
      } else {
         this.p();
      }
   }

   private uu.n u(class_11658 var1) {
      return (uu.n)var1.getData(this.D);
   }

   public boolean b(class_11658 var1) {
      uu.n var2 = this.u(var1);
      return this.R.isRuntimeActive() && this.F.getValue() && var2 != null && var2.glow > 0.0;
   }

   public void G(f1 var1, boolean var2, class_11658 var3) {
      uu.n var4 = this.u(var3);
      if (this.R.isRuntimeActive() && this.F.getValue() && var4 != null && (!var2 || !(var4.glow <= 0.0))) {
         long var5 = c5.X();

         for (e2.w var8 : var4.boxes) {
            wr.d(var1, var2, G, new class_238(var8.pos()).method_1014(0.002), var8.alpha(), var4.color, var4.opacity, var4.width, var4.fillOpacity, var4.glow);
         }

         c5.w(this.R.getName() + (var2 ? " highlight glow" : " highlight base"), var5);
      }
   }

   private static com.larpclient.module.lo r(String var0, String var1, double var2, double var4, double var6) {
      return new com.larpclient.module.lo(var0, var1, var2, var4, var6, 0.05) {
         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + " s";
         }
      };
   }

   private static com.larpclient.module.lo i(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new com.larpclient.module.lo(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + "%";
         }

         @Override
         public String getEditValueAsString() {
            return Long.toString(Math.round(this.getValue() * 100.0));
         }

         @Override
         public void setFromEditString(String var1) {
            this.q(Double.parseDouble(var1) / 100.0);
         }
      };
   }

   public interface m {
      uu placementHighlight();
   }

   private record n(List<e2.w> boxes, int color, double opacity, double width, double fillOpacity, double glow) {
   }
}

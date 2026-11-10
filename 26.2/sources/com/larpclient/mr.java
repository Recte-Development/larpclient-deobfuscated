package com.larpclient;

import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

public final class mr {
   private static final su X = su.build(Shapes.block());
   private final Module L;
   private final com.larpclient.module.s T = new com.larpclient.module.s("Highlight", "Show the next placement with a soft block outline", true);
   private final com.larpclient.module.l Y = new com.larpclient.module.l(
      "Highlight Color Mode", "Follow your theme or choose a custom color", "Theme", "Theme", "Custom"
   );
   private final com.larpclient.module.i u = new com.larpclient.module.i("Highlight Color", "Custom placement highlight color", ho.J);
   private final com.larpclient.module.e n = T("Highlight Opacity", "Outline visibility", 0.85, 0.1, 1.0, 0.05);
   private final com.larpclient.module.e c = new com.larpclient.module.e("Highlight Thickness", "Outline width in blocks", 0.015, 0.005, 0.04, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   };
   private final com.larpclient.module.s M = new com.larpclient.module.s("Highlight Fill", "Subtle tint inside the placement box", true);
   private final com.larpclient.module.e x = T("Highlight Fill Opacity", "Keep the scene visible through the fill", 0.08, 0.01, 0.3, 0.01);
   private final com.larpclient.module.e a = T("Highlight Glow", "Soft edge halo and shared quality-controlled bloom", 0.65, 0.0, 1.5, 0.05);
   private final com.larpclient.module.e o;
   private final com.larpclient.module.e p;
   private final List<com.larpclient.module.q<?>> k;
   private final ue P = new ue();
   private final RenderStateDataKey<mr.o> G;
   private BlockPos K;
   private Object R;
   private Object A;
   private Object V;
   private Object t;
   private int H;
   private int E = -1;
   private Vec3 D;

   public mr(Module var1, Consumer<com.larpclient.module.q<?>> var2) {
      this(var1, var2, -1.0, -1.0);
   }

   public mr(Module var1, Consumer<com.larpclient.module.q<?>> var2, double var3, double var5) {
      this.L = var1;
      this.o = var3 < 0.0 ? null : V("Highlight Hold", "Seconds to retain the previous placement box", var3, 0.0, 3.0);
      this.p = var5 < 0.0 ? null : V("Highlight Fade", "Seconds for the previous box to fade out", var5, 0.1, 2.0);
      ArrayList var7 = new ArrayList<>(List.of(this.T, this.Y, this.u, this.n, this.c, this.M, this.x, this.a));
      if (this.o != null) {
         var7.add(this.o);
         var7.add(this.p);
      }

      this.k = List.copyOf(var7);
      this.G = RenderStateDataKey.create(() -> var1.getName() + " placement highlight");
      this.k.forEach(var2);

      for (com.larpclient.module.q var9 : this.k) {
         if (var9 != this.T) {
            var9.P(this.T::getValue);
         }
      }

      this.u.P(() -> this.T.getValue() && this.Y.O("Custom"));
      this.x.P(() -> this.T.getValue() && this.M.getValue());
      this.T.o(var1x -> this.c());
   }

   public boolean B() {
      return this.T.getValue();
   }

   public String Z() {
      return this.L.getName() + " highlight";
   }

   public List<com.larpclient.module.q<?>> c() {
      return this.k;
   }

   public void q(BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      this.K = this.T.getValue() && var1 != null ? var1.immutable() : null;
      this.R = var2.level;
      this.A = var2.player;
      this.H = var2.player == null ? 0 : var2.player.tickCount;
   }

   public void c() {
      this.K = null;
      this.R = this.A = this.V = this.t = null;
      this.D = null;
      this.P.Y();
   }

   public void t() {
      this.c();
      h.A(
         this.L.getName(),
         "Placement highlight {}; theme/custom color, world-fixed fades, shared depth-tested glow",
         this.T.getValue() ? "enabled" : "disabled"
      );
   }

   public void T(LevelExtractionContext var1) {
      LevelRenderState var2 = var1.levelState();
      var2.setData(this.G, null);
      Minecraft var3 = Minecraft.getInstance();
      if (this.L.isRuntimeActive()
         && this.T.getValue()
         && var3.player != null
         && var3.player.isAlive()
         && var3.gui.screen() == null
         && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && var3.isWindowActive()) {
         Vec3 var4 = var1.camera().position();
         int var5 = this.D != null && this.D.distanceTo(var4) > 8.0 ? 1 : 0;
         if (this.V != var1.level() || this.t != var3.player || this.E != wa.s || var5 != 0) {
            this.P.Y();
            if (var5 != 0) {
               this.K = null;
            }

            this.V = var1.level();
            this.t = var3.player;
            this.E = wa.s;
         }

         this.D = var4;
         BlockPos var6 = t1.k(this.R, var3.level, this.A, var3.player, this.H, var3.player.tickCount) ? this.K : null;
         List var7 = this.P.N(var6, cf.F(), this.o == null ? 0.06 : this.o.getValue(), this.p == null ? 0.18 : this.p.getValue());
         if (!var7.isEmpty()) {
            var2.setData(
               this.G,
               new mr.o(
                  var7,
                  this.Y.O("Theme") ? ho.J : this.u.getValue(),
                  this.n.getValue(),
                  this.c.getValue(),
                  this.M.getValue() ? this.x.getValue() : 0.0,
                  this.a.getValue()
               )
            );
         }
      } else {
         this.c();
      }
   }

   private mr.o C(LevelRenderState var1) {
      return (mr.o)var1.getData(this.G);
   }

   public boolean S(LevelRenderState var1) {
      mr.o var2 = this.C(var1);
      return this.L.isRuntimeActive() && this.T.getValue() && var2 != null && var2.glow > 0.0;
   }

   public void H(kj var1, boolean var2, LevelRenderState var3) {
      mr.o var4 = this.C(var3);
      if (this.L.isRuntimeActive() && this.T.getValue() && var4 != null && (!var2 || !(var4.glow <= 0.0))) {
         long var5 = zj.m();

         for (ue.l var8 : var4.boxes) {
            m5.s(var1, var2, X, new AABB(var8.pos()).inflate(0.002), var8.alpha(), var4.color, var4.opacity, var4.width, var4.fillOpacity, var4.glow);
         }

         zj.l(this.L.getName() + (var2 ? " highlight glow" : " highlight base"), var5);
      }
   }

   private static com.larpclient.module.e V(String var0, String var1, double var2, double var4, double var6) {
      return new com.larpclient.module.e(var0, var1, var2, var4, var6, 0.05) {
         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + " s";
         }
      };
   }

   private static com.larpclient.module.e T(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new com.larpclient.module.e(var0, var1, var2, var4, var6, var8) {
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
            this.X(Double.parseDouble(var1) / 100.0);
         }
      };
   }

   public interface n {
      mr placementHighlight();
   }

   private record o(List<ue.l> boxes, int color, double opacity, double width, double fillOpacity, double glow) {
   }
}

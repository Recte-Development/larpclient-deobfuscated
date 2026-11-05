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

public final class hy {
   private static final d4 x = d4.build(Shapes.block());
   private final Module X;
   private final com.larpclient.module.ln R = new com.larpclient.module.ln("Highlight", "Show the next placement with a soft block outline", true);
   private final com.larpclient.module.lp N = new com.larpclient.module.lp(
      "Highlight Color Mode", "Follow your theme or choose a custom color", "Theme", "Theme", "Custom"
   );
   private final com.larpclient.module.lv K = new com.larpclient.module.lv("Highlight Color", "Custom placement highlight color", rv.e);
   private final com.larpclient.module.lc z = s("Highlight Opacity", "Outline visibility", 0.85, 0.1, 1.0, 0.05);
   private final com.larpclient.module.lc c = new com.larpclient.module.lc("Highlight Thickness", "Outline width in blocks", 0.015, 0.005, 0.04, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   };
   private final com.larpclient.module.ln G = new com.larpclient.module.ln("Highlight Fill", "Subtle tint inside the placement box", true);
   private final com.larpclient.module.lc u = s("Highlight Fill Opacity", "Keep the scene visible through the fill", 0.08, 0.01, 0.3, 0.01);
   private final com.larpclient.module.lc S = s("Highlight Glow", "Soft edge halo and shared quality-controlled bloom", 0.65, 0.0, 1.5, 0.05);
   private final com.larpclient.module.lc J;
   private final com.larpclient.module.lc b;
   private final List<com.larpclient.module.l6<?>> q;
   private final d7 n = new d7();
   private final RenderStateDataKey<hy.e> U;
   private BlockPos T;
   private Object l;
   private Object M;
   private Object o;
   private Object I;
   private int Y;
   private int D = -1;
   private Vec3 h;

   public hy(Module var1, Consumer<com.larpclient.module.l6<?>> var2) {
      this(var1, var2, -1.0, -1.0);
   }

   public hy(Module var1, Consumer<com.larpclient.module.l6<?>> var2, double var3, double var5) {
      this.X = var1;
      this.J = var3 < 0.0 ? null : C("Highlight Hold", "Seconds to retain the previous placement box", var3, 0.0, 3.0);
      this.b = var5 < 0.0 ? null : C("Highlight Fade", "Seconds for the previous box to fade out", var5, 0.1, 2.0);
      ArrayList var7 = new ArrayList<>(List.of(this.R, this.N, this.K, this.z, this.c, this.G, this.u, this.S));
      if (this.J != null) {
         var7.add(this.J);
         var7.add(this.b);
      }

      this.q = List.copyOf(var7);
      this.U = RenderStateDataKey.create(() -> var1.getName() + " placement highlight");
      this.q.forEach(var2);

      for (com.larpclient.module.l6 var9 : this.q) {
         if (var9 != this.R) {
            var9.x(this.R::getValue);
         }
      }

      this.K.x(() -> this.R.getValue() && this.N.i("Custom"));
      this.u.x(() -> this.R.getValue() && this.G.getValue());
      this.R.W(var1x -> this.C());
   }

   public boolean z() {
      return this.R.getValue();
   }

   public String C() {
      return this.X.getName() + " highlight";
   }

   public List<com.larpclient.module.l6<?>> c() {
      return this.q;
   }

   public void i(BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      this.T = this.R.getValue() && var1 != null ? var1.immutable() : null;
      this.l = var2.level;
      this.M = var2.player;
      this.Y = var2.player == null ? 0 : var2.player.tickCount;
   }

   public void C() {
      this.T = null;
      this.l = this.M = this.o = this.I = null;
      this.h = null;
      this.n.n();
   }

   public void S() {
      this.C();
      r4.V(
         this.X.getName(),
         "Placement highlight {}; theme/custom color, world-fixed fades, shared depth-tested glow",
         this.R.getValue() ? "enabled" : "disabled"
      );
   }

   public void k(LevelExtractionContext var1) {
      LevelRenderState var2 = var1.levelState();
      var2.setData(this.U, null);
      Minecraft var3 = Minecraft.getInstance();
      if (this.X.isRuntimeActive()
         && this.R.getValue()
         && var3.player != null
         && var3.player.isAlive()
         && var3.gui.screen() == null
         && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && var3.isWindowActive()) {
         Vec3 var4 = var1.camera().position();
         int var5 = this.h != null && this.h.distanceTo(var4) > 8.0 ? 1 : 0;
         if (this.o != var1.level() || this.I != var3.player || this.D != jk.v || var5 != 0) {
            this.n.n();
            if (var5 != 0) {
               this.T = null;
            }

            this.o = var1.level();
            this.I = var3.player;
            this.D = jk.v;
         }

         this.h = var4;
         BlockPos var6 = m.X(this.l, var3.level, this.M, var3.player, this.Y, var3.player.tickCount) ? this.T : null;
         List var7 = this.n.P(var6, ea.y(), this.J == null ? 0.06 : this.J.getValue(), this.b == null ? 0.18 : this.b.getValue());
         if (!var7.isEmpty()) {
            var2.setData(
               this.U,
               new hy.e(
                  var7,
                  this.N.i("Theme") ? rv.e : this.K.getValue(),
                  this.z.getValue(),
                  this.c.getValue(),
                  this.G.getValue() ? this.u.getValue() : 0.0,
                  this.S.getValue()
               )
            );
         }
      } else {
         this.C();
      }
   }

   private hy.e s(LevelRenderState var1) {
      return (hy.e)var1.getData(this.U);
   }

   public boolean u(LevelRenderState var1) {
      hy.e var2 = this.s(var1);
      return this.X.isRuntimeActive() && this.R.getValue() && var2 != null && var2.glow > 0.0;
   }

   public void H(r6 var1, boolean var2, LevelRenderState var3) {
      hy.e var4 = this.s(var3);
      if (this.X.isRuntimeActive() && this.R.getValue() && var4 != null && (!var2 || !(var4.glow <= 0.0))) {
         long var5 = h_.U();

         for (d7.r var8 : var4.boxes) {
            md.C(var1, var2, x, new AABB(var8.pos()).inflate(0.002), var8.alpha(), var4.color, var4.opacity, var4.width, var4.fillOpacity, var4.glow);
         }

         h_.n(this.X.getName() + (var2 ? " highlight glow" : " highlight base"), var5);
      }
   }

   private static com.larpclient.module.lc C(String var0, String var1, double var2, double var4, double var6) {
      return new com.larpclient.module.lc(var0, var1, var2, var4, var6, 0.05) {
         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + " s";
         }
      };
   }

   private static com.larpclient.module.lc s(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new com.larpclient.module.lc(var0, var1, var2, var4, var6, var8) {
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
            this.C(Double.parseDouble(var1) / 100.0);
         }
      };
   }

   public interface c {
      hy placementHighlight();
   }

   private record e(List<d7.r> boxes, int color, double opacity, double width, double fillOpacity, double glow) {
   }
}

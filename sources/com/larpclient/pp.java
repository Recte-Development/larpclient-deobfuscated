package com.larpclient;

import com.larpclient.module.Module;
import com.larpclient.module.df;
import com.larpclient.module.dh;
import com.larpclient.module.dk;
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

public final class pp {
   private static final hf d = hf.build(Shapes.block());
   private final Module i;
   private final dh h = new dh("Highlight", "Show the next placement with a soft block outline", true);
   private final com.larpclient.module.d6 N = new com.larpclient.module.d6(
      "Highlight Color Mode", "Follow your theme or choose a custom color", "Theme", "Theme", "Custom"
   );
   private final dk E = new dk("Highlight Color", "Custom placement highlight color", com.larpclient.f.C);
   private final com.larpclient.module.d9 A = i("Highlight Opacity", "Outline visibility", 0.85, 0.1, 1.0, 0.05);
   private final com.larpclient.module.d9 q = new com.larpclient.module.d9("Highlight Thickness", "Outline width in blocks", 0.015, 0.005, 0.04, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.P());
      }
   };
   private final dh H = new dh("Highlight Fill", "Subtle tint inside the placement box", true);
   private final com.larpclient.module.d9 l = i("Highlight Fill Opacity", "Keep the scene visible through the fill", 0.08, 0.01, 0.3, 0.01);
   private final com.larpclient.module.d9 C = i("Highlight Glow", "Soft edge halo and shared quality-controlled bloom", 0.65, 0.0, 1.5, 0.05);
   private final com.larpclient.module.d9 t;
   private final com.larpclient.module.d9 k;
   private final List<df<?>> e;
   private final wi S = new wi();
   private final RenderStateDataKey<pp.s> U;
   private BlockPos f;
   private Object G;
   private Object z;
   private Object J;
   private Object w;
   private int D;
   private int V = -1;
   private Vec3 F;

   public pp(Module var1, Consumer<df<?>> var2) {
      this(var1, var2, -1.0, -1.0);
   }

   public pp(Module var1, Consumer<df<?>> var2, double var3, double var5) {
      this.i = var1;
      this.t = var3 < 0.0 ? null : E("Highlight Hold", "Seconds to retain the previous placement box", var3, 0.0, 3.0);
      this.k = var5 < 0.0 ? null : E("Highlight Fade", "Seconds for the previous box to fade out", var5, 0.1, 2.0);
      ArrayList var7 = new ArrayList<>(List.of(this.h, this.N, this.E, this.A, this.q, this.H, this.l, this.C));
      if (this.t != null) {
         var7.add(this.t);
         var7.add(this.k);
      }

      this.e = List.copyOf(var7);
      this.U = RenderStateDataKey.create(() -> var1.getName() + " placement highlight");
      this.e.forEach(var2);

      for (df var9 : this.e) {
         if (var9 != this.h) {
            var9.H(this.h::P);
         }
      }

      this.E.H(() -> this.h.P() && this.N.S("Custom"));
      this.l.H(() -> this.h.P() && this.H.P());
      this.h.h(var1x -> this.P());
   }

   public boolean F() {
      return this.h.P();
   }

   public String u() {
      return this.i.getName() + " highlight";
   }

   public List<df<?>> t() {
      return this.e;
   }

   public void m(BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      this.f = this.h.P() && var1 != null ? var1.immutable() : null;
      this.G = var2.level;
      this.z = var2.player;
      this.D = var2.player == null ? 0 : var2.player.tickCount;
   }

   public void P() {
      this.f = null;
      this.G = this.z = this.J = this.w = null;
      this.F = null;
      this.S.P();
   }

   public void w() {
      this.P();
      mt.K(this.i.getName(), "Placement highlight {}; theme/custom color, world-fixed fades, shared depth-tested glow", this.h.P() ? "enabled" : "disabled");
   }

   public void Y(LevelExtractionContext var1) {
      LevelRenderState var2 = var1.levelState();
      var2.setData(this.U, null);
      Minecraft var3 = Minecraft.getInstance();
      if (this.i.isRuntimeActive()
         && this.h.P()
         && var3.player != null
         && var3.player.isAlive()
         && var3.gui.screen() == null
         && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && var3.isWindowActive()) {
         Vec3 var4 = var1.camera().position();
         int var5 = this.F != null && this.F.distanceTo(var4) > 8.0 ? 1 : 0;
         if (this.J != var1.level() || this.w != var3.player || this.V != m5.k || var5 != 0) {
            this.S.P();
            if (var5 != 0) {
               this.f = null;
            }

            this.J = var1.level();
            this.w = var3.player;
            this.V = m5.k;
         }

         this.F = var4;
         BlockPos var6 = _d.o(this.G, var3.level, this.z, var3.player, this.D, var3.player.tickCount) ? this.f : null;
         List var7 = this.S.i(var6, ty.K(), this.t == null ? 0.06 : this.t.P(), this.k == null ? 0.18 : this.k.P());
         if (!var7.isEmpty()) {
            var2.setData(
               this.U, new pp.s(var7, this.N.S("Theme") ? com.larpclient.f.C : this.E.P(), this.A.P(), this.q.P(), this.H.P() ? this.l.P() : 0.0, this.C.P())
            );
         }
      } else {
         this.P();
      }
   }

   private pp.s q(LevelRenderState var1) {
      return (pp.s)var1.getData(this.U);
   }

   public boolean j(LevelRenderState var1) {
      pp.s var2 = this.q(var1);
      return this.i.isRuntimeActive() && this.h.P() && var2 != null && var2.glow > 0.0;
   }

   public void t(ki var1, boolean var2, LevelRenderState var3) {
      pp.s var4 = this.q(var3);
      if (this.i.isRuntimeActive() && this.h.P() && var4 != null && (!var2 || !(var4.glow <= 0.0))) {
         long var5 = kx.G();

         for (wi.k var8 : var4.boxes) {
            mx.N(var1, var2, d, new AABB(var8.pos()).inflate(0.002), var8.alpha(), var4.color, var4.opacity, var4.width, var4.fillOpacity, var4.glow);
         }

         kx.u(this.i.getName() + (var2 ? " highlight glow" : " highlight base"), var5);
      }
   }

   private static com.larpclient.module.d9 E(String var0, String var1, double var2, double var4, double var6) {
      return new com.larpclient.module.d9(var0, var1, var2, var4, var6, 0.05) {
         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + " s";
         }
      };
   }

   private static com.larpclient.module.d9 i(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new com.larpclient.module.d9(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + "%";
         }

         @Override
         public String getEditValueAsString() {
            return Long.toString(Math.round(this.P() * 100.0));
         }

         @Override
         public void setFromEditString(String var1) {
            this.o(Double.parseDouble(var1) / 100.0);
         }
      };
   }

   private record s(List<wi.k> boxes, int color, double opacity, double width, double fillOpacity, double glow) {
   }

   public interface x {
      pp placementHighlight();
   }
}

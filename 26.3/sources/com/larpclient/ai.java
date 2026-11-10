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

public final class ai {
   private static final fa d = fa.build(Shapes.block());
   private final Module q;
   private final com.larpclient.module.m h = new com.larpclient.module.m("Highlight", "Show the next placement with a soft block outline", true);
   private final com.larpclient.module.o A = new com.larpclient.module.o(
      "Highlight Color Mode", "Follow your theme or choose a custom color", "Theme", "Theme", "Custom"
   );
   private final com.larpclient.module.h F = new com.larpclient.module.h("Highlight Color", "Custom placement highlight color", c.y);
   private final com.larpclient.module.b B = E("Highlight Opacity", "Outline visibility", 0.85, 0.1, 1.0, 0.05);
   private final com.larpclient.module.b W = new com.larpclient.module.b("Highlight Thickness", "Outline width in blocks", 0.015, 0.005, 0.04, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   };
   private final com.larpclient.module.m t = new com.larpclient.module.m("Highlight Fill", "Subtle tint inside the placement box", true);
   private final com.larpclient.module.b f = E("Highlight Fill Opacity", "Keep the scene visible through the fill", 0.08, 0.01, 0.3, 0.01);
   private final com.larpclient.module.b a = E("Highlight Glow", "Soft edge halo and shared quality-controlled bloom", 0.65, 0.0, 1.5, 0.05);
   private final com.larpclient.module.b Q;
   private final com.larpclient.module.b K;
   private final List<com.larpclient.module.r<?>> N;
   private final jb V = new jb();
   private final RenderStateDataKey<ai.f> p;
   private BlockPos Z;
   private Object w;
   private Object y;
   private Object S;
   private Object O;
   private int E;
   private int b = -1;
   private Vec3 U;

   public ai(Module var1, Consumer<com.larpclient.module.r<?>> var2) {
      this(var1, var2, -1.0, -1.0);
   }

   public ai(Module var1, Consumer<com.larpclient.module.r<?>> var2, double var3, double var5) {
      this.q = var1;
      this.Q = var3 < 0.0 ? null : Y("Highlight Hold", "Seconds to retain the previous placement box", var3, 0.0, 3.0);
      this.K = var5 < 0.0 ? null : Y("Highlight Fade", "Seconds for the previous box to fade out", var5, 0.1, 2.0);
      ArrayList var7 = new ArrayList<>(List.of(this.h, this.A, this.F, this.B, this.W, this.t, this.f, this.a));
      if (this.Q != null) {
         var7.add(this.Q);
         var7.add(this.K);
      }

      this.N = List.copyOf(var7);
      this.p = RenderStateDataKey.create(() -> var1.getName() + " placement highlight");
      this.N.forEach(var2);

      for (com.larpclient.module.r var9 : this.N) {
         if (var9 != this.h) {
            var9.a(this.h::getValue);
         }
      }

      this.F.a(() -> this.h.getValue() && this.A.w("Custom"));
      this.f.a(() -> this.h.getValue() && this.t.getValue());
      this.h.w(var1x -> this.m());
   }

   public boolean D() {
      return this.h.getValue();
   }

   public String M() {
      return this.q.getName() + " highlight";
   }

   public List<com.larpclient.module.r<?>> g() {
      return this.N;
   }

   public void H(BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      this.Z = this.h.getValue() && var1 != null ? var1.immutable() : null;
      this.w = var2.level;
      this.y = var2.player;
      this.E = var2.player == null ? 0 : var2.player.tickCount;
   }

   public void m() {
      this.Z = null;
      this.w = this.y = this.S = this.O = null;
      this.U = null;
      this.V.E();
   }

   public void b() {
      this.m();
      _q.z(
         this.q.getName(),
         "Placement highlight {}; theme/custom color, world-fixed fades, shared depth-tested glow",
         this.h.getValue() ? "enabled" : "disabled"
      );
   }

   public void v(LevelExtractionContext var1) {
      LevelRenderState var2 = var1.levelState();
      var2.setData(this.p, null);
      Minecraft var3 = Minecraft.getInstance();
      if (this.q.isRuntimeActive()
         && this.h.getValue()
         && var3.player != null
         && var3.player.isAlive()
         && var3.gui.screen() == null
         && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && var3.isWindowActive()) {
         Vec3 var4 = var1.camera().position();
         int var5 = this.U != null && this.U.distanceTo(var4) > 8.0 ? 1 : 0;
         if (this.S != var1.level() || this.O != var3.player || this.b != nl.q || var5 != 0) {
            this.V.E();
            if (var5 != 0) {
               this.Z = null;
            }

            this.S = var1.level();
            this.O = var3.player;
            this.b = nl.q;
         }

         this.U = var4;
         BlockPos var6 = vi.A(this.w, var3.level, this.y, var3.player, this.E, var3.player.tickCount) ? this.Z : null;
         List var7 = this.V.B(var6, hz.Q(), this.Q == null ? 0.06 : this.Q.getValue(), this.K == null ? 0.18 : this.K.getValue());
         if (!var7.isEmpty()) {
            var2.setData(
               this.p,
               new ai.f(
                  var7,
                  this.A.w("Theme") ? c.y : this.F.getValue(),
                  this.B.getValue(),
                  this.W.getValue(),
                  this.t.getValue() ? this.f.getValue() : 0.0,
                  this.a.getValue()
               )
            );
         }
      } else {
         this.m();
      }
   }

   private ai.f H(LevelRenderState var1) {
      return (ai.f)var1.getData(this.p);
   }

   public boolean b(LevelRenderState var1) {
      ai.f var2 = this.H(var1);
      return this.q.isRuntimeActive() && this.h.getValue() && var2 != null && var2.glow > 0.0;
   }

   public void r(fz var1, boolean var2, LevelRenderState var3) {
      ai.f var4 = this.H(var3);
      if (this.q.isRuntimeActive() && this.h.getValue() && var4 != null && (!var2 || !(var4.glow <= 0.0))) {
         long var5 = he.s();

         for (jb.r var8 : var4.boxes) {
            com.larpclient.b.t(
               var1, var2, d, new AABB(var8.pos()).inflate(0.002), var8.alpha(), var4.color, var4.opacity, var4.width, var4.fillOpacity, var4.glow
            );
         }

         he.T(this.q.getName() + (var2 ? " highlight glow" : " highlight base"), var5);
      }
   }

   private static com.larpclient.module.b Y(String var0, String var1, double var2, double var4, double var6) {
      return new com.larpclient.module.b(var0, var1, var2, var4, var6, 0.05) {
         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + " s";
         }
      };
   }

   private static com.larpclient.module.b E(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new com.larpclient.module.b(var0, var1, var2, var4, var6, var8) {
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
            this.O(Double.parseDouble(var1) / 100.0);
         }
      };
   }

   public interface e {
      ai placementHighlight();
   }

   private record f(List<jb.r> boxes, int color, double opacity, double width, double fillOpacity, double glow) {
   }
}

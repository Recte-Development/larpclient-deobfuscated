package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.fa;
import com.larpclient.fz;
import com.larpclient.he;
import com.larpclient.hz;
import com.larpclient.ja;
import com.larpclient.nl;
import com.larpclient.wp;
import java.util.List;
import java.util.Locale;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class BlockOverlayModule extends Module {
   private static BlockOverlayModule Y;
   private final b N = this.addSetting(new b("Transition", "Time to glide to another block, in milliseconds; 0 is instant", 240.0, 0.0, 300.0, 10.0));
   private final o u = this.addSetting(new o("Color Mode", "Use your interface accent or a custom color", "Theme", "Theme", "Custom"));
   private final h k = this.addSetting(new h("Color", "Custom highlight color", -1381654));
   private final b R = this.addSetting(n("Outline Opacity", "Outline visibility", 0.85, 0.1, 1.0, 0.05));
   private final b X = this.addSetting(new b("Outline Thickness", "Stroke width in blocks", 0.015, 0.005, 0.05, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   });
   private final m Q = this.addSetting(new m("Fill", "Subtle tint over the selected shape", true));
   private final b a = this.addSetting(n("Fill Opacity", "Keep the block texture visible", 0.15, 0.01, 0.3, 0.01));
   private final b T = this.addSetting(n("Glow", "Soft edge halo and shared quality-controlled bloom", 1.5, 0.0, 1.5, 0.05));
   private final ja i = new ja();
   private Object n;
   private Object t;
   private Vec3 s;
   private VoxelShape W;
   private fa e;
   private BlockPos H;
   private int O = -1;
   private boolean r;
   private boolean L;
   private static final RenderStateDataKey<BlockOverlayModule.p> S = RenderStateDataKey.create(() -> "Larp block overlay");

   public BlockOverlayModule() {
      super("Block Overlay", "Highlight the block you aim at", Category.VISUALS, -1);
      Y = this;
      this.k.a(() -> this.u.w("Custom"));
      this.a.a(this.Q::getValue);
   }

   private static b n(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new b(var0, var1, var2, var4, var6, var8) {
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

   public static BlockOverlayModule c() {
      return Y;
   }

   public List<r<?>> w() {
      return List.of(this.u, this.k, this.R, this.X, this.Q, this.a, this.T);
   }

   public List<r<?>> T() {
      return List.of(this.N);
   }

   @Override
   public void onEnable() {
      this.N();
      _q.z("BlockOverlay", "Selection overlay enabled; cached native shapes, interruptible {} ms transition, shared glow", this.N.getValue());
   }

   @Override
   public void onDisable() {
      this.N();
   }

   public void N() {
      this.i.F();
      this.e = null;
      this.W = null;
      this.H = null;
      this.n = this.t = null;
      this.s = null;
      this.r = this.L = (boolean)0;
   }

   public void e(LevelExtractionContext var1, HitResult var2) {
      if (this.isEnabled()) {
         LevelRenderState var3 = var1.levelState();
         var3.setData(S, null);
         if (!this.isRuntimeActive()) {
            this.N();
         } else {
            Minecraft var4 = getMc();
            if (var4.gui.screen() == null
               && !var4.gameRenderer.gameRenderState().guiRenderState.isHudHidden
               && var4.isWindowActive()
               && var4.player != null
               && var4.player.isAlive()) {
               Vec3 var5 = var1.camera().position();
               if (this.n != var1.level() || this.t != var4.player || this.O != nl.q || this.s != null && this.s.distanceTo(var5) > 8.0) {
                  this.N();
                  this.n = var1.level();
                  this.t = var4.player;
                  this.O = nl.q;
               }

               this.s = var5;
               if (!this.r) {
                  try {
                     BlockOutlineRenderState var6 = var1.levelState().blockOutlineRenderState;
                     AABB var7 = null;
                     this.H = null;
                     if (var6 != null && var2 != null && var2.getType() == Type.BLOCK) {
                        VoxelShape var8 = var6.shape();
                        this.e = this.x(var8);
                        if (this.e != null) {
                           this.H = var6.pos();
                           var7 = this.e.bounds().move(this.H);
                        } else if (!this.L) {
                           this.L = (boolean)1;
                           _q.z("BlockOverlay", "Empty or complex selection shape; keeping native outline (limits: {} edges, {} boxes)", 128, 32);
                        }
                     }

                     ja.i var11 = this.i.Q(var7, hz.Q(), this.N.getValue() / 1000.0);
                     BlockOverlayModule.p var9 = this.e != null && var11.bounds() != null
                        ? new BlockOverlayModule.p(
                           this.H,
                           this.e,
                           var11.bounds().inflate(0.002),
                           var11.alpha(),
                           this.u.w("Theme") ? com.larpclient.c.y : this.k.getValue(),
                           this.R.getValue(),
                           this.X.getValue(),
                           this.Q.getValue() ? this.a.getValue() : 0.0,
                           this.T.getValue()
                        )
                        : null;
                     var3.setData(S, var9);
                  } catch (RuntimeException var10) {
                     this.r = (boolean)1;
                     this.H = null;
                     _q.o("BlockOverlay", "Selection extraction failed; native outline retained, retry on resource reload", var10);
                  }
               }
            } else {
               this.N();
            }
         }
      }
   }

   private fa x(VoxelShape var1) {
      if (var1 != this.W) {
         this.e = fa.build(var1);
         this.W = var1;
      }

      return this.e;
   }

   private static BlockOverlayModule.p R(LevelRenderState var0) {
      return (BlockOverlayModule.p)var0.getData(S);
   }

   public boolean R(BlockPos var1, LevelRenderState var2) {
      if (!this.isEnabled()) {
         return false;
      }

      BlockOverlayModule.p var3 = R(var2);
      return this.isRuntimeActive() && !this.r && wp.Q() && var3 != null && var1.equals(var3.selected);
   }

   public boolean t(LevelRenderState var1) {
      if (!this.isEnabled()) {
         return false;
      }

      BlockOverlayModule.p var2 = R(var1);
      return this.isRuntimeActive() && var2 != null && var2.alpha > 0.001 && var2.glow > 0.0;
   }

   public void l(fz var1, boolean var2, LevelRenderState var3) {
      if (this.isEnabled()) {
         BlockOverlayModule.p var4 = R(var3);
         if (this.isRuntimeActive() && var4 != null && !(var4.alpha < 0.001) && (!var2 || !(var4.glow <= 0.0))) {
            long var5 = he.s();
            com.larpclient.b.t(var1, var2, var4.geometry, var4.bounds, var4.alpha, var4.color, var4.opacity, var4.width, var4.fillOpacity, var4.glow);
            he.T(var2 ? "BlockOverlay glow" : "BlockOverlay base", var5);
         }
      }
   }

   private record p(BlockPos selected, fa geometry, AABB bounds, double alpha, int color, double opacity, double width, double fillOpacity, double glow) {
   }
}

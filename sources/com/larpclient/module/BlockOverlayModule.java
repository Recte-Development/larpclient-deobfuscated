package com.larpclient.module;

import com.larpclient._f;
import com.larpclient.d4;
import com.larpclient.ea;
import com.larpclient.f8;
import com.larpclient.h_;
import com.larpclient.jk;
import com.larpclient.md;
import com.larpclient.r4;
import com.larpclient.r6;
import com.larpclient.rv;
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
   private static BlockOverlayModule l;
   private final lc W = this.addSetting(new lc("Transition", "Time to glide to another block, in milliseconds; 0 is instant", 240.0, 0.0, 300.0, 10.0));
   private final lp q = this.addSetting(new lp("Color Mode", "Use your interface accent or a custom color", "Theme", "Theme", "Custom"));
   private final lv A = this.addSetting(new lv("Color", "Custom highlight color", -1381654));
   private final lc y = this.addSetting(E("Outline Opacity", "Outline visibility", 0.85, 0.1, 1.0, 0.05));
   private final lc m = this.addSetting(new lc("Outline Thickness", "Stroke width in blocks", 0.015, 0.005, 0.05, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   });
   private final ln E = this.addSetting(new ln("Fill", "Subtle tint over the selected shape", true));
   private final lc F = this.addSetting(E("Fill Opacity", "Keep the block texture visible", 0.15, 0.01, 0.3, 0.01));
   private final lc M = this.addSetting(E("Glow", "Soft edge halo and shared quality-controlled bloom", 1.5, 0.0, 1.5, 0.05));
   private final _f p = new _f();
   private Object s;
   private Object Z;
   private Vec3 o;
   private VoxelShape K;
   private d4 B;
   private BlockPos n;
   private int Q = -1;
   private boolean I;
   private boolean R;
   private static final RenderStateDataKey<BlockOverlayModule.u> U = RenderStateDataKey.create(() -> "Larp block overlay");

   public BlockOverlayModule() {
      super("Block Overlay", "Highlight the block you aim at", Category.VISUALS, -1);
      l = this;
      this.A.x(() -> this.q.i("Custom"));
      this.F.x(this.E::getValue);
   }

   private static lc E(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new lc(var0, var1, var2, var4, var6, var8) {
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

   public static BlockOverlayModule z() {
      return l;
   }

   public List<l6<?>> b() {
      return List.of(this.q, this.A, this.y, this.m, this.E, this.F, this.M);
   }

   public List<l6<?>> S() {
      return List.of(this.W);
   }

   @Override
   public void onEnable() {
      this.A();
      r4.V("BlockOverlay", "Selection overlay enabled; cached native shapes, interruptible {} ms transition, shared glow", this.W.getValue());
   }

   @Override
   public void onDisable() {
      this.A();
   }

   public void A() {
      this.p.t();
      this.B = null;
      this.K = null;
      this.n = null;
      this.s = this.Z = null;
      this.o = null;
      this.I = this.R = (boolean)0;
   }

   public void j(LevelExtractionContext var1, HitResult var2) {
      if (this.isEnabled()) {
         LevelRenderState var3 = var1.levelState();
         var3.setData(U, null);
         if (!this.isRuntimeActive()) {
            this.A();
         } else {
            Minecraft var4 = getMc();
            if (var4.gui.screen() == null
               && !var4.gameRenderer.gameRenderState().guiRenderState.isHudHidden
               && var4.isWindowActive()
               && var4.player != null
               && var4.player.isAlive()) {
               Vec3 var5 = var1.camera().position();
               if (this.s != var1.level() || this.Z != var4.player || this.Q != jk.v || this.o != null && this.o.distanceTo(var5) > 8.0) {
                  this.A();
                  this.s = var1.level();
                  this.Z = var4.player;
                  this.Q = jk.v;
               }

               this.o = var5;
               if (!this.I) {
                  try {
                     BlockOutlineRenderState var6 = var1.levelState().blockOutlineRenderState;
                     AABB var7 = null;
                     this.n = null;
                     if (var6 != null && var2 != null && var2.getType() == Type.BLOCK) {
                        VoxelShape var8 = var6.shape();
                        this.B = this.d(var8);
                        if (this.B != null) {
                           this.n = var6.pos();
                           var7 = this.B.bounds().move(this.n);
                        } else if (!this.R) {
                           this.R = (boolean)1;
                           r4.V("BlockOverlay", "Empty or complex selection shape; keeping native outline (limits: {} edges, {} boxes)", 128, 32);
                        }
                     }

                     _f.e var11 = this.p.A(var7, ea.y(), this.W.getValue() / 1000.0);
                     BlockOverlayModule.u var9 = this.B != null && var11.bounds() != null
                        ? new BlockOverlayModule.u(
                           this.n,
                           this.B,
                           var11.bounds().inflate(0.002),
                           var11.alpha(),
                           this.q.i("Theme") ? rv.e : this.A.getValue(),
                           this.y.getValue(),
                           this.m.getValue(),
                           this.E.getValue() ? this.F.getValue() : 0.0,
                           this.M.getValue()
                        )
                        : null;
                     var3.setData(U, var9);
                  } catch (RuntimeException var10) {
                     this.I = (boolean)1;
                     this.n = null;
                     r4.r("BlockOverlay", "Selection extraction failed; native outline retained, retry on resource reload", var10);
                  }
               }
            } else {
               this.A();
            }
         }
      }
   }

   private d4 d(VoxelShape var1) {
      if (var1 != this.K) {
         this.B = d4.build(var1);
         this.K = var1;
      }

      return this.B;
   }

   private static BlockOverlayModule.u E(LevelRenderState var0) {
      return (BlockOverlayModule.u)var0.getData(U);
   }

   public boolean f(BlockPos var1, LevelRenderState var2) {
      if (!this.isEnabled()) {
         return false;
      }

      BlockOverlayModule.u var3 = E(var2);
      return this.isRuntimeActive() && !this.I && f8.c() && var3 != null && var1.equals(var3.selected);
   }

   public boolean e(LevelRenderState var1) {
      if (!this.isEnabled()) {
         return false;
      }

      BlockOverlayModule.u var2 = E(var1);
      return this.isRuntimeActive() && var2 != null && var2.alpha > 0.001 && var2.glow > 0.0;
   }

   public void F(r6 var1, boolean var2, LevelRenderState var3) {
      if (this.isEnabled()) {
         BlockOverlayModule.u var4 = E(var3);
         if (this.isRuntimeActive() && var4 != null && !(var4.alpha < 0.001) && (!var2 || !(var4.glow <= 0.0))) {
            long var5 = h_.U();
            md.C(var1, var2, var4.geometry, var4.bounds, var4.alpha, var4.color, var4.opacity, var4.width, var4.fillOpacity, var4.glow);
            h_.n(var2 ? "BlockOverlay glow" : "BlockOverlay base", var5);
         }
      }
   }

   private record u(BlockPos selected, d4 geometry, AABB bounds, double alpha, int color, double opacity, double width, double fillOpacity, double glow) {
   }
}

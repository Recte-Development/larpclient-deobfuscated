package com.larpclient.module;

import com.larpclient.h;
import com.larpclient.hf;
import com.larpclient.ki;
import com.larpclient.kx;
import com.larpclient.m5;
import com.larpclient.mt;
import com.larpclient.mx;
import com.larpclient.ty;
import com.larpclient.y;
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
   private static BlockOverlayModule F;
   private final d9 b = this.addSetting(new d9("Transition", "Time to glide to another block, in milliseconds; 0 is instant", 240.0, 0.0, 300.0, 10.0));
   private final d6 R = this.addSetting(new d6("Color Mode", "Use your interface accent or a custom color", "Theme", "Theme", "Custom"));
   private final dk d = this.addSetting(new dk("Color", "Custom highlight color", -1381654));
   private final d9 G = this.addSetting(L("Outline Opacity", "Outline visibility", 0.85, 0.1, 1.0, 0.05));
   private final d9 Y = this.addSetting(new d9("Outline Thickness", "Stroke width in blocks", 0.015, 0.005, 0.05, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.P());
      }
   });
   private final dh K = this.addSetting(new dh("Fill", "Subtle tint over the selected shape", true));
   private final d9 j = this.addSetting(L("Fill Opacity", "Keep the block texture visible", 0.15, 0.01, 0.3, 0.01));
   private final d9 e = this.addSetting(L("Glow", "Soft edge halo and shared quality-controlled bloom", 1.5, 0.0, 1.5, 0.05));
   private final y i = new y();
   private Object J;
   private Object U;
   private Vec3 X;
   private VoxelShape x;
   private hf W;
   private BlockPos C;
   private int r = -1;
   private boolean N;
   private boolean w;
   private static final RenderStateDataKey<BlockOverlayModule.n> D = RenderStateDataKey.create(() -> "Larp block overlay");

   public BlockOverlayModule() {
      super("Block Overlay", "Highlight the block you aim at", Category.VISUALS, -1);
      F = this;
      this.d.H(() -> this.R.S("Custom"));
      this.j.H(this.K::P);
   }

   private static d9 L(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new d9(var0, var1, var2, var4, var6, var8) {
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

   public static BlockOverlayModule c() {
      return F;
   }

   public List<df<?>> t() {
      return List.of(this.R, this.d, this.G, this.Y, this.K, this.j, this.e);
   }

   public List<df<?>> e() {
      return List.of(this.b);
   }

   @Override
   public void onEnable() {
      this.r();
      mt.K("BlockOverlay", "Selection overlay enabled; cached native shapes, interruptible {} ms transition, shared glow", this.b.P());
   }

   @Override
   public void onDisable() {
      this.r();
   }

   public void r() {
      this.i.h();
      this.W = null;
      this.x = null;
      this.C = null;
      this.J = this.U = null;
      this.X = null;
      this.N = this.w = (boolean)0;
   }

   public void m(LevelExtractionContext var1, HitResult var2) {
      if (this.isEnabled()) {
         LevelRenderState var3 = var1.levelState();
         var3.setData(D, null);
         if (!this.isRuntimeActive()) {
            this.r();
         } else {
            Minecraft var4 = getMc();
            if (var4.gui.screen() == null
               && !var4.gameRenderer.gameRenderState().guiRenderState.isHudHidden
               && var4.isWindowActive()
               && var4.player != null
               && var4.player.isAlive()) {
               Vec3 var5 = var1.camera().position();
               if (this.J != var1.level() || this.U != var4.player || this.r != m5.k || this.X != null && this.X.distanceTo(var5) > 8.0) {
                  this.r();
                  this.J = var1.level();
                  this.U = var4.player;
                  this.r = m5.k;
               }

               this.X = var5;
               if (!this.N) {
                  try {
                     BlockOutlineRenderState var6 = var1.levelState().blockOutlineRenderState;
                     AABB var7 = null;
                     this.C = null;
                     if (var6 != null && var2 != null && var2.getType() == Type.BLOCK) {
                        VoxelShape var8 = var6.shape();
                        this.W = this.F(var8);
                        if (this.W != null) {
                           this.C = var6.pos();
                           var7 = this.W.bounds().move(this.C);
                        } else if (!this.w) {
                           this.w = (boolean)1;
                           mt.K("BlockOverlay", "Empty or complex selection shape; keeping native outline (limits: {} edges, {} boxes)", 128, 32);
                        }
                     }

                     y.f var11 = this.i.E(var7, ty.K(), this.b.P() / 1000.0);
                     BlockOverlayModule.n var9 = this.W != null && var11.bounds() != null
                        ? new BlockOverlayModule.n(
                           this.C,
                           this.W,
                           var11.bounds().inflate(0.002),
                           var11.alpha(),
                           this.R.S("Theme") ? com.larpclient.f.C : this.d.P(),
                           this.G.P(),
                           this.Y.P(),
                           this.K.P() ? this.j.P() : 0.0,
                           this.e.P()
                        )
                        : null;
                     var3.setData(D, var9);
                  } catch (RuntimeException var10) {
                     this.N = (boolean)1;
                     this.C = null;
                     mt.P("BlockOverlay", "Selection extraction failed; native outline retained, retry on resource reload", var10);
                  }
               }
            } else {
               this.r();
            }
         }
      }
   }

   private hf F(VoxelShape var1) {
      if (var1 != this.x) {
         this.W = hf.build(var1);
         this.x = var1;
      }

      return this.W;
   }

   private static BlockOverlayModule.n s(LevelRenderState var0) {
      return (BlockOverlayModule.n)var0.getData(D);
   }

   public boolean W(BlockPos var1, LevelRenderState var2) {
      if (!this.isEnabled()) {
         return false;
      }

      BlockOverlayModule.n var3 = s(var2);
      return this.isRuntimeActive() && !this.N && h.V() && var3 != null && var1.equals(var3.selected);
   }

   public boolean o(LevelRenderState var1) {
      if (!this.isEnabled()) {
         return false;
      }

      BlockOverlayModule.n var2 = s(var1);
      return this.isRuntimeActive() && var2 != null && var2.alpha > 0.001 && var2.glow > 0.0;
   }

   public void s(ki var1, boolean var2, LevelRenderState var3) {
      if (this.isEnabled()) {
         BlockOverlayModule.n var4 = s(var3);
         if (this.isRuntimeActive() && var4 != null && !(var4.alpha < 0.001) && (!var2 || !(var4.glow <= 0.0))) {
            long var5 = kx.G();
            mx.N(var1, var2, var4.geometry, var4.bounds, var4.alpha, var4.color, var4.opacity, var4.width, var4.fillOpacity, var4.glow);
            kx.u(var2 ? "BlockOverlay glow" : "BlockOverlay base", var5);
         }
      }
   }

   private record n(BlockPos selected, hf geometry, AABB bounds, double alpha, int color, double opacity, double width, double fillOpacity, double glow) {
   }
}

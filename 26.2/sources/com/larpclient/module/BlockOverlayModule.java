package com.larpclient.module;

import com.larpclient.cf;
import com.larpclient.ho;
import com.larpclient.kj;
import com.larpclient.kz;
import com.larpclient.m5;
import com.larpclient.py;
import com.larpclient.su;
import com.larpclient.wa;
import com.larpclient.zj;
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
   private static BlockOverlayModule L;
   private final e V = this.addSetting(new e("Transition", "Time to glide to another block, in milliseconds; 0 is instant", 240.0, 0.0, 300.0, 10.0));
   private final l T = this.addSetting(new l("Color Mode", "Use your interface accent or a custom color", "Theme", "Theme", "Custom"));
   private final i f = this.addSetting(new i("Color", "Custom highlight color", -1381654));
   private final e C = this.addSetting(n("Outline Opacity", "Outline visibility", 0.85, 0.1, 1.0, 0.05));
   private final e X = this.addSetting(new e("Outline Thickness", "Stroke width in blocks", 0.015, 0.005, 0.05, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   });
   private final s y = this.addSetting(new s("Fill", "Subtle tint over the selected shape", true));
   private final e j = this.addSetting(n("Fill Opacity", "Keep the block texture visible", 0.15, 0.01, 0.3, 0.01));
   private final e c = this.addSetting(n("Glow", "Soft edge halo and shared quality-controlled bloom", 1.5, 0.0, 1.5, 0.05));
   private final py W = new py();
   private Object Q;
   private Object d;
   private Vec3 i;
   private VoxelShape J;
   private su x;
   private BlockPos R;
   private int G = -1;
   private boolean H;
   private boolean A;
   private static final RenderStateDataKey<BlockOverlayModule.y> D = RenderStateDataKey.create(() -> "Larp block overlay");

   public BlockOverlayModule() {
      super("Block Overlay", "Highlight the block you aim at", Category.VISUALS, -1);
      L = this;
      this.f.P(() -> this.T.O("Custom"));
      this.j.P(this.y::getValue);
   }

   private static e n(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new e(var0, var1, var2, var4, var6, var8) {
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

   public static BlockOverlayModule T() {
      return L;
   }

   public List<q<?>> o() {
      return List.of(this.T, this.f, this.C, this.X, this.y, this.j, this.c);
   }

   public List<q<?>> N() {
      return List.of(this.V);
   }

   @Override
   public void onEnable() {
      this.a();
      com.larpclient.h.A("BlockOverlay", "Selection overlay enabled; cached native shapes, interruptible {} ms transition, shared glow", this.V.getValue());
   }

   @Override
   public void onDisable() {
      this.a();
   }

   public void a() {
      this.W.v();
      this.x = null;
      this.J = null;
      this.R = null;
      this.Q = this.d = null;
      this.i = null;
      this.H = this.A = (boolean)0;
   }

   public void C(LevelExtractionContext var1, HitResult var2) {
      if (this.isEnabled()) {
         LevelRenderState var3 = var1.levelState();
         var3.setData(D, null);
         if (!this.isRuntimeActive()) {
            this.a();
         } else {
            Minecraft var4 = getMc();
            if (var4.gui.screen() == null
               && !var4.gameRenderer.gameRenderState().guiRenderState.isHudHidden
               && var4.isWindowActive()
               && var4.player != null
               && var4.player.isAlive()) {
               Vec3 var5 = var1.camera().position();
               if (this.Q != var1.level() || this.d != var4.player || this.G != wa.s || this.i != null && this.i.distanceTo(var5) > 8.0) {
                  this.a();
                  this.Q = var1.level();
                  this.d = var4.player;
                  this.G = wa.s;
               }

               this.i = var5;
               if (!this.H) {
                  try {
                     BlockOutlineRenderState var6 = var1.levelState().blockOutlineRenderState;
                     AABB var7 = null;
                     this.R = null;
                     if (var6 != null && var2 != null && var2.getType() == Type.BLOCK) {
                        VoxelShape var8 = var6.shape();
                        this.x = this.o(var8);
                        if (this.x != null) {
                           this.R = var6.pos();
                           var7 = this.x.bounds().move(this.R);
                        } else if (!this.A) {
                           this.A = (boolean)1;
                           com.larpclient.h.A("BlockOverlay", "Empty or complex selection shape; keeping native outline (limits: {} edges, {} boxes)", 128, 32);
                        }
                     }

                     py.o var11 = this.W.q(var7, cf.F(), this.V.getValue() / 1000.0);
                     BlockOverlayModule.y var9 = this.x != null && var11.bounds() != null
                        ? new BlockOverlayModule.y(
                           this.R,
                           this.x,
                           var11.bounds().inflate(0.002),
                           var11.alpha(),
                           this.T.O("Theme") ? ho.J : this.f.getValue(),
                           this.C.getValue(),
                           this.X.getValue(),
                           this.y.getValue() ? this.j.getValue() : 0.0,
                           this.c.getValue()
                        )
                        : null;
                     var3.setData(D, var9);
                  } catch (RuntimeException var10) {
                     this.H = (boolean)1;
                     this.R = null;
                     com.larpclient.h.m("BlockOverlay", "Selection extraction failed; native outline retained, retry on resource reload", var10);
                  }
               }
            } else {
               this.a();
            }
         }
      }
   }

   private su o(VoxelShape var1) {
      if (var1 != this.J) {
         this.x = su.build(var1);
         this.J = var1;
      }

      return this.x;
   }

   private static BlockOverlayModule.y v(LevelRenderState var0) {
      return (BlockOverlayModule.y)var0.getData(D);
   }

   public boolean k(BlockPos var1, LevelRenderState var2) {
      if (!this.isEnabled()) {
         return false;
      }

      BlockOverlayModule.y var3 = v(var2);
      return this.isRuntimeActive() && !this.H && kz.x() && var3 != null && var1.equals(var3.selected);
   }

   public boolean g(LevelRenderState var1) {
      if (!this.isEnabled()) {
         return false;
      }

      BlockOverlayModule.y var2 = v(var1);
      return this.isRuntimeActive() && var2 != null && var2.alpha > 0.001 && var2.glow > 0.0;
   }

   public void b(kj var1, boolean var2, LevelRenderState var3) {
      if (this.isEnabled()) {
         BlockOverlayModule.y var4 = v(var3);
         if (this.isRuntimeActive() && var4 != null && !(var4.alpha < 0.001) && (!var2 || !(var4.glow <= 0.0))) {
            long var5 = zj.m();
            m5.s(var1, var2, var4.geometry, var4.bounds, var4.alpha, var4.color, var4.opacity, var4.width, var4.fillOpacity, var4.glow);
            zj.l(var2 ? "BlockOverlay glow" : "BlockOverlay base", var5);
         }
      }
   }

   private record y(BlockPos selected, su geometry, AABB bounds, double alpha, int color, double opacity, double width, double fillOpacity, double glow) {
   }
}

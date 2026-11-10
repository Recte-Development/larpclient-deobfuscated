package com.larpclient.module;

import com.larpclient.bu;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.dimension.DimensionType.Skybox;

public final class FogModule extends Module {
   private static FogModule I;
   public final e V = this.Y("Start Distance", "Clear range before fog begins, in blocks", 0.0, 0.0, 256.0, 4.0);
   public final e n = this.Y("End Distance", "Distance where fog reaches full density, in blocks", 48.0, 16.0, 1024.0, 8.0);
   public final e H = this.Y("Height", "Fog thins above sea level over this many blocks", 256.0, 8.0, 256.0, 4.0);
   public final e p = this.Y("Sun Glow", "Warm light scattered through the fog toward the sun", 0.45, 0.0, 1.0, 0.05);
   public final l r = this.addSetting(new l("Color", "Sky follows the native and sky module fog color", "Sky", "Sky", "Custom"));
   public final i g = this.addSetting(new i("Custom Color", "Fog color when Color is Custom", -4601895));
   public final e J = this.Y("Sky Height", "Degrees above the horizon the fog covers the sky; zero keeps the sky clear", 30.0, 0.0, 30.0, 1.0);
   public final s w = this.addSetting(new s("Clear Sky", "Keep vanilla fog from climbing high up the sky", true));
   public final s c = this.addSetting(new s("Fade Underground", "Fade the fog where the camera cannot see the sky", true));
   public final l Z = this.addSetting(new l("Dimension Filter", "Choose where the fog appears", "Overworld", "Overworld", "All"));
   public static final float T = 512.0F;
   public static final float m = 1.0F;
   private double Q = 1.0;
   private double L = 1.0;
   private int K;

   private e Y(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new e(var1, var2, var3, var5, var7, var9));
   }

   public FogModule() {
      super("Fog", "Soft distance fog that keeps the sky clear", Category.VISUALS, -1);
      I = this;
      this.g.P(() -> this.r.O("Custom"));
   }

   public static FogModule P() {
      return I;
   }

   public List<q<?>> h() {
      return List.of(this.V, this.n);
   }

   public List<q<?>> M() {
      return List.of(this.H, this.p, this.r, this.g);
   }

   public List<q<?>> m() {
      return List.of(this.J, this.w);
   }

   public List<q<?>> G() {
      return List.of(this.c, this.Z);
   }

   public boolean c() {
      if (!this.isEnabled()) {
         return false;
      } else {
         return this.isRuntimeActive() && mc != null && mc.level != null ? this.Z.O("All") || mc.level.dimensionType().skybox() == Skybox.OVERWORLD : false;
      }
   }

   public static boolean l() {
      return I != null && I.isEnabled() ? I != null && I.w.getValue() && I.c() : false;
   }

   public float o() {
      return (float)Math.max(this.n.getValue(), this.V.getValue() + 8.0);
   }

   public float J(float var1) {
      return (float)(this.L + (this.Q - this.L) * Math.clamp(var1, 0.0F, 1.0F));
   }

   @Override
   public void onTick() {
      this.L = this.Q;
      if (this.isInGame()) {
         double var1 = 1.0;
         if (this.c.getValue()) {
            BlockPos var3 = BlockPos.containing(mc.gameRenderer.mainCamera().position());
            int var10000 = mc.level.hasChunkAt(var3);
            boolean var9 = false;
            byte var8 = (byte)var10000;
            var10000 = Integer.signum(var8);
            double var14;
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               var10000 = mc.level.getBrightness(LightLayer.SKY, var3);
               byte var11 = 3;
               int var10 = var10000;
               var14 = Math.clamp((var10 - var11) / 7.0, 0.0, 1.0);
            } else {
               var14 = this.Q;
            }

            var1 = var14;
         }

         this.Q = this.Q + Math.clamp(var1 - this.Q, -0.04, 0.04);
         int var15 = this.K;
         byte var5 = 1;
         int var4 = this.K;
         this.K = var4 + var5;
         if (var15 % 100 == 0) {
            double var17;
            var15 = (var17 = Math.abs(var1 - this.Q) - 0.5) == 0.0 ? 0 : (var17 < 0.0 ? -1 : 1);
            boolean var7 = false;
            int var6 = var15;
            if ((-Integer.signum(var6) >>> 31 ^ 1) == 0) {
               com.larpclient.h.U("Fog", "Underground fade toward {}", var1);
            }
         }
      }
   }

   @Override
   public void onEnable() {
      this.Q = this.L = 1.0;
      this.K = 0;
      com.larpclient.h.A(
         "Fog",
         "Enabled; start={}; end={}; height={}; sky height={} degrees; clear sky={}",
         this.V.getValue(),
         this.o(),
         this.H.getValue(),
         this.J.getValue(),
         this.w.getValue()
      );
   }

   @Override
   public void onDisable() {
      bu.D();
   }
}

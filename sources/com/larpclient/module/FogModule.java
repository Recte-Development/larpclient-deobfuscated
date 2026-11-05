package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.u1;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.dimension.DimensionType.Skybox;

public final class FogModule extends Module {
   private static FogModule v;
   public final d9 A = this.X("Start Distance", "Clear range before fog begins, in blocks", 0.0, 0.0, 256.0, 4.0);
   public final d9 k = this.X("End Distance", "Distance where fog reaches full density, in blocks", 48.0, 16.0, 1024.0, 8.0);
   public final d9 h = this.X("Height", "Fog thins above sea level over this many blocks", 256.0, 8.0, 256.0, 4.0);
   public final d9 f = this.X("Sun Glow", "Warm light scattered through the fog toward the sun", 0.45, 0.0, 1.0, 0.05);
   public final d6 B = this.addSetting(new d6("Color", "Sky follows the native and sky module fog color", "Sky", "Sky", "Custom"));
   public final dk P = this.addSetting(new dk("Custom Color", "Fog color when Color is Custom", -4601895));
   public final d9 i = this.X("Sky Height", "Degrees above the horizon the fog covers the sky; zero keeps the sky clear", 30.0, 0.0, 30.0, 1.0);
   public final dh n = this.addSetting(new dh("Clear Sky", "Keep vanilla fog from climbing high up the sky", true));
   public final dh I = this.addSetting(new dh("Fade Underground", "Fade the fog where the camera cannot see the sky", true));
   public final d6 L = this.addSetting(new d6("Dimension Filter", "Choose where the fog appears", "Overworld", "Overworld", "All"));
   public static final float J = 512.0F;
   public static final float p = 1.0F;
   private double z = 1.0;
   private double q = 1.0;
   private int W;

   private d9 X(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new d9(var1, var2, var3, var5, var7, var9));
   }

   public FogModule() {
      super("Fog", "Soft distance fog that keeps the sky clear", Category.VISUALS, -1);
      v = this;
      this.P.H(() -> this.B.S("Custom"));
   }

   public static FogModule U() {
      return v;
   }

   public List<df<?>> Y() {
      return List.of(this.A, this.k);
   }

   public List<df<?>> l() {
      return List.of(this.h, this.f, this.B, this.P);
   }

   public List<df<?>> A() {
      return List.of(this.i, this.n);
   }

   public List<df<?>> Z() {
      return List.of(this.I, this.L);
   }

   public boolean i() {
      if (!this.isEnabled()) {
         return false;
      } else {
         return this.isRuntimeActive() && mc != null && mc.level != null ? this.L.S("All") || mc.level.dimensionType().skybox() == Skybox.OVERWORLD : false;
      }
   }

   public static boolean I() {
      return v != null && v.isEnabled() ? v != null && v.n.P() && v.i() : false;
   }

   public float p() {
      return (float)Math.max(this.k.P(), this.A.P() + 8.0);
   }

   public float r(float var1) {
      return (float)(this.q + (this.z - this.q) * Math.clamp(var1, 0.0F, 1.0F));
   }

   @Override
   public void onTick() {
      this.q = this.z;
      if (this.isInGame()) {
         double var1 = 1.0;
         int var10000 = this.I.P();
         boolean var9 = false;
         byte var8 = (byte)var10000;
         var10000 = Integer.signum(var8);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            BlockPos var3 = BlockPos.containing(mc.gameRenderer.mainCamera().position());
            double var14;
            if (mc.level.hasChunkAt(var3)) {
               var10000 = mc.level.getBrightness(LightLayer.SKY, var3);
               byte var11 = 3;
               int var10 = var10000;
               var14 = Math.clamp((var10 - var11) / 7.0, 0.0, 1.0);
            } else {
               var14 = this.z;
            }

            var1 = var14;
         }

         this.z = this.z + Math.clamp(var1 - this.z, -0.04, 0.04);
         var10000 = this.W;
         byte var5 = 1;
         int var4 = this.W;
         this.W = var4 + var5;
         if (var10000 % 100 == 0) {
            double var17;
            var10000 = (var17 = Math.abs(var1 - this.z) - 0.5) == 0.0 ? 0 : (var17 < 0.0 ? -1 : 1);
            boolean var7 = false;
            int var6 = var10000;
            if ((-Integer.signum(var6) >>> 31 ^ 1) == 0) {
               mt.U("Fog", "Underground fade toward {}", var1);
            }
         }
      }
   }

   @Override
   public void onEnable() {
      this.z = this.q = 1.0;
      this.W = 0;
      mt.K("Fog", "Enabled; start={}; end={}; height={}; sky height={} degrees; clear sky={}", this.A.P(), this.p(), this.h.P(), this.i.P(), this.n.P());
   }

   @Override
   public void onDisable() {
      u1.I();
   }
}

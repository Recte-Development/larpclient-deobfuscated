package com.larpclient.module;

import com.larpclient.fo;
import com.larpclient.r4;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.dimension.DimensionType.Skybox;

public final class FogModule extends Module {
   private static FogModule Z;
   public final lc H = this.r("Start Distance", "Clear range before fog begins, in blocks", 0.0, 0.0, 256.0, 4.0);
   public final lc n = this.r("End Distance", "Distance where fog reaches full density, in blocks", 48.0, 16.0, 1024.0, 8.0);
   public final lc f = this.r("Height", "Fog thins above sea level over this many blocks", 256.0, 8.0, 256.0, 4.0);
   public final lc c = this.r("Sun Glow", "Warm light scattered through the fog toward the sun", 0.45, 0.0, 1.0, 0.05);
   public final lp k = this.addSetting(new lp("Color", "Sky follows the native and sky module fog color", "Sky", "Sky", "Custom"));
   public final lv d = this.addSetting(new lv("Custom Color", "Fog color when Color is Custom", -4601895));
   public final lc I = this.r("Sky Height", "Degrees above the horizon the fog covers the sky; zero keeps the sky clear", 30.0, 0.0, 30.0, 1.0);
   public final ln M = this.addSetting(new ln("Clear Sky", "Keep vanilla fog from climbing high up the sky", true));
   public final ln T = this.addSetting(new ln("Fade Underground", "Fade the fog where the camera cannot see the sky", true));
   public final lp g = this.addSetting(new lp("Dimension Filter", "Choose where the fog appears", "Overworld", "Overworld", "All"));
   public static final float s = 512.0F;
   public static final float j = 1.0F;
   private double o = 1.0;
   private double O = 1.0;
   private int h;

   private lc r(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lc(var1, var2, var3, var5, var7, var9));
   }

   public FogModule() {
      super("Fog", "Soft distance fog that keeps the sky clear", Category.VISUALS, -1);
      Z = this;
      this.d.x(() -> this.k.i("Custom"));
   }

   public static FogModule A() {
      return Z;
   }

   public List<l6<?>> e() {
      return List.of(this.H, this.n);
   }

   public List<l6<?>> Z() {
      return List.of(this.f, this.c, this.k, this.d);
   }

   public List<l6<?>> s() {
      return List.of(this.I, this.M);
   }

   public List<l6<?>> O() {
      return List.of(this.T, this.g);
   }

   public boolean p() {
      if (!this.isEnabled()) {
         return false;
      } else {
         return this.isRuntimeActive() && mc != null && mc.level != null ? this.g.i("All") || mc.level.dimensionType().skybox() == Skybox.OVERWORLD : false;
      }
   }

   public static boolean S() {
      return Z != null && Z.isEnabled() ? Z != null && Z.M.getValue() && Z.p() : false;
   }

   public float f() {
      return (float)Math.max(this.n.getValue(), this.H.getValue() + 8.0);
   }

   public float T(float var1) {
      return (float)(this.O + (this.o - this.O) * Math.clamp(var1, 0.0F, 1.0F));
   }

   @Override
   public void onTick() {
      this.O = this.o;
      if (this.isInGame()) {
         double var1 = 1.0;
         if (this.T.getValue()) {
            BlockPos var3 = BlockPos.containing(mc.gameRenderer.mainCamera().position());
            int var10000 = mc.level.hasChunkAt(var3);
            boolean var11 = false;
            byte var10 = (byte)var10000;
            var10000 = Integer.signum(var10);
            var1 = ((var10000 | -var10000) >>> 31 ^ 1) == 0 ? Math.clamp((mc.level.getBrightness(LightLayer.SKY, var3) - 3) / 7.0, 0.0, 1.0) : this.o;
         }

         this.o = this.o + Math.clamp(var1 - this.o, -0.04, 0.04);
         int var13 = this.h;
         byte var5 = 1;
         int var4 = this.h;
         this.h = var4 + var5;
         var13 %= 100;
         boolean var7 = false;
         int var6 = var13;
         var13 = Integer.signum(var6);
         if ((var13 | -var13) >>> 31 == 0) {
            double var17;
            var13 = (var17 = Math.abs(var1 - this.o) - 0.5) == 0.0 ? 0 : (var17 < 0.0 ? -1 : 1);
            boolean var9 = false;
            int var8 = var13;
            if ((-Integer.signum(var8) >>> 31 ^ 1) == 0) {
               r4.g("Fog", "Underground fade toward {}", var1);
            }
         }
      }
   }

   @Override
   public void onEnable() {
      this.o = this.O = 1.0;
      this.h = 0;
      r4.V(
         "Fog",
         "Enabled; start={}; end={}; height={}; sky height={} degrees; clear sky={}",
         this.H.getValue(),
         this.f(),
         this.f.getValue(),
         this.I.getValue(),
         this.M.getValue()
      );
   }

   @Override
   public void onDisable() {
      fo.v();
   }
}

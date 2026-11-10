package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.bj;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.dimension.DimensionType.Skybox;

public final class FogModule extends Module {
   private static FogModule f;
   public final b N = this.G("Start Distance", "Clear range before fog begins, in blocks", 0.0, 0.0, 256.0, 4.0);
   public final b H = this.G("End Distance", "Distance where fog reaches full density, in blocks", 48.0, 16.0, 1024.0, 8.0);
   public final b l = this.G("Height", "Fog thins above sea level over this many blocks", 256.0, 8.0, 256.0, 4.0);
   public final b g = this.G("Sun Glow", "Warm light scattered through the fog toward the sun", 0.45, 0.0, 1.0, 0.05);
   public final o d = this.addSetting(new o("Color", "Sky follows the native and sky module fog color", "Sky", "Sky", "Custom"));
   public final h m = this.addSetting(new h("Custom Color", "Fog color when Color is Custom", -4601895));
   public final b y = this.G("Sky Height", "Degrees above the horizon the fog covers the sky; zero keeps the sky clear", 30.0, 0.0, 30.0, 1.0);
   public final m t = this.addSetting(new m("Clear Sky", "Keep vanilla fog from climbing high up the sky", true));
   public final m a = this.addSetting(new m("Fade Underground", "Fade the fog where the camera cannot see the sky", true));
   public final o G = this.addSetting(new o("Dimension Filter", "Choose where the fog appears", "Overworld", "Overworld", "All"));
   public static final float e = 512.0F;
   public static final float r = 1.0F;
   private double E = 1.0;
   private double I = 1.0;
   private int Z;

   private b G(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new b(var1, var2, var3, var5, var7, var9));
   }

   public FogModule() {
      super("Fog", "Soft distance fog that keeps the sky clear", Category.VISUALS, -1);
      f = this;
      this.m.a(() -> this.d.w("Custom"));
   }

   public static FogModule z() {
      return f;
   }

   public List<r<?>> e() {
      return List.of(this.N, this.H);
   }

   public List<r<?>> W() {
      return List.of(this.l, this.g, this.d, this.m);
   }

   public List<r<?>> w() {
      return List.of(this.y, this.t);
   }

   public List<r<?>> S() {
      return List.of(this.a, this.G);
   }

   public boolean R() {
      if (!this.isEnabled()) {
         return false;
      } else {
         return this.isRuntimeActive() && mc != null && mc.level != null ? this.G.w("All") || mc.level.dimensionType().skybox() == Skybox.OVERWORLD : false;
      }
   }

   public static boolean n() {
      return f != null && f.isEnabled() ? f != null && f.t.getValue() && f.R() : false;
   }

   public float n() {
      return (float)Math.max(this.H.getValue(), this.N.getValue() + 8.0);
   }

   public float T(float var1) {
      return (float)(this.I + (this.E - this.I) * Math.clamp(var1, 0.0F, 1.0F));
   }

   @Override
   public void onTick() {
      this.I = this.E;
      if (this.isInGame()) {
         double var1 = 1.0;
         int var10000 = this.a.getValue();
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
               var14 = this.E;
            }

            var1 = var14;
         }

         this.E = this.E + Math.clamp(var1 - this.E, -0.04, 0.04);
         var10000 = this.Z;
         byte var5 = 1;
         int var4 = this.Z;
         this.Z = var4 + var5;
         if (var10000 % 100 == 0) {
            double var17;
            var10000 = (var17 = Math.abs(var1 - this.E) - 0.5) == 0.0 ? 0 : (var17 < 0.0 ? -1 : 1);
            boolean var7 = false;
            int var6 = var10000;
            if ((-Integer.signum(var6) >>> 31 ^ 1) == 0) {
               _q.c("Fog", "Underground fade toward {}", var1);
            }
         }
      }
   }

   @Override
   public void onEnable() {
      this.E = this.I = 1.0;
      this.Z = 0;
      _q.z(
         "Fog",
         "Enabled; start={}; end={}; height={}; sky height={} degrees; clear sky={}",
         this.N.getValue(),
         this.n(),
         this.l.getValue(),
         this.y.getValue(),
         this.t.getValue()
      );
   }

   @Override
   public void onDisable() {
      bj.t();
   }
}

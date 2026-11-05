package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import com.larpclient.module.NoWeatherModule;
import com.larpclient.module.RemovalsModule;
import com.larpclient.module.dh;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class SnowGroundModule extends Module {
   private static SnowGroundModule o;
   public final com.larpclient.module.d9 J = this.M("Coverage", "Snow coverage on exposed ground and roofs, percent", 100.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.d9 W = this.M("Depth", "Soft drift relief and snow along upper edges, percent", 75.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.d9 H = this.M("Sparkle", "Small sunlit ice crystals, percent", 30.0, 0.0, 100.0, 5.0);
   public final dh w = this.addSetting(new dh("Snowfall", "Falling snow even in clear server weather", true));
   public final com.larpclient.module.d9 g = this.M("Density", "Falling snow density, percent", 85.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.d9 i = this.M("Wind", "Sideways drift and rolling gusts, percent", 35.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.d9 e = this.M("Radius", "Snowfall radius around the camera in blocks", 24.0, 12.0, 40.0, 2.0);
   public final com.larpclient.module.d6 Q = this.addSetting(
      new com.larpclient.module.d6("Quality", "Snow detail and bounded flake count", "High", "Low", "Medium", "High")
   );
   public final dh C = this.addSetting(new dh("Footsteps", "Clear powder beneath you and leave a soft walking trail", true));
   public final com.larpclient.module.d9 I = this.M("Trail Time", "Seconds before walking trails fill back in", 12.0, 2.0, 30.0, 1.0);
   private final z9 t = new z9();
   private Vec3 d = Vec3.ZERO;
   private final o8 X = new o8();
   private Object R;
   private Object F;
   private int f = -1;
   private int u = -1;

   private com.larpclient.module.d9 M(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new com.larpclient.module.d9(var1, var2, var3, var5, var7, var9));
   }

   public SnowGroundModule() {
      super("Snow Ground", "Deep powder, wind-shaped snow drifts and dense falling snow", Category.RENDER, -1);
      o = this;
      this.g.H(this.w::P);
      this.i.H(this.w::P);
      this.e.H(this.w::P);
      this.I.H(this.C::P);
      this.C.h(var1 -> this.t.L());
      this.w.h(var1 -> this.X.m());
      this.Q.h(var1 -> this.X.m());
      this.e.h(var1 -> this.X.m());
   }

   public static boolean Z() {
      return o != null && o.isEnabled() ? o != null && o.isRuntimeActive() : false;
   }

   public static SnowGroundModule d() {
      return o;
   }

   public int m() {
      return this.Q.S("High") ? 2 : (this.Q.S("Medium") ? 1 : 0);
   }

   public static int z(int var0) {
      return var0 >= 2 ? 3200 : (var0 == 1 ? 1600 : 800);
   }

   public static boolean a() {
      if (o != null && o.isEnabled()) {
         SnowGroundModule var0 = o;
         return var0 != null && Z() && var0.f != m5.k && var0.w.P() && var0.g.P() > 0.0 && !NoWeatherModule.F() && !RemovalsModule.r("weather");
      } else {
         return false;
      }
   }

   public o8 p() {
      return this.X;
   }

   public void v() {
      this.X.m();
      this.t.L();
      this.d = Vec3.ZERO;
      this.R = this.F = null;
   }

   public z9 k() {
      return this.t;
   }

   public void u(ByteBuffer var1, Vec3 var2, double var3, boolean var5) {
      if (this.isEnabled()) {
         this.t.p(var1, var2, this.d, var3, this.I.P(), var5 && Z() && this.C.P() && this.R == mc.level && this.F == mc.player);
      }
   }

   public void N() {
      this.v();
      this.f = m5.k;
   }

   public static boolean K() {
      return o != null && o.isEnabled() ? a() && o.X.i() > 0 : false;
   }

   @Override
   public void onEnable() {
      this.v();
      k.g();
      k.k();
      mt.K(
         "SnowGround",
         "Enabled powder and snowfall; quality={}, flake cap={}, radius={} blocks, footsteps={}, trail={} seconds",
         this.Q.P(),
         z(this.m()),
         this.e.P(),
         this.C.P(),
         this.I.P()
      );
   }

   @Override
   public void onDisable() {
      this.v();
      k.k();
      k.w();
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && k.K() ? "Material fallback" : "";
   }

   @Override
   public void onTick() {
      int var3 = m5.k;
      int var2 = this.u;
      int var10000 = Integer.compare(var2, var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         this.v();
         this.u = m5.k;
      }

      if (!this.isInGame()) {
         this.v();
      } else {
         if (this.R != mc.level || this.F != mc.player) {
            this.v();
            this.R = mc.level;
            this.F = mc.player;
         }

         if (!mc.isPaused()) {
            Vec3 var1 = new Vec3(mc.player.getX(), mc.player.getBoundingBox().minY, mc.player.getZ());
            this.d = var1;
            if (this.C.P()) {
               int var15;
               label37: {
                  var10 = this.t;
                  var15 = mc.player.onGround();
                  boolean var5 = false;
                  byte var4 = var15;
                  var15 = Integer.signum(var4);
                  if (((var15 | -var15) >>> 31 ^ 1) == 0) {
                     int var12 = mc.player.isPassenger();
                     boolean var7 = false;
                     byte var6 = (byte)var12;
                     var12 = Integer.signum(var6);
                     if ((var12 | -var12) >>> 31 == 0 && !mc.player.isSpectator() && !mc.player.isInWater()) {
                        boolean var9 = false;
                        byte var8 = mc.player.getAbilities().flying;
                        var12 = Integer.signum(var8);
                        if ((var12 | -var12) >>> 31 == 0) {
                           var15 = 1;
                           break label37;
                        }
                     }
                  }

                  var15 = 0;
               }

               var10.D(var1, (boolean)var15, ty.P(), this.I.P());
            } else {
               this.t.L();
            }

            if (!a()) {
               this.X.m();
            } else {
               this.X.Y(mc.gameRenderer.mainCamera().position(), new o8.h(this.e.P(), this.g.P() / 100.0, this.i.P() / 100.0, z(this.m())), (var0, var1x) -> {
                  if (!mc.level.hasChunk(var0 >> 4, var1x >> 4)) {
                     return o8.r.UNAVAILABLE;
                  }

                  int var2x = mc.level.getHeight(Types.MOTION_BLOCKING, var0, var1x) - 1;
                  BlockPos var3x = new BlockPos(var0, var2x, var1x);
                  BlockState var4x = mc.level.getBlockState(var3x);
                  VoxelShape var5x = var4x.getCollisionShape(mc.level, var3x);
                  double var6x = var2x + (!var5x.isEmpty() && var4x.getFluidState().isEmpty() ? var5x.max(Axis.Y) : 1.0);
                  int var8x = mc.level.getMaxLocalRawBrightness(var3x.above());
                  return new o8.r(var6x, var8x / 15.0);
               });
            }
         }
      }
   }

   public void o(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (Z() && this.R == mc.level && this.F == mc.player) {
            this.d = mc.player.getEyePosition(var1).subtract(0.0, mc.player.getEyeHeight(), 0.0);
            if (!a()) {
               this.X.m();
            } else {
               this.X.H(var2.pos, var1);
            }
         } else {
            this.v();
         }
      }
   }

   public void h(ki var1) {
      if (this.isEnabled()) {
         if (Z() && a() && this.R == mc.level && this.F == mc.player) {
            this.X.W(var1);
         }
      }
   }
}

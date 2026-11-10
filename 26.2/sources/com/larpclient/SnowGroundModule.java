package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import com.larpclient.module.NoWeatherModule;
import com.larpclient.module.RemovalsModule;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class SnowGroundModule extends Module {
   private static SnowGroundModule d;
   public final com.larpclient.module.e H = this.R("Coverage", "Snow coverage on exposed ground and roofs, percent", 100.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.e t = this.R("Depth", "Soft drift relief and snow along upper edges, percent", 75.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.e l = this.R("Sparkle", "Small sunlit ice crystals, percent", 30.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.s J = this.addSetting(new com.larpclient.module.s("Snowfall", "Falling snow even in clear server weather", true));
   public final com.larpclient.module.e M = this.R("Density", "Falling snow density, percent", 85.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.e u = this.R("Wind", "Sideways drift and rolling gusts, percent", 35.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.e Y = this.R("Radius", "Snowfall radius around the camera in blocks", 24.0, 12.0, 40.0, 2.0);
   public final com.larpclient.module.l j = this.addSetting(
      new com.larpclient.module.l("Quality", "Snow detail and bounded flake count", "High", "Low", "Medium", "High")
   );
   public final com.larpclient.module.s W = this.addSetting(
      new com.larpclient.module.s("Footsteps", "Clear powder beneath you and leave a soft walking trail", true)
   );
   public final com.larpclient.module.e r = this.R("Trail Time", "Seconds before walking trails fill back in", 12.0, 2.0, 30.0, 1.0);
   private final cz I = new cz();
   private Vec3 S = Vec3.ZERO;
   private final mu v = new mu();
   private Object g;
   private Object s;
   private int G = -1;
   private int w = -1;

   private com.larpclient.module.e R(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new com.larpclient.module.e(var1, var2, var3, var5, var7, var9));
   }

   public SnowGroundModule() {
      super("Snow Ground", "Deep powder, wind-shaped snow drifts and dense falling snow", Category.RENDER, -1);
      d = this;
      this.M.P(this.J::getValue);
      this.u.P(this.J::getValue);
      this.Y.P(this.J::getValue);
      this.r.P(this.W::getValue);
      this.W.o(var1 -> this.I.t());
      this.J.o(var1 -> this.v.T());
      this.j.o(var1 -> this.v.T());
      this.Y.o(var1 -> this.v.T());
   }

   public static boolean i() {
      return d != null && d.isEnabled() ? d != null && d.isRuntimeActive() : false;
   }

   public static SnowGroundModule C() {
      return d;
   }

   public int j() {
      return this.j.O("High") ? 2 : (this.j.O("Medium") ? 1 : 0);
   }

   public static int M(int var0) {
      return var0 >= 2 ? 3200 : (var0 == 1 ? 1600 : 800);
   }

   public static boolean O() {
      if (d != null && d.isEnabled()) {
         SnowGroundModule var0 = d;
         return var0 != null && i() && var0.G != wa.s && var0.J.getValue() && var0.M.getValue() > 0.0 && !NoWeatherModule.R() && !RemovalsModule.n("weather");
      } else {
         return false;
      }
   }

   public mu K() {
      return this.v;
   }

   public void N() {
      this.v.T();
      this.I.t();
      this.S = Vec3.ZERO;
      this.g = this.s = null;
   }

   public cz f() {
      return this.I;
   }

   public void q(ByteBuffer var1, Vec3 var2, double var3, boolean var5) {
      if (this.isEnabled()) {
         this.I.L(var1, var2, this.S, var3, this.r.getValue(), var5 && i() && this.W.getValue() && this.g == mc.level && this.s == mc.player);
      }
   }

   public void D() {
      this.N();
      this.G = wa.s;
   }

   public static boolean e() {
      return d != null && d.isEnabled() ? O() && d.v.s() > 0 : false;
   }

   @Override
   public void onEnable() {
      this.N();
      w6.Q();
      w6.N();
      h.A(
         "SnowGround",
         "Enabled powder and snowfall; quality={}, flake cap={}, radius={} blocks, footsteps={}, trail={} seconds",
         this.j.getValue(),
         M(this.j()),
         this.Y.getValue(),
         this.W.getValue(),
         this.r.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.N();
      w6.N();
      w6.P();
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && w6.C() ? "Material fallback" : "";
   }

   @Override
   public void onTick() {
      int var3 = wa.s;
      int var2 = this.w;
      int var10000 = Integer.compare(var2, var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         this.N();
         this.w = wa.s;
      }

      if (!this.isInGame()) {
         this.N();
      } else {
         if (this.g != mc.level || this.s != mc.player) {
            this.N();
            this.g = mc.level;
            this.s = mc.player;
         }

         if (!mc.isPaused()) {
            Vec3 var1 = new Vec3(mc.player.getX(), mc.player.getBoundingBox().minY, mc.player.getZ());
            this.S = var1;
            if (this.W.getValue()) {
               int var15;
               label37: {
                  var10 = this.I;
                  if (mc.player.onGround()) {
                     var15 = mc.player.isPassenger();
                     boolean var5 = false;
                     byte var4 = var15;
                     var15 = Integer.signum(var4);
                     if ((var15 | -var15) >>> 31 == 0 && !mc.player.isSpectator()) {
                        int var12 = mc.player.isInWater();
                        boolean var7 = false;
                        byte var6 = (byte)var12;
                        var12 = Integer.signum(var6);
                        if ((var12 | -var12) >>> 31 == 0) {
                           boolean var9 = false;
                           byte var8 = mc.player.getAbilities().flying;
                           var12 = Integer.signum(var8);
                           if ((var12 | -var12) >>> 31 == 0) {
                              var15 = 1;
                              break label37;
                           }
                        }
                     }
                  }

                  var15 = 0;
               }

               var10.b(var1, (boolean)var15, cf.I(), this.r.getValue());
            } else {
               this.I.t();
            }

            if (!O()) {
               this.v.T();
            } else {
               this.v
                  .M(
                     mc.gameRenderer.mainCamera().position(),
                     new mu.j(this.Y.getValue(), this.M.getValue() / 100.0, this.u.getValue() / 100.0, M(this.j())),
                     (var0, var1x) -> {
                        if (!mc.level.hasChunk(var0 >> 4, var1x >> 4)) {
                           return mu.h.UNAVAILABLE;
                        }

                        int var2x = mc.level.getHeight(Types.MOTION_BLOCKING, var0, var1x) - 1;
                        BlockPos var3x = new BlockPos(var0, var2x, var1x);
                        BlockState var4x = mc.level.getBlockState(var3x);
                        VoxelShape var5x = var4x.getCollisionShape(mc.level, var3x);
                        double var6x = var2x + (!var5x.isEmpty() && var4x.getFluidState().isEmpty() ? var5x.max(Axis.Y) : 1.0);
                        int var8x = mc.level.getMaxLocalRawBrightness(var3x.above());
                        return new mu.h(var6x, var8x / 15.0);
                     }
                  );
            }
         }
      }
   }

   public void d(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (i() && this.g == mc.level && this.s == mc.player) {
            this.S = mc.player.getEyePosition(var1).subtract(0.0, mc.player.getEyeHeight(), 0.0);
            if (!O()) {
               this.v.T();
            } else {
               this.v.b(var2.pos, var1);
            }
         } else {
            this.N();
         }
      }
   }

   public void p(kj var1) {
      if (this.isEnabled()) {
         if (i() && O() && this.g == mc.level && this.s == mc.player) {
            this.v.J(var1);
         }
      }
   }
}

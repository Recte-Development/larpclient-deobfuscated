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
   private static SnowGroundModule P;
   public final com.larpclient.module.b p = this.c("Coverage", "Snow coverage on exposed ground and roofs, percent", 100.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.b L = this.c("Depth", "Soft drift relief and snow along upper edges, percent", 75.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.b q = this.c("Sparkle", "Small sunlit ice crystals, percent", 30.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.m W = this.addSetting(new com.larpclient.module.m("Snowfall", "Falling snow even in clear server weather", true));
   public final com.larpclient.module.b v = this.c("Density", "Falling snow density, percent", 85.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.b s = this.c("Wind", "Sideways drift and rolling gusts, percent", 35.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.b S = this.c("Radius", "Snowfall radius around the camera in blocks", 24.0, 12.0, 40.0, 2.0);
   public final com.larpclient.module.o j = this.addSetting(
      new com.larpclient.module.o("Quality", "Snow detail and bounded flake count", "High", "Low", "Medium", "High")
   );
   public final com.larpclient.module.m Z = this.addSetting(
      new com.larpclient.module.m("Footsteps", "Clear powder beneath you and leave a soft walking trail", true)
   );
   public final com.larpclient.module.b R = this.c("Trail Time", "Seconds before walking trails fill back in", 12.0, 2.0, 30.0, 1.0);
   private final so a = new so();
   private Vec3 b = Vec3.ZERO;
   private final aw B = new aw();
   private Object e;
   private Object r;
   private int V = -1;
   private int G = -1;

   private com.larpclient.module.b c(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new com.larpclient.module.b(var1, var2, var3, var5, var7, var9));
   }

   public SnowGroundModule() {
      super("Snow Ground", "Deep powder, wind-shaped snow drifts and dense falling snow", Category.RENDER, -1);
      P = this;
      this.v.a(this.W::getValue);
      this.s.a(this.W::getValue);
      this.S.a(this.W::getValue);
      this.R.a(this.Z::getValue);
      this.Z.w(var1 -> this.a.f());
      this.W.w(var1 -> this.B.R());
      this.j.w(var1 -> this.B.R());
      this.S.w(var1 -> this.B.R());
   }

   public static boolean p() {
      return P != null && P.isEnabled() ? P != null && P.isRuntimeActive() : false;
   }

   public static SnowGroundModule q() {
      return P;
   }

   public int q() {
      return this.j.w("High") ? 2 : (this.j.w("Medium") ? 1 : 0);
   }

   public static int T(int var0) {
      return var0 >= 2 ? 3200 : (var0 == 1 ? 1600 : 800);
   }

   public static boolean f() {
      if (P != null && P.isEnabled()) {
         SnowGroundModule var0 = P;
         return var0 != null && p() && var0.V != nl.q && var0.W.getValue() && var0.v.getValue() > 0.0 && !NoWeatherModule.U() && !RemovalsModule.G("weather");
      } else {
         return false;
      }
   }

   public aw R() {
      return this.B;
   }

   public void U() {
      this.B.R();
      this.a.f();
      this.b = Vec3.ZERO;
      this.e = this.r = null;
   }

   public so k() {
      return this.a;
   }

   public void Y(ByteBuffer var1, Vec3 var2, double var3, boolean var5) {
      if (this.isEnabled()) {
         this.a.t(var1, var2, this.b, var3, this.R.getValue(), var5 && p() && this.Z.getValue() && this.e == mc.level && this.r == mc.player);
      }
   }

   public void n() {
      this.U();
      this.V = nl.q;
   }

   public static boolean l() {
      return P != null && P.isEnabled() ? f() && P.B.y() > 0 : false;
   }

   @Override
   public void onEnable() {
      this.U();
      at.E();
      at.U();
      _q.z(
         "SnowGround",
         "Enabled powder and snowfall; quality={}, flake cap={}, radius={} blocks, footsteps={}, trail={} seconds",
         this.j.getValue(),
         T(this.q()),
         this.S.getValue(),
         this.Z.getValue(),
         this.R.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.U();
      at.U();
      at.r();
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && at.o() ? "Material fallback" : "";
   }

   @Override
   public void onTick() {
      int var3 = nl.q;
      int var2 = this.G;
      int var10000 = Integer.compare(var2, var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         this.U();
         this.G = nl.q;
      }

      if (!this.isInGame()) {
         this.U();
      } else {
         if (this.e != mc.level || this.r != mc.player) {
            this.U();
            this.e = mc.level;
            this.r = mc.player;
         }

         if (!mc.isPaused()) {
            Vec3 var1 = new Vec3(mc.player.getX(), mc.player.getBoundingBox().minY, mc.player.getZ());
            this.b = var1;
            int var10 = this.Z.getValue();
            boolean var5 = false;
            byte var4 = (byte)var10;
            var10 = Integer.signum(var4);
            if (((var10 | -var10) >>> 31 ^ 1) == 0) {
               int var15;
               label37: {
                  var12 = this.a;
                  if (mc.player.onGround()) {
                     var15 = mc.player.isPassenger();
                     boolean var7 = false;
                     byte var6 = var15;
                     var15 = Integer.signum(var6);
                     if ((var15 | -var15) >>> 31 == 0 && !mc.player.isSpectator() && !mc.player.isInWater()) {
                        boolean var9 = false;
                        byte var8 = mc.player.getAbilities().flying;
                        var15 = Integer.signum(var8);
                        if ((var15 | -var15) >>> 31 == 0) {
                           var15 = 1;
                           break label37;
                        }
                     }
                  }

                  var15 = 0;
               }

               var12.d(var1, (boolean)var15, hz.S(), this.R.getValue());
            } else {
               this.a.f();
            }

            if (!f()) {
               this.B.R();
            } else {
               this.B
                  .K(
                     mc.gameRenderer.mainCamera().position(),
                     new aw.j(this.S.getValue(), this.v.getValue() / 100.0, this.s.getValue() / 100.0, T(this.q())),
                     (var0, var1x) -> {
                        if (!mc.level.hasChunk(var0 >> 4, var1x >> 4)) {
                           return aw.i.UNAVAILABLE;
                        }

                        int var2x = mc.level.getHeight(Types.MOTION_BLOCKING, var0, var1x) - 1;
                        BlockPos var3x = new BlockPos(var0, var2x, var1x);
                        BlockState var4x = mc.level.getBlockState(var3x);
                        VoxelShape var5x = var4x.getCollisionShape(mc.level, var3x);
                        double var6x = var2x + (!var5x.isEmpty() && var4x.getFluidState().isEmpty() ? var5x.max(Axis.Y) : 1.0);
                        int var8x = mc.level.getMaxLocalRawBrightness(var3x.above());
                        return new aw.i(var6x, var8x / 15.0);
                     }
                  );
            }
         }
      }
   }

   public void E(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (p() && this.e == mc.level && this.r == mc.player) {
            this.b = mc.player.getEyePosition(var1).subtract(0.0, mc.player.getEyeHeight(), 0.0);
            if (!f()) {
               this.B.R();
            } else {
               this.B.v(var2.pos, var1);
            }
         } else {
            this.U();
         }
      }
   }

   public void f(fz var1) {
      if (this.isEnabled()) {
         if (p() && f() && this.e == mc.level && this.r == mc.player) {
            this.B.j(var1);
         }
      }
   }
}

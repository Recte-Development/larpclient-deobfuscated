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
   private static SnowGroundModule S;
   public final com.larpclient.module.lc a = this.f("Coverage", "Snow coverage on exposed ground and roofs, percent", 100.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.lc j = this.f("Depth", "Soft drift relief and snow along upper edges, percent", 75.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.lc m = this.f("Sparkle", "Small sunlit ice crystals, percent", 30.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.ln p = this.addSetting(new com.larpclient.module.ln("Snowfall", "Falling snow even in clear server weather", true));
   public final com.larpclient.module.lc v = this.f("Density", "Falling snow density, percent", 85.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.lc r = this.f("Wind", "Sideways drift and rolling gusts, percent", 35.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.lc g = this.f("Radius", "Snowfall radius around the camera in blocks", 24.0, 12.0, 40.0, 2.0);
   public final com.larpclient.module.lp L = this.addSetting(
      new com.larpclient.module.lp("Quality", "Snow detail and bounded flake count", "High", "Low", "Medium", "High")
   );
   public final com.larpclient.module.ln Z = this.addSetting(
      new com.larpclient.module.ln("Footsteps", "Clear powder beneath you and leave a soft walking trail", true)
   );
   public final com.larpclient.module.lc s = this.f("Trail Time", "Seconds before walking trails fill back in", 12.0, 2.0, 30.0, 1.0);
   private final lu J = new lu();
   private Vec3 w = Vec3.ZERO;
   private final on o = new on();
   private Object M;
   private Object V;
   private int x = -1;
   private int R = -1;

   private com.larpclient.module.lc f(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new com.larpclient.module.lc(var1, var2, var3, var5, var7, var9));
   }

   public SnowGroundModule() {
      super("Snow Ground", "Deep powder, wind-shaped snow drifts and dense falling snow", Category.RENDER, -1);
      S = this;
      this.v.x(this.p::getValue);
      this.r.x(this.p::getValue);
      this.g.x(this.p::getValue);
      this.s.x(this.Z::getValue);
      this.Z.W(var1 -> this.J.C());
      this.p.W(var1 -> this.o.k());
      this.L.W(var1 -> this.o.k());
      this.g.W(var1 -> this.o.k());
   }

   public static boolean z() {
      return S != null && S.isEnabled() ? S != null && S.isRuntimeActive() : false;
   }

   public static SnowGroundModule x() {
      return S;
   }

   public int A() {
      return this.L.i("High") ? 2 : (this.L.i("Medium") ? 1 : 0);
   }

   public static int r(int var0) {
      return var0 >= 2 ? 3200 : (var0 == 1 ? 1600 : 800);
   }

   public static boolean O() {
      if (S != null && S.isEnabled()) {
         SnowGroundModule var0 = S;
         return var0 != null && z() && var0.x != jk.v && var0.p.getValue() && var0.v.getValue() > 0.0 && !NoWeatherModule.b() && !RemovalsModule.s("weather");
      } else {
         return false;
      }
   }

   public on j() {
      return this.o;
   }

   public void X() {
      this.o.k();
      this.J.C();
      this.w = Vec3.ZERO;
      this.M = this.V = null;
   }

   public lu Z() {
      return this.J;
   }

   public void z(ByteBuffer var1, Vec3 var2, double var3, boolean var5) {
      if (this.isEnabled()) {
         this.J.P(var1, var2, this.w, var3, this.s.getValue(), var5 && z() && this.Z.getValue() && this.M == mc.level && this.V == mc.player);
      }
   }

   public void h() {
      this.X();
      this.x = jk.v;
   }

   public static boolean f() {
      return S != null && S.isEnabled() ? O() && S.o.k() > 0 : false;
   }

   @Override
   public void onEnable() {
      this.X();
      y3.G();
      y3.z();
      r4.V(
         "SnowGround",
         "Enabled powder and snowfall; quality={}, flake cap={}, radius={} blocks, footsteps={}, trail={} seconds",
         this.L.getValue(),
         r(this.A()),
         this.g.getValue(),
         this.Z.getValue(),
         this.s.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.X();
      y3.z();
      y3.J();
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && y3.S() ? "Material fallback" : "";
   }

   @Override
   public void onTick() {
      int var3 = jk.v;
      int var2 = this.R;
      int var10000 = Integer.compare(var2, var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         this.X();
         this.R = jk.v;
      }

      if (!this.isInGame()) {
         this.X();
      } else {
         if (this.M != mc.level || this.V != mc.player) {
            this.X();
            this.M = mc.level;
            this.V = mc.player;
         }

         int var10 = mc.isPaused();
         boolean var5 = false;
         byte var4 = (byte)var10;
         var10 = Integer.signum(var4);
         if (((var10 | -var10) >>> 31 ^ 1) != 0) {
            Vec3 var1 = new Vec3(mc.player.getX(), mc.player.getBoundingBox().minY, mc.player.getZ());
            this.w = var1;
            if (this.Z.getValue()) {
               int var15;
               label37: {
                  var12 = this.J;
                  if (mc.player.onGround() && !mc.player.isPassenger() && !mc.player.isSpectator()) {
                     var15 = mc.player.isInWater();
                     boolean var7 = false;
                     byte var6 = var15;
                     var15 = Integer.signum(var6);
                     if ((var15 | -var15) >>> 31 == 0) {
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

               var12.C(var1, (boolean)var15, ea.t(), this.s.getValue());
            } else {
               this.J.C();
            }

            if (!O()) {
               this.o.k();
            } else {
               this.o
                  .I(
                     mc.gameRenderer.mainCamera().position(),
                     new on.o(this.g.getValue(), this.v.getValue() / 100.0, this.r.getValue() / 100.0, r(this.A())),
                     (var0, var1x) -> {
                        if (!mc.level.hasChunk(var0 >> 4, var1x >> 4)) {
                           return on.u.UNAVAILABLE;
                        }

                        int var2x = mc.level.getHeight(Types.MOTION_BLOCKING, var0, var1x) - 1;
                        BlockPos var3x = new BlockPos(var0, var2x, var1x);
                        BlockState var4x = mc.level.getBlockState(var3x);
                        VoxelShape var5x = var4x.getCollisionShape(mc.level, var3x);
                        double var6x = var2x + (!var5x.isEmpty() && var4x.getFluidState().isEmpty() ? var5x.max(Axis.Y) : 1.0);
                        int var8x = mc.level.getMaxLocalRawBrightness(var3x.above());
                        return new on.u(var6x, var8x / 15.0);
                     }
                  );
            }
         }
      }
   }

   public void i(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (z() && this.M == mc.level && this.V == mc.player) {
            this.w = mc.player.getEyePosition(var1).subtract(0.0, mc.player.getEyeHeight(), 0.0);
            if (!O()) {
               this.o.k();
            } else {
               this.o.i(var2.pos, var1);
            }
         } else {
            this.X();
         }
      }
   }

   public void W(r6 var1) {
      if (this.isEnabled()) {
         if (z() && O() && this.M == mc.level && this.V == mc.player) {
            this.o.S(var1);
         }
      }
   }
}

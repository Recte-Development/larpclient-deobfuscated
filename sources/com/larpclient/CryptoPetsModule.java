package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import com.larpclient.module.df;
import com.larpclient.module.dh;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.joml.Quaternionf;

public final class CryptoPetsModule extends Module {
   public static final int z = 12;
   private static CryptoPetsModule w;
   private final Map<pr.h, dh> i = new LinkedHashMap<>();
   private final com.larpclient.module.d9 a = this.addSetting(new com.larpclient.module.d9("Count", "How many coins circle you", 5.0, 1.0, 12.0, 1.0));
   private final com.larpclient.module.d9 c = this.addSetting(new com.larpclient.module.d9("Size", "Coin size", 1.3, 0.5, 2.5, 0.05));
   private final com.larpclient.module.d9 N = this.addSetting(new com.larpclient.module.d9("Radius", "Distance from you in blocks", 1.2, 0.6, 3.0, 0.05));
   private final com.larpclient.module.d9 H = this.addSetting(new com.larpclient.module.d9("Orbit speed", "How fast the ring turns", 1.0, 0.0, 3.0, 0.05));
   private final dh m = this.addSetting(new dh("Sparkle trail", "Sparks follow each coin", true));
   private final dh S = this.addSetting(new dh("In first person", "Show the coins in first person", true));
   private Object g;
   private Object W;
   private final double[] M = new double[12];
   private final double[] u = new double[12];
   private double n;
   private double E = 1.0;
   private double p;
   private double o;
   private double y;
   private boolean e;
   private boolean T;
   private final ArrayDeque<CryptoPetsModule.v> x = new ArrayDeque<>();
   private final Random Y = new Random(1129470288L);
   private CryptoPetsModule.i j;

   public CryptoPetsModule() {
      super("Crypto Pets", "Spinning crypto coins that circle you", Category.RENDER, -1);
      w = this;

      for (pr.h var2 : pr.x) {
         this.i.put(var2, this.addSetting(new dh(var2.style(), "Include " + var2.style() + " coins", true)));
      }
   }

   public static CryptoPetsModule C() {
      return w;
   }

   public List<df<?>> E() {
      return List.copyOf(this.i.values());
   }

   public List<df<?>> b() {
      return List.of(this.a, this.c, this.N, this.H, this.m, this.S);
   }

   @Override
   public void onEnable() {
      this.P();
      mt.U("Pets", "Crypto Pets activated: {} coins", this.a.P().intValue());
   }

   @Override
   public void onDisable() {
      this.P();
   }

   public void P() {
      this.j = null;
      this.g = this.W = null;
      this.x.clear();
      this.y = 0.0;
      this.p = this.n = 0.0;
      this.E = 1.0;
      this.e = this.T = (boolean)0;
      Arrays.fill(this.M, 0.0);
      Arrays.fill(this.u, 0.0);
      this.o = ty.K();
      this.Y.setSeed(1129470288L);
   }

   private List<pr.h> R() {
      ArrayList var1 = new ArrayList();
      this.i.forEach((var1x, var2) -> {
         if (var2.P()) {
            var1.add(var1x);
         }
      });
      return var1;
   }

   public void I(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.j = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            Minecraft var3 = getMc();
            if (this.g != var3.level || this.W != var3.player) {
               this.P();
               this.g = var3.level;
               this.W = var3.player;
            }

            double var4 = Math.clamp(ty.K() - this.o, 0.0, 0.1);
            this.o = ty.K();
            this.p += var4;
            List var6 = this.R();
            int var7 = var6.isEmpty() ? 0 : this.a.P().intValue();
            double var8 = this.T ? 1.0 - Math.exp(-6.0 * var4) : 1.0;

            for (int var10 = 0; var10 < 12; var10++) {
               if (var10 < var7) {
                  this.M[var10] = this.M[var10] + ((Math.PI * 2) * var10 / var7 - this.M[var10]) * var8;
               }

               this.u[var10] = this.u[var10] + ((var10 < var7 ? 1 : 0) - this.u[var10]) * var8;
            }

            this.T = (boolean)1;
            this.n = (this.n + var4 * this.H.P() * 0.9) % (Math.PI * 2);
            if (_8.Q(var3.player) && !this.e) {
               this.E = 0.0;
            }

            this.e = _8.Q(var3.player);
            this.E = Math.min(1.0, this.E + var4 / 0.65);
            this.x.removeIf(var1x -> this.p - var1x.birth > var1x.lifetime);
            if (!this.m.P()) {
               this.x.clear();
               this.y = 0.0;
            }

            int var29 = var3.options.getCameraType().isFirstPerson() && !this.S.P() ? 0 : 1;
            if (var29 != 0 && !var6.isEmpty()) {
               pr.g();
               Vec3 var11 = var3.player.getEyePosition(var1);
               double var12 = this.E < 1.0 ? (1.0 - Math.pow(1.0 - this.E, 3.0)) * Math.PI * 2.0 : 0.0;
               ArrayList var14 = new ArrayList();

               for (int var15 = 0; var15 < 12; var15++) {
                  if (!(this.u[var15] < 0.01)) {
                     pr.h var16 = (pr.h)var6.get(var15 % var6.size());
                     double var17 = P(var16, this.c.P()) * this.u[var15];
                     double var19 = this.n + this.M[var15];
                     Vec3 var21 = var11.add(Math.cos(var19) * this.N.P(), -0.35 + Math.sin(this.p * 2.1 + var15 * 1.7) * 0.06, Math.sin(var19) * this.N.P());
                     BlockHitResult var22 = var3.level.clip(new ClipContext(var11, var21, Block.COLLIDER, Fluid.NONE, var3.player));
                     if (var22.getType() == Type.BLOCK) {
                        var21 = var22.getLocation().subtract(var21.subtract(var11).normalize().scale(0.3 * var17));
                     }

                     BlockHitResult var23 = var3.level.clip(new ClipContext(var21, var21.add(0.0, -4.5, 0.0), Block.COLLIDER, Fluid.ANY, var3.player));
                     Vec3 var24 = var23.getType() == Type.BLOCK ? var23.getLocation().add(0.0, 0.012, 0.0) : null;
                     double var25 = var24 == null ? 0.0 : Math.pow(Math.max(0.0, 1.0 - (var21.y - var24.y) / 4.5), 2.0) * 0.4 * this.u[var15];
                     Quaternionf var27 = new Quaternionf().rotationY((float)((this.p * 1.6 + var15 * 0.9 + var12) % (Math.PI * 2)));
                     var14.add(new CryptoPetsModule.r(var16, var21, var27, var17, var24, var25));
                     if (this.m.P() && var4 > 0.0 && this.Y.nextDouble() < var4 * 15.0) {
                        Vec3 var28 = var21.add(this.Y.nextGaussian() * 0.06 * var17, this.Y.nextGaussian() * 0.06 * var17, this.Y.nextGaussian() * 0.06 * var17);
                        this.x.addLast(new CryptoPetsModule.v(var28, this.p, 0.8 + this.Y.nextDouble() * 0.8, 0.8 + this.Y.nextDouble() * 0.8, var16.spark()));

                        while (this.x.size() > 96) {
                           this.x.removeFirst();
                        }
                     }
                  }
               }

               this.j = new CryptoPetsModule.i(List.copyOf(var14), this.p, this.m.P() ? 0.5 : 0.0, List.copyOf(this.x));
            }
         }
      }
   }

   public static double P(pr.h var0, double var1) {
      return var1 * var0.scale();
   }

   public boolean h() {
      return !this.isEnabled() ? false : this.j != null && this.j.glowStrength > 0.0 && !this.j.coins.isEmpty();
   }

   public void E(ki var1, boolean var2) {
      if (this.isEnabled()) {
         CryptoPetsModule.i var3 = this.j;
         if (var3 != null && (!var2 || !(var3.glowStrength <= 0.0))) {
            for (CryptoPetsModule.r var5 : var3.coins) {
               if (!var2) {
                  if (var5.shadow != null) {
                     var1.r(var5.shadow, 0.18 * var5.scale, 0.16 * var5.scale, _3.A(-16777216, var5.shadowAlpha));
                  }

                  pr.T(var1, var5.kind, var5.center, var5.rotation, (float)var5.scale);
               }
            }

            for (CryptoPetsModule.v var9 : var3.sparks) {
               double var6 = Math.clamp(1.0 - (var3.time - var9.birth) / var9.lifetime, 0.0, 1.0);
               var1.t(
                  var9.point.x,
                  var9.point.y + 0.08 * (1.0 - var6),
                  var9.point.z,
                  0.035 * this.c.P() * var6 * var9.scale,
                  var3.time,
                  _3.A(var9.color, var6 * var6 * var3.glowStrength)
               );
            }
         }
      }
   }

   public record i(List<CryptoPetsModule.r> coins, double time, double glowStrength, List<CryptoPetsModule.v> sparks) {
   }

   public record r(pr.h kind, Vec3 center, Quaternionf rotation, double scale, Vec3 shadow, double shadowAlpha) {
   }

   private record v(Vec3 point, double birth, double lifetime, double scale, int color) {
   }
}

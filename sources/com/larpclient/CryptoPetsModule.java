package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
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
   public static final int G = 12;
   private static CryptoPetsModule c;
   private final Map<jz.d, com.larpclient.module.ln> O = new LinkedHashMap<>();
   private final com.larpclient.module.lc C = this.addSetting(new com.larpclient.module.lc("Count", "How many coins circle you", 5.0, 1.0, 12.0, 1.0));
   private final com.larpclient.module.lc u = this.addSetting(new com.larpclient.module.lc("Size", "Coin size", 1.3, 0.5, 2.5, 0.05));
   private final com.larpclient.module.lc w = this.addSetting(new com.larpclient.module.lc("Radius", "Distance from you in blocks", 1.2, 0.6, 3.0, 0.05));
   private final com.larpclient.module.lc f = this.addSetting(new com.larpclient.module.lc("Orbit speed", "How fast the ring turns", 1.0, 0.0, 3.0, 0.05));
   private final com.larpclient.module.ln z = this.addSetting(new com.larpclient.module.ln("Sparkle trail", "Sparks follow each coin", true));
   private final com.larpclient.module.ln N = this.addSetting(new com.larpclient.module.ln("In first person", "Show the coins in first person", true));
   private Object X;
   private Object S;
   private final double[] Q = new double[12];
   private final double[] P = new double[12];
   private double K;
   private double q = 1.0;
   private double Y;
   private double r;
   private double m;
   private boolean I;
   private boolean o;
   private final ArrayDeque<CryptoPetsModule.h> d = new ArrayDeque<>();
   private final Random v = new Random(1129470288L);
   private CryptoPetsModule.q i;

   public CryptoPetsModule() {
      super("Crypto Pets", "Spinning crypto coins that circle you", Category.RENDER, -1);
      c = this;

      for (jz.d var2 : jz.u) {
         this.O.put(var2, this.addSetting(new com.larpclient.module.ln(var2.style(), "Include " + var2.style() + " coins", true)));
      }
   }

   public static CryptoPetsModule q() {
      return c;
   }

   public List<com.larpclient.module.l6<?>> m() {
      return List.copyOf(this.O.values());
   }

   public List<com.larpclient.module.l6<?>> J() {
      return List.of(this.C, this.u, this.w, this.f, this.z, this.N);
   }

   @Override
   public void onEnable() {
      this.c();
      r4.g("Pets", "Crypto Pets activated: {} coins", this.C.getValue().intValue());
   }

   @Override
   public void onDisable() {
      this.c();
   }

   public void c() {
      this.i = null;
      this.X = this.S = null;
      this.d.clear();
      this.m = 0.0;
      this.Y = this.K = 0.0;
      this.q = 1.0;
      this.I = this.o = (boolean)0;
      Arrays.fill(this.Q, 0.0);
      Arrays.fill(this.P, 0.0);
      this.r = ea.y();
      this.v.setSeed(1129470288L);
   }

   private List<jz.d> E() {
      ArrayList var1 = new ArrayList();
      this.O.forEach((var1x, var2) -> {
         if (var2.getValue()) {
            var1.add(var1x);
         }
      });
      return var1;
   }

   public void P(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.i = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            Minecraft var3 = getMc();
            if (this.X != var3.level || this.S != var3.player) {
               this.c();
               this.X = var3.level;
               this.S = var3.player;
            }

            double var4 = Math.clamp(ea.y() - this.r, 0.0, 0.1);
            this.r = ea.y();
            this.Y += var4;
            List var6 = this.E();
            int var7 = var6.isEmpty() ? 0 : this.C.getValue().intValue();
            double var8 = this.o ? 1.0 - Math.exp(-6.0 * var4) : 1.0;

            for (int var10 = 0; var10 < 12; var10++) {
               if (var10 < var7) {
                  this.Q[var10] = this.Q[var10] + ((Math.PI * 2) * var10 / var7 - this.Q[var10]) * var8;
               }

               this.P[var10] = this.P[var10] + ((var10 < var7 ? 1 : 0) - this.P[var10]) * var8;
            }

            this.o = (boolean)1;
            this.K = (this.K + var4 * this.f.getValue() * 0.9) % (Math.PI * 2);
            if (hp.o(var3.player) && !this.I) {
               this.q = 0.0;
            }

            this.I = hp.o(var3.player);
            this.q = Math.min(1.0, this.q + var4 / 0.65);
            this.d.removeIf(var1x -> this.Y - var1x.birth > var1x.lifetime);
            if (!this.z.getValue()) {
               this.d.clear();
               this.m = 0.0;
            }

            int var29 = var3.options.getCameraType().isFirstPerson() && !this.N.getValue() ? 0 : 1;
            if (var29 != 0 && !var6.isEmpty()) {
               jz.o();
               Vec3 var11 = var3.player.getEyePosition(var1);
               double var12 = this.q < 1.0 ? (1.0 - Math.pow(1.0 - this.q, 3.0)) * Math.PI * 2.0 : 0.0;
               ArrayList var14 = new ArrayList();

               for (int var15 = 0; var15 < 12; var15++) {
                  if (!(this.P[var15] < 0.01)) {
                     jz.d var16 = (jz.d)var6.get(var15 % var6.size());
                     double var17 = c(var16, this.u.getValue()) * this.P[var15];
                     double var19 = this.K + this.Q[var15];
                     Vec3 var21 = var11.add(
                        Math.cos(var19) * this.w.getValue(), -0.35 + Math.sin(this.Y * 2.1 + var15 * 1.7) * 0.06, Math.sin(var19) * this.w.getValue()
                     );
                     BlockHitResult var22 = var3.level.clip(new ClipContext(var11, var21, Block.COLLIDER, Fluid.NONE, var3.player));
                     if (var22.getType() == Type.BLOCK) {
                        var21 = var22.getLocation().subtract(var21.subtract(var11).normalize().scale(0.3 * var17));
                     }

                     BlockHitResult var23 = var3.level.clip(new ClipContext(var21, var21.add(0.0, -4.5, 0.0), Block.COLLIDER, Fluid.ANY, var3.player));
                     Vec3 var24 = var23.getType() == Type.BLOCK ? var23.getLocation().add(0.0, 0.012, 0.0) : null;
                     double var25 = var24 == null ? 0.0 : Math.pow(Math.max(0.0, 1.0 - (var21.y - var24.y) / 4.5), 2.0) * 0.4 * this.P[var15];
                     Quaternionf var27 = new Quaternionf().rotationY((float)((this.Y * 1.6 + var15 * 0.9 + var12) % (Math.PI * 2)));
                     var14.add(new CryptoPetsModule.y(var16, var21, var27, var17, var24, var25));
                     if (this.z.getValue() && var4 > 0.0 && this.v.nextDouble() < var4 * 15.0) {
                        Vec3 var28 = var21.add(this.v.nextGaussian() * 0.06 * var17, this.v.nextGaussian() * 0.06 * var17, this.v.nextGaussian() * 0.06 * var17);
                        this.d.addLast(new CryptoPetsModule.h(var28, this.Y, 0.8 + this.v.nextDouble() * 0.8, 0.8 + this.v.nextDouble() * 0.8, var16.spark()));

                        while (this.d.size() > 96) {
                           this.d.removeFirst();
                        }
                     }
                  }
               }

               this.i = new CryptoPetsModule.q(List.copyOf(var14), this.Y, this.z.getValue() ? 0.5 : 0.0, List.copyOf(this.d));
            }
         }
      }
   }

   public static double c(jz.d var0, double var1) {
      return var1 * var0.scale();
   }

   public boolean Q() {
      return !this.isEnabled() ? false : this.i != null && this.i.glowStrength > 0.0 && !this.i.coins.isEmpty();
   }

   public void L(r6 var1, boolean var2) {
      if (this.isEnabled()) {
         CryptoPetsModule.q var3 = this.i;
         if (var3 != null && (!var2 || !(var3.glowStrength <= 0.0))) {
            for (CryptoPetsModule.y var5 : var3.coins) {
               if (!var2) {
                  if (var5.shadow != null) {
                     var1.C(var5.shadow, 0.18 * var5.scale, 0.16 * var5.scale, oh.i(-16777216, var5.shadowAlpha));
                  }

                  jz.i(var1, var5.kind, var5.center, var5.rotation, (float)var5.scale);
               }
            }

            for (CryptoPetsModule.h var9 : var3.sparks) {
               double var6 = Math.clamp(1.0 - (var3.time - var9.birth) / var9.lifetime, 0.0, 1.0);
               var1.m(
                  var9.point.x,
                  var9.point.y + 0.08 * (1.0 - var6),
                  var9.point.z,
                  0.035 * this.u.getValue() * var6 * var9.scale,
                  var3.time,
                  oh.i(var9.color, var6 * var6 * var3.glowStrength)
               );
            }
         }
      }
   }

   private record h(Vec3 point, double birth, double lifetime, double scale, int color) {
   }

   public record q(List<CryptoPetsModule.y> coins, double time, double glowStrength, List<CryptoPetsModule.h> sparks) {
   }

   public record y(jz.d kind, Vec3 center, Quaternionf rotation, double scale, Vec3 shadow, double shadowAlpha) {
   }
}

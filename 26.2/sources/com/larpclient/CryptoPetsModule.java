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
   public static final int q = 12;
   private static CryptoPetsModule g;
   private final Map<pv.p, com.larpclient.module.s> R = new LinkedHashMap<>();
   private final com.larpclient.module.e I = this.addSetting(new com.larpclient.module.e("Count", "How many coins circle you", 5.0, 1.0, 12.0, 1.0));
   private final com.larpclient.module.e H = this.addSetting(new com.larpclient.module.e("Size", "Coin size", 1.3, 0.5, 2.5, 0.05));
   private final com.larpclient.module.e B = this.addSetting(new com.larpclient.module.e("Radius", "Distance from you in blocks", 1.2, 0.6, 3.0, 0.05));
   private final com.larpclient.module.e i = this.addSetting(new com.larpclient.module.e("Orbit speed", "How fast the ring turns", 1.0, 0.0, 3.0, 0.05));
   private final com.larpclient.module.s Z = this.addSetting(new com.larpclient.module.s("Sparkle trail", "Sparks follow each coin", true));
   private final com.larpclient.module.s J = this.addSetting(new com.larpclient.module.s("In first person", "Show the coins in first person", true));
   private Object x;
   private Object r;
   private final double[] w = new double[12];
   private final double[] s = new double[12];
   private double V;
   private double T = 1.0;
   private double F;
   private double f;
   private double v;
   private boolean C;
   private boolean P;
   private final ArrayDeque<CryptoPetsModule.d> W = new ArrayDeque<>();
   private final Random p = new Random(1129470288L);
   private CryptoPetsModule.p l;

   public CryptoPetsModule() {
      super("Crypto Pets", "Spinning crypto coins that circle you", Category.RENDER, -1);
      g = this;

      for (pv.p var2 : pv.o) {
         this.R.put(var2, this.addSetting(new com.larpclient.module.s(var2.style(), "Include " + var2.style() + " coins", true)));
      }
   }

   public static CryptoPetsModule O() {
      return g;
   }

   public List<com.larpclient.module.q<?>> x() {
      return List.copyOf(this.R.values());
   }

   public List<com.larpclient.module.q<?>> u() {
      return List.of(this.I, this.H, this.B, this.i, this.Z, this.J);
   }

   @Override
   public void onEnable() {
      this.E();
      h.U("Pets", "Crypto Pets activated: {} coins", this.I.getValue().intValue());
   }

   @Override
   public void onDisable() {
      this.E();
   }

   public void E() {
      this.l = null;
      this.x = this.r = null;
      this.W.clear();
      this.v = 0.0;
      this.F = this.V = 0.0;
      this.T = 1.0;
      this.C = this.P = (boolean)0;
      Arrays.fill(this.w, 0.0);
      Arrays.fill(this.s, 0.0);
      this.f = cf.F();
      this.p.setSeed(1129470288L);
   }

   private List<pv.p> W() {
      ArrayList var1 = new ArrayList();
      this.R.forEach((var1x, var2) -> {
         if (var2.getValue()) {
            var1.add(var1x);
         }
      });
      return var1;
   }

   public void Q(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.l = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            Minecraft var3 = getMc();
            if (this.x != var3.level || this.r != var3.player) {
               this.E();
               this.x = var3.level;
               this.r = var3.player;
            }

            double var4 = Math.clamp(cf.F() - this.f, 0.0, 0.1);
            this.f = cf.F();
            this.F += var4;
            List var6 = this.W();
            int var7 = var6.isEmpty() ? 0 : this.I.getValue().intValue();
            double var8 = this.P ? 1.0 - Math.exp(-6.0 * var4) : 1.0;

            for (int var10 = 0; var10 < 12; var10++) {
               if (var10 < var7) {
                  this.w[var10] = this.w[var10] + ((Math.PI * 2) * var10 / var7 - this.w[var10]) * var8;
               }

               this.s[var10] = this.s[var10] + ((var10 < var7 ? 1 : 0) - this.s[var10]) * var8;
            }

            this.P = (boolean)1;
            this.V = (this.V + var4 * this.i.getValue() * 0.9) % (Math.PI * 2);
            if (ks.M(var3.player) && !this.C) {
               this.T = 0.0;
            }

            this.C = ks.M(var3.player);
            this.T = Math.min(1.0, this.T + var4 / 0.65);
            this.W.removeIf(var1x -> this.F - var1x.birth > var1x.lifetime);
            if (!this.Z.getValue()) {
               this.W.clear();
               this.v = 0.0;
            }

            int var29 = var3.options.getCameraType().isFirstPerson() && !this.J.getValue() ? 0 : 1;
            if (var29 != 0 && !var6.isEmpty()) {
               pv.J();
               Vec3 var11 = var3.player.getEyePosition(var1);
               double var12 = this.T < 1.0 ? (1.0 - Math.pow(1.0 - this.T, 3.0)) * Math.PI * 2.0 : 0.0;
               ArrayList var14 = new ArrayList();

               for (int var15 = 0; var15 < 12; var15++) {
                  if (!(this.s[var15] < 0.01)) {
                     pv.p var16 = (pv.p)var6.get(var15 % var6.size());
                     double var17 = o(var16, this.H.getValue()) * this.s[var15];
                     double var19 = this.V + this.w[var15];
                     Vec3 var21 = var11.add(
                        Math.cos(var19) * this.B.getValue(), -0.35 + Math.sin(this.F * 2.1 + var15 * 1.7) * 0.06, Math.sin(var19) * this.B.getValue()
                     );
                     BlockHitResult var22 = var3.level.clip(new ClipContext(var11, var21, Block.COLLIDER, Fluid.NONE, var3.player));
                     if (var22.getType() == Type.BLOCK) {
                        var21 = var22.getLocation().subtract(var21.subtract(var11).normalize().scale(0.3 * var17));
                     }

                     BlockHitResult var23 = var3.level.clip(new ClipContext(var21, var21.add(0.0, -4.5, 0.0), Block.COLLIDER, Fluid.ANY, var3.player));
                     Vec3 var24 = var23.getType() == Type.BLOCK ? var23.getLocation().add(0.0, 0.012, 0.0) : null;
                     double var25 = var24 == null ? 0.0 : Math.pow(Math.max(0.0, 1.0 - (var21.y - var24.y) / 4.5), 2.0) * 0.4 * this.s[var15];
                     Quaternionf var27 = new Quaternionf().rotationY((float)((this.F * 1.6 + var15 * 0.9 + var12) % (Math.PI * 2)));
                     var14.add(new CryptoPetsModule.f(var16, var21, var27, var17, var24, var25));
                     if (this.Z.getValue() && var4 > 0.0 && this.p.nextDouble() < var4 * 15.0) {
                        Vec3 var28 = var21.add(this.p.nextGaussian() * 0.06 * var17, this.p.nextGaussian() * 0.06 * var17, this.p.nextGaussian() * 0.06 * var17);
                        this.W.addLast(new CryptoPetsModule.d(var28, this.F, 0.8 + this.p.nextDouble() * 0.8, 0.8 + this.p.nextDouble() * 0.8, var16.spark()));

                        while (this.W.size() > 96) {
                           this.W.removeFirst();
                        }
                     }
                  }
               }

               this.l = new CryptoPetsModule.p(List.copyOf(var14), this.F, this.Z.getValue() ? 0.5 : 0.0, List.copyOf(this.W));
            }
         }
      }
   }

   public static double o(pv.p var0, double var1) {
      return var1 * var0.scale();
   }

   public boolean m() {
      return !this.isEnabled() ? false : this.l != null && this.l.glowStrength > 0.0 && !this.l.coins.isEmpty();
   }

   public void I(kj var1, boolean var2) {
      if (this.isEnabled()) {
         CryptoPetsModule.p var3 = this.l;
         if (var3 != null && (!var2 || !(var3.glowStrength <= 0.0))) {
            for (CryptoPetsModule.f var5 : var3.coins) {
               if (!var2) {
                  if (var5.shadow != null) {
                     var1.I(var5.shadow, 0.18 * var5.scale, 0.16 * var5.scale, uh.H(-16777216, var5.shadowAlpha));
                  }

                  pv.h(var1, var5.kind, var5.center, var5.rotation, (float)var5.scale);
               }
            }

            for (CryptoPetsModule.d var9 : var3.sparks) {
               double var6 = Math.clamp(1.0 - (var3.time - var9.birth) / var9.lifetime, 0.0, 1.0);
               var1.P(
                  var9.point.x,
                  var9.point.y + 0.08 * (1.0 - var6),
                  var9.point.z,
                  0.035 * this.H.getValue() * var6 * var9.scale,
                  var3.time,
                  uh.H(var9.color, var6 * var6 * var3.glowStrength)
               );
            }
         }
      }
   }

   private record d(Vec3 point, double birth, double lifetime, double scale, int color) {
   }

   public record f(pv.p kind, Vec3 center, Quaternionf rotation, double scale, Vec3 shadow, double shadowAlpha) {
   }

   public record p(List<CryptoPetsModule.f> coins, double time, double glowStrength, List<CryptoPetsModule.d> sparks) {
   }
}

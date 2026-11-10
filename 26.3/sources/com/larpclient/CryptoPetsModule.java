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
   public static final int c = 12;
   private static CryptoPetsModule G;
   private final Map<ho.z, com.larpclient.module.m> s = new LinkedHashMap<>();
   private final com.larpclient.module.b I = this.addSetting(new com.larpclient.module.b("Count", "How many coins circle you", 5.0, 1.0, 12.0, 1.0));
   private final com.larpclient.module.b L = this.addSetting(new com.larpclient.module.b("Size", "Coin size", 1.3, 0.5, 2.5, 0.05));
   private final com.larpclient.module.b u = this.addSetting(new com.larpclient.module.b("Radius", "Distance from you in blocks", 1.2, 0.6, 3.0, 0.05));
   private final com.larpclient.module.b W = this.addSetting(new com.larpclient.module.b("Orbit speed", "How fast the ring turns", 1.0, 0.0, 3.0, 0.05));
   private final com.larpclient.module.m Z = this.addSetting(new com.larpclient.module.m("Sparkle trail", "Sparks follow each coin", true));
   private final com.larpclient.module.m g = this.addSetting(new com.larpclient.module.m("In first person", "Show the coins in first person", true));
   private Object V;
   private Object j;
   private final double[] m = new double[12];
   private final double[] O = new double[12];
   private double p;
   private double Q = 1.0;
   private double J;
   private double v;
   private double t;
   private boolean q;
   private boolean o;
   private final ArrayDeque<CryptoPetsModule.q> n = new ArrayDeque<>();
   private final Random y = new Random(1129470288L);
   private CryptoPetsModule.v l;

   public CryptoPetsModule() {
      super("Crypto Pets", "Spinning crypto coins that circle you", Category.RENDER, -1);
      G = this;

      for (ho.z var2 : ho.X) {
         this.s.put(var2, this.addSetting(new com.larpclient.module.m(var2.style(), "Include " + var2.style() + " coins", true)));
      }
   }

   public static CryptoPetsModule v() {
      return G;
   }

   public List<com.larpclient.module.r<?>> D() {
      return List.copyOf(this.s.values());
   }

   public List<com.larpclient.module.r<?>> h() {
      return List.of(this.I, this.L, this.u, this.W, this.Z, this.g);
   }

   @Override
   public void onEnable() {
      this.Y();
      _q.c("Pets", "Crypto Pets activated: {} coins", this.I.getValue().intValue());
   }

   @Override
   public void onDisable() {
      this.Y();
   }

   public void Y() {
      this.l = null;
      this.V = this.j = null;
      this.n.clear();
      this.t = 0.0;
      this.J = this.p = 0.0;
      this.Q = 1.0;
      this.q = this.o = (boolean)0;
      Arrays.fill(this.m, 0.0);
      Arrays.fill(this.O, 0.0);
      this.v = hz.Q();
      this.y.setSeed(1129470288L);
   }

   private List<ho.z> l() {
      ArrayList var1 = new ArrayList();
      this.s.forEach((var1x, var2) -> {
         if (var2.getValue()) {
            var1.add(var1x);
         }
      });
      return var1;
   }

   public void P(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.l = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            Minecraft var3 = getMc();
            if (this.V != var3.level || this.j != var3.player) {
               this.Y();
               this.V = var3.level;
               this.j = var3.player;
            }

            double var4 = Math.clamp(hz.Q() - this.v, 0.0, 0.1);
            this.v = hz.Q();
            this.J += var4;
            List var6 = this.l();
            int var7 = var6.isEmpty() ? 0 : this.I.getValue().intValue();
            double var8 = this.o ? 1.0 - Math.exp(-6.0 * var4) : 1.0;

            for (int var10 = 0; var10 < 12; var10++) {
               if (var10 < var7) {
                  this.m[var10] = this.m[var10] + ((Math.PI * 2) * var10 / var7 - this.m[var10]) * var8;
               }

               this.O[var10] = this.O[var10] + ((var10 < var7 ? 1 : 0) - this.O[var10]) * var8;
            }

            this.o = (boolean)1;
            this.p = (this.p + var4 * this.W.getValue() * 0.9) % (Math.PI * 2);
            if (h8.S(var3.player) && !this.q) {
               this.Q = 0.0;
            }

            this.q = h8.S(var3.player);
            this.Q = Math.min(1.0, this.Q + var4 / 0.65);
            this.n.removeIf(var1x -> this.J - var1x.birth > var1x.lifetime);
            if (!this.Z.getValue()) {
               this.n.clear();
               this.t = 0.0;
            }

            int var29 = var3.options.getCameraType().isFirstPerson() && !this.g.getValue() ? 0 : 1;
            if (var29 != 0 && !var6.isEmpty()) {
               ho.l();
               Vec3 var11 = var3.player.getEyePosition(var1);
               double var12 = this.Q < 1.0 ? (1.0 - Math.pow(1.0 - this.Q, 3.0)) * Math.PI * 2.0 : 0.0;
               ArrayList var14 = new ArrayList();

               for (int var15 = 0; var15 < 12; var15++) {
                  if (!(this.O[var15] < 0.01)) {
                     ho.z var16 = (ho.z)var6.get(var15 % var6.size());
                     double var17 = r(var16, this.L.getValue()) * this.O[var15];
                     double var19 = this.p + this.m[var15];
                     Vec3 var21 = var11.add(
                        Math.cos(var19) * this.u.getValue(), -0.35 + Math.sin(this.J * 2.1 + var15 * 1.7) * 0.06, Math.sin(var19) * this.u.getValue()
                     );
                     BlockHitResult var22 = var3.level.clip(new ClipContext(var11, var21, Block.COLLIDER, Fluid.NONE, var3.player));
                     if (var22.getType() == Type.BLOCK) {
                        var21 = var22.getLocation().subtract(var21.subtract(var11).normalize().scale(0.3 * var17));
                     }

                     BlockHitResult var23 = var3.level.clip(new ClipContext(var21, var21.add(0.0, -4.5, 0.0), Block.COLLIDER, Fluid.ANY, var3.player));
                     Vec3 var24 = var23.getType() == Type.BLOCK ? var23.getLocation().add(0.0, 0.012, 0.0) : null;
                     double var25 = var24 == null ? 0.0 : Math.pow(Math.max(0.0, 1.0 - (var21.y - var24.y) / 4.5), 2.0) * 0.4 * this.O[var15];
                     Quaternionf var27 = new Quaternionf().rotationY((float)((this.J * 1.6 + var15 * 0.9 + var12) % (Math.PI * 2)));
                     var14.add(new CryptoPetsModule.y(var16, var21, var27, var17, var24, var25));
                     if (this.Z.getValue() && var4 > 0.0 && this.y.nextDouble() < var4 * 15.0) {
                        Vec3 var28 = var21.add(this.y.nextGaussian() * 0.06 * var17, this.y.nextGaussian() * 0.06 * var17, this.y.nextGaussian() * 0.06 * var17);
                        this.n.addLast(new CryptoPetsModule.q(var28, this.J, 0.8 + this.y.nextDouble() * 0.8, 0.8 + this.y.nextDouble() * 0.8, var16.spark()));

                        while (this.n.size() > 96) {
                           this.n.removeFirst();
                        }
                     }
                  }
               }

               this.l = new CryptoPetsModule.v(List.copyOf(var14), this.J, this.Z.getValue() ? 0.5 : 0.0, List.copyOf(this.n));
            }
         }
      }
   }

   public static double r(ho.z var0, double var1) {
      return var1 * var0.scale();
   }

   public boolean B() {
      return !this.isEnabled() ? false : this.l != null && this.l.glowStrength > 0.0 && !this.l.coins.isEmpty();
   }

   public void p(fz var1, boolean var2) {
      if (this.isEnabled()) {
         CryptoPetsModule.v var3 = this.l;
         if (var3 != null && (!var2 || !(var3.glowStrength <= 0.0))) {
            for (CryptoPetsModule.y var5 : var3.coins) {
               if (!var2) {
                  if (var5.shadow != null) {
                     var1.q(var5.shadow, 0.18 * var5.scale, 0.16 * var5.scale, vv.N(-16777216, var5.shadowAlpha));
                  }

                  ho.w(var1, var5.kind, var5.center, var5.rotation, (float)var5.scale);
               }
            }

            for (CryptoPetsModule.q var9 : var3.sparks) {
               double var6 = Math.clamp(1.0 - (var3.time - var9.birth) / var9.lifetime, 0.0, 1.0);
               var1.K(
                  var9.point.x,
                  var9.point.y + 0.08 * (1.0 - var6),
                  var9.point.z,
                  0.035 * this.L.getValue() * var6 * var9.scale,
                  var3.time,
                  vv.N(var9.color, var6 * var6 * var3.glowStrength)
               );
            }
         }
      }
   }

   private record q(Vec3 point, double birth, double lifetime, double scale, int color) {
   }

   public record v(List<CryptoPetsModule.y> coins, double time, double glowStrength, List<CryptoPetsModule.q> sparks) {
   }

   public record y(ho.z kind, Vec3 center, Quaternionf rotation, double scale, Vec3 shadow, double shadowAlpha) {
   }
}

package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public final class CrystalChamsModule extends Module {
   private static CrystalChamsModule R;
   private final com.larpclient.module.o J = this.addSetting(
      new com.larpclient.module.o("Model", "Crystal appearance", "Vanilla", "Vanilla", "Gem", "Orb", "Prism")
   );
   private final com.larpclient.module.o B = this.addSetting(
      new com.larpclient.module.o("Color mode", "Crystal palette", "Theme", "Theme", "Custom", "Rainbow", "Original")
   );
   private final com.larpclient.module.h K = this.addSetting(new com.larpclient.module.h("Color", "Custom crystal tint", -1459895));
   private final com.larpclient.module.b t = this.addSetting(new com.larpclient.module.b("Opacity", "Crystal opacity in percent", 100.0, 10.0, 100.0, 5.0));
   private final com.larpclient.module.m E = this.addSetting(new com.larpclient.module.m("Glow", "Full brightness and luminous core", true));
   private final com.larpclient.module.m i = this.addSetting(new com.larpclient.module.m("Rings", "Thin orbiting rings", true));
   private final com.larpclient.module.b p = this.addSetting(new com.larpclient.module.b("Size", "Visual scale in percent", 100.0, 20.0, 300.0, 5.0));
   private final com.larpclient.module.b b = this.addSetting(new com.larpclient.module.b("Spin rate", "Rotation speed in percent", 100.0, 0.0, 600.0, 10.0));
   private final com.larpclient.module.b H = this.addSetting(new com.larpclient.module.b("Bounce", "Bob height in percent", 100.0, 0.0, 300.0, 10.0));
   private final com.larpclient.module.o o = this.addSetting(
      new com.larpclient.module.o("Base", "Base plate visibility", "Default", "Default", "Always", "Never")
   );
   private CrystalChamsModule.w n;

   public CrystalChamsModule() {
      super("Crystal Chams", "Crystal colors, motion and Gem, Orb or Prism models", Category.RENDER, -1);
      R = this;
      this.K.a(() -> this.B.w("Custom"));
      this.i.a(() -> !this.J.w("Vanilla"));
   }

   public static CrystalChamsModule W() {
      return R;
   }

   public List<com.larpclient.module.r<?>> S() {
      return List.of(this.J, this.p, this.i, this.o);
   }

   public List<com.larpclient.module.r<?>> B() {
      return List.of(this.B, this.K, this.t, this.E);
   }

   public List<com.larpclient.module.r<?>> u() {
      return List.of(this.b, this.H);
   }

   public boolean L() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && !this.J.w("Vanilla");
   }

   public float a() {
      return this.isRuntimeActive() ? this.p.getValue().floatValue() / 100.0F : 1.0F;
   }

   public float u() {
      return this.isRuntimeActive() ? this.b.getValue().floatValue() / 100.0F : 1.0F;
   }

   public float n() {
      return this.isRuntimeActive() ? this.H.getValue().floatValue() / 100.0F : 1.0F;
   }

   public boolean x() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.E.getValue();
   }

   public boolean S() {
      return this.isEnabled() ? this.t.getValue() < 100.0 : false;
   }

   public boolean a() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && (!this.B.w("Original") || this.S() || this.E.getValue());
   }

   public Boolean t() {
      return this.isRuntimeActive() && !this.o.w("Default") ? this.o.w("Always") : null;
   }

   public int O(int var1) {
      double var2 = hz.Q();

      int var4 = switch ((String)this.B.getValue()) {
         case "Custom" -> this.K.getValue();
         case "Original" -> -1;
         case "Rainbow" -> vv.b((float)((var2 * 0.12 + var1 * 0.13) % 1.0), 0.7F, 1.0F);
         default -> vv.Q(c.y, c.t, 0.5 + 0.5 * Math.sin(var2 + var1 * 0.1));
      };
      return vv.N(var4, this.t.getValue() / 100.0);
   }

   @Override
   public void onEnable() {
      _q.c("CrystalChams", "Native crystal appearance activated: {}", this.J.getValue());
   }

   @Override
   public void onDisable() {
      this.r();
   }

   public void r() {
      this.n = null;
   }

   public boolean R(Object var1) {
      if (this.isEnabled()) {
         CrystalChamsModule.w var2 = this.n;
         return wp.Q() && this.L() && var2 != null && var1 instanceof ve var3
            ? var2.crystals.stream().anyMatch(var1x -> var1x.id == var3.larpCrystalId())
            : false;
      } else {
         return false;
      }
   }

   public void d(float var1) {
      if (this.isEnabled()) {
         this.n = null;
         if (this.L() && this.isInGame()) {
            ArrayList var2 = new ArrayList();

            for (Entity var4 : getMc().level.entitiesForRendering()) {
               if (var4 instanceof EndCrystal var5 && !(var4.distanceToSqr(getMc().player) > 9216.0)) {
                  if (var2.size() >= 128) {
                     break;
                  }

                  float var6 = var5.time + var1;
                  double var7 = Math.sin(var6 * 0.2) / 2.0 + 0.5;
                  double var9 = (var7 * var7 + var7) * 0.4 * this.n();
                  Vec3 var11 = var5.getPosition(var1);
                  Vec3 var12 = var11.add(0.0, 1.2 + var9, 0.0);
                  int var13 = this.B.w("Original") ? vv.N(-3707905, this.t.getValue() / 100.0) : this.O((int)(var11.x * 7.0 + var11.z * 13.0));
                  var2.add(
                     new CrystalChamsModule.m(
                        var5.getId(), var12, var11, var6, Math.toRadians(var6 * 3.0F * this.u()), var13, this.t() == null ? var5.showsBottom() : this.t()
                     )
                  );
               }
            }

            this.n = new CrystalChamsModule.w(this.J.getValue(), this.a(), this.i.getValue(), this.E.getValue(), List.copyOf(var2));
         }
      }
   }

   public boolean R() {
      return !this.isEnabled() ? false : this.n != null && this.n.glow && !this.n.crystals.isEmpty();
   }

   public void o(fz var1, boolean var2) {
      if (this.isEnabled()) {
         CrystalChamsModule.w var3 = this.n;
         if (var3 != null && (!var2 || var3.glow)) {
            for (CrystalChamsModule.m var5 : var3.crystals) {
               int var7;
               iv var8;
               label105: {
                  int var6 = var5.tint;
                  var7 = vv.Q(var6, vv.N(-1, (var6 >>> 24) / 255.0), 0.35);
                  var8 = new iv(var1, var5.center, new Quaternionf(), var3.scale, var2);
                  if (!var2 && var5.base) {
                     iv var9 = new iv(var1, var5.foot, new Quaternionf(), 1.0, false);
                     var9.O(0.0, 0.125, 0.0, 0.375, 0.125, 0.375, -14278098);
                     var9.K(0.27, 0.012, new Quaternionf(), var6);
                  }

                  iv var15;
                  int var16;
                  int var17;
                  switch (var3.model) {
                     case "Gem":
                        var8.R(0.13, 0.24, 0.07, -var5.angle * 1.7, var7);
                        if (!var2) {
                           var8.R(0.36, 0.6, 0.22, var5.angle, vv.O(var6, 0.85));
                        }
                        break label105;
                     case "Orb":
                        double var24 = 0.85 + 0.15 * Math.sin(var5.age * 0.25);
                        var8.J(0.0, 0.0, 0.0, 0.15 * var24, 0.15 * var24, 0.15 * var24, var7);
                        if (!var2) {
                           var8.J(0.0, 0.0, 0.0, 0.42, 0.42, 0.42, vv.O(var6, 0.7));
                           var8.K(0.48, 0.03, new Quaternionf().rotationY((float)var5.angle).rotateX(0.35F), vv.Q(var6, -1459895, 0.4));
                        }
                        break label105;
                     default:
                        float var11 = (float)Math.sqrt(0.5);
                        Quaternionf var12 = new Quaternionf().rotationY((float)var5.angle).rotateAxis((float) (Math.PI / 3), var11, 0.0F, var11);
                        Quaternionf var13 = new Quaternionf(var12)
                           .mul(new Quaternionf().setAngleAxis((float) (Math.PI / 3), var11, 0.0F, var11).rotateY((float)var5.angle * 1.4F));
                        iv var14 = new iv(var1, var5.center, var13, var3.scale, var2);
                        var14.O(0.0, 0.0, 0.0, 0.126, 0.126, 0.126, var7);
                        if (var2) {
                           break label105;
                        }

                        var14.O(0.0, 0.0, 0.0, 0.225, 0.225, 0.225, vv.O(var6, 0.55));
                        var15 = new iv(var1, var5.center, var12, var3.scale, false);
                        var15.O(0.0, 0.0, 0.0, 0.3, 0.3, 0.3, vv.O(var6, 0.5));
                        var16 = vv.Q(var6, -1459895, 0.55);
                        var17 = 0;
                  }

                  while (var17 < 3) {
                     for (byte var18 = -1; var18 <= 1; var18 += 2) {
                        for (byte var19 = -1; var19 <= 1; var19 += 2) {
                           double[] var20 = new double[3];
                           double[] var21 = new double[]{0.022, 0.022, 0.022};
                           var20[(var17 + 1) % 3] = var18 * 0.3;
                           var20[(var17 + 2) % 3] = var19 * 0.3;
                           var21[var17] = 0.322;
                           var15.O(var20[0], var20[1], var20[2], var21[0], var21[1], var21[2], var16);
                        }
                     }

                     var17++;
                  }
               }

               if (var3.rings) {
                  for (int var23 = 0; var23 < 2; var23++) {
                     var8.K(
                        0.62 + var23 * 0.1,
                        0.008,
                        new Quaternionf().rotationY((float)(var5.angle * (var23 == 0 ? 1.6 : -1.2) + var23)).rotateX(0.5F + var23 * 0.6F),
                        vv.O(var7, 0.9)
                     );
                  }
               }
            }
         }
      }
   }

   private record m(int id, Vec3 center, Vec3 foot, float age, double angle, int tint, boolean base) {
   }

   private record w(String model, double scale, boolean rings, boolean glow, List<CrystalChamsModule.m> crystals) {
   }
}

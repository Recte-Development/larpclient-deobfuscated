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
   private static CrystalChamsModule m;
   private final com.larpclient.module.l N = this.addSetting(
      new com.larpclient.module.l("Model", "Crystal appearance", "Vanilla", "Vanilla", "Gem", "Orb", "Prism")
   );
   private final com.larpclient.module.l O = this.addSetting(
      new com.larpclient.module.l("Color mode", "Crystal palette", "Theme", "Theme", "Custom", "Rainbow", "Original")
   );
   private final com.larpclient.module.i Q = this.addSetting(new com.larpclient.module.i("Color", "Custom crystal tint", -1459895));
   private final com.larpclient.module.e B = this.addSetting(new com.larpclient.module.e("Opacity", "Crystal opacity in percent", 100.0, 10.0, 100.0, 5.0));
   private final com.larpclient.module.s z = this.addSetting(new com.larpclient.module.s("Glow", "Full brightness and luminous core", true));
   private final com.larpclient.module.s w = this.addSetting(new com.larpclient.module.s("Rings", "Thin orbiting rings", true));
   private final com.larpclient.module.e Z = this.addSetting(new com.larpclient.module.e("Size", "Visual scale in percent", 100.0, 20.0, 300.0, 5.0));
   private final com.larpclient.module.e K = this.addSetting(new com.larpclient.module.e("Spin rate", "Rotation speed in percent", 100.0, 0.0, 600.0, 10.0));
   private final com.larpclient.module.e E = this.addSetting(new com.larpclient.module.e("Bounce", "Bob height in percent", 100.0, 0.0, 300.0, 10.0));
   private final com.larpclient.module.l A = this.addSetting(
      new com.larpclient.module.l("Base", "Base plate visibility", "Default", "Default", "Always", "Never")
   );
   private CrystalChamsModule.n a;

   public CrystalChamsModule() {
      super("Crystal Chams", "Crystal colors, motion and Gem, Orb or Prism models", Category.RENDER, -1);
      m = this;
      this.Q.P(() -> this.O.O("Custom"));
      this.w.P(() -> !this.N.O("Vanilla"));
   }

   public static CrystalChamsModule Y() {
      return m;
   }

   public List<com.larpclient.module.q<?>> C() {
      return List.of(this.N, this.Z, this.w, this.A);
   }

   public List<com.larpclient.module.q<?>> m() {
      return List.of(this.O, this.Q, this.B, this.z);
   }

   public List<com.larpclient.module.q<?>> X() {
      return List.of(this.K, this.E);
   }

   public boolean C() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && !this.N.O("Vanilla");
   }

   public float a() {
      return this.isRuntimeActive() ? this.Z.getValue().floatValue() / 100.0F : 1.0F;
   }

   public float w() {
      return this.isRuntimeActive() ? this.K.getValue().floatValue() / 100.0F : 1.0F;
   }

   public float M() {
      return this.isRuntimeActive() ? this.E.getValue().floatValue() / 100.0F : 1.0F;
   }

   public boolean y() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.z.getValue();
   }

   public boolean H() {
      return this.isEnabled() ? this.B.getValue() < 100.0 : false;
   }

   public boolean i() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && (!this.O.O("Original") || this.H() || this.z.getValue());
   }

   public Boolean h() {
      return this.isRuntimeActive() && !this.A.O("Default") ? this.A.O("Always") : null;
   }

   public int m(int var1) {
      double var2 = cf.F();

      int var4 = switch ((String)this.O.getValue()) {
         case "Custom" -> this.Q.getValue();
         case "Original" -> -1;
         case "Rainbow" -> uh.n((float)((var2 * 0.12 + var1 * 0.13) % 1.0), 0.7F, 1.0F);
         default -> uh.q(ho.J, ho.ds, 0.5 + 0.5 * Math.sin(var2 + var1 * 0.1));
      };
      return uh.H(var4, this.B.getValue() / 100.0);
   }

   @Override
   public void onEnable() {
      h.U("CrystalChams", "Native crystal appearance activated: {}", this.N.getValue());
   }

   @Override
   public void onDisable() {
      this.W();
   }

   public void W() {
      this.a = null;
   }

   public boolean B(Object var1) {
      if (this.isEnabled()) {
         CrystalChamsModule.n var2 = this.a;
         return kz.x() && this.C() && var2 != null && var1 instanceof c var3
            ? var2.crystals.stream().anyMatch(var1x -> var1x.id == var3.larpCrystalId())
            : false;
      } else {
         return false;
      }
   }

   public void r(float var1) {
      if (this.isEnabled()) {
         this.a = null;
         if (this.C() && this.isInGame()) {
            ArrayList var2 = new ArrayList();

            for (Entity var4 : getMc().level.entitiesForRendering()) {
               if (var4 instanceof EndCrystal var5 && !(var4.distanceToSqr(getMc().player) > 9216.0)) {
                  if (var2.size() >= 128) {
                     break;
                  }

                  float var6 = var5.time + var1;
                  double var7 = Math.sin(var6 * 0.2) / 2.0 + 0.5;
                  double var9 = (var7 * var7 + var7) * 0.4 * this.M();
                  Vec3 var11 = var5.getPosition(var1);
                  Vec3 var12 = var11.add(0.0, 1.2 + var9, 0.0);
                  int var13 = this.O.O("Original") ? uh.H(-3707905, this.B.getValue() / 100.0) : this.m((int)(var11.x * 7.0 + var11.z * 13.0));
                  var2.add(
                     new CrystalChamsModule.x(
                        var5.getId(), var12, var11, var6, Math.toRadians(var6 * 3.0F * this.w()), var13, this.h() == null ? var5.showsBottom() : this.h()
                     )
                  );
               }
            }

            this.a = new CrystalChamsModule.n(this.N.getValue(), this.a(), this.w.getValue(), this.z.getValue(), List.copyOf(var2));
         }
      }
   }

   public boolean B() {
      return !this.isEnabled() ? false : this.a != null && this.a.glow && !this.a.crystals.isEmpty();
   }

   public void E(kj var1, boolean var2) {
      if (this.isEnabled()) {
         CrystalChamsModule.n var3 = this.a;
         if (var3 != null && (!var2 || var3.glow)) {
            for (CrystalChamsModule.x var5 : var3.crystals) {
               int var7;
               ku var8;
               label105: {
                  int var6 = var5.tint;
                  var7 = uh.q(var6, uh.H(-1, (var6 >>> 24) / 255.0), 0.35);
                  var8 = new ku(var1, var5.center, new Quaternionf(), var3.scale, var2);
                  if (!var2 && var5.base) {
                     ku var9 = new ku(var1, var5.foot, new Quaternionf(), 1.0, false);
                     var9.i(0.0, 0.125, 0.0, 0.375, 0.125, 0.375, -14278098);
                     var9.g(0.27, 0.012, new Quaternionf(), var6);
                  }

                  ku var15;
                  int var16;
                  int var17;
                  switch (var3.model) {
                     case "Gem":
                        var8.t(0.13, 0.24, 0.07, -var5.angle * 1.7, var7);
                        if (!var2) {
                           var8.t(0.36, 0.6, 0.22, var5.angle, uh.F(var6, 0.85));
                        }
                        break label105;
                     case "Orb":
                        double var24 = 0.85 + 0.15 * Math.sin(var5.age * 0.25);
                        var8.s(0.0, 0.0, 0.0, 0.15 * var24, 0.15 * var24, 0.15 * var24, var7);
                        if (!var2) {
                           var8.s(0.0, 0.0, 0.0, 0.42, 0.42, 0.42, uh.F(var6, 0.7));
                           var8.g(0.48, 0.03, new Quaternionf().rotationY((float)var5.angle).rotateX(0.35F), uh.q(var6, -1459895, 0.4));
                        }
                        break label105;
                     default:
                        float var11 = (float)Math.sqrt(0.5);
                        Quaternionf var12 = new Quaternionf().rotationY((float)var5.angle).rotateAxis((float) (Math.PI / 3), var11, 0.0F, var11);
                        Quaternionf var13 = new Quaternionf(var12)
                           .mul(new Quaternionf().setAngleAxis((float) (Math.PI / 3), var11, 0.0F, var11).rotateY((float)var5.angle * 1.4F));
                        ku var14 = new ku(var1, var5.center, var13, var3.scale, var2);
                        var14.i(0.0, 0.0, 0.0, 0.126, 0.126, 0.126, var7);
                        if (var2) {
                           break label105;
                        }

                        var14.i(0.0, 0.0, 0.0, 0.225, 0.225, 0.225, uh.F(var6, 0.55));
                        var15 = new ku(var1, var5.center, var12, var3.scale, false);
                        var15.i(0.0, 0.0, 0.0, 0.3, 0.3, 0.3, uh.F(var6, 0.5));
                        var16 = uh.q(var6, -1459895, 0.55);
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
                           var15.i(var20[0], var20[1], var20[2], var21[0], var21[1], var21[2], var16);
                        }
                     }

                     var17++;
                  }
               }

               if (var3.rings) {
                  for (int var23 = 0; var23 < 2; var23++) {
                     var8.g(
                        0.62 + var23 * 0.1,
                        0.008,
                        new Quaternionf().rotationY((float)(var5.angle * (var23 == 0 ? 1.6 : -1.2) + var23)).rotateX(0.5F + var23 * 0.6F),
                        uh.F(var7, 0.9)
                     );
                  }
               }
            }
         }
      }
   }

   private record n(String model, double scale, boolean rings, boolean glow, List<CrystalChamsModule.x> crystals) {
   }

   private record x(int id, Vec3 center, Vec3 foot, float age, double angle, int tint, boolean base) {
   }
}

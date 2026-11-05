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
   private static CrystalChamsModule S;
   private final com.larpclient.module.lp R = this.addSetting(
      new com.larpclient.module.lp("Model", "Crystal appearance", "Vanilla", "Vanilla", "Gem", "Orb", "Prism")
   );
   private final com.larpclient.module.lp Z = this.addSetting(
      new com.larpclient.module.lp("Color mode", "Crystal palette", "Theme", "Theme", "Custom", "Rainbow", "Original")
   );
   private final com.larpclient.module.lv o = this.addSetting(new com.larpclient.module.lv("Color", "Custom crystal tint", -1459895));
   private final com.larpclient.module.lc h = this.addSetting(new com.larpclient.module.lc("Opacity", "Crystal opacity in percent", 100.0, 10.0, 100.0, 5.0));
   private final com.larpclient.module.ln Q = this.addSetting(new com.larpclient.module.ln("Glow", "Full brightness and luminous core", true));
   private final com.larpclient.module.ln X = this.addSetting(new com.larpclient.module.ln("Rings", "Thin orbiting rings", true));
   private final com.larpclient.module.lc q = this.addSetting(new com.larpclient.module.lc("Size", "Visual scale in percent", 100.0, 20.0, 300.0, 5.0));
   private final com.larpclient.module.lc O = this.addSetting(new com.larpclient.module.lc("Spin rate", "Rotation speed in percent", 100.0, 0.0, 600.0, 10.0));
   private final com.larpclient.module.lc J = this.addSetting(new com.larpclient.module.lc("Bounce", "Bob height in percent", 100.0, 0.0, 300.0, 10.0));
   private final com.larpclient.module.lp m = this.addSetting(
      new com.larpclient.module.lp("Base", "Base plate visibility", "Default", "Default", "Always", "Never")
   );
   private CrystalChamsModule.n b;

   public CrystalChamsModule() {
      super("Crystal Chams", "Crystal colors, motion and Gem, Orb or Prism models", Category.RENDER, -1);
      S = this;
      this.o.x(() -> this.Z.i("Custom"));
      this.X.x(() -> !this.R.i("Vanilla"));
   }

   public static CrystalChamsModule l() {
      return S;
   }

   public List<com.larpclient.module.l6<?>> W() {
      return List.of(this.R, this.q, this.X, this.m);
   }

   public List<com.larpclient.module.l6<?>> p() {
      return List.of(this.Z, this.o, this.h, this.Q);
   }

   public List<com.larpclient.module.l6<?>> b() {
      return List.of(this.O, this.J);
   }

   public boolean s() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && !this.R.i("Vanilla");
   }

   public float N() {
      return this.isRuntimeActive() ? this.q.getValue().floatValue() / 100.0F : 1.0F;
   }

   public float H() {
      return this.isRuntimeActive() ? this.O.getValue().floatValue() / 100.0F : 1.0F;
   }

   public float G() {
      return this.isRuntimeActive() ? this.J.getValue().floatValue() / 100.0F : 1.0F;
   }

   public boolean D() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.Q.getValue();
   }

   public boolean g() {
      return this.isEnabled() ? this.h.getValue() < 100.0 : false;
   }

   public boolean o() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && (!this.Z.i("Original") || this.g() || this.Q.getValue());
   }

   public Boolean D() {
      return this.isRuntimeActive() && !this.m.i("Default") ? this.m.i("Always") : null;
   }

   public int Z(int var1) {
      double var2 = ea.y();

      int var4 = switch ((String)this.Z.getValue()) {
         case "Custom" -> this.o.getValue();
         case "Original" -> -1;
         case "Rainbow" -> oh.O((float)((var2 * 0.12 + var1 * 0.13) % 1.0), 0.7F, 1.0F);
         default -> oh.j(rv.e, rv.GQ, 0.5 + 0.5 * Math.sin(var2 + var1 * 0.1));
      };
      return oh.i(var4, this.h.getValue() / 100.0);
   }

   @Override
   public void onEnable() {
      r4.g("CrystalChams", "Native crystal appearance activated: {}", this.R.getValue());
   }

   @Override
   public void onDisable() {
      this.h();
   }

   public void h() {
      this.b = null;
   }

   public boolean r(Object var1) {
      if (this.isEnabled()) {
         CrystalChamsModule.n var2 = this.b;
         return f8.c() && this.s() && var2 != null && var1 instanceof yn var3
            ? var2.crystals.stream().anyMatch(var1x -> var1x.id == var3.larpCrystalId())
            : false;
      } else {
         return false;
      }
   }

   public void q(float var1) {
      if (this.isEnabled()) {
         this.b = null;
         if (this.s() && this.isInGame()) {
            ArrayList var2 = new ArrayList();

            for (Entity var4 : getMc().level.entitiesForRendering()) {
               if (var4 instanceof EndCrystal var5 && !(var4.distanceToSqr(getMc().player) > 9216.0)) {
                  if (var2.size() >= 128) {
                     break;
                  }

                  float var6 = var5.time + var1;
                  double var7 = Math.sin(var6 * 0.2) / 2.0 + 0.5;
                  double var9 = (var7 * var7 + var7) * 0.4 * this.G();
                  Vec3 var11 = var5.getPosition(var1);
                  Vec3 var12 = var11.add(0.0, 1.2 + var9, 0.0);
                  int var13 = this.Z.i("Original") ? oh.i(-3707905, this.h.getValue() / 100.0) : this.Z((int)(var11.x * 7.0 + var11.z * 13.0));
                  var2.add(
                     new CrystalChamsModule.i(
                        var5.getId(), var12, var11, var6, Math.toRadians(var6 * 3.0F * this.H()), var13, this.D() == null ? var5.showsBottom() : this.D()
                     )
                  );
               }
            }

            this.b = new CrystalChamsModule.n(this.R.getValue(), this.N(), this.X.getValue(), this.Q.getValue(), List.copyOf(var2));
         }
      }
   }

   public boolean t() {
      return !this.isEnabled() ? false : this.b != null && this.b.glow && !this.b.crystals.isEmpty();
   }

   public void N(r6 var1, boolean var2) {
      if (this.isEnabled()) {
         CrystalChamsModule.n var3 = this.b;
         if (var3 != null && (!var2 || var3.glow)) {
            for (CrystalChamsModule.i var5 : var3.crystals) {
               int var7;
               _3 var8;
               label105: {
                  int var6 = var5.tint;
                  var7 = oh.j(var6, oh.i(-1, (var6 >>> 24) / 255.0), 0.35);
                  var8 = new _3(var1, var5.center, new Quaternionf(), var3.scale, var2);
                  if (!var2 && var5.base) {
                     _3 var9 = new _3(var1, var5.foot, new Quaternionf(), 1.0, false);
                     var9.m(0.0, 0.125, 0.0, 0.375, 0.125, 0.375, -14278098);
                     var9.D(0.27, 0.012, new Quaternionf(), var6);
                  }

                  _3 var15;
                  int var16;
                  int var17;
                  switch (var3.model) {
                     case "Gem":
                        var8.I(0.13, 0.24, 0.07, -var5.angle * 1.7, var7);
                        if (!var2) {
                           var8.I(0.36, 0.6, 0.22, var5.angle, oh.S(var6, 0.85));
                        }
                        break label105;
                     case "Orb":
                        double var24 = 0.85 + 0.15 * Math.sin(var5.age * 0.25);
                        var8.K(0.0, 0.0, 0.0, 0.15 * var24, 0.15 * var24, 0.15 * var24, var7);
                        if (!var2) {
                           var8.K(0.0, 0.0, 0.0, 0.42, 0.42, 0.42, oh.S(var6, 0.7));
                           var8.D(0.48, 0.03, new Quaternionf().rotationY((float)var5.angle).rotateX(0.35F), oh.j(var6, -1459895, 0.4));
                        }
                        break label105;
                     default:
                        float var11 = (float)Math.sqrt(0.5);
                        Quaternionf var12 = new Quaternionf().rotationY((float)var5.angle).rotateAxis((float) (Math.PI / 3), var11, 0.0F, var11);
                        Quaternionf var13 = new Quaternionf(var12)
                           .mul(new Quaternionf().setAngleAxis((float) (Math.PI / 3), var11, 0.0F, var11).rotateY((float)var5.angle * 1.4F));
                        _3 var14 = new _3(var1, var5.center, var13, var3.scale, var2);
                        var14.m(0.0, 0.0, 0.0, 0.126, 0.126, 0.126, var7);
                        if (var2) {
                           break label105;
                        }

                        var14.m(0.0, 0.0, 0.0, 0.225, 0.225, 0.225, oh.S(var6, 0.55));
                        var15 = new _3(var1, var5.center, var12, var3.scale, false);
                        var15.m(0.0, 0.0, 0.0, 0.3, 0.3, 0.3, oh.S(var6, 0.5));
                        var16 = oh.j(var6, -1459895, 0.55);
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
                           var15.m(var20[0], var20[1], var20[2], var21[0], var21[1], var21[2], var16);
                        }
                     }

                     var17++;
                  }
               }

               if (var3.rings) {
                  for (int var23 = 0; var23 < 2; var23++) {
                     var8.D(
                        0.62 + var23 * 0.1,
                        0.008,
                        new Quaternionf().rotationY((float)(var5.angle * (var23 == 0 ? 1.6 : -1.2) + var23)).rotateX(0.5F + var23 * 0.6F),
                        oh.S(var7, 0.9)
                     );
                  }
               }
            }
         }
      }
   }

   private record i(int id, Vec3 center, Vec3 foot, float age, double angle, int tint, boolean base) {
   }

   private record n(String model, double scale, boolean rings, boolean glow, List<CrystalChamsModule.i> crystals) {
   }
}

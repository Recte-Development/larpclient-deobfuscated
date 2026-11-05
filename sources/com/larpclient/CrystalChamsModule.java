package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import com.larpclient.module.df;
import com.larpclient.module.dh;
import com.larpclient.module.dk;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public final class CrystalChamsModule extends Module {
   private static CrystalChamsModule X;
   private final com.larpclient.module.d6 a = this.addSetting(
      new com.larpclient.module.d6("Model", "Crystal appearance", "Vanilla", "Vanilla", "Gem", "Orb", "Prism")
   );
   private final com.larpclient.module.d6 M = this.addSetting(
      new com.larpclient.module.d6("Color mode", "Crystal palette", "Theme", "Theme", "Custom", "Rainbow", "Original")
   );
   private final dk p = this.addSetting(new dk("Color", "Custom crystal tint", -1459895));
   private final com.larpclient.module.d9 G = this.addSetting(new com.larpclient.module.d9("Opacity", "Crystal opacity in percent", 100.0, 10.0, 100.0, 5.0));
   private final dh C = this.addSetting(new dh("Glow", "Full brightness and luminous core", true));
   private final dh Z = this.addSetting(new dh("Rings", "Thin orbiting rings", true));
   private final com.larpclient.module.d9 E = this.addSetting(new com.larpclient.module.d9("Size", "Visual scale in percent", 100.0, 20.0, 300.0, 5.0));
   private final com.larpclient.module.d9 g = this.addSetting(new com.larpclient.module.d9("Spin rate", "Rotation speed in percent", 100.0, 0.0, 600.0, 10.0));
   private final com.larpclient.module.d9 A = this.addSetting(new com.larpclient.module.d9("Bounce", "Bob height in percent", 100.0, 0.0, 300.0, 10.0));
   private final com.larpclient.module.d6 d = this.addSetting(
      new com.larpclient.module.d6("Base", "Base plate visibility", "Default", "Default", "Always", "Never")
   );
   private CrystalChamsModule.h S;

   public CrystalChamsModule() {
      super("Crystal Chams", "Crystal colors, motion and Gem, Orb or Prism models", Category.RENDER, -1);
      X = this;
      this.p.H(() -> this.M.S("Custom"));
      this.Z.H(() -> !this.a.S("Vanilla"));
   }

   public static CrystalChamsModule O() {
      return X;
   }

   public List<df<?>> f() {
      return List.of(this.a, this.E, this.Z, this.d);
   }

   public List<df<?>> E() {
      return List.of(this.M, this.p, this.G, this.C);
   }

   public List<df<?>> k() {
      return List.of(this.g, this.A);
   }

   public boolean f() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && !this.a.S("Vanilla");
   }

   public float l() {
      return this.isRuntimeActive() ? this.E.P().floatValue() / 100.0F : 1.0F;
   }

   public float B() {
      return this.isRuntimeActive() ? this.g.P().floatValue() / 100.0F : 1.0F;
   }

   public float s() {
      return this.isRuntimeActive() ? this.A.P().floatValue() / 100.0F : 1.0F;
   }

   public boolean F() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.C.P();
   }

   public boolean x() {
      return this.isEnabled() ? this.G.P() < 100.0 : false;
   }

   public boolean K() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && (!this.M.S("Original") || this.x() || this.C.P());
   }

   public Boolean l() {
      return this.isRuntimeActive() && !this.d.S("Default") ? this.d.S("Always") : null;
   }

   public int l(int var1) {
      double var2 = ty.K();

      int var4 = switch ((String)this.M.P()) {
         case "Custom" -> this.p.P();
         case "Original" -> -1;
         case "Rainbow" -> _3.Z((float)((var2 * 0.12 + var1 * 0.13) % 1.0), 0.7F, 1.0F);
         default -> _3.D(com.larpclient.f.C, com.larpclient.f.O, 0.5 + 0.5 * Math.sin(var2 + var1 * 0.1));
      };
      return _3.A(var4, this.G.P() / 100.0);
   }

   @Override
   public void onEnable() {
      mt.U("CrystalChams", "Native crystal appearance activated: {}", this.a.P());
   }

   @Override
   public void onDisable() {
      this.k();
   }

   public void k() {
      this.S = null;
   }

   public boolean y(Object var1) {
      if (this.isEnabled()) {
         CrystalChamsModule.h var2 = this.S;
         return com.larpclient.h.V() && this.f() && var2 != null && var1 instanceof ev var3
            ? var2.crystals.stream().anyMatch(var1x -> var1x.id == var3.larpCrystalId())
            : false;
      } else {
         return false;
      }
   }

   public void k(float var1) {
      if (this.isEnabled()) {
         this.S = null;
         if (this.f() && this.isInGame()) {
            ArrayList var2 = new ArrayList();

            for (Entity var4 : getMc().level.entitiesForRendering()) {
               if (var4 instanceof EndCrystal var5 && !(var4.distanceToSqr(getMc().player) > 9216.0)) {
                  if (var2.size() >= 128) {
                     break;
                  }

                  float var6 = var5.time + var1;
                  double var7 = Math.sin(var6 * 0.2) / 2.0 + 0.5;
                  double var9 = (var7 * var7 + var7) * 0.4 * this.s();
                  Vec3 var11 = var5.getPosition(var1);
                  Vec3 var12 = var11.add(0.0, 1.2 + var9, 0.0);
                  int var13 = this.M.S("Original") ? _3.A(-3707905, this.G.P() / 100.0) : this.l((int)(var11.x * 7.0 + var11.z * 13.0));
                  var2.add(
                     new CrystalChamsModule.x(
                        var5.getId(), var12, var11, var6, Math.toRadians(var6 * 3.0F * this.B()), var13, this.l() == null ? var5.showsBottom() : this.l()
                     )
                  );
               }
            }

            this.S = new CrystalChamsModule.h(this.a.P(), this.l(), this.Z.P(), this.C.P(), List.copyOf(var2));
         }
      }
   }

   public boolean W() {
      return !this.isEnabled() ? false : this.S != null && this.S.glow && !this.S.crystals.isEmpty();
   }

   public void G(ki var1, boolean var2) {
      if (this.isEnabled()) {
         CrystalChamsModule.h var3 = this.S;
         if (var3 != null && (!var2 || var3.glow)) {
            for (CrystalChamsModule.x var5 : var3.crystals) {
               int var7;
               e2 var8;
               label105: {
                  int var6 = var5.tint;
                  var7 = _3.D(var6, _3.A(-1, (var6 >>> 24) / 255.0), 0.35);
                  var8 = new e2(var1, var5.center, new Quaternionf(), var3.scale, var2);
                  if (!var2 && var5.base) {
                     e2 var9 = new e2(var1, var5.foot, new Quaternionf(), 1.0, false);
                     var9.D(0.0, 0.125, 0.0, 0.375, 0.125, 0.375, -14278098);
                     var9.v(0.27, 0.012, new Quaternionf(), var6);
                  }

                  e2 var15;
                  int var16;
                  int var17;
                  switch (var3.model) {
                     case "Gem":
                        var8.d(0.13, 0.24, 0.07, -var5.angle * 1.7, var7);
                        if (!var2) {
                           var8.d(0.36, 0.6, 0.22, var5.angle, _3.U(var6, 0.85));
                        }
                        break label105;
                     case "Orb":
                        double var24 = 0.85 + 0.15 * Math.sin(var5.age * 0.25);
                        var8.v(0.0, 0.0, 0.0, 0.15 * var24, 0.15 * var24, 0.15 * var24, var7);
                        if (!var2) {
                           var8.v(0.0, 0.0, 0.0, 0.42, 0.42, 0.42, _3.U(var6, 0.7));
                           var8.v(0.48, 0.03, new Quaternionf().rotationY((float)var5.angle).rotateX(0.35F), _3.D(var6, -1459895, 0.4));
                        }
                        break label105;
                     default:
                        float var11 = (float)Math.sqrt(0.5);
                        Quaternionf var12 = new Quaternionf().rotationY((float)var5.angle).rotateAxis((float) (Math.PI / 3), var11, 0.0F, var11);
                        Quaternionf var13 = new Quaternionf(var12)
                           .mul(new Quaternionf().setAngleAxis((float) (Math.PI / 3), var11, 0.0F, var11).rotateY((float)var5.angle * 1.4F));
                        e2 var14 = new e2(var1, var5.center, var13, var3.scale, var2);
                        var14.D(0.0, 0.0, 0.0, 0.126, 0.126, 0.126, var7);
                        if (var2) {
                           break label105;
                        }

                        var14.D(0.0, 0.0, 0.0, 0.225, 0.225, 0.225, _3.U(var6, 0.55));
                        var15 = new e2(var1, var5.center, var12, var3.scale, false);
                        var15.D(0.0, 0.0, 0.0, 0.3, 0.3, 0.3, _3.U(var6, 0.5));
                        var16 = _3.D(var6, -1459895, 0.55);
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
                           var15.D(var20[0], var20[1], var20[2], var21[0], var21[1], var21[2], var16);
                        }
                     }

                     var17++;
                  }
               }

               if (var3.rings) {
                  for (int var23 = 0; var23 < 2; var23++) {
                     var8.v(
                        0.62 + var23 * 0.1,
                        0.008,
                        new Quaternionf().rotationY((float)(var5.angle * (var23 == 0 ? 1.6 : -1.2) + var23)).rotateX(0.5F + var23 * 0.6F),
                        _3.U(var7, 0.9)
                     );
                  }
               }
            }
         }
      }
   }

   private record h(String model, double scale, boolean rings, boolean glow, List<CrystalChamsModule.x> crystals) {
   }

   private record x(int id, Vec3 center, Vec3 foot, float age, double angle, int tint, boolean base) {
   }
}

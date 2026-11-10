package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_243;
import org.joml.Quaternionf;

public final class CrystalChamsModule extends Module {
   private static CrystalChamsModule J;
   private final com.larpclient.module.li u = this.addSetting(
      new com.larpclient.module.li("Model", "Crystal appearance", "Vanilla", "Vanilla", "Gem", "Orb", "Prism")
   );
   private final com.larpclient.module.li V = this.addSetting(
      new com.larpclient.module.li("Color mode", "Crystal palette", "Theme", "Theme", "Custom", "Rainbow", "Original")
   );
   private final com.larpclient.module.l0 W = this.addSetting(new com.larpclient.module.l0("Color", "Custom crystal tint", -1459895));
   private final com.larpclient.module.lo C = this.addSetting(new com.larpclient.module.lo("Opacity", "Crystal opacity in percent", 100.0, 10.0, 100.0, 5.0));
   private final com.larpclient.module.l3 y = this.addSetting(new com.larpclient.module.l3("Glow", "Full brightness and luminous core", true));
   private final com.larpclient.module.l3 F = this.addSetting(new com.larpclient.module.l3("Rings", "Thin orbiting rings", true));
   private final com.larpclient.module.lo P = this.addSetting(new com.larpclient.module.lo("Size", "Visual scale in percent", 100.0, 20.0, 300.0, 5.0));
   private final com.larpclient.module.lo I = this.addSetting(new com.larpclient.module.lo("Spin rate", "Rotation speed in percent", 100.0, 0.0, 600.0, 10.0));
   private final com.larpclient.module.lo Q = this.addSetting(new com.larpclient.module.lo("Bounce", "Bob height in percent", 100.0, 0.0, 300.0, 10.0));
   private final com.larpclient.module.li n = this.addSetting(
      new com.larpclient.module.li("Base", "Base plate visibility", "Default", "Default", "Always", "Never")
   );
   private CrystalChamsModule.f c;

   public CrystalChamsModule() {
      super("Crystal Chams", "Crystal colors, motion and Gem, Orb or Prism models", Category.RENDER, -1);
      J = this;
      this.W.O(() -> this.V.G("Custom"));
      this.F.O(() -> !this.u.G("Vanilla"));
   }

   public static CrystalChamsModule X() {
      return J;
   }

   public List<com.larpclient.module.lb<?>> e() {
      return List.of(this.u, this.P, this.F, this.n);
   }

   public List<com.larpclient.module.lb<?>> L() {
      return List.of(this.V, this.W, this.C, this.y);
   }

   public List<com.larpclient.module.lb<?>> v() {
      return List.of(this.I, this.Q);
   }

   public boolean O() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && !this.u.G("Vanilla");
   }

   public float s() {
      return this.isRuntimeActive() ? this.P.getValue().floatValue() / 100.0F : 1.0F;
   }

   public float i() {
      return this.isRuntimeActive() ? this.I.getValue().floatValue() / 100.0F : 1.0F;
   }

   public float d() {
      return this.isRuntimeActive() ? this.Q.getValue().floatValue() / 100.0F : 1.0F;
   }

   public boolean P() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.y.getValue();
   }

   public boolean K() {
      return this.isEnabled() ? this.C.getValue() < 100.0 : false;
   }

   public boolean p() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && (!this.V.G("Original") || this.K() || this.y.getValue());
   }

   public Boolean a() {
      return this.isRuntimeActive() && !this.n.G("Default") ? this.n.G("Always") : null;
   }

   public int o(int var1) {
      double var2 = qo.x();

      int var4 = switch ((String)this.V.getValue()) {
         case "Custom" -> this.W.getValue();
         case "Original" -> -1;
         case "Rainbow" -> ei.X((float)((var2 * 0.12 + var1 * 0.13) % 1.0), 0.7F, 1.0F);
         default -> ei.W(ez.a, ez.f, 0.5 + 0.5 * Math.sin(var2 + var1 * 0.1));
      };
      return ei.D(var4, this.C.getValue() / 100.0);
   }

   @Override
   public void onEnable() {
      q8.v("CrystalChams", "Native crystal appearance activated: {}", this.u.getValue());
   }

   @Override
   public void onDisable() {
      this.g();
   }

   public void g() {
      this.c = null;
   }

   public boolean b(Object var1) {
      if (this.isEnabled()) {
         CrystalChamsModule.f var2 = this.c;
         return wc.l() && this.O() && var2 != null && var1 instanceof bb var3
            ? var2.crystals.stream().anyMatch(var1x -> var1x.id == var3.larpCrystalId())
            : false;
      } else {
         return false;
      }
   }

   public void E(float var1) {
      if (this.isEnabled()) {
         this.c = null;
         if (this.O() && this.isInGame()) {
            ArrayList var2 = new ArrayList();

            for (class_1297 var4 : getMc().field_1687.method_18112()) {
               if (var4 instanceof class_1511 var5 && !(var4.method_5858(getMc().field_1724) > 9216.0)) {
                  if (var2.size() >= 128) {
                     break;
                  }

                  float var6 = var5.field_7034 + var1;
                  double var7 = Math.sin(var6 * 0.2) / 2.0 + 0.5;
                  double var9 = (var7 * var7 + var7) * 0.4 * this.d();
                  class_243 var11 = var5.method_30950(var1);
                  class_243 var12 = var11.method_1031(0.0, 1.2 + var9, 0.0);
                  int var13 = this.V.G("Original")
                     ? ei.D(-3707905, this.C.getValue() / 100.0)
                     : this.o((int)(var11.field_1352 * 7.0 + var11.field_1350 * 13.0));
                  var2.add(
                     new CrystalChamsModule.d(
                        var5.method_5628(), var12, var11, var6, Math.toRadians(var6 * 3.0F * this.i()), var13, this.a() == null ? var5.method_6836() : this.a()
                     )
                  );
               }
            }

            this.c = new CrystalChamsModule.f(this.u.getValue(), this.s(), this.F.getValue(), this.y.getValue(), List.copyOf(var2));
         }
      }
   }

   public boolean U() {
      return !this.isEnabled() ? false : this.c != null && this.c.glow && !this.c.crystals.isEmpty();
   }

   public void u(f1 var1, boolean var2) {
      if (this.isEnabled()) {
         CrystalChamsModule.f var3 = this.c;
         if (var3 != null && (!var2 || var3.glow)) {
            for (CrystalChamsModule.d var5 : var3.crystals) {
               int var7;
               st var8;
               label105: {
                  int var6 = var5.tint;
                  var7 = ei.W(var6, ei.D(-1, (var6 >>> 24) / 255.0), 0.35);
                  var8 = new st(var1, var5.center, new Quaternionf(), var3.scale, var2);
                  if (!var2 && var5.base) {
                     st var9 = new st(var1, var5.foot, new Quaternionf(), 1.0, false);
                     var9.p(0.0, 0.125, 0.0, 0.375, 0.125, 0.375, -14278098);
                     var9.h(0.27, 0.012, new Quaternionf(), var6);
                  }

                  st var15;
                  int var16;
                  int var17;
                  switch (var3.model) {
                     case "Gem":
                        var8.F(0.13, 0.24, 0.07, -var5.angle * 1.7, var7);
                        if (!var2) {
                           var8.F(0.36, 0.6, 0.22, var5.angle, ei.R(var6, 0.85));
                        }
                        break label105;
                     case "Orb":
                        double var24 = 0.85 + 0.15 * Math.sin(var5.age * 0.25);
                        var8.c(0.0, 0.0, 0.0, 0.15 * var24, 0.15 * var24, 0.15 * var24, var7);
                        if (!var2) {
                           var8.c(0.0, 0.0, 0.0, 0.42, 0.42, 0.42, ei.R(var6, 0.7));
                           var8.h(0.48, 0.03, new Quaternionf().rotationY((float)var5.angle).rotateX(0.35F), ei.W(var6, -1459895, 0.4));
                        }
                        break label105;
                     default:
                        float var11 = (float)Math.sqrt(0.5);
                        Quaternionf var12 = new Quaternionf().rotationY((float)var5.angle).rotateAxis((float) (Math.PI / 3), var11, 0.0F, var11);
                        Quaternionf var13 = new Quaternionf(var12)
                           .mul(new Quaternionf().setAngleAxis((float) (Math.PI / 3), var11, 0.0F, var11).rotateY((float)var5.angle * 1.4F));
                        st var14 = new st(var1, var5.center, var13, var3.scale, var2);
                        var14.p(0.0, 0.0, 0.0, 0.126, 0.126, 0.126, var7);
                        if (var2) {
                           break label105;
                        }

                        var14.p(0.0, 0.0, 0.0, 0.225, 0.225, 0.225, ei.R(var6, 0.55));
                        var15 = new st(var1, var5.center, var12, var3.scale, false);
                        var15.p(0.0, 0.0, 0.0, 0.3, 0.3, 0.3, ei.R(var6, 0.5));
                        var16 = ei.W(var6, -1459895, 0.55);
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
                           var15.p(var20[0], var20[1], var20[2], var21[0], var21[1], var21[2], var16);
                        }
                     }

                     var17++;
                  }
               }

               if (var3.rings) {
                  for (int var23 = 0; var23 < 2; var23++) {
                     var8.h(
                        0.62 + var23 * 0.1,
                        0.008,
                        new Quaternionf().rotationY((float)(var5.angle * (var23 == 0 ? 1.6 : -1.2) + var23)).rotateX(0.5F + var23 * 0.6F),
                        ei.R(var7, 0.9)
                     );
                  }
               }
            }
         }
      }
   }

   private record d(int id, class_243 center, class_243 foot, float age, double angle, int tint, boolean base) {
   }

   private record f(String model, double scale, boolean rings, boolean glow, List<CrystalChamsModule.d> crystals) {
   }
}

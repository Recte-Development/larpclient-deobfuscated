package com.larpclient.module;

import com.larpclient.cr;
import com.larpclient.e9;
import com.larpclient.q8;
import java.util.ArrayList;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;

public final class AutoToolModule extends Module {
   private static AutoToolModule X;
   private final l3 A = this.addSetting(new l3("Switch Back", "Restore the previous slot after mining", true));
   private final l3 H = this.addSetting(new l3("Protect Tools", "Avoid tools with only one durability remaining", true));
   private final e9.x M = new e9.x();
   private Object h;
   private Object n;
   private int N = -1;
   private class_1799 g;

   public AutoToolModule() {
      super("AutoTool", "Select the best hotbar tool while mining", Category.PLAYER, -1);
      X = this;
   }

   @Override
   public void onEnable() {
      this.M.G(-1, false);
      this.N = -1;
      this.g = null;
      this.h = getMc().field_1687;
      this.n = getMc().field_1724;
      q8.G("AutoTool", "AutoTool active: hotbar tools, harvestability, Efficiency and durability protection");
   }

   @Override
   public void onDisable() {
      this.l();
   }

   private boolean i() {
      return cr.f()
         && !mc.field_1724.method_68878()
         && !mc.field_1724.method_6115()
         && mc.field_1690.field_1886.method_1434()
         && mc.field_1765 instanceof class_3965 var1
         && var1.method_17783() == class_240.field_1332;
   }

   @Override
   public void onTick() {
      if (this.h != getMc().field_1687 || this.n != getMc().field_1724) {
         this.M.G(-1, false);
         this.N = -1;
         this.g = null;
         this.h = mc.field_1687;
         this.n = mc.field_1724;
      }

      int var10000 = this.i();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.l();
      }
   }

   private void l() {
      if (getMc().field_1687 == this.h && mc.field_1724 == this.n && mc.field_1724 != null) {
         class_1661 var1 = mc.field_1724.method_31548();
         var1.method_61496(this.M.G(var1.method_67532(), this.A.getValue() && this.N >= 0 && var1.method_5438(this.N) == this.g));
      } else {
         this.M.G(-1, false);
      }

      this.N = -1;
      this.g = null;
   }

   public static boolean M(class_2338 var0) {
      if (X != null && X.isEnabled()) {
         AutoToolModule var1 = X;
         if (var1 == null || !var1.isRuntimeActive() || cr.j() || !var1.i()) {
            return false;
         }

         if (var1.h == getMc().field_1687 && var1.n == getMc().field_1724) {
            if (!(mc.field_1765 instanceof class_3965 var2 && var2.method_17777().equals(var0) && mc.field_1687.method_22340(var0))) {
               return false;
            } else if (!mc.field_1724.method_21701(mc.field_1687, var0, mc.field_1761.method_2920()) && mc.field_1687.method_8621().method_11952(var0)) {
               class_2680 var8 = mc.field_1687.method_8320(var0);
               if (!var8.method_26215() && !(var8.method_26214(mc.field_1687, var0) <= 0.0F)) {
                  class_1661 var4 = mc.field_1724.method_31548();
                  ArrayList var5 = new ArrayList(9);

                  for (int var6 = 0; var6 < 9; var6++) {
                     var5.add(e9.s(var4.method_5438(var6), var8, var1.H.getValue()));
                  }

                  int var9 = var4.method_67532();
                  if (var1.N == var9 && var4.method_5438(var9) != var1.g) {
                     var1.M.D();
                  }

                  int var7 = var1.M.l(var9, e9.J(var5, var9));
                  if (var7 != var9) {
                     var4.method_61496(var7);
                     var1.N = var7;
                     var1.g = var4.method_5438(var7);
                  }

                  return var7 != var9;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}

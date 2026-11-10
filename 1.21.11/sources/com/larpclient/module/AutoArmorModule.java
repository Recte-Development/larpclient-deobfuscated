package com.larpclient.module;

import com.larpclient.w;
import java.util.List;
import java.util.Set;
import net.minecraft.class_10192;
import net.minecraft.class_1304;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_5134;
import net.minecraft.class_9334;
import net.minecraft.class_1322.class_1323;

public class AutoArmorModule extends Module {
   private int t = 0;
   private final l3 l = new l3("Diamond", "Equip diamond armor upgrades", true);
   private final l3 Y = new l3("Netherite", "Equip netherite armor upgrades", true);
   private final l3 A = new l3("Gold", "Equip gold armor upgrades", true);
   private final l3 B = new l3("Iron", "Equip iron armor upgrades", true);
   private static final Set<class_1792> z = Set.of(class_1802.field_8805, class_1802.field_8058, class_1802.field_8348, class_1802.field_8285);
   private static final Set<class_1792> x = Set.of(class_1802.field_22027, class_1802.field_22028, class_1802.field_22029, class_1802.field_22030);
   private static final Set<class_1792> J = Set.of(class_1802.field_8862, class_1802.field_8678, class_1802.field_8416, class_1802.field_8753);
   private static final Set<class_1792> O = Set.of(class_1802.field_8743, class_1802.field_8523, class_1802.field_8396, class_1802.field_8660);
   private static final Set<class_1792> i = Set.of(
      class_1802.field_8283,
      class_1802.field_8873,
      class_1802.field_8218,
      class_1802.field_8313,
      class_1802.field_61343,
      class_1802.field_61344,
      class_1802.field_61345,
      class_1802.field_61346,
      class_1802.field_8090
   );

   public AutoArmorModule() {
      super("AutoArmor", "Automatically equip better allowed armor", Category.PLAYER, -1);
      this.addSetting(this.l);
      this.addSetting(this.Y);
      this.addSetting(this.A);
      this.addSetting(this.B);
   }

   public List<lb<?>> b() {
      return List.of(this.l, this.Y, this.A, this.B);
   }

   public boolean b(class_1799 var1, class_1304 var2) {
      if (!r(var1, var2)) {
         return false;
      } else {
         class_1792 var3 = var1.method_7909();
         if (z.contains(var3)) {
            return this.l.getValue();
         } else if (x.contains(var3)) {
            return this.Y.getValue();
         } else if (J.contains(var3)) {
            return this.A.getValue();
         } else {
            return O.contains(var3) ? this.B.getValue() : i.contains(var3);
         }
      }
   }

   @Override
   public void onDisable() {
      this.t = 0;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (mc.field_1755 == null) {
            boolean var14 = false;
            int var13 = this.t;
            if ((-Integer.signum(var13) >>> 31 ^ 1) == 0) {
               byte var16 = 1;
               int var15 = this.t;
               this.t = var15 - var16;
            } else {
               for (class_1304 var4 : new class_1304[]{class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166}) {
                  if (var4 != class_1304.field_6174 || !w.u()) {
                     int var5 = this.w(var4);
                     class_1799 var6 = mc.field_1724.method_6118(var4);
                     int var7 = X(var6, var4);
                     int var8 = -1;
                     int var9 = var7;

                     for (int var10 = 0; var10 < 36; var10++) {
                        class_1799 var11 = mc.field_1724.method_31548().method_5438(var10);
                        if (this.b(var11, var4)) {
                           int var12 = X(var11, var4);
                           if (var12 > var9) {
                              var9 = var12;
                              var8 = var10;
                           }
                        }
                     }

                     if (var8 != -1) {
                        int var17 = mc.field_1724.field_7512.field_7763;
                        int var18 = var8 < 9 ? var8 + 36 : var8;
                        mc.field_1761.method_2906(var17, var18, 0, class_1713.field_7790, mc.field_1724);
                        mc.field_1761.method_2906(var17, var5, 0, class_1713.field_7790, mc.field_1724);
                        if (!var6.method_7960()) {
                           mc.field_1761.method_2906(var17, var18, 0, class_1713.field_7790, mc.field_1724);
                        }

                        this.t = 3;
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private static boolean r(class_1799 var0, class_1304 var1) {
      if (var0.method_7960()) {
         return false;
      }

      class_1792 var2 = var0.method_7909();
      if (!z.contains(var2) && !x.contains(var2) && !J.contains(var2) && !O.contains(var2) && !i.contains(var2)) {
         return false;
      }

      class_10192 var3 = (class_10192)var0.method_58694(class_9334.field_54196);
      return var3 != null && var3.comp_3174() == var1;
   }

   private static int X(class_1799 var0, class_1304 var1) {
      if (!var0.method_7960() && r(var0, var1)) {
         double[] var2 = new double[]{0.0};
         var0.method_57354(var1, (var1x, var2x) -> {
            if (var1x.method_55838(class_5134.field_23724) && var2x.comp_2450() == class_1323.field_6328) {
               var2[0] += var2x.comp_2449();
            }
         });
         return (int)Math.round(var2[0]);
      } else {
         return -1;
      }
   }

   private int w(class_1304 var1) {
      return switch (var1) {
         case field_6169 -> 5;
         case field_6174 -> 6;
         case field_6172 -> 7;
         case field_6166 -> 8;
         default -> -1;
      };
   }
}

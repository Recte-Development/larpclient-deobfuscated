package com.larpclient;

import net.minecraft.class_1268;
import net.minecraft.class_1747;
import net.minecraft.class_1750;
import net.minecraft.class_1792;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_3726;
import net.minecraft.class_3965;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_239.class_240;

public final class sw extends s4 {
   private final com.larpclient.module.l3 N = this.G("Legit", "Repeat normal crosshair placement at normal use speed", false);
   private final com.larpclient.module.lo M = this.Z("Range", "Distance along your view to search for a supported position", 4.5, 1.0, 6.0, 0.5);
   private final com.larpclient.module.lo q = this.Z("Delay", "Ticks between placement attempts", 4.0, 0.0, 20.0, 1.0);
   private int c;

   public sw() {
      super("Air Place", "Place held blocks along your crosshair using nearby support faces");
   }

   @Override
   public void onTick() {
      if (this.r() && mc.field_1690.field_1904.method_1434()) {
         int var11 = this.c;
         int var10 = mc.field_1724.field_6012;
         if (Integer.compare(var10, var11) >>> 31 == 0) {
            int var10000 = mc.field_1724.method_6047().method_7909() instanceof class_1747;
            boolean var13 = false;
            byte var12 = (byte)var10000;
            var10000 = Integer.signum(var12);
            if ((var10000 | -var10000) >>> 31 != 0) {
               int var29 = this.N.getValue();
               boolean var15 = false;
               byte var14 = (byte)var29;
               var29 = Integer.signum(var14);
               if (((var29 | -var29) >>> 31 ^ 1) == 0) {
                  if (mc.field_1765 instanceof class_3965 var18 && var18.method_17783() == class_240.field_1332) {
                     class_2338 var22 = var18.method_17777().method_10093(var18.method_17780());
                     fk.a(
                        this,
                        var22,
                        mc.field_1724.method_6047().method_7909(),
                        this.M.getValue(),
                        false,
                        false,
                        () -> this.r() && mc.field_1690.field_1904.method_1434(),
                        var1x -> {
                           this.c = mc.field_1724.field_6012 + Math.max(4, this.q.getValue().intValue());
                           this.m(var1x ? "Placed" : "Placement rejected");
                        }
                     );
                  }

                  return;
               }

               class_2338 var1 = null;
               class_239 var3 = mc.field_1765;
               int var31 = var3 instanceof class_3965;
               boolean var17 = false;
               byte var16 = (byte)var31;
               var31 = Integer.signum(var16);
               if (((var31 | -var31) >>> 31 ^ 1) == 0) {
                  class_3965 var2 = (class_3965)var3;
                  if (var2.method_17783() == class_240.field_1332) {
                     class_2338 var23 = var2.method_17777().method_10093(var2.method_17780());
                     if (fk.v(var23)) {
                        var1 = var23;
                     }
                  }
               }

               if (var1 == null) {
                  class_243 var19 = mc.field_1724.method_33571();
                  class_243 var24 = mc.field_1724.method_5720();
                  class_2338 var4 = null;
                  double var5 = Math.min(this.M.getValue(), mc.field_1724.method_55754());

                  for (double var7 = 0.5; var7 <= var5; var7 += 0.25) {
                     class_2338 var9 = class_2338.method_49638(var19.method_1019(var24.method_1021(var7)));
                     if (!var9.equals(var4)) {
                        var4 = var9;
                        if (this.r(var9) != null) {
                           var1 = var9;
                           break;
                        }
                     }
                  }
               }

               if (var1 == null) {
                  this.m("No supported position along view");
                  return;
               }

               class_2338 var20 = var1.method_10062();
               class_1792 var25 = mc.field_1724.method_6047().method_7909();
               class_638 var26 = mc.field_1687;
               class_746 var27 = mc.field_1724;
               int var6 = mc.field_1724.field_6012;
               cr.y(
                  this,
                  () -> {
                     class_3965 var2x = this.r(var20);
                     if (var2x != null) {
                        boolean var3x = mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, var2x).method_23665();
                        if (var3x) {
                           w6.H(mc.field_1724, class_1268.field_5808);
                        }

                        this.c = mc.field_1724.field_6012 + Math.max(1, this.q.getValue().intValue());
                        this.m(var3x ? "Placed" : "Server or placement context rejected action");
                     }
                  },
                  () -> this.r()
                     && mc.field_1690.field_1904.method_1434()
                     && mc.field_1724.method_6047().method_31574(var25)
                     && fk.f(var26, mc.field_1687, var27, mc.field_1724, var6, mc.field_1724.field_6012)
                     && var20.equals(this.U(var20))
                     && this.r(var20) != null
               );
               return;
            }
         }
      } else {
         fk.N(this);
      }
   }

   private class_2338 U(class_2338 var1) {
      if (mc.field_1765 instanceof class_3965 var2
         && var2.method_17783() == class_240.field_1332
         && var2.method_17777().method_10093(var2.method_17780()).equals(var1)) {
         return var1;
      } else {
         class_243 var5 = mc.field_1724.method_33571();

         for (double var6 = 0.5; var6 <= Math.min(this.M.getValue(), mc.field_1724.method_55754()); var6 += 0.25) {
            if (class_2338.method_49638(var5.method_1019(mc.field_1724.method_5720().method_1021(var6))).equals(var1)) {
               return var1;
            }
         }

         return null;
      }
   }

   private class_3965 r(class_2338 var1) {
      if (fk.v(var1) && mc.field_1724.method_6047().method_7909() instanceof class_1747 var2) {
         for (class_2350 var6 : class_2350.values()) {
            class_2338 var7 = var1.method_10093(var6);
            if (mc.field_1687.method_22340(var7)) {
               class_2680 var8 = mc.field_1687.method_8320(var7);
               if (!var8.method_45474() && !var8.method_26220(mc.field_1687, var7).method_1110()) {
                  class_2350 var9 = var6.method_10153();
                  class_243 var10 = class_243.method_24953(var7).method_1031(var9.method_10148() * 0.5, var9.method_10164() * 0.5, var9.method_10165() * 0.5);
                  double var11 = Math.min(this.M.getValue(), mc.field_1724.method_55754());
                  if (!(mc.field_1724.method_33571().method_1025(var10) > var11 * var11)) {
                     class_3965 var13 = new class_3965(var10, var9, var7, false);
                     class_1750 var14 = new class_1750(mc.field_1724, class_1268.field_5808, mc.field_1724.method_6047(), var13);
                     if (var14.method_7716() && var14.method_8037().equals(var1)) {
                        class_2680 var15 = var2.method_7711().method_9605(var14);
                        if (var15 != null
                           && var15.method_26184(mc.field_1687, var1)
                           && mc.field_1687.method_8628(var15, var1, class_3726.method_67715(mc.field_1724))) {
                           return var13;
                        }
                     }
                  }
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.c = 0;
   }

   @Override
   public void onDisable() {
      super.onDisable();
      fk.N(this);
      this.c = 0;
   }
}

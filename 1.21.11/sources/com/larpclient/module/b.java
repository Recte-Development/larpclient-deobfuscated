package com.larpclient.module;

import com.larpclient.fk;
import com.larpclient.t9;
import com.larpclient.uu;
import com.larpclient.wp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.class_1657;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2478;
import net.minecraft.class_2350.class_2353;

public final class b extends fk.y implements uu.m {
   private final uu K = new uu(this, var1 -> this.addSetting(var1));
   private static b D;
   private Object o;
   private Object A;
   private int I;
   private final l3 s = this.O("Legs", "Place webs at the opponent's feet", true);
   private final l3 b = this.O("Head", "Place webs at the opponent's head", true);
   private final l3 X = this.O("Surround", "Web the lower perimeter", false);
   private final l3 U = this.O("Upper Surround", "Web the upper perimeter", false);
   private final l3 G = this.O("Web Predict", "Predict movement unless the target is already in webs", false);
   private final lo T = this.X("Predict Ticks", "Ticks of displacement prediction", 2.0, 0.0, 10.0, 0.5);
   private final l3 t = this.O("Integrate With Trap", "Choose the footprint used by the active AutoTrap", false);
   private final l3 h = this.O("Auto Sign", "Use ordinary hotbar signs at head positions instead of head webs", false);

   @Override
   public uu placementHighlight() {
      return this.K;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.K.d();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.K.p();
      this.N();
   }

   public b() {
      super("AutoWeb", "Place cobwebs around opponents");
      D = this;
      this.T.O(this.G::getValue);
      this.m.a(var1 -> {
         fk.N(this);
         this.N();
      });
   }

   public static b e() {
      return D;
   }

   @Override
   protected void aim(float var1, float var2) {
      if (this.isEnabled()) {
         if (wp.h(wp.z.r)) {
            super.aim(var1, var2);
            if (t9.k(t9.v.j)) {
               this.o = mc.field_1724;
               this.A = mc.field_1687;
               this.I = mc.field_1724.field_6012;
               wp.S(wp.z.r);
            } else {
               this.N();
            }
         }
      }
   }

   private void N() {
      this.o = this.A = null;
      wp.k(wp.z.r);
   }

   public boolean F() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.m.G("Visible")
            && mc.field_1724 != null
            && this.o == mc.field_1724
            && this.A == mc.field_1687
            && mc.field_1724.field_6012 >= this.I
            && mc.field_1724.field_6012 - this.I <= 1
            && (!this.C.getValue() || !mc.field_1724.method_6115())
            && (!this.R.getValue() || !mc.field_1690.field_1886.method_1434());
   }

   public float S(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && wp.o() == wp.z.r && t9.k(t9.v.j)) {
            mc.field_1724.method_5847(mc.field_1724.method_36454());
            return mc.field_1724.method_36454();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private boolean c(class_1657 var1) {
      for (class_2338 var3 : g.y(var1, var1.method_73189())) {
         if (mc.field_1687.method_8320(var3).method_27852(class_2246.field_10343)) {
            return true;
         }
      }

      return false;
   }

   private class_1792 x() {
      for (int var1 = 0; var1 < 9; var1++) {
         class_1792 var2 = mc.field_1724.method_31548().method_5438(var1).method_7909();
         if (var2 instanceof class_1747 var3 && var3.method_7711() instanceof class_2478) {
            return var2;
         }
      }

      return null;
   }

   @Override
   public void onTick() {
      this.K.x(null);
      fk.N(this);
      boolean var1 = this.e();
      if (var1 || this.K.S() && this.g()) {
         class_1657 var2 = this.U();
         if (var2 == null) {
            this.W("Waiting for a target");
         } else {
            class_243 var3 = g.V(var2, this.G.getValue() && !this.c(var2) ? this.T.getValue() : 0.0);
            List var4 = g.y(var2, var3);
            g var5 = g.K();
            if (this.t.getValue() && var5 != null && var5.isRuntimeActive()) {
               LinkedHashSet var6 = new LinkedHashSet();

               for (class_2338 var8 : var5.r(var2)) {
                  if (var8.method_10264() == ((class_2338)var4.getFirst()).method_10264() - 1) {
                     var6.add(var8.method_10084());
                  }
               }

               int var10000 = var6.isEmpty();
               boolean var15 = false;
               byte var14 = (byte)var10000;
               var10000 = Integer.signum(var14);
               if ((var10000 | -var10000) >>> 31 == 0) {
                  var4 = new ArrayList(var6);
               }
            }

            int var30 = this.h.getValue();
            boolean var17 = false;
            byte var16 = (byte)var30;
            var30 = Integer.signum(var16);
            class_1792 var22 = ((var30 | -var30) >>> 31 ^ 1) == 0 ? this.x() : null;
            LinkedHashSet var23 = new LinkedHashSet();
            LinkedHashSet var24 = new LinkedHashSet();

            for (class_2338 var10 : var4) {
               if (this.s.getValue()) {
                  var23.add(var10);
               }

               if (var22 != null) {
                  var24.add(var10.method_10084());
               } else if (this.b.getValue()) {
                  var23.add(var10.method_10084());
               }

               for (class_2350 var12 : class_2353.field_11062) {
                  class_2338 var13 = var10.method_10093(var12);
                  if (!var4.contains(var13)) {
                     if (this.X.getValue()) {
                        var23.add(var13);
                     }

                     if (this.U.getValue()) {
                        var23.add(var13.method_10084());
                     }
                  }
               }
            }

            var30 = fk.T(class_1802.field_8786, this.y.getValue());
            byte var19 = -1;
            int var18 = var30;
            var30 = Integer.compare(var18, var19);
            if (((var30 | -var30) >>> 31 ^ 1) == 0) {
               for (class_2338 var27 : var23) {
                  if (var1 ? this.u(var27, class_1802.field_8786, var2) : this.K(var27, class_1802.field_8786)) {
                     this.K.x(var27);
                     if (var1) {
                        this.W("Preparing web placement");
                     }

                     return;
                  }
               }
            }

            if (var22 != null) {
               for (class_2338 var28 : var24) {
                  if (var1 ? this.u(var28, var22, var2) : this.K(var28, var22)) {
                     this.K.x(var28);
                     if (var1) {
                        this.W("Preparing sign placement");
                     }

                     return;
                  }
               }
            }

            int var10001 = fk.T(class_1802.field_8786, this.y.getValue());
            byte var21 = -1;
            int var20 = var10001;
            var10001 = Integer.compare(var20, var21);
            this.W((var10001 | -var10001) >>> 31 == 0 ? "Cobwebs missing from hotbar" : "No reachable empty web position: " + fk.c(this));
         }
      }
   }
}

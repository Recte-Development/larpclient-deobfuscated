package com.larpclient.module;

import com.larpclient.j;
import java.util.Set;
import net.minecraft.class_1297;
import net.minecraft.class_1301;
import net.minecraft.class_1309;
import net.minecraft.class_1531;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_746;

public class SpeedModule extends Module {
   private static SpeedModule i;
   private final lo G = this.addSetting(new lo("Speed", "Speed multiplier", 1.5, 1.0, 10.0, 0.1));
   private final li V = this.addSetting(new li("Mode", "Speed method", "Vanilla", "Vanilla", "BHop", "Strafe", "LowHop", "Entity Collision"));
   private final l3 o = this.addSetting(new l3("Auto Jump", "Jump automatically", true));
   private final l3 e = this.addSetting(new l3("Only On Ground", "Only boost when grounded", false));
   private final l3 g = this.addSetting(new l3("Damage Boost", "Speed after taking damage", false));
   private final lo I = this.addSetting(new lo("Strafe", "Air strafe strength", 0.8, 0.1, 2.0, 0.1));
   private final l3 O = this.addSetting(new l3("Smooth Accel", "Gradual speed increase", true));
   private final lo Q = this.addSetting(new lo("Collision Boost", "Entity Collision only: horizontal boost per nearby entity", 0.02, 0.01, 0.06, 0.01));
   private final lo y = this.addSetting(new lo("Collision Limit", "Entity Collision only: maximum entities contributing to the boost", 1.0, 1.0, 3.0, 1.0));
   private double Z;
   private String M = "";
   private boolean R;
   private int H;
   private boolean n;
   private String j = "";
   private int F;
   private int t;

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   public SpeedModule() {
      super("Speed", "Move faster", Category.MOVEMENT, -1);
      i = this;
      this.Q.O(() -> this.V.G("Entity Collision"));
      this.y.O(() -> this.V.G("Entity Collision"));
      this.G.O(() -> !this.V.G("Entity Collision"));
      this.o.O(() -> this.V.G("BHop") || this.V.G("Strafe") || this.V.G("LowHop"));
      this.e.O(() -> !this.V.G("Entity Collision"));
      this.g.O(() -> !this.V.G("Entity Collision"));
      this.I.O(() -> this.V.G("Strafe"));
      this.O.O(() -> !this.V.G("Entity Collision"));
   }

   @Override
   public void onEnable() {
      this.onDisable();
      this.j = this.V.getValue();
   }

   @Override
   public void onDisable() {
      this.B();
      this.j = "";
      this.F = 0;
      this.t = 0;
   }

   private void B() {
      this.Z = 0.0;
      this.M = "";
      this.H = 0;
      this.n = (boolean)0;
      this.R = (boolean)0;
   }

   public static void c(class_746 var0) {
      if (i != null && i.isRuntimeActive() && var0 == getMc().field_1724) {
         i.F = 20;
         i.t = 0;
      }
   }

   @Override
   public void onTick() {
      this.t = 0;
      int var1 = this.F > 0 ? 1 : 0;
      if (var1 != 0) {
         byte var19 = 1;
         int var18 = this.F;
         this.F = var18 - var19;
      }

      if (this.isInGame()) {
         if (!this.j.equals(this.V.getValue())) {
            this.B();
            this.j = this.V.getValue();
         }

         if (com.larpclient.j.k()
            && !TargetStrafeModule.Y()
            && mc.field_1755 == null
            && !mc.field_1724.method_5799()
            && !mc.field_1724.method_5771()
            && !mc.field_1724.method_6101()) {
            class_243 var2 = com.larpclient.j.i();
            boolean var3 = mc.field_1724.method_24828();
            if (this.V.G("Entity Collision")) {
               if (var1 == 0) {
                  this.x(var2);
               }
            } else if (var2.method_1027() != 0.0 && (!this.e.getValue() || var3)) {
               if (!this.M.equals(this.V.getValue())) {
                  this.Z = 0.0;
                  this.M = this.V.getValue();
               }

               int var4 = mc.field_1724.field_6235 > 0 ? 1 : 0;
               if (this.g.getValue() && var4 != 0) {
                  boolean var15 = false;
                  byte var14 = this.n;
                  int var10000 = Integer.signum(var14);
                  if ((var10000 | -var10000) >>> 31 == 0) {
                     this.H = 10;
                  }
               }

               int var25;
               label120: {
                  this.n = (boolean)var4;
                  if (this.g.getValue()) {
                     var25 = this.H;
                     byte var17 = 1;
                     int var16 = this.H;
                     this.H = var16 - var17;
                     if (var25 > 0) {
                        var25 = 1;
                        break label120;
                     }
                  }

                  var25 = 0;
               }

               this.R = (boolean)var25;
               boolean var21 = false;
               int var20 = this.H;
               if ((Integer.signum(var20) >>> 31 ^ 1) == 0) {
                  this.H = 0;
               }

               if (!this.V.G("Vanilla") || var3) {
                  double var5 = switch ((String)this.V.getValue()) {
                     case "BHop" -> 0.26;
                     case "Strafe" -> 0.28;
                     case "LowHop" -> 0.22;
                     default -> 0.13;
                  };
                  double var22 = var5 * this.G.getValue() * (this.R ? 1.15 : 1.0);
                  this.Z = this.O.getValue() ? this.Z + (var22 - this.Z) * 0.3 : var22;
                  class_243 var9 = mc.field_1724.method_18798();
                  class_243 var10 = var2.method_1021(this.Z);
                  class_243 var11 = new class_243(var9.field_1352, 0.0, var9.field_1350);
                  double var12 = (this.V.G("Strafe") && !var3 ? 0.04 * this.I.getValue() : 0.08) * D(this.G.getValue());
                  var11 = com.larpclient.j.N(var11, var10, var12);
                  mc.field_1724.method_18800(var11.field_1352, var9.field_1351, var11.field_1350);
                  if (var3 && this.o.getValue() && !this.V.G("Vanilla") && var9.field_1351 <= 0.0) {
                     if (this.V.G("LowHop")) {
                        mc.field_1724.method_18800(var11.field_1352, 0.32, var11.field_1350);
                     } else {
                        mc.field_1724.method_6043();
                     }
                  }
               }
            } else {
               this.Z = 0.0;
            }
         } else {
            this.B();
         }
      }
   }

   public static double D(double var0) {
      return Math.max(1.0, var0 - 4.0);
   }

   private void x(class_243 var1) {
      class_746 var2 = mc.field_1724;
      if (var1.method_1027() != 0.0
         && !var2.method_5765()
         && !var2.method_7325()
         && !var2.method_31549().field_7479
         && !var2.method_6128()
         && !var2.method_18276()
         && var2.field_6235 <= 0) {
         class_238 var3 = var2.method_5829().method_1014(0.19);

         for (class_1297 var5 : mc.field_1687
            .method_8333(
               var2,
               var3,
               var0 -> var0 instanceof class_1309
                  && !(var0 instanceof class_1531)
                  && var0.method_5805()
                  && !var0.method_7325()
                  && !var0.method_5765()
                  && var0.method_5810()
            )) {
            if (class_1301.method_5911(var5).test(var2) && ++this.t >= this.y.getValue().intValue()) {
               break;
            }
         }

         if (this.t != 0) {
            double var7 = Math.min(0.08, this.Q.getValue() * this.t);
            class_243 var6 = var2.method_18798();
            var2.method_18800(var6.field_1352 + var1.field_1352 * var7, var6.field_1351, var6.field_1350 + var1.field_1350 * var7);
         }
      }
   }

   @Override
   public String getStatus() {
      if (!this.V.G("Entity Collision")) {
         return "";
      } else if (!this.isRuntimeActive()) {
         return "Inactive";
      } else if (this.F > 0) {
         return "Collision boost recovering";
      } else {
         return this.t == 0 ? "Waiting for collision" : "Collision boost active";
      }
   }
}

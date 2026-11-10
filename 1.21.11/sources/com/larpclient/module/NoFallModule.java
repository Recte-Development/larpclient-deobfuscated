package com.larpclient.module;

import com.larpclient.q8;
import java.util.Set;
import java.util.UUID;
import net.minecraft.class_1132;
import net.minecraft.class_2596;
import net.minecraft.class_2848;
import net.minecraft.class_3222;
import net.minecraft.class_5134;
import net.minecraft.class_746;
import net.minecraft.class_2848.class_2849;

public class NoFallModule extends Module {
   private static final String z = "No Fall";
   private static final double I = 0.5;
   private static NoFallModule F;
   private volatile int o;
   private class_746 n;
   private double q;
   private boolean j;
   private int D;
   private double R;
   private boolean v;
   private class_2596<?> p;

   public NoFallModule() {
      super("NoFall", "Try to reduce fall damage", Category.MOVEMENT, -1);
      F = this;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   public static boolean V(boolean var0) {
      if (F != null && F.isEnabled()) {
         NoFallModule var1 = F;
         class_746 var2 = getMc().field_1724;
         return var1 != null && var2 != null ? var1.f(var2, var0) : var0;
      } else {
         return var0;
      }
   }

   private boolean f(class_746 var1, boolean var2) {
      if (var1 != this.n) {
         this.s(var1);
      }

      double var3 = var1.field_6017;
      this.R = Math.max(this.R, var3);
      this.j = (boolean)0;
      int var5 = !var2
            && !var1.method_5799()
            && !var1.method_5771()
            && !var1.method_6101()
            && !var1.method_5765()
            && !var1.method_31549().field_7479
            && var1.method_5805()
         ? 0
         : 1;
      if (var5 != 0) {
         this.V(var2 ? "landed" : "ended");
         this.q = 0.0;
         return var2;
      }

      if (var3 < this.q) {
         this.q = 0.0;
      }

      if (this.isRuntimeActive() && !this.v && !var1.method_6128()) {
         double var6 = var1.method_45325(class_5134.field_49079) - 0.5;
         if (var3 - this.q <= var6) {
            return false;
         }

         this.q = var3;
         this.j = (boolean)1;
         this.D++;
         if (this.D == 1) {
            q8.v("No Fall", "Ground report sent after {} blocks of fall", Q(var3));
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean g(class_2596<?> var0) {
      if (F != null && F.isEnabled()) {
         NoFallModule var1 = F;
         if (var1 != null
            && var1.j
            && var1.p == null
            && var0 instanceof class_2848 var2
            && var2.method_12365() == class_2849.field_12982
            && getMc().field_1724 == var1.n) {
            var1.p = var0;
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static void r(class_746 var0) {
      NoFallModule var1 = F;
      if (var1 != null && var1.p != null) {
         class_2596 var2 = var1.p;
         var1.p = null;
         if (var0 == var1.n && var0.field_3944 != null) {
            var0.field_3944.method_52787(var2);
            q8.v("No Fall", "Glide start sent after the airborne report, so the server accepts it");
         }
      }
   }

   public static void M(class_746 var0) {
      NoFallModule var1 = F;
      if (var1 != null && var0 != null && var0 == var1.n && var1.D != 0 && !var1.v) {
         var1.v = (boolean)1;
         var1.j = (boolean)0;
         q8.G(
            "No Fall",
            "Server corrected your position after {} ground report(s) this fall, likely an anticheat (Grim flags airborne ground reports and keeps the damage); real reports until you land",
            var1.D
         );
      }
   }

   private void V(String var1) {
      if (this.D > 0 || this.v) {
         q8.G("No Fall", "Fall {} after {} blocks: {} ground report(s){}", var1, Q(this.R), this.D, this.v ? ", stopped by a server correction" : "");
      }

      this.D = 0;
      this.R = 0.0;
      this.v = (boolean)0;
   }

   private void s(class_746 var1) {
      this.n = var1;
      this.q = 0.0;
      this.j = (boolean)0;
      this.D = 0;
      this.R = 0.0;
      this.v = (boolean)0;
      this.p = null;
   }

   private static double Q(double var0) {
      return Math.round(var0 * 10.0) / 10.0;
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      } else {
         return this.v ? "Paused until landing (server corrected position)" : "Fall protection active";
      }
   }

   @Override
   public void onEnable() {
      this.o++;
   }

   @Override
   public void onDisable() {
      this.o++;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         class_1132 var1 = mc.method_1576();
         if (var1 != null) {
            UUID var2 = mc.field_1724.method_5667();
            int var3 = this.o;
            var1.execute(() -> {
               class_3222 var4 = var1.method_3760().method_14602(var2);
               if (this.o == var3 && var4 != null && var4.method_5805()) {
                  var4.field_6017 = 0.0;
               }
            });
         }
      }
   }
}

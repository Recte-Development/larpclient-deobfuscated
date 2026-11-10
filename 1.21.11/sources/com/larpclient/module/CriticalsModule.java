package com.larpclient.module;

import com.larpclient.cr;
import com.larpclient.j;
import java.util.Set;
import net.minecraft.class_746;

public class CriticalsModule extends Module {
   private final li z = this.addSetting(new li("Mode", "Actual jump assistance; waits for falling eligibility", "Jump", "Jump", "MiniJump"));
   private static CriticalsModule t;

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y, l5.F);
   }

   public CriticalsModule() {
      super("Criticals", "Help time jumps for critical hits", Category.COMBAT, -1);
      t = this;
   }

   @Override
   public Set<l5> getCurrentNetworkImpacts() {
      return Set.of(this.z.G("Jump") ? l5.Y : l5.F);
   }

   public static boolean g() {
      if (t == null || !t.isEnabled()) {
         return false;
      } else if (t != null && t.isRuntimeActive() && cr.f() && j.t()) {
         class_746 var0 = getMc().field_1724;
         return !var0.method_5799()
            && !var0.method_5771()
            && !var0.method_6101()
            && !var0.method_5765()
            && !var0.method_5624()
            && !var0.method_31549().field_7479
            && !var0.method_6128()
            && !var0.method_74025()
            && !var0.method_6115();
      } else {
         return false;
      }
   }

   public static boolean w() {
      if (t == null || !t.isEnabled()) {
         return false;
      } else if (g() && !o()) {
         class_746 var0 = getMc().field_1724;
         return !var0.method_24828() && var0.method_18798().field_1351 > 0.0 && var0.method_7261(0.0F) >= 1.0F ? true : Y();
      } else {
         return false;
      }
   }

   public static boolean Y() {
      if (t == null || !t.isEnabled()) {
         return false;
      }

      if (!g()) {
         return false;
      }

      class_746 var0 = getMc().field_1724;
      if (var0.method_24828() && !(var0.method_18798().field_1351 > 0.0) && !(var0.method_7261(0.0F) < 1.0F)) {
         if (t.z.G("Jump")) {
            var0.method_6043();
         } else {
            var0.method_18800(var0.method_18798().field_1352, 0.1, var0.method_18798().field_1350);
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean o() {
      if (t == null || !t.isEnabled()) {
         return false;
      }

      if (!g()) {
         return false;
      }

      class_746 var0 = getMc().field_1724;
      return !var0.method_24828() && var0.field_6017 > 0.0 && var0.method_18798().field_1351 < 0.0 && var0.method_7261(0.0F) >= 1.0F;
   }

   @Override
   public void onDisable() {
      cr.H();
   }
}

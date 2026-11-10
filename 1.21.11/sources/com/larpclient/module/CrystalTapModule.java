package com.larpclient.module;

import com.larpclient.bn;
import com.larpclient.cr;
import com.larpclient.hw;
import com.larpclient.q8;
import com.larpclient.ug;
import com.larpclient.w3;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1511;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2680;
import net.minecraft.class_3965;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_9334;

public final class CrystalTapModule extends Module {
   private static CrystalTapModule a;
   private final lo k = this.addSetting(
      new lo("Delay", "Extra ticks to wait once the crystal appears; 0 hits it on the first tick it exists", 0.0, 0.0, 10.0, 1.0)
   );
   private final lo Z = this.addSetting(new lo("Random Delay", "Adds up to this many more ticks at random to each hit", 0.0, 0.0, 5.0, 1.0));
   private final lo j = this.addSetting(new lo("Hit Window", "Ticks your crosshair has to reach a new crystal before it is left alone", 8.0, 1.0, 40.0, 1.0));
   private final lo G = this.addSetting(new lo("Range", "Hit reach in blocks, capped by your real reach", 3.0, 1.0, 6.0, 0.1));
   private final l3 p = this.addSetting(new l3("Anti Suicide", "Leave a crystal alone when its blast would kill you", true));
   private final w3 m = new w3();
   private class_746 L;
   private class_638 I;
   private class_2338 A;
   private boolean O;
   private String R = "Place a crystal";
   private String J = "";

   public CrystalTapModule() {
      super("Crystal Tap", "Hit a crystal the moment the one you placed appears", Category.COMBAT, -1);
      a = this;
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.R : "";
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.P;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y, l5.F);
   }

   public static void y(class_746 var0, class_1268 var1, class_3965 var2) {
      if (a != null && a.isEnabled()) {
         CrystalTapModule var3 = a;
         if (var3 != null) {
            var3.A = null;
            if (var3.isRuntimeActive() && var0 == mc.field_1724 && !cr.j() && var0.method_5998(var1).method_31574(class_1802.field_8301)) {
               class_2680 var4 = mc.field_1687.method_8320(var2.method_17777());
               if (var4.method_27852(class_2246.field_10540) || var4.method_27852(class_2246.field_9987)) {
                  var3.A = var2.method_17777().method_10062();
               }
            }
         }
      }
   }

   public static void Z(class_1269 var0) {
      if (a != null && a.isEnabled()) {
         CrystalTapModule var1 = a;
         if (var1 != null && var1.A != null) {
            class_2338 var2 = var1.A;
            var1.A = null;
            if (var1.isRuntimeActive() && var0.method_23665()) {
               int var3 = ThreadLocalRandom.current().nextInt(var1.Z.getValue().intValue() + 1);
               var1.m.s(var2, mc.field_1724.field_6012, var1.k.getValue().intValue() + var3);
               var1.s("Waiting for the crystal");
            }
         }
      }
   }

   public static boolean k() {
      if (a != null && a.isEnabled()) {
         CrystalTapModule var0 = a;
         return var0 != null && var0.isRuntimeActive() && !var0.m.C() && var0.L == mc.field_1724 && var0.I == mc.field_1687 && var0.j() != null;
      } else {
         return false;
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.L();
      } else {
         if (this.L != mc.field_1724 || this.I != mc.field_1687) {
            this.L();
            this.L = mc.field_1724;
            this.I = mc.field_1687;
         }

         boolean var1 = mc.field_1761.method_2923();
         int var10000 = this.m.C();
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         CrystalTapModule.q var2 = ((var10000 | -var10000) >>> 31 ^ 1) == 0 ? null : this.j();
         this.O = var1;
         if (var2 == null) {
            int var10 = this.m.C();
            boolean var6 = false;
            byte var5 = (byte)var10;
            var10 = Integer.signum(var5);
            if (((var10 | -var10) >>> 31 ^ 1) == 0) {
               this.s("Place a crystal");
            }
         } else {
            double var3 = cr.R(this.G.getValue());
            cr.U(this, var2.crystal(), var3, false, () -> this.B(var2.crystal()) == null, 1, () -> this.f(var2), var1x -> this.s("Waiting: " + var1x));
         }
      }
   }

   private void f(CrystalTapModule.q var1) {
      long var2 = this.m.R(var1.base(), mc.field_1724.field_6012);
      this.m.z(var1.base());
      this.s("Hit");
      q8.v("CrystalTap", "Hit crystal {} on {}, {} ticks after placing it", var1.crystal().method_5628(), var1.base().method_23854(), var2);
   }

   private CrystalTapModule.q j() {
      long var1 = mc.field_1724.field_6012;
      int var3 = this.j.getValue().intValue();
      String var4 = null;

      for (class_2338 var6 : this.m.f()) {
         class_1511 var7 = this.p(var6);
         if (this.m.W(var6, var7 != null, var1, var3) == w3.s.m) {
            String var8 = this.B(var7);
            if (var8 == null) {
               return new CrystalTapModule.q(var6, var7);
            }

            if (var4 == null) {
               var4 = var8;
            }
         }
      }

      if (var4 != null) {
         this.s(var4);
      } else if (!this.m.C()) {
         this.s("Waiting for the crystal");
      }

      return null;
   }

   private class_1511 p(class_2338 var1) {
      class_2338 var2 = var1.method_10084();
      List var3 = mc.field_1687.method_8390(class_1511.class, new class_238(var2), var1x -> !var1x.method_31481() && var1x.method_24515().equals(var2));
      return var3.isEmpty() ? null : (class_1511)var3.getFirst();
   }

   private String B(class_1511 var1) {
      class_746 var2 = mc.field_1724;
      if (!bn.q(mc)) {
         return "Paused";
      } else if (!bn.R(mc, var2) || this.O) {
         return "Your hands are busy";
      } else if (bn.w(var2)) {
         return "You already acted this tick";
      } else {
         class_1799 var3 = var2.method_6047();
         if (var3.method_57826(class_9334.field_63631) || !var3.method_45435(mc.field_1687.method_45162()) || var2.method_75202(var3, 0)) {
            return "This item cannot hit";
         } else if (var1.method_5655()) {
            return "This crystal cannot be broken";
         } else if (!CrystalOptimizerModule.A(var2)) {
            return "Your hit deals no damage";
         } else {
            double var4 = cr.R(this.G.getValue());
            if (ug.p(var2.method_33571(), var2.method_5720(), var1.method_5829(), var4).isEmpty()) {
               return "Aim at the crystal";
            } else {
               String var6 = cr.l(var1, var4, var2.method_5720());
               if (var6 != null) {
                  return "Blocked by " + var6;
               } else {
                  return this.p.getValue() && !hw.t(hw.d(var2, var1.method_73189()), Double.MAX_VALUE, var2.method_6032() + var2.method_6067())
                     ? "This crystal would kill you"
                     : null;
               }
            }
         }
      }
   }

   private void s(String var1) {
      this.R = var1;
      if (!var1.equals(this.J)) {
         this.J = var1;
         q8.v("CrystalTap", "Status: {}", var1);
      }
   }

   private void L() {
      this.m.H();
      cr.p(this);
      this.A = null;
      this.L = null;
      this.I = null;
      this.O = (boolean)0;
      this.R = "Place a crystal";
   }

   @Override
   public void onEnable() {
      this.L();
      q8.G("CrystalTap", "Enabled; hits your own placed crystals with the native attack on your real look");
   }

   @Override
   public void onDisable() {
      this.L();
      q8.v("CrystalTap", "Disabled; tracked placements cleared");
   }

   private record q(class_2338 base, class_1511 crystal) {
   }
}

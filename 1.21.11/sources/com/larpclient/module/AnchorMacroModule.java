package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.cr;
import com.larpclient.fk;
import com.larpclient.q8;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.class_12206;
import net.minecraft.class_1661;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_3965;
import net.minecraft.class_4969;
import net.minecraft.class_239.class_240;

public final class AnchorMacroModule extends Module {
   private static AnchorMacroModule H;
   private final lo A = this.addSetting(new lo("Delay", "Milliseconds to wait after a slot switch before using the anchor", 0.0, 0.0, 500.0, 5.0));
   private final lo u = this.addSetting(new lo("Charge Delay", "Minimum milliseconds between glowstone charges", 0.0, 0.0, 500.0, 5.0));
   private final lo p = this.addSetting(new lo("Totem Slot", "Hotbar slot to move to once the anchor is charged", 1.0, 1.0, 9.0, 1.0));
   private final l3 X = this.addSetting(new l3("Auto Explode", "Detonate the charged anchor from the totem slot", false));
   private final l3 Z = this.addSetting(new l3("Spam", "Keep working on anchors while Use stays held; off needs a new press for each one", true));
   private boolean r;
   private boolean m;
   private int l = -1;
   private long f;
   private long N;
   private String x = "Hold Use on an anchor";

   public AnchorMacroModule() {
      super("Anchor Macro", "Hold Use on an anchor to charge it and move to your totem slot", Category.COMBAT, -1);
      H = this;
   }

   public static boolean R() {
      if (H != null && H.isEnabled()) {
         AnchorMacroModule var0 = H;
         return var0 != null && var0.isRuntimeActive() && !var0.C() ? var0.r || mc.field_1690.field_1904.method_1434() && var0.V() != null : false;
      } else {
         return false;
      }
   }

   @Override
   public String getStatus() {
      return this.x;
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.P;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public void onTick() {
      fk.N(this);
      if (!cr.f() || !mc.method_1569()) {
         this.K("Anchor interaction suspended");
      } else if (this.C()) {
         this.K("AnchorTap handles anchors while it is enabled");
      } else {
         int var10000 = mc.field_1690.field_1904.method_1434();
         boolean var6 = false;
         byte var5 = (byte)var10000;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.K("Hold Use on an anchor");
         } else {
            boolean var8 = false;
            byte var7 = this.r;
            var10000 = Integer.signum(var7);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               boolean var10 = false;
               byte var9 = this.m;
               var10000 = Integer.signum(var9);
               if ((var10000 | -var10000) >>> 31 == 0) {
                  int var16 = this.Z.getValue();
                  boolean var12 = false;
                  byte var11 = (byte)var16;
                  var16 = Integer.signum(var11);
                  if ((var16 | -var16) >>> 31 == 0) {
                     return;
                  }
               }
            }

            class_3965 var1 = this.V();
            if (var1 != null) {
               this.r = this.m = (boolean)1;
               class_2338 var2 = var1.method_17777().method_10062();
               long var3 = System.currentTimeMillis();
               if (this.J(var2) == 0) {
                  this.R(var2, var3);
               } else {
                  this.N(var2, var3);
               }
            }
         }
      }
   }

   private void R(class_2338 var1, long var2) {
      if (var2 - this.N < Math.round(this.u.getValue())) {
         this.x = "Waiting for Charge Delay";
      } else {
         int var4 = this.V();
         if (var4 == -1) {
            this.x = "No glowstone in hotbar";
         } else if (!this.A(var4, var2)) {
            this.x = "Waiting for Delay";
         } else {
            this.q(var1, class_1802.field_8801, "Charging anchor", () -> this.J(var1) == 0, var2x -> {
               this.N = System.currentTimeMillis();
               q8.G("AnchorMacro", "Charge at {}: {}", var1.method_23854(), var2x ? "sent" : "rejected");
            });
         }
      }
   }

   private void N(class_2338 var1, long var2) {
      if (!this.A(this.p.getValue().intValue() - 1, var2)) {
         this.x = "Waiting for Delay";
      } else if (!this.X.getValue()) {
         this.m = (boolean)0;
         this.x = "Holding totem slot";
      } else {
         class_1799 var4 = mc.field_1724.method_6047();
         if (!var4.method_31574(class_1802.field_8801) && !mc.field_1724.method_6079().method_31574(class_1802.field_8801)) {
            this.q(var1, var4.method_7960() ? class_1802.field_8162 : var4.method_7909(), "Detonating anchor", () -> this.J(var1) > 0, var2x -> {
               if (var2x) {
                  this.m = (boolean)0;
               }

               q8.G("AnchorMacro", "Detonation at {}: {}", var1.method_23854(), var2x ? "sent" : "rejected");
            });
         } else {
            this.x = "Glowstone in hand blocks detonation";
         }
      }
   }

   private void q(class_2338 var1, class_1792 var2, String var3, BooleanSupplier var4, Consumer<Boolean> var5) {
      boolean var6 = fk.C(this, var1, var2, mc.field_1724.method_55754(), false, false, var4, var5);
      this.x = var6 ? var3 : "Aim at the anchor within reach";
   }

   private class_3965 V() {
      if (cr.f()
         && mc.method_1569()
         && !mc.field_1724.method_6115()
         && !mc.field_1724.method_21823()
         && mc.field_1765 instanceof class_3965 var1
         && var1.method_17783() == class_240.field_1332) {
         class_2338 var3 = var1.method_17777();
         return mc.field_1687.method_8320(var3).method_27852(class_2246.field_23152)
               && !Boolean.TRUE.equals(mc.field_1687.method_75728().method_75697(class_12206.field_63757, var3))
            ? var1
            : null;
      } else {
         return null;
      }
   }

   private int J(class_2338 var1) {
      class_2680 var2 = mc.field_1687.method_8320(var1);
      return var2.method_27852(class_2246.field_23152) ? (Integer)var2.method_11654(class_4969.field_23153) : 0;
   }

   private int V() {
      class_1661 var1 = mc.field_1724.method_31548();
      if (mc.field_1724.method_6047().method_31574(class_1802.field_8801)) {
         return var1.method_67532();
      }

      for (int var2 = 0; var2 < 9; var2++) {
         if (var1.method_5438(var2).method_31574(class_1802.field_8801)) {
            return var2;
         }
      }

      return -1;
   }

   private boolean A(int var1, long var2) {
      class_1661 var4 = mc.field_1724.method_31548();
      long var5 = Math.round(this.A.getValue());
      if (var4.method_67532() != var1) {
         var4.method_61496(var1);
         this.l = var1;
         this.f = var2;
         return var5 == 0L;
      } else {
         return var1 != this.l || var2 - this.f >= var5;
      }
   }

   private boolean C() {
      AnchorTapModule var1 = LarpClient.b().G().b(AnchorTapModule.class);
      return var1 != null && var1.isRuntimeActive();
   }

   private void K(String var1) {
      this.r = this.m = (boolean)0;
      this.l = -1;
      this.f = 0L;
      this.x = var1;
   }

   @Override
   public void onEnable() {
      this.N = 0L;
      this.K("Hold Use on an anchor");
      q8.G(
         "AnchorMacro",
         "Enabled; totemSlot={}, autoExplode={}, spam={}, delay={}ms, chargeDelay={}ms",
         this.p.getValue().intValue(),
         this.X.getValue(),
         this.Z.getValue(),
         this.A.getValue().intValue(),
         this.u.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      fk.N(this);
      this.K("Hold Use on an anchor");
      q8.v("AnchorMacro", "Anchor intents cleared");
   }
}

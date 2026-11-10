package com.larpclient.module;

import com.larpclient.cr;
import com.larpclient.fk;
import com.larpclient.q8;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.minecraft.class_12206;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_3965;
import net.minecraft.class_4969;

public final class AnchorTapModule extends Module {
   private static AnchorTapModule Y;
   private final lo V = this.addSetting(new lo("Delay", "Ticks between executed anchor steps", 0.0, 0.0, 20.0, 1.0));
   private final l3 e = this.addSetting(new l3("Safe Anchor", "Place an obsidian shield between you and the anchor before detonation", false));
   private final l3 S = this.addSetting(new l3("Air Replace", "Replace the removed anchor once after detonation", false));
   private final lo r = this.addSetting(new lo("Range", "Reach capped by native block interaction distance", 4.1, 1.0, 6.0, 0.1));
   private final li C = this.addSetting(new li("Rotation", "Visible turns the real view; Crosshair waits for your aim", "Visible", "Visible", "Crosshair"));
   private final l3 I = this.addSetting(new l3("Auto Switch", "Use hotbar items and restore the selected slot after each step", true));
   private AnchorTapModule.f j = AnchorTapModule.f.U;
   private class_2338 y;
   private boolean p;
   private long U;
   private long O;
   private String b = "Aim at an anchor and press Use";

   public AnchorTapModule() {
      super("AnchorTap", "Charge and explode an anchor with the use key", Category.COMBAT, -1);
      Y = this;
   }

   public static boolean N() {
      if (Y != null && Y.isEnabled()) {
         AnchorTapModule var0 = Y;
         if (var0 == null || !var0.isRuntimeActive() || !cr.f()) {
            return false;
         } else {
            return var0.j != AnchorTapModule.f.U
               ? true
               : mc.field_1765 instanceof class_3965 var1
                  && mc.field_1690.field_1904.method_1434()
                  && mc.field_1687.method_8320(var1.method_17777()).method_27852(class_2246.field_23152);
         }
      } else {
         return false;
      }
   }

   @Override
   public String getStatus() {
      return this.b;
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
      if (cr.f() && mc.method_1569()) {
         boolean var1 = mc.field_1690.field_1904.method_1434();
         if (var1) {
            boolean var13 = false;
            byte var12 = this.p;
            int var10000 = Integer.signum(var12);
            if ((var10000 | -var10000) >>> 31 == 0
               && this.j == AnchorTapModule.f.U
               && mc.field_1765 instanceof class_3965 var2
               && mc.field_1687.method_8320(var2.method_17777()).method_27852(class_2246.field_23152)) {
               this.y = var2.method_17777().method_10062();
               this.j = AnchorTapModule.f.v;
               this.U = mc.field_1687.method_75260();
               this.O = 0L;
               q8.v("AnchorTap", "Anchor cycle started at {}", this.y);
            }
         }

         this.p = var1;
         if (this.j != AnchorTapModule.f.U && this.y != null) {
            long var16 = mc.field_1687.method_75260();
            if (mc.field_1687.method_22340(this.y) && !mc.field_1724.method_6115()) {
               if (var16 - this.U > 40L) {
                  this.I("Anchor response timed out");
               } else if (var16 >= this.O) {
                  class_2680 var4 = mc.field_1687.method_8320(this.y);
                  boolean var5 = var4.method_27852(class_2246.field_23152);
                  switch (this.j) {
                     case v:
                        if (!var5) {
                           this.I("Anchor no longer exists");
                           return;
                        }

                        if ((Integer)var4.method_11654(class_4969.field_23153) > 0) {
                           this.y(AnchorTapModule.f.L);
                           return;
                        }

                        this.H(
                           class_1802.field_8801,
                           AnchorTapModule.f.C,
                           () -> mc.field_1687.method_8320(this.y).method_27852(class_2246.field_23152)
                              && (Integer)mc.field_1687.method_8320(this.y).method_11654(class_4969.field_23153) == 0
                        );
                        break;
                     case C:
                        if (!var5) {
                           this.I("Anchor removed before charging");
                        } else if ((Integer)var4.method_11654(class_4969.field_23153) > 0) {
                           this.y(AnchorTapModule.f.L);
                        } else if (var16 - this.U > 8L) {
                           this.I("Anchor charge not confirmed");
                        }
                        break;
                     case L:
                        if (!this.e.getValue()) {
                           this.y(AnchorTapModule.f.z);
                           return;
                        }

                        class_2338 var18 = this.g();
                        if (!fk.v(var18)) {
                           class_2680 var19 = mc.field_1687.method_8320(var18);
                           if (!var19.method_27852(class_2246.field_10540)
                              && !var19.method_27852(class_2246.field_22423)
                              && !var19.method_27852(class_2246.field_9987)) {
                              this.I("Safe Anchor shield position occupied by another block");
                           } else {
                              this.y(AnchorTapModule.f.z);
                           }

                           return;
                        }

                        if (mc.field_1724.method_5829().method_994(new class_238(var18))) {
                           this.I("No safe shield position outside your body");
                           return;
                        }

                        int var23 = fk.T(class_1802.field_8281, this.I.getValue());
                        byte var9 = -1;
                        int var8 = var23;
                        var23 = Integer.compare(var8, var9);
                        if ((var23 | -var23) >>> 31 == 0) {
                           this.I("Safe Anchor requires hotbar obsidian");
                           return;
                        }

                        boolean var7 = fk.a(
                           this,
                           var18,
                           class_1802.field_8281,
                           this.r.getValue(),
                           this.C.G("Visible"),
                           this.I.getValue(),
                           () -> this.j == AnchorTapModule.f.L && this.V() && !mc.field_1724.method_5829().method_994(new class_238(var18)),
                           var1x -> {
                              if (var1x) {
                                 this.O = mc.field_1687.method_75260() + Math.max(1, this.V.getValue().intValue());
                                 this.b = "Waiting for obsidian shield";
                              }
                           }
                        );
                        if (!var7) {
                           this.b = "Aim at a reachable shield support";
                        }
                        break;
                     case z:
                        if (!this.V()) {
                           this.I("Charged anchor no longer exists");
                           return;
                        }

                        if ((Boolean)mc.field_1687.method_75728().method_75697(class_12206.field_63757, this.y)) {
                           this.I("Anchors set spawn here; detonation skipped");
                           return;
                        }

                        if (mc.field_1724.method_6079().method_31574(class_1802.field_8801)) {
                           int var22 = (Integer)var4.method_11654(class_4969.field_23153);
                           byte var15 = 4;
                           int var14 = var22;
                           if ((Integer.compare(var14, var15) >>> 31 ^ 1) == 0) {
                              this.b = "Move offhand glowstone before detonation";
                              return;
                           }
                        }

                        class_1792 var17 = this.K();
                        if (var17 == null) {
                           this.I("A non-glowstone hotbar item is required");
                           return;
                        }

                        this.H(
                           var17,
                           AnchorTapModule.f.T,
                           () -> this.V()
                              && (
                                 !mc.field_1724.method_6079().method_31574(class_1802.field_8801)
                                    || (Integer)mc.field_1687.method_8320(this.y).method_11654(class_4969.field_23153) == 4
                              )
                              && !(Boolean)mc.field_1687.method_75728().method_75697(class_12206.field_63757, this.y)
                        );
                        break;
                     case T:
                        if (!var5) {
                           if (this.S.getValue() && fk.v(this.y)) {
                              this.y(AnchorTapModule.f.H);
                           } else {
                              this.I("Anchor cycle complete");
                           }
                        } else if (var16 - this.U > 8L) {
                           this.I("Anchor removal not confirmed");
                        }
                        break;
                     case H:
                        if (!fk.v(this.y)) {
                           this.I("Replacement position occupied");
                           return;
                        }

                        int var20 = fk.T(class_1802.field_23141, this.I.getValue());
                        byte var11 = -1;
                        int var10 = var20;
                        var20 = Integer.compare(var10, var11);
                        if ((var20 | -var20) >>> 31 == 0) {
                           this.I("Replacement anchor missing from hotbar");
                           return;
                        }

                        boolean var6 = fk.a(
                           this,
                           this.y,
                           class_1802.field_23141,
                           this.r.getValue(),
                           this.C.G("Visible"),
                           this.I.getValue(),
                           () -> this.j == AnchorTapModule.f.H && fk.v(this.y),
                           var1x -> {
                              if (var1x) {
                                 this.y(AnchorTapModule.f.J);
                              }
                           }
                        );
                        if (!var6) {
                           this.b = "Aim at a reachable replacement support";
                        }
                        break;
                     case J:
                        if (var5) {
                           this.I("Anchor replacement complete");
                        } else if (var16 - this.U > 8L) {
                           this.I("Anchor replacement not confirmed");
                        }
                  }
               }
            } else {
               this.I("Anchor cycle cancelled");
            }
         }
      } else {
         this.I("Anchor interaction suspended");
      }
   }

   private class_2338 g() {
      class_243 var1 = mc.field_1724.method_73189().method_1020(class_243.method_24953(this.y));
      class_2350 var2 = Math.abs(var1.field_1352) > Math.abs(var1.field_1350)
         ? (var1.field_1352 >= 0.0 ? class_2350.field_11034 : class_2350.field_11039)
         : (var1.field_1350 >= 0.0 ? class_2350.field_11035 : class_2350.field_11043);
      return this.y.method_10093(var2);
   }

   private boolean V() {
      return this.y != null
         && mc.field_1687.method_8320(this.y).method_27852(class_2246.field_23152)
         && (Integer)mc.field_1687.method_8320(this.y).method_11654(class_4969.field_23153) > 0;
   }

   private class_1792 K() {
      class_1799 var1 = mc.field_1724.method_6047();
      if (!var1.method_31574(class_1802.field_8801)) {
         return var1.method_7909();
      }

      if (this.I.getValue()) {
         for (int var2 = 0; var2 < 9; var2++) {
            class_1799 var3 = mc.field_1724.method_31548().method_5438(var2);
            if (!var3.method_31574(class_1802.field_8801)) {
               return var3.method_7909();
            }
         }
      }

      return null;
   }

   private void H(class_1792 var1, AnchorTapModule.f var2, BooleanSupplier var3) {
      if (fk.T(var1, this.I.getValue()) == -1) {
         this.I("Required anchor item missing from hotbar");
      } else {
         AnchorTapModule.f var4 = this.j;
         boolean var5 = fk.C(
            this, this.y, var1, this.r.getValue(), this.C.G("Visible"), this.I.getValue(), () -> this.j == var4 && var3.getAsBoolean(), var2x -> {
               if (var2x) {
                  this.y(var2);
                  this.O = mc.field_1687.method_75260() + Math.max(1, this.V.getValue().intValue());
               } else {
                  this.b = "Anchor interaction rejected; retrying";
               }
            }
         );
         if (!var5) {
            this.b = "Aim at the anchor within native reach";
         }
      }
   }

   private void y(AnchorTapModule.f var1) {
      this.j = var1;
      this.U = mc.field_1687.method_75260();
      this.b = var1.name().toLowerCase().replace('_', ' ');
   }

   private void I(String var1) {
      fk.N(this);
      if (this.j != AnchorTapModule.f.U) {
         q8.v("AnchorTap", "{}", var1);
      }

      this.j = AnchorTapModule.f.U;
      this.y = null;
      this.U = 0L;
      this.O = 0L;
      this.b = var1;
   }

   @Override
   public void onEnable() {
      this.p = (boolean)(mc.field_1724 != null && mc.field_1690.field_1904.method_1434() ? 1 : 0);
      this.I("Aim at an anchor and press Use");
      q8.v("AnchorTap", "Anchor use handler active");
   }

   @Override
   public void onDisable() {
      this.I("Anchor intents cleared");
      this.p = (boolean)0;
   }

   private enum f {
      U,
      v,
      C,
      L,
      z,
      T,
      H,
      J;
   }
}

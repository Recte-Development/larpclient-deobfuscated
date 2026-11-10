package com.larpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1533;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1716;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_3917;
import net.minecraft.class_3965;
import net.minecraft.class_465;
import net.minecraft.class_480;
import net.minecraft.class_634;
import net.minecraft.class_638;
import net.minecraft.class_640;
import net.minecraft.class_9209;
import net.minecraft.class_9334;

@Environment(EnvType.CLIENT)
public final class qr extends q7 {
   private static final qr zW = new qr();
   private final Map<Integer, Integer> zA = new HashMap<>();
   private final Map<Integer, class_1799> zc = new HashMap<>();
   private class_1799[] zs;
   private Map<Integer, Integer> z3;
   private qr.z z6 = qr.z.u;
   private qr.z zq = qr.z.u;
   private int zV = -1;
   private int zg = -1;
   private int zp = -1;
   private class_1799 zH = class_1799.field_8037;
   private int zm = -1;
   private int zU;
   private int zz;
   private boolean zK;
   private String z7;
   private class_2338 zb;
   private int zd = -1;
   private class_638 zk;
   private long zt = -1L;
   private qr.j z9;
   private int zL = -1;
   private class_1799 z5;
   private boolean zF;
   private long zB;
   private boolean za;
   private boolean zw;
   private long z2;
   private String zR = "no map change";

   private qr() {
   }

   public static qr h() {
      return zW;
   }

   public qr.z M() {
      return this.z6;
   }

   public String p() {
      return this.z6.B();
   }

   public int C() {
      return this.zV;
   }

   public int J() {
      return this.zm;
   }

   public boolean R() {
      return this.zK;
   }

   public int b(int var1) {
      return this.zA.getOrDefault(this.getTemplateIdForValue(var1), 0);
   }

   public void k(String var1) {
      this.z6 = "higher".equalsIgnoreCase(var1) ? qr.z.a : ("lower".equalsIgnoreCase(var1) ? qr.z.q : qr.z.u);
      if (this.z6 == qr.z.u) {
         this.u();
      }
   }

   public void D() {
      this.k(switch (this.z6) {
         case u -> "higher";
         case a -> "lower";
         case q -> "off";
      });
   }

   public void u() {
      this.clearQueue();
      this.zV = this.zg = this.zp = -1;
      this.zH = class_1799.field_8037;
      this.z3 = null;
      this.zz = 0;
   }

   @Override
   public void clearQueue() {
      this.zK = (boolean)0;
      this.zm = -1;
      if (this.z9 != null) {
         this.abortSwap();
      } else {
         this.zA.clear();
      }
   }

   @Override
   protected boolean supportsPhysicalSwap() {
      return false;
   }

   @Override
   protected class_2338 getAssignedDispenserPosition(class_310 var1) {
      hu var2 = hu.f();
      return var2.m(hu.q.R).contains(this.O()) ? var2.j(this.O()) : null;
   }

   @Override
   public boolean hasQueuedForce() {
      return this.zK && this.zm > 0;
   }

   @Override
   public int getPendingValue() {
      return this.hasQueuedForce() ? this.zm : -1;
   }

   @Override
   public int getQueuedValueAt(int var1) {
      return var1 == 0 ? this.getPendingValue() : -1;
   }

   @Override
   public String pendingLabel() {
      return this.getPendingValue() < 1 ? "none" : String.valueOf(this.getPendingValue());
   }

   @Override
   public int containerIndexForValue(int var1) {
      return var1 >= 1 && var1 <= 9 ? var1 - 1 : -1;
   }

   @Override
   public void setContainerContext(String var1) {
      if (!Objects.equals(var1, this.O())) {
         if (this.O() != null) {
            this.u();
         }

         super.setContainerContext(var1);
      }
   }

   @Override
   public boolean scanCurrentContainerTemplateSlots(class_310 var1) {
      boolean var2 = super.scanCurrentContainerTemplateSlots(var1);
      if (var1 != null && var1.field_1724 != null && var1.field_1724.field_7512 instanceof class_1716 var3) {
         for (int var7 = 0; var7 < 9; var7++) {
            class_1799 var5 = var3.method_7611(var7).method_7677();
            int var6 = this.o(var5);
            if (var6 > 0) {
               this.zc.put(var6, var5.method_46651(1));
            }
         }
      }

      return var2;
   }

   public void r(class_310 var1) {
      this.L(var1);
      if (var1 != null && var1.field_1724 != null && fd.h().v && this.z6 != qr.z.u && !this.zK && !this.isSwapBusy()) {
         q7 var2 = q7.L();
         if (var2 == this || !var2.isSwapBusy()) {
            this.zs = Z(var1);
            if (this.zV > 0) {
               int var3 = this.zg >= 0 && this.zg < this.zs.length && !this.zH.method_7960() && R(this.zH, this.zs[this.zg]) ? this.zg : -1;
               if (var3 < 0) {
                  for (int var4 = 0; var4 < this.zs.length; var4++) {
                     if (!this.zH.method_7960() && R(this.zH, this.zs[var4])) {
                        if (var3 >= 0) {
                           this.clearQueue();
                           yq.E("HigherLower", "Activation disarmed: moved reference stack is ambiguous");
                           return;
                        }

                        var3 = var4;
                     }
                  }
               }

               if (var3 < 0) {
                  this.u();
                  yq.E("HigherLower", "Previous reference removed; waiting for first card of new round");
               } else {
                  this.zg = var3;
               }
            }

            this.zA.clear();
            this.zA.putAll(n(this.zs));
            if (this.zV < 1 && this.z3 == null) {
               this.z3 = W(var1);
            }

            this.zp = this.zV;
            this.zq = this.z6;
            this.z7 = this.O();
            class_2338 var5 = this.getAssignedDispenserPosition(var1);
            this.zb = var5 == null ? null : var5.method_10062();
            this.zm = this.T();
            this.zd = this.f(this.zm);
            this.zK = (boolean)1;
            this.zU = 0;
            this.zR = "no map change";
            yq.R("HigherLower Activation target=" + this.zq.B() + " reference=" + this.zp + " recommendation=" + this.zm);
         }
      }
   }

   @Override
   public void onDealtCardSlotUpdate(int var1, int var2, int var3) {
      this.L(class_310.method_1551());
   }

   private void L(class_310 var1) {
      if (this.zK && this.z9 == null && var1 != null && var1.field_1724 != null) {
         class_1799[] var2 = Z(var1);
         Map var3 = n(var2);
         int var4 = -1;
         int var5 = -1;

         for (int var6 = 0; var6 < var2.length; var6++) {
            if (!k(var2[var6], this.zs[var6])) {
               class_1799 var7 = this.zs[var6];
               class_1799 var8 = var2[var6];
               int var9 = this.o(var8);
               if (var4 >= 0 || var9 < 1 || !var7.method_7960() && !R(var7, var8) || var8.method_7947() != var7.method_7947() + 1) {
                  this.zR = "slot="
                     + var6
                     + " reason="
                     + (
                        var4 >= 0
                           ? "multiple map changes"
                           : (var9 < 1 ? "unrecognized or removed map" : (!var7.method_7960() && !R(var7, var8) ? "origin/components changed" : "not +1"))
                     )
                     + " before="
                     + B(var7)
                     + " after="
                     + B(var8);
                  return;
               }

               var4 = var6;
               var5 = var9;
            }
         }

         if (var4 >= 0 && var3.getOrDefault(D(var2[var4]), 0) == this.zA.getOrDefault(D(var2[var4]), 0) + 1) {
            int var12 = this.zq == qr.z.a ? (var5 > this.zp ? 1 : 0) : (var5 < this.zp ? 1 : 0);
            if (this.zp < 1 || this.zm < 1 || var12 != 0 || var5 == this.zm) {
               this.n(var5, var4);
            } else if (il.K(var1) && il.S(this, var4, this.zm)) {
               int var14 = this.zm;
               this.X(var14, var4, var5, true);
            } else {
               int var13 = -1;
               int var15 = this.zm == this.zp ? 1 : 0;
               if (var15 != 0 && this.zg >= 0 && this.o(this.zs[this.zg]) == this.zm) {
                  var13 = this.zg;
               } else if (var15 == 0) {
                  int var16 = this.zg >= 0 && this.zg < 9 ? this.zg : var1.field_1724.method_31548().method_67532();

                  for (int var10 = 1; var10 <= 9; var10++) {
                     int var11 = (var16 + var10) % 9;
                     if (this.zs[var11].method_7960() && (var2[var11].method_7960() || var11 == var4)) {
                        var13 = var11;
                        break;
                     }
                  }
               }

               class_1799 var17 = this.zc.get(this.zm);
               if (var15 != 0 && var13 >= 0) {
                  var17 = this.zs[var13].method_46651(1);
               }

               if (var17 != null
                  && D(var17) == this.getTemplateIdForValue(this.zm)
                  && var13 >= 0
                  && this.zb != null
                  && (var15 == 0 || R(this.zs[var13], var17) && this.zs[var13].method_7947() < var17.method_7914())) {
                  qr.j var18 = new qr.j(this.z7, this.zb, this.zq, this.zp, this.zm, var5, var4, var13, this.zs, var2[var4], this.zA);
                  var18.w = this.zd;
                  var18.e.merge(this.getTemplateIdForValue(var18.B), 1, Integer::sum);
                  var18.X.put(var4, var18.J[var4].method_7972());
                  var18.X.put(var13, var17.method_46651(var18.J[var13].method_7947() + 1));
                  this.z9 = var18;
                  var18.x = 1;
                  this.zK = (boolean)0;
                  this.q(var1);
                  yq.E(
                     "HigherLower",
                     "One-card exchange real="
                        + var5
                        + " ->"
                        + var18.B
                        + " target="
                        + var18.T.B()
                        + " reference="
                        + var18.k
                        + " arrival="
                        + var4
                        + " destination="
                        + var13
                  );
                  this.z();
               } else {
                  yq.E("HigherLower", "Exchange unavailable: destination, components, capacity or assignment");
                  this.n(var5, var4);
               }
            }
         } else {
            this.zR = "reason=no +1 map total arrival=" + var4;
         }
      }
   }

   private void n(int var1, int var2) {
      this.X(var1, var2, var1, false);
   }

   private void X(int var1, int var2, int var3, boolean var4) {
      if (this.z9 == null) {
         yq.E("HigherLower", yq.R(Integer.toString(var3), Integer.toString(var1), this.zp > 0 && this.zm > 0, var4));
      }

      this.zV = var1;
      this.zg = var2;
      this.zH = class_310.method_1551().field_1724.method_31548().method_5438(var2).method_46651(1);
      this.zK = (boolean)0;
      this.zm = -1;
      this.zA.clear();
   }

   @Override
   public void tick(class_310 var1) {
      if (var1 != null && var1.field_1724 != null && var1.field_1687 != null) {
         if (this.zk != null && this.zk != var1.field_1687) {
            this.z9 = null;
            this.zL = -1;
            this.u();
         }

         if (this.zk != var1.field_1687 || this.zt != var1.field_1687.method_75260()) {
            this.zk = var1.field_1687;
            this.zt = var1.field_1687.method_75260();
            if (this.z9 != null) {
               this.z9.x++;
               this.G(var1);
            } else if (fd.h().v) {
               this.L(var1);
               if (this.zK && ++this.zU > 100) {
                  yq.E("HigherLower", "Draw timed out: " + this.zR + " baselineMapCounts=" + this.zA + " receivedMapCounts=" + n(Z(var1)));
                  this.clearQueue();
               }

               if (this.z9 == null && !this.zK && this.zV > 0 && this.z3 != null) {
                  Map var2 = W(var1);
                  int var3 = 0;

                  for (Entry var5 : var2.entrySet()) {
                     var3 += Math.max(0, (Integer)var5.getValue() - this.z3.getOrDefault(var5.getKey(), 0));
                  }

                  this.zz = var3 == 0 ? this.zz + 1 : 0;
                  if (this.zz >= 6) {
                     this.u();
                  }
               }
            }
         }
      } else {
         this.z9 = null;
         this.zL = -1;
         this.u();
         this.zk = null;
      }
   }

   private void G(class_310 var1) {
      qr.j var2 = this.z9;
      if (!var2.b && var2.V.h(var2.E) || !Objects.equals(var2.Q, this.O()) || !var2.d.equals(this.getAssignedDispenserPosition(var1))) {
         this.c("timeout or assignment changed step=" + var2.g + " sync=" + var2.q + " ready=" + var2.Y + " " + var2.a.E());
      }

      class_1703 var3 = var1.field_1724.field_7512;
      if (var2.q < 0) {
         if (var2.x >= 100) {
            this.c("dispenser did not open; no cards moved");
         }

         if (var2.m) {
            if (!var3.method_34255().method_7960()) {
               var2.X.clear();
               this.zL = -1;
            }

            this.o(var1, false);
         } else {
            if (var1.field_1755 == null && var3 == var1.field_1724.field_7498 && var3.method_34255().method_7960() && var2.a.I(var2.x)) {
               this.G(var1, var2);
            }
         }
      } else if (!(var3 instanceof class_1716) || var3 != var2.c || var3.field_7763 != var2.q) {
         this.c("handler replaced");
         var2.X.clear();
         this.zL = -1;
         this.o(var1, false);
      } else if (!var2.K) {
         if (var2.Y && var1.field_1761 != null) {
            if (var2.m || var2.a.d()) {
               if (var2.b) {
                  if (!var2.V.Z(var2.f < 0 || ss.h(var2.l.get(var2.f), var2.p), ss.h(var2.l.get(var2.C), var2.n), ss.h(var2.s, var2.Z))) {
                     if (var2.V.m()) {
                        this.c("click not confirmed; no replay " + c(var2));
                        this.V(var1, var3, var2);
                     }

                     return;
                  }

                  yq.R("HigherLower Click confirmed " + c(var2));
                  var2.b = (boolean)0;
                  if (!var2.m && var2.g == 3) {
                     var2.X.put(var2.y, var2.l.get(var2.C).method_7972());
                  }

                  var2.F = 0;
                  if (!var2.m) {
                     var2.g += 3;
                  }
               }

               if (var2.m) {
                  this.F(var1, var3, var2);
               } else {
                  int var4 = f(var3, var1, var2.o);
                  int var5 = f(var3, var1, var2.y);
                  if (var4 >= 0 && var5 >= 0) {
                     if (var2.g == 0) {
                        if (!var3.method_34255().method_7960()) {
                           this.c("occupied cursor");
                           return;
                        }

                        for (int var6 = 0; var6 < var2.J.length; var6++) {
                           class_1799 var7 = var6 == var2.o ? var2.h.method_46651(var2.J[var6].method_7947() + 1) : var2.J[var6];
                           class_1799 var8 = var1.field_1724.method_31548().method_5438(var6);
                           if (var6 != var2.o && var6 != var2.y ? !k(var8, var7) : !ss.h(var8, var7)) {
                              this.c("inventory changed before extraction slot=" + var6 + " expected=" + B(var7) + " received=" + B(var8));
                              return;
                           }
                        }

                        if (var2.M < 0) {
                           for (int var11 = 0; var11 < 9; var11++) {
                              if (var11 != var2.w
                                 && class_1799.method_31577(var3.method_7611(var11).method_7677(), var3.method_7611(var4).method_7677())
                                 && class_1799.method_31577(var2.l.get(var11), var2.l.get(var4))
                                 && R(var3.method_7611(var11), var2.h, 1)) {
                                 var2.M = var11;
                                 break;
                              }
                           }

                           for (int var12 = 0; var12 < 9; var12++) {
                              if (var2.M < 0
                                 && var12 != var2.w
                                 && var3.method_7611(var12).method_7677().method_7960()
                                 && var2.l.get(var12).method_7960()
                                 && R(var3.method_7611(var12), var2.h, 1)) {
                                 var2.M = var12;
                                 break;
                              }
                           }
                        }

                        if (var2.w < 0
                           || var2.w >= 9
                           || var2.M < 0
                           || this.o(var3.method_7611(var2.w).method_7677()) != var2.B
                           || !var3.method_7611(var2.w).method_7674(var1.field_1724)
                           || !var3.method_7611(var5).method_7680(var3.method_7611(var2.w).method_7677())
                           || !var2.J[var2.y].method_7960() && !R(var2.J[var2.y], var3.method_7611(var2.w).method_7677())
                           || var2.J[var2.y].method_7947() + 1 > var3.method_7611(var5).method_7676(var3.method_7611(var2.w).method_7677())
                           || !var3.method_7611(var4).method_7674(var1.field_1724)) {
                           this.c("source, return space, or destination unavailable");
                           return;
                        }

                        class_1799 var13 = var3.method_7611(var2.w).method_7677();
                        if (!var2.J[var2.y].method_7960()
                           && (
                              !class_1799.method_31577(var13, var3.method_7611(var5).method_7677())
                                 || !class_1799.method_31577(var2.l.get(var2.w), var2.l.get(var5))
                           )) {
                           this.c("replacement destination has incompatible vanilla components");
                           return;
                        }

                        var2.v = var13.method_7972();
                        var2.u = var3.method_7611(var2.M).method_7677().method_7972();
                        var2.X.put(var2.y, var13.method_46651(var2.J[var2.y].method_7947() + 1));
                        this.E(var1, var3, var4, var2.M);
                     } else if (var2.g == 3) {
                        if (!var3.method_34255().method_7960()
                           || !ss.h(var3.method_7611(var2.w).method_7677(), var2.v)
                           || !ss.h(var3.method_7611(var4).method_7677(), var2.J[var2.o])
                           || !ss.h(var3.method_7611(var5).method_7677(), var2.J[var2.y])
                           || !R(var3.method_7611(var5), var3.method_7611(var2.w).method_7677(), 1)) {
                           this.c("replacement source changed");
                           return;
                        }

                        this.E(var1, var3, var2.w, var5);
                     } else {
                        int var14 = var2.s.method_7960()
                              && var3.method_34255().method_7960()
                              && var2.e.equals(n(Z(var1)))
                              && ss.h(var2.l.get(var2.w), var2.v.method_46651(var2.v.method_7947() - 1))
                              && ss.h(var2.l.get(var2.M), var2.h.method_46651(var2.u.method_7947() + 1))
                           ? 1
                           : 0;

                        for (int var15 = 0; var15 < var2.J.length && var14 != 0; var15++) {
                           int var16 = f(var3, var1, var15);
                           class_1799 var9 = var15 == var2.y ? var2.X.get(var15) : var2.J[var15];
                           int var10 = var15 != var2.o && var15 != var2.y ? 0 : 1;
                           var14 = var16 < 0
                                 || (
                                    var10 != 0
                                       ? !ss.h(var2.l.get(var16), var9) || !ss.h(var1.field_1724.method_31548().method_5438(var15), var9)
                                       : !k(var2.l.get(var16), var9) || !k(var1.field_1724.method_31548().method_5438(var15), var9)
                                 )
                              ? 0
                              : 1;
                           if (var14 == 0) {
                              this.c(
                                 "final slot="
                                    + var15
                                    + " expected="
                                    + B(var9)
                                    + " server="
                                    + (var16 < 0 ? "missing" : B(var2.l.get(var16)))
                                    + " local="
                                    + B(var1.field_1724.method_31548().method_5438(var15))
                              );
                           }
                        }

                        if (var14 == 0) {
                           this.c(
                              "final quantity ledger mismatch expectedCounts="
                                 + var2.e
                                 + " receivedCounts="
                                 + n(Z(var1))
                                 + " source="
                                 + B(var2.l.get(var2.w))
                                 + " expectedSource="
                                 + B(var2.v.method_46651(var2.v.method_7947() - 1))
                                 + " deposit="
                                 + B(var2.l.get(var2.M))
                                 + " expectedDeposit="
                                 + B(var2.h.method_46651(var2.u.method_7947() + 1))
                           );
                           return;
                        }

                        this.o(var1, true);
                     }
                  } else {
                     this.c("player slots missing");
                  }
               }
            }
         } else {
            if (var2.m) {
               this.V(var1, var3, var2);
            }
         }
      }
   }

   private void E(class_310 var1, class_1703 var2, int var3, int var4) {
      qr.j var5 = this.z9;
      if (!var5.b && !var5.m && !var5.K) {
         if (var5.Y && var2 == var5.c && var5.l.size() == var2.field_7761.size() && var5.s.method_7960() && var2.method_34255().method_7960()) {
            for (int var6 = 0; var6 < var5.l.size(); var6++) {
               if (!ss.h(var2.method_7611(var6).method_7677(), var5.l.get(var6))) {
                  this.c("pre-batch local/server mismatch slot=" + var6);
                  this.V(var1, var2, var5);
                  return;
               }
            }

            class_1735 var26 = var2.method_7611(var3);
            class_1735 var7 = var2.method_7611(var4);
            class_1799 var8 = var26.method_7677().method_7972();
            class_1799 var9 = var7.method_7677().method_7972();
            class_1799 var10 = var5.l.get(var3);
            class_1799 var11 = var5.l.get(var4);
            int var12 = !var9.method_7960() && !class_1799.method_31577(var8, var9) || !var11.method_7960() && !class_1799.method_31577(var10, var11) ? 0 : 1;
            t.p var13 = com.larpclient.t.batch(
               var3,
               var4,
               var10.method_7947(),
               var11.method_7947(),
               (boolean)var12,
               Math.min(var26.method_7676(var10), var10.method_7914()),
               Math.min(var7.method_7676(var10), var10.method_7914())
            );
            t.p var14 = com.larpclient.t.batch(
               var3,
               var4,
               var8.method_7947(),
               var9.method_7947(),
               (boolean)var12,
               Math.min(var26.method_7676(var8), var8.method_7914()),
               Math.min(var7.method_7676(var8), var8.method_7914())
            );
            if (var13 != null
               && var13.equals(var14)
               && var26.method_7674(var1.field_1724)
               && var7.method_7680(var8)
               && var7.method_7680(var10)
               && (var13.clicks() != 3 || var26.method_7680(var8) && var26.method_7680(var10))) {
               var5.f = var3;
               var5.p = var10.method_46651(var13.returned().slotCount());
               var5.C = var4;
               var5.n = (var11.method_7960() ? var10 : var11).method_46651(var13.placement().slotCount());
               var5.Z = class_1799.field_8037;
               var5.b = (boolean)1;
               var5.F = 0;
               class_634 var15 = var1.method_1562();
               class_640 var16 = var15 == null ? null : var15.method_2871(var1.field_1724.method_5667());
               var5.V.T(var16 == null ? 0 : var16.method_2959());
               var5.V.v(var2, var4, var3, this.z2);
               var5.P = var5.D = var5.G = "none";

               for (int var17 = 0; var17 < var13.clicks(); var17++) {
                  int var18 = var17 == 1 ? var4 : var3;
                  int var19 = var17 == 0 ? var13.pickupButton() : (var17 == 1 ? 1 : 0);
                  var5.r = var19;
                  this.za = (boolean)(var17 == var13.clicks() - 1 ? 1 : 0);

                  try {
                     var1.field_1761.method_2906(var2.field_7763, var18, var19, class_1713.field_7790, var1.field_1724);
                  } finally {
                     this.za = (boolean)0;
                  }

                  int var20 = var17 == 2 ? var13.returned().slotCount() : var13.pickup().slotCount();
                  int var21 = var17 == 0 ? var9.method_7947() : var13.placement().slotCount();
                  int var22 = var17 == 0 ? var13.pickup().cursorCount() : (var17 == 1 ? var13.placement().cursorCount() : 0);
                  class_1799 var23 = (var9.method_7960() ? var8 : var9).method_46651(var21);
                  if (!class_1799.method_7973(var26.method_7677(), var8.method_46651(var20))
                     || !class_1799.method_7973(var7.method_7677(), var23)
                     || !class_1799.method_7973(var2.method_34255(), var8.method_46651(var22))) {
                     this.c("batch prediction diverged after click=" + var17 + "; no replay");
                     this.V(var1, var2, var5);
                     return;
                  }
               }

               yq.R("HigherLower Batch issued clicks=" + var13.clicks() + " source=" + var3 + " " + c(var5));
            } else {
               this.c("unsafe batch: source, capacity or vanilla component merge mismatch");
            }
         } else {
            this.c("batch requires known handler and empty authoritative cursor");
         }
      }
   }

   private void t(class_310 var1, class_1703 var2, int var3, int var4) {
      qr.j var5 = this.z9;
      var5.f = -1;
      if (ss.h(var2.method_7611(var3).method_7677(), var5.l.get(var3)) && ss.h(var2.method_34255(), var5.s)) {
         class_1799 var6 = var5.l.get(var3);
         class_1799 var7 = var5.s;
         t var8 = com.larpclient.t.plan(
            var6.method_7947(),
            var7.method_7947(),
            var4,
            var7.method_7960() || R(var2.method_7611(var3), var7, var4 == 1 ? 1 : var7.method_7947()),
            var7.method_7960() ? 0 : Math.min(var2.method_7611(var3).method_7676(var7), var7.method_7914())
         );
         if (var8 == null) {
            this.c("transfer unavailable before click slot=" + var3);
         } else {
            var5.n = (var6.method_7960() ? var7 : var6).method_46651(var8.slotCount());
            var5.Z = (var7.method_7960() ? var6 : var7).method_46651(var8.cursorCount());
            var5.b = (boolean)1;
            var5.C = var3;
            var5.r = var4;
            var5.F = 0;
            var5.V.Q(var2, var3, this.z2);
            var5.P = var5.D = var5.G = "none";
            this.za = (boolean)1;

            try {
               var1.field_1761.method_2906(var2.field_7763, var3, var4, class_1713.field_7790, var1.field_1724);
            } finally {
               this.za = (boolean)0;
            }

            yq.R("HigherLower Click issued " + c(var5));
            if (ss.h(var5.n, var5.l.get(var3)) || ss.h(var5.Z, var5.s)) {
               this.c("issued pickup did not change both slot and cursor; no replay");
               this.V(var1, var2, var5);
            }
         }
      } else {
         this.c(
            "pre-click local/server mismatch step="
               + var5.g
               + " slot="
               + var3
               + " localSlot="
               + B(var2.method_7611(var3).method_7677())
               + " serverSlot="
               + B(var5.l.get(var3))
               + " localCursor="
               + B(var2.method_34255())
               + " serverCursor="
               + B(var5.s)
         );
         this.V(var1, var2, var5);
      }
   }

   @Override
   public void onInventorySynchronized(int var1, List<class_1799> var2, class_1799 var3) {
      long var4 = ++this.z2;
      class_310 var6 = class_310.method_1551();
      qr.j var7 = this.z9;
      if (var7 != null) {
         var7.P = "full seq=" + var4 + " sync=" + var1 + " size=" + var2.size();
      }

      if (var7 != null && var6.field_1724 != null && var1 == var7.q && var6.field_1724.field_7512 == var7.c && var2.size() == var7.c.field_7761.size()) {
         var7.l.clear();

         for (class_1799 var9 : var2) {
            var7.l.add(var9.method_7972());
         }

         var7.s = var3.method_7972();
         var7.D = var7.G = "full";
         var7.Y = (boolean)1;
         var7.a.v();
         if (var7.b) {
            var7.V.O(var7.c, var4, true, -1, true);
         }
      }

      this.L(var6);
      this.z();
   }

   @Override
   public void onServerSlot(int var1, int var2, class_1799 var3) {
      long var4 = ++this.z2;
      class_310 var6 = class_310.method_1551();
      qr.j var7 = this.z9;
      if (var7 != null) {
         var7.P = "slot seq=" + var4 + " sync=" + var1 + " slot=" + var2 + " " + B(var3);
      }

      if (var7 != null && var7.Y && var6.field_1724 != null && var6.field_1724.field_7512 == var7.c) {
         if (var1 == -1 && var2 == -1) {
            var7.s = var3.method_7972();
            var7.G = "cursor-packet";
            if (var7.b) {
               var7.V.O(var7.c, var4, false, -1, true);
            }
         } else if (var1 == var7.q && var2 >= 0 && var2 < var7.l.size()) {
            var7.l.set(var2, var3.method_7972());
            if (var2 == var7.C) {
               var7.D = "handler-slot";
            }

            if (var7.b) {
               var7.V.O(var7.c, var4, false, var2, false);
            }
         } else if (var1 == 0 && var2 >= 0 && var2 < var6.field_1724.field_7498.field_7761.size()) {
            class_1735 var8 = var6.field_1724.field_7498.method_7611(var2);
            int var9 = var8.field_7871 == var6.field_1724.method_31548() ? f(var7.c, var6, var8.method_34266()) : -1;
            if (var9 >= 0 && var9 < var7.l.size()) {
               var7.l.set(var9, var3.method_7972());
               if (var9 == var7.C) {
                  var7.D = "sync0-slot";
               }

               if (var7.b) {
                  var7.V.O(var7.c, var4, false, var9, false);
               }
            }
         }
      }

      this.L(class_310.method_1551());
      this.z();
   }

   @Override
   public void onPlayerInventorySlot(int var1, class_1799 var2) {
      long var3 = ++this.z2;
      class_310 var5 = class_310.method_1551();
      qr.j var6 = this.z9;
      if (var6 != null) {
         var6.P = "player-inventory seq=" + var3 + " index=" + var1 + " " + B(var2);
      }

      if (var6 != null && var6.Y && var5.field_1724 != null && var5.field_1724.field_7512 == var6.c) {
         int var7 = f(var5.field_1724.field_7512, var5, var1);
         if (var7 >= 0 && var7 < var6.l.size()) {
            var6.l.set(var7, var2.method_7972());
            if (var7 == var6.C) {
               var6.D = "player-inventory";
            }

            if (var6.b) {
               var6.V.O(var6.c, var3, false, var7, false);
            }
         }
      }

      this.L(var5);
      this.z();
   }

   @Override
   public boolean hidesCursor(class_1703 var1) {
      return this.z9 != null && var1.field_7763 == this.z9.q && !this.z9.X.isEmpty();
   }

   @Override
   public boolean blocksUserInteraction() {
      return this.z9 != null && !this.z9.X.isEmpty();
   }

   @Override
   public int clickRevision(class_1703 var1) {
      int var2 = var1.method_37421();
      qr.j var3 = this.z9;
      if (var3 != null && var1 == var3.c && (this.za || var3.K)) {
         char var4 = '耀';
         if (this.za) {
            var3.O = var2;
            var3.R = var4;
         }

         return var4;
      } else {
         return var2;
      }
   }

   @Override
   public boolean blocksClose() {
      qr.j var1 = this.z9;
      class_310 var2 = class_310.method_1551();
      if (var1 == null || var2.field_1724 == null) {
         return false;
      }

      if (var1.K) {
         return false;
      }

      class_1703 var3 = var2.field_1724.field_7512;
      return var3.field_7763 == var1.q && (var1.b || !var1.Y || !var3.method_34255().method_7960() || !var1.s.method_7960());
   }

   @Override
   public boolean allowsRecoveryOpen(class_2338 var1) {
      return this.z9 != null && this.z9.q < 0 && this.z9.d.equals(var1);
   }

   public boolean g(class_2338 var1) {
      return this.z9 == null || this.z9.q < 0 && this.z9.d.equals(var1);
   }

   private void F(class_310 var1, class_1703 var2, qr.j var3) {
      if (!var3.K) {
         class_1799 var4 = var2.method_34255();
         if (var4.method_7960() && var3.s.method_7960()) {
            if (!var3.v.method_7960()) {
               class_1799 var8 = var2.method_7611(var3.w).method_7677();
               if (!var8.method_7960() && !R(var8, var3.v) || var3.v.method_7947() - var8.method_7947() > 1) {
                  this.V(var1, var2, var3);
                  return;
               }
            }

            for (int var9 = 0; var9 < var3.J.length; var9++) {
               class_1799 var10 = var3.J[var9];
               class_1799 var11 = var1.field_1724.method_31548().method_5438(var9);
               if (!var10.method_7960() && (!R(var10, var11) || var11.method_7947() < var10.method_7947())) {
                  this.V(var1, var2, var3);
                  return;
               }
            }

            this.o(var1, false);
         } else if (ss.h(var4, var3.s)) {
            int var5 = this.o(var4) == var3.B ? var3.w : f(var2, var1, var3.o);
            if (var5 >= 0 && R(var2.method_7611(var5), var4, var4.method_7947())) {
               this.t(var1, var2, var5, 0);
            } else {
               for (class_1735 var7 : var2.field_7761) {
                  if (R(var7, var4, var4.method_7947())) {
                     this.t(var1, var2, var7.field_7874, 0);
                     return;
                  }
               }

               this.V(var1, var2, var3);
            }
         }
      }
   }

   private void V(class_310 var1, class_1703 var2, qr.j var3) {
      var3.X.clear();
      var3.b = (boolean)0;
      this.zL = -1;
      if (!var3.K) {
         var3.K = (boolean)1;
         yq.E("HigherLower", "Manual recovery required: clear cursor, restore baseline slots and return any source surplus");
      }

      if (var2 instanceof class_1716 && (var1.field_1755 == null || var1.field_1755 instanceof class_465 var4 && var4.method_17577() != var2)) {
         var1.method_1507(new class_480((class_1716)var2, var1.field_1724.method_31548(), class_2561.method_43470("HigherLower recovery: " + var3.L)));
      }
   }

   private void c(String var1) {
      if (this.z9 != null && !this.z9.m) {
         this.z9.m = (boolean)1;
         this.z9.L = var1;
         yq.E("HigherLower", "Exchange failed: " + var1 + "; parking cursor without replay");
      }
   }

   @Override
   public void abortSwap() {
      this.c("cancelled");
   }

   private void o(class_310 var1, boolean var2) {
      qr.j var3 = this.z9;
      class_1703 var4 = var1.field_1724.field_7512;
      if (var4.method_34255().method_7960()) {
         if (var4 == var3.c) {
            var1.field_1724.method_7346();
         }

         if (var2) {
            yq.X("HigherLower", var3.W, var3.B, this.getTemplateIdForValue(var3.B));
            this.n(var3.B, var3.y);
            this.zF = (boolean)1;
            if (var1.field_1773.field_4012 instanceof l0 var5) {
               var5.larpRig$releaseHeldMap(this, var1.field_1724.method_6047());
            }
         } else {
            this.zV = this.zg = -1;
            this.zH = class_1799.field_8037;
            this.z3 = null;
            this.zL = -1;
            this.zm = -1;
         }

         this.z9 = null;
         this.zA.clear();
         yq.E(
            "HigherLower",
            var2
               ? "One-card exchange confirmed (open=" + (var3.H - var3.E) / 1000000L + "ms, transfer=" + (System.nanoTime() - var3.H) / 1000000L + "ms)"
               : "Exchange stopped: " + var3.L
         );
      }
   }

   private void G(class_310 var1, qr.j var2) {
      if (var1.field_1761 != null && var1.method_1562() != null && !var1.field_1724.method_5715() && !var1.field_1690.field_1904.method_1434()) {
         class_3965 var3 = T(var1, var2.d);
         if (var3 != null) {
            var2.A = (boolean)1;
            this.U(var1, var3, var1x -> var2.a.L(var1x, var2.x));
         }
      }
   }

   @Override
   public void onBlockInteractionAcknowledged(int var1) {
      qr.j var2 = this.z9;
      if (var2 != null && var2.a.V()) {
         boolean var3 = var2.a.d();
         var2.a.L(var1);
         if (!var3 && var2.a.d()) {
            yq.R("HigherLower Dispenser interaction processed sync=" + var2.q + " " + var2.a.E());
         }

         this.z();
      }
   }

   private void z() {
      class_310 var1 = class_310.method_1551();
      if (!this.zw && !this.za && this.z9 != null && var1.field_1724 != null && var1.field_1687 != null) {
         this.zw = (boolean)1;

         try {
            this.G(var1);
         } finally {
            this.zw = (boolean)0;
         }
      }
   }

   @Override
   public boolean isSwapActive() {
      return this.z9 != null;
   }

   @Override
   public boolean isSwapBusy() {
      return this.z9 != null;
   }

   @Override
   public boolean isSwapSuppressing() {
      return this.z9 != null && this.z9.A;
   }

   @Override
   public boolean shouldSuppressOpen(class_3917<?> var1) {
      return var1 == class_3917.field_17328 && this.isSwapSuppressing();
   }

   @Override
   public void onSwapContainerOpened(class_3917<?> var1, int var2) {
      qr.j var3 = this.z9;
      if (var3 != null && var1 == class_3917.field_17328) {
         int var4 = var3.a.V() && !var3.b && var3.g <= 0 ? 0 : 1;
         yq.R("HigherLower Dispenser opened sync=" + var2 + " " + var3.a.E());
         if (var4 != 0) {
            this.c("unsolicited or mid-transfer container replacement");
            var3.b = (boolean)0;
         }

         var3.q = var2;
         var3.H = System.nanoTime();
         var3.Y = (boolean)0;
         var3.c = class_310.method_1551().field_1724.field_7512;
         var3.V.Q(var3.c, -1, this.z2);
         var3.F = 0;
         var3.l.clear();
         var3.s = class_1799.field_8037;
         if (var4 != 0) {
            class_310 var5 = class_310.method_1551();
            this.V(var5, var5.field_1724.field_7512, var3);
         }
      }
   }

   @Override
   public int getSwapSyncId() {
      return this.z9 == null ? -1 : this.z9.q;
   }

   @Override
   public int getSwapValue() {
      return this.z9 == null ? this.zV : this.z9.B;
   }

   @Override
   public int getSwapRealMapId() {
      return this.z9 == null ? -1 : D(this.z9.h);
   }

   @Override
   public boolean isSwapJuggling() {
      return this.z9 != null;
   }

   @Override
   public boolean isSwapFreezing() {
      return this.z9 != null;
   }

   @Override
   public boolean isSwapFreezingPassThrough() {
      return false;
   }

   @Override
   public boolean isDealBobAllowed(class_1799 var1) {
      return this.z9 == null;
   }

   @Override
   public boolean isHotbarBobAllowed(class_1799 var1) {
      return this.z9 == null;
   }

   @Override
   public class_1799 getVisualOverride(class_1799 var1) {
      return null;
   }

   @Override
   public class_1799 getInventorySlotProjection(int var1) {
      class_1799 var2 = il.W(var1);
      if (var2 != null) {
         return var2;
      } else {
         return this.z9 == null ? null : this.z9.X.get(var1);
      }
   }

   @Override
   public class_1799 projectSlot(int var1, class_1799 var2) {
      class_1799 var3 = this.getInventorySlotProjection(var1);
      return var3 == null ? var2 : var3;
   }

   private void q(class_310 var1) {
      int var2 = var1.field_1724.method_31548().method_67532();
      if (var2 != this.zL) {
         this.zB++;
      }

      this.zL = var2;
      this.zF = (boolean)0;
      this.z5 = this.projectSlot(var2, var1.field_1724.method_6047()).method_7972();
   }

   @Override
   public boolean isDealtHandLocked() {
      class_310 var1 = class_310.method_1551();
      if (var1.field_1724 == null || var1.field_1724.method_31548().method_67532() != this.zL || this.zF && !R(var1.field_1724.method_6047(), this.z5)) {
         this.zL = -1;
      }

      return this.zL >= 0;
   }

   @Override
   public class_1799 getDealtHandVisual(class_1799 var1) {
      if (!this.isDealtHandLocked()) {
         return var1;
      } else {
         return this.zF ? var1 : this.projectSlot(this.zL, var1);
      }
   }

   @Override
   public long getDealtHandSequence() {
      return this.zB;
   }

   @Override
   public boolean isDealtHandSettled() {
      return this.zF;
   }

   @Override
   public void signalFramePlacement(class_1533 var1) {
      this.zL = -1;
      if (class_310.method_1551().field_1773.field_4012 instanceof l0 var2) {
         var2.larpRig$releaseHeldMap(this, class_310.method_1551().field_1724.method_6047());
      }
   }

   private int T() {
      if (this.zV >= 1 && this.z6 != qr.z.u) {
         ArrayList var1 = new ArrayList();

         for (int var2 = 1; var2 <= 9; var2++) {
            int var3 = this.z6 == qr.z.a ? (var2 > this.zV ? 1 : 0) : (var2 < this.zV ? 1 : 0);
            if (var3 != 0 && this.getTemplateForValue(var2) != null && this.f(var2) >= 0) {
               var1.add(var2);
            }
         }

         if (!var1.isEmpty()) {
            return (Integer)var1.get(ThreadLocalRandom.current().nextInt(var1.size()));
         } else {
            return (this.z6 == qr.z.q && this.zV == 1 || this.z6 == qr.z.a && this.zV == 9) && this.f(this.zV) >= 0 ? this.zV : -1;
         }
      } else {
         return -1;
      }
   }

   private int o(class_1799 var1) {
      int var2 = D(var1);
      if (var2 < 0) {
         return -1;
      }

      for (int var3 = 1; var3 <= 9; var3++) {
         if (this.getTemplateIdForValue(var3) == var2) {
            return var3;
         }
      }

      return -1;
   }

   private static int D(class_1799 var0) {
      class_9209 var1 = var0.method_31574(class_1802.field_8204) ? (class_9209)var0.method_58694(class_9334.field_49646) : null;
      return var1 == null ? -1 : var1.comp_2315();
   }

   private static boolean R(class_1799 var0, class_1799 var1) {
      return ss.B(var0, var1);
   }

   private static boolean k(class_1799 var0, class_1799 var1) {
      return !var0.method_31574(class_1802.field_8204) && !var1.method_31574(class_1802.field_8204) || ss.h(var0, var1);
   }

   private static String B(class_1799 var0) {
      return "map=" + D(var0) + "/count=" + var0.method_7947() + "/filled=" + var0.method_31574(class_1802.field_8204);
   }

   private static String c(qr.j var0) {
      return "step="
         + var0.g
         + " slot="
         + var0.C
         + " button="
         + var0.r
         + " sync="
         + var0.q
         + " revision="
         + var0.O
         + " probe="
         + var0.R
         + " ack["
         + var0.V.U()
         + "] slotSource="
         + var0.D
         + " cursorSource="
         + var0.G
         + " lastPacket=["
         + var0.P
         + "] expectedSlot="
         + B(var0.n)
         + " receivedSlot="
         + B(var0.l.get(var0.C))
         + " expectedCursor="
         + B(var0.Z)
         + " receivedCursor="
         + B(var0.s)
         + (var0.f < 0 ? "" : " batchSource=" + var0.f + " expectedSource=" + B(var0.p) + " receivedSource=" + B(var0.l.get(var0.f)))
         + " slotIdentity="
         + ss.h(var0.l.get(var0.C), var0.n)
         + " cursorIdentity="
         + ss.h(var0.s, var0.Z)
         + " slotComponentsExact="
         + class_1799.method_31577(var0.l.get(var0.C), var0.n)
         + " cursorComponentsExact="
         + class_1799.method_31577(var0.s, var0.Z);
   }

   private static class_1799[] Z(class_310 var0) {
      class_1799[] var1 = new class_1799[36];

      for (int var2 = 0; var2 < var1.length; var2++) {
         var1[var2] = var0.field_1724.method_31548().method_5438(var2).method_7972();
      }

      return var1;
   }

   private static Map<Integer, Integer> n(class_1799[] var0) {
      HashMap var1 = new HashMap();

      for (class_1799 var5 : var0) {
         if (D(var5) >= 0) {
            var1.merge(D(var5), var5.method_7947(), Integer::sum);
         }
      }

      return var1;
   }

   private static Map<Integer, Integer> W(class_310 var0) {
      Map var1 = n(Z(var0));
      class_1799 var2 = var0.field_1724.field_7512.method_34255();
      if (D(var2) >= 0) {
         var1.merge(D(var2), var2.method_7947(), Integer::sum);
      }

      if (var0.field_1687 != null) {
         for (class_1297 var4 : var0.field_1687.method_18112()) {
            if (var4 instanceof class_1533 var5) {
               class_1799 var6 = var5.method_6940();
               if (D(var6) >= 0) {
                  var1.merge(D(var6), var6.method_7947(), Integer::sum);
               }
            }
         }
      }

      return var1;
   }

   private static int f(class_1703 var0, class_310 var1, int var2) {
      for (class_1735 var4 : var0.field_7761) {
         if (var4.field_7871 == var1.field_1724.method_31548() && var4.method_34266() == var2) {
            return var4.field_7874;
         }
      }

      return -1;
   }

   private static boolean R(class_1735 var0, class_1799 var1, int var2) {
      class_1799 var3 = var0.method_7677();
      return !var1.method_7960()
         && var0.method_7680(var1)
         && (var3.method_7960() || R(var3, var1))
         && var3.method_7947() + var2 <= Math.min(var0.method_7676(var1), var1.method_7914());
   }

   private static final class j {
      final long E = System.nanoTime();
      long H;
      final String Q;
      final class_2338 d;
      final qr.z T;
      final int k;
      final int B;
      final int W;
      final int o;
      final int y;
      final class_1799[] J;
      final class_1799 h;
      final Map<Integer, Integer> e;
      final Map<Integer, class_1799> X = new HashMap<>();
      int q = -1;
      int w = -1;
      int M = -1;
      int g;
      int x;
      int F;
      int C = -1;
      final bo a = new bo();
      boolean A;
      boolean Y;
      boolean b;
      boolean m;
      boolean K;
      String L;
      List<class_1799> l = new ArrayList<>();
      class_1799 s = class_1799.field_8037;
      class_1799 n = class_1799.field_8037;
      class_1799 Z = class_1799.field_8037;
      int f = -1;
      class_1799 p = class_1799.field_8037;
      class_1799 v = class_1799.field_8037;
      class_1799 u = class_1799.field_8037;
      final ee V = new ee();
      class_1703 c;
      int r;
      int O;
      int R;
      String P = "none";
      String D = "none";
      String G = "none";

      j(
         String var1,
         class_2338 var2,
         qr.z var3,
         int var4,
         int var5,
         int var6,
         int var7,
         int var8,
         class_1799[] var9,
         class_1799 var10,
         Map<Integer, Integer> var11
      ) {
         this.Q = var1;
         this.d = var2;
         this.T = var3;
         this.k = var4;
         this.B = var5;
         this.W = var6;
         this.o = var7;
         this.y = var8;
         this.J = var9;
         this.h = var10.method_46651(1);
         this.e = new HashMap<>(var11);
      }
   }

   public enum z {
      u("off"),
      a("higher"),
      q("lower");

      private final String Y;

      z(String var3) {
         this.Y = var3;
      }

      public String B() {
         return this.Y;
      }
   }
}

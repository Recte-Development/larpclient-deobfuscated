package com.larpclient;

import com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59.q5a3f607535c664c2;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.IntConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1533;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_1716;
import net.minecraft.class_1723;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2815;
import net.minecraft.class_310;
import net.minecraft.class_3917;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_408;
import net.minecraft.class_437;
import net.minecraft.class_480;
import net.minecraft.class_490;
import net.minecraft.class_638;
import net.minecraft.class_9209;
import net.minecraft.class_9334;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

@Environment(EnvType.CLIENT)
public class q7 {
   private static final q7 A = new q7();
   private static final String J = "";
   private static final int nc = 660;
   private static final int[] y = new int[]{3, 6, 10, 15, 22, 30, 40, 52, 66, 82, 100, 120, 150, 180};
   private static final int W = 100;
   private static final int j = 1200;
   private static final int X = 6;
   private static final int x = 16;
   private static final int K = 40;
   private final Deque<Integer> n0 = new ArrayDeque<>();
   private final Map<Integer, class_9209> q = new HashMap<>();
   private final Map<Integer, Integer> i = new HashMap<>();
   private final Map<String, Map<Integer, Integer>> p = new HashMap<>();
   private final Map<String, Map<Integer, Integer>> e = new HashMap<>();
   private Map<Integer, Integer> ng = new HashMap<>();
   private final int[] d = new int[36];
   private final int[] C = new int[36];
   private int m = -1;
   private int U = -1;
   private Map<Integer, Integer> b;
   private int[] h;
   private int[] r;
   private boolean z;
   private String G;
   private q7.v Z = q7.v.I;
   private int R = -1;
   private int l = -1;
   private int n1 = -1;
   private int k = -1;
   private int n = -1;
   private int w = -1;
   private int Y;
   private int H;
   private int c;
   private int nj = -1;
   private int a;
   private int E;
   private int F;
   private int s = -1;
   private long B;
   private boolean Q;
   private boolean nW;
   private int nB = -1;
   private class_1799 u;
   private int v;
   private int V = -1;
   private int O;
   private int I;
   private int nd;
   private int n5 = -1;
   private int T = -1;
   private int P = -1;
   private class_1799 D;
   private class_2338 N;
   private qa o;
   private bo M = new bo();
   private int ne = -1;
   private int g = -1;
   private Map<Integer, class_1799> t = Map.of();
   private int S = -1;
   private boolean nx;
   private class_638 nP;
   private long nE = -1L;
   private final Map<Integer, Integer> n3 = new HashMap<>();
   private int f = -20;
   private boolean L;

   protected q7() {
   }

   public static q7 u() {
      return A;
   }

   public static boolean M() {
      return L().isSwapActive();
   }

   public static q7 L() {
      q7 var0 = u();
      qr var1 = qr.h();
      qq var2 = qq.f();
      qc var3 = qc.i();
      ql var4 = ql.Y();
      if (var0.isSwapActive()) {
         return var0;
      } else if (var1.isSwapActive()) {
         return var1;
      } else if (var2.isSwapActive()) {
         return var2;
      } else if (var3.isSwapActive()) {
         return var3;
      } else if (var4.isSwapActive()) {
         return var4;
      } else if (var0.hasQueuedForce()) {
         return var0;
      } else if (var1.hasQueuedForce()) {
         return var1;
      } else if (var2.hasQueuedForce()) {
         return var2;
      } else if (var3.hasQueuedForce()) {
         return var3;
      } else if (var4.hasQueuedForce()) {
         return var4;
      } else if (qr.h().R()) {
         return var1;
      } else if (var0.isDealtHandLocked()) {
         return var0;
      } else if (var1.isDealtHandLocked()) {
         return var1;
      } else if (var2.isDealtHandLocked()) {
         return var2;
      } else if (var3.isDealtHandLocked()) {
         return var3;
      } else if (var4.isDealtHandLocked()) {
         return var4;
      } else if (var0.m()) {
         return var0;
      } else if (var1.m()) {
         return var1;
      } else if (var2.m()) {
         return var2;
      } else if (var3.m()) {
         return var3;
      } else {
         return var4.m() ? var4 : var0;
      }
   }

   private boolean m() {
      return this.Z != q7.v.I || this.n5 >= 0 || this.T >= 1 || this.V >= 0;
   }

   public static q7 B() {
      q7 var0 = L();
      if (var0.isSwapActive()) {
         return var0;
      }

      if (A.o != null) {
         return A;
      }

      qq var1 = qq.f();
      return var1.o != null ? var1 : qr.h();
   }

   private boolean X() {
      return this.Z == q7.v.I && this.n5 < 0 && this.T < 1 && !this.n0.isEmpty();
   }

   public void queueForce(int var1) {
      this.queueForceSequence(var1);
   }

   public void queueForceSequence(int... var1) {
      if (this.isSwapBusy()) {
         if (!this.z) {
            yq.R("SWAP " + this.M() + " queue replacement rejected while busy");
            this.z = (boolean)1;
         }
      } else {
         this.z = (boolean)0;
         this.n0.clear();
         this.l();
         if (var1 != null) {
            int var2 = Math.max(1, this.maxQueuedForces());

            for (int var6 : var1) {
               if (this.n0.size() >= var2) {
                  break;
               }

               if (var6 >= 1 && var6 <= 10 && this.getTemplateForValue(var6) != null) {
                  this.n0.addLast(var6);
               }
            }
         }

         if (this.n0.isEmpty()) {
            this.ng.clear();
         } else {
            this.C();
         }
      }
   }

   private void C() {
      this.ng = this.e();
      class_310 var1 = class_310.method_1551();

      for (int var2 = 0; var2 < 36; var2++) {
         class_1799 var3 = var1.field_1724 == null ? class_1799.field_8037 : var1.field_1724.method_31548().method_5438(var2);
         this.d[var2] = this.A(var3);
         this.C[var2] = var3.method_7947();
      }

      this.m = -1;
   }

   private int d(class_310 var1, int var2) {
      if (this.e().getOrDefault(var2, 0) <= this.ng.getOrDefault(var2, 0)) {
         return -1;
      }

      int[] var3 = new int[36];
      int[] var4 = new int[36];

      for (int var5 = 0; var5 < 36; var5++) {
         class_1799 var6 = var1.field_1724.method_31548().method_5438(var5);
         var3[var5] = this.A(var6);
         var4[var5] = var6.method_7947();
      }

      return com.larpclient.t.dealtSlot(var2, this.d, this.C, var3, var4, this.m);
   }

   public void clearQueue() {
      this.abortSwap();
      this.n0.clear();
      this.ng.clear();
      this.z = (boolean)0;
   }

   public int getPendingValue() {
      Integer var1 = this.n0.peekFirst();
      return var1 == null ? -1 : var1;
   }

   public int getQueuedValueAt(int var1) {
      if (var1 < 0) {
         return -1;
      }

      int var2 = 0;

      for (Integer var4 : this.n0) {
         if (var2++ == var1) {
            return var4;
         }
      }

      return -1;
   }

   public boolean hasQueuedForce() {
      return !this.n0.isEmpty();
   }

   public String pendingLabel() {
      if (this.n0.isEmpty()) {
         return "none";
      }

      StringBuilder var1 = new StringBuilder();

      for (Integer var3 : this.n0) {
         if (!var1.isEmpty()) {
            var1.append(" -> ");
         }

         var1.append(this.m(var3));
      }

      return var1.toString();
   }

   public void tick(class_310 var1) {
      if (var1 != null && var1.field_1724 != null) {
         if (var1.field_1687 != null) {
            if (this.nP != var1.field_1687) {
               this.nP = var1.field_1687;
               this.nE = var1.field_1687.method_75260();
            } else if (var1.field_1687.method_75260() == this.nE) {
               return;
            }
         }

         this.nE = var1.field_1687 == null ? -1L : var1.field_1687.method_75260();
         if (this.F > 0) {
            this.F--;
         }

         if (this.v > 0) {
            this.v--;
         }

         if (this.a > 0) {
            this.a--;
         }

         if (this.Z != q7.v.I) {
            this.v(var1);
         } else {
            if (this.nd > 0 && --this.nd == 0) {
               this.s("post-swap settle");
            }

            if (this.V >= 0) {
               int var2 = this.V;
               int var3 = this.O;
               int var4 = this.I;
               if (this.n0.isEmpty() || this.n0.peekFirst() != var4) {
                  this.H();
               } else if (this.N(var1, var2) < 0) {
                  this.H();
               } else if (this.a <= 0) {
                  if (var3 == var4) {
                     this.G(var3, var3);
                     yq.R("SWAP " + this.M() + " deferred deal already matched " + this.m(var4));
                     this.H();
                     this.l();
                     this.c();
                     return;
                  }

                  if (!this.swapWorthwhile(var3, var4)) {
                     this.G(var3, var3);
                     yq.R("SWAP " + this.M() + " deferred skip real=" + this.m(var3) + " armed=" + this.m(var4));
                     this.H();
                     this.l();
                     this.c();
                     return;
                  }

                  if (this.o(var1, var2, var3)) {
                     return;
                  }
               }

               if (this.V >= 0) {
                  return;
               }
            }

            if (this.Z == q7.v.I && this.n5 >= 0 && this.V < 0 && this.N(var1, this.n5) < 0) {
               this.l();
            }

            if (P(var1.field_1755) && var1.field_1724.field_7512 instanceof class_1716) {
               this.scanCurrentContainerTemplateSlots(var1);
            }

            if (!this.n0.isEmpty()) {
               if (this.u()) {
                  this.k(var1);
               } else if (this.n5 >= 0 && this.Z == q7.v.I && this.V < 0 && this.a <= 0) {
                  this.g(var1);
               }
            }
         }
      } else {
         this.abortSwap();
         this.clearQueue();
         this.nP = null;
         this.nE = -1L;
      }
   }

   private void g(class_310 var1) {
      int var2 = this.n5;
      if (var2 >= 0 && !this.n0.isEmpty()) {
         int var3 = this.C(new class_9209(var2));
         if (var3 < 1) {
            this.l();
         } else {
            int var4 = this.n0.peekFirst();
            yq.R("SWAP " + this.M() + " deal real=" + this.m(var3) + " (visual) map=" + var2 + " pending=" + this.m(var4));
            if (var3 == var4) {
               this.G(var3, var3);
               this.l();
               this.c();
            } else if (!this.swapWorthwhile(var3, var4)) {
               this.G(var3, var3);
               this.l();
               this.c();
            } else if (this.a <= 0 && !P(var1.field_1755)) {
               this.s();
               if (!this.o(var1, var2, var3) && this.N(var1, var2) >= 0) {
                  this.V = var2;
                  this.O = var3;
                  this.I = var4;
                  this.l();
               }
            }
         }
      } else {
         this.l();
      }
   }

   public void setTemplateForValue(int var1, class_9209 var2) {
      if (var1 >= 1 && var1 <= 10) {
         if (var2 == null) {
            this.q.remove(var1);
            this.i.remove(var1);
         } else {
            this.q.put(var1, var2);
            this.i.put(var1, var2.comp_2315());
         }
      }
   }

   public class_9209 getTemplateForValue(int var1) {
      tw var2 = tw.s();
      if (var2.j() && var2.K()) {
         int var3 = var2.P(var1);
         return var3 < 0 ? null : new class_9209(var3);
      } else {
         return var1 >= 1 && var1 <= 10 ? this.q.get(var1) : null;
      }
   }

   public int getTemplateIdForValue(int var1) {
      tw var2 = tw.s();
      return var2.j() && var2.K() ? var2.P(var1) : this.i.getOrDefault(var1, -1);
   }

   public void setTemplateIdForValue(int var1, int var2) {
      if (var1 >= 1 && var1 <= 10) {
         this.setTemplateForValue(var1, var2 < 0 ? null : new class_9209(var2));
      }
   }

   public int countTemplates() {
      return this.q.size();
   }

   public boolean m(class_9209 var1) {
      return this.C(var1) >= 1;
   }

   public void setContainerContext(String var1) {
      if (!Objects.equals(this.G, var1) && this.o != null) {
         this.j(class_310.method_1551(), "dispenser context changed");
      }

      this.G = var1;
      this.ng.clear();
      this.l();
   }

   public String O() {
      return this.G;
   }

   public int f(int var1) {
      return this.S().getOrDefault(var1, -1);
   }

   public boolean scanCurrentContainerTemplateSlots(class_310 var1) {
      if (var1 != null && var1.field_1724 != null && var1.field_1724.field_7512 instanceof class_1716 var2) {
         Map var11 = this.S();
         Map var4 = this.g();
         var11.clear();
         var4.clear();
         byte var5 = 0;

         for (class_1735 var7 : var2.field_7761) {
            if (var7 != null && !(var7.field_7871 instanceof class_1661)) {
               class_1799 var8 = var7.method_7677();
               if (var8 != null && !var8.method_7960() && var8.method_31574(class_1802.field_8204)) {
                  class_9209 var9 = (class_9209)var8.method_57353().method_58694(class_9334.field_49646);
                  int var10 = var9 == null ? -1 : this.C(var9);
                  if (var9 != null) {
                     var4.put(var9.comp_2315(), var7.field_7874);
                  }

                  if (var10 > 0) {
                     var11.put(var10, var7.field_7874);
                     var5 = 1;
                  }
               }
            }
         }

         return (boolean)var5;
      } else {
         return false;
      }
   }

   public int M(String var1, class_1799 var2) {
      if (var2 != null) {
         var2 = il.k(var1, var2);
      }

      return var2 != null && !var2.method_7960() && var2.method_31574(class_1802.field_8204)
         ? this.C((class_9209)var2.method_57353().method_58694(class_9334.field_49646))
         : -1;
   }

   public int c(class_9209 var1) {
      return this.C(var1);
   }

   public int containerIndexForValue(int var1) {
      if (var1 == 1) {
         return 0;
      } else {
         return var1 >= 3 && var1 <= 10 ? var1 - 2 : -1;
      }
   }

   protected int maxQueuedForces() {
      return 10;
   }

   private void G(int var1, int var2) {
      this.i(var1, var2, false);
   }

   private void i(int var1, int var2, boolean var3) {
      yq.x(this.M(), var1, var2, this.getTemplateIdForValue(var2), var3);
      if (this.getClass() == q7.class || this instanceof qq || this instanceof ql) {
         this.n3.merge(this.getTemplateIdForValue(var3 ? var1 : var2), 1, Integer::sum);
      }
   }

   public void S(int var1, int var2) {
      int var3 = this.n3.getOrDefault(var2, 0);
      if (var3 > 0) {
         if (var3 == 1) {
            this.n3.remove(var2);
         } else {
            this.n3.put(var2, var3 - 1);
         }
      } else {
         yq.E(this.M(), yq.R(yq.k(this.M(), var1), Integer.toString(var1), false, false));
      }
   }

   public void U() {
      this.n3.clear();
   }

   protected String M() {
      if (this instanceof qr) {
         return "Higher/Lower";
      } else if (this instanceof qc) {
         return "Odd/Even";
      } else if (this instanceof qq) {
         return "Baccarat";
      } else {
         return this instanceof ql ? "Mexican Jack" : "Blackjack";
      }
   }

   protected String m(int var1) {
      return switch (var1) {
         case 1 -> "A";
         case 10 -> "10";
         default -> String.valueOf(var1);
      };
   }

   public boolean isSwapActive() {
      return this.Z != q7.v.I;
   }

   public boolean isSwapJuggling() {
      return (boolean)(switch (this.Z) {
         case W, w, g, o -> 1;
         default -> 0;
      });
   }

   public boolean isSwapFreezing() {
      if (this.v > 0) {
         return true;
      }

      return (boolean)(switch (this.Z) {
         case w, g, o -> 1;
         default -> 0;
      });
   }

   public boolean isSwapFreezingPassThrough() {
      return this.F > 0;
   }

   public void signalFramePlacement(class_1533 var1) {
      class_310 var2 = class_310.method_1551();
      if (var2.field_1724 != null) {
         class_1799 var3 = var2.field_1724.method_6047();
         if (var3.method_31574(class_1802.field_8204)
            && var1.method_6940().method_7960()
            && this.Z != q7.v.I
            && this.A(var3) == this.getTemplateIdForValue(this.R)) {
            this.s = var1.method_5628();
         }

         this.M();
         this.k();
         this.F = 40;
         fr.V("PLACEMENT frame=" + var1.method_5628() + " map=" + this.A(var3) + " phase=" + this.Z + " vanilla animation");
      }
   }

   private boolean l(class_310 var1, int var2) {
      return this.s >= 0 && var1.field_1687 != null && var1.field_1687.method_8469(this.s) instanceof class_1533 var3 && this.A(var3.method_6940()) == var2;
   }

   public boolean s(class_1799 var1) {
      if (this.isSwapJuggling()) {
         return true;
      } else if (!this.isSwapActive()) {
         return false;
      } else {
         class_310 var2 = class_310.method_1551();
         if (var2.field_1724 != null && this.getInventorySlotProjection(var2.field_1724.method_31548().method_67532()) != null) {
            return true;
         } else {
            return var1 != null && !var1.method_7960() && var1.method_31574(class_1802.field_8204) ? this.getVisualOverride(var1) != null : false;
         }
      }
   }

   public boolean isSwapBusy() {
      return this.isSwapActive() || this.V >= 0;
   }

   public boolean isSwapSuppressing() {
      return this.Z != q7.v.I ? true : this.w >= 0 && !this.n0.isEmpty();
   }

   public boolean isDealBobAllowed(class_1799 var1) {
      return this.J(var1, true);
   }

   public boolean isHotbarBobAllowed(class_1799 var1) {
      return this.J(var1, false);
   }

   private boolean J(class_1799 var1, boolean var2) {
      if (var1 != null && !var1.method_7960() && var1.method_31574(class_1802.field_8204)) {
         class_310 var3 = class_310.method_1551();
         if (var3 != null && var3.field_1724 != null && var3.field_1724.method_6079() == var1) {
            return false;
         }

         if (this.Z != q7.v.I) {
            return false;
         }

         int var4 = this.getTemplateIdForValue(this.R);
         if (var4 >= 0 && this.A(var1) == var4) {
            return false;
         }

         if (this.n0.isEmpty()) {
            return true;
         }

         int var5 = this.n0.peekFirst();
         int var6 = this.getTemplateIdForValue(var5);
         if (var6 >= 0 && this.A(var1) == var6) {
            return false;
         }

         if (var2) {
            this.G();
         }

         return true;
      } else {
         return true;
      }
   }

   private void G() {
      class_310 var1 = class_310.method_1551();
      if (var1.field_1724 != null) {
         if (!this.isDealtHandLocked()) {
            this.Q = (boolean)1;
            this.nB = var1.field_1724.method_31548().method_67532();
            this.u = null;
            this.F = 0;
            this.B++;
         }

         this.nW = (boolean)0;
         if (var1.field_1724.method_6047().method_31574(class_1802.field_8204)) {
            this.getDealtHandVisual(var1.field_1724.method_6047());
         }
      }
   }

   private void M() {
      class_310 var1 = class_310.method_1551();
      if (var1.field_1724 != null && var1.field_1773.field_4012 instanceof l0 var2) {
         var2.larpRig$releaseHeldMap(this, var1.field_1724.method_6047());
      }
   }

   public boolean Q() {
      return this.getClass() == q7.class || this instanceof qq || this instanceof ql || this instanceof qc;
   }

   private void s() {
      this.G();
      class_310 var1 = class_310.method_1551();
      if (this.Q() && var1.field_1724 != null && this.isDealtHandLocked() && var1.field_1773.field_4012 instanceof l0 var2) {
         var2.larpRig$captureHeldMap(this, var1.field_1724.method_6047(), var1.field_1724.method_31548().method_67532());
      }
   }

   private void k() {
      this.Q = (boolean)0;
      this.nW = (boolean)0;
      this.nB = -1;
      this.u = null;
   }

   public boolean isDealtHandLocked() {
      class_310 var1 = class_310.method_1551();
      if (this.Q && (var1.field_1724 == null || var1.field_1724.method_31548().method_67532() != this.nB)) {
         this.k();
      }

      return this.Q;
   }

   public class_1799 getDealtHandVisual(class_1799 var1) {
      if (!this.isDealtHandLocked()) {
         return var1;
      }

      class_1799 var2 = this.getInventorySlotProjection(this.nB);
      if (var2 != null) {
         this.u = var2;
         return var2;
      }

      if (!this.isDealtHandLocked()) {
         return var1;
      }

      if (this.nW) {
         if (!var1.method_31574(class_1802.field_8204) || this.u == null || this.A(var1) != this.A(this.u)) {
            this.k();
         }

         return var1;
      } else {
         if (var1.method_31574(class_1802.field_8204)) {
            class_1799 var3 = this.getVisualOverride(var1);
            class_1799 var4 = var3 == null ? var1 : var3;
            if (this.u == null || this.A(this.u) != this.A(var4)) {
               this.u = var4.method_7972();
            }
         } else if (this.Z == q7.v.I && this.v == 0 && this.T < 1 && this.V < 0) {
            this.k();
            return var1;
         }

         return this.u == null ? var1 : this.u;
      }
   }

   public long getDealtHandSequence() {
      return this.B;
   }

   public boolean isDealtHandSettled() {
      return this.nW;
   }

   public int getSwapValue() {
      return this.R;
   }

   public int getSwapRealMapId() {
      return this.n1;
   }

   public int e() {
      return this.V;
   }

   public class_1799 getVisualOverride(class_1799 var1) {
      if (this.getClass() == q7.class || this instanceof qq || this instanceof ql || this instanceof qc) {
         return null;
      }

      if (var1 != null && !var1.method_7960() && var1.method_31574(class_1802.field_8204)) {
         class_310 var2 = class_310.method_1551();
         if (var2 != null && var2.field_1724 != null && var2.field_1724.method_6079() == var1) {
            return null;
         }

         int var4 = -1;
         int var3;
         if (this.Z != q7.v.I) {
            var3 = this.getTemplateIdForValue(this.R);
            var4 = this.l;
         } else {
            if (this.T < 1) {
               return null;
            }

            var3 = this.getTemplateIdForValue(this.T);
         }

         if (var3 < 0) {
            return null;
         } else {
            int var5 = this.A(var1);
            if (var5 == var3) {
               return null;
            } else if (this.P >= 0 && var2 != null && var2.field_1724 != null && var2.field_1724.method_31548().method_5438(this.P) == var1) {
               return this.h(var1, var3);
            } else if (this.n5 >= 0 && var5 == this.n5) {
               return this.h(var1, var3);
            } else {
               return var4 >= 1 && this.C(new class_9209(var5)) == var4 ? this.h(var1, var3) : null;
            }
         }
      } else {
         return null;
      }
   }

   private class_1799 h(class_1799 var1, int var2) {
      if (this.D != null && this.A(this.D) == var2) {
         return this.D;
      }

      class_1799 var3 = var1.method_7972();
      var3.method_57379(class_9334.field_49646, new class_9209(var2));
      this.D = var3;
      return var3;
   }

   public class_1799 getInventorySlotProjection(int var1) {
      class_1799 var2 = il.W(var1);
      if (var2 != null) {
         return var2;
      }

      class_310 var3 = class_310.method_1551();
      if (var3.field_1724 == null) {
         return null;
      }

      if (!this.isSwapActive()) {
         if (var1 == this.P && this.T > 0) {
            class_1799 var4 = var3.field_1724.method_31548().method_5438(var1);
            int var5 = this.getTemplateIdForValue(this.T);
            if (this.A(var4) == this.n5 && var5 >= 0) {
               return this.h(var4, var5);
            }
         }

         return null;
      } else {
         if (this.isSwapJuggling()
            || this.U >= 0
               && this.A(var3.field_1724.method_31548().method_5438(this.U)) == this.n1
               && var3.field_1724.method_31548().method_5438(this.U).method_7947() >= this.r[this.U]
               && (
                  this.S < 0
                     || this.S == this.U
                     || this.A(var3.field_1724.method_31548().method_5438(this.S)) == this.h[this.S]
                        && var3.field_1724.method_31548().method_5438(this.S).method_7947() == this.r[this.S]
               )) {
            return this.t.get(var1);
         }

         this.t = Map.of();
         this.k();
         return null;
      }
   }

   public class_1799 projectSlot(int var1, class_1799 var2) {
      class_1799 var3 = this.getInventorySlotProjection(var1);
      return var3 == null ? var2 : var3;
   }

   public void I(int var1) {
      if (this.Q() && this.isDealtHandLocked() && var1 == this.nB) {
         this.k();
      }

      if (this.t.containsKey(var1) && this.isSwapActive()) {
         this.j(class_310.method_1551(), "projected card manually dropped; no replay");
      }
   }

   public boolean shouldSuppressOpen(class_3917<?> var1) {
      if (this.F()) {
         this.o = null;
         this.w = -1;
         return false;
      } else {
         return var1 != null && var1 == class_3917.field_17328 && this.isSwapSuppressing();
      }
   }

   public void onSwapContainerOpened(class_3917<?> var1, int var2) {
      if (var2 >= 0) {
         if (var1 == null || var1 == class_3917.field_17328) {
            if (this.isSwapSuppressing()) {
               if (this.o != null && this.o.y) {
                  this.j(class_310.method_1551(), "container replaced; no replay");
               } else {
                  class_310 var3 = class_310.method_1551();
                  this.o = var3.field_1724 == null ? null : new qa(var3.field_1724.field_7512);
                  q7.v var4 = this.Z;
                  this.w = var2;
                  fr.V("OPEN arrived syncId=" + var2 + " phase=" + var4 + " clicksSinceOpen=0");
                  yq.R("SWAP " + this.M() + " container opened syncId=" + var2);
               }
            }
         }
      }
   }

   public void abortSwap() {
      this.k();
      if (this.o != null && this.o.y && this.isSwapActive()) {
         this.j(class_310.method_1551(), "cancelled after transfer; no replay");
      } else if (this.Z == q7.v.I) {
         if (this.w >= 0) {
            this.z(class_310.method_1551());
         }

         this.l();
         this.v = 0;
      } else if (this.m(class_310.method_1551())) {
         this.j(3, "abort");
      } else {
         this.z(class_310.method_1551());
         this.Z = q7.v.I;
         this.l();
         this.v = 0;
         yq.R("SWAP " + this.M() + " aborted");
      }
   }

   protected boolean supportsPhysicalSwap() {
      return this.getClass() == q7.class;
   }

   protected class_2338 getAssignedDispenserPosition(class_310 var1) {
      if (this instanceof qq) {
         class_2338 var2 = hu.f().j(this.O());
         return var2 != null ? var2 : hu.f().F(hu.q.q);
      } else {
         return this instanceof qr ? hu.f().F(hu.q.R) : hu.f().F(hu.q.G);
      }
   }

   private void k(class_310 var1) {
      ArrayList var2 = new ArrayList();
      Map var3 = this.e();

      for (Entry var5 : var3.entrySet()) {
         if ((Integer)var5.getValue() > this.ng.getOrDefault(var5.getKey(), 0)) {
            var2.add((Integer)var5.getKey());
         }
      }

      if (!var2.isEmpty()) {
         if (this.n0.isEmpty()) {
            yq.R("SWAP " + this.M() + " rise ignored (no plan armed) increased=" + var2 + " queue=" + this.pendingLabel());
         } else {
            int var7 = this.n0.peekFirst();
            int var8;
            if (var2.size() == 1) {
               var8 = (Integer)var2.get(0);
            } else {
               if (this.n5 < 0 || !var2.contains(this.n5)) {
                  this.C();
                  return;
               }

               var8 = this.n5;
            }

            int var6 = this.C(new class_9209(var8));
            if (var6 < 1) {
               this.C();
            } else {
               yq.R(
                  "SWAP "
                     + this.M()
                     + " deal real="
                     + this.m(var6)
                     + " (map "
                     + var8
                     + (this.n5 == var8 ? ", prearmed" : ", iterated")
                     + ") increased="
                     + var2
                     + " pending="
                     + this.m(var7)
               );
               if (var6 == var7) {
                  this.G(var6, var6);
                  this.l();
                  this.c();
               } else if (!this.swapWorthwhile(var6, var7)) {
                  this.G(var6, var6);
                  yq.R("SWAP " + this.M() + " skip (dealer already wins) real=" + this.m(var6) + " armed=" + this.m(var7) + " queue=" + this.pendingLabel());
                  this.l();
                  this.c();
               } else {
                  this.n5 = var8;
                  this.T = var7;
                  this.s();
                  if (!this.o(var1, var8, var6)) {
                     if (this.N(var1, var8) >= 0) {
                        this.V = var8;
                        this.O = var6;
                        this.I = var7;
                        this.l();
                     } else {
                        this.l();
                        this.c();
                     }
                  }
               }
            }
         }
      }
   }

   protected boolean swapWorthwhile(int var1, int var2) {
      return l4.K().K(var1, var2);
   }

   private boolean o(class_310 var1, int var2, int var3) {
      if (var1 == null || var1.field_1724 == null || var1.field_1687 == null) {
         return false;
      }

      if (!this.supportsPhysicalSwap()) {
         return false;
      }

      q7 var4 = L();
      if (var4 != this && var4.isSwapActive()) {
         return false;
      }

      if (this.n0.isEmpty()) {
         return false;
      }

      int var5 = this.n0.peekFirst();
      if (var5 < 1 || var5 > 10 || var5 == var3) {
         return false;
      }

      if (this.getTemplateForValue(var5) == null || var3 < 1) {
         return false;
      }

      if (!this.swapWorthwhile(var3, var5)) {
         return false;
      }

      int var6 = this.d(var1, var2);
      if (il.K(var1) && var6 >= 0 && il.S(this, var6, var5)) {
         this.D(var3, var5);
         return true;
      }

      if (this.a > 0) {
         return false;
      }

      if (P(var1.field_1755)) {
         return false;
      }

      if (!var1.field_1724.field_7512.method_34255().method_7960() || !var1.field_1724.field_7498.method_34255().method_7960()) {
         return false;
      }

      if (this.N(var1, var2) < 0) {
         return false;
      }

      int var7 = this.d(var1, var2);
      if (var7 < 0) {
         return false;
      }

      this.U = var7;
      class_2338 var8 = this.getAssignedDispenserPosition(var1);
      if (var8 == null) {
         return false;
      }

      this.N = var8.method_10062();
      this.s = -1;
      this.n1 = var2;
      this.l = var3;
      this.R = var5;
      this.b = this.e();
      this.b.merge(var2, -1, Integer::sum);
      this.b.merge(this.getTemplateIdForValue(var5), 1, Integer::sum);
      this.h = new int[36];
      this.r = new int[36];

      for (int var9 = 0; var9 < 36; var9++) {
         class_1799 var10 = var1.field_1724.method_31548().method_5438(var9);
         this.h[var9] = this.A(var10);
         this.r[var9] = var10.method_7947();
      }

      this.z = (boolean)0;
      class_1716 var12 = this.i(var1);
      if (var12 != null && this.o != null && this.o.j == var12) {
         this.o = this.o.Z();
      } else {
         this.o = null;
      }

      this.M = new bo();
      this.w = var12 != null ? var12.field_7763 : -1;
      fr.V("BEGIN swap real=" + this.m(var3) + " ->" + this.m(var5) + " retainedSyncId=" + this.w + " cooldown=" + this.a + " phase=" + this.Z);
      this.k = -1;
      this.n = -1;
      this.nj = -1;
      this.E = 0;
      this.c = 0;
      this.a = 0;
      this.H();
      this.s();
      this.S = ek.m(var7, this.r, var3x -> {
         class_1799 var4x = var1.field_1724.method_31548().method_5438(var3x);
         return this.A(var4x) == this.getTemplateIdForValue(var5) && var4x.method_7947() < var4x.method_7914();
      });
      class_1799 var13 = var1.field_1724.method_31548().method_5438(var7);
      class_1799 var11 = var13.method_46651(1);
      var11.method_57379(class_9334.field_49646, this.getTemplateForValue(var5));
      this.t = ek.L(
         var7,
         this.S,
         this.r[var7],
         this.S >= 0 && this.S != var7 ? this.r[this.S] : 0,
         var13,
         this.S >= 0 && this.r[this.S] > 0 && this.S != var7 ? var1.field_1724.method_31548().method_5438(this.S) : var11,
         class_1799::method_46651
      );
      this.Y(q7.v.V);
      this.H = 0;
      this.f = -20;
      yq.R("SWAP BEGIN " + this.M() + " real=" + this.m(var3) + " ->" + this.m(var5));
      this.s("swap begin");
      this.v(var1);
      return true;
   }

   private void D(int var1, int var2) {
      this.i(var1, var2, true);
      this.H();
      this.l();
      this.M();
      this.k();
      this.c();
      yq.R("VISUAL " + this.M() + " real=" + var1 + " shown=" + var2);
   }

   public void onInventorySynchronized(int var1, List<class_1799> var2, class_1799 var3) {
      if (this.o != null && this.isSwapActive()) {
         this.o.k("receive full sync=" + var1 + " size=" + var2.size() + " cursorCount=" + var3.method_7947());
      }

      class_310 var4 = class_310.method_1551();
      if (this.o != null && var4.field_1724 != null && var4.field_1724.field_7512 == this.o.j && var1 == this.o.j.field_7763) {
         this.o.b(var2, var3);
         if (this.o.V) {
            this.M.v();
         }

         this.i();
      }
   }

   public void onServerSlot(int var1, int var2, class_1799 var3) {
      if (this.o != null && this.isSwapActive()) {
         this.o.k("receive slot sync=" + var1 + " slot=" + var2 + " map=" + this.A(var3) + " count=" + var3.method_7947());
      }

      class_310 var4 = class_310.method_1551();
      if (this.o != null && var4.field_1724 != null && var4.field_1724.field_7512 == this.o.j) {
         if (var1 == -1 && var2 == -1) {
            this.o.n(-1, var3);
         } else if (var1 == this.o.j.field_7763) {
            this.o.n(var2, var3);
         } else if (var1 == 0 && var2 >= 0 && var2 < var4.field_1724.field_7498.field_7761.size()) {
            class_1735 var5 = var4.field_1724.field_7498.method_7611(var2);
            if (var5.field_7871 == var4.field_1724.method_31548()) {
               this.onPlayerInventorySlot(var5.method_34266(), var3);
            }
         }

         this.i();
      }
   }

   public void onPlayerInventorySlot(int var1, class_1799 var2) {
      if (this.o != null && this.isSwapActive()) {
         this.o.k("receive player slot=" + var1 + " map=" + this.A(var2) + " count=" + var2.method_7947());
      }

      class_310 var3 = class_310.method_1551();
      if (this.o != null && var3.field_1724 != null && var3.field_1724.field_7512 == this.o.j) {
         for (class_1735 var5 : this.o.j.field_7761) {
            if (var5.field_7871 == var3.field_1724.method_31548() && var5.method_34266() == var1) {
               this.o.n(var5.field_7874, var2);
               this.i();
               return;
            }
         }
      }
   }

   public int clickRevision(class_1703 var1) {
      return this.o != null && var1 == this.o.j && this.o.i ? 32768 : var1.method_37421();
   }

   public void J(class_2338 var1, String var2) {
      class_310 var3 = class_310.method_1551();
      if (var3.field_1724 != null && var3.field_1687 != null && var1.equals(this.getAssignedDispenserPosition(var3))) {
         fr.V(
            var2
               + " "
               + this.q(var3)
               + " yaw="
               + var3.field_1724.method_36454()
               + " pitch="
               + var3.field_1724.method_36455()
               + " main="
               + var3.field_1724.method_6047()
               + " off="
               + var3.field_1724.method_6079()
         );
      }
   }

   public void onBlockInteractionAcknowledged(int var1) {
      if (this.Z == q7.v.G) {
         fr.V("OPEN ACK seq=" + var1 + " " + this.M.E());
      }

      this.M.L(var1);
      this.i();
   }

   private void i() {
      class_310 var1 = class_310.method_1551();
      if (!this.nx && this.o != null && !this.o.l && this.isSwapActive() && var1.field_1724 != null) {
         q7.v var2 = this.Z;
         this.nx = (boolean)1;

         try {
            if (this.Z == q7.v.G && this.w >= 0) {
               this.Y(q7.v.W);
            }

            if (this.isSwapJuggling()) {
               this.V(var1);
            }
         } finally {
            this.nx = (boolean)0;
            if (this.Z != var2) {
               fr.V("PACKET continuation " + this.M() + " " + var2 + " -> " + this.Z + " syncId=" + this.w);
            }
         }
      }
   }

   public boolean r() {
      return this.L;
   }

   public boolean blocksUserInteraction() {
      return this.isSwapActive() && (this.o == null || !this.o.l);
   }

   public boolean allowsRecoveryOpen(class_2338 var1) {
      return this.Z == q7.v.G && this.w < 0 && this.o == null && var1 != null && var1.equals(this.N);
   }

   public boolean blocksClose() {
      return this.blocksUserInteraction();
   }

   public boolean Y(class_1703 var1) {
      return var1 instanceof class_1723 && (this.isSwapActive() || this.o != null);
   }

   boolean F() {
      return !this.isSwapActive() && this.o != null && this.o.K();
   }

   public void t(class_1703 var1) {
      if (this.Y(var1) && this.F()) {
         this.z(class_310.method_1551());
      }
   }

   public boolean hidesCursor(class_1703 var1) {
      return this.isSwapJuggling() && !this.t.isEmpty() && (this.Y(var1) || this.o != null && var1 == this.o.j);
   }

   private String q(class_310 var1) {
      return "phase="
         + this.Z
         + " distance="
         + this.B(var1)
         + (
            var1.field_1724 == null
               ? " player=null"
               : " liveSync="
                  + var1.field_1724.field_7512.field_7763
                  + " pos="
                  + var1.field_1724.method_73189()
                  + " velocity="
                  + var1.field_1724.method_18798()
                  + " selected="
                  + var1.field_1724.method_31548().method_67532()
                  + " forward="
                  + var1.field_1690.field_1894.method_1434()
                  + " back="
                  + var1.field_1690.field_1881.method_1434()
         )
         + " screen="
         + (var1.field_1755 == null ? "none" : var1.field_1755.getClass().getSimpleName());
   }

   public void P(int var1, String var2) {
      if (this.o != null && this.isSwapActive() && this.o.j.field_7763 == var1) {
         this.o.k(var2);
      }
   }

   public void L(int var1) {
      if (this.isSwapActive()) {
         fr.V("SERVER CLOSE " + this.M() + " sync=" + var1 + " " + this.q(class_310.method_1551()));
      }
   }

   private void j(class_310 var1, String var2) {
      fr.V("BATCH RECOVERY " + this.M() + " reason=" + var2 + " " + this.q(var1) + " " + (this.o == null ? "batch=null" : this.o.r()));
      yq.E(this.M(), "Manual recovery required: " + var2 + " [phase=" + this.Z + "; details: swap-open.log]");
      class_1703 var3 = this.o == null ? null : this.o.j;
      this.o = null;
      this.w = -1;
      this.Z = q7.v.I;
      this.n0.clear();
      this.ng.clear();
      this.H();
      this.l();
      this.M();
      this.k();
      if (var1.field_1724 != null && var1.field_1724.field_7512 instanceof class_1716 var4 && (var4 == var3 || var1.field_1755 == null)) {
         var1.method_1507(new class_480(var4, var1.field_1724.method_31548(), class_2561.method_43470(this.M() + " recovery: " + var2)));
      }
   }

   private void V(class_310 var1) {
      qa var2 = this.o;
      if (var2 == null || var1.field_1724.field_7512 != var2.j || P(var1.field_1755)) {
         this.j(var1, "handler lost; no replay");
      } else if (!var2.V) {
         if (!this.nx && ++this.Y > 100) {
            this.j(var1, "initial authoritative inventory missing");
         }
      } else if (this.M.V() && !this.M.d()) {
         if (!this.nx && ++this.Y > 100) {
            this.j(var1, "open acknowledgement missing");
         }
      } else if (!var2.I(!this.nx)) {
         if (var2.w != null) {
            this.j(var1, var2.w);
         }
      } else if (!var2.y && il.K(var1)) {
         if (var2.K() && il.S(this, this.U, this.R)) {
            this.z(var1);
            this.Z = q7.v.I;
            this.D(this.l, this.R);
         }
      } else {
         class_1716 var3 = (class_1716)var2.j;
         if (this.Z == q7.v.W) {
            this.ne = -1;
            this.g = -1;

            for (class_1735 var5 : var3.field_7761) {
               if (var5.field_7871 == var1.field_1724.method_31548() && var5.method_34266() == this.U && this.A(var5.method_7677()) == this.n1) {
                  this.ne = var5.field_7874;
               }
            }

            if (!var2.d()) {
               this.v(var1, var2, "initial local/server ledger or cursor mismatch", "");
               return;
            }

            if (this.ne < 0) {
               if (var2.K()) {
                  this.S(false, "draw moved before transfer");
               } else {
                  this.v(var1, var2, "real arrival unavailable", "");
               }

               return;
            }

            if (var3.method_7611(this.ne).method_7677().method_7947() < this.r[this.U]) {
               this.v(var1, var2, "original arrival quantity changed before transfer", "");
               return;
            }

            ArrayList var25 = new ArrayList();
            ArrayList var26 = new ArrayList();
            ArrayList var6 = new ArrayList();
            int var7 = this.getTemplateIdForValue(this.R);
            int var8 = var2.Y.get(this.ne).method_7947() == 1 ? 1 : 0;

            for (class_1735 var10 : var3.field_7761) {
               int var11 = var10.method_34266();
               if (var10.field_7871 == var1.field_1724.method_31548()
                  && var11 >= 0
                  && var11 < 36
                  && var11 != this.U
                  && this.h[var11] == var7
                  && this.r[var11] > 0
                  && this.A(var10.method_7677()) == var7
                  && var10.method_7677().method_7947() == this.r[var11]) {
                  var26.add(var10.field_7874);
               }
            }

            if (var8 != 0) {
               var26.add(this.ne);
            }

            int var27 = var26.size();

            for (class_1735 var31 : var3.field_7761) {
               if (!(var31.field_7871 instanceof class_1661)) {
                  if (var7 >= 0 && this.A(var31.method_7677()) == var7) {
                     var25.add(var31.field_7874);
                  }

                  if (!var31.method_7677().method_7960() && this.A(var31.method_7677()) == this.n1) {
                     var6.add(var31.field_7874);
                  }
               } else if (var8 == 0
                  && var31.field_7871 == var1.field_1724.method_31548()
                  && var31.method_34266() >= 0
                  && var31.method_34266() < 36
                  && this.r[var31.method_34266()] == 0
                  && var31.method_7677().method_7960()
                  && var2.Y.get(var31.field_7874).method_7960()) {
                  var26.add(var31.field_7874);
               }
            }

            var26.subList(var27, var26.size()).sort(Comparator.comparingInt(var1x -> var3.method_7611(var1x).method_34266()));

            for (class_1735 var32 : var3.field_7761) {
               if (!(var32.field_7871 instanceof class_1661) && var32.method_7677().method_7960() && var2.Y.get(var32.field_7874).method_7960()) {
                  var6.add(var32.field_7874);
               }
            }

            var6.removeIf(var2x -> !this.allowsReturnSlot(var3.method_7611(var2x).method_34266(), this.l));
            iz.n var30 = iz.X(
               var25,
               var26,
               var6,
               (var3x, var4) -> var2.u(var1, var3x, var4, var4 == this.ne, true).batch() != null,
               var3x -> var2.h(var1, this.ne, var3x) != null
            );
            if (var30 == null) {
               String var33 = var25.isEmpty()
                  ? "armed source unavailable"
                  : (
                     var26.isEmpty()
                        ? "player destination unavailable: no truly empty baseline slot for duplicate arrival"
                        : (var6.isEmpty() ? "real return unavailable: no real-map or empty container slot" : null)
                  );
               StringBuilder var12 = new StringBuilder("sources=" + var25 + " destinations=" + var26 + " eligibleReturns=" + var6 + " afterRemoval=" + var8);
               byte var13 = 0;
               String var14 = null;
               String var15 = null;

               for (int var17 : var25) {
                  int var18 = -1;
                  String var19 = null;
                  LinkedHashMap var20 = new LinkedHashMap();

                  for (int var22 : var26) {
                     qa.i var23 = var2.u(var1, var17, var22, var22 == this.ne, true);
                     if (var23.batch() != null) {
                        var18 = var22;
                        break;
                     }

                     var20.merge(var23.reason(), 1, Integer::sum);
                     if (var14 == null) {
                        var14 = var23.reason();
                     }

                     if (var19 == null) {
                        var19 = "destination=" + var22 + " " + var23.reason() + " " + var2.Z(var17, var22);
                     }
                  }

                  var12.append("\nsource=")
                     .append(var17)
                     .append(" delivery=")
                     .append(var18)
                     .append(" firstReject=")
                     .append(var19)
                     .append(" rejectionCounts=")
                     .append(var20);
                  var13 |= var18 >= 0 ? 1 : 0;

                  for (class_1735 var35 : var3.field_7761) {
                     if (!(var35.field_7871 instanceof class_1661)) {
                        qa.i var36 = var2.u(var1, this.ne, var35.field_7874, false, true);
                        String var24 = var35.field_7874 == var17
                           ? "excluded chosen source"
                           : (!var6.contains(var35.field_7874) ? "ineligible return (not real-map/empty); planner=" + var36.reason() : var36.reason());
                        var12.append("\nsource=")
                           .append(var17)
                           .append(" return=")
                           .append(var35.field_7874)
                           .append(" returnPlan=")
                           .append(var24)
                           .append(" ")
                           .append(var2.Z(this.ne, var35.field_7874));
                        if (var15 == null && var18 >= 0 && var35.field_7874 != var17 && var6.contains(var35.field_7874)) {
                           var15 = var24;
                        }
                     }
                  }
               }

               if (var33 == null) {
                  var33 = var13 != 0
                     ? "real return unavailable: " + (var15 == null ? "only chosen source eligible" : var15)
                     : "armed delivery invalid: " + var14;
               }

               this.v(var1, var2, var33, var12.toString());
               return;
            }

            this.k = var30.source();
            this.g = var30.destination();
            this.nj = var30.returnSlot();
            this.n = var3.method_7611(this.g).method_34266();
            this.S = this.n;
            this.t = ek.L(
               this.U,
               this.S,
               this.r[this.U],
               this.S == this.U ? 0 : this.r[this.S],
               var2.Y.get(this.ne),
               this.S != this.U && !var2.Y.get(this.g).method_7960() ? var2.Y.get(this.g) : var2.Y.get(this.k),
               class_1799::method_46651
            );
            this.Y(q7.v.w);
            var2.T(var1, this.ne, this.nj, this.M() + " RETURN_REAL", true);
         } else if (this.Z == q7.v.w) {
            this.Y(q7.v.g);
            var2.T(var1, this.k, this.g, this.M() + " DELIVER_ARMED", true);
         } else {
            if (!var2.d()) {
               this.j(var1, "final quantity ledger changed");
               return;
            }

            this.G(true, "two one-card batches confirmed");
         }

         if (var2.w != null) {
            this.j(var1, var2.w);
         }
      }
   }

   private void v(class_310 var1, qa var2, String var3, String var4) {
      StringBuilder var5 = new StringBuilder(
         "SWAP "
            + this.M()
            + " PREFLIGHT REJECT sync="
            + var2.j.field_7763
            + " revision="
            + var2.j.method_37421()
            + " realMap="
            + this.n1
            + " armedMap="
            + this.getTemplateIdForValue(this.R)
            + " arrival="
            + this.ne
            + " arrivalInv="
            + this.U
            + " source="
            + this.k
            + " destination="
            + this.g
            + " cursorSafe="
            + var2.K()
            + " "
            + var2.w(-1)
            + " reason="
            + var3
            + "\n"
            + var4
      );

      for (class_1735 var7 : var2.j.field_7761) {
         var5.append("\n").append(var2.w(var7.field_7874));
         if (var7.field_7871 == var1.field_1724.method_31548() && var7.method_34266() >= 0 && var7.method_34266() < 36) {
            var5.append(" inv=")
               .append(var7.method_34266())
               .append(" drawBaselineMap=")
               .append(this.h[var7.method_34266()])
               .append(" drawBaselineCount=")
               .append(this.r[var7.method_34266()]);
         }
      }

      yq.R(var5.toString());
      this.j(var1, var3);
   }

   private void v(class_310 var1) {
      if (this.Z != q7.v.W && this.Z != q7.v.w && this.Z != q7.v.g && this.Z != q7.v.o) {
         this.H++;
         if (this.H > 1200) {
            this.S(false, "whole-swap timeout");
         } else {
            switch (this.Z) {
               case V:
                  this.Y++;
                  if (this.w >= 0) {
                     if (this.i(var1) != null) {
                        fr.V("REQUEST retained harness live syncId=" + this.w + " tick=" + this.Y);
                        this.Y(q7.v.W);
                        this.V(var1);
                     } else if (var1.field_1724 != null && var1.field_1724.field_7512 instanceof class_1716 var2) {
                        this.w = var2.field_7763;
                        fr.V("REQUEST adopting live open syncId=" + var2.field_7763 + " tick=" + this.Y + " screenOpen=" + (var1.field_1755 != null));
                        yq.R("SWAP " + this.M() + " retained harness vanished - adopting live open syncId=" + var2.field_7763);
                        this.Y(q7.v.G);
                     } else if (!P(var1.field_1755)) {
                        this.w = -1;
                        fr.V("REQUEST retained harness gone - reopening tick=" + this.Y);
                        yq.R("SWAP " + this.M() + " retained harness vanished - reopening");
                        this.a(var1);
                     } else {
                        fr.V(
                           "REQUEST retained gone but screen open tick="
                              + this.Y
                              + " screen="
                              + (var1.field_1755 == null ? "null" : var1.field_1755.getClass().getSimpleName())
                              + " handler="
                              + (var1.field_1724.field_7512 == null ? "null" : var1.field_1724.field_7512.getClass().getSimpleName())
                        );
                     }
                  } else if (this.Y >= 1) {
                     fr.V("REQUEST fresh - compact requestContainerOpen tick=" + this.Y);
                     this.a(var1);
                  }
                  break;
               case G:
                  this.Y++;
                  if (this.w >= 0) {
                     fr.V("WAIT_OPEN open arrived tick=" + this.Y + " syncId=" + this.w);
                     yq.R("SWAP " + this.M() + " open-arrived tick=" + this.Y + " syncId=" + this.w);
                     this.Y(q7.v.W);
                     this.V(var1);
                  } else if (this.Y >= 100 && this.o == null) {
                     this.S(false, "dispenser did not open; no cards moved");
                  } else if (this.Y >= 660) {
                     fr.V(
                        "WAIT_OPEN open timeout tick=" + this.Y + " screen=" + (var1.field_1755 == null ? "null" : var1.field_1755.getClass().getSimpleName())
                     );
                     this.N("open timeout");
                  } else if (this.M.I(this.H)) {
                     this.Z(var1);
                  }
                  break;
               default:
                  this.Z = q7.v.I;
            }
         }
      } else {
         this.V(var1);
      }
   }

   private void a(class_310 var1) {
      if (this.N == null) {
         this.S(false, "dispenser unknown");
      } else {
         this.Z(var1);
         this.Y(q7.v.G);
      }
   }

   private void Z(class_310 var1) {
      if (this.M.I(this.H) && this.H - this.f >= 4) {
         if (this.N != null && var1 != null && var1.field_1724 != null && var1.field_1761 != null && var1.method_1562() != null && var1.field_1687 != null) {
            class_3965 var2 = this.H(var1);
            if (var2 == null) {
               fr.V("open blocked: no visible dispenser face pos=" + this.N);
            } else {
               boolean var3 = var1.field_1724.method_5715();
               double var4 = this.B(var1);
               fr.V(
                  "open-issue phase="
                     + this.Z
                     + " tick="
                     + this.Y
                     + " sneaking="
                     + var3
                     + " dist="
                     + String.format("%.1f", var4)
                     + " syncId="
                     + this.w
                     + " pass="
                     + this.c
               );
               yq.R(
                  "SWAP "
                     + this.M()
                     + " open-issue phase="
                     + this.Z
                     + " tick="
                     + this.Y
                     + " sneaking="
                     + var3
                     + " dist="
                     + String.format("%.1f", var4)
                     + " syncId="
                     + this.w
               );
               if (!var1.field_1724.method_5715() && !var1.field_1690.field_1904.method_1434()) {
                  this.U(var1, var2, var1x -> {
                     this.f = this.H;
                     this.M.L(var1x, this.H);
                  });
               }
            }
         }
      }
   }

   protected void U(class_310 var1, class_3965 var2, IntConsumer var3) {
      e7.m(
         var1,
         var2,
         () -> {
            if (this.isSwapActive()
               && var1.field_1724 != null
               && var1.field_1687 != null
               && var1.field_1724.field_7512 == var1.field_1724.field_7498
               && !var1.field_1724.method_5715()
               && !var1.field_1690.field_1904.method_1434()
               && var2.method_17777().equals(this.getAssignedDispenserPosition(var1))) {
               this.L = (boolean)1;

               try {
                  var1.field_1761.method_2896(var1.field_1724, class_1268.field_5808, var2);
                  var3.accept(((q5a3f607535c664c2)var1.field_1687).LarpRig$getPendingUpdateManager().method_41942());
                  return true;
               } finally {
                  this.L = (boolean)0;
               }
            } else {
               return false;
            }
         }
      );
   }

   static int S(int var0, int var1) {
      return var0 >= 0 && var0 < 9 ? var0 : var1;
   }

   private class_3965 H(class_310 var1) {
      return T(var1, this.N);
   }

   protected static class_3965 T(class_310 var0, class_2338 var1) {
      if (var0.field_1765 instanceof class_3965 var2
         && var2.method_17783() == class_240.field_1332
         && var2.method_17777().equals(var1)
         && var0.field_1724.method_33571().method_1025(var2.method_17784()) <= Math.pow(var0.field_1724.method_55754(), 2.0)) {
         return var2;
      } else {
         class_243 var14 = var0.field_1724.method_33571();
         class_3965 var15 = null;
         double var4 = Double.MAX_VALUE;

         for (class_2350 var9 : class_2350.values()) {
            class_243 var10 = class_243.method_24953(var1).method_1031(var9.method_10148() * 0.499, var9.method_10164() * 0.499, var9.method_10165() * 0.499);
            class_3965 var11 = var0.field_1687.method_17742(new class_3959(var14, var10, class_3960.field_17559, class_242.field_1348, var0.field_1724));
            double var12 = var14.method_1025(var11.method_17784());
            if (var11.method_17783() == class_240.field_1332
               && var11.method_17777().equals(var1)
               && var12 <= var0.field_1724.method_55754() * var0.field_1724.method_55754()
               && var12 < var4) {
               var15 = var11;
               var4 = var12;
            }
         }

         return var15;
      }
   }

   private void N(String var1) {
      if (this.c < 6) {
         this.c++;
         fr.V("retry (" + var1 + ") pass=" + this.c + " phase=" + this.Z + " tick=" + this.Y);
         yq.R("SWAP " + this.M() + " retry (" + var1 + ")");
         this.j(1, "retry " + var1);
      } else {
         fr.V("CRITICAL fail (" + var1 + ") after pass=" + this.c);
         this.S(false, var1);
      }
   }

   private void z(class_310 var1) {
      this.o = null;
      if (var1 != null && var1.field_1724 != null) {
         if (this.w >= 0 && var1.field_1724.field_7512 != null && var1.field_1724.field_7512.field_7763 == this.w) {
            fr.V("close suppressed container syncId=" + this.w + " phase=" + this.Z);
            if (!(var1.field_1755 instanceof class_490) && !(var1.field_1755 instanceof class_408)) {
               var1.field_1724.method_7346();
            } else {
               var1.method_1562().method_52787(new class_2815(this.w));
            }
         }

         var1.field_1724.field_7512 = var1.field_1724.field_7498;
         this.w = -1;
      }
   }

   private void S(boolean var1, String var2) {
      if (!var1) {
         yq.E(this.M(), "Swap failed: " + var2);
      }

      yq.R("SWAP " + this.M() + " " + (var1 ? "OK" : "FAIL") + " (" + var2 + ") real=" + this.m(this.l) + " ->" + this.m(this.R));
      if (!var1 && this.m(class_310.method_1551())) {
         this.j(2, "cleanup " + var2);
      } else {
         this.G(var1, var2);
      }
   }

   private void G(boolean var1, String var2) {
      class_310 var3 = class_310.method_1551();
      qa var4 = this.o;
      if (var4 != null) {
         fr.V("BATCH TRACE " + this.M() + " " + this.q(var3) + " " + var4.r());
      }

      fr.V("finishSwapDecided ok=" + var1 + " reason=" + var2 + " syncId=" + this.w + " queue=" + this.pendingLabel() + " phase=" + this.Z);
      this.z(var3);
      this.a = 0;
      this.nd = var1 ? 12 : 0;
      if (var1) {
         this.G(this.l, this.R);
      }

      this.c();
      if (var1 && !this.n0.isEmpty() && var4 != null) {
         this.ng = new HashMap<>(this.b);
         com.larpclient.t.completeDrawBaseline(this.h, this.r, this.U, this.n, this.getTemplateIdForValue(this.R));
         System.arraycopy(this.h, 0, this.d, 0, 36);
         System.arraycopy(this.r, 0, this.C, 0, 36);
      }

      this.l();
      this.Z = q7.v.I;
      this.z = (boolean)0;
      if (var1) {
         class_1799 var5 = var3.field_1724.method_6047();
         if (!this.isDealtHandLocked()
            || !var5.method_31574(class_1802.field_8204)
            || this.A(var5) < 0
            || (this.u == null || this.A(var5) != this.A(this.u)) && this.A(var5) != this.getTemplateIdForValue(this.R)) {
            this.M();
            this.k();
         } else {
            this.u = var5.method_7972();
            this.nW = (boolean)1;
         }
      }

      if (!var1) {
         this.M();
         this.k();
      }

      this.v = var1 ? 16 : 0;
      this.s("swap end " + (var1 ? "ok" : "fail") + " " + var2);
      if (this.V >= 0 && !this.n0.isEmpty() && this.n0.peekFirst() == this.I) {
         this.n5 = this.V;
         this.T = this.I;
         this.S();
      } else {
         this.H();
         this.l();
      }
   }

   private void j(int var1, String var2) {
      class_310 var3 = class_310.method_1551();
      if (!this.m(var3)) {
         this.z(var3);
         this.U(var3, var1, var2);
      } else {
         this.j(var3, var2 + ": occupied cursor before batch");
      }
   }

   private void U(class_310 var1, int var2, String var3) {
      switch (var2) {
         case 1:
            this.E = 0;
            this.w = -1;
            this.Y = 1;
            this.Z = q7.v.V;
            break;
         case 2:
            this.G(false, var3);
            break;
         default:
            this.z(var1);
            this.Z = q7.v.I;
            this.l();
            yq.R("SWAP " + this.M() + " aborted");
      }
   }

   private boolean m(class_310 var1) {
      class_1716 var2 = this.i(var1);
      return var2 != null && !var2.method_34255().method_7960();
   }

   private void c() {
      if (!this.n0.isEmpty()) {
         yq.R("PLAYED " + this.M() + " next=" + this.n0.peekFirst());
         this.n0.pollFirst();
         if (this.n0.isEmpty()) {
            this.ng.clear();
            if (this.Z == q7.v.I && this.w >= 0) {
               if (this.o != null && this.o.K()) {
                  this.z(class_310.method_1551());
               } else {
                  this.j(class_310.method_1551(), "queue ended without a safe cursor snapshot");
               }
            }
         } else {
            this.C();
         }
      }
   }

   private void Y(q7.v var1) {
      this.Z = var1;
      if (this.o != null) {
         this.o.k(this.q(class_310.method_1551()));
      }

      this.Y = 0;
   }

   private void H() {
      this.V = -1;
      this.O = 0;
      this.I = 0;
   }

   private void l() {
      this.t = Map.of();
      this.S = -1;
      this.n5 = -1;
      this.T = -1;
      this.P = -1;
      this.D = null;
   }

   public int getSwapSyncId() {
      return this.w;
   }

   private void S() {
      this.P = -1;
      if (this.n5 >= 0) {
         class_310 var1 = class_310.method_1551();
         if (var1 != null && var1.field_1724 != null) {
            this.P = this.m >= 0 && this.m < 36 && this.A(var1.field_1724.method_31548().method_5438(this.m)) == this.n5 ? this.m : this.N(var1, this.n5);
         }
      }
   }

   public void onDealtCardSlotUpdate(int var1, int var2, int var3) {
      if (var3 >= 0) {
         int var4 = this.C(new class_9209(var3));
         if (var4 >= 1) {
            class_310 var5 = class_310.method_1551();
            Object var6 = var1 == 0 ? var5.field_1724.field_7498 : var5.field_1724.field_7512;
            if (var2 >= 0 && var2 < ((class_1703)var6).field_7761.size()) {
               class_1735 var7 = var6.method_7611(var2);
               if (var7.field_7871 == var5.field_1724.method_31548() && var7.method_34266() < 36) {
                  this.m = var7.method_34266();
                  if (this.n0.isEmpty() || this.Z != q7.v.I || this.d(var5, var3) >= 0) {
                     if (this.n0.isEmpty()) {
                        this.n5 = var3;
                        this.S();
                        yq.R("SWAP " + this.M() + " slot-update sync=" + var1 + " slot=" + var2 + " real=" + this.m(var4) + " (no plan armed)");
                     } else if (this.Z == q7.v.I) {
                        int var8 = this.n0.peekFirst();
                        if (var4 == var8) {
                           yq.R("SWAP " + this.M() + " slot-update real=" + this.m(var4) + " == pending (no swap needed)");
                        } else {
                           this.n5 = var3;
                           this.T = var8;
                           this.S();
                           this.s();
                           if (!this.swapWorthwhile(var4, var8)) {
                              this.G(var4, var4);
                              yq.R(
                                 "SWAP "
                                    + this.M()
                                    + " skip (dealer already wins) real="
                                    + this.m(var4)
                                    + " armed="
                                    + this.m(var8)
                                    + " queue="
                                    + this.pendingLabel()
                              );
                              this.l();
                              this.c();
                           } else {
                              if (!this.o(class_310.method_1551(), var3, var4) && this.N(class_310.method_1551(), var3) >= 0) {
                                 this.V = var3;
                                 this.O = var4;
                                 this.I = var8;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   static boolean P(class_437 var0) {
      return var0 != null && !(var0 instanceof class_490) && !(var0 instanceof class_408);
   }

   private class_1716 i(class_310 var1) {
      if (var1 != null && var1.field_1724 != null && !P(var1.field_1755)) {
         return var1.field_1724.field_7512 instanceof class_1716 var2 && var2.field_7763 == this.w ? var2 : null;
      } else {
         return null;
      }
   }

   protected boolean allowsReturnSlot(int var1, int var2) {
      return true;
   }

   private int Z(class_1716 var1, int var2) {
      if (var2 < 0) {
         return -1;
      }

      for (class_1735 var4 : var1.field_7761) {
         if (var4 != null && !(var4.field_7871 instanceof class_1661)) {
            class_1799 var5 = var4.method_7677();
            if (var5 != null && var5.method_31574(class_1802.field_8204) && this.A(var5) == var2) {
               return var4.field_7874;
            }
         }
      }

      return -1;
   }

   private int F(class_1716 var1, class_1799 var2) {
      int var3 = this.A(var2);
      int var4 = this.C((class_9209)var2.method_57353().method_58694(class_9334.field_49646));
      int var5 = -1;
      if (var4 >= 1 && var3 >= 0) {
         var5 = this.Z(var1, var3);
         if (var5 < 0) {
            var5 = this.containerIndexForValue(var4);
         }
      } else {
         var5 = this.k;
      }

      if (var5 >= 0) {
         class_1735 var6 = var1.method_7611(var5);
         if (var6 != null) {
            class_1799 var7 = var6.method_7677();
            if (var7 != null && !var7.method_7960() && this.A(var7) != var3) {
               var5 = -1;
            }
         }
      }

      if (var5 < 0) {
         var5 = this.a(var1);
      }

      return var5;
   }

   private int a(class_1716 var1) {
      for (class_1735 var3 : var1.field_7761) {
         if (var3 != null && !(var3.field_7871 instanceof class_1661) && var3.method_7677().method_7960()) {
            return var3.field_7874;
         }
      }

      return -1;
   }

   private int v(class_1716 var1, int var2) {
      int var3 = this.getTemplateIdForValue(var2);
      return var3 < 0 ? -1 : this.Z(var1, var3);
   }

   private int N(class_310 var1, int var2) {
      if (var1 != null && var1.field_1724 != null) {
         class_1661 var3 = var1.field_1724.method_31548();

         for (int var4 = 0; var4 < var3.method_5439(); var4++) {
            class_1799 var5 = var3.method_5438(var4);
            if (var5 != null && var5.method_31574(class_1802.field_8204) && this.A(var5) == var2) {
               return var4;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private int A(class_1716 var1, int var2) {
      if (var2 < 0) {
         return -1;
      }

      for (class_1735 var4 : var1.field_7761) {
         if (var4 != null && var4.field_7871 instanceof class_1661) {
            class_1799 var5 = var4.method_7677();
            if (var5 != null && var5.method_31574(class_1802.field_8204) && this.A(var5) == var2) {
               return var4.field_7874;
            }
         }
      }

      return -1;
   }

   private boolean i(class_310 var1, int var2) {
      return this.N(var1, var2) >= 0;
   }

   private int J(int var1) {
      return var1 >= 0 && var1 < 9 ? var1 + 36 : var1;
   }

   private boolean o(class_1716 var1, int var2) {
      return this.c(var1, this.getTemplateIdForValue(var2));
   }

   private boolean c(class_1716 var1, int var2) {
      class_1799 var3 = var1.method_34255();
      return var3 != null && var3.method_31574(class_1802.field_8204) && this.A(var3) == var2;
   }

   private boolean R(class_1716 var1, int var2, int var3) {
      return this.k(var1, var2, this.getTemplateIdForValue(var3));
   }

   private boolean k(class_1716 var1, int var2, int var3) {
      if (var2 >= 0 && var2 < var1.field_7761.size()) {
         class_1735 var4 = var1.method_7611(var2);
         if (var4 == null) {
            return false;
         }

         class_1799 var5 = var4.method_7677();
         return var5 != null && var5.method_31574(class_1802.field_8204) && this.A(var5) == var3;
      } else {
         return false;
      }
   }

   private double B(class_310 var1) {
      if (var1 != null && var1.field_1724 != null && this.N != null) {
         class_238 var2 = new class_238(this.N);
         return Math.sqrt(var2.method_49271(var1.field_1724.method_33571()));
      } else {
         return -1.0;
      }
   }

   private void s(String var1) {
      class_310 var2 = class_310.method_1551();
      if (var2 != null && var2.field_1724 != null) {
         StringBuilder var3 = new StringBuilder();
         int var4 = var2.field_1724.method_31548().method_67532();

         for (int var5 = 0; var5 < var2.field_1724.method_31548().method_5439(); var5++) {
            class_1799 var6 = var2.field_1724.method_31548().method_5438(var5);
            if (var6 != null && !var6.method_7960() && var6.method_31574(class_1802.field_8204)) {
               class_9209 var7 = (class_9209)var6.method_57353().method_58694(class_9334.field_49646);
               int var8 = var7 == null ? -1 : this.C(var7);
               if (var8 >= 1) {
                  if (!var3.isEmpty()) {
                     var3.append(", ");
                  }

                  var3.append("v").append(this.m(var8)).append("x").append(var6.method_7947()).append("@").append(var5).append(var5 == var4 ? "(sel)" : "");
               }
            }
         }

         yq.R("DUMP " + this.M() + " " + var1 + " selected=" + var4 + " cards=[" + var3 + "]");
      }
   }

   private int A(class_1799 var1) {
      class_9209 var2 = (class_9209)var1.method_57353().method_58694(class_9334.field_49646);
      return var2 == null ? -1 : var2.comp_2315();
   }

   private int C(class_9209 var1) {
      if (var1 == null) {
         return -1;
      }

      tw var2 = tw.s();
      if (var2.j() && var2.K()) {
         return var2.U(var1.comp_2315());
      }

      for (Entry var4 : this.q.entrySet()) {
         if (var4.getValue() != null && ((class_9209)var4.getValue()).comp_2315() == var1.comp_2315()) {
            return (Integer)var4.getKey();
         }
      }

      return -1;
   }

   private Map<Integer, Integer> S() {
      return this.p.computeIfAbsent(this.P(), var0 -> new HashMap<>());
   }

   private Map<Integer, Integer> g() {
      return this.e.computeIfAbsent(this.P(), var0 -> new HashMap<>());
   }

   private String P() {
      return this.G == null ? "" : this.G;
   }

   private boolean u() {
      Map var1 = this.e();

      for (Entry var3 : var1.entrySet()) {
         if ((Integer)var3.getValue() > this.ng.getOrDefault(var3.getKey(), 0)) {
            return true;
         }
      }

      return false;
   }

   private Map<Integer, Integer> e() {
      HashMap var1 = new HashMap();
      class_310 var2 = class_310.method_1551();
      if (var2 != null && var2.field_1724 != null) {
         for (int var3 = 0; var3 < var2.field_1724.method_31548().method_5439(); var3++) {
            this.o(var1, var2.field_1724.method_31548().method_5438(var3));
         }
      }

      if (var2 != null && var2.field_1687 != null) {
         for (class_1297 var4 : var2.field_1687.method_18112()) {
            if (var4 instanceof class_1533 var5) {
               this.o(var1, var5.method_6940());
            }
         }
      }

      return var1;
   }

   private void o(Map<Integer, Integer> var1, class_1799 var2) {
      if (var2 != null && !var2.method_7960() && var2.method_31574(class_1802.field_8204)) {
         class_9209 var3 = (class_9209)var2.method_57353().method_58694(class_9334.field_49646);
         if (var3 != null) {
            var1.merge(var3.comp_2315(), var2.method_7947(), Integer::sum);
         }
      }
   }

   public enum v {
      I,
      V,
      G,
      W,
      w,
      g,
      o;
   }
}

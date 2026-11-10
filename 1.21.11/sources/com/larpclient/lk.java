package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1661;
import net.minecraft.class_1716;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_310;
import net.minecraft.class_9209;
import net.minecraft.class_9334;

public final class lk {
   private static final lk g = new lk();
   private final List<Integer> y = new ArrayList<>();
   private final List<Integer> x = new ArrayList<>();
   private final Deque<lk.r> Z = new ArrayDeque<>();
   private final Map<Integer, Integer> U = new HashMap<>();
   private final Map<Integer, Integer> s = new HashMap<>();
   private Map<Integer, Integer> K = new HashMap<>();
   private int J = -1;
   private Map<Integer, Integer> L;
   private Map<String, Integer> z = new HashMap<>();
   private lk.p r = lk.p.y;
   private int M;
   private int o = -1;
   private boolean f;
   private boolean t;
   private boolean k;
   private boolean B;
   private final ArrayDeque<Integer> E = new ArrayDeque<>();

   private lk() {
   }

   public static lk k() {
      return g;
   }

   public void f(class_310 var1, qq var2, lk.r var3) {
      b7.x(
         "DISP side="
            + var3
            + " roundActive="
            + this.f
            + " roundComplete="
            + this.t
            + " dealtCards="
            + this.M
            + " pending="
            + this.Z
            + " target="
            + this.r
            + " enabled="
            + v.D().V
      );
      if (!this.f || this.t) {
         this.N(var1);
      }

      if (var2.isSwapBusy()) {
         this.Z.addLast(var3);
      } else {
         if (this.B) {
            if (!var2.hasQueuedForce() && !this.E.isEmpty()) {
               var2.queueForce(this.E.removeFirst());
            }

            if (var2.hasQueuedForce()) {
               b7.x("PRESET draw side=" + var3 + " value=" + var2.getPendingValue() + " left=" + this.E);
               this.Z.addLast(var3);
               return;
            }

            this.B = (boolean)0;
            yq.E("Baccarat", "Preset used up; the automatic plan resumes");
         }

         this.U(var2);
         if (v.D().V && this.r != lk.p.y && this.M >= 3 && this.n(var3) >= 1) {
            this.o = this.h(var2, var3);
            if (this.o >= 1) {
               var2.queueForce(this.o);
               this.k = (boolean)1;
            }
         }

         this.Z.addLast(var3);
      }
   }

   public void Y(class_310 var1, qq var2, boolean var3) {
      if (var3 && var1 != null && var1.field_1724 != null) {
         Map var4 = this.X(var1);
         Map var5 = this.N(var1);
         if (this.L == null) {
            this.L = new HashMap<>(var4);
            this.z = new HashMap<>(var5);
         } else if (var2.isSwapBusy()) {
            b7.x("HOLD swapBusy=" + var2.isSwapActive() + " defer=" + var2.e());
         } else {
            if (this.f && !this.Z.isEmpty()) {
               for (Entry var7 : var4.entrySet()) {
                  int var8 = Math.max(0, (Integer)var7.getValue() - this.L.getOrDefault(var7.getKey(), 0));

                  while (var8-- > 0 && !this.Z.isEmpty()) {
                     int var9 = this.j(var1, var2, (Integer)var7.getKey(), var5);
                     int var10 = this.k && this.o >= 1 && var9 < 1 ? this.o : var9;
                     b7.x(
                        "TICK mapId="
                           + var7.getKey()
                           + " physical="
                           + var9
                           + " chosen="
                           + var10
                           + " pending="
                           + this.Z
                           + " prev="
                           + this.L.getOrDefault(var7.getKey(), 0)
                           + " cur="
                           + var7.getValue()
                     );
                     if (var10 >= 1 && var10 <= 9) {
                        this.t(this.Z.removeFirst(), var10, (Integer)var7.getKey());
                     } else {
                        b7.x("DROPPED mapId=" + var7.getKey() + " value=" + var10 + " (consumed added but pending side not removed)");
                     }
                  }
               }
            }

            this.L = new HashMap<>(var4);
            this.z = new HashMap<>(var5);
            if (this.M > 0 && this.b(var1)) {
               this.I(var2, var4);
            }
         }
      } else {
         this.V(var2);
      }
   }

   public void t(qq var1) {
      this.U(var1);

      this.r = switch (this.r) {
         case y -> lk.p.F;
         case F -> lk.p.J;
         case J -> lk.p.y;
      };
   }

   public void U(String var1, qq var2) {
      if (var1 != null) {
         lk.p var3 = switch (var1.trim().toLowerCase(Locale.ROOT)) {
            case "player" -> lk.p.F;
            case "banker" -> lk.p.J;
            case "off" -> lk.p.y;
            default -> null;
         };
         if (var3 != null && var3 != this.r) {
            this.U(var2);
            this.r = var3;
         }
      }
   }

   private void N(class_310 var1) {
      qq.f().U();
      this.y.clear();
      this.x.clear();
      this.U.clear();
      this.s.clear();
      this.K.clear();
      this.J = -1;
      this.M = 0;
      this.o = -1;
      this.Z.clear();
      this.f = (boolean)1;
      this.t = (boolean)0;
      this.k = (boolean)0;
      if (var1 != null && var1.field_1724 != null) {
         this.L = this.X(var1);
         this.z = this.N(var1);
      }
   }

   private void t(lk.r var1, int var2, int var3) {
      qq.f().S(var2, var3);
      b7.x("RECORD side=" + var1 + " value=" + var2 + " mapId=" + var3 + " player=" + this.y + " banker=" + this.x);
      if (var1 == lk.r.E) {
         this.y.add(var2);
      } else {
         this.x.add(var2);
      }

      this.U.merge(var3, 1, Integer::sum);
      this.M++;
      this.t = this.u();
      this.k = (boolean)0;
      this.o = -1;
   }

   private boolean u() {
      if (this.y.size() >= 2 && this.x.size() >= 2) {
         int var1 = j(this.y);
         int var2 = j(this.x);
         if (this.y.size() != 2 || this.x.size() != 2) {
            int var3 = this.y.size() < 3 && var1 <= 5 ? 0 : 1;
            int var4 = this.x.size() < 3 && var2 <= 5 ? 0 : 1;
            return var3 != 0 && var4 != 0;
         } else {
            return var1 < 8 && var2 < 8 ? var1 > 5 && var2 > 5 : true;
         }
      } else {
         return false;
      }
   }

   private int n(lk.r var1) {
      return var1 == lk.r.E ? this.y.size() : this.x.size();
   }

   private int h(qq var1, lk.r var2) {
      int var3 = Integer.MIN_VALUE;
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 <= 9; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = this.J(var1, var2, var7);
            if (var8 >= 100) {
               var5.add(var7);
            } else if (var8 == 50) {
               var6.add(var7);
            }

            if (var8 > var3) {
               var3 = var8;
               var4.clear();
               var4.add(var7);
            } else if (var8 == var3) {
               var4.add(var7);
            }
         }
      }

      if (!var5.isEmpty()) {
         return (Integer)var5.get(ThreadLocalRandom.current().nextInt(var5.size()));
      } else if (!var6.isEmpty()) {
         return (Integer)var6.get(ThreadLocalRandom.current().nextInt(var6.size()));
      } else {
         return var4.isEmpty() ? -1 : (Integer)var4.get(ThreadLocalRandom.current().nextInt(var4.size()));
      }
   }

   private int J(qq var1, lk.r var2, int var3) {
      ArrayList var4 = new ArrayList<>(this.y);
      ArrayList var5 = new ArrayList<>(this.x);
      if (var2 == lk.r.E) {
         var4.add(var3);
      } else {
         var5.add(var3);
      }

      int var6 = this.j(var1, var4, var5);
      if (var4.size() == 2 && var5.size() == 2) {
         int var7 = this.r == lk.p.F ? j(var4) - j(var5) : j(var5) - j(var4);
         if ((var2 == lk.r.E && this.r == lk.p.F || var2 == lk.r.l && this.r == lk.p.J) && var7 > 0) {
            var6 += 1000 + var7;
         }
      }

      return var6;
   }

   private int j(qq var1, List<Integer> var2, List<Integer> var3) {
      if (var2.size() >= 2 && var3.size() >= 2) {
         int var4 = j(var2);
         int var5 = j(var3);
         if (var2.size() == 2 && var3.size() == 2) {
            if (var4 >= 8 || var5 >= 8) {
               return this.g(var4, var5);
            } else if (var4 <= 5) {
               return this.p(var1, var2, var3, lk.r.E);
            } else {
               return var5 <= 5 ? this.p(var1, var2, var3, lk.r.l) : this.g(var4, var5);
            }
         } else {
            return var2.size() >= 3 && var3.size() == 2 && var5 <= 5 ? this.p(var1, var2, var3, lk.r.l) : this.g(var4, var5);
         }
      } else {
         return this.g(j(var2), j(var3));
      }
   }

   private int p(qq var1, List<Integer> var2, List<Integer> var3, lk.r var4) {
      int var5 = Integer.MIN_VALUE;

      for (int var6 = 1; var6 <= 9; var6++) {
         if (var1.getTemplateForValue(var6) != null) {
            ArrayList var7 = new ArrayList(var2);
            ArrayList var8 = new ArrayList(var3);
            if (var4 == lk.r.E) {
               var7.add(var6);
            } else {
               var8.add(var6);
            }

            var5 = Math.max(var5, this.j(var1, var7, var8));
         }
      }

      return var5 == Integer.MIN_VALUE ? this.g(j(var2), j(var3)) : var5;
   }

   private int g(int var1, int var2) {
      int var3 = this.r == lk.p.F ? var1 - var2 : var2 - var1;
      if (var3 > 0) {
         return 100 + var3;
      } else {
         return var3 == 0 ? 50 : var3;
      }
   }

   public boolean w(qq var1, int var2, int var3) {
      if (this.r != lk.p.y && var2 >= 1 && var3 >= 1) {
         lk.r var4 = this.Z.peekFirst();
         if (var4 == null) {
            return true;
         }

         int var5 = this.J(var1, var4, var2);
         int var6 = this.J(var1, var4, var3);
         int var7 = var5 >= 100 || var6 < 100 && (var5 >= 50 || var6 != 50) ? 0 : 1;
         b7.x(
            "SWAP_DECISION side="
               + var4
               + " target="
               + this.r
               + " real="
               + var2
               + " armed="
               + var3
               + " realScore="
               + var5
               + " armedScore="
               + var6
               + " swap="
               + var7
         );
         return (boolean)var7;
      } else {
         return true;
      }
   }

   public boolean F(qq var1, int[] var2) {
      if (var1 != null && var2 != null && var2.length != 0) {
         if (var1.isSwapBusy()) {
            return false;
         }

         this.U(var1);

         for (int var6 : var2) {
            if (var1.getTemplateForValue(var6) != null) {
               this.E.addLast(var6);
            }
         }

         if (this.E.isEmpty()) {
            return false;
         }

         var1.queueForce(this.E.removeFirst());
         this.B = var1.hasQueuedForce();
         if (!this.B) {
            this.E.clear();
         }

         return this.B;
      } else {
         return false;
      }
   }

   public String N() {
      if (!this.B) {
         return "";
      }

      StringBuilder var1 = new StringBuilder();
      int var2 = qq.f().getPendingValue();
      if (var2 >= 1) {
         var1.append(var2);
      }

      for (int var4 : this.E) {
         var1.append(var1.isEmpty() ? "" : " ").append(var4);
      }

      return var1.toString();
   }

   private void U(qq var1) {
      this.B = (boolean)0;
      this.E.clear();
      if (var1 != null) {
         var1.clearQueue();
      }

      this.k = (boolean)0;
      this.o = -1;
   }

   private void V(qq var1) {
      var1.U();
      this.U(var1);
      this.y.clear();
      this.x.clear();
      this.U.clear();
      this.s.clear();
      this.K.clear();
      this.J = -1;
      this.L = null;
      this.z.clear();
      this.Z.clear();
      this.M = 0;
      this.f = (boolean)0;
      this.t = (boolean)0;
   }

   private Map<Integer, Integer> X(class_310 var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.field_1724.method_31548().method_5439(); var3++) {
         X(var2, var1.field_1724.method_31548().method_5438(var3));
      }

      if (var1.field_1724.field_7512 != null) {
         X(var2, var1.field_1724.field_7512.method_34255());
      }

      return var2;
   }

   private static void X(Map<Integer, Integer> var0, class_1799 var1) {
      if (var1 != null && !var1.method_7960() && var1.method_31574(class_1802.field_8204)) {
         class_9209 var2 = (class_9209)var1.method_57353().method_58694(class_9334.field_49646);
         if (var2 != null) {
            var0.merge(var2.comp_2315(), var1.method_7947(), Integer::sum);
         }
      }
   }

   private Map<String, Integer> N(class_310 var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.field_1724.method_31548().method_5439(); var3++) {
         class_1799 var4 = var1.field_1724.method_31548().method_5438(var3);
         if (var4 != null && !var4.method_7960() && var4.method_31574(class_1802.field_8204)) {
            class_9209 var5 = (class_9209)var4.method_57353().method_58694(class_9334.field_49646);
            if (var5 != null) {
               var2.put("inventory[" + var3 + "]#" + var5.comp_2315(), var4.method_7947());
            }
         }
      }

      return var2;
   }

   private int j(class_310 var1, qq var2, int var3, Map<String, Integer> var4) {
      for (int var5 = 0; var5 < var1.field_1724.method_31548().method_5439(); var5++) {
         class_1799 var6 = var1.field_1724.method_31548().method_5438(var5);
         if (var6 != null && !var6.method_7960() && var6.method_31574(class_1802.field_8204)) {
            class_9209 var7 = (class_9209)var6.method_57353().method_58694(class_9334.field_49646);
            String var8 = "inventory[" + var5 + "]";
            String var9 = var8 + "#" + var3;
            if (var7 != null && var7.comp_2315() == var3 && var4.getOrDefault(var9, 0) > this.z.getOrDefault(var9, 0)) {
               return var2.M(var8, var6);
            }
         }
      }

      for (int var10 = 0; var10 < var1.field_1724.method_31548().method_5439(); var10++) {
         class_1799 var11 = var1.field_1724.method_31548().method_5438(var10);
         if (var11 != null && !var11.method_7960() && var11.method_31574(class_1802.field_8204)) {
            class_9209 var12 = (class_9209)var11.method_57353().method_58694(class_9334.field_49646);
            if (var12 != null && var12.comp_2315() == var3) {
               return var2.M("inventory[" + var10 + "]", var11);
            }
         }
      }

      return -1;
   }

   private boolean b(class_310 var1) {
      if (var1.field_1724.field_7512 instanceof class_1716 var2) {
         HashMap var7 = new HashMap();

         for (class_1735 var5 : var2.field_7761) {
            if (var5 != null && !(var5.field_7871 instanceof class_1661)) {
               X(var7, var5.method_7677());
            }
         }

         if (var2.field_7763 != this.J) {
            this.J = var2.field_7763;
            this.K = var7;
            return false;
         }

         for (Entry var10 : var7.entrySet()) {
            if (this.U.containsKey(var10.getKey())) {
               int var6 = Math.max(0, (Integer)var10.getValue() - this.K.getOrDefault(var10.getKey(), 0));
               if (var6 > 0) {
                  this.s.merge((Integer)var10.getKey(), var6, Integer::sum);
               }
            }
         }

         this.K = var7;

         for (Entry var11 : this.U.entrySet()) {
            if (this.s.getOrDefault(var11.getKey(), 0) < (Integer)var11.getValue()) {
               return false;
            }
         }

         return true;
      } else {
         this.J = -1;
         this.K.clear();
         return false;
      }
   }

   private void I(qq var1, Map<Integer, Integer> var2) {
      this.U(var1);
      this.y.clear();
      this.x.clear();
      this.U.clear();
      this.s.clear();
      this.K.clear();
      this.J = -1;
      this.Z.clear();
      this.M = 0;
      this.f = (boolean)0;
      this.t = (boolean)0;
      this.L = new HashMap<>(var2);
      this.z = this.N(class_310.method_1551());
   }

   private static int j(List<Integer> var0) {
      int var1 = 0;

      for (int var3 : var0) {
         var1 += var3;
      }

      return Math.floorMod(var1, 10);
   }

   public int E() {
      return j(this.y);
   }

   public int s() {
      return j(this.x);
   }

   public int n() {
      return this.o;
   }

   public int v() {
      return this.M;
   }

   public String m() {
      return this.r.name().toLowerCase(Locale.ROOT);
   }

   private enum p {
      y,
      F,
      J;
   }

   public enum r {
      E,
      l;
   }
}

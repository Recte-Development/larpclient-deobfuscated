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
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_310;
import net.minecraft.class_9209;
import net.minecraft.class_9334;

public final class q_ {
   private static final q_ r = new q_();
   private final List<Integer> s = new ArrayList<>();
   private final List<Integer> O = new ArrayList<>();
   private final Deque<q_.y> V = new ArrayDeque<>();
   private final Map<Integer, Integer> X = new HashMap<>();
   private Map<Integer, Integer> a;
   private Map<String, Integer> R = new HashMap<>();
   private q_.u k = q_.u.e;
   private int h = -1;
   private boolean t;
   private boolean J;
   private int Y;

   private q_() {
   }

   public static q_ j() {
      return r;
   }

   public void C(class_310 var1, ql var2) {
      if (!this.t) {
         this.V(var1);
      }

      int var3 = this.W() + this.V.size();
      q_.y var4 = var3 == 0 ? q_.y.t : (var3 == 1 ? q_.y.C : (this.k == q_.u.R ? q_.y.C : q_.y.t));
      this.V.addLast(var4);
      if (!var2.isSwapBusy()) {
         int var5 = this.h;
         this.w(var2);
         if (var3 >= 2 && var4 == q_.y.C && this.k == q_.u.R && s1.j().e) {
            this.h = var5 >= 1 ? var5 : this.x(var2);
            if (this.h >= 1) {
               var2.queueForce(this.h);
               this.J = (boolean)1;
            }
         }
      }
   }

   public void s(class_310 var1, ql var2, boolean var3) {
      if (!var3 || var1 == null || var1.field_1724 == null) {
         this.a(var2);
      } else if (!var2.isSwapBusy()) {
         Map var4 = this.L(var1);
         Map var5 = this.v(var1);
         if (this.a == null) {
            this.a = new HashMap<>(var4);
            this.R = new HashMap<>(var5);
         } else {
            if (this.t && !this.V.isEmpty()) {
               for (Entry var7 : var4.entrySet()) {
                  int var8 = Math.max(0, (Integer)var7.getValue() - this.a.getOrDefault(var7.getKey(), 0));

                  while (var8-- > 0 && !this.V.isEmpty()) {
                     int var9 = this.n(var1, var2, (Integer)var7.getKey(), var5);
                     if (var9 >= 1 && var9 <= 9) {
                        this.v(this.V.removeFirst(), var9, (Integer)var7.getKey());
                     }
                  }
               }
            }

            this.a = new HashMap<>(var4);
            this.R = new HashMap<>(var5);
            this.A(var2, var4);
            this.D(var2);
         }
      }
   }

   private void A(ql var1, Map<Integer, Integer> var2) {
      if (this.W() <= 0) {
         this.Y = 0;
      } else {
         int var3 = 0;

         for (Entry var5 : this.X.entrySet()) {
            int var6 = var2.getOrDefault(var5.getKey(), 0);
            var3 += Math.min((Integer)var5.getValue(), var6);
         }

         this.Y = var3 <= 0 ? this.Y + 1 : 0;
         if (this.Y >= 6) {
            this.U(var1, var2);
         }
      }
   }

   public void B(ql var1) {
      this.w(var1);

      this.k = switch (this.k) {
         case e -> q_.u.R;
         case R -> q_.u.e;
      };
      this.D(var1);
   }

   public void p(String var1, ql var2) {
      if (var1 != null) {
         q_.u var3 = switch (var1.trim().toLowerCase(Locale.ROOT)) {
            case "player" -> q_.u.e;
            case "dealer" -> q_.u.R;
            case "off" -> q_.u.e;
            default -> null;
         };
         if (var3 != null && var3 != this.k) {
            this.w(var2);
            this.k = var3;
            this.D(var2);
         }
      }
   }

   private void D(ql var1) {
      if (!var1.isSwapBusy() && this.V.isEmpty()) {
         if (this.k != q_.u.R || this.W() < 2 || !s1.j().e) {
            this.h = -1;
         } else if (this.h < 1) {
            this.h = this.x(var1);
         }
      }
   }

   private void V(class_310 var1) {
      ql.Y().U();
      this.s.clear();
      this.O.clear();
      this.X.clear();
      this.h = -1;
      this.V.clear();
      this.Y = 0;
      this.t = (boolean)1;
      this.J = (boolean)0;
      if (var1 != null && var1.field_1724 != null) {
         this.a = this.L(var1);
         this.R = this.v(var1);
      }
   }

   private void v(q_.y var1, int var2, int var3) {
      ql.Y().S(var2, var3);
      if (var1 == q_.y.t) {
         this.s.add(var2);
      } else {
         this.O.add(var2);
      }

      this.X.merge(var3, 1, Integer::sum);
      this.J = (boolean)0;
      this.h = -1;
   }

   private int x(ql var1) {
      int var2 = u(this.s);
      int var3 = u(this.O);
      if (var2 <= 9 && var3 < 9) {
         ArrayList var4 = new ArrayList();
         ArrayList var5 = new ArrayList();
         int var6 = -1;

         for (int var7 = 1; var7 <= 9; var7++) {
            if (var1.getTemplateForValue(var7) != null) {
               int var8 = var3 + var7;
               if (var8 <= 9) {
                  if (var2 >= 1 && var8 >= var2) {
                     var4.add(var7);
                  }

                  if (var8 > var6) {
                     var6 = var8;
                     var5.clear();
                     var5.add(var7);
                  } else if (var8 == var6) {
                     var5.add(var7);
                  }
               }
            }
         }

         if (!var4.isEmpty()) {
            return (Integer)var4.get(ThreadLocalRandom.current().nextInt(var4.size()));
         } else {
            return var5.isEmpty() ? -1 : (Integer)var5.get(ThreadLocalRandom.current().nextInt(var5.size()));
         }
      } else {
         return -1;
      }
   }

   private void w(ql var1) {
      if (var1 != null) {
         var1.clearQueue();
      }

      this.J = (boolean)0;
      this.h = -1;
   }

   private void a(ql var1) {
      var1.U();
      this.w(var1);
      this.s.clear();
      this.O.clear();
      this.X.clear();
      this.a = null;
      this.R.clear();
      this.V.clear();
      this.Y = 0;
      this.t = (boolean)0;
   }

   private Map<Integer, Integer> L(class_310 var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.field_1724.method_31548().method_5439(); var3++) {
         y(var2, var1.field_1724.method_31548().method_5438(var3));
      }

      if (var1.field_1724.field_7512 != null) {
         y(var2, var1.field_1724.field_7512.method_34255());
      }

      return var2;
   }

   private static void y(Map<Integer, Integer> var0, class_1799 var1) {
      if (var1 != null && !var1.method_7960() && var1.method_31574(class_1802.field_8204)) {
         class_9209 var2 = (class_9209)var1.method_57353().method_58694(class_9334.field_49646);
         if (var2 != null) {
            var0.merge(var2.comp_2315(), var1.method_7947(), Integer::sum);
         }
      }
   }

   private Map<String, Integer> v(class_310 var1) {
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

   private int n(class_310 var1, ql var2, int var3, Map<String, Integer> var4) {
      for (int var5 = 0; var5 < var1.field_1724.method_31548().method_5439(); var5++) {
         class_1799 var6 = var1.field_1724.method_31548().method_5438(var5);
         if (var6 != null && !var6.method_7960() && var6.method_31574(class_1802.field_8204)) {
            class_9209 var7 = (class_9209)var6.method_57353().method_58694(class_9334.field_49646);
            String var8 = "inventory[" + var5 + "]";
            String var9 = var8 + "#" + var3;
            if (var7 != null && var7.comp_2315() == var3 && var4.getOrDefault(var9, 0) > this.R.getOrDefault(var9, 0)) {
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

   private void U(ql var1, Map<Integer, Integer> var2) {
      this.w(var1);
      this.s.clear();
      this.O.clear();
      this.X.clear();
      this.V.clear();
      this.Y = 0;
      this.t = (boolean)0;
      this.a = new HashMap<>(var2);
      this.R = this.v(class_310.method_1551());
   }

   private String N(List<Integer> var1) {
      if (var1.isEmpty()) {
         return "-";
      }

      StringBuilder var2 = new StringBuilder();

      for (int var4 : var1) {
         if (!var2.isEmpty()) {
            var2.append(", ");
         }

         var2.append(var4);
      }

      var2.append(" = ").append(u(var1));
      if (u(var1) > 9) {
         var2.append(" BUST");
      }

      return var2.toString();
   }

   private int W() {
      return this.X.values().stream().mapToInt(Integer::intValue).sum();
   }

   private static int u(List<Integer> var0) {
      int var1 = 0;

      for (int var3 : var0) {
         var1 += var3;
      }

      return var1;
   }

   public int J() {
      return u(this.s);
   }

   public int j() {
      return u(this.O);
   }

   public String e() {
      return this.N(this.s);
   }

   public String c() {
      return this.N(this.O);
   }

   public int l() {
      return this.h;
   }

   public int S() {
      int var1 = ql.Y().getPendingValue();
      return var1 >= 1 ? var1 : this.h;
   }

   public String v() {
      return this.k.name().toLowerCase(Locale.ROOT);
   }

   private enum u {
      e,
      R;
   }

   public enum y {
      t,
      C;
   }
}

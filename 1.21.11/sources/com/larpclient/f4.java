package com.larpclient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_10192;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1531;
import net.minecraft.class_1542;
import net.minecraft.class_1713;
import net.minecraft.class_1716;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_2246;
import net.minecraft.class_2315;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3966;
import net.minecraft.class_638;
import net.minecraft.class_7923;
import net.minecraft.class_9334;

public final class f4 {
   public static final f4 H = new f4(true);
   public static final f4 Y = new f4(false);
   private final boolean M;
   private final wq P = new wq();
   private final fo D = new fo();
   private final class_1799[] a = new class_1799[5];
   private int[] z = new int[9];
   private final Set<UUID> n = new HashSet<>();
   private final Set<UUID> e = new HashSet<>();
   private final Set<String> i = new HashSet<>();
   private class_2338 N;
   private class_638 w;
   private class_1716 f;
   private boolean W;
   private boolean l;
   private int j = 1;
   private int r;
   private String I = "Open the assigned dispenser to detect armor";
   private String c = "";

   private void o(class_1716 var1, String var2) {
      StringBuilder var3 = new StringBuilder();

      for (int var4 = 0; var4 < 9; var4++) {
         int var5 = this.P.I(var4);
         var3.append(" v")
            .append(var4 + 1)
            .append("->p")
            .append(var5 + 1)
            .append(" real=")
            .append(this.b(var1.method_7611(var5).method_7677()))
            .append(" shown=")
            .append(var1.method_7611(var5).method_7677().method_7960() ? -1 : this.P.b("stock:" + var5, -1));
      }

      String var6 = var3.toString();
      if (!var2.equals("snapshot") || !var6.equals(this.c)) {
         fr.V(this.F() + " " + var2 + " sync=" + var1.field_7763 + var6);
      }

      this.c = var6;
   }

   f4(boolean var1) {
      this.M = var1;
      Arrays.fill(this.a, class_1799.field_8037);
   }

   public static f4 L() {
      return H.W ? H : Y;
   }

   public static f4 O(hu.q var0) {
      return var0 == hu.q.w ? H : (var0 == hu.q.J ? Y : null);
   }

   private hu.q H() {
      return this.M ? hu.q.w : hu.q.J;
   }

   public int W() {
      return this.M ? 3 : 4;
   }

   public boolean h() {
      return this.W;
   }

   public int e() {
      return this.j;
   }

   public String n() {
      return this.I;
   }

   public String F() {
      return this.M ? "Horse Racing" : "Armor Game";
   }

   public String q(int var1) {
      if (var1 >= 1 && var1 <= this.W()) {
         return this.a[var1].method_7960()
            ? (this.M ? "Horse " + var1 : new String[]{"", "Helmet", "Chestplate", "Leggings", "Boots"}[var1])
            : this.a[var1].method_7964().getString();
      } else {
         return "";
      }
   }

   public void x(int var1) {
      if (var1 >= 1 && var1 <= this.W()) {
         this.j = var1;
      }
   }

   public String l() {
      return !this.W ? "1" : (this.j == this.W() ? "off" : Integer.toString(this.j + 1));
   }

   public String f(int var1) {
      if (this.M) {
         return this.q(var1);
      }

      return switch (var1) {
         case 1 -> "Helmet";
         case 2 -> "Chestplate";
         case 3 -> "Leggings";
         case 4 -> "Boots";
         default -> "";
      };
   }

   public void a(boolean var1) {
      if (this.W != var1) {
         this.W = var1;
         this.r(class_310.method_1551());
      }
   }

   private boolean P() {
      return this.N != null && !this.a[1].method_7960();
   }

   private static boolean U(class_1799 var0, class_1799 var1) {
      return !var0.method_7960()
         && !var1.method_7960()
         && var0.method_31574(var1.method_7909())
         && Objects.equals(var0.method_58694(class_9334.field_49631), var1.method_58694(class_9334.field_49631));
   }

   private int b(class_1799 var1) {
      for (int var2 = 1; var2 <= this.W(); var2++) {
         if (U(var1, this.a[var2])) {
            return var2;
         }
      }

      return -1;
   }

   boolean Y(class_1799[] var1) {
      class_1799[] var2 = new class_1799[5];
      Arrays.fill(var2, class_1799.field_8037);
      int[] var3 = new int[9];
      int var4 = 0;
      if (var1.length != 9) {
         throw new IllegalArgumentException("Expected nine dispenser slots");
      }

      for (int var5 = 0; var5 < 9; var5++) {
         class_1799 var6 = var1[var5];
         if (this.M || !var6.method_7960()) {
            if (var6.method_7947() != 1) {
               throw new IllegalArgumentException(
                  this.M ? "Use three horse armor types, three of each (one per slot)" : "Use one helmet, chestplate, leggings and boots"
               );
            }

            int var7;
            if (!this.M) {
               var7 = L(var6);
               if (var7 < 1 || !var2[var7].method_7960()) {
                  throw new IllegalArgumentException("Use one piece for each armor slot; leave other slots empty");
               }
            } else {
               if (!class_7923.field_41178.method_10221(var6.method_7909()).method_12832().endsWith("_horse_armor")) {
                  throw new IllegalArgumentException("Horse Racing requires horse armor");
               }

               var7 = 1;

               while (var7 <= var4 && !U(var6, var2[var7])) {
                  var7++;
               }

               if (var7 > 3) {
                  throw new IllegalArgumentException("Use exactly three horse armor types");
               }

               var4 = Math.max(var4, var7);
            }

            var2[var7] = var6.method_46651(1);
            var3[var5] = var7;
         }
      }

      if (this.M) {
         int[] var10 = Arrays.stream(var3).map(var0 -> var0 - 1).toArray();
         fo.s(var10);
         int[] var13 = new int[4];
         byte var15 = 1;

         for (int var8 = 1; var8 <= 3; var8++) {
            var13[var8] = this.b(var2[var8]);
            if (var13[var8] < 1) {
               var15 = 0;
            }
         }

         if (var15 != 0) {
            class_1799[] var16 = new class_1799[5];
            Arrays.fill(var16, class_1799.field_8037);

            for (int var9 = 1; var9 <= 3; var9++) {
               var16[var13[var9]] = var2[var9];
            }

            for (int var17 = 0; var17 < 9; var17++) {
               var3[var17] = var13[var3[var17]];
            }

            var2 = var16;
         }
      } else {
         for (int var11 = 1; var11 <= 4; var11++) {
            if (var2[var11].method_7960()) {
               throw new IllegalArgumentException("Armor Game needs all four armor pieces");
            }
         }
      }

      int var12 = !Arrays.equals(this.z, var3) ? 1 : 0;

      for (int var14 = 1; var14 <= this.W(); var14++) {
         var12 |= !class_1799.method_31577(this.a[var14], var2[var14]) ? 1 : 0;
      }

      System.arraycopy(var2, 0, this.a, 0, 5);
      this.z = var3;
      return (boolean)var12;
   }

   public void B(class_310 var1, class_1716 var2, class_2338 var3) {
      if (var1 != null && var1.field_1724 != null && var3 != null) {
         int var4 = this.w == var1.field_1687 && Objects.equals(this.N, var3) ? 0 : 1;
         if (var4 != 0) {
            Arrays.fill(this.a, class_1799.field_8037);
            this.z = new int[9];
            this.N = var3;
            this.w = var1.field_1687;
            this.r(var1);
         }

         this.f = var2;
         class_1799[] var5 = new class_1799[9];

         for (int var6 = 0; var6 < 9; var6++) {
            var5[var6] = var2.method_7611(var6).method_7677();
         }

         int var15 = this.P() && this.P.H() ? 1 : 0;

         for (class_1799 var10 : var5) {
            if (!var10.method_7960() && (var10.method_7947() != 1 || this.b(var10) < 1)) {
               var15 = 0;
            }
         }

         if (var15 != 0) {
            this.h(var1);

            for (class_1799 var24 : var5) {
               int var26 = this.b(var24);
               if (var26 > 0) {
                  this.a[var26] = var24.method_46651(1);
               }
            }

            this.I = "Refreshed armor; retained displayed slots";
         } else {
            try {
               this.Y(var5);
               this.r(var1);
               this.f = var2;
               this.I = "Detected " + (this.M ? "9 horse armor slots" : "4 armor pieces and their slots");
               yq.E(this.F(), this.I);
            } catch (IllegalArgumentException var14) {
               byte var17 = this.P();

               for (class_1799 var12 : var5) {
                  if (!var12.method_7960() && (var12.method_7947() != 1 || this.b(var12) < 1)) {
                     var17 = 0;
                  }
               }

               if (var17 != 0) {
                  this.h(var1);

                  for (class_1799 var27 : var5) {
                     int var13 = this.b(var27);
                     if (var13 > 0) {
                        this.a[var13] = var27.method_46651(1);
                     }
                  }

                  this.I = "Tracking active round; detected occupied slots";
                  return;
               }

               this.I = var14.getMessage();
               Arrays.fill(this.a, class_1799.field_8037);
               this.z = new int[9];
               this.r(var1);
               yq.E(this.F(), "Not ready: " + this.I);
            }
         }
      }
   }

   public void A(class_310 var1) {
      if (var1 != null && var1.field_1687 != null && this.w == var1.field_1687) {
         hu var2 = hu.f();
         if (this.N != null && !Objects.equals(this.N, var2.F(this.H()))) {
            this.N = null;
            Arrays.fill(this.a, class_1799.field_8037);
            this.z = new int[9];
            this.r(var1);
         }

         this.h(var1);
      } else {
         this.N = null;
         this.w = var1 == null ? null : var1.field_1687;
         Arrays.fill(this.a, class_1799.field_8037);
         this.z = new int[9];
         this.r(var1);
      }
   }

   private void r(class_310 var1) {
      this.D.H();
      this.P.F(this.z);
      this.n.clear();
      this.e.clear();
      this.i.clear();
      this.f = null;
      this.r = ThreadLocalRandom.current().nextInt(2520);
      if (var1 != null && var1.field_1724 != null) {
         for (int var2 = 0; var2 < 38; var2++) {
            if (this.b(this.D(var1, var2)) > 0) {
               this.i.add("player:" + var2);
            }
         }

         if (!this.M) {
            for (int var6 = 1; var6 <= 4; var6++) {
               if (!var1.field_1724.method_6118(z(var6)).method_7960()) {
                  this.i.add("worn:" + var6);
               }
            }
         }

         if (var1.field_1687 != null) {
            for (class_1297 var3 : var1.field_1687.method_18112()) {
               this.e.add(var3.method_5667());
               if (!this.M && var3 instanceof class_1531 var4) {
                  for (int var5 = 1; var5 <= 4; var5++) {
                     if (!var4.method_6118(z(var5)).method_7960()) {
                        this.i.add("stand:" + var4.method_5667() + ":" + var5);
                     }
                  }
               }
            }
         }
      }
   }

   private class_1799 D(class_310 var1, int var2) {
      return var2 < 36
         ? var1.field_1724.method_31548().method_5438(var2)
         : (var2 == 36 ? var1.field_1724.field_7512.method_34255() : var1.field_1724.method_6079());
   }

   private class_1716 o(class_310 var1) {
      hu var2 = hu.f();
      return var1 != null
            && var1.field_1724 != null
            && this.f != null
            && var1.field_1724.field_7512 == this.f
            && var2.z() == this.f.field_7763
            && var2.f() == this.H()
            && Objects.equals(this.N, var2.j(var2.W()))
         ? this.f
         : null;
   }

   private class_1799 i(class_1799 var1, int var2) {
      return !var1.method_7960() && var2 > 0 && var2 <= this.W() && !this.a[var2].method_7960() ? this.a[var2].method_46651(var1.method_7947()) : var1;
   }

   private void U(int var1, int var2) {
      yq.E(this.F(), yq.R(this.q(var1), this.q(var2), true, true));
   }

   public class_1799 U(class_1799 var1) {
      class_310 var2 = class_310.method_1551();
      if (this.W && this.P() && var2 != null && var2.field_1724 != null && !var1.method_7960()) {
         this.h(var2);

         for (int var3 = 0; var3 < 38; var3++) {
            if (var1 == this.D(var2, var3)) {
               return this.i(var1, this.P.b("player:" + var3, this.b(var1)));
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public class_1799 h(class_1542 var1, class_1799 var2) {
      if (this.W && this.P()) {
         this.h(class_310.method_1551());
         return this.n.contains(var1.method_5667()) ? this.i(var2, this.P.b("world:" + var1.method_5667(), this.b(var2))) : var2;
      } else {
         return var2;
      }
   }

   public class_1799 y(class_310 var1, class_1735 var2) {
      if (this.W && this.P() && var2 != null) {
         if (!this.M && var2.field_7871 == var1.field_1724.method_31548() && var2.method_34266() >= 36 && var2.method_34266() <= 39) {
            class_1799[] var6 = this.p(var1);
            return var6 == null ? null : var6[40 - var2.method_34266()];
         } else {
            class_1716 var3 = this.o(var1);
            if (var3 != null && var2.field_7871 != var1.field_1724.method_31548() && var2.method_34266() >= 0 && var2.method_34266() <= 8) {
               this.h(var1);
               int var4 = this.P.I(var2.method_34266());
               class_1799 var5 = var3.method_7611(var4).method_7677();
               return this.i(var5, this.P.b("stock:" + var4, this.b(var5)));
            } else {
               return null;
            }
         }
      } else {
         return null;
      }
   }

   public boolean g(class_310 var1, int var2, int var3, int var4, class_1713 var5) {
      if (this.W && this.P() && !this.l && var3 >= 0 && var1 != null && var1.field_1761 != null) {
         if (!this.M
            && var1.field_1724 != null
            && var1.field_1724.field_7512 == var1.field_1724.field_7498
            && var2 == var1.field_1724.field_7498.field_7763
            && var3 >= 5
            && var3 <= 8) {
            this.h(var1);
            int var6 = var3 - 4;
            int var7 = var6;
            class_1799 var8 = var1.field_1724.field_7512.method_34255();
            int var9 = L(var8);
            if (!var8.method_7960() && var9 > 0 && var9 != var6 && this.P.b("player:36", var9) == var6 && var1.field_1724.method_6118(z(var9)).method_7960()) {
               this.l = (boolean)1;

               try {
                  var1.field_1761.method_2906(var2, var9 + 4, var4, var5, var1.field_1724);
               } finally {
                  this.l = (boolean)0;
               }

               return true;
            }

            for (int var10 = 1; var10 <= 4; var10++) {
               if (!var1.field_1724.method_6118(z(var10)).method_7960() && this.P.b("worn:" + var10, var10) == var6) {
                  var7 = var10;
                  break;
               }
            }

            if (var7 != var6) {
               this.l = (boolean)1;

               try {
                  var1.field_1761.method_2906(var2, var7 + 4, var4, var5, var1.field_1724);
               } finally {
                  this.l = (boolean)0;
               }

               return true;
            }

            if (!var1.field_1724.method_6118(z(var6)).method_7960() && this.P.b("worn:" + var6, var6) != var6) {
               return true;
            }
         }

         class_1716 var34 = this.o(var1);
         if (var34 == null || var34.field_7763 != var2) {
            return false;
         }

         if (var3 >= var34.field_7761.size()) {
            return false;
         }

         this.h(var1);
         if (var5 == class_1713.field_7794 && var34.method_7611(var3).field_7871 == var1.field_1724.method_31548()) {
            int var35 = this.P.i("player:" + var34.method_7611(var3).method_34266());
            if (var35 >= 0 && var35 < 9) {
               if (var34.method_34255().method_7960()
                  && var34.method_7611(var35).method_7677().method_7960()
                  && var34.method_7611(var3).method_7677().method_7947() == 1) {
                  if (this.M) {
                     for (int var37 = 0; var37 < 9; var37++) {
                        if (var34.method_7611(this.P.I(var37)).method_7677().method_7960()) {
                           this.P.w("player:" + var34.method_7611(var3).method_34266(), var37);
                           break;
                        }
                     }
                  }

                  this.o(var34, "home-return source=" + var3 + " destination=" + var35);
                  this.l = (boolean)1;

                  try {
                     int var38 = var34.method_7611(var3).method_34266();
                     if (var38 < 9) {
                        var1.field_1761.method_2906(var2, var35, var38, class_1713.field_7791, var1.field_1724);
                     } else {
                        var1.field_1761.method_2906(var2, var3, 40, class_1713.field_7791, var1.field_1724);
                        var1.field_1761.method_2906(var2, var35, 40, class_1713.field_7791, var1.field_1724);
                        var1.field_1761.method_2906(var2, var3, 40, class_1713.field_7791, var1.field_1724);
                     }
                  } finally {
                     this.l = (boolean)0;
                  }

                  this.h(var1);
                  return true;
               }

               return true;
            }
         }

         if (var3 > 8) {
            return false;
         }

         int var36 = this.P.I(var3);
         if (var36 == var3) {
            return false;
         }

         this.l = (boolean)1;

         try {
            var1.field_1761.method_2906(var2, var36, var4, var5, var1.field_1724);
         } finally {
            this.l = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public void h(class_310 var1) {
      if (!this.l && this.W && this.P() && var1 != null && var1.field_1724 != null && var1.field_1687 != null) {
         LinkedHashMap var2 = new LinkedHashMap();
         HashSet var3 = new HashSet();
         class_1716 var4 = this.o(var1);
         if (var4 != null) {
            for (int var5 = 0; var5 < 9; var5++) {
               var2.put("stock:" + var5, this.b(var4.method_7611(var5).method_7677()));
            }
         }

         for (int var14 = 0; var14 < 38; var14++) {
            class_1799 var6 = var14 < 36
               ? var1.field_1724.method_31548().method_5438(var14)
               : (var14 == 36 ? var1.field_1724.field_7512.method_34255() : var1.field_1724.method_6079());
            String var7 = "player:" + var14;
            int var8 = this.b(var6);
            if (var8 < 0) {
               this.i.remove(var7);
            }

            if (!this.i.contains(var7)) {
               var2.put(var7, var8);
               if (var4 == null) {
                  var3.add(var7);
               }
            }
         }

         if (!this.M) {
            for (int var15 = 1; var15 <= 4; var15++) {
               String var17 = "worn:" + var15;
               int var19 = this.b(var1.field_1724.method_6118(z(var15)));
               if (var19 < 1) {
                  this.i.remove(var17);
               }

               if (!this.i.contains(var17)) {
                  var2.put(var17, var19);
               }
            }
         }

         class_2338 var16 = this.N;
         class_2680 var18 = var1.field_1687.method_8320(var16);
         class_243 var20 = class_243.method_24953(var16);
         if (var18.method_27852(class_2246.field_10228) || var18.method_27852(class_2246.field_10200)) {
            class_2350 var21 = (class_2350)var18.method_11654(class_2315.field_10918);
            var20 = var20.method_1031(var21.method_10148() * 0.7, var21.method_10164() * 0.7, var21.method_10165() * 0.7);
         }

         for (class_1297 var9 : var1.field_1687.method_18112()) {
            if (var9 instanceof class_1542 var10 && !var10.method_31481() && this.b(var10.method_6983()) > 0) {
               int var11 = var10.method_5707(var20) <= 2.25 ? 1 : 0;
               if (!this.e.contains(var10.method_5667()) && (var11 != 0 || var10.method_5858(var1.field_1724) <= 9.0)) {
                  this.n.add(var10.method_5667());
               }

               if (this.n.contains(var10.method_5667())) {
                  String var12 = "world:" + var10.method_5667();
                  var2.put(var12, this.b(var10.method_6983()));
                  if (var11 != 0) {
                     var3.add(var12);
                  }
               }
            }
         }

         if (!this.M) {
            for (class_1297 var27 : var1.field_1687.method_18112()) {
               if (var27 instanceof class_1531 var23 && var23.method_5707(var20) < 9.0) {
                  for (int var28 = 1; var28 <= 4; var28++) {
                     String var29 = "stand:" + var23.method_5667() + ":" + var28;
                     int var13 = this.b(var23.method_6118(z(var28)));
                     if (var13 < 1) {
                        this.i.remove(var29);
                     }

                     if (!this.i.contains(var29)) {
                        var2.put(var29, var13);
                        var3.add(var29);
                     }
                  }
               }
            }
         }

         if (this.M) {
            this.P.C(var2, var3, () -> this.D.F(this.j), this.r, var4 != null);
         } else {
            this.P.u(var2, var3, this.j, this.r, var4 != null);
         }

         if (this.M && var4 != null && this.P.m()) {
            this.D.H();
         }

         for (wq.l var26 : this.P.u()) {
            this.U(var26.actual(), var26.display());
         }

         if (var4 != null) {
            this.o(var4, "snapshot");
         }
      }
   }

   private static class_1304 z(int var0) {
      return switch (var0) {
         case 1 -> class_1304.field_6169;
         case 2 -> class_1304.field_6174;
         case 3 -> class_1304.field_6172;
         default -> class_1304.field_6166;
      };
   }

   private static int L(class_1799 var0) {
      class_10192 var1 = (class_10192)var0.method_58694(class_9334.field_54196);
      if (var1 == null) {
         return -1;
      }

      for (int var2 = 1; var2 <= 4; var2++) {
         if (var1.comp_3174() == z(var2)) {
            return var2;
         }
      }

      return -1;
   }

   public class_1799[] p(class_310 var1) {
      if (!this.M && this.W && this.P() && var1 != null && var1.field_1724 != null) {
         this.h(var1);
         class_1799[] var2 = new class_1799[5];
         Arrays.fill(var2, class_1799.field_8037);

         for (int var3 = 1; var3 <= 4; var3++) {
            class_1799 var4 = var1.field_1724.method_6118(z(var3));
            int var5 = this.P.b("worn:" + var3, var3);
            if (!var4.method_7960()) {
               var2[var5] = var5 == var3 ? var4 : this.i(var4, var5);
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   public class_1799[] D(class_1531 var1) {
      if (this.W && !this.M && this.P() && !(var1.method_5707(class_243.method_24953(this.N)) > 16.0)) {
         this.h(class_310.method_1551());
         class_1799[] var2 = new class_1799[5];
         Arrays.fill(var2, class_1799.field_8037);

         for (int var3 = 1; var3 <= 4; var3++) {
            class_1799 var4 = var1.method_6118(z(var3));
            int var5 = this.P.b("stand:" + var1.method_5667() + ":" + var3, var3);
            if (!var4.method_7960()) {
               var2[this.b(var4) > 0 ? var5 : var3] = this.b(var4) > 0 ? this.i(var4, var5) : var4;
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   public class_3966 F(class_1531 var1, class_3966 var2) {
      if (this.D(var1) == null) {
         return var2;
      }

      double var3 = (var2.method_17784().field_1351 - var1.method_23318()) / (var1.method_6914() ? 0.5 : 1.0);
      int var5 = var3 >= 1.6 ? 1 : (var3 >= 0.9 ? 2 : (var3 >= 0.4 ? 3 : 4));

      for (int var6 = 1; var6 <= 4; var6++) {
         if (!var1.method_6118(z(var6)).method_7960() && this.P.b("stand:" + var1.method_5667() + ":" + var6, var6) == var5) {
            double var7 = switch (var6) {
               case 1 -> 1.75;
               case 2 -> 1.2;
               case 3 -> 0.65;
               default -> 0.15;
            };
            return new class_3966(
               var1,
               new class_243(var2.method_17784().field_1352, var1.method_23318() + var7 * (var1.method_6914() ? 0.5 : 1.0), var2.method_17784().field_1350)
            );
         }
      }

      return var2;
   }
}

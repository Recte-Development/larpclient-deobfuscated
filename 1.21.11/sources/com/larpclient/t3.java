package com.larpclient;

import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_1542;
import net.minecraft.class_1713;
import net.minecraft.class_1716;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_310;

@Environment(EnvType.CLIENT)
public final class t3 {
   private static final class_1792[] A = new class_1792[]{
      class_1802.field_8831,
      class_1802.field_27022,
      class_1802.field_8620,
      class_1802.field_8477,
      class_1802.field_23847,
      class_1802.field_22019,
      class_1802.field_22020,
      class_1802.field_49814,
      class_1802.field_8547
   };
   private static final String f = "Host";
   private static final String D = "Viewer";
   private static final t3 H = new t3();
   public class_1792 v;
   public class_1792 t;
   public boolean C;
   private final class_1792[] q = new class_1792[9];
   private final class_1792[] B = new class_1792[9];
   private final class_1799[] K = new class_1799[9];
   private final class_1799[] X = new class_1799[9];
   private boolean V;
   private boolean Q;
   private long M;
   private t3.v T;
   private String z;
   private String R;
   private final f3 n = new f3();
   private boolean g;
   private boolean l;

   private t3() {
      this.v = class_1802.field_8279;
      this.t = class_1802.field_8477;
      this.C = (boolean)0;

      for (int var1 = 0; var1 < A.length; var1++) {
         this.q[var1] = A[var1];
         this.B[var1] = A[var1];
         this.K[var1] = new class_1799(A[var1]);
         this.X[var1] = new class_1799(A[var1]);
      }

      this.V = (boolean)1;
      this.Q = (boolean)0;
      this.T = t3.v.d;
      this.z = "Host";
      this.R = "Viewer";
   }

   public static t3 c() {
      return H;
   }

   public t3.v Y() {
      return this.T;
   }

   public String u() {
      return this.T.z();
   }

   public String C() {
      return this.C ? this.u() : "off";
   }

   public String B() {
      return this.T.V();
   }

   public boolean J(t3.v var1) {
      if (var1 != null && this.T != var1) {
         this.T = var1;
         this.L();
         yq.E("Paper", "Visual winner target switched to " + var1.V());
         return true;
      } else {
         return false;
      }
   }

   public boolean O(String var1) {
      if (var1 == null) {
         return false;
      }

      return (boolean)(switch (var1.trim().toLowerCase(Locale.ROOT)) {
         case "host" -> this.J(t3.v.d);
         case "viewer" -> this.J(t3.v.a);
         default -> 0;
      });
   }

   public void M() {
      this.J(this.T == t3.v.d ? t3.v.a : t3.v.d);
   }

   public boolean x() {
      return this.V;
   }

   public long S() {
      return this.M;
   }

   public boolean z(boolean var1) {
      if (this.V == var1) {
         return false;
      }

      this.V = var1;
      if (var1) {
         for (int var2 = 0; var2 < this.q.length; var2++) {
            this.B[var2] = this.q[var2];
            this.X[var2] = g(this.K[var2]);
         }
      }

      this.M++;
      this.L();
      return true;
   }

   public boolean R() {
      return this.Q;
   }

   public void e(boolean var1) {
      this.Q = var1;
   }

   public String c() {
      return this.z;
   }

   public void b(String var1) {
      String var2 = o(var1);
      if (var2 != null) {
         this.z = var2;
      }
   }

   public String G() {
      return this.R;
   }

   public void f(String var1) {
      String var2 = o(var1);
      if (var2 != null) {
         this.R = var2;
      }
   }

   public class_1792 G(boolean var1, int var2) {
      if (var2 < 0 || var2 >= this.q.length) {
         return class_1802.field_8407;
      } else if (this.V) {
         return this.q[var2];
      } else {
         return var1 ? this.q[var2] : this.B[var2];
      }
   }

   public class_1799 s(boolean var1, int var2) {
      if (var2 >= 0 && var2 < this.K.length) {
         class_1799 var3 = this.V ? this.K[var2] : (var1 ? this.K[var2] : this.X[var2]);
         return g(var3);
      } else {
         return class_1799.field_8037;
      }
   }

   public void n(boolean var1, int var2, class_1792 var3) {
      if (var2 >= 0 && var2 < this.q.length && var3 != null && var3 != class_1802.field_8162) {
         this.f(var1, var2, new class_1799(var3));
      }
   }

   public boolean f(boolean var1, int var2, class_1799 var3) {
      if (var2 >= 0 && var2 < this.q.length && var3 != null) {
         class_1799 var4 = g(var3);
         if (this.V) {
            int var5 = j(this.K[var2], var4) && j(this.X[var2], var4) ? 0 : 1;
            if (var5 == 0) {
               return false;
            }

            this.K[var2] = var4;
            this.X[var2] = g(var4);
            if (!var4.method_7960()) {
               this.q[var2] = var4.method_7909();
               this.B[var2] = var4.method_7909();
            }
         } else if (var1) {
            int var6 = !j(this.K[var2], var4) ? 1 : 0;
            if (var6 == 0) {
               return false;
            }

            this.K[var2] = var4;
            if (!var4.method_7960()) {
               this.q[var2] = var4.method_7909();
            }
         } else {
            int var7 = !j(this.X[var2], var4) ? 1 : 0;
            if (var7 == 0) {
               return false;
            }

            this.X[var2] = var4;
            if (!var4.method_7960()) {
               this.B[var2] = var4.method_7909();
            }
         }

         this.M++;
         return true;
      } else {
         return false;
      }
   }

   public boolean F(boolean var1, int var2, class_1799 var3) {
      if (!var3.method_7960() && !this.n.d()) {
         int var4 = u4.r(var3.method_7964().getString());
         if (var4 > 0 && u4.r(var3.method_7964().getString()) != var1) {
            return false;
         }

         if (var4 < 1) {
            var4 = var2 + 1;
         }

         this.n.R(var1 ? 0 : 1, var2, var4);
         return this.f(var1, var4 - 1, var3);
      } else {
         return false;
      }
   }

   public void N(class_310 var1, boolean var2, class_2338 var3) {
   }

   public void O(class_310 var1) {
      if (var1 != null && var1.field_1724 != null && var1.field_1687 != null) {
         hu var2 = hu.f();
         this.n.t(var1, this, var2.F(hu.q.U), var2.F(hu.q.o));
         this.g = this.C;
         this.n.B(var1);
      } else {
         if (this.g) {
            this.n.t(var1);
         }

         this.g = (boolean)0;
      }
   }

   public void n(class_310 var1, int var2, int var3, int var4) {
   }

   public class_1799 C(class_1542 var1, class_1799 var2) {
      return this.C && var1 != null ? this.n.e(class_310.method_1551(), var1) : var2;
   }

   public class_1799 Z(int var1, class_1799 var2) {
      return this.C ? this.n.G(var2) : var2;
   }

   public class_1799 o(class_1799 var1) {
      return this.C ? this.n.G(var1) : var1;
   }

   public class_1799 p(class_1309 var1, class_1799 var2) {
      return this.C && var1 == class_310.method_1551().field_1724 ? this.n.G(var2) : var2;
   }

   private int K(class_310 var1, class_1716 var2) {
      hu var3 = hu.f();
      if (this.C && var1 != null && var1.field_1724 != null && var2 == var1.field_1724.field_7512 && var2.field_7763 == var3.z()) {
         for (int var4 = 0; var4 < 2; var4++) {
            class_2338 var5 = this.n.b[var4];
            if (var5 != null && var3.N(var1, var5).equals(var3.W())) {
               return var4;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public void Y(class_310 var1, class_1716 var2) {
      hu var3 = hu.f();
      this.n.t(var1, this, var3.F(hu.q.U), var3.F(hu.q.o));
      int var4 = this.K(var1, var2);
      if (var4 >= 0) {
         this.n.K(var2, var4, true);
      }
   }

   public class_1799 X(class_310 var1, class_1735 var2) {
      if (var1 != null && var1.field_1724 != null && var2.field_7871 != var1.field_1724.method_31548() && var1.field_1724.field_7512 instanceof class_1716 var3
         )
       {
         int var5 = this.K(var1, var3);
         return var5 < 0 ? null : this.n.B(var1, var3, var5, var2.method_34266());
      } else {
         return null;
      }
   }

   public boolean T(class_310 var1, int var2, int var3, int var4, class_1713 var5) {
      if (!this.l
         && var1 != null
         && var1.field_1724 != null
         && var1.field_1761 != null
         && var1.field_1724.field_7512 instanceof class_1716 var6
         && var6.field_7763 == var2
         && var3 >= 0
         && var3 < 9) {
         int var12 = this.K(var1, var6);
         if (var12 < 0) {
            return false;
         }

         int var8 = this.n.F(var1, var6, var12, var3);
         if (var8 == var3) {
            return false;
         }

         this.l = (boolean)1;

         try {
            var1.field_1761.method_2906(var2, var8, var4, var5, var1.field_1724);
         } finally {
            this.l = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   private void L() {
      this.n.t(class_310.method_1551());
      this.g = (boolean)0;
   }

   private static boolean j(class_1799 var0, class_1799 var1) {
      int var2 = var0 != null && !var0.method_7960() ? 0 : 1;
      int var3 = var1 != null && !var1.method_7960() ? 0 : 1;
      return var2 == 0 && var3 == 0 ? class_1799.method_31577(var0, var1) : var2 == var3;
   }

   private static class_1799 g(class_1799 var0) {
      return var0 != null && !var0.method_7960() ? var0.method_46651(1) : class_1799.field_8037;
   }

   private static String o(String var0) {
      if (var0 == null) {
         return null;
      }

      String var1 = var0.trim();
      if (var1.length() > 24) {
         var1 = var1.substring(0, 24);
      }

      return var1;
   }

   public enum v {
      d("host", "Host"),
      a("viewer", "Viewer");

      private final String u;
      private final String b;

      v(String var3, String var4) {
         this.u = var3;
         this.b = var4;
      }

      public String z() {
         return this.u;
      }

      public String V() {
         return this.b;
      }
   }
}

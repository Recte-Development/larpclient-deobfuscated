package com.larpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.class_1661;
import net.minecraft.class_1716;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2248;
import net.minecraft.class_2315;
import net.minecraft.class_2338;
import net.minecraft.class_2428;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_310;
import net.minecraft.class_3965;
import net.minecraft.class_9209;
import net.minecraft.class_9334;
import net.minecraft.class_239.class_240;

public final class hu {
   private static final hu q = new hu();
   private final Map<String, hu.q> L = new LinkedHashMap<>();
   private final Map<String, Boolean> X = new HashMap<>();
   private String J;
   private int d = -1;
   private int N = -1;
   private int s;
   private int v = -1;
   private String o;
   private boolean x;
   private boolean w;
   private String G;

   private static Path l() {
      return j6.l("dispensers.properties");
   }

   private hu() {
      this.S();
   }

   public static hu f() {
      return q;
   }

   public hu.q f() {
      return this.J == null ? null : this.L.get(this.J);
   }

   public hu.q Y() {
      return this.x ? hu.q.Y : this.f();
   }

   public boolean N() {
      return this.w;
   }

   public String W() {
      return this.J;
   }

   public int z() {
      return this.d;
   }

   public boolean t(class_310 var1, hu.q var2) {
      if (var1 != null && var1.field_1724 != null && var1.field_1687 != null && var1.field_1765 instanceof class_3965 var3) {
         class_2338 var7 = var3.method_17777().method_10062();
         class_2680 var5 = var1.field_1687.method_8320(var7);
         if (!(var5.method_26204() instanceof class_2315)) {
            return false;
         }

         String var6 = this.N(var1, var7);
         this.o = var6;
         return this.J(var6, var2).success();
      } else {
         return false;
      }
   }

   public hu.x E(class_310 var1) {
      return var1 != null
            && var1.field_1724 != null
            && var1.field_1687 != null
            && var1.field_1765 instanceof class_3965 var2
            && var1.field_1687.method_8320(var2.method_17777()).method_26204() instanceof class_2315
         ? this.d(this.N(var1, var2.method_17777()))
         : new hu.x(false, "Look at a dispenser first.");
   }

   public hu.q h(class_310 var1) {
      return var1 != null
            && var1.field_1687 != null
            && var1.field_1765 instanceof class_3965 var2
            && var1.field_1687.method_8320(var2.method_17777()).method_26204() instanceof class_2315
         ? this.L.get(this.N(var1, var2.method_17777()))
         : null;
   }

   public hu.x H(class_310 var1, hu.q var2) {
      if (var1 != null
         && var1.field_1724 != null
         && var1.field_1687 != null
         && var1.field_1765 instanceof class_3965 var3
         && var1.field_1687.method_8320(var3.method_17777()).method_26204() instanceof class_2315) {
         String var5 = this.N(var1, var3.method_17777());
         this.o = var5;
         this.w = (boolean)0;
         return this.J(var5, var2);
      } else {
         return new hu.x(false, "Look at a dispenser first.");
      }
   }

   public void D() {
      this.X.clear();
      this.J = null;
      this.d = -1;
      this.v = -1;
      this.s = 0;
      this.w = (boolean)0;
      this.x = (boolean)0;
      this.G = null;
   }

   public Map<String, hu.q> C() {
      return Collections.unmodifiableMap(this.L);
   }

   public List<String> m(hu.q var1) {
      ArrayList var2 = new ArrayList();
      class_310 var3 = class_310.method_1551();
      if (var3 != null && var3.field_1687 != null && var1 != null) {
         String var4 = var3.field_1687.method_27983().method_29177() + ";";

         for (Entry var6 : this.L.entrySet()) {
            if (var6.getValue() == var1 && ((String)var6.getKey()).startsWith(var4)) {
               var2.add((String)var6.getKey());
            }
         }

         return var2;
      } else {
         return var2;
      }
   }

   public hu.x J(String var1, hu.q var2) {
      if (var1 != null && var2 != null) {
         hu.q var3 = this.L.get(var1);
         if (var3 == var2) {
            boolean var5 = this.v();
            return new hu.x(var5, var5 ? "This dispenser is set and saved as " + var2.c + "." : "This dispenser is set, but saving failed.");
         }

         this.L.put(var1, var2);
         this.o = var1;
         this.X.remove(var1);
         if (!this.v()) {
            return new hu.x(false, "Could not save dispenser assignment. Check the Larp log.");
         }

         String var4 = var3 == null ? "Dispenser set to " + var2.c + "." : "Dispenser moved from " + var3.c + " to " + var2.c + ".";
         if (o(var2)) {
            var4 = var4 + " Open it manually to refresh all item templates.";
         }

         return new hu.x(true, var4);
      } else {
         return new hu.x(false, "Invalid dispenser assignment.");
      }
   }

   public hu.x d(String var1) {
      hu.q var2 = this.L.remove(var1);
      this.X.remove(var1);
      boolean var3 = this.v();
      return new hu.x(
         var3, !var3 ? "Could not save dispenser assignment." : (var2 == null ? "This dispenser was not assigned." : "Removed " + var2.c + " dispenser.")
      );
   }

   public void M(class_310 var1, class_1716 var2) {
      if (var1 != null && var1.field_1724 != null && var1.field_1687 != null && var2 != null) {
         if (!this.w && !q7.L().isSwapActive()) {
            String var3 = this.K(var1);
            hu.q var4 = var3 == null ? null : this.L.get(var3);
            if (var4 != null) {
               if (var2.field_7763 != this.v || !var3.equals(this.J)) {
                  this.x = (boolean)0;
                  this.J = var3;
                  this.d = var2.field_7763;
                  this.N = -1;
                  this.v = var2.field_7763;
                  this.s = !o(var4) && var4 != hu.q.g && var4 != hu.q.X && f4.O(var4) == null ? 20 : 0;
                  switch (var4) {
                     case G:
                     case k:
                        q7.u().setContainerContext(var3);
                        break;
                     case C:
                        qc.i().setContainerContext(var3);
                        break;
                     case R:
                        qr.h().setContainerContext(var3);
                        break;
                     case q:
                     case E:
                        qq.f().setContainerContext(var3);
                        break;
                     case m:
                        ql.Y().setContainerContext(var3);
                     case X:
                     case w:
                     case J:
                     default:
                        break;
                     case Y:
                        yu.o().C(var1, var2);
                  }

                  this.D(var1, var4);
                  if (!o(var4) && var4 != hu.q.X && f4.O(var4) == null && this.l(var1, var2, var4)) {
                     this.s = 0;
                  }
               }
            }
         }
      }
   }

   public void u(class_310 var1) {
      if (var1 != null && var1.field_1724 != null && var1.field_1687 != null) {
         this.o(var1);
         this.v(var1);
         if (!this.w && !this.L.isEmpty()) {
            if (var1.field_1724.field_7512 instanceof class_1716 var2) {
               if (var2.field_7763 != this.v) {
                  this.M(var1, var2);
               }
            } else {
               this.v = -1;
            }

            if (this.s > 0 && var1.field_1724.field_7512 instanceof class_1716 var7 && var7.field_7763 == this.d) {
               hu.q var10 = this.L.get(this.J);
               if (var10 != null && !o(var10) && this.l(var1, var7, var10)) {
                  this.s = 0;
                  if (var10 == hu.q.G) {
                     yq.E("Blackjack", "Templates imported manually from " + this.J + " - registry now holds " + q7.u().countTemplates() + " cards");
                  }
               } else {
                  this.s--;
               }
            }

            String var8 = var1.field_1687.method_27983().method_29177() + ";";

            for (Entry var4 : new LinkedHashMap<>(this.L).entrySet()) {
               String var5 = (String)var4.getKey();
               if (var5.startsWith(var8)) {
                  class_2338 var6 = this.y(var5);
                  if (var6 != null
                     && (
                        var4.getValue() == hu.q.R
                           || var4.getValue() == hu.q.G
                           || var4.getValue() == hu.q.k
                           || var4.getValue() == hu.q.q
                           || var4.getValue() == hu.q.E
                           || var4.getValue() == hu.q.m
                           || var4.getValue() == hu.q.C
                           || o((hu.q)var4.getValue())
                     )) {
                     this.E(var1, var6, var1.field_1687.method_8320(var6));
                  }
               }
            }
         }
      } else {
         this.w = (boolean)0;
         this.x = (boolean)0;
      }
   }

   private void o(class_310 var1) {
      String var2 = var1.field_1687.method_27983().method_29177().toString();
      if (!var2.equals(this.G)) {
         this.X.clear();
         this.S();
         this.G = var2;
      }
   }

   public void E(class_310 var1, class_2338 var2, class_2680 var3) {
      if (var1 != null && var1.method_18854() && var1.field_1724 != null && var1.field_1687 != null) {
         this.o(var1);
         String var4 = this.N(var1, var2);
         hu.q var5 = this.L.get(var4);
         if (var5 == hu.q.G || var5 == hu.q.k || var5 == hu.q.R || var5 == hu.q.q || var5 == hu.q.E || var5 == hu.q.m || var5 == hu.q.C || o(var5)) {
            int var6 = var3.method_26204() instanceof class_2315 && this.h(var3) ? 1 : 0;
            if (this.N(var4, (boolean)var6)) {
               com.larpclient.q var7 = com.larpclient.q.U();
               if (!this.w && var7 != null) {
                  if (o(var5)) {
                     if (var7.Z() && t3.c().C) {
                        int var12 = var5 == hu.q.U ? 1 : 0;
                        yq.E("Paper", (var12 != 0 ? "Host" : "Viewer") + " dispenser trigger detected at " + var4);
                        t3.c().N(var1, (boolean)var12, var2);
                     }
                  } else if (var5 == hu.q.G) {
                     q7 var11 = q7.u();
                     if (!var11.isSwapActive()) {
                        var11.setContainerContext(var4);
                     }

                     l4.K().H(var1, false);
                  } else if (var5 == hu.q.k) {
                     tw.s().d(var2, var3);
                     l4.K().H(var1, true);
                  } else if (var5 == hu.q.q || var5 == hu.q.E) {
                     qq var10 = qq.f();
                     if (!var10.isSwapActive()) {
                        var10.setContainerContext(var4);
                     }

                     lk.k().f(var1, var10, var5 == hu.q.q ? lk.r.E : lk.r.l);
                  } else if (var5 == hu.q.m) {
                     ql var9 = ql.Y();
                     if (!var9.isSwapActive()) {
                        var9.setContainerContext(var4);
                     }

                     q_.j().C(var1, var9);
                  } else if (var5 == hu.q.C) {
                     qc.i().E();
                  } else if (var7.Z() && fd.h().v) {
                     qr var8 = qr.h();
                     if (!var8.isSwapBusy()) {
                        var8.setContainerContext(var4);
                        var8.r(var1);
                     }
                  }
               }
            }
         }
      }
   }

   public boolean E(hu.q var1) {
      return !this.w && !this.m(var1).isEmpty();
   }

   private boolean N(String var1, boolean var2) {
      Boolean var3 = this.X.put(var1, var2);
      return var2 && !Boolean.TRUE.equals(var3);
   }

   public String x(hu.q var1) {
      class_310 var2 = class_310.method_1551();
      if (var2 != null && var2.field_1687 != null && var1 != null) {
         String var3 = var2.field_1687.method_27983().method_29177() + ";";
         String var4 = null;
         if (var2.field_1724 != null && var2.field_1724.field_7512 instanceof class_1716 var5 && var5.field_7763 == this.d && this.L.get(this.J) == var1) {
            var4 = this.J;
         } else if (var2.field_1765 instanceof class_3965 var6 && var6.method_17783() == class_240.field_1332) {
            var4 = this.N(var2, var6.method_17777());
         }

         return this.p(
            var1,
            var3,
            var2.field_1724 == null ? null : new class_243(var2.field_1724.method_23317(), var2.field_1724.method_23318(), var2.field_1724.method_23321()),
            var4
         );
      } else {
         return null;
      }
   }

   String p(hu.q var1, String var2, class_243 var3, String var4) {
      String var5 = null;
      double var6 = Double.MAX_VALUE;

      for (Entry var9 : this.L.entrySet()) {
         if (var9.getValue() == var1 && ((String)var9.getKey()).startsWith(var2)) {
            class_2338 var10 = this.y((String)var9.getKey());
            if (var10 != null) {
               double var11 = var3 == null ? 0.0 : var3.method_1025(class_243.method_24953(var10));
               if (((String)var9.getKey()).equals(var4) && var11 <= 64.0) {
                  return var4;
               }

               if (var5 == null || var11 < var6) {
                  var6 = var11;
                  var5 = (String)var9.getKey();
               }
            }
         }
      }

      return var5;
   }

   public class_2338 j(String var1) {
      return var1 == null ? null : this.y(var1);
   }

   public boolean w(hu.q var1, double var2) {
      class_310 var4 = class_310.method_1551();
      if (var4 != null && var4.field_1724 != null && var4.field_1687 != null && var1 != null) {
         class_2338 var5 = this.F(var1);
         if (var5 == null) {
            return false;
         }

         double var6 = var4.field_1724.method_5649(var5.method_10263() + 0.5, var5.method_10264() + 0.5, var5.method_10260() + 0.5);
         return var6 <= var2 * var2;
      } else {
         return false;
      }
   }

   public void S(int var1) {
      class_310 var2 = class_310.method_1551();
      if (var2 != null && var2.field_1724 != null && var1 == this.d && var2.field_1724.field_7512 instanceof class_1716 var3 && var3.field_7763 == var1) {
         hu.q var6 = this.L.get(this.J);
         if (var6 == hu.q.A) {
            ju.b().o(var2, var3, this.j(this.J));
         }

         f4 var5 = f4.O(var6);
         if (var5 != null) {
            var5.B(var2, var3, this.j(this.J));
         } else if (var6 == hu.q.g || var6 == hu.q.X) {
            this.l(var2, var3, var6);
         } else if (var6 == hu.q.Y) {
            yu.o().C(var2, var3);
         } else if (o(var6) && this.N != var1) {
            this.N = var1;
            t3.c().Y(var2, var3);
            if (this.l(var2, var3, var6)) {
               yq.E("Paper", (var6 == hu.q.U ? "Host" : "Viewer") + " templates refreshed from manually opened dispenser " + this.J);
            }
         }
      }
   }

   public void V(int var1, int var2) {
      class_310 var3 = class_310.method_1551();
      if (var3 != null
         && var3.field_1724 != null
         && var1 == this.d
         && var2 >= 0
         && var2 < 9
         && var3.field_1724.field_7512 instanceof class_1716 var4
         && var4.field_7763 == var1
         && o(this.f())) {
         class_1799 var7 = var4.method_7611(var2).method_7677();
         if (!var7.method_7960() && t3.c().F(this.f() == hu.q.U, var2, var7)) {
            com.larpclient.q var6 = com.larpclient.q.U();
            if (var6 != null) {
               var6.y();
            }
         }
      }
   }

   private static boolean o(hu.q var0) {
      return var0 == hu.q.U || var0 == hu.q.o;
   }

   private void D(class_310 var1, hu.q var2) {
      com.larpclient.q var3 = com.larpclient.q.U();
      if (var3 != null && var2 != null) {
         if (var3.Z()) {
            switch (var2) {
               case U:
               case o:
                  var3.C(com.larpclient.q.n.C);
                  break;
               case g:
                  var3.C(com.larpclient.q.n.P);
                  break;
               case A:
                  var3.C(com.larpclient.q.n.O);
                  break;
               case G:
                  var3.C(com.larpclient.q.n.W);
                  break;
               case k:
                  var3.C(com.larpclient.q.n.x);
                  break;
               case C:
                  var3.C(com.larpclient.q.n.S);
                  break;
               case R:
                  var3.C(com.larpclient.q.n.g);
                  break;
               case q:
               case E:
                  var3.C(com.larpclient.q.n.j);
                  break;
               case m:
                  var3.C(com.larpclient.q.n.m);
                  break;
               case X:
                  var3.C(com.larpclient.q.n.y);
                  break;
               case w:
                  var3.C(com.larpclient.q.n.d);
                  break;
               case J:
                  var3.C(com.larpclient.q.n.e);
                  break;
               case Y:
                  var3.C(com.larpclient.q.n.X);
            }
         }
      }
   }

   private String K(class_310 var1) {
      if (var1.field_1765 instanceof class_3965 var2) {
         String var13 = this.N(var1, var2.method_17777());
         if (var1.field_1687.method_8320(var2.method_17777()).method_26204() instanceof class_2315) {
            return this.L.containsKey(var13) ? var13 : null;
         }
      }

      if (this.o != null && this.L.containsKey(this.o)) {
         class_2338 var11 = this.y(this.o);
         String var14 = var1.field_1687.method_27983().method_29177() + ";";
         if (var11 != null
            && this.o.startsWith(var14)
            && var1.field_1724.method_5649(var11.method_10263() + 0.5, var11.method_10264() + 0.5, var11.method_10260() + 0.5) <= 64.0) {
            return this.o;
         }
      }

      String var12 = var1.field_1687.method_27983().method_29177() + ";";
      String var15 = null;
      double var4 = 64.0;

      for (String var7 : this.L.keySet()) {
         if (var7.startsWith(var12)) {
            class_2338 var8 = this.y(var7);
            if (var8 != null) {
               double var9 = var1.field_1724.method_5649(var8.method_10263() + 0.5, var8.method_10264() + 0.5, var8.method_10260() + 0.5);
               if (var9 < var4) {
                  var4 = var9;
                  var15 = var7;
               }
            }
         }
      }

      return var15;
   }

   private void v(class_310 var1) {
      if (var1.field_1755 == null) {
         if (var1.field_1765 instanceof class_3965 var2 && var2.method_17783() == class_240.field_1332) {
            class_2248 var7 = var1.field_1687.method_8320(var2.method_17777()).method_26204();
            if (var7 instanceof class_2428) {
               this.x = (boolean)1;
               this.w = (boolean)0;
            } else if (!(var7 instanceof class_2315)) {
               this.w = (boolean)0;
            } else {
               this.x = (boolean)0;
               String var4 = this.N(var1, var2.method_17777());
               tw var5 = tw.s();
               int var6 = var5.j() && var5.m() && this.f() == hu.q.k ? 1 : 0;
               this.w = (boolean)(!this.L.containsKey(var4) && var6 == 0 ? 1 : 0);
               if (this.L.containsKey(var4)) {
                  this.o = var4;
                  if (!var4.equals(this.J)) {
                     this.J = var4;
                     this.d = -1;
                     this.s = 0;
                  }
               }
            }
         } else {
            this.w = (boolean)0;
         }
      }
   }

   private boolean l(class_310 var1, class_1716 var2, hu.q var3) {
      int var4 = switch (var3) {
         case U -> this.H(var2, true);
         case o -> this.H(var2, false);
         case g -> this.D(var2);
         case A, w, J, Y -> 0;
         case G -> this.H(var2, q7.u(), new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10});
         case k -> tw.s().K() ? 0 : this.H(var2, q7.u(), new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10});
         case C -> this.E(var2);
         case R -> this.E(var2, qr.h(), 9);
         case q, E -> this.E(var2, qq.f(), 9);
         case m -> this.H(var2, ql.Y(), new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
         case X -> this.W(var2);
      };
      if (var4 <= 0) {
         return false;
      }

      com.larpclient.q var5 = com.larpclient.q.U();
      if (var5 != null) {
         var5.y();
      }

      if (var3 == hu.q.X) {
         yb var6 = yb.b();
         yq.E(
            "Russian Roulette",
            "Loaded dispenser: normal=" + new class_1799(var6.Y).method_7964().getString() + ", trigger=" + new class_1799(var6.P).method_7964().getString()
         );
      }

      return true;
   }

   private int H(class_1716 var1, boolean var2) {
      class_1799[] var3 = new class_1799[9];
      Arrays.fill(var3, class_1799.field_8037);
      int var4 = 0;
      int var5 = 0;

      for (class_1735 var7 : var1.field_7761) {
         if (R(var7) && var7.method_34266() >= 0 && var7.method_34266() <= 8) {
            class_1799 var8 = var7.method_7681() ? var7.method_7677().method_46651(1) : class_1799.field_8037;
            var3[var7.method_34266()] = var8;
            var4++;
            if (!var8.method_7960()) {
               var5++;
            }
         }
      }

      if (var4 >= 9 && var5 > 0) {
         t3 var9 = t3.c();
         byte var10 = 0;

         for (int var11 = 0; var11 < var3.length; var11++) {
            if (!var3[var11].method_7960() && var9.F(var2, var11, var3[var11])) {
               var10 = 1;
            }
         }

         if (var10 != 0) {
            q8.G(
               "Rig", "Paper: learned {} items from the {} table ({})", var5, var2 ? "host" : "viewer", var9.x() ? "shared by both tables" : "this table only"
            );
         }

         return var10 != 0 ? var5 : 0;
      } else {
         return 0;
      }
   }

   private int D(class_1716 var1) {
      return ux.o().k(class_310.method_1551(), this.j(this.J), var1) ? 9 : 0;
   }

   private int E(class_1716 var1, q7 var2, int var3) {
      int var4 = 0;

      for (class_1735 var6 : var1.field_7761) {
         if (R(var6) && var6.method_7681() && var6.method_7677().method_31574(class_1802.field_8204)) {
            class_1799 var7 = var6.method_7677();
            class_9209 var8 = (class_9209)var7.method_57353().method_58694(class_9334.field_49646);
            int var9 = I(var7.method_7964().getString(), var3);
            if (var9 < 1 && var6.method_34266() >= 0 && var6.method_34266() < var3) {
               var9 = var6.method_34266() + 1;
            }

            if (var8 != null && var9 >= 1 && var9 <= var3) {
               var2.setTemplateForValue(var9, var8);
               if (var2 instanceof qr) {
                  String var10 = var7.method_7964().getString().replaceAll("[\\p{Cntrl}]", " ");
                  yq.R(
                     "HigherLower Template import slot="
                        + var6.method_34266()
                        + " mapId="
                        + var8.comp_2315()
                        + " displayedName="
                        + var10.substring(0, Math.min(var10.length(), 120))
                        + " chosenValue="
                        + var9
                  );
               }

               var4++;
            }
         }
      }

      var2.scanCurrentContainerTemplateSlots(class_310.method_1551());
      return var4;
   }

   private int E(class_1716 var1) {
      qc var2 = qc.i();
      int var3 = 0;

      for (int var4 = 0; var4 < 9; var4++) {
         if (var4 != 4) {
            class_1735 var5 = null;

            for (class_1735 var7 : var1.field_7761) {
               if (R(var7) && var7.method_34266() == var4) {
                  var5 = var7;
                  break;
               }
            }

            if (var5 != null && var5.method_7681() && var5.method_7677().method_31574(class_1802.field_8204)) {
               class_9209 var8 = (class_9209)var5.method_7677().method_57353().method_58694(class_9334.field_49646);
               int var9 = var4 + 1;
               if (var8 != null) {
                  var2.setTemplateForValue(var9, var8);
                  var3++;
               }
            }
         }
      }

      var2.scanCurrentContainerTemplateSlots(class_310.method_1551());
      var2.clearQueue();
      var2.T();
      return var3;
   }

   private int H(class_1716 var1, q7 var2, int[] var3) {
      int var4 = 0;

      for (class_1735 var6 : var1.field_7761) {
         if (R(var6) && var6.method_7681() && var6.method_7677().method_31574(class_1802.field_8204)) {
            int var7 = var6.method_34266();
            if (var7 >= 0 && var7 < var3.length) {
               class_9209 var8 = (class_9209)var6.method_7677().method_57353().method_58694(class_9334.field_49646);
               if (var8 != null) {
                  var2.setTemplateForValue(var3[var7], var8);
                  var4++;
               }
            }
         }
      }

      var2.scanCurrentContainerTemplateSlots(class_310.method_1551());
      return var4;
   }

   private int W(class_1716 var1) {
      return yb.b().W(class_310.method_1551(), this.j(this.J), var1) ? 9 : 0;
   }

   private static boolean R(class_1735 var0) {
      return var0 != null && !(var0.field_7871 instanceof class_1661);
   }

   private static int I(String var0, int var1) {
      if (var0 == null) {
         return -1;
      }

      String var2 = var0.toLowerCase(Locale.ROOT);
      if (var2.matches(".*\\b(ace|a)\\b.*")) {
         return 1;
      }

      if (var2.matches(".*\\b(jack|queen|king|j|q|k)\\b.*")) {
         return Math.min(10, var1);
      }

      Matcher var3 = Pattern.compile("(?<!\\d)(10|[1-9])(?!\\d)").matcher(var2);
      if (!var3.find()) {
         return -1;
      }

      try {
         int var4 = Integer.parseInt(var3.group(1));
         return var4 <= var1 ? var4 : -1;
      } catch (NumberFormatException var5) {
         return -1;
      }
   }

   private boolean h(class_2680 var1) {
      return var1 != null && var1.method_28498(class_2741.field_12522) && (Boolean)var1.method_11654(class_2741.field_12522);
   }

   public String N(class_310 var1, class_2338 var2) {
      return var1.field_1687.method_27983().method_29177() + ";" + var2.method_10263() + ";" + var2.method_10264() + ";" + var2.method_10260();
   }

   private class_2338 y(String var1) {
      try {
         String[] var2 = var1.split(";");
         return var2.length == 4 ? new class_2338(Integer.parseInt(var2[1]), Integer.parseInt(var2[2]), Integer.parseInt(var2[3])) : null;
      } catch (Exception var3) {
         return null;
      }
   }

   public class_2338 F(hu.q var1) {
      return this.j(this.x(var1));
   }

   private void S() {
      if (l() != null && Files.exists(l())) {
         Properties var1 = new Properties();

         try (BufferedReader var2 = Files.newBufferedReader(l())) {
            var1.load(var2);
            LinkedHashMap var3 = new LinkedHashMap();

            for (String var5 : var1.stringPropertyNames()) {
               hu.q var6 = hu.q.Y(var1.getProperty(var5));
               if (var6 != null) {
                  var3.put(var5, var6);
               }
            }

            this.L.clear();
            this.L.putAll(var3);
         } catch (Exception var9) {
            q8.L("Rig", "Could not load dispenser assignments from {}: {}", l(), var9.getMessage());
         }
      }
   }

   private boolean v() {
      if (l() == null) {
         return false;
      }

      Properties var1 = new Properties();

      for (Entry var3 : this.L.entrySet()) {
         var1.setProperty((String)var3.getKey(), ((hu.q)var3.getValue()).j);
      }

      try {
         Files.createDirectories(l().getParent());
         Path var18 = l().resolveSibling(l().getFileName() + ".tmp");

         try (
            FileChannel var19 = FileChannel.open(var18, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            OutputStream var4 = Channels.newOutputStream(var19);
            OutputStreamWriter var5 = new OutputStreamWriter(var4, StandardCharsets.ISO_8859_1);
         ) {
            var1.store(var5, "Assigned casino game dispensers");
            var5.flush();
            var19.force(true);
         }

         try {
            Files.move(var18, l(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (Exception var12) {
            Files.move(var18, l(), StandardCopyOption.REPLACE_EXISTING);
         }

         Properties var20 = new Properties();

         try (BufferedReader var21 = Files.newBufferedReader(l())) {
            var20.load(var21);
         }

         for (Entry var23 : this.L.entrySet()) {
            if (!((hu.q)var23.getValue()).j.equalsIgnoreCase(var20.getProperty((String)var23.getKey(), ""))) {
               throw new IOException("verification failed for " + (String)var23.getKey());
            }
         }

         return true;
      } catch (Exception var17) {
         q8.L("Rig", "Could not save dispenser assignments to {}: {}", l(), var17.getMessage());
         return false;
      }
   }

   public enum q {
      U("paper_host", "Paper host"),
      o("paper_viewer", "Paper viewer"),
      g("forty_five_ten", "45/45/10"),
      A("fifty_fifty", "50/50"),
      G("blackjack", "Blackjack"),
      k("blackjack_fo", "Blackjack Force Only"),
      C("odd_even", "Even or Odd"),
      R("higher_lower", "Higher or Lower"),
      q("baccarat_player", "Baccarat Player"),
      E("baccarat_banker", "Baccarat Banker"),
      m("mexican_jack", "Mexican Jack"),
      X("russian", "Russian Roulette"),
      w("horse_racing", "Horse Racing"),
      J("armor_game", "Armor Game"),
      Y("arrow", "Arrow Game");

      public final String j;
      public final String c;

      q(String var3, String var4) {
         this.j = var3;
         this.c = var4;
      }

      private static hu.q Y(String var0) {
         if (var0 == null) {
            return null;
         }

         String var1 = var0.trim();
         if ("paper".equalsIgnoreCase(var1)) {
            return U;
         }

         if ("baccarat".equalsIgnoreCase(var1)) {
            return q;
         }

         if (!"blackjack_force".equalsIgnoreCase(var1) && !"blackjack_normal".equalsIgnoreCase(var1)) {
            for (hu.q var5 : values()) {
               if (var5.j.equalsIgnoreCase(var1)) {
                  return var5;
               }
            }

            return null;
         } else {
            return G;
         }
      }
   }

   public record x(boolean success, String message) {
   }
}

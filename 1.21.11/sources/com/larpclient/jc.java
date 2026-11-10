package com.larpclient;

import com.larpclient.account.AccountVault;
import com.larpclient.account.MicrosoftAccounts;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_310;
import net.minecraft.class_320;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_5481;

public final class jc {
   static final UUID Z = new UUID(0L, 0L);
   private static final int N = -2;
   private static final int r = 20;
   private final class_437 b;
   private final MicrosoftAccounts y = MicrosoftAccounts.INSTANCE;
   private final qb i = new qb(0.0);
   private final lo H;
   private final lo e;
   private final lo k;
   private final lo E;
   private ue.c J = new ue.c(8, 8, 120, 22);
   private jc.u O = jc.u.m;
   private id.f B = id.f.J;
   private boolean W;
   private boolean l;
   private boolean x;
   private int z;
   private int q;
   private int D;
   private int h;
   private int a;
   private int R = Integer.MIN_VALUE;
   private int X;
   private String Q = "";
   private UUID P;
   private class_320 m;

   public jc(class_437 var1) {
      this.b = var1;
      this.H = new lo("Paste access token", var1x -> this.Q = "", 8192).d().T(this::K);
      this.e = new lo("Proxy host:port", this::Y, 300).T(this::z);
      this.k = new lo("Username (optional)", var1x -> this.Q = "", 255).T(this::z);
      this.E = new lo("Password (optional)", var1x -> this.Q = "", 255).d().T(this::z);
   }

   public boolean M() {
      return this.W;
   }

   public void W(ue.c var1) {
      this.J = var1;
   }

   public void a() {
      if (this.W) {
         this.Q();
      } else {
         this.W = (boolean)1;
         this.x = (boolean)0;
         this.P = null;
         this.l = (boolean)0;
         this.q = this.D = 0;
         this.O = jc.u.m;
         this.X();
         this.m = class_310.method_1551().method_1548();
         this.i.w(0.0);
         this.y.open(this.b);
      }
   }

   public void Q() {
      if (this.W) {
         this.y.cancel(this.b);
      }

      this.W = (boolean)0;
      this.x = (boolean)0;
      this.P = null;
      this.O = jc.u.m;
      this.X();
   }

   public static List<jc.w> V(class_320 var0, class_320 var1, List<AccountVault.Entry> var2, boolean var3) {
      LinkedHashMap var4 = new LinkedHashMap();
      AccountVault.Entry var5 = var2.stream().filter(var1x -> var1x.id().equals(var0.method_44717())).findFirst().orElse(null);
      var4.put(var0.method_44717(), new jc.w(var0.method_44717(), var0.method_1676(), true, var5 != null, false, var5 == null ? "" : v(var5)));

      for (AccountVault.Entry var7 : var2) {
         var4.putIfAbsent(var7.id(), new jc.w(var7.id(), var7.name(), false, true, false, v(var7)));
      }

      if (var3 && var1 != null) {
         var4.putIfAbsent(var1.method_44717(), new jc.w(var1.method_44717(), var1.method_1676(), false, false, true, ""));
      }

      return List.copyOf(var4.values());
   }

   public static List<jc.w> n(List<id> var0, UUID var1) {
      ArrayList var2 = new ArrayList();
      var2.add(new jc.w(Z, "No proxy", var1 == null, false, false, "Direct"));

      for (id var4 : var0) {
         var2.add(new jc.w(var4.id(), var4.label(), var4.id().equals(var1), true, false, var4.authenticated() ? "Login" : ""));
      }

      return List.copyOf(var2);
   }

   private static String v(AccountVault.Entry var0) {
      if (!var0.token()) {
         return "";
      }

      Instant var1 = var0.tokenExpiry();
      return var1 != null && var1.isBefore(Instant.now()) ? "Expired" : "Token";
   }

   private List<jc.w> W() {
      if (this.O == jc.u.s) {
         return n(this.y.proxies(), this.y.activeProxyId());
      }

      class_310 var1 = class_310.method_1551();
      return V(var1.method_1548(), this.y.launcher(), this.y.entries(), this.y.canReturn(var1));
   }

   private jc.n F() {
      if (this.x) {
         return jc.n.Z;
      }

      if (this.y.busy()) {
         return this.y.code().isBlank() ? jc.n.F : jc.n.J;
      }

      if (!this.y.unlocked()) {
         return jc.n.M;
      }

      return switch (this.O) {
         case m -> jc.n.r;
         case Z -> jc.n.Y;
         case s -> jc.n.T;
         case H -> jc.n.P;
      };
   }

   private static boolean e(jc.n var0) {
      return var0 == jc.n.r || var0 == jc.n.T;
   }

   private static boolean q(jc.n var0) {
      return var0 == jc.n.Y || var0 == jc.n.P;
   }

   public static jc.g S(int var0, int var1, ue.c var2, int var3, int var4, jc.n var5, boolean var6) {
      int var7 = Math.max(1, Math.min(280, var0 - 16));
      int var8 = e(var5) && var6 ? 28 : 0;

      int var9 = switch (var5) {
         case r, T -> Math.min(6, Math.max(1, var3)) * 32 + 46 + var8;
         case F -> 94;
         case J -> 166;
         case M -> 150;
         case Z -> 124;
         case Y -> 158;
         case P -> 200;
      };
      int var10 = Math.clamp(var2.x() + var2.width() / 2 - var7 / 2, 8, Math.max(8, var0 - var7 - 8));
      int var11 = var2.y() + var2.height() + 4;
      int var12 = e(var5) ? 78 + var8 : var9;
      int var13 = var1 - 8 - var11;
      int var14 = var2.y() - 12;
      int var15 = var13 < var12 && var14 > var13 ? 1 : 0;
      int var16 = var15 != 0 ? var14 : var13;
      int var17 = Math.max(1, Math.min(Math.min(var9, Math.max(var12, var16)), var1 - 16));
      int var18 = var15 != 0 ? var2.y() - var17 - 4 : var11;
      int var19 = Math.clamp(var18, 8, Math.max(8, var1 - var17 - 8));
      int var20 = Math.max(1, (var17 - 46 - var8) / 32);
      return new jc.g(
         new ue.c(var10, var19, var7, var17),
         new ue.c(var10 + 6, var19 + var17 - 34, var7 - 12, 28),
         var20,
         Math.clamp(var4, 0, Math.max(0, var3 - var20)),
         var8
      );
   }

   public static ue.c k(jc.g var0, jc.n var1, int var2) {
      ue.c var3 = var0.panel();
      int var4 = var1 == jc.n.P ? 4 : 1;
      int var5 = var3.y() + (var1 == jc.n.Y ? 50 : 26);
      int var6 = var0.footer().y() - 4 - 20;
      int var7 = Math.max(12, Math.min(26, (var6 - var5) / var4));
      return new ue.c(var3.x() + 10, var5 + var2 * var7, var3.width() - 20, Math.max(10, Math.min(20, var7 - 4)));
   }

   private jc.g V(jc.n var1, int var2) {
      return S(this.b.field_22789, this.b.field_22790, this.J, var2, this.z, var1, !this.S(var1).isBlank());
   }

   private static boolean H(ue.c var0, double var1, double var3) {
      return var1 >= var0.x() && var3 >= var0.y() && var1 < var0.x() + var0.width() && var3 < var0.y() + var0.height();
   }

   private String S(jc.n var1) {
      if (q(var1)) {
         return !this.Q.isEmpty() ? this.Q : (this.y.error() ? this.y.status() : "");
      } else {
         return var1 == jc.n.T && this.y.status().isBlank() && gf.D() != null ? gf.D() : this.y.status();
      }
   }

   private boolean V(jc.n var1) {
      if (q(var1)) {
         return true;
      } else {
         return var1 == jc.n.T && this.y.status().isBlank() ? gf.D() != null : this.y.error();
      }
   }

   private static boolean S() {
      return gf.Q() != null || gf.D() != null;
   }

   private static String b() {
      return gf.Q() != null ? "Proxy: On" : (gf.D() != null ? "Proxy: Paused" : "Proxy: Off");
   }

   private int b(jc.g var1, int var2, int var3, int var4) {
      for (int var5 = var1.scroll(); var5 < Math.min(var2, var1.scroll() + var1.capacity()); var5++) {
         if (H(var1.row(var5), var3, var4)) {
            return var5;
         }
      }

      return -1;
   }

   private static List<ue.c> e(ue.c var0, int var1) {
      int var2 = (var0.width() - 4 * (var1 - 1)) / var1;
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < var1; var4++) {
         var3.add(
            new ue.c(var0.x() + var4 * (var2 + 4), var0.y(), var4 == var1 - 1 ? var0.x() + var0.width() - (var0.x() + var4 * (var2 + 4)) : var2, var0.height())
         );
      }

      return var3;
   }

   private List<jc.h> i(jc.g var1, jc.n var2, List<jc.w> var3, int var4) {
      ArrayList var5 = new ArrayList();
      ue.c var6 = var1.footer();
      ue.c var7 = new ue.c(var6.x(), var6.y() - 30, var6.width(), 24);
      switch (var2) {
         case r:
         case T:
            int var17 = var2 == jc.n.T ? 1 : 0;
            if (var4 >= var1.scroll() && var4 < Math.min(var3.size(), var1.scroll() + var1.capacity())) {
               jc.w var20 = (jc.w)var3.get(var4);
               ue.c var23 = var1.row(var4);
               int var24 = var23.x() + var23.width() - 4;
               if (var20.id().equals(this.P)) {
                  var5.add(new jc.h(new ue.c(var24 - 124, var23.y() + 4, 64, 24), "Remove?", () -> {
                     this.P = null;
                     if (var17) {
                        this.y.removeProxy(this.b, var20.id());
                     } else {
                        this.y.remove(this.b, var20.id());
                     }
                  }, true, false, var4));
                  var5.add(new jc.h(new ue.c(var24 - 56, var23.y() + 4, 56, 24), "Cancel", () -> this.P = null, false, false, var4));
               } else {
                  if (!var20.current()) {
                     var5.add(new jc.h(new ue.c(var24 - (var20.saved() ? 124 : 56), var23.y() + 4, 56, 24), var17 != 0 ? "Use" : "Switch", () -> {
                        if (var17) {
                           if (var20.id().equals(Z)) {
                              this.y.proxyOff(this.b);
                           } else {
                              this.y.useProxy(this.b, var20.id());
                           }
                        } else if (var20.launcher()) {
                           this.y.returnToLauncher(this.b);
                        } else {
                           this.y.switchTo(this.b, var20.id());
                        }
                     }, false, false, var4));
                  }

                  if (var20.saved()) {
                     var5.add(new jc.h(new ue.c(var24 - 64, var23.y() + 4, 64, 24), "Remove", () -> this.P = var20.id(), true, false, var4));
                  }
               }
            }

            if (var17 != 0) {
               List var21 = e(var6, 2);
               var5.add(new jc.h((ue.c)var21.get(0), "+ Add proxy", () -> this.Y(jc.u.H), false, false, var3.size()));
               var5.add(new jc.h((ue.c)var21.get(1), "Back", () -> this.Y(jc.u.m), false, false, var3.size()));
            } else {
               List var22 = e(var6, 3);
               var5.add(new jc.h((ue.c)var22.get(0), "+ Microsoft", () -> {
                  this.P = null;
                  this.y.add(this.b);
               }, false, false, var3.size()));
               var5.add(new jc.h((ue.c)var22.get(1), "+ Token", () -> this.Y(jc.u.Z), false, false, var3.size()));
               var5.add(new jc.h((ue.c)var22.get(2), b(), () -> this.Y(jc.u.s), false, S(), var3.size()));
            }
            break;
         case F:
            var5.add(new jc.h(var6, "Cancel", () -> this.y.cancel(this.b), false, false, 0));
            break;
         case J:
            List var16 = e(var7, 2);
            var5.add(new jc.h((ue.c)var16.get(0), "Open Microsoft", this.y::openBrowser, false, false, 0));
            var5.add(new jc.h((ue.c)var16.get(1), "Copy code", this.y::copyCode, false, false, 0));
            var5.add(new jc.h(var6, "Cancel", () -> this.y.cancel(this.b), false, false, 0));
            break;
         case M:
            List var15 = e(var7, 2);
            var5.add(new jc.h((ue.c)var15.get(0), "Retry", () -> this.y.open(this.b), false, false, 0));
            if (this.y.exists()) {
               var5.add(new jc.h((ue.c)var15.get(1), "Start new list", () -> this.x = (boolean)1, false, false, 0));
            }

            if (S()) {
               List var19 = e(var6, 2);
               var5.add(new jc.h((ue.c)var19.get(0), "Proxy off", () -> this.y.proxyOff(this.b), false, false, 0));
               var5.add(new jc.h((ue.c)var19.get(1), "Close", this::Q, false, false, 0));
            } else {
               var5.add(new jc.h(var6, "Close", this::Q, false, false, 0));
            }
            break;
         case Z:
            List var14 = e(var6, 2);
            var5.add(new jc.h((ue.c)var14.get(0), "Start new list", () -> {
               this.x = (boolean)0;
               this.y.archive(this.b);
            }, true, false, 0));
            var5.add(new jc.h((ue.c)var14.get(1), "Cancel", () -> this.x = (boolean)0, false, false, 0));
            break;
         case Y:
            List var13 = e(var6, 3);
            var5.add(new jc.h((ue.c)var13.get(0), "Paste", this::C, false, false, 0));
            var5.add(new jc.h((ue.c)var13.get(1), "Sign in", this::K, false, false, 0));
            var5.add(new jc.h((ue.c)var13.get(2), "Cancel", () -> this.Y(jc.u.m), false, false, 0));
            break;
         case P:
            List var8 = e(k(var1, var2, 0), 3);

            for (id.f var12 : id.f.values()) {
               var5.add(new jc.h((ue.c)var8.get(var12.ordinal()), var12.b, () -> {
                  this.B = var12;
                  this.Q = "";
               }, false, var12 == this.B, -2));
            }

            List var18 = e(var6, 2);
            var5.add(new jc.h((ue.c)var18.get(0), "Save and use", this::z, false, false, 0));
            var5.add(new jc.h((ue.c)var18.get(1), "Cancel", () -> this.Y(jc.u.s), false, false, 0));
      }

      return var5;
   }

   private List<lo> B(jc.n var1) {
      return var1 == jc.n.Y ? List.of(this.H) : (var1 == jc.n.P ? List.of(this.e, this.k, this.E) : List.of());
   }

   private List<Object> s(jc.n var1, List<jc.h> var2) {
      ArrayList var3 = new ArrayList();

      for (jc.h var5 : var2) {
         if (var5.line() == -2) {
            var3.add(var5);
         }
      }

      var3.addAll(this.B(var1));

      for (jc.h var7 : var2) {
         if (var7.line() != -2) {
            var3.add(var7);
         }
      }

      return var3;
   }

   private void B(jc.g var1, jc.n var2) {
      List var3 = this.B(var2);

      for (int var4 = 0; var4 < var3.size(); var4++) {
         ue.c var5 = k(var1, var2, var2 == jc.n.P ? var4 + 1 : var4);
         ((lo)var3.get(var4)).bounds(var5.x(), var5.y(), var5.width());
         ((lo)var3.get(var4)).w = var5.height();
      }
   }

   private void K(List<Object> var1) {
      for (lo var3 : List.of(this.H, this.e, this.k, this.E)) {
         var3.M = (boolean)(this.h >= 0 && this.h < var1.size() && var1.get(this.h) == var3 ? 1 : 0);
      }
   }

   private void Y(jc.u var1) {
      this.O = var1;
      this.P = null;
      this.z = 0;
      this.q = 0;
      this.D = 0;
      this.Q = "";
      this.y.clearMessage();
      if (var1 == jc.u.m || var1 == jc.u.s) {
         this.X();
      }

      jc.n var2 = this.F();
      if (q(var2)) {
         List var3 = this.s(var2, this.i(this.V(var2, 0), var2, List.of(), -1));
         this.h = var3.indexOf(this.B(var2).getFirst());
         this.K(var3);
      }
   }

   private void X() {
      for (lo var2 : List.of(this.H, this.e, this.k, this.E)) {
         var2.h = "";
         var2.blur();
      }

      this.a = 0;
      this.h = 0;
      this.Q = "";
      this.B = id.f.J;
   }

   private void K() {
      if (this.F() == jc.n.Y) {
         this.Q = "";
         this.y.addToken(this.b, this.H.h, () -> {
            this.H.h = "";
            this.Y(jc.u.m);
         });
      }
   }

   private void C() {
      String var1 = class_310.method_1551().field_1774.method_1460();
      this.H.h = "";
      this.H.M = (boolean)1;
      this.H.v(var1);
      jc.n var2 = this.F();
      List var3 = this.s(var2, this.i(this.V(var2, 0), var2, List.of(), -1));
      this.h = var3.indexOf(this.H);
      this.K(var3);
   }

   private void z() {
      if (this.F() == jc.n.P) {
         id var1;
         try {
            var1 = id.parse(this.B, this.e.h, this.k.h, this.E.h);
         } catch (IllegalArgumentException var3) {
            this.Q = var3.getMessage();
            return;
         }

         this.Q = "";
         this.y.saveProxy(this.b, var1, () -> this.Y(jc.u.s));
      }
   }

   private void Y(String var1) {
      this.Q = "";
      int var2 = this.a;
      this.a = var1.length();
      if (var1.length() - var2 > 1) {
         id.s var3 = id.split(var1, this.B);
         if (var3 != null) {
            this.B = var3.type();
            this.e.h = var3.address();
            this.a = this.e.h.length();
            if (!var3.username().isEmpty() || !var3.password().isEmpty()) {
               this.k.h = var3.username();
               this.E.h = var3.password();
            }
         }
      }
   }

   public void x(class_332 var1, int var2, int var3) {
      if (this.W) {
         if (!this.y.busy() && class_310.method_1551().method_1548() != this.m) {
            this.Q();
         } else {
            jc.n var4 = this.F();
            List var5 = this.W();
            jc.g var6 = this.V(var4, var5.size());
            this.z = var6.scroll();
            if (var2 != this.R || var3 != this.X) {
               this.l = (boolean)0;
               this.R = var2;
               this.X = var3;
            }

            boolean var7 = e(var4);
            int var8 = !var7 ? -1 : (this.l ? this.q : this.b(var6, var5.size(), var2, var3));
            if (var7 && this.P != null) {
               for (int var9 = 0; var9 < var5.size(); var9++) {
                  if (((jc.w)var5.get(var9)).id().equals(this.P)) {
                     var8 = var9;
                  }
               }
            }

            List var34 = this.i(var6, var4, var5, var8);
            List var10 = q(var4) ? this.s(var4, var34) : List.of();
            boolean var11 = LarpClient.b().e().F().k;
            double var12 = var11 ? this.i.a(1.0, 140) : 1.0;
            ue.c var14 = var6.panel();
            String var15 = this.S(var4);
            var1.method_71048();
            p.B(var1, var14.x(), var14.y(), var14.width(), var14.height(), 5, ei.R(-99347436, var12));
            p.a(var1, var14.x(), var14.y(), var14.width(), var14.height(), 5, ei.R(-13290187, var12));
            var1.method_44379(var14.x(), var14.y(), var14.x() + var14.width(), var14.y() + var14.height());

            try {
               if (var7) {
                  for (int var35 = this.z; var35 < Math.min(var5.size(), this.z + var6.capacity()); var35++) {
                     jc.w var38 = (jc.w)var5.get(var35);
                     ue.c var41 = var6.row(var35);
                     int var19 = var35 == var8 ? 1 : 0;
                     int var20 = var35;
                     int var21 = var19 != 0 && var34.stream().anyMatch(var1x -> var1x.line() == var20) ? 1 : 0;
                     if (var19 != 0) {
                        p.B(var1, var41.x(), var41.y(), var41.width(), var41.height(), 3, ei.R(-14342875, var12));
                     }

                     String var22 = var38.current() ? (var4 == jc.n.T ? "Active" : "Current") : var38.note();
                     int var23 = !var22.isEmpty() && var21 == 0 ? uw.i(var22) + 18 : 14;
                     if (var21 != 0) {
                        for (jc.h var25 : var34) {
                           if (var25.line() == var35) {
                              var23 = Math.max(var23, var41.x() + var41.width() - var25.box().x() + 8);
                           }
                        }
                     }

                     uw.D(
                        var1,
                        uw.R(var38.name(), var41.width() - var23),
                        var41.x() + 8,
                        var41.y() + 11,
                        ei.R(var38.current() ? ez.H : ez.PH, var12),
                        var38.current() ? uw.r.V : uw.r.n
                     );
                     if (!var22.isEmpty() && var21 == 0) {
                        uw.z(var1, var22, var41.x() + var41.width() - 8 - uw.i(var22), var41.y() + 11, ei.R("Expired".equals(var22) ? -24915 : ez.t, var12));
                     }
                  }

                  if (var5.size() > var6.capacity()) {
                     int var36 = var6.capacity() * 32;
                     int var39 = Math.max(8, var36 * var6.capacity() / var5.size());
                     int var42 = (var36 - var39) * this.z / (var5.size() - var6.capacity());
                     gu.Z(var1, var14.x() + var14.width() - 3, var14.y() + 6 + var42, 1, var39, ei.R(ez.PH, var12));
                  }

                  if (var6.statusHeight() > 0) {
                     z(var1, var15, var14.x() + 10, var6.footer().y() - var6.statusHeight() + 1, var14.width() - 20, 2, this.V(var4) ? -24915 : ez.t, var12);
                  }

                  gu.Z(var1, var14.x() + 6, var6.footer().y() - 3, var14.width() - 12, 1, ei.R(-13619152, var12));
               } else if (!q(var4)) {
                  if (var4 == jc.n.J) {
                     z(var1, "Microsoft sign-in", var14.x() + 10, var14.y() + 12, var14.width() - 20, 1, ez.H, var12);
                     z(var1, "microsoft.com/link", var14.x() + 10, var14.y() + 30, var14.width() - 20, 1, ez.PH, var12);
                     uw.N(var1, this.y.code(), var14.x() + var14.width() / 2, var14.y() + 49, ei.R(ez.H, var12));
                     z(var1, "Approve only the code shown here.", var14.x() + 10, var14.y() + 69, var14.width() - 20, 2, ez.PH, var12);
                  } else if (var4 == jc.n.Z) {
                     z(var1, "Start a new saved list?", var14.x() + 10, var14.y() + 12, var14.width() - 20, 1, ez.H, var12);
                     z(
                        var1,
                        "The old encrypted file will be kept for recovery. Add your accounts again.",
                        var14.x() + 10,
                        var14.y() + 34,
                        var14.width() - 20,
                        3,
                        ez.PH,
                        var12
                     );
                  } else {
                     z(
                        var1,
                        this.y.status().isBlank() ? "Loading saved accounts..." : this.y.status(),
                        var14.x() + 10,
                        var14.y() + 12,
                        var14.width() - 20,
                        Math.max(1, (var6.footer().y() - var14.y() - (var4 == jc.n.M ? 46 : 18)) / 10),
                        this.y.error() ? -24915 : ez.PH,
                        var12
                     );
                  }
               } else {
                  this.B(var6, var4);
                  int var16 = var4 == jc.n.Y ? 1 : 0;
                  z(
                     var1,
                     var16 != 0 ? "Sign in with a token" : "Add a proxy for server traffic",
                     var14.x() + 10,
                     var14.y() + 10,
                     var14.width() - 20,
                     1,
                     ez.H,
                     var12
                  );
                  if (var16 != 0) {
                     z(
                        var1,
                        "Paste a Minecraft access token. It is checked with Minecraft and saved encrypted until it expires.",
                        var14.x() + 10,
                        var14.y() + 24,
                        var14.width() - 20,
                        2,
                        ez.PH,
                        var12
                     );
                  }

                  this.K(var10);

                  for (lo var18 : this.B(var4)) {
                     var18.render(var1, var2, var3);
                  }

                  if (!var15.isBlank()) {
                     z(var1, var15, var14.x() + 10, var6.footer().y() - 4 - 20, var14.width() - 20, 2, this.V(var4) ? -24915 : ez.t, var12);
                  }

                  gu.Z(var1, var14.x() + 6, var6.footer().y() - 3, var14.width() - 12, 1, ei.R(-13619152, var12));
               }

               for (int var37 = 0; var37 < var34.size(); var37++) {
                  jc.h var40 = (jc.h)var34.get(var37);
                  ue.c var43 = var40.box();
                  int var44 = this.l && this.n(var4, var34, var10, var37) ? 1 : 0;
                  int var45 = !this.l && H(var43, var2, var3) ? 1 : 0;
                  if (var40.selected() || var44 != 0 || var45 != 0) {
                     p.B(var1, var43.x(), var43.y(), var43.width(), var43.height(), 3, ei.R(var40.selected() ? -13882324 : -13421773, var12));
                  }

                  if (var44 != 0) {
                     p.a(var1, var43.x(), var43.y(), var43.width(), var43.height(), 3, ei.R(ez.PH, var12));
                  }

                  int var46 = uw.i(var40.label());
                  float var47 = n(var46, var43.width() - 12);
                  var1.method_51448().pushMatrix();

                  try {
                     var1.method_51448().translate(var43.x() + (var43.width() - var46 * var47) / 2.0F, var43.y() + (var43.height() - 9.0F * var47) / 2.0F);
                     var1.method_51448().scale(var47, var47);
                     uw.z(var1, var40.label(), 0, 0, ei.R(var40.destructive() ? -24915 : (!var40.selected() && var45 == 0 && var44 == 0 ? ez.PH : ez.H), var12));
                  } finally {
                     var1.method_51448().popMatrix();
                  }
               }
            } finally {
               var1.method_44380();
            }
         }
      }
   }

   private boolean n(jc.n var1, List<jc.h> var2, List<Object> var3, int var4) {
      if (!q(var1)) {
         if (!e(var1)) {
            return this.D == var4;
         }

         int var5 = ((jc.h)var2.get(var4)).line();
         int var6 = 0;
         if (var5 != this.q) {
            return false;
         }

         for (int var7 = 0; var7 < var4; var7++) {
            if (((jc.h)var2.get(var7)).line() == var5) {
               var6++;
            }
         }

         return var6 == this.D;
      } else {
         return this.h >= 0 && this.h < var3.size() && var3.get(this.h) == var2.get(var4);
      }
   }

   public static float n(int var0, int var1) {
      return Math.min(1.0F, (float)Math.max(1, var1) / Math.max(1, var0));
   }

   private static void z(class_332 var0, String var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      class_310 var9 = class_310.method_1551();
      List var10 = var9.field_1772.method_1728(uw.v(var1, uw.r.n), var4);

      for (int var11 = 0; var11 < Math.min(var5, var10.size()); var11++) {
         var0.method_51430(var9.field_1772, (class_5481)var10.get(var11), var2, var3 + var11 * 10, ei.R(var6, var7), false);
      }
   }

   public boolean C(class_11909 var1) {
      if (!this.W) {
         return false;
      }

      jc.n var2 = this.F();
      List var3 = this.W();
      jc.g var4 = this.V(var2, var3.size());
      if (!H(var4.panel(), var1.comp_4798(), var1.comp_4799())) {
         this.Q();
         return true;
      }

      if (s2.m(var1) != 0) {
         return true;
      }

      this.l = (boolean)0;
      boolean var5 = e(var2);
      int var6 = var5 ? this.b(var4, var3.size(), (int)var1.comp_4798(), (int)var1.comp_4799()) : -1;
      if (var5 && this.P != null) {
         for (int var7 = 0; var7 < var3.size(); var7++) {
            if (((jc.w)var3.get(var7)).id().equals(this.P)) {
               var6 = var7;
            }
         }
      }

      List var11 = this.i(var4, var2, var3, var6);

      for (jc.h var9 : var11) {
         if (H(var9.box(), var1.comp_4798(), var1.comp_4799())) {
            var9.action().run();
            return true;
         }
      }

      if (q(var2)) {
         this.B(var4, var2);
         List var12 = this.s(var2, var11);

         for (lo var10 : this.B(var2)) {
            if (var10.contains(var1.comp_4798(), var1.comp_4799())) {
               this.h = var12.indexOf(var10);
            }
         }

         this.K(var12);
      }

      if (var6 >= 0) {
         this.q = var6;
         this.D = 0;
      }

      return true;
   }

   public boolean L(class_11905 var1) {
      if (!this.W) {
         return false;
      }

      for (lo var3 : this.B(this.F())) {
         if (var3.M) {
            var3.character(var1.comp_4793());
         }
      }

      return true;
   }

   public boolean l(double var1, double var3, double var5) {
      if (!this.W) {
         return false;
      }

      jc.n var7 = this.F();
      List var8 = this.W();
      jc.g var9 = this.V(var7, var8.size());
      if (e(var7) && H(var9.panel(), var1, var3) && var5 != 0.0) {
         this.z = Math.clamp(var9.scroll() + (var5 > 0.0 ? -1 : 1), 0, Math.max(0, var8.size() - var9.capacity()));
         this.P = null;
      }

      return true;
   }

   public boolean L(class_11908 var1) {
      if (!this.W) {
         return false;
      }

      int var2 = s2.i(var1);
      jc.n var3 = this.F();
      if (var2 == 256) {
         if (this.x) {
            this.x = (boolean)0;
         } else if (this.P != null) {
            this.P = null;
         } else if (var3 == jc.n.Y || var3 == jc.n.T) {
            this.Y(jc.u.m);
         } else if (var3 == jc.n.P) {
            this.Y(jc.u.s);
         } else {
            this.Q();
         }

         return true;
      } else {
         this.l = (boolean)1;
         List var4 = this.W();
         jc.g var5 = this.V(var3, var4.size());
         int var6 = (var1.comp_4797() & 1) != 0 ? 1 : 0;
         int var7 = var2 != 257 && var2 != 335 && var2 != 32 ? 0 : 1;
         if (e(var3)) {
            this.q = Math.clamp(this.q, 0, var4.size());
            int var14 = var4.size() + 1;
            int var15 = E(this.i(var5, var3, var4, this.q), this.q);
            if (var2 == 264 || var2 == 265) {
               this.q = Math.floorMod(this.q + (var2 == 265 ? -1 : 1), var14);
               this.D = 0;
               this.P = null;
            } else if (var2 == 258 && var6 == 0) {
               if (this.D < var15 - 1) {
                  this.D++;
               } else {
                  this.q = Math.floorMod(this.q + 1, var14);
                  this.D = 0;
                  this.P = null;
               }
            } else if (var2 == 258) {
               if (this.D > 0) {
                  this.D--;
               } else {
                  this.q = Math.floorMod(this.q - 1, var14);
                  this.P = null;
                  this.D = Math.max(0, E(this.i(var5, var3, var4, this.q), this.q) - 1);
               }
            } else if (var2 == 268) {
               this.q = 0;
               this.D = 0;
            } else if (var2 == 269) {
               this.q = var4.size();
               this.D = 0;
            } else if (var2 == 263 || var2 == 262) {
               this.D = Math.clamp(this.D + (var2 == 262 ? 1 : -1), 0, Math.max(0, var15 - 1));
            }

            if (this.q < var4.size()) {
               this.z = Math.clamp(
                  this.q < this.z ? this.q : (this.q >= this.z + var5.capacity() ? this.q - var5.capacity() + 1 : this.z),
                  0,
                  Math.max(0, var4.size() - var5.capacity())
               );
            }

            if (var7 != 0) {
               var5 = this.V(var3, var4.size());
               int var16 = 0;

               for (jc.h var12 : this.i(var5, var3, var4, this.q)) {
                  if (var12.line() == this.q && var16++ == this.D) {
                     var12.action().run();
                     break;
                  }
               }
            }

            return true;
         } else {
            List var8 = this.i(var5, var3, var4, -1);
            if (q(var3)) {
               List var9 = this.s(var3, var8);
               this.h = Math.clamp(this.h, 0, var9.size() - 1);
               Object var10 = var9.get(this.h);
               if (var2 == 258) {
                  this.h = Math.floorMod(this.h + (var6 != 0 ? -1 : 1), var9.size());
                  this.K(var9);
                  return true;
               } else if (var10 instanceof lo var17) {
                  var17.M = (boolean)1;
                  var17.key(var2);
                  return true;
               } else {
                  if (var2 != 264 && var2 != 262 && var2 != 265 && var2 != 263) {
                     if (var7 != 0 && var10 instanceof jc.h var11) {
                        var11.action().run();
                     }
                  } else {
                     this.h = Math.floorMod(this.h + (var2 != 265 && var2 != 263 ? 1 : -1), var9.size());
                     this.K(var9);
                  }

                  return true;
               }
            } else {
               if (var2 == 258 || var2 == 264 || var2 == 265) {
                  this.D = Math.floorMod(this.D + (var2 != 265 && var6 == 0 ? 1 : -1), Math.max(1, var8.size()));
               }

               if (var7 != 0 && this.D >= 0 && this.D < var8.size()) {
                  ((jc.h)var8.get(this.D)).action().run();
               }

               return true;
            }
         }
      }
   }

   private static int E(List<jc.h> var0, int var1) {
      int var2 = 0;

      for (jc.h var4 : var0) {
         if (var4.line() == var1) {
            var2++;
         }
      }

      return var2;
   }

   public record g(ue.c panel, ue.c footer, int capacity, int scroll, int statusHeight) {
      public ue.c row(int var1) {
         return new ue.c(this.panel.x() + 6, this.panel.y() + 6 + (var1 - this.scroll) * 32, this.panel.width() - 12, 32);
      }
   }

   private record h(ue.c box, String label, Runnable action, boolean destructive, boolean selected, int line) {
   }

   public enum n {
      r,
      F,
      J,
      M,
      Z,
      Y,
      T,
      P;
   }

   private enum u {
      m,
      Z,
      s,
      H;
   }

   public record w(UUID id, String name, boolean current, boolean saved, boolean launcher, String note) {
   }
}

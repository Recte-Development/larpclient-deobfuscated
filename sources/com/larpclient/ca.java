package com.larpclient;

import com.larpclient.account.AccountVault;
import com.larpclient.account.MicrosoftAccounts;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.util.FormattedCharSequence;

public final class ca {
   static final UUID L = new UUID(0L, 0L);
   private static final int p = -2;
   private static final int M = 20;
   private final Screen b;
   private final MicrosoftAccounts H = MicrosoftAccounts.INSTANCE;
   private final ef f = new ef(0.0);
   private final ur a;
   private final ur v;
   private final ur e;
   private final ur c;
   private p5.w w = new p5.w(8, 8, 120, 22);
   private ca.n F = ca.n.O;
   private oe.k Y = oe.k.x;
   private boolean y;
   private boolean o;
   private boolean n;
   private int O;
   private int h;
   private int A;
   private int r;
   private int s;
   private int i = Integer.MIN_VALUE;
   private int N;
   private String K = "";
   private UUID S;
   private User P;

   public ca(Screen var1) {
      this.b = var1;
      this.a = new ur("Paste access token", var1x -> this.K = "", 8192).t().R(this::g);
      this.v = new ur("Proxy host:port", this::k, 300).R(this::p);
      this.e = new ur("Username (optional)", var1x -> this.K = "", 255).R(this::p);
      this.c = new ur("Password (optional)", var1x -> this.K = "", 255).t().R(this::p);
   }

   public boolean O() {
      return this.y;
   }

   public void w(p5.w var1) {
      this.w = var1;
   }

   public void D() {
      if (this.y) {
         this.j();
      } else {
         this.y = (boolean)1;
         this.n = (boolean)0;
         this.S = null;
         this.o = (boolean)0;
         this.h = this.A = 0;
         this.F = ca.n.O;
         this.B();
         this.P = Minecraft.getInstance().getUser();
         this.f.Q(0.0);
         this.H.open(this.b);
      }
   }

   public void j() {
      if (this.y) {
         this.H.cancel(this.b);
      }

      this.y = (boolean)0;
      this.n = (boolean)0;
      this.S = null;
      this.F = ca.n.O;
      this.B();
   }

   public static List<ca.e> Q(User var0, User var1, List<AccountVault.Entry> var2, boolean var3) {
      LinkedHashMap var4 = new LinkedHashMap();
      AccountVault.Entry var5 = var2.stream().filter(var1x -> var1x.id().equals(var0.getProfileId())).findFirst().orElse(null);
      var4.put(var0.getProfileId(), new ca.e(var0.getProfileId(), var0.getName(), true, var5 != null, false, var5 == null ? "" : K(var5)));

      for (AccountVault.Entry var7 : var2) {
         var4.putIfAbsent(var7.id(), new ca.e(var7.id(), var7.name(), false, true, false, K(var7)));
      }

      if (var3 && var1 != null) {
         var4.putIfAbsent(var1.getProfileId(), new ca.e(var1.getProfileId(), var1.getName(), false, false, true, ""));
      }

      return List.copyOf(var4.values());
   }

   public static List<ca.e> C(List<oe> var0, UUID var1) {
      ArrayList var2 = new ArrayList();
      var2.add(new ca.e(L, "No proxy", var1 == null, false, false, "Direct"));

      for (oe var4 : var0) {
         var2.add(new ca.e(var4.id(), var4.label(), var4.id().equals(var1), true, false, var4.authenticated() ? "Login" : ""));
      }

      return List.copyOf(var2);
   }

   private static String K(AccountVault.Entry var0) {
      if (!var0.token()) {
         return "";
      }

      Instant var1 = var0.tokenExpiry();
      return var1 != null && var1.isBefore(Instant.now()) ? "Expired" : "Token";
   }

   private List<ca.e> U() {
      if (this.F == ca.n.E) {
         return C(this.H.proxies(), this.H.activeProxyId());
      }

      Minecraft var1 = Minecraft.getInstance();
      return Q(var1.getUser(), this.H.launcher(), this.H.entries(), this.H.canReturn(var1));
   }

   private ca.q n() {
      if (this.n) {
         return ca.q.b;
      }

      if (this.H.busy()) {
         return this.H.code().isBlank() ? ca.q.c : ca.q.K;
      }

      if (!this.H.unlocked()) {
         return ca.q.H;
      }

      return switch (this.F) {
         case O -> ca.q.m;
         case o -> ca.q.O;
         case E -> ca.q.r;
         case c -> ca.q.W;
      };
   }

   private static boolean x(ca.q var0) {
      return var0 == ca.q.m || var0 == ca.q.r;
   }

   private static boolean q(ca.q var0) {
      return var0 == ca.q.O || var0 == ca.q.W;
   }

   public static ca.m I(int var0, int var1, p5.w var2, int var3, int var4, ca.q var5, boolean var6) {
      int var7 = Math.max(1, Math.min(280, var0 - 16));
      int var8 = x(var5) && var6 ? 28 : 0;

      int var9 = switch (var5) {
         case m, r -> Math.min(6, Math.max(1, var3)) * 32 + 46 + var8;
         case c -> 94;
         case K -> 166;
         case H -> 150;
         case b -> 124;
         case O -> 158;
         case W -> 200;
      };
      int var10 = Math.clamp(var2.x() + var2.width() / 2 - var7 / 2, 8, Math.max(8, var0 - var7 - 8));
      int var11 = var2.y() + var2.height() + 4;
      int var12 = x(var5) ? 78 + var8 : var9;
      int var13 = var1 - 8 - var11;
      int var14 = var2.y() - 12;
      int var15 = var13 < var12 && var14 > var13 ? 1 : 0;
      int var16 = var15 != 0 ? var14 : var13;
      int var17 = Math.max(1, Math.min(Math.min(var9, Math.max(var12, var16)), var1 - 16));
      int var18 = var15 != 0 ? var2.y() - var17 - 4 : var11;
      int var19 = Math.clamp(var18, 8, Math.max(8, var1 - var17 - 8));
      int var20 = Math.max(1, (var17 - 46 - var8) / 32);
      return new ca.m(
         new p5.w(var10, var19, var7, var17),
         new p5.w(var10 + 6, var19 + var17 - 34, var7 - 12, 28),
         var20,
         Math.clamp(var4, 0, Math.max(0, var3 - var20)),
         var8
      );
   }

   public static p5.w O(ca.m var0, ca.q var1, int var2) {
      p5.w var3 = var0.panel();
      int var4 = var1 == ca.q.W ? 4 : 1;
      int var5 = var3.y() + (var1 == ca.q.O ? 50 : 26);
      int var6 = var0.footer().y() - 4 - 20;
      int var7 = Math.max(12, Math.min(26, (var6 - var5) / var4));
      return new p5.w(var3.x() + 10, var5 + var2 * var7, var3.width() - 20, Math.max(10, Math.min(20, var7 - 4)));
   }

   private ca.m W(ca.q var1, int var2) {
      return I(this.b.width, this.b.height, this.w, var2, this.O, var1, !this.t(var1).isBlank());
   }

   private static boolean U(p5.w var0, double var1, double var3) {
      return var1 >= var0.x() && var3 >= var0.y() && var1 < var0.x() + var0.width() && var3 < var0.y() + var0.height();
   }

   private String t(ca.q var1) {
      if (q(var1)) {
         return !this.K.isEmpty() ? this.K : (this.H.error() ? this.H.status() : "");
      } else {
         return var1 == ca.q.r && this.H.status().isBlank() && xo.x() != null ? xo.x() : this.H.status();
      }
   }

   private boolean V(ca.q var1) {
      if (q(var1)) {
         return true;
      } else {
         return var1 == ca.q.r && this.H.status().isBlank() ? xo.x() != null : this.H.error();
      }
   }

   private static boolean Y() {
      return xo.y() != null || xo.x() != null;
   }

   private static String k() {
      return xo.y() != null ? "Proxy: On" : (xo.x() != null ? "Proxy: Paused" : "Proxy: Off");
   }

   private int H(ca.m var1, int var2, int var3, int var4) {
      for (int var5 = var1.scroll(); var5 < Math.min(var2, var1.scroll() + var1.capacity()); var5++) {
         if (U(var1.row(var5), var3, var4)) {
            return var5;
         }
      }

      return -1;
   }

   private static List<p5.w> p(p5.w var0, int var1) {
      int var2 = (var0.width() - 4 * (var1 - 1)) / var1;
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < var1; var4++) {
         var3.add(
            new p5.w(var0.x() + var4 * (var2 + 4), var0.y(), var4 == var1 - 1 ? var0.x() + var0.width() - (var0.x() + var4 * (var2 + 4)) : var2, var0.height())
         );
      }

      return var3;
   }

   private List<ca.g> w(ca.m var1, ca.q var2, List<ca.e> var3, int var4) {
      ArrayList var5 = new ArrayList();
      p5.w var6 = var1.footer();
      p5.w var7 = new p5.w(var6.x(), var6.y() - 30, var6.width(), 24);
      switch (var2) {
         case m:
         case r:
            int var17 = var2 == ca.q.r ? 1 : 0;
            if (var4 >= var1.scroll() && var4 < Math.min(var3.size(), var1.scroll() + var1.capacity())) {
               ca.e var20 = (ca.e)var3.get(var4);
               p5.w var23 = var1.row(var4);
               int var24 = var23.x() + var23.width() - 4;
               if (var20.id().equals(this.S)) {
                  var5.add(new ca.g(new p5.w(var24 - 124, var23.y() + 4, 64, 24), "Remove?", () -> {
                     this.S = null;
                     if (var17) {
                        this.H.removeProxy(this.b, var20.id());
                     } else {
                        this.H.remove(this.b, var20.id());
                     }
                  }, true, false, var4));
                  var5.add(new ca.g(new p5.w(var24 - 56, var23.y() + 4, 56, 24), "Cancel", () -> this.S = null, false, false, var4));
               } else {
                  if (!var20.current()) {
                     var5.add(new ca.g(new p5.w(var24 - (var20.saved() ? 124 : 56), var23.y() + 4, 56, 24), var17 != 0 ? "Use" : "Switch", () -> {
                        if (var17) {
                           if (var20.id().equals(L)) {
                              this.H.proxyOff(this.b);
                           } else {
                              this.H.useProxy(this.b, var20.id());
                           }
                        } else if (var20.launcher()) {
                           this.H.returnToLauncher(this.b);
                        } else {
                           this.H.switchTo(this.b, var20.id());
                        }
                     }, false, false, var4));
                  }

                  if (var20.saved()) {
                     var5.add(new ca.g(new p5.w(var24 - 64, var23.y() + 4, 64, 24), "Remove", () -> this.S = var20.id(), true, false, var4));
                  }
               }
            }

            if (var17 != 0) {
               List var21 = p(var6, 2);
               var5.add(new ca.g((p5.w)var21.get(0), "+ Add proxy", () -> this.j(ca.n.c), false, false, var3.size()));
               var5.add(new ca.g((p5.w)var21.get(1), "Back", () -> this.j(ca.n.O), false, false, var3.size()));
            } else {
               List var22 = p(var6, 3);
               var5.add(new ca.g((p5.w)var22.get(0), "+ Microsoft", () -> {
                  this.S = null;
                  this.H.add(this.b);
               }, false, false, var3.size()));
               var5.add(new ca.g((p5.w)var22.get(1), "+ Token", () -> this.j(ca.n.o), false, false, var3.size()));
               var5.add(new ca.g((p5.w)var22.get(2), k(), () -> this.j(ca.n.E), false, Y(), var3.size()));
            }
            break;
         case c:
            var5.add(new ca.g(var6, "Cancel", () -> this.H.cancel(this.b), false, false, 0));
            break;
         case K:
            List var16 = p(var7, 2);
            var5.add(new ca.g((p5.w)var16.get(0), "Open Microsoft", this.H::openBrowser, false, false, 0));
            var5.add(new ca.g((p5.w)var16.get(1), "Copy code", this.H::copyCode, false, false, 0));
            var5.add(new ca.g(var6, "Cancel", () -> this.H.cancel(this.b), false, false, 0));
            break;
         case H:
            List var15 = p(var7, 2);
            var5.add(new ca.g((p5.w)var15.get(0), "Retry", () -> this.H.open(this.b), false, false, 0));
            if (this.H.exists()) {
               var5.add(new ca.g((p5.w)var15.get(1), "Start new list", () -> this.n = (boolean)1, false, false, 0));
            }

            if (Y()) {
               List var19 = p(var6, 2);
               var5.add(new ca.g((p5.w)var19.get(0), "Proxy off", () -> this.H.proxyOff(this.b), false, false, 0));
               var5.add(new ca.g((p5.w)var19.get(1), "Close", this::j, false, false, 0));
            } else {
               var5.add(new ca.g(var6, "Close", this::j, false, false, 0));
            }
            break;
         case b:
            List var14 = p(var6, 2);
            var5.add(new ca.g((p5.w)var14.get(0), "Start new list", () -> {
               this.n = (boolean)0;
               this.H.archive(this.b);
            }, true, false, 0));
            var5.add(new ca.g((p5.w)var14.get(1), "Cancel", () -> this.n = (boolean)0, false, false, 0));
            break;
         case O:
            List var13 = p(var6, 3);
            var5.add(new ca.g((p5.w)var13.get(0), "Paste", this::w, false, false, 0));
            var5.add(new ca.g((p5.w)var13.get(1), "Sign in", this::g, false, false, 0));
            var5.add(new ca.g((p5.w)var13.get(2), "Cancel", () -> this.j(ca.n.O), false, false, 0));
            break;
         case W:
            List var8 = p(O(var1, var2, 0), 3);

            for (oe.k var12 : oe.k.values()) {
               var5.add(new ca.g((p5.w)var8.get(var12.ordinal()), var12.I, () -> {
                  this.Y = var12;
                  this.K = "";
               }, false, var12 == this.Y, -2));
            }

            List var18 = p(var6, 2);
            var5.add(new ca.g((p5.w)var18.get(0), "Save and use", this::p, false, false, 0));
            var5.add(new ca.g((p5.w)var18.get(1), "Cancel", () -> this.j(ca.n.E), false, false, 0));
      }

      return var5;
   }

   private List<ur> n(ca.q var1) {
      return var1 == ca.q.O ? List.of(this.a) : (var1 == ca.q.W ? List.of(this.v, this.e, this.c) : List.of());
   }

   private List<Object> G(ca.q var1, List<ca.g> var2) {
      ArrayList var3 = new ArrayList();

      for (ca.g var5 : var2) {
         if (var5.line() == -2) {
            var3.add(var5);
         }
      }

      var3.addAll(this.n(var1));

      for (ca.g var7 : var2) {
         if (var7.line() != -2) {
            var3.add(var7);
         }
      }

      return var3;
   }

   private void K(ca.m var1, ca.q var2) {
      List var3 = this.n(var2);

      for (int var4 = 0; var4 < var3.size(); var4++) {
         p5.w var5 = O(var1, var2, var2 == ca.q.W ? var4 + 1 : var4);
         ((ur)var3.get(var4)).bounds(var5.x(), var5.y(), var5.width());
         ((ur)var3.get(var4)).W = var5.height();
      }
   }

   private void A(List<Object> var1) {
      for (ur var3 : List.of(this.a, this.v, this.e, this.c)) {
         var3.l = (boolean)(this.r >= 0 && this.r < var1.size() && var1.get(this.r) == var3 ? 1 : 0);
      }
   }

   private void j(ca.n var1) {
      this.F = var1;
      this.S = null;
      this.O = 0;
      this.h = 0;
      this.A = 0;
      this.K = "";
      this.H.clearMessage();
      if (var1 == ca.n.O || var1 == ca.n.E) {
         this.B();
      }

      ca.q var2 = this.n();
      if (q(var2)) {
         List var3 = this.G(var2, this.w(this.W(var2, 0), var2, List.of(), -1));
         this.r = var3.indexOf(this.n(var2).getFirst());
         this.A(var3);
      }
   }

   private void B() {
      for (ur var2 : List.of(this.a, this.v, this.e, this.c)) {
         var2.d = "";
         var2.blur();
      }

      this.s = 0;
      this.r = 0;
      this.K = "";
      this.Y = oe.k.x;
   }

   private void g() {
      if (this.n() == ca.q.O) {
         this.K = "";
         this.H.addToken(this.b, this.a.d, () -> {
            this.a.d = "";
            this.j(ca.n.O);
         });
      }
   }

   private void w() {
      String var1 = Minecraft.getInstance().keyboardHandler.getClipboard();
      this.a.d = "";
      this.a.l = (boolean)1;
      this.a.f(var1);
      ca.q var2 = this.n();
      List var3 = this.G(var2, this.w(this.W(var2, 0), var2, List.of(), -1));
      this.r = var3.indexOf(this.a);
      this.A(var3);
   }

   private void p() {
      if (this.n() == ca.q.W) {
         oe var1;
         try {
            var1 = oe.parse(this.Y, this.v.d, this.e.d, this.c.d);
         } catch (IllegalArgumentException var3) {
            this.K = var3.getMessage();
            return;
         }

         this.K = "";
         this.H.saveProxy(this.b, var1, () -> this.j(ca.n.E));
      }
   }

   private void k(String var1) {
      this.K = "";
      int var2 = this.s;
      this.s = var1.length();
      if (var1.length() - var2 > 1) {
         oe.s var3 = oe.split(var1, this.Y);
         if (var3 != null) {
            this.Y = var3.type();
            this.v.d = var3.address();
            this.s = this.v.d.length();
            if (!var3.username().isEmpty() || !var3.password().isEmpty()) {
               this.e.d = var3.username();
               this.c.d = var3.password();
            }
         }
      }
   }

   public void s(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.y) {
         if (!this.H.busy() && Minecraft.getInstance().getUser() != this.P) {
            this.j();
         } else {
            ca.q var4 = this.n();
            List var5 = this.U();
            ca.m var6 = this.W(var4, var5.size());
            this.O = var6.scroll();
            if (var2 != this.i || var3 != this.N) {
               this.o = (boolean)0;
               this.i = var2;
               this.N = var3;
            }

            boolean var7 = x(var4);
            int var8 = !var7 ? -1 : (this.o ? this.h : this.H(var6, var5.size(), var2, var3));
            if (var7 && this.S != null) {
               for (int var9 = 0; var9 < var5.size(); var9++) {
                  if (((ca.e)var5.get(var9)).id().equals(this.S)) {
                     var8 = var9;
                  }
               }
            }

            List var34 = this.w(var6, var4, var5, var8);
            List var10 = q(var4) ? this.G(var4, var34) : List.of();
            boolean var11 = LarpClient.F().f().t().V;
            double var12 = var11 ? this.f.m(1.0, 140) : 1.0;
            p5.w var14 = var6.panel();
            String var15 = this.t(var4);
            var1.nextStratum();
            pn.b(var1, var14.x(), var14.y(), var14.width(), var14.height(), 5, _3.U(-99347436, var12));
            pn.D(var1, var14.x(), var14.y(), var14.width(), var14.height(), 5, _3.U(-13290187, var12));
            var1.enableScissor(var14.x(), var14.y(), var14.x() + var14.width(), var14.y() + var14.height());

            try {
               if (var7) {
                  for (int var35 = this.O; var35 < Math.min(var5.size(), this.O + var6.capacity()); var35++) {
                     ca.e var38 = (ca.e)var5.get(var35);
                     p5.w var41 = var6.row(var35);
                     int var19 = var35 == var8 ? 1 : 0;
                     int var20 = var35;
                     int var21 = var19 != 0 && var34.stream().anyMatch(var1x -> var1x.line() == var20) ? 1 : 0;
                     if (var19 != 0) {
                        pn.b(var1, var41.x(), var41.y(), var41.width(), var41.height(), 3, _3.U(-14342875, var12));
                     }

                     String var22 = var38.current() ? (var4 == ca.q.r ? "Active" : "Current") : var38.note();
                     int var23 = !var22.isEmpty() && var21 == 0 ? k6.x(var22) + 18 : 14;
                     if (var21 != 0) {
                        for (ca.g var25 : var34) {
                           if (var25.line() == var35) {
                              var23 = Math.max(var23, var41.x() + var41.width() - var25.box().x() + 8);
                           }
                        }
                     }

                     k6.r(
                        var1,
                        k6.n(var38.name(), var41.width() - var23),
                        var41.x() + 8,
                        var41.y() + 11,
                        _3.U(var38.current() ? com.larpclient.f.J : com.larpclient.f.s, var12),
                        var38.current() ? k6.v.B : k6.v.K
                     );
                     if (!var22.isEmpty() && var21 == 0) {
                        k6.G(
                           var1,
                           var22,
                           var41.x() + var41.width() - 8 - k6.x(var22),
                           var41.y() + 11,
                           _3.U("Expired".equals(var22) ? -24915 : com.larpclient.f.G, var12)
                        );
                     }
                  }

                  if (var5.size() > var6.capacity()) {
                     int var36 = var6.capacity() * 32;
                     int var39 = Math.max(8, var36 * var6.capacity() / var5.size());
                     int var42 = (var36 - var39) * this.O / (var5.size() - var6.capacity());
                     c7.s(var1, var14.x() + var14.width() - 3, var14.y() + 6 + var42, 1, var39, _3.U(com.larpclient.f.s, var12));
                  }

                  if (var6.statusHeight() > 0) {
                     K(
                        var1,
                        var15,
                        var14.x() + 10,
                        var6.footer().y() - var6.statusHeight() + 1,
                        var14.width() - 20,
                        2,
                        this.V(var4) ? -24915 : com.larpclient.f.G,
                        var12
                     );
                  }

                  c7.s(var1, var14.x() + 6, var6.footer().y() - 3, var14.width() - 12, 1, _3.U(-13619152, var12));
               } else if (!q(var4)) {
                  if (var4 == ca.q.K) {
                     K(var1, "Microsoft sign-in", var14.x() + 10, var14.y() + 12, var14.width() - 20, 1, com.larpclient.f.J, var12);
                     K(var1, "microsoft.com/link", var14.x() + 10, var14.y() + 30, var14.width() - 20, 1, com.larpclient.f.s, var12);
                     k6.M(var1, this.H.code(), var14.x() + var14.width() / 2, var14.y() + 49, _3.U(com.larpclient.f.J, var12));
                     K(var1, "Approve only the code shown here.", var14.x() + 10, var14.y() + 69, var14.width() - 20, 2, com.larpclient.f.s, var12);
                  } else if (var4 == ca.q.b) {
                     K(var1, "Start a new saved list?", var14.x() + 10, var14.y() + 12, var14.width() - 20, 1, com.larpclient.f.J, var12);
                     K(
                        var1,
                        "The old encrypted file will be kept for recovery. Add your accounts again.",
                        var14.x() + 10,
                        var14.y() + 34,
                        var14.width() - 20,
                        3,
                        com.larpclient.f.s,
                        var12
                     );
                  } else {
                     K(
                        var1,
                        this.H.status().isBlank() ? "Loading saved accounts..." : this.H.status(),
                        var14.x() + 10,
                        var14.y() + 12,
                        var14.width() - 20,
                        Math.max(1, (var6.footer().y() - var14.y() - (var4 == ca.q.H ? 46 : 18)) / 10),
                        this.H.error() ? -24915 : com.larpclient.f.s,
                        var12
                     );
                  }
               } else {
                  this.K(var6, var4);
                  int var16 = var4 == ca.q.O ? 1 : 0;
                  K(
                     var1,
                     var16 != 0 ? "Sign in with a token" : "Add a proxy for server traffic",
                     var14.x() + 10,
                     var14.y() + 10,
                     var14.width() - 20,
                     1,
                     com.larpclient.f.J,
                     var12
                  );
                  if (var16 != 0) {
                     K(
                        var1,
                        "Paste a Minecraft access token. It is checked with Minecraft and saved encrypted until it expires.",
                        var14.x() + 10,
                        var14.y() + 24,
                        var14.width() - 20,
                        2,
                        com.larpclient.f.s,
                        var12
                     );
                  }

                  this.A(var10);

                  for (ur var18 : this.n(var4)) {
                     var18.render(var1, var2, var3);
                  }

                  if (!var15.isBlank()) {
                     K(var1, var15, var14.x() + 10, var6.footer().y() - 4 - 20, var14.width() - 20, 2, this.V(var4) ? -24915 : com.larpclient.f.G, var12);
                  }

                  c7.s(var1, var14.x() + 6, var6.footer().y() - 3, var14.width() - 12, 1, _3.U(-13619152, var12));
               }

               for (int var37 = 0; var37 < var34.size(); var37++) {
                  ca.g var40 = (ca.g)var34.get(var37);
                  p5.w var43 = var40.box();
                  int var44 = this.o && this.m(var4, var34, var10, var37) ? 1 : 0;
                  int var45 = !this.o && U(var43, var2, var3) ? 1 : 0;
                  if (var40.selected() || var44 != 0 || var45 != 0) {
                     pn.b(var1, var43.x(), var43.y(), var43.width(), var43.height(), 3, _3.U(var40.selected() ? -13882324 : -13421773, var12));
                  }

                  if (var44 != 0) {
                     pn.D(var1, var43.x(), var43.y(), var43.width(), var43.height(), 3, _3.U(com.larpclient.f.s, var12));
                  }

                  int var46 = k6.x(var40.label());
                  float var47 = Y(var46, var43.width() - 12);
                  var1.pose().pushMatrix();

                  try {
                     var1.pose().translate(var43.x() + (var43.width() - var46 * var47) / 2.0F, var43.y() + (var43.height() - 9.0F * var47) / 2.0F);
                     var1.pose().scale(var47, var47);
                     k6.G(
                        var1,
                        var40.label(),
                        0,
                        0,
                        _3.U(var40.destructive() ? -24915 : (!var40.selected() && var45 == 0 && var44 == 0 ? com.larpclient.f.s : com.larpclient.f.J), var12)
                     );
                  } finally {
                     var1.pose().popMatrix();
                  }
               }
            } finally {
               var1.disableScissor();
            }
         }
      }
   }

   private boolean m(ca.q var1, List<ca.g> var2, List<Object> var3, int var4) {
      if (!q(var1)) {
         if (!x(var1)) {
            return this.A == var4;
         }

         int var5 = ((ca.g)var2.get(var4)).line();
         int var6 = 0;
         if (var5 != this.h) {
            return false;
         }

         for (int var7 = 0; var7 < var4; var7++) {
            if (((ca.g)var2.get(var7)).line() == var5) {
               var6++;
            }
         }

         return var6 == this.A;
      } else {
         return this.r >= 0 && this.r < var3.size() && var3.get(this.r) == var2.get(var4);
      }
   }

   public static float Y(int var0, int var1) {
      return Math.min(1.0F, (float)Math.max(1, var1) / Math.max(1, var0));
   }

   private static void K(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      Minecraft var9 = Minecraft.getInstance();
      List var10 = var9.font.split(k6.X(var1, k6.v.K), var4);

      for (int var11 = 0; var11 < Math.min(var5, var10.size()); var11++) {
         var0.text(var9.font, (FormattedCharSequence)var10.get(var11), var2, var3 + var11 * 10, _3.U(var6, var7), false);
      }
   }

   public boolean d(MouseButtonEvent var1) {
      if (!this.y) {
         return false;
      }

      ca.q var2 = this.n();
      List var3 = this.U();
      ca.m var4 = this.W(var2, var3.size());
      if (!U(var4.panel(), var1.x(), var1.y())) {
         this.j();
         return true;
      }

      if (mo.Z(var1) != 0) {
         return true;
      }

      this.o = (boolean)0;
      boolean var5 = x(var2);
      int var6 = var5 ? this.H(var4, var3.size(), (int)var1.x(), (int)var1.y()) : -1;
      if (var5 && this.S != null) {
         for (int var7 = 0; var7 < var3.size(); var7++) {
            if (((ca.e)var3.get(var7)).id().equals(this.S)) {
               var6 = var7;
            }
         }
      }

      List var11 = this.w(var4, var2, var3, var6);

      for (ca.g var9 : var11) {
         if (U(var9.box(), var1.x(), var1.y())) {
            var9.action().run();
            return true;
         }
      }

      if (q(var2)) {
         this.K(var4, var2);
         List var12 = this.G(var2, var11);

         for (ur var10 : this.n(var2)) {
            if (var10.contains(var1.x(), var1.y())) {
               this.r = var12.indexOf(var10);
            }
         }

         this.A(var12);
      }

      if (var6 >= 0) {
         this.h = var6;
         this.A = 0;
      }

      return true;
   }

   public boolean A(CharacterEvent var1) {
      if (!this.y) {
         return false;
      }

      for (ur var3 : this.n(this.n())) {
         if (var3.l) {
            var3.character(var1.codepoint());
         }
      }

      return true;
   }

   public boolean o(double var1, double var3, double var5) {
      if (!this.y) {
         return false;
      }

      ca.q var7 = this.n();
      List var8 = this.U();
      ca.m var9 = this.W(var7, var8.size());
      if (x(var7) && U(var9.panel(), var1, var3) && var5 != 0.0) {
         this.O = Math.clamp(var9.scroll() + (var5 > 0.0 ? -1 : 1), 0, Math.max(0, var8.size() - var9.capacity()));
         this.S = null;
      }

      return true;
   }

   public boolean X(KeyEvent var1) {
      if (!this.y) {
         return false;
      }

      int var2 = mo.O(var1);
      ca.q var3 = this.n();
      if (var2 == 256) {
         if (this.n) {
            this.n = (boolean)0;
         } else if (this.S != null) {
            this.S = null;
         } else if (var3 == ca.q.O || var3 == ca.q.r) {
            this.j(ca.n.O);
         } else if (var3 == ca.q.W) {
            this.j(ca.n.E);
         } else {
            this.j();
         }

         return true;
      } else {
         this.o = (boolean)1;
         List var4 = this.U();
         ca.m var5 = this.W(var3, var4.size());
         int var6 = (var1.modifiers() & 1) != 0 ? 1 : 0;
         int var7 = var2 != 257 && var2 != 335 && var2 != 32 ? 0 : 1;
         if (x(var3)) {
            this.h = Math.clamp(this.h, 0, var4.size());
            int var14 = var4.size() + 1;
            int var15 = M(this.w(var5, var3, var4, this.h), this.h);
            if (var2 == 264 || var2 == 265) {
               this.h = Math.floorMod(this.h + (var2 == 265 ? -1 : 1), var14);
               this.A = 0;
               this.S = null;
            } else if (var2 == 258 && var6 == 0) {
               if (this.A < var15 - 1) {
                  this.A++;
               } else {
                  this.h = Math.floorMod(this.h + 1, var14);
                  this.A = 0;
                  this.S = null;
               }
            } else if (var2 == 258) {
               if (this.A > 0) {
                  this.A--;
               } else {
                  this.h = Math.floorMod(this.h - 1, var14);
                  this.S = null;
                  this.A = Math.max(0, M(this.w(var5, var3, var4, this.h), this.h) - 1);
               }
            } else if (var2 == 268) {
               this.h = 0;
               this.A = 0;
            } else if (var2 == 269) {
               this.h = var4.size();
               this.A = 0;
            } else if (var2 == 263 || var2 == 262) {
               this.A = Math.clamp(this.A + (var2 == 262 ? 1 : -1), 0, Math.max(0, var15 - 1));
            }

            if (this.h < var4.size()) {
               this.O = Math.clamp(
                  this.h < this.O ? this.h : (this.h >= this.O + var5.capacity() ? this.h - var5.capacity() + 1 : this.O),
                  0,
                  Math.max(0, var4.size() - var5.capacity())
               );
            }

            if (var7 != 0) {
               var5 = this.W(var3, var4.size());
               int var16 = 0;

               for (ca.g var12 : this.w(var5, var3, var4, this.h)) {
                  if (var12.line() == this.h && var16++ == this.A) {
                     var12.action().run();
                     break;
                  }
               }
            }

            return true;
         } else {
            List var8 = this.w(var5, var3, var4, -1);
            if (q(var3)) {
               List var9 = this.G(var3, var8);
               this.r = Math.clamp(this.r, 0, var9.size() - 1);
               Object var10 = var9.get(this.r);
               if (var2 == 258) {
                  this.r = Math.floorMod(this.r + (var6 != 0 ? -1 : 1), var9.size());
                  this.A(var9);
                  return true;
               } else if (var10 instanceof ur var17) {
                  var17.l = (boolean)1;
                  var17.key(var2);
                  return true;
               } else {
                  if (var2 != 264 && var2 != 262 && var2 != 265 && var2 != 263) {
                     if (var7 != 0 && var10 instanceof ca.g var11) {
                        var11.action().run();
                     }
                  } else {
                     this.r = Math.floorMod(this.r + (var2 != 265 && var2 != 263 ? 1 : -1), var9.size());
                     this.A(var9);
                  }

                  return true;
               }
            } else {
               if (var2 == 258 || var2 == 264 || var2 == 265) {
                  this.A = Math.floorMod(this.A + (var2 != 265 && var6 == 0 ? 1 : -1), Math.max(1, var8.size()));
               }

               if (var7 != 0 && this.A >= 0 && this.A < var8.size()) {
                  ((ca.g)var8.get(this.A)).action().run();
               }

               return true;
            }
         }
      }
   }

   private static int M(List<ca.g> var0, int var1) {
      int var2 = 0;

      for (ca.g var4 : var0) {
         if (var4.line() == var1) {
            var2++;
         }
      }

      return var2;
   }

   public record e(UUID id, String name, boolean current, boolean saved, boolean launcher, String note) {
   }

   private record g(p5.w box, String label, Runnable action, boolean destructive, boolean selected, int line) {
   }

   public record m(p5.w panel, p5.w footer, int capacity, int scroll, int statusHeight) {
      public p5.w row(int var1) {
         return new p5.w(this.panel.x() + 6, this.panel.y() + 6 + (var1 - this.scroll) * 32, this.panel.width() - 12, 32);
      }
   }

   private enum n {
      O,
      o,
      E,
      c;
   }

   public enum q {
      m,
      c,
      K,
      H,
      b,
      O,
      r,
      W;
   }
}

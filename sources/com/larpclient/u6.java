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

public final class u6 {
   static final UUID k = new UUID(0L, 0L);
   private static final int t = -2;
   private static final int O = 20;
   private final Screen G;
   private final MicrosoftAccounts B = MicrosoftAccounts.INSTANCE;
   private final u7 R = new u7(0.0);
   private final p_ Z;
   private final p_ H;
   private final p_ Q;
   private final p_ S;
   private o6.b q = new o6.b(8, 8, 120, 22);
   private u6.e W = u6.e.B;
   private h4.h r = h4.h.e;
   private boolean o;
   private boolean w;
   private boolean b;
   private int f;
   private int J;
   private int u;
   private int X;
   private int Y;
   private int P = Integer.MIN_VALUE;
   private int d;
   private String C = "";
   private UUID U;
   private User j;

   public u6(Screen var1) {
      this.G = var1;
      this.Z = new p_("Paste access token", var1x -> this.C = "", 8192).h().T(this::J);
      this.H = new p_("Proxy host:port", this::F, 300).T(this::V);
      this.Q = new p_("Username (optional)", var1x -> this.C = "", 255).T(this::V);
      this.S = new p_("Password (optional)", var1x -> this.C = "", 255).h().T(this::V);
   }

   public boolean I() {
      return this.o;
   }

   public void J(o6.b var1) {
      this.q = var1;
   }

   public void a() {
      if (this.o) {
         this.r();
      } else {
         this.o = (boolean)1;
         this.b = (boolean)0;
         this.U = null;
         this.w = (boolean)0;
         this.J = this.u = 0;
         this.W = u6.e.B;
         this.k();
         this.j = Minecraft.getInstance().getUser();
         this.R.c(0.0);
         this.B.open(this.G);
      }
   }

   public void r() {
      if (this.o) {
         this.B.cancel(this.G);
      }

      this.o = (boolean)0;
      this.b = (boolean)0;
      this.U = null;
      this.W = u6.e.B;
      this.k();
   }

   public static List<u6.j> f(User var0, User var1, List<AccountVault.Entry> var2, boolean var3) {
      LinkedHashMap var4 = new LinkedHashMap();
      AccountVault.Entry var5 = var2.stream().filter(var1x -> var1x.id().equals(var0.getProfileId())).findFirst().orElse(null);
      var4.put(var0.getProfileId(), new u6.j(var0.getProfileId(), var0.getName(), true, var5 != null, false, var5 == null ? "" : Y(var5)));

      for (AccountVault.Entry var7 : var2) {
         var4.putIfAbsent(var7.id(), new u6.j(var7.id(), var7.name(), false, true, false, Y(var7)));
      }

      if (var3 && var1 != null) {
         var4.putIfAbsent(var1.getProfileId(), new u6.j(var1.getProfileId(), var1.getName(), false, false, true, ""));
      }

      return List.copyOf(var4.values());
   }

   public static List<u6.j> c(List<h4> var0, UUID var1) {
      ArrayList var2 = new ArrayList();
      var2.add(new u6.j(k, "No proxy", var1 == null, false, false, "Direct"));

      for (h4 var4 : var0) {
         var2.add(new u6.j(var4.id(), var4.label(), var4.id().equals(var1), true, false, var4.authenticated() ? "Login" : ""));
      }

      return List.copyOf(var2);
   }

   private static String Y(AccountVault.Entry var0) {
      if (!var0.token()) {
         return "";
      }

      Instant var1 = var0.tokenExpiry();
      return var1 != null && var1.isBefore(Instant.now()) ? "Expired" : "Token";
   }

   private List<u6.j> Z() {
      if (this.W == u6.e.i) {
         return c(this.B.proxies(), this.B.activeProxyId());
      }

      Minecraft var1 = Minecraft.getInstance();
      return f(var1.getUser(), this.B.launcher(), this.B.entries(), this.B.canReturn(var1));
   }

   private u6.z L() {
      if (this.b) {
         return u6.z.N;
      }

      if (this.B.busy()) {
         return this.B.code().isBlank() ? u6.z.a : u6.z.I;
      }

      if (!this.B.unlocked()) {
         return u6.z.o;
      }

      return switch (this.W) {
         case B -> u6.z.e;
         case U -> u6.z.p;
         case i -> u6.z.v;
         case o -> u6.z.A;
      };
   }

   private static boolean R(u6.z var0) {
      return var0 == u6.z.e || var0 == u6.z.v;
   }

   private static boolean j(u6.z var0) {
      return var0 == u6.z.p || var0 == u6.z.A;
   }

   public static u6.q W(int var0, int var1, o6.b var2, int var3, int var4, u6.z var5, boolean var6) {
      int var7 = Math.max(1, Math.min(280, var0 - 16));
      int var8 = R(var5) && var6 ? 28 : 0;

      int var9 = switch (var5) {
         case e, v -> Math.min(6, Math.max(1, var3)) * 32 + 46 + var8;
         case a -> 94;
         case I -> 166;
         case o -> 150;
         case N -> 124;
         case p -> 158;
         case A -> 200;
      };
      int var10 = Math.clamp(var2.x() + var2.width() / 2 - var7 / 2, 8, Math.max(8, var0 - var7 - 8));
      int var11 = var2.y() + var2.height() + 4;
      int var12 = R(var5) ? 78 + var8 : var9;
      int var13 = var1 - 8 - var11;
      int var14 = var2.y() - 12;
      int var15 = var13 < var12 && var14 > var13 ? 1 : 0;
      int var16 = var15 != 0 ? var14 : var13;
      int var17 = Math.max(1, Math.min(Math.min(var9, Math.max(var12, var16)), var1 - 16));
      int var18 = var15 != 0 ? var2.y() - var17 - 4 : var11;
      int var19 = Math.clamp(var18, 8, Math.max(8, var1 - var17 - 8));
      int var20 = Math.max(1, (var17 - 46 - var8) / 32);
      return new u6.q(
         new o6.b(var10, var19, var7, var17),
         new o6.b(var10 + 6, var19 + var17 - 34, var7 - 12, 28),
         var20,
         Math.clamp(var4, 0, Math.max(0, var3 - var20)),
         var8
      );
   }

   public static o6.b a(u6.q var0, u6.z var1, int var2) {
      o6.b var3 = var0.panel();
      int var4 = var1 == u6.z.A ? 4 : 1;
      int var5 = var3.y() + (var1 == u6.z.p ? 50 : 26);
      int var6 = var0.footer().y() - 4 - 20;
      int var7 = Math.max(12, Math.min(26, (var6 - var5) / var4));
      return new o6.b(var3.x() + 10, var5 + var2 * var7, var3.width() - 20, Math.max(10, Math.min(20, var7 - 4)));
   }

   private u6.q w(u6.z var1, int var2) {
      return W(this.G.width, this.G.height, this.q, var2, this.f, var1, !this.y(var1).isBlank());
   }

   private static boolean J(o6.b var0, double var1, double var3) {
      return var1 >= var0.x() && var3 >= var0.y() && var1 < var0.x() + var0.width() && var3 < var0.y() + var0.height();
   }

   private String y(u6.z var1) {
      if (j(var1)) {
         return !this.C.isEmpty() ? this.C : (this.B.error() ? this.B.status() : "");
      } else {
         return var1 == u6.z.v && this.B.status().isBlank() && _v.u() != null ? _v.u() : this.B.status();
      }
   }

   private boolean V(u6.z var1) {
      if (j(var1)) {
         return true;
      } else {
         return var1 == u6.z.v && this.B.status().isBlank() ? _v.u() != null : this.B.error();
      }
   }

   private static boolean Q() {
      return _v.B() != null || _v.u() != null;
   }

   private static String N() {
      return _v.B() != null ? "Proxy: On" : (_v.u() != null ? "Proxy: Paused" : "Proxy: Off");
   }

   private int W(u6.q var1, int var2, int var3, int var4) {
      for (int var5 = var1.scroll(); var5 < Math.min(var2, var1.scroll() + var1.capacity()); var5++) {
         if (J(var1.row(var5), var3, var4)) {
            return var5;
         }
      }

      return -1;
   }

   private static List<o6.b> X(o6.b var0, int var1) {
      int var2 = (var0.width() - 4 * (var1 - 1)) / var1;
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < var1; var4++) {
         var3.add(
            new o6.b(var0.x() + var4 * (var2 + 4), var0.y(), var4 == var1 - 1 ? var0.x() + var0.width() - (var0.x() + var4 * (var2 + 4)) : var2, var0.height())
         );
      }

      return var3;
   }

   private List<u6.m> I(u6.q var1, u6.z var2, List<u6.j> var3, int var4) {
      ArrayList var5 = new ArrayList();
      o6.b var6 = var1.footer();
      o6.b var7 = new o6.b(var6.x(), var6.y() - 30, var6.width(), 24);
      switch (var2) {
         case e:
         case v:
            int var17 = var2 == u6.z.v ? 1 : 0;
            if (var4 >= var1.scroll() && var4 < Math.min(var3.size(), var1.scroll() + var1.capacity())) {
               u6.j var20 = (u6.j)var3.get(var4);
               o6.b var23 = var1.row(var4);
               int var24 = var23.x() + var23.width() - 4;
               if (var20.id().equals(this.U)) {
                  var5.add(new u6.m(new o6.b(var24 - 124, var23.y() + 4, 64, 24), "Remove?", () -> {
                     this.U = null;
                     if (var17) {
                        this.B.removeProxy(this.G, var20.id());
                     } else {
                        this.B.remove(this.G, var20.id());
                     }
                  }, true, false, var4));
                  var5.add(new u6.m(new o6.b(var24 - 56, var23.y() + 4, 56, 24), "Cancel", () -> this.U = null, false, false, var4));
               } else {
                  if (!var20.current()) {
                     var5.add(new u6.m(new o6.b(var24 - (var20.saved() ? 124 : 56), var23.y() + 4, 56, 24), var17 != 0 ? "Use" : "Switch", () -> {
                        if (var17) {
                           if (var20.id().equals(k)) {
                              this.B.proxyOff(this.G);
                           } else {
                              this.B.useProxy(this.G, var20.id());
                           }
                        } else if (var20.launcher()) {
                           this.B.returnToLauncher(this.G);
                        } else {
                           this.B.switchTo(this.G, var20.id());
                        }
                     }, false, false, var4));
                  }

                  if (var20.saved()) {
                     var5.add(new u6.m(new o6.b(var24 - 64, var23.y() + 4, 64, 24), "Remove", () -> this.U = var20.id(), true, false, var4));
                  }
               }
            }

            if (var17 != 0) {
               List var21 = X(var6, 2);
               var5.add(new u6.m((o6.b)var21.get(0), "+ Add proxy", () -> this.S(u6.e.o), false, false, var3.size()));
               var5.add(new u6.m((o6.b)var21.get(1), "Back", () -> this.S(u6.e.B), false, false, var3.size()));
            } else {
               List var22 = X(var6, 3);
               var5.add(new u6.m((o6.b)var22.get(0), "+ Microsoft", () -> {
                  this.U = null;
                  this.B.add(this.G);
               }, false, false, var3.size()));
               var5.add(new u6.m((o6.b)var22.get(1), "+ Token", () -> this.S(u6.e.U), false, false, var3.size()));
               var5.add(new u6.m((o6.b)var22.get(2), N(), () -> this.S(u6.e.i), false, Q(), var3.size()));
            }
            break;
         case a:
            var5.add(new u6.m(var6, "Cancel", () -> this.B.cancel(this.G), false, false, 0));
            break;
         case I:
            List var16 = X(var7, 2);
            var5.add(new u6.m((o6.b)var16.get(0), "Open Microsoft", this.B::openBrowser, false, false, 0));
            var5.add(new u6.m((o6.b)var16.get(1), "Copy code", this.B::copyCode, false, false, 0));
            var5.add(new u6.m(var6, "Cancel", () -> this.B.cancel(this.G), false, false, 0));
            break;
         case o:
            List var15 = X(var7, 2);
            var5.add(new u6.m((o6.b)var15.get(0), "Retry", () -> this.B.open(this.G), false, false, 0));
            if (this.B.exists()) {
               var5.add(new u6.m((o6.b)var15.get(1), "Start new list", () -> this.b = (boolean)1, false, false, 0));
            }

            if (Q()) {
               List var19 = X(var6, 2);
               var5.add(new u6.m((o6.b)var19.get(0), "Proxy off", () -> this.B.proxyOff(this.G), false, false, 0));
               var5.add(new u6.m((o6.b)var19.get(1), "Close", this::r, false, false, 0));
            } else {
               var5.add(new u6.m(var6, "Close", this::r, false, false, 0));
            }
            break;
         case N:
            List var14 = X(var6, 2);
            var5.add(new u6.m((o6.b)var14.get(0), "Start new list", () -> {
               this.b = (boolean)0;
               this.B.archive(this.G);
            }, true, false, 0));
            var5.add(new u6.m((o6.b)var14.get(1), "Cancel", () -> this.b = (boolean)0, false, false, 0));
            break;
         case p:
            List var13 = X(var6, 3);
            var5.add(new u6.m((o6.b)var13.get(0), "Paste", this::A, false, false, 0));
            var5.add(new u6.m((o6.b)var13.get(1), "Sign in", this::J, false, false, 0));
            var5.add(new u6.m((o6.b)var13.get(2), "Cancel", () -> this.S(u6.e.B), false, false, 0));
            break;
         case A:
            List var8 = X(a(var1, var2, 0), 3);

            for (h4.h var12 : h4.h.values()) {
               var5.add(new u6.m((o6.b)var8.get(var12.ordinal()), var12.j, () -> {
                  this.r = var12;
                  this.C = "";
               }, false, var12 == this.r, -2));
            }

            List var18 = X(var6, 2);
            var5.add(new u6.m((o6.b)var18.get(0), "Save and use", this::V, false, false, 0));
            var5.add(new u6.m((o6.b)var18.get(1), "Cancel", () -> this.S(u6.e.i), false, false, 0));
      }

      return var5;
   }

   private List<p_> H(u6.z var1) {
      return var1 == u6.z.p ? List.of(this.Z) : (var1 == u6.z.A ? List.of(this.H, this.Q, this.S) : List.of());
   }

   private List<Object> y(u6.z var1, List<u6.m> var2) {
      ArrayList var3 = new ArrayList();

      for (u6.m var5 : var2) {
         if (var5.line() == -2) {
            var3.add(var5);
         }
      }

      var3.addAll(this.H(var1));

      for (u6.m var7 : var2) {
         if (var7.line() != -2) {
            var3.add(var7);
         }
      }

      return var3;
   }

   private void B(u6.q var1, u6.z var2) {
      List var3 = this.H(var2);

      for (int var4 = 0; var4 < var3.size(); var4++) {
         o6.b var5 = a(var1, var2, var2 == u6.z.A ? var4 + 1 : var4);
         ((p_)var3.get(var4)).bounds(var5.x(), var5.y(), var5.width());
         ((p_)var3.get(var4)).O = var5.height();
      }
   }

   private void b(List<Object> var1) {
      for (p_ var3 : List.of(this.Z, this.H, this.Q, this.S)) {
         var3.A = (boolean)(this.X >= 0 && this.X < var1.size() && var1.get(this.X) == var3 ? 1 : 0);
      }
   }

   private void S(u6.e var1) {
      this.W = var1;
      this.U = null;
      this.f = 0;
      this.J = 0;
      this.u = 0;
      this.C = "";
      this.B.clearMessage();
      if (var1 == u6.e.B || var1 == u6.e.i) {
         this.k();
      }

      u6.z var2 = this.L();
      if (j(var2)) {
         List var3 = this.y(var2, this.I(this.w(var2, 0), var2, List.of(), -1));
         this.X = var3.indexOf(this.H(var2).getFirst());
         this.b(var3);
      }
   }

   private void k() {
      for (p_ var2 : List.of(this.Z, this.H, this.Q, this.S)) {
         var2.V = "";
         var2.blur();
      }

      this.Y = 0;
      this.X = 0;
      this.C = "";
      this.r = h4.h.e;
   }

   private void J() {
      if (this.L() == u6.z.p) {
         this.C = "";
         this.B.addToken(this.G, this.Z.V, () -> {
            this.Z.V = "";
            this.S(u6.e.B);
         });
      }
   }

   private void A() {
      String var1 = Minecraft.getInstance().keyboardHandler.getClipboard();
      this.Z.V = "";
      this.Z.A = (boolean)1;
      this.Z.E(var1);
      u6.z var2 = this.L();
      List var3 = this.y(var2, this.I(this.w(var2, 0), var2, List.of(), -1));
      this.X = var3.indexOf(this.Z);
      this.b(var3);
   }

   private void V() {
      if (this.L() == u6.z.A) {
         h4 var1;
         try {
            var1 = h4.parse(this.r, this.H.V, this.Q.V, this.S.V);
         } catch (IllegalArgumentException var3) {
            this.C = var3.getMessage();
            return;
         }

         this.C = "";
         this.B.saveProxy(this.G, var1, () -> this.S(u6.e.i));
      }
   }

   private void F(String var1) {
      this.C = "";
      int var2 = this.Y;
      this.Y = var1.length();
      if (var1.length() - var2 > 1) {
         h4.i var3 = h4.split(var1, this.r);
         if (var3 != null) {
            this.r = var3.type();
            this.H.V = var3.address();
            this.Y = this.H.V.length();
            if (!var3.username().isEmpty() || !var3.password().isEmpty()) {
               this.Q.V = var3.username();
               this.S.V = var3.password();
            }
         }
      }
   }

   public void R(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.o) {
         if (!this.B.busy() && Minecraft.getInstance().getUser() != this.j) {
            this.r();
         } else {
            u6.z var4 = this.L();
            List var5 = this.Z();
            u6.q var6 = this.w(var4, var5.size());
            this.f = var6.scroll();
            if (var2 != this.P || var3 != this.d) {
               this.w = (boolean)0;
               this.P = var2;
               this.d = var3;
            }

            boolean var7 = R(var4);
            int var8 = !var7 ? -1 : (this.w ? this.J : this.W(var6, var5.size(), var2, var3));
            if (var7 && this.U != null) {
               for (int var9 = 0; var9 < var5.size(); var9++) {
                  if (((u6.j)var5.get(var9)).id().equals(this.U)) {
                     var8 = var9;
                  }
               }
            }

            List var34 = this.I(var6, var4, var5, var8);
            List var10 = j(var4) ? this.y(var4, var34) : List.of();
            boolean var11 = LarpClient.y().v().E().o;
            double var12 = var11 ? this.R.y(1.0, 140) : 1.0;
            o6.b var14 = var6.panel();
            String var15 = this.y(var4);
            var1.nextStratum();
            ll.N(var1, var14.x(), var14.y(), var14.width(), var14.height(), 5, oh.S(-99347436, var12));
            ll.u(var1, var14.x(), var14.y(), var14.width(), var14.height(), 5, oh.S(-13290187, var12));
            var1.enableScissor(var14.x(), var14.y(), var14.x() + var14.width(), var14.y() + var14.height());

            try {
               if (var7) {
                  for (int var35 = this.f; var35 < Math.min(var5.size(), this.f + var6.capacity()); var35++) {
                     u6.j var38 = (u6.j)var5.get(var35);
                     o6.b var41 = var6.row(var35);
                     int var19 = var35 == var8 ? 1 : 0;
                     int var20 = var35;
                     int var21 = var19 != 0 && var34.stream().anyMatch(var1x -> var1x.line() == var20) ? 1 : 0;
                     if (var19 != 0) {
                        ll.N(var1, var41.x(), var41.y(), var41.width(), var41.height(), 3, oh.S(-14342875, var12));
                     }

                     String var22 = var38.current() ? (var4 == u6.z.v ? "Active" : "Current") : var38.note();
                     int var23 = !var22.isEmpty() && var21 == 0 ? j_.L(var22) + 18 : 14;
                     if (var21 != 0) {
                        for (u6.m var25 : var34) {
                           if (var25.line() == var35) {
                              var23 = Math.max(var23, var41.x() + var41.width() - var25.box().x() + 8);
                           }
                        }
                     }

                     j_.y(
                        var1,
                        j_.x(var38.name(), var41.width() - var23),
                        var41.x() + 8,
                        var41.y() + 11,
                        oh.S(var38.current() ? rv.P : rv.G0, var12),
                        var38.current() ? j_.w.Q : j_.w.H
                     );
                     if (!var22.isEmpty() && var21 == 0) {
                        j_.c(var1, var22, var41.x() + var41.width() - 8 - j_.L(var22), var41.y() + 11, oh.S("Expired".equals(var22) ? -24915 : rv.r, var12));
                     }
                  }

                  if (var5.size() > var6.capacity()) {
                     int var36 = var6.capacity() * 32;
                     int var39 = Math.max(8, var36 * var6.capacity() / var5.size());
                     int var42 = (var36 - var39) * this.f / (var5.size() - var6.capacity());
                     fr.k(var1, var14.x() + var14.width() - 3, var14.y() + 6 + var42, 1, var39, oh.S(rv.G0, var12));
                  }

                  if (var6.statusHeight() > 0) {
                     u(var1, var15, var14.x() + 10, var6.footer().y() - var6.statusHeight() + 1, var14.width() - 20, 2, this.V(var4) ? -24915 : rv.r, var12);
                  }

                  fr.k(var1, var14.x() + 6, var6.footer().y() - 3, var14.width() - 12, 1, oh.S(-13619152, var12));
               } else if (!j(var4)) {
                  if (var4 == u6.z.I) {
                     u(var1, "Microsoft sign-in", var14.x() + 10, var14.y() + 12, var14.width() - 20, 1, rv.P, var12);
                     u(var1, "microsoft.com/link", var14.x() + 10, var14.y() + 30, var14.width() - 20, 1, rv.G0, var12);
                     j_.n(var1, this.B.code(), var14.x() + var14.width() / 2, var14.y() + 49, oh.S(rv.P, var12));
                     u(var1, "Approve only the code shown here.", var14.x() + 10, var14.y() + 69, var14.width() - 20, 2, rv.G0, var12);
                  } else if (var4 == u6.z.N) {
                     u(var1, "Start a new saved list?", var14.x() + 10, var14.y() + 12, var14.width() - 20, 1, rv.P, var12);
                     u(
                        var1,
                        "The old encrypted file will be kept for recovery. Add your accounts again.",
                        var14.x() + 10,
                        var14.y() + 34,
                        var14.width() - 20,
                        3,
                        rv.G0,
                        var12
                     );
                  } else {
                     u(
                        var1,
                        this.B.status().isBlank() ? "Loading saved accounts..." : this.B.status(),
                        var14.x() + 10,
                        var14.y() + 12,
                        var14.width() - 20,
                        Math.max(1, (var6.footer().y() - var14.y() - (var4 == u6.z.o ? 46 : 18)) / 10),
                        this.B.error() ? -24915 : rv.G0,
                        var12
                     );
                  }
               } else {
                  this.B(var6, var4);
                  int var16 = var4 == u6.z.p ? 1 : 0;
                  u(
                     var1,
                     var16 != 0 ? "Sign in with a token" : "Add a proxy for server traffic",
                     var14.x() + 10,
                     var14.y() + 10,
                     var14.width() - 20,
                     1,
                     rv.P,
                     var12
                  );
                  if (var16 != 0) {
                     u(
                        var1,
                        "Paste a Minecraft access token. It is checked with Minecraft and saved encrypted until it expires.",
                        var14.x() + 10,
                        var14.y() + 24,
                        var14.width() - 20,
                        2,
                        rv.G0,
                        var12
                     );
                  }

                  this.b(var10);

                  for (p_ var18 : this.H(var4)) {
                     var18.render(var1, var2, var3);
                  }

                  if (!var15.isBlank()) {
                     u(var1, var15, var14.x() + 10, var6.footer().y() - 4 - 20, var14.width() - 20, 2, this.V(var4) ? -24915 : rv.r, var12);
                  }

                  fr.k(var1, var14.x() + 6, var6.footer().y() - 3, var14.width() - 12, 1, oh.S(-13619152, var12));
               }

               for (int var37 = 0; var37 < var34.size(); var37++) {
                  u6.m var40 = (u6.m)var34.get(var37);
                  o6.b var43 = var40.box();
                  int var44 = this.w && this.y(var4, var34, var10, var37) ? 1 : 0;
                  int var45 = !this.w && J(var43, var2, var3) ? 1 : 0;
                  if (var40.selected() || var44 != 0 || var45 != 0) {
                     ll.N(var1, var43.x(), var43.y(), var43.width(), var43.height(), 3, oh.S(var40.selected() ? -13882324 : -13421773, var12));
                  }

                  if (var44 != 0) {
                     ll.u(var1, var43.x(), var43.y(), var43.width(), var43.height(), 3, oh.S(rv.G0, var12));
                  }

                  int var46 = j_.L(var40.label());
                  float var47 = w(var46, var43.width() - 12);
                  var1.pose().pushMatrix();

                  try {
                     var1.pose().translate(var43.x() + (var43.width() - var46 * var47) / 2.0F, var43.y() + (var43.height() - 9.0F * var47) / 2.0F);
                     var1.pose().scale(var47, var47);
                     j_.c(var1, var40.label(), 0, 0, oh.S(var40.destructive() ? -24915 : (!var40.selected() && var45 == 0 && var44 == 0 ? rv.G0 : rv.P), var12));
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

   private boolean y(u6.z var1, List<u6.m> var2, List<Object> var3, int var4) {
      if (!j(var1)) {
         if (!R(var1)) {
            return this.u == var4;
         }

         int var5 = ((u6.m)var2.get(var4)).line();
         int var6 = 0;
         if (var5 != this.J) {
            return false;
         }

         for (int var7 = 0; var7 < var4; var7++) {
            if (((u6.m)var2.get(var7)).line() == var5) {
               var6++;
            }
         }

         return var6 == this.u;
      } else {
         return this.X >= 0 && this.X < var3.size() && var3.get(this.X) == var2.get(var4);
      }
   }

   public static float w(int var0, int var1) {
      return Math.min(1.0F, (float)Math.max(1, var1) / Math.max(1, var0));
   }

   private static void u(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      Minecraft var9 = Minecraft.getInstance();
      List var10 = var9.font.split(j_.v(var1, j_.w.H), var4);

      for (int var11 = 0; var11 < Math.min(var5, var10.size()); var11++) {
         var0.text(var9.font, (FormattedCharSequence)var10.get(var11), var2, var3 + var11 * 10, oh.S(var6, var7), false);
      }
   }

   public boolean l(MouseButtonEvent var1) {
      if (!this.o) {
         return false;
      }

      u6.z var2 = this.L();
      List var3 = this.Z();
      u6.q var4 = this.w(var2, var3.size());
      if (!J(var4.panel(), var1.x(), var1.y())) {
         this.r();
         return true;
      }

      if (r7.y(var1) != 0) {
         return true;
      }

      this.w = (boolean)0;
      boolean var5 = R(var2);
      int var6 = var5 ? this.W(var4, var3.size(), (int)var1.x(), (int)var1.y()) : -1;
      if (var5 && this.U != null) {
         for (int var7 = 0; var7 < var3.size(); var7++) {
            if (((u6.j)var3.get(var7)).id().equals(this.U)) {
               var6 = var7;
            }
         }
      }

      List var11 = this.I(var4, var2, var3, var6);

      for (u6.m var9 : var11) {
         if (J(var9.box(), var1.x(), var1.y())) {
            var9.action().run();
            return true;
         }
      }

      if (j(var2)) {
         this.B(var4, var2);
         List var12 = this.y(var2, var11);

         for (p_ var10 : this.H(var2)) {
            if (var10.contains(var1.x(), var1.y())) {
               this.X = var12.indexOf(var10);
            }
         }

         this.b(var12);
      }

      if (var6 >= 0) {
         this.J = var6;
         this.u = 0;
      }

      return true;
   }

   public boolean N(CharacterEvent var1) {
      if (!this.o) {
         return false;
      }

      for (p_ var3 : this.H(this.L())) {
         if (var3.A) {
            var3.character(var1.codepoint());
         }
      }

      return true;
   }

   public boolean Q(double var1, double var3, double var5) {
      if (!this.o) {
         return false;
      }

      u6.z var7 = this.L();
      List var8 = this.Z();
      u6.q var9 = this.w(var7, var8.size());
      if (R(var7) && J(var9.panel(), var1, var3) && var5 != 0.0) {
         this.f = Math.clamp(var9.scroll() + (var5 > 0.0 ? -1 : 1), 0, Math.max(0, var8.size() - var9.capacity()));
         this.U = null;
      }

      return true;
   }

   public boolean b(KeyEvent var1) {
      if (!this.o) {
         return false;
      }

      int var2 = r7.g(var1);
      u6.z var3 = this.L();
      if (var2 == 256) {
         if (this.b) {
            this.b = (boolean)0;
         } else if (this.U != null) {
            this.U = null;
         } else if (var3 == u6.z.p || var3 == u6.z.v) {
            this.S(u6.e.B);
         } else if (var3 == u6.z.A) {
            this.S(u6.e.i);
         } else {
            this.r();
         }

         return true;
      } else {
         this.w = (boolean)1;
         List var4 = this.Z();
         u6.q var5 = this.w(var3, var4.size());
         int var6 = (var1.modifiers() & 1) != 0 ? 1 : 0;
         int var7 = var2 != 257 && var2 != 335 && var2 != 32 ? 0 : 1;
         if (R(var3)) {
            this.J = Math.clamp(this.J, 0, var4.size());
            int var14 = var4.size() + 1;
            int var15 = m(this.I(var5, var3, var4, this.J), this.J);
            if (var2 == 264 || var2 == 265) {
               this.J = Math.floorMod(this.J + (var2 == 265 ? -1 : 1), var14);
               this.u = 0;
               this.U = null;
            } else if (var2 == 258 && var6 == 0) {
               if (this.u < var15 - 1) {
                  this.u++;
               } else {
                  this.J = Math.floorMod(this.J + 1, var14);
                  this.u = 0;
                  this.U = null;
               }
            } else if (var2 == 258) {
               if (this.u > 0) {
                  this.u--;
               } else {
                  this.J = Math.floorMod(this.J - 1, var14);
                  this.U = null;
                  this.u = Math.max(0, m(this.I(var5, var3, var4, this.J), this.J) - 1);
               }
            } else if (var2 == 268) {
               this.J = 0;
               this.u = 0;
            } else if (var2 == 269) {
               this.J = var4.size();
               this.u = 0;
            } else if (var2 == 263 || var2 == 262) {
               this.u = Math.clamp(this.u + (var2 == 262 ? 1 : -1), 0, Math.max(0, var15 - 1));
            }

            if (this.J < var4.size()) {
               this.f = Math.clamp(
                  this.J < this.f ? this.J : (this.J >= this.f + var5.capacity() ? this.J - var5.capacity() + 1 : this.f),
                  0,
                  Math.max(0, var4.size() - var5.capacity())
               );
            }

            if (var7 != 0) {
               var5 = this.w(var3, var4.size());
               int var16 = 0;

               for (u6.m var12 : this.I(var5, var3, var4, this.J)) {
                  if (var12.line() == this.J && var16++ == this.u) {
                     var12.action().run();
                     break;
                  }
               }
            }

            return true;
         } else {
            List var8 = this.I(var5, var3, var4, -1);
            if (j(var3)) {
               List var9 = this.y(var3, var8);
               this.X = Math.clamp(this.X, 0, var9.size() - 1);
               Object var10 = var9.get(this.X);
               if (var2 == 258) {
                  this.X = Math.floorMod(this.X + (var6 != 0 ? -1 : 1), var9.size());
                  this.b(var9);
                  return true;
               } else if (var10 instanceof p_ var17) {
                  var17.A = (boolean)1;
                  var17.key(var2);
                  return true;
               } else {
                  if (var2 != 264 && var2 != 262 && var2 != 265 && var2 != 263) {
                     if (var7 != 0 && var10 instanceof u6.m var11) {
                        var11.action().run();
                     }
                  } else {
                     this.X = Math.floorMod(this.X + (var2 != 265 && var2 != 263 ? 1 : -1), var9.size());
                     this.b(var9);
                  }

                  return true;
               }
            } else {
               if (var2 == 258 || var2 == 264 || var2 == 265) {
                  this.u = Math.floorMod(this.u + (var2 != 265 && var6 == 0 ? 1 : -1), Math.max(1, var8.size()));
               }

               if (var7 != 0 && this.u >= 0 && this.u < var8.size()) {
                  ((u6.m)var8.get(this.u)).action().run();
               }

               return true;
            }
         }
      }
   }

   private static int m(List<u6.m> var0, int var1) {
      int var2 = 0;

      for (u6.m var4 : var0) {
         if (var4.line() == var1) {
            var2++;
         }
      }

      return var2;
   }

   private enum e {
      B,
      U,
      i,
      o;
   }

   public record j(UUID id, String name, boolean current, boolean saved, boolean launcher, String note) {
   }

   private record m(o6.b box, String label, Runnable action, boolean destructive, boolean selected, int line) {
   }

   public record q(o6.b panel, o6.b footer, int capacity, int scroll, int statusHeight) {
      public o6.b row(int var1) {
         return new o6.b(this.panel.x() + 6, this.panel.y() + 6 + (var1 - this.scroll) * 32, this.panel.width() - 12, 32);
      }
   }

   public enum z {
      e,
      a,
      I,
      o,
      N,
      p,
      v,
      A;
   }
}

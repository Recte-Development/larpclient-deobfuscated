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

public final class pq {
   static final UUID q = new UUID(0L, 0L);
   private static final int S = -2;
   private static final int I = 20;
   private final Screen O;
   private final MicrosoftAccounts R = MicrosoftAccounts.INSTANCE;
   private final fq x = new fq(0.0);
   private final sk A;
   private final sk u;
   private final sk Z;
   private final sk y;
   private _c.f V = new _c.f(8, 8, 120, 22);
   private pq.k C = pq.k.o;
   private ou.u o = ou.u.r;
   private boolean j;
   private boolean P;
   private boolean T;
   private int i;
   private int m;
   private int J;
   private int K;
   private int z;
   private int h = Integer.MIN_VALUE;
   private int L;
   private String E = "";
   private UUID k;
   private User F;

   public pq(Screen var1) {
      this.O = var1;
      this.A = new sk("Paste access token", var1x -> this.E = "", 8192).m().O(this::Z);
      this.u = new sk("Proxy host:port", this::P, 300).O(this::R);
      this.Z = new sk("Username (optional)", var1x -> this.E = "", 255).O(this::R);
      this.y = new sk("Password (optional)", var1x -> this.E = "", 255).m().O(this::R);
   }

   public boolean M() {
      return this.j;
   }

   public void Z(_c.f var1) {
      this.V = var1;
   }

   public void t() {
      if (this.j) {
         this.U();
      } else {
         this.j = (boolean)1;
         this.T = (boolean)0;
         this.k = null;
         this.P = (boolean)0;
         this.m = this.J = 0;
         this.C = pq.k.o;
         this.m();
         this.F = Minecraft.getInstance().getUser();
         this.x.p(0.0);
         this.R.open(this.O);
      }
   }

   public void U() {
      if (this.j) {
         this.R.cancel(this.O);
      }

      this.j = (boolean)0;
      this.T = (boolean)0;
      this.k = null;
      this.C = pq.k.o;
      this.m();
   }

   public static List<pq.x> q(User var0, User var1, List<AccountVault.Entry> var2, boolean var3) {
      LinkedHashMap var4 = new LinkedHashMap();
      AccountVault.Entry var5 = var2.stream().filter(var1x -> var1x.id().equals(var0.getProfileId())).findFirst().orElse(null);
      var4.put(var0.getProfileId(), new pq.x(var0.getProfileId(), var0.getName(), true, var5 != null, false, var5 == null ? "" : D(var5)));

      for (AccountVault.Entry var7 : var2) {
         var4.putIfAbsent(var7.id(), new pq.x(var7.id(), var7.name(), false, true, false, D(var7)));
      }

      if (var3 && var1 != null) {
         var4.putIfAbsent(var1.getProfileId(), new pq.x(var1.getProfileId(), var1.getName(), false, false, true, ""));
      }

      return List.copyOf(var4.values());
   }

   public static List<pq.x> I(List<ou> var0, UUID var1) {
      ArrayList var2 = new ArrayList();
      var2.add(new pq.x(q, "No proxy", var1 == null, false, false, "Direct"));

      for (ou var4 : var0) {
         var2.add(new pq.x(var4.id(), var4.label(), var4.id().equals(var1), true, false, var4.authenticated() ? "Login" : ""));
      }

      return List.copyOf(var2);
   }

   private static String D(AccountVault.Entry var0) {
      if (!var0.token()) {
         return "";
      }

      Instant var1 = var0.tokenExpiry();
      return var1 != null && var1.isBefore(Instant.now()) ? "Expired" : "Token";
   }

   private List<pq.x> D() {
      if (this.C == pq.k.k) {
         return I(this.R.proxies(), this.R.activeProxyId());
      }

      Minecraft var1 = Minecraft.getInstance();
      return q(var1.getUser(), this.R.launcher(), this.R.entries(), this.R.canReturn(var1));
   }

   private pq.t h() {
      if (this.T) {
         return pq.t.p;
      }

      if (this.R.busy()) {
         return this.R.code().isBlank() ? pq.t.B : pq.t.Z;
      }

      if (!this.R.unlocked()) {
         return pq.t.f;
      }

      return switch (this.C) {
         case o -> pq.t.m;
         case B -> pq.t.R;
         case k -> pq.t.v;
         case F -> pq.t.D;
      };
   }

   private static boolean v(pq.t var0) {
      return var0 == pq.t.m || var0 == pq.t.v;
   }

   private static boolean z(pq.t var0) {
      return var0 == pq.t.R || var0 == pq.t.D;
   }

   public static pq.n y(int var0, int var1, _c.f var2, int var3, int var4, pq.t var5, boolean var6) {
      int var7 = Math.max(1, Math.min(280, var0 - 16));
      int var8 = v(var5) && var6 ? 28 : 0;

      int var9 = switch (var5) {
         case m, v -> Math.min(6, Math.max(1, var3)) * 32 + 46 + var8;
         case B -> 94;
         case Z -> 166;
         case f -> 150;
         case p -> 124;
         case R -> 158;
         case D -> 200;
      };
      int var10 = Math.clamp(var2.x() + var2.width() / 2 - var7 / 2, 8, Math.max(8, var0 - var7 - 8));
      int var11 = var2.y() + var2.height() + 4;
      int var12 = v(var5) ? 78 + var8 : var9;
      int var13 = var1 - 8 - var11;
      int var14 = var2.y() - 12;
      int var15 = var13 < var12 && var14 > var13 ? 1 : 0;
      int var16 = var15 != 0 ? var14 : var13;
      int var17 = Math.max(1, Math.min(Math.min(var9, Math.max(var12, var16)), var1 - 16));
      int var18 = var15 != 0 ? var2.y() - var17 - 4 : var11;
      int var19 = Math.clamp(var18, 8, Math.max(8, var1 - var17 - 8));
      int var20 = Math.max(1, (var17 - 46 - var8) / 32);
      return new pq.n(
         new _c.f(var10, var19, var7, var17),
         new _c.f(var10 + 6, var19 + var17 - 34, var7 - 12, 28),
         var20,
         Math.clamp(var4, 0, Math.max(0, var3 - var20)),
         var8
      );
   }

   public static _c.f v(pq.n var0, pq.t var1, int var2) {
      _c.f var3 = var0.panel();
      int var4 = var1 == pq.t.D ? 4 : 1;
      int var5 = var3.y() + (var1 == pq.t.R ? 50 : 26);
      int var6 = var0.footer().y() - 4 - 20;
      int var7 = Math.max(12, Math.min(26, (var6 - var5) / var4));
      return new _c.f(var3.x() + 10, var5 + var2 * var7, var3.width() - 20, Math.max(10, Math.min(20, var7 - 4)));
   }

   private pq.n Q(pq.t var1, int var2) {
      return y(this.O.width, this.O.height, this.V, var2, this.i, var1, !this.k(var1).isBlank());
   }

   private static boolean E(_c.f var0, double var1, double var3) {
      return var1 >= var0.x() && var3 >= var0.y() && var1 < var0.x() + var0.width() && var3 < var0.y() + var0.height();
   }

   private String k(pq.t var1) {
      if (z(var1)) {
         return !this.E.isEmpty() ? this.E : (this.R.error() ? this.R.status() : "");
      } else {
         return var1 == pq.t.v && this.R.status().isBlank() && ih.T() != null ? ih.T() : this.R.status();
      }
   }

   private boolean A(pq.t var1) {
      if (z(var1)) {
         return true;
      } else {
         return var1 == pq.t.v && this.R.status().isBlank() ? ih.T() != null : this.R.error();
      }
   }

   private static boolean O() {
      return ih.g() != null || ih.T() != null;
   }

   private static String a() {
      return ih.g() != null ? "Proxy: On" : (ih.T() != null ? "Proxy: Paused" : "Proxy: Off");
   }

   private int F(pq.n var1, int var2, int var3, int var4) {
      for (int var5 = var1.scroll(); var5 < Math.min(var2, var1.scroll() + var1.capacity()); var5++) {
         if (E(var1.row(var5), var3, var4)) {
            return var5;
         }
      }

      return -1;
   }

   private static List<_c.f> T(_c.f var0, int var1) {
      int var2 = (var0.width() - 4 * (var1 - 1)) / var1;
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < var1; var4++) {
         var3.add(
            new _c.f(var0.x() + var4 * (var2 + 4), var0.y(), var4 == var1 - 1 ? var0.x() + var0.width() - (var0.x() + var4 * (var2 + 4)) : var2, var0.height())
         );
      }

      return var3;
   }

   private List<pq.d> k(pq.n var1, pq.t var2, List<pq.x> var3, int var4) {
      ArrayList var5 = new ArrayList();
      _c.f var6 = var1.footer();
      _c.f var7 = new _c.f(var6.x(), var6.y() - 30, var6.width(), 24);
      switch (var2) {
         case m:
         case v:
            int var17 = var2 == pq.t.v ? 1 : 0;
            if (var4 >= var1.scroll() && var4 < Math.min(var3.size(), var1.scroll() + var1.capacity())) {
               pq.x var20 = (pq.x)var3.get(var4);
               _c.f var23 = var1.row(var4);
               int var24 = var23.x() + var23.width() - 4;
               if (var20.id().equals(this.k)) {
                  var5.add(new pq.d(new _c.f(var24 - 124, var23.y() + 4, 64, 24), "Remove?", () -> {
                     this.k = null;
                     if (var17) {
                        this.R.removeProxy(this.O, var20.id());
                     } else {
                        this.R.remove(this.O, var20.id());
                     }
                  }, true, false, var4));
                  var5.add(new pq.d(new _c.f(var24 - 56, var23.y() + 4, 56, 24), "Cancel", () -> this.k = null, false, false, var4));
               } else {
                  if (!var20.current()) {
                     var5.add(new pq.d(new _c.f(var24 - (var20.saved() ? 124 : 56), var23.y() + 4, 56, 24), var17 != 0 ? "Use" : "Switch", () -> {
                        if (var17) {
                           if (var20.id().equals(q)) {
                              this.R.proxyOff(this.O);
                           } else {
                              this.R.useProxy(this.O, var20.id());
                           }
                        } else if (var20.launcher()) {
                           this.R.returnToLauncher(this.O);
                        } else {
                           this.R.switchTo(this.O, var20.id());
                        }
                     }, false, false, var4));
                  }

                  if (var20.saved()) {
                     var5.add(new pq.d(new _c.f(var24 - 64, var23.y() + 4, 64, 24), "Remove", () -> this.k = var20.id(), true, false, var4));
                  }
               }
            }

            if (var17 != 0) {
               List var21 = T(var6, 2);
               var5.add(new pq.d((_c.f)var21.get(0), "+ Add proxy", () -> this.s(pq.k.F), false, false, var3.size()));
               var5.add(new pq.d((_c.f)var21.get(1), "Back", () -> this.s(pq.k.o), false, false, var3.size()));
            } else {
               List var22 = T(var6, 3);
               var5.add(new pq.d((_c.f)var22.get(0), "+ Microsoft", () -> {
                  this.k = null;
                  this.R.add(this.O);
               }, false, false, var3.size()));
               var5.add(new pq.d((_c.f)var22.get(1), "+ Token", () -> this.s(pq.k.B), false, false, var3.size()));
               var5.add(new pq.d((_c.f)var22.get(2), a(), () -> this.s(pq.k.k), false, O(), var3.size()));
            }
            break;
         case B:
            var5.add(new pq.d(var6, "Cancel", () -> this.R.cancel(this.O), false, false, 0));
            break;
         case Z:
            List var16 = T(var7, 2);
            var5.add(new pq.d((_c.f)var16.get(0), "Open Microsoft", this.R::openBrowser, false, false, 0));
            var5.add(new pq.d((_c.f)var16.get(1), "Copy code", this.R::copyCode, false, false, 0));
            var5.add(new pq.d(var6, "Cancel", () -> this.R.cancel(this.O), false, false, 0));
            break;
         case f:
            List var15 = T(var7, 2);
            var5.add(new pq.d((_c.f)var15.get(0), "Retry", () -> this.R.open(this.O), false, false, 0));
            if (this.R.exists()) {
               var5.add(new pq.d((_c.f)var15.get(1), "Start new list", () -> this.T = (boolean)1, false, false, 0));
            }

            if (O()) {
               List var19 = T(var6, 2);
               var5.add(new pq.d((_c.f)var19.get(0), "Proxy off", () -> this.R.proxyOff(this.O), false, false, 0));
               var5.add(new pq.d((_c.f)var19.get(1), "Close", this::U, false, false, 0));
            } else {
               var5.add(new pq.d(var6, "Close", this::U, false, false, 0));
            }
            break;
         case p:
            List var14 = T(var6, 2);
            var5.add(new pq.d((_c.f)var14.get(0), "Start new list", () -> {
               this.T = (boolean)0;
               this.R.archive(this.O);
            }, true, false, 0));
            var5.add(new pq.d((_c.f)var14.get(1), "Cancel", () -> this.T = (boolean)0, false, false, 0));
            break;
         case R:
            List var13 = T(var6, 3);
            var5.add(new pq.d((_c.f)var13.get(0), "Paste", this::x, false, false, 0));
            var5.add(new pq.d((_c.f)var13.get(1), "Sign in", this::Z, false, false, 0));
            var5.add(new pq.d((_c.f)var13.get(2), "Cancel", () -> this.s(pq.k.o), false, false, 0));
            break;
         case D:
            List var8 = T(v(var1, var2, 0), 3);

            for (ou.u var12 : ou.u.values()) {
               var5.add(new pq.d((_c.f)var8.get(var12.ordinal()), var12.R, () -> {
                  this.o = var12;
                  this.E = "";
               }, false, var12 == this.o, -2));
            }

            List var18 = T(var6, 2);
            var5.add(new pq.d((_c.f)var18.get(0), "Save and use", this::R, false, false, 0));
            var5.add(new pq.d((_c.f)var18.get(1), "Cancel", () -> this.s(pq.k.k), false, false, 0));
      }

      return var5;
   }

   private List<sk> F(pq.t var1) {
      return var1 == pq.t.R ? List.of(this.A) : (var1 == pq.t.D ? List.of(this.u, this.Z, this.y) : List.of());
   }

   private List<Object> H(pq.t var1, List<pq.d> var2) {
      ArrayList var3 = new ArrayList();

      for (pq.d var5 : var2) {
         if (var5.line() == -2) {
            var3.add(var5);
         }
      }

      var3.addAll(this.F(var1));

      for (pq.d var7 : var2) {
         if (var7.line() != -2) {
            var3.add(var7);
         }
      }

      return var3;
   }

   private void w(pq.n var1, pq.t var2) {
      List var3 = this.F(var2);

      for (int var4 = 0; var4 < var3.size(); var4++) {
         _c.f var5 = v(var1, var2, var2 == pq.t.D ? var4 + 1 : var4);
         ((sk)var3.get(var4)).bounds(var5.x(), var5.y(), var5.width());
         ((sk)var3.get(var4)).O = var5.height();
      }
   }

   private void C(List<Object> var1) {
      for (sk var3 : List.of(this.A, this.u, this.Z, this.y)) {
         var3.F = (boolean)(this.K >= 0 && this.K < var1.size() && var1.get(this.K) == var3 ? 1 : 0);
      }
   }

   private void s(pq.k var1) {
      this.C = var1;
      this.k = null;
      this.i = 0;
      this.m = 0;
      this.J = 0;
      this.E = "";
      this.R.clearMessage();
      if (var1 == pq.k.o || var1 == pq.k.k) {
         this.m();
      }

      pq.t var2 = this.h();
      if (z(var2)) {
         List var3 = this.H(var2, this.k(this.Q(var2, 0), var2, List.of(), -1));
         this.K = var3.indexOf(this.F(var2).getFirst());
         this.C(var3);
      }
   }

   private void m() {
      for (sk var2 : List.of(this.A, this.u, this.Z, this.y)) {
         var2.j = "";
         var2.blur();
      }

      this.z = 0;
      this.K = 0;
      this.E = "";
      this.o = ou.u.r;
   }

   private void Z() {
      if (this.h() == pq.t.R) {
         this.E = "";
         this.R.addToken(this.O, this.A.j, () -> {
            this.A.j = "";
            this.s(pq.k.o);
         });
      }
   }

   private void x() {
      String var1 = Minecraft.getInstance().keyboardHandler.getClipboard();
      this.A.j = "";
      this.A.F = (boolean)1;
      this.A.z(var1);
      pq.t var2 = this.h();
      List var3 = this.H(var2, this.k(this.Q(var2, 0), var2, List.of(), -1));
      this.K = var3.indexOf(this.A);
      this.C(var3);
   }

   private void R() {
      if (this.h() == pq.t.D) {
         ou var1;
         try {
            var1 = ou.parse(this.o, this.u.j, this.Z.j, this.y.j);
         } catch (IllegalArgumentException var3) {
            this.E = var3.getMessage();
            return;
         }

         this.E = "";
         this.R.saveProxy(this.O, var1, () -> this.s(pq.k.k));
      }
   }

   private void P(String var1) {
      this.E = "";
      int var2 = this.z;
      this.z = var1.length();
      if (var1.length() - var2 > 1) {
         ou.e var3 = ou.split(var1, this.o);
         if (var3 != null) {
            this.o = var3.type();
            this.u.j = var3.address();
            this.z = this.u.j.length();
            if (!var3.username().isEmpty() || !var3.password().isEmpty()) {
               this.Z.j = var3.username();
               this.y.j = var3.password();
            }
         }
      }
   }

   public void c(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.j) {
         if (!this.R.busy() && Minecraft.getInstance().getUser() != this.F) {
            this.U();
         } else {
            pq.t var4 = this.h();
            List var5 = this.D();
            pq.n var6 = this.Q(var4, var5.size());
            this.i = var6.scroll();
            if (var2 != this.h || var3 != this.L) {
               this.P = (boolean)0;
               this.h = var2;
               this.L = var3;
            }

            boolean var7 = v(var4);
            int var8 = !var7 ? -1 : (this.P ? this.m : this.F(var6, var5.size(), var2, var3));
            if (var7 && this.k != null) {
               for (int var9 = 0; var9 < var5.size(); var9++) {
                  if (((pq.x)var5.get(var9)).id().equals(this.k)) {
                     var8 = var9;
                  }
               }
            }

            List var34 = this.k(var6, var4, var5, var8);
            List var10 = z(var4) ? this.H(var4, var34) : List.of();
            boolean var11 = LarpClient.N().h().k().w;
            double var12 = var11 ? this.x.n(1.0, 140) : 1.0;
            _c.f var14 = var6.panel();
            String var15 = this.k(var4);
            var1.nextStratum();
            wc.b(var1, var14.x(), var14.y(), var14.width(), var14.height(), 5, vv.O(-99347436, var12));
            wc.h(var1, var14.x(), var14.y(), var14.width(), var14.height(), 5, vv.O(-13290187, var12));
            var1.enableScissor(var14.x(), var14.y(), var14.x() + var14.width(), var14.y() + var14.height());

            try {
               if (var7) {
                  for (int var35 = this.i; var35 < Math.min(var5.size(), this.i + var6.capacity()); var35++) {
                     pq.x var38 = (pq.x)var5.get(var35);
                     _c.f var41 = var6.row(var35);
                     int var19 = var35 == var8 ? 1 : 0;
                     int var20 = var35;
                     int var21 = var19 != 0 && var34.stream().anyMatch(var1x -> var1x.line() == var20) ? 1 : 0;
                     if (var19 != 0) {
                        wc.b(var1, var41.x(), var41.y(), var41.width(), var41.height(), 3, vv.O(-14342875, var12));
                     }

                     String var22 = var38.current() ? (var4 == pq.t.v ? "Active" : "Current") : var38.note();
                     int var23 = !var22.isEmpty() && var21 == 0 ? ic.E(var22) + 18 : 14;
                     if (var21 != 0) {
                        for (pq.d var25 : var34) {
                           if (var25.line() == var35) {
                              var23 = Math.max(var23, var41.x() + var41.width() - var25.box().x() + 8);
                           }
                        }
                     }

                     ic.Y(
                        var1,
                        ic.W(var38.name(), var41.width() - var23),
                        var41.x() + 8,
                        var41.y() + 11,
                        vv.O(var38.current() ? c.H : c.W, var12),
                        var38.current() ? ic.b.R : ic.b.c
                     );
                     if (!var22.isEmpty() && var21 == 0) {
                        ic.H(var1, var22, var41.x() + var41.width() - 8 - ic.E(var22), var41.y() + 11, vv.O("Expired".equals(var22) ? -24915 : c.f, var12));
                     }
                  }

                  if (var5.size() > var6.capacity()) {
                     int var36 = var6.capacity() * 32;
                     int var39 = Math.max(8, var36 * var6.capacity() / var5.size());
                     int var42 = (var36 - var39) * this.i / (var5.size() - var6.capacity());
                     h5.N(var1, var14.x() + var14.width() - 3, var14.y() + 6 + var42, 1, var39, vv.O(c.W, var12));
                  }

                  if (var6.statusHeight() > 0) {
                     H(var1, var15, var14.x() + 10, var6.footer().y() - var6.statusHeight() + 1, var14.width() - 20, 2, this.A(var4) ? -24915 : c.f, var12);
                  }

                  h5.N(var1, var14.x() + 6, var6.footer().y() - 3, var14.width() - 12, 1, vv.O(-13619152, var12));
               } else if (!z(var4)) {
                  if (var4 == pq.t.Z) {
                     H(var1, "Microsoft sign-in", var14.x() + 10, var14.y() + 12, var14.width() - 20, 1, c.H, var12);
                     H(var1, "microsoft.com/link", var14.x() + 10, var14.y() + 30, var14.width() - 20, 1, c.W, var12);
                     ic.s(var1, this.R.code(), var14.x() + var14.width() / 2, var14.y() + 49, vv.O(c.H, var12));
                     H(var1, "Approve only the code shown here.", var14.x() + 10, var14.y() + 69, var14.width() - 20, 2, c.W, var12);
                  } else if (var4 == pq.t.p) {
                     H(var1, "Start a new saved list?", var14.x() + 10, var14.y() + 12, var14.width() - 20, 1, c.H, var12);
                     H(
                        var1,
                        "The old encrypted file will be kept for recovery. Add your accounts again.",
                        var14.x() + 10,
                        var14.y() + 34,
                        var14.width() - 20,
                        3,
                        c.W,
                        var12
                     );
                  } else {
                     H(
                        var1,
                        this.R.status().isBlank() ? "Loading saved accounts..." : this.R.status(),
                        var14.x() + 10,
                        var14.y() + 12,
                        var14.width() - 20,
                        Math.max(1, (var6.footer().y() - var14.y() - (var4 == pq.t.f ? 46 : 18)) / 10),
                        this.R.error() ? -24915 : c.W,
                        var12
                     );
                  }
               } else {
                  this.w(var6, var4);
                  int var16 = var4 == pq.t.R ? 1 : 0;
                  H(
                     var1,
                     var16 != 0 ? "Sign in with a token" : "Add a proxy for server traffic",
                     var14.x() + 10,
                     var14.y() + 10,
                     var14.width() - 20,
                     1,
                     c.H,
                     var12
                  );
                  if (var16 != 0) {
                     H(
                        var1,
                        "Paste a Minecraft access token. It is checked with Minecraft and saved encrypted until it expires.",
                        var14.x() + 10,
                        var14.y() + 24,
                        var14.width() - 20,
                        2,
                        c.W,
                        var12
                     );
                  }

                  this.C(var10);

                  for (sk var18 : this.F(var4)) {
                     var18.render(var1, var2, var3);
                  }

                  if (!var15.isBlank()) {
                     H(var1, var15, var14.x() + 10, var6.footer().y() - 4 - 20, var14.width() - 20, 2, this.A(var4) ? -24915 : c.f, var12);
                  }

                  h5.N(var1, var14.x() + 6, var6.footer().y() - 3, var14.width() - 12, 1, vv.O(-13619152, var12));
               }

               for (int var37 = 0; var37 < var34.size(); var37++) {
                  pq.d var40 = (pq.d)var34.get(var37);
                  _c.f var43 = var40.box();
                  int var44 = this.P && this.B(var4, var34, var10, var37) ? 1 : 0;
                  int var45 = !this.P && E(var43, var2, var3) ? 1 : 0;
                  if (var40.selected() || var44 != 0 || var45 != 0) {
                     wc.b(var1, var43.x(), var43.y(), var43.width(), var43.height(), 3, vv.O(var40.selected() ? -13882324 : -13421773, var12));
                  }

                  if (var44 != 0) {
                     wc.h(var1, var43.x(), var43.y(), var43.width(), var43.height(), 3, vv.O(c.W, var12));
                  }

                  int var46 = ic.E(var40.label());
                  float var47 = D(var46, var43.width() - 12);
                  var1.pose().pushMatrix();

                  try {
                     var1.pose().translate(var43.x() + (var43.width() - var46 * var47) / 2.0F, var43.y() + (var43.height() - 9.0F * var47) / 2.0F);
                     var1.pose().scale(var47, var47);
                     ic.H(var1, var40.label(), 0, 0, vv.O(var40.destructive() ? -24915 : (!var40.selected() && var45 == 0 && var44 == 0 ? c.W : c.H), var12));
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

   private boolean B(pq.t var1, List<pq.d> var2, List<Object> var3, int var4) {
      if (!z(var1)) {
         if (!v(var1)) {
            return this.J == var4;
         }

         int var5 = ((pq.d)var2.get(var4)).line();
         int var6 = 0;
         if (var5 != this.m) {
            return false;
         }

         for (int var7 = 0; var7 < var4; var7++) {
            if (((pq.d)var2.get(var7)).line() == var5) {
               var6++;
            }
         }

         return var6 == this.J;
      } else {
         return this.K >= 0 && this.K < var3.size() && var3.get(this.K) == var2.get(var4);
      }
   }

   public static float D(int var0, int var1) {
      return Math.min(1.0F, (float)Math.max(1, var1) / Math.max(1, var0));
   }

   private static void H(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      Minecraft var9 = Minecraft.getInstance();
      List var10 = var9.font.split(ic.D(var1, ic.b.c), var4);

      for (int var11 = 0; var11 < Math.min(var5, var10.size()); var11++) {
         var0.text(var9.font, (FormattedCharSequence)var10.get(var11), var2, var3 + var11 * 10, vv.O(var6, var7), false);
      }
   }

   public boolean e(MouseButtonEvent var1) {
      if (!this.j) {
         return false;
      }

      pq.t var2 = this.h();
      List var3 = this.D();
      pq.n var4 = this.Q(var2, var3.size());
      if (!E(var4.panel(), var1.x(), var1.y())) {
         this.U();
         return true;
      }

      if (j2.L(var1) != 0) {
         return true;
      }

      this.P = (boolean)0;
      boolean var5 = v(var2);
      int var6 = var5 ? this.F(var4, var3.size(), (int)var1.x(), (int)var1.y()) : -1;
      if (var5 && this.k != null) {
         for (int var7 = 0; var7 < var3.size(); var7++) {
            if (((pq.x)var3.get(var7)).id().equals(this.k)) {
               var6 = var7;
            }
         }
      }

      List var11 = this.k(var4, var2, var3, var6);

      for (pq.d var9 : var11) {
         if (E(var9.box(), var1.x(), var1.y())) {
            var9.action().run();
            return true;
         }
      }

      if (z(var2)) {
         this.w(var4, var2);
         List var12 = this.H(var2, var11);

         for (sk var10 : this.F(var2)) {
            if (var10.contains(var1.x(), var1.y())) {
               this.K = var12.indexOf(var10);
            }
         }

         this.C(var12);
      }

      if (var6 >= 0) {
         this.m = var6;
         this.J = 0;
      }

      return true;
   }

   public boolean J(CharacterEvent var1) {
      if (!this.j) {
         return false;
      }

      for (sk var3 : this.F(this.h())) {
         if (var3.F) {
            var3.character(var1.codepoint());
         }
      }

      return true;
   }

   public boolean x(double var1, double var3, double var5) {
      if (!this.j) {
         return false;
      }

      pq.t var7 = this.h();
      List var8 = this.D();
      pq.n var9 = this.Q(var7, var8.size());
      if (v(var7) && E(var9.panel(), var1, var3) && var5 != 0.0) {
         this.i = Math.clamp(var9.scroll() + (var5 > 0.0 ? -1 : 1), 0, Math.max(0, var8.size() - var9.capacity()));
         this.k = null;
      }

      return true;
   }

   public boolean d(KeyEvent var1) {
      if (!this.j) {
         return false;
      }

      int var2 = j2.P(var1);
      pq.t var3 = this.h();
      if (var2 == 256) {
         if (this.T) {
            this.T = (boolean)0;
         } else if (this.k != null) {
            this.k = null;
         } else if (var3 == pq.t.R || var3 == pq.t.v) {
            this.s(pq.k.o);
         } else if (var3 == pq.t.D) {
            this.s(pq.k.k);
         } else {
            this.U();
         }

         return true;
      } else {
         this.P = (boolean)1;
         List var4 = this.D();
         pq.n var5 = this.Q(var3, var4.size());
         int var6 = (var1.modifiers() & 1) != 0 ? 1 : 0;
         int var7 = var2 != 257 && var2 != 335 && var2 != 32 ? 0 : 1;
         if (v(var3)) {
            this.m = Math.clamp(this.m, 0, var4.size());
            int var14 = var4.size() + 1;
            int var15 = b(this.k(var5, var3, var4, this.m), this.m);
            if (var2 == 264 || var2 == 265) {
               this.m = Math.floorMod(this.m + (var2 == 265 ? -1 : 1), var14);
               this.J = 0;
               this.k = null;
            } else if (var2 == 258 && var6 == 0) {
               if (this.J < var15 - 1) {
                  this.J++;
               } else {
                  this.m = Math.floorMod(this.m + 1, var14);
                  this.J = 0;
                  this.k = null;
               }
            } else if (var2 == 258) {
               if (this.J > 0) {
                  this.J--;
               } else {
                  this.m = Math.floorMod(this.m - 1, var14);
                  this.k = null;
                  this.J = Math.max(0, b(this.k(var5, var3, var4, this.m), this.m) - 1);
               }
            } else if (var2 == 268) {
               this.m = 0;
               this.J = 0;
            } else if (var2 == 269) {
               this.m = var4.size();
               this.J = 0;
            } else if (var2 == 263 || var2 == 262) {
               this.J = Math.clamp(this.J + (var2 == 262 ? 1 : -1), 0, Math.max(0, var15 - 1));
            }

            if (this.m < var4.size()) {
               this.i = Math.clamp(
                  this.m < this.i ? this.m : (this.m >= this.i + var5.capacity() ? this.m - var5.capacity() + 1 : this.i),
                  0,
                  Math.max(0, var4.size() - var5.capacity())
               );
            }

            if (var7 != 0) {
               var5 = this.Q(var3, var4.size());
               int var16 = 0;

               for (pq.d var12 : this.k(var5, var3, var4, this.m)) {
                  if (var12.line() == this.m && var16++ == this.J) {
                     var12.action().run();
                     break;
                  }
               }
            }

            return true;
         } else {
            List var8 = this.k(var5, var3, var4, -1);
            if (z(var3)) {
               List var9 = this.H(var3, var8);
               this.K = Math.clamp(this.K, 0, var9.size() - 1);
               Object var10 = var9.get(this.K);
               if (var2 == 258) {
                  this.K = Math.floorMod(this.K + (var6 != 0 ? -1 : 1), var9.size());
                  this.C(var9);
                  return true;
               } else if (var10 instanceof sk var17) {
                  var17.F = (boolean)1;
                  var17.key(var2);
                  return true;
               } else {
                  if (var2 != 264 && var2 != 262 && var2 != 265 && var2 != 263) {
                     if (var7 != 0 && var10 instanceof pq.d var11) {
                        var11.action().run();
                     }
                  } else {
                     this.K = Math.floorMod(this.K + (var2 != 265 && var2 != 263 ? 1 : -1), var9.size());
                     this.C(var9);
                  }

                  return true;
               }
            } else {
               if (var2 == 258 || var2 == 264 || var2 == 265) {
                  this.J = Math.floorMod(this.J + (var2 != 265 && var6 == 0 ? 1 : -1), Math.max(1, var8.size()));
               }

               if (var7 != 0 && this.J >= 0 && this.J < var8.size()) {
                  ((pq.d)var8.get(this.J)).action().run();
               }

               return true;
            }
         }
      }
   }

   private static int b(List<pq.d> var0, int var1) {
      int var2 = 0;

      for (pq.d var4 : var0) {
         if (var4.line() == var1) {
            var2++;
         }
      }

      return var2;
   }

   private record d(_c.f box, String label, Runnable action, boolean destructive, boolean selected, int line) {
   }

   private enum k {
      o,
      B,
      k,
      F;
   }

   public record n(_c.f panel, _c.f footer, int capacity, int scroll, int statusHeight) {
      public _c.f row(int var1) {
         return new _c.f(this.panel.x() + 6, this.panel.y() + 6 + (var1 - this.scroll) * 32, this.panel.width() - 12, 32);
      }
   }

   public enum t {
      m,
      B,
      Z,
      f,
      p,
      R,
      v,
      D;
   }

   public record x(UUID id, String name, boolean current, boolean saved, boolean launcher, String note) {
   }
}

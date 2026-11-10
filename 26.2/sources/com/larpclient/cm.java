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

public final class cm {
   static final UUID S = new UUID(0L, 0L);
   private static final int s = -2;
   private static final int Q = 20;
   private final Screen I;
   private final MicrosoftAccounts H = MicrosoftAccounts.INSTANCE;
   private final jp J = new jp(0.0);
   private final j9 R;
   private final j9 d;
   private final j9 X;
   private final j9 i;
   private p1.e G = new p1.e(8, 8, 120, 22);
   private cm.g Y = cm.g.f;
   private p8.l M = p8.l.B;
   private boolean w;
   private boolean e;
   private boolean r;
   private int n;
   private int F;
   private int B;
   private int c;
   private int T;
   private int O = Integer.MIN_VALUE;
   private int K;
   private String j = "";
   private UUID o;
   private User b;

   public cm(Screen var1) {
      this.I = var1;
      this.R = new j9("Paste access token", var1x -> this.j = "", 8192).u().B(this::c);
      this.d = new j9("Proxy host:port", this::r, 300).B(this::V);
      this.X = new j9("Username (optional)", var1x -> this.j = "", 255).B(this::V);
      this.i = new j9("Password (optional)", var1x -> this.j = "", 255).u().B(this::V);
   }

   public boolean o() {
      return this.w;
   }

   public void N(p1.e var1) {
      this.G = var1;
   }

   public void u() {
      if (this.w) {
         this.j();
      } else {
         this.w = (boolean)1;
         this.r = (boolean)0;
         this.o = null;
         this.e = (boolean)0;
         this.F = this.B = 0;
         this.Y = cm.g.f;
         this.d();
         this.b = Minecraft.getInstance().getUser();
         this.J.F(0.0);
         this.H.open(this.I);
      }
   }

   public void j() {
      if (this.w) {
         this.H.cancel(this.I);
      }

      this.w = (boolean)0;
      this.r = (boolean)0;
      this.o = null;
      this.Y = cm.g.f;
      this.d();
   }

   public static List<cm.a> J(User var0, User var1, List<AccountVault.Entry> var2, boolean var3) {
      LinkedHashMap var4 = new LinkedHashMap();
      AccountVault.Entry var5 = var2.stream().filter(var1x -> var1x.id().equals(var0.getProfileId())).findFirst().orElse(null);
      var4.put(var0.getProfileId(), new cm.a(var0.getProfileId(), var0.getName(), true, var5 != null, false, var5 == null ? "" : z(var5)));

      for (AccountVault.Entry var7 : var2) {
         var4.putIfAbsent(var7.id(), new cm.a(var7.id(), var7.name(), false, true, false, z(var7)));
      }

      if (var3 && var1 != null) {
         var4.putIfAbsent(var1.getProfileId(), new cm.a(var1.getProfileId(), var1.getName(), false, false, true, ""));
      }

      return List.copyOf(var4.values());
   }

   public static List<cm.a> c(List<p8> var0, UUID var1) {
      ArrayList var2 = new ArrayList();
      var2.add(new cm.a(S, "No proxy", var1 == null, false, false, "Direct"));

      for (p8 var4 : var0) {
         var2.add(new cm.a(var4.id(), var4.label(), var4.id().equals(var1), true, false, var4.authenticated() ? "Login" : ""));
      }

      return List.copyOf(var2);
   }

   private static String z(AccountVault.Entry var0) {
      if (!var0.token()) {
         return "";
      }

      Instant var1 = var0.tokenExpiry();
      return var1 != null && var1.isBefore(Instant.now()) ? "Expired" : "Token";
   }

   private List<cm.a> Z() {
      if (this.Y == cm.g.i) {
         return c(this.H.proxies(), this.H.activeProxyId());
      }

      Minecraft var1 = Minecraft.getInstance();
      return J(var1.getUser(), this.H.launcher(), this.H.entries(), this.H.canReturn(var1));
   }

   private cm.d J() {
      if (this.r) {
         return cm.d.l;
      }

      if (this.H.busy()) {
         return this.H.code().isBlank() ? cm.d.I : cm.d.B;
      }

      if (!this.H.unlocked()) {
         return cm.d.M;
      }

      return switch (this.Y) {
         case f -> cm.d.J;
         case B -> cm.d.c;
         case i -> cm.d.h;
         case y -> cm.d.T;
      };
   }

   private static boolean G(cm.d var0) {
      return var0 == cm.d.J || var0 == cm.d.h;
   }

   private static boolean N(cm.d var0) {
      return var0 == cm.d.c || var0 == cm.d.T;
   }

   public static cm.w N(int var0, int var1, p1.e var2, int var3, int var4, cm.d var5, boolean var6) {
      int var7 = Math.max(1, Math.min(280, var0 - 16));
      int var8 = G(var5) && var6 ? 28 : 0;

      int var9 = switch (var5) {
         case J, h -> Math.min(6, Math.max(1, var3)) * 32 + 46 + var8;
         case I -> 94;
         case B -> 166;
         case M -> 150;
         case l -> 124;
         case c -> 158;
         case T -> 200;
      };
      int var10 = Math.clamp(var2.x() + var2.width() / 2 - var7 / 2, 8, Math.max(8, var0 - var7 - 8));
      int var11 = var2.y() + var2.height() + 4;
      int var12 = G(var5) ? 78 + var8 : var9;
      int var13 = var1 - 8 - var11;
      int var14 = var2.y() - 12;
      int var15 = var13 < var12 && var14 > var13 ? 1 : 0;
      int var16 = var15 != 0 ? var14 : var13;
      int var17 = Math.max(1, Math.min(Math.min(var9, Math.max(var12, var16)), var1 - 16));
      int var18 = var15 != 0 ? var2.y() - var17 - 4 : var11;
      int var19 = Math.clamp(var18, 8, Math.max(8, var1 - var17 - 8));
      int var20 = Math.max(1, (var17 - 46 - var8) / 32);
      return new cm.w(
         new p1.e(var10, var19, var7, var17),
         new p1.e(var10 + 6, var19 + var17 - 34, var7 - 12, 28),
         var20,
         Math.clamp(var4, 0, Math.max(0, var3 - var20)),
         var8
      );
   }

   public static p1.e v(cm.w var0, cm.d var1, int var2) {
      p1.e var3 = var0.panel();
      int var4 = var1 == cm.d.T ? 4 : 1;
      int var5 = var3.y() + (var1 == cm.d.c ? 50 : 26);
      int var6 = var0.footer().y() - 4 - 20;
      int var7 = Math.max(12, Math.min(26, (var6 - var5) / var4));
      return new p1.e(var3.x() + 10, var5 + var2 * var7, var3.width() - 20, Math.max(10, Math.min(20, var7 - 4)));
   }

   private cm.w y(cm.d var1, int var2) {
      return N(this.I.width, this.I.height, this.G, var2, this.n, var1, !this.H(var1).isBlank());
   }

   private static boolean i(p1.e var0, double var1, double var3) {
      return var1 >= var0.x() && var3 >= var0.y() && var1 < var0.x() + var0.width() && var3 < var0.y() + var0.height();
   }

   private String H(cm.d var1) {
      if (N(var1)) {
         return !this.j.isEmpty() ? this.j : (this.H.error() ? this.H.status() : "");
      } else {
         return var1 == cm.d.h && this.H.status().isBlank() && p6.N() != null ? p6.N() : this.H.status();
      }
   }

   private boolean R(cm.d var1) {
      if (N(var1)) {
         return true;
      } else {
         return var1 == cm.d.h && this.H.status().isBlank() ? p6.N() != null : this.H.error();
      }
   }

   private static boolean K() {
      return p6.l() != null || p6.N() != null;
   }

   private static String r() {
      return p6.l() != null ? "Proxy: On" : (p6.N() != null ? "Proxy: Paused" : "Proxy: Off");
   }

   private int G(cm.w var1, int var2, int var3, int var4) {
      for (int var5 = var1.scroll(); var5 < Math.min(var2, var1.scroll() + var1.capacity()); var5++) {
         if (i(var1.row(var5), var3, var4)) {
            return var5;
         }
      }

      return -1;
   }

   private static List<p1.e> O(p1.e var0, int var1) {
      int var2 = (var0.width() - 4 * (var1 - 1)) / var1;
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < var1; var4++) {
         var3.add(
            new p1.e(var0.x() + var4 * (var2 + 4), var0.y(), var4 == var1 - 1 ? var0.x() + var0.width() - (var0.x() + var4 * (var2 + 4)) : var2, var0.height())
         );
      }

      return var3;
   }

   private List<cm.z> x(cm.w var1, cm.d var2, List<cm.a> var3, int var4) {
      ArrayList var5 = new ArrayList();
      p1.e var6 = var1.footer();
      p1.e var7 = new p1.e(var6.x(), var6.y() - 30, var6.width(), 24);
      switch (var2) {
         case J:
         case h:
            int var17 = var2 == cm.d.h ? 1 : 0;
            if (var4 >= var1.scroll() && var4 < Math.min(var3.size(), var1.scroll() + var1.capacity())) {
               cm.a var20 = (cm.a)var3.get(var4);
               p1.e var23 = var1.row(var4);
               int var24 = var23.x() + var23.width() - 4;
               if (var20.id().equals(this.o)) {
                  var5.add(new cm.z(new p1.e(var24 - 124, var23.y() + 4, 64, 24), "Remove?", () -> {
                     this.o = null;
                     if (var17) {
                        this.H.removeProxy(this.I, var20.id());
                     } else {
                        this.H.remove(this.I, var20.id());
                     }
                  }, true, false, var4));
                  var5.add(new cm.z(new p1.e(var24 - 56, var23.y() + 4, 56, 24), "Cancel", () -> this.o = null, false, false, var4));
               } else {
                  if (!var20.current()) {
                     var5.add(new cm.z(new p1.e(var24 - (var20.saved() ? 124 : 56), var23.y() + 4, 56, 24), var17 != 0 ? "Use" : "Switch", () -> {
                        if (var17) {
                           if (var20.id().equals(S)) {
                              this.H.proxyOff(this.I);
                           } else {
                              this.H.useProxy(this.I, var20.id());
                           }
                        } else if (var20.launcher()) {
                           this.H.returnToLauncher(this.I);
                        } else {
                           this.H.switchTo(this.I, var20.id());
                        }
                     }, false, false, var4));
                  }

                  if (var20.saved()) {
                     var5.add(new cm.z(new p1.e(var24 - 64, var23.y() + 4, 64, 24), "Remove", () -> this.o = var20.id(), true, false, var4));
                  }
               }
            }

            if (var17 != 0) {
               List var21 = O(var6, 2);
               var5.add(new cm.z((p1.e)var21.get(0), "+ Add proxy", () -> this.k(cm.g.y), false, false, var3.size()));
               var5.add(new cm.z((p1.e)var21.get(1), "Back", () -> this.k(cm.g.f), false, false, var3.size()));
            } else {
               List var22 = O(var6, 3);
               var5.add(new cm.z((p1.e)var22.get(0), "+ Microsoft", () -> {
                  this.o = null;
                  this.H.add(this.I);
               }, false, false, var3.size()));
               var5.add(new cm.z((p1.e)var22.get(1), "+ Token", () -> this.k(cm.g.B), false, false, var3.size()));
               var5.add(new cm.z((p1.e)var22.get(2), r(), () -> this.k(cm.g.i), false, K(), var3.size()));
            }
            break;
         case I:
            var5.add(new cm.z(var6, "Cancel", () -> this.H.cancel(this.I), false, false, 0));
            break;
         case B:
            List var16 = O(var7, 2);
            var5.add(new cm.z((p1.e)var16.get(0), "Open Microsoft", this.H::openBrowser, false, false, 0));
            var5.add(new cm.z((p1.e)var16.get(1), "Copy code", this.H::copyCode, false, false, 0));
            var5.add(new cm.z(var6, "Cancel", () -> this.H.cancel(this.I), false, false, 0));
            break;
         case M:
            List var15 = O(var7, 2);
            var5.add(new cm.z((p1.e)var15.get(0), "Retry", () -> this.H.open(this.I), false, false, 0));
            if (this.H.exists()) {
               var5.add(new cm.z((p1.e)var15.get(1), "Start new list", () -> this.r = (boolean)1, false, false, 0));
            }

            if (K()) {
               List var19 = O(var6, 2);
               var5.add(new cm.z((p1.e)var19.get(0), "Proxy off", () -> this.H.proxyOff(this.I), false, false, 0));
               var5.add(new cm.z((p1.e)var19.get(1), "Close", this::j, false, false, 0));
            } else {
               var5.add(new cm.z(var6, "Close", this::j, false, false, 0));
            }
            break;
         case l:
            List var14 = O(var6, 2);
            var5.add(new cm.z((p1.e)var14.get(0), "Start new list", () -> {
               this.r = (boolean)0;
               this.H.archive(this.I);
            }, true, false, 0));
            var5.add(new cm.z((p1.e)var14.get(1), "Cancel", () -> this.r = (boolean)0, false, false, 0));
            break;
         case c:
            List var13 = O(var6, 3);
            var5.add(new cm.z((p1.e)var13.get(0), "Paste", this::D, false, false, 0));
            var5.add(new cm.z((p1.e)var13.get(1), "Sign in", this::c, false, false, 0));
            var5.add(new cm.z((p1.e)var13.get(2), "Cancel", () -> this.k(cm.g.f), false, false, 0));
            break;
         case T:
            List var8 = O(v(var1, var2, 0), 3);

            for (p8.l var12 : p8.l.values()) {
               var5.add(new cm.z((p1.e)var8.get(var12.ordinal()), var12.M, () -> {
                  this.M = var12;
                  this.j = "";
               }, false, var12 == this.M, -2));
            }

            List var18 = O(var6, 2);
            var5.add(new cm.z((p1.e)var18.get(0), "Save and use", this::V, false, false, 0));
            var5.add(new cm.z((p1.e)var18.get(1), "Cancel", () -> this.k(cm.g.i), false, false, 0));
      }

      return var5;
   }

   private List<j9> Q(cm.d var1) {
      return var1 == cm.d.c ? List.of(this.R) : (var1 == cm.d.T ? List.of(this.d, this.X, this.i) : List.of());
   }

   private List<Object> W(cm.d var1, List<cm.z> var2) {
      ArrayList var3 = new ArrayList();

      for (cm.z var5 : var2) {
         if (var5.line() == -2) {
            var3.add(var5);
         }
      }

      var3.addAll(this.Q(var1));

      for (cm.z var7 : var2) {
         if (var7.line() != -2) {
            var3.add(var7);
         }
      }

      return var3;
   }

   private void H(cm.w var1, cm.d var2) {
      List var3 = this.Q(var2);

      for (int var4 = 0; var4 < var3.size(); var4++) {
         p1.e var5 = v(var1, var2, var2 == cm.d.T ? var4 + 1 : var4);
         ((j9)var3.get(var4)).bounds(var5.x(), var5.y(), var5.width());
         ((j9)var3.get(var4)).J = var5.height();
      }
   }

   private void v(List<Object> var1) {
      for (j9 var3 : List.of(this.R, this.d, this.X, this.i)) {
         var3.T = (boolean)(this.c >= 0 && this.c < var1.size() && var1.get(this.c) == var3 ? 1 : 0);
      }
   }

   private void k(cm.g var1) {
      this.Y = var1;
      this.o = null;
      this.n = 0;
      this.F = 0;
      this.B = 0;
      this.j = "";
      this.H.clearMessage();
      if (var1 == cm.g.f || var1 == cm.g.i) {
         this.d();
      }

      cm.d var2 = this.J();
      if (N(var2)) {
         List var3 = this.W(var2, this.x(this.y(var2, 0), var2, List.of(), -1));
         this.c = var3.indexOf(this.Q(var2).getFirst());
         this.v(var3);
      }
   }

   private void d() {
      for (j9 var2 : List.of(this.R, this.d, this.X, this.i)) {
         var2.H = "";
         var2.blur();
      }

      this.T = 0;
      this.c = 0;
      this.j = "";
      this.M = p8.l.B;
   }

   private void c() {
      if (this.J() == cm.d.c) {
         this.j = "";
         this.H.addToken(this.I, this.R.H, () -> {
            this.R.H = "";
            this.k(cm.g.f);
         });
      }
   }

   private void D() {
      String var1 = Minecraft.getInstance().keyboardHandler.getClipboard();
      this.R.H = "";
      this.R.T = (boolean)1;
      this.R.X(var1);
      cm.d var2 = this.J();
      List var3 = this.W(var2, this.x(this.y(var2, 0), var2, List.of(), -1));
      this.c = var3.indexOf(this.R);
      this.v(var3);
   }

   private void V() {
      if (this.J() == cm.d.T) {
         p8 var1;
         try {
            var1 = p8.parse(this.M, this.d.H, this.X.H, this.i.H);
         } catch (IllegalArgumentException var3) {
            this.j = var3.getMessage();
            return;
         }

         this.j = "";
         this.H.saveProxy(this.I, var1, () -> this.k(cm.g.i));
      }
   }

   private void r(String var1) {
      this.j = "";
      int var2 = this.T;
      this.T = var1.length();
      if (var1.length() - var2 > 1) {
         p8.s var3 = p8.split(var1, this.M);
         if (var3 != null) {
            this.M = var3.type();
            this.d.H = var3.address();
            this.T = this.d.H.length();
            if (!var3.username().isEmpty() || !var3.password().isEmpty()) {
               this.X.H = var3.username();
               this.i.H = var3.password();
            }
         }
      }
   }

   public void O(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.w) {
         if (!this.H.busy() && Minecraft.getInstance().getUser() != this.b) {
            this.j();
         } else {
            cm.d var4 = this.J();
            List var5 = this.Z();
            cm.w var6 = this.y(var4, var5.size());
            this.n = var6.scroll();
            if (var2 != this.O || var3 != this.K) {
               this.e = (boolean)0;
               this.O = var2;
               this.K = var3;
            }

            boolean var7 = G(var4);
            int var8 = !var7 ? -1 : (this.e ? this.F : this.G(var6, var5.size(), var2, var3));
            if (var7 && this.o != null) {
               for (int var9 = 0; var9 < var5.size(); var9++) {
                  if (((cm.a)var5.get(var9)).id().equals(this.o)) {
                     var8 = var9;
                  }
               }
            }

            List var34 = this.x(var6, var4, var5, var8);
            List var10 = N(var4) ? this.W(var4, var34) : List.of();
            boolean var11 = LarpClient.R().o().g().L;
            double var12 = var11 ? this.J.k(1.0, 140) : 1.0;
            p1.e var14 = var6.panel();
            String var15 = this.H(var4);
            var1.nextStratum();
            wp.I(var1, var14.x(), var14.y(), var14.width(), var14.height(), 5, uh.F(-99347436, var12));
            wp.q(var1, var14.x(), var14.y(), var14.width(), var14.height(), 5, uh.F(-13290187, var12));
            var1.enableScissor(var14.x(), var14.y(), var14.x() + var14.width(), var14.y() + var14.height());

            try {
               if (var7) {
                  for (int var35 = this.n; var35 < Math.min(var5.size(), this.n + var6.capacity()); var35++) {
                     cm.a var38 = (cm.a)var5.get(var35);
                     p1.e var41 = var6.row(var35);
                     int var19 = var35 == var8 ? 1 : 0;
                     int var20 = var35;
                     int var21 = var19 != 0 && var34.stream().anyMatch(var1x -> var1x.line() == var20) ? 1 : 0;
                     if (var19 != 0) {
                        wp.I(var1, var41.x(), var41.y(), var41.width(), var41.height(), 3, uh.F(-14342875, var12));
                     }

                     String var22 = var38.current() ? (var4 == cm.d.h ? "Active" : "Current") : var38.note();
                     int var23 = !var22.isEmpty() && var21 == 0 ? bj.q(var22) + 18 : 14;
                     if (var21 != 0) {
                        for (cm.z var25 : var34) {
                           if (var25.line() == var35) {
                              var23 = Math.max(var23, var41.x() + var41.width() - var25.box().x() + 8);
                           }
                        }
                     }

                     bj.z(
                        var1,
                        bj.G(var38.name(), var41.width() - var23),
                        var41.x() + 8,
                        var41.y() + 11,
                        uh.F(var38.current() ? ho.u : ho.Z, var12),
                        var38.current() ? bj.j.T : bj.j.c
                     );
                     if (!var22.isEmpty() && var21 == 0) {
                        bj.h(var1, var22, var41.x() + var41.width() - 8 - bj.q(var22), var41.y() + 11, uh.F("Expired".equals(var22) ? -24915 : ho.j, var12));
                     }
                  }

                  if (var5.size() > var6.capacity()) {
                     int var36 = var6.capacity() * 32;
                     int var39 = Math.max(8, var36 * var6.capacity() / var5.size());
                     int var42 = (var36 - var39) * this.n / (var5.size() - var6.capacity());
                     kv.d(var1, var14.x() + var14.width() - 3, var14.y() + 6 + var42, 1, var39, uh.F(ho.Z, var12));
                  }

                  if (var6.statusHeight() > 0) {
                     r(var1, var15, var14.x() + 10, var6.footer().y() - var6.statusHeight() + 1, var14.width() - 20, 2, this.R(var4) ? -24915 : ho.j, var12);
                  }

                  kv.d(var1, var14.x() + 6, var6.footer().y() - 3, var14.width() - 12, 1, uh.F(-13619152, var12));
               } else if (!N(var4)) {
                  if (var4 == cm.d.B) {
                     r(var1, "Microsoft sign-in", var14.x() + 10, var14.y() + 12, var14.width() - 20, 1, ho.u, var12);
                     r(var1, "microsoft.com/link", var14.x() + 10, var14.y() + 30, var14.width() - 20, 1, ho.Z, var12);
                     bj.n(var1, this.H.code(), var14.x() + var14.width() / 2, var14.y() + 49, uh.F(ho.u, var12));
                     r(var1, "Approve only the code shown here.", var14.x() + 10, var14.y() + 69, var14.width() - 20, 2, ho.Z, var12);
                  } else if (var4 == cm.d.l) {
                     r(var1, "Start a new saved list?", var14.x() + 10, var14.y() + 12, var14.width() - 20, 1, ho.u, var12);
                     r(
                        var1,
                        "The old encrypted file will be kept for recovery. Add your accounts again.",
                        var14.x() + 10,
                        var14.y() + 34,
                        var14.width() - 20,
                        3,
                        ho.Z,
                        var12
                     );
                  } else {
                     r(
                        var1,
                        this.H.status().isBlank() ? "Loading saved accounts..." : this.H.status(),
                        var14.x() + 10,
                        var14.y() + 12,
                        var14.width() - 20,
                        Math.max(1, (var6.footer().y() - var14.y() - (var4 == cm.d.M ? 46 : 18)) / 10),
                        this.H.error() ? -24915 : ho.Z,
                        var12
                     );
                  }
               } else {
                  this.H(var6, var4);
                  int var16 = var4 == cm.d.c ? 1 : 0;
                  r(
                     var1,
                     var16 != 0 ? "Sign in with a token" : "Add a proxy for server traffic",
                     var14.x() + 10,
                     var14.y() + 10,
                     var14.width() - 20,
                     1,
                     ho.u,
                     var12
                  );
                  if (var16 != 0) {
                     r(
                        var1,
                        "Paste a Minecraft access token. It is checked with Minecraft and saved encrypted until it expires.",
                        var14.x() + 10,
                        var14.y() + 24,
                        var14.width() - 20,
                        2,
                        ho.Z,
                        var12
                     );
                  }

                  this.v(var10);

                  for (j9 var18 : this.Q(var4)) {
                     var18.render(var1, var2, var3);
                  }

                  if (!var15.isBlank()) {
                     r(var1, var15, var14.x() + 10, var6.footer().y() - 4 - 20, var14.width() - 20, 2, this.R(var4) ? -24915 : ho.j, var12);
                  }

                  kv.d(var1, var14.x() + 6, var6.footer().y() - 3, var14.width() - 12, 1, uh.F(-13619152, var12));
               }

               for (int var37 = 0; var37 < var34.size(); var37++) {
                  cm.z var40 = (cm.z)var34.get(var37);
                  p1.e var43 = var40.box();
                  int var44 = this.e && this.w(var4, var34, var10, var37) ? 1 : 0;
                  int var45 = !this.e && i(var43, var2, var3) ? 1 : 0;
                  if (var40.selected() || var44 != 0 || var45 != 0) {
                     wp.I(var1, var43.x(), var43.y(), var43.width(), var43.height(), 3, uh.F(var40.selected() ? -13882324 : -13421773, var12));
                  }

                  if (var44 != 0) {
                     wp.q(var1, var43.x(), var43.y(), var43.width(), var43.height(), 3, uh.F(ho.Z, var12));
                  }

                  int var46 = bj.q(var40.label());
                  float var47 = C(var46, var43.width() - 12);
                  var1.pose().pushMatrix();

                  try {
                     var1.pose().translate(var43.x() + (var43.width() - var46 * var47) / 2.0F, var43.y() + (var43.height() - 9.0F * var47) / 2.0F);
                     var1.pose().scale(var47, var47);
                     bj.h(var1, var40.label(), 0, 0, uh.F(var40.destructive() ? -24915 : (!var40.selected() && var45 == 0 && var44 == 0 ? ho.Z : ho.u), var12));
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

   private boolean w(cm.d var1, List<cm.z> var2, List<Object> var3, int var4) {
      if (!N(var1)) {
         if (!G(var1)) {
            return this.B == var4;
         }

         int var5 = ((cm.z)var2.get(var4)).line();
         int var6 = 0;
         if (var5 != this.F) {
            return false;
         }

         for (int var7 = 0; var7 < var4; var7++) {
            if (((cm.z)var2.get(var7)).line() == var5) {
               var6++;
            }
         }

         return var6 == this.B;
      } else {
         return this.c >= 0 && this.c < var3.size() && var3.get(this.c) == var2.get(var4);
      }
   }

   public static float C(int var0, int var1) {
      return Math.min(1.0F, (float)Math.max(1, var1) / Math.max(1, var0));
   }

   private static void r(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      Minecraft var9 = Minecraft.getInstance();
      List var10 = var9.font.split(bj.n(var1, bj.j.c), var4);

      for (int var11 = 0; var11 < Math.min(var5, var10.size()); var11++) {
         var0.text(var9.font, (FormattedCharSequence)var10.get(var11), var2, var3 + var11 * 10, uh.F(var6, var7), false);
      }
   }

   public boolean y(MouseButtonEvent var1) {
      if (!this.w) {
         return false;
      }

      cm.d var2 = this.J();
      List var3 = this.Z();
      cm.w var4 = this.y(var2, var3.size());
      if (!i(var4.panel(), var1.x(), var1.y())) {
         this.j();
         return true;
      }

      if (k2.w(var1) != 0) {
         return true;
      }

      this.e = (boolean)0;
      boolean var5 = G(var2);
      int var6 = var5 ? this.G(var4, var3.size(), (int)var1.x(), (int)var1.y()) : -1;
      if (var5 && this.o != null) {
         for (int var7 = 0; var7 < var3.size(); var7++) {
            if (((cm.a)var3.get(var7)).id().equals(this.o)) {
               var6 = var7;
            }
         }
      }

      List var11 = this.x(var4, var2, var3, var6);

      for (cm.z var9 : var11) {
         if (i(var9.box(), var1.x(), var1.y())) {
            var9.action().run();
            return true;
         }
      }

      if (N(var2)) {
         this.H(var4, var2);
         List var12 = this.W(var2, var11);

         for (j9 var10 : this.Q(var2)) {
            if (var10.contains(var1.x(), var1.y())) {
               this.c = var12.indexOf(var10);
            }
         }

         this.v(var12);
      }

      if (var6 >= 0) {
         this.F = var6;
         this.B = 0;
      }

      return true;
   }

   public boolean t(CharacterEvent var1) {
      if (!this.w) {
         return false;
      }

      for (j9 var3 : this.Q(this.J())) {
         if (var3.T) {
            var3.character(var1.codepoint());
         }
      }

      return true;
   }

   public boolean h(double var1, double var3, double var5) {
      if (!this.w) {
         return false;
      }

      cm.d var7 = this.J();
      List var8 = this.Z();
      cm.w var9 = this.y(var7, var8.size());
      if (G(var7) && i(var9.panel(), var1, var3) && var5 != 0.0) {
         this.n = Math.clamp(var9.scroll() + (var5 > 0.0 ? -1 : 1), 0, Math.max(0, var8.size() - var9.capacity()));
         this.o = null;
      }

      return true;
   }

   public boolean v(KeyEvent var1) {
      if (!this.w) {
         return false;
      }

      int var2 = k2.a(var1);
      cm.d var3 = this.J();
      if (var2 == 256) {
         if (this.r) {
            this.r = (boolean)0;
         } else if (this.o != null) {
            this.o = null;
         } else if (var3 == cm.d.c || var3 == cm.d.h) {
            this.k(cm.g.f);
         } else if (var3 == cm.d.T) {
            this.k(cm.g.i);
         } else {
            this.j();
         }

         return true;
      } else {
         this.e = (boolean)1;
         List var4 = this.Z();
         cm.w var5 = this.y(var3, var4.size());
         int var6 = (var1.modifiers() & 1) != 0 ? 1 : 0;
         int var7 = var2 != 257 && var2 != 335 && var2 != 32 ? 0 : 1;
         if (G(var3)) {
            this.F = Math.clamp(this.F, 0, var4.size());
            int var14 = var4.size() + 1;
            int var15 = A(this.x(var5, var3, var4, this.F), this.F);
            if (var2 == 264 || var2 == 265) {
               this.F = Math.floorMod(this.F + (var2 == 265 ? -1 : 1), var14);
               this.B = 0;
               this.o = null;
            } else if (var2 == 258 && var6 == 0) {
               if (this.B < var15 - 1) {
                  this.B++;
               } else {
                  this.F = Math.floorMod(this.F + 1, var14);
                  this.B = 0;
                  this.o = null;
               }
            } else if (var2 == 258) {
               if (this.B > 0) {
                  this.B--;
               } else {
                  this.F = Math.floorMod(this.F - 1, var14);
                  this.o = null;
                  this.B = Math.max(0, A(this.x(var5, var3, var4, this.F), this.F) - 1);
               }
            } else if (var2 == 268) {
               this.F = 0;
               this.B = 0;
            } else if (var2 == 269) {
               this.F = var4.size();
               this.B = 0;
            } else if (var2 == 263 || var2 == 262) {
               this.B = Math.clamp(this.B + (var2 == 262 ? 1 : -1), 0, Math.max(0, var15 - 1));
            }

            if (this.F < var4.size()) {
               this.n = Math.clamp(
                  this.F < this.n ? this.F : (this.F >= this.n + var5.capacity() ? this.F - var5.capacity() + 1 : this.n),
                  0,
                  Math.max(0, var4.size() - var5.capacity())
               );
            }

            if (var7 != 0) {
               var5 = this.y(var3, var4.size());
               int var16 = 0;

               for (cm.z var12 : this.x(var5, var3, var4, this.F)) {
                  if (var12.line() == this.F && var16++ == this.B) {
                     var12.action().run();
                     break;
                  }
               }
            }

            return true;
         } else {
            List var8 = this.x(var5, var3, var4, -1);
            if (N(var3)) {
               List var9 = this.W(var3, var8);
               this.c = Math.clamp(this.c, 0, var9.size() - 1);
               Object var10 = var9.get(this.c);
               if (var2 == 258) {
                  this.c = Math.floorMod(this.c + (var6 != 0 ? -1 : 1), var9.size());
                  this.v(var9);
                  return true;
               } else if (var10 instanceof j9 var17) {
                  var17.T = (boolean)1;
                  var17.key(var2);
                  return true;
               } else {
                  if (var2 != 264 && var2 != 262 && var2 != 265 && var2 != 263) {
                     if (var7 != 0 && var10 instanceof cm.z var11) {
                        var11.action().run();
                     }
                  } else {
                     this.c = Math.floorMod(this.c + (var2 != 265 && var2 != 263 ? 1 : -1), var9.size());
                     this.v(var9);
                  }

                  return true;
               }
            } else {
               if (var2 == 258 || var2 == 264 || var2 == 265) {
                  this.B = Math.floorMod(this.B + (var2 != 265 && var6 == 0 ? 1 : -1), Math.max(1, var8.size()));
               }

               if (var7 != 0 && this.B >= 0 && this.B < var8.size()) {
                  ((cm.z)var8.get(this.B)).action().run();
               }

               return true;
            }
         }
      }
   }

   private static int A(List<cm.z> var0, int var1) {
      int var2 = 0;

      for (cm.z var4 : var0) {
         if (var4.line() == var1) {
            var2++;
         }
      }

      return var2;
   }

   public record a(UUID id, String name, boolean current, boolean saved, boolean launcher, String note) {
   }

   public enum d {
      J,
      I,
      B,
      M,
      l,
      c,
      h,
      T;
   }

   private enum g {
      f,
      B,
      i,
      y;
   }

   public record w(p1.e panel, p1.e footer, int capacity, int scroll, int statusHeight) {
      public p1.e row(int var1) {
         return new p1.e(this.panel.x() + 6, this.panel.y() + 6 + (var1 - this.scroll) * 32, this.panel.width() - 12, 32);
      }
   }

   private record z(p1.e box, String label, Runnable action, boolean destructive, boolean selected, int line) {
   }
}

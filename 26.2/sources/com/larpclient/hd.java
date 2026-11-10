package com.larpclient;

import com.google.gson.JsonObject;
import com.larpclient.module.Module;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;

public final class hd extends h6 {
   final uq GQ = uq.x;
   final gg n = LarpClient.R().o();
   final gq Q = new gq(this);
   private final j7 i = new j7(this);
   String Gr = "";
   String P = "";
   String e = "";
   boolean t;
   boolean c;
   private boolean T;
   private boolean X;
   private String Ga = "";
   private String GD = "";
   private Runnable Gc;
   private Runnable G2;
   private String GJ = "";
   private String Gb = "Confirm";
   private String K = "";
   private String GS = "Cancel";
   private boolean Gh;
   private final List<jx> GW = new ArrayList<>();
   private final List<jx> A = new ArrayList<>();
   private final Map<jx, List<jx>> f = new LinkedHashMap<>();
   private j9 Z;
   private j9 GG;
   private j9 v;
   private int G9;
   private int B;
   private int GN = 62;
   private final jp D = new jp(0.0);
   private final z9<JsonObject> G1 = new z9<>(var1x -> this.b().execute(var1x), this::D);

   hd(hf var1) {
      super("Configs");
      this.O = var1;
   }

   private boolean D() {
      return this.b().gui.screen() == this.O && this.O.m(this);
   }

   List<Module> f() {
      return LarpClient.R().z().h();
   }

   Minecraft b() {
      return this.O.H();
   }

   void f(jd var1) {
      this.p.add(var1);
   }

   @Override
   protected void text(String var1) {
      super.text(var1);
   }

   jx y(String var1, Runnable var2) {
      return this.a(var1, () -> {
         if (!this.t) {
            this.i(var2);
         }
      });
   }

   @Override
   protected jx option(String var1, String var2, Runnable var3) {
      return super.option(var1, var2, () -> {
         if (!this.t) {
            this.i(var3);
         }
      });
   }

   jx k(String var1, Runnable var2) {
      return this.N(var1, () -> {
         if (!this.t) {
            this.i(var2);
         }
      });
   }

   void b(String var1, String var2) {
      this.M = var1;
      this.W = var2;
   }

   j9 s(String var1, String var2, int var3, Consumer<String> var4) {
      j9 var5 = new j9(var1, var4, var3);
      var5.H = var2;
      this.h(var5);
      return var5;
   }

   void E(String var1, String var2, boolean var3, Runnable var4) {
      jx var5 = new jx(var1, () -> {
         if (!this.t) {
            this.P = "";
            this.C();
            this.i(var4);
         }
      });
      var5.g = (boolean)1;
      var5.s = (boolean)1;
      var5.X = var2;
      var5.z = var2.equals("Active");
      this.GW.add(var5);
      this.p.add(var5);
   }

   void Y(String var1, Runnable var2) {
      if (!this.GW.isEmpty()) {
         jx var3 = this.GW.getLast();
         jx var4 = new jx(var1, () -> {
            if (!this.t) {
               this.i(var2);
            }
         });
         var4.x = (boolean)(!var1.equals("Load") && !var1.equals("Update") ? 0 : 1);
         var4.a = var1.equals("Delete");
         var4.G = (boolean)1;
         this.f.computeIfAbsent(var3, var0 -> new ArrayList<>()).add(var4);
         this.p.add(var4);
      }
   }

   private void U(String var1, Runnable var2) {
      jx var3 = new jx(var1, () -> {
         if (!this.t) {
            this.i(var2);
         }
      });
      var3.g = (boolean)1;
      this.A.add(var3);
      this.p.add(var3);
   }

   private void i(Runnable var1) {
      try {
         var1.run();
      } catch (IllegalArgumentException var3) {
         this.Gr = var3.getMessage();
         com.larpclient.h.g("Config", "Config action rejected: {}", this.Gr);
         this.build();
      } catch (RuntimeException var4) {
         this.Gr = "The operation could not be completed. Try again.";
         com.larpclient.h.m("Config", "Config action failed", var4);
         this.build();
      }
   }

   @Override
   protected void build() {
      this.v();
      this.GW.clear();
      this.A.clear();
      this.f.clear();
      this.GG = this.v = null;
      this.J = (boolean)(!this.T && !this.c && this.Q.n == null && this.Gc == null && this.Ga.isEmpty() && !this.K() ? 0 : 1);
      this.O("My Configs", !this.X, () -> this.i("My Configs"));
      this.O("Discover", this.X, () -> this.i("Discover"));
      this.Z = new j9(this.X ? "Search this page of configs..." : "Search configs...", var1x -> {
         if (this.X) {
            this.i.l = var1x;
            this.i.D = System.currentTimeMillis() + 180L;
         } else {
            this.e = var1x;
            this.h();
         }
      }, 80);
      this.Z.H = this.X ? this.i.l : this.e;
      this.p.add(this.Z);
      if (!this.X) {
         this.U("Save as new config", () -> this.H("New", ""));
         this.U("Import config", () -> {
            this.c = (boolean)1;
            this.Y = 0;
            this.build();
            if (this.v != null) {
               this.U(this.v);
            }
         });
         if (this.y != null && !this.y.columns()) {
            this.U("More", () -> {
               this.T = (boolean)1;
               this.Y = 0;
               this.build();
            });
         } else {
            this.U("Refresh shared", () -> this.Q.r(false));
            if (this.GQ.A()) {
               this.U(this.Q.q ? "Regular storage" : "Experimental storage", this::k);
            }
         }

         if (!this.P.isEmpty()) {
            this.U("Copy share code", () -> {
               this.b().keyboardHandler.setClipboard(this.P);
               this.Gr = "Share code copied to clipboard.";
            });
         }
      } else {
         this.U("Refresh", this.i::h);
         this.U("Previous", () -> {
            if (this.i.E > 0) {
               this.i.E = Math.max(0, this.i.E - 30);
               this.i.h();
            }
         });
         this.U("Next", () -> {
            if (this.i.E + 30 < this.i.h) {
               this.i.E += 30;
               this.i.h();
            }
         });
         this.A.get(1).p = (boolean)(this.i.E > 0 ? 1 : 0);
         this.A.get(2).p = (boolean)(this.i.E + 30 < this.i.h ? 1 : 0);
      }

      if (this.X) {
         this.i.I();
      } else {
         this.Q.I();
      }

      if (this.T) {
         this.b("Shared configs", "");
         this.option("Refresh shared", "Add your uploaded configs to this list", () -> {
            this.T = (boolean)0;
            this.Q.r(false);
         });
         if (this.GQ.A()) {
            this.option(
               this.Q.q ? "Regular storage" : "Experimental storage",
               this.Q.q ? "Show your regular shared configs" : "Show configs in experimental storage",
               () -> {
                  this.T = (boolean)0;
                  this.k();
               }
            );
         }
      } else if (this.Gc != null) {
         int var1 = this.GJ.indexOf(10);
         this.b(var1 < 0 ? this.GJ : this.GJ.substring(0, var1), "");
         if (var1 >= 0) {
            this.text(this.GJ.substring(var1 + 1));
         }

         this.k(this.GS, () -> {
            this.Gc = this.G2 = null;
            this.build();
         });
         if (this.G2 != null) {
            this.k(this.K, () -> this.c(this.G2));
         }

         jx var2 = this.k(this.Gb, () -> this.c(this.Gc));
         var2.x = (boolean)1;
         var2.a = this.Gh;
      } else if (this.Q.n != null) {
         this.Q.z();
      } else if (this.c) {
         this.b("Import config", "Paste a share code. Nothing loads until you confirm.");
         this.v = this.Q.v();
         this.k("Cancel", () -> {
            this.c = (boolean)0;
            this.build();
         });
         this.k("Fetch preview", () -> this.Q.g(ut.T(this.Q.p))).x = (boolean)1;
      } else if (!this.Ga.isEmpty()) {
         switch (this.Ga) {
            case "New":
               this.b("Save as new config", "Saves your current settings. Nothing is loaded.");
               break;
            case "Duplicate":
               this.b("Duplicate “" + this.Q.j + "”", "Name the copy.");
               break;
            default:
               this.b(this.Ga + " “" + this.Q.j + "”", "");
         }

         this.GG = this.s("Config name", this.GD, 32, var1x -> this.GD = var1x);
         this.k("Cancel", () -> {
            this.Ga = "";
            this.build();
         });
         this.k(this.Ga.equals("Rename") ? "Rename" : "Save", this::G).x = (boolean)1;
      } else if (this.X) {
         this.i.t();
      } else {
         this.Q.U();
      }

      this.m = this.X ? "Discover" : "My Configs";

      for (jd var6 : this.p) {
         if (!(var6 instanceof j1)) {
            var6.p = (boolean)(var6.p & (!this.t ? 1 : 0));
         }
      }

      if (this.y != null) {
         this.layout();
      }
   }

   private void k() {
      this.Q.q = (boolean)(!this.Q.q ? 1 : 0);
      this.Q.Z();
      this.Q.W = List.of();
      this.Q.r(false);
   }

   void L() {
      this.h();
   }

   private void h() {
      int var1 = this.Z != null && this.Z.T ? 1 : 0;
      this.G9 = 0;
      this.build();
      if (var1 != 0) {
         this.U(this.Z);
      }
   }

   void H(String var1, String var2) {
      this.Ga = var1;
      this.GD = var2;
      this.Y = 0;
      this.build();
      if (this.GG != null) {
         this.U(this.GG);
      }
   }

   private void G() {
      this.O.r();
      String var1 = km.r(this.GD);

      try {
         if (this.Ga.equals("Rename")) {
            this.n.T(this.Q.j, var1);
         } else if (this.Ga.equals("Duplicate")) {
            this.n.E(this.Q.j, var1);
         } else {
            this.n.M(var1, false);
         }

         this.Q.j = var1;
         this.Ga = "";
         this.Gr = "Saved " + var1;
         this.build();
      } catch (IOException var3) {
         this.Gr = var3.getMessage();
         com.larpclient.h.g("Config", "Config {} failed: {}", this.Ga, var3.toString());
         this.build();
      }
   }

   void i(String var1) {
      if (!this.t) {
         this.C();
         this.X = var1.equals("Discover");
         this.Q.n = null;
         this.c = (boolean)0;
         this.T = (boolean)0;
         this.Ga = "";
         this.Gc = null;
         this.i.V();
         this.Y = this.G9 = 0;
         this.P = "";
         this.Gr = "";
         com.larpclient.h.A("GUI", "Configs switched to {}", this.X ? "Discover" : "My Configs");
         if (this.X && !this.i.M) {
            this.i.h();
         } else {
            this.build();
         }
      }
   }

   boolean I() {
      return this.X && !this.J;
   }

   void U(String var1, String var2, Runnable var3) {
      this.M(var1, var2, var3, "", null);
   }

   void I(String var1, String var2, Runnable var3) {
      this.M(var1, var2, var3, "", null);
      this.Gh = (boolean)1;
      this.build();
   }

   void M(String var1, String var2, Runnable var3, String var4, Runnable var5) {
      this.GJ = var1;
      this.Gb = var2;
      this.Gc = var3;
      this.Gh = (boolean)0;
      this.GS = "Cancel";
      this.K = var4;
      this.G2 = var5;
      this.Y = 0;
      this.build();
   }

   void J(String var1, String var2, Runnable var3, String var4) {
      this.M(var1, var2, var3, "", null);
      this.GS = var4;
      this.build();
   }

   private void c(Runnable var1) {
      this.Gc = this.G2 = null;
      this.c = (boolean)0;
      var1.run();
   }

   void o(String var1, CompletableFuture<JsonObject> var2, Consumer<JsonObject> var3) {
      if (this.t) {
         var2.cancel(true);
      } else {
         this.t = (boolean)1;
         this.Gr = var1;
         this.build();
         lo.C("API", lo.z.O);
         this.G1.J(var2, (var3x, var4) -> {
            this.t = (boolean)0;
            if (var4 != null) {
               lo.C("API", lo.z.W);
               this.Gr = this.GQ.C(var4);
               com.larpclient.h.g("API", "Config request failed: {}", this.Gr);
               bh var5 = uq.q(var4);
               if (var5.j().equals("REVISION_CONFLICT") && !var1.equals("Loading cloud items...")) {
                  this.Q.r(true);
                  return;
               }

               if (var5.i() == 403 && this.Q.q) {
                  this.Q.r();
               }

               if (var5.i() == 401 || var5.i() == 503 || var5.j().equals("NETWORK")) {
                  this.GQ.l();
               }
            } else {
               try {
                  var3.accept(var3x);
                  lo.C("API", lo.z.Y);
               } catch (RuntimeException var6) {
                  this.Gr = "Invalid server response. Nothing was loaded locally.";
                  com.larpclient.h.g("API", "Invalid config response: {}", var6.toString());
               }
            }

            this.build();
         });
      }
   }

   @Override
   protected boolean showTitle() {
      return false;
   }

   @Override
   protected boolean contentShown() {
      return this.J;
   }

   @Override
   protected int bodyTop() {
      return this.y.bodyTop() + this.GN;
   }

   private int N() {
      return this.y.right();
   }

   private boolean K() {
      return this.X ? this.i.i != null : this.Q.z != null || this.Q.j != null;
   }

   @Override
   protected void layout() {
      int var1 = this.y.contentX();
      int var2 = this.y.right() - var1;
      int var3 = Math.min(68, Math.max(1, var2 - 60));
      int var4 = Math.min(180, Math.max(1, var2 - var3));
      this.Z.bounds(var1 + var3, this.y.bodyTop(), var4);
      this.Z.J = 20;
      p9.c var5 = p9.P(var2, this.A.stream().map(var0 -> bj.e(var0.K, bj.u.M) + 12).toList());
      this.GN = 27 + var5.height() + 8;

      for (int var6 = 0; var6 < this.A.size(); var6++) {
         jx var7 = this.A.get(var6);
         p9.g var8 = var5.cells().get(var6);
         var7.G = (boolean)1;
         var7.bounds(var1 + var8.x(), this.y.bodyTop() + 27 + var8.y(), var8.width());
         var7.J = 24;
      }

      super.layout();
      hp var12 = hp.calculate(var2, this.GW.size());
      int var13 = Math.max(1, this.y.bottom() - this.bodyTop());
      this.B = var12.height();
      this.G9 = Math.clamp(this.G9, 0, Math.max(0, this.B - var13));
      int var14 = (int)Math.round(this.D.k(this.G9, 150));

      for (int var9 = 0; var9 < this.GW.size(); var9++) {
         jx var10 = this.GW.get(var9);
         var10.bounds(var1 + var12.x(var9), this.bodyTop() + var12.y(var9) - var14, var12.cardWidth());
         var10.J = 48;
         List var11 = this.f.getOrDefault(var10, List.of());
         C(var10, var11);
      }
   }

   static void C(jx var0, List<jx> var1) {
      int var2 = var1.stream().mapToInt(var0x -> var0x.a ? 36 : 46).sum();
      int var3 = Math.max(0, var1.size() - 1) * 4;
      int var4 = var0.A - jx.l(var0.A) - 6 - var3;
      double var5 = Math.min(1.0, (double)Math.max(1, var4) / Math.max(1, var2));
      int var7 = var1.stream().mapToInt(var2x -> Math.max(1, (int)((var2x.a ? 36 : 46) * var5))).sum() + var3;
      int var8 = var0.S + var0.A - 6 - var7;

      for (jx var10 : var1) {
         int var11 = Math.max(1, (int)((var10.a ? 36 : 46) * var5));
         var10.bounds(var8, var0.u + 31, var11);
         var10.J = 14;
         var10.i = var0.i;
         var8 += var11 + 4;
      }
   }

   @Override
   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
      bj.k(var1, bj.i("Configs", Math.max(1, this.Z.S - this.y.contentX() - 6), bj.u.N), this.y.contentX(), this.y.bodyTop() + 3, ho.u, bj.u.N);
      this.Z.render(var1, var2, var3);

      for (jx var5 : this.A) {
         var5.render(var1, var2, var3);
      }

      var1.enableScissor(this.y.contentX(), this.bodyTop(), this.N(), this.y.bottom());

      try {
         int var12 = var3 >= this.bodyTop() && var3 < this.y.bottom() ? var2 : -1;

         for (jx var6 : this.GW) {
            if (var6.u + var6.J > this.bodyTop() && var6.u < this.y.bottom()) {
               var6.render(var1, var12, var3);

               for (jx var8 : this.f.getOrDefault(var6, List.of())) {
                  var8.render(var1, var12, var3);
               }
            }
         }
      } finally {
         var1.disableScissor();
      }

      wp.p(var1, this.N() - 3, this.bodyTop(), Math.max(1, this.y.bottom() - this.bodyTop()), this.B, (int)Math.round(this.D.B()));
      if (this.GW.isEmpty()) {
         bj.E(var1, this.t ? "Loading..." : "No configs found", this.y.contentX() + 8, this.bodyTop() + 8, ho.Z);
      }
   }

   @Override
   protected boolean accepts(jd var1, double var2, double var4) {
      if (this.GW.contains(var1) && this.f.getOrDefault(var1, List.of()).stream().anyMatch(var4x -> var4x.contains(var2, var4))) {
         return false;
      } else {
         return !this.GW.contains(var1) && !this.f.values().stream().anyMatch(var1x -> var1x.contains(var1))
            ? super.accepts(var1, var2, var4)
            : !this.J && var1.contains(var2, var4) && var4 >= this.bodyTop() && var4 < this.y.bottom() && var2 < this.N();
      }
   }

   @Override
   protected void focusChanged(jd var1) {
      if (!this.GW.contains(var1) && !this.f.values().stream().anyMatch(var1x -> var1x.contains(var1))) {
         super.focusChanged(var1);
      } else {
         if (var1.u < this.bodyTop()) {
            this.G9 = this.G9 - (this.bodyTop() - var1.u);
         } else if (var1.u + var1.J > this.y.bottom()) {
            this.G9 = this.G9 + (var1.u + var1.J - this.y.bottom());
         }

         this.D.F(this.G9);
         this.layout();
      }
   }

   @Override
   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (!this.q() && (this.y.columns() || !this.K()) && var1 >= this.y.contentX() && var1 < this.N() && var3 >= this.bodyTop() && var3 < this.y.bottom()) {
         this.G9 -= (int)(var7 * 27.0);
         this.layout();
         return true;
      } else {
         return super.mouseScrolled(var1, var3, var5, var7);
      }
   }

   @Override
   protected void dismissDialog() {
      if (this.J && !this.t) {
         if (this.K() && !this.T && this.Gc == null && this.Q.n == null && !this.c && this.Ga.isEmpty()) {
            this.Q.z = null;
            this.Q.j = null;
            this.i.V();
         } else if (this.T) {
            this.T = (boolean)0;
         } else if (this.Gc != null) {
            this.Gc = this.G2 = null;
         } else if (this.Q.n != null) {
            this.Q.n = null;
         } else {
            this.c = (boolean)0;
            this.Ga = "";
         }

         this.build();
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (k2.a(var1) == 256 && this.J && !this.t) {
         this.dismissDialog();
         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   public void tick() {
      this.i.L();
   }

   @Override
   void suspend() {
      this.t = (boolean)0;
      this.G1.v();
      super.suspend();
   }
}

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
import net.minecraft.class_11908;
import net.minecraft.class_310;
import net.minecraft.class_332;

public final class tf extends t6 {
   final ja Jd = ja.n;
   final qk L = LarpClient.b().e();
   final s_ c = new s_(this);
   private final so u = new so(this);
   String JK = "";
   String Jr = "";
   String U = "";
   boolean JP;
   boolean JT;
   private boolean JE;
   private boolean M;
   private String E = "";
   private String J = "";
   private Runnable t;
   private Runnable Y;
   private String Jp = "";
   private String k = "Confirm";
   private String Jb = "";
   private String q = "Cancel";
   private boolean Q;
   private final List<ls> J2 = new ArrayList<>();
   private final List<ls> Jx = new ArrayList<>();
   private final Map<ls, List<ls>> JB = new LinkedHashMap<>();
   private lo JC;
   private lo d;
   private lo J_;
   private int Ja;
   private int g;
   private int Jg = 62;
   private final qb V = new qb(0.0);
   private final se<JsonObject> o = new se<>(var1x -> this.Z().execute(var1x), this::b);

   tf(tk var1) {
      super("Configs");
      this.X = var1;
   }

   private boolean b() {
      return this.Z().field_1755 == this.X && this.X.b(this);
   }

   List<Module> P() {
      return LarpClient.b().G().M();
   }

   class_310 Z() {
      return this.X.c();
   }

   void z(lc var1) {
      this.K.add(var1);
   }

   @Override
   protected void text(String var1) {
      super.text(var1);
   }

   ls J(String var1, Runnable var2) {
      return this.s(var1, () -> {
         if (!this.JP) {
            this.E(var2);
         }
      });
   }

   @Override
   protected ls option(String var1, String var2, Runnable var3) {
      return super.option(var1, var2, () -> {
         if (!this.JP) {
            this.E(var3);
         }
      });
   }

   ls f(String var1, Runnable var2) {
      return this.k(var1, () -> {
         if (!this.JP) {
            this.E(var2);
         }
      });
   }

   void m(String var1, String var2) {
      this.h = var1;
      this.B = var2;
   }

   lo w(String var1, String var2, int var3, Consumer<String> var4) {
      lo var5 = new lo(var1, var4, var3);
      var5.h = var2;
      this.u(var5);
      return var5;
   }

   void I(String var1, String var2, boolean var3, Runnable var4) {
      ls var5 = new ls(var1, () -> {
         if (!this.JP) {
            this.Jr = "";
            this.W();
            this.E(var4);
         }
      });
      var5.S = (boolean)1;
      var5.E = (boolean)1;
      var5.T = var2;
      var5.o = var2.equals("Active");
      this.J2.add(var5);
      this.K.add(var5);
   }

   void y(String var1, Runnable var2) {
      if (!this.J2.isEmpty()) {
         ls var3 = this.J2.getLast();
         ls var4 = new ls(var1, () -> {
            if (!this.JP) {
               this.E(var2);
            }
         });
         var4.v = (boolean)(!var1.equals("Load") && !var1.equals("Update") ? 0 : 1);
         var4.L = var1.equals("Delete");
         var4.x = (boolean)1;
         this.JB.computeIfAbsent(var3, var0 -> new ArrayList<>()).add(var4);
         this.K.add(var4);
      }
   }

   private void z(String var1, Runnable var2) {
      ls var3 = new ls(var1, () -> {
         if (!this.JP) {
            this.E(var2);
         }
      });
      var3.S = (boolean)1;
      this.Jx.add(var3);
      this.K.add(var3);
   }

   private void E(Runnable var1) {
      try {
         var1.run();
      } catch (IllegalArgumentException var3) {
         this.JK = var3.getMessage();
         q8.L("Config", "Config action rejected: {}", this.JK);
         this.build();
      } catch (RuntimeException var4) {
         this.JK = "The operation could not be completed. Try again.";
         q8.u("Config", "Config action failed", var4);
         this.build();
      }
   }

   @Override
   protected void build() {
      this.o();
      this.J2.clear();
      this.Jx.clear();
      this.JB.clear();
      this.d = this.J_ = null;
      this.e = (boolean)(!this.JE && !this.JT && this.c.g == null && this.t == null && this.E.isEmpty() && !this.a() ? 0 : 1);
      this.S("My Configs", !this.M, () -> this.b("My Configs"));
      this.S("Discover", this.M, () -> this.b("Discover"));
      this.JC = new lo(this.M ? "Search this page of configs..." : "Search configs...", var1x -> {
         if (this.M) {
            this.u.X = var1x;
            this.u.g = System.currentTimeMillis() + 180L;
         } else {
            this.U = var1x;
            this.M();
         }
      }, 80);
      this.JC.h = this.M ? this.u.X : this.U;
      this.K.add(this.JC);
      if (!this.M) {
         this.z("Save as new config", () -> this.V("New", ""));
         this.z("Import config", () -> {
            this.JT = (boolean)1;
            this.f = 0;
            this.build();
            if (this.J_ != null) {
               this.p(this.J_);
            }
         });
         if (this.r != null && !this.r.columns()) {
            this.z("More", () -> {
               this.JE = (boolean)1;
               this.f = 0;
               this.build();
            });
         } else {
            this.z("Refresh shared", () -> this.c.c(false));
            if (this.Jd.C()) {
               this.z(this.c.y ? "Regular storage" : "Experimental storage", this::S);
            }
         }

         if (!this.Jr.isEmpty()) {
            this.z("Copy share code", () -> {
               this.Z().field_1774.method_1455(this.Jr);
               this.JK = "Share code copied to clipboard.";
            });
         }
      } else {
         this.z("Refresh", this.u::k);
         this.z("Previous", () -> {
            if (this.u.d > 0) {
               this.u.d = Math.max(0, this.u.d - 30);
               this.u.k();
            }
         });
         this.z("Next", () -> {
            if (this.u.d + 30 < this.u.T) {
               this.u.d += 30;
               this.u.k();
            }
         });
         this.Jx.get(1).N = (boolean)(this.u.d > 0 ? 1 : 0);
         this.Jx.get(2).N = (boolean)(this.u.d + 30 < this.u.T ? 1 : 0);
      }

      if (this.M) {
         this.u.C();
      } else {
         this.c.t();
      }

      if (this.JE) {
         this.m("Shared configs", "");
         this.option("Refresh shared", "Add your uploaded configs to this list", () -> {
            this.JE = (boolean)0;
            this.c.c(false);
         });
         if (this.Jd.C()) {
            this.option(
               this.c.y ? "Regular storage" : "Experimental storage",
               this.c.y ? "Show your regular shared configs" : "Show configs in experimental storage",
               () -> {
                  this.JE = (boolean)0;
                  this.S();
               }
            );
         }
      } else if (this.t != null) {
         int var1 = this.Jp.indexOf(10);
         this.m(var1 < 0 ? this.Jp : this.Jp.substring(0, var1), "");
         if (var1 >= 0) {
            this.text(this.Jp.substring(var1 + 1));
         }

         this.f(this.q, () -> {
            this.t = this.Y = null;
            this.build();
         });
         if (this.Y != null) {
            this.f(this.Jb, () -> this.I(this.Y));
         }

         ls var2 = this.f(this.k, () -> this.I(this.t));
         var2.v = (boolean)1;
         var2.L = this.Q;
      } else if (this.c.g != null) {
         this.c.Q();
      } else if (this.JT) {
         this.m("Import config", "Paste a share code. Nothing loads until you confirm.");
         this.J_ = this.c.J();
         this.f("Cancel", () -> {
            this.JT = (boolean)0;
            this.build();
         });
         this.f("Fetch preview", () -> this.c.u(t4.c(this.c.n))).v = (boolean)1;
      } else if (!this.E.isEmpty()) {
         switch (this.E) {
            case "New":
               this.m("Save as new config", "Saves your current settings. Nothing is loaded.");
               break;
            case "Duplicate":
               this.m("Duplicate “" + this.c.q + "”", "Name the copy.");
               break;
            default:
               this.m(this.E + " “" + this.c.q + "”", "");
         }

         this.d = this.w("Config name", this.J, 32, var1x -> this.J = var1x);
         this.f("Cancel", () -> {
            this.E = "";
            this.build();
         });
         this.f(this.E.equals("Rename") ? "Rename" : "Save", this::V).v = (boolean)1;
      } else if (this.M) {
         this.u.p();
      } else {
         this.c.Y();
      }

      this.v = this.M ? "Discover" : "My Configs";

      for (lc var6 : this.K) {
         if (!(var6 instanceof l5)) {
            var6.N = (boolean)(var6.N & (!this.JP ? 1 : 0));
         }
      }

      if (this.r != null) {
         this.layout();
      }
   }

   private void S() {
      this.c.y = (boolean)(!this.c.y ? 1 : 0);
      this.c.C();
      this.c.w = List.of();
      this.c.c(false);
   }

   void F() {
      this.M();
   }

   private void M() {
      int var1 = this.JC != null && this.JC.M ? 1 : 0;
      this.Ja = 0;
      this.build();
      if (var1 != 0) {
         this.p(this.JC);
      }
   }

   void V(String var1, String var2) {
      this.E = var1;
      this.J = var2;
      this.f = 0;
      this.build();
      if (this.d != null) {
         this.p(this.d);
      }
   }

   private void V() {
      this.X.D();
      String var1 = com.larpclient.h.Y(this.J);

      try {
         if (this.E.equals("Rename")) {
            this.L.v(this.c.q, var1);
         } else if (this.E.equals("Duplicate")) {
            this.L.b(this.c.q, var1);
         } else {
            this.L.f(var1, false);
         }

         this.c.q = var1;
         this.E = "";
         this.JK = "Saved " + var1;
         this.build();
      } catch (IOException var3) {
         this.JK = var3.getMessage();
         q8.L("Config", "Config {} failed: {}", this.E, var3.toString());
         this.build();
      }
   }

   void b(String var1) {
      if (!this.JP) {
         this.W();
         this.M = var1.equals("Discover");
         this.c.g = null;
         this.JT = (boolean)0;
         this.JE = (boolean)0;
         this.E = "";
         this.t = null;
         this.u.I();
         this.f = this.Ja = 0;
         this.Jr = "";
         this.JK = "";
         q8.G("GUI", "Configs switched to {}", this.M ? "Discover" : "My Configs");
         if (this.M && !this.u.f) {
            this.u.k();
         } else {
            this.build();
         }
      }
   }

   boolean f() {
      return this.M && !this.e;
   }

   void s(String var1, String var2, Runnable var3) {
      this.V(var1, var2, var3, "", null);
   }

   void W(String var1, String var2, Runnable var3) {
      this.V(var1, var2, var3, "", null);
      this.Q = (boolean)1;
      this.build();
   }

   void V(String var1, String var2, Runnable var3, String var4, Runnable var5) {
      this.Jp = var1;
      this.k = var2;
      this.t = var3;
      this.Q = (boolean)0;
      this.q = "Cancel";
      this.Jb = var4;
      this.Y = var5;
      this.f = 0;
      this.build();
   }

   void U(String var1, String var2, Runnable var3, String var4) {
      this.V(var1, var2, var3, "", null);
      this.q = var4;
      this.build();
   }

   private void I(Runnable var1) {
      this.t = this.Y = null;
      this.JT = (boolean)0;
      var1.run();
   }

   void y(String var1, CompletableFuture<JsonObject> var2, Consumer<JsonObject> var3) {
      if (this.JP) {
         var2.cancel(true);
      } else {
         this.JP = (boolean)1;
         this.JK = var1;
         this.build();
         com.larpclient.f.h("API", f.q.M);
         this.o.P(var2, (var3x, var4) -> {
            this.JP = (boolean)0;
            if (var4 != null) {
               com.larpclient.f.h("API", f.q.t);
               this.JK = this.Jd.v(var4);
               q8.L("API", "Config request failed: {}", this.JK);
               u6 var5 = ja.E(var4);
               if (var5.X().equals("REVISION_CONFLICT") && !var1.equals("Loading cloud items...")) {
                  this.c.c(true);
                  return;
               }

               if (var5.M() == 403 && this.c.y) {
                  this.c.P();
               }

               if (var5.M() == 401 || var5.M() == 503 || var5.X().equals("NETWORK")) {
                  this.Jd.s();
               }
            } else {
               try {
                  var3.accept(var3x);
                  com.larpclient.f.h("API", f.q.f);
               } catch (RuntimeException var6) {
                  this.JK = "Invalid server response. Nothing was loaded locally.";
                  q8.L("API", "Invalid config response: {}", var6.toString());
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
      return this.e;
   }

   @Override
   protected int bodyTop() {
      return this.r.bodyTop() + this.Jg;
   }

   private int M() {
      return this.r.right();
   }

   private boolean a() {
      return this.M ? this.u.U != null : this.c.M != null || this.c.q != null;
   }

   @Override
   protected void layout() {
      int var1 = this.r.contentX();
      int var2 = this.r.right() - var1;
      int var3 = Math.min(68, Math.max(1, var2 - 60));
      int var4 = Math.min(180, Math.max(1, var2 - var3));
      this.JC.bounds(var1 + var3, this.r.bodyTop(), var4);
      this.JC.w = 20;
      lz.r var5 = lz.x(var2, this.Jx.stream().map(var0 -> uw.C(var0.O, uw.c.M) + 12).toList());
      this.Jg = 27 + var5.height() + 8;

      for (int var6 = 0; var6 < this.Jx.size(); var6++) {
         ls var7 = this.Jx.get(var6);
         lz.v var8 = var5.cells().get(var6);
         var7.x = (boolean)1;
         var7.bounds(var1 + var8.x(), this.r.bodyTop() + 27 + var8.y(), var8.width());
         var7.w = 24;
      }

      super.layout();
      e0 var12 = e0.calculate(var2, this.J2.size());
      int var13 = Math.max(1, this.r.bottom() - this.bodyTop());
      this.g = var12.height();
      this.Ja = Math.clamp(this.Ja, 0, Math.max(0, this.g - var13));
      int var14 = (int)Math.round(this.V.a(this.Ja, 150));

      for (int var9 = 0; var9 < this.J2.size(); var9++) {
         ls var10 = this.J2.get(var9);
         var10.bounds(var1 + var12.x(var9), this.bodyTop() + var12.y(var9) - var14, var12.cardWidth());
         var10.w = 48;
         List var11 = this.JB.getOrDefault(var10, List.of());
         X(var10, var11);
      }
   }

   static void X(ls var0, List<ls> var1) {
      int var2 = var1.stream().mapToInt(var0x -> var0x.L ? 36 : 46).sum();
      int var3 = Math.max(0, var1.size() - 1) * 4;
      int var4 = var0.d - ls.p(var0.d) - 6 - var3;
      double var5 = Math.min(1.0, (double)Math.max(1, var4) / Math.max(1, var2));
      int var7 = var1.stream().mapToInt(var2x -> Math.max(1, (int)((var2x.L ? 36 : 46) * var5))).sum() + var3;
      int var8 = var0.D + var0.d - 6 - var7;

      for (ls var10 : var1) {
         int var11 = Math.max(1, (int)((var10.L ? 36 : 46) * var5));
         var10.bounds(var8, var0.g + 31, var11);
         var10.w = 14;
         var10.I = var0.I;
         var8 += var11 + 4;
      }
   }

   @Override
   protected void renderExtra(class_332 var1, int var2, int var3) {
      uw.X(var1, uw.X("Configs", Math.max(1, this.JC.D - this.r.contentX() - 6), uw.c.v), this.r.contentX(), this.r.bodyTop() + 3, ez.H, uw.c.v);
      this.JC.render(var1, var2, var3);

      for (ls var5 : this.Jx) {
         var5.render(var1, var2, var3);
      }

      var1.method_44379(this.r.contentX(), this.bodyTop(), this.M(), this.r.bottom());

      try {
         int var12 = var3 >= this.bodyTop() && var3 < this.r.bottom() ? var2 : -1;

         for (ls var6 : this.J2) {
            if (var6.g + var6.w > this.bodyTop() && var6.g < this.r.bottom()) {
               var6.render(var1, var12, var3);

               for (ls var8 : this.JB.getOrDefault(var6, List.of())) {
                  var8.render(var1, var12, var3);
               }
            }
         }
      } finally {
         var1.method_44380();
      }

      com.larpclient.p.t(var1, this.M() - 3, this.bodyTop(), Math.max(1, this.r.bottom() - this.bodyTop()), this.g, (int)Math.round(this.V.u()));
      if (this.J2.isEmpty()) {
         uw.G(var1, this.JP ? "Loading..." : "No configs found", this.r.contentX() + 8, this.bodyTop() + 8, ez.PH);
      }
   }

   @Override
   protected boolean accepts(lc var1, double var2, double var4) {
      if (this.J2.contains(var1) && this.JB.getOrDefault(var1, List.of()).stream().anyMatch(var4x -> var4x.contains(var2, var4))) {
         return false;
      } else {
         return !this.J2.contains(var1) && !this.JB.values().stream().anyMatch(var1x -> var1x.contains(var1))
            ? super.accepts(var1, var2, var4)
            : !this.e && var1.contains(var2, var4) && var4 >= this.bodyTop() && var4 < this.r.bottom() && var2 < this.M();
      }
   }

   @Override
   protected void focusChanged(lc var1) {
      if (!this.J2.contains(var1) && !this.JB.values().stream().anyMatch(var1x -> var1x.contains(var1))) {
         super.focusChanged(var1);
      } else {
         if (var1.g < this.bodyTop()) {
            this.Ja = this.Ja - (this.bodyTop() - var1.g);
         } else if (var1.g + var1.w > this.r.bottom()) {
            this.Ja = this.Ja + (var1.g + var1.w - this.r.bottom());
         }

         this.V.w(this.Ja);
         this.layout();
      }
   }

   @Override
   public boolean method_25401(double var1, double var3, double var5, double var7) {
      if (!this.N() && (this.r.columns() || !this.a()) && var1 >= this.r.contentX() && var1 < this.M() && var3 >= this.bodyTop() && var3 < this.r.bottom()) {
         this.Ja -= (int)(var7 * 27.0);
         this.layout();
         return true;
      } else {
         return super.method_25401(var1, var3, var5, var7);
      }
   }

   @Override
   protected void dismissDialog() {
      if (this.e && !this.JP) {
         if (this.a() && !this.JE && this.t == null && this.c.g == null && !this.JT && this.E.isEmpty()) {
            this.c.M = null;
            this.c.q = null;
            this.u.I();
         } else if (this.JE) {
            this.JE = (boolean)0;
         } else if (this.t != null) {
            this.t = this.Y = null;
         } else if (this.c.g != null) {
            this.c.g = null;
         } else {
            this.JT = (boolean)0;
            this.E = "";
         }

         this.build();
      }
   }

   @Override
   public boolean method_25404(class_11908 var1) {
      if (s2.i(var1) == 256 && this.e && !this.JP) {
         this.dismissDialog();
         return true;
      } else {
         return super.method_25404(var1);
      }
   }

   public void method_25393() {
      this.u.e();
   }

   @Override
   void suspend() {
      this.JP = (boolean)0;
      this.o.K();
      super.suspend();
   }
}

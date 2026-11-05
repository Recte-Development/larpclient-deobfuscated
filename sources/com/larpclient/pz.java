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

public final class pz extends pb {
   final hl K = hl.E;
   final mo dQ = LarpClient.y().v();
   final _p dn = new _p(this);
   private final uz db = new uz(this);
   String V = "";
   String dI = "";
   String dy = "";
   boolean E;
   boolean da;
   private boolean dT;
   private boolean d5;
   private String d9 = "";
   private String s = "";
   private Runnable G;
   private Runnable X;
   private String R = "";
   private String d1 = "Confirm";
   private String dU = "";
   private String q = "Cancel";
   private boolean f;
   private final List<p7> a = new ArrayList<>();
   private final List<p7> y = new ArrayList<>();
   private final Map<p7, List<p7>> r = new LinkedHashMap<>();
   private p_ di;
   private p_ dr;
   private p_ dK;
   private int w;
   private int I;
   private int i = 62;
   private final u7 dV = new u7(0.0);
   private final ml<JsonObject> S = new ml<>(var1x -> this.S().execute(var1x), this::D);

   pz(p0 var1) {
      super("Configs");
      this.x = var1;
   }

   private boolean D() {
      return this.S().gui.screen() == this.x && this.x.H(this);
   }

   List<Module> W() {
      return LarpClient.y().e().X();
   }

   Minecraft S() {
      return this.x.M();
   }

   void j(oq var1) {
      this.k.add(var1);
   }

   @Override
   protected void text(String var1) {
      super.text(var1);
   }

   p7 o(String var1, Runnable var2) {
      return this.L(var1, () -> {
         if (!this.E) {
            this.s(var2);
         }
      });
   }

   @Override
   protected p7 option(String var1, String var2, Runnable var3) {
      return super.option(var1, var2, () -> {
         if (!this.E) {
            this.s(var3);
         }
      });
   }

   p7 V(String var1, Runnable var2) {
      return this.Y(var1, () -> {
         if (!this.E) {
            this.s(var2);
         }
      });
   }

   void t(String var1, String var2) {
      this.C = var1;
      this.B = var2;
   }

   p_ k(String var1, String var2, int var3, Consumer<String> var4) {
      p_ var5 = new p_(var1, var4, var3);
      var5.V = var2;
      this.e(var5);
      return var5;
   }

   void f(String var1, String var2, boolean var3, Runnable var4) {
      p7 var5 = new p7(var1, () -> {
         if (!this.E) {
            this.dI = "";
            this.D();
            this.s(var4);
         }
      });
      var5.c = (boolean)1;
      var5.P = (boolean)1;
      var5.y = var2;
      var5.R = var2.equals("Active");
      this.a.add(var5);
      this.k.add(var5);
   }

   void R(String var1, Runnable var2) {
      if (!this.a.isEmpty()) {
         p7 var3 = this.a.getLast();
         p7 var4 = new p7(var1, () -> {
            if (!this.E) {
               this.s(var2);
            }
         });
         var4.d = (boolean)(!var1.equals("Load") && !var1.equals("Update") ? 0 : 1);
         var4.z = var1.equals("Delete");
         var4.q = (boolean)1;
         this.r.computeIfAbsent(var3, var0 -> new ArrayList<>()).add(var4);
         this.k.add(var4);
      }
   }

   private void z(String var1, Runnable var2) {
      p7 var3 = new p7(var1, () -> {
         if (!this.E) {
            this.s(var2);
         }
      });
      var3.c = (boolean)1;
      this.y.add(var3);
      this.k.add(var3);
   }

   private void s(Runnable var1) {
      try {
         var1.run();
      } catch (IllegalArgumentException var3) {
         this.V = var3.getMessage();
         r4.m("Config", "Config action rejected: {}", this.V);
         this.build();
      } catch (RuntimeException var4) {
         this.V = "The operation could not be completed. Try again.";
         r4.r("Config", "Config action failed", var4);
         this.build();
      }
   }

   @Override
   protected void build() {
      this.f();
      this.a.clear();
      this.y.clear();
      this.r.clear();
      this.dr = this.dK = null;
      this.h = (boolean)(!this.dT && !this.da && this.dn.L == null && this.G == null && this.d9.isEmpty() && !this.n() ? 0 : 1);
      this.o("My Configs", !this.d5, () -> this.D("My Configs"));
      this.o("Discover", this.d5, () -> this.D("Discover"));
      this.di = new p_(this.d5 ? "Search this page of configs..." : "Search configs...", var1x -> {
         if (this.d5) {
            this.db.R = var1x;
            this.db.f = System.currentTimeMillis() + 180L;
         } else {
            this.dy = var1x;
            this.p();
         }
      }, 80);
      this.di.V = this.d5 ? this.db.R : this.dy;
      this.k.add(this.di);
      if (!this.d5) {
         this.z("Save as new config", () -> this.a("New", ""));
         this.z("Import config", () -> {
            this.da = (boolean)1;
            this.e = 0;
            this.build();
            if (this.dK != null) {
               this.D(this.dK);
            }
         });
         if (this.M != null && !this.M.columns()) {
            this.z("More", () -> {
               this.dT = (boolean)1;
               this.e = 0;
               this.build();
            });
         } else {
            this.z("Refresh shared", () -> this.dn.w(false));
            if (this.K.r()) {
               this.z(this.dn.D ? "Regular storage" : "Experimental storage", this::I);
            }
         }

         if (!this.dI.isEmpty()) {
            this.z("Copy share code", () -> {
               this.S().keyboardHandler.setClipboard(this.dI);
               this.V = "Share code copied to clipboard.";
            });
         }
      } else {
         this.z("Refresh", this.db::f);
         this.z("Previous", () -> {
            if (this.db.b > 0) {
               this.db.b = Math.max(0, this.db.b - 30);
               this.db.f();
            }
         });
         this.z("Next", () -> {
            if (this.db.b + 30 < this.db.M) {
               this.db.b += 30;
               this.db.f();
            }
         });
         this.y.get(1).x = (boolean)(this.db.b > 0 ? 1 : 0);
         this.y.get(2).x = (boolean)(this.db.b + 30 < this.db.M ? 1 : 0);
      }

      if (this.d5) {
         this.db.e();
      } else {
         this.dn.C();
      }

      if (this.dT) {
         this.t("Shared configs", "");
         this.option("Refresh shared", "Add your uploaded configs to this list", () -> {
            this.dT = (boolean)0;
            this.dn.w(false);
         });
         if (this.K.r()) {
            this.option(
               this.dn.D ? "Regular storage" : "Experimental storage",
               this.dn.D ? "Show your regular shared configs" : "Show configs in experimental storage",
               () -> {
                  this.dT = (boolean)0;
                  this.I();
               }
            );
         }
      } else if (this.G != null) {
         int var1 = this.R.indexOf(10);
         this.t(var1 < 0 ? this.R : this.R.substring(0, var1), "");
         if (var1 >= 0) {
            this.text(this.R.substring(var1 + 1));
         }

         this.V(this.q, () -> {
            this.G = this.X = null;
            this.build();
         });
         if (this.X != null) {
            this.V(this.dU, () -> this.r(this.X));
         }

         p7 var2 = this.V(this.d1, () -> this.r(this.G));
         var2.d = (boolean)1;
         var2.z = this.f;
      } else if (this.dn.L != null) {
         this.dn.X();
      } else if (this.da) {
         this.t("Import config", "Paste a share code. Nothing loads until you confirm.");
         this.dK = this.dn.M();
         this.V("Cancel", () -> {
            this.da = (boolean)0;
            this.build();
         });
         this.V("Fetch preview", () -> this.dn.W(_4.p(this.dn.T))).d = (boolean)1;
      } else if (!this.d9.isEmpty()) {
         switch (this.d9) {
            case "New":
               this.t("Save as new config", "Saves your current settings. Nothing is loaded.");
               break;
            case "Duplicate":
               this.t("Duplicate “" + this.dn.e + "”", "Name the copy.");
               break;
            default:
               this.t(this.d9 + " “" + this.dn.e + "”", "");
         }

         this.dr = this.k("Config name", this.s, 32, var1x -> this.s = var1x);
         this.V("Cancel", () -> {
            this.d9 = "";
            this.build();
         });
         this.V(this.d9.equals("Rename") ? "Rename" : "Save", this::h).d = (boolean)1;
      } else if (this.d5) {
         this.db.L();
      } else {
         this.dn.e();
      }

      this.Y = this.d5 ? "Discover" : "My Configs";

      for (oq var6 : this.k) {
         if (!(var6 instanceof p8)) {
            var6.x = (boolean)(var6.x & (!this.E ? 1 : 0));
         }
      }

      if (this.M != null) {
         this.layout();
      }
   }

   private void I() {
      this.dn.D = (boolean)(!this.dn.D ? 1 : 0);
      this.dn.W();
      this.dn.O = List.of();
      this.dn.w(false);
   }

   void O() {
      this.p();
   }

   private void p() {
      int var1 = this.di != null && this.di.A ? 1 : 0;
      this.w = 0;
      this.build();
      if (var1 != 0) {
         this.D(this.di);
      }
   }

   void a(String var1, String var2) {
      this.d9 = var1;
      this.s = var2;
      this.e = 0;
      this.build();
      if (this.dr != null) {
         this.D(this.dr);
      }
   }

   private void h() {
      this.x.i();
      String var1 = yj.S(this.s);

      try {
         if (this.d9.equals("Rename")) {
            this.dQ.c(this.dn.e, var1);
         } else if (this.d9.equals("Duplicate")) {
            this.dQ.i(this.dn.e, var1);
         } else {
            this.dQ.H(var1, false);
         }

         this.dn.e = var1;
         this.d9 = "";
         this.V = "Saved " + var1;
         this.build();
      } catch (IOException var3) {
         this.V = var3.getMessage();
         r4.m("Config", "Config {} failed: {}", this.d9, var3.toString());
         this.build();
      }
   }

   void D(String var1) {
      if (!this.E) {
         this.D();
         this.d5 = var1.equals("Discover");
         this.dn.L = null;
         this.da = (boolean)0;
         this.dT = (boolean)0;
         this.d9 = "";
         this.G = null;
         this.db.u();
         this.e = this.w = 0;
         this.dI = "";
         this.V = "";
         r4.V("GUI", "Configs switched to {}", this.d5 ? "Discover" : "My Configs");
         if (this.d5 && !this.db.m) {
            this.db.f();
         } else {
            this.build();
         }
      }
   }

   boolean e() {
      return this.d5 && !this.h;
   }

   void J(String var1, String var2, Runnable var3) {
      this.c(var1, var2, var3, "", null);
   }

   void L(String var1, String var2, Runnable var3) {
      this.c(var1, var2, var3, "", null);
      this.f = (boolean)1;
      this.build();
   }

   void c(String var1, String var2, Runnable var3, String var4, Runnable var5) {
      this.R = var1;
      this.d1 = var2;
      this.G = var3;
      this.f = (boolean)0;
      this.q = "Cancel";
      this.dU = var4;
      this.X = var5;
      this.e = 0;
      this.build();
   }

   void B(String var1, String var2, Runnable var3, String var4) {
      this.c(var1, var2, var3, "", null);
      this.q = var4;
      this.build();
   }

   private void r(Runnable var1) {
      this.G = this.X = null;
      this.da = (boolean)0;
      var1.run();
   }

   void t(String var1, CompletableFuture<JsonObject> var2, Consumer<JsonObject> var3) {
      if (this.E) {
         var2.cancel(true);
      } else {
         this.E = (boolean)1;
         this.V = var1;
         this.build();
         lf.w("API", lf.t.v);
         this.S.I(var2, (var3x, var4) -> {
            this.E = (boolean)0;
            if (var4 != null) {
               lf.w("API", lf.t.F);
               this.V = this.K.V(var4);
               r4.m("API", "Config request failed: {}", this.V);
               oj var5 = hl.D(var4);
               if (var5.f().equals("REVISION_CONFLICT") && !var1.equals("Loading cloud items...")) {
                  this.dn.w(true);
                  return;
               }

               if (var5.M() == 403 && this.dn.D) {
                  this.dn.Z();
               }

               if (var5.M() == 401 || var5.M() == 503 || var5.f().equals("NETWORK")) {
                  this.K.f();
               }
            } else {
               try {
                  var3.accept(var3x);
                  lf.w("API", lf.t.f);
               } catch (RuntimeException var6) {
                  this.V = "Invalid server response. Nothing was loaded locally.";
                  r4.m("API", "Invalid config response: {}", var6.toString());
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
      return this.h;
   }

   @Override
   protected int bodyTop() {
      return this.M.bodyTop() + this.i;
   }

   private int X() {
      return this.M.right();
   }

   private boolean n() {
      return this.d5 ? this.db.a != null : this.dn.E != null || this.dn.e != null;
   }

   @Override
   protected void layout() {
      int var1 = this.M.contentX();
      int var2 = this.M.right() - var1;
      int var3 = Math.min(68, Math.max(1, var2 - 60));
      int var4 = Math.min(180, Math.max(1, var2 - var3));
      this.di.bounds(var1 + var3, this.M.bodyTop(), var4);
      this.di.O = 20;
      dn.i var5 = com.larpclient.dn.U(var2, this.y.stream().map(var0 -> j_.o(var0.J, j_.m.U) + 12).toList());
      this.i = 27 + var5.height() + 8;

      for (int var6 = 0; var6 < this.y.size(); var6++) {
         p7 var7 = this.y.get(var6);
         dn.v var8 = var5.cells().get(var6);
         var7.q = (boolean)1;
         var7.bounds(var1 + var8.x(), this.M.bodyTop() + 27 + var8.y(), var8.width());
         var7.O = 24;
      }

      super.layout();
      f3 var12 = f3.calculate(var2, this.a.size());
      int var13 = Math.max(1, this.M.bottom() - this.bodyTop());
      this.I = var12.height();
      this.w = Math.clamp(this.w, 0, Math.max(0, this.I - var13));
      int var14 = (int)Math.round(this.dV.y(this.w, 150));

      for (int var9 = 0; var9 < this.a.size(); var9++) {
         p7 var10 = this.a.get(var9);
         var10.bounds(var1 + var12.x(var9), this.bodyTop() + var12.y(var9) - var14, var12.cardWidth());
         var10.O = 48;
         List var11 = this.r.getOrDefault(var10, List.of());
         f(var10, var11);
      }
   }

   static void f(p7 var0, List<p7> var1) {
      int var2 = var1.stream().mapToInt(var0x -> var0x.z ? 36 : 46).sum();
      int var3 = Math.max(0, var1.size() - 1) * 4;
      int var4 = var0.v - p7.N(var0.v) - 6 - var3;
      double var5 = Math.min(1.0, (double)Math.max(1, var4) / Math.max(1, var2));
      int var7 = var1.stream().mapToInt(var2x -> Math.max(1, (int)((var2x.z ? 36 : 46) * var5))).sum() + var3;
      int var8 = var0.B + var0.v - 6 - var7;

      for (p7 var10 : var1) {
         int var11 = Math.max(1, (int)((var10.z ? 36 : 46) * var5));
         var10.bounds(var8, var0.L + 31, var11);
         var10.O = 14;
         var10.F = var0.F;
         var8 += var11 + 4;
      }
   }

   @Override
   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
      j_.L(var1, j_.d("Configs", Math.max(1, this.di.B - this.M.contentX() - 6), j_.m.a), this.M.contentX(), this.M.bodyTop() + 3, rv.P, j_.m.a);
      this.di.render(var1, var2, var3);

      for (p7 var5 : this.y) {
         var5.render(var1, var2, var3);
      }

      var1.enableScissor(this.M.contentX(), this.bodyTop(), this.X(), this.M.bottom());

      try {
         int var12 = var3 >= this.bodyTop() && var3 < this.M.bottom() ? var2 : -1;

         for (p7 var6 : this.a) {
            if (var6.L + var6.O > this.bodyTop() && var6.L < this.M.bottom()) {
               var6.render(var1, var12, var3);

               for (p7 var8 : this.r.getOrDefault(var6, List.of())) {
                  var8.render(var1, var12, var3);
               }
            }
         }
      } finally {
         var1.disableScissor();
      }

      ll.g(var1, this.X() - 3, this.bodyTop(), Math.max(1, this.M.bottom() - this.bodyTop()), this.I, (int)Math.round(this.dV.V()));
      if (this.a.isEmpty()) {
         j_.E(var1, this.E ? "Loading..." : "No configs found", this.M.contentX() + 8, this.bodyTop() + 8, rv.G0);
      }
   }

   @Override
   protected boolean accepts(oq var1, double var2, double var4) {
      if (this.a.contains(var1) && this.r.getOrDefault(var1, List.of()).stream().anyMatch(var4x -> var4x.contains(var2, var4))) {
         return false;
      } else {
         return !this.a.contains(var1) && !this.r.values().stream().anyMatch(var1x -> var1x.contains(var1))
            ? super.accepts(var1, var2, var4)
            : !this.h && var1.contains(var2, var4) && var4 >= this.bodyTop() && var4 < this.M.bottom() && var2 < this.X();
      }
   }

   @Override
   protected void focusChanged(oq var1) {
      if (!this.a.contains(var1) && !this.r.values().stream().anyMatch(var1x -> var1x.contains(var1))) {
         super.focusChanged(var1);
      } else {
         if (var1.L < this.bodyTop()) {
            this.w = this.w - (this.bodyTop() - var1.L);
         } else if (var1.L + var1.O > this.M.bottom()) {
            this.w = this.w + (var1.L + var1.O - this.M.bottom());
         }

         this.dV.c(this.w);
         this.layout();
      }
   }

   @Override
   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (!this.m() && (this.M.columns() || !this.n()) && var1 >= this.M.contentX() && var1 < this.X() && var3 >= this.bodyTop() && var3 < this.M.bottom()) {
         this.w -= (int)(var7 * 27.0);
         this.layout();
         return true;
      } else {
         return super.mouseScrolled(var1, var3, var5, var7);
      }
   }

   @Override
   protected void dismissDialog() {
      if (this.h && !this.E) {
         if (this.n() && !this.dT && this.G == null && this.dn.L == null && !this.da && this.d9.isEmpty()) {
            this.dn.E = null;
            this.dn.e = null;
            this.db.u();
         } else if (this.dT) {
            this.dT = (boolean)0;
         } else if (this.G != null) {
            this.G = this.X = null;
         } else if (this.dn.L != null) {
            this.dn.L = null;
         } else {
            this.da = (boolean)0;
            this.d9 = "";
         }

         this.build();
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (r7.g(var1) == 256 && this.h && !this.E) {
         this.dismissDialog();
         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   public void tick() {
      this.db.y();
   }

   @Override
   void suspend() {
      this.E = (boolean)0;
      this.S.w();
      super.suspend();
   }
}

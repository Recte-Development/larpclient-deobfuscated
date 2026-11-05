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

public final class hx extends hb {
   final cm h = cm.R;
   final w9 vQ = LarpClient.F().f();
   final xb v6 = new xb(this);
   private final uu vB = new uu(this);
   String A = "";
   String v1 = "";
   String x = "";
   boolean v9;
   boolean S;
   private boolean B;
   private boolean k;
   private String o = "";
   private String w = "";
   private Runnable vl;
   private Runnable vr;
   private String J = "";
   private String P = "Confirm";
   private String D = "";
   private String O = "Cancel";
   private boolean H;
   private final List<u7> n = new ArrayList<>();
   private final List<u7> vF = new ArrayList<>();
   private final Map<u7, List<u7>> vs = new LinkedHashMap<>();
   private ur va;
   private ur vj;
   private ur vt;
   private int vY;
   private int U;
   private int vH = 62;
   private final ef l = new ef(0.0);
   private final xd<JsonObject> vV = new xd<>(var1x -> this.i().execute(var1x), this::V);

   hx(hw var1) {
      super("Configs");
      this.M = var1;
   }

   private boolean V() {
      return this.i().gui.screen() == this.M && this.M.J(this);
   }

   List<Module> i() {
      return LarpClient.F().x().C();
   }

   Minecraft i() {
      return this.M.F();
   }

   void T(b var1) {
      this.G.add(var1);
   }

   @Override
   protected void text(String var1) {
      super.text(var1);
   }

   u7 r(String var1, Runnable var2) {
      return this.U(var1, () -> {
         if (!this.v9) {
            this.Y(var2);
         }
      });
   }

   @Override
   protected u7 option(String var1, String var2, Runnable var3) {
      return super.option(var1, var2, () -> {
         if (!this.v9) {
            this.Y(var3);
         }
      });
   }

   u7 k(String var1, Runnable var2) {
      return this.x(var1, () -> {
         if (!this.v9) {
            this.Y(var2);
         }
      });
   }

   void H(String var1, String var2) {
      this.Q = var1;
      this.E = var2;
   }

   ur Q(String var1, String var2, int var3, Consumer<String> var4) {
      ur var5 = new ur(var1, var4, var3);
      var5.d = var2;
      this.w(var5);
      return var5;
   }

   void N(String var1, String var2, boolean var3, Runnable var4) {
      u7 var5 = new u7(var1, () -> {
         if (!this.v9) {
            this.v1 = "";
            this.P();
            this.Y(var4);
         }
      });
      var5.r = (boolean)1;
      var5.y = (boolean)1;
      var5.u = var2;
      var5.U = var2.equals("Active");
      this.n.add(var5);
      this.G.add(var5);
   }

   void E(String var1, Runnable var2) {
      if (!this.n.isEmpty()) {
         u7 var3 = this.n.getLast();
         u7 var4 = new u7(var1, () -> {
            if (!this.v9) {
               this.Y(var2);
            }
         });
         var4.w = (boolean)(!var1.equals("Load") && !var1.equals("Update") ? 0 : 1);
         var4.k = var1.equals("Delete");
         var4.I = (boolean)1;
         this.vs.computeIfAbsent(var3, var0 -> new ArrayList<>()).add(var4);
         this.G.add(var4);
      }
   }

   private void d(String var1, Runnable var2) {
      u7 var3 = new u7(var1, () -> {
         if (!this.v9) {
            this.Y(var2);
         }
      });
      var3.r = (boolean)1;
      this.vF.add(var3);
      this.G.add(var3);
   }

   private void Y(Runnable var1) {
      try {
         var1.run();
      } catch (IllegalArgumentException var3) {
         this.A = var3.getMessage();
         mt.T("Config", "Config action rejected: {}", this.A);
         this.build();
      } catch (RuntimeException var4) {
         this.A = "The operation could not be completed. Try again.";
         mt.P("Config", "Config action failed", var4);
         this.build();
      }
   }

   @Override
   protected void build() {
      this.d();
      this.n.clear();
      this.vF.clear();
      this.vs.clear();
      this.vj = this.vt = null;
      this.W = (boolean)(!this.B && !this.S && this.v6.K == null && this.vl == null && this.o.isEmpty() && !this.k() ? 0 : 1);
      this.f("My Configs", !this.k, () -> this.V("My Configs"));
      this.f("Discover", this.k, () -> this.V("Discover"));
      this.va = new ur(this.k ? "Search this page of configs..." : "Search configs...", var1x -> {
         if (this.k) {
            this.vB.V = var1x;
            this.vB.D = System.currentTimeMillis() + 180L;
         } else {
            this.x = var1x;
            this.x();
         }
      }, 80);
      this.va.d = this.k ? this.vB.V : this.x;
      this.G.add(this.va);
      if (!this.k) {
         this.d("Save as new config", () -> this.r("New", ""));
         this.d("Import config", () -> {
            this.S = (boolean)1;
            this.Z = 0;
            this.build();
            if (this.vt != null) {
               this.U(this.vt);
            }
         });
         if (this.j != null && !this.j.columns()) {
            this.d("More", () -> {
               this.B = (boolean)1;
               this.Z = 0;
               this.build();
            });
         } else {
            this.d("Refresh shared", () -> this.v6.Q(false));
            if (this.h.R()) {
               this.d(this.v6.R ? "Regular storage" : "Experimental storage", this::b);
            }
         }

         if (!this.v1.isEmpty()) {
            this.d("Copy share code", () -> {
               this.i().keyboardHandler.setClipboard(this.v1);
               this.A = "Share code copied to clipboard.";
            });
         }
      } else {
         this.d("Refresh", this.vB::Q);
         this.d("Previous", () -> {
            if (this.vB.M > 0) {
               this.vB.M = Math.max(0, this.vB.M - 30);
               this.vB.Q();
            }
         });
         this.d("Next", () -> {
            if (this.vB.M + 30 < this.vB.n) {
               this.vB.M += 30;
               this.vB.Q();
            }
         });
         this.vF.get(1).X = (boolean)(this.vB.M > 0 ? 1 : 0);
         this.vF.get(2).X = (boolean)(this.vB.M + 30 < this.vB.n ? 1 : 0);
      }

      if (this.k) {
         this.vB.W();
      } else {
         this.v6.M();
      }

      if (this.B) {
         this.H("Shared configs", "");
         this.option("Refresh shared", "Add your uploaded configs to this list", () -> {
            this.B = (boolean)0;
            this.v6.Q(false);
         });
         if (this.h.R()) {
            this.option(
               this.v6.R ? "Regular storage" : "Experimental storage",
               this.v6.R ? "Show your regular shared configs" : "Show configs in experimental storage",
               () -> {
                  this.B = (boolean)0;
                  this.b();
               }
            );
         }
      } else if (this.vl != null) {
         int var1 = this.J.indexOf(10);
         this.H(var1 < 0 ? this.J : this.J.substring(0, var1), "");
         if (var1 >= 0) {
            this.text(this.J.substring(var1 + 1));
         }

         this.k(this.O, () -> {
            this.vl = this.vr = null;
            this.build();
         });
         if (this.vr != null) {
            this.k(this.D, () -> this.O(this.vr));
         }

         u7 var2 = this.k(this.P, () -> this.O(this.vl));
         var2.w = (boolean)1;
         var2.k = this.H;
      } else if (this.v6.K != null) {
         this.v6.I();
      } else if (this.S) {
         this.H("Import config", "Paste a share code. Nothing loads until you confirm.");
         this.vt = this.v6.m();
         this.k("Cancel", () -> {
            this.S = (boolean)0;
            this.build();
         });
         this.k("Fetch preview", () -> this.v6.S(md.L(this.v6.X))).w = (boolean)1;
      } else if (!this.o.isEmpty()) {
         switch (this.o) {
            case "New":
               this.H("Save as new config", "Saves your current settings. Nothing is loaded.");
               break;
            case "Duplicate":
               this.H("Duplicate “" + this.v6.E + "”", "Name the copy.");
               break;
            default:
               this.H(this.o + " “" + this.v6.E + "”", "");
         }

         this.vj = this.Q("Config name", this.w, 32, var1x -> this.w = var1x);
         this.k("Cancel", () -> {
            this.o = "";
            this.build();
         });
         this.k(this.o.equals("Rename") ? "Rename" : "Save", this::C).w = (boolean)1;
      } else if (this.k) {
         this.vB.O();
      } else {
         this.v6.V();
      }

      this.F = this.k ? "Discover" : "My Configs";

      for (b var6 : this.G) {
         if (!(var6 instanceof us)) {
            var6.X = (boolean)(var6.X & (!this.v9 ? 1 : 0));
         }
      }

      if (this.j != null) {
         this.layout();
      }
   }

   private void b() {
      this.v6.R = (boolean)(!this.v6.R ? 1 : 0);
      this.v6.a();
      this.v6.T = List.of();
      this.v6.Q(false);
   }

   void J() {
      this.x();
   }

   private void x() {
      int var1 = this.va != null && this.va.l ? 1 : 0;
      this.vY = 0;
      this.build();
      if (var1 != 0) {
         this.U(this.va);
      }
   }

   void r(String var1, String var2) {
      this.o = var1;
      this.w = var2;
      this.Z = 0;
      this.build();
      if (this.vj != null) {
         this.U(this.vj);
      }
   }

   private void C() {
      this.M.e();
      String var1 = p_.g(this.w);

      try {
         if (this.o.equals("Rename")) {
            this.vQ.G(this.v6.E, var1);
         } else if (this.o.equals("Duplicate")) {
            this.vQ.s(this.v6.E, var1);
         } else {
            this.vQ.Z(var1, false);
         }

         this.v6.E = var1;
         this.o = "";
         this.A = "Saved " + var1;
         this.build();
      } catch (IOException var3) {
         this.A = var3.getMessage();
         mt.T("Config", "Config {} failed: {}", this.o, var3.toString());
         this.build();
      }
   }

   void V(String var1) {
      if (!this.v9) {
         this.P();
         this.k = var1.equals("Discover");
         this.v6.K = null;
         this.S = (boolean)0;
         this.B = (boolean)0;
         this.o = "";
         this.vl = null;
         this.vB.H();
         this.Z = this.vY = 0;
         this.v1 = "";
         this.A = "";
         mt.K("GUI", "Configs switched to {}", this.k ? "Discover" : "My Configs");
         if (this.k && !this.vB.B) {
            this.vB.Q();
         } else {
            this.build();
         }
      }
   }

   boolean F() {
      return this.k && !this.W;
   }

   void f(String var1, String var2, Runnable var3) {
      this.x(var1, var2, var3, "", null);
   }

   void l(String var1, String var2, Runnable var3) {
      this.x(var1, var2, var3, "", null);
      this.H = (boolean)1;
      this.build();
   }

   void x(String var1, String var2, Runnable var3, String var4, Runnable var5) {
      this.J = var1;
      this.P = var2;
      this.vl = var3;
      this.H = (boolean)0;
      this.O = "Cancel";
      this.D = var4;
      this.vr = var5;
      this.Z = 0;
      this.build();
   }

   void z(String var1, String var2, Runnable var3, String var4) {
      this.x(var1, var2, var3, "", null);
      this.O = var4;
      this.build();
   }

   private void O(Runnable var1) {
      this.vl = this.vr = null;
      this.S = (boolean)0;
      var1.run();
   }

   void n(String var1, CompletableFuture<JsonObject> var2, Consumer<JsonObject> var3) {
      if (this.v9) {
         var2.cancel(true);
      } else {
         this.v9 = (boolean)1;
         this.A = var1;
         this.build();
         wz.e("API", wz.j.J);
         this.vV.F(var2, (var3x, var4) -> {
            this.v9 = (boolean)0;
            if (var4 != null) {
               wz.e("API", wz.j.X);
               this.A = this.h.Y(var4);
               mt.T("API", "Config request failed: {}", this.A);
               pe var5 = cm.G(var4);
               if (var5.l().equals("REVISION_CONFLICT") && !var1.equals("Loading cloud items...")) {
                  this.v6.Q(true);
                  return;
               }

               if (var5.F() == 403 && this.v6.R) {
                  this.v6.o();
               }

               if (var5.F() == 401 || var5.F() == 503 || var5.l().equals("NETWORK")) {
                  this.h.M();
               }
            } else {
               try {
                  var3.accept(var3x);
                  wz.e("API", wz.j.p);
               } catch (RuntimeException var6) {
                  this.A = "Invalid server response. Nothing was loaded locally.";
                  mt.T("API", "Invalid config response: {}", var6.toString());
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
      return this.W;
   }

   @Override
   protected int bodyTop() {
      return this.j.bodyTop() + this.vH;
   }

   private int l() {
      return this.j.right();
   }

   private boolean k() {
      return this.k ? this.vB.v != null : this.v6.h != null || this.v6.E != null;
   }

   @Override
   protected void layout() {
      int var1 = this.j.contentX();
      int var2 = this.j.right() - var1;
      int var3 = Math.min(68, Math.max(1, var2 - 60));
      int var4 = Math.min(180, Math.max(1, var2 - var3));
      this.va.bounds(var1 + var3, this.j.bodyTop(), var4);
      this.va.W = 20;
      e5.v var5 = e5.e(var2, this.vF.stream().map(var0 -> k6.a(var0.S, k6.d.K) + 12).toList());
      this.vH = 27 + var5.height() + 8;

      for (int var6 = 0; var6 < this.vF.size(); var6++) {
         u7 var7 = this.vF.get(var6);
         e5.c var8 = var5.cells().get(var6);
         var7.I = (boolean)1;
         var7.bounds(var1 + var8.x(), this.j.bodyTop() + 27 + var8.y(), var8.width());
         var7.W = 24;
      }

      super.layout();
      wd var12 = wd.calculate(var2, this.n.size());
      int var13 = Math.max(1, this.j.bottom() - this.bodyTop());
      this.U = var12.height();
      this.vY = Math.clamp(this.vY, 0, Math.max(0, this.U - var13));
      int var14 = (int)Math.round(this.l.m(this.vY, 150));

      for (int var9 = 0; var9 < this.n.size(); var9++) {
         u7 var10 = this.n.get(var9);
         var10.bounds(var1 + var12.x(var9), this.bodyTop() + var12.y(var9) - var14, var12.cardWidth());
         var10.W = 48;
         List var11 = this.vs.getOrDefault(var10, List.of());
         N(var10, var11);
      }
   }

   static void N(u7 var0, List<u7> var1) {
      int var2 = var1.stream().mapToInt(var0x -> var0x.k ? 36 : 46).sum();
      int var3 = Math.max(0, var1.size() - 1) * 4;
      int var4 = var0.A - u7.w(var0.A) - 6 - var3;
      double var5 = Math.min(1.0, (double)Math.max(1, var4) / Math.max(1, var2));
      int var7 = var1.stream().mapToInt(var2x -> Math.max(1, (int)((var2x.k ? 36 : 46) * var5))).sum() + var3;
      int var8 = var0.q + var0.A - 6 - var7;

      for (u7 var10 : var1) {
         int var11 = Math.max(1, (int)((var10.k ? 36 : 46) * var5));
         var10.bounds(var8, var0.J + 31, var11);
         var10.W = 14;
         var10.D = var0.D;
         var8 += var11 + 4;
      }
   }

   @Override
   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
      k6.T(var1, k6.o("Configs", Math.max(1, this.va.q - this.j.contentX() - 6), k6.d.m), this.j.contentX(), this.j.bodyTop() + 3, com.larpclient.f.J, k6.d.m);
      this.va.render(var1, var2, var3);

      for (u7 var5 : this.vF) {
         var5.render(var1, var2, var3);
      }

      var1.enableScissor(this.j.contentX(), this.bodyTop(), this.l(), this.j.bottom());

      try {
         int var12 = var3 >= this.bodyTop() && var3 < this.j.bottom() ? var2 : -1;

         for (u7 var6 : this.n) {
            if (var6.J + var6.W > this.bodyTop() && var6.J < this.j.bottom()) {
               var6.render(var1, var12, var3);

               for (u7 var8 : this.vs.getOrDefault(var6, List.of())) {
                  var8.render(var1, var12, var3);
               }
            }
         }
      } finally {
         var1.disableScissor();
      }

      pn.u(var1, this.l() - 3, this.bodyTop(), Math.max(1, this.j.bottom() - this.bodyTop()), this.U, (int)Math.round(this.l.G()));
      if (this.n.isEmpty()) {
         k6.Z(var1, this.v9 ? "Loading..." : "No configs found", this.j.contentX() + 8, this.bodyTop() + 8, com.larpclient.f.s);
      }
   }

   @Override
   protected boolean accepts(b var1, double var2, double var4) {
      if (this.n.contains(var1) && this.vs.getOrDefault(var1, List.of()).stream().anyMatch(var4x -> var4x.contains(var2, var4))) {
         return false;
      } else {
         return !this.n.contains(var1) && !this.vs.values().stream().anyMatch(var1x -> var1x.contains(var1))
            ? super.accepts(var1, var2, var4)
            : !this.W && var1.contains(var2, var4) && var4 >= this.bodyTop() && var4 < this.j.bottom() && var2 < this.l();
      }
   }

   @Override
   protected void focusChanged(b var1) {
      if (!this.n.contains(var1) && !this.vs.values().stream().anyMatch(var1x -> var1x.contains(var1))) {
         super.focusChanged(var1);
      } else {
         if (var1.J < this.bodyTop()) {
            this.vY = this.vY - (this.bodyTop() - var1.J);
         } else if (var1.J + var1.W > this.j.bottom()) {
            this.vY = this.vY + (var1.J + var1.W - this.j.bottom());
         }

         this.l.Q(this.vY);
         this.layout();
      }
   }

   @Override
   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (!this.B() && (this.j.columns() || !this.k()) && var1 >= this.j.contentX() && var1 < this.l() && var3 >= this.bodyTop() && var3 < this.j.bottom()) {
         this.vY -= (int)(var7 * 27.0);
         this.layout();
         return true;
      } else {
         return super.mouseScrolled(var1, var3, var5, var7);
      }
   }

   @Override
   protected void dismissDialog() {
      if (this.W && !this.v9) {
         if (this.k() && !this.B && this.vl == null && this.v6.K == null && !this.S && this.o.isEmpty()) {
            this.v6.h = null;
            this.v6.E = null;
            this.vB.H();
         } else if (this.B) {
            this.B = (boolean)0;
         } else if (this.vl != null) {
            this.vl = this.vr = null;
         } else if (this.v6.K != null) {
            this.v6.K = null;
         } else {
            this.S = (boolean)0;
            this.o = "";
         }

         this.build();
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (mo.O(var1) == 256 && this.W && !this.v9) {
         this.dismissDialog();
         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   public void tick() {
      this.vB.A();
   }

   @Override
   void suspend() {
      this.v9 = (boolean)0;
      this.vV.k();
      super.suspend();
   }
}

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

public final class fk extends fp {
   final bl Pp = bl.F;
   final aq D = LarpClient.N().h();
   final am t = new am(this);
   private final oq PZ = new oq(this);
   String w = "";
   String P0 = "";
   String Pt = "";
   boolean m;
   boolean W;
   private boolean Px;
   private boolean u;
   private String Pe = "";
   private String Pf = "";
   private Runnable Ps;
   private Runnable Pn;
   private String PC = "";
   private String O = "Confirm";
   private String PK = "";
   private String a = "Cancel";
   private boolean S;
   private final List<s8> l = new ArrayList<>();
   private final List<s8> Po = new ArrayList<>();
   private final Map<s8, List<s8>> Pg = new LinkedHashMap<>();
   private sk PX;
   private sk x;
   private sk h;
   private int H;
   private int e;
   private int Y = 62;
   private final fq Q = new fq(0.0);
   private final ps<JsonObject> Pb = new ps<>(var1x -> this.J().execute(var1x), this::D);

   fk(f8 var1) {
      super("Configs");
      this.T = var1;
   }

   private boolean D() {
      return this.J().gui.screen() == this.T && this.T.d(this);
   }

   List<Module> R() {
      return LarpClient.N().j().d();
   }

   Minecraft J() {
      return this.T.a();
   }

   void j(j0 var1) {
      this.J.add(var1);
   }

   @Override
   protected void text(String var1) {
      super.text(var1);
   }

   s8 k(String var1, Runnable var2) {
      return this.t(var1, () -> {
         if (!this.m) {
            this.w(var2);
         }
      });
   }

   @Override
   protected s8 option(String var1, String var2, Runnable var3) {
      return super.option(var1, var2, () -> {
         if (!this.m) {
            this.w(var3);
         }
      });
   }

   s8 i(String var1, Runnable var2) {
      return this.U(var1, () -> {
         if (!this.m) {
            this.w(var2);
         }
      });
   }

   void L(String var1, String var2) {
      this.z = var1;
      this.v = var2;
   }

   sk F(String var1, String var2, int var3, Consumer<String> var4) {
      sk var5 = new sk(var1, var4, var3);
      var5.j = var2;
      this.f(var5);
      return var5;
   }

   void A(String var1, String var2, boolean var3, Runnable var4) {
      s8 var5 = new s8(var1, () -> {
         if (!this.m) {
            this.P0 = "";
            this.C();
            this.w(var4);
         }
      });
      var5.N = (boolean)1;
      var5.C = (boolean)1;
      var5.z = var2;
      var5.a = var2.equals("Active");
      this.l.add(var5);
      this.J.add(var5);
   }

   void n(String var1, Runnable var2) {
      if (!this.l.isEmpty()) {
         s8 var3 = this.l.getLast();
         s8 var4 = new s8(var1, () -> {
            if (!this.m) {
               this.w(var2);
            }
         });
         var4.d = (boolean)(!var1.equals("Load") && !var1.equals("Update") ? 0 : 1);
         var4.f = var1.equals("Delete");
         var4.Z = (boolean)1;
         this.Pg.computeIfAbsent(var3, var0 -> new ArrayList<>()).add(var4);
         this.J.add(var4);
      }
   }

   private void R(String var1, Runnable var2) {
      s8 var3 = new s8(var1, () -> {
         if (!this.m) {
            this.w(var2);
         }
      });
      var3.N = (boolean)1;
      this.Po.add(var3);
      this.J.add(var3);
   }

   private void w(Runnable var1) {
      try {
         var1.run();
      } catch (IllegalArgumentException var3) {
         this.w = var3.getMessage();
         _q.R("Config", "Config action rejected: {}", this.w);
         this.build();
      } catch (RuntimeException var4) {
         this.w = "The operation could not be completed. Try again.";
         _q.o("Config", "Config action failed", var4);
         this.build();
      }
   }

   @Override
   protected void build() {
      this.D();
      this.l.clear();
      this.Po.clear();
      this.Pg.clear();
      this.x = this.h = null;
      this.A = (boolean)(!this.Px && !this.W && this.t.H == null && this.Ps == null && this.Pe.isEmpty() && !this.t() ? 0 : 1);
      this.f("My Configs", !this.u, () -> this.U("My Configs"));
      this.f("Discover", this.u, () -> this.U("Discover"));
      this.PX = new sk(this.u ? "Search this page of configs..." : "Search configs...", var1x -> {
         if (this.u) {
            this.PZ.v = var1x;
            this.PZ.u = System.currentTimeMillis() + 180L;
         } else {
            this.Pt = var1x;
            this.R();
         }
      }, 80);
      this.PX.j = this.u ? this.PZ.v : this.Pt;
      this.J.add(this.PX);
      if (!this.u) {
         this.R("Save as new config", () -> this.A("New", ""));
         this.R("Import config", () -> {
            this.W = (boolean)1;
            this.L = 0;
            this.build();
            if (this.h != null) {
               this.i(this.h);
            }
         });
         if (this.G != null && !this.G.columns()) {
            this.R("More", () -> {
               this.Px = (boolean)1;
               this.L = 0;
               this.build();
            });
         } else {
            this.R("Refresh shared", () -> this.t.V(false));
            if (this.Pp.H()) {
               this.R(this.t.E ? "Regular storage" : "Experimental storage", this::U);
            }
         }

         if (!this.P0.isEmpty()) {
            this.R("Copy share code", () -> {
               this.J().keyboardHandler.setClipboard(this.P0);
               this.w = "Share code copied to clipboard.";
            });
         }
      } else {
         this.R("Refresh", this.PZ::K);
         this.R("Previous", () -> {
            if (this.PZ.G > 0) {
               this.PZ.G = Math.max(0, this.PZ.G - 30);
               this.PZ.K();
            }
         });
         this.R("Next", () -> {
            if (this.PZ.G + 30 < this.PZ.L) {
               this.PZ.G += 30;
               this.PZ.K();
            }
         });
         this.Po.get(1).V = (boolean)(this.PZ.G > 0 ? 1 : 0);
         this.Po.get(2).V = (boolean)(this.PZ.G + 30 < this.PZ.L ? 1 : 0);
      }

      if (this.u) {
         this.PZ.x();
      } else {
         this.t.b();
      }

      if (this.Px) {
         this.L("Shared configs", "");
         this.option("Refresh shared", "Add your uploaded configs to this list", () -> {
            this.Px = (boolean)0;
            this.t.V(false);
         });
         if (this.Pp.H()) {
            this.option(
               this.t.E ? "Regular storage" : "Experimental storage",
               this.t.E ? "Show your regular shared configs" : "Show configs in experimental storage",
               () -> {
                  this.Px = (boolean)0;
                  this.U();
               }
            );
         }
      } else if (this.Ps != null) {
         int var1 = this.PC.indexOf(10);
         this.L(var1 < 0 ? this.PC : this.PC.substring(0, var1), "");
         if (var1 >= 0) {
            this.text(this.PC.substring(var1 + 1));
         }

         this.i(this.a, () -> {
            this.Ps = this.Pn = null;
            this.build();
         });
         if (this.Pn != null) {
            this.i(this.PK, () -> this.A(this.Pn));
         }

         s8 var2 = this.i(this.O, () -> this.A(this.Ps));
         var2.d = (boolean)1;
         var2.f = this.S;
      } else if (this.t.H != null) {
         this.t.w();
      } else if (this.W) {
         this.L("Import config", "Paste a share code. Nothing loads until you confirm.");
         this.h = this.t.t();
         this.i("Cancel", () -> {
            this.W = (boolean)0;
            this.build();
         });
         this.i("Fetch preview", () -> this.t.y(st.N(this.t.x))).d = (boolean)1;
      } else if (!this.Pe.isEmpty()) {
         switch (this.Pe) {
            case "New":
               this.L("Save as new config", "Saves your current settings. Nothing is loaded.");
               break;
            case "Duplicate":
               this.L("Duplicate “" + this.t.f + "”", "Name the copy.");
               break;
            default:
               this.L(this.Pe + " “" + this.t.f + "”", "");
         }

         this.x = this.F("Config name", this.Pf, 32, var1x -> this.Pf = var1x);
         this.i("Cancel", () -> {
            this.Pe = "";
            this.build();
         });
         this.i(this.Pe.equals("Rename") ? "Rename" : "Save", this::I).d = (boolean)1;
      } else if (this.u) {
         this.PZ.c();
      } else {
         this.t.H();
      }

      this.j = this.u ? "Discover" : "My Configs";

      for (j0 var6 : this.J) {
         if (!(var6 instanceof sj)) {
            var6.V = (boolean)(var6.V & (!this.m ? 1 : 0));
         }
      }

      if (this.G != null) {
         this.layout();
      }
   }

   private void U() {
      this.t.E = (boolean)(!this.t.E ? 1 : 0);
      this.t.f();
      this.t.l = List.of();
      this.t.V(false);
   }

   void n() {
      this.R();
   }

   private void R() {
      int var1 = this.PX != null && this.PX.F ? 1 : 0;
      this.H = 0;
      this.build();
      if (var1 != 0) {
         this.i(this.PX);
      }
   }

   void A(String var1, String var2) {
      this.Pe = var1;
      this.Pf = var2;
      this.L = 0;
      this.build();
      if (this.x != null) {
         this.i(this.x);
      }
   }

   private void I() {
      this.T.b();
      String var1 = _4.p(this.Pf);

      try {
         if (this.Pe.equals("Rename")) {
            this.D.K(this.t.f, var1);
         } else if (this.Pe.equals("Duplicate")) {
            this.D.a(this.t.f, var1);
         } else {
            this.D.s(var1, false);
         }

         this.t.f = var1;
         this.Pe = "";
         this.w = "Saved " + var1;
         this.build();
      } catch (IOException var3) {
         this.w = var3.getMessage();
         _q.R("Config", "Config {} failed: {}", this.Pe, var3.toString());
         this.build();
      }
   }

   void U(String var1) {
      if (!this.m) {
         this.C();
         this.u = var1.equals("Discover");
         this.t.H = null;
         this.W = (boolean)0;
         this.Px = (boolean)0;
         this.Pe = "";
         this.Ps = null;
         this.PZ.s();
         this.L = this.H = 0;
         this.P0 = "";
         this.w = "";
         _q.z("GUI", "Configs switched to {}", this.u ? "Discover" : "My Configs");
         if (this.u && !this.PZ.w) {
            this.PZ.K();
         } else {
            this.build();
         }
      }
   }

   boolean h() {
      return this.u && !this.A;
   }

   void e(String var1, String var2, Runnable var3) {
      this.O(var1, var2, var3, "", null);
   }

   void F(String var1, String var2, Runnable var3) {
      this.O(var1, var2, var3, "", null);
      this.S = (boolean)1;
      this.build();
   }

   void O(String var1, String var2, Runnable var3, String var4, Runnable var5) {
      this.PC = var1;
      this.O = var2;
      this.Ps = var3;
      this.S = (boolean)0;
      this.a = "Cancel";
      this.PK = var4;
      this.Pn = var5;
      this.L = 0;
      this.build();
   }

   void E(String var1, String var2, Runnable var3, String var4) {
      this.O(var1, var2, var3, "", null);
      this.a = var4;
      this.build();
   }

   private void A(Runnable var1) {
      this.Ps = this.Pn = null;
      this.W = (boolean)0;
      var1.run();
   }

   void H(String var1, CompletableFuture<JsonObject> var2, Consumer<JsonObject> var3) {
      if (this.m) {
         var2.cancel(true);
      } else {
         this.m = (boolean)1;
         this.w = var1;
         this.build();
         i0.W("API", i0.p.F);
         this.Pb.o(var2, (var3x, var4) -> {
            this.m = (boolean)0;
            if (var4 != null) {
               i0.W("API", i0.p.Y);
               this.w = this.Pp.Y(var4);
               _q.R("API", "Config request failed: {}", this.w);
               wr var5 = bl.i(var4);
               if (var5.e().equals("REVISION_CONFLICT") && !var1.equals("Loading cloud items...")) {
                  this.t.V(true);
                  return;
               }

               if (var5.A() == 403 && this.t.E) {
                  this.t.B();
               }

               if (var5.A() == 401 || var5.A() == 503 || var5.e().equals("NETWORK")) {
                  this.Pp.S();
               }
            } else {
               try {
                  var3.accept(var3x);
                  i0.W("API", i0.p.g);
               } catch (RuntimeException var6) {
                  this.w = "Invalid server response. Nothing was loaded locally.";
                  _q.R("API", "Invalid config response: {}", var6.toString());
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
      return this.A;
   }

   @Override
   protected int bodyTop() {
      return this.G.bodyTop() + this.Y;
   }

   private int W() {
      return this.G.right();
   }

   private boolean t() {
      return this.u ? this.PZ.S != null : this.t.U != null || this.t.f != null;
   }

   @Override
   protected void layout() {
      int var1 = this.G.contentX();
      int var2 = this.G.right() - var1;
      int var3 = Math.min(68, Math.max(1, var2 - 60));
      int var4 = Math.min(180, Math.max(1, var2 - var3));
      this.PX.bounds(var1 + var3, this.G.bodyTop(), var4);
      this.PX.O = 20;
      pf.t var5 = pf.h(var2, this.Po.stream().map(var0 -> ic.f(var0.W, ic.n.I) + 12).toList());
      this.Y = 27 + var5.height() + 8;

      for (int var6 = 0; var6 < this.Po.size(); var6++) {
         s8 var7 = this.Po.get(var6);
         pf.j var8 = var5.cells().get(var6);
         var7.Z = (boolean)1;
         var7.bounds(var1 + var8.x(), this.G.bodyTop() + 27 + var8.y(), var8.width());
         var7.O = 24;
      }

      super.layout();
      du var12 = du.calculate(var2, this.l.size());
      int var13 = Math.max(1, this.G.bottom() - this.bodyTop());
      this.e = var12.height();
      this.H = Math.clamp(this.H, 0, Math.max(0, this.e - var13));
      int var14 = (int)Math.round(this.Q.n(this.H, 150));

      for (int var9 = 0; var9 < this.l.size(); var9++) {
         s8 var10 = this.l.get(var9);
         var10.bounds(var1 + var12.x(var9), this.bodyTop() + var12.y(var9) - var14, var12.cardWidth());
         var10.O = 48;
         List var11 = this.Pg.getOrDefault(var10, List.of());
         N(var10, var11);
      }
   }

   static void N(s8 var0, List<s8> var1) {
      int var2 = var1.stream().mapToInt(var0x -> var0x.f ? 36 : 46).sum();
      int var3 = Math.max(0, var1.size() - 1) * 4;
      int var4 = var0.L - s8.s(var0.L) - 6 - var3;
      double var5 = Math.min(1.0, (double)Math.max(1, var4) / Math.max(1, var2));
      int var7 = var1.stream().mapToInt(var2x -> Math.max(1, (int)((var2x.f ? 36 : 46) * var5))).sum() + var3;
      int var8 = var0.P + var0.L - 6 - var7;

      for (s8 var10 : var1) {
         int var11 = Math.max(1, (int)((var10.f ? 36 : 46) * var5));
         var10.bounds(var8, var0.T + 31, var11);
         var10.O = 14;
         var10.R = var0.R;
         var8 += var11 + 4;
      }
   }

   @Override
   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
      ic.N(var1, ic.o("Configs", Math.max(1, this.PX.P - this.G.contentX() - 6), ic.n.Z), this.G.contentX(), this.G.bodyTop() + 3, com.larpclient.c.H, ic.n.Z);
      this.PX.render(var1, var2, var3);

      for (s8 var5 : this.Po) {
         var5.render(var1, var2, var3);
      }

      var1.enableScissor(this.G.contentX(), this.bodyTop(), this.W(), this.G.bottom());

      try {
         int var12 = var3 >= this.bodyTop() && var3 < this.G.bottom() ? var2 : -1;

         for (s8 var6 : this.l) {
            if (var6.T + var6.O > this.bodyTop() && var6.T < this.G.bottom()) {
               var6.render(var1, var12, var3);

               for (s8 var8 : this.Pg.getOrDefault(var6, List.of())) {
                  var8.render(var1, var12, var3);
               }
            }
         }
      } finally {
         var1.disableScissor();
      }

      wc.V(var1, this.W() - 3, this.bodyTop(), Math.max(1, this.G.bottom() - this.bodyTop()), this.e, (int)Math.round(this.Q.K()));
      if (this.l.isEmpty()) {
         ic.N(var1, this.m ? "Loading..." : "No configs found", this.G.contentX() + 8, this.bodyTop() + 8, com.larpclient.c.W);
      }
   }

   @Override
   protected boolean accepts(j0 var1, double var2, double var4) {
      if (this.l.contains(var1) && this.Pg.getOrDefault(var1, List.of()).stream().anyMatch(var4x -> var4x.contains(var2, var4))) {
         return false;
      } else {
         return !this.l.contains(var1) && !this.Pg.values().stream().anyMatch(var1x -> var1x.contains(var1))
            ? super.accepts(var1, var2, var4)
            : !this.A && var1.contains(var2, var4) && var4 >= this.bodyTop() && var4 < this.G.bottom() && var2 < this.W();
      }
   }

   @Override
   protected void focusChanged(j0 var1) {
      if (!this.l.contains(var1) && !this.Pg.values().stream().anyMatch(var1x -> var1x.contains(var1))) {
         super.focusChanged(var1);
      } else {
         if (var1.T < this.bodyTop()) {
            this.H = this.H - (this.bodyTop() - var1.T);
         } else if (var1.T + var1.O > this.G.bottom()) {
            this.H = this.H + (var1.T + var1.O - this.G.bottom());
         }

         this.Q.p(this.H);
         this.layout();
      }
   }

   @Override
   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (!this.c() && (this.G.columns() || !this.t()) && var1 >= this.G.contentX() && var1 < this.W() && var3 >= this.bodyTop() && var3 < this.G.bottom()) {
         this.H -= (int)(var7 * 27.0);
         this.layout();
         return true;
      } else {
         return super.mouseScrolled(var1, var3, var5, var7);
      }
   }

   @Override
   protected void dismissDialog() {
      if (this.A && !this.m) {
         if (this.t() && !this.Px && this.Ps == null && this.t.H == null && !this.W && this.Pe.isEmpty()) {
            this.t.U = null;
            this.t.f = null;
            this.PZ.s();
         } else if (this.Px) {
            this.Px = (boolean)0;
         } else if (this.Ps != null) {
            this.Ps = this.Pn = null;
         } else if (this.t.H != null) {
            this.t.H = null;
         } else {
            this.W = (boolean)0;
            this.Pe = "";
         }

         this.build();
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (j2.P(var1) == 256 && this.A && !this.m) {
         this.dismissDialog();
         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   public void tick() {
      this.PZ.M();
   }

   @Override
   void suspend() {
      this.m = (boolean)0;
      this.Pb.m();
      super.suspend();
   }
}

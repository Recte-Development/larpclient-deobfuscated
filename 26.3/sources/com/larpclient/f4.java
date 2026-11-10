package com.larpclient;

import com.larpclient.module.AutoSellModule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class f4 extends fs {
   private final Screen a;
   private final AutoSellModule K;
   private final boolean l;
   private final LinkedHashSet<String> Q;
   private final List<f4.d> r = new ArrayList<>();
   private final List<j0> B = new ArrayList<>();
   private List<f4.d> Y = List.of();
   private sk p;
   private s8 Z;
   private s8 L;
   private s8 W;
   private s8 d;
   private s8 z;
   private s8 t;
   private s8 D;
   private String N = "";
   private String s = "";
   private boolean n;
   private boolean E;
   private int b;
   private int F;
   private int M;
   private int P;
   private int x;
   private int o;
   private int U;
   private int S;
   private int T;
   private Set<String> G = Set.of();
   private Map<String, Integer> i = Map.of();
   private final fq C = new fq(0.0);

   public f4(Screen var1, AutoSellModule var2, boolean var3) {
      super(var3 ? "Never sell" : "Choose sell items");
      this.a = var1;
      this.K = var2;
      this.l = var3;
      this.Q = new LinkedHashSet<>(var2.M(var3));

      for (Item var5 : BuiltInRegistries.ITEM) {
         ItemStack var6 = new ItemStack(var5);
         if (!var6.isEmpty()) {
            String var7 = BuiltInRegistries.ITEM.getKey(var5).toString();
            String var8 = var6.getHoverName().getString();
            this.r.add(new f4.d(var7, var8, var6, (var8 + " " + var7).toLowerCase(Locale.ROOT).replace('_', ' ')));
         }
      }

      for (String var10 : this.Q) {
         if (this.r.stream().noneMatch(var1x -> var1x.id.equals(var10))) {
            this.r.add(new f4.d(var10, var10 + " (unavailable)", ItemStack.EMPTY, var10.replace('_', ' ')));
         }
      }

      this.r.sort(Comparator.comparing(f4.d::name, String.CASE_INSENSITIVE_ORDER).thenComparing(f4.d::id));
      this.h();
      this.C.n(1.0, 200);
      _q.c("AutoSell", "Opened {} item picker; draft size={}", var3 ? "excluded" : "selected", this.Q.size());
   }

   private void h() {
      HashSet var1 = new HashSet();
      if (this.minecraft.player != null) {
         for (int var2 = 0; var2 < 36; var2++) {
            ItemStack var3 = this.minecraft.player.getInventory().getItem(var2);
            if (!var3.isEmpty()) {
               var1.add(BuiltInRegistries.ITEM.getKey(var3.getItem()).toString());
            }
         }
      }

      this.G = Set.copyOf(var1);
   }

   protected void init() {
      this.W();
      this.B.clear();
      this.M = Math.max(1, Math.min(700, this.width - 24));
      this.P = Math.max(1, Math.min(500, this.height - 20));
      this.b = (this.width - this.M) / 2;
      this.F = (this.height - this.P) / 2;
      this.U = this.M >= 440 ? 2 : 1;
      this.x = this.F + 111;
      this.o = this.F + this.P - 66;
      this.S = Math.max(1, (this.o - this.x) / 34);
      this.p = new sk("Search items or namespace:item...", var1x -> {
         this.N = var1x;
         this.T = 0;
         this.Q();
      }, 96);
      this.p.j = this.N;
      this.p.bounds(this.b + 12, this.F + 40, this.M - 24);
      this.p.O = 24;
      this.J.add(this.p);
      this.Z = this.m("In inventory", () -> {
         this.h();
         this.n = (boolean)(!this.n ? 1 : 0);
         this.T = 0;
         this.Q();
      });
      this.L = this.m("Selected", () -> {
         this.E = (boolean)(!this.E ? 1 : 0);
         this.T = 0;
         this.Q();
      });
      this.W = this.m("Add held", () -> {
         if (this.minecraft.player != null && !this.minecraft.player.getMainHandItem().isEmpty()) {
            String var1x = BuiltInRegistries.ITEM.getKey(this.minecraft.player.getMainHandItem().getItem()).toString();
            this.K(() -> {
               LinkedHashSet var2x = new LinkedHashSet<>(this.Q);
               var2x.add(var1x);
               this.s(var2x);
            });
         } else {
            this.s = "Hold an item first.";
         }
      });
      this.d = this.m("Select results", () -> this.K(() -> {
         LinkedHashSet var1x = new LinkedHashSet<>(this.Q);
         this.Y.forEach(var1xx -> var1x.add(var1xx.id));
         this.s(var1x);
      }));
      this.z = this.m("Clear", () -> this.K(this.Q::clear));
      this.t = this.m("Cancel", this::onClose);
      this.D = this.m("Done", () -> {
         try {
            this.K.W(this.l, this.Q);
            this.K.n();
            _q.c("AutoSell", "Saved {} item selection; size={}", this.l ? "excluded" : "selected", this.Q.size());
            this.minecraft.gui.setScreen(this.a);
         } catch (IllegalArgumentException var2x) {
            this.s = var2x.getMessage();
         }
      });
      this.D.d = (boolean)1;
      int var1 = (this.M - 32) / 3;
      this.Z.bounds(this.b + 12, this.F + 73, var1);
      this.L.bounds(this.b + 16 + var1, this.F + 73, var1);
      this.W.bounds(this.b + 20 + var1 * 2, this.F + 73, var1);
      int var2 = this.F + this.P - 31;
      int var3 = (this.M - 36) / 4;
      this.d.bounds(this.b + 12, var2, var3);
      this.z.bounds(this.b + 16 + var3, var2, var3);
      this.t.bounds(this.b + 20 + var3 * 2, var2, var3);
      this.D.bounds(this.b + 24 + var3 * 3, var2, var3);
      this.Q();
      this.q = this.p;
      this.p.F = (boolean)1;
   }

   private s8 m(String var1, Runnable var2) {
      s8 var3 = new s8(var1, var2);
      this.J.add(var3);
      return var3;
   }

   private void s(Collection<String> var1) {
      List var2 = as.K(var1);
      this.Q.clear();
      this.Q.addAll(var2);
   }

   private void K(Runnable var1) {
      try {
         var1.run();
         this.s = "";
         this.Q();
      } catch (IllegalArgumentException var3) {
         this.s = var3.getMessage();
      }
   }

   private void Q() {
      String[] var1 = this.N.strip().toLowerCase(Locale.ROOT).replace('_', ' ').split("\\s+");
      this.Y = this.r
         .stream()
         .filter(var1x -> !this.n || this.G.contains(var1x.id))
         .filter(var1x -> !this.E || this.Q.contains(var1x.id))
         .filter(var1x -> Arrays.stream(var1).allMatch(var1x.search::contains))
         .sorted(Comparator.comparing(var1x -> !this.Q.contains(var1x.id)))
         .toList();
      this.T = Math.clamp(this.T, 0, Math.max(0, (this.Y.size() + this.U - 1) / this.U - this.S));
      this.Z.a = this.n;
      this.L.a = this.E;
      this.i();
      this.S();
   }

   private void i() {
      as.r var1 = this.K.U();
      int var2 = !var1.error().isEmpty() ? 1 : 0;
      as.r var3 = new as.r(
         var2 != 0 ? as.n.getFirst() : var1.mode(),
         this.l ? var1.selected() : this.Q,
         this.l ? this.Q : var1.excluded(),
         var2 != 0 || var1.hotbar(),
         var2 != 0 || var1.named(),
         var2 != 0 || var1.enchanted(),
         var2 != 0 || var1.filled(),
         ""
      );
      HashMap var4 = new HashMap();
      if (this.minecraft.player != null) {
         for (int var5 = 0; var5 < 36; var5++) {
            as.b var6 = AutoSellModule.C(this.minecraft.player.getInventory().getItem(var5), var5);
            if (var3.eligible(var6)) {
               var4.merge(var6.id(), 1, Integer::sum);
            }
         }
      }

      this.i = Map.copyOf(var4);
   }

   private void S() {
      if (this.q != null && this.B.contains(this.q)) {
         this.q.blur();
         this.q = null;
      }

      this.J.removeAll(this.B);
      this.B.clear();
      int var1 = (this.M - 28) / this.U;

      for (int var2 = this.T * this.U; var2 < Math.min(this.Y.size(), (this.T + this.S) * this.U); var2++) {
         f4.a var3 = new f4.a(this.Y.get(var2));
         int var4 = var2 - this.T * this.U;
         var3.bounds(this.b + 12 + var4 % this.U * var1, this.x + var4 / this.U * 34, var1 - 4);
         var3.O = 30;
         this.B.add(var3);
         this.J.add(var3);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      double var5 = this.C.n(1.0, 200);
      h5.N(var1, 0, 0, this.width, this.height, -1609560044);

      try (oa var7 = oa.j(var5)) {
         wc.t(var1, this.b, this.F, this.M, this.P, c.i, c.D, 0.6);
         ic.N(var1, this.l ? "Never sell" : "Choose sell items", this.b + 12, this.F + 12, c.H, ic.n.Z);
         String var8 = this.Q.size() + " / 32";
         ic.N(var1, var8, this.b + this.M - 12 - ic.H(var8), this.F + 16, c.W);

         for (j0 var10 : this.J) {
            if (!this.B.contains(var10)) {
               var10.render(var1, var2, var3);
            }
         }

         var1.enableScissor(this.b + 8, this.x, this.b + this.M - 8, this.o);

         try {
            for (j0 var20 : this.B) {
               var20.render(var1, var2, var3);
            }
         } finally {
            var1.disableScissor();
         }

         if (this.Y.isEmpty()) {
            ic.N(var1, "No matching items", this.b + 16, this.x + 12, c.W);
         }

         int var19 = (this.Y.size() + this.U - 1) / this.U * 34;
         wc.V(var1, this.b + this.M - 7, this.x, Math.max(1, this.o - this.x), var19, this.T * 34);
         String var21 = !this.s.isEmpty()
            ? this.s
            : this.Y.size() + " results, " + this.i.values().stream().mapToInt(Integer::intValue).sum() + " eligible inventory stacks. Escape cancels.";
         ic.N(var1, ic.o(var21, this.M - 24, ic.n.I), this.b + 12, this.F + this.P - 50, this.s.isEmpty() ? c.W : -24915, ic.n.I);
      }
   }

   @Override
   protected boolean accepts(j0 var1, double var2, double var4) {
      return super.accepts(var1, var2, var4) && (!this.B.contains(var1) || var4 >= this.x && var4 < this.o);
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      return super.mouseClicked(var1, var2);
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (!(var1 < this.b) && !(var1 >= this.b + this.M) && !(var3 < this.x) && !(var3 >= this.o)) {
         this.T = this.T - (int)Math.signum(var7) * 2;
         this.Q();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (j2.P(var1) == 256) {
         this.onClose();
         return true;
      } else if (this.q != this.p && (j2.P(var1) == 266 || j2.P(var1) == 267 || j2.P(var1) == 264 || j2.P(var1) == 265)) {
         this.T = this.T + (j2.P(var1) == 266 ? -this.S : (j2.P(var1) == 267 ? this.S : (j2.P(var1) == 264 ? 1 : -1)));
         this.Q();
         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   public void onClose() {
      _q.c("AutoSell", "Item picker draft discarded");
      this.minecraft.gui.setScreen(this.a);
   }

   private final class a extends j0 {
      private final f4.d C;
      private final fq W = new fq(0.0);

      a(f4.d var2) {
         this.C = var2;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         boolean var4 = f4.this.Q.contains(this.C.id);
         double var5 = this.W.n(!this.contains(var2, var3) && !this.F ? 0.0 : 1.0, 160);
         wc.b(var1, this.P, this.T, this.L, this.O, c.G, vv.Q(var4 ? c.J : c.R, c.g, var5 * 0.6));
         if (!this.C.icon.isEmpty()) {
            var1.item(this.C.icon, this.P + 7, this.T + 7);
         }

         ic.N(var1, ic.U(this.C.name, this.L - 54), this.P + 29, this.T + 5, c.H);
         ic.N(var1, ic.o(this.C.id, this.L - 54, ic.n.I), this.P + 29, this.T + 18, c.W, ic.n.I);
         if (var4) {
            ic.N(var1, "+", this.P + this.L - 15, this.T + 10, c.c(this.P));
         }

         if (this.F && this.X) {
            wc.U(var1, this.P, this.T, this.L, this.O, c.G);
         }

         if (this.contains(var2, var3)) {
            String var7 = this.C.id
               + (f4.this.G.contains(this.C.id) ? " | " + f4.this.i.getOrDefault(this.C.id, 0) + " eligible inventory stacks" : "")
               + (f4.this.K.M(!f4.this.l).contains(this.C.id) ? (f4.this.l ? " | Also selected for selling; exclusion wins" : " | Excluded from selling") : "");
            var1.setTooltipForNextFrame(Component.literal(var7), var2, var3);
         }
      }

      private void e() {
         boolean var1 = this.X;
         f4.this.K(() -> {
            LinkedHashSet var1x = new LinkedHashSet<>(f4.this.Q);
            if (!var1x.remove(this.C.id)) {
               var1x.add(this.C.id);
            }

            f4.this.s(var1x);
         });

         for (j0 var3 : f4.this.B) {
            if (var3 instanceof f4.a var4 && var4.C.id.equals(this.C.id)) {
               f4.this.q = var4;
               var4.F = (boolean)1;
               var4.X = var1;
               break;
            }
         }

         ft.h(ft.e.K);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            this.e();
            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (this.F && (var1 == 257 || var1 == 32)) {
            this.e();
            return true;
         } else {
            return false;
         }
      }
   }

   private record d(String id, String name, ItemStack icon, String search) {
   }
}

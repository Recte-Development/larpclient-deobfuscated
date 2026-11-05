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

public final class hi extends h8 {
   private final Screen m;
   private final AutoSellModule j;
   private final boolean i;
   private final LinkedHashSet<String> W;
   private final List<hi.e> v = new ArrayList<>();
   private final List<b> X = new ArrayList<>();
   private List<hi.e> s = List.of();
   private ur I;
   private u7 P;
   private u7 L;
   private u7 y;
   private u7 Q;
   private u7 w;
   private u7 F;
   private u7 c;
   private String e = "";
   private String n = "";
   private boolean o;
   private boolean x;
   private int E;
   private int H;
   private int J;
   private int S;
   private int z;
   private int Y;
   private int a;
   private int U;
   private int M;
   private Set<String> O = Set.of();
   private Map<String, Integer> K = Map.of();
   private final ef d = new ef(0.0);

   public hi(Screen var1, AutoSellModule var2, boolean var3) {
      super(var3 ? "Never sell" : "Choose sell items");
      this.m = var1;
      this.j = var2;
      this.i = var3;
      this.W = new LinkedHashSet<>(var2.r(var3));

      for (Item var5 : BuiltInRegistries.ITEM) {
         ItemStack var6 = new ItemStack(var5);
         if (!var6.isEmpty()) {
            String var7 = BuiltInRegistries.ITEM.getKey(var5).toString();
            String var8 = var6.getHoverName().getString();
            this.v.add(new hi.e(var7, var8, var6, (var8 + " " + var7).toLowerCase(Locale.ROOT).replace('_', ' ')));
         }
      }

      for (String var10 : this.W) {
         if (this.v.stream().noneMatch(var1x -> var1x.id.equals(var10))) {
            this.v.add(new hi.e(var10, var10 + " (unavailable)", ItemStack.EMPTY, var10.replace('_', ' ')));
         }
      }

      this.v.sort(Comparator.comparing(hi.e::name, String.CASE_INSENSITIVE_ORDER).thenComparing(hi.e::id));
      this.L();
      this.d.m(1.0, 200);
      mt.U("AutoSell", "Opened {} item picker; draft size={}", var3 ? "excluded" : "selected", this.W.size());
   }

   private void L() {
      HashSet var1 = new HashSet();
      if (this.minecraft.player != null) {
         for (int var2 = 0; var2 < 36; var2++) {
            ItemStack var3 = this.minecraft.player.getInventory().getItem(var2);
            if (!var3.isEmpty()) {
               var1.add(BuiltInRegistries.ITEM.getKey(var3.getItem()).toString());
            }
         }
      }

      this.O = Set.copyOf(var1);
   }

   protected void init() {
      this.z();
      this.X.clear();
      this.J = Math.max(1, Math.min(700, this.width - 24));
      this.S = Math.max(1, Math.min(500, this.height - 20));
      this.E = (this.width - this.J) / 2;
      this.H = (this.height - this.S) / 2;
      this.a = this.J >= 440 ? 2 : 1;
      this.z = this.H + 111;
      this.Y = this.H + this.S - 66;
      this.U = Math.max(1, (this.Y - this.z) / 34);
      this.I = new ur("Search items or namespace:item...", var1x -> {
         this.e = var1x;
         this.M = 0;
         this.n();
      }, 96);
      this.I.d = this.e;
      this.I.bounds(this.E + 12, this.H + 40, this.J - 24);
      this.I.W = 24;
      this.G.add(this.I);
      this.P = this.X("In inventory", () -> {
         this.L();
         this.o = (boolean)(!this.o ? 1 : 0);
         this.M = 0;
         this.n();
      });
      this.L = this.X("Selected", () -> {
         this.x = (boolean)(!this.x ? 1 : 0);
         this.M = 0;
         this.n();
      });
      this.y = this.X("Add held", () -> {
         if (this.minecraft.player != null && !this.minecraft.player.getMainHandItem().isEmpty()) {
            String var1x = BuiltInRegistries.ITEM.getKey(this.minecraft.player.getMainHandItem().getItem()).toString();
            this.U(() -> {
               LinkedHashSet var2x = new LinkedHashSet<>(this.W);
               var2x.add(var1x);
               this.I(var2x);
            });
         } else {
            this.n = "Hold an item first.";
         }
      });
      this.Q = this.X("Select results", () -> this.U(() -> {
         LinkedHashSet var1x = new LinkedHashSet<>(this.W);
         this.s.forEach(var1xx -> var1x.add(var1xx.id));
         this.I(var1x);
      }));
      this.w = this.X("Clear", () -> this.U(this.W::clear));
      this.F = this.X("Cancel", this::onClose);
      this.c = this.X("Done", () -> {
         try {
            this.j.P(this.i, this.W);
            this.j.A();
            mt.U("AutoSell", "Saved {} item selection; size={}", this.i ? "excluded" : "selected", this.W.size());
            this.minecraft.gui.setScreen(this.m);
         } catch (IllegalArgumentException var2x) {
            this.n = var2x.getMessage();
         }
      });
      this.c.w = (boolean)1;
      int var1 = (this.J - 32) / 3;
      this.P.bounds(this.E + 12, this.H + 73, var1);
      this.L.bounds(this.E + 16 + var1, this.H + 73, var1);
      this.y.bounds(this.E + 20 + var1 * 2, this.H + 73, var1);
      int var2 = this.H + this.S - 31;
      int var3 = (this.J - 36) / 4;
      this.Q.bounds(this.E + 12, var2, var3);
      this.w.bounds(this.E + 16 + var3, var2, var3);
      this.F.bounds(this.E + 20 + var3 * 2, var2, var3);
      this.c.bounds(this.E + 24 + var3 * 3, var2, var3);
      this.n();
      this.q = this.I;
      this.I.l = (boolean)1;
   }

   private u7 X(String var1, Runnable var2) {
      u7 var3 = new u7(var1, var2);
      this.G.add(var3);
      return var3;
   }

   private void I(Collection<String> var1) {
      List var2 = ch.U(var1);
      this.W.clear();
      this.W.addAll(var2);
   }

   private void U(Runnable var1) {
      try {
         var1.run();
         this.n = "";
         this.n();
      } catch (IllegalArgumentException var3) {
         this.n = var3.getMessage();
      }
   }

   private void n() {
      String[] var1 = this.e.strip().toLowerCase(Locale.ROOT).replace('_', ' ').split("\\s+");
      this.s = this.v
         .stream()
         .filter(var1x -> !this.o || this.O.contains(var1x.id))
         .filter(var1x -> !this.x || this.W.contains(var1x.id))
         .filter(var1x -> Arrays.stream(var1).allMatch(var1x.search::contains))
         .sorted(Comparator.comparing(var1x -> !this.W.contains(var1x.id)))
         .toList();
      this.M = Math.clamp(this.M, 0, Math.max(0, (this.s.size() + this.a - 1) / this.a - this.U));
      this.P.U = this.o;
      this.L.U = this.x;
      this.X();
      this.y();
   }

   private void X() {
      ch.z var1 = this.j.K();
      int var2 = !var1.error().isEmpty() ? 1 : 0;
      ch.z var3 = new ch.z(
         var2 != 0 ? ch.V.getFirst() : var1.mode(),
         this.i ? var1.selected() : this.W,
         this.i ? this.W : var1.excluded(),
         var2 != 0 || var1.hotbar(),
         var2 != 0 || var1.named(),
         var2 != 0 || var1.enchanted(),
         var2 != 0 || var1.filled(),
         ""
      );
      HashMap var4 = new HashMap();
      if (this.minecraft.player != null) {
         for (int var5 = 0; var5 < 36; var5++) {
            ch.t var6 = AutoSellModule.n(this.minecraft.player.getInventory().getItem(var5), var5);
            if (var3.eligible(var6)) {
               var4.merge(var6.id(), 1, Integer::sum);
            }
         }
      }

      this.K = Map.copyOf(var4);
   }

   private void y() {
      if (this.q != null && this.X.contains(this.q)) {
         this.q.blur();
         this.q = null;
      }

      this.G.removeAll(this.X);
      this.X.clear();
      int var1 = (this.J - 28) / this.a;

      for (int var2 = this.M * this.a; var2 < Math.min(this.s.size(), (this.M + this.U) * this.a); var2++) {
         hi.x var3 = new hi.x(this.s.get(var2));
         int var4 = var2 - this.M * this.a;
         var3.bounds(this.E + 12 + var4 % this.a * var1, this.z + var4 / this.a * 34, var1 - 4);
         var3.W = 30;
         this.X.add(var3);
         this.G.add(var3);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      double var5 = this.d.m(1.0, 200);
      c7.s(var1, 0, 0, this.width, this.height, -1609560044);

      try (de var7 = de.l(var5)) {
         pn.g(var1, this.E, this.H, this.J, this.S, com.larpclient.f.y, com.larpclient.f.u, 0.6);
         k6.T(var1, this.i ? "Never sell" : "Choose sell items", this.E + 12, this.H + 12, com.larpclient.f.J, k6.d.m);
         String var8 = this.W.size() + " / 32";
         k6.Z(var1, var8, this.E + this.J - 12 - k6.P(var8), this.H + 16, com.larpclient.f.s);

         for (b var10 : this.G) {
            if (!this.X.contains(var10)) {
               var10.render(var1, var2, var3);
            }
         }

         var1.enableScissor(this.E + 8, this.z, this.E + this.J - 8, this.Y);

         try {
            for (b var20 : this.X) {
               var20.render(var1, var2, var3);
            }
         } finally {
            var1.disableScissor();
         }

         if (this.s.isEmpty()) {
            k6.Z(var1, "No matching items", this.E + 16, this.z + 12, com.larpclient.f.s);
         }

         int var19 = (this.s.size() + this.a - 1) / this.a * 34;
         pn.u(var1, this.E + this.J - 7, this.z, Math.max(1, this.Y - this.z), var19, this.M * 34);
         String var21 = !this.n.isEmpty()
            ? this.n
            : this.s.size() + " results, " + this.K.values().stream().mapToInt(Integer::intValue).sum() + " eligible inventory stacks. Escape cancels.";
         k6.T(var1, k6.o(var21, this.J - 24, k6.d.K), this.E + 12, this.H + this.S - 50, this.n.isEmpty() ? com.larpclient.f.s : -24915, k6.d.K);
      }
   }

   @Override
   protected boolean accepts(b var1, double var2, double var4) {
      return super.accepts(var1, var2, var4) && (!this.X.contains(var1) || var4 >= this.z && var4 < this.Y);
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      return super.mouseClicked(var1, var2);
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (!(var1 < this.E) && !(var1 >= this.E + this.J) && !(var3 < this.z) && !(var3 >= this.Y)) {
         this.M = this.M - (int)Math.signum(var7) * 2;
         this.n();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (mo.O(var1) == 256) {
         this.onClose();
         return true;
      } else if (this.q != this.I && (mo.O(var1) == 266 || mo.O(var1) == 267 || mo.O(var1) == 264 || mo.O(var1) == 265)) {
         this.M = this.M + (mo.O(var1) == 266 ? -this.U : (mo.O(var1) == 267 ? this.U : (mo.O(var1) == 264 ? 1 : -1)));
         this.n();
         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   public void onClose() {
      mt.U("AutoSell", "Item picker draft discarded");
      this.minecraft.gui.setScreen(this.m);
   }

   private record e(String id, String name, ItemStack icon, String search) {
   }

   private final class x extends b {
      private final hi.e h;
      private final ef s = new ef(0.0);

      x(hi.e var2) {
         this.h = var2;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         boolean var4 = hi.this.W.contains(this.h.id);
         double var5 = this.s.m(!this.contains(var2, var3) && !this.l ? 0.0 : 1.0, 160);
         pn.b(var1, this.q, this.J, this.A, this.W, com.larpclient.f.aj, _3.D(var4 ? com.larpclient.f.V : com.larpclient.f.S, com.larpclient.f.x, var5 * 0.6));
         if (!this.h.icon.isEmpty()) {
            var1.item(this.h.icon, this.q + 7, this.J + 7);
         }

         k6.Z(var1, k6.j(this.h.name, this.A - 54), this.q + 29, this.J + 5, com.larpclient.f.J);
         k6.T(var1, k6.o(this.h.id, this.A - 54, k6.d.K), this.q + 29, this.J + 18, com.larpclient.f.s, k6.d.K);
         if (var4) {
            k6.Z(var1, "+", this.q + this.A - 15, this.J + 10, com.larpclient.f.z(this.q));
         }

         if (this.l && this.R) {
            pn.y(var1, this.q, this.J, this.A, this.W, com.larpclient.f.aj);
         }

         if (this.contains(var2, var3)) {
            String var7 = this.h.id
               + (hi.this.O.contains(this.h.id) ? " | " + hi.this.K.getOrDefault(this.h.id, 0) + " eligible inventory stacks" : "")
               + (hi.this.j.r(!hi.this.i).contains(this.h.id) ? (hi.this.i ? " | Also selected for selling; exclusion wins" : " | Excluded from selling") : "");
            var1.setTooltipForNextFrame(Component.literal(var7), var2, var3);
         }
      }

      private void R() {
         boolean var1 = this.R;
         hi.this.U(() -> {
            LinkedHashSet var1x = new LinkedHashSet<>(hi.this.W);
            if (!var1x.remove(this.h.id)) {
               var1x.add(this.h.id);
            }

            hi.this.I(var1x);
         });

         for (b var3 : hi.this.X) {
            if (var3 instanceof hi.x var4 && var4.h.id.equals(this.h.id)) {
               hi.this.q = var4;
               var4.l = (boolean)1;
               var4.R = var1;
               break;
            }
         }

         kb.d(kb.n.w);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            this.R();
            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (this.l && (var1 == 257 || var1 == 32)) {
            this.R();
            return true;
         } else {
            return false;
         }
      }
   }
}

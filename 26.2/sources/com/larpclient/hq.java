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

public final class hq extends h3 {
   private final Screen Z;
   private final AutoSellModule N;
   private final boolean x;
   private final LinkedHashSet<String> k;
   private final List<hq.h> J = new ArrayList<>();
   private final List<jd> q = new ArrayList<>();
   private List<hq.h> c = List.of();
   private j9 h;
   private jx W;
   private jx m;
   private jx T;
   private jx d;
   private jx Q;
   private jx b;
   private jx w;
   private String y = "";
   private String i = "";
   private boolean P;
   private boolean a;
   private int u;
   private int t;
   private int r;
   private int S;
   private int A;
   private int s;
   private int X;
   private int C;
   private int B;
   private Set<String> L = Set.of();
   private Map<String, Integer> n = Map.of();
   private final jp g = new jp(0.0);

   public hq(Screen var1, AutoSellModule var2, boolean var3) {
      super(var3 ? "Never sell" : "Choose sell items");
      this.Z = var1;
      this.N = var2;
      this.x = var3;
      this.k = new LinkedHashSet<>(var2.o(var3));

      for (Item var5 : BuiltInRegistries.ITEM) {
         ItemStack var6 = new ItemStack(var5);
         if (!var6.isEmpty()) {
            String var7 = BuiltInRegistries.ITEM.getKey(var5).toString();
            String var8 = var6.getHoverName().getString();
            this.J.add(new hq.h(var7, var8, var6, (var8 + " " + var7).toLowerCase(Locale.ROOT).replace('_', ' ')));
         }
      }

      for (String var10 : this.k) {
         if (this.J.stream().noneMatch(var1x -> var1x.id.equals(var10))) {
            this.J.add(new hq.h(var10, var10 + " (unavailable)", ItemStack.EMPTY, var10.replace('_', ' ')));
         }
      }

      this.J.sort(Comparator.comparing(hq.h::name, String.CASE_INSENSITIVE_ORDER).thenComparing(hq.h::id));
      this.P();
      this.g.k(1.0, 200);
      com.larpclient.h.U("AutoSell", "Opened {} item picker; draft size={}", var3 ? "excluded" : "selected", this.k.size());
   }

   private void P() {
      HashSet var1 = new HashSet();
      if (this.minecraft.player != null) {
         for (int var2 = 0; var2 < 36; var2++) {
            ItemStack var3 = this.minecraft.player.getInventory().getItem(var2);
            if (!var3.isEmpty()) {
               var1.add(BuiltInRegistries.ITEM.getKey(var3.getItem()).toString());
            }
         }
      }

      this.L = Set.copyOf(var1);
   }

   protected void init() {
      this.u();
      this.q.clear();
      this.r = Math.max(1, Math.min(700, this.width - 24));
      this.S = Math.max(1, Math.min(500, this.height - 20));
      this.u = (this.width - this.r) / 2;
      this.t = (this.height - this.S) / 2;
      this.X = this.r >= 440 ? 2 : 1;
      this.A = this.t + 111;
      this.s = this.t + this.S - 66;
      this.C = Math.max(1, (this.s - this.A) / 34);
      this.h = new j9("Search items or namespace:item...", var1x -> {
         this.y = var1x;
         this.B = 0;
         this.p();
      }, 96);
      this.h.H = this.y;
      this.h.bounds(this.u + 12, this.t + 40, this.r - 24);
      this.h.J = 24;
      this.p.add(this.h);
      this.W = this.q("In inventory", () -> {
         this.P();
         this.P = (boolean)(!this.P ? 1 : 0);
         this.B = 0;
         this.p();
      });
      this.m = this.q("Selected", () -> {
         this.a = (boolean)(!this.a ? 1 : 0);
         this.B = 0;
         this.p();
      });
      this.T = this.q("Add held", () -> {
         if (this.minecraft.player != null && !this.minecraft.player.getMainHandItem().isEmpty()) {
            String var1x = BuiltInRegistries.ITEM.getKey(this.minecraft.player.getMainHandItem().getItem()).toString();
            this.Q(() -> {
               LinkedHashSet var2x = new LinkedHashSet<>(this.k);
               var2x.add(var1x);
               this.z(var2x);
            });
         } else {
            this.i = "Hold an item first.";
         }
      });
      this.d = this.q("Select results", () -> this.Q(() -> {
         LinkedHashSet var1x = new LinkedHashSet<>(this.k);
         this.c.forEach(var1xx -> var1x.add(var1xx.id));
         this.z(var1x);
      }));
      this.Q = this.q("Clear", () -> this.Q(this.k::clear));
      this.b = this.q("Cancel", this::onClose);
      this.w = this.q("Done", () -> {
         try {
            this.N.b(this.x, this.k);
            this.N.e();
            com.larpclient.h.U("AutoSell", "Saved {} item selection; size={}", this.x ? "excluded" : "selected", this.k.size());
            this.minecraft.gui.setScreen(this.Z);
         } catch (IllegalArgumentException var2x) {
            this.i = var2x.getMessage();
         }
      });
      this.w.x = (boolean)1;
      int var1 = (this.r - 32) / 3;
      this.W.bounds(this.u + 12, this.t + 73, var1);
      this.m.bounds(this.u + 16 + var1, this.t + 73, var1);
      this.T.bounds(this.u + 20 + var1 * 2, this.t + 73, var1);
      int var2 = this.t + this.S - 31;
      int var3 = (this.r - 36) / 4;
      this.d.bounds(this.u + 12, var2, var3);
      this.Q.bounds(this.u + 16 + var3, var2, var3);
      this.b.bounds(this.u + 20 + var3 * 2, var2, var3);
      this.w.bounds(this.u + 24 + var3 * 3, var2, var3);
      this.p();
      this.o = this.h;
      this.h.T = (boolean)1;
   }

   private jx q(String var1, Runnable var2) {
      jx var3 = new jx(var1, var2);
      this.p.add(var3);
      return var3;
   }

   private void z(Collection<String> var1) {
      List var2 = b4.p(var1);
      this.k.clear();
      this.k.addAll(var2);
   }

   private void Q(Runnable var1) {
      try {
         var1.run();
         this.i = "";
         this.p();
      } catch (IllegalArgumentException var3) {
         this.i = var3.getMessage();
      }
   }

   private void p() {
      String[] var1 = this.y.strip().toLowerCase(Locale.ROOT).replace('_', ' ').split("\\s+");
      this.c = this.J
         .stream()
         .filter(var1x -> !this.P || this.L.contains(var1x.id))
         .filter(var1x -> !this.a || this.k.contains(var1x.id))
         .filter(var1x -> Arrays.stream(var1).allMatch(var1x.search::contains))
         .sorted(Comparator.comparing(var1x -> !this.k.contains(var1x.id)))
         .toList();
      this.B = Math.clamp(this.B, 0, Math.max(0, (this.c.size() + this.X - 1) / this.X - this.C));
      this.W.z = this.P;
      this.m.z = this.a;
      this.g();
      this.c();
   }

   private void g() {
      b4.e var1 = this.N.a();
      int var2 = !var1.error().isEmpty() ? 1 : 0;
      b4.e var3 = new b4.e(
         var2 != 0 ? b4.Y.getFirst() : var1.mode(),
         this.x ? var1.selected() : this.k,
         this.x ? this.k : var1.excluded(),
         var2 != 0 || var1.hotbar(),
         var2 != 0 || var1.named(),
         var2 != 0 || var1.enchanted(),
         var2 != 0 || var1.filled(),
         ""
      );
      HashMap var4 = new HashMap();
      if (this.minecraft.player != null) {
         for (int var5 = 0; var5 < 36; var5++) {
            b4.d var6 = AutoSellModule.H(this.minecraft.player.getInventory().getItem(var5), var5);
            if (var3.eligible(var6)) {
               var4.merge(var6.id(), 1, Integer::sum);
            }
         }
      }

      this.n = Map.copyOf(var4);
   }

   private void c() {
      if (this.o != null && this.q.contains(this.o)) {
         this.o.blur();
         this.o = null;
      }

      this.p.removeAll(this.q);
      this.q.clear();
      int var1 = (this.r - 28) / this.X;

      for (int var2 = this.B * this.X; var2 < Math.min(this.c.size(), (this.B + this.C) * this.X); var2++) {
         hq.c var3 = new hq.c(this.c.get(var2));
         int var4 = var2 - this.B * this.X;
         var3.bounds(this.u + 12 + var4 % this.X * var1, this.A + var4 / this.X * 34, var1 - 4);
         var3.J = 30;
         this.q.add(var3);
         this.p.add(var3);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      double var5 = this.g.k(1.0, 200);
      kv.d(var1, 0, 0, this.width, this.height, -1609560044);

      try (ua var7 = ua.K(var5)) {
         wp.Z(var1, this.u, this.t, this.r, this.S, ho.k, ho.A, 0.6);
         bj.k(var1, this.x ? "Never sell" : "Choose sell items", this.u + 12, this.t + 12, ho.u, bj.u.N);
         String var8 = this.k.size() + " / 32";
         bj.E(var1, var8, this.u + this.r - 12 - bj.u(var8), this.t + 16, ho.Z);

         for (jd var10 : this.p) {
            if (!this.q.contains(var10)) {
               var10.render(var1, var2, var3);
            }
         }

         var1.enableScissor(this.u + 8, this.A, this.u + this.r - 8, this.s);

         try {
            for (jd var20 : this.q) {
               var20.render(var1, var2, var3);
            }
         } finally {
            var1.disableScissor();
         }

         if (this.c.isEmpty()) {
            bj.E(var1, "No matching items", this.u + 16, this.A + 12, ho.Z);
         }

         int var19 = (this.c.size() + this.X - 1) / this.X * 34;
         wp.p(var1, this.u + this.r - 7, this.A, Math.max(1, this.s - this.A), var19, this.B * 34);
         String var21 = !this.i.isEmpty()
            ? this.i
            : this.c.size() + " results, " + this.n.values().stream().mapToInt(Integer::intValue).sum() + " eligible inventory stacks. Escape cancels.";
         bj.k(var1, bj.i(var21, this.r - 24, bj.u.M), this.u + 12, this.t + this.S - 50, this.i.isEmpty() ? ho.Z : -24915, bj.u.M);
      }
   }

   @Override
   protected boolean accepts(jd var1, double var2, double var4) {
      return super.accepts(var1, var2, var4) && (!this.q.contains(var1) || var4 >= this.A && var4 < this.s);
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      return super.mouseClicked(var1, var2);
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (!(var1 < this.u) && !(var1 >= this.u + this.r) && !(var3 < this.A) && !(var3 >= this.s)) {
         this.B = this.B - (int)Math.signum(var7) * 2;
         this.p();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (k2.a(var1) == 256) {
         this.onClose();
         return true;
      } else if (this.o != this.h && (k2.a(var1) == 266 || k2.a(var1) == 267 || k2.a(var1) == 264 || k2.a(var1) == 265)) {
         this.B = this.B + (k2.a(var1) == 266 ? -this.C : (k2.a(var1) == 267 ? this.C : (k2.a(var1) == 264 ? 1 : -1)));
         this.p();
         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   public void onClose() {
      com.larpclient.h.U("AutoSell", "Item picker draft discarded");
      this.minecraft.gui.setScreen(this.Z);
   }

   private final class c extends jd {
      private final hq.h I;
      private final jp b = new jp(0.0);

      c(hq.h var2) {
         this.I = var2;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         boolean var4 = hq.this.k.contains(this.I.id);
         double var5 = this.b.k(!this.contains(var2, var3) && !this.T ? 0.0 : 1.0, 160);
         wp.I(var1, this.S, this.u, this.A, this.J, ho.Q, uh.q(var4 ? ho.o : ho.B, ho.e, var5 * 0.6));
         if (!this.I.icon.isEmpty()) {
            var1.item(this.I.icon, this.S + 7, this.u + 7);
         }

         bj.E(var1, bj.T(this.I.name, this.A - 54), this.S + 29, this.u + 5, ho.u);
         bj.k(var1, bj.i(this.I.id, this.A - 54, bj.u.M), this.S + 29, this.u + 18, ho.Z, bj.u.M);
         if (var4) {
            bj.E(var1, "+", this.S + this.A - 15, this.u + 10, ho.Q(this.S));
         }

         if (this.T && this.W) {
            wp.t(var1, this.S, this.u, this.A, this.J, ho.Q);
         }

         if (this.contains(var2, var3)) {
            String var7 = this.I.id
               + (hq.this.L.contains(this.I.id) ? " | " + hq.this.n.getOrDefault(this.I.id, 0) + " eligible inventory stacks" : "")
               + (hq.this.N.o(!hq.this.x).contains(this.I.id) ? (hq.this.x ? " | Also selected for selling; exclusion wins" : " | Excluded from selling") : "");
            var1.setTooltipForNextFrame(Component.literal(var7), var2, var3);
         }
      }

      private void O() {
         boolean var1 = this.W;
         hq.this.Q(() -> {
            LinkedHashSet var1x = new LinkedHashSet<>(hq.this.k);
            if (!var1x.remove(this.I.id)) {
               var1x.add(this.I.id);
            }

            hq.this.z(var1x);
         });

         for (jd var3 : hq.this.q) {
            if (var3 instanceof hq.c var4 && var4.I.id.equals(this.I.id)) {
               hq.this.o = var4;
               var4.T = (boolean)1;
               var4.W = var1;
               break;
            }
         }

         sr.v(sr.e.q);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            this.O();
            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (this.T && (var1 == 257 || var1 == 32)) {
            this.O();
            return true;
         } else {
            return false;
         }
      }
   }

   private record h(String id, String name, ItemStack icon, String search) {
   }
}

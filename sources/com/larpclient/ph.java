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

public final class ph extends pw {
   private final Screen P;
   private final AutoSellModule z;
   private final boolean R;
   private final LinkedHashSet<String> U;
   private final List<ph.w> v = new ArrayList<>();
   private final List<oq> O = new ArrayList<>();
   private List<ph.w> u = List.of();
   private p_ N;
   private p7 D;
   private p7 H;
   private p7 V;
   private p7 S;
   private p7 I;
   private p7 g;
   private p7 b;
   private String m = "";
   private String n = "";
   private boolean l;
   private boolean j;
   private int f;
   private int Z;
   private int C;
   private int a;
   private int o;
   private int x;
   private int M;
   private int d;
   private int B;
   private Set<String> G = Set.of();
   private Map<String, Integer> E = Map.of();
   private final u7 J = new u7(0.0);

   public ph(Screen var1, AutoSellModule var2, boolean var3) {
      super(var3 ? "Never sell" : "Choose sell items");
      this.P = var1;
      this.z = var2;
      this.R = var3;
      this.U = new LinkedHashSet<>(var2.W(var3));

      for (Item var5 : BuiltInRegistries.ITEM) {
         ItemStack var6 = new ItemStack(var5);
         if (!var6.isEmpty()) {
            String var7 = BuiltInRegistries.ITEM.getKey(var5).toString();
            String var8 = var6.getHoverName().getString();
            this.v.add(new ph.w(var7, var8, var6, (var8 + " " + var7).toLowerCase(Locale.ROOT).replace('_', ' ')));
         }
      }

      for (String var10 : this.U) {
         if (this.v.stream().noneMatch(var1x -> var1x.id.equals(var10))) {
            this.v.add(new ph.w(var10, var10 + " (unavailable)", ItemStack.EMPTY, var10.replace('_', ' ')));
         }
      }

      this.v.sort(Comparator.comparing(ph.w::name, String.CASE_INSENSITIVE_ORDER).thenComparing(ph.w::id));
      this.Z();
      this.J.y(1.0, 200);
      r4.g("AutoSell", "Opened {} item picker; draft size={}", var3 ? "excluded" : "selected", this.U.size());
   }

   private void Z() {
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
      this.u();
      this.O.clear();
      this.C = Math.max(1, Math.min(700, this.width - 24));
      this.a = Math.max(1, Math.min(500, this.height - 20));
      this.f = (this.width - this.C) / 2;
      this.Z = (this.height - this.a) / 2;
      this.M = this.C >= 440 ? 2 : 1;
      this.o = this.Z + 111;
      this.x = this.Z + this.a - 66;
      this.d = Math.max(1, (this.x - this.o) / 34);
      this.N = new p_("Search items or namespace:item...", var1x -> {
         this.m = var1x;
         this.B = 0;
         this.n();
      }, 96);
      this.N.V = this.m;
      this.N.bounds(this.f + 12, this.Z + 40, this.C - 24);
      this.N.O = 24;
      this.k.add(this.N);
      this.D = this.C("In inventory", () -> {
         this.Z();
         this.l = (boolean)(!this.l ? 1 : 0);
         this.B = 0;
         this.n();
      });
      this.H = this.C("Selected", () -> {
         this.j = (boolean)(!this.j ? 1 : 0);
         this.B = 0;
         this.n();
      });
      this.V = this.C("Add held", () -> {
         if (this.minecraft.player != null && !this.minecraft.player.getMainHandItem().isEmpty()) {
            String var1x = BuiltInRegistries.ITEM.getKey(this.minecraft.player.getMainHandItem().getItem()).toString();
            this.Q(() -> {
               LinkedHashSet var2x = new LinkedHashSet<>(this.U);
               var2x.add(var1x);
               this.G(var2x);
            });
         } else {
            this.n = "Hold an item first.";
         }
      });
      this.S = this.C("Select results", () -> this.Q(() -> {
         LinkedHashSet var1x = new LinkedHashSet<>(this.U);
         this.u.forEach(var1xx -> var1x.add(var1xx.id));
         this.G(var1x);
      }));
      this.I = this.C("Clear", () -> this.Q(this.U::clear));
      this.g = this.C("Cancel", this::onClose);
      this.b = this.C("Done", () -> {
         try {
            this.z.U(this.R, this.U);
            this.z.V();
            r4.g("AutoSell", "Saved {} item selection; size={}", this.R ? "excluded" : "selected", this.U.size());
            this.minecraft.gui.setScreen(this.P);
         } catch (IllegalArgumentException var2x) {
            this.n = var2x.getMessage();
         }
      });
      this.b.d = (boolean)1;
      int var1 = (this.C - 32) / 3;
      this.D.bounds(this.f + 12, this.Z + 73, var1);
      this.H.bounds(this.f + 16 + var1, this.Z + 73, var1);
      this.V.bounds(this.f + 20 + var1 * 2, this.Z + 73, var1);
      int var2 = this.Z + this.a - 31;
      int var3 = (this.C - 36) / 4;
      this.S.bounds(this.f + 12, var2, var3);
      this.I.bounds(this.f + 16 + var3, var2, var3);
      this.g.bounds(this.f + 20 + var3 * 2, var2, var3);
      this.b.bounds(this.f + 24 + var3 * 3, var2, var3);
      this.n();
      this.W = this.N;
      this.N.A = (boolean)1;
   }

   private p7 C(String var1, Runnable var2) {
      p7 var3 = new p7(var1, var2);
      this.k.add(var3);
      return var3;
   }

   private void G(Collection<String> var1) {
      List var2 = ej.V(var1);
      this.U.clear();
      this.U.addAll(var2);
   }

   private void Q(Runnable var1) {
      try {
         var1.run();
         this.n = "";
         this.n();
      } catch (IllegalArgumentException var3) {
         this.n = var3.getMessage();
      }
   }

   private void n() {
      String[] var1 = this.m.strip().toLowerCase(Locale.ROOT).replace('_', ' ').split("\\s+");
      this.u = this.v
         .stream()
         .filter(var1x -> !this.l || this.G.contains(var1x.id))
         .filter(var1x -> !this.j || this.U.contains(var1x.id))
         .filter(var1x -> Arrays.stream(var1).allMatch(var1x.search::contains))
         .sorted(Comparator.comparing(var1x -> !this.U.contains(var1x.id)))
         .toList();
      this.B = Math.clamp(this.B, 0, Math.max(0, (this.u.size() + this.M - 1) / this.M - this.d));
      this.D.R = this.l;
      this.H.R = this.j;
      this.G();
      this.m();
   }

   private void G() {
      ej.q var1 = this.z.l();
      int var2 = !var1.error().isEmpty() ? 1 : 0;
      ej.q var3 = new ej.q(
         var2 != 0 ? ej.R.getFirst() : var1.mode(),
         this.R ? var1.selected() : this.U,
         this.R ? this.U : var1.excluded(),
         var2 != 0 || var1.hotbar(),
         var2 != 0 || var1.named(),
         var2 != 0 || var1.enchanted(),
         var2 != 0 || var1.filled(),
         ""
      );
      HashMap var4 = new HashMap();
      if (this.minecraft.player != null) {
         for (int var5 = 0; var5 < 36; var5++) {
            ej.v var6 = AutoSellModule.r(this.minecraft.player.getInventory().getItem(var5), var5);
            if (var3.eligible(var6)) {
               var4.merge(var6.id(), 1, Integer::sum);
            }
         }
      }

      this.E = Map.copyOf(var4);
   }

   private void m() {
      if (this.W != null && this.O.contains(this.W)) {
         this.W.blur();
         this.W = null;
      }

      this.k.removeAll(this.O);
      this.O.clear();
      int var1 = (this.C - 28) / this.M;

      for (int var2 = this.B * this.M; var2 < Math.min(this.u.size(), (this.B + this.d) * this.M); var2++) {
         ph.e var3 = new ph.e(this.u.get(var2));
         int var4 = var2 - this.B * this.M;
         var3.bounds(this.f + 12 + var4 % this.M * var1, this.o + var4 / this.M * 34, var1 - 4);
         var3.O = 30;
         this.O.add(var3);
         this.k.add(var3);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      double var5 = this.J.y(1.0, 200);
      fr.k(var1, 0, 0, this.width, this.height, -1609560044);

      try (r var7 = r.p(var5)) {
         ll.Y(var1, this.f, this.Z, this.C, this.a, rv.Gj, rv.w, 0.6);
         j_.L(var1, this.R ? "Never sell" : "Choose sell items", this.f + 12, this.Z + 12, rv.P, j_.m.a);
         String var8 = this.U.size() + " / 32";
         j_.E(var1, var8, this.f + this.C - 12 - j_.a(var8), this.Z + 16, rv.G0);

         for (oq var10 : this.k) {
            if (!this.O.contains(var10)) {
               var10.render(var1, var2, var3);
            }
         }

         var1.enableScissor(this.f + 8, this.o, this.f + this.C - 8, this.x);

         try {
            for (oq var20 : this.O) {
               var20.render(var1, var2, var3);
            }
         } finally {
            var1.disableScissor();
         }

         if (this.u.isEmpty()) {
            j_.E(var1, "No matching items", this.f + 16, this.o + 12, rv.G0);
         }

         int var19 = (this.u.size() + this.M - 1) / this.M * 34;
         ll.g(var1, this.f + this.C - 7, this.o, Math.max(1, this.x - this.o), var19, this.B * 34);
         String var21 = !this.n.isEmpty()
            ? this.n
            : this.u.size() + " results, " + this.E.values().stream().mapToInt(Integer::intValue).sum() + " eligible inventory stacks. Escape cancels.";
         j_.L(var1, j_.d(var21, this.C - 24, j_.m.U), this.f + 12, this.Z + this.a - 50, this.n.isEmpty() ? rv.G0 : -24915, j_.m.U);
      }
   }

   @Override
   protected boolean accepts(oq var1, double var2, double var4) {
      return super.accepts(var1, var2, var4) && (!this.O.contains(var1) || var4 >= this.o && var4 < this.x);
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      return super.mouseClicked(var1, var2);
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (!(var1 < this.f) && !(var1 >= this.f + this.C) && !(var3 < this.o) && !(var3 >= this.x)) {
         this.B = this.B - (int)Math.signum(var7) * 2;
         this.n();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (r7.g(var1) == 256) {
         this.onClose();
         return true;
      } else if (this.W != this.N && (r7.g(var1) == 266 || r7.g(var1) == 267 || r7.g(var1) == 264 || r7.g(var1) == 265)) {
         this.B = this.B + (r7.g(var1) == 266 ? -this.d : (r7.g(var1) == 267 ? this.d : (r7.g(var1) == 264 ? 1 : -1)));
         this.n();
         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   public void onClose() {
      r4.g("AutoSell", "Item picker draft discarded");
      this.minecraft.gui.setScreen(this.P);
   }

   private final class e extends oq {
      private final ph.w l;
      private final u7 t = new u7(0.0);

      e(ph.w var2) {
         this.l = var2;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         boolean var4 = ph.this.U.contains(this.l.id);
         double var5 = this.t.y(!this.contains(var2, var3) && !this.A ? 0.0 : 1.0, 160);
         ll.N(var1, this.B, this.L, this.v, this.O, rv.g, oh.j(var4 ? rv.K : rv.z, rv.Z, var5 * 0.6));
         if (!this.l.icon.isEmpty()) {
            var1.item(this.l.icon, this.B + 7, this.L + 7);
         }

         j_.E(var1, j_.S(this.l.name, this.v - 54), this.B + 29, this.L + 5, rv.P);
         j_.L(var1, j_.d(this.l.id, this.v - 54, j_.m.U), this.B + 29, this.L + 18, rv.G0, j_.m.U);
         if (var4) {
            j_.E(var1, "+", this.B + this.v - 15, this.L + 10, rv.m(this.B));
         }

         if (this.A && this.h) {
            ll.w(var1, this.B, this.L, this.v, this.O, rv.g);
         }

         if (this.contains(var2, var3)) {
            String var7 = this.l.id
               + (ph.this.G.contains(this.l.id) ? " | " + ph.this.E.getOrDefault(this.l.id, 0) + " eligible inventory stacks" : "")
               + (ph.this.z.W(!ph.this.R).contains(this.l.id) ? (ph.this.R ? " | Also selected for selling; exclusion wins" : " | Excluded from selling") : "");
            var1.setTooltipForNextFrame(Component.literal(var7), var2, var3);
         }
      }

      private void C() {
         boolean var1 = this.h;
         ph.this.Q(() -> {
            LinkedHashSet var1x = new LinkedHashSet<>(ph.this.U);
            if (!var1x.remove(this.l.id)) {
               var1x.add(this.l.id);
            }

            ph.this.G(var1x);
         });

         for (oq var3 : ph.this.O) {
            if (var3 instanceof ph.e var4 && var4.l.id.equals(this.l.id)) {
               ph.this.W = var4;
               var4.A = (boolean)1;
               var4.h = var1;
               break;
            }
         }

         hx.w(hx.t.X);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            this.C();
            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (this.A && (var1 == 257 || var1 == 32)) {
            this.C();
            return true;
         } else {
            return false;
         }
      }
   }

   private record w(String id, String name, ItemStack icon, String search) {
   }
}

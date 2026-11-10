package com.larpclient;

import com.larpclient.module.Module;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public final class j4 {
   public static final int P = 64;
   private static long N = -1L;
   private final BooleanSupplier n;
   private final Predicate<com.larpclient.k.l> o;
   private final List<Module> y;
   private final cp B;
   private final ArrayDeque<j4.g> C = new ArrayDeque<>();
   private final ArrayDeque<j4.g> b = new ArrayDeque<>();
   private j4.g O;
   private long L = hz.o;
   private long G;
   private boolean j;
   private boolean w;
   private String r = "Saved";

   public j4(BooleanSupplier var1, Predicate<com.larpclient.k.l> var2) {
      this(var1, var2, List.of());
   }

   public j4(BooleanSupplier var1, Predicate<com.larpclient.k.l> var2, List<Module> var3) {
      this.y = List.copyOf(var3);
      this.n = var1;
      this.o = var2;
      this.j = (boolean)(N == hz.o ? 1 : 0);
      if (this.j) {
         this.r = "Save failed. Retry";
      }

      this.B = new cp(this::i, var0 -> {}, () -> {});
      this.B.b(var2);
   }

   public cp j() {
      return this.B;
   }

   public com.larpclient.k.l t() {
      return this.B.r();
   }

   public boolean l() {
      return !this.C.isEmpty();
   }

   public boolean y() {
      return !this.b.isEmpty();
   }

   public int E() {
      return this.C.size();
   }

   public boolean k() {
      return this.j;
   }

   public String Z() {
      return this.r;
   }

   public void V(com.larpclient.k.l var1) {
      this.B.H(var1);
      this.i();
   }

   public void r() {
      this.Z();
      if (this.O == null) {
         this.O = j4.g.capture(this.y);
      }
   }

   public void s() {
      this.w = (boolean)1;
   }

   public void b(Runnable var1) {
      this.r();
      var1.run();
      this.i();
   }

   public boolean i() {
      if (this.L != hz.o) {
         this.Z();
         return true;
      }

      byte var1 = this.w;
      if (this.O != null) {
         if (!this.O.equals(j4.g.capture(this.y))) {
            i(this.C, this.O);
            this.b.clear();
            var1 = 1;
         }

         this.O = null;
      }

      this.j |= var1;
      this.w = (boolean)0;
      return var1 != 0 ? this.f() : !this.j;
   }

   public boolean f() {
      if (!this.j) {
         return true;
      } else {
         this.G++;
         if (this.n.getAsBoolean()) {
            this.j = (boolean)0;
            N = -1L;
            this.r = "Saved";
            q8.v("HUD editor", "Saved completed workspace edit");
            return true;
         } else {
            N = this.L;
            this.r = "Save failed. Retry";
            q8.L("HUD editor", "Workspace save failed; pending edits retained for retry");
            return false;
         }
      }
   }

   public boolean g() {
      return this.X(this.C, this.b);
   }

   public boolean n() {
      return this.X(this.b, this.C);
   }

   private boolean X(ArrayDeque<j4.g> var1, ArrayDeque<j4.g> var2) {
      this.B.u(0);
      this.i();
      if (var1.isEmpty()) {
         return false;
      }

      i(var2, j4.g.capture(this.y));
      ((j4.g)var1.removeLast()).restore();
      this.j = (boolean)1;
      this.f();
      return true;
   }

   private static void i(ArrayDeque<j4.g> var0, j4.g var1) {
      if (var0.size() == 64) {
         var0.removeFirst();
      }

      var0.addLast(var1);
   }

   public void A(int var1, int var2, int var3, int var4) {
      com.larpclient.k.l var5 = this.t();
      if (var5 != null && !this.o.test(var5)) {
         this.b(() -> var5.e(cv.o(var5.l(var3) + var1, var5.N(), var3), cv.o(var5.M(var4) + var2, var5.E(), var4), var3, var4));
      }
   }

   public void H() {
      this.b(() -> com.larpclient.k.z(var1 -> !this.o.test(var1)));
   }

   public void M(boolean var1, boolean var2) {
      hz.j var3 = hz.l;
      if (var3.snapping() != var1 || var3.grid() != var2) {
         hz.l = new hz.j(var3.panel(), var3.collapsed(), var1, var2);
         this.j = (boolean)1;
         this.f();
      }
   }

   public boolean Z() {
      if (this.L == hz.o) {
         return false;
      }

      this.B.P(true, true);
      this.C.clear();
      this.b.clear();
      this.O = null;
      this.j = this.w = (boolean)0;
      this.r = "Saved";
      this.L = hz.o;
      return true;
   }

   public void v() {
      this.B.u(0);
      this.i();
   }

   public void C() {
      long var1 = this.G;
      this.B.u();
      this.i();
      if (this.G == var1) {
         this.f();
      }
   }

   public record g(Map<String, hz.v> positions, Map<String, hz.s> styles, Map<com.larpclient.module.lb<?>, Object> settings, Map<Module, Boolean> owners) {
      public g(Map<String, hz.v> positions, Map<String, hz.s> styles, Map<com.larpclient.module.lb<?>, Object> settings, Map<Module, Boolean> owners) {
         positions = Map.copyOf(positions);
         styles = Map.copyOf(styles);
         settings = Map.copyOf(settings);
         owners = Map.copyOf(owners);
         this.positions = positions;
         this.styles = styles;
         this.settings = settings;
         this.owners = owners;
      }

      public static j4.g capture() {
         return capture(List.of());
      }

      public static j4.g capture(List<Module> var0) {
         LinkedHashMap var1 = new LinkedHashMap();
         LinkedHashMap var2 = new LinkedHashMap();

         for (Module var4 : var0) {
            var2.put(var4, var4.isEnabled());

            for (com.larpclient.module.lb var6 : var4.getSettings()) {
               var1.put(var6, var6.getValue());
            }
         }

         return new j4.g(hz.J, hz.S, var1, var2);
      }

      private static void restoreSetting(com.larpclient.module.lb<?> var0, Object var1) {
         if (!Objects.equals(var0.getValue(), var1)) {
            var0.setValue(var1);
         }
      }

      public void restore() {
         hz.J.clear();
         hz.J.putAll(this.positions);
         hz.S.clear();
         hz.S.putAll(this.styles);
         this.settings.forEach(j4.g::restoreSetting);
         this.owners.forEach((var0, var1) -> {
            if (var0.isEnabled() != var1) {
               var0.setEnabled(var1);
            }
         });
      }
   }

   public enum k {
      G,
      d,
      Q,
      A,
      X;
   }

   public record w(cv.z dock, cv.z panel, cv.z body, cv.z retry, List<cv.z> tools, int width, int height) {
      public w(cv.z dock, cv.z panel, cv.z body, cv.z retry, List<cv.z> tools, int width, int height) {
         tools = List.copyOf(tools);
         this.dock = dock;
         this.panel = panel;
         this.body = body;
         this.retry = retry;
         this.tools = tools;
         this.width = width;
         this.height = height;
      }

      public static j4.w calculate(int var0, int var1, j4.k var2, int var3, int var4, int var5, boolean var6, boolean var7) {
         if (!var6) {
            return new j4.w(null, null, null, null, List.of(), var0, var1);
         }

         int var8 = Math.min(12, Math.max(0, var0 / 4));
         int var9 = Math.max(1, Math.min(340, var0 - var8 * 2));
         cv.z var10 = new cv.z((var0 - var9) / 2, Math.max(0, var1 - var8 - 42), var9, Math.min(42, var1));
         ArrayList var11 = new ArrayList();
         int var12 = Math.max(2, var9 - 172);
         int[] var13 = new int[]{var12 / 2 + 4, 28, 28, 28, var12 - var12 / 2 - 4, 56};
         double var14 = Math.min(1.0, (double)Math.max(1, var9 - 32) / Arrays.stream(var13).sum());
         int var16 = var10.x() + 6;

         for (int var20 : var13) {
            int var21 = Math.max(1, (int)Math.floor(var20 * var14));
            var11.add(new cv.z(var16, var10.y() + 6, var21, Math.min(30, var10.height())));
            var16 += var21 + 4;
         }

         cv.z var26 = var7 ? new cv.z((var0 - Math.min(208, var9)) / 2, Math.max(0, var10.y() - 29), Math.min(208, var9), 22) : null;
         if (var2 == j4.k.G) {
            return new j4.w(var10, null, null, var26, var11, var0, var1);
         }

         int var27 = Math.max(1, Math.min(var2 == j4.k.d ? 268 : 260, var0 - var8 * 2));
         int var28 = header(var2);
         int var29 = Math.max(1, var10.y() - (var7 ? 40 : 12) - var8);
         int var30 = Math.min(var28 + Math.min(Math.max(0, var3), 280) + 10, var29);
         int var22 = Math.clamp(var4 < 0 ? var10.x() : var4, var8, Math.max(var8, var0 - var8 - var27));
         int var23 = Math.clamp(var5 < 0 ? var10.y() - var30 - 12 : var5, var8, Math.max(var8, var10.y() - (var7 ? 40 : 12) - var30));
         cv.z var24 = new cv.z(var22, var23, var27, var30);
         cv.z var25 = new cv.z(var22 + 10, var23 + Math.min(var28, var30), Math.max(1, var27 - 20), Math.max(0, var30 - var28 - 10));
         return new j4.w(var10, var24, var25, var26, var11, var0, var1);
      }

      public static int header(j4.k var0) {
         return var0 == j4.k.d ? 78 : (var0 != j4.k.Q && var0 != j4.k.A ? 45 : 76);
      }

      public boolean panelContains(double var1, double var3) {
         return this.panel != null && this.panel.contains(var1, var3, 0);
      }

      public boolean canvasContains(double var1, double var3) {
         return var1 >= 0.0
            && var1 < this.width
            && var3 >= 0.0
            && var3 < this.height
            && !this.panelContains(var1, var3)
            && (this.dock == null || !this.dock.contains(var1, var3, 0))
            && (this.retry == null || !this.retry.contains(var1, var3, 0));
      }

      public boolean controlContains(cv.z var1, double var2, double var4) {
         return this.body != null && this.body.contains(var2, var4, 0) && var1.contains(var2, var4, 0);
      }
   }
}

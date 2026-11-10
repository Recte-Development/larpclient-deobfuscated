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

public final class p0 {
   public static final int o = 64;
   private static long U = -1L;
   private final BooleanSupplier S;
   private final Predicate<dk.q> y;
   private final List<Module> d;
   private final db l;
   private final ArrayDeque<p0.t> b = new ArrayDeque<>();
   private final ArrayDeque<p0.t> a = new ArrayDeque<>();
   private p0.t K;
   private long n = bh.k;
   private long f;
   private boolean C;
   private boolean J;
   private String M = "Saved";

   public p0(BooleanSupplier var1, Predicate<dk.q> var2) {
      this(var1, var2, List.of());
   }

   public p0(BooleanSupplier var1, Predicate<dk.q> var2, List<Module> var3) {
      this.d = List.copyOf(var3);
      this.S = var1;
      this.y = var2;
      this.C = (boolean)(U == bh.k ? 1 : 0);
      if (this.C) {
         this.M = "Save failed. Retry";
      }

      this.l = new db(this::a, var0 -> {}, () -> {});
      this.l.B(var2);
   }

   public db a() {
      return this.l;
   }

   public dk.q b() {
      return this.l.W();
   }

   public boolean U() {
      return !this.b.isEmpty();
   }

   public boolean V() {
      return !this.a.isEmpty();
   }

   public int w() {
      return this.b.size();
   }

   public boolean H() {
      return this.C;
   }

   public String X() {
      return this.M;
   }

   public void O(dk.q var1) {
      this.l.H(var1);
      this.a();
   }

   public void z() {
      this.j();
      if (this.K == null) {
         this.K = p0.t.capture(this.d);
      }
   }

   public void p() {
      this.J = (boolean)1;
   }

   public void T(Runnable var1) {
      this.z();
      var1.run();
      this.a();
   }

   public boolean a() {
      if (this.n != bh.k) {
         this.j();
         return true;
      }

      byte var1 = this.J;
      if (this.K != null) {
         if (!this.K.equals(p0.t.capture(this.d))) {
            a(this.b, this.K);
            this.a.clear();
            var1 = 1;
         }

         this.K = null;
      }

      this.C |= var1;
      this.J = (boolean)0;
      return var1 != 0 ? this.L() : !this.C;
   }

   public boolean L() {
      if (!this.C) {
         return true;
      } else {
         this.f++;
         if (this.S.getAsBoolean()) {
            this.C = (boolean)0;
            U = -1L;
            this.M = "Saved";
            _q.c("HUD editor", "Saved completed workspace edit");
            return true;
         } else {
            U = this.n;
            this.M = "Save failed. Retry";
            _q.R("HUD editor", "Workspace save failed; pending edits retained for retry");
            return false;
         }
      }
   }

   public boolean e() {
      return this.x(this.b, this.a);
   }

   public boolean u() {
      return this.x(this.a, this.b);
   }

   private boolean x(ArrayDeque<p0.t> var1, ArrayDeque<p0.t> var2) {
      this.l.L(0);
      this.a();
      if (var1.isEmpty()) {
         return false;
      }

      a(var2, p0.t.capture(this.d));
      ((p0.t)var1.removeLast()).restore();
      this.C = (boolean)1;
      this.L();
      return true;
   }

   private static void a(ArrayDeque<p0.t> var0, p0.t var1) {
      if (var0.size() == 64) {
         var0.removeFirst();
      }

      var0.addLast(var1);
   }

   public void v(int var1, int var2, int var3, int var4) {
      dk.q var5 = this.b();
      if (var5 != null && !this.y.test(var5)) {
         this.T(() -> var5.o(qy.k(var5.f(var3) + var1, var5.v(), var3), qy.k(var5.p(var4) + var2, var5.u(), var4), var3, var4));
      }
   }

   public void T() {
      this.T(() -> dk.N(var1 -> !this.y.test(var1)));
   }

   public void w(boolean var1, boolean var2) {
      bh.f var3 = bh.z;
      if (var3.snapping() != var1 || var3.grid() != var2) {
         bh.z = new bh.f(var3.panel(), var3.collapsed(), var1, var2);
         this.C = (boolean)1;
         this.L();
      }
   }

   public boolean j() {
      if (this.n == bh.k) {
         return false;
      }

      this.l.h(true, true);
      this.b.clear();
      this.a.clear();
      this.K = null;
      this.C = this.J = (boolean)0;
      this.M = "Saved";
      this.n = bh.k;
      return true;
   }

   public void d() {
      this.l.L(0);
      this.a();
   }

   public void M() {
      long var1 = this.f;
      this.l.I();
      this.a();
      if (this.f == var1) {
         this.L();
      }
   }

   public enum d {
      b,
      t,
      Y,
      y,
      V;
   }

   public record l(qy.i dock, qy.i panel, qy.i body, qy.i retry, List<qy.i> tools, int width, int height) {
      public l(qy.i dock, qy.i panel, qy.i body, qy.i retry, List<qy.i> tools, int width, int height) {
         tools = List.copyOf(tools);
         this.dock = dock;
         this.panel = panel;
         this.body = body;
         this.retry = retry;
         this.tools = tools;
         this.width = width;
         this.height = height;
      }

      public static p0.l calculate(int var0, int var1, p0.d var2, int var3, int var4, int var5, boolean var6, boolean var7) {
         if (!var6) {
            return new p0.l(null, null, null, null, List.of(), var0, var1);
         }

         int var8 = Math.min(12, Math.max(0, var0 / 4));
         int var9 = Math.max(1, Math.min(340, var0 - var8 * 2));
         qy.i var10 = new qy.i((var0 - var9) / 2, Math.max(0, var1 - var8 - 42), var9, Math.min(42, var1));
         ArrayList var11 = new ArrayList();
         int var12 = Math.max(2, var9 - 172);
         int[] var13 = new int[]{var12 / 2 + 4, 28, 28, 28, var12 - var12 / 2 - 4, 56};
         double var14 = Math.min(1.0, (double)Math.max(1, var9 - 32) / Arrays.stream(var13).sum());
         int var16 = var10.x() + 6;

         for (int var20 : var13) {
            int var21 = Math.max(1, (int)Math.floor(var20 * var14));
            var11.add(new qy.i(var16, var10.y() + 6, var21, Math.min(30, var10.height())));
            var16 += var21 + 4;
         }

         qy.i var26 = var7 ? new qy.i((var0 - Math.min(208, var9)) / 2, Math.max(0, var10.y() - 29), Math.min(208, var9), 22) : null;
         if (var2 == p0.d.b) {
            return new p0.l(var10, null, null, var26, var11, var0, var1);
         }

         int var27 = Math.max(1, Math.min(var2 == p0.d.t ? 268 : 260, var0 - var8 * 2));
         int var28 = header(var2);
         int var29 = Math.max(1, var10.y() - (var7 ? 40 : 12) - var8);
         int var30 = Math.min(var28 + Math.min(Math.max(0, var3), 280) + 10, var29);
         int var22 = Math.clamp(var4 < 0 ? var10.x() : var4, var8, Math.max(var8, var0 - var8 - var27));
         int var23 = Math.clamp(var5 < 0 ? var10.y() - var30 - 12 : var5, var8, Math.max(var8, var10.y() - (var7 ? 40 : 12) - var30));
         qy.i var24 = new qy.i(var22, var23, var27, var30);
         qy.i var25 = new qy.i(var22 + 10, var23 + Math.min(var28, var30), Math.max(1, var27 - 20), Math.max(0, var30 - var28 - 10));
         return new p0.l(var10, var24, var25, var26, var11, var0, var1);
      }

      public static int header(p0.d var0) {
         return var0 == p0.d.t ? 78 : (var0 != p0.d.Y && var0 != p0.d.y ? 45 : 76);
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

      public boolean controlContains(qy.i var1, double var2, double var4) {
         return this.body != null && this.body.contains(var2, var4, 0) && var1.contains(var2, var4, 0);
      }
   }

   public record t(Map<String, bh.u> positions, Map<String, bh.b> styles, Map<com.larpclient.module.r<?>, Object> settings, Map<Module, Boolean> owners) {
      public t(Map<String, bh.u> positions, Map<String, bh.b> styles, Map<com.larpclient.module.r<?>, Object> settings, Map<Module, Boolean> owners) {
         positions = Map.copyOf(positions);
         styles = Map.copyOf(styles);
         settings = Map.copyOf(settings);
         owners = Map.copyOf(owners);
         this.positions = positions;
         this.styles = styles;
         this.settings = settings;
         this.owners = owners;
      }

      public static p0.t capture() {
         return capture(List.of());
      }

      public static p0.t capture(List<Module> var0) {
         LinkedHashMap var1 = new LinkedHashMap();
         LinkedHashMap var2 = new LinkedHashMap();

         for (Module var4 : var0) {
            var2.put(var4, var4.isEnabled());

            for (com.larpclient.module.r var6 : var4.getSettings()) {
               var1.put(var6, var6.getValue());
            }
         }

         return new p0.t(bh.Z, bh.F, var1, var2);
      }

      private static void restoreSetting(com.larpclient.module.r<?> var0, Object var1) {
         if (!Objects.equals(var0.getValue(), var1)) {
            var0.setValue(var1);
         }
      }

      public void restore() {
         bh.Z.clear();
         bh.Z.putAll(this.positions);
         bh.F.clear();
         bh.F.putAll(this.styles);
         this.settings.forEach(p0.t::restoreSetting);
         this.owners.forEach((var0, var1) -> {
            if (var0.isEnabled() != var1) {
               var0.setEnabled(var1);
            }
         });
      }
   }
}

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

public final class pc {
   public static final int D = 64;
   private static long E = -1L;
   private final BooleanSupplier z;
   private final Predicate<m4.p> e;
   private final List<Module> u;
   private final s4 d;
   private final ArrayDeque<pc.r> Q = new ArrayDeque<>();
   private final ArrayDeque<pc.r> N = new ArrayDeque<>();
   private pc.r p;
   private long s = c7.w;
   private long b;
   private boolean L;
   private boolean F;
   private String U = "Saved";

   public pc(BooleanSupplier var1, Predicate<m4.p> var2) {
      this(var1, var2, List.of());
   }

   public pc(BooleanSupplier var1, Predicate<m4.p> var2, List<Module> var3) {
      this.u = List.copyOf(var3);
      this.z = var1;
      this.e = var2;
      this.L = (boolean)(E == c7.w ? 1 : 0);
      if (this.L) {
         this.U = "Save failed. Retry";
      }

      this.d = new s4(this::o, var0 -> {}, () -> {});
      this.d.A(var2);
   }

   public s4 y() {
      return this.d;
   }

   public m4.p z() {
      return this.d.R();
   }

   public boolean q() {
      return !this.Q.isEmpty();
   }

   public boolean e() {
      return !this.N.isEmpty();
   }

   public int P() {
      return this.Q.size();
   }

   public boolean S() {
      return this.L;
   }

   public String B() {
      return this.U;
   }

   public void x(m4.p var1) {
      this.d.H(var1);
      this.o();
   }

   public void j() {
      this.E();
      if (this.p == null) {
         this.p = pc.r.capture(this.u);
      }
   }

   public void H() {
      this.F = (boolean)1;
   }

   public void l(Runnable var1) {
      this.j();
      var1.run();
      this.o();
   }

   public boolean o() {
      if (this.s != c7.w) {
         this.E();
         return true;
      }

      byte var1 = this.F;
      if (this.p != null) {
         if (!this.p.equals(pc.r.capture(this.u))) {
            y(this.Q, this.p);
            this.N.clear();
            var1 = 1;
         }

         this.p = null;
      }

      this.L |= var1;
      this.F = (boolean)0;
      return var1 != 0 ? this.Z() : !this.L;
   }

   public boolean Z() {
      if (!this.L) {
         return true;
      } else {
         this.b++;
         if (this.z.getAsBoolean()) {
            this.L = (boolean)0;
            E = -1L;
            this.U = "Saved";
            h.U("HUD editor", "Saved completed workspace edit");
            return true;
         } else {
            E = this.s;
            this.U = "Save failed. Retry";
            h.g("HUD editor", "Workspace save failed; pending edits retained for retry");
            return false;
         }
      }
   }

   public boolean y() {
      return this.F(this.Q, this.N);
   }

   public boolean a() {
      return this.F(this.N, this.Q);
   }

   private boolean F(ArrayDeque<pc.r> var1, ArrayDeque<pc.r> var2) {
      this.d.O(0);
      this.o();
      if (var1.isEmpty()) {
         return false;
      }

      y(var2, pc.r.capture(this.u));
      ((pc.r)var1.removeLast()).restore();
      this.L = (boolean)1;
      this.Z();
      return true;
   }

   private static void y(ArrayDeque<pc.r> var0, pc.r var1) {
      if (var0.size() == 64) {
         var0.removeFirst();
      }

      var0.addLast(var1);
   }

   public void R(int var1, int var2, int var3, int var4) {
      m4.p var5 = this.z();
      if (var5 != null && !this.e.test(var5)) {
         this.l(() -> var5.E(hw.m(var5.T(var3) + var1, var5.A(), var3), hw.m(var5.P(var4) + var2, var5.m(), var4), var3, var4));
      }
   }

   public void k() {
      this.l(() -> m4.F(var1 -> !this.e.test(var1)));
   }

   public void X(boolean var1, boolean var2) {
      c7.d var3 = c7.B;
      if (var3.snapping() != var1 || var3.grid() != var2) {
         c7.B = new c7.d(var3.panel(), var3.collapsed(), var1, var2);
         this.L = (boolean)1;
         this.Z();
      }
   }

   public boolean E() {
      if (this.s == c7.w) {
         return false;
      }

      this.d.q(true, true);
      this.Q.clear();
      this.N.clear();
      this.p = null;
      this.L = this.F = (boolean)0;
      this.U = "Saved";
      this.s = c7.w;
      return true;
   }

   public void o() {
      this.d.O(0);
      this.o();
   }

   public void I() {
      long var1 = this.b;
      this.d.N();
      this.o();
      if (this.b == var1) {
         this.Z();
      }
   }

   public record q(hw.l dock, hw.l panel, hw.l body, hw.l retry, List<hw.l> tools, int width, int height) {
      public q(hw.l dock, hw.l panel, hw.l body, hw.l retry, List<hw.l> tools, int width, int height) {
         tools = List.copyOf(tools);
         this.dock = dock;
         this.panel = panel;
         this.body = body;
         this.retry = retry;
         this.tools = tools;
         this.width = width;
         this.height = height;
      }

      public static pc.q calculate(int var0, int var1, pc.v var2, int var3, int var4, int var5, boolean var6, boolean var7) {
         if (!var6) {
            return new pc.q(null, null, null, null, List.of(), var0, var1);
         }

         int var8 = Math.min(12, Math.max(0, var0 / 4));
         int var9 = Math.max(1, Math.min(340, var0 - var8 * 2));
         hw.l var10 = new hw.l((var0 - var9) / 2, Math.max(0, var1 - var8 - 42), var9, Math.min(42, var1));
         ArrayList var11 = new ArrayList();
         int var12 = Math.max(2, var9 - 172);
         int[] var13 = new int[]{var12 / 2 + 4, 28, 28, 28, var12 - var12 / 2 - 4, 56};
         double var14 = Math.min(1.0, (double)Math.max(1, var9 - 32) / Arrays.stream(var13).sum());
         int var16 = var10.x() + 6;

         for (int var20 : var13) {
            int var21 = Math.max(1, (int)Math.floor(var20 * var14));
            var11.add(new hw.l(var16, var10.y() + 6, var21, Math.min(30, var10.height())));
            var16 += var21 + 4;
         }

         hw.l var26 = var7 ? new hw.l((var0 - Math.min(208, var9)) / 2, Math.max(0, var10.y() - 29), Math.min(208, var9), 22) : null;
         if (var2 == pc.v.D) {
            return new pc.q(var10, null, null, var26, var11, var0, var1);
         }

         int var27 = Math.max(1, Math.min(var2 == pc.v.d ? 268 : 260, var0 - var8 * 2));
         int var28 = header(var2);
         int var29 = Math.max(1, var10.y() - (var7 ? 40 : 12) - var8);
         int var30 = Math.min(var28 + Math.min(Math.max(0, var3), 280) + 10, var29);
         int var22 = Math.clamp(var4 < 0 ? var10.x() : var4, var8, Math.max(var8, var0 - var8 - var27));
         int var23 = Math.clamp(var5 < 0 ? var10.y() - var30 - 12 : var5, var8, Math.max(var8, var10.y() - (var7 ? 40 : 12) - var30));
         hw.l var24 = new hw.l(var22, var23, var27, var30);
         hw.l var25 = new hw.l(var22 + 10, var23 + Math.min(var28, var30), Math.max(1, var27 - 20), Math.max(0, var30 - var28 - 10));
         return new pc.q(var10, var24, var25, var26, var11, var0, var1);
      }

      public static int header(pc.v var0) {
         return var0 == pc.v.d ? 78 : (var0 != pc.v.o && var0 != pc.v.F ? 45 : 76);
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

      public boolean controlContains(hw.l var1, double var2, double var4) {
         return this.body != null && this.body.contains(var2, var4, 0) && var1.contains(var2, var4, 0);
      }
   }

   public record r(Map<String, c7.q> positions, Map<String, c7.l> styles, Map<com.larpclient.module.q<?>, Object> settings, Map<Module, Boolean> owners) {
      public r(Map<String, c7.q> positions, Map<String, c7.l> styles, Map<com.larpclient.module.q<?>, Object> settings, Map<Module, Boolean> owners) {
         positions = Map.copyOf(positions);
         styles = Map.copyOf(styles);
         settings = Map.copyOf(settings);
         owners = Map.copyOf(owners);
         this.positions = positions;
         this.styles = styles;
         this.settings = settings;
         this.owners = owners;
      }

      public static pc.r capture() {
         return capture(List.of());
      }

      public static pc.r capture(List<Module> var0) {
         LinkedHashMap var1 = new LinkedHashMap();
         LinkedHashMap var2 = new LinkedHashMap();

         for (Module var4 : var0) {
            var2.put(var4, var4.isEnabled());

            for (com.larpclient.module.q var6 : var4.getSettings()) {
               var1.put(var6, var6.getValue());
            }
         }

         return new pc.r(c7.v, c7.y, var1, var2);
      }

      private static void restoreSetting(com.larpclient.module.q<?> var0, Object var1) {
         if (!Objects.equals(var0.getValue(), var1)) {
            var0.setValue(var1);
         }
      }

      public void restore() {
         c7.v.clear();
         c7.v.putAll(this.positions);
         c7.y.clear();
         c7.y.putAll(this.styles);
         this.settings.forEach(pc.r::restoreSetting);
         this.owners.forEach((var0, var1) -> {
            if (var0.isEnabled() != var1) {
               var0.setEnabled(var1);
            }
         });
      }
   }

   public enum v {
      D,
      d,
      o,
      F,
      s;
   }
}

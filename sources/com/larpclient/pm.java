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

public final class pm {
   public static final int f = 64;
   private static long w = -1L;
   private final BooleanSupplier y;
   private final Predicate<o8.a> l;
   private final List<Module> Q;
   private final j7 e;
   private final ArrayDeque<pm.q> d = new ArrayDeque<>();
   private final ArrayDeque<pm.q> u = new ArrayDeque<>();
   private pm.q v;
   private long I = fc.S;
   private long z;
   private boolean t;
   private boolean c;
   private String h = "Saved";

   public pm(BooleanSupplier var1, Predicate<o8.a> var2) {
      this(var1, var2, List.of());
   }

   public pm(BooleanSupplier var1, Predicate<o8.a> var2, List<Module> var3) {
      this.Q = List.copyOf(var3);
      this.y = var1;
      this.l = var2;
      this.t = (boolean)(w == fc.S ? 1 : 0);
      if (this.t) {
         this.h = "Save failed. Retry";
      }

      this.e = new j7(this::Y, var0 -> {}, () -> {});
      this.e.Y(var2);
   }

   public j7 q() {
      return this.e;
   }

   public o8.a k() {
      return this.e.n();
   }

   public boolean I() {
      return !this.d.isEmpty();
   }

   public boolean v() {
      return !this.u.isEmpty();
   }

   public int v() {
      return this.d.size();
   }

   public boolean d() {
      return this.t;
   }

   public String E() {
      return this.h;
   }

   public void j(o8.a var1) {
      this.e.i(var1);
      this.Y();
   }

   public void Q() {
      this.g();
      if (this.v == null) {
         this.v = pm.q.capture(this.Q);
      }
   }

   public void i() {
      this.c = (boolean)1;
   }

   public void u(Runnable var1) {
      this.Q();
      var1.run();
      this.Y();
   }

   public boolean Y() {
      if (this.I != fc.S) {
         this.g();
         return true;
      }

      byte var1 = this.c;
      if (this.v != null) {
         if (!this.v.equals(pm.q.capture(this.Q))) {
            S(this.d, this.v);
            this.u.clear();
            var1 = 1;
         }

         this.v = null;
      }

      this.t |= var1;
      this.c = (boolean)0;
      return var1 != 0 ? this.i() : !this.t;
   }

   public boolean i() {
      if (!this.t) {
         return true;
      } else {
         this.z++;
         if (this.y.getAsBoolean()) {
            this.t = (boolean)0;
            w = -1L;
            this.h = "Saved";
            r4.g("HUD editor", "Saved completed workspace edit");
            return true;
         } else {
            w = this.I;
            this.h = "Save failed. Retry";
            r4.m("HUD editor", "Workspace save failed; pending edits retained for retry");
            return false;
         }
      }
   }

   public boolean n() {
      return this.q(this.d, this.u);
   }

   public boolean L() {
      return this.q(this.u, this.d);
   }

   private boolean q(ArrayDeque<pm.q> var1, ArrayDeque<pm.q> var2) {
      this.e.R(0);
      this.Y();
      if (var1.isEmpty()) {
         return false;
      }

      S(var2, pm.q.capture(this.Q));
      ((pm.q)var1.removeLast()).restore();
      this.t = (boolean)1;
      this.i();
      return true;
   }

   private static void S(ArrayDeque<pm.q> var0, pm.q var1) {
      if (var0.size() == 64) {
         var0.removeFirst();
      }

      var0.addLast(var1);
   }

   public void F(int var1, int var2, int var3, int var4) {
      o8.a var5 = this.k();
      if (var5 != null && !this.l.test(var5)) {
         this.u(() -> var5.i(_h.n(var5.b(var3) + var1, var5.c(), var3), _h.n(var5.M(var4) + var2, var5.p(), var4), var3, var4));
      }
   }

   public void r() {
      this.u(() -> o8.K(var1 -> !this.l.test(var1)));
   }

   public void p(boolean var1, boolean var2) {
      fc.p var3 = fc.h;
      if (var3.snapping() != var1 || var3.grid() != var2) {
         fc.h = new fc.p(var3.panel(), var3.collapsed(), var1, var2);
         this.t = (boolean)1;
         this.i();
      }
   }

   public boolean g() {
      if (this.I == fc.S) {
         return false;
      }

      this.e.I(true, true);
      this.d.clear();
      this.u.clear();
      this.v = null;
      this.t = this.c = (boolean)0;
      this.h = "Saved";
      this.I = fc.S;
      return true;
   }

   public void g() {
      this.e.R(0);
      this.Y();
   }

   public void p() {
      long var1 = this.z;
      this.e.N();
      this.Y();
      if (this.z == var1) {
         this.i();
      }
   }

   public record c(_h.p dock, _h.p panel, _h.p body, _h.p retry, List<_h.p> tools, int width, int height) {
      public c(_h.p dock, _h.p panel, _h.p body, _h.p retry, List<_h.p> tools, int width, int height) {
         tools = List.copyOf(tools);
         this.dock = dock;
         this.panel = panel;
         this.body = body;
         this.retry = retry;
         this.tools = tools;
         this.width = width;
         this.height = height;
      }

      public static pm.c calculate(int var0, int var1, pm.d var2, int var3, int var4, int var5, boolean var6, boolean var7) {
         if (!var6) {
            return new pm.c(null, null, null, null, List.of(), var0, var1);
         }

         int var8 = Math.min(12, Math.max(0, var0 / 4));
         int var9 = Math.max(1, Math.min(340, var0 - var8 * 2));
         _h.p var10 = new _h.p((var0 - var9) / 2, Math.max(0, var1 - var8 - 42), var9, Math.min(42, var1));
         ArrayList var11 = new ArrayList();
         int var12 = Math.max(2, var9 - 172);
         int[] var13 = new int[]{var12 / 2 + 4, 28, 28, 28, var12 - var12 / 2 - 4, 56};
         double var14 = Math.min(1.0, (double)Math.max(1, var9 - 32) / Arrays.stream(var13).sum());
         int var16 = var10.x() + 6;

         for (int var20 : var13) {
            int var21 = Math.max(1, (int)Math.floor(var20 * var14));
            var11.add(new _h.p(var16, var10.y() + 6, var21, Math.min(30, var10.height())));
            var16 += var21 + 4;
         }

         _h.p var26 = var7 ? new _h.p((var0 - Math.min(208, var9)) / 2, Math.max(0, var10.y() - 29), Math.min(208, var9), 22) : null;
         if (var2 == pm.d.e) {
            return new pm.c(var10, null, null, var26, var11, var0, var1);
         }

         int var27 = Math.max(1, Math.min(var2 == pm.d.W ? 268 : 260, var0 - var8 * 2));
         int var28 = header(var2);
         int var29 = Math.max(1, var10.y() - (var7 ? 40 : 12) - var8);
         int var30 = Math.min(var28 + Math.min(Math.max(0, var3), 280) + 10, var29);
         int var22 = Math.clamp(var4 < 0 ? var10.x() : var4, var8, Math.max(var8, var0 - var8 - var27));
         int var23 = Math.clamp(var5 < 0 ? var10.y() - var30 - 12 : var5, var8, Math.max(var8, var10.y() - (var7 ? 40 : 12) - var30));
         _h.p var24 = new _h.p(var22, var23, var27, var30);
         _h.p var25 = new _h.p(var22 + 10, var23 + Math.min(var28, var30), Math.max(1, var27 - 20), Math.max(0, var30 - var28 - 10));
         return new pm.c(var10, var24, var25, var26, var11, var0, var1);
      }

      public static int header(pm.d var0) {
         return var0 == pm.d.W ? 78 : (var0 != pm.d.f && var0 != pm.d.b ? 45 : 76);
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

      public boolean controlContains(_h.p var1, double var2, double var4) {
         return this.body != null && this.body.contains(var2, var4, 0) && var1.contains(var2, var4, 0);
      }
   }

   public enum d {
      e,
      W,
      f,
      b,
      p;
   }

   public record q(Map<String, fc.g> positions, Map<String, fc.n> styles, Map<com.larpclient.module.l6<?>, Object> settings, Map<Module, Boolean> owners) {
      public q(Map<String, fc.g> positions, Map<String, fc.n> styles, Map<com.larpclient.module.l6<?>, Object> settings, Map<Module, Boolean> owners) {
         positions = Map.copyOf(positions);
         styles = Map.copyOf(styles);
         settings = Map.copyOf(settings);
         owners = Map.copyOf(owners);
         this.positions = positions;
         this.styles = styles;
         this.settings = settings;
         this.owners = owners;
      }

      public static pm.q capture() {
         return capture(List.of());
      }

      public static pm.q capture(List<Module> var0) {
         LinkedHashMap var1 = new LinkedHashMap();
         LinkedHashMap var2 = new LinkedHashMap();

         for (Module var4 : var0) {
            var2.put(var4, var4.isEnabled());

            for (com.larpclient.module.l6 var6 : var4.getSettings()) {
               var1.put(var6, var6.getValue());
            }
         }

         return new pm.q(fc.m, fc.Q, var1, var2);
      }

      private static void restoreSetting(com.larpclient.module.l6<?> var0, Object var1) {
         if (!Objects.equals(var0.getValue(), var1)) {
            var0.setValue(var1);
         }
      }

      public void restore() {
         fc.m.clear();
         fc.m.putAll(this.positions);
         fc.Q.clear();
         fc.Q.putAll(this.styles);
         this.settings.forEach(pm.q::restoreSetting);
         this.owners.forEach((var0, var1) -> {
            if (var0.isEnabled() != var1) {
               var0.setEnabled(var1);
            }
         });
      }
   }
}

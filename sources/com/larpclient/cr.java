package com.larpclient;

import com.larpclient.module.Module;
import com.larpclient.module.df;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public final class cr {
   public static final int b = 64;
   private static long n = -1L;
   private final BooleanSupplier Q;
   private final Predicate<mq.s> l;
   private final List<Module> K;
   private final c4 R;
   private final ArrayDeque<cr.e> p = new ArrayDeque<>();
   private final ArrayDeque<cr.e> c = new ArrayDeque<>();
   private cr.e s;
   private long h = cx.U;
   private long G;
   private boolean A;
   private boolean t;
   private String M = "Saved";

   public cr(BooleanSupplier var1, Predicate<mq.s> var2) {
      this(var1, var2, List.of());
   }

   public cr(BooleanSupplier var1, Predicate<mq.s> var2, List<Module> var3) {
      this.K = List.copyOf(var3);
      this.Q = var1;
      this.l = var2;
      this.A = (boolean)(n == cx.U ? 1 : 0);
      if (this.A) {
         this.M = "Save failed. Retry";
      }

      this.R = new c4(this::o, var0 -> {}, () -> {});
      this.R.A(var2);
   }

   public c4 J() {
      return this.R;
   }

   public mq.s l() {
      return this.R.k();
   }

   public boolean D() {
      return !this.p.isEmpty();
   }

   public boolean n() {
      return !this.c.isEmpty();
   }

   public int G() {
      return this.p.size();
   }

   public boolean Y() {
      return this.A;
   }

   public String Z() {
      return this.M;
   }

   public void M(mq.s var1) {
      this.R.Q(var1);
      this.o();
   }

   public void M() {
      this.k();
      if (this.s == null) {
         this.s = cr.e.capture(this.K);
      }
   }

   public void y() {
      this.t = (boolean)1;
   }

   public void S(Runnable var1) {
      this.M();
      var1.run();
      this.o();
   }

   public boolean o() {
      if (this.h != cx.U) {
         this.k();
         return true;
      }

      byte var1 = this.t;
      if (this.s != null) {
         if (!this.s.equals(cr.e.capture(this.K))) {
            O(this.p, this.s);
            this.c.clear();
            var1 = 1;
         }

         this.s = null;
      }

      this.A |= var1;
      this.t = (boolean)0;
      return var1 != 0 ? this.m() : !this.A;
   }

   public boolean m() {
      if (!this.A) {
         return true;
      } else {
         this.G++;
         if (this.Q.getAsBoolean()) {
            this.A = (boolean)0;
            n = -1L;
            this.M = "Saved";
            mt.U("HUD editor", "Saved completed workspace edit");
            return true;
         } else {
            n = this.h;
            this.M = "Save failed. Retry";
            mt.T("HUD editor", "Workspace save failed; pending edits retained for retry");
            return false;
         }
      }
   }

   public boolean T() {
      return this.N(this.p, this.c);
   }

   public boolean z() {
      return this.N(this.c, this.p);
   }

   private boolean N(ArrayDeque<cr.e> var1, ArrayDeque<cr.e> var2) {
      this.R.a(0);
      this.o();
      if (var1.isEmpty()) {
         return false;
      }

      O(var2, cr.e.capture(this.K));
      ((cr.e)var1.removeLast()).restore();
      this.A = (boolean)1;
      this.m();
      return true;
   }

   private static void O(ArrayDeque<cr.e> var0, cr.e var1) {
      if (var0.size() == 64) {
         var0.removeFirst();
      }

      var0.addLast(var1);
   }

   public void M(int var1, int var2, int var3, int var4) {
      mq.s var5 = this.l();
      if (var5 != null && !this.l.test(var5)) {
         this.S(() -> var5.s(ez.a(var5.W(var3) + var1, var5.Y(), var3), ez.a(var5.i(var4) + var2, var5.K(), var4), var3, var4));
      }
   }

   public void R() {
      this.S(() -> mq.P(var1 -> !this.l.test(var1)));
   }

   public void e(boolean var1, boolean var2) {
      cx.h var3 = cx.l;
      if (var3.snapping() != var1 || var3.grid() != var2) {
         cx.l = new cx.h(var3.panel(), var3.collapsed(), var1, var2);
         this.A = (boolean)1;
         this.m();
      }
   }

   public boolean k() {
      if (this.h == cx.U) {
         return false;
      }

      this.R.h(true, true);
      this.p.clear();
      this.c.clear();
      this.s = null;
      this.A = this.t = (boolean)0;
      this.M = "Saved";
      this.h = cx.U;
      return true;
   }

   public void b() {
      this.R.a(0);
      this.o();
   }

   public void G() {
      long var1 = this.G;
      this.R.y();
      this.o();
      if (this.G == var1) {
         this.m();
      }
   }

   public record a(ez.h dock, ez.h panel, ez.h body, ez.h retry, List<ez.h> tools, int width, int height) {
      public a(ez.h dock, ez.h panel, ez.h body, ez.h retry, List<ez.h> tools, int width, int height) {
         tools = List.copyOf(tools);
         this.dock = dock;
         this.panel = panel;
         this.body = body;
         this.retry = retry;
         this.tools = tools;
         this.width = width;
         this.height = height;
      }

      public static cr.a calculate(int var0, int var1, cr.y var2, int var3, int var4, int var5, boolean var6, boolean var7) {
         if (!var6) {
            return new cr.a(null, null, null, null, List.of(), var0, var1);
         }

         int var8 = Math.min(12, Math.max(0, var0 / 4));
         int var9 = Math.max(1, Math.min(340, var0 - var8 * 2));
         ez.h var10 = new ez.h((var0 - var9) / 2, Math.max(0, var1 - var8 - 42), var9, Math.min(42, var1));
         ArrayList var11 = new ArrayList();
         int var12 = Math.max(2, var9 - 172);
         int[] var13 = new int[]{var12 / 2 + 4, 28, 28, 28, var12 - var12 / 2 - 4, 56};
         double var14 = Math.min(1.0, (double)Math.max(1, var9 - 32) / Arrays.stream(var13).sum());
         int var16 = var10.x() + 6;

         for (int var20 : var13) {
            int var21 = Math.max(1, (int)Math.floor(var20 * var14));
            var11.add(new ez.h(var16, var10.y() + 6, var21, Math.min(30, var10.height())));
            var16 += var21 + 4;
         }

         ez.h var26 = var7 ? new ez.h((var0 - Math.min(208, var9)) / 2, Math.max(0, var10.y() - 29), Math.min(208, var9), 22) : null;
         if (var2 == cr.y.U) {
            return new cr.a(var10, null, null, var26, var11, var0, var1);
         }

         int var27 = Math.max(1, Math.min(var2 == cr.y.T ? 268 : 260, var0 - var8 * 2));
         int var28 = header(var2);
         int var29 = Math.max(1, var10.y() - (var7 ? 40 : 12) - var8);
         int var30 = Math.min(var28 + Math.min(Math.max(0, var3), 280) + 10, var29);
         int var22 = Math.clamp(var4 < 0 ? var10.x() : var4, var8, Math.max(var8, var0 - var8 - var27));
         int var23 = Math.clamp(var5 < 0 ? var10.y() - var30 - 12 : var5, var8, Math.max(var8, var10.y() - (var7 ? 40 : 12) - var30));
         ez.h var24 = new ez.h(var22, var23, var27, var30);
         ez.h var25 = new ez.h(var22 + 10, var23 + Math.min(var28, var30), Math.max(1, var27 - 20), Math.max(0, var30 - var28 - 10));
         return new cr.a(var10, var24, var25, var26, var11, var0, var1);
      }

      public static int header(cr.y var0) {
         return var0 == cr.y.T ? 78 : (var0 != cr.y.c && var0 != cr.y.P ? 45 : 76);
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

      public boolean controlContains(ez.h var1, double var2, double var4) {
         return this.body != null && this.body.contains(var2, var4, 0) && var1.contains(var2, var4, 0);
      }
   }

   public record e(Map<String, cx.m> positions, Map<String, cx.z> styles, Map<df<?>, Object> settings, Map<Module, Boolean> owners) {
      public e(Map<String, cx.m> positions, Map<String, cx.z> styles, Map<df<?>, Object> settings, Map<Module, Boolean> owners) {
         positions = Map.copyOf(positions);
         styles = Map.copyOf(styles);
         settings = Map.copyOf(settings);
         owners = Map.copyOf(owners);
         this.positions = positions;
         this.styles = styles;
         this.settings = settings;
         this.owners = owners;
      }

      public static cr.e capture() {
         return capture(List.of());
      }

      public static cr.e capture(List<Module> var0) {
         LinkedHashMap var1 = new LinkedHashMap();
         LinkedHashMap var2 = new LinkedHashMap();

         for (Module var4 : var0) {
            var2.put(var4, var4.isEnabled());

            for (df var6 : var4.getSettings()) {
               var1.put(var6, var6.P());
            }
         }

         return new cr.e(cx.O, cx.D, var1, var2);
      }

      private static void restoreSetting(df<?> var0, Object var1) {
         if (!Objects.equals(var0.P(), var1)) {
            var0.setValue(var1);
         }
      }

      public void restore() {
         cx.O.clear();
         cx.O.putAll(this.positions);
         cx.D.clear();
         cx.D.putAll(this.styles);
         this.settings.forEach(cr.e::restoreSetting);
         this.owners.forEach((var0, var1) -> {
            if (var0.isEnabled() != var1) {
               var0.setEnabled(var1);
            }
         });
      }
   }

   public enum y {
      U,
      T,
      c,
      P,
      r;
   }
}

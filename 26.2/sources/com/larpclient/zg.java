package com.larpclient;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

final class zg {
   final WaypointsModule R;
   private final hs C;
   private final Map<jx, sn> m = new IdentityHashMap<>();
   private String T;
   private String g = "";
   private String H = "";
   private String U = "";
   private com.larpclient.module.i n;
   private String v;
   private String F = "";
   private String h = "";
   private String f = "";
   private com.larpclient.module.i a;
   private boolean D;

   zg(WaypointsModule var1, hs var2) {
      this.R = var1;
      this.C = var2;
      this.n = Q(var1.v());
   }

   private static com.larpclient.module.i Q(int var0) {
      return new com.larpclient.module.i("Color", "Color of this waypoint's beam and label", var0);
   }

   void O() {
      this.m.clear();
      this.T = this.R.Z();
      if (this.T == null) {
         this.C.text("Join a world or server to set waypoints. Each one keeps its own list.");
      } else {
         this.R();
         this.C.h(new j1(this.R.f() ? "Saved on this server" : "Saved in this world"));
         List var1 = this.R.F();
         if (var1.isEmpty()) {
            this.C.text("Nothing saved yet.");
         }

         for (sn var3 : var1) {
            jx var4 = new jx(var3.name(), () -> this.I(var3));
            var4.X = this.R.p(var3);
            var4.I = (boolean)1;
            var4.J = 34;
            var4.z = var3.id().equals(this.v);
            var4.M = var3.visible();
            var4.Q = () -> {
               this.R.k(var3.id(), !var3.visible());
               this.g();
            };
            this.m.put(var4, var3);
            this.C.h(var4);
            if (var3.id().equals(this.v)) {
               this.r(var3);
            }
         }

         String var5 = this.R.S();
         if (!var5.isEmpty()) {
            this.C.text(var5);
         }
      }
   }

   private void R() {
      this.C.h(new j1("New waypoint"));
      this.i("Name (optional)", this.g, var1x -> this.g = var1x, 32, this::b);
      this.i("X Y Z (blank uses your position)", this.H, var1x -> this.H = var1x, 40, this::b);
      this.C.h(new ju(this.n));
      jx var1 = this.C.a("Add waypoint", this::b);
      var1.x = (boolean)1;
      var1.p = (boolean)(!this.R.x() ? 1 : 0);
      if (!this.U.isEmpty()) {
         this.C.text(this.U);
      }
   }

   private void r(sn var1) {
      this.i("Name", this.F, var1x -> this.F = var1x, 32, () -> this.h(var1));
      this.i("X Y Z", this.h, var1x -> this.h = var1x, 40, () -> this.h(var1));
      this.C.h(new ju(this.a));
      Object var2 = new Object();
      jx var3 = this.Y("Save", var2, () -> this.h(var1));
      var3.x = (boolean)1;
      this.Y("Use my position", var2, () -> {
         this.h = this.R.Y();
         this.f = "";
         this.g();
      });
      jx var4 = this.Y(this.D ? "Confirm delete" : "Delete", var2, () -> this.H(var1));
      var4.a = (boolean)1;
      var4.x = this.D;
      if (!this.f.isEmpty()) {
         this.C.text(this.f);
      }
   }

   private void i(String var1, String var2, Consumer<String> var3, int var4, Runnable var5) {
      j9 var6 = new j9(var1, var3, var4);
      var6.H = var2;
      var6.B(var5);
      this.C.h(var6);
   }

   private jx Y(String var1, Object var2, Runnable var3) {
      jx var4 = new jx(var1, var3);
      var4.G = (boolean)1;
      var4.r = var2;
      var4.J = 24;
      this.C.h(var4);
      return var4;
   }

   private void b() {
      String var1 = this.R.v(this.g, this.H, this.n.getValue());
      if (var1.isEmpty()) {
         this.g = this.H = "";
         this.n = Q(this.R.v());
         this.U = this.R.isEnabled() ? "Waypoint added." : "Waypoint added. Turn the module on to see it.";
      } else {
         this.U = var1;
      }

      this.g();
   }

   private void I(sn var1) {
      this.D = (boolean)0;
      this.f = "";
      if (var1.id().equals(this.v)) {
         this.v = null;
      } else {
         this.v = var1.id();
         this.F = var1.name();
         this.h = pr.M(var1.position());
         this.a = Q(var1.color());
      }

      this.g();
   }

   private void h(sn var1) {
      this.D = (boolean)0;
      this.f = this.R.i(var1.id(), this.F, this.h, this.a.getValue());
      if (this.f.isEmpty()) {
         this.v = null;
      }

      this.g();
   }

   private void H(sn var1) {
      if (!this.D) {
         this.D = (boolean)1;
         this.g();
      } else {
         this.D = (boolean)0;
         this.v = null;
         this.R.Z(var1.id());
         this.g();
      }
   }

   private void g() {
      this.C.j();
   }

   void D() {
      if (!Objects.equals(this.T, this.R.Z())) {
         this.v = null;
         this.g();
      } else {
         this.m.forEach((var1, var2) -> var1.X = this.R.p(var2));
      }
   }
}

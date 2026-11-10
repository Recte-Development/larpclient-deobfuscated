package com.larpclient;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

final class bs {
   final WaypointsModule S;
   private final t2 n;
   private final Map<ls, cy> P = new IdentityHashMap<>();
   private String Q;
   private String H = "";
   private String C = "";
   private String G = "";
   private com.larpclient.module.l0 K;
   private String L;
   private String A = "";
   private String l = "";
   private String s = "";
   private com.larpclient.module.l0 T;
   private boolean F;

   bs(WaypointsModule var1, t2 var2) {
      this.S = var1;
      this.n = var2;
      this.K = W(var1.C());
   }

   private static com.larpclient.module.l0 W(int var0) {
      return new com.larpclient.module.l0("Color", "Color of this waypoint's beam and label", var0);
   }

   void O() {
      this.P.clear();
      this.Q = this.S.e();
      if (this.Q == null) {
         this.n.text("Join a world or server to set waypoints. Each one keeps its own list.");
      } else {
         this.Z();
         this.n.u(new l5(this.S.K() ? "Saved on this server" : "Saved in this world"));
         List var1 = this.S.Z();
         if (var1.isEmpty()) {
            this.n.text("Nothing saved yet.");
         }

         for (cy var3 : var1) {
            ls var4 = new ls(var3.name(), () -> this.b(var3));
            var4.T = this.S.O(var3);
            var4.j = (boolean)1;
            var4.w = 34;
            var4.o = var3.id().equals(this.L);
            var4.W = var3.visible();
            var4.Y = () -> {
               this.S.b(var3.id(), !var3.visible());
               this.I();
            };
            this.P.put(var4, var3);
            this.n.u(var4);
            if (var3.id().equals(this.L)) {
               this.k(var3);
            }
         }

         String var5 = this.S.p();
         if (!var5.isEmpty()) {
            this.n.text(var5);
         }
      }
   }

   private void Z() {
      this.n.u(new l5("New waypoint"));
      this.F("Name (optional)", this.H, var1x -> this.H = var1x, 32, this::d);
      this.F("X Y Z (blank uses your position)", this.C, var1x -> this.C = var1x, 40, this::d);
      this.n.u(new l9(this.K));
      ls var1 = this.n.s("Add waypoint", this::d);
      var1.v = (boolean)1;
      var1.N = (boolean)(!this.S.y() ? 1 : 0);
      if (!this.G.isEmpty()) {
         this.n.text(this.G);
      }
   }

   private void k(cy var1) {
      this.F("Name", this.A, var1x -> this.A = var1x, 32, () -> this.X(var1));
      this.F("X Y Z", this.l, var1x -> this.l = var1x, 40, () -> this.X(var1));
      this.n.u(new l9(this.T));
      Object var2 = new Object();
      ls var3 = this.e("Save", var2, () -> this.X(var1));
      var3.v = (boolean)1;
      this.e("Use my position", var2, () -> {
         this.l = this.S.C();
         this.s = "";
         this.I();
      });
      ls var4 = this.e(this.F ? "Confirm delete" : "Delete", var2, () -> this.h(var1));
      var4.L = (boolean)1;
      var4.v = this.F;
      if (!this.s.isEmpty()) {
         this.n.text(this.s);
      }
   }

   private void F(String var1, String var2, Consumer<String> var3, int var4, Runnable var5) {
      lo var6 = new lo(var1, var3, var4);
      var6.h = var2;
      var6.T(var5);
      this.n.u(var6);
   }

   private ls e(String var1, Object var2, Runnable var3) {
      ls var4 = new ls(var1, var3);
      var4.x = (boolean)1;
      var4.U = var2;
      var4.w = 24;
      this.n.u(var4);
      return var4;
   }

   private void d() {
      String var1 = this.S.D(this.H, this.C, this.K.getValue());
      if (var1.isEmpty()) {
         this.H = this.C = "";
         this.K = W(this.S.C());
         this.G = this.S.isEnabled() ? "Waypoint added." : "Waypoint added. Turn the module on to see it.";
      } else {
         this.G = var1;
      }

      this.I();
   }

   private void b(cy var1) {
      this.F = (boolean)0;
      this.s = "";
      if (var1.id().equals(this.L)) {
         this.L = null;
      } else {
         this.L = var1.id();
         this.A = var1.name();
         this.l = qp.N(var1.position());
         this.T = W(var1.color());
      }

      this.I();
   }

   private void X(cy var1) {
      this.F = (boolean)0;
      this.s = this.S.R(var1.id(), this.A, this.l, this.T.getValue());
      if (this.s.isEmpty()) {
         this.L = null;
      }

      this.I();
   }

   private void h(cy var1) {
      if (!this.F) {
         this.F = (boolean)1;
         this.I();
      } else {
         this.F = (boolean)0;
         this.L = null;
         this.S.t(var1.id());
         this.I();
      }
   }

   private void I() {
      this.n.O();
   }

   void S() {
      if (!Objects.equals(this.Q, this.S.e())) {
         this.L = null;
         this.I();
      } else {
         this.P.forEach((var1, var2) -> var1.T = this.S.O(var2));
      }
   }
}

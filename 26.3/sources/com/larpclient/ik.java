package com.larpclient;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

final class ik {
   final WaypointsModule Z;
   private final fu s;
   private final Map<s8, nu> z = new IdentityHashMap<>();
   private String F;
   private String K = "";
   private String n = "";
   private String G = "";
   private com.larpclient.module.h O;
   private String i;
   private String g = "";
   private String S = "";
   private String r = "";
   private com.larpclient.module.h C;
   private boolean D;

   ik(WaypointsModule var1, fu var2) {
      this.Z = var1;
      this.s = var2;
      this.O = d(var1.b());
   }

   private static com.larpclient.module.h d(int var0) {
      return new com.larpclient.module.h("Color", "Color of this waypoint's beam and label", var0);
   }

   void Q() {
      this.z.clear();
      this.F = this.Z.u();
      if (this.F == null) {
         this.s.text("Join a world or server to set waypoints. Each one keeps its own list.");
      } else {
         this.J();
         this.s.f(new sj(this.Z.L() ? "Saved on this server" : "Saved in this world"));
         List var1 = this.Z.U();
         if (var1.isEmpty()) {
            this.s.text("Nothing saved yet.");
         }

         for (nu var3 : var1) {
            s8 var4 = new s8(var3.name(), () -> this.Z(var3));
            var4.z = this.Z.g(var3);
            var4.b = (boolean)1;
            var4.O = 34;
            var4.a = var3.id().equals(this.i);
            var4.G = var3.visible();
            var4.o = () -> {
               this.Z.a(var3.id(), !var3.visible());
               this.k();
            };
            this.z.put(var4, var3);
            this.s.f(var4);
            if (var3.id().equals(this.i)) {
               this.j(var3);
            }
         }

         String var5 = this.Z.o();
         if (!var5.isEmpty()) {
            this.s.text(var5);
         }
      }
   }

   private void J() {
      this.s.f(new sj("New waypoint"));
      this.U("Name (optional)", this.K, var1x -> this.K = var1x, 32, this::L);
      this.U("X Y Z (blank uses your position)", this.n, var1x -> this.n = var1x, 40, this::L);
      this.s.f(new su(this.O));
      s8 var1 = this.s.t("Add waypoint", this::L);
      var1.d = (boolean)1;
      var1.V = (boolean)(!this.Z.C() ? 1 : 0);
      if (!this.G.isEmpty()) {
         this.s.text(this.G);
      }
   }

   private void j(nu var1) {
      this.U("Name", this.g, var1x -> this.g = var1x, 32, () -> this.P(var1));
      this.U("X Y Z", this.S, var1x -> this.S = var1x, 40, () -> this.P(var1));
      this.s.f(new su(this.C));
      Object var2 = new Object();
      s8 var3 = this.O("Save", var2, () -> this.P(var1));
      var3.d = (boolean)1;
      this.O("Use my position", var2, () -> {
         this.S = this.Z.G();
         this.r = "";
         this.k();
      });
      s8 var4 = this.O(this.D ? "Confirm delete" : "Delete", var2, () -> this.y(var1));
      var4.f = (boolean)1;
      var4.d = this.D;
      if (!this.r.isEmpty()) {
         this.s.text(this.r);
      }
   }

   private void U(String var1, String var2, Consumer<String> var3, int var4, Runnable var5) {
      sk var6 = new sk(var1, var3, var4);
      var6.j = var2;
      var6.O(var5);
      this.s.f(var6);
   }

   private s8 O(String var1, Object var2, Runnable var3) {
      s8 var4 = new s8(var1, var3);
      var4.Z = (boolean)1;
      var4.l = var2;
      var4.O = 24;
      this.s.f(var4);
      return var4;
   }

   private void L() {
      String var1 = this.Z.P(this.K, this.n, this.O.getValue());
      if (var1.isEmpty()) {
         this.K = this.n = "";
         this.O = d(this.Z.b());
         this.G = this.Z.isEnabled() ? "Waypoint added." : "Waypoint added. Turn the module on to see it.";
      } else {
         this.G = var1;
      }

      this.k();
   }

   private void Z(nu var1) {
      this.D = (boolean)0;
      this.r = "";
      if (var1.id().equals(this.i)) {
         this.i = null;
      } else {
         this.i = var1.id();
         this.g = var1.name();
         this.S = w.o(var1.position());
         this.C = d(var1.color());
      }

      this.k();
   }

   private void P(nu var1) {
      this.D = (boolean)0;
      this.r = this.Z.I(var1.id(), this.g, this.S, this.C.getValue());
      if (this.r.isEmpty()) {
         this.i = null;
      }

      this.k();
   }

   private void y(nu var1) {
      if (!this.D) {
         this.D = (boolean)1;
         this.k();
      } else {
         this.D = (boolean)0;
         this.i = null;
         this.Z.c(var1.id());
         this.k();
      }
   }

   private void k() {
      this.s.z();
   }

   void s() {
      if (!Objects.equals(this.F, this.Z.u())) {
         this.i = null;
         this.k();
      } else {
         this.z.forEach((var1, var2) -> var1.z = this.Z.g(var2));
      }
   }
}

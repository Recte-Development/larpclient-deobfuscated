package com.larpclient;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

final class so {
   private final tf y;
   String X = "";
   int d;
   int T;
   long g;
   boolean f;
   List<JsonObject> b = List.of();
   JsonObject U;

   so(tf var1) {
      this.y = var1;
   }

   void I() {
      this.U = null;
   }

   void C() {
      String var1 = this.X.toLowerCase(Locale.ROOT);

      for (JsonObject var3 : this.b) {
         String var4 = h.f(var3, "name", 32);
         String var5 = h.f(var3, "description", 4000);
         if ((var4 + " " + var5 + " " + h.f(var3, "author", 160)).toLowerCase(Locale.ROOT).contains(var1)) {
            this.y.I(var4, h.f(var3, "author", 160), this.U == var3, () -> {
               this.U = var3;
               this.y.f = 0;
               this.y.build();
            });
         }
      }
   }

   void p() {
      if (this.U != null) {
         String var1 = h.f(this.U, "author", 160);
         this.y.m(h.f(this.U, "name", 32), var1.isBlank() ? "" : "By " + var1);
         String var2 = h.f(this.U, "description", 4000);
         if (!var2.isBlank()) {
            this.y.text(var2);
         }

         ArrayList var3 = new ArrayList();
         String var4 = h.f(this.U, "minecraftVersion", 160);
         String var5 = h.f(this.U, "larpVersion", 160);
         if (!var4.isBlank()) {
            var3.add("Minecraft " + var4);
         }

         if (!var5.isBlank()) {
            var3.add("Larp " + var5);
         }

         if (!var3.isEmpty()) {
            this.y.text(String.join(" · ", var3));
         }

         this.y.f("Preview import", () -> this.y.c.u(t4.F(this.U))).v = (boolean)1;
      }
   }

   void k() {
      this.y.f = 0;
      this.U = null;
      this.f = (boolean)0;
      this.b = List.of();
      this.y.y("Loading marketplace...", this.y.Jd.q.V("/api/configs?limit=30&offset=" + this.d, false).thenApply(var0 -> {
         ey.validate(var0);
         return (JsonObject)var0;
      }), var1 -> {
         this.b = ey.validate(var1).items();
         this.T = var1.get("total").getAsInt();
         this.f = (boolean)1;
         this.y.JK = "Search filters this loaded page (" + this.b.size() + " of " + this.T + ").";
      });
   }

   void e() {
      if (this.g != 0L && System.currentTimeMillis() >= this.g && this.y.f()) {
         this.g = 0L;
         this.y.F();
      }
   }
}

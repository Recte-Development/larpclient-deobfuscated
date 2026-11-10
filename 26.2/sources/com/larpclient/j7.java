package com.larpclient;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

final class j7 {
   private final hd F;
   String l = "";
   int E;
   int h;
   long D;
   boolean M;
   List<JsonObject> Y = List.of();
   JsonObject i;

   j7(hd var1) {
      this.F = var1;
   }

   void V() {
      this.i = null;
   }

   void I() {
      String var1 = this.l.toLowerCase(Locale.ROOT);

      for (JsonObject var3 : this.Y) {
         String var4 = km.a(var3, "name", 32);
         String var5 = km.a(var3, "description", 4000);
         if ((var4 + " " + var5 + " " + km.a(var3, "author", 160)).toLowerCase(Locale.ROOT).contains(var1)) {
            this.F.E(var4, km.a(var3, "author", 160), this.i == var3, () -> {
               this.i = var3;
               this.F.Y = 0;
               this.F.build();
            });
         }
      }
   }

   void t() {
      if (this.i != null) {
         String var1 = km.a(this.i, "author", 160);
         this.F.b(km.a(this.i, "name", 32), var1.isBlank() ? "" : "By " + var1);
         String var2 = km.a(this.i, "description", 4000);
         if (!var2.isBlank()) {
            this.F.text(var2);
         }

         ArrayList var3 = new ArrayList();
         String var4 = km.a(this.i, "minecraftVersion", 160);
         String var5 = km.a(this.i, "larpVersion", 160);
         if (!var4.isBlank()) {
            var3.add("Minecraft " + var4);
         }

         if (!var5.isBlank()) {
            var3.add("Larp " + var5);
         }

         if (!var3.isEmpty()) {
            this.F.text(String.join(" · ", var3));
         }

         this.F.k("Preview import", () -> this.F.Q.g(ut.o(this.i))).x = (boolean)1;
      }
   }

   void h() {
      this.F.Y = 0;
      this.i = null;
      this.M = (boolean)0;
      this.Y = List.of();
      this.F.o("Loading marketplace...", this.F.GQ.h.Y("/api/configs?limit=30&offset=" + this.E, false).thenApply(var0 -> {
         c_.validate(var0);
         return (JsonObject)var0;
      }), var1 -> {
         this.Y = c_.validate(var1).items();
         this.h = var1.get("total").getAsInt();
         this.M = (boolean)1;
         this.F.Gr = "Search filters this loaded page (" + this.Y.size() + " of " + this.h + ").";
      });
   }

   void L() {
      if (this.D != 0L && System.currentTimeMillis() >= this.D && this.F.I()) {
         this.D = 0L;
         this.F.L();
      }
   }
}

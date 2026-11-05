package com.larpclient;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

final class uz {
   private final pz O;
   String R = "";
   int b;
   int M;
   long f;
   boolean m;
   List<JsonObject> S = List.of();
   JsonObject a;

   uz(pz var1) {
      this.O = var1;
   }

   void u() {
      this.a = null;
   }

   void e() {
      String var1 = this.R.toLowerCase(Locale.ROOT);

      for (JsonObject var3 : this.S) {
         String var4 = yj.y(var3, "name", 32);
         String var5 = yj.y(var3, "description", 4000);
         if ((var4 + " " + var5 + " " + yj.y(var3, "author", 160)).toLowerCase(Locale.ROOT).contains(var1)) {
            this.O.f(var4, yj.y(var3, "author", 160), this.a == var3, () -> {
               this.a = var3;
               this.O.e = 0;
               this.O.build();
            });
         }
      }
   }

   void L() {
      if (this.a != null) {
         String var1 = yj.y(this.a, "author", 160);
         this.O.t(yj.y(this.a, "name", 32), var1.isBlank() ? "" : "By " + var1);
         String var2 = yj.y(this.a, "description", 4000);
         if (!var2.isBlank()) {
            this.O.text(var2);
         }

         ArrayList var3 = new ArrayList();
         String var4 = yj.y(this.a, "minecraftVersion", 160);
         String var5 = yj.y(this.a, "larpVersion", 160);
         if (!var4.isBlank()) {
            var3.add("Minecraft " + var4);
         }

         if (!var5.isBlank()) {
            var3.add("Larp " + var5);
         }

         if (!var3.isEmpty()) {
            this.O.text(String.join(" · ", var3));
         }

         this.O.V("Preview import", () -> this.O.dn.W(_4.g(this.a))).d = (boolean)1;
      }
   }

   void f() {
      this.O.e = 0;
      this.a = null;
      this.m = (boolean)0;
      this.S = List.of();
      this.O.t("Loading marketplace...", this.O.K.f.X("/api/configs?limit=30&offset=" + this.b, false).thenApply(var0 -> {
         r0.validate(var0);
         return (JsonObject)var0;
      }), var1 -> {
         this.S = r0.validate(var1).items();
         this.M = var1.get("total").getAsInt();
         this.m = (boolean)1;
         this.O.V = "Search filters this loaded page (" + this.S.size() + " of " + this.M + ").";
      });
   }

   void y() {
      if (this.f != 0L && System.currentTimeMillis() >= this.f && this.O.e()) {
         this.f = 0L;
         this.O.O();
      }
   }
}

package com.larpclient;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

final class uu {
   private final hx p;
   String V = "";
   int M;
   int n;
   long D;
   boolean B;
   List<JsonObject> z = List.of();
   JsonObject v;

   uu(hx var1) {
      this.p = var1;
   }

   void H() {
      this.v = null;
   }

   void W() {
      String var1 = this.V.toLowerCase(Locale.ROOT);

      for (JsonObject var3 : this.z) {
         String var4 = p_.Q(var3, "name", 32);
         String var5 = p_.Q(var3, "description", 4000);
         if ((var4 + " " + var5 + " " + p_.Q(var3, "author", 160)).toLowerCase(Locale.ROOT).contains(var1)) {
            this.p.N(var4, p_.Q(var3, "author", 160), this.v == var3, () -> {
               this.v = var3;
               this.p.Z = 0;
               this.p.build();
            });
         }
      }
   }

   void O() {
      if (this.v != null) {
         String var1 = p_.Q(this.v, "author", 160);
         this.p.H(p_.Q(this.v, "name", 32), var1.isBlank() ? "" : "By " + var1);
         String var2 = p_.Q(this.v, "description", 4000);
         if (!var2.isBlank()) {
            this.p.text(var2);
         }

         ArrayList var3 = new ArrayList();
         String var4 = p_.Q(this.v, "minecraftVersion", 160);
         String var5 = p_.Q(this.v, "larpVersion", 160);
         if (!var4.isBlank()) {
            var3.add("Minecraft " + var4);
         }

         if (!var5.isBlank()) {
            var3.add("Larp " + var5);
         }

         if (!var3.isEmpty()) {
            this.p.text(String.join(" · ", var3));
         }

         this.p.k("Preview import", () -> this.p.v6.S(md.a(this.v))).w = (boolean)1;
      }
   }

   void Q() {
      this.p.Z = 0;
      this.v = null;
      this.B = (boolean)0;
      this.z = List.of();
      this.p.n("Loading marketplace...", this.p.h.n.F("/api/configs?limit=30&offset=" + this.M, false).thenApply(var0 -> {
         o0.validate(var0);
         return (JsonObject)var0;
      }), var1 -> {
         this.z = o0.validate(var1).items();
         this.n = var1.get("total").getAsInt();
         this.B = (boolean)1;
         this.p.A = "Search filters this loaded page (" + this.z.size() + " of " + this.n + ").";
      });
   }

   void A() {
      if (this.D != 0L && System.currentTimeMillis() >= this.D && this.p.F()) {
         this.D = 0L;
         this.p.J();
      }
   }
}

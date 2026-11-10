package com.larpclient;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

final class oq {
   private final fk h;
   String v = "";
   int G;
   int L;
   long u;
   boolean w;
   List<JsonObject> P = List.of();
   JsonObject S;

   oq(fk var1) {
      this.h = var1;
   }

   void s() {
      this.S = null;
   }

   void x() {
      String var1 = this.v.toLowerCase(Locale.ROOT);

      for (JsonObject var3 : this.P) {
         String var4 = _4.m(var3, "name", 32);
         String var5 = _4.m(var3, "description", 4000);
         if ((var4 + " " + var5 + " " + _4.m(var3, "author", 160)).toLowerCase(Locale.ROOT).contains(var1)) {
            this.h.A(var4, _4.m(var3, "author", 160), this.S == var3, () -> {
               this.S = var3;
               this.h.L = 0;
               this.h.build();
            });
         }
      }
   }

   void c() {
      if (this.S != null) {
         String var1 = _4.m(this.S, "author", 160);
         this.h.L(_4.m(this.S, "name", 32), var1.isBlank() ? "" : "By " + var1);
         String var2 = _4.m(this.S, "description", 4000);
         if (!var2.isBlank()) {
            this.h.text(var2);
         }

         ArrayList var3 = new ArrayList();
         String var4 = _4.m(this.S, "minecraftVersion", 160);
         String var5 = _4.m(this.S, "larpVersion", 160);
         if (!var4.isBlank()) {
            var3.add("Minecraft " + var4);
         }

         if (!var5.isBlank()) {
            var3.add("Larp " + var5);
         }

         if (!var3.isEmpty()) {
            this.h.text(String.join(" · ", var3));
         }

         this.h.i("Preview import", () -> this.h.t.y(st.W(this.S))).d = (boolean)1;
      }
   }

   void K() {
      this.h.L = 0;
      this.S = null;
      this.w = (boolean)0;
      this.P = List.of();
      this.h.H("Loading marketplace...", this.h.Pp.E.x("/api/configs?limit=30&offset=" + this.G, false).thenApply(var0 -> {
         bx.validate(var0);
         return (JsonObject)var0;
      }), var1 -> {
         this.P = bx.validate(var1).items();
         this.L = var1.get("total").getAsInt();
         this.w = (boolean)1;
         this.h.w = "Search filters this loaded page (" + this.P.size() + " of " + this.L + ").";
      });
   }

   void M() {
      if (this.u != 0L && System.currentTimeMillis() >= this.u && this.h.h()) {
         this.u = 0L;
         this.h.n();
      }
   }
}

package com.larpclient;

import com.google.gson.JsonObject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1657;
import net.minecraft.class_2663;
import net.minecraft.class_310;
import net.minecraft.class_642;
import net.minecraft.class_8143;

public final class uf {
   private static uf j;
   private final s L;
   private final CompletableFuture<JsonObject> K;
   private ye G;
   private long w;
   private long t;
   private long J = -1L;
   private Object Z;
   private boolean u;

   private uf() {
      class_310 var1 = class_310.method_1551();
      this.L = new s(FabricLoader.getInstance().getConfigDir().resolve("larp-client/activity.json"), var1x -> var1.execute(() -> q8.O("Activity", var1x, null)));
      this.K = this.L.H();
   }

   public static void b() {
      if (j == null) {
         j = new uf();
      }
   }

   private static long P() {
      return System.nanoTime() / 1000000L;
   }

   public static void i(class_310 var0) {
      if (j != null) {
         j.x(var0);
      }
   }

   private void x(class_310 var1) {
      if (!this.u && this.K.isDone()) {
         long var2 = P();
         if (this.G == null) {
            this.G = new ye((var1x, var2x) -> this.L.<CompletableFuture<JsonObject>>A(() -> {
               PublicApiAccess var2xx = PublicApiAccess.anonymous();
               if (!var2xx.v().equals(var1x)) {
                  throw new u6(401, "UNAUTHENTICATED");
               } else {
                  return ja.n.q.u(var2x == null ? "GET" : "POST", var2x == null ? "/api/activity/preferences" : "/api/activity", var2x, true, null, var2xx);
               }
            }).thenCompose(var0 -> (CompletionStage<JsonObject>)var0), var0 -> q8.v("Activity", var0), this.K.join());
         }

         if (var2 >= this.w) {
            this.w = var2 + 1000L;
            if (this.Z != var1.field_1687) {
               this.Z = var1.field_1687;
               this.G.i();
            }

            class_642 var4 = var1.method_1558();
            String var5 = var1.field_1724 != null && var1.field_1687 != null && !var1.method_1542() && var4 != null ? var4.field_3761 : null;
            this.G.d("", var5, var5 != null && !var1.method_1493(), var2, System.currentTimeMillis());
            if (var2 >= this.t || this.J != this.G.R()) {
               this.J = this.G.R();
               this.t = var2 + 30000L;
               this.L.b(this.G.x());
            }
         }
      }
   }

   public static void h(class_8143 var0) {
      uf var1 = j;
      class_310 var2 = class_310.method_1551();
      if (var1 != null && var1.G != null && var2.field_1687 != null && var2.field_1724 != null) {
         if (var1.Z != var2.field_1687) {
            var1.Z = var2.field_1687;
            var1.G.i();
         }

         if (var2.field_1687.method_8469(var0.comp_1267()) instanceof class_1657 var4 && var4 != var2.field_1724) {
            var1.G.W(var4.method_5667(), var0.comp_1269() == var2.field_1724.method_5628(), P());
         }
      }
   }

   public static void J(class_2663 var0) {
      uf var1 = j;
      class_310 var2 = class_310.method_1551();
      if (var1 != null && var1.G != null && var2.field_1687 != null && var0.method_11470() == 3) {
         if (var1.Z != var2.field_1687) {
            var1.Z = var2.field_1687;
            var1.G.i();
         }

         if (var0.method_11469(var2.field_1687) instanceof class_1657 var3 && var3 != var2.field_1724) {
            var1.G.x(var3.method_5667(), P());
         }
      }
   }

   public static void K() {
      uf var0 = j;
      if (var0 != null && var0.G != null) {
         var0.G.G();
         var0.L.b(var0.G.x());
         var0.Z = null;
      }
   }

   public static void N() {
      uf var0 = j;
      if (var0 != null) {
         K();
         var0.u = (boolean)1;
         var0.L.close();
      }
   }
}

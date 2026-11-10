package com.larpclient;

import com.google.gson.JsonObject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.entity.player.Player;

public final class lq {
   private static lq Y;
   private final tg F;
   private final CompletableFuture<JsonObject> Q;
   private lc w;
   private long M;
   private long B;
   private long n = -1L;
   private Object W;
   private boolean h;

   private lq() {
      Minecraft var1 = Minecraft.getInstance();
      this.F = new tg(
         FabricLoader.getInstance().getConfigDir().resolve("larp-client/activity.json"),
         var1x -> var1.execute(() -> com.larpclient.h.w("Activity", var1x, null))
      );
      this.Q = this.F.m();
   }

   public static void R() {
      if (Y == null) {
         Y = new lq();
      }
   }

   private static long p() {
      return System.nanoTime() / 1000000L;
   }

   public static void g(Minecraft var0) {
      if (Y != null) {
         Y.v(var0);
      }
   }

   private void v(Minecraft var1) {
      if (!this.h && this.Q.isDone()) {
         long var2 = p();
         if (this.w == null) {
            this.w = new lc((var1x, var2x) -> this.F.<CompletableFuture<JsonObject>>N(() -> {
               PublicApiAccess var2xx = PublicApiAccess.anonymous();
               if (!var2xx.T().equals(var1x)) {
                  throw new bh(401, "UNAUTHENTICATED");
               } else {
                  return uq.x.h.F(var2x == null ? "GET" : "POST", var2x == null ? "/api/activity/preferences" : "/api/activity", var2x, true, null, var2xx);
               }
            }).thenCompose(var0 -> (CompletionStage<JsonObject>)var0), var0 -> com.larpclient.h.U("Activity", var0), this.Q.join());
         }

         if (var2 >= this.M) {
            this.M = var2 + 1000L;
            if (this.W != var1.level) {
               this.W = var1.level;
               this.w.B();
            }

            ServerData var4 = var1.getCurrentServer();
            String var5 = var1.player != null && var1.level != null && !var1.isLocalServer() && var4 != null ? var4.ip : null;
            this.w.p("", var5, var5 != null && !var1.isPaused(), var2, System.currentTimeMillis());
            if (var2 >= this.B || this.n != this.w.p()) {
               this.n = this.w.p();
               this.B = var2 + 30000L;
               this.F.g(this.w.G());
            }
         }
      }
   }

   public static void m(ClientboundDamageEventPacket var0) {
      lq var1 = Y;
      Minecraft var2 = Minecraft.getInstance();
      if (var1 != null && var1.w != null && var2.level != null && var2.player != null) {
         if (var1.W != var2.level) {
            var1.W = var2.level;
            var1.w.B();
         }

         if (var2.level.getEntity(var0.entityId()) instanceof Player var4 && var4 != var2.player) {
            var1.w.V(var4.getUUID(), var0.sourceCauseId() == var2.player.getId(), p());
         }
      }
   }

   public static void B(ClientboundEntityEventPacket var0) {
      lq var1 = Y;
      Minecraft var2 = Minecraft.getInstance();
      if (var1 != null && var1.w != null && var2.level != null && var0.getEventId() == 3) {
         if (var1.W != var2.level) {
            var1.W = var2.level;
            var1.w.B();
         }

         if (var0.getEntity(var2.level) instanceof Player var3 && var3 != var2.player) {
            var1.w.d(var3.getUUID(), p());
         }
      }
   }

   public static void P() {
      lq var0 = Y;
      if (var0 != null && var0.w != null) {
         var0.w.b();
         var0.F.g(var0.w.G());
         var0.W = null;
      }
   }

   public static void V() {
      lq var0 = Y;
      if (var0 != null) {
         P();
         var0.h = (boolean)1;
         var0.F.close();
      }
   }
}

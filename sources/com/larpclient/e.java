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

public final class e {
   private static e F;
   private final d9 W;
   private final CompletableFuture<JsonObject> N;
   private ht r;
   private long B;
   private long Y;
   private long u = -1L;
   private Object j;
   private boolean T;

   private e() {
      Minecraft var1 = Minecraft.getInstance();
      this.W = new d9(
         FabricLoader.getInstance().getConfigDir().resolve("larp-client/activity.json"), var1x -> var1.execute(() -> mt.B("Activity", var1x, null))
      );
      this.N = this.W.l();
   }

   public static void B() {
      if (F == null) {
         F = new e();
      }
   }

   private static long R() {
      return System.nanoTime() / 1000000L;
   }

   public static void M(Minecraft var0) {
      if (F != null) {
         F.z(var0);
      }
   }

   private void z(Minecraft var1) {
      if (!this.T && this.N.isDone()) {
         long var2 = R();
         if (this.r == null) {
            this.r = new ht((var1x, var2x) -> this.W.<CompletableFuture<JsonObject>>x(() -> {
               PublicApiAccess var2xx = PublicApiAccess.anonymous();
               if (!var2xx.M().equals(var1x)) {
                  throw new pe(401, "UNAUTHENTICATED");
               } else {
                  return cm.R.n.D(var2x == null ? "GET" : "POST", var2x == null ? "/api/activity/preferences" : "/api/activity", var2x, true, null, var2xx);
               }
            }).thenCompose(var0 -> (CompletionStage<JsonObject>)var0), var0 -> mt.U("Activity", var0), this.N.join());
         }

         if (var2 >= this.B) {
            this.B = var2 + 1000L;
            if (this.j != var1.level) {
               this.j = var1.level;
               this.r.S();
            }

            ServerData var4 = var1.getCurrentServer();
            String var5 = var1.player != null && var1.level != null && !var1.isLocalServer() && var4 != null ? var4.ip : null;
            this.r.e("", var5, var5 != null && !var1.isPaused(), var2, System.currentTimeMillis());
            if (var2 >= this.Y || this.u != this.r.d()) {
               this.u = this.r.d();
               this.Y = var2 + 30000L;
               this.W.R(this.r.n());
            }
         }
      }
   }

   public static void k(ClientboundDamageEventPacket var0) {
      e var1 = F;
      Minecraft var2 = Minecraft.getInstance();
      if (var1 != null && var1.r != null && var2.level != null && var2.player != null) {
         if (var1.j != var2.level) {
            var1.j = var2.level;
            var1.r.S();
         }

         if (var2.level.getEntity(var0.entityId()) instanceof Player var4 && var4 != var2.player) {
            var1.r.O(var4.getUUID(), var0.sourceCauseId() == var2.player.getId(), R());
         }
      }
   }

   public static void X(ClientboundEntityEventPacket var0) {
      e var1 = F;
      Minecraft var2 = Minecraft.getInstance();
      if (var1 != null && var1.r != null && var2.level != null && var0.getEventId() == 3) {
         if (var1.j != var2.level) {
            var1.j = var2.level;
            var1.r.S();
         }

         if (var0.getEntity(var2.level) instanceof Player var3 && var3 != var2.player) {
            var1.r.H(var3.getUUID(), R());
         }
      }
   }

   public static void J() {
      e var0 = F;
      if (var0 != null && var0.r != null) {
         var0.r.n();
         var0.W.R(var0.r.n());
         var0.j = null;
      }
   }

   public static void g() {
      e var0 = F;
      if (var0 != null) {
         J();
         var0.T = (boolean)1;
         var0.W.close();
      }
   }
}

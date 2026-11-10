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

public final class vc {
   private static vc f;
   private final nj x;
   private final CompletableFuture<JsonObject> C;
   private dz r;
   private long X;
   private long e;
   private long U = -1L;
   private Object j;
   private boolean Z;

   private vc() {
      Minecraft var1 = Minecraft.getInstance();
      this.x = new nj(
         FabricLoader.getInstance().getConfigDir().resolve("larp-client/activity.json"), var1x -> var1.execute(() -> _q.g("Activity", var1x, null))
      );
      this.C = this.x.K();
   }

   public static void d() {
      if (f == null) {
         f = new vc();
      }
   }

   private static long a() {
      return System.nanoTime() / 1000000L;
   }

   public static void S(Minecraft var0) {
      if (f != null) {
         f.F(var0);
      }
   }

   private void F(Minecraft var1) {
      if (!this.Z && this.C.isDone()) {
         long var2 = a();
         if (this.r == null) {
            this.r = new dz((var1x, var2x) -> this.x.<CompletableFuture<JsonObject>>Z(() -> {
               PublicApiAccess var2xx = PublicApiAccess.anonymous();
               if (!var2xx.B().equals(var1x)) {
                  throw new wr(401, "UNAUTHENTICATED");
               } else {
                  return bl.F.E.b(var2x == null ? "GET" : "POST", var2x == null ? "/api/activity/preferences" : "/api/activity", var2x, true, null, var2xx);
               }
            }).thenCompose(var0 -> (CompletionStage<JsonObject>)var0), var0 -> _q.c("Activity", var0), this.C.join());
         }

         if (var2 >= this.X) {
            this.X = var2 + 1000L;
            if (this.j != var1.level) {
               this.j = var1.level;
               this.r.j();
            }

            ServerData var4 = var1.getCurrentServer();
            String var5 = var1.player != null && var1.level != null && !var1.isLocalServer() && var4 != null ? var4.ip : null;
            this.r.B("", var5, var5 != null && !var1.isPaused(), var2, System.currentTimeMillis());
            if (var2 >= this.e || this.U != this.r.w()) {
               this.U = this.r.w();
               this.e = var2 + 30000L;
               this.x.j(this.r.H());
            }
         }
      }
   }

   public static void K(ClientboundDamageEventPacket var0) {
      vc var1 = f;
      Minecraft var2 = Minecraft.getInstance();
      if (var1 != null && var1.r != null && var2.level != null && var2.player != null) {
         if (var1.j != var2.level) {
            var1.j = var2.level;
            var1.r.j();
         }

         if (var2.level.getEntity(var0.entityId()) instanceof Player var4 && var4 != var2.player) {
            var1.r.S(var4.getUUID(), var0.sourceCauseId() == var2.player.getId(), a());
         }
      }
   }

   public static void l(ClientboundEntityEventPacket var0) {
      vc var1 = f;
      Minecraft var2 = Minecraft.getInstance();
      if (var1 != null && var1.r != null && var2.level != null && var0.getEventId() == 3) {
         if (var1.j != var2.level) {
            var1.j = var2.level;
            var1.r.j();
         }

         if (var0.getEntity(var2.level) instanceof Player var3 && var3 != var2.player) {
            var1.r.a(var3.getUUID(), a());
         }
      }
   }

   public static void i() {
      vc var0 = f;
      if (var0 != null && var0.r != null) {
         var0.r.h();
         var0.x.j(var0.r.H());
         var0.j = null;
      }
   }

   public static void u() {
      vc var0 = f;
      if (var0 != null) {
         i();
         var0.Z = (boolean)1;
         var0.x.close();
      }
   }
}

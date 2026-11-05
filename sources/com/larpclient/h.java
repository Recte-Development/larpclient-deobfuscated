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

public final class h {
   private static h b;
   private final yf q;
   private final CompletableFuture<JsonObject> v;
   private es p;
   private long e;
   private long E;
   private long r = -1L;
   private Object h;
   private boolean W;

   private h() {
      Minecraft var1 = Minecraft.getInstance();
      this.q = new yf(
         FabricLoader.getInstance().getConfigDir().resolve("larp-client/activity.json"), var1x -> var1.execute(() -> r4.T("Activity", var1x, null))
      );
      this.v = this.q.N();
   }

   public static void P() {
      if (b == null) {
         b = new h();
      }
   }

   private static long v() {
      return System.nanoTime() / 1000000L;
   }

   public static void C(Minecraft var0) {
      if (b != null) {
         b.P(var0);
      }
   }

   private void P(Minecraft var1) {
      if (!this.W && this.v.isDone()) {
         long var2 = v();
         if (this.p == null) {
            this.p = new es((var1x, var2x) -> this.q.<CompletableFuture<JsonObject>>Z(() -> {
               PublicApiAccess var2xx = PublicApiAccess.anonymous();
               if (!var2xx.z().equals(var1x)) {
                  throw new oj(401, "UNAUTHENTICATED");
               } else {
                  return hl.E.f.h(var2x == null ? "GET" : "POST", var2x == null ? "/api/activity/preferences" : "/api/activity", var2x, true, null, var2xx);
               }
            }).thenCompose(var0 -> (CompletionStage<JsonObject>)var0), var0 -> r4.g("Activity", var0), this.v.join());
         }

         if (var2 >= this.e) {
            this.e = var2 + 1000L;
            if (this.h != var1.level) {
               this.h = var1.level;
               this.p.k();
            }

            ServerData var4 = var1.getCurrentServer();
            String var5 = var1.player != null && var1.level != null && !var1.isLocalServer() && var4 != null ? var4.ip : null;
            this.p.I("", var5, var5 != null && !var1.isPaused(), var2, System.currentTimeMillis());
            if (var2 >= this.E || this.r != this.p.m()) {
               this.r = this.p.m();
               this.E = var2 + 30000L;
               this.q.x(this.p.I());
            }
         }
      }
   }

   public static void k(ClientboundDamageEventPacket var0) {
      h var1 = b;
      Minecraft var2 = Minecraft.getInstance();
      if (var1 != null && var1.p != null && var2.level != null && var2.player != null) {
         if (var1.h != var2.level) {
            var1.h = var2.level;
            var1.p.k();
         }

         if (var2.level.getEntity(var0.entityId()) instanceof Player var4 && var4 != var2.player) {
            var1.p.w(var4.getUUID(), var0.sourceCauseId() == var2.player.getId(), v());
         }
      }
   }

   public static void u(ClientboundEntityEventPacket var0) {
      h var1 = b;
      Minecraft var2 = Minecraft.getInstance();
      if (var1 != null && var1.p != null && var2.level != null && var0.getEventId() == 3) {
         if (var1.h != var2.level) {
            var1.h = var2.level;
            var1.p.k();
         }

         if (var0.getEntity(var2.level) instanceof Player var3 && var3 != var2.player) {
            var1.p.K(var3.getUUID(), v());
         }
      }
   }

   public static void J() {
      h var0 = b;
      if (var0 != null && var0.p != null) {
         var0.p.i();
         var0.q.x(var0.p.I());
         var0.h = null;
      }
   }

   public static void K() {
      h var0 = b;
      if (var0 != null) {
         J();
         var0.W = (boolean)1;
         var0.q.close();
      }
   }
}

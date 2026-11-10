package com.larpclient.module;

import com.larpclient.cf;
import com.larpclient.ko;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public final class AutoGGModule extends Module {
   static final int S = 5;
   private static AutoGGModule N;
   private final g[] A = this.c();
   private final l n = this.addSetting(new l("Order", "Which message to use when you have several", "Random", "Random", "In Order"));
   private final e H = this.addSetting(new e("Delay", "Wait this long after the kill before sending", 0.5, 0.0, 5.0, 0.1).V("s"));
   private final e L = this.addSetting(new e("Cooldown", "Kills within this time of your last message get no message", 3.0, 1.0, 30.0, 0.5).V("s"));
   private final ko Z = new ko();
   private final r v = new r();
   private int E = -1;

   public AutoGGModule() {
      super("Auto GG", "Send your own chat message or command after you kill a player", Category.PLAYER, -1);
      N = this;
      this.n.P(() -> this.W().size() > 1);
   }

   private g[] c() {
      g[] var1 = new g[5];
      var1[0] = this.addSetting(
         new g("Message", "Sent after a kill. {player} becomes their name. Start with / to run a command. /pay is not allowed.", "gg {player}")
            .F("gg {player}")
            .a(r::M, "Auto GG cannot send /pay")
      );

      for (int var2 = 1; var2 < 5; var2++) {
         g var3 = var1[var2 - 1];
         g var4 = this.addSetting(
            new g("Message " + (var2 + 1), "Another message to mix in. Leave empty to skip.", "").F("Optional").a(r::M, "Auto GG cannot send /pay")
         );
         var4.P(() -> !var3.getValue().isBlank() || !var4.getValue().isBlank());
         var1[var2] = var4;
      }

      return var1;
   }

   public static AutoGGModule i() {
      return N;
   }

   public List<q<?>> o() {
      ArrayList var1 = new ArrayList<>(Arrays.asList(this.A));
      var1.add(this.n);
      return var1;
   }

   public List<q<?>> Q() {
      return List.of(this.H, this.L);
   }

   @Override
   public List<String> getSearchAliases() {
      return List.of("Kill Message", "Kill Say", "Chat");
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   private List<String> W() {
      return Arrays.stream(this.A).map(q::getValue).filter(var0 -> !var0.isBlank()).toList();
   }

   @Override
   public void onEnable() {
      com.larpclient.h.A("AutoGG", "Enabled: {} message(s), server-confirmed kills only", this.W().size());
   }

   @Override
   public void onDisable() {
      this.Z.R();
      this.v.o();
   }

   public void k(ClientboundDamageEventPacket var1) {
      if (this.isEnabled()) {
         Minecraft var2 = getMc();
         if (this.isRuntimeActive() && this.isInGame()) {
            Entity var3 = var2.level.getEntity(var1.entityId());
            if (var3 instanceof Player && var3 != var2.player) {
               this.Z.q(var3, var1.sourceCauseId() == var2.player.getId(), cf.I());
            }
         }
      }
   }

   public void P(ClientboundEntityEventPacket var1) {
      if (this.isEnabled()) {
         Minecraft var2 = getMc();
         if (this.isRuntimeActive() && this.isInGame()) {
            if (var1.getEntity(var2.level) instanceof Player var3 && var3 != var2.player) {
               if (var1.getEventId() == 35) {
                  this.Z.u(var3);
               } else {
                  if (var1.getEventId() == 3 && this.Z.T(var3, cf.I())) {
                     this.X(var3.getGameProfile().name());
                  }
               }
            }
         }
      }
   }

   private void X(String var1) {
      double var2 = cf.I();
      if (!this.v.h(var2)) {
         com.larpclient.h.A("AutoGG", "Killed {}; no message because one is waiting or the cooldown is running", var1);
      } else {
         List var4 = this.W();
         if (var4.isEmpty()) {
            com.larpclient.h.A("AutoGG", "Killed {}; every message is empty", var1);
         } else {
            this.E = r.F(var4.size(), this.E, this.n.O("Random"), ThreadLocalRandom.current()::nextInt);
            r.k var5 = r.p((String)var4.get(this.E), var1);
            if (var5 == null) {
               com.larpclient.h.A("AutoGG", "Killed {}; message {} has nothing it may send after cleaning (empty or /pay)", var1, this.E + 1);
            } else {
               this.v.a(var5, var2 + this.H.getValue());
               com.larpclient.h.A("AutoGG", "Killed {}; {} queued for {}s from now", var1, var5.command() ? "command" : "message", this.H.getValue());
            }
         }
      }
   }

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      double var2 = cf.I();
      this.Z.P(var2, var1x -> var1x instanceof Entity var2x && !var2x.isRemoved() && var2x.level() == var1.level);
      r.k var4 = this.v.i(var2, this.L.getValue());
      if (var4 != null) {
         int var10000 = var4.command();
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            var1.player.connection.sendCommand(var4.text());
         } else {
            var1.player.connection.sendChat(var4.text());
         }

         Object[] var10002 = new Object[2];
         int var10005 = var4.command();
         boolean var6 = false;
         byte var5 = (byte)var10005;
         var10005 = Integer.signum(var5);
         var10002[0] = ((var10005 | -var10005) >>> 31 ^ 1) == 0 ? "command" : "message";
         var10002[1] = var4.text().length();
         com.larpclient.h.A("AutoGG", "Sent {} ({} characters)", var10002);
      }
   }
}

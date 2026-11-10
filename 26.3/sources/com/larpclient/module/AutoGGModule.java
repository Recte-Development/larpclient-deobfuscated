package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.hz;
import com.larpclient.v3;
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
   static final int E = 5;
   private static AutoGGModule O;
   private final e[] h = this.C();
   private final o F = this.addSetting(new o("Order", "Which message to use when you have several", "Random", "Random", "In Order"));
   private final b T = this.addSetting(new b("Delay", "Wait this long after the kill before sending", 0.5, 0.0, 5.0, 0.1).A("s"));
   private final b p = this.addSetting(new b("Cooldown", "Kills within this time of your last message get no message", 3.0, 1.0, 30.0, 0.5).A("s"));
   private final v3 I = new v3();
   private final u d = new u();
   private int A = -1;

   public AutoGGModule() {
      super("Auto GG", "Send your own chat message or command after you kill a player", Category.PLAYER, -1);
      O = this;
      this.F.a(() -> this.L().size() > 1);
   }

   private e[] C() {
      e[] var1 = new e[5];
      var1[0] = this.addSetting(
         new e("Message", "Sent after a kill. {player} becomes their name. Start with / to run a command. /pay is not allowed.", "gg {player}")
            .L("gg {player}")
            .F(u::z, "Auto GG cannot send /pay")
      );

      for (int var2 = 1; var2 < 5; var2++) {
         e var3 = var1[var2 - 1];
         e var4 = this.addSetting(
            new e("Message " + (var2 + 1), "Another message to mix in. Leave empty to skip.", "").L("Optional").F(u::z, "Auto GG cannot send /pay")
         );
         var4.a(() -> !var3.getValue().isBlank() || !var4.getValue().isBlank());
         var1[var2] = var4;
      }

      return var1;
   }

   public static AutoGGModule J() {
      return O;
   }

   public List<r<?>> n() {
      ArrayList var1 = new ArrayList<>(Arrays.asList(this.h));
      var1.add(this.F);
      return var1;
   }

   public List<r<?>> m() {
      return List.of(this.T, this.p);
   }

   @Override
   public List<String> getSearchAliases() {
      return List.of("Kill Message", "Kill Say", "Chat");
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   private List<String> L() {
      return Arrays.stream(this.h).map(r::getValue).filter(var0 -> !var0.isBlank()).toList();
   }

   @Override
   public void onEnable() {
      _q.z("AutoGG", "Enabled: {} message(s), server-confirmed kills only", this.L().size());
   }

   @Override
   public void onDisable() {
      this.I.F();
      this.d.J();
   }

   public void T(ClientboundDamageEventPacket var1) {
      if (this.isEnabled()) {
         Minecraft var2 = getMc();
         if (this.isRuntimeActive() && this.isInGame()) {
            Entity var3 = var2.level.getEntity(var1.entityId());
            if (var3 instanceof Player && var3 != var2.player) {
               this.I.t(var3, var1.sourceCauseId() == var2.player.getId(), hz.S());
            }
         }
      }
   }

   public void e(ClientboundEntityEventPacket var1) {
      if (this.isEnabled()) {
         Minecraft var2 = getMc();
         if (this.isRuntimeActive() && this.isInGame()) {
            if (var1.getEntity(var2.level) instanceof Player var3 && var3 != var2.player) {
               if (var1.getEventId() == 35) {
                  this.I.L(var3);
               } else {
                  if (var1.getEventId() == 3 && this.I.I(var3, hz.S())) {
                     this.O(var3.getGameProfile().name());
                  }
               }
            }
         }
      }
   }

   private void O(String var1) {
      double var2 = hz.S();
      if (!this.d.M(var2)) {
         _q.z("AutoGG", "Killed {}; no message because one is waiting or the cooldown is running", var1);
      } else {
         List var4 = this.L();
         if (var4.isEmpty()) {
            _q.z("AutoGG", "Killed {}; every message is empty", var1);
         } else {
            this.A = u.v(var4.size(), this.A, this.F.w("Random"), ThreadLocalRandom.current()::nextInt);
            u.f var5 = u.i((String)var4.get(this.A), var1);
            if (var5 == null) {
               _q.z("AutoGG", "Killed {}; message {} has nothing it may send after cleaning (empty or /pay)", var1, this.A + 1);
            } else {
               this.d.G(var5, var2 + this.T.getValue());
               _q.z("AutoGG", "Killed {}; {} queued for {}s from now", var1, var5.command() ? "command" : "message", this.T.getValue());
            }
         }
      }
   }

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      double var2 = hz.S();
      this.I.q(var2, var1x -> var1x instanceof Entity var2x && !var2x.isRemoved() && var2x.level() == var1.level);
      u.f var4 = this.d.U(var2, this.p.getValue());
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
         _q.z("AutoGG", "Sent {} ({} characters)", var10002);
      }
   }
}

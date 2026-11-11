package com.larpclient.module.misc;

import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.data.RecentEventTracker;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
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
   static final int maxMessages = 5;
   private static AutoGGModule instance;
   private final TextSetting[] messageSettings = this.createMessageSettings();
   private final ModeSetting order = this.addSetting(new ModeSetting("Order", "Which message to use when you have several", "Random", "Random", "In Order"));
   private final NumberSetting delay = this.addSetting(
      new NumberSetting("Delay", "Wait this long after the kill before sending", 0.5, 0.0, 5.0, 0.1).setSuffix("s")
   );
   private final NumberSetting cooldown = this.addSetting(
      new NumberSetting("Cooldown", "Kills within this time of your last message get no message", 3.0, 1.0, 30.0, 0.5).setSuffix("s")
   );
   private final RecentEventTracker killTracker = new RecentEventTracker();
   private final ChatMessageQueue messageQueue = new ChatMessageQueue();
   private int lastMessageIndex = -1;

   public AutoGGModule() {
      super("Auto GG", "Send your own chat message or command after you kill a player", Category.PLAYER, -1);
      instance = this;
      this.order.visibleWhen(() -> this.getMessages().size() > 1);
   }

   private TextSetting[] createMessageSettings() {
      TextSetting[] var1 = new TextSetting[5];
      var1[0] = this.addSetting(
         new TextSetting("Message", "Sent after a kill. {player} becomes their name. Start with / to run a command. /pay is not allowed.", "gg {player}")
            .setTitle("gg {player}")
            .setRejector(ChatMessageQueue::isPayCommand, "Auto GG cannot send /pay")
      );

      for (int var2 = 1; var2 < 5; var2++) {
         TextSetting var3 = var1[var2 - 1];
         TextSetting var4 = this.addSetting(
            new TextSetting("Message " + (var2 + 1), "Another message to mix in. Leave empty to skip.", "")
               .setTitle("Optional")
               .setRejector(ChatMessageQueue::isPayCommand, "Auto GG cannot send /pay")
         );
         var4.visibleWhen(() -> !var3.getValue().isBlank() || !var4.getValue().isBlank());
         var1[var2] = var4;
      }

      return var1;
   }

   public static AutoGGModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getMessageSettings() {
      ArrayList var1 = new ArrayList<>(Arrays.asList(this.messageSettings));
      var1.add(this.order);
      return var1;
   }

   public List<Setting<?>> getTimingSettings() {
      return List.of(this.delay, this.cooldown);
   }

   @Override
   public List<String> getSearchAliases() {
      return List.of("Kill Message", "Kill Say", "Chat");
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   private List<String> getMessages() {
      return Arrays.stream(this.messageSettings).map(Setting::getValue).filter(var0 -> !var0.isBlank()).toList();
   }

   @Override
   public void onEnable() {
      LarpLog.info("AutoGG", "Enabled: {} message(s), server-confirmed kills only", this.getMessages().size());
   }

   @Override
   public void onDisable() {
      this.killTracker.clear();
      this.messageQueue.clearPending();
   }

   public void onDamageEvent(ClientboundDamageEventPacket var1) {
      if (this.isEnabled()) {
         Minecraft var2 = getMc();
         if (this.isRuntimeActive() && this.isInGame()) {
            Entity var3 = var2.level.getEntity(var1.entityId());
            if (var3 instanceof Player && var3 != var2.player) {
               this.killTracker.markPending(var3, var1.sourceCauseId() == var2.player.getId(), TickTimer.getTickTime());
            }
         }
      }
   }

   public void onEntityEvent(ClientboundEntityEventPacket var1) {
      if (this.isEnabled()) {
         Minecraft var2 = getMc();
         if (this.isRuntimeActive() && this.isInGame()) {
            if (var1.getEntity(var2.level) instanceof Player var3 && var3 != var2.player) {
               if (var1.getEventId() == 35) {
                  this.killTracker.clearPending(var3);
               } else {
                  if (var1.getEventId() == 3 && this.killTracker.consume(var3, TickTimer.getTickTime())) {
                     this.queueMessage(var3.getGameProfile().name());
                  }
               }
            }
         }
      }
   }

   private void queueMessage(String var1) {
      double var2 = TickTimer.getTickTime();
      if (!this.messageQueue.canQueue(var2)) {
         LarpLog.info("AutoGG", "Killed {}; no message because one is waiting or the cooldown is running", var1);
      } else {
         List var4 = this.getMessages();
         if (var4.isEmpty()) {
            LarpLog.info("AutoGG", "Killed {}; every message is empty", var1);
         } else {
            this.lastMessageIndex = ChatMessageQueue.nextIndex(
               var4.size(), this.lastMessageIndex, this.order.isMode("Random"), ThreadLocalRandom.current()::nextInt
            );
            ChatMessageQueue.OutgoingMessage var5 = ChatMessageQueue.prepareMessage((String)var4.get(this.lastMessageIndex), var1);
            if (var5 == null) {
               LarpLog.info("AutoGG", "Killed {}; message {} has nothing it may send after cleaning (empty or /pay)", var1, this.lastMessageIndex + 1);
            } else {
               this.messageQueue.queue(var5, var2 + this.delay.getValue());
               LarpLog.info("AutoGG", "Killed {}; {} queued for {}s from now", var1, var5.command() ? "command" : "message", this.delay.getValue());
            }
         }
      }
   }

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      double var2 = TickTimer.getTickTime();
      this.killTracker.expire(var2, var1x -> var1x instanceof Entity var2x && !var2x.isRemoved() && var2x.level() == var1.level);
      ChatMessageQueue.OutgoingMessage var4 = this.messageQueue.pollDue(var2, this.cooldown.getValue());
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
         LarpLog.info("AutoGG", "Sent {} ({} characters)", var10002);
      }
   }
}

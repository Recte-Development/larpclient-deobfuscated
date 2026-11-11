package com.larpclient.util.misc;

import com.larpclient.social.misc.PlayerMessages;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

public final class PayNotice {
   private static final long durationMs = 5000L;
   private static final long initialSuppressMs = 2000L;
   private static final long resendIntervalMs = 1500L;
   private static long expiresAt;
   private static long suppressUntil;
   private static long forcedSuppressUntil;
   private static long resendStartAt;
   private static long lastSentAt;
   private static Component message;

   private PayNotice() {
   }

   public static void show(long var0) {
      message = Component.literal("$ ")
         .withStyle(ChatFormatting.GREEN)
         .append(Component.literal(" " + AuctionUtil.formatCompact(var0)).withStyle(ChatFormatting.WHITE));
      long var2 = System.currentTimeMillis();
      long var4 = DelayedTaskScheduler.randomDelay();
      suppressUntil = var2 + 2000L;
      expiresAt = var2 + 5000L + var4;
      resendStartAt = var2 + var4;
      lastSentAt = 0L;
      DelayedTaskScheduler.schedule(var4, () -> {
         if (message != null && System.currentTimeMillis() < expiresAt) {
            resendMessage();
            LocalPlayer var0x = Minecraft.getInstance().player;
            if (var0x != null) {
               PlayerMessages.sendMessage(message, false);
            }
         }
      });
   }

   public static void clear() {
      message = null;
      forcedSuppressUntil = 0L;
      suppressUntil = 0L;
      expiresAt = 0L;
   }

   public static void tick() {
      if (message == null || System.currentTimeMillis() >= expiresAt) {
         message = null;
      } else if (System.currentTimeMillis() >= resendStartAt) {
         if (System.currentTimeMillis() - lastSentAt >= 1500L) {
            resendMessage();
         }
      }
   }

   public static void suppressFor(long var0) {
      forcedSuppressUntil = System.currentTimeMillis() + var0;
   }

   public static boolean shouldSuppress(Component var0, boolean var1) {
      return message != null && System.currentTimeMillis() < suppressUntil || System.currentTimeMillis() < forcedSuppressUntil;
   }

   private static void resendMessage() {
      LocalPlayer var0 = Minecraft.getInstance().player;
      if (var0 != null && message != null) {
         PlayerMessages.sendMessage(message, true);
         lastSentAt = System.currentTimeMillis();
      }
   }
}

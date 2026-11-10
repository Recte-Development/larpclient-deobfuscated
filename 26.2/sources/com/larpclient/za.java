package com.larpclient;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

public final class za {
   private static final long M = 5000L;
   private static final long X = 2000L;
   private static final long R = 1500L;
   private static long v;
   private static long w;
   private static long C;
   private static long g;
   private static long N;
   private static Component j;

   private za() {
   }

   public static void j(long var0) {
      j = Component.literal("$ ").withStyle(ChatFormatting.GREEN).append(Component.literal(" " + px.z(var0)).withStyle(ChatFormatting.WHITE));
      long var2 = System.currentTimeMillis();
      long var4 = t4.B();
      w = var2 + 2000L;
      v = var2 + 5000L + var4;
      g = var2 + var4;
      N = 0L;
      t4.C(var4, () -> {
         if (j != null && System.currentTimeMillis() < v) {
            K();
            LocalPlayer var0x = Minecraft.getInstance().player;
            if (var0x != null) {
               k4.f(j, false);
            }
         }
      });
   }

   public static void M() {
      j = null;
      C = 0L;
      w = 0L;
      v = 0L;
   }

   public static void n() {
      if (j == null || System.currentTimeMillis() >= v) {
         j = null;
      } else if (System.currentTimeMillis() >= g) {
         if (System.currentTimeMillis() - N >= 1500L) {
            K();
         }
      }
   }

   public static void L(long var0) {
      C = System.currentTimeMillis() + var0;
   }

   public static boolean O(Component var0, boolean var1) {
      return j != null && System.currentTimeMillis() < w || System.currentTimeMillis() < C;
   }

   private static void K() {
      LocalPlayer var0 = Minecraft.getInstance().player;
      if (var0 != null && j != null) {
         k4.f(j, true);
         N = System.currentTimeMillis();
      }
   }
}

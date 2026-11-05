package com.larpclient;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

public final class k9 {
   private static final long D = 5000L;
   private static final long i = 2000L;
   private static final long J = 1500L;
   private static long Y;
   private static long w;
   private static long U;
   private static long o;
   private static long p;
   private static Component d;

   private k9() {
   }

   public static void S(long var0) {
      d = Component.literal("$ ").withStyle(ChatFormatting.GREEN).append(Component.literal(" " + d1.h(var0)).withStyle(ChatFormatting.WHITE));
      long var2 = System.currentTimeMillis();
      long var4 = t1.E();
      w = var2 + 2000L;
      Y = var2 + 5000L + var4;
      o = var2 + var4;
      p = 0L;
      t1.O(var4, () -> {
         if (d != null && System.currentTimeMillis() < Y) {
            Z();
            LocalPlayer var0x = Minecraft.getInstance().player;
            if (var0x != null) {
               ws.c(d, false);
            }
         }
      });
   }

   public static void F() {
      d = null;
      U = 0L;
      w = 0L;
      Y = 0L;
   }

   public static void c() {
      if (d == null || System.currentTimeMillis() >= Y) {
         d = null;
      } else if (System.currentTimeMillis() >= o) {
         if (System.currentTimeMillis() - p >= 1500L) {
            Z();
         }
      }
   }

   public static void z(long var0) {
      U = System.currentTimeMillis() + var0;
   }

   public static boolean m(Component var0, boolean var1) {
      return d != null && System.currentTimeMillis() < w || System.currentTimeMillis() < U;
   }

   private static void Z() {
      LocalPlayer var0 = Minecraft.getInstance().player;
      if (var0 != null && d != null) {
         ws.c(d, true);
         p = System.currentTimeMillis();
      }
   }
}

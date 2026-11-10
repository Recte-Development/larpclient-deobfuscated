package com.larpclient;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

public final class h4 {
   private static final long H = 5000L;
   private static final long z = 2000L;
   private static final long A = 1500L;
   private static long o;
   private static long v;
   private static long L;
   private static long d;
   private static long r;
   private static Component n;

   private h4() {
   }

   public static void C(long var0) {
      n = Component.literal("$ ").withStyle(ChatFormatting.GREEN).append(Component.literal(" " + nb.b(var0)).withStyle(ChatFormatting.WHITE));
      long var2 = System.currentTimeMillis();
      long var4 = qw.a();
      v = var2 + 2000L;
      o = var2 + 5000L + var4;
      d = var2 + var4;
      r = 0L;
      qw.K(var4, () -> {
         if (n != null && System.currentTimeMillis() < o) {
            j();
            LocalPlayer var0x = Minecraft.getInstance().player;
            if (var0x != null) {
               iw.V(n, false);
            }
         }
      });
   }

   public static void v() {
      n = null;
      L = 0L;
      v = 0L;
      o = 0L;
   }

   public static void L() {
      if (n == null || System.currentTimeMillis() >= o) {
         n = null;
      } else if (System.currentTimeMillis() >= d) {
         if (System.currentTimeMillis() - r >= 1500L) {
            j();
         }
      }
   }

   public static void x(long var0) {
      L = System.currentTimeMillis() + var0;
   }

   public static boolean M(Component var0, boolean var1) {
      return n != null && System.currentTimeMillis() < v || System.currentTimeMillis() < L;
   }

   private static void j() {
      LocalPlayer var0 = Minecraft.getInstance().player;
      if (var0 != null && n != null) {
         iw.V(n, true);
         r = System.currentTimeMillis();
      }
   }
}

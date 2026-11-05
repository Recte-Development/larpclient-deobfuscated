package com.larpclient;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

public final class ul {
   private static final long S = 5000L;
   private static final long y = 2000L;
   private static final long m = 1500L;
   private static long b;
   private static long i;
   private static long Z;
   private static long C;
   private static long O;
   private static Component A;

   private ul() {
   }

   public static void p(long var0) {
      A = Component.literal("$ ").withStyle(ChatFormatting.GREEN).append(Component.literal(" " + hr.l(var0)).withStyle(ChatFormatting.WHITE));
      long var2 = System.currentTimeMillis();
      long var4 = ff.J();
      i = var2 + 2000L;
      b = var2 + 5000L + var4;
      C = var2 + var4;
      O = 0L;
      ff.r(var4, () -> {
         if (A != null && System.currentTimeMillis() < b) {
            h();
            LocalPlayer var0x = Minecraft.getInstance().player;
            if (var0x != null) {
               jb.v(A, false);
            }
         }
      });
   }

   public static void f() {
      A = null;
      Z = 0L;
      i = 0L;
      b = 0L;
   }

   public static void K() {
      if (A == null || System.currentTimeMillis() >= b) {
         A = null;
      } else if (System.currentTimeMillis() >= C) {
         if (System.currentTimeMillis() - O >= 1500L) {
            h();
         }
      }
   }

   public static void J(long var0) {
      Z = System.currentTimeMillis() + var0;
   }

   public static boolean C(Component var0, boolean var1) {
      return A != null && System.currentTimeMillis() < i || System.currentTimeMillis() < Z;
   }

   private static void h() {
      LocalPlayer var0 = Minecraft.getInstance().player;
      if (var0 != null && A != null) {
         jb.v(A, true);
         O = System.currentTimeMillis();
      }
   }
}

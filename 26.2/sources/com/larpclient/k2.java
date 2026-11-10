package com.larpclient;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;

public final class k2 {
   public static final int B = -1;
   public static final int W = 32;
   public static final int S = 66;
   public static final int c = 67;
   public static final int h = 71;
   public static final int E = 344;
   public static final int f = 348;
   public static final int Z = 0;
   public static final int s = 1;
   public static final int t = 2;

   public static int J(int var0) {
      return var0;
   }

   public static int M(int var0) {
      return var0;
   }

   public static int I(int var0) {
      return var0;
   }

   public static int f(int var0) {
      return var0;
   }

   public static int a(KeyEvent var0) {
      return J(var0.key());
   }

   public static int w(MouseButtonEvent var0) {
      return I(var0.button());
   }

   public static int F(MouseButtonInfo var0) {
      return I(var0.button());
   }

   public static boolean w(Window var0, int var1) {
      return InputConstants.isKeyDown(var0, var1);
   }

   private k2() {
   }
}

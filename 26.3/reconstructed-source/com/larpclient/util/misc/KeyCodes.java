package com.larpclient.util.misc;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import java.util.Arrays;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;

public final class KeyCodes {
   public static final int kEY_NONE = -1;
   public static final int kEY_SPACE = 32;
   public static final int kEY_B = 66;
   public static final int kEY_C = 67;
   public static final int kEY_G = 71;
   public static final int kEY_RIGHT_SHIFT = 344;
   public static final int kEY_MENU = 348;
   public static final int mOUSE_LEFT = 0;
   public static final int mOUSE_RIGHT = 1;
   public static final int mOUSE_MIDDLE = 2;
   private static final int[] kEY_SCANCODE_PAIRS = new int[]{
      32,
      44,
      39,
      52,
      44,
      54,
      45,
      45,
      46,
      55,
      47,
      56,
      48,
      39,
      49,
      30,
      50,
      31,
      51,
      32,
      52,
      33,
      53,
      34,
      54,
      35,
      55,
      36,
      56,
      37,
      57,
      38,
      59,
      51,
      61,
      46,
      65,
      4,
      66,
      5,
      67,
      6,
      68,
      7,
      69,
      8,
      70,
      9,
      71,
      10,
      72,
      11,
      73,
      12,
      74,
      13,
      75,
      14,
      76,
      15,
      77,
      16,
      78,
      17,
      79,
      18,
      80,
      19,
      81,
      20,
      82,
      21,
      83,
      22,
      84,
      23,
      85,
      24,
      86,
      25,
      87,
      26,
      88,
      27,
      89,
      28,
      90,
      29,
      91,
      47,
      92,
      49,
      93,
      48,
      96,
      53,
      256,
      41,
      257,
      40,
      258,
      43,
      259,
      42,
      260,
      73,
      261,
      76,
      262,
      79,
      263,
      80,
      264,
      81,
      265,
      82,
      266,
      75,
      267,
      78,
      268,
      74,
      269,
      77,
      280,
      57,
      281,
      71,
      282,
      83,
      283,
      70,
      284,
      72,
      290,
      58,
      291,
      59,
      292,
      60,
      293,
      61,
      294,
      62,
      295,
      63,
      296,
      64,
      297,
      65,
      298,
      66,
      299,
      67,
      300,
      68,
      301,
      69,
      302,
      104,
      303,
      105,
      304,
      106,
      305,
      107,
      306,
      108,
      307,
      109,
      308,
      110,
      309,
      111,
      310,
      112,
      311,
      113,
      312,
      114,
      313,
      115,
      320,
      98,
      321,
      89,
      322,
      90,
      323,
      91,
      324,
      92,
      325,
      93,
      326,
      94,
      327,
      95,
      328,
      96,
      329,
      97,
      330,
      220,
      332,
      85,
      334,
      87,
      335,
      88,
      336,
      103,
      340,
      225,
      341,
      224,
      342,
      226,
      343,
      227,
      344,
      229,
      345,
      228,
      346,
      230,
      347,
      231
   };
   private static final int[] glfwToScancode = new int[349];
   private static final int[] scancodeToGlfw = new int[512];

   public static int fromScancode(int var0) {
      return var0 >= 0 && var0 < scancodeToGlfw.length ? scancodeToGlfw[var0] : -1;
   }

   public static int toScancode(int var0) {
      return var0 >= 0 && var0 <= 348 ? glfwToScancode[var0] : 0;
   }

   public static int fromMinecraftButton(int var0) {
      return switch (var0) {
         case 1 -> 0;
         case 2 -> 2;
         case 3 -> 1;
         default -> var0 >= 4 ? var0 - 1 : -1;
      };
   }

   public static int toMinecraftButton(int var0) {
      return switch (var0) {
         case 0 -> 1;
         case 1 -> 3;
         case 2 -> 2;
         default -> var0 >= 3 ? var0 + 1 : 0;
      };
   }

   public static int keyOf(KeyEvent var0) {
      return fromScancode(var0.key());
   }

   public static int mouseButtonOf(MouseButtonEvent var0) {
      return fromMinecraftButton(var0.button());
   }

   public static int mouseButtonOfInfo(MouseButtonInfo var0) {
      return fromMinecraftButton(var0.button());
   }

   public static boolean isKeyDown(Window var0, int var1) {
      int var2 = toScancode(var1);
      return var2 > 0 && InputConstants.isKeyDown(var2);
   }

   private KeyCodes() {
   }

   static {
      Arrays.fill(glfwToScancode, 0);
      Arrays.fill(scancodeToGlfw, -1);

      for (byte var0 = 0; var0 < kEY_SCANCODE_PAIRS.length; var0 += 2) {
         glfwToScancode[kEY_SCANCODE_PAIRS[var0]] = kEY_SCANCODE_PAIRS[var0 + 1];
         scancodeToGlfw[kEY_SCANCODE_PAIRS[var0 + 1]] = kEY_SCANCODE_PAIRS[var0];
      }
   }
}

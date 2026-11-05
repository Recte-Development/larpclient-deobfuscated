package com.larpclient;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import java.util.Arrays;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;

public final class mo {
   public static final int z = -1;
   public static final int H = 32;
   public static final int b = 66;
   public static final int v = 67;
   public static final int g = 71;
   public static final int e = 344;
   public static final int t = 348;
   public static final int Q = 0;
   public static final int p = 1;
   public static final int j = 2;
   private static final int[] i = new int[]{
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
   private static final int[] V = new int[349];
   private static final int[] w = new int[512];

   public static int X(int var0) {
      return var0 >= 0 && var0 < w.length ? w[var0] : -1;
   }

   public static int f(int var0) {
      return var0 >= 0 && var0 <= 348 ? V[var0] : 0;
   }

   public static int j(int var0) {
      return switch (var0) {
         case 1 -> 0;
         case 2 -> 2;
         case 3 -> 1;
         default -> var0 >= 4 ? var0 - 1 : -1;
      };
   }

   public static int A(int var0) {
      return switch (var0) {
         case 0 -> 1;
         case 1 -> 3;
         case 2 -> 2;
         default -> var0 >= 3 ? var0 + 1 : 0;
      };
   }

   public static int O(KeyEvent var0) {
      return X(var0.key());
   }

   public static int Z(MouseButtonEvent var0) {
      return j(var0.button());
   }

   public static int i(MouseButtonInfo var0) {
      return j(var0.button());
   }

   public static boolean N(Window var0, int var1) {
      int var2 = f(var1);
      return var2 > 0 && InputConstants.isKeyDown(var2);
   }

   private mo() {
   }

   static {
      Arrays.fill(V, 0);
      Arrays.fill(w, -1);

      for (byte var0 = 0; var0 < i.length; var0 += 2) {
         V[i[var0]] = i[var0 + 1];
         w[i[var0 + 1]] = i[var0];
      }
   }
}

package com.larpclient;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_10799;
import net.minecraft.class_156;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_2682;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3620;

public final class ln extends lc {
   static final int W = 2000000;
   static final int C = 104;
   static final int m = 6;
   static final int c = 14;
   static final int X = 46;
   private static final int a = -6643287;
   private static final int L = -2630430;
   static final int j = -4598820;
   private static final class_2960 p = class_2960.method_60655("larp-client", "auto_builder/schematic_preview");
   private static int V;
   private static Object h;
   private static Object T;
   private static ln.s S;
   private static CompletableFuture<ln.s> A;
   private static boolean k;
   private final s0 Y;

   public ln(s0 var1) {
      this.Y = var1;
      this.w = 46;
   }

   @Override
   public void bounds(int var1, int var2, int var3) {
      super.bounds(var1, var2, var3);
      this.w = this.Y.C() == null ? 46 : 130;
   }

   @Override
   public void render(class_332 var1, int var2, int var3) {
      n var4 = this.Y.C();
      if (var4 == null) {
         J();
         int var15 = uw.c.o.b();
         uw.X(var1, uw.X(this.Y.k(), this.d - 12, uw.c.o), this.D + 6 + 2, this.g + 6 + 2, -2630430, uw.c.o);
         uw.X(var1, uw.X("Choose a file in the list above", this.d - 12, uw.c.M), this.D + 6 + 2, this.g + 6 + 6 + var15, -6643287, uw.c.M);
      } else {
         int var5 = Math.max(1, this.d - 12);
         double var6 = Math.max(1, class_310.method_1551().method_22683().method_4495());
         ln.r var8 = new ln.r(var4, V, (int)Math.round(var5 * var6), (int)Math.round(104.0 * var6));
         j(var8, var4);
         String var9 = var4.D() + ", " + var4.r() + " x " + var4.C() + " x " + var4.X() + ", " + var4.I() + " blocks";
         if (S != null && h instanceof ln.r var10 && var10.schematic() == var4) {
            int var17 = (int)Math.round(S.width() / var6);
            int var12 = (int)Math.round(S.height() / var6);
            int var13 = this.D + 6 + (var5 - var17) / 2;
            int var14 = this.g + 6 + (104 - var12) / 2;
            var1.method_25302(class_10799.field_56883, p, var13, var14, 0.0F, 0.0F, var17, var12, S.width(), S.height(), S.width(), S.height());
            if (S.group() > 1) {
               var9 = var9 + ", 1 cube is " + S.group() + " blocks";
            }
         } else {
            uw.X(var1, "Drawing the preview", this.D + 6 + 2, this.g + 6 + 52 - 4, -6643287, uw.c.M);
         }

         int var16 = this.g + 6 + 104 + 3;
         String var18 = "Click to turn";
         int var19 = uw.C(var18, uw.c.M);
         uw.X(var1, uw.X(var9, Math.max(1, this.d - 18 - var19), uw.c.M), this.D + 6 + 2, var16, -2630430, uw.c.M);
         uw.X(var1, var18, this.D + this.d - 6 - var19, var16, -6643287, uw.c.M);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.Y.C() != null && this.contains(var1, var3)) {
         V = (V + 1) % 4;
         c_.b(c_.d.q);
         return true;
      } else {
         return false;
      }
   }

   private static void j(ln.r var0, n var1) {
      if (A != null && A.isDone()) {
         ln.s var2 = null;

         try {
            var2 = A.join();
         } catch (RuntimeException var4) {
            q8.L("Auto Builder", "Schematic preview failed: {}", var4.toString());
         }

         if (var2 != null && var2.width() > 0 && var2.height() > 0) {
            F(var2, T);
         } else {
            S = null;
            h = T;
         }

         A = null;
      }

      if (!var0.equals(h) && (A == null || !var0.equals(T))) {
         if (A == null) {
            int[] var5 = l(var1);
            T = var0;
            A = CompletableFuture.supplyAsync(() -> n(var1, var5, var0.view(), var0.width(), var0.height()), class_156.method_18349());
         }
      }
   }

   private static void F(ln.s var0, Object var1) {
      class_1011 var2 = new class_1011(var0.width(), var0.height(), false);

      for (int var3 = 0; var3 < var0.height(); var3++) {
         for (int var4 = 0; var4 < var0.width(); var4++) {
            var2.method_61941(var4, var3, var0.pixels()[var3 * var0.width() + var4]);
         }
      }

      class_310.method_1551().method_1531().method_4616(p, new class_1043(p::toString, var2));
      k = (boolean)1;
      S = var0;
      h = var1;
   }

   private static void J() {
      if (A != null) {
         A.cancel(false);
         A = null;
         T = null;
      }

      if (k) {
         class_310.method_1551().method_1531().method_4615(p);
         k = (boolean)0;
         S = null;
         h = null;
      }
   }

   private static int[] l(n var0) {
      List var1 = var0.a();
      int[] var2 = new int[var1.size()];

      for (int var3 = 0; var3 < var2.length; var3++) {
         try {
            class_2680 var4 = uo.B((String)var1.get(var3));
            if (var4 != null && !var4.method_26215()) {
               class_3620 var5 = var4.method_26205(class_2682.field_12294, class_2338.field_10980);
               var2[var3] = var5 == class_3620.field_16008 ? -4598820 : 0xFF000000 | var5.field_16011;
            }
         } catch (RuntimeException var6) {
            var2[var3] = -8355712;
         }
      }

      return var2;
   }

   public static ln.s n(n var0, int[] var1, int var2, int var3, int var4) {
      if (var0.I() != 0 && var3 >= 4 && var4 >= 4) {
         int var5 = (var2 & 1) == 1 ? 1 : 0;
         int var6 = var0.r();
         int var7 = var0.C();
         int var8 = var0.X();
         int var9 = var5 != 0 ? var8 : var6;
         int var10 = var5 != 0 ? var6 : var8;
         int var11 = 1;

         while (true) {
            long var12 = f(var9, var11);
            long var14 = f(var7, var11);
            long var16 = f(var10, var11);
            if ((var12 + var16) * 2L <= var3 && var12 + var16 + var14 * 2L <= var4 && var12 * var14 * var16 <= 2000000L) {
               int var37 = f(var9, var11);
               int var13 = f(var7, var11);
               int var38 = f(var10, var11);
               int var15 = 1;

               while (var15 < 48 && (var37 + var38) * (var15 + 1) * 2 <= var3 && (var37 + var38) * (var15 + 1) + var13 * (var15 + 1) * 2 <= var4) {
                  var15++;
               }

               int[] var39 = new int[var37 * var13 * var38];

               for (int var17 = 0; var17 < var0.I(); var17++) {
                  int var18 = var1[var0.D(var17)];
                  if (var18 != 0) {
                     int var19 = var0.s(var17) - var0.i();
                     int var20 = var0.r(var17) - var0.p();
                     int var21 = var0.F(var17) - var0.Q();
                     int var22;
                     int var23;
                     switch (var2 & 3) {
                        case 1:
                           var22 = var8 - 1 - var21;
                           var23 = var19;
                           break;
                        case 2:
                           var22 = var6 - 1 - var19;
                           var23 = var8 - 1 - var21;
                           break;
                        case 3:
                           var22 = var21;
                           var23 = var6 - 1 - var19;
                           break;
                        default:
                           var22 = var19;
                           var23 = var21;
                     }

                     var39[(var20 / var11 * var38 + var23 / var11) * var37 + var22 / var11] = var18;
                  }
               }

               int var40 = (var37 + var38) * var15 * 2;
               int var41 = (var37 + var38) * var15 + var13 * var15 * 2;
               int[] var42 = new int[var40 * var41];
               int[] var43 = new int[var42.length];
               Arrays.fill(var43, -1);
               byte[] var44 = s(var15);
               int var45 = var15 * 4;

               for (int var46 = 0; var46 < var13; var46++) {
                  for (int var24 = 0; var24 < var38; var24++) {
                     for (int var25 = 0; var25 < var37; var25++) {
                        int var26 = var39[(var46 * var38 + var24) * var37 + var25];
                        if (var26 != 0
                           && (
                              var46 + 1 >= var13
                                 || var39[((var46 + 1) * var38 + var24) * var37 + var25] == 0
                                 || var25 + 1 >= var37
                                 || var39[(var46 * var38 + var24) * var37 + var25 + 1] == 0
                                 || var24 + 1 >= var38
                                 || var39[(var46 * var38 + var24 + 1) * var37 + var25] == 0
                           )) {
                           int[] var27 = new int[]{var26, H(var26, 80), H(var26, 64), H(var26, 88), H(var26, 71), H(var26, 57)};
                           int var28 = (var25 - var24 + var38 - 1) * 2 * var15;
                           int var29 = (var25 + var24) * var15 + (var13 - 1 - var46) * 2 * var15;
                           int var30 = var25 + var46 + var24;

                           for (int var31 = 0; var31 < var45; var31++) {
                              for (int var32 = 0; var32 < var45; var32++) {
                                 byte var33 = var44[var31 * var45 + var32];
                                 if (var33 != 0) {
                                    int var34 = var28 + var32;
                                    int var35 = var29 + var31;
                                    if (var34 >= 0 && var35 >= 0 && var34 < var40 && var35 < var41) {
                                       int var36 = var35 * var40 + var34;
                                       if (var30 >= var43[var36]) {
                                          var43[var36] = var30;
                                          var42[var36] = var27[var33 - 1];
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               return new ln.s(var40, var41, var42, var11);
            }

            var11++;
         }
      } else {
         return new ln.s(0, 0, new int[0], 1);
      }
   }

   static byte[] s(int var0) {
      int var1 = var0 * 4;
      byte[] var2 = new byte[var1 * var1];

      for (int var3 = 0; var3 < var1; var3++) {
         for (int var4 = 0; var4 < var1; var4++) {
            double var5 = var4 + 0.5;
            double var7 = var3 + 0.5;
            byte var9 = 0;
            double var11 = Math.abs(var5 - 2 * var0) / (2 * var0) + Math.abs(var7 - var0) / var0;
            int var10;
            if (var11 <= 1.0) {
               var9 = 1;
               var10 = (1.0 - var11) * var0 < 0.9 ? 1 : 0;
            } else if (var5 < 2 * var0 && var7 >= var0 + var5 / 2.0 && var7 <= 3 * var0 + var5 / 2.0) {
               var9 = 2;
               var10 = !(var5 < 1.0) && !(var7 - (var0 + var5 / 2.0) < 1.0) && !(3 * var0 + var5 / 2.0 - var7 < 1.0) ? 0 : 1;
            } else if (var5 >= 2 * var0 && var7 >= 2 * var0 - (var5 - 2 * var0) / 2.0 && var7 <= 4 * var0 - (var5 - 2 * var0) / 2.0) {
               var9 = 3;
               var10 = !(var5 > var1 - 1) && !(var7 - (2 * var0 - (var5 - 2 * var0) / 2.0) < 1.0) && !(4 * var0 - (var5 - 2 * var0) / 2.0 - var7 < 1.0) ? 0 : 1;
            } else {
               var10 = 0;
            }

            if (var9 != 0 && var10 != 0 && var0 >= 2) {
               var9 = (byte)(var9 + 3);
            }

            var2[var3 * var1 + var4] = var9;
         }
      }

      return var2;
   }

   static int H(int var0, int var1) {
      int var2 = (var0 >> 16 & 0xFF) * var1 / 100;
      int var3 = (var0 >> 8 & 0xFF) * var1 / 100;
      int var4 = (var0 & 0xFF) * var1 / 100;
      return var0 & 0xFF000000 | var2 << 16 | var3 << 8 | var4;
   }

   private static int f(int var0, int var1) {
      return (var0 + var1 - 1) / var1;
   }

   private record r(n schematic, int view, int width, int height) {
   }

   public record s(int width, int height, int[] pixels, int group) {
   }
}

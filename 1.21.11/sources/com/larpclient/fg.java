package com.larpclient;

import com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.qad41357d687ba689.q2ee3efb11fe52f83;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.class_10055;
import net.minecraft.class_1306;
import net.minecraft.class_2350;
import net.minecraft.class_4587;
import net.minecraft.class_5603;
import net.minecraft.class_5605;
import net.minecraft.class_5606;
import net.minecraft.class_5607;
import net.minecraft.class_5609;
import net.minecraft.class_5610;
import net.minecraft.class_591;
import net.minecraft.class_630;
import net.minecraft.class_7833;
import net.minecraft.class_630.class_593;
import net.minecraft.class_630.class_618;
import net.minecraft.class_630.class_628;

public final class fg {
   private static final float U = 4.0F;
   private static final float V = 6.0F;
   private final class_630[] T;
   private final class_630[] Y;
   private final class_630[] S;
   private final class_630[] M;
   private final boolean[] X = new boolean[8];
   private final boolean[] h = new boolean[8];
   private boolean j;

   public fg(class_591 var1) {
      this.T = new class_630[]{
         var1.field_3401, var1.field_27433, var1.field_3392, var1.field_3397, var1.field_3486, var1.field_3484, var1.field_3479, var1.field_3482
      };
      this.Y = new class_630[4];
      this.S = new class_630[4];
      this.M = new class_630[8];

      for (int var2 = 0; var2 < 4; var2++) {
         float var3 = var2 < 2 ? 4.0F : 6.0F;
         fg.b var4 = fg.b.of(this.T[var2]);
         if (var4 != null) {
            class_630 var5 = this.T[var2 + 4];
            fg.b var6 = var5.method_41921().equals(class_5603.field_27701) ? fg.b.of(var5) : null;
            LinkedHashMap var7 = new LinkedHashMap();
            LinkedHashMap var8 = new LinkedHashMap();
            if (var6 != null) {
               this.M[var2] = new class_630(List.of(var6.upper(var3)), Map.of());
               this.M[var2 + 4] = new class_630(List.of(var6.lower(var3)), Map.of());
               var8.put("layer", this.M[var2 + 4]);
               this.h[var2 + 4] = (boolean)1;
            }

            this.S[var2] = new class_630(List.of(var4.lower(var3)), var8);
            this.S[var2].method_41918(class_5603.method_32090(0.0F, var3, 0.0F));
            this.S[var2].method_41923();
            var7.put("joint", this.S[var2]);
            if (this.M[var2] != null) {
               var7.put("layer", this.M[var2]);
            }

            class_630 var9 = new class_630(List.of(var4.upper(var3)), var7);
            var9.field_3665 = (boolean)0;
            this.Y[var2] = var9;
            this.h[var2] = (boolean)1;
            ((q2ee3efb11fe52f83)this.T[var2]).larpWalkChildren().put("larp_walk_joint", var9);
         }
      }
   }

   public static class_630 F(int var0, boolean var1) {
      int var2 = var0 < 2 ? 1 : 0;
      int var3 = (var0 & 1) == 1 ? 1 : 0;
      int var4 = var2 != 0 && var1 ? 3 : 4;
      float var5 = var2 != 0 ? (var3 != 0 ? -1.0F : (var1 ? -2.0F : -3.0F)) : -2.0F;
      float var6 = var2 != 0 ? -2.0F : 0.0F;
      float var7 = var2 != 0 ? 4.0F : 6.0F;
      int var8 = var2 != 0 ? (var3 != 0 ? 32 : 40) : (var3 != 0 ? 16 : 0);
      int var9 = var3 != 0 ? 48 : 16;
      int var10 = var2 != 0 ? (var3 != 0 ? 48 : 40) : (var3 != 0 ? 0 : 0);
      int var11 = var3 != 0 ? 48 : 32;
      class_5609 var12 = new class_5609();
      class_5610 var13 = var12.method_32111()
         .method_32117("upper", class_5606.method_32108().method_32101(var8, var9).method_32097(var5, var6, -2.0F, var4, 6.0F, 4.0F), class_5603.field_27701);
      var13.method_32117(
         "layer",
         class_5606.method_32108().method_32101(var10, var11).method_32098(var5, var6, -2.0F, var4, 6.0F, 4.0F, new class_5605(0.25F)),
         class_5603.field_27701
      );
      class_5610 var14 = var13.method_32117(
         "joint",
         class_5606.method_32108().method_32101(var8, var9 + 5).method_32097(var5, -1.0F, -2.0F, var4, 7.0F, 4.0F),
         class_5603.method_32090(0.0F, var7, 0.0F)
      );
      var14.method_32117(
         "layer",
         class_5606.method_32108().method_32101(var10, var11 + 5).method_32098(var5, -1.0F, -2.0F, var4, 7.0F, 4.0F, new class_5605(0.25F)),
         class_5603.field_27701
      );
      return class_5607.method_32110(var12, 64, 64).method_32109().method_32086("upper");
   }

   public static class_628 X(List<class_628> var0) {
      fg.b var1 = fg.b.of(var0);
      return var1 == null ? null : var1.whole();
   }

   public void r() {
      if (this.j) {
         for (int var1 = 0; var1 < 8; var1++) {
            if (this.h[var1]) {
               this.T[var1].field_38456 = this.X[var1];
            }
         }
      }

      for (class_630 var4 : this.Y) {
         if (var4 != null) {
            var4.field_3665 = (boolean)0;
         }
      }

      this.j = (boolean)0;
   }

   public void C(class_1306 var1, class_4587 var2) {
      class_630 var3 = this.S[var1 == class_1306.field_6183 ? 0 : 1];
      if (this.j && var3 != null) {
         var2.method_22904(0.0, 0.25, 0.0);
         em.W(var2, class_7833.field_40714.rotation(var3.field_3654));
         var2.method_22904(0.0, -0.25, 0.0);
      }
   }

   public void E(float[] var1, class_10055 var2) {
      if (var1 != null) {
         for (int var3 = 0; var3 < 8; var3++) {
            if (this.h[var3]) {
               this.X[var3] = this.T[var3].field_38456;
               this.T[var3].field_38456 = (boolean)1;
            }
         }

         for (int var4 = 0; var4 < 4; var4++) {
            if (this.Y[var4] != null) {
               this.Y[var4].field_3665 = (boolean)1;
               this.S[var4].field_3654 = var1[var4];
               if (this.M[var4] != null) {
                  this.M[var4].field_3665 = this.M[var4 + 4].field_3665 = this.T[var4 + 4].field_3665;
               }
            }
         }

         this.j = (boolean)1;
      }
   }

   private record b(
      int u, int v, float x, float y, float z, float w, float h, float d, float growX, float growY, float growZ, boolean mirror, float texW, float texH
   ) {
      static fg.b of(class_630 var0) {
         return of(((q2ee3efb11fe52f83)var0).larpWalkCubes());
      }

      static fg.b of(List<class_628> var0) {
         if (var0.size() != 1) {
            return null;
         }

         class_628 var1 = (class_628)var0.getFirst();
         float var2 = var1.field_3648 - var1.field_3645;
         float var3 = var1.field_3647 - var1.field_3644;
         float var4 = var1.field_3646 - var1.field_3643;
         if (!(var2 <= 0.0F) && !(var3 <= 0.0F) && !(var4 <= 0.0F)) {
            float var5 = Float.MAX_VALUE;
            float var6 = -Float.MAX_VALUE;
            float var7 = Float.MAX_VALUE;
            float var8 = -Float.MAX_VALUE;
            float var9 = Float.MAX_VALUE;
            float var10 = -Float.MAX_VALUE;
            float var11 = Float.MAX_VALUE;
            float var12 = Float.MAX_VALUE;

            for (class_593 var16 : var1.field_3649) {
               for (class_618 var20 : var16.comp_3184()) {
                  var5 = Math.min(var5, var20.comp_4804());
                  var6 = Math.max(var6, var20.comp_4804());
                  var7 = Math.min(var7, var20.comp_4805());
                  var8 = Math.max(var8, var20.comp_4805());
                  var9 = Math.min(var9, var20.comp_4806());
                  var10 = Math.max(var10, var20.comp_4806());
                  var11 = Math.min(var11, var20.comp_3187());
                  var12 = Math.min(var12, var20.comp_3188());
               }
            }

            float var31 = 0.0F;
            float var32 = 0.0F;
            Boolean var33 = null;

            for (class_593 var37 : var1.field_3649) {
               class_618[] var38 = var37.comp_3184();
               float var21 = Float.MAX_VALUE;
               float var22 = -Float.MAX_VALUE;
               float var23 = Float.MAX_VALUE;
               float var24 = -Float.MAX_VALUE;
               byte var25 = 1;
               byte var26 = 1;

               for (class_618 var30 : var38) {
                  var21 = Math.min(var21, var30.comp_3187());
                  var22 = Math.max(var22, var30.comp_3187());
                  var23 = Math.min(var23, var30.comp_3188());
                  var24 = Math.max(var24, var30.comp_3188());
                  var25 &= Math.abs(var30.comp_4805() - var7) < 1.0E-4 ? 1 : 0;
                  var26 &= Math.abs(var30.comp_4804() - var38[0].comp_4804()) < 1.0E-4 ? 1 : 0;
               }

               if (var25 != 0 && var22 > var21 && var24 > var23) {
                  var31 = var2 / (var22 - var21);
                  var32 = var4 / (var24 - var23);
               }

               if (var26 != 0 && Math.abs(var21 - var11) < 1.0E-5) {
                  var33 = Math.abs(var38[0].comp_4804() - var6) < 1.0E-4;
               }
            }

            return !(var31 <= 0.0F) && !(var32 <= 0.0F) && var33 != null
               ? new fg.b(
                  Math.round(var11 * var31),
                  Math.round(var12 * var32),
                  var1.field_3645,
                  var1.field_3644,
                  var1.field_3643,
                  var2,
                  var3,
                  var4,
                  (var6 - var5 - var2) / 2.0F,
                  (var8 - var7 - var3) / 2.0F,
                  (var10 - var9 - var4) / 2.0F,
                  var33,
                  Math.round(var31),
                  Math.round(var32)
               )
               : null;
         } else {
            return null;
         }
      }

      class_628 whole() {
         return this.cube(this.y, this.h, 0);
      }

      class_628 upper(float var1) {
         return this.cube(this.y, var1 - this.y, 0);
      }

      class_628 lower(float var1) {
         float var2 = var1 - 1.0F;
         return this.cube(var2 - var1, this.y + this.h - var2, Math.round(var2 - this.y));
      }

      private class_628 cube(float var1, float var2, int var3) {
         return new class_628(
            this.u,
            this.v + var3,
            this.x,
            var1,
            this.z,
            this.w,
            var2,
            this.d,
            this.growX,
            this.growY,
            this.growZ,
            this.mirror,
            this.texW,
            this.texH,
            EnumSet.allOf(class_2350.class)
         );
      }
   }
}

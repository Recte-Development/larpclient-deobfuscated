package com.larpclient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.class_1011;
import net.minecraft.class_10537;
import net.minecraft.class_10539;
import net.minecraft.class_1060;
import net.minecraft.class_10799;
import net.minecraft.class_1084;
import net.minecraft.class_12253;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3300;
import net.minecraft.class_332;

public final class m {
   private static final int J = 128;
   private static final int G = 262144;
   private static final int j = 256;
   private static final int y = 512;
   private static final long S = TimeUnit.MINUTES.toNanos(1L);
   private static final AtomicInteger w = new AtomicInteger();
   private final class_2960 d = class_2960.method_60655("larp-client", "discord/avatar-" + w.getAndIncrement());
   private volatile String N = "";
   private volatile g.h W;
   private volatile boolean n;
   private volatile long q;
   private m.p A;
   private class_1060 T;
   private int I;

   public boolean G(class_332 var1, int var2, int var3, int var4) {
      ui var5 = bh.current().account();
      if (var5 != null && var4 > 0) {
         String var6 = var5.id() + "/" + var5.avatar();
         if (!var6.equals(this.N)) {
            this.q();
            this.N = var6;
         }

         g.h var7 = this.W;
         if (var7 == null) {
            if (!this.n && System.nanoTime() >= this.q) {
               this.B(var5, var6);
            }

            return false;
         } else {
            int var8 = (int)Math.clamp(Math.ceil(class_310.method_1551().method_22683().method_4495()), 1.0, 6.0);
            int var9 = Math.min(256, var4 * var8);
            if (!this.s(var7, var9)) {
               return false;
            }

            var1.method_25293(class_10799.field_56883, this.d, var2, var3, 0.0F, 0.0F, var4, var4, this.I, this.I, this.I, this.I, fp.R(-1));
            return true;
         }
      } else {
         this.q();
         return false;
      }
   }

   public void q() {
      if (this.A != null || !this.N.isEmpty()) {
         if (this.A != null) {
            class_310.method_1551().method_1531().method_4615(this.d);
            this.A = null;
         }

         this.N = "";
         this.W = null;
         this.I = 0;
         this.q = 0L;
      }
   }

   private boolean s(g.h var1, int var2) {
      class_1060 var3 = class_310.method_1551().method_1531();
      if (var3 != this.T) {
         this.A = null;
         this.I = 0;
         this.T = var3;
      }

      if (this.A != null && this.I == var2) {
         return true;
      }

      m.p var4 = new m.p(this.d, var1, var2);

      try {
         var3.method_65876(this.d, var4);
      } catch (RuntimeException var6) {
         var4.close();
         this.W = null;
         this.A = null;
         this.I = 0;
         this.q = System.nanoTime() + S;
         q8.O("Discord", "Avatar upload failed; the account card keeps the game head", var6);
         return false;
      }

      this.A = var4;
      this.I = var2;
      q8.v("Discord", "Prepared the linked avatar at {} pixels", var2);
      return true;
   }

   private void B(ui var1, String var2) {
      this.n = (boolean)1;
      String var3 = var1.avatarUrl(128);
      Thread.ofPlatform().daemon().name("Larp-Discord-Avatar").start(() -> {
         try {
            byte[] var3x = lh.t(var3, 262144, "image/png");
            z(var3x);
            g.e var4 = g.H(new ByteArrayInputStream(var3x));
            if (var2.equals(this.N)) {
               this.W = var4.picture();
            }
         } catch (Exception var8) {
            this.q = System.nanoTime() + S;
            q8.O("Discord", "Avatar download failed; the account card keeps the game head", var8);
         } finally {
            this.n = (boolean)0;
         }
      });
   }

   private static void z(byte[] var0) throws IOException {
      if (var0.length < 24) {
         throw new IOException("Avatar response is not a PNG");
      }

      ByteBuffer var1 = ByteBuffer.wrap(var0);
      if (var1.getLong() == -8552249625308161526L && var1.getInt() == 13 && var1.getInt() == 1229472850) {
         int var2 = var1.getInt();
         int var3 = var1.getInt();
         if (var2 < 1 || var3 < 1 || var2 > 512 || var3 > 512) {
            throw new IOException("Avatar is " + var2 + "x" + var3 + "; at most 512 per side");
         }
      } else {
         throw new IOException("Avatar response is not a PNG");
      }
   }

   private static int[] O(g.h var0, int var1) {
      int var2 = var0.width();
      int var3 = var0.height();
      int[] var4 = new int[var1 * var1];

      for (int var5 = 0; var5 < var1; var5++) {
         int var6 = var5 * var3 / var1;
         int var7 = Math.max(var6 + 1, (var5 + 1) * var3 / var1);

         for (int var8 = 0; var8 < var1; var8++) {
            int var9 = var8 * var2 / var1;
            int var10 = Math.max(var9 + 1, (var8 + 1) * var2 / var1);
            long var11 = 0L;
            long var13 = 0L;
            long var15 = 0L;
            long var17 = 0L;
            int var19 = 0;

            for (int var20 = var6; var20 < var7; var20++) {
               for (int var21 = var9; var21 < var10; var21++) {
                  int var22 = var0.pixels()[var20 * var2 + var21];
                  int var23 = var22 >>> 24;
                  var11 += var23;
                  var13 += (var22 >> 16 & 0xFF) * var23;
                  var15 += (var22 >> 8 & 0xFF) * var23;
                  var17 += (var22 & 0xFF) * var23;
                  var19++;
               }
            }

            var4[var5 * var1 + var8] = var11 == 0L
               ? 0
               : (int)(var11 / var19) << 24 | (int)(var13 / var11) << 16 | (int)(var15 / var11) << 8 | (int)(var17 / var11);
         }
      }

      return var4;
   }

   private static final class p extends class_10537 {
      private final g.h B;
      private final int f;

      p(class_2960 var1, g.h var2, int var3) {
         super(var1);
         this.B = var2;
         this.f = var3;
      }

      public class_10539 method_65809(class_3300 var1) {
         int[] var2 = m.O(this.B, this.f);
         class_1011 var3 = new class_1011(this.f, this.f, false);

         try {
            double var4 = this.f / 2.0;

            for (int var6 = 0; var6 < this.f; var6++) {
               for (int var7 = 0; var7 < this.f; var7++) {
                  double var8 = var4 - Math.hypot(var7 + 0.5 - var4, var6 + 0.5 - var4);
                  int var10 = var2[var6 * this.f + var7];
                  int var11 = (int)Math.round((var10 >>> 24) * Math.clamp(var8 + 0.5, 0.0, 1.0));
                  var3.method_61941(var7, var6, var11 << 24 | var10 & 16777215);
               }
            }

            return new class_10539(var3, new class_1084(true, true, class_12253.field_64077, 0.0F));
         } catch (RuntimeException var12) {
            var3.close();
            throw var12;
         }
      }
   }
}

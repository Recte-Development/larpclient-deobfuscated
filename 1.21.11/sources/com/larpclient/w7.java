package com.larpclient;

import com.mojang.blaze3d.textures.GpuTexture;
import java.util.Objects;
import net.minecraft.class_6367;

public final class w7 implements AutoCloseable {
   public class_6367 V;
   public class_6367 F;
   public class_6367 d;
   private final w7.t a;
   private final w7.c i;
   private w7.u s;

   private static class_6367 o(String var0, int var1, int var2, boolean var3, w7.c var4) {
      return var4 == w7.c.d ? new gy(var0, var1, var2, var3) : new class_6367(var0, var1, var2, var3);
   }

   public w7() {
      this(w7::o);
   }

   public w7(w7.c var1) {
      this(w7::o, var1);
   }

   w7(w7.t var1) {
      this(var1, w7.c.P);
   }

   private w7(w7.t var1, w7.c var2) {
      this.a = Objects.requireNonNull(var1);
      this.i = Objects.requireNonNull(var2);
   }

   public void V(int var1, int var2, int var3, boolean var4) {
      this.f(var1, var2, var3, var4 ? w7.p.G : w7.p.z);
   }

   public void f(int var1, int var2, int var3, w7.p var4) {
      w7.u var5 = new w7.u(var1, var2, var3, var4);
      if (!var5.equals(this.s)) {
         this.close();

         try {
            if (var4.F) {
               this.V = this.a.U("Larp glow mask", var1, var2, var4 != w7.p.r, this.i);
            }

            if (var4.d) {
               this.F = this.a.U("Larp blur ping", var5.scaledWidth(), var5.scaledHeight(), false, this.i);
            }

            if (var4.q) {
               this.d = this.a.U("Larp blur pong", var5.scaledWidth(), var5.scaledHeight(), false, this.i);
            }

            this.s = var5;
         } catch (RuntimeException var9) {
            try {
               this.close();
            } catch (RuntimeException var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         q8.v("Render", "Framebuffers recreated {}x{}, scale=1/{}, mode={}", var1, var2, var3, var4);
      }
   }

   public long G() {
      long var1 = 0L;

      for (class_6367 var6 : new class_6367[]{this.V, this.F, this.d}) {
         if (var6 != null) {
            for (GpuTexture var10 : new GpuTexture[]{var6.method_30277(), var6.method_30278()}) {
               if (var10 != null) {
                  var1 += (long)var10.getWidth(0)
                     * var10.getHeight(0)
                     * (var6 instanceof gy && var10 == var6.method_30277() ? 8 : var10.getFormat().pixelSize());
               }
            }
         }
      }

      return var1;
   }

   @Override
   public void close() {
      class_6367 var1 = this.V;
      class_6367 var2 = this.F;
      class_6367 var3 = this.d;
      this.V = this.F = this.d = null;
      this.s = null;
      RuntimeException var4 = null;

      for (class_6367 var8 : new class_6367[]{var1, var2, var3}) {
         if (var8 != null) {
            try {
               var8.method_1238();
            } catch (RuntimeException var10) {
               if (var4 == null) {
                  var4 = var10;
               } else {
                  var4.addSuppressed(var10);
               }
            }
         }
      }

      if (var4 != null) {
         throw var4;
      }
   }

   public enum c {
      P,
      d;
   }

   public enum p {
      x(false, true, false),
      u(true, false, false),
      r(true, false, false),
      z(false, true, true),
      G(true, true, true);

      final boolean F;
      final boolean d;
      final boolean q;

      p(boolean var3, boolean var4, boolean var5) {
         this.F = var3;
         this.d = var4;
         this.q = var5;
      }
   }

   @FunctionalInterface
   interface t {
      class_6367 U(String var1, int var2, int var3, boolean var4, w7.c var5);
   }

   public record u(int width, int height, int divisor, w7.p mode) {
      public u(int width, int height, int divisor, w7.p mode) {
         if (width >= 1 && height >= 1 && divisor >= 1 && mode != null) {
            this.width = width;
            this.height = height;
            this.divisor = divisor;
            this.mode = mode;
         } else {
            throw new IllegalArgumentException("Invalid framebuffer layout");
         }
      }

      public int scaledWidth() {
         return Math.max(1, this.width / this.divisor);
      }

      public int scaledHeight() {
         return Math.max(1, this.height / this.divisor);
      }

      public long colorBytes() {
         return (this.mode.F ? (long)this.width * this.height * 4L : 0L)
            + ((this.mode.d ? 1L : 0L) + (this.mode.q ? 1L : 0L)) * this.scaledWidth() * this.scaledHeight() * 4L;
      }
   }
}

package com.larpclient;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.textures.GpuTexture;
import java.util.Objects;

public final class e4 implements AutoCloseable {
   public TextureTarget M;
   public TextureTarget b;
   public TextureTarget v;
   private final e4.q u;
   private final GpuFormat d;
   private e4.b X;

   public e4() {
      this(hj::j);
   }

   public e4(GpuFormat var1) {
      this(hj::j, var1);
   }

   e4(e4.q var1) {
      this(var1, GpuFormat.RGBA8_UNORM);
   }

   private e4(e4.q var1, GpuFormat var2) {
      this.u = Objects.requireNonNull(var1);
      this.d = Objects.requireNonNull(var2);
   }

   public void r(int var1, int var2, int var3, boolean var4) {
      this.e(var1, var2, var3, var4 ? e4.o.G : e4.o.x);
   }

   public void e(int var1, int var2, int var3, e4.o var4) {
      e4.b var5 = new e4.b(var1, var2, var3, var4);
      if (!var5.equals(this.X)) {
         this.close();

         try {
            if (var4.p) {
               this.M = this.u.j("Larp glow mask", var1, var2, var4 != e4.o.C, this.d);
            }

            if (var4.W) {
               this.b = this.u.j("Larp blur ping", var5.scaledWidth(), var5.scaledHeight(), false, this.d);
            }

            if (var4.K) {
               this.v = this.u.j("Larp blur pong", var5.scaledWidth(), var5.scaledHeight(), false, this.d);
            }

            this.X = var5;
         } catch (RuntimeException var9) {
            try {
               this.close();
            } catch (RuntimeException var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         mt.U("Render", "Framebuffers recreated {}x{}, scale=1/{}, mode={}", var1, var2, var3, var4);
      }
   }

   public long E() {
      long var1 = 0L;

      for (TextureTarget var6 : new TextureTarget[]{this.M, this.b, this.v}) {
         if (var6 != null) {
            for (GpuTexture var10 : new GpuTexture[]{var6.getColorTexture(), var6.getDepthTexture()}) {
               if (var10 != null) {
                  var1 += (long)var10.getWidth(0) * var10.getHeight(0) * var10.getFormat().blockSize();
               }
            }
         }
      }

      return var1;
   }

   @Override
   public void close() {
      TextureTarget var1 = this.M;
      TextureTarget var2 = this.b;
      TextureTarget var3 = this.v;
      this.M = this.b = this.v = null;
      this.X = null;
      RuntimeException var4 = null;

      for (TextureTarget var8 : new TextureTarget[]{var1, var2, var3}) {
         if (var8 != null) {
            try {
               var8.destroyBuffers();
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

   public record b(int width, int height, int divisor, e4.o mode) {
      public b(int width, int height, int divisor, e4.o mode) {
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
         return (this.mode.p ? (long)this.width * this.height * 4L : 0L)
            + ((this.mode.W ? 1L : 0L) + (this.mode.K ? 1L : 0L)) * this.scaledWidth() * this.scaledHeight() * 4L;
      }
   }

   public enum o {
      c(false, true, false),
      f(true, false, false),
      C(true, false, false),
      x(false, true, true),
      G(true, true, true);

      final boolean p;
      final boolean W;
      final boolean K;

      o(boolean var3, boolean var4, boolean var5) {
         this.p = var3;
         this.W = var4;
         this.K = var5;
      }
   }

   @FunctionalInterface
   interface q {
      TextureTarget j(String var1, int var2, int var3, boolean var4, GpuFormat var5);
   }
}

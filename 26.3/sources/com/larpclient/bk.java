package com.larpclient;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.textures.GpuTexture;
import java.util.Objects;

public final class bk implements AutoCloseable {
   public TextureTarget Y;
   public TextureTarget B;
   public TextureTarget w;
   private final bk.t f;
   private final GpuFormat O;
   private bk.n P;

   public bk() {
      this(p7::m);
   }

   public bk(GpuFormat var1) {
      this(p7::m, var1);
   }

   bk(bk.t var1) {
      this(var1, GpuFormat.RGBA8_UNORM);
   }

   private bk(bk.t var1, GpuFormat var2) {
      this.f = Objects.requireNonNull(var1);
      this.O = Objects.requireNonNull(var2);
   }

   public void l(int var1, int var2, int var3, boolean var4) {
      this.l(var1, var2, var3, var4 ? bk.p.v : bk.p.U);
   }

   public void l(int var1, int var2, int var3, bk.p var4) {
      bk.n var5 = new bk.n(var1, var2, var3, var4);
      if (!var5.equals(this.P)) {
         this.close();

         try {
            if (var4.J) {
               this.Y = this.f.R("Larp glow mask", var1, var2, var4 != bk.p.y, this.O);
            }

            if (var4.f) {
               this.B = this.f.R("Larp blur ping", var5.scaledWidth(), var5.scaledHeight(), false, this.O);
            }

            if (var4.K) {
               this.w = this.f.R("Larp blur pong", var5.scaledWidth(), var5.scaledHeight(), false, this.O);
            }

            this.P = var5;
         } catch (RuntimeException var9) {
            try {
               this.close();
            } catch (RuntimeException var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         _q.c("Render", "Framebuffers recreated {}x{}, scale=1/{}, mode={}", var1, var2, var3, var4);
      }
   }

   public long Z() {
      long var1 = 0L;

      for (TextureTarget var6 : new TextureTarget[]{this.Y, this.B, this.w}) {
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
      TextureTarget var1 = this.Y;
      TextureTarget var2 = this.B;
      TextureTarget var3 = this.w;
      this.Y = this.B = this.w = null;
      this.P = null;
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

   public record n(int width, int height, int divisor, bk.p mode) {
      public n(int width, int height, int divisor, bk.p mode) {
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
         return (this.mode.J ? (long)this.width * this.height * 4L : 0L)
            + ((this.mode.f ? 1L : 0L) + (this.mode.K ? 1L : 0L)) * this.scaledWidth() * this.scaledHeight() * 4L;
      }
   }

   public enum p {
      m(false, true, false),
      R(true, false, false),
      y(true, false, false),
      U(false, true, true),
      v(true, true, true);

      final boolean J;
      final boolean f;
      final boolean K;

      p(boolean var3, boolean var4, boolean var5) {
         this.J = var3;
         this.f = var4;
         this.K = var5;
      }
   }

   @FunctionalInterface
   interface t {
      TextureTarget R(String var1, int var2, int var3, boolean var4, GpuFormat var5);
   }
}

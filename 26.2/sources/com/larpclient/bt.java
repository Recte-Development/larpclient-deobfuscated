package com.larpclient;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.textures.GpuTexture;
import java.util.Objects;

public final class bt implements AutoCloseable {
   public TextureTarget I;
   public TextureTarget M;
   public TextureTarget D;
   private final bt.t r;
   private final GpuFormat z;
   private bt.y F;

   public bt() {
      this(mx::n);
   }

   public bt(GpuFormat var1) {
      this(mx::n, var1);
   }

   bt(bt.t var1) {
      this(var1, GpuFormat.RGBA8_UNORM);
   }

   private bt(bt.t var1, GpuFormat var2) {
      this.r = Objects.requireNonNull(var1);
      this.z = Objects.requireNonNull(var2);
   }

   public void O(int var1, int var2, int var3, boolean var4) {
      this.V(var1, var2, var3, var4 ? bt.p.F : bt.p.M);
   }

   public void V(int var1, int var2, int var3, bt.p var4) {
      bt.y var5 = new bt.y(var1, var2, var3, var4);
      if (!var5.equals(this.F)) {
         this.close();

         try {
            if (var4.q) {
               this.I = this.r.G("Larp glow mask", var1, var2, var4 != bt.p.V, this.z);
            }

            if (var4.G) {
               this.M = this.r.G("Larp blur ping", var5.scaledWidth(), var5.scaledHeight(), false, this.z);
            }

            if (var4.X) {
               this.D = this.r.G("Larp blur pong", var5.scaledWidth(), var5.scaledHeight(), false, this.z);
            }

            this.F = var5;
         } catch (RuntimeException var9) {
            try {
               this.close();
            } catch (RuntimeException var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         h.U("Render", "Framebuffers recreated {}x{}, scale=1/{}, mode={}", var1, var2, var3, var4);
      }
   }

   public long N() {
      long var1 = 0L;

      for (TextureTarget var6 : new TextureTarget[]{this.I, this.M, this.D}) {
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
      TextureTarget var1 = this.I;
      TextureTarget var2 = this.M;
      TextureTarget var3 = this.D;
      this.I = this.M = this.D = null;
      this.F = null;
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

   public enum p {
      S(false, true, false),
      a(true, false, false),
      V(true, false, false),
      M(false, true, true),
      F(true, true, true);

      final boolean q;
      final boolean G;
      final boolean X;

      p(boolean var3, boolean var4, boolean var5) {
         this.q = var3;
         this.G = var4;
         this.X = var5;
      }
   }

   @FunctionalInterface
   interface t {
      TextureTarget G(String var1, int var2, int var3, boolean var4, GpuFormat var5);
   }

   public record y(int width, int height, int divisor, bt.p mode) {
      public y(int width, int height, int divisor, bt.p mode) {
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
         return (this.mode.q ? (long)this.width * this.height * 4L : 0L)
            + ((this.mode.G ? 1L : 0L) + (this.mode.X ? 1L : 0L)) * this.scaledWidth() * this.scaledHeight() * 4L;
      }
   }
}

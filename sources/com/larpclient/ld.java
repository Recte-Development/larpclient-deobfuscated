package com.larpclient;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.textures.GpuTexture;
import java.util.Objects;

public final class ld implements AutoCloseable {
   public TextureTarget B;
   public TextureTarget U;
   public TextureTarget H;
   private final ld.q e;
   private final GpuFormat L;
   private ld.f P;

   public ld() {
      this(of::T);
   }

   public ld(GpuFormat var1) {
      this(of::T, var1);
   }

   ld(ld.q var1) {
      this(var1, GpuFormat.RGBA8_UNORM);
   }

   private ld(ld.q var1, GpuFormat var2) {
      this.e = Objects.requireNonNull(var1);
      this.L = Objects.requireNonNull(var2);
   }

   public void t(int var1, int var2, int var3, boolean var4) {
      this.m(var1, var2, var3, var4 ? ld.y.c : ld.y.H);
   }

   public void m(int var1, int var2, int var3, ld.y var4) {
      ld.f var5 = new ld.f(var1, var2, var3, var4);
      if (!var5.equals(this.P)) {
         this.close();

         try {
            if (var4.a) {
               this.B = this.e.A("Larp glow mask", var1, var2, var4 != ld.y.R, this.L);
            }

            if (var4.p) {
               this.U = this.e.A("Larp blur ping", var5.scaledWidth(), var5.scaledHeight(), false, this.L);
            }

            if (var4.V) {
               this.H = this.e.A("Larp blur pong", var5.scaledWidth(), var5.scaledHeight(), false, this.L);
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

         r4.g("Render", "Framebuffers recreated {}x{}, scale=1/{}, mode={}", var1, var2, var3, var4);
      }
   }

   public long I() {
      long var1 = 0L;

      for (TextureTarget var6 : new TextureTarget[]{this.B, this.U, this.H}) {
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
      TextureTarget var1 = this.B;
      TextureTarget var2 = this.U;
      TextureTarget var3 = this.H;
      this.B = this.U = this.H = null;
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

   public record f(int width, int height, int divisor, ld.y mode) {
      public f(int width, int height, int divisor, ld.y mode) {
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
         return (this.mode.a ? (long)this.width * this.height * 4L : 0L)
            + ((this.mode.p ? 1L : 0L) + (this.mode.V ? 1L : 0L)) * this.scaledWidth() * this.scaledHeight() * 4L;
      }
   }

   @FunctionalInterface
   interface q {
      TextureTarget A(String var1, int var2, int var3, boolean var4, GpuFormat var5);
   }

   public enum y {
      s(false, true, false),
      O(true, false, false),
      R(true, false, false),
      H(false, true, true),
      c(true, true, true);

      final boolean a;
      final boolean p;
      final boolean V;

      y(boolean var3, boolean var4, boolean var5) {
         this.a = var3;
         this.p = var4;
         this.V = var5;
      }
   }
}

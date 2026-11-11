package com.larpclient.render.overlay;

import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.util.misc.LarpLog;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.textures.GpuTexture;
import java.util.Objects;

public final class GlowFramebuffers implements AutoCloseable {
   public TextureTarget glowMask;
   public TextureTarget blurPing;
   public TextureTarget blurPong;
   private final GlowFramebuffers.TargetFactory targetFactory;
   private final GpuFormat format;
   private GlowFramebuffers.Layout layout;

   public GlowFramebuffers() {
      this(RenderPipelineCompat::createTextureTarget);
   }

   public GlowFramebuffers(GpuFormat var1) {
      this(RenderPipelineCompat::createTextureTarget, var1);
   }

   GlowFramebuffers(GlowFramebuffers.TargetFactory var1) {
      this(var1, GpuFormat.RGBA8_UNORM);
   }

   private GlowFramebuffers(GlowFramebuffers.TargetFactory var1, GpuFormat var2) {
      this.targetFactory = Objects.requireNonNull(var1);
      this.format = Objects.requireNonNull(var2);
   }

   public void resize(int var1, int var2, int var3, boolean var4) {
      this.resize(var1, var2, var3, var4 ? GlowFramebuffers.Mode.MASK_AND_BLUR : GlowFramebuffers.Mode.BLUR_ONLY);
   }

   public void resize(int var1, int var2, int var3, GlowFramebuffers.Mode var4) {
      GlowFramebuffers.Layout var5 = new GlowFramebuffers.Layout(var1, var2, var3, var4);
      if (!var5.equals(this.layout)) {
         this.close();

         try {
            if (var4.HASMASK) {
               this.glowMask = this.targetFactory.create("Larp glow mask", var1, var2, var4 != GlowFramebuffers.Mode.MASK_NO_DEPTH, this.format);
            }

            if (var4.HASPING) {
               this.blurPing = this.targetFactory.create("Larp blur ping", var5.scaledWidth(), var5.scaledHeight(), false, this.format);
            }

            if (var4.HASPONG) {
               this.blurPong = this.targetFactory.create("Larp blur pong", var5.scaledWidth(), var5.scaledHeight(), false, this.format);
            }

            this.layout = var5;
         } catch (RuntimeException var9) {
            try {
               this.close();
            } catch (RuntimeException var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         LarpLog.debug("Render", "Framebuffers recreated {}x{}, scale=1/{}, mode={}", var1, var2, var3, var4);
      }
   }

   public long getMemoryBytes() {
      long var1 = 0L;

      for (TextureTarget var6 : new TextureTarget[]{this.glowMask, this.blurPing, this.blurPong}) {
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
      TextureTarget var1 = this.glowMask;
      TextureTarget var2 = this.blurPing;
      TextureTarget var3 = this.blurPong;
      this.glowMask = this.blurPing = this.blurPong = null;
      this.layout = null;
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

   public record Layout(int width, int height, int divisor, GlowFramebuffers.Mode mode) {
      public Layout(int width, int height, int divisor, GlowFramebuffers.Mode mode) {
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
         return (this.mode.HASMASK ? (long)this.width * this.height * 4L : 0L)
            + ((this.mode.HASPING ? 1L : 0L) + (this.mode.HASPONG ? 1L : 0L)) * this.scaledWidth() * this.scaledHeight() * 4L;
      }
   }

   public enum Mode {
      PING_ONLY(false, true, false),
      MASK_ONLY(true, false, false),
      MASK_NO_DEPTH(true, false, false),
      BLUR_ONLY(false, true, true),
      MASK_AND_BLUR(true, true, true);

      final boolean HASMASK;
      final boolean HASPING;
      final boolean HASPONG;

      Mode(boolean var3, boolean var4, boolean var5) {
         this.HASMASK = var3;
         this.HASPING = var4;
         this.HASPONG = var5;
      }
   }

   @FunctionalInterface
   interface TargetFactory {
      TextureTarget create(String var1, int var2, int var3, boolean var4, GpuFormat var5);
   }
}

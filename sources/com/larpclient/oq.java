package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.system.MemoryStack;

public abstract class oq implements ec {
   private final RenderPipeline y;
   private boolean S;
   protected double a;
   protected double W;

   protected oq(String var1, String... var2) {
      this.y = zx.f(var1, var2);
   }

   public final RenderPipeline i() {
      return this.y;
   }

   @Override
   public final void render(e_ var1) throws IOException {
      if (!this.S) {
         ea.z(this.y);
         this.load(var1);
         this.S = (boolean)1;
         mt.K("Sky", "Shader compiled and resources ready: {}", hj.q(this.y));
      }

      this.advance(var1);
      MemoryStack var2 = MemoryStack.stackPush();

      try {
         ByteBuffer var3 = var2.calloc(160);
         this.write(var1, var3);
         var3.position(0).limit(160);
         var1.t.r(var1, this, var3);
      } catch (Throwable var6) {
         if (var2 != null) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (var2 != null) {
         var2.close();
      }
   }

   protected void advance(e_ var1) {
      this.a = this.a + var1.R * var1.x.B();
      this.W = (this.W + var1.R * Math.toRadians(var1.x.v())) % (Math.PI * 2);
   }

   protected void load(e_ var1) throws IOException {
   }

   public void bindTextures(RenderPass var1) {
   }

   protected abstract void write(e_ var1, ByteBuffer var2);

   protected void g(e_ var1, ByteBuffer var2) {
      this.H(var1, var2, 1.0F, 1.0F);
   }

   protected void H(e_ var1, ByteBuffer var2, float var3, float var4) {
      CustomSkyModule var5 = var1.x;
      K(var2, var5.P().zenith());
      K(var2, var5.P().horizon());
      s(var2, (float)this.a, (float)this.W, var5.d(), var5.X() ? 1.0F : 0.0F);
      s(var2, var5.G() * var3, var5.y() * var4, 0.0F, var5.N());
      K(var2, var5.p());
      K(var2, var5.X());
   }

   protected static void s(ByteBuffer var0, float var1, float var2, float var3, float var4) {
      var0.putFloat(var1).putFloat(var2).putFloat(var3).putFloat(var4);
   }

   protected static void K(ByteBuffer var0, int var1) {
      s(var0, (var1 >> 16 & 0xFF) / 255.0F, (var1 >> 8 & 0xFF) / 255.0F, (var1 & 0xFF) / 255.0F, 1.0F);
   }

   @Override
   public final void reload() {
      this.close();
   }

   @Override
   public void close() {
      this.S = (boolean)0;
   }
}

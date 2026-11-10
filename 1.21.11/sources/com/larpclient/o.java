package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.system.MemoryStack;

public abstract class o implements gt {
   private final RenderPipeline k;
   private boolean e;
   protected double y;
   protected double T;

   protected o(String var1, String... var2) {
      this.k = fy.o(var1, var2);
   }

   public final RenderPipeline h() {
      return this.k;
   }

   @Override
   public final void render(gx var1) throws IOException {
      if (!this.e) {
         f2.a(this.k);
         this.load(var1);
         this.e = (boolean)1;
         q8.G("Sky", "Shader compiled and resources ready: {}", yf.z(this.k));
      }

      this.advance(var1);
      MemoryStack var2 = MemoryStack.stackPush();

      try {
         ByteBuffer var3 = var2.calloc(160);
         this.write(var1, var3);
         var3.position(0).limit(160);
         var1.z.D(var1, this, var3);
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

   protected void advance(gx var1) {
      this.y = this.y + var1.t * var1.w.L();
      this.T = (this.T + var1.t * Math.toRadians(var1.w.o())) % (Math.PI * 2);
   }

   protected void load(gx var1) throws IOException {
   }

   public void bindTextures(RenderPass var1) {
   }

   protected abstract void write(gx var1, ByteBuffer var2);

   protected void J(gx var1, ByteBuffer var2) {
      this.R(var1, var2, 1.0F, 1.0F);
   }

   protected void R(gx var1, ByteBuffer var2, float var3, float var4) {
      CustomSkyModule var5 = var1.w;
      L(var2, var5.Y().zenith());
      L(var2, var5.Y().horizon());
      J(var2, (float)this.y, (float)this.T, var5.U(), var5.D() ? 1.0F : 0.0F);
      J(var2, var5.M() * var3, var5.N() * var4, 0.0F, var5.E());
      L(var2, var5.P());
      L(var2, var5.V());
   }

   protected static void J(ByteBuffer var0, float var1, float var2, float var3, float var4) {
      var0.putFloat(var1).putFloat(var2).putFloat(var3).putFloat(var4);
   }

   protected static void L(ByteBuffer var0, int var1) {
      J(var0, (var1 >> 16 & 0xFF) / 255.0F, (var1 >> 8 & 0xFF) / 255.0F, (var1 & 0xFF) / 255.0F, 1.0F);
   }

   @Override
   public final void reload() {
      this.close();
   }

   @Override
   public void close() {
      this.e = (boolean)0;
   }
}

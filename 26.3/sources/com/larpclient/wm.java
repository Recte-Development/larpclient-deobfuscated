package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.system.MemoryStack;

public abstract class wm implements d0 {
   private final RenderPipeline f;
   private boolean L;
   protected double K;
   protected double H;

   protected wm(String var1, String... var2) {
      this.f = bm.m(var1, var2);
   }

   public final RenderPipeline S() {
      return this.f;
   }

   @Override
   public final void render(pa var1) throws IOException {
      if (!this.L) {
         se.j(this.f);
         this.load(var1);
         this.L = (boolean)1;
         _q.z("Sky", "Shader compiled and resources ready: {}", p7.a(this.f));
      }

      this.advance(var1);
      MemoryStack var2 = MemoryStack.stackPush();

      try {
         ByteBuffer var3 = var2.calloc(160);
         this.write(var1, var3);
         var3.position(0).limit(160);
         var1.D.a(var1, this, var3);
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

   protected void advance(pa var1) {
      this.K = this.K + var1.J * var1.e.j();
      this.H = (this.H + var1.J * Math.toRadians(var1.e.f())) % (Math.PI * 2);
   }

   protected void load(pa var1) throws IOException {
   }

   public void bindTextures(RenderPass var1) {
   }

   protected abstract void write(pa var1, ByteBuffer var2);

   protected void G(pa var1, ByteBuffer var2) {
      this.I(var1, var2, 1.0F, 1.0F);
   }

   protected void I(pa var1, ByteBuffer var2, float var3, float var4) {
      CustomSkyModule var5 = var1.e;
      S(var2, var5.X().zenith());
      S(var2, var5.X().horizon());
      j(var2, (float)this.K, (float)this.H, var5.T(), var5.G() ? 1.0F : 0.0F);
      j(var2, var5.O() * var3, var5.F() * var4, 0.0F, var5.V());
      S(var2, var5.P());
      S(var2, var5.f());
   }

   protected static void j(ByteBuffer var0, float var1, float var2, float var3, float var4) {
      var0.putFloat(var1).putFloat(var2).putFloat(var3).putFloat(var4);
   }

   protected static void S(ByteBuffer var0, int var1) {
      j(var0, (var1 >> 16 & 0xFF) / 255.0F, (var1 >> 8 & 0xFF) / 255.0F, (var1 & 0xFF) / 255.0F, 1.0F);
   }

   @Override
   public final void reload() {
      this.close();
   }

   @Override
   public void close() {
      this.L = (boolean)0;
   }
}

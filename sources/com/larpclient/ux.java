package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.system.MemoryStack;

public abstract class ux implements _1 {
   private final RenderPipeline f;
   private boolean Q;
   protected double E;
   protected double y;

   protected ux(String var1, String... var2) {
      this.f = ln.k(var1, var2);
   }

   public final RenderPipeline K() {
      return this.f;
   }

   @Override
   public final void render(lb var1) throws IOException {
      if (!this.Q) {
         e4.U(this.f);
         this.load(var1);
         this.Q = (boolean)1;
         r4.V("Sky", "Shader compiled and resources ready: {}", of.b(this.f));
      }

      this.advance(var1);
      MemoryStack var2 = MemoryStack.stackPush();

      try {
         ByteBuffer var3 = var2.calloc(160);
         this.write(var1, var3);
         var3.position(0).limit(160);
         var1.d.t(var1, this, var3);
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

   protected void advance(lb var1) {
      this.E = this.E + var1.N * var1.o.s();
      this.y = (this.y + var1.N * Math.toRadians(var1.o.P())) % (Math.PI * 2);
   }

   protected void load(lb var1) throws IOException {
   }

   public void bindTextures(RenderPass var1) {
   }

   protected abstract void write(lb var1, ByteBuffer var2);

   protected void K(lb var1, ByteBuffer var2) {
      this.e(var1, var2, 1.0F, 1.0F);
   }

   protected void e(lb var1, ByteBuffer var2, float var3, float var4) {
      CustomSkyModule var5 = var1.o;
      y(var2, var5.p().zenith());
      y(var2, var5.p().horizon());
      K(var2, (float)this.E, (float)this.y, var5.h(), var5.A() ? 1.0F : 0.0F);
      K(var2, var5.p() * var3, var5.c() * var4, 0.0F, var5.H());
      y(var2, var5.k());
      y(var2, var5.F());
   }

   protected static void K(ByteBuffer var0, float var1, float var2, float var3, float var4) {
      var0.putFloat(var1).putFloat(var2).putFloat(var3).putFloat(var4);
   }

   protected static void y(ByteBuffer var0, int var1) {
      K(var0, (var1 >> 16 & 0xFF) / 255.0F, (var1 >> 8 & 0xFF) / 255.0F, (var1 & 0xFF) / 255.0F, 1.0F);
   }

   @Override
   public final void reload() {
      this.close();
   }

   @Override
   public void close() {
      this.Q = (boolean)0;
   }
}

package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.system.MemoryStack;

public abstract class z5 implements tp {
   private final RenderPipeline q;
   private boolean D;
   protected double O;
   protected double f;

   protected z5(String var1, String... var2) {
      this.q = sq.b(var1, var2);
   }

   public final RenderPipeline p() {
      return this.q;
   }

   @Override
   public final void render(wj var1) throws IOException {
      if (!this.D) {
         ai.U(this.q);
         this.load(var1);
         this.D = (boolean)1;
         h.A("Sky", "Shader compiled and resources ready: {}", mx.K(this.q));
      }

      this.advance(var1);
      MemoryStack var2 = MemoryStack.stackPush();

      try {
         ByteBuffer var3 = var2.calloc(160);
         this.write(var1, var3);
         var3.position(0).limit(160);
         var1.M.r(var1, this, var3);
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

   protected void advance(wj var1) {
      this.O = this.O + var1.k * var1.n.U();
      this.f = (this.f + var1.k * Math.toRadians(var1.n.A())) % (Math.PI * 2);
   }

   protected void load(wj var1) throws IOException {
   }

   public void bindTextures(RenderPass var1) {
   }

   protected abstract void write(wj var1, ByteBuffer var2);

   protected void m(wj var1, ByteBuffer var2) {
      this.S(var1, var2, 1.0F, 1.0F);
   }

   protected void S(wj var1, ByteBuffer var2, float var3, float var4) {
      CustomSkyModule var5 = var1.n;
      m(var2, var5.m().zenith());
      m(var2, var5.m().horizon());
      B(var2, (float)this.O, (float)this.f, var5.z(), var5.o() ? 1.0F : 0.0F);
      B(var2, var5.K() * var3, var5.D() * var4, 0.0F, var5.X());
      m(var2, var5.n());
      m(var2, var5.m());
   }

   protected static void B(ByteBuffer var0, float var1, float var2, float var3, float var4) {
      var0.putFloat(var1).putFloat(var2).putFloat(var3).putFloat(var4);
   }

   protected static void m(ByteBuffer var0, int var1) {
      B(var0, (var1 >> 16 & 0xFF) / 255.0F, (var1 >> 8 & 0xFF) / 255.0F, (var1 & 0xFF) / 255.0F, 1.0F);
   }

   @Override
   public final void reload() {
      this.close();
   }

   @Override
   public void close() {
      this.D = (boolean)0;
   }
}

package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.minecraft.class_1049;
import net.minecraft.class_2960;
import net.minecraft.class_310;

public final class ia extends o {
   private static final class_2960 f = class_2960.method_60655("larp-client", "textures/sky/nebula/nebula_clouds.png");
   private class_1049 J;

   public ia() {
      super("nebula", "NebulaClouds");
   }

   @Override
   protected void load(gx var1) throws IOException {
      this.J = new class_1049(f);
      this.J.method_65857(this.J.method_65809(class_310.method_1551().method_1478()));
   }

   @Override
   protected void advance(gx var1) {
      this.y = this.y + var1.t * var1.w.P();
      this.T = (this.T + var1.t * Math.toRadians(var1.w.D())) % (Math.PI * 2);
   }

   @Override
   protected void write(gx var1, ByteBuffer var2) {
      CustomSkyModule var3 = var1.w;
      lw var4 = var3.Y();
      L(var2, var4.zenith());
      L(var2, var4.horizon());
      J(var2, var3.Q(), 0.0F, var3.M(), 0.0F);
      J(var2, var3.g(), var3.c(), var3.v(), var3.N());
      J(var2, (float)this.y, (float)this.T, 0.0F, 0.0F);
      J(var2, var3.P() ? 1.0F : 0.0F, var3.X(), var3.R(), 0.0F);
      L(var2, var3.y());
   }

   @Override
   public void bindTextures(RenderPass var1) {
      yf.R(var1, "NebulaClouds", this.J.method_71659(), RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR));
   }

   @Override
   public void close() {
      super.close();
      if (this.J != null) {
         this.J.close();
         this.J = null;
      }
   }
}

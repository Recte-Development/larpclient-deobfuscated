package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.textures.FilterMode;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.Identifier;

public final class os extends oq {
   private static final Identifier U = Identifier.fromNamespaceAndPath("larp-client", "textures/sky/nebula/nebula_clouds.png");
   private SimpleTexture s;

   public os() {
      super("nebula", "NebulaClouds");
   }

   @Override
   protected void load(e_ var1) throws IOException {
      this.s = new SimpleTexture(U);
      this.s.apply(this.s.loadContents(Minecraft.getInstance().getResourceManager()));
   }

   @Override
   protected void advance(e_ var1) {
      this.a = this.a + var1.R * var1.x.M();
      this.W = (this.W + var1.R * Math.toRadians(var1.x.s())) % (Math.PI * 2);
   }

   @Override
   protected void write(e_ var1, ByteBuffer var2) {
      CustomSkyModule var3 = var1.x;
      et var4 = var3.P();
      K(var2, var4.zenith());
      K(var2, var4.horizon());
      s(var2, var3.X(), 0.0F, var3.G(), 0.0F);
      s(var2, var3.t(), var3.b(), var3.S(), var3.y());
      s(var2, (float)this.a, (float)this.W, 0.0F, 0.0F);
      s(var2, var3.W() ? 1.0F : 0.0F, var3.A(), var3.h(), 0.0F);
      K(var2, var3.x());
   }

   @Override
   public void bindTextures(RenderPass var1) {
      hj.u(var1, "NebulaClouds", this.s.getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
   }

   @Override
   public void close() {
      super.close();
      if (this.s != null) {
         this.s.close();
         this.s = null;
      }
   }
}

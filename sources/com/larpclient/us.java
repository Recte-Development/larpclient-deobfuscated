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

public final class us extends ux {
   private static final Identifier x = Identifier.fromNamespaceAndPath("larp-client", "textures/sky/nebula/nebula_clouds.png");
   private SimpleTexture s;

   public us() {
      super("nebula", "NebulaClouds");
   }

   @Override
   protected void load(lb var1) throws IOException {
      this.s = new SimpleTexture(x);
      this.s.apply(this.s.loadContents(Minecraft.getInstance().getResourceManager()));
   }

   @Override
   protected void advance(lb var1) {
      this.E = this.E + var1.N * var1.o.b();
      this.y = (this.y + var1.N * Math.toRadians(var1.o.u())) % (Math.PI * 2);
   }

   @Override
   protected void write(lb var1, ByteBuffer var2) {
      CustomSkyModule var3 = var1.o;
      f5 var4 = var3.p();
      y(var2, var4.zenith());
      y(var2, var4.horizon());
      K(var2, var3.x(), 0.0F, var3.p(), 0.0F);
      K(var2, var3.f(), var3.T(), var3.i(), var3.c());
      K(var2, (float)this.E, (float)this.y, 0.0F, 0.0F);
      K(var2, var3.M() ? 1.0F : 0.0F, var3.G(), var3.A(), 0.0F);
      y(var2, var3.f());
   }

   @Override
   public void bindTextures(RenderPass var1) {
      of.h(var1, "NebulaClouds", this.s.getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
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

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

public final class wt extends wm {
   private static final Identifier o = Identifier.fromNamespaceAndPath("larp-client", "textures/sky/nebula/nebula_clouds.png");
   private SimpleTexture T;

   public wt() {
      super("nebula", "NebulaClouds");
   }

   @Override
   protected void load(pa var1) throws IOException {
      this.T = new SimpleTexture(o);
      this.T.apply(this.T.loadContents(Minecraft.getInstance().getResourceManager()));
   }

   @Override
   protected void advance(pa var1) {
      this.K = this.K + var1.J * var1.e.L();
      this.H = (this.H + var1.J * Math.toRadians(var1.e.l())) % (Math.PI * 2);
   }

   @Override
   protected void write(pa var1, ByteBuffer var2) {
      CustomSkyModule var3 = var1.e;
      _i var4 = var3.X();
      S(var2, var4.zenith());
      S(var2, var4.horizon());
      j(var2, var3.U(), 0.0F, var3.O(), 0.0F);
      j(var2, var3.J(), var3.d(), var3.X(), var3.F());
      j(var2, (float)this.K, (float)this.H, 0.0F, 0.0F);
      j(var2, var3.y() ? 1.0F : 0.0F, var3.k(), var3.R(), 0.0F);
      S(var2, var3.c());
   }

   @Override
   public void bindTextures(RenderPass var1) {
      p7.t(var1, "NebulaClouds", this.T.getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
   }

   @Override
   public void close() {
      super.close();
      if (this.T != null) {
         this.T.close();
         this.T = null;
      }
   }
}

package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.Identifier;

public final class zb extends z5 {
   private static final Identifier U = Identifier.fromNamespaceAndPath("larp-client", "textures/sky/nebula/nebula_clouds.png");
   private SimpleTexture R;

   public zb() {
      super("nebula", "NebulaClouds");
   }

   @Override
   protected void load(wj var1) throws IOException {
      this.R = new SimpleTexture(U);
      this.R.apply(this.R.loadContents(Minecraft.getInstance().getResourceManager()));
   }

   @Override
   protected void advance(wj var1) {
      this.O = this.O + var1.k * var1.n.O();
      this.f = (this.f + var1.k * Math.toRadians(var1.n.l())) % (Math.PI * 2);
   }

   @Override
   protected void write(wj var1, ByteBuffer var2) {
      CustomSkyModule var3 = var1.n;
      b1 var4 = var3.m();
      m(var2, var4.zenith());
      m(var2, var4.horizon());
      B(var2, var3.G(), 0.0F, var3.K(), 0.0F);
      B(var2, var3.C(), var3.e(), var3.r(), var3.D());
      B(var2, (float)this.O, (float)this.f, 0.0F, 0.0F);
      B(var2, var3.V() ? 1.0F : 0.0F, var3.a(), var3.S(), 0.0F);
      m(var2, var3.b());
   }

   @Override
   public void bindTextures(RenderPass var1) {
      mx.x(var1, "NebulaClouds", this.R.getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
   }

   @Override
   public void close() {
      super.close();
      if (this.R != null) {
         this.R.close();
         this.R = null;
      }
   }
}

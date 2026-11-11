package com.larpclient.render.sky;

import com.larpclient.module.render.CustomSkyModule;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.textures.FilterMode;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.Identifier;

public final class NebulaCloudsSky extends SkyShader {
   private static final Identifier tEXTURE = Identifier.fromNamespaceAndPath("larp-client", "textures/sky/nebula/nebula_clouds.png");
   private SimpleTexture texture;

   public NebulaCloudsSky() {
      super("nebula", "NebulaClouds");
   }

   @Override
   protected void load(SkyRenderContext var1) throws IOException {
      this.texture = new SimpleTexture(tEXTURE);
      this.texture.apply(this.texture.loadContents(Minecraft.getInstance().getResourceManager()));
   }

   @Override
   protected void advance(SkyRenderContext var1) {
      this.time = this.time + var1.partialTick * var1.skyModule.getNebulaAnimationSpeed();
      this.rotation = (this.rotation + var1.partialTick * Math.toRadians(var1.skyModule.getNebulaRotationSpeed())) % (Math.PI * 2);
   }

   @Override
   protected void write(SkyRenderContext var1, ByteBuffer var2) {
      CustomSkyModule var3 = var1.skyModule;
      SkyGradient var4 = var3.getGradient();
      getPipeline(var2, var4.zenith());
      getPipeline(var2, var4.horizon());
      putVec4(var2, var3.getAnimationTime(), 0.0F, var3.getStarBrightness(), 0.0F);
      putVec4(var2, var3.getNebulaBrightness(), var3.getNebulaSaturation(), var3.getGradient(), var3.isShowVanillaMoon());
      putVec4(var2, (float)this.time, (float)this.rotation, 0.0F, 0.0F);
      putVec4(var2, var3.isFlowMotion() ? 1.0F : 0.0F, var3.getNebulaFlowStrength(), var3.getEnvironmentSettings(), 0.0F);
      getPipeline(var2, var3.getNebulaColor());
   }

   @Override
   public void bindTextures(RenderPass var1) {
      RenderPipelineCompat.setSamplerUniform(
         var1, "NebulaClouds", this.texture.getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)
      );
   }

   @Override
   public void close() {
      super.close();
      if (this.texture != null) {
         this.texture.close();
         this.texture = null;
      }
   }
}

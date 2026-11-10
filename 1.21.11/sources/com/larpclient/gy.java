package com.larpclient;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.TextureFormat;
import java.nio.ByteBuffer;
import net.minecraft.class_10868;
import net.minecraft.class_6367;
import org.lwjgl.opengl.GL11;

public final class gy extends class_6367 {
   public gy(String var1, int var2, int var3, boolean var4) {
      super(var1, var2, var3, var4);
   }

   public void method_1231(int var1, int var2) {
      super.method_1231(var1, var2);
      this.method_71639().close();
      this.method_30277().close();
      this.field_60567 = null;
      this.field_1475 = null;
      int var3 = GL11.glGenTextures();
      int var4 = GL11.glGetInteger(32873);

      try {
         GL11.glBindTexture(3553, var3);
         GL11.glTexParameteri(3553, 33084, 0);
         GL11.glTexParameteri(3553, 33085, 0);
         GL11.glTexImage2D(3553, 0, 34842, var1, var2, 0, 6408, 5126, (ByteBuffer)null);
         if (GL11.glGetTexLevelParameteri(3553, 0, 4099) != 34842) {
            throw new IllegalStateException("RGBA16F mask allocation did not retain floating storage");
         }

         this.field_1475 = new gy.k(this.field_56738, var1, var2, var3);
         this.field_60567 = RenderSystem.getDevice().createTextureView(this.field_1475);
      } catch (RuntimeException var9) {
         if (this.field_1475 == null) {
            GL11.glDeleteTextures(var3);
         }

         this.method_1238();
         throw var9;
      } finally {
         GL11.glBindTexture(3553, var4);
      }
   }

   private static final class k extends class_10868 {
      k(String var1, int var2, int var3, int var4) {
         super(14, var1, TextureFormat.RGBA8, var2, var3, 1, 1, var4);
      }
   }
}

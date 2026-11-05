package com.larpclient;

import com.larpclient.module.EmotesModule;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class xx {
   private static final String L = "models/emotes/";
   private static final float s = 0.86F;
   private static final float d = 2.25F;
   private static final float C = 0.05F;
   private static final int g = 9;
   private static final RenderPipeline t = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
      .withLocation(T("emote_prop"))
      .withFragmentShader(T("particle/sprite"))
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
      .withColorTargetState(ColorTargetState.DEFAULT)
      .withCull(true)
      .build();
   private static final Map<String, xx.v> v = new HashMap<>();
   private static final Map<String, RenderType> R = new HashMap<>();
   private static xx.x u;

   private static Identifier T(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private xx() {
   }

   public static void j() {
      u = null;
   }

   private static xx.v o(String var0) {
      xx.v var1 = v.get(var0);
      if (var1 != null && var1.h == jk.v) {
         return var1;
      }

      ResourceManager var2 = Minecraft.getInstance().getResourceManager();
      Identifier var3 = T("models/emotes/" + var0 + "/albedo.png");

      try (InputStream var4 = var2.getResourceOrThrow(T("models/emotes/" + var0 + "/mesh.bin")).open()) {
         var2.getResourceOrThrow(var3);
         m2.k var5 = m2.A(var4);
         if (var5.parts().size() != 2) {
            throw new IOException("Prop needs a body and a wheel part");
         }

         e4.U(t);
         RenderType var6 = R.computeIfAbsent(
            var0, var1x -> RenderType.create("larp_emote_prop_" + var1x, RenderSetup.builder(t).withTexture("Sampler0", var3).createRenderSetup())
         );
         var1 = new xx.v(var5, var6, jk.v);
         v.put(var0, var1);
         r4.V("Emotes", "Prop {} ready: {} triangles, resource generation {}", var0, var5.triangles(), var1.h);
         return var1;
      } catch (IOException var10) {
         throw new IllegalStateException("Emote prop " + var0 + " failed to load; reload resources to retry", var10);
      }
   }

   public static void i(float var0) {
      u = null;
      EmotesModule.s var1 = EmotesModule.A(var0);
      if (var1 != null) {
         float var2 = var1.track()[6] * var1.fade() * var1.avatarScale();
         float var3 = Math.min(1.0F, var1.age() / 9.0F);
         float var4 = var1.stopping() ? Math.min(1.0F, var1.stopAge() / 4.0F) : var3;
         float var5 = var1.stopping() ? (1.0F - var4) * 0.45F : (var3 < 1.0F ? (1.0F - var3) * (1.0F - var3) * 0.55F : 0.0F);
         if (!(var2 < 0.001F) || !(var5 <= 0.0F)) {
            xx.v var6 = o(var1.prop());
            float[] var7 = var1.track();
            Quaternionf var8 = new Quaternionf().rotationY((float)Math.toRadians(180.0F - var1.facing()));
            Quaternionf var9 = new Quaternionf(var8).rotateY(var7[4]);
            Quaternionf var10 = new Quaternionf(var9).rotateZ(var7[5]).rotateX(var7[3]);
            Vector3f var11 = var8.transform(new Vector3f(var7[0] * var2, 0.0F, var7[2] * var2));
            Vec3 var12 = var1.feet().add(var11.x, var11.y, var11.z);
            Vector3f var13 = var9.transform(new Vector3f(0.0F, var7[1] * var2, 0.0F));
            ClientLevel var14 = Minecraft.getInstance().level;
            int var15 = 15;
            if (var14 != null) {
               BlockPos var16 = BlockPos.containing(var12.x, var12.y + 1.2, var12.z);
               var15 = var14.getRawBrightness(var16, var14.getSkyDarken());
            }

            x(var6, var15);
            u = new xx.x(
               var6,
               var12,
               var9,
               var10,
               var12.add(var13.x, var13.y, var13.z),
               var2,
               Math.clamp(0.42F - var7[1] * 0.9F, 0.0F, 0.5F) * Math.min(1.0F, var2),
               0.55F + 1.9F * var4,
               var5
            );
         }
      }
   }

   private static void x(xx.v var0, int var1) {
      if (var0.Q != var1) {
         float var2 = 0.22F + 0.78F * (float)Math.pow(var1 / 15.0, 1.3);

         for (int var3 = 0; var3 < var0.q.length; var3++) {
            int[] var4 = var0.e.parts().get(var3).colors();
            int[] var5 = var0.q[var3];

            for (int var6 = 0; var6 < var4.length; var6++) {
               int var7 = Math.round((var4[var6] & 0xFF) * var2);
               var5[var6] = 0xFF000000 | var7 << 16 | var7 << 8 | var7;
            }
         }

         var0.Q = var1;
      }
   }

   public static void T(r6 var0, boolean var1) {
      xx.x var2 = u;
      if (var2 != null && !var1) {
         if (var2.puffAlpha > 0.0F) {
            var0.C(var2.ground.x, var2.ground.y + 0.02, var2.ground.z, 1.1 * var2.puff, 0.5 + 0.4 * var2.puff, oh.i(-1446155, var2.puffAlpha));
            var0.C(var2.ground.x, var2.ground.y + 0.03, var2.ground.z, 0.6 * var2.puff, 0.35, oh.i(-1, var2.puffAlpha * 0.7));
         }

         if (!(var2.scale < 0.001F)) {
            if (var2.shadow > 0.0F) {
               G(var0, var2);
            }

            List var3 = var2.prop.e.parts();
            var0.R(var2.prop.U, var2.bodyOrigin, var2.bodyRotation, var2.scale, ((m2.t)var3.get(0)).vertices(), var2.prop.q[0]);
            var0.R(var2.prop.U, var2.ground, var2.carRotation, var2.scale, ((m2.t)var3.get(1)).vertices(), var2.prop.q[1]);
            h_.A("Emote prop triangles", var2.prop.e.triangles());
         }
      }
   }

   private static void G(r6 var0, xx.x var1) {
      float var2 = 0.32F * var1.scale;
      float var3 = 0.86F * var1.scale;
      float var4 = 2.25F * var1.scale;
      float var5 = 0.05F * var1.scale;
      int var6 = oh.i(-16777216, var1.shadow);
      byte var7 = 0;
      float[][] var8 = new float[][]{{-var3, var5 - var4}, {var3, var5 - var4}, {var3, var5 + var4}, {-var3, var5 + var4}};
      float[][] var9 = new float[][]{
         {-var3 - var2, var5 - var4 - var2}, {var3 + var2, var5 - var4 - var2}, {var3 + var2, var5 + var4 + var2}, {-var3 - var2, var5 + var4 + var2}
      };
      s(var0, var1, var8[3], var6, var8[2], var6, var8[1], var6, var8[0], var6);

      for (int var10 = 0; var10 < 4; var10++) {
         int var11 = (var10 + 1) % 4;
         s(var0, var1, var9[var10], var7, var9[var11], var7, var8[var11], var6, var8[var10], var6);
      }
   }

   private static void s(r6 var0, xx.x var1, float[] var2, int var3, float[] var4, int var5, float[] var6, int var7, float[] var8, int var9) {
      K(var0, var1, var2, var3);
      K(var0, var1, var4, var5);
      K(var0, var1, var6, var7);
      K(var0, var1, var8, var9);
   }

   private static void K(r6 var0, xx.x var1, float[] var2, int var3) {
      Vector3f var4 = var1.carRotation.transform(new Vector3f(var2[0], 0.0F, var2[1]));
      var0.vertex(var1.ground.x + var4.x, var1.ground.y + 0.015, var1.ground.z + var4.z, var3);
   }

   private static final class v {
      final m2.k e;
      final RenderType U;
      final long h;
      final int[][] q;
      int Q = -1;

      v(m2.k var1, RenderType var2, long var3) {
         this.e = var1;
         this.U = var2;
         this.h = var3;
         this.q = new int[var1.parts().size()][];

         for (int var5 = 0; var5 < this.q.length; var5++) {
            this.q[var5] = new int[var1.parts().get(var5).colors().length];
         }
      }
   }

   private record x(
      xx.v prop, Vec3 ground, Quaternionf carRotation, Quaternionf bodyRotation, Vec3 bodyOrigin, float scale, float shadow, float puff, float puffAlpha
   ) {
   }
}

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
import java.util.Arrays;
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

public final class iz {
   private static final String e = "models/emotes/";
   private static final float V = 0.86F;
   private static final float Z = 2.25F;
   private static final float b = 0.05F;
   private static final float y = 0.66F;
   private static final int X = 9;
   private static final RenderPipeline g = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
      .withLocation(U("emote_prop"))
      .withFragmentShader(U("particle/sprite"))
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
      .withColorTargetState(ColorTargetState.DEFAULT)
      .withCull(true)
      .build();
   private static final Map<String, iz.h> p = new HashMap<>();
   private static final Map<String, RenderType> K = new HashMap<>();
   private static iz.t B;

   private static Identifier U(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private iz() {
   }

   public static void O() {
      B = null;
   }

   private static iz.h x(String var0) {
      iz.h var1 = p.get(var0);
      if (var1 != null && var1.z == nl.q) {
         return var1;
      }

      ResourceManager var2 = Minecraft.getInstance().getResourceManager();
      Identifier var3 = U("models/emotes/" + var0 + "/albedo.png");

      try (InputStream var4 = var2.getResourceOrThrow(U("models/emotes/" + var0 + "/mesh.bin")).open()) {
         var2.getResourceOrThrow(var3);
         qu.j var5 = qu.Z(var4);
         if (var5.parts().size() != 2) {
            throw new IOException("Prop needs a body and a wheel part");
         }

         se.j(g);
         RenderType var6 = K.computeIfAbsent(
            var0, var1x -> RenderType.create("larp_emote_prop_" + var1x, RenderSetup.builder(g).withTexture("Sampler0", var3).createRenderSetup())
         );
         var1 = new iz.h(var5, var6, nl.q);
         p.put(var0, var1);
         _q.z("Emotes", "Prop {} ready: {} triangles, resource generation {}", var0, var5.triangles(), var1.z);
         return var1;
      } catch (IOException var10) {
         throw new IllegalStateException("Emote prop " + var0 + " failed to load; reload resources to retry", var10);
      }
   }

   public static void Y(float var0) {
      B = null;
      EmotesModule.o var1 = EmotesModule.X(var0);
      if (var1 != null) {
         float var2 = var1.track()[6] * var1.fade() * var1.avatarScale();
         float var3 = Math.min(1.0F, var1.age() / 9.0F);
         float var4 = var1.stopping() ? Math.min(1.0F, var1.stopAge() / 4.0F) : var3;
         float var5 = var1.stopping() ? (1.0F - var4) * 0.45F : (var3 < 1.0F ? (1.0F - var3) * (1.0F - var3) * 0.55F : 0.0F);
         if (!(var2 < 0.001F) || !(var5 <= 0.0F)) {
            iz.h var6 = x(var1.prop());
            float[] var7 = var1.track();
            Quaternionf var8 = new Quaternionf().rotationY((float)Math.toRadians(180.0F - var1.facing()));
            Quaternionf var9 = new Quaternionf(var8).rotateY(var7[4]);
            Quaternionf var10 = new Quaternionf(var9).rotateZ(var7[5]).rotateX(var7[3]);
            boolean var11 = "crypto_rock".equals(var1.prop());
            float var12 = var11 ? var1.avatarScale() : var2;
            Vector3f var13 = var8.transform(new Vector3f(var7[0] * var12, 0.0F, var7[2] * var12));
            Vec3 var14 = var1.feet().add(var13.x, var13.y, var13.z);
            Vector3f var15 = var9.transform(new Vector3f(0.0F, var7[1] * var2, 0.0F));
            ClientLevel var16 = Minecraft.getInstance().level;
            int var17 = 15;
            if (var16 != null) {
               BlockPos var18 = BlockPos.containing(var14.x, var14.y + 1.2, var14.z);
               var17 = var16.getRawBrightness(var18, var16.getSkyDarken());
            }

            b(var6, var17);
            B = new iz.t(
               var6,
               var14,
               var9,
               var10,
               var14.add(var15.x, var15.y, var15.z),
               var2,
               Math.clamp(0.42F - var7[1] * 0.9F, 0.0F, 0.5F) * Math.min(1.0F, var2),
               0.55F + 1.9F * var4,
               var5,
               var11 ? var1.age() + var1.stopAge() : -1.0F,
               var11 && ho.F("Emotes"),
               Math.round(170.0F * Z(var17))
            );
         }
      }
   }

   private static float Z(int var0) {
      return 0.22F + 0.78F * (float)Math.pow(var0 / 15.0, 1.3);
   }

   private static void b(iz.h var0, int var1) {
      if (var0.q != var1) {
         float var2 = Z(var1);

         for (int var3 = 0; var3 < var0.M.length; var3++) {
            int[] var4 = var0.Q.parts().get(var3).colors();
            int[] var5 = var0.M[var3];

            for (int var6 = 0; var6 < var4.length; var6++) {
               int var7 = Math.round((var4[var6] & 0xFF) * var2);
               var5[var6] = 0xFF000000 | var7 << 16 | var7 << 8 | var7;
            }
         }

         var0.q = var1;
      }
   }

   public static void i(fz var0, boolean var1) {
      iz.t var2 = B;
      if (var2 != null && !var1) {
         if (var2.puffAlpha > 0.0F) {
            var0.l(var2.ground.x, var2.ground.y + 0.02, var2.ground.z, 1.1 * var2.puff, 0.5 + 0.4 * var2.puff, vv.N(-1446155, var2.puffAlpha));
            var0.l(var2.ground.x, var2.ground.y + 0.03, var2.ground.z, 0.6 * var2.puff, 0.35, vv.N(-1, var2.puffAlpha * 0.7));
         }

         if (!(var2.scale < 0.001F)) {
            if (var2.shadow > 0.0F) {
               a(var0, var2);
            }

            List var3 = var2.prop.Q.parts();
            var0.L(var2.prop.H, var2.bodyOrigin, var2.bodyRotation, var2.scale, ((qu.l)var3.get(0)).vertices(), var2.prop.M[0]);
            var0.L(var2.prop.H, var2.ground, var2.carRotation, var2.scale, ((qu.l)var3.get(1)).vertices(), var2.prop.M[1]);
            he.Z("Emote prop triangles", var2.prop.Q.triangles());
            if (var2.effectsAge >= 0.0F) {
               h(var0, var2);
            }
         }
      }
   }

   private static void h(fz var0, iz.t var1) {
      float var2 = wl.t(var1.effectsAge);
      if (var2 >= 0.0F && var2 < 3.0F) {
         Vec3 var3 = l(var1, -0.3672F, 0.37802F, 0.43596F);
         float var4 = var2 / 3.0F;
         var0.K(var3.x, var3.y, var3.z, 0.34F * var1.scale * (1.0F - var4 * var4), var2 * 0.7F, vv.N(-11654, 1.0F - var4));
      }

      if (var2 >= 0.0F && var2 < 6.0F) {
         float var8 = var2 / 6.0F;
         var0.l(var1.ground.x, var1.ground.y + 0.02, var1.ground.z, (0.72F + 0.5F * var8) * var1.scale, 0.3F, vv.N(-4607064, 0.28F * (1.0F - var8)));
      }

      int var9 = var1.chipGray;
      int[] var10 = new int[wl.R.length / 5];
      Arrays.fill(var10, 0xFF000000 | var9 << 16 | var9 << 8 | var9);

      for (wl.h var6 : wl.u(var1.effectsAge)) {
         if (!(var6.scale() <= 0.0F)) {
            Quaternionf var7 = new Quaternionf(var1.carRotation).rotateXYZ(var6.spin(), var6.spin() * 0.7F, var6.spin() * 1.3F);
            var0.L(var1.prop.H, l(var1, var6.x(), var6.y(), var6.z()), var7, var6.scale() * var1.scale, wl.R, var10);
         }
      }

      if (var1.coins) {
         for (wl.y var12 : wl.P(var1.effectsAge)) {
            if (!(var12.scale() <= 0.0F)) {
               Quaternionf var13 = new Quaternionf(var1.carRotation).rotateY(var12.yaw()).rotateX(var12.pitch());
               ho.w(var0, ho.X.get(var12.kind()), l(var1, var12.x(), var12.y(), var12.z()), var13, var12.scale() * var1.scale);
            }
         }
      }
   }

   private static Vec3 l(iz.t var0, float var1, float var2, float var3) {
      Vector3f var4 = var0.carRotation.transform(new Vector3f(var1, var2, var3).mul(var0.scale));
      return var0.ground.add(var4.x, var4.y, var4.z);
   }

   private static void a(fz var0, iz.t var1) {
      int var2 = var1.effectsAge >= 0.0F ? 1 : 0;
      float var3 = 0.32F * var1.scale;
      float var4 = (var2 != 0 ? 0.66F : 0.86F) * var1.scale;
      float var5 = (var2 != 0 ? 0.66F : 2.25F) * var1.scale;
      float var6 = var2 != 0 ? 0.0F : 0.05F * var1.scale;
      int var7 = vv.N(-16777216, var1.shadow);
      byte var8 = 0;
      float[][] var9 = new float[][]{{-var4, var6 - var5}, {var4, var6 - var5}, {var4, var6 + var5}, {-var4, var6 + var5}};
      float[][] var10 = new float[][]{
         {-var4 - var3, var6 - var5 - var3}, {var4 + var3, var6 - var5 - var3}, {var4 + var3, var6 + var5 + var3}, {-var4 - var3, var6 + var5 + var3}
      };
      K(var0, var1, var9[3], var7, var9[2], var7, var9[1], var7, var9[0], var7);

      for (int var11 = 0; var11 < 4; var11++) {
         int var12 = (var11 + 1) % 4;
         K(var0, var1, var10[var11], var8, var10[var12], var8, var9[var12], var7, var9[var11], var7);
      }
   }

   private static void K(fz var0, iz.t var1, float[] var2, int var3, float[] var4, int var5, float[] var6, int var7, float[] var8, int var9) {
      I(var0, var1, var2, var3);
      I(var0, var1, var4, var5);
      I(var0, var1, var6, var7);
      I(var0, var1, var8, var9);
   }

   private static void I(fz var0, iz.t var1, float[] var2, int var3) {
      Vector3f var4 = var1.carRotation.transform(new Vector3f(var2[0], 0.0F, var2[1]));
      var0.vertex(var1.ground.x + var4.x, var1.ground.y + 0.015, var1.ground.z + var4.z, var3);
   }

   private static final class h {
      final qu.j Q;
      final RenderType H;
      final long z;
      final int[][] M;
      int q = -1;

      h(qu.j var1, RenderType var2, long var3) {
         this.Q = var1;
         this.H = var2;
         this.z = var3;
         this.M = new int[var1.parts().size()][];

         for (int var5 = 0; var5 < this.M.length; var5++) {
            this.M[var5] = new int[var1.parts().get(var5).colors().length];
         }
      }
   }

   private record t(
      iz.h prop,
      Vec3 ground,
      Quaternionf carRotation,
      Quaternionf bodyRotation,
      Vec3 bodyOrigin,
      float scale,
      float shadow,
      float puff,
      float puffAlpha,
      float effectsAge,
      boolean coins,
      int chipGray
   ) {
   }
}

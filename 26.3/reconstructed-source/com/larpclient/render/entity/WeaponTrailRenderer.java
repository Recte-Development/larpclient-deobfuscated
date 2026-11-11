package com.larpclient.render.entity;

import com.larpclient.module.render.AstralWeaponTrailsModule;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.model.ModelAxisFinder;
import com.larpclient.render.model.ModelVisualRenderer;
import com.larpclient.render.model.TrailMeshBuilder;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.inventory.ItemStackTokenCache;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.Direction.Axis;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3fc;

public final class WeaponTrailRenderer {
   private static final SwingTrail[] trailHistories = new SwingTrail[]{new SwingTrail(), new SwingTrail()};
   private static final ItemStackTokenCache[] bladeTrackers = new ItemStackTokenCache[]{new ItemStackTokenCache(), new ItemStackTokenCache()};
   private static final LinkedHashMap<BakedQuad, ModelAxisFinder.Axis> quadBladeCache = new LinkedHashMap<>();
   private static final IdentityHashMap<float[], ModelAxisFinder.Axis> modelBladeCache = new IdentityHashMap<>();
   private static int resourceGeneration = -1;
   private static boolean suspended;
   private static boolean warnedNoBlade;

   public static void reset() {
      for (SwingTrail var3 : trailHistories) {
         var3.clear();
      }

      for (ItemStackTokenCache var7 : bladeTrackers) {
         var7.clear();
      }

      quadBladeCache.clear();
      modelBladeCache.clear();
      warnedNoBlade = (boolean)0;
      suspended = (boolean)0;
   }

   public static void resetHand(InteractionHand var0) {
      int var1 = var0 == InteractionHand.MAIN_HAND ? 0 : 1;
      trailHistories[var1].clear();
      bladeTrackers[var1].clear();
   }

   public static int historySize() {
      return trailHistories[0].size() + trailHistories[1].size();
   }

   public static void setCameraMotion(double var0, double var2) {
      for (SwingTrail var7 : trailHistories) {
         var7.prune(var0, var2);
      }
   }

   private static boolean isTrailItem(ItemStack var0) {
      AstralWeaponTrailsModule var1 = AstralWeaponTrailsModule.getInstance();
      return var1 != null && var1.shouldRenderFirstPerson(var0);
   }

   public static void submitItem(
      ItemStackRenderState var0,
      PoseStack var1,
      SubmitNodeCollector var2,
      SubmitNodeCollector var3,
      ItemStack var4,
      ItemDisplayContext var5,
      InteractionHand var6,
      float var7,
      float var8,
      int var9,
      int var10,
      int var11
   ) {
      if (isTrailItem(var4) && var5.firstPerson() && !suspended) {
         WeaponTrailRenderer.BladeCapture var12 = new WeaponTrailRenderer.BladeCapture();
         SubmitNodeCollector var13 = (SubmitNodeCollector)Proxy.newProxyInstance(
            SubmitNodeCollector.class.getClassLoader(), new Class[]{SubmitNodeCollector.class}, (var2x, var3x, var4x) -> {
               if (var3x.getName().equals("submitItem")) {
                  try {
                     List var5x = ModelVisualRenderer.itemQuads(var4x[6]);
                     var12.collectQuads((PoseStack)var4x[0], var5x);
                  } catch (RuntimeException var7x) {
                     suspend(var7x);
                  }
               }

               try {
                  return var3x.invoke(var2, var4x);
               } catch (InvocationTargetException var6x) {
                  throw var6x.getCause();
               }
            }
         );
         var0.submit(var1, var13, var9, var10, var11);
         if (var12.worldBlade != null) {
            renderTrail(var6, var4, var12.modelBlade, var12.worldBlade, var7, var7 > 0.0F, var3);
         } else if (!warnedNoBlade) {
            warnedNoBlade = (boolean)1;
            LarpLog.debug("WeaponTrails", "No usable native sword blade quads; this model keeps ordinary rendering without a guessed trail attachment");
         }
      } else {
         var0.submit(var1, var2, var9, var10, var11);
      }
   }

   public static void submitModelBlade(
      InteractionHand var0, ItemStack var1, float[] var2, int var3, PoseStack var4, float var5, boolean var6, SubmitNodeCollector var7
   ) {
      if (isTrailItem(var1) && !suspended) {
         try {
            ModelAxisFinder.Axis var8 = modelBladeCache.get(var2);
            if (var8 == null) {
               var8 = ModelAxisFinder.fromVertices(var2);
               if (var8 == null) {
                  return;
               }

               if (modelBladeCache.size() >= 12) {
                  modelBladeCache.clear();
               }

               modelBladeCache.put(var2, var8);
            }

            renderTrail(var0, var1, var8, var8.transform(var4.last().pose(), var3), var5, var6, var7);
         } catch (RuntimeException var9) {
            suspend(var9);
         }
      }
   }

   private static void renderTrail(
      InteractionHand var0, ItemStack var1, ModelAxisFinder.Axis var2, ModelAxisFinder.Axis var3, float var4, boolean var5, SubmitNodeCollector var6
   ) {
      AstralWeaponTrailsModule var7 = AstralWeaponTrailsModule.getInstance();
      if (isTrailItem(var1) && !suspended) {
         long var8 = PerfProfiler.startTimer();

         try {
            if (resourceGeneration != PostProcessing.reloadCount) {
               reset();
               resourceGeneration = PostProcessing.reloadCount;
            }

            int var10 = var0 == InteractionHand.MAIN_HAND ? 0 : 1;
            SwingTrail var11 = trailHistories[var10];
            TrailMeshBuilder.TrailStyle var12 = var7.buildTrailStyle();
            PerfProfiler.addCounter("Weapon trail blade captures", 1L);
            PerfProfiler.addCounter("Weapon trail active captures", var5 && var4 > 0.0F ? 1L : 0L);
            PerfProfiler.setCounter(var10 == 0 ? "Weapon trail main swing (milli)" : "Weapon trail offhand swing (milli)", Math.round(var4 * 1000.0F));
            var11.addSample(
               bladeTrackers[var10].getToken(var1, var2), var3.root(), var3.tip(), TickTimer.getTime(), var4, var5, var12.duration(), var7.getUpdateInterval()
            );
            TrailMeshBuilder.TrailMesh var13 = TrailMeshBuilder.buildMesh(var11.snapshot(), TickTimer.getTime(), var12);
            PerfProfiler.setCounter("Weapon trail history", historySize());
            PerfProfiler.addCounter("Weapon trail vertices", var13.vertices());
            if (var13.vertices() != 0) {
               WeaponTrailRenderer.TrailRenderTypes.validatePipelines();
               if (!var13.body().addVertex()) {
                  submitRibbon(var6, var13.body(), false);
               }

               if (!var13.light().addVertex()) {
                  submitRibbon(var6, var13.light(), true);
               }

               return;
            }
         } catch (RuntimeException var17) {
            suspend(var17);
            return;
         } finally {
            PerfProfiler.endTimer("Weapon trails", var8);
         }
      }
   }

   private static void submitRibbon(SubmitNodeCollector var0, TrailMeshBuilder.VertexBuffer var1, boolean var2) {
      var0.order(var2 ? 3 : 2).submitCustomGeometry(new PoseStack(), WeaponTrailRenderer.TrailRenderTypes.getRenderType(var2), (var1x, var2x) -> {
         try {
            for (int var3 = 0; var3 < var1.getColor(); var3++) {
               var2x.addVertex(var1x, var1.getX(var3), var1.getY(var3), var1.getZ(var3)).setColor(var1.getColor(var3));
            }
         } catch (RuntimeException var4) {
            suspend(var4);
         }
      });
   }

   private static void suspend(RuntimeException var0) {
      for (SwingTrail var4 : trailHistories) {
         var4.clear();
      }

      if (!suspended) {
         LarpLog.error("WeaponTrails", "Native blade capture/submission failed; trails suspended until toggle or resource reload", var0);
      }

      suspended = (boolean)1;
   }

   private WeaponTrailRenderer() {
   }

   private static final class BladeCapture {
      ModelAxisFinder.Axis worldBlade;
      ModelAxisFinder.Axis modelBlade;

      void collectQuads(PoseStack var1, List<BakedQuad> var2) {
         if (!var2.isEmpty()) {
            BakedQuad var3 = (BakedQuad)var2.getFirst();
            ModelAxisFinder.Axis var4 = WeaponTrailRenderer.quadBladeCache.get(var3);
            if (var4 == null) {
               ArrayList var5 = new ArrayList(Math.min(4096, var2.size() * 4));

               for (BakedQuad var7 : var2) {
                  if (var7.direction().getAxis() != Axis.Z) {
                     for (int var8 = 0; var8 < 4 && var5.size() < 4096; var8++) {
                        Vector3fc var9 = var7.position(var8);
                        var5.add(new Vec3(var9.x(), var9.y(), var9.z()));
                     }
                  }
               }

               if (var5.size() < 4) {
                  for (BakedQuad var12 : var2) {
                     for (int var13 = 0; var13 < 4 && var5.size() < 4096; var13++) {
                        Vector3fc var14 = var12.position(var13);
                        var5.add(new Vec3(var14.x(), var14.y(), var14.z()));
                     }
                  }
               }

               var4 = ModelAxisFinder.fitPrincipalAxis(var5);
               if (var4 == null) {
                  return;
               }

               if (WeaponTrailRenderer.quadBladeCache.size() >= 32) {
                  WeaponTrailRenderer.quadBladeCache.remove(WeaponTrailRenderer.quadBladeCache.keySet().iterator().next());
               }

               WeaponTrailRenderer.quadBladeCache.put(var3, var4);
            }

            ModelAxisFinder.Axis var10 = var4.transform(var1.last().pose(), 1);
            if (this.worldBlade == null || var10.length() > this.worldBlade.length()) {
               this.worldBlade = var10;
               this.modelBlade = var4;
            }
         }
      }
   }

   private static final class TrailRenderTypes {
      private static final RenderType bODY = createRenderType(false);
      private static final RenderType lIGHT = createRenderType(true);
      private static int validatedGeneration = -1;

      private static RenderType createRenderType(boolean var0) {
         RenderPipeline var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.DEBUG_FILLED_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0 ? "weapon_trail_light" : "weapon_trail_body"))
            .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
            .withColorTargetState(new ColorTargetState(var0 ? BlendFunction.LIGHTNING : BlendFunction.TRANSLUCENT))
            .withCull(false)
            .build();
         return RenderType.create(var0 ? "larp_weapon_trail_light" : "larp_weapon_trail_body", RenderSetup.builder(var1).sortOnUpload().createRenderSetup());
      }

      static RenderType getRenderType(boolean var0) {
         return var0 ? lIGHT : bODY;
      }

      static void validatePipelines() {
         if (validatedGeneration != PostProcessing.reloadCount) {
            ClientPipelines.validatePipeline(bODY.pipeline());
            ClientPipelines.validatePipeline(lIGHT.pipeline());
            validatedGeneration = PostProcessing.reloadCount;
            LarpLog.debug("WeaponTrails", "Native depth-tested hand ribbon materials validated for generation {}", validatedGeneration);
         }
      }
   }
}

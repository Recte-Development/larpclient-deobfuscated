package com.larpclient;

import com.larpclient.module.AstralWeaponTrailsModule;
import com.larpclient.module.HalloweenModule;
import com.larpclient.module.SwordModelsModule;
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
import net.minecraft.client.Minecraft;
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

public final class ym {
   private static final _j[] F = new _j[]{new _j(), new _j()};
   private static final r_[] P = new r_[]{new r_(), new r_()};
   private static final LinkedHashMap<BakedQuad, ms.c> L = new LinkedHashMap<>();
   private static final IdentityHashMap<float[], ms.c> I = new IdentityHashMap<>();
   private static int C = -1;
   private static boolean y;
   private static boolean D;

   public static void s() {
      for (_j var3 : F) {
         var3.X();
      }

      for (r_ var7 : P) {
         var7.n();
      }

      L.clear();
      I.clear();
      D = (boolean)0;
      y = (boolean)0;
   }

   public static void t(InteractionHand var0) {
      int var1 = var0 == InteractionHand.MAIN_HAND ? 0 : 1;
      F[var1].X();
      P[var1].n();
   }

   public static int h() {
      return F[0].v() + F[1].v();
   }

   public static void I(double var0, double var2) {
      for (_j var7 : F) {
         var7.a(var0, var2);
      }
   }

   private static boolean r(ItemStack var0) {
      if (!HalloweenModule.p()) {
         AstralWeaponTrailsModule var2 = AstralWeaponTrailsModule.g();
         return var2 != null && var2.e(var0);
      } else {
         Minecraft var1 = Minecraft.getInstance();
         return SwordModelsModule.G(var0)
            && var1.level != null
            && var1.player != null
            && var1.player.isAlive()
            && !var1.player.isScoping()
            && var1.options.getCameraType().isFirstPerson();
      }
   }

   public static le.y F() {
      ol var0 = HalloweenModule.U().t();
      return new le.y("Astral", 0.22, 0.44, var0.D * 0.75, -7026494, -3046331, var0.y(), false, true, var0 == ol.R ? 3 : 2);
   }

   public static void v(
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
      if (r(var4) && var5.firstPerson() && !y) {
         ym.o var12 = new ym.o();
         SubmitNodeCollector var13 = (SubmitNodeCollector)Proxy.newProxyInstance(
            SubmitNodeCollector.class.getClassLoader(), new Class[]{SubmitNodeCollector.class}, (var2x, var3x, var4x) -> {
               if (var3x.getName().equals("submitItem")) {
                  try {
                     List var5x = (List)var4x[6];
                     var12.V((PoseStack)var4x[0], var5x);
                  } catch (RuntimeException var7x) {
                     V(var7x);
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
         if (var12.d != null) {
            I(var6, var4, var12.u, var12.d, var7, var7 > 0.0F, var3);
         } else if (!D) {
            D = (boolean)1;
            r4.g("WeaponTrails", "No usable native sword blade quads; this model keeps ordinary rendering without a guessed trail attachment");
         }
      } else {
         var0.submit(var1, var2, var9, var10, var11);
      }
   }

   public static void p(InteractionHand var0, ItemStack var1, float[] var2, int var3, PoseStack var4, float var5, boolean var6, SubmitNodeCollector var7) {
      if (r(var1) && !y) {
         try {
            ms.c var8 = I.get(var2);
            if (var8 == null) {
               var8 = ms.N(var2);
               if (var8 == null) {
                  return;
               }

               if (I.size() >= 12) {
                  I.clear();
               }

               I.put(var2, var8);
            }

            I(var0, var1, var8, var8.transform(var4.last().pose(), var3), var5, var6, var7);
         } catch (RuntimeException var9) {
            V(var9);
         }
      }
   }

   private static void I(InteractionHand var0, ItemStack var1, ms.c var2, ms.c var3, float var4, boolean var5, SubmitNodeCollector var6) {
      AstralWeaponTrailsModule var7 = AstralWeaponTrailsModule.g();
      if (r(var1) && !y) {
         long var8 = h_.U();

         try {
            if (C != jk.v) {
               s();
               C = jk.v;
            }

            int var10 = var0 == InteractionHand.MAIN_HAND ? 0 : 1;
            _j var11 = F[var10];
            le.y var12 = HalloweenModule.p() ? F() : var7.r();
            h_.A("Weapon trail blade captures", 1L);
            h_.A("Weapon trail active captures", var5 && var4 > 0.0F ? 1L : 0L);
            h_.S(var10 == 0 ? "Weapon trail main swing (milli)" : "Weapon trail offhand swing (milli)", Math.round(var4 * 1000.0F));
            var11.a(P[var10].Q(var1, var2), var3.root(), var3.tip(), ea.y(), var4, var5, var12.duration(), HalloweenModule.p() ? lo.H(48) : var7.L());
            le.a var13 = le.t(var11.e(), ea.y(), var12);
            h_.S("Weapon trail history", h());
            h_.A("Weapon trail vertices", var13.vertices());
            if (var13.vertices() != 0) {
               ym.q.w();
               if (!var13.body().R()) {
                  S(var6, var13.body(), false);
               }

               if (!var13.light().R()) {
                  S(var6, var13.light(), true);
               }

               return;
            }
         } catch (RuntimeException var17) {
            V(var17);
            return;
         } finally {
            h_.n("Weapon trails", var8);
         }
      }
   }

   private static void S(SubmitNodeCollector var0, le.q var1, boolean var2) {
      var0.order(var2 ? 3 : 2).submitCustomGeometry(new PoseStack(), ym.q.h(var2), (var1x, var2x) -> {
         try {
            for (int var3 = 0; var3 < var1.Q(); var3++) {
               var2x.addVertex(var1x, var1.d(var3), var1.G(var3), var1.u(var3)).setColor(var1.e(var3));
            }
         } catch (RuntimeException var4) {
            V(var4);
         }
      });
   }

   private static void V(RuntimeException var0) {
      for (_j var4 : F) {
         var4.X();
      }

      if (!y) {
         r4.r("WeaponTrails", "Native blade capture/submission failed; trails suspended until toggle or resource reload", var0);
      }

      y = (boolean)1;
   }

   private ym() {
   }

   private static final class o {
      ms.c d;
      ms.c u;

      void V(PoseStack var1, List<BakedQuad> var2) {
         if (!var2.isEmpty()) {
            BakedQuad var3 = (BakedQuad)var2.getFirst();
            ms.c var4 = ym.L.get(var3);
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

               var4 = ms.w(var5);
               if (var4 == null) {
                  return;
               }

               if (ym.L.size() >= 32) {
                  ym.L.remove(ym.L.keySet().iterator().next());
               }

               ym.L.put(var3, var4);
            }

            ms.c var10 = var4.transform(var1.last().pose(), 1);
            if (this.d == null || var10.length() > this.d.length()) {
               this.d = var10;
               this.u = var4;
            }
         }
      }
   }

   private static final class q {
      private static final RenderType j = S(false);
      private static final RenderType g = S(true);
      private static int h = -1;

      private static RenderType S(boolean var0) {
         RenderPipeline var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.DEBUG_FILLED_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0 ? "weapon_trail_light" : "weapon_trail_body"))
            .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
            .withColorTargetState(new ColorTargetState(var0 ? BlendFunction.LIGHTNING : BlendFunction.TRANSLUCENT))
            .withCull(false)
            .build();
         return RenderType.create(var0 ? "larp_weapon_trail_light" : "larp_weapon_trail_body", RenderSetup.builder(var1).sortOnUpload().createRenderSetup());
      }

      static RenderType h(boolean var0) {
         return var0 ? g : j;
      }

      static void w() {
         if (h != jk.v) {
            e4.U(j.pipeline());
            e4.U(g.pipeline());
            h = jk.v;
            r4.g("WeaponTrails", "Native depth-tested hand ribbon materials validated for generation {}", h);
         }
      }
   }
}

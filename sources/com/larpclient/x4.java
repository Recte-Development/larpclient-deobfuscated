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

public final class x4 {
   private static final t8[] I = new t8[]{new t8(), new t8()};
   private static final w1[] w = new w1[]{new w1(), new w1()};
   private static final LinkedHashMap<BakedQuad, h3.r> k = new LinkedHashMap<>();
   private static final IdentityHashMap<float[], h3.r> e = new IdentityHashMap<>();
   private static int Y = -1;
   private static boolean z;
   private static boolean H;

   public static void D() {
      for (t8 var3 : I) {
         var3.h();
      }

      for (w1 var7 : w) {
         var7.h();
      }

      k.clear();
      e.clear();
      H = (boolean)0;
      z = (boolean)0;
   }

   public static void Z(InteractionHand var0) {
      int var1 = var0 == InteractionHand.MAIN_HAND ? 0 : 1;
      I[var1].h();
      w[var1].h();
   }

   public static int z() {
      return I[0].W() + I[1].W();
   }

   public static void Q(double var0, double var2) {
      for (t8 var7 : I) {
         var7.i(var0, var2);
      }
   }

   private static boolean s(ItemStack var0) {
      if (!HalloweenModule.S()) {
         AstralWeaponTrailsModule var2 = AstralWeaponTrailsModule.s();
         return var2 != null && var2.h(var0);
      } else {
         Minecraft var1 = Minecraft.getInstance();
         return SwordModelsModule.q(var0)
            && var1.level != null
            && var1.player != null
            && var1.player.isAlive()
            && !var1.player.isScoping()
            && var1.options.getCameraType().isFirstPerson();
      }
   }

   public static xf.v x() {
      u8 var0 = HalloweenModule.X().s();
      return new xf.v("Astral", 0.22, 0.44, var0.w * 0.75, -7026494, -3046331, var0.O(), false, true, var0 == u8.S ? 3 : 2);
   }

   public static void O(
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
      if (s(var4) && var5.firstPerson() && !z) {
         x4.u var12 = new x4.u();
         SubmitNodeCollector var13 = (SubmitNodeCollector)Proxy.newProxyInstance(
            SubmitNodeCollector.class.getClassLoader(), new Class[]{SubmitNodeCollector.class}, (var2x, var3x, var4x) -> {
               if (var3x.getName().equals("submitItem")) {
                  try {
                     List var5x = (List)var4x[6];
                     var12.e((PoseStack)var4x[0], var5x);
                  } catch (RuntimeException var7x) {
                     x(var7x);
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
            x(var6, var4, var12.L, var12.d, var7, var7 > 0.0F, var3);
         } else if (!H) {
            H = (boolean)1;
            mt.U("WeaponTrails", "No usable native sword blade quads; this model keeps ordinary rendering without a guessed trail attachment");
         }
      } else {
         var0.submit(var1, var2, var9, var10, var11);
      }
   }

   public static void K(InteractionHand var0, ItemStack var1, float[] var2, int var3, PoseStack var4, float var5, boolean var6, SubmitNodeCollector var7) {
      if (s(var1) && !z) {
         try {
            h3.r var8 = e.get(var2);
            if (var8 == null) {
               var8 = h3.d(var2);
               if (var8 == null) {
                  return;
               }

               if (e.size() >= 12) {
                  e.clear();
               }

               e.put(var2, var8);
            }

            x(var0, var1, var8, var8.transform(var4.last().pose(), var3), var5, var6, var7);
         } catch (RuntimeException var9) {
            x(var9);
         }
      }
   }

   private static void x(InteractionHand var0, ItemStack var1, h3.r var2, h3.r var3, float var4, boolean var5, SubmitNodeCollector var6) {
      AstralWeaponTrailsModule var7 = AstralWeaponTrailsModule.s();
      if (s(var1) && !z) {
         long var8 = kx.G();

         try {
            if (Y != m5.k) {
               D();
               Y = m5.k;
            }

            int var10 = var0 == InteractionHand.MAIN_HAND ? 0 : 1;
            t8 var11 = I[var10];
            xf.v var12 = HalloweenModule.S() ? x() : var7.e();
            kx.U("Weapon trail blade captures", 1L);
            kx.U("Weapon trail active captures", var5 && var4 > 0.0F ? 1L : 0L);
            kx.s(var10 == 0 ? "Weapon trail main swing (milli)" : "Weapon trail offhand swing (milli)", Math.round(var4 * 1000.0F));
            var11.G(w[var10].q(var1, var2), var3.root(), var3.tip(), ty.K(), var4, var5, var12.duration(), HalloweenModule.S() ? _k.m(48) : var7.A());
            xf.n var13 = xf.O(var11.C(), ty.K(), var12);
            kx.s("Weapon trail history", z());
            kx.U("Weapon trail vertices", var13.vertices());
            if (var13.vertices() != 0) {
               x4.d.A();
               if (!var13.body().l()) {
                  S(var6, var13.body(), false);
               }

               if (!var13.light().l()) {
                  S(var6, var13.light(), true);
               }

               return;
            }
         } catch (RuntimeException var17) {
            x(var17);
            return;
         } finally {
            kx.u("Weapon trails", var8);
         }
      }
   }

   private static void S(SubmitNodeCollector var0, xf.j var1, boolean var2) {
      var0.order(var2 ? 3 : 2).submitCustomGeometry(new PoseStack(), x4.d.u(var2), (var1x, var2x) -> {
         try {
            for (int var3 = 0; var3 < var1.f(); var3++) {
               var2x.addVertex(var1x, var1.Y(var3), var1.o(var3), var1.k(var3)).setColor(var1.s(var3));
            }
         } catch (RuntimeException var4) {
            x(var4);
         }
      });
   }

   private static void x(RuntimeException var0) {
      for (t8 var4 : I) {
         var4.h();
      }

      if (!z) {
         mt.P("WeaponTrails", "Native blade capture/submission failed; trails suspended until toggle or resource reload", var0);
      }

      z = (boolean)1;
   }

   private x4() {
   }

   private static final class d {
      private static final RenderType K = m(false);
      private static final RenderType F = m(true);
      private static int A = -1;

      private static RenderType m(boolean var0) {
         RenderPipeline var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.DEBUG_FILLED_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0 ? "weapon_trail_light" : "weapon_trail_body"))
            .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
            .withColorTargetState(new ColorTargetState(var0 ? BlendFunction.LIGHTNING : BlendFunction.TRANSLUCENT))
            .withCull(false)
            .build();
         return RenderType.create(var0 ? "larp_weapon_trail_light" : "larp_weapon_trail_body", RenderSetup.builder(var1).sortOnUpload().createRenderSetup());
      }

      static RenderType u(boolean var0) {
         return var0 ? F : K;
      }

      static void A() {
         if (A != m5.k) {
            ea.z(K.pipeline());
            ea.z(F.pipeline());
            A = m5.k;
            mt.U("WeaponTrails", "Native depth-tested hand ribbon materials validated for generation {}", A);
         }
      }
   }

   private static final class u {
      h3.r d;
      h3.r L;

      void e(PoseStack var1, List<BakedQuad> var2) {
         if (!var2.isEmpty()) {
            BakedQuad var3 = (BakedQuad)var2.getFirst();
            h3.r var4 = x4.k.get(var3);
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

               var4 = h3.r(var5);
               if (var4 == null) {
                  return;
               }

               if (x4.k.size() >= 32) {
                  x4.k.remove(x4.k.keySet().iterator().next());
               }

               x4.k.put(var3, var4);
            }

            h3.r var10 = var4.transform(var1.last().pose(), 1);
            if (this.d == null || var10.length() > this.d.length()) {
               this.d = var10;
               this.L = var4;
            }
         }
      }
   }
}

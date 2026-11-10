package com.larpclient;

import com.larpclient.module.AstralWeaponTrailsModule;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.PoseStack;
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

public final class w {
   private static final sk[] x = new sk[]{new sk(), new sk()};
   private static final jq[] U = new jq[]{new jq(), new jq()};
   private static final LinkedHashMap<BakedQuad, z2.e> d = new LinkedHashMap<>();
   private static final IdentityHashMap<float[], z2.e> z = new IdentityHashMap<>();
   private static int F = -1;
   private static boolean p;
   private static boolean D;

   public static void e() {
      for (sk var3 : x) {
         var3.i();
      }

      for (jq var7 : U) {
         var7.o();
      }

      d.clear();
      z.clear();
      D = (boolean)0;
      p = (boolean)0;
   }

   public static void O(InteractionHand var0) {
      int var1 = var0 == InteractionHand.MAIN_HAND ? 0 : 1;
      x[var1].i();
      U[var1].o();
   }

   public static int c() {
      return x[0].h() + x[1].h();
   }

   public static void m(double var0, double var2) {
      for (sk var7 : x) {
         var7.l(var0, var2);
      }
   }

   private static boolean v(ItemStack var0) {
      AstralWeaponTrailsModule var1 = AstralWeaponTrailsModule.C();
      return var1 != null && var1.N(var0);
   }

   public static void l(
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
      if (v(var4) && var5.firstPerson() && !p) {
         w.h var12 = new w.h();
         SubmitNodeCollector var13 = (SubmitNodeCollector)Proxy.newProxyInstance(
            SubmitNodeCollector.class.getClassLoader(), new Class[]{SubmitNodeCollector.class}, (var2x, var3x, var4x) -> {
               if (var3x.getName().equals("submitItem")) {
                  try {
                     List var5x = j4.o(var4x[6]);
                     var12.h((PoseStack)var4x[0], var5x);
                  } catch (RuntimeException var7x) {
                     z(var7x);
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
         if (var12.m != null) {
            F(var6, var4, var12.Y, var12.m, var7, var7 > 0.0F, var3);
         } else if (!D) {
            D = (boolean)1;
            com.larpclient.h.U("WeaponTrails", "No usable native sword blade quads; this model keeps ordinary rendering without a guessed trail attachment");
         }
      } else {
         var0.submit(var1, var2, var9, var10, var11);
      }
   }

   public static void T(InteractionHand var0, ItemStack var1, float[] var2, int var3, PoseStack var4, float var5, boolean var6, SubmitNodeCollector var7) {
      if (v(var1) && !p) {
         try {
            z2.e var8 = z.get(var2);
            if (var8 == null) {
               var8 = z2.l(var2);
               if (var8 == null) {
                  return;
               }

               if (z.size() >= 12) {
                  z.clear();
               }

               z.put(var2, var8);
            }

            F(var0, var1, var8, var8.transform(var4.last().pose(), var3), var5, var6, var7);
         } catch (RuntimeException var9) {
            z(var9);
         }
      }
   }

   private static void F(InteractionHand var0, ItemStack var1, z2.e var2, z2.e var3, float var4, boolean var5, SubmitNodeCollector var6) {
      AstralWeaponTrailsModule var7 = AstralWeaponTrailsModule.C();
      if (v(var1) && !p) {
         long var8 = zj.m();

         try {
            if (F != wa.s) {
               e();
               F = wa.s;
            }

            int var10 = var0 == InteractionHand.MAIN_HAND ? 0 : 1;
            sk var11 = x[var10];
            z6.g var12 = var7.e();
            zj.K("Weapon trail blade captures", 1L);
            zj.K("Weapon trail active captures", var5 && var4 > 0.0F ? 1L : 0L);
            zj.s(var10 == 0 ? "Weapon trail main swing (milli)" : "Weapon trail offhand swing (milli)", Math.round(var4 * 1000.0F));
            var11.n(U[var10].g(var1, var2), var3.root(), var3.tip(), cf.F(), var4, var5, var12.duration(), var7.j());
            z6.s var13 = z6.e(var11.e(), cf.F(), var12);
            zj.s("Weapon trail history", c());
            zj.K("Weapon trail vertices", var13.vertices());
            if (var13.vertices() != 0) {
               w.t.P();
               if (!var13.body().Y()) {
                  Z(var6, var13.body(), false);
               }

               if (!var13.light().Y()) {
                  Z(var6, var13.light(), true);
               }

               return;
            }
         } catch (RuntimeException var17) {
            z(var17);
            return;
         } finally {
            zj.l("Weapon trails", var8);
         }
      }
   }

   private static void Z(SubmitNodeCollector var0, z6.b var1, boolean var2) {
      var0.order(var2 ? 3 : 2).submitCustomGeometry(new PoseStack(), w.t.w(var2), (var1x, var2x) -> {
         try {
            for (int var3 = 0; var3 < var1.L(); var3++) {
               var2x.addVertex(var1x, var1.f(var3), var1.C(var3), var1.s(var3)).setColor(var1.v(var3));
            }
         } catch (RuntimeException var4) {
            z(var4);
         }
      });
   }

   private static void z(RuntimeException var0) {
      for (sk var4 : x) {
         var4.i();
      }

      if (!p) {
         com.larpclient.h.m("WeaponTrails", "Native blade capture/submission failed; trails suspended until toggle or resource reload", var0);
      }

      p = (boolean)1;
   }

   private w() {
   }

   private static final class h {
      z2.e m;
      z2.e Y;

      void h(PoseStack var1, List<BakedQuad> var2) {
         if (!var2.isEmpty()) {
            BakedQuad var3 = (BakedQuad)var2.getFirst();
            z2.e var4 = w.d.get(var3);
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

               var4 = z2.W(var5);
               if (var4 == null) {
                  return;
               }

               if (w.d.size() >= 32) {
                  w.d.remove(w.d.keySet().iterator().next());
               }

               w.d.put(var3, var4);
            }

            z2.e var10 = var4.transform(var1.last().pose(), 1);
            if (this.m == null || var10.length() > this.m.length()) {
               this.m = var10;
               this.Y = var4;
            }
         }
      }
   }

   private static final class t {
      private static final RenderType U = v(false);
      private static final RenderType c = v(true);
      private static int Q = -1;

      private static RenderType v(boolean var0) {
         RenderPipeline var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.DEBUG_FILLED_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0 ? "weapon_trail_light" : "weapon_trail_body"))
            .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
            .withColorTargetState(new ColorTargetState(var0 ? BlendFunction.LIGHTNING : BlendFunction.TRANSLUCENT))
            .withCull(false)
            .build();
         return RenderType.create(var0 ? "larp_weapon_trail_light" : "larp_weapon_trail_body", RenderSetup.builder(var1).sortOnUpload().createRenderSetup());
      }

      static RenderType w(boolean var0) {
         return var0 ? c : U;
      }

      static void P() {
         if (Q != wa.s) {
            ai.U(U.pipeline());
            ai.U(c.pipeline());
            Q = wa.s;
            com.larpclient.h.U("WeaponTrails", "Native depth-tested hand ribbon materials validated for generation {}", Q);
         }
      }
   }
}

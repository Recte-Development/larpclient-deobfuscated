package com.larpclient;

import com.larpclient.module.AstralWeaponTrailsModule;
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

public final class dh {
   private static final dp[] g = new dp[]{new dp(), new dp()};
   private static final i[] O = new i[]{new i(), new i()};
   private static final LinkedHashMap<BakedQuad, op.r> Y = new LinkedHashMap<>();
   private static final IdentityHashMap<float[], op.r> J = new IdentityHashMap<>();
   private static int A = -1;
   private static boolean i;
   private static boolean n;

   public static void j() {
      for (dp var3 : g) {
         var3.j();
      }

      for (i var7 : O) {
         var7.c();
      }

      Y.clear();
      J.clear();
      n = (boolean)0;
      i = (boolean)0;
   }

   public static void q(InteractionHand var0) {
      int var1 = var0 == InteractionHand.MAIN_HAND ? 0 : 1;
      g[var1].j();
      O[var1].c();
   }

   public static int N() {
      return g[0].Z() + g[1].Z();
   }

   public static void b(double var0, double var2) {
      for (dp var7 : g) {
         var7.o(var0, var2);
      }
   }

   private static boolean l(ItemStack var0) {
      AstralWeaponTrailsModule var1 = AstralWeaponTrailsModule.J();
      return var1 != null && var1.X(var0);
   }

   public static void R(
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
      if (l(var4) && var5.firstPerson() && !i) {
         dh.d var12 = new dh.d();
         SubmitNodeCollector var13 = (SubmitNodeCollector)Proxy.newProxyInstance(
            SubmitNodeCollector.class.getClassLoader(), new Class[]{SubmitNodeCollector.class}, (var2x, var3x, var4x) -> {
               if (var3x.getName().equals("submitItem")) {
                  try {
                     List var5x = w7.T(var4x[6]);
                     var12.R((PoseStack)var4x[0], var5x);
                  } catch (RuntimeException var7x) {
                     g(var7x);
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
         if (var12.o != null) {
            W(var6, var4, var12.M, var12.o, var7, var7 > 0.0F, var3);
         } else if (!n) {
            n = (boolean)1;
            _q.c("WeaponTrails", "No usable native sword blade quads; this model keeps ordinary rendering without a guessed trail attachment");
         }
      } else {
         var0.submit(var1, var2, var9, var10, var11);
      }
   }

   public static void O(InteractionHand var0, ItemStack var1, float[] var2, int var3, PoseStack var4, float var5, boolean var6, SubmitNodeCollector var7) {
      if (l(var1) && !i) {
         try {
            op.r var8 = J.get(var2);
            if (var8 == null) {
               var8 = op.c(var2);
               if (var8 == null) {
                  return;
               }

               if (J.size() >= 12) {
                  J.clear();
               }

               J.put(var2, var8);
            }

            W(var0, var1, var8, var8.transform(var4.last().pose(), var3), var5, var6, var7);
         } catch (RuntimeException var9) {
            g(var9);
         }
      }
   }

   private static void W(InteractionHand var0, ItemStack var1, op.r var2, op.r var3, float var4, boolean var5, SubmitNodeCollector var6) {
      AstralWeaponTrailsModule var7 = AstralWeaponTrailsModule.J();
      if (l(var1) && !i) {
         long var8 = he.s();

         try {
            if (A != nl.q) {
               j();
               A = nl.q;
            }

            int var10 = var0 == InteractionHand.MAIN_HAND ? 0 : 1;
            dp var11 = g[var10];
            aj.n var12 = var7.Q();
            he.Z("Weapon trail blade captures", 1L);
            he.Z("Weapon trail active captures", var5 && var4 > 0.0F ? 1L : 0L);
            he.z(var10 == 0 ? "Weapon trail main swing (milli)" : "Weapon trail offhand swing (milli)", Math.round(var4 * 1000.0F));
            var11.C(O[var10].R(var1, var2), var3.root(), var3.tip(), hz.Q(), var4, var5, var12.duration(), var7.S());
            aj.u var13 = aj.d(var11.q(), hz.Q(), var12);
            he.z("Weapon trail history", N());
            he.Z("Weapon trail vertices", var13.vertices());
            if (var13.vertices() != 0) {
               dh.u.r();
               if (!var13.body().H()) {
                  t(var6, var13.body(), false);
               }

               if (!var13.light().H()) {
                  t(var6, var13.light(), true);
               }

               return;
            }
         } catch (RuntimeException var17) {
            g(var17);
            return;
         } finally {
            he.T("Weapon trails", var8);
         }
      }
   }

   private static void t(SubmitNodeCollector var0, aj.k var1, boolean var2) {
      var0.order(var2 ? 3 : 2).submitCustomGeometry(new PoseStack(), dh.u.h(var2), (var1x, var2x) -> {
         try {
            for (int var3 = 0; var3 < var1.A(); var3++) {
               var2x.addVertex(var1x, var1.J(var3), var1.v(var3), var1.k(var3)).setColor(var1.A(var3));
            }
         } catch (RuntimeException var4) {
            g(var4);
         }
      });
   }

   private static void g(RuntimeException var0) {
      for (dp var4 : g) {
         var4.j();
      }

      if (!i) {
         _q.o("WeaponTrails", "Native blade capture/submission failed; trails suspended until toggle or resource reload", var0);
      }

      i = (boolean)1;
   }

   private dh() {
   }

   private static final class d {
      op.r o;
      op.r M;

      void R(PoseStack var1, List<BakedQuad> var2) {
         if (!var2.isEmpty()) {
            BakedQuad var3 = (BakedQuad)var2.getFirst();
            op.r var4 = dh.Y.get(var3);
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

               var4 = op.L(var5);
               if (var4 == null) {
                  return;
               }

               if (dh.Y.size() >= 32) {
                  dh.Y.remove(dh.Y.keySet().iterator().next());
               }

               dh.Y.put(var3, var4);
            }

            op.r var10 = var4.transform(var1.last().pose(), 1);
            if (this.o == null || var10.length() > this.o.length()) {
               this.o = var10;
               this.M = var4;
            }
         }
      }
   }

   private static final class u {
      private static final RenderType E = H(false);
      private static final RenderType p = H(true);
      private static int q = -1;

      private static RenderType H(boolean var0) {
         RenderPipeline var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.DEBUG_FILLED_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0 ? "weapon_trail_light" : "weapon_trail_body"))
            .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
            .withColorTargetState(new ColorTargetState(var0 ? BlendFunction.LIGHTNING : BlendFunction.TRANSLUCENT))
            .withCull(false)
            .build();
         return RenderType.create(var0 ? "larp_weapon_trail_light" : "larp_weapon_trail_body", RenderSetup.builder(var1).sortOnUpload().createRenderSetup());
      }

      static RenderType h(boolean var0) {
         return var0 ? p : E;
      }

      static void r() {
         if (q != nl.q) {
            se.j(E.pipeline());
            se.j(p.pipeline());
            q = nl.q;
            _q.c("WeaponTrails", "Native depth-tested hand ribbon materials validated for generation {}", q);
         }
      }
   }
}

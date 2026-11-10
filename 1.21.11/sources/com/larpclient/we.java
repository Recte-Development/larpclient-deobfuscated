package com.larpclient;

import com.larpclient.module.AstralWeaponTrailsModule;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.class_10444;
import net.minecraft.class_10799;
import net.minecraft.class_11659;
import net.minecraft.class_12247;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_1921;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_777;
import net.minecraft.class_811;
import net.minecraft.class_2350.class_2351;
import org.joml.Vector3fc;

public final class we {
   private static final fb[] M = new fb[]{new fb(), new fb()};
   private static final uy[] t = new uy[]{new uy(), new uy()};
   private static final LinkedHashMap<class_777, ch.r> k = new LinkedHashMap<>();
   private static final IdentityHashMap<float[], ch.r> j = new IdentityHashMap<>();
   private static int u = -1;
   private static boolean F;
   private static boolean h;

   public static void K() {
      for (fb var3 : M) {
         var3.p();
      }

      for (uy var7 : t) {
         var7.g();
      }

      k.clear();
      j.clear();
      h = (boolean)0;
      F = (boolean)0;
   }

   public static void t(class_1268 var0) {
      int var1 = var0 == class_1268.field_5808 ? 0 : 1;
      M[var1].p();
      t[var1].g();
   }

   public static int h() {
      return M[0].A() + M[1].A();
   }

   public static void n(double var0, double var2) {
      for (fb var7 : M) {
         var7.x(var0, var2);
      }
   }

   private static boolean u(class_1799 var0) {
      AstralWeaponTrailsModule var1 = AstralWeaponTrailsModule.k();
      return var1 != null && var1.e(var0);
   }

   public static void J(
      class_10444 var0,
      class_4587 var1,
      class_11659 var2,
      class_11659 var3,
      class_1799 var4,
      class_811 var5,
      class_1268 var6,
      float var7,
      float var8,
      int var9,
      int var10,
      int var11
   ) {
      if (u(var4) && var5.method_29998() && !F) {
         we.k var12 = new we.k();
         class_11659 var13 = (class_11659)Proxy.newProxyInstance(class_11659.class.getClassLoader(), new Class[]{class_11659.class}, (var2x, var3x, var4x) -> {
            if (var3x.getName().equals("submitItem")) {
               try {
                  List var5x = gv.z(var4x[6]);
                  var12.q((class_4587)var4x[0], var5x);
               } catch (RuntimeException var7x) {
                  l(var7x);
               }
            }

            try {
               return var3x.invoke(var2, var4x);
            } catch (InvocationTargetException var6x) {
               throw var6x.getCause();
            }
         });
         var0.method_65604(var1, var13, var9, var10, var11);
         if (var12.m != null) {
            S(var6, var4, var12.j, var12.m, var7, var7 > 0.0F, var3);
         } else if (!h) {
            h = (boolean)1;
            q8.v("WeaponTrails", "No usable native sword blade quads; this model keeps ordinary rendering without a guessed trail attachment");
         }
      } else {
         var0.method_65604(var1, var2, var9, var10, var11);
      }
   }

   public static void P(class_1268 var0, class_1799 var1, float[] var2, int var3, class_4587 var4, float var5, boolean var6, class_11659 var7) {
      if (u(var1) && !F) {
         try {
            ch.r var8 = j.get(var2);
            if (var8 == null) {
               var8 = ch.s(var2);
               if (var8 == null) {
                  return;
               }

               if (j.size() >= 12) {
                  j.clear();
               }

               j.put(var2, var8);
            }

            S(var0, var1, var8, var8.transform(var4.method_23760().method_23761(), var3), var5, var6, var7);
         } catch (RuntimeException var9) {
            l(var9);
         }
      }
   }

   private static void S(class_1268 var0, class_1799 var1, ch.r var2, ch.r var3, float var4, boolean var5, class_11659 var6) {
      AstralWeaponTrailsModule var7 = AstralWeaponTrailsModule.k();
      if (u(var1) && !F) {
         long var8 = c5.X();

         try {
            if (u != t_.g) {
               K();
               u = t_.g;
            }

            int var10 = var0 == class_1268.field_5808 ? 0 : 1;
            fb var11 = M[var10];
            j3.b var12 = var7.o();
            c5.V("Weapon trail blade captures", 1L);
            c5.V("Weapon trail active captures", var5 && var4 > 0.0F ? 1L : 0L);
            c5.L(var10 == 0 ? "Weapon trail main swing (milli)" : "Weapon trail offhand swing (milli)", Math.round(var4 * 1000.0F));
            var11.r(t[var10].i(var1, var2), var3.root(), var3.tip(), qo.x(), var4, var5, var12.duration(), var7.i());
            j3.m var13 = j3.v(var11.K(), qo.x(), var12);
            c5.L("Weapon trail history", h());
            c5.V("Weapon trail vertices", var13.vertices());
            if (var13.vertices() != 0) {
               we.p.m();
               if (!var13.body().Q()) {
                  s(var6, var13.body(), false);
               }

               if (!var13.light().Q()) {
                  s(var6, var13.light(), true);
               }

               return;
            }
         } catch (RuntimeException var17) {
            l(var17);
            return;
         } finally {
            c5.w("Weapon trails", var8);
         }
      }
   }

   private static void s(class_11659 var0, j3.p var1, boolean var2) {
      var0.method_73529(var2 ? 3 : 2).method_73483(new class_4587(), we.p.e(var2), (var1x, var2x) -> {
         try {
            for (int var3 = 0; var3 < var1.C(); var3++) {
               var2x.method_56824(var1x, var1.z(var3), var1.h(var3), var1.l(var3)).method_39415(var1.p(var3));
            }
         } catch (RuntimeException var4) {
            l(var4);
         }
      });
   }

   private static void l(RuntimeException var0) {
      for (fb var4 : M) {
         var4.p();
      }

      if (!F) {
         q8.u("WeaponTrails", "Native blade capture/submission failed; trails suspended until toggle or resource reload", var0);
      }

      F = (boolean)1;
   }

   private we() {
   }

   private static final class k {
      ch.r m;
      ch.r j;

      void q(class_4587 var1, List<class_777> var2) {
         if (!var2.isEmpty()) {
            class_777 var3 = (class_777)var2.getFirst();
            ch.r var4 = we.k.get(var3);
            if (var4 == null) {
               ArrayList var5 = new ArrayList(Math.min(4096, var2.size() * 4));

               for (class_777 var7 : var2) {
                  if (var7.comp_3723().method_10166() != class_2351.field_11051) {
                     for (int var8 = 0; var8 < 4 && var5.size() < 4096; var8++) {
                        Vector3fc var9 = var7.method_76648(var8);
                        var5.add(new class_243(var9.x(), var9.y(), var9.z()));
                     }
                  }
               }

               if (var5.size() < 4) {
                  for (class_777 var12 : var2) {
                     for (int var13 = 0; var13 < 4 && var5.size() < 4096; var13++) {
                        Vector3fc var14 = var12.method_76648(var13);
                        var5.add(new class_243(var14.x(), var14.y(), var14.z()));
                     }
                  }
               }

               var4 = ch.y(var5);
               if (var4 == null) {
                  return;
               }

               if (we.k.size() >= 32) {
                  we.k.remove(we.k.keySet().iterator().next());
               }

               we.k.put(var3, var4);
            }

            ch.r var10 = var4.transform(var1.method_23760().method_23761(), 1);
            if (this.m == null || var10.length() > this.m.length()) {
               this.m = var10;
               this.j = var4;
            }
         }
      }
   }

   private static final class p {
      private static final class_1921 a = i(false);
      private static final class_1921 C = i(true);
      private static int E = -1;

      private static class_1921 i(boolean var0) {
         RenderPipeline var1 = RenderPipeline.builder(new Snippet[]{class_10799.field_56860})
            .withLocation(class_2960.method_60655("larp-client", var0 ? "weapon_trail_light" : "weapon_trail_body"))
            .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
            .withDepthWrite(false)
            .withBlend(var0 ? BlendFunction.LIGHTNING : BlendFunction.TRANSLUCENT)
            .withCull(false)
            .build();
         return class_1921.method_75940(
            var0 ? "larp_weapon_trail_light" : "larp_weapon_trail_body", class_12247.method_75927(var1).method_75937().method_75938()
         );
      }

      static class_1921 e(boolean var0) {
         return var0 ? C : a;
      }

      static void m() {
         if (E != t_.g) {
            f2.a(a.method_73243());
            f2.a(C.method_73243());
            E = t_.g;
            q8.v("WeaponTrails", "Native depth-tested hand ribbon materials validated for generation {}", E);
         }
      }
   }
}

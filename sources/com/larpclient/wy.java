package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.HalloweenModule;
import com.mojang.blaze3d.pipeline.RenderTarget;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;

public final class wy implements AutoCloseable {
   private static wy S;
   private static final Set<String> l = new HashSet<>();
   private static int C = -1;
   private static boolean h;
   private final e_ G = new e_();
   private final Map<String, ec> V = new HashMap<>();
   private ec b;
   private String t;
   private int o = -1;
   private double e = Double.NaN;
   private boolean M;

   public wy() {
      S = this;
   }

   public static boolean X(String var0) {
      return h || C != m5.k || !l.contains(var0);
   }

   public static boolean q() {
      return X("Nebula");
   }

   public static void e() {
      h = (boolean)1;
   }

   public static void s() {
      if (S != null) {
         S.M = (boolean)0;
      }

      if (h || C != m5.k) {
         l.clear();
         C = m5.k;
         h = (boolean)0;
         if (S != null) {
            S.h();
         }
      }

      CustomSkyModule var0 = CustomSkyModule.Y();
      if (S != null && (!HalloweenModule.f() && (var0 == null || !var0.isOperational()) || Minecraft.getInstance().level == null) && S.b != null) {
         S.close();
         mt.K("Sky", "Sky renderer disabled; GPU resources released");
      }
   }

   public static boolean a() {
      return S != null && S.M && HalloweenModule.f() && X("Harvest Moon");
   }

   public boolean t(RenderTarget var1) {
      this.M = (boolean)0;
      if (HalloweenModule.f() && X("Harvest Moon")) {
         long var2 = kx.G();

         try {
            if (this.o != m5.k) {
               this.h();
            }

            if (!"Harvest Moon".equals(this.t)) {
               if (this.b != null) {
                  this.b.close();
               }

               this.t = "Harvest Moon";
               this.e = Double.NaN;
               this.b = this.V.computeIfAbsent(this.t, var0 -> new o7());
               mt.K("Halloween", "Harvest Moon owns the sky; existing selections retained");
            }

            double var4 = ty.K();
            this.G.R = Double.isNaN(this.e) ? 0.0 : Math.clamp(var4 - this.e, 0.0, 0.1);
            this.e = var4;
            this.G.F = var1;
            this.b.render(this.G);
            this.M = (boolean)1;
            return true;
         } catch (RuntimeException | IOException var10) {
            l.add("Harvest Moon");
            C = m5.k;
            this.close();
            mt.P("Halloween", "Harvest Moon shader failed; existing or native sky restored until F3+T or toggle", var10);
            return false;
         } finally {
            this.G.F = null;
            kx.u("Harvest Moon CPU", var2);
         }
      } else {
         return false;
      }
   }

   public boolean k(RenderTarget var1, CustomSkyModule var2) {
      if (!X(var2.h())) {
         return false;
      }

      long var3 = kx.G();

      try {
         if (this.o != m5.k) {
            this.h();
         }

         if (!var2.h().equals(this.t)) {
            if (this.b != null) {
               this.b.close();
            }

            this.e = Double.NaN;
            this.t = var2.h();
            this.b = this.V.computeIfAbsent(this.t, var0 -> {
               return switch (var0) {
                  case "Ocean" -> new o9();
                  case "Israel Flag" -> new og();
                  default -> new os();
               };
            });
            mt.K("Sky", "Sky mode switched: {}; resource generation={}", this.t, this.o);
         }

         double var5 = ty.K();
         this.G.R = Double.isNaN(this.e) ? 0.0 : Math.clamp(var5 - this.e, 0.0, 0.1);
         this.e = var5;
         this.G.F = var1;
         this.G.x = var2;
         this.b.render(this.G);
         return true;
      } catch (RuntimeException | IOException var11) {
         l.add(var2.h());
         C = m5.k;
         this.close();
         mt.P("Sky", var2.h() + " failed; using vanilla sky until F3+T or disable/enable; generation=" + this.o, var11);
         return false;
      } finally {
         this.G.F = null;
         this.G.x = null;
         kx.u("Sky", var3);
      }
   }

   private void h() {
      for (ec var2 : this.V.values()) {
         var2.reload();
      }

      this.G.t.close();
      this.o = m5.k;
      this.e = Double.NaN;
   }

   @Override
   public void close() {
      this.M = (boolean)0;

      for (ec var2 : this.V.values()) {
         var2.close();
      }

      this.b = null;
      this.G.t.close();
      this.t = null;
      this.e = Double.NaN;
   }
}

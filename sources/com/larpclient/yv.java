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

public final class yv implements AutoCloseable {
   private static yv z;
   private static final Set<String> p = new HashSet<>();
   private static int P = -1;
   private static boolean T;
   private final lb m = new lb();
   private final Map<String, _1> v = new HashMap<>();
   private _1 B;
   private String R;
   private int V = -1;
   private double u = Double.NaN;
   private boolean H;

   public yv() {
      z = this;
   }

   public static boolean Z(String var0) {
      return T || P != jk.v || !p.contains(var0);
   }

   public static boolean G() {
      return Z("Nebula");
   }

   public static void h() {
      T = (boolean)1;
   }

   public static void B() {
      if (z != null) {
         z.H = (boolean)0;
      }

      if (T || P != jk.v) {
         p.clear();
         P = jk.v;
         T = (boolean)0;
         if (z != null) {
            z.x();
         }
      }

      CustomSkyModule var0 = CustomSkyModule.Y();
      if (z != null && (!HalloweenModule.U() && (var0 == null || !var0.isOperational()) || Minecraft.getInstance().level == null) && z.B != null) {
         z.close();
         r4.V("Sky", "Sky renderer disabled; GPU resources released");
      }
   }

   public static boolean K() {
      return z != null && z.H && HalloweenModule.U() && Z("Harvest Moon");
   }

   public boolean E(RenderTarget var1) {
      this.H = (boolean)0;
      if (HalloweenModule.U() && Z("Harvest Moon")) {
         long var2 = h_.U();

         try {
            if (this.V != jk.v) {
               this.x();
            }

            if (!"Harvest Moon".equals(this.R)) {
               if (this.B != null) {
                  this.B.close();
               }

               this.R = "Harvest Moon";
               this.u = Double.NaN;
               this.B = this.v.computeIfAbsent(this.R, var0 -> new uk());
               r4.V("Halloween", "Harvest Moon owns the sky; existing selections retained");
            }

            double var4 = ea.y();
            this.m.N = Double.isNaN(this.u) ? 0.0 : Math.clamp(var4 - this.u, 0.0, 0.1);
            this.u = var4;
            this.m.x = var1;
            this.B.render(this.m);
            this.H = (boolean)1;
            return true;
         } catch (RuntimeException | IOException var10) {
            p.add("Harvest Moon");
            P = jk.v;
            this.close();
            r4.r("Halloween", "Harvest Moon shader failed; existing or native sky restored until F3+T or toggle", var10);
            return false;
         } finally {
            this.m.x = null;
            h_.n("Harvest Moon CPU", var2);
         }
      } else {
         return false;
      }
   }

   public boolean y(RenderTarget var1, CustomSkyModule var2) {
      if (!Z(var2.Q())) {
         return false;
      }

      long var3 = h_.U();

      try {
         if (this.V != jk.v) {
            this.x();
         }

         if (!var2.Q().equals(this.R)) {
            if (this.B != null) {
               this.B.close();
            }

            this.u = Double.NaN;
            this.R = var2.Q();
            this.B = this.v.computeIfAbsent(this.R, var0 -> {
               return switch (var0) {
                  case "Ocean" -> new u0();
                  case "Israel Flag" -> new uh();
                  default -> new us();
               };
            });
            r4.V("Sky", "Sky mode switched: {}; resource generation={}", this.R, this.V);
         }

         double var5 = ea.y();
         this.m.N = Double.isNaN(this.u) ? 0.0 : Math.clamp(var5 - this.u, 0.0, 0.1);
         this.u = var5;
         this.m.x = var1;
         this.m.o = var2;
         this.B.render(this.m);
         return true;
      } catch (RuntimeException | IOException var11) {
         p.add(var2.Q());
         P = jk.v;
         this.close();
         r4.r("Sky", var2.Q() + " failed; using vanilla sky until F3+T or disable/enable; generation=" + this.V, var11);
         return false;
      } finally {
         this.m.x = null;
         this.m.o = null;
         h_.n("Sky", var3);
      }
   }

   private void x() {
      for (_1 var2 : this.v.values()) {
         var2.reload();
      }

      this.m.d.close();
      this.V = jk.v;
      this.u = Double.NaN;
   }

   @Override
   public void close() {
      this.H = (boolean)0;

      for (_1 var2 : this.v.values()) {
         var2.close();
      }

      this.B = null;
      this.m.d.close();
      this.R = null;
      this.u = Double.NaN;
   }
}

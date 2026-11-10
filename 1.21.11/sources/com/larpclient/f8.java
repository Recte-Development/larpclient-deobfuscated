package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_276;
import net.minecraft.class_310;

public final class f8 implements AutoCloseable {
   private static f8 j;
   private static final Set<String> g = new HashSet<>();
   private static int E = -1;
   private static boolean f;
   private final gx F = new gx();
   private final Map<String, gt> M = new HashMap<>();
   private gt t;
   private String h;
   private int q = -1;
   private double y = Double.NaN;

   public f8() {
      j = this;
   }

   public static boolean Y(String var0) {
      return f || E != t_.g || !g.contains(var0);
   }

   public static boolean P() {
      return Y("Nebula");
   }

   public static void R() {
      f = (boolean)1;
   }

   public static void m() {
      if (f || E != t_.g) {
         g.clear();
         E = t_.g;
         f = (boolean)0;
         if (j != null) {
            j.x();
         }
      }

      CustomSkyModule var0 = CustomSkyModule.M();
      if (j != null && (var0 == null || !var0.isOperational() || class_310.method_1551().field_1687 == null) && j.t != null) {
         j.close();
         q8.G("Sky", "Sky renderer disabled; GPU resources released");
      }
   }

   public boolean J(class_276 var1, CustomSkyModule var2) {
      if (!Y(var2.V())) {
         return false;
      }

      long var3 = c5.X();

      try {
         if (this.q != t_.g) {
            this.x();
         }

         if (!var2.V().equals(this.h)) {
            if (this.t != null) {
               this.t.close();
            }

            this.y = Double.NaN;
            this.h = var2.V();
            this.t = this.M.computeIfAbsent(this.h, var0 -> {
               return switch (var0) {
                  case "Ocean" -> new iu();
                  case "Israel Flag" -> new i4();
                  default -> new ia();
               };
            });
            q8.G("Sky", "Sky mode switched: {}; resource generation={}", this.h, this.q);
         }

         double var5 = qo.x();
         this.F.t = Double.isNaN(this.y) ? 0.0 : Math.clamp(var5 - this.y, 0.0, 0.1);
         this.y = var5;
         this.F.X = var1;
         this.F.w = var2;
         this.t.render(this.F);
         return true;
      } catch (RuntimeException | IOException var11) {
         g.add(var2.V());
         E = t_.g;
         this.close();
         q8.u("Sky", var2.V() + " failed; using vanilla sky until F3+T or disable/enable; generation=" + this.q, var11);
         return false;
      } finally {
         this.F.X = null;
         this.F.w = null;
         c5.w("Sky", var3);
      }
   }

   private void x() {
      for (gt var2 : this.M.values()) {
         var2.reload();
      }

      this.F.z.close();
      this.q = t_.g;
      this.y = Double.NaN;
   }

   @Override
   public void close() {
      for (gt var2 : this.M.values()) {
         var2.close();
      }

      this.t = null;
      this.F.z.close();
      this.h = null;
      this.y = Double.NaN;
   }
}

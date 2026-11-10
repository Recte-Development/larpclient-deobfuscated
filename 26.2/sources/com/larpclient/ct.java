package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.mojang.blaze3d.pipeline.RenderTarget;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;

public final class ct implements AutoCloseable {
   private static ct O;
   private static final Set<String> Q = new HashSet<>();
   private static int x = -1;
   private static boolean q;
   private final wj c = new wj();
   private final Map<String, tp> t = new HashMap<>();
   private tp U;
   private String a;
   private int K = -1;
   private double Y = Double.NaN;

   public ct() {
      O = this;
   }

   public static boolean F(String var0) {
      return q || x != wa.s || !Q.contains(var0);
   }

   public static boolean K() {
      return F("Nebula");
   }

   public static void S() {
      q = (boolean)1;
   }

   public static void M() {
      if (q || x != wa.s) {
         Q.clear();
         x = wa.s;
         q = (boolean)0;
         if (O != null) {
            O.W();
         }
      }

      CustomSkyModule var0 = CustomSkyModule.K();
      if (O != null && (var0 == null || !var0.isOperational() || Minecraft.getInstance().level == null) && O.U != null) {
         O.close();
         h.A("Sky", "Sky renderer disabled; GPU resources released");
      }
   }

   public boolean t(RenderTarget var1, CustomSkyModule var2) {
      if (!F(var2.o())) {
         return false;
      }

      long var3 = zj.m();

      try {
         if (this.K != wa.s) {
            this.W();
         }

         if (!var2.o().equals(this.a)) {
            if (this.U != null) {
               this.U.close();
            }

            this.Y = Double.NaN;
            this.a = var2.o();
            this.U = this.t.computeIfAbsent(this.a, var0 -> {
               return switch (var0) {
                  case "Ocean" -> new zz();
                  case "Israel Flag" -> new zo();
                  default -> new zb();
               };
            });
            h.A("Sky", "Sky mode switched: {}; resource generation={}", this.a, this.K);
         }

         double var5 = cf.F();
         this.c.k = Double.isNaN(this.Y) ? 0.0 : Math.clamp(var5 - this.Y, 0.0, 0.1);
         this.Y = var5;
         this.c.S = var1;
         this.c.n = var2;
         this.U.render(this.c);
         return true;
      } catch (RuntimeException | IOException var11) {
         Q.add(var2.o());
         x = wa.s;
         this.close();
         h.m("Sky", var2.o() + " failed; using vanilla sky until F3+T or disable/enable; generation=" + this.K, var11);
         return false;
      } finally {
         this.c.S = null;
         this.c.n = null;
         zj.l("Sky", var3);
      }
   }

   private void W() {
      for (tp var2 : this.t.values()) {
         var2.reload();
      }

      this.c.M.close();
      this.K = wa.s;
      this.Y = Double.NaN;
   }

   @Override
   public void close() {
      for (tp var2 : this.t.values()) {
         var2.close();
      }

      this.U = null;
      this.c.M.close();
      this.a = null;
      this.Y = Double.NaN;
   }
}

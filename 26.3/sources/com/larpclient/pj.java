package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.mojang.blaze3d.pipeline.RenderTarget;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;

public final class pj implements AutoCloseable {
   private static pj T;
   private static final Set<String> Q = new HashSet<>();
   private static int F = -1;
   private static boolean f;
   private final pa q = new pa();
   private final Map<String, d0> E = new HashMap<>();
   private d0 h;
   private String l;
   private int u = -1;
   private double D = Double.NaN;

   public pj() {
      T = this;
   }

   public static boolean Q(String var0) {
      return f || F != nl.q || !Q.contains(var0);
   }

   public static boolean T() {
      return Q("Nebula");
   }

   public static void P() {
      f = (boolean)1;
   }

   public static void D() {
      if (f || F != nl.q) {
         Q.clear();
         F = nl.q;
         f = (boolean)0;
         if (T != null) {
            T.Y();
         }
      }

      CustomSkyModule var0 = CustomSkyModule.p();
      if (T != null && (var0 == null || !var0.isOperational() || Minecraft.getInstance().level == null) && T.h != null) {
         T.close();
         _q.z("Sky", "Sky renderer disabled; GPU resources released");
      }
   }

   public boolean U(RenderTarget var1, CustomSkyModule var2) {
      if (!Q(var2.E())) {
         return false;
      }

      long var3 = he.s();

      try {
         if (this.u != nl.q) {
            this.Y();
         }

         if (!var2.E().equals(this.l)) {
            if (this.h != null) {
               this.h.close();
            }

            this.D = Double.NaN;
            this.l = var2.E();
            this.h = this.E.computeIfAbsent(this.l, var0 -> {
               return switch (var0) {
                  case "Ocean" -> new wn();
                  case "Israel Flag" -> new w0();
                  default -> new wt();
               };
            });
            _q.z("Sky", "Sky mode switched: {}; resource generation={}", this.l, this.u);
         }

         double var5 = hz.Q();
         this.q.J = Double.isNaN(this.D) ? 0.0 : Math.clamp(var5 - this.D, 0.0, 0.1);
         this.D = var5;
         this.q.V = var1;
         this.q.e = var2;
         this.h.render(this.q);
         return true;
      } catch (RuntimeException | IOException var11) {
         Q.add(var2.E());
         F = nl.q;
         this.close();
         _q.o("Sky", var2.E() + " failed; using vanilla sky until F3+T or disable/enable; generation=" + this.u, var11);
         return false;
      } finally {
         this.q.V = null;
         this.q.e = null;
         he.T("Sky", var3);
      }
   }

   private void Y() {
      for (d0 var2 : this.E.values()) {
         var2.reload();
      }

      this.q.D.close();
      this.u = nl.q;
      this.D = Double.NaN;
   }

   @Override
   public void close() {
      for (d0 var2 : this.E.values()) {
         var2.close();
      }

      this.h = null;
      this.q.D.close();
      this.l = null;
      this.D = Double.NaN;
   }
}

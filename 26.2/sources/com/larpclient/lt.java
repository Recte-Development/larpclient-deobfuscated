package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;

public final class lt {
   private final ArrayDeque<lt.j> T = new ArrayDeque<>();
   private double g = -100.0;
   private Vec3 R;
   private boolean y;
   private List<lt.f> G = List.of();
   private boolean L;
   public double Y = 1.2;
   public double I = 2.4;
   public double d = 0.45;
   public int A = 6;
   public int o = -8798209;
   public boolean e = (boolean)1;
   public String s = "Original";
   public String f = "Smooth";

   public boolean T() {
      return this.L;
   }

   public void N(Vec3 var1) {
      if (this.y) {
         this.y = (boolean)0;
         this.R();
      }

      ArrayList var2 = new ArrayList();
      this.L = (boolean)0;

      for (lt.j var4 : this.T) {
         EntityRenderState var5 = var4.state;
         double var6 = var5.x - var1.x;
         double var8 = var5.y - var1.y;
         double var10 = var5.z - var1.z;
         if (!(var6 * var6 + var8 * var8 + var10 * var10 > 2304.0)) {
            double var12 = Math.clamp((cf.F() - var4.born) / var4.lifetime, 0.0, 1.0);
            double var14 = this.d * (this.f.equals("Linear") ? 1.0 - var12 : st.h(1.0 - var12));
            if (!(var14 <= 0.0)) {
               j4.j var16 = switch (this.s) {
                  case "Original" -> j4.j.A;
                  case "Silhouette" -> j4.j.H;
                  case "Hologram" -> j4.j.k;
                  default -> j4.j.o;
               };
               var2.add(new lt.f(var5, var16, this.s.equals("Theme Tint") ? ho.J : this.o, var14, this.e));
               this.L = this.L | this.e;
            }
         }
      }

      this.G = List.copyOf(var2);
   }

   public void n() {
      while (this.T.size() > Math.clamp(this.A, 1, 12)) {
         this.T.removeFirst();
      }

      this.T.removeIf(var0 -> cf.I() - var0.born >= var0.lifetime);
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player == null || !var1.player.isAlive()) {
         this.x();
      }
   }

   public void y() {
      this.y = (boolean)1;
   }

   private void R() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.player.isAlive()) {
         double var2 = cf.I();
         Vec3 var4 = var1.player.position();
         if (!(var2 - this.g < this.Y) && (this.R == null || !(this.R.distanceToSqr(var4) < 0.04))) {
            if (this.R == null) {
               this.R = var4;
               this.g = var2;
            } else if (this.R.distanceToSqr(var4) > 64.0) {
               this.x();
               this.R = var4;
               this.g = var2;
            } else {
               EntityRenderState var5 = j4.d(var1.player, 1.0F);
               var5.nameTag = null;
               var5.scoreText = null;
               var5.shadowPieces.clear();
               var5.outlineColor = 0;

               while (this.T.size() >= Math.clamp(this.A, 1, 12)) {
                  this.T.removeFirst();
               }

               this.T.addLast(new lt.j(var5, var2, this.I));
               this.R = var4;
               this.g = var2;
            }
         }
      }
   }

   public boolean x() {
      return this.T.isEmpty();
   }

   public void x() {
      this.T.clear();
      this.G = List.of();
      this.L = this.y = (boolean)0;
      this.R = null;
      this.g = -100.0;
   }

   public void f(PoseStack var1, hh var2, boolean var3) {
      long var4 = zj.m();

      for (lt.f var7 : this.G) {
         if (!var3 || var7.bloom) {
            j4.I(var7.state, var1, var2, var7.material, var7.color, var7.alpha * (var3 ? 0.5 : 1.0), false);
         }
      }

      zj.l("GhostEcho", var4);
   }

   private record f(EntityRenderState state, j4.j material, int color, double alpha, boolean bloom) {
   }

   private record j(EntityRenderState state, double born, double lifetime) {
   }
}

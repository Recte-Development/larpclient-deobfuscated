package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;

public final class z0 {
   private final ArrayDeque<z0.i> r = new ArrayDeque<>();
   private double j = -100.0;
   private Vec3 V;
   private boolean p;
   private List<z0.o> C = List.of();
   private boolean Z;
   public double G = 1.2;
   public double g = 2.4;
   public double s = 0.45;
   public int E = 6;
   public int N = -8798209;
   public boolean Y = (boolean)1;
   public String W = "Original";
   public String d = "Smooth";

   public boolean J() {
      return this.Z;
   }

   public void b(Vec3 var1) {
      if (this.p) {
         this.p = (boolean)0;
         this.K();
      }

      ArrayList var2 = new ArrayList();
      this.Z = (boolean)0;

      for (z0.i var4 : this.r) {
         EntityRenderState var5 = var4.state;
         double var6 = var5.x - var1.x;
         double var8 = var5.y - var1.y;
         double var10 = var5.z - var1.z;
         if (!(var6 * var6 + var8 * var8 + var10 * var10 > 2304.0)) {
            double var12 = Math.clamp((ty.K() - var4.born) / var4.lifetime, 0.0, 1.0);
            double var14 = this.s * (this.d.equals("Linear") ? 1.0 - var12 : hp.R(1.0 - var12));
            if (!(var14 <= 0.0)) {
               tj.b var16 = switch (this.W) {
                  case "Original" -> tj.b.u;
                  case "Silhouette" -> tj.b.i;
                  case "Hologram" -> tj.b.g;
                  default -> tj.b.n;
               };
               var2.add(new z0.o(var5, var16, this.W.equals("Theme Tint") ? f.C : this.N, var14, this.Y));
               this.Z = this.Z | this.Y;
            }
         }
      }

      this.C = List.copyOf(var2);
   }

   public void y() {
      while (this.r.size() > Math.clamp(this.E, 1, 12)) {
         this.r.removeFirst();
      }

      this.r.removeIf(var0 -> ty.P() - var0.born >= var0.lifetime);
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player == null || !var1.player.isAlive()) {
         this.C();
      }
   }

   public void v() {
      this.p = (boolean)1;
   }

   private void K() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.player.isAlive()) {
         double var2 = ty.P();
         Vec3 var4 = var1.player.position();
         if (!(var2 - this.j < this.G) && (this.V == null || !(this.V.distanceToSqr(var4) < 0.04))) {
            if (this.V == null) {
               this.V = var4;
               this.j = var2;
            } else if (this.V.distanceToSqr(var4) > 64.0) {
               this.C();
               this.V = var4;
               this.j = var2;
            } else {
               EntityRenderState var5 = tj.s(var1.player, 1.0F);
               var5.nameTag = null;
               var5.scoreText = null;
               var5.shadowPieces.clear();
               var5.outlineColor = 0;

               while (this.r.size() >= Math.clamp(this.E, 1, 12)) {
                  this.r.removeFirst();
               }

               this.r.addLast(new z0.i(var5, var2, this.g));
               this.V = var4;
               this.j = var2;
            }
         }
      }
   }

   public boolean T() {
      return this.r.isEmpty();
   }

   public void C() {
      this.r.clear();
      this.C = List.of();
      this.Z = this.p = (boolean)0;
      this.V = null;
      this.j = -100.0;
   }

   public void Y(PoseStack var1, xh var2, boolean var3) {
      long var4 = kx.G();

      for (z0.o var7 : this.C) {
         if (!var3 || var7.bloom) {
            tj.u(var7.state, var1, var2, var7.material, var7.color, var7.alpha * (var3 ? 0.5 : 1.0), false);
         }
      }

      kx.u("GhostEcho", var4);
   }

   private record i(EntityRenderState state, double born, double lifetime) {
   }

   private record o(EntityRenderState state, tj.b material, int color, double alpha, boolean bloom) {
   }
}

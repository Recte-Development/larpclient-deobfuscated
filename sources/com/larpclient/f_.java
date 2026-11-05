package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;

public final class f_ {
   private final ArrayDeque<f_.f> X = new ArrayDeque<>();
   private double l = -100.0;
   private Vec3 k;
   private boolean Z;
   private List<f_.p> S = List.of();
   private boolean o;
   public double V = 1.2;
   public double u = 2.4;
   public double W = 0.45;
   public int n = 6;
   public int B = -8798209;
   public boolean E = (boolean)1;
   public String g = "Original";
   public String x = "Smooth";

   public boolean W() {
      return this.o;
   }

   public void O(Vec3 var1) {
      if (this.Z) {
         this.Z = (boolean)0;
         this.Y();
      }

      ArrayList var2 = new ArrayList();
      this.o = (boolean)0;

      for (f_.f var4 : this.X) {
         EntityRenderState var5 = var4.state;
         double var6 = var5.x - var1.x;
         double var8 = var5.y - var1.y;
         double var10 = var5.z - var1.z;
         if (!(var6 * var6 + var8 * var8 + var10 * var10 > 2304.0)) {
            double var12 = Math.clamp((ea.y() - var4.born) / var4.lifetime, 0.0, 1.0);
            double var14 = this.W * (this.x.equals("Linear") ? 1.0 - var12 : yc.H(1.0 - var12));
            if (!(var14 <= 0.0)) {
               uv.w var16 = switch (this.g) {
                  case "Original" -> uv.w.a;
                  case "Silhouette" -> uv.w.Y;
                  case "Hologram" -> uv.w.I;
                  default -> uv.w.V;
               };
               var2.add(new f_.p(var5, var16, this.g.equals("Theme Tint") ? rv.e : this.B, var14, this.E));
               this.o = this.o | this.E;
            }
         }
      }

      this.S = List.copyOf(var2);
   }

   public void o() {
      while (this.X.size() > Math.clamp(this.n, 1, 12)) {
         this.X.removeFirst();
      }

      this.X.removeIf(var0 -> ea.t() - var0.born >= var0.lifetime);
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player == null || !var1.player.isAlive()) {
         this.v();
      }
   }

   public void G() {
      this.Z = (boolean)1;
   }

   private void Y() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.player.isAlive()) {
         double var2 = ea.t();
         Vec3 var4 = var1.player.position();
         if (!(var2 - this.l < this.V) && (this.k == null || !(this.k.distanceToSqr(var4) < 0.04))) {
            if (this.k == null) {
               this.k = var4;
               this.l = var2;
            } else if (this.k.distanceToSqr(var4) > 64.0) {
               this.v();
               this.k = var4;
               this.l = var2;
            } else {
               EntityRenderState var5 = uv.v(var1.player, 1.0F);
               var5.nameTag = null;
               var5.scoreText = null;
               var5.shadowPieces.clear();
               var5.outlineColor = 0;

               while (this.X.size() >= Math.clamp(this.n, 1, 12)) {
                  this.X.removeFirst();
               }

               this.X.addLast(new f_.f(var5, var2, this.u));
               this.k = var4;
               this.l = var2;
            }
         }
      }
   }

   public boolean z() {
      return this.X.isEmpty();
   }

   public void v() {
      this.X.clear();
      this.S = List.of();
      this.o = this.Z = (boolean)0;
      this.k = null;
      this.l = -100.0;
   }

   public void a(PoseStack var1, y2 var2, boolean var3) {
      long var4 = h_.U();

      for (f_.p var7 : this.S) {
         if (!var3 || var7.bloom) {
            uv.k(var7.state, var1, var2, var7.material, var7.color, var7.alpha * (var3 ? 0.5 : 1.0), false);
         }
      }

      h_.n("GhostEcho", var4);
   }

   private record f(EntityRenderState state, double born, double lifetime) {
   }

   private record p(EntityRenderState state, uv.w material, int color, double alpha, boolean bloom) {
   }
}

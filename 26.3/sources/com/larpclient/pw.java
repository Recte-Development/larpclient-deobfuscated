package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;

public final class pw {
   private final ArrayDeque<pw.l> R = new ArrayDeque<>();
   private double P = -100.0;
   private Vec3 h;
   private boolean B;
   private List<pw.h> f = List.of();
   private boolean K;
   public double k = 1.2;
   public double e = 2.4;
   public double x = 0.45;
   public int E = 6;
   public int b = -8798209;
   public boolean a = (boolean)1;
   public String I = "Original";
   public String G = "Smooth";

   public boolean C() {
      return this.K;
   }

   public void l(Vec3 var1) {
      if (this.B) {
         this.B = (boolean)0;
         this.F();
      }

      ArrayList var2 = new ArrayList();
      this.K = (boolean)0;

      for (pw.l var4 : this.R) {
         EntityRenderState var5 = var4.state;
         double var6 = var5.x - var1.x;
         double var8 = var5.y - var1.y;
         double var10 = var5.z - var1.z;
         if (!(var6 * var6 + var8 * var8 + var10 * var10 > 2304.0)) {
            double var12 = Math.clamp((hz.Q() - var4.born) / var4.lifetime, 0.0, 1.0);
            double var14 = this.x * (this.G.equals("Linear") ? 1.0 - var12 : pr.k(1.0 - var12));
            if (!(var14 <= 0.0)) {
               w7.b var16 = switch (this.I) {
                  case "Original" -> w7.b.n;
                  case "Silhouette" -> w7.b.q;
                  case "Hologram" -> w7.b.Q;
                  default -> w7.b.E;
               };
               var2.add(new pw.h(var5, var16, this.I.equals("Theme Tint") ? c.y : this.b, var14, this.a));
               this.K = this.K | this.a;
            }
         }
      }

      this.f = List.copyOf(var2);
   }

   public void R() {
      while (this.R.size() > Math.clamp(this.E, 1, 12)) {
         this.R.removeFirst();
      }

      this.R.removeIf(var0 -> hz.S() - var0.born >= var0.lifetime);
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player == null || !var1.player.isAlive()) {
         this.t();
      }
   }

   public void g() {
      this.B = (boolean)1;
   }

   private void F() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.player.isAlive()) {
         double var2 = hz.S();
         Vec3 var4 = var1.player.position();
         if (!(var2 - this.P < this.k) && (this.h == null || !(this.h.distanceToSqr(var4) < 0.04))) {
            if (this.h == null) {
               this.h = var4;
               this.P = var2;
            } else if (this.h.distanceToSqr(var4) > 64.0) {
               this.t();
               this.h = var4;
               this.P = var2;
            } else {
               EntityRenderState var5 = w7.u(var1.player, 1.0F);
               var5.nameTag = null;
               var5.scoreText = null;
               var5.shadowPieces.clear();
               var5.outlineColor = 0;

               while (this.R.size() >= Math.clamp(this.E, 1, 12)) {
                  this.R.removeFirst();
               }

               this.R.addLast(new pw.l(var5, var2, this.e));
               this.h = var4;
               this.P = var2;
            }
         }
      }
   }

   public boolean P() {
      return this.R.isEmpty();
   }

   public void t() {
      this.R.clear();
      this.f = List.of();
      this.K = this.B = (boolean)0;
      this.h = null;
      this.P = -100.0;
   }

   public void N(PoseStack var1, wh var2, boolean var3) {
      long var4 = he.s();

      for (pw.h var7 : this.f) {
         if (!var3 || var7.bloom) {
            w7.Q(var7.state, var1, var2, var7.material, var7.color, var7.alpha * (var3 ? 0.5 : 1.0), false);
         }
      }

      he.T("GhostEcho", var4);
   }

   private record h(EntityRenderState state, w7.b material, int color, double alpha, boolean bloom) {
   }

   private record l(EntityRenderState state, double born, double lifetime) {
   }
}

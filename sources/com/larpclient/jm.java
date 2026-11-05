package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.minecraft.world.phys.Vec3;

public final class jm {
   private final fe e = new fe();
   private final _l J = new _l();
   private Object l;
   private Object b;
   private Vec3 g;
   private int T = -1;
   private int I;
   private int f = -1;
   private boolean M;
   private boolean L;

   private boolean Q(Minecraft var1, boolean var2) {
      if (var1.level != null && var1.player != null) {
         Vec3 var3 = var1.gameRenderer.mainCamera().position();
         boolean var4 = var1.options.getCameraType().isFirstPerson();
         if (this.l != var1.level || this.b != var1.player || this.T != jk.v || this.g != null && this.g.distanceToSqr(var3) > 256.0) {
            this.Z();
            this.l = var1.level;
            this.b = var1.player;
            this.T = jk.v;
            if (var2) {
               ym.s();
            }

            r4.g("Halloween", "World effects initialized; reload and relocation clear transient geometry");
         }

         if (var2 && var4 != this.L) {
            ym.s();
         }

         this.L = var4;
         this.g = var3;
         return true;
      } else {
         this.Z();
         return false;
      }
   }

   public void M(Minecraft var1, ol var2, boolean var3, boolean var4) {
      if (this.Q(var1, var4)) {
         if (var3 && var1.level.dimensionType().skybox() == Skybox.OVERWORLD) {
            if (this.I++ % 5 == 0) {
               BlockPos var5 = BlockPos.containing(this.g);
               this.M = (boolean)(var1.level.hasChunkAt(var5) && var1.level.canSeeSky(var5) ? 1 : 0);
            }

            this.e.u(this.g, var2, this.M);
         } else {
            this.e.H();
         }

         if (var4) {
            this.J.s();
            int var6 = var1.player.getInventory().getSelectedSlot();
            if (var6 != this.f || !this.L || !var1.player.isAlive()) {
               ym.s();
            }

            this.f = var6;
            ym.I(ea.t(), 0.22);
         } else {
            this.J.S();
         }
      }
   }

   public void B(Minecraft var1, ClientboundDamageEventPacket var2, ol var3) {
      if (this.Q(var1, true) && var2.sourceCauseId() == var1.player.getId()) {
         Entity var4 = var1.level.getEntity(var2.entityId());
         if (var4 instanceof LivingEntity var5 && var4 != var1.player && !var4.isRemoved() && !(var4.position().distanceToSqr(var1.player.position()) > 1600.0)
            )
          {
            Vec3 var6 = var5.position().add(0.0, var5.getBbHeight() * 0.6, 0.0);
            this.J.a(var6, var6.subtract(var1.player.getEyePosition()).normalize(), var3);
         }
      }
   }

   public void Z(float var1, CameraRenderState var2, boolean var3, boolean var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (this.l == var5.level && this.b == var5.player && this.T == jk.v) {
         float var6 = var5.isPaused() ? 1.0F : var1;
         if (var3) {
            this.e.B(var6, var2);
         }

         if (var4) {
            this.J.Q(var6, var2);
         }
      } else {
         this.Z();
      }
   }

   public void r(r6 var1, boolean var2, boolean var3, boolean var4) {
      if (this.T == jk.v) {
         if (var3) {
            this.e.l(var1, var2);
         }

         if (var4) {
            this.J.r(var1, var2);
         }
      }
   }

   public boolean G(boolean var1, boolean var2) {
      return var1 && this.e.G() || var2 && this.J.A();
   }

   public boolean C(boolean var1, boolean var2) {
      return var1 && this.e.T() || var2 && this.J.X();
   }

   public void K() {
      this.e.H();
   }

   public void M() {
      this.J.S();
   }

   public void Z() {
      this.e.H();
      this.J.S();
      this.l = this.b = null;
      this.g = null;
      this.T = -1;
      this.I = 0;
      this.f = -1;
      this.M = (boolean)0;
   }
}

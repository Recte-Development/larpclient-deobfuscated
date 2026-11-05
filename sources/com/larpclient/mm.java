package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.minecraft.world.phys.Vec3;

public final class mm {
   private final _x x = new _x();
   private final xp w = new xp();
   private Object K;
   private Object d;
   private Vec3 f;
   private int D = -1;
   private int t;
   private int B = -1;
   private boolean c;
   private boolean b;

   private boolean V(Minecraft var1, boolean var2) {
      if (var1.level != null && var1.player != null) {
         Vec3 var3 = var1.gameRenderer.mainCamera().position();
         boolean var4 = var1.options.getCameraType().isFirstPerson();
         if (this.K != var1.level || this.d != var1.player || this.D != m5.k || this.f != null && this.f.distanceToSqr(var3) > 256.0) {
            this.s();
            this.K = var1.level;
            this.d = var1.player;
            this.D = m5.k;
            if (var2) {
               x4.D();
            }

            mt.U("Halloween", "World effects initialized; reload and relocation clear transient geometry");
         }

         if (var2 && var4 != this.b) {
            x4.D();
         }

         this.b = var4;
         this.f = var3;
         return true;
      } else {
         this.s();
         return false;
      }
   }

   public void h(Minecraft var1, u8 var2, boolean var3, boolean var4) {
      if (this.V(var1, var4)) {
         if (var3 && var1.level.dimensionType().skybox() == Skybox.OVERWORLD) {
            if (this.t++ % 5 == 0) {
               BlockPos var5 = BlockPos.containing(this.f);
               this.c = (boolean)(var1.level.hasChunkAt(var5) && var1.level.canSeeSky(var5) ? 1 : 0);
            }

            this.x.t(this.f, var2, this.c);
         } else {
            this.x.r();
         }

         if (var4) {
            this.w.a();
            int var6 = var1.player.getInventory().getSelectedSlot();
            if (var6 != this.B || !this.b || !var1.player.isAlive()) {
               x4.D();
            }

            this.B = var6;
            x4.Q(ty.P(), 0.22);
         } else {
            this.w.Z();
         }
      }
   }

   public void n(Minecraft var1, ClientboundDamageEventPacket var2, u8 var3) {
      if (this.V(var1, true) && var2.sourceCauseId() == var1.player.getId()) {
         Entity var4 = var1.level.getEntity(var2.entityId());
         if (var4 instanceof LivingEntity var5 && var4 != var1.player && !var4.isRemoved() && !(var4.position().distanceToSqr(var1.player.position()) > 1600.0)
            )
          {
            Vec3 var6 = var5.position().add(0.0, var5.getBbHeight() * 0.6, 0.0);
            this.w.q(var6, var6.subtract(var1.player.getEyePosition()).normalize(), var3);
         }
      }
   }

   public void H(float var1, CameraRenderState var2, boolean var3, boolean var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (this.K == var5.level && this.d == var5.player && this.D == m5.k) {
         float var6 = var5.isPaused() ? 1.0F : var1;
         if (var3) {
            this.x.B(var6, var2);
         }

         if (var4) {
            this.w.i(var6, var2);
         }
      } else {
         this.s();
      }
   }

   public void k(ki var1, boolean var2, boolean var3, boolean var4) {
      if (this.D == m5.k) {
         if (var3) {
            this.x.Y(var1, var2);
         }

         if (var4) {
            this.w.s(var1, var2);
         }
      }
   }

   public boolean Y(boolean var1, boolean var2) {
      return var1 && this.x.U() || var2 && this.w.f();
   }

   public boolean g(boolean var1, boolean var2) {
      return var1 && this.x.o() || var2 && this.w.s();
   }

   public void J() {
      this.x.r();
   }

   public void t() {
      this.w.Z();
   }

   public void s() {
      this.x.r();
      this.w.Z();
      this.K = this.d = null;
      this.f = null;
      this.D = -1;
      this.t = 0;
      this.B = -1;
      this.c = (boolean)0;
   }
}

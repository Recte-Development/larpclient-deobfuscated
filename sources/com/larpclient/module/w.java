package com.larpclient.module;

import com.larpclient._d;
import com.larpclient.m9;
import com.larpclient.pp;
import com.larpclient.tq;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.phys.Vec3;

public final class w extends _d.u implements pp.x {
   private final pp y = new pp(this, var1 -> this.addSetting(var1));
   private static w c;
   private Object Q;
   private Object o;
   private int q;
   private final dh r = this.p("Legs", "Place webs at the opponent's feet", true);
   private final dh x = this.p("Head", "Place webs at the opponent's head", true);
   private final dh H = this.p("Surround", "Web the lower perimeter", false);
   private final dh X = this.p("Upper Surround", "Web the upper perimeter", false);
   private final dh E = this.p("Web Predict", "Predict movement unless the target is already in webs", false);
   private final d9 N = this.S("Predict Ticks", "Ticks of displacement prediction", 2.0, 0.0, 10.0, 0.5);
   private final dh J = this.p("Integrate With Trap", "Choose the footprint used by the active AutoTrap", false);
   private final dh A = this.p("Auto Sign", "Use ordinary hotbar signs at head positions instead of head webs", false);

   @Override
   public pp placementHighlight() {
      return this.y;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.y.w();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.y.P();
      this.o();
   }

   public w() {
      super("AutoWeb", "Place cobwebs around opponents");
      c = this;
      this.N.H(this.E::P);
      this.M.h(var1 -> {
         _d.N(this);
         this.o();
      });
   }

   public static w Y() {
      return c;
   }

   @Override
   protected void aim(float var1, float var2) {
      if (this.isEnabled()) {
         if (tq.y(tq.y.a)) {
            super.aim(var1, var2);
            if (m9.F(m9.s.p)) {
               this.Q = mc.player;
               this.o = mc.level;
               this.q = mc.player.tickCount;
               tq.i(tq.y.a);
            } else {
               this.o();
            }
         }
      }
   }

   private void o() {
      this.Q = this.o = null;
      tq.A(tq.y.a);
   }

   public boolean f() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.M.S("Visible")
            && mc.player != null
            && this.Q == mc.player
            && this.o == mc.level
            && mc.player.tickCount >= this.q
            && mc.player.tickCount - this.q <= 1
            && (!this.w.P() || !mc.player.isUsingItem())
            && (!this.G.P() || !mc.options.keyAttack.isDown());
   }

   public float Z(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && tq.l() == tq.y.a && m9.F(m9.s.p)) {
            mc.player.setYHeadRot(mc.player.getYRot());
            return mc.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private boolean t(Player var1) {
      for (BlockPos var3 : com.larpclient.module.f.j(var1, var1.position())) {
         if (mc.level.getBlockState(var3).is(Blocks.COBWEB)) {
            return true;
         }
      }

      return false;
   }

   private Item L() {
      for (int var1 = 0; var1 < 9; var1++) {
         Item var2 = mc.player.getInventory().getItem(var1).getItem();
         if (var2 instanceof BlockItem var3 && var3.getBlock() instanceof SignBlock) {
            return var2;
         }
      }

      return null;
   }

   @Override
   public void onTick() {
      this.y.m(null);
      _d.N(this);
      boolean var1 = this.s();
      if (!var1) {
         int var10000 = this.y.F();
         boolean var15 = false;
         byte var14 = (byte)var10000;
         var10000 = Integer.signum(var14);
         if (((var10000 | -var10000) >>> 31 ^ 1) != 0 || !this.F()) {
            return;
         }
      }

      Player var2 = this.a();
      if (var2 == null) {
         this.e("Waiting for a target");
      } else {
         Vec3 var3 = com.larpclient.module.f.D(var2, this.E.P() && !this.t(var2) ? this.N.P() : 0.0);
         List var4 = com.larpclient.module.f.j(var2, var3);
         com.larpclient.module.f var5 = com.larpclient.module.f.X();
         if (this.J.P() && var5 != null && var5.isRuntimeActive()) {
            LinkedHashSet var6 = new LinkedHashSet();

            for (BlockPos var8 : var5.I(var2)) {
               if (var8.getY() == ((BlockPos)var4.getFirst()).getY() - 1) {
                  var6.add(var8.above());
               }
            }

            if (!var6.isEmpty()) {
               var4 = new ArrayList(var6);
            }
         }

         int var30 = this.A.P();
         boolean var17 = false;
         byte var16 = (byte)var30;
         var30 = Integer.signum(var16);
         Item var22 = ((var30 | -var30) >>> 31 ^ 1) == 0 ? this.L() : null;
         LinkedHashSet var23 = new LinkedHashSet();
         LinkedHashSet var24 = new LinkedHashSet();

         for (BlockPos var10 : var4) {
            if (this.r.P()) {
               var23.add(var10);
            }

            if (var22 != null) {
               var24.add(var10.above());
            } else if (this.x.P()) {
               var23.add(var10.above());
            }

            for (Direction var12 : Plane.HORIZONTAL) {
               BlockPos var13 = var10.relative(var12);
               if (!var4.contains(var13)) {
                  if (this.H.P()) {
                     var23.add(var13);
                  }

                  if (this.X.P()) {
                     var23.add(var13.above());
                  }
               }
            }
         }

         var30 = _d.B(Items.COBWEB, this.d.P());
         byte var19 = -1;
         int var18 = var30;
         var30 = Integer.compare(var18, var19);
         if (((var30 | -var30) >>> 31 ^ 1) == 0) {
            for (BlockPos var27 : var23) {
               if (var1 ? this.I(var27, Items.COBWEB, var2) : this.C(var27, Items.COBWEB)) {
                  this.y.m(var27);
                  if (var1) {
                     this.e("Preparing web placement");
                  }

                  return;
               }
            }
         }

         if (var22 != null) {
            for (BlockPos var28 : var24) {
               if (var1 ? this.I(var28, var22, var2) : this.C(var28, var22)) {
                  this.y.m(var28);
                  if (var1) {
                     this.e("Preparing sign placement");
                  }

                  return;
               }
            }
         }

         int var10001 = _d.B(Items.COBWEB, this.d.P());
         byte var21 = -1;
         int var20 = var10001;
         var10001 = Integer.compare(var20, var21);
         this.e((var10001 | -var10001) >>> 31 == 0 ? "Cobwebs missing from hotbar" : "No reachable empty web position: " + _d.T(this));
      }
   }
}

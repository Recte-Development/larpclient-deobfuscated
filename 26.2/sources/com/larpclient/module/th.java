package com.larpclient.module;

import com.larpclient.j6;
import com.larpclient.mr;
import com.larpclient.p_;
import com.larpclient.t1;
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

public final class th extends t1.b implements mr.n {
   private final mr h = new mr(this, var1 -> this.addSetting(var1));
   private static th G;
   private Object T;
   private Object R;
   private int u;
   private final s M = this.s("Legs", "Place webs at the opponent's feet", true);
   private final s m = this.s("Head", "Place webs at the opponent's head", true);
   private final s S = this.s("Surround", "Web the lower perimeter", false);
   private final s Z = this.s("Upper Surround", "Web the upper perimeter", false);
   private final s N = this.s("Web Predict", "Predict movement unless the target is already in webs", false);
   private final e A = this.Y("Predict Ticks", "Ticks of displacement prediction", 2.0, 0.0, 10.0, 0.5);
   private final s E = this.s("Integrate With Trap", "Choose the footprint used by the active AutoTrap", false);
   private final s k = this.s("Auto Sign", "Use ordinary hotbar signs at head positions instead of head webs", false);

   @Override
   public mr placementHighlight() {
      return this.h;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.h.t();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.h.c();
      this.K();
   }

   public th() {
      super("AutoWeb", "Place cobwebs around opponents");
      G = this;
      this.A.P(this.N::getValue);
      this.U.o(var1 -> {
         t1.v(this);
         this.K();
      });
   }

   public static th B() {
      return G;
   }

   @Override
   protected void aim(float var1, float var2) {
      if (this.isEnabled()) {
         if (p_.I(p_.o.h)) {
            super.aim(var1, var2);
            if (j6.g(j6.l.p)) {
               this.T = mc.player;
               this.R = mc.level;
               this.u = mc.player.tickCount;
               p_.p(p_.o.h);
            } else {
               this.K();
            }
         }
      }
   }

   private void K() {
      this.T = this.R = null;
      p_.Y(p_.o.h);
   }

   public boolean V() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.U.O("Visible")
            && mc.player != null
            && this.T == mc.player
            && this.R == mc.level
            && mc.player.tickCount >= this.u
            && mc.player.tickCount - this.u <= 1
            && (!this.y.getValue() || !mc.player.isUsingItem())
            && (!this.C.getValue() || !mc.options.keyAttack.isDown());
   }

   public float J(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && p_.A() == p_.o.h && j6.g(j6.l.p)) {
            mc.player.setYHeadRot(mc.player.getYRot());
            return mc.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private boolean W(Player var1) {
      for (BlockPos var3 : tb.a(var1, var1.position())) {
         if (mc.level.getBlockState(var3).is(Blocks.COBWEB)) {
            return true;
         }
      }

      return false;
   }

   private Item s() {
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
      this.h.q(null);
      t1.v(this);
      boolean var1 = this.v();
      if (!var1) {
         int var10000 = this.h.B();
         boolean var15 = false;
         byte var14 = (byte)var10000;
         var10000 = Integer.signum(var14);
         if (((var10000 | -var10000) >>> 31 ^ 1) != 0 || !this.m()) {
            return;
         }
      }

      Player var2 = this.L();
      if (var2 == null) {
         this.U("Waiting for a target");
      } else {
         Vec3 var3 = tb.l(var2, this.N.getValue() && !this.W(var2) ? this.A.getValue() : 0.0);
         List var4 = tb.a(var2, var3);
         tb var5 = tb.R();
         if (this.E.getValue() && var5 != null && var5.isRuntimeActive()) {
            LinkedHashSet var6 = new LinkedHashSet();

            for (BlockPos var8 : var5.a(var2)) {
               if (var8.getY() == ((BlockPos)var4.getFirst()).getY() - 1) {
                  var6.add(var8.above());
               }
            }

            if (!var6.isEmpty()) {
               var4 = new ArrayList(var6);
            }
         }

         int var30 = this.k.getValue();
         boolean var19 = false;
         byte var18 = (byte)var30;
         var30 = Integer.signum(var18);
         Item var22 = ((var30 | -var30) >>> 31 ^ 1) == 0 ? this.s() : null;
         LinkedHashSet var23 = new LinkedHashSet();
         LinkedHashSet var24 = new LinkedHashSet();

         for (BlockPos var10 : var4) {
            if (this.M.getValue()) {
               var23.add(var10);
            }

            if (var22 != null) {
               var24.add(var10.above());
            } else if (this.m.getValue()) {
               var23.add(var10.above());
            }

            for (Direction var12 : Plane.HORIZONTAL) {
               BlockPos var13 = var10.relative(var12);
               if (!var4.contains(var13)) {
                  if (this.S.getValue()) {
                     var23.add(var13);
                  }

                  if (this.Z.getValue()) {
                     var23.add(var13.above());
                  }
               }
            }
         }

         var30 = t1.M(Items.COBWEB, this.l.getValue());
         byte var17 = -1;
         int var16 = var30;
         var30 = Integer.compare(var16, var17);
         if (((var30 | -var30) >>> 31 ^ 1) == 0) {
            for (BlockPos var27 : var23) {
               if (var1 ? this.b(var27, Items.COBWEB, var2) : this.n(var27, Items.COBWEB)) {
                  this.h.q(var27);
                  if (var1) {
                     this.U("Preparing web placement");
                  }

                  return;
               }
            }
         }

         if (var22 != null) {
            for (BlockPos var28 : var24) {
               if (var1 ? this.b(var28, var22, var2) : this.n(var28, var22)) {
                  this.h.q(var28);
                  if (var1) {
                     this.U("Preparing sign placement");
                  }

                  return;
               }
            }
         }

         int var10001 = t1.M(Items.COBWEB, this.l.getValue());
         byte var21 = -1;
         int var20 = var10001;
         var10001 = Integer.compare(var20, var21);
         this.U((var10001 | -var10001) >>> 31 == 0 ? "Cobwebs missing from hotbar" : "No reachable empty web position: " + t1.L(this));
      }
   }
}

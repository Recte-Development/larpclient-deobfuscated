package com.larpclient.module;

import com.larpclient.ep;
import com.larpclient.hy;
import com.larpclient.m;
import com.larpclient.rr;
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

public final class k extends m.o implements hy.c {
   private final hy W = new hy(this, var1 -> this.addSetting(var1));
   private static k C;
   private Object N;
   private Object e;
   private int n;
   private final ln u = this.h("Legs", "Place webs at the opponent's feet", true);
   private final ln o = this.h("Head", "Place webs at the opponent's head", true);
   private final ln F = this.h("Surround", "Web the lower perimeter", false);
   private final ln R = this.h("Upper Surround", "Web the upper perimeter", false);
   private final ln a = this.h("Web Predict", "Predict movement unless the target is already in webs", false);
   private final lc j = this.z("Predict Ticks", "Ticks of displacement prediction", 2.0, 0.0, 10.0, 0.5);
   private final ln P = this.h("Integrate With Trap", "Choose the footprint used by the active AutoTrap", false);
   private final ln f = this.h("Auto Sign", "Use ordinary hotbar signs at head positions instead of head webs", false);

   @Override
   public hy placementHighlight() {
      return this.W;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.W.S();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.W.C();
      this.G();
   }

   public k() {
      super("AutoWeb", "Place cobwebs around opponents");
      C = this;
      this.j.x(this.a::getValue);
      this.Y.W(var1 -> {
         m.g(this);
         this.G();
      });
   }

   public static k v() {
      return C;
   }

   @Override
   protected void aim(float var1, float var2) {
      if (this.isEnabled()) {
         if (rr.j(rr.f.L)) {
            super.aim(var1, var2);
            if (ep.W(ep.z.d)) {
               this.N = mc.player;
               this.e = mc.level;
               this.n = mc.player.tickCount;
               rr.W(rr.f.L);
            } else {
               this.G();
            }
         }
      }
   }

   private void G() {
      this.N = this.e = null;
      rr.N(rr.f.L);
   }

   public boolean g() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.Y.i("Visible")
            && mc.player != null
            && this.N == mc.player
            && this.e == mc.level
            && mc.player.tickCount >= this.n
            && mc.player.tickCount - this.n <= 1
            && (!this.w.getValue() || !mc.player.isUsingItem())
            && (!this.d.getValue() || !mc.options.keyAttack.isDown());
   }

   public float A(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && rr.u() == rr.f.L && ep.W(ep.z.d)) {
            mc.player.setYHeadRot(mc.player.getYRot());
            return mc.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private boolean r(Player var1) {
      for (BlockPos var3 : t.S(var1, var1.position())) {
         if (mc.level.getBlockState(var3).is(Blocks.COBWEB)) {
            return true;
         }
      }

      return false;
   }

   private Item O() {
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
      this.W.i(null);
      m.g(this);
      boolean var1 = this.K();
      if (var1 || this.W.z() && this.i()) {
         Player var2 = this.o();
         if (var2 == null) {
            this.H("Waiting for a target");
         } else {
            int var10001 = this.a.getValue();
            boolean var19 = false;
            byte var18 = (byte)var10001;
            var10001 = Integer.signum(var18);
            Vec3 var3 = t.X(var2, ((var10001 | -var10001) >>> 31 ^ 1) == 0 && !this.r(var2) ? this.j.getValue() : 0.0);
            List var4 = t.S(var2, var3);
            t var5 = t.d();
            int var10000 = this.P.getValue();
            boolean var21 = false;
            byte var20 = (byte)var10000;
            var10000 = Integer.signum(var20);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && var5 != null && var5.isRuntimeActive()) {
               LinkedHashSet var6 = new LinkedHashSet();

               for (BlockPos var8 : var5.Z(var2)) {
                  if (var8.getY() == ((BlockPos)var4.getFirst()).getY() - 1) {
                     var6.add(var8.above());
                  }
               }

               if (!var6.isEmpty()) {
                  var4 = new ArrayList(var6);
               }
            }

            Item var22 = this.f.getValue() ? this.O() : null;
            LinkedHashSet var23 = new LinkedHashSet();
            LinkedHashSet var24 = new LinkedHashSet();

            for (BlockPos var10 : var4) {
               if (this.u.getValue()) {
                  var23.add(var10);
               }

               if (var22 != null) {
                  var24.add(var10.above());
               } else if (this.o.getValue()) {
                  var23.add(var10.above());
               }

               for (Direction var12 : Plane.HORIZONTAL) {
                  BlockPos var13 = var10.relative(var12);
                  if (!var4.contains(var13)) {
                     if (this.F.getValue()) {
                        var23.add(var13);
                     }

                     if (this.R.getValue()) {
                        var23.add(var13.above());
                     }
                  }
               }
            }

            var10000 = m.Q(Items.COBWEB, this.x.getValue());
            byte var17 = -1;
            int var16 = var10000;
            var10000 = Integer.compare(var16, var17);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               for (BlockPos var27 : var23) {
                  if (var1 ? this.H(var27, Items.COBWEB, var2) : this.f(var27, Items.COBWEB)) {
                     this.W.i(var27);
                     if (var1) {
                        this.H("Preparing web placement");
                     }

                     return;
                  }
               }
            }

            if (var22 != null) {
               for (BlockPos var28 : var24) {
                  if (var1 ? this.H(var28, var22, var2) : this.f(var28, var22)) {
                     this.W.i(var28);
                     if (var1) {
                        this.H("Preparing sign placement");
                     }

                     return;
                  }
               }
            }

            var10001 = m.Q(Items.COBWEB, this.x.getValue());
            byte var15 = -1;
            int var14 = var10001;
            var10001 = Integer.compare(var14, var15);
            this.H((var10001 | -var10001) >>> 31 == 0 ? "Cobwebs missing from hotbar" : "No reachable empty web position: " + m.N(this));
         }
      }
   }
}

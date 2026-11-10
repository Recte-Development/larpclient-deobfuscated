package com.larpclient.module;

import com.larpclient.ai;
import com.larpclient.b6;
import com.larpclient.ij;
import com.larpclient.vi;
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

public final class kb extends vi.n implements ai.e {
   private final ai L = new ai(this, var1 -> this.addSetting(var1));
   private static kb J;
   private Object M;
   private Object f;
   private int V;
   private final m d = this.O("Legs", "Place webs at the opponent's feet", true);
   private final m U = this.O("Head", "Place webs at the opponent's head", true);
   private final m j = this.O("Surround", "Web the lower perimeter", false);
   private final m q = this.O("Upper Surround", "Web the upper perimeter", false);
   private final m e = this.O("Web Predict", "Predict movement unless the target is already in webs", false);
   private final b y = this.x("Predict Ticks", "Ticks of displacement prediction", 2.0, 0.0, 10.0, 0.5);
   private final m Y = this.O("Integrate With Trap", "Choose the footprint used by the active AutoTrap", false);
   private final m i = this.O("Auto Sign", "Use ordinary hotbar signs at head positions instead of head webs", false);

   @Override
   public ai placementHighlight() {
      return this.L;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.L.b();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.L.m();
      this.T();
   }

   public kb() {
      super("AutoWeb", "Place cobwebs around opponents");
      J = this;
      this.y.a(this.e::getValue);
      this.O.w(var1 -> {
         vi.J(this);
         this.T();
      });
   }

   public static kb u() {
      return J;
   }

   @Override
   protected void aim(float var1, float var2) {
      if (this.isEnabled()) {
         if (ij.a(ij.r.s)) {
            super.aim(var1, var2);
            if (b6.c(b6.w.F)) {
               this.M = mc.player;
               this.f = mc.level;
               this.V = mc.player.tickCount;
               ij.V(ij.r.s);
            } else {
               this.T();
            }
         }
      }
   }

   private void T() {
      this.M = this.f = null;
      ij.W(ij.r.s);
   }

   public boolean w() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.O.w("Visible")
            && mc.player != null
            && this.M == mc.player
            && this.f == mc.level
            && mc.player.tickCount >= this.V
            && mc.player.tickCount - this.V <= 1
            && (!this.g.getValue() || !mc.player.isUsingItem())
            && (!this.o.getValue() || !mc.options.keyAttack.isDown());
   }

   public float A(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && ij.L() == ij.r.s && b6.c(b6.w.F)) {
            mc.player.setYHeadRot(mc.player.getYRot());
            return mc.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private boolean B(Player var1) {
      for (BlockPos var3 : kv.r(var1, var1.position())) {
         if (mc.level.getBlockState(var3).is(Blocks.COBWEB)) {
            return true;
         }
      }

      return false;
   }

   private Item g() {
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
      this.L.H(null);
      vi.J(this);
      boolean var1 = this.G();
      if (var1 || this.L.D() && this.Z()) {
         Player var2 = this.P();
         if (var2 == null) {
            this.M("Waiting for a target");
         } else {
            int var10001 = this.e.getValue();
            boolean var19 = false;
            byte var18 = (byte)var10001;
            var10001 = Integer.signum(var18);
            Vec3 var3 = kv.L(var2, ((var10001 | -var10001) >>> 31 ^ 1) == 0 && !this.B(var2) ? this.y.getValue() : 0.0);
            List var4 = kv.r(var2, var3);
            kv var5 = kv.m();
            if (this.Y.getValue() && var5 != null && var5.isRuntimeActive()) {
               LinkedHashSet var6 = new LinkedHashSet();

               for (BlockPos var8 : var5.M(var2)) {
                  if (var8.getY() == ((BlockPos)var4.getFirst()).getY() - 1) {
                     var6.add(var8.above());
                  }
               }

               int var10000 = var6.isEmpty();
               boolean var15 = false;
               byte var14 = (byte)var10000;
               var10000 = Integer.signum(var14);
               if ((var10000 | -var10000) >>> 31 == 0) {
                  var4 = new ArrayList(var6);
               }
            }

            Item var22 = this.i.getValue() ? this.g() : null;
            LinkedHashSet var23 = new LinkedHashSet();
            LinkedHashSet var24 = new LinkedHashSet();

            for (BlockPos var10 : var4) {
               if (this.d.getValue()) {
                  var23.add(var10);
               }

               if (var22 != null) {
                  var24.add(var10.above());
               } else if (this.U.getValue()) {
                  var23.add(var10.above());
               }

               for (Direction var12 : Plane.HORIZONTAL) {
                  BlockPos var13 = var10.relative(var12);
                  if (!var4.contains(var13)) {
                     if (this.j.getValue()) {
                        var23.add(var13);
                     }

                     if (this.q.getValue()) {
                        var23.add(var13.above());
                     }
                  }
               }
            }

            int var30 = vi.u(Items.COBWEB, this.a.getValue());
            byte var21 = -1;
            int var20 = var30;
            var30 = Integer.compare(var20, var21);
            if (((var30 | -var30) >>> 31 ^ 1) == 0) {
               for (BlockPos var27 : var23) {
                  if (var1 ? this.n(var27, Items.COBWEB, var2) : this.y(var27, Items.COBWEB)) {
                     this.L.H(var27);
                     if (var1) {
                        this.M("Preparing web placement");
                     }

                     return;
                  }
               }
            }

            if (var22 != null) {
               for (BlockPos var28 : var24) {
                  if (var1 ? this.n(var28, var22, var2) : this.y(var28, var22)) {
                     this.L.H(var28);
                     if (var1) {
                        this.M("Preparing sign placement");
                     }

                     return;
                  }
               }
            }

            var10001 = vi.u(Items.COBWEB, this.a.getValue());
            byte var17 = -1;
            int var16 = var10001;
            var10001 = Integer.compare(var16, var17);
            this.M((var10001 | -var10001) >>> 31 == 0 ? "Cobwebs missing from hotbar" : "No reachable empty web position: " + vi.k(this));
         }
      }
   }
}

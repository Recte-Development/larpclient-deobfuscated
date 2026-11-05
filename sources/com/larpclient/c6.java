package com.larpclient;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.DispenserScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class c6 {
   private static final c6 s = new c6();
   private boolean t;
   private boolean V;
   private boolean U;
   private boolean T;
   private boolean b;
   private boolean a;
   private boolean L;
   public Item W = Items.APPLE;
   public Item N = Items.DIAMOND;
   public boolean P = (boolean)1;
   private int i = -1;
   private int H = -1;
   private int u = -1;
   private DispenserMenu E;
   private BlockPos v;
   private final ItemStack[] m = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
   private final int[] G = new int[]{-1, -1};
   private ItemStack l = ItemStack.EMPTY;
   private ItemStack Z = ItemStack.EMPTY;
   private int K;
   private c6.q w = c6.q.H;
   private long k;
   private long f;

   public static c6 K() {
      return s;
   }

   public boolean F() {
      return this.t;
   }

   public boolean w() {
      return this.w != c6.q.H;
   }

   public int T() {
      return this.P ? 1 : 2;
   }

   public ItemStack f(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      return this.i >= 0 && this.w() && var2.player != null && var1 == var2.player.getInventory().getItem(this.i) ? ItemStack.EMPTY : var1;
   }

   public ItemStack W(Minecraft var1, AbstractContainerMenu var2, Slot var3) {
      if (this.w != c6.q.n && this.w() && var2 == this.E && this.q()) {
         if (var3.container == var1.player.getInventory()) {
            return var3.getContainerSlot() == this.i ? ItemStack.EMPTY : null;
         } else {
            return var3.index == this.H && this.E.getSlot(this.H) == var3 ? this.l : null;
         }
      } else {
         return null;
      }
   }

   public String h(int var1) {
      return this.m[var1].isEmpty() ? "ITEM " + (var1 + 1) : this.m[var1].getHoverName().getString();
   }

   public boolean q() {
      Minecraft var1 = Minecraft.getInstance();
      return this.T && this.E != null && var1.player != null && var1.player.containerMenu == this.E;
   }

   public void u(int var1) {
      if ((!this.w() || this.w == c6.q.b) && var1 >= 1 && var1 <= 2) {
         this.P = (boolean)(var1 == 1 ? 1 : 0);
      }
   }

   public void H(boolean var1) {
      this.t = var1;
   }

   private BlockPos d() {
      return ep.H().n(ep.o.M);
   }

   public void T(Minecraft var1, DispenserMenu var2, BlockPos var3) {
      if (!this.w() && var3 != null) {
         this.E = var2;
         this.v = var3.immutable();
         this.T = (boolean)0;
      }
   }

   public boolean K(MenuType<?> var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.V && var1 == MenuType.GENERIC_3x3 && var3.player != null) {
         this.E = new DispenserMenu(var2, var3.player.getInventory());
         var3.player.containerMenu = this.E;
         this.V = (boolean)0;
         this.T = (boolean)0;
         return true;
      } else {
         return false;
      }
   }

   static int[] v(List<ItemStack> var0) {
      int var1 = -1;
      int var2 = -1;

      for (int var3 = 0; var3 < 9; var3++) {
         if (!((ItemStack)var0.get(var3)).isEmpty()) {
            if (((ItemStack)var0.get(var3)).getCount() != 1) {
               return null;
            }

            if (var1 < 0) {
               var1 = var3;
            } else {
               if (var2 >= 0) {
                  return null;
               }

               var2 = var3;
            }
         }
      }

      return var2 >= 0 && !((ItemStack)var0.get(var1)).is(((ItemStack)var0.get(var2)).getItem()) ? new int[]{var1, var2} : null;
   }

   public void Y(int var1, List<ItemStack> var2, ItemStack var3) {
      if (this.E != null && var1 == this.E.containerId && var2.size() == this.E.slots.size()) {
         this.T = (boolean)1;
         if (this.w == c6.q.H) {
            int[] var4 = v(var2);
            if (var4 != null) {
               for (int var5 = 0; var5 < 2; var5++) {
                  this.G[var5] = var4[var5];
                  this.m[var5] = ((ItemStack)var2.get(var4[var5])).copy();
               }

               this.W = this.m[0].getItem();
               this.N = this.m[1].getItem();
            }
         }

         this.r(var2, var3);
      }
   }

   public void R(int var1, int var2, ItemStack var3) {
      if (this.E != null && this.w() && this.w != c6.q.n) {
         if (var1 == this.E.containerId || var1 == 0 && var2 == 36 + this.i) {
            if (this.w == c6.q.k || this.w == c6.q.X) {
               if (var2 == this.H) {
                  this.a = this.w == c6.q.k ? var3.isEmpty() : ItemStack.matches(var3, this.l);
               }

               if (var2 == 36 + this.i) {
                  this.L = this.w == c6.q.k ? ItemStack.matches(var3, this.l) : var3.isEmpty();
               }

               if (this.a && this.L && this.E.getCarried().isEmpty()) {
                  if (this.w == c6.q.k) {
                     this.P(!ItemStack.matches(this.E.getSlot(this.u).getItem(), this.Z));
                  } else {
                     this.U();
                  }
               }
            }

            if (this.w == c6.q.b && var2 == this.u && !ItemStack.matches(var3, this.Z)) {
               this.S();
            }
         }
      }
   }

   public void N(int var1, ItemStack var2) {
      if (this.E != null && var1 == this.i) {
         this.R(this.E.containerId, 36 + var1, var2);
      }
   }

   private void P(boolean var1) {
      this.w = c6.q.b;
      this.k = System.nanoTime() + 15000000000L;
      xa.q("50/50", "Trigger swap confirmed: " + this.Z.getHoverName().getString());
      if (!this.t || this.T() != this.K || var1) {
         this.S();
      }
   }

   private void U() {
      this.w = c6.q.H;
      this.l = ItemStack.EMPTY;
      this.i = -1;
      this.Z = ItemStack.EMPTY;
      xa.q("50/50", "Returned held item; dispenser ready");
   }

   private void r(List<ItemStack> var1, ItemStack var2) {
      if (this.w() && this.w != c6.q.n && this.i >= 0 && var2.isEmpty()) {
         int var3 = 36 + this.i;
         if (this.w == c6.q.k && ((ItemStack)var1.get(this.H)).isEmpty() && ItemStack.matches((ItemStack)var1.get(var3), this.l)) {
            this.P(!ItemStack.matches((ItemStack)var1.get(this.u), this.Z));
         } else if (this.w == c6.q.k && ((ItemStack)var1.get(var3)).isEmpty()) {
            this.w = c6.q.H;
            this.i = -1;
            this.l = ItemStack.EMPTY;
            this.Z = ItemStack.EMPTY;
            xa.q("50/50", "Missed trigger: removal not accepted; no replay");
         } else if (this.w == c6.q.b && !ItemStack.matches((ItemStack)var1.get(this.u), this.Z)) {
            this.S();
         } else if (this.w == c6.q.X && ItemStack.matches((ItemStack)var1.get(this.H), this.l) && ((ItemStack)var1.get(var3)).isEmpty()) {
            this.U();
         }
      }
   }

   private void S() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.q() && this.E.getSlot(this.H).getItem().isEmpty() && ItemStack.matches(var1.player.getInventory().getItem(this.i), this.l)) {
         this.w = c6.q.X;
         this.k = System.nanoTime() + 15000000000L;
         this.T(this.H, this.i);
      } else {
         this.r("return state changed");
      }
   }

   private void T(int var1, int var2) {
      this.a = this.L = (boolean)0;
      Minecraft var3 = Minecraft.getInstance();
      this.U = (boolean)1;

      try {
         var3.gameMode.handleContainerInput(this.E.containerId, var1, var2, ContainerInput.SWAP, var3.player);
      } finally {
         this.U = (boolean)0;
      }
   }

   public int k(AbstractContainerMenu var1, int var2) {
      return this.U && var1 == this.E ? 32768 : var2;
   }

   public boolean j() {
      return this.w() && !this.U && this.w != c6.q.n;
   }

   public boolean h() {
      return this.w == c6.q.b;
   }

   public boolean E() {
      if (!this.j()) {
         return false;
      }

      Minecraft var1 = Minecraft.getInstance();
      return !this.h()
         || !(
            var1.hitResult instanceof BlockHitResult var2
               && var1.level.getBlockState(var2.getBlockPos()).is(Blocks.LEVER)
               && this.v != null
               && !(var2.getBlockPos().distSqr(this.v) > 9.0)
         );
   }

   public boolean A(Minecraft var1, InteractionHand var2, BlockHitResult var3) {
      if (this.U) {
         return false;
      }

      if (this.w()) {
         return this.w != c6.q.n;
      }

      if (this.t
         && this.q()
         && var2 == InteractionHand.MAIN_HAND
         && var1.level != null
         && var1.level.getBlockState(var3.getBlockPos()).is(Blocks.LEVER)
         && !(Boolean)var1.level.getBlockState(var3.getBlockPos()).getValue(LeverBlock.POWERED)
         && this.v != null
         && var3.getBlockPos().distSqr(this.v) <= 9.0) {
         this.S(var1);
      }

      return false;
   }

   public void i(BlockPos var1, BlockState var2) {
      if (this.v != null && this.v.equals(var1)) {
         int var3 = var2.hasProperty(DispenserBlock.TRIGGERED) && var2.getValue(DispenserBlock.TRIGGERED) ? 1 : 0;
         int var4 = var3 != 0 && !this.b ? 1 : 0;
         this.b = (boolean)var3;
         if (var4 != 0 && this.t && this.q() && !this.w()) {
            this.S(Minecraft.getInstance());
         }
      }
   }

   private void S(Minecraft var1) {
      NonNullList var2 = this.E.getItems();
      if (v(var2) != null
         && this.G[0] >= 0
         && ItemStack.matches((ItemStack)var2.get(this.G[0]), this.m[0])
         && ItemStack.matches((ItemStack)var2.get(this.G[1]), this.m[1])
         && this.E.getCarried().isEmpty()) {
         this.i = -1;

         for (int var3 = 0; var3 < 9; var3++) {
            if (var1.player.getInventory().getItem(var3).isEmpty() && var3 != var1.player.getInventory().getSelectedSlot()) {
               this.i = var3;
               break;
            }
         }

         if (this.i >= 0) {
            this.K = this.T();
            this.u = this.G[this.K - 1];
            this.H = this.G[2 - this.K];
            this.Z = ((ItemStack)var2.get(this.u)).copy();
            this.l = ((ItemStack)var2.get(this.H)).copy();
            this.w = c6.q.k;
            this.k = System.nanoTime() + 15000000000L;
            this.T(this.H, this.i);
         }
      }
   }

   private void r(String var1) {
      this.w = c6.q.n;
      this.t = (boolean)0;
      this.i = -1;
      xa.q("50/50", "Stopped: " + var1 + "; held item remains real, no replay");
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu == this.E) {
         var2.gui.setScreen(new DispenserScreen(this.E, var2.player.getInventory(), Component.literal("50/50 recovery")));
      }
   }

   public void k(Minecraft var1) {
      if (var1.player == null || var1.level == null) {
         this.E = null;
         this.w = c6.q.H;
         this.V = this.T = this.b = (boolean)0;
         this.i = -1;
      } else if (this.w == c6.q.n) {
         if (var1.player.containerMenu != this.E) {
            this.w = c6.q.H;
            this.E = null;
            this.T = (boolean)0;
         }
      } else if (this.w()) {
         if (var1.player.containerMenu != this.E) {
            this.r("container closed during transfer");
         } else {
            if (this.h()) {
               if (!this.t || this.T() != this.K || System.nanoTime() > this.k) {
                  this.S();
               }
            } else if (System.nanoTime() > this.k) {
               this.r("server confirmation timed out");
            }
         }
      } else {
         if (this.E != null && var1.player.containerMenu != this.E) {
            this.E = null;
            this.T = (boolean)0;
         }

         if (!this.t) {
            if (this.q() && var1.gui.screen() == null) {
               var1.player.closeContainer();
            }

            this.V = (boolean)0;
         } else if (!this.q()) {
            if ((!this.V || System.nanoTime() >= this.k) && var1.gui.screen() == null && var1.player.containerMenu == var1.player.inventoryMenu) {
               if (System.nanoTime() >= this.f) {
                  this.v = this.d();
                  if (this.v != null && !(var1.player.distanceToSqr(Vec3.atCenterOf(this.v)) > 16.0)) {
                     this.V = (boolean)1;
                     this.T = (boolean)0;
                     this.k = System.nanoTime() + 5000000000L;
                     this.f = System.nanoTime() + 5000000000L;
                     this.U = (boolean)1;

                     try {
                        var1.gameMode
                           .useItemOn(var1.player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(this.v), Direction.UP, this.v, false));
                     } finally {
                        this.U = (boolean)0;
                     }
                  }
               }
            }
         }
      }
   }

   private enum q {
      H,
      k,
      b,
      X,
      n;
   }
}

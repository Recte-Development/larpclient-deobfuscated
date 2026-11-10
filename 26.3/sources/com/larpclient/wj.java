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

public final class wj {
   private static final wj L = new wj();
   private boolean g;
   private boolean c;
   private boolean K;
   private boolean I;
   private boolean Z;
   private boolean x;
   private boolean v;
   public Item a = Items.APPLE;
   public Item o = Items.DIAMOND;
   public boolean b = (boolean)1;
   private int w = -1;
   private int P = -1;
   private int p = -1;
   private DispenserMenu n;
   private BlockPos r;
   private final ItemStack[] l = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
   private final int[] B = new int[]{-1, -1};
   private ItemStack O = ItemStack.EMPTY;
   private ItemStack E = ItemStack.EMPTY;
   private int f;
   private wj.v F = wj.v.l;
   private long z;
   private long q;

   public static wj m() {
      return L;
   }

   public boolean J() {
      return this.g;
   }

   public boolean r() {
      return this.F != wj.v.l;
   }

   public int w() {
      return this.b ? 1 : 2;
   }

   public ItemStack F(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      return this.w >= 0 && this.r() && var2.player != null && var1 == var2.player.getInventory().getItem(this.w) ? ItemStack.EMPTY : var1;
   }

   public ItemStack w(Minecraft var1, AbstractContainerMenu var2, Slot var3) {
      if (this.F != wj.v.V && this.r() && var2 == this.n && this.M()) {
         if (var3.container == var1.player.getInventory()) {
            return var3.getContainerSlot() == this.w ? ItemStack.EMPTY : null;
         } else {
            return var3.index == this.P && this.n.getSlot(this.P) == var3 ? this.O : null;
         }
      } else {
         return null;
      }
   }

   public String L(int var1) {
      return this.l[var1].isEmpty() ? "ITEM " + (var1 + 1) : this.l[var1].getHoverName().getString();
   }

   public boolean M() {
      Minecraft var1 = Minecraft.getInstance();
      return this.I && this.n != null && var1.player != null && var1.player.containerMenu == this.n;
   }

   public void P(int var1) {
      if ((!this.r() || this.F == wj.v.c) && var1 >= 1 && var1 <= 2) {
         this.b = (boolean)(var1 == 1 ? 1 : 0);
      }
   }

   public void O(boolean var1) {
      this.g = var1;
   }

   private BlockPos T() {
      return hk.e().f(hk.k.B);
   }

   public void r(Minecraft var1, DispenserMenu var2, BlockPos var3) {
      if (!this.r() && var3 != null) {
         this.n = var2;
         this.r = var3.immutable();
         this.I = (boolean)0;
      }
   }

   public boolean l(MenuType<?> var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.c && var1 == MenuType.GENERIC_3x3 && var3.player != null) {
         this.n = new DispenserMenu(var2, var3.player.getInventory());
         var3.player.containerMenu = this.n;
         this.c = (boolean)0;
         this.I = (boolean)0;
         return true;
      } else {
         return false;
      }
   }

   static int[] x(List<ItemStack> var0) {
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

   public void n(int var1, List<ItemStack> var2, ItemStack var3) {
      if (this.n != null && var1 == this.n.containerId && var2.size() == this.n.slots.size()) {
         this.I = (boolean)1;
         if (this.F == wj.v.l) {
            int[] var4 = x(var2);
            if (var4 != null) {
               for (int var5 = 0; var5 < 2; var5++) {
                  this.B[var5] = var4[var5];
                  this.l[var5] = ((ItemStack)var2.get(var4[var5])).copy();
               }

               this.a = this.l[0].getItem();
               this.o = this.l[1].getItem();
            }
         }

         this.l(var2, var3);
      }
   }

   public void D(int var1, int var2, ItemStack var3) {
      if (this.n != null && this.r() && this.F != wj.v.V) {
         if (var1 == this.n.containerId || var1 == 0 && var2 == 36 + this.w) {
            if (this.F == wj.v.u || this.F == wj.v.j) {
               if (var2 == this.P) {
                  this.x = this.F == wj.v.u ? var3.isEmpty() : ItemStack.matches(var3, this.O);
               }

               if (var2 == 36 + this.w) {
                  this.v = this.F == wj.v.u ? ItemStack.matches(var3, this.O) : var3.isEmpty();
               }

               if (this.x && this.v && this.n.getCarried().isEmpty()) {
                  if (this.F == wj.v.u) {
                     this.d(!ItemStack.matches(this.n.getSlot(this.p).getItem(), this.E));
                  } else {
                     this.F();
                  }
               }
            }

            if (this.F == wj.v.c && var2 == this.p && !ItemStack.matches(var3, this.E)) {
               this.T();
            }
         }
      }
   }

   public void x(int var1, ItemStack var2) {
      if (this.n != null && var1 == this.w) {
         this.D(this.n.containerId, 36 + var1, var2);
      }
   }

   private void d(boolean var1) {
      this.F = wj.v.c;
      this.z = System.nanoTime() + 15000000000L;
      bn.j("50/50", "Trigger swap confirmed: " + this.E.getHoverName().getString());
      if (!this.g || this.w() != this.f || var1) {
         this.T();
      }
   }

   private void F() {
      this.F = wj.v.l;
      this.O = ItemStack.EMPTY;
      this.w = -1;
      this.E = ItemStack.EMPTY;
      bn.j("50/50", "Returned held item; dispenser ready");
   }

   private void l(List<ItemStack> var1, ItemStack var2) {
      if (this.r() && this.F != wj.v.V && this.w >= 0 && var2.isEmpty()) {
         int var3 = 36 + this.w;
         if (this.F == wj.v.u && ((ItemStack)var1.get(this.P)).isEmpty() && ItemStack.matches((ItemStack)var1.get(var3), this.O)) {
            this.d(!ItemStack.matches((ItemStack)var1.get(this.p), this.E));
         } else if (this.F == wj.v.u && ((ItemStack)var1.get(var3)).isEmpty()) {
            this.F = wj.v.l;
            this.w = -1;
            this.O = ItemStack.EMPTY;
            this.E = ItemStack.EMPTY;
            bn.j("50/50", "Missed trigger: removal not accepted; no replay");
         } else if (this.F == wj.v.c && !ItemStack.matches((ItemStack)var1.get(this.p), this.E)) {
            this.T();
         } else if (this.F == wj.v.j && ItemStack.matches((ItemStack)var1.get(this.P), this.O) && ((ItemStack)var1.get(var3)).isEmpty()) {
            this.F();
         }
      }
   }

   private void T() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.M() && this.n.getSlot(this.P).getItem().isEmpty() && ItemStack.matches(var1.player.getInventory().getItem(this.w), this.O)) {
         this.F = wj.v.j;
         this.z = System.nanoTime() + 15000000000L;
         this.e(this.P, this.w);
      } else {
         this.b("return state changed");
      }
   }

   private void e(int var1, int var2) {
      this.x = this.v = (boolean)0;
      Minecraft var3 = Minecraft.getInstance();
      this.K = (boolean)1;

      try {
         var3.gameMode.handleContainerInput(this.n.containerId, var1, var2, ContainerInput.SWAP, var3.player);
      } finally {
         this.K = (boolean)0;
      }
   }

   public int z(AbstractContainerMenu var1, int var2) {
      return this.K && var1 == this.n ? 32768 : var2;
   }

   public boolean Q() {
      return this.r() && !this.K && this.F != wj.v.V;
   }

   public boolean U() {
      return this.F == wj.v.c;
   }

   public boolean E() {
      if (!this.Q()) {
         return false;
      }

      Minecraft var1 = Minecraft.getInstance();
      return !this.U()
         || !(
            var1.hitResult instanceof BlockHitResult var2
               && var1.level.getBlockState(var2.getBlockPos()).is(Blocks.LEVER)
               && this.r != null
               && !(var2.getBlockPos().distSqr(this.r) > 9.0)
         );
   }

   public boolean g(Minecraft var1, InteractionHand var2, BlockHitResult var3) {
      if (this.K) {
         return false;
      }

      if (this.r()) {
         return this.F != wj.v.V;
      }

      if (this.g
         && this.M()
         && var2 == InteractionHand.MAIN_HAND
         && var1.level != null
         && var1.level.getBlockState(var3.getBlockPos()).is(Blocks.LEVER)
         && !(Boolean)var1.level.getBlockState(var3.getBlockPos()).getValue(LeverBlock.POWERED)
         && this.r != null
         && var3.getBlockPos().distSqr(this.r) <= 9.0) {
         this.n(var1);
      }

      return false;
   }

   public void r(BlockPos var1, BlockState var2) {
      if (this.r != null && this.r.equals(var1)) {
         int var3 = var2.hasProperty(DispenserBlock.TRIGGERED) && var2.getValue(DispenserBlock.TRIGGERED) ? 1 : 0;
         int var4 = var3 != 0 && !this.Z ? 1 : 0;
         this.Z = (boolean)var3;
         if (var4 != 0 && this.g && this.M() && !this.r()) {
            this.n(Minecraft.getInstance());
         }
      }
   }

   private void n(Minecraft var1) {
      NonNullList var2 = this.n.getItems();
      if (x(var2) != null
         && this.B[0] >= 0
         && ItemStack.matches((ItemStack)var2.get(this.B[0]), this.l[0])
         && ItemStack.matches((ItemStack)var2.get(this.B[1]), this.l[1])
         && this.n.getCarried().isEmpty()) {
         this.w = -1;

         for (int var3 = 0; var3 < 9; var3++) {
            if (var1.player.getInventory().getItem(var3).isEmpty() && var3 != var1.player.getInventory().getSelectedSlot()) {
               this.w = var3;
               break;
            }
         }

         if (this.w >= 0) {
            this.f = this.w();
            this.p = this.B[this.f - 1];
            this.P = this.B[2 - this.f];
            this.E = ((ItemStack)var2.get(this.p)).copy();
            this.O = ((ItemStack)var2.get(this.P)).copy();
            this.F = wj.v.u;
            this.z = System.nanoTime() + 15000000000L;
            this.e(this.P, this.w);
         }
      }
   }

   private void b(String var1) {
      this.F = wj.v.V;
      this.g = (boolean)0;
      this.w = -1;
      bn.j("50/50", "Stopped: " + var1 + "; held item remains real, no replay");
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu == this.n) {
         var2.gui.setScreen(new DispenserScreen(this.n, var2.player.getInventory(), Component.literal("50/50 recovery")));
      }
   }

   public void Z(Minecraft var1) {
      if (var1.player == null || var1.level == null) {
         this.n = null;
         this.F = wj.v.l;
         this.c = this.I = this.Z = (boolean)0;
         this.w = -1;
      } else if (this.F == wj.v.V) {
         if (var1.player.containerMenu != this.n) {
            this.F = wj.v.l;
            this.n = null;
            this.I = (boolean)0;
         }
      } else if (this.r()) {
         if (var1.player.containerMenu != this.n) {
            this.b("container closed during transfer");
         } else {
            if (this.U()) {
               if (!this.g || this.w() != this.f || System.nanoTime() > this.z) {
                  this.T();
               }
            } else if (System.nanoTime() > this.z) {
               this.b("server confirmation timed out");
            }
         }
      } else {
         if (this.n != null && var1.player.containerMenu != this.n) {
            this.n = null;
            this.I = (boolean)0;
         }

         if (!this.g) {
            if (this.M() && var1.gui.screen() == null) {
               var1.player.closeContainer();
            }

            this.c = (boolean)0;
         } else if (!this.M()) {
            if ((!this.c || System.nanoTime() >= this.z) && var1.gui.screen() == null && var1.player.containerMenu == var1.player.inventoryMenu) {
               if (System.nanoTime() >= this.q) {
                  this.r = this.T();
                  if (this.r != null && !(var1.player.distanceToSqr(Vec3.atCenterOf(this.r)) > 16.0)) {
                     this.c = (boolean)1;
                     this.I = (boolean)0;
                     this.z = System.nanoTime() + 5000000000L;
                     this.q = System.nanoTime() + 5000000000L;
                     this.K = (boolean)1;

                     try {
                        var1.gameMode
                           .useItemOn(var1.player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(this.r), Direction.UP, this.r, false));
                     } finally {
                        this.K = (boolean)0;
                     }
                  }
               }
            }
         }
      }
   }

   private enum v {
      l,
      u,
      c,
      j,
      V;
   }
}

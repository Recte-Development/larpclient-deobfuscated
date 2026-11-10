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

public final class pw {
   private static final pw w = new pw();
   private boolean U;
   private boolean z;
   private boolean A;
   private boolean b;
   private boolean Q;
   private boolean G;
   private boolean L;
   public Item R = Items.APPLE;
   public Item X = Items.DIAMOND;
   public boolean E = (boolean)1;
   private int t = -1;
   private int q = -1;
   private int a = -1;
   private DispenserMenu D;
   private BlockPos g;
   private final ItemStack[] Z = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
   private final int[] e = new int[]{-1, -1};
   private ItemStack i = ItemStack.EMPTY;
   private ItemStack M = ItemStack.EMPTY;
   private int I;
   private pw.z S = pw.z.J;
   private long p;
   private long j;

   public static pw S() {
      return w;
   }

   public boolean K() {
      return this.U;
   }

   public boolean P() {
      return this.S != pw.z.J;
   }

   public int Z() {
      return this.E ? 1 : 2;
   }

   public ItemStack C(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      return this.t >= 0 && this.P() && var2.player != null && var1 == var2.player.getInventory().getItem(this.t) ? ItemStack.EMPTY : var1;
   }

   public ItemStack k(Minecraft var1, AbstractContainerMenu var2, Slot var3) {
      if (this.S != pw.z.T && this.P() && var2 == this.D && this.X()) {
         if (var3.container == var1.player.getInventory()) {
            return var3.getContainerSlot() == this.t ? ItemStack.EMPTY : null;
         } else {
            return var3.index == this.q && this.D.getSlot(this.q) == var3 ? this.i : null;
         }
      } else {
         return null;
      }
   }

   public String d(int var1) {
      return this.Z[var1].isEmpty() ? "ITEM " + (var1 + 1) : this.Z[var1].getHoverName().getString();
   }

   public boolean X() {
      Minecraft var1 = Minecraft.getInstance();
      return this.b && this.D != null && var1.player != null && var1.player.containerMenu == this.D;
   }

   public void m(int var1) {
      if ((!this.P() || this.S == pw.z.x) && var1 >= 1 && var1 <= 2) {
         this.E = (boolean)(var1 == 1 ? 1 : 0);
      }
   }

   public void Y(boolean var1) {
      this.U = var1;
   }

   private BlockPos T() {
      return gj.c().q(gj.d.V);
   }

   public void l(Minecraft var1, DispenserMenu var2, BlockPos var3) {
      if (!this.P() && var3 != null) {
         this.D = var2;
         this.g = var3.immutable();
         this.b = (boolean)0;
      }
   }

   public boolean N(MenuType<?> var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.z && var1 == MenuType.GENERIC_3x3 && var3.player != null) {
         this.D = new DispenserMenu(var2, var3.player.getInventory());
         var3.player.containerMenu = this.D;
         this.z = (boolean)0;
         this.b = (boolean)0;
         return true;
      } else {
         return false;
      }
   }

   static int[] X(List<ItemStack> var0) {
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

   public void J(int var1, List<ItemStack> var2, ItemStack var3) {
      if (this.D != null && var1 == this.D.containerId && var2.size() == this.D.slots.size()) {
         this.b = (boolean)1;
         if (this.S == pw.z.J) {
            int[] var4 = X(var2);
            if (var4 != null) {
               for (int var5 = 0; var5 < 2; var5++) {
                  this.e[var5] = var4[var5];
                  this.Z[var5] = ((ItemStack)var2.get(var4[var5])).copy();
               }

               this.R = this.Z[0].getItem();
               this.X = this.Z[1].getItem();
            }
         }

         this.A(var2, var3);
      }
   }

   public void z(int var1, int var2, ItemStack var3) {
      if (this.D != null && this.P() && this.S != pw.z.T) {
         if (var1 == this.D.containerId || var1 == 0 && var2 == 36 + this.t) {
            if (this.S == pw.z.t || this.S == pw.z.Q) {
               if (var2 == this.q) {
                  this.G = this.S == pw.z.t ? var3.isEmpty() : ItemStack.matches(var3, this.i);
               }

               if (var2 == 36 + this.t) {
                  this.L = this.S == pw.z.t ? ItemStack.matches(var3, this.i) : var3.isEmpty();
               }

               if (this.G && this.L && this.D.getCarried().isEmpty()) {
                  if (this.S == pw.z.t) {
                     this.k(!ItemStack.matches(this.D.getSlot(this.a).getItem(), this.M));
                  } else {
                     this.p();
                  }
               }
            }

            if (this.S == pw.z.x && var2 == this.a && !ItemStack.matches(var3, this.M)) {
               this.J();
            }
         }
      }
   }

   public void O(int var1, ItemStack var2) {
      if (this.D != null && var1 == this.t) {
         this.z(this.D.containerId, 36 + var1, var2);
      }
   }

   private void k(boolean var1) {
      this.S = pw.z.x;
      this.p = System.nanoTime() + 15000000000L;
      te.Z("50/50", "Trigger swap confirmed: " + this.M.getHoverName().getString());
      if (!this.U || this.Z() != this.I || var1) {
         this.J();
      }
   }

   private void p() {
      this.S = pw.z.J;
      this.i = ItemStack.EMPTY;
      this.t = -1;
      this.M = ItemStack.EMPTY;
      te.Z("50/50", "Returned held item; dispenser ready");
   }

   private void A(List<ItemStack> var1, ItemStack var2) {
      if (this.P() && this.S != pw.z.T && this.t >= 0 && var2.isEmpty()) {
         int var3 = 36 + this.t;
         if (this.S == pw.z.t && ((ItemStack)var1.get(this.q)).isEmpty() && ItemStack.matches((ItemStack)var1.get(var3), this.i)) {
            this.k(!ItemStack.matches((ItemStack)var1.get(this.a), this.M));
         } else if (this.S == pw.z.t && ((ItemStack)var1.get(var3)).isEmpty()) {
            this.S = pw.z.J;
            this.t = -1;
            this.i = ItemStack.EMPTY;
            this.M = ItemStack.EMPTY;
            te.Z("50/50", "Missed trigger: removal not accepted; no replay");
         } else if (this.S == pw.z.x && !ItemStack.matches((ItemStack)var1.get(this.a), this.M)) {
            this.J();
         } else if (this.S == pw.z.Q && ItemStack.matches((ItemStack)var1.get(this.q), this.i) && ((ItemStack)var1.get(var3)).isEmpty()) {
            this.p();
         }
      }
   }

   private void J() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.X() && this.D.getSlot(this.q).getItem().isEmpty() && ItemStack.matches(var1.player.getInventory().getItem(this.t), this.i)) {
         this.S = pw.z.Q;
         this.p = System.nanoTime() + 15000000000L;
         this.D(this.q, this.t);
      } else {
         this.Y("return state changed");
      }
   }

   private void D(int var1, int var2) {
      this.G = this.L = (boolean)0;
      Minecraft var3 = Minecraft.getInstance();
      this.A = (boolean)1;

      try {
         var3.gameMode.handleContainerInput(this.D.containerId, var1, var2, ContainerInput.SWAP, var3.player);
      } finally {
         this.A = (boolean)0;
      }
   }

   public int n(AbstractContainerMenu var1, int var2) {
      return this.A && var1 == this.D ? 32768 : var2;
   }

   public boolean C() {
      return this.P() && !this.A && this.S != pw.z.T;
   }

   public boolean Z() {
      return this.S == pw.z.x;
   }

   public boolean u() {
      if (!this.C()) {
         return false;
      }

      Minecraft var1 = Minecraft.getInstance();
      return !this.Z()
         || !(
            var1.hitResult instanceof BlockHitResult var2
               && var1.level.getBlockState(var2.getBlockPos()).is(Blocks.LEVER)
               && this.g != null
               && !(var2.getBlockPos().distSqr(this.g) > 9.0)
         );
   }

   public boolean z(Minecraft var1, InteractionHand var2, BlockHitResult var3) {
      if (this.A) {
         return false;
      }

      if (this.P()) {
         return this.S != pw.z.T;
      }

      if (this.U
         && this.X()
         && var2 == InteractionHand.MAIN_HAND
         && var1.level != null
         && var1.level.getBlockState(var3.getBlockPos()).is(Blocks.LEVER)
         && !(Boolean)var1.level.getBlockState(var3.getBlockPos()).getValue(LeverBlock.POWERED)
         && this.g != null
         && var3.getBlockPos().distSqr(this.g) <= 9.0) {
         this.X(var1);
      }

      return false;
   }

   public void B(BlockPos var1, BlockState var2) {
      if (this.g != null && this.g.equals(var1)) {
         int var3 = var2.hasProperty(DispenserBlock.TRIGGERED) && var2.getValue(DispenserBlock.TRIGGERED) ? 1 : 0;
         int var4 = var3 != 0 && !this.Q ? 1 : 0;
         this.Q = (boolean)var3;
         if (var4 != 0 && this.U && this.X() && !this.P()) {
            this.X(Minecraft.getInstance());
         }
      }
   }

   private void X(Minecraft var1) {
      NonNullList var2 = this.D.getItems();
      if (X(var2) != null
         && this.e[0] >= 0
         && ItemStack.matches((ItemStack)var2.get(this.e[0]), this.Z[0])
         && ItemStack.matches((ItemStack)var2.get(this.e[1]), this.Z[1])
         && this.D.getCarried().isEmpty()) {
         this.t = -1;

         for (int var3 = 0; var3 < 9; var3++) {
            if (var1.player.getInventory().getItem(var3).isEmpty() && var3 != var1.player.getInventory().getSelectedSlot()) {
               this.t = var3;
               break;
            }
         }

         if (this.t >= 0) {
            this.I = this.Z();
            this.a = this.e[this.I - 1];
            this.q = this.e[2 - this.I];
            this.M = ((ItemStack)var2.get(this.a)).copy();
            this.i = ((ItemStack)var2.get(this.q)).copy();
            this.S = pw.z.t;
            this.p = System.nanoTime() + 15000000000L;
            this.D(this.q, this.t);
         }
      }
   }

   private void Y(String var1) {
      this.S = pw.z.T;
      this.U = (boolean)0;
      this.t = -1;
      te.Z("50/50", "Stopped: " + var1 + "; held item remains real, no replay");
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu == this.D) {
         var2.gui.setScreen(new DispenserScreen(this.D, var2.player.getInventory(), Component.literal("50/50 recovery")));
      }
   }

   public void k(Minecraft var1) {
      if (var1.player == null || var1.level == null) {
         this.D = null;
         this.S = pw.z.J;
         this.z = this.b = this.Q = (boolean)0;
         this.t = -1;
      } else if (this.S == pw.z.T) {
         if (var1.player.containerMenu != this.D) {
            this.S = pw.z.J;
            this.D = null;
            this.b = (boolean)0;
         }
      } else if (this.P()) {
         if (var1.player.containerMenu != this.D) {
            this.Y("container closed during transfer");
         } else {
            if (this.Z()) {
               if (!this.U || this.Z() != this.I || System.nanoTime() > this.p) {
                  this.J();
               }
            } else if (System.nanoTime() > this.p) {
               this.Y("server confirmation timed out");
            }
         }
      } else {
         if (this.D != null && var1.player.containerMenu != this.D) {
            this.D = null;
            this.b = (boolean)0;
         }

         if (!this.U) {
            if (this.X() && var1.gui.screen() == null) {
               var1.player.closeContainer();
            }

            this.z = (boolean)0;
         } else if (!this.X()) {
            if ((!this.z || System.nanoTime() >= this.p) && var1.gui.screen() == null && var1.player.containerMenu == var1.player.inventoryMenu) {
               if (System.nanoTime() >= this.j) {
                  this.g = this.T();
                  if (this.g != null && !(var1.player.distanceToSqr(Vec3.atCenterOf(this.g)) > 16.0)) {
                     this.z = (boolean)1;
                     this.b = (boolean)0;
                     this.p = System.nanoTime() + 5000000000L;
                     this.j = System.nanoTime() + 5000000000L;
                     this.A = (boolean)1;

                     try {
                        var1.gameMode
                           .useItemOn(var1.player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(this.g), Direction.UP, this.g, false));
                     } finally {
                        this.A = (boolean)0;
                     }
                  }
               }
            }
         }
      }
   }

   private enum z {
      J,
      t,
      x,
      Q,
      T;
   }
}

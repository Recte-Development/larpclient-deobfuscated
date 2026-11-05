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

public final class ro {
   private static final ro M = new ro();
   private boolean D;
   private boolean X;
   private boolean d;
   private boolean L;
   private boolean l;
   private boolean P;
   private boolean N;
   public Item B = Items.APPLE;
   public Item w = Items.DIAMOND;
   public boolean a = (boolean)1;
   private int i = -1;
   private int I = -1;
   private int r = -1;
   private DispenserMenu j;
   private BlockPos H;
   private final ItemStack[] C = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
   private final int[] t = new int[]{-1, -1};
   private ItemStack q = ItemStack.EMPTY;
   private ItemStack O = ItemStack.EMPTY;
   private int Z;
   private ro.z E = ro.z.u;
   private long y;
   private long W;

   public static ro H() {
      return M;
   }

   public boolean z() {
      return this.D;
   }

   public boolean I() {
      return this.E != ro.z.u;
   }

   public int W() {
      return this.a ? 1 : 2;
   }

   public ItemStack n(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      return this.i >= 0 && this.I() && var2.player != null && var1 == var2.player.getInventory().getItem(this.i) ? ItemStack.EMPTY : var1;
   }

   public ItemStack F(Minecraft var1, AbstractContainerMenu var2, Slot var3) {
      if (this.E != ro.z.W && this.I() && var2 == this.j && this.h()) {
         if (var3.container == var1.player.getInventory()) {
            return var3.getContainerSlot() == this.i ? ItemStack.EMPTY : null;
         } else {
            return var3.index == this.I && this.j.getSlot(this.I) == var3 ? this.q : null;
         }
      } else {
         return null;
      }
   }

   public String J(int var1) {
      return this.C[var1].isEmpty() ? "ITEM " + (var1 + 1) : this.C[var1].getHoverName().getString();
   }

   public boolean h() {
      Minecraft var1 = Minecraft.getInstance();
      return this.L && this.j != null && var1.player != null && var1.player.containerMenu == this.j;
   }

   public void L(int var1) {
      if ((!this.I() || this.E == ro.z.E) && var1 >= 1 && var1 <= 2) {
         this.a = (boolean)(var1 == 1 ? 1 : 0);
      }
   }

   public void X(boolean var1) {
      this.D = var1;
   }

   private BlockPos H() {
      return f9.W().R(f9.b.j);
   }

   public void m(Minecraft var1, DispenserMenu var2, BlockPos var3) {
      if (!this.I() && var3 != null) {
         this.j = var2;
         this.H = var3.immutable();
         this.L = (boolean)0;
      }
   }

   public boolean S(MenuType<?> var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.X && var1 == MenuType.GENERIC_3x3 && var3.player != null) {
         this.j = new DispenserMenu(var2, var3.player.getInventory());
         var3.player.containerMenu = this.j;
         this.X = (boolean)0;
         this.L = (boolean)0;
         return true;
      } else {
         return false;
      }
   }

   static int[] Y(List<ItemStack> var0) {
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

   public void T(int var1, List<ItemStack> var2, ItemStack var3) {
      if (this.j != null && var1 == this.j.containerId && var2.size() == this.j.slots.size()) {
         this.L = (boolean)1;
         if (this.E == ro.z.u) {
            int[] var4 = Y(var2);
            if (var4 != null) {
               for (int var5 = 0; var5 < 2; var5++) {
                  this.t[var5] = var4[var5];
                  this.C[var5] = ((ItemStack)var2.get(var4[var5])).copy();
               }

               this.B = this.C[0].getItem();
               this.w = this.C[1].getItem();
            }
         }

         this.u(var2, var3);
      }
   }

   public void O(int var1, int var2, ItemStack var3) {
      if (this.j != null && this.I() && this.E != ro.z.W) {
         if (var1 == this.j.containerId || var1 == 0 && var2 == 36 + this.i) {
            if (this.E == ro.z.d || this.E == ro.z.B) {
               if (var2 == this.I) {
                  this.P = this.E == ro.z.d ? var3.isEmpty() : ItemStack.matches(var3, this.q);
               }

               if (var2 == 36 + this.i) {
                  this.N = this.E == ro.z.d ? ItemStack.matches(var3, this.q) : var3.isEmpty();
               }

               if (this.P && this.N && this.j.getCarried().isEmpty()) {
                  if (this.E == ro.z.d) {
                     this.L(!ItemStack.matches(this.j.getSlot(this.r).getItem(), this.O));
                  } else {
                     this.x();
                  }
               }
            }

            if (this.E == ro.z.E && var2 == this.r && !ItemStack.matches(var3, this.O)) {
               this.U();
            }
         }
      }
   }

   public void e(int var1, ItemStack var2) {
      if (this.j != null && var1 == this.i) {
         this.O(this.j.containerId, 36 + var1, var2);
      }
   }

   private void L(boolean var1) {
      this.E = ro.z.E;
      this.y = System.nanoTime() + 15000000000L;
      hh.K("50/50", "Trigger swap confirmed: " + this.O.getHoverName().getString());
      if (!this.D || this.W() != this.Z || var1) {
         this.U();
      }
   }

   private void x() {
      this.E = ro.z.u;
      this.q = ItemStack.EMPTY;
      this.i = -1;
      this.O = ItemStack.EMPTY;
      hh.K("50/50", "Returned held item; dispenser ready");
   }

   private void u(List<ItemStack> var1, ItemStack var2) {
      if (this.I() && this.E != ro.z.W && this.i >= 0 && var2.isEmpty()) {
         int var3 = 36 + this.i;
         if (this.E == ro.z.d && ((ItemStack)var1.get(this.I)).isEmpty() && ItemStack.matches((ItemStack)var1.get(var3), this.q)) {
            this.L(!ItemStack.matches((ItemStack)var1.get(this.r), this.O));
         } else if (this.E == ro.z.d && ((ItemStack)var1.get(var3)).isEmpty()) {
            this.E = ro.z.u;
            this.i = -1;
            this.q = ItemStack.EMPTY;
            this.O = ItemStack.EMPTY;
            hh.K("50/50", "Missed trigger: removal not accepted; no replay");
         } else if (this.E == ro.z.E && !ItemStack.matches((ItemStack)var1.get(this.r), this.O)) {
            this.U();
         } else if (this.E == ro.z.B && ItemStack.matches((ItemStack)var1.get(this.I), this.q) && ((ItemStack)var1.get(var3)).isEmpty()) {
            this.x();
         }
      }
   }

   private void U() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.h() && this.j.getSlot(this.I).getItem().isEmpty() && ItemStack.matches(var1.player.getInventory().getItem(this.i), this.q)) {
         this.E = ro.z.B;
         this.y = System.nanoTime() + 15000000000L;
         this.I(this.I, this.i);
      } else {
         this.f("return state changed");
      }
   }

   private void I(int var1, int var2) {
      this.P = this.N = (boolean)0;
      Minecraft var3 = Minecraft.getInstance();
      this.d = (boolean)1;

      try {
         var3.gameMode.handleContainerInput(this.j.containerId, var1, var2, ContainerInput.SWAP, var3.player);
      } finally {
         this.d = (boolean)0;
      }
   }

   public int P(AbstractContainerMenu var1, int var2) {
      return this.d && var1 == this.j ? 32768 : var2;
   }

   public boolean p() {
      return this.I() && !this.d && this.E != ro.z.W;
   }

   public boolean O() {
      return this.E == ro.z.E;
   }

   public boolean E() {
      if (!this.p()) {
         return false;
      }

      Minecraft var1 = Minecraft.getInstance();
      return !this.O()
         || !(
            var1.hitResult instanceof BlockHitResult var2
               && var1.level.getBlockState(var2.getBlockPos()).is(Blocks.LEVER)
               && this.H != null
               && !(var2.getBlockPos().distSqr(this.H) > 9.0)
         );
   }

   public boolean i(Minecraft var1, InteractionHand var2, BlockHitResult var3) {
      if (this.d) {
         return false;
      }

      if (this.I()) {
         return this.E != ro.z.W;
      }

      if (this.D
         && this.h()
         && var2 == InteractionHand.MAIN_HAND
         && var1.level != null
         && var1.level.getBlockState(var3.getBlockPos()).is(Blocks.LEVER)
         && !(Boolean)var1.level.getBlockState(var3.getBlockPos()).getValue(LeverBlock.POWERED)
         && this.H != null
         && var3.getBlockPos().distSqr(this.H) <= 9.0) {
         this.U(var1);
      }

      return false;
   }

   public void j(BlockPos var1, BlockState var2) {
      if (this.H != null && this.H.equals(var1)) {
         int var3 = var2.hasProperty(DispenserBlock.TRIGGERED) && var2.getValue(DispenserBlock.TRIGGERED) ? 1 : 0;
         int var4 = var3 != 0 && !this.l ? 1 : 0;
         this.l = (boolean)var3;
         if (var4 != 0 && this.D && this.h() && !this.I()) {
            this.U(Minecraft.getInstance());
         }
      }
   }

   private void U(Minecraft var1) {
      NonNullList var2 = this.j.getItems();
      if (Y(var2) != null
         && this.t[0] >= 0
         && ItemStack.matches((ItemStack)var2.get(this.t[0]), this.C[0])
         && ItemStack.matches((ItemStack)var2.get(this.t[1]), this.C[1])
         && this.j.getCarried().isEmpty()) {
         this.i = -1;

         for (int var3 = 0; var3 < 9; var3++) {
            if (var1.player.getInventory().getItem(var3).isEmpty() && var3 != var1.player.getInventory().getSelectedSlot()) {
               this.i = var3;
               break;
            }
         }

         if (this.i >= 0) {
            this.Z = this.W();
            this.r = this.t[this.Z - 1];
            this.I = this.t[2 - this.Z];
            this.O = ((ItemStack)var2.get(this.r)).copy();
            this.q = ((ItemStack)var2.get(this.I)).copy();
            this.E = ro.z.d;
            this.y = System.nanoTime() + 15000000000L;
            this.I(this.I, this.i);
         }
      }
   }

   private void f(String var1) {
      this.E = ro.z.W;
      this.D = (boolean)0;
      this.i = -1;
      hh.K("50/50", "Stopped: " + var1 + "; held item remains real, no replay");
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu == this.j) {
         var2.gui.setScreen(new DispenserScreen(this.j, var2.player.getInventory(), Component.literal("50/50 recovery")));
      }
   }

   public void D(Minecraft var1) {
      if (var1.player == null || var1.level == null) {
         this.j = null;
         this.E = ro.z.u;
         this.X = this.L = this.l = (boolean)0;
         this.i = -1;
      } else if (this.E == ro.z.W) {
         if (var1.player.containerMenu != this.j) {
            this.E = ro.z.u;
            this.j = null;
            this.L = (boolean)0;
         }
      } else if (this.I()) {
         if (var1.player.containerMenu != this.j) {
            this.f("container closed during transfer");
         } else {
            if (this.O()) {
               if (!this.D || this.W() != this.Z || System.nanoTime() > this.y) {
                  this.U();
               }
            } else if (System.nanoTime() > this.y) {
               this.f("server confirmation timed out");
            }
         }
      } else {
         if (this.j != null && var1.player.containerMenu != this.j) {
            this.j = null;
            this.L = (boolean)0;
         }

         if (!this.D) {
            if (this.h() && var1.gui.screen() == null) {
               var1.player.closeContainer();
            }

            this.X = (boolean)0;
         } else if (!this.h()) {
            if ((!this.X || System.nanoTime() >= this.y) && var1.gui.screen() == null && var1.player.containerMenu == var1.player.inventoryMenu) {
               if (System.nanoTime() >= this.W) {
                  this.H = this.H();
                  if (this.H != null && !(var1.player.distanceToSqr(Vec3.atCenterOf(this.H)) > 16.0)) {
                     this.X = (boolean)1;
                     this.L = (boolean)0;
                     this.y = System.nanoTime() + 5000000000L;
                     this.W = System.nanoTime() + 5000000000L;
                     this.d = (boolean)1;

                     try {
                        var1.gameMode
                           .useItemOn(var1.player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(this.H), Direction.UP, this.H, false));
                     } finally {
                        this.d = (boolean)0;
                     }
                  }
               }
            }
         }
      }
   }

   private enum z {
      u,
      d,
      E,
      B,
      W;
   }
}

package com.larpclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.DispenserScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class lh {
   private static final int[] S = new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10};
   private static final long A = 15000000000L;
   private static final double Y = 64.0;
   private static final lh J = new lh();
   private lh.w M = lh.w.r;
   private boolean i;
   private boolean d;
   private boolean q;
   private boolean u;
   private boolean w;
   private boolean H;
   private boolean F;
   private boolean t;
   private boolean k;
   private final int[] l = new int[9];
   private final boolean[] f = new boolean[9];
   private final ItemStack[] B = new ItemStack[9];
   private final List<ItemStack> p = new ArrayList<>();
   private DispenserMenu R;
   private BlockPos K;
   private BlockPos T;
   private long h;
   private long m;
   private int E;
   private int L;
   private int a = -1;
   private int s = -1;
   private lh.m C;
   private boolean c;
   private int b = -1;
   private String g;
   private long X;
   private final int[] n = new int[11];

   public static lh I() {
      return J;
   }

   private lh() {
      Arrays.fill(this.l, -1);
      Arrays.fill(this.n, -1);
   }

   public String W(Minecraft var1) {
      if (var1 != null && var1.player != null) {
         int[] var2 = new int[11];
         Arrays.fill(var2, -1);
         HashSet var3 = new HashSet();

         for (int var4 = 0; var4 < S.length; var4++) {
            int var5 = S.length - 1 - var4;
            ItemStack var6 = var1.player.getInventory().getItem(var5);
            MapId var7 = var6 != null && var6.is(Items.FILLED_MAP) ? (MapId)var6.get(DataComponents.MAP_ID) : null;
            if (var7 == null) {
               return "Visible hotbar slot " + (var4 + 1) + " must hold a filled card map.";
            }

            if (!var3.add(var7.id())) {
               return "Each hotbar slot needs a different card map.";
            }

            var2[S[var4]] = var7.id();
         }

         System.arraycopy(var2, 0, this.n, 0, var2.length);
         StringBuilder var9 = new StringBuilder("Imported hotbar map mapping:");

         for (int var8 : S) {
            var9.append(' ').append(var8 == 1 ? "A" : var8).append("=map#").append(var2[var8]);
         }

         te.Z("Blackjack FO", var9.toString());
         return null;
      } else {
         return "Join a world before importing the hotbar.";
      }
   }

   public int[] L() {
      return (int[])this.n.clone();
   }

   public void w(int[] var1) {
      Arrays.fill(this.n, -1);
      if (var1 != null) {
         System.arraycopy(var1, 0, this.n, 0, Math.min(var1.length, this.n.length));
      }
   }

   public boolean V() {
      for (int var4 : S) {
         if (this.n[var4] < 0) {
            return false;
         }
      }

      return true;
   }

   public int Z(int var1) {
      return var1 >= 1 && var1 <= 10 ? this.n[var1] : -1;
   }

   public int d(int var1) {
      return x(this.n, var1);
   }

   static int x(int[] var0, int var1) {
      if (var1 >= 0 && var0 != null) {
         for (int var5 : S) {
            if (var5 < var0.length && var0[var5] == var1) {
               return var5;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public void f(boolean var1) {
      if (var1 && !this.i) {
         this.m = 0L;
         this.c = (boolean)0;
      }

      this.i = var1;
   }

   public boolean S() {
      return this.i;
   }

   public boolean k() {
      return this.M != lh.w.r;
   }

   public int p() {
      if (this.i && this.M != lh.w.I) {
         return this.M == lh.w.r ? this.j() : this.a;
      } else {
         return -1;
      }
   }

   public int p(int var1, ItemStack var2) {
      if (this.M != lh.w.r && this.M != lh.w.I && var2 != null && !var2.isEmpty()) {
         for (int var3 = 0; var3 < this.l.length; var3++) {
            if (this.l[var3] == var1 && this.B[var3] != null && ItemStack.isSameItemSameComponents(var2, this.B[var3])) {
               return Math.min(var2.getCount(), this.B[var3].getCount());
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   public boolean A() {
      return this.M != lh.w.r && this.M != lh.w.I && !this.u;
   }

   public boolean y() {
      return this.u;
   }

   public boolean w() {
      return this.A();
   }

   public boolean g(Minecraft var1) {
      return var1.gui.screen() instanceof DispenserScreen var2 && var2.getMenu() == this.R;
   }

   public boolean i(Minecraft var1) {
      if (this.i
         && this.R != null
         && var1.player != null
         && var1.player.containerMenu == this.R
         && this.R.getCarried().isEmpty()
         && var1.gui.screen() == null
         && var1.hitResult instanceof BlockHitResult var2
         && var2.getBlockPos().equals(this.o())) {
         var1.gui.setScreen(new DispenserScreen(this.R, var1.player.getInventory(), Component.translatable("container.dropper")));
         return true;
      } else {
         return false;
      }
   }

   public int A(AbstractContainerMenu var1, int var2) {
      return this.u && var1 == this.R ? 32768 : var2;
   }

   public ItemStack z(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.M != lh.w.r && this.M != lh.w.I && var2.player != null) {
         for (int var6 : this.l) {
            if (var6 >= 0 && var1 == var2.player.getInventory().getItem(var6)) {
               return ItemStack.EMPTY;
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public ItemStack j(Minecraft var1, AbstractContainerMenu var2, Slot var3) {
      if (this.M != lh.w.r && this.M != lh.w.I && var2 == this.R && var1.player != null) {
         if (var3.container == var1.player.getInventory()) {
            for (int var7 : this.l) {
               if (var7 == var3.getContainerSlot()) {
                  return ItemStack.EMPTY;
               }
            }

            return null;
         } else {
            return var3.index >= 0 && var3.index < 9 && this.B[var3.index] != null ? this.B[var3.index] : null;
         }
      } else {
         return null;
      }
   }

   private BlockPos o() {
      return gj.c().q(gj.d.a);
   }

   public void N(BlockPos var1) {
      if (!this.u && this.i && this.M == lh.w.r && var1.equals(this.o())) {
         this.K = var1;
         this.q = (boolean)1;
         this.d = (boolean)0;
         this.h = System.nanoTime() + 3000000000L;
      }
   }

   public void A(AbstractContainerMenu var1) {
      if (var1 instanceof DispenserMenu var2) {
         gj var3 = gj.c();
         if (this.q || var3.E() == gj.d.a && var3.r() == var2.containerId) {
            this.R = var2;
            this.p.clear();
            this.q = this.d = this.w = (boolean)0;
         }
      }
   }

   public boolean w(AbstractContainerMenu var1) {
      return this.i && this.M != lh.w.I && var1 == this.R && this.R.getCarried().isEmpty();
   }

   public boolean s(MenuType<?> var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (!this.q && this.d && var1 == MenuType.GENERIC_3x3 && var3.player != null) {
         this.R = new DispenserMenu(var2, var3.player.getInventory());
         var3.player.containerMenu = this.R;
         this.d = this.w = (boolean)0;
         return true;
      } else {
         return false;
      }
   }

   public void V(int var1, List<ItemStack> var2, ItemStack var3) {
      if (this.R != null && var1 == this.R.containerId && var2.size() == this.R.slots.size()) {
         this.p.clear();

         for (ItemStack var5 : var2) {
            this.p.add(var5.copy());
         }

         this.w = (boolean)1;
         this.f(var3);
      }
   }

   public void i(int var1, int var2, ItemStack var3) {
      if (this.R != null && !this.p.isEmpty() && this.M != lh.w.I) {
         if (var1 == this.R.containerId && var2 >= 0 && var2 < this.p.size()) {
            this.p.set(var2, var3.copy());
         } else {
            if (var1 != 0 || U(var2) < 0 || var2 >= this.p.size()) {
               return;
            }

            this.p.set(var2, var3.copy());
         }

         this.f(this.R.getCarried());
      }
   }

   public void h(int var1, ItemStack var2) {
      if (this.R != null && !this.p.isEmpty() && this.M != lh.w.I && var1 >= 0 && var1 < 36) {
         this.p.set(l(var1), var2.copy());
         this.f(this.R.getCarried());
      }
   }

   private static int l(int var0) {
      return var0 < 9 ? 36 + var0 : var0;
   }

   static int V(int var0) {
      for (int var1 = 0; var1 < S.length; var1++) {
         if (S[var1] == var0) {
            return var1;
         }
      }

      return -1;
   }

   static boolean o(int var0, boolean var1, int var2, int var3) {
      return !var1 && var0 + var2 > 21 && var0 + var3 > 21;
   }

   static int U(int var0) {
      return var0 >= 9 && var0 < 45 ? var0 : -1;
   }

   private boolean k(boolean var1) {
      if (this.p.size() < 45) {
         return false;
      }

      for (int var2 = 0; var2 < 9; var2++) {
         if (this.l[var2] >= 0) {
            if (!ItemStack.matches(this.p.get(var2), var1 ? ItemStack.EMPTY : this.B[var2])) {
               return false;
            }

            ItemStack var3 = this.p.get(l(this.l[var2]));
            if (var1 ? !ItemStack.matches(var3, this.B[var2]) : !var3.isEmpty()) {
               return false;
            }
         }
      }

      return true;
   }

   private boolean M() {
      if (this.p.size() < 9) {
         return false;
      }

      for (int var1 = 0; var1 < 9; var1++) {
         if (this.l[var1] < 0 && this.B[var1] != null && !this.B[var1].isEmpty() && !ItemStack.matches(this.p.get(var1), this.B[var1])) {
            return true;
         }
      }

      return false;
   }

   private void f(ItemStack var1) {
      if (var1.isEmpty()) {
         if (this.M == lh.w.A && this.k(true)) {
            this.M = lh.w.G;
            if (!this.M() && !this.t && this.i && s3.S().U() == this.k) {
               te.Z("Blackjack FO", (this.k ? "Dealer" : "Player") + " draw ready");
            } else {
               this.i();
            }
         } else if (this.M != lh.w.G || !this.M() && !this.t && this.k(true)) {
            if (this.M == lh.w.C) {
               this.I(false);
               if (this.M == lh.w.C && this.k(false)) {
                  this.M = lh.w.r;
                  this.t = (boolean)0;
                  Arrays.fill(this.l, -1);
                  te.Z("Blackjack FO", "Other cards returned after dispenser draw");
               }
            }
         } else {
            this.i();
         }
      }
   }

   static int h(boolean var0, boolean var1, boolean var2, boolean var3) {
      if (var0 && var3) {
         return 0;
      } else {
         return var1 && var2 ? 1 : -1;
      }
   }

   static boolean c(int var0, boolean var1) {
      return var0 == 1 && !var1;
   }

   static boolean i(boolean var0, boolean var1, long var2, long var4) {
      return !var0 && !var1 && var2 >= var4;
   }

   private int C(int var1) {
      if (this.p.size() < 45) {
         return -1;
      }

      ItemStack var2 = this.p.get(var1);
      ItemStack var3 = this.p.get(l(this.l[var1]));
      return h(ItemStack.matches(var2, this.B[var1]), var2.isEmpty(), ItemStack.matches(var3, this.B[var1]), var3.isEmpty());
   }

   public void H(BlockPos var1, BlockState var2) {
      if (this.K != null) {
         if (var2.getBlock() instanceof LeverBlock && var1.distSqr(this.K) <= 9.0) {
            boolean var5 = (Boolean)var2.getValue(LeverBlock.POWERED);
            int var6 = !var5 || var1.equals(this.T) && this.F ? 0 : 1;
            this.T = var1.immutable();
            this.F = var5;
            if (var6 != 0) {
               this.M("lever");
            }
         } else if (this.K.equals(var1)) {
            int var3 = var2.getBlock() instanceof DispenserBlock && var2.getValue(DispenserBlock.TRIGGERED) ? 1 : 0;
            int var4 = var3 != 0 && !this.H ? 1 : 0;
            this.H = (boolean)var3;
            if (var4 != 0) {
               this.M("dispenser");
            }
         }
      }
   }

   private void M(String var1) {
      if (this.i && System.nanoTime() >= this.X) {
         int var2 = this.M == lh.w.r ? this.j() : -1;
         if (var2 > 0 && this.X(var2)) {
            this.c = (boolean)1;
            s3 var4 = s3.S();
            this.b = var4.D();
            this.g = var4.N();
            te.Z(
               "Blackjack FO",
               "Started rigging " + var2 + " for " + (var4.U() ? "dealer" : "player") + " on " + var1 + ": player=" + var4.A() + " dealer=" + var4.K()
            );
         } else if (this.M == lh.w.r && (s3.S().U() || s3.S().A() >= 12)) {
            s3 var3 = s3.S();
            te.Z(
               "Blackjack FO",
               (var3.U() ? "Dealer" : "Player")
                  + " draw unforced on "
                  + var1
                  + ": transfer="
                  + this.M
                  + " connected="
                  + this.w
                  + " slots="
                  + this.p.size()
                  + " cards="
                  + var3.D()
                  + " pending="
                  + var3.t()
                  + " player="
                  + var3.A()
                  + " dealer="
                  + var3.K()
            );
         }
      }
   }

   private int j() {
      if (this.w && this.p.size() >= 45) {
         boolean[] var1 = new boolean[11];
         int var2 = 0;

         for (int var3 = 0; var3 < S.length; var3++) {
            ItemStack var4 = this.p.get(var3);
            MapId var5 = var4.is(Items.FILLED_MAP) ? (MapId)var4.get(DataComponents.MAP_ID) : null;
            int var6 = this.V() ? (var5 == null ? -1 : this.d(var5.id())) : S[var3];
            if (var6 >= 1 && var6 <= 10) {
               var1[var6] = (boolean)(var5 != null ? 1 : 0);
               if (var1[var6]) {
                  var2 |= 1 << var6;
               }
            }
         }

         s3 var7 = s3.S();
         if (this.c && (var7.D() != this.b || !var7.N().equals(this.g))) {
            this.c = (boolean)0;
         }

         lh.m var8 = new lh.m(var7.N(), var7.A(), var7.K(), var7.u(), var7.K(), var7.D(), var7.t(), var2);
         if (!var8.equals(this.C)) {
            this.C = var8;
            this.s = var7.U() ? var7.f(var1) : var7.l(var1);
         }

         return this.c ? -1 : this.s;
      } else {
         this.C = null;
         this.s = -1;
         return -1;
      }
   }

   public void V(BlockPos var1) {
      if (this.K != null && this.K.equals(var1) && (this.M == lh.w.A || this.M == lh.w.G)) {
         this.t = (boolean)1;
         if (this.M == lh.w.G) {
            this.i();
         }
      }
   }

   public void H(BlockPos var1) {
      if (this.i && this.K != null && var1.distSqr(this.K) <= 9.0) {
         this.X = System.nanoTime() + 2000000000L;
      }
   }

   static int[] y(int[] var0, boolean[] var1, boolean[] var2, int var3, int var4) {
      if (var0.length == 9 && var1.length == 9 && var2.length == 36 && var3 >= 0 && var3 < 9) {
         int var5 = -1;

         for (int var6 = 0; var6 < 9; var6++) {
            if (var1[var6] && var0[var6] == var4) {
               if (var5 >= 0) {
                  return null;
               }

               var5 = var6;
            }
         }

         if (var5 < 0) {
            return null;
         }

         int[] var11 = new int[9];
         Arrays.fill(var11, -1);
         var11[var5] = -2;
         boolean[] var7 = (boolean[])var2.clone();
         var7[var3] = (boolean)1;

         for (int var8 = 0; var8 < 9; var8++) {
            if (var1[var8] && var8 != var5) {
               int var9 = -1;

               for (int var10 = 0; var10 < 9; var10++) {
                  if (!var7[var10]) {
                     var9 = var10;
                     break;
                  }
               }

               if (var9 < 0) {
                  for (int var12 = 35; var12 >= 9; var12--) {
                     if (!var7[var12]) {
                        var9 = var12;
                        break;
                     }
                  }
               }

               if (var9 < 0) {
                  return null;
               }

               var11[var8] = var9;
               var7[var9] = (boolean)1;
            }
         }

         return var11;
      } else {
         return null;
      }
   }

   private boolean X(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.w && this.R != null && var2.player != null && var2.player.containerMenu == this.R && this.R.getCarried().isEmpty() && this.p.size() >= 45) {
         int var3 = this.t(var1);
         MapId var4 = var3 < 0 ? null : (MapId)this.p.get(var3).get(DataComponents.MAP_ID);
         int var5 = var4 == null ? -1 : var4.id();
         if (var5 < 0) {
            te.Z("Blackjack FO", "Skipped draw: no card template for rank " + var1);
            return false;
         }

         int[] var6 = new int[9];
         boolean[] var7 = new boolean[9];
         boolean[] var8 = new boolean[36];
         s3 var9 = s3.S();
         this.k = var9.U();
         int var10 = var9.K();
         boolean var11 = var9.K();

         for (int var12 = 0; var12 < 9; var12++) {
            ItemStack var13 = this.p.get(var12);
            MapId var14 = var13.is(Items.FILLED_MAP) ? (MapId)var13.get(DataComponents.MAP_ID) : null;
            var6[var12] = var14 == null ? -1 : var14.id();
            int var15 = this.V() ? (var14 == null ? -1 : this.d(var14.id())) : S[var12];
            var7[var12] = (boolean)(var13.isEmpty()
                  || var12 != var3 && var14 != null && var15 >= 1 && (this.k ? o(var10, var11, var1, var15) : var9.r(var1, var15))
               ? 0
               : 1);
         }

         for (int var21 = 0; var21 < 36; var21++) {
            var8[var21] = (boolean)(this.p.get(l(var21)).isEmpty() && var2.player.getInventory().getItem(var21).isEmpty() ? 0 : 1);
         }

         int[] var22 = y(var6, var7, var8, var2.player.getInventory().getSelectedSlot(), var5);
         if (var22 == null) {
            te.Z("Blackjack FO", "Skipped draw: card missing/duplicated or not enough empty inventory slots");
            return false;
         }

         Arrays.fill(this.l, -1);
         Arrays.fill(this.f, false);

         for (int var23 = 0; var23 < 9; var23++) {
            this.B[var23] = this.p.get(var23).copy();
            this.l[var23] = var22[var23] >= 0 ? var22[var23] : -1;
         }

         this.M = lh.w.A;
         this.a = var1;
         this.t = (boolean)0;
         this.E = 0;
         this.L = 0;
         this.h = System.nanoTime() + 15000000000L;
         this.u = (boolean)1;

         try {
            for (int var24 = 0; var24 < 9; var24++) {
               if (this.l[var24] >= 0) {
                  this.k(var24, this.l[var24], false, var2);
               }
            }
         } catch (RuntimeException var19) {
            this.X("removal click failed");
            return false;
         } finally {
            this.u = (boolean)0;
         }

         return true;
      } else {
         te.Z("Blackjack FO", "Skipped draw: dispenser not ready");
         return false;
      }
   }

   private int t(int var1) {
      if (!this.V()) {
         return V(var1);
      }

      int var2 = this.Z(var1);
      if (var2 >= 0 && this.p.size() >= 9) {
         int var3 = -1;

         for (int var4 = 0; var4 < 9; var4++) {
            MapId var5 = this.p.get(var4).is(Items.FILLED_MAP) ? (MapId)this.p.get(var4).get(DataComponents.MAP_ID) : null;
            if (var5 != null && var5.id() == var2) {
               if (var3 >= 0) {
                  return -1;
               }

               var3 = var4;
            }
         }

         return var3;
      } else {
         return -1;
      }
   }

   private void i() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.M != lh.w.I) {
         if (var1.player != null && var1.player.containerMenu == this.R && this.R.getCarried().isEmpty() && this.p.size() >= 45) {
            this.M = lh.w.C;
            this.h = System.nanoTime() + 15000000000L;
            Arrays.fill(this.f, false);
            this.I(true);
            this.f(this.R.getCarried());
         } else {
            this.X("return state changed");
         }
      }
   }

   private void I(boolean var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu == this.R && this.p.size() >= 45) {
         StringBuilder var3 = new StringBuilder();
         this.u = (boolean)1;

         try {
            int var4 = 0;

            for (int var5 = 0; var5 < 9; var5++) {
               if (this.l[var5] >= 0) {
                  int var6 = this.C(var5);
                  if (var6 == 0) {
                     var4++;
                  } else {
                     if (var4 > this.L) {
                        this.L = var4;
                        this.E = 0;
                        this.h = System.nanoTime() + 15000000000L;
                     }

                     if (var6 < 0) {
                        if (var1) {
                           var3.append(' ').append(var5 + 1).append('[').append(this.p.get(var5)).append(" / ").append(this.p.get(l(this.l[var5]))).append(']');
                        }
                     } else if (c(var6, this.f[var5])) {
                        this.k(var5, this.l[var5], true, var2);
                        this.f[var5] = (boolean)1;
                     }
                  }
               }
            }
         } catch (RuntimeException var10) {
            this.X("return click failed");
         } finally {
            this.u = (boolean)0;
         }

         if (!var3.isEmpty()) {
            te.Z("Blackjack FO", "Waiting for changed slots:" + var3);
         }
      }
   }

   private void k(int var1, int var2, boolean var3, Minecraft var4) {
      if (var2 < 9) {
         var4.gameMode.handleContainerInput(this.R.containerId, var1, var2, ContainerInput.SWAP, var4.player);
      } else {
         int var5 = var3 ? l(var2) : var1;
         int var6 = var3 ? var1 : l(var2);
         var4.gameMode.handleContainerInput(this.R.containerId, var5, 0, ContainerInput.PICKUP, var4.player);
         var4.gameMode.handleContainerInput(this.R.containerId, var6, 0, ContainerInput.PICKUP, var4.player);
      }
   }

   private void X(String var1) {
      this.M = lh.w.I;
      this.i = (boolean)0;
      te.Z("Blackjack FO", "Stopped: " + var1 + "; inspect real slots, no automatic replay");
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu == this.R) {
         var2.gui.setScreen(new DispenserScreen(this.R, var2.player.getInventory(), Component.literal("Blackjack FO recovery")));
      }
   }

   public void t(Minecraft var1) {
      if (var1.player == null || var1.level == null) {
         this.R = null;
         this.M = lh.w.r;
         this.d = this.q = this.w = this.H = this.t = (boolean)0;
         this.F = (boolean)0;
         this.T = null;
         Arrays.fill(this.l, -1);
      } else if (this.M == lh.w.I) {
         if (var1.player.containerMenu != this.R) {
            this.M = lh.w.r;
            this.R = null;
            this.w = (boolean)0;
         }
      } else if (this.M != lh.w.r) {
         if (var1.player.containerMenu != this.R) {
            this.X("container closed during transfer");
         } else {
            if (this.M != lh.w.G || this.i && s3.S().U() == this.k && (this.K == null || !(var1.player.distanceToSqr(Vec3.atCenterOf(this.K)) > 64.0))) {
               if (this.M != lh.w.G && System.nanoTime() > this.h) {
                  if (this.M == lh.w.A || this.M == lh.w.C && this.E++ == 0) {
                     te.Z("Blackjack FO", "Returning parked cards after " + this.M + " confirmation timeout");
                     if (this.M == lh.w.A && this.t && this.p.size() >= 45) {
                        for (int var7 = 0; var7 < 9; var7++) {
                           if (this.l[var7] >= 0 && this.p.get(var7).isEmpty() && this.p.get(l(this.l[var7])).isEmpty()) {
                              this.l[var7] = -1;
                           }
                        }
                     }

                     this.i();
                  } else {
                     this.X("server confirmation timed out in " + this.M);
                  }
               }
            } else {
               this.i();
            }
         }
      } else {
         if (this.R != null && var1.player.containerMenu != this.R) {
            this.R = null;
            this.w = (boolean)0;
            this.p.clear();
            this.m = 0L;
         }

         if (!this.i) {
            if (this.w && this.R != null && var1.gui.screen() == null) {
               var1.player.closeContainer();
            }

            this.d = (boolean)0;
         } else {
            long var2 = System.nanoTime();
            if (!this.q || var2 >= this.h) {
               this.q = (boolean)0;
               if (this.R != null && i(this.w, var1.gui.screen() != null, var2, this.h)) {
                  var1.player.closeContainer();
                  this.R = null;
                  this.p.clear();
                  this.d = (boolean)0;
                  this.m = 0L;
                  te.Z("Blackjack FO", "Dispenser opening timed out; reconnecting");
               }

               if (!this.w) {
                  if (var1.gui.screen() == null && var1.player.containerMenu == var1.player.inventoryMenu && var2 >= this.m) {
                     this.K = this.o();
                     if (this.K != null && !(var1.player.distanceToSqr(Vec3.atCenterOf(this.K)) > 64.0)) {
                        this.d = (boolean)1;
                        this.h = System.nanoTime() + 3000000000L;
                        this.m = System.nanoTime() + 3000000000L;
                        this.u = (boolean)1;

                        try {
                           var1.gameMode
                              .useItemOn(var1.player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(this.K), Direction.UP, this.K, false));
                        } finally {
                           this.u = (boolean)0;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private record m(String side, int playerTotal, int dealerTotal, boolean playerSoft, boolean dealerSoft, int dealtCards, int pending, int availableMask) {
   }

   private enum w {
      r,
      A,
      G,
      C,
      I;
   }
}

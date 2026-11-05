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

public final class kq {
   private static final int[] b = new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10};
   private static final long a = 15000000000L;
   private static final double z = 64.0;
   private static final kq j = new kq();
   private kq.p R = kq.p.n;
   private boolean k;
   private boolean h;
   private boolean Z;
   private boolean F;
   private boolean f;
   private boolean v;
   private boolean m;
   private boolean c;
   private boolean d;
   private final int[] D = new int[9];
   private final boolean[] X = new boolean[9];
   private final ItemStack[] I = new ItemStack[9];
   private final List<ItemStack> Y = new ArrayList<>();
   private DispenserMenu r;
   private BlockPos M;
   private BlockPos s;
   private long J;
   private long G;
   private int N;
   private int A;
   private int E = -1;
   private int Q = -1;
   private kq.v p;
   private boolean K;
   private int W = -1;
   private String C;
   private long o;
   private final int[] L = new int[11];

   public static kq I() {
      return j;
   }

   private kq() {
      Arrays.fill(this.D, -1);
      Arrays.fill(this.L, -1);
   }

   public String w(Minecraft var1) {
      if (var1 != null && var1.player != null) {
         int[] var2 = new int[11];
         Arrays.fill(var2, -1);
         HashSet var3 = new HashSet();

         for (int var4 = 0; var4 < b.length; var4++) {
            int var5 = b.length - 1 - var4;
            ItemStack var6 = var1.player.getInventory().getItem(var5);
            MapId var7 = var6 != null && var6.is(Items.FILLED_MAP) ? (MapId)var6.get(DataComponents.MAP_ID) : null;
            if (var7 == null) {
               return "Visible hotbar slot " + (var4 + 1) + " must hold a filled card map.";
            }

            if (!var3.add(var7.id())) {
               return "Each hotbar slot needs a different card map.";
            }

            var2[b[var4]] = var7.id();
         }

         System.arraycopy(var2, 0, this.L, 0, var2.length);
         StringBuilder var9 = new StringBuilder("Imported hotbar map mapping:");

         for (int var8 : b) {
            var9.append(' ').append(var8 == 1 ? "A" : var8).append("=map#").append(var2[var8]);
         }

         xa.q("Blackjack FO", var9.toString());
         return null;
      } else {
         return "Join a world before importing the hotbar.";
      }
   }

   public int[] q() {
      return (int[])this.L.clone();
   }

   public void E(int[] var1) {
      Arrays.fill(this.L, -1);
      if (var1 != null) {
         System.arraycopy(var1, 0, this.L, 0, Math.min(var1.length, this.L.length));
      }
   }

   public boolean f() {
      for (int var4 : b) {
         if (this.L[var4] < 0) {
            return false;
         }
      }

      return true;
   }

   public int T(int var1) {
      return var1 >= 1 && var1 <= 10 ? this.L[var1] : -1;
   }

   public int P(int var1) {
      return W(this.L, var1);
   }

   static int W(int[] var0, int var1) {
      if (var1 >= 0 && var0 != null) {
         for (int var5 : b) {
            if (var5 < var0.length && var0[var5] == var1) {
               return var5;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public void s(boolean var1) {
      if (var1 && !this.k) {
         this.G = 0L;
         this.K = (boolean)0;
      }

      this.k = var1;
   }

   public boolean Z() {
      return this.k;
   }

   public boolean k() {
      return this.R != kq.p.n;
   }

   public int D() {
      if (this.k && this.R != kq.p.D) {
         return this.R == kq.p.n ? this.m() : this.E;
      } else {
         return -1;
      }
   }

   public int Y(int var1, ItemStack var2) {
      if (this.R != kq.p.n && this.R != kq.p.D && var2 != null && !var2.isEmpty()) {
         for (int var3 = 0; var3 < this.D.length; var3++) {
            if (this.D[var3] == var1 && this.I[var3] != null && ItemStack.isSameItemSameComponents(var2, this.I[var3])) {
               return Math.min(var2.getCount(), this.I[var3].getCount());
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   public boolean j() {
      return this.R != kq.p.n && this.R != kq.p.D && !this.F;
   }

   public boolean A() {
      return this.F;
   }

   public boolean G() {
      return this.j();
   }

   public boolean l(Minecraft var1) {
      return var1.gui.screen() instanceof DispenserScreen var2 && var2.getMenu() == this.r;
   }

   public boolean L(Minecraft var1) {
      if (this.k
         && this.r != null
         && var1.player != null
         && var1.player.containerMenu == this.r
         && this.r.getCarried().isEmpty()
         && var1.gui.screen() == null
         && var1.hitResult instanceof BlockHitResult var2
         && var2.getBlockPos().equals(this.w())) {
         var1.gui.setScreen(new DispenserScreen(this.r, var1.player.getInventory(), Component.translatable("container.dropper")));
         return true;
      } else {
         return false;
      }
   }

   public int j(AbstractContainerMenu var1, int var2) {
      return this.F && var1 == this.r ? 32768 : var2;
   }

   public ItemStack R(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.R != kq.p.n && this.R != kq.p.D && var2.player != null) {
         for (int var6 : this.D) {
            if (var6 >= 0 && var1 == var2.player.getInventory().getItem(var6)) {
               return ItemStack.EMPTY;
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public ItemStack l(Minecraft var1, AbstractContainerMenu var2, Slot var3) {
      if (this.R != kq.p.n && this.R != kq.p.D && var2 == this.r && var1.player != null) {
         if (var3.container == var1.player.getInventory()) {
            for (int var7 : this.D) {
               if (var7 == var3.getContainerSlot()) {
                  return ItemStack.EMPTY;
               }
            }

            return null;
         } else {
            return var3.index >= 0 && var3.index < 9 && this.I[var3.index] != null ? this.I[var3.index] : null;
         }
      } else {
         return null;
      }
   }

   private BlockPos w() {
      return ep.H().n(ep.o.v);
   }

   public void N(BlockPos var1) {
      if (!this.F && this.k && this.R == kq.p.n && var1.equals(this.w())) {
         this.M = var1;
         this.Z = (boolean)1;
         this.h = (boolean)0;
         this.J = System.nanoTime() + 3000000000L;
      }
   }

   public void s(AbstractContainerMenu var1) {
      if (var1 instanceof DispenserMenu var2) {
         ep var3 = ep.H();
         if (this.Z || var3.U() == ep.o.v && var3.V() == var2.containerId) {
            this.r = var2;
            this.Y.clear();
            this.Z = this.h = this.f = (boolean)0;
         }
      }
   }

   public boolean T(AbstractContainerMenu var1) {
      return this.k && this.R != kq.p.D && var1 == this.r && this.r.getCarried().isEmpty();
   }

   public boolean r(MenuType<?> var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (!this.Z && this.h && var1 == MenuType.GENERIC_3x3 && var3.player != null) {
         this.r = new DispenserMenu(var2, var3.player.getInventory());
         var3.player.containerMenu = this.r;
         this.h = this.f = (boolean)0;
         return true;
      } else {
         return false;
      }
   }

   public void m(int var1, List<ItemStack> var2, ItemStack var3) {
      if (this.r != null && var1 == this.r.containerId && var2.size() == this.r.slots.size()) {
         this.Y.clear();

         for (ItemStack var5 : var2) {
            this.Y.add(var5.copy());
         }

         this.f = (boolean)1;
         this.B(var3);
      }
   }

   public void t(int var1, int var2, ItemStack var3) {
      if (this.r != null && !this.Y.isEmpty() && this.R != kq.p.D) {
         if (var1 == this.r.containerId && var2 >= 0 && var2 < this.Y.size()) {
            this.Y.set(var2, var3.copy());
         } else {
            if (var1 != 0 || F(var2) < 0 || var2 >= this.Y.size()) {
               return;
            }

            this.Y.set(var2, var3.copy());
         }

         this.B(this.r.getCarried());
      }
   }

   public void H(int var1, ItemStack var2) {
      if (this.r != null && !this.Y.isEmpty() && this.R != kq.p.D && var1 >= 0 && var1 < 36) {
         this.Y.set(G(var1), var2.copy());
         this.B(this.r.getCarried());
      }
   }

   private static int G(int var0) {
      return var0 < 9 ? 36 + var0 : var0;
   }

   static int Y(int var0) {
      for (int var1 = 0; var1 < b.length; var1++) {
         if (b[var1] == var0) {
            return var1;
         }
      }

      return -1;
   }

   static boolean Z(int var0, boolean var1, int var2, int var3) {
      return !var1 && var0 + var2 > 21 && var0 + var3 > 21;
   }

   static int F(int var0) {
      return var0 >= 9 && var0 < 45 ? var0 : -1;
   }

   private boolean W(boolean var1) {
      if (this.Y.size() < 45) {
         return false;
      }

      for (int var2 = 0; var2 < 9; var2++) {
         if (this.D[var2] >= 0) {
            if (!ItemStack.matches(this.Y.get(var2), var1 ? ItemStack.EMPTY : this.I[var2])) {
               return false;
            }

            ItemStack var3 = this.Y.get(G(this.D[var2]));
            if (var1 ? !ItemStack.matches(var3, this.I[var2]) : !var3.isEmpty()) {
               return false;
            }
         }
      }

      return true;
   }

   private boolean L() {
      if (this.Y.size() < 9) {
         return false;
      }

      for (int var1 = 0; var1 < 9; var1++) {
         if (this.D[var1] < 0 && this.I[var1] != null && !this.I[var1].isEmpty() && !ItemStack.matches(this.Y.get(var1), this.I[var1])) {
            return true;
         }
      }

      return false;
   }

   private void B(ItemStack var1) {
      if (var1.isEmpty()) {
         if (this.R == kq.p.O && this.W(true)) {
            this.R = kq.p.L;
            if (!this.L() && !this.c && this.k && mj.D().v() == this.d) {
               xa.q("Blackjack FO", (this.d ? "Dealer" : "Player") + " draw ready");
            } else {
               this.Q();
            }
         } else if (this.R != kq.p.L || !this.L() && !this.c && this.W(true)) {
            if (this.R == kq.p.Y) {
               this.e(false);
               if (this.R == kq.p.Y && this.W(false)) {
                  this.R = kq.p.n;
                  this.c = (boolean)0;
                  Arrays.fill(this.D, -1);
                  xa.q("Blackjack FO", "Other cards returned after dispenser draw");
               }
            }
         } else {
            this.Q();
         }
      }
   }

   static int z(boolean var0, boolean var1, boolean var2, boolean var3) {
      if (var0 && var3) {
         return 0;
      } else {
         return var1 && var2 ? 1 : -1;
      }
   }

   static boolean E(int var0, boolean var1) {
      return var0 == 1 && !var1;
   }

   static boolean v(boolean var0, boolean var1, long var2, long var4) {
      return !var0 && !var1 && var2 >= var4;
   }

   private int i(int var1) {
      if (this.Y.size() < 45) {
         return -1;
      }

      ItemStack var2 = this.Y.get(var1);
      ItemStack var3 = this.Y.get(G(this.D[var1]));
      return z(ItemStack.matches(var2, this.I[var1]), var2.isEmpty(), ItemStack.matches(var3, this.I[var1]), var3.isEmpty());
   }

   public void I(BlockPos var1, BlockState var2) {
      if (this.M != null) {
         if (var2.getBlock() instanceof LeverBlock && var1.distSqr(this.M) <= 9.0) {
            boolean var5 = (Boolean)var2.getValue(LeverBlock.POWERED);
            int var6 = !var5 || var1.equals(this.s) && this.m ? 0 : 1;
            this.s = var1.immutable();
            this.m = var5;
            if (var6 != 0) {
               this.G("lever");
            }
         } else if (this.M.equals(var1)) {
            int var3 = var2.getBlock() instanceof DispenserBlock && var2.getValue(DispenserBlock.TRIGGERED) ? 1 : 0;
            int var4 = var3 != 0 && !this.v ? 1 : 0;
            this.v = (boolean)var3;
            if (var4 != 0) {
               this.G("dispenser");
            }
         }
      }
   }

   private void G(String var1) {
      if (this.k && System.nanoTime() >= this.o) {
         int var2 = this.R == kq.p.n ? this.m() : -1;
         if (var2 > 0 && this.k(var2)) {
            this.K = (boolean)1;
            mj var4 = mj.D();
            this.W = var4.O();
            this.C = var4.v();
            xa.q(
               "Blackjack FO",
               "Started rigging " + var2 + " for " + (var4.v() ? "dealer" : "player") + " on " + var1 + ": player=" + var4.B() + " dealer=" + var4.l()
            );
         } else if (this.R == kq.p.n && (mj.D().v() || mj.D().B() >= 12)) {
            mj var3 = mj.D();
            xa.q(
               "Blackjack FO",
               (var3.v() ? "Dealer" : "Player")
                  + " draw unforced on "
                  + var1
                  + ": transfer="
                  + this.R
                  + " connected="
                  + this.f
                  + " slots="
                  + this.Y.size()
                  + " cards="
                  + var3.O()
                  + " pending="
                  + var3.J()
                  + " player="
                  + var3.B()
                  + " dealer="
                  + var3.l()
            );
         }
      }
   }

   private int m() {
      if (this.f && this.Y.size() >= 45) {
         boolean[] var1 = new boolean[11];
         int var2 = 0;

         for (int var3 = 0; var3 < b.length; var3++) {
            ItemStack var4 = this.Y.get(var3);
            MapId var5 = var4.is(Items.FILLED_MAP) ? (MapId)var4.get(DataComponents.MAP_ID) : null;
            int var6 = this.f() ? (var5 == null ? -1 : this.P(var5.id())) : b[var3];
            if (var6 >= 1 && var6 <= 10) {
               var1[var6] = (boolean)(var5 != null ? 1 : 0);
               if (var1[var6]) {
                  var2 |= 1 << var6;
               }
            }
         }

         mj var7 = mj.D();
         if (this.K && (var7.O() != this.W || !var7.v().equals(this.C))) {
            this.K = (boolean)0;
         }

         kq.v var8 = new kq.v(var7.v(), var7.B(), var7.l(), var7.K(), var7.A(), var7.O(), var7.J(), var2);
         if (!var8.equals(this.p)) {
            this.p = var8;
            this.Q = var7.v() ? var7.T(var1) : var7.D(var1);
         }

         return this.K ? -1 : this.Q;
      } else {
         this.p = null;
         this.Q = -1;
         return -1;
      }
   }

   public void r(BlockPos var1) {
      if (this.M != null && this.M.equals(var1) && (this.R == kq.p.O || this.R == kq.p.L)) {
         this.c = (boolean)1;
         if (this.R == kq.p.L) {
            this.Q();
         }
      }
   }

   public void J(BlockPos var1) {
      if (this.k && this.M != null && var1.distSqr(this.M) <= 9.0) {
         this.o = System.nanoTime() + 2000000000L;
      }
   }

   static int[] C(int[] var0, boolean[] var1, boolean[] var2, int var3, int var4) {
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

   private boolean k(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.f && this.r != null && var2.player != null && var2.player.containerMenu == this.r && this.r.getCarried().isEmpty() && this.Y.size() >= 45) {
         int var3 = this.I(var1);
         MapId var4 = var3 < 0 ? null : (MapId)this.Y.get(var3).get(DataComponents.MAP_ID);
         int var5 = var4 == null ? -1 : var4.id();
         if (var5 < 0) {
            xa.q("Blackjack FO", "Skipped draw: no card template for rank " + var1);
            return false;
         }

         int[] var6 = new int[9];
         boolean[] var7 = new boolean[9];
         boolean[] var8 = new boolean[36];
         mj var9 = mj.D();
         this.d = var9.v();
         int var10 = var9.l();
         boolean var11 = var9.A();

         for (int var12 = 0; var12 < 9; var12++) {
            ItemStack var13 = this.Y.get(var12);
            MapId var14 = var13.is(Items.FILLED_MAP) ? (MapId)var13.get(DataComponents.MAP_ID) : null;
            var6[var12] = var14 == null ? -1 : var14.id();
            int var15 = this.f() ? (var14 == null ? -1 : this.P(var14.id())) : b[var12];
            var7[var12] = (boolean)(var13.isEmpty()
                  || var12 != var3 && var14 != null && var15 >= 1 && (this.d ? Z(var10, var11, var1, var15) : var9.l(var1, var15))
               ? 0
               : 1);
         }

         for (int var21 = 0; var21 < 36; var21++) {
            var8[var21] = (boolean)(this.Y.get(G(var21)).isEmpty() && var2.player.getInventory().getItem(var21).isEmpty() ? 0 : 1);
         }

         int[] var22 = C(var6, var7, var8, var2.player.getInventory().getSelectedSlot(), var5);
         if (var22 == null) {
            xa.q("Blackjack FO", "Skipped draw: card missing/duplicated or not enough empty inventory slots");
            return false;
         }

         Arrays.fill(this.D, -1);
         Arrays.fill(this.X, false);

         for (int var23 = 0; var23 < 9; var23++) {
            this.I[var23] = this.Y.get(var23).copy();
            this.D[var23] = var22[var23] >= 0 ? var22[var23] : -1;
         }

         this.R = kq.p.O;
         this.E = var1;
         this.c = (boolean)0;
         this.N = 0;
         this.A = 0;
         this.J = System.nanoTime() + 15000000000L;
         this.F = (boolean)1;

         try {
            for (int var24 = 0; var24 < 9; var24++) {
               if (this.D[var24] >= 0) {
                  this.W(var24, this.D[var24], false, var2);
               }
            }
         } catch (RuntimeException var19) {
            this.E("removal click failed");
            return false;
         } finally {
            this.F = (boolean)0;
         }

         return true;
      } else {
         xa.q("Blackjack FO", "Skipped draw: dispenser not ready");
         return false;
      }
   }

   private int I(int var1) {
      if (!this.f()) {
         return Y(var1);
      }

      int var2 = this.T(var1);
      if (var2 >= 0 && this.Y.size() >= 9) {
         int var3 = -1;

         for (int var4 = 0; var4 < 9; var4++) {
            MapId var5 = this.Y.get(var4).is(Items.FILLED_MAP) ? (MapId)this.Y.get(var4).get(DataComponents.MAP_ID) : null;
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

   private void Q() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.R != kq.p.D) {
         if (var1.player != null && var1.player.containerMenu == this.r && this.r.getCarried().isEmpty() && this.Y.size() >= 45) {
            this.R = kq.p.Y;
            this.J = System.nanoTime() + 15000000000L;
            Arrays.fill(this.X, false);
            this.e(true);
            this.B(this.r.getCarried());
         } else {
            this.E("return state changed");
         }
      }
   }

   private void e(boolean var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu == this.r && this.Y.size() >= 45) {
         StringBuilder var3 = new StringBuilder();
         this.F = (boolean)1;

         try {
            int var4 = 0;

            for (int var5 = 0; var5 < 9; var5++) {
               if (this.D[var5] >= 0) {
                  int var6 = this.i(var5);
                  if (var6 == 0) {
                     var4++;
                  } else {
                     if (var4 > this.A) {
                        this.A = var4;
                        this.N = 0;
                        this.J = System.nanoTime() + 15000000000L;
                     }

                     if (var6 < 0) {
                        if (var1) {
                           var3.append(' ').append(var5 + 1).append('[').append(this.Y.get(var5)).append(" / ").append(this.Y.get(G(this.D[var5]))).append(']');
                        }
                     } else if (E(var6, this.X[var5])) {
                        this.W(var5, this.D[var5], true, var2);
                        this.X[var5] = (boolean)1;
                     }
                  }
               }
            }
         } catch (RuntimeException var10) {
            this.E("return click failed");
         } finally {
            this.F = (boolean)0;
         }

         if (!var3.isEmpty()) {
            xa.q("Blackjack FO", "Waiting for changed slots:" + var3);
         }
      }
   }

   private void W(int var1, int var2, boolean var3, Minecraft var4) {
      if (var2 < 9) {
         var4.gameMode.handleContainerInput(this.r.containerId, var1, var2, ContainerInput.SWAP, var4.player);
      } else {
         int var5 = var3 ? G(var2) : var1;
         int var6 = var3 ? var1 : G(var2);
         var4.gameMode.handleContainerInput(this.r.containerId, var5, 0, ContainerInput.PICKUP, var4.player);
         var4.gameMode.handleContainerInput(this.r.containerId, var6, 0, ContainerInput.PICKUP, var4.player);
      }
   }

   private void E(String var1) {
      this.R = kq.p.D;
      this.k = (boolean)0;
      xa.q("Blackjack FO", "Stopped: " + var1 + "; inspect real slots, no automatic replay");
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu == this.r) {
         var2.gui.setScreen(new DispenserScreen(this.r, var2.player.getInventory(), Component.literal("Blackjack FO recovery")));
      }
   }

   public void Z(Minecraft var1) {
      if (var1.player == null || var1.level == null) {
         this.r = null;
         this.R = kq.p.n;
         this.h = this.Z = this.f = this.v = this.c = (boolean)0;
         this.m = (boolean)0;
         this.s = null;
         Arrays.fill(this.D, -1);
      } else if (this.R == kq.p.D) {
         if (var1.player.containerMenu != this.r) {
            this.R = kq.p.n;
            this.r = null;
            this.f = (boolean)0;
         }
      } else if (this.R != kq.p.n) {
         if (var1.player.containerMenu != this.r) {
            this.E("container closed during transfer");
         } else {
            if (this.R != kq.p.L || this.k && mj.D().v() == this.d && (this.M == null || !(var1.player.distanceToSqr(Vec3.atCenterOf(this.M)) > 64.0))) {
               if (this.R != kq.p.L && System.nanoTime() > this.J) {
                  if (this.R == kq.p.O || this.R == kq.p.Y && this.N++ == 0) {
                     xa.q("Blackjack FO", "Returning parked cards after " + this.R + " confirmation timeout");
                     if (this.R == kq.p.O && this.c && this.Y.size() >= 45) {
                        for (int var7 = 0; var7 < 9; var7++) {
                           if (this.D[var7] >= 0 && this.Y.get(var7).isEmpty() && this.Y.get(G(this.D[var7])).isEmpty()) {
                              this.D[var7] = -1;
                           }
                        }
                     }

                     this.Q();
                  } else {
                     this.E("server confirmation timed out in " + this.R);
                  }
               }
            } else {
               this.Q();
            }
         }
      } else {
         if (this.r != null && var1.player.containerMenu != this.r) {
            this.r = null;
            this.f = (boolean)0;
            this.Y.clear();
            this.G = 0L;
         }

         if (!this.k) {
            if (this.f && this.r != null && var1.gui.screen() == null) {
               var1.player.closeContainer();
            }

            this.h = (boolean)0;
         } else {
            long var2 = System.nanoTime();
            if (!this.Z || var2 >= this.J) {
               this.Z = (boolean)0;
               if (this.r != null && v(this.f, var1.gui.screen() != null, var2, this.J)) {
                  var1.player.closeContainer();
                  this.r = null;
                  this.Y.clear();
                  this.h = (boolean)0;
                  this.G = 0L;
                  xa.q("Blackjack FO", "Dispenser opening timed out; reconnecting");
               }

               if (!this.f) {
                  if (var1.gui.screen() == null && var1.player.containerMenu == var1.player.inventoryMenu && var2 >= this.G) {
                     this.M = this.w();
                     if (this.M != null && !(var1.player.distanceToSqr(Vec3.atCenterOf(this.M)) > 64.0)) {
                        this.h = (boolean)1;
                        this.J = System.nanoTime() + 3000000000L;
                        this.G = System.nanoTime() + 3000000000L;
                        this.F = (boolean)1;

                        try {
                           var1.gameMode
                              .useItemOn(var1.player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(this.M), Direction.UP, this.M, false));
                        } finally {
                           this.F = (boolean)0;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private enum p {
      n,
      O,
      L,
      Y,
      D;
   }

   private record v(String side, int playerTotal, int dealerTotal, boolean playerSoft, boolean dealerSoft, int dealtCards, int pending, int availableMask) {
   }
}

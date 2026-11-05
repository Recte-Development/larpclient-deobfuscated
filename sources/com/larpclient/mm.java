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

public final class mm {
   private static final int[] q = new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10};
   private static final long R = 15000000000L;
   private static final double j = 64.0;
   private static final mm z = new mm();
   private mm.b y = mm.b.L;
   private boolean A;
   private boolean w;
   private boolean p;
   private boolean D;
   private boolean d;
   private boolean G;
   private boolean x;
   private boolean f;
   private boolean i;
   private final int[] U = new int[9];
   private final boolean[] B = new boolean[9];
   private final ItemStack[] Y = new ItemStack[9];
   private final List<ItemStack> L = new ArrayList<>();
   private DispenserMenu W;
   private BlockPos n;
   private BlockPos X;
   private long u;
   private long r;
   private int K;
   private int C;
   private int k = -1;
   private int g = -1;
   private mm.r H;
   private boolean c;
   private int F = -1;
   private String o;
   private long V;
   private final int[] O = new int[11];

   public static mm W() {
      return z;
   }

   private mm() {
      Arrays.fill(this.U, -1);
      Arrays.fill(this.O, -1);
   }

   public String t(Minecraft var1) {
      if (var1 != null && var1.player != null) {
         int[] var2 = new int[11];
         Arrays.fill(var2, -1);
         HashSet var3 = new HashSet();

         for (int var4 = 0; var4 < q.length; var4++) {
            int var5 = q.length - 1 - var4;
            ItemStack var6 = var1.player.getInventory().getItem(var5);
            MapId var7 = var6 != null && var6.is(Items.FILLED_MAP) ? (MapId)var6.get(DataComponents.MAP_ID) : null;
            if (var7 == null) {
               return "Visible hotbar slot " + (var4 + 1) + " must hold a filled card map.";
            }

            if (!var3.add(var7.id())) {
               return "Each hotbar slot needs a different card map.";
            }

            var2[q[var4]] = var7.id();
         }

         System.arraycopy(var2, 0, this.O, 0, var2.length);
         StringBuilder var9 = new StringBuilder("Imported hotbar map mapping:");

         for (int var8 : q) {
            var9.append(' ').append(var8 == 1 ? "A" : var8).append("=map#").append(var2[var8]);
         }

         hh.K("Blackjack FO", var9.toString());
         return null;
      } else {
         return "Join a world before importing the hotbar.";
      }
   }

   public int[] S() {
      return (int[])this.O.clone();
   }

   public void R(int[] var1) {
      Arrays.fill(this.O, -1);
      if (var1 != null) {
         System.arraycopy(var1, 0, this.O, 0, Math.min(var1.length, this.O.length));
      }
   }

   public boolean S() {
      for (int var4 : q) {
         if (this.O[var4] < 0) {
            return false;
         }
      }

      return true;
   }

   public int c(int var1) {
      return var1 >= 1 && var1 <= 10 ? this.O[var1] : -1;
   }

   public int d(int var1) {
      return a(this.O, var1);
   }

   static int a(int[] var0, int var1) {
      if (var1 >= 0 && var0 != null) {
         for (int var5 : q) {
            if (var5 < var0.length && var0[var5] == var1) {
               return var5;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public void G(boolean var1) {
      if (var1 && !this.A) {
         this.r = 0L;
         this.c = (boolean)0;
      }

      this.A = var1;
   }

   public boolean I() {
      return this.A;
   }

   public boolean x() {
      return this.y != mm.b.L;
   }

   public int M() {
      if (this.A && this.y != mm.b.Z) {
         return this.y == mm.b.L ? this.l() : this.k;
      } else {
         return -1;
      }
   }

   public int v(int var1, ItemStack var2) {
      if (this.y != mm.b.L && this.y != mm.b.Z && var2 != null && !var2.isEmpty()) {
         for (int var3 = 0; var3 < this.U.length; var3++) {
            if (this.U[var3] == var1 && this.Y[var3] != null && ItemStack.isSameItemSameComponents(var2, this.Y[var3])) {
               return Math.min(var2.getCount(), this.Y[var3].getCount());
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   public boolean h() {
      return this.y != mm.b.L && this.y != mm.b.Z && !this.D;
   }

   public boolean E() {
      return this.D;
   }

   public boolean K() {
      return this.h();
   }

   public boolean T(Minecraft var1) {
      return var1.gui.screen() instanceof DispenserScreen var2 && var2.getMenu() == this.W;
   }

   public boolean p(Minecraft var1) {
      if (this.A
         && this.W != null
         && var1.player != null
         && var1.player.containerMenu == this.W
         && this.W.getCarried().isEmpty()
         && var1.gui.screen() == null
         && var1.hitResult instanceof BlockHitResult var2
         && var2.getBlockPos().equals(this.o())) {
         var1.gui.setScreen(new DispenserScreen(this.W, var1.player.getInventory(), Component.translatable("container.dropper")));
         return true;
      } else {
         return false;
      }
   }

   public int w(AbstractContainerMenu var1, int var2) {
      return this.D && var1 == this.W ? 32768 : var2;
   }

   public ItemStack u(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.y != mm.b.L && this.y != mm.b.Z && var2.player != null) {
         for (int var6 : this.U) {
            if (var6 >= 0 && var1 == var2.player.getInventory().getItem(var6)) {
               return ItemStack.EMPTY;
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public ItemStack t(Minecraft var1, AbstractContainerMenu var2, Slot var3) {
      if (this.y != mm.b.L && this.y != mm.b.Z && var2 == this.W && var1.player != null) {
         if (var3.container == var1.player.getInventory()) {
            for (int var7 : this.U) {
               if (var7 == var3.getContainerSlot()) {
                  return ItemStack.EMPTY;
               }
            }

            return null;
         } else {
            return var3.index >= 0 && var3.index < 9 && this.Y[var3.index] != null ? this.Y[var3.index] : null;
         }
      } else {
         return null;
      }
   }

   private BlockPos o() {
      return f9.W().R(f9.b.P);
   }

   public void l(BlockPos var1) {
      if (!this.D && this.A && this.y == mm.b.L && var1.equals(this.o())) {
         this.n = var1;
         this.p = (boolean)1;
         this.w = (boolean)0;
         this.u = System.nanoTime() + 3000000000L;
      }
   }

   public void w(AbstractContainerMenu var1) {
      if (var1 instanceof DispenserMenu var2) {
         f9 var3 = f9.W();
         if (this.p || var3.o() == f9.b.P && var3.i() == var2.containerId) {
            this.W = var2;
            this.L.clear();
            this.p = this.w = this.d = (boolean)0;
         }
      }
   }

   public boolean D(AbstractContainerMenu var1) {
      return this.A && this.y != mm.b.Z && var1 == this.W && this.W.getCarried().isEmpty();
   }

   public boolean y(MenuType<?> var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (!this.p && this.w && var1 == MenuType.GENERIC_3x3 && var3.player != null) {
         this.W = new DispenserMenu(var2, var3.player.getInventory());
         var3.player.containerMenu = this.W;
         this.w = this.d = (boolean)0;
         return true;
      } else {
         return false;
      }
   }

   public void h(int var1, List<ItemStack> var2, ItemStack var3) {
      if (this.W != null && var1 == this.W.containerId && var2.size() == this.W.slots.size()) {
         this.L.clear();

         for (ItemStack var5 : var2) {
            this.L.add(var5.copy());
         }

         this.d = (boolean)1;
         this.b(var3);
      }
   }

   public void v(int var1, int var2, ItemStack var3) {
      if (this.W != null && !this.L.isEmpty() && this.y != mm.b.Z) {
         if (var1 == this.W.containerId && var2 >= 0 && var2 < this.L.size()) {
            this.L.set(var2, var3.copy());
         } else {
            if (var1 != 0 || S(var2) < 0 || var2 >= this.L.size()) {
               return;
            }

            this.L.set(var2, var3.copy());
         }

         this.b(this.W.getCarried());
      }
   }

   public void u(int var1, ItemStack var2) {
      if (this.W != null && !this.L.isEmpty() && this.y != mm.b.Z && var1 >= 0 && var1 < 36) {
         this.L.set(Q(var1), var2.copy());
         this.b(this.W.getCarried());
      }
   }

   private static int Q(int var0) {
      return var0 < 9 ? 36 + var0 : var0;
   }

   static int g(int var0) {
      for (int var1 = 0; var1 < q.length; var1++) {
         if (q[var1] == var0) {
            return var1;
         }
      }

      return -1;
   }

   static boolean D(int var0, boolean var1, int var2, int var3) {
      return !var1 && var0 + var2 > 21 && var0 + var3 > 21;
   }

   static int S(int var0) {
      return var0 >= 9 && var0 < 45 ? var0 : -1;
   }

   private boolean k(boolean var1) {
      if (this.L.size() < 45) {
         return false;
      }

      for (int var2 = 0; var2 < 9; var2++) {
         if (this.U[var2] >= 0) {
            if (!ItemStack.matches(this.L.get(var2), var1 ? ItemStack.EMPTY : this.Y[var2])) {
               return false;
            }

            ItemStack var3 = this.L.get(Q(this.U[var2]));
            if (var1 ? !ItemStack.matches(var3, this.Y[var2]) : !var3.isEmpty()) {
               return false;
            }
         }
      }

      return true;
   }

   private boolean M() {
      if (this.L.size() < 9) {
         return false;
      }

      for (int var1 = 0; var1 < 9; var1++) {
         if (this.U[var1] < 0 && this.Y[var1] != null && !this.Y[var1].isEmpty() && !ItemStack.matches(this.L.get(var1), this.Y[var1])) {
            return true;
         }
      }

      return false;
   }

   private void b(ItemStack var1) {
      if (var1.isEmpty()) {
         if (this.y == mm.b.o && this.k(true)) {
            this.y = mm.b.f;
            if (!this.M() && !this.f && this.A && mz.F().A() == this.i) {
               hh.K("Blackjack FO", (this.i ? "Dealer" : "Player") + " draw ready");
            } else {
               this.S();
            }
         } else if (this.y != mm.b.f || !this.M() && !this.f && this.k(true)) {
            if (this.y == mm.b.R) {
               this.f(false);
               if (this.y == mm.b.R && this.k(false)) {
                  this.y = mm.b.L;
                  this.f = (boolean)0;
                  Arrays.fill(this.U, -1);
                  hh.K("Blackjack FO", "Other cards returned after dispenser draw");
               }
            }
         } else {
            this.S();
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

   static boolean P(int var0, boolean var1) {
      return var0 == 1 && !var1;
   }

   static boolean N(boolean var0, boolean var1, long var2, long var4) {
      return !var0 && !var1 && var2 >= var4;
   }

   private int k(int var1) {
      if (this.L.size() < 45) {
         return -1;
      }

      ItemStack var2 = this.L.get(var1);
      ItemStack var3 = this.L.get(Q(this.U[var1]));
      return h(ItemStack.matches(var2, this.Y[var1]), var2.isEmpty(), ItemStack.matches(var3, this.Y[var1]), var3.isEmpty());
   }

   public void q(BlockPos var1, BlockState var2) {
      if (this.n != null) {
         if (var2.getBlock() instanceof LeverBlock && var1.distSqr(this.n) <= 9.0) {
            boolean var5 = (Boolean)var2.getValue(LeverBlock.POWERED);
            int var6 = !var5 || var1.equals(this.X) && this.x ? 0 : 1;
            this.X = var1.immutable();
            this.x = var5;
            if (var6 != 0) {
               this.o("lever");
            }
         } else if (this.n.equals(var1)) {
            int var3 = var2.getBlock() instanceof DispenserBlock && var2.getValue(DispenserBlock.TRIGGERED) ? 1 : 0;
            int var4 = var3 != 0 && !this.G ? 1 : 0;
            this.G = (boolean)var3;
            if (var4 != 0) {
               this.o("dispenser");
            }
         }
      }
   }

   private void o(String var1) {
      if (this.A && System.nanoTime() >= this.V) {
         int var2 = this.y == mm.b.L ? this.l() : -1;
         if (var2 > 0 && this.U(var2)) {
            this.c = (boolean)1;
            mz var4 = mz.F();
            this.F = var4.G();
            this.o = var4.m();
            hh.K(
               "Blackjack FO",
               "Started rigging " + var2 + " for " + (var4.A() ? "dealer" : "player") + " on " + var1 + ": player=" + var4.Y() + " dealer=" + var4.h()
            );
         } else if (this.y == mm.b.L && (mz.F().A() || mz.F().Y() >= 12)) {
            mz var3 = mz.F();
            hh.K(
               "Blackjack FO",
               (var3.A() ? "Dealer" : "Player")
                  + " draw unforced on "
                  + var1
                  + ": transfer="
                  + this.y
                  + " connected="
                  + this.d
                  + " slots="
                  + this.L.size()
                  + " cards="
                  + var3.G()
                  + " pending="
                  + var3.K()
                  + " player="
                  + var3.Y()
                  + " dealer="
                  + var3.h()
            );
         }
      }
   }

   private int l() {
      if (this.d && this.L.size() >= 45) {
         boolean[] var1 = new boolean[11];
         int var2 = 0;

         for (int var3 = 0; var3 < q.length; var3++) {
            ItemStack var4 = this.L.get(var3);
            MapId var5 = var4.is(Items.FILLED_MAP) ? (MapId)var4.get(DataComponents.MAP_ID) : null;
            int var6 = this.S() ? (var5 == null ? -1 : this.d(var5.id())) : q[var3];
            if (var6 >= 1 && var6 <= 10) {
               var1[var6] = (boolean)(var5 != null ? 1 : 0);
               if (var1[var6]) {
                  var2 |= 1 << var6;
               }
            }
         }

         mz var7 = mz.F();
         if (this.c && (var7.G() != this.F || !var7.m().equals(this.o))) {
            this.c = (boolean)0;
         }

         mm.r var8 = new mm.r(var7.m(), var7.Y(), var7.h(), var7.x(), var7.a(), var7.G(), var7.K(), var2);
         if (!var8.equals(this.H)) {
            this.H = var8;
            this.g = var7.A() ? var7.t(var1) : var7.r(var1);
         }

         return this.c ? -1 : this.g;
      } else {
         this.H = null;
         this.g = -1;
         return -1;
      }
   }

   public void x(BlockPos var1) {
      if (this.n != null && this.n.equals(var1) && (this.y == mm.b.o || this.y == mm.b.f)) {
         this.f = (boolean)1;
         if (this.y == mm.b.f) {
            this.S();
         }
      }
   }

   public void Q(BlockPos var1) {
      if (this.A && this.n != null && var1.distSqr(this.n) <= 9.0) {
         this.V = System.nanoTime() + 2000000000L;
      }
   }

   static int[] O(int[] var0, boolean[] var1, boolean[] var2, int var3, int var4) {
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

   private boolean U(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.d && this.W != null && var2.player != null && var2.player.containerMenu == this.W && this.W.getCarried().isEmpty() && this.L.size() >= 45) {
         int var3 = this.E(var1);
         MapId var4 = var3 < 0 ? null : (MapId)this.L.get(var3).get(DataComponents.MAP_ID);
         int var5 = var4 == null ? -1 : var4.id();
         if (var5 < 0) {
            hh.K("Blackjack FO", "Skipped draw: no card template for rank " + var1);
            return false;
         }

         int[] var6 = new int[9];
         boolean[] var7 = new boolean[9];
         boolean[] var8 = new boolean[36];
         mz var9 = mz.F();
         this.i = var9.A();
         int var10 = var9.h();
         boolean var11 = var9.a();

         for (int var12 = 0; var12 < 9; var12++) {
            ItemStack var13 = this.L.get(var12);
            MapId var14 = var13.is(Items.FILLED_MAP) ? (MapId)var13.get(DataComponents.MAP_ID) : null;
            var6[var12] = var14 == null ? -1 : var14.id();
            int var15 = this.S() ? (var14 == null ? -1 : this.d(var14.id())) : q[var12];
            var7[var12] = (boolean)(var13.isEmpty()
                  || var12 != var3 && var14 != null && var15 >= 1 && (this.i ? D(var10, var11, var1, var15) : var9.O(var1, var15))
               ? 0
               : 1);
         }

         for (int var21 = 0; var21 < 36; var21++) {
            var8[var21] = (boolean)(this.L.get(Q(var21)).isEmpty() && var2.player.getInventory().getItem(var21).isEmpty() ? 0 : 1);
         }

         int[] var22 = O(var6, var7, var8, var2.player.getInventory().getSelectedSlot(), var5);
         if (var22 == null) {
            hh.K("Blackjack FO", "Skipped draw: card missing/duplicated or not enough empty inventory slots");
            return false;
         }

         Arrays.fill(this.U, -1);
         Arrays.fill(this.B, false);

         for (int var23 = 0; var23 < 9; var23++) {
            this.Y[var23] = this.L.get(var23).copy();
            this.U[var23] = var22[var23] >= 0 ? var22[var23] : -1;
         }

         this.y = mm.b.o;
         this.k = var1;
         this.f = (boolean)0;
         this.K = 0;
         this.C = 0;
         this.u = System.nanoTime() + 15000000000L;
         this.D = (boolean)1;

         try {
            for (int var24 = 0; var24 < 9; var24++) {
               if (this.U[var24] >= 0) {
                  this.L(var24, this.U[var24], false, var2);
               }
            }
         } catch (RuntimeException var19) {
            this.q("removal click failed");
            return false;
         } finally {
            this.D = (boolean)0;
         }

         return true;
      } else {
         hh.K("Blackjack FO", "Skipped draw: dispenser not ready");
         return false;
      }
   }

   private int E(int var1) {
      if (!this.S()) {
         return g(var1);
      }

      int var2 = this.c(var1);
      if (var2 >= 0 && this.L.size() >= 9) {
         int var3 = -1;

         for (int var4 = 0; var4 < 9; var4++) {
            MapId var5 = this.L.get(var4).is(Items.FILLED_MAP) ? (MapId)this.L.get(var4).get(DataComponents.MAP_ID) : null;
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

   private void S() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.y != mm.b.Z) {
         if (var1.player != null && var1.player.containerMenu == this.W && this.W.getCarried().isEmpty() && this.L.size() >= 45) {
            this.y = mm.b.R;
            this.u = System.nanoTime() + 15000000000L;
            Arrays.fill(this.B, false);
            this.f(true);
            this.b(this.W.getCarried());
         } else {
            this.q("return state changed");
         }
      }
   }

   private void f(boolean var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu == this.W && this.L.size() >= 45) {
         StringBuilder var3 = new StringBuilder();
         this.D = (boolean)1;

         try {
            int var4 = 0;

            for (int var5 = 0; var5 < 9; var5++) {
               if (this.U[var5] >= 0) {
                  int var6 = this.k(var5);
                  if (var6 == 0) {
                     var4++;
                  } else {
                     if (var4 > this.C) {
                        this.C = var4;
                        this.K = 0;
                        this.u = System.nanoTime() + 15000000000L;
                     }

                     if (var6 < 0) {
                        if (var1) {
                           var3.append(' ').append(var5 + 1).append('[').append(this.L.get(var5)).append(" / ").append(this.L.get(Q(this.U[var5]))).append(']');
                        }
                     } else if (P(var6, this.B[var5])) {
                        this.L(var5, this.U[var5], true, var2);
                        this.B[var5] = (boolean)1;
                     }
                  }
               }
            }
         } catch (RuntimeException var10) {
            this.q("return click failed");
         } finally {
            this.D = (boolean)0;
         }

         if (!var3.isEmpty()) {
            hh.K("Blackjack FO", "Waiting for changed slots:" + var3);
         }
      }
   }

   private void L(int var1, int var2, boolean var3, Minecraft var4) {
      if (var2 < 9) {
         var4.gameMode.handleContainerInput(this.W.containerId, var1, var2, ContainerInput.SWAP, var4.player);
      } else {
         int var5 = var3 ? Q(var2) : var1;
         int var6 = var3 ? var1 : Q(var2);
         var4.gameMode.handleContainerInput(this.W.containerId, var5, 0, ContainerInput.PICKUP, var4.player);
         var4.gameMode.handleContainerInput(this.W.containerId, var6, 0, ContainerInput.PICKUP, var4.player);
      }
   }

   private void q(String var1) {
      this.y = mm.b.Z;
      this.A = (boolean)0;
      hh.K("Blackjack FO", "Stopped: " + var1 + "; inspect real slots, no automatic replay");
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu == this.W) {
         var2.gui.setScreen(new DispenserScreen(this.W, var2.player.getInventory(), Component.literal("Blackjack FO recovery")));
      }
   }

   public void B(Minecraft var1) {
      if (var1.player == null || var1.level == null) {
         this.W = null;
         this.y = mm.b.L;
         this.w = this.p = this.d = this.G = this.f = (boolean)0;
         this.x = (boolean)0;
         this.X = null;
         Arrays.fill(this.U, -1);
      } else if (this.y == mm.b.Z) {
         if (var1.player.containerMenu != this.W) {
            this.y = mm.b.L;
            this.W = null;
            this.d = (boolean)0;
         }
      } else if (this.y != mm.b.L) {
         if (var1.player.containerMenu != this.W) {
            this.q("container closed during transfer");
         } else {
            if (this.y != mm.b.f || this.A && mz.F().A() == this.i && (this.n == null || !(var1.player.distanceToSqr(Vec3.atCenterOf(this.n)) > 64.0))) {
               if (this.y != mm.b.f && System.nanoTime() > this.u) {
                  if (this.y == mm.b.o || this.y == mm.b.R && this.K++ == 0) {
                     hh.K("Blackjack FO", "Returning parked cards after " + this.y + " confirmation timeout");
                     if (this.y == mm.b.o && this.f && this.L.size() >= 45) {
                        for (int var7 = 0; var7 < 9; var7++) {
                           if (this.U[var7] >= 0 && this.L.get(var7).isEmpty() && this.L.get(Q(this.U[var7])).isEmpty()) {
                              this.U[var7] = -1;
                           }
                        }
                     }

                     this.S();
                  } else {
                     this.q("server confirmation timed out in " + this.y);
                  }
               }
            } else {
               this.S();
            }
         }
      } else {
         if (this.W != null && var1.player.containerMenu != this.W) {
            this.W = null;
            this.d = (boolean)0;
            this.L.clear();
            this.r = 0L;
         }

         if (!this.A) {
            if (this.d && this.W != null && var1.gui.screen() == null) {
               var1.player.closeContainer();
            }

            this.w = (boolean)0;
         } else {
            long var2 = System.nanoTime();
            if (!this.p || var2 >= this.u) {
               this.p = (boolean)0;
               if (this.W != null && N(this.d, var1.gui.screen() != null, var2, this.u)) {
                  var1.player.closeContainer();
                  this.W = null;
                  this.L.clear();
                  this.w = (boolean)0;
                  this.r = 0L;
                  hh.K("Blackjack FO", "Dispenser opening timed out; reconnecting");
               }

               if (!this.d) {
                  if (var1.gui.screen() == null && var1.player.containerMenu == var1.player.inventoryMenu && var2 >= this.r) {
                     this.n = this.o();
                     if (this.n != null && !(var1.player.distanceToSqr(Vec3.atCenterOf(this.n)) > 64.0)) {
                        this.w = (boolean)1;
                        this.u = System.nanoTime() + 3000000000L;
                        this.r = System.nanoTime() + 3000000000L;
                        this.D = (boolean)1;

                        try {
                           var1.gameMode
                              .useItemOn(var1.player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(this.n), Direction.UP, this.n, false));
                        } finally {
                           this.D = (boolean)0;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private enum b {
      L,
      o,
      f,
      R,
      Z;
   }

   private record r(String side, int playerTotal, int dealerTotal, boolean playerSoft, boolean dealerSoft, int dealtCards, int pending, int availableMask) {
   }
}

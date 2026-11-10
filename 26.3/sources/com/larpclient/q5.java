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

public final class q5 {
   private static final int[] E = new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10};
   private static final long I = 15000000000L;
   private static final double H = 64.0;
   private static final q5 X = new q5();
   private q5.h A = q5.h.y;
   private boolean h;
   private boolean C;
   private boolean p;
   private boolean t;
   private boolean z;
   private boolean a;
   private boolean b;
   private boolean Q;
   private boolean O;
   private final int[] u = new int[9];
   private final boolean[] S = new boolean[9];
   private final ItemStack[] L = new ItemStack[9];
   private final List<ItemStack> G = new ArrayList<>();
   private DispenserMenu c;
   private BlockPos B;
   private BlockPos e;
   private long g;
   private long K;
   private int n;
   private int q;
   private int Z = -1;
   private int f = -1;
   private q5.b j;
   private boolean M;
   private int F = -1;
   private String R;
   private long l;
   private final int[] k = new int[11];

   public static q5 a() {
      return X;
   }

   private q5() {
      Arrays.fill(this.u, -1);
      Arrays.fill(this.k, -1);
   }

   public String r(Minecraft var1) {
      if (var1 != null && var1.player != null) {
         int[] var2 = new int[11];
         Arrays.fill(var2, -1);
         HashSet var3 = new HashSet();

         for (int var4 = 0; var4 < E.length; var4++) {
            int var5 = E.length - 1 - var4;
            ItemStack var6 = var1.player.getInventory().getItem(var5);
            MapId var7 = var6 != null && var6.is(Items.FILLED_MAP) ? (MapId)var6.get(DataComponents.MAP_ID) : null;
            if (var7 == null) {
               return "Visible hotbar slot " + (var4 + 1) + " must hold a filled card map.";
            }

            if (!var3.add(var7.id())) {
               return "Each hotbar slot needs a different card map.";
            }

            var2[E[var4]] = var7.id();
         }

         System.arraycopy(var2, 0, this.k, 0, var2.length);
         StringBuilder var9 = new StringBuilder("Imported hotbar map mapping:");

         for (int var8 : E) {
            var9.append(' ').append(var8 == 1 ? "A" : var8).append("=map#").append(var2[var8]);
         }

         bn.j("Blackjack FO", var9.toString());
         return null;
      } else {
         return "Join a world before importing the hotbar.";
      }
   }

   public int[] x() {
      return (int[])this.k.clone();
   }

   public void b(int[] var1) {
      Arrays.fill(this.k, -1);
      if (var1 != null) {
         System.arraycopy(var1, 0, this.k, 0, Math.min(var1.length, this.k.length));
      }
   }

   public boolean G() {
      for (int var4 : E) {
         if (this.k[var4] < 0) {
            return false;
         }
      }

      return true;
   }

   public int R(int var1) {
      return var1 >= 1 && var1 <= 10 ? this.k[var1] : -1;
   }

   public int n(int var1) {
      return e(this.k, var1);
   }

   static int e(int[] var0, int var1) {
      if (var1 >= 0 && var0 != null) {
         for (int var5 : E) {
            if (var5 < var0.length && var0[var5] == var1) {
               return var5;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public void t(boolean var1) {
      if (var1 && !this.h) {
         this.K = 0L;
         this.M = (boolean)0;
      }

      this.h = var1;
   }

   public boolean N() {
      return this.h;
   }

   public boolean L() {
      return this.A != q5.h.y;
   }

   public int M() {
      if (this.h && this.A != q5.h.r) {
         return this.A == q5.h.y ? this.L() : this.Z;
      } else {
         return -1;
      }
   }

   public int r(int var1, ItemStack var2) {
      if (this.A != q5.h.y && this.A != q5.h.r && var2 != null && !var2.isEmpty()) {
         for (int var3 = 0; var3 < this.u.length; var3++) {
            if (this.u[var3] == var1 && this.L[var3] != null && ItemStack.isSameItemSameComponents(var2, this.L[var3])) {
               return Math.min(var2.getCount(), this.L[var3].getCount());
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   public boolean C() {
      return this.A != q5.h.y && this.A != q5.h.r && !this.t;
   }

   public boolean l() {
      return this.t;
   }

   public boolean O() {
      return this.C();
   }

   public boolean f(Minecraft var1) {
      return var1.gui.screen() instanceof DispenserScreen var2 && var2.getMenu() == this.c;
   }

   public boolean n(Minecraft var1) {
      if (this.h
         && this.c != null
         && var1.player != null
         && var1.player.containerMenu == this.c
         && this.c.getCarried().isEmpty()
         && var1.gui.screen() == null
         && var1.hitResult instanceof BlockHitResult var2
         && var2.getBlockPos().equals(this.B())) {
         var1.gui.setScreen(new DispenserScreen(this.c, var1.player.getInventory(), Component.translatable("container.dropper")));
         return true;
      } else {
         return false;
      }
   }

   public int V(AbstractContainerMenu var1, int var2) {
      return this.t && var1 == this.c ? 32768 : var2;
   }

   public ItemStack v(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.A != q5.h.y && this.A != q5.h.r && var2.player != null) {
         for (int var6 : this.u) {
            if (var6 >= 0 && var1 == var2.player.getInventory().getItem(var6)) {
               return ItemStack.EMPTY;
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public ItemStack P(Minecraft var1, AbstractContainerMenu var2, Slot var3) {
      if (this.A != q5.h.y && this.A != q5.h.r && var2 == this.c && var1.player != null) {
         if (var3.container == var1.player.getInventory()) {
            for (int var7 : this.u) {
               if (var7 == var3.getContainerSlot()) {
                  return ItemStack.EMPTY;
               }
            }

            return null;
         } else {
            return var3.index >= 0 && var3.index < 9 && this.L[var3.index] != null ? this.L[var3.index] : null;
         }
      } else {
         return null;
      }
   }

   private BlockPos B() {
      return hk.e().f(hk.k.q);
   }

   public void c(BlockPos var1) {
      if (!this.t && this.h && this.A == q5.h.y && var1.equals(this.B())) {
         this.B = var1;
         this.p = (boolean)1;
         this.C = (boolean)0;
         this.g = System.nanoTime() + 3000000000L;
      }
   }

   public void a(AbstractContainerMenu var1) {
      if (var1 instanceof DispenserMenu var2) {
         hk var3 = hk.e();
         if (this.p || var3.l() == hk.k.q && var3.v() == var2.containerId) {
            this.c = var2;
            this.G.clear();
            this.p = this.C = this.z = (boolean)0;
         }
      }
   }

   public boolean f(AbstractContainerMenu var1) {
      return this.h && this.A != q5.h.r && var1 == this.c && this.c.getCarried().isEmpty();
   }

   public boolean u(MenuType<?> var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (!this.p && this.C && var1 == MenuType.GENERIC_3x3 && var3.player != null) {
         this.c = new DispenserMenu(var2, var3.player.getInventory());
         var3.player.containerMenu = this.c;
         this.C = this.z = (boolean)0;
         return true;
      } else {
         return false;
      }
   }

   public void g(int var1, List<ItemStack> var2, ItemStack var3) {
      if (this.c != null && var1 == this.c.containerId && var2.size() == this.c.slots.size()) {
         this.G.clear();

         for (ItemStack var5 : var2) {
            this.G.add(var5.copy());
         }

         this.z = (boolean)1;
         this.W(var3);
      }
   }

   public void u(int var1, int var2, ItemStack var3) {
      if (this.c != null && !this.G.isEmpty() && this.A != q5.h.r) {
         if (var1 == this.c.containerId && var2 >= 0 && var2 < this.G.size()) {
            this.G.set(var2, var3.copy());
         } else {
            if (var1 != 0 || j(var2) < 0 || var2 >= this.G.size()) {
               return;
            }

            this.G.set(var2, var3.copy());
         }

         this.W(this.c.getCarried());
      }
   }

   public void d(int var1, ItemStack var2) {
      if (this.c != null && !this.G.isEmpty() && this.A != q5.h.r && var1 >= 0 && var1 < 36) {
         this.G.set(k(var1), var2.copy());
         this.W(this.c.getCarried());
      }
   }

   private static int k(int var0) {
      return var0 < 9 ? 36 + var0 : var0;
   }

   static int D(int var0) {
      for (int var1 = 0; var1 < E.length; var1++) {
         if (E[var1] == var0) {
            return var1;
         }
      }

      return -1;
   }

   static boolean y(int var0, boolean var1, int var2, int var3) {
      return !var1 && var0 + var2 > 21 && var0 + var3 > 21;
   }

   static int j(int var0) {
      return var0 >= 9 && var0 < 45 ? var0 : -1;
   }

   private boolean q(boolean var1) {
      if (this.G.size() < 45) {
         return false;
      }

      for (int var2 = 0; var2 < 9; var2++) {
         if (this.u[var2] >= 0) {
            if (!ItemStack.matches(this.G.get(var2), var1 ? ItemStack.EMPTY : this.L[var2])) {
               return false;
            }

            ItemStack var3 = this.G.get(k(this.u[var2]));
            if (var1 ? !ItemStack.matches(var3, this.L[var2]) : !var3.isEmpty()) {
               return false;
            }
         }
      }

      return true;
   }

   private boolean c() {
      if (this.G.size() < 9) {
         return false;
      }

      for (int var1 = 0; var1 < 9; var1++) {
         if (this.u[var1] < 0 && this.L[var1] != null && !this.L[var1].isEmpty() && !ItemStack.matches(this.G.get(var1), this.L[var1])) {
            return true;
         }
      }

      return false;
   }

   private void W(ItemStack var1) {
      if (var1.isEmpty()) {
         if (this.A == q5.h.M && this.q(true)) {
            this.A = q5.h.Z;
            if (!this.c() && !this.Q && this.h && io.z().e() == this.O) {
               bn.j("Blackjack FO", (this.O ? "Dealer" : "Player") + " draw ready");
            } else {
               this.w();
            }
         } else if (this.A != q5.h.Z || !this.c() && !this.Q && this.q(true)) {
            if (this.A == q5.h.p) {
               this.l(false);
               if (this.A == q5.h.p && this.q(false)) {
                  this.A = q5.h.y;
                  this.Q = (boolean)0;
                  Arrays.fill(this.u, -1);
                  bn.j("Blackjack FO", "Other cards returned after dispenser draw");
               }
            }
         } else {
            this.w();
         }
      }
   }

   static int b(boolean var0, boolean var1, boolean var2, boolean var3) {
      if (var0 && var3) {
         return 0;
      } else {
         return var1 && var2 ? 1 : -1;
      }
   }

   static boolean k(int var0, boolean var1) {
      return var0 == 1 && !var1;
   }

   static boolean F(boolean var0, boolean var1, long var2, long var4) {
      return !var0 && !var1 && var2 >= var4;
   }

   private int Z(int var1) {
      if (this.G.size() < 45) {
         return -1;
      }

      ItemStack var2 = this.G.get(var1);
      ItemStack var3 = this.G.get(k(this.u[var1]));
      return b(ItemStack.matches(var2, this.L[var1]), var2.isEmpty(), ItemStack.matches(var3, this.L[var1]), var3.isEmpty());
   }

   public void f(BlockPos var1, BlockState var2) {
      if (this.B != null) {
         if (var2.getBlock() instanceof LeverBlock && var1.distSqr(this.B) <= 9.0) {
            boolean var5 = (Boolean)var2.getValue(LeverBlock.POWERED);
            int var6 = !var5 || var1.equals(this.e) && this.b ? 0 : 1;
            this.e = var1.immutable();
            this.b = var5;
            if (var6 != 0) {
               this.V("lever");
            }
         } else if (this.B.equals(var1)) {
            int var3 = var2.getBlock() instanceof DispenserBlock && var2.getValue(DispenserBlock.TRIGGERED) ? 1 : 0;
            int var4 = var3 != 0 && !this.a ? 1 : 0;
            this.a = (boolean)var3;
            if (var4 != 0) {
               this.V("dispenser");
            }
         }
      }
   }

   private void V(String var1) {
      if (this.h && System.nanoTime() >= this.l) {
         int var2 = this.A == q5.h.y ? this.L() : -1;
         if (var2 > 0 && this.O(var2)) {
            this.M = (boolean)1;
            io var4 = io.z();
            this.F = var4.S();
            this.R = var4.R();
            bn.j(
               "Blackjack FO",
               "Started rigging " + var2 + " for " + (var4.e() ? "dealer" : "player") + " on " + var1 + ": player=" + var4.g() + " dealer=" + var4.b()
            );
         } else if (this.A == q5.h.y && (io.z().e() || io.z().g() >= 12)) {
            io var3 = io.z();
            bn.j(
               "Blackjack FO",
               (var3.e() ? "Dealer" : "Player")
                  + " draw unforced on "
                  + var1
                  + ": transfer="
                  + this.A
                  + " connected="
                  + this.z
                  + " slots="
                  + this.G.size()
                  + " cards="
                  + var3.S()
                  + " pending="
                  + var3.A()
                  + " player="
                  + var3.g()
                  + " dealer="
                  + var3.b()
            );
         }
      }
   }

   private int L() {
      if (this.z && this.G.size() >= 45) {
         boolean[] var1 = new boolean[11];
         int var2 = 0;

         for (int var3 = 0; var3 < E.length; var3++) {
            ItemStack var4 = this.G.get(var3);
            MapId var5 = var4.is(Items.FILLED_MAP) ? (MapId)var4.get(DataComponents.MAP_ID) : null;
            int var6 = this.G() ? (var5 == null ? -1 : this.n(var5.id())) : E[var3];
            if (var6 >= 1 && var6 <= 10) {
               var1[var6] = (boolean)(var5 != null ? 1 : 0);
               if (var1[var6]) {
                  var2 |= 1 << var6;
               }
            }
         }

         io var7 = io.z();
         if (this.M && (var7.S() != this.F || !var7.R().equals(this.R))) {
            this.M = (boolean)0;
         }

         q5.b var8 = new q5.b(var7.R(), var7.g(), var7.b(), var7.i(), var7.F(), var7.S(), var7.A(), var2);
         if (!var8.equals(this.j)) {
            this.j = var8;
            this.f = var7.e() ? var7.W(var1) : var7.m(var1);
         }

         return this.M ? -1 : this.f;
      } else {
         this.j = null;
         this.f = -1;
         return -1;
      }
   }

   public void M(BlockPos var1) {
      if (this.B != null && this.B.equals(var1) && (this.A == q5.h.M || this.A == q5.h.Z)) {
         this.Q = (boolean)1;
         if (this.A == q5.h.Z) {
            this.w();
         }
      }
   }

   public void F(BlockPos var1) {
      if (this.h && this.B != null && var1.distSqr(this.B) <= 9.0) {
         this.l = System.nanoTime() + 2000000000L;
      }
   }

   static int[] Z(int[] var0, boolean[] var1, boolean[] var2, int var3, int var4) {
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

   private boolean O(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.z && this.c != null && var2.player != null && var2.player.containerMenu == this.c && this.c.getCarried().isEmpty() && this.G.size() >= 45) {
         int var3 = this.P(var1);
         MapId var4 = var3 < 0 ? null : (MapId)this.G.get(var3).get(DataComponents.MAP_ID);
         int var5 = var4 == null ? -1 : var4.id();
         if (var5 < 0) {
            bn.j("Blackjack FO", "Skipped draw: no card template for rank " + var1);
            return false;
         }

         int[] var6 = new int[9];
         boolean[] var7 = new boolean[9];
         boolean[] var8 = new boolean[36];
         io var9 = io.z();
         this.O = var9.e();
         int var10 = var9.b();
         boolean var11 = var9.F();

         for (int var12 = 0; var12 < 9; var12++) {
            ItemStack var13 = this.G.get(var12);
            MapId var14 = var13.is(Items.FILLED_MAP) ? (MapId)var13.get(DataComponents.MAP_ID) : null;
            var6[var12] = var14 == null ? -1 : var14.id();
            int var15 = this.G() ? (var14 == null ? -1 : this.n(var14.id())) : E[var12];
            var7[var12] = (boolean)(var13.isEmpty()
                  || var12 != var3 && var14 != null && var15 >= 1 && (this.O ? y(var10, var11, var1, var15) : var9.S(var1, var15))
               ? 0
               : 1);
         }

         for (int var21 = 0; var21 < 36; var21++) {
            var8[var21] = (boolean)(this.G.get(k(var21)).isEmpty() && var2.player.getInventory().getItem(var21).isEmpty() ? 0 : 1);
         }

         int[] var22 = Z(var6, var7, var8, var2.player.getInventory().getSelectedSlot(), var5);
         if (var22 == null) {
            bn.j("Blackjack FO", "Skipped draw: card missing/duplicated or not enough empty inventory slots");
            return false;
         }

         Arrays.fill(this.u, -1);
         Arrays.fill(this.S, false);

         for (int var23 = 0; var23 < 9; var23++) {
            this.L[var23] = this.G.get(var23).copy();
            this.u[var23] = var22[var23] >= 0 ? var22[var23] : -1;
         }

         this.A = q5.h.M;
         this.Z = var1;
         this.Q = (boolean)0;
         this.n = 0;
         this.q = 0;
         this.g = System.nanoTime() + 15000000000L;
         this.t = (boolean)1;

         try {
            for (int var24 = 0; var24 < 9; var24++) {
               if (this.u[var24] >= 0) {
                  this.I(var24, this.u[var24], false, var2);
               }
            }
         } catch (RuntimeException var19) {
            this.O("removal click failed");
            return false;
         } finally {
            this.t = (boolean)0;
         }

         return true;
      } else {
         bn.j("Blackjack FO", "Skipped draw: dispenser not ready");
         return false;
      }
   }

   private int P(int var1) {
      if (!this.G()) {
         return D(var1);
      }

      int var2 = this.R(var1);
      if (var2 >= 0 && this.G.size() >= 9) {
         int var3 = -1;

         for (int var4 = 0; var4 < 9; var4++) {
            MapId var5 = this.G.get(var4).is(Items.FILLED_MAP) ? (MapId)this.G.get(var4).get(DataComponents.MAP_ID) : null;
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

   private void w() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.A != q5.h.r) {
         if (var1.player != null && var1.player.containerMenu == this.c && this.c.getCarried().isEmpty() && this.G.size() >= 45) {
            this.A = q5.h.p;
            this.g = System.nanoTime() + 15000000000L;
            Arrays.fill(this.S, false);
            this.l(true);
            this.W(this.c.getCarried());
         } else {
            this.O("return state changed");
         }
      }
   }

   private void l(boolean var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu == this.c && this.G.size() >= 45) {
         StringBuilder var3 = new StringBuilder();
         this.t = (boolean)1;

         try {
            int var4 = 0;

            for (int var5 = 0; var5 < 9; var5++) {
               if (this.u[var5] >= 0) {
                  int var6 = this.Z(var5);
                  if (var6 == 0) {
                     var4++;
                  } else {
                     if (var4 > this.q) {
                        this.q = var4;
                        this.n = 0;
                        this.g = System.nanoTime() + 15000000000L;
                     }

                     if (var6 < 0) {
                        if (var1) {
                           var3.append(' ').append(var5 + 1).append('[').append(this.G.get(var5)).append(" / ").append(this.G.get(k(this.u[var5]))).append(']');
                        }
                     } else if (k(var6, this.S[var5])) {
                        this.I(var5, this.u[var5], true, var2);
                        this.S[var5] = (boolean)1;
                     }
                  }
               }
            }
         } catch (RuntimeException var10) {
            this.O("return click failed");
         } finally {
            this.t = (boolean)0;
         }

         if (!var3.isEmpty()) {
            bn.j("Blackjack FO", "Waiting for changed slots:" + var3);
         }
      }
   }

   private void I(int var1, int var2, boolean var3, Minecraft var4) {
      if (var2 < 9) {
         var4.gameMode.handleContainerInput(this.c.containerId, var1, var2, ContainerInput.SWAP, var4.player);
      } else {
         int var5 = var3 ? k(var2) : var1;
         int var6 = var3 ? var1 : k(var2);
         var4.gameMode.handleContainerInput(this.c.containerId, var5, 0, ContainerInput.PICKUP, var4.player);
         var4.gameMode.handleContainerInput(this.c.containerId, var6, 0, ContainerInput.PICKUP, var4.player);
      }
   }

   private void O(String var1) {
      this.A = q5.h.r;
      this.h = (boolean)0;
      bn.j("Blackjack FO", "Stopped: " + var1 + "; inspect real slots, no automatic replay");
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu == this.c) {
         var2.gui.setScreen(new DispenserScreen(this.c, var2.player.getInventory(), Component.literal("Blackjack FO recovery")));
      }
   }

   public void m(Minecraft var1) {
      if (var1.player == null || var1.level == null) {
         this.c = null;
         this.A = q5.h.y;
         this.C = this.p = this.z = this.a = this.Q = (boolean)0;
         this.b = (boolean)0;
         this.e = null;
         Arrays.fill(this.u, -1);
      } else if (this.A == q5.h.r) {
         if (var1.player.containerMenu != this.c) {
            this.A = q5.h.y;
            this.c = null;
            this.z = (boolean)0;
         }
      } else if (this.A != q5.h.y) {
         if (var1.player.containerMenu != this.c) {
            this.O("container closed during transfer");
         } else {
            if (this.A != q5.h.Z || this.h && io.z().e() == this.O && (this.B == null || !(var1.player.distanceToSqr(Vec3.atCenterOf(this.B)) > 64.0))) {
               if (this.A != q5.h.Z && System.nanoTime() > this.g) {
                  if (this.A == q5.h.M || this.A == q5.h.p && this.n++ == 0) {
                     bn.j("Blackjack FO", "Returning parked cards after " + this.A + " confirmation timeout");
                     if (this.A == q5.h.M && this.Q && this.G.size() >= 45) {
                        for (int var7 = 0; var7 < 9; var7++) {
                           if (this.u[var7] >= 0 && this.G.get(var7).isEmpty() && this.G.get(k(this.u[var7])).isEmpty()) {
                              this.u[var7] = -1;
                           }
                        }
                     }

                     this.w();
                  } else {
                     this.O("server confirmation timed out in " + this.A);
                  }
               }
            } else {
               this.w();
            }
         }
      } else {
         if (this.c != null && var1.player.containerMenu != this.c) {
            this.c = null;
            this.z = (boolean)0;
            this.G.clear();
            this.K = 0L;
         }

         if (!this.h) {
            if (this.z && this.c != null && var1.gui.screen() == null) {
               var1.player.closeContainer();
            }

            this.C = (boolean)0;
         } else {
            long var2 = System.nanoTime();
            if (!this.p || var2 >= this.g) {
               this.p = (boolean)0;
               if (this.c != null && F(this.z, var1.gui.screen() != null, var2, this.g)) {
                  var1.player.closeContainer();
                  this.c = null;
                  this.G.clear();
                  this.C = (boolean)0;
                  this.K = 0L;
                  bn.j("Blackjack FO", "Dispenser opening timed out; reconnecting");
               }

               if (!this.z) {
                  if (var1.gui.screen() == null && var1.player.containerMenu == var1.player.inventoryMenu && var2 >= this.K) {
                     this.B = this.B();
                     if (this.B != null && !(var1.player.distanceToSqr(Vec3.atCenterOf(this.B)) > 64.0)) {
                        this.C = (boolean)1;
                        this.g = System.nanoTime() + 3000000000L;
                        this.K = System.nanoTime() + 3000000000L;
                        this.t = (boolean)1;

                        try {
                           var1.gameMode
                              .useItemOn(var1.player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(this.B), Direction.UP, this.B, false));
                        } finally {
                           this.t = (boolean)0;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private record b(String side, int playerTotal, int dealerTotal, boolean playerSoft, boolean dealerSoft, int dealtCards, int pending, int availableMask) {
   }

   private enum h {
      y,
      M,
      Z,
      p,
      r;
   }
}

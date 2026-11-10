package com.larpclient;

import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Environment(EnvType.CLIENT)
public final class g5 {
   private static final Item[] X = new Item[]{
      Items.DIRT,
      Items.COPPER_INGOT,
      Items.IRON_INGOT,
      Items.DIAMOND,
      Items.GILDED_BLACKSTONE,
      Items.ANCIENT_DEBRIS,
      Items.NETHERITE_INGOT,
      Items.MACE,
      Items.TRIDENT
   };
   private static final String L = "Host";
   private static final String H = "Viewer";
   private static final g5 D = new g5();
   public Item l;
   public Item i;
   public boolean Z;
   private final Item[] V = new Item[9];
   private final Item[] J = new Item[9];
   private final ItemStack[] m = new ItemStack[9];
   private final ItemStack[] M = new ItemStack[9];
   private boolean G;
   private boolean j;
   private long q;
   private g5.q g;
   private String u;
   private String s;
   private final bn E = new bn();
   private boolean K;
   private boolean P;

   private g5() {
      this.l = Items.APPLE;
      this.i = Items.DIAMOND;
      this.Z = (boolean)0;

      for (int var1 = 0; var1 < X.length; var1++) {
         this.V[var1] = X[var1];
         this.J[var1] = X[var1];
         this.m[var1] = new ItemStack(X[var1]);
         this.M[var1] = new ItemStack(X[var1]);
      }

      this.G = (boolean)1;
      this.j = (boolean)0;
      this.g = g5.q.z;
      this.u = "Host";
      this.s = "Viewer";
   }

   public static g5 a() {
      return D;
   }

   public g5.q k() {
      return this.g;
   }

   public String q() {
      return this.g.Z();
   }

   public String z() {
      return this.Z ? this.q() : "off";
   }

   public String X() {
      return this.g.B();
   }

   public boolean b(g5.q var1) {
      if (var1 != null && this.g != var1) {
         this.g = var1;
         this.S();
         te.Z("Paper", "Visual winner target switched to " + var1.B());
         return true;
      } else {
         return false;
      }
   }

   public boolean F(String var1) {
      if (var1 == null) {
         return false;
      }

      return (boolean)(switch (var1.trim().toLowerCase(Locale.ROOT)) {
         case "host" -> this.b(g5.q.z);
         case "viewer" -> this.b(g5.q.t);
         default -> 0;
      });
   }

   public void A() {
      this.b(this.g == g5.q.z ? g5.q.t : g5.q.z);
   }

   public boolean g() {
      return this.G;
   }

   public long B() {
      return this.q;
   }

   public boolean G(boolean var1) {
      if (this.G == var1) {
         return false;
      }

      this.G = var1;
      if (var1) {
         for (int var2 = 0; var2 < this.V.length; var2++) {
            this.J[var2] = this.V[var2];
            this.M[var2] = t(this.m[var2]);
         }
      }

      this.q++;
      this.S();
      return true;
   }

   public boolean b() {
      return this.j;
   }

   public void q(boolean var1) {
      this.j = var1;
   }

   public String e() {
      return this.u;
   }

   public void d(String var1) {
      String var2 = e(var1);
      if (var2 != null) {
         this.u = var2;
      }
   }

   public String c() {
      return this.s;
   }

   public void E(String var1) {
      String var2 = e(var1);
      if (var2 != null) {
         this.s = var2;
      }
   }

   public Item a(boolean var1, int var2) {
      if (var2 < 0 || var2 >= this.V.length) {
         return Items.PAPER;
      } else if (this.G) {
         return this.V[var2];
      } else {
         return var1 ? this.V[var2] : this.J[var2];
      }
   }

   public ItemStack Y(boolean var1, int var2) {
      if (var2 >= 0 && var2 < this.m.length) {
         ItemStack var3 = this.G ? this.m[var2] : (var1 ? this.m[var2] : this.M[var2]);
         return t(var3);
      } else {
         return ItemStack.EMPTY;
      }
   }

   public void s(boolean var1, int var2, Item var3) {
      if (var2 >= 0 && var2 < this.V.length && var3 != null && var3 != Items.AIR) {
         this.z(var1, var2, new ItemStack(var3));
      }
   }

   public boolean z(boolean var1, int var2, ItemStack var3) {
      if (var2 >= 0 && var2 < this.V.length && var3 != null) {
         ItemStack var4 = t(var3);
         if (this.G) {
            int var5 = x(this.m[var2], var4) && x(this.M[var2], var4) ? 0 : 1;
            if (var5 == 0) {
               return false;
            }

            this.m[var2] = var4;
            this.M[var2] = t(var4);
            if (!var4.isEmpty()) {
               this.V[var2] = var4.getItem();
               this.J[var2] = var4.getItem();
            }
         } else if (var1) {
            int var6 = !x(this.m[var2], var4) ? 1 : 0;
            if (var6 == 0) {
               return false;
            }

            this.m[var2] = var4;
            if (!var4.isEmpty()) {
               this.V[var2] = var4.getItem();
            }
         } else {
            int var7 = !x(this.M[var2], var4) ? 1 : 0;
            if (var7 == 0) {
               return false;
            }

            this.M[var2] = var4;
            if (!var4.isEmpty()) {
               this.J[var2] = var4.getItem();
            }
         }

         this.q++;
         return true;
      } else {
         return false;
      }
   }

   public boolean E(boolean var1, int var2, ItemStack var3) {
      if (!var3.isEmpty() && !this.E.q()) {
         int var4 = zv.K(var3.getHoverName().getString());
         if (var4 > 0 && zv.X(var3.getHoverName().getString()) != var1) {
            return false;
         }

         if (var4 < 1) {
            var4 = var2 + 1;
         }

         this.E.y(var1 ? 0 : 1, var2, var4);
         return this.z(var1, var4 - 1, var3);
      } else {
         return false;
      }
   }

   public void d(Minecraft var1, boolean var2, BlockPos var3) {
   }

   public void M(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         gj var2 = gj.c();
         this.E.k(var1, this, var2.q(gj.d.J), var2.q(gj.d.R));
         this.K = this.Z;
         this.E.L(var1);
      } else {
         if (this.K) {
            this.E.h(var1);
         }

         this.K = (boolean)0;
      }
   }

   public void D(Minecraft var1, int var2, int var3, int var4) {
   }

   public ItemStack p(ItemEntity var1, ItemStack var2) {
      return this.Z && var1 != null ? this.E.p(Minecraft.getInstance(), var1) : var2;
   }

   public ItemStack y(int var1, ItemStack var2) {
      return this.Z ? this.E.Y(var2) : var2;
   }

   public ItemStack E(ItemStack var1) {
      return this.Z ? this.E.Y(var1) : var1;
   }

   public ItemStack M(LivingEntity var1, ItemStack var2) {
      return this.Z && var1 == Minecraft.getInstance().player ? this.E.Y(var2) : var2;
   }

   private int z(Minecraft var1, DispenserMenu var2) {
      gj var3 = gj.c();
      if (this.Z && var1 != null && var1.player != null && var2 == var1.player.containerMenu && var2.containerId == var3.r()) {
         for (int var4 = 0; var4 < 2; var4++) {
            BlockPos var5 = this.E.G[var4];
            if (var5 != null && var3.w(var1, var5).equals(var3.C())) {
               return var4;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public void D(Minecraft var1, DispenserMenu var2) {
      gj var3 = gj.c();
      this.E.k(var1, this, var3.q(gj.d.J), var3.q(gj.d.R));
      int var4 = this.z(var1, var2);
      if (var4 >= 0) {
         this.E.M(var2, var4, true);
      }
   }

   public ItemStack i(Minecraft var1, Slot var2) {
      if (var1 != null && var1.player != null && var2.container != var1.player.getInventory() && var1.player.containerMenu instanceof DispenserMenu var3) {
         int var5 = this.z(var1, var3);
         return var5 < 0 ? null : this.E.L(var1, var3, var5, var2.getContainerSlot());
      } else {
         return null;
      }
   }

   public boolean B(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (!this.P
         && var1 != null
         && var1.player != null
         && var1.gameMode != null
         && var1.player.containerMenu instanceof DispenserMenu var6
         && var6.containerId == var2
         && var3 >= 0
         && var3 < 9) {
         int var12 = this.z(var1, var6);
         if (var12 < 0) {
            return false;
         }

         int var8 = this.E.E(var1, var6, var12, var3);
         if (var8 == var3) {
            return false;
         }

         this.P = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var8, var4, var5, var1.player);
         } finally {
            this.P = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   private void S() {
      this.E.h(Minecraft.getInstance());
      this.K = (boolean)0;
   }

   private static boolean x(ItemStack var0, ItemStack var1) {
      int var2 = var0 != null && !var0.isEmpty() ? 0 : 1;
      int var3 = var1 != null && !var1.isEmpty() ? 0 : 1;
      return var2 == 0 && var3 == 0 ? ItemStack.isSameItemSameComponents(var0, var1) : var2 == var3;
   }

   private static ItemStack t(ItemStack var0) {
      return var0 != null && !var0.isEmpty() ? var0.copyWithCount(1) : ItemStack.EMPTY;
   }

   private static String e(String var0) {
      if (var0 == null) {
         return null;
      }

      String var1 = var0.trim();
      if (var1.length() > 24) {
         var1 = var1.substring(0, 24);
      }

      return var1;
   }

   public enum q {
      z("host", "Host"),
      t("viewer", "Viewer");

      private final String Z;
      private final String L;

      q(String var3, String var4) {
         this.Z = var3;
         this.L = var4;
      }

      public String Z() {
         return this.Z;
      }

      public String B() {
         return this.L;
      }
   }
}

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
public final class mi {
   private static final Item[] B = new Item[]{
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
   private static final String o = "Host";
   private static final String z = "Viewer";
   private static final mi K = new mi();
   public Item h;
   public Item I;
   public boolean p;
   private final Item[] G = new Item[9];
   private final Item[] r = new Item[9];
   private final ItemStack[] i = new ItemStack[9];
   private final ItemStack[] a = new ItemStack[9];
   private boolean Z;
   private boolean H;
   private long y;
   private mi.c v;
   private String l;
   private String Y;
   private final xr M = new xr();
   private boolean m;
   private boolean E;

   private mi() {
      this.h = Items.APPLE;
      this.I = Items.DIAMOND;
      this.p = (boolean)0;

      for (int var1 = 0; var1 < B.length; var1++) {
         this.G[var1] = B[var1];
         this.r[var1] = B[var1];
         this.i[var1] = new ItemStack(B[var1]);
         this.a[var1] = new ItemStack(B[var1]);
      }

      this.Z = (boolean)1;
      this.H = (boolean)0;
      this.v = mi.c.o;
      this.l = "Host";
      this.Y = "Viewer";
   }

   public static mi L() {
      return K;
   }

   public mi.c p() {
      return this.v;
   }

   public String t() {
      return this.v.T();
   }

   public String E() {
      return this.p ? this.t() : "off";
   }

   public String Q() {
      return this.v.k();
   }

   public boolean O(mi.c var1) {
      if (var1 != null && this.v != var1) {
         this.v = var1;
         this.K();
         xa.q("Paper", "Visual winner target switched to " + var1.k());
         return true;
      } else {
         return false;
      }
   }

   public boolean C(String var1) {
      if (var1 == null) {
         return false;
      }

      return (boolean)(switch (var1.trim().toLowerCase(Locale.ROOT)) {
         case "host" -> this.O(mi.c.o);
         case "viewer" -> this.O(mi.c.b);
         default -> 0;
      });
   }

   public void h() {
      this.O(this.v == mi.c.o ? mi.c.b : mi.c.o);
   }

   public boolean l() {
      return this.Z;
   }

   public long a() {
      return this.y;
   }

   public boolean W(boolean var1) {
      if (this.Z == var1) {
         return false;
      }

      this.Z = var1;
      if (var1) {
         for (int var2 = 0; var2 < this.G.length; var2++) {
            this.r[var2] = this.G[var2];
            this.a[var2] = X(this.i[var2]);
         }
      }

      this.y++;
      this.K();
      return true;
   }

   public boolean N() {
      return this.H;
   }

   public void l(boolean var1) {
      this.H = var1;
   }

   public String u() {
      return this.l;
   }

   public void f(String var1) {
      String var2 = z(var1);
      if (var2 != null) {
         this.l = var2;
      }
   }

   public String o() {
      return this.Y;
   }

   public void X(String var1) {
      String var2 = z(var1);
      if (var2 != null) {
         this.Y = var2;
      }
   }

   public Item m(boolean var1, int var2) {
      if (var2 < 0 || var2 >= this.G.length) {
         return Items.PAPER;
      } else if (this.Z) {
         return this.G[var2];
      } else {
         return var1 ? this.G[var2] : this.r[var2];
      }
   }

   public ItemStack m(boolean var1, int var2) {
      if (var2 >= 0 && var2 < this.i.length) {
         ItemStack var3 = this.Z ? this.i[var2] : (var1 ? this.i[var2] : this.a[var2]);
         return X(var3);
      } else {
         return ItemStack.EMPTY;
      }
   }

   public void F(boolean var1, int var2, Item var3) {
      if (var2 >= 0 && var2 < this.G.length && var3 != null && var3 != Items.AIR) {
         this.U(var1, var2, new ItemStack(var3));
      }
   }

   public boolean U(boolean var1, int var2, ItemStack var3) {
      if (var2 >= 0 && var2 < this.G.length && var3 != null) {
         ItemStack var4 = X(var3);
         if (this.Z) {
            int var5 = H(this.i[var2], var4) && H(this.a[var2], var4) ? 0 : 1;
            if (var5 == 0) {
               return false;
            }

            this.i[var2] = var4;
            this.a[var2] = X(var4);
            if (!var4.isEmpty()) {
               this.G[var2] = var4.getItem();
               this.r[var2] = var4.getItem();
            }
         } else if (var1) {
            int var6 = !H(this.i[var2], var4) ? 1 : 0;
            if (var6 == 0) {
               return false;
            }

            this.i[var2] = var4;
            if (!var4.isEmpty()) {
               this.G[var2] = var4.getItem();
            }
         } else {
            int var7 = !H(this.a[var2], var4) ? 1 : 0;
            if (var7 == 0) {
               return false;
            }

            this.a[var2] = var4;
            if (!var4.isEmpty()) {
               this.r[var2] = var4.getItem();
            }
         }

         this.y++;
         return true;
      } else {
         return false;
      }
   }

   public boolean I(boolean var1, int var2, ItemStack var3) {
      if (!var3.isEmpty() && !this.M.I()) {
         int var4 = ol.N(var3.getHoverName().getString());
         if (var4 > 0 && ol.U(var3.getHoverName().getString()) != var1) {
            return false;
         }

         if (var4 < 1) {
            var4 = var2 + 1;
         }

         this.M.o(var1 ? 0 : 1, var2, var4);
         return this.U(var1, var4 - 1, var3);
      } else {
         return false;
      }
   }

   public void z(Minecraft var1, boolean var2, BlockPos var3) {
   }

   public void s(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         ep var2 = ep.H();
         this.M.w(var1, this, var2.n(ep.o.k), var2.n(ep.o.h));
         this.m = this.p;
         this.M.e(var1);
      } else {
         if (this.m) {
            this.M.n(var1);
         }

         this.m = (boolean)0;
      }
   }

   public void I(Minecraft var1, int var2, int var3, int var4) {
   }

   public ItemStack Z(ItemEntity var1, ItemStack var2) {
      return this.p && var1 != null ? this.M.L(Minecraft.getInstance(), var1) : var2;
   }

   public ItemStack Z(int var1, ItemStack var2) {
      return this.p ? this.M.t(var2) : var2;
   }

   public ItemStack x(ItemStack var1) {
      return this.p ? this.M.t(var1) : var1;
   }

   public ItemStack u(LivingEntity var1, ItemStack var2) {
      return this.p && var1 == Minecraft.getInstance().player ? this.M.t(var2) : var2;
   }

   private int H(Minecraft var1, DispenserMenu var2) {
      ep var3 = ep.H();
      if (this.p && var1 != null && var1.player != null && var2 == var1.player.containerMenu && var2.containerId == var3.V()) {
         for (int var4 = 0; var4 < 2; var4++) {
            BlockPos var5 = this.M.F[var4];
            if (var5 != null && var3.m(var1, var5).equals(var3.b())) {
               return var4;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public void R(Minecraft var1, DispenserMenu var2) {
      ep var3 = ep.H();
      this.M.w(var1, this, var3.n(ep.o.k), var3.n(ep.o.h));
      int var4 = this.H(var1, var2);
      if (var4 >= 0) {
         this.M.u(var2, var4, true);
      }
   }

   public ItemStack i(Minecraft var1, Slot var2) {
      if (var1 != null && var1.player != null && var2.container != var1.player.getInventory() && var1.player.containerMenu instanceof DispenserMenu var3) {
         int var5 = this.H(var1, var3);
         return var5 < 0 ? null : this.M.o(var1, var3, var5, var2.getContainerSlot());
      } else {
         return null;
      }
   }

   public boolean G(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (!this.E
         && var1 != null
         && var1.player != null
         && var1.gameMode != null
         && var1.player.containerMenu instanceof DispenserMenu var6
         && var6.containerId == var2
         && var3 >= 0
         && var3 < 9) {
         int var12 = this.H(var1, var6);
         if (var12 < 0) {
            return false;
         }

         int var8 = this.M.P(var1, var6, var12, var3);
         if (var8 == var3) {
            return false;
         }

         this.E = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var8, var4, var5, var1.player);
         } finally {
            this.E = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   private void K() {
      this.M.n(Minecraft.getInstance());
      this.m = (boolean)0;
   }

   private static boolean H(ItemStack var0, ItemStack var1) {
      int var2 = var0 != null && !var0.isEmpty() ? 0 : 1;
      int var3 = var1 != null && !var1.isEmpty() ? 0 : 1;
      return var2 == 0 && var3 == 0 ? ItemStack.isSameItemSameComponents(var0, var1) : var2 == var3;
   }

   private static ItemStack X(ItemStack var0) {
      return var0 != null && !var0.isEmpty() ? var0.copyWithCount(1) : ItemStack.EMPTY;
   }

   private static String z(String var0) {
      if (var0 == null) {
         return null;
      }

      String var1 = var0.trim();
      if (var1.length() > 24) {
         var1 = var1.substring(0, 24);
      }

      return var1;
   }

   public enum c {
      o("host", "Host"),
      b("viewer", "Viewer");

      private final String A;
      private final String U;

      c(String var3, String var4) {
         this.A = var3;
         this.U = var4;
      }

      public String T() {
         return this.A;
      }

      public String k() {
         return this.U;
      }
   }
}

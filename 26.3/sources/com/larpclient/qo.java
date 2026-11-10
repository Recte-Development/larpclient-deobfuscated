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
public final class qo {
   private static final Item[] t = new Item[]{
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
   private static final String e = "Host";
   private static final String O = "Viewer";
   private static final qo k = new qo();
   public Item P;
   public Item i;
   public boolean u;
   private final Item[] R = new Item[9];
   private final Item[] o = new Item[9];
   private final ItemStack[] m = new ItemStack[9];
   private final ItemStack[] U = new ItemStack[9];
   private boolean f;
   private boolean T;
   private long a;
   private qo.s j;
   private String M;
   private String D;
   private final j7 s = new j7();
   private boolean H;
   private boolean G;

   private qo() {
      this.P = Items.APPLE;
      this.i = Items.DIAMOND;
      this.u = (boolean)0;

      for (int var1 = 0; var1 < t.length; var1++) {
         this.R[var1] = t[var1];
         this.o[var1] = t[var1];
         this.m[var1] = new ItemStack(t[var1]);
         this.U[var1] = new ItemStack(t[var1]);
      }

      this.f = (boolean)1;
      this.T = (boolean)0;
      this.j = qo.s.z;
      this.M = "Host";
      this.D = "Viewer";
   }

   public static qo Z() {
      return k;
   }

   public qo.s l() {
      return this.j;
   }

   public String I() {
      return this.j.Y();
   }

   public String S() {
      return this.u ? this.I() : "off";
   }

   public String a() {
      return this.j.x();
   }

   public boolean R(qo.s var1) {
      if (var1 != null && this.j != var1) {
         this.j = var1;
         this.D();
         bn.j("Paper", "Visual winner target switched to " + var1.x());
         return true;
      } else {
         return false;
      }
   }

   public boolean X(String var1) {
      if (var1 == null) {
         return false;
      }

      return (boolean)(switch (var1.trim().toLowerCase(Locale.ROOT)) {
         case "host" -> this.R(qo.s.z);
         case "viewer" -> this.R(qo.s.j);
         default -> 0;
      });
   }

   public void B() {
      this.R(this.j == qo.s.z ? qo.s.j : qo.s.z);
   }

   public boolean a() {
      return this.f;
   }

   public long B() {
      return this.a;
   }

   public boolean Q(boolean var1) {
      if (this.f == var1) {
         return false;
      }

      this.f = var1;
      if (var1) {
         for (int var2 = 0; var2 < this.R.length; var2++) {
            this.o[var2] = this.R[var2];
            this.U[var2] = c(this.m[var2]);
         }
      }

      this.a++;
      this.D();
      return true;
   }

   public boolean p() {
      return this.T;
   }

   public void B(boolean var1) {
      this.T = var1;
   }

   public String o() {
      return this.M;
   }

   public void M(String var1) {
      String var2 = x(var1);
      if (var2 != null) {
         this.M = var2;
      }
   }

   public String R() {
      return this.D;
   }

   public void X(String var1) {
      String var2 = x(var1);
      if (var2 != null) {
         this.D = var2;
      }
   }

   public Item F(boolean var1, int var2) {
      if (var2 < 0 || var2 >= this.R.length) {
         return Items.PAPER;
      } else if (this.f) {
         return this.R[var2];
      } else {
         return var1 ? this.R[var2] : this.o[var2];
      }
   }

   public ItemStack h(boolean var1, int var2) {
      if (var2 >= 0 && var2 < this.m.length) {
         ItemStack var3 = this.f ? this.m[var2] : (var1 ? this.m[var2] : this.U[var2]);
         return c(var3);
      } else {
         return ItemStack.EMPTY;
      }
   }

   public void N(boolean var1, int var2, Item var3) {
      if (var2 >= 0 && var2 < this.R.length && var3 != null && var3 != Items.AIR) {
         this.l(var1, var2, new ItemStack(var3));
      }
   }

   public boolean l(boolean var1, int var2, ItemStack var3) {
      if (var2 >= 0 && var2 < this.R.length && var3 != null) {
         ItemStack var4 = c(var3);
         if (this.f) {
            int var5 = y(this.m[var2], var4) && y(this.U[var2], var4) ? 0 : 1;
            if (var5 == 0) {
               return false;
            }

            this.m[var2] = var4;
            this.U[var2] = c(var4);
            if (!var4.isEmpty()) {
               this.R[var2] = var4.getItem();
               this.o[var2] = var4.getItem();
            }
         } else if (var1) {
            int var6 = !y(this.m[var2], var4) ? 1 : 0;
            if (var6 == 0) {
               return false;
            }

            this.m[var2] = var4;
            if (!var4.isEmpty()) {
               this.R[var2] = var4.getItem();
            }
         } else {
            int var7 = !y(this.U[var2], var4) ? 1 : 0;
            if (var7 == 0) {
               return false;
            }

            this.U[var2] = var4;
            if (!var4.isEmpty()) {
               this.o[var2] = var4.getItem();
            }
         }

         this.a++;
         return true;
      } else {
         return false;
      }
   }

   public boolean C(boolean var1, int var2, ItemStack var3) {
      if (!var3.isEmpty() && !this.s.u()) {
         int var4 = j8.i(var3.getHoverName().getString());
         if (var4 > 0 && j8.P(var3.getHoverName().getString()) != var1) {
            return false;
         }

         if (var4 < 1) {
            var4 = var2 + 1;
         }

         this.s.C(var1 ? 0 : 1, var2, var4);
         return this.l(var1, var4 - 1, var3);
      } else {
         return false;
      }
   }

   public void k(Minecraft var1, boolean var2, BlockPos var3) {
   }

   public void o(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         hk var2 = hk.e();
         this.s.e(var1, this, var2.f(hk.k.y), var2.f(hk.k.w));
         this.H = this.u;
         this.s.f(var1);
      } else {
         if (this.H) {
            this.s.Z(var1);
         }

         this.H = (boolean)0;
      }
   }

   public void H(Minecraft var1, int var2, int var3, int var4) {
   }

   public ItemStack I(ItemEntity var1, ItemStack var2) {
      return this.u && var1 != null ? this.s.I(Minecraft.getInstance(), var1) : var2;
   }

   public ItemStack g(int var1, ItemStack var2) {
      return this.u ? this.s.H(var2) : var2;
   }

   public ItemStack e(ItemStack var1) {
      return this.u ? this.s.H(var1) : var1;
   }

   public ItemStack p(LivingEntity var1, ItemStack var2) {
      return this.u && var1 == Minecraft.getInstance().player ? this.s.H(var2) : var2;
   }

   private int W(Minecraft var1, DispenserMenu var2) {
      hk var3 = hk.e();
      if (this.u && var1 != null && var1.player != null && var2 == var1.player.containerMenu && var2.containerId == var3.v()) {
         for (int var4 = 0; var4 < 2; var4++) {
            BlockPos var5 = this.s.w[var4];
            if (var5 != null && var3.v(var1, var5).equals(var3.v())) {
               return var4;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public void R(Minecraft var1, DispenserMenu var2) {
      hk var3 = hk.e();
      this.s.e(var1, this, var3.f(hk.k.y), var3.f(hk.k.w));
      int var4 = this.W(var1, var2);
      if (var4 >= 0) {
         this.s.A(var2, var4, true);
      }
   }

   public ItemStack G(Minecraft var1, Slot var2) {
      if (var1 != null && var1.player != null && var2.container != var1.player.getInventory() && var1.player.containerMenu instanceof DispenserMenu var3) {
         int var5 = this.W(var1, var3);
         return var5 < 0 ? null : this.s.Q(var1, var3, var5, var2.getContainerSlot());
      } else {
         return null;
      }
   }

   public boolean Q(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (!this.G
         && var1 != null
         && var1.player != null
         && var1.gameMode != null
         && var1.player.containerMenu instanceof DispenserMenu var6
         && var6.containerId == var2
         && var3 >= 0
         && var3 < 9) {
         int var12 = this.W(var1, var6);
         if (var12 < 0) {
            return false;
         }

         int var8 = this.s.k(var1, var6, var12, var3);
         if (var8 == var3) {
            return false;
         }

         this.G = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var8, var4, var5, var1.player);
         } finally {
            this.G = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   private void D() {
      this.s.Z(Minecraft.getInstance());
      this.H = (boolean)0;
   }

   private static boolean y(ItemStack var0, ItemStack var1) {
      int var2 = var0 != null && !var0.isEmpty() ? 0 : 1;
      int var3 = var1 != null && !var1.isEmpty() ? 0 : 1;
      return var2 == 0 && var3 == 0 ? ItemStack.isSameItemSameComponents(var0, var1) : var2 == var3;
   }

   private static ItemStack c(ItemStack var0) {
      return var0 != null && !var0.isEmpty() ? var0.copyWithCount(1) : ItemStack.EMPTY;
   }

   private static String x(String var0) {
      if (var0 == null) {
         return null;
      }

      String var1 = var0.trim();
      if (var1.length() > 24) {
         var1 = var1.substring(0, 24);
      }

      return var1;
   }

   public enum s {
      z("host", "Host"),
      j("viewer", "Viewer");

      private final String l;
      private final String a;

      s(String var3, String var4) {
         this.l = var3;
         this.a = var4;
      }

      public String Y() {
         return this.l;
      }

      public String x() {
         return this.a;
      }
   }
}

package com.larpclient;

import java.util.stream.IntStream;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class p2 {
   private static final p2 E = new p2();
   public Item S = Items.STONE;
   public Item O = Items.COBBLESTONE;
   public Item G = Items.DIAMOND;
   public p2.v z = p2.v.g;
   public boolean U;
   private final ns u = new ns(true);
   private final p2.v[] v = new p2.v[9];
   private boolean l;
   private boolean Q;

   public p2() {
      this.i();
   }

   public static p2 p() {
      return E;
   }

   public void c() {
      this.z = this.z.i();
   }

   public Item H() {
      return switch (this.z) {
         case g -> this.S;
         case F -> this.O;
         case b -> this.G;
      };
   }

   public boolean x(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      this.u.u = this.z.ordinal() + 1;
      if (var2 != null && var1 != null) {
         if (this.u.J() && var2.equals(this.u.h(0)) && this.u.I(var1)) {
            this.u.F(var3, 0, true);
            return true;
         }

         Item var4 = null;
         Item var5 = null;
         Item var6 = null;
         int var7 = 0;

         for (int var8 = 0; var8 < 9; var8++) {
            ItemStack var9 = var3.getSlot(var8).getItem();
            if (!var9.isEmpty()) {
               var7++;
               if (var8 == 4) {
                  var6 = var9.getItem();
               } else if (var4 == null) {
                  var4 = var9.getItem();
               } else if (var9.getItem() != var4 && var5 == null) {
                  var5 = var9.getItem();
               }
            }
         }

         if (var7 == 0) {
            return false;
         }

         if (!this.u.J() || !var2.equals(this.u.h(0)) || var7 == 9) {
            if (var4 != null) {
               this.S = var4;
            }

            if (var5 != null) {
               this.O = var5;
            }

            if (var6 != null) {
               this.G = var6;
            }
         }

         for (int var10 = 0; var10 < 9; var10++) {
            ItemStack var11 = var3.getSlot(var10).getItem();
            if (!var11.isEmpty()) {
               this.X(var10, var10 == 4 ? p2.v.b : (var11.is(this.O) ? p2.v.F : p2.v.g));
            }
         }

         this.u.u(var1, var2, var3, this.S, this.O, this.G, this.e());
         return true;
      } else {
         return false;
      }
   }

   public void i() {
      this.U(new int[]{1, 1, 2, 1, 3, 2, 1, 2, 2});
   }

   public p2.v m(int var1) {
      return var1 >= 0 && var1 < 9 ? this.v[var1] : null;
   }

   public void X(int var1, p2.v var2) {
      if (var1 >= 0 && var1 < 9) {
         this.v[var1] = var2;
      }
   }

   public void D(int var1) {
      this.X(var1, null);
   }

   public int[] e() {
      int[] var1 = new int[9];

      for (int var2 = 0; var2 < 9; var2++) {
         var1[var2] = this.v[var2] == null ? 0 : this.v[var2].ordinal() + 1;
      }

      return var1;
   }

   public void U(int[] var1) {
      if (var1 != null && var1.length == 9) {
         for (int var2 = 0; var2 < 9; var2++) {
            this.v[var2] = var1[var2] >= 1 && var1[var2] <= 3 ? p2.v.values()[var1[var2] - 1] : null;
         }
      } else {
         this.i();
      }
   }

   public int[] h(p2.v var1) {
      return IntStream.range(0, 9).filter(var2 -> var1 != null && this.v[var2] == var1).toArray();
   }

   public void K() {
   }

   public ItemStack v(ItemStack var1, boolean var2) {
      return this.j(var1);
   }

   public String A(ItemStack var1, String var2) {
      return var2;
   }

   public static ItemStack M(Item var0, ItemStack var1, boolean var2) {
      ItemStack var3 = new ItemStack(var0, var1.getCount());

      for (int var4 = 1; var4 <= 3; var4++) {
         ItemStack var5 = E.u.U(var4);
         if (var5.is(var0)) {
            var3 = var5.copyWithCount(var1.getCount());
            break;
         }
      }

      if (var2 && var1.has(DataComponents.CUSTOM_NAME)) {
         var3.set(DataComponents.CUSTOM_NAME, (Component)var1.get(DataComponents.CUSTOM_NAME));
      }

      return var3;
   }

   private boolean m(Minecraft var1, DispenserMenu var2) {
      hk var3 = hk.e();
      BlockPos var4 = this.u.h(0);
      return this.U
         && var4 != null
         && var1 != null
         && var1.player != null
         && var2 == var1.player.containerMenu
         && var2.containerId == var3.v()
         && var3.v(var1, var4).equals(var3.v());
   }

   public void W(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         if (!this.U) {
            if (this.Q) {
               this.u.G(var1);
            }

            this.Q = (boolean)0;
         } else {
            this.Q = (boolean)1;
            this.u.u = this.z.ordinal() + 1;
            this.u.Z(var1);
         }
      } else {
         if (this.Q && var1 != null) {
            this.u.G(var1);
         }

         this.Q = (boolean)0;
      }
   }

   public ItemStack j(ItemStack var1) {
      return this.U && var1 != null && !var1.isEmpty() ? this.u.q(var1) : var1;
   }

   public ItemStack t(ItemStack var1) {
      return this.j(var1);
   }

   public boolean I(ItemStack var1) {
      return (var1.is(Items.WATER_BUCKET) || var1.is(Items.LAVA_BUCKET)) && this.j(var1).is(Items.TOTEM_OF_UNDYING);
   }

   public ItemStack P(LivingEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      return var3 != null && var1 == var3.player ? this.j(var2) : var2;
   }

   public ItemStack C(ItemEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.U && var1 != null && var3 != null && var3.level != null) {
         ItemStack var4 = this.u.H(var3, var1);
         return var4 == var1.getItem() ? var2 : var4;
      } else {
         return var2;
      }
   }

   public ItemStack d(Minecraft var1, Slot var2) {
      return var1 != null
            && var1.player != null
            && var2.container != var1.player.getInventory()
            && var1.player.containerMenu instanceof DispenserMenu var3
            && var2.getContainerSlot() >= 0
            && var2.getContainerSlot() < 9
            && this.m(var1, var3)
         ? this.u.Q(var1, var3, 0, var2.getContainerSlot())
         : null;
   }

   public boolean F(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (!this.l
         && var1 != null
         && var1.player != null
         && var1.gameMode != null
         && var1.player.containerMenu instanceof DispenserMenu var6
         && var6.containerId == var2
         && var3 >= 0
         && var3 < 9
         && this.m(var1, var6)) {
         int var12 = this.u.F(var1, var6, 0, var3);
         int var8 = var5 == ContainerInput.SWAP && var4 >= 0 && var4 < 9 ? var4 : (var5 == ContainerInput.PICKUP && var4 == 0 ? 36 : -1);
         if (var8 >= 0 && this.u.i(var1, var6, var12, var8)) {
            return true;
         }

         if (var12 == var3) {
            return false;
         }

         this.l = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var12, var4, var5, var1.player);
         } finally {
            this.l = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public enum v {
      g("1st"),
      F("2nd"),
      b("Middle");

      public final String z;

      v(String var3) {
         this.z = var3;
      }

      public p2.v i() {
         return switch (this) {
            case g -> b;
            case F -> g;
            case b -> F;
         };
      }
   }
}

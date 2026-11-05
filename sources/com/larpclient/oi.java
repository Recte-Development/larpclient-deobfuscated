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
public final class oi {
   private static final Item[] p = new Item[]{
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
   private static final String q = "Host";
   private static final String n = "Viewer";
   private static final oi V = new oi();
   public Item g;
   public Item j;
   public boolean D;
   private final Item[] y = new Item[9];
   private final Item[] Q = new Item[9];
   private final ItemStack[] K = new ItemStack[9];
   private final ItemStack[] x = new ItemStack[9];
   private boolean S;
   private boolean u;
   private long z;
   private oi.x U;
   private String w;
   private String H;
   private final ey o = new ey();
   private boolean e;
   private boolean t;

   private oi() {
      this.g = Items.APPLE;
      this.j = Items.DIAMOND;
      this.D = (boolean)0;

      for (int var1 = 0; var1 < p.length; var1++) {
         this.y[var1] = p[var1];
         this.Q[var1] = p[var1];
         this.K[var1] = new ItemStack(p[var1]);
         this.x[var1] = new ItemStack(p[var1]);
      }

      this.S = (boolean)1;
      this.u = (boolean)0;
      this.U = oi.x.m;
      this.w = "Host";
      this.H = "Viewer";
   }

   public static oi A() {
      return V;
   }

   public oi.x u() {
      return this.U;
   }

   public String i() {
      return this.U.E();
   }

   public String o() {
      return this.D ? this.i() : "off";
   }

   public String J() {
      return this.U.U();
   }

   public boolean M(oi.x var1) {
      if (var1 != null && this.U != var1) {
         this.U = var1;
         this.g();
         hh.K("Paper", "Visual winner target switched to " + var1.U());
         return true;
      } else {
         return false;
      }
   }

   public boolean W(String var1) {
      if (var1 == null) {
         return false;
      }

      return (boolean)(switch (var1.trim().toLowerCase(Locale.ROOT)) {
         case "host" -> this.M(oi.x.m);
         case "viewer" -> this.M(oi.x.b);
         default -> 0;
      });
   }

   public void M() {
      this.M(this.U == oi.x.m ? oi.x.b : oi.x.m);
   }

   public boolean B() {
      return this.S;
   }

   public long i() {
      return this.z;
   }

   public boolean e(boolean var1) {
      if (this.S == var1) {
         return false;
      }

      this.S = var1;
      if (var1) {
         for (int var2 = 0; var2 < this.y.length; var2++) {
            this.Q[var2] = this.y[var2];
            this.x[var2] = t(this.K[var2]);
         }
      }

      this.z++;
      this.g();
      return true;
   }

   public boolean z() {
      return this.u;
   }

   public void W(boolean var1) {
      this.u = var1;
   }

   public String Z() {
      return this.w;
   }

   public void r(String var1) {
      String var2 = j(var1);
      if (var2 != null) {
         this.w = var2;
      }
   }

   public String Q() {
      return this.H;
   }

   public void L(String var1) {
      String var2 = j(var1);
      if (var2 != null) {
         this.H = var2;
      }
   }

   public Item Q(boolean var1, int var2) {
      if (var2 < 0 || var2 >= this.y.length) {
         return Items.PAPER;
      } else if (this.S) {
         return this.y[var2];
      } else {
         return var1 ? this.y[var2] : this.Q[var2];
      }
   }

   public ItemStack y(boolean var1, int var2) {
      if (var2 >= 0 && var2 < this.K.length) {
         ItemStack var3 = this.S ? this.K[var2] : (var1 ? this.K[var2] : this.x[var2]);
         return t(var3);
      } else {
         return ItemStack.EMPTY;
      }
   }

   public void w(boolean var1, int var2, Item var3) {
      if (var2 >= 0 && var2 < this.y.length && var3 != null && var3 != Items.AIR) {
         this.t(var1, var2, new ItemStack(var3));
      }
   }

   public boolean t(boolean var1, int var2, ItemStack var3) {
      if (var2 >= 0 && var2 < this.y.length && var3 != null) {
         ItemStack var4 = t(var3);
         if (this.S) {
            int var5 = n(this.K[var2], var4) && n(this.x[var2], var4) ? 0 : 1;
            if (var5 == 0) {
               return false;
            }

            this.K[var2] = var4;
            this.x[var2] = t(var4);
            if (!var4.isEmpty()) {
               this.y[var2] = var4.getItem();
               this.Q[var2] = var4.getItem();
            }
         } else if (var1) {
            int var6 = !n(this.K[var2], var4) ? 1 : 0;
            if (var6 == 0) {
               return false;
            }

            this.K[var2] = var4;
            if (!var4.isEmpty()) {
               this.y[var2] = var4.getItem();
            }
         } else {
            int var7 = !n(this.x[var2], var4) ? 1 : 0;
            if (var7 == 0) {
               return false;
            }

            this.x[var2] = var4;
            if (!var4.isEmpty()) {
               this.Q[var2] = var4.getItem();
            }
         }

         this.z++;
         return true;
      } else {
         return false;
      }
   }

   public boolean K(boolean var1, int var2, ItemStack var3) {
      if (!var3.isEmpty() && !this.o.Q()) {
         int var4 = _a.p(var3.getHoverName().getString());
         if (var4 > 0 && _a.R(var3.getHoverName().getString()) != var1) {
            return false;
         }

         if (var4 < 1) {
            var4 = var2 + 1;
         }

         this.o.G(var1 ? 0 : 1, var2, var4);
         return this.t(var1, var4 - 1, var3);
      } else {
         return false;
      }
   }

   public void L(Minecraft var1, boolean var2, BlockPos var3) {
   }

   public void B(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         f9 var2 = f9.W();
         this.o.I(var1, this, var2.R(f9.b.y), var2.R(f9.b.C));
         this.e = this.D;
         this.o.z(var1);
      } else {
         if (this.e) {
            this.o.r(var1);
         }

         this.e = (boolean)0;
      }
   }

   public void W(Minecraft var1, int var2, int var3, int var4) {
   }

   public ItemStack J(ItemEntity var1, ItemStack var2) {
      return this.D && var1 != null ? this.o.y(Minecraft.getInstance(), var1) : var2;
   }

   public ItemStack y(int var1, ItemStack var2) {
      return this.D ? this.o.Z(var2) : var2;
   }

   public ItemStack W(ItemStack var1) {
      return this.D ? this.o.Z(var1) : var1;
   }

   public ItemStack w(LivingEntity var1, ItemStack var2) {
      return this.D && var1 == Minecraft.getInstance().player ? this.o.Z(var2) : var2;
   }

   private int L(Minecraft var1, DispenserMenu var2) {
      f9 var3 = f9.W();
      if (this.D && var1 != null && var1.player != null && var2 == var1.player.containerMenu && var2.containerId == var3.i()) {
         for (int var4 = 0; var4 < 2; var4++) {
            BlockPos var5 = this.o.x[var4];
            if (var5 != null && var3.x(var1, var5).equals(var3.v())) {
               return var4;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public void w(Minecraft var1, DispenserMenu var2) {
      f9 var3 = f9.W();
      this.o.I(var1, this, var3.R(f9.b.y), var3.R(f9.b.C));
      int var4 = this.L(var1, var2);
      if (var4 >= 0) {
         this.o.B(var2, var4, true);
      }
   }

   public ItemStack A(Minecraft var1, Slot var2) {
      if (var1 != null && var1.player != null && var2.container != var1.player.getInventory() && var1.player.containerMenu instanceof DispenserMenu var3) {
         int var5 = this.L(var1, var3);
         return var5 < 0 ? null : this.o.w(var1, var3, var5, var2.getContainerSlot());
      } else {
         return null;
      }
   }

   public boolean h(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (!this.t
         && var1 != null
         && var1.player != null
         && var1.gameMode != null
         && var1.player.containerMenu instanceof DispenserMenu var6
         && var6.containerId == var2
         && var3 >= 0
         && var3 < 9) {
         int var12 = this.L(var1, var6);
         if (var12 < 0) {
            return false;
         }

         int var8 = this.o.k(var1, var6, var12, var3);
         if (var8 == var3) {
            return false;
         }

         this.t = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var8, var4, var5, var1.player);
         } finally {
            this.t = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   private void g() {
      this.o.r(Minecraft.getInstance());
      this.e = (boolean)0;
   }

   private static boolean n(ItemStack var0, ItemStack var1) {
      int var2 = var0 != null && !var0.isEmpty() ? 0 : 1;
      int var3 = var1 != null && !var1.isEmpty() ? 0 : 1;
      return var2 == 0 && var3 == 0 ? ItemStack.isSameItemSameComponents(var0, var1) : var2 == var3;
   }

   private static ItemStack t(ItemStack var0) {
      return var0 != null && !var0.isEmpty() ? var0.copyWithCount(1) : ItemStack.EMPTY;
   }

   private static String j(String var0) {
      if (var0 == null) {
         return null;
      }

      String var1 = var0.trim();
      if (var1.length() > 24) {
         var1 = var1.substring(0, 24);
      }

      return var1;
   }

   public enum x {
      m("host", "Host"),
      b("viewer", "Viewer");

      private final String u;
      private final String T;

      x(String var3, String var4) {
         this.u = var3;
         this.T = var4;
      }

      public String E() {
         return this.u;
      }

      public String U() {
         return this.T;
      }
   }
}

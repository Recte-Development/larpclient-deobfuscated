package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapId;

final class p9 {
   final AbstractContainerMenu R;
   final List<ItemStack> y = new ArrayList<>();
   private final List<ItemStack> l = new ArrayList<>();
   private final Set<Integer> E = new HashSet<>();
   private final oe Z = new oe();
   private ItemStack n = ItemStack.EMPTY;
   private ItemStack g = ItemStack.EMPTY;
   private hv.f K;
   private BiConsumer<Integer, Integer> L;
   private boolean u;
   private long Y;
   private int h;
   private int r;
   boolean J;
   boolean A;
   boolean q;
   boolean d;
   boolean P;
   String B;
   private final long I = System.nanoTime();
   private final ArrayDeque<String> c = new ArrayDeque<>();

   void i(String var1) {
      if (this.c.size() == 48) {
         this.c.removeFirst();
      }

      this.c.addLast((System.nanoTime() - this.I) / 1000000L + "ms " + var1);
   }

   String u() {
      return "sync="
         + this.R.containerId
         + " revision="
         + this.R.getStateId()
         + " pending="
         + this.J
         + " ack["
         + this.Z.E()
         + "] source{"
         + this.w(this.h)
         + "} destination{"
         + this.w(this.r)
         + "} "
         + this.w(-1)
         + " events="
         + this.c;
   }

   p9(AbstractContainerMenu var1) {
      this.R = var1;
   }

   p9 x() {
      p9 var1 = new p9(this.R);
      if (this.d) {
         var1.O(this.y, this.n);
      }

      return var1;
   }

   void O(List<ItemStack> var1, ItemStack var2) {
      if (var1.size() == this.R.slots.size()) {
         this.y.clear();

         for (ItemStack var4 : var1) {
            this.y.add(var4.copy());
         }

         this.n = var2.copy();
         this.d = (boolean)1;
         this.Z.X(this.R, ++this.Y, true, -1, true);
      }
   }

   void C(int var1, ItemStack var2) {
      if (this.d) {
         if (var1 == -1) {
            this.n = var2.copy();
         } else {
            if (var1 < 0 || var1 >= this.y.size()) {
               return;
            }

            this.y.set(var1, var2.copy());
         }

         this.Z.X(this.R, ++this.Y, false, var1, var1 == -1);
      }
   }

   boolean B() {
      if (this.d && je.F(this.n, this.g) && je.F(this.R.getCarried(), this.g)) {
         for (int var1 = 0; var1 < this.y.size(); var1++) {
            if (!je.F(this.y.get(var1), this.R.getSlot(var1).getItem()) || this.E.contains(var1) && !je.F(this.y.get(var1), this.l.get(var1))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   ItemStack J(int var1) {
      return this.l.get(var1);
   }

   boolean g(int var1) {
      return this.E.contains(var1);
   }

   boolean L() {
      return this.d && !this.J && this.B == null && this.n.isEmpty() && this.R.getCarried().isEmpty();
   }

   boolean Q() {
      return this.F(true);
   }

   boolean F(boolean var1) {
      if (this.A) {
         return false;
      }

      if (this.B != null) {
         return false;
      }

      if (!this.J) {
         return true;
      }

      if (this.Z.q(je.F(this.y.get(this.h), this.l.get(this.h)), je.F(this.y.get(this.r), this.l.get(this.r)), je.F(this.n, this.g))
         && (!this.u || this.Z.z())
         && this.B()) {
         if (this.K == null) {
            this.J = (boolean)0;
            return true;
         }

         ItemStack var2 = this.y.get(this.h);
         if (ItemStack.matches(var2, this.R.getSlot(this.h).getItem())
            && ItemStack.matches(this.n, this.R.getCarried())
            && (ItemStack.isSameItemSameComponents(var2, this.n) || jt.G(var2, this.n))
            && this.R.getSlot(this.h).mayPlace(this.n)
            && var2.getCount() + this.n.getCount() <= Math.min(this.R.getSlot(this.h).getMaxStackSize(this.n), this.n.getMaxStackSize())) {
            this.l.set(this.h, var2.copyWithCount(this.K.returned().slotCount()));
            this.g = ItemStack.EMPTY;
            this.K = null;
            this.Z.c(this.R, this.h, this.r, this.Y);
            this.A = this.q = (boolean)1;

            try {
               this.L.accept(this.h, 0);
            } catch (RuntimeException var7) {
               this.B = "surplus click failed; no replay: " + var7.getClass().getSimpleName();
            } finally {
               this.A = this.q = (boolean)0;
               this.L = null;
            }

            return false;
         } else {
            this.B = "unsafe authoritative surplus return; no replay";
            return false;
         }
      } else {
         if (var1 && this.Z.y()) {
            this.B = "batch acknowledgement timeout; no replay " + this.Z.E();
         }

         return false;
      }
   }

   hv.f C(Minecraft var1, int var2, int var3, boolean var4) {
      return this.T(var1, var2, var3, var4).batch();
   }

   hv.f M(Minecraft var1, int var2, int var3) {
      return this.B(var1, var2, var3, false, true).batch();
   }

   p9.i T(Minecraft var1, int var2, int var3, boolean var4) {
      return this.B(var1, var2, var3, var4, false);
   }

   p9.i B(Minecraft var1, int var2, int var3, boolean var4, boolean var5) {
      if (!this.d) {
         return new p9.i(null, "authoritative inventory missing");
      }

      if (var2 >= 0 && var3 >= 0 && var2 < this.y.size() && var3 < this.y.size()) {
         Slot var6 = this.R.getSlot(var2);
         Slot var7 = this.R.getSlot(var3);
         ItemStack var8 = this.y.get(var2);
         ItemStack var9 = var4 ? ItemStack.EMPTY : this.y.get(var3);
         ItemStack var10 = var6.getItem();
         ItemStack var11 = var4 ? ItemStack.EMPTY : var7.getItem();
         int var12 = !var9.isEmpty() && !ItemStack.isSameItemSameComponents(var8, var9) && (!var5 || !jt.G(var8, var9)) ? 0 : 1;
         int var13 = !var11.isEmpty() && !ItemStack.isSameItemSameComponents(var10, var11) && (!var5 || !jt.G(var10, var11)) ? 0 : 1;
         int var14 = var12 != 0 && var13 != 0 ? 1 : 0;
         hv.f var15 = hv.batch(
            var2,
            var3,
            var8.getCount(),
            var9.getCount(),
            (boolean)var14,
            Math.min(var6.getMaxStackSize(var8), var8.getMaxStackSize()),
            Math.min(var7.getMaxStackSize(var8), var8.getMaxStackSize())
         );
         hv.f var16 = hv.batch(
            var2,
            var3,
            var10.getCount(),
            var11.getCount(),
            (boolean)var14,
            Math.min(var6.getMaxStackSize(var10), var10.getMaxStackSize()),
            Math.min(var7.getMaxStackSize(var10), var10.getMaxStackSize())
         );
         String var17 = null;
         if (var2 == var3) {
            var17 = "source equals destination";
         } else if (var14 == 0) {
            var17 = (var12 == 0 ? "server merge mismatch " + d(var8, var9) : "")
               + (var12 == 0 && var13 == 0 ? "; " : "")
               + (var13 == 0 ? "local merge mismatch " + d(var10, var11) : "");
         } else if (var15 == null) {
            var17 = "server count/capacity rejected";
         } else if (var16 == null) {
            var17 = "local count/capacity rejected";
         } else if (!var15.equals(var16)) {
            var17 = "local/server plan divergence server=" + var15 + " local=" + var16;
         } else if (!var6.mayPickup(var1.player)) {
            var17 = "source take permission denied";
         } else if (!var7.mayPlace(var8)) {
            var17 = "destination insert permission denied (server stack)";
         } else if (!var7.mayPlace(var10)) {
            var17 = "destination insert permission denied (local stack)";
         } else if (var15.clicks() == 3 && !var6.mayPlace(var8)) {
            var17 = "source remainder insert permission denied (server stack)";
         } else if (var15.clicks() == 3 && !var6.mayPlace(var10)) {
            var17 = "source remainder insert permission denied (local stack)";
         }

         return new p9.i(var17 == null ? var15 : null, var17 == null ? "ok" : var17);
      } else {
         return new p9.i(null, "source/destination slot unavailable");
      }
   }

   private static String d(ItemStack var0, ItemStack var1) {
      HashSet var2 = new HashSet(var0.getComponents().keySet());
      var2.addAll(var1.getComponents().keySet());
      TreeSet var3 = new TreeSet();

      for (DataComponentType var5 : var2) {
         if (!Objects.equals(var0.getComponents().get(var5), var1.getComponents().get(var5))) {
            var3.add(String.valueOf(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(var5)));
         }
      }

      return "itemEqual="
         + var0.is(var1.getItem())
         + " componentKeys="
         + var3
         + (var3.contains("minecraft:lore") ? " loreA=" + jt.T(var0) + " loreB=" + jt.T(var1) : "");
   }

   String w(int var1) {
      if (var1 == -1) {
         return "cursor server{" + c(this.n, null) + "} local{" + c(this.R.getCarried(), null) + "} server/local " + d(this.n, this.R.getCarried());
      } else if (this.d && var1 >= 0 && var1 < this.y.size()) {
         Slot var2 = this.R.getSlot(var1);
         ItemStack var3 = this.y.get(var1);
         ItemStack var4 = var2.getItem();
         return "slot=" + var1 + " server{" + c(var3, var2) + "} local{" + c(var4, var2) + "} server/local " + d(var3, var4);
      } else {
         return "slot=" + var1 + " unavailable";
      }
   }

   private static String c(ItemStack var0, Slot var1) {
      MapId var2 = (MapId)var0.get(DataComponents.MAP_ID);
      return "map="
         + (var2 == null ? -1 : var2.id())
         + " count="
         + var0.getCount()
         + (var1 == null ? "" : " limit=" + Math.min(var1.getMaxStackSize(var0), var0.getMaxStackSize()));
   }

   String r(int var1, int var2) {
      Slot var3 = this.R.getSlot(var1);
      Slot var4 = this.R.getSlot(var2);
      ItemStack var5 = this.y.get(var1);
      ItemStack var6 = var3.getItem();
      return "effectiveLimits(server/local) source="
         + Math.min(var3.getMaxStackSize(var5), var5.getMaxStackSize())
         + "/"
         + Math.min(var3.getMaxStackSize(var6), var6.getMaxStackSize())
         + " destination="
         + Math.min(var4.getMaxStackSize(var5), var5.getMaxStackSize())
         + "/"
         + Math.min(var4.getMaxStackSize(var6), var6.getMaxStackSize());
   }

   boolean o(Minecraft var1, int var2, int var3, String var4) {
      return this.c(var1, var2, var3, var4, false);
   }

   boolean c(Minecraft var1, int var2, int var3, String var4, boolean var5) {
      if (this.J || this.B != null) {
         return false;
      } else if (var1.gameMode == null) {
         this.B = "interaction manager unavailable; no replay";
         return false;
      } else {
         ClientPacketListener var6 = var1.getConnection();
         PlayerInfo var7 = var6 == null ? null : var6.getPlayerInfo(var1.player.getUUID());
         this.Z.o(var7 == null ? 0 : var7.getLatency());
         hv.f var8 = this.B(var1, var2, var3, false, var5).batch();
         if (this.B() && var8 != null && var1.player.containerMenu == this.R) {
            bn.b("SWAP " + var4 + " batch clicks=" + var8.clicks() + " source=" + var2 + " destination=" + var3);
            return this.P(var2, var3, var8, var4, (var2x, var3x) -> {
               if (var1.player.containerMenu != this.R) {
                  throw new IllegalStateException("handler lost");
               }

               var1.gameMode.handleContainerInput(this.R.containerId, var2x, var3x, ContainerInput.PICKUP, var1.player);
            }, var5);
         } else {
            this.B = "unsafe pre-batch ledger, permission, capacity or vanilla merge";
            return false;
         }
      }
   }

   boolean Z(int var1, int var2, hv.f var3, String var4, BiConsumer<Integer, Integer> var5) {
      return this.P(var1, var2, var3, var4, var5, false);
   }

   boolean P(int var1, int var2, hv.f var3, String var4, BiConsumer<Integer, Integer> var5, boolean var6) {
      if (!this.J && this.B == null && var3 != null && this.B()) {
         ItemStack var7 = this.y.get(var1);
         ItemStack var8 = this.y.get(var2);
         ItemStack var9 = this.R.getSlot(var1).getItem().copy();
         ItemStack var10 = this.R.getSlot(var2).getItem().copy();
         this.u = (boolean)(!var6 || var8.isEmpty() || ItemStack.isSameItemSameComponents(var7, var8) && ItemStack.isSameItemSameComponents(var9, var10)
            ? 0
            : 1);
         if (!this.u
            || (ItemStack.isSameItemSameComponents(var7, var8) || jt.G(var7, var8)) && (ItemStack.isSameItemSameComponents(var9, var10) || jt.G(var9, var10))) {
            if (this.l.isEmpty()) {
               for (ItemStack var12 : this.y) {
                  this.l.add(var12.copy());
               }
            }

            this.E.add(var1);
            this.E.add(var2);
            this.K = this.u && var3.clicks() == 3 ? var3 : null;
            this.L = this.K == null ? null : var5;
            this.g = this.K == null ? ItemStack.EMPTY : var7.copyWithCount(var3.placement().cursorCount());
            this.l.set(var1, var7.copyWithCount(this.K == null ? var3.returned().slotCount() : var3.pickup().slotCount()));
            this.l.set(var2, (var8.isEmpty() ? var7 : var8).copyWithCount(var3.placement().slotCount()));
            this.h = var1;
            this.r = var2;
            this.i("issue " + var4 + " source=" + var1 + " destination=" + var2 + " clicks=" + var3.clicks());
            this.J = this.P = (boolean)1;
            this.Z.c(this.R, var1, var2, this.Y);
            return var3.execute(
                  var7x -> {
                     this.A = (boolean)1;
                     this.q = (boolean)(var7x == (this.u ? 1 : var3.clicks() - 1) ? 1 : 0);

                     try {
                        this.i("click call index=" + var7x + " probe=" + this.q + " revision=" + this.R.getStateId());
                        var5.accept(var7x == 1 ? var2 : var1, var7x == 0 ? var3.pickupButton() : (var7x == 1 ? 1 : 0));
                     } catch (RuntimeException var13) {
                        this.B = "batch click failed; no replay: " + var13.getClass().getSimpleName();
                        return false;
                     } finally {
                        this.A = this.q = (boolean)0;
                     }

                     if (this.u && var7x == 1) {
                        return false;
                     }

                     this.i("click returned index=" + var7x);
                     int var8x = var7x == 2 ? var3.returned().slotCount() : var3.pickup().slotCount();
                     int var9x = var7x == 0 ? var10.getCount() : var3.placement().slotCount();
                     int var10x = var7x == 0 ? var3.pickup().cursorCount() : (var7x == 1 ? var3.placement().cursorCount() : 0);
                     if (ItemStack.matches(this.R.getSlot(var1).getItem(), var9.copyWithCount(var8x))
                        && ItemStack.matches(this.R.getSlot(var2).getItem(), (var10.isEmpty() ? var9 : var10).copyWithCount(var9x))
                        && ItemStack.matches(this.R.getCarried(), var9.copyWithCount(var10x))) {
                        return true;
                     }

                     this.B = "batch prediction diverged after click=" + var7x + "; no replay";
                     return false;
                  }
               )
               || this.u && this.B == null;
         } else {
            this.B = "worth return policy rejected before clicks; no replay";
            return false;
         }
      } else {
         return false;
      }
   }

   record i(hv.f batch, String reason) {
   }
}

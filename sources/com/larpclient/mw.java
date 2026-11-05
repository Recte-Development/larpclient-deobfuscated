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

final class mw {
   final AbstractContainerMenu J;
   final List<ItemStack> Q = new ArrayList<>();
   private final List<ItemStack> S = new ArrayList<>();
   private final Set<Integer> K = new HashSet<>();
   private final e A = new e();
   private ItemStack F = ItemStack.EMPTY;
   private ItemStack r = ItemStack.EMPTY;
   private yk.e V;
   private BiConsumer<Integer, Integer> d;
   private boolean p;
   private long w;
   private int D;
   private int n;
   boolean t;
   boolean f;
   boolean i;
   boolean I;
   boolean h;
   String z;
   private final long o = System.nanoTime();
   private final ArrayDeque<String> g = new ArrayDeque<>();

   void u(String var1) {
      if (this.g.size() == 48) {
         this.g.removeFirst();
      }

      this.g.addLast((System.nanoTime() - this.o) / 1000000L + "ms " + var1);
   }

   String h() {
      return "sync="
         + this.J.containerId
         + " revision="
         + this.J.getStateId()
         + " pending="
         + this.t
         + " ack["
         + this.A.c()
         + "] source{"
         + this.F(this.D)
         + "} destination{"
         + this.F(this.n)
         + "} "
         + this.F(-1)
         + " events="
         + this.g;
   }

   mw(AbstractContainerMenu var1) {
      this.J = var1;
   }

   mw L() {
      mw var1 = new mw(this.J);
      if (this.I) {
         var1.N(this.Q, this.F);
      }

      return var1;
   }

   void N(List<ItemStack> var1, ItemStack var2) {
      if (var1.size() == this.J.slots.size()) {
         this.Q.clear();

         for (ItemStack var4 : var1) {
            this.Q.add(var4.copy());
         }

         this.F = var2.copy();
         this.I = (boolean)1;
         this.A.K(this.J, ++this.w, true, -1, true);
      }
   }

   void o(int var1, ItemStack var2) {
      if (this.I) {
         if (var1 == -1) {
            this.F = var2.copy();
         } else {
            if (var1 < 0 || var1 >= this.Q.size()) {
               return;
            }

            this.Q.set(var1, var2.copy());
         }

         this.A.K(this.J, ++this.w, false, var1, var1 == -1);
      }
   }

   boolean H() {
      if (this.I && mq.T(this.F, this.r) && mq.T(this.J.getCarried(), this.r)) {
         for (int var1 = 0; var1 < this.Q.size(); var1++) {
            if (!mq.T(this.Q.get(var1), this.J.getSlot(var1).getItem()) || this.K.contains(var1) && !mq.T(this.Q.get(var1), this.S.get(var1))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   ItemStack P(int var1) {
      return this.S.get(var1);
   }

   boolean w(int var1) {
      return this.K.contains(var1);
   }

   boolean o() {
      return this.I && !this.t && this.z == null && this.F.isEmpty() && this.J.getCarried().isEmpty();
   }

   boolean g() {
      return this.T(true);
   }

   boolean T(boolean var1) {
      if (this.f) {
         return false;
      }

      if (this.z != null) {
         return false;
      }

      if (!this.t) {
         return true;
      }

      if (this.A.l(mq.T(this.Q.get(this.D), this.S.get(this.D)), mq.T(this.Q.get(this.n), this.S.get(this.n)), mq.T(this.F, this.r))
         && (!this.p || this.A.c())
         && this.H()) {
         if (this.V == null) {
            this.t = (boolean)0;
            return true;
         }

         ItemStack var2 = this.Q.get(this.D);
         if (ItemStack.matches(var2, this.J.getSlot(this.D).getItem())
            && ItemStack.matches(this.F, this.J.getCarried())
            && (ItemStack.isSameItemSameComponents(var2, this.F) || pf.b(var2, this.F))
            && this.J.getSlot(this.D).mayPlace(this.F)
            && var2.getCount() + this.F.getCount() <= Math.min(this.J.getSlot(this.D).getMaxStackSize(this.F), this.F.getMaxStackSize())) {
            this.S.set(this.D, var2.copyWithCount(this.V.returned().slotCount()));
            this.r = ItemStack.EMPTY;
            this.V = null;
            this.A.P(this.J, this.D, this.n, this.w);
            this.f = this.i = (boolean)1;

            try {
               this.d.accept(this.D, 0);
            } catch (RuntimeException var7) {
               this.z = "surplus click failed; no replay: " + var7.getClass().getSimpleName();
            } finally {
               this.f = this.i = (boolean)0;
               this.d = null;
            }

            return false;
         } else {
            this.z = "unsafe authoritative surplus return; no replay";
            return false;
         }
      } else {
         if (var1 && this.A.t()) {
            this.z = "batch acknowledgement timeout; no replay " + this.A.c();
         }

         return false;
      }
   }

   yk.e R(Minecraft var1, int var2, int var3, boolean var4) {
      return this.e(var1, var2, var3, var4).batch();
   }

   yk.e X(Minecraft var1, int var2, int var3) {
      return this.w(var1, var2, var3, false, true).batch();
   }

   mw.q e(Minecraft var1, int var2, int var3, boolean var4) {
      return this.w(var1, var2, var3, var4, false);
   }

   mw.q w(Minecraft var1, int var2, int var3, boolean var4, boolean var5) {
      if (!this.I) {
         return new mw.q(null, "authoritative inventory missing");
      }

      if (var2 >= 0 && var3 >= 0 && var2 < this.Q.size() && var3 < this.Q.size()) {
         Slot var6 = this.J.getSlot(var2);
         Slot var7 = this.J.getSlot(var3);
         ItemStack var8 = this.Q.get(var2);
         ItemStack var9 = var4 ? ItemStack.EMPTY : this.Q.get(var3);
         ItemStack var10 = var6.getItem();
         ItemStack var11 = var4 ? ItemStack.EMPTY : var7.getItem();
         int var12 = !var9.isEmpty() && !ItemStack.isSameItemSameComponents(var8, var9) && (!var5 || !pf.b(var8, var9)) ? 0 : 1;
         int var13 = !var11.isEmpty() && !ItemStack.isSameItemSameComponents(var10, var11) && (!var5 || !pf.b(var10, var11)) ? 0 : 1;
         int var14 = var12 != 0 && var13 != 0 ? 1 : 0;
         yk.e var15 = yk.batch(
            var2,
            var3,
            var8.getCount(),
            var9.getCount(),
            (boolean)var14,
            Math.min(var6.getMaxStackSize(var8), var8.getMaxStackSize()),
            Math.min(var7.getMaxStackSize(var8), var8.getMaxStackSize())
         );
         yk.e var16 = yk.batch(
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
            var17 = (var12 == 0 ? "server merge mismatch " + O(var8, var9) : "")
               + (var12 == 0 && var13 == 0 ? "; " : "")
               + (var13 == 0 ? "local merge mismatch " + O(var10, var11) : "");
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

         return new mw.q(var17 == null ? var15 : null, var17 == null ? "ok" : var17);
      } else {
         return new mw.q(null, "source/destination slot unavailable");
      }
   }

   private static String O(ItemStack var0, ItemStack var1) {
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
         + (var3.contains("minecraft:lore") ? " loreA=" + pf.v(var0) + " loreB=" + pf.v(var1) : "");
   }

   String F(int var1) {
      if (var1 == -1) {
         return "cursor server{" + i(this.F, null) + "} local{" + i(this.J.getCarried(), null) + "} server/local " + O(this.F, this.J.getCarried());
      } else if (this.I && var1 >= 0 && var1 < this.Q.size()) {
         Slot var2 = this.J.getSlot(var1);
         ItemStack var3 = this.Q.get(var1);
         ItemStack var4 = var2.getItem();
         return "slot=" + var1 + " server{" + i(var3, var2) + "} local{" + i(var4, var2) + "} server/local " + O(var3, var4);
      } else {
         return "slot=" + var1 + " unavailable";
      }
   }

   private static String i(ItemStack var0, Slot var1) {
      MapId var2 = (MapId)var0.get(DataComponents.MAP_ID);
      return "map="
         + (var2 == null ? -1 : var2.id())
         + " count="
         + var0.getCount()
         + (var1 == null ? "" : " limit=" + Math.min(var1.getMaxStackSize(var0), var0.getMaxStackSize()));
   }

   String R(int var1, int var2) {
      Slot var3 = this.J.getSlot(var1);
      Slot var4 = this.J.getSlot(var2);
      ItemStack var5 = this.Q.get(var1);
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

   boolean g(Minecraft var1, int var2, int var3, String var4) {
      return this.B(var1, var2, var3, var4, false);
   }

   boolean B(Minecraft var1, int var2, int var3, String var4, boolean var5) {
      if (this.t || this.z != null) {
         return false;
      } else if (var1.gameMode == null) {
         this.z = "interaction manager unavailable; no replay";
         return false;
      } else {
         ClientPacketListener var6 = var1.getConnection();
         PlayerInfo var7 = var6 == null ? null : var6.getPlayerInfo(var1.player.getUUID());
         this.A.f(var7 == null ? 0 : var7.getLatency());
         yk.e var8 = this.w(var1, var2, var3, false, var5).batch();
         if (this.H() && var8 != null && var1.player.containerMenu == this.J) {
            hh.L("SWAP " + var4 + " batch clicks=" + var8.clicks() + " source=" + var2 + " destination=" + var3);
            return this.U(var2, var3, var8, var4, (var2x, var3x) -> {
               if (var1.player.containerMenu != this.J) {
                  throw new IllegalStateException("handler lost");
               }

               var1.gameMode.handleContainerInput(this.J.containerId, var2x, var3x, ContainerInput.PICKUP, var1.player);
            }, var5);
         } else {
            this.z = "unsafe pre-batch ledger, permission, capacity or vanilla merge";
            return false;
         }
      }
   }

   boolean m(int var1, int var2, yk.e var3, String var4, BiConsumer<Integer, Integer> var5) {
      return this.U(var1, var2, var3, var4, var5, false);
   }

   boolean U(int var1, int var2, yk.e var3, String var4, BiConsumer<Integer, Integer> var5, boolean var6) {
      if (!this.t && this.z == null && var3 != null && this.H()) {
         ItemStack var7 = this.Q.get(var1);
         ItemStack var8 = this.Q.get(var2);
         ItemStack var9 = this.J.getSlot(var1).getItem().copy();
         ItemStack var10 = this.J.getSlot(var2).getItem().copy();
         this.p = (boolean)(!var6 || var8.isEmpty() || ItemStack.isSameItemSameComponents(var7, var8) && ItemStack.isSameItemSameComponents(var9, var10)
            ? 0
            : 1);
         if (!this.p
            || (ItemStack.isSameItemSameComponents(var7, var8) || pf.b(var7, var8)) && (ItemStack.isSameItemSameComponents(var9, var10) || pf.b(var9, var10))) {
            if (this.S.isEmpty()) {
               for (ItemStack var12 : this.Q) {
                  this.S.add(var12.copy());
               }
            }

            this.K.add(var1);
            this.K.add(var2);
            this.V = this.p && var3.clicks() == 3 ? var3 : null;
            this.d = this.V == null ? null : var5;
            this.r = this.V == null ? ItemStack.EMPTY : var7.copyWithCount(var3.placement().cursorCount());
            this.S.set(var1, var7.copyWithCount(this.V == null ? var3.returned().slotCount() : var3.pickup().slotCount()));
            this.S.set(var2, (var8.isEmpty() ? var7 : var8).copyWithCount(var3.placement().slotCount()));
            this.D = var1;
            this.n = var2;
            this.u("issue " + var4 + " source=" + var1 + " destination=" + var2 + " clicks=" + var3.clicks());
            this.t = this.h = (boolean)1;
            this.A.P(this.J, var1, var2, this.w);
            return var3.execute(
                  var7x -> {
                     this.f = (boolean)1;
                     this.i = (boolean)(var7x == (this.p ? 1 : var3.clicks() - 1) ? 1 : 0);

                     try {
                        this.u("click call index=" + var7x + " probe=" + this.i + " revision=" + this.J.getStateId());
                        var5.accept(var7x == 1 ? var2 : var1, var7x == 0 ? var3.pickupButton() : (var7x == 1 ? 1 : 0));
                     } catch (RuntimeException var13) {
                        this.z = "batch click failed; no replay: " + var13.getClass().getSimpleName();
                        return false;
                     } finally {
                        this.f = this.i = (boolean)0;
                     }

                     if (this.p && var7x == 1) {
                        return false;
                     }

                     this.u("click returned index=" + var7x);
                     int var8x = var7x == 2 ? var3.returned().slotCount() : var3.pickup().slotCount();
                     int var9x = var7x == 0 ? var10.getCount() : var3.placement().slotCount();
                     int var10x = var7x == 0 ? var3.pickup().cursorCount() : (var7x == 1 ? var3.placement().cursorCount() : 0);
                     if (ItemStack.matches(this.J.getSlot(var1).getItem(), var9.copyWithCount(var8x))
                        && ItemStack.matches(this.J.getSlot(var2).getItem(), (var10.isEmpty() ? var9 : var10).copyWithCount(var9x))
                        && ItemStack.matches(this.J.getCarried(), var9.copyWithCount(var10x))) {
                        return true;
                     }

                     this.z = "batch prediction diverged after click=" + var7x + "; no replay";
                     return false;
                  }
               )
               || this.p && this.z == null;
         } else {
            this.z = "worth return policy rejected before clicks; no replay";
            return false;
         }
      } else {
         return false;
      }
   }

   record q(yk.e batch, String reason) {
   }
}

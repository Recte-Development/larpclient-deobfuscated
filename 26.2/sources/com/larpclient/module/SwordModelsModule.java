package com.larpclient.module;

import com.larpclient.cf;
import com.larpclient.gx;
import com.larpclient.gy;
import com.larpclient.ks;
import com.larpclient.sl;
import com.larpclient.tc;
import com.larpclient.tr;
import com.larpclient.z1;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public final class SwordModelsModule extends Module {
   public final l Y = this.addSetting(
      new l("Model", "First-person sword replacement", "Bayonet", "Bayonet", "Stiletto", "Butterfly", "Luxury Watch", "Ledger Nano S")
   );
   public final l l = this.addSetting(new l("Finish", "Moving galaxy colors on the blade", "Galaxy", "Galaxy", "Violet", "Rose", "Ice", "Steel"));
   public final e m = this.addSetting(new e("Model Scale", "Knife size or watch case size", 1.0, 0.7, 1.3, 0.05));
   public final e c = this.addSetting(new e("Hand X", "Horizontal hand offset", 0.0, -0.2, 0.2, 0.01));
   public final e j = this.addSetting(new e("Hand Y", "Vertical hand offset", 0.0, -0.2, 0.2, 0.01));
   public final e Q = this.addSetting(new e("Hand Z", "Depth of the hand", 0.0, -0.2, 0.15, 0.01));
   public final v X = this.addSetting(new v("Inspect Key", "Inspect main-hand knife or watch, or offhand if main hand is not a sword", 71));
   public final e n = this.addSetting(new e("Inspect Speed", "2 matches the reference showcase tempo", 2.0, 0.5, 2.0, 0.05));
   public final s y = this.addSetting(new s("Idle Motion", "Subtle breathing motion", true));
   public final s b = this.addSetting(new s("Enchantment Glint", "Show enchantment shimmer on the model geometry", true));
   private final s F = this.addSetting(new s("Reset Defaults", "Restore all Sword Skins settings", false));
   private static SwordModelsModule D;
   private final gx[] g = new gx[]{new gx(), new gx()};
   private final z1[] N = new z1[]{new z1(), new z1()};
   private final tr[] w = new tr[]{new tr(), new tr()};
   private final sl[] G = new sl[]{new sl(), new sl()};
   private final ItemStack[] A = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
   private final ItemStack[] R = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
   private final Object[] p = new Object[]{new Object(), new Object()};
   private final Set<String> W = new HashSet<>();
   private int i = -1;

   public SwordModelsModule() {
      super("Sword Models", "Replace held swords with CS:GO knives, a Ledger or a wristwatch", Category.VISUALS, -1);
      D = this;
      this.l.P(() -> !this.M().L());
      this.F.P(() -> false);
      this.X.P(() -> this.M().z());
      this.n.P(() -> this.M().z());
      this.y.P(() -> !this.M().L());
      this.Y.o(var1 -> this.l());
      this.F.o(var1 -> {
         if (var1) {
            for (q var3 : this.getSettings()) {
               var3.u();
            }

            this.l();
         }
      });
   }

   @Override
   public String getDisplayName() {
      return "Sword Skins";
   }

   @Override
   public List<String> getSearchAliases() {
      return List.of("CS:GO Swords", "Knives");
   }

   public static SwordModelsModule N() {
      return D;
   }

   public static boolean V(ItemStack var0) {
      return var0 != null && !var0.isEmpty() && (var0.is(ItemTags.SWORDS) || V(var0) != null);
   }

   private static Identifier V(ItemStack var0) {
      if (var0.is(ItemTags.SWORDS)) {
         return null;
      }

      Identifier var1 = (Identifier)var0.get(DataComponents.ITEM_MODEL);
      if (var1 == null) {
         return null;
      }

      Optional var2 = BuiltInRegistries.ITEM.get(var1);
      return var2.isPresent() && ((Reference)var2.get()).is(ItemTags.SWORDS) ? var1 : null;
   }

   public static boolean F(ItemStack var0) {
      return V(var0) != null;
   }

   public static boolean h(ItemStack var0, ItemStack var1) {
      return V(var0) && V(var1) && (ItemStack.isSameItem(var0, var1) || V(var0) != null || V(var1) != null);
   }

   public Object i(InteractionHand var1, ItemStack var2) {
      int var3 = var1 == InteractionHand.MAIN_HAND ? 0 : 1;
      if (var2 != this.R[var3]) {
         if (!h(this.R[var3], var2)) {
            this.p[var3] = new Object();
         }

         this.R[var3] = var2;
      }

      return this.p[var3];
   }

   public tc M() {
      return tc.A(this.Y.getValue());
   }

   public gx B(InteractionHand var1) {
      return this.g[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   public z1 S(InteractionHand var1) {
      return this.N[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   public tr A(InteractionHand var1) {
      return this.w[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   public sl i(InteractionHand var1) {
      return this.G[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   private void f(int var1) {
      this.g[var1].h();
      this.N[var1].Y();
      this.w[var1].x();
      this.G[var1].E();
   }

   public boolean Q(ItemStack var1) {
      return !this.isEnabled() ? false : this.isOperational() && V(var1) && gy.x(this.M()) != null;
   }

   public int M() {
      return switch ((String)this.l.getValue()) {
         case "Violet" -> 1;
         case "Rose" -> 2;
         case "Ice" -> 3;
         case "Steel" -> 4;
         default -> 0;
      };
   }

   private void l() {
      for (int var1 = 0; var1 < this.g.length; var1++) {
         this.f(var1);
      }

      this.A[0] = this.A[1] = ItemStack.EMPTY;
      this.R[0] = this.R[1] = ItemStack.EMPTY;
      this.i = -1;
   }

   @Override
   public void onEnable() {
      this.l();
      this.W.clear();
   }

   @Override
   public void onDisable() {
      this.l();
   }

   @Override
   public void onTick() {
      LocalPlayer var1 = this.getPlayer();
      if (var1 == null) {
         this.l();
      } else {
         int var2 = var1.getInventory().getSelectedSlot();
         int var13 = this.i;
         int var12 = var2;
         int var10000 = Integer.compare(var12, var13);
         int var3 = ((var10000 | -var10000) >>> 31 ^ 1) == 0 ? 1 : 0;
         boolean var11 = false;
         int var10 = var3;
         var10000 = Integer.signum(var10);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.f(0);
            this.i = var2;
         }

         for (InteractionHand var7 : InteractionHand.values()) {
            int var8 = var7 == InteractionHand.MAIN_HAND ? 0 : 1;
            ItemStack var9 = var1.getItemInHand(var7);
            if (var9 != this.A[var8] && (var3 == 0 || var7 != InteractionHand.MAIN_HAND)) {
               this.h(var7, this.A[var8], var9);
            }

            if (var9 != this.A[var8] && !h(this.A[var8], var9) || !V(var9)) {
               this.f(var8);
            }

            this.A[var8] = var9;
            if (ks.t(var1) == var7) {
               this.g[var8].l();
            }
         }
      }
   }

   private void h(InteractionHand var1, ItemStack var2, ItemStack var3) {
      if (V(var2) && !var3.isEmpty()) {
         Identifier var4 = V(var3);
         if (var4 != null || !V(var3)) {
            String var5 = BuiltInRegistries.ITEM.getKey(var3.getItem()).toString();
            Identifier var6 = (Identifier)var3.get(DataComponents.ITEM_MODEL);
            if (this.W.add(var1 + "|" + var5 + "|" + var6)) {
               if (var4 != null) {
                  com.larpclient.h.A("SwordModels", "{} sword swapped to {} drawn with sword model {}; keeping the skin", var1, var5, var4);
               } else {
                  com.larpclient.h.A(
                     "SwordModels",
                     "{} sword swapped to {} (item model {}, use {}, using {}); not a sword model, vanilla draws it",
                     var1,
                     var5,
                     var6,
                     var3.getUseAnimation(),
                     this.getPlayer().isUsingItem()
                  );
               }
            }
         }
      }
   }

   public void b(InteractionHand var1, ItemStack var2) {
      String var3 = BuiltInRegistries.ITEM.getKey(var2.getItem()).toString();
      if (this.W.add("use|" + var1 + "|" + var3)) {
         com.larpclient.h.A("SwordModels", "{} {} is being used ({}); vanilla draws its use pose instead of {}", var1, var3, var2.getUseAnimation(), this.M().P);
      }
   }

   public void y(int var1) {
      if (this.isEnabled()) {
         Minecraft var2 = getMc();
         int var3 = var1 == this.X.getValue()
               && this.isOperational()
               && var2.player != null
               && var2.gui.screen() == null
               && var2.isWindowActive()
               && !var2.isPaused()
            ? 1
            : 0;
         if (var3 != 0) {
            LocalPlayer var4 = var2.player;
            InteractionHand var5 = V(var4.getMainHandItem()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            if (this.M().z()
               && this.Q(var4.getItemInHand(var5))
               && !ks.M(var4)
               && !var4.isUsingItem()
               && this.B(var5).r(cf.F(), this.n.getValue())
               && this.M() == tc.x) {
               com.larpclient.h.U("SwordModels", "Watch inspect started in {} at speed {}", var5, this.n.getValue());
            }
         }
      }
   }
}

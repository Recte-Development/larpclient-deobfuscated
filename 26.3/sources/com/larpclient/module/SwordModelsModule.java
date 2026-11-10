package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.h8;
import com.larpclient.hz;
import com.larpclient.i7;
import com.larpclient.s_;
import com.larpclient.v6;
import com.larpclient.wd;
import com.larpclient.wq;
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
   public final o Z = this.addSetting(
      new o("Model", "First-person sword replacement", "Bayonet", "Bayonet", "Stiletto", "Butterfly", "Luxury Watch", "Ledger Nano S")
   );
   public final o d = this.addSetting(new o("Finish", "Moving galaxy colors on the blade", "Galaxy", "Galaxy", "Violet", "Rose", "Ice", "Steel"));
   public final b t = this.addSetting(new b("Model Scale", "Knife size or watch case size", 1.0, 0.7, 1.3, 0.05));
   public final b r = this.addSetting(new b("Hand X", "Horizontal hand offset", 0.0, -0.2, 0.2, 0.01));
   public final b x = this.addSetting(new b("Hand Y", "Vertical hand offset", 0.0, -0.2, 0.2, 0.01));
   public final b i = this.addSetting(new b("Hand Z", "Depth of the hand", 0.0, -0.2, 0.15, 0.01));
   public final x H = this.addSetting(new x("Inspect Key", "Inspect main-hand knife or watch, or offhand if main hand is not a sword", 71));
   public final b v = this.addSetting(new b("Inspect Speed", "2 matches the reference showcase tempo", 2.0, 0.5, 2.0, 0.05));
   public final m V = this.addSetting(new m("Idle Motion", "Subtle breathing motion", true));
   public final m j = this.addSetting(new m("Enchantment Glint", "Show enchantment shimmer on the model geometry", true));
   private final m g = this.addSetting(new m("Reset Defaults", "Restore all Sword Skins settings", false));
   private static SwordModelsModule K;
   private final i7[] h = new i7[]{new i7(), new i7()};
   private final wq[] y = new wq[]{new wq(), new wq()};
   private final v6[] o = new v6[]{new v6(), new v6()};
   private final wd[] k = new wd[]{new wd(), new wd()};
   private final ItemStack[] J = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
   private final ItemStack[] N = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
   private final Object[] B = new Object[]{new Object(), new Object()};
   private final Set<String> Q = new HashSet<>();
   private int G = -1;

   public SwordModelsModule() {
      super("Sword Models", "Replace held swords with CS:GO knives, a Ledger or a wristwatch", Category.VISUALS, -1);
      K = this;
      this.d.a(() -> !this.V().d());
      this.g.a(() -> false);
      this.H.a(() -> this.V().D());
      this.v.a(() -> this.V().D());
      this.V.a(() -> !this.V().d());
      this.Z.w(var1 -> this.y());
      this.g.w(var1 -> {
         if (var1) {
            for (r var3 : this.getSettings()) {
               var3.u();
            }

            this.y();
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

   public static SwordModelsModule p() {
      return K;
   }

   public static boolean I(ItemStack var0) {
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

   public static boolean m(ItemStack var0) {
      return V(var0) != null;
   }

   public static boolean J(ItemStack var0, ItemStack var1) {
      return I(var0) && I(var1) && (ItemStack.isSameItem(var0, var1) || V(var0) != null || V(var1) != null);
   }

   public Object i(InteractionHand var1, ItemStack var2) {
      int var3 = var1 == InteractionHand.MAIN_HAND ? 0 : 1;
      if (var2 != this.N[var3]) {
         if (!J(this.N[var3], var2)) {
            this.B[var3] = new Object();
         }

         this.N[var3] = var2;
      }

      return this.B[var3];
   }

   public s_ V() {
      return s_.C(this.Z.getValue());
   }

   public i7 R(InteractionHand var1) {
      return this.h[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   public wq D(InteractionHand var1) {
      return this.y[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   public v6 s(InteractionHand var1) {
      return this.o[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   public wd k(InteractionHand var1) {
      return this.k[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   private void M(int var1) {
      this.h[var1].i();
      this.y[var1].w();
      this.o[var1].O();
      this.k[var1].t();
   }

   public boolean E(ItemStack var1) {
      return !this.isEnabled() ? false : this.isOperational() && I(var1) && com.larpclient.fb.m(this.V()) != null;
   }

   public int M() {
      return switch ((String)this.d.getValue()) {
         case "Violet" -> 1;
         case "Rose" -> 2;
         case "Ice" -> 3;
         case "Steel" -> 4;
         default -> 0;
      };
   }

   private void y() {
      for (int var1 = 0; var1 < this.h.length; var1++) {
         this.M(var1);
      }

      this.J[0] = this.J[1] = ItemStack.EMPTY;
      this.N[0] = this.N[1] = ItemStack.EMPTY;
      this.G = -1;
   }

   @Override
   public void onEnable() {
      this.y();
      this.Q.clear();
   }

   @Override
   public void onDisable() {
      this.y();
   }

   @Override
   public void onTick() {
      LocalPlayer var1 = this.getPlayer();
      if (var1 == null) {
         this.y();
      } else {
         int var2 = var1.getInventory().getSelectedSlot();
         int var11 = this.G;
         int var10 = var2;
         int var10000 = Integer.compare(var10, var11);
         int var3 = ((var10000 | -var10000) >>> 31 ^ 1) == 0 ? 1 : 0;
         boolean var13 = false;
         int var12 = var3;
         var10000 = Integer.signum(var12);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.M(0);
            this.G = var2;
         }

         for (InteractionHand var7 : InteractionHand.values()) {
            int var8 = var7 == InteractionHand.MAIN_HAND ? 0 : 1;
            ItemStack var9 = var1.getItemInHand(var7);
            if (var9 != this.J[var8] && (var3 == 0 || var7 != InteractionHand.MAIN_HAND)) {
               this.I(var7, this.J[var8], var9);
            }

            if (var9 != this.J[var8] && !J(this.J[var8], var9) || !I(var9)) {
               this.M(var8);
            }

            this.J[var8] = var9;
            if (h8.J(var1) == var7) {
               this.h[var8].X();
            }
         }
      }
   }

   private void I(InteractionHand var1, ItemStack var2, ItemStack var3) {
      if (I(var2) && !var3.isEmpty()) {
         Identifier var4 = V(var3);
         if (var4 != null || !I(var3)) {
            String var5 = BuiltInRegistries.ITEM.getKey(var3.getItem()).toString();
            Identifier var6 = (Identifier)var3.get(DataComponents.ITEM_MODEL);
            if (this.Q.add(var1 + "|" + var5 + "|" + var6)) {
               if (var4 != null) {
                  _q.z("SwordModels", "{} sword swapped to {} drawn with sword model {}; keeping the skin", var1, var5, var4);
               } else {
                  _q.z(
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

   public void T(InteractionHand var1, ItemStack var2) {
      String var3 = BuiltInRegistries.ITEM.getKey(var2.getItem()).toString();
      if (this.Q.add("use|" + var1 + "|" + var3)) {
         _q.z("SwordModels", "{} {} is being used ({}); vanilla draws its use pose instead of {}", var1, var3, var2.getUseAnimation(), this.V().N);
      }
   }

   public void V(int var1) {
      if (this.isEnabled()) {
         Minecraft var2 = getMc();
         int var3 = var1 == this.H.getValue()
               && this.isOperational()
               && var2.player != null
               && var2.gui.screen() == null
               && var2.isWindowActive()
               && !var2.isPaused()
            ? 1
            : 0;
         if (var3 != 0) {
            LocalPlayer var4 = var2.player;
            InteractionHand var5 = I(var4.getMainHandItem()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            if (this.V().D()
               && this.E(var4.getItemInHand(var5))
               && !h8.S(var4)
               && !var4.isUsingItem()
               && this.R(var5).t(hz.Q(), this.v.getValue())
               && this.V() == s_.r) {
               _q.c("SwordModels", "Watch inspect started in {} at speed {}", var5, this.v.getValue());
            }
         }
      }
   }
}

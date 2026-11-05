package com.larpclient.module;

import com.larpclient._8;
import com.larpclient.c_;
import com.larpclient.mt;
import com.larpclient.p4;
import com.larpclient.tt;
import com.larpclient.ty;
import com.larpclient.z5;
import com.larpclient.zs;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public final class SwordModelsModule extends Module {
   public final d6 n = this.addSetting(
      new d6("Model", "First-person sword replacement", "Bayonet", "Bayonet", "Stiletto", "Butterfly", "Luxury Watch", "Ledger Nano S")
   );
   public final d6 c = this.addSetting(new d6("Finish", "Moving galaxy colors on the blade", "Galaxy", "Galaxy", "Violet", "Rose", "Ice", "Steel"));
   public final d9 R = this.addSetting(new d9("Model Scale", "Knife size or watch case size", 1.0, 0.7, 1.3, 0.05));
   public final d9 z = this.addSetting(new d9("Hand X", "Horizontal hand offset", 0.0, -0.2, 0.2, 0.01));
   public final d9 s = this.addSetting(new d9("Hand Y", "Vertical hand offset", 0.0, -0.2, 0.2, 0.01));
   public final d9 E = this.addSetting(new d9("Hand Z", "Depth of the hand", 0.0, -0.2, 0.15, 0.01));
   public final dv i = this.addSetting(new dv("Inspect Key", "Inspect main-hand knife or watch, or offhand if main hand is not a sword", 71));
   public final d9 T = this.addSetting(new d9("Inspect Speed", "2 matches the reference showcase tempo", 2.0, 0.5, 2.0, 0.05));
   public final dh S = this.addSetting(new dh("Idle Motion", "Subtle breathing motion", true));
   public final dh j = this.addSetting(new dh("Enchantment Glint", "Show enchantment shimmer on the model geometry", true));
   private final dh d = this.addSetting(new dh("Reset Defaults", "Restore all Sword Skins settings", false));
   private static SwordModelsModule L;
   private final tt[] P = new tt[]{new tt(), new tt()};
   private final c_[] M = new c_[]{new c_(), new c_()};
   private final p4[] v = new p4[]{new p4(), new p4()};
   private final ItemStack[] o = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
   private int V = -1;

   public SwordModelsModule() {
      super("Sword Models", "Replace held swords with CS:GO knives, a Ledger or a wristwatch", Category.VISUALS, -1);
      L = this;
      this.c.H(() -> !this.Y().o());
      this.d.H(() -> false);
      this.i.H(() -> this.Y().v());
      this.T.H(() -> this.Y().v());
      this.S.H(() -> !this.Y().o());
      this.n.h(var1 -> this.s());
      this.d.h(var1 -> {
         if (var1) {
            for (df var3 : this.getSettings()) {
               var3.B();
            }

            this.s();
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

   public static SwordModelsModule n() {
      return L;
   }

   public static boolean q(ItemStack var0) {
      return var0 != null && !var0.isEmpty() && var0.is(ItemTags.SWORDS);
   }

   public z5 Y() {
      return z5.y(this.n.P());
   }

   public tt T(InteractionHand var1) {
      return this.P[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   public c_ J(InteractionHand var1) {
      return this.M[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   public p4 r(InteractionHand var1) {
      return this.v[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   private void q(int var1) {
      this.P[var1].A();
      this.M[var1].T();
      this.v[var1].o();
   }

   public boolean V(ItemStack var1) {
      return !this.isEnabled() ? false : this.isOperational() && q(var1) && zs.U(this.Y()) != null;
   }

   public int t() {
      return switch ((String)this.c.P()) {
         case "Violet" -> 1;
         case "Rose" -> 2;
         case "Ice" -> 3;
         case "Steel" -> 4;
         default -> 0;
      };
   }

   private void s() {
      for (int var1 = 0; var1 < this.P.length; var1++) {
         this.q(var1);
      }

      this.o[0] = this.o[1] = ItemStack.EMPTY;
      this.V = -1;
   }

   @Override
   public void onEnable() {
      this.s();
   }

   @Override
   public void onDisable() {
      this.s();
   }

   @Override
   public void onTick() {
      LocalPlayer var1 = this.getPlayer();
      if (var1 == null) {
         this.s();
      } else {
         int var2 = var1.getInventory().getSelectedSlot();
         int var10 = this.V;
         int var9 = var2;
         int var10000 = Integer.compare(var9, var10);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.q(0);
            this.V = var2;
         }

         for (InteractionHand var6 : InteractionHand.values()) {
            int var7 = var6 == InteractionHand.MAIN_HAND ? 0 : 1;
            ItemStack var8 = var1.getItemInHand(var6);
            if (var8 != this.o[var7] || !q(var8)) {
               this.q(var7);
            }

            this.o[var7] = var8;
            if (_8.M(var1) == var6) {
               this.P[var7].x();
            }
         }
      }
   }

   public void j(int var1) {
      if (this.isEnabled()) {
         Minecraft var2 = getMc();
         int var3 = var1 == this.i.P() && this.isOperational() && var2.player != null && var2.gui.screen() == null && var2.isWindowActive() && !var2.isPaused()
            ? 1
            : 0;
         if (var3 != 0) {
            LocalPlayer var4 = var2.player;
            InteractionHand var5 = q(var4.getMainHandItem()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            if (this.Y().v()
               && this.V(var4.getItemInHand(var5))
               && !_8.Q(var4)
               && !var4.isUsingItem()
               && this.T(var5).S(ty.K(), this.T.P())
               && this.Y() == z5.r) {
               mt.U("SwordModels", "Watch inspect started in {} at speed {}", var5, this.T.P());
            }
         }
      }
   }
}

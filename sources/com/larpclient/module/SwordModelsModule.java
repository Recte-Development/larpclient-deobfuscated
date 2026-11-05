package com.larpclient.module;

import com.larpclient.ea;
import com.larpclient.hp;
import com.larpclient.jv;
import com.larpclient.r4;
import com.larpclient.xh;
import com.larpclient.xt;
import com.larpclient.yi;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public final class SwordModelsModule extends Module {
   public final lp X = this.addSetting(
      new lp("Model", "First-person sword replacement", "Bayonet", "Bayonet", "Stiletto", "Butterfly", "Luxury Watch", "Ledger Nano S")
   );
   public final lp U = this.addSetting(new lp("Finish", "Moving galaxy colors on the blade", "Galaxy", "Galaxy", "Violet", "Rose", "Ice", "Steel"));
   public final lc V = this.addSetting(new lc("Model Scale", "Knife size or watch case size", 1.0, 0.7, 1.3, 0.05));
   public final lc d = this.addSetting(new lc("Hand X", "Horizontal hand offset", 0.0, -0.2, 0.2, 0.01));
   public final lc A = this.addSetting(new lc("Hand Y", "Vertical hand offset", 0.0, -0.2, 0.2, 0.01));
   public final lc q = this.addSetting(new lc("Hand Z", "Depth of the hand", 0.0, -0.2, 0.15, 0.01));
   public final la L = this.addSetting(new la("Inspect Key", "Inspect main-hand knife or watch, or offhand if main hand is not a sword", 71));
   public final lc j = this.addSetting(new lc("Inspect Speed", "2 matches the reference showcase tempo", 2.0, 0.5, 2.0, 0.05));
   public final ln x = this.addSetting(new ln("Idle Motion", "Subtle breathing motion", true));
   public final ln r = this.addSetting(new ln("Enchantment Glint", "Show enchantment shimmer on the model geometry", true));
   private final ln a = this.addSetting(new ln("Reset Defaults", "Restore all Sword Skins settings", false));
   private static SwordModelsModule H;
   private final xh[] I = new xh[]{new xh(), new xh()};
   private final com.larpclient.lk[] Q = new com.larpclient.lk[]{new com.larpclient.lk(), new com.larpclient.lk()};
   private final xt[] P = new xt[]{new xt(), new xt()};
   private final ItemStack[] f = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
   private int m = -1;

   public SwordModelsModule() {
      super("Sword Models", "Replace held swords with CS:GO knives, a Ledger or a wristwatch", Category.VISUALS, -1);
      H = this;
      this.U.x(() -> !this.T().E());
      this.a.x(() -> false);
      this.L.x(() -> this.T().x());
      this.j.x(() -> this.T().x());
      this.x.x(() -> !this.T().E());
      this.X.W(var1 -> this.N());
      this.a.W(var1 -> {
         if (var1) {
            for (l6 var3 : this.getSettings()) {
               var3.x();
            }

            this.N();
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

   public static SwordModelsModule t() {
      return H;
   }

   public static boolean G(ItemStack var0) {
      return var0 != null && !var0.isEmpty() && var0.is(ItemTags.SWORDS);
   }

   public yi T() {
      return yi.F(this.X.getValue());
   }

   public xh T(InteractionHand var1) {
      return this.I[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   public com.larpclient.lk z(InteractionHand var1) {
      return this.Q[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   public xt K(InteractionHand var1) {
      return this.P[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   private void b(int var1) {
      this.I[var1].c();
      this.Q[var1].r();
      this.P[var1].N();
   }

   public boolean p(ItemStack var1) {
      return !this.isEnabled() ? false : this.isOperational() && G(var1) && jv.r(this.T()) != null;
   }

   public int Y() {
      return switch ((String)this.U.getValue()) {
         case "Violet" -> 1;
         case "Rose" -> 2;
         case "Ice" -> 3;
         case "Steel" -> 4;
         default -> 0;
      };
   }

   private void N() {
      for (int var1 = 0; var1 < this.I.length; var1++) {
         this.b(var1);
      }

      this.f[0] = this.f[1] = ItemStack.EMPTY;
      this.m = -1;
   }

   @Override
   public void onEnable() {
      this.N();
   }

   @Override
   public void onDisable() {
      this.N();
   }

   @Override
   public void onTick() {
      LocalPlayer var1 = this.getPlayer();
      if (var1 == null) {
         this.N();
      } else {
         int var2 = var1.getInventory().getSelectedSlot();
         int var10 = this.m;
         int var9 = var2;
         int var10000 = Integer.compare(var9, var10);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.b(0);
            this.m = var2;
         }

         for (InteractionHand var6 : InteractionHand.values()) {
            int var7 = var6 == InteractionHand.MAIN_HAND ? 0 : 1;
            ItemStack var8 = var1.getItemInHand(var6);
            if (var8 != this.f[var7] || !G(var8)) {
               this.b(var7);
            }

            this.f[var7] = var8;
            if (hp.k(var1) == var6) {
               this.I[var7].P();
            }
         }
      }
   }

   public void B(int var1) {
      if (this.isEnabled()) {
         Minecraft var2 = getMc();
         int var3 = var1 == this.L.getValue()
               && this.isOperational()
               && var2.player != null
               && var2.gui.screen() == null
               && var2.isWindowActive()
               && !var2.isPaused()
            ? 1
            : 0;
         if (var3 != 0) {
            LocalPlayer var4 = var2.player;
            InteractionHand var5 = G(var4.getMainHandItem()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            if (this.T().x()
               && this.p(var4.getItemInHand(var5))
               && !hp.o(var4)
               && !var4.isUsingItem()
               && this.T(var5).Y(ea.y(), this.j.getValue())
               && this.T() == yi.m) {
               r4.g("SwordModels", "Watch inspect started in {} at speed {}", var5, this.j.getValue());
            }
         }
      }
   }
}

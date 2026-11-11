package com.larpclient.module.render;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.KeybindSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.model.CustomItemModel;
import com.larpclient.render.model.InspectAnimationBlender;
import com.larpclient.render.model.SwordModels;
import com.larpclient.render.model.WeaponAnimator;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.math.PoseBlender;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.SpinAnimator;
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
   public final ModeSetting model = this.addSetting(
      new ModeSetting("Model", "First-person sword replacement", "Bayonet", "Bayonet", "Stiletto", "Butterfly", "Luxury Watch", "Ledger Nano S")
   );
   public final ModeSetting finish = this.addSetting(
      new ModeSetting("Finish", "Moving galaxy colors on the blade", "Galaxy", "Galaxy", "Violet", "Rose", "Ice", "Steel")
   );
   public final NumberSetting modelScale = this.addSetting(new NumberSetting("Model Scale", "Knife size or watch case size", 1.0, 0.7, 1.3, 0.05));
   public final NumberSetting handX = this.addSetting(new NumberSetting("Hand X", "Horizontal hand offset", 0.0, -0.2, 0.2, 0.01));
   public final NumberSetting handY = this.addSetting(new NumberSetting("Hand Y", "Vertical hand offset", 0.0, -0.2, 0.2, 0.01));
   public final NumberSetting handZ = this.addSetting(new NumberSetting("Hand Z", "Depth of the hand", 0.0, -0.2, 0.15, 0.01));
   public final KeybindSetting inspectKey = this.addSetting(
      new KeybindSetting("Inspect Key", "Inspect main-hand knife or watch, or offhand if main hand is not a sword", 71)
   );
   public final NumberSetting inspectSpeed = this.addSetting(new NumberSetting("Inspect Speed", "2 matches the reference showcase tempo", 2.0, 0.5, 2.0, 0.05));
   public final BooleanSetting idleMotion = this.addSetting(new BooleanSetting("Idle Motion", "Subtle breathing motion", true));
   public final BooleanSetting enchantmentGlint = this.addSetting(
      new BooleanSetting("Enchantment Glint", "Show enchantment shimmer on the model geometry", true)
   );
   private final BooleanSetting resetDefaults = this.addSetting(new BooleanSetting("Reset Defaults", "Restore all Sword Skins settings", false));
   private static SwordModelsModule instance;
   private final WeaponAnimator[] animators = new WeaponAnimator[]{new WeaponAnimator(), new WeaponAnimator()};
   private final PoseBlender[] poseBlenders = new PoseBlender[]{new PoseBlender(), new PoseBlender()};
   private final InspectAnimationBlender[] inspectBlenders = new InspectAnimationBlender[]{new InspectAnimationBlender(), new InspectAnimationBlender()};
   private final SpinAnimator[] spinAnimators = new SpinAnimator[]{new SpinAnimator(), new SpinAnimator()};
   private final ItemStack[] lastStacks = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
   private final ItemStack[] renderedStacks = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
   private final Object[] stackTokens = new Object[]{new Object(), new Object()};
   private final Set<String> loggedKeys = new HashSet<>();
   private int lastSelectedSlot = -1;

   public SwordModelsModule() {
      super("Sword Models", "Replace held swords with CS:GO knives, a Ledger or a wristwatch", Category.VISUALS, -1);
      instance = this;
      this.finish.visibleWhen(() -> !this.getModel().isAccessory());
      this.resetDefaults.visibleWhen(() -> false);
      this.inspectKey.visibleWhen(() -> this.getModel().hasSwingAnimation());
      this.inspectSpeed.visibleWhen(() -> this.getModel().hasSwingAnimation());
      this.idleMotion.visibleWhen(() -> !this.getModel().isAccessory());
      this.model.onChange(var1 -> this.resetAll());
      this.resetDefaults.onChange(var1 -> {
         if (var1) {
            for (Setting var3 : this.getSettings()) {
               var3.reset();
            }

            this.resetAll();
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

   public static SwordModelsModule getInstance() {
      return instance;
   }

   public static boolean isSword(ItemStack var0) {
      return var0 != null && !var0.isEmpty() && (var0.is(ItemTags.SWORDS) || getModel(var0) != null);
   }

   private static Identifier getModel(ItemStack var0) {
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

   public static boolean isSkinnedSword(ItemStack var0) {
      return getModel(var0) != null;
   }

   public static boolean isSameWeapon(ItemStack var0, ItemStack var1) {
      return isSword(var0) && isSword(var1) && (ItemStack.isSameItem(var0, var1) || getModel(var0) != null || getModel(var1) != null);
   }

   public Object getStackToken(InteractionHand var1, ItemStack var2) {
      int var3 = var1 == InteractionHand.MAIN_HAND ? 0 : 1;
      if (var2 != this.renderedStacks[var3]) {
         if (!isSameWeapon(this.renderedStacks[var3], var2)) {
            this.stackTokens[var3] = new Object();
         }

         this.renderedStacks[var3] = var2;
      }

      return this.stackTokens[var3];
   }

   public CustomItemModel getModel() {
      return CustomItemModel.fromDisplayName(this.model.getValue());
   }

   public WeaponAnimator getAnimator(InteractionHand var1) {
      return this.animators[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   public PoseBlender getPoseBlender(InteractionHand var1) {
      return this.poseBlenders[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   public InspectAnimationBlender getInspectBlender(InteractionHand var1) {
      return this.inspectBlenders[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   public SpinAnimator getSpinAnimator(InteractionHand var1) {
      return this.spinAnimators[var1 == InteractionHand.MAIN_HAND ? 0 : 1];
   }

   private void resetSlot(int var1) {
      this.animators[var1].reset();
      this.poseBlenders[var1].reset();
      this.inspectBlenders[var1].reset();
      this.spinAnimators[var1].reset();
   }

   public boolean shouldReplace(ItemStack var1) {
      return !this.isEnabled() ? false : this.isOperational() && isSword(var1) && SwordModels.getModel(this.getModel()) != null;
   }

   public int resetSlot() {
      return switch ((String)this.finish.getValue()) {
         case "Violet" -> 1;
         case "Rose" -> 2;
         case "Ice" -> 3;
         case "Steel" -> 4;
         default -> 0;
      };
   }

   private void resetAll() {
      for (int var1 = 0; var1 < this.animators.length; var1++) {
         this.resetSlot(var1);
      }

      this.lastStacks[0] = this.lastStacks[1] = ItemStack.EMPTY;
      this.renderedStacks[0] = this.renderedStacks[1] = ItemStack.EMPTY;
      this.lastSelectedSlot = -1;
   }

   @Override
   public void onEnable() {
      this.resetAll();
      this.loggedKeys.clear();
   }

   @Override
   public void onDisable() {
      this.resetAll();
   }

   @Override
   public void onTick() {
      LocalPlayer var1 = this.getPlayer();
      if (var1 == null) {
         this.resetAll();
      } else {
         int var2 = var1.getInventory().getSelectedSlot();
         int var11 = this.lastSelectedSlot;
         int var10 = var2;
         int var10000 = Integer.compare(var10, var11);
         int var3 = ((var10000 | -var10000) >>> 31 ^ 1) == 0 ? 1 : 0;
         boolean var13 = false;
         int var12 = var3;
         var10000 = Integer.signum(var12);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.resetSlot(0);
            this.lastSelectedSlot = var2;
         }

         for (InteractionHand var7 : InteractionHand.values()) {
            int var8 = var7 == InteractionHand.MAIN_HAND ? 0 : 1;
            ItemStack var9 = var1.getItemInHand(var7);
            if (var9 != this.lastStacks[var8] && (var3 == 0 || var7 != InteractionHand.MAIN_HAND)) {
               this.isSword(var7, this.lastStacks[var8], var9);
            }

            if (var9 != this.lastStacks[var8] && !isSameWeapon(this.lastStacks[var8], var9) || !isSword(var9)) {
               this.resetSlot(var8);
            }

            this.lastStacks[var8] = var9;
            if (SwingHelper.getSwingHand(var1) == var7) {
               this.animators[var8].beginHold();
            }
         }
      }
   }

   private void isSword(InteractionHand var1, ItemStack var2, ItemStack var3) {
      if (isSword(var2) && !var3.isEmpty()) {
         Identifier var4 = getModel(var3);
         if (var4 != null || !isSword(var3)) {
            String var5 = BuiltInRegistries.ITEM.getKey(var3.getItem()).toString();
            Identifier var6 = (Identifier)var3.get(DataComponents.ITEM_MODEL);
            if (this.loggedKeys.add(var1 + "|" + var5 + "|" + var6)) {
               if (var4 != null) {
                  LarpLog.info("SwordModels", "{} sword swapped to {} drawn with sword model {}; keeping the skin", var1, var5, var4);
               } else {
                  LarpLog.info(
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

   public void logUsePose(InteractionHand var1, ItemStack var2) {
      String var3 = BuiltInRegistries.ITEM.getKey(var2.getItem()).toString();
      if (this.loggedKeys.add("use|" + var1 + "|" + var3)) {
         LarpLog.info(
            "SwordModels",
            "{} {} is being used ({}); vanilla draws its use pose instead of {}",
            var1,
            var3,
            var2.getUseAnimation(),
            this.getModel().displayName
         );
      }
   }

   public void getModel(int var1) {
      if (this.isEnabled()) {
         Minecraft var2 = getMc();
         int var3 = var1 == this.inspectKey.getValue()
               && this.isOperational()
               && var2.player != null
               && var2.gui.screen() == null
               && var2.isWindowActive()
               && !var2.isPaused()
            ? 1
            : 0;
         if (var3 != 0) {
            LocalPlayer var4 = var2.player;
            InteractionHand var5 = isSword(var4.getMainHandItem()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            if (this.getModel().hasSwingAnimation()
               && this.shouldReplace(var4.getItemInHand(var5))
               && !SwingHelper.isSwinging(var4)
               && !var4.isUsingItem()
               && this.getAnimator(var5).startSwing(TickTimer.getTime(), this.inspectSpeed.getValue())
               && this.getModel() == CustomItemModel.lUXURY_WATCH) {
               LarpLog.debug("SwordModels", "Watch inspect started in {} at speed {}", var5, this.inspectSpeed.getValue());
            }
         }
      }
   }
}

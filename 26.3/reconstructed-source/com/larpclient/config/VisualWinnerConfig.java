package com.larpclient.config;

import com.larpclient.util.data.DispenserRankTracker;
import com.larpclient.util.misc.RankParser;
import com.larpclient.util.misc.RigLog;
import com.larpclient.util.world.DispenserRegistry;
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
public final class VisualWinnerConfig {
   private static final Item[] defaultItems = new Item[]{
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
   private static final String defaultHostName = "Host";
   private static final String defaultViewerName = "Viewer";
   private static final VisualWinnerConfig instance = new VisualWinnerConfig();
   public Item hostPrize;
   public Item viewerPrize;
   public boolean enabled;
   private final Item[] hostItems = new Item[9];
   private final Item[] viewerItems = new Item[9];
   private final ItemStack[] hostStacks = new ItemStack[9];
   private final ItemStack[] viewerStacks = new ItemStack[9];
   private boolean linked;
   private boolean hideNames;
   private long revision;
   private VisualWinnerConfig.Target target;
   private String hostName;
   private String viewerName;
   private final DispenserRankTracker session = new DispenserRankTracker();
   private boolean wasEnabled;
   private boolean handlingClick;

   private VisualWinnerConfig() {
      this.hostPrize = Items.APPLE;
      this.viewerPrize = Items.DIAMOND;
      this.enabled = (boolean)0;

      for (int var1 = 0; var1 < defaultItems.length; var1++) {
         this.hostItems[var1] = defaultItems[var1];
         this.viewerItems[var1] = defaultItems[var1];
         this.hostStacks[var1] = new ItemStack(defaultItems[var1]);
         this.viewerStacks[var1] = new ItemStack(defaultItems[var1]);
      }

      this.linked = (boolean)1;
      this.hideNames = (boolean)0;
      this.target = VisualWinnerConfig.Target.HOST;
      this.hostName = "Host";
      this.viewerName = "Viewer";
   }

   public static VisualWinnerConfig getInstance() {
      return instance;
   }

   public VisualWinnerConfig.Target getTarget() {
      return this.target;
   }

   public String getTargetId() {
      return this.target.getId();
   }

   public String getActiveTargetId() {
      return this.enabled ? this.getTargetId() : "off";
   }

   public String isLinked() {
      return this.target.getDisplayName();
   }

   public boolean setTarget(VisualWinnerConfig.Target var1) {
      if (var1 != null && this.target != var1) {
         this.target = var1;
         this.disable();
         RigLog.logMessage("Paper", "Visual winner target switched to " + var1.getDisplayName());
         return true;
      } else {
         return false;
      }
   }

   public boolean setTargetByName(String var1) {
      if (var1 == null) {
         return false;
      }

      return (boolean)(switch (var1.trim().toLowerCase(Locale.ROOT)) {
         case "host" -> this.setTarget(VisualWinnerConfig.Target.HOST);
         case "viewer" -> this.setTarget(VisualWinnerConfig.Target.VIEWER);
         default -> 0;
      });
   }

   public void toggleTarget() {
      this.setTarget(this.target == VisualWinnerConfig.Target.HOST ? VisualWinnerConfig.Target.VIEWER : VisualWinnerConfig.Target.HOST);
   }

   public boolean isLinked() {
      return this.linked;
   }

   public long toggleTarget() {
      return this.revision;
   }

   public boolean setLinked(boolean var1) {
      if (this.linked == var1) {
         return false;
      }

      this.linked = var1;
      if (var1) {
         for (int var2 = 0; var2 < this.hostItems.length; var2++) {
            this.viewerItems[var2] = this.hostItems[var2];
            this.viewerStacks[var2] = copySingle(this.hostStacks[var2]);
         }
      }

      this.revision++;
      this.disable();
      return true;
   }

   public boolean isHideNames() {
      return this.hideNames;
   }

   public void toggleTarget(boolean var1) {
      this.hideNames = var1;
   }

   public String tick() {
      return this.hostName;
   }

   public void setHostName(String var1) {
      String var2 = sanitizeName(var1);
      if (var2 != null) {
         this.hostName = var2;
      }
   }

   public String setTarget() {
      return this.viewerName;
   }

   public void setTargetByName(String var1) {
      String var2 = sanitizeName(var1);
      if (var2 != null) {
         this.viewerName = var2;
      }
   }

   public Item getItem(boolean var1, int var2) {
      if (var2 < 0 || var2 >= this.hostItems.length) {
         return Items.PAPER;
      } else if (this.linked) {
         return this.hostItems[var2];
      } else {
         return var1 ? this.hostItems[var2] : this.viewerItems[var2];
      }
   }

   public ItemStack getStack(boolean var1, int var2) {
      if (var2 >= 0 && var2 < this.hostStacks.length) {
         ItemStack var3 = this.linked ? this.hostStacks[var2] : (var1 ? this.hostStacks[var2] : this.viewerStacks[var2]);
         return copySingle(var3);
      } else {
         return ItemStack.EMPTY;
      }
   }

   public void setItem(boolean var1, int var2, Item var3) {
      if (var2 >= 0 && var2 < this.hostItems.length && var3 != null && var3 != Items.AIR) {
         this.getTarget(var1, var2, new ItemStack(var3));
      }
   }

   public boolean getTarget(boolean var1, int var2, ItemStack var3) {
      if (var2 >= 0 && var2 < this.hostItems.length && var3 != null) {
         ItemStack var4 = copySingle(var3);
         if (this.linked) {
            int var5 = isSameStack(this.hostStacks[var2], var4) && isSameStack(this.viewerStacks[var2], var4) ? 0 : 1;
            if (var5 == 0) {
               return false;
            }

            this.hostStacks[var2] = var4;
            this.viewerStacks[var2] = copySingle(var4);
            if (!var4.isEmpty()) {
               this.hostItems[var2] = var4.getItem();
               this.viewerItems[var2] = var4.getItem();
            }
         } else if (var1) {
            int var6 = !isSameStack(this.hostStacks[var2], var4) ? 1 : 0;
            if (var6 == 0) {
               return false;
            }

            this.hostStacks[var2] = var4;
            if (!var4.isEmpty()) {
               this.hostItems[var2] = var4.getItem();
            }
         } else {
            int var7 = !isSameStack(this.viewerStacks[var2], var4) ? 1 : 0;
            if (var7 == 0) {
               return false;
            }

            this.viewerStacks[var2] = var4;
            if (!var4.isEmpty()) {
               this.viewerItems[var2] = var4.getItem();
            }
         }

         this.revision++;
         return true;
      } else {
         return false;
      }
   }

   public boolean setStackFromName(boolean var1, int var2, ItemStack var3) {
      if (!var3.isEmpty() && !this.session.hasTakenRank()) {
         int var4 = RankParser.parseRank(var3.getHoverName().getString());
         if (var4 > 0 && RankParser.isViewerRank(var3.getHoverName().getString()) != var1) {
            return false;
         }

         if (var4 < 1) {
            var4 = var2 + 1;
         }

         this.session.setSlotRank(var1 ? 0 : 1, var2, var4);
         return this.getTarget(var1, var4 - 1, var3);
      } else {
         return false;
      }
   }

   public void onContainerOpen(Minecraft var1, boolean var2, BlockPos var3) {
   }

   public void tick(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         DispenserRegistry var2 = DispenserRegistry.getInstance();
         this.session
            .scanDispensedItems(
               var1, this, var2.onDispenserUpdate(DispenserRegistry.GameType.PAPER_HOST), var2.onDispenserUpdate(DispenserRegistry.GameType.PAPER_VIEWER)
            );
         this.wasEnabled = this.enabled;
         this.session.refresh(var1);
      } else {
         if (this.wasEnabled) {
            this.session.reset(var1);
         }

         this.wasEnabled = (boolean)0;
      }
   }

   public void onContainerClick(Minecraft var1, int var2, int var3, int var4) {
   }

   public ItemStack getTargetId(ItemEntity var1, ItemStack var2) {
      return this.enabled && var1 != null ? this.session.snapshotMenu(Minecraft.getInstance(), var1) : var2;
   }

   public ItemStack replaceHotbarStack(int var1, ItemStack var2) {
      return this.enabled ? this.session.spoofStack(var2) : var2;
   }

   public ItemStack replaceStack(ItemStack var1) {
      return this.enabled ? this.session.spoofStack(var1) : var1;
   }

   public ItemStack isHideNames(LivingEntity var1, ItemStack var2) {
      return this.enabled && var1 == Minecraft.getInstance().player ? this.session.spoofStack(var2) : var2;
   }

   private int findSessionIndex(Minecraft var1, DispenserMenu var2) {
      DispenserRegistry var3 = DispenserRegistry.getInstance();
      if (this.enabled && var1 != null && var1.player != null && var2 == var1.player.containerMenu && var2.containerId == var3.getActiveKey()) {
         for (int var4 = 0; var4 < 2; var4++) {
            BlockPos var5 = this.session.dispenserPositions[var4];
            if (var5 != null && var3.getActiveKey(var1, var5).equals(var3.getActiveKey())) {
               return var4;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public void setTarget(Minecraft var1, DispenserMenu var2) {
      DispenserRegistry var3 = DispenserRegistry.getInstance();
      this.session
         .scanDispensedItems(
            var1, this, var3.onDispenserUpdate(DispenserRegistry.GameType.PAPER_HOST), var3.onDispenserUpdate(DispenserRegistry.GameType.PAPER_VIEWER)
         );
      int var4 = this.findSessionIndex(var1, var2);
      if (var4 >= 0) {
         this.session.countTrackedItems(var2, var4, true);
      }
   }

   public ItemStack getSlotStack(Minecraft var1, Slot var2) {
      if (var1 != null && var1.player != null && var2.container != var1.player.getInventory() && var1.player.containerMenu instanceof DispenserMenu var3) {
         int var5 = this.findSessionIndex(var1, var3);
         return var5 < 0 ? null : this.session.spoofSlotStack(var1, var3, var5, var2.getContainerSlot());
      } else {
         return null;
      }
   }

   public boolean setLinked(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (!this.handlingClick
         && var1 != null
         && var1.player != null
         && var1.gameMode != null
         && var1.player.containerMenu instanceof DispenserMenu var6
         && var6.containerId == var2
         && var3 >= 0
         && var3 < 9) {
         int var12 = this.findSessionIndex(var1, var6);
         if (var12 < 0) {
            return false;
         }

         int var8 = this.session.hasDispenser(var1, var6, var12, var3);
         if (var8 == var3) {
            return false;
         }

         this.handlingClick = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var8, var4, var5, var1.player);
         } finally {
            this.handlingClick = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   private void disable() {
      this.session.reset(Minecraft.getInstance());
      this.wasEnabled = (boolean)0;
   }

   private static boolean isSameStack(ItemStack var0, ItemStack var1) {
      int var2 = var0 != null && !var0.isEmpty() ? 0 : 1;
      int var3 = var1 != null && !var1.isEmpty() ? 0 : 1;
      return var2 == 0 && var3 == 0 ? ItemStack.isSameItemSameComponents(var0, var1) : var2 == var3;
   }

   private static ItemStack copySingle(ItemStack var0) {
      return var0 != null && !var0.isEmpty() ? var0.copyWithCount(1) : ItemStack.EMPTY;
   }

   private static String sanitizeName(String var0) {
      if (var0 == null) {
         return null;
      }

      String var1 = var0.trim();
      if (var1.length() > 24) {
         var1 = var1.substring(0, 24);
      }

      return var1;
   }

   public enum Target {
      HOST("host", "Host"),
      VIEWER("viewer", "Viewer");

      private final String ID;
      private final String DISPLAYNAME;

      Target(String var3, String var4) {
         this.ID = var3;
         this.DISPLAYNAME = var4;
      }

      public String getId() {
         return this.ID;
      }

      public String getDisplayName() {
         return this.DISPLAYNAME;
      }
   }
}

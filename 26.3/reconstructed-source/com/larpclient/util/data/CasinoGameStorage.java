package com.larpclient.util.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.config.VisualWinnerConfig;
import com.larpclient.module.exploit.BlackjackRigger;
import com.larpclient.module.exploit.CardForce;
import com.larpclient.module.exploit.HotbarForceQueue;
import com.larpclient.module.exploit.HotbarSwapQueue2;
import com.larpclient.module.exploit.OddEvenCardForce;
import com.larpclient.module.misc.CasinoRigManager;
import com.larpclient.util.inventory.DispenserRemap;
import com.larpclient.util.inventory.DispenserSpoofer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.CasinoPaths;
import com.mojang.serialization.JsonOps;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class CasinoGameStorage {
   public static final int sLOT_COLUMNS = 5;
   public static final int sLOT_ROWS = 3;
   private static final String fILE_NAME = "items.json";
   private static final Gson gSON = new GsonBuilder().setPrettyPrinting().create();

   private CasinoGameStorage() {
   }

   private static int[] createEmptySlots(int var0) {
      int[] var1 = new int[var0];
      Arrays.fill(var1, -1);
      return var1;
   }

   public static int[] createEmptyLayout() {
      return createEmptySlots(15);
   }

   public static int[] sanitizeLayout(int[] var0) {
      if (var0 != null && var0.length == 15) {
         int[] var1 = (int[])var0.clone();

         for (int var2 = 0; var2 < var1.length; var2++) {
            if (var1[var2] < 1 || var1[var2] > 9) {
               var1[var2] = -1;
            }
         }

         return var1;
      } else {
         return createEmptyLayout();
      }
   }

   private static Path getFilePath() {
      return CasinoPaths.resolve("items.json");
   }

   public static synchronized void load(CasinoRigManager var0) {
      Path var1 = getFilePath();
      if (Files.exists(var1)) {
         try {
            JsonElement var2 = JsonParser.parseString(Files.readString(var1, StandardCharsets.UTF_8));
            if (var2 == null || !var2.isJsonObject()) {
               throw new IOException("not a JSON object");
            }

            CasinoGameStorage.CasinoGameData var3 = (CasinoGameStorage.CasinoGameData)gSON.fromJson((JsonObject)var2, CasinoGameStorage.CasinoGameData.class);
            if (var3 == null) {
               throw new IOException("empty");
            }

            getFilePath(var0, var3);
         } catch (Exception var4) {
            quarantineUnreadableFile(var1, var4);
         }
      }
   }

   public static synchronized void save(CasinoRigManager var0) {
      CasinoGameStorage.CasinoGameData var1 = new CasinoGameStorage.CasinoGameData();
      capture(var0, var1);
      Path var2 = getFilePath();

      try {
         Files.createDirectories(var2.getParent());
         Path var3 = var2.resolveSibling(var2.getFileName() + ".tmp");
         Files.writeString(var3, gSON.toJson(var1), StandardCharsets.UTF_8);

         try {
            Files.move(var3, var2, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (IOException var5) {
            Files.move(var3, var2, StandardCopyOption.REPLACE_EXISTING);
         }
      } catch (IOException var6) {
         LarpLog.warn("Rig", "Could not save casino game data: {}", var6.getMessage());
      }
   }

   private static void quarantineUnreadableFile(Path var0, Exception var1) {
      LarpLog.warn("Rig", "Casino game data was unreadable ({}); starting from defaults", var1.getMessage());

      try {
         Files.move(var0, var0.resolveSibling(var0.getFileName() + ".bad"), StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException var3) {
      }
   }

   public static void capture(CasinoRigManager var0, CasinoGameStorage.CasinoGameData var1) {
      VisualWinnerConfig var2 = VisualWinnerConfig.getInstance();
      DispenserSpoofer var3 = DispenserSpoofer.getInstance();
      DispenserRemap var4 = DispenserRemap.getInstance();
      var1.linked = var2.isLinked();
      var1.hideNames = var2.isHideNames();
      var1.targetType = var2.getTarget().name();
      var1.hostName = var2.tick();
      var1.targetName = var2.setTarget();
      var1.hostPrize = getItemId(var2.hostPrize);
      var1.viewerPrize = getItemId(var2.viewerPrize);
      var1.hostItems = getItemIds(var2, true);
      var1.viewerItems = getItemIds(var2, false);
      var1.hostStacks = serializeStacks(var2, true);
      var1.viewerStacks = serializeStacks(var2, false);
      var1.firstItem = getItemId(var3.firstItem);
      var1.secondItem = getItemId(var3.secondItem);
      var1.middleItem = getItemId(var3.middleItem);
      var1.slotType = var3.selectedSlotType.name();
      var1.layout = var3.encodeLayout();
      var1.tntItem = getItemId(var4.tntItem);
      var1.paperItem = getItemId(var4.paperItem);
      CardForce var5 = CardForce.getInstance();
      var1.cardTemplateIds = new int[10];

      for (int var6 = 1; var6 <= 10; var6++) {
         var1.cardTemplateIds[var6 - 1] = isConfigurableValue(var6) ? var5.getTemplateIdForValue(var6) : -1;
      }

      var1.blackjackMapIds = BlackjackRigger.getInstance().getMapIds();
      OddEvenCardForce var9 = OddEvenCardForce.getInstance();
      int[] var7 = var9.forceEven();
      var1.oddEvenTemplateIds = new int[var7.length];

      for (int var8 = 0; var8 < var7.length; var8++) {
         var1.oddEvenTemplateIds[var8] = var9.getTemplateIdForValue(var7[var8]);
      }

      var1.parity = var9.getParity();
      var1.hotbarForceIds = readTemplateIds(HotbarForceQueue.getInstance());
      var1.hotbarSwapIds = readTemplateIds(HotbarSwapQueue2.getInstance());
      var1.slotMapping = var0.getSlotMapping();
   }

   public static void getFilePath(CasinoRigManager var0, CasinoGameStorage.CasinoGameData var1) {
      VisualWinnerConfig var2 = VisualWinnerConfig.getInstance();
      DispenserSpoofer var3 = DispenserSpoofer.getInstance();
      DispenserRemap var4 = DispenserRemap.getInstance();
      var2.setHostName(var1.hostName);
      var2.setTargetByName(var1.targetName);
      var2.setTargetByName(var1.targetType == null ? VisualWinnerConfig.Target.HOST.name().toLowerCase() : var1.targetType.toLowerCase());
      var2.toggleTarget(var1.hideNames);
      var2.hostPrize = parseItem(var1.hostPrize, var2.hostPrize);
      var2.viewerPrize = parseItem(var1.viewerPrize, var2.viewerPrize);
      var2.setLinked(var1.linked);
      restoreItems(var2, var1.hostItems, true);
      restoreStacks(var2, var1.hostStacks, true);
      if (!var1.linked) {
         restoreItems(var2, var1.viewerItems, false);
         restoreStacks(var2, var1.viewerStacks, false);
      }

      var3.firstItem = parseItem(var1.firstItem, var3.firstItem);
      var3.secondItem = parseItem(var1.secondItem, var3.secondItem);
      var3.middleItem = parseItem(var1.middleItem, var3.middleItem);
      var3.selectedSlotType = getFilePath(var1.slotType, var3.selectedSlotType);
      var3.loadLayout(var1.layout);
      var4.tntItem = parseItem(var1.tntItem, var4.tntItem);
      var4.paperItem = parseItem(var1.paperItem, var4.paperItem);
      CardForce var5 = CardForce.getInstance();

      for (int var6 = 1; var6 <= 10; var6++) {
         int var7 = isConfigurableValue(var6) && var1.cardTemplateIds != null && var1.cardTemplateIds.length >= var6 ? var1.cardTemplateIds[var6 - 1] : -1;
         var5.setTemplateIdForValue(var6, var7);
      }

      BlackjackRigger.getInstance().setMapIds(var1.blackjackMapIds);
      OddEvenCardForce var9 = OddEvenCardForce.getInstance();
      int[] var10 = var9.forceEven();

      for (int var8 = 0; var8 < var10.length; var8++) {
         var9.setTemplateIdForValue(var10[var8], var1.oddEvenTemplateIds != null && var1.oddEvenTemplateIds.length > var8 ? var1.oddEvenTemplateIds[var8] : -1);
      }

      var9.setParity(var1.parity);
      writeTemplateIds(HotbarForceQueue.getInstance(), var1.hotbarForceIds != null ? var1.hotbarForceIds : var1.cardTemplateIds);
      writeTemplateIds(HotbarSwapQueue2.getInstance(), var1.hotbarSwapIds != null ? var1.hotbarSwapIds : var1.hotbarForceIds);
      var0.setSlotMapping(var1.slotMapping);
   }

   private static boolean isConfigurableValue(int var0) {
      return var0 == 1 || var0 >= 3 && var0 <= 10;
   }

   private static int[] readTemplateIds(CardForce var0) {
      int[] var1 = new int[9];

      for (int var2 = 1; var2 <= 9; var2++) {
         var1[var2 - 1] = var0.getTemplateIdForValue(var2);
      }

      return var1;
   }

   private static void writeTemplateIds(CardForce var0, int[] var1) {
      for (int var2 = 1; var2 <= 9; var2++) {
         var0.setTemplateIdForValue(var2, var1 != null && var1.length >= var2 ? var1[var2 - 1] : -1);
      }
   }

   private static Item parseItem(String var0, Item var1) {
      if (var0 != null && !var0.isBlank()) {
         try {
            return (Item)BuiltInRegistries.ITEM.getValue(Identifier.parse(var0));
         } catch (Exception var3) {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private static String getItemId(Item var0) {
      return BuiltInRegistries.ITEM.getKey(var0).toString();
   }

   private static DispenserSpoofer.SlotType getFilePath(String var0, DispenserSpoofer.SlotType var1) {
      if (var0 == null) {
         return var1;
      }

      try {
         return DispenserSpoofer.SlotType.valueOf(var0);
      } catch (IllegalArgumentException var3) {
         return var1;
      }
   }

   private static void restoreItems(VisualWinnerConfig var0, String[] var1, boolean var2) {
      if (var1 != null && var1.length >= 9) {
         for (int var3 = 0; var3 < 9; var3++) {
            var0.setItem(var2, var3, parseItem(var1[var3], var0.getItem(var2, var3)));
         }
      }
   }

   private static String[] getItemIds(VisualWinnerConfig var0, boolean var1) {
      String[] var2 = new String[9];

      for (int var3 = 0; var3 < 9; var3++) {
         Item var4 = var0.getItem(var1, var3);
         var2[var3] = getItemId(var4 == null ? Items.PAPER : var4);
      }

      return var2;
   }

   private static void restoreStacks(VisualWinnerConfig var0, String[] var1, boolean var2) {
      if (var1 != null && var1.length >= 9) {
         RegistryOps var3 = VanillaRegistries.createWorldLookup().createSerializationContext(JsonOps.INSTANCE);

         for (int var4 = 0; var4 < 9; var4++) {
            if (var1[var4] != null && !var1[var4].isBlank()) {
               try {
                  ItemStack var5 = ItemStack.CODEC.parse(var3, JsonParser.parseString(var1[var4])).result().orElse(ItemStack.EMPTY);
                  if (!var5.isEmpty()) {
                     var0.getTarget(var2, var4, var5);
                  }
               } catch (Exception var6) {
                  LarpLog.warn("Rig", "Could not restore Paper item metadata for slot {}", var4 + 1);
               }
            }
         }
      }
   }

   private static String[] serializeStacks(VisualWinnerConfig var0, boolean var1) {
      RegistryOps var2 = VanillaRegistries.createWorldLookup().createSerializationContext(JsonOps.INSTANCE);
      String[] var3 = new String[9];

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var0.getStack(var1, var4);
         var3[var4] = var5 != null && !var5.isEmpty() ? ItemStack.CODEC.encodeStart(var2, var5).result().<String>map(gSON::toJson).orElse("") : "";
      }

      return var3;
   }

   public static final class CasinoGameData {
      public boolean linked = (boolean)1;
      public boolean hideNames = (boolean)0;
      public String targetType = VisualWinnerConfig.Target.HOST.name();
      public String hostName = "Host";
      public String targetName = "Viewer";
      public String hostPrize = "minecraft:apple";
      public String viewerPrize = "minecraft:diamond";
      public String[] hostItems;
      public String[] viewerItems;
      public String[] hostStacks;
      public String[] viewerStacks;
      public String firstItem = "minecraft:stone";
      public String secondItem = "minecraft:cobblestone";
      public String middleItem = "minecraft:diamond";
      public String slotType = DispenserSpoofer.SlotType.FIRST.name();
      public int[] layout;
      public String tntItem = "minecraft:tnt";
      public String paperItem = "minecraft:paper";
      public int[] cardTemplateIds = CasinoGameStorage.createEmptySlots(10);
      public int[] blackjackMapIds = CasinoGameStorage.createEmptySlots(11);
      public int[] oddEvenTemplateIds = CasinoGameStorage.createEmptySlots(8);
      public int parity = 0;
      public int[] hotbarForceIds = CasinoGameStorage.createEmptySlots(9);
      public int[] hotbarSwapIds = CasinoGameStorage.createEmptySlots(9);
      public int[] slotMapping = CasinoGameStorage.createEmptyLayout();
   }
}

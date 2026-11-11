package com.larpclient.hud;

import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.StaffListModule;
import com.larpclient.module.render.HUDModule;
import com.larpclient.render.overlay.RoundedShapes;
import com.larpclient.render.overlay.SdfText;
import com.larpclient.util.render.DrawUtil;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class HudPanels2 {
   private static final List<String> staffKeywords = List.of(
      "helper", "admin", "owner", "founder", "manager", "curator", "developer", "staff", "куратор", "админ", "хелпер", "помощн", "стаж", "владел", "разраб"
   );
   private static final String smallCapsChars = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡʏᴢ";
   private static final String latinChars = "abcdefghijklmnopqrstuvwyz";
   private static final Pattern colorCodePattern = Pattern.compile("§.?");
   private static final Pattern nonLetterPattern = Pattern.compile("[^\\p{L}]+");
   private static final Map<String, Boolean> staffNameCache = new ConcurrentHashMap<>();
   private static final int inventoryPadding = 5;
   private static final int slotStride = 22;
   private static final int slotSize = 21;
   private static final int inventoryHeaderHeight = 20;
   private static final int inventoryCornerRadius = 7;
   private static final int inventoryTitleSize = 11;
   private static final float itemScale = 1.25F;
   private static final int inventoryWidth = 197;
   private static final int inventoryGridHeight = 65;

   private HudPanels2() {
   }

   public static boolean isStaffName(String var0) {
      if (var0.isBlank()) {
         return false;
      }

      if (staffNameCache.size() > 1024) {
         staffNameCache.clear();
      }

      return staffNameCache.computeIfAbsent(var0, HudPanels2::computeIsStaff);
   }

   private static boolean computeIsStaff(String var0) {
      String var1 = Normalizer.normalize(colorCodePattern.matcher(var0).replaceAll(""), Form.NFKC).toLowerCase(Locale.ROOT);
      StringBuilder var2 = new StringBuilder(var1.length());

      for (int var3 = 0; var3 < var1.length(); var3++) {
         int var4 = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡʏᴢ".indexOf(var1.charAt(var3));
         var2.append(var4 < 0 ? var1.charAt(var3) : "abcdefghijklmnopqrstuvwyz".charAt(var4));
      }

      String var8 = var2.toString();
      if (staffKeywords.stream().anyMatch(var8::contains)) {
         return true;
      }

      for (String var7 : nonLetterPattern.split(var8)) {
         if (var7.endsWith("mod")
            || var7.endsWith("мод")
            || var7.equals("mods")
            || var7.equals("dev")
            || var7.contains("moder") && !var7.contains("modern")
            || var7.contains("модер") && !var7.contains("модерн")) {
            return true;
         }
      }

      return false;
   }

   public static HudPanels2.Panel buildPanel(String var0, boolean var1) {
      Minecraft var2 = Minecraft.getInstance();
      HUDModule var3 = HUDModule.getInstance();
      ArrayList var4 = new ArrayList();
      switch (var0) {
         case "cooldowns":
            if (var2.player != null) {
               HashSet var11 = new HashSet();
               ItemCooldowns var12 = var2.player.getCooldowns();

               for (int var9 = 0; var9 < var2.player.getInventory().getContainerSize(); var9++) {
                  ItemStack var10 = var2.player.getInventory().getItem(var9);
                  if (!var10.isEmpty()
                     && (var3 == null || !var3.cooldownItems.isMode("Combat") || HudPanels2.CombatCooldownItems.items.contains(var10.getItem()))
                     && var12.isOnCooldown(var10)
                     && var11.add(var12.getCooldownGroup(var10))) {
                     var4.add(
                        new HudPanels2.Row(
                           var10.getHoverName().getString(),
                           String.format(Locale.ROOT, "%.1fs", var12.getCooldownPercent(var10, 0.0F) * 3.0F),
                           var10.copy(),
                           null,
                           -1250068
                        )
                     );
                  }
               }
            }

            if (var1 && var4.isEmpty()) {
               var4.add(new HudPanels2.Row("Ender Pearl", "1.8s", new ItemStack(Items.ENDER_PEARL), null, -1250068));
            }

            return listPanel("Cooldowns", "gauge", var4, 23);
         case "staffs":
            for (StaffListModule.StaffEntry var8 : StaffListModule.getStaffEntries()) {
               var4.add(new HudPanels2.Row(var8.name(), var8.status().LABEL, ItemStack.EMPTY, var8.info(), var8.status().COLOR));
            }

            if (var1 && var4.isEmpty()) {
               var4.add(new HudPanels2.Row("Staff", "Online", ItemStack.EMPTY, null, -10551483));
            }

            if (var4.isEmpty()) {
               var4.add(new HudPanels2.Row("No staff online", "", ItemStack.EMPTY, null, -7697772));
            }

            return listPanel("StaffList", "user", var4, 19);
         case "inventory":
            return inventoryPanel(var3);
         default:
            throw new IllegalArgumentException(var0);
      }
   }

   private static int inventoryGridTop(boolean var0) {
      return var0 ? 24 : 5;
   }

   private static HudPanels2.Panel inventoryPanel(HUDModule var0) {
      int var1 = var0 != null && !var0.inventoryHeader.getValue() ? 0 : 1;
      return new HudPanels2.Panel("Inventory", "panels-top-left", 207, inventoryGridTop((boolean)var1) + 65 + 5, List.of());
   }

   private static HudPanels2.Panel listPanel(String var0, String var1, List<HudPanels2.Row> var2, int var3) {
      int var4 = 168;

      for (HudPanels2.Row var6 : var2) {
         var4 = Math.max(var4, Math.min(300, HudPanels.textWidthAtSize(var6.label(), 13.0) + HudPanels.textWidthAtSize(var6.value(), 13.0) + 64));
      }

      return new HudPanels2.Panel(var0, var1, var4, 48 + var2.size() * var3, List.copyOf(var2));
   }

   public static boolean shouldShow(String var0, HudPanels2.Panel var1) {
      return (boolean)(switch (var0) {
         case "cooldowns" -> var1 != null && !var1.rows().isEmpty() ? 1 : 0;
         default -> 1;
      });
   }

   public static void renderFrame(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, String var5, String var6, boolean var7) {
      renderFrameWithHeader(var0, var1, var2, var3, var4, var5, var6, var7 ? 32 : 0, 12, 13);
   }

   private static void renderFrameWithHeader(
      GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, String var5, String var6, int var7, int var8, int var9
   ) {
      DrawUtil.fillRoundedRect(var0, var1, var2, var3, var4, var8, -603321844);
      if (var7 > 0) {
         var0.enableScissor(var1, var2, var1 + var3, var2 + var7);

         try {
            DrawUtil.fillRoundedRect(var0, var1, var2, var3, var4, var8, -267645934);
         } finally {
            var0.disableScissor();
         }
      }

      HudRenderer2.drawGlow(var0, var1, var2, var3, var4, var8);
      HudRenderer2.drawShine(var0, var1, var2, var3, var4, var8);
      if (var7 > 0) {
         int var10 = Math.max(6, var8);
         int var11 = var9 + 1;
         RoundedShapes.fillRounded(var0, var1 + 2, var2 + var7, var3 - 4, 1.0F, 0.5F, 675036230);
         HudPanels.drawText(var0, var5, var1 + var10, SdfText.centerBaselineY(var2, var7, var9), var9, -1250068);
         HudPanels.drawIconOrFallback(var0, var6, var1 + var3 - var10 - var11, var2 + (var7 - var11) / 2, var11, Theme.accent);
      }
   }

   public static void renderPanel(GuiGraphicsExtractor var0, String var1, HudPanels2.Panel var2, int var3, int var4, boolean var5) {
      Minecraft var6 = Minecraft.getInstance();
      HUDModule var7 = HUDModule.getInstance();
      switch (var1) {
         case "inventory":
            int var20 = var7 != null && !var7.inventoryHeader.getValue() ? 0 : 1;
            renderFrameWithHeader(var0, var3, var4, var2.width(), var2.height(), var2.title(), var2.icon(), var20 != 0 ? 20 : 0, 7, 11);
            int var21 = var4 + inventoryGridTop((boolean)var20);
            float var22 = 0.5F;

            for (int var23 = 0; var23 < 27; var23++) {
               int var24 = var3 + 5 + var23 % 9 * 22;
               int var25 = var21 + var23 / 9 * 22;
               ItemStack var16 = var6.player == null ? ItemStack.EMPTY : var6.player.getInventory().getItem(var23 + 9);
               if (var5 && var16.isEmpty() && var23 < 3) {
                  var16 = new ItemStack(var23 == 0 ? Items.ENDER_PEARL : (var23 == 1 ? Items.DIAMOND_SWORD : Items.GOLDEN_APPLE));
               }

               DrawUtil.fillRoundedRect(var0, var24, var25, 21.0F, 21.0F, 3.0F, var16.isEmpty() ? 452984831 : 788529151);
               if (!var16.isEmpty()) {
                  var0.pose().pushMatrix();

                  try {
                     var0.pose().translate(var24 + var22, var25 + var22);
                     var0.pose().scale(1.25F, 1.25F);
                     var0.item(var16, 0, 0);
                     var0.itemDecorations(var6.font, var16, 0, 0);
                  } finally {
                     var0.pose().popMatrix();
                  }
               }
            }
            break;
         default:
            renderFrame(var0, var3, var4, var2.width(), var2.height(), var2.title(), var2.icon(), true);
            int var10 = var1.equals("staffs") ? 19 : 23;
            int var11 = var4 + 39;

            for (HudPanels2.Row var13 : var2.rows()) {
               int var14 = var3 + 12;
               if (!var13.item().isEmpty()) {
                  var0.item(var13.item(), var14, var11 + (var10 - 16) / 2);
                  var14 += 22;
               } else if (var13.player() != null && StaffListModule.shouldShowHeads()) {
                  PlayerFaceExtractor.extractRenderState(var0, var13.player().getSkin(), var14, var11 + (var10 - 15) / 2, 15);
                  var14 += 21;
               }

               double var15 = SdfText.centerBaselineY(var11, var10, 13.0);
               int var17 = var13.value().isEmpty() ? 0 : HudPanels.textWidthAtSize(var13.value(), 13.0) + 8;
               HudPanels.drawText(
                  var0,
                  HudPanels.truncate(var13.label(), var3 + var2.width() - 12 - var17 - var14, 13.0),
                  var14,
                  var15,
                  13.0,
                  var13.value().isEmpty() ? var13.tint() : -1250068
               );
               HudPanels.drawText(var0, var13.value(), var3 + var2.width() - 12 - HudPanels.textWidthAtSize(var13.value(), 13.0), var15, 13.0, var13.tint());
               var11 += var10;
            }
      }
   }

   private static final class CombatCooldownItems {
      static final Set<Item> items = Set.of(Items.ENDER_PEARL, Items.CHORUS_FRUIT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.SHIELD);
   }

   public record Panel(String title, String icon, int width, int height, List<HudPanels2.Row> rows) {
   }

   public record Row(String label, String value, ItemStack item, PlayerInfo player, int tint) {
   }
}

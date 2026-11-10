package com.larpclient;

import com.larpclient.module.HUDModule;
import com.larpclient.module.StaffListModule;
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

public final class nv {
   private static final List<String> k = List.of(
      "helper", "admin", "owner", "founder", "manager", "curator", "developer", "staff", "куратор", "админ", "хелпер", "помощн", "стаж", "владел", "разраб"
   );
   private static final String h = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡʏᴢ";
   private static final String p = "abcdefghijklmnopqrstuvwyz";
   private static final Pattern z = Pattern.compile("§.?");
   private static final Pattern O = Pattern.compile("[^\\p{L}]+");
   private static final Map<String, Boolean> E = new ConcurrentHashMap<>();
   private static final int P = 5;
   private static final int l = 22;
   private static final int b = 21;
   private static final int A = 20;
   private static final int m = 7;
   private static final int J = 11;
   private static final float o = 1.25F;
   private static final int r = 197;
   private static final int d = 65;

   private nv() {
   }

   public static boolean i(String var0) {
      if (var0.isBlank()) {
         return false;
      }

      if (E.size() > 1024) {
         E.clear();
      }

      return E.computeIfAbsent(var0, nv::Z);
   }

   private static boolean Z(String var0) {
      String var1 = Normalizer.normalize(z.matcher(var0).replaceAll(""), Form.NFKC).toLowerCase(Locale.ROOT);
      StringBuilder var2 = new StringBuilder(var1.length());

      for (int var3 = 0; var3 < var1.length(); var3++) {
         int var4 = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡʏᴢ".indexOf(var1.charAt(var3));
         var2.append(var4 < 0 ? var1.charAt(var3) : "abcdefghijklmnopqrstuvwyz".charAt(var4));
      }

      String var8 = var2.toString();
      if (k.stream().anyMatch(var8::contains)) {
         return true;
      }

      for (String var7 : O.split(var8)) {
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

   public static nv.b E(String var0, boolean var1) {
      Minecraft var2 = Minecraft.getInstance();
      HUDModule var3 = HUDModule.w();
      ArrayList var4 = new ArrayList();
      switch (var0) {
         case "cooldowns":
            if (var2.player != null) {
               HashSet var11 = new HashSet();
               ItemCooldowns var12 = var2.player.getCooldowns();

               for (int var9 = 0; var9 < var2.player.getInventory().getContainerSize(); var9++) {
                  ItemStack var10 = var2.player.getInventory().getItem(var9);
                  if (!var10.isEmpty()
                     && (var3 == null || !var3.P.w("Combat") || nv.a.f.contains(var10.getItem()))
                     && var12.isOnCooldown(var10)
                     && var11.add(var12.getCooldownGroup(var10))) {
                     var4.add(
                        new nv.h(
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
               var4.add(new nv.h("Ender Pearl", "1.8s", new ItemStack(Items.ENDER_PEARL), null, -1250068));
            }

            return n("Cooldowns", "gauge", var4, 23);
         case "staffs":
            for (StaffListModule.o var8 : StaffListModule.F()) {
               var4.add(new nv.h(var8.name(), var8.status().d, ItemStack.EMPTY, var8.info(), var8.status().B));
            }

            if (var1 && var4.isEmpty()) {
               var4.add(new nv.h("Staff", "Online", ItemStack.EMPTY, null, -10551483));
            }

            if (var4.isEmpty()) {
               var4.add(new nv.h("No staff online", "", ItemStack.EMPTY, null, -7697772));
            }

            return n("StaffList", "user", var4, 19);
         case "inventory":
            return j(var3);
         default:
            throw new IllegalArgumentException(var0);
      }
   }

   private static int H(boolean var0) {
      return var0 ? 24 : 5;
   }

   private static nv.b j(HUDModule var0) {
      int var1 = var0 != null && !var0.R.getValue() ? 0 : 1;
      return new nv.b("Inventory", "panels-top-left", 207, H((boolean)var1) + 65 + 5, List.of());
   }

   private static nv.b n(String var0, String var1, List<nv.h> var2, int var3) {
      int var4 = 168;

      for (nv.h var6 : var2) {
         var4 = Math.max(var4, Math.min(300, i5.l(var6.label(), 13.0) + i5.l(var6.value(), 13.0) + 64));
      }

      return new nv.b(var0, var1, var4, 48 + var2.size() * var3, List.copyOf(var2));
   }

   public static boolean f(String var0, nv.b var1) {
      return (boolean)(switch (var0) {
         case "cooldowns" -> var1 != null && !var1.rows().isEmpty() ? 1 : 0;
         default -> 1;
      });
   }

   public static void J(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, String var5, String var6, boolean var7) {
      X(var0, var1, var2, var3, var4, var5, var6, var7 ? 32 : 0, 12, 13);
   }

   private static void X(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, String var5, String var6, int var7, int var8, int var9) {
      h5.q(var0, var1, var2, var3, var4, var8, -603321844);
      if (var7 > 0) {
         var0.enableScissor(var1, var2, var1 + var3, var2 + var7);

         try {
            h5.q(var0, var1, var2, var3, var4, var8, -267645934);
         } finally {
            var0.disableScissor();
         }
      }

      nc.X(var0, var1, var2, var3, var4, var8);
      nc.e(var0, var1, var2, var3, var4, var8);
      if (var7 > 0) {
         int var10 = Math.max(6, var8);
         int var11 = var9 + 1;
         w1.a(var0, var1 + 2, var2 + var7, var3 - 4, 1.0F, 0.5F, 675036230);
         i5.i(var0, var5, var1 + var10, n8.b(var2, var7, var9), var9, -1250068);
         i5.t(var0, var6, var1 + var3 - var10 - var11, var2 + (var7 - var11) / 2, var11, c.y);
      }
   }

   public static void K(GuiGraphicsExtractor var0, String var1, nv.b var2, int var3, int var4, boolean var5) {
      Minecraft var6 = Minecraft.getInstance();
      HUDModule var7 = HUDModule.w();
      switch (var1) {
         case "inventory":
            int var20 = var7 != null && !var7.R.getValue() ? 0 : 1;
            X(var0, var3, var4, var2.width(), var2.height(), var2.title(), var2.icon(), var20 != 0 ? 20 : 0, 7, 11);
            int var21 = var4 + H((boolean)var20);
            float var22 = 0.5F;

            for (int var23 = 0; var23 < 27; var23++) {
               int var24 = var3 + 5 + var23 % 9 * 22;
               int var25 = var21 + var23 / 9 * 22;
               ItemStack var16 = var6.player == null ? ItemStack.EMPTY : var6.player.getInventory().getItem(var23 + 9);
               if (var5 && var16.isEmpty() && var23 < 3) {
                  var16 = new ItemStack(var23 == 0 ? Items.ENDER_PEARL : (var23 == 1 ? Items.DIAMOND_SWORD : Items.GOLDEN_APPLE));
               }

               h5.q(var0, var24, var25, 21.0F, 21.0F, 3.0F, var16.isEmpty() ? 452984831 : 788529151);
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
            J(var0, var3, var4, var2.width(), var2.height(), var2.title(), var2.icon(), true);
            int var10 = var1.equals("staffs") ? 19 : 23;
            int var11 = var4 + 39;

            for (nv.h var13 : var2.rows()) {
               int var14 = var3 + 12;
               if (!var13.item().isEmpty()) {
                  var0.item(var13.item(), var14, var11 + (var10 - 16) / 2);
                  var14 += 22;
               } else if (var13.player() != null && StaffListModule.D()) {
                  PlayerFaceExtractor.extractRenderState(var0, var13.player().getSkin(), var14, var11 + (var10 - 15) / 2, 15);
                  var14 += 21;
               }

               double var15 = n8.b(var11, var10, 13.0);
               int var17 = var13.value().isEmpty() ? 0 : i5.l(var13.value(), 13.0) + 8;
               i5.i(
                  var0,
                  i5.I(var13.label(), var3 + var2.width() - 12 - var17 - var14, 13.0),
                  var14,
                  var15,
                  13.0,
                  var13.value().isEmpty() ? var13.tint() : -1250068
               );
               i5.i(var0, var13.value(), var3 + var2.width() - 12 - i5.l(var13.value(), 13.0), var15, 13.0, var13.tint());
               var11 += var10;
            }
      }
   }

   private static final class a {
      static final Set<Item> f = Set.of(Items.ENDER_PEARL, Items.CHORUS_FRUIT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.SHIELD);
   }

   public record b(String title, String icon, int width, int height, List<nv.h> rows) {
   }

   public record h(String label, String value, ItemStack item, PlayerInfo player, int tint) {
   }
}

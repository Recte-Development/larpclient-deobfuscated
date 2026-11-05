package com.larpclient;

import com.larpclient.module.HUDModule;
import com.larpclient.module.MCFriendModule;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class e0 {
   private static final List<String> E = List.of(
      "helper", "admin", "owner", "founder", "manager", "curator", "developer", "staff", "куратор", "админ", "хелпер", "помощн", "стаж", "владел", "разраб"
   );
   private static final String v = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡʏᴢ";
   private static final String H = "abcdefghijklmnopqrstuvwyz";
   private static final Pattern C = Pattern.compile("§.?");
   private static final Pattern P = Pattern.compile("[^\\p{L}]+");
   private static final Map<String, Boolean> O = new ConcurrentHashMap<>();

   private e0() {
   }

   public static boolean w(String var0) {
      if (var0.isBlank()) {
         return false;
      }

      if (O.size() > 1024) {
         O.clear();
      }

      return O.computeIfAbsent(var0, e0::n);
   }

   private static boolean n(String var0) {
      String var1 = Normalizer.normalize(C.matcher(var0).replaceAll(""), Form.NFKC).toLowerCase(Locale.ROOT);
      StringBuilder var2 = new StringBuilder(var1.length());

      for (int var3 = 0; var3 < var1.length(); var3++) {
         int var4 = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡʏᴢ".indexOf(var1.charAt(var3));
         var2.append(var4 < 0 ? var1.charAt(var3) : "abcdefghijklmnopqrstuvwyz".charAt(var4));
      }

      String var8 = var2.toString();
      if (E.stream().anyMatch(var8::contains)) {
         return true;
      }

      for (String var7 : P.split(var8)) {
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

   public static double[] c(double var0, double var2, double var4, double var6) {
      double var8 = Math.hypot(var0, var2);
      if (var8 > var6) {
         return null;
      }

      double var10 = Math.atan2(var2, var0) - Math.toRadians(var4 + 180.0);
      return new double[]{42.0 + var8 / var6 * 24.0 * Math.cos(var10), 42.0 + var8 / var6 * 24.0 * Math.sin(var10)};
   }

   public static e0.o t(String var0, boolean var1) {
      Minecraft var2 = Minecraft.getInstance();
      HUDModule var3 = HUDModule.U();
      ArrayList var4 = new ArrayList();
      switch (var0) {
         case "cooldowns":
            if (var2.player != null) {
               HashSet var11 = new HashSet();
               ItemCooldowns var12 = var2.player.getCooldowns();

               for (int var9 = 0; var9 < var2.player.getInventory().getContainerSize(); var9++) {
                  ItemStack var10 = var2.player.getInventory().getItem(var9);
                  if (!var10.isEmpty()
                     && (var3 == null || !var3.b.S("Combat") || e0.j.n.contains(var10.getItem()))
                     && var12.isOnCooldown(var10)
                     && var11.add(var12.getCooldownGroup(var10))) {
                     var4.add(
                        new e0.k(
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
               var4.add(new e0.k("Ender Pearl", "1.8s", new ItemStack(Items.ENDER_PEARL), null, -1250068));
            }

            return n("Cooldowns", "gauge", var4, 23);
         case "staffs":
            for (StaffListModule.l var8 : StaffListModule.Q()) {
               var4.add(new e0.k(var8.name(), var8.status().O, ItemStack.EMPTY, var8.info(), var8.status().e));
            }

            if (var1 && var4.isEmpty()) {
               var4.add(new e0.k("Staff", "Online", ItemStack.EMPTY, null, -10551483));
            }

            if (var4.isEmpty()) {
               var4.add(new e0.k("No staff online", "", ItemStack.EMPTY, null, -7697772));
            }

            return n("StaffList", "user", var4, 19);
         case "inventory":
            return new e0.o("Inventory", "panels-top-left", 250, var3 != null && !var3.M.P() ? 90 : 120, List.of());
         case "radar":
            return new e0.o("Radar", "crosshair", 84, 84, List.of());
         default:
            throw new IllegalArgumentException(var0);
      }
   }

   private static e0.o n(String var0, String var1, List<e0.k> var2, int var3) {
      int var4 = 168;

      for (e0.k var6 : var2) {
         var4 = Math.max(var4, Math.min(300, we.V(var6.label(), 13.0) + we.V(var6.value(), 13.0) + 64));
      }

      return new e0.o(var0, var1, var4, 48 + var2.size() * var3, List.copyOf(var2));
   }

   public static boolean v(String var0, e0.o var1) {
      return (boolean)(switch (var0) {
         case "cooldowns" -> var1 != null && !var1.rows().isEmpty() ? 1 : 0;
         default -> 1;
      });
   }

   public static void l(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, String var5, String var6, boolean var7) {
      c7.n(var0, var1, var2, var3, var4, 12.0F, -603321844);
      if (var7) {
         var0.enableScissor(var1, var2, var1 + var3, var2 + 32);

         try {
            c7.n(var0, var1, var2, var3, var4, 12.0F, -267645934);
         } finally {
            var0.disableScissor();
         }
      }

      mn.b(var0, var1, var2, var3, var4, 12.0F);
      mn.X(var0, var1, var2, var3, var4, 12);
      if (var7) {
         tl.g(var0, var1 + 2, var2 + 32, var3 - 4, 1.0F, 0.5F, 675036230);
         we.F(var0, var5, var1 + 12, ew.N(var2, 32.0, 13.0), 13.0, -1250068);
         we.C(var0, var6, var1 + var3 - 26, var2 + 9, 14, f.C);
      }
   }

   public static void W(GuiGraphicsExtractor var0, String var1, e0.o var2, int var3, int var4, boolean var5) {
      Minecraft var6 = Minecraft.getInstance();
      HUDModule var7 = HUDModule.U();
      switch (var1) {
         case "inventory":
            int var19 = var7 != null && !var7.M.P() ? 0 : 1;
            l(var0, var3, var4, var2.width(), var2.height(), var2.title(), var2.icon(), (boolean)var19);
            int var20 = var4 + (var19 != 0 ? 37 : 8);

            for (int var22 = 0; var22 < 27; var22++) {
               int var24 = var3 + 8 + var22 % 9 * 26;
               int var26 = var20 + var22 / 9 * 26;
               c7.n(var0, var24, var26, 25.0F, 25.0F, 2.0F, 676352089);
               ItemStack var28 = var6.player == null ? ItemStack.EMPTY : var6.player.getInventory().getItem(var22 + 9);
               if (var5 && var28.isEmpty() && var22 < 3) {
                  var28 = new ItemStack(var22 == 0 ? Items.ENDER_PEARL : (var22 == 1 ? Items.DIAMOND_SWORD : Items.GOLDEN_APPLE));
               }

               if (!var28.isEmpty()) {
                  var0.item(var28, var24 + 5, var26 + 5);
                  var0.itemDecorations(var6.font, var28, var24 + 5, var26 + 5);
               }
            }
            break;
         case "radar":
            l(var0, var3, var4, 84, 84, "", "", false);
            c7.s(var0, var3 + 42, var4 + 14, 1, 56, _3.A(f.C, 0.23));
            c7.s(var0, var3 + 14, var4 + 42, 56, 1, _3.A(f.C, 0.23));
            c7.K(var0, var3 + 42, var4 + 42, 2, f.C);
            double var18 = var7 == null ? 47.0 : var7.u.P();
            if (var6.player != null && var6.level != null) {
               MCFriendModule var21 = LarpClient.F().x().V(MCFriendModule.class);

               for (Entity var25 : var6.level.entitiesForRendering()) {
                  boolean var27 = var25 instanceof Player;
                  if (var25 != var6.player && var25.isAlive() && (var27 || var7 != null && var7.H.P() && var25 instanceof Mob)) {
                     double[] var16 = c(var25.getX() - var6.player.getX(), var25.getZ() - var6.player.getZ(), var6.player.getYRot(), var18);
                     if (var16 != null) {
                        int var29 = !var27
                           ? -922768896
                           : (var25 == h.o() ? -44462 : (var21 != null && var21.V(var25.getName().getString()) ? -10551483 : -335544321));
                        c7.K(var0, var3 + (int)Math.round(var16[0]), var4 + (int)Math.round(var16[1]), 2, var29);
                     }
                  }
               }
            }

            if (var5) {
               c7.K(var0, var3 + 54, var4 + 29, 2, -335544321);
            }
            break;
         default:
            l(var0, var3, var4, var2.width(), var2.height(), var2.title(), var2.icon(), true);
            int var10 = var1.equals("staffs") ? 19 : 23;
            int var11 = var4 + 39;

            for (e0.k var13 : var2.rows()) {
               int var14 = var3 + 12;
               if (!var13.item().isEmpty()) {
                  var0.item(var13.item(), var14, var11 + (var10 - 16) / 2);
                  var14 += 22;
               } else if (var13.player() != null && StaffListModule.X()) {
                  PlayerFaceExtractor.extractRenderState(var0, var13.player().getSkin(), var14, var11 + (var10 - 15) / 2, 15);
                  var14 += 21;
               }

               double var15 = ew.N(var11, var10, 13.0);
               int var17 = var13.value().isEmpty() ? 0 : we.V(var13.value(), 13.0) + 8;
               we.F(
                  var0,
                  we.t(var13.label(), var3 + var2.width() - 12 - var17 - var14, 13.0),
                  var14,
                  var15,
                  13.0,
                  var13.value().isEmpty() ? var13.tint() : -1250068
               );
               we.F(var0, var13.value(), var3 + var2.width() - 12 - we.V(var13.value(), 13.0), var15, 13.0, var13.tint());
               var11 += var10;
            }
      }
   }

   private static final class j {
      static final Set<Item> n = Set.of(Items.ENDER_PEARL, Items.CHORUS_FRUIT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.SHIELD);
   }

   public record k(String label, String value, ItemStack item, PlayerInfo player, int tint) {
   }

   public record o(String title, String icon, int width, int height, List<e0.k> rows) {
   }
}

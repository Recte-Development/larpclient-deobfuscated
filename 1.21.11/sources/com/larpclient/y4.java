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
import net.minecraft.class_1792;
import net.minecraft.class_1796;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_640;
import net.minecraft.class_7532;

public final class y4 {
   private static final List<String> L = List.of(
      "helper", "admin", "owner", "founder", "manager", "curator", "developer", "staff", "куратор", "админ", "хелпер", "помощн", "стаж", "владел", "разраб"
   );
   private static final String K = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡʏᴢ";
   private static final String i = "abcdefghijklmnopqrstuvwyz";
   private static final Pattern w = Pattern.compile("§.?");
   private static final Pattern B = Pattern.compile("[^\\p{L}]+");
   private static final Map<String, Boolean> R = new ConcurrentHashMap<>();
   private static final int k = 5;
   private static final int H = 22;
   private static final int v = 21;
   private static final int V = 20;
   private static final int J = 7;
   private static final int Z = 11;
   private static final float j = 1.25F;
   private static final int u = 197;
   private static final int c = 65;

   private y4() {
   }

   public static boolean x(String var0) {
      if (var0.isBlank()) {
         return false;
      }

      if (R.size() > 1024) {
         R.clear();
      }

      return R.computeIfAbsent(var0, y4::F);
   }

   private static boolean F(String var0) {
      String var1 = Normalizer.normalize(w.matcher(var0).replaceAll(""), Form.NFKC).toLowerCase(Locale.ROOT);
      StringBuilder var2 = new StringBuilder(var1.length());

      for (int var3 = 0; var3 < var1.length(); var3++) {
         int var4 = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡʏᴢ".indexOf(var1.charAt(var3));
         var2.append(var4 < 0 ? var1.charAt(var3) : "abcdefghijklmnopqrstuvwyz".charAt(var4));
      }

      String var8 = var2.toString();
      if (L.stream().anyMatch(var8::contains)) {
         return true;
      }

      for (String var7 : B.split(var8)) {
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

   public static y4.l L(String var0, boolean var1) {
      class_310 var2 = class_310.method_1551();
      HUDModule var3 = HUDModule.y();
      ArrayList var4 = new ArrayList();
      switch (var0) {
         case "cooldowns":
            if (var2.field_1724 != null) {
               HashSet var11 = new HashSet();
               class_1796 var12 = var2.field_1724.method_7357();

               for (int var9 = 0; var9 < var2.field_1724.method_31548().method_5439(); var9++) {
                  class_1799 var10 = var2.field_1724.method_31548().method_5438(var9);
                  if (!var10.method_7960()
                     && (var3 == null || !var3.w.G("Combat") || y4.w.j.contains(var10.method_7909()))
                     && var12.method_7904(var10)
                     && var11.add(var12.method_62836(var10))) {
                     var4.add(
                        new y4.z(
                           var10.method_7964().getString(),
                           String.format(Locale.ROOT, "%.1fs", var12.method_7905(var10, 0.0F) * 3.0F),
                           var10.method_7972(),
                           null,
                           -1250068
                        )
                     );
                  }
               }
            }

            if (var1 && var4.isEmpty()) {
               var4.add(new y4.z("Ender Pearl", "1.8s", new class_1799(class_1802.field_8634), null, -1250068));
            }

            return Q("Cooldowns", "gauge", var4, 23);
         case "staffs":
            for (StaffListModule.z var8 : StaffListModule.L()) {
               var4.add(new y4.z(var8.name(), var8.status().K, class_1799.field_8037, var8.info(), var8.status().g));
            }

            if (var1 && var4.isEmpty()) {
               var4.add(new y4.z("Staff", "Online", class_1799.field_8037, null, -10551483));
            }

            if (var4.isEmpty()) {
               var4.add(new y4.z("No staff online", "", class_1799.field_8037, null, -7697772));
            }

            return Q("StaffList", "user", var4, 19);
         case "inventory":
            return r(var3);
         default:
            throw new IllegalArgumentException(var0);
      }
   }

   private static int H(boolean var0) {
      return var0 ? 24 : 5;
   }

   private static y4.l r(HUDModule var0) {
      int var1 = var0 != null && !var0.j.getValue() ? 0 : 1;
      return new y4.l("Inventory", "panels-top-left", 207, H((boolean)var1) + 65 + 5, List.of());
   }

   private static y4.l Q(String var0, String var1, List<y4.z> var2, int var3) {
      int var4 = 168;

      for (y4.z var6 : var2) {
         var4 = Math.max(var4, Math.min(300, w5.A(var6.label(), 13.0) + w5.A(var6.value(), 13.0) + 64));
      }

      return new y4.l(var0, var1, var4, 48 + var2.size() * var3, List.copyOf(var2));
   }

   public static boolean p(String var0, y4.l var1) {
      return (boolean)(switch (var0) {
         case "cooldowns" -> var1 != null && !var1.rows().isEmpty() ? 1 : 0;
         default -> 1;
      });
   }

   public static void R(class_332 var0, int var1, int var2, int var3, int var4, String var5, String var6, boolean var7) {
      T(var0, var1, var2, var3, var4, var5, var6, var7 ? 32 : 0, 12, 13);
   }

   private static void T(class_332 var0, int var1, int var2, int var3, int var4, String var5, String var6, int var7, int var8, int var9) {
      gu.N(var0, var1, var2, var3, var4, var8, -603321844);
      if (var7 > 0) {
         var0.method_44379(var1, var2, var1 + var3, var2 + var7);

         try {
            gu.N(var0, var1, var2, var3, var4, var8, -267645934);
         } finally {
            var0.method_44380();
         }
      }

      ur.D(var0, var1, var2, var3, var4, var8);
      ur.x(var0, var1, var2, var3, var4, var8);
      if (var7 > 0) {
         int var10 = Math.max(6, var8);
         int var11 = var9 + 1;
         ws.r(var0, var1 + 2, var2 + var7, var3 - 4, 1.0F, 0.5F, 675036230);
         w5.y(var0, var5, var1 + var10, q2.e(var2, var7, var9), var9, -1250068);
         w5.C(var0, var6, var1 + var3 - var10 - var11, var2 + (var7 - var11) / 2, var11, ez.a);
      }
   }

   public static void E(class_332 var0, String var1, y4.l var2, int var3, int var4, boolean var5) {
      class_310 var6 = class_310.method_1551();
      HUDModule var7 = HUDModule.y();
      switch (var1) {
         case "inventory":
            int var20 = var7 != null && !var7.j.getValue() ? 0 : 1;
            T(var0, var3, var4, var2.width(), var2.height(), var2.title(), var2.icon(), var20 != 0 ? 20 : 0, 7, 11);
            int var21 = var4 + H((boolean)var20);
            float var22 = 0.5F;

            for (int var23 = 0; var23 < 27; var23++) {
               int var24 = var3 + 5 + var23 % 9 * 22;
               int var25 = var21 + var23 / 9 * 22;
               class_1799 var16 = var6.field_1724 == null ? class_1799.field_8037 : var6.field_1724.method_31548().method_5438(var23 + 9);
               if (var5 && var16.method_7960() && var23 < 3) {
                  var16 = new class_1799(var23 == 0 ? class_1802.field_8634 : (var23 == 1 ? class_1802.field_8802 : class_1802.field_8463));
               }

               gu.N(var0, var24, var25, 21.0F, 21.0F, 3.0F, var16.method_7960() ? 452984831 : 788529151);
               if (!var16.method_7960()) {
                  var0.method_51448().pushMatrix();

                  try {
                     var0.method_51448().translate(var24 + var22, var25 + var22);
                     var0.method_51448().scale(1.25F, 1.25F);
                     var0.method_51427(var16, 0, 0);
                     var0.method_51431(var6.field_1772, var16, 0, 0);
                  } finally {
                     var0.method_51448().popMatrix();
                  }
               }
            }
            break;
         default:
            R(var0, var3, var4, var2.width(), var2.height(), var2.title(), var2.icon(), true);
            int var10 = var1.equals("staffs") ? 19 : 23;
            int var11 = var4 + 39;

            for (y4.z var13 : var2.rows()) {
               int var14 = var3 + 12;
               if (!var13.item().method_7960()) {
                  var0.method_51427(var13.item(), var14, var11 + (var10 - 16) / 2);
                  var14 += 22;
               } else if (var13.player() != null && StaffListModule.j()) {
                  class_7532.method_52722(var0, var13.player().method_52810(), var14, var11 + (var10 - 15) / 2, 15);
                  var14 += 21;
               }

               double var15 = q2.e(var11, var10, 13.0);
               int var17 = var13.value().isEmpty() ? 0 : w5.A(var13.value(), 13.0) + 8;
               w5.y(
                  var0,
                  w5.O(var13.label(), var3 + var2.width() - 12 - var17 - var14, 13.0),
                  var14,
                  var15,
                  13.0,
                  var13.value().isEmpty() ? var13.tint() : -1250068
               );
               w5.y(var0, var13.value(), var3 + var2.width() - 12 - w5.A(var13.value(), 13.0), var15, 13.0, var13.tint());
               var11 += var10;
            }
      }
   }

   public record l(String title, String icon, int width, int height, List<y4.z> rows) {
   }

   private static final class w {
      static final Set<class_1792> j = Set.of(class_1802.field_8634, class_1802.field_8233, class_1802.field_8463, class_1802.field_8367, class_1802.field_8255);
   }

   public record z(String label, String value, class_1799 item, class_640 player, int tint) {
   }
}

package com.larpclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.Setting;
import com.larpclient.util.data.BindCodes;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class SettingShortcuts {
   private static final int mAX_SHORTCUTS = 256;
   private final Map<String, Map<String, Integer>> shortcuts = new LinkedHashMap<>();
   private List<SettingShortcuts.ShortcutEntry> cachedEntries;
   private List<Module> cachedModules;

   public static boolean supportsShortcut(Setting<?> var0) {
      return var0 instanceof BooleanSetting || var0 instanceof ModeSetting var1 && !var1.isSearchable();
   }

   public static Module findOwner(List<Module> var0, Setting<?> var1) {
      for (Module var3 : var0) {
         for (Setting var5 : var3.getSettings()) {
            if (var5 == var1) {
               return var3;
            }
         }
      }

      return null;
   }

   public int getCode(Module var1, Setting<?> var2) {
      Map var3 = this.shortcuts.get(var1.getName());
      return var3 == null ? -1 : var3.getOrDefault(var2.getName(), -1);
   }

   public void setCode(Module var1, Setting<?> var2, int var3) {
      if (!supportsShortcut(var2)) {
         throw new IllegalArgumentException("Only toggle and choice settings take a shortcut");
      }

      if (var3 != -1 && !BindCodes.isValidBind(var3)) {
         throw new IllegalArgumentException("Invalid bind code");
      }

      String var4 = validateModuleName(var1.getName());
      String var5 = validateSettingName(var2.getName());
      if (var3 == -1) {
         Map var6 = this.shortcuts.get(var4);
         if (var6 != null && var6.remove(var5) != null && var6.isEmpty()) {
            this.shortcuts.remove(var4);
         }
      } else {
         if (this.getCode(var1, var2) == -1 && this.count() >= 256) {
            throw new IllegalArgumentException("Too many setting shortcuts");
         }

         this.shortcuts.computeIfAbsent(var4, var0 -> new LinkedHashMap<>()).put(var5, var3);
      }

      this.cachedEntries = null;
   }

   public int count() {
      return this.shortcuts.values().stream().mapToInt(Map::size).sum();
   }

   public List<SettingShortcuts.ShortcutEntry> getEntries(List<Module> var1) {
      if (this.cachedEntries != null && this.cachedModules == var1) {
         return this.cachedEntries;
      }

      ArrayList var2 = new ArrayList();

      for (Module var4 : var1) {
         Map var5 = this.shortcuts.get(var4.getName());
         if (var5 != null) {
            for (Setting var7 : var4.getSettings()) {
               Integer var8 = (Integer)var5.get(var7.getName());
               if (var8 != null && supportsShortcut(var7)) {
                  var2.add(new SettingShortcuts.ShortcutEntry(var4, var7, var8));
               }
            }
         }
      }

      this.cachedModules = var1;
      return this.cachedEntries = List.copyOf(var2);
   }

   public SettingShortcuts copy() {
      return fromJson(this.toJson());
   }

   public JsonObject toJson() {
      JsonObject var1 = new JsonObject();
      this.shortcuts.forEach((var1x, var2) -> {
         JsonObject var3 = new JsonObject();
         var2.forEach(var3::addProperty);
         var1.add(var1x, var3);
      });
      return var1;
   }

   public static SettingShortcuts fromJson(JsonElement var0) {
      SettingShortcuts var1 = new SettingShortcuts();
      if (var0 != null && var0.isJsonObject()) {
         for (Entry var3 : var0.getAsJsonObject().entrySet()) {
            if (((JsonElement)var3.getValue()).isJsonObject()) {
               for (Entry var5 : ((JsonElement)var3.getValue()).getAsJsonObject().entrySet()) {
                  try {
                     if (var1.count() >= 256) {
                        return var1;
                     }

                     JsonElement var6 = (JsonElement)var5.getValue();
                     if (var6.isJsonPrimitive() && var6.getAsJsonPrimitive().isNumber()) {
                        double var7 = var6.getAsDouble();
                        if (var7 == Math.rint(var7) && BindCodes.isValidBind((int)var7)) {
                           var1.shortcuts
                              .computeIfAbsent(validateModuleName((String)var3.getKey()), var0x -> new LinkedHashMap<>())
                              .put(validateSettingName((String)var5.getKey()), (int)var7);
                        }
                     }
                  } catch (RuntimeException var9) {
                  }
               }
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   private static String validateModuleName(String var0) {
      if (var0 != null && var0.matches("[A-Za-z0-9 _-]{1,128}") && !var0.isBlank()) {
         return var0;
      } else {
         throw new IllegalArgumentException("Invalid module identifier");
      }
   }

   private static String validateSettingName(String var0) {
      if (var0 != null && !var0.isEmpty() && var0.length() <= 128 && !var0.chars().anyMatch(Character::isISOControl)) {
         return var0;
      } else {
         throw new IllegalArgumentException("Invalid setting identifier");
      }
   }

   public record ShortcutEntry(Module module, Setting<?> setting, int code) {
   }
}

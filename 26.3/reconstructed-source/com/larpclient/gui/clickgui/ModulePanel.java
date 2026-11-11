package com.larpclient.gui.clickgui;

import com.larpclient.LarpClient;
import com.larpclient.gui.widget.Widget;
import com.larpclient.module.misc.Module;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public interface ModulePanel {
   default ModulePanel.HudCard hudCard() {
      return null;
   }

   List<ModulePanel.Command> commands();

   String summary();

   default String detail() {
      return "";
   }

   default Widget preview() {
      return null;
   }

   default ModulePanel.Status status() {
      return null;
   }

   default void panelTick() {
   }

   List<ModulePanel.SettingGroup> settingGroups();

   default ModulePanel.Action findAction(String var1, String var2) {
      return this.commands()
         .stream()
         .filter(var1x -> var1x.label().equals(var2))
         .findFirst()
         .map(var1x -> new ModulePanel.Action(var1, var1x.run()))
         .orElseThrow();
   }

   static String formatDuration(long var0) {
      long var2 = Math.max(0L, var0 / 1000L);
      return var2 >= 3600L ? var2 / 3600L + "h " + var2 % 3600L / 60L + "m" : (var2 >= 60L ? var2 / 60L + "m " + var2 % 60L + "s" : var2 + "s");
   }

   static String findConflictingModule(Module var0) {
      LarpClient var1 = LarpClient.getInstance();
      if (var1 != null && var1.getModuleManager() != null) {
         for (Module var3 : var1.getModuleManager().getModules()) {
            if (var3 != var0 && var3 instanceof ModulePanel && var3.isRuntimeActive()) {
               return var3.getName();
            }
         }

         return null;
      } else {
         return null;
      }
   }

   record Action(String label, Supplier<String> run, BooleanSupplier available) {
      public Action(String var1, Supplier<String> var2) {
         this(var1, var2, () -> true);
      }

      public ModulePanel.Action availableWhen(BooleanSupplier var1) {
         return new ModulePanel.Action(this.label, this.run, var1);
      }
   }

   record Command(String section, String label, Supplier<String> run) {
   }

   record HudCard(String headline, String value, String action, String time, double progress, boolean alert) {
      public HudCard(String var1, String var2, String var3, String var4, double var5) {
         this(var1, var2, var3, var4, var5, false);
      }
   }

   record Setting(Supplier<String> value, List<List<ModulePanel.Action>> rows, Supplier<Widget> picture, boolean settingsFirst) {
      public Setting(Supplier<String> var1, List<List<ModulePanel.Action>> var2) {
         this(var1, var2, () -> null, false);
      }
   }

   record SettingGroup(String title, List<com.larpclient.module.misc.Setting<?>> settings, boolean advanced, ModulePanel.Setting setup) {
      public SettingGroup(String var1, List<com.larpclient.module.misc.Setting<?>> var2) {
         this(var1, var2, false, null);
      }

      public static ModulePanel.SettingGroup advanced(String var0, List<com.larpclient.module.misc.Setting<?>> var1) {
         return new ModulePanel.SettingGroup(var0, var1, true, null);
      }

      public ModulePanel.SettingGroup with(ModulePanel.Setting var1) {
         return new ModulePanel.SettingGroup(this.title, this.settings, this.advanced, var1);
      }
   }

   record Status(String text, boolean problem) {
   }
}

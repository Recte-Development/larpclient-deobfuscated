package com.larpclient.module.render;

import com.larpclient.config.ClientPreferences;
import com.larpclient.module.misc.KeybindSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.Setting;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayList;
import java.util.List;

public final class ChooseASkyEffectAndAdjustItsAppearanceModule extends Module {
   private final CustomSkyModule customSky;
   private final SkyShaderModule skyShader;
   private final ClientPreferences preferences;
   private final ModeSetting styleSetting;
   private static ChooseASkyEffectAndAdjustItsAppearanceModule instance;
   private static List<Module> lastModules = List.of();
   private static List<Module> lastResult = List.of();

   public ChooseASkyEffectAndAdjustItsAppearanceModule(final CustomSkyModule var1, final SkyShaderModule var2, final ClientPreferences var3) {
      super(var1.getName(), "Choose a sky effect and adjust its appearance", var1.getCategory(), -1);
      this.customSky = var1;
      this.skyShader = var2;
      this.preferences = var3;
      ArrayList var4 = new ArrayList();
      var1.getSkyStyleSetting().getModes().forEach(var1x -> var4.add("World: " + var1x));
      var2.getModeSetting().getModes().forEach(var1x -> var4.add("Shader: " + var1x));
      this.styleSetting = new ModeSetting(
         "Style", "Choose a sky. Classic and shader skies retain their own appearance settings", "World: Nebula", var4.toArray(String[]::new)
      ) {
         public String getValue() {
            return ChooseASkyEffectAndAdjustItsAppearanceModule.this.getSkySource()
               + ": "
               + (ChooseASkyEffectAndAdjustItsAppearanceModule.this.getSkySource().equals("World") ? var1.getSkyStyleSetting() : var2.getModeSetting())
                  .getValue();
         }

         @Override
         public String getModeLabel(String var1x) {
            String var2x = var1x.substring(var1x.indexOf(": ") + 2);
            return var1x.startsWith("World:") ? var2x + " (Classic)" : var2x;
         }

         @Override
         public boolean isSearchable() {
            return true;
         }

         @Override
         public void setValue(String var1x) {
            if (!this.acceptsValue(var1x)) {
               throw new IllegalArgumentException("Unknown sky: " + var1x);
            }

            boolean var2x = ChooseASkyEffectAndAdjustItsAppearanceModule.this.isEnabled();
            boolean var3x = var1x.startsWith("World:");
            (var3x ? var1.getSkyStyleSetting() : var2.getModeSetting()).setValue(var1x.substring(var1x.indexOf(": ") + 2));
            var3.skySource = var3x ? "World" : "Shader";
            if (var2x) {
               ChooseASkyEffectAndAdjustItsAppearanceModule.this.switchSource(var3x);
            }

            LarpLog.info("Sky", "Selected {}; enabled={}", var1x, var2x);
         }
      };
   }

   private static KeybindSetting createKeybindSetting(String var0, Module var1) {
      KeybindSetting var2 = new KeybindSetting(
         var0,
         "Retains the original sky shortcut. Takes a key or mouse button; for left or right click, click this field again. Escape/Delete clears it",
         var1.getKeyBind()
      );
      var2.onChange(var1::setKeyBind);
      return var2;
   }

   private String getSkySource() {
      if (this.customSky.isEnabled()) {
         this.preferences.skySource = "World";
      } else if (this.skyShader.isEnabled()) {
         this.preferences.skySource = "Shader";
      }

      return this.preferences.skySource;
   }

   private void switchSource(boolean var1) {
      if (var1) {
         this.skyShader.disable();
         this.customSky.enable();
      } else {
         this.customSky.disable();
         this.skyShader.enable();
      }
   }

   public ModeSetting getStyleSetting() {
      return this.styleSetting;
   }

   public List<Setting<?>> getKeybindSettings() {
      return List.of(createKeybindSetting("Classic sky keybind", this.customSky), createKeybindSetting("Larp sky keybind", this.skyShader));
   }

   public Module getActiveModule() {
      return this.getSkySource().equals("World") ? this.customSky : this.skyShader;
   }

   public List<Setting<?>> getAppearanceSettings() {
      Module var1 = this.getSkySource().equals("World") ? this.customSky : this.skyShader;
      ModeSetting var2 = var1 == this.customSky ? this.customSky.getSkyStyleSetting() : this.skyShader.getModeSetting();
      return var1.getSettings().stream().filter(var1x -> var1x != var2).toList();
   }

   @Override
   public String getDisplayName() {
      return "Sky";
   }

   @Override
   public boolean hasSettings() {
      return true;
   }

   @Override
   public List<Setting<?>> getSettings() {
      ArrayList var1 = new ArrayList<>(this.customSky.getSettings());
      var1.addAll(this.skyShader.getSettings());
      return var1;
   }

   @Override
   public boolean isEnabled() {
      return this.customSky.isEnabled() || this.skyShader.isEnabled();
   }

   @Override
   public boolean isOperational() {
      return !this.isEnabled() ? false : this.customSky.isOperational() || this.skyShader.isOperational();
   }

   @Override
   public boolean isRuntimeActive() {
      return !this.isEnabled() ? false : this.customSky.isRuntimeActive() || this.skyShader.isRuntimeActive();
   }

   @Override
   public void toggle() {
      this.setEnabled(!this.isEnabled());
   }

   @Override
   public void setEnabled(boolean var1) {
      if (var1) {
         this.enable();
      } else {
         this.disable();
      }
   }

   @Override
   public void enable() {
      this.switchSource(this.getSkySource().equals("World"));
   }

   @Override
   public void disable() {
      this.getSkySource();
      this.customSky.disable();
      this.skyShader.disable();
   }

   public static List<Module> mergeSkyModules(List<Module> var0, ClientPreferences var1) {
      if (instance != null && instance.preferences == var1 && lastModules.equals(var0)) {
         return lastResult;
      }

      CustomSkyModule var2 = null;
      SkyShaderModule var3 = null;

      for (Module var5 : var0) {
         if (var5 instanceof CustomSkyModule var6) {
            var2 = var6;
         }

         if (var5 instanceof SkyShaderModule var9) {
            var3 = var9;
         }
      }

      if (var2 != null && var3 != null) {
         if (instance == null || instance.customSky != var2 || instance.skyShader != var3 || instance.preferences != var1) {
            instance = new ChooseASkyEffectAndAdjustItsAppearanceModule(var2, var3, var1);
         }

         ArrayList var7 = new ArrayList(var0.size() - 1);

         for (Module var10 : var0) {
            if (var10 == var2) {
               var7.add(instance);
            } else if (var10 != var3) {
               var7.add(var10);
            }
         }

         lastModules = List.copyOf(var0);
         lastResult = List.copyOf(var7);
         return lastResult;
      } else {
         return var0;
      }
   }

   public static Module resolveModule(Module var0, List<Module> var1, ClientPreferences var2) {
      return !(var0 instanceof CustomSkyModule) && !(var0 instanceof SkyShaderModule)
         ? var0
         : mergeSkyModules(var1, var2).stream().filter(var0x -> var0x instanceof ChooseASkyEffectAndAdjustItsAppearanceModule).findFirst().orElse(var0);
   }
}

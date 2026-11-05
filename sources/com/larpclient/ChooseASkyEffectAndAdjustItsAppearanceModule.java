package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.Module;
import com.larpclient.module.SkyShaderModule;
import java.util.ArrayList;
import java.util.List;

public final class ChooseASkyEffectAndAdjustItsAppearanceModule extends Module {
   private final CustomSkyModule I;
   private final SkyShaderModule q;
   private final fs N;
   private final com.larpclient.module.lp E;
   private static ChooseASkyEffectAndAdjustItsAppearanceModule z;
   private static List<Module> J = List.of();
   private static List<Module> m = List.of();

   public ChooseASkyEffectAndAdjustItsAppearanceModule(final CustomSkyModule var1, final SkyShaderModule var2, final fs var3) {
      super(var1.getName(), "Choose a sky effect and adjust its appearance", var1.getCategory(), -1);
      this.I = var1;
      this.q = var2;
      this.N = var3;
      ArrayList var4 = new ArrayList();
      var1.G().X().forEach(var1x -> var4.add("World: " + var1x));
      var2.d().X().forEach(var1x -> var4.add("Shader: " + var1x));
      this.E = new com.larpclient.module.lp(
         "Style", "Choose a sky. Classic and shader skies retain their own appearance settings", "World: Nebula", var4.toArray(String[]::new)
      ) {
         public String getValue() {
            return ChooseASkyEffectAndAdjustItsAppearanceModule.this.G()
               + ": "
               + (ChooseASkyEffectAndAdjustItsAppearanceModule.this.G().equals("World") ? var1.G() : var2.d()).getValue();
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
            (var3x ? var1.G() : var2.d()).setValue(var1x.substring(var1x.indexOf(": ") + 2));
            var3.k = var3x ? "World" : "Shader";
            if (var2x) {
               ChooseASkyEffectAndAdjustItsAppearanceModule.this.i(var3x);
            }

            r4.V("Sky", "Selected {}; enabled={}", var1x, var2x);
         }
      };
   }

   private static com.larpclient.module.la S(String var0, Module var1) {
      com.larpclient.module.la var2 = new com.larpclient.module.la(
         var0,
         "Retains the original sky shortcut. Takes a key or mouse button; for left or right click, click this field again. Escape/Delete clears it",
         var1.getKeyBind()
      );
      var2.W(var1::setKeyBind);
      return var2;
   }

   private String G() {
      if (this.I.isEnabled()) {
         this.N.k = "World";
      } else if (this.q.isEnabled()) {
         this.N.k = "Shader";
      }

      return this.N.k;
   }

   private void i(boolean var1) {
      if (var1) {
         this.q.disable();
         this.I.enable();
      } else {
         this.I.disable();
         this.q.enable();
      }
   }

   public com.larpclient.module.lp w() {
      return this.E;
   }

   public List<com.larpclient.module.l6<?>> R() {
      return List.of(S("Classic sky keybind", this.I), S("Larp sky keybind", this.q));
   }

   public Module m() {
      return this.G().equals("World") ? this.I : this.q;
   }

   public List<com.larpclient.module.l6<?>> C() {
      Module var1 = this.G().equals("World") ? this.I : this.q;
      com.larpclient.module.lp var2 = var1 == this.I ? this.I.G() : this.q.d();
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
   public List<com.larpclient.module.l6<?>> getSettings() {
      ArrayList var1 = new ArrayList<>(this.I.getSettings());
      var1.addAll(this.q.getSettings());
      return var1;
   }

   @Override
   public boolean isEnabled() {
      return this.I.isEnabled() || this.q.isEnabled();
   }

   @Override
   public boolean isOperational() {
      return !this.isEnabled() ? false : this.I.isOperational() || this.q.isOperational();
   }

   @Override
   public boolean isRuntimeActive() {
      return !this.isEnabled() ? false : this.I.isRuntimeActive() || this.q.isRuntimeActive();
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
      this.i(this.G().equals("World"));
   }

   @Override
   public void disable() {
      this.G();
      this.I.disable();
      this.q.disable();
   }

   public static List<Module> O(List<Module> var0, fs var1) {
      if (z != null && z.N == var1 && J.equals(var0)) {
         return m;
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
         if (z == null || z.I != var2 || z.q != var3 || z.N != var1) {
            z = new ChooseASkyEffectAndAdjustItsAppearanceModule(var2, var3, var1);
         }

         ArrayList var7 = new ArrayList(var0.size() - 1);

         for (Module var10 : var0) {
            if (var10 == var2) {
               var7.add(z);
            } else if (var10 != var3) {
               var7.add(var10);
            }
         }

         J = List.copyOf(var0);
         m = List.copyOf(var7);
         return m;
      } else {
         return var0;
      }
   }

   public static Module R(Module var0, List<Module> var1, fs var2) {
      return !(var0 instanceof CustomSkyModule) && !(var0 instanceof SkyShaderModule)
         ? var0
         : O(var1, var2).stream().filter(var0x -> var0x instanceof ChooseASkyEffectAndAdjustItsAppearanceModule).findFirst().orElse(var0);
   }
}

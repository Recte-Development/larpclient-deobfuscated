package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.Module;
import com.larpclient.module.SkyShaderModule;
import java.util.ArrayList;
import java.util.List;

public final class ChooseASkyEffectAndAdjustItsAppearanceModule extends Module {
   private final CustomSkyModule L;
   private final SkyShaderModule O;
   private final i3 W;
   private final com.larpclient.module.o p;
   private static ChooseASkyEffectAndAdjustItsAppearanceModule G;
   private static List<Module> E = List.of();
   private static List<Module> i = List.of();

   public ChooseASkyEffectAndAdjustItsAppearanceModule(final CustomSkyModule var1, final SkyShaderModule var2, final i3 var3) {
      super(var1.getName(), "Choose a sky effect and adjust its appearance", var1.getCategory(), -1);
      this.L = var1;
      this.O = var2;
      this.W = var3;
      ArrayList var4 = new ArrayList();
      var1.Z().j().forEach(var1x -> var4.add("World: " + var1x));
      var2.E().j().forEach(var1x -> var4.add("Shader: " + var1x));
      this.p = new com.larpclient.module.o(
         "Style", "Choose a sky. Classic and shader skies retain their own appearance settings", "World: Nebula", var4.toArray(String[]::new)
      ) {
         public String getValue() {
            return ChooseASkyEffectAndAdjustItsAppearanceModule.this.z()
               + ": "
               + (ChooseASkyEffectAndAdjustItsAppearanceModule.this.z().equals("World") ? var1.Z() : var2.E()).getValue();
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
            (var3x ? var1.Z() : var2.E()).setValue(var1x.substring(var1x.indexOf(": ") + 2));
            var3.P = var3x ? "World" : "Shader";
            if (var2x) {
               ChooseASkyEffectAndAdjustItsAppearanceModule.this.c(var3x);
            }

            _q.z("Sky", "Selected {}; enabled={}", var1x, var2x);
         }
      };
   }

   private static com.larpclient.module.x K(String var0, Module var1) {
      com.larpclient.module.x var2 = new com.larpclient.module.x(
         var0,
         "Retains the original sky shortcut. Takes a key or mouse button; for left or right click, click this field again. Escape/Delete clears it",
         var1.getKeyBind()
      );
      var2.w(var1::setKeyBind);
      return var2;
   }

   private String z() {
      if (this.L.isEnabled()) {
         this.W.P = "World";
      } else if (this.O.isEnabled()) {
         this.W.P = "Shader";
      }

      return this.W.P;
   }

   private void c(boolean var1) {
      if (var1) {
         this.O.disable();
         this.L.enable();
      } else {
         this.L.disable();
         this.O.enable();
      }
   }

   public com.larpclient.module.o l() {
      return this.p;
   }

   public List<com.larpclient.module.r<?>> p() {
      return List.of(K("Classic sky keybind", this.L), K("Larp sky keybind", this.O));
   }

   public Module L() {
      return this.z().equals("World") ? this.L : this.O;
   }

   public List<com.larpclient.module.r<?>> J() {
      Module var1 = this.z().equals("World") ? this.L : this.O;
      com.larpclient.module.o var2 = var1 == this.L ? this.L.Z() : this.O.E();
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
   public List<com.larpclient.module.r<?>> getSettings() {
      ArrayList var1 = new ArrayList<>(this.L.getSettings());
      var1.addAll(this.O.getSettings());
      return var1;
   }

   @Override
   public boolean isEnabled() {
      return this.L.isEnabled() || this.O.isEnabled();
   }

   @Override
   public boolean isOperational() {
      return !this.isEnabled() ? false : this.L.isOperational() || this.O.isOperational();
   }

   @Override
   public boolean isRuntimeActive() {
      return !this.isEnabled() ? false : this.L.isRuntimeActive() || this.O.isRuntimeActive();
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
      this.c(this.z().equals("World"));
   }

   @Override
   public void disable() {
      this.z();
      this.L.disable();
      this.O.disable();
   }

   public static List<Module> Z(List<Module> var0, i3 var1) {
      if (G != null && G.W == var1 && E.equals(var0)) {
         return i;
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
         if (G == null || G.L != var2 || G.O != var3 || G.W != var1) {
            G = new ChooseASkyEffectAndAdjustItsAppearanceModule(var2, var3, var1);
         }

         ArrayList var7 = new ArrayList(var0.size() - 1);

         for (Module var10 : var0) {
            if (var10 == var2) {
               var7.add(G);
            } else if (var10 != var3) {
               var7.add(var10);
            }
         }

         E = List.copyOf(var0);
         i = List.copyOf(var7);
         return i;
      } else {
         return var0;
      }
   }

   public static Module N(Module var0, List<Module> var1, i3 var2) {
      return !(var0 instanceof CustomSkyModule) && !(var0 instanceof SkyShaderModule)
         ? var0
         : Z(var1, var2).stream().filter(var0x -> var0x instanceof ChooseASkyEffectAndAdjustItsAppearanceModule).findFirst().orElse(var0);
   }
}

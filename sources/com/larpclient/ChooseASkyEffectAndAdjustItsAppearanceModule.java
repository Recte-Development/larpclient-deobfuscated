package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.Module;
import com.larpclient.module.SkyShaderModule;
import com.larpclient.module.df;
import com.larpclient.module.dv;
import java.util.ArrayList;
import java.util.List;

public final class ChooseASkyEffectAndAdjustItsAppearanceModule extends Module {
   private final CustomSkyModule O;
   private final SkyShaderModule H;
   private final xv a;
   private final com.larpclient.module.d6 K;
   private static ChooseASkyEffectAndAdjustItsAppearanceModule S;
   private static List<Module> y = List.of();
   private static List<Module> i = List.of();

   public ChooseASkyEffectAndAdjustItsAppearanceModule(final CustomSkyModule var1, final SkyShaderModule var2, final xv var3) {
      super(var1.getName(), "Choose a sky effect and adjust its appearance", var1.getCategory(), -1);
      this.O = var1;
      this.H = var2;
      this.a = var3;
      ArrayList var4 = new ArrayList();
      var1.N().N().forEach(var1x -> var4.add("World: " + var1x));
      var2.p().N().forEach(var1x -> var4.add("Shader: " + var1x));
      this.K = new com.larpclient.module.d6(
         "Style", "Choose a sky. Classic and shader skies retain their own appearance settings", "World: Nebula", var4.toArray(String[]::new)
      ) {
         public String getValue() {
            return ChooseASkyEffectAndAdjustItsAppearanceModule.this.w()
               + ": "
               + (ChooseASkyEffectAndAdjustItsAppearanceModule.this.w().equals("World") ? var1.N() : var2.p()).P();
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
            (var3x ? var1.N() : var2.p()).setValue(var1x.substring(var1x.indexOf(": ") + 2));
            var3.z = var3x ? "World" : "Shader";
            if (var2x) {
               ChooseASkyEffectAndAdjustItsAppearanceModule.this.m(var3x);
            }

            mt.K("Sky", "Selected {}; enabled={}", var1x, var2x);
         }
      };
   }

   private static dv O(String var0, Module var1) {
      dv var2 = new dv(
         var0,
         "Retains the original sky shortcut. Takes a key or mouse button; for left or right click, click this field again. Escape/Delete clears it",
         var1.getKeyBind()
      );
      var2.h(var1::setKeyBind);
      return var2;
   }

   private String w() {
      if (this.O.isEnabled()) {
         this.a.z = "World";
      } else if (this.H.isEnabled()) {
         this.a.z = "Shader";
      }

      return this.a.z;
   }

   private void m(boolean var1) {
      if (var1) {
         this.H.disable();
         this.O.enable();
      } else {
         this.O.disable();
         this.H.enable();
      }
   }

   public com.larpclient.module.d6 q() {
      return this.K;
   }

   public List<df<?>> V() {
      return List.of(O("Classic sky keybind", this.O), O("Larp sky keybind", this.H));
   }

   public Module d() {
      return this.w().equals("World") ? this.O : this.H;
   }

   public List<df<?>> t() {
      Module var1 = this.w().equals("World") ? this.O : this.H;
      com.larpclient.module.d6 var2 = var1 == this.O ? this.O.N() : this.H.p();
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
   public List<df<?>> getSettings() {
      ArrayList var1 = new ArrayList<>(this.O.getSettings());
      var1.addAll(this.H.getSettings());
      return var1;
   }

   @Override
   public boolean isEnabled() {
      return this.O.isEnabled() || this.H.isEnabled();
   }

   @Override
   public boolean isOperational() {
      return !this.isEnabled() ? false : this.O.isOperational() || this.H.isOperational();
   }

   @Override
   public boolean isRuntimeActive() {
      return !this.isEnabled() ? false : this.O.isRuntimeActive() || this.H.isRuntimeActive();
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
      this.m(this.w().equals("World"));
   }

   @Override
   public void disable() {
      this.w();
      this.O.disable();
      this.H.disable();
   }

   public static List<Module> C(List<Module> var0, xv var1) {
      if (S != null && S.a == var1 && y.equals(var0)) {
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
         if (S == null || S.O != var2 || S.H != var3 || S.a != var1) {
            S = new ChooseASkyEffectAndAdjustItsAppearanceModule(var2, var3, var1);
         }

         ArrayList var7 = new ArrayList(var0.size() - 1);

         for (Module var10 : var0) {
            if (var10 == var2) {
               var7.add(S);
            } else if (var10 != var3) {
               var7.add(var10);
            }
         }

         y = List.copyOf(var0);
         i = List.copyOf(var7);
         return i;
      } else {
         return var0;
      }
   }

   public static Module r(Module var0, List<Module> var1, xv var2) {
      return !(var0 instanceof CustomSkyModule) && !(var0 instanceof SkyShaderModule)
         ? var0
         : C(var1, var2).stream().filter(var0x -> var0x instanceof ChooseASkyEffectAndAdjustItsAppearanceModule).findFirst().orElse(var0);
   }
}

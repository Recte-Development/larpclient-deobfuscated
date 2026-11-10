package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.Module;
import com.larpclient.module.SkyShaderModule;
import java.util.ArrayList;
import java.util.List;

public final class ChooseASkyEffectAndAdjustItsAppearanceModule extends Module {
   private final CustomSkyModule f;
   private final SkyShaderModule y;
   private final hb a;
   private final com.larpclient.module.l Z;
   private static ChooseASkyEffectAndAdjustItsAppearanceModule B;
   private static List<Module> E = List.of();
   private static List<Module> J = List.of();

   public ChooseASkyEffectAndAdjustItsAppearanceModule(final CustomSkyModule var1, final SkyShaderModule var2, final hb var3) {
      super(var1.getName(), "Choose a sky effect and adjust its appearance", var1.getCategory(), -1);
      this.f = var1;
      this.y = var2;
      this.a = var3;
      ArrayList var4 = new ArrayList();
      var1.d().a().forEach(var1x -> var4.add("World: " + var1x));
      var2.S().a().forEach(var1x -> var4.add("Shader: " + var1x));
      this.Z = new com.larpclient.module.l(
         "Style", "Choose a sky. Classic and shader skies retain their own appearance settings", "World: Nebula", var4.toArray(String[]::new)
      ) {
         public String getValue() {
            return ChooseASkyEffectAndAdjustItsAppearanceModule.this.i()
               + ": "
               + (ChooseASkyEffectAndAdjustItsAppearanceModule.this.i().equals("World") ? var1.d() : var2.S()).getValue();
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
            (var3x ? var1.d() : var2.S()).setValue(var1x.substring(var1x.indexOf(": ") + 2));
            var3.e = var3x ? "World" : "Shader";
            if (var2x) {
               ChooseASkyEffectAndAdjustItsAppearanceModule.this.b(var3x);
            }

            h.A("Sky", "Selected {}; enabled={}", var1x, var2x);
         }
      };
   }

   private static com.larpclient.module.v V(String var0, Module var1) {
      com.larpclient.module.v var2 = new com.larpclient.module.v(
         var0,
         "Retains the original sky shortcut. Takes a key or mouse button; for left or right click, click this field again. Escape/Delete clears it",
         var1.getKeyBind()
      );
      var2.o(var1::setKeyBind);
      return var2;
   }

   private String i() {
      if (this.f.isEnabled()) {
         this.a.e = "World";
      } else if (this.y.isEnabled()) {
         this.a.e = "Shader";
      }

      return this.a.e;
   }

   private void b(boolean var1) {
      if (var1) {
         this.y.disable();
         this.f.enable();
      } else {
         this.f.disable();
         this.y.enable();
      }
   }

   public com.larpclient.module.l a() {
      return this.Z;
   }

   public List<com.larpclient.module.q<?>> U() {
      return List.of(V("Classic sky keybind", this.f), V("Larp sky keybind", this.y));
   }

   public Module Y() {
      return this.i().equals("World") ? this.f : this.y;
   }

   public List<com.larpclient.module.q<?>> H() {
      Module var1 = this.i().equals("World") ? this.f : this.y;
      com.larpclient.module.l var2 = var1 == this.f ? this.f.d() : this.y.S();
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
   public List<com.larpclient.module.q<?>> getSettings() {
      ArrayList var1 = new ArrayList<>(this.f.getSettings());
      var1.addAll(this.y.getSettings());
      return var1;
   }

   @Override
   public boolean isEnabled() {
      return this.f.isEnabled() || this.y.isEnabled();
   }

   @Override
   public boolean isOperational() {
      return !this.isEnabled() ? false : this.f.isOperational() || this.y.isOperational();
   }

   @Override
   public boolean isRuntimeActive() {
      return !this.isEnabled() ? false : this.f.isRuntimeActive() || this.y.isRuntimeActive();
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
      this.b(this.i().equals("World"));
   }

   @Override
   public void disable() {
      this.i();
      this.f.disable();
      this.y.disable();
   }

   public static List<Module> Z(List<Module> var0, hb var1) {
      if (B != null && B.a == var1 && E.equals(var0)) {
         return J;
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
         if (B == null || B.f != var2 || B.y != var3 || B.a != var1) {
            B = new ChooseASkyEffectAndAdjustItsAppearanceModule(var2, var3, var1);
         }

         ArrayList var7 = new ArrayList(var0.size() - 1);

         for (Module var10 : var0) {
            if (var10 == var2) {
               var7.add(B);
            } else if (var10 != var3) {
               var7.add(var10);
            }
         }

         E = List.copyOf(var0);
         J = List.copyOf(var7);
         return J;
      } else {
         return var0;
      }
   }

   public static Module p(Module var0, List<Module> var1, hb var2) {
      return !(var0 instanceof CustomSkyModule) && !(var0 instanceof SkyShaderModule)
         ? var0
         : Z(var1, var2).stream().filter(var0x -> var0x instanceof ChooseASkyEffectAndAdjustItsAppearanceModule).findFirst().orElse(var0);
   }
}

package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.Module;
import com.larpclient.module.SkyShaderModule;
import java.util.ArrayList;
import java.util.List;

public final class ChooseASkyEffectAndAdjustItsAppearanceModule extends Module {
   private final CustomSkyModule r;
   private final SkyShaderModule h;
   private final y0 E;
   private final com.larpclient.module.li Y;
   private static ChooseASkyEffectAndAdjustItsAppearanceModule M;
   private static List<Module> u = List.of();
   private static List<Module> k = List.of();

   public ChooseASkyEffectAndAdjustItsAppearanceModule(final CustomSkyModule var1, final SkyShaderModule var2, final y0 var3) {
      super(var1.getName(), "Choose a sky effect and adjust its appearance", var1.getCategory(), -1);
      this.r = var1;
      this.h = var2;
      this.E = var3;
      ArrayList var4 = new ArrayList();
      var1.r().e().forEach(var1x -> var4.add("World: " + var1x));
      var2.i().e().forEach(var1x -> var4.add("Shader: " + var1x));
      this.Y = new com.larpclient.module.li(
         "Style", "Choose a sky. Classic and shader skies retain their own appearance settings", "World: Nebula", var4.toArray(String[]::new)
      ) {
         public String getValue() {
            return ChooseASkyEffectAndAdjustItsAppearanceModule.this.d()
               + ": "
               + (ChooseASkyEffectAndAdjustItsAppearanceModule.this.d().equals("World") ? var1.r() : var2.i()).getValue();
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
            (var3x ? var1.r() : var2.i()).setValue(var1x.substring(var1x.indexOf(": ") + 2));
            var3.m = var3x ? "World" : "Shader";
            if (var2x) {
               ChooseASkyEffectAndAdjustItsAppearanceModule.this.j(var3x);
            }

            q8.G("Sky", "Selected {}; enabled={}", var1x, var2x);
         }
      };
   }

   private static com.larpclient.module.lw U(String var0, Module var1) {
      com.larpclient.module.lw var2 = new com.larpclient.module.lw(
         var0,
         "Retains the original sky shortcut. Takes a key or mouse button; for left or right click, click this field again. Escape/Delete clears it",
         var1.getKeyBind()
      );
      var2.a(var1::setKeyBind);
      return var2;
   }

   private String d() {
      if (this.r.isEnabled()) {
         this.E.m = "World";
      } else if (this.h.isEnabled()) {
         this.E.m = "Shader";
      }

      return this.E.m;
   }

   private void j(boolean var1) {
      if (var1) {
         this.h.disable();
         this.r.enable();
      } else {
         this.r.disable();
         this.h.enable();
      }
   }

   public com.larpclient.module.li s() {
      return this.Y;
   }

   public List<com.larpclient.module.lb<?>> B() {
      return List.of(U("Classic sky keybind", this.r), U("Larp sky keybind", this.h));
   }

   public Module H() {
      return this.d().equals("World") ? this.r : this.h;
   }

   public List<com.larpclient.module.lb<?>> i() {
      Module var1 = this.d().equals("World") ? this.r : this.h;
      com.larpclient.module.li var2 = var1 == this.r ? this.r.r() : this.h.i();
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
   public List<com.larpclient.module.lb<?>> getSettings() {
      ArrayList var1 = new ArrayList<>(this.r.getSettings());
      var1.addAll(this.h.getSettings());
      return var1;
   }

   @Override
   public boolean isEnabled() {
      return this.r.isEnabled() || this.h.isEnabled();
   }

   @Override
   public boolean isOperational() {
      return !this.isEnabled() ? false : this.r.isOperational() || this.h.isOperational();
   }

   @Override
   public boolean isRuntimeActive() {
      return !this.isEnabled() ? false : this.r.isRuntimeActive() || this.h.isRuntimeActive();
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
      this.j(this.d().equals("World"));
   }

   @Override
   public void disable() {
      this.d();
      this.r.disable();
      this.h.disable();
   }

   public static List<Module> C(List<Module> var0, y0 var1) {
      if (M != null && M.E == var1 && u.equals(var0)) {
         return k;
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
         if (M == null || M.r != var2 || M.h != var3 || M.E != var1) {
            M = new ChooseASkyEffectAndAdjustItsAppearanceModule(var2, var3, var1);
         }

         ArrayList var7 = new ArrayList(var0.size() - 1);

         for (Module var10 : var0) {
            if (var10 == var2) {
               var7.add(M);
            } else if (var10 != var3) {
               var7.add(var10);
            }
         }

         u = List.copyOf(var0);
         k = List.copyOf(var7);
         return k;
      } else {
         return var0;
      }
   }

   public static Module z(Module var0, List<Module> var1, y0 var2) {
      return !(var0 instanceof CustomSkyModule) && !(var0 instanceof SkyShaderModule)
         ? var0
         : C(var1, var2).stream().filter(var0x -> var0x instanceof ChooseASkyEffectAndAdjustItsAppearanceModule).findFirst().orElse(var0);
   }
}

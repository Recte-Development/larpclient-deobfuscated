package com.larpclient.module;

import com.larpclient.q8;
import com.larpclient.qj;
import com.larpclient.s5;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.class_310;
import net.minecraft.class_746;

public abstract class Module {
   private final String name;
   private final String description;
   private final Category category;
   private int keyBind;
   private boolean enabled;
   private boolean pendingEnable;
   private boolean heldByBind;
   private li keybindMode;
   private final List<lb<?>> settings = new ArrayList<>();
   protected static class_310 mc = null;

   protected static class_310 getMc() {
      if (mc == null) {
         mc = class_310.method_1551();
      }

      return mc;
   }

   public Module(String var1, String var2, Category var3, int var4) {
      this.name = var1;
      this.description = var2;
      this.category = var3;
      this.keyBind = var4;
      this.enabled = (boolean)0;
   }

   public void toggle() {
      if (this.enabled) {
         this.disable();
      } else {
         this.enable();
      }
   }

   public void enable() {
      if (!this.enabled) {
         this.enabled = (boolean)1;
         this.pendingEnable = (boolean)1;
         this.activatePending();
         if (this.enabled) {
            q8.G("Module", "Enabled {}", this.name);
         }
      }
   }

   public void activatePending() {
      if (this.enabled && this.pendingEnable) {
         boolean var1 = this.isOperational();
         int var2 = var1 && this.isInGame() ? 1 : 0;
         int var3 = var2 == 0 || !getMc().field_1724.method_5805() && !this.canActivateWhileDead() ? 0 : 1;
         int var4 = (this.enabled ? 1 : 0) | (this.pendingEnable ? 2 : 0) | (var1 ? 4 : 0) | (var2 != 0 ? 8 : 0) | (var3 != 0 ? 16 : 0);
         int var10000 = Integer.compare(var4, 31);
         if (((var10000 | -var10000) >>> 31 ^ 1) != 0) {
            this.invokeActivation();
         }
      }
   }

   private void invokeActivation() {
      this.pendingEnable = (boolean)0;

      try {
         this.onEnable();
      } catch (RuntimeException var4) {
         this.enabled = (boolean)0;

         try {
            this.onDisable();
         } catch (RuntimeException var3) {
            var4.addSuppressed(var3);
         }

         q8.u("Module", "Enable failed: " + this.name, var4);
      }
   }

   public void disable() {
      this.heldByBind = (boolean)0;
      if (this.enabled) {
         this.enabled = (boolean)0;

         try {
            if (!this.pendingEnable) {
               this.onDisable();
            }
         } catch (RuntimeException var2) {
            q8.u("Module", "Disable failed: " + this.name, var2);
         }

         this.pendingEnable = (boolean)0;
         q8.G("Module", "Disabled {}", this.name);
      }
   }

   public void suspendForDisconnect() {
      if (this.heldByBind) {
         this.disable();
      } else if (this.enabled && !this.pendingEnable) {
         try {
            this.onDisable();
         } catch (RuntimeException var2) {
            q8.u("Module", "Disconnect cleanup failed: " + this.name, var2);
         }

         this.pendingEnable = (boolean)1;
      }
   }

   protected boolean canActivateWhileDead() {
      return false;
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void onTick() {
   }

   public boolean allowsScreenInteractions() {
      return false;
   }

   public boolean allowsDetachedCamera() {
      return false;
   }

   public Module.p getTickPhase() {
      return Module.p.v;
   }

   protected <T extends lb<?>> T addSetting(T var1) {
      this.settings.add(var1);
      return (T)var1;
   }

   public List<lb<?>> getSettings() {
      return this.settings;
   }

   public boolean hasSettings() {
      return !this.settings.isEmpty();
   }

   protected class_746 getPlayer() {
      class_310 var1 = getMc();
      return var1 != null ? var1.field_1724 : null;
   }

   protected boolean isInGame() {
      class_310 var1 = getMc();
      return var1 != null && var1.field_1724 != null && var1.field_1687 != null;
   }

   public String getName() {
      return this.name;
   }

   public String getDisplayName() {
      return this.name;
   }

   public List<String> getSearchAliases() {
      return List.of();
   }

   public String getDescription() {
      return this.description;
   }

   public Category getCategory() {
      return this.category;
   }

   public int getKeyBind() {
      return this.keyBind;
   }

   public void setKeyBind(int var1) {
      if (var1 != this.keyBind) {
         this.endHeldBind("Keybind changed");
      }

      this.keyBind = var1;
   }

   public final void offerHoldKeybind() {
      if (this.keybindMode() == null) {
         this.keybindMode = this.addSetting(
            new li("Keybind Mode", "Toggle switches the module with each press; Hold keeps it on only while the key is held", "Toggle", "Toggle", "Hold")
         );
         this.keybindMode.a(var1 -> this.endHeldBind("Keybind Mode changed"));
      }
   }

   public li keybindMode() {
      return this.keybindMode;
   }

   public boolean holdsKeybind() {
      return this.keybindMode != null && this.keybindMode.G("Hold");
   }

   public boolean activateHeldBind() {
      class_310 var1 = getMc();
      if (this.holdsKeybind() && var1 != null && var1.method_22683() != null && s5.m(var1.method_22683().method_4490(), this.keyBind)) {
         boolean var2 = this.enabled;
         this.heldByBind = (boolean)1;
         if (var2) {
            q8.v("Input", "{} shortcut now holds the enabled module", this.name);
            return false;
         }

         this.enable();
         if (!this.enabled) {
            this.heldByBind = (boolean)0;
         }

         return this.enabled;
      } else {
         return false;
      }
   }

   public void validateHeldBind() {
      if (this.heldByBind) {
         if (!this.enabled) {
            this.heldByBind = (boolean)0;
         } else {
            class_310 var1 = getMc();
            String var2 = null;
            if (!this.holdsKeybind()) {
               var2 = "Keybind Mode changed";
            } else if (var1.field_1724 == null || var1.field_1687 == null || !var1.field_1724.method_5805()) {
               var2 = "no live player";
            } else if (var1.field_1755 != null || var1.method_18506() != null) {
               var2 = "screen opened";
            } else if (!var1.method_1569()) {
               var2 = "window lost focus";
            } else if (!s5.m(var1.method_22683().method_4490(), this.keyBind)) {
               var2 = "key released";
            }

            if (var2 != null) {
               this.endHeldBind(var2);
            }
         }
      }
   }

   private void endHeldBind(String var1) {
      if (this.heldByBind) {
         q8.v("Input", "{} hold ended: {}", this.name, var1);
         this.disable();
      }
   }

   public boolean isEnabledPreference() {
      return this.enabled && !this.heldByBind;
   }

   public boolean isRuntimeActive() {
      return !this.isEnabled() ? false : this.isOperational() && !this.pendingEnable;
   }

   public boolean isOperational() {
      return !this.isEnabled() ? false : this.enabled && !lf.Y(this) && !qj.X(this);
   }

   public String getStatus() {
      return "";
   }

   public Set<l5> getCurrentNetworkImpacts() {
      return this.getNetworkImpacts();
   }

   public Set<l5> getNetworkImpacts() {
      return Set.of();
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      if (var1) {
         this.enable();
      } else {
         this.disable();
      }
   }

   public enum p {
      g,
      P,
      v,
      s;
   }
}

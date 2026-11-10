package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.jj;
import com.larpclient.p4;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public abstract class Module {
   private final String name;
   private final String description;
   private final Category category;
   private int keyBind;
   private boolean enabled;
   private boolean pendingEnable;
   private boolean heldByBind;
   private o keybindMode;
   private final List<r<?>> settings = new ArrayList<>();
   protected static Minecraft mc = null;

   protected static Minecraft getMc() {
      if (mc == null) {
         mc = Minecraft.getInstance();
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
            _q.z("Module", "Enabled {}", this.name);
         }
      }
   }

   public void activatePending() {
      if (this.enabled && this.pendingEnable) {
         boolean var1 = this.isOperational();
         int var2 = var1 && this.isInGame() ? 1 : 0;
         int var3 = var2 == 0 || !getMc().player.isAlive() && !this.canActivateWhileDead() ? 0 : 1;
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

         _q.o("Module", "Enable failed: " + this.name, var4);
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
            _q.o("Module", "Disable failed: " + this.name, var2);
         }

         this.pendingEnable = (boolean)0;
         _q.z("Module", "Disabled {}", this.name);
      }
   }

   public void suspendForDisconnect() {
      if (this.heldByBind) {
         this.disable();
      } else if (this.enabled && !this.pendingEnable) {
         try {
            this.onDisable();
         } catch (RuntimeException var2) {
            _q.o("Module", "Disconnect cleanup failed: " + this.name, var2);
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

   public Module.a getTickPhase() {
      return Module.a.O;
   }

   protected <T extends r<?>> T addSetting(T var1) {
      this.settings.add(var1);
      return (T)var1;
   }

   public List<r<?>> getSettings() {
      return this.settings;
   }

   public boolean hasSettings() {
      return !this.settings.isEmpty();
   }

   protected LocalPlayer getPlayer() {
      Minecraft var1 = getMc();
      return var1 != null ? var1.player : null;
   }

   protected boolean isInGame() {
      Minecraft var1 = getMc();
      return var1 != null && var1.player != null && var1.level != null;
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
            new o("Keybind Mode", "Toggle switches the module with each press; Hold keeps it on only while the key is held", "Toggle", "Toggle", "Hold")
         );
         this.keybindMode.w(var1 -> this.endHeldBind("Keybind Mode changed"));
      }
   }

   public o keybindMode() {
      return this.keybindMode;
   }

   public boolean holdsKeybind() {
      return this.keybindMode != null && this.keybindMode.w("Hold");
   }

   public boolean activateHeldBind() {
      Minecraft var1 = getMc();
      if (this.holdsKeybind() && var1 != null && var1.getWindow() != null && p4.Y(var1.getWindow().handle(), this.keyBind)) {
         boolean var2 = this.enabled;
         this.heldByBind = (boolean)1;
         if (var2) {
            _q.c("Input", "{} shortcut now holds the enabled module", this.name);
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
            Minecraft var1 = getMc();
            String var2 = null;
            if (!this.holdsKeybind()) {
               var2 = "Keybind Mode changed";
            } else if (var1.player == null || var1.level == null || !var1.player.isAlive()) {
               var2 = "no live player";
            } else if (var1.gui.screen() != null || var1.gui.overlay() != null) {
               var2 = "screen opened";
            } else if (!var1.isWindowActive()) {
               var2 = "window lost focus";
            } else if (!p4.Y(var1.getWindow().handle(), this.keyBind)) {
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
         _q.c("Input", "{} hold ended: {}", this.name, var1);
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
      return !this.isEnabled() ? false : this.enabled && !fj.r(this) && !jj.T(this);
   }

   public String getStatus() {
      return "";
   }

   public Set<l> getCurrentNetworkImpacts() {
      return this.getNetworkImpacts();
   }

   public Set<l> getNetworkImpacts() {
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

   public enum a {
      n,
      T,
      O,
      N;
   }
}

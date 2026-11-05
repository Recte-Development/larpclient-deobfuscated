package com.larpclient.module;

import com.larpclient.r4;
import com.larpclient.r8;
import java.util.List;
import java.util.Set;
import net.minecraft.network.chat.Component;

public final class HomeMacroModule extends Module {
   private final lc y = this.addSetting(new lc("Home Slot", "Numbered home to delete and set again", 1.0, 1.0, 5.0, 1.0));
   private final ln l = this.addSetting(new ln("Chat Feedback", "Confirm when the replacement command is sent", true));
   private Object O;
   private Object S;
   private Object x;
   private final r8.i G = new r8.i();
   private int q;

   public HomeMacroModule() {
      super("Home Macro", "Delete a numbered home, then set it again after one second", Category.PLAYER, -1);
   }

   @Override
   public String getDisplayName() {
      return "Home Macro";
   }

   public List<l6<?>> D() {
      return List.of(this.y, this.l);
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   @Override
   public String getStatus() {
      return this.G.l() ? "Replacing Home " + this.q : "";
   }

   @Override
   public void onEnable() {
      this.onDisable();
      if (this.isInGame() && mc.player.connection != null) {
         this.O = mc.player;
         this.S = mc.level;
         this.x = mc.player.connection;
         this.q = this.y.getValue().intValue();
         this.G.H(this.q, System.nanoTime());
         mc.player.connection.sendCommand("delhome " + this.q);
         r4.g("HomeMacro", "Delete sent for home {}; replacement waits one second", this.q);
      } else {
         this.disable();
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.G.l();
      boolean var3 = false;
      byte var2 = (byte)var10000;
      var10000 = Integer.signum(var2);
      if ((var10000 | -var10000) >>> 31 != 0) {
         if (this.isInGame() && mc.player == this.O && mc.level == this.S && mc.player.connection == this.x) {
            int var11 = mc.player.isAlive();
            boolean var7 = false;
            byte var6 = (byte)var11;
            var11 = Integer.signum(var6);
            if ((var11 | -var11) >>> 31 != 0) {
               int var1 = this.G.b(System.nanoTime());
               boolean var5 = false;
               int var4 = var1;
               var11 = Integer.signum(var4);
               if ((var11 | -var11) >>> 31 == 0) {
                  return;
               }

               mc.player.connection.sendCommand("sethome " + this.q);
               int var14 = this.l.getValue();
               boolean var9 = false;
               byte var8 = (byte)var14;
               var14 = Integer.signum(var8);
               if (((var14 | -var14) >>> 31 ^ 1) == 0) {
                  mc.gui.chatListener().handleSystemMessage(Component.literal("[LARP] Home " + this.q + " replacement command sent."), false);
               }

               r4.g("HomeMacro", "Replacement command sent for home {}", this.q);
               this.disable();
               return;
            }
         }

         r4.g("HomeMacro", "Replacement cancelled because the active session changed");
         this.disable();
      }
   }

   @Override
   public void onDisable() {
      this.G.k();
      this.q = 0;
      this.O = this.S = this.x = null;
   }
}

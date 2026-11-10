package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.s7;
import java.util.List;
import java.util.Set;
import net.minecraft.network.chat.Component;

public final class HomeMacroModule extends Module {
   private final b S = this.addSetting(new b("Home Slot", "Numbered home to delete and set again", 1.0, 1.0, 5.0, 1.0));
   private final m H = this.addSetting(new m("Chat Feedback", "Confirm when the replacement command is sent", true));
   private Object G;
   private Object s;
   private Object P;
   private final s7.g n = new s7.g();
   private int C;

   public HomeMacroModule() {
      super("Home Macro", "Delete a numbered home, then set it again after one second", Category.PLAYER, -1);
   }

   @Override
   public String getDisplayName() {
      return "Home Macro";
   }

   public List<r<?>> u() {
      return List.of(this.S, this.H);
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   @Override
   public String getStatus() {
      return this.n.H() ? "Replacing Home " + this.C : "";
   }

   @Override
   public void onEnable() {
      this.onDisable();
      if (this.isInGame() && mc.player.connection != null) {
         this.G = mc.player;
         this.s = mc.level;
         this.P = mc.player.connection;
         this.C = this.S.getValue().intValue();
         this.n.i(this.C, System.nanoTime());
         mc.player.connection.sendCommand("delhome " + this.C);
         _q.c("HomeMacro", "Delete sent for home {}; replacement waits one second", this.C);
      } else {
         this.disable();
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.n.H();
      boolean var3 = false;
      byte var2 = (byte)var10000;
      var10000 = Integer.signum(var2);
      if ((var10000 | -var10000) >>> 31 != 0) {
         if (this.isInGame() && mc.player == this.G && mc.level == this.s && mc.player.connection == this.P) {
            int var11 = mc.player.isAlive();
            boolean var9 = false;
            byte var8 = (byte)var11;
            var11 = Integer.signum(var8);
            if ((var11 | -var11) >>> 31 != 0) {
               int var1 = this.n.r(System.nanoTime());
               boolean var7 = false;
               int var6 = var1;
               var11 = Integer.signum(var6);
               if ((var11 | -var11) >>> 31 == 0) {
                  return;
               }

               mc.player.connection.sendCommand("sethome " + this.C);
               int var14 = this.H.getValue();
               boolean var5 = false;
               byte var4 = (byte)var14;
               var14 = Integer.signum(var4);
               if (((var14 | -var14) >>> 31 ^ 1) == 0) {
                  mc.gui.chatListener().handleSystemMessage(Component.literal("[LARP] Home " + this.C + " replacement command sent."), false);
               }

               _q.c("HomeMacro", "Replacement command sent for home {}", this.C);
               this.disable();
               return;
            }
         }

         _q.c("HomeMacro", "Replacement cancelled because the active session changed");
         this.disable();
      }
   }

   @Override
   public void onDisable() {
      this.n.W();
      this.C = 0;
      this.G = this.s = this.P = null;
   }
}

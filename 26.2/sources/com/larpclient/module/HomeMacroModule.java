package com.larpclient.module;

import com.larpclient.w4;
import java.util.List;
import java.util.Set;
import net.minecraft.network.chat.Component;

public final class HomeMacroModule extends Module {
   private final e o = this.addSetting(new e("Home Slot", "Numbered home to delete and set again", 1.0, 1.0, 5.0, 1.0));
   private final s N = this.addSetting(new s("Chat Feedback", "Confirm when the replacement command is sent", true));
   private Object m;
   private Object M;
   private Object K;
   private final w4.q O = new w4.q();
   private int p;

   public HomeMacroModule() {
      super("Home Macro", "Delete a numbered home, then set it again after one second", Category.PLAYER, -1);
   }

   @Override
   public String getDisplayName() {
      return "Home Macro";
   }

   public List<q<?>> n() {
      return List.of(this.o, this.N);
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.U);
   }

   @Override
   public String getStatus() {
      return this.O.Y() ? "Replacing Home " + this.p : "";
   }

   @Override
   public void onEnable() {
      this.onDisable();
      if (this.isInGame() && mc.player.connection != null) {
         this.m = mc.player;
         this.M = mc.level;
         this.K = mc.player.connection;
         this.p = this.o.getValue().intValue();
         this.O.B(this.p, System.nanoTime());
         mc.player.connection.sendCommand("delhome " + this.p);
         com.larpclient.h.U("HomeMacro", "Delete sent for home {}; replacement waits one second", this.p);
      } else {
         this.disable();
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.O.Y();
      boolean var3 = false;
      byte var2 = (byte)var10000;
      var10000 = Integer.signum(var2);
      if ((var10000 | -var10000) >>> 31 != 0) {
         if (this.isInGame() && mc.player == this.m && mc.level == this.M && mc.player.connection == this.K) {
            int var11 = mc.player.isAlive();
            boolean var9 = false;
            byte var8 = (byte)var11;
            var11 = Integer.signum(var8);
            if ((var11 | -var11) >>> 31 != 0) {
               int var1 = this.O.s(System.nanoTime());
               boolean var5 = false;
               int var4 = var1;
               var11 = Integer.signum(var4);
               if ((var11 | -var11) >>> 31 == 0) {
                  return;
               }

               mc.player.connection.sendCommand("sethome " + this.p);
               int var14 = this.N.getValue();
               boolean var7 = false;
               byte var6 = (byte)var14;
               var14 = Integer.signum(var6);
               if (((var14 | -var14) >>> 31 ^ 1) == 0) {
                  mc.gui.chatListener().handleSystemMessage(Component.literal("[LARP] Home " + this.p + " replacement command sent."), false);
               }

               com.larpclient.h.U("HomeMacro", "Replacement command sent for home {}", this.p);
               this.disable();
               return;
            }
         }

         com.larpclient.h.U("HomeMacro", "Replacement cancelled because the active session changed");
         this.disable();
      }
   }

   @Override
   public void onDisable() {
      this.O.u();
      this.p = 0;
      this.m = this.M = this.K = null;
   }
}

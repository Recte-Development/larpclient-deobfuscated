package com.larpclient.module;

import com.larpclient.k7;
import com.larpclient.mt;
import java.util.List;
import java.util.Set;
import net.minecraft.network.chat.Component;

public final class HomeMacroModule extends Module {
   private final d9 V = this.addSetting(new d9("Home Slot", "Numbered home to delete and set again", 1.0, 1.0, 5.0, 1.0));
   private final dh Q = this.addSetting(new dh("Chat Feedback", "Confirm when the replacement command is sent", true));
   private Object a;
   private Object U;
   private Object u;
   private final k7.u r = new k7.u();
   private int A;

   public HomeMacroModule() {
      super("Home Macro", "Delete a numbered home, then set it again after one second", Category.PLAYER, -1);
   }

   @Override
   public String getDisplayName() {
      return "Home Macro";
   }

   public List<df<?>> v() {
      return List.of(this.V, this.Q);
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   @Override
   public String getStatus() {
      return this.r.Y() ? "Replacing Home " + this.A : "";
   }

   @Override
   public void onEnable() {
      this.onDisable();
      if (this.isInGame() && mc.player.connection != null) {
         this.a = mc.player;
         this.U = mc.level;
         this.u = mc.player.connection;
         this.A = this.V.P().intValue();
         this.r.L(this.A, System.nanoTime());
         mc.player.connection.sendCommand("delhome " + this.A);
         mt.U("HomeMacro", "Delete sent for home {}; replacement waits one second", this.A);
      } else {
         this.disable();
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.r.Y();
      boolean var3 = false;
      byte var2 = (byte)var10000;
      var10000 = Integer.signum(var2);
      if ((var10000 | -var10000) >>> 31 != 0) {
         if (this.isInGame() && mc.player == this.a && mc.level == this.U && mc.player.connection == this.u) {
            int var11 = mc.player.isAlive();
            boolean var9 = false;
            byte var8 = (byte)var11;
            var11 = Integer.signum(var8);
            if ((var11 | -var11) >>> 31 != 0) {
               int var1 = this.r.a(System.nanoTime());
               boolean var5 = false;
               int var4 = var1;
               var11 = Integer.signum(var4);
               if ((var11 | -var11) >>> 31 == 0) {
                  return;
               }

               mc.player.connection.sendCommand("sethome " + this.A);
               int var14 = this.Q.P();
               boolean var7 = false;
               byte var6 = (byte)var14;
               var14 = Integer.signum(var6);
               if (((var14 | -var14) >>> 31 ^ 1) == 0) {
                  mc.gui.chatListener().handleSystemMessage(Component.literal("[LARP] Home " + this.A + " replacement command sent."), false);
               }

               mt.U("HomeMacro", "Replacement command sent for home {}", this.A);
               this.disable();
               return;
            }
         }

         mt.U("HomeMacro", "Replacement cancelled because the active session changed");
         this.disable();
      }
   }

   @Override
   public void onDisable() {
      this.r.Z();
      this.A = 0;
      this.a = this.U = this.u = null;
   }
}

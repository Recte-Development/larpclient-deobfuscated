package com.larpclient.module;

import com.larpclient.q8;
import com.larpclient.uk;
import java.util.List;
import java.util.Set;
import net.minecraft.class_2561;

public final class HomeMacroModule extends Module {
   private final lo O = this.addSetting(new lo("Home Slot", "Numbered home to delete and set again", 1.0, 1.0, 5.0, 1.0));
   private final l3 m = this.addSetting(new l3("Chat Feedback", "Confirm when the replacement command is sent", true));
   private Object T;
   private Object E;
   private Object y;
   private final uk.t K = new uk.t();
   private int s;

   public HomeMacroModule() {
      super("Home Macro", "Delete a numbered home, then set it again after one second", Category.PLAYER, -1);
   }

   @Override
   public String getDisplayName() {
      return "Home Macro";
   }

   public List<lb<?>> p() {
      return List.of(this.O, this.m);
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public String getStatus() {
      return this.K.v() ? "Replacing Home " + this.s : "";
   }

   @Override
   public void onEnable() {
      this.onDisable();
      if (this.isInGame() && mc.field_1724.field_3944 != null) {
         this.T = mc.field_1724;
         this.E = mc.field_1687;
         this.y = mc.field_1724.field_3944;
         this.s = this.O.getValue().intValue();
         this.K.S(this.s, System.nanoTime());
         mc.field_1724.field_3944.method_45730("delhome " + this.s);
         q8.v("HomeMacro", "Delete sent for home {}; replacement waits one second", this.s);
      } else {
         this.disable();
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.K.v();
      boolean var3 = false;
      byte var2 = (byte)var10000;
      var10000 = Integer.signum(var2);
      if ((var10000 | -var10000) >>> 31 != 0) {
         if (this.isInGame() && mc.field_1724 == this.T && mc.field_1687 == this.E && mc.field_1724.field_3944 == this.y) {
            int var11 = mc.field_1724.method_5805();
            boolean var9 = false;
            byte var8 = (byte)var11;
            var11 = Integer.signum(var8);
            if ((var11 | -var11) >>> 31 != 0) {
               int var1 = this.K.k(System.nanoTime());
               boolean var7 = false;
               int var6 = var1;
               var11 = Integer.signum(var6);
               if ((var11 | -var11) >>> 31 == 0) {
                  return;
               }

               mc.field_1724.field_3944.method_45730("sethome " + this.s);
               int var14 = this.m.getValue();
               boolean var5 = false;
               byte var4 = (byte)var14;
               var14 = Integer.signum(var4);
               if (((var14 | -var14) >>> 31 ^ 1) == 0) {
                  mc.method_44714().method_44736(class_2561.method_43470("[LARP] Home " + this.s + " replacement command sent."), false);
               }

               q8.v("HomeMacro", "Replacement command sent for home {}", this.s);
               this.disable();
               return;
            }
         }

         q8.v("HomeMacro", "Replacement cancelled because the active session changed");
         this.disable();
      }
   }

   @Override
   public void onDisable() {
      this.K.K();
      this.s = 0;
      this.T = this.E = this.y = null;
   }
}

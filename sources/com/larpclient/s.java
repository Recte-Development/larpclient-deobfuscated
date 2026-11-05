package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import java.util.Set;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

abstract class s extends Module {
   private String R = "Waiting";

   s(String var1, String var2) {
      super(var1, var2, Category.PLAYER, -1);
   }

   final com.larpclient.module.ln S(String var1, String var2, boolean var3) {
      return this.addSetting(new com.larpclient.module.ln(var1, var2, var3));
   }

   final com.larpclient.module.lc H(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new com.larpclient.module.lc(var1, var2, var3, var5, var7, var9));
   }

   final boolean G() {
      if (!this.isEnabled()) {
         return false;
      }

      getMc();
      return this.isRuntimeActive()
         && d8.r(this)
         && mc.gui.overlay() == null
         && mc.player.connection.hasClientLoaded()
         && (this.allowsScreenInteractions() || mc.isWindowActive())
         && !mc.player.isUsingItem()
         && (!mc.isWindowActive() || mc.gui.screen() != null || !mc.options.keyAttack.isDown());
   }

   void B(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         float[] var3 = ep.g(mc.player.getEyePosition(), var1);
         double var4 = Math.max(Math.abs(Mth.wrapDegrees(var3[0] - mc.player.getYRot())), Math.abs(var3[1] - mc.player.getXRot()));
         ep.t(this, ep.z.d, var3[0], var3[1], var4 <= var2 ? 1.0 : var2 / var4);
      }
   }

   final void I(String var1) {
      if (!var1.equals(this.R)) {
         this.R = var1;
         r4.g(this.getName(), "State: {}", var1);
      }
   }

   @Override
   public String getStatus() {
      return this.R;
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.w;
   }

   @Override
   public Set<com.larpclient.module.ld> getNetworkImpacts() {
      return Set.of(com.larpclient.module.ld.c);
   }

   @Override
   public void onEnable() {
      getMc();
      this.I("Waiting");
      r4.V(
         this.getName(),
         this.allowsScreenInteractions()
            ? "World automation enabled; mining continues in screens and while unfocused; manual movement, mining, item use and freecam yield control"
            : "World automation enabled; manual mining, item use, screens and focus loss pause actions"
      );
   }

   @Override
   public void onDisable() {
      d8.K(this);
      yx.H(this);
      this.I("Waiting");
   }
}

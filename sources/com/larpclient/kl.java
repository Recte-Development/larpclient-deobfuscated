package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import com.larpclient.module.dh;
import com.larpclient.module.dm;
import java.util.Set;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

abstract class kl extends Module {
   private String l = "Waiting";

   kl(String var1, String var2) {
      super(var1, var2, Category.PLAYER, -1);
   }

   final dh Y(String var1, String var2, boolean var3) {
      return this.addSetting(new dh(var1, var2, var3));
   }

   final com.larpclient.module.d9 H(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new com.larpclient.module.d9(var1, var2, var3, var5, var7, var9));
   }

   final boolean N() {
      if (!this.isEnabled()) {
         return false;
      }

      getMc();
      return this.isRuntimeActive()
         && _m.n(this)
         && mc.gui.overlay() == null
         && mc.player.connection.hasClientLoaded()
         && (this.allowsScreenInteractions() || mc.isWindowActive())
         && !mc.player.isUsingItem()
         && (!mc.isWindowActive() || mc.gui.screen() != null || !mc.options.keyAttack.isDown());
   }

   void aimAt(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         float[] var3 = m9.Z(mc.player.getEyePosition(), var1);
         double var4 = Math.max(Math.abs(Mth.wrapDegrees(var3[0] - mc.player.getYRot())), Math.abs(var3[1] - mc.player.getXRot()));
         m9.k(this, m9.s.p, var3[0], var3[1], var4 <= var2 ? 1.0 : var2 / var4);
      }
   }

   final void w(String var1) {
      if (!var1.equals(this.l)) {
         this.l = var1;
         mt.U(this.getName(), "State: {}", var1);
      }
   }

   @Override
   public String getStatus() {
      return this.l;
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.Z;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   @Override
   public void onEnable() {
      getMc();
      this.w("Waiting");
      mt.K(
         this.getName(),
         this.allowsScreenInteractions()
            ? "World automation enabled; mining continues in screens and while unfocused; manual movement, mining, item use and freecam yield control"
            : "World automation enabled; manual mining, item use, screens and focus loss pause actions"
      );
   }

   @Override
   public void onDisable() {
      _m.w(this);
      wa.N(this);
      this.w("Waiting");
   }
}

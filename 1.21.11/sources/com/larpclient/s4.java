package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import java.util.Set;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_437;

public abstract class s4 extends Module {
   private String P = "Waiting";
   private Object t;
   private long s;
   private int y;
   private int G;

   protected s4(String var1, String var2) {
      super(var1, var2, Category.PLAYER, -1);
   }

   protected final com.larpclient.module.l3 G(String var1, String var2, boolean var3) {
      return this.addSetting(new com.larpclient.module.l3(var1, var2, var3));
   }

   protected final com.larpclient.module.lo Z(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new com.larpclient.module.lo(var1, var2, var3, var5, var7, var9));
   }

   public final boolean r() {
      return !this.isEnabled() ? false : this.m() == null && cr.o(this);
   }

   private boolean a() {
      long var1 = mc.field_1687.method_75260();
      if (this.t != mc.field_1724) {
         this.t = mc.field_1724;
         this.s = var1;
         this.y = mc.field_1724.field_6012;
         this.G = 0;
      } else if (var1 != this.s) {
         this.G = mc.field_1724.field_6012 == this.y ? this.G + 1 : 0;
         this.s = var1;
         this.y = mc.field_1724.field_6012;
      }

      return this.G < 2;
   }

   protected final String m() {
      getMc();
      if (!this.isRuntimeActive()) {
         return "Waiting to start";
      } else if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1761 == null) {
         return "Waiting for the world";
      } else if (!mc.field_1724.method_5805()) {
         return "Waiting to respawn";
      } else if (mc.field_1724.method_7325()) {
         return "Paused in spectator mode";
      } else {
         class_437 var1 = mc.field_1755;
         if (!cr.l(var1 != null, this)) {
            return "Paused while a screen is open";
         } else if (wp.d()) {
            return "Paused while Freecam is on";
         } else if (mc.method_18506() != null) {
            return "Waiting for the loading screen";
         } else if (!mc.field_1724.field_3944.method_76760() || !this.a()) {
            return "Waiting for the world to load";
         } else if (!this.allowsScreenInteractions() && !mc.method_1569()) {
            return "Paused while the game is unfocused";
         } else if (mc.field_1724.method_6115()) {
            return "Paused while an item is in use";
         } else {
            return mc.method_1569() && var1 == null && mc.field_1690.field_1886.method_1434() ? "Paused while you hold attack" : null;
         }
      }
   }

   protected boolean holdsToolBetweenBlocks() {
      return false;
   }

   public boolean aimsAtOpenFaces(class_2338 var1) {
      return false;
   }

   public boolean aimFaceAllowed(class_2338 var1, class_2350 var2) {
      return true;
   }

   public boolean aimsAtFaceCentre(class_2338 var1) {
      return false;
   }

   public void digFinished(class_2338 var1) {
   }

   protected void aimAt(class_243 var1, float var2) {
      if (this.isEnabled()) {
         float[] var3 = t9.X(mc.field_1724.method_33571(), var1);
         double var4 = Math.max(Math.abs(class_3532.method_15393(var3[0] - mc.field_1724.method_36454())), Math.abs(var3[1] - mc.field_1724.method_36455()));
         t9.O(this, t9.v.j, var3[0], var3[1], var4 <= var2 ? 1.0 : var2 / var4);
      }
   }

   protected final void m(String var1) {
      if (!var1.equals(this.P)) {
         this.P = var1;
         q8.v(this.getName(), "State: {}", var1);
      }
   }

   @Override
   public String getStatus() {
      return this.P;
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.P;
   }

   @Override
   public Set<com.larpclient.module.l5> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l5.F);
   }

   @Override
   public void onEnable() {
      getMc();
      this.m("Waiting");
      q8.G(
         this.getName(),
         this.allowsScreenInteractions()
            ? "World automation enabled; mining continues in screens and while unfocused; manual movement, mining, item use and freecam yield control"
            : "World automation enabled; manual mining, item use, screens and focus loss pause actions"
      );
   }

   @Override
   public void onDisable() {
      cr.p(this);
      bv.i(this);
      this.m("Waiting");
   }
}

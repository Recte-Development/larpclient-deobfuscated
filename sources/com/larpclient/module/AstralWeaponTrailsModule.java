package com.larpclient.module;

import com.larpclient.ea;
import com.larpclient.jk;
import com.larpclient.r4;
import com.larpclient.r6;
import com.larpclient.rv;
import com.larpclient.u2;
import com.larpclient.ym;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class AstralWeaponTrailsModule extends Module implements u2 {
   private static AstralWeaponTrailsModule T;
   private final lp B = this.addSetting(new lp("Style", "Energy ribbon appearance", "Astral", "Astral", "Prismatic", "Void"));
   private final lc S = this.addSetting(new lc("Trail Duration", "Seconds of recent blade motion", 0.28, 0.1, 0.7, 0.02));
   private final lc J = this.addSetting(new lc("Width", "Ribbon span along the actual blade", 0.65, 0.15, 1.0, 0.05));
   private final lc V = this.addSetting(new lc("Intensity", "Body and leading edge brightness", 1.0, 0.2, 2.0, 0.1));
   private final ln C = this.addSetting(new ln("Theme Colors", "Use the current theme palette", true));
   private final lv l = this.addSetting(new lv("Primary Color", "Leading energy color", -8790017));
   private final lv e = this.addSetting(new lv("Secondary Color", "Fading wake color", -9142785));
   private final ln I = this.addSetting(new ln("Bloom", "Soft emissive halo on the native hand ribbon", true));
   private final ln Z = this.addSetting(new ln("Sparks", "Small streaks separating from the swing", true));
   private final ln y = this.addSetting(new ln("First Person", "Show trails on the local rendered weapon", true));
   private final ln i = this.addSetting(new ln("Motion Smoothing", "Curve between captured blade poses", true));
   private Object g;
   private Object a;
   private Vec3 R;
   private int q = -1;
   private int W = -1;
   private boolean N;

   public AstralWeaponTrailsModule() {
      super("Astral Weapon Trails", "Luminous ribbons following the rendered sword or knife", Category.VISUALS, -1);
      T = this;
      this.l.x(() -> !this.C.getValue());
      this.e.x(() -> !this.C.getValue());
      this.B.W(var0 -> {
         ym.s();
         r4.g("WeaponTrails", "Style changed to {}; history reset", var0);
      });
      this.y.W(var0 -> ym.s());
   }

   public static AstralWeaponTrailsModule g() {
      return T;
   }

   public List<l6<?>> p() {
      return List.of(this.B, this.S, this.J, this.V);
   }

   public List<l6<?>> f() {
      return List.of(this.C, this.l, this.e, this.I);
   }

   public List<l6<?>> c() {
      return List.of(this.Z, this.y, this.i);
   }

   public int L() {
      return Math.max(16, com.larpclient.lo.H(64));
   }

   public com.larpclient.le.y r() {
      return new com.larpclient.le.y(
         this.B.getValue(),
         this.S.getValue(),
         this.J.getValue(),
         this.V.getValue(),
         this.C.getValue() ? rv.e : this.l.getValue(),
         this.C.getValue() ? rv.GQ : this.e.getValue(),
         this.I.getValue() && com.larpclient.lo.q != jk.x.i,
         this.Z.getValue(),
         this.i.getValue(),
         com.larpclient.lo.i() ? 3 : 2
      );
   }

   public boolean e(ItemStack var1) {
      if (!this.isEnabled()) {
         return false;
      }

      Minecraft var2 = getMc();
      return this.isRuntimeActive()
         && this.y.getValue()
         && SwordModelsModule.G(var1)
         && var2.player != null
         && var2.level != null
         && var2.player.isAlive()
         && !var2.player.isScoping()
         && var2.options.getCameraType().isFirstPerson();
   }

   @Override
   public String effectName() {
      return "WeaponTrails";
   }

   @Override
   public void onEnable() {
      this.clear();
      r4.V("WeaponTrails", "Enabled {}; native sword layer and articulated knife blade capture, two hands, at most 128 retained samples", this.B.getValue());
   }

   @Override
   public void onDisable() {
      this.clear();
   }

   @Override
   public void tick(Minecraft var1) {
      if (this.isEnabled()) {
         if (!HalloweenModule.p()) {
            if (!this.isRuntimeActive()) {
               this.clear();
            } else {
               boolean var2 = var1.options.getCameraType().isFirstPerson();
               if (this.g != var1.level || this.a != var1.player || this.N != var2 || this.q != jk.v) {
                  this.clear();
                  this.g = var1.level;
                  this.a = var1.player;
                  this.N = var2;
                  this.q = jk.v;
               }

               if (var1.player != null && var1.level != null && var1.player.isAlive() && var2) {
                  Vec3 var3 = var1.player.position();
                  if (this.R != null && this.R.distanceToSqr(var3) > 64.0) {
                     ym.s();
                     r4.g("WeaponTrails", "Cleared blade history after player relocation");
                  }

                  this.R = var3;
                  int var4 = var1.player.getInventory().getSelectedSlot();
                  if (this.W != var4) {
                     ym.t(InteractionHand.MAIN_HAND);
                  }

                  this.W = var4;
                  if (!SwordModelsModule.G(var1.player.getMainHandItem())) {
                     ym.t(InteractionHand.MAIN_HAND);
                  }

                  if (!SwordModelsModule.G(var1.player.getOffhandItem())) {
                     ym.t(InteractionHand.OFF_HAND);
                  }

                  ym.I(ea.t(), this.S.getValue());
               } else {
                  ym.s();
               }
            }
         }
      }
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (!this.isEnabled()) {
         ;
      }
   }

   @Override
   public void render(r6 var1, boolean var2) {
      if (!this.isEnabled()) {
         ;
      }
   }

   @Override
   public boolean glowing() {
      return this.isEnabled() ? false : false;
   }

   @Override
   public void clear() {
      ym.s();
      this.g = this.a = null;
      this.R = null;
      this.W = -1;
   }
}

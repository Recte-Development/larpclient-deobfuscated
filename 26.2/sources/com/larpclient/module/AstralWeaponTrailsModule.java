package com.larpclient.module;

import com.larpclient.cf;
import com.larpclient.ho;
import com.larpclient.kj;
import com.larpclient.tk;
import com.larpclient.wa;
import com.larpclient.z6;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class AstralWeaponTrailsModule extends Module implements tk {
   private static AstralWeaponTrailsModule t;
   private final l j = this.addSetting(new l("Style", "Energy ribbon appearance", "Astral", "Astral", "Prismatic", "Void"));
   private final e w = this.addSetting(new e("Trail Duration", "Seconds of recent blade motion", 0.28, 0.1, 0.7, 0.02));
   private final e f = this.addSetting(new e("Width", "Ribbon span along the actual blade", 0.65, 0.15, 1.0, 0.05));
   private final e A = this.addSetting(new e("Intensity", "Body and leading edge brightness", 1.0, 0.2, 2.0, 0.1));
   private final s c = this.addSetting(new s("Theme Colors", "Use the current theme palette", true));
   private final i N = this.addSetting(new i("Primary Color", "Leading energy color", -8790017));
   private final i o = this.addSetting(new i("Secondary Color", "Fading wake color", -9142785));
   private final s C = this.addSetting(new s("Bloom", "Soft emissive halo on the native hand ribbon", true));
   private final s l = this.addSetting(new s("Sparks", "Small streaks separating from the swing", true));
   private final s X = this.addSetting(new s("First Person", "Show trails on the local rendered weapon", true));
   private final s U = this.addSetting(new s("Motion Smoothing", "Curve between captured blade poses", true));
   private Object k;
   private Object Y;
   private Vec3 M;
   private int T = -1;
   private int P = -1;
   private boolean D;

   public AstralWeaponTrailsModule() {
      super("Astral Weapon Trails", "Luminous ribbons following the rendered sword or knife", Category.VISUALS, -1);
      t = this;
      this.N.P(() -> !this.c.getValue());
      this.o.P(() -> !this.c.getValue());
      this.j.o(var0 -> {
         com.larpclient.w.e();
         com.larpclient.h.U("WeaponTrails", "Style changed to {}; history reset", var0);
      });
      this.X.o(var0 -> com.larpclient.w.e());
   }

   public static AstralWeaponTrailsModule C() {
      return t;
   }

   public List<q<?>> N() {
      return List.of(this.j, this.w, this.f, this.A);
   }

   public List<q<?>> z() {
      return List.of(this.c, this.N, this.o, this.C);
   }

   public List<q<?>> d() {
      return List.of(this.l, this.X, this.U);
   }

   public int j() {
      return Math.max(16, com.larpclient.y.i(64));
   }

   public z6.g e() {
      return new z6.g(
         this.j.getValue(),
         this.w.getValue(),
         this.f.getValue(),
         this.A.getValue(),
         this.c.getValue() ? ho.J : this.N.getValue(),
         this.c.getValue() ? ho.ds : this.o.getValue(),
         this.C.getValue() && com.larpclient.y.m != wa.a.Z,
         this.l.getValue(),
         this.U.getValue(),
         com.larpclient.y.y() ? 3 : 2
      );
   }

   public boolean N(ItemStack var1) {
      if (!this.isEnabled()) {
         return false;
      }

      Minecraft var2 = getMc();
      return this.isRuntimeActive()
         && this.X.getValue()
         && SwordModelsModule.V(var1)
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
      com.larpclient.h.A(
         "WeaponTrails", "Enabled {}; native sword layer and articulated knife blade capture, two hands, at most 128 retained samples", this.j.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.clear();
   }

   @Override
   public void tick(Minecraft var1) {
      if (this.isEnabled()) {
         if (!this.isRuntimeActive()) {
            this.clear();
         } else {
            boolean var2 = var1.options.getCameraType().isFirstPerson();
            if (this.k != var1.level || this.Y != var1.player || this.D != var2 || this.T != wa.s) {
               this.clear();
               this.k = var1.level;
               this.Y = var1.player;
               this.D = var2;
               this.T = wa.s;
            }

            if (var1.player != null && var1.level != null && var1.player.isAlive() && var2) {
               Vec3 var3 = var1.player.position();
               if (this.M != null && this.M.distanceToSqr(var3) > 64.0) {
                  com.larpclient.w.e();
                  com.larpclient.h.U("WeaponTrails", "Cleared blade history after player relocation");
               }

               this.M = var3;
               int var4 = var1.player.getInventory().getSelectedSlot();
               if (this.P != var4) {
                  com.larpclient.w.O(InteractionHand.MAIN_HAND);
               }

               this.P = var4;
               if (!SwordModelsModule.V(var1.player.getMainHandItem())) {
                  com.larpclient.w.O(InteractionHand.MAIN_HAND);
               }

               if (!SwordModelsModule.V(var1.player.getOffhandItem())) {
                  com.larpclient.w.O(InteractionHand.OFF_HAND);
               }

               com.larpclient.w.m(cf.I(), this.w.getValue());
            } else {
               com.larpclient.w.e();
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
   public void render(kj var1, boolean var2) {
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
      com.larpclient.w.e();
      this.k = this.Y = null;
      this.M = null;
      this.P = -1;
   }
}

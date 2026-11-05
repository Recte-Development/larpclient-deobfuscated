package com.larpclient.module;

import com.larpclient._k;
import com.larpclient.ki;
import com.larpclient.m5;
import com.larpclient.mt;
import com.larpclient.ty;
import com.larpclient.x4;
import com.larpclient.xf;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class AstralWeaponTrailsModule extends Module implements com.larpclient.c {
   private static AstralWeaponTrailsModule q;
   private final d6 u = this.addSetting(new d6("Style", "Energy ribbon appearance", "Astral", "Astral", "Prismatic", "Void"));
   private final d9 J = this.addSetting(new d9("Trail Duration", "Seconds of recent blade motion", 0.28, 0.1, 0.7, 0.02));
   private final d9 U = this.addSetting(new d9("Width", "Ribbon span along the actual blade", 0.65, 0.15, 1.0, 0.05));
   private final d9 b = this.addSetting(new d9("Intensity", "Body and leading edge brightness", 1.0, 0.2, 2.0, 0.1));
   private final dh K = this.addSetting(new dh("Theme Colors", "Use the current theme palette", true));
   private final dk j = this.addSetting(new dk("Primary Color", "Leading energy color", -8790017));
   private final dk E = this.addSetting(new dk("Secondary Color", "Fading wake color", -9142785));
   private final dh S = this.addSetting(new dh("Bloom", "Soft emissive halo on the native hand ribbon", true));
   private final dh r = this.addSetting(new dh("Sparks", "Small streaks separating from the swing", true));
   private final dh Z = this.addSetting(new dh("First Person", "Show trails on the local rendered weapon", true));
   private final dh f = this.addSetting(new dh("Motion Smoothing", "Curve between captured blade poses", true));
   private Object k;
   private Object W;
   private Vec3 M;
   private int h = -1;
   private int p = -1;
   private boolean X;

   public AstralWeaponTrailsModule() {
      super("Astral Weapon Trails", "Luminous ribbons following the rendered sword or knife", Category.VISUALS, -1);
      q = this;
      this.j.H(() -> !this.K.P());
      this.E.H(() -> !this.K.P());
      this.u.h(var0 -> {
         x4.D();
         mt.U("WeaponTrails", "Style changed to {}; history reset", var0);
      });
      this.Z.h(var0 -> x4.D());
   }

   public static AstralWeaponTrailsModule s() {
      return q;
   }

   public List<df<?>> f() {
      return List.of(this.u, this.J, this.U, this.b);
   }

   public List<df<?>> u() {
      return List.of(this.K, this.j, this.E, this.S);
   }

   public List<df<?>> b() {
      return List.of(this.r, this.Z, this.f);
   }

   public int A() {
      return Math.max(16, _k.m(64));
   }

   public xf.v e() {
      return new xf.v(
         this.u.P(),
         this.J.P(),
         this.U.P(),
         this.b.P(),
         this.K.P() ? com.larpclient.f.C : this.j.P(),
         this.K.P() ? com.larpclient.f.O : this.E.P(),
         this.S.P() && _k.t != m5.n.N,
         this.r.P(),
         this.f.P(),
         _k.C() ? 3 : 2
      );
   }

   public boolean h(ItemStack var1) {
      if (!this.isEnabled()) {
         return false;
      }

      Minecraft var2 = getMc();
      return this.isRuntimeActive()
         && this.Z.P()
         && SwordModelsModule.q(var1)
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
      mt.K("WeaponTrails", "Enabled {}; native sword layer and articulated knife blade capture, two hands, at most 128 retained samples", this.u.P());
   }

   @Override
   public void onDisable() {
      this.clear();
   }

   @Override
   public void tick(Minecraft var1) {
      if (this.isEnabled()) {
         if (!HalloweenModule.S()) {
            if (!this.isRuntimeActive()) {
               this.clear();
            } else {
               boolean var2 = var1.options.getCameraType().isFirstPerson();
               if (this.k != var1.level || this.W != var1.player || this.X != var2 || this.h != m5.k) {
                  this.clear();
                  this.k = var1.level;
                  this.W = var1.player;
                  this.X = var2;
                  this.h = m5.k;
               }

               if (var1.player != null && var1.level != null && var1.player.isAlive() && var2) {
                  Vec3 var3 = var1.player.position();
                  if (this.M != null && this.M.distanceToSqr(var3) > 64.0) {
                     x4.D();
                     mt.U("WeaponTrails", "Cleared blade history after player relocation");
                  }

                  this.M = var3;
                  int var4 = var1.player.getInventory().getSelectedSlot();
                  if (this.p != var4) {
                     x4.Z(InteractionHand.MAIN_HAND);
                  }

                  this.p = var4;
                  if (!SwordModelsModule.q(var1.player.getMainHandItem())) {
                     x4.Z(InteractionHand.MAIN_HAND);
                  }

                  if (!SwordModelsModule.q(var1.player.getOffhandItem())) {
                     x4.Z(InteractionHand.OFF_HAND);
                  }

                  x4.Q(ty.P(), this.J.P());
               } else {
                  x4.D();
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
   public void render(ki var1, boolean var2) {
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
      x4.D();
      this.k = this.W = null;
      this.M = null;
      this.p = -1;
   }
}

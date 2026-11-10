package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.a4;
import com.larpclient.aj;
import com.larpclient.dh;
import com.larpclient.fz;
import com.larpclient.hz;
import com.larpclient.nl;
import com.larpclient.qe;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class AstralWeaponTrailsModule extends Module implements qe {
   private static AstralWeaponTrailsModule P;
   private final o C = this.addSetting(new o("Style", "Energy ribbon appearance", "Astral", "Astral", "Prismatic", "Void"));
   private final b m = this.addSetting(new b("Trail Duration", "Seconds of recent blade motion", 0.28, 0.1, 0.7, 0.02));
   private final b L = this.addSetting(new b("Width", "Ribbon span along the actual blade", 0.65, 0.15, 1.0, 0.05));
   private final b T = this.addSetting(new b("Intensity", "Body and leading edge brightness", 1.0, 0.2, 2.0, 0.1));
   private final m h = this.addSetting(new m("Theme Colors", "Use the current theme palette", true));
   private final h B = this.addSetting(new h("Primary Color", "Leading energy color", -8790017));
   private final h s = this.addSetting(new h("Secondary Color", "Fading wake color", -9142785));
   private final m b = this.addSetting(new m("Bloom", "Soft emissive halo on the native hand ribbon", true));
   private final m S = this.addSetting(new m("Sparks", "Small streaks separating from the swing", true));
   private final m a = this.addSetting(new m("First Person", "Show trails on the local rendered weapon", true));
   private final m c = this.addSetting(new m("Motion Smoothing", "Curve between captured blade poses", true));
   private Object o;
   private Object R;
   private Vec3 H;
   private int u = -1;
   private int e = -1;
   private boolean g;

   public AstralWeaponTrailsModule() {
      super("Astral Weapon Trails", "Luminous ribbons following the rendered sword or knife", Category.VISUALS, -1);
      P = this;
      this.B.a(() -> !this.h.getValue());
      this.s.a(() -> !this.h.getValue());
      this.C.w(var0 -> {
         dh.j();
         _q.c("WeaponTrails", "Style changed to {}; history reset", var0);
      });
      this.a.w(var0 -> dh.j());
   }

   public static AstralWeaponTrailsModule J() {
      return P;
   }

   public List<r<?>> U() {
      return List.of(this.C, this.m, this.L, this.T);
   }

   public List<r<?>> v() {
      return List.of(this.h, this.B, this.s, this.b);
   }

   public List<r<?>> W() {
      return List.of(this.S, this.a, this.c);
   }

   public int S() {
      return Math.max(16, a4.N(64));
   }

   public aj.n Q() {
      return new aj.n(
         this.C.getValue(),
         this.m.getValue(),
         this.L.getValue(),
         this.T.getValue(),
         this.h.getValue() ? com.larpclient.c.y : this.B.getValue(),
         this.h.getValue() ? com.larpclient.c.t : this.s.getValue(),
         this.b.getValue() && a4.h != nl.q.q,
         this.S.getValue(),
         this.c.getValue(),
         a4.K() ? 3 : 2
      );
   }

   public boolean X(ItemStack var1) {
      if (!this.isEnabled()) {
         return false;
      }

      Minecraft var2 = getMc();
      return this.isRuntimeActive()
         && this.a.getValue()
         && SwordModelsModule.I(var1)
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
      _q.z("WeaponTrails", "Enabled {}; native sword layer and articulated knife blade capture, two hands, at most 128 retained samples", this.C.getValue());
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
            if (this.o != var1.level || this.R != var1.player || this.g != var2 || this.u != nl.q) {
               this.clear();
               this.o = var1.level;
               this.R = var1.player;
               this.g = var2;
               this.u = nl.q;
            }

            if (var1.player != null && var1.level != null && var1.player.isAlive() && var2) {
               Vec3 var3 = var1.player.position();
               if (this.H != null && this.H.distanceToSqr(var3) > 64.0) {
                  dh.j();
                  _q.c("WeaponTrails", "Cleared blade history after player relocation");
               }

               this.H = var3;
               int var4 = var1.player.getInventory().getSelectedSlot();
               if (this.e != var4) {
                  dh.q(InteractionHand.MAIN_HAND);
               }

               this.e = var4;
               if (!SwordModelsModule.I(var1.player.getMainHandItem())) {
                  dh.q(InteractionHand.MAIN_HAND);
               }

               if (!SwordModelsModule.I(var1.player.getOffhandItem())) {
                  dh.q(InteractionHand.OFF_HAND);
               }

               dh.b(hz.S(), this.m.getValue());
            } else {
               dh.j();
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
   public void render(fz var1, boolean var2) {
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
      dh.j();
      this.o = this.R = null;
      this.H = null;
      this.e = -1;
   }
}

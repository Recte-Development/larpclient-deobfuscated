package com.larpclient.module;

import com.larpclient.b0;
import com.larpclient.ez;
import com.larpclient.f1;
import com.larpclient.j3;
import com.larpclient.q8;
import com.larpclient.qo;
import com.larpclient.t_;
import com.larpclient.we;
import java.util.List;
import net.minecraft.class_12075;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_243;
import net.minecraft.class_310;

public final class AstralWeaponTrailsModule extends Module implements com.larpclient.ll {
   private static AstralWeaponTrailsModule C;
   private final li r = this.addSetting(new li("Style", "Energy ribbon appearance", "Astral", "Astral", "Prismatic", "Void"));
   private final lo T = this.addSetting(new lo("Trail Duration", "Seconds of recent blade motion", 0.28, 0.1, 0.7, 0.02));
   private final lo I = this.addSetting(new lo("Width", "Ribbon span along the actual blade", 0.65, 0.15, 1.0, 0.05));
   private final lo s = this.addSetting(new lo("Intensity", "Body and leading edge brightness", 1.0, 0.2, 2.0, 0.1));
   private final l3 l = this.addSetting(new l3("Theme Colors", "Use the current theme palette", true));
   private final l0 o = this.addSetting(new l0("Primary Color", "Leading energy color", -8790017));
   private final l0 a = this.addSetting(new l0("Secondary Color", "Fading wake color", -9142785));
   private final l3 Z = this.addSetting(new l3("Bloom", "Soft emissive halo on the native hand ribbon", true));
   private final l3 m = this.addSetting(new l3("Sparks", "Small streaks separating from the swing", true));
   private final l3 D = this.addSetting(new l3("First Person", "Show trails on the local rendered weapon", true));
   private final l3 F = this.addSetting(new l3("Motion Smoothing", "Curve between captured blade poses", true));
   private Object f;
   private Object d;
   private class_243 u;
   private int U = -1;
   private int i = -1;
   private boolean Y;

   public AstralWeaponTrailsModule() {
      super("Astral Weapon Trails", "Luminous ribbons following the rendered sword or knife", Category.VISUALS, -1);
      C = this;
      this.o.O(() -> !this.l.getValue());
      this.a.O(() -> !this.l.getValue());
      this.r.a(var0 -> {
         we.K();
         q8.v("WeaponTrails", "Style changed to {}; history reset", var0);
      });
      this.D.a(var0 -> we.K());
   }

   public static AstralWeaponTrailsModule k() {
      return C;
   }

   public List<lb<?>> p() {
      return List.of(this.r, this.T, this.I, this.s);
   }

   public List<lb<?>> l() {
      return List.of(this.l, this.o, this.a, this.Z);
   }

   public List<lb<?>> K() {
      return List.of(this.m, this.D, this.F);
   }

   public int i() {
      return Math.max(16, b0.Y(64));
   }

   public j3.b o() {
      return new j3.b(
         this.r.getValue(),
         this.T.getValue(),
         this.I.getValue(),
         this.s.getValue(),
         this.l.getValue() ? ez.a : this.o.getValue(),
         this.l.getValue() ? ez.f : this.a.getValue(),
         this.Z.getValue() && b0.r != t_.t.A,
         this.m.getValue(),
         this.F.getValue(),
         b0.E() ? 3 : 2
      );
   }

   public boolean e(class_1799 var1) {
      if (!this.isEnabled()) {
         return false;
      }

      class_310 var2 = getMc();
      return this.isRuntimeActive()
         && this.D.getValue()
         && SwordModelsModule.F(var1)
         && var2.field_1724 != null
         && var2.field_1687 != null
         && var2.field_1724.method_5805()
         && !var2.field_1724.method_31550()
         && var2.field_1690.method_31044().method_31034();
   }

   @Override
   public String effectName() {
      return "WeaponTrails";
   }

   @Override
   public void onEnable() {
      this.clear();
      q8.G("WeaponTrails", "Enabled {}; native sword layer and articulated knife blade capture, two hands, at most 128 retained samples", this.r.getValue());
   }

   @Override
   public void onDisable() {
      this.clear();
   }

   @Override
   public void tick(class_310 var1) {
      if (this.isEnabled()) {
         if (!this.isRuntimeActive()) {
            this.clear();
         } else {
            boolean var2 = var1.field_1690.method_31044().method_31034();
            if (this.f != var1.field_1687 || this.d != var1.field_1724 || this.Y != var2 || this.U != t_.g) {
               this.clear();
               this.f = var1.field_1687;
               this.d = var1.field_1724;
               this.Y = var2;
               this.U = t_.g;
            }

            if (var1.field_1724 != null && var1.field_1687 != null && var1.field_1724.method_5805() && var2) {
               class_243 var3 = var1.field_1724.method_73189();
               if (this.u != null && this.u.method_1025(var3) > 64.0) {
                  we.K();
                  q8.v("WeaponTrails", "Cleared blade history after player relocation");
               }

               this.u = var3;
               int var4 = var1.field_1724.method_31548().method_67532();
               if (this.i != var4) {
                  we.t(class_1268.field_5808);
               }

               this.i = var4;
               if (!SwordModelsModule.F(var1.field_1724.method_6047())) {
                  we.t(class_1268.field_5808);
               }

               if (!SwordModelsModule.F(var1.field_1724.method_6079())) {
                  we.t(class_1268.field_5810);
               }

               we.n(qo.K(), this.T.getValue());
            } else {
               we.K();
            }
         }
      }
   }

   @Override
   public void prepare(float var1, class_12075 var2) {
      if (!this.isEnabled()) {
         ;
      }
   }

   @Override
   public void render(f1 var1, boolean var2) {
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
      we.K();
      this.f = this.d = null;
      this.u = null;
      this.i = -1;
   }
}

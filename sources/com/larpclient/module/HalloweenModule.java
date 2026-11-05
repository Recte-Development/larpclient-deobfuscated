package com.larpclient.module;

import com.larpclient.f8;
import com.larpclient.jm;
import com.larpclient.ol;
import com.larpclient.r4;
import com.larpclient.r6;
import com.larpclient.u2;
import com.larpclient.ym;
import com.larpclient.yv;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.minecraft.world.level.material.FogType;

public final class HalloweenModule extends Module implements u2 {
   private static HalloweenModule x;
   private final lp E = this.addSetting(new lp("Intensity", "A coordinated After Dark preset", "Balanced", "Subtle", "Balanced", "Cinematic"));
   private final ln m = this.d("Harvest Moon", "Amber moon and midnight atmosphere");
   private final ln Y = this.d("Cinderfall", "Sparse drifting ash and embers outdoors");
   private final ln n = this.d("Haunted Combat", "Spectral hit accents and native weapon trails");
   private final ln o = this.d("Soul Harvest", "An intact pumpkin flattens the defeated player and sends out a ground shockwave");
   private final ln M = this.addSetting(new ln("After Dark Menu", "Use the Halloween logo in menus and ClickGUI", true) {
      @Override
      public String getDisplayName() {
         return "Menu Logo";
      }
   });
   private jm a;

   public HalloweenModule() {
      super("Halloween", "After Dark: harvest moon, drifting cinders and spectral combat", Category.VISUALS, -1);
      x = this;
      this.E.W(var1 -> {
         this.clear();
         if (this.isRuntimeActive() && this.n.getValue()) {
            a();
         }

         r4.V("Halloween", "Intensity changed: {}", var1);
      });
      this.Y.W(var1 -> {
         if (this.a != null) {
            this.a.K();
         }
      });
      this.n.W(var1 -> {
         if (this.a != null) {
            this.a.M();
         }

         if (this.isRuntimeActive()) {
            a();
         }
      });
      this.o.W(var1 -> {
         if (this.isRuntimeActive()) {
            F();
         }
      });
      this.m.W(var1 -> {
         if (this.isRuntimeActive()) {
            yv.h();
         }
      });
   }

   private ln d(String var1, String var2) {
      return this.addSetting(new ln(var1, var2, true));
   }

   public static HalloweenModule U() {
      return x;
   }

   public ol t() {
      return ol.W(this.E.getValue());
   }

   public List<l6<?>> a() {
      return List.of(this.E);
   }

   public List<l6<?>> T() {
      return List.of(this.m, this.Y, this.n, this.o, this.M);
   }

   public static boolean p() {
      return x != null && x.isEnabled() ? x != null && x.isRuntimeActive() && x.n.getValue() && f8.p("Halloween") : false;
   }

   public static boolean K() {
      return x != null && x.isEnabled() ? x != null && x.isRuntimeActive() && x.o.getValue() : false;
   }

   public static boolean M() {
      return x != null && x.isEnabled() ? x != null && x.isOperational() && x.M.getValue() : false;
   }

   public static boolean U() {
      if (x == null || !x.isEnabled()) {
         return false;
      } else if (x != null && x.isRuntimeActive() && x.m.getValue()) {
         Minecraft var0 = getMc();
         return var0.level != null
            && var0.player != null
            && var0.level.dimensionType().skybox() == Skybox.OVERWORLD
            && var0.gameRenderer.mainCamera().getFluidInCamera() == FogType.NONE;
      } else {
         return false;
      }
   }

   public void C(ClientboundDamageEventPacket var1) {
      if (this.isEnabled()) {
         if (p()) {
            this.l().B(getMc(), var1, this.t());
         }
      }
   }

   private jm l() {
      if (this.a == null) {
         this.a = new jm();
      }

      return this.a;
   }

   @Override
   public String effectName() {
      return "Halloween";
   }

   @Override
   public void tick(Minecraft var1) {
      if (this.isEnabled()) {
         this.l().M(var1, this.t(), this.Y.getValue(), p());
      }
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (this.a != null) {
            this.a.Z(var1, var2, this.Y.getValue(), p());
         }
      }
   }

   @Override
   public void render(r6 var1, boolean var2) {
      if (this.isEnabled()) {
         if (this.a != null) {
            this.a.r(var1, var2, this.Y.getValue(), p());
         }
      }
   }

   @Override
   public boolean glowing() {
      return !this.isEnabled() ? false : this.a != null && this.a.G(this.Y.getValue(), p());
   }

   @Override
   public boolean soft() {
      return this.a != null && this.a.C(this.Y.getValue(), p());
   }

   private static void a() {
      ym.s();
   }

   private static void F() {
      KillFXModule var0 = KillFXModule.X();
      if (var0 != null) {
         var0.d();
      }
   }

   @Override
   public void clear() {
      if (this.a != null) {
         this.a.Z();
      }
   }

   public void x(boolean var1) {
      this.clear();
      if (this.isRuntimeActive()) {
         this.J();
      }

      if (!var1) {
         this.disable();

         for (l6 var3 : this.getSettings()) {
            var3.x();
         }

         this.setKeyBind(-1);
      }
   }

   @Override
   public void onEnable() {
      this.clear();
      this.J();
      r4.V("Halloween", "After Dark enabled: {}; sky, hit and finisher ownership is temporary", this.E.getValue());
   }

   @Override
   public void onDisable() {
      this.clear();
      this.J();
      r4.V("Halloween", "After Dark disabled; seasonal state cleared, saved cosmetics retained");
   }

   private void J() {
      if (this.o.getValue()) {
         F();
      }

      if (this.n.getValue()) {
         a();
      }

      if (this.m.getValue()) {
         yv.h();
      }
   }
}

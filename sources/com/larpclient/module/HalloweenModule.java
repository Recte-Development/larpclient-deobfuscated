package com.larpclient.module;

import com.larpclient.h;
import com.larpclient.ki;
import com.larpclient.mm;
import com.larpclient.mt;
import com.larpclient.u8;
import com.larpclient.wy;
import com.larpclient.x4;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.minecraft.world.level.material.FogType;

public final class HalloweenModule extends Module implements com.larpclient.c {
   private static HalloweenModule g;
   private final d6 Y = this.addSetting(new d6("Intensity", "A coordinated After Dark preset", "Balanced", "Subtle", "Balanced", "Cinematic"));
   private final dh P = this.x("Harvest Moon", "Amber moon and midnight atmosphere");
   private final dh s = this.x("Cinderfall", "Sparse drifting ash and embers outdoors");
   private final dh X = this.x("Haunted Combat", "Spectral hit accents and native weapon trails");
   private final dh m = this.x("Soul Harvest", "An intact pumpkin flattens the defeated player and sends out a ground shockwave");
   private final dh y = this.addSetting(new dh("After Dark Menu", "Use the Halloween logo in menus and ClickGUI", true) {
      @Override
      public String getDisplayName() {
         return "Menu Logo";
      }
   });
   private mm x;

   public HalloweenModule() {
      super("Halloween", "After Dark: harvest moon, drifting cinders and spectral combat", Category.VISUALS, -1);
      g = this;
      this.Y.h(var1 -> {
         this.clear();
         if (this.isRuntimeActive() && this.X.P()) {
            c();
         }

         mt.K("Halloween", "Intensity changed: {}", var1);
      });
      this.s.h(var1 -> {
         if (this.x != null) {
            this.x.J();
         }
      });
      this.X.h(var1 -> {
         if (this.x != null) {
            this.x.t();
         }

         if (this.isRuntimeActive()) {
            c();
         }
      });
      this.m.h(var1 -> {
         if (this.isRuntimeActive()) {
            G();
         }
      });
      this.P.h(var1 -> {
         if (this.isRuntimeActive()) {
            wy.e();
         }
      });
   }

   private dh x(String var1, String var2) {
      return this.addSetting(new dh(var1, var2, true));
   }

   public static HalloweenModule X() {
      return g;
   }

   public u8 s() {
      return u8.R(this.Y.P());
   }

   public List<df<?>> E() {
      return List.of(this.Y);
   }

   public List<df<?>> o() {
      return List.of(this.P, this.s, this.X, this.m, this.y);
   }

   public static boolean S() {
      return g != null && g.isEnabled() ? g != null && g.isRuntimeActive() && g.X.P() && h.R("Halloween") : false;
   }

   public static boolean E() {
      return g != null && g.isEnabled() ? g != null && g.isRuntimeActive() && g.m.P() : false;
   }

   public static boolean a() {
      return g != null && g.isEnabled() ? g != null && g.isOperational() && g.y.P() : false;
   }

   public static boolean f() {
      if (g == null || !g.isEnabled()) {
         return false;
      } else if (g != null && g.isRuntimeActive() && g.P.P()) {
         Minecraft var0 = getMc();
         return var0.level != null
            && var0.player != null
            && var0.level.dimensionType().skybox() == Skybox.OVERWORLD
            && var0.gameRenderer.mainCamera().getFluidInCamera() == FogType.NONE;
      } else {
         return false;
      }
   }

   public void S(ClientboundDamageEventPacket var1) {
      if (this.isEnabled()) {
         if (S()) {
            this.g().n(getMc(), var1, this.s());
         }
      }
   }

   private mm g() {
      if (this.x == null) {
         this.x = new mm();
      }

      return this.x;
   }

   @Override
   public String effectName() {
      return "Halloween";
   }

   @Override
   public void tick(Minecraft var1) {
      if (this.isEnabled()) {
         this.g().h(var1, this.s(), this.s.P(), S());
      }
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (this.x != null) {
            this.x.H(var1, var2, this.s.P(), S());
         }
      }
   }

   @Override
   public void render(ki var1, boolean var2) {
      if (this.isEnabled()) {
         if (this.x != null) {
            this.x.k(var1, var2, this.s.P(), S());
         }
      }
   }

   @Override
   public boolean glowing() {
      return !this.isEnabled() ? false : this.x != null && this.x.Y(this.s.P(), S());
   }

   @Override
   public boolean soft() {
      return this.x != null && this.x.g(this.s.P(), S());
   }

   private static void c() {
      x4.D();
   }

   private static void G() {
      KillFXModule var0 = KillFXModule.l();
      if (var0 != null) {
         var0.c();
      }
   }

   @Override
   public void clear() {
      if (this.x != null) {
         this.x.s();
      }
   }

   public void t(boolean var1) {
      this.clear();
      if (this.isRuntimeActive()) {
         this.V();
      }

      if (!var1) {
         this.disable();

         for (df var3 : this.getSettings()) {
            var3.B();
         }

         this.setKeyBind(-1);
      }
   }

   @Override
   public void onEnable() {
      this.clear();
      this.V();
      mt.K("Halloween", "After Dark enabled: {}; sky, hit and finisher ownership is temporary", this.Y.P());
   }

   @Override
   public void onDisable() {
      this.clear();
      this.V();
      mt.K("Halloween", "After Dark disabled; seasonal state cleared, saved cosmetics retained");
   }

   private void V() {
      if (this.m.P()) {
         G();
      }

      if (this.X.P()) {
         c();
      }

      if (this.P.P()) {
         wy.e();
      }
   }
}

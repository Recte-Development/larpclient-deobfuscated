package com.larpclient.module;

import com.larpclient.b5;
import com.larpclient.ho;
import com.larpclient.kj;
import com.larpclient.pt;
import com.larpclient.tk;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;

public final class CelestialWeatherModule extends Module implements tk {
   private static CelestialWeatherModule p;
   public final l o = this.addSetting(new l("Style", "Local celestial atmosphere", "Meteor Shower", "Meteor Shower", "Stardust", "Astral Rain", "Glowing Snow"));
   public final e b = this.U("Density", "Emission strength within the weather budget", 1.0, 0.1, 4.0, 0.1);
   public final e E = this.U("Speed", "Flight and floating motion", 1.0, 0.0, 3.0, 0.1);
   public final e S = this.U("Size", "Meteor cores, dust, droplets and snowflakes", 1.0, 0.4, 2.5, 0.1);
   public final s J = this.addSetting(new s("Theme Colors", "Use the current theme palette", true));
   public final i m = this.addSetting(new i("Color", "Celestial cores and dust", -2561281));
   public final i Z = this.addSetting(new i("Secondary Color", "Celestial tails and contact glow", -6059009));
   public final s Q = this.addSetting(new s("Bloom", "Use shared world glow", true));
   public final e k = this.U("Radius", "World-space emission region in blocks", 28.0, 8.0, 48.0, 1.0);
   public final e x = this.U("Wind Strength", "Coherent horizontal drift", 0.8, 0.0, 6.0, 0.1);
   public final s O = this.addSetting(new s("Surface Impacts", "Selected nearby flights contact actual block surfaces", true));
   public final e M = this.U("Render Distance", "Fade celestial weather beyond this distance", 80.0, 24.0, 128.0, 4.0);
   public final l h = this.addSetting(new l("Dimension Filter", "Choose where local weather appears", "Overworld", "Overworld", "All"));
   public final l F = this.addSetting(new l("Indoors", "Fade weather when the camera is sheltered", "Fade", "Fade", "Allow"));
   private final pt.b n = this::u;
   private final pt c = new pt();
   private Object A;
   private Object W;
   private Vec3 l;
   private int H;
   private boolean d;
   private boolean j;

   public CelestialWeatherModule() {
      super("Celestial Weather", "Meteor showers, stardust, luminous rain and glowing snow", Category.VISUALS, -1);
      p = this;
      this.m.P(() -> !this.J.getValue());
      this.Z.P(() -> !this.J.getValue());
      this.O.P(() -> !this.o.O("Stardust") && !this.o.O("Glowing Snow"));
      this.F.P(() -> !this.o.O("Glowing Snow"));
      this.o.o(var1 -> this.clear());
      this.h.o(var1 -> this.clear());
   }

   private e U(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new e(var1, var2, var3, var5, var7, var9));
   }

   public List<q<?>> R() {
      return List.of(this.o, this.b, this.E, this.S);
   }

   public List<q<?>> F() {
      return List.of(this.J, this.m, this.Z, this.Q);
   }

   public List<q<?>> h() {
      return List.of(this.k, this.x, this.O, this.M, this.h, this.F);
   }

   public static CelestialWeatherModule V() {
      return p;
   }

   public pt V() {
      return this.c;
   }

   public pt.s B() {
      pt.l var1 = this.o.O("Stardust") ? pt.l.u : (this.o.O("Astral Rain") ? pt.l.R : (this.o.O("Glowing Snow") ? pt.l.a : pt.l.Q));
      return new pt.s(
         var1,
         this.b.getValue(),
         this.k.getValue(),
         this.E.getValue(),
         this.S.getValue(),
         this.x.getValue(),
         this.M.getValue(),
         this.J.getValue() ? ho.J : this.m.getValue(),
         this.J.getValue() ? ho.ds : this.Z.getValue(),
         this.Q.getValue(),
         this.O.getValue(),
         com.larpclient.y.i(256)
      );
   }

   @Override
   public String effectName() {
      return "Celestial Weather";
   }

   @Override
   public void tick(Minecraft var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var1.level != null && var1.player != null && var1.player.isAlive()) {
            this.j = var1.isPaused();
            if (!this.j) {
               Vec3 var2 = var1.gameRenderer.mainCamera().position();
               if (this.A != var1.level || this.W != var1.player || this.l != null && this.l.distanceToSqr(var2) > 4096.0) {
                  this.clear();
                  this.A = var1.level;
                  this.W = var1.player;
                  com.larpclient.h.U(
                     this.effectName(),
                     "World field initialized; style={}; max flights={}; max sprites={}; max terrain queries/tick={}",
                     this.o.getValue(),
                     256,
                     1024,
                     4
                  );
               }

               this.l = var2;
               if (this.h.O("Overworld") && var1.level.dimensionType().skybox() != Skybox.OVERWORLD) {
                  this.c.y();
               } else {
                  if (this.H++ % 5 == 0) {
                     BlockPos var3 = BlockPos.containing(var2);
                     int var4 = !this.F.O("Allow") && !this.o.O("Glowing Snow") && (!var1.level.hasChunkAt(var3) || !var1.level.canSeeSky(var3)) ? 0 : 1;
                     if (var4 != this.d) {
                        com.larpclient.h.U(this.effectName(), "Exposure changed: {}; doorway fade active", var4 != 0 ? "outdoors" : "sheltered");
                     }

                     this.d = (boolean)var4;
                  }

                  this.c.K(var2, this.B(), this.d, (var1x, var2x) -> b5.r(var1.level, var1x, var2x), this.o.O("Glowing Snow") ? this.n : null);
               }
            }
         } else {
            this.clear();
         }
      }
   }

   private double u(int var1, int var2) {
      ClientLevel var3 = Minecraft.getInstance().level;
      return var3 != null && var3.hasChunk(var1 >> 4, var2 >> 4) ? var3.getHeight(Types.MOTION_BLOCKING, var1, var2) : Double.NaN;
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (!this.isRuntimeActive()) {
            this.c.y();
         } else {
            this.c.c(var2.pos, this.j ? 1.0F : var1, var2.cullFrustum);
         }
      }
   }

   @Override
   public void render(kj var1, boolean var2) {
      if (this.isEnabled()) {
         this.c.C(var1, var2);
      }
   }

   @Override
   public boolean glowing() {
      return this.isEnabled() ? this.c.d() : false;
   }

   @Override
   public boolean soft() {
      return this.c.b();
   }

   @Override
   public void clear() {
      this.c.y();
      this.A = this.W = null;
      this.l = null;
      this.H = 0;
      this.d = this.j = (boolean)0;
   }

   @Override
   public void onEnable() {
      this.clear();
      com.larpclient.h.A(this.effectName(), "Enabled {}; isolated weather pool; terrain queries capped at {}/tick", this.o.getValue(), 4);
   }

   @Override
   public void onDisable() {
      this.clear();
   }
}

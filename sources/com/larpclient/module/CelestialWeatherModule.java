package com.larpclient.module;

import com.larpclient.et;
import com.larpclient.r4;
import com.larpclient.r6;
import com.larpclient.rv;
import com.larpclient.u2;
import com.larpclient.xi;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;

public final class CelestialWeatherModule extends Module implements u2 {
   private static CelestialWeatherModule X;
   public final lp M = this.addSetting(
      new lp("Style", "Local celestial atmosphere", "Meteor Shower", "Meteor Shower", "Stardust", "Astral Rain", "Glowing Snow")
   );
   public final lc S = this.U("Density", "Emission strength within the weather budget", 1.0, 0.1, 4.0, 0.1);
   public final lc T = this.U("Speed", "Flight and floating motion", 1.0, 0.0, 3.0, 0.1);
   public final lc e = this.U("Size", "Meteor cores, dust, droplets and snowflakes", 1.0, 0.4, 2.5, 0.1);
   public final ln N = this.addSetting(new ln("Theme Colors", "Use the current theme palette", true));
   public final lv u = this.addSetting(new lv("Color", "Celestial cores and dust", -2561281));
   public final lv F = this.addSetting(new lv("Secondary Color", "Celestial tails and contact glow", -6059009));
   public final ln Z = this.addSetting(new ln("Bloom", "Use shared world glow", true));
   public final lc D = this.U("Radius", "World-space emission region in blocks", 28.0, 8.0, 48.0, 1.0);
   public final lc U = this.U("Wind Strength", "Coherent horizontal drift", 0.8, 0.0, 6.0, 0.1);
   public final ln c = this.addSetting(new ln("Surface Impacts", "Selected nearby flights contact actual block surfaces", true));
   public final lc Y = this.U("Render Distance", "Fade celestial weather beyond this distance", 80.0, 24.0, 128.0, 4.0);
   public final lp I = this.addSetting(new lp("Dimension Filter", "Choose where local weather appears", "Overworld", "Overworld", "All"));
   public final lp w = this.addSetting(new lp("Indoors", "Fade weather when the camera is sheltered", "Fade", "Fade", "Allow"));
   private final xi.t q = this::M;
   private final xi C = new xi();
   private Object d;
   private Object B;
   private Vec3 P;
   private int h;
   private boolean y;
   private boolean m;

   public CelestialWeatherModule() {
      super("Celestial Weather", "Meteor showers, stardust, luminous rain and glowing snow", Category.VISUALS, -1);
      X = this;
      this.u.x(() -> !this.N.getValue());
      this.F.x(() -> !this.N.getValue());
      this.c.x(() -> !this.M.i("Stardust") && !this.M.i("Glowing Snow"));
      this.w.x(() -> !this.M.i("Glowing Snow"));
      this.M.W(var1 -> this.clear());
      this.I.W(var1 -> this.clear());
   }

   private lc U(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lc(var1, var2, var3, var5, var7, var9));
   }

   public List<l6<?>> T() {
      return List.of(this.M, this.S, this.T, this.e);
   }

   public List<l6<?>> o() {
      return List.of(this.N, this.u, this.F, this.Z);
   }

   public List<l6<?>> W() {
      return List.of(this.D, this.U, this.c, this.Y, this.I, this.w);
   }

   public static CelestialWeatherModule t() {
      return X;
   }

   public xi F() {
      return this.C;
   }

   public xi.j m() {
      xi.v var1 = this.M.i("Stardust") ? xi.v.A : (this.M.i("Astral Rain") ? xi.v.l : (this.M.i("Glowing Snow") ? xi.v.z : xi.v.m));
      return new xi.j(
         var1,
         this.S.getValue(),
         this.D.getValue(),
         this.T.getValue(),
         this.e.getValue(),
         this.U.getValue(),
         this.Y.getValue(),
         this.N.getValue() ? rv.e : this.u.getValue(),
         this.N.getValue() ? rv.GQ : this.F.getValue(),
         this.Z.getValue(),
         this.c.getValue(),
         com.larpclient.lo.H(256)
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
            this.m = var1.isPaused();
            if (!this.m) {
               Vec3 var2 = var1.gameRenderer.mainCamera().position();
               if (this.d != var1.level || this.B != var1.player || this.P != null && this.P.distanceToSqr(var2) > 4096.0) {
                  this.clear();
                  this.d = var1.level;
                  this.B = var1.player;
                  r4.g(
                     this.effectName(),
                     "World field initialized; style={}; max flights={}; max sprites={}; max terrain queries/tick={}",
                     this.M.getValue(),
                     256,
                     1024,
                     4
                  );
               }

               this.P = var2;
               if (this.I.i("Overworld") && var1.level.dimensionType().skybox() != Skybox.OVERWORLD) {
                  this.C.J();
               } else {
                  if (this.h++ % 5 == 0) {
                     BlockPos var3 = BlockPos.containing(var2);
                     int var4 = !this.w.i("Allow") && !this.M.i("Glowing Snow") && (!var1.level.hasChunkAt(var3) || !var1.level.canSeeSky(var3)) ? 0 : 1;
                     if (var4 != this.y) {
                        r4.g(this.effectName(), "Exposure changed: {}; doorway fade active", var4 != 0 ? "outdoors" : "sheltered");
                     }

                     this.y = (boolean)var4;
                  }

                  this.C.Y(var2, this.m(), this.y, (var1x, var2x) -> et.Q(var1.level, var1x, var2x), this.M.i("Glowing Snow") ? this.q : null);
               }
            }
         } else {
            this.clear();
         }
      }
   }

   private double M(int var1, int var2) {
      ClientLevel var3 = Minecraft.getInstance().level;
      return var3 != null && var3.hasChunk(var1 >> 4, var2 >> 4) ? var3.getHeight(Types.MOTION_BLOCKING, var1, var2) : Double.NaN;
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (!this.isRuntimeActive()) {
            this.C.J();
         } else {
            this.C.k(var2.pos, this.m ? 1.0F : var1, var2.cullFrustum);
         }
      }
   }

   @Override
   public void render(r6 var1, boolean var2) {
      if (this.isEnabled()) {
         this.C.y(var1, var2);
      }
   }

   @Override
   public boolean glowing() {
      return this.isEnabled() ? this.C.W() : false;
   }

   @Override
   public boolean soft() {
      return this.C.x();
   }

   @Override
   public void clear() {
      this.C.J();
      this.d = this.B = null;
      this.P = null;
      this.h = 0;
      this.y = this.m = (boolean)0;
   }

   @Override
   public void onEnable() {
      this.clear();
      r4.V(this.effectName(), "Enabled {}; isolated weather pool; terrain queries capped at {}/tick", this.M.getValue(), 4);
   }

   @Override
   public void onDisable() {
      this.clear();
   }
}

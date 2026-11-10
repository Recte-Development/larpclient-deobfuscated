package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.a4;
import com.larpclient.fz;
import com.larpclient.qe;
import com.larpclient.v9;
import com.larpclient.wa;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;

public final class CelestialWeatherModule extends Module implements qe {
   private static CelestialWeatherModule N;
   public final o n = this.addSetting(new o("Style", "Local celestial atmosphere", "Meteor Shower", "Meteor Shower", "Stardust", "Astral Rain", "Glowing Snow"));
   public final b d = this.v("Density", "Emission strength within the weather budget", 1.0, 0.1, 4.0, 0.1);
   public final b c = this.v("Speed", "Flight and floating motion", 1.0, 0.0, 3.0, 0.1);
   public final b p = this.v("Size", "Meteor cores, dust, droplets and snowflakes", 1.0, 0.4, 2.5, 0.1);
   public final m w = this.addSetting(new m("Theme Colors", "Use the current theme palette", true));
   public final h f = this.addSetting(new h("Color", "Celestial cores and dust", -2561281));
   public final h V = this.addSetting(new h("Secondary Color", "Celestial tails and contact glow", -6059009));
   public final m t = this.addSetting(new m("Bloom", "Use shared world glow", true));
   public final b l = this.v("Radius", "World-space emission region in blocks", 28.0, 8.0, 48.0, 1.0);
   public final b Y = this.v("Wind Strength", "Coherent horizontal drift", 0.8, 0.0, 6.0, 0.1);
   public final m e = this.addSetting(new m("Surface Impacts", "Selected nearby flights contact actual block surfaces", true));
   public final b S = this.v("Render Distance", "Fade celestial weather beyond this distance", 80.0, 24.0, 128.0, 4.0);
   public final o U = this.addSetting(new o("Dimension Filter", "Choose where local weather appears", "Overworld", "Overworld", "All"));
   public final o H = this.addSetting(new o("Indoors", "Fade weather when the camera is sheltered", "Fade", "Fade", "Allow"));
   private final wa.u z = this::K;
   private final wa F = new wa();
   private Object O;
   private Object L;
   private Vec3 v;
   private int D;
   private boolean Z;
   private boolean I;

   public CelestialWeatherModule() {
      super("Celestial Weather", "Meteor showers, stardust, luminous rain and glowing snow", Category.VISUALS, -1);
      N = this;
      this.f.a(() -> !this.w.getValue());
      this.V.a(() -> !this.w.getValue());
      this.e.a(() -> !this.n.w("Stardust") && !this.n.w("Glowing Snow"));
      this.H.a(() -> !this.n.w("Glowing Snow"));
      this.n.w(var1 -> this.clear());
      this.U.w(var1 -> this.clear());
   }

   private b v(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new b(var1, var2, var3, var5, var7, var9));
   }

   public List<r<?>> q() {
      return List.of(this.n, this.d, this.c, this.p);
   }

   public List<r<?>> V() {
      return List.of(this.w, this.f, this.V, this.t);
   }

   public List<r<?>> o() {
      return List.of(this.l, this.Y, this.e, this.S, this.U, this.H);
   }

   public static CelestialWeatherModule L() {
      return N;
   }

   public wa g() {
      return this.F;
   }

   public wa.l l() {
      wa.h var1 = this.n.w("Stardust") ? wa.h.d : (this.n.w("Astral Rain") ? wa.h.W : (this.n.w("Glowing Snow") ? wa.h.x : wa.h.S));
      return new wa.l(
         var1,
         this.d.getValue(),
         this.l.getValue(),
         this.c.getValue(),
         this.p.getValue(),
         this.Y.getValue(),
         this.S.getValue(),
         this.w.getValue() ? com.larpclient.c.y : this.f.getValue(),
         this.w.getValue() ? com.larpclient.c.t : this.V.getValue(),
         this.t.getValue(),
         this.e.getValue(),
         a4.N(256)
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
            this.I = var1.isPaused();
            if (!this.I) {
               Vec3 var2 = var1.gameRenderer.mainCamera().position();
               if (this.O != var1.level || this.L != var1.player || this.v != null && this.v.distanceToSqr(var2) > 4096.0) {
                  this.clear();
                  this.O = var1.level;
                  this.L = var1.player;
                  _q.c(
                     this.effectName(),
                     "World field initialized; style={}; max flights={}; max sprites={}; max terrain queries/tick={}",
                     this.n.getValue(),
                     256,
                     1024,
                     4
                  );
               }

               this.v = var2;
               if (this.U.w("Overworld") && var1.level.dimensionType().skybox() != Skybox.OVERWORLD) {
                  this.F.g();
               } else {
                  if (this.D++ % 5 == 0) {
                     BlockPos var3 = BlockPos.containing(var2);
                     int var4 = this.H.w("Allow") || this.n.w("Glowing Snow") || var1.level.hasChunkAt(var3) && var1.level.canSeeSky(var3) ? 1 : 0;
                     if (var4 != this.Z) {
                        _q.c(this.effectName(), "Exposure changed: {}; doorway fade active", var4 != 0 ? "outdoors" : "sheltered");
                     }

                     this.Z = (boolean)var4;
                  }

                  this.F.i(var2, this.l(), this.Z, (var1x, var2x) -> v9.u(var1.level, var1x, var2x), this.n.w("Glowing Snow") ? this.z : null);
               }
            }
         } else {
            this.clear();
         }
      }
   }

   private double K(int var1, int var2) {
      ClientLevel var3 = Minecraft.getInstance().level;
      return var3 != null && var3.hasChunk(var1 >> 4, var2 >> 4) ? var3.getHeight(Types.MOTION_BLOCKING, var1, var2) : Double.NaN;
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (!this.isRuntimeActive()) {
            this.F.g();
         } else {
            this.F.z(var2.pos, this.I ? 1.0F : var1, var2.cullFrustum);
         }
      }
   }

   @Override
   public void render(fz var1, boolean var2) {
      if (this.isEnabled()) {
         this.F.k(var1, var2);
      }
   }

   @Override
   public boolean glowing() {
      return this.isEnabled() ? this.F.F() : false;
   }

   @Override
   public boolean soft() {
      return this.F.j();
   }

   @Override
   public void clear() {
      this.F.g();
      this.O = this.L = null;
      this.v = null;
      this.D = 0;
      this.Z = this.I = (boolean)0;
   }

   @Override
   public void onEnable() {
      this.clear();
      _q.z(this.effectName(), "Enabled {}; isolated weather pool; terrain queries capped at {}/tick", this.n.getValue(), 4);
   }

   @Override
   public void onDisable() {
      this.clear();
   }
}

package com.larpclient.module;

import com.larpclient._k;
import com.larpclient.e3;
import com.larpclient.ki;
import com.larpclient.mt;
import com.larpclient.w8;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;

public final class CelestialWeatherModule extends Module implements com.larpclient.c {
   private static CelestialWeatherModule f;
   public final d6 W = this.addSetting(
      new d6("Style", "Local celestial atmosphere", "Meteor Shower", "Meteor Shower", "Stardust", "Astral Rain", "Glowing Snow")
   );
   public final d9 B = this.E("Density", "Emission strength within the weather budget", 1.0, 0.1, 4.0, 0.1);
   public final d9 L = this.E("Speed", "Flight and floating motion", 1.0, 0.0, 3.0, 0.1);
   public final d9 Y = this.E("Size", "Meteor cores, dust, droplets and snowflakes", 1.0, 0.4, 2.5, 0.1);
   public final dh X = this.addSetting(new dh("Theme Colors", "Use the current theme palette", true));
   public final dk t = this.addSetting(new dk("Color", "Celestial cores and dust", -2561281));
   public final dk a = this.addSetting(new dk("Secondary Color", "Celestial tails and contact glow", -6059009));
   public final dh U = this.addSetting(new dh("Bloom", "Use shared world glow", true));
   public final d9 A = this.E("Radius", "World-space emission region in blocks", 28.0, 8.0, 48.0, 1.0);
   public final d9 i = this.E("Wind Strength", "Coherent horizontal drift", 0.8, 0.0, 6.0, 0.1);
   public final dh k = this.addSetting(new dh("Surface Impacts", "Selected nearby flights contact actual block surfaces", true));
   public final d9 v = this.E("Render Distance", "Fade celestial weather beyond this distance", 80.0, 24.0, 128.0, 4.0);
   public final d6 w = this.addSetting(new d6("Dimension Filter", "Choose where local weather appears", "Overworld", "Overworld", "All"));
   public final d6 o = this.addSetting(new d6("Indoors", "Fade weather when the camera is sheltered", "Fade", "Fade", "Allow"));
   private final e3.m Z = this::x;
   private final e3 h = new e3();
   private Object b;
   private Object Q;
   private Vec3 x;
   private int l;
   private boolean d;
   private boolean C;

   public CelestialWeatherModule() {
      super("Celestial Weather", "Meteor showers, stardust, luminous rain and glowing snow", Category.VISUALS, -1);
      f = this;
      this.t.H(() -> !this.X.P());
      this.a.H(() -> !this.X.P());
      this.k.H(() -> !this.W.S("Stardust") && !this.W.S("Glowing Snow"));
      this.o.H(() -> !this.W.S("Glowing Snow"));
      this.W.h(var1 -> this.clear());
      this.w.h(var1 -> this.clear());
   }

   private d9 E(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new d9(var1, var2, var3, var5, var7, var9));
   }

   public List<df<?>> R() {
      return List.of(this.W, this.B, this.L, this.Y);
   }

   public List<df<?>> F() {
      return List.of(this.X, this.t, this.a, this.U);
   }

   public List<df<?>> j() {
      return List.of(this.A, this.i, this.k, this.v, this.w, this.o);
   }

   public static CelestialWeatherModule p() {
      return f;
   }

   public e3 F() {
      return this.h;
   }

   public e3.i z() {
      e3.d var1 = this.W.S("Stardust") ? e3.d.p : (this.W.S("Astral Rain") ? e3.d.X : (this.W.S("Glowing Snow") ? e3.d.t : e3.d.w));
      return new e3.i(
         var1,
         this.B.P(),
         this.A.P(),
         this.L.P(),
         this.Y.P(),
         this.i.P(),
         this.v.P(),
         this.X.P() ? com.larpclient.f.C : this.t.P(),
         this.X.P() ? com.larpclient.f.O : this.a.P(),
         this.U.P(),
         this.k.P(),
         _k.m(256)
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
            this.C = var1.isPaused();
            if (!this.C) {
               Vec3 var2 = var1.gameRenderer.mainCamera().position();
               if (this.b != var1.level || this.Q != var1.player || this.x != null && this.x.distanceToSqr(var2) > 4096.0) {
                  this.clear();
                  this.b = var1.level;
                  this.Q = var1.player;
                  mt.U(
                     this.effectName(),
                     "World field initialized; style={}; max flights={}; max sprites={}; max terrain queries/tick={}",
                     this.W.P(),
                     256,
                     1024,
                     4
                  );
               }

               this.x = var2;
               if (this.w.S("Overworld") && var1.level.dimensionType().skybox() != Skybox.OVERWORLD) {
                  this.h.Y();
               } else {
                  if (this.l++ % 5 == 0) {
                     BlockPos var3 = BlockPos.containing(var2);
                     int var4 = this.o.S("Allow") || this.W.S("Glowing Snow") || var1.level.hasChunkAt(var3) && var1.level.canSeeSky(var3) ? 1 : 0;
                     if (var4 != this.d) {
                        mt.U(this.effectName(), "Exposure changed: {}; doorway fade active", var4 != 0 ? "outdoors" : "sheltered");
                     }

                     this.d = (boolean)var4;
                  }

                  this.h.M(var2, this.z(), this.d, (var1x, var2x) -> w8.G(var1.level, var1x, var2x), this.W.S("Glowing Snow") ? this.Z : null);
               }
            }
         } else {
            this.clear();
         }
      }
   }

   private double x(int var1, int var2) {
      ClientLevel var3 = Minecraft.getInstance().level;
      return var3 != null && var3.hasChunk(var1 >> 4, var2 >> 4) ? var3.getHeight(Types.MOTION_BLOCKING, var1, var2) : Double.NaN;
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (!this.isRuntimeActive()) {
            this.h.Y();
         } else {
            this.h.r(var2.pos, this.C ? 1.0F : var1, var2.cullFrustum);
         }
      }
   }

   @Override
   public void render(ki var1, boolean var2) {
      if (this.isEnabled()) {
         this.h.s(var1, var2);
      }
   }

   @Override
   public boolean glowing() {
      return this.isEnabled() ? this.h.D() : false;
   }

   @Override
   public boolean soft() {
      return this.h.S();
   }

   @Override
   public void clear() {
      this.h.Y();
      this.b = this.Q = null;
      this.x = null;
      this.l = 0;
      this.d = this.C = (boolean)0;
   }

   @Override
   public void onEnable() {
      this.clear();
      mt.K(this.effectName(), "Enabled {}; isolated weather pool; terrain queries capped at {}/tick", this.W.P(), 4);
   }

   @Override
   public void onDisable() {
      this.clear();
   }
}

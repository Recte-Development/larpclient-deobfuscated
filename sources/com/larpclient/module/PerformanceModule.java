package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._k;
import com.larpclient._v;
import com.larpclient.m5;
import com.larpclient.mt;
import com.larpclient.w9;
import java.util.List;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.PrioritizeChunkUpdates;
import net.minecraft.server.level.ParticleStatus;

public final class PerformanceModule extends Module {
   private static PerformanceModule H;
   public final d6 R = this.addSetting(
      new d6(
         "Chunk Updates",
         "Balanced threads terrain updates but keeps player edits immediate; Threaded may delay visible block updates",
         "Balanced",
         "Balanced",
         "Threaded",
         "Unchanged"
      )
   );
   public final dh W = this.addSetting(
      new dh("Streaming FPS Limit", "Cap gameplay FPS to leave GPU headroom for recording; never raises Minecraft's existing limit", false)
   );
   public final d9 Q = this.addSetting(new d9("Streaming FPS", "Gameplay frame limit while recording or streaming", 120.0, 30.0, 240.0, 1.0));
   public final dh b = this.addSetting(
      new dh("Aggressive Mode", "Applies the reductions below together while Performance is on. Your video, effect and module settings stay as saved", false)
   );
   public final dh C = this.addSetting(
      new dh(
         "Single Color Blocks",
         "Draws solid and cutout terrain in one color with its shapes, face shading and light. Water and glass keep their textures",
         false
      )
   );
   public final dk V = this.addSetting(new dk("Block Color", "Terrain color for Single Color Blocks", -4932926));
   public final d6 T = this.addSetting(
      new d6("Particles", "Caps Minecraft's particle setting; Unchanged keeps your video setting", "Decreased", "Unchanged", "Decreased", "Minimal")
   );
   public final d6 n = this.addSetting(
      new d6("Client Effects", "Caps Larp effect detail and glow; Unchanged keeps your Render quality choices", "Low", "Unchanged", "Low", "Off")
   );
   public final dh v = this.addSetting(new dh("Hide Clouds", "Skips cloud rendering", true));
   public final d9 e = this.addSetting(new d9("Entity Distance", "Share of your entity render distance; 100 keeps it", 100.0, 25.0, 100.0, 5.0));

   public PerformanceModule() {
      super(
         "Performance",
         "Render quality for client effects always applies. Turn the module on for smoother chunk updates and an optional recording FPS cap",
         Category.PLAYER,
         -1
      );
      H = this;
      this.Q.H(this.W::P);
      this.R.h(var1 -> this.A());
      this.W.h(var1 -> this.A());
      this.Q.h(var1 -> this.A());

      for (df var2 : this.Y()) {
         if (var2 != this.b) {
            var2.H(this.b::P);
         }

         var2.h(var1 -> this.A());
      }

      this.V.H(() -> this.b.P() && this.C.P());
      m5.R = PerformanceModule::W;
   }

   public List<df<?>> f() {
      return List.of(this.R, this.W, this.Q);
   }

   public List<df<?>> Y() {
      return List.of(this.b, this.C, this.V, this.T, this.n, this.v, this.e);
   }

   public List<df<?>> P() {
      d6 var1 = new d6("Bloom", "Glow quality for effects that request bloom", m5.y.name(), "Off", "Low", "Medium", "High", "Ultra");
      var1.h(var0 -> m5.y = m5.n.valueOf(var0));
      d6 var2 = new d6("Blur", "Blur quality behind the ClickGUI window and HUD panels", m5.u.name(), "Off", "Low", "Medium", "High", "Ultra");
      var2.h(var0 -> m5.u = m5.n.valueOf(var0));
      m5.n var3 = _k.b();
      d6 var4 = new d6(
         "Effects",
         "Particle detail, glow and refraction for trails, weather and After Dark",
         var3 == m5.n.h ? "Balanced" : var3.name(),
         "Off",
         "Low",
         "Balanced",
         "High",
         "Ultra"
      );
      var4.h(var1x -> {
         w9 var2x = LarpClient.F().f();
         m5.n var3x = var1x.equals("Balanced") ? m5.n.h : m5.n.valueOf(var1x);
         if (!var2x.x(var3x)) {
            m5.n var4x = var2x.t().B;
            var4.setValue(var4x == m5.n.h ? "Balanced" : var4x.name());
         }
      });
      dh var5 = new dh("Soft particles", "Fade glow and smoke where they meet solid surfaces", _v.e);
      var5.h(var1x -> {
         w9 var2x = LarpClient.F().f();
         if (!var2x.S(var1x)) {
            var5.setValue(var2x.t().L);
         }
      });
      return List.of(var1, var2, var4, var5);
   }

   public static PrioritizeChunkUpdates A(PrioritizeChunkUpdates var0) {
      if (H != null && H.isEnabled()) {
         PerformanceModule var1 = H;
         if (var1 == null || !var1.isRuntimeActive()) {
            return var0;
         } else if (var1.R.S("Threaded")) {
            return PrioritizeChunkUpdates.NONE;
         } else {
            return var1.R.S("Balanced") && var0 == PrioritizeChunkUpdates.NEARBY ? PrioritizeChunkUpdates.PLAYER_AFFECTED : var0;
         }
      } else {
         return var0;
      }
   }

   private static PerformanceModule J() {
      PerformanceModule var0 = H;
      return var0 != null && var0.isRuntimeActive() && var0.b.P() ? var0 : null;
   }

   public static boolean n() {
      if (H != null && H.isEnabled()) {
         PerformanceModule var0 = J();
         return var0 != null && var0.C.P();
      } else {
         return false;
      }
   }

   public static int g() {
      PerformanceModule var0 = H;
      return var0 == null ? -4932926 : var0.V.P();
   }

   public static ParticleStatus m(ParticleStatus var0) {
      if (H != null && H.isEnabled()) {
         PerformanceModule var1 = J();
         if (var1 != null && !var1.T.S("Unchanged")) {
            ParticleStatus var2 = var1.T.S("Minimal") ? ParticleStatus.MINIMAL : ParticleStatus.DECREASED;
            return var0.ordinal() >= var2.ordinal() ? var0 : var2;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static CloudStatus f(CloudStatus var0) {
      if (H != null && H.isEnabled()) {
         PerformanceModule var1 = J();
         return var1 != null && var1.v.P() ? CloudStatus.OFF : var0;
      } else {
         return var0;
      }
   }

   public static double u(double var0) {
      if (H != null && H.isEnabled()) {
         PerformanceModule var2 = J();
         return var2 == null ? var0 : var0 * var2.e.P() / 100.0;
      } else {
         return var0;
      }
   }

   public static m5.n W() {
      if (H != null && H.isEnabled()) {
         PerformanceModule var0 = J();
         if (var0 != null && !var0.n.S("Unchanged")) {
            return var0.n.S("Off") ? m5.n.N : m5.n.T;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static int G(int var0) {
      if (H != null && H.isEnabled()) {
         PerformanceModule var1 = H;
         return var1 != null && var1.isRuntimeActive() && var1.W.P() ? Math.min(var0, var1.Q.P().intValue()) : var0;
      } else {
         return var0;
      }
   }

   private void A() {
      if (this.isRuntimeActive()) {
         this.f();
      }
   }

   private void f() {
      mt.K("Performance", "Chunk updates={}; recording FPS limit={}", this.R.P(), this.W.P() ? this.Q.P().intValue() : "off");
      if (this.b.P()) {
         mt.K(
            "Performance",
            "Aggressive: single color={} ({}), particles={}, client effects={}, clouds={}, entity distance={}%",
            this.C.P(),
            this.V.getValueAsString(),
            this.T.P(),
            this.n.P(),
            this.v.P() ? "hidden" : "unchanged",
            this.e.P().intValue()
         );
      }
   }

   @Override
   public void onEnable() {
      this.f();
   }

   @Override
   public void onDisable() {
      mt.K("Performance", "Native render policy released; video preferences retained");
   }
}

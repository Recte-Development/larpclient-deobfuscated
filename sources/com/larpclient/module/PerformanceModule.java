package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.jk;
import com.larpclient.jx;
import com.larpclient.mo;
import com.larpclient.r4;
import java.util.List;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.PrioritizeChunkUpdates;
import net.minecraft.server.level.ParticleStatus;

public final class PerformanceModule extends Module {
   private static PerformanceModule w;
   public final lp m = this.addSetting(
      new lp(
         "Chunk Updates",
         "Balanced threads terrain updates but keeps player edits immediate; Threaded may delay visible block updates",
         "Balanced",
         "Balanced",
         "Threaded",
         "Unchanged"
      )
   );
   public final ln G = this.addSetting(
      new ln("Streaming FPS Limit", "Cap gameplay FPS to leave GPU headroom for recording; never raises Minecraft's existing limit", false)
   );
   public final lc Q = this.addSetting(new lc("Streaming FPS", "Gameplay frame limit while recording or streaming", 120.0, 30.0, 240.0, 1.0));
   public final ln F = this.addSetting(
      new ln("Aggressive Mode", "Applies the reductions below together while Performance is on. Your video, effect and module settings stay as saved", false)
   );
   public final ln c = this.addSetting(
      new ln(
         "Single Color Blocks",
         "Draws solid and cutout terrain in one color with its shapes, face shading and light. Water and glass keep their textures",
         false
      )
   );
   public final lv a = this.addSetting(new lv("Block Color", "Terrain color for Single Color Blocks", -4932926));
   public final lp H = this.addSetting(
      new lp("Particles", "Caps Minecraft's particle setting; Unchanged keeps your video setting", "Decreased", "Unchanged", "Decreased", "Minimal")
   );
   public final lp K = this.addSetting(
      new lp("Client Effects", "Caps Larp effect detail and glow; Unchanged keeps your Render quality choices", "Low", "Unchanged", "Low", "Off")
   );
   public final ln I = this.addSetting(new ln("Hide Clouds", "Skips cloud rendering", true));
   public final lc L = this.addSetting(new lc("Entity Distance", "Share of your entity render distance; 100 keeps it", 100.0, 25.0, 100.0, 5.0));

   public PerformanceModule() {
      super(
         "Performance",
         "Render quality for client effects always applies. Turn the module on for smoother chunk updates and an optional recording FPS cap",
         Category.PLAYER,
         -1
      );
      w = this;
      this.Q.x(this.G::getValue);
      this.m.W(var1 -> this.Q());
      this.G.W(var1 -> this.Q());
      this.Q.W(var1 -> this.Q());

      for (l6 var2 : this.r()) {
         if (var2 != this.F) {
            var2.x(this.F::getValue);
         }

         var2.W(var1 -> this.Q());
      }

      this.a.x(() -> this.F.getValue() && this.c.getValue());
      jk.f = PerformanceModule::K;
   }

   public List<l6<?>> E() {
      return List.of(this.m, this.G, this.Q);
   }

   public List<l6<?>> r() {
      return List.of(this.F, this.c, this.a, this.H, this.K, this.I, this.L);
   }

   public List<l6<?>> p() {
      lp var1 = new lp("Bloom", "Glow quality for effects that request bloom", jk.u.name(), "Off", "Low", "Medium", "High", "Ultra");
      var1.W(var0 -> jk.u = jk.x.valueOf(var0));
      lp var2 = new lp("Blur", "Blur quality behind the ClickGUI window and HUD panels", jk.E.name(), "Off", "Low", "Medium", "High", "Ultra");
      var2.W(var0 -> jk.E = jk.x.valueOf(var0));
      jk.x var3 = com.larpclient.lo.m();
      lp var4 = new lp(
         "Effects",
         "Particle detail, glow and refraction for trails, weather and After Dark",
         var3 == jk.x.V ? "Balanced" : var3.name(),
         "Off",
         "Low",
         "Balanced",
         "High",
         "Ultra"
      );
      var4.W(var1x -> {
         mo var2x = LarpClient.y().v();
         jk.x var3x = var1x.equals("Balanced") ? jk.x.V : jk.x.valueOf(var1x);
         if (!var2x.I(var3x)) {
            jk.x var4x = var2x.E().M;
            var4.setValue(var4x == jk.x.V ? "Balanced" : var4x.name());
         }
      });
      ln var5 = new ln("Soft particles", "Fade glow and smoke where they meet solid surfaces", jx.s);
      var5.W(var1x -> {
         mo var2x = LarpClient.y().v();
         if (!var2x.s(var1x)) {
            var5.setValue(var2x.E().J);
         }
      });
      return List.of(var1, var2, var4, var5);
   }

   public static PrioritizeChunkUpdates t(PrioritizeChunkUpdates var0) {
      if (w != null && w.isEnabled()) {
         PerformanceModule var1 = w;
         if (var1 == null || !var1.isRuntimeActive()) {
            return var0;
         } else if (var1.m.i("Threaded")) {
            return PrioritizeChunkUpdates.NONE;
         } else {
            return var1.m.i("Balanced") && var0 == PrioritizeChunkUpdates.NEARBY ? PrioritizeChunkUpdates.PLAYER_AFFECTED : var0;
         }
      } else {
         return var0;
      }
   }

   private static PerformanceModule W() {
      PerformanceModule var0 = w;
      return var0 != null && var0.isRuntimeActive() && var0.F.getValue() ? var0 : null;
   }

   public static boolean A() {
      if (w != null && w.isEnabled()) {
         PerformanceModule var0 = W();
         return var0 != null && var0.c.getValue();
      } else {
         return false;
      }
   }

   public static int G() {
      PerformanceModule var0 = w;
      return var0 == null ? -4932926 : var0.a.getValue();
   }

   public static ParticleStatus i(ParticleStatus var0) {
      if (w != null && w.isEnabled()) {
         PerformanceModule var1 = W();
         if (var1 != null && !var1.H.i("Unchanged")) {
            ParticleStatus var2 = var1.H.i("Minimal") ? ParticleStatus.MINIMAL : ParticleStatus.DECREASED;
            return var0.ordinal() >= var2.ordinal() ? var0 : var2;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static CloudStatus B(CloudStatus var0) {
      if (w != null && w.isEnabled()) {
         PerformanceModule var1 = W();
         return var1 != null && var1.I.getValue() ? CloudStatus.OFF : var0;
      } else {
         return var0;
      }
   }

   public static double j(double var0) {
      if (w != null && w.isEnabled()) {
         PerformanceModule var2 = W();
         return var2 == null ? var0 : var0 * var2.L.getValue() / 100.0;
      } else {
         return var0;
      }
   }

   public static jk.x K() {
      if (w != null && w.isEnabled()) {
         PerformanceModule var0 = W();
         if (var0 != null && !var0.K.i("Unchanged")) {
            return var0.K.i("Off") ? jk.x.i : jk.x.L;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static int i(int var0) {
      if (w != null && w.isEnabled()) {
         PerformanceModule var1 = w;
         return var1 != null && var1.isRuntimeActive() && var1.G.getValue() ? Math.min(var0, var1.Q.getValue().intValue()) : var0;
      } else {
         return var0;
      }
   }

   private void Q() {
      if (this.isRuntimeActive()) {
         this.A();
      }
   }

   private void A() {
      r4.V("Performance", "Chunk updates={}; recording FPS limit={}", this.m.getValue(), this.G.getValue() ? this.Q.getValue().intValue() : "off");
      if (this.F.getValue()) {
         r4.V(
            "Performance",
            "Aggressive: single color={} ({}), particles={}, client effects={}, clouds={}, entity distance={}%",
            this.c.getValue(),
            this.a.getValueAsString(),
            this.H.getValue(),
            this.K.getValue(),
            this.I.getValue() ? "hidden" : "unchanged",
            this.L.getValue().intValue()
         );
      }
   }

   @Override
   public void onEnable() {
      this.A();
   }

   @Override
   public void onDisable() {
      r4.V("Performance", "Native render policy released; video preferences retained");
   }
}

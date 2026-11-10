package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.gg;
import com.larpclient.he;
import com.larpclient.wa;
import java.util.List;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.PrioritizeChunkUpdates;
import net.minecraft.server.level.ParticleStatus;

public final class PerformanceModule extends Module {
   private static PerformanceModule G;
   public final l T = this.addSetting(
      new l(
         "Chunk Updates",
         "Balanced threads terrain updates but keeps player edits immediate; Threaded may delay visible block updates",
         "Balanced",
         "Balanced",
         "Threaded",
         "Unchanged"
      )
   );
   public final s t = this.addSetting(
      new s("Streaming FPS Limit", "Cap gameplay FPS to leave GPU headroom for recording; never raises Minecraft's existing limit", false)
   );
   public final e L = this.addSetting(new e("Streaming FPS", "Gameplay frame limit while recording or streaming", 120.0, 30.0, 240.0, 1.0));
   public final s r = this.addSetting(
      new s("Aggressive Mode", "Applies the reductions below together while Performance is on. Your video, effect and module settings stay as saved", false)
   );
   public final s y = this.addSetting(
      new s(
         "Single Color Blocks",
         "Draws solid and cutout terrain in one color with its shapes, face shading and light. Water and glass keep their textures",
         false
      )
   );
   public final i d = this.addSetting(new i("Block Color", "Terrain color for Single Color Blocks", -4932926));
   public final l g = this.addSetting(
      new l("Particles", "Caps Minecraft's particle setting; Unchanged keeps your video setting", "Decreased", "Unchanged", "Decreased", "Minimal")
   );
   public final l p = this.addSetting(
      new l("Client Effects", "Caps Larp effect detail and glow; Unchanged keeps your Render quality choices", "Low", "Unchanged", "Low", "Off")
   );
   public final s m = this.addSetting(new s("Hide Clouds", "Skips cloud rendering", true));
   public final e W = this.addSetting(new e("Entity Distance", "Share of your entity render distance; 100 keeps it", 100.0, 25.0, 100.0, 5.0));

   public PerformanceModule() {
      super(
         "Performance",
         "Render quality for client effects always applies. Turn the module on for smoother chunk updates and an optional recording FPS cap",
         Category.PLAYER,
         -1
      );
      G = this;
      this.L.P(this.t::getValue);
      this.T.o(var1 -> this.o());
      this.t.o(var1 -> this.o());
      this.L.o(var1 -> this.o());

      for (q var2 : this.Y()) {
         if (var2 != this.r) {
            var2.P(this.r::getValue);
         }

         var2.o(var1 -> this.o());
      }

      this.d.P(() -> this.r.getValue() && this.y.getValue());
      wa.C = PerformanceModule::e;
   }

   public List<q<?>> j() {
      return List.of(this.T, this.t, this.L);
   }

   public List<q<?>> Y() {
      return List.of(this.r, this.y, this.d, this.g, this.p, this.m, this.W);
   }

   public List<q<?>> A() {
      l var1 = new l("Bloom", "Glow quality for effects that request bloom", wa.h.name(), "Off", "Low", "Medium", "High", "Ultra");
      var1.o(var0 -> wa.h = wa.a.valueOf(var0));
      l var2 = new l("Blur", "Blur quality behind the ClickGUI window and HUD panels", wa.n.name(), "Off", "Low", "Medium", "High", "Ultra");
      var2.o(var0 -> wa.n = wa.a.valueOf(var0));
      wa.a var3 = com.larpclient.y.P();
      l var4 = new l(
         "Effects",
         "Particle detail, glow and refraction for trails and weather",
         var3 == wa.a.Y ? "Balanced" : var3.name(),
         "Off",
         "Low",
         "Balanced",
         "High",
         "Ultra"
      );
      var4.o(var1x -> {
         gg var2x = LarpClient.R().o();
         wa.a var3x = var1x.equals("Balanced") ? wa.a.Y : wa.a.valueOf(var1x);
         if (!var2x.D(var3x)) {
            wa.a var4x = var2x.g().V;
            var4.setValue(var4x == wa.a.Y ? "Balanced" : var4x.name());
         }
      });
      s var5 = new s("Soft particles", "Fade glow and smoke where they meet solid surfaces", he.C);
      var5.o(var1x -> {
         gg var2x = LarpClient.R().o();
         if (!var2x.v(var1x)) {
            var5.setValue(var2x.g().u);
         }
      });
      return List.of(var1, var2, var4, var5);
   }

   public static PrioritizeChunkUpdates y(PrioritizeChunkUpdates var0) {
      if (G != null && G.isEnabled()) {
         PerformanceModule var1 = G;
         if (var1 == null || !var1.isRuntimeActive()) {
            return var0;
         } else if (var1.T.O("Threaded")) {
            return PrioritizeChunkUpdates.NONE;
         } else {
            return var1.T.O("Balanced") && var0 == PrioritizeChunkUpdates.NEARBY ? PrioritizeChunkUpdates.PLAYER_AFFECTED : var0;
         }
      } else {
         return var0;
      }
   }

   private static PerformanceModule E() {
      PerformanceModule var0 = G;
      return var0 != null && var0.isRuntimeActive() && var0.r.getValue() ? var0 : null;
   }

   public static boolean D() {
      if (G != null && G.isEnabled()) {
         PerformanceModule var0 = E();
         return var0 != null && var0.y.getValue();
      } else {
         return false;
      }
   }

   public static int Q() {
      PerformanceModule var0 = G;
      return var0 == null ? -4932926 : var0.d.getValue();
   }

   public static ParticleStatus A(ParticleStatus var0) {
      if (G != null && G.isEnabled()) {
         PerformanceModule var1 = E();
         if (var1 != null && !var1.g.O("Unchanged")) {
            ParticleStatus var2 = var1.g.O("Minimal") ? ParticleStatus.MINIMAL : ParticleStatus.DECREASED;
            return var0.ordinal() >= var2.ordinal() ? var0 : var2;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static CloudStatus k(CloudStatus var0) {
      if (G != null && G.isEnabled()) {
         PerformanceModule var1 = E();
         return var1 != null && var1.m.getValue() ? CloudStatus.OFF : var0;
      } else {
         return var0;
      }
   }

   public static double e(double var0) {
      if (G != null && G.isEnabled()) {
         PerformanceModule var2 = E();
         return var2 == null ? var0 : var0 * var2.W.getValue() / 100.0;
      } else {
         return var0;
      }
   }

   public static wa.a e() {
      if (G != null && G.isEnabled()) {
         PerformanceModule var0 = E();
         if (var0 != null && !var0.p.O("Unchanged")) {
            return var0.p.O("Off") ? wa.a.Z : wa.a.P;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static int h(int var0) {
      if (G != null && G.isEnabled()) {
         PerformanceModule var1 = G;
         return var1 != null && var1.isRuntimeActive() && var1.t.getValue() ? Math.min(var0, var1.L.getValue().intValue()) : var0;
      } else {
         return var0;
      }
   }

   private void o() {
      if (this.isRuntimeActive()) {
         this.s();
      }
   }

   private void s() {
      com.larpclient.h.A("Performance", "Chunk updates={}; recording FPS limit={}", this.T.getValue(), this.t.getValue() ? this.L.getValue().intValue() : "off");
      if (this.r.getValue()) {
         com.larpclient.h.A(
            "Performance",
            "Aggressive: single color={} ({}), particles={}, client effects={}, clouds={}, entity distance={}%",
            this.y.getValue(),
            this.d.getValueAsString(),
            this.g.getValue(),
            this.p.getValue(),
            this.m.getValue() ? "hidden" : "unchanged",
            this.W.getValue().intValue()
         );
      }
   }

   @Override
   public void onEnable() {
      this.s();
   }

   @Override
   public void onDisable() {
      com.larpclient.h.A("Performance", "Native render policy released; video preferences retained");
   }
}

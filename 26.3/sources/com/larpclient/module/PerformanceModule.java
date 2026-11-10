package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._q;
import com.larpclient.a4;
import com.larpclient.aq;
import com.larpclient.d3;
import com.larpclient.nl;
import java.util.List;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.PrioritizeChunkUpdates;
import net.minecraft.server.level.ParticleStatus;

public final class PerformanceModule extends Module {
   private static PerformanceModule N;
   public final o f = this.addSetting(
      new o(
         "Chunk Updates",
         "Balanced threads terrain updates but keeps player edits immediate; Threaded may delay visible block updates",
         "Balanced",
         "Balanced",
         "Threaded",
         "Unchanged"
      )
   );
   public final m w = this.addSetting(
      new m("Streaming FPS Limit", "Cap gameplay FPS to leave GPU headroom for recording; never raises Minecraft's existing limit", false)
   );
   public final b g = this.addSetting(new b("Streaming FPS", "Gameplay frame limit while recording or streaming", 120.0, 30.0, 240.0, 1.0));
   public final m k = this.addSetting(
      new m("Aggressive Mode", "Applies the reductions below together while Performance is on. Your video, effect and module settings stay as saved", false)
   );
   public final m C = this.addSetting(
      new m(
         "Single Color Blocks",
         "Draws solid and cutout terrain in one color with its shapes, face shading and light. Water and glass keep their textures",
         false
      )
   );
   public final h u = this.addSetting(new h("Block Color", "Terrain color for Single Color Blocks", -4932926));
   public final o h = this.addSetting(
      new o("Particles", "Caps Minecraft's particle setting; Unchanged keeps your video setting", "Decreased", "Unchanged", "Decreased", "Minimal")
   );
   public final o K = this.addSetting(
      new o("Client Effects", "Caps Larp effect detail and glow; Unchanged keeps your Render quality choices", "Low", "Unchanged", "Low", "Off")
   );
   public final m i = this.addSetting(new m("Hide Clouds", "Skips cloud rendering", true));
   public final b e = this.addSetting(new b("Entity Distance", "Share of your entity render distance; 100 keeps it", 100.0, 25.0, 100.0, 5.0));

   public PerformanceModule() {
      super(
         "Performance",
         "Render quality for client effects always applies. Turn the module on for smoother chunk updates and an optional recording FPS cap",
         Category.PLAYER,
         -1
      );
      N = this;
      this.g.a(this.w::getValue);
      this.f.w(var1 -> this.T());
      this.w.w(var1 -> this.T());
      this.g.w(var1 -> this.T());

      for (r var2 : this.F()) {
         if (var2 != this.k) {
            var2.a(this.k::getValue);
         }

         var2.w(var1 -> this.T());
      }

      this.u.a(() -> this.k.getValue() && this.C.getValue());
      nl.Y = PerformanceModule::E;
   }

   public List<r<?>> I() {
      return List.of(this.f, this.w, this.g);
   }

   public List<r<?>> F() {
      return List.of(this.k, this.C, this.u, this.h, this.K, this.i, this.e);
   }

   public List<r<?>> A() {
      o var1 = new o("Bloom", "Glow quality for effects that request bloom", nl.x.name(), "Off", "Low", "Medium", "High", "Ultra");
      var1.w(var0 -> nl.x = nl.q.valueOf(var0));
      o var2 = new o("Blur", "Blur quality behind the ClickGUI window and HUD panels", nl.Q.name(), "Off", "Low", "Medium", "High", "Ultra");
      var2.w(var0 -> nl.Q = nl.q.valueOf(var0));
      nl.q var3 = a4.c();
      o var4 = new o(
         "Effects",
         "Particle detail, glow and refraction for trails and weather",
         var3 == nl.q.C ? "Balanced" : var3.name(),
         "Off",
         "Low",
         "Balanced",
         "High",
         "Ultra"
      );
      var4.w(var1x -> {
         aq var2x = LarpClient.N().h();
         nl.q var3x = var1x.equals("Balanced") ? nl.q.C : nl.q.valueOf(var1x);
         if (!var2x.W(var3x)) {
            nl.q var4x = var2x.k().W;
            var4.setValue(var4x == nl.q.C ? "Balanced" : var4x.name());
         }
      });
      m var5 = new m("Soft particles", "Fade glow and smoke where they meet solid surfaces", d3.y);
      var5.w(var1x -> {
         aq var2x = LarpClient.N().h();
         if (!var2x.r(var1x)) {
            var5.setValue(var2x.k().t);
         }
      });
      return List.of(var1, var2, var4, var5);
   }

   public static PrioritizeChunkUpdates l(PrioritizeChunkUpdates var0) {
      if (N != null && N.isEnabled()) {
         PerformanceModule var1 = N;
         if (var1 == null || !var1.isRuntimeActive()) {
            return var0;
         } else if (var1.f.w("Threaded")) {
            return PrioritizeChunkUpdates.NONE;
         } else {
            return var1.f.w("Balanced") && var0 == PrioritizeChunkUpdates.NEARBY ? PrioritizeChunkUpdates.PLAYER_AFFECTED : var0;
         }
      } else {
         return var0;
      }
   }

   private static PerformanceModule G() {
      PerformanceModule var0 = N;
      return var0 != null && var0.isRuntimeActive() && var0.k.getValue() ? var0 : null;
   }

   public static boolean h() {
      if (N != null && N.isEnabled()) {
         PerformanceModule var0 = G();
         return var0 != null && var0.C.getValue();
      } else {
         return false;
      }
   }

   public static int R() {
      PerformanceModule var0 = N;
      return var0 == null ? -4932926 : var0.u.getValue();
   }

   public static ParticleStatus Y(ParticleStatus var0) {
      if (N != null && N.isEnabled()) {
         PerformanceModule var1 = G();
         if (var1 != null && !var1.h.w("Unchanged")) {
            ParticleStatus var2 = var1.h.w("Minimal") ? ParticleStatus.MINIMAL : ParticleStatus.DECREASED;
            return var0.ordinal() >= var2.ordinal() ? var0 : var2;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static CloudStatus u(CloudStatus var0) {
      if (N != null && N.isEnabled()) {
         PerformanceModule var1 = G();
         return var1 != null && var1.i.getValue() ? CloudStatus.OFF : var0;
      } else {
         return var0;
      }
   }

   public static double u(double var0) {
      if (N != null && N.isEnabled()) {
         PerformanceModule var2 = G();
         return var2 == null ? var0 : var0 * var2.e.getValue() / 100.0;
      } else {
         return var0;
      }
   }

   public static nl.q E() {
      if (N != null && N.isEnabled()) {
         PerformanceModule var0 = G();
         if (var0 != null && !var0.K.w("Unchanged")) {
            return var0.K.w("Off") ? nl.q.q : nl.q.l;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static int a(int var0) {
      if (N != null && N.isEnabled()) {
         PerformanceModule var1 = N;
         return var1 != null && var1.isRuntimeActive() && var1.w.getValue() ? Math.min(var0, var1.g.getValue().intValue()) : var0;
      } else {
         return var0;
      }
   }

   private void T() {
      if (this.isRuntimeActive()) {
         this.K();
      }
   }

   private void K() {
      _q.z("Performance", "Chunk updates={}; recording FPS limit={}", this.f.getValue(), this.w.getValue() ? this.g.getValue().intValue() : "off");
      if (this.k.getValue()) {
         _q.z(
            "Performance",
            "Aggressive: single color={} ({}), particles={}, client effects={}, clouds={}, entity distance={}%",
            this.C.getValue(),
            this.u.getValueAsString(),
            this.h.getValue(),
            this.K.getValue(),
            this.i.getValue() ? "hidden" : "unchanged",
            this.e.getValue().intValue()
         );
      }
   }

   @Override
   public void onEnable() {
      this.K();
   }

   @Override
   public void onDisable() {
      _q.z("Performance", "Native render policy released; video preferences retained");
   }
}

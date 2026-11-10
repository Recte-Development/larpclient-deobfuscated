package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.b0;
import com.larpclient.lj;
import com.larpclient.q8;
import com.larpclient.qk;
import com.larpclient.t_;
import java.util.List;
import net.minecraft.class_4063;
import net.minecraft.class_4066;
import net.minecraft.class_6597;

public final class PerformanceModule extends Module {
   private static PerformanceModule G;
   public final li S = this.addSetting(
      new li(
         "Chunk Updates",
         "Balanced threads terrain updates but keeps player edits immediate; Threaded may delay visible block updates",
         "Balanced",
         "Balanced",
         "Threaded",
         "Unchanged"
      )
   );
   public final l3 g = this.addSetting(
      new l3("Streaming FPS Limit", "Cap gameplay FPS to leave GPU headroom for recording; never raises Minecraft's existing limit", false)
   );
   public final lo u = this.addSetting(new lo("Streaming FPS", "Gameplay frame limit while recording or streaming", 120.0, 30.0, 240.0, 1.0));
   public final l3 d = this.addSetting(
      new l3("Aggressive Mode", "Applies the reductions below together while Performance is on. Your video, effect and module settings stay as saved", false)
   );
   public final l3 N = this.addSetting(
      new l3(
         "Single Color Blocks",
         "Draws solid and cutout terrain in one color with its shapes, face shading and light. Water and glass keep their textures",
         false
      )
   );
   public final l0 J = this.addSetting(new l0("Block Color", "Terrain color for Single Color Blocks", -4932926));
   public final li A = this.addSetting(
      new li("Particles", "Caps Minecraft's particle setting; Unchanged keeps your video setting", "Decreased", "Unchanged", "Decreased", "Minimal")
   );
   public final li M = this.addSetting(
      new li("Client Effects", "Caps Larp effect detail and glow; Unchanged keeps your Render quality choices", "Low", "Unchanged", "Low", "Off")
   );
   public final l3 b = this.addSetting(new l3("Hide Clouds", "Skips cloud rendering", true));
   public final lo R = this.addSetting(new lo("Entity Distance", "Share of your entity render distance; 100 keeps it", 100.0, 25.0, 100.0, 5.0));

   public PerformanceModule() {
      super(
         "Performance",
         "Render quality for client effects always applies. Turn the module on for smoother chunk updates and an optional recording FPS cap",
         Category.PLAYER,
         -1
      );
      G = this;
      this.u.O(this.g::getValue);
      this.S.a(var1 -> this.b());
      this.g.a(var1 -> this.b());
      this.u.a(var1 -> this.b());

      for (lb var2 : this.O()) {
         if (var2 != this.d) {
            var2.O(this.d::getValue);
         }

         var2.a(var1 -> this.b());
      }

      this.J.O(() -> this.d.getValue() && this.N.getValue());
      t_.k = PerformanceModule::t;
   }

   public List<lb<?>> Z() {
      return List.of(this.S, this.g, this.u);
   }

   public List<lb<?>> O() {
      return List.of(this.d, this.N, this.J, this.A, this.M, this.b, this.R);
   }

   public List<lb<?>> m() {
      li var1 = new li("Bloom", "Glow quality for effects that request bloom", t_.O.name(), "Off", "Low", "Medium", "High", "Ultra");
      var1.a(var0 -> t_.O = t_.t.valueOf(var0));
      li var2 = new li("Blur", "Blur quality behind the ClickGUI window and HUD panels", t_.r.name(), "Off", "Low", "Medium", "High", "Ultra");
      var2.a(var0 -> t_.r = t_.t.valueOf(var0));
      t_.t var3 = b0.M();
      li var4 = new li(
         "Effects",
         "Particle detail, glow and refraction for trails and weather",
         var3 == t_.t.Q ? "Balanced" : var3.name(),
         "Off",
         "Low",
         "Balanced",
         "High",
         "Ultra"
      );
      var4.a(var1x -> {
         qk var2x = LarpClient.b().e();
         t_.t var3x = var1x.equals("Balanced") ? t_.t.Q : t_.t.valueOf(var1x);
         if (!var2x.I(var3x)) {
            t_.t var4x = var2x.F().Z;
            var4.setValue(var4x == t_.t.Q ? "Balanced" : var4x.name());
         }
      });
      l3 var5 = new l3("Soft particles", "Fade glow and smoke where they meet solid surfaces", lj.B);
      var5.a(var1x -> {
         qk var2x = LarpClient.b().e();
         if (!var2x.p(var1x)) {
            var5.setValue(var2x.F().l);
         }
      });
      return List.of(var1, var2, var4, var5);
   }

   public static class_6597 b(class_6597 var0) {
      if (G != null && G.isEnabled()) {
         PerformanceModule var1 = G;
         if (var1 == null || !var1.isRuntimeActive()) {
            return var0;
         } else if (var1.S.G("Threaded")) {
            return class_6597.field_34788;
         } else {
            return var1.S.G("Balanced") && var0 == class_6597.field_34790 ? class_6597.field_34789 : var0;
         }
      } else {
         return var0;
      }
   }

   private static PerformanceModule m() {
      PerformanceModule var0 = G;
      return var0 != null && var0.isRuntimeActive() && var0.d.getValue() ? var0 : null;
   }

   public static boolean w() {
      if (G != null && G.isEnabled()) {
         PerformanceModule var0 = m();
         return var0 != null && var0.N.getValue();
      } else {
         return false;
      }
   }

   public static int o() {
      PerformanceModule var0 = G;
      return var0 == null ? -4932926 : var0.J.getValue();
   }

   public static class_4066 Y(class_4066 var0) {
      if (G != null && G.isEnabled()) {
         PerformanceModule var1 = m();
         if (var1 != null && !var1.A.G("Unchanged")) {
            class_4066 var2 = var1.A.G("Minimal") ? class_4066.field_18199 : class_4066.field_18198;
            return var0.ordinal() >= var2.ordinal() ? var0 : var2;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static class_4063 D(class_4063 var0) {
      if (G != null && G.isEnabled()) {
         PerformanceModule var1 = m();
         return var1 != null && var1.b.getValue() ? class_4063.field_18162 : var0;
      } else {
         return var0;
      }
   }

   public static double z(double var0) {
      if (G != null && G.isEnabled()) {
         PerformanceModule var2 = m();
         return var2 == null ? var0 : var0 * var2.R.getValue() / 100.0;
      } else {
         return var0;
      }
   }

   public static t_.t t() {
      if (G != null && G.isEnabled()) {
         PerformanceModule var0 = m();
         if (var0 != null && !var0.M.G("Unchanged")) {
            return var0.M.G("Off") ? t_.t.A : t_.t.Y;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static int B(int var0) {
      if (G != null && G.isEnabled()) {
         PerformanceModule var1 = G;
         return var1 != null && var1.isRuntimeActive() && var1.g.getValue() ? Math.min(var0, var1.u.getValue().intValue()) : var0;
      } else {
         return var0;
      }
   }

   private void b() {
      if (this.isRuntimeActive()) {
         this.p();
      }
   }

   private void p() {
      q8.G("Performance", "Chunk updates={}; recording FPS limit={}", this.S.getValue(), this.g.getValue() ? this.u.getValue().intValue() : "off");
      if (this.d.getValue()) {
         q8.G(
            "Performance",
            "Aggressive: single color={} ({}), particles={}, client effects={}, clouds={}, entity distance={}%",
            this.N.getValue(),
            this.J.getValueAsString(),
            this.A.getValue(),
            this.M.getValue(),
            this.b.getValue() ? "hidden" : "unchanged",
            this.R.getValue().intValue()
         );
      }
   }

   @Override
   public void onEnable() {
      this.p();
   }

   @Override
   public void onDisable() {
      q8.G("Performance", "Native render policy released; video preferences retained");
   }
}

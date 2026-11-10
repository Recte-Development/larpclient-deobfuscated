package com.larpclient.module;

import com.larpclient.bd;
import com.larpclient.q8;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1109;
import net.minecraft.class_1111;
import net.minecraft.class_1113;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3419;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_1113.class_1114;
import net.minecraft.class_1140.class_11518;

public final class HitSoundsModule extends Module {
   private static HitSoundsModule F;
   public static final String[] m = new String[]{
      "Attack Crit",
      "Attack Strong",
      "Attack Weak",
      "Attack Sweep",
      "Attack Knockback",
      "Orb Pickup",
      "Level Up",
      "Totem Use",
      "Anvil Land",
      "Bell",
      "Anime",
      "Blade Ball",
      "Chink",
      "Click",
      "Copper Bell",
      "Crystal",
      "Glass 1",
      "Glass 2",
      "Metal Bat",
      "Neverlose",
      "Punch",
      "Slice",
      "Tap",
      "Water",
      "Rust Headshot"
   };
   private static final String H = "Impact";
   private final li a = this.addSetting(s("Sound", "Choose the hit sound", "Attack Crit", p(false)));
   private final lo I = this.addSetting(
      new lo(
         "Volume",
         "Custom hit sound loudness; 0% is silent, 100% is original volume, 200% boosts playback. Players and Master sliders also apply",
         1.0,
         0.0,
         2.0,
         0.05
      ) {
         @Override
         public String getEditValueAsString() {
            return Long.toString(Math.round(this.getValue() * 100.0));
         }

         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + "%";
         }

         @Override
         public void setFromEditString(String var1) {
            this.q(Double.parseDouble(var1.trim().replace("%", "")) / 100.0);
         }
      }
   );
   private final lo e = this.addSetting(new lo("Pitch", "1 is the original sound; lower is deeper and higher is brighter", 1.0, 0.5, 2.0, 0.05));
   private final l3 w = this.addSetting(new l3("Mob Sounds", "Also play hit sounds for mobs and other non-player entities", true));
   private final l3 A = this.addSetting(
      new l3("Mute normal hit sounds", "Mute vanilla attack impacts, critical hits and sweeps locally; other sounds stay audible", true)
   );
   private static final Set<class_2960> l = Set.of(
      class_2960.method_60656("entity.player.attack.strong"),
      class_2960.method_60656("entity.player.attack.weak"),
      class_2960.method_60656("entity.player.attack.crit"),
      class_2960.method_60656("entity.player.attack.sweep"),
      class_2960.method_60656("entity.player.attack.knockback"),
      class_2960.method_60656("entity.player.attack.nodamage")
   );
   private final lo W = this.addSetting(new lo("Pitch Random", "Random pitch variation around the chosen pitch", 0.0, 0.0, 1.0, 0.05));
   private final l3 d = this.addSetting(new l3("Only Players", "Play feedback only for player targets", false));
   private final l3 o = this.addSetting(new l3("Only Crit", "Play feedback only for critical attacks", false));
   private final l3 K = this.addSetting(new l3("Random Sound", "Choose a random sound on each hit", false));
   private final li B = this.addSetting(s("Second Sound", "Layer an additional hit sound", "None", p(true)));
   private class_1109 Q;
   private long L;
   private class_1109 U;
   private class_638 b;
   private class_746 u;
   private int N = Integer.MIN_VALUE;
   private boolean k;
   private boolean O;

   private static String[] p(boolean var0) {
      ArrayList var1 = new ArrayList<>(List.of(m));
      if (var0) {
         var1.addFirst("None");
      } else {
         var1.addAll(List.of("Glass", "Round Hit", "Crisp Tick"));
      }

      return var1.toArray(String[]::new);
   }

   private static li s(String var0, String var1, String var2, String[] var3) {
      return new li(var0, var1, var2, var3) {
         @Override
         public boolean acceptsValue(String var1) {
            return "Impact".equals(var1) || super.acceptsValue(var1);
         }

         @Override
         public void setValue(String var1) {
            super.setValue("Impact".equals(var1) ? "Rust Headshot" : var1);
         }
      };
   }

   public HitSoundsModule() {
      super("Hit Sounds", "Play custom sounds when you hit targets", Category.VISUALS, -1);
      F = this;
      this.w.O(() -> !this.d.getValue());
      this.a.O(() -> !this.K.getValue());
      this.A.a(var0 -> q8.G("HitSounds", "Vanilla attack sounds muted: {}", var0));
      this.a.a(var0 -> q8.G("HitSounds", "Sound selected: {}", var0));
   }

   public static HitSoundsModule B() {
      return F;
   }

   public List<lb<?>> S() {
      return List.of(this.I, this.a, this.e, this.W, this.K, this.B);
   }

   public List<lb<?>> Y() {
      return List.of(this.d, this.o, this.A, this.w);
   }

   public boolean D(class_1113 var1) {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.A.getValue()
            && var1 != null
            && var1 != this.U
            && var1 != this.Q
            && (this.O || System.nanoTime() <= this.L)
            && l.contains(var1.method_4775());
   }

   public class_2960 f() {
      return T(this.a.getValue());
   }

   public static class_2960 T(String var0) {
      String var1 = switch (var0) {
         case "Attack Crit" -> "entity.player.attack.crit";
         case "Attack Strong" -> "entity.player.attack.strong";
         case "Attack Weak" -> "entity.player.attack.weak";
         case "Attack Sweep" -> "entity.player.attack.sweep";
         case "Attack Knockback" -> "entity.player.attack.knockback";
         case "Orb Pickup" -> "entity.experience_orb.pickup";
         case "Level Up" -> "entity.player.levelup";
         case "Totem Use" -> "item.totem.use";
         case "Anvil Land" -> "block.anvil.land";
         case "Bell" -> "block.bell.use";
         default -> null;
      };
      if (var1 != null) {
         return class_2960.method_60656(var1);
      }

      String var5 = switch (var0) {
         case "Glass" -> "glass";
         case "Round Hit" -> "round_hit";
         case "Crisp Tick" -> "crisp_tick";
         case "Rust Headshot" -> "impact";
         default -> "clarity_" + var0.toLowerCase(Locale.ROOT).replace(" ", "");
      };
      return class_2960.method_60655("larp-client", "hit." + var5);
   }

   public class_1109 C() {
      return this.r(this.a.getValue());
   }

   private class_1109 r(String var1) {
      float var2 = (float)Math.clamp(this.e.getValue() + ThreadLocalRandom.current().nextDouble(-1.0, 1.0) * this.W.getValue(), 0.5, 2.0);
      return new class_1109(
         T(var1), class_3419.field_15248, this.I.getValue().floatValue(), var2, class_1113.method_43221(), false, 0, class_1114.field_5478, 0.0, 0.0, 0.0, true
      );
   }

   public void J(class_1297 var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && var1 != null && var1 != this.getPlayer() && var1.method_5805() && !var1.method_31481()) {
            if (!this.d.getValue() && this.w.getValue() || var1 instanceof class_1657) {
               if (!this.o.getValue() || bd.a(this.getPlayer(), var1)) {
                  class_310 var2 = getMc();
                  this.h();
                  int var3 = var2.field_1724.field_6012;
                  if (this.N != var3) {
                     this.N = var3;
                     this.L = System.nanoTime() + 800000000L;
                     if (this.I.getValue() <= 0.0) {
                        this.C("Muted by Hit Sounds Volume=0");
                     } else {
                        try {
                           this.d(false);
                        } catch (RuntimeException var5) {
                           q8.u("HitSounds", "Local sound playback failed; disabling Hit Sounds", var5);
                           this.setEnabled(false);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void b() {
      class_310 var1 = getMc();
      if (var1 != null) {
         try {
            this.h();
            this.d(true);
         } catch (RuntimeException var3) {
            q8.u("HitSounds", "Sound preview failed; reload resources and retry", var3);
         }
      }
   }

   public void a() {
      if (this.O) {
         this.r();
      }
   }

   private void d(boolean var1) {
      class_310 var2 = getMc();
      if (this.U != null) {
         var2.method_1483().method_4870(this.U);
      }

      if (this.Q != null) {
         var2.method_1483().method_4870(this.Q);
      }

      String var3 = this.K.getValue() ? m[ThreadLocalRandom.current().nextInt(m.length)] : this.a.getValue();
      this.U = this.r(var3);
      this.O = var1;
      class_11518 var4 = var2.method_1483().method_4873(this.U);
      this.Q = this.B.G("None") ? null : this.r(this.B.getValue());
      if (this.Q != null) {
         var2.method_1483().method_4873(this.Q);
      }

      class_1111 var5 = this.U.method_4776();
      String var6 = (var1 ? "Preview" : "Attack received")
         + "; sound="
         + this.f()
         + ", playback="
         + var4
         + ", moduleVolume="
         + this.I.getValue()
         + ", Players/Master gain="
         + var2.field_1690.method_71978(class_3419.field_15248)
         + ", resolved sample="
         + (var5 == null ? "unresolved" : var5.method_4767())
         + ", resolved volume="
         + (var5 == null ? 0.0F : this.U.method_4781())
         + ", resolved pitch="
         + (var5 == null ? 0.0F : this.U.method_4782());
      if (var1) {
         q8.G("HitSounds", var6);
      } else {
         this.C(var6);
      }
   }

   private void C(String var1) {
      if (!this.k) {
         this.k = (boolean)1;
         q8.G("HitSounds", var1);
      }
   }

   @Override
   public void onEnable() {
      this.r();
      this.k = (boolean)0;
      q8.G("HitSounds", "Local attack feedback enabled: sound={}, reference sample gain, paired local voices, Minecraft Players volume", this.a.getValue());
   }

   @Override
   public void onTick() {
      this.h();
   }

   @Override
   public void onDisable() {
      this.r();
   }

   private void h() {
      class_310 var1 = getMc();
      if (this.b != var1.field_1687 || this.u != var1.field_1724) {
         this.r();
         this.b = var1.field_1687;
         this.u = var1.field_1724;
      }
   }

   private void r() {
      class_310 var1 = getMc();
      class_1109 var2 = this.U;
      if (this.Q != null && var1 != null) {
         var1.method_1483().method_4870(this.Q);
      }

      this.Q = null;
      this.L = 0L;
      this.U = null;
      this.O = (boolean)0;
      this.b = null;
      this.u = null;
      this.N = Integer.MIN_VALUE;
      if (var2 != null && var1 != null) {
         var1.method_1483().method_4870(var2);
      }
   }
}

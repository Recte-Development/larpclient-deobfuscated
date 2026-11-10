package com.larpclient.module;

import com.larpclient.cb;
import com.larpclient.eb;
import com.larpclient.fj;
import com.larpclient.fp;
import com.larpclient.q1;
import com.larpclient.q8;
import com.larpclient.s5;
import com.larpclient.w6;
import com.larpclient.wp;
import com.larpclient.yd;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.class_10055;
import net.minecraft.class_10185;
import net.minecraft.class_10799;
import net.minecraft.class_11890;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_315;
import net.minecraft.class_332;
import net.minecraft.class_408;
import net.minecraft.class_437;
import net.minecraft.class_5498;
import net.minecraft.class_746;
import net.minecraft.class_1140.class_11518;

public final class EmotesModule extends Module {
   private static final int V = 3;
   private static final int x = 4;
   private static EmotesModule f;
   public final lw d = this.addSetting(new lw("Wheel Key", "Hold to open the emote wheel, release over an emote to play it", 66));
   private final li[] I = new li[]{
      this.addSetting(new EmotesModule.f("Slot 1", "Top emote on the wheel", "Maybach Bounce")),
      this.addSetting(new EmotesModule.f("Slot 2", "Right emote on the wheel", "Money Spread")),
      this.addSetting(new EmotesModule.f("Slot 3", "Bottom emote on the wheel", "Crypto Mining")),
      this.addSetting(new EmotesModule.f("Slot 4", "Left emote on the wheel", "Floss"))
   };
   public final l3 B = this.addSetting(new l3("Third Person", "Switch to third person while an emote plays, then switch back", true));
   public final lo u = this.addSetting(
      new lo(
         "Music Volume",
         "Loudness of your emote songs from the emote music resource pack; 0% is silent. Players and Master sliders also apply",
         0.3,
         0.0,
         1.0,
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
   private cb s;
   private fj.g b;
   private String O = "";
   private int T;
   private int z;
   private boolean A;
   private boolean t;
   private boolean L;
   private class_5498 a = class_5498.field_26664;
   private class_5498 y = class_5498.field_26665;
   private final float[] i = new float[cb.w];
   private float F;
   private int p;
   private Object G;
   private Object D;
   private yd o;
   public static int X;
   private static final String[] c;

   public EmotesModule() {
      super("Emotes", "Hold a key to pick an emote from a wheel, like Fortnite", Category.RENDER, -1);
      f = this;
   }

   public static EmotesModule s() {
      return f;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.E);
   }

   public List<String> f() {
      return Arrays.stream(this.I).map(lb::getValue).toList();
   }

   public int N() {
      return this.d.getValue();
   }

   public boolean n() {
      return !this.isEnabled() ? false : this.s != null && !this.A;
   }

   public String S() {
      return this.n() ? this.O : "";
   }

   @Override
   public void onEnable() {
      this.t = this.K();
      q8.v("Emotes", "Ready; wheel key={}, slots={}", this.d.getValue(), this.f());
   }

   @Override
   public void onDisable() {
      if (getMc().field_1755 instanceof eb var1) {
         var1.method_25419();
      }

      this.s("module disabled");
   }

   @Override
   public void onTick() {
      class_310 var1 = getMc();
      byte var2 = this.K();
      boolean var4 = false;
      byte var3 = var2;
      int var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         boolean var6 = false;
         byte var5 = this.t;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0 && var1.field_1755 == null && var1.method_18506() == null) {
            int var12 = var1.method_1569();
            boolean var8 = false;
            byte var7 = (byte)var12;
            var12 = Integer.signum(var7);
            if (((var12 | -var12) >>> 31 ^ 1) == 0 && var1.field_1724 != null) {
               int var14 = var1.field_1724.method_5805();
               boolean var10 = false;
               byte var9 = (byte)var14;
               var14 = Integer.signum(var9);
               if (((var14 | -var14) >>> 31 ^ 1) == 0) {
                  var1.method_1507(new eb(this));
                  q8.v("Emotes", "Wheel opened");
               }
            }
         }
      }

      this.t = (boolean)var2;
      this.P();
   }

   private boolean K() {
      class_310 var1 = getMc();
      return var1.method_22683() != null && s5.m(var1.method_22683().method_4490(), this.d.getValue());
   }

   public void Y(int var1) {
      if (this.isEnabled()) {
         if (var1 >= 0 && var1 < this.I.length && this.isRuntimeActive() && this.isInGame()) {
            fj.g var2 = fj.e(this.I[var1].getValue());
            if (var2 != null) {
               cb var3 = fj.i(var2);
               if (var3 != null) {
                  class_310 var4 = getMc();
                  class_746 var5 = var4.field_1724;
                  this.s = var3;
                  this.b = var2;
                  this.O = var2.label();
                  this.T = this.z = 0;
                  this.A = (boolean)0;
                  this.G = var5;
                  this.D = var4.field_1687;
                  this.p = var5.field_6235;
                  this.F = var5.method_36454();
                  this.b(var2);
                  this.q(var2);
                  String[] var6 = c;
                  String var10000 = var6[2];
                  String var10001 = var6[0];
                  Object[] var10002 = new Object[]{this.O, (var1 | 1) + (var1 & 1), null, null};
                  Object var10005;
                  if (this.L) {
                     var10005 = this.y;
                  } else {
                     var6 = c;
                     var10005 = var6[3];
                  }

                  var10002[2] = var10005;
                  var10002[3] = var2.prop() == null ? c[1] : var2.prop();
                  q8.G(var10000, var10001, var10002);
               }
            }
         }
      }
   }

   private void q(fj.g var1) {
      class_315 var2 = getMc().field_1690;
      class_5498 var3 = var2.method_31044();
      class_5498 var4 = var1.prop() != null ? class_5498.field_26666 : class_5498.field_26665;
      int var5 = this.L && var3 == this.y ? 1 : 0;
      int var6 = !var3.method_31034() && var5 == 0 && (var1.prop() == null || var3 != class_5498.field_26665) ? 0 : 1;
      if (this.B.getValue() && wp.o() == wp.z.D && var6 != 0 && var3 != var4) {
         if (var5 == 0) {
            this.a = var3;
         }

         var2.method_31043(var4);
         this.L = (boolean)1;
         this.y = var4;
      }
   }

   public void M(String var1) {
      if (this.s != null && !this.A) {
         this.A = (boolean)1;
         this.z = 0;
         if (this.o != null) {
            this.o.B();
         }

         this.C();
         q8.G("Emotes", "Stopped {} after {} ticks: {}", this.O, this.T, var1);
      }
   }

   private void s(String var1) {
      if (this.s != null && !this.A) {
         q8.G("Emotes", "Stopped {} after {} ticks: {}", this.O, this.T, var1);
      }

      this.C();
      this.w();
      this.s = null;
      this.b = null;
      this.O = "";
      this.A = (boolean)0;
      this.G = this.D = null;
   }

   private void b(fj.g var1) {
      this.w();
      if (!(this.u.getValue() <= 0.0)) {
         if (getMc().method_1483().method_4869(var1.music()) == null) {
            q8.v("Emotes", "No music for {}: add {} with the emote music resource pack", var1.label(), var1.music());
         } else {
            yd var2 = new yd(var1, this.u::getValue, 4);

            try {
               class_11518 var3 = getMc().method_1483().method_4873(var2);
               this.o = var2;
               q8.v("Emotes", "Music {} started at {}% volume: {}", var1.music(), Math.round(this.u.getValue() * 100.0), var3);
            } catch (RuntimeException var4) {
               q8.O("Emotes", "Emote music failed to start; the dance plays silently, reload resources to retry", var4);
            }
         }
      }
   }

   private void w() {
      if (this.o != null) {
         yd var1 = this.o;
         this.o = null;

         try {
            getMc().method_1483().method_4870(var1);
         } catch (RuntimeException var3) {
            q8.O("Emotes", "Emote music stop failed; it ends with the sound engine", var3);
         }
      }
   }

   private void C() {
      class_315 var1 = getMc().field_1690;
      if (this.L && var1.method_31044() == this.y) {
         var1.method_31043(this.a);
      }

      this.L = (boolean)0;
   }

   private void P() {
      if (this.s != null) {
         class_310 var1 = getMc();
         class_746 var2 = var1.field_1724;
         if (var2 == null || var2 != this.G || var1.field_1687 != this.D || !var2.method_5805()) {
            this.s("world or player changed");
         } else if (this.A) {
            if (++this.z >= 4) {
               this.s("blend finished");
            }
         } else {
            this.T++;
            String var3 = this.d(var2);
            if (var3 != null) {
               this.M(var3);
            } else if (this.s.d(this.T)) {
               this.M("emote finished");
            }
         }
      }
   }

   private String d(class_746 var1) {
      class_10185 var2 = var1.field_3913.field_54155;
      if (var2.comp_3159() || var2.comp_3160() || var2.comp_3161() || var2.comp_3162()) {
         return "movement input";
      } else if (var2.comp_3163()) {
         return "jump";
      } else if (var2.comp_3164()) {
         return "sneak";
      } else {
         int var3 = var1.field_6235;
         int var4 = var3 > this.p ? 1 : 0;
         this.p = var3;
         if (var4 != 0) {
            return "took damage";
         } else if (w6.l(var1) || var1.method_6115()) {
            return "attack or item use";
         } else {
            return !var1.method_5765() && !var1.method_6128() && !var1.method_5681() && !var1.method_6113() && !var1.method_31549().field_7479
               ? null
               : "pose changed";
         }
      }
   }

   public static void V(class_11890 var0, class_10055 var1, float var2) {
      if (f != null && f.isEnabled()) {
         q1 var3 = (q1)var1;
         EmotesModule var4 = f;
         if (var4 != null && var4.s != null && var4.isRuntimeActive() && var0 == getMc().field_1724) {
            float var5 = var4.L(var2);
            var3.larp$setEmoteWeight(var5);
            if (!(var5 <= 0.0F)) {
               int var6 = var4.T;
               float var7 = var4.A ? 0.0F : var2;
               var4.s.z(var6, var7, var3.larp$emoteSample());
               var3.larp$setEmoteProp(var4.b.prop(), var4.s.K(var6, var7));
               class_437 var8 = getMc().field_1755;
               if (var8 == null || var8 instanceof class_408 || var8 instanceof eb) {
                  var1.field_53446 = var4.F;
                  var1.field_53447 = 0.0F;
                  var1.field_53448 = 0.0F;
               }
            }
         } else {
            var3.larp$setEmoteWeight(0.0F);
            var3.larp$setEmoteProp(null, 0.0F);
         }
      }
   }

   private float L(float var1) {
      float var2 = Math.min(1.0F, (this.T + (this.A ? 0.0F : var1)) / 3.0F);
      float var3 = this.A ? 1.0F - Math.min(1.0F, (this.z + var1) / 4.0F) : 1.0F;
      float var4 = Math.min(var2, var3);
      return var4 * var4 * (3.0F - 2.0F * var4);
   }

   public static EmotesModule.j C(float var0) {
      EmotesModule var1 = f;
      if (var1 != null && var1.s != null && var1.b != null && var1.b.prop() != null && !var1.b.handProp() && var1.isRuntimeActive()) {
         class_746 var2 = getMc().field_1724;
         if (var2 != null && var2 == var1.G) {
            var1.s.z(var1.T, var1.A ? 0.0F : var0, var1.i);
            int var3 = cb.x.B.ordinal() * 10;
            float[] var4 = new float[7];

            for (int var5 = 0; var5 < 7; var5++) {
               float var6 = var1.i[var3 + (var5 == 6 ? 7 : var5)];
               var4[var5] = Float.isNaN(var6) ? (var5 == 6 ? 1 : 0) : var6;
            }

            float var7 = var1.A ? Math.min(1.0F, (var1.z + var0) / 4.0F) : 0.0F;
            float var8 = 1.0F - var7 * var7 * (3.0F - 2.0F * var7);
            return new EmotesModule.j(
               var1.b.prop(),
               var2.method_30950(var0),
               var1.F,
               var2.method_55693(),
               var4,
               var8,
               var1.T + (var1.A ? 0.0F : var0),
               var1.A ? var1.z + var0 : 0.0F,
               var1.A
            );
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static boolean Q(class_10055 var0) {
      return f != null && f.isEnabled() ? var0 instanceof q1 var1 && var1.larp$emoteWeight() > 0.0F : false;
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      } else {
         int var1 = this.d.getValue();
         if (this.n()) {
            return this.O;
         } else {
            return var1 < 0 ? "Wheel key unbound" : "Hold " + s5.S(var1);
         }
      }
   }

   private static final class f extends li {
      f(String var1, String var2, String var3) {
         super(var1, var2, var3, fj.e());
      }

      public int previewSize() {
         return 44;
      }

      public void drawPreview(class_332 var1, String var2, int var3, int var4, int var5) {
         fj.g var6 = fj.e(var2);
         if (var6 != null) {
            short var7 = 192;
            var1.method_25293(class_10799.field_56883, var6.icon(), var3, var4, 0.0F, 0.0F, var5, var5, var7, var7, var7, var7, fp.R(-1));
         }
      }
   }

   public record j(String prop, class_243 feet, float facing, float avatarScale, float[] track, float fade, float age, float stopAge, boolean stopping) {
   }
}

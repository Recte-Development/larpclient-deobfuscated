package com.larpclient.module;

import com.larpclient._8;
import com.larpclient.cc;
import com.larpclient.cu;
import com.larpclient.m7;
import com.larpclient.mt;
import com.larpclient.p0;
import com.larpclient.t5;
import com.larpclient.tq;
import com.larpclient.uy;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.sounds.SoundEngine.PlayResult;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec3;

public final class EmotesModule extends Module {
   private static final int B = 3;
   private static final int v = 4;
   private static EmotesModule A;
   public final dv q = this.addSetting(new dv("Wheel Key", "Hold to open the emote wheel, release over an emote to play it", 66));
   private final d6[] h = new d6[]{
      this.addSetting(new d6("Slot 1", "Top emote on the wheel", "Take the L", p0.y())),
      this.addSetting(new d6("Slot 2", "Right emote on the wheel", "Floss", p0.y())),
      this.addSetting(new d6("Slot 3", "Bottom emote on the wheel", "Maybach Bounce", p0.y())),
      this.addSetting(new d6("Slot 4", "Left emote on the wheel", "Money Spread", p0.y()))
   };
   public final dh d = this.addSetting(new dh("Third Person", "Switch to third person while an emote plays, then switch back", true));
   public final d9 w = this.addSetting(
      new d9(
         "Music Volume",
         "Loudness of your emote songs from the emote music resource pack; 0% is silent. Players and Master sliders also apply",
         0.3,
         0.0,
         1.0,
         0.05
      ) {
         @Override
         public String getEditValueAsString() {
            return Long.toString(Math.round(this.P() * 100.0));
         }

         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + "%";
         }

         @Override
         public void setFromEditString(String var1) {
            this.o(Double.parseDouble(var1.trim().replace("%", "")) / 100.0);
         }
      }
   );
   private uy j;
   private p0.u z;
   private String m = "";
   private int W;
   private int Z;
   private boolean N;
   private boolean S;
   private boolean D;
   private CameraType T = CameraType.FIRST_PERSON;
   private CameraType Q = CameraType.THIRD_PERSON_BACK;
   private final float[] Y = new float[uy.Y];
   private float i;
   private int u;
   private Object r;
   private Object G;
   private m7 R;
   public static boolean E;
   private static final String[] a;

   public EmotesModule() {
      super("Emotes", "Hold a key to pick an emote from a wheel, like Fortnite", Category.RENDER, -1);
      A = this;
   }

   public static EmotesModule T() {
      return A;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.m);
   }

   public List<String> M() {
      return Arrays.stream(this.h).map(df::P).toList();
   }

   public int n() {
      return this.q.P();
   }

   public boolean p() {
      return !this.isEnabled() ? false : this.j != null && !this.N;
   }

   public String Q() {
      return this.p() ? this.m : "";
   }

   @Override
   public void onEnable() {
      this.S = this.g();
      mt.U("Emotes", "Ready; wheel key={}, slots={}", this.q.P(), this.M());
   }

   @Override
   public void onDisable() {
      if (getMc().gui.screen() instanceof t5 var1) {
         var1.onClose();
      }

      this.M("module disabled");
   }

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      byte var2 = this.g();
      boolean var4 = false;
      byte var3 = var2;
      int var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         boolean var6 = false;
         byte var5 = this.S;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0 && var1.gui.screen() == null && var1.gui.overlay() == null) {
            int var12 = var1.isWindowActive();
            boolean var8 = false;
            byte var7 = (byte)var12;
            var12 = Integer.signum(var7);
            if (((var12 | -var12) >>> 31 ^ 1) == 0 && var1.player != null) {
               int var14 = var1.player.isAlive();
               boolean var10 = false;
               byte var9 = (byte)var14;
               var14 = Integer.signum(var9);
               if (((var14 | -var14) >>> 31 ^ 1) == 0) {
                  var1.gui.setScreen(new t5(this));
                  mt.U("Emotes", "Wheel opened");
               }
            }
         }
      }

      this.S = (boolean)var2;
      this.d();
   }

   private boolean g() {
      Minecraft var1 = getMc();
      return var1.getWindow() != null && cc.N(var1.getWindow().handle(), this.q.P());
   }

   public void t(int var1) {
      if (this.isEnabled()) {
         if (var1 >= 0 && var1 < this.h.length && this.isRuntimeActive() && this.isInGame()) {
            p0.u var2 = p0.X(this.h[var1].P());
            if (var2 != null) {
               uy var3 = p0.x(var2);
               if (var3 != null) {
                  Minecraft var4 = getMc();
                  LocalPlayer var5 = var4.player;
                  this.j = var3;
                  this.z = var2;
                  this.m = var2.label();
                  this.W = this.Z = 0;
                  this.N = (boolean)0;
                  this.r = var5;
                  this.G = var4.level;
                  this.u = var5.hurtTime;
                  this.i = var5.getYRot();
                  this.i(var2);
                  this.z(var2);
                  String[] var6 = a;
                  String var10000 = var6[0];
                  String var10001 = var6[1];
                  Object[] var10002 = new Object[]{this.m, (var1 | 1) + (var1 & 1), null, null};
                  Object var10005;
                  if (this.D) {
                     var10005 = this.Q;
                  } else {
                     var6 = a;
                     var10005 = var6[3];
                  }

                  var10002[2] = var10005;
                  var10002[3] = var2.prop() == null ? a[2] : var2.prop();
                  mt.K(var10000, var10001, var10002);
               }
            }
         }
      }
   }

   private void z(p0.u var1) {
      Options var2 = getMc().options;
      CameraType var3 = var2.getCameraType();
      CameraType var4 = var1.prop() != null ? CameraType.THIRD_PERSON_FRONT : CameraType.THIRD_PERSON_BACK;
      int var5 = this.D && var3 == this.Q ? 1 : 0;
      int var6 = !var3.isFirstPerson() && var5 == 0 && (var1.prop() == null || var3 != CameraType.THIRD_PERSON_BACK) ? 0 : 1;
      if (this.d.P() && tq.l() == tq.y.A && var6 != 0 && var3 != var4) {
         if (var5 == 0) {
            this.T = var3;
         }

         var2.setCameraType(var4);
         this.D = (boolean)1;
         this.Q = var4;
      }
   }

   public void p(String var1) {
      if (this.j != null && !this.N) {
         this.N = (boolean)1;
         this.Z = 0;
         if (this.R != null) {
            this.R.t();
         }

         this.V();
         mt.K("Emotes", "Stopped {} after {} ticks: {}", this.m, this.W, var1);
      }
   }

   private void M(String var1) {
      if (this.j != null && !this.N) {
         mt.K("Emotes", "Stopped {} after {} ticks: {}", this.m, this.W, var1);
      }

      this.V();
      this.i();
      this.j = null;
      this.z = null;
      this.m = "";
      this.N = (boolean)0;
      this.r = this.G = null;
   }

   private void i(p0.u var1) {
      this.i();
      if (!(this.w.P() <= 0.0)) {
         if (getMc().getSoundManager().getSoundEvent(var1.music()) == null) {
            mt.U("Emotes", "No music for {}: add {} with the emote music resource pack", var1.label(), var1.music());
         } else {
            m7 var2 = new m7(var1, this.w::P, 4);

            try {
               PlayResult var3 = getMc().getSoundManager().play(var2);
               this.R = var2;
               mt.U("Emotes", "Music {} started at {}% volume: {}", var1.music(), Math.round(this.w.P() * 100.0), var3);
            } catch (RuntimeException var4) {
               mt.B("Emotes", "Emote music failed to start; the dance plays silently, reload resources to retry", var4);
            }
         }
      }
   }

   private void i() {
      if (this.R != null) {
         m7 var1 = this.R;
         this.R = null;

         try {
            getMc().getSoundManager().stop(var1);
         } catch (RuntimeException var3) {
            mt.B("Emotes", "Emote music stop failed; it ends with the sound engine", var3);
         }
      }
   }

   private void V() {
      Options var1 = getMc().options;
      if (this.D && var1.getCameraType() == this.Q) {
         var1.setCameraType(this.T);
      }

      this.D = (boolean)0;
   }

   private void d() {
      if (this.j != null) {
         Minecraft var1 = getMc();
         LocalPlayer var2 = var1.player;
         if (var2 == null || var2 != this.r || var1.level != this.G || !var2.isAlive()) {
            this.M("world or player changed");
         } else if (this.N) {
            if (++this.Z >= 4) {
               this.M("blend finished");
            }
         } else {
            this.W++;
            String var3 = this.w(var2);
            if (var3 != null) {
               this.p(var3);
            } else if (this.j.p(this.W)) {
               this.p("emote finished");
            }
         }
      }
   }

   private String w(LocalPlayer var1) {
      Input var2 = var1.input.keyPresses;
      if (var2.forward() || var2.backward() || var2.left() || var2.right()) {
         return "movement input";
      } else if (var2.jump()) {
         return "jump";
      } else if (var2.shift()) {
         return "sneak";
      } else {
         int var3 = var1.hurtTime;
         int var4 = var3 > this.u ? 1 : 0;
         this.u = var3;
         if (var4 != 0) {
            return "took damage";
         } else if (_8.Q(var1) || var1.isUsingItem()) {
            return "attack or item use";
         } else {
            return !var1.isPassenger() && !var1.isFallFlying() && !var1.isSwimming() && !var1.isSleeping() && !var1.getAbilities().flying
               ? null
               : "pose changed";
         }
      }
   }

   public static void z(Avatar var0, AvatarRenderState var1, float var2) {
      if (A != null && A.isEnabled()) {
         cu var3 = (cu)var1;
         EmotesModule var4 = A;
         if (var4 != null && var4.j != null && var4.isRuntimeActive() && var0 == getMc().player) {
            float var5 = var4.S(var2);
            var3.larp$setEmoteWeight(var5);
            if (!(var5 <= 0.0F)) {
               int var6 = var4.W;
               float var7 = var4.N ? 0.0F : var2;
               var4.j.p(var6, var7, var3.larp$emoteSample());
               var3.larp$setEmoteProp(var4.z.prop(), var4.j.W(var6, var7));
               Screen var8 = getMc().gui.screen();
               if (var8 == null || var8 instanceof ChatScreen || var8 instanceof t5) {
                  var1.bodyRot = var4.i;
                  var1.yRot = 0.0F;
                  var1.xRot = 0.0F;
               }
            }
         } else {
            var3.larp$setEmoteWeight(0.0F);
            var3.larp$setEmoteProp(null, 0.0F);
         }
      }
   }

   private float S(float var1) {
      float var2 = Math.min(1.0F, (this.W + (this.N ? 0.0F : var1)) / 3.0F);
      float var3 = this.N ? 1.0F - Math.min(1.0F, (this.Z + var1) / 4.0F) : 1.0F;
      float var4 = Math.min(var2, var3);
      return var4 * var4 * (3.0F - 2.0F * var4);
   }

   public static EmotesModule.w C(float var0) {
      EmotesModule var1 = A;
      if (var1 != null && var1.j != null && var1.z != null && var1.z.prop() != null && !var1.z.handProp() && var1.isRuntimeActive()) {
         LocalPlayer var2 = getMc().player;
         if (var2 != null && var2 == var1.r) {
            var1.j.p(var1.W, var1.N ? 0.0F : var0, var1.Y);
            int var3 = uy.q.u.ordinal() * 10;
            float[] var4 = new float[7];

            for (int var5 = 0; var5 < 7; var5++) {
               float var6 = var1.Y[var3 + (var5 == 6 ? 7 : var5)];
               var4[var5] = Float.isNaN(var6) ? (var5 == 6 ? 1 : 0) : var6;
            }

            float var7 = var1.N ? Math.min(1.0F, (var1.Z + var0) / 4.0F) : 0.0F;
            float var8 = 1.0F - var7 * var7 * (3.0F - 2.0F * var7);
            return new EmotesModule.w(
               var1.z.prop(),
               var2.getPosition(var0),
               var1.i,
               var2.getScale(),
               var4,
               var8,
               var1.W + (var1.N ? 0.0F : var0),
               var1.N ? var1.Z + var0 : 0.0F,
               var1.N
            );
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static boolean A(AvatarRenderState var0) {
      return A != null && A.isEnabled() ? var0 instanceof cu var1 && var1.larp$emoteWeight() > 0.0F : false;
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      } else {
         int var1 = this.q.P();
         if (this.p()) {
            return this.m;
         } else {
            return var1 < 0 ? "Wheel key unbound" : "Hold " + cc.u(var1);
         }
      }
   }

   public record w(String prop, Vec3 feet, float facing, float avatarScale, float[] track, float fade, float age, float stopAge, boolean stopping) {
   }
}

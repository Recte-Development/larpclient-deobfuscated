package com.larpclient.module;

import com.larpclient.e1;
import com.larpclient.fp;
import com.larpclient.hp;
import com.larpclient.jc;
import com.larpclient.pe;
import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.ya;
import com.larpclient.yo;
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
   private static final int R = 3;
   private static final int j = 4;
   private static EmotesModule V;
   public final la W = this.addSetting(new la("Wheel Key", "Hold to open the emote wheel, release over an emote to play it", 66));
   private final lp[] X = new lp[]{
      this.addSetting(new lp("Slot 1", "Top emote on the wheel", "Take the L", ya.P())),
      this.addSetting(new lp("Slot 2", "Right emote on the wheel", "Floss", ya.P())),
      this.addSetting(new lp("Slot 3", "Bottom emote on the wheel", "Maybach Bounce", ya.P())),
      this.addSetting(new lp("Slot 4", "Left emote on the wheel", "Money Spread", ya.P()))
   };
   public final ln n = this.addSetting(new ln("Third Person", "Switch to third person while an emote plays, then switch back", true));
   public final lc C = this.addSetting(
      new lc(
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
            this.C(Double.parseDouble(var1.trim().replace("%", "")) / 100.0);
         }
      }
   );
   private jc p;
   private ya.g u;
   private String Z = "";
   private int D;
   private int L;
   private boolean o;
   private boolean f;
   private boolean P;
   private CameraType H = CameraType.FIRST_PERSON;
   private CameraType m = CameraType.THIRD_PERSON_BACK;
   private final float[] A = new float[jc.t];
   private float U;
   private int a;
   private Object i;
   private Object J;
   private e1 M;
   public static boolean d;
   private static final String[] b;

   public EmotesModule() {
      super("Emotes", "Hold a key to pick an emote from a wheel, like Fortnite", Category.RENDER, -1);
      V = this;
   }

   public static EmotesModule n() {
      return V;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.k);
   }

   public List<String> u() {
      return Arrays.stream(this.X).map(l6::getValue).toList();
   }

   public int B() {
      return this.W.getValue();
   }

   public boolean K() {
      return !this.isEnabled() ? false : this.p != null && !this.o;
   }

   public String F() {
      return this.K() ? this.Z : "";
   }

   @Override
   public void onEnable() {
      this.f = this.V();
      r4.g("Emotes", "Ready; wheel key={}, slots={}", this.W.getValue(), this.u());
   }

   @Override
   public void onDisable() {
      if (getMc().gui.screen() instanceof fp var1) {
         var1.onClose();
      }

      this.l("module disabled");
   }

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      byte var2 = this.V();
      boolean var4 = false;
      byte var3 = var2;
      int var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         boolean var6 = false;
         byte var5 = this.f;
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
                  var1.gui.setScreen(new fp(this));
                  r4.g("Emotes", "Wheel opened");
               }
            }
         }
      }

      this.f = (boolean)var2;
      this.i();
   }

   private boolean V() {
      Minecraft var1 = getMc();
      return var1.getWindow() != null && pe.A(var1.getWindow().handle(), this.W.getValue());
   }

   public void D(int var1) {
      if (this.isEnabled()) {
         if (var1 >= 0 && var1 < this.X.length && this.isRuntimeActive() && this.isInGame()) {
            ya.g var2 = ya.T(this.X[var1].getValue());
            if (var2 != null) {
               jc var3 = ya.M(var2);
               if (var3 != null) {
                  Minecraft var4 = getMc();
                  LocalPlayer var5 = var4.player;
                  this.p = var3;
                  this.u = var2;
                  this.Z = var2.label();
                  this.D = this.L = 0;
                  this.o = (boolean)0;
                  this.i = var5;
                  this.J = var4.level;
                  this.a = var5.hurtTime;
                  this.U = var5.getYRot();
                  this.Q(var2);
                  this.z(var2);
                  String[] var6 = b;
                  String var10000 = var6[3];
                  String var10001 = var6[1];
                  Object[] var10002 = new Object[]{this.Z, (var1 | 1) + (var1 & 1), null, null};
                  Object var10005;
                  if (this.P) {
                     var10005 = this.m;
                  } else {
                     var6 = b;
                     var10005 = var6[0];
                  }

                  var10002[2] = var10005;
                  var10002[3] = var2.prop() == null ? b[2] : var2.prop();
                  r4.V(var10000, var10001, var10002);
               }
            }
         }
      }
   }

   private void z(ya.g var1) {
      Options var2 = getMc().options;
      CameraType var3 = var2.getCameraType();
      CameraType var4 = var1.prop() != null ? CameraType.THIRD_PERSON_FRONT : CameraType.THIRD_PERSON_BACK;
      int var5 = this.P && var3 == this.m ? 1 : 0;
      int var6 = var3.isFirstPerson() || var5 != 0 || var1.prop() != null && var3 == CameraType.THIRD_PERSON_BACK ? 1 : 0;
      if (this.n.getValue() && rr.u() == rr.f.i && var6 != 0 && var3 != var4) {
         if (var5 == 0) {
            this.H = var3;
         }

         var2.setCameraType(var4);
         this.P = (boolean)1;
         this.m = var4;
      }
   }

   public void k(String var1) {
      if (this.p != null && !this.o) {
         this.o = (boolean)1;
         this.L = 0;
         if (this.M != null) {
            this.M.G();
         }

         this.q();
         r4.V("Emotes", "Stopped {} after {} ticks: {}", this.Z, this.D, var1);
      }
   }

   private void l(String var1) {
      if (this.p != null && !this.o) {
         r4.V("Emotes", "Stopped {} after {} ticks: {}", this.Z, this.D, var1);
      }

      this.q();
      this.J();
      this.p = null;
      this.u = null;
      this.Z = "";
      this.o = (boolean)0;
      this.i = this.J = null;
   }

   private void Q(ya.g var1) {
      this.J();
      if (!(this.C.getValue() <= 0.0)) {
         if (getMc().getSoundManager().getSoundEvent(var1.music()) == null) {
            r4.g("Emotes", "No music for {}: add {} with the emote music resource pack", var1.label(), var1.music());
         } else {
            e1 var2 = new e1(var1, this.C::getValue, 4);

            try {
               PlayResult var3 = getMc().getSoundManager().play(var2);
               this.M = var2;
               r4.g("Emotes", "Music {} started at {}% volume: {}", var1.music(), Math.round(this.C.getValue() * 100.0), var3);
            } catch (RuntimeException var4) {
               r4.T("Emotes", "Emote music failed to start; the dance plays silently, reload resources to retry", var4);
            }
         }
      }
   }

   private void J() {
      if (this.M != null) {
         e1 var1 = this.M;
         this.M = null;

         try {
            getMc().getSoundManager().stop(var1);
         } catch (RuntimeException var3) {
            r4.T("Emotes", "Emote music stop failed; it ends with the sound engine", var3);
         }
      }
   }

   private void q() {
      Options var1 = getMc().options;
      if (this.P && var1.getCameraType() == this.m) {
         var1.setCameraType(this.H);
      }

      this.P = (boolean)0;
   }

   private void i() {
      if (this.p != null) {
         Minecraft var1 = getMc();
         LocalPlayer var2 = var1.player;
         if (var2 == null || var2 != this.i || var1.level != this.J || !var2.isAlive()) {
            this.l("world or player changed");
         } else if (this.o) {
            if (++this.L >= 4) {
               this.l("blend finished");
            }
         } else {
            this.D++;
            String var3 = this.T(var2);
            if (var3 != null) {
               this.k(var3);
            } else if (this.p.v(this.D)) {
               this.k("emote finished");
            }
         }
      }
   }

   private String T(LocalPlayer var1) {
      Input var2 = var1.input.keyPresses;
      if (var2.forward() || var2.backward() || var2.left() || var2.right()) {
         return "movement input";
      } else if (var2.jump()) {
         return "jump";
      } else if (var2.shift()) {
         return "sneak";
      } else {
         int var3 = var1.hurtTime;
         int var4 = var3 > this.a ? 1 : 0;
         this.a = var3;
         if (var4 != 0) {
            return "took damage";
         } else if (hp.o(var1) || var1.isUsingItem()) {
            return "attack or item use";
         } else {
            return !var1.isPassenger() && !var1.isFallFlying() && !var1.isSwimming() && !var1.isSleeping() && !var1.getAbilities().flying
               ? null
               : "pose changed";
         }
      }
   }

   public static void w(Avatar var0, AvatarRenderState var1, float var2) {
      if (V != null && V.isEnabled()) {
         yo var3 = (yo)var1;
         EmotesModule var4 = V;
         if (var4 != null && var4.p != null && var4.isRuntimeActive() && var0 == getMc().player) {
            float var5 = var4.p(var2);
            var3.larp$setEmoteWeight(var5);
            if (!(var5 <= 0.0F)) {
               int var6 = var4.D;
               float var7 = var4.o ? 0.0F : var2;
               var4.p.Y(var6, var7, var3.larp$emoteSample());
               var3.larp$setEmoteProp(var4.u.prop(), var4.p.I(var6, var7));
               Screen var8 = getMc().gui.screen();
               if (var8 == null || var8 instanceof ChatScreen || var8 instanceof fp) {
                  var1.bodyRot = var4.U;
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

   private float p(float var1) {
      float var2 = Math.min(1.0F, (this.D + (this.o ? 0.0F : var1)) / 3.0F);
      float var3 = this.o ? 1.0F - Math.min(1.0F, (this.L + var1) / 4.0F) : 1.0F;
      float var4 = Math.min(var2, var3);
      return var4 * var4 * (3.0F - 2.0F * var4);
   }

   public static EmotesModule.s A(float var0) {
      EmotesModule var1 = V;
      if (var1 != null && var1.p != null && var1.u != null && var1.u.prop() != null && !var1.u.handProp() && var1.isRuntimeActive()) {
         LocalPlayer var2 = getMc().player;
         if (var2 != null && var2 == var1.i) {
            var1.p.Y(var1.D, var1.o ? 0.0F : var0, var1.A);
            int var3 = jc.z.Y.ordinal() * 10;
            float[] var4 = new float[7];

            for (int var5 = 0; var5 < 7; var5++) {
               float var6 = var1.A[var3 + (var5 == 6 ? 7 : var5)];
               var4[var5] = Float.isNaN(var6) ? (var5 == 6 ? 1 : 0) : var6;
            }

            float var7 = var1.o ? Math.min(1.0F, (var1.L + var0) / 4.0F) : 0.0F;
            float var8 = 1.0F - var7 * var7 * (3.0F - 2.0F * var7);
            return new EmotesModule.s(
               var1.u.prop(),
               var2.getPosition(var0),
               var1.U,
               var2.getScale(),
               var4,
               var8,
               var1.D + (var1.o ? 0.0F : var0),
               var1.o ? var1.L + var0 : 0.0F,
               var1.o
            );
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static boolean h(AvatarRenderState var0) {
      return V != null && V.isEnabled() ? var0 instanceof yo var1 && var1.larp$emoteWeight() > 0.0F : false;
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      } else {
         int var1 = this.W.getValue();
         if (this.K()) {
            return this.Z;
         } else {
            return var1 < 0 ? "Wheel key unbound" : "Hold " + pe.A(var1);
         }
      }
   }

   public record s(String prop, Vec3 feet, float facing, float avatarScale, float[] track, float fade, float age, float stopAge, boolean stopping) {
   }
}

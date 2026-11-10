package com.larpclient.module;

import com.larpclient.bc;
import com.larpclient.bp;
import com.larpclient.cx;
import com.larpclient.gn;
import com.larpclient.ks;
import com.larpclient.mf;
import com.larpclient.mz;
import com.larpclient.p_;
import com.larpclient.ua;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.sounds.SoundEngine.PlayResult;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec3;

public final class EmotesModule extends Module {
   private static final int O = 3;
   private static final int n = 4;
   private static EmotesModule f;
   public final v c = this.addSetting(new v("Wheel Key", "Hold to open the emote wheel, release over an emote to play it", 66));
   private final l[] D = new l[]{
      this.addSetting(new EmotesModule.j("Slot 1", "Top emote on the wheel", "Maybach Bounce")),
      this.addSetting(new EmotesModule.j("Slot 2", "Right emote on the wheel", "Money Spread")),
      this.addSetting(new EmotesModule.j("Slot 3", "Bottom emote on the wheel", "Crypto Mining")),
      this.addSetting(new EmotesModule.j("Slot 4", "Left emote on the wheel", "Floss"))
   };
   public final s k = this.addSetting(new s("Third Person", "Switch to third person while an emote plays, then switch back", true));
   public final e t = this.addSetting(
      new e(
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
            this.X(Double.parseDouble(var1.trim().replace("%", "")) / 100.0);
         }
      }
   );
   private cx p;
   private mf.h d;
   private String N = "";
   private int Y;
   private int w;
   private boolean A;
   private boolean M;
   private boolean V;
   private CameraType C = CameraType.FIRST_PERSON;
   private CameraType v = CameraType.THIRD_PERSON_BACK;
   private final float[] F = new float[cx.B];
   private float s;
   private int r;
   private Object o;
   private Object T;
   private bp L;
   public static boolean h;
   private static final String[] a;

   public EmotesModule() {
      super("Emotes", "Hold a key to pick an emote from a wheel, like Fortnite", Category.RENDER, -1);
      f = this;
   }

   public static EmotesModule W() {
      return f;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.k);
   }

   public List<String> n() {
      return Arrays.stream(this.D).map(q::getValue).toList();
   }

   public int N() {
      return this.c.getValue();
   }

   public boolean k() {
      return !this.isEnabled() ? false : this.p != null && !this.A;
   }

   public String I() {
      return this.k() ? this.N : "";
   }

   @Override
   public void onEnable() {
      this.M = this.N();
      com.larpclient.h.U("Emotes", "Ready; wheel key={}, slots={}", this.c.getValue(), this.n());
   }

   @Override
   public void onDisable() {
      if (getMc().gui.screen() instanceof mz var1) {
         var1.onClose();
      }

      this.f("module disabled");
   }

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      byte var2 = this.N();
      boolean var4 = false;
      byte var3 = var2;
      int var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         boolean var6 = false;
         byte var5 = this.M;
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
                  var1.gui.setScreen(new mz(this));
                  com.larpclient.h.U("Emotes", "Wheel opened");
               }
            }
         }
      }

      this.M = (boolean)var2;
      this.W();
   }

   private boolean N() {
      Minecraft var1 = getMc();
      return var1.getWindow() != null && bc.I(var1.getWindow().handle(), this.c.getValue());
   }

   public void j(int var1) {
      if (this.isEnabled()) {
         if (var1 >= 0 && var1 < this.D.length && this.isRuntimeActive() && this.isInGame()) {
            mf.h var2 = mf.j(this.D[var1].getValue());
            if (var2 != null) {
               cx var3 = mf.q(var2);
               if (var3 != null) {
                  Minecraft var4 = getMc();
                  LocalPlayer var5 = var4.player;
                  this.p = var3;
                  this.d = var2;
                  this.N = var2.label();
                  this.Y = this.w = 0;
                  this.A = (boolean)0;
                  this.o = var5;
                  this.T = var4.level;
                  this.r = var5.hurtTime;
                  this.s = var5.getYRot();
                  this.D(var2);
                  this.l(var2);
                  String[] var6 = a;
                  String var10000 = var6[1];
                  String var10001 = var6[0];
                  Object[] var10002 = new Object[]{this.N, (var1 | 1) + (var1 & 1), null, null};
                  Object var10005;
                  if (this.V) {
                     var10005 = this.v;
                  } else {
                     var6 = a;
                     var10005 = var6[3];
                  }

                  var10002[2] = var10005;
                  var10002[3] = var2.prop() == null ? a[2] : var2.prop();
                  com.larpclient.h.A(var10000, var10001, var10002);
               }
            }
         }
      }
   }

   private void l(mf.h var1) {
      Options var2 = getMc().options;
      CameraType var3 = var2.getCameraType();
      CameraType var4 = var1.prop() != null ? CameraType.THIRD_PERSON_FRONT : CameraType.THIRD_PERSON_BACK;
      int var5 = this.V && var3 == this.v ? 1 : 0;
      int var6 = var3.isFirstPerson() || var5 != 0 || var1.prop() != null && var3 == CameraType.THIRD_PERSON_BACK ? 1 : 0;
      if (this.k.getValue() && p_.A() == p_.o.T && var6 != 0 && var3 != var4) {
         if (var5 == 0) {
            this.C = var3;
         }

         var2.setCameraType(var4);
         this.V = (boolean)1;
         this.v = var4;
      }
   }

   public void J(String var1) {
      if (this.p != null && !this.A) {
         this.A = (boolean)1;
         this.w = 0;
         if (this.L != null) {
            this.L.p();
         }

         this.Z();
         com.larpclient.h.A("Emotes", "Stopped {} after {} ticks: {}", this.N, this.Y, var1);
      }
   }

   private void f(String var1) {
      if (this.p != null && !this.A) {
         com.larpclient.h.A("Emotes", "Stopped {} after {} ticks: {}", this.N, this.Y, var1);
      }

      this.Z();
      this.u();
      this.p = null;
      this.d = null;
      this.N = "";
      this.A = (boolean)0;
      this.o = this.T = null;
   }

   private void D(mf.h var1) {
      this.u();
      if (!(this.t.getValue() <= 0.0)) {
         if (getMc().getSoundManager().getSoundEvent(var1.music()) == null) {
            com.larpclient.h.U("Emotes", "No music for {}: add {} with the emote music resource pack", var1.label(), var1.music());
         } else {
            bp var2 = new bp(var1, this.t::getValue, 4);

            try {
               PlayResult var3 = getMc().getSoundManager().play(var2);
               this.L = var2;
               com.larpclient.h.U("Emotes", "Music {} started at {}% volume: {}", var1.music(), Math.round(this.t.getValue() * 100.0), var3);
            } catch (RuntimeException var4) {
               com.larpclient.h.w("Emotes", "Emote music failed to start; the dance plays silently, reload resources to retry", var4);
            }
         }
      }
   }

   private void u() {
      if (this.L != null) {
         bp var1 = this.L;
         this.L = null;

         try {
            getMc().getSoundManager().stop(var1);
         } catch (RuntimeException var3) {
            com.larpclient.h.w("Emotes", "Emote music stop failed; it ends with the sound engine", var3);
         }
      }
   }

   private void Z() {
      Options var1 = getMc().options;
      if (this.V && var1.getCameraType() == this.v) {
         var1.setCameraType(this.C);
      }

      this.V = (boolean)0;
   }

   private void W() {
      if (this.p != null) {
         Minecraft var1 = getMc();
         LocalPlayer var2 = var1.player;
         if (var2 == null || var2 != this.o || var1.level != this.T || !var2.isAlive()) {
            this.f("world or player changed");
         } else if (this.A) {
            if (++this.w >= 4) {
               this.f("blend finished");
            }
         } else {
            this.Y++;
            String var3 = this.l(var2);
            if (var3 != null) {
               this.J(var3);
            } else if (this.p.b(this.Y)) {
               this.J("emote finished");
            }
         }
      }
   }

   private String l(LocalPlayer var1) {
      Input var2 = var1.input.keyPresses;
      if (var2.forward() || var2.backward() || var2.left() || var2.right()) {
         return "movement input";
      } else if (var2.jump()) {
         return "jump";
      } else if (var2.shift()) {
         return "sneak";
      } else {
         int var3 = var1.hurtTime;
         int var4 = var3 > this.r ? 1 : 0;
         this.r = var3;
         if (var4 != 0) {
            return "took damage";
         } else if (ks.M(var1) || var1.isUsingItem()) {
            return "attack or item use";
         } else {
            return !var1.isPassenger() && !var1.isFallFlying() && !var1.isSwimming() && !var1.isSleeping() && !var1.getAbilities().flying
               ? null
               : "pose changed";
         }
      }
   }

   public static void u(Avatar var0, AvatarRenderState var1, float var2) {
      if (f != null && f.isEnabled()) {
         gn var3 = (gn)var1;
         EmotesModule var4 = f;
         if (var4 != null && var4.p != null && var4.isRuntimeActive() && var0 == getMc().player) {
            float var5 = var4.j(var2);
            var3.larp$setEmoteWeight(var5);
            if (!(var5 <= 0.0F)) {
               int var6 = var4.Y;
               float var7 = var4.A ? 0.0F : var2;
               var4.p.A(var6, var7, var3.larp$emoteSample());
               var3.larp$setEmoteProp(var4.d.prop(), var4.p.L(var6, var7));
               Screen var8 = getMc().gui.screen();
               if (var8 == null || var8 instanceof ChatScreen || var8 instanceof mz) {
                  var1.bodyRot = var4.s;
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

   private float j(float var1) {
      float var2 = Math.min(1.0F, (this.Y + (this.A ? 0.0F : var1)) / 3.0F);
      float var3 = this.A ? 1.0F - Math.min(1.0F, (this.w + var1) / 4.0F) : 1.0F;
      float var4 = Math.min(var2, var3);
      return var4 * var4 * (3.0F - 2.0F * var4);
   }

   public static EmotesModule.t Y(float var0) {
      EmotesModule var1 = f;
      if (var1 != null && var1.p != null && var1.d != null && var1.d.prop() != null && !var1.d.handProp() && var1.isRuntimeActive()) {
         LocalPlayer var2 = getMc().player;
         if (var2 != null && var2 == var1.o) {
            var1.p.A(var1.Y, var1.A ? 0.0F : var0, var1.F);
            int var3 = cx.q.M.ordinal() * 10;
            float[] var4 = new float[7];

            for (int var5 = 0; var5 < 7; var5++) {
               float var6 = var1.F[var3 + (var5 == 6 ? 7 : var5)];
               var4[var5] = Float.isNaN(var6) ? (var5 == 6 ? 1 : 0) : var6;
            }

            float var7 = var1.A ? Math.min(1.0F, (var1.w + var0) / 4.0F) : 0.0F;
            float var8 = 1.0F - var7 * var7 * (3.0F - 2.0F * var7);
            return new EmotesModule.t(
               var1.d.prop(),
               var2.getPosition(var0),
               var1.s,
               var2.getScale(),
               var4,
               var8,
               var1.Y + (var1.A ? 0.0F : var0),
               var1.A ? var1.w + var0 : 0.0F,
               var1.A
            );
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static boolean J(AvatarRenderState var0) {
      return f != null && f.isEnabled() ? var0 instanceof gn var1 && var1.larp$emoteWeight() > 0.0F : false;
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      } else {
         int var1 = this.c.getValue();
         if (this.k()) {
            return this.N;
         } else {
            return var1 < 0 ? "Wheel key unbound" : "Hold " + bc.G(var1);
         }
      }
   }

   private static final class j extends l {
      j(String var1, String var2, String var3) {
         super(var1, var2, var3, mf.j());
      }

      @Override
      public int previewSize() {
         return 44;
      }

      @Override
      public void drawPreview(GuiGraphicsExtractor var1, String var2, int var3, int var4, int var5) {
         mf.h var6 = mf.j(var2);
         if (var6 != null) {
            short var7 = 192;
            var1.blit(RenderPipelines.GUI_TEXTURED, var6.icon(), var3, var4, 0.0F, 0.0F, var5, var5, var7, var7, var7, var7, ua.I(-1));
         }
      }
   }

   public record t(String prop, Vec3 feet, float facing, float avatarScale, float[] track, float fade, float age, float stopAge, boolean stopping) {
   }
}

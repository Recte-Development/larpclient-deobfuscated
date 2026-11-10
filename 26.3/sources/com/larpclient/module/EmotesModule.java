package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.bo;
import com.larpclient.h8;
import com.larpclient.hb;
import com.larpclient.ij;
import com.larpclient.il;
import com.larpclient.jh;
import com.larpclient.oa;
import com.larpclient.p4;
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
   private static final int z = 3;
   private static final int b = 4;
   private static EmotesModule w;
   public final x X = this.addSetting(new x("Wheel Key", "Hold to open the emote wheel, release over an emote to play it", 66));
   private final com.larpclient.module.o[] q = new com.larpclient.module.o[]{
      this.addSetting(new EmotesModule.a("Slot 1", "Top emote on the wheel", "Maybach Bounce")),
      this.addSetting(new EmotesModule.a("Slot 2", "Right emote on the wheel", "Money Spread")),
      this.addSetting(new EmotesModule.a("Slot 3", "Bottom emote on the wheel", "Crypto Mining")),
      this.addSetting(new EmotesModule.a("Slot 4", "Left emote on the wheel", "Floss"))
   };
   public final m i = this.addSetting(new m("Third Person", "Switch to third person while an emote plays, then switch back", true));
   public final b g = this.addSetting(
      new b(
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
            this.O(Double.parseDouble(var1.trim().replace("%", "")) / 100.0);
         }
      }
   );
   private bo A;
   private il.w B;
   private String k = "";
   private int C;
   private int L;
   private boolean d;
   private boolean V;
   private boolean p;
   private CameraType S = CameraType.FIRST_PERSON;
   private CameraType y = CameraType.THIRD_PERSON_BACK;
   private final float[] R = new float[bo.s];
   private float N;
   private int f;
   private Object H;
   private Object s;
   private hb Y;
   public static int l;
   private static final String[] a;

   public EmotesModule() {
      super("Emotes", "Hold a key to pick an emote from a wheel, like Fortnite", Category.RENDER, -1);
      w = this;
   }

   public static EmotesModule b() {
      return w;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.E);
   }

   public List<String> f() {
      return Arrays.stream(this.q).map(r::getValue).toList();
   }

   public int S() {
      return this.X.getValue();
   }

   public boolean O() {
      return !this.isEnabled() ? false : this.A != null && !this.d;
   }

   public String J() {
      return this.O() ? this.k : "";
   }

   @Override
   public void onEnable() {
      this.V = this.p();
      _q.c("Emotes", "Ready; wheel key={}, slots={}", this.X.getValue(), this.f());
   }

   @Override
   public void onDisable() {
      if (getMc().gui.screen() instanceof jh var1) {
         var1.onClose();
      }

      this.k("module disabled");
   }

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      byte var2 = this.p();
      boolean var4 = false;
      byte var3 = var2;
      int var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         boolean var6 = false;
         byte var5 = this.V;
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
                  var1.gui.setScreen(new jh(this));
                  _q.c("Emotes", "Wheel opened");
               }
            }
         }
      }

      this.V = (boolean)var2;
      this.N();
   }

   private boolean p() {
      Minecraft var1 = getMc();
      return var1.getWindow() != null && p4.Y(var1.getWindow().handle(), this.X.getValue());
   }

   public void W(int var1) {
      if (this.isEnabled()) {
         if (var1 >= 0 && var1 < this.q.length && this.isRuntimeActive() && this.isInGame()) {
            il.w var2 = il.c(this.q[var1].getValue());
            if (var2 != null) {
               bo var3 = il.o(var2);
               if (var3 != null) {
                  Minecraft var4 = getMc();
                  LocalPlayer var5 = var4.player;
                  this.A = var3;
                  this.B = var2;
                  this.k = var2.label();
                  this.C = this.L = 0;
                  this.d = (boolean)0;
                  this.H = var5;
                  this.s = var4.level;
                  this.f = var5.hurtTime;
                  this.N = var5.getYRot();
                  this.F(var2);
                  this.l(var2);
                  String[] var6 = a;
                  String var10000 = var6[1];
                  String var10001 = var6[0];
                  Object[] var10002 = new Object[]{this.k, (var1 | 1) + (var1 & 1), null, null};
                  Object var10005;
                  if (this.p) {
                     var10005 = this.y;
                  } else {
                     var6 = a;
                     var10005 = var6[3];
                  }

                  var10002[2] = var10005;
                  var10002[3] = var2.prop() == null ? a[2] : var2.prop();
                  _q.z(var10000, var10001, var10002);
               }
            }
         }
      }
   }

   private void l(il.w var1) {
      Options var2 = getMc().options;
      CameraType var3 = var2.getCameraType();
      CameraType var4 = var1.prop() != null ? CameraType.THIRD_PERSON_FRONT : CameraType.THIRD_PERSON_BACK;
      int var5 = this.p && var3 == this.y ? 1 : 0;
      int var6 = var3.isFirstPerson() || var5 != 0 || var1.prop() != null && var3 == CameraType.THIRD_PERSON_BACK ? 1 : 0;
      if (this.i.getValue() && ij.L() == ij.r.X && var6 != 0 && var3 != var4) {
         if (var5 == 0) {
            this.S = var3;
         }

         var2.setCameraType(var4);
         this.p = (boolean)1;
         this.y = var4;
      }
   }

   public void x(String var1) {
      if (this.A != null && !this.d) {
         this.d = (boolean)1;
         this.L = 0;
         if (this.Y != null) {
            this.Y.v();
         }

         this.u();
         _q.z("Emotes", "Stopped {} after {} ticks: {}", this.k, this.C, var1);
      }
   }

   private void k(String var1) {
      if (this.A != null && !this.d) {
         _q.z("Emotes", "Stopped {} after {} ticks: {}", this.k, this.C, var1);
      }

      this.u();
      this.i();
      this.A = null;
      this.B = null;
      this.k = "";
      this.d = (boolean)0;
      this.H = this.s = null;
   }

   private void F(il.w var1) {
      this.i();
      if (!(this.g.getValue() <= 0.0)) {
         if (getMc().getSoundManager().getSoundEvent(var1.music()) == null) {
            _q.c("Emotes", "No music for {}: add {} with the emote music resource pack", var1.label(), var1.music());
         } else {
            hb var2 = new hb(var1, this.g::getValue, 4);

            try {
               PlayResult var3 = getMc().getSoundManager().play(var2);
               this.Y = var2;
               _q.c("Emotes", "Music {} started at {}% volume: {}", var1.music(), Math.round(this.g.getValue() * 100.0), var3);
            } catch (RuntimeException var4) {
               _q.g("Emotes", "Emote music failed to start; the dance plays silently, reload resources to retry", var4);
            }
         }
      }
   }

   private void i() {
      if (this.Y != null) {
         hb var1 = this.Y;
         this.Y = null;

         try {
            getMc().getSoundManager().stop(var1);
         } catch (RuntimeException var3) {
            _q.g("Emotes", "Emote music stop failed; it ends with the sound engine", var3);
         }
      }
   }

   private void u() {
      Options var1 = getMc().options;
      if (this.p && var1.getCameraType() == this.y) {
         var1.setCameraType(this.S);
      }

      this.p = (boolean)0;
   }

   private void N() {
      if (this.A != null) {
         Minecraft var1 = getMc();
         LocalPlayer var2 = var1.player;
         if (var2 == null || var2 != this.H || var1.level != this.s || !var2.isAlive()) {
            this.k("world or player changed");
         } else if (this.d) {
            if (++this.L >= 4) {
               this.k("blend finished");
            }
         } else {
            this.C++;
            String var3 = this.J(var2);
            if (var3 != null) {
               this.x(var3);
            } else if (this.A.X(this.C)) {
               this.x("emote finished");
            }
         }
      }
   }

   private String J(LocalPlayer var1) {
      Input var2 = var1.input.keyPresses;
      if (var2.forward() || var2.backward() || var2.left() || var2.right()) {
         return "movement input";
      } else if (var2.jump()) {
         return "jump";
      } else if (var2.shift()) {
         return "sneak";
      } else {
         int var3 = var1.hurtTime;
         int var4 = var3 > this.f ? 1 : 0;
         this.f = var3;
         if (var4 != 0) {
            return "took damage";
         } else if (h8.S(var1) || var1.isUsingItem()) {
            return "attack or item use";
         } else {
            return !var1.isPassenger() && !var1.isFallFlying() && !var1.isSwimming() && !var1.isSleeping() && !var1.getAbilities().flying
               ? null
               : "pose changed";
         }
      }
   }

   public static void R(Avatar var0, AvatarRenderState var1, float var2) {
      if (w != null && w.isEnabled()) {
         com.larpclient.f0 var3 = (com.larpclient.f0)var1;
         EmotesModule var4 = w;
         if (var4 != null && var4.A != null && var4.isRuntimeActive() && var0 == getMc().player) {
            float var5 = var4.Q(var2);
            var3.larp$setEmoteWeight(var5);
            if (!(var5 <= 0.0F)) {
               int var6 = var4.C;
               float var7 = var4.d ? 0.0F : var2;
               var4.A.X(var6, var7, var3.larp$emoteSample());
               var3.larp$setEmoteProp(var4.B.prop(), var4.A.z(var6, var7));
               Screen var8 = getMc().gui.screen();
               if (var8 == null || var8 instanceof ChatScreen || var8 instanceof jh) {
                  var1.bodyRot = var4.N;
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

   private float Q(float var1) {
      float var2 = Math.min(1.0F, (this.C + (this.d ? 0.0F : var1)) / 3.0F);
      float var3 = this.d ? 1.0F - Math.min(1.0F, (this.L + var1) / 4.0F) : 1.0F;
      float var4 = Math.min(var2, var3);
      return var4 * var4 * (3.0F - 2.0F * var4);
   }

   public static EmotesModule.o X(float var0) {
      EmotesModule var1 = w;
      if (var1 != null && var1.A != null && var1.B != null && var1.B.prop() != null && !var1.B.handProp() && var1.isRuntimeActive()) {
         LocalPlayer var2 = getMc().player;
         if (var2 != null && var2 == var1.H) {
            var1.A.X(var1.C, var1.d ? 0.0F : var0, var1.R);
            int var3 = bo.i.p.ordinal() * 10;
            float[] var4 = new float[7];

            for (int var5 = 0; var5 < 7; var5++) {
               float var6 = var1.R[var3 + (var5 == 6 ? 7 : var5)];
               var4[var5] = Float.isNaN(var6) ? (var5 == 6 ? 1 : 0) : var6;
            }

            float var7 = var1.d ? Math.min(1.0F, (var1.L + var0) / 4.0F) : 0.0F;
            float var8 = 1.0F - var7 * var7 * (3.0F - 2.0F * var7);
            return new EmotesModule.o(
               var1.B.prop(),
               var2.getPosition(var0),
               var1.N,
               var2.getScale(),
               var4,
               var8,
               var1.C + (var1.d ? 0.0F : var0),
               var1.d ? var1.L + var0 : 0.0F,
               var1.d
            );
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static boolean S(AvatarRenderState var0) {
      return w != null && w.isEnabled() ? var0 instanceof com.larpclient.f0 var1 && var1.larp$emoteWeight() > 0.0F : false;
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      } else {
         int var1 = this.X.getValue();
         if (this.O()) {
            return this.k;
         } else {
            return var1 < 0 ? "Wheel key unbound" : "Hold " + p4.n(var1);
         }
      }
   }

   private static final class a extends com.larpclient.module.o {
      a(String var1, String var2, String var3) {
         super(var1, var2, var3, il.d());
      }

      @Override
      public int previewSize() {
         return 44;
      }

      @Override
      public void drawPreview(GuiGraphicsExtractor var1, String var2, int var3, int var4, int var5) {
         il.w var6 = il.c(var2);
         if (var6 != null) {
            short var7 = 192;
            var1.blit(RenderPipelines.GUI_TEXTURED, var6.icon(), var3, var4, 0.0F, 0.0F, var5, var5, var7, var7, var7, var7, oa.e(-1));
         }
      }
   }

   public record o(String prop, Vec3 feet, float facing, float avatarScale, float[] track, float fade, float age, float stopAge, boolean stopping) {
   }
}

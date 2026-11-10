package com.larpclient.module;

import com.larpclient.l8;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;

public final class HeadlessKorbloxModule extends Module {
   public static final int h = -14935272;
   public static final int E = -10178636;
   private static HeadlessKorbloxModule T;
   public final s v = this.addSetting(new s("Self", "Wear it on your own player", true));
   public final s Z = this.addSetting(new s("Other Players", "Local preview on nearby players; only you see it", false));
   public final s j = this.addSetting(new s("Headless", "Hide the head and hat layer like Roblox's Headless Horseman", true));
   public final s J = this.addSetting(
      new s("Hide Helmets", "Also hide helmets, carved pumpkins and skulls so no head shows; off lets them float like Roblox hats", true)
   );
   public final s K = this.addSetting(new s("Korblox", "Swap a leg for the Korblox Deathspeaker bone spike", true));
   public final l Y = this.addSetting(new l("Leg", "Which leg becomes the bone spike; Roblox's is the right", "Right", "Right", "Left", "Both"));
   public final e L = this.addSetting(
      new e("Length", "Spike length; 1 matches the Roblox leg, the longest reaches the foot", 1.0, 0.6, Math.floor(com.larpclient.u.X() * 20.0) / 20.0, 0.05)
   );
   public final e r = this.addSetting(new e("Thickness", "Spike and hip knob width", 1.0, 0.6, 1.5, 0.05));
   public final i a = this.addSetting(new i("Hip Color", "Color at the hip knob", -14935272));
   public final i X = this.addSetting(new i("Tip Color", "Color the bone fades to at its point", -10178636));
   public final e l = this.addSetting(new e("Tip Glow", "How brightly the point glows in the dark", 0.3, 0.0, 1.0, 0.05));
   private l8 w;

   public HeadlessKorbloxModule() {
      super("Headless Korblox", "Go headless and swap a leg for the Korblox bone spike, like the Roblox combo", Category.VISUALS, -1);
      T = this;
      this.J.P(this.j::getValue);

      for (q var4 : new q[]{this.Y, this.L, this.r, this.a, this.X, this.l}) {
         var4.P(this.K::getValue);
      }

      this.j.o(var0 -> com.larpclient.h.A("Headless", "Headless={}", var0));
      this.J.o(var0 -> com.larpclient.h.A("Headless", "Hide Helmets={}", var0));
      this.K.o(var0 -> com.larpclient.h.A("Korblox", "Korblox={}", var0));
   }

   public static HeadlessKorbloxModule I() {
      return T;
   }

   @Override
   public void onEnable() {
      com.larpclient.h.A(
         "Headless",
         "Enabled; headless={}, hideHelmets={}, korblox={}, leg={}, self={}, others={}",
         this.j.getValue(),
         this.J.getValue(),
         this.K.getValue(),
         this.Y.getValue(),
         this.v.getValue(),
         this.Z.getValue()
      );
   }

   public int f(Avatar var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.j.getValue() && this.K(var1)) {
            return this.J.getValue() ? 2 : 1;
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }

   public l8 R(Avatar var1) {
      if (this.isEnabled()) {
         return this.isRuntimeActive() && this.K.getValue() && this.K(var1) ? this.b() : null;
      } else {
         return null;
      }
   }

   public boolean F() {
      return !this.isEnabled() ? false : this.isOperational() && this.v.getValue() && this.j.getValue();
   }

   public l8 O() {
      if (this.isEnabled()) {
         return this.isOperational() && this.v.getValue() && this.K.getValue() ? this.b() : null;
      } else {
         return null;
      }
   }

   private boolean K(Avatar var1) {
      Minecraft var2 = getMc();
      return var2 != null && var1 == var2.player ? this.v.getValue() : this.Z.getValue() && var1 instanceof Player var3 && !var3.isSpectator();
   }

   private l8 b() {
      int var1 = this.Y.O("Both") ? 3 : (this.Y.O("Left") ? 2 : 1);
      int var2 = this.a.getValue();
      int var3 = this.X.getValue();
      float var4 = this.l.getValue().floatValue();
      float var5 = this.L.getValue().floatValue();
      float var6 = this.r.getValue().floatValue();
      l8 var7 = this.w;
      if (var7 == null
         || var7.legs() != var1
         || var7.hip() != var2
         || var7.tip() != var3
         || var7.glow() != var4
         || var7.length() != var5
         || var7.thickness() != var6) {
         this.w = var7 = new l8(var1, var2, var3, var4, var5, var6);
      }

      return var7;
   }
}

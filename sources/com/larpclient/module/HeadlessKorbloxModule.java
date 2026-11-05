package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.u3;
import com.larpclient.xu;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;

public final class HeadlessKorbloxModule extends Module {
   public static final int Q = -14935272;
   public static final int T = -10178636;
   private static HeadlessKorbloxModule t;
   public final dh c = this.addSetting(new dh("Self", "Wear it on your own player", true));
   public final dh d = this.addSetting(new dh("Other Players", "Local preview on nearby players; only you see it", false));
   public final dh Y = this.addSetting(new dh("Headless", "Hide the head and hat layer like Roblox's Headless Horseman", true));
   public final dh C = this.addSetting(
      new dh("Hide Helmets", "Also hide helmets, carved pumpkins and skulls so no head shows; off lets them float like Roblox hats", true)
   );
   public final dh H = this.addSetting(new dh("Korblox", "Swap a leg for the Korblox Deathspeaker bone spike", true));
   public final d6 S = this.addSetting(new d6("Leg", "Which leg becomes the bone spike; Roblox's is the right", "Right", "Right", "Left", "Both"));
   public final d9 X = this.addSetting(
      new d9("Length", "Spike length; 1 matches the Roblox leg, the longest reaches the foot", 1.0, 0.6, Math.floor(xu.H() * 20.0) / 20.0, 0.05)
   );
   public final d9 F = this.addSetting(new d9("Thickness", "Spike and hip knob width", 1.0, 0.6, 1.5, 0.05));
   public final dk J = this.addSetting(new dk("Hip Color", "Color at the hip knob", -14935272));
   public final dk z = this.addSetting(new dk("Tip Color", "Color the bone fades to at its point", -10178636));
   public final d9 o = this.addSetting(new d9("Tip Glow", "How brightly the point glows in the dark", 0.3, 0.0, 1.0, 0.05));
   private u3 h;

   public HeadlessKorbloxModule() {
      super("Headless Korblox", "Go headless and swap a leg for the Korblox bone spike, like the Roblox combo", Category.VISUALS, -1);
      t = this;
      this.C.H(this.Y::P);

      for (df var4 : new df[]{this.S, this.X, this.F, this.J, this.z, this.o}) {
         var4.H(this.H::P);
      }

      this.Y.h(var0 -> mt.K("Headless", "Headless={}", var0));
      this.C.h(var0 -> mt.K("Headless", "Hide Helmets={}", var0));
      this.H.h(var0 -> mt.K("Korblox", "Korblox={}", var0));
   }

   public static HeadlessKorbloxModule k() {
      return t;
   }

   @Override
   public void onEnable() {
      mt.K(
         "Headless",
         "Enabled; headless={}, hideHelmets={}, korblox={}, leg={}, self={}, others={}",
         this.Y.P(),
         this.C.P(),
         this.H.P(),
         this.S.P(),
         this.c.P(),
         this.d.P()
      );
   }

   public int U(Avatar var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.Y.P() && this.u(var1)) {
            return this.C.P() ? 2 : 1;
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }

   public u3 e(Avatar var1) {
      if (this.isEnabled()) {
         return this.isRuntimeActive() && this.H.P() && this.u(var1) ? this.C() : null;
      } else {
         return null;
      }
   }

   public boolean c() {
      return !this.isEnabled() ? false : this.isOperational() && this.c.P() && this.Y.P();
   }

   public u3 o() {
      if (this.isEnabled()) {
         return this.isOperational() && this.c.P() && this.H.P() ? this.C() : null;
      } else {
         return null;
      }
   }

   private boolean u(Avatar var1) {
      Minecraft var2 = getMc();
      return var2 != null && var1 == var2.player ? this.c.P() : this.d.P() && var1 instanceof Player var3 && !var3.isSpectator();
   }

   private u3 C() {
      int var1 = this.S.S("Both") ? 3 : (this.S.S("Left") ? 2 : 1);
      int var2 = this.J.P();
      int var3 = this.z.P();
      float var4 = this.o.P().floatValue();
      float var5 = this.X.P().floatValue();
      float var6 = this.F.P().floatValue();
      u3 var7 = this.h;
      if (var7 == null
         || var7.legs() != var1
         || var7.hip() != var2
         || var7.tip() != var3
         || var7.glow() != var4
         || var7.length() != var5
         || var7.thickness() != var6) {
         this.h = var7 = new u3(var1, var2, var3, var4, var5, var6);
      }

      return var7;
   }
}

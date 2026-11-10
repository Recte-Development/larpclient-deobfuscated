package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.a1;
import com.larpclient.w5;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;

public final class HeadlessKorbloxModule extends Module {
   public static final int z = -14935272;
   public static final int X = -10178636;
   private static HeadlessKorbloxModule A;
   public final m w = this.addSetting(new m("Self", "Wear it on your own player", true));
   public final m i = this.addSetting(new m("Other Players", "Local preview on nearby players; only you see it", false));
   public final m J = this.addSetting(new m("Headless", "Hide the head and hat layer like Roblox's Headless Horseman", true));
   public final m S = this.addSetting(
      new m("Hide Helmets", "Also hide helmets, carved pumpkins and skulls so no head shows; off lets them float like Roblox hats", true)
   );
   public final m y = this.addSetting(new m("Korblox", "Swap a leg for the Korblox Deathspeaker bone spike", true));
   public final o Y = this.addSetting(new o("Leg", "Which leg becomes the bone spike; Roblox's is the right", "Right", "Right", "Left", "Both"));
   public final b o = this.addSetting(
      new b("Length", "Spike length; 1 matches the Roblox leg, the longest reaches the foot", 1.0, 0.6, Math.floor(w5.O() * 20.0) / 20.0, 0.05)
   );
   public final b v = this.addSetting(new b("Thickness", "Spike and hip knob width", 1.0, 0.6, 1.5, 0.05));
   public final h H = this.addSetting(new h("Hip Color", "Color at the hip knob", -14935272));
   public final h u = this.addSetting(new h("Tip Color", "Color the bone fades to at its point", -10178636));
   public final b P = this.addSetting(new b("Tip Glow", "How brightly the point glows in the dark", 0.3, 0.0, 1.0, 0.05));
   private a1 a;

   public HeadlessKorbloxModule() {
      super("Headless Korblox", "Go headless and swap a leg for the Korblox bone spike, like the Roblox combo", Category.VISUALS, -1);
      A = this;
      this.S.a(this.J::getValue);

      for (r var4 : new r[]{this.Y, this.o, this.v, this.H, this.u, this.P}) {
         var4.a(this.y::getValue);
      }

      this.J.w(var0 -> _q.z("Headless", "Headless={}", var0));
      this.S.w(var0 -> _q.z("Headless", "Hide Helmets={}", var0));
      this.y.w(var0 -> _q.z("Korblox", "Korblox={}", var0));
   }

   public static HeadlessKorbloxModule W() {
      return A;
   }

   @Override
   public void onEnable() {
      _q.z(
         "Headless",
         "Enabled; headless={}, hideHelmets={}, korblox={}, leg={}, self={}, others={}",
         this.J.getValue(),
         this.S.getValue(),
         this.y.getValue(),
         this.Y.getValue(),
         this.w.getValue(),
         this.i.getValue()
      );
   }

   public int q(Avatar var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.J.getValue() && this.A(var1)) {
            return this.S.getValue() ? 2 : 1;
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }

   public a1 X(Avatar var1) {
      if (this.isEnabled()) {
         return this.isRuntimeActive() && this.y.getValue() && this.A(var1) ? this.S() : null;
      } else {
         return null;
      }
   }

   public boolean i() {
      return !this.isEnabled() ? false : this.isOperational() && this.w.getValue() && this.J.getValue();
   }

   public a1 l() {
      if (this.isEnabled()) {
         return this.isOperational() && this.w.getValue() && this.y.getValue() ? this.S() : null;
      } else {
         return null;
      }
   }

   private boolean A(Avatar var1) {
      Minecraft var2 = getMc();
      return var2 != null && var1 == var2.player ? this.w.getValue() : this.i.getValue() && var1 instanceof Player var3 && !var3.isSpectator();
   }

   private a1 S() {
      int var1 = this.Y.w("Both") ? 3 : (this.Y.w("Left") ? 2 : 1);
      int var2 = this.H.getValue();
      int var3 = this.u.getValue();
      float var4 = this.P.getValue().floatValue();
      float var5 = this.o.getValue().floatValue();
      float var6 = this.v.getValue().floatValue();
      a1 var7 = this.a;
      if (var7 == null
         || var7.legs() != var1
         || var7.hip() != var2
         || var7.tip() != var3
         || var7.glow() != var4
         || var7.length() != var5
         || var7.thickness() != var6) {
         this.a = var7 = new a1(var1, var2, var3, var4, var5, var6);
      }

      return var7;
   }
}

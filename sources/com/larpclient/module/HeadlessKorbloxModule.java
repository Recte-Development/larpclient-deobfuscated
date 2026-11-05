package com.larpclient.module;

import com.larpclient.du;
import com.larpclient.fd;
import com.larpclient.r4;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;

public final class HeadlessKorbloxModule extends Module {
   public static final int V = -14935272;
   public static final int S = -10178636;
   private static HeadlessKorbloxModule d;
   public final ln n = this.addSetting(new ln("Self", "Wear it on your own player", true));
   public final ln k = this.addSetting(new ln("Other Players", "Local preview on nearby players; only you see it", false));
   public final ln M = this.addSetting(new ln("Headless", "Hide the head and hat layer like Roblox's Headless Horseman", true));
   public final ln m = this.addSetting(
      new ln("Hide Helmets", "Also hide helmets, carved pumpkins and skulls so no head shows; off lets them float like Roblox hats", true)
   );
   public final ln p = this.addSetting(new ln("Korblox", "Swap a leg for the Korblox Deathspeaker bone spike", true));
   public final lp T = this.addSetting(new lp("Leg", "Which leg becomes the bone spike; Roblox's is the right", "Right", "Right", "Left", "Both"));
   public final lc l = this.addSetting(
      new lc("Length", "Spike length; 1 matches the Roblox leg, the longest reaches the foot", 1.0, 0.6, Math.floor(du.n() * 20.0) / 20.0, 0.05)
   );
   public final lc o = this.addSetting(new lc("Thickness", "Spike and hip knob width", 1.0, 0.6, 1.5, 0.05));
   public final lv Z = this.addSetting(new lv("Hip Color", "Color at the hip knob", -14935272));
   public final lv X = this.addSetting(new lv("Tip Color", "Color the bone fades to at its point", -10178636));
   public final lc G = this.addSetting(new lc("Tip Glow", "How brightly the point glows in the dark", 0.3, 0.0, 1.0, 0.05));
   private fd E;

   public HeadlessKorbloxModule() {
      super("Headless Korblox", "Go headless and swap a leg for the Korblox bone spike, like the Roblox combo", Category.VISUALS, -1);
      d = this;
      this.m.x(this.M::getValue);

      for (l6 var4 : new l6[]{this.T, this.l, this.o, this.Z, this.X, this.G}) {
         var4.x(this.p::getValue);
      }

      this.M.W(var0 -> r4.V("Headless", "Headless={}", var0));
      this.m.W(var0 -> r4.V("Headless", "Hide Helmets={}", var0));
      this.p.W(var0 -> r4.V("Korblox", "Korblox={}", var0));
   }

   public static HeadlessKorbloxModule U() {
      return d;
   }

   @Override
   public void onEnable() {
      r4.V(
         "Headless",
         "Enabled; headless={}, hideHelmets={}, korblox={}, leg={}, self={}, others={}",
         this.M.getValue(),
         this.m.getValue(),
         this.p.getValue(),
         this.T.getValue(),
         this.n.getValue(),
         this.k.getValue()
      );
   }

   public int u(Avatar var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.M.getValue() && this.Y(var1)) {
            return this.m.getValue() ? 2 : 1;
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }

   public fd g(Avatar var1) {
      if (this.isEnabled()) {
         return this.isRuntimeActive() && this.p.getValue() && this.Y(var1) ? this.I() : null;
      } else {
         return null;
      }
   }

   public boolean g() {
      return !this.isEnabled() ? false : this.isOperational() && this.n.getValue() && this.M.getValue();
   }

   public fd n() {
      if (this.isEnabled()) {
         return this.isOperational() && this.n.getValue() && this.p.getValue() ? this.I() : null;
      } else {
         return null;
      }
   }

   private boolean Y(Avatar var1) {
      Minecraft var2 = getMc();
      return var2 != null && var1 == var2.player ? this.n.getValue() : this.k.getValue() && var1 instanceof Player var3 && !var3.isSpectator();
   }

   private fd I() {
      int var1 = this.T.i("Both") ? 3 : (this.T.i("Left") ? 2 : 1);
      int var2 = this.Z.getValue();
      int var3 = this.X.getValue();
      float var4 = this.G.getValue().floatValue();
      float var5 = this.l.getValue().floatValue();
      float var6 = this.o.getValue().floatValue();
      fd var7 = this.E;
      if (var7 == null
         || var7.legs() != var1
         || var7.hip() != var2
         || var7.tip() != var3
         || var7.glow() != var4
         || var7.length() != var5
         || var7.thickness() != var6) {
         this.E = var7 = new fd(var1, var2, var3, var4, var5, var6);
      }

      return var7;
   }
}

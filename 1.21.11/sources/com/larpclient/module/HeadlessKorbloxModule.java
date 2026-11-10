package com.larpclient.module;

import com.larpclient.q8;
import com.larpclient.t7;
import com.larpclient.yv;
import net.minecraft.class_11890;
import net.minecraft.class_1657;
import net.minecraft.class_310;

public final class HeadlessKorbloxModule extends Module {
   public static final int d = -14935272;
   public static final int m = -10178636;
   private static HeadlessKorbloxModule a;
   public final l3 w = this.addSetting(new l3("Self", "Wear it on your own player", true));
   public final l3 u = this.addSetting(new l3("Other Players", "Local preview on nearby players; only you see it", false));
   public final l3 N = this.addSetting(new l3("Headless", "Hide the head and hat layer like Roblox's Headless Horseman", true));
   public final l3 x = this.addSetting(
      new l3("Hide Helmets", "Also hide helmets, carved pumpkins and skulls so no head shows; off lets them float like Roblox hats", true)
   );
   public final l3 z = this.addSetting(new l3("Korblox", "Swap a leg for the Korblox Deathspeaker bone spike", true));
   public final li c = this.addSetting(new li("Leg", "Which leg becomes the bone spike; Roblox's is the right", "Right", "Right", "Left", "Both"));
   public final lo i = this.addSetting(
      new lo("Length", "Spike length; 1 matches the Roblox leg, the longest reaches the foot", 1.0, 0.6, Math.floor(t7.D() * 20.0) / 20.0, 0.05)
   );
   public final lo G = this.addSetting(new lo("Thickness", "Spike and hip knob width", 1.0, 0.6, 1.5, 0.05));
   public final l0 s = this.addSetting(new l0("Hip Color", "Color at the hip knob", -14935272));
   public final l0 W = this.addSetting(new l0("Tip Color", "Color the bone fades to at its point", -10178636));
   public final lo H = this.addSetting(new lo("Tip Glow", "How brightly the point glows in the dark", 0.3, 0.0, 1.0, 0.05));
   private yv b;

   public HeadlessKorbloxModule() {
      super("Headless Korblox", "Go headless and swap a leg for the Korblox bone spike, like the Roblox combo", Category.VISUALS, -1);
      a = this;
      this.x.O(this.N::getValue);

      for (lb var4 : new lb[]{this.c, this.i, this.G, this.s, this.W, this.H}) {
         var4.O(this.z::getValue);
      }

      this.N.a(var0 -> q8.G("Headless", "Headless={}", var0));
      this.x.a(var0 -> q8.G("Headless", "Hide Helmets={}", var0));
      this.z.a(var0 -> q8.G("Korblox", "Korblox={}", var0));
   }

   public static HeadlessKorbloxModule e() {
      return a;
   }

   @Override
   public void onEnable() {
      q8.G(
         "Headless",
         "Enabled; headless={}, hideHelmets={}, korblox={}, leg={}, self={}, others={}",
         this.N.getValue(),
         this.x.getValue(),
         this.z.getValue(),
         this.c.getValue(),
         this.w.getValue(),
         this.u.getValue()
      );
   }

   public int o(class_11890 var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.N.getValue() && this.H(var1)) {
            return this.x.getValue() ? 2 : 1;
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }

   public yv n(class_11890 var1) {
      if (this.isEnabled()) {
         return this.isRuntimeActive() && this.z.getValue() && this.H(var1) ? this.J() : null;
      } else {
         return null;
      }
   }

   public boolean s() {
      return !this.isEnabled() ? false : this.isOperational() && this.w.getValue() && this.N.getValue();
   }

   public yv w() {
      if (this.isEnabled()) {
         return this.isOperational() && this.w.getValue() && this.z.getValue() ? this.J() : null;
      } else {
         return null;
      }
   }

   private boolean H(class_11890 var1) {
      class_310 var2 = getMc();
      return var2 != null && var1 == var2.field_1724 ? this.w.getValue() : this.u.getValue() && var1 instanceof class_1657 var3 && !var3.method_7325();
   }

   private yv J() {
      int var1 = this.c.G("Both") ? 3 : (this.c.G("Left") ? 2 : 1);
      int var2 = this.s.getValue();
      int var3 = this.W.getValue();
      float var4 = this.H.getValue().floatValue();
      float var5 = this.i.getValue().floatValue();
      float var6 = this.G.getValue().floatValue();
      yv var7 = this.b;
      if (var7 == null
         || var7.legs() != var1
         || var7.hip() != var2
         || var7.tip() != var3
         || var7.glow() != var4
         || var7.length() != var5
         || var7.thickness() != var6) {
         this.b = var7 = new yv(var1, var2, var3, var4, var5, var6);
      }

      return var7;
   }
}

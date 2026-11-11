package com.larpclient.module.render;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.LegTrail;
import com.larpclient.render.model.ChinaHatMesh;
import com.larpclient.util.misc.LarpLog;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;

public final class HeadlessKorbloxModule extends Module {
   public static final int dEFAULT_HIP_COLOR = -14935272;
   public static final int dEFAULT_TIP_COLOR = -10178636;
   private static HeadlessKorbloxModule instance;
   public final BooleanSetting self = this.addSetting(new BooleanSetting("Self", "Wear it on your own player", true));
   public final BooleanSetting otherPlayers = this.addSetting(new BooleanSetting("Other Players", "Local preview on nearby players; only you see it", false));
   public final BooleanSetting headless = this.addSetting(new BooleanSetting("Headless", "Hide the head and hat layer like Roblox's Headless Horseman", true));
   public final BooleanSetting hideHelmets = this.addSetting(
      new BooleanSetting("Hide Helmets", "Also hide helmets, carved pumpkins and skulls so no head shows; off lets them float like Roblox hats", true)
   );
   public final BooleanSetting korblox = this.addSetting(new BooleanSetting("Korblox", "Swap a leg for the Korblox Deathspeaker bone spike", true));
   public final ModeSetting leg = this.addSetting(
      new ModeSetting("Leg", "Which leg becomes the bone spike; Roblox's is the right", "Right", "Right", "Left", "Both")
   );
   public final NumberSetting length = this.addSetting(
      new NumberSetting(
         "Length", "Spike length; 1 matches the Roblox leg, the longest reaches the foot", 1.0, 0.6, Math.floor(ChinaHatMesh.aspectRatio() * 20.0) / 20.0, 0.05
      )
   );
   public final NumberSetting thickness = this.addSetting(new NumberSetting("Thickness", "Spike and hip knob width", 1.0, 0.6, 1.5, 0.05));
   public final ColorSetting hipColor = this.addSetting(new ColorSetting("Hip Color", "Color at the hip knob", -14935272));
   public final ColorSetting tipColor = this.addSetting(new ColorSetting("Tip Color", "Color the bone fades to at its point", -10178636));
   public final NumberSetting tipGlow = this.addSetting(new NumberSetting("Tip Glow", "How brightly the point glows in the dark", 0.3, 0.0, 1.0, 0.05));
   private LegTrail cachedLegTrail;

   public HeadlessKorbloxModule() {
      super("Headless Korblox", "Go headless and swap a leg for the Korblox bone spike, like the Roblox combo", Category.VISUALS, -1);
      instance = this;
      this.hideHelmets.visibleWhen(this.headless::getValue);

      for (Setting var4 : new Setting[]{this.leg, this.length, this.thickness, this.hipColor, this.tipColor, this.tipGlow}) {
         var4.visibleWhen(this.korblox::getValue);
      }

      this.headless.onChange(var0 -> LarpLog.info("Headless", "Headless={}", var0));
      this.hideHelmets.onChange(var0 -> LarpLog.info("Headless", "Hide Helmets={}", var0));
      this.korblox.onChange(var0 -> LarpLog.info("Korblox", "Korblox={}", var0));
   }

   public static HeadlessKorbloxModule getInstance() {
      return instance;
   }

   @Override
   public void onEnable() {
      LarpLog.info(
         "Headless",
         "Enabled; headless={}, hideHelmets={}, korblox={}, leg={}, self={}, others={}",
         this.headless.getValue(),
         this.hideHelmets.getValue(),
         this.korblox.getValue(),
         this.leg.getValue(),
         this.self.getValue(),
         this.otherPlayers.getValue()
      );
   }

   public int getHeadMode(Avatar var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.headless.getValue() && this.appliesTo(var1)) {
            return this.hideHelmets.getValue() ? 2 : 1;
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }

   public LegTrail getLegTrail(Avatar var1) {
      if (this.isEnabled()) {
         return this.isRuntimeActive() && this.korblox.getValue() && this.appliesTo(var1) ? this.resolveLegTrail() : null;
      } else {
         return null;
      }
   }

   public boolean isSelfHeadless() {
      return !this.isEnabled() ? false : this.isOperational() && this.self.getValue() && this.headless.getValue();
   }

   public LegTrail getSelfLegTrail() {
      if (this.isEnabled()) {
         return this.isOperational() && this.self.getValue() && this.korblox.getValue() ? this.resolveLegTrail() : null;
      } else {
         return null;
      }
   }

   private boolean appliesTo(Avatar var1) {
      Minecraft var2 = getMc();
      return var2 != null && var1 == var2.player ? this.self.getValue() : this.otherPlayers.getValue() && var1 instanceof Player var3 && !var3.isSpectator();
   }

   private LegTrail resolveLegTrail() {
      int var1 = this.leg.isMode("Both") ? 3 : (this.leg.isMode("Left") ? 2 : 1);
      int var2 = this.hipColor.getValue();
      int var3 = this.tipColor.getValue();
      float var4 = this.tipGlow.getValue().floatValue();
      float var5 = this.length.getValue().floatValue();
      float var6 = this.thickness.getValue().floatValue();
      LegTrail var7 = this.cachedLegTrail;
      if (var7 == null
         || var7.legs() != var1
         || var7.hip() != var2
         || var7.tip() != var3
         || var7.glow() != var4
         || var7.length() != var5
         || var7.thickness() != var6) {
         this.cachedLegTrail = var7 = new LegTrail(var1, var2, var3, var4, var5, var6);
      }

      return var7;
   }
}

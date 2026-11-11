package com.larpclient.module.misc;

import com.larpclient.gui.theme.Theme;
import com.larpclient.render.entity.EchoHandRenderer;
import com.larpclient.util.data.TickTimer;
import java.awt.Color;
import java.util.List;
import java.util.Locale;

public final class EchoHandModule extends Module {
   private static EchoHandModule instance;
   public final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "History material", "Echo Trail", "Echo Trail", "Flame"));
   public final BooleanSetting echoTrail = this.addSetting(new BooleanSetting("Echo Trail", "Enable the echo trail material", true));
   public final NumberSetting strength = this.addNumberSetting("Strength", 0.75, 0.05, 1.5, 0.05, false);
   public final NumberSetting radius = this.addNumberSetting("Radius", 11.0, 2.0, 28.0, 0.5, false);
   public final NumberSetting trail = this.addNumberSetting("Trail", 0.84, 0.55, 0.96, 0.01, false);
   public final NumberSetting lag = this.addNumberSetting("Lag", 1.15, 0.0, 3.0, 0.05, false);
   public final ColorSetting color = this.addSetting(new ColorSetting("Color", "Echo color and opacity", -924394030));
   public final BooleanSetting outsideOnly = this.addSetting(new BooleanSetting("Outside Only", "Keep echoes outside the current hand silhouette", true));
   public final ModeSetting flameColorMode = this.addSetting(new ModeSetting("Flame Color", "Flame color source", "Custom", "Custom", "Client", "Rainbow"));
   public final ColorSetting flameColor = this.addSetting(new ColorSetting("Flame Color Value", "Flame color and opacity", -419430401));
   public final NumberSetting flameStrength = this.addNumberSetting("Flame Strength", 0.85, 0.0, 2.0, 0.05, true);
   public final NumberSetting riseSpeed = this.addNumberSetting("Rise Speed", 0.0, 0.0, 2.0, 0.05, true);
   public final NumberSetting wobble = this.addNumberSetting("Wobble", 0.65, 0.0, 2.0, 0.05, true);
   public final NumberSetting length = this.addNumberSetting("Length", 0.95, 0.1, 2.5, 0.05, true);
   public final NumberSetting flameBrightness = this.addNumberSetting("Flame Brightness", 0.9, 0.0, 2.0, 0.05, true);
   public final NumberSetting width = this.addNumberSetting("Width", 1.0, 0.1, 3.0, 0.05, true);
   public final NumberSetting fire = this.addNumberSetting("Fire", 1.0, 0.0, 3.0, 0.05, true);
   public final NumberSetting turbulence = this.addNumberSetting("Turbulence", 1.0, 0.0, 3.0, 0.05, true);
   public final BooleanSetting onlyItems = this.addSetting(new BooleanSetting("Only Items", "Require an item in either hand for the flame material", false));

   public EchoHandModule() {
      super("EchoHand", "Colored motion history around first-person hands and items", Category.RENDER, -1);
      instance = this;
      this.echoTrail.visibleWhen(() -> !this.isFlame());

      for (Setting var2 : List.of(this.strength, this.radius, this.trail, this.lag, this.color, this.outsideOnly)) {
         var2.visibleWhen(() -> !this.isFlame() && this.echoTrail.getValue());
      }

      this.flameColorMode.visibleWhen(this::isFlame);
      this.flameColor.visibleWhen(() -> this.isFlame() && this.flameColorMode.getValue().equals("Custom"));
      this.onlyItems.visibleWhen(this::isFlame);
      this.mode.onChange(var0 -> EchoHandRenderer.release());
      this.echoTrail.onChange(var0 -> EchoHandRenderer.release());
   }

   private NumberSetting addNumberSetting(String var1, double var2, double var4, double var6, final double var8, boolean var10) {
      var var11 = (<unrepresentable>)this.addSetting(new NumberSetting(var1, var1 + " of the history material", var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 0.5 ? "%.1f" : "%.2f", this.getValue());
         }
      });
      var11.visibleWhen(() -> this.isFlame() == var10);
      return var11;
   }

   public boolean isFlame() {
      return this.mode.getValue().equals("Flame");
   }

   public List<Setting<?>> getModeSettings() {
      return List.of(this.mode, this.echoTrail, this.onlyItems);
   }

   public List<Setting<?>> getAppearanceSettings() {
      return List.of(
         this.strength, this.color, this.outsideOnly, this.flameColorMode, this.flameColor, this.flameStrength, this.flameBrightness, this.length, this.width
      );
   }

   public List<Setting<?>> addNumberSetting() {
      return List.of(this.radius, this.trail, this.lag, this.riseSpeed, this.wobble, this.fire, this.turbulence);
   }

   public static EchoHandModule getInstance() {
      return instance;
   }

   public int getColor() {
      if (!this.isFlame()) {
         return this.color.getValue();
      }

      return switch ((String)this.flameColorMode.getValue()) {
         case "Client" -> -436207616 | Theme.accent & 16777215;
         case "Rainbow" -> -436207616 | Color.HSBtoRGB((float)(TickTimer.getTime() / 4.0 % 1.0), 0.72F, 1.0F) & 16777215;
         default -> this.flameColor.getValue();
      };
   }

   @Override
   public void onDisable() {
      EchoHandRenderer.release();
   }

   @Override
   public void onTick() {
      EchoHandRenderer.releaseIfInvalid();
   }
}

package com.larpclient.module.render;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.PoseStackUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.world.InteractionHand;

public final class ViewModelModule extends Module {
   public static final double mIN_SCALE = 0.1;
   public static final double mAX_SCALE = 2.0;
   private static final int mIN_SWING_TICKS = 2;
   private final ViewModelModule.HandSettings mainHand = this.createHandSettings("Main", "main hand");
   private final ViewModelModule.HandSettings offHand = this.createHandSettings("Off", "offhand");
   private final NumberSetting swingSpeed = this.addNumberSetting(
      "Swing Speed", "Speed", "How fast your hits animate. 1 is normal, lower is slower, higher is faster", 1.0, 0.25, 3.0, 0.25
   );
   private static ViewModelModule instance;

   public ViewModelModule() {
      super("ViewModel", "Resize, move and rotate each hand's item and change swing speed. Drag hands in chat to place them", Category.VISUALS, -1);
      instance = this;
   }

   public static ViewModelModule getInstance() {
      return instance;
   }

   public List<Setting<?>> resetHand() {
      return List.of(this.swingSpeed);
   }

   public List<Setting<?>> getMainHandSettings() {
      return this.mainHand.settings();
   }

   public List<Setting<?>> getInstance() {
      return this.offHand.settings();
   }

   public int scaleSwingDuration(int var1) {
      if (this.isEnabled()) {
         double var2 = this.swingSpeed.getValue();
         return var2 == 1.0 ? var1 : Math.max(2, (int)Math.round(var1 / var2));
      } else {
         return var1;
      }
   }

   private ViewModelModule.HandSettings getHandSettings(InteractionHand var1) {
      return var1 == InteractionHand.OFF_HAND ? this.offHand : this.mainHand;
   }

   public void applyTranslation(PoseStack var1, InteractionHand var2, boolean var3) {
      if (this.isEnabled()) {
         ViewModelModule.HandSettings var4 = this.getHandSettings(var2);
         var1.translate((var3 ? -1 : 1) * var4.offsetX().getValue(), var4.offsetY().getValue(), var4.offsetZ().getValue());
      }
   }

   public void applyRotationAndScale(PoseStack var1, InteractionHand var2, boolean var3) {
      if (this.isEnabled()) {
         ViewModelModule.HandSettings var4 = this.getHandSettings(var2);
         float var5 = var3 ? -1.0F : 1.0F;
         PoseStackUtil.rotate(var1, Axis.XP.rotationDegrees(var4.pitch().getValue().floatValue()));
         PoseStackUtil.rotate(var1, Axis.YP.rotationDegrees(var5 * var4.yaw().getValue().floatValue()));
         PoseStackUtil.rotate(var1, Axis.ZP.rotationDegrees(var5 * var4.roll().getValue().floatValue()));
         float var6 = var4.scale().getValue().floatValue();
         var1.scale(var6, var6, var6);
      }
   }

   public void scaleSwingDuration(PoseStack var1, InteractionHand var2, boolean var3) {
      if (this.isEnabled()) {
         float var4 = (var3 ? 6 : -6) / 16.0F;
         float var5 = 0.75F;
         var1.translate(var4, var5, 0.0F);
         this.applyRotationAndScale(var1, var2, var3);
         var1.translate(-var4, -var5, 0.0F);
      }
   }

   public double getScale(InteractionHand var1) {
      return this.getHandSettings(var1).scale().getValue();
   }

   public double getOffsetX(InteractionHand var1) {
      return this.getHandSettings(var1).offsetX().getValue();
   }

   public double getOffsetY(InteractionHand var1) {
      return this.getHandSettings(var1).offsetY().getValue();
   }

   public void setOffset(InteractionHand var1, double var2, double var4) {
      ViewModelModule.HandSettings var6 = this.getHandSettings(var1);
      var6.offsetX().getDisplayValue(var2);
      var6.offsetY().getDisplayValue(var4);
   }

   public void applyTranslation(InteractionHand var1, double var2) {
      ViewModelModule.HandSettings var4 = this.getHandSettings(var1);
      var4.scale().getDisplayValue(var4.scale().getValue() + var2 * 0.05);
   }

   public void resetHand(InteractionHand var1) {
      ViewModelModule.HandSettings var2 = this.getHandSettings(var1);

      for (NumberSetting var4 : List.of(var2.scale(), var2.offsetX(), var2.offsetY(), var2.offsetZ())) {
         var4.reset();
      }
   }

   @Override
   public void onEnable() {
      LarpLog.info(
         "ViewModel",
         "Active: main {}x, offhand {}x, swing speed {}x",
         this.mainHand.scale().getValue(),
         this.offHand.scale().getValue(),
         this.swingSpeed.getValue()
      );
   }

   @Override
   public void onDisable() {
      LarpLog.info("ViewModel", "Hands returned to vanilla placement");
   }

   private ViewModelModule.HandSettings createHandSettings(String var1, String var2) {
      return new ViewModelModule.HandSettings(
         this.addNumberSetting(var1 + " Scale", "Size", "Size of the " + var2 + " item or arm", 1.0, 0.1, 2.0, 0.05),
         this.addNumberSetting(var1 + " X", "Sideways", "Positive moves the " + var2 + " outward", 0.0, -2.5, 2.5, 0.01),
         this.addNumberSetting(var1 + " Y", "Height", "Vertical offset of the " + var2, 0.0, -2.0, 2.0, 0.01),
         this.addNumberSetting(var1 + " Z", "Depth", "Negative moves the " + var2 + " away from you", 0.0, -2.0, 1.0, 0.01),
         this.addNumberSetting(var1 + " Pitch", "Pitch", "Tilt of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0),
         this.addNumberSetting(var1 + " Yaw", "Yaw", "Turn of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0),
         this.addNumberSetting(var1 + " Roll", "Roll", "Roll of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0)
      );
   }

   private NumberSetting addNumberSetting(String var1, String var2, String var3, double var4, double var6, double var8, double var10) {
      NumberSetting var12 = this.addSetting(new NumberSetting(var1, var3, var4, var6, var8, var10));
      var12.setDisplayName(var2);
      return var12;
   }

   private record HandSettings(
      NumberSetting scale, NumberSetting x, NumberSetting y, NumberSetting z, NumberSetting pitch, NumberSetting yaw, NumberSetting roll
   ) {
      private final NumberSetting offsetX;
      private final NumberSetting offsetY;
      private final NumberSetting offsetZ;

      private HandSettings(NumberSetting scale, NumberSetting x, NumberSetting y, NumberSetting z, NumberSetting pitch, NumberSetting yaw, NumberSetting roll) {
         this.scale = scale;
         this.offsetX = x;
         this.offsetY = y;
         this.offsetZ = z;
         this.pitch = pitch;
         this.yaw = yaw;
         this.roll = roll;
      }

      List<Setting<?>> settings() {
         return List.of(this.scale, this.offsetX, this.offsetY, this.offsetZ, this.pitch, this.yaw, this.roll);
      }
   }
}

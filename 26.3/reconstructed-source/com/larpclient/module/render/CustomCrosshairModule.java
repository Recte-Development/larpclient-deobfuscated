package com.larpclient.module.render;

import com.google.gson.JsonObject;
import com.larpclient.core.StreamProofManager;
import com.larpclient.gui.hud.HudEditorScreen;
import com.larpclient.gui.misc.DrawingGrid;
import com.larpclient.gui.theme.Theme;
import com.larpclient.hud.CrosshairRenderer;
import com.larpclient.hud.CrosshairShape;
import com.larpclient.hud.HudLayout;
import com.larpclient.hud.HudLayout2;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.misc.TextSetting;
import com.larpclient.render.overlay.CrosshairImages;
import com.larpclient.util.misc.LarpLog;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.Identifier;

public final class CustomCrosshairModule extends Module {
   public static final String iMAGE_STYLE = "Tung Tung";
   private static final Map<String, String> styleAliases = Map.of("6-7", "67", "Custom Image", "Tung Tung");
   private static CustomCrosshairModule instance;
   private final ModeSetting style = this.addSetting(
      new ModeSetting(
         "Style", "Crosshair geometry", "Star of David", Stream.concat(CrosshairShape.sTYLES.stream(), Stream.of("Tung Tung")).toArray(String[]::new)
      ) {
         @Override
         public boolean acceptsValue(String var1) {
            return CustomCrosshairModule.styleAliases.containsKey(var1) || super.acceptsValue(var1);
         }

         @Override
         public void setValue(String var1) {
            super.setValue(CustomCrosshairModule.styleAliases.getOrDefault(var1, var1));
         }
      }
   );
   private final TextSetting drawing = this.addSetting(new TextSetting("Drawing", "Cells of the drawn crosshair", DrawingGrid.dEFAULT_CROSSHAIR.toHex()));
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "Shape radius; half the image width", 7.0, 2.0, 24.0, 1.0));
   private final NumberSetting thickness = this.addSetting(new NumberSetting("Thickness", "Line width", 1.0, 1.0, 5.0, 1.0));
   private final BooleanSetting themeColor = this.addSetting(new BooleanSetting("Theme Color", "Follow the interface accent color", true));
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Crosshair color and opacity", -16737281));
   private final BooleanSetting outline = this.addSetting(new BooleanSetting("Outline", "Thin dark contour for bright and dark scenery", false));
   private final BooleanSetting spin = this.addSetting(new BooleanSetting("Spin", "Continuously rotate the crosshair", true));
   private final NumberSetting spinSpeed = this.addSetting(new NumberSetting("Spin Speed", "Degrees per second", 180.0, 30.0, 720.0, 15.0));
   private final BooleanSetting clickSpin = this.addSetting(new BooleanSetting("Click Spin", "Turn a quarter on each left click", false));
   private final NumberSetting clickSpinTime = this.addSetting(
      new NumberSetting("Click Spin Time", "Milliseconds per quarter turn", 350.0, 100.0, 1000.0, 25.0)
   );
   private double spinAngle;
   private long lastSpinNanos;
   private double clickStartAngle;
   private double clickTargetAngle;
   private double clickStartVelocity;
   private long lastClickNanos;
   private CrosshairImages.TextureHandle imageTexture;
   private String cachedDrawingHex;
   private DrawingGrid drawingGrid = DrawingGrid.dEFAULT_CROSSHAIR;

   public CustomCrosshairModule() {
      super("CustomCrosshair", "Customize your crosshair", Category.RENDER, -1);
      instance = this;
      this.spinSpeed.visibleWhen(this.spin::getValue);
      this.color.visibleWhen(() -> !this.themeColor.getValue());
      this.clickSpinTime.visibleWhen(this.clickSpin::getValue);
      this.thickness.visibleWhen(() -> CrosshairShape.snapToPixel(this.style.getValue()));
      this.outline.visibleWhen(() -> !this.style.isMode("Tung Tung"));
      this.drawing.visibleWhen(() -> false);
      this.style.onChange(var0 -> LarpLog.info("Crosshair", "Style selected: {}", var0));
      this.spin.onChange(var0 -> LarpLog.info("Crosshair", "Spin {}", var0 ? "enabled" : "disabled"));
      this.clickSpin.onChange(var1 -> {
         this.resetClickSpin();
         LarpLog.info("Crosshair", "Click Spin {}", var1 ? "enabled" : "disabled");
      });
      HudElementRegistry.addLast(
         Identifier.fromNamespaceAndPath("larp-client", "customcrosshair"),
         (var1, var2) -> {
            Minecraft var3 = getMc();
            if (this.isOperational()
               && this.isInGame()
               && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
               && var3.options.getCameraType().isFirstPerson()
               && (
                  var3.gui.screen() == null
                     || var3.gui.screen().getClass() == ChatScreen.class
                     || var3.gui.screen() instanceof HudEditorScreen var4 && var4.isLiveContent()
               )) {
               var1 = StreamProofManager.selectGraphics(var1, false, false);
               if (var1 != null) {
                  HudLayout.beginFrame(var1);
                  HudLayout.HudElement var10 = HudLayout.getElement("crosshair");
                  if (!var10.isHidden()) {
                     var10.fitTo(var3.getWindow().getGuiScaledWidth(), var3.getWindow().getGuiScaledHeight());
                     var10.getY(var1, var10.getX(var3.getWindow().getGuiScaledWidth()), var10.getY(var3.getWindow().getGuiScaledHeight()));

                     try {
                        this.renderCrosshair(
                           var1,
                           var10.getX(var3.getWindow().getGuiScaledWidth()) + var10.width / 2,
                           var10.getY(var3.getWindow().getGuiScaledHeight()) + var10.height / 2
                        );
                     } finally {
                        var10.popScale(var1);
                     }
                  }
               }
            }
         }
      );
   }

   public static void drawPreview(GuiGraphicsExtractor var0, int var1, int var2) {
      if (instance != null) {
         instance.draw(var0, var1, var2, true);
      }
   }

   public static int getCurrentExtent() {
      return instance == null ? 10 : instance.getRadius(instance.style.isMode("Tung Tung") ? instance.imageTexture : null);
   }

   private int getRadius(CrosshairImages.TextureHandle var1) {
      int var2 = this.size.getValue().intValue();
      return var1 != null
         ? CrosshairRenderer.imageRadius(var1, var2, this.spin.getValue() || this.clickSpin.getValue())
         : CrosshairShape.extent(this.style.getValue(), var2, this.thickness.getValue().intValue(), this.outline.getValue());
   }

   public List<Setting<?>> getShapeSettings() {
      return List.of(this.style, this.drawing, this.size, this.thickness);
   }

   public List<Setting<?>> getAppearanceSettings() {
      return List.of(this.themeColor, this.color, this.outline, this.spin, this.spinSpeed, this.clickSpin, this.clickSpinTime);
   }

   public boolean isImageStyle() {
      return this.style.isMode("Tung Tung");
   }

   public boolean isDrawMode() {
      return this.style.isMode("Draw Your Own");
   }

   public DrawingGrid getDrawing() {
      String var1 = this.drawing.getValue();
      if (!var1.equals(this.cachedDrawingHex)) {
         this.cachedDrawingHex = var1;

         try {
            this.drawingGrid = DrawingGrid.fromHex(var1);
         } catch (IllegalArgumentException var3) {
            this.drawingGrid = DrawingGrid.dEFAULT_CROSSHAIR;
            LarpLog.warn("Crosshair", "Saved drawing is unreadable ({}); showing the default until it is edited", var3.getMessage());
         }
      }

      return this.drawingGrid;
   }

   public void setCell(int var1, int var2, boolean var3) {
      DrawingGrid var4 = this.getDrawing().withCell(var1, var2, var3);
      if (var4 != this.drawingGrid) {
         this.drawing.setText(var4.toHex());
      }
   }

   public void clearDrawing() {
      this.drawing.setText(DrawingGrid.eMPTY.toHex());
      LarpLog.info("Crosshair", "Drawing cleared");
   }

   public static void migrateConfig(JsonObject var0) {
      if (!var0.has("Theme Color") && var0.has("Color")) {
         try {
            int var1 = (int)((long)var0.get("Color").getAsDouble());
            var0.addProperty("Theme Color", var1 == -1381654 || var1 == Theme.accent);
         } catch (RuntimeException var2) {
         }
      }
   }

   private int getRawColor() {
      return this.themeColor.getValue() ? Theme.accent | 0xFF000000 : this.color.getValue();
   }

   public int getColor() {
      return this.getRawColor() | 0xFF000000;
   }

   @Override
   public void onEnable() {
      this.spinAngle = 0.0;
      this.resetClickSpin();
   }

   @Override
   public void onDisable() {
      this.spinAngle = 0.0;
      this.resetClickSpin();
      this.imageTexture = null;
      CrosshairImages.reset();
   }

   public static void onClick(int var0) {
      if (instance != null && instance.isEnabled()) {
         CustomCrosshairModule var1 = instance;
         if (var0 == 0 && var1 != null && var1.isOperational() && var1.clickSpin.getValue() && getMc().player != null && getMc().gui.screen() == null) {
            var1.startClickSpin(System.nanoTime());
         }
      }
   }

   private void resetClickSpin() {
      this.clickStartAngle = this.clickTargetAngle = this.clickStartVelocity = 0.0;
      this.lastClickNanos = 0L;
   }

   private void startClickSpin(long var1) {
      double var3 = this.clickSpinTime.getValue() / 1000.0;
      double var5 = this.lastClickNanos == 0L ? 1.0 : Math.clamp((var1 - this.lastClickNanos) / 1.0E9 / var3, 0.0, 1.0);
      double var7 = this.hermiteAngle(var5);
      double var9 = this.hermiteVelocity(var5);
      double var11 = Math.floor(var7 / (Math.PI * 2)) * 2.0 * Math.PI;
      this.clickStartAngle = var7 - var11;
      this.clickTargetAngle = this.clickTargetAngle - var11 + (Math.PI / 2);
      this.clickStartVelocity = var9;
      this.lastClickNanos = var1;
   }

   private double hermiteAngle(double var1) {
      double var3 = this.clickSpinTime.getValue() / 1000.0;
      double var5 = var1 * var1;
      double var7 = var5 * var1;
      return (2.0 * var7 - 3.0 * var5 + 1.0) * this.clickStartAngle
         + (var7 - 2.0 * var5 + var1) * this.clickStartVelocity * var3
         + (3.0 * var5 - 2.0 * var7) * this.clickTargetAngle;
   }

   private double hermiteVelocity(double var1) {
      double var3 = this.clickSpinTime.getValue() / 1000.0;
      double var5 = var1 * var1;
      return (
            (6.0 * var5 - 6.0 * var1) * this.clickStartAngle
               + (3.0 * var5 - 4.0 * var1 + 1.0) * this.clickStartVelocity * var3
               + (6.0 * var1 - 6.0 * var5) * this.clickTargetAngle
         )
         / var3;
   }

   private double clearDrawing() {
      if (this.clickSpin.getValue() && this.lastClickNanos != 0L) {
         double var1 = Math.clamp((System.nanoTime() - this.lastClickNanos) / 1.0E9 / (this.clickSpinTime.getValue() / 1000.0), 0.0, 1.0);
         return this.hermiteAngle(var1);
      } else {
         return this.clickTargetAngle;
      }
   }

   private double updateSpinAngle() {
      long var1 = System.nanoTime();
      if (!this.spin.getValue()) {
         this.spinAngle = 0.0;
         this.lastSpinNanos = var1;
         return 0.0;
      } else {
         double var3 = Math.clamp((var1 - this.lastSpinNanos) / 1.0E9, 0.0, 0.1);
         this.lastSpinNanos = var1;
         this.spinAngle = (this.spinAngle + var3 * Math.toRadians(this.spinSpeed.getValue())) % (Math.PI * 2);
         return this.spinAngle;
      }
   }

   private void renderCrosshair(GuiGraphicsExtractor var1, int var2, int var3) {
      this.draw(var1, var2, var3, false);
   }

   private void draw(GuiGraphicsExtractor var1, int var2, int var3, boolean var4) {
      Minecraft var5 = getMc();
      int var6 = this.size.getValue().intValue();
      int var7 = this.thickness.getValue().intValue();
      int var8 = this.getRawColor();
      CrosshairImages.TextureHandle var9 = null;
      if (this.style.isMode("Tung Tung")) {
         double var10 = Math.hypot(var1.pose().m00(), var1.pose().m01()) * var5.getWindow().getGuiScale();
         var9 = CrosshairImages.getTexture((int)Math.ceil(2 * var6 * var10), var4);
      }

      if (!var4) {
         this.imageTexture = var9;
      }

      if (!var4 && var8 >>> 24 != 0) {
         HudLayout.HudElement var12 = HudLayout.getElement("crosshair");
         int var11 = this.getRadius(var9);
         HudLayout.addBounds(
            var12,
            new HudLayout2.Rect(
               var12.getX(var5.getWindow().getGuiScaledWidth()) + (int)Math.floor((var12.width / 2.0 - var11) * var12.effectiveScale()),
               var12.getY(var5.getWindow().getGuiScaledHeight()) + (int)Math.floor((var12.height / 2.0 - var11) * var12.effectiveScale()),
               (int)Math.ceil(2 * var11 * var12.effectiveScale()),
               (int)Math.ceil(2 * var11 * var12.effectiveScale())
            )
         );
      }

      double var13 = this.updateSpinAngle() + this.clearDrawing();
      if (var9 != null) {
         CrosshairRenderer.drawImageCrosshair(var1, var2, var3, var9, var6, var8, var13);
      } else {
         CrosshairRenderer.drawCrosshair(
            var1,
            var2,
            var3,
            this.style.isMode("Tung Tung") ? "Star of David" : this.style.getValue(),
            var6,
            var7,
            this.getDrawing(),
            var8,
            this.outline.getValue(),
            var13,
            var4
         );
      }
   }

   public static boolean isActive() {
      return instance != null && instance.isEnabled() ? instance != null && instance.isOperational() : false;
   }
}

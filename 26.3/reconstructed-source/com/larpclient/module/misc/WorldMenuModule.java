package com.larpclient.module.misc;

import com.larpclient.gui.clickgui.ClickGuiScreen;
import com.larpclient.gui.hud.HudEditorScreen;
import com.larpclient.gui.misc.GuiLayout;
import com.larpclient.gui.theme.Icons;
import com.larpclient.gui.theme.Theme;
import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.render.overlay.ScreenWarp;
import com.larpclient.util.math.CameraMath;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import com.mojang.blaze3d.platform.Window;
import java.util.Locale;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.util.Util;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.joml.Matrix4f;

public final class WorldMenuModule extends Module {
   private static WorldMenuModule instance;
   private final ModeSetting placement = this.addSetting(
      new ModeSetting("Placement", "Pinned stays where you leave it and closes once you walk away; Follow moves along with you", "Pinned", "Pinned", "Follow")
   );
   private final NumberSetting closeDistance = this.addSetting(
      new NumberSetting("Close Distance", "A pinned menu closes once you are this many blocks from it", 15.0, 4.0, 48.0, 1.0)
   );
   private final NumberSetting menuHeight = this.addSetting(
      new NumberSetting("Menu Height", "Share of your view the menu fills when it opens in front of you, in percent", 50.0, 30.0, 90.0, 1.0)
   );
   private final NumberSetting panelSize = this.addSetting(new NumberSetting("Panel Size", "Height of the menu panel in blocks", 1.5, 0.5, 4.0, 0.1));
   private static final double mIN_DISTANCE = 0.5;
   private static final double mAX_DISTANCE = 16.0;
   private static final long vISIBILITY_CHECK_INTERVAL_NANOS = 50000000L;
   private static final long dOUBLE_CLICK_MILLIS = 250L;
   private static final int hANDLE_WIDTH = 76;
   private static final int hANDLE_HEIGHT = 6;
   private static final int hANDLE_GAP = 9;
   private static final int hANDLE_HIT_PADDING_X = 16;
   private static final int hANDLE_HIT_PADDING_Y = 9;
   private static final int cLOSE_BUTTON_SIZE = 16;
   private static final int cLOSE_BUTTON_OFFSET = 22;
   private CameraMath.CameraPose panelPose;
   private Object panelLevel;
   private ClickGuiScreen activeScreen;
   private ClickGuiScreen closingScreen;
   private Vec3 lastCameraPos;
   private boolean cursorOnPanel;
   private double cursorX;
   private double cursorY;
   private int heldButtons;
   private double lastDragX;
   private double lastDragY;
   private long lastClickTime;
   private int lastClickButton = -1;
   private WorldMenuModule.CameraOffset grabOffset;
   private final Animation visibility = new Animation(0.0);
   private final Animation handleHover = new Animation(0.0);
   private double targetVisibility = 1.0;
   private long lastVisibilityCheck;

   public WorldMenuModule() {
      super("World Menu", "Opens the ClickGUI on a panel in the world while you keep playing", Category.RENDER, -1);
      instance = this;
   }

   public static WorldMenuModule getInstance() {
      return instance;
   }

   @Override
   public String getDisplayName() {
      return "3D Click UI";
   }

   private boolean isAvailable() {
      Minecraft var1 = getMc();
      return this.isRuntimeActive() && var1.player != null && var1.level != null && var1.gameRenderer.mainCamera().isInitialized();
   }

   private boolean isWorldFocused() {
      Minecraft var1 = getMc();
      return var1.gui.screen() == null && var1.gui.overlay() == null && var1.mouseHandler.isMouseGrabbed();
   }

   private double getPanelScale(ClickGuiScreen var1) {
      double[] var2 = var1.getBoundsPixels();
      return this.panelSize.getValue() / Math.max(1.0, var2[3] - var2[1]);
   }

   private ScreenWarp.HomographyWarp computeWarp(ClickGuiScreen var1, CameraMath.CameraPose var2) {
      Camera var3 = getMc().gameRenderer.mainCamera();
      Vec3 var4 = var3.position();
      return CameraMath.projectionBasis(
         var2,
         this.getPanelScale(var1),
         var1.width / 2.0,
         var1.height / 2.0,
         var4.x,
         var4.y,
         var4.z,
         var3.getViewRotationProjectionMatrix(new Matrix4f()),
         var1.width,
         var1.height
      );
   }

   private static double getPanelDistance(ClickGuiScreen var0, ScreenWarp.HomographyWarp var1) {
      double[] var2 = var0.getBoundsPixels();
      double[] var3 = var1.bounds(var2[0], var2[1], var2[2], var2[3]);
      if (var3[0] == -10000.0) {
         return 0.0;
      }

      double var4 = getMc().gameRenderer.mainCamera().getFov();
      double var6 = ((Integer)getMc().options.fov().get()).intValue();
      double var8 = var4 > 1.0 && var4 < 179.0 && var6 > 1.0 && var6 < 179.0
         ? Math.tan(Math.toRadians(var4) / 2.0) / Math.tan(Math.toRadians(var6) / 2.0)
         : 1.0;
      return (var3[3] - var3[1]) / Math.max(1, var0.height) * var8;
   }

   private double removePanel() {
      return this.placement.isMode("Pinned") ? Math.clamp(this.closeDistance.getValue() - 1.0, 0.5, 16.0) : 16.0;
   }

   public static boolean claimScreen(ClickGuiScreen var0) {
      if (instance != null && instance.isEnabled()) {
         WorldMenuModule var1 = instance;
         return var1 != null && var1.openScreen(var0);
      } else {
         return false;
      }
   }

   private boolean openScreen(ClickGuiScreen var1) {
      if (!this.isAvailable()) {
         return false;
      }

      if (var1 == this.activeScreen) {
         return true;
      }

      if (var1.renderCloseEffect() != null) {
         return false;
      }

      if (this.activeScreen != null) {
         this.removePanel("a new window replaced it");
      }

      this.closingScreen = null;
      this.activeScreen = var1;
      this.isMenuScreen();
      Window var2 = getMc().getWindow();
      var1.init(var2.getGuiScaledWidth(), var2.getGuiScaledHeight());
      this.getPanelDistance(var1);
      return true;
   }

   private void getPanelDistance(ClickGuiScreen var1) {
      Camera var2 = getMc().gameRenderer.mainCamera();
      Vec3 var3 = var2.position();
      CameraMath.CameraPose var4 = CameraMath.offsetAlongView(var3.x, var3.y, var3.z, var2.yRot(), var2.xRot(), 1.0);
      double var5 = getPanelDistance(var1, this.computeWarp(var1, var4));
      double var7 = var5 > 0.0 ? Math.clamp(var5 / (this.menuHeight.getValue() / 100.0), 0.5, 16.0) : 2.0;
      this.panelPose = CameraMath.offsetAlongView(var3.x, var3.y, var3.z, var2.yRot(), var2.xRot(), var7);
      this.panelLevel = getMc().level;
      this.lastCameraPos = var3;
      this.visibility.snapTo(1.0);
      this.targetVisibility = 1.0;
      this.lastVisibilityCheck = 0L;
      LarpLog.debug(
         "GUI",
         "World Menu opened in the world, {} blocks ahead, yaw {}, pitch {}, {}",
         formatDistance(var7),
         Math.round(this.panelPose.yaw()),
         Math.round(this.panelPose.pitch()),
         this.placement.isMode("Pinned") ? "closes beyond " + this.closeDistance.getValue().intValue() + " blocks" : "following"
      );
   }

   private double getPanelDistance() {
      if (this.panelPose == null) {
         return 0.0;
      }

      Vec3 var1 = getMc().gameRenderer.mainCamera().position();
      return this.panelPose.distance(var1.x, var1.y, var1.z);
   }

   private static String formatDistance(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   public static boolean hasOpenMenu() {
      if (instance != null && instance.isEnabled()) {
         WorldMenuModule var0 = instance;
         return var0 != null && var0.activeScreen != null && var0.isAvailable();
      } else {
         return false;
      }
   }

   public static boolean closeWithKey() {
      if (instance != null && instance.isEnabled()) {
         WorldMenuModule var0 = instance;
         if (var0 != null && var0.activeScreen != null && var0.isAvailable()) {
            LarpLog.debug("GUI", "World Menu closed with the menu key");
            var0.activeScreen.onClose();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean releaseScreen(ClickGuiScreen var0) {
      if (instance != null && instance.isEnabled()) {
         WorldMenuModule var1 = instance;
         return var1 != null && var1.onMouseRelease(var0);
      } else {
         return false;
      }
   }

   private boolean onMouseRelease(ClickGuiScreen var1) {
      if (var1 != null && var1 == this.activeScreen) {
         this.activeScreen = null;
         this.closingScreen = var1;
         this.isMenuScreen();
         var1.removed();
         LarpLog.debug("GUI", "World Menu closing {} blocks away", formatDistance(this.getPanelDistance()));
         return true;
      } else {
         return false;
      }
   }

   private void removePanel(String var1) {
      ClickGuiScreen var2 = this.activeScreen;
      this.activeScreen = this.closingScreen = null;
      this.isMenuScreen();
      this.panelPose = null;
      this.panelLevel = null;
      this.lastCameraPos = null;
      if (var2 != null) {
         LarpLog.debug("GUI", "World Menu removed its panel ({})", var1);

         try {
            var2.removed();
         } catch (RuntimeException var4) {
            LarpLog.warn("GUI", "World Menu window cleanup failed after {}: {}", var1, var4.toString());
         }
      }
   }

   private void isMenuScreen() {
      this.heldButtons = 0;
      this.grabOffset = null;
      this.cursorOnPanel = (boolean)0;
   }

   @Override
   public void onTick() {
      ClickGuiScreen var1 = this.activeScreen;
      if (var1 != null) {
         int var10000 = this.isAvailable();
         boolean var9 = false;
         byte var8 = (byte)var10000;
         var10000 = Integer.signum(var8);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.panelLevel == getMc().level) {
            var1.tick();
            if (this.activeScreen == var1) {
               int var11 = this.placement.isMode("Pinned");
               boolean var7 = false;
               byte var6 = (byte)var11;
               var11 = Integer.signum(var6);
               if (((var11 | -var11) >>> 31 ^ 1) == 0 && this.grabOffset == null) {
                  double var2 = this.getPanelDistance();
                  double var14;
                  var11 = (var14 = var2 - this.closeDistance.getValue()) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
                  boolean var5 = false;
                  int var4 = var11;
                  if (-Integer.signum(var4) >>> 31 == 0) {
                     return;
                  }

                  LarpLog.debug(
                     "GUI",
                     "World Menu closed: you are {} blocks from it, past Close Distance {}",
                     formatDistance(var2),
                     this.closeDistance.getValue().intValue()
                  );
                  var1.onClose();
                  return;
               }
            }
         } else {
            this.removePanel("left the level");
         }
      }
   }

   public static void renderPanel(GuiGraphicsExtractor var0, float var1) {
      if (instance != null && instance.isEnabled()) {
         WorldMenuModule var2 = instance;
         if (var2 != null) {
            var2.extractPanel(var0, var1);
         }
      }
   }

   private void extractPanel(GuiGraphicsExtractor var1, float var2) {
      this.cursorOnPanel = (boolean)0;
      ClickGuiScreen var3 = this.activeScreen != null ? this.activeScreen : this.closingScreen;
      if (var3 != null) {
         Minecraft var4 = getMc();
         if (!this.isAvailable() || this.panelPose == null || this.panelLevel != var4.level) {
            this.removePanel(this.isAvailable() ? "left the level" : "unavailable");
         } else if (var3 == this.closingScreen && var3.openHudSettings()) {
            this.closingScreen = null;
         } else {
            if (var3.width != var1.guiWidth() || var3.height != var1.guiHeight()) {
               if (var3 != this.activeScreen) {
                  this.closingScreen = null;
                  return;
               }

               var3.init(var1.guiWidth(), var1.guiHeight());
               LarpLog.debug("GUI", "World Menu window laid out again at {}x{}", var3.width, var3.height);
            }

            if (!var4.gameRenderer.gameRenderState().guiRenderState.isHudHidden && !(var4.gui.screen() instanceof HudEditorScreen)) {
               Vec3 var5 = var4.gameRenderer.mainCamera().position();
               if (this.placement.isMode("Follow") && this.lastCameraPos != null && this.grabOffset == null) {
                  this.panelPose = new CameraMath.CameraPose(
                     this.panelPose.x2() + var5.x - this.lastCameraPos.x,
                     this.panelPose.y2() + var5.y - this.lastCameraPos.y,
                     this.panelPose.z2() + var5.z - this.lastCameraPos.z,
                     this.panelPose.yaw(),
                     this.panelPose.pitch()
                  );
               }

               this.lastCameraPos = var5;
               if (this.grabOffset != null) {
                  this.updateGrabbedPose(var3);
               }

               if (!this.panelPose.faces(var5.x, var5.y, var5.z)) {
                  this.visibility.snapTo(0.0);
               } else {
                  ScreenWarp.HomographyWarp var6 = this.computeWarp(var3, this.panelPose);
                  double[] var7 = var3.getBoundsPixels();
                  double[] var8 = var6.bounds(0.0, 0.0, var3.width, Math.max(var3.height, var7[3] + 24.0));
                  if (!(var8[2] < 0.0) && !(var8[3] < 0.0) && !(var8[0] > var3.width) && !(var8[1] > var3.height)) {
                     long var9 = System.nanoTime();
                     if (var9 - this.lastVisibilityCheck >= 50000000L) {
                        this.lastVisibilityCheck = var9;
                        this.targetVisibility = this.computeVisibility(var3, var5);
                     }

                     double var11 = this.visibility.animateTo(this.targetVisibility, 160);
                     if (!(var11 < 0.01)) {
                        double[] var13 = var6.inverse(var3.width / 2.0, var3.height / 2.0);
                        int var14 = var3 == this.activeScreen && var13[0] != -10000.0 ? 1 : 0;
                        this.cursorOnPanel = (boolean)(var14 != 0
                              && var11 > 0.5
                              && this.isWorldFocused()
                              && var3.isPointInside(var13[0], var13[1])
                              && !this.isOccluded(var3, var13, var5)
                           ? 1
                           : 0);
                        if (this.cursorOnPanel) {
                           this.cursorX = var13[0];
                           this.cursorY = var13[1];
                        }

                        if (var14 != 0 && this.heldButtons != 0 && this.grabOffset == null && (var13[0] != this.lastDragX || var13[1] != this.lastDragY)) {
                           MouseButtonInfo var15 = new MouseButtonInfo(Integer.numberOfTrailingZeros(this.heldButtons), 0);
                           var3.mouseDragged(new MouseButtonEvent(var13[0], var13[1], var15), var13[0] - this.lastDragX, var13[1] - this.lastDragY);
                           this.lastDragX = var13[0];
                           this.lastDragY = var13[1];
                        }

                        int var18 = !this.cursorOnPanel && (var14 == 0 || this.heldButtons == 0) ? 0 : 1;
                        int var16 = var18 != 0 ? (int)Math.floor(var13[0]) : -10000;
                        int var17 = var18 != 0 ? (int)Math.floor(var13[1]) : -10000;
                        var1.nextStratum();
                        ScreenWarp.warpGraphics(var1, var6, false, () -> {
                           var1.nextStratum();

                           try (AlphaScope var7x = AlphaScope.push(var11)) {
                              var3.extractRenderState(var1, var16, var17, var2);
                           }
                        });
                        var1.nextStratum();
                     }
                  } else {
                     this.visibility.snapTo(0.0);
                  }
               }
            }
         }
      }
   }

   private boolean isOccluded(ClickGuiScreen var1, double[] var2, Vec3 var3) {
      HitResult var4 = getMc().hitResult;
      if (var4 != null && var4.getType() != Type.MISS) {
         double var5 = this.getPanelScale(var1);
         double[] var7 = this.panelPose.point((var2[0] - var1.width / 2.0) * var5, (var1.height / 2.0 - var2[1]) * var5);
         return var4.getLocation().distanceTo(var3) < new Vec3(var7[0], var7[1], var7[2]).distanceTo(var3);
      } else {
         return false;
      }
   }

   private void updateGrabbedPose(ClickGuiScreen var1) {
      Camera var2 = getMc().gameRenderer.mainCamera();
      double[] var3 = CameraMath.screenToRay(var2.getViewRotationProjectionMatrix(new Matrix4f()), var1.width / 2.0, var1.height / 2.0, var1.width, var1.height);
      if (var3 != null) {
         Vec3 var4 = var2.position();
         this.panelPose = CameraMath.solveCamera(var4.x, var4.y, var4.z, var3, this.grabOffset.distance(), this.grabOffset.right(), this.grabOffset.upOffset());
      }
   }

   private double computeVisibility(ClickGuiScreen var1, Vec3 var2) {
      Minecraft var3 = getMc();
      double[] var4 = var1.getBoundsPixels();
      double var5 = this.getPanelScale(var1);
      double var7 = var1.width / 2.0;
      double var9 = var1.height / 2.0;
      double var11 = (var4[0] + var4[2]) / 2.0;
      double var13 = (var4[1] + var4[3]) / 2.0;
      double var15 = (var4[2] - var4[0]) * 0.4;
      double var17 = (var4[3] - var4[1]) * 0.4;
      double[][] var19 = new double[][]{
         {var11, var13}, {var11 - var15, var13 - var17}, {var11 + var15, var13 - var17}, {var11 - var15, var13 + var17}, {var11 + var15, var13 + var17}
      };
      int var20 = 0;

      for (double[] var24 : var19) {
         double[] var25 = this.panelPose.point((var24[0] - var7) * var5, (var9 - var24[1]) * var5);
         Vec3 var26 = new Vec3(var25[0], var25[1], var25[2]);
         BlockHitResult var27 = var3.level.clip(new ClipContext(var2, var26, Block.VISUAL, Fluid.NONE, var3.player));
         if (var27.getType() == Type.MISS || var27.getLocation().distanceTo(var2) >= var26.distanceTo(var2) - 0.05) {
            var20++;
         }
      }

      return (double)var20 / var19.length;
   }

   public static boolean onMouseRelease(MouseButtonInfo var0, int var1) {
      if (instance != null && instance.isEnabled()) {
         WorldMenuModule var2 = instance;
         return var2 != null && var2.handleMouseButton(var0, var1);
      } else {
         return false;
      }
   }

   private boolean handleMouseButton(MouseButtonInfo var1, int var2) {
      ClickGuiScreen var3 = this.activeScreen;
      int var4 = 1 << Math.clamp(KeyCodes.mouseButtonOfInfo(var1), 0, 30);
      if (var2 == 0) {
         if ((this.heldButtons & var4) == 0) {
            return false;
         }

         this.heldButtons &= ~var4;
         if (KeyCodes.mouseButtonOfInfo(var1) == 0) {
            this.releaseGrab();
         }

         if (var3 != null) {
            var3.mouseReleased(new MouseButtonEvent(this.lastDragX, this.lastDragY, var1));
         }

         return getMc().gui.screen() == null;
      } else {
         if (var2 != 1 || var3 == null || !this.isAvailable() || !this.isWorldFocused()) {
            return false;
         }

         if (!this.cursorOnPanel) {
            if (var3.finishInspectorClose()) {
               var3.clearFocus();
               LarpLog.debug("GUI", "World Menu typing ended by a click in the world");
            }

            return false;
         } else {
            long var5 = Util.getMillis();
            int var7 = KeyCodes.mouseButtonOfInfo(var1) == this.lastClickButton && var5 - this.lastClickTime < 250L ? 1 : 0;
            this.lastClickTime = var5;
            this.lastClickButton = KeyCodes.mouseButtonOfInfo(var1);
            this.heldButtons |= var4;
            this.lastDragX = this.cursorX;
            this.lastDragY = this.cursorY;
            var3.mouseClicked(new MouseButtonEvent(this.cursorX, this.cursorY, var1), (boolean)var7);
            return true;
         }
      }
   }

   public static boolean getPanelDistance() {
      if (instance != null && instance.isEnabled()) {
         WorldMenuModule var0 = instance;
         return var0 != null && var0.activeScreen != null && (var0.cursorOnPanel || var0.heldButtons != 0) && var0.isWorldFocused();
      } else {
         return false;
      }
   }

   public static boolean onMouseRelease(double var0) {
      if (instance != null && instance.isEnabled()) {
         WorldMenuModule var2 = instance;
         return var2 != null && var2.scrollPanel(var0);
      } else {
         return false;
      }
   }

   private boolean scrollPanel(double var1) {
      ClickGuiScreen var3 = this.activeScreen;
      if (var3 == null || !this.isAvailable() || !this.isWorldFocused()) {
         return false;
      }

      if (this.grabOffset != null) {
         this.grabOffset = new WorldMenuModule.CameraOffset(
            Math.clamp(this.grabOffset.distance() * Math.pow(1.12, var1), 0.5, this.removePanel()), this.grabOffset.right(), this.grabOffset.upOffset()
         );
         return true;
      }

      if (!this.cursorOnPanel) {
         return false;
      }

      var3.mouseScrolled(this.cursorX, this.cursorY, 0.0, var1);
      return true;
   }

   private boolean extractPanel(ClickGuiScreen var1) {
      return var1 != null && var1 == this.activeScreen && this.isAvailable() && this.isWorldFocused() && var1.finishInspectorClose();
   }

   public static boolean isOccluded() {
      if (instance != null && instance.isEnabled()) {
         WorldMenuModule var0 = instance;
         return var0 != null && var0.extractPanel(var0.activeScreen);
      } else {
         return false;
      }
   }

   public static boolean isOverHandle(int var0, KeyEvent var1) {
      if (instance != null && instance.isEnabled()) {
         WorldMenuModule var2 = instance;
         if (var2 != null && var0 != 0 && var2.extractPanel(var2.activeScreen)) {
            if (KeyCodes.keyOf(var1) == 256) {
               var2.activeScreen.cancelTextInput();
               LarpLog.debug("GUI", "World Menu typing ended with Escape");
            } else {
               var2.activeScreen.keyPressed(var1);
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean onCharTyped(CharacterEvent var0) {
      if (instance != null && instance.isEnabled()) {
         WorldMenuModule var1 = instance;
         if (var1 != null && var1.extractPanel(var1.activeScreen)) {
            var1.activeScreen.charTyped(var0);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean isMenuScreen(ClickGuiScreen var0) {
      if (instance != null && instance.isEnabled()) {
         WorldMenuModule var1 = instance;
         return var1 != null && var0 != null && (var0 == var1.activeScreen || var0 == var1.closingScreen) && var1.isAvailable();
      } else {
         return false;
      }
   }

   private static int[] getHandleRect(GuiLayout var0) {
      return new int[]{var0.left() + (var0.width() - 76) / 2, var0.top() + var0.height() + 9, 76, 6};
   }

   private static boolean isOverHandle(GuiLayout var0, double var1, double var3) {
      int[] var5 = getHandleRect(var0);
      return var1 >= var5[0] - 16 && var1 < var5[0] + var5[2] + 16 && var3 >= var5[1] - 9 && var3 < var5[1] + var5[3] + 9;
   }

   private static int[] getCloseButtonRect(GuiLayout var0) {
      int[] var1 = getHandleRect(var0);
      return new int[]{var1[0] + var1[2] + 22, var1[1] + var1[3] / 2 - 8, 16, 16};
   }

   private static boolean isOverCloseButton(GuiLayout var0, double var1, double var3) {
      int[] var5 = getCloseButtonRect(var0);
      return var1 >= var5[0] - 4 && var1 < var5[0] + var5[2] + 4 && var3 >= var5[1] - 4 && var3 < var5[1] + var5[3] + 4;
   }

   public static boolean isOverControls(GuiLayout var0, double var1, double var3) {
      return isOverHandle(var0, var1, var3) || isOverCloseButton(var0, var1, var3);
   }

   public static void removePanel(GuiGraphicsExtractor var0, ClickGuiScreen var1, GuiLayout var2, int var3, int var4) {
      if (instance != null && instance.isEnabled()) {
         WorldMenuModule var5 = instance;
         if (var5 != null && isMenuScreen(var1)) {
            var5.renderHandle(var0, var1, var2, var3, var4);
         }
      }
   }

   private void renderHandle(GuiGraphicsExtractor var1, ClickGuiScreen var2, GuiLayout var3, int var4, int var5) {
      int var6 = var2 == this.activeScreen ? 1 : 0;
      int var7 = var6 != 0 && this.grabOffset != null ? 1 : 0;
      int var8 = var7 != 0 || var6 != 0 && isOverHandle(var3, var4, var5) ? 1 : 0;
      double var9 = this.handleHover.animateTo(var8 != 0 ? 1.0 : 0.0, 180);
      int[] var11 = getHandleRect(var3);
      int var12 = var11[2] + (int)Math.round(18.0 * var9);
      int var13 = var11[0] - (var12 - var11[2]) / 2;
      int var14 = var7 != 0 ? Theme.readableAccent(var13, Theme.background) : ColorUtil.withAlpha(Theme.textPrimary, 0.34 + 0.42 * var9);
      DrawUtil.fillRoundedRect(var1, var13, var11[1], var12, var11[3], var11[3] / 2, var14);
      int[] var15 = getCloseButtonRect(var3);
      int var16 = var6 != 0 && this.grabOffset == null && isOverCloseButton(var3, var4, var5) ? 1 : 0;
      DrawUtil.fillRoundedRect(var1, var15[0], var15[1], var15[2], var15[3], var15[2] / 2, var16 != 0 ? -1750963 : ColorUtil.withAlpha(Theme.textPrimary, 0.16));
      Icons.texture(var1, Icons.get("x"), var15[0] + 3, var15[1] + 3, var15[2] - 6, var16 != 0 ? -1 : ColorUtil.withAlpha(Theme.textPrimary, 0.8));
   }

   public static boolean isAvailable(ClickGuiScreen var0, double var1, double var3, int var5, GuiLayout var6) {
      if (instance != null && instance.isEnabled()) {
         WorldMenuModule var7 = instance;
         return var7 != null && var7.getHandleRect(var0, var1, var3, var5, var6);
      } else {
         return false;
      }
   }

   private boolean getHandleRect(ClickGuiScreen var1, double var2, double var4, int var6, GuiLayout var7) {
      if (var6 != 0 || var1 != this.activeScreen || !this.isAvailable()) {
         return false;
      } else if (isOverCloseButton(var7, var2, var4)) {
         LarpLog.debug("GUI", "World Menu closed with its X");
         var1.onClose();
         return true;
      } else if (isOverHandle(var7, var2, var4) && this.cursorOnPanel) {
         double var8 = this.getPanelScale(var1);
         double var10 = (this.cursorX - var1.width / 2.0) * var8;
         double var12 = (var1.height / 2.0 - this.cursorY) * var8;
         double[] var14 = this.panelPose.point(var10, var12);
         Vec3 var15 = getMc().gameRenderer.mainCamera().position();
         double var16 = new Vec3(var14[0], var14[1], var14[2]).distanceTo(var15);
         this.grabOffset = new WorldMenuModule.CameraOffset(Math.clamp(var16, 0.5, this.removePanel()), var10, var12);
         LarpLog.debug("GUI", "World Menu grabbed {} blocks away", formatDistance(var16));
         return true;
      } else {
         return false;
      }
   }

   private void releaseGrab() {
      if (this.grabOffset != null) {
         this.grabOffset = null;
         LarpLog.debug(
            "GUI",
            "World Menu set down {} blocks away, yaw {}, pitch {}",
            formatDistance(this.getPanelDistance()),
            this.panelPose == null ? 0L : Math.round(this.panelPose.yaw()),
            this.panelPose == null ? 0L : Math.round(this.panelPose.pitch())
         );
      }
   }

   @Override
   public void onEnable() {
      LarpLog.debug(
         "GUI",
         "World Menu active: placement={}, close distance={}, height={}%, size={}",
         this.placement.getValue(),
         this.closeDistance.getValue().intValue(),
         this.menuHeight.getValue().intValue(),
         this.panelSize.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.removePanel("turned off");
      this.visibility.snapTo(0.0);
   }

   private record CameraOffset(double distance, double right, double up) {
      private final double upOffset;

      private CameraOffset(double distance, double right, double up) {
         this.distance = distance;
         this.right = right;
         this.upOffset = up;
      }
   }
}

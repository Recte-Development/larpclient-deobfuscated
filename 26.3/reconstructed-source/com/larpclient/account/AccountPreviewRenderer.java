package com.larpclient.account;

import com.larpclient.gui.misc.PlayerPreviewAnimator;
import com.larpclient.gui.screen.HomeLayout;
import com.larpclient.gui.screen.MainMenuLayout;
import com.larpclient.gui.screen.MenuIntroAnimation;
import com.larpclient.gui.theme.MenuBackground;
import com.larpclient.module.render.WingsModule;
import com.larpclient.render.entity.KorbloxLegRenderer;
import com.larpclient.render.entity.LegTrail;
import com.larpclient.render.model.AvatarPoseModel;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.social.cosmetic.AccountCosmeticsRenderer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.SdlWindowShim;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerCapeModel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;

public final class AccountPreviewRenderer {
   static final double mIN_CLEARANCE = 1.45;
   private static final double mIN_IMPACT_SOUND = 4.5;
   private static final double sPEED_FACTOR = 0.09;
   private final PlayerPreviewAnimator animator;
   private final AccountCosmeticsRenderer outfit = new AccountCosmeticsRenderer();
   private AvatarPoseModel wideModel;
   private AvatarPoseModel slimModel;
   private PlayerCapeModel capeModel;
   private int bakedGeneration = -1;
   private int failedGeneration = -1;
   private PlayerPreviewAnimator.Stage lastStage;
   private double nameOpacity;
   private PlayerPreviewAnimator.PreviewFrame toyFrame;
   private PlayerPreviewAnimator.PreviewLayout toyLayout;
   private AvatarPoseModel toyModel;
   private Identifier skinTexture;
   private double toyExtent;
   private long lastImpactSoundNanos;

   public AccountPreviewRenderer(PlayerPreviewAnimator var1) {
      this.animator = var1;
   }

   public static PlayerPreviewAnimator.PreviewLayout ensureModels(MainMenuLayout var0) {
      return PlayerPreviewAnimator.computeLayoutWithExtra(
         var0, AccountCosmeticsRenderer.maxHatRise(AccountCosmeticsRenderer.activeHats(), AccountCosmeticsRenderer.activeWings())
      );
   }

   public void render(GuiGraphicsExtractor var1, MainMenuLayout var2, MenuIntroAnimation var3, AccountSkin var4, int var5, int var6, boolean var7, boolean var8) {
      Minecraft var9 = Minecraft.getInstance();
      this.toyFrame = null;
      if (this.animator.isGrabbing() && SdlWindowShim.getMouseButtonState(var9.getWindow().handle(), 0) != 1) {
         this.animator.resetToy();
      }

      List var10 = AccountCosmeticsRenderer.activeHats();
      WingsModule.WingStyle var11 = AccountCosmeticsRenderer.activeWings();
      PlayerPreviewAnimator.PreviewLayout var12 = PlayerPreviewAnimator.computeLayoutWithExtra(var2, AccountCosmeticsRenderer.maxHatRise(var10, var11));
      int var13 = var12.visible() && var9.gui.overlay() == null && var9.isWindowActive() ? 1 : 0;
      long var14 = System.nanoTime();
      PlayerPreviewAnimator.PreviewFrame var16 = this.animator.update(var14, (boolean)var13, var7, var8, var3.isDocked(), var12, var2.width(), var5, var6);
      this.nameOpacity = var16.nameOpacity();
      this.logStage(var16);
      if (var12.visible() && var16.stage() != PlayerPreviewAnimator.Stage.HIDDEN) {
         PlayerPreviewAnimator.ToyFrame var17 = var16.toy();
         if (var17 != null) {
            this.playImpactSound(var9, var17, var14);
         }

         HomeLayout.Rect var18 = var12.model();
         int var19 = Math.round(var12.centerX() + (float)var16.xOffset());
         double var20 = AccountCosmeticsRenderer.maxHatRadius(var10, var11);
         int var22 = Math.max(var12.renderWidth(), (int)Math.ceil(2.0 * var20 * var12.scale()));
         int var23 = var19 - var22 / 2;
         int var24 = PlayerPreviewAnimator.clipTop(var12, AccountCosmeticsRenderer.maxHatRise(var10, var11));
         if (var17 == null) {
            var1.enableScissor(var2.navigation(0).left() + var2.menuWidth() + 8, var24, var2.width(), var18.top() + var18.height() + 8);
         }

         try {
            if (var17 == null) {
               drawShadow(var1, var19, var12.floorY(), Math.min(var18.width() * 0.3F, var12.scale() * 0.62F), 1.0);
            }

            if (!this.ensureModels(var9)) {
               if (var17 == null) {
                  int var31 = Math.min(48, var18.width() / 2);
                  var4.drawFace(var1, var19 - var31 / 2, var18.top() + var18.height() / 3, var31);
               }

               return;
            }

            PlayerSkin var25 = AccountCosmeticsRenderer.applySkinCosmetics(var9.getGameProfile().id(), var4.getSkin());
            AvatarPoseModel var26 = var25.model() == PlayerModelType.SLIM ? this.slimModel : this.wideModel;
            var26.applyBlendedPose(var16.pose(), var17 == null ? null : var17.limbs());
            LegTrail var27 = AccountCosmeticsRenderer.activeKorblox();
            if (var27 != null && !KorbloxLegRenderer.ensureTexture()) {
               var27 = null;
            }

            var26.setAppearance(
               this.outfit
                  .buildCosmeticsFull(
                     AccountCosmeticsRenderer.capeTexture(var25), var10, var11, AccountCosmeticsRenderer.hidesHead(), var27, var16.pose(), this.capeModel
                  )
            );
            if (var17 == null) {
               var26.setRootTransform(0.0F, 0.0F, 0.0F);
               var1.skin(
                  var26,
                  var25.body().texturePath(),
                  var12.scale(),
                  0.0F,
                  var16.pose().bodyYaw(),
                  -1.0625F,
                  var23,
                  var24,
                  var23 + var22,
                  var18.top() + var18.height()
               );
               return;
            }

            this.toyFrame = var16;
            this.toyLayout = var12;
            this.toyModel = var26;
            this.skinTexture = var25.body().texturePath();
            this.toyExtent = Math.max(1.45, Math.max(0.85 + AccountCosmeticsRenderer.maxHatRise(var10, var11) + 0.1, Math.hypot(var20, 0.6) + 0.05));
         } finally {
            if (var17 == null) {
               var1.disableScissor();
            }
         }
      }
   }

   public void ensureModels(GuiGraphicsExtractor var1) {
      if (this.toyFrame != null) {
         PlayerPreviewAnimator.ToyFrame var2 = this.toyFrame.toy();
         PlayerPreviewAnimator.PreviewLayout var3 = this.toyLayout;
         float var4 = var3.scale();
         var1.nextStratum();
         double var5 = Math.abs(Math.sin(var2.roll()));
         double var7 = 1.0 / (1.0 + var2.lift() * 0.9);
         drawShadow(
            var1,
            (float)var2.x2(),
            var3.floorY(),
            (float)(Math.min(var3.model().width() * 0.3F, var4 * 0.62F) * (1.0 + 0.9 * var5) / (1.0 + var2.lift() * 0.5)),
            var7
         );
         double var9 = this.toyFrame.pose().ageInTicks() / 20.0;
         renderDizzyStars(var1, var2, var4, false, var9);
         PlayerPreviewAnimator.ToyBounds var11 = PlayerPreviewAnimator.ToyBounds.fromToy(var2, var4, this.toyExtent);
         this.toyModel.setRootTransform(var2.roll(), var11.shiftX(), var11.shiftY());
         var1.skin(
            this.toyModel,
            this.skinTexture,
            var4,
            0.0F,
            this.toyFrame.pose().bodyYaw(),
            -1.0625F,
            var11.left(),
            var11.top(),
            var11.left() + var11.side(),
            var11.top() + var11.side()
         );
         var1.nextStratum();
         renderDizzyStars(var1, var2, var4, true, var9);
      }
   }

   public double getNameOpacity() {
      return this.nameOpacity;
   }

   public boolean containsPoint(double var1, double var3) {
      return this.animator.canGrab(var1, var3);
   }

   public boolean isDragging() {
      return this.animator.isGrabbing();
   }

   public void drag(double var1, double var3) {
      this.animator.computeLayout(var1, var3);
   }

   public boolean ensureModels(double var1, double var3) {
      if (!this.animator.startGrab(var1, var3)) {
         return false;
      }

      this.animator.computeLayout(var1, var3);
      return true;
   }

   public boolean reset() {
      return this.animator.resetToy();
   }

   private void logStage(PlayerPreviewAnimator.PreviewFrame var1) {
      PlayerPreviewAnimator.Stage var2 = var1.stage();
      if (var2 != this.lastStage) {
         PlayerPreviewAnimator.ToyFrame var3 = var1.toy();
         switch (var2) {
            case DROPPING:
               if (this.lastStage == PlayerPreviewAnimator.Stage.HIDDEN) {
                  LarpLog.info("Menu", "Account walk-on started after menu reveal; native left-facing stride");
               }
               break;
            case IDLE:
               if (this.lastStage == PlayerPreviewAnimator.Stage.SETTLING) {
                  LarpLog.info("Menu", "Account walk-on settled; native idle and pointer tracking active");
               } else if (this.lastStage == PlayerPreviewAnimator.Stage.STANDING) {
                  LarpLog.info("Menu", "Account model back home; account switcher restored");
               } else if (this.lastStage != null && this.lastStage.ordinal() > PlayerPreviewAnimator.Stage.IDLE.ordinal()) {
                  LarpLog.info("Menu", "Account model reset to its resting composition from {}", this.lastStage);
               }
               break;
            case HELD:
               LarpLog.info("Menu", "Account model picked up from {}; account switcher hidden", this.lastStage);
               break;
            case THROWN:
               LarpLog.info(
                  "Menu",
                  "Account model thrown at {} blocks/s, spin {} rad/s",
                  String.format(Locale.ROOT, "%.1f", Math.hypot(var3.velX(), var3.velY())),
                  String.format(Locale.ROOT, "%.1f", var3.spin())
               );
               break;
            case LYING:
               LarpLog.info(
                  "Menu", "Account model knocked down at roll {} deg{}", Math.round(Math.toDegrees(var3.roll())), var3.dizzy() > 0.0F ? ", dazed" : ""
               );
               break;
            case LANDING:
               if (this.lastStage == PlayerPreviewAnimator.Stage.THROWN) {
                  LarpLog.info("Menu", "Account model landed on its feet");
               }
               break;
            case WALKING:
               LarpLog.info("Menu", "Account model running home from {} px away", Math.round(Math.abs(var1.xOffset())));
         }

         this.lastStage = var2;
         LarpLog.debug("Menu", "Account preview: {}", this.lastStage);
      }
   }

   private void playImpactSound(Minecraft var1, PlayerPreviewAnimator.ToyFrame var2, long var3) {
      if (!(var2.impact() < 4.5) && !(var3 - this.lastImpactSoundNanos < 9.0E7)) {
         this.lastImpactSoundNanos = var3;
         SoundEvent var5 = var2.impact() >= 12.0F ? SoundEvents.PLAYER_BIG_FALL : SoundEvents.PLAYER_SMALL_FALL;
         var1.getSoundManager().play(SimpleSoundInstance.forUI(var5, 1.0F, (float)Math.clamp(var2.impact() / 16.0F, 0.18, 0.7)));
      }
   }

   private static void renderDizzyStars(GuiGraphicsExtractor var0, PlayerPreviewAnimator.ToyFrame var1, float var2, boolean var3, double var4) {
      if (!(var1.dizzy() <= 0.0F)) {
         double var6 = Math.cos(var1.roll());
         double var8 = Math.sin(var1.roll());

         for (int var10 = 0; var10 < 3; var10++) {
            double var11 = var4 * 4.2 + var10 * Math.PI * 2.0 / 3.0;
            double var13 = Math.sin(var11);
            if ((var13 > 0.0 ? 1 : 0) == var3) {
               double var15 = Math.cos(var11) * 0.42;
               double var17 = -0.99 + var13 * 0.08;
               float var19 = (float)(var1.x2() + (var15 * var6 - var17 * var8) * var2);
               float var20 = (float)(var1.y2() + (var15 * var8 + var17 * var6) * var2);
               float var21 = (float)(var2 * (0.13 + 0.03 * var13) / 64.0);
               var0.pose().pushMatrix();

               try {
                  var0.pose().translate(var19, var20);
                  var0.pose().rotate((float)(var4 * 3.0 + var10));
                  var0.pose().scale(var21, var21);
                  var0.blit(
                     RenderPipelines.GUI_TEXTURED,
                     MenuBackground.resolveLogo(),
                     -32,
                     -32,
                     0.0F,
                     0.0F,
                     64,
                     64,
                     64,
                     64,
                     64,
                     64,
                     ColorUtil.withAlpha(-855310, var1.dizzy() * (0.75 + 0.25 * var13))
                  );
               } finally {
                  var0.pose().popMatrix();
               }
            }
         }
      }
   }

   private boolean ensureModels(Minecraft var1) {
      int var2 = PostProcessing.reloadCount;
      if (this.failedGeneration == var2) {
         return false;
      }

      if (this.bakedGeneration == var2 && this.wideModel != null) {
         return true;
      }

      try {
         EntityModelSet var3 = var1.getEntityModels();
         this.wideModel = new AvatarPoseModel(var3.bakeLayer(ModelLayers.PLAYER));
         this.slimModel = new AvatarPoseModel(var3.bakeLayer(ModelLayers.PLAYER_SLIM));
         this.capeModel = new PlayerCapeModel(var3.bakeLayer(ModelLayers.PLAYER_CAPE));
         this.bakedGeneration = var2;
         LarpLog.info("Menu", "Account skin preview ready; native wide/slim models and cape, resource generation {}", var2);
         return true;
      } catch (RuntimeException var4) {
         this.wideModel = this.slimModel = null;
         this.capeModel = null;
         this.failedGeneration = var2;
         LarpLog.error("Menu", "Account model bake failed; showing the account portrait until reopen or resource reload", var4);
         return false;
      }
   }

   private static void drawShadow(GuiGraphicsExtractor var0, float var1, int var2, float var3, double var4) {
      var0.pose().pushMatrix();

      try {
         var0.pose().translate(var1, var2);
         var0.pose().scale(1.0F, 0.13F);

         for (int var6 = 8; var6 >= 1; var6--) {
            DrawUtil.fillCircle(var0, 0, 0, Math.max(1, Math.round(var3 * var6 / 8.0F)), ColorUtil.multiplyAlpha(110663832, var4));
         }
      } finally {
         var0.pose().popMatrix();
      }
   }

   public void tick() {
      this.animator.dropStartDistance();
   }

   public void reset(boolean var1) {
      this.wideModel = this.slimModel = null;
      this.capeModel = null;
      this.toyFrame = null;
      this.toyModel = null;
      this.bakedGeneration = this.failedGeneration = -1;
      this.animator.resetToy(var1);
   }
}

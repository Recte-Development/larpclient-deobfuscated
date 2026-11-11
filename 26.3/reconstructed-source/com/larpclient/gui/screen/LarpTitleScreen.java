package com.larpclient.gui.screen;

import com.larpclient.LarpClient;
import com.larpclient.account.AccountPreviewRenderer;
import com.larpclient.account.AccountSkin;
import com.larpclient.config.ClientPreferences;
import com.larpclient.gui.clickgui.ClickGuiScreen;
import com.larpclient.gui.clickgui.LegacyClickGuiScreen;
import com.larpclient.gui.misc.MenuPopupHost;
import com.larpclient.gui.misc.PlayerPreviewAnimator;
import com.larpclient.gui.theme.Icons;
import com.larpclient.gui.theme.MenuBackground;
import com.larpclient.gui.widget.AccountSwitcherPanel;
import com.larpclient.gui.widget.AnimatedButton;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.RoundedShapes;
import com.larpclient.render.sky.AtmosphereProvider;
import com.larpclient.update.UpdateLayout;
import com.larpclient.update.UpdateRuntime;
import com.larpclient.util.misc.DisplayFormat;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.mojang.realmsclient.RealmsMainScreen;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.InputType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

public final class LarpTitleScreen extends Screen implements AtmosphereProvider {
   private static final MenuIntroAnimation.Factory iNTRO_FACTORY = new MenuIntroAnimation.Factory();
   private static final PlayerPreviewAnimator.Builder nAV_LAYOUT = new PlayerPreviewAnimator.Builder();
   private static final String tITLE = "Larp Client";
   private static final FontRenderer.Face tITLE_FONT = FontRenderer.Face.CLICK_SEMIBOLD;
   private static final float tITLE_OUTLINE = 0.45F;
   private static final float lOGO_OUTLINE = 1.1F;
   private final MenuIntroAnimation intro = iNTRO_FACTORY.create();
   private final List<AnimatedButton> navButtons = new ArrayList<>();
   private final List<LarpTitleScreen.TitleGlyph> titleGlyphs = new ArrayList<>();
   private MainMenuLayout layout;
   private AnimatedButton singleplayerButton;
   private AnimatedButton enterButton;
   private AnimatedButton accountButton;
   private AnimatedButton viewUpdateButton;
   private AnimatedButton laterButton;
   private UpdateLayout.Notice updateNotice;
   private boolean clickPressed;
   private boolean draggingPanel;
   private boolean launchLogged;
   private MenuIntroAnimation.Phase lastPhase;
   private final AccountSkin panelState = new AccountSkin();
   private final AccountPreviewRenderer panel = new AccountPreviewRenderer(nAV_LAYOUT.build());
   private final AccountSwitcherPanel accountSwitcher = new AccountSwitcherPanel(this);

   public LarpTitleScreen() {
      super(Component.literal("Larp Client"));
   }

   public static boolean isMenuEnabled() {
      LarpClient var0 = LarpClient.getInstance();
      return var0 == null || var0.getConfigManager() == null || var0.getConfigManager().getPreferences().customMenu;
   }

   protected void init() {
      this.layout = MainMenuLayout.calculate(this.width, this.height, (int)Math.ceil(FontRenderer.forceClickUiFont("Larp Client", tITLE_FONT) * 1.375F));
      this.renderLogoTrail();
      this.navButtons.clear();
      if (!this.getConfig().cinematicMenu) {
         this.intro.skipToDocked();
      }

      this.singleplayerButton = (AnimatedButton)this.addRenderableWidget(
         new AnimatedButton(
            Component.translatable("menu.singleplayer"),
            AnimatedButton.Style.PRIMARY,
            () -> this.openScreen(new SelectWorldScreen(this)),
            this.layout.navigation(0)
         )
      );
      this.addRenderableWidget(
         new AnimatedButton(
            Component.translatable("menu.multiplayer"),
            AnimatedButton.Style.PRIMARY,
            () -> this.openScreen(new JoinMultiplayerScreen(this)),
            this.layout.navigation(1)
         )
      );
      this.addRenderableWidget(
         new AnimatedButton(
            Component.translatable("menu.online"), AnimatedButton.Style.PRIMARY, () -> this.openScreen(new RealmsMainScreen(this)), this.layout.navigation(2)
         )
      );
      this.addRenderableWidget(
         new AnimatedButton(
            Component.literal("Minecraft Settings"),
            AnimatedButton.Style.SECONDARY,
            () -> this.openScreen(new OptionsScreen(this, this.minecraft.options)),
            this.layout.navigation(3)
         )
      );
      this.addRenderableWidget(
         new AnimatedButton(
            Component.literal("Client"), AnimatedButton.Style.SECONDARY, () -> this.openScreen(LegacyClickGuiScreen.create(this)), this.layout.navigation(4)
         )
      );
      this.addRenderableWidget(
         new AnimatedButton(Component.translatable("menu.quit"), AnimatedButton.Style.TERTIARY, this.minecraft::stop, this.layout.navigation(5))
      );
      PlayerPreviewAnimator.PreviewLayout var1 = AccountPreviewRenderer.ensureModels(this.layout);
      this.accountButton = (AnimatedButton)this.addRenderableWidget(
         new AnimatedButton(
            Component.literal("Switch Microsoft account: " + this.minecraft.getUser().getName()),
            AnimatedButton.Style.QUATERNARY,
            this.accountSwitcher::toggle,
            var1.visible() ? var1.name() : this.layout.account()
         )
      );
      this.accountSwitcher
         .setAnchor(new HomeLayout.Rect(this.accountButton.getX(), this.accountButton.getY(), this.accountButton.getWidth(), this.accountButton.getHeight()));
      this.accountButton.customRenderer = var1x -> {
         String var2 = FontRenderer.truncateToWidth(this.minecraft.getUser().getName(), this.accountButton.getWidth() - 32, FontRenderer.Face.SEMIBOLD);
         int var3x = FontRenderer.measureRawWidth(var2, FontRenderer.Face.SEMIBOLD);
         int var4x = this.accountButton.getX() + (this.accountButton.getWidth() - var3x - 14) / 2;
         FontRenderer.drawTextRaw(
            var1x, var2, var4x, this.accountButton.getY() + 4, ColorUtil.multiplyAlpha(-1513240, this.accountButton.fontScale()), FontRenderer.Face.SEMIBOLD
         );
         Icons.texture(
            var1x,
            Icons.get(this.accountSwitcher.isOpen() ? "chevron-up" : "chevron-down"),
            var4x + var3x + 5,
            this.accountButton.getY() + 4,
            9,
            ColorUtil.multiplyAlpha(-5592406, this.accountButton.fontScale())
         );
         double var5 = this.accountButton.getHoverProgress();
         AnimatedButton.drawUnderline(
            var1x,
            this.accountButton.getX() + this.accountButton.getWidth() / 2.0F,
            this.accountButton.getY() + 17,
            (float)(20.0 + (var3x + 14 - 20) * var5),
            ColorUtil.multiplyAlpha(ColorUtil.lerpColor(-11447983, -4342339, var5), this.accountButton.fontScale())
         );
      };

      for (GuiEventListener var3 : this.children()) {
         if (var3 instanceof AnimatedButton var4 && var4 != this.accountButton) {
            this.navButtons.add(var4);
         }
      }

      this.enterButton = (AnimatedButton)this.addRenderableWidget(
         new AnimatedButton(
            Component.literal("Press to enter"), AnimatedButton.Style.PRIMARY, this::enter, MenuIntroAnimation.buttonBounds(this.width, this.height)
         )
      );
      this.updateNotice = UpdateLayout.notice(this.width, this.height);
      this.viewUpdateButton = (AnimatedButton)this.addRenderableWidget(
         new AnimatedButton(Component.literal("View update"), AnimatedButton.Style.SECONDARY, () -> {
            UpdateRuntime.later();
            this.openScreen(ClickGuiScreen.openSettings(this, "Updates"));
         }, this.updateNotice.open())
      );
      this.laterButton = (AnimatedButton)this.addRenderableWidget(
         new AnimatedButton(Component.literal("Later"), AnimatedButton.Style.TERTIARY, UpdateRuntime::later, this.updateNotice.later())
      );
      this.syncWidgets();
      LarpLog.debug(
         "Menu", "Text navigation initialized at {}x{}; bordered labels, Realms under Multiplayer, direct Minecraft options", this.width, this.height
      );
   }

   protected void setInitialFocus() {
      if (!this.intro.isDocked()) {
         if (this.intro.isHome()) {
            this.setInitialFocus(this.enterButton);
         } else {
            this.clearFocus();
         }
      } else {
         if (this.minecraft.getLastInputType().isKeyboard()) {
            this.setInitialFocus(this.singleplayerButton);
         } else {
            this.clearFocus();
         }
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      this.tickIntro();
      MenuIntroAnimation.LogoTransform var5 = this.intro.logoTransform(this.layout, this.isAnimated());
      if (this.getConfig().softParticles && this.isAnimated()) {
         this.renderLogoTrail(var1);
      }

      var1.nextStratum();
      MenuIntroAnimation.TitleTransform var6 = this.intro.titleTransform(this.layout, this.isAnimated());
      if (var6.alpha() > 0.0) {
         this.renderTitleReveal(var1, var6);
         var1.nextStratum();
      }

      var1.pose().pushMatrix();

      try {
         var1.pose().translate((float)var5.centerX(), (float)var5.centerY());
         var1.pose().rotate((float)var5.rotation());
         float var7 = (float)(var5.size() / 64.0);
         float var8 = (float)(var5.size() / 64.0 * var5.stretch());
         var1.pose().scale(var7, var8);

         for (int var9 = 0; var9 < 16; var9++) {
            double var10 = var9 * Math.PI / 8.0;
            var1.pose().pushMatrix();

            try {
               var1.pose().translate((float)(Math.cos(var10) * 1.1F / var7), (float)(Math.sin(var10) * 1.1F / var8));
               var1.blit(RenderPipelines.GUI_TEXTURED, MenuBackground.resolveLogo(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, -16777216);
            } finally {
               var1.pose().popMatrix();
            }
         }

         var1.blit(RenderPipelines.GUI_TEXTURED, MenuBackground.resolveLogo(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, -855310);
      } finally {
         var1.pose().popMatrix();
      }

      MenuIntroAnimation.PartTransform var20 = this.intro.backgroundFade(this.isAnimated());
      if (!this.intro.isDocked() && var20.alpha() > 0.0) {
         this.renderWordmark(
            var1,
            this.width / 2.0F,
            (float)(this.height * 0.48 + Math.clamp(this.height * 0.16, 30.0, 86.0) / 2.0 + 14.0),
            Math.min(1.65F, this.layout.brandScale() + 0.15F),
            var20.alpha()
         );
      }

      this.panel.render(var1, this.layout, this.intro, this.panelState, var2, var3, this.isAnimated(), this.getConfig().cinematicMenu);
      this.updateAccountButton();
      var1.nextStratum();
      this.renderUpdateNotice(var1);
      if (!this.accountSwitcher.isOpen() && this.enter() && this.isOverLogo(var2, var3)) {
         var1.requestCursor(CursorTypes.POINTING_HAND);
      }

      int var21 = this.accountSwitcher.isOpen() || this.panel.isDragging() || !this.intro.isDocked() && !this.intro.isHome() ? 0 : 1;
      super.extractRenderState(var1, var21 != 0 ? var2 : -1, var21 != 0 ? var3 : -1, var4);
      this.panel.ensureModels(var1);
      if (this.panel.isDragging()) {
         var1.requestCursor(CursorTypes.RESIZE_ALL);
      } else if (!this.accountSwitcher.isOpen() && this.panel.containsPoint(var2, var3)) {
         var1.requestCursor(CursorTypes.POINTING_HAND);
      }

      this.accountSwitcher.render(var1, var2, var3);
   }

   private void renderUpdateNotice(GuiGraphicsExtractor var1) {
      if (this.viewUpdateButton != null && this.viewUpdateButton.visible) {
         UpdateRuntime.noticeShown();
         if (!this.updateNotice.compact()) {
            HomeLayout.Rect var2 = this.updateNotice.surface();
            RoundedShapes.fillRounded(var1, var2.left(), var2.top(), var2.width(), var2.height(), 5.0F, -451208421);
            FontRenderer.drawTextRaw(
               var1,
               FontRenderer.drawTextWithFace(UpdateRuntime.noticeTitle(), var2.width() - 16),
               var2.left() + 8,
               var2.top() + 9,
               -2171170,
               FontRenderer.Face.MEDIUM
            );
            String var3 = UpdateRuntime.noticeDetail();
            FontRenderer.drawDefaultText(var1, FontRenderer.drawTextWithFace(var3, var2.width() - 16), var2.left() + 8, var2.top() + 25, -6513508);
         }
      }
   }

   private void renderWordmark(GuiGraphicsExtractor var1, float var2, float var3, float var4, double var5) {
      float var7 = var4 * 1.375F;
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(var2 - FontRenderer.forceClickUiFont("Larp Client", tITLE_FONT) * var7 / 2.0F, var3 + 4.5F * var4 - 4.6F * var7);
         var1.pose().scale(var7, var7);
         FontRenderer.drawOutlinedText(
            var1, "Larp Client", 0, 0, ColorUtil.multiplyAlpha(-1644826, var5), ColorUtil.multiplyAlpha(-16777216, var5), 0.45F, tITLE_FONT
         );
      } finally {
         var1.pose().popMatrix();
      }
   }

   private void renderTitleReveal(GuiGraphicsExtractor var1, MenuIntroAnimation.TitleTransform var2) {
      HomeLayout.Rect var3 = this.layout.title();
      float var4 = this.layout.brandScale() * 1.375F;
      float var5 = (float)(var3.top() + var3.height() / 2.0 - 4.6F * var4 + var2.lift());
      if (!(var2.clip() <= var2.posX()) && !this.titleGlyphs.isEmpty()) {
         for (LarpTitleScreen.TitleGlyph var7 : this.titleGlyphs) {
            double var8 = var2.posX() + var7.offset() * var4;
            double var10 = Math.max(1.0F, var7.width() * var4);
            double var12 = Math.clamp((var8 + var10 - var2.clip()) / var10, 0.0, 1.0);
            double var14 = var2.alpha() * var12 * var12 * (3.0 - 2.0 * var12);
            if (!(var14 < 0.016)) {
               drawTitleText(var1, var7.text(), (float)var8, var5, var4, var14);
            }
         }
      } else {
         drawTitleText(var1, "Larp Client", (float)var2.posX(), var5, var4, var2.alpha());
      }
   }

   private static void drawTitleText(GuiGraphicsExtractor var0, String var1, float var2, float var3, float var4, double var5) {
      var0.pose().pushMatrix();

      try {
         var0.pose().translate(var2, var3);
         var0.pose().scale(var4, var4);
         FontRenderer.drawOutlinedText(var0, var1, 0, 0, ColorUtil.multiplyAlpha(-1644826, var5), ColorUtil.multiplyAlpha(-16777216, var5), 0.45F, tITLE_FONT);
      } finally {
         var0.pose().popMatrix();
      }
   }

   private void renderLogoTrail() {
      this.titleGlyphs.clear();
      float var1 = 0.0F;
      int var2 = 0;

      while (var2 < "Larp Client".length()) {
         int var3 = "Larp Client".codePointAt(var2);
         String var4 = "Larp Client".substring(var2, var2 + Character.charCount(var3));
         var2 += var4.length();
         float var5 = FontRenderer.forceClickUiFont(var4, tITLE_FONT);
         this.titleGlyphs.add(new LarpTitleScreen.TitleGlyph(var4, var1, var5));
         var1 += var5;
      }
   }

   private void renderLogoTrail(GuiGraphicsExtractor var1) {
      for (int var2 = 0; var2 < 12; var2++) {
         MenuIntroAnimation.ParticleState var3 = this.intro.particleAt(var2, this.layout);
         if (!(var3.alpha() <= 0.0)) {
            var1.pose().pushMatrix();

            try {
               var1.pose().translate((float)var3.posX(), (float)var3.posY());
               var1.pose().rotate((float)var3.rotation());
               float var4 = (float)(var3.size() / 64.0);
               var1.pose().scale(var4, var4);
               var1.blit(
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
                  ColorUtil.withAlpha(-1513240, var3.alpha())
               );
            } finally {
               var1.pose().popMatrix();
            }
         }
      }
   }

   private ClientPreferences getConfig() {
      return LarpClient.getInstance().getConfigManager().getPreferences();
   }

   private void openScreen(Screen var1) {
      if (var1 instanceof ClickGuiScreen var2) {
         var2.openConfigsWithDiscover();
      } else if (var1 instanceof MenuPopupHost var3) {
         var3.larpOpenMenuPopup();
      } else {
         LarpLog.warn("Menu", "Popup hook unavailable for {}; opening destination directly", var1.getClass().getSimpleName());
      }

      LarpLog.debug("Menu", "Opening {} with the shared {} ms popup", var1.getClass().getSimpleName(), 240);
      this.minecraft.gui.setScreen(var1);
   }

   private boolean isAnimated() {
      return this.getConfig().motion
         && this.getConfig().menuQuality != DisplayFormat.MenuQuality.OFF
         && (Double)this.minecraft.options.panoramaSpeed().get() > 0.0;
   }

   public MenuIntroAnimation tickIntro() {
      this.intro.retainKeys(var1x -> KeyCodes.isKeyDown(this.minecraft.getWindow(), var1x));
      long var1 = System.nanoTime();
      int var3 = this.minecraft.gui.overlay() == null && this.minecraft.isWindowActive() ? 1 : 0;
      this.intro.update(var1, (boolean)var3, this.isAnimated());
      this.syncWidgets();
      if (!this.launchLogged && this.minecraft.gui.overlay() == null) {
         this.launchLogged = (boolean)1;
         LarpLog.info("Menu", "Launch presentation: {}; Enter, Space or click to enter; another press skips motion", this.intro.getPhase());
      }

      return this.intro;
   }

   private void syncWidgets() {
      if (this.enterButton != null) {
         this.enterButton.visible = (boolean)(!this.intro.isDocked() && this.intro.addKey(this.isAnimated()).alpha() > 0.0 ? 1 : 0);
         this.enterButton.active = this.intro.isHome();
         this.enterButton.setTransform(this.intro.addKey(this.isAnimated()));

         for (int var1 = 0; var1 < this.navButtons.size(); var1++) {
            AnimatedButton var2 = this.navButtons.get(var1);
            MenuIntroAnimation.PartTransform var3 = this.intro.retainKeys(var1);
            var2.setTransform(var3);
            var2.visible = (boolean)(var3.alpha() > 0.0 ? 1 : 0);
            var2.active = this.intro.isDocked();
         }

         this.updateAccountButton();
         if (this.viewUpdateButton != null) {
            int var4 = this.intro.isDocked() && UpdateRuntime.notice() ? 1 : 0;
            this.viewUpdateButton.visible = this.viewUpdateButton.active = (boolean)var4;
            this.laterButton.visible = this.laterButton.active = (boolean)(var4 != 0 && !this.updateNotice.compact() ? 1 : 0);
            this.viewUpdateButton.setMessage(Component.literal(this.updateNotice.compact() ? UpdateRuntime.noticeTitle() : UpdateRuntime.noticeAction()));
         }

         if (this.lastPhase != this.intro.getPhase()) {
            MenuIntroAnimation.Phase var5 = this.lastPhase;
            this.lastPhase = this.intro.getPhase();
            this.setInitialFocus();
            if (this.intro.isDocked()) {
               LarpLog.info("Menu", "Launch reveal settled; native navigation active");
            } else if (this.intro.isHome() && var5 == MenuIntroAnimation.Phase.UNDOCKING) {
               LarpLog.info("Menu", "Entrance replay reached the welcome pose; waiting for entry");
            }
         }
      }
   }

   private void updateAccountButton() {
      if (this.accountButton != null) {
         int var1 = !this.intro.isDocked() || PlayerPreviewAnimator.computeLayout(this.layout).visible() && this.panel.getNameOpacity() != 1.0 ? 0 : 1;
         this.accountButton.visible = this.accountButton.active = (boolean)var1;
         this.accountButton.setTransform(MenuIntroAnimation.PartTransform.REST);
         this.accountButton.setMessage(Component.literal("Switch Microsoft account: " + this.minecraft.getUser().getName()));
      }
   }

   private void enter() {
      this.tickIntro();
      if (this.intro.beginDock(this.isAnimated())) {
         this.clearFocus();
         this.syncWidgets();
         LarpLog.info(
            "Menu", "Entry accepted; logo anticipation and side arc into the lockup, then the wordmark slides out from behind it while the menu staggers in"
         );
      }
   }

   private boolean isOverLogo(double var1, double var3) {
      HomeLayout.Rect var5 = this.layout.logo();
      HomeLayout.Rect var6 = this.layout.title();
      int var7 = Math.min(var5.left(), var6.left());
      int var8 = Math.min(var5.top(), var6.top());
      int var9 = Math.max(var5.left() + var5.width(), var6.left() + var6.width());
      int var10 = Math.max(var5.top() + var5.height(), var6.top() + var6.height());
      return var1 >= var7 && var1 < var9 && var3 >= var8 && var3 < var10;
   }

   private boolean enter() {
      return this.intro.isDocked() && this.getConfig().cinematicMenu;
   }

   private void replayIntro() {
      this.tickIntro();
      if (this.intro.beginUndock(this.isAnimated())) {
         this.accountSwitcher.close();
         this.clearFocus();
         this.panel.tick();
         this.syncWidgets();
         LarpLog.info("Menu", "Logo clicked; rewinding the entrance to the welcome pose ({})", this.isAnimated() ? "animated" : "reduced motion");
      }
   }

   public boolean charTyped(CharacterEvent var1) {
      return this.accountSwitcher.charTyped(var1) || super.charTyped(var1);
   }

   public boolean keyPressed(KeyEvent var1) {
      if (this.accountSwitcher.keyPressed(var1)) {
         return true;
      }

      int var2 = KeyCodes.keyOf(var1);
      if (this.intro.isHome(var2)) {
         return true;
      }

      if (!this.intro.isDocked()) {
         if (var2 == 257 || var2 == 335 || var2 == 32) {
            this.minecraft.setLastInputType(InputType.KEYBOARD_TAB);
            this.intro.addKey(var2);
            if (this.intro.isHome()) {
               this.enterButton.playDownSound(this.minecraft.getSoundManager());
               this.enter();
            } else {
               this.intro.finishTransition();
               this.syncWidgets();
            }

            return true;
         }

         if (!this.intro.isHome()) {
            this.intro.finishTransition();
            this.syncWidgets();
         }

         if (this.intro.isHome()) {
            return true;
         }
      }

      return super.keyPressed(var1);
   }

   public boolean keyReleased(KeyEvent var1) {
      return this.intro.removeKey(KeyCodes.keyOf(var1)) || super.keyReleased(var1);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.accountSwitcher.mouseClicked(var1)) {
         return true;
      }

      if (!this.intro.isDocked()) {
         if (KeyCodes.mouseButtonOf(var1) == 0) {
            this.clickPressed = (boolean)1;
            if (this.intro.isHome()) {
               this.enterButton.playDownSound(this.minecraft.getSoundManager());
               this.enter();
            } else {
               this.intro.finishTransition();
               this.syncWidgets();
            }
         }

         return true;
      } else if (KeyCodes.mouseButtonOf(var1) == 0 && this.panel.ensureModels(var1.x(), var1.y())) {
         this.draggingPanel = (boolean)1;
         this.accountSwitcher.close();
         this.clearFocus();
         return true;
      } else if (super.mouseClicked(var1, var2)) {
         return true;
      } else if (KeyCodes.mouseButtonOf(var1) == 0 && this.enter() && this.isOverLogo(var1.x(), var1.y())) {
         this.clickPressed = (boolean)1;
         this.enterButton.playDownSound(this.minecraft.getSoundManager());
         this.replayIntro();
         return true;
      } else {
         return false;
      }
   }

   public boolean mouseReleased(MouseButtonEvent var1) {
      if (KeyCodes.mouseButtonOf(var1) == 0 && this.draggingPanel) {
         this.draggingPanel = (boolean)0;
         this.panel.reset();
         return true;
      } else if (this.accountSwitcher.isOpen()) {
         return true;
      } else if (this.clickPressed && KeyCodes.mouseButtonOf(var1) == 0) {
         this.clickPressed = (boolean)0;
         return true;
      } else {
         return !this.intro.isDocked() || super.mouseReleased(var1);
      }
   }

   public void mouseMoved(double var1, double var3) {
      this.panel.drag(var1, var3);
      super.mouseMoved(var1, var3);
   }

   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      this.panel.drag(var1.x(), var1.y());
      return this.panel.isDragging() || super.mouseDragged(var1, var2, var4);
   }

   public void added() {
      super.added();
      this.intro.retainKeys(var1 -> KeyCodes.isKeyDown(this.minecraft.getWindow(), var1));
      if (this.intro.isHome()) {
         for (int var4 : new int[]{257, 335, 32}) {
            if (KeyCodes.isKeyDown(this.minecraft.getWindow(), var4)) {
               this.intro.addKey(var4);
            }
         }
      }
   }

   public void removed() {
      this.accountSwitcher.close();
      if (!this.intro.isHome()) {
         this.intro.finishTransition();
      }

      this.draggingPanel = (boolean)0;
      this.panel.reset(!this.intro.isHome());
      super.removed();
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      return this.accountSwitcher.pasteToken(var1, var3, var7) || super.mouseScrolled(var1, var3, var5, var7);
   }

   public boolean isPauseScreen() {
      return false;
   }

   private record TitleGlyph(String text, float x, float width) {
      private final float offset;

      private TitleGlyph(String text, float x, float width) {
         this.text = text;
         this.offset = x;
         this.width = width;
      }
   }
}

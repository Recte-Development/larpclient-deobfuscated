package com.larpclient.gui.screen;

import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;

public final class MenuIntroAnimation {
   public static final double tRANSITION_MS = 1420.0;
   private static final double sQUASH_MS = 150.0;
   private static final double lOGO_TRAVEL_MS = 610.0;
   private static final double tITLE_DELAY_MS = 720.0;
   private static final double tITLE_MS = 600.0;
   private static final double tITLE_OFFSET_RATIO = 0.28;
   public static final int pARTICLE_COUNT = 12;
   private static final double rEVERSE_SPEED = 1.4;
   private final MenuIntroAnimation.Factory state;
   private MenuIntroAnimation.Phase phase;
   private long lastNanos = -1L;
   private double idleTimeMs;
   private double progressMs;
   private boolean active;
   private final Set<Integer> heldKeys = new HashSet<>();

   private MenuIntroAnimation(MenuIntroAnimation.Factory var1, boolean var2) {
      this.state = var1;
      this.phase = var2 ? MenuIntroAnimation.Phase.HOME : MenuIntroAnimation.Phase.DOCKED;
   }

   public MenuIntroAnimation.Phase getPhase() {
      return this.phase;
   }

   public boolean isHome() {
      return this.phase == MenuIntroAnimation.Phase.HOME;
   }

   public boolean isDocked() {
      return this.phase == MenuIntroAnimation.Phase.DOCKED;
   }

   public double getProgress() {
      return this.progressMs;
   }

   public void update(long var1, boolean var3, boolean var4) {
      double var5 = this.lastNanos >= 0L && var1 >= this.lastNanos && var1 - this.lastNanos <= 500000000L ? (var1 - this.lastNanos) / 1000000.0 : 0.0;
      this.lastNanos = var1;
      if (var3 && this.active) {
         if (this.isHome()) {
            this.idleTimeMs += var5;
         } else if (this.phase == MenuIntroAnimation.Phase.DOCKING) {
            this.progressMs += var5;
         } else if (this.phase == MenuIntroAnimation.Phase.UNDOCKING) {
            this.progressMs -= var5 * 1.4;
         }
      }

      this.active = var3;
      if (this.phase == MenuIntroAnimation.Phase.DOCKING && (!var4 || this.progressMs >= 1420.0)) {
         this.finishTransition();
      }

      if (this.phase == MenuIntroAnimation.Phase.UNDOCKING && (!var4 || this.progressMs <= 0.0)) {
         this.finishTransition();
      }
   }

   public boolean beginDock(boolean var1) {
      if (this.active && this.isHome()) {
         this.state.introPlayed = (boolean)1;
         this.phase = var1 ? MenuIntroAnimation.Phase.DOCKING : MenuIntroAnimation.Phase.DOCKED;
         this.progressMs = 0.0;
         return true;
      } else {
         return false;
      }
   }

   public boolean beginUndock(boolean var1) {
      if (this.active && this.isDocked()) {
         this.phase = var1 ? MenuIntroAnimation.Phase.UNDOCKING : MenuIntroAnimation.Phase.HOME;
         this.progressMs = var1 ? 1420.0 : 0.0;
         return true;
      } else {
         return false;
      }
   }

   public void finishTransition() {
      if (this.phase == MenuIntroAnimation.Phase.UNDOCKING) {
         this.phase = MenuIntroAnimation.Phase.HOME;
         this.progressMs = 0.0;
      } else if (!this.isHome()) {
         this.phase = MenuIntroAnimation.Phase.DOCKED;
         this.progressMs = 1420.0;
      }
   }

   public void skipToDocked() {
      this.state.introPlayed = (boolean)1;
      this.phase = MenuIntroAnimation.Phase.DOCKED;
      this.progressMs = 1420.0;
   }

   public boolean addKey(int var1) {
      return this.heldKeys.add(var1);
   }

   public boolean isHome(int var1) {
      return this.heldKeys.contains(var1);
   }

   public boolean removeKey(int var1) {
      return this.heldKeys.remove(var1);
   }

   public void retainKeys(IntPredicate var1) {
      this.heldKeys.removeIf(var1x -> !var1.test(var1x));
   }

   public static HomeLayout.Rect buttonBounds(int var0, int var1) {
      int var2 = Math.min(188, var0 - 32);
      return new HomeLayout.Rect((var0 - var2) / 2, (int)(var1 * 0.48) + logoHeight(var1) / 2 + 42, var2, 28);
   }

   private static int logoHeight(int var0) {
      return (int)Math.clamp(var0 * 0.16, 30.0, 86.0);
   }

   public MenuIntroAnimation.LogoTransform logoTransform(MainMenuLayout var1, boolean var2) {
      HomeLayout.Rect var3 = var1.logo();
      double var4 = var1.width() / 2.0;
      double var6 = var1.height() * 0.48;
      double var8 = var3.left() + var3.width() / 2.0;
      double var10 = var3.top() + var3.height() / 2.0;
      double var12 = logoHeight(var1.height());
      if (this.isDocked()) {
         return new MenuIntroAnimation.LogoTransform(var8, var10, var3.width(), 1.0, 0.0);
      }

      double var14 = var2 ? Math.sin(this.idleTimeMs / 1800.0) * 0.025 : 0.0;
      if (this.isHome()) {
         return new MenuIntroAnimation.LogoTransform(var4, var6, var12, 1.0, var14);
      }

      double var16 = Math.pow(Math.sin(Math.PI * clamp01(this.progressMs / 150.0)), 2.0);
      if (this.progressMs < 150.0) {
         return new MenuIntroAnimation.LogoTransform(var4, var6 + 5.0 * var16, var12 * (1.0 + 0.06 * var16), 1.0 - 0.15 * var16, var14 - 0.055 * var16);
      }

      double var18 = clamp01((this.progressMs - 150.0) / 610.0);
      double var20 = smootherstep(var18);
      double var22 = Math.sin(Math.PI * var20);
      double var24 = Math.sin((Math.PI * 2) * var18) * Math.pow(1.0 - var18, 2.0);
      return new MenuIntroAnimation.LogoTransform(
         var4 + (var8 - var4) * var20 + Math.min(42.0, var1.width() * 0.06) * var22,
         var6 + (var10 - var6) * var20 - 24.0 * var22,
         var12 + (var3.width() - var12) * var20,
         1.0 + 0.13 * var24,
         var14 * (1.0 - var20) - 0.28 * var22 + 0.1 * var24
      );
   }

   public MenuIntroAnimation.TitleTransform titleTransform(MainMenuLayout var1, boolean var2) {
      HomeLayout.Rect var3 = var1.logo();
      HomeLayout.Rect var4 = var1.title();
      MenuIntroAnimation.LogoTransform var5 = this.logoTransform(var1, var2);
      double var6 = var5.centerX() + var5.size() * 0.28;
      if (this.isDocked()) {
         return new MenuIntroAnimation.TitleTransform(var4.left(), 0.0, var6, 1.0);
      }

      double var8 = this.isHome() ? 0.0 : clamp01((this.progressMs - 720.0) / 600.0);
      double var10 = 1.0 - Math.pow(1.0 - var8, 3.0);
      double var12 = var3.width() * 0.28 - var4.width() - 2.0;
      double var14 = var4.left() - (var3.left() + var3.width() / 2.0);
      double var16 = var5.centerY() - (var3.top() + var3.height() / 2.0);
      return new MenuIntroAnimation.TitleTransform(var5.centerX() + var12 + (var14 - var12) * var10, var16, var6, retainKeys(var8 / 0.25));
   }

   public MenuIntroAnimation.PartTransform backgroundFade(boolean var1) {
      double var2 = this.isHome() ? 1.0 : 1.0 - retainKeys(this.progressMs / 150.0);
      return new MenuIntroAnimation.PartTransform(0.0, 0.0, 1.0, 1.0, 0.0, var2);
   }

   public MenuIntroAnimation.PartTransform addKey(boolean var1) {
      double var2 = this.isHome() ? (var1 ? 0.78 + 0.12 * Math.sin(this.idleTimeMs / 900.0) : 1.0) : 1.0 - retainKeys(this.progressMs / 110.0);
      return new MenuIntroAnimation.PartTransform(0.0, 0.0, 1.0, 1.0, 0.0, var2);
   }

   public MenuIntroAnimation.PartTransform retainKeys(int var1) {
      int var2 = var1 == 5 ? 1 : 0;
      int var3 = var1 < 3 ? 1 : 0;
      return this.removeKey(
         var2 != 0 ? 700 + var1 * 70 + 50 : 700 + var1 * 70,
         440.0,
         var3 != 0 ? 27.0 : 16.0,
         var2 != 0 ? -10.0 : (var1 % 2 == 0 ? -7 : 7),
         var3 != 0 ? 0.78 : 0.9
      );
   }

   private MenuIntroAnimation.PartTransform removeKey(double var1, double var3, double var5, double var7, double var9) {
      if (this.isDocked()) {
         return MenuIntroAnimation.PartTransform.REST;
      }

      double var11 = this.isHome() ? 0.0 : clamp01((this.progressMs - var1) / var3);
      double var13 = retainKeys(var11 / 0.42);
      double var15 = easeOutBack(var11);
      double var17 = Math.sin(Math.PI * var11) * (1.0 - var11);
      return new MenuIntroAnimation.PartTransform(
         var7 * var17, var5 * (1.0 - var15), var9 + (1.0 - var9) * var15, var9 + (1.0 - var9) * var15, 0.022 * var7 / 10.0 * var17, var13
      );
   }

   public double beginUndock() {
      return this.isDocked() ? 1.0 : (this.isHome() ? 0.12 : 0.12 + 0.88 * smootherstep((this.progressMs - 110.0) / 820.0));
   }

   public MenuIntroAnimation.ParticleState particleAt(int var1, MainMenuLayout var2) {
      double var3 = !this.isHome() && !this.isDocked() ? clamp01((this.progressMs - 180.0 - var1 % 3 * 20) / 660.0) : 0.0;
      double var5 = var1 % 2 == 0 ? -1.0 : 1.0;
      double var7 = -0.9 + var1 / 2.0 * 0.29;
      double var9 = Math.min(var2.width() * 0.43, 270.0) * (1.0 - Math.pow(1.0 - var3, 3.0));
      double var11 = var2.width() / 2.0 + var5 * (20.0 + var9 * Math.cos(var7));
      double var13 = var2.height() * 0.48 + var9 * 0.35 * Math.sin(var7) - 44.0 * Math.sin(Math.PI * var3) + 35.0 * var3 * var3;
      return new MenuIntroAnimation.ParticleState(
         var11, var13, (3 + var1 % 3 * 2) * (1.0 - 0.6 * var3), var5 * var3 * 2.3, 0.32 * retainKeys(var3 / 0.15) * (1.0 - retainKeys((var3 - 0.35) / 0.65))
      );
   }

   public static double clamp01(double var0) {
      return Math.clamp(var0, 0.0, 1.0);
   }

   private static double retainKeys(double var0) {
      var0 = clamp01(var0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static double smootherstep(double var0) {
      var0 = clamp01(var0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }

   private static double easeOutBack(double var0) {
      var0 = clamp01(var0);
      double var2 = var0 - 1.0;
      return 1.0 + 2.4 * var2 * var2 * var2 + 1.4 * var2 * var2;
   }

   public static final class Factory {
      private boolean introPlayed;

      public MenuIntroAnimation create() {
         return new MenuIntroAnimation(this, !this.introPlayed);
      }
   }

   public record LogoTransform(double x, double y, double size, double stretch, double rotation) {
      private final double centerX;
      private final double centerY;

      public LogoTransform(double x, double y, double size, double stretch, double rotation) {
         this.centerX = x;
         this.centerY = y;
         this.size = size;
         this.stretch = stretch;
         this.rotation = rotation;
      }
   }

   public record PartTransform(double x, double y, double scaleX, double scaleY, double rotation, double alpha) {
      private final double offsetX;
      private final double offsetY;
      public static final MenuIntroAnimation.PartTransform REST = new MenuIntroAnimation.PartTransform(0.0, 0.0, 1.0, 1.0, 0.0, 1.0);

      public PartTransform(double x, double y, double scaleX, double scaleY, double rotation, double alpha) {
         this.offsetX = x;
         this.offsetY = y;
         this.scaleX = scaleX;
         this.scaleY = scaleY;
         this.rotation = rotation;
         this.alpha = alpha;
      }
   }

   public record ParticleState(double x, double y, double size, double rotation, double alpha) {
      private final double posX;
      private final double posY;

      public ParticleState(double x, double y, double size, double rotation, double alpha) {
         this.posX = x;
         this.posY = y;
         this.size = size;
         this.rotation = rotation;
         this.alpha = alpha;
      }
   }

   public enum Phase {
      HOME,
      DOCKING,
      UNDOCKING,
      DOCKED;
   }

   public record TitleTransform(double x, double lift, double clip, double alpha) {
      private final double posX;

      public TitleTransform(double x, double lift, double clip, double alpha) {
         this.posX = x;
         this.lift = lift;
         this.clip = clip;
         this.alpha = alpha;
      }
   }
}

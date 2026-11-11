package com.larpclient.account;

import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.util.misc.LarpLog;
import com.mojang.authlib.GameProfile;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.PlayerSkin;

public final class AccountSkin {
   private static final long sKIN_TIMEOUT_NANOS = 10000000000L;
   private Supplier<PlayerSkin> skinLookup;
   private GameProfile profile;
   private int generation = -1;
   private PlayerSkin fallbackSkin;
   private long switchStartNanos = -1L;

   public PlayerSkin getSkin() {
      Minecraft var1 = Minecraft.getInstance();
      int var2 = PostProcessing.reloadCount;
      GameProfile var3 = var1.getGameProfile();
      if (this.skinLookup == null || !var3.equals(this.profile) || this.generation != var2) {
         if (this.profile != null && !this.profile.id().equals(var3.id())) {
            this.fallbackSkin = DefaultPlayerSkin.get(var3);
            this.switchStartNanos = System.nanoTime();
            LarpLog.info("Accounts", "Game account changed; menu skin lookup restarted, native fallback shown until the texture loads");
         }

         this.profile = var3;
         this.generation = var2;
         this.skinLookup = var1.getSkinManager().createLookup(this.profile, false);
      }

      PlayerSkin var4 = this.skinLookup.get();
      if (this.switchStartNanos >= 0L) {
         this.checkSwitchProgress(var4);
      }

      return var4;
   }

   private void checkSwitchProgress(PlayerSkin var1) {
      long var2 = System.nanoTime() - this.switchStartNanos;
      if (!var1.equals(this.fallbackSkin)) {
         LarpLog.info("Accounts", "Switched account skin ready after {} ms", var2 / 1000000L);
         this.switchStartNanos = -1L;
      } else if (var2 > 10000000000L) {
         LarpLog.warn(
            "Accounts",
            "Switched account still shows the default skin after {} s; it has no custom skin or the skin service is unavailable",
            var2 / 1000000000L
         );
         this.switchStartNanos = -1L;
      }
   }

   public void drawFace(GuiGraphicsExtractor var1, int var2, int var3, int var4) {
      PlayerFaceExtractor.extractRenderState(var1, this.getSkin(), var2, var3, var4, AlphaScope.applyAlpha(-1));
   }
}

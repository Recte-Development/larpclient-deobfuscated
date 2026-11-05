package com.larpclient;

import com.mojang.authlib.GameProfile;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.PlayerSkin;

public final class cn {
   private static final long W = 10000000000L;
   private Supplier<PlayerSkin> H;
   private GameProfile U;
   private int P = -1;
   private PlayerSkin r;
   private long s = -1L;

   public PlayerSkin x() {
      Minecraft var1 = Minecraft.getInstance();
      int var2 = m5.k;
      GameProfile var3 = var1.getGameProfile();
      if (this.H == null || !var3.equals(this.U) || this.P != var2) {
         if (this.U != null && !this.U.id().equals(var3.id())) {
            this.r = DefaultPlayerSkin.get(var3);
            this.s = System.nanoTime();
            mt.K("Accounts", "Game account changed; menu skin lookup restarted, native fallback shown until the texture loads");
         }

         this.U = var3;
         this.P = var2;
         this.H = var1.getSkinManager().createLookup(this.U, false);
      }

      PlayerSkin var4 = this.H.get();
      if (this.s >= 0L) {
         this.N(var4);
      }

      return var4;
   }

   private void N(PlayerSkin var1) {
      long var2 = System.nanoTime() - this.s;
      if (!var1.equals(this.r)) {
         mt.K("Accounts", "Switched account skin ready after {} ms", var2 / 1000000L);
         this.s = -1L;
      } else if (var2 > 10000000000L) {
         mt.T(
            "Accounts",
            "Switched account still shows the default skin after {} s; it has no custom skin or the skin service is unavailable",
            var2 / 1000000000L
         );
         this.s = -1L;
      }
   }

   public void W(GuiGraphicsExtractor var1, int var2, int var3, int var4) {
      PlayerFaceExtractor.extractRenderState(var1, this.x(), var2, var3, var4, de.h(-1));
   }
}

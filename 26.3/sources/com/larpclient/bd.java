package com.larpclient;

import com.mojang.authlib.GameProfile;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.PlayerSkin;

public final class bd {
   private static final long A = 10000000000L;
   private Supplier<PlayerSkin> Z;
   private GameProfile Y;
   private int B = -1;
   private PlayerSkin i;
   private long F = -1L;

   public PlayerSkin j() {
      Minecraft var1 = Minecraft.getInstance();
      int var2 = nl.q;
      GameProfile var3 = var1.getGameProfile();
      if (this.Z == null || !var3.equals(this.Y) || this.B != var2) {
         if (this.Y != null && !this.Y.id().equals(var3.id())) {
            this.i = DefaultPlayerSkin.get(var3);
            this.F = System.nanoTime();
            _q.z("Accounts", "Game account changed; menu skin lookup restarted, native fallback shown until the texture loads");
         }

         this.Y = var3;
         this.B = var2;
         this.Z = var1.getSkinManager().createLookup(this.Y, false);
      }

      PlayerSkin var4 = this.Z.get();
      if (this.F >= 0L) {
         this.Z(var4);
      }

      return var4;
   }

   private void Z(PlayerSkin var1) {
      long var2 = System.nanoTime() - this.F;
      if (!var1.equals(this.i)) {
         _q.z("Accounts", "Switched account skin ready after {} ms", var2 / 1000000L);
         this.F = -1L;
      } else if (var2 > 10000000000L) {
         _q.R(
            "Accounts",
            "Switched account still shows the default skin after {} s; it has no custom skin or the skin service is unavailable",
            var2 / 1000000000L
         );
         this.F = -1L;
      }
   }

   public void K(GuiGraphicsExtractor var1, int var2, int var3, int var4) {
      PlayerFaceExtractor.extractRenderState(var1, this.j(), var2, var3, var4, oa.e(-1));
   }
}

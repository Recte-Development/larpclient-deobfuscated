package com.larpclient;

import com.mojang.authlib.GameProfile;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.PlayerSkin;

public final class yd {
   private static final long r = 10000000000L;
   private Supplier<PlayerSkin> u;
   private GameProfile d;
   private int J = -1;
   private PlayerSkin D;
   private long y = -1L;

   public PlayerSkin O() {
      Minecraft var1 = Minecraft.getInstance();
      int var2 = jk.v;
      GameProfile var3 = var1.getGameProfile();
      if (this.u == null || !var3.equals(this.d) || this.J != var2) {
         if (this.d != null && !this.d.id().equals(var3.id())) {
            this.D = DefaultPlayerSkin.get(var3);
            this.y = System.nanoTime();
            r4.V("Accounts", "Game account changed; menu skin lookup restarted, native fallback shown until the texture loads");
         }

         this.d = var3;
         this.J = var2;
         this.u = var1.getSkinManager().createLookup(this.d, false);
      }

      PlayerSkin var4 = this.u.get();
      if (this.y >= 0L) {
         this.G(var4);
      }

      return var4;
   }

   private void G(PlayerSkin var1) {
      long var2 = System.nanoTime() - this.y;
      if (!var1.equals(this.D)) {
         r4.V("Accounts", "Switched account skin ready after {} ms", var2 / 1000000L);
         this.y = -1L;
      } else if (var2 > 10000000000L) {
         r4.m(
            "Accounts",
            "Switched account still shows the default skin after {} s; it has no custom skin or the skin service is unavailable",
            var2 / 1000000000L
         );
         this.y = -1L;
      }
   }

   public void n(GuiGraphicsExtractor var1, int var2, int var3, int var4) {
      PlayerFaceExtractor.extractRenderState(var1, this.O(), var2, var3, var4, com.larpclient.r.D(-1));
   }
}

package com.larpclient;

import com.mojang.authlib.GameProfile;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.PlayerSkin;

public final class b6 {
   private static final long u = 10000000000L;
   private Supplier<PlayerSkin> A;
   private GameProfile d;
   private int H = -1;
   private PlayerSkin D;
   private long t = -1L;

   public PlayerSkin S() {
      Minecraft var1 = Minecraft.getInstance();
      int var2 = wa.s;
      GameProfile var3 = var1.getGameProfile();
      if (this.A == null || !var3.equals(this.d) || this.H != var2) {
         if (this.d != null && !this.d.id().equals(var3.id())) {
            this.D = DefaultPlayerSkin.get(var3);
            this.t = System.nanoTime();
            h.A("Accounts", "Game account changed; menu skin lookup restarted, native fallback shown until the texture loads");
         }

         this.d = var3;
         this.H = var2;
         this.A = var1.getSkinManager().createLookup(this.d, false);
      }

      PlayerSkin var4 = this.A.get();
      if (this.t >= 0L) {
         this.T(var4);
      }

      return var4;
   }

   private void T(PlayerSkin var1) {
      long var2 = System.nanoTime() - this.t;
      if (!var1.equals(this.D)) {
         h.A("Accounts", "Switched account skin ready after {} ms", var2 / 1000000L);
         this.t = -1L;
      } else if (var2 > 10000000000L) {
         h.g(
            "Accounts",
            "Switched account still shows the default skin after {} s; it has no custom skin or the skin service is unavailable",
            var2 / 1000000000L
         );
         this.t = -1L;
      }
   }

   public void c(GuiGraphicsExtractor var1, int var2, int var3, int var4) {
      PlayerFaceExtractor.extractRenderState(var1, this.S(), var2, var3, var4, ua.I(-1));
   }
}

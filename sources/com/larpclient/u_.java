package com.larpclient;

import java.util.Locale;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ReadOnlyScoreInfo;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria.RenderType;

public final class u_ {
   private static final Pattern t = Pattern.compile("\\bhp\\b");
   private static final u_.g S = new u_.g();
   private static final u_.g F = new u_.g();
   private static boolean C;

   private u_() {
   }

   public static float B(LivingEntity var0) {
      float var1 = var0.getHealth();
      if (var0 instanceof Player var2 && var2 != Minecraft.getInstance().player) {
         int var3 = F(var2);
         if (var3 <= 0) {
            return var1;
         }

         float var4 = Math.max(0.0F, var0.getAbsorptionAmount());
         if (!(Math.abs(var3 - Math.ceil(var1)) <= 1.0) && !(Math.abs(var3 - Math.ceil(var1 + var4)) <= 1.0)) {
            if (!C) {
               C = (boolean)1;
               r4.V("ESP", "Synced health {} disagrees with scoreboard health {}; using scoreboard", var1, var3);
            }

            return Math.min(var3, Math.max(1.0F, var0.getMaxHealth()));
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static double g(LivingEntity var0) {
      return Math.clamp(B(var0) / Math.max(1.0F, var0.getMaxHealth()), 0.0F, 1.0F);
   }

   private static int F(Player var0) {
      ClientLevel var1 = Minecraft.getInstance().level;
      if (var1 == null) {
         return -1;
      }

      Scoreboard var2 = var1.getScoreboard();
      int var3 = C(var2, var2.getDisplayObjective(DisplaySlot.BELOW_NAME), S, var0);
      return var3 > 0 ? var3 : C(var2, var2.getDisplayObjective(DisplaySlot.LIST), F, var0);
   }

   private static int C(Scoreboard var0, Objective var1, u_.g var2, Player var3) {
      if (var1 != null && var2.L(var1)) {
         ReadOnlyScoreInfo var4 = var0.getPlayerScoreInfo(var3, var1);
         return var4 == null ? -1 : var4.value();
      } else {
         return -1;
      }
   }

   private static final class g {
      private Objective x;
      private Component Y;
      private boolean C;

      boolean L(Objective var1) {
         if (var1.getRenderType() == RenderType.HEARTS) {
            return true;
         }

         Component var2 = var1.getDisplayName();
         if (var1 == this.x && var2 == this.Y) {
            return this.C;
         }

         String var3 = var2.getString().toLowerCase(Locale.ROOT);
         this.x = var1;
         this.Y = var2;
         this.C = (boolean)(!var3.contains("❤") && !var3.contains("♥") && !var3.contains("health") && !u_.t.matcher(var3).find() ? 0 : 1);
         return this.C;
      }
   }
}

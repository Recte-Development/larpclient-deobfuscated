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

public final class t3 {
   private static final Pattern p = Pattern.compile("\\bhp\\b");
   private static final t3.t q = new t3.t();
   private static final t3.t A = new t3.t();
   private static boolean X;

   private t3() {
   }

   public static float Z(LivingEntity var0) {
      float var1 = var0.getHealth();
      if (var0 instanceof Player var2 && var2 != Minecraft.getInstance().player) {
         int var3 = o(var2);
         if (var3 <= 0) {
            return var1;
         }

         float var4 = Math.max(0.0F, var0.getAbsorptionAmount());
         if (!(Math.abs(var3 - Math.ceil(var1)) <= 1.0) && !(Math.abs(var3 - Math.ceil(var1 + var4)) <= 1.0)) {
            if (!X) {
               X = (boolean)1;
               mt.K("ESP", "Synced health {} disagrees with scoreboard health {}; using scoreboard", var1, var3);
            }

            return Math.min(var3, Math.max(1.0F, var0.getMaxHealth()));
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static double f(LivingEntity var0) {
      return Math.clamp(Z(var0) / Math.max(1.0F, var0.getMaxHealth()), 0.0F, 1.0F);
   }

   private static int o(Player var0) {
      ClientLevel var1 = Minecraft.getInstance().level;
      if (var1 == null) {
         return -1;
      }

      Scoreboard var2 = var1.getScoreboard();
      int var3 = T(var2, var2.getDisplayObjective(DisplaySlot.BELOW_NAME), q, var0);
      return var3 > 0 ? var3 : T(var2, var2.getDisplayObjective(DisplaySlot.LIST), A, var0);
   }

   private static int T(Scoreboard var0, Objective var1, t3.t var2, Player var3) {
      if (var1 != null && var2.R(var1)) {
         ReadOnlyScoreInfo var4 = var0.getPlayerScoreInfo(var3, var1);
         return var4 == null ? -1 : var4.value();
      } else {
         return -1;
      }
   }

   private static final class t {
      private Objective U;
      private Component O;
      private boolean S;

      boolean R(Objective var1) {
         if (var1.getRenderType() == RenderType.HEARTS) {
            return true;
         }

         Component var2 = var1.getDisplayName();
         if (var1 == this.U && var2 == this.O) {
            return this.S;
         }

         String var3 = var2.getString().toLowerCase(Locale.ROOT);
         this.U = var1;
         this.O = var2;
         this.S = (boolean)(!var3.contains("❤") && !var3.contains("♥") && !var3.contains("health") && !t3.p.matcher(var3).find() ? 0 : 1);
         return this.S;
      }
   }
}

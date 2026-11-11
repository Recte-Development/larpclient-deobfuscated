package com.larpclient.util.entity;

import com.larpclient.util.misc.LarpLog;
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

public final class EntityHealth {
   private static final Pattern hP_PATTERN = Pattern.compile("\\bhp\\b");
   private static final EntityHealth.HealthObjectiveCache belowNameCache = new EntityHealth.HealthObjectiveCache();
   private static final EntityHealth.HealthObjectiveCache listCache = new EntityHealth.HealthObjectiveCache();
   private static boolean warnedMismatch;

   private EntityHealth() {
   }

   public static float getHealth(LivingEntity var0) {
      float var1 = var0.getHealth();
      if (var0 instanceof Player var2 && var2 != Minecraft.getInstance().player) {
         int var3 = getScoreboardHealth(var2);
         if (var3 <= 0) {
            return var1;
         }

         float var4 = Math.max(0.0F, var0.getAbsorptionAmount());
         if (!(Math.abs(var3 - Math.ceil(var1)) <= 1.0) && !(Math.abs(var3 - Math.ceil(var1 + var4)) <= 1.0)) {
            if (!warnedMismatch) {
               warnedMismatch = (boolean)1;
               LarpLog.info("ESP", "Synced health {} disagrees with scoreboard health {}; using scoreboard", var1, var3);
            }

            return Math.min(var3, Math.max(1.0F, var0.getMaxHealth()));
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static double getHealthFraction(LivingEntity var0) {
      return Math.clamp(getHealth(var0) / Math.max(1.0F, var0.getMaxHealth()), 0.0F, 1.0F);
   }

   private static int getScoreboardHealth(Player var0) {
      ClientLevel var1 = Minecraft.getInstance().level;
      if (var1 == null) {
         return -1;
      }

      Scoreboard var2 = var1.getScoreboard();
      int var3 = getScoreboardHealth(var2, var2.getDisplayObjective(DisplaySlot.BELOW_NAME), belowNameCache, var0);
      return var3 > 0 ? var3 : getScoreboardHealth(var2, var2.getDisplayObjective(DisplaySlot.LIST), listCache, var0);
   }

   private static int getScoreboardHealth(Scoreboard var0, Objective var1, EntityHealth.HealthObjectiveCache var2, Player var3) {
      if (var1 != null && var2.isHealthObjective(var1)) {
         ReadOnlyScoreInfo var4 = var0.getPlayerScoreInfo(var3, var1);
         return var4 == null ? -1 : var4.value();
      } else {
         return -1;
      }
   }

   private static final class HealthObjectiveCache {
      private Objective objective;
      private Component displayName;
      private boolean isHealth;

      boolean isHealthObjective(Objective var1) {
         if (var1.getRenderType() == RenderType.HEARTS) {
            return true;
         }

         Component var2 = var1.getDisplayName();
         if (var1 == this.objective && var2 == this.displayName) {
            return this.isHealth;
         }

         String var3 = var2.getString().toLowerCase(Locale.ROOT);
         this.objective = var1;
         this.displayName = var2;
         this.isHealth = (boolean)(!var3.contains("❤") && !var3.contains("♥") && !var3.contains("health") && !EntityHealth.hP_PATTERN.matcher(var3).find()
            ? 0
            : 1);
         return this.isHealth;
      }
   }
}

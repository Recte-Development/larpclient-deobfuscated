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

public final class zy {
   private static final Pattern d = Pattern.compile("\\bhp\\b");
   private static final zy.f V = new zy.f();
   private static final zy.f W = new zy.f();
   private static boolean m;

   private zy() {
   }

   public static float f(LivingEntity var0) {
      float var1 = var0.getHealth();
      if (var0 instanceof Player var2 && var2 != Minecraft.getInstance().player) {
         int var3 = I(var2);
         if (var3 <= 0) {
            return var1;
         }

         float var4 = Math.max(0.0F, var0.getAbsorptionAmount());
         if (!(Math.abs(var3 - Math.ceil(var1)) <= 1.0) && !(Math.abs(var3 - Math.ceil(var1 + var4)) <= 1.0)) {
            if (!m) {
               m = (boolean)1;
               h.A("ESP", "Synced health {} disagrees with scoreboard health {}; using scoreboard", var1, var3);
            }

            return Math.min(var3, Math.max(1.0F, var0.getMaxHealth()));
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static double N(LivingEntity var0) {
      return Math.clamp(f(var0) / Math.max(1.0F, var0.getMaxHealth()), 0.0F, 1.0F);
   }

   private static int I(Player var0) {
      ClientLevel var1 = Minecraft.getInstance().level;
      if (var1 == null) {
         return -1;
      }

      Scoreboard var2 = var1.getScoreboard();
      int var3 = D(var2, var2.getDisplayObjective(DisplaySlot.BELOW_NAME), V, var0);
      return var3 > 0 ? var3 : D(var2, var2.getDisplayObjective(DisplaySlot.LIST), W, var0);
   }

   private static int D(Scoreboard var0, Objective var1, zy.f var2, Player var3) {
      if (var1 != null && var2.d(var1)) {
         ReadOnlyScoreInfo var4 = var0.getPlayerScoreInfo(var3, var1);
         return var4 == null ? -1 : var4.value();
      } else {
         return -1;
      }
   }

   private static final class f {
      private Objective u;
      private Component j;
      private boolean y;

      boolean d(Objective var1) {
         if (var1.getRenderType() == RenderType.HEARTS) {
            return true;
         }

         Component var2 = var1.getDisplayName();
         if (var1 == this.u && var2 == this.j) {
            return this.y;
         }

         String var3 = var2.getString().toLowerCase(Locale.ROOT);
         this.u = var1;
         this.j = var2;
         this.y = (boolean)(!var3.contains("❤") && !var3.contains("♥") && !var3.contains("health") && !zy.d.matcher(var3).find() ? 0 : 1);
         return this.y;
      }
   }
}

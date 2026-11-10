package com.larpclient.module;

import com.larpclient.q8;
import com.larpclient.qo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2663;
import net.minecraft.class_310;
import net.minecraft.class_8143;

public final class AutoGGModule extends Module {
   static final int c = 5;
   private static AutoGGModule t;
   private final aj[] A = this.E();
   private final li C = this.addSetting(new li("Order", "Which message to use when you have several", "Random", "Random", "In Order"));
   private final lo L = this.addSetting(new lo("Delay", "Wait this long after the kill before sending", 0.5, 0.0, 5.0, 0.1).m("s"));
   private final lo f = this.addSetting(new lo("Cooldown", "Kills within this time of your last message get no message", 3.0, 1.0, 30.0, 0.5).m("s"));
   private final com.larpclient.r x = new com.larpclient.r();
   private final l9 K = new l9();
   private int T = -1;

   public AutoGGModule() {
      super("Auto GG", "Send your own chat message or command after you kill a player", Category.PLAYER, -1);
      t = this;
      this.C.O(() -> this.K().size() > 1);
   }

   private aj[] E() {
      aj[] var1 = new aj[5];
      var1[0] = this.addSetting(
         new aj("Message", "Sent after a kill. {player} becomes their name. Start with / to run a command. /pay is not allowed.", "gg {player}")
            .A("gg {player}")
            .Y(l9::Y, "Auto GG cannot send /pay")
      );

      for (int var2 = 1; var2 < 5; var2++) {
         aj var3 = var1[var2 - 1];
         aj var4 = this.addSetting(
            new aj("Message " + (var2 + 1), "Another message to mix in. Leave empty to skip.", "").A("Optional").Y(l9::Y, "Auto GG cannot send /pay")
         );
         var4.O(() -> !var3.getValue().isBlank() || !var4.getValue().isBlank());
         var1[var2] = var4;
      }

      return var1;
   }

   public static AutoGGModule A() {
      return t;
   }

   public List<lb<?>> J() {
      ArrayList var1 = new ArrayList<>(Arrays.asList(this.A));
      var1.add(this.C);
      return var1;
   }

   public List<lb<?>> j() {
      return List.of(this.L, this.f);
   }

   @Override
   public List<String> getSearchAliases() {
      return List.of("Kill Message", "Kill Say", "Chat");
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   private List<String> K() {
      return Arrays.stream(this.A).map(lb::getValue).filter(var0 -> !var0.isBlank()).toList();
   }

   @Override
   public void onEnable() {
      q8.G("AutoGG", "Enabled: {} message(s), server-confirmed kills only", this.K().size());
   }

   @Override
   public void onDisable() {
      this.x.E();
      this.K.v();
   }

   public void w(class_8143 var1) {
      if (this.isEnabled()) {
         class_310 var2 = getMc();
         if (this.isRuntimeActive() && this.isInGame()) {
            class_1297 var3 = var2.field_1687.method_8469(var1.comp_1267());
            if (var3 instanceof class_1657 && var3 != var2.field_1724) {
               this.x.u(var3, var1.comp_1269() == var2.field_1724.method_5628(), qo.K());
            }
         }
      }
   }

   public void c(class_2663 var1) {
      if (this.isEnabled()) {
         class_310 var2 = getMc();
         if (this.isRuntimeActive() && this.isInGame()) {
            if (var1.method_11469(var2.field_1687) instanceof class_1657 var3 && var3 != var2.field_1724) {
               if (var1.method_11470() == 35) {
                  this.x.y(var3);
               } else {
                  if (var1.method_11470() == 3 && this.x.M(var3, qo.K())) {
                     this.A(var3.method_7334().name());
                  }
               }
            }
         }
      }
   }

   private void A(String var1) {
      double var2 = qo.K();
      if (!this.K.Z(var2)) {
         q8.G("AutoGG", "Killed {}; no message because one is waiting or the cooldown is running", var1);
      } else {
         List var4 = this.K();
         if (var4.isEmpty()) {
            q8.G("AutoGG", "Killed {}; every message is empty", var1);
         } else {
            this.T = l9.D(var4.size(), this.T, this.C.G("Random"), ThreadLocalRandom.current()::nextInt);
            l9.k var5 = l9.b((String)var4.get(this.T), var1);
            if (var5 == null) {
               q8.G("AutoGG", "Killed {}; message {} has nothing it may send after cleaning (empty or /pay)", var1, this.T + 1);
            } else {
               this.K.W(var5, var2 + this.L.getValue());
               q8.G("AutoGG", "Killed {}; {} queued for {}s from now", var1, var5.command() ? "command" : "message", this.L.getValue());
            }
         }
      }
   }

   @Override
   public void onTick() {
      class_310 var1 = getMc();
      double var2 = qo.K();
      this.x.I(var2, var1x -> var1x instanceof class_1297 var2x && !var2x.method_31481() && var2x.method_73183() == var1.field_1687);
      l9.k var4 = this.K.S(var2, this.f.getValue());
      if (var4 != null) {
         int var10000 = var4.command();
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            var1.field_1724.field_3944.method_45730(var4.text());
         } else {
            var1.field_1724.field_3944.method_45729(var4.text());
         }

         Object[] var10002 = new Object[2];
         int var10005 = var4.command();
         boolean var6 = false;
         byte var5 = (byte)var10005;
         var10005 = Integer.signum(var5);
         var10002[0] = ((var10005 | -var10005) >>> 31 ^ 1) == 0 ? "command" : "message";
         var10002[1] = var4.text().length();
         q8.G("AutoGG", "Sent {} ({} characters)", var10002);
      }
   }
}

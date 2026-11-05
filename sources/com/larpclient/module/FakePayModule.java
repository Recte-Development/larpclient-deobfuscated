package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.hv;
import com.larpclient.r4;
import com.larpclient.uo;
import com.larpclient.x0;
import java.math.BigDecimal;
import java.util.List;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents.AllowChat;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents.AllowCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.numbers.StyledFormat;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerScoreEntry;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

public final class FakePayModule extends Module {
   private static volatile FakePayModule V;
   private volatile boolean r;
   private final hv E = new hv();
   private Object L;
   private boolean x;
   private boolean v;
   private boolean l;
   private boolean a;
   private long F;
   private boolean w;
   private final ln T = this.addSetting(new ln("Spoof Money", "Replace sidebar, tab and /bal displays with your local fake balance", false));
   private final lc S = this.addSetting(new lc("Fake Balance", "Starting fake money; click the value to enter an amount", 1000000.0, 0.0, 1.0E9, 0.01) {
      @Override
      public String getValueAsString() {
         return BigDecimal.valueOf(this.getValue()).stripTrailingZeros().toPlainString();
      }
   });
   private final lc C = this.addSetting(new lc("Balance Billions", "Whole billions in the submitted balance", 0.0, 0.0, 10000.0, 1.0));
   private final ln O = this.addSetting(new ln("Deduct Payments", "Subtract successful fake payments and reject amounts above the fake balance", true));
   private final ln j = this.addSetting(new ln("Action Bar", "Also show the local payment above the hotbar", true));
   private final ln X = this.addSetting(new ln("Sound", "Play a local payment confirmation ding", true));
   private final ln q = this.addSetting(new ln("Hide Errors", "Hide local feedback for invalid /pay attempts and insufficient fake funds", false));

   public FakePayModule() {
      super("FakePay", "Show fake local /pay messages", Category.PLAYER, -1);
      V = this;
      this.S.x(this.T::getValue).W(var1 -> this.i());
      this.C.x(() -> false).W(var1 -> this.i());
      this.O.x(this.T::getValue);
      this.T.W(var1 -> this.i());
      ClientSendMessageEvents.ALLOW_COMMAND.register((AllowCommand)var1 -> !this.p(var1));
      ClientSendMessageEvents.ALLOW_CHAT.register((AllowChat)var1 -> !var1.strip().startsWith("/") || !this.p(var1));
      ClientReceiveMessageEvents.MODIFY_GAME.register(FakePayModule::g);
   }

   private boolean p(String var1) {
      return x0.v(this.r, var1, this::X, FakePayModule::y);
   }

   @Override
   public void enable() {
      this.r = (boolean)1;
      super.enable();
      this.r = this.isEnabled();
   }

   @Override
   public void disable() {
      this.r = (boolean)0;
      this.i();
      super.disable();
   }

   private static void y(RuntimeException var0) {
      r4.r("FakePay", "Local simulation failed; payment remains blocked", var0);
   }

   private void X(x0.w var1) {
      Minecraft var2 = getMc();
      if (var2 != null && var2.level != null && var2.player != null) {
         if (!var1.valid()) {
            Component var5 = this.f(var1.error());
            if (var5 != null) {
               var2.gui.chatListener().handleSystemMessage(var5, false);
            }

            r4.g("FakePay", "Invalid payment attempt consumed locally");
         } else {
            if (this.T.getValue() && this.O.getValue()) {
               this.L(var2.level);
               if (!this.E.J(var1.amount())) {
                  Component var4 = this.f("Not enough fake money. Balance: $ " + x0.V(this.E.u()));
                  if (var4 != null) {
                     var2.gui.chatListener().handleSystemMessage(var4, false);
                  }

                  r4.g("FakePay", "Payment rejected locally: insufficient fake balance");
                  return;
               }

               r4.g("FakePay", "Local fake balance after payment: {}", this.E.u());
            }

            Component var3 = R(var1);
            var2.gui.chatListener().handleSystemMessage(var3, false);
            if (this.j.getValue()) {
               var2.player.sendOverlayMessage(var3);
            }

            if (this.X.getValue()) {
               var2.player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 1.0F);
            }

            r4.g("FakePay", "Simulated payment shown locally; nothing sent");
         }
      }
   }

   public static Component R(x0.w var0) {
      if (!var0.valid()) {
         throw new IllegalArgumentException("Invalid simulated payment");
      } else {
         return Component.literal("You paid " + var0.player() + " ")
            .withStyle(var0x -> var0x.withColor(16777215))
            .append(Component.literal("$").withStyle(var0x -> var0x.withColor(64512)))
            .append(Component.literal(" " + x0.V(var0.amount())).withStyle(var0x -> var0x.withColor(16777215)));
      }
   }

   private void i() {
      this.L = null;
      this.x = (boolean)0;
      this.v = (boolean)0;
      this.l = (boolean)0;
      this.a = (boolean)0;
      this.F = 0L;
      this.w = (boolean)0;
      this.E.E(this.O());
   }

   private void L(Object var1) {
      if (this.L != var1) {
         this.E.E(this.O());
         this.L = var1;
         this.x = (boolean)0;
         this.v = (boolean)0;
         this.l = (boolean)0;
         this.a = (boolean)0;
         this.F = 0L;
         this.w = (boolean)0;
      }
   }

   public lc P() {
      return this.S;
   }

   private BigDecimal O() {
      return BigDecimal.valueOf(this.C.getValue()).scaleByPowerOfTen(9).add(BigDecimal.valueOf(this.S.getValue())).min(hv.f);
   }

   public String u() {
      return this.O().stripTrailingZeros().toPlainString();
   }

   public static void E(JsonObject var0) {
      if (var0.has("Fake Balance") && !var0.has("Balance Billions")) {
         var0.addProperty("Balance Billions", 0);
      }
   }

   public String G(String var1) {
      try {
         BigDecimal var2 = hv.W(var1);
         BigDecimal var3 = BigDecimal.valueOf(1000000000L);
         this.S.C(var2.remainder(var3).doubleValue());
         this.C.C(var2.divideToIntegralValue(var3).doubleValue());
         return "";
      } catch (IllegalArgumentException var4) {
         return var4.getMessage();
      }
   }

   public String I() {
      Minecraft var1 = getMc();
      if (this.r && this.T.getValue() && var1 != null && var1.level != null) {
         this.L(var1.level);
      }

      return "Balance: $ " + x0.V(this.L == null ? this.O() : this.E.u());
   }

   public Component f(String var1) {
      return this.q.getValue() ? null : Component.literal("[Larp/FakePay] " + var1).withStyle(var0 -> var0.withColor(15834027));
   }

   public static MutableComponent Y(MutableComponent var0, Component var1) {
      return V != null && V.isEnabled() ? T(var0, var1, "", "") : var0;
   }

   public static MutableComponent T(MutableComponent var0, Component var1, String var2, String var3) {
      if (V != null && V.isEnabled()) {
         FakePayModule var4 = V;
         Minecraft var5 = getMc();
         if (var4 != null && var4.r && var4.T.getValue() && var5 != null && var5.level != null) {
            var4.L(var5.level);
            MutableComponent var6 = var1 == null ? hv.E(var0, var4.E.u(), var2, var3) : (hv.m(var1, var2, var3) ? hv.z(var0, var4.E.u()) : var0);
            if (var6 != var0 && !var4.x) {
               var4.x = (boolean)1;
               r4.V("FakePay", "Money sidebar row matched; local display override active");
            }

            return var6;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static Component k(Component var0) {
      if (V != null && V.isEnabled()) {
         FakePayModule var1 = V;
         Minecraft var2 = getMc();
         if (var0 != null && var1 != null && var1.r && var1.T.getValue() && var2 != null && var2.level != null) {
            var1.L(var2.level);
            Component var3 = hv.w(var0, var1.E.u());
            if (var3 != var0 && !var1.v) {
               var1.v = (boolean)1;
               r4.V("FakePay", "Tab footer money matched; shared local balance override active");
            }

            return var3;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static Component g(Component var0, boolean var1) {
      if (V != null && V.isEnabled()) {
         FakePayModule var2 = V;
         Minecraft var3 = getMc();
         if (var0 != null && var2 != null && var2.r && var2.T.getValue() && var3 != null && var3.level != null) {
            var2.L(var3.level);
            Component var4 = hv.l(var0, var2.E.u());
            if (var4 != var0 && (var1 ? !var2.a : !var2.l)) {
               if (var1) {
                  var2.a = (boolean)1;
               } else {
                  var2.l = (boolean)1;
               }

               r4.V("FakePay", "Balance response matched in {}; shared local balance override active", var1 ? "action bar" : "chat");
            }

            return var4;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static void f(Objective var0) {
      FakePayModule var1 = V;
      Minecraft var2 = getMc();
      if (var1 != null && var1.r && var1.T.getValue() && var2 != null && var2.level != null) {
         var1.L(var2.level);
         if (!var1.x && !var1.w) {
            long var3 = System.nanoTime();
            if (var1.F == 0L) {
               var1.F = var3;
            } else if (var3 - var1.F >= 3000000000L) {
               Scoreboard var5 = var0.getScoreboard();
               List var6 = var5.listPlayerScores(var0).stream().filter(var0x -> !var0x.isHidden()).limit(15L).toList();
               if (!var6.isEmpty()) {
                  var1.w = (boolean)1;
                  r4.m("FakePay", "No money row matched after 3 seconds; received sidebar snapshot follows");

                  for (PlayerScoreEntry var8 : var6) {
                     PlayerTeam var9 = var5.getPlayersTeam(var8.owner());
                     MutableComponent var10 = PlayerTeam.formatNameForTeam(var9, var8.ownerName());
                     MutableComponent var11 = var8.formatValue(var0.numberFormatOrDefault(StyledFormat.SIDEBAR_DEFAULT));
                     r4.V(
                        "FakePay",
                        "Sidebar owner={} team={} label={} value={}",
                        c(var8.owner()),
                        c(var9 == null ? "" : var9.getName()),
                        c(var10.getString()),
                        c(var11.getString())
                     );
                  }
               }
            }
         }
      }
   }

   private static String c(String var0) {
      StringBuilder var1 = new StringBuilder();
      var0.codePoints().limit(160L).forEach(var1x -> {
         if (var1x >= 32 && var1x <= 126) {
            var1.appendCodePoint(var1x);
         } else {
            var1.append(String.format("\\u{%04x}", var1x));
         }
      });
      return var1.toString();
   }

   public static boolean v(Packet<?> var0) {
      FakePayModule var1 = V;
      if (var1 != null && var1.r) {
         String var2 = uo.S(var0);
         return var2 == null ? false : x0.v(true, var2, var1x -> {
            Minecraft var2x = getMc();
            if (var2x != null) {
               ClientLevel var3 = var2x.level;
               LocalPlayer var4 = var2x.player;
               var2x.execute(() -> {
                  if (var3 != null && var2x.level == var3 && var2x.player == var4 && var1.r) {
                     try {
                        var1.X(var1x);
                     } catch (RuntimeException var6) {
                        y(var6);
                     }
                  }
               });
               r4.g("FakePay", "Final outgoing payment packet blocked");
            }
         }, FakePayModule::y);
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      this.i();
      r4.V("FakePay", "Local /pay simulation active; valid and invalid payment attempts are blocked");
   }

   @Override
   public void onDisable() {
      this.i();
      if (!this.isEnabled()) {
         this.r = (boolean)0;
      }

      r4.g("FakePay", "Runtime released; command guard follows enabled preference");
   }
}

package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.hn;
import com.larpclient.k_;
import com.larpclient.mt;
import com.larpclient.tx;
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
   private static volatile FakePayModule Z;
   private volatile boolean n;
   private final hn O = new hn();
   private Object t;
   private boolean T;
   private boolean H;
   private boolean b;
   private boolean r;
   private long s;
   private boolean v;
   private final dh Y = this.addSetting(new dh("Spoof Money", "Replace sidebar, tab and /bal displays with your local fake balance", false));
   private final d9 j = this.addSetting(new d9("Fake Balance", "Starting fake money; click the value to enter an amount", 1000000.0, 0.0, 1.0E9, 0.01) {
      @Override
      public String getValueAsString() {
         return BigDecimal.valueOf(this.P()).stripTrailingZeros().toPlainString();
      }
   });
   private final d9 k = this.addSetting(new d9("Balance Billions", "Whole billions in the submitted balance", 0.0, 0.0, 10000.0, 1.0));
   private final dh L = this.addSetting(new dh("Deduct Payments", "Subtract successful fake payments and reject amounts above the fake balance", true));
   private final dh e = this.addSetting(new dh("Action Bar", "Also show the local payment above the hotbar", true));
   private final dh q = this.addSetting(new dh("Sound", "Play a local payment confirmation ding", true));
   private final dh l = this.addSetting(new dh("Hide Errors", "Hide local feedback for invalid /pay attempts and insufficient fake funds", false));

   public FakePayModule() {
      super("FakePay", "Show fake local /pay messages", Category.PLAYER, -1);
      Z = this;
      this.j.H(this.Y::P).h(var1 -> this.W());
      this.k.H(() -> false).h(var1 -> this.W());
      this.L.H(this.Y::P);
      this.Y.h(var1 -> this.W());
      ClientSendMessageEvents.ALLOW_COMMAND.register((AllowCommand)var1 -> !this.U(var1));
      ClientSendMessageEvents.ALLOW_CHAT.register((AllowChat)var1 -> !var1.strip().startsWith("/") || !this.U(var1));
      ClientReceiveMessageEvents.MODIFY_GAME.register(FakePayModule::V);
   }

   private boolean U(String var1) {
      return k_.X(this.n, var1, this::O, FakePayModule::Y);
   }

   @Override
   public void enable() {
      this.n = (boolean)1;
      super.enable();
      this.n = this.isEnabled();
   }

   @Override
   public void disable() {
      this.n = (boolean)0;
      this.W();
      super.disable();
   }

   private static void Y(RuntimeException var0) {
      mt.P("FakePay", "Local simulation failed; payment remains blocked", var0);
   }

   private void O(k_.t var1) {
      Minecraft var2 = getMc();
      if (var2 != null && var2.level != null && var2.player != null) {
         if (!var1.valid()) {
            Component var5 = this.V(var1.error());
            if (var5 != null) {
               var2.gui.chatListener().handleSystemMessage(var5, false);
            }

            mt.U("FakePay", "Invalid payment attempt consumed locally");
         } else {
            if (this.Y.P() && this.L.P()) {
               this.y(var2.level);
               if (!this.O.r(var1.amount())) {
                  Component var4 = this.V("Not enough fake money. Balance: $ " + k_.D(this.O.R()));
                  if (var4 != null) {
                     var2.gui.chatListener().handleSystemMessage(var4, false);
                  }

                  mt.U("FakePay", "Payment rejected locally: insufficient fake balance");
                  return;
               }

               mt.U("FakePay", "Local fake balance after payment: {}", this.O.R());
            }

            Component var3 = K(var1);
            var2.gui.chatListener().handleSystemMessage(var3, false);
            if (this.e.P()) {
               var2.player.sendOverlayMessage(var3);
            }

            if (this.q.P()) {
               var2.player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 1.0F);
            }

            mt.U("FakePay", "Simulated payment shown locally; nothing sent");
         }
      }
   }

   public static Component K(k_.t var0) {
      if (!var0.valid()) {
         throw new IllegalArgumentException("Invalid simulated payment");
      } else {
         return Component.literal("You paid " + var0.player() + " ")
            .withStyle(var0x -> var0x.withColor(16777215))
            .append(Component.literal("$").withStyle(var0x -> var0x.withColor(64512)))
            .append(Component.literal(" " + k_.D(var0.amount())).withStyle(var0x -> var0x.withColor(16777215)));
      }
   }

   private void W() {
      this.t = null;
      this.T = (boolean)0;
      this.H = (boolean)0;
      this.b = (boolean)0;
      this.r = (boolean)0;
      this.s = 0L;
      this.v = (boolean)0;
      this.O.R(this.D());
   }

   private void y(Object var1) {
      if (this.t != var1) {
         this.O.R(this.D());
         this.t = var1;
         this.T = (boolean)0;
         this.H = (boolean)0;
         this.b = (boolean)0;
         this.r = (boolean)0;
         this.s = 0L;
         this.v = (boolean)0;
      }
   }

   public d9 p() {
      return this.j;
   }

   private BigDecimal D() {
      return BigDecimal.valueOf(this.k.P()).scaleByPowerOfTen(9).add(BigDecimal.valueOf(this.j.P())).min(hn.W);
   }

   public String C() {
      return this.D().stripTrailingZeros().toPlainString();
   }

   public static void Z(JsonObject var0) {
      if (var0.has("Fake Balance") && !var0.has("Balance Billions")) {
         var0.addProperty("Balance Billions", 0);
      }
   }

   public String j(String var1) {
      try {
         BigDecimal var2 = hn.o(var1);
         BigDecimal var3 = BigDecimal.valueOf(1000000000L);
         this.j.o(var2.remainder(var3).doubleValue());
         this.k.o(var2.divideToIntegralValue(var3).doubleValue());
         return "";
      } catch (IllegalArgumentException var4) {
         return var4.getMessage();
      }
   }

   public String L() {
      Minecraft var1 = getMc();
      if (this.n && this.Y.P() && var1 != null && var1.level != null) {
         this.y(var1.level);
      }

      return "Balance: $ " + k_.D(this.t == null ? this.D() : this.O.R());
   }

   public Component V(String var1) {
      return this.l.P() ? null : Component.literal("[Larp/FakePay] " + var1).withStyle(var0 -> var0.withColor(15834027));
   }

   public static MutableComponent D(MutableComponent var0, Component var1) {
      return Z != null && Z.isEnabled() ? X(var0, var1, "", "") : var0;
   }

   public static MutableComponent X(MutableComponent var0, Component var1, String var2, String var3) {
      if (Z != null && Z.isEnabled()) {
         FakePayModule var4 = Z;
         Minecraft var5 = getMc();
         if (var4 != null && var4.n && var4.Y.P() && var5 != null && var5.level != null) {
            var4.y(var5.level);
            MutableComponent var6 = var1 == null ? hn.G(var0, var4.O.R(), var2, var3) : (hn.K(var1, var2, var3) ? hn.z(var0, var4.O.R()) : var0);
            if (var6 != var0 && !var4.T) {
               var4.T = (boolean)1;
               mt.K("FakePay", "Money sidebar row matched; local display override active");
            }

            return var6;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static Component h(Component var0) {
      if (Z != null && Z.isEnabled()) {
         FakePayModule var1 = Z;
         Minecraft var2 = getMc();
         if (var0 != null && var1 != null && var1.n && var1.Y.P() && var2 != null && var2.level != null) {
            var1.y(var2.level);
            Component var3 = hn.i(var0, var1.O.R());
            if (var3 != var0 && !var1.H) {
               var1.H = (boolean)1;
               mt.K("FakePay", "Tab footer money matched; shared local balance override active");
            }

            return var3;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static Component V(Component var0, boolean var1) {
      if (Z != null && Z.isEnabled()) {
         FakePayModule var2 = Z;
         Minecraft var3 = getMc();
         if (var0 != null && var2 != null && var2.n && var2.Y.P() && var3 != null && var3.level != null) {
            var2.y(var3.level);
            Component var4 = hn.Y(var0, var2.O.R());
            if (var4 != var0 && (var1 ? !var2.r : !var2.b)) {
               if (var1) {
                  var2.r = (boolean)1;
               } else {
                  var2.b = (boolean)1;
               }

               mt.K("FakePay", "Balance response matched in {}; shared local balance override active", var1 ? "action bar" : "chat");
            }

            return var4;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static void I(Objective var0) {
      FakePayModule var1 = Z;
      Minecraft var2 = getMc();
      if (var1 != null && var1.n && var1.Y.P() && var2 != null && var2.level != null) {
         var1.y(var2.level);
         if (!var1.T && !var1.v) {
            long var3 = System.nanoTime();
            if (var1.s == 0L) {
               var1.s = var3;
            } else if (var3 - var1.s >= 3000000000L) {
               Scoreboard var5 = var0.getScoreboard();
               List var6 = var5.listPlayerScores(var0).stream().filter(var0x -> !var0x.isHidden()).limit(15L).toList();
               if (!var6.isEmpty()) {
                  var1.v = (boolean)1;
                  mt.T("FakePay", "No money row matched after 3 seconds; received sidebar snapshot follows");

                  for (PlayerScoreEntry var8 : var6) {
                     PlayerTeam var9 = var5.getPlayersTeam(var8.owner());
                     MutableComponent var10 = PlayerTeam.formatNameForTeam(var9, var8.ownerName());
                     MutableComponent var11 = var8.formatValue(var0.numberFormatOrDefault(StyledFormat.SIDEBAR_DEFAULT));
                     mt.K(
                        "FakePay",
                        "Sidebar owner={} team={} label={} value={}",
                        x(var8.owner()),
                        x(var9 == null ? "" : var9.getName()),
                        x(var10.getString()),
                        x(var11.getString())
                     );
                  }
               }
            }
         }
      }
   }

   private static String x(String var0) {
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

   public static boolean V(Packet<?> var0) {
      FakePayModule var1 = Z;
      if (var1 != null && var1.n) {
         String var2 = tx.W(var0);
         return var2 == null ? false : k_.X(true, var2, var1x -> {
            Minecraft var2x = getMc();
            if (var2x != null) {
               ClientLevel var3 = var2x.level;
               LocalPlayer var4 = var2x.player;
               var2x.execute(() -> {
                  if (var3 != null && var2x.level == var3 && var2x.player == var4 && var1.n) {
                     try {
                        var1.O(var1x);
                     } catch (RuntimeException var6) {
                        Y(var6);
                     }
                  }
               });
               mt.U("FakePay", "Final outgoing payment packet blocked");
            }
         }, FakePayModule::Y);
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      this.W();
      mt.K("FakePay", "Local /pay simulation active; valid and invalid payment attempts are blocked");
   }

   @Override
   public void onDisable() {
      this.W();
      if (!this.isEnabled()) {
         this.n = (boolean)0;
      }

      mt.U("FakePay", "Runtime released; command guard follows enabled preference");
   }
}

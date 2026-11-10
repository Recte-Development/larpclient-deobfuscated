package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.w7;
import com.larpclient.wm;
import com.larpclient.z7;
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
   private static volatile FakePayModule K;
   private volatile boolean A;
   private final z7 p = new z7();
   private Object I;
   private boolean a;
   private boolean P;
   private boolean H;
   private boolean U;
   private long L;
   private boolean f;
   private final s z = this.addSetting(new s("Spoof Money", "Replace sidebar, tab and /bal displays with your local fake balance", false));
   private final e k = this.addSetting(new e("Fake Balance", "Starting fake money; click the value to enter an amount", 1000000.0, 0.0, 1.0E9, 0.01) {
      @Override
      public String getValueAsString() {
         return BigDecimal.valueOf(this.getValue()).stripTrailingZeros().toPlainString();
      }
   });
   private final e t = this.addSetting(new e("Balance Billions", "Whole billions in the submitted balance", 0.0, 0.0, 10000.0, 1.0));
   private final s F = this.addSetting(new s("Deduct Payments", "Subtract successful fake payments and reject amounts above the fake balance", true));
   private final s l = this.addSetting(new s("Action Bar", "Also show the local payment above the hotbar", true));
   private final s O = this.addSetting(new s("Sound", "Play a local payment confirmation ding", true));
   private final s e = this.addSetting(new s("Hide Errors", "Hide local feedback for invalid /pay attempts and insufficient fake funds", false));

   public FakePayModule() {
      super("FakePay", "Show fake local /pay messages", Category.PLAYER, -1);
      K = this;
      this.k.P(this.z::getValue).o(var1 -> this.M());
      this.t.P(() -> false).o(var1 -> this.M());
      this.F.P(this.z::getValue);
      this.z.o(var1 -> this.M());
      ClientSendMessageEvents.ALLOW_COMMAND.register((AllowCommand)var1 -> !this.J(var1));
      ClientSendMessageEvents.ALLOW_CHAT.register((AllowChat)var1 -> !var1.strip().startsWith("/") || !this.J(var1));
      ClientReceiveMessageEvents.MODIFY_GAME.register(FakePayModule::g);
   }

   private boolean J(String var1) {
      return wm.x(this.K(), var1, this::S, FakePayModule::b);
   }

   private boolean K() {
      return this.A && !c.W(this);
   }

   @Override
   public void enable() {
      this.A = (boolean)1;
      super.enable();
      this.A = this.isEnabled();
   }

   @Override
   public void disable() {
      this.A = (boolean)0;
      this.M();
      super.disable();
   }

   private static void b(RuntimeException var0) {
      com.larpclient.h.m("FakePay", "Local simulation failed; payment remains blocked", var0);
   }

   private void S(wm.q var1) {
      Minecraft var2 = getMc();
      if (var2 != null && var2.level != null && var2.player != null) {
         if (!var1.valid()) {
            Component var5 = this.X(var1.error());
            if (var5 != null) {
               var2.gui.chatListener().handleSystemMessage(var5, false);
            }

            com.larpclient.h.U("FakePay", "Invalid payment attempt consumed locally");
         } else {
            if (this.z.getValue() && this.F.getValue()) {
               this.p(var2.level);
               if (!this.p.j(var1.amount())) {
                  Component var4 = this.X("Not enough fake money. Balance: $ " + wm.X(this.p.b()));
                  if (var4 != null) {
                     var2.gui.chatListener().handleSystemMessage(var4, false);
                  }

                  com.larpclient.h.U("FakePay", "Payment rejected locally: insufficient fake balance");
                  return;
               }

               com.larpclient.h.U("FakePay", "Local fake balance after payment: {}", this.p.b());
            }

            Component var3 = u(var1);
            var2.gui.chatListener().handleSystemMessage(var3, false);
            if (this.l.getValue()) {
               var2.player.sendOverlayMessage(var3);
            }

            if (this.O.getValue()) {
               var2.player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 1.0F);
            }

            com.larpclient.h.U("FakePay", "Simulated payment shown locally; nothing sent");
         }
      }
   }

   public static Component u(wm.q var0) {
      if (!var0.valid()) {
         throw new IllegalArgumentException("Invalid simulated payment");
      } else {
         return Component.literal("You paid " + var0.player() + " ")
            .withStyle(var0x -> var0x.withColor(16777215))
            .append(Component.literal("$").withStyle(var0x -> var0x.withColor(64512)))
            .append(Component.literal(" " + wm.X(var0.amount())).withStyle(var0x -> var0x.withColor(16777215)));
      }
   }

   private void M() {
      this.I = null;
      this.a = (boolean)0;
      this.P = (boolean)0;
      this.H = (boolean)0;
      this.U = (boolean)0;
      this.L = 0L;
      this.f = (boolean)0;
      this.p.v(this.N());
   }

   private void p(Object var1) {
      if (this.I != var1) {
         this.p.v(this.N());
         this.I = var1;
         this.a = (boolean)0;
         this.P = (boolean)0;
         this.H = (boolean)0;
         this.U = (boolean)0;
         this.L = 0L;
         this.f = (boolean)0;
      }
   }

   public e h() {
      return this.k;
   }

   private BigDecimal N() {
      return BigDecimal.valueOf(this.t.getValue()).scaleByPowerOfTen(9).add(BigDecimal.valueOf(this.k.getValue())).min(z7.u);
   }

   public String I() {
      return this.N().stripTrailingZeros().toPlainString();
   }

   public static void Z(JsonObject var0) {
      if (var0.has("Fake Balance") && !var0.has("Balance Billions")) {
         var0.addProperty("Balance Billions", 0);
      }
   }

   public String y(String var1) {
      try {
         BigDecimal var2 = z7.A(var1);
         BigDecimal var3 = BigDecimal.valueOf(1000000000L);
         this.k.X(var2.remainder(var3).doubleValue());
         this.t.X(var2.divideToIntegralValue(var3).doubleValue());
         return "";
      } catch (IllegalArgumentException var4) {
         return var4.getMessage();
      }
   }

   public String w() {
      Minecraft var1 = getMc();
      if (this.K() && this.z.getValue() && var1 != null && var1.level != null) {
         this.p(var1.level);
      }

      return "Balance: $ " + wm.X(this.I == null ? this.N() : this.p.b());
   }

   public Component X(String var1) {
      return this.e.getValue() ? null : Component.literal("[Larp/FakePay] " + var1).withStyle(var0 -> var0.withColor(15834027));
   }

   public static MutableComponent W(MutableComponent var0, Component var1) {
      return K != null && K.isEnabled() ? z(var0, var1, "", "") : var0;
   }

   public static MutableComponent z(MutableComponent var0, Component var1, String var2, String var3) {
      if (K != null && K.isEnabled()) {
         FakePayModule var4 = K;
         Minecraft var5 = getMc();
         if (var4 != null && var4.K() && var4.z.getValue() && var5 != null && var5.level != null) {
            var4.p(var5.level);
            MutableComponent var6 = var1 == null ? z7.w(var0, var4.p.b(), var2, var3) : (z7.H(var1, var2, var3) ? z7.f(var0, var4.p.b()) : var0);
            if (var6 != var0 && !var4.a) {
               var4.a = (boolean)1;
               com.larpclient.h.A("FakePay", "Money sidebar row matched; local display override active");
            }

            return var6;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static Component S(Component var0) {
      if (K != null && K.isEnabled()) {
         FakePayModule var1 = K;
         Minecraft var2 = getMc();
         if (var0 != null && var1 != null && var1.K() && var1.z.getValue() && var2 != null && var2.level != null) {
            var1.p(var2.level);
            Component var3 = z7.q(var0, var1.p.b());
            if (var3 != var0 && !var1.P) {
               var1.P = (boolean)1;
               com.larpclient.h.A("FakePay", "Tab footer money matched; shared local balance override active");
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
      if (K != null && K.isEnabled()) {
         FakePayModule var2 = K;
         Minecraft var3 = getMc();
         if (var0 != null && var2 != null && var2.K() && var2.z.getValue() && var3 != null && var3.level != null) {
            var2.p(var3.level);
            Component var4 = z7.c(var0, var2.p.b());
            if (var4 != var0 && (var1 ? !var2.U : !var2.H)) {
               if (var1) {
                  var2.U = (boolean)1;
               } else {
                  var2.H = (boolean)1;
               }

               com.larpclient.h.A("FakePay", "Balance response matched in {}; shared local balance override active", var1 ? "action bar" : "chat");
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
      FakePayModule var1 = K;
      Minecraft var2 = getMc();
      if (var1 != null && var1.K() && var1.z.getValue() && var2 != null && var2.level != null) {
         var1.p(var2.level);
         if (!var1.a && !var1.f) {
            long var3 = System.nanoTime();
            if (var1.L == 0L) {
               var1.L = var3;
            } else if (var3 - var1.L >= 3000000000L) {
               Scoreboard var5 = var0.getScoreboard();
               List var6 = var5.listPlayerScores(var0).stream().filter(var0x -> !var0x.isHidden()).limit(15L).toList();
               if (!var6.isEmpty()) {
                  var1.f = (boolean)1;
                  com.larpclient.h.g("FakePay", "No money row matched after 3 seconds; received sidebar snapshot follows");

                  for (PlayerScoreEntry var8 : var6) {
                     PlayerTeam var9 = var5.getPlayersTeam(var8.owner());
                     MutableComponent var10 = PlayerTeam.formatNameForTeam(var9, var8.ownerName());
                     MutableComponent var11 = var8.formatValue(var0.numberFormatOrDefault(StyledFormat.SIDEBAR_DEFAULT));
                     com.larpclient.h.A(
                        "FakePay",
                        "Sidebar owner={} team={} label={} value={}",
                        e(var8.owner()),
                        e(var9 == null ? "" : var9.getName()),
                        e(var10.getString()),
                        e(var11.getString())
                     );
                  }
               }
            }
         }
      }
   }

   private static String e(String var0) {
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

   public static boolean j(Packet<?> var0) {
      FakePayModule var1 = K;
      if (var1 != null && var1.K()) {
         String var2 = w7.j(var0);
         return var2 == null ? false : wm.x(true, var2, var1x -> {
            Minecraft var2x = getMc();
            if (var2x != null) {
               ClientLevel var3 = var2x.level;
               LocalPlayer var4 = var2x.player;
               var2x.execute(() -> {
                  if (var3 != null && var2x.level == var3 && var2x.player == var4 && var1.K()) {
                     try {
                        var1.S(var1x);
                     } catch (RuntimeException var6) {
                        b(var6);
                     }
                  }
               });
               com.larpclient.h.U("FakePay", "Final outgoing payment packet blocked");
            }
         }, FakePayModule::b);
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      this.M();
      com.larpclient.h.A("FakePay", "Local /pay simulation active; valid and invalid payment attempts are blocked");
   }

   @Override
   public void onDisable() {
      this.M();
      if (!this.isEnabled()) {
         this.A = (boolean)0;
      }

      com.larpclient.h.U("FakePay", "Runtime released; command guard follows enabled preference");
   }
}

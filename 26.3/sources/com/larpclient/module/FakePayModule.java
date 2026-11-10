package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient._q;
import com.larpclient.fy;
import com.larpclient.jw;
import com.larpclient.w_;
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
   private static volatile FakePayModule c;
   private volatile boolean O;
   private final fy H = new fy();
   private Object U;
   private boolean Q;
   private boolean e;
   private boolean u;
   private boolean R;
   private long j;
   private boolean h;
   private final m w = this.addSetting(new m("Spoof Money", "Replace sidebar, tab and /bal displays with your local fake balance", false));
   private final b i = this.addSetting(new b("Fake Balance", "Starting fake money; click the value to enter an amount", 1000000.0, 0.0, 1.0E9, 0.01) {
      @Override
      public String getValueAsString() {
         return BigDecimal.valueOf(this.getValue()).stripTrailingZeros().toPlainString();
      }
   });
   private final b V = this.addSetting(new b("Balance Billions", "Whole billions in the submitted balance", 0.0, 0.0, 10000.0, 1.0));
   private final m y = this.addSetting(new m("Deduct Payments", "Subtract successful fake payments and reject amounts above the fake balance", true));
   private final m m = this.addSetting(new m("Action Bar", "Also show the local payment above the hotbar", true));
   private final m S = this.addSetting(new m("Sound", "Play a local payment confirmation ding", true));
   private final m K = this.addSetting(new m("Hide Errors", "Hide local feedback for invalid /pay attempts and insufficient fake funds", false));

   public FakePayModule() {
      super("FakePay", "Show fake local /pay messages", Category.PLAYER, -1);
      c = this;
      this.i.a(this.w::getValue).w(var1 -> this.H());
      this.V.a(() -> false).w(var1 -> this.H());
      this.y.a(this.w::getValue);
      this.w.w(var1 -> this.H());
      ClientSendMessageEvents.ALLOW_COMMAND.register((AllowCommand)var1 -> !this.m(var1));
      ClientSendMessageEvents.ALLOW_CHAT.register((AllowChat)var1 -> !var1.strip().startsWith("/") || !this.m(var1));
      ClientReceiveMessageEvents.MODIFY_GAME.register(FakePayModule::B);
   }

   private boolean m(String var1) {
      return w_.m(this.D(), var1, this::C, FakePayModule::B);
   }

   private boolean D() {
      return this.O && !fj.r(this);
   }

   @Override
   public void enable() {
      this.O = (boolean)1;
      super.enable();
      this.O = this.isEnabled();
   }

   @Override
   public void disable() {
      this.O = (boolean)0;
      this.H();
      super.disable();
   }

   private static void B(RuntimeException var0) {
      _q.o("FakePay", "Local simulation failed; payment remains blocked", var0);
   }

   private void C(w_.e var1) {
      Minecraft var2 = getMc();
      if (var2 != null && var2.level != null && var2.player != null) {
         if (!var1.valid()) {
            Component var5 = this.L(var1.error());
            if (var5 != null) {
               var2.gui.chatListener().handleSystemMessage(var5, false);
            }

            _q.c("FakePay", "Invalid payment attempt consumed locally");
         } else {
            if (this.w.getValue() && this.y.getValue()) {
               this.I(var2.level);
               if (!this.H.V(var1.amount())) {
                  Component var4 = this.L("Not enough fake money. Balance: $ " + w_.U(this.H.J()));
                  if (var4 != null) {
                     var2.gui.chatListener().handleSystemMessage(var4, false);
                  }

                  _q.c("FakePay", "Payment rejected locally: insufficient fake balance");
                  return;
               }

               _q.c("FakePay", "Local fake balance after payment: {}", this.H.J());
            }

            Component var3 = c(var1);
            var2.gui.chatListener().handleSystemMessage(var3, false);
            if (this.m.getValue()) {
               var2.player.sendOverlayMessage(var3);
            }

            if (this.S.getValue()) {
               var2.player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 1.0F);
            }

            _q.c("FakePay", "Simulated payment shown locally; nothing sent");
         }
      }
   }

   public static Component c(w_.e var0) {
      if (!var0.valid()) {
         throw new IllegalArgumentException("Invalid simulated payment");
      } else {
         return Component.literal("You paid " + var0.player() + " ")
            .withStyle(var0x -> var0x.withColor(16777215))
            .append(Component.literal("$").withStyle(var0x -> var0x.withColor(64512)))
            .append(Component.literal(" " + w_.U(var0.amount())).withStyle(var0x -> var0x.withColor(16777215)));
      }
   }

   private void H() {
      this.U = null;
      this.Q = (boolean)0;
      this.e = (boolean)0;
      this.u = (boolean)0;
      this.R = (boolean)0;
      this.j = 0L;
      this.h = (boolean)0;
      this.H.H(this.g());
   }

   private void I(Object var1) {
      if (this.U != var1) {
         this.H.H(this.g());
         this.U = var1;
         this.Q = (boolean)0;
         this.e = (boolean)0;
         this.u = (boolean)0;
         this.R = (boolean)0;
         this.j = 0L;
         this.h = (boolean)0;
      }
   }

   public b O() {
      return this.i;
   }

   private BigDecimal g() {
      return BigDecimal.valueOf(this.V.getValue()).scaleByPowerOfTen(9).add(BigDecimal.valueOf(this.i.getValue())).min(fy.R);
   }

   public String a() {
      return this.g().stripTrailingZeros().toPlainString();
   }

   public static void T(JsonObject var0) {
      if (var0.has("Fake Balance") && !var0.has("Balance Billions")) {
         var0.addProperty("Balance Billions", 0);
      }
   }

   public String t(String var1) {
      try {
         BigDecimal var2 = fy.e(var1);
         BigDecimal var3 = BigDecimal.valueOf(1000000000L);
         this.i.O(var2.remainder(var3).doubleValue());
         this.V.O(var2.divideToIntegralValue(var3).doubleValue());
         return "";
      } catch (IllegalArgumentException var4) {
         return var4.getMessage();
      }
   }

   public String R() {
      Minecraft var1 = getMc();
      if (this.D() && this.w.getValue() && var1 != null && var1.level != null) {
         this.I(var1.level);
      }

      return "Balance: $ " + w_.U(this.U == null ? this.g() : this.H.J());
   }

   public Component L(String var1) {
      return this.K.getValue() ? null : Component.literal("[Larp/FakePay] " + var1).withStyle(var0 -> var0.withColor(15834027));
   }

   public static MutableComponent P(MutableComponent var0, Component var1) {
      return c != null && c.isEnabled() ? k(var0, var1, "", "") : var0;
   }

   public static MutableComponent k(MutableComponent var0, Component var1, String var2, String var3) {
      if (c != null && c.isEnabled()) {
         FakePayModule var4 = c;
         Minecraft var5 = getMc();
         if (var4 != null && var4.D() && var4.w.getValue() && var5 != null && var5.level != null) {
            var4.I(var5.level);
            MutableComponent var6 = var1 == null ? fy.N(var0, var4.H.J(), var2, var3) : (fy.Z(var1, var2, var3) ? fy.C(var0, var4.H.J()) : var0);
            if (var6 != var0 && !var4.Q) {
               var4.Q = (boolean)1;
               _q.z("FakePay", "Money sidebar row matched; local display override active");
            }

            return var6;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static Component L(Component var0) {
      if (c != null && c.isEnabled()) {
         FakePayModule var1 = c;
         Minecraft var2 = getMc();
         if (var0 != null && var1 != null && var1.D() && var1.w.getValue() && var2 != null && var2.level != null) {
            var1.I(var2.level);
            Component var3 = fy.f(var0, var1.H.J());
            if (var3 != var0 && !var1.e) {
               var1.e = (boolean)1;
               _q.z("FakePay", "Tab footer money matched; shared local balance override active");
            }

            return var3;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static Component B(Component var0, boolean var1) {
      if (c != null && c.isEnabled()) {
         FakePayModule var2 = c;
         Minecraft var3 = getMc();
         if (var0 != null && var2 != null && var2.D() && var2.w.getValue() && var3 != null && var3.level != null) {
            var2.I(var3.level);
            Component var4 = fy.w(var0, var2.H.J());
            if (var4 != var0 && (var1 ? !var2.R : !var2.u)) {
               if (var1) {
                  var2.R = (boolean)1;
               } else {
                  var2.u = (boolean)1;
               }

               _q.z("FakePay", "Balance response matched in {}; shared local balance override active", var1 ? "action bar" : "chat");
            }

            return var4;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static void Z(Objective var0) {
      FakePayModule var1 = c;
      Minecraft var2 = getMc();
      if (var1 != null && var1.D() && var1.w.getValue() && var2 != null && var2.level != null) {
         var1.I(var2.level);
         if (!var1.Q && !var1.h) {
            long var3 = System.nanoTime();
            if (var1.j == 0L) {
               var1.j = var3;
            } else if (var3 - var1.j >= 3000000000L) {
               Scoreboard var5 = var0.getScoreboard();
               List var6 = var5.listPlayerScores(var0).stream().filter(var0x -> !var0x.isHidden()).limit(15L).toList();
               if (!var6.isEmpty()) {
                  var1.h = (boolean)1;
                  _q.R("FakePay", "No money row matched after 3 seconds; received sidebar snapshot follows");

                  for (PlayerScoreEntry var8 : var6) {
                     PlayerTeam var9 = var5.getPlayersTeam(var8.owner());
                     MutableComponent var10 = PlayerTeam.formatNameForTeam(var9, var8.ownerName());
                     MutableComponent var11 = var8.formatValue(var0.numberFormatOrDefault(StyledFormat.SIDEBAR_DEFAULT));
                     _q.z(
                        "FakePay",
                        "Sidebar owner={} team={} label={} value={}",
                        G(var8.owner()),
                        G(var9 == null ? "" : var9.getName()),
                        G(var10.getString()),
                        G(var11.getString())
                     );
                  }
               }
            }
         }
      }
   }

   private static String G(String var0) {
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

   public static boolean O(Packet<?> var0) {
      FakePayModule var1 = c;
      if (var1 != null && var1.D()) {
         String var2 = jw.i(var0);
         return var2 == null ? false : w_.m(true, var2, var1x -> {
            Minecraft var2x = getMc();
            if (var2x != null) {
               ClientLevel var3 = var2x.level;
               LocalPlayer var4 = var2x.player;
               var2x.execute(() -> {
                  if (var3 != null && var2x.level == var3 && var2x.player == var4 && var1.D()) {
                     try {
                        var1.C(var1x);
                     } catch (RuntimeException var6) {
                        B(var6);
                     }
                  }
               });
               _q.c("FakePay", "Final outgoing payment packet blocked");
            }
         }, FakePayModule::B);
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      this.H();
      _q.z("FakePay", "Local /pay simulation active; valid and invalid payment attempts are blocked");
   }

   @Override
   public void onDisable() {
      this.H();
      if (!this.isEnabled()) {
         this.O = (boolean)0;
      }

      _q.c("FakePay", "Runtime released; command guard follows enabled preference");
   }
}

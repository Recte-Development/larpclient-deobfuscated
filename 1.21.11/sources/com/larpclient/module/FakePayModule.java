package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.q8;
import com.larpclient.qy;
import com.larpclient.uj;
import com.larpclient.y5;
import java.math.BigDecimal;
import java.util.List;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents.AllowChat;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents.AllowCommand;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_266;
import net.minecraft.class_268;
import net.minecraft.class_269;
import net.minecraft.class_310;
import net.minecraft.class_3417;
import net.minecraft.class_5250;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_9011;
import net.minecraft.class_9025;

public final class FakePayModule extends Module {
   private static volatile FakePayModule X;
   private volatile boolean E;
   private final uj o = new uj();
   private Object R;
   private boolean u;
   private boolean C;
   private boolean M;
   private boolean w;
   private long U;
   private boolean G;
   private final l3 O = this.addSetting(new l3("Spoof Money", "Replace sidebar, tab and /bal displays with your local fake balance", false));
   private final lo s = this.addSetting(new lo("Fake Balance", "Starting fake money; click the value to enter an amount", 1000000.0, 0.0, 1.0E9, 0.01) {
      @Override
      public String getValueAsString() {
         return BigDecimal.valueOf(this.getValue()).stripTrailingZeros().toPlainString();
      }
   });
   private final lo r = this.addSetting(new lo("Balance Billions", "Whole billions in the submitted balance", 0.0, 0.0, 10000.0, 1.0));
   private final l3 F = this.addSetting(new l3("Deduct Payments", "Subtract successful fake payments and reject amounts above the fake balance", true));
   private final l3 W = this.addSetting(new l3("Action Bar", "Also show the local payment above the hotbar", true));
   private final l3 d = this.addSetting(new l3("Sound", "Play a local payment confirmation ding", true));
   private final l3 e = this.addSetting(new l3("Hide Errors", "Hide local feedback for invalid /pay attempts and insufficient fake funds", false));

   public FakePayModule() {
      super("FakePay", "Show fake local /pay messages", Category.PLAYER, -1);
      X = this;
      this.s.O(this.O::getValue).a(var1 -> this.p());
      this.r.O(() -> false).a(var1 -> this.p());
      this.F.O(this.O::getValue);
      this.O.a(var1 -> this.p());
      ClientSendMessageEvents.ALLOW_COMMAND.register((AllowCommand)var1 -> !this.t(var1));
      ClientSendMessageEvents.ALLOW_CHAT.register((AllowChat)var1 -> !var1.strip().startsWith("/") || !this.t(var1));
      ClientReceiveMessageEvents.MODIFY_GAME.register(FakePayModule::X);
   }

   private boolean t(String var1) {
      return qy.X(this.C(), var1, this::v, FakePayModule::b);
   }

   private boolean C() {
      return this.E && !lf.Y(this);
   }

   @Override
   public void enable() {
      this.E = (boolean)1;
      super.enable();
      this.E = this.isEnabled();
   }

   @Override
   public void disable() {
      this.E = (boolean)0;
      this.p();
      super.disable();
   }

   private static void b(RuntimeException var0) {
      q8.u("FakePay", "Local simulation failed; payment remains blocked", var0);
   }

   private void v(qy.b var1) {
      class_310 var2 = getMc();
      if (var2 != null && var2.field_1687 != null && var2.field_1724 != null) {
         if (!var1.valid()) {
            class_2561 var5 = this.D(var1.error());
            if (var5 != null) {
               var2.method_44714().method_44736(var5, false);
            }

            q8.v("FakePay", "Invalid payment attempt consumed locally");
         } else {
            if (this.O.getValue() && this.F.getValue()) {
               this.c(var2.field_1687);
               if (!this.o.i(var1.amount())) {
                  class_2561 var4 = this.D("Not enough fake money. Balance: $ " + qy.r(this.o.Z()));
                  if (var4 != null) {
                     var2.method_44714().method_44736(var4, false);
                  }

                  q8.v("FakePay", "Payment rejected locally: insufficient fake balance");
                  return;
               }

               q8.v("FakePay", "Local fake balance after payment: {}", this.o.Z());
            }

            class_2561 var3 = S(var1);
            var2.method_44714().method_44736(var3, false);
            if (this.W.getValue()) {
               var2.field_1724.method_7353(var3, true);
            }

            if (this.d.getValue()) {
               var2.field_1724.method_5783(class_3417.field_15224, 1.0F, 1.0F);
            }

            q8.v("FakePay", "Simulated payment shown locally; nothing sent");
         }
      }
   }

   public static class_2561 S(qy.b var0) {
      if (!var0.valid()) {
         throw new IllegalArgumentException("Invalid simulated payment");
      } else {
         return class_2561.method_43470("You paid " + var0.player() + " ")
            .method_27694(var0x -> var0x.method_36139(16777215))
            .method_10852(class_2561.method_43470("$").method_27694(var0x -> var0x.method_36139(64512)))
            .method_10852(class_2561.method_43470(" " + qy.r(var0.amount())).method_27694(var0x -> var0x.method_36139(16777215)));
      }
   }

   private void p() {
      this.R = null;
      this.u = (boolean)0;
      this.C = (boolean)0;
      this.M = (boolean)0;
      this.w = (boolean)0;
      this.U = 0L;
      this.G = (boolean)0;
      this.o.c(this.p());
   }

   private void c(Object var1) {
      if (this.R != var1) {
         this.o.c(this.p());
         this.R = var1;
         this.u = (boolean)0;
         this.C = (boolean)0;
         this.M = (boolean)0;
         this.w = (boolean)0;
         this.U = 0L;
         this.G = (boolean)0;
      }
   }

   public lo Q() {
      return this.s;
   }

   private BigDecimal p() {
      return BigDecimal.valueOf(this.r.getValue()).scaleByPowerOfTen(9).add(BigDecimal.valueOf(this.s.getValue())).min(uj.y);
   }

   public String r() {
      return this.p().stripTrailingZeros().toPlainString();
   }

   public static void H(JsonObject var0) {
      if (var0.has("Fake Balance") && !var0.has("Balance Billions")) {
         var0.addProperty("Balance Billions", 0);
      }
   }

   public String s(String var1) {
      try {
         BigDecimal var2 = uj.Z(var1);
         BigDecimal var3 = BigDecimal.valueOf(1000000000L);
         this.s.q(var2.remainder(var3).doubleValue());
         this.r.q(var2.divideToIntegralValue(var3).doubleValue());
         return "";
      } catch (IllegalArgumentException var4) {
         return var4.getMessage();
      }
   }

   public String j() {
      class_310 var1 = getMc();
      if (this.C() && this.O.getValue() && var1 != null && var1.field_1687 != null) {
         this.c(var1.field_1687);
      }

      return "Balance: $ " + qy.r(this.R == null ? this.p() : this.o.Z());
   }

   public class_2561 D(String var1) {
      return this.e.getValue() ? null : class_2561.method_43470("[Larp/FakePay] " + var1).method_27694(var0 -> var0.method_36139(15834027));
   }

   public static class_5250 k(class_5250 var0, class_2561 var1) {
      return X != null && X.isEnabled() ? a(var0, var1, "", "") : var0;
   }

   public static class_5250 a(class_5250 var0, class_2561 var1, String var2, String var3) {
      if (X != null && X.isEnabled()) {
         FakePayModule var4 = X;
         class_310 var5 = getMc();
         if (var4 != null && var4.C() && var4.O.getValue() && var5 != null && var5.field_1687 != null) {
            var4.c(var5.field_1687);
            class_5250 var6 = var1 == null ? uj.h(var0, var4.o.Z(), var2, var3) : (uj.A(var1, var2, var3) ? uj.U(var0, var4.o.Z()) : var0);
            if (var6 != var0 && !var4.u) {
               var4.u = (boolean)1;
               q8.G("FakePay", "Money sidebar row matched; local display override active");
            }

            return var6;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static class_2561 x(class_2561 var0) {
      if (X != null && X.isEnabled()) {
         FakePayModule var1 = X;
         class_310 var2 = getMc();
         if (var0 != null && var1 != null && var1.C() && var1.O.getValue() && var2 != null && var2.field_1687 != null) {
            var1.c(var2.field_1687);
            class_2561 var3 = uj.h(var0, var1.o.Z());
            if (var3 != var0 && !var1.C) {
               var1.C = (boolean)1;
               q8.G("FakePay", "Tab footer money matched; shared local balance override active");
            }

            return var3;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static class_2561 X(class_2561 var0, boolean var1) {
      if (X != null && X.isEnabled()) {
         FakePayModule var2 = X;
         class_310 var3 = getMc();
         if (var0 != null && var2 != null && var2.C() && var2.O.getValue() && var3 != null && var3.field_1687 != null) {
            var2.c(var3.field_1687);
            class_2561 var4 = uj.a(var0, var2.o.Z());
            if (var4 != var0 && (var1 ? !var2.w : !var2.M)) {
               if (var1) {
                  var2.w = (boolean)1;
               } else {
                  var2.M = (boolean)1;
               }

               q8.G("FakePay", "Balance response matched in {}; shared local balance override active", var1 ? "action bar" : "chat");
            }

            return var4;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static void k(class_266 var0) {
      FakePayModule var1 = X;
      class_310 var2 = getMc();
      if (var1 != null && var1.C() && var1.O.getValue() && var2 != null && var2.field_1687 != null) {
         var1.c(var2.field_1687);
         if (!var1.u && !var1.G) {
            long var3 = System.nanoTime();
            if (var1.U == 0L) {
               var1.U = var3;
            } else if (var3 - var1.U >= 3000000000L) {
               class_269 var5 = var0.method_1117();
               List var6 = var5.method_1184(var0).stream().filter(var0x -> !var0x.method_55385()).limit(15L).toList();
               if (!var6.isEmpty()) {
                  var1.G = (boolean)1;
                  q8.L("FakePay", "No money row matched after 3 seconds; received sidebar snapshot follows");

                  for (class_9011 var8 : var6) {
                     class_268 var9 = var5.method_1164(var8.comp_2127());
                     class_5250 var10 = class_268.method_1142(var9, var8.method_55387());
                     class_5250 var11 = var8.method_55386(var0.method_55380(class_9025.field_47567));
                     q8.G(
                        "FakePay",
                        "Sidebar owner={} team={} label={} value={}",
                        L(var8.comp_2127()),
                        L(var9 == null ? "" : var9.method_1197()),
                        L(var10.getString()),
                        L(var11.getString())
                     );
                  }
               }
            }
         }
      }
   }

   private static String L(String var0) {
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

   public static boolean L(class_2596<?> var0) {
      FakePayModule var1 = X;
      if (var1 != null && var1.C()) {
         String var2 = y5.A(var0);
         return var2 == null ? false : qy.X(true, var2, var1x -> {
            class_310 var2x = getMc();
            if (var2x != null) {
               class_638 var3 = var2x.field_1687;
               class_746 var4 = var2x.field_1724;
               var2x.execute(() -> {
                  if (var3 != null && var2x.field_1687 == var3 && var2x.field_1724 == var4 && var1.C()) {
                     try {
                        var1.v(var1x);
                     } catch (RuntimeException var6) {
                        b(var6);
                     }
                  }
               });
               q8.v("FakePay", "Final outgoing payment packet blocked");
            }
         }, FakePayModule::b);
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      this.p();
      q8.G("FakePay", "Local /pay simulation active; valid and invalid payment attempts are blocked");
   }

   @Override
   public void onDisable() {
      this.p();
      if (!this.isEnabled()) {
         this.E = (boolean)0;
      }

      q8.v("FakePay", "Runtime released; command guard follows enabled preference");
   }
}

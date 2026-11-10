package com.larpclient;

import com.larpclient.account.MicrosoftAccounts;
import com.larpclient.module.AimAssistModule;
import com.larpclient.module.AutoPearlCatchModule;
import com.larpclient.module.AutoReconnectModule;
import com.larpclient.module.ClickPulseModule;
import com.larpclient.module.CustomCrosshairModule;
import com.larpclient.module.FreecamModule;
import com.larpclient.module.FreelookModule;
import com.larpclient.module.LungeSwapModule;
import com.larpclient.module.Module;
import com.larpclient.module.SwordModelsModule;
import com.larpclient.module.WTapModule;
import com.larpclient.module.WindChargeHopModule;
import com.larpclient.module.WorldMenuModule;
import com.larpclient.module.an;
import com.larpclient.update.UpdateRuntime;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_408;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_642;
import net.minecraft.class_304.class_11900;
import net.minecraft.class_3675.class_306;
import net.minecraft.class_3675.class_307;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LarpClient implements ClientModInitializer {
   public static final String s = "larp-client";
   public static final String z = sc.v;
   public static final Logger p = LoggerFactory.getLogger("larp-client");
   public static boolean q = (boolean)0;
   private static LarpClient P;
   private com.larpclient.module.lr e;
   private qk E;
   private y8 F;
   private k X;
   private class_304 S;
   private class_304 N;
   private final jj<Module> Y = new jj<>(349);
   private final jj<je.w> x = new jj<>(349);
   private boolean d = (boolean)0;
   private boolean G = (boolean)0;
   private volatile boolean V;
   public static final class_11900 J = class_11900.method_74698(class_2960.method_60655("larp-client", "main"));
   public static final class_11900 U = class_11900.method_74698(class_2960.method_60655("larp-client", "editing"));
   private int r = -1;
   private class_642 Z;
   private String c;
   private final com.larpclient.module.lf.v O = new com.larpclient.module.lf.v();

   public void onInitializeClient() {
      P = this;
      p.info("LarpClient v{} by Tobias - loading...", z);
      uf.b();
      this.e = new com.larpclient.module.lr();
      this.e.H();
      this.E = new qk();
      this.E.J();
      com.larpclient.module.lf.M(this.E.F().u);
      MicrosoftAccounts.INSTANCE.start(class_310.method_1551());
      UpdateRuntime.start();
      this.F = new y8(this.E.F().G4);
      this.X = new k();
      this.X.J();
      wt.T().J();
      an.k();
      com.larpclient.module.lm.T();
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var1 -> {
         this.F.close();
         uf.N();
         this.V = (boolean)1;
         this.I();
         this.E.r();
         MicrosoftAccounts.INSTANCE.stop();
         UpdateRuntime.stop();
      });
      qs.e();
      wc.f();
      this.S = KeyBindingHelper.registerKeyBinding(new class_304("key.larp-client.open_gui", class_307.field_1668, s2.m(344), J));
      this.N = KeyBindingHelper.registerKeyBinding(new class_304("key.larp-client.open_editor", class_307.field_1668, s2.m(-1), U));
      q8.G("Input", "Module shortcuts use ClickGUI/profile keybinds only; legacy Minecraft module shortcuts are ignored");
      ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var1, var2) -> {
         uf.K();
         if (!this.V) {
            var2.execute(() -> {
               if (!this.V && (var2.method_1562() == null || var2.method_1562() == var1)) {
                  this.I();
               }
            });
         }
      });
      ClientTickEvents.END_CLIENT_TICK.register(this::a);
      p.info("LarpClient v{} loaded! {} modules registered.", z, this.e.M().size());
      q8.G("Startup", "Press RIGHT SHIFT to open the GUI");
   }

   public void c(boolean var1) {
      if (this.F != null) {
         this.F.O(var1);
      }
   }

   private void p(class_310 var1) {
      if (this.F != null) {
         class_642 var2 = var1.method_1558();
         int var3 = var1.field_1687 != null ? 1 : 0;
         boolean var4 = var1.method_1496();
         String var5 = var2 == null ? null : var2.field_3761;
         int var6 = (var3 != 0 ? 1 : 0) | (var4 ? 2 : 0);
         if (var6 != this.r || var2 != this.Z || !Objects.equals(var5, this.c)) {
            this.r = var6;
            this.Z = var2;
            this.c = var5;
            this.F.z(y8.z((boolean)var3, var4, var2 != null && var2.method_52811(), var2 != null && var2.method_2994(), var5));
         }
      }
   }

   private void I() {
      this.Y.e();
      this.x.e();

      for (Module var2 : this.e.M()) {
         var2.suspendForDisconnect();
      }

      this.E.h();
   }

   public boolean z(int var1) {
      class_310 var2 = class_310.method_1551();
      class_437 var3 = var2.field_1755;
      int var4 = var2.method_18506() != null ? 1 : 0;
      if (!this.V && this.E != null && var4 == 0 && !WorldMenuModule.V() && com.larpclient.module.lf.D(var3)) {
         if (!this.O.u(var1, this.E.F().e, System.nanoTime() / 1000000L)) {
            return false;
         }

         this.V(!com.larpclient.module.lf.l());

         while (var2.field_1690.field_1845.method_1436()) {
         }

         return true;
      } else {
         this.O.u();
         return false;
      }
   }

   public void V(boolean var1) {
      if (var1 != com.larpclient.module.lf.l()) {
         this.e.x(var1);
         this.E.F().u = var1;
         this.E.g();
         this.Y.e();
         this.x.e();
         class_310 var2 = class_310.method_1551();
         class_437 var3 = var2.field_1755;
         if (!(var3 instanceof class_408) && !(var3 instanceof tk) && !(var3 instanceof tm)) {
            WorldMenuModule.H();
         } else {
            var2.method_1507(null);
         }

         if (var1) {
            wt.T().Q();
         } else {
            wt.T().m("Legit mode", false);
         }
      }
   }

   public void g(int var1) {
      g9.Y(var1);
      CustomCrosshairModule.X(var1);
      ClickPulseModule.n(var1);
      int var2 = s5.K(var1);
      if (var2 != -1) {
         if (WorldMenuModule.A()) {
            this.Y.h(var2);
            this.x.h(var2);
         } else {
            this.r(var2);
         }
      }
   }

   public void r(int var1) {
      class_310 var2 = class_310.method_1551();
      if (!this.V && var2.field_1724 != null && var2.field_1755 == null && var2.method_18506() == null && var2.method_1569() && !WorldMenuModule.V()) {
         this.Y.D(var1);
         this.x.D(var1);
         LungeSwapModule.T(var1);
         AutoPearlCatchModule.v(var1);
         WindChargeHopModule.J(var1);
         SwordModelsModule var3 = SwordModelsModule.X();
         if (var3 != null) {
            var3.o(var1);
         }
      } else {
         this.Y.h(var1);
         this.x.h(var1);
      }
   }

   private void a(class_310 var1) {
      FreecamModule.H();
      FreelookModule.n();

      for (Module var3 : this.e.M()) {
         var3.validateHeldBind();
      }

      this.e.Z();
      AutoReconnectModule.A(var1);
      AimAssistModule.t();
      WTapModule.K();
      k.c(var1);
      uf.i(var1);
      er.m();
      this.p(var1);
      c0.v(var1);
      ja.n.x();
      if (var1.field_1755 instanceof class_442) {
         q = (boolean)1;
      }

      if (!this.G && var1.field_1755 instanceof class_442) {
         this.G = (boolean)1;
      }

      if (this.G && var1.field_1755 instanceof class_442) {
      }

      this.Y.G();
      long var13 = var1.method_22683().method_4490();

      for (Module var5 : this.e.Y()) {
         int var6 = var5.getKeyBind();
         if (s5.A(var6)) {
            this.Y.x(var5, var6, s5.m(var13, var6));
         }
      }

      List var14 = this.Y.Y();
      this.x.G();

      for (je.w var17 : this.E.F().j.i(this.e.Y())) {
         this.x.x(var17, var17.code(), s5.m(var13, var17.code()));
      }

      List var16 = this.x.Y();
      int var18 = var1.field_1755 == null && var1.method_18506() == null && var1.method_1569() && !WorldMenuModule.V() ? 1 : 0;
      boolean var7 = this.S.method_1436();
      byte var8 = 0;

      while (this.N.method_1436()) {
         var8 = 1;
      }

      if (var1.field_1724 != null) {
         if (var8 != 0 && var18 != 0 && !var7 && var1.field_1687 != null) {
            var1.method_1507(new tj(null));
         } else {
            if (!this.d) {
               this.d = (boolean)1;
               var1.field_1690.method_42454().method_41748(0.0);
               var1.field_1690.method_42448().method_41748(false);
               q8.G("Startup", "Applied Larp defaults: dynamic FOV off, view bobbing off");
            }

            if (var7) {
               if (var1.field_1755 instanceof tk var9) {
                  var9.method_25419();
               } else if (var1.field_1755 instanceof tm var10) {
                  var10.method_25419();
               } else if (!WorldMenuModule.H()) {
                  var1.method_1507(tm.y(null));
               }
            }

            if (var18 != 0 && !var7 && var1.field_1755 == null) {
               for (Module var21 : var14) {
                  if (var21 instanceof FreecamModule var24 && var24.D()) {
                     if (!var24.I()) {
                        continue;
                     }
                  } else if (var21 instanceof FreelookModule var12 && var12.E()) {
                     if (!var12.V()) {
                        continue;
                     }
                  } else if (var21.holdsKeybind()) {
                     if (!var21.activateHeldBind()) {
                        continue;
                     }
                  } else {
                     var21.toggle();
                  }

                  wt.T().m(var21.getDisplayName(), var21.isEnabled());
               }

               for (je.w var22 : var16) {
                  z(var22);
               }
            }
         }
      }
   }

   private static void z(je.w var0) {
      com.larpclient.module.lb var1 = var0.setting();
      String var2 = var0.module().getDisplayName() + " " + var1.getDisplayName();
      if (!var1.B()) {
         q8.v("Input", "Setting shortcut {} skipped while the setting is hidden", var2);
      } else {
         try {
            if (var1 instanceof com.larpclient.module.l3 var3) {
               var3.h();
               wt.T().m(var2, var3.getValue());
            } else if (var1 instanceof com.larpclient.module.li var4) {
               var4.P();
               wt.T().z(var4.getModeLabel(var4.getValue()), var2, wt.s.R, 2000L);
            }

            q8.v("Input", "Setting shortcut changed {} to {}", var2, var1.getValueAsString());
         } catch (RuntimeException var5) {
            q8.L("Input", "Setting shortcut for {} was rejected: {}", var2, var5.toString());
         }
      }
   }

   public static LarpClient b() {
      return P;
   }

   public com.larpclient.module.lr G() {
      return this.e;
   }

   public qk e() {
      return this.E;
   }

   public int V() {
      if (this.S == null) {
         return 344;
      }

      class_306 var1 = KeyBindingHelper.getBoundKeyOf(this.S);

      return switch (var1.method_1442()) {
         case field_1668 -> s2.H(var1.method_1444());
         case field_1672 -> s5.K(s2.I(var1.method_1444()));
         default -> -1;
      };
   }

   public void n(int var1) {
      if (this.S != null && var1 >= -1 && var1 <= 348) {
         this.S.method_1422(s5.X(var1) ? class_307.field_1672.method_1447(s2.t(s5.W(var1))) : class_307.field_1668.method_1447(s2.m(var1)));
         this.S.method_23481(false);
         class_304.method_1426();

         while (this.S.method_1436()) {
         }

         class_310.method_1551().field_1690.method_1640();
         q8.G("Input", "ClickUI shortcut changed to {}", this.S.method_16007().getString());
      }
   }
}

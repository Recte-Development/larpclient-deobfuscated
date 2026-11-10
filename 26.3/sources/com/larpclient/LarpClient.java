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
import com.larpclient.module.kl;
import com.larpclient.update.UpdateRuntime;
import com.mojang.blaze3d.platform.InputConstants.Key;
import com.mojang.blaze3d.platform.InputConstants.Type;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping.Category;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LarpClient implements ClientModInitializer {
   public static final String g = "larp-client";
   public static final String G = a0.r;
   public static final Logger L = LoggerFactory.getLogger("larp-client");
   public static boolean W = (boolean)0;
   private static LarpClient r;
   private kl x;
   private aq w;
   private f2 F;
   private dk X;
   private KeyMapping k;
   private KeyMapping T;
   private final jm<Module> o = new jm<>(349);
   private final jm<b1.x> U = new jm<>(349);
   private boolean b = (boolean)0;
   private boolean m = (boolean)0;
   private volatile boolean N;
   public static final Category j = Category.register(Identifier.fromNamespaceAndPath("larp-client", "main"));
   public static final Category c = Category.register(Identifier.fromNamespaceAndPath("larp-client", "editing"));
   private int E = -1;
   private ServerData R;
   private String u;
   private final com.larpclient.module.fj.b P = new com.larpclient.module.fj.b();

   public void onInitializeClient() {
      r = this;
      L.info("LarpClient v{} by Tobias - loading...", G);
      vc.d();
      this.x = new kl();
      this.x.g();
      this.w = new aq();
      this.w.p();
      com.larpclient.module.fj.l(this.w.k().G);
      MicrosoftAccounts.INSTANCE.start(Minecraft.getInstance());
      UpdateRuntime.start();
      this.F = new f2(this.w.k().f);
      this.X = new dk();
      this.X.G();
      ib.L().w();
      com.larpclient.module.s.e();
      com.larpclient.module.z.d();
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var1 -> {
         this.F.close();
         vc.u();
         this.N = (boolean)1;
         this.d();
         this.w.M();
         MicrosoftAccounts.INSTANCE.stop();
         UpdateRuntime.stop();
      });
      wp.T();
      at.Q();
      this.k = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.larp-client.open_gui", Type.KEYBOARD, j2.X(344), j));
      this.T = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.larp-client.open_editor", Type.KEYBOARD, j2.X(-1), c));
      _q.z("Input", "Module shortcuts use ClickGUI/profile keybinds only; legacy Minecraft module shortcuts are ignored");
      ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var1, var2) -> {
         vc.i();
         if (!this.N) {
            var2.execute(() -> {
               if (!this.N && (var2.getConnection() == null || var2.getConnection() == var1)) {
                  this.d();
               }
            });
         }
      });
      ClientTickEvents.END_CLIENT_TICK.register(this::i);
      L.info("LarpClient v{} loaded! {} modules registered.", G, this.x.d().size());
      _q.z("Startup", "Press RIGHT SHIFT to open the GUI");
   }

   public void j(boolean var1) {
      if (this.F != null) {
         this.F.X(var1);
      }
   }

   private void l(Minecraft var1) {
      if (this.F != null) {
         ServerData var2 = var1.getCurrentServer();
         int var3 = var1.level != null ? 1 : 0;
         boolean var4 = var1.hasSingleplayerServer();
         String var5 = var2 == null ? null : var2.ip;
         int var6 = (var3 != 0 ? 1 : 0) | (var4 ? 2 : 0);
         if (var6 != this.E || var2 != this.R || !Objects.equals(var5, this.u)) {
            this.E = var6;
            this.R = var2;
            this.u = var5;
            this.F.i(f2.e((boolean)var3, var4, var2 != null && var2.isRealm(), var2 != null && var2.isLan(), var5));
         }
      }
   }

   private void d() {
      this.o.z();
      this.U.z();

      for (Module var2 : this.x.d()) {
         var2.suspendForDisconnect();
      }

      this.w.g();
   }

   public boolean Z(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      Screen var3 = var2.gui.screen();
      int var4 = var2.gui.overlay() != null ? 1 : 0;
      if (!this.N && this.w != null && var4 == 0 && !WorldMenuModule.Y() && com.larpclient.module.fj.G(var3)) {
         if (!this.P.T(var1, this.w.k().D, System.nanoTime() / 1000000L)) {
            return false;
         }

         this.c(!com.larpclient.module.fj.r());

         while (var2.options.keyCommand.consumeClick()) {
         }

         return true;
      } else {
         this.P.j();
         return false;
      }
   }

   public void c(boolean var1) {
      if (var1 != com.larpclient.module.fj.r()) {
         this.x.i(var1);
         this.w.k().G = var1;
         this.w.c();
         this.o.z();
         this.U.z();
         Minecraft var2 = Minecraft.getInstance();
         Screen var3 = var2.gui.screen();
         if (!(var3 instanceof ChatScreen) && !(var3 instanceof f8) && !(var3 instanceof fj)) {
            WorldMenuModule.a();
         } else {
            var2.gui.setScreen(null);
         }

         if (var1) {
            ib.L().R();
         } else {
            ib.L().z("Legit mode", false);
         }
      }
   }

   public void m(int var1) {
      _k.U(var1);
      CustomCrosshairModule.k(var1);
      ClickPulseModule.Z(var1);
      int var2 = p4.f(var1);
      if (var2 != -1) {
         if (WorldMenuModule.i()) {
            this.o.y(var2);
            this.U.y(var2);
         } else {
            this.a(var2);
         }
      }
   }

   public void a(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (!this.N && var2.player != null && var2.gui.screen() == null && var2.gui.overlay() == null && var2.isWindowActive() && !WorldMenuModule.Y()) {
         this.o.l(var1);
         this.U.l(var1);
         LungeSwapModule.D(var1);
         AutoPearlCatchModule.p(var1);
         WindChargeHopModule.t(var1);
         SwordModelsModule var3 = SwordModelsModule.p();
         if (var3 != null) {
            var3.V(var1);
         }
      } else {
         this.o.y(var1);
         this.U.y(var1);
      }
   }

   private void i(Minecraft var1) {
      FreecamModule.V();
      FreelookModule.d();

      for (Module var3 : this.x.d()) {
         var3.validateHeldBind();
      }

      this.x.E();
      AutoReconnectModule.h(var1);
      AimAssistModule.d();
      WTapModule.c();
      dk.B(var1);
      vc.S(var1);
      dr.h();
      this.l(var1);
      com.larpclient.g.T(var1);
      bl.F.S();
      if (var1.gui.screen() instanceof TitleScreen) {
         W = (boolean)1;
      }

      if (!this.m && var1.gui.screen() instanceof TitleScreen) {
         this.m = (boolean)1;
      }

      if (this.m && var1.gui.screen() instanceof TitleScreen) {
      }

      this.o.c();
      long var13 = var1.getWindow().handle();

      for (Module var5 : this.x.a()) {
         int var6 = var5.getKeyBind();
         if (p4.g(var6)) {
            this.o.f(var5, var6, p4.Y(var13, var6));
         }
      }

      List var14 = this.o.a();
      this.U.c();

      for (b1.x var17 : this.w.k().Q.N(this.x.a())) {
         this.U.f(var17, var17.code(), p4.Y(var13, var17.code()));
      }

      List var16 = this.U.a();
      int var18 = var1.gui.screen() == null && var1.gui.overlay() == null && var1.isWindowActive() && !WorldMenuModule.Y() ? 1 : 0;
      boolean var7 = this.k.consumeClick();
      byte var8 = 0;

      while (this.T.consumeClick()) {
         var8 = 1;
      }

      if (var1.player != null) {
         if (var8 != 0 && var18 != 0 && !var7 && var1.level != null) {
            var1.gui.setScreen(new fv(null));
         } else {
            if (!this.b) {
               this.b = (boolean)1;
               var1.options.fovEffectScale().set(0.0);
               var1.options.bobView().set(false);
               _q.z("Startup", "Applied Larp defaults: dynamic FOV off, view bobbing off");
            }

            if (var7) {
               if (var1.gui.screen() instanceof f8 var9) {
                  var9.onClose();
               } else if (var1.gui.screen() instanceof fj var10) {
                  var10.onClose();
               } else if (!WorldMenuModule.a()) {
                  var1.gui.setScreen(fj.I(null));
               }
            }

            if (var18 != 0 && !var7 && var1.gui.screen() == null) {
               for (Module var21 : var14) {
                  if (var21 instanceof FreecamModule var24 && var24.E()) {
                     if (!var24.O()) {
                        continue;
                     }
                  } else if (var21 instanceof FreelookModule var12 && var12.J()) {
                     if (!var12.k()) {
                        continue;
                     }
                  } else if (var21.holdsKeybind()) {
                     if (!var21.activateHeldBind()) {
                        continue;
                     }
                  } else {
                     var21.toggle();
                  }

                  ib.L().z(var21.getDisplayName(), var21.isEnabled());
               }

               for (b1.x var22 : var16) {
                  q(var22);
               }
            }
         }
      }
   }

   private static void q(b1.x var0) {
      com.larpclient.module.r var1 = var0.setting();
      String var2 = var0.module().getDisplayName() + " " + var1.getDisplayName();
      if (!var1.j()) {
         _q.c("Input", "Setting shortcut {} skipped while the setting is hidden", var2);
      } else {
         try {
            if (var1 instanceof com.larpclient.module.m var3) {
               var3.q();
               ib.L().z(var2, var3.getValue());
            } else if (var1 instanceof com.larpclient.module.o var4) {
               var4.C();
               ib.L().l(var4.getModeLabel(var4.getValue()), var2, ib.v.f, 2000L);
            }

            _q.c("Input", "Setting shortcut changed {} to {}", var2, var1.getValueAsString());
         } catch (RuntimeException var5) {
            _q.R("Input", "Setting shortcut for {} was rejected: {}", var2, var5.toString());
         }
      }
   }

   public static LarpClient N() {
      return r;
   }

   public kl j() {
      return this.x;
   }

   public aq h() {
      return this.w;
   }

   public int t() {
      if (this.k == null) {
         return 344;
      }

      Key var1 = KeyMappingHelper.getBoundKeyOf(this.k);

      return switch (var1.getType()) {
         case KEYBOARD -> j2.S(var1.getValue());
         case MOUSE -> p4.f(j2.b(var1.getValue()));
         default -> -1;
      };
   }

   public void M(int var1) {
      if (this.k != null && var1 >= -1 && var1 <= 348) {
         this.k.setKey(p4.h(var1) ? Type.MOUSE.getOrCreate(j2.K(p4.y(var1))) : Type.KEYBOARD.getOrCreate(j2.X(var1)));
         this.k.setDown(false);
         KeyMapping.resetMapping();

         while (this.k.consumeClick()) {
         }

         Minecraft.getInstance().options.save();
         _q.z("Input", "ClickUI shortcut changed to {}", this.k.getTranslatedKeyMessage().getString());
      }
   }
}

package com.larpclient;

import com.larpclient.account.MicrosoftAccounts;
import com.larpclient.module.AimAssistModule;
import com.larpclient.module.AutoPearlCatchModule;
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
import com.larpclient.module.d4;
import com.larpclient.module.df;
import com.larpclient.module.dh;
import com.larpclient.module.dz;
import com.larpclient.update.UpdateRuntime;
import com.mojang.blaze3d.platform.InputConstants.Key;
import com.mojang.blaze3d.platform.InputConstants.Type;
import java.util.List;
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
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LarpClient implements ClientModInitializer {
   public static final String Z = "larp-client";
   public static final String t = zq.x;
   public static final Logger U = LoggerFactory.getLogger("larp-client");
   public static boolean h = (boolean)0;
   private static LarpClient L;
   private dz D;
   private w9 f;
   private _t Q;
   private mq P;
   private KeyMapping u;
   private KeyMapping N;
   private final cq<Module> r = new cq<>(349);
   private final cq<um.w> d = new cq<>(349);
   private boolean w = (boolean)0;
   private boolean W = (boolean)0;
   private volatile boolean s;
   public static final Category c = Category.register(Identifier.fromNamespaceAndPath("larp-client", "main"));
   public static final Category m = Category.register(Identifier.fromNamespaceAndPath("larp-client", "editing"));

   public void onInitializeClient() {
      L = this;
      U.info("LarpClient v{} by Tobias - loading...", t);
      e.B();
      this.D = new dz();
      this.D.w();
      this.f = new w9();
      this.f.g();
      MicrosoftAccounts.INSTANCE.start(Minecraft.getInstance());
      UpdateRuntime.start();
      this.Q = new _t(this.f.t().d);
      this.P = new mq();
      this.P.G();
      oj.K().p();
      d4.F();
      com.larpclient.module.c.N();
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var1 -> {
         this.Q.close();
         e.g();
         this.s = (boolean)1;
         this.C();
         this.f.y();
         MicrosoftAccounts.INSTANCE.stop();
         UpdateRuntime.stop();
      });
      com.larpclient.h.s();
      k.i();
      this.u = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.larp-client.open_gui", Type.KEYBOARD, mo.f(344), c));
      this.N = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.larp-client.open_editor", Type.KEYBOARD, mo.f(-1), m));
      mt.K("Input", "Module shortcuts use ClickGUI/profile keybinds only; legacy Minecraft module shortcuts are ignored");
      ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var1, var2) -> {
         e.J();
         if (!this.s) {
            var2.execute(() -> {
               if (!this.s && (var2.getConnection() == null || var2.getConnection() == var1)) {
                  this.C();
               }
            });
         }
      });
      ClientTickEvents.END_CLIENT_TICK.register(this::L);
      U.info("LarpClient v{} loaded! {} modules registered.", t, this.D.C().size());
      U.info("Press RIGHT SHIFT to open the GUI.");
   }

   public void a(boolean var1) {
      if (this.Q != null) {
         this.Q.K(var1);
      }
   }

   private void C() {
      this.r.u();
      this.d.u();

      for (Module var2 : this.D.C()) {
         var2.suspendForDisconnect();
      }

      this.f.b();
   }

   public void B(int var1) {
      kp.j(var1);
      CustomCrosshairModule.E(var1);
      ClickPulseModule.J(var1);
      int var2 = cc.M(var1);
      if (var2 != -1) {
         if (WorldMenuModule.d()) {
            this.r.x(var2);
            this.d.x(var2);
         } else {
            this.S(var2);
         }
      }
   }

   public void S(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (!this.s && var2.player != null && var2.gui.screen() == null && var2.gui.overlay() == null && var2.isWindowActive() && !WorldMenuModule.K()) {
         this.r.k(var1);
         this.d.k(var1);
         LungeSwapModule.T(var1);
         AutoPearlCatchModule.v(var1);
         WindChargeHopModule.W(var1);
         SwordModelsModule var3 = SwordModelsModule.n();
         if (var3 != null) {
            var3.j(var1);
         }
      } else {
         this.r.x(var1);
         this.d.x(var1);
      }
   }

   private void L(Minecraft var1) {
      FreecamModule.o();
      FreelookModule.o();

      for (Module var3 : this.D.C()) {
         var3.validateHeldBind();
      }

      this.D.P();
      AimAssistModule.R();
      WTapModule.z();
      mq.Z(var1);
      e.M(var1);
      _r.f();
      uo.n(var1);
      cm.R.m();
      if (var1.gui.screen() instanceof TitleScreen) {
         h = (boolean)1;
      }

      if (!this.W && var1.gui.screen() instanceof TitleScreen) {
         this.W = (boolean)1;
      }

      if (this.W && var1.gui.screen() instanceof TitleScreen) {
      }

      this.r.F();
      long var13 = var1.getWindow().handle();

      for (Module var5 : this.D.C()) {
         int var6 = var5.getKeyBind();
         if (cc.N(var6)) {
            this.r.B(var5, var6, cc.N(var13, var6));
         }
      }

      List var14 = this.r.y();
      this.d.F();

      for (um.w var17 : this.f.t().s.l(this.D.C())) {
         this.d.B(var17, var17.code(), cc.N(var13, var17.code()));
      }

      List var16 = this.d.y();
      int var18 = var1.gui.screen() == null && var1.gui.overlay() == null && var1.isWindowActive() && !WorldMenuModule.K() ? 1 : 0;
      boolean var7 = this.u.consumeClick();
      byte var8 = 0;

      while (this.N.consumeClick()) {
         var8 = 1;
      }

      if (var1.player != null) {
         if (var8 != 0 && var18 != 0 && !var7 && var1.level != null) {
            var1.gui.setScreen(new hu(null));
         } else {
            if (!this.w) {
               this.w = (boolean)1;
               var1.options.fovEffectScale().set(0.0);
               var1.options.bobView().set(false);
               U.info("Applied LARP defaults: dynamic FOV off, view bobbing off");
            }

            if (var7) {
               if (var1.gui.screen() instanceof hw var9) {
                  var9.onClose();
               } else if (var1.gui.screen() instanceof h1 var10) {
                  var10.onClose();
               } else if (!WorldMenuModule.W()) {
                  var1.gui.setScreen(h1.G(null));
               }
            }

            if (var18 != 0 && !var7 && var1.gui.screen() == null) {
               for (Module var21 : var14) {
                  if (var21 instanceof FreecamModule var24 && var24.w()) {
                     if (!var24.Q()) {
                        continue;
                     }
                  } else if (var21 instanceof FreelookModule var12 && var12.g()) {
                     if (!var12.s()) {
                        continue;
                     }
                  } else if (var21.holdsKeybind()) {
                     if (!var21.activateHeldBind()) {
                        continue;
                     }
                  } else {
                     var21.toggle();
                  }

                  oj.K().r(var21.getDisplayName(), var21.isEnabled());
               }

               for (um.w var22 : var16) {
                  M(var22);
               }
            }
         }
      }
   }

   private static void M(um.w var0) {
      df var1 = var0.setting();
      String var2 = var0.module().getDisplayName() + " " + var1.getDisplayName();
      if (!var1.B()) {
         mt.U("Input", "Setting shortcut {} skipped while the setting is hidden", var2);
      } else {
         try {
            if (var1 instanceof dh var3) {
               var3.T();
               oj.K().r(var2, var3.P());
            } else if (var1 instanceof com.larpclient.module.d6 var4) {
               var4.f();
               oj.K().v(var4.getModeLabel(var4.P()), var2, oj.u.O, 2000L);
            }

            mt.U("Input", "Setting shortcut changed {} to {}", var2, var1.getValueAsString());
         } catch (RuntimeException var5) {
            mt.T("Input", "Setting shortcut for {} was rejected: {}", var2, var5.toString());
         }
      }
   }

   public static LarpClient F() {
      return L;
   }

   public dz x() {
      return this.D;
   }

   public w9 f() {
      return this.f;
   }

   public int R() {
      if (this.u == null) {
         return 344;
      }

      Key var1 = KeyMappingHelper.getBoundKeyOf(this.u);

      return switch (var1.getType()) {
         case KEYBOARD -> mo.X(var1.getValue());
         case MOUSE -> cc.M(mo.j(var1.getValue()));
         default -> -1;
      };
   }

   public void g(int var1) {
      if (this.u != null && var1 >= -1 && var1 <= 348) {
         this.u.setKey(cc.h(var1) ? Type.MOUSE.getOrCreate(mo.A(cc.T(var1))) : Type.KEYBOARD.getOrCreate(mo.f(var1)));
         this.u.setDown(false);
         KeyMapping.resetMapping();

         while (this.u.consumeClick()) {
         }

         Minecraft.getInstance().options.save();
         mt.K("Input", "ClickUI shortcut changed to {}", this.u.getTranslatedKeyMessage().getString());
      }
   }
}

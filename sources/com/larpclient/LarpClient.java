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
import com.larpclient.module.l1;
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
   public static final String H = "larp-client";
   public static final String L = xo.y;
   public static final Logger V = LoggerFactory.getLogger("larp-client");
   public static boolean N = (boolean)0;
   private static LarpClient A;
   private com.larpclient.module.ly s;
   private mo w;
   private fl C;
   private o8 F;
   private KeyMapping E;
   private KeyMapping v;
   private final hs<Module> t = new hs<>(349);
   private final hs<m5.w> h = new hs<>(349);
   private boolean o = (boolean)0;
   private boolean W = (boolean)0;
   private volatile boolean M;
   public static final Category l = Category.register(Identifier.fromNamespaceAndPath("larp-client", "main"));
   public static final Category u = Category.register(Identifier.fromNamespaceAndPath("larp-client", "editing"));

   public void onInitializeClient() {
      A = this;
      V.info("LarpClient v{} by Tobias - loading...", L);
      com.larpclient.h.P();
      this.s = new com.larpclient.module.ly();
      this.s.P();
      this.w = new mo();
      this.w.x();
      MicrosoftAccounts.INSTANCE.start(Minecraft.getInstance());
      UpdateRuntime.start();
      this.C = new fl(this.w.E().L);
      this.F = new o8();
      this.F.K();
      hg.B().K();
      l1.O();
      com.larpclient.module.l2.K();
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var1 -> {
         this.C.close();
         com.larpclient.h.K();
         this.M = (boolean)1;
         this.f();
         this.w.U();
         MicrosoftAccounts.INSTANCE.stop();
         UpdateRuntime.stop();
      });
      f8.L();
      y3.R();
      this.E = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.larp-client.open_gui", Type.KEYBOARD, r7.e(344), l));
      this.v = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.larp-client.open_editor", Type.KEYBOARD, r7.e(-1), u));
      r4.V("Input", "Module shortcuts use ClickGUI/profile keybinds only; legacy Minecraft module shortcuts are ignored");
      ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var1, var2) -> {
         com.larpclient.h.J();
         if (!this.M) {
            var2.execute(() -> {
               if (!this.M && (var2.getConnection() == null || var2.getConnection() == var1)) {
                  this.f();
               }
            });
         }
      });
      ClientTickEvents.END_CLIENT_TICK.register(this::e);
      V.info("LarpClient v{} loaded! {} modules registered.", L, this.s.X().size());
      V.info("Press RIGHT SHIFT to open the GUI.");
   }

   public void s(boolean var1) {
      if (this.C != null) {
         this.C.d(var1);
      }
   }

   private void f() {
      this.t.x();
      this.h.x();

      for (Module var2 : this.s.X()) {
         var2.suspendForDisconnect();
      }

      this.w.j();
   }

   public void b(int var1) {
      xc.f(var1);
      CustomCrosshairModule.j(var1);
      ClickPulseModule.j(var1);
      int var2 = pe.h(var1);
      if (var2 != -1) {
         if (WorldMenuModule.Y()) {
            this.t.T(var2);
            this.h.T(var2);
         } else {
            this.N(var2);
         }
      }
   }

   public void N(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (!this.M && var2.player != null && var2.gui.screen() == null && var2.gui.overlay() == null && var2.isWindowActive() && !WorldMenuModule.w()) {
         this.t.Y(var1);
         this.h.Y(var1);
         LungeSwapModule.l(var1);
         AutoPearlCatchModule.K(var1);
         WindChargeHopModule.C(var1);
         SwordModelsModule var3 = SwordModelsModule.t();
         if (var3 != null) {
            var3.B(var1);
         }
      } else {
         this.t.T(var1);
         this.h.T(var1);
      }
   }

   private void e(Minecraft var1) {
      FreecamModule.y();
      FreelookModule.k();

      for (Module var3 : this.s.X()) {
         var3.validateHeldBind();
      }

      this.s.Z();
      AimAssistModule.U();
      WTapModule.b();
      o8.C(var1);
      com.larpclient.h.C(var1);
      _7.W();
      o2.N(var1);
      hl.E.e();
      if (var1.gui.screen() instanceof TitleScreen) {
         N = (boolean)1;
      }

      if (!this.W && var1.gui.screen() instanceof TitleScreen) {
         this.W = (boolean)1;
      }

      if (this.W && var1.gui.screen() instanceof TitleScreen) {
      }

      this.t.R();
      long var13 = var1.getWindow().handle();

      for (Module var5 : this.s.X()) {
         int var6 = var5.getKeyBind();
         if (pe.F(var6)) {
            this.t.I(var5, var6, pe.A(var13, var6));
         }
      }

      List var14 = this.t.g();
      this.h.R();

      for (m5.w var17 : this.w.E().B.O(this.s.X())) {
         this.h.I(var17, var17.code(), pe.A(var13, var17.code()));
      }

      List var16 = this.h.g();
      int var18 = var1.gui.screen() == null && var1.gui.overlay() == null && var1.isWindowActive() && !WorldMenuModule.w() ? 1 : 0;
      boolean var7 = this.E.consumeClick();
      byte var8 = 0;

      while (this.v.consumeClick()) {
         var8 = 1;
      }

      if (var1.player != null) {
         if (var8 != 0 && var18 != 0 && !var7 && var1.level != null) {
            var1.gui.setScreen(new ps(null));
         } else {
            if (!this.o) {
               this.o = (boolean)1;
               var1.options.fovEffectScale().set(0.0);
               var1.options.bobView().set(false);
               V.info("Applied LARP defaults: dynamic FOV off, view bobbing off");
            }

            if (var7) {
               if (var1.gui.screen() instanceof p0 var9) {
                  var9.onClose();
               } else if (var1.gui.screen() instanceof pc var10) {
                  var10.onClose();
               } else if (!WorldMenuModule.Q()) {
                  var1.gui.setScreen(pc.o(null));
               }
            }

            if (var18 != 0 && !var7 && var1.gui.screen() == null) {
               for (Module var21 : var14) {
                  if (var21 instanceof FreecamModule var24 && var24.t()) {
                     if (!var24.O()) {
                        continue;
                     }
                  } else if (var21 instanceof FreelookModule var12 && var12.o()) {
                     if (!var12.P()) {
                        continue;
                     }
                  } else if (var21.holdsKeybind()) {
                     if (!var21.activateHeldBind()) {
                        continue;
                     }
                  } else {
                     var21.toggle();
                  }

                  hg.B().G(var21.getDisplayName(), var21.isEnabled());
               }

               for (m5.w var22 : var16) {
                  X(var22);
               }
            }
         }
      }
   }

   private static void X(m5.w var0) {
      com.larpclient.module.l6 var1 = var0.setting();
      String var2 = var0.module().getDisplayName() + " " + var1.getDisplayName();
      if (!var1.R()) {
         r4.g("Input", "Setting shortcut {} skipped while the setting is hidden", var2);
      } else {
         try {
            if (var1 instanceof com.larpclient.module.ln var3) {
               var3.S();
               hg.B().G(var2, var3.getValue());
            } else if (var1 instanceof com.larpclient.module.lp var4) {
               var4.F();
               hg.B().J(var4.getModeLabel(var4.getValue()), var2, hg.u.U, 2000L);
            }

            r4.g("Input", "Setting shortcut changed {} to {}", var2, var1.getValueAsString());
         } catch (RuntimeException var5) {
            r4.m("Input", "Setting shortcut for {} was rejected: {}", var2, var5.toString());
         }
      }
   }

   public static LarpClient y() {
      return A;
   }

   public com.larpclient.module.ly e() {
      return this.s;
   }

   public mo v() {
      return this.w;
   }

   public int b() {
      if (this.E == null) {
         return 344;
      }

      Key var1 = KeyMappingHelper.getBoundKeyOf(this.E);

      return switch (var1.getType()) {
         case KEYBOARD -> r7.u(var1.getValue());
         case MOUSE -> pe.h(r7.w(var1.getValue()));
         default -> -1;
      };
   }

   public void X(int var1) {
      if (this.E != null && var1 >= -1 && var1 <= 348) {
         this.E.setKey(pe.o(var1) ? Type.MOUSE.getOrCreate(r7.y(pe.H(var1))) : Type.KEYBOARD.getOrCreate(r7.e(var1)));
         this.E.setDown(false);
         KeyMapping.resetMapping();

         while (this.E.consumeClick()) {
         }

         Minecraft.getInstance().options.save();
         r4.V("Input", "ClickUI shortcut changed to {}", this.E.getTranslatedKeyMessage().getString());
      }
   }
}

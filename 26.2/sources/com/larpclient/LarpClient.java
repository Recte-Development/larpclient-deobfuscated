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
   public static final String r = "larp-client";
   public static final String X = z0.f;
   public static final Logger d = LoggerFactory.getLogger("larp-client");
   public static boolean A = (boolean)0;
   private static LarpClient g;
   private com.larpclient.module.f c;
   private gg b;
   private h4 e;
   private m4 S;
   private KeyMapping I;
   private KeyMapping O;
   private final pf<Module> z = new pf<>(349);
   private final pf<wr.n> P = new pf<>(349);
   private boolean W = (boolean)0;
   private boolean T = (boolean)0;
   private volatile boolean M;
   public static final Category B = Category.register(Identifier.fromNamespaceAndPath("larp-client", "main"));
   public static final Category Q = Category.register(Identifier.fromNamespaceAndPath("larp-client", "editing"));
   private int H = -1;
   private ServerData j;
   private String a;
   private final com.larpclient.module.c.p Y = new com.larpclient.module.c.p();

   public void onInitializeClient() {
      g = this;
      d.info("LarpClient v{} by Tobias - loading...", X);
      lq.R();
      this.c = new com.larpclient.module.f();
      this.c.Q();
      this.b = new gg();
      this.b.a();
      com.larpclient.module.c.a(this.b.g().J);
      MicrosoftAccounts.INSTANCE.start(Minecraft.getInstance());
      UpdateRuntime.start();
      this.e = new h4(this.b.g().w);
      this.S = new m4();
      this.S.S();
      s0.s().v();
      com.larpclient.module.k.D();
      com.larpclient.module.p.M();
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var1 -> {
         this.e.close();
         lq.V();
         this.M = (boolean)1;
         this.i();
         this.b.k();
         MicrosoftAccounts.INSTANCE.stop();
         UpdateRuntime.stop();
      });
      kz.s();
      w6.W();
      this.I = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.larp-client.open_gui", Type.KEYSYM, k2.M(344), B));
      this.O = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.larp-client.open_editor", Type.KEYSYM, k2.M(-1), Q));
      h.A("Input", "Module shortcuts use ClickGUI/profile keybinds only; legacy Minecraft module shortcuts are ignored");
      ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var1, var2) -> {
         lq.P();
         if (!this.M) {
            var2.execute(() -> {
               if (!this.M && (var2.getConnection() == null || var2.getConnection() == var1)) {
                  this.i();
               }
            });
         }
      });
      ClientTickEvents.END_CLIENT_TICK.register(this::z);
      d.info("LarpClient v{} loaded! {} modules registered.", X, this.c.h().size());
      h.A("Startup", "Press RIGHT SHIFT to open the GUI");
   }

   public void I(boolean var1) {
      if (this.e != null) {
         this.e.x(var1);
      }
   }

   private void L(Minecraft var1) {
      if (this.e != null) {
         ServerData var2 = var1.getCurrentServer();
         int var3 = var1.level != null ? 1 : 0;
         boolean var4 = var1.hasSingleplayerServer();
         String var5 = var2 == null ? null : var2.ip;
         int var6 = (var3 != 0 ? 1 : 0) | (var4 ? 2 : 0);
         if (var6 != this.H || var2 != this.j || !Objects.equals(var5, this.a)) {
            this.H = var6;
            this.j = var2;
            this.a = var5;
            this.e.J(h4.p((boolean)var3, var4, var2 != null && var2.isRealm(), var2 != null && var2.isLan(), var5));
         }
      }
   }

   private void i() {
      this.z.S();
      this.P.S();

      for (Module var2 : this.c.h()) {
         var2.suspendForDisconnect();
      }

      this.b.m();
   }

   public boolean t(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      Screen var3 = var2.gui.screen();
      int var4 = var2.gui.overlay() != null ? 1 : 0;
      if (!this.M && this.b != null && var4 == 0 && !WorldMenuModule.K() && com.larpclient.module.c.s(var3)) {
         if (!this.Y.a(var1, this.b.g().m, System.nanoTime() / 1000000L)) {
            return false;
         }

         this.o(!com.larpclient.module.c.p());

         while (var2.options.keyCommand.consumeClick()) {
         }

         return true;
      } else {
         this.Y.s();
         return false;
      }
   }

   public void o(boolean var1) {
      if (var1 != com.larpclient.module.c.p()) {
         this.c.y(var1);
         this.b.g().J = var1;
         this.b.W();
         this.z.S();
         this.P.S();
         Minecraft var2 = Minecraft.getInstance();
         Screen var3 = var2.gui.screen();
         if (!(var3 instanceof ChatScreen) && !(var3 instanceof hf) && !(var3 instanceof hi)) {
            WorldMenuModule.a();
         } else {
            var2.gui.setScreen(null);
         }

         if (var1) {
            s0.s().c();
         } else {
            s0.s().x("Legit mode", false);
         }
      }
   }

   public void u(int var1) {
      to.m(var1);
      CustomCrosshairModule.q(var1);
      ClickPulseModule.x(var1);
      int var2 = bc.Z(var1);
      if (var2 != -1) {
         if (WorldMenuModule.X()) {
            this.z.T(var2);
            this.P.T(var2);
         } else {
            this.q(var2);
         }
      }
   }

   public void q(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (!this.M && var2.player != null && var2.gui.screen() == null && var2.gui.overlay() == null && var2.isWindowActive() && !WorldMenuModule.K()) {
         this.z.h(var1);
         this.P.h(var1);
         LungeSwapModule.x(var1);
         AutoPearlCatchModule.T(var1);
         WindChargeHopModule.p(var1);
         SwordModelsModule var3 = SwordModelsModule.N();
         if (var3 != null) {
            var3.y(var1);
         }
      } else {
         this.z.T(var1);
         this.P.T(var1);
      }
   }

   private void z(Minecraft var1) {
      FreecamModule.C();
      FreelookModule.S();

      for (Module var3 : this.c.h()) {
         var3.validateHeldBind();
      }

      this.c.u();
      AutoReconnectModule.S(var1);
      AimAssistModule.J();
      WTapModule.J();
      m4.B(var1);
      lq.g(var1);
      zt.j();
      this.L(var1);
      hn.C(var1);
      uq.x.M();
      if (var1.gui.screen() instanceof TitleScreen) {
         A = (boolean)1;
      }

      if (!this.T && var1.gui.screen() instanceof TitleScreen) {
         this.T = (boolean)1;
      }

      if (this.T && var1.gui.screen() instanceof TitleScreen) {
      }

      this.z.i();
      long var13 = var1.getWindow().handle();

      for (Module var5 : this.c.a()) {
         int var6 = var5.getKeyBind();
         if (bc.a(var6)) {
            this.z.M(var5, var6, bc.I(var13, var6));
         }
      }

      List var14 = this.z.J();
      this.P.i();

      for (wr.n var17 : this.b.g().K.y(this.c.a())) {
         this.P.M(var17, var17.code(), bc.I(var13, var17.code()));
      }

      List var16 = this.P.J();
      int var18 = var1.gui.screen() == null && var1.gui.overlay() == null && var1.isWindowActive() && !WorldMenuModule.K() ? 1 : 0;
      boolean var7 = this.I.consumeClick();
      byte var8 = 0;

      while (this.O.consumeClick()) {
         var8 = 1;
      }

      if (var1.player != null) {
         if (var8 != 0 && var18 != 0 && !var7 && var1.level != null) {
            var1.gui.setScreen(new h7(null));
         } else {
            if (!this.W) {
               this.W = (boolean)1;
               var1.options.fovEffectScale().set(0.0);
               var1.options.bobView().set(false);
               h.A("Startup", "Applied Larp defaults: dynamic FOV off, view bobbing off");
            }

            if (var7) {
               if (var1.gui.screen() instanceof hf var9) {
                  var9.onClose();
               } else if (var1.gui.screen() instanceof hi var10) {
                  var10.onClose();
               } else if (!WorldMenuModule.a()) {
                  var1.gui.setScreen(hi.w(null));
               }
            }

            if (var18 != 0 && !var7 && var1.gui.screen() == null) {
               for (Module var21 : var14) {
                  if (var21 instanceof FreecamModule var24 && var24.D()) {
                     if (!var24.g()) {
                        continue;
                     }
                  } else if (var21 instanceof FreelookModule var12 && var12.J()) {
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

                  s0.s().x(var21.getDisplayName(), var21.isEnabled());
               }

               for (wr.n var22 : var16) {
                  r(var22);
               }
            }
         }
      }
   }

   private static void r(wr.n var0) {
      com.larpclient.module.q var1 = var0.setting();
      String var2 = var0.module().getDisplayName() + " " + var1.getDisplayName();
      if (!var1.W()) {
         h.U("Input", "Setting shortcut {} skipped while the setting is hidden", var2);
      } else {
         try {
            if (var1 instanceof com.larpclient.module.s var3) {
               var3.c();
               s0.s().x(var2, var3.getValue());
            } else if (var1 instanceof com.larpclient.module.l var4) {
               var4.U();
               s0.s().Z(var4.getModeLabel(var4.getValue()), var2, s0.u.B, 2000L);
            }

            h.U("Input", "Setting shortcut changed {} to {}", var2, var1.getValueAsString());
         } catch (RuntimeException var5) {
            h.g("Input", "Setting shortcut for {} was rejected: {}", var2, var5.toString());
         }
      }
   }

   public static LarpClient R() {
      return g;
   }

   public com.larpclient.module.f z() {
      return this.c;
   }

   public gg o() {
      return this.b;
   }

   public int m() {
      if (this.I == null) {
         return 344;
      }

      Key var1 = KeyMappingHelper.getBoundKeyOf(this.I);

      return switch (var1.getType()) {
         case KEYSYM -> k2.J(var1.getValue());
         case MOUSE -> bc.Z(k2.I(var1.getValue()));
         default -> -1;
      };
   }

   public void C(int var1) {
      if (this.I != null && var1 >= -1 && var1 <= 348) {
         this.I.setKey(bc.Q(var1) ? Type.MOUSE.getOrCreate(k2.f(bc.a(var1))) : Type.KEYSYM.getOrCreate(k2.M(var1)));
         this.I.setDown(false);
         KeyMapping.resetMapping();

         while (this.I.consumeClick()) {
         }

         Minecraft.getInstance().options.save();
         h.A("Input", "ClickUI shortcut changed to {}", this.I.getTranslatedKeyMessage().getString());
      }
   }
}

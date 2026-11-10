package com.larpclient.module;

import com.larp.obf.q41a5851bcf8e876a.q0c224d8eb12ccb1e;
import com.larpclient.LarpClient;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.bb;
import com.larpclient.ij;
import com.larpclient.p4;
import com.larpclient.qg;
import com.larpclient.qx;
import com.larpclient.s6;
import com.larpclient.vm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.PiercingWeapon;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;

public final class LungeSwapModule extends Module {
   private static LungeSwapModule b;
   public static final int A = 10;
   public static final int z = 12;
   public static final double a = 0.4;
   private final o O = this.addSetting(
      new o("Mode", "Legit waits for full charges like vanilla; Blatant jabs as soon as the server accepts it", "Legit", "Legit", "Blatant")
   );
   private final x N = this.addSetting(new x("Activation Key", "Press to swap to your Lunge spear and jab; separate from the module toggle", 86));
   private final m v = this.addSetting(new m("Swap Back", "Return after the jab; Blatant always returns", true));
   private final m x = this.addSetting(new m("Swap Back To Slot", "Return to a fixed hotbar slot instead of your previous one", false));
   private final b h = this.addSetting(new b("Swap Back Slot", "Hotbar slot to return to", 1.0, 1.0, 9.0, 1.0));
   private final m V = this.addSetting(new m("Hold To Lunge", "Keep jabbing while the activation key is held", false));
   private final m K = this.addSetting(
      new m(
         "Charge After Swap", "On a server that resets charge on a swap, hold the spear until its own charge is ready. Off always jabs in the swap tick", true
      )
   );
   private final o R = this.addSetting(
      new o(
         "Swap Rule",
         "How the server treats charge across a swap. Detect jabs in the swap tick and waits for the spear's charge on a server where that did not lunge",
         "Detect",
         "Detect",
         "Kept",
         "Reset"
      )
   );
   private final b y = this.addSetting(
      new b(
         "Jab Delay",
         "Fewest ticks from your press to the swap and jab; your held item keeps charging meanwhile. 0 jabs in the press tick",
         0.0,
         0.0,
         10.0,
         1.0
      )
   );
   private final b E = this.addSetting(new b("Jab Delay Max", "Most ticks from your press to the swap and jab", 0.0, 0.0, 10.0, 1.0));
   private final b U = this.addSetting(
      new b("Swap Back Delay", "Fewest ticks after the jab before you return to your slot. 1 is the next tick", 1.0, 1.0, 20.0, 1.0)
   );
   private final b D = this.addSetting(new b("Swap Back Delay Max", "Most ticks after the jab before you return to your slot", 1.0, 1.0, 20.0, 1.0));
   private final b d = this.addSetting(new b("Hold Interval", "Fewest extra ticks between lunges while Hold To Lunge is held", 0.0, 0.0, 40.0, 1.0));
   private final b m = this.addSetting(new b("Hold Interval Max", "Most extra ticks between lunges while Hold To Lunge is held", 0.0, 0.0, 40.0, 1.0));
   private static final Map<String, Boolean> T = new HashMap<>();
   private LungeSwapModule.t I = LungeSwapModule.t.j;
   private boolean P;
   private int H;
   private int g;
   private int o;
   private int X = -1;
   private int B = -1;
   private int Z;
   private int F;
   private int J;
   private int e;
   private int r;
   private bb.m s = bb.m.R;
   private String M = "";
   private String c = "";
   private float u;
   private LocalPlayer S;
   private ClientLevel w;
   private String C = "Ready";
   private String L = "";
   private int W;
   private int i;
   private double Y;
   private boolean G;
   private boolean p;
   private Vec3 t = Vec3.ZERO;

   public LungeSwapModule() {
      super("Lunge Swap", "Swap to your Lunge spear and jab with an activation key", Category.COMBAT, -1);
      b = this;
      this.v.a(this::s);
      this.x.a(this::P);
      this.h.a(() -> this.P() && this.x.getValue());
      this.R.a(this.K::getValue);
      this.y.R(this.E).B("Jab Delay");
      this.U.R(this.D).B("Swap Back Delay");
      this.d.R(this.m).B("Hold Interval");
      this.U.a(this::P);
      this.D.a(this::P);
      this.d.a(this.V::getValue);
      this.m.a(this.V::getValue);
      this.O.w(var1 -> this.F(true));
      this.N.w(var1 -> {
         this.F(true);
         this.W = 0;
      });
      this.R.w(var1 -> this.F(true));
   }

   private boolean s() {
      return this.O.w("Legit");
   }

   private boolean m() {
      return this.O.w("Blatant");
   }

   private boolean P() {
      return this.m() || this.v.getValue();
   }

   public static int R(boolean var0) {
      return bb.g(var0);
   }

   public static int R(double var0, double var2, double var4) {
      return qx.p((int)var0, (int)var2, var4);
   }

   private int A(b var1, b var2) {
      return R(var1.getValue(), var2.getValue(), ThreadLocalRandom.current().nextDouble());
   }

   public static int c(boolean var0, boolean var1, int var2, int var3, int var4, int var5) {
      int var6 = var1 ? var2 : var3;
      return var0 && var4 == var5 && var6 >= 0 && var6 < 9 && var6 != var4 ? var6 : -1;
   }

   public static bb.m L(boolean var0, String var1, Boolean var2, boolean var3) {
      if (!var0 || "Kept".equals(var1)) {
         return bb.m.R;
      } else if ("Reset".equals(var1)) {
         return bb.m.D;
      } else if (var2 != null) {
         return var2 ? bb.m.D : bb.m.R;
      } else {
         return var3 ? bb.m.D : bb.m.R;
      }
   }

   public static void D(int var0) {
      if (b != null && b.isEnabled()) {
         LungeSwapModule var1 = b;
         if (var1 != null && var1.isRuntimeActive() && p4.g(var0) && var0 == var1.N.getValue()) {
            var1.P = (boolean)1;
         }
      }
   }

   public boolean c() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.I != LungeSwapModule.t.j && this.S != null && this.S == getMc().player;
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.F, l.k);
   }

   @Override
   public String getStatus() {
      if (!this.isEnabled()) {
         return "";
      } else {
         return this.I == LungeSwapModule.t.j && !this.L.isEmpty() ? p4.n(this.N.getValue()) + " also triggers " + this.L : this.C;
      }
   }

   @Override
   public void onEnable() {
      this.t();
      this.S = mc.player;
      this.w = mc.level;
      this.C = "Ready";
      this.L = "";
      this.W = 0;
      _q.z(
         "LungeSwap",
         "Enabled; mode={}, activation key={}, hold={}, charge after swap={}, swap rule={}, jab delay {}-{}, swap back delay {}-{}, hold interval {}-{} ticks",
         this.O.getValue(),
         this.N.getValue(),
         this.V.getValue(),
         this.K.getValue(),
         this.R.getValue(),
         this.y.getValue().intValue(),
         this.E.getValue().intValue(),
         this.U.getValue().intValue(),
         this.D.getValue().intValue(),
         this.d.getValue().intValue(),
         this.m.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.F(true);
      this.S = null;
      this.w = null;
   }

   private boolean b() {
      return this.isInGame()
         && this.S == mc.player
         && this.w == mc.level
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && mc.gui.screen() == null
         && (qg.U(mc) || ij.Y() && qg.j(mc, ij.r.o));
   }

   @Override
   public void onTick() {
      boolean var1 = this.P;
      this.P = (boolean)0;
      int var10000 = --this.W;
      boolean var4 = false;
      int var3 = var10000;
      if (-Integer.signum(var3) >>> 31 == 0) {
         this.W = 20;
         this.I();
      }

      if (this.S != mc.player || this.w != mc.level) {
         this.F(false);
         this.S = mc.player;
         this.w = mc.level;
         this.i = 0;
      }

      if (this.i > 0) {
         this.a();
      }

      if (!this.b()) {
         this.F(true);
         this.C = "Paused";
      } else {
         boolean var2 = p4.Y(mc.getWindow().handle(), this.N.getValue());
         boolean var8 = false;
         int var7 = this.o;
         if ((-Integer.signum(var7) >>> 31 ^ 1) == 0 && (!var2 || var1)) {
            this.o = 0;
         }

         label60:
         if (this.I == LungeSwapModule.t.j) {
            if (!var1) {
               if (!this.V.getValue() || !var2) {
                  break label60;
               }

               var10000 = this.o;
               byte var10 = 1;
               int var9 = this.o;
               this.o = var9 - var10;
               if (var10000 > 0) {
                  break label60;
               }
            }

            this.I(var1);
         }

         if (this.I == LungeSwapModule.t.h) {
            this.N();
         } else if (this.I == LungeSwapModule.t.p) {
            this.q();
         } else {
            if (this.I == LungeSwapModule.t.q) {
               var10000 = --this.g;
               boolean var6 = false;
               int var5 = var10000;
               if (-Integer.signum(var5) >>> 31 == 0) {
                  this.U();
                  if (this.V.getValue() && var2) {
                     this.o = this.A(this.d, this.m);
                  }

                  this.C = "Ready";
                  return;
               }
            }

            if (this.I == LungeSwapModule.t.j && !p4.g(this.N.getValue())) {
               this.C = "Set an Activation Key";
            }
         }
      }
   }

   public static String i(Module var0, int var1, Iterable<Module> var2) {
      if (!p4.g(var1)) {
         return "";
      }

      if (var0.getKeyBind() == var1) {
         return "the Lunge Swap toggle";
      }

      for (Module var4 : var2) {
         if (var4 != var0) {
            if (var4.getKeyBind() == var1) {
               return var4.getName();
            }

            for (r var6 : var4.getSettings()) {
               if (var6 instanceof x var7 && var7.getValue() == var1) {
                  return var4.getName();
               }
            }
         }
      }

      return "";
   }

   private void I() {
      LarpClient var1 = LarpClient.N();
      int var2 = this.N.getValue();
      String var3 = var1 != null && var1.j() != null ? i(this, var2, var1.j().d()) : "";
      if (!var3.isEmpty() && !var3.equals(this.L)) {
         _q.R(
            "LungeSwap",
            "Activation Key {} is also bound to {}; one press triggers both, and a movement module such as Fly cancels the lunge. Rebind one of them",
            p4.n(var2),
            var3
         );
      }

      this.L = var3;
   }

   private void a() {
      LocalPlayer var1 = mc.player;
      this.Y = Math.max(this.Y, var1.getDeltaMovement().horizontalDistance());
      if (--this.i <= 0) {
         int var2 = this.Y >= 0.4 ? 1 : 0;
         _q.z(
            "LungeSwap",
            "Jab result: peak horizontal speed {} blocks/tick, moved {} blocks in {} ticks ({})",
            String.format(Locale.ROOT, "%.3f", this.Y),
            String.format(Locale.ROOT, "%.2f", var1.position().subtract(this.t).horizontalDistance()),
            12,
            var2 != 0 ? "lunge push arrived" : "no lunge push; the server refused it or another movement owner replaced it"
         );
         if (this.G && this.K.getValue() && this.R.w("Detect")) {
            String var3 = this.Z();
            Boolean var4 = T.get(var3);
            T.put(var3, var2 == 0);
            if (var2 == 0 && !Boolean.TRUE.equals(var4)) {
               _q.z("LungeSwap", "A swap-tick jab did not lunge on {}; later presses there wait for the spear's own charge", var3);
            } else if (var2 != 0 && var4 == null) {
               _q.z("LungeSwap", "{} keeps the charge across a swap; presses there jab in the swap tick", var3);
            }
         }
      }
   }

   private String Z() {
      ServerData var1 = mc.getCurrentServer();
      return var1 != null ? var1.ip : (mc.hasSingleplayerServer() ? "singleplayer" : "this server");
   }

   private void I(boolean var1) {
      String var2 = this.X();
      if (var2 != null) {
         this.C = var2;
         if (var1) {
            _q.z("LungeSwap", "Press refused: {}", var2);
         }
      } else if (this.Z() == null) {
         this.C = "No Lunge spear in hotbar";
         if (var1) {
            _q.z("LungeSwap", "Press ignored: no Lunge spear in the hotbar");
         }
      } else {
         this.X = mc.player.getInventory().getSelectedSlot();
         this.M = mc.player.getMainHandItem().getHoverName().getString();
         this.u = mc.player.getAttackStrengthScale(0.0F);
         this.s = L(this.K.getValue(), this.R.getValue(), T.get(this.Z()), vm.G());
         this.Z = this.F = 0;
         this.J = -1;
         this.c = "";
         this.H = this.A(this.y, this.E);
         this.I = LungeSwapModule.t.h;
      }
   }

   private String X() {
      LocalPlayer var1 = mc.player;
      return bb.X(var1.getFoodData().getFoodLevel(), var1.getAbilities().instabuild, var1.isInWater(), var1.isPassenger(), var1.isFallFlying());
   }

   private bb.w Z() {
      LocalPlayer var1 = mc.player;
      ItemStack var2 = var1.getMainHandItem();
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < 9; var4++) {
         int var5 = this.r(var4);
         if (var5 > 0) {
            ItemStack var6 = var1.getInventory().getItem(var4);
            if (var6.isItemEnabled(this.w.enabledFeatures())) {
               var3.add(
                  new bb.t(
                     var4,
                     s(var1, var6),
                     (Float)var6.getOrDefault(DataComponents.MINIMUM_ATTACK_CHARGE, 0.0F),
                     var5,
                     var6.isDamageableItem() ? var6.getMaxDamage() - var6.getDamageValue() : Integer.MAX_VALUE,
                     var6.nextDamageWillBreak(),
                     ItemStack.isSameItem(var6, var2)
                  )
               );
            }
         }
      }

      return bb.Y(
         var3,
         var1.getInventory().getSelectedSlot(),
         this.s,
         ((q0c224d8eb12ccb1e)var1).larpAttackStrengthTicker(),
         var1.getCurrentItemAttackStrengthDelay(),
         this.m()
      );
   }

   private static float s(LocalPlayer var0, ItemStack var1) {
      AttributeInstance var2 = var0.getAttribute(Attributes.ATTACK_SPEED);
      if (var2 == null) {
         return var0.getCurrentItemAttackStrengthDelay();
      }

      ArrayList var3 = new ArrayList();

      for (AttributeModifier var5 : var2.getModifiers()) {
         if (!var5.id().equals(Item.BASE_ATTACK_SPEED_ID)) {
            var3.add(var5);
         }
      }

      var1.forEachModifier(EquipmentSlot.MAINHAND, (var1x, var2x) -> {
         if (var1x.equals(Attributes.ATTACK_SPEED)) {
            var3.add(var2x);
         }
      });
      double var10 = var2.getBaseValue();

      for (AttributeModifier var9 : var3) {
         if (var9.operation() == Operation.ADD_VALUE) {
            var10 += var9.amount();
         }
      }

      double var6 = var10;

      for (AttributeModifier var13 : var3) {
         if (var13.operation() == Operation.ADD_MULTIPLIED_BASE) {
            var6 += var10 * var13.amount();
         }
      }

      for (AttributeModifier var14 : var3) {
         if (var14.operation() == Operation.ADD_MULTIPLIED_TOTAL) {
            var6 *= 1.0 + var14.amount();
         }
      }

      return (float)(1.0 / ((Attribute)var2.getAttribute().value()).sanitizeValue(var6) * 20.0);
   }

   private void N() {
      bb.w var1 = this.Z();
      if (var1 == null) {
         this.t();
         this.C = "No Lunge spear in hotbar";
      } else {
         if (this.J < 0) {
            this.J = var1.ticks();
            _q.z(
               "LungeSwap",
               "Press: {} rule, slot {} (Lunge {}) from slot {} ({}, charge {}); {}",
               this.s,
               var1.slot() + 1,
               var1.lunge(),
               this.X + 1,
               this.M,
               u(this.u),
               var1.ticks() == 0 ? (var1.swap() ? "swap and jab now" : "jab now") : "server needs " + var1.ticks() + " more ticks"
            );
         }

         this.B = var1.slot();
         if (this.H > 0) {
            this.H--;
            this.C = "Lunge in " + bb.j(Math.max(this.H + 1, var1.ticks()));
            this.x("Jab Delay");
         } else if (var1.ticks() > 0 && !var1.resets()) {
            this.Z++;
            this.C = "Lunge in " + bb.j(var1.ticks());
            this.x("charge");
            if (this.Z > this.J + 10 + 2) {
               this.r("the charge never passed");
            }
         } else if (!this.p()) {
            String var2 = this.f();
            if (var2 != null) {
               this.x(var2);
               this.C = "Waiting for " + var2;
               if (++this.F > 10) {
                  this.r(var2);
               }
            } else {
               this.C = "Lunging";
               _p.w(this, this::D, () -> this.f() == null && this.I == LungeSwapModule.t.h);
            }
         }
      }
   }

   private boolean p() {
      if (!ij.Y()) {
         return false;
      }

      ij.B("a Lunge Swap jab");
      this.x("your look");
      this.C = "Lunging";
      return true;
   }

   private String f() {
      if (!this.isRuntimeActive() || !this.b()) {
         return "the game";
      } else if (!s6.L(this)) {
         return "the hotbar";
      } else if (!qg.g(mc, mc.player)) {
         return "free hands";
      } else if (this.s() && qg.W(mc) > 0) {
         return "the miss delay";
      } else if (this.X() != null) {
         return "the Lunge conditions";
      } else {
         return this.B >= 0 && this.y(this.B) ? null : "the spear";
      }
   }

   private void x(String var1) {
      if (!var1.equals(this.c)) {
         this.c = var1;
         _q.z("LungeSwap", "Jab held by {}", var1);
      }
   }

   private void r(String var1) {
      _q.z("LungeSwap", "Press lapsed after {} ticks, held by {}", this.Z + this.F, var1);
      this.F(true);
      this.C = "Lunge blocked by " + var1;
   }

   private void D() {
      bb.w var1 = this.Z();
      if (this.I == LungeSwapModule.t.h && var1 != null && (var1.ticks() <= 0 || var1.resets()) && this.f() == null) {
         LocalPlayer var2 = mc.player;
         this.B = var1.slot();
         boolean var3 = var1.swap();
         if (var1.resets()) {
            this.r = bb.i(this.c(this.B), false, this.s, 0, 0.0F, this.m());
            var2.getInventory().setSelectedSlot(this.B);
            this.I = LungeSwapModule.t.p;
            this.e = 0;
            this.C = "Charging spear " + bb.j(this.r);
            _q.z("LungeSwap", "Selected spear slot {}; the server restarts its charge, so the jab waits {} ticks", this.B + 1, this.r);
         } else {
            this.p = (boolean)(var3 && !ItemStack.isSameItem(var2.getMainHandItem(), var2.getInventory().getItem(this.B)) ? 1 : 0);
            var2.getInventory().setSelectedSlot(this.B);
            this.X(var3);
         }
      }
   }

   private bb.t c(int var1) {
      LocalPlayer var2 = mc.player;
      ItemStack var3 = var2.getInventory().getItem(var1);
      return new bb.t(var1, s(var2, var3), (Float)var3.getOrDefault(DataComponents.MINIMUM_ATTACK_CHARGE, 0.0F), this.r(var1), 0, false, false);
   }

   private void q() {
      LocalPlayer var1 = mc.player;
      this.e++;
      if (var1.getInventory().getSelectedSlot() == this.B && this.y(this.B)) {
         int var2 = this.r - this.e;
         if (var2 > 0) {
            this.C = "Charging spear " + bb.j(var2);
         } else if (!this.p()) {
            String var3 = this.f();
            if (var3 != null) {
               this.x(var3);
               this.C = "Waiting for " + var3;
               if (++this.F > 10) {
                  _q.z("LungeSwap", "Charged jab lapsed, held by {}", var3);
                  _p.e(this);
                  this.U();
                  this.C = "Lunge blocked by " + var3;
               }
            } else {
               _p.w(this, () -> {
                  if (this.I == LungeSwapModule.t.p && this.f() == null) {
                     this.X(true);
                  }
               }, () -> this.I == LungeSwapModule.t.p && this.f() == null);
            }
         }
      } else {
         _q.z("LungeSwap", "Charge interrupted after {} ticks: slot {} selected", this.e, var1.getInventory().getSelectedSlot() + 1);
         _p.e(this);
         this.t();
         this.C = "Swap interrupted";
      }
   }

   private void X(boolean var1) {
      LocalPlayer var2 = mc.player;
      ItemStack var3 = var2.getInventory().getItem(this.B);
      int var4 = var1 && this.I == LungeSwapModule.t.h ? 1 : 0;
      if (var3.isItemEnabled(this.w.enabledFeatures())) {
         mc.gameMode.piercingAttack(var3.getAttackAnimation(), (PiercingWeapon)var3.get(DataComponents.PIERCING_WEAPON));
         this.C = "Lunged";
         this.i = 12;
         this.Y = 0.0;
         this.t = var2.position();
         this.G = (boolean)(var4 != 0 && this.p ? 1 : 0);
      } else {
         this.C = "Spear not enabled here";
      }

      int var5 = this.Z + this.F + this.e;
      this.I = LungeSwapModule.t.q;
      this.g = this.A(this.U, this.D);
      _q.z(
         "LungeSwap",
         "{}: {} {} rule, slot {} ({}, charge {}) to spear slot {} (Lunge {}) {} ticks after the press{}; food {}, ground {}, sprint {}",
         this.C,
         this.O.getValue(),
         this.s,
         this.X + 1,
         this.M,
         u(this.u),
         this.B + 1,
         this.r(this.B),
         var5,
         var4 != 0 ? ", swapped in the jab tick" : (var1 ? ", spear charged after the swap" : ", spear already held"),
         var2.getFoodData().getFoodLevel(),
         var2.onGround(),
         var2.isSprinting()
      );
      if (this.g > 1) {
         _q.z("LungeSwap", "Swap back in {} ticks", this.g);
      }
   }

   private void U() {
      LocalPlayer var1 = getMc() == null ? null : mc.player;
      if (var1 != null && var1 == this.S && var1.level() == this.w) {
         int var2 = c(this.P(), this.x.getValue(), this.h.getValue().intValue() - 1, this.X, var1.getInventory().getSelectedSlot(), this.B);
         if (var2 >= 0) {
            var1.getInventory().setSelectedSlot(var2);
         }
      }

      this.t();
   }

   private void F(boolean var1) {
      _p.e(this);
      if (this.I != LungeSwapModule.t.j) {
         _q.z("LungeSwap", "Cancelled at {}; restore={}, status {}", this.I, var1, this.C);
      }

      if (!var1 || this.I != LungeSwapModule.t.q && this.I != LungeSwapModule.t.p) {
         this.t();
      } else {
         this.U();
      }
   }

   private void t() {
      this.I = LungeSwapModule.t.j;
      this.p = (boolean)0;
      this.X = this.B = -1;
      this.H = this.g = 0;
      this.Z = this.F = this.e = this.r = 0;
      this.J = -1;
      this.P = (boolean)0;
      this.c = "";
   }

   private boolean y(int var1) {
      return this.r(var1) > 0;
   }

   private int r(int var1) {
      if (var1 >= 0 && var1 < 9 && mc.player != null && mc.level != null) {
         ItemStack var2 = mc.player.getInventory().getItem(var1);
         if (!var2.has(DataComponents.PIERCING_WEAPON)) {
            return 0;
         }

         Optional var3 = mc.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.LUNGE);
         return var3.<Integer>map(var1x -> EnchantmentHelper.getItemEnchantmentLevel(var1x, var2)).orElse(0);
      } else {
         return 0;
      }
   }

   private static String u(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   public enum t {
      j,
      h,
      p,
      q;
   }
}

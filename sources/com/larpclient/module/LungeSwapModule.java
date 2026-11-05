package com.larpclient.module;

import com.larp.obf.qbac0b262827a15ac.qccc4a2dfe8596cd6;
import com.larpclient.LarpClient;
import com.larpclient._m;
import com.larpclient._p;
import com.larpclient.c2;
import com.larpclient.cc;
import com.larpclient.mt;
import com.larpclient.ti;
import com.larpclient.tq;
import com.larpclient.w7;
import com.larpclient.zd;
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
   private static LungeSwapModule s;
   public static final int Z = 10;
   public static final int x = 12;
   public static final double G = 0.4;
   private final d6 f = this.addSetting(
      new d6("Mode", "Legit waits for full charges like vanilla; Blatant jabs as soon as the server accepts it", "Legit", "Legit", "Blatant")
   );
   private final dv P = this.addSetting(new dv("Activation Key", "Press to swap to your Lunge spear and jab; separate from the module toggle", 86));
   private final dh J = this.addSetting(new dh("Swap Back", "Return after the jab; Blatant always returns", true));
   private final dh e = this.addSetting(new dh("Swap Back To Slot", "Return to a fixed hotbar slot instead of your previous one", false));
   private final d9 t = this.addSetting(new d9("Swap Back Slot", "Hotbar slot to return to", 1.0, 1.0, 9.0, 1.0));
   private final dh u = this.addSetting(new dh("Hold To Lunge", "Keep jabbing while the activation key is held", false));
   private final dh j = this.addSetting(
      new dh(
         "Charge After Swap", "On a server that resets charge on a swap, hold the spear until its own charge is ready. Off always jabs in the swap tick", true
      )
   );
   private final d6 K = this.addSetting(
      new d6(
         "Swap Rule",
         "How the server treats charge across a swap. Detect jabs in the swap tick and waits for the spear's charge on a server where that did not lunge",
         "Detect",
         "Detect",
         "Kept",
         "Reset"
      )
   );
   private final d9 v = this.addSetting(
      new d9(
         "Jab Delay",
         "Fewest ticks from your press to the swap and jab; your held item keeps charging meanwhile. 0 jabs in the press tick",
         0.0,
         0.0,
         10.0,
         1.0
      )
   );
   private final d9 p = this.addSetting(new d9("Jab Delay Max", "Most ticks from your press to the swap and jab", 0.0, 0.0, 10.0, 1.0));
   private final d9 L = this.addSetting(
      new d9("Swap Back Delay", "Fewest ticks after the jab before you return to your slot. 1 is the next tick", 1.0, 1.0, 20.0, 1.0)
   );
   private final d9 B = this.addSetting(new d9("Swap Back Delay Max", "Most ticks after the jab before you return to your slot", 1.0, 1.0, 20.0, 1.0));
   private final d9 z = this.addSetting(new d9("Hold Interval", "Fewest extra ticks between lunges while Hold To Lunge is held", 0.0, 0.0, 40.0, 1.0));
   private final d9 y = this.addSetting(new d9("Hold Interval Max", "Most extra ticks between lunges while Hold To Lunge is held", 0.0, 0.0, 40.0, 1.0));
   private static final Map<String, Boolean> o = new HashMap<>();
   private LungeSwapModule.f V = LungeSwapModule.f.F;
   private boolean O;
   private int H;
   private int N;
   private int q;
   private int D = -1;
   private int M = -1;
   private int c;
   private int h;
   private int I;
   private int g;
   private int d;
   private _p.t C = _p.t.g;
   private String n = "";
   private String S = "";
   private float m;
   private LocalPlayer Y;
   private ClientLevel X;
   private String a = "Ready";
   private String T = "";
   private int A;
   private int l;
   private double w;
   private boolean b;
   private boolean U;
   private Vec3 F = Vec3.ZERO;

   public LungeSwapModule() {
      super("Lunge Swap", "Swap to your Lunge spear and jab with an activation key", Category.COMBAT, -1);
      s = this;
      this.J.H(this::j);
      this.e.H(this::s);
      this.t.H(() -> this.s() && this.e.P());
      this.K.H(this.j::P);
      this.v.V(this.p).n("Jab Delay");
      this.L.V(this.B).n("Swap Back Delay");
      this.z.V(this.y).n("Hold Interval");
      this.L.H(this::s);
      this.B.H(this::s);
      this.z.H(this.u::P);
      this.y.H(this.u::P);
      this.f.h(var1 -> this.Z(true));
      this.P.h(var1 -> {
         this.Z(true);
         this.A = 0;
      });
      this.K.h(var1 -> this.Z(true));
   }

   private boolean j() {
      return this.f.S("Legit");
   }

   private boolean K() {
      return this.f.S("Blatant");
   }

   private boolean s() {
      return this.K() || this.J.P();
   }

   public static int S(boolean var0) {
      return _p.C(var0);
   }

   public static int z(double var0, double var2, double var4) {
      return zd.f((int)var0, (int)var2, var4);
   }

   private int B(d9 var1, d9 var2) {
      return z(var1.P(), var2.P(), ThreadLocalRandom.current().nextDouble());
   }

   public static int K(boolean var0, boolean var1, int var2, int var3, int var4, int var5) {
      int var6 = var1 ? var2 : var3;
      return var0 && var4 == var5 && var6 >= 0 && var6 < 9 && var6 != var4 ? var6 : -1;
   }

   public static _p.t G(boolean var0, String var1, Boolean var2, boolean var3) {
      if (!var0 || "Kept".equals(var1)) {
         return _p.t.g;
      } else if ("Reset".equals(var1)) {
         return _p.t.e;
      } else if (var2 != null) {
         return var2 ? _p.t.e : _p.t.g;
      } else {
         return var3 ? _p.t.e : _p.t.g;
      }
   }

   public static void T(int var0) {
      if (s != null && s.isEnabled()) {
         LungeSwapModule var1 = s;
         if (var1 != null && var1.isRuntimeActive() && cc.N(var0) && var0 == var1.P.P()) {
            var1.O = (boolean)1;
         }
      }
   }

   public boolean U() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.V != LungeSwapModule.f.F && this.Y != null && this.Y == getMc().player;
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.Z;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z, dm.j);
   }

   @Override
   public String getStatus() {
      if (!this.isEnabled()) {
         return "";
      } else {
         return this.V == LungeSwapModule.f.F && !this.T.isEmpty() ? cc.u(this.P.P()) + " also triggers " + this.T : this.a;
      }
   }

   @Override
   public void onEnable() {
      this.h();
      this.Y = mc.player;
      this.X = mc.level;
      this.a = "Ready";
      this.T = "";
      this.A = 0;
      mt.K(
         "LungeSwap",
         "Enabled; mode={}, activation key={}, hold={}, charge after swap={}, swap rule={}, jab delay {}-{}, swap back delay {}-{}, hold interval {}-{} ticks",
         this.f.P(),
         this.P.P(),
         this.u.P(),
         this.j.P(),
         this.K.P(),
         this.v.P().intValue(),
         this.p.P().intValue(),
         this.L.P().intValue(),
         this.B.P().intValue(),
         this.z.P().intValue(),
         this.y.P().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.Z(true);
      this.Y = null;
      this.X = null;
   }

   private boolean d() {
      return this.isInGame()
         && this.Y == mc.player
         && this.X == mc.level
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && mc.gui.screen() == null
         && (w7.O(mc) || tq.Q() && w7.m(mc, tq.y.P));
   }

   @Override
   public void onTick() {
      boolean var1 = this.O;
      this.O = (boolean)0;
      if (--this.A <= 0) {
         this.A = 20;
         this.N();
      }

      if (this.Y != mc.player || this.X != mc.level) {
         this.Z(false);
         this.Y = mc.player;
         this.X = mc.level;
         this.l = 0;
      }

      if (this.l > 0) {
         this.t();
      }

      if (!this.d()) {
         this.Z(true);
         this.a = "Paused";
      } else {
         boolean var2 = cc.N(mc.getWindow().handle(), this.P.P());
         boolean var6 = false;
         int var5 = this.q;
         if ((-Integer.signum(var5) >>> 31 ^ 1) == 0 && (!var2 || var1)) {
            this.q = 0;
         }

         label60:
         if (this.V == LungeSwapModule.f.F) {
            if (!var1) {
               if (!this.u.P() || !var2) {
                  break label60;
               }

               int var10000 = this.q;
               byte var8 = 1;
               int var7 = this.q;
               this.q = var7 - var8;
               boolean var10 = false;
               int var9 = var10000;
               if (-Integer.signum(var9) >>> 31 != 0) {
                  break label60;
               }
            }

            this.W(var1);
         }

         if (this.V == LungeSwapModule.f.z) {
            this.n();
         } else if (this.V == LungeSwapModule.f.a) {
            this.Q();
         } else {
            if (this.V == LungeSwapModule.f.f) {
               int var11 = --this.N;
               boolean var4 = false;
               int var3 = var11;
               if (-Integer.signum(var3) >>> 31 == 0) {
                  this.m();
                  if (this.u.P() && var2) {
                     this.q = this.B(this.z, this.y);
                  }

                  this.a = "Ready";
                  return;
               }
            }

            if (this.V == LungeSwapModule.f.F && !cc.N(this.P.P())) {
               this.a = "Set an Activation Key";
            }
         }
      }
   }

   public static String a(Module var0, int var1, Iterable<Module> var2) {
      if (!cc.N(var1)) {
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

            for (df var6 : var4.getSettings()) {
               if (var6 instanceof dv var7 && var7.P() == var1) {
                  return var4.getName();
               }
            }
         }
      }

      return "";
   }

   private void N() {
      LarpClient var1 = LarpClient.F();
      int var2 = this.P.P();
      String var3 = var1 != null && var1.x() != null ? a(this, var2, var1.x().C()) : "";
      if (!var3.isEmpty() && !var3.equals(this.T)) {
         mt.T(
            "LungeSwap",
            "Activation Key {} is also bound to {}; one press triggers both, and a movement module such as Fly cancels the lunge. Rebind one of them",
            cc.u(var2),
            var3
         );
      }

      this.T = var3;
   }

   private void t() {
      LocalPlayer var1 = mc.player;
      this.w = Math.max(this.w, var1.getDeltaMovement().horizontalDistance());
      if (--this.l <= 0) {
         int var2 = this.w >= 0.4 ? 1 : 0;
         mt.K(
            "LungeSwap",
            "Jab result: peak horizontal speed {} blocks/tick, moved {} blocks in {} ticks ({})",
            String.format(Locale.ROOT, "%.3f", this.w),
            String.format(Locale.ROOT, "%.2f", var1.position().subtract(this.F).horizontalDistance()),
            12,
            var2 != 0 ? "lunge push arrived" : "no lunge push; the server refused it or another movement owner replaced it"
         );
         if (this.b && this.j.P() && this.K.S("Detect")) {
            String var3 = this.K();
            Boolean var4 = o.get(var3);
            o.put(var3, var2 == 0);
            if (var2 == 0 && !Boolean.TRUE.equals(var4)) {
               mt.K("LungeSwap", "A swap-tick jab did not lunge on {}; later presses there wait for the spear's own charge", var3);
            } else if (var2 != 0 && var4 == null) {
               mt.K("LungeSwap", "{} keeps the charge across a swap; presses there jab in the swap tick", var3);
            }
         }
      }
   }

   private String K() {
      ServerData var1 = mc.getCurrentServer();
      return var1 != null ? var1.ip : (mc.hasSingleplayerServer() ? "singleplayer" : "this server");
   }

   private void W(boolean var1) {
      String var2 = this.k();
      if (var2 != null) {
         this.a = var2;
         if (var1) {
            mt.K("LungeSwap", "Press refused: {}", var2);
         }
      } else if (this.I() == null) {
         this.a = "No Lunge spear in hotbar";
         if (var1) {
            mt.K("LungeSwap", "Press ignored: no Lunge spear in the hotbar");
         }
      } else {
         this.D = mc.player.getInventory().getSelectedSlot();
         this.n = mc.player.getMainHandItem().getHoverName().getString();
         this.m = mc.player.getAttackStrengthScale(0.0F);
         this.C = G(this.j.P(), this.K.P(), o.get(this.K()), c2.C());
         this.c = this.h = 0;
         this.I = -1;
         this.S = "";
         this.H = this.B(this.v, this.p);
         this.V = LungeSwapModule.f.z;
      }
   }

   private String k() {
      LocalPlayer var1 = mc.player;
      return _p.B(var1.getFoodData().getFoodLevel(), var1.getAbilities().instabuild, var1.isInWater(), var1.isPassenger(), var1.isFallFlying());
   }

   private _p.a I() {
      LocalPlayer var1 = mc.player;
      ItemStack var2 = var1.getMainHandItem();
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < 9; var4++) {
         int var5 = this.e(var4);
         if (var5 > 0) {
            ItemStack var6 = var1.getInventory().getItem(var4);
            if (var6.isItemEnabled(this.X.enabledFeatures())) {
               var3.add(
                  new _p.x(
                     var4,
                     l(var1, var6),
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

      return _p.w(
         var3,
         var1.getInventory().getSelectedSlot(),
         this.C,
         ((qccc4a2dfe8596cd6)var1).larpAttackStrengthTicker(),
         var1.getCurrentItemAttackStrengthDelay(),
         this.K()
      );
   }

   private static float l(LocalPlayer var0, ItemStack var1) {
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

   private void n() {
      _p.a var1 = this.I();
      if (var1 == null) {
         this.h();
         this.a = "No Lunge spear in hotbar";
      } else {
         if (this.I < 0) {
            this.I = var1.ticks();
            mt.K(
               "LungeSwap",
               "Press: {} rule, slot {} (Lunge {}) from slot {} ({}, charge {}); {}",
               this.C,
               var1.slot() + 1,
               var1.lunge(),
               this.D + 1,
               this.n,
               Q(this.m),
               var1.ticks() == 0 ? (var1.swap() ? "swap and jab now" : "jab now") : "server needs " + var1.ticks() + " more ticks"
            );
         }

         this.M = var1.slot();
         if (this.H > 0) {
            this.H--;
            this.a = "Lunge in " + _p.d(Math.max(this.H + 1, var1.ticks()));
            this.S("Jab Delay");
         } else if (var1.ticks() > 0 && !var1.resets()) {
            this.c++;
            this.a = "Lunge in " + _p.d(var1.ticks());
            this.S("charge");
            if (this.c > this.I + 10 + 2) {
               this.Y("the charge never passed");
            }
         } else if (!this.O()) {
            String var2 = this.D();
            if (var2 != null) {
               this.S(var2);
               this.a = "Waiting for " + var2;
               if (++this.h > 10) {
                  this.Y(var2);
               }
            } else {
               this.a = "Lunging";
               _m.F(this, this::y, () -> this.D() == null && this.V == LungeSwapModule.f.z);
            }
         }
      }
   }

   private boolean O() {
      if (!tq.Q()) {
         return false;
      }

      tq.j("a Lunge Swap jab");
      this.S("your look");
      this.a = "Lunging";
      return true;
   }

   private String D() {
      if (!this.isRuntimeActive() || !this.d()) {
         return "the game";
      } else if (!ti.V(this)) {
         return "the hotbar";
      } else if (!w7.f(mc, mc.player)) {
         return "free hands";
      } else if (this.j() && w7.O(mc) > 0) {
         return "the miss delay";
      } else if (this.k() != null) {
         return "the Lunge conditions";
      } else {
         return this.M >= 0 && this.F(this.M) ? null : "the spear";
      }
   }

   private void S(String var1) {
      if (!var1.equals(this.S)) {
         this.S = var1;
         mt.K("LungeSwap", "Jab held by {}", var1);
      }
   }

   private void Y(String var1) {
      mt.K("LungeSwap", "Press lapsed after {} ticks, held by {}", this.c + this.h, var1);
      this.Z(true);
      this.a = "Lunge blocked by " + var1;
   }

   private void y() {
      _p.a var1 = this.I();
      if (this.V == LungeSwapModule.f.z && var1 != null && (var1.ticks() <= 0 || var1.resets()) && this.D() == null) {
         LocalPlayer var2 = mc.player;
         this.M = var1.slot();
         boolean var3 = var1.swap();
         if (var1.resets()) {
            this.d = _p.H(this.v(this.M), false, this.C, 0, 0.0F, this.K());
            var2.getInventory().setSelectedSlot(this.M);
            this.V = LungeSwapModule.f.a;
            this.g = 0;
            this.a = "Charging spear " + _p.d(this.d);
            mt.K("LungeSwap", "Selected spear slot {}; the server restarts its charge, so the jab waits {} ticks", this.M + 1, this.d);
         } else {
            this.U = (boolean)(var3 && !ItemStack.isSameItem(var2.getMainHandItem(), var2.getInventory().getItem(this.M)) ? 1 : 0);
            var2.getInventory().setSelectedSlot(this.M);
            this.i(var3);
         }
      }
   }

   private _p.x v(int var1) {
      LocalPlayer var2 = mc.player;
      ItemStack var3 = var2.getInventory().getItem(var1);
      return new _p.x(var1, l(var2, var3), (Float)var3.getOrDefault(DataComponents.MINIMUM_ATTACK_CHARGE, 0.0F), this.e(var1), 0, false, false);
   }

   private void Q() {
      LocalPlayer var1 = mc.player;
      this.g++;
      if (var1.getInventory().getSelectedSlot() == this.M && this.F(this.M)) {
         int var2 = this.d - this.g;
         if (var2 > 0) {
            this.a = "Charging spear " + _p.d(var2);
         } else if (!this.O()) {
            String var3 = this.D();
            if (var3 != null) {
               this.S(var3);
               this.a = "Waiting for " + var3;
               if (++this.h > 10) {
                  mt.K("LungeSwap", "Charged jab lapsed, held by {}", var3);
                  _m.w(this);
                  this.m();
                  this.a = "Lunge blocked by " + var3;
               }
            } else {
               _m.F(this, () -> {
                  if (this.V == LungeSwapModule.f.a && this.D() == null) {
                     this.i(true);
                  }
               }, () -> this.V == LungeSwapModule.f.a && this.D() == null);
            }
         }
      } else {
         mt.K("LungeSwap", "Charge interrupted after {} ticks: slot {} selected", this.g, var1.getInventory().getSelectedSlot() + 1);
         _m.w(this);
         this.h();
         this.a = "Swap interrupted";
      }
   }

   private void i(boolean var1) {
      LocalPlayer var2 = mc.player;
      ItemStack var3 = var2.getInventory().getItem(this.M);
      int var4 = var1 && this.V == LungeSwapModule.f.z ? 1 : 0;
      if (var3.isItemEnabled(this.X.enabledFeatures())) {
         mc.gameMode.piercingAttack(var3.getAttackAnimation(), (PiercingWeapon)var3.get(DataComponents.PIERCING_WEAPON));
         this.a = "Lunged";
         this.l = 12;
         this.w = 0.0;
         this.F = var2.position();
         this.b = (boolean)(var4 != 0 && this.U ? 1 : 0);
      } else {
         this.a = "Spear not enabled here";
      }

      int var5 = this.c + this.h + this.g;
      this.V = LungeSwapModule.f.f;
      this.N = this.B(this.L, this.B);
      mt.K(
         "LungeSwap",
         "{}: {} {} rule, slot {} ({}, charge {}) to spear slot {} (Lunge {}) {} ticks after the press{}; food {}, ground {}, sprint {}",
         this.a,
         this.f.P(),
         this.C,
         this.D + 1,
         this.n,
         Q(this.m),
         this.M + 1,
         this.e(this.M),
         var5,
         var4 != 0 ? ", swapped in the jab tick" : (var1 ? ", spear charged after the swap" : ", spear already held"),
         var2.getFoodData().getFoodLevel(),
         var2.onGround(),
         var2.isSprinting()
      );
      if (this.N > 1) {
         mt.K("LungeSwap", "Swap back in {} ticks", this.N);
      }
   }

   private void m() {
      LocalPlayer var1 = getMc() == null ? null : mc.player;
      if (var1 != null && var1 == this.Y && var1.level() == this.X) {
         int var2 = K(this.s(), this.e.P(), this.t.P().intValue() - 1, this.D, var1.getInventory().getSelectedSlot(), this.M);
         if (var2 >= 0) {
            var1.getInventory().setSelectedSlot(var2);
         }
      }

      this.h();
   }

   private void Z(boolean var1) {
      _m.w(this);
      if (this.V != LungeSwapModule.f.F) {
         mt.K("LungeSwap", "Cancelled at {}; restore={}, status {}", this.V, var1, this.a);
      }

      if (!var1 || this.V != LungeSwapModule.f.f && this.V != LungeSwapModule.f.a) {
         this.h();
      } else {
         this.m();
      }
   }

   private void h() {
      this.V = LungeSwapModule.f.F;
      this.U = (boolean)0;
      this.D = this.M = -1;
      this.H = this.N = 0;
      this.c = this.h = this.g = this.d = 0;
      this.I = -1;
      this.O = (boolean)0;
      this.S = "";
   }

   private boolean F(int var1) {
      return this.e(var1) > 0;
   }

   private int e(int var1) {
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

   private static String Q(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   public enum f {
      F,
      z,
      a,
      f;
   }
}

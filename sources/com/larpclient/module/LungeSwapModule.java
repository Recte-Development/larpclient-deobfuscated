package com.larpclient.module;

import com.larp.obf.q5682c6a8aca9b6ec.q036f9c7ffe4c5a07;
import com.larpclient.LarpClient;
import com.larpclient.d8;
import com.larpclient.e5;
import com.larpclient.j5;
import com.larpclient.o_;
import com.larpclient.oz;
import com.larpclient.pe;
import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.yh;
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
   private static LungeSwapModule g;
   public static final int T = 10;
   public static final int a = 12;
   public static final double D = 0.4;
   private final lp x = this.addSetting(
      new lp("Mode", "Legit waits for full charges like vanilla; Blatant jabs as soon as the server accepts it", "Legit", "Legit", "Blatant")
   );
   private final la Q = this.addSetting(new la("Activation Key", "Press to swap to your Lunge spear and jab; separate from the module toggle", 86));
   private final ln v = this.addSetting(new ln("Swap Back", "Return after the jab; Blatant always returns", true));
   private final ln X = this.addSetting(new ln("Swap Back To Slot", "Return to a fixed hotbar slot instead of your previous one", false));
   private final lc R = this.addSetting(new lc("Swap Back Slot", "Hotbar slot to return to", 1.0, 1.0, 9.0, 1.0));
   private final ln P = this.addSetting(new ln("Hold To Lunge", "Keep jabbing while the activation key is held", false));
   private final ln h = this.addSetting(
      new ln(
         "Charge After Swap", "On a server that resets charge on a swap, hold the spear until its own charge is ready. Off always jabs in the swap tick", true
      )
   );
   private final lp I = this.addSetting(
      new lp(
         "Swap Rule",
         "How the server treats charge across a swap. Detect jabs in the swap tick and waits for the spear's charge on a server where that did not lunge",
         "Detect",
         "Detect",
         "Kept",
         "Reset"
      )
   );
   private final lc t = this.addSetting(
      new lc(
         "Jab Delay",
         "Fewest ticks from your press to the swap and jab; your held item keeps charging meanwhile. 0 jabs in the press tick",
         0.0,
         0.0,
         10.0,
         1.0
      )
   );
   private final lc s = this.addSetting(new lc("Jab Delay Max", "Most ticks from your press to the swap and jab", 0.0, 0.0, 10.0, 1.0));
   private final lc z = this.addSetting(
      new lc("Swap Back Delay", "Fewest ticks after the jab before you return to your slot. 1 is the next tick", 1.0, 1.0, 20.0, 1.0)
   );
   private final lc V = this.addSetting(new lc("Swap Back Delay Max", "Most ticks after the jab before you return to your slot", 1.0, 1.0, 20.0, 1.0));
   private final lc u = this.addSetting(new lc("Hold Interval", "Fewest extra ticks between lunges while Hold To Lunge is held", 0.0, 0.0, 40.0, 1.0));
   private final lc e = this.addSetting(new lc("Hold Interval Max", "Most extra ticks between lunges while Hold To Lunge is held", 0.0, 0.0, 40.0, 1.0));
   private static final Map<String, Boolean> E = new HashMap<>();
   private LungeSwapModule.c U = LungeSwapModule.c.P;
   private boolean N;
   private int m;
   private int G;
   private int i;
   private int F = -1;
   private int O = -1;
   private int C;
   private int Z;
   private int J;
   private int c;
   private int H;
   private e5.g B = e5.g.g;
   private String S = "";
   private String k = "";
   private float r;
   private LocalPlayer A;
   private ClientLevel o;
   private String W = "Ready";
   private String d = "";
   private int w;
   private int K;
   private double M;
   private boolean q;
   private boolean Y;
   private Vec3 b = Vec3.ZERO;

   public LungeSwapModule() {
      super("Lunge Swap", "Swap to your Lunge spear and jab with an activation key", Category.COMBAT, -1);
      g = this;
      this.v.x(this::a);
      this.X.x(this::c);
      this.R.x(() -> this.c() && this.X.getValue());
      this.I.x(this.h::getValue);
      this.t.A(this.s).b("Jab Delay");
      this.z.A(this.V).b("Swap Back Delay");
      this.u.A(this.e).b("Hold Interval");
      this.z.x(this::c);
      this.V.x(this::c);
      this.u.x(this.P::getValue);
      this.e.x(this.P::getValue);
      this.x.W(var1 -> this.T(true));
      this.Q.W(var1 -> {
         this.T(true);
         this.w = 0;
      });
      this.I.W(var1 -> this.T(true));
   }

   private boolean a() {
      return this.x.i("Legit");
   }

   private boolean f() {
      return this.x.i("Blatant");
   }

   private boolean c() {
      return this.f() || this.v.getValue();
   }

   public static int E(boolean var0) {
      return e5.o(var0);
   }

   public static int b(double var0, double var2, double var4) {
      return yh.Z((int)var0, (int)var2, var4);
   }

   private int n(lc var1, lc var2) {
      return b(var1.getValue(), var2.getValue(), ThreadLocalRandom.current().nextDouble());
   }

   public static int q(boolean var0, boolean var1, int var2, int var3, int var4, int var5) {
      int var6 = var1 ? var2 : var3;
      return var0 && var4 == var5 && var6 >= 0 && var6 < 9 && var6 != var4 ? var6 : -1;
   }

   public static e5.g k(boolean var0, String var1, Boolean var2, boolean var3) {
      if (!var0 || "Kept".equals(var1)) {
         return e5.g.g;
      } else if ("Reset".equals(var1)) {
         return e5.g.N;
      } else if (var2 != null) {
         return var2 ? e5.g.N : e5.g.g;
      } else {
         return var3 ? e5.g.N : e5.g.g;
      }
   }

   public static void l(int var0) {
      if (g != null && g.isEnabled()) {
         LungeSwapModule var1 = g;
         if (var1 != null && var1.isRuntimeActive() && pe.F(var0) && var0 == var1.Q.getValue()) {
            var1.N = (boolean)1;
         }
      }
   }

   public boolean U() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.U != LungeSwapModule.c.P && this.A != null && this.A == getMc().player;
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.w;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a, ld.c);
   }

   @Override
   public String getStatus() {
      if (!this.isEnabled()) {
         return "";
      } else {
         return this.U == LungeSwapModule.c.P && !this.d.isEmpty() ? pe.A(this.Q.getValue()) + " also triggers " + this.d : this.W;
      }
   }

   @Override
   public void onEnable() {
      this.W();
      this.A = mc.player;
      this.o = mc.level;
      this.W = "Ready";
      this.d = "";
      this.w = 0;
      r4.V(
         "LungeSwap",
         "Enabled; mode={}, activation key={}, hold={}, charge after swap={}, swap rule={}, jab delay {}-{}, swap back delay {}-{}, hold interval {}-{} ticks",
         this.x.getValue(),
         this.Q.getValue(),
         this.P.getValue(),
         this.h.getValue(),
         this.I.getValue(),
         this.t.getValue().intValue(),
         this.s.getValue().intValue(),
         this.z.getValue().intValue(),
         this.V.getValue().intValue(),
         this.u.getValue().intValue(),
         this.e.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.T(true);
      this.A = null;
      this.o = null;
   }

   private boolean V() {
      return this.isInGame()
         && this.A == mc.player
         && this.o == mc.level
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && mc.gui.screen() == null
         && (j5.A(mc) || rr.n() && j5.o(mc, rr.f.E));
   }

   @Override
   public void onTick() {
      boolean var1 = this.N;
      this.N = (boolean)0;
      int var10000 = --this.w;
      boolean var4 = false;
      int var3 = var10000;
      if (-Integer.signum(var3) >>> 31 == 0) {
         this.w = 20;
         this.X();
      }

      if (this.A != mc.player || this.o != mc.level) {
         this.T(false);
         this.A = mc.player;
         this.o = mc.level;
         this.K = 0;
      }

      if (this.K > 0) {
         this.G();
      }

      if (!this.V()) {
         this.T(true);
         this.W = "Paused";
      } else {
         boolean var2 = pe.A(mc.getWindow().handle(), this.Q.getValue());
         boolean var6 = false;
         int var5 = this.i;
         if ((-Integer.signum(var5) >>> 31 ^ 1) == 0 && (!var2 || var1)) {
            this.i = 0;
         }

         label60:
         if (this.U == LungeSwapModule.c.P) {
            if (!var1) {
               if (!this.P.getValue() || !var2) {
                  break label60;
               }

               var10000 = this.i;
               byte var8 = 1;
               int var7 = this.i;
               this.i = var7 - var8;
               boolean var10 = false;
               int var9 = var10000;
               if (-Integer.signum(var9) >>> 31 != 0) {
                  break label60;
               }
            }

            this.M(var1);
         }

         if (this.U == LungeSwapModule.c.L) {
            this.F();
         } else if (this.U == LungeSwapModule.c.J) {
            this.Y();
         } else if (this.U == LungeSwapModule.c.i && --this.G <= 0) {
            this.A();
            if (this.P.getValue() && var2) {
               this.i = this.n(this.u, this.e);
            }

            this.W = "Ready";
         } else if (this.U == LungeSwapModule.c.P && !pe.F(this.Q.getValue())) {
            this.W = "Set an Activation Key";
         }
      }
   }

   public static String Z(Module var0, int var1, Iterable<Module> var2) {
      if (!pe.F(var1)) {
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

            for (l6 var6 : var4.getSettings()) {
               if (var6 instanceof la var7 && var7.getValue() == var1) {
                  return var4.getName();
               }
            }
         }
      }

      return "";
   }

   private void X() {
      LarpClient var1 = LarpClient.y();
      int var2 = this.Q.getValue();
      String var3 = var1 != null && var1.e() != null ? Z(this, var2, var1.e().X()) : "";
      if (!var3.isEmpty() && !var3.equals(this.d)) {
         r4.m(
            "LungeSwap",
            "Activation Key {} is also bound to {}; one press triggers both, and a movement module such as Fly cancels the lunge. Rebind one of them",
            pe.A(var2),
            var3
         );
      }

      this.d = var3;
   }

   private void G() {
      LocalPlayer var1 = mc.player;
      this.M = Math.max(this.M, var1.getDeltaMovement().horizontalDistance());
      if (--this.K <= 0) {
         int var2 = this.M >= 0.4 ? 1 : 0;
         r4.V(
            "LungeSwap",
            "Jab result: peak horizontal speed {} blocks/tick, moved {} blocks in {} ticks ({})",
            String.format(Locale.ROOT, "%.3f", this.M),
            String.format(Locale.ROOT, "%.2f", var1.position().subtract(this.b).horizontalDistance()),
            12,
            var2 != 0 ? "lunge push arrived" : "no lunge push; the server refused it or another movement owner replaced it"
         );
         if (this.q && this.h.getValue() && this.I.i("Detect")) {
            String var3 = this.b();
            Boolean var4 = E.get(var3);
            E.put(var3, var2 == 0);
            if (var2 == 0 && !Boolean.TRUE.equals(var4)) {
               r4.V("LungeSwap", "A swap-tick jab did not lunge on {}; later presses there wait for the spear's own charge", var3);
            } else if (var2 != 0 && var4 == null) {
               r4.V("LungeSwap", "{} keeps the charge across a swap; presses there jab in the swap tick", var3);
            }
         }
      }
   }

   private String b() {
      ServerData var1 = mc.getCurrentServer();
      return var1 != null ? var1.ip : (mc.hasSingleplayerServer() ? "singleplayer" : "this server");
   }

   private void M(boolean var1) {
      String var2 = this.I();
      if (var2 != null) {
         this.W = var2;
         if (var1) {
            r4.V("LungeSwap", "Press refused: {}", var2);
         }
      } else if (this.q() == null) {
         this.W = "No Lunge spear in hotbar";
         if (var1) {
            r4.V("LungeSwap", "Press ignored: no Lunge spear in the hotbar");
         }
      } else {
         this.F = mc.player.getInventory().getSelectedSlot();
         this.S = mc.player.getMainHandItem().getHoverName().getString();
         this.r = mc.player.getAttackStrengthScale(0.0F);
         this.B = k(this.h.getValue(), this.I.getValue(), E.get(this.b()), oz.w());
         this.C = this.Z = 0;
         this.J = -1;
         this.k = "";
         this.m = this.n(this.t, this.s);
         this.U = LungeSwapModule.c.L;
      }
   }

   private String I() {
      LocalPlayer var1 = mc.player;
      return e5.C(var1.getFoodData().getFoodLevel(), var1.getAbilities().instabuild, var1.isInWater(), var1.isPassenger(), var1.isFallFlying());
   }

   private e5.j q() {
      LocalPlayer var1 = mc.player;
      ItemStack var2 = var1.getMainHandItem();
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < 9; var4++) {
         int var5 = this.m(var4);
         if (var5 > 0) {
            ItemStack var6 = var1.getInventory().getItem(var4);
            if (var6.isItemEnabled(this.o.enabledFeatures())) {
               var3.add(
                  new e5.m(
                     var4,
                     y(var1, var6),
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

      return e5.a(
         var3,
         var1.getInventory().getSelectedSlot(),
         this.B,
         ((q036f9c7ffe4c5a07)var1).larpAttackStrengthTicker(),
         var1.getCurrentItemAttackStrengthDelay(),
         this.f()
      );
   }

   private static float y(LocalPlayer var0, ItemStack var1) {
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

   private void F() {
      e5.j var1 = this.q();
      if (var1 == null) {
         this.W();
         this.W = "No Lunge spear in hotbar";
      } else {
         if (this.J < 0) {
            this.J = var1.ticks();
            r4.V(
               "LungeSwap",
               "Press: {} rule, slot {} (Lunge {}) from slot {} ({}, charge {}); {}",
               this.B,
               var1.slot() + 1,
               var1.lunge(),
               this.F + 1,
               this.S,
               w(this.r),
               var1.ticks() == 0 ? (var1.swap() ? "swap and jab now" : "jab now") : "server needs " + var1.ticks() + " more ticks"
            );
         }

         this.O = var1.slot();
         if (this.m > 0) {
            this.m--;
            this.W = "Lunge in " + e5.c(Math.max(this.m + 1, var1.ticks()));
            this.v("Jab Delay");
         } else if (var1.ticks() > 0 && !var1.resets()) {
            this.C++;
            this.W = "Lunge in " + e5.c(var1.ticks());
            this.v("charge");
            if (this.C > this.J + 10 + 2) {
               this.b("the charge never passed");
            }
         } else if (!this.H()) {
            String var2 = this.A();
            if (var2 != null) {
               this.v(var2);
               this.W = "Waiting for " + var2;
               if (++this.Z > 10) {
                  this.b(var2);
               }
            } else {
               this.W = "Lunging";
               d8.A(this, this::J, () -> this.A() == null && this.U == LungeSwapModule.c.L);
            }
         }
      }
   }

   private boolean H() {
      if (!rr.n()) {
         return false;
      }

      rr.T("a Lunge Swap jab");
      this.v("your look");
      this.W = "Lunging";
      return true;
   }

   private String A() {
      if (!this.isRuntimeActive() || !this.V()) {
         return "the game";
      } else if (!o_.N(this)) {
         return "the hotbar";
      } else if (!j5.E(mc, mc.player)) {
         return "free hands";
      } else if (this.a() && j5.w(mc) > 0) {
         return "the miss delay";
      } else if (this.I() != null) {
         return "the Lunge conditions";
      } else {
         return this.O >= 0 && this.Q(this.O) ? null : "the spear";
      }
   }

   private void v(String var1) {
      if (!var1.equals(this.k)) {
         this.k = var1;
         r4.V("LungeSwap", "Jab held by {}", var1);
      }
   }

   private void b(String var1) {
      r4.V("LungeSwap", "Press lapsed after {} ticks, held by {}", this.C + this.Z, var1);
      this.T(true);
      this.W = "Lunge blocked by " + var1;
   }

   private void J() {
      e5.j var1 = this.q();
      if (this.U == LungeSwapModule.c.L && var1 != null && (var1.ticks() <= 0 || var1.resets()) && this.A() == null) {
         LocalPlayer var2 = mc.player;
         this.O = var1.slot();
         boolean var3 = var1.swap();
         if (var1.resets()) {
            this.H = e5.q(this.R(this.O), false, this.B, 0, 0.0F, this.f());
            var2.getInventory().setSelectedSlot(this.O);
            this.U = LungeSwapModule.c.J;
            this.c = 0;
            this.W = "Charging spear " + e5.c(this.H);
            r4.V("LungeSwap", "Selected spear slot {}; the server restarts its charge, so the jab waits {} ticks", this.O + 1, this.H);
         } else {
            this.Y = (boolean)(var3 && !ItemStack.isSameItem(var2.getMainHandItem(), var2.getInventory().getItem(this.O)) ? 1 : 0);
            var2.getInventory().setSelectedSlot(this.O);
            this.S(var3);
         }
      }
   }

   private e5.m R(int var1) {
      LocalPlayer var2 = mc.player;
      ItemStack var3 = var2.getInventory().getItem(var1);
      return new e5.m(var1, y(var2, var3), (Float)var3.getOrDefault(DataComponents.MINIMUM_ATTACK_CHARGE, 0.0F), this.m(var1), 0, false, false);
   }

   private void Y() {
      LocalPlayer var1 = mc.player;
      this.c++;
      if (var1.getInventory().getSelectedSlot() == this.O && this.Q(this.O)) {
         int var2 = this.H - this.c;
         if (var2 > 0) {
            this.W = "Charging spear " + e5.c(var2);
         } else if (!this.H()) {
            String var3 = this.A();
            if (var3 != null) {
               this.v(var3);
               this.W = "Waiting for " + var3;
               if (++this.Z > 10) {
                  r4.V("LungeSwap", "Charged jab lapsed, held by {}", var3);
                  d8.K(this);
                  this.A();
                  this.W = "Lunge blocked by " + var3;
               }
            } else {
               d8.A(this, () -> {
                  if (this.U == LungeSwapModule.c.J && this.A() == null) {
                     this.S(true);
                  }
               }, () -> this.U == LungeSwapModule.c.J && this.A() == null);
            }
         }
      } else {
         r4.V("LungeSwap", "Charge interrupted after {} ticks: slot {} selected", this.c, var1.getInventory().getSelectedSlot() + 1);
         d8.K(this);
         this.W();
         this.W = "Swap interrupted";
      }
   }

   private void S(boolean var1) {
      LocalPlayer var2 = mc.player;
      ItemStack var3 = var2.getInventory().getItem(this.O);
      int var4 = var1 && this.U == LungeSwapModule.c.L ? 1 : 0;
      if (var3.isItemEnabled(this.o.enabledFeatures())) {
         mc.gameMode.piercingAttack(var3.getAttackAnimation(), (PiercingWeapon)var3.get(DataComponents.PIERCING_WEAPON));
         this.W = "Lunged";
         this.K = 12;
         this.M = 0.0;
         this.b = var2.position();
         this.q = (boolean)(var4 != 0 && this.Y ? 1 : 0);
      } else {
         this.W = "Spear not enabled here";
      }

      int var5 = this.C + this.Z + this.c;
      this.U = LungeSwapModule.c.i;
      this.G = this.n(this.z, this.V);
      r4.V(
         "LungeSwap",
         "{}: {} {} rule, slot {} ({}, charge {}) to spear slot {} (Lunge {}) {} ticks after the press{}; food {}, ground {}, sprint {}",
         this.W,
         this.x.getValue(),
         this.B,
         this.F + 1,
         this.S,
         w(this.r),
         this.O + 1,
         this.m(this.O),
         var5,
         var4 != 0 ? ", swapped in the jab tick" : (var1 ? ", spear charged after the swap" : ", spear already held"),
         var2.getFoodData().getFoodLevel(),
         var2.onGround(),
         var2.isSprinting()
      );
      if (this.G > 1) {
         r4.V("LungeSwap", "Swap back in {} ticks", this.G);
      }
   }

   private void A() {
      LocalPlayer var1 = getMc() == null ? null : mc.player;
      if (var1 != null && var1 == this.A && var1.level() == this.o) {
         int var2 = q(this.c(), this.X.getValue(), this.R.getValue().intValue() - 1, this.F, var1.getInventory().getSelectedSlot(), this.O);
         if (var2 >= 0) {
            var1.getInventory().setSelectedSlot(var2);
         }
      }

      this.W();
   }

   private void T(boolean var1) {
      d8.K(this);
      if (this.U != LungeSwapModule.c.P) {
         r4.V("LungeSwap", "Cancelled at {}; restore={}, status {}", this.U, var1, this.W);
      }

      if (!var1 || this.U != LungeSwapModule.c.i && this.U != LungeSwapModule.c.J) {
         this.W();
      } else {
         this.A();
      }
   }

   private void W() {
      this.U = LungeSwapModule.c.P;
      this.Y = (boolean)0;
      this.F = this.O = -1;
      this.m = this.G = 0;
      this.C = this.Z = this.c = this.H = 0;
      this.J = -1;
      this.N = (boolean)0;
      this.k = "";
   }

   private boolean Q(int var1) {
      return this.m(var1) > 0;
   }

   private int m(int var1) {
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

   private static String w(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   public enum c {
      P,
      L,
      J,
      i;
   }
}

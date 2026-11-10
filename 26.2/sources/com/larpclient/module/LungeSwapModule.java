package com.larpclient.module;

import com.larp.obf.qa300483f54d804a3.q56b215c3695b1780;
import com.larpclient.LarpClient;
import com.larpclient.bc;
import com.larpclient.hu;
import com.larpclient.hx;
import com.larpclient.l6;
import com.larpclient.lu;
import com.larpclient.p_;
import com.larpclient.si;
import com.larpclient.ta;
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
import net.minecraft.world.InteractionHand;
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
   private static LungeSwapModule r;
   public static final int Y = 10;
   public static final int H = 12;
   public static final double l = 0.4;
   private final l u = this.addSetting(
      new l("Mode", "Legit waits for full charges like vanilla; Blatant jabs as soon as the server accepts it", "Legit", "Legit", "Blatant")
   );
   private final v Q = this.addSetting(new v("Activation Key", "Press to swap to your Lunge spear and jab; separate from the module toggle", 86));
   private final s X = this.addSetting(new s("Swap Back", "Return after the jab; Blatant always returns", true));
   private final s m = this.addSetting(new s("Swap Back To Slot", "Return to a fixed hotbar slot instead of your previous one", false));
   private final e J = this.addSetting(new e("Swap Back Slot", "Hotbar slot to return to", 1.0, 1.0, 9.0, 1.0));
   private final s p = this.addSetting(new s("Hold To Lunge", "Keep jabbing while the activation key is held", false));
   private final s V = this.addSetting(
      new s(
         "Charge After Swap", "On a server that resets charge on a swap, hold the spear until its own charge is ready. Off always jabs in the swap tick", true
      )
   );
   private final l A = this.addSetting(
      new l(
         "Swap Rule",
         "How the server treats charge across a swap. Detect jabs in the swap tick and waits for the spear's charge on a server where that did not lunge",
         "Detect",
         "Detect",
         "Kept",
         "Reset"
      )
   );
   private final e w = this.addSetting(
      new e(
         "Jab Delay",
         "Fewest ticks from your press to the swap and jab; your held item keeps charging meanwhile. 0 jabs in the press tick",
         0.0,
         0.0,
         10.0,
         1.0
      )
   );
   private final e k = this.addSetting(new e("Jab Delay Max", "Most ticks from your press to the swap and jab", 0.0, 0.0, 10.0, 1.0));
   private final e z = this.addSetting(
      new e("Swap Back Delay", "Fewest ticks after the jab before you return to your slot. 1 is the next tick", 1.0, 1.0, 20.0, 1.0)
   );
   private final e h = this.addSetting(new e("Swap Back Delay Max", "Most ticks after the jab before you return to your slot", 1.0, 1.0, 20.0, 1.0));
   private final e B = this.addSetting(new e("Hold Interval", "Fewest extra ticks between lunges while Hold To Lunge is held", 0.0, 0.0, 40.0, 1.0));
   private final e b = this.addSetting(new e("Hold Interval Max", "Most extra ticks between lunges while Hold To Lunge is held", 0.0, 0.0, 40.0, 1.0));
   private static final Map<String, Boolean> q = new HashMap<>();
   private LungeSwapModule.h a = LungeSwapModule.h.n;
   private boolean Z;
   private int g;
   private int c;
   private int O;
   private int x = -1;
   private int P = -1;
   private int W;
   private int N;
   private int K;
   private int n;
   private int o;
   private lu.y j = lu.y.P;
   private String T = "";
   private String f = "";
   private float L;
   private LocalPlayer G;
   private ClientLevel R;
   private String s = "Ready";
   private String d = "";
   private int C;
   private int I;
   private double M;
   private boolean v;
   private boolean i;
   private Vec3 F = Vec3.ZERO;

   public LungeSwapModule() {
      super("Lunge Swap", "Swap to your Lunge spear and jab with an activation key", Category.COMBAT, -1);
      r = this;
      this.X.P(this::Z);
      this.m.P(this::E);
      this.J.P(() -> this.E() && this.m.getValue());
      this.A.P(this.V::getValue);
      this.w.C(this.k).h("Jab Delay");
      this.z.C(this.h).h("Swap Back Delay");
      this.B.C(this.b).h("Hold Interval");
      this.z.P(this::E);
      this.h.P(this::E);
      this.B.P(this.p::getValue);
      this.b.P(this.p::getValue);
      this.u.o(var1 -> this.Z(true));
      this.Q.o(var1 -> {
         this.Z(true);
         this.C = 0;
      });
      this.A.o(var1 -> this.Z(true));
   }

   private boolean Z() {
      return this.u.O("Legit");
   }

   private boolean s() {
      return this.u.O("Blatant");
   }

   private boolean E() {
      return this.s() || this.X.getValue();
   }

   public static int u(boolean var0) {
      return lu.e(var0);
   }

   public static int m(double var0, double var2, double var4) {
      return hx.C((int)var0, (int)var2, var4);
   }

   private int C(e var1, e var2) {
      return m(var1.getValue(), var2.getValue(), ThreadLocalRandom.current().nextDouble());
   }

   public static int B(boolean var0, boolean var1, int var2, int var3, int var4, int var5) {
      int var6 = var1 ? var2 : var3;
      return var0 && var4 == var5 && var6 >= 0 && var6 < 9 && var6 != var4 ? var6 : -1;
   }

   public static lu.y P(boolean var0, String var1, Boolean var2, boolean var3) {
      if (!var0 || "Kept".equals(var1)) {
         return lu.y.P;
      } else if ("Reset".equals(var1)) {
         return lu.y.a;
      } else if (var2 != null) {
         return var2 ? lu.y.a : lu.y.P;
      } else {
         return var3 ? lu.y.a : lu.y.P;
      }
   }

   public static void x(int var0) {
      if (r != null && r.isEnabled()) {
         LungeSwapModule var1 = r;
         if (var1 != null && var1.isRuntimeActive() && bc.a(var0) && var0 == var1.Q.getValue()) {
            var1.Z = (boolean)1;
         }
      }
   }

   public boolean S() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.a != LungeSwapModule.h.n && this.G != null && this.G == getMc().player;
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.K, com.larpclient.module.m.U);
   }

   @Override
   public String getStatus() {
      if (!this.isEnabled()) {
         return "";
      } else {
         return this.a == LungeSwapModule.h.n && !this.d.isEmpty() ? bc.G(this.Q.getValue()) + " also triggers " + this.d : this.s;
      }
   }

   @Override
   public void onEnable() {
      this.f();
      this.G = mc.player;
      this.R = mc.level;
      this.s = "Ready";
      this.d = "";
      this.C = 0;
      com.larpclient.h.A(
         "LungeSwap",
         "Enabled; mode={}, activation key={}, hold={}, charge after swap={}, swap rule={}, jab delay {}-{}, swap back delay {}-{}, hold interval {}-{} ticks",
         this.u.getValue(),
         this.Q.getValue(),
         this.p.getValue(),
         this.V.getValue(),
         this.A.getValue(),
         this.w.getValue().intValue(),
         this.k.getValue().intValue(),
         this.z.getValue().intValue(),
         this.h.getValue().intValue(),
         this.B.getValue().intValue(),
         this.b.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.Z(true);
      this.G = null;
      this.R = null;
   }

   private boolean c() {
      return this.isInGame()
         && this.G == mc.player
         && this.R == mc.level
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && mc.gui.screen() == null
         && (si.P(mc) || p_.R() && si.w(mc, p_.o.R));
   }

   @Override
   public void onTick() {
      boolean var1 = this.Z;
      this.Z = (boolean)0;
      int var10000 = --this.C;
      boolean var4 = false;
      int var3 = var10000;
      if (-Integer.signum(var3) >>> 31 == 0) {
         this.C = 20;
         this.X();
      }

      if (this.G != mc.player || this.R != mc.level) {
         this.Z(false);
         this.G = mc.player;
         this.R = mc.level;
         this.I = 0;
      }

      boolean var6 = false;
      int var5 = this.I;
      if ((-Integer.signum(var5) >>> 31 ^ 1) == 0) {
         this.e();
      }

      if (!this.c()) {
         this.Z(true);
         this.s = "Paused";
      } else {
         boolean var2 = bc.I(mc.getWindow().handle(), this.Q.getValue());
         if (this.O > 0 && (!var2 || var1)) {
            this.O = 0;
         }

         label60:
         if (this.a == LungeSwapModule.h.n) {
            if (!var1) {
               if (!this.p.getValue() || !var2) {
                  break label60;
               }

               var10000 = this.O;
               byte var8 = 1;
               int var7 = this.O;
               this.O = var7 - var8;
               boolean var10 = false;
               int var9 = var10000;
               if (-Integer.signum(var9) >>> 31 != 0) {
                  break label60;
               }
            }

            this.e(var1);
         }

         if (this.a == LungeSwapModule.h.H) {
            this.J();
         } else if (this.a == LungeSwapModule.h.j) {
            this.M();
         } else if (this.a == LungeSwapModule.h.u && --this.c <= 0) {
            this.j();
            if (this.p.getValue() && var2) {
               this.O = this.C(this.B, this.b);
            }

            this.s = "Ready";
         } else if (this.a == LungeSwapModule.h.n && !bc.a(this.Q.getValue())) {
            this.s = "Set an Activation Key";
         }
      }
   }

   public static String b(Module var0, int var1, Iterable<Module> var2) {
      if (!bc.a(var1)) {
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

            for (q var6 : var4.getSettings()) {
               if (var6 instanceof v var7 && var7.getValue() == var1) {
                  return var4.getName();
               }
            }
         }
      }

      return "";
   }

   private void X() {
      LarpClient var1 = LarpClient.R();
      int var2 = this.Q.getValue();
      String var3 = var1 != null && var1.z() != null ? b(this, var2, var1.z().h()) : "";
      if (!var3.isEmpty() && !var3.equals(this.d)) {
         com.larpclient.h.g(
            "LungeSwap",
            "Activation Key {} is also bound to {}; one press triggers both, and a movement module such as Fly cancels the lunge. Rebind one of them",
            bc.G(var2),
            var3
         );
      }

      this.d = var3;
   }

   private void e() {
      LocalPlayer var1 = mc.player;
      this.M = Math.max(this.M, var1.getDeltaMovement().horizontalDistance());
      if (--this.I <= 0) {
         int var2 = this.M >= 0.4 ? 1 : 0;
         com.larpclient.h.A(
            "LungeSwap",
            "Jab result: peak horizontal speed {} blocks/tick, moved {} blocks in {} ticks ({})",
            String.format(Locale.ROOT, "%.3f", this.M),
            String.format(Locale.ROOT, "%.2f", var1.position().subtract(this.F).horizontalDistance()),
            12,
            var2 != 0 ? "lunge push arrived" : "no lunge push; the server refused it or another movement owner replaced it"
         );
         if (this.v && this.V.getValue() && this.A.O("Detect")) {
            String var3 = this.D();
            Boolean var4 = q.get(var3);
            q.put(var3, var2 == 0);
            if (var2 == 0 && !Boolean.TRUE.equals(var4)) {
               com.larpclient.h.A("LungeSwap", "A swap-tick jab did not lunge on {}; later presses there wait for the spear's own charge", var3);
            } else if (var2 != 0 && var4 == null) {
               com.larpclient.h.A("LungeSwap", "{} keeps the charge across a swap; presses there jab in the swap tick", var3);
            }
         }
      }
   }

   private String D() {
      ServerData var1 = mc.getCurrentServer();
      return var1 != null ? var1.ip : (mc.hasSingleplayerServer() ? "singleplayer" : "this server");
   }

   private void e(boolean var1) {
      String var2 = this.X();
      if (var2 != null) {
         this.s = var2;
         if (var1) {
            com.larpclient.h.A("LungeSwap", "Press refused: {}", var2);
         }
      } else if (this.d() == null) {
         this.s = "No Lunge spear in hotbar";
         if (var1) {
            com.larpclient.h.A("LungeSwap", "Press ignored: no Lunge spear in the hotbar");
         }
      } else {
         this.x = mc.player.getInventory().getSelectedSlot();
         this.T = mc.player.getMainHandItem().getHoverName().getString();
         this.L = mc.player.getAttackStrengthScale(0.0F);
         this.j = P(this.V.getValue(), this.A.getValue(), q.get(this.D()), l6.o());
         this.W = this.N = 0;
         this.K = -1;
         this.f = "";
         this.g = this.C(this.w, this.k);
         this.a = LungeSwapModule.h.H;
      }
   }

   private String X() {
      LocalPlayer var1 = mc.player;
      return lu.L(var1.getFoodData().getFoodLevel(), var1.getAbilities().instabuild, var1.isInWater(), var1.isPassenger(), var1.isFallFlying());
   }

   private lu.m d() {
      LocalPlayer var1 = mc.player;
      ItemStack var2 = var1.getMainHandItem();
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < 9; var4++) {
         int var5 = this.E(var4);
         if (var5 > 0) {
            ItemStack var6 = var1.getInventory().getItem(var4);
            if (var6.isItemEnabled(this.R.enabledFeatures())) {
               var3.add(
                  new lu.g(
                     var4,
                     Z(var1, var6),
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

      return lu.R(
         var3,
         var1.getInventory().getSelectedSlot(),
         this.j,
         ((q56b215c3695b1780)var1).larpAttackStrengthTicker(),
         var1.getCurrentItemAttackStrengthDelay(),
         this.s()
      );
   }

   private static float Z(LocalPlayer var0, ItemStack var1) {
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

   private void J() {
      lu.m var1 = this.d();
      if (var1 == null) {
         this.f();
         this.s = "No Lunge spear in hotbar";
      } else {
         if (this.K < 0) {
            this.K = var1.ticks();
            com.larpclient.h.A(
               "LungeSwap",
               "Press: {} rule, slot {} (Lunge {}) from slot {} ({}, charge {}); {}",
               this.j,
               var1.slot() + 1,
               var1.lunge(),
               this.x + 1,
               this.T,
               M(this.L),
               var1.ticks() == 0 ? (var1.swap() ? "swap and jab now" : "jab now") : "server needs " + var1.ticks() + " more ticks"
            );
         }

         this.P = var1.slot();
         if (this.g > 0) {
            this.g--;
            this.s = "Lunge in " + lu.d(Math.max(this.g + 1, var1.ticks()));
            this.M("Jab Delay");
         } else if (var1.ticks() > 0 && !var1.resets()) {
            this.W++;
            this.s = "Lunge in " + lu.d(var1.ticks());
            this.M("charge");
            if (this.W > this.K + 10 + 2) {
               this.e("the charge never passed");
            }
         } else if (!this.v()) {
            String var2 = this.U();
            if (var2 != null) {
               this.M(var2);
               this.s = "Waiting for " + var2;
               if (++this.N > 10) {
                  this.e(var2);
               }
            } else {
               this.s = "Lunging";
               ta.F(this, this::Z, () -> this.U() == null && this.a == LungeSwapModule.h.H);
            }
         }
      }
   }

   private boolean v() {
      if (!p_.R()) {
         return false;
      }

      p_.g("a Lunge Swap jab");
      this.M("your look");
      this.s = "Lunging";
      return true;
   }

   private String U() {
      if (!this.isRuntimeActive() || !this.c()) {
         return "the game";
      } else if (!hu.k(this)) {
         return "the hotbar";
      } else if (!si.M(mc, mc.player)) {
         return "free hands";
      } else if (this.Z() && si.c(mc) > 0) {
         return "the miss delay";
      } else if (this.X() != null) {
         return "the Lunge conditions";
      } else {
         return this.P >= 0 && this.R(this.P) ? null : "the spear";
      }
   }

   private void M(String var1) {
      if (!var1.equals(this.f)) {
         this.f = var1;
         com.larpclient.h.A("LungeSwap", "Jab held by {}", var1);
      }
   }

   private void e(String var1) {
      com.larpclient.h.A("LungeSwap", "Press lapsed after {} ticks, held by {}", this.W + this.N, var1);
      this.Z(true);
      this.s = "Lunge blocked by " + var1;
   }

   private void Z() {
      lu.m var1 = this.d();
      if (this.a == LungeSwapModule.h.H && var1 != null && (var1.ticks() <= 0 || var1.resets()) && this.U() == null) {
         LocalPlayer var2 = mc.player;
         this.P = var1.slot();
         boolean var3 = var1.swap();
         if (var1.resets()) {
            this.o = lu.b(this.O(this.P), false, this.j, 0, 0.0F, this.s());
            var2.getInventory().setSelectedSlot(this.P);
            this.a = LungeSwapModule.h.j;
            this.n = 0;
            this.s = "Charging spear " + lu.d(this.o);
            com.larpclient.h.A("LungeSwap", "Selected spear slot {}; the server restarts its charge, so the jab waits {} ticks", this.P + 1, this.o);
         } else {
            this.i = (boolean)(var3 && !ItemStack.isSameItem(var2.getMainHandItem(), var2.getInventory().getItem(this.P)) ? 1 : 0);
            var2.getInventory().setSelectedSlot(this.P);
            this.M(var3);
         }
      }
   }

   private lu.g O(int var1) {
      LocalPlayer var2 = mc.player;
      ItemStack var3 = var2.getInventory().getItem(var1);
      return new lu.g(var1, Z(var2, var3), (Float)var3.getOrDefault(DataComponents.MINIMUM_ATTACK_CHARGE, 0.0F), this.E(var1), 0, false, false);
   }

   private void M() {
      LocalPlayer var1 = mc.player;
      this.n++;
      if (var1.getInventory().getSelectedSlot() == this.P && this.R(this.P)) {
         int var2 = this.o - this.n;
         if (var2 > 0) {
            this.s = "Charging spear " + lu.d(var2);
         } else if (!this.v()) {
            String var3 = this.U();
            if (var3 != null) {
               this.M(var3);
               this.s = "Waiting for " + var3;
               if (++this.N > 10) {
                  com.larpclient.h.A("LungeSwap", "Charged jab lapsed, held by {}", var3);
                  ta.s(this);
                  this.j();
                  this.s = "Lunge blocked by " + var3;
               }
            } else {
               ta.F(this, () -> {
                  if (this.a == LungeSwapModule.h.j && this.U() == null) {
                     this.M(true);
                  }
               }, () -> this.a == LungeSwapModule.h.j && this.U() == null);
            }
         }
      } else {
         com.larpclient.h.A("LungeSwap", "Charge interrupted after {} ticks: slot {} selected", this.n, var1.getInventory().getSelectedSlot() + 1);
         ta.s(this);
         this.f();
         this.s = "Swap interrupted";
      }
   }

   private void M(boolean var1) {
      LocalPlayer var2 = mc.player;
      ItemStack var3 = var2.getInventory().getItem(this.P);
      int var4 = var1 && this.a == LungeSwapModule.h.H ? 1 : 0;
      if (var3.isItemEnabled(this.R.enabledFeatures())) {
         mc.gameMode.piercingAttack((PiercingWeapon)var3.get(DataComponents.PIERCING_WEAPON));
         var2.swing(InteractionHand.MAIN_HAND);
         this.s = "Lunged";
         this.I = 12;
         this.M = 0.0;
         this.F = var2.position();
         this.v = (boolean)(var4 != 0 && this.i ? 1 : 0);
      } else {
         this.s = "Spear not enabled here";
      }

      int var5 = this.W + this.N + this.n;
      this.a = LungeSwapModule.h.u;
      this.c = this.C(this.z, this.h);
      com.larpclient.h.A(
         "LungeSwap",
         "{}: {} {} rule, slot {} ({}, charge {}) to spear slot {} (Lunge {}) {} ticks after the press{}; food {}, ground {}, sprint {}",
         this.s,
         this.u.getValue(),
         this.j,
         this.x + 1,
         this.T,
         M(this.L),
         this.P + 1,
         this.E(this.P),
         var5,
         var4 != 0 ? ", swapped in the jab tick" : (var1 ? ", spear charged after the swap" : ", spear already held"),
         var2.getFoodData().getFoodLevel(),
         var2.onGround(),
         var2.isSprinting()
      );
      if (this.c > 1) {
         com.larpclient.h.A("LungeSwap", "Swap back in {} ticks", this.c);
      }
   }

   private void j() {
      LocalPlayer var1 = getMc() == null ? null : mc.player;
      if (var1 != null && var1 == this.G && var1.level() == this.R) {
         int var2 = B(this.E(), this.m.getValue(), this.J.getValue().intValue() - 1, this.x, var1.getInventory().getSelectedSlot(), this.P);
         if (var2 >= 0) {
            var1.getInventory().setSelectedSlot(var2);
         }
      }

      this.f();
   }

   private void Z(boolean var1) {
      ta.s(this);
      if (this.a != LungeSwapModule.h.n) {
         com.larpclient.h.A("LungeSwap", "Cancelled at {}; restore={}, status {}", this.a, var1, this.s);
      }

      if (!var1 || this.a != LungeSwapModule.h.u && this.a != LungeSwapModule.h.j) {
         this.f();
      } else {
         this.j();
      }
   }

   private void f() {
      this.a = LungeSwapModule.h.n;
      this.i = (boolean)0;
      this.x = this.P = -1;
      this.g = this.c = 0;
      this.W = this.N = this.n = this.o = 0;
      this.K = -1;
      this.Z = (boolean)0;
      this.f = "";
   }

   private boolean R(int var1) {
      return this.E(var1) > 0;
   }

   private int E(int var1) {
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

   private static String M(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   public enum h {
      n,
      H,
      j,
      u;
   }
}

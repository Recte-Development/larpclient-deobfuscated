package com.larpclient.module;

import com.larp.obf.qa300483f54d804a3.q1881b7c262e73011;
import com.larpclient.hu;
import com.larpclient.j6;
import com.larpclient.ks;
import com.larpclient.l6;
import com.larpclient.lm;
import com.larpclient.mk;
import com.larpclient.p_;
import com.larpclient.si;
import com.larpclient.ta;
import com.larpclient.u6;
import com.larpclient.uo;
import com.larpclient.wi;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public final class MaceModule extends Module {
   private static MaceModule i;
   private final s c = this.addSetting(A("auto-swap", "Auto Swap", "Select a mace immediately before a smash hit", true));
   private final l C = this.addSetting(K("pick", "Pick", "Prefer damage or a Wind Burst mace for smash hits", "Damage", "Damage", "Wind Burst"));
   private final s d = this.addSetting(
      A(
         "breach-swap",
         "Breach Swap",
         "Use a Breach mace for ordinary hits when expected damage improves by 15%. Skipped on Paper servers, which reset the charge on a swap",
         false
      )
   );
   private final s B = this.addSetting(A("swap-back", "Swap Back", "Return to the previous hotbar slot after the attack", true));
   private final s f = this.addSetting(A("smash-assist", "Smash Assist", "Attack the enemy under the crosshair during a qualifying fall", false));
   private final l v = this.addSetting(
      K("smash-timing", "Smash Timing", "Delay while the next movement step can still reach the target", "Delayed", "Direct", "Delayed")
   );
   private final e G = this.addSetting(new e("min-fall", "Minimum fall distance for automatic smash hits", 3.0, 1.5, 30.0, 0.5) {
      @Override
      public String getDisplayName() {
         return "Min Fall";
      }
   });
   private final s E = this.addSetting(A("mobs", "Mobs", "Allow smash assistance against non-player enemies", false));
   private final s x = this.addSetting(A("silent-aim", "Silent Aim", "Turn toward a nearby enemy and smash it during a fall while your view stays free", false));
   private final e V = this.addSetting(l("aim-range", "Aim Range", "Distance at which Silent Aim starts turning toward an enemy", 6.0, 3.0, 10.0, 0.5));
   private final e n = this.addSetting(l("turn-speed", "Turn Speed", "Maximum Silent Aim turn in degrees per tick", 60.0, 10.0, 180.0, 5.0));
   private final l a = this.addSetting(
      K(
         "rotation",
         "Rotation",
         "Kill Aura rotation engine that turns the player toward the target; Legit moves most like a hand",
         "Sloth",
         "Sloth",
         "FunTime",
         "Polar",
         "Matrix",
         "Legit"
      )
   );
   private final s O = this.addSetting(
      A("lead-turn", "Lead Turn", "In a fast fall, start turning early enough that the turn finishes before the target is in reach", true)
   );
   private final l u = this.addSetting(
      K(
         "stun-slam",
         "Stun Slam",
         "When a smash target raises a shield, hit it with your best axe to disable the shield, then smash. Same Tick sends both hits at once; Next Tick smashes a tick later, which Grim's experimental packet order check does not flag",
         "Same Tick",
         "Same Tick",
         "Next Tick",
         "Off"
      )
   );
   private final l U = this.addSetting(
      K(
         "range-ring",
         "Range Ring",
         "Draw Silent Aim's reach on the ground below you: an enemy standing inside the ring is in range. In Air shows it only while you are off the ground",
         "In Air",
         "In Air",
         "Always",
         "Off"
      )
   );
   private final s b = this.addSetting(
      A(
         "target-mark",
         "Target Mark",
         "Outline the enemy Silent Aim is turning toward and mark where your real facing points; green once that facing is on the enemy",
         true
      )
   );
   private final e Q = this.addSetting(
      l(
         "return-speed",
         "Return Speed",
         "Most the player turns per tick back to your look after Silent Aim lets go, like a hand. 180 returns at once",
         35.0,
         10.0,
         180.0,
         5.0
      )
   );
   private final lm N = new lm();
   private final hu j = new hu(this);
   private final t M = new t();
   private final a R = new a();
   private final tf F = new tf();
   private final j o = new j();
   private final b m = new b();
   private x S;
   private boolean P;
   private boolean g;
   private LocalPlayer q;
   private Object y;
   private int K = Integer.MIN_VALUE;
   private LivingEntity z;
   private String A = "Waiting";
   private LivingEntity X;
   private int r;
   private int W;
   private int h;
   private double J;
   private boolean I;
   private LivingEntity l;
   private int L = Integer.MIN_VALUE;
   private boolean Y;
   private String Z = "";
   private String H = "";

   public MaceModule() {
      super("Mace", "Select a mace for smash hits, with optional smash assistance and Silent Aim", Category.COMBAT, -1);
      i = this;
      this.C.P(this.c::getValue);
      this.d.P(this.c::getValue);
      this.B.P(() -> this.c.getValue() || this.a());
      this.v.P(this::a);
      this.G.P(this::a);
      this.E.P(this::a);
      this.V.P(this.x::getValue);
      this.O.P(this.x::getValue);
      this.n.P(this.x::getValue);
      this.a.P(this.x::getValue);
      this.U.P(this.x::getValue);
      this.b.P(this.x::getValue);
      this.Q.P(this.x::getValue);
      this.x.o(var1 -> this.c());
   }

   public static MaceModule W() {
      return i;
   }

   public lm S() {
      return this.N;
   }

   public double R() {
      return this.Q.getValue();
   }

   @Override
   public String getDisplayName() {
      return "Auto Mace";
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   private boolean a() {
      return this.f.getValue() || this.x.getValue();
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.K);
   }

   @Override
   public String getStatus() {
      return this.A;
   }

   @Override
   public void onEnable() {
      this.R();
      com.larpclient.h.A(
         "Mace",
         "Active; autoSwap={}, smashAssist={}, silentAim={}, timing={}, minFall={}, stunSlam={}, leadTurn={}, rangeRing={}, targetMark={}, returnSpeed={}",
         this.c.getValue(),
         this.f.getValue(),
         this.x.getValue(),
         this.v.getValue(),
         this.G.getValue(),
         this.u.getValue(),
         this.O.getValue(),
         this.U.getValue(),
         this.b.getValue(),
         this.Q.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.R();
      this.c();
      this.j.V(getMc(), this.B.getValue());
      com.larpclient.h.U("Mace", "Disabled or context suspended; hotbar, view and sprint ownership released");
   }

   public static void M() {
      if (i != null && i.isEnabled()) {
         MaceModule var0 = i;
         if (var0 != null && var0.isRuntimeActive()) {
            Minecraft var1 = getMc();
            var0.q = null;
            var0.y = null;
            var0.K = Integer.MIN_VALUE;
            if (var1.player != null && var1.player.onGround()) {
               var0.i();
            }

            if (var0.l == null) {
               var0.j.J(var1, var0.B.getValue());
            }
         }
      }
   }

   private void i() {
      if (this.I) {
         com.larpclient.h.A("Mace", "Landed before the server confirmed the smash on {}", r(this.X));
      }

      this.P = this.I = (boolean)0;
      this.X = null;
      this.h = 0;
      this.l = null;
      this.Z = "";
   }

   public void Z(LocalPlayer var1, Entity var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var1 == getMc().player && var2 instanceof LivingEntity var3) {
            boolean var4 = MaceItem.canSmashAttack(var1);
            if (this.M(var1, var2)) {
               int var7 = si.E(var1);
               if (this.j.O(var1, var7)) {
                  si.V(var3);
                  this.l = var3;
                  this.L = var1.tickCount;
                  this.Y = this.u.O("Same Tick");
                  this.A = "Stun Slam";
                  com.larpclient.h.A(
                     "Mace", "Stun Slam on {}: axe slot {} for your hit, smash {}", r(var3), var7 + 1, this.Y ? "in the same tick" : "next tick"
                  );
               }
            } else if (this.c.getValue() && (!si.k(var3, var1) || si.x(var3))) {
               if (var4 || this.d.getValue() && !this.u("Breach Swap")) {
                  int var5 = this.K(var1, var3, var4);
                  if (var5 >= 0 && uo.i(var4, n(var1, var1.getInventory().getItem(var5), var3), n(var1, var1.getMainHandItem(), var3))) {
                     int var6 = var1.getInventory().getSelectedSlot();
                     if (this.j.O(var1, var5) && var6 != var5) {
                        com.larpclient.h.U("Mace", "Selected hotbar slot {} for {} hit", var5 + 1, var4 ? "smash" : "Breach");
                     }

                     if (var4) {
                        this.j(var1, var3);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean u(String var1) {
      if (!l6.o()) {
         return false;
      }

      if (!var1.equals(this.H)) {
         com.larpclient.h.A("Mace", "{} skipped: {} resets the charge on a swap", var1, l6.J());
      }

      this.H = var1;
      return true;
   }

   public boolean M(LocalPlayer var1, Entity var2) {
      if (!this.isEnabled()) {
         return false;
      } else if (this.isRuntimeActive()
         && !this.u.O("Off")
         && var1 == getMc().player
         && var2 instanceof Player var3
         && !ta.U()
         && MaceItem.canSmashAttack(var1)) {
         uo.l var4 = uo.v(this.u.getValue(), si.k(var3, var1), si.x(var3), si.E(var1) >= 0, this.s(var1, var3) >= 0);
         return var4 == uo.l.a || var4 == uo.l.e;
      } else {
         return false;
      }
   }

   public void L(LocalPlayer var1, Entity var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var2 == this.l && this.Y && this.L == var1.tickCount) {
            LivingEntity var3 = this.l;
            this.l = null;
            this.m(getMc(), var1, var3, "Stun Slam smash after your axe hit");
         }
      }
   }

   private boolean m(Minecraft var1, LocalPlayer var2, LivingEntity var3, String var4) {
      int var5 = this.s(var2, var3);
      if (var5 >= 0 && this.j.O(var2, var5)) {
         boolean var6 = ta.j(this, var3, () -> {
            var1.gameMode.attack(var2, var3);
            ks.X(var2, InteractionHand.MAIN_HAND);
         });
         if (var6) {
            this.j(var2, var3);
            com.larpclient.h.A("Mace", "{}: fall {}, mace slot {}", var4, w(var2.fallDistance), var5 + 1);
         }

         return var6;
      } else {
         com.larpclient.h.A("Mace", "{} skipped: no usable mace", var4);
         return false;
      }
   }

   private int s(LocalPlayer var1, LivingEntity var2) {
      if (var1.getMainHandItem().is(Items.MACE) && !var1.getMainHandItem().nextDamageWillBreak()) {
         return var1.getInventory().getSelectedSlot();
      } else {
         return this.c.getValue() ? this.K(var1, var2, true) : -1;
      }
   }

   private int K(LocalPlayer var1, LivingEntity var2, boolean var3) {
      uo.w[] var4 = new uo.w[Inventory.getSelectionSize()];

      for (int var5 = 0; var5 < var4.length; var5++) {
         ItemStack var6 = var1.getInventory().getItem(var5);
         if (var6.is(Items.MACE) && !var6.nextDamageWillBreak()) {
            int var7 = d(var6, Enchantments.BREACH);
            if (var3 || var7 != 0) {
               var4[var5] = new uo.w(var5, true, false, var7, d(var6, Enchantments.WIND_BURST), n(var1, var6, var2));
            }
         }
      }

      return uo.N(var4, var3, this.C.O("Wind Burst"));
   }

   private static float n(LocalPlayer var0, ItemStack var1, LivingEntity var2) {
      return uo.P(
         si.q(var0.getMainHandItem()),
         si.i(var0),
         var0.fallDistance,
         var1.is(Items.MACE),
         MaceItem.canSmashAttack(var0),
         d(var1, Enchantments.DENSITY),
         si.g(var0, var2),
         d(var1, Enchantments.SHARPNESS),
         var2.getArmorValue(),
         (float)var2.getAttributeValue(Attributes.ARMOR_TOUGHNESS),
         d(var1, Enchantments.BREACH)
      );
   }

   private static int d(ItemStack var0, ResourceKey<Enchantment> var1) {
      for (Entry var3 : var0.getEnchantments().entrySet()) {
         if (((Holder)var3.getKey()).is(var1)) {
            return var3.getIntValue();
         }
      }

      return 0;
   }

   @Override
   public void onTick() {
      boolean var1 = this.g;
      this.g = (boolean)0;
      Minecraft var2 = getMc();
      if (var2.player != null) {
         this.e(var2.player);
      }

      boolean var10 = false;
      byte var9 = this.P;
      int var10001 = Integer.signum(var9);
      this.A = ((var10001 | -var10001) >>> 31 ^ 1) == 0 ? (this.I ? "Waiting for the server" : "Smash used this fall") : "Waiting";
      if (this.l != null && !this.Y) {
         this.v(var2);
      } else {
         label120: {
            if (this.Y && this.l != null && var2.player != null) {
               int var14 = var2.player.tickCount;
               int var13 = this.L;
               var10001 = Integer.compare(var13, var14);
               if ((var10001 | -var10001) >>> 31 == 0) {
                  var19 = this.l;
                  break label120;
               }
            }

            var19 = null;
         }

         this.l = var19;
         if (!this.a()) {
            this.c();
         } else {
            LivingEntity var3 = this.x.getValue() ? this.y(var2) : null;
            if (var3 == null) {
               this.c();
            }

            if (si.w(var2, p_.o.K) && hu.k(this)) {
               LocalPlayer var4 = var2.player;
               EntityHitResult var5 = si.d(var2);
               LivingEntity var6 = this.P(var4, var5, var3);
               int var7 = var6 == null && var3 == null ? 0 : 1;
               int var10000 = si.o(var4);
               boolean var20 = si.B(var2);
               boolean var10002 = si.A(var2);
               int var10003 = var3 == null && j6.j() ? 1 : 0;
               boolean var10004 = si.M(var2, var4);
               int var10005 = var4.getDeltaMovement().y < 0.0 ? 1 : 0;
               boolean var10006 = MaceItem.canSmashAttack(var4);
               double var10007 = var4.fallDistance;
               double var10008 = this.G.getValue();
               boolean var16 = false;
               byte var15 = this.P;
               int var10009 = Integer.signum(var15);
               String var8 = uo.e(
                  (boolean)var10000,
                  var20,
                  var10002,
                  (boolean)var10003,
                  var10004,
                  (boolean)var10005,
                  var10006,
                  var10007,
                  var10008,
                  ((var10009 | -var10009) >>> 31 ^ 1) == 0 && !this.I,
                  this.I,
                  var6 != null && this.s(var4, var6) >= 0,
                  var6 != null
               );
               if (var8 == null) {
                  this.n(var2, var4, var5, var6, var3, var1);
               } else if (var7 != 0) {
                  this.r(var8, var4, var3);
               }

               label83:
               if (var3 != null) {
                  if (this.P) {
                     boolean var12 = false;
                     byte var11 = this.I;
                     var10000 = Integer.signum(var11);
                     if ((var10000 | -var10000) >>> 31 == 0) {
                        break label83;
                     }
                  }

                  if (!si.o(var4)) {
                     this.b(var2, var3);
                  }

                  return;
               }

               this.c();
            } else {
               this.c();
            }
         }
      }
   }

   private LivingEntity P(LocalPlayer var1, EntityHitResult var2, LivingEntity var3) {
      if (!(var2 != null && var2.getEntity() instanceof LivingEntity var4)) {
         return null;
      } else if (!this.f.getValue() && var4 != var3) {
         return null;
      } else {
         return si.x(var1, var4) && (var4 instanceof Player || this.E.getValue()) ? var4 : null;
      }
   }

   private void r(String var1, LocalPlayer var2, LivingEntity var3) {
      if (!var1.equals(this.Z)) {
         this.Z = var1;
         com.larpclient.h.A(
            "Mace",
            "Smash held: {}; fall {}, speed {}{}",
            var1,
            w(var2.fallDistance),
            w(var2.getDeltaMovement().y),
            var3 == null ? "" : ", Silent Aim target " + r(var3) + " at " + w(wi.j(var2.getEyePosition(), var3.getBoundingBox()))
         );
      }
   }

   private void n(Minecraft var1, LocalPlayer var2, EntityHitResult var3, LivingEntity var4, LivingEntity var5, boolean var6) {
      this.Z = "";
      uo.l var7 = uo.v(this.u.getValue(), si.k(var4, var2), si.x(var4), si.E(var2) >= 0, this.s(var2, var4) >= 0);
      if ((var7 != uo.l.e || !M(var2, var4)) && var7 != uo.l.j) {
         if (var7 == uo.l.A && si.k(var4, var2) && !si.x(var4)) {
            this.r("raised shield and no axe in the hotbar", var2, var5);
         } else if (this.X(var2, var4)) {
            this.g = (boolean)1;
            this.q = var2;
            this.y = var1.level;
            this.K = var2.tickCount;
            var2.setSprinting(false);
            si.b(var1, Math.max(si.c(var1), 1));
            this.A = "Delaying smash";
            if (!var6) {
               com.larpclient.h.U("Mace", "Delaying smash while the predicted next step remains in reach");
            }
         } else if (var7 != uo.l.A) {
            this.m(var1, var2, var4, var7);
         } else {
            int var8 = this.s(var2, var4);
            if (var8 == var2.getInventory().getSelectedSlot() || this.j.O(var2, var8)) {
               if (si.k(var1, var3)) {
                  this.j(var2, var4);
                  si.G(var1);
                  com.larpclient.h.A(
                     "Mace", "Smash sent on {}: fall {}, speed {}, silent={}", r(var4), w(var2.fallDistance), w(var2.getDeltaMovement().y), var4 == var5
                  );
               } else {
                  com.larpclient.h.A("Mace", "Smash attack refused by the client gate on {}", r(var4));
               }
            }
         }
      } else {
         this.m(var1, var2, var4, var7);
      }
   }

   private void m(Minecraft var1, LocalPlayer var2, LivingEntity var3, uo.l var4) {
      int var5 = si.E(var2);
      int var6 = this.s(var2, var3);
      if (this.j.O(var2, var5)) {
         int var7 = var4 == uo.l.a ? 1 : 0;
         boolean var8 = ta.B(this, var3, () -> {
            var1.gameMode.attack(var2, var3);
            ks.X(var2, InteractionHand.MAIN_HAND);
            si.V(var3);
            if (var7 && this.j.O(var2, var6)) {
               var1.gameMode.attack(var2, var3);
               ks.X(var2, InteractionHand.MAIN_HAND);
            }
         });
         if (!var8) {
            this.j.V(var1, true);
            com.larpclient.h.A("Mace", "Stun Slam refused: this tick's attack belongs to another owner");
         } else {
            si.G(var1);
            if (var7 != 0) {
               this.j(var2, var3);
               this.A = "Stun Slam";
               com.larpclient.h.A(
                  "Mace", "Stun Slam on {}: axe slot {} then mace slot {} in one tick, fall {}", r(var3), var5 + 1, var6 + 1, w(var2.fallDistance)
               );
            } else if (var4 == uo.l.e) {
               this.l = var3;
               this.L = var2.tickCount;
               this.Y = (boolean)0;
               this.A = "Stun Slam";
               com.larpclient.h.A("Mace", "Stun Slam on {}: axe slot {} now, smash next tick", r(var3), var5 + 1);
            } else {
               this.A = "Shield break hit";
               com.larpclient.h.A("Mace", "Shield break hit on {} with axe slot {}; no mace for the smash", r(var3), var5 + 1);
            }
         }
      }
   }

   private void v(Minecraft var1) {
      LocalPlayer var2 = var1.player;
      LivingEntity var3 = this.l;
      if (var2 != null && var2.tickCount == this.L + 1) {
         this.l = null;
         EntityHitResult var4 = si.d(var1);
         if (var4 != null && var4.getEntity() == var3 && MaceItem.canSmashAttack(var2) && !var2.onGround()) {
            int var5 = this.s(var2, var3);
            if (var5 >= 0 && this.j.O(var2, var5)) {
               boolean var6 = ta.B(this, var3, () -> {
                  var1.gameMode.attack(var2, var3);
                  ks.X(var2, InteractionHand.MAIN_HAND);
               });
               if (var6) {
                  this.j(var2, var3);
                  si.G(var1);
                  com.larpclient.h.A("Mace", "Stun Slam smash on {} one tick after the axe: fall {}", r(var3), w(var2.fallDistance));
               } else {
                  com.larpclient.h.A("Mace", "Next Tick smash on {} refused: this tick's attack belongs to another owner", r(var3));
               }
            } else {
               this.j.V(var1, this.B.getValue());
            }
         } else {
            com.larpclient.h.A(
               "Mace",
               "Next Tick smash on {} skipped: {}",
               r(var3),
               var2.onGround() ? "landed" : (!MaceItem.canSmashAttack(var2) ? "fall ended" : "facing left the target")
            );
            this.j.V(var1, this.B.getValue());
         }
      } else {
         if (var2 == null || var2.tickCount > this.L + 1) {
            this.l = null;
            this.j.V(var1, this.B.getValue());
         }
      }
   }

   private void j(LocalPlayer var1, LivingEntity var2) {
      this.P = this.I = (boolean)1;
      this.X = var2;
      this.r = var1.tickCount;
      this.W = var2.hurtTime;
      this.J = var1.getDeltaMovement().y;
      this.A = "Waiting for the server";
   }

   private void e(LocalPlayer var1) {
      if (this.I && this.X != null) {
         if (uo.p(this.J, var1.getDeltaMovement().y, this.W, this.X.hurtTime)) {
            this.I = (boolean)0;
            com.larpclient.h.A("Mace", "Smash confirmed on {} after {} ticks", r(this.X), var1.tickCount - this.r);
         } else if (var1.tickCount - this.r >= uo.u(this.h(var1))) {
            this.I = (boolean)0;
            if (this.h < 1 && !var1.onGround() && MaceItem.canSmashAttack(var1)) {
               this.h++;
               this.P = (boolean)0;
               com.larpclient.h.A(
                  "Mace", "No server confirmation for the smash on {} after {} ticks; retrying once this fall", r(this.X), var1.tickCount - this.r
               );
            } else {
               com.larpclient.h.A(
                  "Mace", "No server confirmation for the smash on {}; not retrying ({})", r(this.X), this.h >= 1 ? "already retried" : "fall ended"
               );
            }
         }
      }
   }

   private int h(LocalPlayer var1) {
      ClientPacketListener var2 = getMc().getConnection();
      PlayerInfo var3 = var2 == null ? null : var2.getPlayerInfo(var1.getUUID());
      return var3 == null ? 0 : Math.round(var3.getLatency() / 50.0F);
   }

   private LivingEntity y(Minecraft var1) {
      LocalPlayer var2 = var1.player;
      if (var2 != null && var1.level != null && (!this.P || this.I) && !var2.onGround() && !(var2.getDeltaMovement().y >= 0.0) && p_.I(p_.o.K)) {
         if (this.z != null && this.s(var2, this.z, this.N(var2, this.z))) {
            return this.z;
         }

         Vec3 var3 = var2.getEyePosition();
         double var4 = 16.0;
         LivingEntity var6 = null;
         double var7 = Double.MAX_VALUE;

         for (Entity var10 : var1.level
            .getEntities(var2, var2.getBoundingBox().inflate(Math.max(this.V.getValue(), var4)), var0 -> var0 instanceof LivingEntity)) {
            LivingEntity var11 = (LivingEntity)var10;
            double var12 = wi.j(var3, var11.getBoundingBox());
            if (var12 < var7 && this.s(var2, var11, this.N(var2, var11))) {
               var6 = var11;
               var7 = var12;
            }
         }

         return var6;
      } else {
         return null;
      }
   }

   private double N(LocalPlayer var1, LivingEntity var2) {
      u var3 = new u(var1.getYRot(), var1.getXRot());
      int var4 = this.y().ticksToReach(var3, tv.C(M(var1, var2)));
      return uo.A(this.V.getValue(), this.O.getValue(), -var1.getDeltaMovement().y, var4, var1.entityInteractionRange());
   }

   public double O(LocalPlayer var1) {
      if (!this.isEnabled()) {
         return 0.0;
      } else if (this.isRuntimeActive() && this.x.getValue() && var1 != null && !this.U.O("Off") && (!this.U.O("In Air") || !var1.onGround())) {
         u var2 = new u(var1.getYRot(), var1.getXRot());
         int var3 = this.y().ticksToReach(var2, new u(var1.getYRot(), 90.0F));
         return uo.A(this.V.getValue(), this.O.getValue(), -var1.getDeltaMovement().y, var3, var1.entityInteractionRange());
      } else {
         return 0.0;
      }
   }

   public LivingEntity V() {
      if (!this.isEnabled()) {
         return null;
      } else {
         return this.b.getValue() && this.B() ? this.z : null;
      }
   }

   private boolean s(LocalPlayer var1, LivingEntity var2, double var3) {
      Vec3 var5 = var1.getEyePosition();
      return si.x(var1, var2)
         && (var2 instanceof Player || this.E.getValue())
         && wi.j(var5, var2.getBoundingBox()) <= var3
         && this.W(var1, var2)
         && si.F(var1, var5, M(var1, var2));
   }

   private static Vec3 M(LocalPlayer var0, LivingEntity var1) {
      AABB var2 = var1.getBoundingBox();
      Vec3 var3 = var0.getEyePosition();
      AABB var4 = var2.deflate(Math.min(0.1, var2.getXsize() * 0.25), Math.min(0.1, var2.getYsize() * 0.25), Math.min(0.1, var2.getZsize() * 0.25));
      Vec3 var5 = new Vec3(Math.clamp(var3.x, var4.minX, var4.maxX), Math.clamp(var3.y, var4.minY, var4.maxY), Math.clamp(var3.z, var4.minZ, var4.maxZ));
      Vec3 var6 = new Vec3(var2.getCenter().x, var2.minY + var2.getYsize() * 0.6, var2.getCenter().z);
      return ta.m(var1, var0.entityInteractionRange(), var6, mk.B(var2, var5)).orElse(var6);
   }

   private x y() {
      return switch ((String)this.a.getValue()) {
         case "FunTime" -> this.R;
         case "Polar" -> this.F;
         case "Matrix" -> this.o;
         case "Legit" -> this.m;
         default -> this.M;
      };
   }

   private void b(Minecraft var1, LivingEntity var2) {
      LocalPlayer var3 = var1.player;
      if (!ta.E(this)) {
         this.c();
      } else {
         x var4 = this.y();
         if (var4 != this.S) {
            if (this.S != null) {
               this.S.reset();
            }

            this.S = var4;
         }

         AABB var5 = var2.getBoundingBox();
         if (var2 != this.z) {
            this.S.targetChanged(var2);
            com.larpclient.h.A(
               "Mace",
               "Silent Aim target {} at {} blocks, falling {} blocks/tick, rotation={}",
               r(var2),
               w(wi.j(var3.getEyePosition(), var5)),
               w(-var3.getDeltaMovement().y),
               this.a.getValue()
            );
         }

         this.z = var2;
         p_.p(p_.o.K);
         if (p_.A() != p_.o.K) {
            this.z = null;
         } else {
            u var6 = new u(var3.getYRot(), var3.getXRot());
            double var7 = var3.entityInteractionRange();
            int var9 = wi.j(var3.getEyePosition(), var5) <= var7 ? 1 : 0;
            q1881b7c262e73011 var10 = (q1881b7c262e73011)var3;
            boolean var11 = com.larpclient.module.y.t(var3, var5.inflate(var2.getPickRadius()), var10.larpYRotLast(), var10.larpXRotLast(), var7 + 1.0, false);
            u var12 = this.S.rotate(var6, tv.C(M(var3, var2)), var2, (boolean)var9, var11);
            float[] var13 = u6.A(var6.R, var6.x, var12.R, var12.x, this.n.getValue());
            var12 = new u(var13[0], var13[1]);
            var12 = this.S == this.m ? this.m.u(var6, var12) : tv.o(var6, var12);
            j6.U(this, j6.l.D, var12.R, var12.x, 1.0);
            if (!j6.g(j6.l.D)) {
               com.larpclient.h.U("Mace", "Silent Aim yielded to a higher rotation owner");
               this.c();
            } else {
               if (!this.g) {
                  this.A = "Silent aim";
               }
            }
         }
      }
   }

   private void c() {
      if (this.z != null && this.S != null) {
         this.S.reset();
      }

      this.z = null;
      p_.t(p_.o.K, this.N, this.Q.getValue(), true);
   }

   public float A(float var1, float var2) {
      if (this.isEnabled()) {
         return this.B() ? AuraModule.O(var1, var2, 35.0F, 0.25F) : var1;
      } else {
         return var1;
      }
   }

   public boolean B() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.x.getValue() && this.z != null && this.z.isAlive() && !this.z.isRemoved() && ta.e(this);
   }

   public void V() {
      if (this.isEnabled()) {
         this.P = (boolean)1;
         this.A = "Smash used this fall";
      }
   }

   public boolean W(LocalPlayer var1, LivingEntity var2) {
      if (!this.isEnabled()) {
         return false;
      }

      boolean var3 = var1.getMainHandItem().is(Items.MACE);
      int var4 = !var3
            && this.c.getValue()
            && this.isRuntimeActive()
            && !this.P
            && MaceItem.canSmashAttack(var1)
            && var1.fallDistance >= this.G.getValue()
            && this.K(var1, var2, true) >= 0
         ? 1
         : 0;
      return uo.w(this.isRuntimeActive(), this.P, MaceItem.canSmashAttack(var1), var1.fallDistance, this.G.getValue(), var3, this.c.getValue(), (boolean)var4);
   }

   public boolean X(LocalPlayer var1, LivingEntity var2) {
      return this.isEnabled()
         ? uo.S(M(var1, var2), this.v.O("Delayed"), si.i(var1), var1.getAttackStrengthScale(1.5F), var2.hurtTime, var1.isSprinting())
         : false;
   }

   private static boolean M(LocalPlayer var0, LivingEntity var1) {
      Vec3 var2 = var0.getDeltaMovement();
      return uo.T(
         var0.level().noCollision(var0, var0.getBoundingBox().move(var2)),
         var0.entityInteractionRange(),
         var0.getEyePosition(),
         var2,
         var1.getBoundingBox(),
         var1.position().subtract(var1.xo, var1.yo, var1.zo),
         var1.getPickRadius(),
         var0.getViewVector(1.0F),
         (var1x, var2x) -> si.F(var0, var1x, var2x)
      );
   }

   public static boolean d() {
      if (i != null && i.isEnabled()) {
         MaceModule var0 = i;
         if (var0 != null && var0.isRuntimeActive() && var0.a()) {
            Minecraft var1 = getMc();
            if (var1.player == null) {
               return false;
            } else {
               return p_.A() == p_.o.K
                  ? true
                  : var1.player == var0.q
                     && var1.level == var0.y
                     && var1.gui.screen() == null
                     && var1.player.tickCount >= var0.K
                     && (long)var1.player.tickCount - var0.K <= 1L;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static Input e(LocalPlayer var0, Input var1) {
      if (i != null && i.isEnabled()) {
         MaceModule var2 = i;
         if (var2 != null && var2.isRuntimeActive() && var0 != null && var0 == getMc().player && p_.A() == p_.o.K) {
            if (var0.isSprinting()) {
               var0.setSprinting(false);
               com.larpclient.h.U("Mace", "Silent Aim lock released sprint");
            }

            return si.X(var1);
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void R() {
      this.P = this.I = (boolean)0;
      this.g = (boolean)0;
      this.X = null;
      this.h = 0;
      this.l = null;
      this.Z = "";
      this.q = null;
      this.y = null;
      this.K = Integer.MIN_VALUE;
      this.A = "Waiting";
   }

   private static String r(Entity var0) {
      return var0 == null ? "?" : var0.getName().getString();
   }

   private static String w(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static s A(String var0, final String var1, String var2, boolean var3) {
      return new s(var0, var2, var3) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }

   private static e l(String var0, final String var1, String var2, double var3, double var5, double var7, double var9) {
      return new e(var0, var2, var3, var5, var7, var9) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }

   private static l K(String var0, final String var1, String var2, String var3, String... var4) {
      return new l(var0, var2, var3, var4) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }
}

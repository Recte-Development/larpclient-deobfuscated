package com.larpclient.module;

import com.larp.obf.q41a5851bcf8e876a.q71cffb54eb30bf94;
import com.larpclient._b;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.b6;
import com.larpclient.h8;
import com.larpclient.ij;
import com.larpclient.ip;
import com.larpclient.jz;
import com.larpclient.nx;
import com.larpclient.ow;
import com.larpclient.qg;
import com.larpclient.s6;
import com.larpclient.vm;
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
   private static MaceModule J;
   private final m n = this.addSetting(X("auto-swap", "Auto Swap", "Select a mace immediately before a smash hit", true));
   private final o x = this.addSetting(a("pick", "Pick", "Prefer damage or a Wind Burst mace for smash hits", "Damage", "Damage", "Wind Burst"));
   private final m z = this.addSetting(
      X(
         "breach-swap",
         "Breach Swap",
         "Use a Breach mace for ordinary hits when expected damage improves by 15%. Skipped on Paper servers, which reset the charge on a swap",
         false
      )
   );
   private final m p = this.addSetting(X("swap-back", "Swap Back", "Return to the previous hotbar slot after the attack", true));
   private final m h = this.addSetting(X("smash-assist", "Smash Assist", "Attack the enemy under the crosshair during a qualifying fall", false));
   private final o Y = this.addSetting(
      a("smash-timing", "Smash Timing", "Delay while the next movement step can still reach the target", "Delayed", "Direct", "Delayed")
   );
   private final b G = this.addSetting(new b("min-fall", "Minimum fall distance for automatic smash hits", 3.0, 1.5, 30.0, 0.5) {
      @Override
      public String getDisplayName() {
         return "Min Fall";
      }
   });
   private final m f = this.addSetting(X("mobs", "Mobs", "Allow smash assistance against non-player enemies", false));
   private final m j = this.addSetting(X("silent-aim", "Silent Aim", "Turn toward a nearby enemy and smash it during a fall while your view stays free", false));
   private final b w = this.addSetting(j("aim-range", "Aim Range", "Distance at which Silent Aim starts turning toward an enemy", 6.0, 3.0, 10.0, 0.5));
   private final b R = this.addSetting(j("turn-speed", "Turn Speed", "Maximum Silent Aim turn in degrees per tick", 60.0, 10.0, 180.0, 5.0));
   private final o l = this.addSetting(
      a(
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
   private final m F = this.addSetting(
      X("lead-turn", "Lead Turn", "In a fast fall, start turning early enough that the turn finishes before the target is in reach", true)
   );
   private final o k = this.addSetting(
      a(
         "stun-slam",
         "Stun Slam",
         "When a smash target raises a shield, hit it with your best axe to disable the shield, then smash. Same Tick sends both hits at once; Next Tick smashes a tick later, which Grim's experimental packet order check does not flag",
         "Same Tick",
         "Same Tick",
         "Next Tick",
         "Off"
      )
   );
   private final o B = this.addSetting(
      a(
         "range-ring",
         "Range Ring",
         "Draw Silent Aim's reach on the ground below you: an enemy standing inside the ring is in range. In Air shows it only while you are off the ground",
         "In Air",
         "In Air",
         "Always",
         "Off"
      )
   );
   private final m m = this.addSetting(
      X(
         "target-mark",
         "Target Mark",
         "Outline the enemy Silent Aim is turning toward and mark where your real facing points; green once that facing is on the enemy",
         true
      )
   );
   private final b H = this.addSetting(
      j(
         "return-speed",
         "Return Speed",
         "Most the player turns per tick back to your look after Silent Aim lets go, like a hand. 180 returns at once",
         35.0,
         10.0,
         180.0,
         5.0
      )
   );
   private final _b g = new _b();
   private final s6 V = new s6(this);
   private final f_ Z = new f_();
   private final p A = new p();
   private final g y = new g();
   private final com.larpclient.module.a T = new com.larpclient.module.a();
   private final q E = new q();
   private k a;
   private boolean e;
   private boolean q;
   private LocalPlayer I;
   private Object Q;
   private int L = Integer.MIN_VALUE;
   private LivingEntity s;
   private String i = "Waiting";
   private LivingEntity U;
   private int M;
   private int o;
   private int S;
   private double r;
   private boolean P;
   private LivingEntity X;
   private int K = Integer.MIN_VALUE;
   private boolean C;
   private String N = "";
   private String c = "";

   public MaceModule() {
      super("Mace", "Select a mace for smash hits, with optional smash assistance and Silent Aim", Category.COMBAT, -1);
      J = this;
      this.x.a(this.n::getValue);
      this.z.a(this.n::getValue);
      this.p.a(() -> this.n.getValue() || this.D());
      this.Y.a(this::D);
      this.G.a(this::D);
      this.f.a(this::D);
      this.w.a(this.j::getValue);
      this.F.a(this.j::getValue);
      this.R.a(this.j::getValue);
      this.l.a(this.j::getValue);
      this.B.a(this.j::getValue);
      this.m.a(this.j::getValue);
      this.H.a(this.j::getValue);
      this.j.w(var1 -> this.F());
   }

   public static MaceModule U() {
      return J;
   }

   public _b f() {
      return this.g;
   }

   public double y() {
      return this.H.getValue();
   }

   @Override
   public String getDisplayName() {
      return "Auto Mace";
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   private boolean D() {
      return this.h.getValue() || this.j.getValue();
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.F);
   }

   @Override
   public String getStatus() {
      return this.i;
   }

   @Override
   public void onEnable() {
      this.J();
      _q.z(
         "Mace",
         "Active; autoSwap={}, smashAssist={}, silentAim={}, timing={}, minFall={}, stunSlam={}, leadTurn={}, rangeRing={}, targetMark={}, returnSpeed={}",
         this.n.getValue(),
         this.h.getValue(),
         this.j.getValue(),
         this.Y.getValue(),
         this.G.getValue(),
         this.k.getValue(),
         this.F.getValue(),
         this.B.getValue(),
         this.m.getValue(),
         this.H.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.J();
      this.F();
      this.V.X(getMc(), this.p.getValue());
      _q.c("Mace", "Disabled or context suspended; hotbar, view and sprint ownership released");
   }

   public static void h() {
      if (J != null && J.isEnabled()) {
         MaceModule var0 = J;
         if (var0 != null && var0.isRuntimeActive()) {
            Minecraft var1 = getMc();
            var0.I = null;
            var0.Q = null;
            var0.L = Integer.MIN_VALUE;
            if (var1.player != null && var1.player.onGround()) {
               var0.d();
            }

            if (var0.X == null) {
               var0.V.G(var1, var0.p.getValue());
            }
         }
      }
   }

   private void d() {
      if (this.P) {
         _q.z("Mace", "Landed before the server confirmed the smash on {}", s(this.U));
      }

      this.e = this.P = (boolean)0;
      this.U = null;
      this.S = 0;
      this.X = null;
      this.N = "";
   }

   public void M(LocalPlayer var1, Entity var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var1 == getMc().player && var2 instanceof LivingEntity var3) {
            boolean var4 = MaceItem.canSmashAttack(var1);
            if (this.R(var1, var2)) {
               int var7 = qg.K(var1);
               if (this.V.h(var1, var7)) {
                  qg.R(var3);
                  this.X = var3;
                  this.K = var1.tickCount;
                  this.C = this.k.w("Same Tick");
                  this.i = "Stun Slam";
                  _q.z("Mace", "Stun Slam on {}: axe slot {} for your hit, smash {}", s(var3), var7 + 1, this.C ? "in the same tick" : "next tick");
               }
            } else if (this.n.getValue() && (!qg.S(var3, var1) || qg.y(var3))) {
               if (var4 || this.z.getValue() && !this.l("Breach Swap")) {
                  int var5 = this.a(var1, var3, var4);
                  if (var5 >= 0 && ip.s(var4, v(var1, var1.getInventory().getItem(var5), var3), v(var1, var1.getMainHandItem(), var3))) {
                     int var6 = var1.getInventory().getSelectedSlot();
                     if (this.V.h(var1, var5) && var6 != var5) {
                        _q.c("Mace", "Selected hotbar slot {} for {} hit", var5 + 1, var4 ? "smash" : "Breach");
                     }

                     if (var4) {
                        this.x(var1, var3);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean l(String var1) {
      if (!vm.G()) {
         return false;
      }

      if (!var1.equals(this.c)) {
         _q.z("Mace", "{} skipped: {} resets the charge on a swap", var1, vm.M());
      }

      this.c = var1;
      return true;
   }

   public boolean R(LocalPlayer var1, Entity var2) {
      if (!this.isEnabled()) {
         return false;
      } else if (this.isRuntimeActive()
         && !this.k.w("Off")
         && var1 == getMc().player
         && var2 instanceof Player var3
         && !_p.v()
         && MaceItem.canSmashAttack(var1)) {
         ip.q var4 = ip.B(this.k.getValue(), qg.S(var3, var1), qg.y(var3), qg.K(var1) >= 0, this.b(var1, var3) >= 0);
         return var4 == ip.q.D || var4 == ip.q.X;
      } else {
         return false;
      }
   }

   public void D(LocalPlayer var1, Entity var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var2 == this.X && this.C && this.K == var1.tickCount) {
            LivingEntity var3 = this.X;
            this.X = null;
            this.R(getMc(), var1, var3, "Stun Slam smash after your axe hit");
         }
      }
   }

   private boolean R(Minecraft var1, LocalPlayer var2, LivingEntity var3, String var4) {
      int var5 = this.b(var2, var3);
      if (var5 >= 0 && this.V.h(var2, var5)) {
         boolean var6 = _p.i(this, var3, () -> {
            var1.gameMode.attack(var2, var3);
            h8.Q(var2, InteractionHand.MAIN_HAND);
         });
         if (var6) {
            this.x(var2, var3);
            _q.z("Mace", "{}: fall {}, mace slot {}", var4, l(var2.fallDistance), var5 + 1);
         }

         return var6;
      } else {
         _q.z("Mace", "{} skipped: no usable mace", var4);
         return false;
      }
   }

   private int b(LocalPlayer var1, LivingEntity var2) {
      if (var1.getMainHandItem().is(Items.MACE) && !var1.getMainHandItem().nextDamageWillBreak()) {
         return var1.getInventory().getSelectedSlot();
      } else {
         return this.n.getValue() ? this.a(var1, var2, true) : -1;
      }
   }

   private int a(LocalPlayer var1, LivingEntity var2, boolean var3) {
      ip.o[] var4 = new ip.o[Inventory.getSelectionSize()];

      for (int var5 = 0; var5 < var4.length; var5++) {
         ItemStack var6 = var1.getInventory().getItem(var5);
         if (var6.is(Items.MACE) && !var6.nextDamageWillBreak()) {
            int var7 = d(var6, Enchantments.BREACH);
            if (var3 || var7 != 0) {
               var4[var5] = new ip.o(var5, true, false, var7, d(var6, Enchantments.WIND_BURST), v(var1, var6, var2));
            }
         }
      }

      return ip.b(var4, var3, this.x.w("Wind Burst"));
   }

   private static float v(LocalPlayer var0, ItemStack var1, LivingEntity var2) {
      return ip.e(
         qg.q(var0.getMainHandItem()),
         qg.e(var0),
         var0.fallDistance,
         var1.is(Items.MACE),
         MaceItem.canSmashAttack(var0),
         d(var1, Enchantments.DENSITY),
         qg.E(var0, var2),
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
      boolean var1 = this.q;
      this.q = (boolean)0;
      Minecraft var2 = getMc();
      if (var2.player != null) {
         this.h(var2.player);
      }

      boolean var10 = false;
      byte var9 = this.e;
      int var10001 = Integer.signum(var9);
      String var18;
      if (((var10001 | -var10001) >>> 31 ^ 1) == 0) {
         boolean var12 = false;
         byte var11 = this.P;
         var10001 = Integer.signum(var11);
         var18 = ((var10001 | -var10001) >>> 31 ^ 1) == 0 ? "Waiting for the server" : "Smash used this fall";
      } else {
         var18 = "Waiting";
      }

      this.i = var18;
      if (this.X != null && !this.C) {
         this.Y(var2);
      } else {
         label120: {
            if (this.C && this.X != null && var2.player != null) {
               int var16 = var2.player.tickCount;
               int var15 = this.K;
               var10001 = Integer.compare(var15, var16);
               if ((var10001 | -var10001) >>> 31 == 0) {
                  var20 = this.X;
                  break label120;
               }
            }

            var20 = null;
         }

         this.X = var20;
         if (!this.D()) {
            this.F();
         } else {
            LivingEntity var3 = this.j.getValue() ? this.H(var2) : null;
            if (var3 == null) {
               this.F();
            }

            if (qg.j(var2, ij.r.w) && s6.L(this)) {
               LocalPlayer var4;
               EntityHitResult var5;
               LivingEntity var6;
               int var7;
               int var22;
               boolean var10000;
               boolean var10002;
               int var10003;
               boolean var10004;
               int var10005;
               boolean var10006;
               double var10007;
               double var10008;
               label96: {
                  var4 = var2.player;
                  var5 = qg.V(var2);
                  var6 = this.A(var4, var5, var3);
                  var7 = var6 == null && var3 == null ? 0 : 1;
                  var10000 = qg.Y(var4);
                  var21 = qg.t(var2);
                  var10002 = qg.O(var2);
                  var10003 = var3 == null && b6.x() ? 1 : 0;
                  var10004 = qg.g(var2, var4);
                  var10005 = var4.getDeltaMovement().y < 0.0 ? 1 : 0;
                  var10006 = MaceItem.canSmashAttack(var4);
                  var10007 = var4.fallDistance;
                  var10008 = this.G.getValue();
                  if (this.e) {
                     boolean var14 = false;
                     byte var13 = this.P;
                     var22 = Integer.signum(var13);
                     if ((var22 | -var22) >>> 31 == 0) {
                        var22 = 1;
                        break label96;
                     }
                  }

                  var22 = 0;
               }

               String var8 = ip.U(
                  var10000,
                  var21,
                  var10002,
                  (boolean)var10003,
                  var10004,
                  (boolean)var10005,
                  var10006,
                  var10007,
                  var10008,
                  (boolean)var22,
                  this.P,
                  var6 != null && this.b(var4, var6) >= 0,
                  var6 != null
               );
               if (var8 == null) {
                  this.j(var2, var4, var5, var6, var3, var1);
               } else if (var7 != 0) {
                  this.J(var8, var4, var3);
               }

               if (var3 != null && (!this.e || this.P)) {
                  if (!qg.Y(var4)) {
                     this.H(var2, var3);
                  }
               } else {
                  this.F();
               }
            } else {
               this.F();
            }
         }
      }
   }

   private LivingEntity A(LocalPlayer var1, EntityHitResult var2, LivingEntity var3) {
      if (!(var2 != null && var2.getEntity() instanceof LivingEntity var4)) {
         return null;
      } else if (!this.h.getValue() && var4 != var3) {
         return null;
      } else {
         return qg.L(var1, var4) && (var4 instanceof Player || this.f.getValue()) ? var4 : null;
      }
   }

   private void J(String var1, LocalPlayer var2, LivingEntity var3) {
      if (!var1.equals(this.N)) {
         this.N = var1;
         _q.z(
            "Mace",
            "Smash held: {}; fall {}, speed {}{}",
            var1,
            l(var2.fallDistance),
            l(var2.getDeltaMovement().y),
            var3 == null ? "" : ", Silent Aim target " + s(var3) + " at " + l(jz.j(var2.getEyePosition(), var3.getBoundingBox()))
         );
      }
   }

   private void j(Minecraft var1, LocalPlayer var2, EntityHitResult var3, LivingEntity var4, LivingEntity var5, boolean var6) {
      this.N = "";
      ip.q var7 = ip.B(this.k.getValue(), qg.S(var4, var2), qg.y(var4), qg.K(var2) >= 0, this.b(var2, var4) >= 0);
      if ((var7 != ip.q.X || !k(var2, var4)) && var7 != ip.q.I) {
         if (var7 == ip.q.o && qg.S(var4, var2) && !qg.y(var4)) {
            this.J("raised shield and no axe in the hotbar", var2, var5);
         } else if (this.y(var2, var4)) {
            this.q = (boolean)1;
            this.I = var2;
            this.Q = var1.level;
            this.L = var2.tickCount;
            var2.setSprinting(false);
            qg.g(var1, Math.max(qg.W(var1), 1));
            this.i = "Delaying smash";
            if (!var6) {
               _q.c("Mace", "Delaying smash while the predicted next step remains in reach");
            }
         } else if (var7 != ip.q.o) {
            this.D(var1, var2, var4, var7);
         } else {
            int var8 = this.b(var2, var4);
            if (var8 == var2.getInventory().getSelectedSlot() || this.V.h(var2, var8)) {
               if (qg.O(var1, var3)) {
                  this.x(var2, var4);
                  qg.z(var1);
                  _q.z("Mace", "Smash sent on {}: fall {}, speed {}, silent={}", s(var4), l(var2.fallDistance), l(var2.getDeltaMovement().y), var4 == var5);
               } else {
                  _q.z("Mace", "Smash attack refused by the client gate on {}", s(var4));
               }
            }
         }
      } else {
         this.D(var1, var2, var4, var7);
      }
   }

   private void D(Minecraft var1, LocalPlayer var2, LivingEntity var3, ip.q var4) {
      int var5 = qg.K(var2);
      int var6 = this.b(var2, var3);
      if (this.V.h(var2, var5)) {
         int var7 = var4 == ip.q.D ? 1 : 0;
         boolean var8 = _p.d(this, var3, () -> {
            var1.gameMode.attack(var2, var3);
            h8.Q(var2, InteractionHand.MAIN_HAND);
            qg.R(var3);
            if (var7 && this.V.h(var2, var6)) {
               var1.gameMode.attack(var2, var3);
               h8.Q(var2, InteractionHand.MAIN_HAND);
            }
         });
         if (!var8) {
            this.V.X(var1, true);
            _q.z("Mace", "Stun Slam refused: this tick's attack belongs to another owner");
         } else {
            qg.z(var1);
            if (var7 != 0) {
               this.x(var2, var3);
               this.i = "Stun Slam";
               _q.z("Mace", "Stun Slam on {}: axe slot {} then mace slot {} in one tick, fall {}", s(var3), var5 + 1, var6 + 1, l(var2.fallDistance));
            } else if (var4 == ip.q.X) {
               this.X = var3;
               this.K = var2.tickCount;
               this.C = (boolean)0;
               this.i = "Stun Slam";
               _q.z("Mace", "Stun Slam on {}: axe slot {} now, smash next tick", s(var3), var5 + 1);
            } else {
               this.i = "Shield break hit";
               _q.z("Mace", "Shield break hit on {} with axe slot {}; no mace for the smash", s(var3), var5 + 1);
            }
         }
      }
   }

   private void Y(Minecraft var1) {
      LocalPlayer var2 = var1.player;
      LivingEntity var3 = this.X;
      if (var2 != null && var2.tickCount == this.K + 1) {
         this.X = null;
         EntityHitResult var4 = qg.V(var1);
         if (var4 != null && var4.getEntity() == var3 && MaceItem.canSmashAttack(var2) && !var2.onGround()) {
            int var5 = this.b(var2, var3);
            if (var5 >= 0 && this.V.h(var2, var5)) {
               boolean var6 = _p.d(this, var3, () -> {
                  var1.gameMode.attack(var2, var3);
                  h8.Q(var2, InteractionHand.MAIN_HAND);
               });
               if (var6) {
                  this.x(var2, var3);
                  qg.z(var1);
                  _q.z("Mace", "Stun Slam smash on {} one tick after the axe: fall {}", s(var3), l(var2.fallDistance));
               } else {
                  _q.z("Mace", "Next Tick smash on {} refused: this tick's attack belongs to another owner", s(var3));
               }
            } else {
               this.V.X(var1, this.p.getValue());
            }
         } else {
            _q.z(
               "Mace",
               "Next Tick smash on {} skipped: {}",
               s(var3),
               var2.onGround() ? "landed" : (!MaceItem.canSmashAttack(var2) ? "fall ended" : "facing left the target")
            );
            this.V.X(var1, this.p.getValue());
         }
      } else {
         if (var2 == null || var2.tickCount > this.K + 1) {
            this.X = null;
            this.V.X(var1, this.p.getValue());
         }
      }
   }

   private void x(LocalPlayer var1, LivingEntity var2) {
      this.e = this.P = (boolean)1;
      this.U = var2;
      this.M = var1.tickCount;
      this.o = var2.hurtTime;
      this.r = var1.getDeltaMovement().y;
      this.i = "Waiting for the server";
   }

   private void h(LocalPlayer var1) {
      if (this.P && this.U != null) {
         if (ip.N(this.r, var1.getDeltaMovement().y, this.o, this.U.hurtTime)) {
            this.P = (boolean)0;
            _q.z("Mace", "Smash confirmed on {} after {} ticks", s(this.U), var1.tickCount - this.M);
         } else if (var1.tickCount - this.M >= ip.j(this.x(var1))) {
            this.P = (boolean)0;
            if (this.S < 1 && !var1.onGround() && MaceItem.canSmashAttack(var1)) {
               this.S++;
               this.e = (boolean)0;
               _q.z("Mace", "No server confirmation for the smash on {} after {} ticks; retrying once this fall", s(this.U), var1.tickCount - this.M);
            } else {
               _q.z("Mace", "No server confirmation for the smash on {}; not retrying ({})", s(this.U), this.S >= 1 ? "already retried" : "fall ended");
            }
         }
      }
   }

   private int x(LocalPlayer var1) {
      ClientPacketListener var2 = getMc().getConnection();
      PlayerInfo var3 = var2 == null ? null : var2.getPlayerInfo(var1.getUUID());
      return var3 == null ? 0 : Math.round(var3.getLatency() / 50.0F);
   }

   private LivingEntity H(Minecraft var1) {
      LocalPlayer var2 = var1.player;
      if (var2 != null && var1.level != null && (!this.e || this.P) && !var2.onGround() && !(var2.getDeltaMovement().y >= 0.0) && ij.a(ij.r.w)) {
         if (this.s != null && this.p(var2, this.s, this.x(var2, this.s))) {
            return this.s;
         }

         Vec3 var3 = var2.getEyePosition();
         double var4 = 16.0;
         LivingEntity var6 = null;
         double var7 = Double.MAX_VALUE;

         for (Entity var10 : var1.level
            .getEntities(var2, var2.getBoundingBox().inflate(Math.max(this.w.getValue(), var4)), var0 -> var0 instanceof LivingEntity)) {
            LivingEntity var11 = (LivingEntity)var10;
            double var12 = jz.j(var3, var11.getBoundingBox());
            if (var12 < var7 && this.p(var2, var11, this.x(var2, var11))) {
               var6 = var11;
               var7 = var12;
            }
         }

         return var6;
      } else {
         return null;
      }
   }

   private double x(LocalPlayer var1, LivingEntity var2) {
      c var3 = new c(var1.getYRot(), var1.getXRot());
      int var4 = this.g().ticksToReach(var3, com.larpclient.module.w.U(b(var1, var2)));
      return ip.u(this.w.getValue(), this.F.getValue(), -var1.getDeltaMovement().y, var4, var1.entityInteractionRange());
   }

   public double w(LocalPlayer var1) {
      if (!this.isEnabled()) {
         return 0.0;
      } else if (this.isRuntimeActive() && this.j.getValue() && var1 != null && !this.B.w("Off") && (!this.B.w("In Air") || !var1.onGround())) {
         c var2 = new c(var1.getYRot(), var1.getXRot());
         int var3 = this.g().ticksToReach(var2, new c(var1.getYRot(), 90.0F));
         return ip.u(this.w.getValue(), this.F.getValue(), -var1.getDeltaMovement().y, var3, var1.entityInteractionRange());
      } else {
         return 0.0;
      }
   }

   public LivingEntity W() {
      if (!this.isEnabled()) {
         return null;
      } else {
         return this.m.getValue() && this.X() ? this.s : null;
      }
   }

   private boolean p(LocalPlayer var1, LivingEntity var2, double var3) {
      Vec3 var5 = var1.getEyePosition();
      return qg.L(var1, var2)
         && (var2 instanceof Player || this.f.getValue())
         && jz.j(var5, var2.getBoundingBox()) <= var3
         && this.d(var1, var2)
         && qg.d(var1, var5, b(var1, var2));
   }

   private static Vec3 b(LocalPlayer var0, LivingEntity var1) {
      AABB var2 = var1.getBoundingBox();
      Vec3 var3 = var0.getEyePosition();
      AABB var4 = var2.deflate(Math.min(0.1, var2.getXsize() * 0.25), Math.min(0.1, var2.getYsize() * 0.25), Math.min(0.1, var2.getZsize() * 0.25));
      Vec3 var5 = new Vec3(Math.clamp(var3.x, var4.minX, var4.maxX), Math.clamp(var3.y, var4.minY, var4.maxY), Math.clamp(var3.z, var4.minZ, var4.maxZ));
      Vec3 var6 = new Vec3(var2.getCenter().x, var2.minY + var2.getYsize() * 0.6, var2.getCenter().z);
      return _p.V(var1, var0.entityInteractionRange(), var6, ow.w(var2, var5)).orElse(var6);
   }

   private k g() {
      return switch ((String)this.l.getValue()) {
         case "FunTime" -> this.A;
         case "Polar" -> this.y;
         case "Matrix" -> this.T;
         case "Legit" -> this.E;
         default -> this.Z;
      };
   }

   private void H(Minecraft var1, LivingEntity var2) {
      LocalPlayer var3 = var1.player;
      if (!_p.Q(this)) {
         this.F();
      } else {
         k var4 = this.g();
         if (var4 != this.a) {
            if (this.a != null) {
               this.a.reset();
            }

            this.a = var4;
         }

         AABB var5 = var2.getBoundingBox();
         if (var2 != this.s) {
            this.a.targetChanged(var2);
            _q.z(
               "Mace",
               "Silent Aim target {} at {} blocks, falling {} blocks/tick, rotation={}",
               s(var2),
               l(jz.j(var3.getEyePosition(), var5)),
               l(-var3.getDeltaMovement().y),
               this.l.getValue()
            );
         }

         this.s = var2;
         ij.V(ij.r.w);
         if (ij.L() != ij.r.w) {
            this.s = null;
         } else {
            c var6 = new c(var3.getYRot(), var3.getXRot());
            double var7 = var3.entityInteractionRange();
            int var9 = jz.j(var3.getEyePosition(), var5) <= var7 ? 1 : 0;
            q71cffb54eb30bf94 var10 = (q71cffb54eb30bf94)var3;
            boolean var11 = v.B(var3, var5.inflate(var2.getPickRadius()), var10.larpYRotLast(), var10.larpXRotLast(), var7 + 1.0, false);
            c var12 = this.a.rotate(var6, com.larpclient.module.w.U(b(var3, var2)), var2, (boolean)var9, var11);
            float[] var13 = nx.M(var6.V, var6.T, var12.V, var12.T, this.R.getValue());
            var12 = new c(var13[0], var13[1]);
            var12 = this.a == this.E ? this.E.x(var6, var12) : com.larpclient.module.w.H(var6, var12);
            b6.P(this, b6.w.q, var12.V, var12.T, 1.0);
            if (!b6.c(b6.w.q)) {
               _q.c("Mace", "Silent Aim yielded to a higher rotation owner");
               this.F();
            } else {
               if (!this.q) {
                  this.i = "Silent aim";
               }
            }
         }
      }
   }

   private void F() {
      if (this.s != null && this.a != null) {
         this.a.reset();
      }

      this.s = null;
      ij.P(ij.r.w, this.g, this.H.getValue(), true);
   }

   public float C(float var1, float var2) {
      if (this.isEnabled()) {
         return this.X() ? AuraModule.H(var1, var2, 35.0F, 0.25F) : var1;
      } else {
         return var1;
      }
   }

   public boolean X() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.j.getValue() && this.s != null && this.s.isAlive() && !this.s.isRemoved() && _p.x(this);
   }

   public void n() {
      if (this.isEnabled()) {
         this.e = (boolean)1;
         this.i = "Smash used this fall";
      }
   }

   public boolean d(LocalPlayer var1, LivingEntity var2) {
      if (!this.isEnabled()) {
         return false;
      }

      boolean var3 = var1.getMainHandItem().is(Items.MACE);
      int var4 = !var3
            && this.n.getValue()
            && this.isRuntimeActive()
            && !this.e
            && MaceItem.canSmashAttack(var1)
            && var1.fallDistance >= this.G.getValue()
            && this.a(var1, var2, true) >= 0
         ? 1
         : 0;
      return ip.Z(this.isRuntimeActive(), this.e, MaceItem.canSmashAttack(var1), var1.fallDistance, this.G.getValue(), var3, this.n.getValue(), (boolean)var4);
   }

   public boolean y(LocalPlayer var1, LivingEntity var2) {
      return this.isEnabled()
         ? ip.a(k(var1, var2), this.Y.w("Delayed"), qg.e(var1), var1.getAttackStrengthScale(1.5F), var2.hurtTime, var1.isSprinting())
         : false;
   }

   private static boolean k(LocalPlayer var0, LivingEntity var1) {
      Vec3 var2 = var0.getDeltaMovement();
      return ip.T(
         var0.level().noCollision(var0, var0.getBoundingBox().move(var2)),
         var0.entityInteractionRange(),
         var0.getEyePosition(),
         var2,
         var1.getBoundingBox(),
         var1.position().subtract(var1.xo, var1.yo, var1.zo),
         var1.getPickRadius(),
         var0.getViewVector(1.0F),
         (var1x, var2x) -> qg.d(var0, var1x, var2x)
      );
   }

   public static boolean Z() {
      if (J != null && J.isEnabled()) {
         MaceModule var0 = J;
         if (var0 != null && var0.isRuntimeActive() && var0.D()) {
            Minecraft var1 = getMc();
            if (var1.player == null) {
               return false;
            } else {
               return ij.L() == ij.r.w
                  ? true
                  : var1.player == var0.I
                     && var1.level == var0.Q
                     && var1.gui.screen() == null
                     && var1.player.tickCount >= var0.L
                     && (long)var1.player.tickCount - var0.L <= 1L;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static Input B(LocalPlayer var0, Input var1) {
      if (J != null && J.isEnabled()) {
         MaceModule var2 = J;
         if (var2 != null && var2.isRuntimeActive() && var0 != null && var0 == getMc().player && ij.L() == ij.r.w) {
            if (var0.isSprinting()) {
               var0.setSprinting(false);
               _q.c("Mace", "Silent Aim lock released sprint");
            }

            return qg.Y(var1);
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void J() {
      this.e = this.P = (boolean)0;
      this.q = (boolean)0;
      this.U = null;
      this.S = 0;
      this.X = null;
      this.N = "";
      this.I = null;
      this.Q = null;
      this.L = Integer.MIN_VALUE;
      this.i = "Waiting";
   }

   private static String s(Entity var0) {
      return var0 == null ? "?" : var0.getName().getString();
   }

   private static String l(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static m X(String var0, final String var1, String var2, boolean var3) {
      return new m(var0, var2, var3) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }

   private static b j(String var0, final String var1, String var2, double var3, double var5, double var7, double var9) {
      return new b(var0, var2, var3, var5, var7, var9) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }

   private static o a(String var0, final String var1, String var2, String var3, String... var4) {
      return new o(var0, var2, var3, var4) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }
}

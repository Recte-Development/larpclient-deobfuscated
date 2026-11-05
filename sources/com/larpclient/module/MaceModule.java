package com.larpclient.module;

import com.larp.obf.qbac0b262827a15ac.q12d31c89547aa6a5;
import com.larpclient._8;
import com.larpclient._m;
import com.larpclient.c2;
import com.larpclient.eq;
import com.larpclient.h_;
import com.larpclient.hq;
import com.larpclient.m9;
import com.larpclient.mt;
import com.larpclient.p1;
import com.larpclient.ti;
import com.larpclient.tq;
import com.larpclient.w7;
import com.larpclient.xm;
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
   private static MaceModule r;
   private final dh l = this.addSetting(D("auto-swap", "Auto Swap", "Select a mace immediately before a smash hit", true));
   private final d6 X = this.addSetting(y("pick", "Pick", "Prefer damage or a Wind Burst mace for smash hits", "Damage", "Damage", "Wind Burst"));
   private final dh K = this.addSetting(
      D(
         "breach-swap",
         "Breach Swap",
         "Use a Breach mace for ordinary hits when expected damage improves by 15%. Skipped on Paper servers, which reset the charge on a swap",
         false
      )
   );
   private final dh v = this.addSetting(D("swap-back", "Swap Back", "Return to the previous hotbar slot after the attack", true));
   private final dh n = this.addSetting(D("smash-assist", "Smash Assist", "Attack the enemy under the crosshair during a qualifying fall", false));
   private final d6 u = this.addSetting(
      y("smash-timing", "Smash Timing", "Delay while the next movement step can still reach the target", "Delayed", "Direct", "Delayed")
   );
   private final d9 R = this.addSetting(new d9("min-fall", "Minimum fall distance for automatic smash hits", 3.0, 1.5, 30.0, 0.5) {
      @Override
      public String getDisplayName() {
         return "Min Fall";
      }
   });
   private final dh M = this.addSetting(D("mobs", "Mobs", "Allow smash assistance against non-player enemies", false));
   private final dh V = this.addSetting(
      D("silent-aim", "Silent Aim", "Turn toward a nearby enemy and smash it during a fall while your view stays free", false)
   );
   private final d9 i = this.addSetting(x("aim-range", "Aim Range", "Distance at which Silent Aim starts turning toward an enemy", 6.0, 3.0, 10.0, 0.5));
   private final d9 m = this.addSetting(x("turn-speed", "Turn Speed", "Maximum Silent Aim turn in degrees per tick", 60.0, 10.0, 180.0, 5.0));
   private final d6 L = this.addSetting(
      y(
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
   private final dh e = this.addSetting(
      D("lead-turn", "Lead Turn", "In a fast fall, start turning early enough that the turn finishes before the target is in reach", true)
   );
   private final d6 b = this.addSetting(
      y(
         "stun-slam",
         "Stun Slam",
         "When a smash target raises a shield, hit it with your best axe to disable the shield, then smash. Same Tick sends both hits at once; Next Tick smashes a tick later, which Grim's experimental packet order check does not flag",
         "Same Tick",
         "Same Tick",
         "Next Tick",
         "Off"
      )
   );
   private final d6 S = this.addSetting(
      y(
         "range-ring",
         "Range Ring",
         "Draw Silent Aim's reach on the ground below you: an enemy standing inside the ring is in range. In Air shows it only while you are off the ground",
         "In Air",
         "In Air",
         "Always",
         "Off"
      )
   );
   private final dh C = this.addSetting(
      D(
         "target-mark",
         "Target Mark",
         "Outline the enemy Silent Aim is turning toward and mark where your real facing points; green once that facing is on the enemy",
         true
      )
   );
   private final d9 y = this.addSetting(
      x(
         "return-speed",
         "Return Speed",
         "Most the player turns per tick back to your look after Silent Aim lets go, like a hand. 180 returns at once",
         35.0,
         10.0,
         180.0,
         5.0
      )
   );
   private final h_ f = new h_();
   private final ti A = new ti(this);
   private final i c = new i();
   private final da W = new da();
   private final x B = new x();
   private final db O = new db();
   private final di d = new di();
   private t t;
   private boolean H;
   private boolean w;
   private LocalPlayer h;
   private Object E;
   private int D = Integer.MIN_VALUE;
   private LivingEntity I;
   private String P = "Waiting";
   private LivingEntity p;
   private int x;
   private int U;
   private int Q;
   private double Z;
   private boolean g;
   private LivingEntity T;
   private int a = Integer.MIN_VALUE;
   private boolean N;
   private String q = "";
   private String o = "";

   public MaceModule() {
      super("Mace", "Select a mace for smash hits, with optional smash assistance and Silent Aim", Category.COMBAT, -1);
      r = this;
      this.X.H(this.l::P);
      this.K.H(this.l::P);
      this.v.H(() -> this.l.P() || this.x());
      this.u.H(this::x);
      this.R.H(this::x);
      this.M.H(this::x);
      this.i.H(this.V::P);
      this.e.H(this.V::P);
      this.m.H(this.V::P);
      this.L.H(this.V::P);
      this.S.H(this.V::P);
      this.C.H(this.V::P);
      this.y.H(this.V::P);
      this.V.h(var1 -> this.j());
   }

   public static MaceModule c() {
      return r;
   }

   public h_ W() {
      return this.f;
   }

   public double A() {
      return this.y.P();
   }

   @Override
   public String getDisplayName() {
      return "Auto Mace";
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.Z;
   }

   private boolean x() {
      return this.n.P() || this.V.P();
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z);
   }

   @Override
   public String getStatus() {
      return this.P;
   }

   @Override
   public void onEnable() {
      this.l();
      mt.K(
         "Mace",
         "Active; autoSwap={}, smashAssist={}, silentAim={}, timing={}, minFall={}, stunSlam={}, leadTurn={}, rangeRing={}, targetMark={}, returnSpeed={}",
         this.l.P(),
         this.n.P(),
         this.V.P(),
         this.u.P(),
         this.R.P(),
         this.b.P(),
         this.e.P(),
         this.S.P(),
         this.C.P(),
         this.y.P().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.l();
      this.j();
      this.A.W(getMc(), this.v.P());
      mt.U("Mace", "Disabled or context suspended; hotbar, view and sprint ownership released");
   }

   public static void z() {
      if (r != null && r.isEnabled()) {
         MaceModule var0 = r;
         if (var0 != null && var0.isRuntimeActive()) {
            Minecraft var1 = getMc();
            var0.h = null;
            var0.E = null;
            var0.D = Integer.MIN_VALUE;
            if (var1.player != null && var1.player.onGround()) {
               var0.E();
            }

            if (var0.T == null) {
               var0.A.r(var1, var0.v.P());
            }
         }
      }
   }

   private void E() {
      if (this.g) {
         mt.K("Mace", "Landed before the server confirmed the smash on {}", k(this.p));
      }

      this.H = this.g = (boolean)0;
      this.p = null;
      this.Q = 0;
      this.T = null;
      this.q = "";
   }

   public void L(LocalPlayer var1, Entity var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var1 == getMc().player && var2 instanceof LivingEntity var3) {
            boolean var4 = MaceItem.canSmashAttack(var1);
            if (this.D(var1, var2)) {
               int var7 = w7.W(var1);
               if (this.A.e(var1, var7)) {
                  w7.l(var3);
                  this.T = var3;
                  this.a = var1.tickCount;
                  this.N = this.b.S("Same Tick");
                  this.P = "Stun Slam";
                  mt.K("Mace", "Stun Slam on {}: axe slot {} for your hit, smash {}", k(var3), var7 + 1, this.N ? "in the same tick" : "next tick");
               }
            } else if (this.l.P() && (!w7.o(var3, var1) || w7.p(var3))) {
               if (var4 || this.K.P() && !this.k("Breach Swap")) {
                  int var5 = this.j(var1, var3, var4);
                  if (var5 >= 0 && eq.x(var4, k(var1, var1.getInventory().getItem(var5), var3), k(var1, var1.getMainHandItem(), var3))) {
                     int var6 = var1.getInventory().getSelectedSlot();
                     if (this.A.e(var1, var5) && var6 != var5) {
                        mt.U("Mace", "Selected hotbar slot {} for {} hit", var5 + 1, var4 ? "smash" : "Breach");
                     }

                     if (var4) {
                        this.p(var1, var3);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean k(String var1) {
      if (!c2.C()) {
         return false;
      }

      if (!var1.equals(this.o)) {
         mt.K("Mace", "{} skipped: {} resets the charge on a swap", var1, c2.v());
      }

      this.o = var1;
      return true;
   }

   public boolean D(LocalPlayer var1, Entity var2) {
      if (!this.isEnabled()) {
         return false;
      } else if (this.isRuntimeActive()
         && !this.b.S("Off")
         && var1 == getMc().player
         && var2 instanceof Player var3
         && !_m.w()
         && MaceItem.canSmashAttack(var1)) {
         eq.n var4 = eq.T(this.b.P(), w7.o(var3, var1), w7.p(var3), w7.W(var1) >= 0, this.Y(var1, var3) >= 0);
         return var4 == eq.n.p || var4 == eq.n.Q;
      } else {
         return false;
      }
   }

   public void U(LocalPlayer var1, Entity var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var2 == this.T && this.N && this.a == var1.tickCount) {
            LivingEntity var3 = this.T;
            this.T = null;
            this.c(getMc(), var1, var3, "Stun Slam smash after your axe hit");
         }
      }
   }

   private boolean c(Minecraft var1, LocalPlayer var2, LivingEntity var3, String var4) {
      int var5 = this.Y(var2, var3);
      if (var5 >= 0 && this.A.e(var2, var5)) {
         boolean var6 = _m.q(this, var3, () -> {
            var1.gameMode.attack(var2, var3);
            _8.f(var2, InteractionHand.MAIN_HAND);
         });
         if (var6) {
            this.p(var2, var3);
            mt.K("Mace", "{}: fall {}, mace slot {}", var4, z(var2.fallDistance), var5 + 1);
         }

         return var6;
      } else {
         mt.K("Mace", "{} skipped: no usable mace", var4);
         return false;
      }
   }

   private int Y(LocalPlayer var1, LivingEntity var2) {
      if (var1.getMainHandItem().is(Items.MACE) && !var1.getMainHandItem().nextDamageWillBreak()) {
         return var1.getInventory().getSelectedSlot();
      } else {
         return this.l.P() ? this.j(var1, var2, true) : -1;
      }
   }

   private int j(LocalPlayer var1, LivingEntity var2, boolean var3) {
      eq.w[] var4 = new eq.w[Inventory.getSelectionSize()];

      for (int var5 = 0; var5 < var4.length; var5++) {
         ItemStack var6 = var1.getInventory().getItem(var5);
         if (var6.is(Items.MACE) && !var6.nextDamageWillBreak()) {
            int var7 = E(var6, Enchantments.BREACH);
            if (var3 || var7 != 0) {
               var4[var5] = new eq.w(var5, true, false, var7, E(var6, Enchantments.WIND_BURST), k(var1, var6, var2));
            }
         }
      }

      return eq.O(var4, var3, this.X.S("Wind Burst"));
   }

   private static float k(LocalPlayer var0, ItemStack var1, LivingEntity var2) {
      return eq.Y(
         w7.c(var0.getMainHandItem()),
         w7.B(var0),
         var0.fallDistance,
         var1.is(Items.MACE),
         MaceItem.canSmashAttack(var0),
         E(var1, Enchantments.DENSITY),
         w7.a(var0, var2),
         E(var1, Enchantments.SHARPNESS),
         var2.getArmorValue(),
         (float)var2.getAttributeValue(Attributes.ARMOR_TOUGHNESS),
         E(var1, Enchantments.BREACH)
      );
   }

   private static int E(ItemStack var0, ResourceKey<Enchantment> var1) {
      for (Entry var3 : var0.getEnchantments().entrySet()) {
         if (((Holder)var3.getKey()).is(var1)) {
            return var3.getIntValue();
         }
      }

      return 0;
   }

   @Override
   public void onTick() {
      boolean var1 = this.w;
      this.w = (boolean)0;
      Minecraft var2 = getMc();
      if (var2.player != null) {
         this.w(var2.player);
      }

      String var18;
      if (this.H) {
         boolean var10 = false;
         byte var9 = this.g;
         int var10001 = Integer.signum(var9);
         var18 = ((var10001 | -var10001) >>> 31 ^ 1) == 0 ? "Waiting for the server" : "Smash used this fall";
      } else {
         var18 = "Waiting";
      }

      this.P = var18;
      if (this.T != null && !this.N) {
         this.B(var2);
      } else {
         label120: {
            if (this.N && this.T != null && var2.player != null) {
               int var16 = var2.player.tickCount;
               int var15 = this.a;
               int var19 = Integer.compare(var15, var16);
               if ((var19 | -var19) >>> 31 == 0) {
                  var20 = this.T;
                  break label120;
               }
            }

            var20 = null;
         }

         this.T = var20;
         if (!this.x()) {
            this.j();
         } else {
            LivingEntity var3 = this.V.P() ? this.F(var2) : null;
            if (var3 == null) {
               this.j();
            }

            if (w7.m(var2, tq.y.e) && ti.V(this)) {
               LocalPlayer var4 = var2.player;
               EntityHitResult var5 = w7.q(var2);
               LivingEntity var6 = this.y(var4, var5, var3);
               int var7 = var6 == null && var3 == null ? 0 : 1;
               int var10000 = w7.H(var4);
               boolean var21 = w7.n(var2);
               boolean var10002 = w7.s(var2);
               int var10003 = var3 == null && m9.R() ? 1 : 0;
               boolean var10004 = w7.f(var2, var4);
               int var10005 = var4.getDeltaMovement().y < 0.0 ? 1 : 0;
               boolean var10006 = MaceItem.canSmashAttack(var4);
               double var10007 = var4.fallDistance;
               double var10008 = this.R.P();
               boolean var12 = false;
               byte var11 = this.H;
               int var10009 = Integer.signum(var11);
               String var8 = eq.v(
                  (boolean)var10000,
                  var21,
                  var10002,
                  (boolean)var10003,
                  var10004,
                  (boolean)var10005,
                  var10006,
                  var10007,
                  var10008,
                  ((var10009 | -var10009) >>> 31 ^ 1) == 0 && !this.g,
                  this.g,
                  var6 != null && this.Y(var4, var6) >= 0,
                  var6 != null
               );
               if (var8 == null) {
                  this.i(var2, var4, var5, var6, var3, var1);
               } else if (var7 != 0) {
                  this.O(var8, var4, var3);
               }

               if (var3 != null) {
                  boolean var14 = false;
                  byte var13 = this.H;
                  var10000 = Integer.signum(var13);
                  if (((var10000 | -var10000) >>> 31 ^ 1) != 0 || this.g) {
                     if (!w7.H(var4)) {
                        this.u(var2, var3);
                     }

                     return;
                  }
               }

               this.j();
            } else {
               this.j();
            }
         }
      }
   }

   private LivingEntity y(LocalPlayer var1, EntityHitResult var2, LivingEntity var3) {
      if (!(var2 != null && var2.getEntity() instanceof LivingEntity var4)) {
         return null;
      } else if (!this.n.P() && var4 != var3) {
         return null;
      } else {
         return w7.E(var1, var4) && (var4 instanceof Player || this.M.P()) ? var4 : null;
      }
   }

   private void O(String var1, LocalPlayer var2, LivingEntity var3) {
      if (!var1.equals(this.q)) {
         this.q = var1;
         mt.K(
            "Mace",
            "Smash held: {}; fall {}, speed {}{}",
            var1,
            z(var2.fallDistance),
            z(var2.getDeltaMovement().y),
            var3 == null ? "" : ", Silent Aim target " + k(var3) + " at " + z(p1.X(var2.getEyePosition(), var3.getBoundingBox()))
         );
      }
   }

   private void i(Minecraft var1, LocalPlayer var2, EntityHitResult var3, LivingEntity var4, LivingEntity var5, boolean var6) {
      this.q = "";
      eq.n var7 = eq.T(this.b.P(), w7.o(var4, var2), w7.p(var4), w7.W(var2) >= 0, this.Y(var2, var4) >= 0);
      if ((var7 != eq.n.Q || !Y(var2, var4)) && var7 != eq.n.c) {
         if (var7 == eq.n.R && w7.o(var4, var2) && !w7.p(var4)) {
            this.O("raised shield and no axe in the hotbar", var2, var5);
         } else if (this.G(var2, var4)) {
            this.w = (boolean)1;
            this.h = var2;
            this.E = var1.level;
            this.D = var2.tickCount;
            var2.setSprinting(false);
            w7.a(var1, Math.max(w7.O(var1), 1));
            this.P = "Delaying smash";
            if (!var6) {
               mt.U("Mace", "Delaying smash while the predicted next step remains in reach");
            }
         } else if (var7 != eq.n.R) {
            this.D(var1, var2, var4, var7);
         } else {
            int var8 = this.Y(var2, var4);
            if (var8 == var2.getInventory().getSelectedSlot() || this.A.e(var2, var8)) {
               if (w7.A(var1, var3)) {
                  this.p(var2, var4);
                  w7.B(var1);
                  mt.K("Mace", "Smash sent on {}: fall {}, speed {}, silent={}", k(var4), z(var2.fallDistance), z(var2.getDeltaMovement().y), var4 == var5);
               } else {
                  mt.K("Mace", "Smash attack refused by the client gate on {}", k(var4));
               }
            }
         }
      } else {
         this.D(var1, var2, var4, var7);
      }
   }

   private void D(Minecraft var1, LocalPlayer var2, LivingEntity var3, eq.n var4) {
      int var5 = w7.W(var2);
      int var6 = this.Y(var2, var3);
      if (this.A.e(var2, var5)) {
         int var7 = var4 == eq.n.p ? 1 : 0;
         boolean var8 = _m.y(this, var3, () -> {
            var1.gameMode.attack(var2, var3);
            _8.f(var2, InteractionHand.MAIN_HAND);
            w7.l(var3);
            if (var7 && this.A.e(var2, var6)) {
               var1.gameMode.attack(var2, var3);
               _8.f(var2, InteractionHand.MAIN_HAND);
            }
         });
         if (!var8) {
            this.A.W(var1, true);
            mt.K("Mace", "Stun Slam refused: this tick's attack belongs to another owner");
         } else {
            w7.B(var1);
            if (var7 != 0) {
               this.p(var2, var3);
               this.P = "Stun Slam";
               mt.K("Mace", "Stun Slam on {}: axe slot {} then mace slot {} in one tick, fall {}", k(var3), var5 + 1, var6 + 1, z(var2.fallDistance));
            } else if (var4 == eq.n.Q) {
               this.T = var3;
               this.a = var2.tickCount;
               this.N = (boolean)0;
               this.P = "Stun Slam";
               mt.K("Mace", "Stun Slam on {}: axe slot {} now, smash next tick", k(var3), var5 + 1);
            } else {
               this.P = "Shield break hit";
               mt.K("Mace", "Shield break hit on {} with axe slot {}; no mace for the smash", k(var3), var5 + 1);
            }
         }
      }
   }

   private void B(Minecraft var1) {
      LocalPlayer var2 = var1.player;
      LivingEntity var3 = this.T;
      if (var2 != null && var2.tickCount == this.a + 1) {
         this.T = null;
         EntityHitResult var4 = w7.q(var1);
         if (var4 != null && var4.getEntity() == var3 && MaceItem.canSmashAttack(var2) && !var2.onGround()) {
            int var5 = this.Y(var2, var3);
            if (var5 >= 0 && this.A.e(var2, var5)) {
               boolean var6 = _m.y(this, var3, () -> {
                  var1.gameMode.attack(var2, var3);
                  _8.f(var2, InteractionHand.MAIN_HAND);
               });
               if (var6) {
                  this.p(var2, var3);
                  w7.B(var1);
                  mt.K("Mace", "Stun Slam smash on {} one tick after the axe: fall {}", k(var3), z(var2.fallDistance));
               } else {
                  mt.K("Mace", "Next Tick smash on {} refused: this tick's attack belongs to another owner", k(var3));
               }
            } else {
               this.A.W(var1, this.v.P());
            }
         } else {
            mt.K(
               "Mace",
               "Next Tick smash on {} skipped: {}",
               k(var3),
               var2.onGround() ? "landed" : (!MaceItem.canSmashAttack(var2) ? "fall ended" : "facing left the target")
            );
            this.A.W(var1, this.v.P());
         }
      } else {
         if (var2 == null || var2.tickCount > this.a + 1) {
            this.T = null;
            this.A.W(var1, this.v.P());
         }
      }
   }

   private void p(LocalPlayer var1, LivingEntity var2) {
      this.H = this.g = (boolean)1;
      this.p = var2;
      this.x = var1.tickCount;
      this.U = var2.hurtTime;
      this.Z = var1.getDeltaMovement().y;
      this.P = "Waiting for the server";
   }

   private void w(LocalPlayer var1) {
      if (this.g && this.p != null) {
         if (eq.l(this.Z, var1.getDeltaMovement().y, this.U, this.p.hurtTime)) {
            this.g = (boolean)0;
            mt.K("Mace", "Smash confirmed on {} after {} ticks", k(this.p), var1.tickCount - this.x);
         } else if (var1.tickCount - this.x >= eq.K(this.k(var1))) {
            this.g = (boolean)0;
            if (this.Q < 1 && !var1.onGround() && MaceItem.canSmashAttack(var1)) {
               this.Q++;
               this.H = (boolean)0;
               mt.K("Mace", "No server confirmation for the smash on {} after {} ticks; retrying once this fall", k(this.p), var1.tickCount - this.x);
            } else {
               mt.K("Mace", "No server confirmation for the smash on {}; not retrying ({})", k(this.p), this.Q >= 1 ? "already retried" : "fall ended");
            }
         }
      }
   }

   private int k(LocalPlayer var1) {
      ClientPacketListener var2 = getMc().getConnection();
      PlayerInfo var3 = var2 == null ? null : var2.getPlayerInfo(var1.getUUID());
      return var3 == null ? 0 : Math.round(var3.getLatency() / 50.0F);
   }

   private LivingEntity F(Minecraft var1) {
      LocalPlayer var2 = var1.player;
      if (var2 != null && var1.level != null && (!this.H || this.g) && !var2.onGround() && !(var2.getDeltaMovement().y >= 0.0) && tq.y(tq.y.e)) {
         if (this.I != null && this.d(var2, this.I, this.H(var2, this.I))) {
            return this.I;
         }

         Vec3 var3 = var2.getEyePosition();
         double var4 = 16.0;
         LivingEntity var6 = null;
         double var7 = Double.MAX_VALUE;

         for (Entity var10 : var1.level.getEntities(var2, var2.getBoundingBox().inflate(Math.max(this.i.P(), var4)), var0 -> var0 instanceof LivingEntity)) {
            LivingEntity var11 = (LivingEntity)var10;
            double var12 = p1.X(var3, var11.getBoundingBox());
            if (var12 < var7 && this.d(var2, var11, this.H(var2, var11))) {
               var6 = var11;
               var7 = var12;
            }
         }

         return var6;
      } else {
         return null;
      }
   }

   private double H(LocalPlayer var1, LivingEntity var2) {
      d5 var3 = new d5(var1.getYRot(), var1.getXRot());
      int var4 = this.R().ticksToReach(var3, dj.J(o(var1, var2)));
      return eq.N(this.i.P(), this.e.P(), -var1.getDeltaMovement().y, var4, var1.entityInteractionRange());
   }

   public double E(LocalPlayer var1) {
      if (!this.isEnabled()) {
         return 0.0;
      } else if (this.isRuntimeActive() && this.V.P() && var1 != null && !this.S.S("Off") && (!this.S.S("In Air") || !var1.onGround())) {
         d5 var2 = new d5(var1.getYRot(), var1.getXRot());
         int var3 = this.R().ticksToReach(var2, new d5(var1.getYRot(), 90.0F));
         return eq.N(this.i.P(), this.e.P(), -var1.getDeltaMovement().y, var3, var1.entityInteractionRange());
      } else {
         return 0.0;
      }
   }

   public LivingEntity T() {
      if (!this.isEnabled()) {
         return null;
      } else {
         return this.C.P() && this.k() ? this.I : null;
      }
   }

   private boolean d(LocalPlayer var1, LivingEntity var2, double var3) {
      Vec3 var5 = var1.getEyePosition();
      return w7.E(var1, var2)
         && (var2 instanceof Player || this.M.P())
         && p1.X(var5, var2.getBoundingBox()) <= var3
         && this.E(var1, var2)
         && w7.Z(var1, var5, o(var1, var2));
   }

   private static Vec3 o(LocalPlayer var0, LivingEntity var1) {
      AABB var2 = var1.getBoundingBox();
      Vec3 var3 = var0.getEyePosition();
      AABB var4 = var2.deflate(Math.min(0.1, var2.getXsize() * 0.25), Math.min(0.1, var2.getYsize() * 0.25), Math.min(0.1, var2.getZsize() * 0.25));
      Vec3 var5 = new Vec3(Math.clamp(var3.x, var4.minX, var4.maxX), Math.clamp(var3.y, var4.minY, var4.maxY), Math.clamp(var3.z, var4.minZ, var4.maxZ));
      Vec3 var6 = new Vec3(var2.getCenter().x, var2.minY + var2.getYsize() * 0.6, var2.getCenter().z);
      return _m.z(var1, var0.entityInteractionRange(), var6, xm.Z(var2, var5)).orElse(var6);
   }

   private t R() {
      return switch ((String)this.L.P()) {
         case "FunTime" -> this.W;
         case "Polar" -> this.B;
         case "Matrix" -> this.O;
         case "Legit" -> this.d;
         default -> this.c;
      };
   }

   private void u(Minecraft var1, LivingEntity var2) {
      LocalPlayer var3 = var1.player;
      if (!_m.f(this)) {
         this.j();
      } else {
         t var4 = this.R();
         if (var4 != this.t) {
            if (this.t != null) {
               this.t.reset();
            }

            this.t = var4;
         }

         AABB var5 = var2.getBoundingBox();
         if (var2 != this.I) {
            this.t.targetChanged(var2);
            mt.K(
               "Mace",
               "Silent Aim target {} at {} blocks, falling {} blocks/tick, rotation={}",
               k(var2),
               z(p1.X(var3.getEyePosition(), var5)),
               z(-var3.getDeltaMovement().y),
               this.L.P()
            );
         }

         this.I = var2;
         tq.i(tq.y.e);
         if (tq.l() != tq.y.e) {
            this.I = null;
         } else {
            d5 var6 = new d5(var3.getYRot(), var3.getXRot());
            double var7 = var3.entityInteractionRange();
            int var9 = p1.X(var3.getEyePosition(), var5) <= var7 ? 1 : 0;
            q12d31c89547aa6a5 var10 = (q12d31c89547aa6a5)var3;
            boolean var11 = k.I(var3, var5.inflate(var2.getPickRadius()), var10.larpYRotLast(), var10.larpXRotLast(), var7 + 1.0, false);
            d5 var12 = this.t.rotate(var6, dj.J(o(var3, var2)), var2, (boolean)var9, var11);
            float[] var13 = hq.A(var6.M, var6.G, var12.M, var12.G, this.m.P());
            var12 = new d5(var13[0], var13[1]);
            var12 = this.t == this.d ? this.d.J(var6, var12) : dj.W(var6, var12);
            m9.k(this, m9.s.X, var12.M, var12.G, 1.0);
            if (!m9.F(m9.s.X)) {
               mt.U("Mace", "Silent Aim yielded to a higher rotation owner");
               this.j();
            } else {
               if (!this.w) {
                  this.P = "Silent aim";
               }
            }
         }
      }
   }

   private void j() {
      if (this.I != null && this.t != null) {
         this.t.reset();
      }

      this.I = null;
      tq.c(tq.y.e, this.f, this.y.P(), true);
   }

   public float R(float var1, float var2) {
      if (this.isEnabled()) {
         return this.k() ? AuraModule.f(var1, var2, 35.0F, 0.25F) : var1;
      } else {
         return var1;
      }
   }

   public boolean k() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.V.P() && this.I != null && this.I.isAlive() && !this.I.isRemoved() && _m.B(this);
   }

   public void M() {
      if (this.isEnabled()) {
         this.H = (boolean)1;
         this.P = "Smash used this fall";
      }
   }

   public boolean E(LocalPlayer var1, LivingEntity var2) {
      if (!this.isEnabled()) {
         return false;
      }

      boolean var3 = var1.getMainHandItem().is(Items.MACE);
      int var4 = !var3
            && this.l.P()
            && this.isRuntimeActive()
            && !this.H
            && MaceItem.canSmashAttack(var1)
            && var1.fallDistance >= this.R.P()
            && this.j(var1, var2, true) >= 0
         ? 1
         : 0;
      return eq.n(this.isRuntimeActive(), this.H, MaceItem.canSmashAttack(var1), var1.fallDistance, this.R.P(), var3, this.l.P(), (boolean)var4);
   }

   public boolean G(LocalPlayer var1, LivingEntity var2) {
      return this.isEnabled()
         ? eq.h(Y(var1, var2), this.u.S("Delayed"), w7.B(var1), var1.getAttackStrengthScale(1.5F), var2.hurtTime, var1.isSprinting())
         : false;
   }

   private static boolean Y(LocalPlayer var0, LivingEntity var1) {
      Vec3 var2 = var0.getDeltaMovement();
      return eq.r(
         var0.level().noCollision(var0, var0.getBoundingBox().move(var2)),
         var0.entityInteractionRange(),
         var0.getEyePosition(),
         var2,
         var1.getBoundingBox(),
         var1.position().subtract(var1.xo, var1.yo, var1.zo),
         var1.getPickRadius(),
         var0.getViewVector(1.0F),
         (var1x, var2x) -> w7.Z(var0, var1x, var2x)
      );
   }

   public static boolean f() {
      if (r != null && r.isEnabled()) {
         MaceModule var0 = r;
         if (var0 != null && var0.isRuntimeActive() && var0.x()) {
            Minecraft var1 = getMc();
            if (var1.player == null) {
               return false;
            } else {
               return tq.l() == tq.y.e
                  ? true
                  : var1.player == var0.h
                     && var1.level == var0.E
                     && var1.gui.screen() == null
                     && var1.player.tickCount >= var0.D
                     && (long)var1.player.tickCount - var0.D <= 1L;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static Input N(LocalPlayer var0, Input var1) {
      if (r != null && r.isEnabled()) {
         MaceModule var2 = r;
         if (var2 != null && var2.isRuntimeActive() && var0 != null && var0 == getMc().player && tq.l() == tq.y.e) {
            if (var0.isSprinting()) {
               var0.setSprinting(false);
               mt.U("Mace", "Silent Aim lock released sprint");
            }

            return w7.E(var1);
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void l() {
      this.H = this.g = (boolean)0;
      this.w = (boolean)0;
      this.p = null;
      this.Q = 0;
      this.T = null;
      this.q = "";
      this.h = null;
      this.E = null;
      this.D = Integer.MIN_VALUE;
      this.P = "Waiting";
   }

   private static String k(Entity var0) {
      return var0 == null ? "?" : var0.getName().getString();
   }

   private static String z(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static dh D(String var0, final String var1, String var2, boolean var3) {
      return new dh(var0, var2, var3) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }

   private static d9 x(String var0, final String var1, String var2, double var3, double var5, double var7, double var9) {
      return new d9(var0, var2, var3, var5, var7, var9) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }

   private static d6 y(String var0, final String var1, String var2, String var3, String... var4) {
      return new d6(var0, var2, var3, var4) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }
}

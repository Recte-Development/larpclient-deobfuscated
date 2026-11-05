package com.larpclient.module;

import com.larp.obf.q5682c6a8aca9b6ec.qac882a24641eea19;
import com.larpclient.d8;
import com.larpclient.d_;
import com.larpclient.e9;
import com.larpclient.ep;
import com.larpclient.hp;
import com.larpclient.j5;
import com.larpclient.l0;
import com.larpclient.m9;
import com.larpclient.o_;
import com.larpclient.oz;
import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.ru;
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
   private static MaceModule z;
   private final ln W = this.addSetting(I("auto-swap", "Auto Swap", "Select a mace immediately before a smash hit", true));
   private final lp N = this.addSetting(I("pick", "Pick", "Prefer damage or a Wind Burst mace for smash hits", "Damage", "Damage", "Wind Burst"));
   private final ln l = this.addSetting(
      I(
         "breach-swap",
         "Breach Swap",
         "Use a Breach mace for ordinary hits when expected damage improves by 15%. Skipped on Paper servers, which reset the charge on a swap",
         false
      )
   );
   private final ln K = this.addSetting(I("swap-back", "Swap Back", "Return to the previous hotbar slot after the attack", true));
   private final ln V = this.addSetting(I("smash-assist", "Smash Assist", "Attack the enemy under the crosshair during a qualifying fall", false));
   private final lp O = this.addSetting(
      I("smash-timing", "Smash Timing", "Delay while the next movement step can still reach the target", "Delayed", "Direct", "Delayed")
   );
   private final lc f = this.addSetting(new lc("min-fall", "Minimum fall distance for automatic smash hits", 3.0, 1.5, 30.0, 0.5) {
      @Override
      public String getDisplayName() {
         return "Min Fall";
      }
   });
   private final ln n = this.addSetting(I("mobs", "Mobs", "Allow smash assistance against non-player enemies", false));
   private final ln D = this.addSetting(
      I("silent-aim", "Silent Aim", "Turn toward a nearby enemy and smash it during a fall while your view stays free", false)
   );
   private final lc S = this.addSetting(V("aim-range", "Aim Range", "Distance at which Silent Aim starts turning toward an enemy", 6.0, 3.0, 10.0, 0.5));
   private final lc t = this.addSetting(V("turn-speed", "Turn Speed", "Maximum Silent Aim turn in degrees per tick", 60.0, 10.0, 180.0, 5.0));
   private final lp e = this.addSetting(
      I(
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
   private final ln y = this.addSetting(
      I("lead-turn", "Lead Turn", "In a fast fall, start turning early enough that the turn finishes before the target is in reach", true)
   );
   private final lp Y = this.addSetting(
      I(
         "stun-slam",
         "Stun Slam",
         "When a smash target raises a shield, hit it with your best axe to disable the shield, then smash. Same Tick sends both hits at once; Next Tick smashes a tick later, which Grim's experimental packet order check does not flag",
         "Same Tick",
         "Same Tick",
         "Next Tick",
         "Off"
      )
   );
   private final lp w = this.addSetting(
      I(
         "range-ring",
         "Range Ring",
         "Draw Silent Aim's reach on the ground below you: an enemy standing inside the ring is in range. In Air shows it only while you are off the ground",
         "In Air",
         "In Air",
         "Always",
         "Off"
      )
   );
   private final ln F = this.addSetting(
      I(
         "target-mark",
         "Target Mark",
         "Outline the enemy Silent Aim is turning toward and mark where your real facing points; green once that facing is on the enemy",
         true
      )
   );
   private final lc j = this.addSetting(
      V(
         "return-speed",
         "Return Speed",
         "Most the player turns per tick back to your look after Silent Aim lets go, like a hand. 180 returns at once",
         35.0,
         10.0,
         180.0,
         5.0
      )
   );
   private final l0 B = new l0();
   private final o_ b = new o_(this);
   private final l5 i = new l5();
   private final l8 T = new l8();
   private final lb h = new lb();
   private final lu r = new lu();
   private final lt p = new lt();
   private lg Z;
   private boolean R;
   private boolean q;
   private LocalPlayer d;
   private Object I;
   private int L = Integer.MIN_VALUE;
   private LivingEntity o;
   private String A = "Waiting";
   private LivingEntity E;
   private int G;
   private int a;
   private int H;
   private double P;
   private boolean m;
   private LivingEntity c;
   private int Q = Integer.MIN_VALUE;
   private boolean u;
   private String x = "";
   private String s = "";

   public MaceModule() {
      super("Mace", "Select a mace for smash hits, with optional smash assistance and Silent Aim", Category.COMBAT, -1);
      z = this;
      this.N.x(this.W::getValue);
      this.l.x(this.W::getValue);
      this.K.x(() -> this.W.getValue() || this.h());
      this.O.x(this::h);
      this.f.x(this::h);
      this.n.x(this::h);
      this.S.x(this.D::getValue);
      this.y.x(this.D::getValue);
      this.t.x(this.D::getValue);
      this.e.x(this.D::getValue);
      this.w.x(this.D::getValue);
      this.F.x(this.D::getValue);
      this.j.x(this.D::getValue);
      this.D.W(var1 -> this.c());
   }

   public static MaceModule Q() {
      return z;
   }

   public l0 W() {
      return this.B;
   }

   public double z() {
      return this.j.getValue();
   }

   @Override
   public String getDisplayName() {
      return "Auto Mace";
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.w;
   }

   private boolean h() {
      return this.V.getValue() || this.D.getValue();
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a);
   }

   @Override
   public String getStatus() {
      return this.A;
   }

   @Override
   public void onEnable() {
      this.z();
      r4.V(
         "Mace",
         "Active; autoSwap={}, smashAssist={}, silentAim={}, timing={}, minFall={}, stunSlam={}, leadTurn={}, rangeRing={}, targetMark={}, returnSpeed={}",
         this.W.getValue(),
         this.V.getValue(),
         this.D.getValue(),
         this.O.getValue(),
         this.f.getValue(),
         this.Y.getValue(),
         this.y.getValue(),
         this.w.getValue(),
         this.F.getValue(),
         this.j.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.z();
      this.c();
      this.b.m(getMc(), this.K.getValue());
      r4.g("Mace", "Disabled or context suspended; hotbar, view and sprint ownership released");
   }

   public static void Q() {
      if (z != null && z.isEnabled()) {
         MaceModule var0 = z;
         if (var0 != null && var0.isRuntimeActive()) {
            Minecraft var1 = getMc();
            var0.d = null;
            var0.I = null;
            var0.L = Integer.MIN_VALUE;
            if (var1.player != null && var1.player.onGround()) {
               var0.w();
            }

            if (var0.c == null) {
               var0.b.C(var1, var0.K.getValue());
            }
         }
      }
   }

   private void w() {
      if (this.m) {
         r4.V("Mace", "Landed before the server confirmed the smash on {}", K(this.E));
      }

      this.R = this.m = (boolean)0;
      this.E = null;
      this.H = 0;
      this.c = null;
      this.x = "";
   }

   public void s(LocalPlayer var1, Entity var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var1 == getMc().player && var2 instanceof LivingEntity var3) {
            boolean var4 = MaceItem.canSmashAttack(var1);
            if (this.Y(var1, var2)) {
               int var7 = j5.X(var1);
               if (this.b.s(var1, var7)) {
                  j5.h(var3);
                  this.c = var3;
                  this.Q = var1.tickCount;
                  this.u = this.Y.i("Same Tick");
                  this.A = "Stun Slam";
                  r4.V("Mace", "Stun Slam on {}: axe slot {} for your hit, smash {}", K(var3), var7 + 1, this.u ? "in the same tick" : "next tick");
               }
            } else if (this.W.getValue() && (!j5.X(var3, var1) || j5.F(var3))) {
               if (var4 || this.l.getValue() && !this.N("Breach Swap")) {
                  int var5 = this.r(var1, var3, var4);
                  if (var5 >= 0 && m9.b(var4, e(var1, var1.getInventory().getItem(var5), var3), e(var1, var1.getMainHandItem(), var3))) {
                     int var6 = var1.getInventory().getSelectedSlot();
                     if (this.b.s(var1, var5) && var6 != var5) {
                        r4.g("Mace", "Selected hotbar slot {} for {} hit", var5 + 1, var4 ? "smash" : "Breach");
                     }

                     if (var4) {
                        this.Z(var1, var3);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean N(String var1) {
      if (!oz.w()) {
         return false;
      }

      if (!var1.equals(this.s)) {
         r4.V("Mace", "{} skipped: {} resets the charge on a swap", var1, oz.r());
      }

      this.s = var1;
      return true;
   }

   public boolean Y(LocalPlayer var1, Entity var2) {
      if (!this.isEnabled()) {
         return false;
      } else if (this.isRuntimeActive()
         && !this.Y.i("Off")
         && var1 == getMc().player
         && var2 instanceof Player var3
         && !d8.E()
         && MaceItem.canSmashAttack(var1)) {
         m9.a var4 = m9.y(this.Y.getValue(), j5.X(var3, var1), j5.F(var3), j5.X(var1) >= 0, this.X(var1, var3) >= 0);
         return var4 == m9.a.j || var4 == m9.a.G;
      } else {
         return false;
      }
   }

   public void Q(LocalPlayer var1, Entity var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var2 == this.c && this.u && this.Q == var1.tickCount) {
            LivingEntity var3 = this.c;
            this.c = null;
            this.q(getMc(), var1, var3, "Stun Slam smash after your axe hit");
         }
      }
   }

   private boolean q(Minecraft var1, LocalPlayer var2, LivingEntity var3, String var4) {
      int var5 = this.X(var2, var3);
      if (var5 >= 0 && this.b.s(var2, var5)) {
         boolean var6 = d8.j(this, var3, () -> {
            var1.gameMode.attack(var2, var3);
            hp.o(var2, InteractionHand.MAIN_HAND);
         });
         if (var6) {
            this.Z(var2, var3);
            r4.V("Mace", "{}: fall {}, mace slot {}", var4, Z(var2.fallDistance), var5 + 1);
         }

         return var6;
      } else {
         r4.V("Mace", "{} skipped: no usable mace", var4);
         return false;
      }
   }

   private int X(LocalPlayer var1, LivingEntity var2) {
      if (var1.getMainHandItem().is(Items.MACE) && !var1.getMainHandItem().nextDamageWillBreak()) {
         return var1.getInventory().getSelectedSlot();
      } else {
         return this.W.getValue() ? this.r(var1, var2, true) : -1;
      }
   }

   private int r(LocalPlayer var1, LivingEntity var2, boolean var3) {
      m9.e[] var4 = new m9.e[Inventory.getSelectionSize()];

      for (int var5 = 0; var5 < var4.length; var5++) {
         ItemStack var6 = var1.getInventory().getItem(var5);
         if (var6.is(Items.MACE) && !var6.nextDamageWillBreak()) {
            int var7 = S(var6, Enchantments.BREACH);
            if (var3 || var7 != 0) {
               var4[var5] = new m9.e(var5, true, false, var7, S(var6, Enchantments.WIND_BURST), e(var1, var6, var2));
            }
         }
      }

      return m9.q(var4, var3, this.N.i("Wind Burst"));
   }

   private static float e(LocalPlayer var0, ItemStack var1, LivingEntity var2) {
      return m9.x(
         j5.R(var0.getMainHandItem()),
         j5.G(var0),
         var0.fallDistance,
         var1.is(Items.MACE),
         MaceItem.canSmashAttack(var0),
         S(var1, Enchantments.DENSITY),
         j5.m(var0, var2),
         S(var1, Enchantments.SHARPNESS),
         var2.getArmorValue(),
         (float)var2.getAttributeValue(Attributes.ARMOR_TOUGHNESS),
         S(var1, Enchantments.BREACH)
      );
   }

   private static int S(ItemStack var0, ResourceKey<Enchantment> var1) {
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
         this.C(var2.player);
      }

      this.A = this.R ? (this.m ? "Waiting for the server" : "Smash used this fall") : "Waiting";
      if (this.c != null && !this.u) {
         this.s(var2);
      } else {
         LivingEntity var19;
         label120: {
            boolean var10 = false;
            byte var9 = this.u;
            int var10001 = Integer.signum(var9);
            if (((var10001 | -var10001) >>> 31 ^ 1) == 0 && this.c != null && var2.player != null) {
               int var12 = var2.player.tickCount;
               int var11 = this.Q;
               int var18 = Integer.compare(var11, var12);
               if ((var18 | -var18) >>> 31 == 0) {
                  var19 = this.c;
                  break label120;
               }
            }

            var19 = null;
         }

         this.c = var19;
         if (!this.h()) {
            this.c();
         } else {
            LivingEntity var3 = this.D.getValue() ? this.A(var2) : null;
            if (var3 == null) {
               this.c();
            }

            if (j5.o(var2, rr.f.T) && o_.N(this)) {
               LocalPlayer var4 = var2.player;
               EntityHitResult var5 = j5.R(var2);
               LivingEntity var6 = this.K(var4, var5, var3);
               int var7 = var6 == null && var3 == null ? 0 : 1;
               int var10000 = j5.n(var4);
               boolean var20 = j5.V(var2);
               boolean var10002 = j5.D(var2);
               int var10003 = var3 == null && ep.f() ? 1 : 0;
               boolean var10004 = j5.E(var2, var4);
               int var10005 = var4.getDeltaMovement().y < 0.0 ? 1 : 0;
               boolean var10006 = MaceItem.canSmashAttack(var4);
               double var10007 = var4.fallDistance;
               double var10008 = this.f.getValue();
               boolean var16 = false;
               byte var15 = this.R;
               int var10009 = Integer.signum(var15);
               String var8 = m9.K(
                  (boolean)var10000,
                  var20,
                  var10002,
                  (boolean)var10003,
                  var10004,
                  (boolean)var10005,
                  var10006,
                  var10007,
                  var10008,
                  ((var10009 | -var10009) >>> 31 ^ 1) == 0 && !this.m,
                  this.m,
                  var6 != null && this.X(var4, var6) >= 0,
                  var6 != null
               );
               if (var8 == null) {
                  this.G(var2, var4, var5, var6, var3, var1);
               } else if (var7 != 0) {
                  this.c(var8, var4, var3);
               }

               label83:
               if (var3 != null) {
                  if (this.R) {
                     boolean var14 = false;
                     byte var13 = this.m;
                     var10000 = Integer.signum(var13);
                     if ((var10000 | -var10000) >>> 31 == 0) {
                        break label83;
                     }
                  }

                  if (!j5.n(var4)) {
                     this.T(var2, var3);
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

   private LivingEntity K(LocalPlayer var1, EntityHitResult var2, LivingEntity var3) {
      if (!(var2 != null && var2.getEntity() instanceof LivingEntity var4)) {
         return null;
      } else if (!this.V.getValue() && var4 != var3) {
         return null;
      } else {
         return j5.p(var1, var4) && (var4 instanceof Player || this.n.getValue()) ? var4 : null;
      }
   }

   private void c(String var1, LocalPlayer var2, LivingEntity var3) {
      if (!var1.equals(this.x)) {
         this.x = var1;
         r4.V(
            "Mace",
            "Smash held: {}; fall {}, speed {}{}",
            var1,
            Z(var2.fallDistance),
            Z(var2.getDeltaMovement().y),
            var3 == null ? "" : ", Silent Aim target " + K(var3) + " at " + Z(d_.T(var2.getEyePosition(), var3.getBoundingBox()))
         );
      }
   }

   private void G(Minecraft var1, LocalPlayer var2, EntityHitResult var3, LivingEntity var4, LivingEntity var5, boolean var6) {
      this.x = "";
      m9.a var7 = m9.y(this.Y.getValue(), j5.X(var4, var2), j5.F(var4), j5.X(var2) >= 0, this.X(var2, var4) >= 0);
      if ((var7 != m9.a.G || !P(var2, var4)) && var7 != m9.a.t) {
         if (var7 == m9.a.c && j5.X(var4, var2) && !j5.F(var4)) {
            this.c("raised shield and no axe in the hotbar", var2, var5);
         } else if (this.o(var2, var4)) {
            this.q = (boolean)1;
            this.d = var2;
            this.I = var1.level;
            this.L = var2.tickCount;
            var2.setSprinting(false);
            j5.i(var1, Math.max(j5.w(var1), 1));
            this.A = "Delaying smash";
            if (!var6) {
               r4.g("Mace", "Delaying smash while the predicted next step remains in reach");
            }
         } else if (var7 != m9.a.c) {
            this.n(var1, var2, var4, var7);
         } else {
            int var8 = this.X(var2, var4);
            if (var8 == var2.getInventory().getSelectedSlot() || this.b.s(var2, var8)) {
               if (j5.u(var1, var3)) {
                  this.Z(var2, var4);
                  j5.s(var1);
                  r4.V("Mace", "Smash sent on {}: fall {}, speed {}, silent={}", K(var4), Z(var2.fallDistance), Z(var2.getDeltaMovement().y), var4 == var5);
               } else {
                  r4.V("Mace", "Smash attack refused by the client gate on {}", K(var4));
               }
            }
         }
      } else {
         this.n(var1, var2, var4, var7);
      }
   }

   private void n(Minecraft var1, LocalPlayer var2, LivingEntity var3, m9.a var4) {
      int var5 = j5.X(var2);
      int var6 = this.X(var2, var3);
      if (this.b.s(var2, var5)) {
         int var7 = var4 == m9.a.j ? 1 : 0;
         boolean var8 = d8.z(this, var3, () -> {
            var1.gameMode.attack(var2, var3);
            hp.o(var2, InteractionHand.MAIN_HAND);
            j5.h(var3);
            if (var7 && this.b.s(var2, var6)) {
               var1.gameMode.attack(var2, var3);
               hp.o(var2, InteractionHand.MAIN_HAND);
            }
         });
         if (!var8) {
            this.b.m(var1, true);
            r4.V("Mace", "Stun Slam refused: this tick's attack belongs to another owner");
         } else {
            j5.s(var1);
            if (var7 != 0) {
               this.Z(var2, var3);
               this.A = "Stun Slam";
               r4.V("Mace", "Stun Slam on {}: axe slot {} then mace slot {} in one tick, fall {}", K(var3), var5 + 1, var6 + 1, Z(var2.fallDistance));
            } else if (var4 == m9.a.G) {
               this.c = var3;
               this.Q = var2.tickCount;
               this.u = (boolean)0;
               this.A = "Stun Slam";
               r4.V("Mace", "Stun Slam on {}: axe slot {} now, smash next tick", K(var3), var5 + 1);
            } else {
               this.A = "Shield break hit";
               r4.V("Mace", "Shield break hit on {} with axe slot {}; no mace for the smash", K(var3), var5 + 1);
            }
         }
      }
   }

   private void s(Minecraft var1) {
      LocalPlayer var2 = var1.player;
      LivingEntity var3 = this.c;
      if (var2 != null && var2.tickCount == this.Q + 1) {
         this.c = null;
         EntityHitResult var4 = j5.R(var1);
         if (var4 != null && var4.getEntity() == var3 && MaceItem.canSmashAttack(var2) && !var2.onGround()) {
            int var5 = this.X(var2, var3);
            if (var5 >= 0 && this.b.s(var2, var5)) {
               boolean var6 = d8.z(this, var3, () -> {
                  var1.gameMode.attack(var2, var3);
                  hp.o(var2, InteractionHand.MAIN_HAND);
               });
               if (var6) {
                  this.Z(var2, var3);
                  j5.s(var1);
                  r4.V("Mace", "Stun Slam smash on {} one tick after the axe: fall {}", K(var3), Z(var2.fallDistance));
               } else {
                  r4.V("Mace", "Next Tick smash on {} refused: this tick's attack belongs to another owner", K(var3));
               }
            } else {
               this.b.m(var1, this.K.getValue());
            }
         } else {
            r4.V(
               "Mace",
               "Next Tick smash on {} skipped: {}",
               K(var3),
               var2.onGround() ? "landed" : (!MaceItem.canSmashAttack(var2) ? "fall ended" : "facing left the target")
            );
            this.b.m(var1, this.K.getValue());
         }
      } else {
         if (var2 == null || var2.tickCount > this.Q + 1) {
            this.c = null;
            this.b.m(var1, this.K.getValue());
         }
      }
   }

   private void Z(LocalPlayer var1, LivingEntity var2) {
      this.R = this.m = (boolean)1;
      this.E = var2;
      this.G = var1.tickCount;
      this.a = var2.hurtTime;
      this.P = var1.getDeltaMovement().y;
      this.A = "Waiting for the server";
   }

   private void C(LocalPlayer var1) {
      if (this.m && this.E != null) {
         if (m9.q(this.P, var1.getDeltaMovement().y, this.a, this.E.hurtTime)) {
            this.m = (boolean)0;
            r4.V("Mace", "Smash confirmed on {} after {} ticks", K(this.E), var1.tickCount - this.G);
         } else if (var1.tickCount - this.G >= m9.K(this.L(var1))) {
            this.m = (boolean)0;
            if (this.H < 1 && !var1.onGround() && MaceItem.canSmashAttack(var1)) {
               this.H++;
               this.R = (boolean)0;
               r4.V("Mace", "No server confirmation for the smash on {} after {} ticks; retrying once this fall", K(this.E), var1.tickCount - this.G);
            } else {
               r4.V("Mace", "No server confirmation for the smash on {}; not retrying ({})", K(this.E), this.H >= 1 ? "already retried" : "fall ended");
            }
         }
      }
   }

   private int L(LocalPlayer var1) {
      ClientPacketListener var2 = getMc().getConnection();
      PlayerInfo var3 = var2 == null ? null : var2.getPlayerInfo(var1.getUUID());
      return var3 == null ? 0 : Math.round(var3.getLatency() / 50.0F);
   }

   private LivingEntity A(Minecraft var1) {
      LocalPlayer var2 = var1.player;
      if (var2 != null && var1.level != null && (!this.R || this.m) && !var2.onGround() && !(var2.getDeltaMovement().y >= 0.0) && rr.j(rr.f.T)) {
         if (this.o != null && this.m(var2, this.o, this.y(var2, this.o))) {
            return this.o;
         }

         Vec3 var3 = var2.getEyePosition();
         double var4 = 16.0;
         LivingEntity var6 = null;
         double var7 = Double.MAX_VALUE;

         for (Entity var10 : var1.level
            .getEntities(var2, var2.getBoundingBox().inflate(Math.max(this.S.getValue(), var4)), var0 -> var0 instanceof LivingEntity)) {
            LivingEntity var11 = (LivingEntity)var10;
            double var12 = d_.T(var3, var11.getBoundingBox());
            if (var12 < var7 && this.m(var2, var11, this.y(var2, var11))) {
               var6 = var11;
               var7 = var12;
            }
         }

         return var6;
      } else {
         return null;
      }
   }

   private double y(LocalPlayer var1, LivingEntity var2) {
      lx var3 = new lx(var1.getYRot(), var1.getXRot());
      int var4 = this.V().ticksToReach(var3, le.p(u(var1, var2)));
      return m9.T(this.S.getValue(), this.y.getValue(), -var1.getDeltaMovement().y, var4, var1.entityInteractionRange());
   }

   public double S(LocalPlayer var1) {
      if (!this.isEnabled()) {
         return 0.0;
      } else if (this.isRuntimeActive() && this.D.getValue() && var1 != null && !this.w.i("Off") && (!this.w.i("In Air") || !var1.onGround())) {
         lx var2 = new lx(var1.getYRot(), var1.getXRot());
         int var3 = this.V().ticksToReach(var2, new lx(var1.getYRot(), 90.0F));
         return m9.T(this.S.getValue(), this.y.getValue(), -var1.getDeltaMovement().y, var3, var1.entityInteractionRange());
      } else {
         return 0.0;
      }
   }

   public LivingEntity j() {
      if (!this.isEnabled()) {
         return null;
      } else {
         return this.F.getValue() && this.E() ? this.o : null;
      }
   }

   private boolean m(LocalPlayer var1, LivingEntity var2, double var3) {
      Vec3 var5 = var1.getEyePosition();
      return j5.p(var1, var2)
         && (var2 instanceof Player || this.n.getValue())
         && d_.T(var5, var2.getBoundingBox()) <= var3
         && this.r(var1, var2)
         && j5.D(var1, var5, u(var1, var2));
   }

   private static Vec3 u(LocalPlayer var0, LivingEntity var1) {
      AABB var2 = var1.getBoundingBox();
      Vec3 var3 = var0.getEyePosition();
      AABB var4 = var2.deflate(Math.min(0.1, var2.getXsize() * 0.25), Math.min(0.1, var2.getYsize() * 0.25), Math.min(0.1, var2.getZsize() * 0.25));
      Vec3 var5 = new Vec3(Math.clamp(var3.x, var4.minX, var4.maxX), Math.clamp(var3.y, var4.minY, var4.maxY), Math.clamp(var3.z, var4.minZ, var4.maxZ));
      Vec3 var6 = new Vec3(var2.getCenter().x, var2.minY + var2.getYsize() * 0.6, var2.getCenter().z);
      return d8.u(var1, var0.entityInteractionRange(), var6, e9.X(var2, var5)).orElse(var6);
   }

   private lg V() {
      return switch ((String)this.e.getValue()) {
         case "FunTime" -> this.T;
         case "Polar" -> this.h;
         case "Matrix" -> this.r;
         case "Legit" -> this.p;
         default -> this.i;
      };
   }

   private void T(Minecraft var1, LivingEntity var2) {
      LocalPlayer var3 = var1.player;
      if (!d8.m(this)) {
         this.c();
      } else {
         lg var4 = this.V();
         if (var4 != this.Z) {
            if (this.Z != null) {
               this.Z.reset();
            }

            this.Z = var4;
         }

         AABB var5 = var2.getBoundingBox();
         if (var2 != this.o) {
            this.Z.targetChanged(var2);
            r4.V(
               "Mace",
               "Silent Aim target {} at {} blocks, falling {} blocks/tick, rotation={}",
               K(var2),
               Z(d_.T(var3.getEyePosition(), var5)),
               Z(-var3.getDeltaMovement().y),
               this.e.getValue()
            );
         }

         this.o = var2;
         rr.W(rr.f.T);
         if (rr.u() != rr.f.T) {
            this.o = null;
         } else {
            lx var6 = new lx(var3.getYRot(), var3.getXRot());
            double var7 = var3.entityInteractionRange();
            int var9 = d_.T(var3.getEyePosition(), var5) <= var7 ? 1 : 0;
            qac882a24641eea19 var10 = (qac882a24641eea19)var3;
            boolean var11 = lf.X(var3, var5.inflate(var2.getPickRadius()), var10.larpYRotLast(), var10.larpXRotLast(), var7 + 1.0, false);
            lx var12 = this.Z.rotate(var6, le.p(u(var3, var2)), var2, (boolean)var9, var11);
            float[] var13 = ru.Y(var6.z, var6.I, var12.z, var12.I, this.t.getValue());
            var12 = new lx(var13[0], var13[1]);
            var12 = this.Z == this.p ? this.p.C(var6, var12) : le.c(var6, var12);
            ep.t(this, ep.z.J, var12.z, var12.I, 1.0);
            if (!ep.W(ep.z.J)) {
               r4.g("Mace", "Silent Aim yielded to a higher rotation owner");
               this.c();
            } else {
               if (!this.q) {
                  this.A = "Silent aim";
               }
            }
         }
      }
   }

   private void c() {
      if (this.o != null && this.Z != null) {
         this.Z.reset();
      }

      this.o = null;
      rr.L(rr.f.T, this.B, this.j.getValue(), true);
   }

   public float P(float var1, float var2) {
      if (this.isEnabled()) {
         return this.E() ? AuraModule.B(var1, var2, 35.0F, 0.25F) : var1;
      } else {
         return var1;
      }
   }

   public boolean E() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.D.getValue() && this.o != null && this.o.isAlive() && !this.o.isRemoved() && d8.T(this);
   }

   public void L() {
      if (this.isEnabled()) {
         this.R = (boolean)1;
         this.A = "Smash used this fall";
      }
   }

   public boolean r(LocalPlayer var1, LivingEntity var2) {
      if (!this.isEnabled()) {
         return false;
      }

      boolean var3 = var1.getMainHandItem().is(Items.MACE);
      int var4 = !var3
            && this.W.getValue()
            && this.isRuntimeActive()
            && !this.R
            && MaceItem.canSmashAttack(var1)
            && var1.fallDistance >= this.f.getValue()
            && this.r(var1, var2, true) >= 0
         ? 1
         : 0;
      return m9.G(this.isRuntimeActive(), this.R, MaceItem.canSmashAttack(var1), var1.fallDistance, this.f.getValue(), var3, this.W.getValue(), (boolean)var4);
   }

   public boolean o(LocalPlayer var1, LivingEntity var2) {
      return this.isEnabled()
         ? m9.r(P(var1, var2), this.O.i("Delayed"), j5.G(var1), var1.getAttackStrengthScale(1.5F), var2.hurtTime, var1.isSprinting())
         : false;
   }

   private static boolean P(LocalPlayer var0, LivingEntity var1) {
      Vec3 var2 = var0.getDeltaMovement();
      return m9.H(
         var0.level().noCollision(var0, var0.getBoundingBox().move(var2)),
         var0.entityInteractionRange(),
         var0.getEyePosition(),
         var2,
         var1.getBoundingBox(),
         var1.position().subtract(var1.xo, var1.yo, var1.zo),
         var1.getPickRadius(),
         var0.getViewVector(1.0F),
         (var1x, var2x) -> j5.D(var0, var1x, var2x)
      );
   }

   public static boolean c() {
      if (z != null && z.isEnabled()) {
         MaceModule var0 = z;
         if (var0 != null && var0.isRuntimeActive() && var0.h()) {
            Minecraft var1 = getMc();
            if (var1.player == null) {
               return false;
            } else {
               return rr.u() == rr.f.T
                  ? true
                  : var1.player == var0.d
                     && var1.level == var0.I
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

   public static Input r(LocalPlayer var0, Input var1) {
      if (z != null && z.isEnabled()) {
         MaceModule var2 = z;
         if (var2 != null && var2.isRuntimeActive() && var0 != null && var0 == getMc().player && rr.u() == rr.f.T) {
            if (var0.isSprinting()) {
               var0.setSprinting(false);
               r4.g("Mace", "Silent Aim lock released sprint");
            }

            return j5.g(var1);
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void z() {
      this.R = this.m = (boolean)0;
      this.q = (boolean)0;
      this.E = null;
      this.H = 0;
      this.c = null;
      this.x = "";
      this.d = null;
      this.I = null;
      this.L = Integer.MIN_VALUE;
      this.A = "Waiting";
   }

   private static String K(Entity var0) {
      return var0 == null ? "?" : var0.getName().getString();
   }

   private static String Z(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static ln I(String var0, final String var1, String var2, boolean var3) {
      return new ln(var0, var2, var3) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }

   private static lc V(String var0, final String var1, String var2, double var3, double var5, double var7, double var9) {
      return new lc(var0, var2, var3, var5, var7, var9) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }

   private static lp I(String var0, final String var1, String var2, String var3, String... var4) {
      return new lp(var0, var2, var3, var4) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }
}

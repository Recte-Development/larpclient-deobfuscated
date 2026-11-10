package com.larpclient;

import com.larpclient.module.Module;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.Game;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class ac extends ar implements s1 {
   private static ac M;
   private static boolean IB;
   static final int IT = 2;
   static final int U = 10;
   static final double B = 10.0;
   static final double IP = 110.0;
   static final double ID = 72.0;
   static final double t = 30.0;
   static final int IV = 6;
   static final int LR = 4;
   static final int Lh = 400;
   static final int o = 3600;
   static final double b = 1500.0;
   static final int IL = 200;
   private final com.larpclient.module.g F = this.addSetting(
      new com.larpclient.module.g("Destination", "Where to fly, as X Z (for example 1200 -3400). X Y Z works too; the height is not used", "").F("X Z")
   );
   private final com.larpclient.module.e IO = this.Z(
      "Flight Height",
      "Height to cruise at. Lower on long trips over flat land saves rockets; higher clears mountains and loads fewer chunks",
      290.0,
      180.0,
      320.0,
      10.0
   );
   private final com.larpclient.module.s Ih = this.U(
      "Auto Mending", "Land on the way and throw XP bottles from your hotbar to repair a Mending elytra long before it can break", true
   );
   private final com.larpclient.module.s L = this.U(
      "Log Off On Arrival", "Leave the server after landing, with a trip summary. Also leaves if the trip has to stop. Never in singleplayer", true
   );
   private final com.larpclient.module.s I = this.U("Waypoint Line", "Draw the planned flight, a beacon at the destination and the landing spot", true);
   private final com.larpclient.module.s Is = this.U(
      "Status Card", "Show a card at the top of the screen with distance, time left, rockets and elytra health", true
   );
   private final com.larpclient.module.l IX = this.addSetting(
      new com.larpclient.module.l("Notifications", "How the pilot tells you what it is doing", "Toast", "Toast", "Chat", "Off")
   );
   private final com.larpclient.module.s m = this.U(
      "Auto Buy Rockets", "On DonutSMP, buy Flight Duration 3 rockets from /ah on the ground when you run low, within the price limit and budget", false
   );
   private final com.larpclient.module.e IC = this.Z("Rocket Stacks", "How many stacks of rockets to keep; it buys up to this many", 3.0, 1.0, 3.0, 1.0);
   private final com.larpclient.module.s I5 = this.U(
      "Auto Buy XP Bottles", "On DonutSMP, buy XP bottles from /ah on the ground to mend a Mending elytra, within the price limit and budget", false
   );
   private final com.larpclient.module.e IA = this.Z("XP Bottle Stacks", "How many stacks of XP bottles to keep; it buys up to this many", 2.0, 1.0, 3.0, 1.0);
   private final com.larpclient.module.l P = this.addSetting(
      new com.larpclient.module.l(
         "Price Limit",
         "Most it pays per stack of 64. Cheap: rockets $35K, XP $320K. Normal: rockets $60K, XP $450K. High: rockets $150K, XP $700K",
         "Normal",
         je.g.toArray(String[]::new)
      )
   );
   private final com.larpclient.module.l e = this.addSetting(
      new com.larpclient.module.l("Trip Budget", "Most money it spends on one trip", "$2M", je.d.toArray(String[]::new))
   );
   private final t6 a = new t6(this.getName());
   private final cu Ia = new cu(this).z(true);
   private final gh z = new gh();
   private final gh.r Ip = new gh.r();
   private final x.w IJ = new x.w();
   private final hc T = new hc();
   private final hk R = new hk(this);
   private final Map<Long, Integer> IG = new HashMap<>();
   private final List<Vec3> s = new ArrayList<>();
   private final List<Vec3> I2 = new ArrayList<>();
   private ac.f Ij = ac.f.z;
   private ac.l IZ = ac.l.l;
   private ac.w If = ac.w.m;
   private double u;
   private double IQ;
   private boolean q;
   private Object I3;
   private Object O;
   private Object I1;
   private ResourceKey<Level> Ir;
   private int C;
   private int X;
   private int LT;
   private int H = -1000;
   private int Id = -1000;
   private int d;
   private gh.s I_;
   private gh.y IH;
   private int E = -1000;
   private int Ib;
   private int IK;
   private Vec3 J;
   private boolean IF;
   private boolean i;
   private double Z = Double.MAX_VALUE;
   private int I9;
   private int W = -1000;
   private int IU = -1000;
   private int Iz = 3;
   private int f = -1000;
   private int G = -1000;
   private int Ix;
   private int A;
   private Float Ii;
   private boolean Ik;
   private float Ig;
   private float g;
   private boolean II;
   private boolean Iw;
   private boolean Y;
   private int IN = Integer.MIN_VALUE;
   private int Q = -1;
   private Vec3 Iy;
   private int j;
   private int IE;
   private int Iq;
   private int Ie;
   private int n = -1000;
   private float IR = 20.0F;
   private int Ic = -1000;
   private Vec3 y;
   private Vec3 Io;
   private int w;
   private int I4;
   private double D;
   private double I0;
   private double IS;
   private long I8;
   private String In = "";
   private String It;
   private String IW;
   private long Im;
   private int S;
   private int x;
   private final EnumSet<je.w> h = EnumSet.noneOf(je.w.class);
   private String r = "";
   private int V = -10000;
   private int c;
   private int Iu;
   private final List<je.w> I6 = new ArrayList<>();
   private long IY;
   private int I7;
   private boolean Il;
   static final double Iv = 48.0;
   private int p = Integer.MIN_VALUE;
   private int IM = -1000;

   public ac() {
      super("Auto Elytra Pilot", "Fly to an X Z on an elytra: takeoff, cruise, landing, rockets and mending stops");
      M = this;
      this.F.h("Destination (X Z)");
      this.IC.P(this.m::getValue);
      this.IA.P(this.I5::getValue);
      this.P.P(() -> this.m.getValue() || this.I5.getValue());
      this.e.P(() -> this.m.getValue() || this.I5.getValue());
      if (!IB) {
         IB = (boolean)1;
         ClientReceiveMessageEvents.GAME.register((Game)(var0, var1) -> {
            ac var2 = M;
            if (!var1 && var2 != null && var2.a.G()) {
               t6.g(var0);
            }
         });
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            ac var1 = M;
            if (var1 != null) {
               var1.o();
            }
         });
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> {
            ac var1 = M;
            if (var1 != null) {
               var1.Q();
            }
         });
      }
   }

   public static ac y() {
      return M;
   }

   @Override
   public boolean allowsScreenInteractions() {
      return mc == null || !(c6.D() instanceof AbstractContainerScreen);
   }

   @Override
   public void disable() {
      this.It = null;
      super.disable();
   }

   public static double[] W(String var0) {
      if (var0 == null) {
         return null;
      }

      String[] var1 = var0.trim().replace(',', ' ').split("\\s+");
      if (var1.length != 2 && var1.length != 3) {
         return null;
      }

      double[] var2 = new double[var1.length];

      try {
         for (int var3 = 0; var3 < var1.length; var3++) {
            var2[var3] = Double.parseDouble(var1[var3]);
         }
      } catch (NumberFormatException var7) {
         return null;
      }

      double var8 = var2[0];
      double var5 = var2[var1.length - 1];
      return Double.isFinite(var8) && Double.isFinite(var5) && !(Math.abs(var8) > 2.9999984E7) && !(Math.abs(var5) > 2.9999984E7)
         ? new double[]{var8, var5}
         : null;
   }

   static String V(double var0, double var2) {
      return (long)Math.floor(var0) + " " + (long)Math.floor(var2);
   }

   static String k(double var0) {
      return String.format(Locale.ROOT, "%,d", Math.round(var0));
   }

   @Override
   public List<s1.a> commands() {
      return List.of(
         new s1.a("Set the destination", "Fly to where you look", this::O),
         new s1.a("Set the destination", "Remember where you stand", this::c),
         new s1.a("Set the destination", "Clear destination", this::F),
         new s1.a("Buying on DonutSMP", "Buy rockets and XP now", this::w)
      );
   }

   private String O() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.isRuntimeActive()) {
         return "Turn the pilot off before changing the destination.";
      }

      Vec3 var1 = mc.player.getEyePosition();
      Vec3 var2 = mc.player.getLookAngle();
      double var3 = Math.max(64.0, mc.options.getEffectiveRenderDistance() * 16.0);
      BlockHitResult var5 = mc.level.clip(new ClipContext(var1, var1.add(var2.scale(var3)), Block.OUTLINE, Fluid.ANY, mc.player));
      Vec3 var6;
      String var7;
      if (var5.getType() == Type.BLOCK) {
         var6 = var5.getLocation();
         var7 = "the block you look at";
      } else {
         Vec3 var8 = new Vec3(var2.x, 0.0, var2.z);
         if (var8.lengthSqr() < 1.0E-6) {
            return "Look toward the horizon, not straight up or down.";
         }

         var6 = mc.player.position().add(var8.normalize().scale(1000.0));
         var7 = "1,000 blocks along your view (nothing loaded is in sight there)";
      }

      this.F.k(V(var6.x, var6.z));
      this.Y();
      double var10 = Math.hypot(var6.x - mc.player.getX(), var6.z - mc.player.getZ());
      com.larpclient.h.A(this.getName(), "Destination set from the view to {} ({} blocks)", this.F.getValue(), k(var10));
      return "Destination " + this.F.getValue().replace(' ', ',').replace(",", ", ") + ", " + var7 + ", " + k(var10) + " blocks away.";
   }

   private String c() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.isRuntimeActive()) {
         return "Turn the pilot off before changing the destination.";
      }

      this.F.k(V(mc.player.getX(), mc.player.getZ()));
      this.Y();
      return "Destination set to where you stand: " + this.F.getValue().replace(" ", ", ") + ". Fly away and turn the pilot on to come back.";
   }

   private String w() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.isRuntimeActive()) {
         return "The pilot buys on its own during a trip.";
      }

      if (!this.I6.isEmpty()) {
         return "Already buying; move to stop it.";
      }

      if (!this.m.getValue() && !this.I5.getValue()) {
         return "Turn on Auto Buy Rockets or Auto Buy XP Bottles first.";
      }

      if (mc.player.onGround() && !(mc.player.getDeltaMovement().horizontalDistance() > 0.01)) {
         if (this.m.getValue()) {
            this.I6.add(je.w.W);
         }

         if (this.I5.getValue()) {
            this.I6.add(je.w.A);
         }

         this.IY = 0L;
         this.I7 = 0;
         com.larpclient.h.A(this.getName(), "Buying now: {}", this.I6);
         return "Buying "
            + (this.m.getValue() ? "rockets" : "")
            + (this.I6.size() == 2 ? " and " : "")
            + (this.I5.getValue() ? "XP bottles" : "")
            + " on /ah, at most "
            + this.P.getValue().toLowerCase(Locale.ROOT)
            + " prices and "
            + je.z(je.p(this.e.getValue()))
            + ". Moving stops it.";
      } else {
         return "Stand still on the ground first.";
      }
   }

   private void o() {
      if (!this.I6.isEmpty()) {
         if (!this.isInGame() || this.isRuntimeActive()) {
            this.a.Q("the restock was interrupted");
            this.I6.clear();
         } else if (this.S()) {
            this.a.Q("you moved");
            this.V("Stopped buying: you moved.");
         } else {
            je.w var1 = this.I6.getFirst();
            int var2 = var1 == je.w.W ? 1 : 0;
            if (!this.a.G()) {
               int var9 = var2 != 0 ? cu.p(mc.player) : this.M();
               int var4 = (var2 != 0 ? this.n() : this.x()) - var9;
               long var5 = je.p(this.e.getValue());
               long var7 = var5 < 0L ? -1L : Math.max(0L, var5 - this.IY);
               if (var4 > 0 && var7 != 0L) {
                  this.a.P(var1, var4, je.U(this.P.getValue(), var1), var7);
               } else {
                  this.I6.removeFirst();
                  if (this.I6.isEmpty()) {
                     this.V(null);
                  }
               }
            } else if (this.a.u() != t6.y.b) {
               this.IY = this.IY + this.a.j();
               this.I7 = this.I7 + this.a.r();
               String var3 = var2 != 0 ? "rockets" : "XP bottles";
               if (this.a.r() > 0) {
                  this.P(s0.u.m, "Bought " + this.a.r() + " " + var3 + " for " + je.z(this.a.j()));
               }

               if (this.a.g() == t6.y.g) {
                  this.P(s0.u.p, "Could not buy " + var3 + ": " + this.a.u());
               }

               this.I6.removeFirst();
               if (this.I6.isEmpty() || this.a.o()) {
                  this.V(null);
               }
            }
         }
      }
   }

   private void V(String var1) {
      this.I6.clear();
      String var2 = var1 != null ? var1 : (this.I7 == 0 ? "Bought nothing." : "Done buying: " + this.I7 + " items for " + je.z(this.IY) + ".");
      com.larpclient.h.A(this.getName(), "Buying now ended: {}", var2);
      this.s(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
   }

   private String F() {
      if (this.isRuntimeActive()) {
         return "Turn the pilot off before clearing the destination.";
      }

      this.F.k("");
      this.Y();
      return "Destination cleared.";
   }

   private void Y() {
      LarpClient var1 = LarpClient.R();
      if (var1 != null && var1.o() != null) {
         var1.o().C();
      }
   }

   @Override
   public String summary() {
      double[] var1 = W(this.F.getValue());
      if (var1 == null) {
         return this.F.getValue().isBlank()
            ? "Type a destination as X Z, or press Fly to where you look."
            : "\"" + this.F.getValue() + "\" is not a destination; type it as X Z, for example 1200 -3400.";
      }

      String var2 = "Destination: " + V(var1[0], var1[1]).replace(" ", ", ");
      if (!this.isInGame()) {
         return var2 + ".";
      }

      double var3 = Math.hypot(var1[0] - mc.player.getX(), var1[1] - mc.player.getZ());
      return this.isRuntimeActive() && this.q ? var2 + ". " + this.d() + "." : var2 + ", " + k(var3) + " blocks away. Turn the pilot on to fly there.";
   }

   @Override
   public String detail() {
      if (!this.isInGame()) {
         return "Needs an elytra and firework rockets in your offhand or hotbar.";
      }

      ItemStack var1 = mc.player.getItemBySlot(EquipmentSlot.CHEST);
      String var2 = var1.is(Items.ELYTRA)
         ? "elytra " + hk.T(var1) + " of " + var1.getMaxDamage() + " uses" + (hk.x(var1) ? " (Mending)" : "")
         : (this.O() >= 0 ? "elytra in your hotbar, not worn yet" : "no elytra worn");
      return "Rockets " + cu.p(mc.player) + ", XP bottles " + this.M() + ", " + var2 + ".";
   }

   @Override
   public List<s1.w> settingGroups() {
      return List.of(
         new s1.w("Destination", List.of(this.F)),
         new s1.w("While flying", List.of(this.Ih, this.L)),
         new s1.w("Buying on DonutSMP", List.of(this.m, this.IC, this.I5, this.IA, this.P, this.e)),
         new s1.w("Display", List.of(this.I, this.Is, this.IX)),
         s1.w.advanced("Advanced", List.of(this.IO))
      );
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.g();
      if (this.It != null) {
         String var11 = this.It;
         this.It = null;
         this.w("it stopped earlier because " + var11 + ". Turn it on again to fly from here");
      } else {
         double[] var1 = W(this.F.getValue());
         if (var1 == null) {
            this.w(this.F.getValue().isBlank() ? "type a destination as X Z first" : "type the destination as X Z, for example 1200 -3400");
         } else {
            String var2 = s1.U(this);
            if (var2 != null) {
               this.w("turn off " + var2 + " first; one bot runs at a time");
            } else if (mc.level.dimension() == Level.NETHER) {
               this.w("it flies in the Overworld and the End, not the Nether");
            } else if (!this.a() && this.O() < 0) {
               this.w("wear an elytra, or put one in your hotbar");
            } else {
               int var3 = cu.p(mc.player);
               if (var3 == 0 && !this.m.getValue()) {
                  cu.t var12 = cu.e(mc.player, 0);
                  this.w(
                     var12.starsOnly()
                        ? "your rockets have firework stars, which hurt when boosting; use plain rockets"
                        : (
                           this.z()
                              ? "put firework rockets in your offhand or hotbar; the pilot never clicks inventory slots"
                              : "you need firework rockets in your offhand or hotbar"
                        )
                  );
               } else {
                  this.u = Math.floor(var1[0]) + 0.5;
                  this.IQ = Math.floor(var1[1]) + 0.5;
                  double var4 = this.v();
                  if (var4 <= 30.0) {
                     this.w("you are already at the destination");
                  } else {
                     this.q = (boolean)1;
                     cu.m(this);
                     ArrayList var6 = new ArrayList();

                     for (String var8 : List.of("ElytraFly", "ElytraHelper", "ElytraMotion")) {
                        Module var9 = LarpClient.R().z().d(var8);
                        if (var9 != null && var9.isEnabled()) {
                           var6.add(var8);
                        }
                     }

                     if (!var6.isEmpty()) {
                        com.larpclient.h.A(this.getName(), "{} paused while the pilot flies", String.join(", ", var6));
                     }

                     this.I3 = mc.level;
                     this.O = mc.player;
                     this.I1 = mc.getConnection() == null ? null : mc.getConnection().getConnection();
                     this.Ir = mc.level.dimension();
                     this.I8 = System.currentTimeMillis();
                     this.I0 = var4;
                     this.c = var3;
                     this.IR = mc.player.getHealth();
                     this.y = mc.player.position();
                     this.R.k();
                     this.G(ac.f.z);
                     this.P(s0.u.B, "Flying to " + V(this.u, this.IQ).replace(" ", ", ") + " (" + k(var4) + " blocks)");
                     this.s(
                        "flying to "
                           + V(this.u, this.IQ).replace(" ", ", ")
                           + ", "
                           + k(var4)
                           + " blocks, at height "
                           + this.IO.getValue().intValue()
                           + " with "
                           + var3
                           + " rockets."
                     );
                     ArrayList var13 = new ArrayList();
                     double var14 = var4 / 1000.0 * 2.6 + 5.0;
                     if (var3 < var14 && !this.m.getValue()) {
                        var13.add("about " + (int)Math.ceil(var14) + " rockets are needed and you have " + var3 + "; it lands safely when they run out");
                     }

                     if (this.z()) {
                        var13.add("rockets in your main inventory are not used, only the offhand and hotbar");
                     }

                     if (!var6.isEmpty()) {
                        var13.add(String.join(" and ", var6) + (var6.size() == 1 ? " pauses" : " pause") + " while the pilot flies and resumes after it lands");
                     }

                     ItemStack var10 = mc.player.getItemBySlot(EquipmentSlot.CHEST);
                     if (this.Ih.getValue()
                        && var10.is(Items.ELYTRA)
                        && hk.x(var10)
                        && this.M() == 0
                        && !this.I5.getValue()
                        && hk.T(var10) < w(var4) / (1 + this.g()) + this.H()) {
                        var13.add("no XP bottles in the hotbar to mend the elytra with on the way");
                     }

                     if (var10.is(Items.ELYTRA) && !hk.x(var10) && this.O() < 0 && hk.T(var10) < w(var4)) {
                        var13.add("the elytra has " + hk.T(var10) + " uses left, no Mending and no spare, so it may land early");
                     }

                     if (hk.J(var0 -> var0.get(DataComponents.FOOD) != null) < 0) {
                        var13.add("no food in the hotbar");
                     }

                     if (this.m.getValue() || this.I5.getValue()) {
                        var13.add(
                           "it may buy "
                              + (this.m.getValue() ? "rockets" : "")
                              + (this.m.getValue() && this.I5.getValue() ? " and " : "")
                              + (this.I5.getValue() ? "XP bottles" : "")
                              + " from /ah on the ground, at most "
                              + this.P.getValue().toLowerCase(Locale.ROOT)
                              + " prices and "
                              + je.z(je.p(this.e.getValue()))
                              + " this trip"
                        );
                     }

                     if (!var13.isEmpty()) {
                        this.s("heads up: " + String.join("; ", var13) + ".");
                     }

                     com.larpclient.h.A(
                        this.getName(),
                        "Started: to {} from {}, {} blocks, height {}, {} rockets, elytra {} uses, {}",
                        V(this.u, this.IQ),
                        mc.player.blockPosition().toShortString(),
                        k(var4),
                        this.IO.getValue().intValue(),
                        var3,
                        this.i(),
                        this.Ir
                     );
                  }
               }
            }
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.isEnabled() && this.q) {
         this.It = "the world, the player or the connection changed mid-flight";
         com.larpclient.h.A(this.getName(), "Interrupted in {} after {} blocks: {}", this.Ij, k(this.D), this.It);
      }

      super.onDisable();
      this.R.F();
      this.Ia.l();
      this.g();
      if (this.IW != null) {
         this.e(this.IW);
         this.IW = null;
      }
   }

   private void g() {
      cu.k(this);
      this.a.Q("the trip ended");
      this.Im = 0L;
      this.S = this.x = 0;
      this.h.clear();
      this.q = (boolean)0;
      this.I3 = this.O = this.I1 = null;
      this.Ir = null;
      this.Ij = ac.f.z;
      this.IZ = ac.l.l;
      this.If = ac.w.m;
      this.C = this.X = this.LT = 0;
      this.H = this.Id = this.E = -1000;
      this.d = 0;
      this.I_ = null;
      this.IH = null;
      this.Ib = this.IK = 0;
      this.J = null;
      this.IF = this.i = (boolean)0;
      this.Z = Double.MAX_VALUE;
      this.I9 = 0;
      this.W = this.IU = this.f = -1000;
      this.Iz = 3;
      this.G = -1000;
      this.Ix = this.A = 0;
      this.Ii = null;
      this.Ik = (boolean)1;
      this.II = (boolean)0;
      this.Iw = this.Y = (boolean)0;
      this.IN = Integer.MIN_VALUE;
      this.Q = -1;
      this.Iy = null;
      this.j = this.IE = this.Iq = this.Ie = 0;
      this.n = -1000;
      this.Ic = -1000;
      this.y = this.Io = null;
      this.w = 0;
      this.I4 = 0;
      this.D = this.I0 = this.IS = 0.0;
      this.In = "";
      this.s.clear();
      this.I2.clear();
      this.IG.clear();
      this.T.x();
      this.c = this.Iu = 0;
      this.p = Integer.MIN_VALUE;
      this.IM = -1000;
   }

   private void w(String var1) {
      com.larpclient.h.A(this.getName(), "Not started: {}", var1);
      this.s("not started, " + var1 + ".");
      s0.s().V("Auto Elytra Pilot", P(var1), s0.u.p);
      this.e("Not started: " + var1);
      this.IW = this.getStatus();
      this.disable();
   }

   @Override
   public void onTick() {
      this.Ia.m();
      this.Iw = this.Y = (boolean)0;
      this.II = (boolean)0;
      boolean var2 = false;
      byte var1 = this.q;
      int var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 != 0) {
         if (mc.level == this.I3 && mc.player == this.O) {
            if (mc.level.dimension() != this.Ir) {
               this.p(false, "you changed dimension");
            } else if (!mc.player.isAlive()) {
               this.p(false, "you died");
            } else {
               byte var4 = 1;
               int var3 = this.C;
               this.C = var3 + var4;
               this.IN = mc.player.tickCount;
               this.a();
               if (p_.V()) {
                  this.e("Paused while Freecam is on");
               } else if (this.S()) {
                  this.T.x();
                  this.e("Paused while you steer");
               } else {
                  if (!this.r()) {
                     int var9 = this.y();
                     boolean var6 = false;
                     byte var5 = (byte)var9;
                     var9 = Integer.signum(var5);
                     if ((var9 | -var9) >>> 31 == 0) {
                        switch (this.Ij) {
                           case z:
                              this.G();
                              break;
                           case U:
                              this.X();
                              break;
                           case l:
                              this.l();
                              break;
                           case N:
                              this.i();
                              break;
                           case i:
                              this.k();
                              break;
                           case f:
                              this.V();
                              break;
                           case a:
                              this.L();
                              break;
                           case C:
                              this.j();
                              break;
                           case y:
                              this.U();
                        }
                     }
                  }

                  if (this.isEnabled()) {
                     boolean var8 = false;
                     byte var7 = this.q;
                     var10000 = Integer.signum(var7);
                     if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
                        this.Z();
                     }
                  }
               }
            }
         } else {
            this.p(false, "the world or the player changed");
         }
      }
   }

   private boolean S() {
      return mc.isWindowActive()
         && c6.D() == null
         && (c5.J(mc.options.keyUp) || c5.J(mc.options.keyDown) || c5.J(mc.options.keyLeft) || c5.J(mc.options.keyRight) || c5.J(mc.options.keyJump));
   }

   private void G(ac.f var1) {
      if (var1 == ac.f.f && this.Ij != ac.f.f) {
         this.E = -1000;
         this.Ib = 0;
      }

      if (var1 == ac.f.a && this.Ij != ac.f.a) {
         this.If = ac.w.m;
         this.h.clear();
      }

      if (var1 == ac.f.U) {
         this.Ix = 0;
         this.A++;
         this.Ii = null;
         this.J = null;
         this.IK = 0;
         this.Id = -1000;
         this.d = 0;
         if (this.IZ != ac.l.P) {
            this.IZ = ac.l.l;
         }
      }

      if (var1 == ac.f.N) {
         this.A = 0;
      }

      if (var1 == ac.f.i && this.Ij != ac.f.i) {
         this.Z = Double.MAX_VALUE;
         this.I9 = this.C;
      }

      if (var1 != ac.f.i) {
         this.i = (boolean)0;
      }

      if (var1 != this.Ij) {
         com.larpclient.h.A(
            this.getName(), "{} -> {} at {} ({} blocks to go, job {})", this.Ij, var1, mc.player.blockPosition().toShortString(), k(this.v()), this.IZ
         );
      }

      this.Ij = var1;
      this.X = this.C;
   }

   private int j() {
      return this.C - this.X;
   }

   private void G() {
      if (mc.player.isInWater()) {
         this.T();
      } else if (this.w()) {
         this.G(ac.f.l);
      } else if (!mc.player.onGround()) {
         this.e("Waiting to land");
      } else {
         this.e("Getting ready");
         if (!this.a()) {
            if (!this.D()) {
               this.p(false, "there is no usable elytra left");
            }
         } else if (this.v() <= 30.0) {
            this.IZ = ac.l.l;
            this.G(ac.f.C);
         } else if (this.j() >= 4) {
            ac.w var1 = this.Q(true);
            if (var1 != ac.w.m) {
               this.IZ = ac.l.K;
               this.G(ac.f.a);
            } else if (cu.p(mc.player) == 0) {
               this.p(false, this.M());
            } else {
               int var2 = this.i();
               if (var2 >= 0 && var2 < 30) {
                  this.p(false, "the elytra is almost broken and cannot be mended here");
               } else if (this.A >= 6) {
                  this.p(false, "it could not take off here; it needs open sky above you");
               } else {
                  this.IZ = ac.l.l;
                  this.G(ac.f.U);
               }
            }
         }
      }
   }

   private void X() {
      if (mc.player.isInWater()) {
         this.T();
      } else if (this.w()) {
         this.G(ac.f.l);
      } else if (!this.a()) {
         this.G(ac.f.z);
      } else if (!this.Ia.D()) {
         this.G(ac.f.z);
      } else {
         this.e("Taking off");
         if (mc.player.onGround()) {
            if (this.C - this.G >= 14) {
               if (this.Ix >= 5) {
                  this.p(false, "it could not take off here; it needs open sky above you");
               } else {
                  this.Ik = this.e(16);
                  if (!this.Ik) {
                     this.Ii = this.p();
                     if (this.Ii == null) {
                        this.p(false, "there is no open sky to take off from; stand somewhere open");
                        return;
                     }
                  }

                  float var1 = this.Ik ? mc.player.getYRot() : this.Ii;
                  float var2 = this.Ik ? -88.0F : -35.0F;
                  this.h(var1, var2);
                  this.Ia.S(3);
                  if (Math.abs(Mth.wrapDegrees(var1 - mc.player.getYRot())) <= 10.0F && Math.abs(var2 - mc.player.getXRot()) <= 10.0F) {
                     this.Ia.d();
                     this.G = this.C;
                     this.Ix++;
                     com.larpclient.h.U(this.getName(), "Takeoff jump {} ({})", this.Ix, this.Ik ? "straight up" : "toward open sky at yaw " + (int)var1);
                  }
               }
            }
         } else {
            this.h(this.Ik ? mc.player.getYRot() : this.Ii, this.Ik ? -88.0F : -35.0F);
            if (mc.player.getDeltaMovement().y < 0.12 && this.C - this.G >= 2) {
               this.M();
            }
         }
      }
   }

   private void M() {
      if (this.C - this.G >= 2) {
         this.Ia.d();
         this.G = this.C;
      } else {
         this.Ia.Z();
      }
   }

   private void l() {
      if (this.K()) {
         double var1 = this.v();
         this.Ip.j = this.f(var1);
         if (!this.q()) {
            double var3 = mc.player.getY();
            boolean var5 = this.k();
            this.e("Climbing to " + (int)(Math.round(this.Ip.j / 10.0) * 10L));
            if (var3 >= this.Ip.j - 15.0 || !var5 && this.j() > 600) {
               this.G(var1 <= this.u() ? ac.f.i : ac.f.N);
            } else if (var5) {
               this.h(mc.player.getYRot(), -80.0F);
               if (this.h() && mc.player.getXRot() < -60.0F) {
                  this.u();
               }
            } else {
               this.e(this.u, this.IQ, Double.NaN, true);
            }
         }
      }
   }

   private void i() {
      if (this.K()) {
         double var1 = this.v();
         this.Ip.j = this.f(var1);
         if (!this.q()) {
            if (var1 <= this.u()) {
               this.IZ = ac.l.l;
               this.J = null;
               this.IK = 0;
               this.G(ac.f.i);
            } else {
               this.e("Flying to " + V(this.u, this.IQ).replace(" ", ", "));
               this.C();
               this.e(this.u, this.IQ, Double.NaN, true);
            }
         }
      }
   }

   private void k() {
      if (this.K()) {
         if (this.IZ != ac.l.l || !this.q()) {
            int var1 = this.IZ != ac.l.l ? 1 : 0;
            if (this.J == null && this.C - this.Id >= (var1 != 0 ? 10 : 20)) {
               this.K((boolean)var1);
            }

            double var2 = this.J != null ? this.J.x : this.u;
            double var4 = this.J != null ? this.J.z : this.IQ;
            double var6 = this.J != null ? this.J.y : this.m(var2, var4);
            double var8 = Math.hypot(var2 - mc.player.getX(), var4 - mc.player.getZ());
            double var10 = mc.player.getY() - var6;
            this.Ip.j = this.f(this.v());
            this.e(
               this.IZ == ac.l.l
                  ? (this.J == null ? "Descending, looking for a landing spot" : "Descending to land")
                  : (this.IZ == ac.l.K ? "Landing for a stop" : "Landing to stop safely")
            );
            if (this.J == null && this.IZ != ac.l.l) {
               this.e(this.u, this.IQ, Double.NaN, var1 == 0 || cu.p(mc.player) > 3);
            } else {
               if (this.J != null) {
                  if (var10 < this.Z - 3.0) {
                     this.Z = var10;
                     this.I9 = this.C;
                  } else if (this.C - this.I9 > 700) {
                     this.P(s0.u.B, "Cannot get down to this landing spot, picking another");
                     this.H();
                     this.I9 = this.C;
                     return;
                  }

                  if (var8 <= 50.0 && var10 <= 28.0 && var10 >= -1.0) {
                     this.G(ac.f.f);
                     return;
                  }
               }

               double var12 = var6 + 14.0 + Math.max(0.0, var8 - 45.0) * 0.095;
               double var14 = mc.player.getY() - var12;
               if (!this.i && var8 < 170.0 && var10 > 32.0 && (var14 > 22.0 || var8 < 60.0)) {
                  this.i = (boolean)1;
                  com.larpclient.h.U(this.getName(), "Circling down: {} blocks above the slope, {} across", (int)var14, (int)var8);
               }

               if (this.i && (var10 <= 26.0 || var14 < 8.0 && var8 >= 60.0 || var8 > 260.0)) {
                  this.i = (boolean)0;
               }

               if (this.i) {
                  double var16 = Math.atan2(mc.player.getZ() - var4, mc.player.getX() - var2) + Math.toRadians(55.0);
                  this.Ip.b = var14 > 40.0 ? 3.0 : 1.9;
                  this.e(var2 + Math.cos(var16) * 42.0, var4 + Math.sin(var16) * 42.0, var6, true);
               } else {
                  this.Ip.b = 1.45;
                  this.e(var2, var4, var6, true);
               }
            }
         }
      }
   }

   private void V() {
      if (mc.player.isInWater()) {
         if (mc.player.isUnderWater()) {
            this.Y = (boolean)1;
         } else {
            this.e();
         }
      } else if (mc.player.onGround() && !this.w()) {
         this.e();
      } else if (!this.w()) {
         this.e("Landing");
         if (mc.player.getDeltaMovement().y < -0.3 && !mc.player.hasEffect(MobEffects.LEVITATION)) {
            this.M();
         }
      } else if (this.J == null) {
         this.G(ac.f.i);
      } else {
         double var1 = Math.hypot(this.J.x - mc.player.getX(), this.J.z - mc.player.getZ());
         double var3 = mc.player.getY() - this.J.y;
         if (!(var3 < -4.0) && this.j() <= 700 && this.Ib <= 24 && !(var1 > 90.0)) {
            this.e("Landing (" + (int)var1 + " blocks out, " + (int)Math.max(0.0, var3) + " up)");
            if (this.C - this.E >= 2 || this.IH == null) {
               this.E = this.C;
               this.w();
               this.IH = this.z.Z(this::B, this::F, this.IJ, this.J.x, this.J.y, this.J.z, this.z(), this.R());
               this.Ib = this.IH.cost() >= 900.0 ? this.Ib + 1 : 0;
            }

            this.h(this.IH.yaw(), this.IH.pitch());
         } else {
            this.r(var3 < -4.0 ? "below the spot" : (var1 > 90.0 ? "too far from the spot" : "the approach did not work out"));
         }
      }
   }

   private void e() {
      if (this.IZ == ac.l.l) {
         double var1 = this.J == null ? Double.MAX_VALUE : Math.hypot(this.J.x - this.u, this.J.z - this.IQ);
         double var3 = this.J == null ? Double.MAX_VALUE : Math.hypot(this.J.x - mc.player.getX(), this.J.z - mc.player.getZ());
         if (this.v() <= 72.0 || var3 <= 25.0 || this.v() <= var1 + 20.0) {
            this.G(ac.f.C);
            return;
         }

         com.larpclient.h.A(this.getName(), "Touched down {} blocks short of the spot; taking off again", k(var3));
         if (this.J != null) {
            this.H();
         }

         this.G(ac.f.z);
      } else if (this.IZ == ac.l.P) {
         if (this.v() <= 72.0) {
            com.larpclient.h.A(this.getName(), "Stopped at the destination; counting it as arrived ({})", this.In);
            this.G(ac.f.C);
         } else {
            this.G(ac.f.y);
         }
      } else {
         this.G(ac.f.a);
      }
   }

   private void r(String var1) {
      this.IK++;
      this.P(s0.u.B, "Go-around (" + var1 + ")");
      if (this.IK >= 4) {
         this.H();
         this.IK = 0;
      }

      this.G(ac.f.i);
   }

   private void L() {
      if (mc.player.isInWater()) {
         this.T();
      } else if (this.w()) {
         this.G(ac.f.f);
      } else if (!mc.player.onGround()) {
         this.e("Waiting to land");
      } else if (this.c()) {
         this.R.B();
         this.a.Q("a threat at the stop");
         int var3 = cu.p(mc.player) < 2 || !this.a() || this.i() >= 0 && this.i() < 30 ? 0 : 1;
         if (var3 != 0) {
            this.P(s0.u.p, "Danger at the landing spot, taking off");
            this.s.add(mc.player.position());
            this.G(ac.f.U);
         } else {
            this.p(false, "a mob reached the stop and there was no way to take off");
         }
      } else {
         if (this.If == ac.w.m) {
            this.If = this.Q(false);
            this.LT = this.C;
            if (this.If == ac.w.m) {
               if (!this.a()) {
                  if (this.D()) {
                     return;
                  }

                  this.p(false, "there is no usable elytra left");
                  return;
               }

               if (cu.p(mc.player) == 0) {
                  this.p(false, this.M());
                  return;
               }

               int var2 = this.i();
               if (var2 >= 0 && var2 < 40) {
                  this.p(false, "the elytra is too worn to fly on and cannot be mended");
                  return;
               }

               if (this.v() <= 30.0) {
                  this.IZ = ac.l.l;
                  this.G(ac.f.C);
                  return;
               }

               this.IZ = ac.l.l;
               this.P(s0.u.m, "Stop done: " + cu.p(mc.player) + " rockets, " + this.M() + " XP bottles");
               this.G(ac.f.U);
               return;
            }

            com.larpclient.h.A(this.getName(), "Stop task: {}", this.If);
         }

         int var1 = this.If != ac.w.t && this.If != ac.w.d ? 0 : 1;
         if (this.C - this.LT > (var1 != 0 ? 3600 : 400)) {
            com.larpclient.h.A(this.getName(), "Stop task {} timed out", this.If);
            if (var1 != 0) {
               this.a.Q("it took too long");
               this.h.add(this.If == ac.w.t ? je.w.W : je.w.A);
            }

            this.K();
         } else {
            switch (this.If) {
               case e:
                  this.e("Eating");
                  if (!this.R.r(4)) {
                     this.K();
                  }
                  break;
               case V:
                  this.f();
                  break;
               case X:
                  this.e("Putting on the spare elytra");
                  if (!this.D()) {
                     this.K();
                  }
                  break;
               case t:
                  this.r(je.w.W);
                  break;
               case d:
                  this.r(je.w.A);
                  break;
               default:
                  this.If = ac.w.m;
            }
         }
      }
   }

   private void K() {
      this.R.B();
      this.If = ac.w.m;
   }

   private boolean j(je.w var1) {
      boolean var2 = var1 == je.w.W ? this.m.getValue() : this.I5.getValue();
      return var2 && !this.h.contains(var1) && !this.Il && this.S() > 0L;
   }

   private long S() {
      long var1 = je.p(this.e.getValue());
      return var1 < 0L ? Long.MAX_VALUE : Math.max(0L, var1 - this.Im);
   }

   private int n() {
      return this.IC.getValue().intValue() * 64;
   }

   private int x() {
      return this.IA.getValue().intValue() * 64;
   }

   private void r(je.w var1) {
      int var2 = var1 == je.w.W ? 1 : 0;
      if (!this.a.G()) {
         int var7 = var2 != 0 ? cu.p(mc.player) : this.M();
         int var8 = (var2 != 0 ? this.n() : this.x()) - var7;
         if (var8 <= 0) {
            this.K();
         } else {
            long var5 = this.S();
            this.a.P(var1, var8, je.U(this.P.getValue(), var1), var5 == Long.MAX_VALUE ? -1L : var5);
            this.e("Buying " + (var2 != 0 ? "rockets" : "XP bottles") + " on /ah");
         }
      } else {
         t6.y var3 = this.a.u();
         this.e("Buying " + (var2 != 0 ? "rockets" : "XP bottles") + " on /ah: " + this.a.r() + " so far");
         if (var3 != t6.y.b) {
            this.Im = this.Im + this.a.j();
            if (var2 != 0) {
               this.S = this.S + this.a.r();
            } else {
               this.x = this.x + this.a.r();
            }

            if (this.a.o()) {
               this.Il = (boolean)1;
            }

            String var4 = var2 != 0 ? "rockets" : "XP bottles";
            if (this.a.r() > 0) {
               this.P(s0.u.m, "Bought " + this.a.r() + " " + var4 + " for " + je.z(this.a.j()) + (this.a.u().isEmpty() ? "" : " (" + this.a.u() + ")"));
            }

            if (var3 == t6.y.g || !this.a.u().isEmpty()) {
               this.h.add(var1);
            }

            if (var3 == t6.y.g) {
               this.P(s0.u.p, "Could not buy " + var4 + ": " + this.a.u());
            }

            this.K();
         }
      }
   }

   private ac.w Q(boolean var1) {
      LocalPlayer var2 = mc.player;
      if ((var2.getFoodData().getFoodLevel() <= 10 || var2.getHealth() < 15.0F && var2.getFoodData().getFoodLevel() < 20)
         && hk.J(var0 -> var0.get(DataComponents.FOOD) != null) >= 0
         && this.j()) {
         return ac.w.e;
      } else {
         int var3 = this.i();
         int var4 = this.Q();
         boolean var5 = this.T();
         int var6 = var5 && this.j(je.w.A) ? 1 : 0;
         if (var3 >= 0 && var3 <= this.H() && (!var5 || this.M() <= 0 && var6 == 0) && this.O() >= 0) {
            return ac.w.X;
         } else {
            int var7 = var5 && var3 >= 0 && var3 < var4 * (var1 ? 0.9 : 0.97) ? 1 : 0;
            int var8 = cu.p(mc.player);
            if (var7 != 0 && this.M() == 0 && var6 != 0) {
               return ac.w.d;
            } else if (var7 != 0 && this.M() > 0) {
               return ac.w.V;
            } else if (this.v() < 1500.0 && var8 >= 4) {
               return ac.w.m;
            } else if (var8 < this.n() / 2 && this.j(je.w.W)) {
               return ac.w.t;
            } else {
               return var6 != 0 && this.M() < this.x() / 2 ? ac.w.d : ac.w.m;
            }
         }
      }
   }

   private boolean j() {
      return !mc.player.isUsingItem() || mc.player.getUseItem().get(DataComponents.FOOD) != null;
   }

   private void f() {
      int var1 = this.i();
      int var2 = this.Q();
      ItemStack var3 = mc.player.getItemBySlot(EquipmentSlot.CHEST);
      if (var1 >= 0 && var3.isDamaged() && var1 < var2) {
         if (this.C - this.LT < 2) {
            this.j = var1;
            this.IE = this.C;
         }

         if (var1 > this.j) {
            this.j = var1;
            this.IE = this.C;
         }

         if (this.C - this.IE > 200) {
            this.Iq++;
            this.P(s0.u.p, "Mending stopped working here (other Mending gear may be taking the XP)");
            this.K();
         } else {
            this.e("Mending the elytra: " + var1 * 10 / Math.max(1, var2) * 10 + "%");
            if (!this.R.C(() -> this.Ie++)) {
               com.larpclient.h.A(this.getName(), "Mending ended at {} of {} uses: no XP bottles left in the hotbar", var1, var2);
               this.K();
            }
         }
      } else {
         if (var1 >= 0) {
            this.Iq = 0;
            this.P(s0.u.m, "Elytra repaired");
         }

         this.K();
      }
   }

   private void j() {
      this.e("Arrived");
      if (mc.player.isInWater()) {
         if (mc.player.isUnderWater()) {
            this.Y = (boolean)1;
         } else if (this.j() > 20) {
            this.p(true, null);
         }
      } else if (!mc.player.onGround()) {
         if (this.w()) {
            this.IZ = ac.l.l;
            this.G(ac.f.f);
         }
      } else {
         Vec3 var1 = mc.player.getDeltaMovement();
         if (this.j() >= 10 || Math.hypot(var1.x, var1.z) <= 0.03) {
            this.p(true, null);
         }
      }
   }

   private void U() {
      this.e("Stopping: " + this.In);
      if (!mc.player.onGround() && !mc.player.isInWater()) {
         if (this.w()) {
            this.G(ac.f.f);
         }
      } else {
         this.p(false, this.In.isEmpty() ? "the trip could not go on" : this.In);
      }
   }

   private void e(double var1, double var3, double var5, boolean var7) {
      if (this.C - this.H >= 2 || this.I_ == null) {
         this.H = this.C;
         this.IG.clear();
         this.w();
         this.Ip.a = var1;
         this.Ip.O = var3;
         this.Ip.q = var5;
         this.Ip.E = 14.0;
         int var8 = this.Ir == Level.END ? 1 : 0;
         this.Ip.Y = var8 != 0 ? 185.0 : 262.0;
         if (!Double.isNaN(var5)) {
            this.Ip.Y = Math.min(this.Ip.Y, Math.max(this.J(), var5) + 48.0);
         }

         this.Ip.i = var8 != 0 ? 35.0 : Double.NEGATIVE_INFINITY;
         this.Ip.w = this.Iz;
         this.Ip.N = this.R();
         this.Ip.U = (boolean)(!var7 || this.C < this.f || !this.F() && !(mc.player.getY() < this.Ip.j - 50.0) ? 0 : 1);
         int var9 = cu.p(mc.player);
         this.Ip.Q = var9 < 12 ? 140.0 : (var9 < 32 ? 95.0 : 70.0);
         this.I_ = this.z.o(this::B, this.IJ, this.z(), this.h(), this.Ip);
         this.y();
      }

      this.h(this.I_.yaw(), this.I_.pitch());
      if (this.I_.rocket()
         && this.h()
         && Math.abs(mc.player.getXRot() - this.I_.pitch()) < 20.0F
         && Math.abs(Mth.wrapDegrees(this.I_.yaw() - mc.player.getYRot())) < 30.0F) {
         this.u();
      }
   }

   private void y() {
      this.I2.clear();
      x.w var1 = new x.w().y(this.IJ);
      int var2 = this.I_.rocket() ? com.larpclient.x.i(this.Ip.w) : this.z();

      for (int var3 = 0; var3 < 60; var3++) {
         var1.A = com.larpclient.x.t(var1.A, this.I_.yaw(), 12.0F);
         var1.z = com.larpclient.x.t(var1.z, this.I_.pitch(), 9.0F);
         com.larpclient.x.P(var1, this.Ip.N);
         com.larpclient.x.s(var1);
         if (var2-- > 0) {
            com.larpclient.x.C(var1);
         }

         if (var3 % 3 == 0) {
            this.I2.add(new Vec3(var1.w, var1.L, var1.M));
         }
      }
   }

   private void w() {
      Vec3 var1 = mc.player.getDeltaMovement();
      this.IJ.K(mc.player.getX(), mc.player.getY(), mc.player.getZ(), var1.x, var1.y, var1.z, mc.player.getYRot(), mc.player.getXRot());
   }

   private double R() {
      return mc.player.hasEffect(MobEffects.SLOW_FALLING) && mc.player.getDeltaMovement().y <= 0.0 ? 0.01 : 0.08;
   }

   private boolean A() {
      if (this.C < this.IU - 12 && this.C - this.W < 4) {
         return true;
      }

      if (this.C - this.W > 80) {
         return false;
      }

      LocalPlayer var1 = mc.player;
      return !mc.level
         .getEntitiesOfClass(
            FireworkRocketEntity.class, var1.getBoundingBox().inflate(3.0), var1x -> var1x.isAlive() && var1x.getOwner() == var1 && !var1x.isShotAtAngle()
         )
         .isEmpty();
   }

   private int z() {
      return this.A() ? Math.max(0, this.IU - this.C) : 0;
   }

   private boolean h() {
      return this.w() && this.Ia.D() && this.C - this.W >= 10 && !this.A() && !mc.player.isUsingItem() && this.allowsScreenInteractions();
   }

   private void u() {
      this.Ia.S(3);
      cu.t var1 = cu.e(mc.player, 3);
      int var2 = var1.rocket() == null ? 3 : Math.max(1, var1.rocket().duration());
      this.Ia.m(() -> this.isRuntimeActive() && this.q && this.w() && mc.player == this.O, 10.0, 3, () -> {
         this.W = this.C;
         this.Iz = var2;
         this.IU = this.C + com.larpclient.x.i(var2);
         this.Iu++;
      });
   }

   private void h(float var1, float var2) {
      this.Ig = var1;
      this.g = Mth.clamp(var2, -90.0F, 90.0F);
      this.II = (boolean)1;
   }

   private void Z() {
      if (this.II && ta.E(this)) {
         float var1 = Math.abs(Mth.wrapDegrees(this.Ig - mc.player.getYRot())) + Math.abs(this.g - mc.player.getXRot());
         float[] var2 = this.T.Q(mc.player.getYRot(), mc.player.getXRot(), this.Ig, this.g, var1 < 1.5F);
         j6.U(this, j6.l.o, var2[0], var2[1], 1.0);
      }
   }

   private boolean w() {
      return mc.player.isFallFlying() && !mc.player.isInWater();
   }

   private boolean K() {
      if (this.w()) {
         return true;
      }

      if (!mc.player.onGround()) {
         if (mc.player.isInWater()) {
            this.G(ac.f.z);
            return false;
         }

         this.e("Opening the elytra");
         if (mc.player.getDeltaMovement().y < 0.0 && !mc.player.hasEffect(MobEffects.LEVITATION)) {
            this.M();
         }

         return false;
      } else {
         if (this.Ij != ac.f.f && this.Ij != ac.f.i) {
            this.G(ac.f.z);
         } else {
            this.e();
         }

         return false;
      }
   }

   private boolean y() {
      if (this.Ij == ac.f.U || this.w() || mc.player.onGround() || mc.player.hasEffect(MobEffects.LEVITATION)) {
         return false;
      }

      if (!this.a() || mc.player.onClimbable() || mc.player.isInWater()) {
         return false;
      }

      if (mc.player.getDeltaMovement().y > -0.5) {
         return false;
      }

      if (mc.player.fallDistance < 3.0 && !this.A(10)) {
         return false;
      }

      this.e("Falling: opening the elytra");
      this.M();
      this.h(mc.player.getYRot(), 8.0F);
      return true;
   }

   private boolean A(int var1) {
      BlockPos var2 = mc.player.blockPosition();

      for (int var3 = 1; var3 <= var1; var3++) {
         if (!mc.level.getBlockState(var2.below(var3)).getCollisionShape(mc.level, var2.below(var3)).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private boolean r() {
      int var1 = this.i();
      if (this.w() && var1 >= 0 && var1 <= 6 && this.C - this.n >= 6) {
         this.n = this.C;
         if (this.O() < 0) {
            return false;
         }

         com.larpclient.h.A(this.getName(), "Elytra at {} uses mid-air; swapping in the spare", var1);
         return this.D();
      } else {
         return false;
      }
   }

   private void C() {
      int var1 = mc.player.getFoodData().getFoodLevel();
      if (var1 > 10 && !mc.player.isUsingItem()) {
         this.R.B();
      } else if (!this.A() && !(mc.player.getY() < 70.0) && (this.I_ == null || !this.I_.wide())) {
         if (this.R.r(4)) {
            this.e("Eating while cruising");
         }
      }
   }

   private boolean q() {
      if (this.Ij == ac.f.i && this.IZ != ac.l.l) {
         return false;
      }

      int var1 = this.i();
      int var2 = cu.p(mc.player);
      double var3 = this.v();
      int var5 = this.g();
      int var6 = (var1 < 0 || var1 > w(var3) / (1 + var5) * 1.35 + 25.0) && var2 > var3 / 1000.0 * 2.6 + 5.0 ? 1 : 0;
      int var7 = var1 >= 0 && var1 <= (var5 >= 1 ? 40 : 60) ? 1 : 0;
      int var8 = var2 <= 3 ? 1 : 0;
      if (var6 != 0 && var7 == 0 && var8 == 0) {
         return false;
      }

      int var9 = var1 >= 0 && var1 <= this.H() ? 1 : 0;
      int var10 = !this.T() || this.M() <= 0 && !this.j(je.w.A) ? 0 : 1;
      boolean var11 = this.j(je.w.W);
      ac.l var12 = null;
      if (var9 == 0 || var10 == 0 && this.O() < 0) {
         if (var9 != 0 && var1 <= 40) {
            var12 = ac.l.P;
         }
      } else {
         var12 = ac.l.K;
      }

      if (var12 == null && var2 <= 20 && var11) {
         var12 = ac.l.K;
      }

      if (var12 == null && var2 <= 6) {
         var12 = ac.l.P;
      }

      if (var12 == null && var7 != 0) {
         var12 = var10 == 0 && this.O() < 0 ? ac.l.P : ac.l.K;
      }

      if (var12 == null && var8 != 0) {
         var12 = var11 ? ac.l.K : ac.l.P;
      }

      if (var12 == null) {
         return false;
      }

      if (var12 == ac.l.P && this.In.isEmpty()) {
         this.In = var2 <= 6 ? this.M() : "the elytra is worn out and cannot be mended";
      }

      this.IZ = var12;
      this.J = null;
      this.IK = 0;
      this.Id = -1000;
      this.P(
         s0.u.B, var12 == ac.l.K ? "Landing for a stop (" + (var9 != 0 ? "mend or swap the elytra" : "elytra low") + ")" : "Landing to stop safely: " + this.In
      );
      this.G(ac.f.i);
      return true;
   }

   static double w(double var0) {
      return var0 / 27.0 + 50.0;
   }

   private int H() {
      int var1 = this.g();
      int var2 = this.Q();
      return Math.max(var1 >= 3 ? 50 : (var1 >= 1 ? 80 : 110), (int)(var2 * (var1 >= 3 ? 0.15 : (var1 >= 1 ? 0.2 : 0.27))));
   }

   private boolean T() {
      return this.Ih.getValue() && hk.x(mc.player.getItemBySlot(EquipmentSlot.CHEST)) && this.Iq < 3;
   }

   private String M() {
      int var1 = cu.p(mc.player);
      return var1 == 0 ? "it ran out of rockets" : "only " + var1 + " rockets are left";
   }

   private boolean c() {
      if (this.C - this.Ic < 30 && mc.player.getHealth() < 14.0F) {
         return true;
      }

      AABB var1 = mc.player.getBoundingBox().inflate(9.0, 4.0, 9.0);

      for (Entity var3 : mc.level.getEntities(mc.player, var1, var0 -> var0.isAlive() && var0 instanceof Enemy)) {
         if (!(var3 instanceof NeutralMob var4 && !var4.isAngry()) && var3 instanceof LivingEntity var5 && mc.player.hasLineOfSight(var5)) {
            return true;
         }
      }

      return false;
   }

   private void K(boolean var1) {
      this.Id = this.C;
      double var2 = mc.player.getX();
      double var4 = mc.player.getZ();
      mj.q var7 = new mj.q() {
         @Override
         public int surface(int var1, int var2x) {
            return ac.this.B(var1, var2x);
         }

         @Override
         public mj.b ground(int var1, int var2x, int var3) {
            return ac.this.U(var1, var2x, var3);
         }
      };
      mj.h var6;
      if (this.IZ == ac.l.l) {
         if (this.B(Mth.floor(this.u), Mth.floor(this.IQ)) == Integer.MIN_VALUE) {
            return;
         }

         this.d++;
         var6 = mj.b(var7, this.u, this.IQ, var2, var4, 96, false, false);
         if (var6 == null) {
            var6 = mj.b(var7, this.u, this.IQ, var2, var4, 96, false, true);
         }

         if (var6 == null) {
            var6 = mj.b(var7, this.u, this.IQ, var2, var4, 160, false, true);
         }

         if (var6 == null) {
            var6 = mj.b(var7, this.u, this.IQ, var2, var4, 160, true, false);
         }

         if (var6 == null && this.d >= 12) {
            var6 = mj.b(var7, this.u, this.IQ, var2, var4, 300, true, true);
         }

         if (var6 == null && this.d >= 24) {
            var6 = mj.b(var7, var2, var4, var2, var4, 220, true, true);
         }

         if (var6 == null && this.d >= 40) {
            this.In = "there is no safe place to land near the destination";
            this.IZ = ac.l.P;
            return;
         }
      } else {
         Vec3 var8 = Vec3.directionFromRotation(0.0F, mc.player.getYRot());
         double var9 = var1 ? 30.0 : 110.0;
         double var11 = var2 + var8.x * var9;
         double var13 = var4 + var8.z * var9;
         var6 = mj.b(var7, var11, var13, var2, var4, var1 ? 120 : 150, false, false);
         if (var6 == null) {
            var6 = mj.b(var7, var11, var13, var2, var4, var1 ? 120 : 150, false, true);
         }

         if (var6 == null && var1) {
            var6 = mj.b(var7, var2, var4, var2, var4, 200, true, true);
         }
      }

      for (int var15 = 0; var6 != null && var15 < 4 && this.p(var6); var15++) {
         double var16 = Math.toRadians(90 * var15 + 45);
         var6 = mj.b(var7, var6.x() + Math.cos(var16) * 60.0, var6.z() + Math.sin(var16) * 60.0, var2, var4, 90, this.IZ != ac.l.l && var1, true);
      }

      if (var6 != null && !this.p(var6)) {
         this.J = new Vec3(var6.x() + 0.5, var6.y(), var6.z() + 0.5);
         this.IF = var6.water();
         com.larpclient.h.A(
            this.getName(),
            "Landing spot {} {} {}{} ({} blocks from the destination, score {})",
            var6.x(),
            var6.y(),
            var6.z(),
            var6.water() ? " in water" : "",
            k(Math.hypot(this.J.x - this.u, this.J.z - this.IQ)),
            String.format(Locale.ROOT, "%.1f", var6.score())
         );
      }
   }

   private boolean p(mj.h var1) {
      for (Vec3 var3 : this.s) {
         if (Math.hypot(var3.x - var1.x() - 0.5, var3.z - var1.z() - 0.5) < 12.0) {
            return true;
         }
      }

      return false;
   }

   private void H() {
      if (this.J != null) {
         this.s.add(this.J);
         com.larpclient.h.A(this.getName(), "Landing spot {} set aside", BlockPos.containing(this.J).toShortString());
      }

      this.J = null;
      this.i = (boolean)0;
      this.Z = Double.MAX_VALUE;
   }

   private boolean F(int var1, int var2, int var3) {
      mj.b var4 = this.U(var1, var2, var3);
      return var4 == mj.b.K ? this.IF || this.IZ == ac.l.P : var4 != mj.b.Z && var4 != mj.b.B;
   }

   int B(int var1, int var2) {
      long var3 = (long)var1 << 32 ^ var2 & 4294967295L;
      Integer var5 = this.IG.get(var3);
      if (var5 != null) {
         return var5;
      }

      LevelChunk var6 = mc.level.getChunkSource().getChunk(var1 >> 4, var2 >> 4, ChunkStatus.FULL, false);
      int var7;
      if (var6 == null) {
         var7 = Integer.MIN_VALUE;
      } else {
         int var8 = var6.getHeight(Types.MOTION_BLOCKING, var1 & 15, var2 & 15) + 1;
         var7 = var8 <= mc.level.getMinY() ? mc.level.getMinY() - 64 : var8;
      }

      this.IG.put(var3, var7);
      return var7;
   }

   private double m(double var1, double var3) {
      int var5 = this.B(Mth.floor(var1), Mth.floor(var3));
      return var5 != Integer.MIN_VALUE && var5 > mc.level.getMinY() + 1 ? var5 : (this.Ir == Level.END ? 60.0 : 68.0);
   }

   mj.b U(int var1, int var2, int var3) {
      if (var2 - 1 < mc.level.getMinY()) {
         return mj.b.H;
      }

      BlockPos var4 = new BlockPos(var1, var2 - 1, var3);
      BlockState var5 = mc.level.getBlockState(var4);
      FluidState var6 = var5.getFluidState();
      if (var6.is(FluidTags.LAVA)) {
         return mj.b.Z;
      }

      if (var6.is(FluidTags.WATER)) {
         return mj.b.K;
      }

      if (var5.is(BlockTags.LEAVES)) {
         return mj.b.M;
      }

      if (var5.is(Blocks.MAGMA_BLOCK)
         || var5.is(BlockTags.CAMPFIRES)
         || var5.is(Blocks.CACTUS)
         || var5.is(Blocks.SWEET_BERRY_BUSH)
         || var5.is(BlockTags.FIRE)
         || var5.is(Blocks.POWDER_SNOW)
         || var5.is(Blocks.POINTED_DRIPSTONE)
         || var5.is(Blocks.WITHER_ROSE)
         || var5.is(Blocks.COBWEB)) {
         return mj.b.B;
      }

      if (!var5.is(Blocks.SLIME_BLOCK) && !var5.is(Blocks.HONEY_BLOCK)) {
         VoxelShape var7 = var5.getCollisionShape(mc.level, var4);
         if (var7.isEmpty()) {
            return mj.b.H;
         } else {
            AABB var8 = var7.bounds();
            if (!(var8.maxY > 1.01) && !(var8.maxY < 0.45) && !(var8.maxX - var8.minX < 0.9) && !(var8.maxZ - var8.minZ < 0.9)) {
               BlockState var9 = mc.level.getBlockState(var4.above());
               return var9.getFluidState().isEmpty() && var9.getCollisionShape(mc.level, var4.above()).isEmpty() ? mj.b.g : mj.b.H;
            } else {
               return mj.b.H;
            }
         }
      } else {
         return mj.b.H;
      }
   }

   private boolean F() {
      Vec3 var1 = mc.player.getDeltaMovement();
      double var2 = Math.hypot(var1.x, var1.z);
      Vec3 var4 = var2 > 0.2 ? new Vec3(var1.x / var2, 0.0, var1.z / var2) : Vec3.directionFromRotation(0.0F, mc.player.getYRot());
      int var5 = Mth.floor(mc.player.getX() + var4.x * 110.0);
      int var6 = Mth.floor(mc.player.getZ() + var4.z * 110.0);
      return mc.level.getChunkSource().getChunk(var5 >> 4, var6 >> 4, ChunkStatus.FULL, false) != null;
   }

   private int J() {
      if (this.C - this.IM >= 20 || this.p == Integer.MIN_VALUE) {
         this.IM = this.C;
         int var1 = mc.player.getBlockX();
         int var2 = mc.player.getBlockZ();
         int var3 = Integer.MIN_VALUE;

         for (short var4 = -160; var4 <= 160; var4 += 16) {
            for (short var5 = -160; var5 <= 160; var5 += 16) {
               int var6 = this.B(var1 + var4, var2 + var5);
               if (var6 != Integer.MIN_VALUE) {
                  var3 = Math.max(var3, var6);
               }
            }
         }

         this.p = var3 == Integer.MIN_VALUE ? (int)this.m(this.u, this.IQ) : var3;
      }

      return this.p;
   }

   private boolean k() {
      int var1 = mc.player.getBlockX();
      int var2 = mc.player.getBlockZ();
      int var3 = Integer.MIN_VALUE;

      for (byte var4 = -24; var4 <= 24; var4 += 6) {
         for (byte var5 = -24; var5 <= 24; var5 += 6) {
            var3 = Math.max(var3, this.B(var1 + var4, var2 + var5));
         }
      }

      return var3 != Integer.MIN_VALUE && mc.player.getY() < var3 + 20 && this.j() < 200;
   }

   private boolean e(int var1) {
      AABB var2 = mc.player.getBoundingBox().deflate(0.02, 0.0, 0.02);
      int var3 = Mth.floor(mc.player.getY() + 0.6);

      for (int var4 = Mth.floor(var2.minX); var4 <= Mth.floor(var2.maxX); var4++) {
         for (int var5 = Mth.floor(var2.minZ); var5 <= Mth.floor(var2.maxZ); var5++) {
            for (int var6 = var3; var6 <= var3 + var1; var6++) {
               BlockPos var7 = new BlockPos(var4, var6, var5);
               if (!mc.level.getBlockState(var7).getCollisionShape(mc.level, var7).isEmpty()) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   private Float p() {
      Vec3 var1 = mc.player.getEyePosition();
      if (this.x(var1, new Vec3(0.0, 1.0, 0.0), 2.2)) {
         return null;
      }

      Vec3 var2 = mc.player.position();
      float var3 = mc.player.getYRot();

      for (float var7 : new float[]{0.0F, 45.0F, -45.0F, 90.0F, -90.0F, 135.0F, -135.0F, 180.0F}) {
         Vec3 var8 = Vec3.directionFromRotation(-32.0F, var3 + var7);
         Vec3 var9 = new Vec3(-var8.z, 0.0, var8.x).normalize().scale(0.35);
         byte var10 = 0;

         for (Vec3 var14 : new Vec3[]{
            var2.add(0.0, 1.2, 0.0), var2.add(0.0, 1.8, 0.0), var2.add(0.0, 1.2, 0.0).add(var9), var2.add(0.0, 1.2, 0.0).subtract(var9)
         }) {
            if (this.x(var14, var8, 22.0)) {
               var10 = 1;
               break;
            }
         }

         if (var10 == 0) {
            return var3 + var7;
         }
      }

      return null;
   }

   private boolean x(Vec3 var1, Vec3 var2, double var3) {
      BlockHitResult var5 = mc.level.clip(new ClipContext(var1, var1.add(var2.normalize().scale(var3)), Block.COLLIDER, Fluid.NONE, mc.player));
      return var5.getType() == Type.BLOCK;
   }

   private boolean a() {
      ItemStack var1 = mc.player.getItemBySlot(EquipmentSlot.CHEST);
      return var1.is(Items.ELYTRA) && LivingEntity.canGlideUsing(var1, EquipmentSlot.CHEST);
   }

   private int i() {
      ItemStack var1 = mc.player.getItemBySlot(EquipmentSlot.CHEST);
      return var1.is(Items.ELYTRA) && var1.isDamageableItem() ? var1.getMaxDamage() - var1.getDamageValue() : -1;
   }

   private int Q() {
      ItemStack var1 = mc.player.getItemBySlot(EquipmentSlot.CHEST);
      return var1.is(Items.ELYTRA) ? var1.getMaxDamage() : 432;
   }

   private int g() {
      ItemStack var1 = mc.player.getItemBySlot(EquipmentSlot.CHEST);

      for (Entry var3 : var1.getEnchantments().entrySet()) {
         if (((Holder)var3.getKey()).is(Enchantments.UNBREAKING)) {
            return var3.getIntValue();
         }
      }

      return 0;
   }

   private int O() {
      int var1 = Math.max(0, this.i());
      int var2 = -1;
      int var3 = var1;

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = mc.player.getInventory().getItem(var4);
         if (var5.is(Items.ELYTRA) && LivingEntity.canGlideUsing(var5, EquipmentSlot.CHEST)) {
            int var6 = var5.getMaxDamage() - var5.getDamageValue();
            if (var6 >= 60 && var6 > var3 + 20) {
               var2 = var4;
               var3 = var6;
            }
         }
      }

      return var2;
   }

   private boolean D() {
      int var1 = this.O();
      if (var1 < 0) {
         return false;
      }

      ta.F(this, () -> {
         int var1x = this.O();
         if (var1x >= 0) {
            Inventory var2 = mc.player.getInventory();
            var2.setSelectedSlot(var1x);
            if (mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND).consumesAction()) {
               ks.p(mc.player, InteractionHand.MAIN_HAND);
               com.larpclient.h.A(this.getName(), "Put on the spare elytra from hotbar slot {}", var1x + 1);
               this.P(s0.u.B, "Put on the spare elytra");
            }
         }
      }, () -> this.isRuntimeActive() && this.q && mc.player == this.O);
      return true;
   }

   private int M() {
      int var1 = 0;

      for (int var2 = 0; var2 < 9; var2++) {
         ItemStack var3 = mc.player.getInventory().getItem(var2);
         if (var3.is(Items.EXPERIENCE_BOTTLE)) {
            var1 += var3.getCount();
         }
      }

      return var1;
   }

   private boolean z() {
      for (int var1 = 9; var1 < 36; var1++) {
         if (cu.g(mc.player.getInventory().getItem(var1))) {
            return true;
         }
      }

      return false;
   }

   private void T() {
      if (this.Q < 0) {
         this.Q = this.C;
      }

      if (this.C - this.Q > 1200) {
         this.p(false, "it was stuck in water and could not reach the shore");
      } else {
         this.e("Swimming to shore");
         this.Y = (boolean)1;
         if (this.C % 20 == 0 || this.Iy == null) {
            this.Iy = this.b(32);
         }

         if (this.Iy == null) {
            this.h(mc.player.getYRot(), 0.0F);
         } else {
            float[] var1 = j6.s(mc.player.getEyePosition(), this.Iy);
            this.h(var1[0], 0.0F);
            if (Math.abs(Mth.wrapDegrees(var1[0] - mc.player.getYRot())) < 30.0F) {
               this.Iw = (boolean)1;
            }
         }
      }
   }

   private Vec3 b(int var1) {
      int var2 = mc.player.getBlockX();
      int var3 = mc.player.getBlockZ();
      Vec3 var4 = null;
      double var5 = Double.MAX_VALUE;

      for (int var7 = -var1; var7 <= var1; var7 += 2) {
         for (int var8 = -var1; var8 <= var1; var8 += 2) {
            int var9 = this.B(var2 + var7, var3 + var8);
            if (var9 != Integer.MIN_VALUE && this.U(var2 + var7, var9, var3 + var8) == mj.b.g) {
               double var10 = var7 * var7 + var8 * var8;
               if (var10 < var5) {
                  var5 = var10;
                  var4 = new Vec3(var2 + var7 + 0.5, var9, var3 + var8 + 0.5);
               }
            }
         }
      }

      return var4;
   }

   public static Input n() {
      if (M != null && M.isEnabled()) {
         ac var0 = M;
         Minecraft var1 = Minecraft.getInstance();
         return var0 != null
               && var0.isRuntimeActive()
               && var0.q
               && var1.player != null
               && var1.player == var0.O
               && var1.player.tickCount == var0.IN
               && (var0.Iw || var0.Y)
               && !p_.V()
               && !cj.b()
               && !(c6.D() instanceof AbstractContainerScreen)
            ? new Input(var0.Iw, false, false, false, var0.Y, false, false)
            : null;
      } else {
         return null;
      }
   }

   private double v() {
      return Math.hypot(this.u - mc.player.getX(), this.IQ - mc.player.getZ());
   }

   private double f(double var1) {
      double var3 = this.m(this.u, this.IQ);
      double var5 = var3 + 14.0 + Math.max(0.0, var1 - 45.0) * 0.095 + 30.0;
      return Math.max(this.Ir == Level.END ? 150.0 : 140.0, Math.min(this.IO.getValue(), var5));
   }

   private double u() {
      double var1 = this.m(this.u, this.IQ);
      double var3 = Math.max(0.0, mc.player.getY() - var1 - 14.0);
      return 45.0 + var3 / 0.095 + 60.0;
   }

   private void a() {
      Vec3 var1 = mc.player.position();
      if (this.y != null) {
         double var2 = Math.hypot(var1.x - this.y.x, var1.z - this.y.z);
         if (this.Io != null && var1.distanceTo(this.y.add(this.Io)) > 4.0 && this.w()) {
            if (this.C - this.I4 > 200) {
               this.I4 = this.C;
               this.w = 0;
            }

            this.w++;
            if (this.w >= 3 && this.C >= this.f) {
               this.f = this.C + 60;
               com.larpclient.h.A(this.getName(), "The server pulled the player back {} times in 10 seconds; holding rockets for 3 seconds", this.w);
            }
         } else if (var2 < 10.0) {
            this.D += var2;
         }

         double var4 = (Math.hypot(this.u - this.y.x, this.IQ - this.y.z) - this.v()) * 20.0;
         this.IS = this.IS == 0.0 ? var4 : this.IS * 0.97 + var4 * 0.03;
      }

      this.y = var1;
      this.Io = mc.player.getDeltaMovement();
      float var6 = mc.player.getHealth();
      if (var6 < this.IR) {
         this.Ic = this.C;
      }

      this.IR = var6;
   }

   private String d() {
      double var1 = this.v();
      StringBuilder var3 = new StringBuilder(k(var1)).append(" blocks left");
      if (this.IS > 2.0 && this.Ij != ac.f.C) {
         var3.append(", about ").append(s1.h((long)(var1 / this.IS * 1000.0)));
      }

      return var3.toString();
   }

   private void p(boolean var1, String var2) {
      if (!this.q) {
         this.disable();
      } else {
         String var3 = s1.h(System.currentTimeMillis() - this.I8);
         String var4 = BlockPos.containing(mc.player.position()).toShortString();
         String var5 = k(this.D)
            + " blocks in "
            + var3
            + ", "
            + this.Iu
            + " rockets"
            + (this.Ie > 0 ? ", " + this.Ie + " XP bottles" : "")
            + (
               this.Im > 0L
                  ? ", bought "
                     + (this.S > 0 ? this.S + " rockets" : "")
                     + (this.S > 0 && this.x > 0 ? " and " : "")
                     + (this.x > 0 ? this.x + " XP bottles" : "")
                     + " for "
                     + je.z(this.Im)
                  : ""
            );
         String var6 = var1
            ? "arrived at " + V(this.u, this.IQ).replace(" ", ", ") + ": " + var5 + "."
            : "stopped at " + var4 + ", " + var2 + ". " + P(var5) + ".";
         com.larpclient.h.A(this.getName(), var1 ? "Arrived: {}" : "Stopped: {}", var6);
         this.s(var6);
         this.P(var1 ? s0.u.m : s0.u.p, P(var6));
         this.e(var1 ? "Arrived" : "Stopped: " + var2);
         this.IW = this.getStatus();
         int var7 = !this.L.getValue() || mc.getConnection() == null || mc.isLocalServer() || !var1 && !mc.player.onGround() && !mc.player.isInWater() ? 0 : 1;
         this.q = (boolean)0;
         this.disable();
         if (var7 != 0) {
            com.larpclient.h.A(this.getName(), "Logging off after the trip");
            mc.getConnection().getConnection().disconnect(Component.literal("Larp Auto Elytra Pilot: " + var6));
         } else if (this.L.getValue() && mc.isLocalServer()) {
            com.larpclient.h.A(this.getName(), "Log Off On Arrival skipped in singleplayer");
         }
      }
   }

   private void s(String var1) {
      if (mc != null && mc.player != null) {
         mc.player.sendSystemMessage(Component.literal("Larp Auto Elytra Pilot: " + var1));
      }
   }

   private void P(s0.u var1, String var2) {
      com.larpclient.h.A(this.getName(), "Notice: {}", var2);
      if (!var2.equals(this.r) || this.C - this.V >= 100) {
         this.r = var2;
         this.V = this.C;
         switch ((String)this.IX.getValue()) {
            case "Toast":
               s0.s().k("Auto Elytra Pilot", var2, var1, 5000L);
               break;
            case "Chat":
               this.s(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
         }
      }
   }

   private static String P(String var0) {
      return var0.isEmpty() ? var0 : var0.substring(0, 1).toUpperCase(Locale.ROOT) + var0.substring(1);
   }

   private void Q() {
      if (this.isInGame() && this.I.getValue() && this.isRuntimeActive() && this.q && mc.level == this.I3) {
         try {
            int var1 = ho.B(0.25);
            double var2 = this.m(this.u, this.IQ);
            Gizmos.line(new Vec3(this.u, var2, this.IQ), new Vec3(this.u, var2 + 256.0, this.IQ), uh.H(var1, 0.9), 3.0F).setAlwaysOnTop();
            Vec3 var4 = mc.player.position();
            if (!this.I2.isEmpty() && this.Ij != ac.f.f && mc.player.isFallFlying()) {
               for (Vec3 var6 : this.I2) {
                  Gizmos.line(var4, var6, uh.H(var1, 0.8), 2.0F).setAlwaysOnTop();
                  var4 = var6;
               }
            }

            Gizmos.line(var4, new Vec3(this.u, Math.max(var2, var4.y - 40.0), this.IQ), uh.H(var1, 0.35), 1.5F).setAlwaysOnTop();
            if (this.J != null) {
               Gizmos.cuboid(
                     new AABB(this.J.x - 1.5, this.J.y, this.J.z - 1.5, this.J.x + 1.5, this.J.y + 0.1, this.J.z + 1.5),
                     GizmoStyle.strokeAndFill(-14249, 2.0F, 872400983)
                  )
                  .setAlwaysOnTop();
            }
         } catch (RuntimeException var7) {
            com.larpclient.h.w(this.getName(), "Waypoint line failed", var7);
         }
      }
   }

   @Override
   public s1.u hudCard() {
      if (this.isInGame() && this.Is.getValue() && this.isRuntimeActive() && this.q && mc.level == this.I3) {
         double var1 = this.I0 <= 0.0 ? -1.0 : Math.clamp(1.0 - this.v() / this.I0, 0.0, 1.0);
         int var3 = this.i();
         String var4 = this.getStatus();
         return new s1.u(
            this.d(),
            var1 < 0.0 ? "" : Math.round(var1 * 100.0) + "%",
            var4 != null && !var4.isBlank() ? var4 : "Starting",
            cu.p(mc.player) + " rockets" + (var3 >= 0 ? ", elytra " + var3 + "/" + this.Q() : ""),
            var1
         );
      } else {
         return null;
      }
   }

   public ac.f T() {
      return this.Ij;
   }

   public boolean s() {
      return this.q;
   }

   public double T() {
      return this.q && mc != null && mc.player != null ? this.v() : -1.0;
   }

   public int P() {
      return this.Iu;
   }

   public Vec3 x() {
      return this.J;
   }

   public enum f {
      z,
      U,
      l,
      N,
      i,
      f,
      a,
      C,
      y;
   }

   enum l {
      l,
      K,
      P;
   }

   enum w {
      m,
      e,
      V,
      X,
      t,
      d;
   }
}

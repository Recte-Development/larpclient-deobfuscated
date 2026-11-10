package com.larpclient;

import com.larpclient.module.MCFriendModule;
import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.Game;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class a4 extends ar implements s1, t1.s {
   private static a4 o;
   private static boolean Kx;
   static final List<String> K9 = List.of("AutoAnchor", "AnchorTap", "Anchor Macro", "AutoCrystal", "Freecam");
   static final double m = 28.0;
   static final int Rl = 900;
   static final long Rj = 120000L;
   static final long Rs = 360000L;
   static final long e = 60000L;
   static final int RF = 3;
   static final int R9 = 16000;
   private final com.larpclient.module.l RW = this.addSetting(
      new com.larpclient.module.l(
         "Region",
         "Which /rtp region to use. Random takes the overworld regions in turn; the Nether is never used",
         "Random",
         com.larpclient.e.O.toArray(new String[0])
      )
   );
   private final com.larpclient.module.e Kj = this.Z(
      "Player Range",
      "Any player closer than this makes the bot leave by the next /rtp. Servers only show players up to about 48 to 128 blocks away, so the default means anyone you can see",
      96.0,
      8.0,
      160.0,
      1.0
   );
   private final com.larpclient.module.s Rt = this.U("Ignore Friends", "Players on your friends list (MCFriend) do not make the bot leave", true);
   private final com.larpclient.module.s R5 = this.U(
      "Log Out If Caught",
      "If a player gets within 5 blocks while /rtp is still cooling down, log out instead of running. Never logs out while a player has hit you in the last 15 seconds",
      false
   );
   private final com.larpclient.module.s Rz = this.U(
      "Dig To Hidden Anchors", "Dig a tunnel to anchors inside walls or underground. Off: only anchors you can walk up to", true
   );
   private final com.larpclient.module.s j = this.U(
      "Pearl Travel", "Throw ender pearls from your hotbar to reach far anchors fast. Only while you wear armour and have enough health", true
   );
   private final com.larpclient.module.s Rr = this.U(
      "Ender Chest Stash",
      "When your inventory is full, put the anchors in your ender chest. Needs an ender chest and a Silk Touch pickaxe in your hotbar",
      true
   );
   private final com.larpclient.module.e RL = this.Z("Stop After", "Stop after this many anchors (0 never stops)", 0.0, 0.0, 2304.0, 1.0);
   private final com.larpclient.module.l D = this.addSetting(
      new com.larpclient.module.l("Notifications", "How the bot tells you about finds and problems", "Toast", "Toast", "Chat", "Off")
   );
   private final com.larpclient.module.s RS = this.U(
      "Status Card", "Show a card at the top of the screen with anchors found, the /rtp timer and what the bot is doing", true
   );
   private final com.larpclient.module.s RG = this.U("Show Path", "Draw the route, the blocks it digs, the anchor it is going to and its pearl throws", true);
   private final com.larpclient.module.s Rw = this.U(
      "Fight Mobs",
      "Hit zombies, spiders and other melee mobs that come for you, with the best sword or axe in your hotbar. Creepers and skeletons are still run from, and it runs at 8 health or less",
      false
   );
   private final com.larpclient.module.s I = this.U(
      "Auto Sell Anchors",
      "After picking up an anchor, sell your anchors with /sell, standing still. Only anchors go into the Sell menu; if the menu looks wrong it stops selling for five minutes",
      false
   );
   private final hc R2 = new hc();
   private final hk KU = new hk(this);
   private final gu h = new gu(this, this.R2, this::J);
   private final e RH = new e();
   private final e.a Rb = new e.a();
   private final ConcurrentLinkedQueue<String> i = new ConcurrentLinkedQueue<>();
   private a4.r f = a4.r.Q;
   private a4.r Rx = a4.r.w;
   private boolean Rh;
   private Object Rf;
   private Object H;
   private Object B;
   private long KA = -1L;
   private String KX;
   private String p;
   private final List<String> R1 = new ArrayList<>();
   private boolean RE;
   private int Rd;
   private int Rq;
   private long RP;
   private long Re;
   private long x;
   private long r = -2305843009213693952L;
   private long RO = -1L;
   private String Rn;
   private boolean RN;
   private Vec3 RX;
   private int RU;
   private int t;
   private int RI;
   private int RA;
   private final Map<String, Integer> X = new TreeMap<>();
   private final Map<Long, Integer> g = new HashMap<>();
   private final Set<Long> Rp = new HashSet<>();
   private boolean KP;
   private boolean Rk;
   private boolean R0;
   private LivingEntity u;
   private int K6;
   private String P = "";
   private double Rg = Double.MAX_VALUE;
   private final List<double[]> w = new ArrayList<>();
   private long Ru = -1L;
   private float Ro = 20.0F;
   private double[] O;
   private int RJ;
   private BlockPos RB;
   private int RY;
   private int KW;
   private int q;
   private int Rm;
   private int R;
   private int R7;
   private int RZ;
   private int y;
   private boolean F;
   private boolean R_;
   private long G;
   private long Kh = -2305843009213693952L;
   private int Rv;
   private int c = -1000;
   private final Set<Long> Rc = new HashSet<>();
   private final Set<Long> Kg = new HashSet<>();
   private Boolean RK;
   private final Set<BlockPos> n = new HashSet<>();
   private int Ra;
   private int Ry;
   private int R8;
   private int d;
   private int M;
   private int RM = -1;
   private int T;
   private int A;
   private BlockPos R4;
   private String RV;
   private int K7;
   private int a;
   private int L;
   private int Y = -1;
   private int W;
   private int S;
   private long RR;
   static final long U = 300000L;
   private a4.a RC = a4.a.r;
   private float J;
   private float V;
   private Vec3 C;
   private int s;
   private int b = -1000;
   private Vec3 z;
   private Vec3 Z = Vec3.ZERO;
   private boolean RQ;
   private boolean RT;
   private boolean E;
   private int RD = Integer.MIN_VALUE;
   private String Q = "";
   private long Ri;
   private final List<BlockPos> R6 = new ArrayList<>();
   private long R3;

   public a4() {
      super("Anchor Hunter", "DonutSMP: /rtp around, find respawn anchors in loaded chunks, walk or dig to them, mine and collect them");
      o = this;
      this.RL.h("Stop After (anchors, 0 never)");
      if (!Kx) {
         Kx = (boolean)1;
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> {
            a4 var1 = o;
            if (var1 != null) {
               var1.U();
            }
         });
         ClientReceiveMessageEvents.GAME.register((Game)(var0, var1) -> F(var0.getString()));
      }
   }

   public static a4 f() {
      return o;
   }

   public static void F(String var0) {
      if (o != null && o.isEnabled()) {
         a4 var1 = o;
         if (var1 != null && var1.Rh && var0 != null && !var0.isBlank() && var1.i.size() < 64) {
            var1.i.add(var0);
         }
      }
   }

   @Override
   public boolean allowsScreenInteractions() {
      return mc == null || !(c6.D() instanceof AbstractContainerScreen) || this.X();
   }

   private boolean X() {
      return this.Rh && (this.f == a4.r.J || this.f == a4.r.u) && mc != null && mc.player != null && mc.player.containerMenu instanceof ChestMenu;
   }

   @Override
   public void disable() {
      this.KX = null;
      int var1 = this.KA >= 0L ? 1 : 0;
      this.KA = -1L;
      super.disable();
      if (var1 != 0) {
         this.b("turned off while teleporting");
      }
   }

   @Override
   public List<s1.a> commands() {
      return List.of(new s1.a("Before you start", "Check my setup", this::b), new s1.a("Before you start", "Find anchors near me", this::Y));
   }

   @Override
   public String summary() {
      if (this.isRuntimeActive() && this.Rh) {
         return N(this.RU, "anchor") + " in " + s1.h(System.currentTimeMillis() - this.Re) + ", " + N(this.t, "teleport") + ". " + this.f.k + ".";
      } else {
         return this.p != null ? this.p + "." : "Stand anywhere on DonutSMP and turn it on: it uses /rtp and mines the respawn anchors it finds.";
      }
   }

   @Override
   public String detail() {
      if (!this.isInGame()) {
         return "Needs a diamond or netherite pickaxe in your hotbar.";
      }

      int var1 = this.G(Blocks.RESPAWN_ANCHOR.defaultBlockState());
      String var2 = var1 >= 0 ? mc.player.getInventory().getItem(var1).getHoverName().getString() : "no diamond or netherite pickaxe";
      return "Pickaxe: "
         + var2
         + ". Ender chest "
         + (hk.J(var0 -> var0.is(Items.ENDER_CHEST)) >= 0 ? "yes" : "no")
         + ", Silk Touch pickaxe "
         + (this.W() >= 0 ? "yes" : "no")
         + ", pearls "
         + this.L()
         + ".";
   }

   @Override
   public List<s1.w> settingGroups() {
      return List.of(
         new s1.w("Where to go", List.of(this.RW)),
         new s1.w("Other players", List.of(this.Rt, this.R5)),
         new s1.w("While hunting", List.of(this.I, this.Rr, this.RL, this.Rz, this.j, this.Rw)),
         new s1.w("Display", List.of(this.RS, this.RG, this.D)),
         s1.w.advanced("Advanced", List.of(this.Kj))
      );
   }

   private String b() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();
      if (mc.level.dimension() != Level.OVERWORLD) {
         var1.add("go to the overworld");
      }

      if (this.G(Blocks.RESPAWN_ANCHOR.defaultBlockState()) < 0) {
         var1.add("put a diamond or netherite pickaxe in your hotbar");
      }

      if (hk.J(a4::P) < 0) {
         var2.add("no food in the hotbar");
      }

      if (!this.O()) {
         if (this.Rr.getValue() && this.a()) {
            var2.add("your inventory is full, so it stashes in the ender chest first");
         } else {
            var1.add("make room in your inventory");
         }
      }

      if (this.Rr.getValue() && !this.a()) {
         var2.add("no ender chest and Silk Touch pickaxe in the hotbar, so a full inventory stops the run");
      }

      if (this.j.getValue() && this.L() == 0) {
         var2.add("no ender pearls in the hotbar, so it walks everywhere");
      } else if (this.j.getValue() && mc.player.getArmorValue() < 12) {
         var2.add("wear armour (12 points or more) for pearl travel");
      }

      String var3 = var1.isEmpty() ? "Ready." : "Not ready: " + String.join("; ", var1) + ".";
      return var2.isEmpty() ? var3 : var3 + " Note: " + String.join("; ", var2) + ".";
   }

   private String Y() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      HashMap var1 = new HashMap();
      int var2 = this.k(var1);
      this.R6.clear();
      Vec3 var3 = mc.player.getEyePosition();
      List var4 = var1.keySet()
         .stream()
         .map(var0 -> new BlockPos(zf.W(var0), zf.d(var0), zf.F(var0)))
         .sorted(Comparator.comparingDouble(var1x -> var3.distanceToSqr(Vec3.atCenterOf(var1x))))
         .toList();
      this.R6.addAll(var4.subList(0, Math.min(64, var4.size())));
      this.R3 = System.currentTimeMillis() + 15000L;
      com.larpclient.h.A(this.getName(), "Scan around the player: {} anchors in {} loaded chunks", var1.size(), var2);
      if (var4.isEmpty()) {
         return "No respawn anchors in the " + var2 + " loaded chunks around you.";
      }

      BlockPos var5 = (BlockPos)var4.getFirst();
      return var1.size()
         + " anchors in the "
         + var2
         + " loaded chunks around you, shown for 15 seconds. Nearest at "
         + var5.toShortString()
         + ", "
         + (int)Math.round(Math.sqrt(var3.distanceToSqr(Vec3.atCenterOf(var5))))
         + " blocks away ("
         + var1.get(zf.O(var5.getX(), var5.getY(), var5.getZ()))
         + " charges).";
   }

   @Override
   public void onEnable() {
      super.onEnable();
      if (this.KA >= 0L) {
         long var1 = System.currentTimeMillis() - this.KA;
         this.KA = -1L;
         if (this.N() == this.B && this.B != null && var1 <= 60000L && mc.player.isAlive()) {
            this.Rh = (boolean)1;
            this.Rf = mc.level;
            this.H = mc.player;
            com.larpclient.h.A(this.getName(), "Teleport landed in a new world after {} ms; carrying on", var1);
            this.M(System.currentTimeMillis(), "a new world");
            return;
         }

         this.KX = var1 > 60000L ? "the teleport did not land within a minute" : "the connection changed during the teleport";
         this.b(this.KX);
      }

      if (this.KX != null) {
         String var4 = this.KX;
         this.KX = null;
         this.J("it stopped earlier because " + var4 + ". Turn it on again to start a new run");
      } else {
         String var3 = s1.U(this);
         if (var3 != null) {
            this.J("turn off " + var3 + " first; one bot runs at a time");
         } else if (mc.level.dimension() != Level.OVERWORLD) {
            this.J("go to the overworld first; it only hunts there");
         } else if (this.G(Blocks.RESPAWN_ANCHOR.defaultBlockState()) < 0) {
            this.J("put a diamond or netherite pickaxe with uses left in your hotbar");
         } else if (mc.player.getFoodData().getFoodLevel() < 10 && hk.J(a4::P) < 0) {
            this.J("bring food in your hotbar; you are hungry");
         } else if (this.O() || this.Rr.getValue() && this.a() && this.H(Items.RESPAWN_ANCHOR) > 0) {
            this.V();
            this.Rh = (boolean)1;
            this.p = null;
            this.Rf = mc.level;
            this.H = mc.player;
            this.B = this.N();
            this.RE = mc.isLocalServer();
            this.Re = System.currentTimeMillis();
            this.Ro = mc.player.getHealth();
            this.KU.k();
            this.h();
            ArrayList var2 = new ArrayList();
            if (!this.R1.isEmpty()) {
               var2.add(String.join(", ", this.R1) + (this.R1.size() == 1 ? " is" : " are") + " off while hunting and back on after");
            }

            if (hk.J(a4::P) < 0) {
               var2.add("no food in the hotbar");
            }

            if (this.Rr.getValue() && !this.a()) {
               var2.add("no ender chest and Silk Touch pickaxe in the hotbar, so a full inventory ends the run");
            }

            if (this.j.getValue() && this.L() == 0) {
               var2.add("no pearls in the hotbar, so it walks to far anchors");
            }

            if (this.RE) {
               var2.add("singleplayer has no /rtp, so it only hunts around you");
            }

            this.X(this.RE ? "hunting anchors around you." : "hunting anchors with /rtp " + this.RW.getValue().toLowerCase(Locale.ROOT) + ".");
            if (!var2.isEmpty()) {
               this.X("heads up: " + String.join("; ", var2) + ".");
            }

            com.larpclient.h.A(
               this.getName(),
               "Started at {} ({}), region {}, range {}, dig {}, pearls {}, stash {}, stop after {}",
               mc.player.blockPosition().toShortString(),
               this.RE ? "singleplayer" : "server",
               this.RW.getValue(),
               this.Kj.getValue().intValue(),
               this.Rz.getValue(),
               this.j.getValue(),
               this.Rr.getValue(),
               this.RL.getValue().intValue()
            );
            if (this.RE) {
               this.x = this.Re;
               this.l(a4.r.s, "start");
            } else {
               this.l(a4.r.N, "start");
            }
         } else {
            this.J("your inventory is full; make room first");
         }
      }
   }

   @Override
   public void onDisable() {
      int var1 = this.isEnabled() && this.Rh ? 1 : 0;
      this.t();
      super.onDisable();
      this.KU.F();
      t1.v(this);
      if (var1 == 0) {
         if (this.Rh) {
            this.b(null);
         }

         if (this.p != null) {
            this.e(this.p);
         }
      } else {
         int var2 = mc != null && mc.player != null && mc.player.isAlive() ? 1 : 0;
         if (var2 == 0 || this.N() != this.B || this.B == null || !this.RH.V() && this.f != a4.r.r && this.f != a4.r.i) {
            this.KX = var2 != 0 ? "the world, the player or the connection changed" : "you died";
            com.larpclient.h.A(this.getName(), "Interrupted in {} after {} anchors: {}", this.f, this.RU, this.KX);
            this.b(this.KX);
         } else {
            this.KA = System.currentTimeMillis();
            this.Rh = (boolean)0;
            com.larpclient.h.A(this.getName(), "World changed while waiting for /rtp; waiting for the new world");
         }
      }
   }

   private void b(String var1) {
      this.p();
      if (this.Rh || var1 != null) {
         com.larpclient.h.A(
            this.getName(),
            "Run ended{}: {} anchors, {} teleports, {} spots{}",
            var1 == null ? "" : " (" + var1 + ")",
            this.RU,
            this.t,
            this.RI,
            this.X.isEmpty() ? "" : ", skipped " + this.X
         );
      }

      this.Rh = (boolean)0;
      this.Rf = this.H = this.B = null;
      this.i.clear();
   }

   private void V() {
      this.RH.v();
      this.Rb.g();
      this.i.clear();
      this.X.clear();
      this.RK = null;
      this.n.clear();
      this.S = 0;
      this.RR = 0L;
      this.K7 = 0;
      this.Y = -1;
      this.f = a4.r.Q;
      this.Rx = a4.r.w;
      this.Rd = this.Rq = 0;
      this.RN = (boolean)0;
      this.RO = -1L;
      this.Rn = null;
      this.RU = this.t = this.RI = this.RA = 0;
      this.r = -2305843009213693952L;
      this.RX = null;
      this.KA = -1L;
      this.d();
      this.R2.x();
   }

   private void d() {
      this.g.clear();
      this.Rp.clear();
      this.KP = this.Rk = this.R0 = (boolean)0;
      this.u = null;
      this.P = "";
      this.Rg = Double.MAX_VALUE;
      this.w.clear();
      this.O = null;
      this.l();
      this.RC = a4.a.r;
      this.Ra = 0;
      this.R4 = null;
   }

   private void l() {
      this.RB = null;
      this.RY = this.KW = this.q = this.Rm = this.R = this.y = this.K6 = 0;
      this.F = this.R_ = (boolean)0;
      this.Rc.clear();
      this.Kg.clear();
      this.h.H();
      this.c = -1000;
   }

   private void t() {
      this.h.H();
      l0.h(this);
      this.Z = Vec3.ZERO;
      this.RQ = this.RT = this.E = (boolean)0;
   }

   private Object N() {
      return mc != null && mc.getConnection() != null ? mc.getConnection().getConnection() : null;
   }

   private void J(String var1) {
      com.larpclient.h.A(this.getName(), "Not started: {}", var1);
      this.X("not started, " + var1 + ".");
      s0.s().V("Anchor Hunter", M(var1), s0.u.p);
      this.p = "Not started: " + var1;
      this.e(this.p);
      this.disable();
   }

   private void y(String var1) {
      String var2 = this.RU + " anchors in " + s1.h(System.currentTimeMillis() - this.Re) + ", " + this.t + " teleports";
      com.larpclient.h.A(this.getName(), "Stopped: {} ({})", var1, var2);
      this.C(a4.t.h, M(var1) + ". " + M(var2) + ".");
      this.p = "Stopped: " + var1;
      this.e(this.p);
      this.f = a4.r.G;
      this.disable();
   }

   private void h() {
      this.R1.clear();
      com.larpclient.module.f var1 = LarpClient.R().z();

      for (String var3 : K9) {
         Module var4 = var1.d(var3);
         if (var4 != null && var4 != this && var4.isEnabled()) {
            var4.setEnabled(false);
            if (!var4.isEnabled()) {
               this.R1.add(var3);
            }
         }
      }

      if (!this.R1.isEmpty()) {
         com.larpclient.h.A(this.getName(), "Turned off for the run: {}", String.join(", ", this.R1));
      }
   }

   private void p() {
      if (!this.R1.isEmpty()) {
         com.larpclient.module.f var1 = LarpClient.R().z();
         ArrayList var2 = new ArrayList();

         for (String var4 : this.R1) {
            Module var5 = var1.d(var4);
            if (var5 != null && !var5.isEnabled()) {
               var5.setEnabled(true);
               var2.add(var4);
            }
         }

         this.R1.clear();
         if (!var2.isEmpty()) {
            com.larpclient.h.A(this.getName(), "Turned back on: {}", String.join(", ", var2));
         }
      }
   }

   private String m() {
      com.larpclient.module.f var1 = LarpClient.R().z();

      for (String var3 : K9) {
         Module var4 = var1.d(var3);
         if (var4 != null && var4 != this && var4.isEnabled()) {
            return var3;
         }
      }

      return null;
   }

   @Override
   public void onTick() {
      this.Z = Vec3.ZERO;
      this.RQ = this.RT = this.E = (boolean)0;
      boolean var8 = false;
      byte var7 = this.Rh;
      int var10000 = Integer.signum(var7);
      if ((var10000 | -var10000) >>> 31 != 0) {
         if (mc.level == this.Rf && mc.player == this.H) {
            byte var10 = 1;
            int var9 = this.Rd;
            this.Rd = var9 + var10;
            this.RD = mc.player.tickCount;
            long var1 = System.currentTimeMillis();
            if (!mc.player.isAlive()) {
               this.y("you died");
            } else {
               String var3 = this.m();
               if (var3 != null) {
                  this.y(var3 + " was turned on");
               } else {
                  this.y(var1);
                  if (this.Rh && this.f != a4.r.G) {
                     this.H(var1);
                     this.t(var1);
                     this.L();
                     String var4 = hk.j();
                     if (var4 != null) {
                        this.t();
                        this.KU.B();
                        this.RQ = (boolean)1;
                        this.e(var4 + ": swimming up");
                     } else if (!this.S(var1)) {
                        boolean var12 = false;
                        byte var11 = this.Rk;
                        var10000 = Integer.signum(var11);
                        if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
                           this.G(var1);
                        } else if (this.q() instanceof String var5) {
                           if (this.f == a4.r.J && mc.player.containerMenu instanceof ChestMenu) {
                              mc.player.closeContainer();
                           }

                           if (this.f != a4.r.F) {
                              this.Rx = this.f != a4.r.J && this.f != a4.r.v ? this.f : a4.r.X;
                              this.l(a4.r.F, var5);
                           }

                           this.t();
                           if (!this.X() && c6.D() instanceof AbstractContainerScreen) {
                              t1.v(this);
                           }

                           this.e("Paused: " + var5);
                        } else {
                           if (this.f == a4.r.F) {
                              this.R2.x();
                              this.l(this.Rx, "resumed");
                           }

                           if (this.f() && this.KU.r(4)) {
                              this.t();
                              this.e("Eating");
                           } else if (this.P() && !this.RE && var1 - this.x > 360000L) {
                              this.R("six minutes on this spot");
                           } else {
                              switch (this.f) {
                                 case Q:
                                 case N:
                                    this.l(var1);
                                    break;
                                 case r:
                                 case i:
                                    this.E(var1);
                                    break;
                                 case g:
                                    if (this.RH.z(var1)) {
                                       this.l(var1);
                                    }
                                    break;
                                 case s:
                                    this.X(var1);
                                    break;
                                 case w:
                                    this.m();
                                    break;
                                 case b:
                                    this.L(var1);
                                    break;
                                 case X:
                                    this.F(var1);
                                    break;
                                 case v:
                                    this.i(var1);
                                    break;
                                 case I:
                                    this.X();
                                    break;
                                 case J:
                                    this.a();
                                    break;
                                 case u:
                                    this.f();
                                    break;
                                 case t:
                                    this.KP = (boolean)1;
                                    this.R("the area is protected");
                                    break;
                                 case x:
                                    this.l(this.RH.V() ? a4.r.r : a4.r.N, "nobody near now");
                                 case F:
                                 case G:
                              }

                              boolean var14 = false;
                              byte var13 = this.Rh;
                              var10000 = Integer.signum(var13);
                              if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.f != a4.r.G) {
                                 this.e(this.s(var1));
                              }
                           }
                        }
                     }
                  }
               }
            }
         } else {
            this.y("the world or the player changed");
         }
      }
   }

   private String s(long var1) {
      return switch (this.f) {
         case g -> "Waiting for /rtp (" + C(this.RH.N(var1)) + ")";
         default -> this.f.k;
         case X -> this.RC == a4.a.X
            ? "Aiming a pearl at the anchor"
            : (
               this.RC == a4.a.I
                  ? "Pearling to an anchor"
                  : (
                     this.h.A() != null
                        ? "Digging toward the anchor at " + this.RB.toShortString()
                        : (this.RB == null ? this.f.k : "Going to the anchor at " + this.RB.toShortString() + ", " + this.q(this.RB) + " blocks")
                  )
            );
         case v -> this.RB == null ? this.f.k : "Mining the anchor at " + this.RB.toShortString();
         case J -> "Stashing anchors in the ender chest (" + (this.Ra + 1) + "/6)";
      };
   }

   private int q(BlockPos var1) {
      return (int)Math.sqrt(mc.player.distanceToSqr(Vec3.atCenterOf(var1)));
   }

   private String q() {
      if (p_.V()) {
         return "Freecam is on";
      } else if (this.q()) {
         return "you are steering";
      } else {
         return c6.D() instanceof AbstractContainerScreen && !this.X() ? "an inventory is open" : null;
      }
   }

   private boolean q() {
      return mc.isWindowActive()
         && c6.D() == null
         && (
            c5.J(mc.options.keyUp)
               || c5.J(mc.options.keyDown)
               || c5.J(mc.options.keyLeft)
               || c5.J(mc.options.keyRight)
               || c5.J(mc.options.keyJump)
               || c5.J(mc.options.keyShift)
         );
   }

   private boolean f() {
      return this.f == a4.r.g
         || this.f == a4.r.s
         || this.f == a4.r.w
         || this.f == a4.r.b
         || this.f == a4.r.X && !this.h.i()
         || this.f == a4.r.v && mc.player.getFoodData().getFoodLevel() <= 6;
   }

   private boolean P() {
      return this.f == a4.r.w || this.f == a4.r.b || this.f == a4.r.X || this.f == a4.r.v || this.f == a4.r.I;
   }

   private void l(a4.r var1, String var2) {
      if (var1 != this.f) {
         com.larpclient.h.U(this.getName(), "{} -> {} ({})", this.f, var1, var2);
         this.f = var1;
         this.Rq = this.Rd;
         this.RP = System.currentTimeMillis();
      }
   }

   private void l(long var1) {
      if (this.RE) {
         this.C("mined every anchor it can reach around you (singleplayer has no /rtp)");
      } else if (!this.RH.z(var1)) {
         this.l(a4.r.g, "rtp not ready");
      } else {
         this.t();
         if (!mc.player.onGround() && !mc.player.isInWater()) {
            this.e("Landing before /rtp");
         } else {
            this.Rn = this.Rb.z(this.RW.getValue());
            mc.player.connection.sendCommand("rtp " + this.Rn);
            this.RH.V(var1);
            this.r = var1;
            this.RN = (boolean)0;
            this.t++;
            this.RX = mc.player.position();
            com.larpclient.h.A(this.getName(), "Sent /rtp {} ({} so far)", this.Rn, this.t);
            this.l(a4.r.r, "rtp " + this.Rn);
         }
      }
   }

   private void E(long var1) {
      if (this.RH.X(var1)) {
         this.Rb.v(this.Rn);
         if (this.RH.d(var1)) {
            this.y("/rtp did not answer 5 times in a row");
         } else {
            com.larpclient.h.A(this.getName(), "/rtp {} got no answer; trying again in {}", this.Rn, C(this.RH.N(var1)));
            this.l(a4.r.g, "rtp silent");
         }
      } else {
         if (!this.RH.V()) {
            this.l(a4.r.g, "rtp answered");
         }
      }
   }

   private void y(long var1) {
      Set var4 = null;

      String var3;
      while ((var3 = this.i.poll()) != null) {
         if (var4 == null) {
            var4 = this.Z();
         }

         Set var5 = var4;
         zi.l var6 = zi.p(var3, var1x -> var5.contains(var1x.toLowerCase(Locale.ROOT)));
         if (this.f == a4.r.u && zi.c(var3).contains("sold")) {
            com.larpclient.h.A(this.getName(), "Sell reply: {}", zi.c(var3));
         }

         if (var6.kind() != zi.e.s) {
            com.larpclient.h.A(this.getName(), "Server reply {} ({} ms wait): {}", var6.kind(), var6.waitMs(), zi.c(var3));
            this.w(var6, var1);
            if (!this.Rh || this.f == a4.r.G) {
               return;
            }
         }
      }
   }

   private Set<String> Z() {
      HashSet var1 = new HashSet();
      if (mc.getConnection() != null) {
         for (PlayerInfo var3 : mc.getConnection().getOnlinePlayers()) {
            var1.add(var3.getProfile().name().toLowerCase(Locale.ROOT));
         }
      }

      return var1;
   }

   private void w(zi.l var1, long var2) {
      boolean var4 = this.RH.V();
      int var5 = var2 - this.r < 2000L ? 1 : 0;
      switch (var1.kind()) {
         case C:
         case A:
            this.RH.M(var2);
            break;
         case f:
            if (var4) {
               this.RN = (boolean)1;
               this.RO = var2;
               if (this.f == a4.r.r) {
                  this.l(a4.r.i, "countdown");
               }
            }
            break;
         case J:
            if (!var4) {
               return;
            }

            this.RH.o(var2, var1.waitMs());
            com.larpclient.h.A(this.getName(), "/rtp is cooling down; next in {}", C(this.RH.N(var2)));
            this.p("rtp cooldown");
            break;
         case L:
            if (var4) {
               this.RH.M(var2, 10000L);
               this.p("combat tagged");
            }
            break;
         case X:
            if (var4 && var5 != 0) {
               this.RH.M(var2, 1500L);
               this.p("chat limit");
            }
            break;
         case T:
            if (var4 && var5 != 0) {
               this.y("this server has no /rtp");
            }
            break;
         case R:
         case V:
            if (var4 || this.RN) {
               this.RH.M(var2, 3000L);
               this.RN = (boolean)0;
               this.p(var1.kind() == zi.e.V ? "unsafe spot" : "teleport cancelled");
            }
            break;
         case q:
            if (var4) {
               this.Rb.v(this.Rn);
               this.RH.M(var2, 3000L);
               this.p("region down");
            }
            break;
         case M:
            if (this.f == a4.r.J) {
               this.RV = "the area is protected";
            } else if ((this.f == a4.r.v || this.f == a4.r.X) && var2 - this.Kh <= 3000L) {
               this.t();
               com.larpclient.h.A(this.getName(), "The server says {} is protected", this.RB == null ? "this spot" : this.RB.toShortString());
               this.l(a4.r.t, "protected area");
            }
         case r:
         case s:
      }
   }

   private void p(String var1) {
      if (this.f == a4.r.r || this.f == a4.r.i || this.f == a4.r.x) {
         this.l(a4.r.g, var1);
      }
   }

   private void H(long var1) {
      Vec3 var3 = mc.player.position();
      Vec3 var4 = this.RX;
      this.RX = var3;
      if (var4 != null && (this.RH.V() || this.RN)) {
         if (var3.distanceTo(var4) > 64.0) {
            this.M(var1, String.format(Locale.ROOT, "%.0f blocks", var3.distanceTo(var4)));
         }
      }
   }

   private void M(long var1, String var3) {
      this.RH.x(var1);
      this.Rb.i(this.Rn);
      this.RI++;
      this.RN = (boolean)0;
      this.d();
      this.t();
      this.x = var1;
      this.RX = mc.player.position();
      com.larpclient.h.A(this.getName(), "Teleported ({}) to {}; spot {}", var3, mc.player.blockPosition().toShortString(), this.RI);
      this.l(a4.r.s, "teleported");
   }

   private void R(String var1) {
      this.t();
      this.l();
      com.larpclient.h.A(this.getName(), "Leaving this spot: {}", var1);
      if (this.RE) {
         this.C("mined every anchor it can reach around you (singleplayer has no /rtp)");
      } else {
         this.l(this.RH.z(System.currentTimeMillis()) ? a4.r.N : a4.r.g, var1);
      }
   }

   private int P() {
      return Math.clamp(mc.options.getEffectiveRenderDistance(), 2, 12);
   }

   private void X(long var1) {
      if (mc.level.dimension() != Level.OVERWORLD) {
         if (var1 - this.RP > 10000L) {
            this.y("the /rtp sent you out of the overworld");
         }
      } else {
         int var3 = !mc.player.onGround() && !mc.player.isInWater() ? 0 : 1;
         int var4 = this.P();
         int var5 = 0;
         int var6 = 0;
         int var7 = mc.player.getBlockX() >> 4;
         int var8 = mc.player.getBlockZ() >> 4;

         for (int var9 = -var4; var9 <= var4; var9++) {
            for (int var10 = -var4; var10 <= var4; var10++) {
               var6++;
               if (mc.level.getChunkSource().getChunk(var7 + var9, var8 + var10, ChunkStatus.FULL, false) != null) {
                  var5++;
               }
            }
         }

         double var11 = var6 == 0 ? 0.0 : (double)var5 / var6;
         if (var3 != 0 && var1 - this.RP >= 1000L && var11 >= 0.9 || var1 - this.RP > 8000L) {
            com.larpclient.h.U(this.getName(), "Landed; {}% of {} chunks loaded", Math.round(var11 * 100.0), var6);
            this.l(a4.r.w, "landed");
         }
      }
   }

   private int k(Map<Long, Integer> var1) {
      int var2 = this.P();
      int var3 = 0;
      int var4 = mc.player.getBlockX() >> 4;
      int var5 = mc.player.getBlockZ() >> 4;

      for (int var6 = -var2; var6 <= var2; var6++) {
         for (int var7 = -var2; var7 <= var2; var7++) {
            LevelChunk var8 = mc.level.getChunkSource().getChunk(var4 + var6, var5 + var7, ChunkStatus.FULL, false);
            if (var8 != null) {
               var3++;
               LevelChunkSection[] var9 = var8.getSections();

               for (int var10 = 0; var10 < var9.length; var10++) {
                  final LevelChunkSection var11 = var9[var10];
                  if (var11 != null && !var11.hasOnlyAir()) {
                     tz.I(new tz.e() {
                        @Override
                        public boolean maybeHasAnchor() {
                           return var11.maybeHas(var0 -> var0.is(Blocks.RESPAWN_ANCHOR));
                        }

                        @Override
                        public BlockState state(int var1, int var2x, int var3x) {
                           return var11.getBlockState(var1, var2x, var3x);
                        }
                     }, var4 + var6 << 4, var8.getSectionYFromSectionIndex(var10) << 4, var5 + var7 << 4, var1);
                  }
               }
            }
         }
      }

      return var3;
   }

   private void m() {
      this.g.clear();
      int var1 = this.k(this.g);
      this.g.keySet().removeAll(this.Rp);
      com.larpclient.h.A(this.getName(), "{} anchors in {} loaded chunks", this.g.size(), var1);
      if (this.g.isEmpty()) {
         this.RA++;
         this.R("no anchors here");
      } else {
         this.C(a4.t.P, this.g.size() + (this.g.size() == 1 ? " anchor" : " anchors") + " in sight of the chunks here");
         this.l(a4.r.b, this.g.size() + " anchors");
      }
   }

   private void L(long var1) {
      this.k(this.g);
      this.g.keySet().removeAll(this.Rp);
      Vec3 var3 = mc.player.getEyePosition();
      List var4 = this.g
         .keySet()
         .stream()
         .sorted(Comparator.comparingDouble(var1x -> var3.distanceToSqr(zf.W(var1x) + 0.5, zf.d(var1x) + 0.5, zf.F(var1x) + 0.5)))
         .limit(48L)
         .toList();
      BlockPos var5 = null;
      double var6 = Double.MAX_VALUE;
      int var8 = mc.player.getBlockY();
      c6 var9 = this.p((BlockPos)null);

      for (long var11 : var4) {
         var9.I(new BlockPos(zf.W(var11), zf.d(var11), zf.F(var11)));
      }

      tz.a var21 = this.z();

      for (long var12 : var4) {
         BlockPos var14 = new BlockPos(zf.W(var12), zf.d(var12), zf.F(var12));
         if (!mc.level.getBlockState(var14).is(Blocks.RESPAWN_ANCHOR)) {
            this.g.remove(var12);
         } else {
            double var15 = Math.hypot(var14.getX() + 0.5 - mc.player.getX(), var14.getZ() + 0.5 - mc.player.getZ());
            tz.v var17 = var15 <= 48.0 ? tz.V(var9, var14.getX(), var14.getY(), var14.getZ(), var21) : null;
            String var18 = var17 == null
               ? (this.KP ? "protected" : null)
               : tz.C(var9, var14.getX(), var14.getY(), var14.getZ(), var8, var17, this.Rz.getValue(), this.KP);
            if (var18 != null) {
               this.S(var12, var18);
            } else {
               double var19 = Math.sqrt(var3.distanceToSqr(Vec3.atCenterOf(var14)))
                  + (var17 != null && var17.exposed().isEmpty() ? 6 + 3 * var17.dig().getFirst().digs() : 0);
               if (var19 < var6) {
                  var6 = var19;
                  var5 = var14;
               }
            }
         }
      }

      if (var5 == null) {
         this.R(this.g.isEmpty() ? "no anchors left here" : "no safe anchors left here");
      } else {
         this.d(var5, var1);
      }
   }

   private void S(long var1, String var3) {
      this.Rp.add(var1);
      this.g.remove(var1);
      this.X.merge(var3, 1, Integer::sum);
      com.larpclient.h.U(this.getName(), "Skipping the anchor at {} {} {}: {}", zf.W(var1), zf.d(var1), zf.F(var1), var3);
   }

   private void d(BlockPos var1, long var2) {
      this.l();
      this.RB = var1.immutable();
      this.RY = this.g.getOrDefault(zf.O(var1.getX(), var1.getY(), var1.getZ()), 0);
      this.G = var2;
      this.Rv = this.h.R();
      com.larpclient.h.A(
         this.getName(),
         "Going for the anchor at {} ({} charges, {} blocks away)",
         this.RB.toShortString(),
         this.RY,
         (int)Math.sqrt(mc.player.distanceToSqr(Vec3.atCenterOf(this.RB)))
      );
      this.l(a4.r.X, "target " + this.RB.toShortString());
   }

   private void x(String var1) {
      if (this.RB != null) {
         this.S(zf.O(this.RB.getX(), this.RB.getY(), this.RB.getZ()), var1);
         com.larpclient.h.A(this.getName(), "Gave up on the anchor at {}: {}", this.RB.toShortString(), var1);
      }

      this.t();
      this.l();
      this.l(a4.r.b, var1);
   }

   private void F(long var1) {
      if (this.RB == null) {
         this.l(a4.r.b, "no target");
      } else if (!mc.level.getBlockState(this.RB).is(Blocks.RESPAWN_ANCHOR)) {
         this.g.remove(zf.O(this.RB.getX(), this.RB.getY(), this.RB.getZ()));
         com.larpclient.h.U(this.getName(), "The anchor at {} is gone", this.RB.toShortString());
         this.l();
         this.l(a4.r.b, "anchor gone");
      } else if (!this.O()) {
         this.K();
      } else if (this.KW < 3 && this.h.R() - this.Rv <= 40 && var1 - this.G <= 120000L) {
         double var3 = Math.hypot(this.RB.getX() + 0.5 - mc.player.getX(), this.RB.getZ() + 0.5 - mc.player.getZ());
         if (!this.R_ && var3 > 28.0) {
            this.R_ = (boolean)1;
         } else if (this.R_ && var3 <= 22.0) {
            this.R_ = (boolean)0;
            this.h.H();
         }

         if (this.RC == a4.a.r && (!this.R_ || !this.j.getValue()) || !this.h()) {
            if (this.h.i() && (!this.R_ || this.Rd - this.c <= 100) && (!this.h.M() || this.h.e().size() > 4 || this.Rd - this.c <= 10)
               || this.b()
               || this.h.i()) {
               gu.r var5 = this.h.S(this.B(), this::K, true);
               this.s();
               switch (var5) {
                  case c:
                     this.h.H();
                     if (!this.R_ && this.Rc.contains(this.r())) {
                        this.S(var1);
                     }
                     break;
                  case J:
                     this.KW++;
                     com.larpclient.h.A(this.getName(), "Route to {} stuck ({}): {}", this.RB.toShortString(), this.KW, this.h.d());
                     this.h.H();
               }
            }
         }
      } else {
         this.x(this.KW >= 3 ? "no way there" : (this.h.R() - this.Rv > 40 ? "too much digging" : "took too long to reach"));
      }
   }

   private boolean b() {
      this.c = this.Rd;
      c6 var1 = this.p(this.RB);
      BlockPos var2 = this.a();
      zf.v var3;
      if (this.R_) {
         zf.s var4 = zf.s.around(var2.getX(), var2.getY(), var2.getZ(), this.RB.getX(), this.RB.getY(), this.RB.getZ(), 16, 24, 24);
         zf.z var5 = new zf.z(16000, this.Rz.getValue(), 3, true, 3.0);
         var3 = zf.B(var1, var2.getX(), var2.getY(), var2.getZ(), zf.f.j(this.RB.getX() + 0.5, this.RB.getY(), this.RB.getZ() + 0.5, 18.0, 24.0), var4, var5);
      } else {
         tz.v var10 = tz.V(var1, this.RB.getX(), this.RB.getY(), this.RB.getZ(), this.z());
         String var11 = tz.C(var1, this.RB.getX(), this.RB.getY(), this.RB.getZ(), var2.getY(), var10, this.Rz.getValue(), this.KP);
         if (var11 != null) {
            this.x(var11);
            return false;
         }

         this.Rc.clear();

         for (tz.b var7 : var10.usable(this.Rz.getValue())) {
            long var8 = zf.O(var7.x(), var7.y(), var7.z());
            if (!this.Kg.contains(var8)) {
               this.Rc.add(var8);
            }
         }

         if (this.Rc.isEmpty()) {
            this.x("no clear view from any standing spot");
            return false;
         }

         if (this.Rc.contains(this.r())) {
            this.S(System.currentTimeMillis());
            return false;
         }

         zf.s var12 = zf.s.around(var2.getX(), var2.getY(), var2.getZ(), this.RB.getX(), this.RB.getY(), this.RB.getZ(), 10, 12, 12);
         zf.z var13 = new zf.z(16000, this.Rz.getValue(), 3, false, 1.0);
         var3 = zf.B(var1, var2.getX(), var2.getY(), var2.getZ(), zf.f.k(this.Rc, this.RB.getX(), this.RB.getY() - 1, this.RB.getZ(), 1.0), var12, var13);
      }

      if (!var3.found()) {
         this.KW++;
         if (var3.explored() <= 1) {
            this.T(var2);
         }

         com.larpclient.h.A(this.getName(), "No route toward {} ({} cells searched, try {})", this.RB.toShortString(), var3.explored(), this.KW);
         return false;
      } else if (var3.steps().isEmpty()) {
         if (this.R_) {
            this.R_ = (boolean)0;
         }

         return false;
      } else {
         this.h.B(var3.steps(), var3.complete());
         com.larpclient.h.U(
            this.getName(),
            "Route of {} steps ({} to dig{}) toward {}",
            var3.steps().size(),
            var3.digs(),
            var3.complete() ? "" : ", partial",
            this.RB.toShortString()
         );
         return true;
      }
   }

   private BlockPos a() {
      return BlockPos.containing(mc.player.getX(), mc.player.getY() + 0.2, mc.player.getZ());
   }

   private void T(BlockPos var1) {
      Function var2 = var0 -> BuiltInRegistries.BLOCK.getKey(mc.level.getBlockState(var0).getBlock()).getPath()
         + (mc.level.getFluidState(var0).isEmpty() ? "" : "+fluid");
      com.larpclient.h.A(
         this.getName(),
         "Route could not start at {} (y {}): feet {}, head {}, below {}, in water {}, on ground {}",
         var1.toShortString(),
         String.format(Locale.ROOT, "%.3f", mc.player.getY()),
         var2.apply(var1),
         var2.apply(var1.above()),
         var2.apply(var1.below()),
         mc.player.isInWater(),
         mc.player.onGround()
      );
   }

   private long r() {
      BlockPos var1 = this.a();
      return zf.O(var1.getX(), var1.getY(), var1.getZ());
   }

   private c6 p(BlockPos var1) {
      c6 var2 = new c6(this::K, this.j());
      if (var1 != null) {
         var2.I(var1);
      }

      for (LivingEntity var4 : mc.level
         .getEntitiesOfClass(
            LivingEntity.class, mc.player.getBoundingBox().inflate(24.0), var0 -> var0 instanceof Enemy && var0.isAlive() && mc.player.hasLineOfSight(var0)
         )) {
         var2.Y(var4.getX(), var4.getY(), var4.getZ(), var4 instanceof Creeper ? 7.0 : 3.0);
      }

      return var2;
   }

   private tz.a z() {
      return (var0, var2, var4, var6, var7, var8) -> {
         Vec3 var9 = new Vec3(var0, var2, var4);
         BlockPos var10 = new BlockPos(var6, var7, var8);
         Vec3 var11 = Vec3.atCenterOf(var10);

         for (int var12 = -1; var12 < 6; var12++) {
            Vec3 var13 = var12 < 0 ? var11 : var11.add(Vec3.atLowerCornerOf(Direction.values()[var12].getUnitVec3i()).scale(0.49));
            BlockHitResult var14 = mc.level.clip(new ClipContext(var9, var13, Block.OUTLINE, Fluid.NONE, mc.player));
            if (var14.getType() != Type.BLOCK || var14.getBlockPos().equals(var10)) {
               return true;
            }
         }

         return false;
      };
   }

   private void S(long var1) {
      this.h.H();
      this.q = 0;
      this.y = 0;
      this.F = (boolean)0;
      this.R7 = this.H(Items.RESPAWN_ANCHOR);
      this.Kh = var1;
      this.l(a4.r.v, "at a standing spot");
   }

   private void i(long var1) {
      if (this.RB == null) {
         this.l(a4.r.b, "no target");
      } else {
         BlockState var3 = mc.level.getBlockState(this.RB);
         if (!var3.is(Blocks.RESPAWN_ANCHOR)) {
            l0.h(this);
            if (!this.F) {
               this.F = (boolean)1;
               this.Rm = 0;
            }

            if (++this.Rm >= 3) {
               this.RZ = this.Rd;
               this.g.remove(zf.O(this.RB.getX(), this.RB.getY(), this.RB.getZ()));
               this.l(a4.r.I, "anchor broken");
            }
         } else {
            if (this.F) {
               this.F = (boolean)0;
               if (++this.R >= 2) {
                  com.larpclient.h.A(this.getName(), "The anchor at {} came back twice", this.RB.toShortString());
                  this.l(a4.r.t, "anchor came back twice");
                  return;
               }
            }

            if (!this.O()) {
               this.K();
            } else if (++this.q > 900) {
               this.x("took too long to mine");
            } else {
               double var4 = this.B();
               if (mc.player.getEyePosition().distanceTo(Vec3.atCenterOf(this.RB)) > var4 + 0.2) {
                  this.l(a4.r.X, "out of reach");
               } else if (this.j() && this.U(this.RB)) {
                  this.x("water or lava next to it and your pickaxe may cut 3x3");
               } else {
                  BlockPos var6 = this.RB;
                  this.r();
                  boolean var7 = l0.z(this, var6, var4, true, 12.0F, this::G, () -> !this.q());
                  if (var7) {
                     this.y = 0;
                  } else {
                     if (++this.y > 20) {
                        this.Kg.add(this.r());
                        this.y = 0;
                        if (this.Kg.size() >= 3) {
                           this.x("no clear view");
                           return;
                        }

                        com.larpclient.h.U(
                           this.getName(),
                           "No clear view of {} from {}; trying another spot",
                           this.RB.toShortString(),
                           mc.player.blockPosition().toShortString()
                        );
                        this.h.H();
                        this.l(a4.r.X, "lost sight");
                     }
                  }
               }
            }
         }
      }
   }

   private void X() {
      this.I();
      int var1 = this.H(Items.RESPAWN_ANCHOR);
      if (var1 > this.R7) {
         int var4 = var1 - this.R7;
         this.RU += var4;
         com.larpclient.h.A(
            this.getName(), "Picked up {} anchor{} ({} total){}", var4, var4 == 1 ? "" : "s", this.RU, this.RY > 0 ? ", it had " + this.RY + " charges" : ""
         );
         this.C(a4.t.O, "+" + var4 + " anchor" + (var4 == 1 ? "" : "s") + " (" + this.RU + " total)");
         this.t();
         this.l();
         if (this.I.getValue() && !this.RE && System.currentTimeMillis() >= this.RR) {
            this.D();
         } else {
            int var5 = this.RL.getValue().intValue();
            if (var5 > 0 && this.RU >= var5) {
               this.C("collected " + this.RU + " anchors");
            } else {
               this.l(a4.r.b, "picked up");
            }
         }
      } else {
         int var2 = this.Rd - this.RZ;
         if (var2 > 200) {
            com.larpclient.h.A(this.getName(), "Lost the drop of the anchor at {}", this.RB == null ? "?" : this.RB.toShortString());
            this.t();
            this.l();
            this.l(a4.r.b, "drop lost");
         } else if (var2 >= 10) {
            ItemEntity var3 = this.o(Items.RESPAWN_ANCHOR, 8.0);
            if (var3 != null) {
               this.J(var3);
            }
         }
      }
   }

   private void C(String var1) {
      com.larpclient.h.A(this.getName(), "Done: {}", var1);
      this.C(a4.t.O, "Done, " + var1 + ".");
      this.p = "Done: " + var1;
      this.e(this.p);
      this.f = a4.r.G;
      this.disable();
   }

   private ItemEntity o(Item var1, double var2) {
      List var4 = mc.level.getEntitiesOfClass(ItemEntity.class, mc.player.getBoundingBox().inflate(var2), var1x -> var1x.isAlive() && var1x.getItem().is(var1));
      return var4.stream().min(Comparator.comparingDouble(var0 -> var0.distanceToSqr(mc.player))).orElse(null);
   }

   private void J(ItemEntity var1) {
      BlockPos var2 = BlockPos.containing(var1.position());
      BlockPos var3 = this.a();
      if (!(Math.hypot(var1.getX() - mc.player.getX(), var1.getZ() - mc.player.getZ()) < 0.6)) {
         if (!this.h.i() || this.Rd - this.c > 20) {
            this.c = this.Rd;
            zf.s var4 = zf.s.around(var3.getX(), var3.getY(), var3.getZ(), var2.getX(), var2.getY(), var2.getZ(), 6, 6, 4);
            zf.v var5 = zf.B(
               this.p((BlockPos)null), var3.getX(), var3.getY(), var3.getZ(), zf.f.j(var1.getX(), var2.getY(), var1.getZ(), 0.5, 1.0), var4, zf.z.walking(4000)
            );
            if (!var5.found() || var5.steps().isEmpty()) {
               this.E(var1.position());
               return;
            }

            this.h.M(var5.steps());
         }

         gu.r var6 = this.h.S(this.B(), this::K, false);
         this.s();
         if (var6 == gu.r.c || var6 == gu.r.J) {
            this.h.H();
            this.E(var1.position());
         }
      }
   }

   private void E(Vec3 var1) {
      Vec3 var2 = var1.subtract(mc.player.position()).multiply(1.0, 0.0, 1.0);
      double var3 = var2.length();
      double var5 = var3 < 1.0E-6 ? 0.0 : mc.player.getDeltaMovement().multiply(1.0, 0.0, 1.0).dot(var2.normalize());
      this.RT = (boolean)1;
      boolean var7 = this.R2.u(var3, var5, ta.E(this));
      this.Z = var7 ? var2 : Vec3.ZERO;
   }

   private void s() {
      this.Z = this.h.q();
      this.RQ = this.h.t();
      this.RT = this.h.o();
      this.E = this.h.O();
   }

   private void K() {
      this.t();
      if (this.Rr.getValue() && this.a() && this.H(Items.RESPAWN_ANCHOR) > 0) {
         this.Rx = this.RB != null ? a4.r.X : a4.r.b;
         this.Ra = 0;
         this.Ry = this.Rd;
         this.R8 = 0;
         this.RV = null;
         if (this.R4 != null && !mc.level.getBlockState(this.R4).is(Blocks.ENDER_CHEST)) {
            this.R4 = null;
         }

         this.d = this.H(Items.RESPAWN_ANCHOR);
         com.larpclient.h.A(this.getName(), "Inventory full with {} anchors; stashing them in the ender chest", this.d);
         this.l(a4.r.J, "inventory full");
      } else {
         this.y(this.H(Items.RESPAWN_ANCHOR) > 0 ? "your inventory is full" : "your inventory is full of other things; make room for anchors");
      }
   }

   private void a() {
      if (this.RV != null && this.Ra <= 1) {
         this.r(this.RV);
      } else if (this.Rd - this.Ry > 300) {
         this.r("step " + this.Ra + " took too long");
      } else if (mc.player.onGround()) {
         switch (this.Ra) {
            case 0:
               this.k();
               break;
            case 1:
               this.Y();
               break;
            case 2:
               this.n();
               break;
            case 3:
               this.Z();
               break;
            case 4:
               this.w();
               break;
            case 5:
               this.b();
               break;
            default:
               this.r("unknown step");
         }
      }
   }

   private void I(int var1) {
      this.Ra = var1;
      this.Ry = this.Rd;
   }

   private void k() {
      if (this.R4 != null && mc.level.getBlockState(this.R4).is(Blocks.ENDER_CHEST)) {
         this.I(1);
      } else {
         if (this.R4 == null) {
            BlockPos var1 = this.a();
            int[] var2 = tz.J(this.p(this.RB), var1.getX(), var1.getY(), var1.getZ());
            if (var2 == null) {
               this.r("no room to place the ender chest");
               return;
            }

            this.R4 = new BlockPos(var2[0], var2[1], var2[2]);
         }

         int var4 = hk.J(var0 -> var0.is(Items.ENDER_CHEST));
         if (var4 < 0) {
            this.r("no ender chest in the hotbar");
         } else if (!this.S(var4)) {
            this.e("Taking out the ender chest");
         } else {
            BlockPos var5 = this.R4;
            boolean var3 = t1.K(
               this,
               var5,
               Items.ENDER_CHEST,
               this.B(),
               true,
               true,
               () -> t1.t(var5),
               var2x -> com.larpclient.h.U(this.getName(), "Ender chest at {} {}", var5.toShortString(), var2x ? "placed" : "rejected")
            );
            if (!var3 && this.Rd - this.Ry > 60) {
               this.R4 = null;
               this.Ry = this.Rd;
               if (++this.R8 >= 3) {
                  this.r("could not place the ender chest");
               }
            }
         }
      }
   }

   private void Y() {
      if (mc.player.containerMenu instanceof ChestMenu) {
         this.I(2);
      } else if (!mc.level.getBlockState(this.R4).is(Blocks.ENDER_CHEST)) {
         if (++this.R8 >= 3) {
            this.r("the ender chest disappeared");
         } else {
            this.R4 = null;
            this.I(0);
         }
      } else if (c6.D() == null) {
         ItemStack var1 = mc.player.getMainHandItem();
         if (this.Rd % 10 == 0) {
            t1.G(
               this,
               this.R4,
               var1.getItem(),
               this.B(),
               true,
               false,
               () -> c6.D() == null,
               var1x -> com.larpclient.h.U(this.getName(), "Opening the ender chest {}", var1x ? "sent" : "refused")
            );
         }
      }
   }

   private void n() {
      if (!(mc.player.containerMenu instanceof ChestMenu var1)) {
         this.I(1);
      } else if (!(mc.player.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4)) {
         int var7 = var1.getRowCount() * 9;
         int var3 = -1;
         byte var4 = 0;

         for (int var5 = 0; var5 < var1.slots.size(); var5++) {
            ItemStack var6 = ((Slot)var1.slots.get(var5)).getItem();
            if (var5 < var7) {
               if (var6.isEmpty() || var6.is(Items.RESPAWN_ANCHOR) && var6.getCount() < var6.getMaxStackSize()) {
                  var4 = 1;
               }
            } else if (var3 < 0 && var6.is(Items.RESPAWN_ANCHOR)) {
               var3 = var5;
            }
         }

         if (var3 >= 0 && var4 != 0) {
            if (this.Rd - this.A >= 4) {
               this.A = this.Rd;
               mc.gameMode.handleContainerInput(var1.containerId, var3, 0, ContainerInput.QUICK_MOVE, mc.player);
            }
         } else {
            int var8 = Math.max(0, this.d - this.H(Items.RESPAWN_ANCHOR));
            if (var8 == 0) {
               this.RV = var3 < 0 ? "no anchors to store" : "the ender chest is full";
            }

            com.larpclient.h.A(this.getName(), "Stored {} anchors in the ender chest{}", var8, this.RV == null ? "" : " (" + this.RV + ")");
            this.I(3);
         }
      }
   }

   private void Z() {
      if (mc.player.containerMenu instanceof ChestMenu) {
         mc.player.closeContainer();
      } else {
         this.M = this.H(Items.ENDER_CHEST);
         this.I(4);
      }
   }

   private void w() {
      if (!mc.level.getBlockState(this.R4).is(Blocks.ENDER_CHEST)) {
         this.RZ = this.Rd;
         this.I(5);
      } else if (!this.z(new ItemStack(Items.ENDER_CHEST))) {
         this.r("no room to pick the ender chest back up");
      } else if (this.W() < 0) {
         this.r("no Silk Touch pickaxe in the hotbar");
      } else {
         BlockPos var1 = this.R4;
         if (!l0.z(this, var1, this.B(), true, 12.0F, var1x -> this.W(), () -> true) && this.Rd - this.Ry > 60) {
            this.r("cannot reach the ender chest to take it back");
         }
      }
   }

   private void b() {
      if (this.H(Items.ENDER_CHEST) > this.M) {
         l0.h(this);
         int var2 = Math.max(0, this.d - this.H(Items.RESPAWN_ANCHOR));
         if (this.RV != null && var2 == 0) {
            this.y("your inventory is full and " + this.RV);
         } else {
            this.C(a4.t.O, var2 + " anchors stored in your ender chest");
            this.R4 = null;
            this.l(this.Rx, "stashed");
         }
      } else {
         ItemEntity var1 = this.o(Items.ENDER_CHEST, 8.0);
         if (var1 != null && this.Rd - this.RZ < 200) {
            this.J(var1);
         } else {
            if (this.Rd - this.RZ > 60) {
               this.r("lost the ender chest");
            }
         }
      }
   }

   private void r(String var1) {
      this.t();
      if (mc.player.containerMenu instanceof ChestMenu) {
         mc.player.closeContainer();
      }

      com.larpclient.h.A(this.getName(), "Ender chest stash failed: {}", var1);
      this.y("your inventory is full (" + var1 + ")");
   }

   private boolean S(int var1) {
      Inventory var2 = mc.player.getInventory();
      if (var2.getSelectedSlot() != var1) {
         l0.h(this);
         var2.setSelectedSlot(var1);
         this.RM = var1;
         this.T = this.Rd;
         return false;
      }

      if (this.RM != var1) {
         this.RM = var1;
         this.T = this.Rd;
      }

      return this.Rd - this.T >= 3;
   }

   private void D() {
      this.K7 = 0;
      this.a = this.Rd;
      this.Y = -1;
      this.L = this.H(Items.RESPAWN_ANCHOR);
      com.larpclient.h.A(this.getName(), "Selling {} anchors with /sell", this.L);
      this.l(a4.r.u, "selling");
   }

   private void z(int var1) {
      this.K7 = var1;
      this.a = this.Rd;
   }

   private void f() {
      if (this.Rd - this.a > 200) {
         this.h("the Sell menu took too long (step " + (this.K7 + 1) + ")");
      } else {
         switch (this.K7) {
            case 0:
               if (!mc.player.onGround() || mc.player.containerMenu != mc.player.inventoryMenu || c6.D() instanceof AbstractContainerScreen) {
                  return;
               }

               if (this.H(Items.RESPAWN_ANCHOR) == 0) {
                  this.j();
                  return;
               }

               mc.player.connection.sendCommand("sell");
               this.z(1);
               break;
            case 1:
               ChestMenu var12 = this.j();
               if (var12 == null) {
                  return;
               }

               int var13 = var12.getRowCount() * 9;
               this.Y = -1;

               for (int var14 = var13 - 9; var14 < var13; var14++) {
                  ItemStack var4 = var12.getSlot(var14).getItem();
                  if (!var4.isEmpty() && (var4.has(DataComponents.CUSTOM_NAME) || var4.has(DataComponents.ITEM_NAME)) && var4.getCount() == 1) {
                     String var5 = BuiltInRegistries.ITEM.getKey(var4.getItem()).toString();
                     ItemLore var6 = (ItemLore)var4.get(DataComponents.LORE);
                     ArrayList var7 = new ArrayList();
                     if (var6 != null) {
                        for (Component var9 : var6.lines()) {
                           var7.add(var9.getString());
                        }
                     }

                     if (mp.A(var5, var4.getHoverName().getString(), var7, var14, var13)) {
                        if (this.Y >= 0) {
                           this.h("the Sell menu has more than one Sell button");
                           return;
                        }

                        this.Y = var14;
                     }
                  }
               }

               if (this.Y < 0) {
                  this.h("the Sell menu has no Sell button in its bottom row");
                  return;
               }

               for (int var15 = 0; var15 < var13 - 9; var15++) {
                  if (!var12.getSlot(var15).getItem().isEmpty()) {
                     this.h("the Sell menu already holds items");
                     return;
                  }
               }

               this.z(2);
               break;
            case 2:
               ChestMenu var11 = this.j();
               if (var11 == null) {
                  this.h("the Sell menu closed");
                  return;
               }

               if (mc.player.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4 || !var11.getCarried().isEmpty()) {
                  return;
               }

               int var2 = var11.getRowCount() * 9;

               for (int var3 = var2; var3 < var11.slots.size(); var3++) {
                  if (((Slot)var11.slots.get(var3)).getItem().is(Items.RESPAWN_ANCHOR)) {
                     if (this.Rd - this.W >= 4) {
                        this.W = this.Rd;
                        mc.gameMode.handleContainerInput(var11.containerId, var3, 0, ContainerInput.QUICK_MOVE, mc.player);
                     }

                     return;
                  }
               }

               if (this.n(var11) == 0) {
                  this.h("no anchors went into the Sell menu");
                  return;
               }

               this.z(3);
               break;
            case 3:
               ChestMenu var10 = this.j();
               if (var10 == null) {
                  this.h("the Sell menu closed before selling");
                  return;
               }

               if (this.Rd - this.W < 4 || !var10.getCarried().isEmpty()) {
                  return;
               }

               this.W = this.Rd;
               mc.gameMode.handleContainerInput(var10.containerId, this.Y, 0, ContainerInput.PICKUP, mc.player);
               this.z(4);
               break;
            default:
               ChestMenu var1 = this.j();
               if (var1 != null && this.n(var1) > 0) {
                  if (this.Rd - this.a > 100) {
                     this.h("the server did not take the anchors");
                  }

                  return;
               }

               if (var1 != null) {
                  mc.player.closeContainer();
               }

               this.j();
         }
      }
   }

   private int n(ChestMenu var1) {
      int var2 = 0;
      int var3 = var1.getRowCount() * 9;

      for (int var4 = 0; var4 < var3 - 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         if (var5.is(Items.RESPAWN_ANCHOR)) {
            var2 += var5.getCount();
         }
      }

      return var2;
   }

   private ChestMenu j() {
      return c6.D() instanceof ContainerScreen var1
            && mc.player.containerMenu instanceof ChestMenu var2
            && var1.getMenu() == var2
            && w4.A(var1.getTitle().getString(), "Sell")
         ? var2
         : null;
   }

   private void j() {
      int var1 = Math.max(0, this.L - this.H(Items.RESPAWN_ANCHOR));
      this.S += var1;
      com.larpclient.h.A(this.getName(), "Sold {} anchors ({} this run)", var1, this.S);
      if (var1 > 0) {
         this.C(a4.t.O, "Sold " + var1 + " anchor" + (var1 == 1 ? "" : "s") + " (" + this.S + " this run)");
      }

      int var2 = this.RL.getValue().intValue();
      if (var2 > 0 && this.RU >= var2) {
         this.C("collected " + this.RU + " anchors");
      } else {
         this.l(a4.r.b, "sold");
      }
   }

   private void h(String var1) {
      if (mc.player.containerMenu instanceof ChestMenu) {
         mc.player.closeContainer();
      }

      this.RR = System.currentTimeMillis() + 300000L;
      com.larpclient.h.A(this.getName(), "Selling stopped: {}; trying again in five minutes", var1);
      this.C(a4.t.X, "Could not sell anchors: " + var1);
      int var2 = this.RL.getValue().intValue();
      if (var2 > 0 && this.RU >= var2) {
         this.C("collected " + this.RU + " anchors");
      } else {
         this.l(a4.r.b, "sell failed");
      }
   }

   private boolean h() {
      LocalPlayer var1 = mc.player;
      switch (this.RC) {
         case X:
            if (this.Rd - this.s > 40 || !this.c()) {
               this.RC = a4.a.r;
               this.b = this.Rd + 60;
               return false;
            } else if (!ta.E(this)) {
               return true;
            } else {
               float[] var4 = this.R2.Q(var1.getYRot(), var1.getXRot(), this.J, this.V, false);
               j6.U(this, j6.l.o, var4[0], var4[1], 1.0);
               int var3 = Math.abs(Mth.wrapDegrees(this.J - var1.getYRot())) <= 0.75F && Math.abs(this.V - var1.getXRot()) <= 0.75F ? 1 : 0;
               if (var3 != 0 && var1.getDeltaMovement().horizontalDistanceSqr() < 1.0E-5) {
                  this.z();
               }

               return true;
            }
         case I:
            int var2 = var1.position().distanceTo(this.z) > 4.0 && var1.onGround() ? 1 : 0;
            if (var2 != 0 || this.Rd - this.s > 100) {
               com.larpclient.h.A(this.getName(), var2 != 0 ? "Pearl landed at {}" : "Pearl throw from {} did not arrive", var1.blockPosition().toShortString());
               this.RC = a4.a.r;
               this.b = this.Rd + 20;
               this.h.H();
            }

            return true;
         default:
            if (this.Rd < this.b || !this.c() || !var1.onGround()) {
               return false;
            } else if (!this.d()) {
               this.b = this.Rd + 100;
               return false;
            } else {
               this.t();
               this.RC = a4.a.X;
               this.s = this.Rd;
               return true;
            }
      }
   }

   private boolean c() {
      LocalPlayer var1 = mc.player;
      int var2 = hk.J(var0 -> var0.is(Items.ENDER_PEARL));
      return var2 >= 0
         && !var1.getCooldowns().isOnCooldown(var1.getInventory().getItem(var2))
         && var1.getArmorValue() >= 12
         && var1.getHealth() + var1.getAbsorptionAmount() >= 12.0F;
   }

   private boolean d() {
      c6 var1 = this.p(this.RB);
      tz.v var2 = tz.V(var1, this.RB.getX(), this.RB.getY(), this.RB.getZ(), this.z());
      ArrayList var3 = new ArrayList();

      for (tz.b var5 : var2.exposed()) {
         if (var3.size() >= 3) {
            break;
         }

         var3.add(new Vec3(var5.x() + 0.5, var5.y(), var5.z() + 0.5));
      }

      if (var3.isEmpty()) {
         for (byte var15 = -4; var15 <= 4 && var3.size() < 3; var15 += 4) {
            for (byte var17 = -4; var17 <= 4 && var3.size() < 3; var17 += 4) {
               int var6 = this.RB.getX() + var15;
               int var7 = this.RB.getZ() + var17;
               int var8 = mc.level.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var6, var7);
               if (var1.floor(var6, var8 - 1, var7) && var1.open(var6, var8, var7) && var1.open(var6, var8 + 1, var7)) {
                  var3.add(new Vec3(var6 + 0.5, var8, var7 + 0.5));
               }
            }
         }
      }

      u4 var16 = new u4(mc.level, mc.player);
      Vec3 var18 = mc.player.getEyePosition();

      for (Vec3 var20 : var3) {
         float var21 = (float)(Math.toDegrees(Math.atan2(var20.z - var18.z, var20.x - var18.x)) - 90.0);
         Vec3 var9 = null;
         float var10 = 0.0F;
         int var11 = Integer.MAX_VALUE;

         for (int var12 = -55; var12 <= 45; var12++) {
            com.larpclient.t.b var13 = com.larpclient.t.m(com.larpclient.t.i.thrown(var18, Vec3.directionFromRotation(var12, var21), Vec3.ZERO), var16);
            Vec3 var14 = var13.X();
            if (var14 != null && !(var14.distanceTo(var20) > 2.0) && var13.y() < var11) {
               var9 = var14;
               var10 = var12;
               var11 = var13.y();
            }
         }

         if (var9 != null) {
            this.J = var21;
            this.V = var10;
            this.C = var9;
            com.larpclient.h.A(this.getName(), "Pearl throw toward {} (pitch {}, {} ticks of flight)", BlockPos.containing(var9).toShortString(), var10, var11);
            return true;
         }
      }

      com.larpclient.h.U(this.getName(), "No pearl throw lands near the anchor at {}", this.RB.toShortString());
      return false;
   }

   private void z() {
      LocalPlayer var1 = mc.player;
      this.z = var1.position();
      ta.F(this, () -> {
         int var2 = hk.J(var0 -> var0.is(Items.ENDER_PEARL));
         if (var2 >= 0) {
            Inventory var3 = var1.getInventory();
            int var4 = var3.getSelectedSlot();
            var3.setSelectedSlot(var2);

            try {
               if (mc.gameMode.useItem(var1, InteractionHand.MAIN_HAND).consumesAction()) {
                  ks.p(var1, InteractionHand.MAIN_HAND);
                  this.RC = a4.a.I;
                  this.s = this.Rd;
               }
            } finally {
               if (var3.getSelectedSlot() == var2) {
                  var3.setSelectedSlot(var4);
               }
            }
         }
      }, () -> mc.player == var1 && this.isRuntimeActive() && this.RC == a4.a.X);
   }

   private void L() {
      this.w.clear();
      this.Rg = Double.MAX_VALUE;
      double var1 = this.Kj.getValue();
      MCFriendModule var3 = LarpClient.R().z().p(MCFriendModule.class);
      String var4 = null;

      for (AbstractClientPlayer var6 : mc.level.players()) {
         if (var6 != mc.player && !var6.isSpectator()) {
            String var7 = var6.getGameProfile().name();
            if (!this.Rt.getValue() || var3 == null || !var3.S(var7)) {
               double var8 = var6.distanceTo(mc.player);
               if (!(var8 > var1)) {
                  this.w.add(new double[]{var6.getX(), var6.getZ()});
                  if (var8 < this.Rg) {
                     this.Rg = var8;
                     var4 = var7;
                  }
               }
            }
         }
      }

      if (var4 != null) {
         this.P = var4;
         if (!this.Rk) {
            this.Rk = (boolean)1;
            com.larpclient.h.A(this.getName(), "{} is {} blocks away; leaving by the next /rtp", var4, (int)this.Rg);
            this.C(a4.t.X, "Player " + var4 + " is " + (int)Math.round(this.Rg) + " blocks away, leaving");
         }
      }
   }

   private void t(long var1) {
      float var3 = mc.player.getHealth();
      if (var3 < this.Ro && mc.player.hurtTime > 0) {
         for (AbstractClientPlayer var5 : mc.level.players()) {
            if (var5 != mc.player && var5.distanceTo(mc.player) < 6.0F) {
               this.Ru = var1;
               break;
            }
         }
      }

      this.Ro = var3;
   }

   private void G(long var1) {
      l0.h(this);
      if (mc.player.containerMenu instanceof ChestMenu) {
         mc.player.closeContainer();
      }

      if (this.f != a4.r.x && this.f != a4.r.r && this.f != a4.r.i) {
         this.l(a4.r.x, "player " + this.P);
      }

      if (this.RH.X(var1)) {
         this.Rb.v(this.Rn);
         if (this.RH.d(var1)) {
            this.y("/rtp did not answer 5 times in a row");
            return;
         }
      }

      double var3 = this.w.isEmpty() ? 64.0 : this.Rg;
      e.y var5 = com.larpclient.e.O(true, this.RH.z(var1), this.RH.V(), this.RN, var3, this.RH.N(var1), this.R5.getValue(), this.Ru < 0L ? -1L : var1 - this.Ru);
      switch (var5) {
         case U:
            this.l(var1);
            break;
         case r:
            this.N("a player came within " + (int)Math.round(var3) + " blocks during the /rtp cooldown");
            break;
         case N:
            this.e("Player nearby, standing still for the teleport");
            break;
         case y:
            this.P();
            this.e("Player nearby, running (/rtp " + (this.RH.V() ? "pending" : "in " + C(this.RH.N(var1))) + ")");
         case t:
      }
   }

   private void P() {
      this.k(24.0, 40);
   }

   private void k(double var1, int var3) {
      if (!this.w.isEmpty() || this.O != null) {
         if (this.O == null || this.Rd - this.RJ > var3 || !this.h.i()) {
            this.RJ = this.Rd;
            if (this.w.isEmpty()) {
               return;
            }

            this.O = com.larpclient.e.L(mc.player.getX(), mc.player.getZ(), this.w, var1);
            BlockPos var4 = this.a();
            zf.s var5 = zf.s.around(var4.getX(), var4.getY(), var4.getZ(), var4.getX(), var4.getY(), var4.getZ(), (int)var1 + 8, 10, 10);
            zf.v var6 = zf.B(
               this.p((BlockPos)null), var4.getX(), var4.getY(), var4.getZ(), zf.f.N(List.copyOf(this.w), var1), var5, zf.z.walking(8000).withPartial(true)
            );
            if (var6.found() && !var6.steps().isEmpty()) {
               this.h.B(var6.steps(), var6.complete());
            }
         }

         gu.r var7 = this.h.S(this.B(), this::K, true);
         this.s();
         if (var7 == gu.r.c || var7 == gu.r.J) {
            this.h.H();
         }
      }
   }

   private boolean S(long var1) {
      LocalPlayer var3 = mc.player;
      ArrayList var4 = new ArrayList();
      byte var5 = 0;
      LivingEntity var6 = null;
      double var7 = Math.min(3.0, var3.entityInteractionRange());

      for (LivingEntity var10 : mc.level
         .getEntitiesOfClass(LivingEntity.class, var3.getBoundingBox().inflate(10.0), var0 -> var0 instanceof Enemy && var0.isAlive())) {
         byte var11 = var10 instanceof Creeper;
         int var12 = !(var10 instanceof RangedAttackMob) && !(var10 instanceof CrossbowAttackMob) ? 0 : 1;
         double var13 = var10.distanceTo(var3);
         if (this.Rw.getValue() && com.larpclient.e.y((boolean)var11, (boolean)var12, var3.getHealth())) {
            if (com.larpclient.e.G((boolean)var11, (boolean)var12, ta.M(var10), var7, var3.getHealth())
               && var3.hasLineOfSight(var10)
               && (var6 == null || var13 < var6.distanceTo(var3))) {
               var6 = var10;
            }
         } else if (com.larpclient.e.H(var13, (boolean)var11, var3.hasLineOfSight(var10), var3.getHealth(), this.R0)) {
            var4.add(var10);
            var5 |= var11;
         }
      }

      if (var4.isEmpty() && var6 != null) {
         this.n(var6, var7);
         return true;
      }

      if (this.u != null) {
         com.larpclient.h.A(this.getName(), "Done fighting; carrying on");
         this.u = null;
      }

      if (var4.isEmpty()) {
         if (this.R0) {
            this.R0 = (boolean)0;
            this.h.H();
            this.O = null;
            com.larpclient.h.A(this.getName(), "Away from the mobs; carrying on");
         }

         return false;
      } else {
         if (!this.R0) {
            this.R0 = (boolean)1;
            if (this.RB != null && ++this.K6 >= 3) {
               com.larpclient.h.A(this.getName(), "Ran from mobs three times on the way to {}; leaving it", this.RB.toShortString());
               this.x("mobs keep guarding it");
            }

            this.h.H();
            this.O = null;
            LivingEntity var15 = var4.stream().min(Comparator.comparingDouble(var1x -> var1x.distanceTo(var3))).orElseThrow();
            com.larpclient.h.A(
               this.getName(),
               "{} {} blocks away at {} health; running",
               var15.getType().getDescription().getString(),
               String.format(Locale.ROOT, "%.1f", var15.distanceTo(var3)),
               (int)var3.getHealth()
            );
         }

         l0.h(this);
         t1.v(this);
         this.KU.B();
         if (var3.containerMenu instanceof ChestMenu) {
            var3.closeContainer();
         }

         if (!this.RE && this.RH.z(var1)) {
            int var16 = this.t;
            this.l(var1);
            if (this.t > var16) {
               com.larpclient.h.A(this.getName(), "{} close; left by /rtp", var5 != 0 ? "Creeper" : "Hostile mob");
            }
         }

         this.w.clear();

         for (LivingEntity var18 : var4) {
            this.w.add(new double[]{var18.getX(), var18.getZ()});
         }

         this.k(var5 != 0 ? 18.0 : 12.0, 20);
         if (!this.h.i()) {
            this.E();
         }

         this.e(var5 != 0 ? "Running from a creeper" : "Running from hostile mobs");
         return true;
      }
   }

   private void n(LivingEntity var1, double var2) {
      LocalPlayer var4 = mc.player;
      if (this.u != var1) {
         this.u = var1;
         com.larpclient.h.A(
            this.getName(),
            "Fighting a {} {} blocks away at {} health",
            var1.getType().getDescription().getString(),
            String.format(Locale.ROOT, "%.1f", var1.distanceTo(var4)),
            (int)var4.getHealth()
         );
      }

      l0.h(this);
      t1.v(this);
      this.KU.B();
      this.h.H();
      int var5 = this.A();
      if (var5 >= 0 && var4.getInventory().getSelectedSlot() != var5) {
         var4.getInventory().setSelectedSlot(var5);
      }

      if (ta.E(this)) {
         Vec3 var6 = var1.getBoundingBox().getCenter().add(0.0, var1.getBbHeight() * 0.2, 0.0);
         float[] var7 = j6.s(var4.getEyePosition(), var6);
         float[] var8 = this.R2.Q(var4.getYRot(), var4.getXRot(), var7[0], var7[1], false);
         j6.U(this, j6.l.o, var8[0], var8[1], 1.0);
      }

      LivingEntity var9 = var1;
      ta.Z(this, var9, var2, true, () -> this.Rh && var9.isAlive() && mc.player == var4);
      this.e("Fighting a " + var1.getType().getDescription().getString());
   }

   private int A() {
      int var1 = -1;
      double var2 = -1.0;

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = mc.player.getInventory().getItem(var4);
         boolean var6 = var5.is(ItemTags.SWORDS);
         boolean var7 = var5.is(ItemTags.AXES);
         if (var6 || var7) {
            double var8 = (var6 ? 10000 : 0) + var5.getMaxDamage();
            if (var8 > var2) {
               var2 = var8;
               var1 = var4;
            }
         }
      }

      return var1;
   }

   private void E() {
      double[] var1 = com.larpclient.e.L(mc.player.getX(), mc.player.getZ(), this.w, 3.0);
      if (var1 != null) {
         c6 var2 = this.p((BlockPos)null);
         BlockPos var3 = this.a();
         int var4 = (int)Math.floor(var1[0]);
         int var5 = (int)Math.floor(var1[1]);
         byte var6 = 0;

         for (int var7 = 1; var7 >= -3 && var6 == 0; var7--) {
            int var8 = var3.getY() + var7;
            var6 = (byte)(var2.open(var4, var8, var5)
                  && var2.open(var4, var8 + 1, var5)
                  && var2.floor(var4, var8 - 1, var5)
                  && var2.penalty(var4, var8, var5) < 8.0
               ? 1
               : 0);
         }

         if (var6 != 0) {
            this.Z = new Vec3(var1[0] - mc.player.getX(), 0.0, var1[1] - mc.player.getZ());
            this.E = (boolean)(mc.player.getFoodData().getFoodLevel() > 6 ? 1 : 0);
         }
      }
   }

   private void N(String var1) {
      com.larpclient.h.A(this.getName(), "Logging out: {}", var1);
      String var2 = "Larp Anchor Hunter: " + var1 + ". " + this.RU + " anchors collected.";
      ClientPacketListener var3 = mc.getConnection();
      this.y("logged out, " + var1);
      if (var3 != null && !mc.isLocalServer()) {
         var3.getConnection().disconnect(Component.literal(var2));
      }
   }

   private static boolean P(ItemStack var0) {
      return var0.get(DataComponents.FOOD) != null
         && !var0.is(Items.GOLDEN_APPLE)
         && !var0.is(Items.ENCHANTED_GOLDEN_APPLE)
         && !var0.is(Items.ROTTEN_FLESH)
         && !var0.is(Items.SPIDER_EYE)
         && !var0.is(Items.POISONOUS_POTATO)
         && !var0.is(Items.PUFFERFISH)
         && !var0.is(Items.CHORUS_FRUIT)
         && !var0.is(Items.SUSPICIOUS_STEW);
   }

   private int H(Item var1) {
      Inventory var2 = mc.player.getInventory();
      int var3 = 0;

      for (int var4 = 0; var4 < 36; var4++) {
         ItemStack var5 = var2.getItem(var4);
         if (var5.is(var1)) {
            var3 += var5.getCount();
         }
      }

      return var3;
   }

   private boolean O() {
      return this.z(new ItemStack(Items.RESPAWN_ANCHOR));
   }

   private boolean z(ItemStack var1) {
      Inventory var2 = mc.player.getInventory();

      for (int var3 = 0; var3 < 36; var3++) {
         ItemStack var4 = var2.getItem(var3);
         if (var4.isEmpty() || ItemStack.isSameItemSameComponents(var4, var1) && var4.getCount() < var4.getMaxStackSize()) {
            return true;
         }
      }

      return false;
   }

   private int L() {
      return this.isInGame() ? this.O(Items.ENDER_PEARL) : 0;
   }

   private int O(Item var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 9; var3++) {
         ItemStack var4 = mc.player.getInventory().getItem(var3);
         if (var4.is(var1)) {
            var2 += var4.getCount();
         }
      }

      return var2;
   }

   private boolean a() {
      return hk.J(var0 -> var0.is(Items.ENDER_CHEST)) >= 0 && this.W() >= 0;
   }

   static boolean Q(ItemStack var0) {
      for (Holder var2 : var0.getEnchantments().keySet()) {
         if (var2.is(Enchantments.SILK_TOUCH)) {
            return true;
         }
      }

      return false;
   }

   private int W() {
      if (!this.isInGame()) {
         return -1;
      }

      BlockState var1 = Blocks.ENDER_CHEST.defaultBlockState();

      for (int var2 = 0; var2 < 9; var2++) {
         ItemStack var3 = mc.player.getInventory().getItem(var2);
         if (Q(var3) && var3.isCorrectToolForDrops(var1) && hk.T(var3) > 5) {
            return var2;
         }
      }

      return -1;
   }

   private int G(BlockState var1) {
      if (!this.isInGame()) {
         return -1;
      }

      int var2 = -1;
      float var3 = -1.0F;

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = mc.player.getInventory().getItem(var4);
         if (var5.isCorrectToolForDrops(var1) && hk.T(var5) > 20) {
            float var6 = var5.getDestroySpeed(var1) + (var5.has(DataComponents.CUSTOM_NAME) ? 0 : 1000);
            if (var6 > var3) {
               var3 = var6;
               var2 = var4;
            }
         }
      }

      return var2;
   }

   private int K(BlockState var1) {
      Inventory var2 = mc.player.getInventory();
      int var3 = var2.getSelectedSlot();
      float var4 = -1.0F;

      for (int var5 = 0; var5 < 9; var5++) {
         ItemStack var6 = var2.getItem(var5);
         if (!var6.isDamageableItem() || hk.T(var6) > 5) {
            float var7 = var6.getDestroySpeed(var1)
               + (var6.isCorrectToolForDrops(var1) ? 100 : 0)
               + (var6.has(DataComponents.CUSTOM_NAME) ? 0 : 1000)
               + (var5 == var2.getSelectedSlot() ? 0.5F : 0.0F);
            if (var7 > var4) {
               var4 = var7;
               var3 = var5;
            }
         }
      }

      return var3;
   }

   private boolean j() {
      int var1 = this.G(Blocks.RESPAWN_ANCHOR.defaultBlockState());
      return var1 >= 0 && mc.player.getInventory().getItem(var1).has(DataComponents.CUSTOM_NAME) && !Boolean.FALSE.equals(this.RK);
   }

   private void r() {
      if (this.RK == null && this.n.isEmpty() && this.j()) {
         BlockHitResult var1 = l0.u(this.RB, this.B());
         if (var1 != null) {
            Axis var2 = var1.getDirection().getAxis();

            for (int var3 = -1; var3 <= 1; var3++) {
               for (int var4 = -1; var4 <= 1; var4++) {
                  if (var3 != 0 || var4 != 0) {
                     BlockPos var5 = var2 == Axis.X
                        ? this.RB.offset(0, var3, var4)
                        : (var2 == Axis.Y ? this.RB.offset(var3, 0, var4) : this.RB.offset(var3, var4, 0));
                     if (l0.B(var5)) {
                        this.n.add(var5.immutable());
                     }
                  }
               }
            }
         }
      }
   }

   private void I() {
      if (!this.n.isEmpty() && this.Rd - this.RZ >= 10) {
         long var1 = this.n.stream().filter(var0 -> mc.level.getBlockState(var0).isAir()).count();
         this.RK = var1 * 4L >= this.n.size() * 3L;
         com.larpclient.h.A(
            this.getName(),
            "Tool check: {} of {} blocks around the anchor broke, so the renamed pickaxe {}",
            var1,
            this.n.size(),
            this.RK ? "cuts 3x3" : "breaks one block at a time"
         );
         this.X(this.RK ? "your renamed pickaxe cuts 3x3, so it keeps away from lava and water." : "your renamed pickaxe breaks one block at a time here.");
         this.n.clear();
      }
   }

   private boolean U(BlockPos var1) {
      for (int var2 = -1; var2 <= 1; var2++) {
         for (int var3 = -1; var3 <= 1; var3++) {
            for (int var4 = -1; var4 <= 1; var4++) {
               if (!mc.level.getFluidState(var1.offset(var2, var3, var4)).isEmpty()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private double B() {
      return Math.min(4.5, mc.player.blockInteractionRange());
   }

   @Override
   protected void aimAt(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         if (ta.E(this)) {
            float[] var3 = j6.s(mc.player.getEyePosition(), var1);
            BlockPos var4 = this.h.A() != null ? this.h.A() : (this.f == a4.r.v ? this.RB : null);
            int var5 = var4 != null && l0.u(var4, this.B()) != null ? 1 : 0;
            float[] var6 = this.R2.Q(mc.player.getYRot(), mc.player.getXRot(), var3[0], var3[1], (boolean)var5);
            j6.U(this, j6.l.o, var6[0], var6[1], 1.0);
         }
      }
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         float[] var3 = this.R2.Q(mc.player.getYRot(), mc.player.getXRot(), var1, var2, false);
         j6.U(this, j6.l.o, var3[0], var3[1], 1.0);
      }
   }

   private void J(Vec3 var1) {
      if (ta.E(this)) {
         float[] var2 = j6.s(mc.player.getEyePosition(), var1);
         float[] var3 = this.R2.Q(mc.player.getYRot(), mc.player.getXRot(), var2[0], var2[1], false);
         j6.U(this, j6.l.o, var3[0], var3[1], 1.0);
      }
   }

   public static Input a() {
      if (o != null && o.isEnabled()) {
         a4 var0 = o;
         Minecraft var1 = Minecraft.getInstance();
         if (var0 != null
            && var0.isRuntimeActive()
            && var0.Rh
            && var1.player != null
            && var1.player == var0.H
            && var1.player.tickCount == var0.RD
            && !p_.V()
            && !cj.b()
            && !(c6.D() instanceof AbstractContainerScreen)
            && (!j6.j() || j6.g(j6.l.o))) {
            int var2 = var0.Z.lengthSqr() > 1.0E-6 ? 1 : 0;
            if (var2 == 0 && !var0.RQ && !var0.RT) {
               return null;
            }

            Input var3 = var2 != 0 ? var0.R2.e(var1.player.getYRot(), var0.Z) : new Input(false, false, false, false, false, false, false);
            return new Input(var3.forward(), var3.backward(), var3.left(), var3.right(), var0.RQ, var0.RT, var0.E);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void X(String var1) {
      if (mc != null && mc.player != null) {
         mc.player.sendSystemMessage(Component.literal("Larp Anchor Hunter: " + var1));
      }
   }

   private void C(a4.t var1, String var2) {
      com.larpclient.h.U(this.getName(), "Notice {}: {}", var1, var2);
      if (var1 != a4.t.P) {
         long var3 = System.currentTimeMillis();
         if (var1 == a4.t.h || !var2.equals(this.Q) || var3 - this.Ri >= 30000L) {
            this.Q = var2;
            this.Ri = var3;
            String var5 = this.D.getValue();
            if (!var5.equals("Off") || var1 == a4.t.h) {
               if (!var5.equals("Chat") && !var5.equals("Off")) {
                  s0.u var6 = switch (var1) {
                     case O -> s0.u.m;
                     case X, h -> s0.u.p;
                     default -> s0.u.B;
                  };
                  s0.s().k("Anchor Hunter", var2, var6, 5000L);
                  if (var1 == a4.t.h) {
                     this.X(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
                  }
               } else {
                  this.X(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
               }
            }
         }
      }
   }

   private static String M(String var0) {
      return var0.isEmpty() ? var0 : var0.substring(0, 1).toUpperCase(Locale.ROOT) + var0.substring(1);
   }

   static String N(int var0, String var1) {
      return var0 + " " + var1 + (var0 == 1 ? "" : "s");
   }

   static String C(long var0) {
      return (var0 + 999L) / 1000L + "s";
   }

   private void U() {
      if (this.isInGame()) {
         try {
            int var1 = ho.B(0.25);
            if (System.currentTimeMillis() < this.R3) {
               for (BlockPos var3 : this.R6) {
                  Gizmos.cuboid(new AABB(var3).inflate(0.02), GizmoStyle.strokeAndFill(-14249, 2.0F, 587188311)).setAlwaysOnTop();
               }
            }

            if (!this.Rh || !this.RG.getValue() || mc.level != this.Rf) {
               return;
            }

            if (this.RB != null) {
               Gizmos.cuboid(new AABB(this.RB).inflate(0.03), GizmoStyle.strokeAndFill(-14249, 2.5F, 872400983)).setAlwaysOnTop();
            }

            List var12 = this.h.e();
            double var13 = 0.05;
            Vec3 var5 = mc.player.position().add(0.0, var13, 0.0);

            for (zf.h var7 : var12) {
               Vec3 var8 = new Vec3(var7.x() + 0.5, var7.y() + var13, var7.z() + 0.5);
               Gizmos.line(var5, var8, uh.H(var1, 0.85), 2.0F).setAlwaysOnTop();
               var5 = var8;

               for (zf.a var10 : var7.digs()) {
                  Gizmos.cuboid(
                     new AABB(var10.x(), var10.y(), var10.z(), var10.x() + 1, var10.y() + 1, var10.z() + 1).inflate(0.01),
                     GizmoStyle.strokeAndFill(uh.H(-38037, 0.9), 1.5F, uh.H(-38037, 0.12))
                  );
               }
            }

            if (this.RC == a4.a.X && this.C != null) {
               Gizmos.cuboid(
                     new AABB(this.C.x - 0.5, this.C.y, this.C.z - 0.5, this.C.x + 0.5, this.C.y + 0.1, this.C.z + 0.5),
                     GizmoStyle.strokeAndFill(-8530948, 2.0F, 863884284)
                  )
                  .setAlwaysOnTop();
            }
         } catch (RuntimeException var11) {
            com.larpclient.h.w(this.getName(), "Path overlay failed", var11);
         }
      }
   }

   @Override
   public s1.u hudCard() {
      if (this.isInGame() && this.RS.getValue() && this.isRuntimeActive() && this.Rh && mc.level == this.Rf) {
         long var1 = System.currentTimeMillis();
         double var3 = Math.max(1.0E-6, (var1 - this.Re) / 3600000.0);
         int var5 = this.RL.getValue().intValue();
         String var6 = this.Rk
            ? "Player " + this.P + " nearby"
            : N(this.RU, "anchor") + (var1 - this.Re > 60000L ? ", " + Math.round(this.RU / var3) + "/h" : "");
         String var7 = this.RE ? "Singleplayer" : (this.RH.V() ? "/rtp waiting" : (this.RH.z(var1) ? "/rtp ready" : "/rtp in " + C(this.RH.N(var1))));
         String var8 = this.getStatus();
         return new s1.u(
            var6,
            var5 > 0 ? this.RU + " of " + var5 : "",
            var8 != null && !var8.isBlank() ? var8 : "Starting",
            var7,
            var5 > 0 ? Math.clamp((double)this.RU / var5, 0.0, 1.0) : -1.0,
            this.Rk
         );
      } else {
         return null;
      }
   }

   public a4.r e() {
      return this.f;
   }

   public boolean G() {
      return this.Rh;
   }

   public int n() {
      return this.RU;
   }

   public BlockPos x() {
      return this.RB;
   }

   public int i() {
      return this.h.R();
   }

   public double b() {
      return this.h.y();
   }

   private enum a {
      r,
      X,
      I;
   }

   public enum r {
      Q("Getting ready"),
      N("Sending /rtp"),
      r("Waiting for the teleport"),
      i("Standing still for the teleport"),
      g("Waiting for /rtp"),
      s("Landing"),
      w("Looking for anchors"),
      b("Choosing an anchor"),
      X("Going to an anchor"),
      v("Mining an anchor"),
      I("Picking up the anchor"),
      J("Putting anchors in the ender chest"),
      u("Selling anchors"),
      t("Protected area, leaving"),
      x("Player nearby, leaving"),
      F("Paused"),
      G("Stopped");

      final String k;

      r(String var3) {
         this.k = var3;
      }
   }

   enum t {
      P,
      O,
      X,
      h;
   }
}

package com.larpclient;

import com.larpclient.module.MCFriendModule;
import com.larpclient.module.Module;
import com.larpclient.module.kl;
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

public final class _z extends _2 implements ac, vi.d {
   private static _z W;
   private static boolean rC;
   static final List<String> x = List.of("AutoAnchor", "AnchorTap", "Anchor Macro", "AutoCrystal", "Freecam");
   static final double I = 28.0;
   static final int rm = 900;
   static final long rq = 120000L;
   static final long p = 360000L;
   static final long rb = 60000L;
   static final int r3 = 3;
   static final int rU = 16000;
   private final com.larpclient.module.o rr = this.addSetting(
      new com.larpclient.module.o(
         "Region", "Which /rtp region to use. Random takes the overworld regions in turn; the Nether is never used", "Random", va.o.toArray(new String[0])
      )
   );
   private final com.larpclient.module.b j = this.g(
      "Player Range",
      "Any player closer than this makes the bot leave by the next /rtp. Servers only show players up to about 48 to 128 blocks away, so the default means anyone you can see",
      96.0,
      8.0,
      160.0,
      1.0
   );
   private final com.larpclient.module.m H = this.C("Ignore Friends", "Players on your friends list (MCFriend) do not make the bot leave", true);
   private final com.larpclient.module.m rd = this.C(
      "Log Out If Caught",
      "If a player gets within 5 blocks while /rtp is still cooling down, log out instead of running. Never logs out while a player has hit you in the last 15 seconds",
      false
   );
   private final com.larpclient.module.m g = this.C(
      "Dig To Hidden Anchors", "Dig a tunnel to anchors inside walls or underground. Off: only anchors you can walk up to", true
   );
   private final com.larpclient.module.m r1 = this.C(
      "Pearl Travel", "Throw ender pearls from your hotbar to reach far anchors fast. Only while you wear armour and have enough health", true
   );
   private final com.larpclient.module.m rj = this.C(
      "Ender Chest Stash",
      "When your inventory is full, put the anchors in your ender chest. Needs an ender chest and a Silk Touch pickaxe in your hotbar",
      true
   );
   private final com.larpclient.module.b D = this.g("Stop After", "Stop after this many anchors (0 never stops)", 0.0, 0.0, 2304.0, 1.0);
   private final com.larpclient.module.o Y = this.addSetting(
      new com.larpclient.module.o("Notifications", "How the bot tells you about finds and problems", "Toast", "Toast", "Chat", "Off")
   );
   private final com.larpclient.module.m f = this.C(
      "Status Card", "Show a card at the top of the screen with anchors found, the /rtp timer and what the bot is doing", true
   );
   private final com.larpclient.module.m YT = this.C("Show Path", "Draw the route, the blocks it digs, the anchor it is going to and its pearl throws", true);
   private final com.larpclient.module.m YO = this.C(
      "Fight Mobs",
      "Hit zombies, spiders and other melee mobs that come for you, with the best sword or axe in your hotbar. Creepers and skeletons are still run from, and it runs at 8 health or less",
      false
   );
   private final com.larpclient.module.m i = this.C(
      "Auto Sell Anchors",
      "After picking up an anchor, sell your anchors with /sell, standing still. Only anchors go into the Sell menu; if the menu looks wrong it stops selling for five minutes",
      false
   );
   private final hw ra = new hw();
   private final bt rE = new bt(this);
   private final ad ro = new ad(this, this.ra, this::e);
   private final va rv = new va();
   private final va.i F = new va.i();
   private final ConcurrentLinkedQueue<String> re = new ConcurrentLinkedQueue<>();
   private _z.b T = _z.b.j;
   private _z.b w = _z.b.Z;
   private boolean V;
   private Object r6;
   private Object r;
   private Object Y0;
   private long rS = -1L;
   private String rW;
   private String r4;
   private final List<String> a = new ArrayList<>();
   private boolean rk;
   private int rw;
   private int r7;
   private long rh;
   private long B;
   private long d;
   private long ru = -2305843009213693952L;
   private long J = -1L;
   private String YZ;
   private boolean rz;
   private Vec3 E;
   private int e;
   private int rM;
   private int u;
   private int rf;
   private final Map<String, Integer> rL = new TreeMap<>();
   private final Map<Long, Integer> r2 = new HashMap<>();
   private final Set<Long> Yb = new HashSet<>();
   private boolean o;
   private boolean O;
   private boolean Yq;
   private LivingEntity rO;
   private int q;
   private String C = "";
   private double y = Double.MAX_VALUE;
   private final List<double[]> rZ = new ArrayList<>();
   private long rR = -1L;
   private float K = 20.0F;
   private double[] rX;
   private int S;
   private BlockPos YD;
   private int ry;
   private int t;
   private int k;
   private int rs;
   private int rG;
   private int rQ;
   private int Y_;
   private int Yy;
   private boolean rn;
   private boolean rp;
   private long rT;
   private long m = -2305843009213693952L;
   private int rH;
   private int R = -1000;
   private final Set<Long> s = new HashSet<>();
   private final Set<Long> v = new HashSet<>();
   private Boolean rJ;
   private final Set<BlockPos> P = new HashSet<>();
   private int b;
   private int rg;
   private int Yz;
   private int YX;
   private int l;
   private int rl = -1;
   private int r8;
   private int rA;
   private BlockPos rF;
   private String rt;
   private int rY;
   private int r_;
   private int G;
   private int r0 = -1;
   private int c;
   private int A;
   private long rP;
   static final long N = 300000L;
   private _z.e rK = _z.e.x;
   private float rI;
   private float rV;
   private Vec3 rB;
   private int X;
   private int r9 = -1000;
   private Vec3 rc;
   private Vec3 rD = Vec3.ZERO;
   private boolean L;
   private boolean rx;
   private boolean YQ;
   private int U = Integer.MIN_VALUE;
   private String Z = "";
   private long rN;
   private final List<BlockPos> ri = new ArrayList<>();
   private long r5;

   public _z() {
      super("Anchor Hunter", "DonutSMP: /rtp around, find respawn anchors in loaded chunks, walk or dig to them, mine and collect them");
      W = this;
      this.D.B("Stop After (anchors, 0 never)");
      if (!rC) {
         rC = (boolean)1;
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> {
            _z var1 = W;
            if (var1 != null) {
               var1.r();
            }
         });
         ClientReceiveMessageEvents.GAME.register((Game)(var0, var1) -> f(var0.getString()));
      }
   }

   public static _z f() {
      return W;
   }

   public static void f(String var0) {
      if (W != null && W.isEnabled()) {
         _z var1 = W;
         if (var1 != null && var1.V && var0 != null && !var0.isBlank() && var1.re.size() < 64) {
            var1.re.add(var0);
         }
      }
   }

   @Override
   public boolean allowsScreenInteractions() {
      return mc == null || !(d1.E() instanceof AbstractContainerScreen) || this.N();
   }

   private boolean N() {
      return this.V && (this.T == _z.b.g || this.T == _z.b.C) && mc != null && mc.player != null && mc.player.containerMenu instanceof ChestMenu;
   }

   @Override
   public void disable() {
      this.rW = null;
      int var1 = this.rS >= 0L ? 1 : 0;
      this.rS = -1L;
      super.disable();
      if (var1 != 0) {
         this.a("turned off while teleporting");
      }
   }

   @Override
   public List<ac.b> commands() {
      return List.of(new ac.b("Before you start", "Check my setup", this::I), new ac.b("Before you start", "Find anchors near me", this::m));
   }

   @Override
   public String summary() {
      if (this.isRuntimeActive() && this.V) {
         return r(this.e, "anchor") + " in " + ac.Q(System.currentTimeMillis() - this.B) + ", " + r(this.rM, "teleport") + ". " + this.T.G + ".";
      } else {
         return this.r4 != null ? this.r4 + "." : "Stand anywhere on DonutSMP and turn it on: it uses /rtp and mines the respawn anchors it finds.";
      }
   }

   @Override
   public String detail() {
      if (!this.isInGame()) {
         return "Needs a diamond or netherite pickaxe in your hotbar.";
      }

      int var1 = this.k(Blocks.RESPAWN_ANCHOR.defaultBlockState());
      String var2 = var1 >= 0 ? mc.player.getInventory().getItem(var1).getHoverName().getString() : "no diamond or netherite pickaxe";
      return "Pickaxe: "
         + var2
         + ". Ender chest "
         + (bt.a(var0 -> var0.is(Items.ENDER_CHEST)) >= 0 ? "yes" : "no")
         + ", Silk Touch pickaxe "
         + (this.u() >= 0 ? "yes" : "no")
         + ", pearls "
         + this.v()
         + ".";
   }

   @Override
   public List<ac.r> settingGroups() {
      return List.of(
         new ac.r("Where to go", List.of(this.rr)),
         new ac.r("Other players", List.of(this.H, this.rd)),
         new ac.r("While hunting", List.of(this.i, this.rj, this.D, this.g, this.r1, this.YO)),
         new ac.r("Display", List.of(this.f, this.YT, this.Y)),
         ac.r.advanced("Advanced", List.of(this.j))
      );
   }

   private String I() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();
      if (mc.level.dimension() != Level.OVERWORLD) {
         var1.add("go to the overworld");
      }

      if (this.k(Blocks.RESPAWN_ANCHOR.defaultBlockState()) < 0) {
         var1.add("put a diamond or netherite pickaxe in your hotbar");
      }

      if (bt.a(_z::K) < 0) {
         var2.add("no food in the hotbar");
      }

      if (!this.p()) {
         if (this.rj.getValue() && this.K()) {
            var2.add("your inventory is full, so it stashes in the ender chest first");
         } else {
            var1.add("make room in your inventory");
         }
      }

      if (this.rj.getValue() && !this.K()) {
         var2.add("no ender chest and Silk Touch pickaxe in the hotbar, so a full inventory stops the run");
      }

      if (this.r1.getValue() && this.v() == 0) {
         var2.add("no ender pearls in the hotbar, so it walks everywhere");
      } else if (this.r1.getValue() && mc.player.getArmorValue() < 12) {
         var2.add("wear armour (12 points or more) for pearl travel");
      }

      String var3 = var1.isEmpty() ? "Ready." : "Not ready: " + String.join("; ", var1) + ".";
      return var2.isEmpty() ? var3 : var3 + " Note: " + String.join("; ", var2) + ".";
   }

   private String m() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      HashMap var1 = new HashMap();
      int var2 = this.C(var1);
      this.ri.clear();
      Vec3 var3 = mc.player.getEyePosition();
      List var4 = var1.keySet()
         .stream()
         .map(var0 -> new BlockPos(hn.k(var0), hn.V(var0), hn.m(var0)))
         .sorted(Comparator.comparingDouble(var1x -> var3.distanceToSqr(Vec3.atCenterOf(var1x))))
         .toList();
      this.ri.addAll(var4.subList(0, Math.min(64, var4.size())));
      this.r5 = System.currentTimeMillis() + 15000L;
      _q.z(this.getName(), "Scan around the player: {} anchors in {} loaded chunks", var1.size(), var2);
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
         + var1.get(hn.f(var5.getX(), var5.getY(), var5.getZ()))
         + " charges).";
   }

   @Override
   public void onEnable() {
      super.onEnable();
      if (this.rS >= 0L) {
         long var1 = System.currentTimeMillis() - this.rS;
         this.rS = -1L;
         if (this.O() == this.Y0 && this.Y0 != null && var1 <= 60000L && mc.player.isAlive()) {
            this.V = (boolean)1;
            this.r6 = mc.level;
            this.r = mc.player;
            _q.z(this.getName(), "Teleport landed in a new world after {} ms; carrying on", var1);
            this.Z(System.currentTimeMillis(), "a new world");
            return;
         }

         this.rW = var1 > 60000L ? "the teleport did not land within a minute" : "the connection changed during the teleport";
         this.a(this.rW);
      }

      if (this.rW != null) {
         String var4 = this.rW;
         this.rW = null;
         this.R("it stopped earlier because " + var4 + ". Turn it on again to start a new run");
      } else {
         String var3 = ac.K(this);
         if (var3 != null) {
            this.R("turn off " + var3 + " first; one bot runs at a time");
         } else if (mc.level.dimension() != Level.OVERWORLD) {
            this.R("go to the overworld first; it only hunts there");
         } else if (this.k(Blocks.RESPAWN_ANCHOR.defaultBlockState()) < 0) {
            this.R("put a diamond or netherite pickaxe with uses left in your hotbar");
         } else if (mc.player.getFoodData().getFoodLevel() < 10 && bt.a(_z::K) < 0) {
            this.R("bring food in your hotbar; you are hungry");
         } else if (this.p() || this.rj.getValue() && this.K() && this.U(Items.RESPAWN_ANCHOR) > 0) {
            this.o();
            this.V = (boolean)1;
            this.r4 = null;
            this.r6 = mc.level;
            this.r = mc.player;
            this.Y0 = this.O();
            this.rk = mc.isLocalServer();
            this.B = System.currentTimeMillis();
            this.K = mc.player.getHealth();
            this.rE.v();
            this.c();
            ArrayList var2 = new ArrayList();
            if (!this.a.isEmpty()) {
               var2.add(String.join(", ", this.a) + (this.a.size() == 1 ? " is" : " are") + " off while hunting and back on after");
            }

            if (bt.a(_z::K) < 0) {
               var2.add("no food in the hotbar");
            }

            if (this.rj.getValue() && !this.K()) {
               var2.add("no ender chest and Silk Touch pickaxe in the hotbar, so a full inventory ends the run");
            }

            if (this.r1.getValue() && this.v() == 0) {
               var2.add("no pearls in the hotbar, so it walks to far anchors");
            }

            if (this.rk) {
               var2.add("singleplayer has no /rtp, so it only hunts around you");
            }

            this.i(this.rk ? "hunting anchors around you." : "hunting anchors with /rtp " + this.rr.getValue().toLowerCase(Locale.ROOT) + ".");
            if (!var2.isEmpty()) {
               this.i("heads up: " + String.join("; ", var2) + ".");
            }

            _q.z(
               this.getName(),
               "Started at {} ({}), region {}, range {}, dig {}, pearls {}, stash {}, stop after {}",
               mc.player.blockPosition().toShortString(),
               this.rk ? "singleplayer" : "server",
               this.rr.getValue(),
               this.j.getValue().intValue(),
               this.g.getValue(),
               this.r1.getValue(),
               this.rj.getValue(),
               this.D.getValue().intValue()
            );
            if (this.rk) {
               this.d = this.B;
               this.r(_z.b.t, "start");
            } else {
               this.r(_z.b.l, "start");
            }
         } else {
            this.R("your inventory is full; make room first");
         }
      }
   }

   @Override
   public void onDisable() {
      int var1 = this.isEnabled() && this.V ? 1 : 0;
      this.p();
      super.onDisable();
      this.rE.b();
      vi.J(this);
      if (var1 == 0) {
         if (this.V) {
            this.a((String)null);
         }

         if (this.r4 != null) {
            this.P(this.r4);
         }
      } else {
         int var2 = mc != null && mc.player != null && mc.player.isAlive() ? 1 : 0;
         if (var2 == 0 || this.O() != this.Y0 || this.Y0 == null || !this.rv.O() && this.T != _z.b.d && this.T != _z.b.n) {
            this.rW = var2 != 0 ? "the world, the player or the connection changed" : "you died";
            _q.z(this.getName(), "Interrupted in {} after {} anchors: {}", this.T, this.e, this.rW);
            this.a(this.rW);
         } else {
            this.rS = System.currentTimeMillis();
            this.V = (boolean)0;
            _q.z(this.getName(), "World changed while waiting for /rtp; waiting for the new world");
         }
      }
   }

   private void a(String var1) {
      this.Q();
      if (this.V || var1 != null) {
         _q.z(
            this.getName(),
            "Run ended{}: {} anchors, {} teleports, {} spots{}",
            var1 == null ? "" : " (" + var1 + ")",
            this.e,
            this.rM,
            this.u,
            this.rL.isEmpty() ? "" : ", skipped " + this.rL
         );
      }

      this.V = (boolean)0;
      this.r6 = this.r = this.Y0 = null;
      this.re.clear();
   }

   private void o() {
      this.rv.x();
      this.F.X();
      this.re.clear();
      this.rL.clear();
      this.rJ = null;
      this.P.clear();
      this.A = 0;
      this.rP = 0L;
      this.rY = 0;
      this.r0 = -1;
      this.T = _z.b.j;
      this.w = _z.b.Z;
      this.rw = this.r7 = 0;
      this.rz = (boolean)0;
      this.J = -1L;
      this.YZ = null;
      this.e = this.rM = this.u = this.rf = 0;
      this.ru = -2305843009213693952L;
      this.E = null;
      this.rS = -1L;
      this.P();
      this.ra.W();
   }

   private void P() {
      this.r2.clear();
      this.Yb.clear();
      this.o = this.O = this.Yq = (boolean)0;
      this.rO = null;
      this.C = "";
      this.y = Double.MAX_VALUE;
      this.rZ.clear();
      this.rX = null;
      this.R();
      this.rK = _z.e.x;
      this.b = 0;
      this.rF = null;
   }

   private void R() {
      this.YD = null;
      this.ry = this.t = this.k = this.rs = this.rG = this.Yy = this.q = 0;
      this.rn = this.rp = (boolean)0;
      this.s.clear();
      this.v.clear();
      this.ro.V();
      this.R = -1000;
   }

   private void p() {
      this.ro.V();
      vs.B(this);
      this.rD = Vec3.ZERO;
      this.L = this.rx = this.YQ = (boolean)0;
   }

   private Object O() {
      return mc != null && mc.getConnection() != null ? mc.getConnection().getConnection() : null;
   }

   private void R(String var1) {
      _q.z(this.getName(), "Not started: {}", var1);
      this.i("not started, " + var1 + ".");
      ib.L().t("Anchor Hunter", I(var1), ib.v.o);
      this.r4 = "Not started: " + var1;
      this.P(this.r4);
      this.disable();
   }

   private void j(String var1) {
      String var2 = this.e + " anchors in " + ac.Q(System.currentTimeMillis() - this.B) + ", " + this.rM + " teleports";
      _q.z(this.getName(), "Stopped: {} ({})", var1, var2);
      this.X(_z.y.j, I(var1) + ". " + I(var2) + ".");
      this.r4 = "Stopped: " + var1;
      this.P(this.r4);
      this.T = _z.b.K;
      this.disable();
   }

   private void c() {
      this.a.clear();
      kl var1 = LarpClient.N().j();

      for (String var3 : x) {
         Module var4 = var1.U(var3);
         if (var4 != null && var4 != this && var4.isEnabled()) {
            var4.setEnabled(false);
            if (!var4.isEnabled()) {
               this.a.add(var3);
            }
         }
      }

      if (!this.a.isEmpty()) {
         _q.z(this.getName(), "Turned off for the run: {}", String.join(", ", this.a));
      }
   }

   private void Q() {
      if (!this.a.isEmpty()) {
         kl var1 = LarpClient.N().j();
         ArrayList var2 = new ArrayList();

         for (String var4 : this.a) {
            Module var5 = var1.U(var4);
            if (var5 != null && !var5.isEnabled()) {
               var5.setEnabled(true);
               var2.add(var4);
            }
         }

         this.a.clear();
         if (!var2.isEmpty()) {
            _q.z(this.getName(), "Turned back on: {}", String.join(", ", var2));
         }
      }
   }

   private String Q() {
      kl var1 = LarpClient.N().j();

      for (String var3 : x) {
         Module var4 = var1.U(var3);
         if (var4 != null && var4 != this && var4.isEnabled()) {
            return var3;
         }
      }

      return null;
   }

   @Override
   public void onTick() {
      this.rD = Vec3.ZERO;
      this.L = this.rx = this.YQ = (boolean)0;
      boolean var8 = false;
      byte var7 = this.V;
      int var10000 = Integer.signum(var7);
      if ((var10000 | -var10000) >>> 31 != 0) {
         if (mc.level == this.r6 && mc.player == this.r) {
            byte var10 = 1;
            int var9 = this.rw;
            this.rw = var9 + var10;
            this.U = mc.player.tickCount;
            long var1 = System.currentTimeMillis();
            if (!mc.player.isAlive()) {
               this.j("you died");
            } else {
               String var3 = this.Q();
               if (var3 != null) {
                  this.j(var3 + " was turned on");
               } else {
                  this.t(var1);
                  boolean var12 = false;
                  byte var11 = this.V;
                  var10000 = Integer.signum(var11);
                  if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.T != _z.b.K) {
                     this.j(var1);
                     this.x(var1);
                     this.X();
                     String var4 = bt.L();
                     if (var4 != null) {
                        this.p();
                        this.rE.s();
                        this.L = (boolean)1;
                        this.P(var4 + ": swimming up");
                     } else if (!this.T(var1)) {
                        if (this.O) {
                           this.N(var1);
                        } else if (this.K() instanceof String var5) {
                           if (this.T == _z.b.g && mc.player.containerMenu instanceof ChestMenu) {
                              mc.player.closeContainer();
                           }

                           if (this.T != _z.b.F) {
                              this.w = this.T != _z.b.g && this.T != _z.b.N ? this.T : _z.b.v;
                              this.r(_z.b.F, var5);
                           }

                           this.p();
                           if (!this.N() && d1.E() instanceof AbstractContainerScreen) {
                              vi.J(this);
                           }

                           this.P("Paused: " + var5);
                        } else {
                           if (this.T == _z.b.F) {
                              this.ra.W();
                              this.r(this.w, "resumed");
                           }

                           if (this.g() && this.rE.T(4)) {
                              this.p();
                              this.P("Eating");
                           } else {
                              if (this.h()) {
                                 boolean var14 = false;
                                 byte var13 = this.rk;
                                 var10000 = Integer.signum(var13);
                                 if ((var10000 | -var10000) >>> 31 == 0 && var1 - this.d > 360000L) {
                                    this.p("six minutes on this spot");
                                    return;
                                 }
                              }

                              switch (this.T) {
                                 case j:
                                 case l:
                                    this.Q(var1);
                                    break;
                                 case d:
                                 case n:
                                    this.U(var1);
                                    break;
                                 case s:
                                    if (this.rv.h(var1)) {
                                       this.Q(var1);
                                    }
                                    break;
                                 case t:
                                    this.P(var1);
                                    break;
                                 case Z:
                                    this.a();
                                    break;
                                 case Q:
                                    this.w(var1);
                                    break;
                                 case v:
                                    this.O(var1);
                                    break;
                                 case N:
                                    this.l(var1);
                                    break;
                                 case b:
                                    this.x();
                                    break;
                                 case g:
                                    this.j();
                                    break;
                                 case C:
                                    this.w();
                                    break;
                                 case V:
                                    this.o = (boolean)1;
                                    this.p("the area is protected");
                                    break;
                                 case D:
                                    this.r(this.rv.O() ? _z.b.d : _z.b.l, "nobody near now");
                                 case F:
                                 case K:
                              }

                              if (this.V && this.T != _z.b.K) {
                                 this.P(this.F(var1));
                              }
                           }
                        }
                     }
                  }
               }
            }
         } else {
            this.j("the world or the player changed");
         }
      }
   }

   private String F(long var1) {
      return switch (this.T) {
         case s -> "Waiting for /rtp (" + w(this.rv.K(var1)) + ")";
         default -> this.T.G;
         case v -> this.rK == _z.e.w
            ? "Aiming a pearl at the anchor"
            : (
               this.rK == _z.e.T
                  ? "Pearling to an anchor"
                  : (
                     this.ro.I() != null
                        ? "Digging toward the anchor at " + this.YD.toShortString()
                        : (this.YD == null ? this.T.G : "Going to the anchor at " + this.YD.toShortString() + ", " + this.h(this.YD) + " blocks")
                  )
            );
         case N -> this.YD == null ? this.T.G : "Mining the anchor at " + this.YD.toShortString();
         case g -> "Stashing anchors in the ender chest (" + (this.b + 1) + "/6)";
      };
   }

   private int h(BlockPos var1) {
      return (int)Math.sqrt(mc.player.distanceToSqr(Vec3.atCenterOf(var1)));
   }

   private String K() {
      if (ij.y()) {
         return "Freecam is on";
      } else if (this.s()) {
         return "you are steering";
      } else {
         return d1.E() instanceof AbstractContainerScreen && !this.N() ? "an inventory is open" : null;
      }
   }

   private boolean s() {
      return mc.isWindowActive()
         && d1.E() == null
         && (
            nz.K(mc.options.keyUp)
               || nz.K(mc.options.keyDown)
               || nz.K(mc.options.keyLeft)
               || nz.K(mc.options.keyRight)
               || nz.K(mc.options.keyJump)
               || nz.K(mc.options.keyShift)
         );
   }

   private boolean g() {
      return this.T == _z.b.s
         || this.T == _z.b.t
         || this.T == _z.b.Z
         || this.T == _z.b.Q
         || this.T == _z.b.v && !this.ro.X()
         || this.T == _z.b.N && mc.player.getFoodData().getFoodLevel() <= 6;
   }

   private boolean h() {
      return this.T == _z.b.Z || this.T == _z.b.Q || this.T == _z.b.v || this.T == _z.b.N || this.T == _z.b.b;
   }

   private void r(_z.b var1, String var2) {
      if (var1 != this.T) {
         _q.c(this.getName(), "{} -> {} ({})", this.T, var1, var2);
         this.T = var1;
         this.r7 = this.rw;
         this.rh = System.currentTimeMillis();
      }
   }

   private void Q(long var1) {
      if (this.rk) {
         this.r("mined every anchor it can reach around you (singleplayer has no /rtp)");
      } else if (!this.rv.h(var1)) {
         this.r(_z.b.s, "rtp not ready");
      } else {
         this.p();
         if (!mc.player.onGround() && !mc.player.isInWater()) {
            this.P("Landing before /rtp");
         } else {
            this.YZ = this.F.K(this.rr.getValue());
            mc.player.connection.sendCommand("rtp " + this.YZ);
            this.rv.o(var1);
            this.ru = var1;
            this.rz = (boolean)0;
            this.rM++;
            this.E = mc.player.position();
            _q.z(this.getName(), "Sent /rtp {} ({} so far)", this.YZ, this.rM);
            this.r(_z.b.d, "rtp " + this.YZ);
         }
      }
   }

   private void U(long var1) {
      if (this.rv.s(var1)) {
         this.F.g(this.YZ);
         if (this.rv.Z(var1)) {
            this.j("/rtp did not answer 5 times in a row");
         } else {
            _q.z(this.getName(), "/rtp {} got no answer; trying again in {}", this.YZ, w(this.rv.K(var1)));
            this.r(_z.b.s, "rtp silent");
         }
      } else {
         if (!this.rv.O()) {
            this.r(_z.b.s, "rtp answered");
         }
      }
   }

   private void t(long var1) {
      Set var4 = null;

      String var3;
      while ((var3 = this.re.poll()) != null) {
         if (var4 == null) {
            var4 = this.e();
         }

         Set var5 = var4;
         vo.s var6 = vo.a(var3, var1x -> var5.contains(var1x.toLowerCase(Locale.ROOT)));
         if (this.T == _z.b.C && vo.N(var3).contains("sold")) {
            _q.z(this.getName(), "Sell reply: {}", vo.N(var3));
         }

         if (var6.kind() != vo.m.A) {
            _q.z(this.getName(), "Server reply {} ({} ms wait): {}", var6.kind(), var6.waitMs(), vo.N(var3));
            this.T(var6, var1);
            if (!this.V || this.T == _z.b.K) {
               return;
            }
         }
      }
   }

   private Set<String> e() {
      HashSet var1 = new HashSet();
      if (mc.getConnection() != null) {
         for (PlayerInfo var3 : mc.getConnection().getOnlinePlayers()) {
            var1.add(var3.getProfile().name().toLowerCase(Locale.ROOT));
         }
      }

      return var1;
   }

   private void T(vo.s var1, long var2) {
      boolean var4 = this.rv.O();
      int var5 = var2 - this.ru < 2000L ? 1 : 0;
      switch (var1.kind()) {
         case m:
         case r:
            this.rv.Q(var2);
            break;
         case G:
            if (var4) {
               this.rz = (boolean)1;
               this.J = var2;
               if (this.T == _z.b.d) {
                  this.r(_z.b.n, "countdown");
               }
            }
            break;
         case n:
            if (!var4) {
               return;
            }

            this.rv.q(var2, var1.waitMs());
            _q.z(this.getName(), "/rtp is cooling down; next in {}", w(this.rv.K(var2)));
            this.c("rtp cooldown");
            break;
         case d:
            if (var4) {
               this.rv.N(var2, 10000L);
               this.c("combat tagged");
            }
            break;
         case P:
            if (var4 && var5 != 0) {
               this.rv.N(var2, 1500L);
               this.c("chat limit");
            }
            break;
         case t:
            if (var4 && var5 != 0) {
               this.j("this server has no /rtp");
            }
            break;
         case i:
         case v:
            if (var4 || this.rz) {
               this.rv.N(var2, 3000L);
               this.rz = (boolean)0;
               this.c(var1.kind() == vo.m.v ? "unsafe spot" : "teleport cancelled");
            }
            break;
         case l:
            if (var4) {
               this.F.g(this.YZ);
               this.rv.N(var2, 3000L);
               this.c("region down");
            }
            break;
         case W:
            if (this.T == _z.b.g) {
               this.rt = "the area is protected";
            } else if ((this.T == _z.b.N || this.T == _z.b.v) && var2 - this.m <= 3000L) {
               this.p();
               _q.z(this.getName(), "The server says {} is protected", this.YD == null ? "this spot" : this.YD.toShortString());
               this.r(_z.b.V, "protected area");
            }
         case M:
         case A:
      }
   }

   private void c(String var1) {
      if (this.T == _z.b.d || this.T == _z.b.n || this.T == _z.b.D) {
         this.r(_z.b.s, var1);
      }
   }

   private void j(long var1) {
      Vec3 var3 = mc.player.position();
      Vec3 var4 = this.E;
      this.E = var3;
      if (var4 != null && (this.rv.O() || this.rz)) {
         if (var3.distanceTo(var4) > 64.0) {
            this.Z(var1, String.format(Locale.ROOT, "%.0f blocks", var3.distanceTo(var4)));
         }
      }
   }

   private void Z(long var1, String var3) {
      this.rv.y(var1);
      this.F.G(this.YZ);
      this.u++;
      this.rz = (boolean)0;
      this.P();
      this.p();
      this.d = var1;
      this.E = mc.player.position();
      _q.z(this.getName(), "Teleported ({}) to {}; spot {}", var3, mc.player.blockPosition().toShortString(), this.u);
      this.r(_z.b.t, "teleported");
   }

   private void p(String var1) {
      this.p();
      this.R();
      _q.z(this.getName(), "Leaving this spot: {}", var1);
      if (this.rk) {
         this.r("mined every anchor it can reach around you (singleplayer has no /rtp)");
      } else {
         this.r(this.rv.h(System.currentTimeMillis()) ? _z.b.l : _z.b.s, var1);
      }
   }

   private int k() {
      return Math.clamp(mc.options.getEffectiveRenderDistance(), 2, 12);
   }

   private void P(long var1) {
      if (mc.level.dimension() != Level.OVERWORLD) {
         if (var1 - this.rh > 10000L) {
            this.j("the /rtp sent you out of the overworld");
         }
      } else {
         int var3 = !mc.player.onGround() && !mc.player.isInWater() ? 0 : 1;
         int var4 = this.k();
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
         if (var3 != 0 && var1 - this.rh >= 1000L && var11 >= 0.9 || var1 - this.rh > 8000L) {
            _q.c(this.getName(), "Landed; {}% of {} chunks loaded", Math.round(var11 * 100.0), var6);
            this.r(_z.b.Z, "landed");
         }
      }
   }

   private int C(Map<Long, Integer> var1) {
      int var2 = this.k();
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
                     au.z(new au.l() {
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

   private void a() {
      this.r2.clear();
      int var1 = this.C(this.r2);
      this.r2.keySet().removeAll(this.Yb);
      _q.z(this.getName(), "{} anchors in {} loaded chunks", this.r2.size(), var1);
      if (this.r2.isEmpty()) {
         this.rf++;
         this.p("no anchors here");
      } else {
         this.X(_z.y.l, this.r2.size() + (this.r2.size() == 1 ? " anchor" : " anchors") + " in sight of the chunks here");
         this.r(_z.b.Q, this.r2.size() + " anchors");
      }
   }

   private void w(long var1) {
      this.C(this.r2);
      this.r2.keySet().removeAll(this.Yb);
      Vec3 var3 = mc.player.getEyePosition();
      List var4 = this.r2
         .keySet()
         .stream()
         .sorted(Comparator.comparingDouble(var1x -> var3.distanceToSqr(hn.k(var1x) + 0.5, hn.V(var1x) + 0.5, hn.m(var1x) + 0.5)))
         .limit(48L)
         .toList();
      BlockPos var5 = null;
      double var6 = Double.MAX_VALUE;
      int var8 = mc.player.getBlockY();
      d1 var9 = this.d(null);

      for (long var11 : var4) {
         var9.J(new BlockPos(hn.k(var11), hn.V(var11), hn.m(var11)));
      }

      au.z var21 = this.o();

      for (long var12 : var4) {
         BlockPos var14 = new BlockPos(hn.k(var12), hn.V(var12), hn.m(var12));
         if (!mc.level.getBlockState(var14).is(Blocks.RESPAWN_ANCHOR)) {
            this.r2.remove(var12);
         } else {
            double var15 = Math.hypot(var14.getX() + 0.5 - mc.player.getX(), var14.getZ() + 0.5 - mc.player.getZ());
            au.q var17 = var15 <= 48.0 ? au.b(var9, var14.getX(), var14.getY(), var14.getZ(), var21) : null;
            String var18 = var17 == null
               ? (this.o ? "protected" : null)
               : au.s(var9, var14.getX(), var14.getY(), var14.getZ(), var8, var17, this.g.getValue(), this.o);
            if (var18 != null) {
               this.Q(var12, var18);
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
         this.p(this.r2.isEmpty() ? "no anchors left here" : "no safe anchors left here");
      } else {
         this.E(var5, var1);
      }
   }

   private void Q(long var1, String var3) {
      this.Yb.add(var1);
      this.r2.remove(var1);
      this.rL.merge(var3, 1, Integer::sum);
      _q.c(this.getName(), "Skipping the anchor at {} {} {}: {}", hn.k(var1), hn.V(var1), hn.m(var1), var3);
   }

   private void E(BlockPos var1, long var2) {
      this.R();
      this.YD = var1.immutable();
      this.ry = this.r2.getOrDefault(hn.f(var1.getX(), var1.getY(), var1.getZ()), 0);
      this.rT = var2;
      this.rH = this.ro.U();
      _q.z(
         this.getName(),
         "Going for the anchor at {} ({} charges, {} blocks away)",
         this.YD.toShortString(),
         this.ry,
         (int)Math.sqrt(mc.player.distanceToSqr(Vec3.atCenterOf(this.YD)))
      );
      this.r(_z.b.v, "target " + this.YD.toShortString());
   }

   private void Z(String var1) {
      if (this.YD != null) {
         this.Q(hn.f(this.YD.getX(), this.YD.getY(), this.YD.getZ()), var1);
         _q.z(this.getName(), "Gave up on the anchor at {}: {}", this.YD.toShortString(), var1);
      }

      this.p();
      this.R();
      this.r(_z.b.Q, var1);
   }

   private void O(long var1) {
      if (this.YD == null) {
         this.r(_z.b.Q, "no target");
      } else if (!mc.level.getBlockState(this.YD).is(Blocks.RESPAWN_ANCHOR)) {
         this.r2.remove(hn.f(this.YD.getX(), this.YD.getY(), this.YD.getZ()));
         _q.c(this.getName(), "The anchor at {} is gone", this.YD.toShortString());
         this.R();
         this.r(_z.b.Q, "anchor gone");
      } else if (!this.p()) {
         this.y();
      } else if (this.t < 3 && this.ro.U() - this.rH <= 40 && var1 - this.rT <= 120000L) {
         double var3 = Math.hypot(this.YD.getX() + 0.5 - mc.player.getX(), this.YD.getZ() + 0.5 - mc.player.getZ());
         if (!this.rp && var3 > 28.0) {
            this.rp = (boolean)1;
         } else if (this.rp && var3 <= 22.0) {
            this.rp = (boolean)0;
            this.ro.V();
         }

         if (this.rK == _z.e.x && (!this.rp || !this.r1.getValue()) || !this.i()) {
            if (this.ro.X() && (!this.rp || this.rw - this.R <= 100) && (!this.ro.B() || this.ro.I().size() > 4 || this.rw - this.R <= 10)
               || this.S()
               || this.ro.X()) {
               ad.s var5 = this.ro.f(this.w(), this::s, true);
               this.Y();
               switch (var5) {
                  case Z:
                     this.ro.V();
                     if (!this.rp && this.s.contains(this.H())) {
                        this.f(var1);
                     }
                     break;
                  case d:
                     this.t++;
                     _q.z(this.getName(), "Route to {} stuck ({}): {}", this.YD.toShortString(), this.t, this.ro.I());
                     this.ro.V();
               }
            }
         }
      } else {
         this.Z(this.t >= 3 ? "no way there" : (this.ro.U() - this.rH > 40 ? "too much digging" : "took too long to reach"));
      }
   }

   private boolean S() {
      this.R = this.rw;
      d1 var1 = this.d(this.YD);
      BlockPos var2 = this.R();
      hn.l var3;
      if (this.rp) {
         hn.j var4 = hn.j.around(var2.getX(), var2.getY(), var2.getZ(), this.YD.getX(), this.YD.getY(), this.YD.getZ(), 16, 24, 24);
         hn.m var5 = new hn.m(16000, this.g.getValue(), 3, true, 3.0);
         var3 = hn.a(var1, var2.getX(), var2.getY(), var2.getZ(), hn.c.a(this.YD.getX() + 0.5, this.YD.getY(), this.YD.getZ() + 0.5, 18.0, 24.0), var4, var5);
      } else {
         au.q var10 = au.b(var1, this.YD.getX(), this.YD.getY(), this.YD.getZ(), this.o());
         String var11 = au.s(var1, this.YD.getX(), this.YD.getY(), this.YD.getZ(), var2.getY(), var10, this.g.getValue(), this.o);
         if (var11 != null) {
            this.Z(var11);
            return false;
         }

         this.s.clear();

         for (au.m var7 : var10.usable(this.g.getValue())) {
            long var8 = hn.f(var7.x(), var7.y(), var7.z());
            if (!this.v.contains(var8)) {
               this.s.add(var8);
            }
         }

         if (this.s.isEmpty()) {
            this.Z("no clear view from any standing spot");
            return false;
         }

         if (this.s.contains(this.H())) {
            this.f(System.currentTimeMillis());
            return false;
         }

         hn.j var12 = hn.j.around(var2.getX(), var2.getY(), var2.getZ(), this.YD.getX(), this.YD.getY(), this.YD.getZ(), 10, 12, 12);
         hn.m var13 = new hn.m(16000, this.g.getValue(), 3, false, 1.0);
         var3 = hn.a(var1, var2.getX(), var2.getY(), var2.getZ(), hn.c.Q(this.s, this.YD.getX(), this.YD.getY() - 1, this.YD.getZ(), 1.0), var12, var13);
      }

      if (!var3.found()) {
         this.t++;
         if (var3.explored() <= 1) {
            this.M(var2);
         }

         _q.z(this.getName(), "No route toward {} ({} cells searched, try {})", this.YD.toShortString(), var3.explored(), this.t);
         return false;
      } else if (var3.steps().isEmpty()) {
         if (this.rp) {
            this.rp = (boolean)0;
         }

         return false;
      } else {
         this.ro.c(var3.steps(), var3.complete());
         _q.c(
            this.getName(),
            "Route of {} steps ({} to dig{}) toward {}",
            var3.steps().size(),
            var3.digs(),
            var3.complete() ? "" : ", partial",
            this.YD.toShortString()
         );
         return true;
      }
   }

   private BlockPos R() {
      return BlockPos.containing(mc.player.getX(), mc.player.getY() + 0.2, mc.player.getZ());
   }

   private void M(BlockPos var1) {
      Function var2 = var0 -> BuiltInRegistries.BLOCK.getKey(mc.level.getBlockState(var0).getBlock()).getPath()
         + (mc.level.getFluidState(var0).isEmpty() ? "" : "+fluid");
      _q.z(
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

   private long H() {
      BlockPos var1 = this.R();
      return hn.f(var1.getX(), var1.getY(), var1.getZ());
   }

   private d1 d(BlockPos var1) {
      d1 var2 = new d1(this::s, this.y());
      if (var1 != null) {
         var2.J(var1);
      }

      for (LivingEntity var4 : mc.level
         .getEntitiesOfClass(
            LivingEntity.class, mc.player.getBoundingBox().inflate(24.0), var0 -> var0 instanceof Enemy && var0.isAlive() && mc.player.hasLineOfSight(var0)
         )) {
         var2.X(var4.getX(), var4.getY(), var4.getZ(), var4 instanceof Creeper ? 7.0 : 3.0);
      }

      return var2;
   }

   private au.z o() {
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

   private void f(long var1) {
      this.ro.V();
      this.k = 0;
      this.Yy = 0;
      this.rn = (boolean)0;
      this.rQ = this.U(Items.RESPAWN_ANCHOR);
      this.m = var1;
      this.r(_z.b.N, "at a standing spot");
   }

   private void l(long var1) {
      if (this.YD == null) {
         this.r(_z.b.Q, "no target");
      } else {
         BlockState var3 = mc.level.getBlockState(this.YD);
         if (!var3.is(Blocks.RESPAWN_ANCHOR)) {
            vs.B(this);
            if (!this.rn) {
               this.rn = (boolean)1;
               this.rs = 0;
            }

            if (++this.rs >= 3) {
               this.Y_ = this.rw;
               this.r2.remove(hn.f(this.YD.getX(), this.YD.getY(), this.YD.getZ()));
               this.r(_z.b.b, "anchor broken");
            }
         } else {
            if (this.rn) {
               this.rn = (boolean)0;
               if (++this.rG >= 2) {
                  _q.z(this.getName(), "The anchor at {} came back twice", this.YD.toShortString());
                  this.r(_z.b.V, "anchor came back twice");
                  return;
               }
            }

            if (!this.p()) {
               this.y();
            } else if (++this.k > 900) {
               this.Z("took too long to mine");
            } else {
               double var4 = this.w();
               if (mc.player.getEyePosition().distanceTo(Vec3.atCenterOf(this.YD)) > var4 + 0.2) {
                  this.r(_z.b.v, "out of reach");
               } else if (this.y() && this.W(this.YD)) {
                  this.Z("water or lava next to it and your pickaxe may cut 3x3");
               } else {
                  BlockPos var6 = this.YD;
                  this.A();
                  boolean var7 = vs.i(this, var6, var4, true, 12.0F, this::k, () -> !this.s());
                  if (var7) {
                     this.Yy = 0;
                  } else {
                     if (++this.Yy > 20) {
                        this.v.add(this.H());
                        this.Yy = 0;
                        if (this.v.size() >= 3) {
                           this.Z("no clear view");
                           return;
                        }

                        _q.c(
                           this.getName(),
                           "No clear view of {} from {}; trying another spot",
                           this.YD.toShortString(),
                           mc.player.blockPosition().toShortString()
                        );
                        this.ro.V();
                        this.r(_z.b.v, "lost sight");
                     }
                  }
               }
            }
         }
      }
   }

   private void x() {
      this.u();
      int var1 = this.U(Items.RESPAWN_ANCHOR);
      if (var1 > this.rQ) {
         int var4 = var1 - this.rQ;
         this.e += var4;
         _q.z(this.getName(), "Picked up {} anchor{} ({} total){}", var4, var4 == 1 ? "" : "s", this.e, this.ry > 0 ? ", it had " + this.ry + " charges" : "");
         this.X(_z.y.h, "+" + var4 + " anchor" + (var4 == 1 ? "" : "s") + " (" + this.e + " total)");
         this.p();
         this.R();
         if (this.i.getValue() && !this.rk && System.currentTimeMillis() >= this.rP) {
            this.I();
         } else {
            int var5 = this.D.getValue().intValue();
            if (var5 > 0 && this.e >= var5) {
               this.r("collected " + this.e + " anchors");
            } else {
               this.r(_z.b.Q, "picked up");
            }
         }
      } else {
         int var2 = this.rw - this.Y_;
         if (var2 > 200) {
            _q.z(this.getName(), "Lost the drop of the anchor at {}", this.YD == null ? "?" : this.YD.toShortString());
            this.p();
            this.R();
            this.r(_z.b.Q, "drop lost");
         } else if (var2 >= 10) {
            ItemEntity var3 = this.X(Items.RESPAWN_ANCHOR, 8.0);
            if (var3 != null) {
               this.S(var3);
            }
         }
      }
   }

   private void r(String var1) {
      _q.z(this.getName(), "Done: {}", var1);
      this.X(_z.y.h, "Done, " + var1 + ".");
      this.r4 = "Done: " + var1;
      this.P(this.r4);
      this.T = _z.b.K;
      this.disable();
   }

   private ItemEntity X(Item var1, double var2) {
      List var4 = mc.level.getEntitiesOfClass(ItemEntity.class, mc.player.getBoundingBox().inflate(var2), var1x -> var1x.isAlive() && var1x.getItem().is(var1));
      return var4.stream().min(Comparator.comparingDouble(var0 -> var0.distanceToSqr(mc.player))).orElse(null);
   }

   private void S(ItemEntity var1) {
      BlockPos var2 = BlockPos.containing(var1.position());
      BlockPos var3 = this.R();
      if (!(Math.hypot(var1.getX() - mc.player.getX(), var1.getZ() - mc.player.getZ()) < 0.6)) {
         if (!this.ro.X() || this.rw - this.R > 20) {
            this.R = this.rw;
            hn.j var4 = hn.j.around(var3.getX(), var3.getY(), var3.getZ(), var2.getX(), var2.getY(), var2.getZ(), 6, 6, 4);
            hn.l var5 = hn.a(
               this.d(null), var3.getX(), var3.getY(), var3.getZ(), hn.c.a(var1.getX(), var2.getY(), var1.getZ(), 0.5, 1.0), var4, hn.m.walking(4000)
            );
            if (!var5.found() || var5.steps().isEmpty()) {
               this.X(var1.position());
               return;
            }

            this.ro.s(var5.steps());
         }

         ad.s var6 = this.ro.f(this.w(), this::s, false);
         this.Y();
         if (var6 == ad.s.Z || var6 == ad.s.d) {
            this.ro.V();
            this.X(var1.position());
         }
      }
   }

   private void X(Vec3 var1) {
      Vec3 var2 = var1.subtract(mc.player.position()).multiply(1.0, 0.0, 1.0);
      double var3 = var2.length();
      double var5 = var3 < 1.0E-6 ? 0.0 : mc.player.getDeltaMovement().multiply(1.0, 0.0, 1.0).dot(var2.normalize());
      this.rx = (boolean)1;
      boolean var7 = this.ra.g(var3, var5, _p.Q(this));
      this.rD = var7 ? var2 : Vec3.ZERO;
   }

   private void Y() {
      this.rD = this.ro.L();
      this.L = this.ro.p();
      this.rx = this.ro.v();
      this.YQ = this.ro.d();
   }

   private void y() {
      this.p();
      if (this.rj.getValue() && this.K() && this.U(Items.RESPAWN_ANCHOR) > 0) {
         this.w = this.YD != null ? _z.b.v : _z.b.Q;
         this.b = 0;
         this.rg = this.rw;
         this.Yz = 0;
         this.rt = null;
         if (this.rF != null && !mc.level.getBlockState(this.rF).is(Blocks.ENDER_CHEST)) {
            this.rF = null;
         }

         this.YX = this.U(Items.RESPAWN_ANCHOR);
         _q.z(this.getName(), "Inventory full with {} anchors; stashing them in the ender chest", this.YX);
         this.r(_z.b.g, "inventory full");
      } else {
         this.j(this.U(Items.RESPAWN_ANCHOR) > 0 ? "your inventory is full" : "your inventory is full of other things; make room for anchors");
      }
   }

   private void j() {
      if (this.rt != null && this.b <= 1) {
         this.B(this.rt);
      } else if (this.rw - this.rg > 300) {
         this.B("step " + this.b + " took too long");
      } else if (mc.player.onGround()) {
         switch (this.b) {
            case 0:
               this.k();
               break;
            case 1:
               this.N();
               break;
            case 2:
               this.U();
               break;
            case 3:
               this.F();
               break;
            case 4:
               this.W();
               break;
            case 5:
               this.C();
               break;
            default:
               this.B("unknown step");
         }
      }
   }

   private void E(int var1) {
      this.b = var1;
      this.rg = this.rw;
   }

   private void k() {
      if (this.rF != null && mc.level.getBlockState(this.rF).is(Blocks.ENDER_CHEST)) {
         this.E(1);
      } else {
         if (this.rF == null) {
            BlockPos var1 = this.R();
            int[] var2 = au.T(this.d(this.YD), var1.getX(), var1.getY(), var1.getZ());
            if (var2 == null) {
               this.B("no room to place the ender chest");
               return;
            }

            this.rF = new BlockPos(var2[0], var2[1], var2[2]);
         }

         int var4 = bt.a(var0 -> var0.is(Items.ENDER_CHEST));
         if (var4 < 0) {
            this.B("no ender chest in the hotbar");
         } else if (!this.V(var4)) {
            this.P("Taking out the ender chest");
         } else {
            BlockPos var5 = this.rF;
            boolean var3 = vi.a(
               this,
               var5,
               Items.ENDER_CHEST,
               this.w(),
               true,
               true,
               () -> vi.i(var5),
               var2x -> _q.c(this.getName(), "Ender chest at {} {}", var5.toShortString(), var2x ? "placed" : "rejected")
            );
            if (!var3 && this.rw - this.rg > 60) {
               this.rF = null;
               this.rg = this.rw;
               if (++this.Yz >= 3) {
                  this.B("could not place the ender chest");
               }
            }
         }
      }
   }

   private void N() {
      if (mc.player.containerMenu instanceof ChestMenu) {
         this.E(2);
      } else if (!mc.level.getBlockState(this.rF).is(Blocks.ENDER_CHEST)) {
         if (++this.Yz >= 3) {
            this.B("the ender chest disappeared");
         } else {
            this.rF = null;
            this.E(0);
         }
      } else if (d1.E() == null) {
         ItemStack var1 = mc.player.getMainHandItem();
         if (this.rw % 10 == 0) {
            vi.d(
               this,
               this.rF,
               var1.getItem(),
               this.w(),
               true,
               false,
               () -> d1.E() == null,
               var1x -> _q.c(this.getName(), "Opening the ender chest {}", var1x ? "sent" : "refused")
            );
         }
      }
   }

   private void U() {
      if (!(mc.player.containerMenu instanceof ChestMenu var1)) {
         this.E(1);
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
            if (this.rw - this.rA >= 4) {
               this.rA = this.rw;
               mc.gameMode.handleContainerInput(var1.containerId, var3, 0, ContainerInput.QUICK_MOVE, mc.player);
            }
         } else {
            int var8 = Math.max(0, this.YX - this.U(Items.RESPAWN_ANCHOR));
            if (var8 == 0) {
               this.rt = var3 < 0 ? "no anchors to store" : "the ender chest is full";
            }

            _q.z(this.getName(), "Stored {} anchors in the ender chest{}", var8, this.rt == null ? "" : " (" + this.rt + ")");
            this.E(3);
         }
      }
   }

   private void F() {
      if (mc.player.containerMenu instanceof ChestMenu) {
         mc.player.closeContainer();
      } else {
         this.l = this.U(Items.ENDER_CHEST);
         this.E(4);
      }
   }

   private void W() {
      if (!mc.level.getBlockState(this.rF).is(Blocks.ENDER_CHEST)) {
         this.Y_ = this.rw;
         this.E(5);
      } else if (!this.a(new ItemStack(Items.ENDER_CHEST))) {
         this.B("no room to pick the ender chest back up");
      } else if (this.u() < 0) {
         this.B("no Silk Touch pickaxe in the hotbar");
      } else {
         BlockPos var1 = this.rF;
         if (!vs.i(this, var1, this.w(), true, 12.0F, var1x -> this.u(), () -> true) && this.rw - this.rg > 60) {
            this.B("cannot reach the ender chest to take it back");
         }
      }
   }

   private void C() {
      if (this.U(Items.ENDER_CHEST) > this.l) {
         vs.B(this);
         int var2 = Math.max(0, this.YX - this.U(Items.RESPAWN_ANCHOR));
         if (this.rt != null && var2 == 0) {
            this.j("your inventory is full and " + this.rt);
         } else {
            this.X(_z.y.h, var2 + " anchors stored in your ender chest");
            this.rF = null;
            this.r(this.w, "stashed");
         }
      } else {
         ItemEntity var1 = this.X(Items.ENDER_CHEST, 8.0);
         if (var1 != null && this.rw - this.Y_ < 200) {
            this.S(var1);
         } else {
            if (this.rw - this.Y_ > 60) {
               this.B("lost the ender chest");
            }
         }
      }
   }

   private void B(String var1) {
      this.p();
      if (mc.player.containerMenu instanceof ChestMenu) {
         mc.player.closeContainer();
      }

      _q.z(this.getName(), "Ender chest stash failed: {}", var1);
      this.j("your inventory is full (" + var1 + ")");
   }

   private boolean V(int var1) {
      Inventory var2 = mc.player.getInventory();
      if (var2.getSelectedSlot() != var1) {
         vs.B(this);
         var2.setSelectedSlot(var1);
         this.rl = var1;
         this.r8 = this.rw;
         return false;
      }

      if (this.rl != var1) {
         this.rl = var1;
         this.r8 = this.rw;
      }

      return this.rw - this.r8 >= 3;
   }

   private void I() {
      this.rY = 0;
      this.r_ = this.rw;
      this.r0 = -1;
      this.G = this.U(Items.RESPAWN_ANCHOR);
      _q.z(this.getName(), "Selling {} anchors with /sell", this.G);
      this.r(_z.b.C, "selling");
   }

   private void j(int var1) {
      this.rY = var1;
      this.r_ = this.rw;
   }

   private void w() {
      if (this.rw - this.r_ > 200) {
         this.m("the Sell menu took too long (step " + (this.rY + 1) + ")");
      } else {
         switch (this.rY) {
            case 0:
               if (!mc.player.onGround() || mc.player.containerMenu != mc.player.inventoryMenu || d1.E() instanceof AbstractContainerScreen) {
                  return;
               }

               if (this.U(Items.RESPAWN_ANCHOR) == 0) {
                  this.m();
                  return;
               }

               mc.player.connection.sendCommand("sell");
               this.j(1);
               break;
            case 1:
               ChestMenu var12 = this.C();
               if (var12 == null) {
                  return;
               }

               int var13 = var12.getRowCount() * 9;
               this.r0 = -1;

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

                     if (v5.a(var5, var4.getHoverName().getString(), var7, var14, var13)) {
                        if (this.r0 >= 0) {
                           this.m("the Sell menu has more than one Sell button");
                           return;
                        }

                        this.r0 = var14;
                     }
                  }
               }

               if (this.r0 < 0) {
                  this.m("the Sell menu has no Sell button in its bottom row");
                  return;
               }

               for (int var15 = 0; var15 < var13 - 9; var15++) {
                  if (!var12.getSlot(var15).getItem().isEmpty()) {
                     this.m("the Sell menu already holds items");
                     return;
                  }
               }

               this.j(2);
               break;
            case 2:
               ChestMenu var11 = this.C();
               if (var11 == null) {
                  this.m("the Sell menu closed");
                  return;
               }

               if (mc.player.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4 || !var11.getCarried().isEmpty()) {
                  return;
               }

               int var2 = var11.getRowCount() * 9;

               for (int var3 = var2; var3 < var11.slots.size(); var3++) {
                  if (((Slot)var11.slots.get(var3)).getItem().is(Items.RESPAWN_ANCHOR)) {
                     if (this.rw - this.c >= 4) {
                        this.c = this.rw;
                        mc.gameMode.handleContainerInput(var11.containerId, var3, 0, ContainerInput.QUICK_MOVE, mc.player);
                     }

                     return;
                  }
               }

               if (this.E(var11) == 0) {
                  this.m("no anchors went into the Sell menu");
                  return;
               }

               this.j(3);
               break;
            case 3:
               ChestMenu var10 = this.C();
               if (var10 == null) {
                  this.m("the Sell menu closed before selling");
                  return;
               }

               if (this.rw - this.c < 4 || !var10.getCarried().isEmpty()) {
                  return;
               }

               this.c = this.rw;
               mc.gameMode.handleContainerInput(var10.containerId, this.r0, 0, ContainerInput.PICKUP, mc.player);
               this.j(4);
               break;
            default:
               ChestMenu var1 = this.C();
               if (var1 != null && this.E(var1) > 0) {
                  if (this.rw - this.r_ > 100) {
                     this.m("the server did not take the anchors");
                  }

                  return;
               }

               if (var1 != null) {
                  mc.player.closeContainer();
               }

               this.m();
         }
      }
   }

   private int E(ChestMenu var1) {
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

   private ChestMenu C() {
      return d1.E() instanceof ContainerScreen var1
            && mc.player.containerMenu instanceof ChestMenu var2
            && var1.getMenu() == var2
            && s7.M(var1.getTitle().getString(), "Sell")
         ? var2
         : null;
   }

   private void m() {
      int var1 = Math.max(0, this.G - this.U(Items.RESPAWN_ANCHOR));
      this.A += var1;
      _q.z(this.getName(), "Sold {} anchors ({} this run)", var1, this.A);
      if (var1 > 0) {
         this.X(_z.y.h, "Sold " + var1 + " anchor" + (var1 == 1 ? "" : "s") + " (" + this.A + " this run)");
      }

      int var2 = this.D.getValue().intValue();
      if (var2 > 0 && this.e >= var2) {
         this.r("collected " + this.e + " anchors");
      } else {
         this.r(_z.b.Q, "sold");
      }
   }

   private void m(String var1) {
      if (mc.player.containerMenu instanceof ChestMenu) {
         mc.player.closeContainer();
      }

      this.rP = System.currentTimeMillis() + 300000L;
      _q.z(this.getName(), "Selling stopped: {}; trying again in five minutes", var1);
      this.X(_z.y.B, "Could not sell anchors: " + var1);
      int var2 = this.D.getValue().intValue();
      if (var2 > 0 && this.e >= var2) {
         this.r("collected " + this.e + " anchors");
      } else {
         this.r(_z.b.Q, "sell failed");
      }
   }

   private boolean i() {
      LocalPlayer var1 = mc.player;
      switch (this.rK) {
         case w:
            if (this.rw - this.X > 40 || !this.c()) {
               this.rK = _z.e.x;
               this.r9 = this.rw + 60;
               return false;
            } else if (!_p.Q(this)) {
               return true;
            } else {
               float[] var4 = this.ra.M(var1.getYRot(), var1.getXRot(), this.rI, this.rV, false);
               b6.P(this, b6.w.x, var4[0], var4[1], 1.0);
               int var3 = Math.abs(Mth.wrapDegrees(this.rI - var1.getYRot())) <= 0.75F && Math.abs(this.rV - var1.getXRot()) <= 0.75F ? 1 : 0;
               if (var3 != 0 && var1.getDeltaMovement().horizontalDistanceSqr() < 1.0E-5) {
                  this.d();
               }

               return true;
            }
         case T:
            int var2 = var1.position().distanceTo(this.rc) > 4.0 && var1.onGround() ? 1 : 0;
            if (var2 != 0 || this.rw - this.X > 100) {
               _q.z(this.getName(), var2 != 0 ? "Pearl landed at {}" : "Pearl throw from {} did not arrive", var1.blockPosition().toShortString());
               this.rK = _z.e.x;
               this.r9 = this.rw + 20;
               this.ro.V();
            }

            return true;
         default:
            if (this.rw < this.r9 || !this.c() || !var1.onGround()) {
               return false;
            } else if (!this.C()) {
               this.r9 = this.rw + 100;
               return false;
            } else {
               this.p();
               this.rK = _z.e.w;
               this.X = this.rw;
               return true;
            }
      }
   }

   private boolean c() {
      LocalPlayer var1 = mc.player;
      int var2 = bt.a(var0 -> var0.is(Items.ENDER_PEARL));
      return var2 >= 0
         && !var1.getCooldowns().isOnCooldown(var1.getInventory().getItem(var2))
         && var1.getArmorValue() >= 12
         && var1.getHealth() + var1.getAbsorptionAmount() >= 12.0F;
   }

   private boolean C() {
      d1 var1 = this.d(this.YD);
      au.q var2 = au.b(var1, this.YD.getX(), this.YD.getY(), this.YD.getZ(), this.o());
      ArrayList var3 = new ArrayList();

      for (au.m var5 : var2.exposed()) {
         if (var3.size() >= 3) {
            break;
         }

         var3.add(new Vec3(var5.x() + 0.5, var5.y(), var5.z() + 0.5));
      }

      if (var3.isEmpty()) {
         for (byte var15 = -4; var15 <= 4 && var3.size() < 3; var15 += 4) {
            for (byte var17 = -4; var17 <= 4 && var3.size() < 3; var17 += 4) {
               int var6 = this.YD.getX() + var15;
               int var7 = this.YD.getZ() + var17;
               int var8 = mc.level.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var6, var7);
               if (var1.floor(var6, var8 - 1, var7) && var1.open(var6, var8, var7) && var1.open(var6, var8 + 1, var7)) {
                  var3.add(new Vec3(var6 + 0.5, var8, var7 + 0.5));
               }
            }
         }
      }

      ne var16 = new ne(mc.level, mc.player);
      Vec3 var18 = mc.player.getEyePosition();

      for (Vec3 var20 : var3) {
         float var21 = (float)(Math.toDegrees(Math.atan2(var20.z - var18.z, var20.x - var18.x)) - 90.0);
         Vec3 var9 = null;
         float var10 = 0.0F;
         int var11 = Integer.MAX_VALUE;

         for (int var12 = -55; var12 <= 45; var12++) {
            s9.j var13 = s9.X(s9.h.thrown(var18, Vec3.directionFromRotation(var12, var21), Vec3.ZERO), var16);
            Vec3 var14 = var13.L();
            if (var14 != null && !(var14.distanceTo(var20) > 2.0) && var13.d() < var11) {
               var9 = var14;
               var10 = var12;
               var11 = var13.d();
            }
         }

         if (var9 != null) {
            this.rI = var21;
            this.rV = var10;
            this.rB = var9;
            _q.z(this.getName(), "Pearl throw toward {} (pitch {}, {} ticks of flight)", BlockPos.containing(var9).toShortString(), var10, var11);
            return true;
         }
      }

      _q.c(this.getName(), "No pearl throw lands near the anchor at {}", this.YD.toShortString());
      return false;
   }

   private void d() {
      LocalPlayer var1 = mc.player;
      this.rc = var1.position();
      _p.w(this, () -> {
         int var2 = bt.a(var0 -> var0.is(Items.ENDER_PEARL));
         if (var2 >= 0) {
            Inventory var3 = var1.getInventory();
            int var4 = var3.getSelectedSlot();
            var3.setSelectedSlot(var2);

            try {
               if (mc.gameMode.useItem(var1, InteractionHand.MAIN_HAND).consumesAction()) {
                  h8.i(var1, InteractionHand.MAIN_HAND);
                  this.rK = _z.e.T;
                  this.X = this.rw;
               }
            } finally {
               if (var3.getSelectedSlot() == var2) {
                  var3.setSelectedSlot(var4);
               }
            }
         }
      }, () -> mc.player == var1 && this.isRuntimeActive() && this.rK == _z.e.w);
   }

   private void X() {
      this.rZ.clear();
      this.y = Double.MAX_VALUE;
      double var1 = this.j.getValue();
      MCFriendModule var3 = LarpClient.N().j().y(MCFriendModule.class);
      String var4 = null;

      for (AbstractClientPlayer var6 : mc.level.players()) {
         if (var6 != mc.player && !var6.isSpectator()) {
            String var7 = var6.getGameProfile().name();
            if (!this.H.getValue() || var3 == null || !var3.M(var7)) {
               double var8 = var6.distanceTo(mc.player);
               if (!(var8 > var1)) {
                  this.rZ.add(new double[]{var6.getX(), var6.getZ()});
                  if (var8 < this.y) {
                     this.y = var8;
                     var4 = var7;
                  }
               }
            }
         }
      }

      if (var4 != null) {
         this.C = var4;
         if (!this.O) {
            this.O = (boolean)1;
            _q.z(this.getName(), "{} is {} blocks away; leaving by the next /rtp", var4, (int)this.y);
            this.X(_z.y.B, "Player " + var4 + " is " + (int)Math.round(this.y) + " blocks away, leaving");
         }
      }
   }

   private void x(long var1) {
      float var3 = mc.player.getHealth();
      if (var3 < this.K && mc.player.hurtTime > 0) {
         for (AbstractClientPlayer var5 : mc.level.players()) {
            if (var5 != mc.player && var5.distanceTo(mc.player) < 6.0F) {
               this.rR = var1;
               break;
            }
         }
      }

      this.K = var3;
   }

   private void N(long var1) {
      vs.B(this);
      if (mc.player.containerMenu instanceof ChestMenu) {
         mc.player.closeContainer();
      }

      if (this.T != _z.b.D && this.T != _z.b.d && this.T != _z.b.n) {
         this.r(_z.b.D, "player " + this.C);
      }

      if (this.rv.s(var1)) {
         this.F.g(this.YZ);
         if (this.rv.Z(var1)) {
            this.j("/rtp did not answer 5 times in a row");
            return;
         }
      }

      double var3 = this.rZ.isEmpty() ? 64.0 : this.y;
      va.v var5 = va.n(true, this.rv.h(var1), this.rv.O(), this.rz, var3, this.rv.K(var1), this.rd.getValue(), this.rR < 0L ? -1L : var1 - this.rR);
      switch (var5) {
         case v:
            this.Q(var1);
            break;
         case Z:
            this.I("a player came within " + (int)Math.round(var3) + " blocks during the /rtp cooldown");
            break;
         case D:
            this.P("Player nearby, standing still for the teleport");
            break;
         case f:
            this.M();
            this.P("Player nearby, running (/rtp " + (this.rv.O() ? "pending" : "in " + w(this.rv.K(var1))) + ")");
         case o:
      }
   }

   private void M() {
      this.k(24.0, 40);
   }

   private void k(double var1, int var3) {
      if (!this.rZ.isEmpty() || this.rX != null) {
         if (this.rX == null || this.rw - this.S > var3 || !this.ro.X()) {
            this.S = this.rw;
            if (this.rZ.isEmpty()) {
               return;
            }

            this.rX = va.g(mc.player.getX(), mc.player.getZ(), this.rZ, var1);
            BlockPos var4 = this.R();
            hn.j var5 = hn.j.around(var4.getX(), var4.getY(), var4.getZ(), var4.getX(), var4.getY(), var4.getZ(), (int)var1 + 8, 10, 10);
            hn.l var6 = hn.a(
               this.d(null), var4.getX(), var4.getY(), var4.getZ(), hn.c.Z(List.copyOf(this.rZ), var1), var5, hn.m.walking(8000).withPartial(true)
            );
            if (var6.found() && !var6.steps().isEmpty()) {
               this.ro.c(var6.steps(), var6.complete());
            }
         }

         ad.s var7 = this.ro.f(this.w(), this::s, true);
         this.Y();
         if (var7 == ad.s.Z || var7 == ad.s.d) {
            this.ro.V();
         }
      }
   }

   private boolean T(long var1) {
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
         if (this.YO.getValue() && va.J((boolean)var11, (boolean)var12, var3.getHealth())) {
            if (va.K((boolean)var11, (boolean)var12, _p.y(var10), var7, var3.getHealth())
               && var3.hasLineOfSight(var10)
               && (var6 == null || var13 < var6.distanceTo(var3))) {
               var6 = var10;
            }
         } else if (va.Y(var13, (boolean)var11, var3.hasLineOfSight(var10), var3.getHealth(), this.Yq)) {
            var4.add(var10);
            var5 |= var11;
         }
      }

      if (var4.isEmpty() && var6 != null) {
         this.Z(var6, var7);
         return true;
      }

      if (this.rO != null) {
         _q.z(this.getName(), "Done fighting; carrying on");
         this.rO = null;
      }

      if (var4.isEmpty()) {
         if (this.Yq) {
            this.Yq = (boolean)0;
            this.ro.V();
            this.rX = null;
            _q.z(this.getName(), "Away from the mobs; carrying on");
         }

         return false;
      } else {
         if (!this.Yq) {
            this.Yq = (boolean)1;
            if (this.YD != null && ++this.q >= 3) {
               _q.z(this.getName(), "Ran from mobs three times on the way to {}; leaving it", this.YD.toShortString());
               this.Z("mobs keep guarding it");
            }

            this.ro.V();
            this.rX = null;
            LivingEntity var15 = var4.stream().min(Comparator.comparingDouble(var1x -> var1x.distanceTo(var3))).orElseThrow();
            _q.z(
               this.getName(),
               "{} {} blocks away at {} health; running",
               var15.getType().getDescription().getString(),
               String.format(Locale.ROOT, "%.1f", var15.distanceTo(var3)),
               (int)var3.getHealth()
            );
         }

         vs.B(this);
         vi.J(this);
         this.rE.s();
         if (var3.containerMenu instanceof ChestMenu) {
            var3.closeContainer();
         }

         if (!this.rk && this.rv.h(var1)) {
            int var16 = this.rM;
            this.Q(var1);
            if (this.rM > var16) {
               _q.z(this.getName(), "{} close; left by /rtp", var5 != 0 ? "Creeper" : "Hostile mob");
            }
         }

         this.rZ.clear();

         for (LivingEntity var18 : var4) {
            this.rZ.add(new double[]{var18.getX(), var18.getZ()});
         }

         this.k(var5 != 0 ? 18.0 : 12.0, 20);
         if (!this.ro.X()) {
            this.n();
         }

         this.P(var5 != 0 ? "Running from a creeper" : "Running from hostile mobs");
         return true;
      }
   }

   private void Z(LivingEntity var1, double var2) {
      LocalPlayer var4 = mc.player;
      if (this.rO != var1) {
         this.rO = var1;
         _q.z(
            this.getName(),
            "Fighting a {} {} blocks away at {} health",
            var1.getType().getDescription().getString(),
            String.format(Locale.ROOT, "%.1f", var1.distanceTo(var4)),
            (int)var4.getHealth()
         );
      }

      vs.B(this);
      vi.J(this);
      this.rE.s();
      this.ro.V();
      int var5 = this.d();
      if (var5 >= 0 && var4.getInventory().getSelectedSlot() != var5) {
         var4.getInventory().setSelectedSlot(var5);
      }

      if (_p.Q(this)) {
         Vec3 var6 = var1.getBoundingBox().getCenter().add(0.0, var1.getBbHeight() * 0.2, 0.0);
         float[] var7 = b6.R(var4.getEyePosition(), var6);
         float[] var8 = this.ra.M(var4.getYRot(), var4.getXRot(), var7[0], var7[1], false);
         b6.P(this, b6.w.x, var8[0], var8[1], 1.0);
      }

      LivingEntity var9 = var1;
      _p.y(this, var9, var2, true, () -> this.V && var9.isAlive() && mc.player == var4);
      this.P("Fighting a " + var1.getType().getDescription().getString());
   }

   private int d() {
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

   private void n() {
      double[] var1 = va.g(mc.player.getX(), mc.player.getZ(), this.rZ, 3.0);
      if (var1 != null) {
         d1 var2 = this.d(null);
         BlockPos var3 = this.R();
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
            this.rD = new Vec3(var1[0] - mc.player.getX(), 0.0, var1[1] - mc.player.getZ());
            this.YQ = (boolean)(mc.player.getFoodData().getFoodLevel() > 6 ? 1 : 0);
         }
      }
   }

   private void I(String var1) {
      _q.z(this.getName(), "Logging out: {}", var1);
      String var2 = "Larp Anchor Hunter: " + var1 + ". " + this.e + " anchors collected.";
      ClientPacketListener var3 = mc.getConnection();
      this.j("logged out, " + var1);
      if (var3 != null && !mc.isLocalServer()) {
         var3.getConnection().disconnect(Component.literal(var2));
      }
   }

   private static boolean K(ItemStack var0) {
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

   private int U(Item var1) {
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

   private boolean p() {
      return this.a(new ItemStack(Items.RESPAWN_ANCHOR));
   }

   private boolean a(ItemStack var1) {
      Inventory var2 = mc.player.getInventory();

      for (int var3 = 0; var3 < 36; var3++) {
         ItemStack var4 = var2.getItem(var3);
         if (var4.isEmpty() || ItemStack.isSameItemSameComponents(var4, var1) && var4.getCount() < var4.getMaxStackSize()) {
            return true;
         }
      }

      return false;
   }

   private int v() {
      return this.isInGame() ? this.c(Items.ENDER_PEARL) : 0;
   }

   private int c(Item var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 9; var3++) {
         ItemStack var4 = mc.player.getInventory().getItem(var3);
         if (var4.is(var1)) {
            var2 += var4.getCount();
         }
      }

      return var2;
   }

   private boolean K() {
      return bt.a(var0 -> var0.is(Items.ENDER_CHEST)) >= 0 && this.u() >= 0;
   }

   static boolean w(ItemStack var0) {
      for (Holder var2 : var0.getEnchantments().keySet()) {
         if (var2.is(Enchantments.SILK_TOUCH)) {
            return true;
         }
      }

      return false;
   }

   private int u() {
      if (!this.isInGame()) {
         return -1;
      }

      BlockState var1 = Blocks.ENDER_CHEST.defaultBlockState();

      for (int var2 = 0; var2 < 9; var2++) {
         ItemStack var3 = mc.player.getInventory().getItem(var2);
         if (w(var3) && var3.isCorrectToolForDrops(var1) && bt.J(var3) > 5) {
            return var2;
         }
      }

      return -1;
   }

   private int k(BlockState var1) {
      if (!this.isInGame()) {
         return -1;
      }

      int var2 = -1;
      float var3 = -1.0F;

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = mc.player.getInventory().getItem(var4);
         if (var5.isCorrectToolForDrops(var1) && bt.J(var5) > 20) {
            float var6 = var5.getDestroySpeed(var1) + (var5.has(DataComponents.CUSTOM_NAME) ? 0 : 1000);
            if (var6 > var3) {
               var3 = var6;
               var2 = var4;
            }
         }
      }

      return var2;
   }

   private int s(BlockState var1) {
      Inventory var2 = mc.player.getInventory();
      int var3 = var2.getSelectedSlot();
      float var4 = -1.0F;

      for (int var5 = 0; var5 < 9; var5++) {
         ItemStack var6 = var2.getItem(var5);
         if (!var6.isDamageableItem() || bt.J(var6) > 5) {
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

   private boolean y() {
      int var1 = this.k(Blocks.RESPAWN_ANCHOR.defaultBlockState());
      return var1 >= 0 && mc.player.getInventory().getItem(var1).has(DataComponents.CUSTOM_NAME) && !Boolean.FALSE.equals(this.rJ);
   }

   private void A() {
      if (this.rJ == null && this.P.isEmpty() && this.y()) {
         BlockHitResult var1 = vs.r(this.YD, this.w());
         if (var1 != null) {
            Axis var2 = var1.getDirection().getAxis();

            for (int var3 = -1; var3 <= 1; var3++) {
               for (int var4 = -1; var4 <= 1; var4++) {
                  if (var3 != 0 || var4 != 0) {
                     BlockPos var5 = var2 == Axis.X
                        ? this.YD.offset(0, var3, var4)
                        : (var2 == Axis.Y ? this.YD.offset(var3, 0, var4) : this.YD.offset(var3, var4, 0));
                     if (vs.W(var5)) {
                        this.P.add(var5.immutable());
                     }
                  }
               }
            }
         }
      }
   }

   private void u() {
      if (!this.P.isEmpty() && this.rw - this.Y_ >= 10) {
         long var1 = this.P.stream().filter(var0 -> mc.level.getBlockState(var0).isAir()).count();
         this.rJ = var1 * 4L >= this.P.size() * 3L;
         _q.z(
            this.getName(),
            "Tool check: {} of {} blocks around the anchor broke, so the renamed pickaxe {}",
            var1,
            this.P.size(),
            this.rJ ? "cuts 3x3" : "breaks one block at a time"
         );
         this.i(this.rJ ? "your renamed pickaxe cuts 3x3, so it keeps away from lava and water." : "your renamed pickaxe breaks one block at a time here.");
         this.P.clear();
      }
   }

   private boolean W(BlockPos var1) {
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

   private double w() {
      return Math.min(4.5, mc.player.blockInteractionRange());
   }

   @Override
   protected void aimAt(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         if (_p.Q(this)) {
            float[] var3 = b6.R(mc.player.getEyePosition(), var1);
            BlockPos var4 = this.ro.I() != null ? this.ro.I() : (this.T == _z.b.N ? this.YD : null);
            int var5 = var4 != null && vs.r(var4, this.w()) != null ? 1 : 0;
            float[] var6 = this.ra.M(mc.player.getYRot(), mc.player.getXRot(), var3[0], var3[1], (boolean)var5);
            b6.P(this, b6.w.x, var6[0], var6[1], 1.0);
         }
      }
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         float[] var3 = this.ra.M(mc.player.getYRot(), mc.player.getXRot(), var1, var2, false);
         b6.P(this, b6.w.x, var3[0], var3[1], 1.0);
      }
   }

   private void e(Vec3 var1) {
      if (_p.Q(this)) {
         float[] var2 = b6.R(mc.player.getEyePosition(), var1);
         float[] var3 = this.ra.M(mc.player.getYRot(), mc.player.getXRot(), var2[0], var2[1], false);
         b6.P(this, b6.w.x, var3[0], var3[1], 1.0);
      }
   }

   public static Input x() {
      if (W != null && W.isEnabled()) {
         _z var0 = W;
         Minecraft var1 = Minecraft.getInstance();
         if (var0 != null
            && var0.isRuntimeActive()
            && var0.V
            && var1.player != null
            && var1.player == var0.r
            && var1.player.tickCount == var0.U
            && !ij.y()
            && !dt.x()
            && !(d1.E() instanceof AbstractContainerScreen)
            && (!b6.x() || b6.c(b6.w.x))) {
            int var2 = var0.rD.lengthSqr() > 1.0E-6 ? 1 : 0;
            if (var2 == 0 && !var0.L && !var0.rx) {
               return null;
            }

            Input var3 = var2 != 0 ? var0.ra.q(var1.player.getYRot(), var0.rD) : new Input(false, false, false, false, false, false, false);
            return new Input(var3.forward(), var3.backward(), var3.left(), var3.right(), var0.L, var0.rx, var0.YQ);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void i(String var1) {
      if (mc != null && mc.player != null) {
         mc.player.sendSystemMessage(Component.literal("Larp Anchor Hunter: " + var1));
      }
   }

   private void X(_z.y var1, String var2) {
      _q.c(this.getName(), "Notice {}: {}", var1, var2);
      if (var1 != _z.y.l) {
         long var3 = System.currentTimeMillis();
         if (var1 == _z.y.j || !var2.equals(this.Z) || var3 - this.rN >= 30000L) {
            this.Z = var2;
            this.rN = var3;
            String var5 = this.Y.getValue();
            if (!var5.equals("Off") || var1 == _z.y.j) {
               if (!var5.equals("Chat") && !var5.equals("Off")) {
                  ib.v var6 = switch (var1) {
                     case h -> ib.v.x;
                     case B, j -> ib.v.o;
                     default -> ib.v.f;
                  };
                  ib.L().N("Anchor Hunter", var2, var6, 5000L);
                  if (var1 == _z.y.j) {
                     this.i(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
                  }
               } else {
                  this.i(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
               }
            }
         }
      }
   }

   private static String I(String var0) {
      return var0.isEmpty() ? var0 : var0.substring(0, 1).toUpperCase(Locale.ROOT) + var0.substring(1);
   }

   static String r(int var0, String var1) {
      return var0 + " " + var1 + (var0 == 1 ? "" : "s");
   }

   static String w(long var0) {
      return (var0 + 999L) / 1000L + "s";
   }

   private void r() {
      if (this.isInGame()) {
         try {
            int var1 = com.larpclient.c.A(0.25);
            if (System.currentTimeMillis() < this.r5) {
               for (BlockPos var3 : this.ri) {
                  Gizmos.cuboid(new AABB(var3).inflate(0.02), GizmoStyle.strokeAndFill(-14249, 2.0F, 587188311)).setAlwaysOnTop();
               }
            }

            if (!this.V || !this.YT.getValue() || mc.level != this.r6) {
               return;
            }

            if (this.YD != null) {
               Gizmos.cuboid(new AABB(this.YD).inflate(0.03), GizmoStyle.strokeAndFill(-14249, 2.5F, 872400983)).setAlwaysOnTop();
            }

            List var12 = this.ro.I();
            double var13 = 0.05;
            Vec3 var5 = mc.player.position().add(0.0, var13, 0.0);

            for (hn.g var7 : var12) {
               Vec3 var8 = new Vec3(var7.x() + 0.5, var7.y() + var13, var7.z() + 0.5);
               Gizmos.line(var5, var8, vv.N(var1, 0.85), 2.0F).setAlwaysOnTop();
               var5 = var8;

               for (hn.n var10 : var7.digs()) {
                  Gizmos.cuboid(
                     new AABB(var10.x(), var10.y(), var10.z(), var10.x() + 1, var10.y() + 1, var10.z() + 1).inflate(0.01),
                     GizmoStyle.strokeAndFill(vv.N(-38037, 0.9), 1.5F, vv.N(-38037, 0.12))
                  );
               }
            }

            if (this.rK == _z.e.w && this.rB != null) {
               Gizmos.cuboid(
                     new AABB(this.rB.x - 0.5, this.rB.y, this.rB.z - 0.5, this.rB.x + 0.5, this.rB.y + 0.1, this.rB.z + 0.5),
                     GizmoStyle.strokeAndFill(-8530948, 2.0F, 863884284)
                  )
                  .setAlwaysOnTop();
            }
         } catch (RuntimeException var11) {
            _q.g(this.getName(), "Path overlay failed", var11);
         }
      }
   }

   @Override
   public ac.g hudCard() {
      if (this.isInGame() && this.f.getValue() && this.isRuntimeActive() && this.V && mc.level == this.r6) {
         long var1 = System.currentTimeMillis();
         double var3 = Math.max(1.0E-6, (var1 - this.B) / 3600000.0);
         int var5 = this.D.getValue().intValue();
         String var6 = this.O ? "Player " + this.C + " nearby" : r(this.e, "anchor") + (var1 - this.B > 60000L ? ", " + Math.round(this.e / var3) + "/h" : "");
         String var7 = this.rk ? "Singleplayer" : (this.rv.O() ? "/rtp waiting" : (this.rv.h(var1) ? "/rtp ready" : "/rtp in " + w(this.rv.K(var1))));
         String var8 = this.getStatus();
         return new ac.g(
            var6,
            var5 > 0 ? this.e + " of " + var5 : "",
            var8 != null && !var8.isBlank() ? var8 : "Starting",
            var7,
            var5 > 0 ? Math.clamp((double)this.e / var5, 0.0, 1.0) : -1.0,
            this.O
         );
      } else {
         return null;
      }
   }

   public _z.b f() {
      return this.T;
   }

   public boolean T() {
      return this.V;
   }

   public int r() {
      return this.e;
   }

   public BlockPos O() {
      return this.YD;
   }

   public int a() {
      return this.ro.U();
   }

   public double h() {
      return this.ro.V();
   }

   public enum b {
      j("Getting ready"),
      l("Sending /rtp"),
      d("Waiting for the teleport"),
      n("Standing still for the teleport"),
      s("Waiting for /rtp"),
      t("Landing"),
      Z("Looking for anchors"),
      Q("Choosing an anchor"),
      v("Going to an anchor"),
      N("Mining an anchor"),
      b("Picking up the anchor"),
      g("Putting anchors in the ender chest"),
      C("Selling anchors"),
      V("Protected area, leaving"),
      D("Player nearby, leaving"),
      F("Paused"),
      K("Stopped");

      final String G;

      b(String var3) {
         this.G = var3;
      }
   }

   private enum e {
      x,
      w,
      T;
   }

   enum y {
      l,
      h,
      B,
      j;
   }
}

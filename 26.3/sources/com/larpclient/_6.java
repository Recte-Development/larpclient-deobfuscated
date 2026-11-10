package com.larpclient;

import com.larp.obf.q41a5851bcf8e876a.q71cffb54eb30bf94;
import com.larpclient.module.FreecamModule;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SplittableRandom;
import java.util.Map.Entry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class _6 extends _2 implements ac, vi.d {
   private static _6 Tt;
   private static boolean CH;
   static final int rJ = 400;
   static final int T2 = 3;
   static final int rE = 2400;
   static final int CP = 600;
   static final int S = 3;
   static final int s = 40;
   private static final int CM = 8;
   private static final int T4 = 20000;
   private static final int TG = 2;
   static final int C4 = -1000000000;
   static final int CJ = 200;
   static final int TX = 60;
   private final com.larpclient.module.e Cf = this.addSetting(
      new com.larpclient.module.e("Corner 1", "First corner as x y z, or use Pick with camera", "").L("x y z")
   );
   private final com.larpclient.module.e T_ = this.addSetting(
      new com.larpclient.module.e("Corner 2", "Second corner as x y z, or use Pick with camera", "").L("x y z")
   );
   private final com.larpclient.module.e CI = this.addSetting(
      new com.larpclient.module.e(
            "Drill Name", "Part of your 3x3 drill's name, not case sensitive. The drill cuts 3x3 faces and a plain pickaxe cleans up the rest", "Drill"
         )
         .L("Drill")
   );
   private final com.larpclient.module.b r7 = this.g(
      "Layer Height", "How tall each layer is. The area is cleared from the top down, one layer at a time", 3.0, 1.0, 6.0, 1.0
   );
   private final com.larpclient.module.b rg = this.g("Pass Width", "How wide each row of a layer is", 3.0, 1.0, 5.0, 1.0);
   private final com.larpclient.module.b TJ = this.g("Reach", "How far the miner reaches, capped by your real reach", 4.5, 3.0, 6.0, 0.5);
   private final com.larpclient.module.m E = this.C(
      "Sweep Leftovers", "After the drill finishes a layer, mine every block still standing before moving down", true
   );
   private final com.larpclient.module.m uA = this.C("Sprint", "Sprint while walking", true);
   private final com.larpclient.module.m uf = this.C("Place Blocks", "Pillar up, bridge gaps and plug lava or water with solid blocks from your hotbar", true);
   private final com.larpclient.module.m r3 = this.C("Auto Eat", "Eat food at 10 hunger or less, and a golden apple after losing the amount below", true);
   private final com.larpclient.module.b K = this.g(
      "Gapple After Losing", "Hunger bars lost before a golden apple from your hotbar is eaten", 2.0, 1.0, 10.0, 1.0
   );
   private final com.larpclient.module.m TR = this.C("XP Repair", "When your tool runs low, throw XP bottles from your hotbar to mend it", true);
   private final com.larpclient.module.b Te = this.g(
      "Repair At", "Uses left on the tool that starts XP repair, or stops the run if it cannot be mended", 50.0, 1.0, 1000.0, 1.0
   );
   private final com.larpclient.module.m TA = this.C("Show Area", "Draw the selected area and the block being mined", true);
   private final com.larpclient.module.m V = this.C("Show Path", "Draw the route the miner is walking", true);
   private final com.larpclient.module.m uV = this.C(
      "Pause In GUI",
      "Stop while a menu other than chat or the pause menu is open, such as the ClickGUI. Chat and the pause menu never stop it; inventories always make it hold still",
      true
   );
   private final com.larpclient.module.m uu = this.C("Notify When Done", "Show a notification when the area is cleared", true);
   private final com.larpclient.module.m Cc = this.C(
      "Show Progress", "Show a small card at the top of the screen with the layer, progress and what the miner is doing", true
   );
   private final com.larpclient.module.m TC = this.C("Collect Drops", "After the last layer, walk over the items lying in the area to pick them up", true);
   private final com.larpclient.module.x uW = this.addSetting(
      new com.larpclient.module.x(
         "Select Area Key", "Press to pick the corners with a free camera: left click sets one corner, right click the other, Esc finishes", -1
      )
   );
   private final com.larpclient.module.m Cy = this.C(
      "Auto Tool",
      "Take the right hotbar tool for each block: a shovel for dirt, sand and gravel, an axe for wood, shears or a hoe for leaves. The drill still cuts its 3x3 faces. Off mines everything with your pickaxe",
      true
   );
   private final com.larpclient.module.o r = this.addSetting(
      new com.larpclient.module.o(
         "Drill Cut",
         "Auto watches what each drill hit breaks and uses 3x3 faces once it sees one. 3x3 trusts your drill from the first hit; One Block uses it like a normal pickaxe",
         "Auto",
         "Auto",
         "3x3",
         "One Block"
      )
   );
   private final com.larpclient.module.m u9 = this.C(
      "Crouch Sometimes",
      "Crouch where players do: while cutting the step down to the next layer and beside a drop, and now and then while mining with the pickaxe. Never for drill hits, which need you standing",
      true
   );
   private final com.larpclient.module.b To = this.g(
      "Camera Speed",
      "How fast the free camera flies while you pick corners, in blocks per second. The mouse wheel changes it until you finish. Your normal Freecam speed stays as it is",
      6.0,
      1.0,
      40.0,
      1.0
   );
   private ol Cx;
   private Object ub;
   private Object Cb;
   private int u4;
   private boolean Td;
   private boolean u_;
   private _6.t o = _6.t.d;
   private final List<BlockPos> rT = new ArrayList<>();
   private final List<BlockPos> ud = new ArrayList<>();
   private int TB = -1000000000;
   private int rB;
   private final Map<BlockPos, Integer> j = new HashMap<>();
   private final Map<BlockPos, Integer> f = new HashMap<>();
   private final Map<BlockPos, Integer> U = new HashMap<>();
   private final Map<BlockPos, Integer> TL = new HashMap<>();
   private final Map<BlockPos, BlockState> Ce = new HashMap<>();
   private final Map<BlockPos, String> C6 = new HashMap<>();
   private int I;
   private int TP;
   private final Map<BlockPos, Integer> rc = new HashMap<>();
   private static final int rL = 200;
   private final int[] rx = new int[5];
   private final int[] uo = new int[5];
   private final Map<BlockPos, Integer> C1 = new HashMap<>();
   private int T8;
   private final Set<BlockPos> u7 = new HashSet<>();
   private final Set<Long> b = new HashSet<>();
   private int ue;
   private int CX;
   private BlockPos C2;
   private BlockPos D;
   private BlockState TT;
   private final Map<BlockPos, BlockState> uX = new HashMap<>();
   private boolean Cz;
   private BlockPos CE;
   private int G;
   private Boolean uP;
   private BlockPos TI;
   private final Map<BlockPos, BlockState> Z = new HashMap<>();
   private int um;
   private final Set<BlockPos> rw = new HashSet<>();
   static final int CW = 10;
   private List<oy.i> x = List.of();
   private int T5;
   private int Tg;
   private int r5;
   private int T3;
   private Vec3 rS;
   private List<oy.i> CY = List.of();
   private BlockPos T6;
   private double B;
   private double u2;
   private double Tw = 0.245;
   private double CQ = 0.6;
   private final SplittableRandom TH = new SplittableRandom();
   private BlockPos TE;
   private int ux;
   private BlockPos ui;
   static final double uZ = 0.015;
   static final int Cj = 20;
   static final int uq = 50;
   private List<ay.k> C0 = List.of();
   private List<ay.k> X = List.of();
   private List<ay.k> R = List.of();
   private List<ay.k> uw;
   private final Set<ay.k> p = new HashSet<>();
   private List<ay.k> H = List.of();
   private int Cq = -1;
   private int rA = -1000000000;
   private int Tm = -1;
   private int Tx = -1000000000;
   private int up;
   private final Map<Long, Integer> Cd = new HashMap<>();
   private int uM = -1;
   private int Cl;
   private int ut;
   private int u1 = -1;
   private int rV = -1000000000;
   private int r6 = -1000000000;
   private int u5;
   private int L = -1;
   private int Ca = -1000000000;
   private boolean uT;
   private boolean ry;
   private boolean Cu;
   private final Set<BlockPos> ul = new HashSet<>();
   private int CV = -1;
   private BlockPos T1;
   private Direction r4;
   private boolean rk;
   private long TW = Long.MIN_VALUE;
   private float Tv;
   private float un;
   static final float uz = 2.5F;
   static final float rX = -2.0F;
   static final float ur = 6.0F;
   static final float u = 25.0F;
   static final double C = 0.12;
   static final int Ct = 3;
   static final int CS = 5;
   private final qi uN = new qi();
   private final Set<BlockPos> uY = new HashSet<>();
   static final int CT = 60;
   private boolean TD;
   private int CF;
   private int Cw;
   private float r9;
   private float CG;
   private BlockPos rK;
   private int Tf = -1000000000;
   private int ug;
   private double rQ;
   static final double uF = 0.035;
   static final int re = 80;
   static final double ro = 6.0;
   private final Set<BlockPos> rh = new HashSet<>();
   private BlockPos r_;
   private int l = -1;
   private boolean uR;
   private int C_;
   private Vec3 Cv;
   private double g;
   static final int CL = 30;
   private Vec3 rC = Vec3.ZERO;
   private boolean u0;
   private boolean Tq;
   private boolean Cg;
   private int d = Integer.MIN_VALUE;
   private int Cs = -1;
   private final Set<Item> uj = new HashSet<>();
   private BlockPos uG;
   private BlockPos ri;
   private boolean Tr;
   private int rG = -1;
   private int k;
   static final int Ty = 3;
   private final Set<BlockPos> Tz = new HashSet<>();
   private boolean uK;
   private boolean w;
   private boolean rP;
   private boolean rb;
   private BlockPos A;
   private final boolean[] Tb = new boolean[2];
   private Screen ra;
   static final double TM = 128.0;
   private boolean rz;
   private int us;
   private int uh;
   private final Set<Integer> Ts = new HashSet<>();
   static final int ru = 1200;
   private int C5;
   private int Ck;
   private boolean Co;
   private final hw rv = new hw(hw.b.QUICK);
   private final bt rf = new bt(this);
   private String Tk;
   private String u6;
   private String uQ;
   private int TS = -1;
   private boolean rY;
   private boolean CO;
   private final Set<BlockPos> c = new HashSet<>();
   private int TU = -1;
   private boolean Ci;
   private ol T9;
   private int O;
   static final int CC = 20000;
   private int ua;
   private final Set<BlockPos> Cp = new HashSet<>();
   static final int rR = 3;
   private int W = -1;
   private int N = -1;
   private int uS = -1000000000;
   private long CR;
   private long Cm;
   static final int rq = 256;
   static final int rD = 200000;
   static final long rm = 15000L;
   private Set<BlockPos> C9;
   private Set<BlockPos> u8;
   private String a = "";
   private String T0 = "";
   private final ad y = new ad(this, this.rv, var1 -> this.aimAt(var1, 12.0F));
   private int CU = -1000000000;
   private int e;
   private final List<BlockPos> T = new ArrayList<>();
   static final int CA = 8;
   static final int uU = 12000;
   private int uk;
   private int m;
   private long Ta;
   private String uI = "";
   static final long Cr = 5000000L;
   static final int TK = 6;
   private ay.k Y;
   private int TV = -1;
   private int C8 = -1;
   private int J;
   private int uC;
   private long Tu = Long.MIN_VALUE;
   private final Set<Long> uE = new HashSet<>();
   static final int rI = 32;
   static final int Tc = 3;
   private BlockPos CK;
   private int Ti = -1;
   static final int t = 2;
   private int uO;
   private int Tn;
   private String rF;
   private String CZ;
   private int q;
   private String CD;
   private String i;
   private int TO;
   static final int C7 = 16;
   private Set<Long> TZ = Set.of();
   private long r8 = Long.MIN_VALUE;
   private int uB = Integer.MIN_VALUE;
   private int uy = -1;
   private int rU = -1000000000;
   private int uL;
   private int Tl = -1;
   static final int F = 6;
   static final int Ch = 8;
   static final int Tj = 16;
   static final int uJ = 2;
   static final int T7 = 6;
   static final float uv = 1.5F;
   static final int P = 5;
   static final int TN = 3;
   private BlockPos Tp;
   private BlockPos C3;
   private int TY;
   private int TF;
   private int uD = -1000000000;
   private int uH;
   private int v;
   private final Map<BlockPos, Integer> rd = new HashMap<>();
   private float rl = 88.0F;
   private String TQ;
   static final double Cn = 0.18;
   static final double u3 = Math.cos(Math.toRadians(70.0));
   static final double CN = 0.75;
   private final Set<BlockPos> uc = new HashSet<>();
   private int CB;
   static final int Th = 10;

   private static int B(double var0) {
      return var0 < 2.0 ? 0 : (var0 < 3.0 ? 1 : (var0 < 3.5 ? 2 : (var0 < 4.0 ? 3 : 4)));
   }

   public _6() {
      super("Auto Area Miner", "Clear the box between two corners, top layer first, with your 3x3 drill and a pickaxe");
      Tt = this;
      this.K.a(this.r3::getValue);
      this.Te.a(this.TR::getValue);
      this.K.B("Golden Apple After");
      this.Te.B("Mend Tool Below");
      this.uV.B("Pause In Other Menus");
      this.Cf.B("Corner 1 (x y z)");
      this.T_.B("Corner 2 (x y z)");
      this.Cf.w(var1 -> this.Cm = System.currentTimeMillis() + 15000L);
      this.T_.w(var1 -> this.Cm = System.currentTimeMillis() + 15000L);
      if (!CH) {
         CH = (boolean)1;
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> {
            _6 var1 = Tt;
            if (var1 != null) {
               var1.c();
            }
         });
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            _6 var1 = Tt;
            if (var1 != null) {
               var1.d();
            }
         });
      }
   }

   public static _6 N() {
      return Tt;
   }

   public static String b(Collection<BlockPos> var0, String var1) {
      _6 var2 = Tt;
      if (var2 == null) {
         return "Auto Area Miner is not available in this edition.";
      }

      if (var2.isEnabled()) {
         return "Auto Area Miner is already on; turn it off first.";
      }

      String var3 = ac.K(var2);
      if (var3 != null) {
         return "Turn off " + var3 + " first; one bot runs at a time.";
      }

      if (var0 != null && !var0.isEmpty()) {
         HashSet var4 = new HashSet(var0.size() * 2);

         for (BlockPos var6 : var0) {
            var4.add(var6.immutable());
         }

         var2.Tk = null;
         var2.u8 = var4;
         var2.T0 = var1 == null ? "" : var1;
         var2.enable();
         return null;
      } else {
         return "There is nothing to mine.";
      }
   }

   public static boolean m(String var0) {
      _6 var1 = Tt;
      if (var1 != null && var1.isRuntimeActive() && var1.C9 != null && !var1.Ci) {
         _q.z(var1.getName(), "Mine Out stopped: {}", var0);
         var1.M("stopped mining out " + var1.a + ": " + var0 + ".");
         var1.disable();
         return true;
      } else {
         return false;
      }
   }

   public static boolean p() {
      _6 var0 = Tt;
      return var0 != null && var0.isRuntimeActive() && var0.C9 != null && !var0.Ci;
   }

   public static int n() {
      _6 var0 = Tt;
      return var0 != null && var0.C9 != null && !var0.Ci ? Math.max(0, var0.N) : -1;
   }

   @Override
   public void disable() {
      this.Tk = null;
      super.disable();
   }

   @Override
   public boolean allowsScreenInteractions() {
      Screen var1 = getMc() == null ? null : mc.gui.screen();
      return var1 == null || var1 instanceof ChatScreen || var1 instanceof PauseScreen || !this.uV.getValue();
   }

   @Override
   public List<ac.b> commands() {
      return List.of(
         new ac.b("Area", "Select the area with a free camera", this::R),
         new ac.b("Area", "Show the area", this::I),
         new ac.b("Area", "Clear corners", this::S),
         new ac.b("Drill", "Use the item in your hand", this::a),
         new ac.b("Drill", "Pick it from your inventory", this::z),
         new ac.b("Drill", "No drill, pickaxe only", this::x)
      );
   }

   @Override
   public String detail() {
      String var1 = this.CI.getValue().trim();
      if (var1.isEmpty()) {
         return "Drill: none, your best pickaxe mines everything.";
      }

      if (!this.isInGame()) {
         return "Drill: any item named like \"" + var1 + "\".";
      }

      int var2 = this.N();
      return var2 >= 0
         ? "Drill: " + mc.player.getInventory().getItem(var2).getHoverName().getString() + " (hotbar slot " + (var2 + 1) + ")."
         : "Drill: \"" + var1 + "\" is not in your hotbar, so the pickaxe mines everything.";
   }

   private String I() {
      ol.c var1 = this.q();
      if (var1 == null) {
         return "Set both corners first.";
      }

      if (!this.isInGame()) {
         return "Join a world first.";
      }

      this.Cm = System.currentTimeMillis() + 15000L;
      return "Showing " + var1.describe() + " for 15 seconds.";
   }

   private String R() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      String var1 = this.o();
      return var1 != null ? var1 : "Fly around and aim: left click sets corner 1, right click sets corner 2, Esc finishes.";
   }

   private String a() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      ItemStack var1 = mc.player.getMainHandItem();
      return var1.isEmpty() ? "Hold your drill first." : this.x(var1);
   }

   private String z() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.isRuntimeActive()) {
         return "Turn the miner off before changing the drill.";
      }

      this.rb = (boolean)1;
      this.ra = mc.gui.screen();
      mc.gui.setScreen(new InventoryScreen(mc.player));
      _q.c(this.getName(), "Picking the drill from the inventory");
      this.M("click your drill in your inventory. Nothing moves; Esc cancels.");
      return "Click your drill in your inventory. Nothing moves; Esc cancels.";
   }

   private String x() {
      this.CI.T("");
      this.B();
      return "No drill: your best pickaxe mines everything.";
   }

   private String x(ItemStack var1) {
      String var2 = var1.getHoverName().getString().trim();
      if (var2.length() > 128) {
         var2 = var2.substring(0, 128);
      }

      this.CI.T(var2);
      this.B();
      _q.z(this.getName(), "Drill set to {}", var2);
      return "Drill set to " + var2 + ".";
   }

   public static boolean D(ItemStack var0) {
      _6 var1 = Tt;
      if (var1 != null && var1.rb && var0 != null && !var0.isEmpty()) {
         var1.rb = (boolean)0;
         String var2 = var1.x(var0);
         var1.M(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
         Screen var3 = var1.ra;
         var1.ra = null;
         getMc().gui.setScreen(var3);
         return true;
      } else {
         return false;
      }
   }

   public static boolean H() {
      _6 var0 = Tt;
      return var0 != null && var0.rb;
   }

   private String S() {
      if (this.isRuntimeActive()) {
         return "Turn the miner off before clearing the corners.";
      }

      this.Cf.T("");
      this.T_.T("");
      this.Cm = 0L;
      this.B();
      return "Corners cleared.";
   }

   private void B() {
      LarpClient var1 = LarpClient.N();
      if (var1 != null && var1.h() != null) {
         var1.h().z();
      }
   }

   @Override
   public String summary() {
      if (this.C9 != null && this.Cx != null && this.isRuntimeActive()) {
         return "Mining out " + this.a + ": " + this.Cx.T().describe() + ". " + this.r() + ".";
      }

      ol.c var1 = this.q();
      if (var1 == null) {
         return "Set both corners to choose the area.";
      }

      String var2 = "Area: " + var1.describe();
      return this.isRuntimeActive() && this.Cx != null ? var2 + ". " + this.r() + "." : var2 + ". Stand on top of it and turn the miner on.";
   }

   @Override
   public List<ac.r> settingGroups() {
      return List.of(
         new ac.r("Area", List.of(this.To, this.uW))
            .with(
               new ac.m(
                  this::V,
                  List.of(
                     List.of(
                        this.k("Pick with camera", "Select the area with a free camera"),
                        this.k("Show", "Show the area").availableWhen(() -> this.q() != null),
                        this.k("Clear", "Clear corners").availableWhen(() -> this.q() != null)
                     )
                  )
               )
            ),
         new ac.r("Drill", List.of(this.CI, this.r))
            .with(
               new ac.m(
                  this::e,
                  List.of(
                     List.of(
                        this.k("Held item", "Use the item in your hand"),
                        this.k("From inventory", "Pick it from your inventory"),
                        this.k("No drill", "No drill, pickaxe only")
                     )
                  )
               )
            ),
         new ac.r("While mining", List.of(this.Cy, this.uf, this.TC, this.uA, this.u9)),
         new ac.r("Food and repair", List.of(this.r3, this.K, this.TR, this.Te)),
         new ac.r("Display", List.of(this.TA, this.V, this.Cc, this.uu)),
         ac.r.advanced("Advanced", List.of(this.Cf, this.T_, this.r7, this.rg, this.TJ, this.E, this.uV))
      );
   }

   private String V() {
      ol.c var1 = this.q();
      return var1 == null ? "Not set. Pick two opposite corners." : var1.describe() + (a(var1) != null ? ", too wide to mine" : "");
   }

   private String e() {
      if (!this.isInGame()) {
         return this.detail();
      }

      int var1 = this.H();
      return this.detail()
         + (var1 >= 0 ? " Pickaxe: " + mc.player.getInventory().getItem(var1).getHoverName().getString() + "." : " No pickaxe in your hotbar.");
   }

   @Override
   public ac.u status() {
      if (this.isRuntimeActive()) {
         return new ac.u(this.Cx == null ? "Starting." : (this.C9 != null ? "Mining out " + this.a + ". " : "") + this.r() + ".", false);
      }

      ol.c var1 = this.q();
      if (var1 == null) {
         return new ac.u("Set the area, then turn the module on.", false);
      }

      if (a(var1) != null) {
         return new ac.u("Each side across can be at most 256 blocks. Pick the corners again.", true);
      }

      if (!this.isInGame()) {
         return new ac.u("Area set. Join the world it is in.", false);
      }

      String var2 = ac.K(this);
      if (var2 != null) {
         return new ac.u(var2 + " is running. One bot runs at a time.", true);
      }

      BlockPos var3 = this.i();
      int var4 = Math.max(Math.max(var1.minX() - var3.getX(), var3.getX() - var1.maxX()), Math.max(var1.minZ() - var3.getZ(), var3.getZ() - var1.maxZ()));
      if (var4 > 16) {
         return new ac.u("You are " + var4 + " blocks from the area. Stand on it or next to it.", true);
      }

      if (var3.getY() < var1.minY()) {
         return new ac.u("You are below the area. Stand on top of it or inside it.", true);
      }

      int var5 = this.H();
      int var6 = this.N();
      if (var5 < 0 && var6 < 0) {
         return new ac.u("Put a pickaxe or your drill in your hotbar.", true);
      }

      int var7 = (int)Math.ceil(var1.sizeY() / this.r7.getValue());
      ArrayList var8 = new ArrayList();

      for (int var12 : new int[]{var6, var5}) {
         if (var12 >= 0) {
            var8.add(mc.player.getInventory().getItem(var12).getHoverName().getString());
         }
      }

      StringBuilder var13 = new StringBuilder(
         "Will clear " + var1.describe() + " in " + var7 + (var7 == 1 ? " layer" : " layers") + " with " + String.join(" and ", var8) + "."
      );
      if (this.uf.getValue() && this.i() < 0) {
         var13.append(" No blocks in your hotbar for bridging or plugging lava.");
      }

      if (this.r3.getValue() && bt.a(var0 -> var0.get(DataComponents.FOOD) != null) < 0) {
         var13.append(" No food in your hotbar.");
      }

      return new ac.u(var13.toString(), false);
   }

   private ol.c q() {
      int[] var1 = ol.z(this.Cf.getValue());
      int[] var2 = ol.z(this.T_.getValue());
      return var1 != null && var2 != null ? ol.c.of(var1, var2) : null;
   }

   private String o() {
      if (this.isRuntimeActive()) {
         return "Turn the miner off before choosing a new area.";
      }

      if (this.uK) {
         return null;
      }

      this.uK = (boolean)1;
      this.Tb[0] = this.Tb[1] = (boolean)0;
      this.A = null;
      mc.gui.setScreen(null);
      FreecamModule var1 = this.u();
      if (var1 != null && (var1.isEnabled() || !var1.E())) {
         var1.D(this.To.getValue());
         if (!var1.isEnabled()) {
            var1.setEnabled(true);
            this.w = var1.isEnabled();
         }

         if (!var1.isEnabled()) {
            var1.Q();
         }
      }

      int var2 = var1 != null && var1.isEnabled() ? 1 : 0;
      _q.z(this.getName(), "Picking corners{}", var2 != 0 ? " with Freecam at " + var1.O() + " blocks/s" : " from your own view");
      if (var2 != 0) {
         this.M("fly with the movement keys at " + this.To.getValue().intValue() + " blocks a second; the mouse wheel changes it. Esc finishes.");
      }

      return null;
   }

   private FreecamModule u() {
      return LarpClient.N().j().y(FreecamModule.class);
   }

   static String a(ol.c var0) {
      return var0.sizeX() <= 256 && var0.sizeZ() <= 256
         ? null
         : String.format(
            Locale.ROOT,
            "the area is %,d x %,d x %,d blocks; each side across can be at most %d. One corner is probably left from another world or server, so pick both corners again",
            var0.sizeX(),
            var0.sizeY(),
            var0.sizeZ(),
            256
         );
   }

   private void r(String var1) {
      if (this.uK) {
         this.uK = (boolean)0;
         this.A = null;
         FreecamModule var2 = this.u();
         if (var2 != null) {
            var2.Q();
            if (this.w && var2.isEnabled()) {
               var2.setEnabled(false);
            }
         }

         this.w = (boolean)0;
         this.B();
         ol.c var3 = this.q();
         if (var3 != null) {
            this.Cm = System.currentTimeMillis() + 15000L;
         }

         _q.z(
            this.getName(),
            "Corner picking ended ({}); area {}, picked corners {}{}",
            var1,
            var3 == null ? "incomplete" : var3.describe(),
            this.Tb[0] ? "1" : "",
            this.Tb[1] ? "2" : ""
         );
         if (this.isInGame()) {
            if (var3 == null) {
               this.M("corner picking ended; set both corners to choose the area.");
            } else if (a(var3) != null) {
               int var4 = this.Tb[0] && !this.Tb[1] ? 2 : (this.Tb[1] && !this.Tb[0] ? 1 : 0);
               this.M(
                  var4 > 0
                     ? "corner "
                        + var4
                        + " is still "
                        + (var4 == 1 ? this.Cf : this.T_).getValue()
                        + " from before, so the area is "
                        + var3.describe()
                        + ". Pick corner "
                        + var4
                        + " too ("
                        + (var4 == 1 ? "left" : "right")
                        + " click)."
                     : "the area is " + var3.describe() + "; each side across can be at most 256 blocks. Pick the corners again."
               );
            } else {
               this.M("area set: " + var3.describe() + ". Stand on top of it and turn the miner on.");
            }
         }
      }
   }

   public static boolean A() {
      _6 var0 = Tt;
      return var0 != null && var0.uK;
   }

   public static void W(int var0) {
      _6 var1 = Tt;
      if (var1 != null && var1.uK && var1.isInGame()) {
         BlockPos var2 = var1.N();
         if (var2 == null) {
            var1.M("aim at a block to set a corner.");
         } else {
            com.larpclient.module.e var3 = var0 == 0 ? var1.Cf : var1.T_;
            var3.T(ol.Q(var2.getX(), var2.getY(), var2.getZ()));
            var1.Tb[var0 == 0 ? 0 : 1] = (boolean)1;
            _q.c(var1.getName(), "Corner {} picked at {}", var0 + 1, var2.toShortString());
         }
      }
   }

   private BlockPos N() {
      boolean var1 = ij.y();
      Vec3 var2 = var1 ? ij.J(1.0F) : mc.player.getEyePosition();
      float var3 = var1 ? ij.D() : mc.player.getYRot();
      float var4 = var1 ? ij.J() : mc.player.getXRot();
      Vec3 var5 = var2.add(Vec3.directionFromRotation(var4, var3).scale(128.0));
      BlockHitResult var6 = mc.level.clip(new ClipContext(var2, var5, Block.OUTLINE, Fluid.NONE, mc.player));
      return var6.getType() == Type.BLOCK ? var6.getBlockPos().immutable() : null;
   }

   private void d() {
      if (!this.isInGame()) {
         if (this.uK) {
            this.uK = (boolean)0;
            this.w = (boolean)0;
            FreecamModule var3 = this.u();
            if (var3 != null) {
               var3.Q();
            }
         }

         this.rb = (boolean)0;
      } else {
         if (this.rb && !(mc.gui.screen() instanceof InventoryScreen)) {
            this.rb = (boolean)0;
            this.ra = null;
         }

         int var1 = this.uW.getValue() != -1 && mc.gui.screen() == null && mc.isWindowActive() && p4.Y(mc.getWindow().handle(), this.uW.getValue()) ? 1 : 0;
         if (var1 != 0 && !this.rP) {
            if (this.uK) {
               this.r("key pressed");
            } else {
               String var2 = this.o();
               if (var2 != null) {
                  this.M(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
               }
            }
         }

         this.rP = (boolean)var1;
         if (this.uK) {
            if (mc.gui.screen() != null) {
               this.r("menu opened");
            } else {
               this.A = this.N();
            }
         }
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.Z();
      if (this.uK) {
         this.r("miner turned on");
      }

      if (this.Tk != null) {
         String var20 = this.Tk;
         this.Tk = null;
         this.p("it stopped earlier because " + var20 + ". Turn it on again to carry on");
      } else if (this.u8 != null) {
         Set var19 = this.u8;
         this.u8 = null;
         this.P(var19, this.T0);
      } else {
         ol.c var1 = this.q();
         if (var1 == null) {
            this.p("set both corners first");
         } else {
            String var2 = a(var1);
            if (var2 != null) {
               this.p(var2);
            } else {
               String var3 = ac.K(this);
               if (var3 != null) {
                  this.p("turn off " + var3 + " first; one bot runs at a time");
               } else {
                  BlockPos var4 = this.i();
                  double var5 = Math.max(
                     Math.max(var1.minX() - var4.getX(), var4.getX() - var1.maxX()), Math.max(var1.minZ() - var4.getZ(), var4.getZ() - var1.maxZ())
                  );
                  if (var5 > 16.0) {
                     this.p("stand inside or next to the area to start (you are " + (int)var5 + " blocks away)");
                  } else if (var4.getY() < var1.minY()) {
                     this.p("you are below the area; stand on top of it or inside it");
                  } else {
                     int var7 = this.H();
                     int var8 = var7 < 0 && this.N() >= 0 ? 1 : 0;
                     if (var7 < 0 && var8 == 0) {
                        this.p("put a pickaxe or your drill in your hotbar");
                     } else {
                        ItemStack var9 = mc.player.getInventory().getItem(var8 != 0 ? this.N() : var7);
                        this.Cx = new ol(var1, this.r7.getValue().intValue(), this.rg.getValue().intValue(), var4.getX(), var4.getZ());
                        this.ub = mc.level;
                        this.Cb = mc.player;
                        this.rf.v();
                        this.Td = (boolean)(this.N() < 0 ? 1 : 0);
                        int var10 = var4.getY() > var1.maxY() ? 0 : Math.max(0, this.Cx.K(var4.getY()));
                        int var11 = var10 <= 0 || (!this.g().equals(this.uQ) || var10 < this.TS) && !this.t(var4) ? 0 : 1;
                        int var12 = var11 != 0 ? var10 : this.J();
                        if (var12 == -1) {
                           _q.z(this.getName(), "Nothing to mine in {}", var1.describe());
                           this.M("there is nothing to mine in " + var1.describe() + ": every block in it is gone already.");
                           this.rY = (boolean)1;
                           this.disable();
                        } else {
                           int var13 = var12 >= 0 && var12 < var10 ? 1 : 0;
                           int var14 = var13 != 0 ? this.g(this.Cx.i(var10).top()) : 0;
                           if (var13 == 0 || this.uf.getValue() && this.i() >= 0) {
                              if (var13 != 0 && var5 > 2.0) {
                                 this.p(
                                    "the layers above you still have "
                                       + var14
                                       + " blocks to mine. Stand on top of the area, or right next to it (you are "
                                       + (int)var5
                                       + " blocks away) so the miner can pillar up beside it"
                                 );
                              } else {
                                 this.u4 = var13 != 0 ? var12 : var10;
                                 this.CX = this.u4;
                                 this.CR = System.currentTimeMillis();
                                 this.W = this.N = this.D();
                                 this.uS = mc.player.tickCount;
                                 this.Cm = 0L;
                                 StringBuilder var15 = new StringBuilder(var9.getHoverName().getString());
                                 int var16 = this.N();
                                 if (var16 >= 0 && var8 == 0) {
                                    var15.insert(0, mc.player.getInventory().getItem(var16).getHoverName().getString() + " and ");
                                 }

                                 String var17 = var13 != 0
                                    ? ", pillaring up to layer " + (this.u4 + 1) + " first: the layers above you still have " + var14 + " blocks"
                                    : (
                                       this.u4 > 0
                                          ? ", starting at layer "
                                             + (this.u4 + 1)
                                             + " where you stand (the "
                                             + this.g(this.Cx.i(this.u4).top())
                                             + " blocks in the layers above you are mined at the end)"
                                          : ""
                                    );
                                 this.M("mining " + var1.describe() + " in " + this.Cx.n() + " layers with " + var15 + var17 + ".");
                                 ArrayList var18 = new ArrayList();
                                 if (var16 < 0) {
                                    var18.add("no hotbar item matches Drill Name \"" + this.CI.getValue() + "\", so the pickaxe mines everything");
                                 }

                                 if (var8 != 0) {
                                    var18.add(
                                       "your drill is your only pickaxe, so it only cuts where its 3x3 stays inside the area; add a plain pickaxe to the hotbar to clear the edges too"
                                    );
                                 }

                                 if (this.uf.getValue() && this.i() < 0) {
                                    var18.add("no solid blocks in the hotbar to pillar, bridge or plug lava with");
                                 }

                                 if (this.r3.getValue() && bt.a(var0 -> var0.get(DataComponents.FOOD) != null) < 0) {
                                    var18.add("no food in the hotbar");
                                 }

                                 if (this.TR.getValue()
                                    && (bt.e(var9) || var16 >= 0 && bt.e(mc.player.getInventory().getItem(var16)))
                                    && bt.a(var0 -> var0.is(Items.EXPERIENCE_BOTTLE)) < 0) {
                                    var18.add("no XP bottles in the hotbar to mend with");
                                 }

                                 if (!var18.isEmpty()) {
                                    this.M("heads up: " + String.join("; ", var18) + ".");
                                 }

                                 _q.z(
                                    this.getName(),
                                    "Started: {} from {}, {} layers from layer {}, rows along {}, pass width {}, drill slot {}, {} solid blocks{}",
                                    var1.describe(),
                                    var4.toShortString(),
                                    this.Cx.n(),
                                    this.u4 + 1,
                                    this.Cx.w() ? "X" : "Z",
                                    this.Cx.Z(),
                                    var16,
                                    this.W,
                                    var13 != 0 ? ", pillaring up from layer " + (var10 + 1) : (var11 != 0 ? ", carrying on where you stand" : "")
                                 );
                                 this.CO = (boolean)1;
                              }
                           } else {
                              this.p(
                                 "the layers above you still have "
                                    + var14
                                    + " blocks to mine. Stand on top of the area to start, or put blocks in your hotbar with Place Blocks on so the miner can pillar up to them"
                              );
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.isEnabled() && this.Cx != null) {
         this.Tk = "the world, the player or the connection changed";
         _q.z(this.getName(), "Interrupted after {} blocks: {}", this.ue, this.Tk);
      }

      if (this.Cx != null && this.C9 == null && this.CO) {
         this.uQ = this.rY ? null : this.g();
         this.TS = this.rY ? -1 : this.u4;
      }

      super.onDisable();
      vi.J(this);
      this.rf.b();
      this.Z();
   }

   private void Z() {
      this.Cx = null;
      this.ub = this.Cb = null;
      this.u4 = 0;
      this.Td = this.u_ = (boolean)0;
      this.o = _6.t.d;
      this.rT.clear();
      this.ud.clear();
      this.TB = -1000000000;
      this.rB = 0;
      this.j.clear();
      this.f.clear();
      this.U.clear();
      this.TL.clear();
      this.Ce.clear();
      this.C6.clear();
      this.rc.clear();
      this.u7.clear();
      this.b.clear();
      this.uN.N();
      this.rk = (boolean)0;
      this.TW = Long.MIN_VALUE;
      this.ue = 0;
      this.C2 = this.D = null;
      this.TT = null;
      this.uX.clear();
      this.Cz = (boolean)0;
      this.t();
      this.rS = null;
      this.CY = List.of();
      this.T6 = null;
      this.C_ = 0;
      this.Cv = null;
      this.TE = this.ui = this.r_ = null;
      this.ux = 0;
      this.rh.clear();
      this.l = -1;
      this.uR = (boolean)0;
      this.rC = Vec3.ZERO;
      this.u0 = this.Tq = this.Cg = (boolean)0;
      this.d = Integer.MIN_VALUE;
      this.Cs = -1;
      this.uj.clear();
      this.uG = this.ri = null;
      this.Ck = 0;
      this.Co = (boolean)0;
      this.rv.W();
      this.rz = (boolean)0;
      this.us = this.uh = 0;
      this.Ts.clear();
      this.CE = null;
      this.G = 0;
      this.CX = 0;
      this.uP = null;
      this.TI = null;
      this.Z.clear();
      this.um = 0;
      this.rw.clear();
      this.Tr = (boolean)0;
      this.Tz.clear();
      this.rG = -1;
      this.k = 0;
      this.W = this.N = -1;
      this.uS = -1000000000;
      this.CR = System.currentTimeMillis();
      this.rY = this.CO = (boolean)0;
      this.c.clear();
      this.TU = -1;
      this.Ci = (boolean)0;
      this.T9 = null;
      this.O = 0;
      this.ua = 0;
      this.Cp.clear();
      this.C9 = null;
      this.a = "";
      this.y.V();
      this.CU = -1000000000;
      this.e = 0;
      this.T.clear();
      this.C0 = this.X = this.R = this.H = List.of();
      this.uw = null;
      this.p.clear();
      this.Cd.clear();
      this.uM = -1;
      this.Cl = this.ut = this.u5 = 0;
      this.u1 = this.L = -1;
      this.rV = this.r6 = this.Ca = -1000000000;
      this.uT = this.ry = this.TD = this.Cu = (boolean)0;
      this.ul.clear();
      this.CV = -1;
      this.T1 = this.rK = null;
      this.r4 = null;
      this.uY.clear();
      this.Tf = -1000000000;
      this.CF = this.Cw = 0;
      this.Tp = this.C3 = null;
      this.TY = this.TF = this.uH = this.v = 0;
      this.uD = -1000000000;
      this.rd.clear();
      this.rl = 88.0F;
   }

   private void t() {
      this.x = List.of();
      this.T5 = 0;
      this.r5 = 0;
      this.T3 = 0;
   }

   @Override
   public void onTick() {
      long var1 = System.nanoTime();

      try {
         this.o();
      } finally {
         this.f(System.nanoTime() - var1);
      }
   }

   private void f(long var1) {
      if (mc.player != null) {
         if (var1 > 5000000L) {
            this.uk++;
            if (var1 > this.Ta) {
               this.Ta = var1;
               this.uI = this.getStatus();
            }
         }

         int var3 = mc.player.tickCount;
         if (var3 - this.m >= 200) {
            this.m = var3;
            this.O();
            if (this.uk != 0) {
               _q.z(
                  this.getName(),
                  "Miner ticks over 5 ms in the last 10 s: {}, longest {} ms while: {}",
                  this.uk,
                  String.format(Locale.ROOT, "%.1f", this.Ta / 1000000.0),
                  this.uI
               );
               this.uk = 0;
               this.Ta = 0L;
            }
         }
      }
   }

   private void o() {
      this.rC = Vec3.ZERO;
      this.u0 = this.Tq = this.Cg = this.TD = (boolean)0;
      this.TO = 0;
      if (this.Cx != null) {
         if (mc.level == this.ub && mc.player == this.Cb) {
            this.d = mc.player.tickCount;
            String var1 = bt.L();
            if (var1 != null) {
               vs.B(this);
               vi.J(this);
               this.rf.s();
               this.u0 = (boolean)1;
               this.P(var1 + ": swimming up");
            } else if (this.r3.getValue() && this.rf.T(this.K.getValue().intValue())) {
               vs.B(this);
               vi.J(this);
               this.P("Eating");
            } else if (this.uG != null) {
               this.q();
            } else {
               String var2 = this.B();
               if (var2 != null) {
                  this.f();
                  this.P(var2);
                  if (!var2.equals(this.u6)) {
                     this.u6 = var2;
                     _q.z(this.getName(), "Held: {}", var2);
                  }
               } else {
                  this.u6 = null;
                  if (this.F()) {
                     this.f();
                     this.rv.W();
                     this.P("Paused for manual movement");
                  } else if (mc.gui.screen() instanceof AbstractContainerScreen) {
                     this.f();
                     this.P("Holding still while an inventory menu is open");
                  } else if (!this.TR.getValue() || !this.L()) {
                     if (!this.O()) {
                        vs.B(this);
                        this.rC = Vec3.ZERO;
                        this.P("Waiting for the area to load");
                     } else if (!this.R()) {
                        if (this.C9 != null) {
                           this.W();
                        } else if (!mc.player.onGround()) {
                           vs.B(this);
                           this.P("Waiting to land");
                        } else {
                           this.e();
                           this.R();
                           this.n();
                           if (this.W >= 0 && mc.player.tickCount - this.uS >= 100) {
                              this.uS = mc.player.tickCount;
                              this.N = this.D();
                           }

                           if (this.rz) {
                              this.Q();
                           } else {
                              ol.k var3 = this.Cx.i(this.u4);
                              int var4 = this.L();
                              if (var4 >= var3.bottom()) {
                                 this.p(var3);
                              }

                              if (var4 > var3.bottom()) {
                                 this.o = _6.t.b;
                                 this.Y(var3);
                              } else if (var4 < var3.bottom()) {
                                 this.o = _6.t.f;
                                 this.t(var3);
                              } else {
                                 this.o = _6.t.d;
                                 this.c(var3);
                              }
                           }
                        }
                     }
                  }
               }
            }
         } else {
            this.p("the world or player changed");
         }
      }
   }

   private void f() {
      vs.B(this);
      vi.J(this);
      this.rC = Vec3.ZERO;
   }

   private boolean F() {
      return mc.isWindowActive()
         && mc.gui.screen() == null
         && (
            nz.K(mc.options.keyUp)
               || nz.K(mc.options.keyDown)
               || nz.K(mc.options.keyLeft)
               || nz.K(mc.options.keyRight)
               || nz.K(mc.options.keyJump)
               || nz.K(mc.options.keyShift)
         );
   }

   private boolean L() {
      int var1 = this.Te.getValue().intValue();

      for (int var5 : new int[]{this.Cs, this.N()}) {
         if (var5 >= 0 && !this.k(var5)) {
            ItemStack var6 = mc.player.getInventory().getItem(var5);
            if (bt.J(var6) <= var1 || this.rf.t()) {
               vs.B(this);
               bt.x var7 = this.rf.k(var5, var1);
               if (var7 == bt.x.a) {
                  this.P("Mending the tool with XP bottles");
                  return true;
               }

               if (var7 == bt.x.f) {
                  this.p(
                     "your "
                        + var6.getHoverName().getString()
                        + " has "
                        + bt.J(var6)
                        + " uses left and cannot be mended (it needs Mending and XP bottles in the hotbar)"
                  );
                  return true;
               }
            }
         }
      }

      return false;
   }

   private void c(ol.k var1) {
      if (!this.u_) {
         this.u_ = (boolean)1;
         BlockPos var2 = this.i();
         if (this.Cx.T().containsColumn(var2.getX(), var2.getZ())) {
            this.b.add(this.Y(var2.getX(), var2.getZ()));
         }

         _q.z(this.getName(), "Layer {} of {}: Y {} to {}", this.u4 + 1, this.Cx.n(), var1.bottom(), var1.top());
      }

      if (mc.player.tickCount - this.TB >= 40) {
         this.r(var1);
      }

      if (!this.uf.getValue() || !this.z(var1)) {
         if (!this.t(var1)) {
            BlockPos var7 = this.h(var1);
            if (var7 != null) {
               this.t();
               if (this.Cz) {
                  this.M(var7, true);
               } else {
                  this.L(var7, null);
               }
            } else {
               vs.B(this);
               BlockPos var3 = this.uT ? this.M(var1) : this.U(var1);
               if (var3 == null) {
                  if (mc.player.tickCount - this.TB > 2) {
                     this.r(var1);
                  } else {
                     byte var4 = 0;
                     if (this.ue != this.TU) {
                        for (BlockPos var6 : this.rT) {
                           if (this.j.containsKey(var6) && !this.u7.contains(var6) && vs.W(var6)) {
                              this.j.remove(var6);
                              var4 = 1;
                           }
                        }
                     }

                     if (var4 != 0) {
                        this.TU = this.ue;
                        _q.c(this.getName(), "Retrying blocks set aside in layer {}", this.u4 + 1);
                        this.t();
                     } else {
                        this.u(var1);
                     }
                  }
               } else {
                  this.H(var3, var1);
               }
            }
         }
      }
   }

   private void p(ol.k var1) {
      if (this.uM != this.u4) {
         this.uM = this.u4;
         this.Cl = this.ut = this.u5 = 0;
         this.u1 = this.L = -1;
         this.rV = this.r6 = this.Ca = -1000000000;
         this.uT = this.ry = this.Cu = (boolean)0;
         this.ul.clear();
         this.CV = -1;
         this.Cd.clear();
         this.Y = null;
         this.TV = this.C8 = -1;
         this.Tu = Long.MIN_VALUE;
         this.uE.clear();
         this.J = 0;
         this.uC = 0;
         this.CK = null;
         this.Ti = -1;
         this.p.clear();
         this.H = List.of();
         this.Cq = -1;
         this.rA = -1000000000;
         this.uw = null;
         this.Tm = -1;
         this.Tx = -1000000000;
         this.up = 0;
         if (this.C9 != null) {
            this.C0 = this.X = this.R = List.of();
            this.uT = (boolean)1;
         } else {
            int var2 = this.L();
            this.C0 = this.X = ay.n(this.Cx, this.u4, var2);
            this.R = ay.U(this.Cx, this.u4, var1.bottom(), true);
            this.z();
            long var3 = this.C0.stream().filter(var0 -> var0.kind() == ay.t.u).count();
            String var5 = var3 > 0L
               ? "a " + var3 + " block stair down from the corner"
               : (var2 > var1.bottom() ? "the first lane is too short for a stair, so it digs down at the corner" : "standing on the layer's floor");
            _q.z(
               this.getName(),
               "Layer {} dig order: {} stands, {} lanes {} wide along {}, {}",
               this.u4 + 1,
               this.C0.size(),
               this.Cx.w(),
               this.Cx.Z(),
               this.Cx.w() ? "X" : "Z",
               var5
            );
         }
      }
   }

   private void z() {
      this.Cd.clear();
      int var1 = 0;

      for (ay.k var3 : this.C0) {
         for (ay.c var5 : var3.cells()) {
            this.Cd.putIfAbsent(var5.key(), var1++);
         }
      }
   }

   private boolean V() {
      return this.Cx != null && this.C9 == null && !this.uT && this.uM == this.u4;
   }

   private boolean t(ol.k var1) {
      if (!this.V()) {
         return false;
      }

      int var2 = mc.player.tickCount;
      if (this.ue != this.CV) {
         this.ul.clear();
         this.CV = this.ue;
      }

      if (var2 < this.CF && !this.z()) {
         vs.B(this);
         this.P("Waiting for the drill's cut to land");
         return true;
      }

      if (this.w()) {
         return true;
      }

      if (var2 < this.Cw) {
         this.M();
         return true;
      }

      while (this.Cl < this.C0.size() && this.L(this.C0.get(this.Cl), var1)) {
         this.o(false);
      }

      if (this.Cl >= this.C0.size() && !this.f(var1, false)) {
         this.uT = (boolean)1;
         this.ul.clear();
         int var7 = 0;

         for (BlockPos var10 : this.rT) {
            if (this.n(var10, var1)) {
               var7++;
            }
         }

         _q.z(
            this.getName(),
            "Layer {}: dig order done after {} of {} stands{}{}{}",
            this.u4 + 1,
            this.ut,
            this.C0.size(),
            this.up > 0 ? ", moved to the nearest face " + this.up + " times" : "",
            var7 > 0 ? ", " + var7 + " blocks left for the clean-up" : "",
            this.uC > 0 ? ", " + this.uC + " passed over with no way to dig to them" : ""
         );
         return false;
      } else {
         BlockPos var3 = this.i();
         if (var3.getX() != this.C0.get(this.Cl).standX() || var3.getZ() != this.C0.get(this.Cl).standZ()) {
            this.N(var1);
         }

         ay.k var4 = this.C0.get(this.Cl);
         if (var3.getX() == var4.standX() && var3.getZ() == var4.standZ()) {
            this.t();
            this.TD = ad.m(var4.standX(), var1.bottom(), var4.standZ());
            if (this.Y() && this.i(var4, var1)) {
               this.Cu = (boolean)1;
               if (this.z(var4) && vs.W(new BlockPos(var4.standX() + var4.dx(), var1.bottom(), var4.standZ() + var4.dz()))) {
                  this.rC = new Vec3(var4.dx(), 0.0, var4.dz());
               }

               return true;
            } else {
               BlockPos var9 = this.H(var4, var1, true);
               if (var9 != null) {
                  this.Cu = (boolean)1;
                  this.h(var9, null);
                  return true;
               } else {
                  BlockPos var6 = this.d(var4, var1);
                  if (var6 != null) {
                     this.Cu = (boolean)1;
                     this.h(var6, null);
                     return true;
                  } else {
                     this.o(true);
                     return true;
                  }
               }
            }
         } else {
            if (this.X(var4, var3, var1)) {
               return true;
            }

            if (this.X(var4, var1)) {
               return true;
            }

            BlockPos var5 = this.H(var4, var1, false);
            if (var5 != null) {
               this.t();
               this.h(var5, null);
               return true;
            }

            this.t(var4, var1);
            if (this.F(var4, var1)) {
               return true;
            }

            this.A(var4, var1);
            this.o(false);
            return ++this.TO % 16 != 0 ? this.t(var1) : true;
         }
      }
   }

   private boolean X(ay.k var1, BlockPos var2, ol.k var3) {
      if (var1.kind() == ay.t.o && var1.drill() && this.Y() && this.z(var1) && var2.getY() == var3.bottom()) {
         int var4 = (var1.standX() - var2.getX()) * var1.dx() + (var1.standZ() - var2.getZ()) * var1.dz();
         int var5 = var1.dx() != 0 ? (var2.getZ() == var1.standZ() ? 1 : 0) : (var2.getX() == var1.standX() ? 1 : 0);
         if (var5 != 0 && var4 >= 1 && var4 <= 3) {
            for (int var6 = 1; var6 <= var4; var6++) {
               BlockPos var7 = var2.offset(var1.dx() * var6, 0, var1.dz() * var6);
               if (!this.P(var7) || !this.P(var7.above()) || !this.S(var7.below()) || ad.m(var7.getX(), var7.getY(), var7.getZ())) {
                  return false;
               }
            }

            ay.c var8 = var1.drillCentre();
            BlockPos var9 = new BlockPos(var8.x(), var8.y(), var8.z());
            if (!vs.W(var9.below())) {
               return false;
            }

            if (this.R(var9) && this.i(var1, var3)) {
               this.Cu = (boolean)1;
            }

            this.t();
            this.rC = new Vec3(var1.dx(), 0.0, var1.dz());
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean z(ay.k var1) {
      return Math.abs(Mth.wrapDegrees(mc.player.getYRot() - z(var1).toYRot())) <= 25.0F;
   }

   private void r(ay.k var1) {
      long var2 = var1.dx() + 2L << 40 ^ var1.dz() + 2L << 36 ^ (var1.dx() != 0 ? var1.standZ() : var1.standX()) & 4294967295L;
      if (var2 != this.TW) {
         this.TW = var2;
         this.Tv = (float)((this.TH.nextDouble() * 2.0 - 1.0) * 2.5);
         this.un = -2.0F + (float)this.TH.nextDouble() * 8.0F;
      }
   }

   static Vec3 N(Vec3 var0, BlockPos var1, Direction var2, float var3, float var4) {
      int var5 = var2.getAxis() == Axis.X ? 1 : 0;
      double var6 = var5 != 0 ? var1.getX() + (var2 == Direction.EAST ? 1 : 0) : var1.getZ() + (var2 == Direction.SOUTH ? 1 : 0);
      double var8 = Math.abs(var6 - (var5 != 0 ? var0.x : var0.z));
      int var10 = var5 != 0 ? var1.getZ() : var1.getX();
      double var11 = Mth.clamp((var5 != 0 ? var0.z : var0.x) + Math.tan(Math.toRadians(var3)) * var8, var10 + 0.12, var10 + 1 - 0.12);
      double var13 = Mth.clamp(var0.y - Math.tan(Math.toRadians(var4)) * var8, var1.getY() + 0.12, var1.getY() + 1 - 0.12);
      return var5 != 0 ? new Vec3(var6, var13, var11) : new Vec3(var11, var13, var6);
   }

   private boolean L(ay.k var1, ol.k var2) {
      return this.p.contains(var1) || this.d(var1, var2);
   }

   private void N(ol.k var1) {
      ay.k var2 = this.C0.get(this.Cl);
      BlockPos var3 = this.i();
      int var4 = mc.player.tickCount;
      int var5 = this.Cl == this.Tm && this.C0 == this.uw ? 0 : 1;
      boolean var6 = this.o(var1.bottom()).contains(oy.K(var2.standX(), var2.standZ()));
      if (var5 != 0 || !var6 && var4 - this.Tx >= 40) {
         this.Tx = var4;
         if (!var6 || Math.abs(var2.standX() - var3.getX()) + Math.abs(var2.standZ() - var3.getZ()) > 6) {
            this.f(var1, var5 == 0);
         }

         this.Tm = this.Cl;
         this.uw = this.C0;
      }
   }

   private List<ay.k> E(ol.k var1) {
      int var2 = mc.player.tickCount;
      if (this.Cq == this.ue && var2 - this.rA < 40) {
         this.H.removeIf(this.p::contains);
      } else {
         this.Cq = this.ue;
         this.rA = var2;
         ArrayList var3 = new ArrayList();

         for (ay.k var5 : this.X) {
            if (!this.L(var5, var1)) {
               var3.add(var5);
            }
         }

         for (ay.k var7 : this.R) {
            if (!this.L(var7, var1)) {
               var3.add(var7);
            }
         }

         this.H = var3;
      }

      return this.H;
   }

   private boolean f(ol.k var1, boolean var2) {
      BlockPos var3 = this.i();
      Set var4 = this.o(var1.bottom());
      ay.k var5 = ay.T(this.E(var1), var4, var3.getX(), var3.getZ());
      if (var5 == null) {
         return false;
      }

      boolean var6 = var4.contains(oy.K(var5.standX(), var5.standZ()));
      if (var2 && !var6) {
         return false;
      }

      if (this.Cl < this.C0.size() && this.C0.get(this.Cl).equals(var5)) {
         return true;
      }

      int var7 = this.C0.indexOf(var5);
      if (var7 < 0) {
         this.C0 = this.C0 == this.R ? this.X : this.R;
         var7 = this.C0.indexOf(var5);
         this.z();
      }

      this.Cl = var7;
      this.u1 = -1;
      this.TV = -1;
      this.ul.clear();
      if (this.up++ < 6) {
         _q.z(
            this.getName(),
            "Layer {}: going to the nearest face instead, stand {} of {} ({}) at {}, {}, {}, lanes {}",
            this.u4 + 1,
            this.Cl + 1,
            this.C0.size(),
            var5.kind(),
            var5.standX(),
            var5.standZ(),
            var6 ? "on the floor" : "off the floor",
            this.C0 == this.R ? "reversed" : "as planned"
         );
      }

      return true;
   }

   private ay.k z(ay.k var1, ol.k var2) {
      if (this.TV != this.Cl) {
         this.uE.clear();
      }

      if (this.TV == this.Cl && this.C8 == this.ue) {
         return this.Y;
      }

      this.TV = this.Cl;
      this.C8 = this.ue;
      this.Y = null;
      int var3 = var2.bottom();

      for (ay.b var5 : ay.U(this.o(var3), var1.standX(), var1.standZ(), 32)) {
         int var6 = var5.x() + var5.dx();
         int var7 = var5.z() + var5.dz();
         if (!this.uE.contains(oy.K(var6, var7)) && this.S(new BlockPos(var6, var3 - 1, var7))) {
            BlockPos var8 = new BlockPos(var6, var3 + 1, var7);
            BlockPos var9 = new BlockPos(var6, var3, var7);
            boolean var10 = this.P(var8);
            boolean var11 = this.P(var9);
            if ((!var10 || !var11) && (var10 || this.n(var8, var2)) && (var11 || this.n(var9, var2))) {
               ArrayList var12 = new ArrayList(2);
               if (!var10) {
                  var12.add(new ay.c(var6, var3 + 1, var7));
               }

               if (!var11) {
                  var12.add(new ay.c(var6, var3, var7));
               }

               this.Y = new ay.k(ay.t.W, var5.x(), var3, var5.z(), var5.dx(), var5.dz(), var12, false);
               break;
            }
         }
      }

      return this.Y;
   }

   private boolean F(ay.k var1, ol.k var2) {
      ay.k var3 = this.z(var1, var2);
      if (var3 == null) {
         return false;
      }

      BlockPos var4 = this.i();
      ay.c var5 = var3.cells().getFirst();
      long var6 = oy.K(var5.x(), var5.z());
      if (var4.getX() == var3.standX() && var4.getZ() == var3.standZ()) {
         this.t();
         if (this.Y() && this.f(var3, var2)) {
            this.Cu = (boolean)1;
            return true;
         } else {
            BlockPos var13 = this.H(var3, var2, true);
            if (var13 == null) {
               this.ul.clear();
               this.uE.add(var6);
               this.C8 = -1;
               return true;
            } else {
               this.Cu = (boolean)1;
               this.h(var13, "Digging a way to the next stand");
               return true;
            }
         }
      } else {
         long var8 = oy.K(var3.standX(), var3.standZ());
         if (this.Tu != var8 || this.x.isEmpty() && this.T5 == 0 || mc.player.tickCount - this.Tg > 40) {
            this.Tu = var8;
            this.Tg = mc.player.tickCount;
            this.u1 = -1;
            int var10 = var3.standX();
            int var11 = var3.standZ();
            List var12 = this.C(var2.bottom(), (var2x, var3x) -> var2x == var10 && var3x == var11);
            if (var12 == null) {
               this.t();
               this.uE.add(var6);
               this.C8 = -1;
               return true;
            }

            if (var12.isEmpty()) {
               return true;
            }

            this.x = var12;
            this.T5 = 0;
            this.ux = 0;
            this.q(var2, var3);
         }

         vs.B(this);
         this.D = new BlockPos(var5.x(), var5.y(), var5.z());
         this.n(var2);
         return true;
      }
   }

   private boolean f(ay.k var1, ol.k var2) {
      ay.c var3 = var1.drillCentre();
      BlockPos var4 = new BlockPos(var3.x(), var3.y(), var3.z());
      if (!this.ul.contains(var4) && this.n(var4, var2)) {
         Direction var5 = z(var1).getOpposite();
         int var6 = this.m(var4, var5, var2.bottom());
         return var6 >= 2 && this.Q(var4, var6) && this.b(var4, var5, "Drilling a way to the next stand (" + var6 + " blocks)");
      } else {
         return false;
      }
   }

   private String I(BlockPos var1) {
      return BuiltInRegistries.BLOCK.getKey(mc.level.getBlockState(var1).getBlock()).getPath();
   }

   private String P(ay.k var1, ol.k var2) {
      BlockPos var3 = new BlockPos(var1.standX(), var2.bottom(), var1.standZ());
      return String.format(
         Locale.ROOT,
         "stand %d of %d (%s) at %d, %d; player at %s, reachable floor %d cells; feet block %s%s, head %s%s, floor %s",
         this.Cl + 1,
         this.C0.size(),
         var1.kind(),
         var1.standX(),
         var1.standZ(),
         this.i().toShortString(),
         this.o(var2.bottom()).size(),
         this.I(var3),
         this.U(var3) ? " (set aside)" : "",
         this.I(var3.above()),
         this.U(var3.above()) ? " (set aside)" : "",
         this.I(var3.below())
      );
   }

   private void t(ay.k var1, ol.k var2) {
      if (this.TV != this.Cl && this.J < 3) {
         this.J++;
         _q.z(this.getName(), "Layer {}: {}; digging a way to it", this.u4 + 1, this.P(var1, var2));
      }
   }

   private void A(ay.k var1, ol.k var2) {
      if (this.uC++ < 3) {
         _q.z(this.getName(), "Layer {}: no way to dig to {}; passed over", this.u4 + 1, this.P(var1, var2));
      }
   }

   private BlockPos M(ol.k var1) {
      if (this.CK != null && this.Ti == this.ue && this.n(this.CK, var1)) {
         return this.CK;
      }

      this.Ti = this.ue;
      this.CK = null;
      double var2 = mc.player.getX();
      double var4 = mc.player.getZ();
      ArrayList var6 = new ArrayList();

      for (BlockPos var8 : this.rT) {
         if (this.n(var8, var1) && this.e(var8)) {
            var6.add(var8);
         }
      }

      var6.sort(Comparator.comparingDouble(var4x -> Math.pow(var4x.getX() + 0.5 - var2, 2.0) + Math.pow(var4x.getZ() + 0.5 - var4, 2.0)));
      int var10 = 0;

      for (BlockPos var9 : var6) {
         if (var10++ >= 32) {
            break;
         }

         if (this.S(var9, var1)) {
            this.CK = var9;
            break;
         }
      }

      return this.CK;
   }

   static Direction z(ay.k var0) {
      return var0.dx() > 0 ? Direction.EAST : (var0.dx() < 0 ? Direction.WEST : (var0.dz() > 0 ? Direction.SOUTH : Direction.NORTH));
   }

   private boolean i(ay.k var1, ol.k var2) {
      if (var1.kind() != ay.t.o && var1.kind() != ay.t.c) {
         return false;
      } else {
         ay.c var3 = var1.drillCentre();
         BlockPos var4 = new BlockPos(var3.x(), var3.y(), var3.z());
         if (!this.ul.contains(var4) && this.n(var4, var2)) {
            Direction var5 = z(var1).getOpposite();
            int var6 = this.m(var4, var5, var2.bottom());
            this.r(var1);
            return var6 >= 2 && this.Q(var4, var6) && this.U(var4, var5, "Cutting a 3x3 face at " + var4.toShortString() + " (" + var6 + " blocks)", true);
         } else {
            return false;
         }
      }
   }

   private boolean Y() {
      return this.N() >= 0 && this.f();
   }

   static List<BlockPos> s(BlockPos var0, Direction var1) {
      ArrayList var2 = new ArrayList(9);
      Axis var3 = var1.getAxis();

      for (int var4 = -1; var4 <= 1; var4++) {
         for (int var5 = -1; var5 <= 1; var5++) {
            var2.add(var3 == Axis.X ? var0.offset(0, var4, var5) : (var3 == Axis.Y ? var0.offset(var4, 0, var5) : var0.offset(var4, var5, 0)));
         }
      }

      return var2;
   }

   private int m(BlockPos var1, Direction var2, int var3) {
      int var4 = 0;

      for (BlockPos var6 : s(var1, var2)) {
         if (vs.W(var6)) {
            if (!this.Y(var6) || var6.getY() < var3 || this.p(var6) || this.O(var6)) {
               return -1;
            }

            var4++;
         }
      }

      return var4;
   }

   private boolean Q(BlockPos var1, int var2) {
      BlockState var3 = mc.level.getBlockState(var1);
      Inventory var4 = mc.player.getInventory();
      ItemStack var5 = var4.getItem(this.N());
      ItemStack var6 = var4.getItem(Math.max(0, this.B(var3, this.N())));
      return var5.getDestroySpeed(var3) * var2 >= var6.getDestroySpeed(var3) && (!var3.requiresCorrectToolForDrops() || var5.isCorrectToolForDrops(var3));
   }

   private boolean b(BlockPos var1, Direction var2, String var3) {
      return this.U(var1, var2, var3, false);
   }

   private boolean U(BlockPos var1, Direction var2, String var3, boolean var4) {
      if (this.uY.contains(var1)) {
         return false;
      }

      this.T1 = var1.immutable();
      this.r4 = var2;
      this.rk = var4;
      BlockHitResult var5 = vs.W(this, var1, this.n());
      if (var5 != null && var5.getDirection() == var2) {
         this.Cz = (boolean)1;
         this.uX.clear();

         for (BlockPos var7 : s(var1, var2)) {
            if (vs.W(var7)) {
               this.uX.put(var7.immutable(), mc.level.getBlockState(var7));
            }
         }

         this.b(var1);
         this.v(var1, true, var3);
         return true;
      } else {
         this.T1 = null;
         return false;
      }
   }

   private void L(BlockPos var1, String var2) {
      if (this.Y()) {
         BlockHitResult var3 = vs.W(this, var1, this.n());
         if (var3 != null) {
            int var4 = this.m(var1, var3.getDirection(), this.L());
            if (var4 >= 2 && this.Q(var1, var4) && this.b(var1, var3.getDirection(), "Cutting a 3x3 at " + var1.toShortString() + " (" + var4 + " blocks)")) {
               return;
            }
         }
      }

      this.h(var1, var2);
   }

   private boolean d(ay.k var1, ol.k var2) {
      if (var1.kind() == ay.t.u) {
         return true;
      }

      for (ay.c var4 : var1.cells()) {
         BlockPos var5 = new BlockPos(var4.x(), var4.y(), var4.z());
         if (!this.ul.contains(var5) && this.n(var5, var2) && !this.t(var1, var5)) {
            return false;
         }
      }

      return true;
   }

   private boolean t(ay.k var1, BlockPos var2) {
      if (!this.E.getValue() && var1.drill() && !this.Td && this.N() >= 0) {
         ay.c var3 = var1.drillCentre();
         return var2.getX() != var3.x() || var2.getZ() != var3.z() || var2.getY() > var3.y();
      } else {
         return false;
      }
   }

   private void o(boolean var1) {
      int var2 = !var1 && !this.Cu ? 0 : 1;
      if (var2 != 0) {
         this.rU = -1000000000;
      }

      this.Cu = (boolean)0;
      if (this.Cl < this.C0.size()) {
         this.p.add(this.C0.get(this.Cl));
      }

      this.Cl++;
      this.u1 = -1;
      this.ul.clear();
      if (var2 != 0) {
         this.ut++;
         if (this.TH.nextDouble() < 0.035 && (this.Td || this.N() < 0)) {
            this.Cw = mc.player.tickCount + this.TH.nextInt(8, 22);
            this.r9 = mc.player.getYRot() + (float)((this.TH.nextBoolean() ? 1 : -1) * (20.0 + this.TH.nextDouble() * 45.0));
            this.CG = Mth.clamp(mc.player.getXRot() - 12.0F + (float)this.TH.nextDouble() * 24.0F, -30.0F, 60.0F);
         }
      }
   }

   private void M() {
      vs.B(this);
      this.rC = Vec3.ZERO;
      this.aimAt(mc.player.getEyePosition().add(Vec3.directionFromRotation(this.CG, this.r9).scale(6.0)), 12.0F);
      this.P("Looking around");
   }

   private boolean w() {
      AABB var1 = mc.player.getBoundingBox().inflate(4.0, 6.0, 4.0);
      if (mc.level.getEntitiesOfClass(FallingBlockEntity.class, var1).isEmpty()) {
         return false;
      }

      vs.B(this);
      this.rC = Vec3.ZERO;
      this.P("Waiting for falling blocks to land");
      return true;
   }

   private BlockPos H(ay.k var1, ol.k var2, boolean var3) {
      double var4 = this.n();
      int var6 = 0;

      for (ay.c var8 : var1.cells()) {
         BlockPos var9 = new BlockPos(var8.x(), var8.y(), var8.z());
         if (!this.ul.contains(var9) && this.n(var9, var2) && !this.t(var1, var9)) {
            if (!this.R(var9)) {
               if (var3) {
                  this.ul.add(var9);
               }
            } else {
               if (!var3 && var6++ >= 8) {
                  return null;
               }

               BlockHitResult var10 = vs.W(this, var9, var4);
               if (var10 != null && this.i(var9, var10)) {
                  return var9;
               }

               if (var3) {
                  this.ul.add(var9);
                  _q.c(this.getName(), "No open face of {} from the stand at {}, {}; later", var9.toShortString(), var1.standX(), var1.standZ());
               }
            }
         }
      }

      return null;
   }

   private BlockPos d(ay.k var1, ol.k var2) {
      if (var1.cells().isEmpty()) {
         return null;
      }

      Integer var3 = this.Cd.get(var1.cells().getFirst().key());
      if (var3 != null && var3 != 0) {
         double var4 = this.n();
         Vec3 var6 = mc.player.getEyePosition();
         int var7 = var1.standX();
         int var8 = var1.standZ();
         int var9 = var7 + var1.dx();
         int var10 = var8 + var1.dz();
         ArrayList var11 = new ArrayList();

         for (BlockPos var13 : this.rT) {
            Integer var14 = this.Cd.get(ay.w(var13.getX(), var13.getY(), var13.getZ()));
            if (var14 != null && var14 < var3 && !this.ul.contains(var13)) {
               int var15 = (Math.abs(var13.getX() - var7) > 1 || Math.abs(var13.getZ() - var8) > 1)
                     && (Math.abs(var13.getX() - var9) > 1 || Math.abs(var13.getZ() - var10) > 1)
                  ? 0
                  : 1;
               if (var15 != 0 && !(var6.distanceToSqr(Vec3.atCenterOf(var13)) > var4 * var4) && this.n(var13, var2)) {
                  var11.add(var13);
               }
            }
         }

         if (var11.isEmpty()) {
            return null;
         }

         float var19 = mc.player.getYRot();
         float var20 = mc.player.getXRot();
         BlockPos var21 = this.TE;
         var11.sort(Comparator.comparingDouble(var6x -> b(var6, var6x, var19, var20, var4, var21)));
         int var22 = 0;

         for (BlockPos var17 : var11) {
            if (var22++ >= 8) {
               break;
            }

            BlockHitResult var18 = vs.W(this, var17, var4);
            if (var18 != null && this.i(var17, var18)) {
               return var17;
            }

            this.ul.add(var17);
         }

         return null;
      } else {
         return null;
      }
   }

   private void h(BlockPos var1, String var2) {
      this.T1 = null;
      this.Cz = (boolean)0;
      this.uX.clear();
      this.b(var1);
      this.v(var1, false, var2);
   }

   private boolean X(ay.k var1, ol.k var2) {
      BlockPos var3 = null;

      for (ay.c var5 : var1.cells()) {
         BlockPos var6 = new BlockPos(var5.x(), var5.y(), var5.z());
         if (this.n(var6, var2)) {
            var3 = var6;
            break;
         }
      }

      if (this.L == this.Cl && mc.player.tickCount - this.Ca < 40) {
         return false;
      }

      if (!this.o(var2.bottom()).contains(oy.K(var1.standX(), var1.standZ()))) {
         return false;
      }

      if (this.u1 != this.Cl || this.x.isEmpty() && this.T5 == 0 || mc.player.tickCount - this.Tg > 40) {
         this.u1 = this.Cl;
         this.Tg = mc.player.tickCount;
         int var7 = var1.standX();
         int var8 = var1.standZ();
         List var9 = this.C(var2.bottom(), (var2x, var3x) -> var2x == var7 && var3x == var8);
         if (var9 == null) {
            this.t();
            this.u1 = -1;
            this.L = this.Cl;
            this.Ca = mc.player.tickCount;
            return false;
         }

         if (var9.isEmpty()) {
            return true;
         }

         this.x = var9;
         this.T5 = 0;
         this.ux = 0;
         this.q(var2, var1);
         _q.c(this.getName(), "{} {} of {}: walking {} steps to {}, {}", var1.kind(), this.Cl + 1, this.C0.size(), this.x.size(), var7, var8);
      }

      vs.B(this);
      this.D = var3;
      this.n(var2);
      return true;
   }

   private void q(ol.k var1, ay.k var2) {
      this.CY = this.x;
      this.T6 = null;
      oy.i var3 = this.x.getLast();
      boolean var4 = ad.m(var3.x(), var1.bottom(), var3.z());
      double var5 = var4 ? 0.0 : -0.18 + this.TH.nextDouble() * 0.19999999999999998;
      double var7 = var4 ? 0.0 : (this.TH.nextDouble() * 2.0 - 1.0) * 0.12;
      this.B = var2.dx() * var5 - var2.dz() * var7;
      this.u2 = var2.dz() * var5 + var2.dx() * var7;
      this.Tw = var4 ? 0.245 : 0.15 + this.TH.nextDouble() * 0.15;
      this.CQ = 0.35 + this.TH.nextDouble() * 0.65;
      this.rQ = this.TH.nextDouble() * Math.PI * 2.0;
   }

   private boolean D(ol.k var1) {
      int var2 = this.L();
      ay.k var3 = null;

      for (ay.k var5 : this.C0) {
         if (var5.kind() == ay.t.u && var5.standY() == var2) {
            var3 = var5;
            break;
         }
      }

      if (var3 == null) {
         return this.D("no step planned from Y " + var2);
      }

      if (var2 != this.r6) {
         this.r6 = var2;
         this.u5 = 0;
      }

      if (this.w()) {
         return true;
      }

      BlockPos var13 = this.i();
      int var14 = var3.standX() + var3.dx();
      int var6 = var3.standZ() + var3.dz();
      BlockPos var7 = var3.cells().isEmpty()
         ? new BlockPos(var14, var2 - 1, var6)
         : new BlockPos(var3.cells().getFirst().x(), var3.cells().getFirst().y(), var3.cells().getFirst().z());
      if (var13.getX() == var14 && var13.getZ() == var6 && this.P(new BlockPos(var14, var2 - 1, var6))) {
         this.Z(var3, var2);
         return ++this.u5 > 80 ? this.D("could not step down to " + new BlockPos(var14, var2 - 1, var6).toShortString()) : true;
      }

      if (var13.getX() == var3.standX() && var13.getZ() == var3.standZ()) {
         this.t();

         for (int var15 = var2 - 1; var15 <= var2 + 1; var15++) {
            if (!var1.contains(var15) && !this.P(new BlockPos(var14, var15, var6))) {
               return this.D(
                  new BlockPos(var14, var15, var6).toShortString()
                     + (this.Cx.T().contains(var14, var15, var6) ? " was left by the layer above" : " above the area")
                     + " and is in the way"
               );
            }
         }

         BlockPos var16 = new BlockPos(var14, var2 - 1, var6);

         for (ay.c var18 : var3.cells()) {
            BlockPos var11 = new BlockPos(var18.x(), var18.y(), var18.z());
            if (!this.P(var11)) {
               AABB var12 = mc.player.getBoundingBox();
               if (var11.getY() == var2 - 1 && var12.maxX > var14 && var12.minX < var14 + 1 && var12.maxZ > var6 && var12.minZ < var6 + 1) {
                  vs.B(this);
                  this.z(new Vec3(var3.standX() + 0.5 - var3.dx() * 0.1, mc.player.getY(), var3.standZ() + 0.5 - var3.dz() * 0.1));
                  this.P("Stepping back from the edge");
                  if (++this.u5 > 80) {
                     return this.D("could not get clear of " + var16.toShortString());
                  }

                  return true;
               }

               if (!this.n(var11, var1)) {
                  return this.D(var11.toShortString() + " cannot be mined (set aside, beside a fluid or unbreakable)");
               }

               if (var11.equals(var16) && !this.u(var16, var1)) {
                  return this.D("the drop at " + var16.toShortString() + " is not safe");
               }

               this.TD = (boolean)1;
               this.h(var11, "Cutting a step down to layer " + (this.u4 + 1));
               return true;
            }
         }

         if (!this.u(var16, var1)) {
            return this.D("the drop at " + var16.toShortString() + " is not safe");
         }

         this.Z(var3, var2);
         return ++this.u5 > 80 ? this.D("could not step down to " + var16.toShortString()) : true;
      } else {
         if (this.rV != var2 || this.x.isEmpty() && this.T5 == 0) {
            this.rV = var2;
            int var8 = var3.standX();
            int var9 = var3.standZ();
            List var10 = this.C(var2, (var2x, var3x) -> var2x == var8 && var3x == var9);
            if (var10 == null) {
               return this.D("no way to the step at " + var8 + ", " + var2 + ", " + var9);
            }

            this.x = var10;
            this.T5 = 0;
            this.CY = List.of();
            this.ux = 0;
         }

         vs.B(this);
         this.D = var7;
         this.v(var2);
         this.P("Walking to the step down to layer " + (this.u4 + 1));
         return true;
      }
   }

   private void Z(ay.k var1, int var2) {
      vs.B(this);
      this.ux = 0;
      int var3 = var1.standX() + var1.dx();
      int var4 = var1.standZ() + var1.dz();
      Vec3 var5 = mc.player.position();
      Vec3 var6 = new Vec3(var3 + 0.5 - var5.x, 0.0, var4 + 0.5 - var5.z);
      double var7 = var6.length();
      double var9 = var7 < 1.0E-6 ? 0.0 : mc.player.getDeltaMovement().multiply(1.0, 0.0, 1.0).dot(var6.normalize());
      boolean var11 = this.rv.g(var7 + 0.2, var9, _p.Q(this));
      this.rC = var11 ? var6 : Vec3.ZERO;
      this.aimAt(new Vec3(var3 + 0.5 + var1.dx() * 0.7, var2 - 1, var4 + 0.5 + var1.dz() * 0.7), 12.0F);
      this.P("Stepping down to layer " + (this.u4 + 1));
   }

   private boolean D(String var1) {
      this.ry = (boolean)1;
      this.t();
      this.TD = (boolean)0;
      _q.z(this.getName(), "Layer {}: the stair down is not used ({}); digging down at the corner instead", this.u4 + 1, var1);
      return false;
   }

   private void P(Set<BlockPos> var1, String var2) {
      int[] var3 = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
      int[] var4 = new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};

      for (BlockPos var6 : var1) {
         var3[0] = Math.min(var3[0], var6.getX());
         var3[1] = Math.min(var3[1], var6.getY());
         var3[2] = Math.min(var3[2], var6.getZ());
         var4[0] = Math.max(var4[0], var6.getX());
         var4[1] = Math.max(var4[1], var6.getY());
         var4[2] = Math.max(var4[2], var6.getZ());
      }

      ol.c var12 = ol.c.of(var3, var4);
      this.a = var2.isBlank() ? "the block set" : var2;
      BlockPos var13 = this.i();
      double var7 = Math.max(
         Math.max(var12.minX() - var13.getX(), var13.getX() - var12.maxX()), Math.max(var12.minZ() - var13.getZ(), var13.getZ() - var12.maxZ())
      );
      if (var7 > 24.0) {
         this.p("stand next to " + this.a + " to mine it out (you are " + (int)var7 + " blocks away)");
      } else {
         int var9 = this.H();
         int var10 = var9 < 0 && this.N() >= 0 ? 1 : 0;
         if (var9 < 0 && var10 == 0) {
            this.p("put a pickaxe in your hotbar");
         } else {
            this.C9 = var1;
            this.Cx = new ol(var12, this.r7.getValue().intValue(), this.rg.getValue().intValue(), var13.getX(), var13.getZ());
            this.ub = mc.level;
            this.Cb = mc.player;
            this.rf.v();
            this.Td = (boolean)1;
            this.u4 = this.CX = 0;
            this.CR = System.currentTimeMillis();
            this.W = this.N = this.V();
            this.uS = mc.player.tickCount;
            this.Cm = 0L;
            String var11 = mc.player.getInventory().getItem(var10 != 0 ? this.N() : var9).getHoverName().getString();
            this.M(
               "mining out "
                  + this.a
                  + ": "
                  + this.W
                  + " blocks in "
                  + var12.describe()
                  + " with "
                  + var11
                  + (var10 != 0 ? ", only where its cut stays inside the schematic" : "")
                  + ", from the top down."
            );
            if (this.r3.getValue() && bt.a(var0 -> var0.get(DataComponents.FOOD) != null) < 0) {
               this.M("heads up: no food in the hotbar.");
            }

            _q.z(this.getName(), "Block set started: {} cells of {} in {}, {} still standing, tool {}", var1.size(), this.a, var12.describe(), this.W, var11);
         }
      }
   }

   private int V() {
      int var1 = 0;

      for (BlockPos var3 : this.C9) {
         if (vs.W(var3)) {
            var1++;
         }
      }

      return var1;
   }

   private void W() {
      if (!mc.player.onGround() && !mc.player.isInWater() && !this.y.X()) {
         vs.B(this);
         this.P("Waiting to land");
      } else {
         this.e();
         this.R();
         this.n();
         if (mc.player.tickCount - this.uS >= 100) {
            this.uS = mc.player.tickCount;
            this.N = this.V();
         }

         if (this.rz) {
            this.Q();
         } else {
            if (mc.player.tickCount - this.TB >= 40) {
               this.C();
            }

            int var1 = this.P();
            if (var1 < 0) {
               if (mc.player.tickCount - this.TB > 2) {
                  this.C();
               } else {
                  byte var5 = 0;

                  for (BlockPos var4 : this.rT) {
                     if (this.j.containsKey(var4) && !this.u7.contains(var4) && vs.W(var4)) {
                        this.j.remove(var4);
                        var5 = 1;
                     }
                  }

                  if (var5 != 0) {
                     _q.c(this.getName(), "Retrying block-set cells set aside");
                     this.y.V();
                  } else {
                     this.y.V();
                     this.N = this.V();
                     if (this.TC.getValue()) {
                        this.rz = (boolean)1;
                        this.P("Picking up drops");
                        _q.z(this.getName(), "Picking up drops around {}", this.a);
                     } else {
                        this.X();
                     }
                  }
               }
            } else {
               this.u4 = var1;
               ol.k var2 = this.Cx.i(var1);
               BlockPos var3 = this.y.X() && this.y.I() != null ? null : this.h(var2);
               if (var3 != null) {
                  this.y.V();
                  this.L(var3, null);
               } else {
                  vs.B(this);
                  this.E(var2);
               }
            }
         }
      }
   }

   private void C() {
      this.TB = mc.player.tickCount;
      this.rT.clear();
      this.ud.clear();
      int var1 = mc.player.tickCount;
      this.j.values().removeIf(var1x -> var1x <= var1);

      for (BlockPos var3 : this.C9) {
         if (mc.level.hasChunkAt(var3) && vs.W(var3)) {
            this.rT.add(var3);
         }
      }

      this.rT
         .sort(
            Comparator.<BlockPos>comparingInt(var1x -> this.Cx.K(var1x.getY()))
               .thenComparingLong(var1x -> this.Cx.A(var1x.getX(), var1x.getY(), var1x.getZ(), this.Cx.K(var1x.getY())))
         );
   }

   private int P() {
      for (BlockPos var2 : this.rT) {
         int var3 = this.Cx.K(var2.getY());
         if (var3 >= 0 && this.n(var2, this.Cx.i(var3))) {
            return var3;
         }
      }

      return -1;
   }

   private void E(ol.k var1) {
      BlockPos var2 = BlockPos.containing(mc.player.getX(), mc.player.getY() + 0.2, mc.player.getZ());
      if (!this.y.X() || mc.player.tickCount - this.CU > 100) {
         this.CU = mc.player.tickCount;
         Vec3 var3 = mc.player.getEyePosition();
         this.T.clear();
         this.rT
            .stream()
            .filter(var2x -> this.n(var2x, var1))
            .sorted(Comparator.comparingDouble(var1x -> var3.distanceToSqr(Vec3.atCenterOf(var1x))))
            .limit(8L)
            .forEach(this.T::add);
         if (this.T.isEmpty()) {
            this.y.V();
            return;
         }

         final double var4 = Math.pow(this.n() - 0.4, 2.0);
         final double var6 = mc.player.getEyeHeight();
         final ArrayList var8 = new ArrayList<>(this.T);
         hn.c var9 = new hn.c() {
            @Override
            public boolean reached(int var1, int var2x, int var3x) {
               for (BlockPos var5 : var8) {
                  if (var5.getX() != var1 || var5.getZ() != var3x || var5.getY() > var2x || var5.getY() < var2x - 1) {
                     double var6x = var1 + 0.5 - (var5.getX() + 0.5);
                     double var8x = var2x + var6 - (var5.getY() + 0.5);
                     double var10 = var3x + 0.5 - (var5.getZ() + 0.5);
                     if (var6x * var6x + var8x * var8x + var10 * var10 <= var4) {
                        return true;
                     }
                  }
               }

               return false;
            }

            @Override
            public double estimate(int var1, int var2x, int var3x) {
               double var4x = Double.MAX_VALUE;

               for (BlockPos var7 : var8) {
                  var4x = Math.min(
                     var4x,
                     Math.sqrt(
                        Math.pow(var1 + 0.5 - var7.getX() - 0.5, 2.0)
                           + Math.pow(var2x + var6 - var7.getY() - 0.5, 2.0)
                           + Math.pow(var3x + 0.5 - var7.getZ() - 0.5, 2.0)
                     )
                  );
               }

               return Math.max(0.0, var4x - Math.sqrt(var4));
            }
         };
         ol.c var10 = this.Cx.T();
         hn.j var11 = new hn.j(
            Math.min(var10.minX(), var2.getX()) - 8,
            Math.min(var10.minY(), var2.getY()) - 4,
            Math.min(var10.minZ(), var2.getZ()) - 8,
            Math.max(var10.maxX(), var2.getX()) + 8,
            Math.max(var10.maxY(), var2.getY()) + 4,
            Math.max(var10.maxZ(), var2.getZ()) + 8
         );
         d1 var12 = new d1(var1x -> this.B(var1x, this.N()), false);
         if (this.Ci) {
            var12.J(this.T9.T().minX(), this.T9.T().minY(), this.T9.T().minZ(), this.T9.T().maxX(), this.T9.T().maxY(), this.T9.T().maxZ());
         } else {
            var12.L(this.C9);
         }

         hn.l var13 = hn.a(var12, var2.getX(), var2.getY(), var2.getZ(), var9, var11, hn.m.digging(12000));
         if (!var13.found() || var13.steps().isEmpty()) {
            this.y.V();
            String var14 = var13.found() ? "not visible from any standing spot nearby" : "no way to stand within reach";

            for (BlockPos var16 : this.T) {
               this.D(var16, var14);
            }

            _q.c(this.getName(), "Block set: {} for {} cells near {}", var14, this.T.size(), this.T.getFirst().toShortString());
            if (this.A() >= 3) {
               this.G(var1, var14);
            }

            this.uL++;
            return;
         }

         this.y.s(var13.steps());
         _q.c(this.getName(), "Block set route of {} steps toward {}", var13.steps().size(), this.T.getFirst().toShortString());
      }

      ad.s var17 = this.y.f(this.n(), var1x -> this.B(var1x, this.N()), this.uA.getValue());
      this.rC = this.y.L();
      this.u0 = this.y.p();
      this.Tq = this.y.v();
      this.Cg = this.y.d();
      this.P("Walking to " + (this.T.isEmpty() ? "the next block" : this.T.getFirst().toShortString()));
      if (var17 == ad.s.Z) {
         this.y.V();
         this.e = 0;
      } else if (var17 == ad.s.d) {
         _q.c(this.getName(), "Block set route stuck: {}", this.y.I());
         this.y.V();
         if (++this.e >= 3) {
            this.e = 0;

            for (BlockPos var5 : this.T) {
               this.D(var5, "stuck on the way");
            }
         }
      }
   }

   private void O() {
      if (this.q > 0) {
         _q.z(this.getName(), "Gave up on {} blocks in the last 10 s (first {}): {}", this.q, this.CD, this.i);
         this.q = 0;
         this.CD = this.i = null;
      }

      if (this.Tn != 0) {
         _q.z(this.getName(), "Set aside {} blocks in {} places in the last 10 s (first around {}): {}", this.uO, this.Tn, this.rF, this.CZ);
         this.uO = this.Tn = 0;
         this.rF = this.CZ = null;
      }
   }

   private Set<Long> o(int var1) {
      BlockPos var2 = this.i();
      long var3 = oy.K(var2.getX(), var2.getZ());
      int var5 = mc.player.tickCount;
      if (var1 != this.uB || this.ue != this.uy || var5 - this.rU >= 40 || var3 != this.r8 && !this.TZ.contains(var3)) {
         this.r8 = var3;
         this.uB = var1;
         this.uy = this.ue;
         this.rU = var5;
         ol.c var6 = this.Cx.T();
         oy.y var7 = new oy.y(
            Math.min(var6.minX(), var2.getX()) - 2,
            Math.min(var6.minZ(), var2.getZ()) - 2,
            Math.max(var6.maxX(), var2.getX()) + 2,
            Math.max(var6.maxZ(), var2.getZ()) + 2
         );
         HashSet var8 = new HashSet();
         ArrayDeque var9 = new ArrayDeque();
         var8.add(var3);
         var9.add(new long[]{var2.getX(), var2.getZ()});
         this.Tr = (boolean)(this.uf.getValue() && this.C9 == null ? 1 : 0);

         try {
            while (!var9.isEmpty() && var8.size() < 20000) {
               long[] var10 = (long[])var9.poll();

               for (int[] var14 : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
                  int var15 = (int)var10[0] + var14[0];
                  int var16 = (int)var10[1] + var14[1];
                  long var17 = oy.K(var15, var16);
                  if (!var8.contains(var17) && var7.contains(var15, var16) && this.H(var15, var1, var16)) {
                     var8.add(var17);
                     var9.add(new long[]{var15, var16});
                  }
               }
            }
         } finally {
            this.Tr = (boolean)0;
         }

         this.TZ = var8;
         return this.TZ;
      } else {
         return this.TZ;
      }
   }

   private boolean S(BlockPos var1, ol.k var2) {
      Set var3 = this.o(var2.bottom());
      if (var3.size() > 4000) {
         return true;
      }

      double var4 = this.n() - 0.4;
      double var6 = var2.bottom() + mc.player.getEyeHeight();
      Vec3 var8 = Vec3.atCenterOf(var1);

      for (long var10 : var3) {
         double var12 = oy.I(var10) + 0.5 - var8.x;
         double var14 = var6 - var8.y;
         double var16 = oy.d(var10) + 0.5 - var8.z;
         if (var12 * var12 + var14 * var14 + var16 * var16 <= var4 * var4) {
            return true;
         }
      }

      return false;
   }

   private int A() {
      if (this.ue != this.Tl) {
         this.Tl = this.ue;
         this.uL = 0;
      }

      return this.uL;
   }

   private void z(BlockPos var1, ol.k var2, String var3) {
      int var4 = mc.player.tickCount;
      int var5 = 0;

      for (BlockPos var7 : this.rT) {
         if (!var7.equals(var1) && var7.distChessboard(var1) <= 2 && this.n(var7, var2)) {
            this.j.put(var7, var4 + 400);
            var5++;
         }
      }

      if (var5 != 0) {
         this.uO += var5;
         this.Tn++;
         if (this.rF == null) {
            this.rF = var1.toShortString();
            this.CZ = var3;
         }
      }
   }

   private void G(ol.k var1, String var2) {
      int var3 = 0;

      for (BlockPos var5 : this.rT) {
         if (this.n(var5, var1)) {
            this.u7.add(var5.immutable());
            var3++;
         }
      }

      if (var3 != 0) {
         int var6 = this.uf.getValue() && this.i() >= 0 ? 1 : 0;
         String var7 = var2 + (var6 != 0 ? "" : ", and there are no blocks in the hotbar to pillar up with");
         _q.z(this.getName(), "Leaving {} blocks of {} at Y {} to {}: {}", var3, this.a, var1.bottom(), var1.top(), var7);
         this.M("left " + var3 + " blocks at Y " + var1.bottom() + " to " + var1.top() + ": " + var7 + ".");
         this.P("Left " + var3 + " blocks out of reach");
      }
   }

   private void n() {
      if (this.C2 != null && !vs.W(this.C2)) {
         if (mc.level.getBlockState(this.C2).isAir()) {
            this.ue++;
            if (this.TT != null) {
               this.Ce.put(this.C2, this.TT);
               int var1 = mc.player.tickCount;
               if (this.rc.size() < 256) {
                  this.rc.put(this.C2, var1);
                  int var2 = B(Math.sqrt(mc.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(this.C2))));
                  this.C1.put(this.C2, var2);
                  this.rx[var2]++;
               }

               this.C6
                  .put(
                     this.C2,
                     String.format(
                        Locale.ROOT,
                        "broken with %s%s after %d ticks on it, %d ticks after the break before, %s",
                        mc.player.getMainHandItem().getHoverName().getString(),
                        this.Cz ? " as a drill face" : "",
                        var1 - this.I,
                        var1 - this.TP,
                        mc.player.onGround() ? "on the ground" : "in the air"
                     )
                  );
               this.TP = var1;
               if (this.Cz && this.uX.size() > 1 && this.f()) {
                  this.CF = var1 + 2 + this.TH.nextInt(3);
                  this.uc.clear();
                  this.uc.addAll(this.uX.keySet());
                  this.CB = var1 + 10;
               }
            }
         }

         for (Entry var4 : this.uX.entrySet()) {
            if (!((BlockPos)var4.getKey()).equals(this.C2) && mc.level.getBlockState((BlockPos)var4.getKey()).isAir()) {
               this.ue++;
               this.Ce.put((BlockPos)var4.getKey(), (BlockState)var4.getValue());
            }
         }

         this.uX.clear();
         this.C2 = null;
         this.TT = null;
      }
   }

   private BlockPos b(BlockPos var1) {
      if (this.TI != null && !var1.equals(this.TI)) {
         this.Cp.add(var1.immutable());
      }

      BlockState var2 = mc.level.getBlockState(var1);
      BlockState var3 = this.Ce.remove(var1);
      String var4 = this.C6.remove(var1);
      if (var3 != null && var3.getBlock() == var2.getBlock() && !(var2.getBlock() instanceof FallingBlock)) {
         _q.z(this.getName(), "Server put back {} ({}): {}", var1.toShortString(), var2.getBlock().getName().getString(), var4);
         this.P(var1);
      }

      if (!var1.equals(this.C2)) {
         this.I = mc.player.tickCount;
      }

      this.C2 = var1;
      this.TT = var2;
      this.TE = var1.immutable();
      return var1;
   }

   private void r(ol.k var1) {
      this.TB = mc.player.tickCount;
      this.rT.clear();
      this.ud.clear();
      ol.c var2 = this.Cx.T();
      int var3 = mc.player.tickCount;
      this.j.values().removeIf(var1x -> var1x <= var3);

      for (int var4 = var1.top(); var4 >= var1.bottom(); var4--) {
         for (int var5 = var2.minX(); var5 <= var2.maxX(); var5++) {
            for (int var6 = var2.minZ(); var6 <= var2.maxZ(); var6++) {
               BlockPos var7 = new BlockPos(var5, var4, var6);
               if (mc.level.hasChunkAt(var7)) {
                  if (!mc.level.getFluidState(var7).isEmpty()) {
                     this.ud.add(var7);
                  } else if (vs.W(var7)) {
                     this.rT.add(var7);
                  }
               }
            }
         }
      }

      int var8 = this.u4;
      this.rT.sort(Comparator.comparingLong(var2x -> {
         Integer var3x = this.uM == var8 ? this.Cd.get(ay.w(var2x.getX(), var2x.getY(), var2x.getZ())) : null;
         return var3x != null ? var3x.intValue() : 1125899906842624L + this.Cx.A(var2x.getX(), var2x.getY(), var2x.getZ(), var8);
      }));
   }

   private void P(BlockPos var1) {
      int var2 = this.U.merge(var1, 1, Integer::sum);
      if (var2 >= 3) {
         this.U.remove(var1);
         this.c.add(var1.immutable());
         this.j.put(var1, mc.player.tickCount + 2400);
         this.M(var1.toShortString() + ": the server keeps putting it back, skipping it for now.");
         _q.z(this.getName(), "Server restored {} {} times; skipping for {} ticks", var1.toShortString(), 3, 2400);
      }
   }

   private boolean R() {
      int var1 = mc.player.tickCount;
      if (var1 - this.T8 >= 600) {
         this.T8 = var1;
         int var2 = 0;

         for (int var6 : this.rx) {
            var2 += var6;
         }

         if (var2 > 0) {
            _q.z(
               this.getName(),
               "Breaks put back by eye distance: under 2 {}/{}, 2-3 {}/{}, 3-3.5 {}/{}, 3.5-4 {}/{}, 4+ {}/{}",
               this.uo[0],
               this.rx[0],
               this.uo[1],
               this.rx[1],
               this.uo[2],
               this.rx[2],
               this.uo[3],
               this.rx[3],
               this.uo[4],
               this.rx[4]
            );
         }
      }

      Vec3 var7 = mc.player.getEyePosition();
      this.rc
         .entrySet()
         .removeIf(
            var3 -> {
               int var4 = var1 - var3.getValue();
               if (var4 <= 200 && var4 >= 0) {
                  BlockState var5 = mc.level.getBlockState(var3.getKey());
                  if (var5.isAir()) {
                     return false;
                  }

                  Integer var6x = this.C1.remove(var3.getKey());
                  if (var6x != null) {
                     this.uo[var6x]++;
                  }

                  vs.j(this.getName(), var3.getKey());
                  _q.z(
                     this.getName(),
                     "{} came back as {} {} ticks after the break, {} blocks from the eye",
                     var3.getKey().toShortString(),
                     var5.getBlock().getName().getString(),
                     var4,
                     String.format(Locale.ROOT, "%.1f", Math.sqrt(var7.distanceToSqr(Vec3.atCenterOf((Vec3i)var3.getKey()))))
                  );
                  if (var4 <= 5 && !(var5.getBlock() instanceof FallingBlock)) {
                     this.uN.M(var3.getKey().asLong());
                  }

                  return true;
               } else {
                  this.C1.remove(var3.getKey());
                  return true;
               }
            }
         );
      if (!this.uN.V()) {
         return false;
      }

      _q.z(this.getName(), "Stopping: the server took back {} of the last {} breaks", this.uN.L(), 10);
      vs.B(this);
      this.p("the server put back " + this.uN.L() + " of your last 10 breaks, so it is refusing them and digging on only draws attention");
      return true;
   }

   private void e() {
      if (mc.player.tickCount - this.rB >= 600) {
         this.rB = mc.player.tickCount;
         this.U.clear();
      }
   }

   private boolean U(BlockPos var1) {
      Integer var2 = this.j.get(var1);
      return var2 != null && var2 > mc.player.tickCount || this.u7.contains(var1);
   }

   private void D(BlockPos var1, String var2) {
      int var3 = this.f.merge(var1, 1, Integer::sum);
      if (var3 >= 3) {
         this.u7.add(var1);
         if (this.q++ == 0) {
            this.CD = var1.toShortString();
            this.i = var2;
         }
      } else {
         this.j.put(var1, mc.player.tickCount + 400);
         _q.c(this.getName(), "Skipping {} for now: {}", var1.toShortString(), var2);
      }

      this.P("Set aside " + var1.toShortString() + ": " + var2);
      if (var1.equals(this.C2)) {
         this.C2 = null;
      }

      if (var1.equals(this.D)) {
         this.D = null;
      }
   }

   private boolean O(BlockPos var1) {
      for (Direction var5 : Direction.values()) {
         if (var5 != Direction.DOWN && !mc.level.getFluidState(var1.relative(var5)).isEmpty()) {
            return true;
         }
      }

      return false;
   }

   private boolean b(BlockPos var1) {
      if (this.Td) {
         return true;
      }

      int var2 = this.Cx.x(var1.getX(), var1.getZ(), this.u4);
      return !this.Cx.J(var2, this.u4) ? true : this.b.contains(this.Y(var1.getX(), var1.getZ()));
   }

   private BlockPos h(ol.k var1) {
      double var2 = this.n();
      Vec3 var4 = mc.player.getEyePosition();
      if (this.C2 == null
         || !this.n(this.C2, var1)
         || !(var4.distanceToSqr(Vec3.atCenterOf(this.C2)) <= var2 * var2)
         || (this.Cz ? !this.C(this.C2) : !this.i(this.C2, vs.W(this, this.C2, var2)))) {
         this.C2 = null;
         this.TT = null;
         this.Cz = (boolean)0;
         this.uX.clear();
         if (!this.Td && this.N() >= 0) {
            BlockPos var5 = this.c(var1, var2, var4);
            if (var5 != null) {
               this.Cz = (boolean)1;
               this.uX.clear();
               int var15 = this.Cx.x(var5.getX(), var5.getZ(), this.u4);
               boolean var17 = this.Cx.w();

               for (int var18 = -1; var18 <= 1; var18++) {
                  for (int var19 = -1; var19 <= 1; var19++) {
                     BlockPos var21 = var17 ? var5.offset(0, var18, var19) : var5.offset(var19, var18, 0);
                     if (vs.W(var21)) {
                        this.uX.put(var21, mc.level.getBlockState(var21));
                     }
                  }
               }

               _q.c(this.getName(), "Drill face at {} in row {} ({} blocks)", var5.toShortString(), var15 + 1, this.uX.size());
               return this.b(var5);
            }
         }

         ArrayList var13 = new ArrayList();

         for (BlockPos var7 : this.rT) {
            if (this.n(var7, var1) && this.b(var7) && !(var4.distanceToSqr(Vec3.atCenterOf(var7)) > var2 * var2)) {
               var13.add(var7);
            }
         }

         float var14 = mc.player.getYRot();
         float var16 = mc.player.getXRot();
         BlockPos var8 = this.TE;
         var13.sort(Comparator.comparingDouble(var6 -> b(var4, var6, var14, var16, var2, var8)));
         int var9 = 0;

         for (BlockPos var11 : var13) {
            if (var9++ >= 16) {
               break;
            }

            if (this.i(var11, vs.W(this, var11, var2))) {
               return this.b(var11);
            }
         }

         for (BlockPos var22 : var13.stream()
            .sorted(Comparator.comparingLong(var1x -> this.Cx.A(var1x.getX(), var1x.getY(), var1x.getZ(), this.u4)))
            .limit(4L)
            .toList()) {
            BlockPos var12 = this.A(var4, var22, var2);
            if (var12 != null && this.n(var12, var1) && this.R(var12) && this.i(var12, vs.W(this, var12, var2))) {
               _q.c(this.getName(), "Digging through {} toward {}", var12.toShortString(), var22.toShortString());
               return this.b(var12);
            }
         }

         return null;
      } else {
         return this.C2;
      }
   }

   static double b(Vec3 var0, BlockPos var1, float var2, float var3, double var4, BlockPos var6) {
      double var7 = Math.sqrt(var0.distanceToSqr(Vec3.atCenterOf(var1)));
      double var9 = A(var0, var1, var2, var3) + 10.0 * var7;
      if (var7 > var4 - 0.6) {
         var9 += 40.0;
      }

      if (var6 != null && var1.distManhattan(var6) == 1) {
         var9 -= 15.0;
      }

      return var9;
   }

   static double A(Vec3 var0, BlockPos var1, float var2, float var3) {
      float[] var4 = b6.R(var0, Vec3.atCenterOf(var1));
      return Math.hypot(Mth.wrapDegrees(var4[0] - var2), var4[1] - var3);
   }

   private BlockPos A(Vec3 var1, BlockPos var2, double var3) {
      Vec3 var5 = Vec3.atCenterOf(var2);
      if (var1.distanceToSqr(var5) > var3 * var3) {
         return null;
      }

      BlockHitResult var6 = mc.level.clip(new ClipContext(var1, var5, Block.OUTLINE, Fluid.NONE, mc.player));
      return var6.getType() == Type.BLOCK && !var6.getBlockPos().equals(var2) ? var6.getBlockPos() : null;
   }

   private boolean n(BlockPos var1, ol.k var2) {
      return var2.contains(var1.getY()) && this.Y(var1) && vs.W(var1) && !this.U(var1) && !this.O(var1) && !this.p(var1);
   }

   private boolean p(BlockPos var1) {
      AABB var2 = mc.player.getBoundingBox();
      return var1.getY() == this.L() - 1 && var2.maxX > var1.getX() && var2.minX < var1.getX() + 1 && var2.maxZ > var1.getZ() && var2.minZ < var1.getZ() + 1;
   }

   private int N() {
      String var1 = this.CI.getValue().trim().toLowerCase(Locale.ROOT);
      if (!var1.isEmpty() && this.isInGame()) {
         Inventory var2 = mc.player.getInventory();

         for (int var3 = 0; var3 < 9; var3++) {
            ItemStack var4 = var2.getItem(var3);
            if (!var4.isEmpty() && var4.getHoverName().getString().toLowerCase(Locale.ROOT).contains(var1)) {
               return var3;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private long Y(int var1, int var2) {
      int var3 = this.Cx.x(var1, var2, this.u4);
      int var4 = this.Cx.w() ? var1 : var2;
      return (long)var3 << 32 ^ var4 & 4294967295L;
   }

   private BlockPos E(int var1, int var2, ol.k var3) {
      int var4 = this.Cx.d(var1, this.u4);
      return this.Cx.w() ? new BlockPos(var2, var3.bottom() + 1, var4) : new BlockPos(var4, var3.bottom() + 1, var2);
   }

   private Direction L(int var1) {
      int var2 = this.Cx.w(var1, this.u4);
      return this.Cx.w() ? (var2 > 0 ? Direction.WEST : Direction.EAST) : (var2 > 0 ? Direction.NORTH : Direction.SOUTH);
   }

   private boolean C(BlockPos var1) {
      if (!var1.equals(this.T1)) {
         return this.Z(var1);
      }

      BlockHitResult var2 = vs.W(this, var1, this.n());
      return var2 != null && var2.getDirection() == this.r4;
   }

   private boolean Z(BlockPos var1) {
      BlockHitResult var2 = vs.W(this, var1, this.n());
      int var3 = this.Cx.x(var1.getX(), var1.getZ(), this.u4);
      return var2 != null && var2.getDirection() == this.L(var3);
   }

   private BlockPos c(ol.k var1, double var2, Vec3 var4) {
      HashSet var5 = new HashSet();

      for (BlockPos var7 : this.rT) {
         if (this.n(var7, var1)) {
            int var8 = this.Cx.x(var7.getX(), var7.getZ(), this.u4);
            if (this.Cx.J(var8, this.u4) && !this.b.contains(this.Y(var7.getX(), var7.getZ()))) {
               long var9 = this.Y(var7.getX(), var7.getZ());
               if (var5.add(var9)) {
                  BlockPos var11 = this.E(var8, this.Cx.w() ? var7.getX() : var7.getZ(), var1);
                  if (this.u7.contains(var11) || !this.U(var11) || !vs.W(var11)) {
                     if (this.n(var11, var1)) {
                        if (!(var4.distanceToSqr(Vec3.atCenterOf(var11)) > var2 * var2) && this.Z(var11)) {
                           return var11;
                        }

                        return null;
                     }

                     this.b.add(var9);
                  }
               }
            }
         }
      }

      return null;
   }

   private double n() {
      return Math.min(this.TJ.getValue(), mc.player.blockInteractionRange());
   }

   private void M(BlockPos var1, boolean var2) {
      this.v(var1, var2, null);
   }

   private void v(BlockPos var1, boolean var2, String var3) {
      int var4 = this.N();
      BlockPos var5 = var1.immutable();
      if (!this.R(var5)) {
         vs.B(this);
         this.CE = null;
         this.D(var5, "out of reach from here");
      } else {
         if (var5.equals(this.CE)) {
            if (var5.equals(this.T1) && this.G >= 60 && !this.z(var5)) {
               _q.z(this.getName(), "Drill hit on {} dropped: the crosshair stayed off its {} face for {} ticks", var5.toShortString(), this.r4, 60);
               this.uY.add(var5);
               this.T1 = null;
               this.Cz = (boolean)0;
               this.uX.clear();
               this.v(var5, false, var3);
               return;
            }

            if (++this.G > 200) {
               BlockHitResult var6 = vs.r(var5, this.n());
               _q.z(
                  this.getName(),
                  "Could not break {} ({}) in {} ticks; crosshair {}, distance {}, phase {}",
                  var5.toShortString(),
                  BuiltInRegistries.BLOCK.getKey(mc.level.getBlockState(var5).getBlock()).getPath(),
                  200,
                  var6 == null ? "off the block" : "on " + var6.getDirection(),
                  String.format(Locale.ROOT, "%.2f", Math.sqrt(mc.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var5)))),
                  this.o
               );
               this.CE = null;
               this.G = 0;
               vs.B(this);
               this.ri = null;
               this.D(var5, "could not break it");
               return;
            }
         } else {
            this.CE = var5;
            this.G = 0;
         }

         if (var4 < 0 || !mc.player.isShiftKeyDown() && !mc.player.isCrouching() || !var2 && this.B(mc.level.getBlockState(var5), var4) != var4) {
            this.m(var5, var2 || var4 >= 0 && this.B(mc.level.getBlockState(var5), var4) == var4);
            boolean var7 = vs.i(
               this,
               var5,
               this.n(),
               true,
               12.0F,
               var4x -> {
                  int var5x = var2 && var4 >= 0 ? var4 : this.B(var4x, var4);
                  this.Cs = var5x;
                  if (var5x >= 0 && this.k(var5x) && this.uj.add(mc.player.getInventory().getItem(var5x).getItem())) {
                     _q.z(
                        this.getName(),
                        "Auto Tool: {} for {}",
                        mc.player.getInventory().getItem(var5x).getHoverName().getString(),
                        BuiltInRegistries.BLOCK.getKey(var4x.getBlock()).getPath()
                     );
                  }

                  if (var5x == var4 && this.r.w("Auto") && this.uP == null && this.TI == null) {
                     this.Y(var5);
                  }

                  return var5x;
               },
               () -> !this.F() && !this.O(var5) && this.r(var5) && this.z(var5)
            );
            this.P(!var7 ? "Waiting for a reachable face" : (var3 != null ? var3 : (var2 ? "Cutting a 3x3 face at " : "Mining ") + var5.toShortString()));
            if (!var7) {
               this.D(var5, "no reachable face");
            }
         } else {
            this.ux = 0;
            vs.B(this);
            this.P("Standing up before the drill hit");
         }
      }
   }

   private void m(BlockPos var1, boolean var2) {
      if (var2 || !this.u9.getValue()) {
         this.ux = 0;
      } else if (this.TD) {
         this.Tq = (boolean)1;
      } else {
         int var3 = mc.player.tickCount;
         if (!var1.equals(this.ui)) {
            this.ui = var1;
            if (var3 >= this.ux && this.TH.nextDouble() < 0.015) {
               this.ux = var3 + this.TH.nextInt(20, 51);
            }
         }

         if (var3 < this.ux) {
            this.Tq = (boolean)1;
         }
      }
   }

   private boolean r(BlockPos var1) {
      int var2 = mc.player.tickCount;
      if (!var1.equals(this.rK)) {
         this.rK = var1.immutable();
         this.Tf = -1000000000;
         this.ug = m(this.TH.nextDouble());
      }

      if (this.Tf == -1000000000) {
         if (vs.r(var1, this.n()) == null) {
            return false;
         }

         this.Tf = var2;
      }

      return var2 - this.Tf >= this.ug;
   }

   static int m(double var0) {
      return (int)Math.min(6.0, Math.floor(-Math.log(1.0 - Math.min(var0, 0.999999)) * 1.3));
   }

   private boolean z(BlockPos var1) {
      if (this.T1 != null && var1.equals(this.T1)) {
         BlockHitResult var2 = vs.r(var1, this.n());
         return var2 != null && var2.getDirection() == this.r4;
      } else {
         return true;
      }
   }

   private int H() {
      BlockState var1 = Blocks.STONE.defaultBlockState();
      int var2 = this.c(var1, this.N());
      return var2 >= 0 && mc.player.getInventory().getItem(var2).isCorrectToolForDrops(var1) ? var2 : -1;
   }

   private int B(BlockState var1, int var2) {
      int var3 = this.H();
      Inventory var4 = mc.player.getInventory();
      int var5 = var3 >= 0 ? var3 : (var2 >= 0 ? var2 : var4.getSelectedSlot());
      if (!this.Cy.getValue()) {
         return var5;
      }

      ArrayList var6 = new ArrayList(9);

      for (int var7 = 0; var7 < 9; var7++) {
         var6.add(var4.getItem(var7));
      }

      int var9 = O(var6, var1, var2, var5, var3 < 0, this.c());
      int var8 = var4.getSelectedSlot();
      return var8 != var9 && var8 >= 0 && var8 < 9 && this.R((ItemStack)var6.get(var8), (ItemStack)var6.get(var9), var1, var8 == var2 && var3 >= 0)
         ? var8
         : var9;
   }

   private boolean R(ItemStack var1, ItemStack var2, BlockState var3, boolean var4) {
      if (!var1.isEmpty() && var1.isDamageableItem() && !var4 && !var1.is(ItemTags.SWORDS) && bt.J(var1) > this.c()) {
         s5.k var5 = s5.d(var1, var3, true);
         s5.k var6 = s5.d(var2, var3, true);
         return var5.usable() && var5.harvest() == var6.harvest() && var5.speed() >= var6.speed();
      } else {
         return false;
      }
   }

   static int O(List<ItemStack> var0, BlockState var1, int var2, int var3, boolean var4, int var5) {
      ArrayList var6 = new ArrayList(var0.size());

      for (int var7 = 0; var7 < var0.size(); var7++) {
         ItemStack var8 = (ItemStack)var0.get(var7);
         s5.k var9 = s5.d(var8, var1, true);
         int var10 = var7 != var3 && (var8.isEmpty() || var8.is(ItemTags.SWORDS) || var7 == var2 && !var4 || bt.J(var8) <= var5) ? 0 : 1;
         var6.add(var10 != 0 ? var9 : new s5.k(var9.speed(), var9.harvest(), false));
      }

      return s5.M(var6, var3);
   }

   private int c() {
      return this.TR.getValue() ? this.Te.getValue().intValue() : 1;
   }

   private boolean k(int var1) {
      return var1 != this.N() && var1 != this.H();
   }

   private boolean j() {
      return this.N() >= 0 && this.H() < 0;
   }

   private boolean f() {
      if (this.r.w("3x3")) {
         return true;
      } else {
         return this.r.w("One Block") ? false : !Boolean.FALSE.equals(this.uP);
      }
   }

   private void P() {
      if (!this.uR && this.r.w("Auto") && this.uP == null) {
         this.uR = (boolean)1;
         this.M(
            "blocks are left because a 3x3 drill cut would reach outside the area, and no drill hit has shown yet how your drill cuts. If it is a normal pickaxe, set Drill Cut to One Block, or add a plain pickaxe to the hotbar."
         );
         _q.z(this.getName(), "Drill Cut hint sent: drill-only, cut unknown, blocks left for cutting outside the area");
      }
   }

   private void Y(BlockPos var1) {
      BlockHitResult var2 = vs.r(var1, this.n());
      Direction var3 = var2 != null ? var2.getDirection() : Direction.UP;
      this.TI = var1;
      this.Z.clear();
      Axis var4 = var3.getAxis();

      for (int var5 = -1; var5 <= 1; var5++) {
         for (int var6 = -1; var6 <= 1; var6++) {
            if (var5 != 0 || var6 != 0) {
               BlockPos var7 = var4 == Axis.X ? var1.offset(0, var5, var6) : (var4 == Axis.Y ? var1.offset(var5, 0, var6) : var1.offset(var5, var6, 0));
               if (vs.W(var7)) {
                  this.Z.put(var7.immutable(), mc.level.getBlockState(var7));
               }
            }
         }
      }

      this.um = 0;
      this.Cp.clear();
      _q.c(this.getName(), "Watching the first drill hit at {} on {} ({} blocks around it)", var1.toShortString(), var3, this.Z.size());
   }

   private void R() {
      if (this.TI != null) {
         if (this.Z.isEmpty()) {
            this.TI = null;
         } else {
            if (this.um == 0) {
               if (vs.W(this.TI)) {
                  return;
               }

               this.um = mc.player.tickCount + 10;
            }

            int var1 = 0;
            int var2 = 0;

            for (BlockPos var4 : this.Z.keySet()) {
               if (!this.Cp.contains(var4)) {
                  var2++;
                  if (mc.level.getBlockState(var4).isAir()) {
                     var1++;
                  }
               }
            }

            int var5 = var2 > 0 && var1 * 2 >= var2 ? 1 : 0;
            if (var5 != 0 || var2 == 0 || mc.player.tickCount >= this.um) {
               if (var5 != 0) {
                  this.uP = true;
                  this.ua = 0;
                  _q.z(this.getName(), "Drill check: {} of {} blocks around the hit broke, so the drill cuts 3x3", var1, var2);
                  this.M("your drill cuts 3x3 here, so it mines full 3x3 faces.");
               } else if (var2 > 0 && ++this.ua >= 3) {
                  this.uP = false;
                  _q.z(
                     this.getName(),
                     "Drill check: {} hits in a row broke one block ({} of {} around the last), so the drill breaks one block at a time",
                     3,
                     var1,
                     var2
                  );
                  this.M("your drill broke one block on 3 hits in a row here, so it is used like a normal pickaxe. Set Drill Cut to 3x3 if it really cuts 3x3.");
                  this.Td = (boolean)1;
                  this.u7.removeAll(this.rw);
                  this.f.keySet().removeAll(this.rw);
                  this.rw.clear();
                  this.j.clear();
               } else {
                  _q.z(this.getName(), "Drill check: {} of {} blocks around the hit broke; checking the next drill hit too", var1, var2);
               }

               this.TI = null;
               this.Z.clear();
               this.Cp.clear();
            }
         }
      }
   }

   private boolean p(BlockPos var1, Direction var2) {
      Axis var3 = var2.getAxis();

      for (int var4 = -1; var4 <= 1; var4++) {
         for (int var5 = -1; var5 <= 1; var5++) {
            BlockPos var6 = var3 == Axis.X ? var1.offset(0, var4, var5) : (var3 == Axis.Y ? var1.offset(var4, 0, var5) : var1.offset(var4, var5, 0));
            if (!this.Y(var6) && vs.W(var6)) {
               return false;
            }
         }
      }

      return true;
   }

   private boolean Y(BlockPos var1) {
      return this.Cx.T().contains(var1.getX(), var1.getY(), var1.getZ()) && (this.C9 == null || this.C9.contains(var1));
   }

   private boolean i(BlockPos var1, BlockHitResult var2) {
      if (var2 == null) {
         return false;
      }

      if (this.j() && this.f() && !this.p(var1, var2.getDirection())) {
         if (this.B(mc.level.getBlockState(var1), this.N()) != this.N()) {
            return true;
         }

         this.rw.add(var1.immutable());
         return false;
      } else {
         return true;
      }
   }

   private int c(BlockState var1, int var2) {
      Inventory var3 = mc.player.getInventory();
      int var4 = -1;
      float var5 = -1.0F;

      for (int var6 = 0; var6 < 9; var6++) {
         if (var6 != var2) {
            ItemStack var7 = var3.getItem(var6);
            float var8 = var7.getDestroySpeed(var1) + (var7.isCorrectToolForDrops(var1) ? 1000 : 0);
            if (var8 > var5) {
               var5 = var8;
               var4 = var6;
            }
         }
      }

      return var4;
   }

   private boolean z(ol.k var1) {
      if (this.ud.isEmpty()) {
         return false;
      }

      double var2 = this.n();
      Vec3 var4 = mc.player.getEyePosition();
      int var5 = this.i();

      for (BlockPos var7 : this.ud) {
         if (!mc.level.getFluidState(var7).isEmpty() && this.TL.getOrDefault(var7, 0) < 60 && !(var4.distanceToSqr(Vec3.atCenterOf(var7)) > var2 * var2)) {
            if (var5 < 0) {
               this.h("no solid block in your hotbar to plug lava or water with");
               return false;
            }

            Item var8 = mc.player.getInventory().getItem(var5).getItem();
            BlockPos var9 = var7.immutable();
            if (vi.B(this, var9, var8, var2, true, true) != null) {
               if (this.K("plug " + (mc.level.getFluidState(var9).is(FluidTags.LAVA) ? "lava" : "water"))) {
                  return true;
               }

               if (!this.i(var5)) {
                  this.P("Taking out " + mc.player.getInventory().getItem(var5).getHoverName().getString() + " to plug a fluid");
                  return true;
               }

               if (mc.player.tickCount < this.uH) {
                  vs.B(this);
                  this.P("Steadying before the next block");
                  return true;
               }

               boolean var10 = vi.M(
                  this, var9, var8, var2, true, true, () -> !mc.level.getFluidState(var9).isEmpty() && !this.F(), () -> this.p(var9, var8, true), var2x -> {
                     this.e(var9);
                     _q.c(this.getName(), "Plug at {} {}", var9.toShortString(), var2x ? "placed" : "rejected");
                  }
               );
               if (var10) {
                  if (this.TL.merge(var9, 1, Integer::sum) == 1) {
                     _q.z(this.getName(), "Plugging {} at {}", mc.level.getFluidState(var9).is(FluidTags.LAVA) ? "lava" : "water", var9.toShortString());
                  }

                  vs.B(this);
                  this.P("Plugging " + (mc.level.getFluidState(var9).is(FluidTags.LAVA) ? "lava" : "water") + " at " + var9.toShortString());
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean i(int var1) {
      Inventory var2 = mc.player.getInventory();
      if (var2.getSelectedSlot() != var1) {
         vs.B(this);
         var2.setSelectedSlot(var1);
         this.rG = var1;
         this.k = mc.player.tickCount;
         return false;
      }

      if (this.rG != var1) {
         this.rG = var1;
         this.k = mc.player.tickCount;
      }

      return mc.player.tickCount - this.k >= 3;
   }

   private float O() {
      q71cffb54eb30bf94 var1 = (q71cffb54eb30bf94)mc.player;
      return (float)Math.hypot(Mth.wrapDegrees(mc.player.getYRot() - var1.larpYRotLast()), mc.player.getXRot() - var1.larpXRotLast());
   }

   private boolean p(BlockPos var1, Item var2, boolean var3) {
      int var4 = mc.player.tickCount;
      if (!var1.equals(this.Tp)) {
         this.Tp = var1.immutable();
         this.TY = 0;
         this.TF = q(this.TH.nextDouble());
         this.uD = -1000000000;
      }

      if (this.uD != var4) {
         this.uD = var4;
         int var5 = var3 && !(mc.player.getDeltaMovement().horizontalDistanceSqr() < 4.0E-4) ? 0 : 1;
         int var6 = var5 != 0 && this.O() <= 1.5F && vi.B(this, var1, var2, this.n(), false, true) != null ? 1 : 0;
         this.TY = var6 != 0 ? this.TY + 1 : 0;
      }

      return this.TY >= this.TF && var4 >= this.uH;
   }

   static int q(double var0) {
      return 2 + (int)Math.round(4.0 * var0 * var0);
   }

   static int K(double var0) {
      return 8 + (int)Math.round(8.0 * var0 * var0);
   }

   private void e(BlockPos var1) {
      int var2 = mc.player.tickCount;
      this.uH = var2 + K(this.TH.nextDouble());
      this.C3 = var1.immutable();
      this.v = var2 + 5;
      this.rd.merge(var1.immutable(), 1, Integer::sum);
      this.Tp = null;
   }

   private boolean K(String var1) {
      Screen var2 = mc.gui.screen();
      if (var2 != null && (!this.allowsScreenInteractions() || var2 instanceof AbstractContainerScreen)) {
         this.P("Waiting for the menu to close to " + var1);
         return true;
      } else {
         return false;
      }
   }

   private int i() {
      Inventory var1 = mc.player.getInventory();
      int var2 = this.N();

      for (int var3 = 0; var3 < 9; var3++) {
         if (var3 != var2) {
            ItemStack var4 = var1.getItem(var3);
            if (var4.getItem() instanceof BlockItem var5 && w(var5)) {
               return var3;
            }
         }
      }

      return -1;
   }

   static boolean w(BlockItem var0) {
      net.minecraft.world.level.block.Block var1 = var0.getBlock();
      BlockState var2 = var1.defaultBlockState();
      return !(var1 instanceof FallingBlock)
         && !var2.hasBlockEntity()
         && !var2.ignitedByLava()
         && var2.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)
         && var1 != Blocks.ICE
         && var1 != Blocks.TNT
         && var1 != Blocks.MAGMA_BLOCK;
   }

   private void h(String var1) {
      if (!var1.equals(this.TQ)) {
         this.TQ = var1;
         this.M(var1 + ".");
      }
   }

   private BlockPos U(ol.k var1) {
      if (this.ue != this.l) {
         this.rh.clear();
         this.l = this.ue;
      }

      BlockPos var2 = null;
      BlockPos var3 = null;
      double var4 = 36.0;
      double var6 = mc.player.getX();
      double var8 = mc.player.getZ();
      int var10 = !this.Td && this.N() >= 0 ? 1 : 0;

      for (BlockPos var12 : this.rT) {
         double var13 = var12.getX() + 0.5 - var6;
         double var15 = var12.getZ() + 0.5 - var8;
         double var17 = var13 * var13 + var15 * var15;
         if ((var2 == null || !(var17 >= var4)) && this.n(var12, var1)) {
            boolean var19 = this.b(var12);
            if (var19 || var10 != 0) {
               if (var2 == null) {
                  var2 = var12;
               }

               if (var19 && var17 < var4 && !this.rh.contains(var12) && this.e(var12)) {
                  var4 = var17;
                  var3 = var12;
               }
            }
         }
      }

      this.r_ = var3 != null && !var3.equals(var2) ? var3 : null;
      return var3 != null ? var3 : var2;
   }

   private boolean e(BlockPos var1) {
      for (Direction var5 : Direction.values()) {
         BlockPos var6 = var1.relative(var5);
         if (mc.level.getBlockState(var6).getCollisionShape(mc.level, var6).isEmpty()) {
            return true;
         }
      }

      return false;
   }

   private void H(BlockPos var1, ol.k var2) {
      if (!this.S(var1, var2)) {
         this.D(var1, "no way to walk within reach");
         this.z(var1, var2, "no way to walk within reach");
         this.t();
      } else {
         int var3 = !this.Td && this.N() >= 0 && !this.b(var1) ? 1 : 0;
         BlockPos var4 = var3 != 0 ? this.E(this.Cx.x(var1.getX(), var1.getZ(), this.u4), this.Cx.w() ? var1.getX() : var1.getZ(), var2) : var1;
         if (!var4.equals(this.D) || mc.player.tickCount - this.Tg > 40 || this.x.isEmpty() && this.T5 == 0) {
            this.D = var4;
            this.Tg = mc.player.tickCount;
            List var5 = this.C(var2.bottom(), var3 != 0 ? this.x(var4) : this.A(var4, var2));
            if ((var5 == null || var5.isEmpty()) && var1.equals(this.r_)) {
               this.rh.add(var1.immutable());
               this.r_ = null;
               this.t();
               return;
            }

            if (var5 == null || var5.isEmpty()) {
               if (var3 != 0) {
                  this.b.add(this.Y(var1.getX(), var1.getZ()));
                  _q.c(
                     this.getName(),
                     "Opening slice at {} with the pickaxe: {}",
                     var4.toShortString(),
                     var5 == null ? "no route to the row line" : "not in line"
                  );
               } else {
                  if (var5 != null
                     && this.A(mc.player.getEyePosition(), var1, this.n()) instanceof BlockPos var6
                     && this.n(var6, var2)
                     && this.i(var6, vs.W(this, var6, this.n()))) {
                     this.t();
                     this.b(var6);
                     this.Cz = (boolean)0;
                     this.M(var6, false);
                     return;
                  }

                  if (var5 != null && this.rw.contains(var1)) {
                     this.P();
                  }

                  String var8 = var5 == null
                     ? "no way to walk within reach"
                     : (this.rw.contains(var1) ? "the drill would cut outside the area" : "not visible from any standing spot nearby");
                  this.D(var1, var8);
                  if (this.A() >= 2) {
                     this.z(var1, var2, var8);
                  }

                  this.uL++;
               }

               this.t();
               return;
            }

            this.x = var5;
            this.T5 = 0;
            this.ux = 0;
            this.Z(var2, var4);
            _q.c(this.getName(), "Route of {} steps toward {}", this.x.size(), var4.toShortString());
         }

         this.n(var2);
      }
   }

   private void Z(ol.k var1, BlockPos var2) {
      oy.i var3 = this.x.getLast();
      oy.i var4 = this.CY.isEmpty() ? null : this.CY.getLast();
      int var5 = var2.equals(this.T6) && var3.equals(var4) ? 1 : 0;
      this.CY = this.x;
      this.T6 = var2;
      if (var5 == 0) {
         boolean var6 = ad.m(var3.x(), var1.bottom(), var3.z());
         this.B = var6 ? 0.0 : (this.TH.nextDouble() * 2.0 - 1.0) * 0.18;
         this.u2 = var6 ? 0.0 : (this.TH.nextDouble() * 2.0 - 1.0) * 0.18;
         this.Tw = var6 ? 0.245 : 0.15 + this.TH.nextDouble() * 0.15;
         this.CQ = 0.35 + this.TH.nextDouble() * 0.65;
         this.rQ = this.TH.nextDouble() * Math.PI * 2.0;
      }
   }

   private Vec3 T(int var1, double var2) {
      oy.i var4 = this.x.get(var1);
      int var5 = this.x == this.CY && var1 == this.x.size() - 1 ? 1 : 0;
      return new Vec3(var4.x() + 0.5 + (var5 != 0 ? this.B : 0.0), var2, var4.z() + 0.5 + (var5 != 0 ? this.u2 : 0.0));
   }

   static boolean T(Vec3 var0, Vec3 var1) {
      double var2 = Math.hypot(var0.x, var0.z);
      double var4 = Math.hypot(var1.x, var1.z);
      return !(var2 < 1.0E-6) && !(var4 < 1.0E-6) ? (var0.x * var1.x + var0.z * var1.z) / (var2 * var4) >= u3 : true;
   }

   public static void n(LocalPlayer var0) {
      if (Tt != null && Tt.isEnabled()) {
         _6 var1 = Tt;
         if (var1 != null && var1.Cx != null && var0 == var1.Cb) {
            var1.Cv = var0.position();
            var1.g = var0.getDeltaMovement().horizontalDistance();
         }
      }
   }

   public static void g(LocalPlayer var0) {
      if (Tt != null && Tt.isEnabled()) {
         _6 var1 = Tt;
         if (var1 != null && var1.Cx != null && var0 == var1.Cb && var1.isRuntimeActive()) {
            Vec3 var2 = var1.Cv;
            var1.Cv = null;
            double var3 = var2 == null ? -1.0 : var2.distanceTo(var0.position());
            if (!(var3 >= 0.0) || !(var3 < 0.001)) {
               int var5 = ++var1.C_;
               if (var5 <= 30 || var5 % 10 == 0) {
                  _q.z(
                     var1.getName(),
                     "Server {} you {} blocks (correction {} this run) while: {}; walking {}, sprint {}, sneak {}, on ground {}, speed {}",
                     var3 > 16.0 ? "teleported" : "moved",
                     var3 < 0.0 ? "?" : String.format(Locale.ROOT, "%.2f", var3),
                     var5,
                     var1.getStatus(),
                     var1.rC.lengthSqr() > 1.0E-6,
                     var0.isSprinting(),
                     var0.isShiftKeyDown(),
                     var0.onGround(),
                     String.format(Locale.ROOT, "%.3f", var1.g)
                  );
               }
            }
         }
      }
   }

   private oy.g A(BlockPos var1, ol.k var2) {
      double var3 = this.n() - 0.4;
      Vec3 var5 = Vec3.atCenterOf(var1);
      double var6 = var2.bottom() + mc.player.getEyeHeight();
      return (var6x, var7) -> {
         double var8 = var6x + 0.5 - var5.x;
         double var10 = var7 + 0.5 - var5.z;
         double var12 = var6 - var5.y;
         return var8 * var8 + var12 * var12 + var10 * var10 <= var3 * var3
            && (var6x != var1.getX() || var7 != var1.getZ())
            && vs.C(new Vec3(var6x + 0.5, var6, var7 + 0.5), var1, var3);
      };
   }

   private oy.g x(BlockPos var1) {
      int var2 = this.Cx.x(var1.getX(), var1.getZ(), this.u4);
      int var3 = this.Cx.w(var2, this.u4);
      int var4 = this.Cx.d(var2, this.u4);
      int var5 = this.Cx.w() ? var1.getX() : var1.getZ();
      int var6 = Math.max(1, (int)Math.floor(this.n() - 0.5));
      Direction var7 = this.L(var2);
      double var8 = var1.getY() - 1 + mc.player.getEyeHeight();
      double var10 = this.n() - 0.4;
      Vec3 var12 = Vec3.atCenterOf(var1);
      return (var12x, var13) -> {
         int var14 = this.Cx.w() ? var13 : var12x;
         int var15 = this.Cx.w() ? var12x : var13;
         int var16 = (var5 - var15) * var3;
         if (var16 < 1 || var16 > var6) {
            return false;
         }

         if (var14 == var4) {
            return true;
         }

         if (Math.abs(var14 - var4) == 1 && var16 >= 2) {
            Vec3 var17 = new Vec3(var12x + 0.5, var8, var13 + 0.5);
            if (var17.distanceToSqr(var12) > var10 * var10) {
               return false;
            }

            BlockHitResult var18 = mc.level.clip(new ClipContext(var17, var12, Block.OUTLINE, Fluid.NONE, mc.player));
            return var18.getType() == Type.BLOCK && var18.getBlockPos().equals(var1) && var18.getDirection() == var7;
         } else {
            return false;
         }
      };
   }

   private boolean O() {
      ol.c var1 = this.Cx.T();
      BlockPos var2 = this.i();
      byte var3 = 3;
      int var4 = var2.getX() >> 4;
      int var5 = var2.getZ() >> 4;
      int var6 = Math.max(var1.minX() - 2 >> 4, var4 - var3);
      int var7 = Math.min(var1.maxX() + 2 >> 4, var4 + var3);
      int var8 = Math.max(var1.minZ() - 2 >> 4, var5 - var3);
      int var9 = Math.min(var1.maxZ() + 2 >> 4, var5 + var3);

      for (int var10 = var6; var10 <= var7; var10++) {
         for (int var11 = var8; var11 <= var9; var11++) {
            if (!mc.level.getChunkSource().hasChunk(var10, var11)) {
               return false;
            }
         }
      }

      return true;
   }

   private List<oy.i> C(int var1, oy.g var2) {
      ol.c var3 = this.Cx.T();
      BlockPos var4 = this.i();
      oy.y var5 = new oy.y(
         Math.min(var3.minX(), var4.getX()) - 2,
         Math.min(var3.minZ(), var4.getZ()) - 2,
         Math.max(var3.maxX(), var4.getX()) + 2,
         Math.max(var3.maxZ(), var4.getZ()) + 2
      );
      this.Tr = (boolean)0;
      List var6 = oy.p(
         (var3x, var4x) -> this.H(var3x, var1, var4x) || var3x == var4.getX() && var4x == var4.getZ(), var4.getX(), var4.getZ(), var2, var5, 20000
      );
      if (var6 == null && this.uf.getValue()) {
         this.Tr = (boolean)1;

         try {
            return oy.p(
               (var3x, var4x) -> this.H(var3x, var1, var4x) || var3x == var4.getX() && var4x == var4.getZ(), var4.getX(), var4.getZ(), var2, var5, 20000
            );
         } finally {
            this.Tr = (boolean)0;
         }
      } else {
         return var6;
      }
   }

   private boolean P(BlockPos var1) {
      BlockState var2 = mc.level.getBlockState(var1);
      return var2.getCollisionShape(mc.level, var1).isEmpty()
         && mc.level.getFluidState(var1).isEmpty()
         && !var2.is(Blocks.COBWEB)
         && !var2.is(Blocks.FIRE)
         && !var2.is(Blocks.SOUL_FIRE)
         && !var2.is(Blocks.SWEET_BERRY_BUSH)
         && !var2.is(Blocks.POWDER_SNOW);
   }

   private boolean S(BlockPos var1) {
      BlockState var2 = mc.level.getBlockState(var1);
      return (var2.isFaceSturdy(mc.level, var1, Direction.UP) || this.n(var2, var1)) && !var2.is(Blocks.MAGMA_BLOCK) && mc.level.getFluidState(var1).isEmpty();
   }

   private boolean n(BlockState var1, BlockPos var2) {
      VoxelShape var3 = var1.getCollisionShape(mc.level, var2);
      return N(var3);
   }

   static boolean N(VoxelShape var0) {
      if (var0.isEmpty()) {
         return false;
      }

      AABB var1 = var0.bounds();
      return var1.maxY >= 0.75 && var1.maxY <= 1.0 && var1.minX <= 0.0 && var1.maxX >= 1.0 && var1.minZ <= 0.0 && var1.maxZ >= 1.0;
   }

   static int x(double var0) {
      return Mth.floor(var0 + 0.25);
   }

   private int L() {
      return x(mc.player.getY());
   }

   private BlockPos i() {
      return new BlockPos(mc.player.getBlockX(), this.L(), mc.player.getBlockZ());
   }

   private boolean E(BlockPos var1) {
      if (this.C9 == null && this.Tr && !this.Tz.contains(var1) && this.uf.getValue() && vi.i(var1) && mc.level.getFluidState(var1).isEmpty()) {
         for (Direction var5 : Direction.values()) {
            if (var5 != Direction.UP && vi.k(var1.relative(var5))) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   boolean H(int var1, int var2, int var3) {
      BlockPos var4 = new BlockPos(var1, var2, var3);
      if (!mc.level.hasChunkAt(var4)) {
         return false;
      }

      BlockPos var5 = var4.below();
      return this.P(var4) && this.P(var4.above()) && (this.S(var5) || this.E(var5) && this.i() >= 0);
   }

   private void n(ol.k var1) {
      if (this.T5 >= this.x.size()) {
         this.t();
      } else {
         Vec3 var2 = mc.player.position();
         int var3 = this.f(var2, var1.bottom());

         while (this.T5 < var3 && this.t(var2, this.T5, var3)) {
            this.T5++;
         }

         int var4 = var3 > this.T5 ? 1 : 0;
         oy.i var5 = this.x.get(this.T5);
         Vec3 var6 = this.T(this.T5, var2.y);
         int var7 = this.T5 == this.x.size() - 1 ? 1 : 0;
         double var8 = this.x == this.CY ? this.Tw : 0.245;
         if (var2.distanceToSqr(var6) < (var7 != 0 ? var8 * var8 : (var4 != 0 ? 0.49 : 0.12))) {
            this.T5++;
            if (this.T5 >= this.x.size()) {
               this.t();
               this.P("Arrived; looking for the next block");
               return;
            }

            var5 = this.x.get(this.T5);
            var6 = this.T(this.T5, var2.y);
            var7 = this.T5 == this.x.size() - 1 ? 1 : 0;
         }

         BlockPos var10 = new BlockPos(var5.x(), var1.bottom() - 1, var5.z());
         if (var10.equals(this.C3) && mc.player.tickCount < this.v) {
            this.P("Waiting for the placed block to land");
         } else if (!this.S(var10)) {
            if (!this.N(var10)) {
               this.Tz.add(var10.immutable());
               this.g("could not bridge the gap at " + var10.toShortString());
            }
         } else {
            double var11 = Math.sqrt(var2.distanceToSqr(var6));

            for (int var13 = this.T5 + 1; var13 < this.x.size(); var13++) {
               oy.i var14 = this.x.get(var13 - 1);
               oy.i var15 = this.x.get(var13);
               var11 += Math.hypot(var15.x() - var14.x(), var15.z() - var14.z());
            }

            Vec3 var24 = (var4 != 0 && var3 < this.x.size() ? this.T(var3, var2.y) : var6).subtract(var2).multiply(1.0, 0.0, 1.0);
            Vec3 var25 = mc.player.getDeltaMovement();
            double var26 = var24.lengthSqr() < 1.0E-6 ? 0.0 : var25.multiply(1.0, 0.0, 1.0).dot(var24.normalize());
            oy.i var17 = this.x.get(this.x.size() - 1);
            this.Tq = (boolean)(var11 < 0.9 && ad.m(var17.x(), var1.bottom(), var17.z()) ? 1 : 0);
            boolean var18 = this.rv.g(this.Tq ? var11 : var11 - 0.1, var26, _p.Q(this));
            this.rC = var18 ? var24 : Vec3.ZERO;
            this.Cg = (boolean)(var18 && this.uA.getValue() && !this.Tq && var11 > 4.0 && mc.player.getFoodData().getFoodLevel() > 6 ? 1 : 0);
            if (this.D == null
               || this.rz
               || !(mc.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(this.D)) <= 64.0)
               || !(var11 < 1.5) && !T(var24, Vec3.atCenterOf(this.D).subtract(var2))) {
               double var19 = mc.player.getEyeY() - (this.x == this.CY ? this.CQ : 0.6) + 0.12 * Math.sin(mc.player.tickCount * 0.09 + this.rQ);
               ArrayList var21 = new ArrayList(this.x.size() - this.T5);

               for (int var22 = this.T5; var22 < this.x.size(); var22++) {
                  var21.add(new Vec3(this.x.get(var22).x() + 0.5, var19, this.x.get(var22).z() + 0.5));
               }

               this.aimAt(ad.b(new Vec3(var2.x, var19, var2.z), var21, 3.0), 12.0F);
            } else {
               this.aimAt(Vec3.atCenterOf(this.D), 12.0F);
            }

            this.y(var18);
            this.P("Walking to " + (this.D == null ? "the next block" : this.D.toShortString()));
         }
      }
   }

   private int f(Vec3 var1, int var2) {
      if (Math.abs(var1.y - var2) > 0.6 && this.L() != var2) {
         return this.T5;
      }

      int var3 = this.T5;

      for (int var4 = this.T5 + 1; var4 < Math.min(this.x.size(), this.T5 + 1 + 8); var3 = var4++) {
         oy.i var5 = this.x.get(var4);
         if (!ad.G(mc.level, var1, var5.x() + 0.5, var5.z() + 0.5, var2)) {
            break;
         }
      }

      return var3;
   }

   private boolean t(Vec3 var1, int var2, int var3) {
      oy.i var4 = this.x.get(var2);
      oy.i var5 = this.x.get(var3);
      return Math.hypot(var5.x() + 0.5 - var1.x, var5.z() + 0.5 - var1.z) <= Math.hypot(var5.x() - var4.x(), var5.z() - var4.z()) + 0.05;
   }

   private void y(boolean var1) {
      Vec3 var2 = mc.player.position();
      if (var1 && this.rS != null && var2.distanceToSqr(this.rS) < 1.0E-4) {
         this.T3++;
      } else {
         this.T3 = 0;
      }

      this.rS = var2;
      if (this.T3 > 40) {
         this.g("movement blocked");
      }
   }

   private void g(String var1) {
      if (++this.r5 >= 3 && this.D != null) {
         this.D(this.D, var1);
      }

      _q.c(this.getName(), "Replanning route: {}", var1);
      this.x = List.of();
      this.T5 = 0;
      this.T3 = 0;
      this.Tg = -1000000000;
   }

   private boolean N(BlockPos var1) {
      int var2 = this.i();
      if (var2 >= 0 && this.uf.getValue()) {
         this.Tq = (boolean)1;
         if (this.K("bridge a gap")) {
            return true;
         } else if (!this.i(var2)) {
            this.P("Taking out " + mc.player.getInventory().getItem(var2).getHoverName().getString() + " to bridge a gap");
            return true;
         } else {
            Item var3 = mc.player.getInventory().getItem(var2).getItem();
            BlockPos var4 = var1.immutable();
            if (this.rd.getOrDefault(var4, 0) >= 3) {
               return false;
            } else if (mc.player.tickCount < this.uH) {
               this.P("Steadying before the next block");
               return true;
            } else {
               boolean var5 = vi.M(this, var4, var3, this.n(), true, true, () -> vi.i(var4) && !this.F(), () -> this.p(var4, var3, true), var2x -> {
                  this.e(var4);
                  _q.c(this.getName(), "Bridge block at {} {}", var4.toShortString(), var2x ? "placed" : "rejected");
               });
               this.P(var5 ? "Bridging a gap at " + var4.toShortString() : "Cannot reach the gap at " + var4.toShortString());
               return var5;
            }
         }
      } else {
         return false;
      }
   }

   private void u(ol.k var1) {
      if (!this.Td && this.N() >= 0) {
         this.Td = (boolean)1;
         if (this.E.getValue()) {
            _q.z(this.getName(), "Layer {}: drill faces done, sweeping leftovers", this.u4 + 1);
            this.TB = -1000000000;
            return;
         }
      }

      int var2 = 0;

      for (BlockPos var4 : this.rT) {
         if (vs.W(var4)) {
            var2++;
         }
      }

      _q.z(this.getName(), "Layer {} of {} done; {} blocks left (held back fluid, unreachable, set aside or not swept)", this.u4 + 1, this.Cx.n(), var2);
      this.u4++;
      this.Td = (boolean)(this.N() < 0 ? 1 : 0);
      this.u_ = (boolean)0;
      this.TU = -1;
      this.ri = null;
      this.rT.clear();
      this.ud.clear();
      this.TB = -1000000000;
      this.C2 = this.D = null;
      this.t();
      if (this.u4 >= this.Cx.n()) {
         this.u4 = this.Cx.n() - 1;
         if (this.TC.getValue()) {
            this.rz = (boolean)1;
            this.P("Picking up drops");
            _q.z(this.getName(), "Picking up drops in the area");
         } else {
            this.X();
         }
      } else {
         this.P("Moving down to layer " + (this.u4 + 1));
      }
   }

   private void X() {
      if (this.Ci) {
         this.C9 = null;
         this.Cx = this.T9;
         this.CX = 0;
         this.W = this.O;
      } else if (this.C9 == null && this.J()) {
         return;
      }

      ol.c var1 = this.Cx.T();
      int var2 = 0;
      if (this.C9 != null) {
         var2 = this.V();
      } else {
         for (int var3 = var1.minY(); var3 <= this.Cx.i(this.CX).top(); var3++) {
            for (int var4 = var1.minX(); var4 <= var1.maxX(); var4++) {
               for (int var5 = var1.minZ(); var5 <= var1.maxZ(); var5++) {
                  if (vs.W(new BlockPos(var4, var3, var5))) {
                     var2++;
                  }
               }
            }
         }
      }

      String var15 = t(System.currentTimeMillis() - this.CR);
      if (this.C9 != null) {
         String var17 = this.a
            + " mined out in "
            + var15
            + ", "
            + Math.max(0, this.W - var2)
            + " blocks mined"
            + (var2 > 0 ? ", " + var2 + " left standing (next to lava or water, unreachable or put back by the server)" : "");
         this.M(var17 + ".");
         _q.z(this.getName(), "Block set done: {}", var17);
         if (this.uu.getValue()) {
            ib.L().N("Auto Area Miner", this.a + " mined out in " + var15 + (var2 > 0 ? ", " + var2 + " left" : ""), ib.v.x, 8000L);
         }

         this.disable();
      } else {
         int var16 = 0;
         int var18 = 0;
         int var6 = 0;
         int var7 = 0;
         BlockPos var8 = null;
         ArrayList var9 = new ArrayList();

         for (int var10 = this.Cx.i(this.CX).top(); var10 >= var1.minY(); var10--) {
            for (int var11 = var1.minX(); var11 <= var1.maxX(); var11++) {
               for (int var12 = var1.minZ(); var12 <= var1.maxZ(); var12++) {
                  BlockPos var13 = new BlockPos(var11, var10, var12);
                  if (vs.W(var13)) {
                     String var14;
                     if (this.O(var13)) {
                        var16++;
                        var14 = "holds back a fluid";
                     } else if (this.c.contains(var13)) {
                        var18++;
                        var14 = "put back by the server";
                     } else if (this.rw.contains(var13)) {
                        var6++;
                        var14 = "drill would cut outside";
                     } else {
                        var7++;
                        var14 = "not reached";
                        if (var8 == null) {
                           var8 = var13;
                        }
                     }

                     if (var9.size() < 24) {
                        var9.add(
                           var13.toShortString()
                              + " "
                              + BuiltInRegistries.BLOCK.getKey(mc.level.getBlockState(var13).getBlock()).getPath()
                              + " ("
                              + var14
                              + ")"
                        );
                     }
                  }
               }
            }
         }

         int var19 = Math.max(0, this.W - var2);
         int var20 = this.CX > 0 ? this.g(this.Cx.i(this.CX).top()) : 0;
         String var21;
         if (var2 == 0) {
            var21 = "area cleared in " + var15 + (this.W >= 0 ? ", " + var19 + " blocks mined" : "");
         } else {
            ArrayList var22 = new ArrayList();
            if (var16 > 0) {
               var22.add(var16 + " next to lava or water, left so the area does not flood");
            }

            if (var18 > 0) {
               var22.add(var18 + " the server keeps putting back");
            }

            if (var6 > 0) {
               var22.add(var6 + " at the edges, where your drill would cut outside the area (add a plain pickaxe to clear them)");
            }

            if (var7 > 0) {
               var22.add(var7 + " it could not reach or see, the first at " + var8.toShortString());
            }

            var21 = "finished in "
               + var15
               + (this.W >= 0 ? ", " + var19 + " blocks mined" : "")
               + ", but "
               + var2
               + " blocks are still standing: "
               + String.join("; ", var22);
         }

         if (var20 > 0) {
            var21 = var21 + ". The " + var20 + " blocks in the layers above where you started were left as they are";
         }

         this.M(var21 + ".");
         _q.z(this.getName(), "Done: {}", var21);
         if (!var9.isEmpty()) {
            _q.z(this.getName(), "Still standing: {}{}", String.join(", ", var9), var2 > var9.size() ? " and more" : "");
         }

         if (this.uu.getValue()) {
            ib.L()
               .N(
                  "Auto Area Miner",
                  var2 == 0 ? "Area cleared in " + var15 : "Finished with " + var2 + " blocks still standing",
                  var2 == 0 ? ib.v.x : ib.v.o,
                  8000L
               );
         }

         this.rY = (boolean)1;
         this.disable();
      }
   }

   private boolean J() {
      ol.c var1 = this.Cx.T();
      if (var1.volume() > 2000000L) {
         return false;
      }

      HashSet var2 = new HashSet();
      int var3 = 0;
      int var4 = this.Cx.i(this.CX).top();

      for (int var5 = var1.maxY(); var5 >= var1.minY(); var5--) {
         for (int var6 = var1.minX(); var6 <= var1.maxX(); var6++) {
            for (int var7 = var1.minZ(); var7 <= var1.maxZ(); var7++) {
               BlockPos var8 = new BlockPos(var6, var5, var7);
               if (vs.W(var8) && !this.O(var8) && !this.c.contains(var8)) {
                  var2.add(var8);
                  if (var5 > var4) {
                     var3++;
                  }
               }
            }
         }
      }

      if (!var2.isEmpty() && var2.size() <= 20000) {
         String var11 = var3 == var2.size()
            ? var2.size() + " blocks in the layers above where you started"
            : (var3 > 0 ? var2.size() + " blocks (" + var3 + " of them above where you started)" : var2.size() + " blocks");
         this.M("checking the whole area: " + var11 + " are still standing, so the miner goes back for them.");
         _q.z(this.getName(), "Final check: {} blocks still standing ({} above layer {}); mining them as a block set", var2.size(), var3, this.CX + 1);
         this.T9 = this.Cx;
         this.O = this.W < 0 ? -1 : this.W + var3;
         this.Ci = (boolean)1;
         this.C9 = var2;
         this.a = "the area's leftovers";
         BlockPos var12 = this.i();
         int[] var13 = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
         int[] var14 = new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};

         for (BlockPos var10 : var2) {
            var13[0] = Math.min(var13[0], var10.getX());
            var13[1] = Math.min(var13[1], var10.getY());
            var13[2] = Math.min(var13[2], var10.getZ());
            var14[0] = Math.max(var14[0], var10.getX());
            var14[1] = Math.max(var14[1], var10.getY());
            var14[2] = Math.max(var14[2], var10.getZ());
         }

         this.Cx = new ol(ol.c.of(var13, var14), this.r7.getValue().intValue(), this.rg.getValue().intValue(), var12.getX(), var12.getZ());
         this.Td = (boolean)1;
         this.u4 = this.CX = 0;
         this.rT.clear();
         this.ud.clear();
         this.TB = -1000000000;
         this.C2 = this.D = null;
         this.TT = null;
         this.t();
         this.rz = (boolean)0;
         this.us = this.uh = 0;
         this.Ts.clear();
         this.j.clear();
         this.f.clear();
         this.u7.clear();
         this.uX.clear();
         this.Cz = (boolean)0;
         this.y.V();
         this.CU = -1000000000;
         this.e = 0;
         this.T.clear();
         this.N = this.V();
         this.uS = mc.player.tickCount;
         this.P("Going back for " + var2.size() + " blocks");
         return true;
      } else {
         return false;
      }
   }

   private String g() {
      return this.Cf.getValue().trim() + "|" + this.T_.getValue().trim() + "|" + this.r7.getValue().intValue();
   }

   private int J() {
      ol.c var1 = this.Cx.T();
      if (var1.volume() > 200000L) {
         return -2;
      }

      MutableBlockPos var2 = new MutableBlockPos();

      for (int var3 = var1.maxY(); var3 >= var1.minY(); var3--) {
         for (int var4 = var1.minX(); var4 <= var1.maxX(); var4++) {
            for (int var5 = var1.minZ(); var5 <= var1.maxZ(); var5++) {
               if (vs.W(var2.set(var4, var3, var5)) && !this.O(var2)) {
                  return this.Cx.K(var3);
               }
            }
         }
      }

      return -1;
   }

   private int g(int var1) {
      ol.c var2 = this.Cx.T();
      if (var2.volume() > 200000L) {
         return 0;
      }

      int var3 = 0;
      MutableBlockPos var4 = new MutableBlockPos();

      for (int var5 = var1 + 1; var5 <= var2.maxY(); var5++) {
         for (int var6 = var2.minX(); var6 <= var2.maxX(); var6++) {
            for (int var7 = var2.minZ(); var7 <= var2.maxZ(); var7++) {
               if (vs.W(var4.set(var6, var5, var7))) {
                  var3++;
               }
            }
         }
      }

      return var3;
   }

   private boolean t(BlockPos var1) {
      ol.c var2 = this.Cx.T();
      if (!var2.containsColumn(var1.getX(), var1.getZ())) {
         return false;
      }

      MutableBlockPos var3 = new MutableBlockPos();

      for (int var4 = var1.getY() + 1; var4 <= var2.maxY(); var4++) {
         if (!mc.level.getBlockState(var3.set(var1.getX(), var4, var1.getZ())).getCollisionShape(mc.level, var3).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private void Q() {
      vs.B(this);
      ol.c var1 = this.Cx.T();
      int var2 = this.L();
      AABB var3 = new AABB(var1.minX() - 1, var2 - 1, var1.minZ() - 1, var1.maxX() + 2, var2 + 2, var1.maxZ() + 2);
      List var4 = mc.level.getEntitiesOfClass(ItemEntity.class, var3, var1x -> var1x.isAlive() && !this.Ts.contains(var1x.getId()));
      if (!var4.isEmpty() && ++this.us <= 1200) {
         Vec3 var5 = mc.player.position();
         ItemEntity var6 = var4.stream().min(Comparator.comparingDouble(var1x -> var1x.position().distanceToSqr(var5))).orElseThrow();
         BlockPos var7 = BlockPos.containing(var6.position());
         if (var7.getX() == mc.player.getBlockX() && var7.getZ() == mc.player.getBlockZ()) {
            this.z(Vec3.atBottomCenterOf(var7));
            if (++this.uh > 30) {
               this.Ts.add(var6.getId());
               this.uh = 0;
            }

            this.P("Picking up drops (" + var4.size() + " left)");
         } else {
            this.uh = 0;
            if (this.x.isEmpty() || this.D == null || !this.D.equals(var7)) {
               List var8 = this.C(var2, (var1x, var2x) -> var1x == var7.getX() && var2x == var7.getZ());
               if (var8 == null || var8.isEmpty()) {
                  this.Ts.add(var6.getId());
                  this.t();
                  return;
               }

               this.x = var8;
               this.T5 = 0;
               this.D = var7;
            }

            this.v(var2);
            this.P("Picking up drops (" + var4.size() + " left)");
         }
      } else {
         if (!var4.isEmpty()) {
            _q.z(this.getName(), "Left {} drops after {} ticks of picking up", var4.size(), 1200);
         }

         this.X();
      }
   }

   private BlockPos z(int var1) {
      int[] var2 = this.Cx.H(0, var1);
      int var3 = (var2[0] + var2[1]) / 2;
      ol.c var4 = this.Cx.T();
      int var5 = this.Cx.w(0, var1) > 0 ? (this.Cx.w() ? var4.minX() : var4.minZ()) : (this.Cx.w() ? var4.maxX() : var4.maxZ());
      return this.Cx.w() ? new BlockPos(var5, 0, var3) : new BlockPos(var3, 0, var5);
   }

   private void Y(ol.k var1) {
      if (!this.V() || this.ry || !this.C0.stream().anyMatch(var0 -> var0.kind() == ay.t.u) || !this.D(var1)) {
         BlockPos var2 = this.i();
         BlockPos var3 = var2.below();
         if (this.ri == null) {
            BlockPos var4 = this.z(this.u4);
            int var5 = var2.getY();
            List var6 = this.C(var5, (var4x, var5x) -> var4x == var4.getX() && var5x == var4.getZ() && this.u(new BlockPos(var4x, var5 - 1, var5x), var1));
            if (var6 == null) {
               var6 = this.C(var5, (var3x, var4x) -> this.Cx.T().containsColumn(var3x, var4x) && this.u(new BlockPos(var3x, var5 - 1, var4x), var1));
            }

            if (var6 == null) {
               this.p("there is no safe place to dig down into layer " + (this.u4 + 1) + "; stand on top of the area");
               return;
            }

            oy.i var7 = var6.isEmpty() ? new oy.i(var2.getX(), var2.getZ()) : (oy.i)var6.getLast();
            this.ri = new BlockPos(var7.x(), var5, var7.z());
            this.x = var6;
            this.T5 = 0;
            this.D = null;
            _q.c(this.getName(), "Digging down to layer {} at {}", this.u4 + 1, this.ri.toShortString());
         }

         if (var2.getX() == this.ri.getX() && var2.getZ() == this.ri.getZ()) {
            this.t();
            if (!this.u(var3, var1)) {
               this.ri = null;
               this.P("Looking for a safe place to dig down");
            } else {
               Vec3 var8 = Vec3.atBottomCenterOf(var2);
               if (mc.player.position().multiply(1.0, 0.0, 1.0).distanceToSqr(var8.multiply(1.0, 0.0, 1.0)) > 0.03) {
                  this.z(var8);
                  this.P("Lining up to dig down");
               } else if (!vs.W(var3)) {
                  this.P("Waiting to drop into layer " + (this.u4 + 1));
               } else {
                  this.b(var3);
                  this.Cz = (boolean)0;
                  this.v(var3, false, "Digging down to layer " + (this.u4 + 1));
                  this.ri = var2.below();
               }
            }
         } else if (this.x.isEmpty()) {
            this.ri = null;
            this.t();
         } else {
            this.v(var2.getY());
         }
      }
   }

   private void v(int var1) {
      ol.k var2 = new ol.k(this.u4, var1, var1);
      this.n(var2);
   }

   private boolean u(BlockPos var1, ol.k var2) {
      if (this.Cx.T().contains(var1.getX(), var1.getY(), var1.getZ()) && mc.level.hasChunkAt(var1) && !this.U(var1)) {
         if (this.j() && this.f() && vs.W(var1) && !this.p(var1, Direction.UP)) {
            return false;
         }

         if (!vs.W(var1) && !mc.level.getBlockState(var1).getCollisionShape(mc.level, var1).isEmpty()) {
            return false;
         }

         for (Direction var6 : Direction.values()) {
            if (var6 != Direction.UP && !mc.level.getFluidState(var1.relative(var6)).isEmpty()) {
               return false;
            }
         }

         int var7 = var1.getY() + 1;

         for (int var8 = var1.getY() - 1; var8 >= var1.getY() - 3 - 1; var8--) {
            BlockPos var9 = new BlockPos(var1.getX(), var8, var1.getZ());
            if (!mc.level.getFluidState(var9).isEmpty()) {
               return false;
            }

            BlockState var10 = mc.level.getBlockState(var9);
            if (!var10.getCollisionShape(mc.level, var9).isEmpty()) {
               return var8 + 1 >= var2.bottom() && ol.f(var7, var8, 3) && !var10.is(Blocks.MAGMA_BLOCK);
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private void z(Vec3 var1) {
      Vec3 var2 = mc.player.position();
      Vec3 var3 = var1.subtract(var2).multiply(1.0, 0.0, 1.0);
      double var4 = var3.length();
      double var6 = var4 < 1.0E-6 ? 0.0 : mc.player.getDeltaMovement().multiply(1.0, 0.0, 1.0).dot(var3.normalize());
      this.Tq = (boolean)1;
      boolean var8 = this.rv.g(var4, var6, _p.Q(this));
      this.rC = var8 ? var3 : Vec3.ZERO;
   }

   private void t(ol.k var1) {
      BlockPos var2 = this.i();
      if (!this.uf.getValue()) {
         this.p("you are below layer " + (this.u4 + 1) + "; turn on Place Blocks to pillar up, or climb back up");
      } else if (this.i() < 0) {
         this.p("you are below layer " + (this.u4 + 1) + " and there is no solid block in your hotbar to pillar up with");
      } else {
         BlockPos var3 = var2.above(2);
         if (this.P(var3)) {
            Vec3 var4 = Vec3.atBottomCenterOf(var2);
            if (mc.player.position().multiply(1.0, 0.0, 1.0).distanceToSqr(var4.multiply(1.0, 0.0, 1.0)) > 0.03) {
               this.z(var4);
               this.P("Lining up to pillar up");
            } else {
               vs.B(this);
               if (!this.K("pillar up")) {
                  if (!this.i(this.i())) {
                     this.P("Taking out a block to pillar up");
                  } else if (mc.player.getXRot() < 84.0F || this.O() > 1.5F) {
                     this.G();
                     this.P("Looking down to pillar up");
                  } else if (mc.player.tickCount < this.uH) {
                     this.G();
                     this.P("Steadying before the next block");
                  } else {
                     this.rl = 86.0F + (float)this.TH.nextDouble() * 3.0F;
                     this.uG = var2.immutable();
                     this.C5 = mc.player.tickCount;
                     this.Co = (boolean)0;
                     this.u0 = (boolean)1;
                     this.P("Pillaring up to layer " + (this.u4 + 1));
                  }
               }
            }
         } else {
            if (this.Cx.T().contains(var3.getX(), var3.getY(), var3.getZ()) && vs.W(var3) && !this.O(var3)) {
               this.b(var3);
               this.Cz = (boolean)0;
               this.v(var3, false, "Clearing headroom to pillar up");
            } else {
               this.p("there is no room to pillar up back to layer " + (this.u4 + 1));
            }
         }
      }
   }

   private void q() {
      this.G();
      this.Tq = (boolean)0;
      int var1 = this.i();
      if (!this.Co && var1 >= 0 && !mc.player.onGround() && mc.player.getY() >= this.uG.getY() + 1.05 && mc.player.tickCount >= this.uH && vi.i(this.uG)) {
         Item var2 = mc.player.getInventory().getItem(var1).getItem();
         BlockPos var3 = this.uG;
         vi.M(
            this,
            var3,
            var2,
            this.n(),
            true,
            true,
            () -> vi.i(var3) && mc.player.getY() >= var3.getY() + 1,
            () -> ((q71cffb54eb30bf94)mc.player).larpYLast() >= var3.getY() + 1.02 && this.O() <= 1.5F,
            var2x -> {
               this.e(var3);
               if (var2x) {
                  this.Co = (boolean)1;
               }
            }
         );
      }

      if (mc.player.onGround() && mc.player.tickCount - this.C5 > 2) {
         int var4 = this.L() > this.uG.getY() ? 1 : 0;
         if (var4 == 0 && ++this.Ck >= 5) {
            this.uG = null;
            this.p("pillaring up failed five times");
         } else {
            if (var4 != 0) {
               this.Ck = 0;
            }

            this.uG = null;
         }
      } else {
         if (mc.player.tickCount - this.C5 > 30) {
            this.uG = null;
            this.Ck++;
         }
      }
   }

   private void G() {
      if (_p.Q(this)) {
         float[] var1 = this.rv.n(mc.player.getYRot(), mc.player.getXRot(), mc.player.getYRot(), this.rl, mc.player.getXRot() >= this.rl - 1.5F, true);
         b6.P(this, b6.w.x, var1[0], var1[1], 1.0);
      }
   }

   @Override
   protected boolean holdsToolBetweenBlocks() {
      return true;
   }

   @Override
   public boolean aimsAtOpenFaces(BlockPos var1) {
      if (this.V()) {
         return true;
      } else if (this.Cx != null && this.u4 < this.Cx.n() && !this.Td && this.N() >= 0 && !this.b(var1)) {
         int var2 = this.Cx.x(var1.getX(), var1.getZ(), this.u4);
         return !var1.equals(this.E(var2, this.Cx.w() ? var1.getX() : var1.getZ(), this.Cx.i(this.u4)));
      } else {
         return true;
      }
   }

   @Override
   public boolean aimsAtFaceCentre(BlockPos var1) {
      return var1.equals(this.T1);
   }

   @Override
   public void digFinished(BlockPos var1) {
      if (this.Cx != null && mc.player != null) {
         this.uN.q(var1.asLong());
         if (this.rc.size() < 256) {
            this.rc.put(var1.immutable(), mc.player.tickCount);
         }
      }
   }

   @Override
   public boolean aimFaceAllowed(BlockPos var1, Direction var2) {
      if (this.T1 != null && var1.equals(this.T1)) {
         return var2 == this.r4;
      }

      if (this.Cx != null && this.j() && this.f()) {
         if (this.B(mc.level.getBlockState(var1), this.N()) == this.N() && !this.p(var1, var2)) {
            this.rw.add(var1.immutable());
            return false;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private boolean R(BlockPos var1) {
      double var2 = this.n();
      return mc.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var1)) <= var2 * var2;
   }

   private boolean z() {
      for (BlockPos var2 : this.uc) {
         if (vs.W(var2)) {
            return false;
         }
      }

      return true;
   }

   @Override
   public boolean trustsSupport(BlockPos var1) {
      if (mc.player == null) {
         return true;
      } else {
         return !var1.equals(this.C2) && !this.uX.containsKey(var1) ? mc.player.tickCount >= this.CB || !this.uc.contains(var1) : false;
      }
   }

   @Override
   public boolean waitsForSentLook() {
      return true;
   }

   @Override
   protected void aimAt(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         if (_p.Q(this)) {
            if (this.rk && this.C2 != null && this.C2.equals(this.T1)) {
               var1 = N(mc.player.getEyePosition(), this.T1, this.r4, this.Tv, this.un);
            }

            float[] var3 = b6.R(mc.player.getEyePosition(), var1);
            BlockHitResult var4 = this.C2 == null ? null : vs.r(this.C2, this.n());
            int var5 = var4 == null || this.T1 != null && this.C2.equals(this.T1) && var4.getDirection() != this.r4 ? 0 : 1;
            float[] var6 = this.rv.M(mc.player.getYRot(), mc.player.getXRot(), var3[0], var3[1], (boolean)var5);
            b6.P(this, b6.w.x, var6[0], var6[1], 1.0);
         }
      }
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         float[] var3 = this.rv.n(mc.player.getYRot(), mc.player.getXRot(), var1, var2, false, true);
         b6.P(this, b6.w.x, var3[0], var3[1], 1.0);
      }
   }

   public static Input i() {
      if (Tt != null && Tt.isEnabled()) {
         _6 var0 = Tt;
         Minecraft var1 = Minecraft.getInstance();
         if (var0 != null
            && var0.isRuntimeActive()
            && var0.Cx != null
            && var1.player != null
            && var1.player == var0.Cb
            && var1.player.tickCount == var0.d
            && !ij.y()
            && !dt.x()
            && !(var1.gui.screen() instanceof AbstractContainerScreen)
            && (!b6.x() || b6.c(b6.w.x))) {
            int var2 = var0.rC.lengthSqr() > 1.0E-6 ? 1 : 0;
            if (var2 == 0 && !var0.u0 && !var0.Tq) {
               return null;
            }

            Input var3 = var2 != 0 ? var0.rv.q(var1.player.getYRot(), var0.rC) : new Input(false, false, false, false, false, false, false);
            return new Input(var3.forward(), var3.backward(), var3.left(), var3.right(), var0.u0, var0.Tq, var0.Cg);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void M(String var1) {
      if (mc != null && mc.player != null) {
         mc.player.sendSystemMessage(Component.literal("Larp Auto Area Miner: " + var1));
      }
   }

   private void p(String var1) {
      _q.z(this.getName(), "Stopped: {}", var1);
      this.M("stopped, " + var1 + ".");
      ib.L().t("Auto Area Miner stopped", var1.substring(0, 1).toUpperCase(Locale.ROOT) + var1.substring(1), ib.v.o);
      this.P("Stopped: " + var1);
      this.disable();
   }

   private void c() {
      if (this.isInGame()) {
         if (this.isRuntimeActive() && this.Cx != null && mc.level == this.ub) {
            try {
               int var1 = com.larpclient.c.A(0.25);
               if (this.TA.getValue()) {
                  ol.c var2 = this.Cx.T();
                  Gizmos.cuboid(
                        new AABB(var2.minX(), var2.minY(), var2.minZ(), var2.maxX() + 1, var2.maxY() + 1, var2.maxZ() + 1),
                        GizmoStyle.strokeAndFill(vv.N(var1, 0.9), 1.5F, 0)
                     )
                     .setAlwaysOnTop();
                  if (this.u4 < this.Cx.n()) {
                     ol.k var3 = this.Cx.i(this.u4);
                     Gizmos.cuboid(
                        new AABB(var2.minX(), var3.bottom(), var2.minZ(), var2.maxX() + 1, var3.top() + 1, var2.maxZ() + 1),
                        GizmoStyle.strokeAndFill(vv.N(var1, 0.5), 1.0F, vv.N(var1, 0.06))
                     );
                  }

                  if (this.C2 != null) {
                     Gizmos.cuboid(
                           new AABB(this.C2).inflate(0.01),
                           GizmoStyle.strokeAndFill(vv.N(this.Cz ? -14249 : var1, 1.0), 2.0F, vv.N(this.Cz ? -14249 : var1, 0.18))
                        )
                        .setAlwaysOnTop();
                  }
               }

               if (this.V.getValue() && this.T5 < this.x.size()) {
                  double var9 = mc.player.getY() + 0.05;
                  Vec3 var4 = new Vec3(mc.player.getX(), var9, mc.player.getZ());

                  for (int var5 = this.T5; var5 < this.x.size(); var5++) {
                     oy.i var6 = this.x.get(var5);
                     Vec3 var7 = new Vec3(var6.x() + 0.5, var9, var6.z() + 0.5);
                     Gizmos.line(var4, var7, vv.N(var1, 0.85), 2.0F).setAlwaysOnTop();
                     var4 = var7;
                  }
               }
            } catch (RuntimeException var8) {
               _q.g(this.getName(), "Area overlay failed", var8);
            }
         } else {
            this.i();
         }
      }
   }

   private void i() {
      if (this.uK) {
         this.w();
      } else if (System.currentTimeMillis() <= this.Cm) {
         ol.c var1 = this.q();
         if (var1 != null) {
            try {
               int var2 = com.larpclient.c.A(0.25);
               Gizmos.cuboid(
                     new AABB(var1.minX(), var1.minY(), var1.minZ(), var1.maxX() + 1, var1.maxY() + 1, var1.maxZ() + 1),
                     GizmoStyle.strokeAndFill(vv.N(var2, 0.95), 2.0F, vv.N(var2, 0.08))
                  )
                  .setAlwaysOnTop();

               for (String var6 : new String[]{this.Cf.getValue(), this.T_.getValue()}) {
                  int[] var7 = ol.z(var6);
                  if (var7 != null) {
                     Gizmos.cuboid(
                           new AABB(var7[0], var7[1], var7[2], var7[0] + 1, var7[1] + 1, var7[2] + 1).inflate(0.02),
                           GizmoStyle.strokeAndFill(-1, 2.0F, vv.N(var2, 0.35))
                        )
                        .setAlwaysOnTop();
                  }
               }
            } catch (RuntimeException var8) {
               _q.g(this.getName(), "Area preview failed", var8);
            }
         }
      }
   }

   private void w() {
      try {
         int var1 = com.larpclient.c.A(0.25);
         int[] var2 = ol.z(this.Cf.getValue());
         int[] var3 = ol.z(this.T_.getValue());
         int[] var4 = this.A == null ? null : new int[]{this.A.getX(), this.A.getY(), this.A.getZ()};
         int[] var5 = var2 != null ? var2 : var4;
         int[] var6 = var3 != null ? var3 : var4;
         if (var5 != null && var6 != null) {
            ol.c var7 = ol.c.of(var5, var6);
            Gizmos.cuboid(
                  new AABB(var7.minX(), var7.minY(), var7.minZ(), var7.maxX() + 1, var7.maxY() + 1, var7.maxZ() + 1),
                  GizmoStyle.strokeAndFill(vv.N(var1, 0.95), 2.0F, vv.N(var1, 0.08))
               )
               .setAlwaysOnTop();
         }

         for (int[] var10 : new int[][]{var2, var3}) {
            if (var10 != null) {
               Gizmos.cuboid(
                     new AABB(var10[0], var10[1], var10[2], var10[0] + 1, var10[1] + 1, var10[2] + 1).inflate(0.02),
                     GizmoStyle.strokeAndFill(-1, 2.0F, vv.N(var1, 0.35))
                  )
                  .setAlwaysOnTop();
            }
         }

         if (this.A != null) {
            Gizmos.cuboid(new AABB(this.A).inflate(0.03), GizmoStyle.strokeAndFill(-14249, 2.0F, 872400983)).setAlwaysOnTop();
         }
      } catch (RuntimeException var11) {
         _q.g(this.getName(), "Corner picking overlay failed", var11);
      }
   }

   private int D() {
      if (this.C9 != null) {
         return this.V();
      }

      ol.c var1 = this.Cx == null ? this.q() : this.Cx.T();
      if (var1 != null && var1.volume() <= 200000L) {
         int var2 = 0;
         MutableBlockPos var3 = new MutableBlockPos();
         int var4 = this.Cx == null ? var1.maxY() : this.Cx.i(this.CX).top();

         for (int var5 = var1.minY(); var5 <= var4; var5++) {
            for (int var6 = var1.minX(); var6 <= var1.maxX(); var6++) {
               for (int var7 = var1.minZ(); var7 <= var1.maxZ(); var7++) {
                  if (vs.W(var3.set(var6, var5, var7))) {
                     var2++;
                  }
               }
            }
         }

         return var2;
      } else {
         return -1;
      }
   }

   private String r() {
      StringBuilder var1 = new StringBuilder("Layer ").append(Math.min(this.u4 + 1, this.Cx.n())).append(" of ").append(this.Cx.n());
      if (this.W > 0 && this.N >= 0) {
         var1.append(", ").append(this.y()).append("% done");
      }

      var1.append(", ").append(t(System.currentTimeMillis() - this.CR));
      return var1.toString();
   }

   private int y() {
      return this.W > 0 && this.N >= 0 ? Math.clamp(Math.round(100.0 * (this.W - this.N) / this.W), 0, 100) : 0;
   }

   static String t(long var0) {
      return ac.Q(var0);
   }

   @Override
   public ac.g hudCard() {
      if (!this.isInGame()) {
         return null;
      }

      if (this.Cc.getValue() && this.isRuntimeActive() && this.Cx != null && mc.level == this.ub) {
         int var6 = this.W > 0 && this.N >= 0 ? 1 : 0;
         String var7 = this.getStatus();
         return new ac.g(
            "Layer " + Math.min(this.u4 + 1, this.Cx.n()) + " of " + this.Cx.n(),
            var6 != 0 ? this.y() + "%" : "",
            var7 != null && !var7.isBlank() ? var7 : "Starting",
            t(System.currentTimeMillis() - this.CR),
            var6 != 0 ? Math.clamp((double)(this.W - this.N) / this.W, 0.0, 1.0) : -1.0
         );
      }

      if (!this.uK) {
         return null;
      }

      ol.c var1 = this.q();
      int var2 = (this.Tb[0] ? 1 : 0) + (this.Tb[1] ? 1 : 0);
      FreecamModule var3 = this.u();
      double var4 = var3 == null ? 0.0 : var3.O();
      return new ac.g(
         var1 == null ? "Choose the area" : var1.describe(),
         var2 + " of 2",
         var2 == 2 ? "Esc to finish, scroll changes camera speed" : "Left click corner 1, right click corner 2",
         var4 > 0.0 ? Math.round(var4) + " b/s" : "",
         var2 / 2.0
      );
   }

   public ol B() {
      return this.Cx;
   }

   public int g() {
      return this.u4;
   }

   public String j() {
      return this.o.name();
   }

   private enum t {
      d,
      b,
      f;
   }
}

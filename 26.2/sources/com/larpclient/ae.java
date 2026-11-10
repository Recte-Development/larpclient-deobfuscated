package com.larpclient;

import com.larp.obf.qa300483f54d804a3.q1881b7c262e73011;
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

public final class ae extends ar implements s1, t1.s {
   private static ae P;
   private static boolean A;
   static final int YD = 400;
   static final int VW = 3;
   static final int YX = 2400;
   static final int uo = 600;
   static final int W = 3;
   static final int gT = 40;
   private static final int V7 = 8;
   private static final int gv = 20000;
   private static final int uy = 2;
   static final int VO = -1000000000;
   static final int YW = 200;
   static final int Yl = 60;
   private final com.larpclient.module.g Q = this.addSetting(
      new com.larpclient.module.g("Corner 1", "First corner as x y z, or use Pick with camera", "").F("x y z")
   );
   private final com.larpclient.module.g D = this.addSetting(
      new com.larpclient.module.g("Corner 2", "Second corner as x y z, or use Pick with camera", "").F("x y z")
   );
   private final com.larpclient.module.g VV = this.addSetting(
      new com.larpclient.module.g(
            "Drill Name", "Part of your 3x3 drill's name, not case sensitive. The drill cuts 3x3 faces and a plain pickaxe cleans up the rest", "Drill"
         )
         .F("Drill")
   );
   private final com.larpclient.module.e uM = this.Z(
      "Layer Height", "How tall each layer is. The area is cleared from the top down, one layer at a time", 3.0, 1.0, 6.0, 1.0
   );
   private final com.larpclient.module.e VZ = this.Z("Pass Width", "How wide each row of a layer is", 3.0, 1.0, 5.0, 1.0);
   private final com.larpclient.module.e gg = this.Z("Reach", "How far the miner reaches, capped by your real reach", 4.5, 3.0, 6.0, 0.5);
   private final com.larpclient.module.s J = this.U(
      "Sweep Leftovers", "After the drill finishes a layer, mine every block still standing before moving down", true
   );
   private final com.larpclient.module.s VD = this.U("Sprint", "Sprint while walking", true);
   private final com.larpclient.module.s R = this.U("Place Blocks", "Pillar up, bridge gaps and plug lava or water with solid blocks from your hotbar", true);
   private final com.larpclient.module.s Vs = this.U("Auto Eat", "Eat food at 10 hunger or less, and a golden apple after losing the amount below", true);
   private final com.larpclient.module.e Yc = this.Z(
      "Gapple After Losing", "Hunger bars lost before a golden apple from your hotbar is eaten", 2.0, 1.0, 10.0, 1.0
   );
   private final com.larpclient.module.s Vr = this.U("XP Repair", "When your tool runs low, throw XP bottles from your hotbar to mend it", true);
   private final com.larpclient.module.e L = this.Z(
      "Repair At", "Uses left on the tool that starts XP repair, or stops the run if it cannot be mended", 50.0, 1.0, 1000.0, 1.0
   );
   private final com.larpclient.module.s uN = this.U("Show Area", "Draw the selected area and the block being mined", true);
   private final com.larpclient.module.s uH = this.U("Show Path", "Draw the route the miner is walking", true);
   private final com.larpclient.module.s gF = this.U(
      "Pause In GUI",
      "Stop while a menu other than chat or the pause menu is open, such as the ClickGUI. Chat and the pause menu never stop it; inventories always make it hold still",
      true
   );
   private final com.larpclient.module.s O = this.U("Notify When Done", "Show a notification when the area is cleared", true);
   private final com.larpclient.module.s YU = this.U(
      "Show Progress", "Show a small card at the top of the screen with the layer, progress and what the miner is doing", true
   );
   private final com.larpclient.module.s g6 = this.U("Collect Drops", "After the last layer, walk over the items lying in the area to pick them up", true);
   private final com.larpclient.module.v Vp = this.addSetting(
      new com.larpclient.module.v(
         "Select Area Key", "Press to pick the corners with a free camera: left click sets one corner, right click the other, Esc finishes", -1
      )
   );
   private final com.larpclient.module.s g9 = this.U(
      "Auto Tool",
      "Take the right hotbar tool for each block: a shovel for dirt, sand and gravel, an axe for wood, shears or a hoe for leaves. The drill still cuts its 3x3 faces. Off mines everything with your pickaxe",
      true
   );
   private final com.larpclient.module.l uu = this.addSetting(
      new com.larpclient.module.l(
         "Drill Cut",
         "Auto watches what each drill hit breaks and uses 3x3 faces once it sees one. 3x3 trusts your drill from the first hit; One Block uses it like a normal pickaxe",
         "Auto",
         "Auto",
         "3x3",
         "One Block"
      )
   );
   private final com.larpclient.module.s p = this.U(
      "Crouch Sometimes",
      "Crouch where players do: while cutting the step down to the next layer and beside a drop, and now and then while mining with the pickaxe. Never for drill hits, which need you standing",
      true
   );
   private final com.larpclient.module.e Ym = this.Z(
      "Camera Speed",
      "How fast the free camera flies while you pick corners, in blocks per second. The mouse wheel changes it until you finish. Your normal Freecam speed stays as it is",
      6.0,
      1.0,
      40.0,
      1.0
   );
   private u1 B;
   private Object ux;
   private Object YZ;
   private int Yt;
   private boolean g2;
   private boolean gd;
   private ae.g um = ae.g.i;
   private final List<BlockPos> Yy = new ArrayList<>();
   private final List<BlockPos> uv = new ArrayList<>();
   private int gE = -1000000000;
   private int Y;
   private final Map<BlockPos, Integer> uW = new HashMap<>();
   private final Map<BlockPos, Integer> Yp = new HashMap<>();
   private final Map<BlockPos, Integer> Vh = new HashMap<>();
   private final Map<BlockPos, Integer> Y1 = new HashMap<>();
   private final Map<BlockPos, BlockState> X = new HashMap<>();
   private final Map<BlockPos, String> G = new HashMap<>();
   private int Yu;
   private int Y0;
   private final Map<BlockPos, Integer> ua = new HashMap<>();
   private static final int YL = 200;
   private final int[] YK = new int[5];
   private final int[] Y2 = new int[5];
   private final Map<BlockPos, Integer> gI = new HashMap<>();
   private int Yr;
   private final Set<BlockPos> YT = new HashSet<>();
   private final Set<Long> gW = new HashSet<>();
   private int d;
   private int b;
   private BlockPos YA;
   private BlockPos F;
   private BlockState VI;
   private final Map<BlockPos, BlockState> g1 = new HashMap<>();
   private boolean uz;
   private BlockPos u_;
   private int uc;
   private Boolean VM;
   private BlockPos VN;
   private final Map<BlockPos, BlockState> uq = new HashMap<>();
   private int S;
   private final Set<BlockPos> gM = new HashSet<>();
   static final int Y6 = 10;
   private List<sj.e> gK = List.of();
   private int Vq;
   private int Ve;
   private int Yv;
   private int r;
   private Vec3 uZ;
   private List<sj.e> uE = List.of();
   private BlockPos uk;
   private double uO;
   private double u9;
   private double gA = 0.245;
   private double Vv = 0.6;
   private final SplittableRandom uQ = new SplittableRandom();
   private BlockPos Vu;
   private int Vc;
   private BlockPos gu;
   static final double u7 = 0.015;
   static final int Yq = 20;
   static final int uf = 50;
   private List<wb.q> VE = List.of();
   private List<wb.q> Yx = List.of();
   private List<wb.q> g0 = List.of();
   private List<wb.q> gB;
   private final Set<wb.q> uL = new HashSet<>();
   private List<wb.q> gQ = List.of();
   private int Vk = -1;
   private int uB = -1000000000;
   private int VQ = -1;
   private int gx = -1000000000;
   private int uV;
   private final Map<Long, Integer> V5 = new HashMap<>();
   private int j = -1;
   private int u0;
   private int Ys;
   private int Vn = -1;
   private int gZ = -1000000000;
   private int Yg = -1000000000;
   private int up;
   private int g8 = -1;
   private int n = -1000000000;
   private boolean Yb;
   private boolean ud;
   private boolean gs;
   private final Set<BlockPos> gS = new HashSet<>();
   private int Z = -1;
   private BlockPos YO;
   private Direction gh;
   private boolean gw;
   private long Y8 = Long.MIN_VALUE;
   private float uA;
   private float Yn;
   static final float uh = 2.5F;
   static final float gJ = -2.0F;
   static final float E = 6.0F;
   static final float gc = 25.0F;
   static final double gy = 0.12;
   static final int V9 = 3;
   static final int Vd = 5;
   private final s6 ga = new s6();
   private final Set<BlockPos> ue = new HashSet<>();
   static final int VG = 60;
   private boolean V8;
   private int uS;
   private int YV;
   private float Yw;
   private float Vo;
   private BlockPos ug;
   private int Y7 = -1000000000;
   private int g5;
   private double u6;
   static final double uX = 0.035;
   static final int YP = 80;
   static final double uw = 6.0;
   private final Set<BlockPos> Vx = new HashSet<>();
   private BlockPos g7;
   private int gi = -1;
   private boolean VS;
   private int ut;
   private Vec3 Yo;
   private double uI;
   static final int ui = 30;
   private Vec3 Vj = Vec3.ZERO;
   private boolean Vi;
   private boolean YY;
   private boolean gY;
   private int un = Integer.MIN_VALUE;
   private int uP = -1;
   private final Set<Item> gz = new HashSet<>();
   private BlockPos Ya;
   private BlockPos u1;
   private boolean Yh;
   private int Yj = -1;
   private int V6;
   static final int YB = 3;
   private final Set<BlockPos> gm = new HashSet<>();
   private boolean Vy;
   private boolean z;
   private boolean ub;
   private boolean Y5;
   private BlockPos y;
   private final boolean[] gP = new boolean[2];
   private Screen gr;
   static final double u5 = 128.0;
   private boolean gD;
   private int Y3;
   private int YI;
   private final Set<Integer> m = new HashSet<>();
   static final int gp = 1200;
   private int uR;
   private int uG;
   private boolean t;
   private final hc gC = new hc(hc.h.QUICK);
   private final hk H = new hk(this);
   private String YC;
   private String VB;
   private String g;
   private int YS = -1;
   private boolean h;
   private boolean ul;
   private final Set<BlockPos> VU = new HashSet<>();
   private int s = -1;
   private boolean gq;
   private u1 gj;
   private int T;
   static final int u4 = 20000;
   private int YN;
   private final Set<BlockPos> VF = new HashSet<>();
   static final int gl = 3;
   private int c = -1;
   private int V2 = -1;
   private int q = -1000000000;
   private long Yz;
   private long o;
   static final int f = 256;
   static final int YE = 200000;
   static final long YH = 15000L;
   private Set<BlockPos> YJ;
   private Set<BlockPos> gb;
   private String VA = "";
   private String V = "";
   private final gu uD = new gu(this, this.gC, var1 -> this.aimAt(var1, 12.0F));
   private int V3 = -1000000000;
   private int u;
   private final List<BlockPos> g4 = new ArrayList<>();
   static final int Y9 = 8;
   static final int e = 12000;
   private int w;
   private int M;
   private long gL;
   private String VT = "";
   static final long Ye = 5000000L;
   static final int uC = 6;
   private wb.q YG;
   private int I = -1;
   private int VJ = -1;
   private int Y4;
   private int u8;
   private long g_ = Long.MIN_VALUE;
   private final Set<Long> u3 = new HashSet<>();
   static final int uJ = 32;
   static final int C = 3;
   private BlockPos uU;
   private int Vt = -1;
   static final int Va = 2;
   private int us;
   private int Y_;
   private String YM;
   private String gV;
   private int uK;
   private String Vb;
   private String gH;
   private int go;
   static final int ur = 16;
   private Set<Long> uT = Set.of();
   private long g3 = Long.MIN_VALUE;
   private int ge = Integer.MIN_VALUE;
   private int gt = -1;
   private int i = -1000000000;
   private int YR;
   private int a = -1;
   static final int V0 = 6;
   static final int x = 8;
   static final int Yf = 16;
   static final int uj = 2;
   static final int gR = 6;
   static final float gn = 1.5F;
   static final int Yd = 5;
   static final int gf = 3;
   private BlockPos u2;
   private BlockPos gU;
   private int Yi;
   private int V1;
   private int gX = -1000000000;
   private int gO;
   private int U;
   private final Map<BlockPos, Integer> uF = new HashMap<>();
   private float gk = 88.0F;
   private String Yk;
   static final double VX = 0.18;
   static final double YQ = Math.cos(Math.toRadians(70.0));
   static final double YF = 0.75;
   private final Set<BlockPos> gG = new HashSet<>();
   private int uY;
   static final int gN = 10;

   private static int G(double var0) {
      return var0 < 2.0 ? 0 : (var0 < 3.0 ? 1 : (var0 < 3.5 ? 2 : (var0 < 4.0 ? 3 : 4)));
   }

   public ae() {
      super("Auto Area Miner", "Clear the box between two corners, top layer first, with your 3x3 drill and a pickaxe");
      P = this;
      this.Yc.P(this.Vs::getValue);
      this.L.P(this.Vr::getValue);
      this.Yc.h("Golden Apple After");
      this.L.h("Mend Tool Below");
      this.gF.h("Pause In Other Menus");
      this.Q.h("Corner 1 (x y z)");
      this.D.h("Corner 2 (x y z)");
      this.Q.o(var1 -> this.o = System.currentTimeMillis() + 15000L);
      this.D.o(var1 -> this.o = System.currentTimeMillis() + 15000L);
      if (!A) {
         A = (boolean)1;
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> {
            ae var1 = P;
            if (var1 != null) {
               var1.P();
            }
         });
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            ae var1 = P;
            if (var1 != null) {
               var1.J();
            }
         });
      }
   }

   public static ae m() {
      return P;
   }

   public static String p(Collection<BlockPos> var0, String var1) {
      ae var2 = P;
      if (var2 == null) {
         return "Auto Area Miner is not available in this edition.";
      }

      if (var2.isEnabled()) {
         return "Auto Area Miner is already on; turn it off first.";
      }

      String var3 = s1.U(var2);
      if (var3 != null) {
         return "Turn off " + var3 + " first; one bot runs at a time.";
      }

      if (var0 != null && !var0.isEmpty()) {
         HashSet var4 = new HashSet(var0.size() * 2);

         for (BlockPos var6 : var0) {
            var4.add(var6.immutable());
         }

         var2.YC = null;
         var2.gb = var4;
         var2.V = var1 == null ? "" : var1;
         var2.enable();
         return null;
      } else {
         return "There is nothing to mine.";
      }
   }

   public static boolean G(String var0) {
      ae var1 = P;
      if (var1 != null && var1.isRuntimeActive() && var1.YJ != null && !var1.gq) {
         com.larpclient.h.A(var1.getName(), "Mine Out stopped: {}", var0);
         var1.K("stopped mining out " + var1.VA + ": " + var0 + ".");
         var1.disable();
         return true;
      } else {
         return false;
      }
   }

   public static boolean J() {
      ae var0 = P;
      return var0 != null && var0.isRuntimeActive() && var0.YJ != null && !var0.gq;
   }

   public static int S() {
      ae var0 = P;
      return var0 != null && var0.YJ != null && !var0.gq ? Math.max(0, var0.V2) : -1;
   }

   @Override
   public void disable() {
      this.YC = null;
      super.disable();
   }

   @Override
   public boolean allowsScreenInteractions() {
      Screen var1 = getMc() == null ? null : mc.gui.screen();
      return var1 == null || var1 instanceof ChatScreen || var1 instanceof PauseScreen || !this.gF.getValue();
   }

   @Override
   public List<s1.a> commands() {
      return List.of(
         new s1.a("Area", "Select the area with a free camera", this::z),
         new s1.a("Area", "Show the area", this::a),
         new s1.a("Area", "Clear corners", this::V),
         new s1.a("Drill", "Use the item in your hand", this::l),
         new s1.a("Drill", "Pick it from your inventory", this::K),
         new s1.a("Drill", "No drill, pickaxe only", this::k)
      );
   }

   @Override
   public String detail() {
      String var1 = this.VV.getValue().trim();
      if (var1.isEmpty()) {
         return "Drill: none, your best pickaxe mines everything.";
      }

      if (!this.isInGame()) {
         return "Drill: any item named like \"" + var1 + "\".";
      }

      int var2 = this.m();
      return var2 >= 0
         ? "Drill: " + mc.player.getInventory().getItem(var2).getHoverName().getString() + " (hotbar slot " + (var2 + 1) + ")."
         : "Drill: \"" + var1 + "\" is not in your hotbar, so the pickaxe mines everything.";
   }

   private String a() {
      u1.y var1 = this.h();
      if (var1 == null) {
         return "Set both corners first.";
      }

      if (!this.isInGame()) {
         return "Join a world first.";
      }

      this.o = System.currentTimeMillis() + 15000L;
      return "Showing " + var1.describe() + " for 15 seconds.";
   }

   private String z() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      String var1 = this.R();
      return var1 != null ? var1 : "Fly around and aim: left click sets corner 1, right click sets corner 2, Esc finishes.";
   }

   private String l() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      ItemStack var1 = mc.player.getMainHandItem();
      return var1.isEmpty() ? "Hold your drill first." : this.v(var1);
   }

   private String K() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.isRuntimeActive()) {
         return "Turn the miner off before changing the drill.";
      }

      this.Y5 = (boolean)1;
      this.gr = mc.gui.screen();
      mc.gui.setScreen(new InventoryScreen(mc.player));
      com.larpclient.h.U(this.getName(), "Picking the drill from the inventory");
      this.K("click your drill in your inventory. Nothing moves; Esc cancels.");
      return "Click your drill in your inventory. Nothing moves; Esc cancels.";
   }

   private String k() {
      this.VV.k("");
      this.K();
      return "No drill: your best pickaxe mines everything.";
   }

   private String v(ItemStack var1) {
      String var2 = var1.getHoverName().getString().trim();
      if (var2.length() > 128) {
         var2 = var2.substring(0, 128);
      }

      this.VV.k(var2);
      this.K();
      com.larpclient.h.A(this.getName(), "Drill set to {}", var2);
      return "Drill set to " + var2 + ".";
   }

   public static boolean G(ItemStack var0) {
      ae var1 = P;
      if (var1 != null && var1.Y5 && var0 != null && !var0.isEmpty()) {
         var1.Y5 = (boolean)0;
         String var2 = var1.v(var0);
         var1.K(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
         Screen var3 = var1.gr;
         var1.gr = null;
         getMc().gui.setScreen(var3);
         return true;
      } else {
         return false;
      }
   }

   public static boolean W() {
      ae var0 = P;
      return var0 != null && var0.Y5;
   }

   private String V() {
      if (this.isRuntimeActive()) {
         return "Turn the miner off before clearing the corners.";
      }

      this.Q.k("");
      this.D.k("");
      this.o = 0L;
      this.K();
      return "Corners cleared.";
   }

   private void K() {
      LarpClient var1 = LarpClient.R();
      if (var1 != null && var1.o() != null) {
         var1.o().C();
      }
   }

   @Override
   public String summary() {
      if (this.YJ != null && this.B != null && this.isRuntimeActive()) {
         return "Mining out " + this.VA + ": " + this.B.J().describe() + ". " + this.u() + ".";
      }

      u1.y var1 = this.h();
      if (var1 == null) {
         return "Set both corners to choose the area.";
      }

      String var2 = "Area: " + var1.describe();
      return this.isRuntimeActive() && this.B != null ? var2 + ". " + this.u() + "." : var2 + ". Stand on top of it and turn the miner on.";
   }

   @Override
   public List<s1.w> settingGroups() {
      return List.of(
         new s1.w("Area", List.of(this.Ym, this.Vp))
            .with(
               new s1.r(
                  this::x,
                  List.of(
                     List.of(
                        this.T("Pick with camera", "Select the area with a free camera"),
                        this.T("Show", "Show the area").availableWhen(() -> this.h() != null),
                        this.T("Clear", "Clear corners").availableWhen(() -> this.h() != null)
                     )
                  )
               )
            ),
         new s1.w("Drill", List.of(this.VV, this.uu))
            .with(
               new s1.r(
                  this::I,
                  List.of(
                     List.of(
                        this.T("Held item", "Use the item in your hand"),
                        this.T("From inventory", "Pick it from your inventory"),
                        this.T("No drill", "No drill, pickaxe only")
                     )
                  )
               )
            ),
         new s1.w("While mining", List.of(this.g9, this.R, this.g6, this.VD, this.p)),
         new s1.w("Food and repair", List.of(this.Vs, this.Yc, this.Vr, this.L)),
         new s1.w("Display", List.of(this.uN, this.uH, this.YU, this.O)),
         s1.w.advanced("Advanced", List.of(this.Q, this.D, this.uM, this.VZ, this.gg, this.J, this.gF))
      );
   }

   private String x() {
      u1.y var1 = this.h();
      return var1 == null ? "Not set. Pick two opposite corners." : var1.describe() + (Q(var1) != null ? ", too wide to mine" : "");
   }

   private String I() {
      if (!this.isInGame()) {
         return this.detail();
      }

      int var1 = this.H();
      return this.detail()
         + (var1 >= 0 ? " Pickaxe: " + mc.player.getInventory().getItem(var1).getHoverName().getString() + "." : " No pickaxe in your hotbar.");
   }

   @Override
   public s1.o status() {
      if (this.isRuntimeActive()) {
         return new s1.o(this.B == null ? "Starting." : (this.YJ != null ? "Mining out " + this.VA + ". " : "") + this.u() + ".", false);
      }

      u1.y var1 = this.h();
      if (var1 == null) {
         return new s1.o("Set the area, then turn the module on.", false);
      }

      if (Q(var1) != null) {
         return new s1.o("Each side across can be at most 256 blocks. Pick the corners again.", true);
      }

      if (!this.isInGame()) {
         return new s1.o("Area set. Join the world it is in.", false);
      }

      String var2 = s1.U(this);
      if (var2 != null) {
         return new s1.o(var2 + " is running. One bot runs at a time.", true);
      }

      BlockPos var3 = this.p();
      int var4 = Math.max(Math.max(var1.minX() - var3.getX(), var3.getX() - var1.maxX()), Math.max(var1.minZ() - var3.getZ(), var3.getZ() - var1.maxZ()));
      if (var4 > 16) {
         return new s1.o("You are " + var4 + " blocks from the area. Stand on it or next to it.", true);
      }

      if (var3.getY() < var1.minY()) {
         return new s1.o("You are below the area. Stand on top of it or inside it.", true);
      }

      int var5 = this.H();
      int var6 = this.m();
      if (var5 < 0 && var6 < 0) {
         return new s1.o("Put a pickaxe or your drill in your hotbar.", true);
      }

      int var7 = (int)Math.ceil(var1.sizeY() / this.uM.getValue());
      ArrayList var8 = new ArrayList();

      for (int var12 : new int[]{var6, var5}) {
         if (var12 >= 0) {
            var8.add(mc.player.getInventory().getItem(var12).getHoverName().getString());
         }
      }

      StringBuilder var13 = new StringBuilder(
         "Will clear " + var1.describe() + " in " + var7 + (var7 == 1 ? " layer" : " layers") + " with " + String.join(" and ", var8) + "."
      );
      if (this.R.getValue() && this.d() < 0) {
         var13.append(" No blocks in your hotbar for bridging or plugging lava.");
      }

      if (this.Vs.getValue() && hk.J(var0 -> var0.get(DataComponents.FOOD) != null) < 0) {
         var13.append(" No food in your hotbar.");
      }

      return new s1.o(var13.toString(), false);
   }

   private u1.y h() {
      int[] var1 = com.larpclient.u1.K(this.Q.getValue());
      int[] var2 = com.larpclient.u1.K(this.D.getValue());
      return var1 != null && var2 != null ? u1.y.of(var1, var2) : null;
   }

   private String R() {
      if (this.isRuntimeActive()) {
         return "Turn the miner off before choosing a new area.";
      }

      if (this.Vy) {
         return null;
      }

      this.Vy = (boolean)1;
      this.gP[0] = this.gP[1] = (boolean)0;
      this.y = null;
      mc.gui.setScreen(null);
      FreecamModule var1 = this.t();
      if (var1 != null && (var1.isEnabled() || !var1.D())) {
         var1.u(this.Ym.getValue());
         if (!var1.isEnabled()) {
            var1.setEnabled(true);
            this.z = var1.isEnabled();
         }

         if (!var1.isEnabled()) {
            var1.F();
         }
      }

      int var2 = var1 != null && var1.isEnabled() ? 1 : 0;
      com.larpclient.h.A(this.getName(), "Picking corners{}", var2 != 0 ? " with Freecam at " + var1.V() + " blocks/s" : " from your own view");
      if (var2 != 0) {
         this.K("fly with the movement keys at " + this.Ym.getValue().intValue() + " blocks a second; the mouse wheel changes it. Esc finishes.");
      }

      return null;
   }

   private FreecamModule t() {
      return LarpClient.R().z().p(FreecamModule.class);
   }

   static String Q(u1.y var0) {
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

   private void S(String var1) {
      if (this.Vy) {
         this.Vy = (boolean)0;
         this.y = null;
         FreecamModule var2 = this.t();
         if (var2 != null) {
            var2.F();
            if (this.z && var2.isEnabled()) {
               var2.setEnabled(false);
            }
         }

         this.z = (boolean)0;
         this.K();
         u1.y var3 = this.h();
         if (var3 != null) {
            this.o = System.currentTimeMillis() + 15000L;
         }

         com.larpclient.h.A(
            this.getName(),
            "Corner picking ended ({}); area {}, picked corners {}{}",
            var1,
            var3 == null ? "incomplete" : var3.describe(),
            this.gP[0] ? "1" : "",
            this.gP[1] ? "2" : ""
         );
         if (this.isInGame()) {
            if (var3 == null) {
               this.K("corner picking ended; set both corners to choose the area.");
            } else if (Q(var3) != null) {
               int var4 = this.gP[0] && !this.gP[1] ? 2 : (this.gP[1] && !this.gP[0] ? 1 : 0);
               this.K(
                  var4 > 0
                     ? "corner "
                        + var4
                        + " is still "
                        + (var4 == 1 ? this.Q : this.D).getValue()
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
               this.K("area set: " + var3.describe() + ". Stand on top of it and turn the miner on.");
            }
         }
      }
   }

   public static boolean d() {
      ae var0 = P;
      return var0 != null && var0.Vy;
   }

   public static void T(int var0) {
      ae var1 = P;
      if (var1 != null && var1.Vy && var1.isInGame()) {
         BlockPos var2 = var1.d();
         if (var2 == null) {
            var1.K("aim at a block to set a corner.");
         } else {
            com.larpclient.module.g var3 = var0 == 0 ? var1.Q : var1.D;
            var3.k(com.larpclient.u1.j(var2.getX(), var2.getY(), var2.getZ()));
            var1.gP[var0 == 0 ? 0 : 1] = (boolean)1;
            com.larpclient.h.U(var1.getName(), "Corner {} picked at {}", var0 + 1, var2.toShortString());
         }
      }
   }

   private BlockPos d() {
      boolean var1 = p_.V();
      Vec3 var2 = var1 ? p_.S(1.0F) : mc.player.getEyePosition();
      float var3 = var1 ? p_.V() : mc.player.getYRot();
      float var4 = var1 ? p_.s() : mc.player.getXRot();
      Vec3 var5 = var2.add(Vec3.directionFromRotation(var4, var3).scale(128.0));
      BlockHitResult var6 = mc.level.clip(new ClipContext(var2, var5, Block.OUTLINE, Fluid.NONE, mc.player));
      return var6.getType() == Type.BLOCK ? var6.getBlockPos().immutable() : null;
   }

   private void J() {
      if (!this.isInGame()) {
         if (this.Vy) {
            this.Vy = (boolean)0;
            this.z = (boolean)0;
            FreecamModule var3 = this.t();
            if (var3 != null) {
               var3.F();
            }
         }

         this.Y5 = (boolean)0;
      } else {
         if (this.Y5 && !(mc.gui.screen() instanceof InventoryScreen)) {
            this.Y5 = (boolean)0;
            this.gr = null;
         }

         int var1 = this.Vp.getValue() != -1 && mc.gui.screen() == null && mc.isWindowActive() && bc.I(mc.getWindow().handle(), this.Vp.getValue()) ? 1 : 0;
         if (var1 != 0 && !this.ub) {
            if (this.Vy) {
               this.S("key pressed");
            } else {
               String var2 = this.R();
               if (var2 != null) {
                  this.K(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
               }
            }
         }

         this.ub = (boolean)var1;
         if (this.Vy) {
            if (mc.gui.screen() != null) {
               this.S("menu opened");
            } else {
               this.y = this.d();
            }
         }
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.u();
      if (this.Vy) {
         this.S("miner turned on");
      }

      if (this.YC != null) {
         String var20 = this.YC;
         this.YC = null;
         this.T("it stopped earlier because " + var20 + ". Turn it on again to carry on");
      } else if (this.gb != null) {
         Set var19 = this.gb;
         this.gb = null;
         this.X(var19, this.V);
      } else {
         u1.y var1 = this.h();
         if (var1 == null) {
            this.T("set both corners first");
         } else {
            String var2 = Q(var1);
            if (var2 != null) {
               this.T(var2);
            } else {
               String var3 = s1.U(this);
               if (var3 != null) {
                  this.T("turn off " + var3 + " first; one bot runs at a time");
               } else {
                  BlockPos var4 = this.p();
                  double var5 = Math.max(
                     Math.max(var1.minX() - var4.getX(), var4.getX() - var1.maxX()), Math.max(var1.minZ() - var4.getZ(), var4.getZ() - var1.maxZ())
                  );
                  if (var5 > 16.0) {
                     this.T("stand inside or next to the area to start (you are " + (int)var5 + " blocks away)");
                  } else if (var4.getY() < var1.minY()) {
                     this.T("you are below the area; stand on top of it or inside it");
                  } else {
                     int var7 = this.H();
                     int var8 = var7 < 0 && this.m() >= 0 ? 1 : 0;
                     if (var7 < 0 && var8 == 0) {
                        this.T("put a pickaxe or your drill in your hotbar");
                     } else {
                        ItemStack var9 = mc.player.getInventory().getItem(var8 != 0 ? this.m() : var7);
                        this.B = new u1(var1, this.uM.getValue().intValue(), this.VZ.getValue().intValue(), var4.getX(), var4.getZ());
                        this.ux = mc.level;
                        this.YZ = mc.player;
                        this.H.k();
                        this.g2 = (boolean)(this.m() < 0 ? 1 : 0);
                        int var10 = var4.getY() > var1.maxY() ? 0 : Math.max(0, this.B.k(var4.getY()));
                        int var11 = var10 <= 0 || (!this.y().equals(this.g) || var10 < this.YS) && !this.w(var4) ? 0 : 1;
                        int var12 = var11 != 0 ? var10 : this.V();
                        if (var12 == -1) {
                           com.larpclient.h.A(this.getName(), "Nothing to mine in {}", var1.describe());
                           this.K("there is nothing to mine in " + var1.describe() + ": every block in it is gone already.");
                           this.h = (boolean)1;
                           this.disable();
                        } else {
                           int var13 = var12 >= 0 && var12 < var10 ? 1 : 0;
                           int var14 = var13 != 0 ? this.E(this.B.J(var10).top()) : 0;
                           if (var13 == 0 || this.R.getValue() && this.d() >= 0) {
                              if (var13 != 0 && var5 > 2.0) {
                                 this.T(
                                    "the layers above you still have "
                                       + var14
                                       + " blocks to mine. Stand on top of the area, or right next to it (you are "
                                       + (int)var5
                                       + " blocks away) so the miner can pillar up beside it"
                                 );
                              } else {
                                 this.Yt = var13 != 0 ? var12 : var10;
                                 this.b = this.Yt;
                                 this.Yz = System.currentTimeMillis();
                                 this.c = this.V2 = this.a();
                                 this.q = mc.player.tickCount;
                                 this.o = 0L;
                                 StringBuilder var15 = new StringBuilder(var9.getHoverName().getString());
                                 int var16 = this.m();
                                 if (var16 >= 0 && var8 == 0) {
                                    var15.insert(0, mc.player.getInventory().getItem(var16).getHoverName().getString() + " and ");
                                 }

                                 String var17 = var13 != 0
                                    ? ", pillaring up to layer " + (this.Yt + 1) + " first: the layers above you still have " + var14 + " blocks"
                                    : (
                                       this.Yt > 0
                                          ? ", starting at layer "
                                             + (this.Yt + 1)
                                             + " where you stand (the "
                                             + this.E(this.B.J(this.Yt).top())
                                             + " blocks in the layers above you are mined at the end)"
                                          : ""
                                    );
                                 this.K("mining " + var1.describe() + " in " + this.B.k() + " layers with " + var15 + var17 + ".");
                                 ArrayList var18 = new ArrayList();
                                 if (var16 < 0) {
                                    var18.add("no hotbar item matches Drill Name \"" + this.VV.getValue() + "\", so the pickaxe mines everything");
                                 }

                                 if (var8 != 0) {
                                    var18.add(
                                       "your drill is your only pickaxe, so it only cuts where its 3x3 stays inside the area; add a plain pickaxe to the hotbar to clear the edges too"
                                    );
                                 }

                                 if (this.R.getValue() && this.d() < 0) {
                                    var18.add("no solid blocks in the hotbar to pillar, bridge or plug lava with");
                                 }

                                 if (this.Vs.getValue() && hk.J(var0 -> var0.get(DataComponents.FOOD) != null) < 0) {
                                    var18.add("no food in the hotbar");
                                 }

                                 if (this.Vr.getValue()
                                    && (hk.x(var9) || var16 >= 0 && hk.x(mc.player.getInventory().getItem(var16)))
                                    && hk.J(var0 -> var0.is(Items.EXPERIENCE_BOTTLE)) < 0) {
                                    var18.add("no XP bottles in the hotbar to mend with");
                                 }

                                 if (!var18.isEmpty()) {
                                    this.K("heads up: " + String.join("; ", var18) + ".");
                                 }

                                 com.larpclient.h.A(
                                    this.getName(),
                                    "Started: {} from {}, {} layers from layer {}, rows along {}, pass width {}, drill slot {}, {} solid blocks{}",
                                    var1.describe(),
                                    var4.toShortString(),
                                    this.B.k(),
                                    this.Yt + 1,
                                    this.B.J() ? "X" : "Z",
                                    this.B.P(),
                                    var16,
                                    this.c,
                                    var13 != 0 ? ", pillaring up from layer " + (var10 + 1) : (var11 != 0 ? ", carrying on where you stand" : "")
                                 );
                                 this.ul = (boolean)1;
                              }
                           } else {
                              this.T(
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
      if (this.isEnabled() && this.B != null) {
         this.YC = "the world, the player or the connection changed";
         com.larpclient.h.A(this.getName(), "Interrupted after {} blocks: {}", this.d, this.YC);
      }

      if (this.B != null && this.YJ == null && this.ul) {
         this.g = this.h ? null : this.y();
         this.YS = this.h ? -1 : this.Yt;
      }

      super.onDisable();
      t1.v(this);
      this.H.F();
      this.u();
   }

   private void u() {
      this.B = null;
      this.ux = this.YZ = null;
      this.Yt = 0;
      this.g2 = this.gd = (boolean)0;
      this.um = ae.g.i;
      this.Yy.clear();
      this.uv.clear();
      this.gE = -1000000000;
      this.Y = 0;
      this.uW.clear();
      this.Yp.clear();
      this.Vh.clear();
      this.Y1.clear();
      this.X.clear();
      this.G.clear();
      this.ua.clear();
      this.YT.clear();
      this.gW.clear();
      this.ga.k();
      this.gw = (boolean)0;
      this.Y8 = Long.MIN_VALUE;
      this.d = 0;
      this.YA = this.F = null;
      this.VI = null;
      this.g1.clear();
      this.uz = (boolean)0;
      this.m();
      this.uZ = null;
      this.uE = List.of();
      this.uk = null;
      this.ut = 0;
      this.Yo = null;
      this.Vu = this.gu = this.g7 = null;
      this.Vc = 0;
      this.Vx.clear();
      this.gi = -1;
      this.VS = (boolean)0;
      this.Vj = Vec3.ZERO;
      this.Vi = this.YY = this.gY = (boolean)0;
      this.un = Integer.MIN_VALUE;
      this.uP = -1;
      this.gz.clear();
      this.Ya = this.u1 = null;
      this.uG = 0;
      this.t = (boolean)0;
      this.gC.x();
      this.gD = (boolean)0;
      this.Y3 = this.YI = 0;
      this.m.clear();
      this.u_ = null;
      this.uc = 0;
      this.b = 0;
      this.VM = null;
      this.VN = null;
      this.uq.clear();
      this.S = 0;
      this.gM.clear();
      this.Yh = (boolean)0;
      this.gm.clear();
      this.Yj = -1;
      this.V6 = 0;
      this.c = this.V2 = -1;
      this.q = -1000000000;
      this.Yz = System.currentTimeMillis();
      this.h = this.ul = (boolean)0;
      this.VU.clear();
      this.s = -1;
      this.gq = (boolean)0;
      this.gj = null;
      this.T = 0;
      this.YN = 0;
      this.VF.clear();
      this.YJ = null;
      this.VA = "";
      this.uD.H();
      this.V3 = -1000000000;
      this.u = 0;
      this.g4.clear();
      this.VE = this.Yx = this.g0 = this.gQ = List.of();
      this.gB = null;
      this.uL.clear();
      this.V5.clear();
      this.j = -1;
      this.u0 = this.Ys = this.up = 0;
      this.Vn = this.g8 = -1;
      this.gZ = this.Yg = this.n = -1000000000;
      this.Yb = this.ud = this.V8 = this.gs = (boolean)0;
      this.gS.clear();
      this.Z = -1;
      this.YO = this.ug = null;
      this.gh = null;
      this.ue.clear();
      this.Y7 = -1000000000;
      this.uS = this.YV = 0;
      this.u2 = this.gU = null;
      this.Yi = this.V1 = this.gO = this.U = 0;
      this.gX = -1000000000;
      this.uF.clear();
      this.gk = 88.0F;
   }

   private void m() {
      this.gK = List.of();
      this.Vq = 0;
      this.Yv = 0;
      this.r = 0;
   }

   @Override
   public void onTick() {
      long var1 = System.nanoTime();

      try {
         this.B();
      } finally {
         this.S(System.nanoTime() - var1);
      }
   }

   private void S(long var1) {
      if (mc.player != null) {
         if (var1 > 5000000L) {
            this.w++;
            if (var1 > this.gL) {
               this.gL = var1;
               this.VT = this.getStatus();
            }
         }

         int var3 = mc.player.tickCount;
         if (var3 - this.M >= 200) {
            this.M = var3;
            this.v();
            if (this.w != 0) {
               com.larpclient.h.A(
                  this.getName(),
                  "Miner ticks over 5 ms in the last 10 s: {}, longest {} ms while: {}",
                  this.w,
                  String.format(Locale.ROOT, "%.1f", this.gL / 1000000.0),
                  this.VT
               );
               this.w = 0;
               this.gL = 0L;
            }
         }
      }
   }

   private void B() {
      this.Vj = Vec3.ZERO;
      this.Vi = this.YY = this.gY = this.V8 = (boolean)0;
      this.go = 0;
      if (this.B != null) {
         if (mc.level == this.ux && mc.player == this.YZ) {
            this.un = mc.player.tickCount;
            String var1 = hk.j();
            if (var1 != null) {
               l0.h(this);
               t1.v(this);
               this.H.B();
               this.Vi = (boolean)1;
               this.e(var1 + ": swimming up");
            } else if (this.Vs.getValue() && this.H.r(this.Yc.getValue().intValue())) {
               l0.h(this);
               t1.v(this);
               this.e("Eating");
            } else if (this.Ya != null) {
               this.p();
            } else {
               String var2 = this.e();
               if (var2 != null) {
                  this.y();
                  this.e(var2);
                  if (!var2.equals(this.VB)) {
                     this.VB = var2;
                     com.larpclient.h.A(this.getName(), "Held: {}", var2);
                  }
               } else {
                  this.VB = null;
                  if (this.B()) {
                     this.y();
                     this.gC.x();
                     this.e("Paused for manual movement");
                  } else if (mc.gui.screen() instanceof AbstractContainerScreen) {
                     this.y();
                     this.e("Holding still while an inventory menu is open");
                  } else if (!this.Vr.getValue() || !this.O()) {
                     if (!this.a()) {
                        l0.h(this);
                        this.Vj = Vec3.ZERO;
                        this.e("Waiting for the area to load");
                     } else if (!this.D()) {
                        if (this.YJ != null) {
                           this.C();
                        } else if (!mc.player.onGround()) {
                           l0.h(this);
                           this.e("Waiting to land");
                        } else {
                           this.T();
                           this.X();
                           this.c();
                           if (this.c >= 0 && mc.player.tickCount - this.q >= 100) {
                              this.q = mc.player.tickCount;
                              this.V2 = this.a();
                           }

                           if (this.gD) {
                              this.h();
                           } else {
                              u1.b var3 = this.B.J(this.Yt);
                              int var4 = this.s();
                              if (var4 >= var3.bottom()) {
                                 this.M(var3);
                              }

                              if (var4 > var3.bottom()) {
                                 this.um = ae.g.l;
                                 this.T(var3);
                              } else if (var4 < var3.bottom()) {
                                 this.um = ae.g.G;
                                 this.v(var3);
                              } else {
                                 this.um = ae.g.i;
                                 this.e(var3);
                              }
                           }
                        }
                     }
                  }
               }
            }
         } else {
            this.T("the world or player changed");
         }
      }
   }

   private void y() {
      l0.h(this);
      t1.v(this);
      this.Vj = Vec3.ZERO;
   }

   private boolean B() {
      return mc.isWindowActive()
         && mc.gui.screen() == null
         && (
            c5.J(mc.options.keyUp)
               || c5.J(mc.options.keyDown)
               || c5.J(mc.options.keyLeft)
               || c5.J(mc.options.keyRight)
               || c5.J(mc.options.keyJump)
               || c5.J(mc.options.keyShift)
         );
   }

   private boolean O() {
      int var1 = this.L.getValue().intValue();

      for (int var5 : new int[]{this.uP, this.m()}) {
         if (var5 >= 0 && !this.A(var5)) {
            ItemStack var6 = mc.player.getInventory().getItem(var5);
            if (hk.T(var6) <= var1 || this.H.P()) {
               l0.h(this);
               hk.h var7 = this.H.w(var5, var1);
               if (var7 == hk.h.E) {
                  this.e("Mending the tool with XP bottles");
                  return true;
               }

               if (var7 == hk.h.f) {
                  this.T(
                     "your "
                        + var6.getHoverName().getString()
                        + " has "
                        + hk.T(var6)
                        + " uses left and cannot be mended (it needs Mending and XP bottles in the hotbar)"
                  );
                  return true;
               }
            }
         }
      }

      return false;
   }

   private void e(u1.b var1) {
      if (!this.gd) {
         this.gd = (boolean)1;
         BlockPos var2 = this.p();
         if (this.B.J().containsColumn(var2.getX(), var2.getZ())) {
            this.gW.add(this.p(var2.getX(), var2.getZ()));
         }

         com.larpclient.h.A(this.getName(), "Layer {} of {}: Y {} to {}", this.Yt + 1, this.B.k(), var1.bottom(), var1.top());
      }

      if (mc.player.tickCount - this.gE >= 40) {
         this.B(var1);
      }

      if (!this.R.getValue() || !this.z(var1)) {
         if (!this.V(var1)) {
            BlockPos var7 = this.n(var1);
            if (var7 != null) {
               this.m();
               if (this.uz) {
                  this.A(var7, true);
               } else {
                  this.P(var7, null);
               }
            } else {
               l0.h(this);
               BlockPos var3 = this.Yb ? this.L(var1) : this.A(var1);
               if (var3 == null) {
                  if (mc.player.tickCount - this.gE > 2) {
                     this.B(var1);
                  } else {
                     byte var4 = 0;
                     if (this.d != this.s) {
                        for (BlockPos var6 : this.Yy) {
                           if (this.uW.containsKey(var6) && !this.YT.contains(var6) && l0.B(var6)) {
                              this.uW.remove(var6);
                              var4 = 1;
                           }
                        }
                     }

                     if (var4 != 0) {
                        this.s = this.d;
                        com.larpclient.h.U(this.getName(), "Retrying blocks set aside in layer {}", this.Yt + 1);
                        this.m();
                     } else {
                        this.b(var1);
                     }
                  }
               } else {
                  this.x(var3, var1);
               }
            }
         }
      }
   }

   private void M(u1.b var1) {
      if (this.j != this.Yt) {
         this.j = this.Yt;
         this.u0 = this.Ys = this.up = 0;
         this.Vn = this.g8 = -1;
         this.gZ = this.Yg = this.n = -1000000000;
         this.Yb = this.ud = this.gs = (boolean)0;
         this.gS.clear();
         this.Z = -1;
         this.V5.clear();
         this.YG = null;
         this.I = this.VJ = -1;
         this.g_ = Long.MIN_VALUE;
         this.u3.clear();
         this.Y4 = 0;
         this.u8 = 0;
         this.uU = null;
         this.Vt = -1;
         this.uL.clear();
         this.gQ = List.of();
         this.Vk = -1;
         this.uB = -1000000000;
         this.gB = null;
         this.VQ = -1;
         this.gx = -1000000000;
         this.uV = 0;
         if (this.YJ != null) {
            this.VE = this.Yx = this.g0 = List.of();
            this.Yb = (boolean)1;
         } else {
            int var2 = this.s();
            this.VE = this.Yx = wb.Q(this.B, this.Yt, var2);
            this.g0 = wb.Y(this.B, this.Yt, var1.bottom(), true);
            this.f();
            long var3 = this.VE.stream().filter(var0 -> var0.kind() == wb.r.h).count();
            String var5 = var3 > 0L
               ? "a " + var3 + " block stair down from the corner"
               : (var2 > var1.bottom() ? "the first lane is too short for a stair, so it digs down at the corner" : "standing on the layer's floor");
            com.larpclient.h.A(
               this.getName(),
               "Layer {} dig order: {} stands, {} lanes {} wide along {}, {}",
               this.Yt + 1,
               this.VE.size(),
               this.B.s(),
               this.B.P(),
               this.B.J() ? "X" : "Z",
               var5
            );
         }
      }
   }

   private void f() {
      this.V5.clear();
      int var1 = 0;

      for (wb.q var3 : this.VE) {
         for (wb.w var5 : var3.cells()) {
            this.V5.putIfAbsent(var5.key(), var1++);
         }
      }
   }

   private boolean K() {
      return this.B != null && this.YJ == null && !this.Yb && this.j == this.Yt;
   }

   private boolean V(u1.b var1) {
      if (!this.K()) {
         return false;
      }

      int var2 = mc.player.tickCount;
      if (this.d != this.Z) {
         this.gS.clear();
         this.Z = this.d;
      }

      if (var2 < this.uS && !this.X()) {
         l0.h(this);
         this.e("Waiting for the drill's cut to land");
         return true;
      }

      if (this.P()) {
         return true;
      }

      if (var2 < this.YV) {
         this.x();
         return true;
      }

      while (this.u0 < this.VE.size() && this.p(this.VE.get(this.u0), var1)) {
         this.q(false);
      }

      if (this.u0 >= this.VE.size() && !this.r(var1, false)) {
         this.Yb = (boolean)1;
         this.gS.clear();
         int var7 = 0;

         for (BlockPos var10 : this.Yy) {
            if (this.L(var10, var1)) {
               var7++;
            }
         }

         com.larpclient.h.A(
            this.getName(),
            "Layer {}: dig order done after {} of {} stands{}{}{}",
            this.Yt + 1,
            this.Ys,
            this.VE.size(),
            this.uV > 0 ? ", moved to the nearest face " + this.uV + " times" : "",
            var7 > 0 ? ", " + var7 + " blocks left for the clean-up" : "",
            this.u8 > 0 ? ", " + this.u8 + " passed over with no way to dig to them" : ""
         );
         return false;
      } else {
         BlockPos var3 = this.p();
         if (var3.getX() != this.VE.get(this.u0).standX() || var3.getZ() != this.VE.get(this.u0).standZ()) {
            this.R(var1);
         }

         wb.q var4 = this.VE.get(this.u0);
         if (var3.getX() == var4.standX() && var3.getZ() == var4.standZ()) {
            this.m();
            this.V8 = com.larpclient.gu.N(var4.standX(), var1.bottom(), var4.standZ());
            if (this.A() && this.z(var4, var1)) {
               this.gs = (boolean)1;
               if (this.x(var4) && l0.B(new BlockPos(var4.standX() + var4.dx(), var1.bottom(), var4.standZ() + var4.dz()))) {
                  this.Vj = new Vec3(var4.dx(), 0.0, var4.dz());
               }

               return true;
            } else {
               BlockPos var9 = this.P(var4, var1, true);
               if (var9 != null) {
                  this.gs = (boolean)1;
                  this.L(var9, (String)null);
                  return true;
               } else {
                  BlockPos var6 = this.k(var4, var1);
                  if (var6 != null) {
                     this.gs = (boolean)1;
                     this.L(var6, (String)null);
                     return true;
                  } else {
                     this.q(true);
                     return true;
                  }
               }
            }
         } else {
            if (this.t(var4, var3, var1)) {
               return true;
            }

            if (this.N(var4, var1)) {
               return true;
            }

            BlockPos var5 = this.P(var4, var1, false);
            if (var5 != null) {
               this.m();
               this.L(var5, (String)null);
               return true;
            }

            this.s(var4, var1);
            if (this.C(var4, var1)) {
               return true;
            }

            this.z(var4, var1);
            this.q(false);
            return ++this.go % 16 != 0 ? this.V(var1) : true;
         }
      }
   }

   private boolean t(wb.q var1, BlockPos var2, u1.b var3) {
      if (var1.kind() == wb.r.J && var1.drill() && this.A() && this.x(var1) && var2.getY() == var3.bottom()) {
         int var4 = (var1.standX() - var2.getX()) * var1.dx() + (var1.standZ() - var2.getZ()) * var1.dz();
         int var5 = var1.dx() != 0 ? (var2.getZ() == var1.standZ() ? 1 : 0) : (var2.getX() == var1.standX() ? 1 : 0);
         if (var5 != 0 && var4 >= 1 && var4 <= 3) {
            for (int var6 = 1; var6 <= var4; var6++) {
               BlockPos var7 = var2.offset(var1.dx() * var6, 0, var1.dz() * var6);
               if (!this.Z(var7) || !this.Z(var7.above()) || !this.M(var7.below()) || com.larpclient.gu.N(var7.getX(), var7.getY(), var7.getZ())) {
                  return false;
               }
            }

            wb.w var8 = var1.drillCentre();
            BlockPos var9 = new BlockPos(var8.x(), var8.y(), var8.z());
            if (!l0.B(var9.below())) {
               return false;
            }

            if (this.B(var9) && this.z(var1, var3)) {
               this.gs = (boolean)1;
            }

            this.m();
            this.Vj = new Vec3(var1.dx(), 0.0, var1.dz());
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean x(wb.q var1) {
      return Math.abs(Mth.wrapDegrees(mc.player.getYRot() - e(var1).toYRot())) <= 25.0F;
   }

   private void R(wb.q var1) {
      long var2 = var1.dx() + 2L << 40 ^ var1.dz() + 2L << 36 ^ (var1.dx() != 0 ? var1.standZ() : var1.standX()) & 4294967295L;
      if (var2 != this.Y8) {
         this.Y8 = var2;
         this.uA = (float)((this.uQ.nextDouble() * 2.0 - 1.0) * 2.5);
         this.Yn = -2.0F + (float)this.uQ.nextDouble() * 8.0F;
      }
   }

   static Vec3 w(Vec3 var0, BlockPos var1, Direction var2, float var3, float var4) {
      int var5 = var2.getAxis() == Axis.X ? 1 : 0;
      double var6 = var5 != 0 ? var1.getX() + (var2 == Direction.EAST ? 1 : 0) : var1.getZ() + (var2 == Direction.SOUTH ? 1 : 0);
      double var8 = Math.abs(var6 - (var5 != 0 ? var0.x : var0.z));
      int var10 = var5 != 0 ? var1.getZ() : var1.getX();
      double var11 = Mth.clamp((var5 != 0 ? var0.z : var0.x) + Math.tan(Math.toRadians(var3)) * var8, var10 + 0.12, var10 + 1 - 0.12);
      double var13 = Mth.clamp(var0.y - Math.tan(Math.toRadians(var4)) * var8, var1.getY() + 0.12, var1.getY() + 1 - 0.12);
      return var5 != 0 ? new Vec3(var6, var13, var11) : new Vec3(var11, var13, var6);
   }

   private boolean p(wb.q var1, u1.b var2) {
      return this.uL.contains(var1) || this.R(var1, var2);
   }

   private void R(u1.b var1) {
      wb.q var2 = this.VE.get(this.u0);
      BlockPos var3 = this.p();
      int var4 = mc.player.tickCount;
      int var5 = this.u0 == this.VQ && this.VE == this.gB ? 0 : 1;
      boolean var6 = this.l(var1.bottom()).contains(sj.F(var2.standX(), var2.standZ()));
      if (var5 != 0 || !var6 && var4 - this.gx >= 40) {
         this.gx = var4;
         if (!var6 || Math.abs(var2.standX() - var3.getX()) + Math.abs(var2.standZ() - var3.getZ()) > 6) {
            this.r(var1, var5 == 0);
         }

         this.VQ = this.u0;
         this.gB = this.VE;
      }
   }

   private List<wb.q> C(u1.b var1) {
      int var2 = mc.player.tickCount;
      if (this.Vk == this.d && var2 - this.uB < 40) {
         this.gQ.removeIf(this.uL::contains);
      } else {
         this.Vk = this.d;
         this.uB = var2;
         ArrayList var3 = new ArrayList();

         for (wb.q var5 : this.Yx) {
            if (!this.p(var5, var1)) {
               var3.add(var5);
            }
         }

         for (wb.q var7 : this.g0) {
            if (!this.p(var7, var1)) {
               var3.add(var7);
            }
         }

         this.gQ = var3;
      }

      return this.gQ;
   }

   private boolean r(u1.b var1, boolean var2) {
      BlockPos var3 = this.p();
      Set var4 = this.l(var1.bottom());
      wb.q var5 = wb.v(this.C(var1), var4, var3.getX(), var3.getZ());
      if (var5 == null) {
         return false;
      }

      boolean var6 = var4.contains(sj.F(var5.standX(), var5.standZ()));
      if (var2 && !var6) {
         return false;
      }

      if (this.u0 < this.VE.size() && this.VE.get(this.u0).equals(var5)) {
         return true;
      }

      int var7 = this.VE.indexOf(var5);
      if (var7 < 0) {
         this.VE = this.VE == this.g0 ? this.Yx : this.g0;
         var7 = this.VE.indexOf(var5);
         this.f();
      }

      this.u0 = var7;
      this.Vn = -1;
      this.I = -1;
      this.gS.clear();
      if (this.uV++ < 6) {
         com.larpclient.h.A(
            this.getName(),
            "Layer {}: going to the nearest face instead, stand {} of {} ({}) at {}, {}, {}, lanes {}",
            this.Yt + 1,
            this.u0 + 1,
            this.VE.size(),
            var5.kind(),
            var5.standX(),
            var5.standZ(),
            var6 ? "on the floor" : "off the floor",
            this.VE == this.g0 ? "reversed" : "as planned"
         );
      }

      return true;
   }

   private wb.q B(wb.q var1, u1.b var2) {
      if (this.I != this.u0) {
         this.u3.clear();
      }

      if (this.I == this.u0 && this.VJ == this.d) {
         return this.YG;
      }

      this.I = this.u0;
      this.VJ = this.d;
      this.YG = null;
      int var3 = var2.bottom();

      for (wb.a var5 : wb.v(this.l(var3), var1.standX(), var1.standZ(), 32)) {
         int var6 = var5.x() + var5.dx();
         int var7 = var5.z() + var5.dz();
         if (!this.u3.contains(sj.F(var6, var7)) && this.M(new BlockPos(var6, var3 - 1, var7))) {
            BlockPos var8 = new BlockPos(var6, var3 + 1, var7);
            BlockPos var9 = new BlockPos(var6, var3, var7);
            boolean var10 = this.Z(var8);
            boolean var11 = this.Z(var9);
            if ((!var10 || !var11) && (var10 || this.L(var8, var2)) && (var11 || this.L(var9, var2))) {
               ArrayList var12 = new ArrayList(2);
               if (!var10) {
                  var12.add(new wb.w(var6, var3 + 1, var7));
               }

               if (!var11) {
                  var12.add(new wb.w(var6, var3, var7));
               }

               this.YG = new wb.q(wb.r.d, var5.x(), var3, var5.z(), var5.dx(), var5.dz(), var12, false);
               break;
            }
         }
      }

      return this.YG;
   }

   private boolean C(wb.q var1, u1.b var2) {
      wb.q var3 = this.B(var1, var2);
      if (var3 == null) {
         return false;
      }

      BlockPos var4 = this.p();
      wb.w var5 = var3.cells().getFirst();
      long var6 = sj.F(var5.x(), var5.z());
      if (var4.getX() == var3.standX() && var4.getZ() == var3.standZ()) {
         this.m();
         if (this.A() && this.m(var3, var2)) {
            this.gs = (boolean)1;
            return true;
         } else {
            BlockPos var13 = this.P(var3, var2, true);
            if (var13 == null) {
               this.gS.clear();
               this.u3.add(var6);
               this.VJ = -1;
               return true;
            } else {
               this.gs = (boolean)1;
               this.L(var13, "Digging a way to the next stand");
               return true;
            }
         }
      } else {
         long var8 = sj.F(var3.standX(), var3.standZ());
         if (this.g_ != var8 || this.gK.isEmpty() && this.Vq == 0 || mc.player.tickCount - this.Ve > 40) {
            this.g_ = var8;
            this.Ve = mc.player.tickCount;
            this.Vn = -1;
            int var10 = var3.standX();
            int var11 = var3.standZ();
            List var12 = this.M(var2.bottom(), (var2x, var3x) -> var2x == var10 && var3x == var11);
            if (var12 == null) {
               this.m();
               this.u3.add(var6);
               this.VJ = -1;
               return true;
            }

            if (var12.isEmpty()) {
               return true;
            }

            this.gK = var12;
            this.Vq = 0;
            this.Vc = 0;
            this.H(var2, var3);
         }

         l0.h(this);
         this.F = new BlockPos(var5.x(), var5.y(), var5.z());
         this.s(var2);
         return true;
      }
   }

   private boolean m(wb.q var1, u1.b var2) {
      wb.w var3 = var1.drillCentre();
      BlockPos var4 = new BlockPos(var3.x(), var3.y(), var3.z());
      if (!this.gS.contains(var4) && this.L(var4, var2)) {
         Direction var5 = e(var1).getOpposite();
         int var6 = this.U(var4, var5, var2.bottom());
         return var6 >= 2 && this.d(var4, var6) && this.b(var4, var5, "Drilling a way to the next stand (" + var6 + " blocks)");
      } else {
         return false;
      }
   }

   private String D(BlockPos var1) {
      return BuiltInRegistries.BLOCK.getKey(mc.level.getBlockState(var1).getBlock()).getPath();
   }

   private String O(wb.q var1, u1.b var2) {
      BlockPos var3 = new BlockPos(var1.standX(), var2.bottom(), var1.standZ());
      return String.format(
         Locale.ROOT,
         "stand %d of %d (%s) at %d, %d; player at %s, reachable floor %d cells; feet block %s%s, head %s%s, floor %s",
         this.u0 + 1,
         this.VE.size(),
         var1.kind(),
         var1.standX(),
         var1.standZ(),
         this.p().toShortString(),
         this.l(var2.bottom()).size(),
         this.D(var3),
         this.g(var3) ? " (set aside)" : "",
         this.D(var3.above()),
         this.g(var3.above()) ? " (set aside)" : "",
         this.D(var3.below())
      );
   }

   private void s(wb.q var1, u1.b var2) {
      if (this.I != this.u0 && this.Y4 < 3) {
         this.Y4++;
         com.larpclient.h.A(this.getName(), "Layer {}: {}; digging a way to it", this.Yt + 1, this.O(var1, var2));
      }
   }

   private void z(wb.q var1, u1.b var2) {
      if (this.u8++ < 3) {
         com.larpclient.h.A(this.getName(), "Layer {}: no way to dig to {}; passed over", this.Yt + 1, this.O(var1, var2));
      }
   }

   private BlockPos L(u1.b var1) {
      if (this.uU != null && this.Vt == this.d && this.L(this.uU, var1)) {
         return this.uU;
      }

      this.Vt = this.d;
      this.uU = null;
      double var2 = mc.player.getX();
      double var4 = mc.player.getZ();
      ArrayList var6 = new ArrayList();

      for (BlockPos var8 : this.Yy) {
         if (this.L(var8, var1) && this.K(var8)) {
            var6.add(var8);
         }
      }

      var6.sort(Comparator.comparingDouble(var4x -> Math.pow(var4x.getX() + 0.5 - var2, 2.0) + Math.pow(var4x.getZ() + 0.5 - var4, 2.0)));
      int var10 = 0;

      for (BlockPos var9 : var6) {
         if (var10++ >= 32) {
            break;
         }

         if (this.s(var9, var1)) {
            this.uU = var9;
            break;
         }
      }

      return this.uU;
   }

   static Direction e(wb.q var0) {
      return var0.dx() > 0 ? Direction.EAST : (var0.dx() < 0 ? Direction.WEST : (var0.dz() > 0 ? Direction.SOUTH : Direction.NORTH));
   }

   private boolean z(wb.q var1, u1.b var2) {
      if (var1.kind() != wb.r.J && var1.kind() != wb.r.m) {
         return false;
      } else {
         wb.w var3 = var1.drillCentre();
         BlockPos var4 = new BlockPos(var3.x(), var3.y(), var3.z());
         if (!this.gS.contains(var4) && this.L(var4, var2)) {
            Direction var5 = e(var1).getOpposite();
            int var6 = this.U(var4, var5, var2.bottom());
            this.R(var1);
            return var6 >= 2 && this.d(var4, var6) && this.N(var4, var5, "Cutting a 3x3 face at " + var4.toShortString() + " (" + var6 + " blocks)", true);
         } else {
            return false;
         }
      }
   }

   private boolean A() {
      return this.m() >= 0 && this.e();
   }

   static List<BlockPos> Q(BlockPos var0, Direction var1) {
      ArrayList var2 = new ArrayList(9);
      Axis var3 = var1.getAxis();

      for (int var4 = -1; var4 <= 1; var4++) {
         for (int var5 = -1; var5 <= 1; var5++) {
            var2.add(var3 == Axis.X ? var0.offset(0, var4, var5) : (var3 == Axis.Y ? var0.offset(var4, 0, var5) : var0.offset(var4, var5, 0)));
         }
      }

      return var2;
   }

   private int U(BlockPos var1, Direction var2, int var3) {
      int var4 = 0;

      for (BlockPos var6 : Q(var1, var2)) {
         if (l0.B(var6)) {
            if (!this.d(var6) || var6.getY() < var3 || this.j(var6) || this.x(var6)) {
               return -1;
            }

            var4++;
         }
      }

      return var4;
   }

   private boolean d(BlockPos var1, int var2) {
      BlockState var3 = mc.level.getBlockState(var1);
      Inventory var4 = mc.player.getInventory();
      ItemStack var5 = var4.getItem(this.m());
      ItemStack var6 = var4.getItem(Math.max(0, this.T(var3, this.m())));
      return var5.getDestroySpeed(var3) * var2 >= var6.getDestroySpeed(var3) && (!var3.requiresCorrectToolForDrops() || var5.isCorrectToolForDrops(var3));
   }

   private boolean b(BlockPos var1, Direction var2, String var3) {
      return this.N(var1, var2, var3, false);
   }

   private boolean N(BlockPos var1, Direction var2, String var3, boolean var4) {
      if (this.ue.contains(var1)) {
         return false;
      }

      this.YO = var1.immutable();
      this.gh = var2;
      this.gw = var4;
      BlockHitResult var5 = l0.L(this, var1, this.f());
      if (var5 != null && var5.getDirection() == var2) {
         this.uz = (boolean)1;
         this.g1.clear();

         for (BlockPos var7 : Q(var1, var2)) {
            if (l0.B(var7)) {
               this.g1.put(var7.immutable(), mc.level.getBlockState(var7));
            }
         }

         this.G(var1);
         this.Z(var1, true, var3);
         return true;
      } else {
         this.YO = null;
         return false;
      }
   }

   private void P(BlockPos var1, String var2) {
      if (this.A()) {
         BlockHitResult var3 = l0.L(this, var1, this.f());
         if (var3 != null) {
            int var4 = this.U(var1, var3.getDirection(), this.s());
            if (var4 >= 2 && this.d(var1, var4) && this.b(var1, var3.getDirection(), "Cutting a 3x3 at " + var1.toShortString() + " (" + var4 + " blocks)")) {
               return;
            }
         }
      }

      this.L(var1, var2);
   }

   private boolean R(wb.q var1, u1.b var2) {
      if (var1.kind() == wb.r.h) {
         return true;
      }

      for (wb.w var4 : var1.cells()) {
         BlockPos var5 = new BlockPos(var4.x(), var4.y(), var4.z());
         if (!this.gS.contains(var5) && this.L(var5, var2) && !this.e(var1, var5)) {
            return false;
         }
      }

      return true;
   }

   private boolean e(wb.q var1, BlockPos var2) {
      if (!this.J.getValue() && var1.drill() && !this.g2 && this.m() >= 0) {
         wb.w var3 = var1.drillCentre();
         return var2.getX() != var3.x() || var2.getZ() != var3.z() || var2.getY() > var3.y();
      } else {
         return false;
      }
   }

   private void q(boolean var1) {
      int var2 = !var1 && !this.gs ? 0 : 1;
      if (var2 != 0) {
         this.i = -1000000000;
      }

      this.gs = (boolean)0;
      if (this.u0 < this.VE.size()) {
         this.uL.add(this.VE.get(this.u0));
      }

      this.u0++;
      this.Vn = -1;
      this.gS.clear();
      if (var2 != 0) {
         this.Ys++;
         if (this.uQ.nextDouble() < 0.035 && (this.g2 || this.m() < 0)) {
            this.YV = mc.player.tickCount + this.uQ.nextInt(8, 22);
            this.Yw = mc.player.getYRot() + (float)((this.uQ.nextBoolean() ? 1 : -1) * (20.0 + this.uQ.nextDouble() * 45.0));
            this.Vo = Mth.clamp(mc.player.getXRot() - 12.0F + (float)this.uQ.nextDouble() * 24.0F, -30.0F, 60.0F);
         }
      }
   }

   private void x() {
      l0.h(this);
      this.Vj = Vec3.ZERO;
      this.aimAt(mc.player.getEyePosition().add(Vec3.directionFromRotation(this.Vo, this.Yw).scale(6.0)), 12.0F);
      this.e("Looking around");
   }

   private boolean P() {
      AABB var1 = mc.player.getBoundingBox().inflate(4.0, 6.0, 4.0);
      if (mc.level.getEntitiesOfClass(FallingBlockEntity.class, var1).isEmpty()) {
         return false;
      }

      l0.h(this);
      this.Vj = Vec3.ZERO;
      this.e("Waiting for falling blocks to land");
      return true;
   }

   private BlockPos P(wb.q var1, u1.b var2, boolean var3) {
      double var4 = this.f();
      int var6 = 0;

      for (wb.w var8 : var1.cells()) {
         BlockPos var9 = new BlockPos(var8.x(), var8.y(), var8.z());
         if (!this.gS.contains(var9) && this.L(var9, var2) && !this.e(var1, var9)) {
            if (!this.B(var9)) {
               if (var3) {
                  this.gS.add(var9);
               }
            } else {
               if (!var3 && var6++ >= 8) {
                  return null;
               }

               BlockHitResult var10 = l0.L(this, var9, var4);
               if (var10 != null && this.K(var9, var10)) {
                  return var9;
               }

               if (var3) {
                  this.gS.add(var9);
                  com.larpclient.h.U(this.getName(), "No open face of {} from the stand at {}, {}; later", var9.toShortString(), var1.standX(), var1.standZ());
               }
            }
         }
      }

      return null;
   }

   private BlockPos k(wb.q var1, u1.b var2) {
      if (var1.cells().isEmpty()) {
         return null;
      }

      Integer var3 = this.V5.get(var1.cells().getFirst().key());
      if (var3 != null && var3 != 0) {
         double var4 = this.f();
         Vec3 var6 = mc.player.getEyePosition();
         int var7 = var1.standX();
         int var8 = var1.standZ();
         int var9 = var7 + var1.dx();
         int var10 = var8 + var1.dz();
         ArrayList var11 = new ArrayList();

         for (BlockPos var13 : this.Yy) {
            Integer var14 = this.V5.get(wb.R(var13.getX(), var13.getY(), var13.getZ()));
            if (var14 != null && var14 < var3 && !this.gS.contains(var13)) {
               int var15 = (Math.abs(var13.getX() - var7) > 1 || Math.abs(var13.getZ() - var8) > 1)
                     && (Math.abs(var13.getX() - var9) > 1 || Math.abs(var13.getZ() - var10) > 1)
                  ? 0
                  : 1;
               if (var15 != 0 && !(var6.distanceToSqr(Vec3.atCenterOf(var13)) > var4 * var4) && this.L(var13, var2)) {
                  var11.add(var13);
               }
            }
         }

         if (var11.isEmpty()) {
            return null;
         }

         float var19 = mc.player.getYRot();
         float var20 = mc.player.getXRot();
         BlockPos var21 = this.Vu;
         var11.sort(Comparator.comparingDouble(var6x -> L(var6, var6x, var19, var20, var4, var21)));
         int var22 = 0;

         for (BlockPos var17 : var11) {
            if (var22++ >= 8) {
               break;
            }

            BlockHitResult var18 = l0.L(this, var17, var4);
            if (var18 != null && this.K(var17, var18)) {
               return var17;
            }

            this.gS.add(var17);
         }

         return null;
      } else {
         return null;
      }
   }

   private void L(BlockPos var1, String var2) {
      this.YO = null;
      this.uz = (boolean)0;
      this.g1.clear();
      this.G(var1);
      this.Z(var1, false, var2);
   }

   private boolean N(wb.q var1, u1.b var2) {
      BlockPos var3 = null;

      for (wb.w var5 : var1.cells()) {
         BlockPos var6 = new BlockPos(var5.x(), var5.y(), var5.z());
         if (this.L(var6, var2)) {
            var3 = var6;
            break;
         }
      }

      if (this.g8 == this.u0 && mc.player.tickCount - this.n < 40) {
         return false;
      }

      if (!this.l(var2.bottom()).contains(sj.F(var1.standX(), var1.standZ()))) {
         return false;
      }

      if (this.Vn != this.u0 || this.gK.isEmpty() && this.Vq == 0 || mc.player.tickCount - this.Ve > 40) {
         this.Vn = this.u0;
         this.Ve = mc.player.tickCount;
         int var7 = var1.standX();
         int var8 = var1.standZ();
         List var9 = this.M(var2.bottom(), (var2x, var3x) -> var2x == var7 && var3x == var8);
         if (var9 == null) {
            this.m();
            this.Vn = -1;
            this.g8 = this.u0;
            this.n = mc.player.tickCount;
            return false;
         }

         if (var9.isEmpty()) {
            return true;
         }

         this.gK = var9;
         this.Vq = 0;
         this.Vc = 0;
         this.H(var2, var1);
         com.larpclient.h.U(this.getName(), "{} {} of {}: walking {} steps to {}, {}", var1.kind(), this.u0 + 1, this.VE.size(), this.gK.size(), var7, var8);
      }

      l0.h(this);
      this.F = var3;
      this.s(var2);
      return true;
   }

   private void H(u1.b var1, wb.q var2) {
      this.uE = this.gK;
      this.uk = null;
      sj.e var3 = this.gK.getLast();
      boolean var4 = com.larpclient.gu.N(var3.x(), var1.bottom(), var3.z());
      double var5 = var4 ? 0.0 : -0.18 + this.uQ.nextDouble() * 0.19999999999999998;
      double var7 = var4 ? 0.0 : (this.uQ.nextDouble() * 2.0 - 1.0) * 0.12;
      this.uO = var2.dx() * var5 - var2.dz() * var7;
      this.u9 = var2.dz() * var5 + var2.dx() * var7;
      this.gA = var4 ? 0.245 : 0.15 + this.uQ.nextDouble() * 0.15;
      this.Vv = 0.35 + this.uQ.nextDouble() * 0.65;
      this.u6 = this.uQ.nextDouble() * Math.PI * 2.0;
   }

   private boolean g(u1.b var1) {
      int var2 = this.s();
      wb.q var3 = null;

      for (wb.q var5 : this.VE) {
         if (var5.kind() == wb.r.h && var5.standY() == var2) {
            var3 = var5;
            break;
         }
      }

      if (var3 == null) {
         return this.n("no step planned from Y " + var2);
      }

      if (var2 != this.Yg) {
         this.Yg = var2;
         this.up = 0;
      }

      if (this.P()) {
         return true;
      }

      BlockPos var13 = this.p();
      int var14 = var3.standX() + var3.dx();
      int var6 = var3.standZ() + var3.dz();
      BlockPos var7 = var3.cells().isEmpty()
         ? new BlockPos(var14, var2 - 1, var6)
         : new BlockPos(var3.cells().getFirst().x(), var3.cells().getFirst().y(), var3.cells().getFirst().z());
      if (var13.getX() == var14 && var13.getZ() == var6 && this.Z(new BlockPos(var14, var2 - 1, var6))) {
         this.T(var3, var2);
         return ++this.up > 80 ? this.n("could not step down to " + new BlockPos(var14, var2 - 1, var6).toShortString()) : true;
      }

      if (var13.getX() == var3.standX() && var13.getZ() == var3.standZ()) {
         this.m();

         for (int var15 = var2 - 1; var15 <= var2 + 1; var15++) {
            if (!var1.contains(var15) && !this.Z(new BlockPos(var14, var15, var6))) {
               return this.n(
                  new BlockPos(var14, var15, var6).toShortString()
                     + (this.B.J().contains(var14, var15, var6) ? " was left by the layer above" : " above the area")
                     + " and is in the way"
               );
            }
         }

         BlockPos var16 = new BlockPos(var14, var2 - 1, var6);

         for (wb.w var18 : var3.cells()) {
            BlockPos var11 = new BlockPos(var18.x(), var18.y(), var18.z());
            if (!this.Z(var11)) {
               AABB var12 = mc.player.getBoundingBox();
               if (var11.getY() == var2 - 1 && var12.maxX > var14 && var12.minX < var14 + 1 && var12.maxZ > var6 && var12.minZ < var6 + 1) {
                  l0.h(this);
                  this.k(new Vec3(var3.standX() + 0.5 - var3.dx() * 0.1, mc.player.getY(), var3.standZ() + 0.5 - var3.dz() * 0.1));
                  this.e("Stepping back from the edge");
                  if (++this.up > 80) {
                     return this.n("could not get clear of " + var16.toShortString());
                  }

                  return true;
               }

               if (!this.L(var11, var1)) {
                  return this.n(var11.toShortString() + " cannot be mined (set aside, beside a fluid or unbreakable)");
               }

               if (var11.equals(var16) && !this.z(var16, var1)) {
                  return this.n("the drop at " + var16.toShortString() + " is not safe");
               }

               this.V8 = (boolean)1;
               this.L(var11, "Cutting a step down to layer " + (this.Yt + 1));
               return true;
            }
         }

         if (!this.z(var16, var1)) {
            return this.n("the drop at " + var16.toShortString() + " is not safe");
         }

         this.T(var3, var2);
         return ++this.up > 80 ? this.n("could not step down to " + var16.toShortString()) : true;
      } else {
         if (this.gZ != var2 || this.gK.isEmpty() && this.Vq == 0) {
            this.gZ = var2;
            int var8 = var3.standX();
            int var9 = var3.standZ();
            List var10 = this.M(var2, (var2x, var3x) -> var2x == var8 && var3x == var9);
            if (var10 == null) {
               return this.n("no way to the step at " + var8 + ", " + var2 + ", " + var9);
            }

            this.gK = var10;
            this.Vq = 0;
            this.uE = List.of();
            this.Vc = 0;
         }

         l0.h(this);
         this.F = var7;
         this.P(var2);
         this.e("Walking to the step down to layer " + (this.Yt + 1));
         return true;
      }
   }

   private void T(wb.q var1, int var2) {
      l0.h(this);
      this.Vc = 0;
      int var3 = var1.standX() + var1.dx();
      int var4 = var1.standZ() + var1.dz();
      Vec3 var5 = mc.player.position();
      Vec3 var6 = new Vec3(var3 + 0.5 - var5.x, 0.0, var4 + 0.5 - var5.z);
      double var7 = var6.length();
      double var9 = var7 < 1.0E-6 ? 0.0 : mc.player.getDeltaMovement().multiply(1.0, 0.0, 1.0).dot(var6.normalize());
      boolean var11 = this.gC.u(var7 + 0.2, var9, ta.E(this));
      this.Vj = var11 ? var6 : Vec3.ZERO;
      this.aimAt(new Vec3(var3 + 0.5 + var1.dx() * 0.7, var2 - 1, var4 + 0.5 + var1.dz() * 0.7), 12.0F);
      this.e("Stepping down to layer " + (this.Yt + 1));
   }

   private boolean n(String var1) {
      this.ud = (boolean)1;
      this.m();
      this.V8 = (boolean)0;
      com.larpclient.h.A(this.getName(), "Layer {}: the stair down is not used ({}); digging down at the corner instead", this.Yt + 1, var1);
      return false;
   }

   private void X(Set<BlockPos> var1, String var2) {
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

      u1.y var12 = u1.y.of(var3, var4);
      this.VA = var2.isBlank() ? "the block set" : var2;
      BlockPos var13 = this.p();
      double var7 = Math.max(
         Math.max(var12.minX() - var13.getX(), var13.getX() - var12.maxX()), Math.max(var12.minZ() - var13.getZ(), var13.getZ() - var12.maxZ())
      );
      if (var7 > 24.0) {
         this.T("stand next to " + this.VA + " to mine it out (you are " + (int)var7 + " blocks away)");
      } else {
         int var9 = this.H();
         int var10 = var9 < 0 && this.m() >= 0 ? 1 : 0;
         if (var9 < 0 && var10 == 0) {
            this.T("put a pickaxe in your hotbar");
         } else {
            this.YJ = var1;
            this.B = new u1(var12, this.uM.getValue().intValue(), this.VZ.getValue().intValue(), var13.getX(), var13.getZ());
            this.ux = mc.level;
            this.YZ = mc.player;
            this.H.k();
            this.g2 = (boolean)1;
            this.Yt = this.b = 0;
            this.Yz = System.currentTimeMillis();
            this.c = this.V2 = this.z();
            this.q = mc.player.tickCount;
            this.o = 0L;
            String var11 = mc.player.getInventory().getItem(var10 != 0 ? this.m() : var9).getHoverName().getString();
            this.K(
               "mining out "
                  + this.VA
                  + ": "
                  + this.c
                  + " blocks in "
                  + var12.describe()
                  + " with "
                  + var11
                  + (var10 != 0 ? ", only where its cut stays inside the schematic" : "")
                  + ", from the top down."
            );
            if (this.Vs.getValue() && hk.J(var0 -> var0.get(DataComponents.FOOD) != null) < 0) {
               this.K("heads up: no food in the hotbar.");
            }

            com.larpclient.h.A(
               this.getName(), "Block set started: {} cells of {} in {}, {} still standing, tool {}", var1.size(), this.VA, var12.describe(), this.c, var11
            );
         }
      }
   }

   private int z() {
      int var1 = 0;

      for (BlockPos var3 : this.YJ) {
         if (l0.B(var3)) {
            var1++;
         }
      }

      return var1;
   }

   private void C() {
      if (!mc.player.onGround() && !mc.player.isInWater() && !this.uD.i()) {
         l0.h(this);
         this.e("Waiting to land");
      } else {
         this.T();
         this.X();
         this.c();
         if (mc.player.tickCount - this.q >= 100) {
            this.q = mc.player.tickCount;
            this.V2 = this.z();
         }

         if (this.gD) {
            this.h();
         } else {
            if (mc.player.tickCount - this.gE >= 40) {
               this.R();
            }

            int var1 = this.o();
            if (var1 < 0) {
               if (mc.player.tickCount - this.gE > 2) {
                  this.R();
               } else {
                  byte var5 = 0;

                  for (BlockPos var4 : this.Yy) {
                     if (this.uW.containsKey(var4) && !this.YT.contains(var4) && l0.B(var4)) {
                        this.uW.remove(var4);
                        var5 = 1;
                     }
                  }

                  if (var5 != 0) {
                     com.larpclient.h.U(this.getName(), "Retrying block-set cells set aside");
                     this.uD.H();
                  } else {
                     this.uD.H();
                     this.V2 = this.z();
                     if (this.g6.getValue()) {
                        this.gD = (boolean)1;
                        this.e("Picking up drops");
                        com.larpclient.h.A(this.getName(), "Picking up drops around {}", this.VA);
                     } else {
                        this.i();
                     }
                  }
               }
            } else {
               this.Yt = var1;
               u1.b var2 = this.B.J(var1);
               BlockPos var3 = this.uD.i() && this.uD.A() != null ? null : this.n(var2);
               if (var3 != null) {
                  this.uD.H();
                  this.P(var3, null);
               } else {
                  l0.h(this);
                  this.I(var2);
               }
            }
         }
      }
   }

   private void R() {
      this.gE = mc.player.tickCount;
      this.Yy.clear();
      this.uv.clear();
      int var1 = mc.player.tickCount;
      this.uW.values().removeIf(var1x -> var1x <= var1);

      for (BlockPos var3 : this.YJ) {
         if (mc.level.hasChunkAt(var3) && l0.B(var3)) {
            this.Yy.add(var3);
         }
      }

      this.Yy
         .sort(
            Comparator.<BlockPos>comparingInt(var1x -> this.B.k(var1x.getY()))
               .thenComparingLong(var1x -> this.B.u(var1x.getX(), var1x.getY(), var1x.getZ(), this.B.k(var1x.getY())))
         );
   }

   private int o() {
      for (BlockPos var2 : this.Yy) {
         int var3 = this.B.k(var2.getY());
         if (var3 >= 0 && this.L(var2, this.B.J(var3))) {
            return var3;
         }
      }

      return -1;
   }

   private void I(u1.b var1) {
      BlockPos var2 = BlockPos.containing(mc.player.getX(), mc.player.getY() + 0.2, mc.player.getZ());
      if (!this.uD.i() || mc.player.tickCount - this.V3 > 100) {
         this.V3 = mc.player.tickCount;
         Vec3 var3 = mc.player.getEyePosition();
         this.g4.clear();
         this.Yy
            .stream()
            .filter(var2x -> this.L(var2x, var1))
            .sorted(Comparator.comparingDouble(var1x -> var3.distanceToSqr(Vec3.atCenterOf(var1x))))
            .limit(8L)
            .forEach(this.g4::add);
         if (this.g4.isEmpty()) {
            this.uD.H();
            return;
         }

         final double var4 = Math.pow(this.f() - 0.4, 2.0);
         final double var6 = mc.player.getEyeHeight();
         final ArrayList var8 = new ArrayList<>(this.g4);
         zf.f var9 = new zf.f() {
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
         u1.y var10 = this.B.J();
         zf.s var11 = new zf.s(
            Math.min(var10.minX(), var2.getX()) - 8,
            Math.min(var10.minY(), var2.getY()) - 4,
            Math.min(var10.minZ(), var2.getZ()) - 8,
            Math.max(var10.maxX(), var2.getX()) + 8,
            Math.max(var10.maxY(), var2.getY()) + 4,
            Math.max(var10.maxZ(), var2.getZ()) + 8
         );
         c6 var12 = new c6(var1x -> this.T(var1x, this.m()), false);
         if (this.gq) {
            var12.S(this.gj.J().minX(), this.gj.J().minY(), this.gj.J().minZ(), this.gj.J().maxX(), this.gj.J().maxY(), this.gj.J().maxZ());
         } else {
            var12.Q(this.YJ);
         }

         zf.v var13 = zf.B(var12, var2.getX(), var2.getY(), var2.getZ(), var9, var11, zf.z.digging(12000));
         if (!var13.found() || var13.steps().isEmpty()) {
            this.uD.H();
            String var14 = var13.found() ? "not visible from any standing spot nearby" : "no way to stand within reach";

            for (BlockPos var16 : this.g4) {
               this.A(var16, var14);
            }

            com.larpclient.h.U(this.getName(), "Block set: {} for {} cells near {}", var14, this.g4.size(), this.g4.getFirst().toShortString());
            if (this.p() >= 3) {
               this.s(var1, var14);
            }

            this.YR++;
            return;
         }

         this.uD.M(var13.steps());
         com.larpclient.h.U(this.getName(), "Block set route of {} steps toward {}", var13.steps().size(), this.g4.getFirst().toShortString());
      }

      gu.r var17 = this.uD.S(this.f(), var1x -> this.T(var1x, this.m()), this.VD.getValue());
      this.Vj = this.uD.q();
      this.Vi = this.uD.t();
      this.YY = this.uD.o();
      this.gY = this.uD.O();
      this.e("Walking to " + (this.g4.isEmpty() ? "the next block" : this.g4.getFirst().toShortString()));
      if (var17 == gu.r.c) {
         this.uD.H();
         this.u = 0;
      } else if (var17 == gu.r.J) {
         com.larpclient.h.U(this.getName(), "Block set route stuck: {}", this.uD.d());
         this.uD.H();
         if (++this.u >= 3) {
            this.u = 0;

            for (BlockPos var5 : this.g4) {
               this.A(var5, "stuck on the way");
            }
         }
      }
   }

   private void v() {
      if (this.uK > 0) {
         com.larpclient.h.A(this.getName(), "Gave up on {} blocks in the last 10 s (first {}): {}", this.uK, this.Vb, this.gH);
         this.uK = 0;
         this.Vb = this.gH = null;
      }

      if (this.Y_ != 0) {
         com.larpclient.h.A(this.getName(), "Set aside {} blocks in {} places in the last 10 s (first around {}): {}", this.us, this.Y_, this.YM, this.gV);
         this.us = this.Y_ = 0;
         this.YM = this.gV = null;
      }
   }

   private Set<Long> l(int var1) {
      BlockPos var2 = this.p();
      long var3 = sj.F(var2.getX(), var2.getZ());
      int var5 = mc.player.tickCount;
      if (var1 != this.ge || this.d != this.gt || var5 - this.i >= 40 || var3 != this.g3 && !this.uT.contains(var3)) {
         this.g3 = var3;
         this.ge = var1;
         this.gt = this.d;
         this.i = var5;
         u1.y var6 = this.B.J();
         sj.o var7 = new sj.o(
            Math.min(var6.minX(), var2.getX()) - 2,
            Math.min(var6.minZ(), var2.getZ()) - 2,
            Math.max(var6.maxX(), var2.getX()) + 2,
            Math.max(var6.maxZ(), var2.getZ()) + 2
         );
         HashSet var8 = new HashSet();
         ArrayDeque var9 = new ArrayDeque();
         var8.add(var3);
         var9.add(new long[]{var2.getX(), var2.getZ()});
         this.Yh = (boolean)(this.R.getValue() && this.YJ == null ? 1 : 0);

         try {
            while (!var9.isEmpty() && var8.size() < 20000) {
               long[] var10 = (long[])var9.poll();

               for (int[] var14 : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
                  int var15 = (int)var10[0] + var14[0];
                  int var16 = (int)var10[1] + var14[1];
                  long var17 = sj.F(var15, var16);
                  if (!var8.contains(var17) && var7.contains(var15, var16) && this.m(var15, var1, var16)) {
                     var8.add(var17);
                     var9.add(new long[]{var15, var16});
                  }
               }
            }
         } finally {
            this.Yh = (boolean)0;
         }

         this.uT = var8;
         return this.uT;
      } else {
         return this.uT;
      }
   }

   private boolean s(BlockPos var1, u1.b var2) {
      Set var3 = this.l(var2.bottom());
      if (var3.size() > 4000) {
         return true;
      }

      double var4 = this.f() - 0.4;
      double var6 = var2.bottom() + mc.player.getEyeHeight();
      Vec3 var8 = Vec3.atCenterOf(var1);

      for (long var10 : var3) {
         double var12 = sj.F(var10) + 0.5 - var8.x;
         double var14 = var6 - var8.y;
         double var16 = sj.o(var10) + 0.5 - var8.z;
         if (var12 * var12 + var14 * var14 + var16 * var16 <= var4 * var4) {
            return true;
         }
      }

      return false;
   }

   private int p() {
      if (this.d != this.a) {
         this.a = this.d;
         this.YR = 0;
      }

      return this.YR;
   }

   private void F(BlockPos var1, u1.b var2, String var3) {
      int var4 = mc.player.tickCount;
      int var5 = 0;

      for (BlockPos var7 : this.Yy) {
         if (!var7.equals(var1) && var7.distChessboard(var1) <= 2 && this.L(var7, var2)) {
            this.uW.put(var7, var4 + 400);
            var5++;
         }
      }

      if (var5 != 0) {
         this.us += var5;
         this.Y_++;
         if (this.YM == null) {
            this.YM = var1.toShortString();
            this.gV = var3;
         }
      }
   }

   private void s(u1.b var1, String var2) {
      int var3 = 0;

      for (BlockPos var5 : this.Yy) {
         if (this.L(var5, var1)) {
            this.YT.add(var5.immutable());
            var3++;
         }
      }

      if (var3 != 0) {
         int var6 = this.R.getValue() && this.d() >= 0 ? 1 : 0;
         String var7 = var2 + (var6 != 0 ? "" : ", and there are no blocks in the hotbar to pillar up with");
         com.larpclient.h.A(this.getName(), "Leaving {} blocks of {} at Y {} to {}: {}", var3, this.VA, var1.bottom(), var1.top(), var7);
         this.K("left " + var3 + " blocks at Y " + var1.bottom() + " to " + var1.top() + ": " + var7 + ".");
         this.e("Left " + var3 + " blocks out of reach");
      }
   }

   private void c() {
      if (this.YA != null && !l0.B(this.YA)) {
         if (mc.level.getBlockState(this.YA).isAir()) {
            this.d++;
            if (this.VI != null) {
               this.X.put(this.YA, this.VI);
               int var1 = mc.player.tickCount;
               if (this.ua.size() < 256) {
                  this.ua.put(this.YA, var1);
                  int var2 = G(Math.sqrt(mc.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(this.YA))));
                  this.gI.put(this.YA, var2);
                  this.YK[var2]++;
               }

               this.G
                  .put(
                     this.YA,
                     String.format(
                        Locale.ROOT,
                        "broken with %s%s after %d ticks on it, %d ticks after the break before, %s",
                        mc.player.getMainHandItem().getHoverName().getString(),
                        this.uz ? " as a drill face" : "",
                        var1 - this.Yu,
                        var1 - this.Y0,
                        mc.player.onGround() ? "on the ground" : "in the air"
                     )
                  );
               this.Y0 = var1;
               if (this.uz && this.g1.size() > 1 && this.e()) {
                  this.uS = var1 + 2 + this.uQ.nextInt(3);
                  this.gG.clear();
                  this.gG.addAll(this.g1.keySet());
                  this.uY = var1 + 10;
               }
            }
         }

         for (Entry var4 : this.g1.entrySet()) {
            if (!((BlockPos)var4.getKey()).equals(this.YA) && mc.level.getBlockState((BlockPos)var4.getKey()).isAir()) {
               this.d++;
               this.X.put((BlockPos)var4.getKey(), (BlockState)var4.getValue());
            }
         }

         this.g1.clear();
         this.YA = null;
         this.VI = null;
      }
   }

   private BlockPos G(BlockPos var1) {
      if (this.VN != null && !var1.equals(this.VN)) {
         this.VF.add(var1.immutable());
      }

      BlockState var2 = mc.level.getBlockState(var1);
      BlockState var3 = this.X.remove(var1);
      String var4 = this.G.remove(var1);
      if (var3 != null && var3.getBlock() == var2.getBlock() && !(var2.getBlock() instanceof FallingBlock)) {
         com.larpclient.h.A(this.getName(), "Server put back {} ({}): {}", var1.toShortString(), var2.getBlock().getName().getString(), var4);
         this.j(var1);
      }

      if (!var1.equals(this.YA)) {
         this.Yu = mc.player.tickCount;
      }

      this.YA = var1;
      this.VI = var2;
      this.Vu = var1.immutable();
      return var1;
   }

   private void B(u1.b var1) {
      this.gE = mc.player.tickCount;
      this.Yy.clear();
      this.uv.clear();
      u1.y var2 = this.B.J();
      int var3 = mc.player.tickCount;
      this.uW.values().removeIf(var1x -> var1x <= var3);

      for (int var4 = var1.top(); var4 >= var1.bottom(); var4--) {
         for (int var5 = var2.minX(); var5 <= var2.maxX(); var5++) {
            for (int var6 = var2.minZ(); var6 <= var2.maxZ(); var6++) {
               BlockPos var7 = new BlockPos(var5, var4, var6);
               if (mc.level.hasChunkAt(var7)) {
                  if (!mc.level.getFluidState(var7).isEmpty()) {
                     this.uv.add(var7);
                  } else if (l0.B(var7)) {
                     this.Yy.add(var7);
                  }
               }
            }
         }
      }

      int var8 = this.Yt;
      this.Yy.sort(Comparator.comparingLong(var2x -> {
         Integer var3x = this.j == var8 ? this.V5.get(wb.R(var2x.getX(), var2x.getY(), var2x.getZ())) : null;
         return var3x != null ? var3x.intValue() : 1125899906842624L + this.B.u(var2x.getX(), var2x.getY(), var2x.getZ(), var8);
      }));
   }

   private void j(BlockPos var1) {
      int var2 = this.Vh.merge(var1, 1, Integer::sum);
      if (var2 >= 3) {
         this.Vh.remove(var1);
         this.VU.add(var1.immutable());
         this.uW.put(var1, mc.player.tickCount + 2400);
         this.K(var1.toShortString() + ": the server keeps putting it back, skipping it for now.");
         com.larpclient.h.A(this.getName(), "Server restored {} {} times; skipping for {} ticks", var1.toShortString(), 3, 2400);
      }
   }

   private boolean D() {
      int var1 = mc.player.tickCount;
      if (var1 - this.Yr >= 600) {
         this.Yr = var1;
         int var2 = 0;

         for (int var6 : this.YK) {
            var2 += var6;
         }

         if (var2 > 0) {
            com.larpclient.h.A(
               this.getName(),
               "Breaks put back by eye distance: under 2 {}/{}, 2-3 {}/{}, 3-3.5 {}/{}, 3.5-4 {}/{}, 4+ {}/{}",
               this.Y2[0],
               this.YK[0],
               this.Y2[1],
               this.YK[1],
               this.Y2[2],
               this.YK[2],
               this.Y2[3],
               this.YK[3],
               this.Y2[4],
               this.YK[4]
            );
         }
      }

      Vec3 var7 = mc.player.getEyePosition();
      this.ua
         .entrySet()
         .removeIf(
            var3 -> {
               int var4 = var1 - var3.getValue();
               if (var4 <= 200 && var4 >= 0) {
                  BlockState var5 = mc.level.getBlockState(var3.getKey());
                  if (var5.isAir()) {
                     return false;
                  }

                  Integer var6x = this.gI.remove(var3.getKey());
                  if (var6x != null) {
                     this.Y2[var6x]++;
                  }

                  l0.r(this.getName(), var3.getKey());
                  com.larpclient.h.A(
                     this.getName(),
                     "{} came back as {} {} ticks after the break, {} blocks from the eye",
                     var3.getKey().toShortString(),
                     var5.getBlock().getName().getString(),
                     var4,
                     String.format(Locale.ROOT, "%.1f", Math.sqrt(var7.distanceToSqr(Vec3.atCenterOf((Vec3i)var3.getKey()))))
                  );
                  if (var4 <= 5 && !(var5.getBlock() instanceof FallingBlock)) {
                     this.ga.g(var3.getKey().asLong());
                  }

                  return true;
               } else {
                  this.gI.remove(var3.getKey());
                  return true;
               }
            }
         );
      if (!this.ga.K()) {
         return false;
      }

      com.larpclient.h.A(this.getName(), "Stopping: the server took back {} of the last {} breaks", this.ga.t(), 10);
      l0.h(this);
      this.T("the server put back " + this.ga.t() + " of your last 10 breaks, so it is refusing them and digging on only draws attention");
      return true;
   }

   private void T() {
      if (mc.player.tickCount - this.Y >= 600) {
         this.Y = mc.player.tickCount;
         this.Vh.clear();
      }
   }

   private boolean g(BlockPos var1) {
      Integer var2 = this.uW.get(var1);
      return var2 != null && var2 > mc.player.tickCount || this.YT.contains(var1);
   }

   private void A(BlockPos var1, String var2) {
      int var3 = this.Yp.merge(var1, 1, Integer::sum);
      if (var3 >= 3) {
         this.YT.add(var1);
         if (this.uK++ == 0) {
            this.Vb = var1.toShortString();
            this.gH = var2;
         }
      } else {
         this.uW.put(var1, mc.player.tickCount + 400);
         com.larpclient.h.U(this.getName(), "Skipping {} for now: {}", var1.toShortString(), var2);
      }

      this.e("Set aside " + var1.toShortString() + ": " + var2);
      if (var1.equals(this.YA)) {
         this.YA = null;
      }

      if (var1.equals(this.F)) {
         this.F = null;
      }
   }

   private boolean x(BlockPos var1) {
      for (Direction var5 : Direction.values()) {
         if (var5 != Direction.DOWN && !mc.level.getFluidState(var1.relative(var5)).isEmpty()) {
            return true;
         }
      }

      return false;
   }

   private boolean H(BlockPos var1) {
      if (this.g2) {
         return true;
      }

      int var2 = this.B.d(var1.getX(), var1.getZ(), this.Yt);
      return !this.B.I(var2, this.Yt) ? true : this.gW.contains(this.p(var1.getX(), var1.getZ()));
   }

   private BlockPos n(u1.b var1) {
      double var2 = this.f();
      Vec3 var4 = mc.player.getEyePosition();
      if (this.YA == null
         || !this.L(this.YA, var1)
         || !(var4.distanceToSqr(Vec3.atCenterOf(this.YA)) <= var2 * var2)
         || (this.uz ? !this.T(this.YA) : !this.K(this.YA, l0.L(this, this.YA, var2)))) {
         this.YA = null;
         this.VI = null;
         this.uz = (boolean)0;
         this.g1.clear();
         if (!this.g2 && this.m() >= 0) {
            BlockPos var5 = this.Z(var1, var2, var4);
            if (var5 != null) {
               this.uz = (boolean)1;
               this.g1.clear();
               int var15 = this.B.d(var5.getX(), var5.getZ(), this.Yt);
               boolean var17 = this.B.J();

               for (int var18 = -1; var18 <= 1; var18++) {
                  for (int var19 = -1; var19 <= 1; var19++) {
                     BlockPos var21 = var17 ? var5.offset(0, var18, var19) : var5.offset(var19, var18, 0);
                     if (l0.B(var21)) {
                        this.g1.put(var21, mc.level.getBlockState(var21));
                     }
                  }
               }

               com.larpclient.h.U(this.getName(), "Drill face at {} in row {} ({} blocks)", var5.toShortString(), var15 + 1, this.g1.size());
               return this.G(var5);
            }
         }

         ArrayList var13 = new ArrayList();

         for (BlockPos var7 : this.Yy) {
            if (this.L(var7, var1) && this.H(var7) && !(var4.distanceToSqr(Vec3.atCenterOf(var7)) > var2 * var2)) {
               var13.add(var7);
            }
         }

         float var14 = mc.player.getYRot();
         float var16 = mc.player.getXRot();
         BlockPos var8 = this.Vu;
         var13.sort(Comparator.comparingDouble(var6 -> L(var4, var6, var14, var16, var2, var8)));
         int var9 = 0;

         for (BlockPos var11 : var13) {
            if (var9++ >= 16) {
               break;
            }

            if (this.K(var11, l0.L(this, var11, var2))) {
               return this.G(var11);
            }
         }

         for (BlockPos var22 : var13.stream()
            .sorted(Comparator.comparingLong(var1x -> this.B.u(var1x.getX(), var1x.getY(), var1x.getZ(), this.Yt)))
            .limit(4L)
            .toList()) {
            BlockPos var12 = this.G(var4, var22, var2);
            if (var12 != null && this.L(var12, var1) && this.B(var12) && this.K(var12, l0.L(this, var12, var2))) {
               com.larpclient.h.U(this.getName(), "Digging through {} toward {}", var12.toShortString(), var22.toShortString());
               return this.G(var12);
            }
         }

         return null;
      } else {
         return this.YA;
      }
   }

   static double L(Vec3 var0, BlockPos var1, float var2, float var3, double var4, BlockPos var6) {
      double var7 = Math.sqrt(var0.distanceToSqr(Vec3.atCenterOf(var1)));
      double var9 = s(var0, var1, var2, var3) + 10.0 * var7;
      if (var7 > var4 - 0.6) {
         var9 += 40.0;
      }

      if (var6 != null && var1.distManhattan(var6) == 1) {
         var9 -= 15.0;
      }

      return var9;
   }

   static double s(Vec3 var0, BlockPos var1, float var2, float var3) {
      float[] var4 = j6.s(var0, Vec3.atCenterOf(var1));
      return Math.hypot(Mth.wrapDegrees(var4[0] - var2), var4[1] - var3);
   }

   private BlockPos G(Vec3 var1, BlockPos var2, double var3) {
      Vec3 var5 = Vec3.atCenterOf(var2);
      if (var1.distanceToSqr(var5) > var3 * var3) {
         return null;
      }

      BlockHitResult var6 = mc.level.clip(new ClipContext(var1, var5, Block.OUTLINE, Fluid.NONE, mc.player));
      return var6.getType() == Type.BLOCK && !var6.getBlockPos().equals(var2) ? var6.getBlockPos() : null;
   }

   private boolean L(BlockPos var1, u1.b var2) {
      return var2.contains(var1.getY()) && this.d(var1) && l0.B(var1) && !this.g(var1) && !this.x(var1) && !this.j(var1);
   }

   private boolean j(BlockPos var1) {
      AABB var2 = mc.player.getBoundingBox();
      return var1.getY() == this.s() - 1 && var2.maxX > var1.getX() && var2.minX < var1.getX() + 1 && var2.maxZ > var1.getZ() && var2.minZ < var1.getZ() + 1;
   }

   private int m() {
      String var1 = this.VV.getValue().trim().toLowerCase(Locale.ROOT);
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

   private long p(int var1, int var2) {
      int var3 = this.B.d(var1, var2, this.Yt);
      int var4 = this.B.J() ? var1 : var2;
      return (long)var3 << 32 ^ var4 & 4294967295L;
   }

   private BlockPos I(int var1, int var2, u1.b var3) {
      int var4 = this.B.p(var1, this.Yt);
      return this.B.J() ? new BlockPos(var2, var3.bottom() + 1, var4) : new BlockPos(var4, var3.bottom() + 1, var2);
   }

   private Direction N(int var1) {
      int var2 = this.B.b(var1, this.Yt);
      return this.B.J() ? (var2 > 0 ? Direction.WEST : Direction.EAST) : (var2 > 0 ? Direction.NORTH : Direction.SOUTH);
   }

   private boolean T(BlockPos var1) {
      if (!var1.equals(this.YO)) {
         return this.Y(var1);
      }

      BlockHitResult var2 = l0.L(this, var1, this.f());
      return var2 != null && var2.getDirection() == this.gh;
   }

   private boolean Y(BlockPos var1) {
      BlockHitResult var2 = l0.L(this, var1, this.f());
      int var3 = this.B.d(var1.getX(), var1.getZ(), this.Yt);
      return var2 != null && var2.getDirection() == this.N(var3);
   }

   private BlockPos Z(u1.b var1, double var2, Vec3 var4) {
      HashSet var5 = new HashSet();

      for (BlockPos var7 : this.Yy) {
         if (this.L(var7, var1)) {
            int var8 = this.B.d(var7.getX(), var7.getZ(), this.Yt);
            if (this.B.I(var8, this.Yt) && !this.gW.contains(this.p(var7.getX(), var7.getZ()))) {
               long var9 = this.p(var7.getX(), var7.getZ());
               if (var5.add(var9)) {
                  BlockPos var11 = this.I(var8, this.B.J() ? var7.getX() : var7.getZ(), var1);
                  if (this.YT.contains(var11) || !this.g(var11) || !l0.B(var11)) {
                     if (this.L(var11, var1)) {
                        if (!(var4.distanceToSqr(Vec3.atCenterOf(var11)) > var2 * var2) && this.Y(var11)) {
                           return var11;
                        }

                        return null;
                     }

                     this.gW.add(var9);
                  }
               }
            }
         }
      }

      return null;
   }

   private double f() {
      return Math.min(this.gg.getValue(), mc.player.blockInteractionRange());
   }

   private void A(BlockPos var1, boolean var2) {
      this.Z(var1, var2, null);
   }

   private void Z(BlockPos var1, boolean var2, String var3) {
      int var4 = this.m();
      BlockPos var5 = var1.immutable();
      if (!this.B(var5)) {
         l0.h(this);
         this.u_ = null;
         this.A(var5, "out of reach from here");
      } else {
         if (var5.equals(this.u_)) {
            if (var5.equals(this.YO) && this.uc >= 60 && !this.V(var5)) {
               com.larpclient.h.A(
                  this.getName(), "Drill hit on {} dropped: the crosshair stayed off its {} face for {} ticks", var5.toShortString(), this.gh, 60
               );
               this.ue.add(var5);
               this.YO = null;
               this.uz = (boolean)0;
               this.g1.clear();
               this.Z(var5, false, var3);
               return;
            }

            if (++this.uc > 200) {
               BlockHitResult var6 = l0.u(var5, this.f());
               com.larpclient.h.A(
                  this.getName(),
                  "Could not break {} ({}) in {} ticks; crosshair {}, distance {}, phase {}",
                  var5.toShortString(),
                  BuiltInRegistries.BLOCK.getKey(mc.level.getBlockState(var5).getBlock()).getPath(),
                  200,
                  var6 == null ? "off the block" : "on " + var6.getDirection(),
                  String.format(Locale.ROOT, "%.2f", Math.sqrt(mc.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var5)))),
                  this.um
               );
               this.u_ = null;
               this.uc = 0;
               l0.h(this);
               this.u1 = null;
               this.A(var5, "could not break it");
               return;
            }
         } else {
            this.u_ = var5;
            this.uc = 0;
         }

         if (var4 < 0 || !mc.player.isShiftKeyDown() && !mc.player.isCrouching() || !var2 && this.T(mc.level.getBlockState(var5), var4) != var4) {
            this.X(var5, var2 || var4 >= 0 && this.T(mc.level.getBlockState(var5), var4) == var4);
            boolean var7 = l0.z(
               this,
               var5,
               this.f(),
               true,
               12.0F,
               var4x -> {
                  int var5x = var2 && var4 >= 0 ? var4 : this.T(var4x, var4);
                  this.uP = var5x;
                  if (var5x >= 0 && this.A(var5x) && this.gz.add(mc.player.getInventory().getItem(var5x).getItem())) {
                     com.larpclient.h.A(
                        this.getName(),
                        "Auto Tool: {} for {}",
                        mc.player.getInventory().getItem(var5x).getHoverName().getString(),
                        BuiltInRegistries.BLOCK.getKey(var4x.getBlock()).getPath()
                     );
                  }

                  if (var5x == var4 && this.uu.O("Auto") && this.VM == null && this.VN == null) {
                     this.F(var5);
                  }

                  return var5x;
               },
               () -> !this.B() && !this.x(var5) && this.o(var5) && this.V(var5)
            );
            this.e(!var7 ? "Waiting for a reachable face" : (var3 != null ? var3 : (var2 ? "Cutting a 3x3 face at " : "Mining ") + var5.toShortString()));
            if (!var7) {
               this.A(var5, "no reachable face");
            }
         } else {
            this.Vc = 0;
            l0.h(this);
            this.e("Standing up before the drill hit");
         }
      }
   }

   private void X(BlockPos var1, boolean var2) {
      if (var2 || !this.p.getValue()) {
         this.Vc = 0;
      } else if (this.V8) {
         this.YY = (boolean)1;
      } else {
         int var3 = mc.player.tickCount;
         if (!var1.equals(this.gu)) {
            this.gu = var1;
            if (var3 >= this.Vc && this.uQ.nextDouble() < 0.015) {
               this.Vc = var3 + this.uQ.nextInt(20, 51);
            }
         }

         if (var3 < this.Vc) {
            this.YY = (boolean)1;
         }
      }
   }

   private boolean o(BlockPos var1) {
      int var2 = mc.player.tickCount;
      if (!var1.equals(this.ug)) {
         this.ug = var1.immutable();
         this.Y7 = -1000000000;
         this.g5 = f(this.uQ.nextDouble());
      }

      if (this.Y7 == -1000000000) {
         if (l0.u(var1, this.f()) == null) {
            return false;
         }

         this.Y7 = var2;
      }

      return var2 - this.Y7 >= this.g5;
   }

   static int f(double var0) {
      return (int)Math.min(6.0, Math.floor(-Math.log(1.0 - Math.min(var0, 0.999999)) * 1.3));
   }

   private boolean V(BlockPos var1) {
      if (this.YO != null && var1.equals(this.YO)) {
         BlockHitResult var2 = l0.u(var1, this.f());
         return var2 != null && var2.getDirection() == this.gh;
      } else {
         return true;
      }
   }

   private int H() {
      BlockState var1 = Blocks.STONE.defaultBlockState();
      int var2 = this.D(var1, this.m());
      return var2 >= 0 && mc.player.getInventory().getItem(var2).isCorrectToolForDrops(var1) ? var2 : -1;
   }

   private int T(BlockState var1, int var2) {
      int var3 = this.H();
      Inventory var4 = mc.player.getInventory();
      int var5 = var3 >= 0 ? var3 : (var2 >= 0 ? var2 : var4.getSelectedSlot());
      if (!this.g9.getValue()) {
         return var5;
      }

      ArrayList var6 = new ArrayList(9);

      for (int var7 = 0; var7 < 9; var7++) {
         var6.add(var4.getItem(var7));
      }

      int var9 = Q(var6, var1, var2, var5, var3 < 0, this.y());
      int var8 = var4.getSelectedSlot();
      return var8 != var9 && var8 >= 0 && var8 < 9 && this.P((ItemStack)var6.get(var8), (ItemStack)var6.get(var9), var1, var8 == var2 && var3 >= 0)
         ? var8
         : var9;
   }

   private boolean P(ItemStack var1, ItemStack var2, BlockState var3, boolean var4) {
      if (!var1.isEmpty() && var1.isDamageableItem() && !var4 && !var1.is(ItemTags.SWORDS) && hk.T(var1) > this.y()) {
         l5.e var5 = l5.F(var1, var3, true);
         l5.e var6 = l5.F(var2, var3, true);
         return var5.usable() && var5.harvest() == var6.harvest() && var5.speed() >= var6.speed();
      } else {
         return false;
      }
   }

   static int Q(List<ItemStack> var0, BlockState var1, int var2, int var3, boolean var4, int var5) {
      ArrayList var6 = new ArrayList(var0.size());

      for (int var7 = 0; var7 < var0.size(); var7++) {
         ItemStack var8 = (ItemStack)var0.get(var7);
         l5.e var9 = l5.F(var8, var1, true);
         int var10 = var7 != var3 && (var8.isEmpty() || var8.is(ItemTags.SWORDS) || var7 == var2 && !var4 || hk.T(var8) <= var5) ? 0 : 1;
         var6.add(var10 != 0 ? var9 : new l5.e(var9.speed(), var9.harvest(), false));
      }

      return l5.H(var6, var3);
   }

   private int y() {
      return this.Vr.getValue() ? this.L.getValue().intValue() : 1;
   }

   private boolean A(int var1) {
      return var1 != this.m() && var1 != this.H();
   }

   private boolean w() {
      return this.m() >= 0 && this.H() < 0;
   }

   private boolean e() {
      if (this.uu.O("3x3")) {
         return true;
      } else {
         return this.uu.O("One Block") ? false : !Boolean.FALSE.equals(this.VM);
      }
   }

   private void A() {
      if (!this.VS && this.uu.O("Auto") && this.VM == null) {
         this.VS = (boolean)1;
         this.K(
            "blocks are left because a 3x3 drill cut would reach outside the area, and no drill hit has shown yet how your drill cuts. If it is a normal pickaxe, set Drill Cut to One Block, or add a plain pickaxe to the hotbar."
         );
         com.larpclient.h.A(this.getName(), "Drill Cut hint sent: drill-only, cut unknown, blocks left for cutting outside the area");
      }
   }

   private void F(BlockPos var1) {
      BlockHitResult var2 = l0.u(var1, this.f());
      Direction var3 = var2 != null ? var2.getDirection() : Direction.UP;
      this.VN = var1;
      this.uq.clear();
      Axis var4 = var3.getAxis();

      for (int var5 = -1; var5 <= 1; var5++) {
         for (int var6 = -1; var6 <= 1; var6++) {
            if (var5 != 0 || var6 != 0) {
               BlockPos var7 = var4 == Axis.X ? var1.offset(0, var5, var6) : (var4 == Axis.Y ? var1.offset(var5, 0, var6) : var1.offset(var5, var6, 0));
               if (l0.B(var7)) {
                  this.uq.put(var7.immutable(), mc.level.getBlockState(var7));
               }
            }
         }
      }

      this.S = 0;
      this.VF.clear();
      com.larpclient.h.U(this.getName(), "Watching the first drill hit at {} on {} ({} blocks around it)", var1.toShortString(), var3, this.uq.size());
   }

   private void X() {
      if (this.VN != null) {
         if (this.uq.isEmpty()) {
            this.VN = null;
         } else {
            if (this.S == 0) {
               if (l0.B(this.VN)) {
                  return;
               }

               this.S = mc.player.tickCount + 10;
            }

            int var1 = 0;
            int var2 = 0;

            for (BlockPos var4 : this.uq.keySet()) {
               if (!this.VF.contains(var4)) {
                  var2++;
                  if (mc.level.getBlockState(var4).isAir()) {
                     var1++;
                  }
               }
            }

            int var5 = var2 > 0 && var1 * 2 >= var2 ? 1 : 0;
            if (var5 != 0 || var2 == 0 || mc.player.tickCount >= this.S) {
               if (var5 != 0) {
                  this.VM = true;
                  this.YN = 0;
                  com.larpclient.h.A(this.getName(), "Drill check: {} of {} blocks around the hit broke, so the drill cuts 3x3", var1, var2);
                  this.K("your drill cuts 3x3 here, so it mines full 3x3 faces.");
               } else if (var2 > 0 && ++this.YN >= 3) {
                  this.VM = false;
                  com.larpclient.h.A(
                     this.getName(),
                     "Drill check: {} hits in a row broke one block ({} of {} around the last), so the drill breaks one block at a time",
                     3,
                     var1,
                     var2
                  );
                  this.K("your drill broke one block on 3 hits in a row here, so it is used like a normal pickaxe. Set Drill Cut to 3x3 if it really cuts 3x3.");
                  this.g2 = (boolean)1;
                  this.YT.removeAll(this.gM);
                  this.Yp.keySet().removeAll(this.gM);
                  this.gM.clear();
                  this.uW.clear();
               } else {
                  com.larpclient.h.A(this.getName(), "Drill check: {} of {} blocks around the hit broke; checking the next drill hit too", var1, var2);
               }

               this.VN = null;
               this.uq.clear();
               this.VF.clear();
            }
         }
      }
   }

   private boolean L(BlockPos var1, Direction var2) {
      Axis var3 = var2.getAxis();

      for (int var4 = -1; var4 <= 1; var4++) {
         for (int var5 = -1; var5 <= 1; var5++) {
            BlockPos var6 = var3 == Axis.X ? var1.offset(0, var4, var5) : (var3 == Axis.Y ? var1.offset(var4, 0, var5) : var1.offset(var4, var5, 0));
            if (!this.d(var6) && l0.B(var6)) {
               return false;
            }
         }
      }

      return true;
   }

   private boolean d(BlockPos var1) {
      return this.B.J().contains(var1.getX(), var1.getY(), var1.getZ()) && (this.YJ == null || this.YJ.contains(var1));
   }

   private boolean K(BlockPos var1, BlockHitResult var2) {
      if (var2 == null) {
         return false;
      }

      if (this.w() && this.e() && !this.L(var1, var2.getDirection())) {
         if (this.T(mc.level.getBlockState(var1), this.m()) != this.m()) {
            return true;
         }

         this.gM.add(var1.immutable());
         return false;
      } else {
         return true;
      }
   }

   private int D(BlockState var1, int var2) {
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

   private boolean z(u1.b var1) {
      if (this.uv.isEmpty()) {
         return false;
      }

      double var2 = this.f();
      Vec3 var4 = mc.player.getEyePosition();
      int var5 = this.d();

      for (BlockPos var7 : this.uv) {
         if (!mc.level.getFluidState(var7).isEmpty() && this.Y1.getOrDefault(var7, 0) < 60 && !(var4.distanceToSqr(Vec3.atCenterOf(var7)) > var2 * var2)) {
            if (var5 < 0) {
               this.I("no solid block in your hotbar to plug lava or water with");
               return false;
            }

            Item var8 = mc.player.getInventory().getItem(var5).getItem();
            BlockPos var9 = var7.immutable();
            if (t1.Q(this, var9, var8, var2, true, true) != null) {
               if (this.i("plug " + (mc.level.getFluidState(var9).is(FluidTags.LAVA) ? "lava" : "water"))) {
                  return true;
               }

               if (!this.f(var5)) {
                  this.e("Taking out " + mc.player.getInventory().getItem(var5).getHoverName().getString() + " to plug a fluid");
                  return true;
               }

               if (mc.player.tickCount < this.gO) {
                  l0.h(this);
                  this.e("Steadying before the next block");
                  return true;
               }

               boolean var10 = t1.W(
                  this, var9, var8, var2, true, true, () -> !mc.level.getFluidState(var9).isEmpty() && !this.B(), () -> this.P(var9, var8, true), var2x -> {
                     this.f(var9);
                     com.larpclient.h.U(this.getName(), "Plug at {} {}", var9.toShortString(), var2x ? "placed" : "rejected");
                  }
               );
               if (var10) {
                  if (this.Y1.merge(var9, 1, Integer::sum) == 1) {
                     com.larpclient.h.A(
                        this.getName(), "Plugging {} at {}", mc.level.getFluidState(var9).is(FluidTags.LAVA) ? "lava" : "water", var9.toShortString()
                     );
                  }

                  l0.h(this);
                  this.e("Plugging " + (mc.level.getFluidState(var9).is(FluidTags.LAVA) ? "lava" : "water") + " at " + var9.toShortString());
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean f(int var1) {
      Inventory var2 = mc.player.getInventory();
      if (var2.getSelectedSlot() != var1) {
         l0.h(this);
         var2.setSelectedSlot(var1);
         this.Yj = var1;
         this.V6 = mc.player.tickCount;
         return false;
      }

      if (this.Yj != var1) {
         this.Yj = var1;
         this.V6 = mc.player.tickCount;
      }

      return mc.player.tickCount - this.V6 >= 3;
   }

   private float L() {
      q1881b7c262e73011 var1 = (q1881b7c262e73011)mc.player;
      return (float)Math.hypot(Mth.wrapDegrees(mc.player.getYRot() - var1.larpYRotLast()), mc.player.getXRot() - var1.larpXRotLast());
   }

   private boolean P(BlockPos var1, Item var2, boolean var3) {
      int var4 = mc.player.tickCount;
      if (!var1.equals(this.u2)) {
         this.u2 = var1.immutable();
         this.Yi = 0;
         this.V1 = s(this.uQ.nextDouble());
         this.gX = -1000000000;
      }

      if (this.gX != var4) {
         this.gX = var4;
         int var5 = var3 && !(mc.player.getDeltaMovement().horizontalDistanceSqr() < 4.0E-4) ? 0 : 1;
         int var6 = var5 != 0 && this.L() <= 1.5F && t1.Q(this, var1, var2, this.f(), false, true) != null ? 1 : 0;
         this.Yi = var6 != 0 ? this.Yi + 1 : 0;
      }

      return this.Yi >= this.V1 && var4 >= this.gO;
   }

   static int s(double var0) {
      return 2 + (int)Math.round(4.0 * var0 * var0);
   }

   static int J(double var0) {
      return 8 + (int)Math.round(8.0 * var0 * var0);
   }

   private void f(BlockPos var1) {
      int var2 = mc.player.tickCount;
      this.gO = var2 + J(this.uQ.nextDouble());
      this.gU = var1.immutable();
      this.U = var2 + 5;
      this.uF.merge(var1.immutable(), 1, Integer::sum);
      this.u2 = null;
   }

   private boolean i(String var1) {
      Screen var2 = mc.gui.screen();
      if (var2 != null && (!this.allowsScreenInteractions() || var2 instanceof AbstractContainerScreen)) {
         this.e("Waiting for the menu to close to " + var1);
         return true;
      } else {
         return false;
      }
   }

   private int d() {
      Inventory var1 = mc.player.getInventory();
      int var2 = this.m();

      for (int var3 = 0; var3 < 9; var3++) {
         if (var3 != var2) {
            ItemStack var4 = var1.getItem(var3);
            if (var4.getItem() instanceof BlockItem var5 && v(var5)) {
               return var3;
            }
         }
      }

      return -1;
   }

   static boolean v(BlockItem var0) {
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

   private void I(String var1) {
      if (!var1.equals(this.Yk)) {
         this.Yk = var1;
         this.K(var1 + ".");
      }
   }

   private BlockPos A(u1.b var1) {
      if (this.d != this.gi) {
         this.Vx.clear();
         this.gi = this.d;
      }

      BlockPos var2 = null;
      BlockPos var3 = null;
      double var4 = 36.0;
      double var6 = mc.player.getX();
      double var8 = mc.player.getZ();
      int var10 = !this.g2 && this.m() >= 0 ? 1 : 0;

      for (BlockPos var12 : this.Yy) {
         double var13 = var12.getX() + 0.5 - var6;
         double var15 = var12.getZ() + 0.5 - var8;
         double var17 = var13 * var13 + var15 * var15;
         if ((var2 == null || !(var17 >= var4)) && this.L(var12, var1)) {
            boolean var19 = this.H(var12);
            if (var19 || var10 != 0) {
               if (var2 == null) {
                  var2 = var12;
               }

               if (var19 && var17 < var4 && !this.Vx.contains(var12) && this.K(var12)) {
                  var4 = var17;
                  var3 = var12;
               }
            }
         }
      }

      this.g7 = var3 != null && !var3.equals(var2) ? var3 : null;
      return var3 != null ? var3 : var2;
   }

   private boolean K(BlockPos var1) {
      for (Direction var5 : Direction.values()) {
         BlockPos var6 = var1.relative(var5);
         if (mc.level.getBlockState(var6).getCollisionShape(mc.level, var6).isEmpty()) {
            return true;
         }
      }

      return false;
   }

   private void x(BlockPos var1, u1.b var2) {
      if (!this.s(var1, var2)) {
         this.A(var1, "no way to walk within reach");
         this.F(var1, var2, "no way to walk within reach");
         this.m();
      } else {
         int var3 = !this.g2 && this.m() >= 0 && !this.H(var1) ? 1 : 0;
         BlockPos var4 = var3 != 0 ? this.I(this.B.d(var1.getX(), var1.getZ(), this.Yt), this.B.J() ? var1.getX() : var1.getZ(), var2) : var1;
         if (!var4.equals(this.F) || mc.player.tickCount - this.Ve > 40 || this.gK.isEmpty() && this.Vq == 0) {
            this.F = var4;
            this.Ve = mc.player.tickCount;
            List var5 = this.M(var2.bottom(), var3 != 0 ? this.P(var4) : this.x(var4, var2));
            if ((var5 == null || var5.isEmpty()) && var1.equals(this.g7)) {
               this.Vx.add(var1.immutable());
               this.g7 = null;
               this.m();
               return;
            }

            if (var5 == null || var5.isEmpty()) {
               if (var3 != 0) {
                  this.gW.add(this.p(var1.getX(), var1.getZ()));
                  com.larpclient.h.U(
                     this.getName(),
                     "Opening slice at {} with the pickaxe: {}",
                     var4.toShortString(),
                     var5 == null ? "no route to the row line" : "not in line"
                  );
               } else {
                  if (var5 != null
                     && this.G(mc.player.getEyePosition(), var1, this.f()) instanceof BlockPos var6
                     && this.L(var6, var2)
                     && this.K(var6, l0.L(this, var6, this.f()))) {
                     this.m();
                     this.G(var6);
                     this.uz = (boolean)0;
                     this.A(var6, false);
                     return;
                  }

                  if (var5 != null && this.gM.contains(var1)) {
                     this.A();
                  }

                  String var8 = var5 == null
                     ? "no way to walk within reach"
                     : (this.gM.contains(var1) ? "the drill would cut outside the area" : "not visible from any standing spot nearby");
                  this.A(var1, var8);
                  if (this.p() >= 2) {
                     this.F(var1, var2, var8);
                  }

                  this.YR++;
               }

               this.m();
               return;
            }

            this.gK = var5;
            this.Vq = 0;
            this.Vc = 0;
            this.O(var2, var4);
            com.larpclient.h.U(this.getName(), "Route of {} steps toward {}", this.gK.size(), var4.toShortString());
         }

         this.s(var2);
      }
   }

   private void O(u1.b var1, BlockPos var2) {
      sj.e var3 = this.gK.getLast();
      sj.e var4 = this.uE.isEmpty() ? null : this.uE.getLast();
      int var5 = var2.equals(this.uk) && var3.equals(var4) ? 1 : 0;
      this.uE = this.gK;
      this.uk = var2;
      if (var5 == 0) {
         boolean var6 = com.larpclient.gu.N(var3.x(), var1.bottom(), var3.z());
         this.uO = var6 ? 0.0 : (this.uQ.nextDouble() * 2.0 - 1.0) * 0.18;
         this.u9 = var6 ? 0.0 : (this.uQ.nextDouble() * 2.0 - 1.0) * 0.18;
         this.gA = var6 ? 0.245 : 0.15 + this.uQ.nextDouble() * 0.15;
         this.Vv = 0.35 + this.uQ.nextDouble() * 0.65;
         this.u6 = this.uQ.nextDouble() * Math.PI * 2.0;
      }
   }

   private Vec3 q(int var1, double var2) {
      sj.e var4 = this.gK.get(var1);
      int var5 = this.gK == this.uE && var1 == this.gK.size() - 1 ? 1 : 0;
      return new Vec3(var4.x() + 0.5 + (var5 != 0 ? this.uO : 0.0), var2, var4.z() + 0.5 + (var5 != 0 ? this.u9 : 0.0));
   }

   static boolean Y(Vec3 var0, Vec3 var1) {
      double var2 = Math.hypot(var0.x, var0.z);
      double var4 = Math.hypot(var1.x, var1.z);
      return !(var2 < 1.0E-6) && !(var4 < 1.0E-6) ? (var0.x * var1.x + var0.z * var1.z) / (var2 * var4) >= YQ : true;
   }

   public static void h(LocalPlayer var0) {
      if (P != null && P.isEnabled()) {
         ae var1 = P;
         if (var1 != null && var1.B != null && var0 == var1.YZ) {
            var1.Yo = var0.position();
            var1.uI = var0.getDeltaMovement().horizontalDistance();
         }
      }
   }

   public static void s(LocalPlayer var0) {
      if (P != null && P.isEnabled()) {
         ae var1 = P;
         if (var1 != null && var1.B != null && var0 == var1.YZ && var1.isRuntimeActive()) {
            Vec3 var2 = var1.Yo;
            var1.Yo = null;
            double var3 = var2 == null ? -1.0 : var2.distanceTo(var0.position());
            if (!(var3 >= 0.0) || !(var3 < 0.001)) {
               int var5 = ++var1.ut;
               if (var5 <= 30 || var5 % 10 == 0) {
                  com.larpclient.h.A(
                     var1.getName(),
                     "Server {} you {} blocks (correction {} this run) while: {}; walking {}, sprint {}, sneak {}, on ground {}, speed {}",
                     var3 > 16.0 ? "teleported" : "moved",
                     var3 < 0.0 ? "?" : String.format(Locale.ROOT, "%.2f", var3),
                     var5,
                     var1.getStatus(),
                     var1.Vj.lengthSqr() > 1.0E-6,
                     var0.isSprinting(),
                     var0.isShiftKeyDown(),
                     var0.onGround(),
                     String.format(Locale.ROOT, "%.3f", var1.uI)
                  );
               }
            }
         }
      }
   }

   private sj.q x(BlockPos var1, u1.b var2) {
      double var3 = this.f() - 0.4;
      Vec3 var5 = Vec3.atCenterOf(var1);
      double var6 = var2.bottom() + mc.player.getEyeHeight();
      return (var6x, var7) -> {
         double var8 = var6x + 0.5 - var5.x;
         double var10 = var7 + 0.5 - var5.z;
         double var12 = var6 - var5.y;
         return var8 * var8 + var12 * var12 + var10 * var10 <= var3 * var3
            && (var6x != var1.getX() || var7 != var1.getZ())
            && l0.k(new Vec3(var6x + 0.5, var6, var7 + 0.5), var1, var3);
      };
   }

   private sj.q P(BlockPos var1) {
      int var2 = this.B.d(var1.getX(), var1.getZ(), this.Yt);
      int var3 = this.B.b(var2, this.Yt);
      int var4 = this.B.p(var2, this.Yt);
      int var5 = this.B.J() ? var1.getX() : var1.getZ();
      int var6 = Math.max(1, (int)Math.floor(this.f() - 0.5));
      Direction var7 = this.N(var2);
      double var8 = var1.getY() - 1 + mc.player.getEyeHeight();
      double var10 = this.f() - 0.4;
      Vec3 var12 = Vec3.atCenterOf(var1);
      return (var12x, var13) -> {
         int var14 = this.B.J() ? var13 : var12x;
         int var15 = this.B.J() ? var12x : var13;
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

   private boolean a() {
      u1.y var1 = this.B.J();
      BlockPos var2 = this.p();
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

   private List<sj.e> M(int var1, sj.q var2) {
      u1.y var3 = this.B.J();
      BlockPos var4 = this.p();
      sj.o var5 = new sj.o(
         Math.min(var3.minX(), var4.getX()) - 2,
         Math.min(var3.minZ(), var4.getZ()) - 2,
         Math.max(var3.maxX(), var4.getX()) + 2,
         Math.max(var3.maxZ(), var4.getZ()) + 2
      );
      this.Yh = (boolean)0;
      List var6 = sj.u(
         (var3x, var4x) -> this.m(var3x, var1, var4x) || var3x == var4.getX() && var4x == var4.getZ(), var4.getX(), var4.getZ(), var2, var5, 20000
      );
      if (var6 == null && this.R.getValue()) {
         this.Yh = (boolean)1;

         try {
            return sj.u(
               (var3x, var4x) -> this.m(var3x, var1, var4x) || var3x == var4.getX() && var4x == var4.getZ(), var4.getX(), var4.getZ(), var2, var5, 20000
            );
         } finally {
            this.Yh = (boolean)0;
         }
      } else {
         return var6;
      }
   }

   private boolean Z(BlockPos var1) {
      BlockState var2 = mc.level.getBlockState(var1);
      return var2.getCollisionShape(mc.level, var1).isEmpty()
         && mc.level.getFluidState(var1).isEmpty()
         && !var2.is(Blocks.COBWEB)
         && !var2.is(Blocks.FIRE)
         && !var2.is(Blocks.SOUL_FIRE)
         && !var2.is(Blocks.SWEET_BERRY_BUSH)
         && !var2.is(Blocks.POWDER_SNOW);
   }

   private boolean M(BlockPos var1) {
      BlockState var2 = mc.level.getBlockState(var1);
      return (var2.isFaceSturdy(mc.level, var1, Direction.UP) || this.j(var2, var1)) && !var2.is(Blocks.MAGMA_BLOCK) && mc.level.getFluidState(var1).isEmpty();
   }

   private boolean j(BlockState var1, BlockPos var2) {
      VoxelShape var3 = var1.getCollisionShape(mc.level, var2);
      return l(var3);
   }

   static boolean l(VoxelShape var0) {
      if (var0.isEmpty()) {
         return false;
      }

      AABB var1 = var0.bounds();
      return var1.maxY >= 0.75 && var1.maxY <= 1.0 && var1.minX <= 0.0 && var1.maxX >= 1.0 && var1.minZ <= 0.0 && var1.maxZ >= 1.0;
   }

   static int N(double var0) {
      return Mth.floor(var0 + 0.25);
   }

   private int s() {
      return N(mc.player.getY());
   }

   private BlockPos p() {
      return new BlockPos(mc.player.getBlockX(), this.s(), mc.player.getBlockZ());
   }

   private boolean Q(BlockPos var1) {
      if (this.YJ == null && this.Yh && !this.gm.contains(var1) && this.R.getValue() && t1.t(var1) && mc.level.getFluidState(var1).isEmpty()) {
         for (Direction var5 : Direction.values()) {
            if (var5 != Direction.UP && t1.j(var1.relative(var5))) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   boolean m(int var1, int var2, int var3) {
      BlockPos var4 = new BlockPos(var1, var2, var3);
      if (!mc.level.hasChunkAt(var4)) {
         return false;
      }

      BlockPos var5 = var4.below();
      return this.Z(var4) && this.Z(var4.above()) && (this.M(var5) || this.Q(var5) && this.d() >= 0);
   }

   private void s(u1.b var1) {
      if (this.Vq >= this.gK.size()) {
         this.m();
      } else {
         Vec3 var2 = mc.player.position();
         int var3 = this.Y(var2, var1.bottom());

         while (this.Vq < var3 && this.g(var2, this.Vq, var3)) {
            this.Vq++;
         }

         int var4 = var3 > this.Vq ? 1 : 0;
         sj.e var5 = this.gK.get(this.Vq);
         Vec3 var6 = this.q(this.Vq, var2.y);
         int var7 = this.Vq == this.gK.size() - 1 ? 1 : 0;
         double var8 = this.gK == this.uE ? this.gA : 0.245;
         if (var2.distanceToSqr(var6) < (var7 != 0 ? var8 * var8 : (var4 != 0 ? 0.49 : 0.12))) {
            this.Vq++;
            if (this.Vq >= this.gK.size()) {
               this.m();
               this.e("Arrived; looking for the next block");
               return;
            }

            var5 = this.gK.get(this.Vq);
            var6 = this.q(this.Vq, var2.y);
            var7 = this.Vq == this.gK.size() - 1 ? 1 : 0;
         }

         BlockPos var10 = new BlockPos(var5.x(), var1.bottom() - 1, var5.z());
         if (var10.equals(this.gU) && mc.player.tickCount < this.U) {
            this.e("Waiting for the placed block to land");
         } else if (!this.M(var10)) {
            if (!this.v(var10)) {
               this.gm.add(var10.immutable());
               this.n("could not bridge the gap at " + var10.toShortString());
            }
         } else {
            double var11 = Math.sqrt(var2.distanceToSqr(var6));

            for (int var13 = this.Vq + 1; var13 < this.gK.size(); var13++) {
               sj.e var14 = this.gK.get(var13 - 1);
               sj.e var15 = this.gK.get(var13);
               var11 += Math.hypot(var15.x() - var14.x(), var15.z() - var14.z());
            }

            Vec3 var24 = (var4 != 0 && var3 < this.gK.size() ? this.q(var3, var2.y) : var6).subtract(var2).multiply(1.0, 0.0, 1.0);
            Vec3 var25 = mc.player.getDeltaMovement();
            double var26 = var24.lengthSqr() < 1.0E-6 ? 0.0 : var25.multiply(1.0, 0.0, 1.0).dot(var24.normalize());
            sj.e var17 = this.gK.get(this.gK.size() - 1);
            this.YY = (boolean)(var11 < 0.9 && com.larpclient.gu.N(var17.x(), var1.bottom(), var17.z()) ? 1 : 0);
            boolean var18 = this.gC.u(this.YY ? var11 : var11 - 0.1, var26, ta.E(this));
            this.Vj = var18 ? var24 : Vec3.ZERO;
            this.gY = (boolean)(var18 && this.VD.getValue() && !this.YY && var11 > 4.0 && mc.player.getFoodData().getFoodLevel() > 6 ? 1 : 0);
            if (this.F == null
               || this.gD
               || !(mc.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(this.F)) <= 64.0)
               || !(var11 < 1.5) && !Y(var24, Vec3.atCenterOf(this.F).subtract(var2))) {
               double var19 = mc.player.getEyeY() - (this.gK == this.uE ? this.Vv : 0.6) + 0.12 * Math.sin(mc.player.tickCount * 0.09 + this.u6);
               ArrayList var21 = new ArrayList(this.gK.size() - this.Vq);

               for (int var22 = this.Vq; var22 < this.gK.size(); var22++) {
                  var21.add(new Vec3(this.gK.get(var22).x() + 0.5, var19, this.gK.get(var22).z() + 0.5));
               }

               this.aimAt(com.larpclient.gu.L(new Vec3(var2.x, var19, var2.z), var21, 3.0), 12.0F);
            } else {
               this.aimAt(Vec3.atCenterOf(this.F), 12.0F);
            }

            this.J(var18);
            this.e("Walking to " + (this.F == null ? "the next block" : this.F.toShortString()));
         }
      }
   }

   private int Y(Vec3 var1, int var2) {
      if (Math.abs(var1.y - var2) > 0.6 && this.s() != var2) {
         return this.Vq;
      }

      int var3 = this.Vq;

      for (int var4 = this.Vq + 1; var4 < Math.min(this.gK.size(), this.Vq + 1 + 8); var3 = var4++) {
         sj.e var5 = this.gK.get(var4);
         if (!com.larpclient.gu.y(mc.level, var1, var5.x() + 0.5, var5.z() + 0.5, var2)) {
            break;
         }
      }

      return var3;
   }

   private boolean g(Vec3 var1, int var2, int var3) {
      sj.e var4 = this.gK.get(var2);
      sj.e var5 = this.gK.get(var3);
      return Math.hypot(var5.x() + 0.5 - var1.x, var5.z() + 0.5 - var1.z) <= Math.hypot(var5.x() - var4.x(), var5.z() - var4.z()) + 0.05;
   }

   private void J(boolean var1) {
      Vec3 var2 = mc.player.position();
      if (var1 && this.uZ != null && var2.distanceToSqr(this.uZ) < 1.0E-4) {
         this.r++;
      } else {
         this.r = 0;
      }

      this.uZ = var2;
      if (this.r > 40) {
         this.n("movement blocked");
      }
   }

   private void n(String var1) {
      if (++this.Yv >= 3 && this.F != null) {
         this.A(this.F, var1);
      }

      com.larpclient.h.U(this.getName(), "Replanning route: {}", var1);
      this.gK = List.of();
      this.Vq = 0;
      this.r = 0;
      this.Ve = -1000000000;
   }

   private boolean v(BlockPos var1) {
      int var2 = this.d();
      if (var2 >= 0 && this.R.getValue()) {
         this.YY = (boolean)1;
         if (this.i("bridge a gap")) {
            return true;
         } else if (!this.f(var2)) {
            this.e("Taking out " + mc.player.getInventory().getItem(var2).getHoverName().getString() + " to bridge a gap");
            return true;
         } else {
            Item var3 = mc.player.getInventory().getItem(var2).getItem();
            BlockPos var4 = var1.immutable();
            if (this.uF.getOrDefault(var4, 0) >= 3) {
               return false;
            } else if (mc.player.tickCount < this.gO) {
               this.e("Steadying before the next block");
               return true;
            } else {
               boolean var5 = t1.W(this, var4, var3, this.f(), true, true, () -> t1.t(var4) && !this.B(), () -> this.P(var4, var3, true), var2x -> {
                  this.f(var4);
                  com.larpclient.h.U(this.getName(), "Bridge block at {} {}", var4.toShortString(), var2x ? "placed" : "rejected");
               });
               this.e(var5 ? "Bridging a gap at " + var4.toShortString() : "Cannot reach the gap at " + var4.toShortString());
               return var5;
            }
         }
      } else {
         return false;
      }
   }

   private void b(u1.b var1) {
      if (!this.g2 && this.m() >= 0) {
         this.g2 = (boolean)1;
         if (this.J.getValue()) {
            com.larpclient.h.A(this.getName(), "Layer {}: drill faces done, sweeping leftovers", this.Yt + 1);
            this.gE = -1000000000;
            return;
         }
      }

      int var2 = 0;

      for (BlockPos var4 : this.Yy) {
         if (l0.B(var4)) {
            var2++;
         }
      }

      com.larpclient.h.A(
         this.getName(), "Layer {} of {} done; {} blocks left (held back fluid, unreachable, set aside or not swept)", this.Yt + 1, this.B.k(), var2
      );
      this.Yt++;
      this.g2 = (boolean)(this.m() < 0 ? 1 : 0);
      this.gd = (boolean)0;
      this.s = -1;
      this.u1 = null;
      this.Yy.clear();
      this.uv.clear();
      this.gE = -1000000000;
      this.YA = this.F = null;
      this.m();
      if (this.Yt >= this.B.k()) {
         this.Yt = this.B.k() - 1;
         if (this.g6.getValue()) {
            this.gD = (boolean)1;
            this.e("Picking up drops");
            com.larpclient.h.A(this.getName(), "Picking up drops in the area");
         } else {
            this.i();
         }
      } else {
         this.e("Moving down to layer " + (this.Yt + 1));
      }
   }

   private void i() {
      if (this.gq) {
         this.YJ = null;
         this.B = this.gj;
         this.b = 0;
         this.c = this.T;
      } else if (this.YJ == null && this.p()) {
         return;
      }

      u1.y var1 = this.B.J();
      int var2 = 0;
      if (this.YJ != null) {
         var2 = this.z();
      } else {
         for (int var3 = var1.minY(); var3 <= this.B.J(this.b).top(); var3++) {
            for (int var4 = var1.minX(); var4 <= var1.maxX(); var4++) {
               for (int var5 = var1.minZ(); var5 <= var1.maxZ(); var5++) {
                  if (l0.B(new BlockPos(var4, var3, var5))) {
                     var2++;
                  }
               }
            }
         }
      }

      String var15 = I(System.currentTimeMillis() - this.Yz);
      if (this.YJ != null) {
         String var17 = this.VA
            + " mined out in "
            + var15
            + ", "
            + Math.max(0, this.c - var2)
            + " blocks mined"
            + (var2 > 0 ? ", " + var2 + " left standing (next to lava or water, unreachable or put back by the server)" : "");
         this.K(var17 + ".");
         com.larpclient.h.A(this.getName(), "Block set done: {}", var17);
         if (this.O.getValue()) {
            s0.s().k("Auto Area Miner", this.VA + " mined out in " + var15 + (var2 > 0 ? ", " + var2 + " left" : ""), s0.u.m, 8000L);
         }

         this.disable();
      } else {
         int var16 = 0;
         int var18 = 0;
         int var6 = 0;
         int var7 = 0;
         BlockPos var8 = null;
         ArrayList var9 = new ArrayList();

         for (int var10 = this.B.J(this.b).top(); var10 >= var1.minY(); var10--) {
            for (int var11 = var1.minX(); var11 <= var1.maxX(); var11++) {
               for (int var12 = var1.minZ(); var12 <= var1.maxZ(); var12++) {
                  BlockPos var13 = new BlockPos(var11, var10, var12);
                  if (l0.B(var13)) {
                     String var14;
                     if (this.x(var13)) {
                        var16++;
                        var14 = "holds back a fluid";
                     } else if (this.VU.contains(var13)) {
                        var18++;
                        var14 = "put back by the server";
                     } else if (this.gM.contains(var13)) {
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

         int var19 = Math.max(0, this.c - var2);
         int var20 = this.b > 0 ? this.E(this.B.J(this.b).top()) : 0;
         String var21;
         if (var2 == 0) {
            var21 = "area cleared in " + var15 + (this.c >= 0 ? ", " + var19 + " blocks mined" : "");
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
               + (this.c >= 0 ? ", " + var19 + " blocks mined" : "")
               + ", but "
               + var2
               + " blocks are still standing: "
               + String.join("; ", var22);
         }

         if (var20 > 0) {
            var21 = var21 + ". The " + var20 + " blocks in the layers above where you started were left as they are";
         }

         this.K(var21 + ".");
         com.larpclient.h.A(this.getName(), "Done: {}", var21);
         if (!var9.isEmpty()) {
            com.larpclient.h.A(this.getName(), "Still standing: {}{}", String.join(", ", var9), var2 > var9.size() ? " and more" : "");
         }

         if (this.O.getValue()) {
            s0.s()
               .k(
                  "Auto Area Miner",
                  var2 == 0 ? "Area cleared in " + var15 : "Finished with " + var2 + " blocks still standing",
                  var2 == 0 ? s0.u.m : s0.u.p,
                  8000L
               );
         }

         this.h = (boolean)1;
         this.disable();
      }
   }

   private boolean p() {
      u1.y var1 = this.B.J();
      if (var1.volume() > 2000000L) {
         return false;
      }

      HashSet var2 = new HashSet();
      int var3 = 0;
      int var4 = this.B.J(this.b).top();

      for (int var5 = var1.maxY(); var5 >= var1.minY(); var5--) {
         for (int var6 = var1.minX(); var6 <= var1.maxX(); var6++) {
            for (int var7 = var1.minZ(); var7 <= var1.maxZ(); var7++) {
               BlockPos var8 = new BlockPos(var6, var5, var7);
               if (l0.B(var8) && !this.x(var8) && !this.VU.contains(var8)) {
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
         this.K("checking the whole area: " + var11 + " are still standing, so the miner goes back for them.");
         com.larpclient.h.A(
            this.getName(), "Final check: {} blocks still standing ({} above layer {}); mining them as a block set", var2.size(), var3, this.b + 1
         );
         this.gj = this.B;
         this.T = this.c < 0 ? -1 : this.c + var3;
         this.gq = (boolean)1;
         this.YJ = var2;
         this.VA = "the area's leftovers";
         BlockPos var12 = this.p();
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

         this.B = new u1(u1.y.of(var13, var14), this.uM.getValue().intValue(), this.VZ.getValue().intValue(), var12.getX(), var12.getZ());
         this.g2 = (boolean)1;
         this.Yt = this.b = 0;
         this.Yy.clear();
         this.uv.clear();
         this.gE = -1000000000;
         this.YA = this.F = null;
         this.VI = null;
         this.m();
         this.gD = (boolean)0;
         this.Y3 = this.YI = 0;
         this.m.clear();
         this.uW.clear();
         this.Yp.clear();
         this.YT.clear();
         this.g1.clear();
         this.uz = (boolean)0;
         this.uD.H();
         this.V3 = -1000000000;
         this.u = 0;
         this.g4.clear();
         this.V2 = this.z();
         this.q = mc.player.tickCount;
         this.e("Going back for " + var2.size() + " blocks");
         return true;
      } else {
         return false;
      }
   }

   private String y() {
      return this.Q.getValue().trim() + "|" + this.D.getValue().trim() + "|" + this.uM.getValue().intValue();
   }

   private int V() {
      u1.y var1 = this.B.J();
      if (var1.volume() > 200000L) {
         return -2;
      }

      MutableBlockPos var2 = new MutableBlockPos();

      for (int var3 = var1.maxY(); var3 >= var1.minY(); var3--) {
         for (int var4 = var1.minX(); var4 <= var1.maxX(); var4++) {
            for (int var5 = var1.minZ(); var5 <= var1.maxZ(); var5++) {
               if (l0.B(var2.set(var4, var3, var5)) && !this.x(var2)) {
                  return this.B.k(var3);
               }
            }
         }
      }

      return -1;
   }

   private int E(int var1) {
      u1.y var2 = this.B.J();
      if (var2.volume() > 200000L) {
         return 0;
      }

      int var3 = 0;
      MutableBlockPos var4 = new MutableBlockPos();

      for (int var5 = var1 + 1; var5 <= var2.maxY(); var5++) {
         for (int var6 = var2.minX(); var6 <= var2.maxX(); var6++) {
            for (int var7 = var2.minZ(); var7 <= var2.maxZ(); var7++) {
               if (l0.B(var4.set(var6, var5, var7))) {
                  var3++;
               }
            }
         }
      }

      return var3;
   }

   private boolean w(BlockPos var1) {
      u1.y var2 = this.B.J();
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

   private void h() {
      l0.h(this);
      u1.y var1 = this.B.J();
      int var2 = this.s();
      AABB var3 = new AABB(var1.minX() - 1, var2 - 1, var1.minZ() - 1, var1.maxX() + 2, var2 + 2, var1.maxZ() + 2);
      List var4 = mc.level.getEntitiesOfClass(ItemEntity.class, var3, var1x -> var1x.isAlive() && !this.m.contains(var1x.getId()));
      if (!var4.isEmpty() && ++this.Y3 <= 1200) {
         Vec3 var5 = mc.player.position();
         ItemEntity var6 = var4.stream().min(Comparator.comparingDouble(var1x -> var1x.position().distanceToSqr(var5))).orElseThrow();
         BlockPos var7 = BlockPos.containing(var6.position());
         if (var7.getX() == mc.player.getBlockX() && var7.getZ() == mc.player.getBlockZ()) {
            this.k(Vec3.atBottomCenterOf(var7));
            if (++this.YI > 30) {
               this.m.add(var6.getId());
               this.YI = 0;
            }

            this.e("Picking up drops (" + var4.size() + " left)");
         } else {
            this.YI = 0;
            if (this.gK.isEmpty() || this.F == null || !this.F.equals(var7)) {
               List var8 = this.M(var2, (var1x, var2x) -> var1x == var7.getX() && var2x == var7.getZ());
               if (var8 == null || var8.isEmpty()) {
                  this.m.add(var6.getId());
                  this.m();
                  return;
               }

               this.gK = var8;
               this.Vq = 0;
               this.F = var7;
            }

            this.P(var2);
            this.e("Picking up drops (" + var4.size() + " left)");
         }
      } else {
         if (!var4.isEmpty()) {
            com.larpclient.h.A(this.getName(), "Left {} drops after {} ticks of picking up", var4.size(), 1200);
         }

         this.i();
      }
   }

   private BlockPos q(int var1) {
      int[] var2 = this.B.E(0, var1);
      int var3 = (var2[0] + var2[1]) / 2;
      u1.y var4 = this.B.J();
      int var5 = this.B.b(0, var1) > 0 ? (this.B.J() ? var4.minX() : var4.minZ()) : (this.B.J() ? var4.maxX() : var4.maxZ());
      return this.B.J() ? new BlockPos(var5, 0, var3) : new BlockPos(var3, 0, var5);
   }

   private void T(u1.b var1) {
      if (!this.K() || this.ud || !this.VE.stream().anyMatch(var0 -> var0.kind() == wb.r.h) || !this.g(var1)) {
         BlockPos var2 = this.p();
         BlockPos var3 = var2.below();
         if (this.u1 == null) {
            BlockPos var4 = this.q(this.Yt);
            int var5 = var2.getY();
            List var6 = this.M(var5, (var4x, var5x) -> var4x == var4.getX() && var5x == var4.getZ() && this.z(new BlockPos(var4x, var5 - 1, var5x), var1));
            if (var6 == null) {
               var6 = this.M(var5, (var3x, var4x) -> this.B.J().containsColumn(var3x, var4x) && this.z(new BlockPos(var3x, var5 - 1, var4x), var1));
            }

            if (var6 == null) {
               this.T("there is no safe place to dig down into layer " + (this.Yt + 1) + "; stand on top of the area");
               return;
            }

            sj.e var7 = var6.isEmpty() ? new sj.e(var2.getX(), var2.getZ()) : (sj.e)var6.getLast();
            this.u1 = new BlockPos(var7.x(), var5, var7.z());
            this.gK = var6;
            this.Vq = 0;
            this.F = null;
            com.larpclient.h.U(this.getName(), "Digging down to layer {} at {}", this.Yt + 1, this.u1.toShortString());
         }

         if (var2.getX() == this.u1.getX() && var2.getZ() == this.u1.getZ()) {
            this.m();
            if (!this.z(var3, var1)) {
               this.u1 = null;
               this.e("Looking for a safe place to dig down");
            } else {
               Vec3 var8 = Vec3.atBottomCenterOf(var2);
               if (mc.player.position().multiply(1.0, 0.0, 1.0).distanceToSqr(var8.multiply(1.0, 0.0, 1.0)) > 0.03) {
                  this.k(var8);
                  this.e("Lining up to dig down");
               } else if (!l0.B(var3)) {
                  this.e("Waiting to drop into layer " + (this.Yt + 1));
               } else {
                  this.G(var3);
                  this.uz = (boolean)0;
                  this.Z(var3, false, "Digging down to layer " + (this.Yt + 1));
                  this.u1 = var2.below();
               }
            }
         } else if (this.gK.isEmpty()) {
            this.u1 = null;
            this.m();
         } else {
            this.P(var2.getY());
         }
      }
   }

   private void P(int var1) {
      u1.b var2 = new u1.b(this.Yt, var1, var1);
      this.s(var2);
   }

   private boolean z(BlockPos var1, u1.b var2) {
      if (this.B.J().contains(var1.getX(), var1.getY(), var1.getZ()) && mc.level.hasChunkAt(var1) && !this.g(var1)) {
         if (this.w() && this.e() && l0.B(var1) && !this.L(var1, Direction.UP)) {
            return false;
         }

         if (!l0.B(var1) && !mc.level.getBlockState(var1).getCollisionShape(mc.level, var1).isEmpty()) {
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
               return var8 + 1 >= var2.bottom() && com.larpclient.u1.W(var7, var8, 3) && !var10.is(Blocks.MAGMA_BLOCK);
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private void k(Vec3 var1) {
      Vec3 var2 = mc.player.position();
      Vec3 var3 = var1.subtract(var2).multiply(1.0, 0.0, 1.0);
      double var4 = var3.length();
      double var6 = var4 < 1.0E-6 ? 0.0 : mc.player.getDeltaMovement().multiply(1.0, 0.0, 1.0).dot(var3.normalize());
      this.YY = (boolean)1;
      boolean var8 = this.gC.u(var4, var6, ta.E(this));
      this.Vj = var8 ? var3 : Vec3.ZERO;
   }

   private void v(u1.b var1) {
      BlockPos var2 = this.p();
      if (!this.R.getValue()) {
         this.T("you are below layer " + (this.Yt + 1) + "; turn on Place Blocks to pillar up, or climb back up");
      } else if (this.d() < 0) {
         this.T("you are below layer " + (this.Yt + 1) + " and there is no solid block in your hotbar to pillar up with");
      } else {
         BlockPos var3 = var2.above(2);
         if (this.Z(var3)) {
            Vec3 var4 = Vec3.atBottomCenterOf(var2);
            if (mc.player.position().multiply(1.0, 0.0, 1.0).distanceToSqr(var4.multiply(1.0, 0.0, 1.0)) > 0.03) {
               this.k(var4);
               this.e("Lining up to pillar up");
            } else {
               l0.h(this);
               if (!this.i("pillar up")) {
                  if (!this.f(this.d())) {
                     this.e("Taking out a block to pillar up");
                  } else if (mc.player.getXRot() < 84.0F || this.L() > 1.5F) {
                     this.j();
                     this.e("Looking down to pillar up");
                  } else if (mc.player.tickCount < this.gO) {
                     this.j();
                     this.e("Steadying before the next block");
                  } else {
                     this.gk = 86.0F + (float)this.uQ.nextDouble() * 3.0F;
                     this.Ya = var2.immutable();
                     this.uR = mc.player.tickCount;
                     this.t = (boolean)0;
                     this.Vi = (boolean)1;
                     this.e("Pillaring up to layer " + (this.Yt + 1));
                  }
               }
            }
         } else {
            if (this.B.J().contains(var3.getX(), var3.getY(), var3.getZ()) && l0.B(var3) && !this.x(var3)) {
               this.G(var3);
               this.uz = (boolean)0;
               this.Z(var3, false, "Clearing headroom to pillar up");
            } else {
               this.T("there is no room to pillar up back to layer " + (this.Yt + 1));
            }
         }
      }
   }

   private void p() {
      this.j();
      this.YY = (boolean)0;
      int var1 = this.d();
      if (!this.t && var1 >= 0 && !mc.player.onGround() && mc.player.getY() >= this.Ya.getY() + 1.05 && mc.player.tickCount >= this.gO && t1.t(this.Ya)) {
         Item var2 = mc.player.getInventory().getItem(var1).getItem();
         BlockPos var3 = this.Ya;
         t1.W(
            this,
            var3,
            var2,
            this.f(),
            true,
            true,
            () -> t1.t(var3) && mc.player.getY() >= var3.getY() + 1,
            () -> ((q1881b7c262e73011)mc.player).larpYLast() >= var3.getY() + 1.02 && this.L() <= 1.5F,
            var2x -> {
               this.f(var3);
               if (var2x) {
                  this.t = (boolean)1;
               }
            }
         );
      }

      if (mc.player.onGround() && mc.player.tickCount - this.uR > 2) {
         int var4 = this.s() > this.Ya.getY() ? 1 : 0;
         if (var4 == 0 && ++this.uG >= 5) {
            this.Ya = null;
            this.T("pillaring up failed five times");
         } else {
            if (var4 != 0) {
               this.uG = 0;
            }

            this.Ya = null;
         }
      } else {
         if (mc.player.tickCount - this.uR > 30) {
            this.Ya = null;
            this.uG++;
         }
      }
   }

   private void j() {
      if (ta.E(this)) {
         float[] var1 = this.gC.c(mc.player.getYRot(), mc.player.getXRot(), mc.player.getYRot(), this.gk, mc.player.getXRot() >= this.gk - 1.5F, true);
         j6.U(this, j6.l.o, var1[0], var1[1], 1.0);
      }
   }

   @Override
   protected boolean holdsToolBetweenBlocks() {
      return true;
   }

   @Override
   public boolean aimsAtOpenFaces(BlockPos var1) {
      if (this.K()) {
         return true;
      } else if (this.B != null && this.Yt < this.B.k() && !this.g2 && this.m() >= 0 && !this.H(var1)) {
         int var2 = this.B.d(var1.getX(), var1.getZ(), this.Yt);
         return !var1.equals(this.I(var2, this.B.J() ? var1.getX() : var1.getZ(), this.B.J(this.Yt)));
      } else {
         return true;
      }
   }

   @Override
   public boolean aimsAtFaceCentre(BlockPos var1) {
      return var1.equals(this.YO);
   }

   @Override
   public void digFinished(BlockPos var1) {
      if (this.B != null && mc.player != null) {
         this.ga.v(var1.asLong());
         if (this.ua.size() < 256) {
            this.ua.put(var1.immutable(), mc.player.tickCount);
         }
      }
   }

   @Override
   public boolean aimFaceAllowed(BlockPos var1, Direction var2) {
      if (this.YO != null && var1.equals(this.YO)) {
         return var2 == this.gh;
      }

      if (this.B != null && this.w() && this.e()) {
         if (this.T(mc.level.getBlockState(var1), this.m()) == this.m() && !this.L(var1, var2)) {
            this.gM.add(var1.immutable());
            return false;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private boolean B(BlockPos var1) {
      double var2 = this.f();
      return mc.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var1)) <= var2 * var2;
   }

   private boolean X() {
      for (BlockPos var2 : this.gG) {
         if (l0.B(var2)) {
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
         return !var1.equals(this.YA) && !this.g1.containsKey(var1) ? mc.player.tickCount >= this.uY || !this.gG.contains(var1) : false;
      }
   }

   @Override
   public boolean waitsForSentLook() {
      return true;
   }

   @Override
   protected void aimAt(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         if (ta.E(this)) {
            if (this.gw && this.YA != null && this.YA.equals(this.YO)) {
               var1 = w(mc.player.getEyePosition(), this.YO, this.gh, this.uA, this.Yn);
            }

            float[] var3 = j6.s(mc.player.getEyePosition(), var1);
            BlockHitResult var4 = this.YA == null ? null : l0.u(this.YA, this.f());
            int var5 = var4 == null || this.YO != null && this.YA.equals(this.YO) && var4.getDirection() != this.gh ? 0 : 1;
            float[] var6 = this.gC.Q(mc.player.getYRot(), mc.player.getXRot(), var3[0], var3[1], (boolean)var5);
            j6.U(this, j6.l.o, var6[0], var6[1], 1.0);
         }
      }
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         float[] var3 = this.gC.c(mc.player.getYRot(), mc.player.getXRot(), var1, var2, false, true);
         j6.U(this, j6.l.o, var3[0], var3[1], 1.0);
      }
   }

   public static Input r() {
      if (P != null && P.isEnabled()) {
         ae var0 = P;
         Minecraft var1 = Minecraft.getInstance();
         if (var0 != null
            && var0.isRuntimeActive()
            && var0.B != null
            && var1.player != null
            && var1.player == var0.YZ
            && var1.player.tickCount == var0.un
            && !p_.V()
            && !cj.b()
            && !(var1.gui.screen() instanceof AbstractContainerScreen)
            && (!j6.j() || j6.g(j6.l.o))) {
            int var2 = var0.Vj.lengthSqr() > 1.0E-6 ? 1 : 0;
            if (var2 == 0 && !var0.Vi && !var0.YY) {
               return null;
            }

            Input var3 = var2 != 0 ? var0.gC.e(var1.player.getYRot(), var0.Vj) : new Input(false, false, false, false, false, false, false);
            return new Input(var3.forward(), var3.backward(), var3.left(), var3.right(), var0.Vi, var0.YY, var0.gY);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void K(String var1) {
      if (mc != null && mc.player != null) {
         mc.player.sendSystemMessage(Component.literal("Larp Auto Area Miner: " + var1));
      }
   }

   private void T(String var1) {
      com.larpclient.h.A(this.getName(), "Stopped: {}", var1);
      this.K("stopped, " + var1 + ".");
      s0.s().V("Auto Area Miner stopped", var1.substring(0, 1).toUpperCase(Locale.ROOT) + var1.substring(1), s0.u.p);
      this.e("Stopped: " + var1);
      this.disable();
   }

   private void P() {
      if (this.isInGame()) {
         if (this.isRuntimeActive() && this.B != null && mc.level == this.ux) {
            try {
               int var1 = ho.B(0.25);
               if (this.uN.getValue()) {
                  u1.y var2 = this.B.J();
                  Gizmos.cuboid(
                        new AABB(var2.minX(), var2.minY(), var2.minZ(), var2.maxX() + 1, var2.maxY() + 1, var2.maxZ() + 1),
                        GizmoStyle.strokeAndFill(com.larpclient.uh.H(var1, 0.9), 1.5F, 0)
                     )
                     .setAlwaysOnTop();
                  if (this.Yt < this.B.k()) {
                     u1.b var3 = this.B.J(this.Yt);
                     Gizmos.cuboid(
                        new AABB(var2.minX(), var3.bottom(), var2.minZ(), var2.maxX() + 1, var3.top() + 1, var2.maxZ() + 1),
                        GizmoStyle.strokeAndFill(com.larpclient.uh.H(var1, 0.5), 1.0F, com.larpclient.uh.H(var1, 0.06))
                     );
                  }

                  if (this.YA != null) {
                     Gizmos.cuboid(
                           new AABB(this.YA).inflate(0.01),
                           GizmoStyle.strokeAndFill(com.larpclient.uh.H(this.uz ? -14249 : var1, 1.0), 2.0F, com.larpclient.uh.H(this.uz ? -14249 : var1, 0.18))
                        )
                        .setAlwaysOnTop();
                  }
               }

               if (this.uH.getValue() && this.Vq < this.gK.size()) {
                  double var9 = mc.player.getY() + 0.05;
                  Vec3 var4 = new Vec3(mc.player.getX(), var9, mc.player.getZ());

                  for (int var5 = this.Vq; var5 < this.gK.size(); var5++) {
                     sj.e var6 = this.gK.get(var5);
                     Vec3 var7 = new Vec3(var6.x() + 0.5, var9, var6.z() + 0.5);
                     Gizmos.line(var4, var7, com.larpclient.uh.H(var1, 0.85), 2.0F).setAlwaysOnTop();
                     var4 = var7;
                  }
               }
            } catch (RuntimeException var8) {
               com.larpclient.h.w(this.getName(), "Area overlay failed", var8);
            }
         } else {
            this.t();
         }
      }
   }

   private void t() {
      if (this.Vy) {
         this.N();
      } else if (System.currentTimeMillis() <= this.o) {
         u1.y var1 = this.h();
         if (var1 != null) {
            try {
               int var2 = ho.B(0.25);
               Gizmos.cuboid(
                     new AABB(var1.minX(), var1.minY(), var1.minZ(), var1.maxX() + 1, var1.maxY() + 1, var1.maxZ() + 1),
                     GizmoStyle.strokeAndFill(com.larpclient.uh.H(var2, 0.95), 2.0F, com.larpclient.uh.H(var2, 0.08))
                  )
                  .setAlwaysOnTop();

               for (String var6 : new String[]{this.Q.getValue(), this.D.getValue()}) {
                  int[] var7 = com.larpclient.u1.K(var6);
                  if (var7 != null) {
                     Gizmos.cuboid(
                           new AABB(var7[0], var7[1], var7[2], var7[0] + 1, var7[1] + 1, var7[2] + 1).inflate(0.02),
                           GizmoStyle.strokeAndFill(-1, 2.0F, com.larpclient.uh.H(var2, 0.35))
                        )
                        .setAlwaysOnTop();
                  }
               }
            } catch (RuntimeException var8) {
               com.larpclient.h.w(this.getName(), "Area preview failed", var8);
            }
         }
      }
   }

   private void N() {
      try {
         int var1 = ho.B(0.25);
         int[] var2 = com.larpclient.u1.K(this.Q.getValue());
         int[] var3 = com.larpclient.u1.K(this.D.getValue());
         int[] var4 = this.y == null ? null : new int[]{this.y.getX(), this.y.getY(), this.y.getZ()};
         int[] var5 = var2 != null ? var2 : var4;
         int[] var6 = var3 != null ? var3 : var4;
         if (var5 != null && var6 != null) {
            u1.y var7 = u1.y.of(var5, var6);
            Gizmos.cuboid(
                  new AABB(var7.minX(), var7.minY(), var7.minZ(), var7.maxX() + 1, var7.maxY() + 1, var7.maxZ() + 1),
                  GizmoStyle.strokeAndFill(com.larpclient.uh.H(var1, 0.95), 2.0F, com.larpclient.uh.H(var1, 0.08))
               )
               .setAlwaysOnTop();
         }

         for (int[] var10 : new int[][]{var2, var3}) {
            if (var10 != null) {
               Gizmos.cuboid(
                     new AABB(var10[0], var10[1], var10[2], var10[0] + 1, var10[1] + 1, var10[2] + 1).inflate(0.02),
                     GizmoStyle.strokeAndFill(-1, 2.0F, com.larpclient.uh.H(var1, 0.35))
                  )
                  .setAlwaysOnTop();
            }
         }

         if (this.y != null) {
            Gizmos.cuboid(new AABB(this.y).inflate(0.03), GizmoStyle.strokeAndFill(-14249, 2.0F, 872400983)).setAlwaysOnTop();
         }
      } catch (RuntimeException var11) {
         com.larpclient.h.w(this.getName(), "Corner picking overlay failed", var11);
      }
   }

   private int a() {
      if (this.YJ != null) {
         return this.z();
      }

      u1.y var1 = this.B == null ? this.h() : this.B.J();
      if (var1 != null && var1.volume() <= 200000L) {
         int var2 = 0;
         MutableBlockPos var3 = new MutableBlockPos();
         int var4 = this.B == null ? var1.maxY() : this.B.J(this.b).top();

         for (int var5 = var1.minY(); var5 <= var4; var5++) {
            for (int var6 = var1.minX(); var6 <= var1.maxX(); var6++) {
               for (int var7 = var1.minZ(); var7 <= var1.maxZ(); var7++) {
                  if (l0.B(var3.set(var6, var5, var7))) {
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

   private String u() {
      StringBuilder var1 = new StringBuilder("Layer ").append(Math.min(this.Yt + 1, this.B.k())).append(" of ").append(this.B.k());
      if (this.c > 0 && this.V2 >= 0) {
         var1.append(", ").append(this.j()).append("% done");
      }

      var1.append(", ").append(I(System.currentTimeMillis() - this.Yz));
      return var1.toString();
   }

   private int j() {
      return this.c > 0 && this.V2 >= 0 ? Math.clamp(Math.round(100.0 * (this.c - this.V2) / this.c), 0, 100) : 0;
   }

   static String I(long var0) {
      return s1.h(var0);
   }

   @Override
   public s1.u hudCard() {
      if (!this.isInGame()) {
         return null;
      }

      if (this.YU.getValue() && this.isRuntimeActive() && this.B != null && mc.level == this.ux) {
         int var6 = this.c > 0 && this.V2 >= 0 ? 1 : 0;
         String var7 = this.getStatus();
         return new s1.u(
            "Layer " + Math.min(this.Yt + 1, this.B.k()) + " of " + this.B.k(),
            var6 != 0 ? this.j() + "%" : "",
            var7 != null && !var7.isBlank() ? var7 : "Starting",
            I(System.currentTimeMillis() - this.Yz),
            var6 != 0 ? Math.clamp((double)(this.c - this.V2) / this.c, 0.0, 1.0) : -1.0
         );
      }

      if (!this.Vy) {
         return null;
      }

      u1.y var1 = this.h();
      int var2 = (this.gP[0] ? 1 : 0) + (this.gP[1] ? 1 : 0);
      FreecamModule var3 = this.t();
      double var4 = var3 == null ? 0.0 : var3.V();
      return new s1.u(
         var1 == null ? "Choose the area" : var1.describe(),
         var2 + " of 2",
         var2 == 2 ? "Esc to finish, scroll changes camera speed" : "Left click corner 1, right click corner 2",
         var4 > 0.0 ? Math.round(var4) + " b/s" : "",
         var2 / 2.0
      );
   }

   public u1 F() {
      return this.B;
   }

   public int U() {
      return this.Yt;
   }

   public String q() {
      return this.um.name();
   }

   private enum g {
      i,
      l,
      G;
   }
}

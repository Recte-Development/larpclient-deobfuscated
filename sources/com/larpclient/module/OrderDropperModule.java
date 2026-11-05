package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._3;
import com.larpclient._8;
import com.larpclient._d;
import com.larpclient._m;
import com.larpclient.cc;
import com.larpclient.kx;
import com.larpclient.m9;
import com.larpclient.mt;
import com.larpclient.p1;
import com.larpclient.te;
import com.larpclient.tj;
import com.larpclient.tq;
import com.larpclient.ty;
import com.larpclient.xh;
import com.larpclient.zc;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.TextGizmo.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class OrderDropperModule extends Module {
   public static final String x = "Drop Only";
   public static final String sy = "Fill Shulkers";
   private static final int s8 = 20;
   private static final int H = 60;
   private static final int B = 60;
   private static final int sU = 120;
   private static final int e = 50;
   private static final int so = 3;
   private static final int s3 = 3;
   private static final int st = -13180806;
   private static final int b = -11702;
   private static final int h = -2061766;
   private static final int a = -1876386;
   private static final int o = -854792;
   private static final int U = -1017711;
   private static final double sm = 24.0;
   private static final double s1 = 10.0;
   private static final int sb = 32;
   private static final long sd = 450000000L;
   private static final long C = 2000000000L;
   private static final long A = 400000000L;
   private static final double s7 = 0.012;
   private static OrderDropperModule k;
   private final d6 D = this.addSetting(
      new d6(
         "Mode",
         "Drop Only drops every page where you stand. Fill Shulkers drops a page at your feet, picks it up, stores it in the selected shulkers and walks back, until the orders are empty",
         "Fill Shulkers",
         "Drop Only",
         "Fill Shulkers"
      )
   );
   private final ds sY = this.addSetting(
         new ds("Order Item", "Item whose orders are emptied, such as totem_of_undying. Use the held-item button to fill it in", "")
      )
      .C("totem_of_undying");
   private final dv O = this.addSetting(
      new dv(
         "Select Shulker Key",
         "Hold and sweep your crosshair over shulker boxes to select them; a sweep that starts on a selected box removes instead. Tap it for click selection: right-click adds, left-click removes, sneak-click covers an area",
         -1
      )
   );
   private final d9 sv = this.addSetting(new d9("Click Delay", "Ticks between menu clicks and item moves", 4.0, 2.0, 20.0, 1.0));
   private final dh sc = this.addSetting(
      new dh("Stop When Hurt", "Stop at once when you take damage. Menus block Auto Totem, so this protects you while it works", true)
   );
   private final dh sz = this.addSetting(new dh("Show Messages", "Report orders, pages, trips and the final summary in chat", true));
   private final d9 L = this.addSetting(
      new d9("Fill Delay", "Ticks between shulker transfers. Lower is faster. Reopens the box to verify the result before leaving", 2.0, 1.0, 20.0, 1.0)
   );
   private final dh sh = this.addSetting(
      new dh("AFK Mode", "Release the mouse and keep running when you switch windows. Your pause-on-focus-loss preference is restored when stopped", false)
   );
   private final d6 p = this.addSetting(
      new d6(
         "Highlight Style",
         "Material worn by selected shulkers, shared with Self Visuals. Colors follow each box's state",
         "Camo",
         "Camo",
         "Glow",
         "Metallic",
         "Marble",
         "Glitter",
         "Solid",
         "Ghost"
      )
   );
   private final d9 s2 = this.addSetting(new d9("Highlight Opacity", "How strongly the material covers selected shulkers", 0.85, 0.2, 1.0, 0.05));
   private final List<BlockPos> sJ = new ArrayList<>();
   private final Set<BlockPos> sa = new HashSet<>();
   private final Map<BlockPos, Integer> sP = new HashMap<>();
   private final Set<BlockPos> Z = new HashSet<>();
   private final Map<BlockPos, Object> sn = new HashMap<>();
   private Level sp;
   private boolean j;
   private boolean s0;
   private Boolean Cy;
   private BlockPos s5;
   private int CV;
   private long sC;
   private final Map<BlockPos, OrderDropperModule.k> sE = new HashMap<>();
   private boolean u;
   private boolean V;
   private boolean f;
   private boolean sk;
   private boolean sX;
   private boolean Ci;
   private BlockPos C9;
   private BlockPos Y;
   private BlockPos sW;
   private long sK;
   private final List<double[]> l = new ArrayList<>();
   private final List<Integer> Cl = new ArrayList<>();
   private long sN = Long.MIN_VALUE;
   private String r;
   private Item s_;
   private Object sI;
   private Object si;
   private Object sB;
   private OrderDropperModule.b i;
   private String sO = "";
   private String ss = "";
   private String d = "Idle";
   private Vec3 C6;
   private float g;
   private float sR;
   private float CA;
   private final Set<String> m = new HashSet<>();
   private final Set<Integer> z = new HashSet<>();
   private boolean sA;
   private boolean s9;
   private boolean s4;
   private boolean sQ;
   private int s6;
   private int G;
   private int sG;
   private int sl;
   private int sH;
   private int C2;
   private int su;
   private int CS;
   private int y;
   private OrderDropperModule.u M;
   private int T;
   private int J;
   private int S;
   private AbstractContainerMenu se;
   private AbstractContainerMenu sr;
   private int sZ;
   private int W;
   private int P;
   private int K;
   private BlockPos X;
   private double R;
   private int sf;
   private int q;
   private int E;
   private int sg;
   private boolean I;
   private boolean sM;
   private boolean Cj;
   private int n;
   private int sw;
   private int v;
   private int sD;
   private boolean w;
   private boolean sj;
   private boolean sF;
   private boolean t;
   private boolean sq;
   private Vec3 sx;
   private final Set<Vec3> F = new HashSet<>();
   private boolean sT;
   private boolean N;
   private int s;
   private int c;
   private int sL;
   private int Q;
   private int sS;
   private int Cv;
   private static final Direction[][] sV = new Direction[6][];

   public OrderDropperModule() {
      super("Order Dropper", "Empty your DonutSMP orders for one item by dropping pages, or fill selected shulkers with them", Category.PLAYER, -1);
      k = this;
      this.L.H(this::J);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var1 -> this.s());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.P());
   }

   public List<df<?>> e() {
      return List.of(this.D, this.sY);
   }

   public List<df<?>> E() {
      return List.of(this.O, this.L, this.p, this.s2);
   }

   public List<df<?>> L() {
      return List.of(this.sv, this.sh, this.sc, this.sz);
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.Z;
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.d : (this.w ? "Paused" : "Idle");
   }

   private boolean J() {
      return this.D.S("Fill Shulkers");
   }

   public String u() {
      String var1 = zc.t(this.sY.P());
      String var2 = var1.isEmpty() ? "No order item chosen" : "Item: " + var1;
      int var3 = this.t().size();
      return var2
         + ". "
         + var3
         + (var3 == 1 ? " shulker" : " shulkers")
         + " selected"
         + (var3 == 0 ? "" : ", " + this.sP.size() + " scanned, " + this.sa.size() + " full, " + this.Z.size() + " unavailable")
         + (this.w ? ". Progress saved for this world session." : ".");
   }

   public void C() {
      if (!this.isInGame()) {
         throw new IllegalArgumentException("Join a world first.");
      }

      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before changing shulkers.");
      }

      mc.gui.setScreen(null);
      this.l();
   }

   public void f() {
      if (this.isInGame() && !mc.player.getMainHandItem().isEmpty()) {
         this.sY.v(q(mc.player.getMainHandItem()));
         LarpClient var1 = LarpClient.F();
         if (var1 != null && var1.f() != null) {
            var1.f().f();
         }
      } else {
         throw new IllegalArgumentException("Hold the order's item first.");
      }
   }

   public void E() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before changing shulkers.");
      }

      this.x();
      this.sJ.clear();
      this.Y = null;
   }

   public void x() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before resetting progress.");
      }

      this.D();
      this.Z();
      this.sP.clear();
      this.sa.clear();
      this.Z.clear();
      this.sn.clear();
      this.w = (boolean)0;
   }

   public void i() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before rescanning shulkers.");
      }

      this.sP.clear();
      this.sa.clear();
      this.Z.clear();
      this.sn.clear();
   }

   private List<BlockPos> t() {
      Minecraft var1 = getMc();
      return var1 != null && var1.level == this.sp ? this.sJ : List.of();
   }

   @Override
   public void onEnable() {
      if (this.isInGame() && mc.player.connection != null) {
         if (this.u) {
            this.I();
         }

         String var1 = zc.t(this.sY.P());
         int var2 = this.w && this.sI == mc.player && this.si == mc.level && this.sB == mc.player.connection && this.sO.equals(var1) && this.J() ? 1 : 0;
         if (var2 == 0) {
            this.Z();
            this.w = (boolean)0;
         }

         this.sO = var1;
         if (this.sO.isEmpty() || !BuiltInRegistries.ITEM.containsKey(Identifier.tryParse(this.sO))) {
            this.W("Choose the Order Item first, for example totem_of_undying, or hold it and use the held-item button.");
         } else if (this.J() && !((Item)BuiltInRegistries.ITEM.getValue(Identifier.parse(this.sO))).canFitInsideContainerItems()) {
            this.W("This item cannot go inside a shulker box. Choose Drop Only for shulker box orders.");
         } else if (this.J() && this.t().isEmpty()) {
            this.W("Select at least one placed shulker box first: look at it and press the Select Shulker Key.");
         } else {
            this.sI = mc.player;
            this.si = mc.level;
            this.sB = mc.player.connection;
            if (var2 == 0) {
               this.C6 = mc.player.position();
               this.g = mc.player.getYRot();
               this.sR = mc.player.getXRot();

               for (int var3 = 0; var3 < 36; var3++) {
                  if (this.T(mc.player.getInventory().getItem(var3))) {
                     this.z.add(var3);
                  }
               }
            }

            this.CA = this.i();
            if (var2 != 0 && this.sF) {
               this.sA = (boolean)0;
               this.sF = (boolean)0;
            }

            this.w = this.J();
            this.Z.clear();
            this.u();
            this.sj = (boolean)(this.J() && this.t().stream().anyMatch(var1x -> !this.sP.containsKey(var1x) && !this.Z.contains(var1x)) ? 1 : 0);
            if (this.sj) {
               this.p();
            } else if (var2 != 0 && this.b()) {
               this.p();
            } else {
               this.V(var2 != 0 ? OrderDropperModule.b.S : OrderDropperModule.b.O);
            }

            this.d(
               this.J()
                  ? (var2 != 0 ? "Resumed. " : "Started. ") + "Emptying " + this.sO + " orders into " + this.t().size() + " shulkers."
                  : "Started. Dropping every page of your " + this.sO + " orders here."
            );
            mt.K("OrderDropper", "Started mode={} item={} shulkers={} start={}", this.D.P(), this.sO, this.t().size(), this.C6);
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.sI != null
         && this.isInGame()
         && mc.player == this.sI
         && mc.player.containerMenu != mc.player.inventoryMenu
         && mc.player.containerMenu.getCarried().isEmpty()
         && this.O()) {
         mc.player.closeContainer();
      }

      if (this.X != null && (this.i == OrderDropperModule.b.q || this.sT || this.Q > 0)) {
         this.sP.remove(this.X);
      }

      this.D();
      this.i = null;
      this.I = this.sM = this.Cj = (boolean)0;
      this.M = null;
      this.sG = 0;
      this.s4 = this.sQ = (boolean)0;
      this.s6 = this.sH = this.C2 = this.su = this.CS = 0;
      this.X = null;
      this.sx = null;
      this.F.clear();
      this.K();
   }

   private void Z() {
      this.sI = this.si = this.sB = null;
      this.i = null;
      this.C6 = null;
      this.d = "Idle";
      this.m.clear();
      this.z.clear();
      this.sA = this.s9 = this.s4 = this.sQ = (boolean)0;
      this.s6 = this.G = this.sG = this.sl = this.sH = this.C2 = this.su = this.CS = this.y = 0;
      this.M = null;
      this.S = 0;
      this.X = null;
      this.ss = "";
      this.sf = this.q = this.E = this.sg = 0;
      this.I = this.sM = this.Cj = (boolean)0;
      this.n = this.sw = this.v = this.sD = 0;
      this.sj = this.sT = this.N = this.sF = (boolean)0;
      this.sx = null;
      this.s = this.c = this.sL = this.Q = this.sS = this.Cv = 0;
      this.F.clear();
      this.K();
   }

   @Override
   public void onTick() {
      this.I = this.sM = this.Cj = (boolean)0;
      if (this.i != null && this.isInGame() && mc.player.connection != null) {
         if (mc.player == this.sI && mc.level == this.si && mc.player.connection == this.sB) {
            this.G();
            if (!mc.player.isAlive()) {
               this.c("You died.");
            } else {
               float var1 = this.i();
               if (this.sc.P() && var1 < this.CA - 0.01F) {
                  this.c("You took damage.");
               } else {
                  this.CA = var1;
                  if (this.G()) {
                     this.c("You moved.");
                  } else if (this.C6 != null && mc.player.getY() < this.C6.y - 2.5) {
                     this.c("You fell below the start spot.");
                  } else {
                     if (this.i != OrderDropperModule.b.O) {
                        int var10000 = mc.gui.screen() instanceof ContainerScreen;
                        boolean var3 = false;
                        byte var2 = (byte)var10000;
                        var10000 = Integer.signum(var2);
                        if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && zc.p(mc.gui.screen().getTitle().getString()) != zc.q.b) {
                           this.d = "Closing Orders before continuing";
                           var10000 = ++this.s6;
                           byte var5 = 100;
                           int var4 = var10000;
                           if ((-Integer.compare(var4, var5) >>> 31 ^ 1) == 0) {
                              this.c("The Orders menu kept reopening.");
                              return;
                           }

                           var10000 = this.s6 % 4;
                           byte var9 = 1;
                           int var8 = var10000;
                           var10000 = Integer.compare(var8, var9);
                           if ((var10000 | -var10000) >>> 31 == 0) {
                              this.J();
                           }

                           return;
                        }
                     }

                     byte var7 = 1;
                     int var6 = this.sl;
                     this.sl = var6 + var7;
                     switch (this.i) {
                        case O:
                           this.v();
                           break;
                        case h:
                           this.r();
                           break;
                        case w:
                           this.Y();
                           break;
                        case z:
                           this.e();
                           break;
                        case q:
                           this.c();
                           break;
                        case S:
                           this.y();
                           break;
                        case b:
                           this.z();
                     }
                  }
               }
            }
         } else {
            this.c("The world changed.");
         }
      }
   }

   private void v() {
      if (this.sG > 0) {
         this.sG--;
      } else {
         Screen var1 = mc.gui.screen();
         if (this.sQ) {
            if (var1 != null) {
               this.sH = 0;
               if (++this.s6 > 100) {
                  this.c("The Orders menu kept reopening.");
                  return;
               }

               if (this.s6 % 4 == 1 && this.O()) {
                  this.J();
               }

               return;
            }

            if (++this.sH < 4) {
               return;
            }

            this.sQ = (boolean)0;
         }

         if (var1 == null) {
            if (this.M != null) {
               this.M = null;
            }

            if (this.J()) {
               if (this.s9) {
                  this.V(OrderDropperModule.b.h);
                  return;
               }

               if (this.h() == 0) {
                  if (this.b()) {
                     this.p();
                  } else {
                     this.c("Your inventory is full of other items. Make room and enable again.");
                  }

                  return;
               }

               if (this.b() > 0) {
                  this.V(OrderDropperModule.b.h);
                  return;
               }

               if (this.sA) {
                  this.n();
                  return;
               }

               if (mc.player.getXRot() < 88.0F) {
                  this.d = "Looking down";
                  m9.k(this, m9.s.w, mc.player.getYRot(), 90.0F, 0.5);
                  return;
               }
            } else if (this.sA) {
               this.o();
               return;
            }

            if (++this.sH >= 5) {
               if (this.C2 <= 0 || ++this.C2 >= 60) {
                  if (++this.su > 3) {
                     this.c("The /orders menu did not open after 3 tries.");
                  } else {
                     this.d = "Opening /orders";
                     mc.player.connection.sendCommand("orders");
                     this.C2 = 1;
                     mt.U("OrderDropper", "Sent /orders (attempt {})", this.su);
                  }
               }
            }
         } else {
            this.sH = 0;
            if (!(var1 instanceof AbstractContainerScreen)) {
               this.d = "Waiting for the open screen to close";
            } else {
               ChestMenu var2 = this.V();
               if (var2 == null) {
                  this.c("A different menu opened: " + var1.getTitle().getString() + ".");
               } else {
                  this.su = this.C2 = 0;
                  if (this.M != null) {
                     int var5 = var2.containerId == this.T && l(var2) == this.J ? 0 : 1;
                     if (var5 != 0) {
                        this.M = null;
                        this.sG = this.o();
                        this.CS = 0;
                     } else if (++this.S > (this.M == OrderDropperModule.u.m ? 20 : 60)) {
                        if (this.M == OrderDropperModule.u.m && ++this.CS <= 3) {
                           this.M = null;
                        } else {
                           this.c("The server did not respond to a menu click.");
                        }
                     }
                  } else {
                     ItemStack var3 = var2.getCarried();
                     if (var3.isEmpty() || this.T(var3) && zc.p(var1.getTitle().getString()) == zc.q.U) {
                        this.G = 0;
                        zc.q var4 = zc.p(var1.getTitle().getString());
                        if (this.j(var2, var4)) {
                           switch (var4) {
                              case y:
                                 this.d = "Opening Your Orders";
                                 this.J(var2, zc.o.H, OrderDropperModule.u.m);
                                 break;
                              case F:
                                 this.C(var2);
                                 break;
                              case J:
                                 this.d = "Opening Collect";
                                 this.J(var2, zc.o.j, OrderDropperModule.u.m);
                                 break;
                              case U:
                                 this.G(var2);
                                 break;
                              case b:
                                 this.c("An unexpected Orders menu opened: " + var1.getTitle().getString() + ".");
                           }
                        }
                     } else {
                        if (++this.G > 60) {
                           this.c("The cursor still holds " + var3.getHoverName().getString() + ".");
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static void A(int var0) {
      OrderDropperModule var1 = k;
      Minecraft var2 = getMc();
      if (var1 != null && var1.i == OrderDropperModule.b.O && var2 != null && var2.player != null) {
         AbstractContainerMenu var3 = var2.player.containerMenu;
         if (var3 instanceof ChestMenu && var3.containerId == var0) {
            var1.se = var3;
            var1.W = 0;
         }
      }
   }

   private void K() {
      this.se = this.sr = null;
      this.sZ = this.W = this.P = this.K = 0;
   }

   private boolean j(ChestMenu var1, zc.q var2) {
      int var3 = l(var1);
      if (this.sr == var1 && this.sZ == var3) {
         this.W++;
      } else {
         this.sr = var1;
         this.sZ = var3;
         this.W = 0;
      }

      String var4 = null;
      if (this.se != var1) {
         var4 = "server contents";
      } else if (this.W < 2) {
         var4 = "settled contents";
      } else if (var2 == zc.q.y && this.L(var1, zc.o.H) == -1) {
         var4 = "YOUR_ORDERS button";
      } else if (var2 == zc.q.J && this.L(var1, zc.o.j) == -1) {
         var4 = "COLLECT button";
      } else if (var2 == zc.q.F) {
         byte var5 = 0;

         for (int var6 = 0; var6 < Math.min(45, var1.getRowCount() * 9); var6++) {
            if (!var1.getSlot(var6).getItem().isEmpty()) {
               var5 = 1;
               break;
            }
         }

         if (var5 == 0) {
            var4 = "your order entries or empty-order placeholders";
         }
      }

      if (var4 == null) {
         this.P = 0;
         return true;
      } else {
         this.d = "Waiting for Orders: " + var4;
         if (++this.P <= 60) {
            return false;
         } else if (++this.K > 3) {
            this.c("The Orders menu did not load " + var4 + " after 3 reloads. Progress retained.");
            return false;
         } else {
            mt.T(
               "OrderDropper",
               "Reloading Orders: missing={} title={} id={} synced={} retry={}/{}",
               var4,
               mc.gui.screen().getTitle().getString(),
               var1.containerId,
               this.se == var1,
               this.K,
               3
            );
            this.P = 0;
            this.R();
            return false;
         }
      }
   }

   private void C(ChestMenu var1) {
      for (int var2 = 0; var2 < 45 && var2 < var1.getRowCount() * 9; var2++) {
         ItemStack var3 = var1.getSlot(var2).getItem();
         if (!var3.isEmpty() && zc.l(q(var3), this.sO, C(var3))) {
            String var4 = L(var3);
            if (!this.m.contains(var4)) {
               this.ss = var4;
               this.y = 0;
               this.d = "Opening your " + var3.getHoverName().getString() + " order";
               mt.U("OrderDropper", "Opening order in slot {}: {}", var2, var4);
               this.n(var1, var2, ContainerInput.PICKUP, OrderDropperModule.u.m);
               return;
            }
         }
      }

      this.sA = (boolean)1;
      this.d(this.m.isEmpty() ? "You have no " + this.sO + " orders with items to collect." : "All " + this.sO + " orders are empty.");
      mt.K("OrderDropper", "Orders finished; emptied={} pages={} taken={}", this.m.size(), this.n, this.sw);
      this.R();
   }

   private void G(ChestMenu var1) {
      int var2 = 0;
      int var3 = -1;

      for (int var4 = 0; var4 < 45; var4++) {
         if (!var1.getSlot(var4).getItem().isEmpty()) {
            var2++;
            if (var3 < 0) {
               var3 = var4;
            }
         }
      }

      int var6 = this.L(var1, zc.o.R);
      int var5 = this.L(var1, zc.o.T);
      if (!var1.getCarried().isEmpty()) {
         this.Y(var1);
      } else if (var2 != 0) {
         this.y = 0;
         if (var6 >= 0) {
            if (this.J() && this.s9) {
               this.R();
            } else if (this.J() && var2 + this.C() > this.s()) {
               this.d("The selected shulkers have room for " + this.s() + " stacks; this page has " + var2 + ". Select more shulkers to continue.");
               mt.K("OrderDropper", "Not dropping: page={} carrying={} room={}", var2, this.C(), this.s());
               this.sA = (boolean)1;
               this.sF = (boolean)1;
               this.R();
            } else {
               this.d = "Dropping page " + (this.n + 1);
               this.J(var1, zc.o.R, OrderDropperModule.u.j);
               this.n++;
               this.s9 = (boolean)1;
               mt.U("OrderDropper", "Dropped page {} ({} stacks)", this.n, var2);
            }
         } else if (this.h() != 0) {
            this.d = "Taking items";
            this.n(var1, var3, ContainerInput.PICKUP, OrderDropperModule.u.j);
         } else if (this.J() && this.b()) {
            this.R();
         } else {
            this.c("This order has no Drop button and your inventory is full. Make room and enable again.");
         }
      } else if (var5 >= 0 && ++this.y <= 3) {
         this.d = "Next page";
         this.J(var1, zc.o.T, OrderDropperModule.u.j);
      } else {
         this.m.add(this.ss);
         this.d("Order emptied.");
         mt.K("OrderDropper", "Order emptied after {} pages: {}", this.n, this.ss);
         this.h();
      }
   }

   private void Y(ChestMenu var1) {
      for (int var2 = var1.getRowCount() * 9; var2 < var1.slots.size(); var2++) {
         Slot var3 = var1.getSlot(var2);
         if (var3.container == mc.player.getInventory() && var3.getContainerSlot() < 36 && var3.getItem().isEmpty()) {
            this.n(var1, var2, ContainerInput.PICKUP, OrderDropperModule.u.j);
            this.sw++;
            return;
         }
      }

      this.c("No empty inventory slot for the item on the cursor.");
   }

   private int L(ChestMenu var1, zc.o var2) {
      int var3 = -1;

      for (int var4 = var1.getRowCount() == 6 ? 45 : 0; var4 < var1.getRowCount() * 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         if (!var5.isEmpty() && zc.P(q(var5), var5.getHoverName().getString()) == var2) {
            if (var3 >= 0) {
               return -2;
            }

            var3 = var4;
         }
      }

      return var3;
   }

   private void J(ChestMenu var1, zc.o var2, OrderDropperModule.u var3) {
      int var4 = this.L(var1, var2);
      if (var4 == -2) {
         this.c("Two " + var2 + " buttons found; this menu layout needs review.");
      } else if (var4 < 0) {
         this.c("No " + var2 + " button in this menu.");
      } else {
         this.n(var1, var4, ContainerInput.PICKUP, var3);
      }
   }

   private void n(AbstractContainerMenu var1, int var2, ContainerInput var3, OrderDropperModule.u var4) {
      ItemStack var5 = var1.getSlot(var2).getItem();
      if (var2 < 54 && var1 instanceof ChestMenu && zc.P(q(var5), var5.getHoverName().getString()) == zc.o.P) {
         this.c("Refused to click a Sell button.");
      } else {
         this.M = var4;
         this.T = var1.containerId;
         this.S = 0;
         this.P = this.K = 0;
         mc.gameMode.handleContainerInput(var1.containerId, var2, 0, var3, mc.player);
         this.J = l(var1);
      }
   }

   private void h() {
      this.ss = "";
      this.y = 0;
      this.R();
   }

   private void R() {
      this.sQ = (boolean)1;
      this.s6 = 0;
      this.M = null;
      this.sH = 0;
      this.C2 = 0;
      this.su = 0;
      this.J();
   }

   private void J() {
      if (mc.player.containerMenu != mc.player.inventoryMenu && mc.player.containerMenu.getCarried().isEmpty()) {
         mc.player.closeContainer();
      }
   }

   private ChestMenu V() {
      if (mc.gui.screen() instanceof ContainerScreen var1
         && mc.player.containerMenu instanceof ChestMenu var2
         && var1.getMenu() == var2
         && (var2.getRowCount() == 6 || var2.getRowCount() == 3)) {
         zc.q var5 = zc.p(var1.getTitle().getString());
         return var5 == zc.q.b && !zc.c(var1.getTitle().getString()).startsWith("orders") ? null : var2;
      } else {
         return null;
      }
   }

   private boolean O() {
      return mc.gui.screen() instanceof ShulkerBoxScreen ? this.X != null : mc.gui.screen() != null && zc.p(mc.gui.screen().getTitle().getString()) != zc.q.b;
   }

   private void r() {
      this.d = "Picking up items";
      int var1 = this.b();
      if (this.h() <= 0 || this.sl >= 120 || var1 <= 0 && this.sl >= 10) {
         if (var1 > 0 && this.h() > 0) {
            mt.T("OrderDropper", "{} item stacks stayed out of pickup reach", var1);
         }

         if (this.h() != 0 && (!this.sA || !this.b())) {
            if (var1 > 0 && !this.b()) {
               this.c("The dropped items landed out of pickup reach.");
            } else if (this.sA) {
               this.n();
            } else {
               this.O();
            }
         } else {
            this.p();
         }
      }
   }

   private void O() {
      this.s9 = (boolean)0;
      this.V(OrderDropperModule.b.O);
   }

   private void n() {
      if (this.b()) {
         this.p();
      } else {
         this.o();
      }
   }

   private void p() {
      this.X = null;
      this.u();
      Vec3 var1 = mc.player.position();
      double var2 = Double.MAX_VALUE;

      for (BlockPos var5 : this.t()) {
         if (!this.Z.contains(var5) && (this.sj ? !this.sP.containsKey(var5) : !this.sa.contains(var5))) {
            double var6 = Vec3.atCenterOf(var5).distanceToSqr(var1);
            if (var6 < var2) {
               var2 = var6;
               this.X = var5;
            }
         }
      }

      if (this.X == null) {
         if (this.sj) {
            this.sj = (boolean)0;
            this.V(OrderDropperModule.b.S);
         } else {
            this.sF = (boolean)1;
            if (this.z(this.C6) > 0.4) {
               this.V(OrderDropperModule.b.S);
               this.d("No usable shulkers remain; walking back.");
               this.sA = (boolean)1;
            } else {
               this.c("No usable shulkers remain. Select more or rescan, then enable again.");
            }
         }
      } else {
         if (!this.sj) {
            this.sD++;
         }

         this.sT = this.N = (boolean)0;
         this.sL = this.Q = this.sS = this.Cv = 0;
         this.sx = null;
         this.F.clear();
         this.R = Double.MAX_VALUE;
         this.sf = 0;
         this.V(OrderDropperModule.b.w);
         mt.U("OrderDropper", "Trip {} to shulker {} carrying {} stacks", this.sD, this.X.toShortString(), this.C());
      }
   }

   private void Y() {
      if (!this.c(this.X)) {
         this.N("is missing or unloaded");
      } else if (mc.gui.screen() != null) {
         this.d = "Close the screen to continue";
      } else if (!this.K()) {
         this.d = this.sj ? "Walking to scan shulker" : "Walking to shulker";
         if (this.z() && this.a(mc.player.getEyePosition()) != null) {
            this.q = this.sg = 0;
            this.s4 = (boolean)0;
            this.V(OrderDropperModule.b.z);
         } else {
            if (this.sx != null && this.z(this.sx) < 0.15 && Math.abs(mc.player.getY() - this.sx.y) < 0.35) {
               this.F.add(this.sx);
               mt.U("OrderDropper", "Replanning shulker {} from eye={} after arriving at {}", this.X.toShortString(), mc.player.getEyePosition(), this.sx);
               this.sx = null;
            }

            if (this.sx == null) {
               this.sx = this.d();
               this.R = Double.MAX_VALUE;
               this.sf = 0;
            }

            if (this.sx == null) {
               mt.T(
                  "OrderDropper",
                  "No visible standing spot for {}: eye={} reach={} tried={}",
                  this.X.toShortString(),
                  mc.player.getEyePosition(),
                  this.h(),
                  this.F.size()
               );
               this.N("has no clear reachable standing spot");
            } else {
               this.P(this.sx, 0.1);
            }
         }
      }
   }

   private void e() {
      if (!this.c(this.X)) {
         this.N("is missing or unloaded");
      } else if (mc.gui.screen() instanceof ShulkerBoxScreen var1 && mc.player.containerMenu instanceof ShulkerBoxMenu var2 && var1.getMenu() == var2) {
         this.s4 = (boolean)0;
         this.sG = this.o();
         this.V(OrderDropperModule.b.q);
      } else if (!this.z() && !this.s4) {
         this.sx = null;
         this.R = Double.MAX_VALUE;
         this.sf = 0;
         this.V(OrderDropperModule.b.w);
      } else {
         this.d = "Opening shulker";
         if (this.s4) {
            if (++this.E <= 60) {
               return;
            }

            this.s4 = (boolean)0;
            if (++this.sg >= 3) {
               this.N("did not open");
               return;
            }
         }

         if (mc.gui.screen() == null) {
            if (!this.K()) {
               BlockHitResult var6 = this.a(mc.player.getEyePosition());
               if (var6 == null) {
                  this.sx = null;
                  this.V(OrderDropperModule.b.w);
               } else {
                  Vec3 var7 = k(this.X, var6);
                  float[] var9 = m9.Z(mc.player.getEyePosition(), var7);
                  m9.k(this, m9.s.w, var9[0], var9[1], 0.5);
                  BlockHitResult var4 = _m.F(this.X, this.h());
                  if (var4 == null) {
                     this.q = 0;
                     if (this.sl > 60) {
                        this.N("could not be aimed at");
                     }
                  } else if (++this.q >= 2 && !mc.player.isShiftKeyDown()) {
                     boolean var5 = _m.h(this, () -> {
                        if (mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, var4).consumesAction()) {
                           _8.G(mc.player, InteractionHand.MAIN_HAND);
                        }
                     }, () -> mc.gui.screen() == null);
                     if (var5) {
                        this.s4 = (boolean)1;
                        this.E = 0;
                     }
                  }
               }
            }
         }
      }
   }

   private void c() {
      if (mc.gui.screen() instanceof ShulkerBoxScreen var1 && mc.player.containerMenu instanceof ShulkerBoxMenu var2 && var1.getMenu() == var2) {
         this.d = this.sj ? "Scanning shulker" : "Filling shulker";
         if (!var2.getCarried().isEmpty()) {
            this.c("The cursor holds an item inside the shulker.");
         } else if (this.sG <= 0 || --this.sG <= 0) {
            if (this.sj) {
               this.K(var2);
               mc.player.closeContainer();
               this.p();
            } else {
               if (this.sT) {
                  int var7 = this.W(var2);
                  int var4 = this.X(var2);
                  int var5 = Math.max(0, var7 - this.s);
                  this.v = this.v + Math.max(0, var4 - this.c);
                  mt.K(
                     "OrderDropper", "Verified shulker {}: items added={} ({} -> {}), carrying={} stacks", this.X.toShortString(), var5, this.s, var7, this.C()
                  );
                  this.s = var7;
                  this.c = var4;
                  this.K(var2);
                  this.sT = (boolean)0;
                  this.Q = 0;
                  if (var5 > 0) {
                     this.sS = 0;
                  } else if (++this.sS > 3 && this.b()) {
                     this.c("Shulker transfers kept being rejected. Progress retained; check the server and enable again.");
                     return;
                  }
               }

               if (!this.N) {
                  this.s = this.W(var2);
                  this.c = this.X(var2);
                  this.N = (boolean)1;
               }

               if (this.Q >= 36) {
                  this.a();
               } else {
                  for (int var8 = 27; var8 < var2.slots.size(); var8++) {
                     Slot var10 = var2.getSlot(var8);
                     ItemStack var11 = var10.getItem();
                     if (this.T(var11) && (var10.container != mc.player.getInventory() || !this.z.contains(var10.getContainerSlot())) && this.F(var2, var11)) {
                        mc.gameMode.handleContainerInput(var2.containerId, var8, 0, ContainerInput.QUICK_MOVE, mc.player);
                        this.Q++;
                        this.sL = 0;
                        this.sG = this.L.P().intValue();
                        return;
                     }
                  }

                  if (!this.b() && ++this.sL < Math.max(10, this.L.P().intValue())) {
                     this.d = "Waiting for remaining items";
                  } else if (this.Q > 0) {
                     this.a();
                  } else {
                     this.K(var2);
                     boolean var9 = this.b();
                     if (var9) {
                        this.sa.add(this.X);
                        this.d("Shulker full (" + this.sa.size() + "/" + this.t().size() + ").");
                     }

                     mc.player.closeContainer();
                     mt.U("OrderDropper", "Closed shulker {}; full={} carrying={}", this.X.toShortString(), var9, this.C());
                     if (var9) {
                        this.p();
                     } else {
                        this.V(OrderDropperModule.b.S);
                     }
                  }
               }
            }
         }
      } else {
         this.sP.remove(this.X);
         if (++this.Cv > 3) {
            this.N("kept closing before filling finished");
         } else {
            this.sx = null;
            this.R = Double.MAX_VALUE;
            this.sf = 0;
            this.V(OrderDropperModule.b.w);
         }
      }
   }

   private void a() {
      this.sT = (boolean)1;
      this.s4 = (boolean)0;
      this.q = this.sg = 0;
      mc.player.closeContainer();
      this.V(OrderDropperModule.b.z);
      mt.U("OrderDropper", "Reopening {} to verify transfers", this.X.toShortString());
   }

   private int W(ShulkerBoxMenu var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (this.T(var1.getSlot(var3).getItem())) {
            var2 += var1.getSlot(var3).getItem().getCount();
         }
      }

      return var2;
   }

   private int X(ShulkerBoxMenu var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (this.T(var1.getSlot(var3).getItem())) {
            var2++;
         }
      }

      return var2;
   }

   private boolean F(ShulkerBoxMenu var1, ItemStack var2) {
      for (int var3 = 0; var3 < 27; var3++) {
         Slot var4 = var1.getSlot(var3);
         ItemStack var5 = var4.getItem();
         if (var5.isEmpty() ? var4.mayPlace(var2) : ItemStack.isSameItemSameComponents(var5, var2) && var5.getCount() < var4.getMaxStackSize(var5)) {
            return true;
         }
      }

      return false;
   }

   private void N(String var1) {
      this.Z.add(this.X);
      this.sP.remove(this.X);
      this.sa.remove(this.X);
      this.d("Skipped a shulker that " + var1 + ".");
      mt.T("OrderDropper", "Shulker {} {}; skipped", this.X.toShortString(), var1);
      this.p();
   }

   private void y() {
      this.d = "Walking back";
      if (this.z(this.C6) > 0.3) {
         this.P(this.C6, 0.3);
      } else if (this.J() && this.b() > 0 && this.h() > 0) {
         this.V(OrderDropperModule.b.h);
      } else if (this.b() && this.t().stream().anyMatch(var1 -> !this.sa.contains(var1) && !this.Z.contains(var1))) {
         this.p();
      } else if (!this.sA) {
         this.O();
      } else {
         this.o();
      }
   }

   private void o() {
      if (this.J() && this.z(this.C6) > 0.3) {
         this.V(OrderDropperModule.b.S);
      } else if (this.J() && (this.b() || this.b() > 0)) {
         this.c("Storage unavailable with items still to store. Select more shulkers or rescan, then enable again.");
      } else {
         this.V(OrderDropperModule.b.b);
      }
   }

   private void z() {
      this.d = "Finishing";
      if (!this.J() || this.sl >= 20 || !(Math.abs(Mth.wrapDegrees(mc.player.getYRot() - this.g)) > 1.0F) && !(Math.abs(mc.player.getXRot() - this.sR) > 1.0F)) {
         this.C(
            "Done: " + this.n + " pages dropped, " + this.sw + " stacks taken" + (this.J() ? ", " + this.v + " stacks stored in " + this.sD + " trips." : ".")
         );
         mt.K("OrderDropper", "Finished pages={} taken={} stored={} trips={}", this.n, this.sw, this.v, this.sD);
         this.w = (boolean)0;
         this.disable();
      } else {
         m9.k(this, m9.s.w, this.g, this.sR, 0.5);
      }
   }

   private void P(Vec3 var1, double var2) {
      if (!this.K()) {
         double var4 = this.z(var1);
         if (var4 < this.R - 0.05) {
            this.R = var4;
            this.sf = 0;
         } else if (++this.sf > 50) {
            this.c("The way is blocked; stopped where it was.");
            return;
         }

         Vec3 var6 = var1.subtract(mc.player.position());
         float var7 = (float)Math.toDegrees(Math.atan2(var6.z, var6.x)) - 90.0F;
         m9.k(this, m9.s.w, var7, 25.0F, 0.35);
         this.I = (boolean)(Math.abs(Mth.wrapDegrees(mc.player.getYRot() - var7)) < 35.0F ? 1 : 0);
         this.sM = (boolean)(this.I && mc.player.horizontalCollision && mc.player.onGround() ? 1 : 0);
         this.Cj = (boolean)(var2 > 0.0 && var4 < 1.0 ? 1 : 0);
      }
   }

   public static Input X() {
      if (k != null && k.isEnabled()) {
         OrderDropperModule var0 = k;
         if (var0 == null || !var0.isRuntimeActive() || getMc().gui.screen() != null || tq.f() || m9.R() && !m9.F(m9.s.w)) {
            return null;
         } else if (!var0.I) {
            return j() ? new Input(false, false, false, false, false, false, false) : null;
         } else {
            return new Input(true, false, false, false, var0.sM, var0.Cj, false);
         }
      } else {
         return null;
      }
   }

   private boolean G() {
      if (mc.gui.screen() == null && mc.isWindowActive()) {
         Options var1 = mc.options;
         return te.e(var1.keyUp) || te.e(var1.keyDown) || te.e(var1.keyLeft) || te.e(var1.keyRight) || te.e(var1.keyJump);
      } else {
         return false;
      }
   }

   private void s() {
      Minecraft var1 = getMc();
      if (var1 != null && var1.getWindow() != null) {
         if (this.sp != null && var1.level != this.sp && !this.isRuntimeActive()) {
            this.u = (boolean)0;
            this.C9 = null;
            this.x();
            this.sJ.clear();
            this.sE.clear();
            this.Y = null;
            this.sp = null;
         }

         if (this.u) {
            if (!this.isInGame() || var1.level != this.sp) {
               this.u = (boolean)0;
               this.C9 = null;
            } else if (!this.isRuntimeActive() && var1.gui.screen() == null) {
               if (System.nanoTime() - this.sK > 2000000000L) {
                  this.W();
               }
            } else {
               this.I();
            }
         }
      }
   }

   private void U() {
      if (this.sp != mc.level) {
         this.x();
         this.sJ.clear();
         this.sE.clear();
         this.Y = null;
         this.sp = mc.level;
      }
   }

   private void l() {
      this.U();
      this.u = (boolean)1;
      this.Y = this.sW = this.C9 = null;
      this.V = this.f = this.sk = this.sX = this.Ci = (boolean)0;
      if (mc.gameMode != null) {
         mc.gameMode.stopDestroyBlock();
      }

      this.W();
      mt.U("OrderDropper", "Selection started with {} shulkers", this.sJ.size());
   }

   private void I() {
      this.u = (boolean)0;
      this.C9 = this.sW = null;
      this.sk = this.sX = this.Ci = (boolean)0;
      this.e("Shulker selection saved. " + this.sJ.size() + " selected.");
      mt.K("OrderDropper", "Selection finished with {} shulkers", this.sJ.size());
   }

   private void W() {
      int var1 = this.O.P();
      this.e(
         this.sJ.size()
            + " selected. Right-click adds, left-click removes, hold to sweep, sneak-click for an area. "
            + (cc.N(var1) ? cc.u(var1) + " or Esc" : "Esc")
            + " to finish."
      );
   }

   public static boolean M() {
      OrderDropperModule var0 = k;
      return var0 != null && var0.u;
   }

   private void N() {
      if (!this.isInGame()) {
         this.C9 = null;
         this.j = (boolean)0;
      } else {
         this.j();
         int var1 = this.j && !this.s0 ? 1 : 0;
         if ((this.u || var1 != 0) && mc.gui.screen() == null) {
            HitResult var2 = mc.player.pick(24.0, mc.getDeltaTracker().getGameTimeDeltaPartialTick(false), false);
            this.C9 = var2 instanceof BlockHitResult var3 && var2.getType() == Type.BLOCK && this.c(var3.getBlockPos()) ? var3.getBlockPos().immutable() : null;
            if (var1 != 0) {
               this.z(this.C9);
            } else {
               boolean var7 = te.e(mc.options.keyUse);
               boolean var4 = te.e(mc.options.keyAttack);
               if (!var7) {
                  this.V = (boolean)1;
               }

               if (!var4) {
                  this.f = (boolean)1;
               }

               int var5 = var7 && this.V ? 1 : 0;
               int var6 = var4 && this.f && var5 == 0 ? 1 : 0;
               if ((var5 == 0 || this.sk) && (var6 == 0 || this.sX)) {
                  if ((var5 != 0 || var6 != 0) && !this.Ci && this.C9 != null && !this.C9.equals(this.sW)) {
                     if (this.O(this.C9, (boolean)var5)) {
                        this.w((boolean)var5, 1);
                     }

                     this.sW = this.Y = this.C9;
                  }
               } else {
                  this.v((boolean)var5);
               }

               this.sk = (boolean)var5;
               this.sX = (boolean)var6;
            }
         } else {
            this.C9 = null;
         }
      }
   }

   private void j() {
      int var1 = this.O.P();
      int var2 = cc.N(var1) && mc.gui.screen() == null && mc.isWindowActive() && cc.N(mc.getWindow().handle(), var1) ? 1 : 0;
      if (var2 != 0 && !this.j) {
         this.s0 = this.isRuntimeActive();
         if (this.s0) {
            this.e("Turn Order Dropper off before changing shulkers.");
         } else {
            this.U();
            this.Cy = null;
            this.s5 = null;
            this.CV = 0;
            this.sC = System.nanoTime();
         }
      } else if (var2 == 0 && this.j && !this.s0) {
         if (this.Cy == null && System.nanoTime() - this.sC < 400000000L) {
            if (this.u) {
               this.I();
            } else {
               this.l();
            }
         } else if (this.Cy != null) {
            mt.K("OrderDropper", "Key sweep {} {} shulkers; total {}", this.Cy ? "added" : "removed", this.CV, this.sJ.size());
         }
      }

      this.j = (boolean)var2;
   }

   private void z(BlockPos var1) {
      if (var1 != null && !var1.equals(this.s5)) {
         this.s5 = var1;
         if (this.Cy == null) {
            this.Cy = !this.sJ.contains(var1);
         }

         if (this.O(var1, this.Cy)) {
            this.Y = var1;
            this.w(this.Cy, ++this.CV);
         }
      }
   }

   private void v(boolean var1) {
      this.sW = null;
      this.Ci = (boolean)0;
      if (this.C9 == null) {
         this.e("Look at a placed shulker box.");
      } else {
         if (te.e(mc.options.keyShift) && this.Y != null && !this.Y.equals(this.C9)) {
            this.Ci = (boolean)1;
            List var2 = zc.A(i(this.Y), i(this.C9));
            if (var2.isEmpty()) {
               this.e("That area is too large. Keep each side within 32 blocks.");
               return;
            }

            int var3 = 0;

            for (zc.t var5 : var2) {
               if (this.O(new BlockPos(var5.x(), var5.y(), var5.z()), var1)) {
                  var3++;
               }
            }

            this.w(var1, var3);
         } else {
            if (this.O(this.C9, var1)) {
               this.w(var1, 1);
            } else {
               this.e((var1 ? "Already selected; left-click removes it. " : "Not selected. ") + this.sJ.size() + " selected.");
            }

            this.sW = this.C9;
         }

         this.Y = this.C9;
      }
   }

   private boolean O(BlockPos var1, boolean var2) {
      if (var2 ? !this.sJ.contains(var1) && this.c(var1) : this.sJ.remove(var1)) {
         if (var2) {
            this.sJ.add(var1.immutable());
         } else {
            this.sa.remove(var1);
            this.sP.remove(var1);
            this.Z.remove(var1);
            this.sn.remove(var1);
         }

         this.sE.put(var1.immutable(), new OrderDropperModule.k(System.nanoTime(), var2));
         return true;
      } else {
         return false;
      }
   }

   private void w(boolean var1, int var2) {
      this.e((var1 ? "Added " : "Removed ") + var2 + (var2 == 1 ? " shulker. " : " shulkers. ") + this.sJ.size() + " selected.");
      mt.U("OrderDropper", "{} {} shulkers; total {}", var1 ? "Added" : "Removed", var2, this.sJ.size());
   }

   private static zc.t i(BlockPos var0) {
      return new zc.t(var0.getX(), var0.getY(), var0.getZ());
   }

   private int C(BlockPos var1, boolean var2) {
      return this.Z.contains(var1) ? -1876386 : (this.sa.contains(var1) ? -2061766 : (this.P(var1, var2) ? -11702 : -13180806));
   }

   private static int O(int var0) {
      return var0 == -11702 ? -10733056 : (var0 == -2061766 ? -11918843 : (var0 == -1876386 ? -11923692 : -16040668));
   }

   private boolean P(BlockPos var1, boolean var2) {
      return var2 && var1.equals(this.X) && (this.i == OrderDropperModule.b.w || this.i == OrderDropperModule.b.z || this.i == OrderDropperModule.b.q);
   }

   private String J(BlockPos var1, boolean var2) {
      if (this.Z.contains(var1)) {
         return "Skipped";
      }

      if (this.P(var1, var2)) {
         return this.sj ? "Scanning" : (this.i == OrderDropperModule.b.q ? "Filling" : (this.i == OrderDropperModule.b.w ? "Next" : "Opening"));
      }

      if (this.sa.contains(var1)) {
         return "Full";
      }

      Integer var3 = this.sP.get(var1);
      return var3 == null ? null : var3 + " free";
   }

   private void P() {
      Minecraft var1 = getMc();
      if (var1 != null && var1.level != null) {
         try {
            this.N();
            if (var1.level != this.sp) {
               return;
            }

            long var2 = System.nanoTime();
            if (!this.sJ.isEmpty()) {
               this.p(var1.gameRenderer.mainCamera().position());
            }

            this.L(var2);
            if (this.C9 != null) {
               this.w();
            }
         } catch (RuntimeException var4) {
            mt.B("OrderDropper", "Shulker highlight failed", var4);
         }
      }
   }

   private void p(Vec3 var1) {
      boolean var2 = this.isRuntimeActive();

      for (BlockPos var4 : this.sJ) {
         String var5 = this.J(var4, var2);
         Vec3 var6 = Vec3.atCenterOf(var4);
         if (var5 != null && var1.distanceToSqr(var6) < 100.0) {
            Gizmos.billboardText(var5, var6, Style.forColorAndCentered(-854792).withScale(0.4F)).setAlwaysOnTop();
         }
      }
   }

   private void L(long var1) {
      Iterator var3 = this.sE.entrySet().iterator();

      while (var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         double var5 = 1.0 - (var1 - ((OrderDropperModule.k)var4.getValue()).at()) / 4.5E8;
         if (var5 <= 0.0) {
            var3.remove();
         } else {
            int var7 = ((OrderDropperModule.k)var4.getValue()).added() ? -13180806 : -1017711;
            Gizmos.cuboid(new AABB((BlockPos)var4.getKey()).inflate(0.02 + 0.06 * (1.0 - var5)), GizmoStyle.fill(_3.A(var7, 0.4 * var5))).setAlwaysOnTop();
         }
      }
   }

   private void w() {
      boolean var1 = this.sJ.contains(this.C9);
      Gizmos.cuboid(new AABB(this.C9).inflate(0.012), GizmoStyle.strokeAndFill(-854792, 2.5F, _3.A(var1 ? -1017711 : -13180806, 0.2))).setAlwaysOnTop();
      if (this.Y != null && !this.Y.equals(this.C9) && !this.j && te.e(mc.options.keyShift)) {
         int var2 = !zc.A(i(this.Y), i(this.C9)).isEmpty() ? 1 : 0;
         AABB var3 = new AABB(this.Y).minmax(new AABB(this.C9)).inflate(0.03);
         Gizmos.cuboid(var3, GizmoStyle.strokeAndFill(var2 != 0 ? -854792 : -1017711, 1.5F, _3.A(var2 != 0 ? -854792 : -1017711, 0.06))).setAlwaysOnTop();
      }
   }

   public static OrderDropperModule i() {
      return k;
   }

   public void b(PoseStack var1, xh var2, Vec3 var3) {
      if (this.isEnabled()) {
         Minecraft var4 = getMc();
         if (var4 != null && var4.level != null && var4.level == this.sp && !this.sJ.isEmpty()) {
            long var5 = kx.G();

            try {
               this.H(this.isRuntimeActive());
               VertexConsumer var7 = tj.W(var2, this.c());
               Pose var8 = var1.last();
               double var9 = ty.K();
               double var11 = var9 * 0.08 % 1.0;
               double var13 = 0.5 + 0.5 * Math.sin(var9 * 6.0);
               double var15 = this.s2.P();

               for (int var17 = 0; var17 < this.l.size(); var17++) {
                  double[] var18 = this.l.get(var17);
                  int var19 = this.Cl.get(var17);
                  double var20 = var19 == -11702 ? var15 * (0.7 + 0.3 * var13) : var15;

                  for (int var22 = 0; var22 < 4; var22++) {
                     int var23 = var22 * 5;
                     tj.n(
                        var7,
                        var8,
                        (float)(var18[var23] - var3.x),
                        (float)(var18[var23 + 1] - var3.y),
                        (float)(var18[var23 + 2] - var3.z),
                        (float)var18[var23 + 3],
                        (float)var18[var23 + 4],
                        (float)var18[20],
                        (float)var18[21],
                        (float)var18[22],
                        var19,
                        O(var19),
                        var20,
                        0.6,
                        1.0,
                        var11
                     );
                  }
               }
            } finally {
               kx.u("Order Dropper selection", var5);
            }
         }
      }
   }

   private tj.b c() {
      return switch ((String)this.p.P()) {
         case "Glow" -> tj.b.b;
         case "Metallic" -> tj.b.I;
         case "Marble" -> tj.b.Z;
         case "Glitter" -> tj.b.B;
         case "Solid" -> tj.b.z;
         case "Ghost" -> tj.b.s;
         default -> tj.b.d;
      };
   }

   private void H(boolean var1) {
      long var2 = this.sJ.size();

      for (BlockPos var5 : this.sJ) {
         var2 = var2 * 31L + var5.asLong() * 17L + this.C(var5, var1);
      }

      if (var2 != this.sN) {
         this.sN = var2;
         this.l.clear();
         this.Cl.clear();
         HashSet var36 = new HashSet<>(this.sJ);

         for (BlockPos var6 : this.sJ) {
            int var7 = this.C(var6, var1);

            for (Direction var11 : Direction.values()) {
               if (!var36.contains(var6.relative(var11))) {
                  Direction[] var12 = sV[var11.ordinal()];
                  Direction var13 = var12[0];
                  Direction var14 = var12[1];
                  double var15 = var36.contains(var6.relative(var13.getOpposite())) ? 0.0 : -0.012;
                  double var17 = var36.contains(var6.relative(var13)) ? 1.0 : 1.012;
                  double var19 = var36.contains(var6.relative(var14.getOpposite())) ? 0.0 : -0.012;
                  double var21 = var36.contains(var6.relative(var14)) ? 1.0 : 1.012;
                  double[] var23 = new double[23];
                  double[][] var24 = new double[][]{{var15, var19}, {var17, var19}, {var17, var21}, {var15, var21}};

                  for (int var25 = 0; var25 < 4; var25++) {
                     double var26 = var24[var25][0];
                     double var28 = var24[var25][1];
                     double var30 = E(var6.getX(), var11.getStepX(), var13.getStepX(), var14.getStepX(), var26, var28);
                     double var32 = E(var6.getY(), var11.getStepY(), var13.getStepY(), var14.getStepY(), var26, var28);
                     double var34 = E(var6.getZ(), var11.getStepZ(), var13.getStepZ(), var14.getStepZ(), var26, var28);
                     var23[var25 * 5] = var30;
                     var23[var25 * 5 + 1] = var32;
                     var23[var25 * 5 + 2] = var34;
                     var23[var25 * 5 + 3] = Math.floorMod(
                           var13.getAxis() == Axis.X ? var6.getX() : (var13.getAxis() == Axis.Y ? var6.getY() : var6.getZ()), 256
                        )
                        + var26;
                     var23[var25 * 5 + 4] = Math.floorMod(
                           var14.getAxis() == Axis.X ? var6.getX() : (var14.getAxis() == Axis.Y ? var6.getY() : var6.getZ()), 256
                        )
                        + var28;
                  }

                  var23[20] = var11.getStepX();
                  var23[21] = var11.getStepY();
                  var23[22] = var11.getStepZ();
                  this.l.add(var23);
                  this.Cl.add(var7);
               }
            }
         }
      }
   }

   private static double E(int var0, int var1, int var2, int var3, double var4, double var6) {
      double var8 = var1 > 0 ? var0 + 1 + 0.012 : (var1 < 0 ? var0 - 0.012 : var0);
      return var8 + (var2 != 0 ? var4 : 0.0) + (var3 != 0 ? var6 : 0.0);
   }

   private void V(OrderDropperModule.b var1) {
      if (this.i != var1) {
         mt.U("OrderDropper", "Phase {} -> {}", this.i, var1);
      }

      this.i = var1;
      this.sl = 0;
      if (var1 == OrderDropperModule.b.w || var1 == OrderDropperModule.b.S) {
         this.R = Double.MAX_VALUE;
         this.sf = 0;
      }
   }

   private int o() {
      return this.sv.P().intValue();
   }

   private float i() {
      return mc.player.getHealth() + mc.player.getAbsorptionAmount();
   }

   private double h() {
      return Math.min(mc.player.blockInteractionRange(), 4.5);
   }

   private double z(Vec3 var1) {
      Vec3 var2 = mc.player.position();
      return Math.hypot(var1.x - var2.x, var1.z - var2.z);
   }

   private boolean c(BlockPos var1) {
      return var1 != null && mc.level.hasChunkAt(var1) && mc.level.getBlockState(var1).getBlock() instanceof ShulkerBoxBlock;
   }

   private boolean T(ItemStack var1) {
      if (!var1.isEmpty() && !this.sO.isEmpty()) {
         if (!this.sO.equals(this.r)) {
            Identifier var2 = Identifier.tryParse(this.sO);
            this.s_ = var2 == null ? null : (Item)BuiltInRegistries.ITEM.getOptional(var2).orElse(null);
            this.r = this.sO;
         }

         return var1.getItem() == this.s_;
      } else {
         return false;
      }
   }

   private int h() {
      int var1 = 0;

      for (int var2 = 0; var2 < 36; var2++) {
         if (mc.player.getInventory().getItem(var2).isEmpty()) {
            var1++;
         }
      }

      return var1;
   }

   private int C() {
      int var1 = 0;

      for (int var2 = 0; var2 < 36; var2++) {
         if (!this.z.contains(var2) && this.T(mc.player.getInventory().getItem(var2))) {
            var1++;
         }
      }

      return var1;
   }

   private boolean b() {
      return this.C() > 0;
   }

   private int s() {
      this.u();
      int var1 = 0;

      for (BlockPos var3 : this.t()) {
         if (!this.sa.contains(var3) && !this.Z.contains(var3)) {
            var1 += this.sP.getOrDefault(var3, 0);
         }
      }

      return var1;
   }

   private void K(ShulkerBoxMenu var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (var1.getSlot(var3).getItem().isEmpty()) {
            var2++;
         }
      }

      this.sP.put(this.X, var2);
      this.sn.put(this.X, mc.level.getBlockEntity(this.X));
      this.Z.remove(this.X);
      if (var2 == 0) {
         this.sa.add(this.X);
      } else {
         this.sa.remove(this.X);
      }

      mt.K("OrderDropper", "Scanned shulker {}: free slots={}", this.X.toShortString(), var2);
   }

   private void u() {
      for (BlockPos var2 : this.t()) {
         if (!this.c(var2)) {
            if (this.Z.add(var2)) {
               mt.T("OrderDropper", "Selected shulker {} is missing or unloaded; capacity removed", var2.toShortString());
            }

            this.sP.remove(var2);
            this.sa.remove(var2);
            this.sn.remove(var2);
         } else if (this.sn.containsKey(var2) && this.sn.get(var2) != mc.level.getBlockEntity(var2)) {
            this.sP.remove(var2);
            this.sa.remove(var2);
            this.sn.remove(var2);
         }
      }
   }

   private static AABB K(BlockPos var0, Direction var1) {
      return new AABB(var0).expandTowards(var1.getStepX() * 0.5, var1.getStepY() * 0.5, var1.getStepZ() * 0.5).inflate(0.1);
   }

   private AABB h() {
      return K(this.X, (Direction)mc.level.getBlockState(this.X).getValue(ShulkerBoxBlock.FACING));
   }

   private boolean z() {
      return !this.h().intersects(mc.player.getBoundingBox());
   }

   private Vec3 d() {
      return G(
         this.X,
         mc.player.position(),
         mc.player.getBoundingBox(),
         this.h(),
         var0 -> mc.level.noCollision(mc.player, var0),
         var1 -> !this.F.contains(var1) && this.a(var1.add(0.0, mc.player.getEyeHeight(), 0.0)) != null
      );
   }

   private BlockHitResult a(Vec3 var1) {
      double var2 = this.h();
      if (J(this.X, var1) > var2 * var2) {
         return null;
      }

      List var4 = mc.level.getEntities(mc.player, new AABB(this.X).minmax(new AABB(var1, var1)).inflate(1.0), var0 -> var0.isPickable() && !var0.isSpectator());
      return n(this.X, var1, var2, (var1x, var2x) -> {
         BlockHitResult var3 = mc.level.clip(new ClipContext(var1x, var2x, Block.OUTLINE, Fluid.NONE, mc.player));
         if (var3.getType() != Type.BLOCK) {
            return null;
         }

         for (Entity var5 : var4) {
            if (p1.H(var1x, var3.getLocation(), var5.getBoundingBox())) {
               return null;
            }
         }

         return var3;
      });
   }

   private static double J(BlockPos var0, Vec3 var1) {
      double var2 = Math.clamp(var1.x, var0.getX(), var0.getX() + 1.0);
      double var4 = Math.clamp(var1.y, var0.getY(), var0.getY() + 1.0);
      double var6 = Math.clamp(var1.z, var0.getZ(), var0.getZ() + 1.0);
      return var1.distanceToSqr(var2, var4, var6);
   }

   private static BlockHitResult n(BlockPos var0, Vec3 var1, double var2, BiFunction<Vec3, Vec3, BlockHitResult> var4) {
      if (Double.isFinite(var2) && !(var2 <= 0.0) && !(J(var0, var1) > var2 * var2 + 1.0E-7)) {
         ArrayList var5 = new ArrayList(28);
         var5.add(Vec3.atCenterOf(var0));
         Vec3 var6 = Vec3.atCenterOf(var0);

         for (Direction var10 : Direction.values()) {
            double var11 = (var1.x - var6.x) * var10.getStepX() + (var1.y - var6.y) * var10.getStepY() + (var1.z - var6.z) * var10.getStepZ();
            if (!(var11 <= 0.5)) {
               for (Vec3 var14 : _d.k(var0, var10)) {
                  if (var1.distanceToSqr(var14) <= var2 * var2 + 1.0E-7) {
                     var5.add(var14);
                  }
               }
            }
         }

         for (Vec3 var16 : var5) {
            Vec3 var17 = var16.subtract(var1);
            if (!(var17.lengthSqr() < 1.0E-8)) {
               BlockHitResult var18 = (BlockHitResult)var4.apply(var1, var1.add(var17.normalize().scale(var2)));
               if (var18 != null
                  && var18.getType() == Type.BLOCK
                  && var18.getBlockPos().equals(var0)
                  && !(var1.distanceToSqr(var18.getLocation()) > var2 * var2 + 1.0E-7)) {
                  float[] var19 = m9.Z(var1, k(var0, var18));
                  BlockHitResult var12 = (BlockHitResult)var4.apply(var1, var1.add(Vec3.directionFromRotation(var19[1], var19[0]).scale(var2)));
                  if (var12 != null
                     && var12.getType() == Type.BLOCK
                     && var12.getBlockPos().equals(var0)
                     && var1.distanceToSqr(var12.getLocation()) <= var2 * var2 + 1.0E-7) {
                     return var18;
                  }
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private static Vec3 k(BlockPos var0, BlockHitResult var1) {
      Vec3 var2 = var1.getLocation();
      return new Vec3(
         Math.clamp(var2.x, var0.getX() + 0.02, var0.getX() + 0.98),
         Math.clamp(var2.y, var0.getY() + 0.02, var0.getY() + 0.98),
         Math.clamp(var2.z, var0.getZ() + 0.02, var0.getZ() + 0.98)
      );
   }

   private static Vec3 G(BlockPos var0, Vec3 var1, AABB var2, AABB var3, Predicate<AABB> var4, Predicate<Vec3> var5) {
      Vec3 var6 = Vec3.atBottomCenterOf(var0);
      ArrayList var7 = new ArrayList(72);

      for (double var11 : new double[]{1.25, 1.8, 2.5}) {
         for (int var16 : new int[]{0, -1, 1}) {
            for (int var17 = -1; var17 <= 1; var17++) {
               for (int var18 = -1; var18 <= 1; var18++) {
                  if (var17 != 0 || var18 != 0) {
                     Vec3 var19 = new Vec3(var6.x + var17 * var11, var1.y + var16, var6.z + var18 * var11);
                     AABB var20 = var2.move(var19.subtract(var1));
                     if (!var3.intersects(var20) && var4.test(var20) && !var4.test(var20.move(0.0, -0.15, 0.0))) {
                        var7.add(var19);
                     }
                  }
               }
            }
         }
      }

      var7.sort(Comparator.comparingDouble(var1x -> var1x.distanceToSqr(var1)));

      for (Vec3 var22 : var7) {
         if (var5.test(var22)) {
            return var22;
         }
      }

      return null;
   }

   private boolean K() {
      if (!_m.z() || m9.R() && !m9.F(m9.s.w)) {
         this.d = "Waiting for camera or rotation control";
         this.sl = Math.max(0, this.sl - 1);
         return true;
      } else {
         return false;
      }
   }

   public static boolean j() {
      if (k != null && k.isEnabled()) {
         OrderDropperModule var0 = k;
         return var0 != null && var0.isRuntimeActive() && var0.i != null && var0.sh.P();
      } else {
         return false;
      }
   }

   private void G() {
      if (!this.sh.P()) {
         this.D();
      } else {
         if (!this.t) {
            this.sq = mc.options.pauseOnLostFocus;
            this.t = (boolean)1;
            mt.K("OrderDropper", "AFK mouse released; pause on focus loss temporarily disabled");
         }

         mc.options.pauseOnLostFocus = (boolean)0;
         if (mc.mouseHandler.isMouseGrabbed()) {
            mc.mouseHandler.releaseMouse();
         }
      }
   }

   private void D() {
      if (this.t) {
         mc.options.pauseOnLostFocus = this.sq;
         this.t = (boolean)0;
         mt.K("OrderDropper", "AFK ended; pause on focus loss restored");
      }
   }

   private int b() {
      Vec3 var1 = this.C6 == null ? mc.player.position() : this.C6;
      AABB var2 = new AABB(var1, var1).inflate(2.5, 1.5, 2.5);
      return mc.level.getEntitiesOfClass(ItemEntity.class, var2, var1x -> var1x.isAlive() && this.T(var1x.getItem())).size();
   }

   private static int l(AbstractContainerMenu var0) {
      int var1 = 31 * var0.containerId + var0.getStateId();

      for (Slot var3 : var0.slots) {
         var1 = 31 * var1 + ItemStack.hashItemAndComponents(var3.getItem()) * 7 + var3.getItem().getCount();
      }

      ItemStack var4 = var0.getCarried();
      return 31 * var1 + ItemStack.hashItemAndComponents(var4) + var4.getCount();
   }

   private static List<String> C(ItemStack var0) {
      ItemLore var1 = (ItemLore)var0.get(DataComponents.LORE);
      if (var1 == null) {
         return List.of();
      }

      ArrayList var2 = new ArrayList();

      for (Component var4 : var1.lines()) {
         var2.add(var4.getString());
      }

      return var2;
   }

   private static String L(ItemStack var0) {
      StringBuilder var1 = new StringBuilder(q(var0));

      for (String var3 : C(var0)) {
         if (!zc.c(var3).contains("until")) {
            var1.append('|').append(var3);
         }
      }

      return var1.toString();
   }

   private static String q(ItemStack var0) {
      return BuiltInRegistries.ITEM.getKey(var0.getItem()).toString();
   }

   private void W(String var1) {
      this.C(var1);
      mt.K("OrderDropper", "Not started: {}", var1);
      this.disable();
   }

   private void c(String var1) {
      mt.T("OrderDropper", "Stopped in phase {}: {}", this.i, var1);
      this.C("Stopped: " + var1);
      this.disable();
   }

   private void e(String var1) {
      this.sK = System.nanoTime();
      if (mc.player != null) {
         mc.player.sendOverlayMessage(Component.literal(var1));
      }
   }

   private void d(String var1) {
      if (this.sz.P()) {
         this.C(var1);
      }
   }

   private void C(String var1) {
      if (this.isInGame()) {
         mc.gui.chatListener().handleSystemMessage(Component.literal("[Order Dropper] " + var1), false);
      }
   }

   static {
      sV[Direction.DOWN.ordinal()] = new Direction[]{Direction.EAST, Direction.SOUTH};
      sV[Direction.UP.ordinal()] = new Direction[]{Direction.SOUTH, Direction.EAST};
      sV[Direction.NORTH.ordinal()] = new Direction[]{Direction.UP, Direction.EAST};
      sV[Direction.SOUTH.ordinal()] = new Direction[]{Direction.EAST, Direction.UP};
      sV[Direction.WEST.ordinal()] = new Direction[]{Direction.SOUTH, Direction.UP};
      sV[Direction.EAST.ordinal()] = new Direction[]{Direction.UP, Direction.SOUTH};
   }

   private enum b {
      O,
      h,
      w,
      z,
      q,
      S,
      b;
   }

   private record k(long at, boolean added) {
   }

   private enum u {
      m,
      j;
   }
}

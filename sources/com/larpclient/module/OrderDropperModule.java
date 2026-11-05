package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.d8;
import com.larpclient.d_;
import com.larpclient.ea;
import com.larpclient.ep;
import com.larpclient.h_;
import com.larpclient.hp;
import com.larpclient.m;
import com.larpclient.oh;
import com.larpclient.pe;
import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.uv;
import com.larpclient.xn;
import com.larpclient.y2;
import com.larpclient.yu;
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
   public static final String U = "Drop Only";
   public static final String VN = "Fill Shulkers";
   private static final int V4 = 20;
   private static final int Vo = 60;
   private static final int H = 60;
   private static final int V6 = 120;
   private static final int V5 = 50;
   private static final int Vj = 3;
   private static final int L = 3;
   private static final int Vq = -13180806;
   private static final int Vr = -11702;
   private static final int Va = -2061766;
   private static final int h = -1876386;
   private static final int VO = -854792;
   private static final int l = -1017711;
   private static final double Q = 24.0;
   private static final double Vk = 10.0;
   private static final int Vb = 32;
   private static final long Ve = 450000000L;
   private static final long Gj = 2000000000L;
   private static final long V9 = 400000000L;
   private static final double j = 0.012;
   private static OrderDropperModule Y;
   private final lp VD = this.addSetting(
      new lp(
         "Mode",
         "Drop Only drops every page where you stand. Fill Shulkers drops a page at your feet, picks it up, stores it in the selected shulkers and walks back, until the orders are empty",
         "Fill Shulkers",
         "Drop Only",
         "Fill Shulkers"
      )
   );
   private final li F = this.addSetting(
         new li("Order Item", "Item whose orders are emptied, such as totem_of_undying. Use the held-item button to fill it in", "")
      )
      .R("totem_of_undying");
   private final la u = this.addSetting(
      new la(
         "Select Shulker Key",
         "Hold and sweep your crosshair over shulker boxes to select them; a sweep that starts on a selected box removes instead. Tap it for click selection: right-click adds, left-click removes, sneak-click covers an area",
         -1
      )
   );
   private final lc VJ = this.addSetting(new lc("Click Delay", "Ticks between menu clicks and item moves", 4.0, 2.0, 20.0, 1.0));
   private final ln b = this.addSetting(
      new ln("Stop When Hurt", "Stop at once when you take damage. Menus block Auto Totem, so this protects you while it works", true)
   );
   private final ln VB = this.addSetting(new ln("Show Messages", "Report orders, pages, trips and the final summary in chat", true));
   private final lc r = this.addSetting(
      new lc("Fill Delay", "Ticks between shulker transfers. Lower is faster. Reopens the box to verify the result before leaving", 2.0, 1.0, 20.0, 1.0)
   );
   private final ln Vd = this.addSetting(
      new ln("AFK Mode", "Release the mouse and keep running when you switch windows. Your pause-on-focus-loss preference is restored when stopped", false)
   );
   private final lp V7 = this.addSetting(
      new lp(
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
   private final lc V0 = this.addSetting(new lc("Highlight Opacity", "How strongly the material covers selected shulkers", 0.85, 0.2, 1.0, 0.05));
   private final List<BlockPos> GF = new ArrayList<>();
   private final Set<BlockPos> n = new HashSet<>();
   private final Map<BlockPos, Integer> G = new HashMap<>();
   private final Set<BlockPos> GO = new HashSet<>();
   private final Map<BlockPos, Object> V2 = new HashMap<>();
   private Level e;
   private boolean VZ;
   private boolean V_;
   private Boolean VQ;
   private BlockPos S;
   private int a;
   private long Vf;
   private final Map<BlockPos, OrderDropperModule.x> Vz = new HashMap<>();
   private boolean v;
   private boolean Vs;
   private boolean V3;
   private boolean I;
   private boolean VV;
   private boolean Vg;
   private BlockPos B;
   private BlockPos i;
   private BlockPos z;
   private long X;
   private final List<double[]> VP = new ArrayList<>();
   private final List<Integer> T = new ArrayList<>();
   private long M = Long.MIN_VALUE;
   private String GY;
   private Item w;
   private Object x;
   private Object G5;
   private Object D;
   private OrderDropperModule.k Vv;
   private String Vx = "";
   private String y = "";
   private String Vh = "Idle";
   private Vec3 G2;
   private float VK;
   private float Vi;
   private float VY;
   private final Set<String> E = new HashSet<>();
   private final Set<Integer> VS = new HashSet<>();
   private boolean VA;
   private boolean k;
   private boolean Vw;
   private boolean g;
   private int VW;
   private int K;
   private int Vc;
   private int VC;
   private int VT;
   private int VU;
   private int GP;
   private int o;
   private int VR;
   private OrderDropperModule.v Vp;
   private int Vt;
   private int Z;
   private int q;
   private AbstractContainerMenu J;
   private AbstractContainerMenu VE;
   private int m;
   private int Vn;
   private int W;
   private int O;
   private BlockPos N;
   private double VX;
   private int V;
   private int G7;
   private int d;
   private int GQ;
   private boolean VG;
   private boolean t;
   private boolean VH;
   private int Vy;
   private int Gr;
   private int f;
   private int A;
   private boolean P;
   private boolean p;
   private boolean VF;
   private boolean VI;
   private boolean VL;
   private Vec3 Vl;
   private final Set<Vec3> V8 = new HashSet<>();
   private boolean C;
   private boolean VM;
   private int R;
   private int V1;
   private int c;
   private int s;
   private int Gg;
   private int Vu;
   private static final Direction[][] Vm = new Direction[6][];

   public OrderDropperModule() {
      super("Order Dropper", "Empty your DonutSMP orders for one item by dropping pages, or fill selected shulkers with them", Category.PLAYER, -1);
      Y = this;
      this.r.x(this::w);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var1 -> this.h());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.M());
   }

   public List<l6<?>> n() {
      return List.of(this.VD, this.F);
   }

   public List<l6<?>> T() {
      return List.of(this.u, this.r, this.V7, this.V0);
   }

   public List<l6<?>> m() {
      return List.of(this.VJ, this.Vd, this.b, this.VB);
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.w;
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.Vh : (this.P ? "Paused" : "Idle");
   }

   private boolean w() {
      return this.VD.i("Fill Shulkers");
   }

   public String J() {
      String var1 = xn.F(this.F.getValue());
      String var2 = var1.isEmpty() ? "No order item chosen" : "Item: " + var1;
      int var3 = this.j().size();
      return var2
         + ". "
         + var3
         + (var3 == 1 ? " shulker" : " shulkers")
         + " selected"
         + (var3 == 0 ? "" : ", " + this.G.size() + " scanned, " + this.n.size() + " full, " + this.GO.size() + " unavailable")
         + (this.P ? ". Progress saved for this world session." : ".");
   }

   public void A() {
      if (!this.isInGame()) {
         throw new IllegalArgumentException("Join a world first.");
      }

      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before changing shulkers.");
      }

      mc.gui.setScreen(null);
      this.o();
   }

   public void p() {
      if (this.isInGame() && !mc.player.getMainHandItem().isEmpty()) {
         this.F.i(h(mc.player.getMainHandItem()));
         LarpClient var1 = LarpClient.y();
         if (var1 != null && var1.v() != null) {
            var1.v().A();
         }
      } else {
         throw new IllegalArgumentException("Hold the order's item first.");
      }
   }

   public void b() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before changing shulkers.");
      }

      this.m();
      this.GF.clear();
      this.i = null;
   }

   public void m() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before resetting progress.");
      }

      this.E();
      this.x();
      this.G.clear();
      this.n.clear();
      this.GO.clear();
      this.V2.clear();
      this.P = (boolean)0;
   }

   public void U() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before rescanning shulkers.");
      }

      this.G.clear();
      this.n.clear();
      this.GO.clear();
      this.V2.clear();
   }

   private List<BlockPos> j() {
      Minecraft var1 = getMc();
      return var1 != null && var1.level == this.e ? this.GF : List.of();
   }

   @Override
   public void onEnable() {
      if (this.isInGame() && mc.player.connection != null) {
         if (this.v) {
            this.O();
         }

         String var1 = xn.F(this.F.getValue());
         int var2 = this.P && this.x == mc.player && this.G5 == mc.level && this.D == mc.player.connection && this.Vx.equals(var1) && this.w() ? 1 : 0;
         if (var2 == 0) {
            this.x();
            this.P = (boolean)0;
         }

         this.Vx = var1;
         if (this.Vx.isEmpty() || !BuiltInRegistries.ITEM.containsKey(Identifier.tryParse(this.Vx))) {
            this.m("Choose the Order Item first, for example totem_of_undying, or hold it and use the held-item button.");
         } else if (this.w() && !((Item)BuiltInRegistries.ITEM.getValue(Identifier.parse(this.Vx))).canFitInsideContainerItems()) {
            this.m("This item cannot go inside a shulker box. Choose Drop Only for shulker box orders.");
         } else if (this.w() && this.j().isEmpty()) {
            this.m("Select at least one placed shulker box first: look at it and press the Select Shulker Key.");
         } else {
            this.x = mc.player;
            this.G5 = mc.level;
            this.D = mc.player.connection;
            if (var2 == 0) {
               this.G2 = mc.player.position();
               this.VK = mc.player.getYRot();
               this.Vi = mc.player.getXRot();

               for (int var3 = 0; var3 < 36; var3++) {
                  if (this.N(mc.player.getInventory().getItem(var3))) {
                     this.VS.add(var3);
                  }
               }
            }

            this.VY = this.e();
            if (var2 != 0 && this.VF) {
               this.VA = (boolean)0;
               this.VF = (boolean)0;
            }

            this.P = this.w();
            this.GO.clear();
            this.V();
            this.p = (boolean)(this.w() && this.j().stream().anyMatch(var1x -> !this.G.containsKey(var1x) && !this.GO.contains(var1x)) ? 1 : 0);
            if (this.p) {
               this.t();
            } else if (var2 != 0 && this.M()) {
               this.t();
            } else {
               this.H(var2 != 0 ? OrderDropperModule.k.f : OrderDropperModule.k.j);
            }

            this.l(
               this.w()
                  ? (var2 != 0 ? "Resumed. " : "Started. ") + "Emptying " + this.Vx + " orders into " + this.j().size() + " shulkers."
                  : "Started. Dropping every page of your " + this.Vx + " orders here."
            );
            r4.V("OrderDropper", "Started mode={} item={} shulkers={} start={}", this.VD.getValue(), this.Vx, this.j().size(), this.G2);
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.x != null
         && this.isInGame()
         && mc.player == this.x
         && mc.player.containerMenu != mc.player.inventoryMenu
         && mc.player.containerMenu.getCarried().isEmpty()
         && this.G()) {
         mc.player.closeContainer();
      }

      if (this.N != null && (this.Vv == OrderDropperModule.k.G || this.C || this.s > 0)) {
         this.G.remove(this.N);
      }

      this.E();
      this.Vv = null;
      this.VG = this.t = this.VH = (boolean)0;
      this.Vp = null;
      this.Vc = 0;
      this.Vw = this.g = (boolean)0;
      this.VW = this.VT = this.VU = this.GP = this.o = 0;
      this.N = null;
      this.Vl = null;
      this.V8.clear();
      this.k();
   }

   private void x() {
      this.x = this.G5 = this.D = null;
      this.Vv = null;
      this.G2 = null;
      this.Vh = "Idle";
      this.E.clear();
      this.VS.clear();
      this.VA = this.k = this.Vw = this.g = (boolean)0;
      this.VW = this.K = this.Vc = this.VC = this.VT = this.VU = this.GP = this.o = this.VR = 0;
      this.Vp = null;
      this.q = 0;
      this.N = null;
      this.y = "";
      this.V = this.G7 = this.d = this.GQ = 0;
      this.VG = this.t = this.VH = (boolean)0;
      this.Vy = this.Gr = this.f = this.A = 0;
      this.p = this.C = this.VM = this.VF = (boolean)0;
      this.Vl = null;
      this.R = this.V1 = this.c = this.s = this.Gg = this.Vu = 0;
      this.V8.clear();
      this.k();
   }

   @Override
   public void onTick() {
      this.VG = this.t = this.VH = (boolean)0;
      if (this.Vv != null && this.isInGame() && mc.player.connection != null) {
         if (mc.player == this.x && mc.level == this.G5 && mc.player.connection == this.D) {
            this.q();
            if (!mc.player.isAlive()) {
               this.Y("You died.");
            } else {
               float var1 = this.e();
               if (this.b.getValue() && var1 < this.VY - 0.01F) {
                  this.Y("You took damage.");
               } else {
                  this.VY = var1;
                  if (this.I()) {
                     this.Y("You moved.");
                  } else {
                     if (this.G2 != null) {
                        double var13;
                        int var10000 = (var13 = mc.player.getY() - (this.G2.y - 2.5)) == 0.0 ? 0 : (var13 < 0.0 ? -1 : 1);
                        boolean var5 = false;
                        int var4 = var10000;
                        if ((Integer.signum(var4) >>> 31 ^ 1) == 0) {
                           this.Y("You fell below the start spot.");
                           return;
                        }
                     }

                     if (this.Vv != OrderDropperModule.k.j
                        && mc.gui.screen() instanceof ContainerScreen
                        && xn.P(mc.gui.screen().getTitle().getString()) != xn.l.r) {
                        this.Vh = "Closing Orders before continuing";
                        int var10 = ++this.VW;
                        byte var7 = 100;
                        int var6 = var10;
                        if ((-Integer.compare(var6, var7) >>> 31 ^ 1) == 0) {
                           this.Y("The Orders menu kept reopening.");
                        } else {
                           var10 = this.VW % 4;
                           byte var3 = 1;
                           int var2 = var10;
                           var10 = Integer.compare(var2, var3);
                           if ((var10 | -var10) >>> 31 == 0) {
                              this.D();
                           }
                        }
                     } else {
                        byte var9 = 1;
                        int var8 = this.VC;
                        this.VC = var8 + var9;
                        switch (this.Vv) {
                           case j:
                              this.X();
                              break;
                           case H:
                              this.Q();
                              break;
                           case C:
                              this.J();
                              break;
                           case T:
                              this.C();
                              break;
                           case G:
                              this.f();
                              break;
                           case f:
                              this.W();
                              break;
                           case K:
                              this.H();
                        }
                     }
                  }
               }
            }
         } else {
            this.Y("The world changed.");
         }
      }
   }

   private void X() {
      if (this.Vc > 0) {
         this.Vc--;
      } else {
         Screen var1 = mc.gui.screen();
         if (this.g) {
            if (var1 != null) {
               this.VT = 0;
               if (++this.VW > 100) {
                  this.Y("The Orders menu kept reopening.");
                  return;
               }

               if (this.VW % 4 == 1 && this.G()) {
                  this.D();
               }

               return;
            }

            if (++this.VT < 4) {
               return;
            }

            this.g = (boolean)0;
         }

         if (var1 == null) {
            if (this.Vp != null) {
               this.Vp = null;
            }

            if (this.w()) {
               if (this.k) {
                  this.H(OrderDropperModule.k.H);
                  return;
               }

               if (this.A() == 0) {
                  if (this.M()) {
                     this.t();
                  } else {
                     this.Y("Your inventory is full of other items. Make room and enable again.");
                  }

                  return;
               }

               if (this.s() > 0) {
                  this.H(OrderDropperModule.k.H);
                  return;
               }

               if (this.VA) {
                  this.R();
                  return;
               }

               if (mc.player.getXRot() < 88.0F) {
                  this.Vh = "Looking down";
                  ep.t(this, ep.z.R, mc.player.getYRot(), 90.0F, 0.5);
                  return;
               }
            } else if (this.VA) {
               this.i();
               return;
            }

            if (++this.VT >= 5) {
               if (this.VU <= 0 || ++this.VU >= 60) {
                  if (++this.GP > 3) {
                     this.Y("The /orders menu did not open after 3 tries.");
                  } else {
                     this.Vh = "Opening /orders";
                     mc.player.connection.sendCommand("orders");
                     this.VU = 1;
                     r4.g("OrderDropper", "Sent /orders (attempt {})", this.GP);
                  }
               }
            }
         } else {
            this.VT = 0;
            if (!(var1 instanceof AbstractContainerScreen)) {
               this.Vh = "Waiting for the open screen to close";
            } else {
               ChestMenu var2 = this.v();
               if (var2 == null) {
                  this.Y("A different menu opened: " + var1.getTitle().getString() + ".");
               } else {
                  this.GP = this.VU = 0;
                  if (this.Vp != null) {
                     int var5 = var2.containerId == this.Vt && m(var2) == this.Z ? 0 : 1;
                     if (var5 != 0) {
                        this.Vp = null;
                        this.Vc = this.q();
                        this.o = 0;
                     } else if (++this.q > (this.Vp == OrderDropperModule.v.k ? 20 : 60)) {
                        if (this.Vp == OrderDropperModule.v.k && ++this.o <= 3) {
                           this.Vp = null;
                        } else {
                           this.Y("The server did not respond to a menu click.");
                        }
                     }
                  } else {
                     ItemStack var3 = var2.getCarried();
                     if (var3.isEmpty() || this.N(var3) && xn.P(var1.getTitle().getString()) == xn.l.t) {
                        this.K = 0;
                        xn.l var4 = xn.P(var1.getTitle().getString());
                        if (this.N(var2, var4)) {
                           switch (var4) {
                              case j:
                                 this.Vh = "Opening Your Orders";
                                 this.c(var2, xn.q.G, OrderDropperModule.v.k);
                                 break;
                              case R:
                                 this.Z(var2);
                                 break;
                              case G:
                                 this.Vh = "Opening Collect";
                                 this.c(var2, xn.q.D, OrderDropperModule.v.k);
                                 break;
                              case t:
                                 this.F(var2);
                                 break;
                              case r:
                                 this.Y("An unexpected Orders menu opened: " + var1.getTitle().getString() + ".");
                           }
                        }
                     } else {
                        if (++this.K > 60) {
                           this.Y("The cursor still holds " + var3.getHoverName().getString() + ".");
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static void x(int var0) {
      OrderDropperModule var1 = Y;
      Minecraft var2 = getMc();
      if (var1 != null && var1.Vv == OrderDropperModule.k.j && var2 != null && var2.player != null) {
         AbstractContainerMenu var3 = var2.player.containerMenu;
         if (var3 instanceof ChestMenu && var3.containerId == var0) {
            var1.J = var3;
            var1.Vn = 0;
         }
      }
   }

   private void k() {
      this.J = this.VE = null;
      this.m = this.Vn = this.W = this.O = 0;
   }

   private boolean N(ChestMenu var1, xn.l var2) {
      int var3 = m(var1);
      if (this.VE == var1 && this.m == var3) {
         this.Vn++;
      } else {
         this.VE = var1;
         this.m = var3;
         this.Vn = 0;
      }

      String var4 = null;
      if (this.J != var1) {
         var4 = "server contents";
      } else if (this.Vn < 2) {
         var4 = "settled contents";
      } else if (var2 == xn.l.j && this.B(var1, xn.q.G) == -1) {
         var4 = "YOUR_ORDERS button";
      } else if (var2 == xn.l.G && this.B(var1, xn.q.D) == -1) {
         var4 = "COLLECT button";
      } else if (var2 == xn.l.R) {
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
         this.W = 0;
         return true;
      } else {
         this.Vh = "Waiting for Orders: " + var4;
         if (++this.W <= 60) {
            return false;
         } else if (++this.O > 3) {
            this.Y("The Orders menu did not load " + var4 + " after 3 reloads. Progress retained.");
            return false;
         } else {
            r4.m(
               "OrderDropper",
               "Reloading Orders: missing={} title={} id={} synced={} retry={}/{}",
               var4,
               mc.gui.screen().getTitle().getString(),
               var1.containerId,
               this.J == var1,
               this.O,
               3
            );
            this.W = 0;
            this.l();
            return false;
         }
      }
   }

   private void Z(ChestMenu var1) {
      for (int var2 = 0; var2 < 45 && var2 < var1.getRowCount() * 9; var2++) {
         ItemStack var3 = var1.getSlot(var2).getItem();
         if (!var3.isEmpty() && xn.T(h(var3), this.Vx, J(var3))) {
            String var4 = O(var3);
            if (!this.E.contains(var4)) {
               this.y = var4;
               this.VR = 0;
               this.Vh = "Opening your " + var3.getHoverName().getString() + " order";
               r4.g("OrderDropper", "Opening order in slot {}: {}", var2, var4);
               this.t(var1, var2, ContainerInput.PICKUP, OrderDropperModule.v.k);
               return;
            }
         }
      }

      this.VA = (boolean)1;
      this.l(this.E.isEmpty() ? "You have no " + this.Vx + " orders with items to collect." : "All " + this.Vx + " orders are empty.");
      r4.V("OrderDropper", "Orders finished; emptied={} pages={} taken={}", this.E.size(), this.Vy, this.Gr);
      this.l();
   }

   private void F(ChestMenu var1) {
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

      int var6 = this.B(var1, xn.q.u);
      int var5 = this.B(var1, xn.q.y);
      if (!var1.getCarried().isEmpty()) {
         this.w(var1);
      } else if (var2 != 0) {
         this.VR = 0;
         if (var6 >= 0) {
            if (this.w() && this.k) {
               this.l();
            } else if (this.w() && var2 + this.V() > this.G()) {
               this.l("The selected shulkers have room for " + this.G() + " stacks; this page has " + var2 + ". Select more shulkers to continue.");
               r4.V("OrderDropper", "Not dropping: page={} carrying={} room={}", var2, this.V(), this.G());
               this.VA = (boolean)1;
               this.VF = (boolean)1;
               this.l();
            } else {
               this.Vh = "Dropping page " + (this.Vy + 1);
               this.c(var1, xn.q.u, OrderDropperModule.v.u);
               this.Vy++;
               this.k = (boolean)1;
               r4.g("OrderDropper", "Dropped page {} ({} stacks)", this.Vy, var2);
            }
         } else if (this.A() != 0) {
            this.Vh = "Taking items";
            this.t(var1, var3, ContainerInput.PICKUP, OrderDropperModule.v.u);
         } else if (this.w() && this.M()) {
            this.l();
         } else {
            this.Y("This order has no Drop button and your inventory is full. Make room and enable again.");
         }
      } else if (var5 >= 0 && ++this.VR <= 3) {
         this.Vh = "Next page";
         this.c(var1, xn.q.y, OrderDropperModule.v.u);
      } else {
         this.E.add(this.y);
         this.l("Order emptied.");
         r4.V("OrderDropper", "Order emptied after {} pages: {}", this.Vy, this.y);
         this.y();
      }
   }

   private void w(ChestMenu var1) {
      for (int var2 = var1.getRowCount() * 9; var2 < var1.slots.size(); var2++) {
         Slot var3 = var1.getSlot(var2);
         if (var3.container == mc.player.getInventory() && var3.getContainerSlot() < 36 && var3.getItem().isEmpty()) {
            this.t(var1, var2, ContainerInput.PICKUP, OrderDropperModule.v.u);
            this.Gr++;
            return;
         }
      }

      this.Y("No empty inventory slot for the item on the cursor.");
   }

   private int B(ChestMenu var1, xn.q var2) {
      int var3 = -1;

      for (int var4 = var1.getRowCount() == 6 ? 45 : 0; var4 < var1.getRowCount() * 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         if (!var5.isEmpty() && xn.L(h(var5), var5.getHoverName().getString()) == var2) {
            if (var3 >= 0) {
               return -2;
            }

            var3 = var4;
         }
      }

      return var3;
   }

   private void c(ChestMenu var1, xn.q var2, OrderDropperModule.v var3) {
      int var4 = this.B(var1, var2);
      if (var4 == -2) {
         this.Y("Two " + var2 + " buttons found; this menu layout needs review.");
      } else if (var4 < 0) {
         this.Y("No " + var2 + " button in this menu.");
      } else {
         this.t(var1, var4, ContainerInput.PICKUP, var3);
      }
   }

   private void t(AbstractContainerMenu var1, int var2, ContainerInput var3, OrderDropperModule.v var4) {
      ItemStack var5 = var1.getSlot(var2).getItem();
      if (var2 < 54 && var1 instanceof ChestMenu && xn.L(h(var5), var5.getHoverName().getString()) == xn.q.r) {
         this.Y("Refused to click a Sell button.");
      } else {
         this.Vp = var4;
         this.Vt = var1.containerId;
         this.q = 0;
         this.W = this.O = 0;
         mc.gameMode.handleContainerInput(var1.containerId, var2, 0, var3, mc.player);
         this.Z = m(var1);
      }
   }

   private void y() {
      this.y = "";
      this.VR = 0;
      this.l();
   }

   private void l() {
      this.g = (boolean)1;
      this.VW = 0;
      this.Vp = null;
      this.VT = 0;
      this.VU = 0;
      this.GP = 0;
      this.D();
   }

   private void D() {
      if (mc.player.containerMenu != mc.player.inventoryMenu && mc.player.containerMenu.getCarried().isEmpty()) {
         mc.player.closeContainer();
      }
   }

   private ChestMenu v() {
      if (mc.gui.screen() instanceof ContainerScreen var1
         && mc.player.containerMenu instanceof ChestMenu var2
         && var1.getMenu() == var2
         && (var2.getRowCount() == 6 || var2.getRowCount() == 3)) {
         xn.l var5 = xn.P(var1.getTitle().getString());
         return var5 == xn.l.r && !xn.V(var1.getTitle().getString()).startsWith("orders") ? null : var2;
      } else {
         return null;
      }
   }

   private boolean G() {
      return mc.gui.screen() instanceof ShulkerBoxScreen ? this.N != null : mc.gui.screen() != null && xn.P(mc.gui.screen().getTitle().getString()) != xn.l.r;
   }

   private void Q() {
      this.Vh = "Picking up items";
      int var1 = this.s();
      if (this.A() <= 0 || this.VC >= 120 || var1 <= 0 && this.VC >= 10) {
         if (var1 > 0 && this.A() > 0) {
            r4.m("OrderDropper", "{} item stacks stayed out of pickup reach", var1);
         }

         if (this.A() != 0 && (!this.VA || !this.M())) {
            if (var1 > 0 && !this.M()) {
               this.Y("The dropped items landed out of pickup reach.");
            } else if (this.VA) {
               this.R();
            } else {
               this.F();
            }
         } else {
            this.t();
         }
      }
   }

   private void F() {
      this.k = (boolean)0;
      this.H(OrderDropperModule.k.j);
   }

   private void R() {
      if (this.M()) {
         this.t();
      } else {
         this.i();
      }
   }

   private void t() {
      this.N = null;
      this.V();
      Vec3 var1 = mc.player.position();
      double var2 = Double.MAX_VALUE;

      for (BlockPos var5 : this.j()) {
         if (!this.GO.contains(var5) && (this.p ? !this.G.containsKey(var5) : !this.n.contains(var5))) {
            double var6 = Vec3.atCenterOf(var5).distanceToSqr(var1);
            if (var6 < var2) {
               var2 = var6;
               this.N = var5;
            }
         }
      }

      if (this.N == null) {
         if (this.p) {
            this.p = (boolean)0;
            this.H(OrderDropperModule.k.f);
         } else {
            this.VF = (boolean)1;
            if (this.N(this.G2) > 0.4) {
               this.H(OrderDropperModule.k.f);
               this.l("No usable shulkers remain; walking back.");
               this.VA = (boolean)1;
            } else {
               this.Y("No usable shulkers remain. Select more or rescan, then enable again.");
            }
         }
      } else {
         if (!this.p) {
            this.A++;
         }

         this.C = this.VM = (boolean)0;
         this.c = this.s = this.Gg = this.Vu = 0;
         this.Vl = null;
         this.V8.clear();
         this.VX = Double.MAX_VALUE;
         this.V = 0;
         this.H(OrderDropperModule.k.C);
         r4.g("OrderDropper", "Trip {} to shulker {} carrying {} stacks", this.A, this.N.toShortString(), this.V());
      }
   }

   private void J() {
      if (!this.Z(this.N)) {
         this.q("is missing or unloaded");
      } else if (mc.gui.screen() != null) {
         this.Vh = "Close the screen to continue";
      } else if (!this.n()) {
         this.Vh = this.p ? "Walking to scan shulker" : "Walking to shulker";
         if (this.c() && this.Z(mc.player.getEyePosition()) != null) {
            this.G7 = this.GQ = 0;
            this.Vw = (boolean)0;
            this.H(OrderDropperModule.k.T);
         } else {
            if (this.Vl != null && this.N(this.Vl) < 0.15 && Math.abs(mc.player.getY() - this.Vl.y) < 0.35) {
               this.V8.add(this.Vl);
               r4.g("OrderDropper", "Replanning shulker {} from eye={} after arriving at {}", this.N.toShortString(), mc.player.getEyePosition(), this.Vl);
               this.Vl = null;
            }

            if (this.Vl == null) {
               this.Vl = this.u();
               this.VX = Double.MAX_VALUE;
               this.V = 0;
            }

            if (this.Vl == null) {
               r4.m(
                  "OrderDropper",
                  "No visible standing spot for {}: eye={} reach={} tried={}",
                  this.N.toShortString(),
                  mc.player.getEyePosition(),
                  this.P(),
                  this.V8.size()
               );
               this.q("has no clear reachable standing spot");
            } else {
               this.I(this.Vl, 0.1);
            }
         }
      }
   }

   private void C() {
      if (!this.Z(this.N)) {
         this.q("is missing or unloaded");
      } else if (mc.gui.screen() instanceof ShulkerBoxScreen var1 && mc.player.containerMenu instanceof ShulkerBoxMenu var2 && var1.getMenu() == var2) {
         this.Vw = (boolean)0;
         this.Vc = this.q();
         this.H(OrderDropperModule.k.G);
      } else if (!this.c() && !this.Vw) {
         this.Vl = null;
         this.VX = Double.MAX_VALUE;
         this.V = 0;
         this.H(OrderDropperModule.k.C);
      } else {
         this.Vh = "Opening shulker";
         if (this.Vw) {
            if (++this.d <= 60) {
               return;
            }

            this.Vw = (boolean)0;
            if (++this.GQ >= 3) {
               this.q("did not open");
               return;
            }
         }

         if (mc.gui.screen() == null) {
            if (!this.n()) {
               BlockHitResult var6 = this.Z(mc.player.getEyePosition());
               if (var6 == null) {
                  this.Vl = null;
                  this.H(OrderDropperModule.k.C);
               } else {
                  Vec3 var7 = u(this.N, var6);
                  float[] var9 = ep.g(mc.player.getEyePosition(), var7);
                  ep.t(this, ep.z.R, var9[0], var9[1], 0.5);
                  BlockHitResult var4 = d8.F(this.N, this.P());
                  if (var4 == null) {
                     this.G7 = 0;
                     if (this.VC > 60) {
                        this.q("could not be aimed at");
                     }
                  } else if (++this.G7 >= 2 && !mc.player.isShiftKeyDown()) {
                     boolean var5 = d8.R(this, () -> {
                        if (mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, var4).consumesAction()) {
                           hp.Q(mc.player, InteractionHand.MAIN_HAND);
                        }
                     }, () -> mc.gui.screen() == null);
                     if (var5) {
                        this.Vw = (boolean)1;
                        this.d = 0;
                     }
                  }
               }
            }
         }
      }
   }

   private void f() {
      if (mc.gui.screen() instanceof ShulkerBoxScreen var1 && mc.player.containerMenu instanceof ShulkerBoxMenu var2 && var1.getMenu() == var2) {
         this.Vh = this.p ? "Scanning shulker" : "Filling shulker";
         if (!var2.getCarried().isEmpty()) {
            this.Y("The cursor holds an item inside the shulker.");
         } else if (this.Vc <= 0 || --this.Vc <= 0) {
            if (this.p) {
               this.e(var2);
               mc.player.closeContainer();
               this.t();
            } else {
               if (this.C) {
                  int var7 = this.D(var2);
                  int var4 = this.g(var2);
                  int var5 = Math.max(0, var7 - this.R);
                  this.f = this.f + Math.max(0, var4 - this.V1);
                  r4.V(
                     "OrderDropper", "Verified shulker {}: items added={} ({} -> {}), carrying={} stacks", this.N.toShortString(), var5, this.R, var7, this.V()
                  );
                  this.R = var7;
                  this.V1 = var4;
                  this.e(var2);
                  this.C = (boolean)0;
                  this.s = 0;
                  if (var5 > 0) {
                     this.Gg = 0;
                  } else if (++this.Gg > 3 && this.M()) {
                     this.Y("Shulker transfers kept being rejected. Progress retained; check the server and enable again.");
                     return;
                  }
               }

               if (!this.VM) {
                  this.R = this.D(var2);
                  this.V1 = this.g(var2);
                  this.VM = (boolean)1;
               }

               if (this.s >= 36) {
                  this.r();
               } else {
                  for (int var8 = 27; var8 < var2.slots.size(); var8++) {
                     Slot var10 = var2.getSlot(var8);
                     ItemStack var11 = var10.getItem();
                     if (this.N(var11) && (var10.container != mc.player.getInventory() || !this.VS.contains(var10.getContainerSlot())) && this.m(var2, var11)) {
                        mc.gameMode.handleContainerInput(var2.containerId, var8, 0, ContainerInput.QUICK_MOVE, mc.player);
                        this.s++;
                        this.c = 0;
                        this.Vc = this.r.getValue().intValue();
                        return;
                     }
                  }

                  if (!this.M() && ++this.c < Math.max(10, this.r.getValue().intValue())) {
                     this.Vh = "Waiting for remaining items";
                  } else if (this.s > 0) {
                     this.r();
                  } else {
                     this.e(var2);
                     boolean var9 = this.M();
                     if (var9) {
                        this.n.add(this.N);
                        this.l("Shulker full (" + this.n.size() + "/" + this.j().size() + ").");
                     }

                     mc.player.closeContainer();
                     r4.g("OrderDropper", "Closed shulker {}; full={} carrying={}", this.N.toShortString(), var9, this.V());
                     if (var9) {
                        this.t();
                     } else {
                        this.H(OrderDropperModule.k.f);
                     }
                  }
               }
            }
         }
      } else {
         this.G.remove(this.N);
         if (++this.Vu > 3) {
            this.q("kept closing before filling finished");
         } else {
            this.Vl = null;
            this.VX = Double.MAX_VALUE;
            this.V = 0;
            this.H(OrderDropperModule.k.C);
         }
      }
   }

   private void r() {
      this.C = (boolean)1;
      this.Vw = (boolean)0;
      this.G7 = this.GQ = 0;
      mc.player.closeContainer();
      this.H(OrderDropperModule.k.T);
      r4.g("OrderDropper", "Reopening {} to verify transfers", this.N.toShortString());
   }

   private int D(ShulkerBoxMenu var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (this.N(var1.getSlot(var3).getItem())) {
            var2 += var1.getSlot(var3).getItem().getCount();
         }
      }

      return var2;
   }

   private int g(ShulkerBoxMenu var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (this.N(var1.getSlot(var3).getItem())) {
            var2++;
         }
      }

      return var2;
   }

   private boolean m(ShulkerBoxMenu var1, ItemStack var2) {
      for (int var3 = 0; var3 < 27; var3++) {
         Slot var4 = var1.getSlot(var3);
         ItemStack var5 = var4.getItem();
         if (var5.isEmpty() ? var4.mayPlace(var2) : ItemStack.isSameItemSameComponents(var5, var2) && var5.getCount() < var4.getMaxStackSize(var5)) {
            return true;
         }
      }

      return false;
   }

   private void q(String var1) {
      this.GO.add(this.N);
      this.G.remove(this.N);
      this.n.remove(this.N);
      this.l("Skipped a shulker that " + var1 + ".");
      r4.m("OrderDropper", "Shulker {} {}; skipped", this.N.toShortString(), var1);
      this.t();
   }

   private void W() {
      this.Vh = "Walking back";
      if (this.N(this.G2) > 0.3) {
         this.I(this.G2, 0.3);
      } else if (this.w() && this.s() > 0 && this.A() > 0) {
         this.H(OrderDropperModule.k.H);
      } else if (this.M() && this.j().stream().anyMatch(var1 -> !this.n.contains(var1) && !this.GO.contains(var1))) {
         this.t();
      } else if (!this.VA) {
         this.F();
      } else {
         this.i();
      }
   }

   private void i() {
      if (this.w() && this.N(this.G2) > 0.3) {
         this.H(OrderDropperModule.k.f);
      } else if (this.w() && (this.M() || this.s() > 0)) {
         this.Y("Storage unavailable with items still to store. Select more shulkers or rescan, then enable again.");
      } else {
         this.H(OrderDropperModule.k.K);
      }
   }

   private void H() {
      this.Vh = "Finishing";
      if (!this.w() || this.VC >= 20 || !(Math.abs(Mth.wrapDegrees(mc.player.getYRot() - this.VK)) > 1.0F) && !(Math.abs(mc.player.getXRot() - this.Vi) > 1.0F)
         )
       {
         this.F(
            "Done: " + this.Vy + " pages dropped, " + this.Gr + " stacks taken" + (this.w() ? ", " + this.f + " stacks stored in " + this.A + " trips." : ".")
         );
         r4.V("OrderDropper", "Finished pages={} taken={} stored={} trips={}", this.Vy, this.Gr, this.f, this.A);
         this.P = (boolean)0;
         this.disable();
      } else {
         ep.t(this, ep.z.R, this.VK, this.Vi, 0.5);
      }
   }

   private void I(Vec3 var1, double var2) {
      if (!this.n()) {
         double var4 = this.N(var1);
         if (var4 < this.VX - 0.05) {
            this.VX = var4;
            this.V = 0;
         } else if (++this.V > 50) {
            this.Y("The way is blocked; stopped where it was.");
            return;
         }

         Vec3 var6 = var1.subtract(mc.player.position());
         float var7 = (float)Math.toDegrees(Math.atan2(var6.z, var6.x)) - 90.0F;
         ep.t(this, ep.z.R, var7, 25.0F, 0.35);
         this.VG = (boolean)(Math.abs(Mth.wrapDegrees(mc.player.getYRot() - var7)) < 35.0F ? 1 : 0);
         this.t = (boolean)(this.VG && mc.player.horizontalCollision && mc.player.onGround() ? 1 : 0);
         this.VH = (boolean)(var2 > 0.0 && var4 < 1.0 ? 1 : 0);
      }
   }

   public static Input Q() {
      if (Y != null && Y.isEnabled()) {
         OrderDropperModule var0 = Y;
         if (var0 == null || !var0.isRuntimeActive() || getMc().gui.screen() != null || rr.o() || ep.f() && !ep.W(ep.z.R)) {
            return null;
         } else if (!var0.VG) {
            return H() ? new Input(false, false, false, false, false, false, false) : null;
         } else {
            return new Input(true, false, false, false, var0.t, var0.VH, false);
         }
      } else {
         return null;
      }
   }

   private boolean I() {
      if (mc.gui.screen() == null && mc.isWindowActive()) {
         Options var1 = mc.options;
         return yu.X(var1.keyUp) || yu.X(var1.keyDown) || yu.X(var1.keyLeft) || yu.X(var1.keyRight) || yu.X(var1.keyJump);
      } else {
         return false;
      }
   }

   private void h() {
      Minecraft var1 = getMc();
      if (var1 != null && var1.getWindow() != null) {
         if (this.e != null && var1.level != this.e && !this.isRuntimeActive()) {
            this.v = (boolean)0;
            this.B = null;
            this.m();
            this.GF.clear();
            this.Vz.clear();
            this.i = null;
            this.e = null;
         }

         if (this.v) {
            if (!this.isInGame() || var1.level != this.e) {
               this.v = (boolean)0;
               this.B = null;
            } else if (!this.isRuntimeActive() && var1.gui.screen() == null) {
               if (System.nanoTime() - this.X > 2000000000L) {
                  this.L();
               }
            } else {
               this.O();
            }
         }
      }
   }

   private void u() {
      if (this.e != mc.level) {
         this.m();
         this.GF.clear();
         this.Vz.clear();
         this.i = null;
         this.e = mc.level;
      }
   }

   private void o() {
      this.u();
      this.v = (boolean)1;
      this.i = this.z = this.B = null;
      this.Vs = this.V3 = this.I = this.VV = this.Vg = (boolean)0;
      if (mc.gameMode != null) {
         mc.gameMode.stopDestroyBlock();
      }

      this.L();
      r4.g("OrderDropper", "Selection started with {} shulkers", this.GF.size());
   }

   private void O() {
      this.v = (boolean)0;
      this.B = this.z = null;
      this.I = this.VV = this.Vg = (boolean)0;
      this.a("Shulker selection saved. " + this.GF.size() + " selected.");
      r4.V("OrderDropper", "Selection finished with {} shulkers", this.GF.size());
   }

   private void L() {
      int var1 = this.u.getValue();
      this.a(
         this.GF.size()
            + " selected. Right-click adds, left-click removes, hold to sweep, sneak-click for an area. "
            + (pe.F(var1) ? pe.A(var1) + " or Esc" : "Esc")
            + " to finish."
      );
   }

   public static boolean O() {
      OrderDropperModule var0 = Y;
      return var0 != null && var0.v;
   }

   private void P() {
      if (!this.isInGame()) {
         this.B = null;
         this.VZ = (boolean)0;
      } else {
         this.Z();
         int var1 = this.VZ && !this.V_ ? 1 : 0;
         if ((this.v || var1 != 0) && mc.gui.screen() == null) {
            HitResult var2 = mc.player.pick(24.0, mc.getDeltaTracker().getGameTimeDeltaPartialTick(false), false);
            this.B = var2 instanceof BlockHitResult var3 && var2.getType() == Type.BLOCK && this.Z(var3.getBlockPos()) ? var3.getBlockPos().immutable() : null;
            if (var1 != 0) {
               this.a(this.B);
            } else {
               boolean var7 = yu.X(mc.options.keyUse);
               boolean var4 = yu.X(mc.options.keyAttack);
               if (!var7) {
                  this.Vs = (boolean)1;
               }

               if (!var4) {
                  this.V3 = (boolean)1;
               }

               int var5 = var7 && this.Vs ? 1 : 0;
               int var6 = var4 && this.V3 && var5 == 0 ? 1 : 0;
               if ((var5 == 0 || this.I) && (var6 == 0 || this.VV)) {
                  if ((var5 != 0 || var6 != 0) && !this.Vg && this.B != null && !this.B.equals(this.z)) {
                     if (this.l(this.B, (boolean)var5)) {
                        this.a((boolean)var5, 1);
                     }

                     this.z = this.i = this.B;
                  }
               } else {
                  this.m((boolean)var5);
               }

               this.I = (boolean)var5;
               this.VV = (boolean)var6;
            }
         } else {
            this.B = null;
         }
      }
   }

   private void Z() {
      int var1 = this.u.getValue();
      int var2 = pe.F(var1) && mc.gui.screen() == null && mc.isWindowActive() && pe.A(mc.getWindow().handle(), var1) ? 1 : 0;
      if (var2 != 0 && !this.VZ) {
         this.V_ = this.isRuntimeActive();
         if (this.V_) {
            this.a("Turn Order Dropper off before changing shulkers.");
         } else {
            this.u();
            this.VQ = null;
            this.S = null;
            this.a = 0;
            this.Vf = System.nanoTime();
         }
      } else if (var2 == 0 && this.VZ && !this.V_) {
         if (this.VQ == null && System.nanoTime() - this.Vf < 400000000L) {
            if (this.v) {
               this.O();
            } else {
               this.o();
            }
         } else if (this.VQ != null) {
            r4.V("OrderDropper", "Key sweep {} {} shulkers; total {}", this.VQ ? "added" : "removed", this.a, this.GF.size());
         }
      }

      this.VZ = (boolean)var2;
   }

   private void a(BlockPos var1) {
      if (var1 != null && !var1.equals(this.S)) {
         this.S = var1;
         if (this.VQ == null) {
            this.VQ = !this.GF.contains(var1);
         }

         if (this.l(var1, this.VQ)) {
            this.i = var1;
            this.a(this.VQ, ++this.a);
         }
      }
   }

   private void m(boolean var1) {
      this.z = null;
      this.Vg = (boolean)0;
      if (this.B == null) {
         this.a("Look at a placed shulker box.");
      } else {
         if (yu.X(mc.options.keyShift) && this.i != null && !this.i.equals(this.B)) {
            this.Vg = (boolean)1;
            List var2 = xn.E(E(this.i), E(this.B));
            if (var2.isEmpty()) {
               this.a("That area is too large. Keep each side within 32 blocks.");
               return;
            }

            int var3 = 0;

            for (xn.y var5 : var2) {
               if (this.l(new BlockPos(var5.x(), var5.y(), var5.z()), var1)) {
                  var3++;
               }
            }

            this.a(var1, var3);
         } else {
            if (this.l(this.B, var1)) {
               this.a(var1, 1);
            } else {
               this.a((var1 ? "Already selected; left-click removes it. " : "Not selected. ") + this.GF.size() + " selected.");
            }

            this.z = this.B;
         }

         this.i = this.B;
      }
   }

   private boolean l(BlockPos var1, boolean var2) {
      if (var2 ? !this.GF.contains(var1) && this.Z(var1) : this.GF.remove(var1)) {
         if (var2) {
            this.GF.add(var1.immutable());
         } else {
            this.n.remove(var1);
            this.G.remove(var1);
            this.GO.remove(var1);
            this.V2.remove(var1);
         }

         this.Vz.put(var1.immutable(), new OrderDropperModule.x(System.nanoTime(), var2));
         return true;
      } else {
         return false;
      }
   }

   private void a(boolean var1, int var2) {
      this.a((var1 ? "Added " : "Removed ") + var2 + (var2 == 1 ? " shulker. " : " shulkers. ") + this.GF.size() + " selected.");
      r4.g("OrderDropper", "{} {} shulkers; total {}", var1 ? "Added" : "Removed", var2, this.GF.size());
   }

   private static xn.y E(BlockPos var0) {
      return new xn.y(var0.getX(), var0.getY(), var0.getZ());
   }

   private int j(BlockPos var1, boolean var2) {
      return this.GO.contains(var1) ? -1876386 : (this.n.contains(var1) ? -2061766 : (this.T(var1, var2) ? -11702 : -13180806));
   }

   private static int r(int var0) {
      return var0 == -11702 ? -10733056 : (var0 == -2061766 ? -11918843 : (var0 == -1876386 ? -11923692 : -16040668));
   }

   private boolean T(BlockPos var1, boolean var2) {
      return var2 && var1.equals(this.N) && (this.Vv == OrderDropperModule.k.C || this.Vv == OrderDropperModule.k.T || this.Vv == OrderDropperModule.k.G);
   }

   private String a(BlockPos var1, boolean var2) {
      if (this.GO.contains(var1)) {
         return "Skipped";
      }

      if (this.T(var1, var2)) {
         return this.p ? "Scanning" : (this.Vv == OrderDropperModule.k.G ? "Filling" : (this.Vv == OrderDropperModule.k.C ? "Next" : "Opening"));
      }

      if (this.n.contains(var1)) {
         return "Full";
      }

      Integer var3 = this.G.get(var1);
      return var3 == null ? null : var3 + " free";
   }

   private void M() {
      Minecraft var1 = getMc();
      if (var1 != null && var1.level != null) {
         try {
            this.P();
            if (var1.level != this.e) {
               return;
            }

            long var2 = System.nanoTime();
            if (!this.GF.isEmpty()) {
               this.w(var1.gameRenderer.mainCamera().position());
            }

            this.m(var2);
            if (this.B != null) {
               this.a();
            }
         } catch (RuntimeException var4) {
            r4.T("OrderDropper", "Shulker highlight failed", var4);
         }
      }
   }

   private void w(Vec3 var1) {
      boolean var2 = this.isRuntimeActive();

      for (BlockPos var4 : this.GF) {
         String var5 = this.a(var4, var2);
         Vec3 var6 = Vec3.atCenterOf(var4);
         if (var5 != null && var1.distanceToSqr(var6) < 100.0) {
            Gizmos.billboardText(var5, var6, Style.forColorAndCentered(-854792).withScale(0.4F)).setAlwaysOnTop();
         }
      }
   }

   private void m(long var1) {
      Iterator var3 = this.Vz.entrySet().iterator();

      while (var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         double var5 = 1.0 - (var1 - ((OrderDropperModule.x)var4.getValue()).at()) / 4.5E8;
         if (var5 <= 0.0) {
            var3.remove();
         } else {
            int var7 = ((OrderDropperModule.x)var4.getValue()).added() ? -13180806 : -1017711;
            Gizmos.cuboid(new AABB((BlockPos)var4.getKey()).inflate(0.02 + 0.06 * (1.0 - var5)), GizmoStyle.fill(oh.i(var7, 0.4 * var5))).setAlwaysOnTop();
         }
      }
   }

   private void a() {
      boolean var1 = this.GF.contains(this.B);
      Gizmos.cuboid(new AABB(this.B).inflate(0.012), GizmoStyle.strokeAndFill(-854792, 2.5F, oh.i(var1 ? -1017711 : -13180806, 0.2))).setAlwaysOnTop();
      if (this.i != null && !this.i.equals(this.B) && !this.VZ && yu.X(mc.options.keyShift)) {
         int var2 = !xn.E(E(this.i), E(this.B)).isEmpty() ? 1 : 0;
         AABB var3 = new AABB(this.i).minmax(new AABB(this.B)).inflate(0.03);
         Gizmos.cuboid(var3, GizmoStyle.strokeAndFill(var2 != 0 ? -854792 : -1017711, 1.5F, oh.i(var2 != 0 ? -854792 : -1017711, 0.06))).setAlwaysOnTop();
      }
   }

   public static OrderDropperModule Y() {
      return Y;
   }

   public void l(PoseStack var1, y2 var2, Vec3 var3) {
      if (this.isEnabled()) {
         Minecraft var4 = getMc();
         if (var4 != null && var4.level != null && var4.level == this.e && !this.GF.isEmpty()) {
            long var5 = h_.U();

            try {
               this.D(this.isRuntimeActive());
               VertexConsumer var7 = uv.i(var2, this.w());
               Pose var8 = var1.last();
               double var9 = ea.y();
               double var11 = var9 * 0.08 % 1.0;
               double var13 = 0.5 + 0.5 * Math.sin(var9 * 6.0);
               double var15 = this.V0.getValue();

               for (int var17 = 0; var17 < this.VP.size(); var17++) {
                  double[] var18 = this.VP.get(var17);
                  int var19 = this.T.get(var17);
                  double var20 = var19 == -11702 ? var15 * (0.7 + 0.3 * var13) : var15;

                  for (int var22 = 0; var22 < 4; var22++) {
                     int var23 = var22 * 5;
                     uv.j(
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
                        r(var19),
                        var20,
                        0.6,
                        1.0,
                        var11
                     );
                  }
               }
            } finally {
               h_.n("Order Dropper selection", var5);
            }
         }
      }
   }

   private uv.w w() {
      return switch ((String)this.V7.getValue()) {
         case "Glow" -> uv.w.K;
         case "Metallic" -> uv.w.x;
         case "Marble" -> uv.w.n;
         case "Glitter" -> uv.w.g;
         case "Solid" -> uv.w.r;
         case "Ghost" -> uv.w.A;
         default -> uv.w.p;
      };
   }

   private void D(boolean var1) {
      long var2 = this.GF.size();

      for (BlockPos var5 : this.GF) {
         var2 = var2 * 31L + var5.asLong() * 17L + this.j(var5, var1);
      }

      if (var2 != this.M) {
         this.M = var2;
         this.VP.clear();
         this.T.clear();
         HashSet var36 = new HashSet<>(this.GF);

         for (BlockPos var6 : this.GF) {
            int var7 = this.j(var6, var1);

            for (Direction var11 : Direction.values()) {
               if (!var36.contains(var6.relative(var11))) {
                  Direction[] var12 = Vm[var11.ordinal()];
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
                     double var30 = a(var6.getX(), var11.getStepX(), var13.getStepX(), var14.getStepX(), var26, var28);
                     double var32 = a(var6.getY(), var11.getStepY(), var13.getStepY(), var14.getStepY(), var26, var28);
                     double var34 = a(var6.getZ(), var11.getStepZ(), var13.getStepZ(), var14.getStepZ(), var26, var28);
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
                  this.VP.add(var23);
                  this.T.add(var7);
               }
            }
         }
      }
   }

   private static double a(int var0, int var1, int var2, int var3, double var4, double var6) {
      double var8 = var1 > 0 ? var0 + 1 + 0.012 : (var1 < 0 ? var0 - 0.012 : var0);
      return var8 + (var2 != 0 ? var4 : 0.0) + (var3 != 0 ? var6 : 0.0);
   }

   private void H(OrderDropperModule.k var1) {
      if (this.Vv != var1) {
         r4.g("OrderDropper", "Phase {} -> {}", this.Vv, var1);
      }

      this.Vv = var1;
      this.VC = 0;
      if (var1 == OrderDropperModule.k.C || var1 == OrderDropperModule.k.f) {
         this.VX = Double.MAX_VALUE;
         this.V = 0;
      }
   }

   private int q() {
      return this.VJ.getValue().intValue();
   }

   private float e() {
      return mc.player.getHealth() + mc.player.getAbsorptionAmount();
   }

   private double P() {
      return Math.min(mc.player.blockInteractionRange(), 4.5);
   }

   private double N(Vec3 var1) {
      Vec3 var2 = mc.player.position();
      return Math.hypot(var1.x - var2.x, var1.z - var2.z);
   }

   private boolean Z(BlockPos var1) {
      return var1 != null && mc.level.hasChunkAt(var1) && mc.level.getBlockState(var1).getBlock() instanceof ShulkerBoxBlock;
   }

   private boolean N(ItemStack var1) {
      if (!var1.isEmpty() && !this.Vx.isEmpty()) {
         if (!this.Vx.equals(this.GY)) {
            Identifier var2 = Identifier.tryParse(this.Vx);
            this.w = var2 == null ? null : (Item)BuiltInRegistries.ITEM.getOptional(var2).orElse(null);
            this.GY = this.Vx;
         }

         return var1.getItem() == this.w;
      } else {
         return false;
      }
   }

   private int A() {
      int var1 = 0;

      for (int var2 = 0; var2 < 36; var2++) {
         if (mc.player.getInventory().getItem(var2).isEmpty()) {
            var1++;
         }
      }

      return var1;
   }

   private int V() {
      int var1 = 0;

      for (int var2 = 0; var2 < 36; var2++) {
         if (!this.VS.contains(var2) && this.N(mc.player.getInventory().getItem(var2))) {
            var1++;
         }
      }

      return var1;
   }

   private boolean M() {
      return this.V() > 0;
   }

   private int G() {
      this.V();
      int var1 = 0;

      for (BlockPos var3 : this.j()) {
         if (!this.n.contains(var3) && !this.GO.contains(var3)) {
            var1 += this.G.getOrDefault(var3, 0);
         }
      }

      return var1;
   }

   private void e(ShulkerBoxMenu var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (var1.getSlot(var3).getItem().isEmpty()) {
            var2++;
         }
      }

      this.G.put(this.N, var2);
      this.V2.put(this.N, mc.level.getBlockEntity(this.N));
      this.GO.remove(this.N);
      if (var2 == 0) {
         this.n.add(this.N);
      } else {
         this.n.remove(this.N);
      }

      r4.V("OrderDropper", "Scanned shulker {}: free slots={}", this.N.toShortString(), var2);
   }

   private void V() {
      for (BlockPos var2 : this.j()) {
         if (!this.Z(var2)) {
            if (this.GO.add(var2)) {
               r4.m("OrderDropper", "Selected shulker {} is missing or unloaded; capacity removed", var2.toShortString());
            }

            this.G.remove(var2);
            this.n.remove(var2);
            this.V2.remove(var2);
         } else if (this.V2.containsKey(var2) && this.V2.get(var2) != mc.level.getBlockEntity(var2)) {
            this.G.remove(var2);
            this.n.remove(var2);
            this.V2.remove(var2);
         }
      }
   }

   private static AABB C(BlockPos var0, Direction var1) {
      return new AABB(var0).expandTowards(var1.getStepX() * 0.5, var1.getStepY() * 0.5, var1.getStepZ() * 0.5).inflate(0.1);
   }

   private AABB K() {
      return C(this.N, (Direction)mc.level.getBlockState(this.N).getValue(ShulkerBoxBlock.FACING));
   }

   private boolean c() {
      return !this.K().intersects(mc.player.getBoundingBox());
   }

   private Vec3 u() {
      return K(
         this.N,
         mc.player.position(),
         mc.player.getBoundingBox(),
         this.K(),
         var0 -> mc.level.noCollision(mc.player, var0),
         var1 -> !this.V8.contains(var1) && this.Z(var1.add(0.0, mc.player.getEyeHeight(), 0.0)) != null
      );
   }

   private BlockHitResult Z(Vec3 var1) {
      double var2 = this.P();
      if (c(this.N, var1) > var2 * var2) {
         return null;
      }

      List var4 = mc.level.getEntities(mc.player, new AABB(this.N).minmax(new AABB(var1, var1)).inflate(1.0), var0 -> var0.isPickable() && !var0.isSpectator());
      return B(this.N, var1, var2, (var1x, var2x) -> {
         BlockHitResult var3 = mc.level.clip(new ClipContext(var1x, var2x, Block.OUTLINE, Fluid.NONE, mc.player));
         if (var3.getType() != Type.BLOCK) {
            return null;
         }

         for (Entity var5 : var4) {
            if (d_.c(var1x, var3.getLocation(), var5.getBoundingBox())) {
               return null;
            }
         }

         return var3;
      });
   }

   private static double c(BlockPos var0, Vec3 var1) {
      double var2 = Math.clamp(var1.x, var0.getX(), var0.getX() + 1.0);
      double var4 = Math.clamp(var1.y, var0.getY(), var0.getY() + 1.0);
      double var6 = Math.clamp(var1.z, var0.getZ(), var0.getZ() + 1.0);
      return var1.distanceToSqr(var2, var4, var6);
   }

   private static BlockHitResult B(BlockPos var0, Vec3 var1, double var2, BiFunction<Vec3, Vec3, BlockHitResult> var4) {
      if (Double.isFinite(var2) && !(var2 <= 0.0) && !(c(var0, var1) > var2 * var2 + 1.0E-7)) {
         ArrayList var5 = new ArrayList(28);
         var5.add(Vec3.atCenterOf(var0));
         Vec3 var6 = Vec3.atCenterOf(var0);

         for (Direction var10 : Direction.values()) {
            double var11 = (var1.x - var6.x) * var10.getStepX() + (var1.y - var6.y) * var10.getStepY() + (var1.z - var6.z) * var10.getStepZ();
            if (!(var11 <= 0.5)) {
               for (Vec3 var14 : com.larpclient.m.S(var0, var10)) {
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
                  float[] var19 = ep.g(var1, u(var0, var18));
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

   private static Vec3 u(BlockPos var0, BlockHitResult var1) {
      Vec3 var2 = var1.getLocation();
      return new Vec3(
         Math.clamp(var2.x, var0.getX() + 0.02, var0.getX() + 0.98),
         Math.clamp(var2.y, var0.getY() + 0.02, var0.getY() + 0.98),
         Math.clamp(var2.z, var0.getZ() + 0.02, var0.getZ() + 0.98)
      );
   }

   private static Vec3 K(BlockPos var0, Vec3 var1, AABB var2, AABB var3, Predicate<AABB> var4, Predicate<Vec3> var5) {
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

   private boolean n() {
      if (!d8.a() || ep.f() && !ep.W(ep.z.R)) {
         this.Vh = "Waiting for camera or rotation control";
         this.VC = Math.max(0, this.VC - 1);
         return true;
      } else {
         return false;
      }
   }

   public static boolean H() {
      if (Y != null && Y.isEnabled()) {
         OrderDropperModule var0 = Y;
         return var0 != null && var0.isRuntimeActive() && var0.Vv != null && var0.Vd.getValue();
      } else {
         return false;
      }
   }

   private void q() {
      if (!this.Vd.getValue()) {
         this.E();
      } else {
         if (!this.VI) {
            this.VL = mc.options.pauseOnLostFocus;
            this.VI = (boolean)1;
            r4.V("OrderDropper", "AFK mouse released; pause on focus loss temporarily disabled");
         }

         mc.options.pauseOnLostFocus = (boolean)0;
         if (mc.mouseHandler.isMouseGrabbed()) {
            mc.mouseHandler.releaseMouse();
         }
      }
   }

   private void E() {
      if (this.VI) {
         mc.options.pauseOnLostFocus = this.VL;
         this.VI = (boolean)0;
         r4.V("OrderDropper", "AFK ended; pause on focus loss restored");
      }
   }

   private int s() {
      Vec3 var1 = this.G2 == null ? mc.player.position() : this.G2;
      AABB var2 = new AABB(var1, var1).inflate(2.5, 1.5, 2.5);
      return mc.level.getEntitiesOfClass(ItemEntity.class, var2, var1x -> var1x.isAlive() && this.N(var1x.getItem())).size();
   }

   private static int m(AbstractContainerMenu var0) {
      int var1 = 31 * var0.containerId + var0.getStateId();

      for (Slot var3 : var0.slots) {
         var1 = 31 * var1 + ItemStack.hashItemAndComponents(var3.getItem()) * 7 + var3.getItem().getCount();
      }

      ItemStack var4 = var0.getCarried();
      return 31 * var1 + ItemStack.hashItemAndComponents(var4) + var4.getCount();
   }

   private static List<String> J(ItemStack var0) {
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

   private static String O(ItemStack var0) {
      StringBuilder var1 = new StringBuilder(h(var0));

      for (String var3 : J(var0)) {
         if (!xn.V(var3).contains("until")) {
            var1.append('|').append(var3);
         }
      }

      return var1.toString();
   }

   private static String h(ItemStack var0) {
      return BuiltInRegistries.ITEM.getKey(var0.getItem()).toString();
   }

   private void m(String var1) {
      this.F(var1);
      r4.V("OrderDropper", "Not started: {}", var1);
      this.disable();
   }

   private void Y(String var1) {
      r4.m("OrderDropper", "Stopped in phase {}: {}", this.Vv, var1);
      this.F("Stopped: " + var1);
      this.disable();
   }

   private void a(String var1) {
      this.X = System.nanoTime();
      if (mc.player != null) {
         mc.player.sendOverlayMessage(Component.literal(var1));
      }
   }

   private void l(String var1) {
      if (this.VB.getValue()) {
         this.F(var1);
      }
   }

   private void F(String var1) {
      if (this.isInGame()) {
         mc.gui.chatListener().handleSystemMessage(Component.literal("[Order Dropper] " + var1), false);
      }
   }

   static {
      Vm[Direction.DOWN.ordinal()] = new Direction[]{Direction.EAST, Direction.SOUTH};
      Vm[Direction.UP.ordinal()] = new Direction[]{Direction.SOUTH, Direction.EAST};
      Vm[Direction.NORTH.ordinal()] = new Direction[]{Direction.UP, Direction.EAST};
      Vm[Direction.SOUTH.ordinal()] = new Direction[]{Direction.EAST, Direction.UP};
      Vm[Direction.WEST.ordinal()] = new Direction[]{Direction.SOUTH, Direction.UP};
      Vm[Direction.EAST.ordinal()] = new Direction[]{Direction.UP, Direction.SOUTH};
   }

   private enum k {
      j,
      H,
      C,
      T,
      G,
      f,
      K;
   }

   private enum v {
      k,
      u;
   }

   private record x(long at, boolean added) {
   }
}

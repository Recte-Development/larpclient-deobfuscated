package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.b6;
import com.larpclient.h8;
import com.larpclient.he;
import com.larpclient.hz;
import com.larpclient.ij;
import com.larpclient.jz;
import com.larpclient.nz;
import com.larpclient.p4;
import com.larpclient.vi;
import com.larpclient.vv;
import com.larpclient.w7;
import com.larpclient.wh;
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
   public static final String e = "Drop Only";
   public static final String I = "Fill Shulkers";
   private static final int H_ = 20;
   private static final int r = 60;
   private static final int V = 60;
   private static final int C = 120;
   private static final int HQ = 50;
   private static final int HA = 3;
   private static final int A = 3;
   private static final int D = -13180806;
   private static final int n = -11702;
   private static final int H3 = -2061766;
   private static final int H6 = -1876386;
   private static final int eL = -854792;
   private static final int Hu = -1017711;
   private static final double Hk = 24.0;
   private static final double c = 10.0;
   private static final int Hr = 32;
   private static final long L = 450000000L;
   private static final long x = 2000000000L;
   private static final long E = 400000000L;
   private static final double q = 0.012;
   private static OrderDropperModule X;
   private final com.larpclient.module.o Hj = this.addSetting(
      new com.larpclient.module.o(
         "Mode",
         "Drop Only drops every page where you stand. Fill Shulkers drops a page at your feet, picks it up, stores it in the selected shulkers and walks back, until the orders are empty",
         "Fill Shulkers",
         "Drop Only",
         "Fill Shulkers"
      )
   );
   private final e eJ = this.addSetting(
         new e("Order Item", "Item whose orders are emptied, such as totem_of_undying. Use the held-item button to fill it in", "")
      )
      .L("totem_of_undying");
   private final x H7 = this.addSetting(
      new x(
         "Select Shulker Key",
         "Hold and sweep your crosshair over shulker boxes to select them; a sweep that starts on a selected box removes instead. Tap it for click selection: right-click adds, left-click removes, sneak-click covers an area",
         -1
      )
   );
   private final b z = this.addSetting(new b("Click Delay", "Ticks between menu clicks and item moves", 4.0, 2.0, 20.0, 1.0));
   private final m Ht = this.addSetting(
      new m("Stop When Hurt", "Stop at once when you take damage. Menus block Auto Totem, so this protects you while it works", true)
   );
   private final m Z = this.addSetting(new m("Show Messages", "Report orders, pages, trips and the final summary in chat", true));
   private final b j = this.addSetting(
      new b("Fill Delay", "Ticks between shulker transfers. Lower is faster. Reopens the box to verify the result before leaving", 2.0, 1.0, 20.0, 1.0)
   );
   private final m HX = this.addSetting(
      new m("AFK Mode", "Release the mouse and keep running when you switch windows. Your pause-on-focus-loss preference is restored when stopped", false)
   );
   private final com.larpclient.module.o K = this.addSetting(
      new com.larpclient.module.o(
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
   private final b l = this.addSetting(new b("Highlight Opacity", "How strongly the material covers selected shulkers", 0.85, 0.2, 1.0, 0.05));
   private final List<BlockPos> i = new ArrayList<>();
   private final Set<BlockPos> H0 = new HashSet<>();
   private final Map<BlockPos, Integer> HK = new HashMap<>();
   private final Set<BlockPos> HG = new HashSet<>();
   private final Map<BlockPos, Object> o = new HashMap<>();
   private Level k;
   private boolean g;
   private boolean Hh;
   private Boolean HT;
   private BlockPos N;
   private int HD;
   private long H1;
   private final Map<BlockPos, OrderDropperModule.i> e9 = new HashMap<>();
   private boolean HC;
   private boolean Ha;
   private boolean J;
   private boolean Hq;
   private boolean Hw;
   private boolean p;
   private BlockPos eh;
   private BlockPos eA;
   private BlockPos HJ;
   private long HV;
   private final List<double[]> eu = new ArrayList<>();
   private final List<Integer> Hx = new ArrayList<>();
   private long HE = Long.MIN_VALUE;
   private String P;
   private Item m;
   private Object t;
   private Object a;
   private Object u;
   private OrderDropperModule.r e7;
   private String O = "";
   private String HY = "";
   private String H5 = "Idle";
   private Vec3 W;
   private float Hv;
   private float Y;
   private float F;
   private final Set<String> Hd = new HashSet<>();
   private final Set<Integer> G = new HashSet<>();
   private boolean d;
   private boolean H8;
   private boolean HI;
   private boolean HZ;
   private int b;
   private int H4;
   private int Hz;
   private int H9;
   private int Hs;
   private int er;
   private int Hp;
   private int B;
   private int Hi;
   private OrderDropperModule.o eb;
   private int Hb;
   private int S;
   private int U;
   private AbstractContainerMenu HW;
   private AbstractContainerMenu HS;
   private int Hc;
   private int HL;
   private int Hn;
   private int M;
   private BlockPos Ho;
   private double HH;
   private int Q;
   private int v;
   private int y;
   private int HP;
   private boolean T;
   private boolean s;
   private boolean HN;
   private int Hg;
   private int Hl;
   private int HM;
   private int HF;
   private boolean H;
   private boolean H2;
   private boolean Hm;
   private boolean He;
   private boolean f;
   private Vec3 HR;
   private final Set<Vec3> HB = new HashSet<>();
   private boolean HU;
   private boolean ef;
   private int Hy;
   private int w;
   private int HO;
   private int h;
   private int Hf;
   private int e_;
   private static final Direction[][] R = new Direction[6][];

   public OrderDropperModule() {
      super("Order Dropper", "Empty your DonutSMP orders for one item by dropping pages, or fill selected shulkers with them", Category.PLAYER, -1);
      X = this;
      this.j.a(this::T);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var1 -> this.J());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.v());
   }

   public List<com.larpclient.module.r<?>> q() {
      return List.of(this.Hj, this.eJ);
   }

   public List<com.larpclient.module.r<?>> D() {
      return List.of(this.H7, this.j, this.K, this.l);
   }

   public List<com.larpclient.module.r<?>> l() {
      return List.of(this.z, this.HX, this.Ht, this.Z);
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.k);
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.H5 : (this.H ? "Paused" : "Idle");
   }

   private boolean T() {
      return this.Hj.w("Fill Shulkers");
   }

   public String S() {
      String var1 = com.larpclient.y.r(this.eJ.getValue());
      String var2 = var1.isEmpty() ? "No order item chosen" : "Item: " + var1;
      int var3 = this.u().size();
      return var2
         + ". "
         + var3
         + (var3 == 1 ? " shulker" : " shulkers")
         + " selected"
         + (var3 == 0 ? "" : ", " + this.HK.size() + " scanned, " + this.H0.size() + " full, " + this.HG.size() + " unavailable")
         + (this.H ? ". Progress saved for this world session." : ".");
   }

   public void r() {
      if (!this.isInGame()) {
         throw new IllegalArgumentException("Join a world first.");
      }

      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before changing shulkers.");
      }

      mc.gui.setScreen(null);
      this.P();
   }

   public void V() {
      if (this.isInGame() && !mc.player.getMainHandItem().isEmpty()) {
         this.eJ.T(n(mc.player.getMainHandItem()));
         LarpClient var1 = LarpClient.N();
         if (var1 != null && var1.h() != null) {
            var1.h().z();
         }
      } else {
         throw new IllegalArgumentException("Hold the order's item first.");
      }
   }

   public void j() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before changing shulkers.");
      }

      this.A();
      this.i.clear();
      this.eA = null;
   }

   public void A() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before resetting progress.");
      }

      this.O();
      this.s();
      this.HK.clear();
      this.H0.clear();
      this.HG.clear();
      this.o.clear();
      this.H = (boolean)0;
   }

   public void C() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before rescanning shulkers.");
      }

      this.HK.clear();
      this.H0.clear();
      this.HG.clear();
      this.o.clear();
   }

   private List<BlockPos> u() {
      Minecraft var1 = getMc();
      return var1 != null && var1.level == this.k ? this.i : List.of();
   }

   @Override
   public void onEnable() {
      if (this.isInGame() && mc.player.connection != null) {
         if (this.HC) {
            this.H();
         }

         String var1 = com.larpclient.y.r(this.eJ.getValue());
         int var2 = this.H && this.t == mc.player && this.a == mc.level && this.u == mc.player.connection && this.O.equals(var1) && this.T() ? 1 : 0;
         if (var2 == 0) {
            this.s();
            this.H = (boolean)0;
         }

         this.O = var1;
         if (this.O.isEmpty() || !BuiltInRegistries.ITEM.containsKey(Identifier.tryParse(this.O))) {
            this.k("Choose the Order Item first, for example totem_of_undying, or hold it and use the held-item button.");
         } else if (this.T() && !((Item)BuiltInRegistries.ITEM.getValue(Identifier.parse(this.O))).canFitInsideContainerItems()) {
            this.k("This item cannot go inside a shulker box. Choose Drop Only for shulker box orders.");
         } else if (this.T() && this.u().isEmpty()) {
            this.k("Select at least one placed shulker box first: look at it and press the Select Shulker Key.");
         } else {
            this.t = mc.player;
            this.a = mc.level;
            this.u = mc.player.connection;
            if (var2 == 0) {
               this.W = mc.player.position();
               this.Hv = mc.player.getYRot();
               this.Y = mc.player.getXRot();

               for (int var3 = 0; var3 < 36; var3++) {
                  if (this.y(mc.player.getInventory().getItem(var3))) {
                     this.G.add(var3);
                  }
               }
            }

            this.F = this.e();
            if (var2 != 0 && this.Hm) {
               this.d = (boolean)0;
               this.Hm = (boolean)0;
            }

            this.H = this.T();
            this.HG.clear();
            this.q();
            this.H2 = (boolean)(this.T() && this.u().stream().anyMatch(var1x -> !this.HK.containsKey(var1x) && !this.HG.contains(var1x)) ? 1 : 0);
            if (this.H2) {
               this.w();
            } else if (var2 != 0 && this.X()) {
               this.w();
            } else {
               this.I(var2 != 0 ? OrderDropperModule.r.g : OrderDropperModule.r.z);
            }

            this.E(
               this.T()
                  ? (var2 != 0 ? "Resumed. " : "Started. ") + "Emptying " + this.O + " orders into " + this.u().size() + " shulkers."
                  : "Started. Dropping every page of your " + this.O + " orders here."
            );
            _q.z("OrderDropper", "Started mode={} item={} shulkers={} start={}", this.Hj.getValue(), this.O, this.u().size(), this.W);
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.t != null
         && this.isInGame()
         && mc.player == this.t
         && mc.player.containerMenu != mc.player.inventoryMenu
         && mc.player.containerMenu.getCarried().isEmpty()
         && this.v()) {
         mc.player.closeContainer();
      }

      if (this.Ho != null && (this.e7 == OrderDropperModule.r.x || this.HU || this.h > 0)) {
         this.HK.remove(this.Ho);
      }

      this.O();
      this.e7 = null;
      this.T = this.s = this.HN = (boolean)0;
      this.eb = null;
      this.Hz = 0;
      this.HI = this.HZ = (boolean)0;
      this.b = this.Hs = this.er = this.Hp = this.B = 0;
      this.Ho = null;
      this.HR = null;
      this.HB.clear();
      this.E();
   }

   private void s() {
      this.t = this.a = this.u = null;
      this.e7 = null;
      this.W = null;
      this.H5 = "Idle";
      this.Hd.clear();
      this.G.clear();
      this.d = this.H8 = this.HI = this.HZ = (boolean)0;
      this.b = this.H4 = this.Hz = this.H9 = this.Hs = this.er = this.Hp = this.B = this.Hi = 0;
      this.eb = null;
      this.U = 0;
      this.Ho = null;
      this.HY = "";
      this.Q = this.v = this.y = this.HP = 0;
      this.T = this.s = this.HN = (boolean)0;
      this.Hg = this.Hl = this.HM = this.HF = 0;
      this.H2 = this.HU = this.ef = this.Hm = (boolean)0;
      this.HR = null;
      this.Hy = this.w = this.HO = this.h = this.Hf = this.e_ = 0;
      this.HB.clear();
      this.E();
   }

   @Override
   public void onTick() {
      this.T = this.s = this.HN = (boolean)0;
      if (this.e7 != null && this.isInGame() && mc.player.connection != null) {
         if (mc.player == this.t && mc.level == this.a && mc.player.connection == this.u) {
            this.U();
            if (!mc.player.isAlive()) {
               this.f("You died.");
            } else {
               float var1 = this.e();
               if (this.Ht.getValue()) {
                  float var13;
                  int var10000 = (var13 = var1 - (this.F - 0.01F)) == 0.0F ? 0 : (var13 < 0.0F ? -1 : 1);
                  boolean var3 = false;
                  int var2 = var10000;
                  if ((Integer.signum(var2) >>> 31 ^ 1) == 0) {
                     this.f("You took damage.");
                     return;
                  }
               }

               this.F = var1;
               if (this.s()) {
                  this.f("You moved.");
               } else if (this.W != null && mc.player.getY() < this.W.y - 2.5) {
                  this.f("You fell below the start spot.");
               } else if (this.e7 != OrderDropperModule.r.z
                  && mc.gui.screen() instanceof ContainerScreen
                  && com.larpclient.y.U(mc.gui.screen().getTitle().getString()) != com.larpclient.y.z.G) {
                  this.H5 = "Closing Orders before continuing";
                  int var10 = ++this.b;
                  byte var7 = 100;
                  int var6 = var10;
                  if ((-Integer.compare(var6, var7) >>> 31 ^ 1) == 0) {
                     this.f("The Orders menu kept reopening.");
                  } else {
                     var10 = this.b % 4;
                     byte var5 = 1;
                     int var4 = var10;
                     var10 = Integer.compare(var4, var5);
                     if ((var10 | -var10) >>> 31 == 0) {
                        this.L();
                     }
                  }
               } else {
                  byte var9 = 1;
                  int var8 = this.H9;
                  this.H9 = var8 + var9;
                  switch (this.e7) {
                     case z:
                        this.o();
                        break;
                     case U:
                        this.z();
                        break;
                     case J:
                        this.G();
                        break;
                     case t:
                        this.f();
                        break;
                     case x:
                        this.X();
                        break;
                     case g:
                        this.S();
                        break;
                     case v:
                        this.F();
                  }
               }
            }
         } else {
            this.f("The world changed.");
         }
      }
   }

   private void o() {
      if (this.Hz > 0) {
         this.Hz--;
      } else {
         Screen var1 = mc.gui.screen();
         if (this.HZ) {
            if (var1 != null) {
               this.Hs = 0;
               if (++this.b > 100) {
                  this.f("The Orders menu kept reopening.");
                  return;
               }

               if (this.b % 4 == 1 && this.v()) {
                  this.L();
               }

               return;
            }

            if (++this.Hs < 4) {
               return;
            }

            this.HZ = (boolean)0;
         }

         if (var1 == null) {
            if (this.eb != null) {
               this.eb = null;
            }

            if (this.T()) {
               if (this.H8) {
                  this.I(OrderDropperModule.r.U);
                  return;
               }

               if (this.j() == 0) {
                  if (this.X()) {
                     this.w();
                  } else {
                     this.f("Your inventory is full of other items. Make room and enable again.");
                  }

                  return;
               }

               if (this.K() > 0) {
                  this.I(OrderDropperModule.r.U);
                  return;
               }

               if (this.d) {
                  this.g();
                  return;
               }

               if (mc.player.getXRot() < 88.0F) {
                  this.H5 = "Looking down";
                  b6.P(this, b6.w.d, mc.player.getYRot(), 90.0F, 0.5);
                  return;
               }
            } else if (this.d) {
               this.Z();
               return;
            }

            if (++this.Hs >= 5) {
               if (this.er <= 0 || ++this.er >= 60) {
                  if (++this.Hp > 3) {
                     this.f("The /orders menu did not open after 3 tries.");
                  } else {
                     this.H5 = "Opening /orders";
                     mc.player.connection.sendCommand("orders");
                     this.er = 1;
                     _q.c("OrderDropper", "Sent /orders (attempt {})", this.Hp);
                  }
               }
            }
         } else {
            this.Hs = 0;
            if (!(var1 instanceof AbstractContainerScreen)) {
               this.H5 = "Waiting for the open screen to close";
            } else {
               ChestMenu var2 = this.l();
               if (var2 == null) {
                  this.f("A different menu opened: " + var1.getTitle().getString() + ".");
               } else {
                  this.Hp = this.er = 0;
                  if (this.eb != null) {
                     int var5 = var2.containerId == this.Hb && O(var2) == this.S ? 0 : 1;
                     if (var5 != 0) {
                        this.eb = null;
                        this.Hz = this.B();
                        this.B = 0;
                     } else if (++this.U > (this.eb == OrderDropperModule.o.x ? 20 : 60)) {
                        if (this.eb == OrderDropperModule.o.x && ++this.B <= 3) {
                           this.eb = null;
                        } else {
                           this.f("The server did not respond to a menu click.");
                        }
                     }
                  } else {
                     ItemStack var3 = var2.getCarried();
                     if (var3.isEmpty() || this.y(var3) && com.larpclient.y.U(var1.getTitle().getString()) == com.larpclient.y.z.O) {
                        this.H4 = 0;
                        com.larpclient.y.z var4 = com.larpclient.y.U(var1.getTitle().getString());
                        if (this.V(var2, var4)) {
                           switch (var4) {
                              case s:
                                 this.H5 = "Opening Your Orders";
                                 this.o(var2, com.larpclient.y.q.y, OrderDropperModule.o.x);
                                 break;
                              case x:
                                 this.R(var2);
                                 break;
                              case u:
                                 this.H5 = "Opening Collect";
                                 this.o(var2, com.larpclient.y.q.N, OrderDropperModule.o.x);
                                 break;
                              case O:
                                 this.w(var2);
                                 break;
                              case G:
                                 this.f("An unexpected Orders menu opened: " + var1.getTitle().getString() + ".");
                           }
                        }
                     } else {
                        if (++this.H4 > 60) {
                           this.f("The cursor still holds " + var3.getHoverName().getString() + ".");
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static void M(int var0) {
      OrderDropperModule var1 = X;
      Minecraft var2 = getMc();
      if (var1 != null && var1.e7 == OrderDropperModule.r.z && var2 != null && var2.player != null) {
         AbstractContainerMenu var3 = var2.player.containerMenu;
         if (var3 instanceof ChestMenu && var3.containerId == var0) {
            var1.HW = var3;
            var1.HL = 0;
         }
      }
   }

   private void E() {
      this.HW = this.HS = null;
      this.Hc = this.HL = this.Hn = this.M = 0;
   }

   private boolean V(ChestMenu var1, com.larpclient.y.z var2) {
      int var3 = O(var1);
      if (this.HS == var1 && this.Hc == var3) {
         this.HL++;
      } else {
         this.HS = var1;
         this.Hc = var3;
         this.HL = 0;
      }

      String var4 = null;
      if (this.HW != var1) {
         var4 = "server contents";
      } else if (this.HL < 2) {
         var4 = "settled contents";
      } else if (var2 == com.larpclient.y.z.s && this.v(var1, com.larpclient.y.q.y) == -1) {
         var4 = "YOUR_ORDERS button";
      } else if (var2 == com.larpclient.y.z.u && this.v(var1, com.larpclient.y.q.N) == -1) {
         var4 = "COLLECT button";
      } else if (var2 == com.larpclient.y.z.x) {
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
         this.Hn = 0;
         return true;
      } else {
         this.H5 = "Waiting for Orders: " + var4;
         if (++this.Hn <= 60) {
            return false;
         } else if (++this.M > 3) {
            this.f("The Orders menu did not load " + var4 + " after 3 reloads. Progress retained.");
            return false;
         } else {
            _q.R(
               "OrderDropper",
               "Reloading Orders: missing={} title={} id={} synced={} retry={}/{}",
               var4,
               mc.gui.screen().getTitle().getString(),
               var1.containerId,
               this.HW == var1,
               this.M,
               3
            );
            this.Hn = 0;
            this.R();
            return false;
         }
      }
   }

   private void R(ChestMenu var1) {
      for (int var2 = 0; var2 < 45 && var2 < var1.getRowCount() * 9; var2++) {
         ItemStack var3 = var1.getSlot(var2).getItem();
         if (!var3.isEmpty() && com.larpclient.y.B(n(var3), this.O, u(var3))) {
            String var4 = P(var3);
            if (!this.Hd.contains(var4)) {
               this.HY = var4;
               this.Hi = 0;
               this.H5 = "Opening your " + var3.getHoverName().getString() + " order";
               _q.c("OrderDropper", "Opening order in slot {}: {}", var2, var4);
               this.D(var1, var2, ContainerInput.PICKUP, OrderDropperModule.o.x);
               return;
            }
         }
      }

      this.d = (boolean)1;
      this.E(this.Hd.isEmpty() ? "You have no " + this.O + " orders with items to collect." : "All " + this.O + " orders are empty.");
      _q.z("OrderDropper", "Orders finished; emptied={} pages={} taken={}", this.Hd.size(), this.Hg, this.Hl);
      this.R();
   }

   private void w(ChestMenu var1) {
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

      int var6 = this.v(var1, com.larpclient.y.q.W);
      int var5 = this.v(var1, com.larpclient.y.q.K);
      if (!var1.getCarried().isEmpty()) {
         this.k(var1);
      } else if (var2 != 0) {
         this.Hi = 0;
         if (var6 >= 0) {
            if (this.T() && this.H8) {
               this.R();
            } else if (this.T() && var2 + this.Z() > this.J()) {
               this.E("The selected shulkers have room for " + this.J() + " stacks; this page has " + var2 + ". Select more shulkers to continue.");
               _q.z("OrderDropper", "Not dropping: page={} carrying={} room={}", var2, this.Z(), this.J());
               this.d = (boolean)1;
               this.Hm = (boolean)1;
               this.R();
            } else {
               this.H5 = "Dropping page " + (this.Hg + 1);
               this.o(var1, com.larpclient.y.q.W, OrderDropperModule.o.o);
               this.Hg++;
               this.H8 = (boolean)1;
               _q.c("OrderDropper", "Dropped page {} ({} stacks)", this.Hg, var2);
            }
         } else if (this.j() != 0) {
            this.H5 = "Taking items";
            this.D(var1, var3, ContainerInput.PICKUP, OrderDropperModule.o.o);
         } else if (this.T() && this.X()) {
            this.R();
         } else {
            this.f("This order has no Drop button and your inventory is full. Make room and enable again.");
         }
      } else if (var5 >= 0 && ++this.Hi <= 3) {
         this.H5 = "Next page";
         this.o(var1, com.larpclient.y.q.K, OrderDropperModule.o.o);
      } else {
         this.Hd.add(this.HY);
         this.E("Order emptied.");
         _q.z("OrderDropper", "Order emptied after {} pages: {}", this.Hg, this.HY);
         this.x();
      }
   }

   private void k(ChestMenu var1) {
      for (int var2 = var1.getRowCount() * 9; var2 < var1.slots.size(); var2++) {
         Slot var3 = var1.getSlot(var2);
         if (var3.container == mc.player.getInventory() && var3.getContainerSlot() < 36 && var3.getItem().isEmpty()) {
            this.D(var1, var2, ContainerInput.PICKUP, OrderDropperModule.o.o);
            this.Hl++;
            return;
         }
      }

      this.f("No empty inventory slot for the item on the cursor.");
   }

   private int v(ChestMenu var1, com.larpclient.y.q var2) {
      int var3 = -1;

      for (int var4 = var1.getRowCount() == 6 ? 45 : 0; var4 < var1.getRowCount() * 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         if (!var5.isEmpty() && com.larpclient.y.K(n(var5), var5.getHoverName().getString()) == var2) {
            if (var3 >= 0) {
               return -2;
            }

            var3 = var4;
         }
      }

      return var3;
   }

   private void o(ChestMenu var1, com.larpclient.y.q var2, OrderDropperModule.o var3) {
      int var4 = this.v(var1, var2);
      if (var4 == -2) {
         this.f("Two " + var2 + " buttons found; this menu layout needs review.");
      } else if (var4 < 0) {
         this.f("No " + var2 + " button in this menu.");
      } else {
         this.D(var1, var4, ContainerInput.PICKUP, var3);
      }
   }

   private void D(AbstractContainerMenu var1, int var2, ContainerInput var3, OrderDropperModule.o var4) {
      ItemStack var5 = var1.getSlot(var2).getItem();
      if (var2 < 54 && var1 instanceof ChestMenu && com.larpclient.y.K(n(var5), var5.getHoverName().getString()) == com.larpclient.y.q.n) {
         this.f("Refused to click a Sell button.");
      } else {
         this.eb = var4;
         this.Hb = var1.containerId;
         this.U = 0;
         this.Hn = this.M = 0;
         mc.gameMode.handleContainerInput(var1.containerId, var2, 0, var3, mc.player);
         this.S = O(var1);
      }
   }

   private void x() {
      this.HY = "";
      this.Hi = 0;
      this.R();
   }

   private void R() {
      this.HZ = (boolean)1;
      this.b = 0;
      this.eb = null;
      this.Hs = 0;
      this.er = 0;
      this.Hp = 0;
      this.L();
   }

   private void L() {
      if (mc.player.containerMenu != mc.player.inventoryMenu && mc.player.containerMenu.getCarried().isEmpty()) {
         mc.player.closeContainer();
      }
   }

   private ChestMenu l() {
      if (mc.gui.screen() instanceof ContainerScreen var1
         && mc.player.containerMenu instanceof ChestMenu var2
         && var1.getMenu() == var2
         && (var2.getRowCount() == 6 || var2.getRowCount() == 3)) {
         com.larpclient.y.z var5 = com.larpclient.y.U(var1.getTitle().getString());
         return var5 == com.larpclient.y.z.G && !com.larpclient.y.R(var1.getTitle().getString()).startsWith("orders") ? null : var2;
      } else {
         return null;
      }
   }

   private boolean v() {
      return mc.gui.screen() instanceof ShulkerBoxScreen
         ? this.Ho != null
         : mc.gui.screen() != null && com.larpclient.y.U(mc.gui.screen().getTitle().getString()) != com.larpclient.y.z.G;
   }

   private void z() {
      this.H5 = "Picking up items";
      int var1 = this.K();
      if (this.j() <= 0 || this.H9 >= 120 || var1 <= 0 && this.H9 >= 10) {
         if (var1 > 0 && this.j() > 0) {
            _q.R("OrderDropper", "{} item stacks stayed out of pickup reach", var1);
         }

         if (this.j() != 0 && (!this.d || !this.X())) {
            if (var1 > 0 && !this.X()) {
               this.f("The dropped items landed out of pickup reach.");
            } else if (this.d) {
               this.g();
            } else {
               this.K();
            }
         } else {
            this.w();
         }
      }
   }

   private void K() {
      this.H8 = (boolean)0;
      this.I(OrderDropperModule.r.z);
   }

   private void g() {
      if (this.X()) {
         this.w();
      } else {
         this.Z();
      }
   }

   private void w() {
      this.Ho = null;
      this.q();
      Vec3 var1 = mc.player.position();
      double var2 = Double.MAX_VALUE;

      for (BlockPos var5 : this.u()) {
         if (!this.HG.contains(var5) && (this.H2 ? !this.HK.containsKey(var5) : !this.H0.contains(var5))) {
            double var6 = Vec3.atCenterOf(var5).distanceToSqr(var1);
            if (var6 < var2) {
               var2 = var6;
               this.Ho = var5;
            }
         }
      }

      if (this.Ho == null) {
         if (this.H2) {
            this.H2 = (boolean)0;
            this.I(OrderDropperModule.r.g);
         } else {
            this.Hm = (boolean)1;
            if (this.J(this.W) > 0.4) {
               this.I(OrderDropperModule.r.g);
               this.E("No usable shulkers remain; walking back.");
               this.d = (boolean)1;
            } else {
               this.f("No usable shulkers remain. Select more or rescan, then enable again.");
            }
         }
      } else {
         if (!this.H2) {
            this.HF++;
         }

         this.HU = this.ef = (boolean)0;
         this.HO = this.h = this.Hf = this.e_ = 0;
         this.HR = null;
         this.HB.clear();
         this.HH = Double.MAX_VALUE;
         this.Q = 0;
         this.I(OrderDropperModule.r.J);
         _q.c("OrderDropper", "Trip {} to shulker {} carrying {} stacks", this.HF, this.Ho.toShortString(), this.Z());
      }
   }

   private void G() {
      if (!this.t(this.Ho)) {
         this.z("is missing or unloaded");
      } else if (mc.gui.screen() != null) {
         this.H5 = "Close the screen to continue";
      } else if (!this.g()) {
         this.H5 = this.H2 ? "Walking to scan shulker" : "Walking to shulker";
         if (this.R() && this.X(mc.player.getEyePosition()) != null) {
            this.v = this.HP = 0;
            this.HI = (boolean)0;
            this.I(OrderDropperModule.r.t);
         } else {
            if (this.HR != null && this.J(this.HR) < 0.15 && Math.abs(mc.player.getY() - this.HR.y) < 0.35) {
               this.HB.add(this.HR);
               _q.c("OrderDropper", "Replanning shulker {} from eye={} after arriving at {}", this.Ho.toShortString(), mc.player.getEyePosition(), this.HR);
               this.HR = null;
            }

            if (this.HR == null) {
               this.HR = this.I();
               this.HH = Double.MAX_VALUE;
               this.Q = 0;
            }

            if (this.HR == null) {
               _q.R(
                  "OrderDropper",
                  "No visible standing spot for {}: eye={} reach={} tried={}",
                  this.Ho.toShortString(),
                  mc.player.getEyePosition(),
                  this.p(),
                  this.HB.size()
               );
               this.z("has no clear reachable standing spot");
            } else {
               this.N(this.HR, 0.1);
            }
         }
      }
   }

   private void f() {
      if (!this.t(this.Ho)) {
         this.z("is missing or unloaded");
      } else if (mc.gui.screen() instanceof ShulkerBoxScreen var1 && mc.player.containerMenu instanceof ShulkerBoxMenu var2 && var1.getMenu() == var2) {
         this.HI = (boolean)0;
         this.Hz = this.B();
         this.I(OrderDropperModule.r.x);
      } else if (!this.R() && !this.HI) {
         this.HR = null;
         this.HH = Double.MAX_VALUE;
         this.Q = 0;
         this.I(OrderDropperModule.r.J);
      } else {
         this.H5 = "Opening shulker";
         if (this.HI) {
            if (++this.y <= 60) {
               return;
            }

            this.HI = (boolean)0;
            if (++this.HP >= 3) {
               this.z("did not open");
               return;
            }
         }

         if (mc.gui.screen() == null) {
            if (!this.g()) {
               BlockHitResult var6 = this.X(mc.player.getEyePosition());
               if (var6 == null) {
                  this.HR = null;
                  this.I(OrderDropperModule.r.J);
               } else {
                  Vec3 var7 = k(this.Ho, var6);
                  float[] var9 = b6.R(mc.player.getEyePosition(), var7);
                  b6.P(this, b6.w.d, var9[0], var9[1], 0.5);
                  BlockHitResult var4 = _p.A(this.Ho, this.p());
                  if (var4 == null) {
                     this.v = 0;
                     if (this.H9 > 60) {
                        this.z("could not be aimed at");
                     }
                  } else if (++this.v >= 2 && !mc.player.isShiftKeyDown()) {
                     boolean var5 = _p.T(this, () -> {
                        if (mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, var4).consumesAction()) {
                           h8.i(mc.player, InteractionHand.MAIN_HAND);
                        }
                     }, () -> mc.gui.screen() == null);
                     if (var5) {
                        this.HI = (boolean)1;
                        this.y = 0;
                     }
                  }
               }
            }
         }
      }
   }

   private void X() {
      if (mc.gui.screen() instanceof ShulkerBoxScreen var1 && mc.player.containerMenu instanceof ShulkerBoxMenu var2 && var1.getMenu() == var2) {
         this.H5 = this.H2 ? "Scanning shulker" : "Filling shulker";
         if (!var2.getCarried().isEmpty()) {
            this.f("The cursor holds an item inside the shulker.");
         } else if (this.Hz <= 0 || --this.Hz <= 0) {
            if (this.H2) {
               this.h(var2);
               mc.player.closeContainer();
               this.w();
            } else {
               if (this.HU) {
                  int var7 = this.k(var2);
                  int var4 = this.f(var2);
                  int var5 = Math.max(0, var7 - this.Hy);
                  this.HM = this.HM + Math.max(0, var4 - this.w);
                  _q.z(
                     "OrderDropper",
                     "Verified shulker {}: items added={} ({} -> {}), carrying={} stacks",
                     this.Ho.toShortString(),
                     var5,
                     this.Hy,
                     var7,
                     this.Z()
                  );
                  this.Hy = var7;
                  this.w = var4;
                  this.h(var2);
                  this.HU = (boolean)0;
                  this.h = 0;
                  if (var5 > 0) {
                     this.Hf = 0;
                  } else if (++this.Hf > 3 && this.X()) {
                     this.f("Shulker transfers kept being rejected. Progress retained; check the server and enable again.");
                     return;
                  }
               }

               if (!this.ef) {
                  this.Hy = this.k(var2);
                  this.w = this.f(var2);
                  this.ef = (boolean)1;
               }

               if (this.h >= 36) {
                  this.Y();
               } else {
                  for (int var8 = 27; var8 < var2.slots.size(); var8++) {
                     Slot var10 = var2.getSlot(var8);
                     ItemStack var11 = var10.getItem();
                     if (this.y(var11) && (var10.container != mc.player.getInventory() || !this.G.contains(var10.getContainerSlot())) && this.w(var2, var11)) {
                        mc.gameMode.handleContainerInput(var2.containerId, var8, 0, ContainerInput.QUICK_MOVE, mc.player);
                        this.h++;
                        this.HO = 0;
                        this.Hz = this.j.getValue().intValue();
                        return;
                     }
                  }

                  if (!this.X() && ++this.HO < Math.max(10, this.j.getValue().intValue())) {
                     this.H5 = "Waiting for remaining items";
                  } else if (this.h > 0) {
                     this.Y();
                  } else {
                     this.h(var2);
                     boolean var9 = this.X();
                     if (var9) {
                        this.H0.add(this.Ho);
                        this.E("Shulker full (" + this.H0.size() + "/" + this.u().size() + ").");
                     }

                     mc.player.closeContainer();
                     _q.c("OrderDropper", "Closed shulker {}; full={} carrying={}", this.Ho.toShortString(), var9, this.Z());
                     if (var9) {
                        this.w();
                     } else {
                        this.I(OrderDropperModule.r.g);
                     }
                  }
               }
            }
         }
      } else {
         this.HK.remove(this.Ho);
         if (++this.e_ > 3) {
            this.z("kept closing before filling finished");
         } else {
            this.HR = null;
            this.HH = Double.MAX_VALUE;
            this.Q = 0;
            this.I(OrderDropperModule.r.J);
         }
      }
   }

   private void Y() {
      this.HU = (boolean)1;
      this.HI = (boolean)0;
      this.v = this.HP = 0;
      mc.player.closeContainer();
      this.I(OrderDropperModule.r.t);
      _q.c("OrderDropper", "Reopening {} to verify transfers", this.Ho.toShortString());
   }

   private int k(ShulkerBoxMenu var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (this.y(var1.getSlot(var3).getItem())) {
            var2 += var1.getSlot(var3).getItem().getCount();
         }
      }

      return var2;
   }

   private int f(ShulkerBoxMenu var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (this.y(var1.getSlot(var3).getItem())) {
            var2++;
         }
      }

      return var2;
   }

   private boolean w(ShulkerBoxMenu var1, ItemStack var2) {
      for (int var3 = 0; var3 < 27; var3++) {
         Slot var4 = var1.getSlot(var3);
         ItemStack var5 = var4.getItem();
         if (var5.isEmpty() ? var4.mayPlace(var2) : ItemStack.isSameItemSameComponents(var5, var2) && var5.getCount() < var4.getMaxStackSize(var5)) {
            return true;
         }
      }

      return false;
   }

   private void z(String var1) {
      this.HG.add(this.Ho);
      this.HK.remove(this.Ho);
      this.H0.remove(this.Ho);
      this.E("Skipped a shulker that " + var1 + ".");
      _q.R("OrderDropper", "Shulker {} {}; skipped", this.Ho.toShortString(), var1);
      this.w();
   }

   private void S() {
      this.H5 = "Walking back";
      if (this.J(this.W) > 0.3) {
         this.N(this.W, 0.3);
      } else if (this.T() && this.K() > 0 && this.j() > 0) {
         this.I(OrderDropperModule.r.U);
      } else if (this.X() && this.u().stream().anyMatch(var1 -> !this.H0.contains(var1) && !this.HG.contains(var1))) {
         this.w();
      } else if (!this.d) {
         this.K();
      } else {
         this.Z();
      }
   }

   private void Z() {
      if (this.T() && this.J(this.W) > 0.3) {
         this.I(OrderDropperModule.r.g);
      } else if (this.T() && (this.X() || this.K() > 0)) {
         this.f("Storage unavailable with items still to store. Select more shulkers or rescan, then enable again.");
      } else {
         this.I(OrderDropperModule.r.v);
      }
   }

   private void F() {
      this.H5 = "Finishing";
      if (!this.T() || this.H9 >= 20 || !(Math.abs(Mth.wrapDegrees(mc.player.getYRot() - this.Hv)) > 1.0F) && !(Math.abs(mc.player.getXRot() - this.Y) > 1.0F)) {
         this.M(
            "Done: "
               + this.Hg
               + " pages dropped, "
               + this.Hl
               + " stacks taken"
               + (this.T() ? ", " + this.HM + " stacks stored in " + this.HF + " trips." : ".")
         );
         _q.z("OrderDropper", "Finished pages={} taken={} stored={} trips={}", this.Hg, this.Hl, this.HM, this.HF);
         this.H = (boolean)0;
         this.disable();
      } else {
         b6.P(this, b6.w.d, this.Hv, this.Y, 0.5);
      }
   }

   private void N(Vec3 var1, double var2) {
      if (!this.g()) {
         double var4 = this.J(var1);
         if (var4 < this.HH - 0.05) {
            this.HH = var4;
            this.Q = 0;
         } else if (++this.Q > 50) {
            this.f("The way is blocked; stopped where it was.");
            return;
         }

         Vec3 var6 = var1.subtract(mc.player.position());
         float var7 = (float)Math.toDegrees(Math.atan2(var6.z, var6.x)) - 90.0F;
         b6.P(this, b6.w.d, var7, 25.0F, 0.35);
         this.T = (boolean)(Math.abs(Mth.wrapDegrees(mc.player.getYRot() - var7)) < 35.0F ? 1 : 0);
         this.s = (boolean)(this.T && mc.player.horizontalCollision && mc.player.onGround() ? 1 : 0);
         this.HN = (boolean)(var2 > 0.0 && var4 < 1.0 ? 1 : 0);
      }
   }

   public static Input e() {
      if (X != null && X.isEnabled()) {
         OrderDropperModule var0 = X;
         if (var0 == null || !var0.isRuntimeActive() || getMc().gui.screen() != null || ij.y() || b6.x() && !b6.c(b6.w.d)) {
            return null;
         } else if (!var0.T) {
            return l() ? new Input(false, false, false, false, false, false, false) : null;
         } else {
            return new Input(true, false, false, false, var0.s, var0.HN, false);
         }
      } else {
         return null;
      }
   }

   private boolean s() {
      if (mc.gui.screen() == null && mc.isWindowActive()) {
         Options var1 = mc.options;
         return nz.K(var1.keyUp) || nz.K(var1.keyDown) || nz.K(var1.keyLeft) || nz.K(var1.keyRight) || nz.K(var1.keyJump);
      } else {
         return false;
      }
   }

   private void J() {
      Minecraft var1 = getMc();
      if (var1 != null && var1.getWindow() != null) {
         if (this.k != null && var1.level != this.k && !this.isRuntimeActive()) {
            this.HC = (boolean)0;
            this.eh = null;
            this.A();
            this.i.clear();
            this.e9.clear();
            this.eA = null;
            this.k = null;
         }

         if (this.HC) {
            if (!this.isInGame() || var1.level != this.k) {
               this.HC = (boolean)0;
               this.eh = null;
            } else if (!this.isRuntimeActive() && var1.gui.screen() == null) {
               if (System.nanoTime() - this.HV > 2000000000L) {
                  this.W();
               }
            } else {
               this.H();
            }
         }
      }
   }

   private void m() {
      if (this.k != mc.level) {
         this.A();
         this.i.clear();
         this.e9.clear();
         this.eA = null;
         this.k = mc.level;
      }
   }

   private void P() {
      this.m();
      this.HC = (boolean)1;
      this.eA = this.HJ = this.eh = null;
      this.Ha = this.J = this.Hq = this.Hw = this.p = (boolean)0;
      if (mc.gameMode != null) {
         mc.gameMode.stopDestroyBlock();
      }

      this.W();
      _q.c("OrderDropper", "Selection started with {} shulkers", this.i.size());
   }

   private void H() {
      this.HC = (boolean)0;
      this.eh = this.HJ = null;
      this.Hq = this.Hw = this.p = (boolean)0;
      this.J("Shulker selection saved. " + this.i.size() + " selected.");
      _q.z("OrderDropper", "Selection finished with {} shulkers", this.i.size());
   }

   private void W() {
      int var1 = this.H7.getValue();
      this.J(
         this.i.size()
            + " selected. Right-click adds, left-click removes, hold to sweep, sneak-click for an area. "
            + (p4.g(var1) ? p4.n(var1) + " or Esc" : "Esc")
            + " to finish."
      );
   }

   public static boolean z() {
      OrderDropperModule var0 = X;
      return var0 != null && var0.HC;
   }

   private void I() {
      if (!this.isInGame()) {
         this.eh = null;
         this.g = (boolean)0;
      } else {
         this.n();
         int var1 = this.g && !this.Hh ? 1 : 0;
         if ((this.HC || var1 != 0) && mc.gui.screen() == null) {
            HitResult var2 = mc.player.pick(24.0, mc.getDeltaTracker().getGameTimeDeltaPartialTick(false), false);
            this.eh = var2 instanceof BlockHitResult var3 && var2.getType() == Type.BLOCK && this.t(var3.getBlockPos()) ? var3.getBlockPos().immutable() : null;
            if (var1 != 0) {
               this.p(this.eh);
            } else {
               boolean var7 = nz.K(mc.options.keyUse);
               boolean var4 = nz.K(mc.options.keyAttack);
               if (!var7) {
                  this.Ha = (boolean)1;
               }

               if (!var4) {
                  this.J = (boolean)1;
               }

               int var5 = var7 && this.Ha ? 1 : 0;
               int var6 = var4 && this.J && var5 == 0 ? 1 : 0;
               if ((var5 == 0 || this.Hq) && (var6 == 0 || this.Hw)) {
                  if ((var5 != 0 || var6 != 0) && !this.p && this.eh != null && !this.eh.equals(this.HJ)) {
                     if (this.S(this.eh, (boolean)var5)) {
                        this.x((boolean)var5, 1);
                     }

                     this.HJ = this.eA = this.eh;
                  }
               } else {
                  this.v((boolean)var5);
               }

               this.Hq = (boolean)var5;
               this.Hw = (boolean)var6;
            }
         } else {
            this.eh = null;
         }
      }
   }

   private void n() {
      int var1 = this.H7.getValue();
      int var2 = p4.g(var1) && mc.gui.screen() == null && mc.isWindowActive() && p4.Y(mc.getWindow().handle(), var1) ? 1 : 0;
      if (var2 != 0 && !this.g) {
         this.Hh = this.isRuntimeActive();
         if (this.Hh) {
            this.J("Turn Order Dropper off before changing shulkers.");
         } else {
            this.m();
            this.HT = null;
            this.N = null;
            this.HD = 0;
            this.H1 = System.nanoTime();
         }
      } else if (var2 == 0 && this.g && !this.Hh) {
         if (this.HT == null && System.nanoTime() - this.H1 < 400000000L) {
            if (this.HC) {
               this.H();
            } else {
               this.P();
            }
         } else if (this.HT != null) {
            _q.z("OrderDropper", "Key sweep {} {} shulkers; total {}", this.HT ? "added" : "removed", this.HD, this.i.size());
         }
      }

      this.g = (boolean)var2;
   }

   private void p(BlockPos var1) {
      if (var1 != null && !var1.equals(this.N)) {
         this.N = var1;
         if (this.HT == null) {
            this.HT = !this.i.contains(var1);
         }

         if (this.S(var1, this.HT)) {
            this.eA = var1;
            this.x(this.HT, ++this.HD);
         }
      }
   }

   private void v(boolean var1) {
      this.HJ = null;
      this.p = (boolean)0;
      if (this.eh == null) {
         this.J("Look at a placed shulker box.");
      } else {
         if (nz.K(mc.options.keyShift) && this.eA != null && !this.eA.equals(this.eh)) {
            this.p = (boolean)1;
            List var2 = com.larpclient.y.c(B(this.eA), B(this.eh));
            if (var2.isEmpty()) {
               this.J("That area is too large. Keep each side within 32 blocks.");
               return;
            }

            int var3 = 0;

            for (com.larpclient.y.d var5 : var2) {
               if (this.S(new BlockPos(var5.x(), var5.y(), var5.z()), var1)) {
                  var3++;
               }
            }

            this.x(var1, var3);
         } else {
            if (this.S(this.eh, var1)) {
               this.x(var1, 1);
            } else {
               this.J((var1 ? "Already selected; left-click removes it. " : "Not selected. ") + this.i.size() + " selected.");
            }

            this.HJ = this.eh;
         }

         this.eA = this.eh;
      }
   }

   private boolean S(BlockPos var1, boolean var2) {
      if (var2 ? !this.i.contains(var1) && this.t(var1) : this.i.remove(var1)) {
         if (var2) {
            this.i.add(var1.immutable());
         } else {
            this.H0.remove(var1);
            this.HK.remove(var1);
            this.HG.remove(var1);
            this.o.remove(var1);
         }

         this.e9.put(var1.immutable(), new OrderDropperModule.i(System.nanoTime(), var2));
         return true;
      } else {
         return false;
      }
   }

   private void x(boolean var1, int var2) {
      this.J((var1 ? "Added " : "Removed ") + var2 + (var2 == 1 ? " shulker. " : " shulkers. ") + this.i.size() + " selected.");
      _q.c("OrderDropper", "{} {} shulkers; total {}", var1 ? "Added" : "Removed", var2, this.i.size());
   }

   private static com.larpclient.y.d B(BlockPos var0) {
      return new com.larpclient.y.d(var0.getX(), var0.getY(), var0.getZ());
   }

   private int k(BlockPos var1, boolean var2) {
      return this.HG.contains(var1) ? -1876386 : (this.H0.contains(var1) ? -2061766 : (this.y(var1, var2) ? -11702 : -13180806));
   }

   private static int q(int var0) {
      return var0 == -11702 ? -10733056 : (var0 == -2061766 ? -11918843 : (var0 == -1876386 ? -11923692 : -16040668));
   }

   private boolean y(BlockPos var1, boolean var2) {
      return var2 && var1.equals(this.Ho) && (this.e7 == OrderDropperModule.r.J || this.e7 == OrderDropperModule.r.t || this.e7 == OrderDropperModule.r.x);
   }

   private String Q(BlockPos var1, boolean var2) {
      if (this.HG.contains(var1)) {
         return "Skipped";
      }

      if (this.y(var1, var2)) {
         return this.H2 ? "Scanning" : (this.e7 == OrderDropperModule.r.x ? "Filling" : (this.e7 == OrderDropperModule.r.J ? "Next" : "Opening"));
      }

      if (this.H0.contains(var1)) {
         return "Full";
      }

      Integer var3 = this.HK.get(var1);
      return var3 == null ? null : var3 + " free";
   }

   private void v() {
      Minecraft var1 = getMc();
      if (var1 != null && var1.level != null) {
         try {
            this.I();
            if (var1.level != this.k) {
               return;
            }

            long var2 = System.nanoTime();
            if (!this.i.isEmpty()) {
               this.w(var1.gameRenderer.mainCamera().position());
            }

            this.o(var2);
            if (this.eh != null) {
               this.a();
            }
         } catch (RuntimeException var4) {
            _q.g("OrderDropper", "Shulker highlight failed", var4);
         }
      }
   }

   private void w(Vec3 var1) {
      boolean var2 = this.isRuntimeActive();

      for (BlockPos var4 : this.i) {
         String var5 = this.Q(var4, var2);
         Vec3 var6 = Vec3.atCenterOf(var4);
         if (var5 != null && var1.distanceToSqr(var6) < 100.0) {
            Gizmos.billboardText(var5, var6, Style.forColorAndCentered(-854792).withScale(0.4F)).setAlwaysOnTop();
         }
      }
   }

   private void o(long var1) {
      Iterator var3 = this.e9.entrySet().iterator();

      while (var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         double var5 = 1.0 - (var1 - ((OrderDropperModule.i)var4.getValue()).at()) / 4.5E8;
         if (var5 <= 0.0) {
            var3.remove();
         } else {
            int var7 = ((OrderDropperModule.i)var4.getValue()).added() ? -13180806 : -1017711;
            Gizmos.cuboid(new AABB((BlockPos)var4.getKey()).inflate(0.02 + 0.06 * (1.0 - var5)), GizmoStyle.fill(vv.N(var7, 0.4 * var5))).setAlwaysOnTop();
         }
      }
   }

   private void a() {
      boolean var1 = this.i.contains(this.eh);
      Gizmos.cuboid(new AABB(this.eh).inflate(0.012), GizmoStyle.strokeAndFill(-854792, 2.5F, vv.N(var1 ? -1017711 : -13180806, 0.2))).setAlwaysOnTop();
      if (this.eA != null && !this.eA.equals(this.eh) && !this.g && nz.K(mc.options.keyShift)) {
         int var2 = !com.larpclient.y.c(B(this.eA), B(this.eh)).isEmpty() ? 1 : 0;
         AABB var3 = new AABB(this.eA).minmax(new AABB(this.eh)).inflate(0.03);
         Gizmos.cuboid(var3, GizmoStyle.strokeAndFill(var2 != 0 ? -854792 : -1017711, 1.5F, vv.N(var2 != 0 ? -854792 : -1017711, 0.06))).setAlwaysOnTop();
      }
   }

   public static OrderDropperModule U() {
      return X;
   }

   public void X(PoseStack var1, wh var2, Vec3 var3) {
      if (this.isEnabled()) {
         Minecraft var4 = getMc();
         if (var4 != null && var4.level != null && var4.level == this.k && !this.i.isEmpty()) {
            long var5 = he.s();

            try {
               this.E(this.isRuntimeActive());
               VertexConsumer var7 = w7.Q(var2, this.k());
               Pose var8 = var1.last();
               double var9 = hz.Q();
               double var11 = var9 * 0.08 % 1.0;
               double var13 = 0.5 + 0.5 * Math.sin(var9 * 6.0);
               double var15 = this.l.getValue();

               for (int var17 = 0; var17 < this.eu.size(); var17++) {
                  double[] var18 = this.eu.get(var17);
                  int var19 = this.Hx.get(var17);
                  double var20 = var19 == -11702 ? var15 * (0.7 + 0.3 * var13) : var15;

                  for (int var22 = 0; var22 < 4; var22++) {
                     int var23 = var22 * 5;
                     w7.S(
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
                        q(var19),
                        var20,
                        0.6,
                        1.0,
                        var11
                     );
                  }
               }
            } finally {
               he.T("Order Dropper selection", var5);
            }
         }
      }
   }

   private w7.b k() {
      return switch ((String)this.K.getValue()) {
         case "Glow" -> w7.b.a;
         case "Metallic" -> w7.b.s;
         case "Marble" -> w7.b.w;
         case "Glitter" -> w7.b.R;
         case "Solid" -> w7.b.h;
         case "Ghost" -> w7.b.V;
         default -> w7.b.U;
      };
   }

   private void E(boolean var1) {
      long var2 = this.i.size();

      for (BlockPos var5 : this.i) {
         var2 = var2 * 31L + var5.asLong() * 17L + this.k(var5, var1);
      }

      if (var2 != this.HE) {
         this.HE = var2;
         this.eu.clear();
         this.Hx.clear();
         HashSet var36 = new HashSet<>(this.i);

         for (BlockPos var6 : this.i) {
            int var7 = this.k(var6, var1);

            for (Direction var11 : Direction.values()) {
               if (!var36.contains(var6.relative(var11))) {
                  Direction[] var12 = R[var11.ordinal()];
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
                     double var30 = X(var6.getX(), var11.getStepX(), var13.getStepX(), var14.getStepX(), var26, var28);
                     double var32 = X(var6.getY(), var11.getStepY(), var13.getStepY(), var14.getStepY(), var26, var28);
                     double var34 = X(var6.getZ(), var11.getStepZ(), var13.getStepZ(), var14.getStepZ(), var26, var28);
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
                  this.eu.add(var23);
                  this.Hx.add(var7);
               }
            }
         }
      }
   }

   private static double X(int var0, int var1, int var2, int var3, double var4, double var6) {
      double var8 = var1 > 0 ? var0 + 1 + 0.012 : (var1 < 0 ? var0 - 0.012 : var0);
      return var8 + (var2 != 0 ? var4 : 0.0) + (var3 != 0 ? var6 : 0.0);
   }

   private void I(OrderDropperModule.r var1) {
      if (this.e7 != var1) {
         _q.c("OrderDropper", "Phase {} -> {}", this.e7, var1);
      }

      this.e7 = var1;
      this.H9 = 0;
      if (var1 == OrderDropperModule.r.J || var1 == OrderDropperModule.r.g) {
         this.HH = Double.MAX_VALUE;
         this.Q = 0;
      }
   }

   private int B() {
      return this.z.getValue().intValue();
   }

   private float e() {
      return mc.player.getHealth() + mc.player.getAbsorptionAmount();
   }

   private double p() {
      return Math.min(mc.player.blockInteractionRange(), 4.5);
   }

   private double J(Vec3 var1) {
      Vec3 var2 = mc.player.position();
      return Math.hypot(var1.x - var2.x, var1.z - var2.z);
   }

   private boolean t(BlockPos var1) {
      return var1 != null && mc.level.hasChunkAt(var1) && mc.level.getBlockState(var1).getBlock() instanceof ShulkerBoxBlock;
   }

   private boolean y(ItemStack var1) {
      if (!var1.isEmpty() && !this.O.isEmpty()) {
         if (!this.O.equals(this.P)) {
            Identifier var2 = Identifier.tryParse(this.O);
            this.m = var2 == null ? null : (Item)BuiltInRegistries.ITEM.getOptional(var2).orElse(null);
            this.P = this.O;
         }

         return var1.getItem() == this.m;
      } else {
         return false;
      }
   }

   private int j() {
      int var1 = 0;

      for (int var2 = 0; var2 < 36; var2++) {
         if (mc.player.getInventory().getItem(var2).isEmpty()) {
            var1++;
         }
      }

      return var1;
   }

   private int Z() {
      int var1 = 0;

      for (int var2 = 0; var2 < 36; var2++) {
         if (!this.G.contains(var2) && this.y(mc.player.getInventory().getItem(var2))) {
            var1++;
         }
      }

      return var1;
   }

   private boolean X() {
      return this.Z() > 0;
   }

   private int J() {
      this.q();
      int var1 = 0;

      for (BlockPos var3 : this.u()) {
         if (!this.H0.contains(var3) && !this.HG.contains(var3)) {
            var1 += this.HK.getOrDefault(var3, 0);
         }
      }

      return var1;
   }

   private void h(ShulkerBoxMenu var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (var1.getSlot(var3).getItem().isEmpty()) {
            var2++;
         }
      }

      this.HK.put(this.Ho, var2);
      this.o.put(this.Ho, mc.level.getBlockEntity(this.Ho));
      this.HG.remove(this.Ho);
      if (var2 == 0) {
         this.H0.add(this.Ho);
      } else {
         this.H0.remove(this.Ho);
      }

      _q.z("OrderDropper", "Scanned shulker {}: free slots={}", this.Ho.toShortString(), var2);
   }

   private void q() {
      for (BlockPos var2 : this.u()) {
         if (!this.t(var2)) {
            if (this.HG.add(var2)) {
               _q.R("OrderDropper", "Selected shulker {} is missing or unloaded; capacity removed", var2.toShortString());
            }

            this.HK.remove(var2);
            this.H0.remove(var2);
            this.o.remove(var2);
         } else if (this.o.containsKey(var2) && this.o.get(var2) != mc.level.getBlockEntity(var2)) {
            this.HK.remove(var2);
            this.H0.remove(var2);
            this.o.remove(var2);
         }
      }
   }

   private static AABB X(BlockPos var0, Direction var1) {
      return new AABB(var0).expandTowards(var1.getStepX() * 0.5, var1.getStepY() * 0.5, var1.getStepZ() * 0.5).inflate(0.1);
   }

   private AABB H() {
      return X(this.Ho, (Direction)mc.level.getBlockState(this.Ho).getValue(ShulkerBoxBlock.FACING));
   }

   private boolean R() {
      return !this.H().intersects(mc.player.getBoundingBox());
   }

   private Vec3 I() {
      return C(
         this.Ho,
         mc.player.position(),
         mc.player.getBoundingBox(),
         this.H(),
         var0 -> mc.level.noCollision(mc.player, var0),
         var1 -> !this.HB.contains(var1) && this.X(var1.add(0.0, mc.player.getEyeHeight(), 0.0)) != null
      );
   }

   private BlockHitResult X(Vec3 var1) {
      double var2 = this.p();
      if (D(this.Ho, var1) > var2 * var2) {
         return null;
      }

      List var4 = mc.level
         .getEntities(mc.player, new AABB(this.Ho).minmax(new AABB(var1, var1)).inflate(1.0), var0 -> var0.isPickable() && !var0.isSpectator());
      return H(this.Ho, var1, var2, (var1x, var2x) -> {
         BlockHitResult var3 = mc.level.clip(new ClipContext(var1x, var2x, Block.OUTLINE, Fluid.NONE, mc.player));
         if (var3.getType() != Type.BLOCK) {
            return null;
         }

         for (Entity var5 : var4) {
            if (jz.P(var1x, var3.getLocation(), var5.getBoundingBox())) {
               return null;
            }
         }

         return var3;
      });
   }

   private static double D(BlockPos var0, Vec3 var1) {
      double var2 = Math.clamp(var1.x, var0.getX(), var0.getX() + 1.0);
      double var4 = Math.clamp(var1.y, var0.getY(), var0.getY() + 1.0);
      double var6 = Math.clamp(var1.z, var0.getZ(), var0.getZ() + 1.0);
      return var1.distanceToSqr(var2, var4, var6);
   }

   private static BlockHitResult H(BlockPos var0, Vec3 var1, double var2, BiFunction<Vec3, Vec3, BlockHitResult> var4) {
      if (Double.isFinite(var2) && !(var2 <= 0.0) && !(D(var0, var1) > var2 * var2 + 1.0E-7)) {
         ArrayList var5 = new ArrayList(28);
         var5.add(Vec3.atCenterOf(var0));
         Vec3 var6 = Vec3.atCenterOf(var0);

         for (Direction var10 : Direction.values()) {
            double var11 = (var1.x - var6.x) * var10.getStepX() + (var1.y - var6.y) * var10.getStepY() + (var1.z - var6.z) * var10.getStepZ();
            if (!(var11 <= 0.5)) {
               for (Vec3 var14 : vi.O(var0, var10)) {
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
                  float[] var19 = b6.R(var1, k(var0, var18));
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

   private static Vec3 C(BlockPos var0, Vec3 var1, AABB var2, AABB var3, Predicate<AABB> var4, Predicate<Vec3> var5) {
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

   private boolean g() {
      if (!_p.g() || b6.x() && !b6.c(b6.w.d)) {
         this.H5 = "Waiting for camera or rotation control";
         this.H9 = Math.max(0, this.H9 - 1);
         return true;
      } else {
         return false;
      }
   }

   public static boolean l() {
      if (X != null && X.isEnabled()) {
         OrderDropperModule var0 = X;
         return var0 != null && var0.isRuntimeActive() && var0.e7 != null && var0.HX.getValue();
      } else {
         return false;
      }
   }

   private void U() {
      if (!this.HX.getValue()) {
         this.O();
      } else {
         if (!this.He) {
            this.f = mc.options.pauseOnLostFocus;
            this.He = (boolean)1;
            _q.z("OrderDropper", "AFK mouse released; pause on focus loss temporarily disabled");
         }

         mc.options.pauseOnLostFocus = (boolean)0;
         if (mc.mouseHandler.isMouseGrabbed()) {
            mc.mouseHandler.releaseMouse();
         }
      }
   }

   private void O() {
      if (this.He) {
         mc.options.pauseOnLostFocus = this.f;
         this.He = (boolean)0;
         _q.z("OrderDropper", "AFK ended; pause on focus loss restored");
      }
   }

   private int K() {
      Vec3 var1 = this.W == null ? mc.player.position() : this.W;
      AABB var2 = new AABB(var1, var1).inflate(2.5, 1.5, 2.5);
      return mc.level.getEntitiesOfClass(ItemEntity.class, var2, var1x -> var1x.isAlive() && this.y(var1x.getItem())).size();
   }

   private static int O(AbstractContainerMenu var0) {
      int var1 = 31 * var0.containerId + var0.getStateId();

      for (Slot var3 : var0.slots) {
         var1 = 31 * var1 + ItemStack.hashItemAndComponents(var3.getItem()) * 7 + var3.getItem().getCount();
      }

      ItemStack var4 = var0.getCarried();
      return 31 * var1 + ItemStack.hashItemAndComponents(var4) + var4.getCount();
   }

   private static List<String> u(ItemStack var0) {
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

   private static String P(ItemStack var0) {
      StringBuilder var1 = new StringBuilder(n(var0));

      for (String var3 : u(var0)) {
         if (!com.larpclient.y.R(var3).contains("until")) {
            var1.append('|').append(var3);
         }
      }

      return var1.toString();
   }

   private static String n(ItemStack var0) {
      return BuiltInRegistries.ITEM.getKey(var0.getItem()).toString();
   }

   private void k(String var1) {
      this.M(var1);
      _q.z("OrderDropper", "Not started: {}", var1);
      this.disable();
   }

   private void f(String var1) {
      _q.R("OrderDropper", "Stopped in phase {}: {}", this.e7, var1);
      this.M("Stopped: " + var1);
      this.disable();
   }

   private void J(String var1) {
      this.HV = System.nanoTime();
      if (mc.player != null) {
         mc.player.sendOverlayMessage(Component.literal(var1));
      }
   }

   private void E(String var1) {
      if (this.Z.getValue()) {
         this.M(var1);
      }
   }

   private void M(String var1) {
      if (this.isInGame()) {
         mc.gui.chatListener().handleSystemMessage(Component.literal("[Order Dropper] " + var1), false);
      }
   }

   static {
      R[Direction.DOWN.ordinal()] = new Direction[]{Direction.EAST, Direction.SOUTH};
      R[Direction.UP.ordinal()] = new Direction[]{Direction.SOUTH, Direction.EAST};
      R[Direction.NORTH.ordinal()] = new Direction[]{Direction.UP, Direction.EAST};
      R[Direction.SOUTH.ordinal()] = new Direction[]{Direction.EAST, Direction.UP};
      R[Direction.WEST.ordinal()] = new Direction[]{Direction.SOUTH, Direction.UP};
      R[Direction.EAST.ordinal()] = new Direction[]{Direction.UP, Direction.SOUTH};
   }

   private record i(long at, boolean added) {
   }

   private enum o {
      x,
      o;
   }

   private enum r {
      z,
      U,
      J,
      t,
      x,
      g,
      v;
   }
}

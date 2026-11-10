package com.larpclient.module;

import com.larpclient.__;
import com.larpclient._q;
import com.larpclient.ae;
import com.larpclient.ba;
import com.larpclient.fn;
import com.larpclient.fz;
import com.larpclient.he;
import com.larpclient.hg;
import com.larpclient.hz;
import com.larpclient.n5;
import com.larpclient.nl;
import com.larpclient.sr;
import com.larpclient.v3;
import com.larpclient.v9;
import com.larpclient.vq;
import com.larpclient.w7;
import com.larpclient.wh;
import com.larpclient.wp;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class KillFXModule extends Module {
   public static final int Q = 4;
   public static final double Z = 0.45;
   private static KillFXModule c;
   private final com.larpclient.module.o r = this.addSetting(
      new com.larpclient.module.o(
         "Style",
         "Choose a finisher or vary each kill",
         "Random",
         "Random",
         "Execution",
         "Singularity",
         "Soul Pyre",
         "Star of David",
         "Tung Tung",
         "Tung Tung Floss",
         "Sakura",
         "Shatter",
         "Rug Pull"
      )
   );
   private final com.larpclient.module.o M = this.addSetting(
      new com.larpclient.module.o("Colors", "Authored palettes, your theme or custom colors", "Style", "Style", "Theme", "Custom")
   );
   private final h q = this.addSetting(new h("Primary Color", "Main energy color", -10101505));
   private final h N = this.addSetting(new h("Secondary Color", "Trail and impact color", -32547));
   private final m L = this.addSetting(new m("Bloom", "Use the shared world bloom pass", true));
   private final m J = this.addSetting(new m("Sound", "Local windup and finishing cues", true));
   private final b b = this.addSetting(new b("Volume", "Local sound volume", 0.55, 0.0, 1.0, 0.05));
   private final b B = this.addSetting(new b("Render Distance", "Maximum distance in blocks", 48.0, 8.0, 64.0, 4.0));
   private final v3 a = new v3();
   private final ba W = new ba();
   private final ArrayList<KillFXModule.o> o = new ArrayList<>();
   private List<KillFXModule.q> Y = List.of();
   private Object E;
   private Object O;
   private int H = -1;
   private int w = -1;
   private long m;
   private Vec3 A;
   private __ u;
   private CameraRenderState l;
   private static final RenderStateDataKey<KillFXModule.v> x = RenderStateDataKey.create(() -> "Larp KillFX confirmed victim");

   public KillFXModule() {
      super("KillFX", "Play cinematic finishers when a player you damaged dies", Category.VISUALS, -1);
      c = this;
      this.q.a(() -> this.M.w("Custom"));
      this.N.a(() -> this.M.w("Custom"));
      this.b.a(() -> this.J.getValue());
      this.r.w(var1 -> {
         this.D();
         _q.z("KillFX", "Style changed: {}; active finishers cleared", var1);
      });
   }

   public static KillFXModule J() {
      return c;
   }

   public List<com.larpclient.module.r<?>> j() {
      return List.of(this.r, this.M, this.q, this.N);
   }

   public com.larpclient.module.r<?> h() {
      return this.r;
   }

   public List<com.larpclient.module.r<?>> C() {
      return List.of(this.L, this.J, this.b);
   }

   public List<com.larpclient.module.r<?>> e() {
      return List.of(this.B);
   }

   public String u() {
      return switch ((String)this.r.getValue()) {
         case "Execution" -> "Lifts the victim into a double helix, slices the body and drops the skinned pieces onto terrain.";
         case "Singularity" -> "Twists the body into a collapsing star vortex, then releases a sharp ring of light.";
         case "Soul Pyre" -> "Burns the skin away in glowing fragments while spiralling soul embers rise from the body.";
         case "Star of David" -> "Lifts the body and turns its six skinned parts into two interlocking triangles facing your view.";
         case "Tung Tung" -> "Summons a towering Tung Tung Sahur, who raises his wooden bat and slams the victim to the ground.";
         case "Tung Tung Floss" -> "Sahur crouches, jumps onto the fallen player and dances the Floss with swinging arms and counter-swaying hips.";
         case "Sakura" -> "A petal shockwave drops the victim, the body blooms into a swirling sphere under a pillar of light, then bursts and settles into a glowing yin-yang seal.";
         case "Shatter" -> "Frost climbs the frozen victim and cracks of light spread before the body shatters into ice shards and tumbling pieces that melt away.";
         case "Rug Pull" -> "A trading chart pumps from $0 to $1,000 beside the victim, then a red candle crashes through the floor, the rug is yanked away and the profit drops to -$67,000 while a headline falls from the sky and floats in front of the chart.";
         default -> "Varies between all finishers without repeating the last animation.";
      };
   }

   @Override
   public void onEnable() {
      this.D();
      _q.z("KillFX", "Enabled: {}; server death events only, four playing finishers maximum", this.r.getValue());
   }

   @Override
   public void onDisable() {
      this.D();
   }

   public void D() {
      this.a.F();
      this.o.clear();
      this.Y = List.of();
      this.W.v();
      if (this.u != null) {
         this.u.close();
         this.u = null;
      }

      this.l = null;
      this.E = this.O = null;
      this.A = null;
      this.H = -1;
   }

   private boolean D() {
      Minecraft var1 = getMc();
      if (this.isRuntimeActive() && var1.level != null && var1.player != null) {
         if (this.E != var1.level || this.O != var1.player || this.H != nl.q || this.A != null && this.A.distanceToSqr(var1.player.position()) > 256.0) {
            this.D();
            this.E = var1.level;
            this.O = var1.player;
            this.H = nl.q;
         }

         this.A = var1.player.position();
         return true;
      } else {
         this.D();
         return false;
      }
   }

   public void M(ClientboundDamageEventPacket var1) {
      if (this.isEnabled()) {
         if (this.D()) {
            Minecraft var2 = getMc();
            Entity var3 = var2.level.getEntity(var1.entityId());
            if (var3 instanceof Player && var3 != var2.player) {
               this.a.t(var3, var1.sourceCauseId() == var2.player.getId(), hz.S());
            }
         }
      }
   }

   public void i(ClientboundEntityEventPacket var1) {
      if (this.isEnabled()) {
         if (this.D()) {
            Minecraft var2 = getMc();
            if (var1.getEntity(var2.level) instanceof AbstractClientPlayer var4 && var4 != var2.player) {
               if (var1.getEventId() == 35) {
                  this.a.L(var4);
                  this.o.removeIf(var1x -> var1x.K == var4);
                  this.Y = this.Y.stream().filter(var1x -> var1x.owner.K != var4).toList();
               } else if (var1.getEventId() == 3 && this.a.I(var4, hz.S())) {
                  if (!(var4.position().distanceToSqr(var2.player.position()) > this.B.getValue() * this.B.getValue())) {
                     double var5 = hz.S();
                     n5.f var7 = this.B();
                     int var8 = var7 == n5.f.f ? -19633 : (var7 == n5.f.R ? -5144321 : -10101505);
                     int var9 = var7 == n5.f.f ? -8915729 : (var7 == n5.f.R ? -8328705 : -32547);
                     if (var7 == n5.f.G) {
                        var8 = -8474625;
                        var9 = -1444097;
                     }

                     if (var7 == n5.f.N || var7 == n5.f.a) {
                        var8 = -18594;
                        var9 = -5188;
                     }

                     if (var7 == n5.f.h) {
                        var8 = -22842;
                        var9 = -3337;
                     }

                     if (var7 == n5.f.i) {
                        var8 = -7938817;
                        var9 = -1114881;
                     }

                     if (var7 == n5.f.S) {
                        var8 = -13242230;
                        var9 = -50360;
                     }

                     if (this.M.w("Custom")) {
                        var8 = this.q.getValue();
                        var9 = this.N.getValue();
                     } else if (this.M.w("Theme")) {
                        var8 = com.larpclient.c.y;
                        var9 = com.larpclient.c.t;
                     }

                     long var10 = var4.getUUID().getMostSignificantBits() ^ var4.getUUID().getLeastSignificantBits() ^ ++this.m;
                     double var12 = Math.toRadians(var4.yBodyRot);
                     n5 var14 = new n5(var7, var4.position(), var12, var5, var10, var4.getSkin().model() == PlayerModelType.SLIM);
                     List var15 = this.o.stream().filter(var0 -> !var0.j()).toList();
                     if (var15.size() >= 4) {
                        ((KillFXModule.o)var15.getFirst()).O(var5);
                        _q.c("KillFX", "Finisher limit reached; retiring the oldest ({}) over {}s", ((KillFXModule.o)var15.getFirst()).n.E, 0.45);
                     }

                     while (this.o.size() >= 8) {
                        this.o.removeFirst();
                     }

                     KillFXModule.o var16 = new KillFXModule.o(var4, var14, var8, var9, this.L.getValue(), this.J.getValue(), this.b.getValue());
                     this.o.add(var16);
                     this.D(
                        var16,
                        var7 == n5.f.f
                           ? SoundEvents.FIRECHARGE_USE
                           : (
                              var7 == n5.f.h
                                 ? SoundEvents.CHERRY_LEAVES_BREAK
                                 : (
                                    var7 == n5.f.i
                                       ? SoundEvents.PLAYER_HURT_FREEZE
                                       : (
                                          var7 == n5.f.S
                                             ? (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value()
                                             : (
                                                var7 == n5.f.a
                                                   ? SoundEvents.PLAYER_ATTACK_SWEEP
                                                   : (var7 == n5.f.N ? SoundEvents.IRON_GOLEM_ATTACK : SoundEvents.AMETHYST_BLOCK_RESONATE)
                                             )
                                       )
                                 )
                           ),
                        0.8F
                     );
                     _q.c("KillFX", "Confirmed player death: id={}, style={}, source=recent observed server damage", var4.getId(), var7);
                  }
               }
            }
         }
      }
   }

   private n5.f B() {
      if (!this.r.w("Random")) {
         return switch ((String)this.r.getValue()) {
            case "Execution" -> n5.f.Q;
            case "Singularity" -> n5.f.R;
            case "Star of David" -> n5.f.G;
            case "Tung Tung" -> n5.f.N;
            case "Tung Tung Floss" -> n5.f.a;
            case "Sakura" -> n5.f.h;
            case "Shatter" -> n5.f.i;
            case "Rug Pull" -> n5.f.S;
            default -> n5.f.f;
         };
      } else {
         int var1 = n5.f.values().length;
         int var2 = Math.floorMod(this.m * 31L + Double.doubleToLongBits(hz.S()), var1);
         if (var2 == this.w) {
            var2 = (var2 + 1) % var1;
         }

         this.w = var2;
         return n5.f.values()[var2];
      }
   }

   public void a() {
      if (this.isEnabled()) {
         if (this.D()) {
            Minecraft var1 = getMc();
            double var2 = hz.S();
            this.a.q(var2, var1x -> var1x instanceof Entity var2x && !var2x.isRemoved() && var2x.level() == var1.level);
            this.o.removeIf(var2x -> var2x.n.R(var2) || var2x.j() && var2 - var2x.F >= 0.45);

            for (KillFXModule.o var5 : this.o) {
               if (var5.N && !var5.K.isRemoved() && var5.K.isAlive() && !var5.j()) {
                  var5.O(var2);
                  _q.c("KillFX", "Victim id={} alive again; retiring its {} finisher", var5.K.getId(), var5.n.E);
               }

               var5.N = (boolean)(var5.N | (!var5.K.isAlive() ? 1 : 0));
               var5.n.V(var2, (var1x, var2x) -> v9.u(var1.level, var1x, var2x));
               KillFXModule.r[] var6 = H(var5.n.E);

               while (var5.c < var6.length && var2 - var5.n.A >= var6[var5.c].time()) {
                  KillFXModule.r var7 = var6[var5.c++];
                  var5.H = (boolean)1;
                  if (!var5.j()) {
                     this.D(var5, var7.event(), var7.pitch());
                  }
               }
            }
         }
      }
   }

   private static KillFXModule.r[] H(n5.f var0) {
      return switch (var0) {
         case Q -> new KillFXModule.r[]{new KillFXModule.r(0.85, (SoundEvent)SoundEvents.TRIDENT_THUNDER.value(), 1.25F)};
         case R -> new KillFXModule.r[]{new KillFXModule.r(1.9, (SoundEvent)SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(), 1.25F)};
         case a -> new KillFXModule.r[]{new KillFXModule.r(1.62, SoundEvents.PLAYER_BIG_FALL, 1.25F)};
         case N -> new KillFXModule.r[]{new KillFXModule.r(1.56, SoundEvents.ANVIL_LAND, 1.25F)};
         case G -> new KillFXModule.r[]{new KillFXModule.r(1.85, SoundEvents.AMETHYST_BLOCK_RESONATE, 1.25F)};
         case f -> new KillFXModule.r[]{new KillFXModule.r(1.25, SoundEvents.BEACON_DEACTIVATE, 1.25F)};
         case h -> new KillFXModule.r[]{
            new KillFXModule.r(1.0, SoundEvents.BEACON_ACTIVATE, 1.4F),
            new KillFXModule.r(3.2, (SoundEvent)SoundEvents.BREEZE_WIND_CHARGE_BURST.value(), 1.1F),
            new KillFXModule.r(5.0, SoundEvents.AMETHYST_BLOCK_CHIME, 0.9F)
         };
         case i -> new KillFXModule.r[]{
            new KillFXModule.r(0.95, SoundEvents.AMETHYST_BLOCK_CHIME, 1.6F),
            new KillFXModule.r(1.32, SoundEvents.GLASS_BREAK, 0.75F),
            new KillFXModule.r(1.87, SoundEvents.AMETHYST_CLUSTER_FALL, 1.3F)
         };
         case S -> new KillFXModule.r[]{
            new KillFXModule.r(0.7, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 0.75F),
            new KillFXModule.r(1.1, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 0.9F),
            new KillFXModule.r(1.45, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 1.05F),
            new KillFXModule.r(1.75, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 1.25F),
            new KillFXModule.r(2.0, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 1.5F),
            new KillFXModule.r(2.3, SoundEvents.EXPERIENCE_ORB_PICKUP, 1.6F),
            new KillFXModule.r(2.6, (SoundEvent)SoundEvents.NOTE_BLOCK_BASS.value(), 0.5F),
            new KillFXModule.r(2.64, (SoundEvent)SoundEvents.NOTE_BLOCK_DIDGERIDOO.value(), 0.5F),
            new KillFXModule.r(2.82, SoundEvents.MACE_SMASH_GROUND_HEAVY, 0.8F),
            new KillFXModule.r(3.25, SoundEvents.VILLAGER_NO, 0.85F)
         };
      };
   }

   private void D(KillFXModule.o var1, SoundEvent var2, float var3) {
      if (var1.i && !(var1.h <= 0.0) && getMc().level != null) {
         Vec3 var4 = var1.n.r;
         float var10006 = (float)var1.h;
         getMc().level.playLocalSound(var4.x, var4.y, var4.z, var2, SoundSource.PLAYERS, var10006, var3, false);
      }
   }

   public void N(CameraRenderState var1) {
      if (this.isEnabled()) {
         Vec3 var2 = var1.pos;
         if (this.u != null) {
            this.u.b();
         }

         this.l = null;
         this.Y = List.of();
         if (this.D() && !this.o.isEmpty()) {
            ArrayList var3 = new ArrayList();
            Vector3f var4 = var1.orientation.transform(new Vector3f(1.0F, 0.0F, 0.0F));
            Vector3f var5 = var1.orientation.transform(new Vector3f(0.0F, 1.0F, 0.0F));
            Vec3 var6 = new Vec3(var4.x, var4.y, var4.z);
            Vec3 var7 = new Vec3(var5.x, var5.y, var5.z);
            this.W.Q();
            ArrayList var8 = new ArrayList();

            for (KillFXModule.o var10 : this.o) {
               if (!(var10.n.r.distanceToSqr(var2) > this.B.getValue() * this.B.getValue())) {
                  if (var10.j == null) {
                     if (!(w7.u(var10.K, 1.0F) instanceof AvatarRenderState var12)) {
                        _q.g("KillFX", "Native avatar snapshot unavailable; leaving original corpse visible", null);
                        continue;
                     }

                     var10.j = var12;
                  }

                  double var20 = Math.max(0.0, hz.Q() - var10.n.A);
                  hg.k var13 = var10.n.E == n5.f.G ? hg.M(hg.Y(var10.n, var20), var2, var6, var7) : (var10.n.R() != null ? var10.n.R().L(var2, hz.Q()) : hg.c);
                  n5.p[] var14 = new n5.p[6];
                  double var15 = var10.b(hz.Q());
                  if (var10.n.R() != null) {
                     if (this.u == null) {
                        this.u = new __();
                     }

                     if (this.u.H()) {
                        try {
                           var10.n.R().g(var13, var20, var15, this.u::R, var3);
                        } catch (RuntimeException var19) {
                           _q.g("KillFX", "Rug Pull chart labels failed; the chart continues without text", var19);
                        }
                     }
                  }

                  for (int var17 = 0; var17 < var14.length; var17++) {
                     n5.p var18 = var10.n.t(var17, var20, hz.C(), var13);
                     var14[var17] = var15 >= 1.0
                        ? var18
                        : new n5.p(
                           var18.position(),
                           var18.rx(),
                           var18.ry(),
                           var18.rz(),
                           var18.scale(),
                           var18.alpha() * var15,
                           var18.dissolve(),
                           var18.landed(),
                           var18.orientation(),
                           var18.stretch()
                        );
                  }

                  sr.v var21 = null;
                  ae.f var22 = null;
                  if (var10.n.E == n5.f.N) {
                     fn.V();
                     if (var10.S == null) {
                        var10.S = new sr(fn.e());
                        _q.c("KillFX", "Tung Tung ready: shared Sahur mesh, height={} blocks, articulated bat strike at {}s", 3.6, 1.56);
                     }

                     var21 = var10.S.w(var10.n, var20, var2);
                  }

                  if (var10.n.E == n5.f.a) {
                     fn.V();
                     if (var10.W == null) {
                        var10.W = new ae(fn.e());
                        _q.c("KillFX", "Tung Tung Floss ready: articulated jump, landing at {}s, four Floss cycles, expiry={}s", 1.62, 7.5);
                     }

                     var22 = var10.W.n(var10.n, var20, var2);
                  }

                  var8.add(new KillFXModule.q(var10, var10.j, var14, var20, var2, var13, var21, var22, this.H, var15));
               }
            }

            this.Y = List.copyOf(var8);
            if (this.u != null) {
               this.u.a(var3, var1);
               this.l = var1;
            }
         }
      }
   }

   public boolean s() {
      return !this.o.isEmpty();
   }

   public void i(Entity var1, EntityRenderState var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var1.level() == this.E) {
            for (KillFXModule.o var4 : this.o) {
               if (var4.K == var1) {
                  var2.setData(x, new KillFXModule.v(var1, this.E, this.H));
                  return;
               }
            }
         }
      }
   }

   public boolean L(EntityRenderState var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isRuntimeActive() && wp.Q() && wp.Z("KillFX") && !w7.x()) {
         KillFXModule.v var2 = (KillFXModule.v)var1.getData(x);
         if (var2 != null && var2.world == getMc().level && var2.generation == nl.q) {
            for (KillFXModule.q var4 : this.Y) {
               if (var4.owner.K == var2.victim
                  && this.o.contains(var4.owner)
                  && var4.generation == var2.generation
                  && (var4.age < 0.25 || var1 instanceof LivingEntityRenderState var5 && var5.deathTime > 0.0F)) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean L() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.Y.stream().anyMatch(var0 -> var0.owner.z);
   }

   public void l(fz var1, PoseStack var2, wh var3, boolean var4) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            long var5 = he.s();

            try {
               for (KillFXModule.q var8 : this.Y) {
                  if (var8.generation == nl.q && (!var4 || var8.owner.z)) {
                     this.W.x(var8.skin, var8.owner.n, var8.parts, var8.camera, var2, var3, var8.owner.M, var4);
                     if (!var4 && var8.strike != null) {
                        fn.U(var2, var3, var8.strike.transform(), var8.strike.vertices(), var8.strike.colors(), var8.strike.opacity() * var8.fade);
                     }

                     if (!var4 && var8.floss != null) {
                        fn.U(var2, var3, var8.floss.transform(), var8.floss.vertices(), var8.floss.colors(), var8.floss.opacity() * var8.fade);
                     }

                     vq.l(var1, var8.owner.n, var8.parts, var8.age, var8.owner.M, var8.owner.E, var4, var8.fade, var8.starPlane, var8.strike, var8.floss);
                  }
               }

               if (!var4 && this.u != null && this.l != null) {
                  this.u.r(var2, this.l, false, false, 1.0);
               }
            } finally {
               he.T("KillFX", var5);
            }
         }
      }
   }

   private static final class o {
      final AbstractClientPlayer K;
      final n5 n;
      final int M;
      final int E;
      final boolean z;
      final boolean i;
      final double h;
      AvatarRenderState j;
      sr S;
      ae W;
      boolean H;
      boolean N;
      int c;
      double F = Double.NaN;

      o(AbstractClientPlayer var1, n5 var2, int var3, int var4, boolean var5, boolean var6, double var7) {
         this.K = var1;
         this.n = var2;
         this.M = var3;
         this.E = var4;
         this.z = var5;
         this.i = var6;
         this.h = var7;
      }

      boolean j() {
         return !Double.isNaN(this.F);
      }

      void O(double var1) {
         if (!this.j()) {
            this.F = var1;
         }
      }

      double b(double var1) {
         return this.j() ? 1.0 - n5.g((var1 - this.F) / 0.45) : 1.0;
      }
   }

   private record q(
      KillFXModule.o owner, AvatarRenderState skin, n5.p[] parts, double age, Vec3 camera, hg.k starPlane, sr.v strike, ae.f floss, int generation, double fade
   ) {
   }

   private record r(double time, SoundEvent event, float pitch) {
   }

   private record v(Object victim, Object world, int generation) {
   }
}

package com.larpclient.module;

import com.larpclient._i;
import com.larpclient.ea;
import com.larpclient.et;
import com.larpclient.ez;
import com.larpclient.f8;
import com.larpclient.h_;
import com.larpclient.jk;
import com.larpclient.r4;
import com.larpclient.r6;
import com.larpclient.ri;
import com.larpclient.rp;
import com.larpclient.rv;
import com.larpclient.u4;
import com.larpclient.ud;
import com.larpclient.un;
import com.larpclient.uv;
import com.larpclient.xy;
import com.larpclient.y2;
import com.larpclient.y9;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class KillFXModule extends Module {
   public static final int p = 4;
   public static final double e = 0.45;
   private static KillFXModule w;
   private final lp a = this.addSetting(
      new lp(
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
         "Shatter"
      )
   );
   private final lp d = this.addSetting(new lp("Colors", "Authored palettes, your theme or custom colors", "Style", "Style", "Theme", "Custom"));
   private final lv n = this.addSetting(new lv("Primary Color", "Main energy color", -10101505));
   private final lv y = this.addSetting(new lv("Secondary Color", "Trail and impact color", -32547));
   private final ln D = this.addSetting(new ln("Bloom", "Use the shared world bloom pass", true));
   private final ln G = this.addSetting(new ln("Sound", "Local windup and finishing cues", true));
   private final lc C = this.addSetting(new lc("Volume", "Local sound volume", 0.55, 0.0, 1.0, 0.05));
   private final lc v = this.addSetting(new lc("Render Distance", "Maximum distance in blocks", 48.0, 8.0, 64.0, 4.0));
   private final un g = new un();
   private final ud u = new ud();
   private final ArrayList<KillFXModule.d> O = new ArrayList<>();
   private List<KillFXModule.l> j = List.of();
   private Object Y;
   private Object s;
   private int M = -1;
   private int i = -1;
   private long S;
   private Vec3 N;
   private static final RenderStateDataKey<KillFXModule.g> k = RenderStateDataKey.create(() -> "Larp KillFX confirmed victim");

   public KillFXModule() {
      super("KillFX", "Play cinematic finishers when a player you damaged dies", Category.VISUALS, -1);
      w = this;
      this.n.x(() -> this.d.i("Custom"));
      this.y.x(() -> this.d.i("Custom"));
      this.C.x(() -> this.G.getValue());
      this.a.W(var1 -> {
         this.d();
         r4.V("KillFX", "Style changed: {}; active finishers cleared", var1);
      });
   }

   public static KillFXModule X() {
      return w;
   }

   public List<l6<?>> F() {
      return List.of(this.a, this.d, this.n, this.y);
   }

   public l6<?> m() {
      return this.a;
   }

   public List<l6<?>> K() {
      return List.of(this.D, this.G, this.C);
   }

   public List<l6<?>> M() {
      return List.of(this.v);
   }

   public String V() {
      return switch ((String)this.a.getValue()) {
         case "Execution" -> "Lifts the victim into a double helix, slices the body and drops the skinned pieces onto terrain.";
         case "Singularity" -> "Twists the body into a collapsing star vortex, then releases a sharp ring of light.";
         case "Soul Pyre" -> "Burns the skin away in glowing fragments while spiralling soul embers rise from the body.";
         case "Star of David" -> "Lifts the body and turns its six skinned parts into two interlocking triangles facing your view.";
         case "Tung Tung" -> "Summons a towering Tung Tung Sahur, who raises his wooden bat and slams the victim to the ground.";
         case "Tung Tung Floss" -> "Sahur crouches, jumps onto the fallen player and dances the Floss with swinging arms and counter-swaying hips.";
         case "Sakura" -> "A petal shockwave drops the victim, the body blooms into a swirling sphere under a pillar of light, then bursts and settles into a glowing yin-yang seal.";
         case "Shatter" -> "Frost climbs the frozen victim and cracks of light spread before the body shatters into ice shards and tumbling pieces that melt away.";
         default -> "Varies between all finishers without repeating the last animation.";
      };
   }

   @Override
   public void onEnable() {
      this.d();
      r4.V("KillFX", "Enabled: {}; server death events only, four playing finishers maximum", this.a.getValue());
   }

   @Override
   public void onDisable() {
      this.d();
   }

   public void d() {
      this.g.Q();
      this.O.clear();
      this.j = List.of();
      this.u.P();
      this.Y = this.s = null;
      this.N = null;
      this.M = -1;
   }

   private boolean w() {
      Minecraft var1 = getMc();
      if (this.a() && var1.level != null && var1.player != null) {
         if (this.Y != var1.level || this.s != var1.player || this.M != jk.v || this.N != null && this.N.distanceToSqr(var1.player.position()) > 256.0) {
            this.d();
            this.Y = var1.level;
            this.s = var1.player;
            this.M = jk.v;
         }

         this.N = var1.player.position();
         return true;
      } else {
         this.d();
         return false;
      }
   }

   public void n(ClientboundDamageEventPacket var1) {
      if (this.isEnabled()) {
         if (this.w()) {
            Minecraft var2 = getMc();
            Entity var3 = var2.level.getEntity(var1.entityId());
            if (var3 instanceof Player && var3 != var2.player) {
               this.g.c(var3, var1.sourceCauseId() == var2.player.getId(), ea.t());
            }
         }
      }
   }

   public void e(ClientboundEntityEventPacket var1) {
      if (this.isEnabled()) {
         if (this.w()) {
            Minecraft var2 = getMc();
            if (var1.getEntity(var2.level) instanceof AbstractClientPlayer var4 && var4 != var2.player) {
               if (var1.getEventId() == 35) {
                  this.g.w(var4);
                  this.O.removeIf(var1x -> var1x.i == var4);
                  this.j = this.j.stream().filter(var1x -> var1x.owner.i != var4).toList();
               } else if (var1.getEventId() == 3 && this.g.P(var4, ea.t())) {
                  if (!(var4.position().distanceToSqr(var2.player.position()) > this.v.getValue() * this.v.getValue())) {
                     double var5 = ea.t();
                     ez.k var7 = HalloweenModule.K() ? ez.k.K : this.v();
                     int var8 = var7 == ez.k.K ? -19633 : (var7 == ez.k.r ? -5144321 : -10101505);
                     int var9 = var7 == ez.k.K ? -8915729 : (var7 == ez.k.r ? -8328705 : -32547);
                     if (var7 == ez.k.G) {
                        var8 = -8474625;
                        var9 = -1444097;
                     }

                     if (var7 == ez.k.k || var7 == ez.k.d) {
                        var8 = -18594;
                        var9 = -5188;
                     }

                     if (var7 == ez.k.A) {
                        var8 = -22842;
                        var9 = -3337;
                     }

                     if (var7 == ez.k.T) {
                        var8 = -7938817;
                        var9 = -1114881;
                     }

                     if (this.d.i("Custom")) {
                        var8 = this.n.getValue();
                        var9 = this.y.getValue();
                     } else if (this.d.i("Theme")) {
                        var8 = rv.e;
                        var9 = rv.GQ;
                     }

                     long var10 = var4.getUUID().getMostSignificantBits() ^ var4.getUUID().getLeastSignificantBits() ^ ++this.S;
                     double var12 = HalloweenModule.K()
                        ? Math.atan2(var2.player.getX() - var4.getX(), var2.player.getZ() - var4.getZ())
                        : Math.toRadians(var4.yBodyRot);
                     ez var14 = new ez(var7, var4.position(), var12, var5, var10, var4.getSkin().model() == PlayerModelType.SLIM);
                     List var15 = this.O.stream().filter(var0 -> !var0.a()).toList();
                     if (var15.size() >= 4) {
                        ((KillFXModule.d)var15.getFirst()).I(var5);
                        r4.g("KillFX", "Finisher limit reached; retiring the oldest ({}) over {}s", ((KillFXModule.d)var15.getFirst()).p.j, 0.45);
                     }

                     while (this.O.size() >= 8) {
                        this.O.removeFirst();
                     }

                     KillFXModule.d var16 = new KillFXModule.d(var4, var14, var8, var9, this.D.getValue(), this.G.getValue(), this.C.getValue());
                     var16.X = HalloweenModule.K();
                     this.O.add(var16);
                     if (!var16.X) {
                        this.h(
                           var16,
                           var7 == ez.k.K
                              ? SoundEvents.FIRECHARGE_USE
                              : (
                                 var7 == ez.k.A
                                    ? SoundEvents.CHERRY_LEAVES_BREAK
                                    : (
                                       var7 == ez.k.T
                                          ? SoundEvents.PLAYER_HURT_FREEZE
                                          : (
                                             var7 == ez.k.d
                                                ? SoundEvents.PLAYER_ATTACK_SWEEP
                                                : (var7 == ez.k.k ? SoundEvents.IRON_GOLEM_ATTACK : SoundEvents.AMETHYST_BLOCK_RESONATE)
                                          )
                                    )
                              ),
                           0.8F
                        );
                     }

                     r4.g(
                        "KillFX",
                        "Confirmed player death: id={}, style={}, source=recent observed server damage",
                        var4.getId(),
                        var16.X ? "Soul Harvest" : var7
                     );
                  }
               }
            }
         }
      }
   }

   private ez.k v() {
      if (!this.a.i("Random")) {
         return switch ((String)this.a.getValue()) {
            case "Execution" -> ez.k.V;
            case "Singularity" -> ez.k.r;
            case "Star of David" -> ez.k.G;
            case "Tung Tung" -> ez.k.k;
            case "Tung Tung Floss" -> ez.k.d;
            case "Sakura" -> ez.k.A;
            case "Shatter" -> ez.k.T;
            default -> ez.k.K;
         };
      } else {
         int var1 = ez.k.values().length;
         int var2 = Math.floorMod(this.S * 31L + Double.doubleToLongBits(ea.t()), var1);
         if (var2 == this.i) {
            var2 = (var2 + 1) % var1;
         }

         this.i = var2;
         return ez.k.values()[var2];
      }
   }

   public void q() {
      if (this.isEnabled()) {
         if (this.w()) {
            Minecraft var1 = getMc();
            double var2 = ea.t();
            this.g.W(var2, var1x -> var1x instanceof Entity var2x && !var2x.isRemoved() && var2x.level() == var1.level);
            this.O
               .removeIf(
                  var3 -> (var3.X ? var2 - var3.p.G >= 2.2 : var3.p.F(var2))
                     || var3.X && !HalloweenModule.K()
                     || !var3.X && !this.isRuntimeActive()
                     || var3.X && var3.l && !var3.i.isRemoved() && var3.i.isAlive()
                     || var3.a() && var2 - var3.M >= 0.45
               );

            for (KillFXModule.d var5 : this.O) {
               if (!var5.X && var5.l && !var5.i.isRemoved() && var5.i.isAlive() && !var5.a()) {
                  var5.I(var2);
                  r4.g("KillFX", "Victim id={} alive again; retiring its {} finisher", var5.i.getId(), var5.p.j);
               }

               var5.l = (boolean)(var5.l | (!var5.i.isAlive() ? 1 : 0));
               if (!var5.X) {
                  var5.p.w(var2, (var1x, var2x) -> et.Q(var1.level, var1x, var2x));
                  KillFXModule.s[] var6 = W(var5.p.j);

                  while (var5.A < var6.length && var2 - var5.p.G >= var6[var5.A].time()) {
                     KillFXModule.s var7 = var6[var5.A++];
                     var5.E = (boolean)1;
                     if (!var5.a()) {
                        this.h(var5, var7.event(), var7.pitch());
                     }
                  }
               } else if (!var5.E && var2 - var5.p.G >= 0.78) {
                  var5.E = (boolean)1;
                  r4.g("Halloween", "Pumpkin smash impact: victim={}, age={}s", var5.i.getId(), var2 - var5.p.G);
               }
            }
         }
      }
   }

   private static KillFXModule.s[] W(ez.k var0) {
      return switch (var0) {
         case V -> new KillFXModule.s[]{new KillFXModule.s(0.85, (SoundEvent)SoundEvents.TRIDENT_THUNDER.value(), 1.25F)};
         case r -> new KillFXModule.s[]{new KillFXModule.s(1.9, (SoundEvent)SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(), 1.25F)};
         case d -> new KillFXModule.s[]{new KillFXModule.s(1.62, SoundEvents.PLAYER_BIG_FALL, 1.25F)};
         case k -> new KillFXModule.s[]{new KillFXModule.s(1.56, SoundEvents.ANVIL_LAND, 1.25F)};
         case G -> new KillFXModule.s[]{new KillFXModule.s(1.85, SoundEvents.AMETHYST_BLOCK_RESONATE, 1.25F)};
         case K -> new KillFXModule.s[]{new KillFXModule.s(1.25, SoundEvents.BEACON_DEACTIVATE, 1.25F)};
         case A -> new KillFXModule.s[]{
            new KillFXModule.s(1.0, SoundEvents.BEACON_ACTIVATE, 1.4F),
            new KillFXModule.s(3.2, (SoundEvent)SoundEvents.BREEZE_WIND_CHARGE_BURST.value(), 1.1F),
            new KillFXModule.s(5.0, SoundEvents.AMETHYST_BLOCK_CHIME, 0.9F)
         };
         case T -> new KillFXModule.s[]{
            new KillFXModule.s(0.95, SoundEvents.AMETHYST_BLOCK_CHIME, 1.6F),
            new KillFXModule.s(1.32, SoundEvents.GLASS_BREAK, 0.75F),
            new KillFXModule.s(1.87, SoundEvents.AMETHYST_CLUSTER_FALL, 1.3F)
         };
      };
   }

   private void h(KillFXModule.d var1, SoundEvent var2, float var3) {
      if (var1.H && !(var1.s <= 0.0) && getMc().level != null) {
         Vec3 var4 = var1.p.D;
         float var10006 = (float)var1.s;
         getMc().level.playLocalSound(var4.x, var4.y, var4.z, var2, SoundSource.PLAYERS, var10006, var3, false);
      }
   }

   public void g(CameraRenderState var1) {
      if (this.isEnabled()) {
         Vec3 var2 = var1.pos;
         this.j = List.of();
         if (this.w() && !this.O.isEmpty()) {
            Vector3f var3 = var1.orientation.transform(new Vector3f(1.0F, 0.0F, 0.0F));
            Vector3f var4 = var1.orientation.transform(new Vector3f(0.0F, 1.0F, 0.0F));
            Vec3 var5 = new Vec3(var3.x, var3.y, var3.z);
            Vec3 var6 = new Vec3(var4.x, var4.y, var4.z);
            this.u.T();
            ArrayList var7 = new ArrayList();

            for (KillFXModule.d var9 : this.O) {
               if (!(var9.p.D.distanceToSqr(var2) > this.v.getValue() * this.v.getValue())) {
                  if (var9.X) {
                     if (HalloweenModule.K()) {
                        double var19 = ea.y() - var9.p.G;
                        Vec3 var20 = var9.p.D;
                        if (var1.cullFrustum == null
                           || var1.cullFrustum.isVisible(new AABB(var20.x - 3.5, var20.y, var20.z - 3.5, var20.x + 3.5, var20.y + 5.6, var20.z + 3.5))) {
                           xy.j();
                           if (var9.d == null) {
                              if (uv.v(var9.i, 1.0F) instanceof AvatarRenderState var23) {
                                 var9.d = var23;
                              } else {
                                 r4.T("Halloween", "Victim skin unavailable; keeping native corpse while the intact pumpkin and wave render", null);
                              }
                           }

                           ez.q[] var22 = var9.d == null ? null : ri.r(var9.p, var19);
                           var7.add(
                              new KillFXModule.l(
                                 var9, var9.d, var22, var19, var2, null, null, null, ri.t(var20, var9.p.A, var19, HalloweenModule.U().t()), this.M, 1.0
                              )
                           );
                        }
                     }
                  } else {
                     if (var9.d == null) {
                        if (!(uv.v(var9.i, 1.0F) instanceof AvatarRenderState var11)) {
                           r4.T("KillFX", "Native avatar snapshot unavailable; leaving original corpse visible", null);
                           continue;
                        }

                        var9.d = var11;
                     }

                     double var18 = Math.max(0.0, ea.y() - var9.p.G);
                     u4.v var12 = var9.p.j == ez.k.G ? u4.g(u4.u(var9.p, var18), var2, var5, var6) : u4.x;
                     ez.q[] var13 = new ez.q[6];
                     double var14 = var9.q(ea.y());

                     for (int var16 = 0; var16 < var13.length; var16++) {
                        ez.q var17 = var9.p.T(var16, var18, ea.Z(), var12);
                        var13[var16] = var14 >= 1.0
                           ? var17
                           : new ez.q(
                              var17.position(),
                              var17.rx(),
                              var17.ry(),
                              var17.rz(),
                              var17.scale(),
                              var17.alpha() * var14,
                              var17.dissolve(),
                              var17.landed(),
                              var17.orientation(),
                              var17.stretch()
                           );
                     }

                     com.larpclient.l9.p var24 = null;
                     y9.w var25 = null;
                     if (var9.p.j == ez.k.k) {
                        _i.c();
                        if (var9.U == null) {
                           var9.U = new com.larpclient.l9(_i.P());
                           r4.g("KillFX", "Tung Tung ready: shared Sahur mesh, height={} blocks, articulated bat strike at {}s", 3.6, 1.56);
                        }

                        var24 = var9.U.Q(var9.p, var18, var2);
                     }

                     if (var9.p.j == ez.k.d) {
                        _i.c();
                        if (var9.J == null) {
                           var9.J = new y9(_i.P());
                           r4.g("KillFX", "Tung Tung Floss ready: articulated jump, landing at {}s, four Floss cycles, expiry={}s", 1.62, 7.5);
                        }

                        var25 = var9.J.D(var9.p, var18, var2);
                     }

                     var7.add(new KillFXModule.l(var9, var9.d, var13, var18, var2, var12, var24, var25, null, this.M, var14));
                  }
               }
            }

            this.j = List.copyOf(var7);
         }
      }
   }

   public void Z(Entity var1, EntityRenderState var2) {
      if (this.isEnabled()) {
         if (this.a() && var1.level() == this.Y) {
            for (KillFXModule.d var4 : this.O) {
               if (var4.i == var1) {
                  var2.setData(k, new KillFXModule.g(var1, this.Y, this.M));
                  return;
               }
            }
         }
      }
   }

   public boolean B(EntityRenderState var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.a() && f8.c() && f8.p("KillFX") && !uv.G()) {
         KillFXModule.g var2 = (KillFXModule.g)var1.getData(k);
         if (var2 != null && var2.world == getMc().level && var2.generation == jk.v) {
            for (KillFXModule.l var4 : this.j) {
               if (var4.skin != null
                  && var4.parts != null
                  && var4.owner.i == var2.victim
                  && this.O.contains(var4.owner)
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

   private boolean a() {
      return this.isRuntimeActive() || HalloweenModule.K();
   }

   public boolean C() {
      return !this.isEnabled()
         ? false
         : this.a() && this.j.stream().anyMatch(var0 -> var0.soul != null ? var0.soul.pumpkin() != null && HalloweenModule.U().t().y() : var0.owner.w);
   }

   public void v(r6 var1, PoseStack var2, y2 var3, boolean var4) {
      if (this.isEnabled()) {
         if (this.a()) {
            long var5 = h_.U();

            try {
               for (KillFXModule.l var8 : this.j) {
                  if (var8.soul != null) {
                     if (HalloweenModule.K() && var8.generation == jk.v && (!var4 || HalloweenModule.U().t().y())) {
                        if (!var4 && var8.skin != null && var8.parts != null) {
                           this.u.L(var8.skin, var8.owner.p, var8.parts, var8.camera, var2, var3, -1, false);
                        }

                        ri.U(var1, var2, var3, var8.soul, var8.camera, var4);
                     }
                  } else if (var8.generation == jk.v && (!var4 || var8.owner.w)) {
                     this.u.L(var8.skin, var8.owner.p, var8.parts, var8.camera, var2, var3, var8.owner.B, var4);
                     if (!var4 && var8.strike != null) {
                        _i.L(var2, var3, var8.strike.transform(), var8.strike.vertices(), var8.strike.colors(), var8.strike.opacity() * var8.fade);
                     }

                     if (!var4 && var8.floss != null) {
                        _i.L(var2, var3, var8.floss.transform(), var8.floss.vertices(), var8.floss.colors(), var8.floss.opacity() * var8.fade);
                     }

                     rp.Z(var1, var8.owner.p, var8.parts, var8.age, var8.owner.B, var8.owner.N, var4, var8.fade, var8.starPlane, var8.strike, var8.floss);
                  }
               }
            } finally {
               h_.n("KillFX", var5);
            }
         }
      }
   }

   private static final class d {
      final AbstractClientPlayer i;
      final ez p;
      final int B;
      final int N;
      final boolean w;
      final boolean H;
      final double s;
      AvatarRenderState d;
      com.larpclient.l9 U;
      y9 J;
      boolean E;
      boolean l;
      boolean X;
      int A;
      double M = Double.NaN;

      d(AbstractClientPlayer var1, ez var2, int var3, int var4, boolean var5, boolean var6, double var7) {
         this.i = var1;
         this.p = var2;
         this.B = var3;
         this.N = var4;
         this.w = var5;
         this.H = var6;
         this.s = var7;
      }

      boolean a() {
         return !Double.isNaN(this.M);
      }

      void I(double var1) {
         if (!this.a()) {
            this.M = var1;
         }
      }

      double q(double var1) {
         return this.a() ? 1.0 - ez.N((var1 - this.M) / 0.45) : 1.0;
      }
   }

   private record g(Object victim, Object world, int generation) {
   }

   private record l(
      KillFXModule.d owner,
      AvatarRenderState skin,
      ez.q[] parts,
      double age,
      Vec3 camera,
      u4.v starPlane,
      com.larpclient.l9.p strike,
      y9.w floss,
      ri.z soul,
      int generation,
      double fade
   ) {
   }

   private record s(double time, SoundEvent event, float pitch) {
   }
}

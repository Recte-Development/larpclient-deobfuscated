#version 330
#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:chunksection.glsl>
layout(std140) uniform LarpMaterial {
    vec4 WindTime;
    vec4 Water;
    vec4 WaterFlags;
    vec4 Wet;
    vec4 Modes;
    vec4 Sun;
    vec4 Sky;
    vec4 Camera;
    vec4 Snow;
    mat4 ViewProjection;
    mat4 InverseViewProjection;
    vec4 SnowTrailInfo;
    vec4 SnowTrailBounds;
    vec4 SnowTracks[48];
};

#moj_import <larp-client:snow.glsl>

uniform sampler2D Sampler0;
in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec3 materialPosition;
in vec3 materialWorld;
in vec3 materialBasePosition;
in float materialSkyLight;
in float materialSnowCap;
in float materialSnowEligible;
in vec3 materialLight;
out vec4 fragColor;
#moj_import <larp-client:wet_ground.glsl>

vec4 sampleNearest(sampler2D source, vec2 uv, vec2 pixelSize, vec2 du, vec2 dv, vec2 texelScreenSize) {
    // Convert our UV back up to texel coordinates and find out how far over we are from the center of each pixel
    vec2 uvTexelCoords = uv / pixelSize;
    vec2 texelCenter = round(uvTexelCoords) - 0.5f;
    vec2 texelOffset = uvTexelCoords - texelCenter;

    // Move our offset closer to the texel center based on texel size on screen
    texelOffset = (texelOffset - 0.5f) * pixelSize / texelScreenSize + 0.5f;
    texelOffset = clamp(texelOffset, 0.0f, 1.0f);

    uv = (texelCenter + texelOffset) * pixelSize;
    return textureGrad(source, uv, du, dv);
}

vec4 sampleNearest(sampler2D source, vec2 uv, vec2 pixelSize) {
    vec2 du = dFdx(uv);
    vec2 dv = dFdy(uv);
    vec2 texelScreenSize = sqrt(du * du + dv * dv);
    return sampleNearest(source, uv, pixelSize, du, dv, texelScreenSize);
}

// Rotated Grid Super-Sampling
vec4 sampleRGSS(sampler2D source, vec2 uv, vec2 pixelSize) {
    vec2 du = dFdx(uv);
    vec2 dv = dFdy(uv);

    vec2 texelScreenSize = sqrt(du * du + dv * dv);
    float maxTexelSize = max(texelScreenSize.x, texelScreenSize.y);

    float minPixelSize = min(pixelSize.x, pixelSize.y);

    float transitionStart = minPixelSize * 1.0;
    float transitionEnd = minPixelSize * 2.0;
    float blendFactor = smoothstep(transitionStart, transitionEnd, maxTexelSize);

    float duLength = length(du);
    float dvLength = length(dv);
    float minDerivative = min(duLength, dvLength);
    float maxDerivative = max(duLength, dvLength);

    float effectiveDerivative = sqrt(minDerivative * maxDerivative);

    float mipLevelExact = max(0.0, log2(effectiveDerivative / minPixelSize));

    float mipLevelLow = floor(mipLevelExact);
    float mipLevelHigh = mipLevelLow + 1.0;
    float mipBlend = fract(mipLevelExact);

    const vec2 offsets[4] = vec2[](
    vec2(0.125, 0.375),
    vec2(-0.125, -0.375),
    vec2(0.375, -0.125),
    vec2(-0.375, 0.125)
    );

    vec4 rgssColorLow = vec4(0.0);
    vec4 rgssColorHigh = vec4(0.0);
    for (int i = 0; i < 4; ++i) {
        vec2 sampleUV = uv + offsets[i] * pixelSize;
        rgssColorLow += textureLod(source, sampleUV, mipLevelLow);
        rgssColorHigh += textureLod(source, sampleUV, mipLevelHigh);
    }
    rgssColorLow *= 0.25;
    rgssColorHigh *= 0.25;

    vec4 rgssColor = mix(rgssColorLow, rgssColorHigh, mipBlend);

    vec4 nearestColor = sampleNearest(source, uv, pixelSize, du, dv, texelScreenSize);

    return mix(nearestColor, rgssColor, blendFactor);
}


vec4 terrainSample() {
    return UseRgss == 1 ? sampleRGSS(Sampler0, texCoord0, 1.0 / TextureSize) : sampleNearest(Sampler0, texCoord0, 1.0 / TextureSize);
}

#ifdef LARP_WATER

flat in int waterKind;
flat in vec2 waterFlow;
in vec3 waterTint;
in vec3 waterLight;
uniform sampler2D SceneColor;
uniform sampler2D SceneDepth;
vec3 positionAt(vec2 uv, float depth) {
    float z = WaterFlags.w > .5 ? depth : depth * 2.0 - 1.0;
    vec4 p = InverseViewProjection * vec4(uv * 2.0 - 1.0, z, 1);
    return p.xyz / max(abs(p.w), 1e-7) * sign(p.w);
}
vec3 reflectedWorld(vec3 origin, vec3 direction, out float confidence) {
    confidence = 0.0;
    vec3 result = vec3(0);
    float previous = .08;
    for (int step = 0; step < 32; step++) {
        float travel = .12 + float(step) * .22 + float(step * step) * .12;
        vec3 p = origin + direction * travel;
        vec4 clip = ViewProjection * vec4(p, 1);
        if (clip.w <= .01) break;
        vec3 ndc = clip.xyz / clip.w;
        vec2 uv = ndc.xy * .5 + .5;
        if (any(lessThan(uv, vec2(.001))) || any(greaterThan(uv, vec2(.999)))) break;
        float sceneDepth = texture(SceneDepth, uv).r;
        float rayDepth = WaterFlags.w > .5 ? ndc.z : ndc.z * .5 + .5;
        // Minecraft 1.21.11 uses standard OpenGL depth, cleared to one.
        if (sceneDepth < 1.0 && rayDepth > sceneDepth) {
            float low = previous, high = travel;
            for (int refine = 0; refine < 4; refine++) {
                float middle = (low + high) * .5;
                vec4 c = ViewProjection * vec4(origin + direction * middle, 1);
                vec3 n = c.xyz / c.w;
                vec2 s = n.xy * .5 + .5;
                float d = texture(SceneDepth, s).r;
                float r = WaterFlags.w > .5 ? n.z : n.z * .5 + .5;
                if (d < 1.0 && r > d) { high = middle; uv = s; sceneDepth = d; }
                else low = middle;
            }
            float separation = length(origin + direction * high) - length(positionAt(uv, sceneDepth));
            if (separation > clamp((travel - previous) * 1.6, .4, 4.0)) { previous = travel; continue; }
            vec2 edge = min(uv, 1.0 - uv);
            confidence = smoothstep(0.0, .08, min(edge.x, edge.y)) * (1.0 - smoothstep(70.0, 140.0, travel));
            result = texture(SceneColor, uv).rgb;
            break;
        }
        previous = travel;
    }
    return result;
}

vec2 waveSlope(vec2 p, float t) {
    return vec2(.38,.21)*(.5*cos(dot(p,vec2(.38,.21))+t*1.1))
        +vec2(-.27,.46)*(.3*cos(dot(p,vec2(-.27,.46))+t*1.4))
        +vec2(.71,-.53)*(.14*cos(dot(p,vec2(.71,-.53))+t*2.1))
        +vec2(-.93,-.88)*(.06*cos(dot(p,vec2(-.93,-.88))+t*2.9));
}
vec2 ripples(vec2 p, float t, vec2 drift) {
    vec2 moving = p - drift*t;
    float frequency = min(6.0,.35/max(length(fwidth(p)),.001));
    return vec2(sin(dot(moving,vec2(1.3,.8))*frequency+t*1.7),cos(dot(moving,vec2(-.7,1.5))*frequency-t*1.1))*.06;
}
float focusedLight(vec3 p, float t) {
    vec2 q = p.xz * 1.2 + waveSlope(p.xz,t)*.15;
    float a = abs(sin(q.x*3.7 + sin(q.y*2.9+t)*.8));
    float b = abs(sin(q.y*4.3 + cos(q.x*3.1-t*.8)*.8));
    return pow(1.0-min(a,b),9.0);
}
vec4 waterMaterial(vec4 vanilla) {
    if (Modes.z < .5 || Water.x <= 0.0) return vanilla;
    vec2 uv = gl_FragCoord.xy / vec2(textureSize(SceneColor,0));
    float distance = max(length(materialPosition),.001);
    vec3 view = materialPosition / distance;
    vec3 face = normalize(cross(dFdx(materialPosition),dFdy(materialPosition)));
    if (dot(face,view)>0.0) face=-face;
    bool surface = abs(face.y)>.7;
    bool below = surface && face.y<0.0;
    vec3 world = materialPosition+Camera.xyz;
    float detail = 1.0-smoothstep(24.0,96.0,distance);
    float seconds = WindTime.w;
    vec2 drift = waterKind==2 ? waterFlow*1.6 : vec2(.35,.2);
    vec3 normal = face;
    float churn = 0.0;
    if (surface) {
        vec2 tilt = waveSlope(world.xz,seconds)*(.07*Water.y*(1.0+Sky.a*.6));
        tilt += ripples(world.xz,seconds,drift)*detail;
        if (Sky.a>.05) tilt+=rainNormal(world.xz,seconds)*Sky.a*(1.0-smoothstep(16.0,40.0,distance));
        normal=normalize(vec3(-tilt.x,below ? -1.0 : 1.0,-tilt.y));
    } else {
        vec2 q=vec2(dot(world.xz,vec2(face.z,-face.x))*5.0,world.y*.7+seconds*(waterKind==3?3.5:1.2));
        float streak=noiseField(q)*.65+noiseField(q*2.2+3.0)*.35;
        normal=normalize(face+vec3(face.z,0,-face.x)*(noiseField(q)-.5)*.3*detail);
        churn=waterKind==3 ? .12+.38*smoothstep(.35,.85,streak) : .12*smoothstep(.6,.9,streak);
    }
    float depth = texture(SceneDepth,uv).r;
    vec3 bed=depth<1.0 ? positionAt(uv,depth) : view*1000.0;
    float thickness = min(max(length(bed)-distance,0.0),surface?10000.0:1.0);
    vec2 bend=(ViewProjection*vec4(normal.x,0,normal.z,0)).xy;
    vec2 refracted=clamp(uv+bend*.03*clamp(thickness*.5,0.0,1.0)/(1.0+distance*.04),vec2(.001),vec2(.999));
    float bentDepth=texture(SceneDepth,refracted).r;
    vec3 bentBed=bentDepth<1.0 ? positionAt(refracted,bentDepth) : view*1000.0;
    if (length(bentBed)<distance) { refracted=uv; bentBed=bed; }
    float bentThickness=min(max(length(bentBed)-distance,0.0),surface?10000.0:1.0);
    vec3 transmitted=texture(SceneColor,refracted).rgb;
    vec3 absorption=mix(vec3(.38,.085,.06),(1.0-waterTint)*.4+.03,.2);
    float light=max(max(waterLight.r,waterLight.g),waterLight.b);
    vec3 scatter=mix(vec3(.012,.04,.05),waterTint*.08,.2)*light*(.25+.75*Sun.w);
    vec3 transmission=exp(-absorption*(below||Modes.w>.5?min(bentThickness,4.0):bentThickness));
    vec3 refraction=transmitted*transmission+scatter*(1.0-transmission);
    float shallow=exp(-bentThickness*.3)*(1.0-exp(-bentThickness*2.5))*Sun.w*(1.0-Sky.a)*detail;
    if (WaterFlags.y>.5 && surface && !below) refraction+=vec3(1,.97,.9)*dot(transmitted,vec3(.3,.5,.2))*transmission*focusedLight(bentBed+Camera.xyz,seconds)*shallow*.55;
    vec3 direction=reflect(view,normal);
    float cosine=clamp(dot(normal,-view),0.0,1.0);
    float fresnel=.02+.98*pow(1.0-cosine,5.0);
    vec3 reflection=skyMirror(vec3(direction.x,abs(direction.y),direction.z),light,FogColor.rgb);
    if (below) {
        float exitAngle=1.769*(1.0-cosine*cosine);
        fresnel=exitAngle>=1.0?1.0:.02+.98*pow(1.0-sqrt(1.0-exitAngle),5.0);
        reflection=scatter*.9;
        refraction=transmitted;
    } else if (Water.z>.5 && detail>0.0 && fresnel>.04) {
        float confidence;
        vec3 hit=reflectedWorld(materialPosition+normal*.03,direction,confidence);
        reflection=mix(reflection,hit*.92,confidence);
    }
    vec3 result=mix(refraction,reflection,clamp(fresnel,0.0,1.0));
    if (!below) {
        float sun=max(dot(direction,normalize(Sun.xyz)),0.0);
        result+=vec3(1,.95,.85)*(pow(sun,700.0)*9.0+pow(sun,45.0)*.12)*Sun.w*light;
    }
    if (WaterFlags.x>.5) {
        if (surface && !below && depth<1.0) {
            float reach=waterKind==2?.25+.2*Water.y:.06;
            float edge=1.0-smoothstep(0.0,reach,thickness*abs(view.y));
            float broken=noiseField(world.xz*4.0-drift*seconds*.8)*.6+noiseField(world.xz*9.3+seconds*.4)*.4;
            churn=max(churn,smoothstep(.5,.85,broken*.6+edge*.5)*edge*detail*(waterKind==2?.7:.25));
        }
        result=mix(result,vec3(.86,.9,.92)*light*(.25+.75*Sun.w),churn);
        fresnel=max(fresnel,churn);
    }
    vec3 behind=texture(SceneColor,uv).rgb;
    result=mix(mix(behind,vanilla.rgb,vanilla.a),result,Water.x);
    float remaining=below?0.0:dot(exp(-absorption*min(bentThickness,8.0)),vec3(.33333))*(1.0-fresnel)*.9*Water.x+(1.0-vanilla.a)*(1.0-Water.x);
    float alpha=1.0-remaining;
    return vec4(clamp((result-behind*remaining)/max(alpha,.001),0.0,1.0),alpha);
}

#else

float snowMaterial(inout vec4 color) {
    vec3 face=normalize(cross(dFdx(materialPosition),dFdy(materialPosition)));
    if (dot(face,materialPosition)>0.0) face=-face;
    float footprint=max(length(dFdx(materialWorld.xz)),length(dFdy(materialWorld.xz)));
    return snowSurface(color,materialPosition,materialBasePosition,materialWorld,materialSkyLight,
        materialSnowCap,materialSnowEligible,materialLight,face,footprint);
}

float wetMaterial(inout vec4 color) {
    return wetSurface(color,materialPosition,materialWorld,materialSkyLight,vertexColor,FogColor.rgb,
        vec2(FogRenderDistanceStart,FogRenderDistanceEnd),cylindricalVertexDistance);
}

#endif

void main() {
    vec4 color=terrainSample()*vertexColor;
#ifdef ALPHA_CUTOUT
    if (color.a<ALPHA_CUTOUT) discard;
#endif
    float mirror=0.0;
#ifdef LARP_WATER
    if (waterKind==4) discard;
    if (waterKind!=0) color=waterMaterial(color);
#else
    mirror=wetMaterial(color);
    mirror*=1.0-snowMaterial(color);
#endif
    color=mix(FogColor*vec4(1,1,1,color.a),color,ChunkVisibility);
    fragColor=apply_fog(color,sphericalVertexDistance,cylindricalVertexDistance,FogEnvironmentalStart,FogEnvironmentalEnd,FogRenderDistanceStart,FogRenderDistanceEnd,FogColor);
#ifndef LARP_WATER
    if (Modes.y>.5 && mirror>.02) fragColor.a=.25+.2*(1.0-clamp(mirror,0.0,1.0));
#endif
}

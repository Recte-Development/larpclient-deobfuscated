#version 330
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
        // Minecraft uses reversed depth, cleared to zero.
        if (sceneDepth > 0.0 && rayDepth < sceneDepth) {
            float low = previous, high = travel;
            for (int refine = 0; refine < 4; refine++) {
                float middle = (low + high) * .5;
                vec4 c = ViewProjection * vec4(origin + direction * middle, 1);
                vec3 n = c.xyz / c.w;
                vec2 s = n.xy * .5 + .5;
                float d = texture(SceneDepth, s).r;
                float r = WaterFlags.w > .5 ? n.z : n.z * .5 + .5;
                if (d > 0.0 && r < d) { high = middle; uv = s; sceneDepth = d; }
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

uniform sampler2D DropletMask;
uniform sampler2D DropletDepth;
in vec2 texCoord;
out vec4 fragColor;
float smoothSurface(vec2 uv, float reference) {
    vec2 texel=1.0/vec2(textureSize(DropletDepth,0));
    float sum=0.0, weight=0.0;
    for (int y=-3; y<=3; y++) for (int x=-3; x<=3; x++) {
        vec2 sampleUv=uv+vec2(x,y)*texel;
        if (texture(DropletMask,sampleUv).a<.5) continue;
        float distance=length(positionAt(sampleUv,texture(DropletDepth,sampleUv).r));
        float spatial=exp(-float(x*x+y*y)*.16);
        float range=exp(-abs(distance-reference)*22.0);
        float w=spatial*range;
        sum+=distance*w; weight+=w;
    }
    return weight>0.0?sum/weight:reference;
}
void main() {
    vec2 uv=texCoord;
    vec3 scene=texture(SceneColor,uv).rgb;
    if (texture(DropletMask,uv).a<.5) { fragColor=vec4(scene,1); return; }
    float depth=texture(DropletDepth,uv).r;
    vec3 center=positionAt(uv,depth);
    float distance=smoothSurface(uv,length(center));
    vec3 position=normalize(center)*distance;
    vec3 normal=normalize(cross(dFdx(position),dFdy(position)));
    if (dot(normal,position)>0.0) normal=-normal;
    // Silhouettes use the analytic sphere normal when derivatives cross uncovered background.
    if (length(fwidth(distance))>.25) normal=normalize(texture(DropletMask,uv).rgb*2.0-1.0);
    vec3 view=normalize(position);
    vec3 reflected=reflect(view,normal);
    float fresnel=.02+.98*pow(1.0-clamp(dot(normal,-view),0.0,1.0),5.0);
    vec3 sky=mix(vec3(.12,.17,.2),Sky.rgb,smoothstep(0.0,.5,reflected.y));
    sky=mix(sky,vec3(.65,.7,.75),Sky.a*.8);
    if (Water.z>.5) {
        float confidence;
        vec3 hit=reflectedWorld(position+normal*.02,reflected,confidence);
        sky=mix(sky,hit*.92,confidence);
    }
    vec2 bend=(ViewProjection*vec4(normal,0)).xy;
    vec2 refracted=clamp(uv+bend*.012/(1.0+distance*.04),vec2(.001),vec2(.999));
    if (length(positionAt(refracted,texture(SceneDepth,refracted).r))<distance) refracted=uv;
    vec3 below=texture(SceneColor,refracted).rgb;
    vec3 transmission=exp(-vec3(.38,.085,.06)*.22);
    vec3 result=mix(below*transmission+vec3(.012,.04,.05)*(1.0-transmission),sky,fresnel);
    float sun=max(dot(reflected,normalize(Sun.xyz)),0.0);
    result+=vec3(1,.95,.85)*(pow(sun,700.0)*9.0+pow(sun,45.0)*.12)*Sun.w;
    fragColor=vec4(mix(scene,result,Water.x),1);
}

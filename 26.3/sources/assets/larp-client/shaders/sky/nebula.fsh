#version 330
layout(std140) uniform SkyPalette {
    vec4 Zenith;
    vec4 Horizon;
    vec4 Parameters;
    vec4 Nebula; // exposure, saturation, cloud intensity, star density
    vec4 Motion; // integrated drift seconds, integrated rotation radians
    vec4 Flow; // visible flow enabled, deformation strength, pattern scale
    vec4 CloudTint;
};
uniform sampler2D NebulaClouds;
in vec3 skyDirection;
out vec4 fragColor;
vec2 mirrored(vec2 uv) { return 1.0 - abs(mod(uv, 2.0) - 1.0); }
// Continuous direction-space triplanar projection: no longitude seam or polar singularity.
vec3 clouds(vec3 d, vec3 offset, float scale) {
    vec3 w = pow(abs(d), vec3(4.0)); w /= max(dot(w, vec3(1.0)), .0001);
    vec3 p = d * scale + offset;
    return texture(NebulaClouds, mirrored(p.yz)).rgb * w.x
         + texture(NebulaClouds, mirrored(p.zx)).rgb * w.y
         + texture(NebulaClouds, mirrored(p.xy)).rgb * w.z;
}
float hash(vec3 p) { p=fract(p*.3183099+vec3(.1,.2,.3)); p*=17.0; return fract(p.x*p.y*p.z*(p.x+p.y+p.z)); }
void main() {
    vec3 d = normalize(skyDirection);
    float a = Motion.y, cs = cos(a), sn = sin(a);
    vec3 q = vec3(cs*d.x-sn*d.z, d.y, sn*d.x+cs*d.z);
    float t = Motion.x * mix(.000025, .012, Flow.x);
    // Smooth bounded deformation in direction space. All motion uses integrated time,
    // so a zero speed freezes both drift and deformation, including at the poles.
    vec3 warp = sin(q.yzx * 4.0 + vec3(t*3.0, -t*2.0, t*2.5))
              * cos(q.zxy * 3.0 - t*1.7) * Flow.y * Flow.x * .16;
    vec3 mainCloud = clouds(q + warp, vec3(.53,.27,.69) + vec3(t, -t*.4, t*.6), .72*Flow.z);
    vec3 detail = clouds(q.yzx - warp*.7, vec3(.19,.71,.38) + vec3(-t*.32,t*.6,-t*.2), 1.13*Flow.z);
    // Keep voids dark; a second, weaker layer adds luminous fine structure.
    vec3 cloud = max(mainCloud - vec3(.012,.007,.024), vec3(0.0));
    cloud += detail * smoothstep(.14,.6,max(detail.r,detail.b)) * .22;
    float luminance = dot(cloud, vec3(.2126,.7152,.0722));
    cloud = mix(vec3(luminance), cloud, Nebula.y);
    vec3 color = Zenith.rgb + max(cloud,vec3(0.0)) * CloudTint.rgb * Nebula.z * Nebula.x;
    vec3 cell = floor(q * 185.0), f = fract(q * 185.0) - .5;
    float r = length(f);
    float aa = max(fwidth(r), .018);
    float star = (1.0 - smoothstep(.075-aa,.075+aa,r)) * step(1.0-Nebula.w*.025, hash(cell));
    vec3 starColor = mix(vec3(.83,.79,1.0), vec3(1.0), hash(cell + 7.0));
    color = mix(Horizon.rgb, color, smoothstep(-.22,.12,d.y));
    color += starColor * star * Parameters.z * Nebula.x * smoothstep(-.12,.2,d.y);
    fragColor = vec4(clamp(color, 0.0, 1.0), 1.0);
}

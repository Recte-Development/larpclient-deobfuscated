layout(std140) uniform SkyPalette {
    vec4 Zenith;
    vec4 Horizon;
    vec4 Motion;     // integrated time, rotation, exposure, horizon blend
    vec4 Stars;      // brightness, density, twinkle, quality 0..3
    vec4 Primary;
    vec4 Secondary;
    vec4 Effect;
    vec4 Placement;
    vec4 Reserved0;
    vec4 Reserved1;
};
in vec3 skyDirection;
out vec4 fragColor;
const float PI = 3.14159265359;
float hash31(vec3 p) {
    p = fract(p * .1031);
    p += dot(p, p.yzx + 33.33);
    return fract((p.x + p.y) * p.z);
}
float skyNoise(vec3 p) {
    vec3 i = floor(p), f = fract(p); f = f*f*(3.0-2.0*f);
    return mix(mix(mix(hash31(i), hash31(i+vec3(1,0,0)), f.x),
                   mix(hash31(i+vec3(0,1,0)), hash31(i+vec3(1,1,0)), f.x), f.y),
               mix(mix(hash31(i+vec3(0,0,1)), hash31(i+vec3(1,0,1)), f.x),
                   mix(hash31(i+vec3(0,1,1)), hash31(i+vec3(1,1,1)), f.x), f.y), f.z);
}
float fbm(vec3 p) {
    float sum=0.0, amplitude=.53;
    for (int i=0; i<6; i++) {
        if (i >= 3 + int(Stars.w)) break;
        sum += amplitude * skyNoise(p);
        p = p.yzx * 2.03 + vec3(7.1,3.7,11.3);
        amplitude *= .48;
    }
    return sum;
}
vec3 direction() {
    vec3 d = normalize(skyDirection);
    float c = cos(Motion.y), s = sin(Motion.y);
    return vec3(c*d.x-s*d.z, d.y, s*d.x+c*d.z);
}
// Jittered star positions stay inside their cells. Three overlapping directional charts
// remove polar/face seams, while different scales and colors hide the underlying lattice.
vec3 starLayer(vec2 uv, float seed, float scale, float probability, float radius, float footprint) {
    vec2 p = uv*scale, cell = floor(p);
    float h = hash31(vec3(cell,seed));
    vec2 center = .22 + .56*vec2(hash31(vec3(cell,seed+9.0)), hash31(vec3(cell,seed+19.0)));
    float r = length(fract(p)-center);
    float aa = max(footprint*scale, .012);
    float core = 1.0-smoothstep(max(0.0,radius-aa),radius+aa,r);
    core *= min(1.0, radius*radius / (aa*aa));
    float halo = exp(-r*r / (radius*radius*5.0)) * .09;
    float present = step(1.0-probability*Stars.y,h);
    float twinkle = 1.0 + Stars.z*sin(Motion.x*.45+h*93.0);
    vec3 tint = mix(vec3(.62,.76,1.0), vec3(1.0,.87,.68), hash31(vec3(cell,seed+31.0)));
    return tint * (core+halo) * present * twinkle;
}
vec3 starChart(vec2 uv, float seed, float footprint) {
    return starLayer(uv,seed,190.0,.38,.045,footprint)*.65
         + starLayer(uv,seed+37.0,85.0,.22,.065,footprint)*1.05
         + starLayer(uv,seed+73.0,34.0,.09,.085,footprint)*1.8;
}
vec3 starfield(vec3 d) {
    if (Stars.x <= 0.0 || Stars.y <= 0.0) return vec3(0);
    vec3 w = pow(abs(d),vec3(10.0)); w /= max(dot(w,vec3(1)),.0001);
    vec2 ux=d.yz/max(abs(d.x),.25), uy=d.zx/max(abs(d.y),.25), uz=d.xy/max(abs(d.z),.25);
    // Derivatives must be evaluated before divergent chart branches. Charts with less than
    // 0.1% contribution are imperceptible, but used to execute three full star layers anyway.
    float ax=length(fwidth(ux))*.55, ay=length(fwidth(uy))*.55, az=length(fwidth(uz))*.55;
    vec3 color=vec3(0);
    if (w.x>.001) color+=w.x*starChart(ux,11.0+sign(d.x),ax);
    if (w.y>.001) color+=w.y*starChart(uy,53.0+sign(d.y),ay);
    if (w.z>.001) color+=w.z*starChart(uz,97.0+sign(d.z),az);
    return Stars.x*color;
}
vec3 deepSpace(vec3 d, float dust) {
    vec3 axis = normalize(vec3(.24,.93,.28));
    float band = exp(-pow(dot(d,axis)*4.6,2.0));
    float cloud = fbm(d*5.3+vec3(.2,Motion.x*.0005,.7));
    float filaments = smoothstep(.30,.72,cloud) * band;
    vec3 color = Zenith.rgb + vec3(.005,.007,.015)*skyNoise(d*2.5);
    color += mix(vec3(.035,.047,.085),vec3(.12,.07,.12),cloud)*filaments*dust;
    color *= 1.0 - dust*.4*band*smoothstep(.42,.7,skyNoise(d*17.0));
    return color + starfield(d)*(1.0-.28*filaments*dust);
}
void finishSky(vec3 color, vec3 d) {
    color *= Motion.z;
    float fade = (1.0-smoothstep(-.18,.16,d.y))*.85*Motion.w;
    color = mix(color,Horizon.rgb*Motion.z,fade);
    // Soft shoulder leaves dim stars intact and prevents glaring clipped highlights.
    color = color / (vec3(1.0)+max(color-vec3(.65),vec3(0.0)));
    fragColor = vec4(clamp(color,0.0,1.0),1.0);
}

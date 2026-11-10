// Shared terrain noise, sky reflection and Wet Ground surface for the vanilla and Sodium
// terrain materials. Import after the LarpMaterial block.
float randomCell(vec2 cell) {
    return fract(sin(dot(cell, vec2(127.1, 311.7))) * 43758.5453);
}
float noiseField(vec2 p) {
    vec2 cell = floor(p), q = fract(p);
    q = q * q * (3.0 - 2.0 * q);
    return mix(mix(randomCell(cell), randomCell(cell + vec2(1, 0)), q.x),
               mix(randomCell(cell + vec2(0, 1)), randomCell(cell + vec2(1, 1)), q.x), q.y);
}
float pools(vec2 p) {
    return noiseField(p) * .55 + noiseField(p * 2.13 + 7.1) * .3 + noiseField(p * 4.7 + 3.3) * .15;
}
vec2 rainNormal(vec2 p, float seconds) {
    vec2 result = vec2(0);
    for (int layer = 0; layer < 2; layer++) {
        vec2 q = p * (layer == 0 ? 2.2 : 3.4) + float(layer) * 17.3;
        vec2 cell = floor(q);
        for (int y = -1; y <= 1; y++) for (int x = -1; x <= 1; x++) {
            vec2 id = cell + vec2(x, y);
            vec2 seed = vec2(randomCell(id), randomCell(id + 19.31));
            float age = fract(seconds * (.75 + seed.y * .3) + seed.x * 7.13);
            vec2 offset = q - id - seed;
            float radius = length(offset);
            float envelope = (radius - age * .85) * 16.0;
            result += offset / max(radius, .001) * sin(envelope * 3.141593)
                * exp(-envelope * envelope * .5) * (1.0-age) * (1.0-age);
        }
    }
    return result * .22;
}
vec3 skyMirror(vec3 direction, float localLight, vec3 fogColor) {
    vec3 clearSky = mix(fogColor, Sky.rgb, smoothstep(0.0,.5,direction.y));
    vec3 cloud = mix(fogColor,vec3(.78,.8,.84),.55);
    return mix(clearSky * localLight, cloud * max(.5,localLight), Sky.a);
}

// position is camera-relative, world is absolute, skyLight is 0-1 and vertexLight is the
// lightmapped vertex color. Returns the weight that marks the pixel for wet_reflection.
float wetSurface(inout vec4 color, vec3 position, vec3 world, float skyLight, vec4 vertexLight,
                 vec3 fogColor, vec2 renderFog, float cylindricalDistance) {
    if (Modes.x < .5) return 0.0;
    float soak=Camera.w; // Wet Ground wetness, 0 keeps the original damp look
    vec3 normal=normalize(cross(dFdx(position),dFdy(position)));
    if (dot(normal,position)>0.0) normal=-normal;
    float distance=length(position);
    float nearby=Wet.w>.5 ? 1.0-smoothstep(renderFog.y*.8,renderFog.y,distance) : 1.0-smoothstep(56.0,110.0,distance);
    float damp=smoothstep(.9,.99,normal.y)*smoothstep(.945,.99,skyLight)*nearby;
    if (damp<.001) return 0.0;
    float pool=damp;
    if (Wet.w<.5) {
        float cover=min(1.0,Wet.x+soak*.18);
        pool*=smoothstep(1.02-cover,1.1+soak*.06-cover,pools(world.xz*.17)+.06);
    }
    float luminance=dot(color.rgb,vec3(.299,.587,.114));
    vec3 base=mix(color.rgb,mix(vec3(luminance),color.rgb,1.15),damp*(.5+.35*soak));
    base*=mix(1.0,.8,min(1.0,damp*(.35+.4*Sky.a+.45*soak)))*mix(1.0,Wet.w>.5?mix(.6,.46,soak):mix(.78,.56,soak),pool);
    vec2 tilt=Wet.z>0.0 ? rainNormal(world.xz,WindTime.w)*Wet.z*(1.0-smoothstep(18.0,40.0,distance))*(1.0+Sky.a) : vec2(0);
    vec3 n=normalize(vec3(tilt.x,1,tilt.y));
    vec3 direction=reflect(normalize(position),n);
    float reflectance=mix(.03,.2,soak);
    float fresnel=reflectance+(1.0-reflectance)*pow(1.0-max(dot(n,normalize(-position)),0.0),5.0);
    float light=max(max(vertexLight.r,vertexLight.g),vertexLight.b);
    vec3 reflected=skyMirror(direction,light,fogColor);
    float amount=(pool*mix(.9,1.0,soak)+(damp-pool)*mix(.2,.5,soak))*Wet.y;
    float sun=max(dot(direction,normalize(Sun.xyz)),0.0);
    vec3 sheen=reflected*fresnel*amount*(.55+1.3*Sky.a+.5*soak)+reflected*pool*(Sky.a*.32+soak*.22)*Wet.y;
    vec3 glint=vec3(1,.97,.9)*(pow(sun,220.0)*4.8+pow(sun,18.0)*.25)*Sun.w*amount*light;
    color.rgb=base*(1.0-.12*pool)+sheen+glint;
    return (pool+(damp-pool)*mix(.12,.45,soak))*(1.0-smoothstep(renderFog.x,renderFog.y,cylindricalDistance));
}

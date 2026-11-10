#version 330
uniform sampler2D Sampler0;
in vec2 texCoord0;
in vec4 vertexColor;
in float hue;
in float surfaceLight;
out vec4 fragColor;
#if defined(LIQUID_GLASS) || defined(CHAMS_STYLE)
in vec3 viewPosition;
in vec3 viewNormal;
#endif
#ifdef LIQUID_GLASS
in vec3 environmentColor;
in vec2 faceCoord;
#endif
#ifdef CHAMS_STYLE
in vec3 secondaryColor;
in float shading;
in float patternScale;
float cell(vec2 p){return fract(sin(dot(p,vec2(127.1,311.7)))*43758.5453);}
float valueNoise(vec2 p){
    vec2 i=floor(p),f=fract(p);
    f=f*f*(3.0-2.0*f);
    return mix(mix(cell(i),cell(i+vec2(1,0)),f.x),mix(cell(i+vec2(0,1)),cell(i+vec2(1,1)),f.x),f.y);
}
float fbm(vec2 p){
    float sum=0.0,amplitude=.5;
    for(int octave=0;octave<4;octave++){sum+=valueNoise(p)*amplitude;p=p*2.03+vec2(17.1,9.7);amplitude*=.5;}
    return sum/.9375;
}
#endif
vec3 rainbow(float h){return clamp(abs(fract(h+vec3(0,2.0/3.0,1.0/3.0))*6.0-3.0)-1.0,0.0,1.0);}
void main(){
#ifdef CHAMS
    // Standard OpenGL depth: move the overlay a few representable depth values toward the camera.
    // Both passes use the same split, so coplanar native skin cannot flicker between colors.
    uint depthBits=floatBitsToUint(gl_FragCoord.z);
    gl_FragDepth=depthBits>8u?uintBitsToFloat(depthBits-8u):0.0;
#endif
#ifdef DEPTH_BIAS
    // Reversed depth falls off as 1/distance, so a relative lift is a constant fraction of range
    // on float and fixed-point depth alike: about a millimetre at third-person distance.
    gl_FragDepth=min(1.0,gl_FragCoord.z*1.0003);
#endif
    vec4 skin=texture(Sampler0,texCoord0);
    if(skin.a<.1)discard;
#ifdef LIQUID_GLASS
    vec3 n = normalize(viewNormal);
    vec3 v = normalize(-viewPosition);
    float flow = sin(texCoord0.y * 50.0 + hue) * cos(texCoord0.x * 46.0 - hue * 2.0);
    // Tangent-space microfacets disturb reflection, without deforming the native silhouette.
    vec3 tangent = normalize(abs(n.y) > .9 ? cross(n, vec3(1, 0, 0)) : cross(n, vec3(0, 1, 0)));
    vec3 bitangent = cross(n, tangent);
    n = normalize(n + tangent * flow * .065 + bitangent * sin(texCoord0.x * 31.0 + texCoord0.y * 23.0 + hue * 2.0) * .045);
    float fresnel = pow(1.0 - abs(dot(n, v)), 3.5);
    float gloss = vertexColor.r, edge = vertexColor.g, iridescence = vertexColor.b;
    vec3 reflection = reflect(-v, n);
    vec3 skyTint = max(environmentColor, vec3(.16, .22, .3));
    float skyWeight = smoothstep(-.6, .55, reflection.y);
    vec3 environment = mix(vec3(.025, .04, .06), skyTint * .8 + vec3(.12, .17, .21), skyWeight);
    float horizon = exp(-reflection.y * reflection.y * 50.0);
    environment += skyTint * horizon * .2;
    float key = pow(max(0.0, dot(reflection, normalize(vec3(-.35, .8, .45)))), mix(8.0, 150.0, gloss));
    float strip = pow(max(0.0, 1.0 - abs(reflection.x * .65 + reflection.y * .35 - .12) * 2.5), mix(5.0, 35.0, gloss));
    vec3 interference = .5 + .5 * cos(vec3(0, 2.1, 4.2) + fresnel * 9.0 + flow * .3);
    float edgeDistance = min(min(faceCoord.x, 1.0 - faceCoord.x), min(faceCoord.y, 1.0 - faceCoord.y));
    float edgeWidth = max(fwidth(faceCoord.x), fwidth(faceCoord.y)) * 1.15;
    float faceRim = 1.0 - smoothstep(0.0, max(.0001, edgeWidth), edgeDistance);
    vec3 highlight = vec3(.78, .9, 1.0) * (fresnel * edge * 1.1 + key * (.3 + gloss) + strip * (.16 + .38 * gloss));
    highlight += vec3(.72, .86, 1.0) * faceRim * edge * (.22 + .32 * fresnel);
#ifdef GLASS_MASK
    fragColor = vec4(clamp(n.xy * .35 + .5, 0.0, 1.0), gl_FragCoord.z, skin.a * vertexColor.a * (.35 + .65 * fresnel));
#elif defined(GLASS_GLOW)
    fragColor = vec4(highlight + interference * iridescence * fresnel * .15, skin.a * vertexColor.a * fresnel * edge * .45);
#else
    vec3 material = skin.rgb * .10 + environment * (.72 + .24 * gloss) + highlight;
    material += interference * iridescence * (.04 + .13 * fresnel);
    fragColor = vec4(material, skin.a * vertexColor.a * (.74 + .26 * fresnel));
#endif
#elif defined(CHAMS_STYLE)
    vec3 primary=vertexColor.rgb;
    vec3 n=normalize(viewNormal), v=normalize(-viewPosition);
    float facing=max(dot(n,v),0.0);
    float lit=mix(1.0,clamp((surfaceLight-.58)*1.6+.42,.3,1.12),shading);
    float detail=dot(skin.rgb,vec3(.2126,.7152,.0722));
    // Skin pixels give patterns the same density on skins and armor regardless of texture size.
    vec2 p=texCoord0*vec2(textureSize(Sampler0,0))*patternScale/8.0;
    float angle=hue*6.2831853;
    vec2 drift=vec2(cos(angle),sin(angle))*.6;
    float alpha=skin.a*vertexColor.a;
#if defined(STYLE_SOLID)
    vec3 c=primary*lit;
#elif defined(STYLE_GLOW)
    vec3 c=mix(primary,vec3(1),.12+.3*pow(1.0-facing,2.0))*1.08;
#elif defined(STYLE_METALLIC)
    // Hammered metal: skin detail tilts each pixel's normal, so features catch the light.
    vec3 tangent=normalize(abs(n.y)>.9?cross(n,vec3(1,0,0)):cross(n,vec3(0,1,0)));
    n=normalize(n+(tangent*(detail-.5)+cross(n,tangent)*(fract(detail*7.0)-.5)*.5)*.35);
    vec3 r=reflect(-v,n);
    float sky=smoothstep(-.4,.65,r.y);
    vec3 environment=mix(secondaryColor*.45+primary*.08,mix(primary,vec3(1),.25),sky);
    environment+=mix(primary,vec3(1),.5)*exp(-r.y*r.y*24.0)*.55;
    float fresnel=pow(1.0-max(dot(n,v),0.0),4.0);
    float key=pow(max(dot(r,normalize(vec3(-.35,.8,.45))),0.0),70.0);
    float fill=pow(max(dot(r,normalize(vec3(.7,.15,-.6))),0.0),12.0);
    vec3 c=environment*mix(primary*1.15,vec3(1),fresnel*.6)+mix(primary,vec3(1),.6)*(key*1.6+fill*.35);
    c*=mix(1.0,lit*1.05,shading*.5);
#elif defined(STYLE_CAMO)
    float field=fbm(p*.55+drift);
    float second=fbm(p*.9+vec2(13.1,4.7)-drift*.7);
    float aa=fwidth(field)*1.5+.015;
    vec3 light=mix(primary,vec3(1),.45);
    vec3 c=mix(secondaryColor,primary,smoothstep(.44-aa,.44+aa,field));
    c=mix(c,light,smoothstep(.6-aa,.6+aa,second)*.85);
    c*=lit*(.92+.16*detail);
#elif defined(STYLE_MARBLE)
    float warp=fbm(p*.45+drift);
    float vein=abs(sin((p.x*.55+p.y*.35+warp*5.5)*3.1415927));
    float thin=abs(sin((p.x*.3-p.y*.8+warp*9.0+1.7)*3.1415927));
    float width=fwidth(vein)+.03;
    float lines=(1.0-smoothstep(0.0,.1+width,vein))*.95+(1.0-smoothstep(0.0,.04+width,thin))*.55;
    vec3 stone=mix(primary*.82,mix(primary,vec3(1),.3),fbm(p*1.3-drift));
    vec3 c=mix(stone,secondaryColor,clamp(lines,0.0,1.0));
    float gloss=pow(max(dot(reflect(-v,n),normalize(vec3(-.35,.8,.45))),0.0),40.0);
    c=c*lit+vec3(gloss*.35);
#elif defined(STYLE_GLITTER)
    vec2 grain=p*6.0;
    vec2 id=floor(grain);
    float seed=cell(id);
    // Whole-number multiples of the loop keep the twinkle continuous at the phase wrap.
    float twinkle=pow(.5+.5*sin(angle*8.0+seed*40.0),8.0);
    float spark=step(.62,cell(id+3.7))*twinkle;
    float visible=1.0-smoothstep(.3,.9,max(length(dFdx(grain)),length(dFdy(grain))));
    vec3 c=mix(secondaryColor,primary,.35+.65*cell(id+9.1))*lit*(.85+.3*detail);
    c+=mix(primary,vec3(1),.65)*spark*visible*(.6+.9*pow(1.0-facing,1.5)+.6*facing);
#else
    // STYLE_GHOST: a faint core with bright grazing edges, so overlapping parts stay readable.
    float rim=pow(1.0-facing,2.2);
    vec3 c=mix(primary*.55,mix(primary,vec3(1),.4),rim)*(.85+.3*detail);
    alpha*=.16+.74*rim;
#endif
    fragColor=vec4(c,alpha);
#elif defined(DISSOLVE)
    vec2 cell=floor(texCoord0*512.0);
    float noise=fract(sin(dot(cell,vec2(12.9898,78.233)))*43758.5453);
    if(hue>=1.0||noise<hue)discard;
    float edge=1.0-smoothstep(0.0,.12,noise-hue);
    vec3 ember=mix(vertexColor.rgb,vec3(1.0,.93,.72),edge*edge);
#ifdef DISSOLVE_GLOW
    if(edge<.1)discard;
    fragColor=vec4(ember,skin.a*vertexColor.a*edge*.65);
#else
    fragColor=vec4(mix(skin.rgb*surfaceLight,ember*1.35,edge),skin.a*vertexColor.a);
#endif
#elif defined(RAINBOW)
    vec3 c=mix(vec3(1),rainbow(hue),vertexColor.r)*vertexColor.g;
    fragColor=vec4(c*mix(.3,1.0,sqrt(dot(skin.rgb,vec3(.2126,.7152,.0722))))*clamp((surfaceLight-.58)*1.6+.36,.34,1.0),skin.a*vertexColor.a);
#elif defined(CHAMS_SURFACE)
    // Resolve one nearest cutout surface, retaining only the entity's range fade.
    fragColor=vertexColor;
#elif defined(SILHOUETTE)
    fragColor=vec4(vertexColor.rgb,skin.a*vertexColor.a);
#elif defined(HOLOGRAM)
    float stripe=.72+.28*sin(gl_FragCoord.y*1.6);
    fragColor=vec4(mix(skin.rgb,vec3(1),.75)*vertexColor.rgb,skin.a*vertexColor.a*stripe);
#else
    fragColor=vec4(skin.rgb*vertexColor.rgb*surfaceLight,skin.a*vertexColor.a);
#endif
}

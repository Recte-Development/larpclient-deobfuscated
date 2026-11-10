// Shared, world-anchored snow field. Snow: coverage, relief, sparkle, detail.
float snowRandom(vec2 cell) {
    return fract(sin(dot(cell,vec2(127.1,311.7)))*43758.5453);
}
float snowNoise(vec2 p) {
    vec2 cell=floor(p), q=fract(p); q=q*q*(3.0-2.0*q);
    return mix(mix(snowRandom(cell),snowRandom(cell+vec2(1,0)),q.x),
               mix(snowRandom(cell+vec2(0,1)),snowRandom(cell+vec2(1,1)),q.x),q.y);
}
float snowDrift(vec2 p) {
    return snowNoise(p*vec2(.22,.47))*.65+snowNoise(p*.91+7.3)*.35;
}
float snowCoverage(vec2 p) {
    if (Snow.x<=0.0) return 0.0;
    return smoothstep(1.0-Snow.x-.08,1.0-Snow.x+.08,snowDrift(p)+.09);
}
// Camera-relative original terrain position, with a narrow height band so roofs stay snowy.
float snowTrack(vec3 p, float padding) {
    if (SnowTrailInfo.x < .5 || p.x < SnowTrailBounds.x || p.z < SnowTrailBounds.y
            || p.x > SnowTrailBounds.z || p.z > SnowTrailBounds.w) return 0.0;
    float cleared = 0.0;
    for (int i = 0; i < int(SnowTrailInfo.x); i++) {
        vec4 track = SnowTracks[i];
        float height = 1.0 - smoothstep(.06,.18,abs(p.y-track.y));
        if (height <= 0.0) continue;
        vec2 offset = p.xz-track.xz;
        float radius = .64+padding;
        if (dot(offset,offset) >= radius*radius) continue;
        float distance = length(offset);
        cleared = max(cleared,(1.0-smoothstep(.44+padding,.64+padding,distance))*height*track.w);
        if (cleared >= .999) break;
    }
    return cleared;
}
float snowRelief(vec2 p) {
    // Real subdivided terrain supports a deep rolling blanket with smaller wind-packed mounds.
    float powder=snowNoise(p*2.35+vec2(13.4,5.8));
    return Snow.y*(.09+.34*snowDrift(p)+.12*powder)*snowCoverage(p);
}
float snowVertexTrack(vec3 p) { return snowTrack(p,.14); }
float snowDisplacement(vec3 world,vec3 relative,float exposure,bool movable) {
    return movable?snowRelief(world.xz)*exposure*(1.0-snowVertexTrack(relative)):0.0;
}

// Powder surface shared by the vanilla and Sodium terrain materials. position is camera-relative
// and displaced, base is its undisplaced form for tracks, world is absolute and light is the
// lightmap color. The fragment caller supplies the camera-facing face normal and footprint, the
// larger screen derivative of world.xz, so this include stays valid in vertex stages.
float snowSurface(inout vec4 color,vec3 position,vec3 base,vec3 world,float skyLight,float cap,
                  float eligible,vec3 light,vec3 face,float footprint) {
    if (Snow.x<=0.0 || eligible<.5) return 0.0;
    float exposed=smoothstep(.92,.99,skyLight);
    float top=smoothstep(.58,.93,face.y);
    float lip=smoothstep(.82,.99,cap)*smoothstep(-.6,-.2,face.y);
    float tracks=snowTrack(base,0.0);
    float amount=max(top,lip*.86)*exposed*snowCoverage(world.xz)*(1.0-tracks);
    if (amount<.001) return 0.0;
    float distance=length(position);
    vec2 p=world.xz;
    float drift=snowDrift(p);
    vec2 slope=vec2(0);
    float detail=1.0-smoothstep(18.0,65.0,distance);
    if (Snow.w>.5) {
        slope=vec2(snowRelief(p+vec2(.06,0))-snowRelief(p-vec2(.06,0)),
            snowRelief(p+vec2(0,.06))-snowRelief(p-vec2(0,.06)))/.12;
        float ripple=cos(dot(p,vec2(1.4,3.2))+snowNoise(p*.4)*3.0);
        slope+=vec2(1.4,3.2)*ripple*.009*Snow.y*detail;
    }
    vec3 normal=normalize(mix(face,normalize(vec3(-slope.x,1,-slope.y)),top));
    vec3 sun=normalize(Sun.xyz), view=normalize(-position);
    float diffuse=max(dot(normal,sun),0.0)*Sun.w;
    float crease=mix(.70,1.0,smoothstep(.16,.8,drift));
    if (Snow.w>.5 && detail>0.0) {
        vec2 powder=p*18.0;
        float resolved=1.0-smoothstep(.25,1.0,footprint*18.0);
        crease*=1.0+(snowNoise(powder)-.5)*.12*resolved*detail;
    }
    vec3 shade=mix(vec3(.57,.67,.82),vec3(.96,.975,1.0),.42+.58*diffuse);
    crease += tracks*(1.0-tracks)*.24; // Soft raised-looking powder rim around the displaced trail.
    vec3 snow=shade*light*crease;
    // Broad forward scattering gives powder a soft rim without turning it into a mirror.
    snow+=vec3(.09,.10,.12)*pow(max(dot(-view,sun),0.0),4.0)*Sun.w*top*light;
    if (Snow.w>1.5 && Snow.z>0.0 && detail>0.0) {
        vec2 grain=p*72.0;
        float visibility=1.0-smoothstep(.18,.75,footprint*72.0);
        float crystal=smoothstep(.973,.998,snowRandom(floor(grain)));
        float glint=pow(max(dot(normal,normalize(sun+view)),0.0),28.0);
        snow+=vec3(1.0,.96,.86)*crystal*glint*visibility*detail*Snow.z*Sun.w*1.1*light;
    }
    color.rgb=mix(color.rgb,snow,amount);
    return amount;
}

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
    return Snow.y*(.035+.10*snowDrift(p))*snowCoverage(p);
}

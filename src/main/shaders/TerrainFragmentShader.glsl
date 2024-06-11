#version 400

in vec2 fTexCords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

// Fog
in float visibility;

out vec4 outColor;


//Fog
uniform vec4 skyColor;

// terrain
uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColor;

uniform float shineDamper;
uniform float reflectivity;

void main(){
    // terrain
    vec4 blendMapColor = texture(blendMap, fTexCords);
    float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
    vec2 tiledCords = fTexCords * 40;

    vec4 backgroundTextureColor = texture(backgroundTexture, tiledCords) * backTextureAmount;
    vec4 rTextureColor = texture(rTexture, tiledCords) * blendMapColor.r;
    vec4 bTextureColor = texture(gTexture, tiledCords) * blendMapColor.g;
    vec4 gTextureColor = texture(bTexture, tiledCords) * blendMapColor.b;
    vec4 totalColor = backgroundTextureColor + rTextureColor + bTextureColor + gTextureColor;


    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    float nDot = dot(unitNormal, unitLightVector);
    float brightness = max(nDot, 0.8f);

    vec3 diffuse = brightness * lightColor;

    vec3 unitVectorToCamera = normalize(toCameraVector);
    vec3 lightDirecton = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirecton, unitNormal);

    float specularFactor = dot(reflectedLightDirection, unitNormal);
    specularFactor = max(specularFactor, 0);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity *  lightColor;

    outColor = vec4(diffuse, 0) * totalColor + vec4(finalSpecular, 1);

    //Fog
    outColor = mix(skyColor, outColor, visibility);
}



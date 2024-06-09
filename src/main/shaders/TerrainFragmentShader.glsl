#version 330 core // 400

in vec2 fTexCords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 outColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;

uniform float shineDamper;
uniform float reflectivity;

void main(){
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

    outColor = vec4(diffuse, 0) * texture(textureSampler, fTexCords) + vec4(finalSpecular, 1);
}



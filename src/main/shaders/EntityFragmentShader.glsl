#version 400

in vec2 fTexCords;
in vec3 fNormals;
in vec3 toLightVector;
in vec3 toCameraVector;

//Fog
in float visibility;

out vec4 outColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;

uniform float shineDamper;
uniform float reflectivity;

//Fog
uniform vec4 skyColor;

void main(){
    vec4 texColor = texture(textureSampler, fTexCords);
    if(texColor.a < 0.5){
        discard;
    }

    vec3 unitNormal = normalize(fNormals);
    vec3 unitLightVector = normalize(toLightVector);

    float nDot = dot(unitNormal, unitLightVector);
    float brightness = max(nDot, 0.4);

    vec3 diffuse = brightness * lightColor;

    vec3 unitVectorToCamera = normalize(toCameraVector);
    vec3 lightDirecton = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirecton, unitNormal);

    float specularFactor = dot(reflectedLightDirection, unitNormal);
    specularFactor = max(specularFactor, 0);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity *  lightColor;

    outColor = vec4(diffuse, 0) * texColor + vec4(finalSpecular, 1);

    // Fog
    outColor = mix(skyColor, outColor, visibility);
}



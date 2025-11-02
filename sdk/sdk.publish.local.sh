#../gradlew :sdk:stats:bundleDevelopDebugAar
#../gradlew :sdk:stats:assemble
# -no-configuration-cache

# on macOs the result will be in ~/.m2/repository by default
../gradlew publishToMavenLocal

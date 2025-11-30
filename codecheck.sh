#./gradlew clean spotlessApply detekt lintDebug testDebug
./gradlew clean spotlessApply detekt updateModuleVersions testDebug pixel4api35aospatdCheck


# test whole group of gradle managed devices
# ./gradlew ciGroupDebugAndroidTest

# test single device
#./gradlew pixel4api35aospatdCheck

# ./gradlew clean pixel4api35aospatdCheck --rerun-tasks

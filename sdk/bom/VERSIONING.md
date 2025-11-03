# SDK Versioning

SDK modules are using [Semantic Versioning] and the integrity ensuring the compatible version of the SDK ecosystem is given by the Bill Of Material [BOM]
system.

Each SDK module has version in `version.properties` file placed in the root of that module.

`SdkVersionReadConventionPlugin` takes care about reading this file and set the version into the project.

# Version automation

Whereas MAJOR/MINOR version is a responsibility of SDK engineer, the PATCH version is bumped via automation.

`SdkVersionUpdateConventionPlugin` takes care about content based versioning of the PATCH number.  
`version.properties` holds not only the sdk module version, but also latest content hash.   
And this hash is crucial when it comes to decision bump/not bump the PATCH.

# Versioning process

- Run `./gradlew updateModuleVersions` before each push to the repository
- There is `./gradlew checkModuleVersions` check validating integrity of versioning via `ci.yml` GitHub actions.


[Semantic Versioning]:https://semver.org/

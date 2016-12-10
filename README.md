# GoCD package material plugin shim

[GoCD 17.1](https://www.go.cd) will deprecate the old-style API-based extension points. The
new-style JSON-message-based extension points will be the only way forward. For the package
repository material plugin, the new extension point is described
[here](https://developer.go.cd/current/writing_go_plugins/package_material/json_message_based_package_material_extension.html).

Instead of having to rewrite an existing plugin to use the new APIs immediately, this shim
gives you a little time and makes the move easier. It brings over the deprecated classes of the
old-style extension point and does the translation to and from the new APIs so that most of the
existing code can be used as is.

## How do I use this?

1. Add a dependency to Google's GSON library ([available at Maven Central](http://search.maven.org/#artifactdetails%7Ccom.google.code.gson%7Cgson%7C2.8.0%7Cjar)).
2. Add a dependency to this library ([available at Maven Central](http://search.maven.org/#artifactdetails%7Ccd.go.plugin%7Cgocd-package-material-plugin-shim%7C16.12.0%7Cjar)).
3. Remove the `@Extension` annotation from your plugin's main provider class.
4. Copy over the class found [in this file](https://github.com/gocd-contrib/gocd-package-material-plugin-shim/blob/master/examples/NewPackageMaterialProvider.java)
   into your plugin and replace the text `YourOriginalProviderWhichHadExtensionAnnotationOnIt` with the class you
   removed the `@Extension` annotation from in step 3.
   
## Examples

1. Moving over the [go-maven-poller](https://github.com/aresok/go-maven-poller/compare/master...arvindsv:make-it-work-with-gocd-17-1).
2. Moving over the [go-puppet-forge-poller](https://github.com/drrb/go-puppet-forge-poller/compare/drrb:master...arvindsv:make-it-work-with-gocd-17-1).

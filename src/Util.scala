object Util {

  // from https://github.com/VirtusLab/scala-cli/blob/b754d2afdda114e97febfb0090773cc582bafd19/modules/options/src/main/scala/scala/build/internals/Util.scala#L33-L57
  implicit class ModuleOps(private val mod: dependency.Module) extends AnyVal {
    def toCs: coursier.Module =
      coursier.Module(
        coursier.Organization(mod.organization),
        coursier.ModuleName(mod.name),
        mod.attributes
      )
  }
  implicit class DependencyOps(private val dep: dependency.Dependency) extends AnyVal {
    def toCs: coursier.Dependency = {
      val mod  = dep.module.toCs
      var dep0 = coursier.Dependency(mod, dep.version)
      if (dep.exclude.nonEmpty)
        dep0 = dep0.withExclusions {
          dep.exclude.toSet[dependency.Module].map { mod =>
            (coursier.Organization(mod.organization), coursier.ModuleName(mod.name))
          }
        }
      for (clOpt <- dep.userParams.get("classifier"); cl <- clOpt)
        dep0 = dep0.withPublication(dep0.publication.withClassifier(coursier.core.Classifier(cl)))
      for (tpeOpt <- dep.userParams.get("type"); tpe <- tpeOpt)
        dep0 = dep0.withPublication(dep0.publication.withType(coursier.core.Type(tpe)))
      for (extOpt <- dep.userParams.get("ext"); ext <- extOpt)
        dep0 = dep0.withPublication(dep0.publication.withExt(coursier.core.Extension(ext)))
      for (_ <- dep.userParams.get("intransitive"))
        dep0 = dep0.withTransitive(false)
      dep0
    }
  }

}
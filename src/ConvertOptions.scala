import caseapp._

// format: off
final case class ConvertOptions(
  dest: String,
  @HelpMessage("Whether the Spark distribution pointed by the passed URL might change in the future")
    changing: Boolean = false,
  @HelpMessage("Erase destination path if it already exists")
    force: Boolean = false,
  @HelpMessage("Force Spark version")
  @ExtraName("spark")
    sparkVersion: Option[String] = None,
  @HelpMessage("Force Scala version")
  @ExtraName("scala")
    scalaVersion: Option[String] = None
)
// format: on

object ConvertOptions {
  implicit lazy val parser: Parser[ConvertOptions] = Parser.derive
  implicit lazy val help: Help[ConvertOptions]     = Help.derive
}

package scalariform.formatter

import scalariform.parser.{CompilationUnit, ScalaParser}
import scalariform.formatter.preferences.{ForceAnnotationToNextLine, IndentPackageBlocks, FormattingPreferences}

class AnnoatationFormatterTest extends AbstractFormatterTest {

  override def debug: Boolean = false

  def parse(parser: ScalaParser) = parser.compilationUnitOrScript()

  type Result = CompilationUnit

  def format(formatter: ScalaFormatter, result: Result) = formatter.format(result)(FormatterState(indentLevel = 0))

  "def asdf(@annotation one: Int, @a @b(c) two: String)" ==> "def asdf(@annotation one: Int, @a @b(c) two: String)"

  """class X {
    |  def asdf(
    |    @annotation one: Int,
    |    @a @b(c) two: String
    |  ) = ???
    |}""".stripMargin ==>
  """class X {
    |  def asdf(
    |    @annotation one: Int,
    |    @a @b(c) two: String
    |  ) = ???
    |}""".stripMargin

  {

    implicit val formattingPreferences = FormattingPreferences.setPreference(ForceAnnotationToNextLine, true)

    """class X {
      |  def asdf(
      |    @annotation one: Int,
      |    @a @b(c) two: String
      |  ) = ???
      |}""".stripMargin ==>
    """class X {
      |  def asdf(
      |    @annotation
      |    one: Int,
      |    @a
      |    @b(c)
      |    two: String
      |  ) = ???
      |}""".stripMargin

    """class A extends B {
      |  @SomeImportantAnnotation(param = true) override val param: Int = 1
      |
      |  @NotSoImportantAnnotation(param = false) def description: String = "Not so important"
      |}""".stripMargin ==>
    """class A extends B {
      |  @SomeImportantAnnotation(param = true)
      |  override val param: Int = 1
      |
      |  @NotSoImportantAnnotation(param = false)
      |  def description: String = "Not so important"
      |}""".stripMargin

    """@Annotation class X{}""" ==>
    """@Annotation
      |class X {}""".stripMargin

    """def secondMethod(@Argument arg: Int)""" ==> """def secondMethod(@Argument arg: Int)"""



  }
}

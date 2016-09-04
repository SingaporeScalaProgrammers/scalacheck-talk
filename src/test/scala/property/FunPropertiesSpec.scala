package property

import org.scalatest.{Matchers, WordSpec}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class FunPropertiesSpec extends WordSpec with GeneratorDrivenPropertyChecks with Matchers{

  "Interger.abs should be > 0" in {
    pending
    forAll{
      (x : Int) => x.abs should be >= 0
    }
  }

}

package common

import testing.Suite

class DateTests extends Suite {

  test("create from string") {
    def check(s: String, expect: (Int, Int, Int)): Unit = {
      val d = Date(s).get
      (d.year, d.month, d.day) shouldEqual expect
    }

    check("2019-01-01", expect = (2019, 1, 1))
    check("2019-01-10", expect = (2019, 1, 10))
    check("2019-12-24", expect = (2019, 12, 24))
  }

  test("toString") {
    def check(s: String): Unit =
      Date(s).get.toString shouldEqual s

    check("2019-01-01")
    check("2019-01-10")
    check("2019-12-24")
  }
}

package de.tudarmstadt.conll

import org.scalatest._

class ExtractDepsSpec extends FunSpec with Matchers {

  it("should return for the first word row the deps in both directions") {

    val path = getClass.getResource("/conll.csv").getPath
    val reader = CoNLLFileReader.open(path)
    val sentence = reader.nextSentence()

    val firstRow = sentence.rows.head
    val headRow = sentence.rows(firstRow.head.toInt)
    val extractedDep = s"???"

    firstRow.deprel should be("nn")
    firstRow.head should be("2")
    firstRow.lemma should be("Website")
    headRow.lemma should be("Tips")
    extractedDep  should be("???")
  }
}
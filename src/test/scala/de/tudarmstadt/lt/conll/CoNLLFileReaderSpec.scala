package de.tudarmstadt.lt.conll

import org.scalatest._

class CoNLLFileReaderSpec extends FunSpec with Matchers {

  it("should read correctly single sentence from de.tudarmstadt.de.tudarmstadt.lt.conll.de.tudarmstadt.lt.conll file") {
    val path = getClass.getResource("/conll-single-sentence.csv").getPath
    val reader = CoNLLFileReader.open(path)

    val firstSentence = reader.nextSentence()

    firstSentence.rows.head should be(
      Row(
        id = "0",
        form = "Website",
        lemma = "Website",
        upostag =  "NNP",
        xpostag = "NNP",
        feats = "",
        head =  "2",
        deprel = "nn",
        deps = "2:nn",
        misc = "O"
      )
    )

    firstSentence.rows.length should be(8)
    reader.hasNext should be(false)
  }

  it("should read correctly two sentences from de.tudarmstadt.de.tudarmstadt.lt.conll.de.tudarmstadt.lt.conll file") {
    val path = getClass.getResource("/conll-two-sentences.csv").getPath
    val reader = CoNLLFileReader.open(path)

    val firstSentence = reader.nextSentence()
    val secondSentence = reader.nextSentence()

    firstSentence.rows.head.form should be("Website")
    firstSentence.rows.length should be(8)
    secondSentence.rows.head.form should be("More")
    secondSentence.rows.length should be(8)
  }


  it("should handle quotes as text in de.tudarmstadt.de.tudarmstadt.lt.conll.de.tudarmstadt.lt.conll file") {
    val path = getClass.getResource("/conll-with-quotes.csv").getPath
    val reader = CoNLLFileReader.open(path)

    val firstSentence = reader.nextSentence()

    firstSentence.rows.head.form should be("Website\"")
    firstSentence.rows.length should be(8)
  }
}



package de.tudarmstadt.conll

import com.github.tototoshi.csv.{CSVReader, TSVFormat}

import scala.io.Source

object CoNLLFileReader {

  implicit val format = new TSVFormat {}

  // For field description see de.tudarmstadt.conll.Row
  val fields = Seq(
    "ID", "FORM", "LEMMA", "UPOSTAG", "XPOSTAG", "FEATS", "HEAD",
    "DEPREL", "DEPS", "MISC"
  )

  def open(path: String): CoNLLFileReader = new CoNLLFileReader(path)
}

class CoNLLFileReader(path: String) {

  private val header = CoNLLFileReader.fields.mkString("\t")
  private var fileLines = scala.io.Source.fromFile(path).getLines()

  private def readNextSentence() = {
    val (commentLines, withoutComments) = fileLines.span(_.startsWith("#"))
    val (sentenceLines, remainingLines) = withoutComments.span(!_.startsWith("#"))
    fileLines = remainingLines

    val linesWithHeader = Seq(header) ++ sentenceLines

    val charSource = Source.fromIterable(linesWithHeader.map(_ + "\n").flatMap(_.toList))

    val csvReader = CSVReader.open(charSource)(CoNLLFileReader.format)
    val rows = csvReader.allWithHeaders() map readRow

    new Sentence(commentLines.toList, rows)
  }

  private def readRow(row: Map[String, String]): Row = {
      Row(
        id = row("ID"),
        form = row("FORM"),
        lemma = row("LEMMA"),
        upostag = row("UPOSTAG"),
        xpostag = row("XPOSTAG"),
        feats = row("FEATS"),
        head = row("HEAD"),
        deprel = row("DEPREL"),
        deps = row("DEPS"),
        misc = row("MISC")
      )
    }

  private val iterator = Iterator.continually{ readNextSentence() }.takeWhile(_.rows.nonEmpty)

  def hasNext: Boolean = iterator.hasNext
  def nextSentence(): Sentence = iterator.next()
}
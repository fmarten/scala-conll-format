package de.tudarmstadt.lt.conll

import java.io.Closeable

import com.github.tototoshi.csv.{CSVReader, TSVFormat}

import scala.io.Source

object CoNLLFileReader {

  private val format = new TSVFormat {
    private val NULL_CHAR = '\u0000'
    override val quoteChar: Char = NULL_CHAR
    override val escapeChar: Char = NULL_CHAR
  }

  // For field description see de.tudarmstadt.de.tudarmstadt.lt.conll.de.tudarmstadt.de.tudarmstadt.lt.conll.de.tudarmstadt.lt.conll.Row
  val fields = Seq(
    "ID", "FORM", "LEMMA", "UPOSTAG", "XPOSTAG", "FEATS", "HEAD",
    "DEPREL", "DEPS", "MISC"
  )

  def open(path: String): CoNLLFileReader = new CoNLLFileReader(path)
}

class CoNLLFileReader(path: String) extends Closeable {

  private val header = CoNLLFileReader.fields.mkString("\t")
  private val source = scala.io.Source.fromFile(path)
  private val lineIterator = source.getLines().buffered

  val iterator: Iterator[Sentence] =
    Iterator.continually{ readNextSentence() }.takeWhile(_.rows.nonEmpty)

  def hasNext: Boolean = iterator.hasNext
  def nextSentence(): Sentence = iterator.next()

  override def close(): Unit = source.close()


  private def takeFromLineIteratorWhile(predicate: (String) => Boolean) = {
    var result = List[String]()
    while(lineIterator.hasNext && predicate(lineIterator.head))
      result = result ::: lineIterator.next() :: Nil
    result
  }

  private def extractNextSentencesCommentsAndLines() = {
    val isComment = (s: String) => s.startsWith("#")
    val isNotComment = (s: String) => !isComment(s)

    val commentLines = takeFromLineIteratorWhile(isComment)
    val sentenceLines = takeFromLineIteratorWhile(isNotComment)

    (commentLines, sentenceLines)
  }

  private def readNextSentence() = {
    val (commentLines, sentenceLines) = extractNextSentencesCommentsAndLines()

    val linesWithHeader = Seq(header) ++ sentenceLines

    val charSource = Source.fromIterable(linesWithHeader.map(_ + "\n").flatMap(_.toList))

    val csvReader = CSVReader.open(charSource)(CoNLLFileReader.format)
    val rows = csvReader.allWithHeaders() map readRow

    new Sentence(commentLines, rows)
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
}
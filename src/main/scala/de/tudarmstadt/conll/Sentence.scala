package de.tudarmstadt.conll

class Sentence(val comments: List[String], val rows: List[Row]) {

  def rowById(id: Int): Row = rows(id)
}

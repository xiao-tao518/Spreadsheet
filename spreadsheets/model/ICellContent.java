package edu.cs3500.spreadsheets.model;

/**
 * represents a cell in worksheet.
 */

public interface ICellContent {

  /**
   * accept a visitor to visitor all class implement the ICellContent.
   *
   * @param visitor use to visitor all class implement the ICellContent
   * @param <R>     any type of ICellContent
   * @return any type of ICellContent
   */
  <R> R accept(ICellContentVisitor<R> visitor);

  /**
   * check if the cell in given coordinate has cycle reference.
   *
   * @param col the column
   * @param row the row
   * @return a boolean
   */
  boolean noCycle(int col, int row);

  /**
   * get the content of a ICellContent.
   *
   * @return a string that represents content
   */
  String getContent();

  /**
   * get the restore of a ICellContent.
   *
   * @return a ICellContent that represents the original class that has not been evaluated
   */
  ICellContent getRestore();

  /**
   * check if it is an IFormula.
   *
   * @return a boolean of whether it is an IFormula
   */
  boolean isFormula();

  /**
   * check whether the cell contain the given coord.
   *
   * @param c the given coord
   * @return boolean whether contain this coord
   */
  boolean containCell(Coord c);


  boolean isError();
}

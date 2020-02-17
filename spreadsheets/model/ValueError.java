package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * Represents a error value in spreadsheet.
 */
public class ValueError implements IValue {

  ICellContent restore;
  String content;

  /**
   * Represents a IValue in worksheet which has a string content that shows some error.
   *
   * @param c error message.
   */
  public ValueError(String c) {
    this.restore = null;
    this.content = c;
  }

  public ValueError(ValueError ve) {
    this.restore = ve.restore;
    this.content = ve.content;
  }

  /**
   * transform the content to a string that shows the error and change the restore to the original
   * errorValue.
   */
  public void transform() {
    if (this.restore == null) {
      String contentCopy = new String(content);
      this.restore = new ValueError(contentCopy);
      this.content = "error message";
    }
  }

  @Override
  public double getDouble() {
    throw new IllegalArgumentException("ERROR");
  }

  @Override
  public boolean getBoolean() {
    throw new IllegalArgumentException("ERROR");
  }

  @Override
  public boolean isValueBoolean() {
    return false;
  }

  @Override
  public boolean isValueDouble() {
    return false;
  }

  @Override
  public boolean isValueString() {
    return false;
  }

  @Override
  public <R> R accept(ICellContentVisitor<R> visitor) {
    return visitor.visitValueError(this.content, this.restore);
  }

  @Override
  public boolean noCycle(int col, int row) {
    return true;
  }

  @Override
  public String toString() {
    return this.content;
  }

  @Override
  public String getContent() {
    return this.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValueError ve = (ValueError) o;
    if (this.restore == null || ve.restore == null) {
      if (this.restore == null && ve.restore == null) {
        return this.content.equals(ve.content);
      } else {
        return false;
      }
    } else {
      return this.content.equals(ve.content) && this.restore.equals(ve.restore);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(content);
  }

  @Override
  public ICellContent getRestore() {
    return this.restore;
  }

  @Override
  public boolean isFormula() {
    return false;
  }

  @Override
  public boolean containCell(Coord c) {
    if (restore == null) {
      return false;
    } else {
      return restore.containCell(c);
    }
  }

  @Override
  public boolean isError() {
    return true;
  }
}

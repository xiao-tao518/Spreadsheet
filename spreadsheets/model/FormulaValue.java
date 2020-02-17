package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * represents a type of IValue in the formula form.
 */

public class FormulaValue implements IFormula {

  ICellContent restore;
  IValue content;

  /**
   * Contain a content which we do not know it is what type of IValue.
   *
   * @param v IValue
   */

  public FormulaValue(IValue v) {
    this.restore = null;
    this.content = v;
  }

  @Override
  public <R> R accept(ICellContentVisitor<R> visitor) {
    return visitor.visitFormulaValue(this.content, this.restore);
  }

  @Override
  public String toString() {
    return this.content.toString();
  }

  @Override
  public boolean noCycle(int col, int row) {
    return true;
  }

  @Override
  public String getContent() {
    return this.toString();
  }

  @Override
  public ICellContent getRestore() {
    return this.restore;
  }

  @Override
  public boolean isFormula() {
    return true;
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
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FormulaValue fv = (FormulaValue) o;
    if (this.restore == null || fv.restore == null) {
      if (this.restore == null && fv.restore == null) {
        return this.content.equals(fv.content);
      } else {
        return false;
      }
    } else {
      return fv.content.equals(this.content) && this.restore.equals(fv.restore);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(content);
  }
}

package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * Represents a IValue in worksheet which contains a double content.
 */
public class ValueDouble implements IValue {
  ICellContent restore;
  double content;

  /**
   * Represents a IValue in worksheet which contains a double content.
   *
   * @param d the input double
   */
  public ValueDouble(double d) {
    this.restore = null;
    this.content = d;
  }

  public ValueDouble(ValueDouble vd) {
    this.restore = vd.restore;
    this.content = vd.content;
  }

  @Override
  public <R> R accept(ICellContentVisitor<R> visitor) {
    return visitor.visitValueDouble(this.content, this.restore);
  }

  @Override
  public String toString() {
    return String.valueOf(this.content);
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
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValueDouble vs = (ValueDouble) o;
    if (this.restore == null || vs.restore == null) {
      if (this.restore == null && vs.restore == null) {
        return Math.abs(this.content - vs.content) <= 0.000001;
      } else {
        return false;
      }
    } else {
      return Math.abs(this.content - vs.content) <= 0.000001 && this.restore.equals(vs.restore);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(content);
  }


  @Override
  public double getDouble() {
    return this.content;
  }

  @Override
  public boolean getBoolean() {
    throw new IllegalArgumentException("Invalid function");
  }

  @Override
  public boolean isValueBoolean() {
    return false;
  }

  @Override
  public boolean isValueDouble() {
    return true;
  }

  @Override
  public boolean isValueString() {
    return false;
  }
}

package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * Represents a IValue in worksheet which contains a boolean content.
 */
public class ValueBoolean implements IValue {
  ICellContent restore;
  boolean content;

  /**
   * Represents a IValue in worksheet which contains a boolean content.
   *
   * @param b the input boolean
   */
  public ValueBoolean(boolean b) {
    this.restore = null;
    this.content = b;
  }

  public ValueBoolean(ValueBoolean vb) {
    this.restore = vb.restore;
    this.content = vb.content;
  }

  @Override
  public <R> R accept(ICellContentVisitor<R> visitor) {
    return visitor.visitValueBoolean(this.content, this.restore);
  }

  @Override
  public String toString() {
    if (this.content) {
      return "true";
    } else {
      return "false";
    }
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
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValueBoolean vb = (ValueBoolean) o;
    if (this.restore == null || vb.restore == null) {
      if (this.restore == null && vb.restore == null) {
        return vb.content == this.content;
      } else {
        return false;
      }
    } else {
      return vb.content == this.content && this.restore == vb.restore;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(content);
  }

  @Override
  public double getDouble() {
    return 0.0;
  }

  @Override
  public boolean getBoolean() {
    return this.content;
  }

  @Override
  public boolean isValueBoolean() {
    return true;
  }

  @Override
  public boolean isValueDouble() {
    return false;
  }

  @Override
  public boolean isValueString() {
    return false;
  }

}

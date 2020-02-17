package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * Represents a IValue in worksheet which contains a string content.
 */
public class ValueString implements IValue {

  ICellContent restore;
  String content;

  /**
   * Represents a IValue in worksheet which contains a string content.
   *
   * @param s the input boolean
   */
  public ValueString(String s) {
    this.restore = null;
    this.content = s;
  }

  public ValueString(ValueString vs) {
    this.restore = vs.restore;
    this.content = vs.content;
  }

  @Override
  public <R> R accept(ICellContentVisitor<R> visitor) {
    return visitor.visitValueString(this.content, this.restore);
  }

  @Override
  public String toString() {
    return "\"" + this.content + "\"";
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
    ValueString vs = (ValueString) o;
    if (this.restore == null || vs.restore == null) {
      if (this.restore == null && vs.restore == null) {
        return this.content.equals(vs.content);
      } else {
        return false;
      }
    } else {
      return this.content.equals(vs.content) && this.restore.equals(vs.restore);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(content);
  }

  @Override
  public double getDouble() {
    throw new IllegalArgumentException("invalid function");
  }

  @Override
  public boolean getBoolean() {
    throw new IllegalArgumentException("invalid function");
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
    return true;
  }
}

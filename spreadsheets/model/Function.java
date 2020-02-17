package edu.cs3500.spreadsheets.model;

import java.util.List;
import java.util.Objects;

/**
 * represents a function in a cell that can be evaluate.
 */

public class Function implements IFormula {

  String method;
  List<ICellContent> content;
  ICellContent restore;

  /**
   * contain a string as the method type, and a list shows other input.
   *
   * @param method the operator in a formula
   * @param l      the rest part of function without the method in the first
   */
  public Function(String method, List<ICellContent> l) {
    this.method = method;
    this.content = l;
    this.restore = null;
  }


  @Override
  public <R> R accept(ICellContentVisitor<R> visitor) {
    return visitor.visitFunction(this.method, this.content, this.restore);
  }

  @Override
  public String toString() {
    String result = "";
    result += "(" + this.method + " ";
    for (ICellContent temp : this.content) {
      result += temp.toString() + " ";
    }
    return result.substring(0, result.length() - 1) + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Function f = (Function) o;
    if (f.content.size() != this.content.size()) {
      return false;
    }
    boolean result = true;
    for (int i = 0; i < f.content.size(); i++) {
      result = result && f.content.get(i).equals(this.content.get(i));
    }
    if (this.restore == null || f.restore == null) {
      if (this.restore == null && f.restore == null) {
        return this.content.equals(f.content);
      } else {
        return false;
      }
    } else {
      return this.method.equals(f.method) && result && this.restore.equals(f.restore);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(content);
  }

  @Override
  public boolean noCycle(int col, int row) {
    boolean result = true;
    for (int i = 0; i < content.size(); i++) {
      result = result && content.get(i).noCycle(col, row);
    }
    return result;
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
    boolean result = false;
    for (ICellContent i : content) {
      if (i.containCell(c)) {
        result = true;
        break;
      }
    }
    return result;
  }

  @Override
  public boolean isError() {
    return false;
  }
}

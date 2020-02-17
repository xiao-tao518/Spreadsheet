package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Reference cell that can refer others cells.
 */
public class Reference implements IFormula {

  String f;
  List<Coord> content = new ArrayList<>();
  ICellContent restore;

  /**
   * Represents a Reference cell with a input string.
   *
   * @param f the input string
   */
  public Reference(String f) {
    this.f = f;
    if (f.contains(":")) {
      int splitNum = f.indexOf(":");
      String firstCoord = f.substring(0, splitNum);
      int row1FirstDigit = firstDigit(firstCoord);
      String row1 = firstCoord.substring(row1FirstDigit);
      if ((firstCoord.length() == row1.length()) || !allDigit(row1)) {
        throw new IllegalArgumentException("invalid row1");
      }
      //String firstCol = firstCoord.replaceAll("[^(a-zA-Z)]", "");
      //String firstRow = firstCoord.replaceAll("[^(0-9)]", "");
      String secondCoord = f.substring(splitNum + 1);
      int row2FirstDigit = firstDigit(secondCoord);
      String row2 = secondCoord.substring(row2FirstDigit);
      if ((secondCoord.length() == row2.length()) || !allDigit(row2)) {
        throw new IllegalArgumentException("invalid row2");
      }
      this.content = reference(f);
    } else {
      int row1FirstDigit = firstDigit(f);
      String row = f.substring(row1FirstDigit);
      if ((f.length() == row.length()) || !allDigit(row)) {
        throw new IllegalArgumentException("invalid row");
      }
      String colString = f.replaceAll("[^(a-zA-Z)]", "");
      String rowString = f.replaceAll("[^(0-9)]", "");
      content.add(new Coord(Coord.colNameToIndex(colString), Integer.parseInt(rowString)));
    }
    this.restore = null;
  }

  private boolean allDigit(String s) {
    boolean result = true;
    for (int i = 0; i < s.length(); i++) {
      result = result && Character.isDigit(s.charAt(i));
    }
    return result;
  }

  private int colonIndex(String s) {
    int index = 0;
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) != (':')) {
        index = i;
        break;
      }
    }
    return index;
  }

  private int firstDigit(String s) {
    int count = 0;
    for (int i = 0; i < s.length(); i++) {
      if (Character.isDigit(s.charAt(i))) {
        count = i;
        break;
      }
    }
    return count;
  }

  private List<Integer> referenceHelper(String s) {
    List<Integer> result = new ArrayList<>();
    int colon = colonIndex(s);
    String first = s.substring(0, colon);
    String second = s.substring(colon + 1);
    int digitOfFirst = firstDigit(first);
    int digitOfSecond = firstDigit(second);
    String initialColumn1 = first.substring(0, digitOfFirst);
    int row1 = Integer.parseInt(first.substring(digitOfFirst));
    String initialColumn2 = second.substring(0, digitOfSecond);
    int row2 = Integer.parseInt(second.substring(digitOfSecond));
    Coord start = new Coord(1, 1);
    Coord end = new Coord(1, 1);
    start.colNameToIndex(initialColumn1);
    end.colNameToIndex(initialColumn2);
    result.add(start.col);
    result.add(row1);
    result.add(end.col);
    result.add(row2);
    return result;
  }

  private List<Coord> reference(String s) {
    List<Coord> result = new ArrayList<>();
    List<Integer> range = referenceHelper(s);
    int startCol;
    int startRow;
    int endCol;
    int endRow;
    int col1 = range.get(0);
    int row1 = range.get(1);
    int col2 = range.get(2);
    int row2 = range.get(3);
    if (col1 <= col2) {
      startCol = col1;
      endCol = col2;
    } else {
      startCol = col2;
      endCol = col1;
    }

    if (row1 <= row2) {
      startRow = row1;
      endRow = col2;
    } else {
      startRow = row2;
      endRow = row1;
    }
    for (int i = startRow; i <= endRow; i++) {
      for (int j = startCol; j <= endCol; j++) {
        result.add(new Coord(i, j));
      }
    }
    return result;
  }

  @Override
  public <R> R accept(ICellContentVisitor<R> visitor) {
    return visitor.visitReference(this.f, this.restore);
  }

  @Override
  public String toString() {
    return this.f;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Reference r = (Reference) o;
    if (this.restore == null || r.restore == null) {
      if (this.restore == null && r.restore == null) {
        return this.content.equals(r.content);
      } else {
        return false;
      }
    } else {
      return this.content.equals(r.content) && this.restore.equals(r.restore);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(content);
  }

  @Override
  public boolean noCycle(int col, int row) {
    boolean result = true;
    for (Coord coord : content) {
      if (coord.col == col && coord.row == row) {
        result = false;
        break;
      }
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
    return this.content.contains(c);
  }

  @Override
  public boolean isError() {
    return false;
  }

}

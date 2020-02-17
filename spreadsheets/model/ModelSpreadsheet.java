package edu.cs3500.spreadsheets.model;

import java.util.HashMap;
import java.util.Map;

import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.Visitor;

/**
 * Create a basic Model Spreadsheet with a field map as our board sheet.
 */

public class ModelSpreadsheet implements IModelSpreadsheet {
  Map<Coord, ICellContent> worksheet = new HashMap<>();

  /**
   * create a empty worksheet without any information.
   */

  public ModelSpreadsheet() {
    worksheet = new HashMap<>();
  }

  /**
   * Create a worksheet use worksheet builder.
   *
   * @param w BasicWorksheetBuilder
   */

  public ModelSpreadsheet(BasicWorksheetBuilder w) {
    this.worksheet = w.worksheet;
  }

  private ICellContent readContent(String in) {
    try {
      return Parser.parse(removeSpace(in)).accept(new Visitor());
    } catch (Exception e) {
      ValueError ve = new ValueError("error input");
      ve.restore = new ValueError(in);
      return ve;
    }
  }

  @Override
  public HashMap<Coord, ICellContent> getWorksheet() {
    return new HashMap<>(this.worksheet);
  }

  //order
  @Override
  public void addAndUpdate(int col, int row, String in) {
    this.noCyclic(col, row, in);
    this.validCoord(col, row);
    ICellContent temp = this.readContent(in);
    Coord c = new Coord(col, row);
    this.worksheet.put(c, temp.accept(new EvaluateVisitor(this.getWorksheet())));
    this.update(this.worksheet, c);
  }

  private void update(Map<Coord, ICellContent> worksheet, Coord c) {
    for (Map.Entry<Coord, ICellContent> entry : worksheet.entrySet()) {
      if (entry.getValue().getRestore() == null) {
        if (entry.getValue().containCell(c)) {
          entry.getValue().accept(new EvaluateVisitor(this.getWorksheet()));
          update(this.worksheet, entry.getKey());
        }
      } else {
        if (entry.getValue().getRestore().containCell(c)) {
          entry.getValue().getRestore().accept(new EvaluateVisitor(this.getWorksheet()));
          update(this.worksheet, entry.getKey());
        }
      }
    }
  }

  @Override
  public String getContent(int col, int row, boolean show) {
    if (show) {
      this.validCoord(col, row);
      Coord c = new Coord(col, row);
      if (this.getWorksheet().get(c) == null) {
        return "";
      } else if (this.getWorksheet().get(c).isError()) {
        ICellContent temp = worksheet.get(new Coord(col, row));
        if (temp.getRestore() == null) {
          return temp.getContent();
        } else {
          if (temp.getRestore().isFormula()) {
            return "=" + temp.getRestore().getContent();
          } else {
            return temp.getRestore().getContent();
          }
        }
      } else if (this.getWorksheet().get(c).getRestore() == null) {
        ICellContent temp = worksheet.get(new Coord(col, row));
        if (temp.isFormula()) {
          return "=" + temp.getContent();
        } else {
          return temp.getContent();
        }
      } else {
        if (this.getWorksheet().get(c).getRestore().isFormula()) {
          ICellContent temp = worksheet.get(new Coord(col, row));
          return "=" + temp.getRestore().getContent();
        } else {
          ICellContent temp = worksheet.get(new Coord(col, row));
          return temp.getRestore().getContent();
        }
      }
    } else {
      this.validCoord(col, row);
      if (worksheet.get(new Coord(col, row)) != null) {
        ICellContent temp = worksheet.get(new Coord(col, row));
        return temp.accept(new EvaluateVisitor(this.getWorksheet())).getContent();
      } else {
        return "";
      }
    }
  }

  @Override
  public boolean sameModel(IModelSpreadsheet w) {
    if (this == w) {
      return true;
    } else if (w == null || this.getClass() != w.getClass()) {
      return false;
    }
    return this.sameMap(((ModelSpreadsheet) w).worksheet);
  }

  @Override
  public void removeCell(Coord c) {
    this.worksheet.remove(c);
  }

  private boolean sameMap(Map<Coord, ICellContent> m) {
    boolean result;
    if (m != null) {
      if (this.worksheet.size() == m.size()) {
        result = this.worksheet.entrySet().stream()
                .allMatch(e -> e.getValue().equals(m.get(e.getKey())));
      } else {
        result = false;
      }
    } else {
      result = false;
    }
    return result;
  }

  private void validCoord(int col, int row) throws IllegalArgumentException {
    if (row > Integer.MAX_VALUE || col > Integer.MAX_VALUE) {
      throw new IllegalArgumentException("Parameter out of bound");
    }
    if (row < 1 || col < 1) {
      throw new IllegalArgumentException("invalid coordinate");
    }
  }

  private void noCyclic(int col, int row, String in) {
    String s = Coord.colIndexToName(col) + row;
    ICellContent temp = this.readContent(in);
    if (!temp.noCycle(col, row)) {
      throw new IllegalArgumentException("has cycle");
    }
  }

  private String removeSpace(String s) {
    String temp = "";
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) != '=') {
        temp += s.charAt(i);
      }
    }
    return temp;
  }

  /**
   * represents a builder which build a worksheet for basic.
   */
  public static class BasicWorksheetBuilder implements
          WorksheetReader.WorksheetBuilder<ModelSpreadsheet> {
    Map<Coord, ICellContent> worksheet = new HashMap<>();

    @Override
    public WorksheetReader.WorksheetBuilder<ModelSpreadsheet> createCell(int col, int row,
                                                                         String contents) {
      if (row < 1 || col < 1) {
        throw new IllegalArgumentException("Coordinates should be strictly positive");
      } else {
        if (contents.contains("=")) {
          contents = removeSpace(contents);
        }
        worksheet.put(new Coord(col, row), Parser.parse(contents).accept(new Visitor()));
      }
      return this;
    }

    private String removeSpace(String s) {
      String temp = "";
      for (int i = 0; i < s.length(); i++) {
        if (s.charAt(i) != '=') {
          temp += s.charAt(i);
        }
      }
      return temp;
    }

    @Override
    public ModelSpreadsheet createWorksheet() {
      return new ModelSpreadsheet(this);
    }
  }

}

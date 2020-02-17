package edu.cs3500.spreadsheets.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ICellContent;
import edu.cs3500.spreadsheets.model.ModelSpreadsheet;

/**
 * A textual view that save the model to the Appendable.
 */
public class BasicTextualView implements ISpreadsheetView {
  final ModelSpreadsheet model;
  Appendable output;

  /**
   * A textual view that save the given model to the Appendable.
   *
   * @param model  spreadsheet model
   * @param output Appendable
   */
  public BasicTextualView(ModelSpreadsheet model, Appendable output) {
    this.model = model;
    this.output = output;
  }

  @Override
  public void render() throws IOException {
    HashMap<Coord, ICellContent> worksheet =
            new HashMap<Coord, ICellContent>(this.model.getWorksheet());
    for (Map.Entry<Coord, ICellContent> entry : worksheet.entrySet()) {
      String cell = entry.getKey().toString();
      if (entry.getValue() != null) {
        if (entry.getValue().getRestore() != null) {
          if (entry.getValue().getRestore().isFormula()) {
            String content = entry.getValue().getRestore().getContent();
            String result = cell + " =" + content;
            this.output.append(result + "\n");
          } else {
            String content = entry.getValue().getRestore().getContent();
            String result = cell + " " + content;
            this.output.append(result + "\n");
          }
        } else {
          if (entry.getValue().isFormula()) {
            String content = entry.getValue().getContent();
            String result = cell + " =" + content;
            this.output.append(result + "\n");
          } else {
            String content = entry.getValue().getContent();
            String result = cell + " " + content;
            this.output.append(result + "\n");
          }
        }
      }
    }
  }

  @Override
  public String getTextString() {
    throw new IllegalArgumentException("error");
  }

  @Override
  public void resetFocus() {
    throw new IllegalArgumentException("error");
  }

  @Override
  public void cleanTextString() {
    throw new IllegalArgumentException("error");
  }

  @Override
  public void updateTextField(Coord c) {
    throw new IllegalArgumentException("error");
  }


  @Override
  public Coord getCoord() {
    throw new IllegalArgumentException("error");
  }

  @Override
  public void addMouse() {
    throw new IllegalArgumentException("error");
  }

  @Override
  public void reDraw() {
    throw new IllegalArgumentException("error");
  }

  @Override
  public void addButton()  {
    throw new IllegalArgumentException("error");
  }
}
package edu.cs3500.spreadsheets;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import edu.cs3500.spreadsheets.controller.Controller;
import edu.cs3500.spreadsheets.model.ModelSpreadsheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.BasicTextualView;
import edu.cs3500.spreadsheets.view.BasicVisualView;
import edu.cs3500.spreadsheets.view.EditableVisualView;
import edu.cs3500.spreadsheets.view.ISpreadsheetView;

/**
 * The main class for our program.
 */
public class Spreadsheet {
  /**
   * The main entry point.
   *
   * @param args any command-line arguments
   */
  public static void main(String[] args) {
    /*
      - read the file and build a model from it, 
      - evaluate all the cells, and
      - report any errors, or print the evaluated value of the requested cell.
    */

    if (args.length == 1 && args[0].equals("-gui")) {
      BasicVisualView view = new BasicVisualView(new ModelSpreadsheet());
      view.render();
    }
    else if (args.length == 3 && args[0].equals("-in") && args[2].equals("-gui")) {
      FileReader fileReader = null;
      try {
        fileReader = new FileReader(args[1]);
      } catch (IOException e) {
        System.out.print("not file");
      }
      ModelSpreadsheet worksheet = WorksheetReader.read(
              new ModelSpreadsheet.BasicWorksheetBuilder(), fileReader);
      BasicVisualView view = new BasicVisualView(worksheet);
      view.render();
    }
    else if (args.length == 4 && args[0].equals("-in") && args[2].equals("-save")) {
      FileReader fileReader = null;
      try {
        fileReader = new FileReader(args[1]);
      } catch (IOException e) {
        System.out.print("not file");
      }
      ModelSpreadsheet worksheet = WorksheetReader.read(
              new ModelSpreadsheet.BasicWorksheetBuilder(), fileReader);
      try {
        PrintWriter p = new PrintWriter(args[3]);
        ISpreadsheetView view = new BasicTextualView(worksheet, p);
        view.render();
        p.close();

      } catch (IOException e) {
        System.out.print("invalid");
      }
    }
    else if (args.length == 4 && args[0].equals("-in") && args[2].equals("-eval")) {
      FileReader fileReader = null;
      try {
        fileReader = new FileReader(args[1]);
      } catch (IOException e) {
        System.out.print("not file");
      }

      try {
        ModelSpreadsheet worksheet = WorksheetReader.read(
                new ModelSpreadsheet.BasicWorksheetBuilder(), fileReader);
        String cols = args[3].replaceAll("[^(a-zA-Z)]", "");
        String rows = args[3].replaceAll("[^(0-9)]", "");
        int col = Coord.colNameToIndex(cols);
        int row = Integer.parseInt(rows);
        System.out.print(worksheet.getContent(col, row, false));
      } catch (IllegalArgumentException e) {
        System.out.print(e);
      }
    }
    else if (args.length == 1 && args[0].equals("-edit")) {
      try {
        ModelSpreadsheet basic = new ModelSpreadsheet();
        BasicVisualView view = new BasicVisualView(basic);
        EditableVisualView ed = new EditableVisualView(view);
        Controller ct = new Controller(ed);
        ct.configureMouseListener();
        ct.configureButtonListener();
        ed.render();
      } catch (IllegalArgumentException e) {
        System.out.print(e);
      }
    }
    else if (args.length == 3 && args[0].equals("-edit") && args[2].equals("-gui")) {
      FileReader fileReader = null;
      try {
        fileReader = new FileReader(args[1]);
      } catch (IOException e) {
        System.out.print("not file");
      }
      try {
        ModelSpreadsheet worksheet = WorksheetReader.read(
                new ModelSpreadsheet.BasicWorksheetBuilder(), fileReader);
        BasicVisualView view = new BasicVisualView(worksheet);
        EditableVisualView ed = new EditableVisualView(view);
        Controller ct = new Controller(ed);
        ct.configureMouseListener();
        ct.configureButtonListener();
        ed.render();
      } catch (IllegalArgumentException e) {
        System.out.print(e);
      }
    }
    else {
      throw new IllegalArgumentException("error command line");
    }
  }
}

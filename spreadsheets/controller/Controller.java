package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.view.ISpreadsheetView;

/**
 * Create a Controller add the listen to the view.
 */
public class Controller implements IController {
  private ISpreadsheetView view;

  /**
   * a Controller input a current model and a ISpreadsheetView.
   *
   * @param view a view is a type of ISpreadsheetView
   */
  public Controller(ISpreadsheetView view) {
    this.view = view;
  }

  @Override
  public void configureButtonListener() {
    this.view.addButton();
  }

  @Override
  public void configureMouseListener() {
    this.view.addMouse();
  }

}

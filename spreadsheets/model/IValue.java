package edu.cs3500.spreadsheets.model;

/**
 * represents a single value which could be boolean, double and string.
 */
public interface IValue extends ICellContent {

  /**
   * get the content of double.
   *
   * @return a bouble
   */
  double getDouble();

  /**
   * get the content of boolean.
   *
   * @return a boolean
   */
  boolean getBoolean();

  /**
   * determine if this is ValueBoolean.
   *
   * @return a boolean
   */
  boolean isValueBoolean();

  /**
   * determine if this is ValueDouble.
   *
   * @return a boolean
   */
  boolean isValueDouble();

  /**
   * determine if this is ValueString.
   *
   * @return a boolean
   */
  boolean isValueString();

}

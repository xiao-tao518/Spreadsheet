package edu.cs3500.spreadsheets.model;

import java.util.List;

/**
 * a visitor interface that transform input to a specific type {@link ICellContent}.
 *
 * @param <R> any type of ICellContent
 */
public interface ICellContentVisitor<R> {

  /**
   * input a boolean which would visitor.
   *
   * @param b       boolean
   * @param restore restore for row code
   * @return a type of ICellContent
   */
  R visitValueBoolean(boolean b, ICellContent restore);

  /**
   * input a double when visitor a value double class.
   *
   * @param d       double the content
   * @param restore restore for row code
   * @return a type of ICellContent
   */
  R visitValueDouble(double d, ICellContent restore);

  /**
   * input a string when visitor a value string class.
   *
   * @param s       string
   * @param restore restore for row code
   * @return a type of ICellContent
   */
  R visitValueString(String s, ICellContent restore);

  /**
   * input a string when visitor a Reference class.
   *
   * @param s       string
   * @param restore restore for row code
   * @return a type of ICellContent
   */
  R visitReference(String s, ICellContent restore);

  /**
   * input a string when visitor a Function class.
   *
   * @param method  string
   * @param l       List of ICellContent
   * @param restore restore for row code
   * @return a type of ICellContent
   */
  R visitFunction(String method, List<ICellContent> l, ICellContent restore);

  /**
   * input a IValue when visitor a FormulaValue class.
   *
   * @param v       IValue
   * @param restore restore for row code
   * @return a type of ICellContent
   */
  R visitFormulaValue(IValue v, ICellContent restore);


  /**
   * input a IValue when visitor a ErrorValue class.
   *
   * @param s       a error information show by strin
   * @param restore restore for row code
   * @return a type of ICellContent
   */
  R visitValueError(String s, ICellContent restore);

}

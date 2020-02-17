package edu.cs3500.spreadsheets.model;

import java.util.List;
import java.util.Map;

/**
 * A visitor that visit formula class and evaluate the input with <.
 */
public class SmallVisitor implements ICellContentVisitor<ValueBoolean> {
  Map<Coord, ICellContent> worksheet;

  /**
   * A visitor that evaluate the input with < with the given worksheet.
   *
   * @param worksheet the worksheet from basic
   */
  public SmallVisitor(Map<Coord, ICellContent> worksheet) {
    this.worksheet = worksheet;
  }

  @Override
  public ValueBoolean visitValueBoolean(boolean b, ICellContent restore) {
    throw new IllegalArgumentException("Invalid");
  }

  @Override
  public ValueBoolean visitValueDouble(double d, ICellContent restore) {
    throw new IllegalArgumentException("Invalid");
  }

  @Override
  public ValueBoolean visitValueString(String s, ICellContent restore) {
    throw new IllegalArgumentException("Invalid");
  }

  @Override
  public ValueBoolean visitReference(String s, ICellContent restore) {
    throw new IllegalArgumentException("Invalid");
  }

  @Override
  public ValueBoolean visitFunction(String method, List<ICellContent> l, ICellContent restore) {
    try {
      if (method.equals(Method.SMALL.toString()) && l.size() == 2) {
        boolean temp = l.get(0).accept(new EvaluateVisitor(worksheet))
                .accept(new SmallHelpVisitor(worksheet)).getDouble()
                < l.get(1).accept(new EvaluateVisitor(worksheet))
                .accept(new SmallHelpVisitor(worksheet)).getDouble();
        ValueBoolean ft = new ValueBoolean(temp);
        ft.restore = new Function(method, l);
        return ft;
      } else {
        throw new IllegalArgumentException("Invalid small");
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("error");
    }
  }

  @Override
  public ValueBoolean visitFormulaValue(IValue v, ICellContent restore) {
    ValueBoolean fv = v.accept(new SmallVisitor(worksheet));
    fv.restore = new FormulaValue(v);
    return fv;
  }

  @Override
  public ValueBoolean visitValueError(String s, ICellContent restore) {
    throw new IllegalArgumentException("Invalid small");
  }
}

package edu.cs3500.spreadsheets.model;

import java.util.List;
import java.util.Map;

/**
 * A visitor that evaluate the input with method < and help the SmallVisitor class to get the
 * double.
 */
public class SmallHelpVisitor implements ICellContentVisitor<ValueDouble> {

  Map<Coord, ICellContent> worksheet;

  /**
   * A visitor that evaluate the input with < with the given worksheet.
   *
   * @param worksheet the worksheet from basic
   */
  public SmallHelpVisitor(Map<Coord, ICellContent> worksheet) {
    this.worksheet = worksheet;
  }

  @Override
  public ValueDouble visitValueBoolean(boolean b, ICellContent restore) {
    throw new IllegalArgumentException("Invalid");
  }

  @Override
  public ValueDouble visitValueDouble(double d, ICellContent restore) {
    return new ValueDouble(d);
  }

  @Override
  public ValueDouble visitValueString(String s, ICellContent restore) {
    throw new IllegalArgumentException("Invalid");
  }

  @Override
  public ValueDouble visitReference(String s, ICellContent restore) {
    throw new IllegalArgumentException("Invalid");
  }

  @Override
  public ValueDouble visitFunction(String method, List<ICellContent> l, ICellContent restore) {
    return new Function(method, l).accept(new EvaluateVisitor(worksheet))
            .accept(new SmallHelpVisitor(worksheet));
  }

  @Override
  public ValueDouble visitFormulaValue(IValue v, ICellContent restore) {
    return v.accept(new SmallHelpVisitor(worksheet));
  }

  @Override
  public ValueDouble visitValueError(String s, ICellContent restore) {
    throw new IllegalArgumentException("Invalid");
  }
}

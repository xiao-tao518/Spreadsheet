package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A visitor that visit formula class evaluate the input with SUB with the given worksheet.
 */
public class SubVisitor implements ICellContentVisitor<ValueDouble> {
  Map<Coord, ICellContent> worksheet;

  /**
   * A visitor that evaluate the input with SUB with the given worksheet.
   *
   * @param worksheet the worksheet from basic
   */
  public SubVisitor(Map<Coord, ICellContent> worksheet) {
    this.worksheet = worksheet;
  }

  @Override
  public ValueDouble visitValueBoolean(boolean b, ICellContent restore) {
    return new ValueDouble(0.0);
  }

  @Override
  public ValueDouble visitValueDouble(double d, ICellContent restore) {
    return new ValueDouble(d);
  }

  @Override
  public ValueDouble visitValueString(String s, ICellContent restore) {
    return new ValueDouble(0.0);
  }

  @Override
  public ValueDouble visitReference(String s, ICellContent restore) {
    List<Coord> temp = new Reference(s).content;
    List<ICellContent> list = new ArrayList<>();
    for (int i = 0; i < temp.size(); i++) {
      list.add(worksheet.get(temp.get(i)));
    }
    double result = 0.0;
    result = result + list.get(0).accept(new EvaluateVisitor(worksheet))
            .accept(new SumVisitor(worksheet)).getDouble();
    for (int i = 1; i < list.size(); i++) {
      result -= list.get(i).accept(new EvaluateVisitor(worksheet))
              .accept(new SumVisitor(worksheet)).getDouble();
    }
    ValueDouble re = new ValueDouble(result);
    re.restore = new Reference(s);
    return re;
  }

  @Override
  public ValueDouble visitFunction(String method, List<ICellContent> l, ICellContent restore) {
    double result = 0.0;
    try {
      if (method.equals(Method.SUB.toString()) && l.size() == 2) {
        result = l.get(0).accept(new EvaluateVisitor(worksheet))
                .accept(new SubVisitor(worksheet)).getDouble()
                - l.get(1).accept(new EvaluateVisitor(worksheet))
                .accept(new SubVisitor(worksheet)).getDouble();
        ValueDouble ft = new ValueDouble(result);
        ft.restore = new Function(method, l);
        return ft;
      } else {
        throw new IllegalArgumentException("not sub method");
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("error");
    }
  }

  @Override
  public ValueDouble visitFormulaValue(IValue v, ICellContent restore) {
    ValueDouble fv = v.accept(new SubVisitor(worksheet));
    fv.restore = new FormulaValue(v);
    return fv;
  }

  @Override
  public ValueDouble visitValueError(String s, ICellContent restore) {
    throw new IllegalArgumentException("invalid");
  }
}

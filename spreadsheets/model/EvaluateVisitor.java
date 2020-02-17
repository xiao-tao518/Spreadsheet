package edu.cs3500.spreadsheets.model;

import java.util.List;
import java.util.Map;

/**
 * a visitor which can visitor all type of ICellContent and .
 */

public class EvaluateVisitor implements ICellContentVisitor<IValue> {
  final Map<Coord, ICellContent> worksheet;

  /**
   * contain a current worksheet use to update.
   *
   * @param worksheet the current
   */

  public EvaluateVisitor(Map<Coord, ICellContent> worksheet) {
    this.worksheet = worksheet;
  }


  @Override
  public IValue visitValueBoolean(boolean b, ICellContent restore) {
    if (restore == null) {
      ValueBoolean vb = new ValueBoolean(b);
      vb.restore = new ValueBoolean(b);
      return vb;
    } else {
      return restore.accept(new EvaluateVisitor(worksheet));
    }
  }

  @Override
  public IValue visitValueDouble(double d, ICellContent restore) {
    if (restore == null) {
      ValueDouble vd = new ValueDouble(d);
      vd.restore = new ValueDouble(d);
      return vd;
    } else {
      return restore.accept(new EvaluateVisitor(worksheet));
    }
  }

  @Override
  public IValue visitValueString(String s, ICellContent restore) {
    if (restore == null) {
      ValueString vs = new ValueString(s);
      vs.restore = new ValueString(s);
      return vs;
    } else {
      return restore.accept(new EvaluateVisitor(worksheet));
    }
  }

  @Override
  public IValue visitReference(String s, ICellContent restore) {
    Reference rf = new Reference(s);
    if (rf.content.size() == 1) {
      ICellContent content = worksheet.get(rf.content.get(0));
      IValue iv = content.accept(new EvaluateVisitor(worksheet));
      if (iv.isValueString()) {
        ValueString vs = new ValueString((ValueString) iv);
        vs.restore = new Reference(s);
        return vs;
      } else if (iv.isValueDouble()) {
        ValueDouble vd = new ValueDouble((ValueDouble) iv);
        vd.restore = new Reference(s);
        return vd;
      } else if (iv.isValueBoolean()) {
        ValueBoolean vb = new ValueBoolean((ValueBoolean) iv);
        vb.restore = new Reference(s);
        return vb;
      } else {
        ValueError ve = new ValueError((ValueError) iv);
        ve.restore = new Reference(s);
        return ve;
      }
    } else {
      ValueError ve = new ValueError("error reference");
      ve.restore = new Reference(s);
      return ve;
    }
  }

  @Override
  public IValue visitFunction(String method, List<ICellContent> l, ICellContent restore) {
    try {
      if (restore != null) {
        throw new IllegalArgumentException("Invalid");
      } else if (method.equals(Method.SUM.toString())) {
        return new Function(method, l).accept(new SumVisitor(worksheet));
      } else if (method.equals(Method.PRD.toString())) {
        return new Function(method, l).accept(new ProductVisitor(worksheet));
      } else if (method.equals(Method.SMALL.toString())) {
        return new Function(method, l).accept(new SmallVisitor(worksheet));
      } else if (method.equals(Method.SUB.toString())) {
        return new Function(method, l).accept(new SubVisitor(worksheet));
      } else {
        throw new IllegalArgumentException("Invalid");
      }
    } catch (Exception e) {
      ValueError ve = new ValueError("error formula");
      ve.restore = new Function(method, l);
      return ve;
    }
  }


  @Override
  public IValue visitFormulaValue(IValue v, ICellContent restore) {
    IValue fv = v.accept(new EvaluateVisitor(worksheet));

    if (fv.isValueBoolean()) {
      ((ValueBoolean) fv).restore = new FormulaValue(v);
    } else if (fv.isValueDouble()) {
      ((ValueDouble) fv).restore = new FormulaValue(v);
    } else if (fv.isValueString()) {
      ((ValueString) fv).restore = new FormulaValue(v);
    } else {
      ((ValueError) fv).restore = new FormulaValue(v);
    }
    return fv;
  }

  @Override
  public IValue visitValueError(String s, ICellContent restore) {
    ValueError ve = new ValueError(s);
    ve.restore = restore;
    return ve;
  }
}

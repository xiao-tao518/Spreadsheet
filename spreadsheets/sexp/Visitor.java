package edu.cs3500.spreadsheets.sexp;

import java.util.ArrayList;
import java.util.List;

import edu.cs3500.spreadsheets.model.Function;
import edu.cs3500.spreadsheets.model.ICellContent;
import edu.cs3500.spreadsheets.model.Reference;
import edu.cs3500.spreadsheets.model.ValueBoolean;
import edu.cs3500.spreadsheets.model.ValueDouble;
import edu.cs3500.spreadsheets.model.ValueError;
import edu.cs3500.spreadsheets.model.ValueString;

/**
 * Represents a visitor that parse the s-expression to ICellcContent..
 */
public class Visitor implements SexpVisitor<ICellContent> {
  @Override
  public ICellContent visitBoolean(boolean b) {
    return new ValueBoolean(b);
  }

  @Override
  public ICellContent visitNumber(double d) {
    return new ValueDouble(d);
  }

  @Override
  public ICellContent visitSymbol(String s) {
    ICellContent result;
    try {
      result = new Reference(s);
    } catch (Exception e1) {
      ValueError ve = new ValueError(s);
      ve.transform();
      result = ve;
    }
    return result;
  }

  @Override
  public ICellContent visitString(String s) {
    return new ValueString(s);
  }

  @Override
  public ICellContent visitSList(List<Sexp> l) {
    if (l.size() == 1) {
      ValueError ve = new ValueError("=(" + l.get(0).toString() + ")");
      ve.transform();
      return ve;
    }
    String temp = l.get(0).toString();
    ArrayList<ICellContent> result = new ArrayList<>();
    for (int i = 1; i < l.size(); i++) {
      result.add(l.get(i).accept(new Visitor()));
    }
    return new Function(temp, result);
  }
}

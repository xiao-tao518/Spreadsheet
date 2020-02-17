package edu.cs3500.spreadsheets.model;

/**
 * represents an operator that can be used in formula and evaluate.
 */
public enum Method {
  SUM("SUM"), PRD("PRODUCT"), SMALL("<"), SUB("SUB");

  String op;

  Method(String op) {
    this.op = op;
  }

  @Override
  public String toString() {
    return this.op;
  }
}
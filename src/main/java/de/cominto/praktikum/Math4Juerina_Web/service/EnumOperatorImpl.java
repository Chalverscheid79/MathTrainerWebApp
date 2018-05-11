package de.cominto.praktikum.Math4Juerina_Web.service;

public enum EnumOperatorImpl implements EnumOperator {
	ADD("+"),SUB("-");
	private final String sign;
	
	private EnumOperatorImpl(final String sign) {
		// TODO Automatisch generierter Konstruktorstub
		this.sign = sign;
	}

	public String getOperator() {
		// TODO Automatisch generierter Methodenstub
		return this.sign;
	}

}

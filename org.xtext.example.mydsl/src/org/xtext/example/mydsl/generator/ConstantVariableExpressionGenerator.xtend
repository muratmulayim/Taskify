package org.xtext.example.mydsl.generator

import org.xtext.example.mydsl.generator.IExpressionGenerator
import org.xtext.example.mydsl.myDsl.ConstantVariableExpression
import org.xtext.example.mydsl.myDsl.ArrayAssignment
import org.xtext.example.mydsl.myDsl.Operation

class ConstantVariableExpressionGenerator implements IExpressionGenerator {
	ConstantVariableExpression expression
	GeneratorSwitcher generator
	
	new (ConstantVariableExpression expression, GeneratorSwitcher generator) {
		this.expression = expression
		this.generator = generator
	}
	
	override String generate() {
		val String scope = SymbolTable.CONSTANT
		val String name = this.expression.name
		val String type = this.expression.type.type
		SymbolTable.addSymbol(name, type, scope)

		var String result = "__nv "
		
		
		val boolean isArray = this.expression.dimension !== null
		
//		Generate variable type and name
		result += CommonGenerator.getVariableTypeName(type, name)
		if (isArray) {
			result += CommonGenerator.getDimension(this.expression.dimension, -1)
		} 
		
//		Generate right side of definition if exists
		if (this.expression.assignment !== null) {
			if (isArray) {
				result += " = " + OperationExpressionGenerator.getAssignment(this.expression.assignment.expression as ArrayAssignment)	
			} else {
				result += " = " + generator.generate(this.expression.assignment.expression as Operation)
			}
		}
		
		result += ";" + CommonGenerator.newLine	
		return result
	}
	
}
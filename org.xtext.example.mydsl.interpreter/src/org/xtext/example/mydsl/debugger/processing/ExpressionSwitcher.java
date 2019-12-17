package org.xtext.example.mydsl.debugger.processing;

import org.eclipse.emf.ecore.EObject;
import org.xtext.example.mydsl.debugger.InterpreterException;
import org.xtext.example.mydsl.debugger.processing.expression.BreakExpressionExecutor;
import org.xtext.example.mydsl.debugger.processing.expression.BuiltinFunctionCallExpressionExecutor;
import org.xtext.example.mydsl.debugger.processing.expression.ForExpressionExecutor;
import org.xtext.example.mydsl.debugger.processing.expression.GlobalVariableExpressionExecutor;
import org.xtext.example.mydsl.debugger.processing.expression.IExpressionExecutor;
import org.xtext.example.mydsl.debugger.processing.expression.IfExpressionExecutor;
import org.xtext.example.mydsl.debugger.processing.expression.OperationExpressionExecutor;
import org.xtext.example.mydsl.debugger.processing.expression.VariableDeclerationExpressionExecutor;
import org.xtext.example.mydsl.debugger.processing.expression.WhileExpressionExecutor;
import org.xtext.example.mydsl.myDsl.BreakExpression;
import org.xtext.example.mydsl.myDsl.BuiltinFunctionCallExpression;
import org.xtext.example.mydsl.myDsl.ForExpression;
import org.xtext.example.mydsl.myDsl.GlobalVariableExpression;
import org.xtext.example.mydsl.myDsl.IfExpression;
import org.xtext.example.mydsl.myDsl.OperationExpression;
import org.xtext.example.mydsl.myDsl.VariableDeclerationExpression;
import org.xtext.example.mydsl.myDsl.WhileExpression;

public class ExpressionSwitcher extends ProcessHandler {
	public static enum MODES {RUN, DEBUG}
	private static MODES MODE = ExpressionSwitcher.MODES.RUN;
	
	public MODES getMode() {
		return ExpressionSwitcher.MODE;
	}
	
	public void setMode(MODES mode) {
		ExpressionSwitcher.MODE = mode;
	}
	
	public void execute(EObject expression, String id) {
		if (ExpressionSwitcher.MODE == ExpressionSwitcher.MODES.DEBUG) {
			ThreadStateForDebugging(expression);
		}

		IExpressionExecutor executor = null;
		
		try {
			if (expression instanceof BreakExpression) {
				executor = (IExpressionExecutor) new BreakExpressionExecutor((BreakExpression) expression, this);
			} else if (expression instanceof BuiltinFunctionCallExpression) {
				executor = (IExpressionExecutor) new BuiltinFunctionCallExpressionExecutor((BuiltinFunctionCallExpression) expression, this);
			} else if (expression instanceof ForExpression) {
				executor = (IExpressionExecutor) new ForExpressionExecutor((ForExpression) expression, this);
			} else if (expression instanceof GlobalVariableExpression) {
				executor = (IExpressionExecutor) new GlobalVariableExpressionExecutor((GlobalVariableExpression) expression, this);
			} else if (expression instanceof IfExpression) {
				executor = (IExpressionExecutor) new IfExpressionExecutor((IfExpression) expression, this);
			} else if (expression instanceof OperationExpression) {
				executor = (IExpressionExecutor) new OperationExpressionExecutor((OperationExpression) expression, this);
			} else if (expression instanceof VariableDeclerationExpression) {
				executor = (IExpressionExecutor) new VariableDeclerationExpressionExecutor((VariableDeclerationExpression) expression, this);
			} else if (expression instanceof WhileExpression) {
				executor = (IExpressionExecutor) new WhileExpressionExecutor((WhileExpression) expression, this);
			} else {
				new InterpreterException("Unsupported expression " + expression.toString());
			}
			
			executor.execute(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	@Override
	public void run() {
		super.start();
	}

}

package com.codetropics.java.asm.timemachine;

import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.LADD;
import static org.objectweb.asm.Opcodes.POP2;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.codetropics.java.asm.timemachine.TimeMachineAgentDelegate.Milliseconds;


/**
 * TimeMachineAdapter is used to catch all the system time queries and manipulate the result
 * to effectively get a shift in time.
 *
 * @see TimeMachineAgentDelegate
 * 
 * @author <a href="http://www.hapiware.com" target="_blank">hapi</a>
 *
 *
 * Updated for ASM 9.x+ by ChatGPT 4.1
 */
public class TimeMachineAdapter extends MethodVisitor
{
	private final Milliseconds milliseconds;
	

	public TimeMachineAdapter(Milliseconds milliseconds, MethodVisitor mv)
	{
		// Set the API version for ASM 9+
		super(Opcodes.ASM9, mv);
		this.milliseconds = milliseconds;
	}
	
	/**
	 * Catches all the system time queries and manipulates the result either by adding
	 * (or subtracting) time from it or replacing the system time value altogether with
	 * absolute time. 
	 */
	@Override
	public void visitMethodInsn(
		int opcode,
		String owner,
		String name,
		String desc,
		boolean isInterface
	) {
		mv.visitMethodInsn(opcode, owner, name, desc, isInterface);
		switch(opcode) {
			case INVOKESPECIAL :
				/*
				//The no-arg constructor new Date() internally calls System.currentTimeMillis()
				// thus new Date() gets the shift applied twice.
				if(
					owner.equals("java/util/Date") && 
					name.equals("<init>") &&
					desc.equals("()V")
				) {

					mv.visitInsn(DUP);
					if(milliseconds.isRelative()) {
						mv.visitInsn(DUP);
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Date", "getTime", "()J", false);
						mv.visitLdcInsn(milliseconds.getTime());
						mv.visitInsn(LADD);
					}
					else
						mv.visitLdcInsn(milliseconds.getTime());
					mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Date", "setTime", "(J)V", false);

				} */
				break;
				
			case INVOKESTATIC :
				if(
					owner.equals("java/lang/System") && 
					name.equals("currentTimeMillis") &&
					desc.equals("()J")
				) {
					if(milliseconds.isRelative()) {
						mv.visitLdcInsn(milliseconds.getTime());
						mv.visitInsn(LADD);
					}
					else {
						mv.visitInsn(POP2);
						mv.visitLdcInsn(milliseconds.getTime());
					}
				}
				else
					if(
						owner.equals("java/util/Calendar") && 
						name.equals("getInstance") &&
						desc.equals("()Ljava/util/Calendar;")
					) {
						mv.visitInsn(DUP);
						if(milliseconds.isRelative()) {
							mv.visitInsn(DUP);
							mv.visitMethodInsn(
								INVOKEVIRTUAL, "java/util/Calendar", "getTimeInMillis", "()J", false
							);
							mv.visitLdcInsn(milliseconds.getTime());
							mv.visitInsn(LADD);
						}
						else
							mv.visitLdcInsn(milliseconds.getTime());
						mv.visitMethodInsn(
							INVOKEVIRTUAL, "java/util/Calendar", "setTimeInMillis", "(J)V", false
						);
					}
				break;
		}
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals)
	{
		mv.visitMaxs(maxStack + 6, maxLocals);
	}
}

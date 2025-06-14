package com.codetropics.java.asm.timemachine;


import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.codetropics.java.asm.timemachine.TimeMachineAgentDelegate.Milliseconds;


/**
 * Initialises {@link TimeMachineAdapter} for class manipulation.
 * 
 * @see TimeMachineAdapter
 * @see TimeMachineTransformer#transform(ClassLoader, String, Class, ProtectionDomain, byte[])
 *
 * @author <a href="http://www.hapiware.com" target="_blank">hapi</a>
 *
 */
public class TimeMachineTransformer
	implements
		ClassFileTransformer
{
	private Pattern[] _includePatterns;
	private Pattern[] _exludePatterns;
	private final Milliseconds _timeShift;

	public TimeMachineTransformer()
	{
		_timeShift = null;
	}
	
	public TimeMachineTransformer(
		Pattern[] includePatterns,
		Pattern[] excludePatterns,
		Milliseconds timeShift
	)
	{
		_includePatterns = includePatterns;
		_exludePatterns = excludePatterns;
		_timeShift = timeShift;
	}
	
	public byte[] transform(
		ClassLoader loader,
		final String className,
		Class<?> classBeingRedefined,
		ProtectionDomain protectionDomain,
		byte[] classFileBuffer
	)
		throws IllegalClassFormatException
	{
		for(Pattern p : _exludePatterns)
			if(p.matcher(className).matches())
				return null;
		
		for(Pattern p : _includePatterns) {
			if(p.matcher(className).matches()) 
			{
				try
				{
					ClassReader cr = new ClassReader(classFileBuffer);
					ClassWriter cw = new ClassWriter(0);
					//ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
					//ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
					cr.accept(
						new ClassVisitor(Opcodes.ASM9, cw)
						{
							@Override
							public MethodVisitor visitMethod(
								int access,
								String name,
								String desc,
								String signature,
								String[] exceptions
							)
							{
								MethodVisitor mv =
									super.visitMethod(access, name, desc, signature, exceptions);	
								return new TimeMachineAdapter(_timeShift, mv);
							} 
						},
						0
					);
	
					return cw.toByteArray();
				}
				catch(Throwable e)
				{
					throw new Error("Instrumentation of a class " + className + " failed.", e);
				}
			}
		}
		return null;
	}
}
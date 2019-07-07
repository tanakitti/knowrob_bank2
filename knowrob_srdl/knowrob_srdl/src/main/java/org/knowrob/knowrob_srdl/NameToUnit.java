/* \file LinkTypeReader.java
 * 
 * \brief URDF2SRDL is a tool for automatically creating a SRDL description 
 * from an URDF document. 
 *
 * It was originally created for <a href="http://www.roboearth.org/">RoboEarth</a>.
 * The research leading to these results has received funding from the 
 * European Union Seventh Framework Programme FP7/2007-2013 
 * under grant agreement no248942 RoboEarth.
 *
 * Copyright (C) 2012 by Philipp Freyer 
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *    <UL>
 *     <LI> Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     <LI> Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     <LI> Neither the name of Willow Garage, Inc. nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *    </UL>
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * \author Philipp Freyer 
 * \version 1.0
 * \date 2012
 */

package org.knowrob.knowrob_srdl;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameToUnit {
	

	//Singleton
	private static NameToUnit me = null;
	private HashMap<String, String> n2u;
	private HashMap<String, String> chunk2u;
	private HashMap<Pattern, String> valueRE2u;
	
	private NameToUnit(){
		n2u = new HashMap<String, String>();
		n2u.put("minrange", "&qudt-unit;Meter");
		n2u.put("updaterate", "&qudt-unit;Hertz");
		
		chunk2u = new HashMap<String, String>();
		chunk2u.put("count", "&qudt-unit;Unitless");
		chunk2u.put("angle", "&qudt-unit;DegreeAngle");
		chunk2u.put("range", "&qudt-unit;Meter");
		chunk2u.put("rate", "&qudt-unit;Hertz");
		chunk2u.put("factor", "&qudt-unit;Unitless");
		chunk2u.put("name", "&xsd;string");
		chunk2u.put("damping", "&qudt-unit;Unitless");
		chunk2u.put("mass", "&qudt-unit;Kilogram");
		chunk2u.put("inertia", "&qudt-unit;Kilogram");
		chunk2u.put("velocity", "&qudt-unit;MeterPerSecond");
		chunk2u.put("effort", "&qudt-unit;NewtonMeter");
		
		
		valueRE2u = new HashMap<Pattern, String>();
		valueRE2u.put(Pattern.compile("(true)|(false)"), "&xsd;boolean");
		valueRE2u.put(Pattern.compile("-?\\d+"), "&qudt-unit;Unitless");
		valueRE2u.put(Pattern.compile("-?\\d+\\.\\d+"), "&qudt-unit;Unitless");
		valueRE2u.put(Pattern.compile("(.*)(\\w+)(.*)"), "&xsd;string");
	}
	
	public static NameToUnit getObjectReference() {
		if(me == null){
			me = new NameToUnit();
		}
		return me;
	}
	
	public String ntu(String name, String value) {
		
		// exact hit
		name = name.toLowerCase();
		if(n2u.containsKey(name)){
			return n2u.get(name);
		}
		
		// chunk hit
		for(String chunk: chunk2u.keySet()){
			if(name.contains(chunk)){
				return chunk2u.get(chunk);
			}
		}
		
		// try to guess type from the value:
		for(Pattern p: valueRE2u.keySet()){
			Matcher m = p.matcher(value);
			if(m.matches()){
				return valueRE2u.get(p);
			}
		}
		
		// not found
		return "&xsd;string";
	}
}

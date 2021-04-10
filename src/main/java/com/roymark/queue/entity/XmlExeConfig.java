package com.roymark.queue.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlExeConfig {

	private String Dictionary_Id;

	private String TerminalExeConfig_Ls;

	private String Terminal_Ls;

	private String ExeType;

	private String ExeCase_Ls;

	private String TerminalExeConfig_Name;

	private String ListenPort;

	private String ComNum;

	private String TerminalExeConfig_Parameter_1;

	private String TerminalExeConfig_Parameter_2;

	private String TerminalExeConfig_Parameter_3;

	private String TerminalExeConfig_Parameter_4;

	public String getDictionary_Id() {
		return Dictionary_Id;
	}


}

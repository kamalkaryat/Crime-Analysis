package com.k2dev.ca.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ForgotPswRequest {
	private String newPsw;
	private String cnfPsw;
	private String username;
}

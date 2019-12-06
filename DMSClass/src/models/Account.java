package models;

import java.io.Serializable;
import enums.UserType;

//계정
public final class Account implements Serializable {
	// 키
	public final String accountId; // 아이디, 학생은 학번이 들어가게 되고, 관리자는 문자열이 들어갈 수 있음.

	// 키가 아닌 컬럼
	public final String password; // 비밀번호
	public final UserType userType; // 사용자 타입

	public Account(String accountId, String password, UserType userType) {
		this.accountId = accountId;
		this.password = password;
		this.userType = userType;
	}
}

package com.ibretail.ibretaildemo.membership;

import java.util.List;

import com.ibretail.ibretaildemo.transaction.AccountBalance;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "PENGGUNA")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Email(message = "Paramter email tidak sesuai format")
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String firstName;
	
	@Column(nullable = false)
	private String lastName;
	
	@Size(min = 8, message = "Password minimal 8 karakter")
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = true)
	private String profileImage;
	
	@Column(nullable = false)
	private String token;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AccountBalance> accountBalances;
	
	public User() {
		
	}

	public User(Integer id, String email, String firstName, String lastName, String password, String token, List<AccountBalance> accountBalances) {
		super();
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.token = token;
		this.accountBalances = accountBalances;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<AccountBalance> getAccountBalances() {
		return accountBalances;
	}

	public void setAccountBalances(List<AccountBalance> accountBalances) {
		this.accountBalances = accountBalances;
	}
	
}

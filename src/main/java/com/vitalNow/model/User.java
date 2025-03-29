package com.vitalNow.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vitalNow.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="User")
public class User implements UserDetails{

	private static final long serialVersionUID = 1L;

	@Id
	private String username;
	
	@Column(name="name")
	@NotBlank(message=" Name must not be null or blank")
	private String name;
	
	@Column(name="first_surname")
	@NotBlank(message=" First surname must not be null or blank")
	private String firstSurname;
	
	@Column(name="second_surname")
	private String secondSurname;
	
	@Column(name="email")
	@NotBlank(message=" Email must not be null or blank")
	@Email
	private String email;
	
	@Column(name="role")
	@NotNull(message="Role null or not valid. It must be 'NURSE', 'DOCTOR' or 'ADMIN'.")
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Column(name="title")
	private String title;
	
	@Column(name="password")
	@NotBlank(message=" Password must not be null or blank")
	private String password;
	
	@Column(name="password_changed")
	private boolean passwordChanged;
	
	@OneToMany(mappedBy="user")
	private Set<Record> records;
	
	
	public User() {
		super();
		this.passwordChanged=false;
		this.records = new TreeSet<Record>();
	}
	
	public User(String name, String firstSurname, String secondSurname, String email, Role role) {
		this();
		this.name = name;
		this.firstSurname = firstSurname;
		this.secondSurname = secondSurname;
		this.email = email;
		this.role = role;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstSurname() {
		return firstSurname;
	}

	public void setFirstSurname(String firstSurname) {
		this.firstSurname = firstSurname;
	}

	public String getSecondSurname() {
		return secondSurname;
	}

	public void setSecondSurname(String secondSurname) {
		this.secondSurname = secondSurname;
	}

	public Set<Record> getRecords() {
		return records;
	}

	public void setRecords(Set<Record> records) {
		this.records = records;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isPasswordChanged() {
		return passwordChanged;
	}

	public void setPasswordChanged(boolean passwordChanged) {
		this.passwordChanged = passwordChanged;
	}

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(username, other.username);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> list = new ArrayList<>();
		list.add(new SimpleGrantedAuthority(role.toString()));
		return list;
	} 

}

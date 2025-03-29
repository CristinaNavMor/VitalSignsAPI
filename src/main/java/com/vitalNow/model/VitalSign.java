package com.vitalNow.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="Vital_sign")
public class VitalSign {
	
	@Id
	@Column(name="name")
	@NotBlank
	private String name;
	
	@Column(name="min_value")
	@NotBlank
	private Float min;
	
	@Column(name="max_value")
	@NotBlank
	private Float max;
	
	/* Se deja sin implementar el @OneToMany de la relación por no aportar a las funcionalidades de la aplicación.*/
	
	public VitalSign() {
		super();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getMin() {
		return min;
	}

	public void setMin(Float min) {
		this.min = min;
	}

	public Float getMax() {
		return max;
	}

	public void setMax(Float max) {
		this.max = max;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VitalSign other = (VitalSign) obj;
		return Objects.equals(name, other.name);
	}
	
	

}

package com.onlyabhinav.cowinslots.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cowinslots")
public class EntityCowinSlots {
	

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
	@Column(name = "pincode")
	private Integer pincode;
	
	@Column(name = "vaccine")
	private String vaccine;
	
	@Column(name = "dose2")
	private Integer dose2;
	
	@Column(name = "all_slots")
	private String all_slots;
	
	@Column(name = "centreName")
	private String centreName;
	

	@Column(name = "isValid")
	private boolean isValid;

}

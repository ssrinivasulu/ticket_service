/**
 * 
 */
package com.demo.ticketservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * @author ssrinivasulu
 *
 */
/*@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,isGetterVisibility = JsonAutoDetect.Visibility.ANY,getterVisibility = JsonAutoDetect.Visibility.ANY)
@Entity (name = "ReservationType")
@Table (name = "reservation_type")
@SequenceGenerator(name = "reservation_type_seq", sequenceName = "reservation_type_seq")
public class ReservationType {
	@Id
	@GeneratedValue (strategy = GenerationType.SEQUENCE)
	@Column (name = "id", unique = true, nullable = false )
	private Long id;
	
	@Enumerated(EnumType.STRING)
    private ReservationTypeCode reservationType;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public ReservationTypeCode getReservationType() {
		return reservationType;
	}
	public void setReservationType(ReservationTypeCode reservationType) {
		this.reservationType = reservationType;
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder(55, 113).append(this.id).append(this.reservationType).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		ReservationType other = (ReservationType) o;
		return new EqualsBuilder()
				         .append(other.reservationType, this.reservationType)
				         .append(other.id, this.id)
				         .isEquals();
	}
	
}*/

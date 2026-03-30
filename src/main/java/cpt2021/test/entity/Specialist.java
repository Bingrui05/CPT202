package cpt2021.test.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "specialist")
public class Specialist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 128)
	private String displayName;

	@Column(length = 255)
	private String email;

	@Column(length = 32)
	private String phone;

	@Column(length = 2000)
	private String bio;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "expertise_category_id", nullable = false)
	private ExpertiseCategory expertiseCategory;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "level_id", nullable = false)
	private Level level;

	@Column(nullable = false)
	private Instant createdAt;

	@Column(nullable = false)
	private Instant updatedAt;

	protected Specialist() {
	}

	public Specialist(
			String displayName,
			String email,
			String phone,
			String bio,
			ExpertiseCategory expertiseCategory,
			Level level) {
		this.displayName = displayName;
		this.email = email;
		this.phone = phone;
		this.bio = bio;
		this.expertiseCategory = expertiseCategory;
		this.level = level;
	}

	@PrePersist
	void onCreate() {
		Instant now = Instant.now();
		this.createdAt = now;
		this.updatedAt = now;
	}

	@PreUpdate
	void onUpdate() {
		this.updatedAt = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public ExpertiseCategory getExpertiseCategory() {
		return expertiseCategory;
	}

	public void setExpertiseCategory(ExpertiseCategory expertiseCategory) {
		this.expertiseCategory = expertiseCategory;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}
}

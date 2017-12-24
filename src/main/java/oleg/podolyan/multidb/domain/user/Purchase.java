package oleg.podolyan.multidb.domain.user;

import oleg.podolyan.multidb.domain.product.Product;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "user_purchases")
@SQLDelete(sql = "UPDATE user_purchases SET deleted = true WHERE purchase_id = ?")
public class Purchase {

	@Id
	@GeneratedValue
	@Column(name = "purchase_id", unique = true)
	private Long id;
	@NaturalId // for test to roll back transaction
	private String title;
	private String description;
	@Column(precision = 2)
	private BigDecimal price;
	@Column(columnDefinition = "DATE")
	private Date purchased;
	private boolean deleted;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	private User user;

	private Purchase(){ }

	private Purchase(String title, String description, BigDecimal price, Date purchased) {
		this.title = title;
		this.description = description;
		this.price = price;
		this.purchased = purchased;
	}

	public static Purchase of(Product product, Date date){
		return new Purchase(product.getTitle(), product.getDescription(), product.getPrice(), date);
	}

	public static Purchase of(Product product){
		return of(product, new Date());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getPurchased() {
		return purchased;
	}

	public void setPurchased(Date purchased) {
		this.purchased = purchased;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}



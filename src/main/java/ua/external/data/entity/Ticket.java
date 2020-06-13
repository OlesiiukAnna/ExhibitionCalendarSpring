package ua.external.data.entity;

import ua.external.util.enums.TicketType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "visit_date")
    private LocalDate visitDate;
    @Column(name = "order_date")
    private LocalDate orderDate;
    @Column(name = "ticket_type", length = 20)
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;
    @Column(name = "ticket_price")
    private int ticketPrice;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    private Exhibition exhibition;
    @Column(name = "is_paid")
    private boolean isPaid;

    public Ticket() {
    }

    public Ticket(LocalDate visitDate, LocalDate orderDate,
                  TicketType ticketType, int ticketPrice,
                  User user, Exhibition exhibition) {
        this.visitDate = visitDate;
        this.orderDate = orderDate;
        this.ticketType = ticketType;
        this.ticketPrice = ticketPrice;
        this.user = user;
        this.exhibition = exhibition;
    }

    public Ticket(int id, LocalDate visitDate, LocalDate orderDate,
                  TicketType ticketType, int ticketPrice, boolean isPaid,
                  User user, Exhibition exhibition) {
        this.id = id;
        this.visitDate = visitDate;
        this.orderDate = orderDate;
        this.ticketType = ticketType;
        this.ticketPrice = ticketPrice;
        this.user = user;
        this.exhibition = exhibition;
        this.isPaid = isPaid;
    }

    public Integer getId() {
        return id;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Exhibition getExhibition() {
        return exhibition;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User visitor) {
        this.user = user;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", visitDate=" + visitDate +
                ", orderDate=" + orderDate +
                ", ticketType=" + ticketType +
                ", ticketPrice=" + ticketPrice +
                ", user=" + user +
                ", exhibition=" + exhibition +
                ", isPaid=" + isPaid +
                '}';
    }
}

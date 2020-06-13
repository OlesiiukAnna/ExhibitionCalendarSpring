package ua.external.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "exhibitions")
public class Exhibition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "begin_date", nullable = false)
    private LocalDate beginDate;
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    @Column(name = "full_ticket_price", nullable = false)
    private int fullTicketPrice;
    @ManyToOne
    @JoinColumn(name = "exhibition_hall_id")
    private ExhibitionHall exhibitionHall;
    @OneToMany(mappedBy = "exhibition", cascade = CascadeType.MERGE)
    private List<Ticket> tickets;

    public Exhibition() {
    }

    public Exhibition(String name, String description,
                      LocalDate beginDate, LocalDate endDate,
                      int fullTicketPrice, ExhibitionHall exhibitionHall) {
        this.name = name;
        this.description = description;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.fullTicketPrice = fullTicketPrice;
        this.exhibitionHall = exhibitionHall;
    }

    public Exhibition(int id, String name, String description,
                      LocalDate beginDate, LocalDate endDate,
                      int fullTicketPrice, ExhibitionHall exhibitionHall) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.fullTicketPrice = fullTicketPrice;
        this.exhibitionHall = exhibitionHall;

    }

    public Integer getId() {
        return id;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getFullTicketPrice() {
        return fullTicketPrice;
    }

    public void setFullTicketPrice(int fullTicketPrice) {
        this.fullTicketPrice = fullTicketPrice;
    }

    public ExhibitionHall getExhibitionHall() {
        return exhibitionHall;
    }

    public void setExhibitionHall(ExhibitionHall exhibitionHall) {
        this.exhibitionHall = exhibitionHall;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "Exhibition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", fullTicketPrice=" + fullTicketPrice +
                ", exhibitionHall=" + exhibitionHall +
                '}';
    }
}

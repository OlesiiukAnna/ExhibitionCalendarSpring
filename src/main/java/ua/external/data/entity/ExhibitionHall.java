package ua.external.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "exhibition_halls")
public class ExhibitionHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "allowable_number_of_visitors_per_day", nullable = false)
    private int allowableNumberOfVisitorsPerDay;
    @OneToMany(mappedBy = "exhibitionHall", cascade = CascadeType.MERGE)
    private List<Exhibition> exhibitions;

    public ExhibitionHall() {
    }

    public ExhibitionHall(String name, int allowableNumberOfVisitorsPerDay) {
        this.name = name;
        this.allowableNumberOfVisitorsPerDay = allowableNumberOfVisitorsPerDay;
    }

    public ExhibitionHall(Integer id, String name, int allowableNumberOfVisitorsPerDay) {
        this.id = id;
        this.name = name;
        this.allowableNumberOfVisitorsPerDay = allowableNumberOfVisitorsPerDay;
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

    public int getAllowableNumberOfVisitorsPerDay() {
        return allowableNumberOfVisitorsPerDay;
    }

    public void setAllowableNumberOfVisitorsPerDay(int allowableNumberOfVisitorsPerDay) {
        this.allowableNumberOfVisitorsPerDay = allowableNumberOfVisitorsPerDay;
    }

    public List<Exhibition> getExhibitions() {
        return exhibitions;
    }

    public void setExhibitions(List<Exhibition> exhibitions) {
        this.exhibitions = exhibitions;
    }

    @Override
    public String toString() {
        return "ExhibitionHall{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numberOfVisitorsPerDay=" + allowableNumberOfVisitorsPerDay +
                '}';
    }
}

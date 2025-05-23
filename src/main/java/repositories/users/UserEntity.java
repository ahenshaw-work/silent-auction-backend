package repositories.users;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_info")
@NamedQuery(name = "UserEntity.findAll", query = "SELECT u FROM UserEntity u ORDER BY u.username", hints = @QueryHint(name = "org.hibernate.cacheable", value = "false"))
@NamedQuery(name = "UserEntity.getUser", query = "SELECT u FROM UserEntity u WHERE u.username = :username", hints = @QueryHint(name = "org.hibernate.cacheable", value = "false"))
@Cacheable
public class UserEntity {

    @Id
    @SequenceGenerator(name = "userSequence", sequenceName = "user_info_id_seq", schema = "auction", allocationSize = 1)
    @GeneratedValue(generator = "userSequence")
    private Integer id;

    @Column(length = 255, unique = true)
    private String username;

    @Column(length = 255, unique = true)
    private String first_name;

    @Column(length = 255, unique = true)
    private String last_name;

    @Column
    private int table_number;

    public UserEntity() {
    }

    public UserEntity(
        String username,
        String first_name,
        String last_name,
        int table_number
    ) {
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.table_number = table_number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public int getTableNumber() {
        return table_number;
    }

    public void setTableNumber(int table_number) {
        this.table_number = table_number;
    }
}
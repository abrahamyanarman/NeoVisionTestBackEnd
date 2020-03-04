package am.neovision.api.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class EmailCodes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "code", nullable = false)
    private long code;

    @Temporal(TemporalType.DATE)
    private Date date;

    public EmailCodes() {
        date = new Date();
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailCodes that = (EmailCodes) o;
        return email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}

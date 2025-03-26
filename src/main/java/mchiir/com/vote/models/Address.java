package mchiir.com.vote.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Setter @Getter
public class Address {

    private String street;

    private String province;

    private String district;

    private String sector;

    private String village;

    @Override
    public String toString() {
        return street + ", " + province + ", " + district + ", " + sector + ", " + village;
    }
}

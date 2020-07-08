package org.reins.url.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Time_distr {
    public int time;
    public long value;

    public Time_distr(){
        value=0;
        time=0;
    }
}

package br.com.kafkamanager.infrastructure.preference;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPreference {

    @Expose
    private String kafkaServerUrl = "localhost:9092";
    @Expose
    private String themeName = "Dark";
}

package account;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccount {
    private String username;
    private String hashedPassword;
    private int wins;
    private int losses;

    // Default constructor needed for Jackson
    public UserAccount() {
    }

    public UserAccount(String username, String hashedPassword, int wins, int losses) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.wins = wins;
        this.losses = losses;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) { 
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) { 
        this.hashedPassword = hashedPassword;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public double getWinLossRatio() {
        if (losses == 0) return wins; // Avoid division by zero
        return (double) wins / losses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount)) return false;
        UserAccount that = (UserAccount) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}

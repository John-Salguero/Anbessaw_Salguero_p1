package testmodels;

public class UserAccounts {
    String username;
    String accountId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "UserAccounts{" +
                "username='" + username + '\'' +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}

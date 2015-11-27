package bubble.web.application.user;

import bubble.web.gameplay.account.Account;
import bubble.web.gameplay.user.Player;

import java.math.BigDecimal;

/**
 * @author Janusz
 */
public class Employee extends Player {

    public Employee(String name) {
        super(name);
    }

    public BigDecimal allowUserToWithdrawResources (Account account, int amout){
        if(account.getCurrentBalance().compareTo(BigDecimal.valueOf(amout))==-1){
            throw new IllegalArgumentException("Player has not enough resources to withdraw");
        }
//        account.subtractFromAccount(BigDecimal.valueOf(amout));
        return BigDecimal.valueOf(amout);
    }
}

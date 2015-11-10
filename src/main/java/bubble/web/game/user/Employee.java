package bubble.web.game.user;

import bubble.web.game.account.Account;

import java.math.BigDecimal;

/**
 * @author Janusz
 */
public class Employee extends User {

    public Employee(String name) {
        super(name);
    }

    public BigDecimal allowUserToWithdrawResources (Account account, int amout){
        if(account.getCurrentState().compareTo(BigDecimal.valueOf(amout))==-1){
            throw new IllegalArgumentException("User has not enough resources to withdraw");
        }
//        account.subtractFromAccount(BigDecimal.valueOf(amout));
        return BigDecimal.valueOf(amout);
    }
}

package com.game.service.accountservices;

import com.game.data.MessageSQLRepo;
import com.game.models.Account;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Class modifies internal attributes of accounts
 */
public class ModificationServiceImp implements ModificationService{
    AccountDetailService accountDetailService;
    static final Logger logger = Logger.getLogger(MessageSQLRepo.class);

    /**
     * requires access to accountDetailService to modify accounts
     * @param accountDetailService accountDetailService
     */
    public ModificationServiceImp(AccountDetailService accountDetailService){
        BasicConfigurator.configure();
        this.accountDetailService = accountDetailService;
    }

    /**
     * Deposits credits into account if bank account is valid
     * @param amount desired amount
     * @param user session user
     * @return true if deposit succeeds
     */
    @Override
    public boolean deposit(int amount, String user) {
        Account temp = accountDetailService.getAccount(user);
        if (temp==null||temp.getCardNumber()==null){
            logger.debug("User or card number is null");
            return false;
        }
        if (amount<=0){
            logger.debug("Can't deposit less or equal to zero");
            return false;
        }
        temp.addBalance(amount);
        accountDetailService.update(temp);
        return true;
    }

    @Override
    public boolean withdraw(int amount, String user) {
        Account temp = accountDetailService.getAccount(user);
        if(amount>temp.getBalance()||amount<=0){
            logger.debug("Invalid amount requested");
            return false;
        }
        temp.subtractBalance(amount);
        accountDetailService.update(temp);
        return true;
    }

    @Override
    public boolean changePassword(String password, String user) {
        if (!accountDetailService.passwordValidations(password)){
            logger.debug("No special characters allowed, minimum length is 8");
            return false;
        }
        Account changed = accountDetailService.getAccount(user);
        changed.setPassword(password);
        accountDetailService.update(changed);
        return true;
    }

    @Override
    public boolean changeBankAccount(String bankAccount, String user) {
        if (validCard(bankAccount)) {
            Account changed = accountDetailService.getAccount(user);
            changed.setCardNumber(bankAccount);
            accountDetailService.update(changed);
            logger.debug("account updated");
            return true;
        }
        logger.debug("account not updated");
        return false;
    }

    @Override
    public boolean validCard(String card){
        if (!(card.length()>=13&&card.length()<=16)){
            logger.debug("invalid card length: "+card.length());
            return false;
        }
        String f;
        String f2;
        f=card.substring(0,1);
        f2=card.substring(0,2);
        if (!(f.equals("4")||f.equals("5")||f.equals("6")||f2.equals("37"))){
            logger.debug("invalid card prefix");
            return false;
        }

        return oddDoubleEvenSum(card) % 10 == 0;
    }

    private int oddDoubleEvenSum(String card){
        return sumOfDoubleEvenPlace(card)+sumOfOddPlace(card);
    }

    //code repurposed from https://www.geeksforgeeks.org/program-credit-card-number-validation/
    // Get the result from Step 2
    public static int sumOfDoubleEvenPlace(String number)
    {
        int sum = 0;
        for (int i = number.length() - 2; i >= 0; i -= 2)
            sum += getDigit(Integer.parseInt(number.charAt(i) + "") * 2);
        return sum;
    }
    public static int getDigit(int number)
    {
        if (number < 9)
            return number;
        return number / 10 + number % 10;
    }

    //code repurposed from https://www.geeksforgeeks.org/program-credit-card-number-validation/
    // Return sum of odd-place digits in number
    public static int sumOfOddPlace(String number)
    {
        int sum = 0;
        for (int i = number.length() - 1; i >= 0; i -= 2)
            sum += Integer.parseInt(number.charAt(i) + "");
        return sum;
    }

}

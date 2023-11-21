package com.server;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AccountRepository {

    public static Map<Integer, Integer> AccoutBanlace = IntStream.rangeClosed(1,10)
            .boxed()
            .collect(Collectors.toMap(
                    Function.identity(),
                    v->v*10));

    public int getBalance(int accountNumber) {
        return AccoutBanlace.get(accountNumber);
    }


    public int credit(int accountNumber, int amount) {
        AccoutBanlace.put(accountNumber, amount + AccoutBanlace.get(accountNumber));
        return AccoutBanlace.get(accountNumber);
    }
    public int debit(int accountNumber, int amount) {
        if(AccoutBanlace.get(accountNumber)>=amount){
            AccoutBanlace.put(accountNumber,  AccoutBanlace.get(accountNumber)-amount);
        }
        return AccoutBanlace.get(accountNumber);

    }
}

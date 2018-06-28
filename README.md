# Zafeplace  SDK-Android

 Zafeplace  SDK is a library for simple working with some crypto currency, such as ethereum and stellar. Using the library, we can
 generate wallets, take wallet balance for coins and tokens, do transaction for translation coins and tokens, alsp we can take list with smart conract functions and 
 execute any of them. 
 
 Example
 -------
 
 You need to use functionality of the library using the object of Zafeplace class. It is main class in library, all needed methods you
 can find here. Object of Zefaplace class is created as singlton and you can use one instance in all project classes. It is created as follows - 
 
          Zafeplace zafeplace = Zafeplace.getInstance(this);
 In the parameters of the method getInstance we need to set current activity.
 We can use any method using instance of Zafeplace dot name of method. For example 
 
    zafeplace.getTokenBalance(STELLAR_WALLET, mZafeplace.getWallet(STELLAR_WALLET).getAddress(), new OnGetTokenBalance() {
                @Override
                public void onTokenBalance(List<ResultToken> tokenBalance) {
                   // take response balance 
                }

                @Override
                public void onErrorTokenBalance(Throwable error) {
                   // handle error 
                 }
            });
            
  Here we send into method type of currency, address of our wallet, and callback for taking result, because all methods of library is asynchronous. Also we can login for user authentication and using some methods like creating transactions and generating wallets.
  You can use fingerprint login and pin code login. For example - pin code login:
      
       zafeplace.pinCodeLogin(String pincode);
   fingerprint login:
   
        zafeplace.fingerprintLogin(FingerprintAuthenticationCallback fingerprintAuthenticationCallback);
             
       
  
  Methods
  -------
  
| Name        | Description           | 
| ------------- |:-------------:| 
|  getInstance(Activity)     |  create instance of class Zafeplace.  | 
| generateWallet(WalletTypes, OnWalletGenerateListener) | Generate wallet for the specified type. | 
| getWalletBalance(WalletTypes, String, OnGetWalletBalance) | Get balance of wallet for the specified type and address |
| createTransaction(WalletTypes, String, String, int, OnGetWalletBalance) | Create transaction for translation coins for specified type, address sender and address recipient |
| createTransactionToken(WalletTypes, String, String, double, OnGetWalletBalance) | Create transaction for translation tokens for specified type, address sender and address recipient |
| getTokenBalance(WalletTypes, String, OnGetTokenBalance) | Get balance tokens for wallet for  specified type and address |
| getWalletBalance(WalletTypes, String, OnGetTokenBalance) |  Get balance coins for wallet for  specified type and address |
| getSmartContractTransactionRaw(OnSmartContractRawList) |  Get list of smart contracts for ethereum currency |
| executeSmartContractMethod(String, String, List<MethodParamsSmart>, OnExecuteMethodSmartContract) | Execute one of methods from list smart contracts for ethereum currency |
| generateAccessToken(String, String, OnAccessTokenListener) | generate access token for authentication user  |
| fingerprintLogin(FingerprintAuthenticationCallback) | method for login user with fingerprint |
| pinCodeLogin(String) |  method for login user with pin code |
|isLoggedIn()| method for checking is user is signed in |
| getAuthType() |  method for checking type of authentication, fingerprint or pin code |

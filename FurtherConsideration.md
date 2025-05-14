Create a machine learning algorithm to predict whether the user will get the loan or not. This is mostly needed for the admin profile views

# Business Case
- To build a neural network or machine learning model that predicts whether a user should be approved for a loan, you'll need to collect data that reflects their creditworthiness, financial stability, and ability to repay.

## Personal and Demographic
- age (derived from dateOfBirth)
- employmentStatus (Employed, Unemployed, Self-employed, etc.)
- annualIncome
- nationalId (used only to fetch external credit info, not used directly in ML)
- address or region (optional — for regional lending risk or geospatial patterns)

## Loan Application Details
- requestedAmount
- termMonths (duration)
- purpose (converted to categorical features)
- applicationDate (to extract temporal trends, e.g. seasonality)
- uploadedDocumentsCount or quality rating (optional)

## Financial Behavior (engineered features from the data)
- account.balance
- averageMonthlyDeposit
- averageMonthlyWithdrawal
- transactionCountLast3Months
- loanCount (number of previous loans requested)
- pastLoanDefault (whether user ever defaulted or was late)
- repaymentConsistency (e.g., % of on-time payments)

## Derived Ratios & Indicators
- debtToIncomeRatio = requestedAmount / annualIncome
- loanToBalanceRatio = requestedAmount / account.balance
- incomeStabilityScore (standard deviation of monthly income)
- creditUtilization if using a simulated credit score

-- Credit score (from external API like Schufa in Germany)
-- 
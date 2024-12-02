# transaction-processing-service

## General info
Used to process transactions and attempt to detect any fraudulent activity.
For example:
● If a user has created more than 10 transactions within the last 1 min.
● If two user transactions are created from different places on Earth within the last 30 min and the
distance between those places is more than 300 kilometres.
● Suppose a transaction is created within the territory of a blacklisted country. You can assume that
you already have the blacklisted countries stored in the system database.
● If a user has created transactions in 3 different countries within the last 10 minutes.
# Take-home-backend (Task 2)

## Notes
The choice of database is TimescaleDB, which is built on top of PostgresQL. 

Chosen mainly for its optimizations for time-series data such as time-based 
indexing and time-series function (for task 3) + my familiarity with PostgresQL.

To set up on your local machine, you can follow their guide https://docs.timescale.com/self-hosted/latest/install/installation-macos/.

I have not automated the setup of the database needed to run this. Please create a Postgres DB called `assignment` and 
install the timescaledb extension as per the guide above before running the app, otherwise it will fail on the Flyway 
migration.


### Update frequency to retrieve data from API
Set to default value of `5000ms`

You can edit this in `application.properties`, under the key `get-exchange-rates-frequency-ms` 
## Stocks

**Brief Investigations**: via Stock Prices


### Running

* Case: local <br>
    ```
    spark-submit 
    --class com.grey.StocksApp 
    --master local[*] 
    target/stocks-...-jar-with-dependencies.jar https://raw.githubusercontent.com/briefings/
       stocks/develop/arguments.yaml
    ```
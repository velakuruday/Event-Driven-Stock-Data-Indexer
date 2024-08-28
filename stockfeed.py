import json
from datetime import datetime, timedelta

import yfinance as yf
from kafka import KafkaProducer


class Stock:
    def __init__(self):
        pass

    def to_dict(self):
        return self.__dict__


class Update:
    def __init__(self, timestamp, stock_list):
        self.timestamp = str(timestamp)
        self.stock_list = stock_list

    def to_dict(self):
        return {
            'timestamp': self.timestamp,
            'stock_list': [stock.to_dict() for stock in self.stock_list]
        }


producer = KafkaProducer(bootstrap_servers=['localhost:9094'], value_serializer=lambda v: json.dumps(v).encode('utf-8'),
                         acks='all', retries=10)

end_time = datetime.now()
start_time = end_time - timedelta(days=7)
tickers = ['AAPL', 'MSFT', 'GOOGL', 'PEP', 'KO']

stock_data = yf.download(tickers=tickers, interval='5m', start=start_time, end=end_time)
data_list = list(stock_data.itertuples(index=True, name=None))
field_dict = {'Adj Close': 'adj_close', 'Close': 'close', 'High': 'high',
              'Low': 'low', 'Open': 'open', 'Volume': 'volume'}

for data in data_list:
    stock_list, i = [], 0

    for i in range(len(tickers)):
        obj = Stock()
        setattr(obj, 'name', stock_data.columns[i][1] if len(tickers) > 1 else tickers[0])

        for j in range(i, len(stock_data.columns), len(tickers)):
            setattr(obj,
                    field_dict[stock_data.columns[j][0]] if len(tickers) > 1 else field_dict[stock_data.columns[j]],
                    data[1:][j])
        stock_list.append(obj)

    update = Update(data[0], stock_list)
    producer.send("stock-updates", update.to_dict())
    print("Produced to topic...")
    producer.flush(timeout=30)

producer.close()
